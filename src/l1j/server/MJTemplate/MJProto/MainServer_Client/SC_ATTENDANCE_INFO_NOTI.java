package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;

import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA.UserAttendanceData;
import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.skill.L1SkillId;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ATTENDANCE_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_ATTENDANCE_INFO_NOTI newInstance() {
		return new SC_ATTENDANCE_INFO_NOTI();
	}

	public static void send1(L1PcInstance pc) {
		SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
		if (userData == null) {
			return;
		}
		SC_ATTENDANCE_INFO_NOTI noti = SC_ATTENDANCE_INFO_NOTI.newInstance();
		for (AttendanceGroupType gType : AttendanceGroupType.getUseList()) {
			UserAttendanceDataGroup info = userData.get_groups().get(gType.toInt());
			ATTENDANCE_GROUP_INFO g_info = ATTENDANCE_GROUP_INFO.newInstance();
			g_info.set_group_id(info.get_groupType().toInt());
			g_info.set_max_check_minute(info.get_totalAttendanceTime() / 60);
			g_info.set_cur_complete_today(info.get_recvedRewardCount());
			g_info.set_max_complete_today(info.get_totalRewardCount());
			noti.add_group_info(g_info);
		}
		for (AttendanceGroupType gType : AttendanceGroupType.getUseList()) {
			switch (gType) {
				case NORMAL: {
					UserAttendanceDataGroup info = userData.get_groups().get(gType.toInt());
					int resultCode = info.get_resultCode().toInt();
					int totalBonusCount = gType.getBonusSize();
					for (int i = 0; i < totalBonusCount; i++) {
						ATTENDANCE_INFO a_info = ATTENDANCE_INFO.newInstance();
						UserAttendanceDataExtend attendance_data = info.get_groupData().get(i);
						UserAttendanceState state = attendance_data.get_state();
						int status;
						switch (state) {
							case COMPLETE:
								status = 2;
								break;
							case CLEAR:
								status = 3;
								break;
							default:
								status = 1;
						}
						a_info.set_attendance_id(i + 1);
						a_info.set_group_id(info.get_groupType().toInt());
						a_info.set_status(status);
						a_info.set_playtimeminute((info.get_attendanceTime() / 60));
						a_info.set_season_num(0);
						noti.add_attendance_info(a_info);
						if (status == 1) {
							break;
						}
					}
				}
					break;
				case PC_CAFE: {
					UserAttendanceDataGroup info = userData.get_groups().get(gType.toInt());
					int totalBonusCount = gType.getBonusSize();
					if (pc.isPcBuff()) {
						for (int i = 0; i < totalBonusCount; i++) {
							ATTENDANCE_INFO a_info = ATTENDANCE_INFO.newInstance();
							UserAttendanceDataExtend attendance_data = info.get_groupData().get(i);
							UserAttendanceState state = attendance_data.get_state();
							int status;
							switch (state) {
								case COMPLETE:
									status = 2;
									break;
								case CLEAR:
									status = 3;
									break;
								default:
									status = 1;
							}
							a_info.set_attendance_id(i + 1);
							a_info.set_group_id(info.get_groupType().toInt());
							a_info.set_status(status);
							a_info.set_playtimeminute((info.get_attendanceTime() / 60));
							a_info.set_season_num(0);
							noti.add_attendance_info(a_info);
							if (status == 1) {
								break;
							}
						}
					}
				}
					break;
				case PREMIUM: {
					UserAttendanceDataGroup info = userData.get_groups().get(gType.toInt());
					int totalBonusCount = gType.getBonusSize();
					if (pc.getAccount().getAttendance_Premium()) {
						for (int i = 0; i < totalBonusCount; i++) {
							ATTENDANCE_INFO a_info = ATTENDANCE_INFO.newInstance();
							UserAttendanceDataExtend attendance_data = info.get_groupData().get(i);
							UserAttendanceState state = attendance_data.get_state();
							int status;
							switch (state) {
								case COMPLETE:
									status = 2;
									break;
								case CLEAR:
									status = 3;
									break;
								default:
									status = 1;
							}
							a_info.set_attendance_id(i + 1);
							a_info.set_group_id(info.get_groupType().toInt());
							a_info.set_status(status);
							a_info.set_playtimeminute((info.get_attendanceTime() / 60));
							a_info.set_season_num(0);
							noti.add_attendance_info(a_info);
							if (status == 1) {
								break;
							}
						}
					} else {
						ATTENDANCE_GROUP_DECOY d_info = ATTENDANCE_GROUP_DECOY.newInstance();
						d_info.set_group_id(info.get_groupType().toInt());
						d_info.set_attendance_id(1);
						d_info.set_season_num(0);
						d_info.set_playtimeminute((info.get_attendanceTime() / 60));
						d_info.set_status(1);
						noti.add_attendance_decoy(d_info);
					}
				}
					break;
				case SPECIAL: {
					UserAttendanceDataGroup info = userData.get_groups().get(gType.toInt());
					int totalBonusCount = gType.getBonusSize();
					if (pc.getAccount().getAttendance_Special()) {

						for (int i = 0; i < totalBonusCount; i++) {
							ATTENDANCE_INFO a_info = ATTENDANCE_INFO.newInstance();
							UserAttendanceDataExtend attendance_data = info
									.get_groupData().get(i);
							UserAttendanceState state = attendance_data.get_state();
							int status;
							switch (state) {
								case COMPLETE:
									status = 2;
									break;
								case CLEAR:
									status = 3;
									break;
								default:
									status = 1;
							}
							a_info.set_attendance_id(i + 1);
							a_info.set_group_id(info.get_groupType().toInt());
							a_info.set_status(status);
							a_info.set_playtimeminute((info.get_attendanceTime() / 60));
							a_info.set_season_num(0);
							noti.add_attendance_info(a_info);
							if (status == 1) {
								break;
							}
						}
					} else {
						ATTENDANCE_GROUP_DECOY d_info = ATTENDANCE_GROUP_DECOY.newInstance();
						d_info.set_group_id(info.get_groupType().toInt());
						d_info.set_attendance_id(1);
						d_info.set_season_num(0);
						d_info.set_playtimeminute((info.get_attendanceTime() / 60));
						d_info.set_status(1);
						noti.add_attendance_decoy(d_info);
					}

				}
					break;
				default:
					break;
			}
		}
		// noti.set_title_str(4380); //출석체크 이벤트
		noti.set_title_str(6198);
		pc.sendPackets(noti.writeTo(MJEProtoMessages.SC_ATTENDANCE_INFO_NOTI), true);
	}

	public static void send2(L1PcInstance pc) {
		SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
		if (userData == null) {
			return;
		}
		SC_ATTENDANCE_INFO_NOTI noti = SC_ATTENDANCE_INFO_NOTI.newInstance();
		for (AttendanceGroupType gType : AttendanceGroupType.getUseList()) {
			UserAttendanceDataGroup info = userData.get_groups().get(gType.toInt());
			ATTENDANCE_GROUP_INFO g_info = ATTENDANCE_GROUP_INFO.newInstance();
			g_info.set_group_id(info.get_groupType().toInt());
			g_info.set_max_check_minute(info.get_totalAttendanceTime() / 60);
			g_info.set_cur_complete_today(info.get_recvedRewardCount());
			g_info.set_max_complete_today(info.get_totalRewardCount());
			noti.add_group_info(g_info);
		}
		for (AttendanceGroupType gType : AttendanceGroupType.getUseList()) {
			switch (gType) {
				case BRAVE_WARRIOR: {
					UserAttendanceDataGroup info = userData.get_groups().get(gType.toInt());
					int totalBonusCount = gType.getBonusSize();
					if (pc.getAccount().getAttendance_Brave_Warrior()) {
						for (int i = 0; i < totalBonusCount; i++) {
							ATTENDANCE_INFO a_info = ATTENDANCE_INFO.newInstance();
							UserAttendanceDataExtend attendance_data = info.get_groupData().get(i);
							UserAttendanceState state = attendance_data.get_state();
							int status;
							switch (state) {
								case COMPLETE:
									status = 2;
									break;
								case CLEAR:
									status = 3;
									break;
								default:
									status = 1;
							}
							a_info.set_attendance_id(i + 1);
							a_info.set_group_id(info.get_groupType().toInt());
							a_info.set_status(status);
							a_info.set_playtimeminute((info.get_attendanceTime() / 60));
							a_info.set_season_num(1);
							noti.add_attendance_info(a_info);
							if (status == 1) {
								break;
							}
						}
					} else {
						ATTENDANCE_GROUP_DECOY d_info = ATTENDANCE_GROUP_DECOY.newInstance();
						d_info.set_group_id(info.get_groupType().toInt());
						d_info.set_attendance_id(1);
						d_info.set_season_num(1);
						d_info.set_playtimeminute((info.get_attendanceTime() / 60));
						d_info.set_status(1);
						noti.add_attendance_decoy(d_info);
					}

					break;
				}
				case ADEN_WORLD: {
					UserAttendanceDataGroup info = userData.get_groups().get(gType.toInt());
					int totalBonusCount = gType.getBonusSize();
					if (pc.getAccount().getAttendance_Aden_World()) {
						for (int i = 0; i < totalBonusCount; i++) {
							ATTENDANCE_INFO a_info = ATTENDANCE_INFO.newInstance();
							UserAttendanceDataExtend attendance_data = info.get_groupData().get(i);
							UserAttendanceState state = attendance_data.get_state();
							int status;
							switch (state) {
								case COMPLETE:
									status = 2;
									break;
								case CLEAR:
									status = 3;
									break;
								default:
									status = 1;
							}
							a_info.set_attendance_id(i + 1);
							a_info.set_group_id(info.get_groupType().toInt());
							a_info.set_status(status);
							a_info.set_playtimeminute((info.get_attendanceTime() / 60));
							a_info.set_season_num(1);
							noti.add_attendance_info(a_info);
							if (status == 1) {
								break;
							}
						}
					} else {
						ATTENDANCE_GROUP_DECOY d_info = ATTENDANCE_GROUP_DECOY.newInstance();
						d_info.set_group_id(info.get_groupType().toInt());
						d_info.set_attendance_id(1);
						d_info.set_season_num(1);
						d_info.set_playtimeminute((info.get_attendanceTime() / 60));
						d_info.set_status(1);
						noti.add_attendance_decoy(d_info);
					}
					break;
				}
				case BRAVERY_MEDAL: {
					UserAttendanceDataGroup info = userData.get_groups().get(gType.toInt());
					int totalBonusCount = gType.getBonusSize();
					if (pc.getAccount().getAttendance_Bravery_Medal()) {
						for (int i = 0; i < totalBonusCount; i++) {
							ATTENDANCE_INFO a_info = ATTENDANCE_INFO.newInstance();
							UserAttendanceDataExtend attendance_data = info
									.get_groupData().get(i);
							UserAttendanceState state = attendance_data.get_state();
							int status;
							switch (state) {
								case COMPLETE:
									status = 2;
									break;
								case CLEAR:
									status = 3;
									break;
								default:
									status = 1;
							}
							a_info.set_attendance_id(i + 1);
							a_info.set_group_id(info.get_groupType().toInt());
							a_info.set_status(status);
							a_info.set_playtimeminute((info.get_attendanceTime() / 60));
							a_info.set_season_num(1);
							noti.add_attendance_info(a_info);
							if (status == 1) {
								break;
							}
						}
					} else {
						ATTENDANCE_GROUP_DECOY d_info = ATTENDANCE_GROUP_DECOY.newInstance();
						d_info.set_group_id(info.get_groupType().toInt());
						d_info.set_attendance_id(1);
						d_info.set_season_num(1);
						d_info.set_playtimeminute((info.get_attendanceTime() / 60));
						d_info.set_status(1);
						noti.add_attendance_decoy(d_info);
					}
					break;
				}
				default:
					break;
			}
		}

		noti.set_title_str(4380); // 출석체크 이벤트
		// noti.set_title_str(6198); //출석체크
		pc.sendPackets(noti.writeTo(MJEProtoMessages.SC_ATTENDANCE_INFO_NOTI), true);
	}

	private java.util.LinkedList<ATTENDANCE_GROUP_INFO> _group_info;
	private java.util.LinkedList<ATTENDANCE_INFO> _attendance_info;
	private java.util.LinkedList<ATTENDANCE_GROUP_DECOY> _attendance_decoy;
	private int _title_str;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ATTENDANCE_INFO_NOTI() {
	}

	public java.util.LinkedList<ATTENDANCE_GROUP_INFO> get_group_info() {
		return _group_info;
	}

	public void add_group_info(ATTENDANCE_GROUP_INFO val) {
		if (!has_group_info()) {
			_group_info = new java.util.LinkedList<ATTENDANCE_GROUP_INFO>();
			_bit |= 0x1;
		}
		_group_info.add(val);
	}

	public boolean has_group_info() {
		return (_bit & 0x1) == 0x1;
	}

	public java.util.LinkedList<ATTENDANCE_INFO> get_attendance_info() {
		return _attendance_info;
	}

	public void add_attendance_info(ATTENDANCE_INFO val) {
		if (!has_attendance_info()) {
			_attendance_info = new java.util.LinkedList<ATTENDANCE_INFO>();
			_bit |= 0x2;
		}
		_attendance_info.add(val);
	}

	public boolean has_attendance_info() {
		return (_bit & 0x2) == 0x2;
	}

	public java.util.LinkedList<ATTENDANCE_GROUP_DECOY> get_attendance_decoy() {
		return _attendance_decoy;
	}

	public void add_attendance_decoy(ATTENDANCE_GROUP_DECOY val) {
		if (!has_attendance_decoy()) {
			_attendance_decoy = new java.util.LinkedList<ATTENDANCE_GROUP_DECOY>();
			_bit |= 0x4;
		}
		_attendance_decoy.add(val);
	}

	public boolean has_attendance_decoy() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_title_str() {
		return _title_str;
	}

	public void set_title_str(int val) {
		_bit |= 0x8;
		_title_str = val;
		// System.out.println(val);
	}

	public boolean has_title_str() {
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
		if (has_group_info()) {
			for (ATTENDANCE_GROUP_INFO val : _group_info)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
						.computeMessageSize(1, val);
		}
		if (has_attendance_info()) {
			for (ATTENDANCE_INFO val : _attendance_info)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
						.computeMessageSize(2, val);
		}
		if (has_attendance_decoy()) {
			for (ATTENDANCE_GROUP_DECOY val : _attendance_decoy) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
						.computeMessageSize(3, val);
			}
		}
		if (has_title_str()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.computeUInt32Size(4, _title_str);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_group_info()) {
			for (ATTENDANCE_GROUP_INFO val : _group_info) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_attendance_info()) {
			for (ATTENDANCE_INFO val : _attendance_info) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_attendance_decoy()) {
			for (ATTENDANCE_GROUP_DECOY val : _attendance_decoy) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output)
			throws IOException {
		if (has_group_info()) {
			for (ATTENDANCE_GROUP_INFO val : _group_info) {
				output.writeMessage(1, val);
			}
		}
		if (has_attendance_info()) {
			for (ATTENDANCE_INFO val : _attendance_info) {
				output.writeMessage(2, val);
			}
		}
		if (has_attendance_decoy()) {
			for (ATTENDANCE_GROUP_DECOY val : _attendance_decoy) {
				output.writeMessage(3, val);
			}
		}
		if (has_title_str()) {
			output.writeUInt32(4, _title_str);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize()
						+ WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public MJIProtoMessage readFrom(
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input)
			throws IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				default: {
					return this;
				}
				case 0x0000000A: {
					add_group_info((ATTENDANCE_GROUP_INFO) input
							.readMessage(ATTENDANCE_GROUP_INFO.newInstance()));
					break;
				}
				case 0x00000012: {
					add_attendance_info((ATTENDANCE_INFO) input
							.readMessage(ATTENDANCE_INFO.newInstance()));
					break;
				}
				case 0x0000001A: {
					add_attendance_decoy((ATTENDANCE_GROUP_DECOY) input
							.readMessage(ATTENDANCE_GROUP_DECOY.newInstance()));
					break;
				}
				case 0x00000020: {
					set_title_str(input.readUInt32());
					break;
				}
			}
		}
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
			byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(
						bytes,
						l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			// System.out.println(_group_info);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_ATTENDANCE_INFO_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_group_info()) {
			for (ATTENDANCE_GROUP_INFO val : _group_info)
				val.dispose();
			_group_info.clear();
			_group_info = null;
		}
		if (has_attendance_info()) {
			for (ATTENDANCE_INFO val : _attendance_info)
				val.dispose();
			_attendance_info.clear();
			_attendance_info = null;
		}
		if (has_attendance_decoy()) {
			for (ATTENDANCE_GROUP_DECOY val : _attendance_decoy)
				val.dispose();
			_attendance_decoy.clear();
			_attendance_decoy = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
