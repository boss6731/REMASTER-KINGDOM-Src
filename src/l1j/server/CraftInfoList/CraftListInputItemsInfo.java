package l1j.server.CraftInfoList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CraftListInputItemsInfo {
	static CraftListInputItemsInfo newInstance(ResultSet rs) throws SQLException {
		CraftListInputItemsInfo pInfo = newInstance();
		pInfo._craft_id 					= rs.getInt("craft_id");
		pInfo._name_id 						= rs.getInt("name_id");
		pInfo._name_id_view 				= rs.getString("name_id_view");
		pInfo._item_is_optional 			= rs.getBoolean("item_is_optional");
		pInfo._item_desc_id 				= rs.getString("item_desc_id");
		pInfo._item_count 					= rs.getInt("item_count");
		pInfo._item_slot 					= rs.getInt("item_slot");
		pInfo._item_enchant 				= rs.getInt("item_enchant");
		pInfo._item_bless 					= rs.getInt("item_bless");
		pInfo._item_name_id 				= rs.getString("item_name_id");
		pInfo._item_name_id_view 			= rs.getString("item_name_id_view");
		pInfo._item_icon_id 				= rs.getInt("item_icon_id");
		pInfo._item_elemental_type 			= rs.getInt("item_elemental_type");
		pInfo._item_elemental_level 		= rs.getInt("item_elemental_level");
		pInfo._item_increase_probability 	= rs.getInt("item_increase_probability");
		pInfo._item_all_enchants_allowed	= rs.getBoolean("item_all_enchants_allowed");
		return pInfo;
	}

	private static CraftListInputItemsInfo newInstance() {
		return new CraftListInputItemsInfo();
	}

	private int _craft_id;
	private int _name_id;
	private String _name_id_view;
	private boolean _item_is_optional;
	private String _item_desc_id;
	private int _item_count;
	private int _item_slot;
	private int _item_enchant;	
	private int _item_bless;
	private String _item_name_id;
	private String _item_name_id_view;
	private int _item_icon_id;
	private int _item_elemental_type;
	private int _item_elemental_level;
	private int _item_increase_probability;
	private boolean _item_all_enchants_allowed;

	private CraftListInputItemsInfo() {
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

	public boolean get_item_is_optional() {
		return _item_is_optional;
	}

	public String get_item_desc_id() {
		return _item_desc_id;
	}

	public int get_item_count() {
		return _item_count;
	}
	
	public int get_item_slot() {
		return _item_slot;
	}
	
	public int get_item_enchant() {
		return _item_enchant;
	}
	
	public int get_item_bless() {
		return _item_bless;
	}
	
	public String get_item_name_id() {
		return _item_name_id;
	}
	
	public String get_item_name_id_view() {
		return _item_name_id_view;
	}
	
	public int get_item_icon_id() {
		return _item_icon_id;
	}
	
	public int get_item_elemental_type() {
		return _item_elemental_type;
	}
	
	public int get_item_elemental_level() {
		return _item_elemental_level;
	}
	
	public int get_item_increase_probability() {
		return _item_increase_probability;
	}
	
	public boolean get_item_all_enchants_allowed() {
		return _item_all_enchants_allowed;
	}
}
