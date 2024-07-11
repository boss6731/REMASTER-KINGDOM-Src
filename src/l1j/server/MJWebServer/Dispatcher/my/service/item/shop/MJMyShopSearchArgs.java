package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

public class MJMyShopSearchArgs {
	public String keyword;
	public MJMyShopPriceSort priceSort;
	public MJMyShopSearchArgs(){
		priceSort = MJMyShopPriceSort.lowPrice;
	}
}
