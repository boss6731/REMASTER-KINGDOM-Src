package l1j.server.server.model.item.smelting;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
//인벤 아이템<
public class SmeltingScrollLoader {
	private static SmeltingScrollLoader _instance;
	public static SmeltingScrollLoader getInstance() {
		if(_instance == null)
			_instance = new SmeltingScrollLoader();
		return _instance;
	}
	
	public static void reload() {
		if(_instance != null) {
			_instance = new SmeltingScrollLoader();
		}
	}
	
	private HashMap<Integer, SmeltingScrollInfo> _smeltingscroll;
	private SmeltingScrollLoader() {
		load();
	}
	
	private void load() {
		final HashMap<Integer, SmeltingScrollInfo> bonus = new HashMap<Integer, SmeltingScrollInfo>(256);
		Selector.exec("select * from smelting_scroll", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					SmeltingScrollInfo Info = SmeltingScrollInfo.newInstance(rs);
					if(Info == null)
						continue;
					bonus.put(Info.get_item_id(), Info);
				}
			}
		});
		_smeltingscroll = bonus;
	}
	
	public SmeltingScrollInfo getSmeltingScrollInfo(int itemId) {
		if (_smeltingscroll.containsKey(itemId)) {
			return _smeltingscroll.get(itemId);
		}
		return null;
	}
}
