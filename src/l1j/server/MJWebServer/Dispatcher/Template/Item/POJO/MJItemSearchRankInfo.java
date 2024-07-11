package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

public class MJItemSearchRankInfo {
	public ItemSearchRank itemSearchRank;
	public MJItemResource itemResource;
	
	public MJItemSearchRankInfo(){
	}
	public MJItemSearchRankInfo(int itemId, MJNameCodeInfo trade_type, MJNameCodeInfo category){
		try{
			itemResource = new MJItemResource(itemId);
			itemSearchRank = new ItemSearchRank(trade_type, category, itemResource);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static class ItemSearchRank{
		public int serverId;
		public MJNameCodeInfo tradeType;
		public MJNameCodeInfo itemCategory;
		public int categoryId;
		public int itemId;
		public String itemName;
		public int enchant;
		public int searchCount;
		public int rank;
		public int diff;
		public int info;
		public int elenchant;
		public String displayName;
		public ItemSearchRank(){}
		public ItemSearchRank(MJNameCodeInfo trade_type, MJNameCodeInfo category, MJItemResource resource){
			serverId = 0;
			tradeType = trade_type;
			itemCategory = category;
			categoryId = resource.category.code;
			itemId = resource.id;
			itemName = resource.name;
			enchant = 0;
			searchCount = 0;
			rank = 0;
			diff = 0;
			info = -1;
			elenchant = -1;
			displayName = resource.name;
		}
	}
}
