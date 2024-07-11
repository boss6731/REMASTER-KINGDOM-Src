package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。由 MJSoft 製作。
public class IndunRoomInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static IndunRoomInfo newInstance() {
		return new IndunRoomInfo();
	}

	private int _room_id;
	private byte[] _title;
	private int _member_count_cur;
	private int _member_count_max;
	private int _member_count_min;
	private eArenaMapKind _map_kind;
	private boolean _closed;
	private int _min_level;
	private int _fee;
	private eDistributionType _distribution_type;
	private boolean _is_playing;
	private boolean _is_locked;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private IndunRoomInfo() {
	}

	public int get_room_id() {
		return _room_id;
	}

	public void set_room_id(int val) {
		_bit |= 0x1;
		_room_id = val;
	}

	public boolean has_room_id() {
		return (_bit & 0x1) == 0x1;
	}

	public byte[] get_title() {
		return _title;
	}

	public void set_title(byte[] val) {
		_bit |= 0x2;
		_title = val;
	}

	public boolean has_title() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_member_count_cur() {
		return _member_count_cur;
	}

	public void set_member_count_cur(int val) {
		_bit |= 0x4;
		_member_count_cur = val;
	}

	public boolean has_member_count_cur() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_member_count_max() {
		return _member_count_max;
	}

	public void set_member_count_max(int val) {
		_bit |= 0x8;
		_member_count_max = val;
	}

	public boolean has_member_count_max() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_member_count_min() {
		return _member_count_min;
	}

	public void set_member_count_min(int val) {
		_bit |= 0x10;
		_member_count_min = val;
	}

	public boolean has_member_count_min() {
		return (_bit & 0x10) == 0x10;
	}

	public eArenaMapKind get_map_kind() {
		return _map_kind;
	}

	public void set_map_kind(eArenaMapKind val) {
		_bit |= 0x20;
		_map_kind = val;
	}

	public boolean has_map_kind() {
		return (_bit & 0x20) == 0x20;
	}

	public boolean get_closed() {
		return _closed;
	}

	public void set_closed(boolean val) {
		_bit |= 0x40;
		_closed = val;
	}

	public boolean has_closed() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_min_level() {
		return _min_level;
	}

	public void set_min_level(int val) {
		_bit |= 0x80;
		_min_level = val;
	}

	public boolean has_min_level() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_fee() {
		return _fee;
	}

	public void set_fee(int val) {
		_bit |= 0x100;
		_fee = val;
	}

	public boolean has_fee() {
		return (_bit & 0x100) == 0x100;
	}

	public eDistributionType get_distribution_type() {
		return _distribution_type;
	}

	public void set_distribution_type(eDistributionType val) {
		_bit |= 0x200;
		_distribution_type = val;
	}

	public boolean has_distribution_type() {
		return (_bit & 0x200) == 0x200;
	}

	public boolean get_is_playing() {
		return _is_playing;
	}

	public void set_is_playing(boolean val) {
		_bit |= 0x400;
		_is_playing = val;
	}

	public boolean has_is_playing() {
		return (_bit & 0x400) == 0x400;
	}

	public boolean get_is_locked() {
		return _is_locked;
	}

	public void set_is_locked(boolean val) {
		_bit |= 0x800;
		_is_locked = val;
	}

	public boolean has_is_locked() {
		return (_bit & 0x800) == 0x800;
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
		if (has_room_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _room_id);
		}
		if (has_title()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _title);
		}
		if (has_member_count_cur()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _member_count_cur);
		}
		if (has_member_count_max()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _member_count_max);
		}
		if (has_member_count_min()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _member_count_min);
		}
		if (has_map_kind()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(6, _map_kind.toInt());
		}
		if (has_closed()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(7, _closed);
		}
		if (has_min_level()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(8, _min_level);
		}
		if (has_fee()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(9, _fee);
		}
		if (has_distribution_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(10, _distribution_type.toInt());
		}
		if (has_is_playing()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(11, _is_playing);
		}
		if (has_is_locked()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(12, _is_locked);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_room_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_title()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_member_count_cur()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_member_count_max()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_member_count_min()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_map_kind()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_closed()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_min_level()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_fee()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_distribution_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_room_id()) {
			output.writeUInt32(1, _room_id);
		}
		if (has_title()) {
			output.writeBytes(2, _title);
		}
		if (has_member_count_cur()) {
			output.writeUInt32(3, _member_count_cur);
		}
		if (has_member_count_max()) {
			output.writeUInt32(4, _member_count_max);
		}
		if (has_member_count_min()) {
			output.writeUInt32(5, _member_count_min);
		}
		if (has_map_kind()) {
			output.writeEnum(6, _map_kind.toInt());
		}
		if (has_closed()) {
			output.writeBool(7, _closed);
		}
		if (has_min_level()) {
			output.writeUInt32(8, _min_level);
		}
		if (has_fee()) {
			output.writeUInt32(9, _fee);
		}
		if (has_distribution_type()) {
			output.writeEnum(10, _distribution_type.toInt());
		}
		if (has_is_playing()) {
			output.writeBool(11, _is_playing);
		}
		if (has_is_locked()) {
			output.writeBool(12, _is_locked);
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
					set_room_id(input.readUInt32());
					break;
				}
				case 0x00000012: {
					set_title(input.readBytes());
					break;
				}
				case 0x00000018: {
					set_member_count_cur(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_member_count_max(input.readUInt32());
					break;
				}
				case 0x00000028: {
					set_member_count_min(input.readUInt32());
					break;
				}
				case 0x00000030: {
					set_map_kind(eArenaMapKind.fromInt(input.readEnum()));
					break;
				}
				case 0x00000038: {
					set_closed(input.readBool());
					break;
				}
				case 0x00000040: {
					set_min_level(input.readUInt32());
					break;
				}
				case 0x00000048: {
					set_fee(input.readUInt32());
					break;
				}
				case 0x00000050: {
					set_distribution_type(eDistributionType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000058: {
					set_is_playing(input.readBool());
					break;
				}
				case 0x00000060: {
					set_is_locked(input.readBool());
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
		return new IndunRoomInfo();
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
