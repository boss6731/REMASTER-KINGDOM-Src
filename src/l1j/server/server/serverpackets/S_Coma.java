
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

// 最初
// S_SkillSound(7382): A類型
// S_SkillSound(7383): B類型可以發送。
// 硬幣背包圖示: 3565
public class S_Coma extends ServerBasePacket {

	// 當 j 是 40 時 A, 當 j 是 41 時 B
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
