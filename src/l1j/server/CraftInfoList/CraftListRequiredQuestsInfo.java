package l1j.server.CraftInfoList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CraftListRequiredQuestsInfo {
	static CraftListRequiredQuestsInfo newInstance(ResultSet rs) throws SQLException {
		CraftListRequiredQuestsInfo pInfo = newInstance();
		pInfo._craft_id = rs.getInt("craft_id");
		pInfo._name_id = rs.getInt("name_id");
		pInfo._name_id_view = rs.getString("name_id_view");
		pInfo._quest_type = rs.getString("quest_type");
		pInfo._quest_count = rs.getInt("quest_count");
		pInfo._quest_flag1 = rs.getString("quest_flag1");
		pInfo._quest_flag2 = rs.getString("quest_flag2");
		return pInfo;
	}

	private static CraftListRequiredQuestsInfo newInstance() {
		return new CraftListRequiredQuestsInfo();
	}

	private int _craft_id;
	private int _name_id;
	private String _name_id_view;
	private String _quest_type;
	private int _quest_count;
	private String _quest_flag1;
	private String _quest_flag2;

	private CraftListRequiredQuestsInfo() {
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

	public String get_quest_type() {
		return _quest_type;
	}

	public int get_quest_count() {
		return _quest_count;
	}

	public String get_quest_flag1() {
		return _quest_flag1;
	}

	public String get_quest_flag2() {
		return _quest_flag2;
	}
}
