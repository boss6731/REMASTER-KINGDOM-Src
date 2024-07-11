package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaMapKind;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_ARENACO_BYPASS_INDUN_MATCHING_ACCEPT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_ARENACO_BYPASS_INDUN_MATCHING_ACCEPT_REQ newInstance() {
		return new CS_ARENACO_BYPASS_INDUN_MATCHING_ACCEPT_REQ();
	}

	private eArenaMapKind _map_kind;
	private CS_ARENACO_BYPASS_INDUN_MATCHING_ACCEPT_REQ.eResult _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_ARENACO_BYPASS_INDUN_MATCHING_ACCEPT_REQ() {
	}

	public eArenaMapKind get_map_kind() {
		return _map_kind;
	}

	public void set_map_kind(eArenaMapKind val) {
		_bit |= 0x1;
		_map_kind = val;
	}

	public boolean has_map_kind() {
		return (_bit & 0x1) == 0x1;
	}

	public CS_ARENACO_BYPASS_INDUN_MATCHING_ACCEPT_REQ.eResult get_result() {
		return _result;
	}

	public void set_result(CS_ARENACO_BYPASS_INDUN_MATCHING_ACCEPT_REQ.eResult val) {
		_bit |= 0x2;
		_result = val;
	}

	public boolean has_result() {
		return (_bit & 0x2) == 0x2;
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
		if (has_map_kind()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _map_kind.toInt());
		}
		if (has_result()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _result.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_map_kind()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_result()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_map_kind()) {
			output.writeEnum(1, _map_kind.toInt());
		}
		if (has_result()) {
			output.writeEnum(2, _result.toInt());
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
					set_map_kind(eArenaMapKind.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_result(CS_ARENACO_BYPASS_INDUN_MATCHING_ACCEPT_REQ.eResult.fromInt(input.readEnum()));
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
		return new CS_ARENACO_BYPASS_INDUN_MATCHING_ACCEPT_REQ();
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
		ACCEPT(1),
		CANCEL(2),
		CANCEL_TIMEOUT(3),
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
				case 1:
					return ACCEPT;
				case 2:
					return CANCEL;
				case 3:
					return CANCEL_TIMEOUT;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eResult，%d", i));
			}
		}
	}
}
