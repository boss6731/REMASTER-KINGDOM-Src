package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.Account;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ATTENDANCE_BONUS_GROUP_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send(L1PcInstance pc) {
		AttendanceGroupType.NORMAL.send(pc);
		if (MJAttendanceLoadManager.ATTEN_PC_IS_RUNNING) {
			AttendanceGroupType.PC_CAFE.send(pc);
		}
		if (MJAttendanceLoadManager.ATTEN_PREMIUM_USE) {
			AttendanceGroupType.PREMIUM.send(pc);
		}
		if (MJAttendanceLoadManager.ATTEN_SPECIAL_USE) {
			AttendanceGroupType.SPECIAL.send(pc);
		}
		if (MJAttendanceLoadManager.ATTEN_BRAVE_WARRIOR_USE) {
			AttendanceGroupType.BRAVE_WARRIOR.send(pc);
		}
		if (MJAttendanceLoadManager.ATTEN_ADEN_WORLD_USE) {
			AttendanceGroupType.ADEN_WORLD.send(pc);
		}
		if (MJAttendanceLoadManager.ATTEN_BRAVERY_MEDAL_USE) {
			AttendanceGroupType.BRAVERY_MEDAL.send(pc);
		}
	}

	public static void load() {
		SC_ATTENDANCE_BONUS_GROUP_INFO gInfo = newInstance();
		createAttendanceBonusGroup(gInfo, "attendance_item");
		AttendanceGroupType.NORMAL.setGroupInfo(gInfo);

		gInfo = newInstance();
		createAttendanceBonusGroup(gInfo, "attendance_item_pc");
		AttendanceGroupType.PC_CAFE.setGroupInfo(gInfo);

		gInfo = newInstance();
		createAttendanceBonusGroup(gInfo, "attendance_item_premium");
		AttendanceGroupType.PREMIUM.setGroupInfo(gInfo);

		gInfo = newInstance();
		createAttendanceBonusGroup(gInfo, "attendance_item_special");
		AttendanceGroupType.SPECIAL.setGroupInfo(gInfo);

		gInfo = newInstance();
		createAttendanceBonusGroup(gInfo, "attendance_item_brave_warrior");
		AttendanceGroupType.BRAVE_WARRIOR.setGroupInfo(gInfo);

		gInfo = newInstance();
		createAttendanceBonusGroup(gInfo, "attendance_item_aden_world");
		AttendanceGroupType.ADEN_WORLD.setGroupInfo(gInfo);

		gInfo = newInstance();
		createAttendanceBonusGroup(gInfo, "attendance_item_bravery_medal");
		AttendanceGroupType.BRAVERY_MEDAL.setGroupInfo(gInfo);
	}

	public static void openinfo(L1PcInstance pc) {
		Account account = pc.getAccount();
		if (MJAttendanceLoadManager.ATTEN_PREMIUM_USE) {
			L1Item item = ItemTable.getInstance().getTemplate(MJAttendanceLoadManager.ATTEN_PREMIUM_NEEDITEMID);
			if (item == null) {
				return;
			}
			if (!account.getAttendance_Premium()) {
				pc.sendPackets(SC_ATTENDANCE_BONUS_GROUP_INFO.make_stream(pc, AttendanceGroupType.PREMIUM,
						item.getItemDescId(), MJAttendanceLoadManager.ATTEN_PREMIUM_NEEDITEMCOUNT, 0), true);
			}
		}
		if (MJAttendanceLoadManager.ATTEN_SPECIAL_USE) {
			L1Item item = ItemTable.getInstance().getTemplate(MJAttendanceLoadManager.ATTEN_SPECIAL_NEEDITEMID);
			if (item == null) {
				return;
			}
			if (!account.getAttendance_Special()) {
				pc.sendPackets(SC_ATTENDANCE_BONUS_GROUP_INFO.make_stream(pc, AttendanceGroupType.SPECIAL,
						item.getItemDescId(), MJAttendanceLoadManager.ATTEN_SPECIAL_NEEDITEMCOUNT, 0), true);
			}
		}
		if (MJAttendanceLoadManager.ATTEN_BRAVE_WARRIOR_USE) {
			L1Item item = ItemTable.getInstance().getTemplate(MJAttendanceLoadManager.ATTEN_BRAVE_WARRIOR_NEEDITEMID);
			if (item == null) {
				return;
			}
			if (!account.getAttendance_Brave_Warrior()) {
				pc.sendPackets(
						SC_ATTENDANCE_BONUS_GROUP_INFO.make_stream(pc, AttendanceGroupType.BRAVE_WARRIOR,
								item.getItemDescId(), MJAttendanceLoadManager.ATTEN_BRAVE_WARRIOR_NEEDITEMCOUNT, 1),
						true);
			}
		}
		if (MJAttendanceLoadManager.ATTEN_ADEN_WORLD_USE) {
			L1Item item = ItemTable.getInstance().getTemplate(MJAttendanceLoadManager.ATTEN_ADEN_WORLD_NEEDITEMID);
			if (item == null) {
				return;
			}
			if (!account.getAttendance_Aden_World()) {
				pc.sendPackets(SC_ATTENDANCE_BONUS_GROUP_INFO.make_stream(pc, AttendanceGroupType.ADEN_WORLD,
						item.getItemDescId(), MJAttendanceLoadManager.ATTEN_ADEN_WORLD_NEEDITEMCOUNT, 1), true);
			}
		}
		if (MJAttendanceLoadManager.ATTEN_BRAVERY_MEDAL_USE) {
			L1Item item = ItemTable.getInstance().getTemplate(MJAttendanceLoadManager.ATTEN_BRAVERY_MEDAL_NEEDITEMID);
			if (item == null) {
				return;
			}
			if (!account.getAttendance_Bravery_Medal()) {
				pc.sendPackets(
						SC_ATTENDANCE_BONUS_GROUP_INFO.make_stream(pc, AttendanceGroupType.BRAVERY_MEDAL,
								item.getItemDescId(), MJAttendanceLoadManager.ATTEN_BRAVERY_MEDAL_NEEDITEMCOUNT, 1),
						true);
			}
		}
	}

	public static ProtoOutputStream make_stream(L1PcInstance pc, AttendanceGroupType type, int descid, int costcount,
			int i) {
		SC_ATTENDANCE_BONUS_GROUP_INFO ginfo = newInstance();
		ginfo.set_groupType(type);
		ginfo.set_randomGroup(AttendanceBonusRandomGroup.opencostinfo(descid, costcount));
		ginfo.set_seasonNumber(i);
		ProtoOutputStream stream = ginfo.writeTo(MJEProtoMessages.SC_ATTENDANCE_BONUS_GROUP_INFO);
		ginfo.dispose();
		return stream;
	}

	private static void createAttendanceBonusGroup(SC_ATTENDANCE_BONUS_GROUP_INFO gInfo, String table) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(String.format("SELECT * FROM %s ORDER BY id ASC", table));
			rs = pstm.executeQuery();
			gInfo.set_bonusGroup(new ArrayList<AttendanceBonusGroup>());
			// System.out.println(table);
			switch (table) {
				case "attendance_item":
				case "attendance_item_pc":
				case "attendance_item_premium":
				case "attendance_item_special":
					gInfo.set_seasonNumber(0);
					break;
				case "attendance_item_brave_warrior":
				case "attendance_item_aden_world":
				case "attendance_item_bravery_medal":
					gInfo.set_seasonNumber(1);
					break;
			}
			ArrayList<AttendanceBonusGroup> groups = gInfo.get_bonusGroup();
			while (rs.next()) {
				int attendance_index = rs.getInt("id");
				AttendanceBonusInfo bInfo = AttendanceBonusInfo.newInstance(rs);
				AttendanceBonusGroup group = null;
				if (attendance_index <= groups.size()) {
					group = groups.get(attendance_index - 1);
				} else {
					group = AttendanceBonusGroup.newInstance(attendance_index);
					groups.add(group);
				}
				group.add_bonuses(bInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public static SC_ATTENDANCE_BONUS_GROUP_INFO newInstance() {
		return new SC_ATTENDANCE_BONUS_GROUP_INFO();
	}

	private AttendanceGroupType _groupType;
	private ArrayList<AttendanceBonusGroup> _bonusGroup;
	private AttendanceBonusRandomGroup _randomGroup;
	private int _seasonNumber;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ATTENDANCE_BONUS_GROUP_INFO() {
	}

	public AttendanceGroupType get_groupType() {
		return _groupType;
	}

	public void set_groupType(AttendanceGroupType val) {
		_bit |= 0x01;
		_groupType = val;
	}

	public boolean has_groupType() {
		return (_bit & 0x1) == 0x1;
	}

	public ArrayList<AttendanceBonusGroup> get_bonusGroup() {
		return _bonusGroup;
	}

	public void add_bonusGroup(AttendanceBonusGroup val) {
		if (!has_bonusGroup()) {
			_bonusGroup = new ArrayList<AttendanceBonusGroup>(32);
			_bit |= 0x2;
		}
		_bonusGroup.add(val);
	}

	public void set_bonusGroup(ArrayList<AttendanceBonusGroup> groups) {
		_bonusGroup = groups;
		_bit |= 0x00000002;
	}

	public boolean has_bonusGroup() {
		return (_bit & 0x2) == 0x2;
	}

	public AttendanceBonusRandomGroup get_randomGroup() {
		return _randomGroup;
	}

	public void set_randomGroup(AttendanceBonusRandomGroup val) {
		_bit |= 0x4;
		_randomGroup = val;
	}

	public boolean has_randomGroup() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_seasonNumber() {
		return _seasonNumber;
	}

	public void set_seasonNumber(int val) {
		_bit |= 0x8;
		_seasonNumber = val;
	}

	public boolean has_seasonNumber() {
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
		if (has_groupType()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _groupType.toInt());
		}
		if (has_bonusGroup()) {
			for (AttendanceBonusGroup val : _bonusGroup) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		if (has_randomGroup()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _randomGroup);
		}
		if (has_seasonNumber()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _seasonNumber);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_groupType()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_bonusGroup()) {
			for (AttendanceBonusGroup val : _bonusGroup) {
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
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_groupType()) {
			output.writeEnum(1, _groupType.toInt());
		}
		if (has_bonusGroup()) {
			for (AttendanceBonusGroup val : _bonusGroup) {
				output.writeMessage(2, val);
			}
		}
		if (has_randomGroup()) {
			output.writeMessage(3, _randomGroup);
		}
		if (has_seasonNumber()) {
			output.writeUInt32(4, _seasonNumber);
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

				case 0x00000008: {
					set_groupType(AttendanceGroupType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012: {
					add_bonusGroup((AttendanceBonusGroup) input.readMessage(AttendanceBonusGroup.newInstance()));
					break;
				}
				case 0x0000001A: {
					set_randomGroup(
							(AttendanceBonusRandomGroup) input.readMessage(AttendanceBonusRandomGroup.newInstance()));
					break;
				}
				case 0x00000020: {
					set_seasonNumber(input.readUInt32());
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
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_ATTENDANCE_BONUS_GROUP_INFO();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_bonusGroup()) {
			for (AttendanceBonusGroup val : _bonusGroup)
				val.dispose();
			_bonusGroup.clear();
			_bonusGroup = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
