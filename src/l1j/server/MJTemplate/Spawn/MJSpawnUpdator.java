package l1j.server.MJTemplate.Spawn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;

public class MJSpawnUpdator implements Runnable{
	private static MJSpawnUpdator m_instance;
	public static MJSpawnUpdator getInstance() {
		if(m_instance == null)
			m_instance = new MJSpawnUpdator();
		return m_instance;
	}

	private ConcurrentHashMap<Integer, MJSpawnInfo> m_spawns;
	private ConcurrentHashMap<Integer, L1NpcInstance> m_npcs;
	private MJSpawnUpdator() {
		m_spawns = new ConcurrentHashMap<Integer, MJSpawnInfo>();
		m_npcs = new ConcurrentHashMap<Integer, L1NpcInstance>();
		GeneralThreadPool.getInstance().scheduleAtFixedRate(this, 500L, 500L);
	}
	
	public void append_spawn_info(MJSpawnInfo sInfo) {
/*		if (sInfo.get_mapid() == 807) {
			System.out.println(sInfo.get_count());
		}*/
		m_spawns.put(sInfo.get_spawn_id(), sInfo);
	}
	
	public MJSpawnInfo remove_spawn_info(MJSpawnInfo sInfo) {
		return remove_spawn_info(sInfo.get_spawn_id());
	}
	
	public MJSpawnInfo remove_spawn_info(int spawn_id) {
		return m_spawns.remove(spawn_id);
	}

	public void append_npc_info(L1NpcInstance npc) {
		m_npcs.put(npc.getId(), npc);
	}
	
	public L1NpcInstance remove_npc_info(L1NpcInstance npc) {
		return remove_npc_info(npc.getId());
	}
	
	public L1NpcInstance remove_npc_info(int object_id) {
		return m_npcs.remove(object_id);
	}
	
	public List<MJSpawnInfo> get_spawns(){
		return new ArrayList<MJSpawnInfo>(m_spawns.values());
	}
	
	@Override
	public void run() {
		try {
			long current_millis = System.currentTimeMillis();
			for(MJSpawnInfo sInfo : m_spawns.values()) {
				if(!sInfo.is_spawn(current_millis))
					continue;
				
				L1NpcInstance npc = sInfo.do_spawn();
				if(npc != null) {
					npc.set_delete_delay(sInfo.get_delete_delay());
					append_npc_info(npc);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
