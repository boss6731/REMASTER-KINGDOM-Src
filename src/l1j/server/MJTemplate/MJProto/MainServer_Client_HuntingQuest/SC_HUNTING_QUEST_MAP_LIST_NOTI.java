package l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_HUNTING_QUEST_MAP_LIST_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_HUNTING_QUEST_MAP_LIST_NOTI newInstance() {
		return new SC_HUNTING_QUEST_MAP_LIST_NOTI();
	}

	private java.util.LinkedList<HUNTING_QUEST_MAP_INFO> _hunting_quest_map_list;
	private int _remain_quest_count;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_HUNTING_QUEST_MAP_LIST_NOTI() {
	}

	public java.util.LinkedList<HUNTING_QUEST_MAP_INFO> get_hunting_quest_map_list() {
		return _hunting_quest_map_list;
	}

	public void add_hunting_quest_map_list(HUNTING_QUEST_MAP_INFO val) {
		if (!has_hunting_quest_map_list()) {
			_hunting_quest_map_list = new java.util.LinkedList<HUNTING_QUEST_MAP_INFO>();
			_bit |= 0x1;
		}
		_hunting_quest_map_list.add(val);
	}

	public boolean has_hunting_quest_map_list() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_remain_quest_count() {
		return _remain_quest_count;
	}

	public void set_remain_quest_count(int val) {
		_bit |= 0x2;
		_remain_quest_count = val;
	}

	public boolean has_remain_quest_count() {
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
		if (has_hunting_quest_map_list()) {
			for (HUNTING_QUEST_MAP_INFO val : _hunting_quest_map_list) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_remain_quest_count()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _remain_quest_count);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_hunting_quest_map_list()) {
			for (HUNTING_QUEST_MAP_INFO val : _hunting_quest_map_list) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_remain_quest_count()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_hunting_quest_map_list()) {
			for (HUNTING_QUEST_MAP_INFO val : _hunting_quest_map_list) {
				output.writeMessage(1, val);
			}
		}
		if (has_remain_quest_count()) {
			output.wirteInt32(2, _remain_quest_count);
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
				case 0x0000000A: {
					add_hunting_quest_map_list(
							(HUNTING_QUEST_MAP_INFO) input.readMessage(HUNTING_QUEST_MAP_INFO.newInstance()));
					break;
				}
				case 0x00000010: {
					set_remain_quest_count(input.readInt32());
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
		return new SC_HUNTING_QUEST_MAP_LIST_NOTI();
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
