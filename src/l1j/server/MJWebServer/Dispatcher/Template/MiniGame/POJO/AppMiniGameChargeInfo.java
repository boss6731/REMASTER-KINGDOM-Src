package l1j.server.MJWebServer.Dispatcher.Template.MiniGame.POJO;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import l1j.server.MJWebServer.Dispatcher.Template.Character.MJUser;

public class AppMiniGameChargeInfo {
	public String _coupon;

	public static AppMiniGameChargeInfo CouponInfoPaser(Map<String, List<String>> collection, MJUser user) {
		AppMiniGameChargeInfo appCash = new AppMiniGameChargeInfo();

		try {
			Set<String> key = collection.keySet();
			for (Iterator<String> iterator = key.iterator(); iterator.hasNext();) {
				String keyName = (String) iterator.next();
				if (keyName.length() < 5)
					continue;
				
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(keyName);

				JSONObject jsonObj = (JSONObject) obj;

				appCash._coupon = (String) jsonObj.get("coupon_num");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appCash;
	}
}
