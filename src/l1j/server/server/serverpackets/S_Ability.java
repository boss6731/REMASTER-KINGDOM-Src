package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Ability extends ServerBasePacket {

	private static final String S_ABILITY = "[S] S_Ability";

	public S_Ability(int type, boolean equipped) {
		buildPacket(type, equipped);
	}

	private void buildPacket(int type, boolean equipped) {
		writeC(Opcodes.S_CHANGE_ABILITY);
		writeC(type); // 1:ROTC 5:ROSC
		if (equipped) {
			writeC(0x01);
		} else {
			writeC(0x00);
		}
		writeC(0x02);
		writeH(0x0000);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_ABILITY;
	}
}
