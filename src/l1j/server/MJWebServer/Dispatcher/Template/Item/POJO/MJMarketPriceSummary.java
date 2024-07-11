package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

public class MJMarketPriceSummary {
	public ItemPriceSummary itemPriceSummary;
	public ItemStatisticSummary itemStatisticSummary;
	public MJMarketPriceSummary(){
		itemPriceSummary = new ItemPriceSummary();
		itemStatisticSummary = new ItemStatisticSummary();
	}
	
	public static class ItemPriceSummary{
		public MJNameCodeInfo itemStatus;
		public String lowestPrice;
		public String highestPrice;
		public int publicStoreCount;
		public int secretStoreCount;
		public ItemPriceSummary(){
			itemStatus = MJNameCodeInfo.STATUS.ALL;
		}
	}
	
	public static class ItemStatisticSummary{
		public MJNameCodeInfo itemStatus;
		public String lowestPrice;
		public String highestPrice;
		public String averagePrice;
		public int tradeCount;
		public ItemStatisticSummary(){
			itemStatus = MJNameCodeInfo.STATUS.ALL;
		}
	}
}
