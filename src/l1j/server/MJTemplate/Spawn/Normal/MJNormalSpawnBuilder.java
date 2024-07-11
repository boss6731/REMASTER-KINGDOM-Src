package l1j.server.MJTemplate.Spawn.Normal;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Spawn.MJSpawnInfo;
import l1j.server.MJTemplate.Spawn.MJSpawnUpdator;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.MapsTable;

public class MJNormalSpawnBuilder {
	static MJNormalSpawnBuilder newInstance(ResultSet rs) throws SQLException{
		return newInstance()
				.set_location_name(rs.getString("location_name"))
				.set_count(rs.getInt("count"))
				.set_npc_id(rs.getInt("npc_id"))
				.set_group_id(rs.getInt("group_id"))
				.set_loc_x(rs.getInt("loc_x"))
				.set_loc_y(rs.getInt("loc_y"))
				.set_area_left(rs.getInt("area_left"))
				.set_area_top(rs.getInt("area_top"))
				.set_area_right(rs.getInt("area_right"))
				.set_area_bottom(rs.getInt("area_bottom"))
				.set_mapid(rs.getInt("mapid"))
				.set_movement_distance(rs.getInt("movement_distance"))
				.set_spawn_type(rs.getInt("spawn_type"))
				.set_delete_delay(rs.getInt("delete_delay"))
				.set_min_respawn_seconds(rs.getInt("min_respawn_seconds"))
				.set_max_respawn_seconds(rs.getInt("max_respawn_seconds"));
	}
	
	private static MJNormalSpawnBuilder newInstance(){
		return new MJNormalSpawnBuilder();
	}
	
	private String m_location_name;
	private int m_count;
	private int m_npc_id;
	private int m_group_id;
	private int m_loc_x;
	private int m_loc_y;
	private int m_area_left;
	private int m_area_top;
	private int m_area_right;
	private int m_area_bottom;
	private int m_mapid;
	private int m_movement_distance;
	private int m_spawn_type;
	private int m_delete_delay;
	private int m_min_respawn_seconds;
	private int m_max_respawn_seconds;
	private MJNormalSpawnBuilder() {
		
	}
	MJNormalSpawnBuilder set_location_name(String location_name){
		m_location_name = MJString.isNullOrEmpty(location_name) ? MJString.EmptyString : location_name;
		return this;
	}
	MJNormalSpawnBuilder set_count(int count){
		double amount_rate = MapsTable.getInstance().getMonsterAmount(m_mapid);
		m_count = count * (int)amount_rate;
		return this;
	}
	MJNormalSpawnBuilder set_npc_id(int npc_id){
		m_npc_id = npc_id;
		return this;
	}
	MJNormalSpawnBuilder set_group_id(int group_Id){
		m_group_id = group_Id;
		return this;
	}
	MJNormalSpawnBuilder set_loc_x(int loc_x){
		m_loc_x = loc_x;
		return this;
	}
	MJNormalSpawnBuilder set_loc_y(int loc_y){
		m_loc_y = loc_y;
		return this;
	}
	MJNormalSpawnBuilder set_area_left(int area_left){
		m_area_left = area_left;
		return this;
	}
	MJNormalSpawnBuilder set_area_top(int area_top){
		m_area_top = area_top;
		return this;
	}
	MJNormalSpawnBuilder set_area_right(int area_right){
		m_area_right = area_right;
		return this;
	}
	MJNormalSpawnBuilder set_area_bottom(int area_bottom){
		m_area_bottom = area_bottom;
		return this;
	}
	MJNormalSpawnBuilder set_mapid(int mapid){
		m_mapid = mapid;
		return this;
	}
	MJNormalSpawnBuilder set_movement_distance(int movement_distance){
		m_movement_distance = movement_distance;
		return this;
	}
	MJNormalSpawnBuilder set_spawn_type(int spawn_type){
		m_spawn_type = spawn_type;
		return this;
	}
	MJNormalSpawnBuilder set_delete_delay(int delete_delay){
		m_delete_delay = delete_delay;
		return this;
	}
	MJNormalSpawnBuilder set_min_respawn_seconds(int min_respawn_seconds){
		m_min_respawn_seconds = min_respawn_seconds;
		return this;
	}
	MJNormalSpawnBuilder set_max_respawn_seconds(int max_respawn_seconds){
		m_max_respawn_seconds = max_respawn_seconds;
		return this;
	}
	
	void build(){
		double amount_rate = MapsTable.getInstance().getMonsterAmount(m_mapid);
		int count = m_count*(int)amount_rate;
		for(int i=count - 1; i>=0; --i) {
/*			if (m_mapid == 807) {
				System.out.println((int)amount_rate);
			}*/
			MJSpawnInfo sInfo = MJNormalSpawnInfo.newInstance()
					.set_min_respawn_seconds(m_min_respawn_seconds)
					.set_max_respawn_seconds(m_max_respawn_seconds)
					.set_spawn_id(IdFactory.getInstance().nextId())
					.set_location_name(m_location_name)
					.set_npc_id(m_npc_id)
					.set_count(m_count)
					.set_group_id(m_group_id)
					.set_loc_x(m_loc_x)
					.set_loc_y(m_loc_y)
					.set_area_left(m_area_left)
					.set_area_top(m_area_top)
					.set_area_right(m_area_right)
					.set_area_bottom(m_area_bottom)
					.set_mapid(m_mapid)
					.set_movement_distance(m_movement_distance)
					.set_spawn_type(m_spawn_type)
					.set_delete_delay(m_delete_delay);
			MJSpawnUpdator.getInstance().append_spawn_info(sInfo);
		}
	}
}
