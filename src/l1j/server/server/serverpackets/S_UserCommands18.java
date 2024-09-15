
package l1j.server.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands18 extends l1j.server.server.serverpackets.ServerBasePacket {

	private static final String S_UserCommands1 = "[C] S_UserCommands1";

	public S_UserCommands18(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number); //數字
		writeS(" 梅蒂斯 "); //作者?
		writeS(" +10 咆哮雙刀 製作書 "); //日期?
		writeS(""); //標題?
		          writeS("\n ── +10 咆哮雙刀製作 ──\n" +
				 "\n" +
				 " 水龍鱗片 (10)個\n" +
				 " 風龍鱗片 (10)個\n" +
				 " 地龍鱗片 (10)個\n" +
				 " 火龍鱗片 (10)個\n" +
				 " 暗黃石 (100)個\n" +
				 " 工匠的對武器施法的卷軸 (3)個\n" +
				 " +8 咆哮雙刀 (1)個\n" +
				 "\n" +
				 " ──────────────"+
		          "     製作是天堂的精髓.\n");
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_UserCommands1;
	}
}

