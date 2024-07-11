package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.IndunEx.RoomInfo.MJIndunRoomController;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaMapKind;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ARENACO_BYPASS_INDUN_ROOM_LIST_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_ARENACO_BYPASS_INDUN_ROOM_LIST_REQ newInstance() {
		return new CS_ARENACO_BYPASS_INDUN_ROOM_LIST_REQ();
	}

	private int _page_id;
	private eArenaMapKind _map_kind;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_ARENACO_BYPASS_INDUN_ROOM_LIST_REQ() {
		set_page_id(1);
		set_map_kind(eArenaMapKind.None);
	}

	public int get_page_id() {
		return _page_id;
	}

	public void set_page_id(int val) {
		_bit |= 0x1;
		_page_id = val;
	}

	public boolean has_page_id() {
		return (_bit & 0x1) == 0x1;
	}

	public eArenaMapKind get_map_kind() {
		return _map_kind;
	}

	public void set_map_kind(eArenaMapKind val) {
		_bit |= 0x2;
		_map_kind = val;
	}

	public boolean has_map_kind() {
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
		if (has_page_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _page_id);
		}
		if (has_map_kind()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _map_kind.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_page_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_page_id()) {
			output.writeUInt32(1, _page_id);
		}
		if (has_map_kind()) {
			output.writeEnum(2, _map_kind.toInt());
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
					set_page_id(input.readUInt32());
					break;
				}
				case 0x00000010: {
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

			MJObjectEventProvider.provider().pcEventFactory().fireIndun(pc);

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			MJIndunRoomController.getInstance().onShowListRooms(pc, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_ARENACO_BYPASS_INDUN_ROOM_LIST_REQ();
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
