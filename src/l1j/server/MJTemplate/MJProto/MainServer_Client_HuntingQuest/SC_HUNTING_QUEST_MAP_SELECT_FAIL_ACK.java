package l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK newInstance() {
		return new SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK();
	}

	private SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK.eResult _fail_reason;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK() {
	}

	public SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK.eResult get_fail_reason() {
		return _fail_reason;
	}

	public void set_fail_reason(SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK.eResult val) {
		_bit |= 0x1;
		_fail_reason = val;
	}

	public boolean has_fail_reason() {
		return (_bit & 0x1) == 0x1;
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
		if (has_fail_reason()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _fail_reason.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_fail_reason()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_fail_reason()) {
			output.writeEnum(1, _fail_reason.toInt());
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
					set_fail_reason(SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK.eResult.fromInt(input.readEnum()));
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
		return new SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK();
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

	public enum eResult {
		SUCCESS(0),
		FAIL_INVALID_LEVEL(1),
		FAIL_ALREADY_MAX_QUEST(2),
		FAIL_INVALID_MAP(3),
		FAIL_ALREADY_ADD_QUEST(4),
		FAIL_ALREADY_FINISHED(5),
		FAIL_UNKNOWN_REASON(9999),
		;

		private int value;

		eResult(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eResult v) {
			return value == v.value;
		}

		public static eResult fromInt(int i) {
			switch (i) {
				case 0:
					return SUCCESS;
				case 1:
					return FAIL_INVALID_LEVEL;
				case 2:
					return FAIL_ALREADY_MAX_QUEST;
				case 3:
					return FAIL_INVALID_MAP;
				case 4:
					return FAIL_ALREADY_ADD_QUEST;
				case 5:
					return FAIL_ALREADY_FINISHED;
				case 9999:
					return FAIL_UNKNOWN_REASON;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eResult，%d", i));
			}
		}
	}
}
