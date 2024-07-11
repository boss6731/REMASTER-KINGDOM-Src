package l1j.server.CraftInfoList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CraftListRequiredItemsInfo {
	static CraftListRequiredItemsInfo newInstance(ResultSet rs) throws SQLException {
		CraftListRequiredItemsInfo pInfo = newInstance();
		pInfo._craft_id = rs.getInt("craft_id");
		pInfo._name_id = rs.getInt("name_id");
		pInfo._name_id_view = rs.getString("name_id_view");
		pInfo._item_type = rs.getString("item_type");
		pInfo._item_desc_id = rs.getInt("item_desc_id");
		pInfo._item_name_id_view = rs.getString("item_name_id_view");
		pInfo._item_name_id_real_view = rs.getString("item_name_id_real_view");
		pInfo._item_count = rs.getInt("item_count");
		pInfo._item_is_nagative = rs.getBoolean("item_is_nagative");
		return pInfo;
	}

	private static CraftListRequiredItemsInfo newInstance() {
		return new CraftListRequiredItemsInfo();
	}

	private int _craft_id;
	private int _name_id;
	private String _name_id_view;
	private String _item_type;
	private int _item_desc_id;
	private String _item_name_id_view;
	private String _item_name_id_real_view;
	private int _item_count;
	private boolean _item_is_nagative;

	private CraftListRequiredItemsInfo() {
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

	public String get_item_type() {
		return _item_type;
	}

	public int get_item_desc_id() {
		return _item_desc_id;
	}

	public String get_item_name_id_view() {
		return _item_name_id_view;
	}
	
	public String get_item_name_id_real_view() {
		return _item_name_id_real_view;
	}
	
	public int get_item_count() {
		return _item_count;
	}
	
	public boolean get_item_is_nagative() {
		return _item_is_nagative;
	}
}
