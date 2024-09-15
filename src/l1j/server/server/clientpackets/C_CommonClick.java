package l1j.server.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetServer.MJClientEntranceService;
import l1j.server.MJRankSystem.Business.MJRankBusiness;

import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_CharPacks;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_PssConfig;
import l1j.server.server.serverpackets.S_Unknown2;
import l1j.server.server.utils.SQLUtil;

public class C_CommonClick {
	private static final String C_COMMON_CLICK = "[C] C_CommonClick";

	public C_CommonClick(final GameClient client) {
		if (client == null || client.getAccount() == null)
			return;

		deleteCharacter(client);

		if (!MJClientEntranceService.service().useWaitQueue()) {
			onCharListEnter(client);
			return;
		}

		MJClientEntranceService.service().offer(client, new Runnable() {
			@Override
			public void run() {
				onCharListEnter(client);
			}
		});
	}

	private static void onCharListEnter(final GameClient client) {
		try {
			int amountOfChars = client.getAccount().countCharacters();
			int slot = client.getAccount().getCharSlot();

			if (Config.Login.CharPassword && (client.getAccount().getCPW() == null || client.getAccount().getCPW().equalsIgnoreCase("")))
				client.sendPacket(new S_CharPass(S_CharPass._CHARACTER_SELECTION_SCREEN_ENTRY3));

			client.sendPacket(l1j.server.server.serverpackets.S_PssConfig.CONFIG);

			client.sendPacket(new S_CharAmount(amountOfChars, slot));
			client.sendPacket(new S_CharPass(S_CharPass._CHARACTER_SELECTION_SCREEN_ENTRY2));
			if (amountOfChars > 0) {
				sendCharPacks(client);
			}
			client.sendPacket(new S_CharPass(S_CharPass._CHARACTER_SELECTION_SCREEN_ENTRY));

			client.getAccount().updateTAMPoints(client.getAccount());
			client.sendPacket(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, client));
			client.sendPacket(new S_Unknown2(0)); // 處理登錄時的未知數據
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendCharPacks(GameClient client) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn.prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY objid");
			pstm.setString(1, client.getAccountName());
			rs = pstm.executeQuery();
			S_CharPacks cpk = null;
			while (rs.next()) {
				String name = rs.getString("char_name");
				String clanname = rs.getString("Clanname");
				int type = rs.getInt("Type");
				byte sex = rs.getByte("Sex");
				int lawful = rs.getInt("Lawful");

				int currenthp = rs.getInt("CurHp");
				// if (currenthp < 1) {
				// currenthp = 1;
				// } else if (currenthp > 32767) {
				// currenthp = 32767;
				// }

				int currentmp = rs.getInt("CurMp");
				// if (currentmp < 1) {
				// currentmp = 1;
				// } else if (currentmp > 32767) {
				// currentmp = 32767;
				// }

				int lvl;
				if (Config.Login.CharacterConfigInServerSide) {
					lvl = rs.getInt("level");
					if (lvl < 1) {
						lvl = 1;
					} else if (lvl > 127) {
						lvl = 127;
					}
				} else {
					lvl = 1;
				}

				int ac = rs.getInt("Ac");
				// if (rs.getInt("Ac") < -128) {
				// ac = -128;
				// } else {
				// ac = rs.getInt("Ac");
				// //// System.out.println("[AC錯誤/錯誤] : 帳號: " + client.getAccountName() + " / 角色名: " + name + " AC: " + ac);
				// }

				int str = rs.getByte("Str");
				int dex = rs.getByte("Dex");
				int con = rs.getByte("Con");
				int wis = rs.getByte("Wis");
				int cha = rs.getByte("Cha");
				int intel = rs.getByte("Intel");
				int accessLevel = rs.getShort("AccessLevel");
				int birth = rs.getInt("BirthDay");
				Timestamp deleteStamp = rs.getTimestamp("DeleteTime");

				/** MJRankSystem **/
				MJRankBusiness.getInstance().noti(client, rs.getInt("objid"));

				cpk = new S_CharPacks(name, clanname, type, sex, lawful, currenthp, currentmp, ac, lvl, str, dex, con, wis, cha, intel, accessLevel, birth,
						deleteStamp == null ? 0 : (int) ((deleteStamp.getTime() - System.currentTimeMillis()) / 1000));
				client.sendPacket(cpk);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(conn);
		}
	}

	private void deleteCharacter(GameClient client) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn.prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY objid");
			pstm.setString(1, client.getAccountName());
			rs = pstm.executeQuery();
			Timestamp deleteTime = null;
			Calendar cal = null;
			L1Clan clan = null;
			while (rs.next()) {
				String name = rs.getString("char_name");
				String clanname = rs.getString("Clanname");

				deleteTime = rs.getTimestamp("DeleteTime");
				if (deleteTime != null) {
					cal = Calendar.getInstance();
					long checkDeleteTime = ((cal.getTimeInMillis() - deleteTime.getTime()) / 1000) / 3600;
					if (checkDeleteTime >= 0) {
						clan = L1World.getInstance().findClan(clanname);
						if (clan != null) {
							clan.removeClanMember(name);
						}
						CharacterTable.getInstance().deleteCharacter(client.getAccountName(), name);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(conn);
		}
	}

	public String getType() {
		return C_COMMON_CLICK;
	}
}