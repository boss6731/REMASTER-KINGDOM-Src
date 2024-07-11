package l1j.server.MJWebServer.Dispatcher.my.service.refund;

import l1j.server.MJTemplate.MJSqlHelper.Clause.MJWhereClause;

public enum MJMyRefundSearchColumn {
	all("*", null),
	refundName("refund_name", new SimpleNameClauseFactory()),
	characterName("character_name", new SimpleNameClauseFactory()),
	accountName("account_name", new SimpleNameClauseFactory()),
	bankName("bank_name", new SimpleNameClauseFactory()),
	;
	
	private String searchColumn;
	private ClauseFactory clauseFactory;
	MJMyRefundSearchColumn(String searchColumn, ClauseFactory clauseFactory){
		this.searchColumn = searchColumn;
		this.clauseFactory = clauseFactory;
	}
	
	String searchColumn(){
		return searchColumn;
	}
	
	public MJWhereClause whereClause(String value){
		if(clauseFactory != null){
			return clauseFactory.clause(searchColumn, value);
		}
		return MJWhereClause.compositeClause(false,
				refundName.whereClause(value),
				characterName.whereClause(value),
				accountName.whereClause(value),
				bankName.whereClause(value)
				);
	}
	
	
	private static abstract class ClauseFactory{
		abstract MJWhereClause clause(String columnName, String value);
	}
	
	private static class SimpleNameClauseFactory extends ClauseFactory{
		SimpleNameClauseFactory(){
		}
		@Override
		MJWhereClause clause(String columnName, String value) {
			return MJWhereClause.compositeClause(false, 
					MJWhereClause.stringClause(columnName, value),
					MJWhereClause.stringLikeClause(columnName, value)
					);
		}
	}
	
	public static MJMyRefundSearchColumn fromColumn(String searchColumn){
		for(MJMyRefundSearchColumn c : values()){
			if(c.searchColumn().equalsIgnoreCase(searchColumn)){
				return c;
			}
		}
		return all;
	}
}
