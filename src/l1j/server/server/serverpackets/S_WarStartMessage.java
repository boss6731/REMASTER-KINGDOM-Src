package l1j.server.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;

public class S_WarStartMessage extends l1j.server.server.serverpackets.ServerBasePacket {

	private static final String[] clanNames = new String[]{"肯特城", "半獸人堡壘", "風木城", "奇岩城", "海音城", "地底城", "亞丁城"};
	public static S_WarStartMessage get(){
		S_WarStartMessage p = new S_WarStartMessage();
		for(L1Clan clan : L1World.getInstance().getAllClans()){
			if(clan.getCastleId() - 1 > 0 && clan.getCastleId() - 1 < clanNames.length)
				clanNames[clan.getCastleId() - 1] = clan.getClanName();
		}

		for(int i=0; i<clanNames.length; i++)
			p.writeS(clanNames[i]);

		return p;
	}

	private S_WarStartMessage(){
		writeC(Opcodes.S_EVENT);
		writeC(0x4E);
		writeC(0x07);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "S_WarStartMessage";
	}
}
