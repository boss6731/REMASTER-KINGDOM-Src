package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_ATTENDANCE_TIME_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_ATTENDANCE_TIME_INFO newInstance() {
		return new SC_ATTENDANCE_TIME_INFO();
	}

	private int _attendanceTime;
	private int _attendanceStartTime;
	private int _todayRewardCount;
	private int _remainedTime;
	private int _remainedRewardCount;
	private UserAttendanceTimeStatus _resultCode;
	private boolean _rewardableUser;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ATTENDANCE_TIME_INFO() {
	}

	public int get_attendanceTime() {
		return _attendanceTime;
	}

	public void set_attendanceTime(int val) {
		_bit |= 0x1;
		_attendanceTime = val;
	}

	public boolean has_attendanceTime() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_attendanceStartTime() {
		return _attendanceStartTime;
	}

	public void set_attendanceStartTime(int val) {
		_bit |= 0x2;
		_attendanceStartTime = val;
	}

	public boolean has_attendanceStartTime() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_todayRewardCount() {
		return _todayRewardCount;
	}

	public void set_todayRewardCount(int val) {
		_bit |= 0x4;
		_todayRewardCount = val;
	}

	public boolean has_todayRewardCount() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_remainedTime() {
		return _remainedTime;
	}

	public void set_remainedTime(int val) {
		_bit |= 0x8;
		_remainedTime = val;
	}

	public boolean has_remainedTime() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_remainedRewardCount() {
		return _remainedRewardCount;
	}

	public void set_remainedRewardCount(int val) {
		_bit |= 0x10;
		_remainedRewardCount = val;
	}

	public boolean has_remainedRewardCount() {
		return (_bit & 0x10) == 0x10;
	}

	public UserAttendanceTimeStatus get_resultCode() {
		return _resultCode;
	}

	public void set_resultCode(UserAttendanceTimeStatus val) {
		_bit |= 0x20;
		_resultCode = val;
	}

	public boolean has_resultCode() {
		return (_bit & 0x20) == 0x20;
	}

	public boolean get_rewardableUser() {
		return _rewardableUser;
	}

	public void set_rewardableUser(boolean val) {
		_bit |= 0x40;
		_rewardableUser = val;
	}

	public boolean has_rewardableUser() {
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
		if (has_attendanceTime()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _attendanceTime);
		}
		if (has_attendanceStartTime()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _attendanceStartTime);
		}
		if (has_todayRewardCount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _todayRewardCount);
		}
		if (has_remainedTime()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _remainedTime);
		}
		if (has_remainedRewardCount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _remainedRewardCount);
		}
		if (has_resultCode()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(6, _resultCode.toInt());
		}
		if (has_rewardableUser()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(7, _rewardableUser);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_attendanceTime()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_attendanceStartTime()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_todayRewardCount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_remainedTime()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_remainedRewardCount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_resultCode()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_rewardableUser()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_attendanceTime()) {
			output.wirteInt32(1, _attendanceTime);
		}
		if (has_attendanceStartTime()) {
			output.wirteInt32(2, _attendanceStartTime);
		}
		if (has_todayRewardCount()) {
			output.wirteInt32(3, _todayRewardCount);
		}
		if (has_remainedTime()) {
			output.wirteInt32(4, _remainedTime);
		}
		if (has_remainedRewardCount()) {
			output.wirteInt32(5, _remainedRewardCount);
		}
		if (has_resultCode()) {
			output.writeEnum(6, _resultCode.toInt());
		}
		if (has_rewardableUser()) {
			output.writeBool(7, _rewardableUser);
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
					set_attendanceTime(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_attendanceStartTime(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_todayRewardCount(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_remainedTime(input.readInt32());
					break;
				}
				case 0x00000028: {
					set_remainedRewardCount(input.readInt32());
					break;
				}
				case 0x00000030: {
					set_resultCode(UserAttendanceTimeStatus.fromInt(input.readEnum()));
					break;
				}
				case 0x00000038: {
					set_rewardableUser(input.readBool());
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
		return new SC_ATTENDANCE_TIME_INFO();
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
