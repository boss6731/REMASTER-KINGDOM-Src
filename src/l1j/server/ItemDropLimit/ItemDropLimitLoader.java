package l1j.server.ItemDropLimit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;

public class ItemDropLimitLoader {
	private static ItemDropLimitLoader _instance;
	public static ItemDropLimitLoader getInstance() {
		if(_instance == null)
			_instance = new ItemDropLimitLoader();
		return _instance;
	}
	public static void reload() {
		if(_instance != null) {
			_instance = new ItemDropLimitLoader();
		}
	}	
	
	private HashMap<Integer, ItemDropLimitInfo> _item_list;
	private ItemDropLimitLoader() {
		load();
	}
	
	private void load() {
		final HashMap<Integer, ItemDropLimitInfo> limit_item = new HashMap<Integer, ItemDropLimitInfo>(256);
		Selector.exec("select * from drop_limit_item", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					ItemDropLimitInfo pInfo = ItemDropLimitInfo.newInstance(rs);
					if(pInfo == null)
						continue;
					limit_item.put(pInfo.get_item_id(), pInfo);
				}
			}
		});
		_item_list = limit_item;
	}
	
	public ItemDropLimitInfo getItemId(int itemid) {
		if (_item_list.containsKey(itemid)) {
			return _item_list.get(itemid);
		}
		return null;
	}
	
	public void updateLimitItem(int itemid, int current_count) {
		Updator.exec("update drop_limit_item set item_id=?, current_count=? where item_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, itemid);
				pstm.setInt(++idx, current_count);
				pstm.setInt(++idx, itemid);
			}
		});
	}
	
	public void clearLimitItem() {
		Updator.exec("update drop_limit_item set current_count=? where current_count > 0", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, 0);
			}
		});
	}
	
	public boolean item_drop_limit(int itemid, int count) {
		ItemDropLimitInfo limit = ItemDropLimitLoader.getInstance().getItemId(itemid);
		if (limit != null) {
			if (itemid == limit.get_item_id()) {
				if (limit.get_current_count() >= limit.get_max_count()) {
					limit.set_current_count(limit.get_max_count());
					updateLimitItem(itemid, limit.get_current_count());
					return true;
				} else {
					limit.add_current_count(count);
					updateLimitItem(itemid, limit.get_current_count());
				}
			}
		}
		return false;
	}
}
