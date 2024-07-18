package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_DeleteCharOK extends ServerBasePacket {
	private static final String S_DELETE_CHAR_OK = "[S] S_DeleteCharOK";

	public static final int DELETE_CHAR_NOW = 0x05;
	public static final int DELETE_CHAR_AFTER_7DAYS = 0x51;

	public static S_DeleteCharOK deleteRemainSeconds(int remainSeconds) {
		S_DeleteCharOK s = new S_DeleteCharOK();
		s.writeC(DELETE_CHAR_AFTER_7DAYS);
		s.writeD(remainSeconds);
		return s;
	}

	public S_DeleteCharOK(int type) {
		writeC(Opcodes.S_DELETE_CHARACTER_CHECK);
		writeC(type);
	}

	private S_DeleteCharOK() {
		writeC(Opcodes.S_DELETE_CHARACTER_CHECK);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_DELETE_CHAR_OK;
	}
}


