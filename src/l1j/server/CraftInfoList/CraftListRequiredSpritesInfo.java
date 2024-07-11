package l1j.server.CraftInfoList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CraftListRequiredSpritesInfo {
	static CraftListRequiredSpritesInfo newInstance(ResultSet rs) throws SQLException {
		CraftListRequiredSpritesInfo pInfo = newInstance();
		pInfo._craft_id = rs.getInt("craft_id");
		pInfo._name_id = rs.getInt("name_id");
		pInfo._name_id_view = rs.getString("name_id_view");
		pInfo._sprite_id = rs.getInt("sprite_id");
		return pInfo;
	}

	private static CraftListRequiredSpritesInfo newInstance() {
		return new CraftListRequiredSpritesInfo();
	}

	private int _craft_id;
	private int _name_id;
	private String _name_id_view;
	private int _sprite_id;

	private CraftListRequiredSpritesInfo() {
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

	public int get_sprite_id() {
		return _sprite_id;
	}
}
