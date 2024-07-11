package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

public class MJMyShopDetailArgs {
	public String itemName;
	public int enchant;
	public MJMyShopStatus status;
	public MJMyShopElemental elemental;
	public MJMyShopTradeType tradeType;
	public MJMyShopDetailArgs(){
		status = MJMyShopStatus.ALL;
		elemental = MJMyShopElemental.ALL;
		tradeType = MJMyShopTradeType.SELL;
	}
}
