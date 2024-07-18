package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_EtcPacket extends ServerBasePacket{

	public S_EtcPacket(int type){	
		writeC(Opcodes.S_EVENT);
		switch(type){
			// 開始
			case 0:
				writeC(71);
				writeC(500);
				break;
			// 結尾
			case 1:
				writeC(0x46);
				writeC(147);
				writeC(92);
				writeC(151);
				writeC(220);
				writeC(42);
				writeC(74);
				break;
			// 開始遊戲
			case 3:
				writeC(64);
				writeC(5);
				writeC(129);
				writeC(252);
				writeC(125);
				writeC(110);
				writeC(17);
				break;
			// 遊戲時間
			case 4:
				writeC(0x41);
				writeC(100);
				writeC(0);
				writeC(0);
				writeC(0);
				writeC(0);
				writeC(0);
				break;
			// 刪除時間包
			case 5:
				writeC(72);
				break;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


