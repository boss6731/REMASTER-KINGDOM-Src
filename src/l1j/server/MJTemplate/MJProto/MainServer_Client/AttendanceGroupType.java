package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.util.ArrayList;

import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum AttendanceGroupType {
	NORMAL(0),
	PC_CAFE(1),
	PREMIUM(2),
	SPECIAL(3),
	BRAVE_WARRIOR(4),
	ADEN_WORLD(5),
	BRAVERY_MEDAL(6);

	private int value;
	private SC_ATTENDANCE_BONUS_GROUP_INFO _gInfo;
	private ProtoOutputStream _gInfoStream;
	private int _bonusSize;
	private int _checkSum;

	AttendanceGroupType(int val) {
		value = val;
		_checkSum = 1 << val;
	}

	public int toInt() {
		return value;
	}

	public int toCheckSum() {
		return _checkSum;
	}

	public boolean isCheckSum(int val) {
		return (val & _checkSum) == _checkSum;
	}

	public boolean equals(AttendanceGroupType v) {
		return value == v.value;
	}

	public int getBonusSize() {
		return _bonusSize;
	}

	public void setGroupInfo(SC_ATTENDANCE_BONUS_GROUP_INFO gInfo) {
		ProtoOutputStream tmpStream = _gInfoStream;
		SC_ATTENDANCE_BONUS_GROUP_INFO tmpInfo = _gInfo;
		if (gInfo.get_bonusGroup() != null)
			_bonusSize = gInfo.get_bonusGroup().size();
		gInfo.set_groupType(this);
		_gInfo = gInfo;
		_gInfoStream = gInfo.writeTo(MJEProtoMessages.SC_ATTENDANCE_BONUS_GROUP_INFO);
		if (tmpStream != null) {
			tmpStream.dispose();
			tmpStream = null;
		}

		if (tmpInfo != null) {
			tmpInfo.dispose();
			tmpInfo = null;
		}
	}

	public SC_ATTENDANCE_BONUS_GROUP_INFO getGroupInfo() {
		return _gInfo;
	}

	public void send(L1PcInstance pc) {
		if (_gInfoStream != null)
			pc.sendPackets(_gInfoStream, false);
	}

	public boolean is_probability_reward(int index) {
		AttendanceBonusGroup bGroup = _gInfo.get_bonusGroup().get(index);
		int size = bGroup.get_bonuses().size();
		return size > 1;
	}

	public AttendanceBonusInfo select_reward(int index) {
		AttendanceBonusGroup bGroup = _gInfo.get_bonusGroup().get(index);
		int size = bGroup.get_bonuses().size();
		if (size == 1) {
			return bGroup.get_bonuses().get(0);
		}

		int total_probability = 0;
		for (AttendanceBonusInfo bInfo : bGroup.get_bonuses())
			total_probability += bInfo.get_probability();

		for (AttendanceBonusInfo bInfo : bGroup.get_bonuses()) {
			int current_probability = bInfo.get_probability();
			if (MJRnd.isWinning(total_probability, current_probability)) {
				return bInfo;
			}
		}
		return null;
	}

	private static final ArrayList<AttendanceGroupType> USE_LIST;
	static {
		USE_LIST = new ArrayList<AttendanceGroupType>();
		for (AttendanceGroupType type : AttendanceGroupType.values()) {
			if (type == PC_CAFE && !MJAttendanceLoadManager.ATTEN_PC_IS_RUNNING) {
				continue;
			}
			if (type == PREMIUM && !MJAttendanceLoadManager.ATTEN_PREMIUM_USE) {
				continue;
			}
			if (type == SPECIAL && !MJAttendanceLoadManager.ATTEN_SPECIAL_USE) {
				continue;
			}
			if (type == BRAVE_WARRIOR && !MJAttendanceLoadManager.ATTEN_BRAVE_WARRIOR_USE) {
				continue;
			}
			if (type == ADEN_WORLD && !MJAttendanceLoadManager.ATTEN_ADEN_WORLD_USE) {
				continue;
			}
			if (type == BRAVERY_MEDAL && !MJAttendanceLoadManager.ATTEN_BRAVERY_MEDAL_USE) {
				continue;
			}
			USE_LIST.add(type);
		}
	}

	public static ArrayList<AttendanceGroupType> getUseList() {
		return USE_LIST;
	}

	public static int getUseSize() {
		return USE_LIST.size();
	}

	public static AttendanceGroupType fromInt(int i) {
		switch (i) {
			case 0:
				return NORMAL;
			case 1:
				return PC_CAFE;
			case 2:
				return PREMIUM;
			case 3:
				return SPECIAL;
			case 4:
				return BRAVE_WARRIOR;
			case 5:
				return ADEN_WORLD;
			case 6:
				return BRAVERY_MEDAL;
			default:
				throw new IllegalArgumentException(String.format("參數無效 attendancegrouptype，%d", i));
		}
	}
}
