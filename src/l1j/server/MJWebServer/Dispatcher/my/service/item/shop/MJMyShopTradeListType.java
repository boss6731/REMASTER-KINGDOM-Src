package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import l1j.server.MJTemplate.MJSqlHelper.Clause.MJWhereClause;

public enum MJMyShopTradeListType {
	SELLING(new TradeListSellingFactory()),
	SELL_COMPLETE(new TradeListSellCompleteFactory()),
	BUY_COMPLETE(new TradeListBuyCompleteFactory()),
	;
	private TradeListClauseFactory whereFactory;
	MJMyShopTradeListType(TradeListClauseFactory whereFactory){
		this.whereFactory = whereFactory;
	}
	
	MJWhereClause whereClause(final String account){
		return whereFactory.whereClause(account);
	}
	
	public static MJMyShopTradeListType fromName(String name){
		for(MJMyShopTradeListType tradeType : values()){
			if(tradeType.name().equalsIgnoreCase(name)){
				return tradeType;
			}
		}
		return SELLING;
	}
	
	private static abstract class TradeListClauseFactory{
		abstract MJWhereClause whereClause(final String account);
	}
	
	private static class TradeListSellingFactory extends TradeListClauseFactory{
		@Override
		MJWhereClause whereClause(final String account) {
			return MJWhereClause.compositeClause(
					true, 
					MJWhereClause.integerClause("completed", 0),
					MJWhereClause.isNullClause("cons_date"), 
					MJWhereClause.stringClause("reg_account", account)
					);
		}
	}
	
	private static class TradeListSellCompleteFactory extends TradeListClauseFactory{
		@Override
		MJWhereClause whereClause(String account) {
			return MJWhereClause.compositeClause(
					true, 
					MJWhereClause.integerClause("completed", 1),
					MJWhereClause.isNotNullClause("cons_date"), 
					MJWhereClause.stringClause("reg_account", account)
					);
		}
	}
	
	private static class TradeListBuyCompleteFactory extends TradeListClauseFactory{
		@Override
		MJWhereClause whereClause(String account) {
			return MJWhereClause.compositeClause(
					true, 
					MJWhereClause.integerClause("completed", 1),
					MJWhereClause.isNotNullClause("cons_date"), 
					MJWhereClause.stringClause("cons_account", account)
					);
		}
	}
}
