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
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class BuyLimitSystemCharacterTable {
	private static BuyLimitSystemCharacterTable _instance;

	private final Map<String, BuyLimitSystemCharacter> _limit_items = new HashMap<String, BuyLimitSystemCharacter>();

	public static BuyLimitSystemCharacterTable getInstance() {
		if (_instance == null) {
			_instance = new BuyLimitSystemCharacterTable();
		}
		return _instance;
	}
	
	public static void reload() {
		Updator.truncate("buy_limit_item_character");
		BuyLimitSystemCharacterTable oldInstance = _instance;
		_instance = new BuyLimitSystemCharacterTable();
		if(oldInstance != null && oldInstance._limit_items != null)
			oldInstance._limit_items.clear();
	}

	private BuyLimitSystemCharacterTable() {
		Connection con = null;
		PreparedStatement ItemIdPS = null;
		ResultSet ItemIdRS = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			ItemIdPS = con.prepareStatement("SELECT distinct(char_id) as char_id, item_id FROM buy_limit_item_character");
			ItemIdRS = ItemIdPS.executeQuery();
			PreparedStatement ItemsPS = null;
			ResultSet ItemsRS = null;
			while (ItemIdRS.next()) {
				try {
					ItemsPS = con.prepareStatement("SELECT item_id, buy_count, buy_time FROM buy_limit_item_character WHERE char_id=? and item_id=?");
					int charId = ItemIdRS.getInt("char_id");
					int itemId = ItemIdRS.getInt("item_id");
					ItemsPS.setInt(1, charId);
					ItemsPS.setInt(2, itemId);
					BuyLimitSystemCharacter limit_item = new BuyLimitSystemCharacter(charId);
					ItemsRS = ItemsPS.executeQuery();
					while (ItemsRS.next()) {
						limit_item.add(String.format("%d:%d", charId, itemId), ItemsRS.getInt("buy_count"), ItemsRS.getTimestamp("buy_time") != null ? ItemsRS.getTimestamp("buy_time") : null);
					}
					_limit_items.put(String.format("%d:%d", charId, itemId), limit_item);
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
	
	public BuyLimitSystemCharacter getLimitTable(L1PcInstance pc, int itemid) {
		L1ItemInstance item_id = ItemTable.getInstance().createItem(itemid);
		BuyLimitSystemCharacter limit_item = _limit_items.get(String.format("%s:%s", pc.getId(), itemid));
		if (_limit_items.containsKey(String.format("%d:%d", pc.getId(), itemid))) {
			return limit_item;
		}
		BuyLimitSystem.L1BuyLimitItems item = BuyLimitSystem.getInstance().getLimitBuyType(itemid);
		limit_item = new BuyLimitSystemCharacter(pc.getId());
		limit_item.setCount(item.getBuyCount());
		_limit_items.put(String.format("%d:%d", pc.getId(), itemid), limit_item);
		addLimitItem(pc.getId(), pc.getName(), itemid, item_id.getItem().getName(), item.getBuyCount());
		return limit_item;
	}

	public void addLimitItem(int charId, String charname, int itemid, String name, int buycount) {
		Updator.exec("INSERT INTO buy_limit_item_character SET char_id=?, char_name=?, item_id=?, item_name=?, buy_count=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, charId);
				pstm.setString(++idx, charname);
				pstm.setInt(++idx, itemid);
				pstm.setString(++idx, name);
				pstm.setInt(++idx, buycount);
			}
		});
	}

	public void updateLimitItem(L1PcInstance pc, L1ItemInstance item, int buycount, boolean timecheck) {
		Updator.exec("update buy_limit_item_character set char_id=?, char_name=?, item_id=?, item_name=?, buy_count=?, buy_time=? where char_id=? and item_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, pc.getId());
				pstm.setString(++idx, pc.getName());
				pstm.setInt(++idx, item.getItemId());
				pstm.setString(++idx, item.getName());
				pstm.setInt(++idx, buycount);
				pstm.setTimestamp(++idx, timecheck ? new Timestamp(System.currentTimeMillis()) : null);
				pstm.setInt(++idx, pc.getId());
				pstm.setInt(++idx, item.getItemId());
			}
		});
	}
}
