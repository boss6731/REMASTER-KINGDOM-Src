package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.MJTemplate.MJString;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.templates.L1Item;

public class MJItemSearchResult {
	public Pagination pagination;
	public ArrayList<MJItemInfo> list;
	public MJItemSearchResult(){
		pagination = new Pagination();
		list = new ArrayList<MJItemInfo>();
	}
	
	public static class Pagination{
		public int currentPage;
		public int paginationSize;
		public int displayCount;
		public int totalCount;
		public int maxPageNo;
		public int start;
		public int end;
		public boolean previous;
		public boolean next;
		public int previousPage;
		public int nextPage;
	}
	
	public static class MJItemInfo{
		public int itemId;
		public String itemName;
		public int enchant;
		public int info;
		public int serverId;
		public String serverName;
		public String thumbnail;
		public int allSellHighPrice;
		public int allSellLowPrice;
		public int allSellCount;
		public int normalSellHighPrice;
		public int normalSellLowPrice;
		public int normalSellCount;
		public int blessSellHighPrice;
		public int blessSellLowPrice;
		public int blessSellCount;
		public int curseSellHighPrice;
		public int curseSellLowPrice;
		public int curseSellCount;
		public int notIdentSellHighPrice;
		public int notIdentSellLowPrice;
		public int notIdentSellCount;
		public int allBuyHighPrice;
		public int allBuyLowPrice;
		public int allBuyCount;
		public int normalBuyHighPrice;
		public int normalBuyLowPrice;
		public int normalBuyCount;
		public int blessBuyHighPrice;
		public int blessBuyLowPrice;
		public int blessBuyCount;
		public int curseBuyHighPrice;
		public int curseBuyLowPrice;
		public int curseBuyCount;
		public int notIdentBuyHighPrice;
		public int notIdentBuyLowPrice;
		public int notIdentBuyCount;
		public int allSellHiddenCount;
		public int allBuyHiddenCount;
		public int notIdentSellHiddenCount;
		public int notIdentBuyHiddenCount;
		public int normalSellHiddenCount;
		public int normalBuyHiddenCount;
		public int blessSellHiddenCount;
		public int blessBuyHiddenCount;
		public int curseSellHiddenCount;
		public int curseBuyHiddenCount;
		public String allSellHigh = MJString.ZeroString;
		public String allSellLow = MJString.ZeroString;
		public String normalSellHigh = MJString.ZeroString;
		public String normalSellLow = MJString.ZeroString;
		public String blessSellHigh = MJString.ZeroString;
		public String blessSellLow = MJString.ZeroString;
		public String curseSellHigh = MJString.ZeroString;
		public String curseSellLow = MJString.ZeroString;
		public String notIdentSellHigh = MJString.ZeroString;
		public String notIdentSellLow = MJString.ZeroString;
		public String allBuyHigh = MJString.ZeroString;
		public String allBuyLow = MJString.ZeroString;
		public String normalBuyHigh = MJString.ZeroString;
		public String normalBuyLow = MJString.ZeroString;
		public String blessBuyHigh = MJString.ZeroString;
		public String blessBuyLow = MJString.ZeroString;
		public String curseBuyHigh = MJString.ZeroString;
		public String curseBuyLow = MJString.ZeroString;
		public String notIdentBuyHigh = MJString.ZeroString;
		public String notIdentBuyLow = MJString.ZeroString;
		public String displayName;
		public MJItemInfo(){}
		public MJItemInfo(int item_id, int enchant_level){
			L1Item template = ItemTable.getInstance().getTemplate(item_id);
			itemId = item_id;
			itemName = template.getName();
			enchant = enchant_level;
			displayName = String.format("+%d %s", enchant, itemName);
			serverId = 1;
			serverName = Config.Message.GameServerName;
			thumbnail = String.format("%s.png", template.getGfxId());
		}
	}
}
