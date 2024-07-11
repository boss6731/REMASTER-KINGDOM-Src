package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class EinhasadFaithInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static EinhasadFaithInfo newInstance() {
		return new EinhasadFaithInfo();
	}

	private EinhasadFaithInfo.FaithInfoType _type;
	private int _groupId;
	private int _indexId;
	private boolean _isEnable;
	private int _expiredTime;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private EinhasadFaithInfo() {
	}

	public EinhasadFaithInfo.FaithInfoType get_type() {
		return _type;
	}

	public void set_type(EinhasadFaithInfo.FaithInfoType val) {
		_bit |= 0x1;
		_type = val;
	}

	public boolean has_type() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_groupId() {
		return _groupId;
	}

	public void set_groupId(int val) {
		_bit |= 0x2;
		_groupId = val;
	}

	public boolean has_groupId() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_indexId() {
		return _indexId;
	}

	public void set_indexId(int val) {
		_bit |= 0x4;
		_indexId = val;
	}

	public boolean has_indexId() {
		return (_bit & 0x4) == 0x4;
	}

	public boolean get_isEnable() {
		return _isEnable;
	}

	public void set_isEnable(boolean val) {
		_bit |= 0x8;
		_isEnable = val;
	}

	public boolean has_isEnable() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_expiredTime() {
		return _expiredTime;
	}

	public void set_expiredTime(int val) {
		_bit |= 0x10;
		_expiredTime = val;
	}

	public boolean has_expiredTime() {
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
		if (has_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _type.toInt());
		}
		if (has_groupId()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _groupId);
		}
		if (has_indexId()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _indexId);
		}
		if (has_isEnable()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _isEnable);
		}
		if (has_expiredTime()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _expiredTime);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_groupId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_indexId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isEnable()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_expiredTime()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_type()) {
			output.writeEnum(1, _type.toInt());
		}
		if (has_groupId()) {
			output.wirteInt32(2, _groupId);
		}
		if (has_indexId()) {
			output.wirteInt32(3, _indexId);
		}
		if (has_isEnable()) {
			output.writeBool(4, _isEnable);
		}
		if (has_expiredTime()) {
			output.writeUInt32(5, _expiredTime);
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
					set_type(EinhasadFaithInfo.FaithInfoType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_groupId(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_indexId(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_isEnable(input.readBool());
					break;
				}
				case 0x00000028: {
					set_expiredTime(input.readUInt32());
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
		return new EinhasadFaithInfo();
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

	public enum FaithInfoType {
		Group(1),
		Index(2),
		;

		private int value;

		FaithInfoType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(FaithInfoType v) {
			return value == v.value;
		}

		public static FaithInfoType fromInt(int i) {
			switch (i) {
				case 1:
					return Group;
				case 2:
					return Index;
				default:
					throw new IllegalArgumentException(String.format("無效參數 FaithInfoType，%d", i));
			}
		}
	}
}
