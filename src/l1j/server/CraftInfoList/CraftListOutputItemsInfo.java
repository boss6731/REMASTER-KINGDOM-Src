package l1j.server.CraftInfoList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CraftListOutputItemsInfo {
	static CraftListOutputItemsInfo newInstance(ResultSet rs) throws SQLException {
		CraftListOutputItemsInfo pInfo = newInstance();
		pInfo._craft_id = rs.getInt("craft_id");
		pInfo._name_id = rs.getInt("name_id");
		pInfo._name_id_view = rs.getString("name_id_view");
		pInfo._is_success = rs.getBoolean("is_success");
		pInfo._is_probability = rs.getBoolean("is_probability");
		pInfo._item_desc_id = rs.getString("item_desc_id");
		pInfo._item_count = rs.getInt("item_count");
		pInfo._item_slot = rs.getInt("item_slot");
		pInfo._item_enchant = rs.getInt("item_enchant");
		pInfo._item_bless = rs.getInt("item_bless");
		pInfo._item_elemental_type = rs.getInt("item_elemental_type");
		pInfo._item_elemental_level = rs.getInt("item_elemental_level");
		pInfo._item_name_id = rs.getString("item_name_id");
		pInfo._item_name_id_view = rs.getString("item_name_id_view");
		pInfo._item_system_name_id = rs.getInt("item_system_name_id");
		pInfo._item_system_name_id_view = rs.getString("item_system_name_id_view");
		pInfo._item_broadcast_name_id = rs.getInt("item_broadcast_name_id");
		pInfo._item_broadcast_name_id_view = rs.getString("item_broadcast_name_id_view");
		pInfo._item_icon_id = rs.getInt("item_icon_id");
		pInfo._item_url = rs.getString("item_url");
		pInfo._item_extra_desc = rs.getString("item_extra_desc");
		pInfo._item_inherit_enchant_from = rs.getInt("item_inherit_enchant_from");
		pInfo._item_inherit_elemental_enchant_from = rs.getInt("item_inherit_elemental_enchant_from");
		pInfo._item_event_id = rs.getInt("item_event_id");
		pInfo._item_inherit_bless_from = rs.getInt("item_inherit_bless_from");
		return pInfo;
	}

	private static CraftListOutputItemsInfo newInstance() {
		return new CraftListOutputItemsInfo();
	}

	private int _craft_id;
	private int _name_id;
	private String _name_id_view;
	private boolean _is_success;
	private boolean _is_probability;
	private String _item_desc_id;
	private int _item_count;
	private int _item_slot;
	private int _item_enchant;
	private int _item_bless;
	private int _item_elemental_type;
	private int _item_elemental_level;
	private String _item_name_id;
	private String _item_name_id_view;
	private int _item_system_name_id;
	private String _item_system_name_id_view;
	private int _item_broadcast_name_id;
	private String _item_broadcast_name_id_view;
	private int _item_icon_id;
	private String _item_url;
	private String _item_extra_desc;
	private int _item_inherit_enchant_from;
	private int _item_inherit_elemental_enchant_from;
	private int _item_event_id;
	private int _item_inherit_bless_from;
	

	private CraftListOutputItemsInfo() {
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

	public boolean get_is_success() {
		return _is_success;
	}

	public boolean get_is_probability() {
		return _is_probability;
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
	
	public int get_item_elemental_type() {
		return _item_elemental_type;
	}
	
	public int get_item_elemental_level() {
		return _item_elemental_level;
	}
	
	public String get_item_name_id() {
		return _item_name_id;
	}
	
	public String get_item_name_id_view() {
		return _item_name_id_view;
	}
	
	public int get_item_system_name_id() {
		return _item_system_name_id;
	}
	
	public String get_item_system_name_id_view() {
		return _item_system_name_id_view;
	}
	
	public int get_item_broadcast_name_id() {
		return _item_broadcast_name_id;
	}
	
	public String get_item_broadcast_name_id_view() {
		return _item_broadcast_name_id_view;
	}
	
	public int get_item_icon_id() {
		return _item_icon_id;
	}
	
	public String get_item_url() {
		return _item_url;
	}
	
	public String get_item_extra_desc() {
		return _item_extra_desc;
	}

	public int get_item_inherit_enchant_from() {
		return _item_inherit_enchant_from;
	}
	
	public int get_item_inherit_elemental_enchant_from() {
		return _item_inherit_elemental_enchant_from;
	}
	
	public int get_item_event_id() {
		return _item_event_id;
	}
	
	public int get_item_inherit_bless_from() {
		return _item_inherit_bless_from;
	}
}
