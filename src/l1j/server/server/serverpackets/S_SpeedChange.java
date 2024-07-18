package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SpeedChange extends ServerBasePacket {
	private static final String S_SPEED_CHANGE = "[S] S_SpeedChange";
	private byte[] _byte = null;
	public static final int SPEED							= 0x040c;
	
	public static final byte[] BANGUARD_SHORT_FORM_ON		= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x00, (byte)0x10, (byte)0x23, (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x01, (byte)0x10, (byte)0x0f };
	public static final byte[] BANGUARD_SHORT_FORM_OFF		= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x00, (byte)0x10, (byte)0x00, (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x01, (byte)0x10, (byte)0x00 };
	public static final byte[] BANGUARD_LONG_FORM_ON		= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x01, (byte)0x10, (byte)0x0a };
	public static final byte[] BANGUARD_LONG_FORM_OFF		= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x01, (byte)0x10, (byte)0x00 };
	public static final byte[] SHOCK_ATTACK_ON				= { (byte)0x0a, (byte)0x0d, (byte)0x08, (byte)0x00, (byte)0x10, (byte)0xdd, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x01 };
	public static final byte[] SHOCK_ATTACK_OFF				= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x00, (byte)0x10, (byte)0x00 };
	public static final byte[] RAIGING_WEAPONE_ATTACK_ON	= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x01, (byte)0x10, (byte)0x0a };
	public static final byte[] RAIGING_WEAPONE_ATTACK_OFF	= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x01, (byte)0x10, (byte)0x00 };
	public static final byte[] BLIND_HIDING_ON				= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x00, (byte)0x10, (byte)0x23 };
	public static final byte[] BLIND_HIDING_OFF				= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x00, (byte)0x10, (byte)0x00 };
	public static final byte[] MADO_ON			        	= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x00, (byte)0x10, (byte)0x05 };
	public static final byte[] MADO_OFF				        = { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x00, (byte)0x10, (byte)0x00 };
	public static final byte[] ENSNARE_ON		        	= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x00, (byte)0x10, (byte)-50 };
	public static final byte[] ENSNARE_OFF		        	= { (byte)0x0a, (byte)0x04, (byte)0x08, (byte)0x00, (byte)0x10, (byte)0 };
	
	public static final int SHOCK_ATTACK					= 1;
	public static final int RAIGING_WEAPONE					= 2;
	public static final int BLIND_HIDING					= 3;
	public static final int MADO         					= 4;
	public static final int ENSNARE         				= 5;
	
	public S_SpeedChange(L1PcInstance pc, boolean longForm, boolean onoff) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SPEED);
		
		writeC(0x08);
		writeBit(pc.getId());
		
		writeC(0x12);
		byte[] banguardBytes = longForm ? (onoff ? BANGUARD_LONG_FORM_ON : BANGUARD_LONG_FORM_OFF) : (onoff ? BANGUARD_SHORT_FORM_ON : BANGUARD_SHORT_FORM_OFF);
		writeBytesWithLength(banguardBytes);

        writeH(0x00);
	}
	
	public S_SpeedChange(L1PcInstance pc, int type, boolean onoff) {
		byte[] speedBytes = null;
		switch(type){
		case SHOCK_ATTACK:		speedBytes = onoff ? SHOCK_ATTACK_ON : SHOCK_ATTACK_OFF;break;
		case RAIGING_WEAPONE:	speedBytes = onoff ? RAIGING_WEAPONE_ATTACK_ON : RAIGING_WEAPONE_ATTACK_OFF;break;
		case BLIND_HIDING:		speedBytes = onoff ? BLIND_HIDING_ON : BLIND_HIDING_OFF;break;
		case MADO:	         	speedBytes = onoff ? MADO_ON : MADO_OFF;break;
		case ENSNARE:           speedBytes = onoff ? ENSNARE_ON : ENSNARE_OFF;break;
		
		}
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SPEED);
		
		writeC(0x08);
		writeBit(pc.getId());
			
		writeC(0x12);
		writeBytesWithLength(speedBytes);

        writeH(0x00);
	}
	
	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = _bao.toByteArray();
		return _byte;
	}

	@Override
	public String getType() {
		return S_SPEED_CHANGE;
	}
}


