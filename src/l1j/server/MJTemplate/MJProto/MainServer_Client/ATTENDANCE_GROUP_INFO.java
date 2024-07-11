package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class ATTENDANCE_GROUP_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ATTENDANCE_GROUP_INFO newInstance() {
		return new ATTENDANCE_GROUP_INFO();
	}

	private int _group_id;
	private int _max_check_minute;
	private int _cur_complete_today;
	private int _max_complete_today;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private ATTENDANCE_GROUP_INFO() {
	}

	public int get_group_id() {
		return _group_id;
	}

	public void set_group_id(int val) {
		_bit |= 0x1;
		_group_id = val;
	}

	public boolean has_group_id() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_max_check_minute() {
		return _max_check_minute;
	}

	public void set_max_check_minute(int val) {
		_bit |= 0x2;
		_max_check_minute = val;
	}

	public boolean has_max_check_minute() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_cur_complete_today() {
		return _cur_complete_today;
	}

	public void set_cur_complete_today(int val) {
		_bit |= 0x4;
		_cur_complete_today = val;
	}

	public boolean has_cur_complete_today() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_max_complete_today() {
		return _max_complete_today;
	}

	public void set_max_complete_today(int val) {
		_bit |= 0x8;
		_max_complete_today = val;
	}

	public boolean has_max_complete_today() {
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
		if (has_group_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _group_id);
		}
		if (has_max_check_minute()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _max_check_minute);
		}
		if (has_cur_complete_today()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _cur_complete_today);
		}
		if (has_max_complete_today()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _max_complete_today);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_group_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_max_check_minute()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_cur_complete_today()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_max_complete_today()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_group_id()) {
			output.wirteInt32(1, _group_id);
		}
		if (has_max_check_minute()) {
			output.wirteInt32(2, _max_check_minute);
		}
		if (has_cur_complete_today()) {
			output.wirteInt32(3, _cur_complete_today);
		}
		if (has_max_complete_today()) {
			output.wirteInt32(4, _max_complete_today);
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
					set_group_id(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_max_check_minute(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_cur_complete_today(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_max_complete_today(input.readInt32());
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
		return new ATTENDANCE_GROUP_INFO();
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
