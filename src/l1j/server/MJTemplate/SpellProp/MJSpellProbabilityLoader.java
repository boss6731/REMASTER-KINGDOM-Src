package l1j.server.MJTemplate.SpellProp;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJSpellProbabilityLoader {
	private static MJSpellProbabilityLoader _instance;
	public static MJSpellProbabilityLoader getInstance() {
		if(_instance == null)
			_instance = new MJSpellProbabilityLoader();
		return _instance;
	}
	public static void reload() {
		if(_instance != null) {
			_instance = new MJSpellProbabilityLoader();
		}
	}
	
	
	private HashMap<Integer, MJSpellProbabilityInfo> _spells;
	private MJSpellProbabilityLoader() {
		load();
	}
	
	private void load() {
		final HashMap<Integer, MJSpellProbabilityInfo> spells = new HashMap<Integer, MJSpellProbabilityInfo>(256);
		Selector.exec("select * from probability_by_spell", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					MJSpellProbabilityInfo pInfo = MJSpellProbabilityInfo.newInstance(rs);
					if(pInfo == null)
						continue;
					
					spells.put(pInfo.get_skill_id(), pInfo);
				}
			}
		});
		_spells = spells;
	}
	
	public int calc_probability(int skill_id, L1PcInstance pc, L1Character target, int attacker_int, int target_mr) {
		MJSpellProbabilityInfo pInfo = _spells.get(skill_id);
		return pInfo == null ? -1 : pInfo.calc_probability(pc, target, attacker_int, target_mr);
	}
	
	public boolean contains_probability(int skill_id) {
		return _spells.containsKey(skill_id);
	}
}
