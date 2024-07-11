package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import MJShiftObject.Battle.MJShiftBattlePlayManager;
import l1j.server.MJAttendanceSystem.MJAttendanceRewardExecutor;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ATTENDANCE_REWARD_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {

	public static CS_ATTENDANCE_REWARD_REQ newInstance() {
		return new CS_ATTENDANCE_REWARD_REQ();
	}

	private int _attendance_id;
	private int _group_id;
	private int _season_num;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_ATTENDANCE_REWARD_REQ() {
	}

	public int get_attendance_id() {
		return _attendance_id;
	}

	public void set_attendance_id(int val) {
		_bit |= 0x1;
		_attendance_id = val;
	}

	public boolean has_attendance_id() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_group_id() {
		return _group_id;
	}

	public void set_group_id(int val) {
		_bit |= 0x2;
		_group_id = val;
	}

	public boolean has_group_id() {
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
		if (has_attendance_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _attendance_id);
		}
		if (has_group_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _group_id);
		}
		if (has_season_num()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _season_num);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_attendance_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_group_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_attendance_id()) {
			output.wirteInt32(1, _attendance_id);
		}
		if (has_group_id()) {
			output.wirteInt32(2, _group_id);
		}
		if (has_season_num()) {
			output.writeUInt32(3, _season_num);
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
					set_attendance_id(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_group_id(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_season_num(input.readUInt32());
					break;
				}
			}
		}
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);
			if (!isInitialized())
				return this;
			
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			final L1PcInstance pc = clnt.getActiveChar();
			if(pc == null)
				return this;
			
			if(MJShiftBattlePlayManager.is_shift_battle(pc))
				return this;
			
			SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
			if(userData == null)
				return this;
			
			AttendanceGroupType egroup = AttendanceGroupType.fromInt(_group_id);
			if(egroup == null){
				System.out.println(String.format("[簽到] 無法識別的簽到群組！角色名稱：%s", pc.getName()));
				return this;
				}
				
				UserAttendanceDataGroup group = userData.get_groups().get(egroup.toInt());
				UserAttendanceTimeStatus timeStatus = group.get_resultCode();
				if (timeStatus.equals(UserAttendanceTimeStatus.ATTENDANCE_ALL_CLEAR)) {
				System.out.println(String.format("[簽到] 已經領取所有獎勵的用戶嘗試領取！角色名稱：%s，群組：%s", pc.getName(), egroup.name()));
				pc.sendPackets("您已經領取了所有獎勵。");
				return this;
				}
				
				final UserAttendanceDataExtend attendance_data = group.get_groupData().get(_attendance_id - 1);
				if (!attendance_data.get_state().equals(UserAttendanceState.COMPLETE)) {
				System.out.println(String.format("[簽到] 請求不符合狀態的獎勵！角色名稱：%s，群組：%s，索引：%d，狀態：%s", pc.getName(), egroup.name(), attendance_data.get_index(), attendance_data.get_state().name()));
				pc.sendPackets("無法領取獎勵。");
				return this;
				}
				final AttendanceBonusInfo bInfo = egroup.select_reward(_attendance_id - 1);
				if (bInfo == null) {
				System.out.println(String.format("[簽到] 無法找到獎勵物品！角色名稱：%s，群組：%s，索引：%d，狀態：%s", pc.getName(), egroup.name(), attendance_data.get_index(), attendance_data.get_state().name()));
				pc.sendPackets("無法領取獎勵。");
				return this;
				}
			}
			int seasonnum = 0;
			Account account = pc.getAccount();
			if (get_group_id() == 2) {
				seasonnum = 0;
				if (!account.getAttendance_Premium()) {
					pc.sendPackets("請先啟用高級簽到功能，再進行操作。");
					return this;
					}
					}
					if (get_group_id() == 3) {
					seasonnum = 0;
					if (!account.getAttendance_Special()) {
					pc.sendPackets("請先啟用特別簽到功能，再進行操作。");
					return this;
					}
					}
					if (get_group_id() == 4) {
					seasonnum = 1;
					if (!account.getAttendance_Brave_Warrior()) {
					pc.sendPackets("請先啟用勇猛簽到功能，再進行操作。");
					return this;
					}
					}
					if (get_group_id() == 5) {
					seasonnum = 1;
					if (!account.getAttendance_Aden_World()) {
					pc.sendPackets("請先啟用統治者簽到功能，再進行操作。");
					return this;
					}
					}
					if (get_group_id() == 6) {
					seasonnum = 1;
					if (!account.getAttendance_Bravery_Medal()) {
					pc.sendPackets("請先啟用勇猛簽到功能，再進行操作。");
					return this;
					}
					}

	
			
			MJAttendanceRewardExecutor.newInstance().set_pc(pc).set_attendance_index(_attendance_id).set_is_probability_reward(egroup.is_probability_reward(_attendance_id - 1)).set_group_type(egroup).set_bonus_info(bInfo).set_season_num(seasonnum).do_execute();
		}catch(

	Exception e){
			e.printStackTrace();
		}return this;
	}

	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_ATTENDANCE_REWARD_REQ();
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
