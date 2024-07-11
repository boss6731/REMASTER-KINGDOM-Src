package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import l1j.server.MJTemplate.MJSqlHelper.Clause.MJOrderClause;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJWhereClause;

public enum MJMyShopTradeType {
	SELL(MJWhereClause.integerClause("type", 0), MJWhereClause.stringClause("trade_type", "SELL"), MJOrderClause.simpleClause("price", true)),
	BUY(MJWhereClause.integerClause("type", 1), MJWhereClause.stringClause("trade_type", "BUY"), MJOrderClause.simpleClause("price", false)),
	;
	
	private MJWhereClause whereClause;
	private MJWhereClause tradeWhereClause;
	private MJOrderClause orderClause;
	private MJMyShopTradeType(MJWhereClause whereClause, MJWhereClause tradeWhereClause, MJOrderClause orderClause){
		this.whereClause = whereClause;
		this.tradeWhereClause = tradeWhereClause;
		this.orderClause = orderClause;
	}
	
	MJWhereClause whereClause(){
		return whereClause;
	}
	
	MJWhereClause tradeWhereClause(){
		return tradeWhereClause;
	}
	
	MJOrderClause orderClause(){
		return orderClause;
	}
	
	public static MJMyShopTradeType fromName(String name){
		for(MJMyShopTradeType tradeType : values()){
			if(tradeType.name().equalsIgnoreCase(name)){
				return tradeType;
			}
		}
		return SELL;
	}
}
