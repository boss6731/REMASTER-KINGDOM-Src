package l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_DEATH_PENALTY_STATUS implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_DEATH_PENALTY_STATUS newInstance() {
		return new SC_DEATH_PENALTY_STATUS();
	}

	private int _globalDeathPenaltyStatus;
	private int _expDeathPenaltyStatus;
	private int _itemDeathPenaltyStatus;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_DEATH_PENALTY_STATUS() {
	}

	public int get_globalDeathPenaltyStatus() {
		return _globalDeathPenaltyStatus;
	}

	public void set_globalDeathPenaltyStatus(int val) {
		_bit |= 0x1;
		_globalDeathPenaltyStatus = val;
	}

	public boolean has_globalDeathPenaltyStatus() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_expDeathPenaltyStatus() {
		return _expDeathPenaltyStatus;
	}

	public void set_expDeathPenaltyStatus(int val) {
		_bit |= 0x2;
		_expDeathPenaltyStatus = val;
	}

	public boolean has_expDeathPenaltyStatus() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_itemDeathPenaltyStatus() {
		return _itemDeathPenaltyStatus;
	}

	public void set_itemDeathPenaltyStatus(int val) {
		_bit |= 0x4;
		_itemDeathPenaltyStatus = val;
	}

	public boolean has_itemDeathPenaltyStatus() {
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
		if (has_globalDeathPenaltyStatus()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _globalDeathPenaltyStatus);
		}
		if (has_expDeathPenaltyStatus()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _expDeathPenaltyStatus);
		}
		if (has_itemDeathPenaltyStatus()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _itemDeathPenaltyStatus);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_globalDeathPenaltyStatus()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_expDeathPenaltyStatus()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_itemDeathPenaltyStatus()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_globalDeathPenaltyStatus()) {
			output.writeUInt32(1, _globalDeathPenaltyStatus);
		}
		if (has_expDeathPenaltyStatus()) {
			output.writeUInt32(2, _expDeathPenaltyStatus);
		}
		if (has_itemDeathPenaltyStatus()) {
			output.writeUInt32(3, _itemDeathPenaltyStatus);
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
					set_globalDeathPenaltyStatus(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_expDeathPenaltyStatus(input.readUInt32());
					break;
				}
				case 0x00000018: {
					set_itemDeathPenaltyStatus(input.readUInt32());
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
		return new SC_DEATH_PENALTY_STATUS();
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
