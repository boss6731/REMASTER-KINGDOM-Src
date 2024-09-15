package l1j.server.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.server.Account;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.server.datatables.ReportTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChangeCharName;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.server.serverpackets.S_CharacterCreation;
import l1j.server.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1ItemBookMark;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class C_Report extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_REPORT = "[C] C_Report";

	public static final int DragonMenu = 0x06;

	public static final int MINI_MAP_SEND = 0x0b;
	/** 怪物擊殺 */
	public static final int MonsterKill = 0x2c;

	// 不知道什麼時候用，但點擊或按 tab 時會顯示
	//    public static final int TEST = 0x0D; //13

	/** 主頁連動圖標 **/
	// public static final int HTTP = 0x13;
	// public static final int 페어리 = 0x37;

	public static final int BOOKMARK_SAVE = 0x22;
	public static final int BOOKMARK_COLOR = 0x27;
	public static final int BOOKMARK_LOADING_SAVE = 0x28;
	public static final int EMBLEM = 0x2e; // 紋章顯示
	public static final int TELEPORT = 0x30; // 村莊傳送
	public static final int CHARACTER_CREATION = 0x2b; // 角色創建
	public static final int POWER_BOOK_SEARCH = 0x13; // 功能書搜索
	public static final int MERCHANT_SEARCH = 0x31; // 商人查找
	public static final int SHOP_SETUP_COUNT = 0x39; // 開店次數
	public static final int AUTO_REPORT = 0x00; // 自動舉報
	public static final int PRICE_SEARCH = 0xff; // 價格搜索
	public static final int CHARACTER_PASSWORD_CREATION = 0x0e; // 角色密碼創建
	public static final int CHARACTER_PASSWORD_CHANGE = 0x10; // 角色密碼變更
	public static final int CHARACTER_PASSWORD_AUTH = 0x11; // 角色密碼認證

	public static final int CHARNAME_CHANGED = 0x1A;

	public C_Report(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		int type = readC();
		L1PcInstance pc = client.getActiveChar();
		
//		System.out.println("C_Report 類型: " + type);
		
		switch (type) {
			case CHARACTER_PASSWORD_CREATION: {
				if (!Config.Login.CharPassword)
					return;
				String password = readSecondPassword();

				/** 密碼格式不正確時踢出 **/
				if (password == null) {
					System.out.println("kick1");
					client.kick();
					return;
				}

				/** 如果已經有二次密碼的帳戶再次發送創建封包，則踢出。 **/
				if (client.getAccount().getCPW() != null && !client.getAccount().getCPW().equalsIgnoreCase("")) {
					System.out.println("kick2");
					client.kick();
					return;
				} else {
					client.getAccount().setCPW(password);
					client.getAccount().UpdateCharPassword(password);
					client.sendPacket(new S_CharPass(S_CharPass._PASSWORD_CREATION_COMPLETE_WINDOW));
				}
			}
			break;
			case CHARACTER_PASSWORD_CHANGE: {
				if (!Config.Login.CharPassword)
					return;

				String password = readSecondPassword();
				readC(); // is null
				String modPassword = readSecondPassword();

			/** 未知格式，踢出。 **/
				if (password == null || modPassword == null) {
					client.kick();
					return;
				}

			if (client.getAccount().getCPW() == null && client.getAccount().getCPW().equalsIgnoreCase("")) {
				return;
			} else {
				if (client.getAccount().getCPW().equals(password)) {
					client.getAccount().setCPW(modPassword);
					client.getAccount().UpdateCharPassword(modPassword);
					client.sendPacket(new S_CharPass(S_CharPass._PASSWORD_CHANGE_RESPONSE, true));
				} else {
					client.sendPacket(new S_CharPass(S_CharPass._PASSWORD_CHANGE_RESPONSE, false));
				}
			}
			break;
		}
			case CHARACTER_PASSWORD_AUTH: {
				if (!Config.Login.CharPassword)
					return;

				String password = readSecondPassword();
				/** 未知格式 **/
				if (password == null) {
					client.kick();
					return;
				}

			if (client.getAccount().getCPW() == null && client.getAccount().getCPW().equalsIgnoreCase("")) {
				return;
			}

			if (client.getAccount().getCPW().equals(password)) {
				client.reset_second_password_failure_count();
				if (client.getAccount().getwaitpacket() != null) {
					int op = client.getAccount().getwaitpacket()[0] & 0xff;
					if (op == Opcodes.C_ENTER_WORLD)
						new C_LoginToServer(client.getAccount().getwaitpacket(), client);
					else if (op == Opcodes.C_DELETE_CHARACTER)
						new C_DeleteChar(client.getAccount().getwaitpacket(), client);
					else
						System.out.println("invalid wait packet : " + op);
				}
			} else {
				int failure_count = client.inc_second_password_failure_count();
				if (failure_count >= Config.Login.CharPasswordMaximumFailureCount) {
					client.kick();
					client.close();
					return;
				}
				client.sendPacket(S_CharPass.do_fail_password(failure_count, Config.Login.CharPasswordMaximumFailureCount));
			}
		}
			break;
			case CHARACTER_CREATION: {
				if (Config.Login.CharPassword
						&& (client.getAccount().getCPW() == null || client.getAccount().getCPW().equalsIgnoreCase(""))) {
					client.sendPacket(new S_CharPass(S_CharPass._PASSWORD_CREATION_WINDOW));
				} else {
					client.sendPacket(new S_CharacterCreation());
				}
			}

			break;
			case AUTO_REPORT: {
				int objid = readD(); // 角色物件
				L1Object obj = L1World.getInstance().findObject(objid); // 錯誤附近
				if (!(obj instanceof L1PcInstance)) {
					return;
				}
				L1PcInstance target = (L1PcInstance) obj; // 錯誤附近
				if (target == null || target.isGm()) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("舉報對象不存在。")));
					return;
				}
				if (!pc.isReport()) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1021))); // 請稍後再次舉報。
					return;
				}
				ReportTable rt = ReportTable.getInstance();
				if (rt.isReport(target.getName())) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1020))); // 已經註冊。
					return;
				}
				Timestamp date = new Timestamp(System.currentTimeMillis());
				rt.reportUpdate(target.getName(), pc.getName(), date);
				pc.sendPackets(String.valueOf(new S_ServerMessage(1019))); // 已註冊。
				pc.startReportDeley();
			}
			break;
			case PRICE_SEARCH:
				String itemname = readS();
				int shopitemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(itemname);

				if (shopitemid == 0) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("無法搜尋到物品名稱。請重新輸入搜尋詞。")));
					return;
				}

				break;
			case FIND_MERCHANT: {
			/** 2016.11.26 MJ App Center LFC **/
				String name = readS();
				if (name == null)
					return;
				try {
					String[] rep = name.split("-");
					if (rep.length >= 2) {
						if (rep[0].equalsIgnoreCase("LFC")) {
							pc.sendPackets(".請使用決鬥指令。");
							break;
						}
					}
				if (pc.getMapId() == 800) {
					Random rnd = new Random(System.nanoTime());
					L1PcInstance pn = L1World.getInstance().getPlayer(name);
					if (pn != null && pn.getMapId() == 800 && pn.isPrivateShop()) {
						/** 2016.12.01 MJ App Center LFC **/
						pc.setFindMerchantId(pn.getId());
						pc.start_teleport(pn.getX() + CommonUtil.random(3) - 1, pn.getY() + CommonUtil.random(3) - 1,
								pn.getMapId(), 0, 18339, false, false);
					} else {
						L1NpcShopInstance nn = L1World.getInstance().getShopNpc(name);

						if (nn != null && nn.getMapId() == 800 && nn.getState() == 1) {
							/** 2016.12.01 MJ App Center LFC **/
							pc.setFindMerchantId(nn.getId());
							pc.start_teleport(nn.getX() + CommonUtil.random(3) - 1,
									nn.getY() + CommonUtil.random(3) - 1, nn.getMapId(), 0, 18339, false, false);
							/** 2016.11.24 MJ App Center Price Search **/
						} else {
							pc.sendPackets(new S_SystemMessage("找商人：找不到您要找的商人。"), true);
						}
					}
					rnd = null;
				}
			} catch (Exception e) {
			}
		}
			break;
			case SHOP_OPEN_COUNT:
				if (pc.getNetConnection() == null || pc.getNetConnection().getAccount() == null)
					return;
				pc.sendPackets(new S_PacketBox(S_PacketBox.SHOP_OPEN_COUNT, pc.getNetConnection().getAccount().Shop_open_count),
						true);
			break;
		case BOOKMARK_COLOR:
			int sizeColor = readD();
			int Numid;
			String name;
			Connection con = null;
			PreparedStatement pstm = null;
			try {
				if (sizeColor != 0) {
					con = L1DatabaseFactory.getInstance().getConnection();
				}
				for (int i = 0; i < sizeColor; i++) {
					Numid = readD();
					int id = 0;
					for (L1BookMark book : pc.getBookMarkArray()) {
						if (book.getNumId() == Numid) {
							id = book.getId();
						}
					}
					name = readS();
					name = name.replace("\\", "\\\\");
					pstm = con.prepareStatement("UPDATE character_teleport SET name='" + name + "' WHERE id='" + id + "'");
					pstm.execute();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
			break;
		case BOOKMARK_SAVE:
			if(pc == null) {
				return;
			}
			readC();
			int num;
			int size = pc._bookmarks.size();
			for (int i = 0; i < size; i++) {
				num = readC();
				pc._bookmarks.get(i).setTemp_id(num);
			}
			pc._speedbookmarks.clear();
			for (int i = 0; i < 5; i++) {
				num = readC();
				if (num == 255)
					return;
				if (pc._bookmarks.size() - 1 < num) {
					System.out.println("書籤大小錯誤 " + pc.getName() + " 數量=  " + " 大小=  " + pc._bookmarks.size());
					return;
				}
				pc._bookmarks.get(num).setSpeed_id(i);
				pc._speedbookmarks.add(pc._bookmarks.get(num));
			}
			break;
		case BOOKMARK_LOADING_SAVE: {
			if (pc.getBookMarkSize() <= 0) {
				pc.sendPackets(new S_ServerMessage(2963));
				return;
			}
			int totalCount = pc.getInventory().getSize();
//			if (pc.getInventory().getWeight100() > 82 || totalCount > 180) {
			if (pc.getInventory().getWeight100() > 82 || totalCount >= 200) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("背包已滿，無法生成記憶寶珠。")));
				return;
			}
			int itemId = readD();
			L1ItemInstance useItem = pc.getInventory().getItem(itemId);
			this.saveMemoryOrb(pc, useItem);
		}
		break;
			case POWER_BOOK_SEARCH:
				// Domain="http://g.lineage.power.plaync.com/wiki/Sand_Worm";
			break;
		case MonsterKill:
			pc.setMonsterkill(0);
			break;
		/*case TEST:
			System.out.println("??????");
			break;*/
		case EMBLEM:
			if (pc.getClanRank() != 4 && pc.getClanRank() != 10) {
				return;
			}
			int emblemStatus = readC();
			L1Clan clan = pc.getClan();
			clan.setEmblemStatus(emblemStatus);
			ClanTable.getInstance().updateClan(clan);

			for (L1PcInstance member : clan.getOnlineClanMember()) {
				member.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, emblemStatus));
			}
			break;
		case TELPORT:
			int mapIndex = readH();
			int point = readH();
			int locx = 0;
			int locy = 0;
			if (mapIndex == 1) {// 아덴
				if (point == 0) {
					locx = 34079 + (int) (Math.random() * 12);
					locy = 33136 + (int) (Math.random() * 15);
				} else if (point == 1) {
					locx = 33970 + (int) (Math.random() * 10);
					locy = 33243 + (int) (Math.random() * 14);
				} else if (point == 2) {
					locx = 33925 + (int) (Math.random() * 14);
					locy = 33351 + (int) (Math.random() * 9);
				}
			} else if (mapIndex == 2) {// 글루딘
				if (point == 0) {
					locx = 32615 + (int) (Math.random() * 11);
					locy = 32719 + (int) (Math.random() * 7);
				} else if (point == 1) {
					locx = 32621 + (int) (Math.random() * 9);
					locy = 32788 + (int) (Math.random() * 13);
				}
			} else if (mapIndex == 3) {// 기란마을
				if (point == 0) {
					locx = 33501 + (int) (Math.random() * 11);
					locy = 32765 + (int) (Math.random() * 9);
				} else if (point == 1) {
					locx = 33440 + (int) (Math.random() * 11);
					locy = 32784 + (int) (Math.random() * 11);
				}
			} else if (mapIndex == 4) {// 기란시장
				if (point == 0) {
					locx = 32844 + (int) (Math.random() * 2);
					locy = 32883 + (int) (Math.random() * 2);
				} else if (point == 1) {
					locx = 32801 + (int) (Math.random() * 2);
					locy = 32882 + (int) (Math.random() * 2);
				} else if (point == 2) {
					locx = 32756 + (int) (Math.random() * 2);
					locy = 32882 + (int) (Math.random() * 2);
				} else if (point == 3) {
					locx = 32743 + (int) (Math.random() * 2);
					locy = 32927 + (int) (Math.random() * 2);
				} else if (point == 4) {
					locx = 32740 + (int) (Math.random() * 2);
					locy = 32972 + (int) (Math.random() * 2);
				} else if (point == 5) {
					locx = 32800 + (int) (Math.random() * 2);
					locy = 32971 + (int) (Math.random() * 2);
				} else if (point == 6) {
					locx = 32844 + (int) (Math.random() * 2);
					locy = 32971 + (int) (Math.random() * 2);
				} else if (point == 7) {
					locx = 32846 + (int) (Math.random() * 2);
					locy = 32928 + (int) (Math.random() * 2);
				} else if (point == 8) {
					locx = 32797 + (int) (Math.random() * 2);
					locy = 32927 + (int) (Math.random() * 2);
				}
			} else if (mapIndex == 5) {// 말하는 섬
				if (point == 0) {
					locx = 32577 + (int) (Math.random() * 11);
					locy = 32933 + (int) (Math.random() * 7);
				} else if (point == 1) {
					locx = 32629 + (int) (Math.random() * 9);
					locy = 32957 + (int) (Math.random() * 13);
				}
			}
			pc.start_teleport(locx, locy, pc.getMapId(), pc.getHeading(), 18339, true, false);
			pc.sendPackets(new S_PacketBox(S_PacketBox.TOWN_TELEPORT, pc));
			break;

		case DragonMenu:
			break;
		case MINI_MAP_SEND:
			String targetName = null;
			int mapid = 0, x = 0, y = 0, Mid = 0;
			try {
				targetName = readS();
				mapid = readH();
				x = readH();
				y = readH();
				Mid = readH();
			} catch (Exception e) {
				return;
			}
			L1PcInstance target = L1World.getInstance().getPlayer(targetName);
			if (target == null)
				pc.sendPackets(new S_ServerMessage(1782));
			else if (pc == target)
				pc.sendPackets(new S_ServerMessage(1785));
			else {
				target.sendPackets(new S_ServerMessage(1784, pc.getName()));
				target.sendPackets(new S_PacketBox(S_PacketBox.MINI_MAP_SEND, pc.getName(), mapid, x, y, Mid));
				pc.sendPackets(new S_ServerMessage(1783, target.getName()));
			}
			break;
		case CHARNAME_CHANGED:
			client.setStatus(MJClientStatus.CLNT_STS_CHANGENAME);
			String sourceName = readS();
			String destinationName = readS();
			if (!client.is_shift_transfer()) {
				if (/*!CharacterTable.getInstance().isContainNameList(sourceName)
						|| */client.getStatus().toInt() != MJClientStatus.CLNT_STS_CHANGENAME.toInt()) {
					client.close();
					return;
				}
			}
			ServerBasePacket packet = S_ChangeCharName.doChangeCharName(client, sourceName, destinationName, true);
			if (packet != null)
				client.sendPacket(packet);
			Account acc = client.getAccount();
			client.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
			if (acc.countCharacters() > 0)
				C_CommonClick.sendCharPacks(client);
			
			client.setStatus(MJClientStatus.CLNT_STS_AUTHLOGIN);
			break;
		default:
			break;
		}
	}

	private void saveMemoryOrb(L1PcInstance pc, L1ItemInstance useItem) {
		ArrayList<L1BookMark> books = pc._bookmarks;
		L1ItemInstance item = ItemTable.getInstance().createItem(700023);
		for (int i = 0; i < books.size(); i++) {
			L1ItemBookMark.addBookmark(pc, item, books.get(i));
		}
		pc.getInventory().storeItem(item);
		pc.getInventory().removeItem(useItem, 1);
	}

	private String readSecondPassword() {
		StringBuilder sb = new StringBuilder();
		int size = readC();
		if (size > 8)
			size = 8;
		int num = 0;
		for (int i = 0; i < size; i++) {
			num = readC();
			if (num < 0 || num > 9) {
				return null;
			}
			sb.append(String.valueOf(num));
		}
		return sb.toString();
	}

	@Override
	public String getType() {
		return C_REPORT;
	}
}