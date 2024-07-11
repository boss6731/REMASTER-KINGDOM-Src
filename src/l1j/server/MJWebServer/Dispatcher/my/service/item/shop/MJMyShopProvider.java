package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

public interface MJMyShopProvider<T> {
	public static final int COUNT_PER_PAGE = 10;
	public MJMyShopStatisticsModel selectStatisticsModel(MJMyShopDetailArgs args);
	public MJMyShopDetailResult<T> selectDetail(MJMyShopDetailArgs args, int page);
	public MJMyShopSearchResult selectSearch(MJMyShopSearchArgs args, int page, String address);
}
