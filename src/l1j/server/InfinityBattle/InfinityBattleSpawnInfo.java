package l1j.server.InfinityBattle;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InfinityBattleSpawnInfo {
	static InfinityBattleSpawnInfo newInstance(ResultSet rs) throws SQLException {
		InfinityBattleSpawnInfo Info = newInstance();
		Info._stage 	= rs.getInt("stage");
		Info._npc_id 	= rs.getInt("npc_id");
		Info._count 	= rs.getInt("count");
		return Info;
	}
	
	private static InfinityBattleSpawnInfo newInstance() {
		return new InfinityBattleSpawnInfo();
	}
	
	private int _stage;
	private int _npc_id;
	private int _count;
	
	private InfinityBattleSpawnInfo() {
	}
	
	public int get_stage() {
		return _stage;
	}
	
	public int get_npc_id() {
		return _npc_id;
	}
	
	public int get_count() {
		return _count;
	}
}
