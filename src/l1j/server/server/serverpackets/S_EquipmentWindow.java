package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_EquipmentWindow extends ServerBasePacket {
	private static final String S_EQUIPMENTWINDOWS = "[S] S_EquipmentWindow"; // 設備視窗
	public static final byte EQUIPMENT_INDEX_HEML = 1; // 頭盔
	public static final byte EQUIPMENT_INDEX_ARMOR = 2; // 盔甲
	public static final byte EQUIPMENT_INDEX_T = 3; // T 恤
	public static final byte EQUIPMENT_INDEX_CLOAK = 4; // 斗篷
	public static final byte EQUIPMENT_INDEX_PAIR = 5; // 護脛
	public static final byte EQUIPMENT_INDEX_BOOTS = 6; // 靴子
	public static final byte EQUIPMENT_INDEX_GLOVE = 7; // 手套
	public static final byte EQUIPMENT_INDEX_SHIELD = 8; // 盾牌
	public static final byte EQUIPMENT_INDEX_WEAPON = 9; // 武器
	public static final byte EQUIPMENT_INDEX_NECKLACE = 11; // 項鍊
	public static final byte EQUIPMENT_INDEX_BELT = 12; // 腰帶
	public static final byte EQUIPMENT_INDEX_EARRING = 13; // 耳環1
	public static final byte EQUIPMENT_INDEX_EARRING_2ND = 28; // 耳環1
	public static final byte EQUIPMENT_INDEX_EARRING1 = 28; // 耳環2
	public static final byte EQUIPMENT_INDEX_EARRING2 = 34; // 耳環2
	public static final byte EQUIPMENT_INDEX_EARRING3 = 35; // 耳環2
	public static final byte EQUIPMENT_INDEX_RING = 19; // 戒指1
	public static final byte EQUIPMENT_INDEX_RUNE = 25; // 符文
	public static final byte EQUIPMENT_INDEX_SENTENCE = 30; // 語句
	public static final byte EQUIPMENT_INDEX_SHOULD = 31; // 應該是
	public static final byte EQUIPMENT_INDEX_BADGE = 32; // 徽章
	public static final byte EQUIPMENT_INDEX_PANDENT = 33; // 墜飾
}

	public S_EquipmentWindow(L1PcInstance pc, int itemObjId, int index, boolean isEq) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(0x42);
		writeD(itemObjId);
		writeC(index);	// 0x1D 견갑, 0x1E 휘장
		if(isEq)
			writeC(1);
		else
			writeC(0);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_EQUIPMENTWINDOWS;
	}
}


