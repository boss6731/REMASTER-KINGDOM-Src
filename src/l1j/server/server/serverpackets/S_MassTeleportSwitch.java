package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_MassTeleportSwitch extends ServerBasePacket {
	private static final String S_MASS_TELEPORT_SWITCH = "[S] S_MassTeleportSwitch";
	private byte[] _byte = null;
	
	public static final int SWITCH	= 0x0a22;
	
	public static final S_MassTeleportSwitch ON		= new S_MassTeleportSwitch(1);
	public static final S_MassTeleportSwitch OFF	= new S_MassTeleportSwitch(0);
	
	public S_MassTeleportSwitch(int value){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SWITCH);
		
		writeC(0x08);
		writeC(value);

        writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = _bao.toByteArray();
		return _byte;
	}

	@Override
	public String getType() {
		return S_MASS_TELEPORT_SWITCH;
	}
}


