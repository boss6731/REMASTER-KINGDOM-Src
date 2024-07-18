package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.MJCommons;

public class S_SmeltingEject extends ServerBasePacket {

		
	public static S_SmeltingEject send(L1Character pc, L1ItemInstance item){
		S_SmeltingEject s = new S_SmeltingEject();
		s.writeC(0x0a);
		s.writeC(0x45);

		
/*		s.writeC(act);
		s.writeD(own.getId());
		s.writeH(own.getX());
		s.writeH(own.getY());
		s.writeC(own.getHeading());
		s.writeD(_sequence.incrementAndGet());
		s.writeH(spr);
		s.writeC(0x00);
		s.writeH(0x00);
		s.writeH(tars.length);
		for(L1Character c : tars){
			s.writeD(c.getId());
			s.writeC(0x20);
			s.writeC(ActionCodes.ACTION_Damage);
		}*/
		
		s.writeH(0x00);
		return s;
	}
	

	private S_SmeltingEject(){
		writeC(Opcodes.S_EVENT);
	}
	
	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}




}
