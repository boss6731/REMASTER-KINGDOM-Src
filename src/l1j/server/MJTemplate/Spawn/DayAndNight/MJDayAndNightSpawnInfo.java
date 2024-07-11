package l1j.server.MJTemplate.Spawn.DayAndNight;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Spawn.MJSpawnInfo;
import l1j.server.MJTemplate.Spawn.MJSpawnUpdator;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.gametime.GameTimeClock;

public class MJDayAndNightSpawnInfo extends MJSpawnInfo{
	private boolean m_is_days_spawn;
	private int m_min_respawn_seconds;
	private int m_max_respawn_seconds;
	private long m_next_spawn_millis;
	static MJDayAndNightSpawnInfo newInstance() {
		return new MJDayAndNightSpawnInfo();
	}
	
	private MJDayAndNightSpawnInfo() {
		super();
		m_next_spawn_millis = System.currentTimeMillis();
	}
	
	public MJDayAndNightSpawnInfo set_is_days_spawn(boolean is_days_spawn){
		m_is_days_spawn = is_days_spawn;
		return this;
	}
	public MJDayAndNightSpawnInfo set_min_respawn_seconds(int min_respawn_seconds){
		m_min_respawn_seconds = min_respawn_seconds;
		return this;
	}
	public MJDayAndNightSpawnInfo set_max_respawn_seconds(int max_respawn_seconds){
		m_max_respawn_seconds = max_respawn_seconds;
		return this;
	}
	public boolean get_is_days_spawn() {
		return m_is_days_spawn;
	}
	public int get_min_respawn_seconds() {
		return m_min_respawn_seconds;
	}
	
	public int get_max_respawn_seconds() {
		return m_max_respawn_seconds;
	}

	@Override
	public boolean is_spawn(long current_millis) {
		if(m_next_spawn_millis == 0)
			return false;

		if(m_next_spawn_millis > current_millis)
			return false;
		
		boolean is_spawn = GameTimeClock.getInstance().is_day() == m_is_days_spawn;
		if(is_spawn)
			m_next_spawn_millis = 0;
		return is_spawn;
	}

	@Override
	public void on_death(L1NpcInstance npc) {
		if(m_min_respawn_seconds == m_max_respawn_seconds) {
			m_next_spawn_millis = System.currentTimeMillis() + (m_min_respawn_seconds * 1000);
		}else {
			m_next_spawn_millis = System.currentTimeMillis() + MJRnd.next(m_min_respawn_seconds, m_max_respawn_seconds) * 1000L;
		}
		MJSpawnUpdator.getInstance().remove_npc_info(npc);
	}
}
