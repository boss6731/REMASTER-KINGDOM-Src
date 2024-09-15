
package l1j.server.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands3 extends l1j.server.server.serverpackets.ServerBasePacket {

	private static final String S_UserCommands3 = "[C] S_UserCommands3";

	public S_UserCommands3(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);//數字
		writeS(" 梅蒂斯 ");//作者?
		writeS(" 黑王刀 製作書 ");//日期?
		writeS("");//標題?
		writeS("\n === 黑王刀 === \n" +
				"\n" +
				" 龍之心臟(1個)\n" +
				" 最高級紅寶石(3個)\n" +
				" 黑光雙刀(1個)\n" +
				" 阿德納 (100,000元)\n" +
				" 被詛咒的皮革(10個)\n" +
				" 冰雪女王的氣息(9個)\n" +
				" 格蘭卡因的眼淚(3個)\n"+
				"\n" +
				" ==========================");
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_UserCommands3;
	}
}

