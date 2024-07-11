package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;

public class SC_ATTENDANCE_BONUS_INFO_EXTEND implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	private static ProtoOutputStream _default_bonusInfo;
	private static ProtoOutputStream _default_bonusInfo1;

	public static void load() {
		try {
			SC_ATTENDANCE_BONUS_INFO_EXTEND bonusInfo = (SC_ATTENDANCE_BONUS_INFO_EXTEND) MJEProtoMessages.SC_ATTENDANCE_BONUS_INFO_EXTEND
					.getMessageInstance();
			bonusInfo.set_checkInterval(MJAttendanceLoadManager.ATTEN_CHECK_INTERVAL / 60);
			bonusInfo.set_resetPeriod(MJAttendanceLoadManager.ATTEN_RESET_PERIOD_SECOND / 3600);
			bonusInfo.set_dailyMaxCount(MJAttendanceLoadManager.ATTEN_DAILY_MAX_COUNT);
			bonusInfo.set_weekendMaxCount(MJAttendanceLoadManager.ATTEN_WEEKEND_MAX_COUNT);
			bonusInfo.set_totalBonusGroupCount(AttendanceGroupType.getUseSize());
			bonusInfo.set_rest_gauge_icon_display(true); // 출석체크 나뭇잎 아이콘 새로나옴
			bonusInfo.set_rest_gauge_bonus_display(100); // 10-16 아인 지급 표기
			bonusInfo.set_season_num(0);
			ProtoOutputStream old_bonusInfo = _default_bonusInfo;
			_default_bonusInfo = bonusInfo.writeTo(MJEProtoMessages.SC_ATTENDANCE_BONUS_INFO_EXTEND);
			// System.out.println(_default_bonusInfo);
			if (old_bonusInfo != null) {
				old_bonusInfo.dispose();
				old_bonusInfo = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void load1() {
		try {
			SC_ATTENDANCE_BONUS_INFO_EXTEND bonusInfo = (SC_ATTENDANCE_BONUS_INFO_EXTEND) MJEProtoMessages.SC_ATTENDANCE_BONUS_INFO_EXTEND
					.getMessageInstance();
			bonusInfo.set_checkInterval(MJAttendanceLoadManager.ATTEN_CHECK_INTERVAL / 60);
			bonusInfo.set_resetPeriod(MJAttendanceLoadManager.ATTEN_RESET_PERIOD_SECOND / 3600);
			bonusInfo.set_dailyMaxCount(MJAttendanceLoadManager.ATTEN_DAILY_MAX_COUNT);
			bonusInfo.set_weekendMaxCount(MJAttendanceLoadManager.ATTEN_WEEKEND_MAX_COUNT);
			bonusInfo.set_totalBonusGroupCount(AttendanceGroupType.getUseSize());
			bonusInfo.set_rest_gauge_icon_display(true); // 출석체크 나뭇잎 아이콘 새로나옴
			bonusInfo.set_rest_gauge_bonus_display(100); // 10-16 아인 지급 표기
			bonusInfo.set_season_num(1);
			ProtoOutputStream old_bonusInfo = _default_bonusInfo1;
			_default_bonusInfo1 = bonusInfo.writeTo(MJEProtoMessages.SC_ATTENDANCE_BONUS_INFO_EXTEND);
			// System.out.println(_default_bonusInfo);
			if (old_bonusInfo != null) {
				old_bonusInfo.dispose();
				old_bonusInfo = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void send(L1PcInstance pc) {
		pc.sendPackets(_default_bonusInfo, false);
		pc.sendPackets(_default_bonusInfo1, false);
	}

	public static SC_ATTENDANCE_BONUS_INFO_EXTEND newInstance() {
		return new SC_ATTENDANCE_BONUS_INFO_EXTEND();
	}

	private int _checkInterval;
	private int _resetPeriod;
	private int _dailyMaxCount;
	private int _weekendMaxCount;
	private int _totalBonusGroupCount;
	private boolean _rest_gauge_icon_display; // 추가
	private int _rest_gauge_bonus_display; // 추가
	private int _season_num; // 추가
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ATTENDANCE_BONUS_INFO_EXTEND() {
	}

	public int get_checkInterval() {
		return _checkInterval;
	}

	public void set_checkInterval(int val) {
		_bit |= 0x00000001;
		_checkInterval = val;
	}

	public boolean has_checkInterval() {
		return (_bit & 0x00000001) == 0x00000001;
	}

	public int get_resetPeriod() {
		return _resetPeriod;
	}

	public void set_resetPeriod(int val) {
		_bit |= 0x00000002;
		_resetPeriod = val;
	}

	public boolean has_resetPeriod() {
		return (_bit & 0x00000002) == 0x00000002;
	}

	public int get_dailyMaxCount() {
		return _dailyMaxCount;
	}

	public void set_dailyMaxCount(int val) {
		_bit |= 0x00000004;
		_dailyMaxCount = val;
	}

	public boolean has_dailyMaxCount() {
		return (_bit & 0x00000004) == 0x00000004;
	}

	public int get_weekendMaxCount() {
		return _weekendMaxCount;
	}

	public void set_weekendMaxCount(int val) {
		_bit |= 0x00000008;
		_weekendMaxCount = val;
	}

	public boolean has_weekendMaxCount() {
		return (_bit & 0x00000008) == 0x00000008;
	}

	public int get_totalBonusGroupCount() {
		return _totalBonusGroupCount;
	}

	public void set_totalBonusGroupCount(int val) {
		_bit |= 0x00000010;
		_totalBonusGroupCount = val;
	}

	public boolean has_totalBonusGroupCount() {
		return (_bit & 0x00000010) == 0x00000010;
	}

	public boolean get_rest_gauge_icon_display() {
		return _rest_gauge_icon_display;
	}

	public void set_rest_gauge_icon_display(boolean val) {
		_bit |= 0x20;
		_rest_gauge_icon_display = val;
	}

	public boolean has_rest_gauge_icon_display() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_rest_gauge_bonus_display() {
		return _rest_gauge_bonus_display;
	}

	public void set_rest_gauge_bonus_display(int val) {
		_bit |= 0x40;
		_rest_gauge_bonus_display = val;
	}

	public boolean has_rest_gauge_bonus_display() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_season_num() {
		return _season_num;
	}

	public void set_season_num(int val) {
		_bit |= 0x80;
		_season_num = val;
	}

	public boolean has_season_num() {
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
		if (has_checkInterval()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _checkInterval);
		}
		if (has_resetPeriod()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _resetPeriod);
		}
		if (has_dailyMaxCount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _dailyMaxCount);
		}
		if (has_weekendMaxCount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _weekendMaxCount);
		}
		if (has_totalBonusGroupCount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _totalBonusGroupCount);
		}
		if (has_rest_gauge_icon_display()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(6, _rest_gauge_icon_display);
		}
		if (has_rest_gauge_bonus_display()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(7, _rest_gauge_bonus_display);
		}
		if (has_season_num()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(8, _season_num);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_checkInterval()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_resetPeriod()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_dailyMaxCount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_weekendMaxCount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_totalBonusGroupCount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_checkInterval()) {
			output.wirteInt32(1, _checkInterval);
		}
		if (has_resetPeriod()) {
			output.wirteInt32(2, _resetPeriod);
		}
		if (has_dailyMaxCount()) {
			output.wirteInt32(3, _dailyMaxCount);
		}
		if (has_weekendMaxCount()) {
			output.wirteInt32(4, _weekendMaxCount);
		}
		if (has_totalBonusGroupCount()) {
			output.wirteInt32(5, _totalBonusGroupCount);
		}
		if (has_rest_gauge_icon_display()) {
			output.writeBool(6, _rest_gauge_icon_display);
		}
		if (has_rest_gauge_bonus_display()) {
			output.writeUInt32(7, _rest_gauge_bonus_display);
		}
		if (has_season_num()) {
			output.writeUInt32(8, _season_num);
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
					set_checkInterval(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_resetPeriod(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_dailyMaxCount(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_weekendMaxCount(input.readInt32());
					break;
				}
				case 0x00000028: {
					set_totalBonusGroupCount(input.readInt32());
					break;
				}
				case 0x00000030: {
					set_rest_gauge_icon_display(input.readBool());
					break;
				}
				case 0x00000038: {
					set_rest_gauge_bonus_display(input.readUInt32());
					break;
				}
				case 0x00000040: {
					set_season_num(input.readUInt32());
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

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.
			// System.out.println("패킷오는지 체크");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_ATTENDANCE_BONUS_INFO_EXTEND();
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
}
