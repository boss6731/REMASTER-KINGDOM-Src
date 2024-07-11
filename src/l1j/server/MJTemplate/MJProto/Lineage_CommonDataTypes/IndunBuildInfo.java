package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。由 MJSoft 製作。
public class IndunBuildInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static IndunBuildInfo newInstance() {
		return new IndunBuildInfo();
	}

	private byte[] _title;
	private int _min_level;
	private int _fee;
	private int _key_item_id;
	private eDistributionType _distribution_type;
	private boolean _closed;
	private int _max_player;
	private String _password;
	private eArenaMapKind _map_kind;
	private int _server_no;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private IndunBuildInfo() {
	}

	public byte[] get_title() {
		return _title;
	}

	public void set_title(byte[] val) {
		_bit |= 0x1;
		_title = val;
	}

	public boolean has_title() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_min_level() {
		return _min_level;
	}

	public void set_min_level(int val) {
		_bit |= 0x2;
		_min_level = val;
	}

	public boolean has_min_level() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_fee() {
		return _fee;
	}

	public void set_fee(int val) {
		_bit |= 0x4;
		_fee = val;
	}

	public boolean has_fee() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_key_item_id() {
		return _key_item_id;
	}

	public void set_key_item_id(int val) {
		_bit |= 0x8;
		_key_item_id = val;
	}

	public boolean has_key_item_id() {
		return (_bit & 0x8) == 0x8;
	}

	public eDistributionType get_distribution_type() {
		return _distribution_type;
	}

	public void set_distribution_type(eDistributionType val) {
		_bit |= 0x10;
		_distribution_type = val;
	}

	public boolean has_distribution_type() {
		return (_bit & 0x10) == 0x10;
	}

	public boolean get_closed() {
		return _closed;
	}

	public void set_closed(boolean val) {
		_bit |= 0x20;
		_closed = val;
	}

	public boolean has_closed() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_max_player() {
		return _max_player;
	}

	public void set_max_player(int val) {
		_bit |= 0x40;
		_max_player = val;
	}

	public boolean has_max_player() {
		return (_bit & 0x40) == 0x40;
	}

	public String get_password() {
		return _password;
	}

	public void set_password(String val) {
		_bit |= 0x80;
		_password = val;
	}

	public boolean has_password() {
		return (_bit & 0x80) == 0x80;
	}

	public eArenaMapKind get_map_kind() {
		return _map_kind;
	}

	public void set_map_kind(eArenaMapKind val) {
		_bit |= 0x100;
		_map_kind = val;
	}

	public boolean has_map_kind() {
		return (_bit & 0x100) == 0x100;
	}

	public int get_server_no() {
		return _server_no;
	}

	public void set_server_no(int val) {
		_bit |= 0x200;
		_server_no = val;
	}

	public boolean has_server_no() {
		return (_bit & 0x200) == 0x200;
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
		if (has_title()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(1, _title);
		}
		if (has_min_level()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _min_level);
		}
		if (has_fee()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _fee);
		}
		if (has_key_item_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _key_item_id);
		}
		if (has_distribution_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(5, _distribution_type.toInt());
		}
		if (has_closed()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(6, _closed);
		}
		if (has_max_player()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(7, _max_player);
		}
		if (has_password()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(8, _password);
		}
		if (has_map_kind()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(9, _map_kind.toInt());
		}
		if (has_server_no()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _server_no);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_title()) {
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
		if (!has_key_item_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_distribution_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_closed()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_max_player()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_map_kind()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_title()) {
			output.writeBytes(1, _title);
		}
		if (has_min_level()) {
			output.writeUInt32(2, _min_level);
		}
		if (has_fee()) {
			output.writeUInt32(3, _fee);
		}
		if (has_key_item_id()) {
			output.writeUInt32(4, _key_item_id);
		}
		if (has_distribution_type()) {
			output.writeEnum(5, _distribution_type.toInt());
		}
		if (has_closed()) {
			output.writeBool(6, _closed);
		}
		if (has_max_player()) {
			output.writeUInt32(7, _max_player);
		}
		if (has_password()) {
			output.writeString(8, _password);
		}
		if (has_map_kind()) {
			output.writeEnum(9, _map_kind.toInt());
		}
		if (has_server_no()) {
			output.wirteInt32(10, _server_no);
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
					set_title(input.readBytes());
					break;
				}
				case 0x00000010: {
					set_min_level(input.readUInt32());
					break;
				}
				case 0x00000018: {
					set_fee(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_key_item_id(input.readUInt32());
					break;
				}
				case 0x00000028: {
					set_distribution_type(eDistributionType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000030: {
					set_closed(input.readBool());
					break;
				}
				case 0x00000038: {
					set_max_player(input.readUInt32());
					break;
				}
				case 0x00000042: {
					set_password(input.readString());
					break;
				}
				case 0x00000048: {
					set_map_kind(eArenaMapKind.fromInt(input.readEnum()));
					break;
				}
				case 0x00000050: {
					set_server_no(input.readInt32());
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
		return new IndunBuildInfo();
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
