package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WHOUSER_NOTI_PACKET;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

public class C_Who extends ClientBasePacket {

	private static final String C_WHO = "[C] C_Who";

	private void onViewDetail(L1PcInstance gm, String name) {
		L1PcInstance pc = L1World.getInstance().getPlayer(name);
		SC_WHOUSER_NOTI_PACKET noti = SC_WHOUSER_NOTI_PACKET.newInstance();
		noti.set_currentusercount(L1World.getInstance().getAllPlayersSize());
		if (pc != null) {
			SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO uInfo = SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO.newInstance();
			SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO aInfo = SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO.newInstance();
			aInfo.set_accountname(pc.getAccountName());
			if (pc.getNetConnection() != null) {
				aInfo.set_ip(pc.getNetConnection().getIp());
				aInfo.set_ipkind(pc.getNetConnection().getIpBigEndian());
			} else {
				aInfo.set_ip("0.0.0.0");
				aInfo.set_ipkind(0);
			}
			aInfo.set_location(pc.getLongLocationReverse());
			aInfo.set_worldnumber(pc.getMapId());
			uInfo.set_accountinfo(aInfo);
			uInfo.set_alignstr(pc.getLawful() > 0 ? "Lawful" : pc.getLawful() < 0 ? "Chaotic" : "Natural");
			String clanname = pc.getClanname();
			uInfo.set_pledge(MJString.isNullOrEmpty(clanname) ? MJString.EmptyString : clanname);
			uInfo.set_serverno(0);
			uInfo.set_title(pc.getTitle());
			uInfo.set_username(pc.getName());
			noti.add_whouserinfo(uInfo);

			SC_WHOUSER_NOTI_PACKET.HuntingMapInfo mInfo = SC_WHOUSER_NOTI_PACKET.HuntingMapInfo.newInstance();
			// mInfo.set_map_desc(pc.getMapId());
			mInfo.set_map_desc(445);
			mInfo.set_user_count(L1World.getInstance().getVisiblePlayers(pc.getMapId()).size());
			noti.add_hunting_map_info(mInfo);
		}
		gm.sendPackets(noti, MJEProtoMessages.SC_WHOUSER_NOTI_PACKET);
	}

