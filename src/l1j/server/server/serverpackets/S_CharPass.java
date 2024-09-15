package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharPass extends ServerBasePacket {
	public static final int _CHARACTER_SELECTION_SCREEN_ENTRY = 0x40;
	public static final int _CHARACTER_SELECTION_SCREEN_ENTRY2 = 0x0a;
	public static final int _CHARACTER_SELECTION_SCREEN_ENTRY3 = 0x16;
	public static final int _PASSWORD_CREATION_SCREEN = 0x17;
	public static final int _PASSWORD_CREATION_COMPLETION_SCREEN = 0x11;
	public static final int _PASSWORD_INPUT_SCREEN = 0x14;
	public static final int _PASSWORD_INPUT_INCORRECT = 0x15;
	public static final int _PASSWORD_CHANGE_RESPONSE = 0x13;
	public static final int _PASSWORD_VERIFICATION_COMPLETED = 0x3f;

	public static S_CharPass do_fail_password(int failure_count, int maximum_count){
		S_CharPass packet = new S_CharPass();
		packet.writeC(_PASSWORD_INPUT_INCORRECT);
		packet.writeD(0xa5);
		packet.writeH(failure_count);
		packet.writeH(maximum_count);
		packet.writeD(0x00);
		return packet;
	}

	public S_CharPass(){
		writeC(Opcodes.S_VOICE_CHAT);
	}

	public S_CharPass(int val) {
		writeC(Opcodes.S_VOICE_CHAT); // 105
		writeC(val);
		switch (val) {
			case _PASSWORD_INPUT_INCORRECT:
				writeD(0xa5);
				writeH(0x02);
				writeH(0x05);
				writeD(0);
				break;
			case _PASSWORD_CREATION_COMPLETION_SCREEN:
				writeD(0);
				break;
			case _CHARACTER_SELECTION_SCREEN_ENTRY2:
				writeD(2);
				break;
			case _CHARACTER_SELECTION_SCREEN_ENTRY3:
				writeD(170);
				writeD(0);
				writeD(0);
				writeH(0);
				writeC(1);
				writeC(0);
				break;
			default:
				break;
		}
	}

	public S_CharPass(int val, boolean ck) {
		writeC(Opcodes.S_VOICE_CHAT); // 105
		writeC(val);
		switch (val) {
			case _PASSWORD_CHANGE_RESPONSE:
				// fe 13 00 00 00 00 00 00 05 00 00 00 00 00 ..............
				if (ck) {
					writeD(0);
					writeH(0);
					writeH(0x05);
					writeD(0);
				} else {
					writeD(0xa5);
					writeH(0x01);
					writeH(0x05);
					writeD(0);
				}
				break;
			case _CHARACTER_SELECTION_SCREEN_ENTRY3:
				if (ck) {
					writeD(0);
					writeH(0);
					writeD(0x05);
					writeD(0);
					writeH(0x01);
				} else {
					writeD(170);
					writeD(0);
					writeD(0);
					writeH(0);
					writeH(1);
				}
				break;
			default:
				break;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return "[S] S_Test";
	}

	@SuppressWarnings("unused")
	private static final String _S__19_Test = "[S] S_Test";
}
