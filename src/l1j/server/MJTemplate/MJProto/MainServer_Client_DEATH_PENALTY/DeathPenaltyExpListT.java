package l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class DeathPenaltyExpListT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static DeathPenaltyExpListT newInstance() {
		return new DeathPenaltyExpListT();
	}

	private int _index;
	private float _exp_ratio;
	private long _recovery_cost;
	private long _delete_time;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private DeathPenaltyExpListT() {
	}

	public int get_index() {
		return _index;
	}

	public void set_index(int val) {
		_bit |= 0x1;
		_index = val;
	}

	public boolean has_index() {
		return (_bit & 0x1) == 0x1;
	}

	public float get_exp_ratio() {
		return _exp_ratio;
	}

	public void set_exp_ratio(float val) {
		_bit |= 0x8;
		_exp_ratio = val;
	}

	public boolean has_exp_ratio() {
		return (_bit & 0x8) == 0x8;
	}

	public long get_recovery_cost() {
		return _recovery_cost;
	}

	public void set_recovery_cost(long val) {
		_bit |= 0x2;
		_recovery_cost = val;
	}

	public boolean has_recovery_cost() {
		return (_bit & 0x2) == 0x2;
	}

	public long get_delete_time() {
		return _delete_time;
	}

	public void set_delete_time(long val) {
		_bit |= 0x4;
		_delete_time = val;
	}

	public boolean has_delete_time() {
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
		if (has_index()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _index);
		}
		if (has_exp_ratio()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeFloatSize(4, _exp_ratio);
		}
		if (has_recovery_cost()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(2, _recovery_cost);
		}
		if (has_delete_time()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(3, _delete_time);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_index()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_exp_ratio()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_recovery_cost()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_delete_time()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_index()) {
			output.writeUInt32(1, _index);
		}
		if (has_exp_ratio()) {
			output.writeFloat(4, _exp_ratio);
		}
		if (has_recovery_cost()) {
			output.writeInt64(2, _recovery_cost);
		}
		if (has_delete_time()) {
			output.writeInt64(3, _delete_time);
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
					set_index(input.readUInt32());
					break;
				}
				case 0x00000025: {
					set_exp_ratio(input.readFloat());
					break;
				}
				case 0x00000010: {
					set_recovery_cost(input.readInt64());
					break;
				}
				case 0x00000018: {
					set_delete_time(input.readInt64());
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
		return new DeathPenaltyExpListT();
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
