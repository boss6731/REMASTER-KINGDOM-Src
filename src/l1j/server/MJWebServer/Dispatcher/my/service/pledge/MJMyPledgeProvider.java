package l1j.server.MJWebServer.Dispatcher.my.service.pledge;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseBuilder;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseResult;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJLimitClause;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJOrderClause;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJWhereClause;

class MJMyPledgeProvider {
	private static final MJMyPledgeProvider provider = new MJMyPledgeProvider();
	static MJMyPledgeProvider provider(){
		return provider;
	}
	
	private MJMyPledgeProvider(){
	}
	
	public int numOfPledge(String query){
		return numOfPledge0(queryWhereClause(query));
	}
	
	List<MJMyPledgeInfoModel> selectPledge(String query, MJMyPledgeCategory category, int page, int countPerPage){
		return selectPage0(queryWhereClause(query), category.clause(), MJLimitClause.pagedClauseFromPage(page, countPerPage));
	}
	
	private MJWhereClause queryWhereClause(String query){
		return MJString.isNullOrEmpty(query) ?
				MJWhereClause.emptyClause :
				MJWhereClause.stringLikeClause("clan_name", query);
	}
	
	private int numOfPledge0(MJWhereClause whereClause){
		return new MJClauseBuilder<Integer>()
				.table("clan_data")
				.columns("count(*) as numOfItems")
				.result(new MJMyPledgeCountResult())
				.whereClause(whereClause)
				.build(true);
	}
	
	private List<MJMyPledgeInfoModel> selectPage0(MJWhereClause whereClause, MJOrderClause orderClause, MJLimitClause limitClause){
		return new MJClauseBuilder<List<MJMyPledgeInfoModel>>()
				.table("clan_data")
				.limitClause(limitClause)
				.result(new MJMyPledgeResult())
				.whereClause(whereClause)
				.orderClause(orderClause)
				.build(true);
	}
	
	private static class MJMyPledgeCountResult implements MJClauseResult<Integer>{
		@Override
		public Integer onResult(ResultSet rs) throws Exception {
			return rs.next() ? rs.getInt("numOfItems") : 0;
		}
	}
	
	private static class MJMyPledgeResult implements MJClauseResult<List<MJMyPledgeInfoModel>>{
		@Override
		public List<MJMyPledgeInfoModel> onResult(ResultSet rs) throws Exception {
			List<MJMyPledgeInfoModel> items = new LinkedList<>();
			while(rs.next()){
				MJMyPledgeInfoModel model = MJMyPledgeInfoModel.newInstance(rs);
				items.add(model);
			}
			return items;
		}
		
	}
}
