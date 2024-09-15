package l1j.server.MJAttendanceSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJPropertyReader;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Exceptions.MJCommandArgsIndexException;
import l1j.server.MJTemplate.MJProto.MainServer_Client.AttendanceGroupType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_GROUP_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_INFO_EXTEND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.UserAttendanceDataGroup;
import l1j.server.MJTemplate.MJProto.MainServer_Client.UserAttendanceTimeStatus;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.utils.SQLUtil;

public class MJAttendanceLoadManager implements MJCommand{
	public static MJAttendanceLoadManager _instance;
	public static MJAttendanceLoadManager getInstance(){
		if(_instance == null)
			_instance = new MJAttendanceLoadManager();
		return _instance;
	}

	public static void loadAttendanceStartupCalendar(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from attendance_startup");
			rs = pstm.executeQuery();
			if(rs.next()){
				Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
				ATTEN_STARTUP_CALENDAR = (Calendar)cal.clone();
				ATTEN_STARTUP_CALENDAR.setTimeInMillis(rs.getTimestamp("startup_info").getTime());

				long diff = (cal.getTimeInMillis() - ATTEN_STARTUP_CALENDAR.getTimeInMillis()) / 1000;
				if(diff > ATTEN_RESET_PERIOD_SECOND)
					updateServerStartupInfo();
				cal.clear();
			}else{
				updateServerStartupInfo();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}

	public static void updateServerStartupInfo(){
		Connection con			= null;
		PreparedStatement pstm 	= null;
		Calendar old = ATTEN_STARTUP_CALENDAR;
		ATTEN_STARTUP_CALENDAR = RealTimeClock.getInstance().getRealTimeCalendar();
		Timestamp ts = new Timestamp(ATTEN_STARTUP_CALENDAR.getTimeInMillis());
		try{
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("insert into attendance_startup set id=1, startup_info=? on duplicate key update startup_info=?");
			pstm.setTimestamp(1, ts);
			pstm.setTimestamp(2, ts);
			pstm.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
		if(old != null){
			old.clear();
			old = null;
		}
	}

	private MJAttendanceLoadManager(){
	}

	public static Calendar ATTEN_STARTUP_CALENDAR;
	public static boolean ATTEN_IS_RUNNING;
	public static int ATTEN_CHECK_INTERVAL;
	public static int ATTEN_RESET_PERIOD_SECOND;
	public static int ATTEN_DAILY_MAX_COUNT;
	public static int ATTEN_WEEKEND_MAX_COUNT;
	public static int ATTEN_TOTAL_TIMESECOND;

	public static boolean ATTEN_PREMIUM_USE;
	public static int ATTEN_PREMIUM_NEEDITEMID;
	public static int ATTEN_PREMIUM_NEEDITEMCOUNT;

	public static boolean ATTEN_SPECIAL_USE;
	public static int ATTEN_SPECIAL_NEEDITEMID;
	public static int ATTEN_SPECIAL_NEEDITEMCOUNT;

	public static boolean ATTEN_PC_IS_RUNNING;

	public static boolean ATTEN_BRAVE_WARRIOR_USE;
	public static int ATTEN_BRAVE_WARRIOR_NEEDITEMID;
	public static int ATTEN_BRAVE_WARRIOR_NEEDITEMCOUNT;

	public static boolean ATTEN_ADEN_WORLD_USE;
	public static int ATTEN_ADEN_WORLD_NEEDITEMID;
	public static int ATTEN_ADEN_WORLD_NEEDITEMCOUNT;

	public static boolean ATTEN_BRAVERY_MEDAL_USE;
	public static int ATTEN_BRAVERY_MEDAL_NEEDITEMID;
	public static int ATTEN_BRAVERY_MEDAL_NEEDITEMCOUNT;


	public void load(){
		loadConfig();
		loadBonusGroup();
		loadAttendanceStartupCalendar();
		if (ATTEN_IS_RUNNING) {
			MJAttendanceScheduler.getInstance().run();
		}
	}

	public void loadBonusGroup(){
		SC_ATTENDANCE_BONUS_GROUP_INFO.load();
	}

	public void loadConfig() {
		MJPropertyReader reader = new MJPropertyReader("./config/mj_attendance.properties");
		ATTEN_IS_RUNNING = reader.readBoolean("AttenIsRunning", "true");
		if (ATTEN_IS_RUNNING) {
			ATTEN_PC_IS_RUNNING = reader.readBoolean("AttenPcIsRunning", "true");
			ATTEN_CHECK_INTERVAL = reader.readInt("checkInterval", "1800");
			ATTEN_RESET_PERIOD_SECOND = reader.readInt("resetPeriodSecond", "86400");
			ATTEN_DAILY_MAX_COUNT = reader.readInt("dailyMaxCount", "1");
			ATTEN_WEEKEND_MAX_COUNT = reader.readInt("weekendMaxCount", "1");
			ATTEN_TOTAL_TIMESECOND = reader.readInt("totalAttendanceTimeSecond", "3600");


			ATTEN_PREMIUM_USE = reader.readBoolean("AttenPremiumRunning", "true");
			ATTEN_PREMIUM_NEEDITEMID = reader.readInt("AttenPremiumItemid", "40308");
			ATTEN_PREMIUM_NEEDITEMCOUNT = reader.readInt("AttenPremiumNeedCount", "5000000");


			ATTEN_SPECIAL_USE = reader.readBoolean("AttenSpecialRunning", "true");
			ATTEN_SPECIAL_NEEDITEMID = reader.readInt("AttenSpecialItemid", "40308");
			ATTEN_SPECIAL_NEEDITEMCOUNT = reader.readInt("AttenSpecialCount", "5000000");

			ATTEN_BRAVE_WARRIOR_USE = reader.readBoolean("AttenBraveWarriorRunning", "true");
			ATTEN_BRAVE_WARRIOR_NEEDITEMID = reader.readInt("AttenBraveWarriorItemid", "40308");
			ATTEN_BRAVE_WARRIOR_NEEDITEMCOUNT = reader.readInt("AttenBraveWarriorCount", "5000000");

			ATTEN_ADEN_WORLD_USE = reader.readBoolean("AttenAdenWorldRunning", "true");
			ATTEN_ADEN_WORLD_NEEDITEMID = reader.readInt("AttenAdenWorldItemid", "40308");
			ATTEN_ADEN_WORLD_NEEDITEMCOUNT = reader.readInt("AttenAdenWorldCount", "5000000");

			ATTEN_BRAVERY_MEDAL_USE = reader.readBoolean("AttenBraveryMedalRunning", "true");
			ATTEN_BRAVERY_MEDAL_NEEDITEMID = reader.readInt("AttenBraveryMedalItemid", "40308");
			ATTEN_BRAVERY_MEDAL_NEEDITEMCOUNT = reader.readInt("AttenBraveryMedalCount", "5000000");
		}
		reader.dispose();
		SC_ATTENDANCE_BONUS_INFO_EXTEND.load();
		SC_ATTENDANCE_BONUS_INFO_EXTEND.load1();
	}

	@Override
	public void execute(MJCommandArgs args) {
		try {
			switch(args.nextInt()) {
				case 1:
					execReload(args);
					break;

				case 2:
					execCharacterCommand(args);
					break;
			}
		}catch(Exception e) {
			args.notify("出席檢查 [1.重載][2.角色設定]");
		}finally {
			args.dispose();
			args = null;
		}
	}

	private void execReload(MJCommandArgs args) {
		try {
			switch(args.nextInt()) {
				case 1:
					loadConfig();
					args.notify("出席檢查配置重載已完成。");
					break;
				case 2:
					loadBonusGroup();
					args.notify("出席檢查物品表重載已完成。");
					break;
			}
		}catch(Exception e) {
			args.notify("出席檢查 1");
			args.notify("[1. 配置][2. 物品數據庫]");
		}
	}

	private L1PcInstance findPc(MJCommandArgs args) throws MJCommandArgsIndexException {
		String name = args.nextString();
		L1PcInstance pc = L1World.getInstance().findpc(name);
		if(pc == null)
			args.notify(String.format("無法找到 %s 您。", name));
		else if(pc.getAttendanceData() == null)
			args.notify(String.format("無法找到 %s 您的出席檢查資訊。", name));
		else
			return pc;
		return null;
	}

	private void execCharacterCommand(MJCommandArgs args){
		try{
			switch(args.nextInt()){
				case 1:{
					L1PcInstance pc = findPc(args);
					if(pc == null)
						return;

					SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
					for(UserAttendanceDataGroup dGroup : userData.get_groups()){
						args.notify(String.format("%s您的 [%s]資訊 - 狀態 : %s, 累積時間 : %d, 當前索引 : %d, 當前類型的累積獎勵 : %d",
								pc.getName(),
								dGroup.get_groupType().name(),
								dGroup.get_resultCode().name(),
								dGroup.get_attendanceTime(),
								dGroup.get_currentAttendanceIndex(),
								dGroup.get_recvedRewardCount())
						);
					}
					break;
				}

				case 2:{
					L1PcInstance pc = findPc(args);
					if(pc == null)
						return;

					SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
					for(UserAttendanceDataGroup dGroup : userData.get_groups()){
						int index = dGroup.get_currentAttendanceIndex();
						dGroup.get_groupData().get(index).set_time(0);
						dGroup.set_currentAttendanceIndex(Math.max(0, dGroup.get_currentAttendanceIndex() - 1));
						dGroup.set_attendanceTime(0);
						if(dGroup.get_groupType().equals(AttendanceGroupType.NORMAL)){
							dGroup.set_resultCode(UserAttendanceTimeStatus.ATTENDANCE_NORMAL);
						}else{
							dGroup.set_resultCode(pc.getAccount().getBuff_PC_Bang() != null ? UserAttendanceTimeStatus.ATTENDANCE_NORMAL : UserAttendanceTimeStatus.ATTENDANCE_CANT_BE_ACHIEVED_TIME);
//						dGroup.set_resultCode(pc.hasSkillEffect(L1SkillId.PC_CAFE) ? UserAttendanceTimeStatus.ATTENDANCE_NORMAL : UserAttendanceTimeStatus.ATTENDANCE_CANT_BE_ACHIEVE_TIME);
						}
						SC_ATTENDANCE_BONUS_INFO_EXTEND.send(pc);
						SC_ATTENDANCE_BONUS_GROUP_INFO.send(pc);
						SC_ATTENDANCE_USER_DATA_EXTEND.send(pc);
					}
					break;
				}
				case 3:{
					args.notify("稍後所有出席檢查信息將被重置。");
					MJAttendanceScheduler.getInstance().dispose();
					GeneralThreadPool.getInstance().schedule(new Runnable(){
						@Override
						public void run(){
							L1World.getInstance().getAllPlayerStream()
									.filter((L1PcInstance pc) -> pc != null && pc.getAttendanceData() != null)
									.forEach((L1PcInstance pc) ->{
										try{
											SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
											pc.setAttendanceData(null);
											userData.dispose();
										}catch(Exception e){
											e.printStackTrace();
										}
									});

							Connection con = null;
							PreparedStatement pstm = null;
							try{
								con = L1DatabaseFactory.getInstance().getConnection();
								pstm = con.prepareStatement("truncate table attendance_startup");
								pstm.executeUpdate();
								SQLUtil.close(pstm);
								pstm = con.prepareStatement("truncate table attendance_userinfo");
								pstm.executeUpdate();
							}catch(Exception e){
								e.printStackTrace();
							}finally{
								SQLUtil.close(pstm, con);
							}
							try{
								load();
								L1World.getInstance().getAllPlayerStream()
										.filter((L1PcInstance pc) -> pc != null && pc.getAI() == null && !pc.isAutomatedStore())
										.forEach((L1PcInstance pc) ->{
											try{
												SC_ATTENDANCE_BONUS_INFO_EXTEND.send(pc);
												SC_ATTENDANCE_BONUS_GROUP_INFO.send(pc);
												SC_ATTENDANCE_USER_DATA_EXTEND.send(pc);
											}catch(Exception e){
												e.printStackTrace();
											}
										});

							}catch(Exception e){}
							return new L1PcInstance[0];
						}
					}, 2000L);
					break;
				}
			}

		}catch(Exception e){
			args.notify(".出席檢查 2");
			args.notify("[1. 查詢角色][2. 重置累計時間][3. 全部重置]");
		}
	}
}
