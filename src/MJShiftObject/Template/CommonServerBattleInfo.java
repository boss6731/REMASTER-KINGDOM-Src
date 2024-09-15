package MJShiftObject.Template;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommonServerBattleInfo{
	public static CommonServerBattleInfo newInstance(ResultSet rs) throws SQLException{
		return newInstance()
				.set_server_identity(rs.getString("server_identity"))
				.set_start_millis(rs.getTimestamp("start_time").getTime())
				.set_ended_millis(rs.getTimestamp("ended_time").getTime())
				.set_current_kind(rs.getInt("current_kind"))
				.set_battle_name(rs.getString("battle_name"));
	}
	
	public static CommonServerBattleInfo newInstance(){
		return new CommonServerBattleInfo();
	}
	
	private String m_server_identity;
	private long m_start_millis;
	private long m_ended_millis;
	private int m_current_kind;
	private String m_battle_name;
	private CommonServerBattleInfo(){
	}
	
	public boolean is_run(){
		long current_millis = System.currentTimeMillis();
		return m_start_millis < current_millis && current_millis < m_ended_millis;
	}
	public boolean is_ended(){
		long current_millis = System.currentTimeMillis();
		return current_millis >= m_ended_millis;
	}
	public CommonServerBattleInfo set_server_identity(String server_identity){
		m_server_identity = server_identity;
		return this;
	}
	public CommonServerBattleInfo set_start_millis(long start_millis){
		m_start_millis = start_millis;
		return this;
	}
	public CommonServerBattleInfo set_ended_millis(long ended_millis){
		m_ended_millis = ended_millis;
		return this;
	}
	public CommonServerBattleInfo set_current_kind(int current_kind){
		m_current_kind = current_kind;
		return this;
	}
	public CommonServerBattleInfo set_battle_name(String battle_name){
		m_battle_name = battle_name;
		return this;
	}
	public String get_server_identity(){
		return m_server_identity;
	}
	public long get_start_millis(){
		return m_start_millis;
	}
	public long get_ended_millis(){
		return m_ended_millis;
	}
	public int get_current_kind(){
		return m_current_kind;
	}
	public String get_battle_name(){
		return m_battle_name;
	}
}
