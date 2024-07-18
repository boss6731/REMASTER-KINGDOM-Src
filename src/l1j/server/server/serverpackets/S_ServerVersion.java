package l1j.server.server.serverpackets;

import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.server.Opcodes;

public class S_ServerVersion extends ServerBasePacket {
	private static final String S_SERVER_VERSION = "[S] ServerVersion";
	private static long _startMs = 0;

	public S_ServerVersion() {
		super(128);
		if(_startMs <= 0)
			_startMs = (System.currentTimeMillis() / 1000);
		
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x0335);
		writeC(0x08);				// version_check
		writeC(0x00);			
		writeC(0x10);				// server_id
		writeC(0x00);				
		writeC(0x18);				// build number
		writeBit(MJNetSafeLoadManager.NS_CLIENT_VERSION);			
		writeC(0x20);				// cache_version
		writeBit(MJNetSafeLoadManager.NS_CLIENT_VERSION);		
		writeC(0x28);				// auth_version
		writeBit(MJNetSafeLoadManager.NS_AUTHSERVER_VERSION);			
		writeC(0x30);				// npc_server_version
		writeBit(MJNetSafeLoadManager.NS_CLIENT_VERSION);		
		writeC(0x38);				// status_start_time
		writeBit(_startMs);			
		writeC(0x40);				// english_only_config
		writeC(0x00);				
		writeC(0x48);				// country_code
		writeC(0x00);				
		writeC(0x50);				// client_setting_switch
		writeBit(MJNetSafeLoadManager.NS_CLIENT_SETTING_SWITCH);			
		writeC(0x58);				// game_real_time
		writeBit(System.currentTimeMillis() / 1000);	
		writeC(0x60);				// global_cache_version
		writeBit(MJNetSafeLoadManager.NS_CACHESERVER_VERSION);			
		writeC(0x68);				// tam_server_version
		writeBit(MJNetSafeLoadManager.NS_TAMSERVER_VERSION);			
		writeC(0x70);				// arca_server_version
		writeBit(MJNetSafeLoadManager.NS_ARCASERVER_VERSION);			
		writeC(0x78);				// hibreed_inter_server_version
		writeBit(MJNetSafeLoadManager.NS_HIBREED_INTERSERVER_VERSION);		
		
		writeC(0x80);				// arenaco_server_version
		writeC(0x01);
		writeBit(MJNetSafeLoadManager.NS_ARENACOSERVER_VERSION);
		
		writeC(0x88);				// server_type
		writeC(0x01);
		writeC(0x00);
		
		writeC(0x90);
		writeC(0x01);				//經紀商..母雞抖..伺服器版本
		writeBit(220221101);
		
		writeC(0x98);
		writeC(0x01);				//ai dll版本
		writeBit(10091);

		
		writeH(0x00);				
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_SERVER_VERSION;
	}
}


