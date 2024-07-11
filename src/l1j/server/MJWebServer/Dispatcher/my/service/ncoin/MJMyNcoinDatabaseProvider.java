package l1j.server.MJWebServer.Dispatcher.my.service.ncoin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import MJNCoinSystem.MJNCoinDepositInfo;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyServiceHelper;

class MJMyNcoinDatabaseProvider{
	static List<MJNCoinDepositInfo> selectDeposit(final MJMyNcoinCategory category, final MJMyNcoinSearchColumn searchColumn, final String searchName, final int page, final int countPerPage){
		MJMyNcoinListSelector selector = new MJMyNcoinListSelector(category, searchColumn, searchName, page, countPerPage);
		Selector.exec(selector.makeQuery(), selector);
		return selector.deposits;
	}
	
	static int selectDepositCount(final MJMyNcoinCategory category, final MJMyNcoinSearchColumn searchColumn, final String searchName){
		MJMyNcoinCountSelector selector = new MJMyNcoinCountSelector(category, searchColumn, searchName);
		Selector.exec(selector.makeQuery(), selector);
		return selector.numOfDeposit;
	}
	
	private MJMyNcoinDatabaseProvider(){}
	
	private static class MJMyNcoinCountSelector extends SelectorHandler{
		private MJMyNcoinCategory category;
		private MJMyNcoinSearchColumn searchColumn; 
		private String searchName;
		private boolean useName;
		private boolean useCategory;
		private boolean useWhereCluase;
		private int numOfDeposit;
		private MJMyNcoinCountSelector(final MJMyNcoinCategory category, final MJMyNcoinSearchColumn searchColumn, final String searchName){
			if(category == null){
				throw new NullPointerException("category is null");			
			}
			if(searchColumn == null){
				throw new NullPointerException("search column is null");
			}
			
			this.numOfDeposit = 0;
			this.category = category;
			this.searchColumn = searchColumn;
			this.searchName = searchName;
			this.useName = !MJString.isNullOrEmpty(searchName);
			this.useCategory = !category.allView();
			this.useWhereCluase = useName || useCategory;
		}
		
		private String makeWhereClause(){
			if(!useWhereCluase){
				return MJString.EmptyString;
			}
			StringBuilder sb = new StringBuilder(128);
			sb.append("where ");
			String and = MJString.EmptyString;
			if(useName){
				sb.append(searchColumn.makeWhereClause());
				and = " and ";
			}
			if(useCategory){
				sb.append(and);
				sb.append(category.whereClause());
			}
			return sb.toString();
		}
		
		private String makeQuery(){
			return String.format("select count(*) as numOfDeposit from ncoin_trade_deposit %s", 
					makeWhereClause());
		}
		
		@Override
		public void handle(PreparedStatement pstm) throws Exception {
			if(useName){
				searchColumn.makePreparedClause(pstm, searchName);
			}
		}
		@Override
		public void result(ResultSet rs) throws Exception {
			if(rs.next()){
				numOfDeposit = rs.getInt("numOfDeposit");
			}
		}
	}
	
	private static class MJMyNcoinListSelector extends SelectorHandler{
		private MJMyNcoinCategory category;
		private MJMyNcoinSearchColumn searchColumn; 
		private String searchName;
		private int countPerPage;
		private int page;
		private boolean useName;
		private boolean useCategory;
		private boolean useWhereCluase;
		private ArrayList<MJNCoinDepositInfo> deposits;
		private MJMyNcoinListSelector(final MJMyNcoinCategory category, final MJMyNcoinSearchColumn searchColumn, final String searchName, final int page, final int countPerPage){
			if(category == null){
				throw new NullPointerException("category is null");			
			}
			if(searchColumn == null){
				throw new NullPointerException("search column is null");
			}
			this.deposits = new ArrayList<MJNCoinDepositInfo>(countPerPage);
			this.category = category;
			this.searchColumn = searchColumn;
			this.searchName = searchName;
			this.page = page;
			this.countPerPage = countPerPage;
			this.useName = !MJString.isNullOrEmpty(searchName);
			this.useCategory = !category.allView();
			this.useWhereCluase = useName || useCategory;
		}
		
		private String makeWhereClause(){
			if(!useWhereCluase){
				return MJString.EmptyString;
			}
			StringBuilder sb = new StringBuilder(128);
			sb.append("where ");
			String and = MJString.EmptyString;
			if(useName){
				sb.append(searchColumn.makeWhereClause());
				and = " and ";
			}
			if(useCategory){
				sb.append(and);
				sb.append(category.whereClause());
			}
			return sb.toString();
		}
		
		private String makeQuery(){
			return String.format("select * from ncoin_trade_deposit %s order by deposit_id desc limit %d,%d", 
					makeWhereClause(), MJMyServiceHelper.calculateOffset(page, countPerPage), countPerPage);
		}

		@Override
		public void handle(PreparedStatement pstm) throws Exception{
			if(useName){
				searchColumn.makePreparedClause(pstm, searchName);
			}
		}

		@Override
		public void result(ResultSet rs) throws Exception {
			while(rs.next()){
				deposits.add(MJNCoinDepositInfo.newInstance(rs));
			}
		}
	}
	
}
