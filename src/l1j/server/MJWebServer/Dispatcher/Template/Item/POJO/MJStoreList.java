package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import java.util.ArrayList;

public class MJStoreList {
	public ArrayList<ItemPriceDetail> itemPriceDetailList;
	public Pagination pagination;
	public int secretCount;
	public int publicCount;
	public MJStoreList(){
		itemPriceDetailList = new ArrayList<ItemPriceDetail>();
		pagination = new Pagination();
	}
	
	public static class ItemPriceDetail{
		public int itemId;
		public MJNameCodeInfo tradeType;
		public String itemPrice;
		public int count;
		public int enchant;
		public int bless;
		public int ident;
		public int elenchant;
		public MJNameCodeInfo itemAttribute;
		public MJTradeUser tradeUser;
		public int info;
		public MJNameCodeInfo itemStatus;
		public String displayAttributeName;
		public ItemPriceDetail(){}
	}
	
	public static class Pagination{
		public int currentPage;
		public int displayCount;
		public int totalCount;
		public int start;
		public int end;
		public boolean previous;
		public boolean next;
		public int maxPageNo;
	}
}
