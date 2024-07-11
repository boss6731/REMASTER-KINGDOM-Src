package l1j.server.MJWebServer.Dispatcher.cache.life;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface MJLifeDatabaseAdapter<T> {
	String sql();
	void prepared(PreparedStatement pstm);
	T resultset(ResultSet rs) throws SQLException;
}
