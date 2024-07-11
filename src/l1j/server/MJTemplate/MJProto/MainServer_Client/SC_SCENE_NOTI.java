package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.Controller.EventThread;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_SCENE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static final String ZEROS = "astarot_wake";
	public static final String BAPHOMET = "baphomet_wake";
	public static final String BARLOG = "barlog_wake";
	public static final String BIGDRAKE = "bigdrake_wake";
	public static final String DEMON = "demon_wake";
	public static final String DEATH_KNIGHT = "dk_wake";
	public static final String JOOMOUT = "hidden_wake";
	public static final String HIDDEN_REPPER = "hidden_wake1";
	public static final String ATUBAKING = "king_wake";
	public static final String START_FOG = "mainfog";
	public static final String PBARLOG = "Pbarlog_wake";
	public static final String PHOENIX = "phoenix_wake";
	public static final String REAPER = "reaper_wake";
	public static final String STAGE2 = "stage2";
	public static final String STAGE3 = "stage3";
	public static final String STAGE4 = "stage4";
	public static final String STAGE5 = "stage5";
	public static final String CT_BOSS = "CT_boss";
	public static final String CT_LASTBOSS2 = "CT_lastboss2";
	public static final String CT_LASTBOSS3 = "CT_lastboss3";
	public static final String CT_NIGHTMARE_BOSS = "CT_nightmare_boss";
	public static final String CT_FOG = "CT_fog";
	public static final String KIKAM_FULL = "54map_kikam_full";
	public static final String KIKAM_SMOKE = "54map_smoke";
	public static final String KIKAM_BOSS = "54map_kikam_boss";
	public static final String MAP54_DISABLE = "54map_disable";

	public static ProtoOutputStream make_stream(String name) {
		SC_SCENE_NOTI noti = newInstance();
		noti.set_script_name(name);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_SCENE_NOTI);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream make_stream(L1PcInstance pc, boolean on) {
		SC_SCENE_NOTI noti = newInstance();
		noti.set_scene_id(Integer.toString(EventThread.getInstance().num1));
		noti.set_enable(on);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_SCENE_NOTI);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream make_stream(boolean scriptType, long currentTime, int scriptName, int x, int y) {
		SC_SCENE_NOTI noti = newInstance();

		if (x > 0 && y > 0) {
			noti.set_pos_x(x);
			noti.set_pos_y(y);
		}

		noti.set_script_start_time(currentTime);
		if (scriptType) {
			noti.set_script_number(scriptName);
		} else {
			noti.set_disable_script_number(scriptName);
		}
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_SCENE_NOTI);
		noti.dispose();

		return stream;
	}

	public static SC_SCENE_NOTI newInstance() {
		return new SC_SCENE_NOTI();
	}

	private String _scene_id;
	private boolean _enable;
	private String _script_name;
	private String _disable_script_name;
	private String _region;
	private long _script_start_time;
	private int _pos_x;
	private int _pos_y;
	private int _script_number;
	private int _disable_script_number;
	private int _object_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_SCENE_NOTI() {
	}

	public String get_scene_id() {
		return _scene_id;
	}

	public void set_scene_id(String val) {
		_bit |= 0x1;
		_scene_id = val;
	}

	public boolean has_scene_id() {
		return (_bit & 0x1) == 0x1;
	}

	public boolean get_enable() {
		return _enable;
	}

	public void set_enable(boolean val) {
		_bit |= 0x2;
		_enable = val;
	}

	public boolean has_enable() {
		return (_bit & 0x2) == 0x2;
	}

	public String get_script_name() {
		return _script_name;
	}

	public void set_script_name(String val) {
		_bit |= 0x4;
		_script_name = val;
	}

	public boolean has_script_name() {
		return (_bit & 0x4) == 0x4;
	}

	public String get_disable_script_name() {
		return _disable_script_name;
	}

	public void set_disable_script_name(String val) {
		_bit |= 0x8;
		_disable_script_name = val;
	}

	public boolean has_disable_script_name() {
		return (_bit & 0x8) == 0x8;
	}

	public String get_region() {
		return _region;
	}

	public void set_region(String val) {
		_bit |= 0x10;
		_region = val;
	}

	public boolean has_region() {
		return (_bit & 0x10) == 0x10;
	}

	public long get_script_start_time() {
		return _script_start_time;
	}

	public void set_script_start_time(long val) {
		_bit |= 0x20;
		_script_start_time = val;
	}

	public boolean has_script_start_time() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_pos_x() {
		return _pos_x;
	}

	public void set_pos_x(int val) {
		_bit |= 0x40;
		_pos_x = val;
	}

	public boolean has_pos_x() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_pos_y() {
		return _pos_y;
	}

	public void set_pos_y(int val) {
		_bit |= 0x80;
		_pos_y = val;
	}

	public boolean has_pos_y() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_script_number() {
		return _script_number;
	}

	public void set_script_number(int val) {
		_bit |= 0x100;
		_script_number = val;
	}

	public boolean has_script_number() {
		return (_bit & 0x100) == 0x100;
	}

	public int get_disable_script_number() {
		return _disable_script_number;
	}

	public void set_disable_script_number(int val) {
		_bit |= 0x200;
		_disable_script_number = val;
	}

	public boolean has_disable_script_number() {
		return (_bit & 0x200) == 0x200;
	}

	public int get_object_id() {
		return _object_id;
	}

	public void set_object_id(int val) {
		_bit |= 0x400;
		_object_id = val;
	}

	public boolean has_object_id() {
		return (_bit & 0x400) == 0x400;
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
		if (has_scene_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _scene_id);
		}
		if (has_enable()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _enable);
		}
		if (has_script_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _script_name);
		}
		if (has_disable_script_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _disable_script_name);
		}
		if (has_region()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(5, _region);
		}
		if (has_script_start_time()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(6, _script_start_time);
		}
		if (has_pos_x()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _pos_x);
		}
		if (has_pos_y()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _pos_y);
		}
		if (has_script_number()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _script_number);
		}
		if (has_disable_script_number()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _disable_script_number);
		}
		if (has_object_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(11, _object_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_scene_id()) {
			output.writeString(1, _scene_id);
		}
		if (has_enable()) {
			output.writeBool(2, _enable);
		}
		if (has_script_name()) {
			output.writeString(3, _script_name);
		}
		if (has_disable_script_name()) {
			output.writeString(4, _disable_script_name);
		}
		if (has_region()) {
			output.writeString(5, _region);
		}
		if (has_script_start_time()) {
			output.wirteUInt64(6, _script_start_time);
		}
		if (has_pos_x()) {
			output.wirteInt32(7, _pos_x);
		}
		if (has_pos_y()) {
			output.wirteInt32(8, _pos_y);
		}
		if (has_script_number()) {
			output.wirteInt32(9, _script_number);
		}
		if (has_disable_script_number()) {
			output.wirteInt32(10, _disable_script_number);
		}
		if (has_object_id()) {
			output.writeUInt32(11, _object_id);
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
				case 0x0000000A: {
					set_scene_id(input.readString());
					break;
				}
				case 0x00000010: {
					set_enable(input.readBool());
					break;
				}
				case 0x0000001A: {
					set_script_name(input.readString());
					break;
				}
				case 0x00000022: {
					set_disable_script_name(input.readString());
					break;
				}
				case 0x0000002A: {
					set_region(input.readString());
					break;
				}
				case 0x00000030: {
					set_script_start_time(input.readUInt64());
					break;
				}
				case 0x00000038: {
					set_pos_x(input.readInt32());
					break;
				}
				case 0x00000040: {
					set_pos_y(input.readInt32());
					break;
				}
				case 0x00000048: {
					set_script_number(input.readInt32());
					break;
				}
				case 0x00000050: {
					set_disable_script_number(input.readInt32());
					break;
				}
				case 0x00000058: {
					set_object_id(input.readUInt32());
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_SCENE_NOTI();
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
