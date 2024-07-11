package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_HUNTING_QUEST_MAP_SELECT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_HUNTING_QUEST_MAP_SELECT_REQ newInstance() {
		return new CS_HUNTING_QUEST_MAP_SELECT_REQ();
	}

	private int _map_number;
	private int _location_desc;
	private boolean _is_select;
	private long _quest_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_HUNTING_QUEST_MAP_SELECT_REQ() {
	}

	public int get_map_number() {
		return _map_number;
	}

	public void set_map_number(int val) {
		_bit |= 0x1;
		_map_number = val;
	}

	public boolean has_map_number() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_location_desc() {
		return _location_desc;
	}

	public void set_location_desc(int val) {
		_bit |= 0x2;
		_location_desc = val;
	}

	public boolean has_location_desc() {
		return (_bit & 0x2) == 0x2;
	}

	public boolean get_is_select() {
		return _is_select;
	}

	public void set_is_select(boolean val) {
		_bit |= 0x4;
		_is_select = val;
	}

	public boolean has_is_select() {
		return (_bit & 0x4) == 0x4;
	}

	public long get_quest_id() {
		return _quest_id;
	}

	public void set_quest_id(long val) {
		_bit |= 0x8;
		_quest_id = val;
	}

	public boolean has_quest_id() {
		return (_bit & 0x8) == 0x8;
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
		if (has_map_number()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _map_number);
		}
		if (has_location_desc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _location_desc);
		}
		if (has_is_select()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _is_select);
		}
		if (has_quest_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(4, _quest_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_map_number()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_is_select()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_map_number()) {
			output.wirteInt32(1, _map_number);
		}
		if (has_location_desc()) {
			output.wirteInt32(2, _location_desc);
		}
		if (has_is_select()) {
			output.writeBool(3, _is_select);
		}
		if (has_quest_id()) {
			output.writeInt64(4, _quest_id);
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
					set_map_number(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_location_desc(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_is_select(input.readBool());
					break;
				}
				case 0x00000020: {
					set_quest_id(input.readInt64());
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
		return new CS_HUNTING_QUEST_MAP_SELECT_REQ();
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
