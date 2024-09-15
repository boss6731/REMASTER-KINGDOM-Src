package MJNCoinSystem;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJSqlHelper.Property.MJIPropertyHandler;
import l1j.server.MJTemplate.MJSqlHelper.Property.MJSqlPropertyReader;

public class MJNCoinSettings {
	public static int REFUND_GENERATE_MIN;
	public static int DEPOSIT_LETTER_ID;
	public static int ADENA_GENERATE_UNIT;
	public static int ADENA_GENERATE_MAX;
	public static int ADENA_MARKET_PRICE;
	public static double GM_COMMISSION; 
	public static double USER_COMMISSION;
	public static double CHARGE_GENERATEMIN;
	public static int ADENA_MARKET_STATISTICS;
	public static int GM_Purchase_Count;
	public static int GM_Cash_Count;
	
	public static void do_load(){
		MJSqlPropertyReader.do_exec("ncoin_trade_settings", "section", "val", new MJIPropertyHandler(){
			@Override
			public void on_load(String section, MJSqlPropertyReader reader, ResultSet rs) throws SQLException {
				switch(section){
				case "RefundGenerateMin":
					REFUND_GENERATE_MIN = reader.read_int(rs);
					break;
				case "DepositLetterId":
					DEPOSIT_LETTER_ID = reader.read_int(rs);
					break;	
				case "AdenaGenerateUnit":
					ADENA_GENERATE_UNIT = reader.read_int(rs);
					break;
				case "AdenaGenerateMax":
					ADENA_GENERATE_MAX = reader.read_int(rs);
					break;
				case "AdenaMarketPrice":
					ADENA_MARKET_PRICE = reader.read_int(rs);
					break;
				case "GMCommission":
					GM_COMMISSION = reader.read_double(rs);
					break;
				case "USERCommission":
					USER_COMMISSION = reader.read_double(rs);
					break;
				case "ChargeGenerateMin":
					CHARGE_GENERATEMIN = reader.read_int(rs);
					break;
				case "AdenaMarketStatistics":
					ADENA_MARKET_STATISTICS = reader.read_int(rs);
					break;
				case "GMPurchaseCount":
					GM_Purchase_Count = reader.read_int(rs);
					break;
				case "GMCashCount":
					GM_Cash_Count = reader.read_int(rs);
					break;
					
				}
			}
		});
	}
}
