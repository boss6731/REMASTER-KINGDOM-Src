package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.server.Account;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.templates.L1Item;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_ATTENDANCE_TAB_OPEN_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_ATTENDANCE_TAB_OPEN_REQ newInstance() {
		return new CS_ATTENDANCE_TAB_OPEN_REQ();
	}

	private int _group_id;
	private int _season_num;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_ATTENDANCE_TAB_OPEN_REQ() {
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

	public int get_season_num() {
		return _season_num;
	}

	public void set_season_num(int val) {
		_bit |= 0x2;
		_season_num = val;
	}

	public boolean has_season_num() {
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
		if (has_group_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _group_id);
		}
		if (has_season_num()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _season_num);
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
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_group_id()) {
			output.wirteInt32(1, _group_id);
		}
		if (has_season_num()) {
			output.writeUInt32(2, _season_num);
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
					set_season_num(input.readUInt32());
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

			Account account = pc.getAccount();
			if (account.getAttendance_Premium()
					&& AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.PREMIUM) {
				return this;
			}
			if (account.getAttendance_Special()
					&& AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.SPECIAL) {
				return this;
			}
			if (account.getBuff_PC방() == null
					&& AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.PC_CAFE) {
				return this;
			}
			if (account.getAttendance_Brave_Warrior()
					&& AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.BRAVE_WARRIOR) {
				return this;
			}
			if (account.getAttendance_Aden_World()
					&& AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.ADEN_WORLD) {
				return this;
			}
			if (account.getAttendance_Bravery_Medal()
					&& AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.BRAVERY_MEDAL) {
				return this;
			}

			SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
			if (userData.get_groups() == null) {
				return this;
			}
			if (!MJAttendanceLoadManager.ATTEN_PREMIUM_USE
					&& AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.PREMIUM) {
				return this;
			}
			if (!MJAttendanceLoadManager.ATTEN_SPECIAL_USE
					&& AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.SPECIAL) {
				return this;
			}
			if (!MJAttendanceLoadManager.ATTEN_BRAVE_WARRIOR_USE
					&& AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.BRAVE_WARRIOR) {
				return this;
			}
			if (!MJAttendanceLoadManager.ATTEN_ADEN_WORLD_USE
					&& AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.ADEN_WORLD) {
				return this;
			}
			if (!MJAttendanceLoadManager.ATTEN_BRAVERY_MEDAL_USE
					&& AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.BRAVERY_MEDAL) {
				return this;
			}
			int itemid = 0;
			int count = 0;
			switch (_group_id) {
				case 2: // premium
					itemid = MJAttendanceLoadManager.ATTEN_PREMIUM_NEEDITEMID;
					count = MJAttendanceLoadManager.ATTEN_PREMIUM_NEEDITEMCOUNT;
					break;
				case 3: // special
					itemid = MJAttendanceLoadManager.ATTEN_SPECIAL_NEEDITEMID;
					count = MJAttendanceLoadManager.ATTEN_SPECIAL_NEEDITEMCOUNT;
					break;
				case 4: // brave_warrior
					itemid = MJAttendanceLoadManager.ATTEN_BRAVE_WARRIOR_NEEDITEMID;
					count = MJAttendanceLoadManager.ATTEN_BRAVE_WARRIOR_NEEDITEMCOUNT;
					break;
				case 5: // aden_world
					itemid = MJAttendanceLoadManager.ATTEN_ADEN_WORLD_NEEDITEMID;
					count = MJAttendanceLoadManager.ATTEN_ADEN_WORLD_NEEDITEMCOUNT;
					break;
				case 6: // bravery_medal
					itemid = MJAttendanceLoadManager.ATTEN_BRAVERY_MEDAL_NEEDITEMID;
					count = MJAttendanceLoadManager.ATTEN_BRAVERY_MEDAL_NEEDITEMCOUNT;
					break;
			}
			// System.out.println("아이템: "+itemid + ","+"수량: "+count);
			if (!pc.getInventory().checkItem(itemid, count)) {
				L1Item item = ItemTable.getInstance().getTemplate(itemid);
				pc.sendPackets(String.format("需要 %s 個 %s。", item.getName(), count));
				return this;
			} else {
				pc.getInventory().consumeItem(itemid, count);
			}
			int i = 0;
			switch (_group_id) {
				case 2:
					account.setAttendance_Premium(true);
					break;
				case 3:
					account.setAttendance_Special(true);
					break;
				case 4:
					account.setAttendance_Brave_Warrior(true);
					i = 1;
					break;
				case 5:
					account.setAttendance_Aden_World(true);
					i = 1;
					break;
				case 6:
					account.setAttendance_Bravery_Medal(true);
					i = 1;
					break;
				default:
					return this;
			}

			/*
			 * if(AttendanceGroupType.fromInt(_group_id) == AttendanceGroupType.PREMIUM) {
			 * account.setAttendance_Premium(true);
			 * } else if (AttendanceGroupType.fromInt(_group_id) ==
			 * AttendanceGroupType.SPECIAL) {
			 * account.setAttendance_Special(true);
			 * } else if (AttendanceGroupType.fromInt(_group_id) ==
			 * AttendanceGroupType.BRAVE_WARRIOR) {
			 * account.setAttendance_Brave_Warrior(true);
			 * } else if (AttendanceGroupType.fromInt(_group_id) ==
			 * AttendanceGroupType.ADEN_WORLD) {
			 * account.setAttendance_Aden_World(true);
			 * } else if (AttendanceGroupType.fromInt(_group_id) ==
			 * AttendanceGroupType.BRAVERY_MEDAL) {
			 * account.setAttendance_Bravery_Medal(true);
			 * }
			 */
			// userData.get_groups().get(_group_id).set_resultCode(UserAttendanceTimeStatus.ATTENDANCE_NORMAL);
			pc.setAttendanceData(userData);
			SC_ATTENDANCE_USER_DATA_EXTEND.send(pc);
			SC_ATTENDANCE_USER_DATA_EXTEND.store(pc);
			pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(_group_id), i),
					true);
			pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(_group_id), i),
					true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_ATTENDANCE_TAB_OPEN_REQ();
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
