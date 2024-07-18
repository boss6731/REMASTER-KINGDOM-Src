package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_ChangeName extends ServerBasePacket {
	private static final String S_CHANGE_NAME = "[S] S_ChangeName";

	public S_ChangeName(int objectId, String name) {
		writeC(Opcodes.S_CHANGE_DESC);
		writeD(objectId);
		writeS(name);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_CHANGE_NAME;
	}
}


