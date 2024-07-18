package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SkillIconAura extends ServerBasePacket {

	public S_SkillIconAura(int i, int j) {
		writeC(Opcodes.S_EVENT);
		writeC(0x16);
		writeC(i);
		writeH(j);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


