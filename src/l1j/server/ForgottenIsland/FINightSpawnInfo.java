package l1j.server.ForgottenIsland;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FINightSpawnInfo {
	public static FINightSpawnInfo newInstance(ResultSet rs) throws SQLException {
		FINightSpawnInfo Info = newInstance();
		Info._type 			= get_stage_info(rs.getString("type"));
		Info._npc_id 		= rs.getString("npc_id");
		Info._map_id		= rs.getInt("map_id");
		Info._spawn_time 	= rs.getInt("spawn_time");
		Info._count 		= rs.getString("count");
		Info._range 		= rs.getInt("range");
		Info._spawn_percent = rs.getInt("spawn_percent");
		return Info;
	}
	
	private static FINightSpawnInfo newInstance() {
		return new FINightSpawnInfo();
	}
	
	private int _type;
	private String _npc_id;
	private int _map_id;
	private int _spawn_time;
	private String _count;
	private int _range;
	private int _spawn_percent;
	
	private FINightSpawnInfo() {
	}
	
	public int get_type() {
		return _type;
	}
	
	public String get_npc_id() {
		return _npc_id;
	}
	
	public int get_map_id() {
		return _map_id;
	}
	
	public int get_spawn_time() {
		return _spawn_time;
	}
	
	public String get_count() {
		return _count;
	}
	
	public int get_range() {
		return _range;
	}
	
	public int get_spawn_percent() {
		return _spawn_percent;
	}
	
	public static int get_stage_info(String info) {
		int type = 0;
		if (info.equalsIgnoreCase("AllUser"))
			type = 1;
		if (info.equalsIgnoreCase("RandomUser"))
			type = 2;
		if (info.equalsIgnoreCase("AllRandom"))
			type = 3;
		return type;
	}
}
