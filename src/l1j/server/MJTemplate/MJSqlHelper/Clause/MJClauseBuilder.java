package l1j.server.MJTemplate.MJSqlHelper.Clause;

import java.util.Collection;
import java.util.LinkedList;

import l1j.server.MJTemplate.MJString;

public class MJClauseBuilder<T> {
	public static <T> MJClauseBuilder<T> newBuilder(){
		return new MJClauseBuilder<T>();
	}
	
	private String table;
	private LinkedList<String> columns;
	private MJWhereClauseBuilder where;
	private MJGroupClause group;
	private MJOrderClauseBuilder order;
	private MJLimitClause limit;
	private MJClauseResult<T> result;
	public MJClauseBuilder(){
		columns = new LinkedList<>();
		table = MJString.EmptyString;
	}
	
	private MJWhereClauseBuilder safeWehere(){
		return where == null ? where = MJWhereClauseBuilder.newBuilder() : where;
	}
	
	private MJWhereClause safeWhereBuild(boolean and){
		return where == null ? null : and ? where.and() : where.or();
	}
	
	private MJOrderClauseBuilder safeOrder(){
		return order == null ? order = MJOrderClauseBuilder.newBuilder() : order;
	}
	
	private MJOrderClause safeOrderBuild(){
		return order == null ? null : order.build();
	}
	
	public MJClauseBuilder<T> table(String table){
		this.table = table;
		return this;
	}
	
	public MJClauseBuilder<T> columns(String column){
		this.columns.add(column);
		return this;
	}
	
	public MJClauseBuilder<T> columns(Collection<String> columns){
		this.columns.addAll(columns);
		return this;
	}
	
	public MJClauseBuilder<T> whereClause(MJWhereClause clause){
		safeWehere().addClause(clause);
		return this;
	}
	
	public MJClauseBuilder<T> whereClause(MJWhereClauseBuilder builder){
		safeWehere().addClauseBuilder(builder);
		return this;
	}
	
	public MJClauseBuilder<T> whereClause(String column, int value){
		return whereClause(MJWhereClause.integerClause(column, value));
	}
	
	public MJClauseBuilder<T> whereClause(String column, boolean value){
		return whereClause(MJWhereClause.booleanClause(column, value));
	}
	
	public MJClauseBuilder<T> whereClauseBoolBit(String column, boolean value){
		return whereClause(MJWhereClause.booleanBitClause(column, value));
	}
	
	public MJClauseBuilder<T> whereClause(String column, String value){
		return whereClause(MJWhereClause.stringClause(column, value));
	}
	
	public MJClauseBuilder<T> orderClause(MJOrderClauseBuilder builder){
		safeOrder().addClauseBuilder(builder);
		return this;
	}
	
	public MJClauseBuilder<T> orderClause(MJOrderClause clause){
		safeOrder().addClause(clause);
		return this;
	}
	
	public MJClauseBuilder<T> orderClause(String column, boolean asc){
		return orderClause(MJOrderClause.simpleClause(column, asc));
	}
	
	public MJClauseBuilder<T> groupClause(MJGroupClause group){
		this.group = group;
		return this;
	}
	
	public MJClauseBuilder<T> limitClause(MJLimitClause limit){
		this.limit = limit;
		return this;
	}
	
	public MJClauseBuilder<T> result(MJClauseResult<T> result){
		this.result = result;
		return this;
	}
	
	private String headSql(){		
		StringBuilder sb = new StringBuilder(32);
		sb.append("select ");
		if(columns.size() <= 0){
			sb.append("* ");
		}else{
			sb.append(MJString.join(",", columns)).append(" ");
		}
		sb.append("from ").append(table);
		return sb.toString();
	}
	
	public T build(boolean whereAnd){
		if(MJString.isNullOrEmpty(table)){
			throw new IllegalArgumentException("table name is empty");
		}
		if(result == null){
			throw new NullPointerException("result listener is null");
		}
		MJClauseExecutor<T> executor = new MJClauseExecutor<T>(headSql(), safeWhereBuild(whereAnd), group, safeOrderBuild(), limit, result);
		return executor.execute();
	}
}
