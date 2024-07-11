package l1j.server.MJAttendanceSystem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_REWARD_ITEM_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_REWARD_ITEM_NOTI.ATTENDANCE_REWARD_ITEM;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJAttendanceRewardsHistory {
	public static void send_history(final L1PcInstance pc) {
		final SC_ATTENDANCE_REWARD_ITEM_NOTI noti = SC_ATTENDANCE_REWARD_ITEM_NOTI.newInstance();
		Selector.exec("select attendance_id,group_id,reward_desc_id,reward_item_count from attendance_rewards_history where object_id=? and is_probability_rewards=1", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}
			
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJAttendanceRewardsHistory o = newInstance(rs);
					ATTENDANCE_REWARD_ITEM reward_item = ATTENDANCE_REWARD_ITEM.newInstance();
					reward_item.set_attendance_id(o.get_attendance_id());
					reward_item.set_group_id(o.get_group_id());
					reward_item.set_item_name_id(o.get_reward_desc_id());
					reward_item.set_item_count(o.get_reward_item_count());
					noti.add_reward_item_info(reward_item);
				}
			}
		});
		if(noti.get_reward_item_info() != null && noti.get_reward_item_info().size() > 0) {
			pc.sendPackets(noti, MJEProtoMessages.SC_ATTENDANCE_REWARD_ITEM_NOTI, true);
		}
	}
	
	public static void DeleteHistory(L1PcInstance pc, int groupid){
		Updator.exec("delete from attendance_rewards_history where object_id=? and group_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, pc.getId());
				pstm.setInt(++idx, groupid);
			}
		});
	}


	private static MJAttendanceRewardsHistory newInstance(ResultSet rs) throws SQLException{
		return newInstance()
				.set_attendance_id(rs.getInt("attendance_id"))
				.set_group_id(rs.getInt("group_id"))
				.set_reward_desc_id(rs.getInt("reward_desc_id"))
				.set_reward_item_count(rs.getInt("reward_item_count"))
				.set_season_num(rs.getInt("season_num"));
	}

	public static MJAttendanceRewardsHistory newInstance(){
		return new MJAttendanceRewardsHistory();
	}

	private int m_season_num;
	private int m_object_id;
	private String m_character_name;
	private int m_attendance_id;
	private int m_group_id;
	private String m_reward_item_name;
	private int m_reward_desc_id;
	private int m_reward_item_count;
	private boolean m_is_probability_rewards;
	private MJAttendanceRewardsHistory(){}

	public MJAttendanceRewardsHistory set_season_num(int season_num){
		m_season_num = season_num;
		return this;
	}
	public int get_season_num(){
		return m_season_num;
	}
	
	public MJAttendanceRewardsHistory set_object_id(int object_id){
		m_object_id = object_id;
		return this;
	}
	public MJAttendanceRewardsHistory set_character_name(String character_name){
		m_character_name = character_name;
		return this;
	}
	public MJAttendanceRewardsHistory set_attendance_id(int attendance_id){
		m_attendance_id = attendance_id;
		return this;
	}
	public MJAttendanceRewardsHistory set_group_id(int group_id){
		m_group_id = group_id;
		return this;
	}
	public MJAttendanceRewardsHistory set_reward_item_name(String reward_item_name) {
		m_reward_item_name = reward_item_name;
		return this;
	}
	public MJAttendanceRewardsHistory set_reward_desc_id(int reward_desc_id){
		m_reward_desc_id = reward_desc_id;
		return this;
	}
	public MJAttendanceRewardsHistory set_reward_item_count(int reward_item_count){
		m_reward_item_count = reward_item_count;
		return this;
	}
	public MJAttendanceRewardsHistory set_is_probability_rewards(boolean is_probability_rewards){
		m_is_probability_rewards = is_probability_rewards;
		return this;
	}
	public int get_object_id(){
		return m_object_id;
	}
	public String get_character_name(){
		return m_character_name;
	}
	public int get_attendance_id(){
		return m_attendance_id;
	}
	public int get_group_id(){
		return m_group_id;
	}
	public String get_reward_item_name() {
		return m_reward_item_name;
	}
	public int get_reward_desc_id(){
		return m_reward_desc_id;
	}
	public int get_reward_item_count(){
		return m_reward_item_count;
	}
	public boolean get_is_probability_rewards(){
		return m_is_probability_rewards;
	}
	
	public void do_update() {
		Updator.exec("insert ignore into attendance_rewards_history set "
				+ "object_id=?, "
				+ "character_name=?, "
				+ "attendance_id=?, "
				+ "group_id=?, "
				+ "reward_item_name=?, "
				+ "reward_desc_id=?, "
				+ "reward_item_count=?, "
				+ "is_probability_rewards=?, "
				+ "season_num=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, get_object_id());
						pstm.setString(++idx, get_character_name());
						pstm.setInt(++idx, get_attendance_id());
						pstm.setInt(++idx, get_group_id());
						pstm.setString(++idx, get_reward_item_name());
						pstm.setInt(++idx, get_reward_desc_id());
						pstm.setInt(++idx, get_reward_item_count());
						pstm.setInt(++idx, get_is_probability_rewards() ? 1 : 0);
						pstm.setInt(++idx, get_season_num());
					}
				});
	}
}
