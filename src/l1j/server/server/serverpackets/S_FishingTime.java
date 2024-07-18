package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_FishingTime extends ServerBasePacket {
	public static final int FISH_WINDOW = 0x3F; // 釣魚窗口常量
	public static final int CAUI = 0x4c; // CAUI 常量

	public S_FishingTime(int type, int subType, boolean ck, int i) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF); // 寫入擴展的 Protobuf 操作碼
		writeH(type); // 寫入類型

		switch (type) {
			case FISH_WINDOW: // 當類型為釣魚窗口
				writeC(0x08); // 固定數據
				writeC(subType); // 寫入子類型

				if (subType == 1) { // 當子類型為 1（開始）
					writeC(0x10); // 固定數據

					if (ck) { // 如果 ck 為 true（裝備釣竿）
						writeC(i); // 寫入成長的釣竿
					} else { // 如果 ck 為 false（未裝備釣竿）
						writeH(i); // 寫入時間（以秒為單位）
					}

					writeC(0x18); // 固定數據
					writeC(ck ? 0x02 : 0x01); // 寫入 1: 未裝備釣竿，2: 裝備釣竿
					writeH(0x00); // 寫入 0
				} else if (subType == 2) { // 當子類型為 2（失敗或成功釣到）
					writeH(0x00); // 寫入 0
				}
				break;
		}
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


