package l1j.server.MJTemplate.MJSqlHelper.Clause;

import java.sql.ResultSet;

public interface MJClauseResult<T> {
	public T onResult(ResultSet rs) throws Exception;
}
