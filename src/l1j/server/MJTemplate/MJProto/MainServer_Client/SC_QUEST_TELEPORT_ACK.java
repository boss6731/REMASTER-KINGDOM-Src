package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_QUEST_TELEPORT_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_QUEST_TELEPORT_ACK newInstance() {
		return new SC_QUEST_TELEPORT_ACK();
	}

	private eResultCode _result;
	private int _id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_QUEST_TELEPORT_ACK() {
	}

	public eResultCode get_result() {
		return _result;
	}

	public void set_result(eResultCode val) {
		_bit |= 0x1;
		_result = val;
	}

	public boolean has_result() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int val) {
		_bit |= 0x2;
		_id = val;
	}

	public boolean has_id() {
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
		if (has_result())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		if (has_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _id);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_result()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_result()) {
			output.writeEnum(1, _result.toInt());
		}
		if (has_id()) {
			output.writeUInt32(2, _id);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				default: {
					return this;
				}
				case 0x00000008: {
					set_result(eResultCode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_id(input.readUInt32());
					break;
				}
			}
		}
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Lenz.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_QUEST_TELEPORT_ACK();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public enum eResultCode {
		SUCCESS(0),
		FAIL(1),
		FAIL_NOT_ENOUGH_ADENA(2),
		FAIL_WRONG_LOCATION(3),
		FAIL_CANT_TELEPORT_NOW(4);

		private int value;

		eResultCode(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eResultCode v) {
			return value == v.value;
		}

		public static eResultCode fromInt(int i) {
			switch (i) {
				case 0:
					return SUCCESS;
				case 1:
					return FAIL;
				case 2:
					return FAIL_NOT_ENOUGH_ADENA;
				case 3:
					return FAIL_WRONG_LOCATION;
				case 4:
					return FAIL_CANT_TELEPORT_NOW;
				default:
					return null;
			}
		}
	}
}
