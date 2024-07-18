package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_GMHtml extends ServerBasePacket {
	public S_GMHtml(String sender, String sender2, String presentName, String receiver) {
		writeC(Opcodes.S_HYPERTEXT);
		writeD(0);
		writeS("giftmessage");
		writeH(0x01);
		writeH(0x04);
		writeS(sender);
		writeS(sender2);
		writeS(presentName);
		writeS(receiver);
	}
	
	public S_GMHtml(String s1, String s2) {
		String[] board = new String[2];
		board[0] = s1;
		board[1] = s2;
		writeC(Opcodes.S_HYPERTEXT);
		writeD(0);
		writeS("withdraw");
		if ((board != null) && (1 <= board.length)) {
			writeH(1);
			writeH(board.length);
			for (String datum : board) {
				writeS(datum);
			}
		}
	}

	public S_GMHtml(int _objid, String html) {
		writeC(Opcodes.S_HYPERTEXT);
		writeD(_objid);
		writeS("hsiw");
		writeS(html);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


