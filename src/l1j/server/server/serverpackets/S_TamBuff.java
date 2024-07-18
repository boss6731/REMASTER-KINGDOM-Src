package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_TamBuff extends ServerBasePacket {

	private static final String S_TamBuff = "[S] S_TamBuff";

	// 構造函數，接受 id, time 和 type 作為參數
	public S_TamBuff(int id, int time, int type) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF); // 寫入擴展操作碼
		writeH(0x6e); // 寫入操作碼後的數據
		writeC(0x08);
		writeC(2);
		writeC(0x10);
		write7B(id); // 寫入 id
		writeC(0x18);
		write7B(time); // 寫入時間
		writeC(0x20);
		writeC(8);
		writeC(0x28);
		write7B(type == 1 ? 6100 : type == 2 ? 6546 : 6547); // 根據類型寫入相應值
		writeC(0x30);
		writeC(0);
		writeC(0x38);
		writeC(1);
		writeC(0x40);
		write7B(type + 4180); // 寫入類型加上 4180 的值
		writeC(0x48);
		writeH(0x20d5); // 寫入固定值
		writeC(0x50);
		writeC(0);
		writeC(0x58);
		writeC(1);
		writeH(0);
	}

	@override
	public byte[] getContent() {
		return getBytes();
	}

	@override
	public String getType() {
		return S_TamBuff;
	}
}


