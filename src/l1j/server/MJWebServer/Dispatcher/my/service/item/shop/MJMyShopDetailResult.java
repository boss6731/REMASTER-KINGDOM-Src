package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import java.util.List;

public class MJMyShopDetailResult<T> {
	int totalCount;
	int totalPage;
	int countPerPage;
	List<T> models;
	
	public int totalCount(){
		return totalCount;
	}
	
	public int totalPage(){
		return totalPage;
	}
	
	public int countPerPage(){
		return countPerPage;
	}
	
	public List<T> models(){
		return models;
	}
}
