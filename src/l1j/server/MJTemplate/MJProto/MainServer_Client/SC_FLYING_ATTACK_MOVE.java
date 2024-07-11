package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_FLYING_ATTACK_MOVE implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_FLYING_ATTACK_MOVE newInstance() {
		return new SC_FLYING_ATTACK_MOVE();
	}

	private int _object_id;
	private int _range;
	private int _start_loc;
	private int _end_loc;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_FLYING_ATTACK_MOVE() {
	}

	public int get_object_id() {
		return _object_id;
	}

	public void set_object_id(int val) {
		_bit |= 0x1;
		_object_id = val;
	}

	public boolean has_object_id() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_range() {
		return _range;
	}

	public void set_range(int val) {
		_bit |= 0x2;
		_range = val;
	}

	public boolean has_range() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_start_loc() {
		return _start_loc;
	}

	public void set_start_loc(int val) {
		_bit |= 0x4;
		_start_loc = val;
	}

	public boolean has_start_loc() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_end_loc() {
		return _end_loc;
	}

	public void set_end_loc(int val) {
		_bit |= 0x8;
		_end_loc = val;
	}

	public boolean has_end_loc() {
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
		if (has_object_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _object_id);
		}
		if (has_range()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _range);
		}
		if (has_start_loc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _start_loc);
		}
		if (has_end_loc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _end_loc);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_object_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_range()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_start_loc()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_end_loc()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_object_id()) {
			output.wirteInt32(1, _object_id);
		}
		if (has_range()) {
			output.wirteInt32(2, _range);
		}
		if (has_start_loc()) {
			output.wirteInt32(3, _start_loc);
		}
		if (has_end_loc()) {
			output.wirteInt32(4, _end_loc);
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
					set_object_id(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_range(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_start_loc(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_end_loc(input.readInt32());
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
		return new SC_FLYING_ATTACK_MOVE();
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
