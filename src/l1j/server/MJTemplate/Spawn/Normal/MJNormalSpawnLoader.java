package l1j.server.MJTemplate.Spawn.Normal;

import java.sql.ResultSet;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.Spawn.MJSpawnInfo;
import l1j.server.MJTemplate.Spawn.MJSpawnUpdator;
import l1j.server.server.model.Instance.L1NpcInstance;

public class MJNormalSpawnLoader {
	private static MJNormalSpawnLoader m_instance;

	public static MJNormalSpawnLoader getInstance() {
		if (m_instance == null)
			m_instance = new MJNormalSpawnLoader();
		return m_instance;
	}

	private MJNormalSpawnLoader() {
		do_load();
	}

	public void do_reload() {
		do_clear();
		do_load();
	}

	public void do_load() {
		Selector.exec("select * from spawnlist_ex_normal", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					MJNormalSpawnBuilder.newInstance(rs).build();
				}
			}
		});
	}

	public void do_clear() {
		MJSpawnUpdator updator = MJSpawnUpdator.getInstance();
		for (MJSpawnInfo sInfo : updator.get_spawns()) {
			if (sInfo == null)
				continue;

			if (!(sInfo instanceof MJNormalSpawnInfo))
				continue;

			updator.remove_spawn_info(sInfo.get_spawn_id());
			L1NpcInstance npc = updator.remove_npc_info(sInfo.get_spawn_id());
			if (npc != null && !npc.isDead()) {
				npc.deleteMe();

			}
		}
	}

	public void do_reload_map(int mapid) {
		do_map_clear(mapid);
		do_map_load(mapid);
	}

	public void do_map_load(int mapid) {
		Selector.exec("select * from spawnlist_ex_normal", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					if (rs.getInt("mapid") != mapid)
						continue;
					
					MJNormalSpawnBuilder.newInstance(rs).build();
				}
			}
		});
		/*Selector.exec("select * from spawnlist_ex_normal where mapid=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, mapid);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					MJNormalSpawnBuilder.newInstance(rs).build();
				}
			}
		});*/
	}

	public void do_map_clear(int mapid) {
		MJSpawnUpdator updator = MJSpawnUpdator.getInstance();
		for (MJSpawnInfo sInfo : updator.get_spawns()) {
			if (sInfo == null)
				continue;

			if (!(sInfo instanceof MJNormalSpawnInfo))
				continue;

			if (!(sInfo.get_mapid() == mapid))
				continue;

			updator.remove_spawn_info(sInfo.get_spawn_id());
			L1NpcInstance npc = updator.remove_npc_info(sInfo.get_spawn_id());
			if (npc != null && !npc.isDead()) {
				npc.deleteMe();
			}
		}
	}
}
