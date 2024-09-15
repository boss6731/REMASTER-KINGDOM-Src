package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_FishingTime extends l1j.server.server.serverpackets.ServerBasePacket {
	public static final int FISH_WINDOW = 0x3F;
	public static final int CAUI = 0x4c;

	public S_FishingTime(int type, int subType, boolean ck, int i) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case FISH_WINDOW:
			writeC(0x08);
			writeC(subType);
			if (subType == 1) { // 開始時
				writeC(0x10);
				if (ck) { // 裝備捲線器
					writeC(i); // 成長的釣竿
				} else { // 未裝備捲線器
					writeH(i); // 時間（以秒為單位）
				}
				writeC(0x18);
				writeC(ck ? 0x02 : 0x01); // 1: 未裝備捲線器 2: 裝備捲線器
				writeH(0x00);
			} else if (subType == 2) { // 失敗或釣到魚時
				writeH(0x00);
			}
			break;
		}
	}

	public S_FishingTime(int type, int test, int time, String CName, boolean swich) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CAUI);
		writeH(264);
		writeC(16);
		if (swich) {
			writeH(test);
			writeH(time);
			writeS(CName);
		} else {
			writeC(0);
		}
		writeH(0);
	}

	public byte[] getContent() {
		return getBytes();
	}
}
