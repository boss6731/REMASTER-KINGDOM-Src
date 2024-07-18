package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharacterCreation extends ServerBasePacket {
	public static final String S_CharacterCreation = "[S] S_LoginResult";
	public static final int CHARACTER_PASSWORD_DISPLAY = 51;
	public static final int CHARACTER_PASSWORD_SUCCESS = 22;

	public S_CharacterCreation(int code) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(CHARACTER_PASSWORD_DISPLAY);
		writeC(0x01);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
	}

	public S_CharacterCreation() {
		buildPacket();
	}

	private void buildPacket() {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(0x3F);
		writeC(0x01);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
	}

	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return "[S] S_LoginResult";
	}
}