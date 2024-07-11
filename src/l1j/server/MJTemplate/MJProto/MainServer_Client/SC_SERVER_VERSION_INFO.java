package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_SERVER_VERSION_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	private static SC_SERVER_VERSION_INFO _version;
	private static ProtoOutputStream _denial;
	private static ProtoOutputStream _denial_client_over;

	// 0 : 정상
	// 1 : 버전이 맞지 않음
	// 2 : 오류가 발생
	// 3 : 한 컴퓨터에서 동시에 실행할 수 있는 리니지 클라이언트 수를 초과
	// 4 : 리소스가 변조되었습니다.
	public static void send(GameClient clnt) {
		_version.set_game_real_time((int) (System.currentTimeMillis() / 1000L));
		clnt.sendPacket(_version.writeTo(MJEProtoMessages.SC_SERVER_VERSION_INFO), true);
	}

	private static void do_denial(GameClient clnt, ProtoOutputStream stream) {
		clnt.sendPacket(stream, false);
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				try {
					clnt.close();
				} catch (Exception e) {
				}
			}
		}, 3000L);
	}

	public static void denial_client_over(GameClient clnt) {
		if (_denial_client_over == null) {
			SC_SERVER_VERSION_INFO denial = new SC_SERVER_VERSION_INFO();
			denial.set_version_check(3);
			_denial_client_over = denial.writeTo(MJEProtoMessages.SC_SERVER_VERSION_INFO);
			denial.dispose();
		}
		do_denial(clnt, _denial_client_over);
	}

	public static void denial(GameClient clnt) {
		if (_denial == null) {
			SC_SERVER_VERSION_INFO denial = new SC_SERVER_VERSION_INFO();
			denial.set_version_check(1);
			_denial = denial.writeTo(MJEProtoMessages.SC_SERVER_VERSION_INFO);
			denial.dispose();
		}
		do_denial(clnt, _denial);
	}

	public static SC_SERVER_VERSION_INFO newInstance() {
		if (_version == null) {
			_version = new SC_SERVER_VERSION_INFO();
			_version.set_version_check(0);
			_version.set_server_id(0);
			// _version.set_build_number(Integer.toUnsignedString(MJNetSafeLoadManager.NS_CLIENT_VERSION));
			_version.set_build_number(MJNetSafeLoadManager.NS_CLIENT_VERSION);
			_version.set_cache_version(MJNetSafeLoadManager.NS_CLIENT_VERSION);
			_version.set_auth_version(MJNetSafeLoadManager.NS_AUTHSERVER_VERSION);
			_version.set_npc_server_version(MJNetSafeLoadManager.NS_CLIENT_VERSION);
			_version.set_status_start_time((int) (System.currentTimeMillis() / 1000L));
			_version.set_english_only_config(0x00);
			_version.set_country_code(0x00);
			_version.set_client_setting_switch(MJNetSafeLoadManager.NS_CLIENT_SETTING_SWITCH);
			_version.set_global_cache_version(MJNetSafeLoadManager.NS_CACHESERVER_VERSION);
			_version.set_tam_server_version(MJNetSafeLoadManager.NS_TAMSERVER_VERSION);
			_version.set_arca_server_version(MJNetSafeLoadManager.NS_ARCASERVER_VERSION);
			_version.set_hibreed_inter_server_version(MJNetSafeLoadManager.NS_HIBREED_INTERSERVER_VERSION);
			_version.set_arenaco_server_version(MJNetSafeLoadManager.NS_ARENACOSERVER_VERSION);
			_version.set_server_type(0x00);
			_version.set_broker_server_version(202211001);
			_version.set_ai_agent_dll_version(10091);

		}
		return _version;
	}

	public void do_print() {
		System.out.println(String.format("[版本檢查]\t%d(%08X)", _version_check, _version_check));
		System.out.println(String.format("[伺服器ID]\t%d(%08X)", _server_id, _server_id));
		System.out.println(String.format("[構建號]\t%s(%08X)", Integer.toUnsignedString(_build_number), _build_number));
		System.out.println(
				String.format("[緩存版本]\t%s(%08X)", Integer.toUnsignedString(_cache_version), _cache_version));
		System.out.println(String.format("[認證版本]\t%s(%08X)", _auth_version, _auth_version));
		System.out.println(
				String.format("[NPC版本]\t%s(%08X)", Integer.toUnsignedString(_npc_server_version), _
						pc_server_version));
		System.out.println(String.format("[僅英文配置]\t%s(%08X)", _english_only_config, _english_only_config));
		System.out.println(String.format("[國家代碼]\t%s(%08X)", _country_code, _country_code));
		System.out.println(String.format("[客戶端切換]\t%s(%08X)", _client_setting_switch, _client_setting_switch));
		System.out.println(String.format("[遊戲時間]\t%s(%08X)", _game_real_time, _game_real_time));
		System.out.println(String.format("[全局緩存版本]\t%s(%08X)", _global_cache_version, _global_cache_version));
		System.out.println(String.format("[TAM伺服器版本]\t%s(%08X)", _tam_server_version, _tam_server_version));
		System.out.println(String.format("[Arca伺服器版本]\t%s(%08X)", _arca_server_version, _arca_server_version));
		System.out.println(
				String.format("[混合伺服器版本]\t%s(%08X)", _hibreed_inter_server_version, _hibre
						d_inter_server_version));
		System.out
				.println(String.format("[ArenaCo伺服器版本]\t%s(%08X)", _arenaco_server_version, _are
						aco_server_version));
		System.out.println(String.format("[伺服器類型]\t%s(%08X)", _server_type, _server_type));
	}

	private int _version_check;
	private int _server_id;
	private int _build_number;
	// private long _build_number;
	private int _cache_version;
	private int _auth_version;
	private int _npc_server_version;
	private int _status_start_time;
	private int _english_only_config;
	private int _country_code;
	private int _client_setting_switch;
	private int _game_real_time;
	private int _global_cache_version;
	private int _tam_server_version;
	private int _arca_server_version;
	private int _hibreed_inter_server_version;
	private int _arenaco_server_version;
	private int _server_type;
	private int _broker_server_version;
	private long _ai_agent_dll_version;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_SERVER_VERSION_INFO() {
	}

	public int get_version_check() {
		return _version_check;
	}

	public void set_version_check(int val) {
		_bit |= 0x1;
		_version_check = val;
	}

	public boolean has_version_check() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_server_id() {
		return _server_id;
	}

	public void set_server_id(int val) {
		_bit |= 0x2;
		_server_id = val;
	}

	public boolean has_server_id() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_build_number() {
		return _build_number;
	}

	/*
	 * public long get_build_number(){
	 * return _build_number;
	 * }
	 */
	public void set_build_number(int val) {
		_bit |= 0x4;
		_build_number = val;
	}

	public boolean has_build_number() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_cache_version() {
		return _cache_version;
	}

	public void set_cache_version(int val) {
		_bit |= 0x8;
		_cache_version = val;
	}

	public boolean has_cache_version() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_auth_version() {
		return _auth_version;
	}

	public void set_auth_version(int val) {
		_bit |= 0x10;
		_auth_version = val;
	}

	public boolean has_auth_version() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_npc_server_version() {
		return _npc_server_version;
	}

	public void set_npc_server_version(int val) {
		_bit |= 0x20;
		_npc_server_version = val;
	}

	public boolean has_npc_server_version() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_status_start_time() {
		return _status_start_time;
	}

	public void set_status_start_time(int val) {
		_bit |= 0x40;
		_status_start_time = val;
	}

	public boolean has_status_start_time() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_english_only_config() {
		return _english_only_config;
	}

	public void set_english_only_config(int val) {
		_bit |= 0x80;
		_english_only_config = val;
	}

	public boolean has_english_only_config() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_country_code() {
		return _country_code;
	}

	public void set_country_code(int val) {
		_bit |= 0x100;
		_country_code = val;
	}

	public boolean has_country_code() {
		return (_bit & 0x100) == 0x100;
	}

	public int get_client_setting_switch() {
		return _client_setting_switch;
	}

	public void set_client_setting_switch(int val) {
		_bit |= 0x200;
		_client_setting_switch = val;
	}

	public boolean has_client_setting_switch() {
		return (_bit & 0x200) == 0x200;
	}

	public int get_game_real_time() {
		return _game_real_time;
	}

	public void set_game_real_time(int val) {
		_bit |= 0x400;
		_game_real_time = val;
	}

	public boolean has_game_real_time() {
		return (_bit & 0x400) == 0x400;
	}

	public int get_global_cache_version() {
		return _global_cache_version;
	}

	public void set_global_cache_version(int val) {
		_bit |= 0x800;
		_global_cache_version = val;
	}

	public boolean has_global_cache_version() {
		return (_bit & 0x800) == 0x800;
	}

	public int get_tam_server_version() {
		return _tam_server_version;
	}

	public void set_tam_server_version(int val) {
		_bit |= 0x1000;
		_tam_server_version = val;
	}

	public boolean has_tam_server_version() {
		return (_bit & 0x1000) == 0x1000;
	}

	public int get_arca_server_version() {
		return _arca_server_version;
	}

	public void set_arca_server_version(int val) {
		_bit |= 0x2000;
		_arca_server_version = val;
	}

	public boolean has_arca_server_version() {
		return (_bit & 0x2000) == 0x2000;
	}

	public int get_hibreed_inter_server_version() {
		return _hibreed_inter_server_version;
	}

	public void set_hibreed_inter_server_version(int val) {
		_bit |= 0x4000;
		_hibreed_inter_server_version = val;
	}

	public boolean has_hibreed_inter_server_version() {
		return (_bit & 0x4000) == 0x4000;
	}

	public int get_arenaco_server_version() {
		return _arenaco_server_version;
	}

	public void set_arenaco_server_version(int val) {
		_bit |= 0x8000;
		_arenaco_server_version = val;
	}

	public boolean has_arenaco_server_version() {
		return (_bit & 0x8000) == 0x8000;
	}

	public int get_server_type() {
		return _server_type;
	}

	public void set_server_type(int val) {
		_bit |= 0x10000;
		_server_type = val;
	}

	public boolean has_server_type() {
		return (_bit & 0x10000) == 0x10000;
	}

	public int get_broker_server_version() {
		return _broker_server_version;
	}

	public void set_broker_server_version(int val) {
		_bit |= 0x20000;
		_broker_server_version = val;
	}

	public boolean has_broker_server_version() {
		return (_bit & 0x20000) == 0x20000;
	}

	public long get_ai_agent_dll_version() {
		return _ai_agent_dll_version;
	}

	public void set_ai_agent_dll_version(long val) {
		_bit |= 0x40000;
		_ai_agent_dll_version = val;
	}

	public boolean has_ai_agent_dll_version() {
		return (_bit & 0x40000) == 0x40000;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		if (has_version_check()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _version_check);
		}
		if (has_server_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _server_id);
		}
		if (has_build_number()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _build_number);
			// size +=
			// l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(3,
			// _build_number);
		}
		if (has_cache_version()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _cache_version);
		}
		if (has_auth_version()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _auth_version);
		}
		if (has_npc_server_version()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(6, _npc_server_version);
		}
		if (has_status_start_time()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _status_start_time);
		}
		if (has_english_only_config()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _english_only_config);
		}
		if (has_country_code()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _country_code);
		}
		if (has_client_setting_switch()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _client_setting_switch);
		}
		if (has_game_real_time()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _game_real_time);
		}
		if (has_global_cache_version()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(12, _global_cache_version);
		}
		if (has_tam_server_version()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(13, _tam_server_version);
		}
		if (has_arca_server_version()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(14, _arca_server_version);
		}
		if (has_hibreed_inter_server_version()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(15,
					_hibreed_inter_server_version);
		}
		if (has_arenaco_server_version()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(16, _arenaco_server_version);
		}
		if (has_server_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(17, _server_type);
		}
		if (has_broker_server_version()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(18, _broker_server_version);
		}
		if (has_ai_agent_dll_version()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(19, _ai_agent_dll_version);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_version_check()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_version_check()) {
			output.writeUInt32(1, _version_check);
		}
		if (has_server_id()) {
			output.writeUInt32(2, _server_id);
		}
		if (has_build_number()) {
			output.writeUInt32(3, _build_number);
			// output.wirteUInt64(3, _build_number);
		}
		if (has_cache_version()) {
			output.writeUInt32(4, _cache_version);
		}
		if (has_auth_version()) {
			output.writeUInt32(5, _auth_version);
		}
		if (has_npc_server_version()) {
			output.writeUInt32(6, _npc_server_version);
		}
		if (has_status_start_time()) {
			output.wirteInt32(7, _status_start_time);
		}
		if (has_english_only_config()) {
			output.wirteInt32(8, _english_only_config);
		}
		if (has_country_code()) {
			output.wirteInt32(9, _country_code);
		}
		if (has_client_setting_switch()) {
			output.wirteInt32(10, _client_setting_switch);
		}
		if (has_game_real_time()) {
			output.wirteInt32(11, _game_real_time);
		}
		if (has_global_cache_version()) {
			output.writeUInt32(12, _global_cache_version);
		}
		if (has_tam_server_version()) {
			output.writeUInt32(13, _tam_server_version);
		}
		if (has_arca_server_version()) {
			output.writeUInt32(14, _arca_server_version);
		}
		if (has_hibreed_inter_server_version()) {
			output.writeUInt32(15, _hibreed_inter_server_version);
		}
		if (has_arenaco_server_version()) {
			output.writeUInt32(16, _arenaco_server_version);
		}
		if (has_server_type()) {
			output.wirteInt32(17, _server_type);
		}
		if (has_broker_server_version()) {
			output.writeUInt32(18, _broker_server_version);
		}
		if (has_ai_agent_dll_version()) {
			output.wirteUInt64(19, _ai_agent_dll_version);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
						message.toInt());
		try {
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				case 0x00000008: {
					set_version_check(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_server_id(input.readUInt32());
					break;
				}
				case 0x00000018: {
					set_build_number(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_cache_version(input.readUInt32());
					break;
				}
				case 0x00000028: {
					set_auth_version(input.readUInt32());
					break;
				}
				case 0x00000030: {
					set_npc_server_version(input.readUInt32());
					break;
				}
				case 0x00000038: {
					set_status_start_time(input.readInt32());
					break;
				}
				case 0x00000040: {
					set_english_only_config(input.readInt32());
					break;
				}
				case 0x00000048: {
					set_country_code(input.readInt32());
					break;
				}
				case 0x00000050: {
					set_client_setting_switch(input.readInt32());
					break;
				}
				case 0x00000058: {
					set_game_real_time(input.readInt32());
					break;
				}
				case 0x00000060: {
					set_global_cache_version(input.readUInt32());
					break;
				}
				case 0x00000068: {
					set_tam_server_version(input.readUInt32());
					break;
				}
				case 0x00000070: {
					set_arca_server_version(input.readUInt32());
					break;
				}
				case 0x00000078: {
					set_hibreed_inter_server_version(input.readUInt32());
					break;
				}
				case 0x00000080: {
					set_arenaco_server_version(input.readUInt32());
					break;
				}
				case 0x00000088: {
					set_server_type(input.readInt32());
					break;
				}
				case 0x00000090: {
					set_broker_server_version(input.readUInt32());
					break;
				}
				case 0x00000098: {
					set_ai_agent_dll_version(input.readUInt64());
					break;
				}
				default: {
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_SERVER_VERSION_INFO();
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}