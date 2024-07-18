package l1j.server.server.serverpackets;

import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.server.Opcodes;

public class S_Petskill extends ServerBasePacket {


	private static final String S_PetWindow = "[S] S_PetWindow";
	
	private byte[] _byte = null;

	public static final int DogBlood    = 14;   /** 瘋狗的血 */
	
	public S_Petskill(int Op, MJCompanionInstance Pet, boolean check) {
		buildPacket(Op, Pet, check);
	}
	
	private void buildPacket(int Op, MJCompanionInstance Pet, boolean check) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0xd0);
		writeC(0x07);
		
		writeC(0x08);
		writeBit(Pet.getId());
		
		switch (Op) {	
			case DogBlood:
				writeC(0x90);
				writeC(0x02);
				if(check){
					writeBit(0x64);
				}else writeBit(0x0c);
				break;
		}
		writeH(0x00);
	}

	@Override
	public String getType() {
		return S_PetWindow;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}


