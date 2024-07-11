package l1j.server.MJTemplate.MJSqlHelper.Clause;

import java.util.LinkedList;

public class MJOrderClauseBuilder {
	public static MJOrderClauseBuilder newBuilder(){
		return new MJOrderClauseBuilder();
	}
	
	private LinkedList<MJOrderClause> clauses;
	private MJOrderClauseBuilder(){
		clauses = new LinkedList<>();
	}
	
	public MJOrderClauseBuilder addClauseBuilder(MJOrderClauseBuilder builder){
		if(builder.clauses != null){
			clauses.addAll(builder.clauses);
		}
		return this;
	}
	
	public MJOrderClauseBuilder addClause(MJOrderClause clause){
		if(clause != null){
			clauses.add(clause);
		}
		return this;
	}
	
	public MJOrderClauseBuilder addClause(String column, boolean asc){
		return addClause(MJOrderClause.simpleClause(column, asc));
	}
	
	public MJOrderClause build(){
		if(clauses.size() <= 0){
			throw new RuntimeException("clauses zeo...");
		}
		return MJOrderClause.compositeClause(clauses);
	}
}
