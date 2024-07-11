package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import java.util.ArrayList;

public class MJItemTradeHistory {
	public ArrayList<ItemTradeHistory> items;
	public MJItemTradeHistory(){
		items = new ArrayList<ItemTradeHistory>();
	}
	public void append_history(ItemTradeHistory history){
		history.index = items.size();
		items.add(history);
	}
	
	public static class ItemTradeHistory{
		public int index;
		public String date;
		public int averagePrice;
	}
}
