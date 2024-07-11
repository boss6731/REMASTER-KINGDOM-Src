package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_HUNTING_GUIDE_BOOK_MAP_INFO_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_HUNTING_GUIDE_BOOK_MAP_INFO_ACK newInstance() {
		return new SC_HUNTING_GUIDE_BOOK_MAP_INFO_ACK();
	}

	private int _map_number;
	private boolean _is_can_random_teleport;
	private int _map_limit_time;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_HUNTING_GUIDE_BOOK_MAP_INFO_ACK() {
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

	public boolean get_is_can_random_teleport() {
		return _is_can_random_teleport;
	}

	public void set_is_can_random_teleport(boolean val) {
		_bit |= 0x2;
		_is_can_random_teleport = val;
	}

	public boolean has_is_can_random_teleport() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_map_limit_time() {
		return _map_limit_time;
	}

	public void set_map_limit_time(int val) {
		_bit |= 0x4;
		_map_limit_time = val;
	}

	public boolean has_map_limit_time() {
		return (_bit & 0x4) == 0x4;
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
		if (has_is_can_random_teleport()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_can_random_teleport);
		}
		if (has_map_limit_time()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _map_limit_time);
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
		if (!has_is_can_random_teleport()) {
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
		if (has_is_can_random_teleport()) {
			output.writeBool(2, _is_can_random_teleport);
		}
		if (has_map_limit_time()) {
			output.wirteInt32(3, _map_limit_time);
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
					set_is_can_random_teleport(input.readBool());
					break;
				}
				case 0x00000018: {
					set_map_limit_time(input.readInt32());
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
		return new SC_HUNTING_GUIDE_BOOK_MAP_INFO_ACK();
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
