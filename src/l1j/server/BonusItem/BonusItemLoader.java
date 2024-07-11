package l1j.server.BonusItem;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class BonusItemLoader {
	private static BonusItemLoader _instance;
	public static BonusItemLoader getInstance() {
		if(_instance == null)
			_instance = new BonusItemLoader();
		return _instance;
	}
	public static void reload() {
		if(_instance != null) {
			_instance = new BonusItemLoader();
		}
	}
	
	
	private HashMap<String, BonusItemInfo> _bonusitem;
	private BonusItemLoader() {
		load();
	}
	
	private void load() {
		final HashMap<String, BonusItemInfo> bonus = new HashMap<String, BonusItemInfo>(256);
		Selector.exec("select * from bonus_item", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					BonusItemInfo pInfo = BonusItemInfo.newInstance(rs);
					if(pInfo == null)
						continue;
					
					String key = new StringBuilder().append(pInfo.get_item_id()).append("|").append(pInfo.get_item_enchant()).toString();
					bonus.put(key, pInfo);
				}
			}
		});
		_bonusitem = bonus;
	}
	
	public BonusItemInfo getBonusItemInfo(int itemId, int enchant) {
		String key = String.format("%d|%d", itemId, enchant);
		if (_bonusitem.containsKey(key)) {
			return _bonusitem.get(key);
		}
		return null;
	}
}
