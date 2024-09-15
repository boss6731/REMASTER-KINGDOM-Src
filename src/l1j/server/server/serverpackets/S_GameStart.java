
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_GameStart extends ServerBasePacket {

	private static final String S_GameStart = "[S] S_GameStart";

	public S_GameStart(L1PcInstance pc){
		buildPacket1(pc);
	}

	//0000 : 7e 40 05 3c 0a ac b7 b6                            ~@.<....

	// 40 = 開始, 41 = 時間, 42 = 清單, 43 = 圈數, 44 = 排名, 45 = 超過, 45 = 結束
	/*開始*/
	private void buildPacket1(L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(0x40);
        writeC(0x05); // 속도
        writeC(0); 
        writeC(0); 
        writeC(0); 
        writeC(0); 
        writeC(0); 

   /*     writeC(0x81); 
        writeC(0xfc); 
        writeC(0x7d); 
        writeC(0x6e); 
        writeC(0x11); */


	 }

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_GameStart;
	}
}
