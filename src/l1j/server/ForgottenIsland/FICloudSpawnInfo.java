package l1j.server.ForgottenIsland;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FICloudSpawnInfo {
	public static FICloudSpawnInfo newInstance(ResultSet rs) throws SQLException {
		FICloudSpawnInfo Info = newInstance();
		Info._id 			= rs.getInt("id");
		Info._npc_id 		= rs.getInt("npc_id");
		Info._x				= rs.getInt("x");
		Info._y				= rs.getInt("y");
		Info._map_id		= rs.getInt("map_id");
		Info._range 		= rs.getInt("range");
		return Info;
	}
	
	private static FICloudSpawnInfo newInstance() {
		return new FICloudSpawnInfo();
	}
	
	private int _id;
	private int _npc_id;
	private int _x;
	private int _y;
	private int _map_id;
	private int _range;
	
	private FICloudSpawnInfo() {
	}
	
	public int get_id() {
		return _id;
	}
	
	public int get_npc_id() {
		return _npc_id;
	}
	
	public int get_x() {
		return _x;
	}
	
	public int get_y() {
		return _y;
	}
	
	public int get_map_id() {
		return _map_id;
	}
	
	public int get_range() {
		return _range;
	}
}
