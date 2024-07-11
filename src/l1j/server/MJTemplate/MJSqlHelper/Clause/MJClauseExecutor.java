package l1j.server.MJTemplate.MJSqlHelper.Clause;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class MJClauseExecutor<T>{
	private String headSql;
	private MJWhereClause where;
	private MJGroupClause group;
	private MJOrderClause order;
	private MJLimitClause limit;
	private MJClauseResult<T> result;
	public MJClauseExecutor(String headSql, MJWhereClause where, MJGroupClause group, MJOrderClause order, MJLimitClause limit, MJClauseResult<T> result){
		this.headSql = headSql;
		this.where = where;
		this.group = group;
		this.order = order;
		this.limit = limit;
		this.result = result;
	}
	
	T execute(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql());
			if(where != null){
				where.preparedClause(1, pstm);
			}
			rs = pstm.executeQuery();
			return result.onResult(rs);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	private String sql(){
		StringBuilder sb = new StringBuilder(32);
		sb.append(headSql);
		if(where != null && where.numOfClause() > 0){
			sb.append(" where ");
			sb.append(where.whereClause());
		}
		
		if(group != null){
			sb.append(" group by ");
			sb.append(group.groupClause());
		}
		
		if(order != null){
			sb.append(" order by ");
			sb.append(order.orderClause());
		}
		if(limit != null){
			sb.append(" ");
			sb.append(limit.limitClause());
		}
		return sb.toString();
	}
}
