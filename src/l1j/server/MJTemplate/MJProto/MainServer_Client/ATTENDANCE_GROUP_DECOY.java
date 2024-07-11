package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class ATTENDANCE_GROUP_DECOY implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ATTENDANCE_GROUP_DECOY newInstance() {
		return new ATTENDANCE_GROUP_DECOY();
	}

	private int _group_id;
	private int _attendance_id;
	private int _season_num;
	private int _playtimeminute;
	private int _status;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private ATTENDANCE_GROUP_DECOY() {
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

	public int get_attendance_id() {
		return _attendance_id;
	}

	public void set_attendance_id(int val) {
		_bit |= 0x2;
		_attendance_id = val;
	}

	public boolean has_attendance_id() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_season_num() {
		return _season_num;
	}

	public void set_season_num(int val) {
		_bit |= 0x4;
		_season_num = val;
	}

	public boolean has_season_num() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_playtimeminute() {
		return _playtimeminute;
	}

	public void set_playtimeminute(int val) {
		_bit |= 0x8;
		_playtimeminute = val;
	}

	public boolean has_playtimeminute() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_status() {
		return _status;
	}

	public void set_status(int val) {
		_bit |= 0x10;
		_status = val;
	}

	public boolean has_status() {
		return (_bit & 0x10) == 0x10;
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
		if (has_attendance_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _attendance_id);
		}
		if (has_season_num()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _season_num);
		}
		if (has_playtimeminute()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _playtimeminute);
		}
		if (has_status()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _status);
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
		if (!has_attendance_id()) {
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
		if (has_attendance_id()) {
			output.wirteInt32(2, _attendance_id);
		}
		if (has_season_num()) {
			output.writeUInt32(3, _season_num);
		}
		if (has_playtimeminute()) {
			output.writeUInt32(4, _playtimeminute);
		}
		if (has_status()) {
			output.wirteInt32(5, _status);
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
					set_attendance_id(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_season_num(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_playtimeminute(input.readUInt32());
					break;
				}
				case 0x00000028: {
					set_status(input.readInt32());
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
		return new ATTENDANCE_GROUP_DECOY();
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
