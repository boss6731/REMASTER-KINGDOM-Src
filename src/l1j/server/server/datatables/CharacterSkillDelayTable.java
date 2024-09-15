package l1j.server.server.server.datatables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_DELAY_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.model.Instance.L1PcInstance;

public class CharacterSkillDelayTable {
	
	private static CharacterSkillDelayTable _instance;
	public static CharacterSkillDelayTable getInstace() {
		if (_instance == null) {
			_instance = new CharacterSkillDelayTable();
		}
		return _instance;
	}
	
	private HashMap<Integer, HashMap<Integer, Long>> _list = new HashMap<Integer, HashMap<Integer, Long>>();
	
	
	private CharacterSkillDelayTable() {
		Selector.exec("SELECT * FROM character_skills_delay", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					int id = rs.getInt("char_id");
					HashMap<Integer, Long> delays = _list.get(id);
					if (delays == null) {
						delays = new HashMap<Integer, Long>();
						_list.put(id, delays);
					}
					delays.put(rs.getInt("skill_id"), rs.getLong("delay"));
				}
			}
		});
	}
	
	public boolean checkDelay(L1PcInstance pc, int skillId) {
		HashMap<Integer, Long> delays = _list.get(pc.getId());
		if (delays != null && delays.get(skillId) != null) {
			long global_delay = SpriteInformationLoader.getInstance().getUseSpellGlobalInterval(pc, skillId);
			int group_id = SpriteInformationLoader.getInstance().getUseSpellGroupId(skillId);
			long currentMillis = System.currentTimeMillis();
			long curDelay = delays.get(skillId);
			if (currentMillis < curDelay) { // 如果延遲時間大於當前時間？ 還有延遲時間。
				long delay = curDelay - currentMillis;
//				long last_delay = global_delay > 0 ? global_delay : delay;
//				System.out.println("check delay:" + delay + " last_delay:" + last_delay + " group_id:" + group_id);
				SC_SPELL_DELAY_NOTI.UseSkillDelay(pc, (int) delay, (int) global_delay, group_id);
//				update(pc, skillId, delay + currentMillis);
				return false;
			} else {
//				delays.remove(skillId);
//				Updator.exec("delete FROM character_skills_delay WHERE char_id=? AND skill_id=?", new Handler(){
//					@Override
//					public void handle(PreparedStatement pstm) throws Exception {
//						int idx = 0;
//						pstm.setInt(++idx, pc.getId());
//						pstm.setInt(++idx, skillId);
//					}
//				});
			}
		}
		return true;
	}
	
	public void update(L1PcInstance pc, int skillId, long curDelay) {
		if (SpriteInformationLoader.getInstance().isUseSpellDelayRecord(skillId)) {
			HashMap<Integer, Long> delays = _list.get(pc.getId());
			if (delays == null) {
				delays = new HashMap<Integer, Long>();
				_list.put(pc.getId(), delays);
			}
			delays.put(skillId, curDelay);
		}
	}
	
	public void updatedata(L1PcInstance pc) {
		HashMap<Integer, Long> delays = _list.get(pc.getId());
		if (delays == null) {
			return;
		}
		if(delays.size() == 0) {
			return;
		}
		delays.forEach((k,v) -> updatedb(pc, k, v));
	}
	
	public void updatedb(L1PcInstance pc, int skillId, long curDelay) {
		Updator.exec("INSERT INTO character_skills_delay SET char_id=?, skill_id=?, delay=? " + 
                "ON DUPLICATE KEY UPDATE skill_id=?, delay=?", new Handler() {
			
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, pc.getId());
				pstm.setInt(++idx, skillId);
				pstm.setLong(++idx, curDelay);
				pstm.setInt(++idx, skillId);
				pstm.setLong(++idx, curDelay);
			}
		});
	}
}
