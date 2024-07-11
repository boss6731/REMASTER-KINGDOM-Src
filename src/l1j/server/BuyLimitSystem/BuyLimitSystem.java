package l1j.server.BuyLimitSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class BuyLimitSystem {
	private static BuyLimitSystem _instance;
	private Map<Integer, L1BuyLimitItems> _list = new HashMap<Integer, L1BuyLimitItems>();

	public static BuyLimitSystem getInstance() {
		if (_instance == null) {
			_instance = new BuyLimitSystem();
		}
		return _instance;
	}

	public BuyLimitSystem() {
		loadLimitBuySystem(_list);
	}

	public void reload() {
		Map<Integer, L1BuyLimitItems> list = new HashMap<Integer, L1BuyLimitItems>();
		loadLimitBuySystem(list);
		_list = list;
	}

	public void loadLimitBuySystem(Map<Integer, L1BuyLimitItems> list) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1BuyLimitItems limit_items = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM buy_limit_item");
			rs = pstm.executeQuery();
			while (rs.next()) {
				limit_items = new L1BuyLimitItems();
				int itemid = rs.getInt("item_id");
				limit_items.setBuyCount(rs.getInt("buy_count"));
				limit_items.setStartTime(rs.getInt("start_time"));
				limit_items.setEndTime(rs.getInt("end_time"));
				limit_items.setLimitType(LimitType(rs.getString("type")));
				list.put(itemid, limit_items);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public int LimitType(String type) {
		if (type.equalsIgnoreCase("Account-Time"))
			return 1;
		else if (type.equalsIgnoreCase("Character-Time"))
			return 2;
		else if (type.equalsIgnoreCase("Account-1Day"))
			return 3;
		else if (type.equalsIgnoreCase("Character-1Day"))
			return 4;
		else if (type.equalsIgnoreCase("Account-7Day"))
			return 5;
		else if (type.equalsIgnoreCase("Character-7Day"))
			return 6;
		return 0;
	}

	public L1BuyLimitItems getLimitBuyType(int itemid) {
		return (L1BuyLimitItems) _list.get(itemid);
	}

	public class L1BuyLimitItems {
		private int _buy_count;
		private int _start_time;
		private int _end_time;
		private int _limit_type;

		public L1BuyLimitItems() {
		}

		public int getBuyCount() {
			return _buy_count;
		}

		public void setBuyCount(int i) {
			_buy_count = i;
		}

		public int getStartTime() {
			return _start_time;
		}

		public void setStartTime(int i) {
			_start_time = i;
		}
		
		public int getEndTime() {
			return _end_time;
		}

		public void setEndTime(int i) {
			_end_time = i;
		}
		
		public int isLimitType() {
			return _limit_type;
		}
		
		public void setLimitType(int i) {
			_limit_type = i;
		}
	}
}