package l1j.server.MJTemplate.Spawn.Normal;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Spawn.MJSpawnInfo;
import l1j.server.MJTemplate.Spawn.MJSpawnUpdator;
import l1j.server.server.model.Instance.L1NpcInstance;

public class MJNormalSpawnInfo extends MJSpawnInfo{
	private int m_min_respawn_seconds;
	private int m_max_respawn_seconds;
	private long m_next_spawn_millis;
	static MJNormalSpawnInfo newInstance() {
		return new MJNormalSpawnInfo();
	}
	
	private MJNormalSpawnInfo() {
		super();
		m_next_spawn_millis = System.currentTimeMillis();
	}
	
	public MJNormalSpawnInfo set_min_respawn_seconds(int min_respawn_seconds){
		m_min_respawn_seconds = min_respawn_seconds;
		return this;
	}
	public MJNormalSpawnInfo set_max_respawn_seconds(int max_respawn_seconds){
		m_max_respawn_seconds = max_respawn_seconds;
		return this;
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

		m_next_spawn_millis = 0;
		return true;
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