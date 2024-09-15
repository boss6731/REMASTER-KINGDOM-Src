package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_LeaveParty extends ClientBasePacket {

	private static final String C_LEAVE_PARTY = "[C] C_LeaveParty";

	public C_LeaveParty(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);
		L1PcInstance player = client.getActiveChar();
		if (player == null) {
			return;
		}

		if (!player.is_world()) {
			player.sendPackets("在該地圖無法退出。");
			return;
		}

		if (player.isInParty()) { // 파티중
			player.getParty().leaveMember(player);
		}
	}

	@Override
	public String getType() {
		return C_LEAVE_PARTY;
	}

}
