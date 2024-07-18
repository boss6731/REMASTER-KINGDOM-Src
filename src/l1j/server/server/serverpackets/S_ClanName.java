package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ClanName extends ServerBasePacket {
	private static final String S_ClanName = "[S] S_ClanName";

	public S_ClanName(L1PcInstance pc, int emblemId, int rank) {
		writeC(Opcodes.S_PLEDGE);
		writeD(pc.getId());
		writeS(rank > 0 ? pc.getClanname() : "");
		writeC(0);
		writeD(emblemId);		
		writeC(rank > 0 ? rank : 0x00);
		writeH(0x00);
	}

	public S_ClanName(L1PcInstance pc){
		writeC(Opcodes.S_PLEDGE);
		writeD(pc.getId());
		writeD(0x00);
	}
	
	public S_ClanName(L1PcInstance pc, int a, int b, int c){
		writeC(Opcodes.S_PLEDGE);
		writeD(pc.getId());
		writeC(0xba);
		writeC(0xce);
		writeC(0xb6);
		writeC(0xf6);
		writeC(0xc5);
		writeC(0xab);
		writeC(0xc5);
		writeC(0xb8);
		writeC(0xc0);
		writeC(0xcc);
		writeC(0xb0);
		writeC(0xc5);
		
		writeC(0x00);
		writeC(0x00);
		
		writeC(0xdd);
		writeC(0x03);
		writeC(0x00);
		writeC(0x64);
		writeC(0x07);
		writeC(0x07);
		writeC(0x00);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_ClanName;
	}
}



