package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharPacks extends ServerBasePacket {
	private static final String S_CHAR_PACKS = "[S] S_CharPacks";

	public S_CharPacks(String name, String clanName, int type, int sex, int lawful, int hp, int mp, int ac, int lv, int str, int dex, int con, int wis, int cha, int intel, int accessLevel, int birth, int deleteSeconds) {
		writeC(Opcodes.S_CHARACTER_INFO); // 寫入操作碼，表示角色資訊
		/**
		 * 顯示「是否要將名稱改為指定名稱」
		 * _L(數字9個)
		 * 公會名稱也必須是空白。
		 */
		writeS(name); // 寫入角色名稱
		writeS(clanName); // 寫入公會名稱
		writeC(type); // 寫入角色類型
		writeC(sex); // 寫入性別
		writeH(lawful); // 寫入合法值
		writeH(hp); // 寫入生命值
		writeH(mp); // 寫入魔法值
		writeC(ac); // 寫入防禦
		writeC(lv); // 寫入等級
		writeC(str); // 寫入力量
		writeC(dex); // 寫入敏捷
		writeC(con); // 寫入體質
		writeC(wis); // 寫入智慧
		writeC(cha); // 寫入魅力
		writeC(intel); // 寫入智力
		writeC(0); // 寫入未知數據（可能是保留位）
		writeD(birth); // 寫入出生日期
		int code = lv ^ str ^ dex ^ con ^ wis ^ cha ^ intel; // 計算校驗碼
		writeC(code & 0xFF); // 寫入校驗碼的低位字節
		if(type >= 32 && deleteSeconds > 0) {
			writeD(deleteSeconds); // 如果類型大於等於 32 且刪除秒數大於 0，寫入刪除秒數
		} else {
			writeD(0x00); // 否則，寫入 0
		}
		writeD(0x00); // 寫入保留位
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_CHAR_PACKS;
	}
}


