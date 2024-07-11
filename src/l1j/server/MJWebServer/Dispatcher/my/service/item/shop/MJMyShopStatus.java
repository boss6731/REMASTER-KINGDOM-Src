package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJWhereClause;

public enum MJMyShopStatus {
	ALL(
			MJWhereClause.emptyClause(), 
			MJWhereClause.emptyClause()),
	
	NORMAL(
			MJWhereClause.integerClause("iden", MJDShopStorage.getPackIden2AppIden(1)), 
			MJWhereClause.compositeClause(true, MJWhereClause.integerClause("bless", 1), MJWhereClause.booleanBitClause("identified", true))),
	
	BLESS(
			MJWhereClause.integerClause("iden", MJDShopStorage.getPackIden2AppIden(0)), 
			MJWhereClause.compositeClause(true, MJWhereClause.integerClause("bless", 0), MJWhereClause.booleanBitClause("identified", true))),
	
	CURSE(
			MJWhereClause.integerClause("iden", MJDShopStorage.getPackIden2AppIden(2)), 
			MJWhereClause.compositeClause(true, MJWhereClause.integerClause("bless", 2), MJWhereClause.booleanBitClause("identified", true))),
	
	NOT_IDENT(
			MJWhereClause.integerClause("iden", MJDShopStorage.getPackIden2AppIden(-1)), 
			MJWhereClause.booleanBitClause("identified", false)),
	;
	private MJWhereClause clause;
	private MJWhereClause tradeClause;
	MJMyShopStatus(MJWhereClause clause, MJWhereClause tradeClause){
		this.clause = clause;
		this.tradeClause = tradeClause;
		
		
	}
	
	MJWhereClause clause(){
		return clause;
	}
	
	MJWhereClause tradeClause(){
		return tradeClause;
	}
	
	public static MJMyShopStatus fromName(String name){
		for(MJMyShopStatus status : values()){
			if(status.name().equalsIgnoreCase(name)){
				return status;
			}
		}
		return ALL;
	}
			
}
