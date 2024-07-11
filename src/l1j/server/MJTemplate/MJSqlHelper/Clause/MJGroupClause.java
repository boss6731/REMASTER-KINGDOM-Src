package l1j.server.MJTemplate.MJSqlHelper.Clause;

import java.util.Collection;

import l1j.server.MJTemplate.MJString;

public interface MJGroupClause {
	public static MJGroupClause simpleClause(String column){
		return new SimpleClause(column);
	}
	
	public static MJGroupClause compositeClause(Collection<String> column){
		return new CompositeClause(column);
	}
	
	public static MJGroupClause compositeClause(String...column){
		return new CompositeClause0(column);
	}
	
	public String groupClause();
	
	static class SimpleClause implements MJGroupClause{
		private String column;
		SimpleClause(String column){
			this.column = column;		}
		@Override
		public String groupClause() {
			return column;
		}
	}
	
	static class CompositeClause implements MJGroupClause{
		private Collection<String> column;
		CompositeClause(Collection<String> column){
			this.column = column;
		}
		@Override
		public String groupClause() {
			return MJString.join(",", column);
		}
	}
	
	static class CompositeClause0 implements MJGroupClause{
		private String[] column;
		CompositeClause0(String[] column){
			this.column = column;
		}
		@Override
		public String groupClause() {
			return MJString.join(",", column);
		}
	}
}
