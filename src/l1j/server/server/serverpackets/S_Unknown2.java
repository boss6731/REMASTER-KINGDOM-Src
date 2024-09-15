package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Unknown2 extends ServerBasePacket {
	public S_Unknown2(int type) {
		writeC(Opcodes.S_EVENT);
		switch(type){
			case 0: // 登錄時處理, 數量前
				writeC(0x3d);
				writeD(0);  // 剩餘時間
				writeC(0);  // 預約
				writeC(0x29); // 未知
				break;
			case 1:    // 重生
			writeC(0x2A);
			writeD(0);
			writeH(0);
			break;

		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
