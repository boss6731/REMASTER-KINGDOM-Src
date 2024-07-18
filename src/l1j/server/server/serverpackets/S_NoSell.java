package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;

public class S_NoSell extends ServerBasePacket {
	private static final String _S__25_NoSell = "[S] _S__25_NoSell";

	public S_NoSell(L1NpcInstance npc) {
		buildPacket(npc);
	}

	private void buildPacket(L1NpcInstance npc) {
		writeC(Opcodes.S_HYPERTEXT); // 寫入操作碼，用於表示超文本數據包
		writeD(npc.getId());         // 寫入 NPC 的 ID
		//        writeS("nosell"); // 注釋掉的行，原本可能是寫入 "nosell"
		writeS("incence");           // 寫入字符串 "incence"，表示動作值
		writeC(1);                   // 寫入一個字節 1
	}
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S__25_NoSell;
	}
}


