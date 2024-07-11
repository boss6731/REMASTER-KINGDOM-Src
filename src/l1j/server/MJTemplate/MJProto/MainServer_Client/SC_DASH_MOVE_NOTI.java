package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_DASH_MOVE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_DASH_MOVE_NOTI newInstance() {
		return new SC_DASH_MOVE_NOTI();
	}

	private int _obj_id;
	private SC_DASH_MOVE_NOTI.eShowType _show_type;
	private int _start_pos;
	private int _end_pos;
	private int _effect_sprite;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_DASH_MOVE_NOTI() {
	}

	public int get_obj_id() {
		return _obj_id;
	}

	public void set_obj_id(int val) {
		_bit |= 0x1;
		_obj_id = val;
	}

	public boolean has_obj_id() {
		return (_bit & 0x1) == 0x1;
	}

	public SC_DASH_MOVE_NOTI.eShowType get_show_type() {
		return _show_type;
	}

	public void set_show_type(SC_DASH_MOVE_NOTI.eShowType val) {
		_bit |= 0x2;
		_show_type = val;
	}

	public boolean has_show_type() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_start_pos() {
		return _start_pos;
	}

	public void set_start_pos(int val) {
		_bit |= 0x4;
		_start_pos = val;
	}

	public boolean has_start_pos() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_end_pos() {
		return _end_pos;
	}

	public void set_end_pos(int val) {
		_bit |= 0x8;
		_end_pos = val;
	}

	public boolean has_end_pos() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_effect_sprite() {
		return _effect_sprite;
	}

	public void set_effect_sprite(int val) {
		_bit |= 0x10;
		_effect_sprite = val;
	}

	public boolean has_effect_sprite() {
		return (_bit & 0x10) == 0x10;
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
		if (has_obj_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _obj_id);
		}
		if (has_show_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _show_type.toInt());
		}
		if (has_start_pos()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _start_pos);
		}
		if (has_end_pos()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _end_pos);
		}
		if (has_effect_sprite()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _effect_sprite);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_obj_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_show_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_start_pos()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_end_pos()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_obj_id()) {
			output.writeUInt32(1, _obj_id);
		}
		if (has_show_type()) {
			output.writeEnum(2, _show_type.toInt());
		}
		if (has_start_pos()) {
			output.writeUInt32(3, _start_pos);
		}
		if (has_end_pos()) {
			output.writeUInt32(4, _end_pos);
		}
		if (has_effect_sprite()) {
			output.writeUInt32(5, _effect_sprite);
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
					set_obj_id(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_show_type(SC_DASH_MOVE_NOTI.eShowType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000018: {
					set_start_pos(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_end_pos(input.readUInt32());
					break;
				}
				case 0x00000028: {
					set_effect_sprite(input.readUInt32());
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
		return new SC_DASH_MOVE_NOTI();
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

	public enum eShowType {
		None(0),
		;

		private int value;

		eShowType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eShowType v) {
			return value == v.value;
		}

		public static eShowType fromInt(int i) {
			switch (i) {
				case 0:
					return None;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eShowType，%d", i));
			}
		}
	}
}
