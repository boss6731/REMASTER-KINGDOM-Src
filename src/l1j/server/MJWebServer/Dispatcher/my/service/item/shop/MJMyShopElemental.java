package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import l1j.server.MJTemplate.MJSqlHelper.Clause.MJWhereClause;

public enum MJMyShopElemental {
	ALL(MJWhereClause.emptyClause(), MJWhereClause.emptyClause()),
	FIRE(MJWhereClause.integerRangeClause("attr", 1, 5), MJWhereClause.integerRangeClause("elemental_enchant", 1, 5)),
	WATER(MJWhereClause.integerRangeClause("attr", 6, 10), MJWhereClause.integerRangeClause("elemental_enchant", 6, 10)),
	EARTH(MJWhereClause.integerRangeClause("attr", 11, 15), MJWhereClause.integerRangeClause("elemental_enchant", 11, 15)),
	WIND(MJWhereClause.integerRangeClause("attr", 16, 20), MJWhereClause.integerRangeClause("elemental_enchant", 16, 20)),
	NONE(MJWhereClause.integerClause("attr", 0), MJWhereClause.integerClause("elemental_enchant", 0)),
	;
	private MJWhereClause clause;
	private MJWhereClause tradeClause;
	MJMyShopElemental(MJWhereClause clause, MJWhereClause tradeClause){
		this.clause = clause;
		this.tradeClause = tradeClause;
	}
	
	MJWhereClause clause(){
		return clause;
	}
	
	MJWhereClause tradeClause(){
		return tradeClause;
	}
	
	public static MJMyShopElemental fromName(String name){
		for(MJMyShopElemental elemental : values()){
			if(elemental.name().equalsIgnoreCase(name)){
				return elemental;
			}
		}
		return NONE;
	}
}
