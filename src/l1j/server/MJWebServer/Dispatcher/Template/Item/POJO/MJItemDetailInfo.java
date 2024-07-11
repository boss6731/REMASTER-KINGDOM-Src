package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

public class MJItemDetailInfo {
	public MJNameCodeInfo[] itemStatusList;
	public boolean isPetnecklace;
	public MJItemResource itemResource;
	public int itemMaxElixir;
	public int itemMaxLevel;
	public MJNameCodeInfo[] itemAttributeList;
	public String itemFavorite;
	
	public MJItemDetailInfo(){
		itemStatusList = MJNameCodeInfo.STATUS.ITEM_STATUS_LIST;
		itemAttributeList = MJNameCodeInfo.ATTRIBUTE.ATTRIBUTE_LIST;
		isPetnecklace = false;
		itemMaxElixir = 0;
		itemMaxLevel = 0;
		itemFavorite = "";
	}
	
	public MJItemDetailInfo(int itemId){
		this();
		itemResource = new MJItemResource(itemId);
	}
}
