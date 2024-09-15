
package l1j.server.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands1 extends l1j.server.server.serverpackets.ServerBasePacket {

	private static final String S_UserCommands1 = "[C] S_UserCommands1";
	public S_UserCommands1(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number); // 數字
		writeS(" 梅蒂斯 "); // 作者?
		writeS(" 鬥士之劍 製作書"); // 日期?
		writeS(""); // 標題?
		writeS("\n === 鬥士之劍 === \n" +
				"\n" +
				" 最高級紅寶石(5個)\n" +
				" 最高級綠寶石(5個)\n" +
				" 最高級藍寶石(5個)\n" +
				" 最高級鑽石(5個)\n" +
				" 奧里哈爾貢箭(500個)\n" +
				" 阿斯塔吉奧的灰燼(30個)\n" +
				"\n" +
				" ==========================");

	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_UserCommands1;
	}
}

