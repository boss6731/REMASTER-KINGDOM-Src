
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_GameRanking extends ServerBasePacket {



	private static final String S_GameRanking = "[S] S_GameRanking";


//0000 : 7e 44 [c8 a3 b9 da 30 30 32] 00 [ac c0 01] 00 00 23    ~D....002......#
//0000 : 7e 44 [b8 c5 b4 cf c1 ae] 00 [13 e9 01] 00 [0e 27 1f]    ~D............'.
//0000 : 7e 44 [b3 c7 76 76] 00 [6b ea 01] 00 [51 c3 ad 29 09]    ~D..vv.k...Q..).
//0000 : 7e 44 [b0 dd c6 c4] 00 [d4 fd 01] 00 [69 63 6f 6d] 00    ~D.........icom.


	public S_GameRanking(L1PcInstance pc){
		buildPacket1(pc);
	}

	private void buildPacket1(L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(0x44);
        writeS(pc.getName());
        writeC(154);
        writeC(247);
        writeC(1);
        writeC(0);
	 }


	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_GameRanking;
	}
}


