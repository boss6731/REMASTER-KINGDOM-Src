package l1j.server.server.server.clientpackets;

import java.sql.Timestamp;


import MJShiftObject.Object.MJShiftObject;
import l1j.server.Config;
import l1j.server.MJBookQuestSystem.Loader.BQSCharacterDataLoader;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;

import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CommonNews;
import l1j.server.server.serverpackets.S_DeleteCharOK;

public class C_DeleteChar extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_DELETE_CHAR = "[C] RequestDeleteChar";
	private static final int DELETE_TYPE_DELETE = 0;
	private static final int DELETE_TYPE_RECOVERY = 1;

	public C_DeleteChar(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);
		String name = readS();
		int type = readC();
		try {
			if (Config.Login.UseShiftServer) {
				MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_sender_object_from_account(client.getAccountName());
				if (sobject != null) {
					System.out.println(String.format("【帳號】%s 【參加中】%s 【刪除】%s 【%s】【IP】%s 嘗試從參與對抗戰的帳號中刪除角色。", client.getAccountName(), sobject.get_source_character_name(), name,
							MJNSHandler.getLocalTime(), client.getIp()));
					SC_CUSTOM_MSGBOX.do_kick(client, String.format("由於 %s 正在參與對抗戰，無法刪除角色。", sobject.get_source_character_name()));
					return;
				}
			}

			L1PcInstance pc = CharacterTable.getInstance().restoreCharacter(name);
			if (pc == null) {
				client.sendPacket(new S_CommonNews("不存在的角色。"));
				return;
			}

			for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) {
				if (target.getId() == pc.getId()) {
					client.sendPacket(new S_CommonNews("無法刪除正在連線的角色。"));
					return;
				}
			}
			if (type == DELETE_TYPE_RECOVERY) {
				onRecovery(pc, client);
			} else if (type == DELETE_TYPE_DELETE) {
				if (pc.getType() >= 32) {
					client.sendPacket(new S_CommonNews("狀態錯誤。(刪除)"));
					return;
				}
				CharacterTable.getInstance().restoreInventory(pc);
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					if (item.getBless() >= 128) {
						client.sendPacket(new S_CommonNews("擁有封印物品的角色無法刪除。"));
						return;
					}
				}
				if (pc.getLevel() > 30 && Config.ServerAdSetting.DELETECHARACTERAFTER7DAYS) {
					onDelete7Days(pc, client);
				} else {
					onDeleteNow(pc, client);
				}
			} else {
				String s = String.format("未知的刪除命令。name:%s code:%d", name, type);
				client.sendPacket(new S_CommonNews(s));
				System.out.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
			client.close();
		}
	}

	private void onRecovery(L1PcInstance pc, GameClient client) {
		if (pc.getType() < 32) {
			client.sendPacket(new S_CommonNews("狀態錯誤。(恢復)"));
			return;
		}

		if (pc.isCrown()) {
			pc.setType(0);
		} else if (pc.isKnight()) {
			pc.setType(1);
		} else if (pc.isElf()) {
			pc.setType(2);
		} else if (pc.isWizard()) {
			pc.setType(3);
		} else if (pc.isDarkelf()) {
			pc.setType(4);
		} else if (pc.isDragonknight()) {
			pc.setType(5);
		} else if (pc.isBlackwizard()) {
			pc.setType(6);
		} else if (pc.isWarrior()) {
			pc.setType(7);
		} else if (pc.isFencer()) {
			pc.setType(8);
		} else if (pc.isLancer()) {
			pc.setType(9);
		}
		pc.setDeleteTime(null);
		pc.save();
		client.sendPacket(new S_DeleteCharOK(S_DeleteCharOK.DELETE_CHAR_AFTER_7DAYS));
	}

	private void onDelete7Days(L1PcInstance pc, GameClient client) {
		if (pc.isCrown()) {
			pc.setType(32);
		} else if (pc.isKnight()) {
			pc.setType(33);
		} else if (pc.isElf()) {
			pc.setType(34);
		} else if (pc.isWizard()) {
			pc.setType(35);
		} else if (pc.isDarkelf()) {
			pc.setType(36);
		} else if (pc.isDragonknight()) {
			pc.setType(37);
		} else if (pc.isBlackwizard()) {
			pc.setType(38);
		} else if (pc.isWarrior()) {
			pc.setType(39);
		} else if (pc.isFencer()) {
			pc.setType(40);
		} else if (pc.isLancer()) {
			pc.setType(41);		
		}
		Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + (604800 * 1000L));
		pc.setDeleteTime(deleteTime);
		pc.save();
		client.sendPacket(S_DeleteCharOK.deleteRemainSeconds(604800));
	}

	private void onDeleteNow(L1PcInstance pc, GameClient client) throws Exception {
		CharacterTable.getInstance().deleteCharacter(client.getAccountName(), pc.getName());
		BQSCharacterDataLoader.deleteCharacterBps(pc, true);
		client.sendPacket(new S_DeleteCharOK(S_DeleteCharOK.DELETE_CHAR_NOW));
	}

	@Override
	public String getType() {
		return C_DELETE_CHAR;
	}

}
