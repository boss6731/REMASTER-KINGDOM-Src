package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import java.util.List;

public class MJMyShopSearchResult {
	int totalCount;
	int totalPage;
	int countPerPage;
	List<MJMyShopSearchModel> models;
	
	public int totalCount(){
		return totalCount;
	}
	
	public int totalPage(){
		return totalPage;
	}
	
	public int countPerPage(){
		return countPerPage;
	}
	
	public List<MJMyShopSearchModel> models(){
		return models;
	}
}
