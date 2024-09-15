package l1j.server.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Fishing extends ServerBasePacket {

    public S_Fishing(int t) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
        int fishUI = 63;
        writeH(fishUI);
		writeH(264);
		writeC(16);
		writeBit(t);
		writeBit(0x18);
		writeBit(0x02);
		writeH(0);
	}

	public S_Fishing(int type, int test, int time, String CName, boolean swich) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
        int CAUI = 76;
        writeH(CAUI);
		writeH(264);
		writeC(16);
		if (swich) {
			writeH(test);
			writeH(time);
			writeS(CName);
		} else {
			writeC(0);
		}
		writeH(0);
	}

	public S_Fishing(int id, int actionFishing, int fx, int fy) {

	}

	public byte[] getContent() {
		return getBytes();
	}







}


