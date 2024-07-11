package l1j.server.MJTemplate.Spawn.DayAndNight;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.MJTemplate.Spawn.MJSpawnInfo;
import l1j.server.MJTemplate.Spawn.MJSpawnUpdator;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.model.gametime.MJIDayAndNightHandler;

public class MJDayAndNightSpawnLoader implements MJIDayAndNightHandler{
	private static MJDayAndNightSpawnLoader m_instance;
	public static MJDayAndNightSpawnLoader getInstance(){
		if(m_instance == null)
			m_instance = new MJDayAndNightSpawnLoader();
		return m_instance;
	}

	private MJDayAndNightSpawnLoader(){
		GameTimeClock.getInstance().add_days_listener(this);
		do_load();
	}

	public void do_reload() {
		do_clear();
		do_load();
	}
	
	public void do_load() {
		Selector.exec("select * from spawnlist_ex_day_night", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					MJDayAndNightSpawnBuilder.newInstance(rs).build();
				}
			}
		});
	}
	
	public void do_clear() {
		MJSpawnUpdator updator = MJSpawnUpdator.getInstance();
		for(MJSpawnInfo sInfo : updator.get_spawns()) {
			if(sInfo == null)
				continue;
			
			if(!(sInfo instanceof MJDayAndNightSpawnInfo))
				continue;
			
			updator.remove_spawn_info(sInfo.get_spawn_id());
			L1NpcInstance npc = updator.remove_npc_info(sInfo.get_spawn_id());
			if(npc != null && !npc.isDead()) {
				npc.deleteMe();
				
			}
		}
	}
	
	public void do_reload_map(int mapid) {
		do_map_clear(mapid);
		do_map_load(mapid);
	}
	
	public void do_map_load(int mapid) {
		Selector.exec("select * from spawnlist_ex_day_night where mapid=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, mapid);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()) {
					MJDayAndNightSpawnBuilder.newInstance(rs).build();
				}
			}
		});
	}
	
	public void do_map_clear(int mapid) {
		MJSpawnUpdator updator = MJSpawnUpdator.getInstance();
		for(MJSpawnInfo sInfo : updator.get_spawns()) {
			if(sInfo == null)
				continue;
			
			if(!(sInfo instanceof MJDayAndNightSpawnInfo))
				continue;
			
			if(!(sInfo.get_mapid() == mapid))
				continue;
			
			updator.remove_spawn_info(sInfo.get_spawn_id());
			L1NpcInstance npc = updator.remove_npc_info(sInfo.get_spawn_id());
			if(npc != null && !npc.isDead()) {
				npc.deleteMe();				
			}
		}
	}

	@Override
	public void on_night() {
		MJSpawnUpdator updator = MJSpawnUpdator.getInstance();
		for(MJSpawnInfo sInfo : updator.get_spawns()) {
			if(!(sInfo instanceof MJDayAndNightSpawnInfo))
				continue;
			
			MJDayAndNightSpawnInfo dnsInfo = (MJDayAndNightSpawnInfo)sInfo;
			if(dnsInfo.get_is_days_spawn()) {
				L1NpcInstance npc = MJSpawnUpdator.getInstance().remove_npc_info(dnsInfo.get_spawn_id());
				if(npc != null && !npc.isDead())
					npc.deleteMe();
			}
		}
	}

	@Override
	public void on_day() {
		MJSpawnUpdator updator = MJSpawnUpdator.getInstance();
		for(MJSpawnInfo sInfo : updator.get_spawns()) {
			if(!(sInfo instanceof MJDayAndNightSpawnInfo))
				continue;
			
			MJDayAndNightSpawnInfo dnsInfo = (MJDayAndNightSpawnInfo)sInfo;
			if(!dnsInfo.get_is_days_spawn()) {
				L1NpcInstance npc = MJSpawnUpdator.getInstance().remove_npc_info(dnsInfo.get_spawn_id());
				if(npc != null && !npc.isDead())
					npc.deleteMe();
			}
		}
	}
}

