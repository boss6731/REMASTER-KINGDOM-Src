package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.IndunEx.RoomInfo.MJIndunRoomController;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaMapKind;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eDistributionType;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ newInstance() {
		return new CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ();
	}

	private int _room_id;
	private byte[] _title;
	private int _min_level;
	private boolean _closed;
	private String _password;
	private eDistributionType _distribution_type;
	private int _max_player;
	private eArenaMapKind _map_kind;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ() {
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

	public byte[] get_title() {
		return _title;
	}

	public void set_title(byte[] val) {
		_bit |= 0x2;
		_title = val;
	}

	public boolean has_title() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_min_level() {
		return _min_level;
	}

	public void set_min_level(int val) {
		_bit |= 0x4;
		_min_level = val;
	}

	public boolean has_min_level() {
		return (_bit & 0x4) == 0x4;
	}

	public boolean get_closed() {
		return _closed;
	}

	public void set_closed(boolean val) {
		_bit |= 0x8;
		_closed = val;
	}

	public boolean has_closed() {
		return (_bit & 0x8) == 0x8;
	}

	public String get_password() {
		return _password;
	}

	public void set_password(String val) {
		_bit |= 0x10;
		_password = val;
	}

	public boolean has_password() {
		return (_bit & 0x10) == 0x10;
	}

	public eDistributionType get_distribution_type() {
		return _distribution_type;
	}

	public void set_distribution_type(eDistributionType val) {
		_bit |= 0x20;
		_distribution_type = val;
	}

	public boolean has_distribution_type() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_max_player() {
		return _max_player;
	}

	public void set_max_player(int val) {
		_bit |= 0x40;
		_max_player = val;
	}

	public boolean has_max_player() {
		return (_bit & 0x40) == 0x40;
	}

	public eArenaMapKind get_map_kind() {
		return _map_kind;
	}

	public void set_map_kind(eArenaMapKind val) {
		_bit |= 0x80;
		_map_kind = val;
	}

	public boolean has_map_kind() {
		return (_bit & 0x80) == 0x80;
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
		if (has_title()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _title);
		}
		if (has_min_level()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _min_level);
		}
		if (has_closed()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _closed);
		}
		if (has_password()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(5, _password);
		}
		if (has_distribution_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(6, _distribution_type.toInt());
		}
		if (has_max_player()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _max_player);
		}
		if (has_map_kind()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(8, _map_kind.toInt());
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
		if (!has_title()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_min_level()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_closed()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_password()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_distribution_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_max_player()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_map_kind()) {
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
		if (has_title()) {
			output.writeBytes(2, _title);
		}
		if (has_min_level()) {
			output.wirteInt32(3, _min_level);
		}
		if (has_closed()) {
			output.writeBool(4, _closed);
		}
		if (has_password()) {
			output.writeString(5, _password);
		}
		if (has_distribution_type()) {
			output.writeEnum(6, _distribution_type.toInt());
		}
		if (has_max_player()) {
			output.wirteInt32(7, _max_player);
		}
		if (has_map_kind()) {
			output.writeEnum(8, _map_kind.toInt());
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
					set_title(input.readBytes());
					break;
				}
				case 0x00000018: {
					set_min_level(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_closed(input.readBool());
					break;
				}
				case 0x0000002A: {
					set_password(input.readString());
					break;
				}
				case 0x00000030: {
					set_distribution_type(eDistributionType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000038: {
					set_max_player(input.readInt32());
					break;
				}
				case 0x00000040: {
					set_map_kind(eArenaMapKind.fromInt(input.readEnum()));
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
			MJIndunRoomController.getInstance().onChangedRoomProperty(pc, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ();
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
