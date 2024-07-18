package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharAmount extends ServerBasePacket {

	public S_CharAmount(int value, int i) {
		buildPacket(value, i);
	}

	private void buildPacket(int value, int slot) {
		writeC(Opcodes.S_NUM_CHARACTER);
		writeC(value);
		writeC(slot);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


