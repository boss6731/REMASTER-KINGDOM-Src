package l1j.server.MJWebServer.Dispatcher.PhoneApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class AutoCashResultDatabase {
	private static Logger _log = Logger.getLogger(AutoCashResultDatabase.class.getName());

	private static AutoCashResultDatabase _instance;

	public static AutoCashResultDatabase getIntstance() {
		if (_instance == null) {
			_instance = new AutoCashResultDatabase();
		}
		return _instance;
	}

	private Map<String, AutoCashUserInfo> _user_list = new HashMap<String, AutoCashUserInfo>();
	private Map<Integer, AutoCashInfo> _list = new HashMap<Integer, AutoCashInfo>();

	private AutoCashResultDatabase() {
		loadAutoUserCashResultDatabase(_user_list);
		loadAutoCashDatabase(_list);
	}
	
	public void reload() {
		Map<String, AutoCashUserInfo> user_list = new HashMap<String, AutoCashUserInfo>();
		Map<Integer, AutoCashInfo> list = new HashMap<Integer, AutoCashInfo>();
		
		loadAutoUserCashResultDatabase(user_list);
		loadAutoCashDatabase(list);
		
		_user_list = user_list;
		_list = list;
	}

	private void loadAutoCashDatabase(Map<Integer, AutoCashInfo> list) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		AutoCashInfo aci = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM app_cash_result_info");
			rs = pstm.executeQuery();
			while (rs.next()) {
				aci = new AutoCashInfo();

				int cash = rs.getInt("cash");

				aci.setCash(rs.getInt("cash"));
				aci.setItemId(rs.getInt("item_id"));
				aci.setItemName(rs.getString("item_name"));
				aci.setCount(rs.getInt("item_count"));

				list.put(cash, aci);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void loadAutoUserCashResultDatabase(Map<String, AutoCashUserInfo> list) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		AutoCashUserInfo aci = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_app_cash");
			rs = pstm.executeQuery();
			while (rs.next()) {
				aci = new AutoCashUserInfo();

				String user_name = rs.getString("user_name");
				aci.setCharName(rs.getString("char_name"));
				aci.setAccountName(rs.getString("account_name"));

				list.put(user_name, aci);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void updateLogCashResult(String user_name, AutoCashUserInfo info) {
		Connection con = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sql = "INSERT INTO app_log_cash_result (user_name, char_name, account_name, cash, date) VALUES (?, ?, ?, ?, ?)";
			SQLUtil.execute(con, sql, new Object[] { user_name, info.getCharName(), info.getAccountName(), Integer.valueOf(info.getCash()),
					new Timestamp(System.currentTimeMillis()) });

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
	}
	

	public void updateCashResult(String user_name, AutoCashUserInfo info) {
		Connection con = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sql = "INSERT INTO character_app_cash (user_name, char_name, account_name) VALUES (?, ?, ?)";
			SQLUtil.execute(con, sql, new Object[] { user_name, info.getCharName(), info.getAccountName() });

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
	}

	public void updateCashNotResult(String user_name, AutoCashUserInfo info) {
		Connection con = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sql = "INSERT INTO app_log_cash_not_result (user_name, char_name, account_name, cash) VALUES (?, ?, ?, ?)";
			SQLUtil.execute(con, sql, new Object[] { user_name, info.getCharName(), info.getAccountName(), Integer.valueOf(info.getCash()) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
	}

	public void addAutoCashUserInfo(String name, AutoCashUserInfo info) {
		_user_list.put(name, info);
		updateCashResult(name, info);
	}

	public AutoCashUserInfo getAutoCashUserInfo(String name) {
		return _user_list.get((Object) name);
	}

	public AutoCashInfo getCashInfo(int cash) {
		return _list.get((Object) cash);
	}
}
