package l1j.server.QueenAntSystem;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastTable;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.templates.L1Npc;

public class QueenAntSpawnlistInfo {
	static FastTable<L1NpcInstance> newInstance(ResultSet rs, int type) throws SQLException {
		FastTable<L1NpcInstance> list = new FastTable<L1NpcInstance>();
		L1Npc l1npc = null;
		L1NpcInstance npc = null;
		if (type == rs.getInt("spawn_type")) {
			int npcid = rs.getInt("npc_id");
			int locX = rs.getInt("loc_x");
			int locY = rs.getInt("loc_y");
			int mapid = rs.getInt("loc_map");
			int heading = rs.getInt("heading");
			int spawn_time = rs.getInt("spawn_time");
			l1npc = NpcTable.getInstance().getTemplate(npcid);
			if (l1npc != null) {
				try {
					npc = NpcTable.getInstance().newNpcInstance(npcid);
					npc.setId(IdFactory.getInstance().nextId());
					npc.setX(locX);
					npc.setY(locY);
					npc.setMap((short) mapid);
					npc.setMap(L1WorldMap.getInstance().getMap((short) mapid));
					npc.setHomeX(npc.getX());
					npc.setHomeY(npc.getY());
					npc.setHeading(heading);
					npc.setLightSize(l1npc.getLightSize());
					npc.getLight().turnOnOffLight();
					
					if (spawn_time > 0) {
						L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, spawn_time * 1000);
						timer.begin();
					}
					
					L1World.getInstance().storeObject(npc);
					L1World.getInstance().addVisibleObject(npc);
					list.add(npc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println(String.format("spawnlist_queen_ant中登記了一個不存在的npcid。npcid: %d", npcid));
			}
		}
		return list;
	}
	
}
