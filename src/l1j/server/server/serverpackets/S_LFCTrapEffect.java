package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_LFCTrapEffect extends ServerBasePacket {

	public S_LFCTrapEffect(int objId, int gfxId){
		writeC(Opcodes.S_EFFECT);
		writeD(objId);
		writeH(gfxId);
	}
	public S_LFCTrapEffect(int x, int y, int gfxId){
		writeC(Opcodes.S_EFFECT_LOC);
		writeH(x);
		writeH(y);
		writeH(gfxId);
		writeH(0);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "S_LFCTrapEffect";
	}
}


