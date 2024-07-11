package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import java.util.Calendar;

public class MJStatisticInfo {
	public String date;
	public int itemId;
	public int amount;
	public String lowestPrice;
	public String highestPrice;
	public String averagePrice;
	public String formattedDate;
	public MJStatisticInfo(){}
	public MJStatisticInfo(Calendar cal){
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		date = String.format("%04d%02d%02d", cal.get(Calendar.YEAR), month, day);
		formattedDate = String.format("%02d. %02d.", month, day);
	}
}
