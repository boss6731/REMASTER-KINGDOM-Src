package l1j.server.MJWebServer.Dispatcher.Template.Cash.POJO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJWebServer.Dispatcher.Template.Character.MJUser;
import l1j.server.server.utils.SQLUtil;

public class AppCashInfo {
	public String _bank_number;
	public String _bank_name;
	public String _user_name;
	public String _character_name;
	public String _account_pass;
	public String _account_secend_pass;
	public String _account_name;

	public static AppCashInfo loadDatabaseCashInfo(String name) {
		AppCashInfo appCash = null;
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM cash_info WHERE account_name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			while (rs.next()) {
				appCash = new AppCashInfo();
				appCash._account_pass = rs.getString("account_pass");
//				appCash._account_secend_pass = rs.getString("account_secend_pass");
				appCash._bank_name = rs.getString("bank_name");
				appCash._bank_number = rs.getString("bank_number");
				appCash._user_name = rs.getString("user_name");
				appCash._account_name = rs.getString("account_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		
		
		return appCash;
	}
	
	public static AppCashInfo chshInfoPaser(Map<String, List<String>> collection, MJUser user) {
		AppCashInfo appCash = new AppCashInfo();

		try {
			Set<String> key = collection.keySet();
			for (Iterator<String> iterator = key.iterator(); iterator.hasNext();) {
				String keyName = (String) iterator.next();
				/**
				 * keyName 값에 json 내용 전달
				 * 이중에 길이가 5 이하인것은 무시하는걸로 _=[1532381042203] 해당내용 무시
				 */
				if (keyName.length() < 5)
					continue;
				
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(keyName);

				JSONObject jsonObj = (JSONObject) obj;

				appCash._bank_number = (String) jsonObj.get("account_num");
				appCash._bank_name = (String) jsonObj.get("bank_name");
				appCash._user_name = (String) jsonObj.get("account_name");
				appCash._account_pass = (String) jsonObj.get("account_pass");
				appCash._account_secend_pass = (String) jsonObj.get("account_second_pass");
				appCash._character_name = user.getCharName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appCash;
	}

	public String getBankNumber() {
		return _bank_number;
	}

	public String getBankName() {
		return _bank_name;
	}

	public String getUserName() {
		return _user_name;
	}

	public String getAccountPass() {
		return _account_pass;
	}

	public String getAccountSecendPass() {
		return _account_secend_pass;
	}

}
