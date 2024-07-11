package l1j.server.MJWebServer.Dispatcher.my.service.refund;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import MJNCoinSystem.MJNCoinRefundInfo;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseBuilder;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseResult;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJLimitClause;

class MJMyRefundDatabaseProvider {
	private static final String tableName = "ncoin_trade_refund";
	private static final String numOfItemsColumns = "count(*) as numOfItems";
	
	static int selectRefundCount(final MJMyRefundCategory category, final MJMyRefundSearchColumn searchColumn, final String searchName){
		MJClauseBuilder<Integer> builder = new MJClauseBuilder<Integer>()
				.table(tableName)
				.columns(numOfItemsColumns)
				.result(new MJMyRefundCountResult());
		if(!MJString.isNullOrEmpty(searchName)){
			builder.whereClause(searchColumn.whereClause(searchName));
		}
		return builder.build(true);
	}
	
	static List<MJMyRefundServiceItem> selectRefund(final MJMyRefundCategory category, final MJMyRefundSearchColumn searchColumn, final String searchName, final int page, final int countPerPage){
		MJClauseBuilder<List<MJMyRefundServiceItem>> builder = new MJClauseBuilder<List<MJMyRefundServiceItem>>()
				.table(tableName)
				.limitClause(MJLimitClause.pagedClauseFromPage(page, countPerPage))
				.result(new MJMyRefundListResult());
		if(!category.allView()){
			builder.whereClause(category.whereClause());
		}
		if(!MJString.isNullOrEmpty(searchName)){
			builder.whereClause(searchColumn.whereClause(searchName));
		}
		return builder.build(true);
	}
	
	private MJMyRefundDatabaseProvider(){}
	
	private static class MJMyRefundCountResult implements MJClauseResult<Integer>{
		@Override
		public Integer onResult(ResultSet rs) throws Exception {
			return rs.next() ? rs.getInt("numOfItems") : 0;
		}
	}
	
	private static class MJMyRefundListResult implements MJClauseResult<List<MJMyRefundServiceItem>>{
		@Override
		public List<MJMyRefundServiceItem> onResult(ResultSet rs) throws Exception {
			List<MJMyRefundServiceItem> items = new LinkedList<>();
			while(rs.next()){
				MJNCoinRefundInfo nInfo = MJNCoinRefundInfo.newInstance(rs);
				items.add(MJMyRefundServiceItem.newInstance(nInfo));
			}
			return items;
		}
		
	}
}
