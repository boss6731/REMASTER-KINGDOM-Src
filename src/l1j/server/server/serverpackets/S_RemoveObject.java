package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Object;

public class S_RemoveObject extends ServerBasePacket {
	private static final String S_RemoveObject = "[S] S_RemoveObject";

	public S_RemoveObject(L1Object obj) {
		writeC(Opcodes.S_REMOVE_OBJECT);
		writeD(obj.getId());
		writeH(0);
	}
	
	public S_RemoveObject(int objId) {
		writeC(Opcodes.S_REMOVE_OBJECT);
		writeD(objId);
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	public String getType() {
		return S_RemoveObject;
	}
}


