package l1j.server.MJTemplate.MJSqlHelper.Clause;

import java.util.LinkedList;

public class MJWhereClauseBuilder {
	public static MJWhereClauseBuilder newBuilder(){
		return new MJWhereClauseBuilder();
	}
	
	private LinkedList<MJWhereClause> clauses;
	private MJWhereClauseBuilder(){
		clauses = new LinkedList<>();
	}
	
	public MJWhereClauseBuilder addClauseBuilder(MJWhereClauseBuilder builder){
		if(builder.clauses != null){
			clauses.addAll(builder.clauses);
		}
		return this;
	}
	
	public MJWhereClauseBuilder addClause(MJWhereClause clause){
		if(clause != null){
			clauses.add(clause);
		}
		return this;
	}
	
	public MJWhereClauseBuilder addIntClause(String column, int value){
		return addClause(MJWhereClause.integerClause(column, value));
	}
	
	public MJWhereClauseBuilder addBooleanClause(String column, boolean value){
		return addClause(MJWhereClause.booleanClause(column, value));
	}
	
	public MJWhereClauseBuilder addBooleanBitClause(String column, boolean value){
		return addClause(MJWhereClause.booleanBitClause(column, value));
	}
	
	public MJWhereClauseBuilder addStringClause(String column, String value){
		return addClause(MJWhereClause.stringClause(column, value));
	}
	
	private MJWhereClause build(boolean and){
		if(clauses.size() <= 0){
			throw new RuntimeException("clauses zeo...");
		}
		return MJWhereClause.compositeClause(and, clauses);
	}
	
	public MJWhereClause or(){
		return build(false);
	}
	
	public MJWhereClause and(){
		return build(true);
	}
}
