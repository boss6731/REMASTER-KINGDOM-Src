package l1j.server.ItemDropLimit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDropLimitInfo {
	static ItemDropLimitInfo newInstance(ResultSet rs) throws SQLException {
		ItemDropLimitInfo pInfo = newInstance();
		pInfo._item_id = rs.getInt("item_id");
		pInfo._item_name = rs.getString("item_name");
		pInfo._current_count = rs.getInt("current_count");
		pInfo._max_count = rs.getInt("max_count");
		return pInfo;
	}
	
	private static ItemDropLimitInfo newInstance() {
		return new ItemDropLimitInfo();
	}
	
	private int _item_id;
	private String _item_name;
	private int _current_count;
	private int _max_count;
	
	public int get_item_id() {
		return _item_id;
	}
	
	public String get_item_name() {
		return _item_name;
	}
	
	public int get_current_count() {
		return _current_count;
	}
	
	public void set_current_count(int value) {
		_current_count = value;
	}
	
	public void add_current_count(int value) {
		_current_count += value;
	}
	
	public int get_max_count() {
		return _max_count;
	}
}
