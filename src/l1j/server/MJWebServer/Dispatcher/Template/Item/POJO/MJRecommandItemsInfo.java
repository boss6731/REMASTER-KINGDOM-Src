package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import java.util.ArrayList;

public class MJRecommandItemsInfo {
	public ArrayList<MJRecommandItemInfo> itemList;
	public MJRecommandItemsInfo(){
		itemList = new ArrayList<MJRecommandItemInfo>();
	}
	
	public static class MJRecommandItemInfo{
		public int sellNowMaxPrice;
		public int sellNowMinPrice;
		public int itemId;
		public MJPowerBookItem powerbookItem;
		public int buyNowMinPrice;
		public int buyNowMaxPrice;
	}
}
