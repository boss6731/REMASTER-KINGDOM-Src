package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_VOICE_CHAT_ROOM_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_VOICE_CHAT_ROOM_INFO_NOTI newInstance() {
		return new SC_VOICE_CHAT_ROOM_INFO_NOTI();
	}

	public static void send(L1PcInstance pc, int _roomKey, int _serverKey, long _gameRoomId) {
		SC_VOICE_CHAT_ROOM_INFO_NOTI noti = SC_VOICE_CHAT_ROOM_INFO_NOTI.newInstance();
		noti.set_roomKey(_roomKey);
		noti.set_serverKey(_serverKey);
		noti.set_gameRoomId(_gameRoomId);
		noti.set_roomType(eChatRoomType.NONE);
		pc.sendPackets(noti, MJEProtoMessages.SC_VOICE_CHAT_ROOM_INFO_NOTI, true);
	}

	private SC_VOICE_CHAT_ROOM_INFO_NOTI.eChatRoomType _roomType;
	private int _roomKey;
	private int _serverKey;
	private long _gameRoomId;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_VOICE_CHAT_ROOM_INFO_NOTI() {
	}

	public SC_VOICE_CHAT_ROOM_INFO_NOTI.eChatRoomType get_roomType() {
		return _roomType;
	}

	public void set_roomType(SC_VOICE_CHAT_ROOM_INFO_NOTI.eChatRoomType val) {
		_bit |= 0x1;
		_roomType = val;
	}

	public boolean has_roomType() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_roomKey() {
		return _roomKey;
	}

	public void set_roomKey(int val) {
		_bit |= 0x2;
		_roomKey = val;
	}

	public boolean has_roomKey() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_serverKey() {
		return _serverKey;
	}

	public void set_serverKey(int val) {
		_bit |= 0x4;
		_serverKey = val;
	}

	public boolean has_serverKey() {
		return (_bit & 0x4) == 0x4;
	}

	public long get_gameRoomId() {
		return _gameRoomId;
	}

	public void set_gameRoomId(long val) {
		_bit |= 0x8;
		_gameRoomId = val;
	}

	public boolean has_gameRoomId() {
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
		if (has_roomType()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _roomType.toInt());
		}
		if (has_roomKey()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _roomKey);
		}
		if (has_serverKey()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _serverKey);
		}
		if (has_gameRoomId()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(4, _gameRoomId);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_roomType()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_roomKey()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_serverKey()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_gameRoomId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_roomType()) {
			output.writeEnum(1, _roomType.toInt());
		}
		if (has_roomKey()) {
			output.writeUInt32(2, _roomKey);
		}
		if (has_serverKey()) {
			output.writeUInt32(3, _serverKey);
		}
		if (has_gameRoomId()) {
			output.wirteUInt64(4, _gameRoomId);
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
					set_roomType(SC_VOICE_CHAT_ROOM_INFO_NOTI.eChatRoomType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_roomKey(input.readUInt32());
					break;
				}
				case 0x00000018: {
					set_serverKey(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_gameRoomId(input.readUInt64());
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
		return new SC_VOICE_CHAT_ROOM_INFO_NOTI();
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

	public enum eChatRoomType {
		NONE(0),
		PARTY(1),
		BLOOD_PLEDGE(2),
		;

		private int value;

		eChatRoomType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eChatRoomType v) {
			return value == v.value;
		}

		public static eChatRoomType fromInt(int i) {
			switch (i) {
				case 0:
					return NONE;
				case 1:
					return PARTY;
				case 2:
					return BLOOD_PLEDGE;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eChatRoomType，%d", i));
			}
		}
	}
}
