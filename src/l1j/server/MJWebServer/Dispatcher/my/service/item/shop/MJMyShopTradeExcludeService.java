package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1ItemInstance;

class MJMyShopTradeExcludeService {
	private HashMap<Integer, Integer> mExcludeItems;
	MJMyShopTradeExcludeService() {
		loadExcludeItems();
	}
	
	private void loadExcludeItems() {
		final HashMap<Integer, Integer> excludeItems = new HashMap<>();
		Selector.exec("select exclude_item_id from ncoin_trade_item_exclude", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					int excludeItemId = rs.getInt("exclude_item_id");
					excludeItems.put(excludeItemId, excludeItemId);
				}
			}
		});
		mExcludeItems = excludeItems;
	}
	
	boolean containsExcludeItem(L1ItemInstance item) {
		return mExcludeItems.containsKey(item.getItemId());
	}
}
