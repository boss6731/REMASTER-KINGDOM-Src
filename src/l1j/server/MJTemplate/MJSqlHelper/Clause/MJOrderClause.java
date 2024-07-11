package l1j.server.MJTemplate.MJSqlHelper.Clause;

import java.util.Collection;

import l1j.server.MJTemplate.MJString;

public interface MJOrderClause {
	public static MJOrderClause simpleClause(String column, boolean asc){
		return new SimpleClause(column, asc);
	}
	
	public static MJOrderClause compositeClause(MJOrderClause... clauses){
		return new CompositeClause(clauses);
	}
	
	public static MJOrderClause compositeClause(Collection<MJOrderClause> clauses){
		return new CollectionBaseCompositeClause(clauses);
	}
	
	public String orderClause();
	
	static class SimpleClause implements MJOrderClause{
		private final String column;
		private final boolean asc;
		SimpleClause(final String column, final boolean asc){
			this.column = column;
			this.asc = asc;
		}
		
		@Override
		public String orderClause(){
			return new StringBuilder(column.length() + 8)
					.append(column)
					.append(asc ? " asc" : " desc")
					.toString();
		}
	}
	
	static class CompositeClause implements MJOrderClause{
		private final MJOrderClause[] clauses;
		CompositeClause(MJOrderClause[] clauses){
			this.clauses = clauses;
		}
		
		@Override
		public String orderClause() {
			String tok = MJString.EmptyString;
			StringBuilder sb = new StringBuilder(clauses.length * 8);
			for(MJOrderClause clause : clauses){
				sb.append(tok);
				sb.append(clause.orderClause());
				tok = ",";
			}
			return sb.toString();
		}
	}
	
	static class CollectionBaseCompositeClause implements MJOrderClause{
		private final Collection<MJOrderClause> clauses;
		CollectionBaseCompositeClause(Collection<MJOrderClause> clauses){
			this.clauses = clauses;
		}
		
		@Override
		public String orderClause() {
			String tok = MJString.EmptyString;
			StringBuilder sb = new StringBuilder(clauses.size() * 8);
			for(MJOrderClause clause : clauses){
				sb.append(tok);
				sb.append(clause.orderClause());
				tok = ",";
			}
			return sb.toString();
		}
	}
}
