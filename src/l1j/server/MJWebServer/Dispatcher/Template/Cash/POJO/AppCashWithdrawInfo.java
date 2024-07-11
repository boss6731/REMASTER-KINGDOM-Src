package l1j.server.MJWebServer.Dispatcher.Template.Cash.POJO;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import l1j.server.MJWebServer.Dispatcher.Template.Character.MJUser;

public class AppCashWithdrawInfo {
	public String _charge_count;
	public String _account_name;
	public String _account_pass;
	public String _account_secend_pass;
	public String _character_name;

	public static AppCashWithdrawInfo chshInfoPaser(Map<String, List<String>> collection, MJUser user) {
		AppCashWithdrawInfo appCash = new AppCashWithdrawInfo();

		try {
			Set<String> key = collection.keySet();
			for (Iterator<String> iterator = key.iterator(); iterator.hasNext();) {
				String keyName = (String) iterator.next();
				if (keyName.length() < 5)
					continue;
				
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(keyName);

				JSONObject jsonObj = (JSONObject) obj;

				appCash._charge_count = (String) jsonObj.get("account_count");
				appCash._account_name = (String) jsonObj.get("account_name");
				appCash._account_pass = (String) jsonObj.get("account_pass");
//				appCash._account_secend_pass = (String) jsonObj.get("account_second_pass");
				appCash._character_name = user.getCharName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appCash;
	}

	public String getChargeCount() {
		return _charge_count;
	}

	public String getAccountName() {
		return _account_name;
	}

	public String getAccountPass() {
		return _account_pass;
	}

	public String getAccountSecendPass() {
		return _account_secend_pass;
	}
}
