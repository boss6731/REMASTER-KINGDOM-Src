package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.GameClient;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_MOVE_SERVER_AUTH_ERROR_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send(GameClient clnt, eErrorType type) {
		SC_MOVE_SERVER_AUTH_ERROR_ACK ack = newInstance();
		ack.set_error(type);
		clnt.sendPacket(ack, MJEProtoMessages.SC_MOVE_SERVER_AUTH_ERROR_ACK.toInt());
	}

	public static SC_MOVE_SERVER_AUTH_ERROR_ACK newInstance() {
		return new SC_MOVE_SERVER_AUTH_ERROR_ACK();
	}

	private eErrorType _error;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_MOVE_SERVER_AUTH_ERROR_ACK() {
	}

	public eErrorType get_error() {
		return _error;
	}

	public void set_error(eErrorType val) {
		_bit |= 0x1;
		_error = val;
	}

	public boolean has_error() {
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
		if (has_error())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _error.toInt());
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_error()) {
			output.writeEnum(1, _error.toInt());
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
					set_error(eErrorType.fromInt(input.readEnum()));
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
		return new SC_MOVE_SERVER_AUTH_ERROR_ACK();
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

	public enum eErrorType {
		error_fail(1),
		error_full(2),
		error_notopen(3),
		error_not_register(4),
		error_already_reserve(5),
		error_wait(6),
		error_logout(7),
		error_complete(8),
		error_not_connected(9),
		error_system_error(10),
		error_not_find(11),
		error_not_ingame(12),
		error_invalid_server(13),
		error_invalid_state(14),
		error_not_security(15);

		private int value;

		eErrorType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eErrorType v) {
			return value == v.value;
		}

		private static final java.util.HashMap<Integer, eErrorType> _eErrorTypes;
		static {
			_eErrorTypes = new java.util.HashMap<Integer, eErrorType>(15);
			for (eErrorType v : eErrorType.values())
				_eErrorTypes.put(v.toInt(), v);
		}

		public static eErrorType fromInt(int i) {
			return _eErrorTypes.get(i);
		}
	}
}
