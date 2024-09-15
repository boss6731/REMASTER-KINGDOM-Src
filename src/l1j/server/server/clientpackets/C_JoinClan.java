package l1j.server.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.FaceToFace;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_JoinClan extends ClientBasePacket {
	

	private static final String C_JOIN_CLAN = "[C] C_JoinClan";

	public C_JoinClan(byte abyte0[], GameClient clientthread)
			throws Exception {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null || pc.isGhost() ) {
			return;
		}

		L1PcInstance target = FaceToFace.faceToFace(pc);
		if (target != null) {
			JoinClan(pc, target);
		}
	}

	private void JoinClan(L1PcInstance player, L1PcInstance target) {
		if (!target.isCrown() && (target.getClanRank() != L1Clan.GUARDIAN)) { // 守護
			player.sendPackets(String.valueOf(new S_SystemMessage(target.getName() + " 不是王子或公主的守護騎士。")));
			return;
		}

		int clan_id = target.getClanid();
		if (clan_id == 0) { // 對方沒有血盟
			player.sendPackets(String.valueOf(new S_ServerMessage(90, target.getName()))); // 1%0 尚未創建血盟。
			return;
		}

		L1Clan clan = L1World.getInstance().getClan(clan_id);
		if (clan == null) {
			return;
		}

		if (target.getClanRank() != L1Clan.PRINCE && target.getClanRank() != L1Clan.GUARDIAN) { // 盟主和守護
			player.sendPackets(String.valueOf(new S_SystemMessage(target.getName() + " 不是王子或公主的守護騎士。")));
			return;
		}

		if (player.getClanid() != 0) { // 已經加入了血盟
			if (player.isCrown()) { // 自己是君主
				L1Clan player_clan = L1World.getInstance().getClan(player.getClanid());
				if (player_clan == null) {
					return;
				}

				if (player.getId() != player_clan.getLeaderId()) { // 自己不是血盟盟主
					player.sendPackets(String.valueOf(new S_ServerMessage(89))); // 您已經加入了一個血盟。
					return;
				}

				if (player_clan.getCastleId() != 0 || // 自己擁有城堡或據點
						player_clan.getHouseId() != 0) {
					player.sendPackets(String.valueOf(new S_ServerMessage(665))); // 1擁有城堡或據點的狀態下無法解散血盟。
					return;
				}
			} else {
				player.sendPackets(String.valueOf(new S_ServerMessage(89))); // 1您已經加入了一個血盟。
				return;
			}
		}
		target.setTempID(player.getId()); // 保留對方的對象 ID
		target.sendPackets(String.valueOf(new S_Message_YN(97, player.getName()))); // %0 已經申請加入血盟。是否接受？ (Y/N)
	}

	@Override
	public String getType() {
		return C_JOIN_CLAN;
	}
}
