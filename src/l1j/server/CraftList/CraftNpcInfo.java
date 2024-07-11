package l1j.server.CraftList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CraftNpcInfo {
	static CraftNpcInfo newInstance(ResultSet rs) throws SQLException {
		CraftNpcInfo pInfo = newInstance();
		pInfo._npc_id = rs.getInt("npc_id");
		pInfo._npc_name = rs.getString("npc_name");
		pInfo._craft_list = rs.getString("craft_list").trim();
		pInfo._make_message = rs.getString("make_message");
		return pInfo;
	}
	
	private static CraftNpcInfo newInstance() {
		return new CraftNpcInfo();
	}
	
	private int _npc_id;
	private String _npc_name;
	private String _craft_list;
	private String _make_message;
	
	public int get_npcid() {
		return _npc_id;
	}
	
	public String get_npc_name() {
		return _npc_name;
	}
	
	public String get_craft_list() {
		return _craft_list;
	}
	
	public String get_make_message() {
		return _make_message;
	}
}
