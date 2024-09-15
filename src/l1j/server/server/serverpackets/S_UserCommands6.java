
package l1j.server.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands6 extends l1j.server.server.serverpackets.ServerBasePacket {

	private static final String S_UserCommands1 = "[C] S_UserCommands1";

	public S_UserCommands6(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);//數字
		writeS(" 梅蒂斯 ");//作者?
		writeS(" 生命之眼 製作書 ");//日期?
		writeS("");//標題?
		          writeS("\n === 生命之眼 ===\n" +
				 "\n" +
				 " 生命之眼碎片(100個)\n" +
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

