package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.server.model.L1ExcludingService;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_EXCLUDE_REQ_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_EXCLUDE_REQ_PACKET newInstance() {
		return new CS_EXCLUDE_REQ_PACKET();
	}

	private CS_EXCLUDE_REQ_PACKET.eExcludeMode _mode;
	private CS_EXCLUDE_REQ_PACKET.eExcludeType _type;
	private java.util.LinkedList<byte[]> _charNames;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_EXCLUDE_REQ_PACKET() {
	}

	public CS_EXCLUDE_REQ_PACKET.eExcludeMode get_mode() {
		return _mode;
	}

	public void set_mode(CS_EXCLUDE_REQ_PACKET.eExcludeMode val) {
		_bit |= 0x1;
		_mode = val;
	}

	public boolean has_mode() {
		return (_bit & 0x1) == 0x1;
	}

	public CS_EXCLUDE_REQ_PACKET.eExcludeType get_type() {
		return _type;
	}

	public void set_type(CS_EXCLUDE_REQ_PACKET.eExcludeType val) {
		_bit |= 0x2;
		_type = val;
	}

	public boolean has_type() {
		return (_bit & 0x2) == 0x2;
	}

	public java.util.LinkedList<byte[]> get_charNames() {
		return _charNames;
	}

	public void add_charNames(byte[] val) {
		if (!has_charNames()) {
			_charNames = new java.util.LinkedList<byte[]>();
			_bit |= 0x4;
		}
		_charNames.add(val);
	}

	public boolean has_charNames() {
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
		if (has_mode()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _mode.toInt());
		}
		if (has_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _type.toInt());
		}
		if (has_charNames()) {
			for (byte[] val : _charNames) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_mode()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_mode()) {
			output.writeEnum(1, _mode.toInt());
		}
		if (has_type()) {
			output.writeEnum(2, _type.toInt());
		}
		if (has_charNames()) {
			for (byte[] val : _charNames) {
				output.writeBytes(3, val);
			}
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
					set_mode(CS_EXCLUDE_REQ_PACKET.eExcludeMode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_type(CS_EXCLUDE_REQ_PACKET.eExcludeType.fromInt(input.readEnum()));
					break;
				}
				case 0x0000001A: {
					add_charNames(input.readBytes());
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			L1ExcludingService.service().onRequest(pc, _charNames, _mode, _type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_EXCLUDE_REQ_PACKET();
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

	public enum eExcludeMode {
		eEXCLUDE_MODE_LIST(0),
		eEXCLUDE_MODE_ADD(1),
		eEXCLUDE_MODE_DEL(2),
		;

		private int value;

		eExcludeMode(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eExcludeMode v) {
			return value == v.value;
		}

		public static eExcludeMode fromInt(int i) {
			switch (i) {
				case 0:
					return eEXCLUDE_MODE_LIST;
				case 1:
					return eEXCLUDE_MODE_ADD;
				case 2:
					return eEXCLUDE_MODE_DEL;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eExcludeMode, %d", i));
			}
		}
	}

	public enum eExcludeType {
		eEXCLUDE_TYPE_CHAT(0),
		eEXCLUDE_TYPE_MAIL(1),
		eEXCLUDE_TYPE_MAX(2),
		;

		private int value;

		eExcludeType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eExcludeType v) {
			return value == v.value;
		}

		public static eExcludeType fromInt(int i) {
			switch (i) {
				case 0:
					return eEXCLUDE_TYPE_CHAT;
				case 1:
					return eEXCLUDE_TYPE_MAIL;
				case 2:
					return eEXCLUDE_TYPE_MAX;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eExcludeType，%d", i));
			}
		}
	}
}
