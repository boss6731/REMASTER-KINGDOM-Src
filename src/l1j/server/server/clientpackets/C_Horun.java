
package l1j.server.server.server.clientpackets;


import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.server.serverpackets.S_Horun;

public class C_Horun extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_HORUN = "[C] C_Horun";

	public C_Horun(byte abyte0[], GameClient clientthread)
			throws Exception {
		super(abyte0);

		int i = readD();

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}
		pc.sendPackets(new S_Horun(i, pc));
	}

	@Override
	public String getType() {
		return C_HORUN;
	}

}
