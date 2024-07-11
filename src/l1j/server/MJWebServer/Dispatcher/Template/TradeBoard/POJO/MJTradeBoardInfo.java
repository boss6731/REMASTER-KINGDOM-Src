package l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.POJO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import MJNCoinSystem.MJNCoinSettings;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJWebServer.Dispatcher.Template.Character.MJUser;
import l1j.server.server.utils.SQLUtil;

public class MJTradeBoardInfo {
	public int number;
	public String category;
	public String state;
	public String char_account_name;
	public String title;
	public String contents;
	public int price;
	public String item_name;
	public int item_count;
	public int item_enchant;
	public String item_bless;
	public String item_attr;
	public int item_attr_level;
	public String account_pass;
	public String account_secend_pass;
	public String character_name;
	public double commission;
	public int commission_coin;
	public int result_ncoin;
	
	public static MJTradeBoardInfo tradeBoardInfoDatabasePaser(Map<String, List<String>> collection, int number) {
		MJTradeBoardInfo tbi = null;
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE id=?");
			pstm.setInt(1, number);
			rs = pstm.executeQuery();
			if(rs.next()) {
				tbi = new MJTradeBoardInfo();
				
				tbi.category = rs.getString("category");
				tbi.title = rs.getString("title");
				tbi.state = rs.getString("state");
				tbi.item_count = rs.getInt("item_count");
				tbi.price = rs.getInt("price");
				tbi.char_account_name = rs.getString("char_account_name");
				tbi.item_name = rs.getString("item_name");
				tbi.item_enchant = rs.getInt("item_enchant");
				tbi.item_bless = rs.getString("item_bless");
				tbi.item_attr = rs.getString("item_attr");
				tbi.item_attr_level = rs.getInt("item_attr_level");
				tbi.character_name = rs.getString("character_name");
				
				Set<String> key = collection.keySet();
				for (Iterator<String> iterator = key.iterator(); iterator.hasNext();) {
					String keyName = (String) iterator.next();
					if (keyName.length() < 5)
						continue;
					JSONParser parser = new JSONParser();
					Object obj = parser.parse(keyName);
					JSONObject jsonObj = (JSONObject) obj;
					tbi.account_pass = (String) jsonObj.get("account_pass");
					tbi.account_secend_pass = (String) jsonObj.get("account_second_pass");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return tbi;
	}
	
	public static MJTradeBoardInfo tradeBoardInfoPaser(Map<String, List<String>> collection, MJUser user) {
		MJTradeBoardInfo tbi = null;

		try {
			Set<String> key = collection.keySet();
			for (Iterator<String> iterator = key.iterator(); iterator.hasNext();) {
				String keyName = (String) iterator.next();
				if (keyName.length() < 5)
					continue;

				JSONParser parser = new JSONParser();
				Object obj = parser.parse(keyName);

				JSONObject jsonObj = (JSONObject) obj;

				tbi = new MJTradeBoardInfo();
				tbi.category = (String) jsonObj.get("item_category");
				tbi.title = (String) jsonObj.get("title_name");
				tbi.state = "銷售";
				tbi.price = Integer.valueOf((String) jsonObj.get("item_price"));
				tbi.account_pass = (String) jsonObj.get("account_pass");
				tbi.account_secend_pass = (String) jsonObj.get("account_second_pass");

				if (tbi.category.equalsIgnoreCase("select_adena")) {
					tbi.item_count = Integer.valueOf((String) jsonObj.get("item_count")) * MJNCoinSettings.ADENA_GENERATE_UNIT; // 註冊物品完成時扣除的金額
					tbi.item_name = "金幣";
					tbi.item_enchant = 0;
					tbi.item_bless = "祝福普通";
					tbi.item_attr = "無屬性";
					tbi.item_attr_level = 0;
				} else {
					tbi.item_count = Integer.valueOf((String) jsonObj.get("item_count"));
					tbi.item_name = (String) jsonObj.get("item_name");
					tbi.item_enchant = Integer.valueOf((String) jsonObj.get("item_enchant"));
					tbi.item_bless = (String) jsonObj.get("item_bless");
					tbi.item_attr = (String) jsonObj.get("item_attr");
					tbi.item_attr_level = Integer.valueOf((String) jsonObj.get("item_attr_enchant"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return tbi;
		}
}
