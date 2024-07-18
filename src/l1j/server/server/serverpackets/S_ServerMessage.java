package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_ServerMessage extends ServerBasePacket {
	private static final String S_SERVER_MESSAGE = "[S] S_ServerMessage";
	public static final int NO_PLEDGE = 208;
	public static final int CANNOT_GLOBAL = 195;
	public static final int CANNOT_BOOKMARK_LOCATION = 214;
	public static final int USER_NOT_ON = 73;
	public static final int NOT_ENOUGH_MP = 278;
	public static final int YOU_FEEL_BETTER = 77;
	public static final int YOUR_WEAPON_BLESSING = 693;
	public static final int YOUR_Are_Slowed = 29;

	public S_ServerMessage(int type) {
		super(4);
		buildPacket(type, null, null, null, null, null, 0);
	}

	public S_ServerMessage(int type, String msg1) {
		super(4 + (msg1.length() * 2));
		buildPacket(type, msg1, null, null, null, null, 1);
	}

	public S_ServerMessage(int type, String[] args) {
		super(16 + (args.length * 16));
		writeC(102);
		writeH(type);
		writeC(args.length);
		if ((args.length == 1) && (type == 2489)) {
			writeC(2);
			writeD(4);
		}
		for (String s : args)
			writeS(s);
	}

	public S_ServerMessage(int type, String msg1, String msg2) {
		/*if(type == 950) {
			new Throwable().printStackTrace();
		}*/
		buildPacket(type, msg1, msg2, null, null, null, 2);
	}

	public S_ServerMessage(int type, String msg1, String msg2, String msg3) {
		buildPacket(type, msg1, msg2, msg3, null, null, 3);
	}

	public S_ServerMessage(int type, String msg1, String msg2, String msg3, String msg4) {
		buildPacket(type, msg1, msg2, msg3, msg4, null, 4);
	}

	public S_ServerMessage(int type, String msg1, String msg2, String msg3, String msg4, String msg5) {
		buildPacket(type, msg1, msg2, msg3, msg4, msg5, 5);
	}

	public S_ServerMessage(boolean chargeGiranTime) {
	}

	private void buildPacket(int type, String msg1, String msg2, String msg3, String msg4, String msg5, int check) {
		writeC(Opcodes.S_MESSAGE_CODE);
		writeH(type);
		if (check == 0) {
			writeC(0);
		} else if (check == 1) {
			writeC(1);
			writeS(msg1);
		} else if (check == 2) {
			writeC(2);
			writeS(msg1);
			writeS(msg2);
		} else if (check == 3) {
			writeC(3);
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
		} else if (check == 4) {
			writeC(4);
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
			writeS(msg4);
		} else {
			writeC(5);
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
			writeS(msg4);
			writeS(msg5);
		}
	}
	
	// 七龍珠.
	public S_ServerMessage(int pearl, int time) {
		writeC(Opcodes.S_MESSAGE_CODE);
		writeH(pearl); // 細繩
		writeC(0x01);
		writeH(time);  // 小時
	}
	
	// 輸出倉庫密碼確認視窗
	public S_ServerMessage() {
		writeC(Opcodes.S_MESSAGE_CODE);
		writeC(66);
		writeH(0x03);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_SERVER_MESSAGE;
	}
}


