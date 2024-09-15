package l1j.server.server.clientpackets;

import java.io.File;

import MJShiftObject.Battle.MJShiftBattlePlayManager;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.GameClient;
import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.server.datatables.ClanStorageTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ClanJoinInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.server.serverpackets.ServerMessage;

public class C_LeaveClan extends ClientBasePacket {

	private static final String C_LEAVE_CLAN = "[C] C_LeaveClan";

	public C_LeaveClan(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		L1PcInstance player = clientthread.getActiveChar();
		if (player == null) {
			return;
		}
		if (MJShiftBattlePlayManager.is_shift_battle(player))
			return;

		String clanname = readS();
		L1Clan clan = L1World.getInstance().findClan(clanname);

		if (clan == null)
			return;

		int clan_id = clan.getClanId();

		if (clan_id == 0)
			return;

		// 是否是該血盟的君主？

		if (player.isCrown() && player.getId() == clan.getLeaderId()) {
			if (clan.getAllianceList() != null) {
				String  alliance_clanlist = clan.getAllianceList().toString();
				int idx = alliance_clanlist.indexOf("[") + 1;
				int idx2 = alliance_clanlist.lastIndexOf("]");
				if (idx > -1 && idx2 > -1) {
					String claninfo = alliance_clanlist.substring(idx, idx2);
					String[] alliance_clan_id = (String[]) MJArrangeParser.parsing(claninfo, ", ", MJArrangeParseeFactory.createStringArrange()).result();
					for (int i = 0; i < alliance_clan_id.length; i++) {
						if (clan_id != Integer.parseInt(alliance_clan_id[i])) {
							L1Clan alliance_clan = L1World.getInstance().getClan(Integer.parseInt(alliance_clan_id[i]));
							if (alliance_clan != null) {
								alliance_clan.removeAlliance(clan_id);
								ClanTable.getInstance().updateClan(alliance_clan);
							}
						}
					}
				}
			}
			leaveClanBoss(clan, player);
		} else { // 非君主的血盟成員退出
			leaveClanMember(clan, player);
		}
	}

	private void leaveClanBoss(L1Clan clan, L1PcInstance player) throws Exception {
		String player_name = player.getName();
		String clan_name = player.getClanname();

		if (clan.getCastleId() > 0 || clan.getHouseId() > 0) {
			player.sendPackets(new S_ServerMessage(ServerMessage.HAVING_NEST_OF_CLAN));
			return;
		}

		if (clan.getCurrentWar() != null) {
			player.sendPackets(new S_ServerMessage(ServerMessage.CANNOT_BREAK_CLAN));
			return;
		}

		if (clan.AllianceSize() > 0) {
			player.sendPackets(new S_ServerMessage(ServerMessage.CANNOT_BREAK_CLAN_HAVING_FRIENDS));
			return;
		}

		L1PcInstance pc = null;
		for (int i = 0; i < clan.getClanMemberList().size(); i++) {
			L1ClanJoinInstance.removeInstance(player.getClanid()); // 無人君主解散
			// 清除血盟成員的血盟信息
			pc = L1World.getInstance().getPlayer(clan.getClanMemberList().get(i).name);

			if (pc == null) {
				// 血盟成員離線的情況
				pc = CharacterTable.getInstance().restoreCharacter(clan.getClanMemberList().get(i).name);
			} else {
				// %1血盟的君主%0解散了血盟。
				pc.sendPackets(new S_ServerMessage(269, player_name, clan_name));
			}
			pc.ClearPlayerClanData(clan);
		}

		String emblem_file = String.valueOf(player.getClanid());
		File file = new File("emblem/" + emblem_file);
		file.delete();
		ClanTable.getInstance().deleteClan(clan_name);
	}

	public static void leaveClanMember(L1Clan clan, L1PcInstance player) throws Exception {
		String player_name = player.getName();
		String clan_name = player.getClanname();
		L1PcInstance clanMember[] = clan.getOnlineClanMember();

		for (int i = 0; i < clanMember.length; i++) {
			clanMember[i].sendPackets(new S_ServerMessage(178, player_name, clan_name)); // 1%0已退出%1血盟。
			player.start_teleport(player.getX(), player.getY(), player.getMapId(), player.getHeading(), 18339, false, false);
		}
		if (player.isClanBuff()) {
			player.killSkillEffectTimer(L1SkillId.CLANBUFF_YES);
			player.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 450, false));
			player.setClanBuff(false);
		}
		player.ClearPlayerClanData(clan);
		clan.removeClanMember(player_name);
		
	}

	@Override
	public String getType() {
		return C_LEAVE_CLAN;
	}
}