	public C_Who(byte[] decrypt, GameClient client) {
		super(decrypt);
		String s = readS();

		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;

		if (pc.isGm() && !s.equalsIgnoreCase("")) {
			onViewDetail(pc, s);
			return;
		}

		L1PcInstance find = L1World.getInstance().getPlayer(s);

		float win = 0;
		float lose = 0;
		float total = 0;
		float winner = 0;
		String NameGm = "梅蒂斯";
		String NameGm1 = "微笑菲亞";
		String NameGm2 = "卡西歐佩亞";

		if (s.contentEquals(NameGm)) {
			pc.sendPackets("\aA" + NameGm + " \fW(Lawful) \\aH請發送 KakaoTalk 消息到:[" + Config.Message.GMKAKAO + "]! \n\r\f2Kill: 1 / \\aGDeath: 0 / \\aI[100.00%]");
			return;
		} else if (s.contentEquals(NameGm1)) {
			pc.sendPackets("\aA" + NameGm1 + " \fW(Lawful) \\aH請發送 KakaoTalk 消息到:[" + Config.Message.GMKAKAO + "]! \n\r\f2Kill: 1 / \\aGDeath: 0 / \\aI[100.00%]");
			return;
		} else if (s.contentEquals(NameGm2)) {
			pc.sendPackets("\aA" + NameGm2 + " \fW(Lawful) \\aH請發送 KakaoTalk 消息到:[" + Config.Message.GMKAKAO + "]! \n\r\f2Kill: 1 / \\aGDeath: 0 / \\aI[100.00%]");
			return;
		}

		/*
		 * if (find != null) { String clanname = find.getClanname(); String lawful = ""; String wanted = "[OFF]"; if (find.hasSkillEffect(L1SkillId.USER_WANTED)) wanted = "[ON]";
		 * 
		 * if (find.getKDA() != null) { win = find.getKDA().kill; lose = find.getKDA().death; }
		 * 
		 * String title = ""; if (find.getTitle().equalsIgnoreCase("") == false) { title = find.getTitle() + " "; }
		 * 
		 * total = win + lose; if(total > 0) winner = ((win * 100) / (total));
		 * 
		 * if (find.getClan() != null) { if (find.getLawful() > 0) lawful = "\\fW(Lawful)"; else lawful = "\\fY(Chaotic)"; pc.sendPackets(String.format("\\aA%s%s \\aE[%s] \n\r \\f2Kill: %d / \\aGDeath: %d / \\aI[%.2f%%]", find.getName(), lawful, clanname,
		 * (int)win, (int)lose, winner)); } else { if (find.getLawful() > 0) lawful = "\\fW(Lawful)"; else lawful = "\\fY(Chaotic)"; pc.sendPackets(String.format("\\aA%s%s \\aD%s \n\r \\f2Kill: %d / \\aGDeath: %d / \\aI[%.2f%%]", find.getName(), lawful, title,
		 * (int)win, (int)lose, winner)); } return; }
		 */

		String locName = MapsTable.getInstance().getMapName(pc.getMapId());
		if (/*pc.getMapId() == 0 || pc.getMapId() == 4 || */locName == null) {
			pc.sendPackets(new S_ServerMessage(563));
			return;
		}

		if (find != null) {
			int i = pc.getMapId();
			int countPlayer = 0;
			for (L1Object each1 : L1World.getInstance().getVisibleObjects(i).values()) {
				if (each1 instanceof L1PcInstance) {
					countPlayer++;
					continue;
				}
			}
			String title = "";
			if (find.getTitle().equalsIgnoreCase("") == false) {
				title = find.getTitle() + " ";
			}

			if (find.getKDA() != null) {
				win = find.getKDA().kill;
				lose = find.getKDA().death;
			}

			String clanname = find.getClanname();
			String lawful = "";
			total = win + lose;
			if (total > 0)
				winner = ((win * 100) / (total));

			if (find.getClan() != null) {
				if (find.getLawful() == 0) {
					lawful = "\fG(Nomal)(普通)";
				} else if (find.getLawful() > 0) {
					lawful = "\fB(Lawful)(守法)";
				} else {
					lawful = "\fG(Chaotic)(混亂)";
				}
				pc.sendPackets(String.format("\\aA%s%s \\aE[%s]\n\r\f2Kill: %d / \\aGDeath: %d / \\aI[%.2f%%]",
				find.getName(), lawful, clanname, (int) win, (int) lose, winner));
			} else {
				if (find.getLawful() == 0) {
					lawful = "\fG(Nomal)(普通)";
				} else if (find.getLawful() > 0) {
					lawful = "\fB(Lawful)(守法)";
				} else {
					lawful = "\\aG(Chaotic)(混亂)";
				}
				pc.sendPackets(String.format("\\aA%s%s \\aD%s\n\r\f2Kill: %d / \\aGDeath: %d / \\aI[%.2f%%]",
				find.getName(), lawful, title, (int) win, (int) lose, winner));
			}

			if (pc.getMapId() == 0 || pc.getMapId() == 4 || locName == null) {
				return;
			} else {
				pc.sendPackets(String.format("%s 利用者 : %d名", locName, (countPlayer * Config.ServerAdSetting.USERGHOSTCOUNT)));
			}
		} else {
			int i = pc.getMapId();
			int countPlayer = 0;
			for (L1Object each1 : L1World.getInstance().getVisibleObjects(i).values()) {
				if (each1 instanceof L1PcInstance) {
					countPlayer++;
					continue;
				}
			}
			if (pc.getMapId() == 0 || pc.getMapId() == 4 || locName == null) {
				return;
			} else {
				pc.sendPackets(String.format("%s 使用者 : %d人", locName, (countPlayer * Config.ServerAdSetting.USERGHOSTCOUNT)));
			}
		}

		/*
		 * int AddUser = (int) (L1World.getInstance().getAllPlayers().size() * Config.ServerAdSetting.USERGHOSTCOUNT); int CalcUser = L1UserCalc.getClacUser(); AddUser += CalcUser; String amount = String.valueOf(AddUser); S_WhoAmount s_whoamount = new
		 * S_WhoAmount(amount); pc.sendPackets(s_whoamount);
		 */
	}
	// }

	@Override
	public String getType() {
		return C_WHO;
	}
}
