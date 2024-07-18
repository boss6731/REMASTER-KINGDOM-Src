
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

//首先，只需發送 S_SkillSound (7382)：類型 A。硬幣庫存圖：3565
public class S_Coma extends ServerBasePacket {

// 當 j 為 40 時為 A，當 j 為 41 時為 B
	public S_Coma(int j,int time) {
		writeC(Opcodes.S_EVENT);
		writeC(0x14);
		for(int i = 0; i < 64; i++) writeC(0x00);
		writeC((int)(time + 16) / 32);
		writeC(j);
		writeC(0x14);
		writeD(0x00000000);
		writeC(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


