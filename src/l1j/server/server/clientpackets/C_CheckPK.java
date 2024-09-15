package l1j.server.server.server.clientpackets;


import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

public class C_CheckPK extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_CHECK_PK = "[C] C_CheckPK";

	public <GameClient> C_CheckPK(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		if (player == null)
			return;
		player.sendPackets(String.valueOf(new S_ServerMessage(562, String.valueOf(player.get_PKcount())))); // 當前的
																											 	 // PK次數是%0。
	}

	@Override
	public String getType() {
		return C_CHECK_PK;
	}

}
