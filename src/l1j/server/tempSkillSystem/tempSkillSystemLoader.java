package l1j.server.tempSkillSystem;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class tempSkillSystemLoader {
	private static tempSkillSystemLoader _instance;
	
	// 取得 tempSkillSystemLoader 的單例模式實例
	public static tempSkillSystemLoader getInstance() {
		if(_instance == null)
			_instance = new tempSkillSystemLoader();
		return _instance;
	}
	
	// 重新載入 tempSkillSystemLoader
	public static void reload() {
		if(_instance != null) {
			_instance = new tempSkillSystemLoader();
		}
	}
	
	// 儲存技能系統信息的哈希映射
	private HashMap<Integer, tempSkillSystemInfo> _bonusitem;
	
	// 私有構造函式，初始化加載
	private tempSkillSystemLoader() {
		load();
	}
	
	// 載入技能系統信息
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
	
	// 根據物品 ID 取得技能系統信息
	public tempSkillSystemInfo getTempSkillSystemInfo(int itemId) {
		if (_bonusitem.containsKey(itemId)) {
			return _bonusitem.get(itemId);
		}
		return null;
	}
}
