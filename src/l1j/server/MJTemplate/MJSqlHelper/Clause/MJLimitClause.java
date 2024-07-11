package l1j.server.MJTemplate.MJSqlHelper.Clause;

public interface MJLimitClause {
	static MJLimitClause oneRowLimitClause = rowLimitClause(1);
	
	public static MJLimitClause oneRowLimitClause(){
		return oneRowLimitClause;
	}
	
	public static MJLimitClause rowLimitClause(int limitRow){
		return new RowLimitClause(limitRow);
	}
	
	public static MJLimitClause pagedClause(int offset, int countPerPage){
		return new PagedClause(offset, countPerPage);
	}
	
	public static MJLimitClause pagedClauseFromPage(int page, int countPerPage){
		return pagedClause(MJClauseHelper.calculateOffset(page, countPerPage), countPerPage);
	}
	
	public String limitClause();
	
	static class RowLimitClause implements MJLimitClause{
		private final int limitRow;
		RowLimitClause(final int limitRow){
			this.limitRow = limitRow;
		}
		@Override
		public String limitClause() {
			return new StringBuilder(10)
					.append("limit ")
					.append(limitRow)
					.toString();
		}
	}
	
	static class PagedClause implements MJLimitClause{
		private final int offset;
		private final int countPerPage;
		PagedClause(final int offset, final int countPerPage){
			this.offset = offset;
			this.countPerPage = countPerPage;
		}
		
		@Override
		public String limitClause() {
			return new StringBuilder(16)
					.append("limit ")
					.append(offset)
					.append(",")
					.append(countPerPage)
					.toString();
		}
		
	}
}
