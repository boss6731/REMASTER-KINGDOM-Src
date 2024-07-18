
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_GameEnd extends ServerBasePacket {


	private static final String S_GameEnd = "[S] S_GameEnd";

	public S_GameEnd(L1PcInstance pc){
		buildPacket1(pc);
	}

	//0000 : 7e 46 00 f6 af 53 02 5c                            ~F...S.\

    private void buildPacket1(L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(0x46);
        writeC(147);
        writeC(92);
        writeC(151);
        writeC(220);
        writeC(42);
        writeC(74);
	 }


	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_GameEnd;
	}
}


