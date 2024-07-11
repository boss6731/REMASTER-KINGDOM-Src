package l1j.server.BuyLimitSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.SQLUtil;

public class BuyLimitSystemAccountTable {
	private static BuyLimitSystemAccountTable _instance;

	private final Map<String, BuyLimitSystemAccount> _limit_items = new HashMap<String, BuyLimitSystemAccount>();

	public static BuyLimitSystemAccountTable getInstance() {
		if (_instance == null) {
			_instance = new BuyLimitSystemAccountTable();
		}
		return _instance;
	}
	
	public static void reload() {
		Updator.truncate("buy_limit_item_account");
		BuyLimitSystemAccountTable oldInstance = _instance;
		_instance = new BuyLimitSystemAccountTable();
		if(oldInstance != null && oldInstance._limit_items != null)
			oldInstance._limit_items.clear();
	}

	private BuyLimitSystemAccountTable() {
		Connection con = null;
		PreparedStatement ItemIdPS = null;
		ResultSet ItemIdRS = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			ItemIdPS = con.prepareStatement("SELECT distinct(account) as account, item_id FROM buy_limit_item_account");
			ItemIdRS = ItemIdPS.executeQuery();
			PreparedStatement ItemsPS = null;
			ResultSet ItemsRS = null;
			while (ItemIdRS.next()) {
				try {
					ItemsPS = con.prepareStatement("SELECT item_id, buy_count, buy_time FROM buy_limit_item_account WHERE account=? and item_id=?");
					String account = ItemIdRS.getString("account");
					int itemId = ItemIdRS.getInt("item_id");
					ItemsPS.setString(1, account);
					ItemsPS.setInt(2, itemId);
					ItemsRS = ItemsPS.executeQuery();
					BuyLimitSystemAccount limit_item = new BuyLimitSystemAccount(account);
					while (ItemsRS.next()) {
						limit_item.add(String.format("%s:%s", account, itemId), ItemsRS.getInt("buy_count"), ItemsRS.getTimestamp("buy_time") != null ? ItemsRS.getTimestamp("buy_time") : null);
					}
					_limit_items.put(String.format("%s:%s", account, itemId), limit_item);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					SQLUtil.close(ItemsRS);
					SQLUtil.close(ItemsPS);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(ItemIdRS);
			SQLUtil.close(ItemIdPS);
			SQLUtil.close(con);
		}
	}

	public BuyLimitSystemAccount getLimitTable(String account, int itemid) {
		L1ItemInstance item_id = ItemTable.getInstance().createItem(itemid);
		BuyLimitSystemAccount limit_item = _limit_items.get(String.format("%s:%d", account, itemid));
		if (_limit_items.containsKey(String.format("%s:%d", account, itemid))) {
			return limit_item;
		}
		BuyLimitSystem.L1BuyLimitItems item = BuyLimitSystem.getInstance().getLimitBuyType(itemid);
		limit_item = new BuyLimitSystemAccount(account);
		limit_item.setCount(item.getBuyCount());
		_limit_items.put(String.format("%s:%d", account, itemid), limit_item);
		addLimitItem(account, itemid, item_id.getItem().getName(), item.getBuyCount());
		return limit_item;
	}

	public void addLimitItem(String account, int itemid, String name, int buycount) {
		Updator.exec("insert into buy_limit_item_account set account=?, item_id=?, item_name=?, buy_count=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, account);
				pstm.setInt(++idx, itemid);
				pstm.setString(++idx, name);
				pstm.setInt(++idx, buycount);
			}
		});
	}
	
	public void updateLimitItem(String account, L1ItemInstance item, int buycount, boolean timecheck) {
		Updator.exec("update buy_limit_item_account set account=?, item_id=?, item_name=?, buy_count=?, buy_time=? where account=? and item_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, account);
				pstm.setInt(++idx, item.getItemId());
				pstm.setString(++idx, item.getName());
				pstm.setInt(++idx, buycount);
				pstm.setTimestamp(++idx, timecheck ? new Timestamp(System.currentTimeMillis()) : null);
				pstm.setString(++idx, account);
				pstm.setInt(++idx, item.getItemId());
			}
		});
	}
}
