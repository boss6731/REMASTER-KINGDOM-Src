package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharPacks extends ServerBasePacket {
	private static final String S_CHAR_PACKS = "[S] S_CharPacks";

	public S_CharPacks(String name, String clanName, int type, int sex,
			int lawful, int hp, int mp, int ac, int lv, int str, int dex,
			int con, int wis, int cha, int intel, int accessLevel, int birth, int deleteSeconds) {
		writeC(Opcodes.S_CHARACTER_INFO);
		/**
		 * 是否顯示為通過名稱識別來更改名稱
		 * _L(9個數字)
		 * 公會名稱也必須是空的。
		 */
		writeS(name);
		writeS(clanName);
		writeC(type);
		writeC(sex);
		writeH(lawful);
		writeH(hp);
		writeH(mp);
		writeC(ac);
		writeC(lv);
		writeC(str);
		writeC(dex);
		writeC(con);
		writeC(wis);
		writeC(cha);
		writeC(intel);
		writeC(0);
		writeD(birth);
		int code = lv ^ str ^ dex ^ con ^ wis ^ cha ^ intel;
		writeC(code & 0xFF);		
		if(type >= 32 && deleteSeconds > 0) {
			writeD(deleteSeconds);
		}else {
			writeD(0x00);			
		}
		writeD(0x00);
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
