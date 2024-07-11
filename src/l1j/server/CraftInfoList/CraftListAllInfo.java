package l1j.server.CraftInfoList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CraftListAllInfo {
	static CraftListAllInfo newInstance(ResultSet rs) throws SQLException {
		CraftListAllInfo pInfo = newInstance();
		pInfo._craft_id 			= rs.getInt("craft_id");
		pInfo._name_id 				= rs.getInt("name_id");
		pInfo._name_id_view 		= rs.getString("name_id_view");
		pInfo._required_classes 	= rs.getInt("required_classes");
		pInfo._batch_delay_seconds 	= rs.getInt("batch_delay_seconds");
		pInfo._min_level 			= rs.getInt("min_level");
		pInfo._max_level 			= rs.getInt("max_level");
		pInfo._required_gender 		= rs.getInt("required_gender");
		pInfo._min_align 			= rs.getInt("min_align");
		pInfo._max_align 			= rs.getInt("max_align");
		pInfo._min_karma 			= rs.getInt("min_karma");
		pInfo._max_karma 			= rs.getInt("max_karma");
		pInfo._max_count 			= rs.getInt("max_count");
		pInfo._show					= rs.getBoolean("show");
		return pInfo;
	}

	private static CraftListAllInfo newInstance() {
		return new CraftListAllInfo();
	}

	private int _craft_id;
	private int _name_id;
	private String _name_id_view;
	private int _required_classes;
	private int _batch_delay_seconds;
	private int _min_level;
	private int _max_level;	
	private int _required_gender;
	private int _min_align;
	private int _max_align;
	private int _min_karma;
	private int _max_karma;
	private int _max_count;
	private boolean _show;

	private CraftListAllInfo() {
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

	public int get_required_classes(){
		return _required_classes;
	}

	public int get_batch_delay_seconds() {
		return _batch_delay_seconds;
	}

	public int get_min_level() {
		return _min_level;
	}
	
	public int get_max_level() {
		return _max_level;
	}
	
	public int get_required_gender() {
		return _required_gender;
	}
	
	public int get_min_align() {
		return _min_align;
	}
	
	public int get_max_align() {
		return _max_align;
	}
	
	public int get_min_karma() {
		return _min_karma;
	}
	
	public int get_max_karma() {
		return _max_karma;
	}
	
	public int get_max_count() {
		return _max_count;
	}
	
	public boolean get_show() {
		return _show;
	}
}
