package l1j.server.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Emblem;

public class C_Clan extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_Clan = "[C] C_Clan";

	public <GameClient> C_Clan(byte abyte0[], GameClient clientthread) {
		super(abyte0);

		int emblemId = readD();
		int numId = readD();
		L1PcInstance pc = clientthread.getActiveChar();
//		System.out.println(emblemId+", "+numId);
		if (pc == null) {
			return;
		}
		
		pc.sendPackets(new S_Emblem(emblemId), true);
			
	}

	@Override
	public String getType() {
		return C_Clan;
	}
}