package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。由 MJSoft 製作。
public class RevengeInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static RevengeInfoT newInstance() {
		return new RevengeInfoT();
	}

	private int _register_timestamp;
	private int _unregister_duration;
	private RevengeInfoT.eAction _action_type;
	private RevengeInfoT.eResult _action_result;
	private int _action_timestamp;
	private int _action_duration;
	private int _action_remain_count;
	private int _action_count;
	private int _crimescene_server_no;
	private int _user_uid;
	private int _server_no;
	private int _game_class;
	private String _user_name;
	private int _pledge_id;
	private String _pledge_name;
	private boolean _active;
	private int _activate_duration;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private RevengeInfoT() {
	}

	public int get_register_timestamp() {
		return _register_timestamp;
	}

	public void set_register_timestamp(int val) {
		_bit |= 0x1;
		_register_timestamp = val;
	}

	public boolean has_register_timestamp() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_unregister_duration() {
		return _unregister_duration;
	}

	public void set_unregister_duration(int val) {
		_bit |= 0x2;
		_unregister_duration = val;
	}

	public boolean has_unregister_duration() {
		return (_bit & 0x2) == 0x2;
	}

	public RevengeInfoT.eAction get_action_type() {
		return _action_type;
	}

	public void set_action_type(RevengeInfoT.eAction val) {
		_bit |= 0x4;
		_action_type = val;
	}

	public boolean has_action_type() {
		return (_bit & 0x4) == 0x4;
	}

	public RevengeInfoT.eResult get_action_result() {
		return _action_result;
	}

	public void set_action_result(RevengeInfoT.eResult val) {
		_bit |= 0x8;
		_action_result = val;
	}

	public boolean has_action_result() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_action_timestamp() {
		return _action_timestamp;
	}

	public void set_action_timestamp(int val) {
		_bit |= 0x10;
		_action_timestamp = val;
	}

	public boolean has_action_timestamp() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_action_duration() {
		return _action_duration;
	}

	public void set_action_duration(int val) {
		_bit |= 0x20;
		_action_duration = val;
	}

	public boolean has_action_duration() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_action_remain_count() {
		return _action_remain_count;
	}

	public void set_action_remain_count(int val) {
		_bit |= 0x40;
		_action_remain_count = val;
	}

	public boolean has_action_remain_count() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_action_count() {
		return _action_count;
	}

	public void set_action_count(int val) {
		_bit |= 0x80;
		_action_count = val;
	}

	public boolean has_action_count() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_crimescene_server_no() {
		return _crimescene_server_no;
	}

	public void set_crimescene_server_no(int val) {
		_bit |= 0x100;
		_crimescene_server_no = val;
	}

	public boolean has_crimescene_server_no() {
		return (_bit & 0x100) == 0x100;
	}

	public int get_user_uid() {
		return _user_uid;
	}

	public void set_user_uid(int val) {
		_bit |= 0x200;
		_user_uid = val;
	}

	public boolean has_user_uid() {
		return (_bit & 0x200) == 0x200;
	}

	public int get_server_no() {
		return _server_no;
	}

	public void set_server_no(int val) {
		_bit |= 0x400;
		_server_no = val;
	}

	public boolean has_server_no() {
		return (_bit & 0x400) == 0x400;
	}

	public int get_game_class() {
		return _game_class;
	}

	public void set_game_class(int val) {
		_bit |= 0x800;
		_game_class = val;
	}

	public boolean has_game_class() {
		return (_bit & 0x800) == 0x800;
	}

	public String get_user_name() {
		return _user_name;
	}

	public void set_user_name(String val) {
		_bit |= 0x1000;
		_user_name = val;
	}

	public boolean has_user_name() {
		return (_bit & 0x1000) == 0x1000;
	}

	public int get_pledge_id() {
		return _pledge_id;
	}

	public void set_pledge_id(int val) {
		_bit |= 0x2000;
		_pledge_id = val;
	}

	public boolean has_pledge_id() {
		return (_bit & 0x2000) == 0x2000;
	}

	public String get_pledge_name() {
		return _pledge_name;
	}

	public void set_pledge_name(String val) {
		_bit |= 0x4000;
		_pledge_name = val;
	}

	public boolean has_pledge_name() {
		return (_bit & 0x4000) == 0x4000;
	}

	public boolean get_active() {
		return _active;
	}

	public void set_active(boolean val) {
		_bit |= 0x8000;
		_active = val;
	}

	public boolean has_active() {
		return (_bit & 0x8000) == 0x8000;
	}

	public int get_activate_duration() {
		return _activate_duration;
	}

	public void set_activate_duration(int val) {
		_bit |= 0x10000;
		_activate_duration = val;
	}

	public boolean has_activate_duration() {
		return (_bit & 0x10000) == 0x10000;
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
		if (has_register_timestamp()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _register_timestamp);
		}
		if (has_unregister_duration()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _unregister_duration);
		}
		if (has_action_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _action_type.toInt());
		}
		if (has_action_result()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _action_result.toInt());
		}
		if (has_action_timestamp()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _action_timestamp);
		}
		if (has_action_duration()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(6, _action_duration);
		}
		if (has_action_remain_count()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(7, _action_remain_count);
		}
		if (has_action_count()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(8, _action_count);
		}
		if (has_crimescene_server_no()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(9, _crimescene_server_no);
		}
		if (has_user_uid()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(10, _user_uid);
		}
		if (has_server_no()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(11, _server_no);
		}
		if (has_game_class()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(12, _game_class);
		}
		if (has_user_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(13, _user_name);
		}
		if (has_pledge_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(14, _pledge_id);
		}
		if (has_pledge_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(15, _pledge_name);
		}
		if (has_active()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(16, _active);
		}
		if (has_activate_duration()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(17, _activate_duration);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_register_timestamp()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_unregister_duration()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_action_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_action_result()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_action_timestamp()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_action_duration()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_action_count()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_crimescene_server_no()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_user_uid()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_server_no()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_game_class()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_user_name()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_pledge_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_pledge_name()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_active()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_register_timestamp()) {
			output.writeUInt32(1, _register_timestamp);
		}
		if (has_unregister_duration()) {
			output.writeUInt32(2, _unregister_duration);
		}
		if (has_action_type()) {
			output.writeEnum(3, _action_type.toInt());
		}
		if (has_action_result()) {
			output.writeEnum(4, _action_result.toInt());
		}
		if (has_action_timestamp()) {
			output.writeUInt32(5, _action_timestamp);
		}
		if (has_action_duration()) {
			output.writeUInt32(6, _action_duration);
		}
		if (has_action_remain_count()) {
			output.writeUInt32(7, _action_remain_count);
		}
		if (has_action_count()) {
			output.writeUInt32(8, _action_count);
		}
		if (has_crimescene_server_no()) {
			output.writeUInt32(9, _crimescene_server_no);
		}
		if (has_user_uid()) {
			output.writeUInt32(10, _user_uid);
		}
		if (has_server_no()) {
			output.writeUInt32(11, _server_no);
		}
		if (has_game_class()) {
			output.writeUInt32(12, _game_class);
		}
		if (has_user_name()) {
			output.writeString(13, _user_name);
		}
		if (has_pledge_id()) {
			output.writeUInt32(14, _pledge_id);
		}
		if (has_pledge_name()) {
			output.writeString(15, _pledge_name);
		}
		if (has_active()) {
			output.writeBool(16, _active);
		}
		if (has_activate_duration()) {
			output.writeUInt32(17, _activate_duration);
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
					set_register_timestamp(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_unregister_duration(input.readUInt32());
					break;
				}
				case 0x00000018: {
					set_action_type(RevengeInfoT.eAction.fromInt(input.readEnum()));
					break;
				}
				case 0x00000020: {
					set_action_result(RevengeInfoT.eResult.fromInt(input.readEnum()));
					break;
				}
				case 0x00000028: {
					set_action_timestamp(input.readUInt32());
					break;
				}
				case 0x00000030: {
					set_action_duration(input.readUInt32());
					break;
				}
				case 0x00000038: {
					set_action_remain_count(input.readUInt32());
					break;
				}
				case 0x00000040: {
					set_action_count(input.readUInt32());
					break;
				}
				case 0x00000048: {
					set_crimescene_server_no(input.readUInt32());
					break;
				}
				case 0x00000050: {
					set_user_uid(input.readUInt32());
					break;
				}
				case 0x00000058: {
					set_server_no(input.readUInt32());
					break;
				}
				case 0x00000060: {
					set_game_class(input.readUInt32());
					break;
				}
				case 0x0000006A: {
					set_user_name(input.readString());
					break;
				}
				case 0x00000070: {
					set_pledge_id(input.readUInt32());
					break;
				}
				case 0x0000007A: {
					set_pledge_name(input.readString());
					break;
				}
				case 0x00000080: {
					set_active(input.readBool());
					break;
				}
				case 0x00000088: {
					set_activate_duration(input.readUInt32());
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

			// TODO：從下面插入處理程式碼。由 MJSoft 製作。

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new RevengeInfoT();
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

	public enum eResult {
		NONE(0),
		LOSE(1),
		WIN(2),
		;

		private int value;

		eResult(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eResult v) {
			return value == v.value;
		}

		public static eResult fromInt(int i) {
			switch (i) {
				case 0:
					return NONE;
				case 1:
					return LOSE;
				case 2:
					return WIN;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eResult，%d", i));
			}
		}
	}

	public enum eAction {
		UNKNOWN(0),
		TAUNT(1),
		PURSUIT(2),
		;

		private int value;

		eAction(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eAction v) {
			return value == v.value;
		}

		public static eAction fromInt(int i) {
			switch (i) {
				case 0:
					return UNKNOWN;
				case 1:
					return TAUNT;
				case 2:
					return PURSUIT;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eAction，%d", i));
			}
		}
	}
}
