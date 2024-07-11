package l1j.server.CraftList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CraftItemInfo {
	static CraftItemInfo newInstance(ResultSet rs) throws SQLException {
		CraftItemInfo pInfo = newInstance();
		pInfo._craft_id = rs.getInt("craft_id");
		pInfo._craft_item_name = rs.getString("craft_item_name");
		pInfo._craft_current_count = rs.getInt("buy_current_count");
		pInfo._craft_max_count = rs.getInt("buy_max_count");
		return pInfo;
	}
	
	private static CraftItemInfo newInstance() {
		return new CraftItemInfo();
	}
	
	private int _craft_id;
	private String _craft_item_name;
	private int _craft_current_count;
	private int _craft_max_count;
	
	public int get_craft_id() {
		return _craft_id;
	}
	
	public String get_craft_item_name() {
		return _craft_item_name;
	}
	
	public int get_craft_current_count() {
		return _craft_current_count;
	}
	
	public void add_craft_current_count(int value) {
		_craft_current_count += value;
	}
	
	public int get_craft_max_count() {
		return _craft_max_count;
	}
}
