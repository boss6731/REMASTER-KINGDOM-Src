package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.IndunEx.RoomInfo.MJIndunRoomController;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaMapKind;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eEnterType;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ARENACO_ENTER_INDUN_ROOM_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_ARENACO_ENTER_INDUN_ROOM_REQ newInstance() {
		return new CS_ARENACO_ENTER_INDUN_ROOM_REQ();
	}

	private int _room_id;
	private String _password;
	private boolean _invite_accept;
	private byte[] _host_char_name;
	private eArenaMapKind _mapkind; // 08.31추가
	private boolean _matching_owner;
	private eEnterType _enter_type;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_ARENACO_ENTER_INDUN_ROOM_REQ() {
		set_mapkind(eArenaMapKind.None);
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

	public String get_password() {
		return _password;
	}

	public void set_password(String val) {
		_bit |= 0x2;
		_password = val;
	}

	public boolean has_password() {
		return (_bit & 0x2) == 0x2;
	}

	public boolean get_invite_accept() {
		return _invite_accept;
	}

	public void set_invite_accept(boolean val) {
		_bit |= 0x4;
		_invite_accept = val;
	}

	public boolean has_invite_accept() {
		return (_bit & 0x4) == 0x4;
	}

	public byte[] get_host_char_name() {
		return _host_char_name;
	}

	public void set_host_char_name(byte[] val) {
		_bit |= 0x8;
		_host_char_name = val;
	}

	public boolean has_host_char_name() {
		return (_bit & 0x8) == 0x8;
	}

	public eArenaMapKind get_mapkind() {
		return _mapkind;
	}

	public void set_mapkind(eArenaMapKind val) {
		_bit |= 0x10;
		_mapkind = val;
	}

	public boolean has_mapkind() {
		return (_bit & 0x10) == 0x10;
	}

	public boolean get_matching_owner() {
		return _matching_owner;
	}

	public void set_matching_owner(boolean val) {
		_bit |= 0x20;
		_matching_owner = val;
	}

	public boolean has_matching_owner() {
		return (_bit & 0x20) == 0x20;
	}

	public eEnterType get_enter_type() {
		return _enter_type;
	}

	public void set_enter_type(eEnterType val) {
		_bit |= 0x40;
		_enter_type = val;
	}

	public boolean has_enter_type() {
		return (_bit & 0x40) == 0x40;
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
		if (has_password()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _password);
		}
		if (has_invite_accept()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _invite_accept);
		}
		if (has_host_char_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _host_char_name);
		}
		if (has_mapkind()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(5, _mapkind.toInt());
		}
		if (has_matching_owner()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(6, _matching_owner);
		}
		if (has_enter_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(7, _enter_type.toInt());
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
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_room_id()) {
			output.writeUInt32(1, _room_id);
		}
		if (has_password()) {
			output.writeString(2, _password);
		}
		if (has_invite_accept()) {
			output.writeBool(3, _invite_accept);
		}
		if (has_host_char_name()) {
			output.writeBytes(4, _host_char_name);
		}
		if (has_mapkind()) {
			output.writeEnum(5, _mapkind.toInt());
		}
		if (has_matching_owner()) {
			output.writeBool(6, _matching_owner);
		}
		if (has_enter_type()) {
			output.writeEnum(7, _enter_type.toInt());
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
				case 0x00000012: {
					set_password(input.readString());
					break;
				}
				case 0x00000018: {
					set_invite_accept(input.readBool());
					break;
				}
				case 0x00000022: {
					set_host_char_name(input.readBytes());
					break;
				}
				case 0x00000028: {
					set_mapkind(eArenaMapKind.fromInt(input.readEnum()));
					break;
				}
				case 0x00000030: {
					set_matching_owner(input.readBool());
					break;
				}
				case 0x00000038: {
					set_enter_type(eEnterType.fromInt(input.readEnum()));
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
			MJIndunRoomController.getInstance().onEnterRoom(pc, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_ARENACO_ENTER_INDUN_ROOM_REQ();
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
