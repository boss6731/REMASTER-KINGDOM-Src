package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;

/**
 * 20221002 變更
 * @作者 Shaman
 *
 */
public class S_MoveCharPacket extends ServerBasePacket {






	private static final String _S__1F_MOVECHARPACKET = "[S] S_MoveCharPacket";

	public static S_MoveCharPacket moveAbsolute(L1Character character) {
		S_MoveCharPacket pack = new S_MoveCharPacket();
		pack.writeD(character.getId());
		pack.writeH(character.getX());
		pack.writeH(character.getY());
		pack.writeC(character.getHeading());
		pack.writeC(129);
		pack.writeD(0);
		return pack;
	}
	
	public S_MoveCharPacket() {
		writeC(Opcodes.S_MOVE_OBJECT);
	}
	
	public S_MoveCharPacket(int object_id, int x, int y, int h){
		switch (h) {
		case 1: x--; y++; break;
		case 2: x--; break;
		case 3: x--; y--; break;
		case 4: y--; break;
		case 5: x++; y--; break;
		case 6: x++; break;
		case 7: x++; y++;break;
		case 0: y++; break;
		}
		
		writeC(Opcodes.S_MOVE_OBJECT);
		writeD(object_id);
		writeH(x);
		writeH(y);
		writeC(h);
		writeC(129);
		writeD(0);
	}
	
	public S_MoveCharPacket(L1Character cha) {
		int x = cha.getX();
		int y = cha.getY();

		switch (cha.getHeading()) {
		case 1: x--; y++; break;
		case 2: x--; break;
		case 3: x--; y--; break;
		case 4: y--; break;
		case 5: x++; y--; break;
		case 6: x++; break;
		case 7: x++; y++;break;
		case 0: y++; break;
		}

		writeC(Opcodes.S_MOVE_OBJECT);
		writeD(cha.getId());
		writeH(x);
		writeH(y);
		writeC(cha.getHeading());
		writeC(129);
		writeD(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__1F_MOVECHARPACKET;
	}
}


