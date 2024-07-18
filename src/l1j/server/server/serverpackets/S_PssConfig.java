package l1j.server.server.serverpackets;

import l1j.server.Config;
import l1j.server.server.Opcodes;

public class S_PssConfig extends ServerBasePacket {
	public static final int Pss_Config	= 0x09c5;
//	private static final byte[] config_byte	="LIVE_LIN_PSSCONFIG_24003492".getBytes();



	private static final byte[] config_byte	= "LIVE_LIN_PSSCONFIG_100020859349".getBytes();
	
	public static final S_PssConfig CONFIG		= new S_PssConfig();
			
//			LIVE_LIN_PSSCONFIG_100020859349
			 
	public S_PssConfig() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(Pss_Config);
		writeC(0x0a);
		writeBytesWithLength(config_byte);
//		writeH(0x00);
	}

	public static void init() {
		
	}

	public byte[] getContent() {
		return getBytes();
	}
}


