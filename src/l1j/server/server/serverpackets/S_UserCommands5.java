
package l1j.server.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands5 extends l1j.server.server.serverpackets.ServerBasePacket {

	private static final String S_UserCommands1 = "[C] S_UserCommands1";

	public S_UserCommands5(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);//數字
		writeS(" 梅蒂斯 ");//作者?
		writeS(" 煉金術士之石 製作書 ");//日期?
		writeS("");//標題?
		          writeS("\n === 煉金術士之石 ===\n" +
				 "\n" +
				 " 古代人的咒術書 第1卷(1個)\n" +
				 " 古代人的咒術書 第2卷(1個)\n" +
				 " 古代人的咒術書 第3卷(1個)\n" +
				 " 古代人的咒術書 第4卷(1個)\n" +
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

