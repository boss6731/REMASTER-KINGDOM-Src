package l1j.server.ClanBuffList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ClanBuffListInfo {
	static ClanBuffListInfo newInstance(ResultSet rs) throws SQLException {
		ClanBuffListInfo pInfo = newInstance();
		pInfo._clan_id = rs.getInt("clan_id");
		pInfo._clan_name = rs.getString("clan_name");
		pInfo._skill_id = rs.getInt("skill_id");
		pInfo._skill_name = rs.getString("skill_name");
		pInfo._remaining_time = rs.getTimestamp("remaining_time");
		return pInfo;
	}
	
	private static ClanBuffListInfo newInstance() {
		return new ClanBuffListInfo();
	}
	
	private int _clan_id;
	private String _clan_name;
	private int _skill_id;
	private String _skill_name;
	private Timestamp _remaining_time;

	public int get_clan_id() {
		return _clan_id;
	}
	
	public void set_clan_id(int clan_id) {
		_clan_id = clan_id;
	}
	
	public String get_clan_name() {
		return _clan_name;
	}
	
	public void set_clan_name(String clan_name) {
		_clan_name = clan_name;
	}
	
	public int get_skill_id() {
		return _skill_id;
	}
	
	public void set_skill_id (int skill_id) {
		_skill_id = skill_id;
	}
	
	public String get_skill_name() {
		return _skill_name;
	}
	
	public void set_skill_name(String skill_name) {
		_skill_name = skill_name;
	}
	
	public Timestamp get_remaining_time() {
		return _remaining_time;
	}
	
	public void set_remaining_time(Timestamp time) {
		_remaining_time = time;
	}
	
}
