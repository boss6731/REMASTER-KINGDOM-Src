package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Unknown2 extends ServerBasePacket {

	public S_Unknown2(int type) {
		writeC(Opcodes.S_EVENT);
		switch(type) {
			case 0: // 登錄時處理，Amount 前
				writeC(0x3d);
				writeD(0); // 剩餘時間
				writeC(0); // 預約
				writeC(0x29); // 未知
				break;
			case 1: // 重生
				writeC(0x2A);
				writeD(0);
				writeH(0);
				break;
		}
	}

	@override
	public byte[] getContent() {
		return getBytes();
	}

	@override
	public String getType() {
		return "[S] S_Unknown2";
	}
}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


