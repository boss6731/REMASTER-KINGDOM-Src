package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import l1j.server.MJTemplate.MJSqlHelper.Clause.MJOrderClause;

public enum MJMyShopPriceSort {
	lowPrice(MJOrderClause.simpleClause("low", true)),
	highPrice(MJOrderClause.simpleClause("low", false)),
	;
	private MJOrderClause orderClause;
	MJMyShopPriceSort(MJOrderClause orderClause){
		this.orderClause = orderClause;
	}
	
	MJOrderClause orderClause(){
		return orderClause;
	}
	
	public static MJMyShopPriceSort fromName(String name){
		for(MJMyShopPriceSort sort : values()){
			if(sort.name().equalsIgnoreCase(name)){
				return sort;
			}
		}
		return lowPrice;
	}
	
	
}
