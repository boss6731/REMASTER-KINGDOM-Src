package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_ATTENDANCE_REFRESH_REWARD_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_ATTENDANCE_REFRESH_REWARD_ACK newInstance() {
		return new SC_ATTENDANCE_REFRESH_REWARD_ACK();
	}

	private SC_ATTENDANCE_REFRESH_REWARD_ACK.eErrorType _error;
	private int _attendance_id;
	private int _group_id;
	private int _refresh_id;
	private java.util.LinkedList<Integer> _random_item;
	private int _season_num;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ATTENDANCE_REFRESH_REWARD_ACK() {
	}

	public SC_ATTENDANCE_REFRESH_REWARD_ACK.eErrorType get_error() {
		return _error;
	}

	public void set_error(SC_ATTENDANCE_REFRESH_REWARD_ACK.eErrorType val) {
		_bit |= 0x1;
		_error = val;
	}

	public boolean has_error() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_attendance_id() {
		return _attendance_id;
	}

	public void set_attendance_id(int val) {
		_bit |= 0x2;
		_attendance_id = val;
	}

	public boolean has_attendance_id() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_group_id() {
		return _group_id;
	}

	public void set_group_id(int val) {
		_bit |= 0x4;
		_group_id = val;
	}

	public boolean has_group_id() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_refresh_id() {
		return _refresh_id;
	}

	public void set_refresh_id(int val) {
		_bit |= 0x8;
		_refresh_id = val;
	}

	public boolean has_refresh_id() {
		return (_bit & 0x8) == 0x8;
	}

	public java.util.LinkedList<Integer> get_random_item() {
		return _random_item;
	}

	public void add_random_item(int val) {
		if (!has_random_item()) {
			_random_item = new java.util.LinkedList<Integer>();
			_bit |= 0x10;
		}
		_random_item.add(val);
	}

	public boolean has_random_item() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_season_num() {
		return _season_num;
	}

	public void set_season_num(int val) {
		_bit |= 0x20;
		_season_num = val;
	}

	public boolean has_season_num() {
		return (_bit & 0x20) == 0x20;
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
		if (has_error()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _error.toInt());
		}
		if (has_attendance_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _attendance_id);
		}
		if (has_group_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _group_id);
		}
		if (has_refresh_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _refresh_id);
		}
		if (has_random_item()) {
			for (int val : _random_item) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, val);
			}
		}
		if (has_season_num()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(6, _season_num);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_attendance_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_group_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		// if (has_random_item()){
		// for(int val : _random_item){
		// if (!val.isInitialized()){
		// _memorizedIsInitialized = -1;
		// return false;
		// }
		// }
		// }
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_error()) {
			output.writeEnum(1, _error.toInt());
		}
		if (has_attendance_id()) {
			output.wirteInt32(2, _attendance_id);
		}
		if (has_group_id()) {
			output.wirteInt32(3, _group_id);
		}
		if (has_refresh_id()) {
			output.wirteInt32(4, _refresh_id);
		}
		if (has_random_item()) {
			for (int val : _random_item) {
				output.wirteInt32(5, val);
			}
		}
		if (has_season_num()) {
			output.writeUInt32(6, _season_num);
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
					set_error(SC_ATTENDANCE_REFRESH_REWARD_ACK.eErrorType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_attendance_id(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_group_id(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_refresh_id(input.readInt32());
					break;
				}
				case 0x00000028: {
					add_random_item(input.readInt32());
					break;
				}
				case 0x00000030: {
					set_season_num(input.readUInt32());
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
		return new SC_ATTENDANCE_REFRESH_REWARD_ACK();
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

	public enum eErrorType {
		error_block(1),
		error_fail(2),
		error_money(3),
		error_maxcount(4),
		error_invalid(5),
		error_wait(6),
		error_xml(7),
		;

		private int value;

		eErrorType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eErrorType v) {
			return value == v.value;
		}

		public static eErrorType fromInt(int i) {
			switch (i) {
				case 1:
					return error_block;
				case 2:
					return error_fail;
				case 3:
					return error_money;
				case 4:
					return error_maxcount;
				case 5:
					return error_invalid;
				case 6:
					return error_wait;
				case 7:
					return error_xml;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eErrorType，%d", i));
			}
		}
	}
}
