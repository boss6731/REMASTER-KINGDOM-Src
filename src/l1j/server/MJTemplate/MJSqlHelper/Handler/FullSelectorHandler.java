package l1j.server.MJTemplate.MJSqlHelper.Handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class FullSelectorHandler extends SelectorHandler{

	@Override
	public void handle(PreparedStatement pstm) {
	}

    public abstract void result(ResultSet rs) throws Exception;
}
