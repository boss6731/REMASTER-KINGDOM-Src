package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_CLIENT_FUNC_LOG_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_CLIENT_FUNC_LOG_NOTI newInstance() {
		return new CS_CLIENT_FUNC_LOG_NOTI();
	}

	private CS_CLIENT_FUNC_LOG_NOTI.FuncID _fid;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_CLIENT_FUNC_LOG_NOTI() {
	}

	public CS_CLIENT_FUNC_LOG_NOTI.FuncID get_fid() {
		return _fid;
	}

	public void set_fid(CS_CLIENT_FUNC_LOG_NOTI.FuncID val) {
		_bit |= 0x1;
		_fid = val;
	}

	public boolean has_fid() {
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
		if (has_fid()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _fid.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_fid()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_fid()) {
			output.writeEnum(1, _fid.toInt());
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
					set_fid(CS_CLIENT_FUNC_LOG_NOTI.FuncID.fromInt(input.readEnum()));
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

			/*
			 * l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			 * if (pc == null){
			 * return this;
			 * }
			 */

			set_fid(FuncID.OK_CHARACTER_PW);

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_CLIENT_FUNC_LOG_NOTI();
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

	public enum FuncID {
		LOGIN_PROCESS(1),
		OK_CHARACTER_PW(2),
		TO_WINDOW_MODE(3),
		OK_INGAME_PW(4),
		GET_MAP_NUM(5),
		;

		private int value;

		FuncID(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(FuncID v) {
			return value == v.value;
		}

		public static FuncID fromInt(int i) {
			switch (i) {
				case 1:
					return LOGIN_PROCESS;
				case 2:
					return OK_CHARACTER_PW;
				case 3:
					return TO_WINDOW_MODE;
				case 4:
					return OK_INGAME_PW;
				case 5:
					return GET_MAP_NUM;
				default:
					throw new IllegalArgumentException(String.format("無效的參數 FuncID, %d", i));
			}
		}
	}
}
