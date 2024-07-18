package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CollectionNoti extends ServerBasePacket {
	private static final String S_COLLECTION_NOTI = "[S] S_CollectionNoti";
	private byte[] _byte = null;
	
	public static final int NOTI		= 0x0a5d;
	
	public static final S_CollectionNoti LOGIN_START = new S_CollectionNoti();
	
	public S_CollectionNoti(){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_COLLECTION_NOTI;
	}
}


