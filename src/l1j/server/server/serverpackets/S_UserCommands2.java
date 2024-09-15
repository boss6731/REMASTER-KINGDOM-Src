
package l1j.server.server.server.serverpackets;
import l1j.server.server.Opcodes;

public class S_UserCommands2 extends l1j.server.server.serverpackets.ServerBasePacket {

	private static final String S_UserCommands2 = "[C] S_UserCommands2";

	public S_UserCommands2(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number); // 數字
		writeS(" 梅蒂斯 "); // 作者?
		writeS(" 風之弓 製作書 "); // 日期?
		writeS(""); // 標題?
		writeS("\n === 風之弓 ===\n" +
				"\n" +
				" 長弓(1個)\n" +
				" 風龍的鱗片(15個)\n" +
				" 風之淚(50個)\n" +
				" 獅鷲的羽毛(30個)\n" +
				"\n" +
				" ==========================");

	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_UserCommands2;
	}
}

