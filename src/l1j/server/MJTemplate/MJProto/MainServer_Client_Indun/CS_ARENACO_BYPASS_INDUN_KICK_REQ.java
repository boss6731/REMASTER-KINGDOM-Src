package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.IndunEx.RoomInfo.MJIndunRoomController;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ARENACO_BYPASS_INDUN_KICK_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_ARENACO_BYPASS_INDUN_KICK_REQ newInstance() {
		return new CS_ARENACO_BYPASS_INDUN_KICK_REQ();
	}

	private int _room_id;
	private long _kick_arena_char_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_ARENACO_BYPASS_INDUN_KICK_REQ() {
	}

	public int get_room_id() {
		return _room_id;
	}

	public void set_room_id(int val) {
		_bit |= 0x1;
		_room_id = val;
	}

	public boolean has_room_id() {
		return (_bit & 0x1) == 0x1;
	}

	public long get_kick_arena_char_id() {
		return _kick_arena_char_id;
	}

	public void set_kick_arena_char_id(long val) {
		_bit |= 0x2;
		_kick_arena_char_id = val;
	}

	public boolean has_kick_arena_char_id() {
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
		if (has_room_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _room_id);
		}
		if (has_kick_arena_char_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(2, _kick_arena_char_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_room_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_kick_arena_char_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_room_id()) {
			output.writeUInt32(1, _room_id);
		}
		if (has_kick_arena_char_id()) {
			output.writeInt64(2, _kick_arena_char_id);
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
					set_room_id(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_kick_arena_char_id(input.readInt64());
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
			MJIndunRoomController.getInstance().onKick(pc, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_ARENACO_BYPASS_INDUN_KICK_REQ();
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
