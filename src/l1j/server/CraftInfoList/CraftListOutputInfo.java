package l1j.server.CraftInfoList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CraftListOutputInfo {
	static CraftListOutputInfo newInstance(ResultSet rs) throws SQLException {
		CraftListOutputInfo pInfo = newInstance();
		pInfo._craft_id = rs.getInt("craft_id");
		pInfo._name_id = rs.getInt("name_id");
		pInfo._name_id_view = rs.getString("name_id_view");
		pInfo._success_probability_by_millions = rs.getInt("success_probability_by_millions");
		pInfo._is_success = rs.getBoolean("is_success");
		pInfo._on_flag_1 = rs.getLong("on_flag_1");
		pInfo._on_flag_2 = rs.getLong("on_flag_2");
		pInfo._off_flag_1 = rs.getLong("off_flag_1");
		pInfo._off_flag_2 = rs.getLong("off_flag_2");
		pInfo._prob_count = rs.getInt("prob_count");
		pInfo._non_prob_count = rs.getInt("non_prob_count");
		pInfo._event_count = rs.getInt("event_count");
		return pInfo;
	}

	private static CraftListOutputInfo newInstance() {
		return new CraftListOutputInfo();
	}

	private int _craft_id;
	private int _name_id;
	private String _name_id_view;
	private int _success_probability_by_millions;
	private boolean _is_success;
	private long _on_flag_1;
	private long _on_flag_2;
	private long _off_flag_1;
	private long _off_flag_2;
	private int _prob_count;
	private int _non_prob_count;
	private int _event_count;

	private CraftListOutputInfo() {
	}

	public int get_craft_id() {
		return _craft_id;
	}

	public int get_name_id() {
		return _name_id;
	}

	public String get_name_id_view() {
		return _name_id_view;
	}

	public int get_success_probability_by_millions() {
		return _success_probability_by_millions;
	}

	public boolean get_is_success() {
		return _is_success;
	}

	public long get_on_flag_1() {
		return _on_flag_1;
	}
	
	public long get_on_flag_2() {
		return _on_flag_2;
	}
	
	public long get_off_flag_1() {
		return _off_flag_1;
	}
	
	public long get_off_flag_2() {
		return _off_flag_2;
	}
	
	public int get_prob_count() {
		return _prob_count;
	}
	
	public int get_non_prob_count() {
		return _non_prob_count;
	}
	
	public int get_event_count() {
		return _event_count;
	}
}
