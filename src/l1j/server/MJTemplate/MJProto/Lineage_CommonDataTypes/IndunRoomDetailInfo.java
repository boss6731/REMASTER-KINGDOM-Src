package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。由 MJSoft 製作。
public class IndunRoomDetailInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static IndunRoomDetailInfo newInstance() {
		return new IndunRoomDetailInfo();
	}

	private int _min_level;
	private eDungeonType _dungeon_type;
	private int _max_player;
	private eDistributionType _distribution_type;
	private IndunEnterCondition _condition;
	private eArenaMapKind _map_kind;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private IndunRoomDetailInfo() {
	}

	public int get_min_level() {
		return _min_level;
	}

	public void set_min_level(int val) {
		_bit |= 0x1;
		_min_level = val;
	}

	public boolean has_min_level() {
		return (_bit & 0x1) == 0x1;
	}

	public eDungeonType get_dungeon_type() {
		return _dungeon_type;
	}

	public void set_dungeon_type(eDungeonType val) {
		_bit |= 0x2;
		_dungeon_type = val;
	}

	public boolean has_dungeon_type() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_max_player() {
		return _max_player;
	}

	public void set_max_player(int val) {
		_bit |= 0x4;
		_max_player = val;
	}

	public boolean has_max_player() {
		return (_bit & 0x4) == 0x4;
	}

	public eDistributionType get_distribution_type() {
		return _distribution_type;
	}

	public void set_distribution_type(eDistributionType val) {
		_bit |= 0x8;
		_distribution_type = val;
	}

	public boolean has_distribution_type() {
		return (_bit & 0x8) == 0x8;
	}

	public IndunEnterCondition get_condition() {
		return _condition;
	}

	public void set_condition(IndunEnterCondition val) {
		_bit |= 0x10;
		_condition = val;
	}

	public boolean has_condition() {
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
		if (has_min_level()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _min_level);
		}
		if (has_dungeon_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _dungeon_type.toInt());
		}
		if (has_max_player()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _max_player);
		}
		if (has_distribution_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _distribution_type.toInt());
		}
		if (has_condition()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, _condition);
		}
		if (has_map_kind()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(6, _map_kind.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_min_level()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_dungeon_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_max_player()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_distribution_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_condition()) {
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
		if (has_min_level()) {
			output.writeUInt32(1, _min_level);
		}
		if (has_dungeon_type()) {
			output.writeEnum(2, _dungeon_type.toInt());
		}
		if (has_max_player()) {
			output.writeUInt32(3, _max_player);
		}
		if (has_distribution_type()) {
			output.writeEnum(4, _distribution_type.toInt());
		}
		if (has_condition()) {
			output.writeMessage(5, _condition);
		}
		if (has_map_kind()) {
			output.writeEnum(6, _map_kind.toInt());
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
					set_min_level(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_dungeon_type(eDungeonType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000018: {
					set_max_player(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_distribution_type(eDistributionType.fromInt(input.readEnum()));
					break;
				}
				case 0x0000002A: {
					set_condition((IndunEnterCondition) input.readMessage(IndunEnterCondition.newInstance()));
					break;
				}
				case 0x00000030: {
					set_map_kind(eArenaMapKind.fromInt(input.readEnum()));
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
		return new IndunRoomDetailInfo();
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
