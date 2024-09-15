package l1j.server.tempSkillSystem;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class tempSkillSystemLoader {
	private static tempSkillSystemLoader _instance;
	public static tempSkillSystemLoader getInstance() {
		if(_instance == null)
			_instance = new tempSkillSystemLoader();
		return _instance;
	}
	
	public static void reload() {
		if(_instance != null) {
			_instance = new tempSkillSystemLoader();
		}
	}
	
	private HashMap<Integer, tempSkillSystemInfo> _bonusitem;
	private tempSkillSystemLoader() {
		load();
	}
	
	private void load() {
		final HashMap<Integer, tempSkillSystemInfo> bonus = new HashMap<Integer, tempSkillSystemInfo>(256);
		Selector.exec("select * from temp_skill_items", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					tempSkillSystemInfo Info = tempSkillSystemInfo.newInstance(rs);
					if(Info == null)
						continue;
					bonus.put(Info.get_item_id(), Info);
				}
			}
		});
		_bonusitem = bonus;
	}
	
	public tempSkillSystemInfo getTempSkillSystemInfo(int itemId) {
		if (_bonusitem.containsKey(itemId)) {
			return _bonusitem.get(itemId);
		}
		return null;
	}
}