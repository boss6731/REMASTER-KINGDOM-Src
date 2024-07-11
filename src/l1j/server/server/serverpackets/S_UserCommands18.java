
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands18 extends ServerBasePacket {

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ); // 寫入操作碼，用於識別這個數據包的類型
		writeD(number); // 寫入一個整數類型的數值，這裡是方法的參數 number
		writeS(" 메蒂斯 "); // 寫入一個字符串，值為 "메蒂斯" (可能是作者)
		writeS(" 製作秘法書 "); // 寫入一個字符串，值為 "製作秘法書" (可能是日期)
		writeS(""); // 寫入一個空字符串 (可能是標題)
		writeS("\n"+
				"\n── +10 咆哮雙劍 ──\n"+
				" 水龍鱗片 (10)個 \n"+
				" 風龍鱗片 (10)個 \n"+
				" 地龍鱗片 (10)個 \n"+
				" 火龍鱗片 (10)個 \n"+
				" 暗黃石 (100)個 \n"+
				" 匠人的武器魔法書 (3)個 \n"+
				" +8 咆哮雙劍 (1)個 \n"+
				"\n"+
				" ──────────────\n" +
				"     製作是天堂的花朵\n。");
		// 寫入多行字符串，包含製作「+10 咆哮雙劍」的材料清單和補充說明
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_UserCommands1;
	}
}

