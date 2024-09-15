package l1j.server.server.server.clientpackets;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.GameClient;
import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;

public class C_BanClan extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_BAN_CLAN = "[C] C_BanClan";
	private static Logger _log = Logger.getLogger(C_BanClan.class.getName());

    public C_BanClan(byte abyte0[]) throws Exception {
        this(abyte0, null);
    }

    private C_BanClan(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);

		String s = readS();
		if ((s == null) || (s.equals("")))
			return;

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		L1Clan clan = L1World.getInstance().findClan(pc.getClanname());
		if (clan != null) {
			int i;
			if (pc.isCrown() && pc.getId() == clan.getLeaderId()) { // 王族, ?
				// 血盟主
				for (i = 0; i < clan.getClanMemberList().size(); i++) {
					if (pc.getName().toLowerCase().equals(s.toLowerCase())) { // 王族
						// 自己
						return;
					}
				}
				int castle_id = clan.getCastleId();
				if (castle_id != 0 && MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(439)));
					return;
				}
				L1PcInstance tempPc = L1World.getInstance().getPlayer(s);
				if (tempPc != null) { // 在線中
					if (tempPc.getClanid() == pc.getClanid()) { // 同一個血盟
						tempPc.ClearPlayerClanData(clan);
						clan.removeClanMember(tempPc.getName());
						pc.sendPackets(new S_PacketBox(pc, S_PacketBox.PLEDGE_REFRESH_MINUS));
						tempPc.sendPackets(String.valueOf(new S_ServerMessage(238, pc.getClanname())));
						// 你已經被逐出 %0 血盟。
						pc.sendPackets(String.valueOf(new S_ServerMessage(240, tempPc.getName()))); // %0 被
						// %0 已經被逐出你的血盟。
					} else {
						pc.sendPackets(String.valueOf(new S_ServerMessage(109, s)));
						// 沒有叫 %0 的人。
					}
				} else { // 離線中
					try {
						L1PcInstance restorePc = CharacterTable.getInstance().restoreCharacter(s);
						if (restorePc != null && restorePc.getClanid() == pc.getClanid()) {
							// 同一個血盟
							restorePc.ClearPlayerClanData(clan);
							clan.removeClanMember(restorePc.getName());
							pc.sendPackets(String.valueOf(new S_ServerMessage(240, restorePc.getName())));
							// %0 已經被逐出你的血盟。
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(109, s)));
							// 沒有叫 %0 的人。
						}
					} catch (Exception e) {
						_log.log(Level.SEVERE, "C_BanClan[]Error", e);
					}
				}
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(518))); // 這個命令只能由血盟的王族使用。
				// 只能由王族使用此命令。
			}
		}
	}

    public static C_BanClan createC_BanClan(byte abyte0[], GameClient clientthread) throws Exception {
        return new C_BanClan(abyte0, clientthread);
    }

    @Override
	public String getType() {
		return C_BAN_CLAN;
	}
}
