package l1j.server.MJWebServer.Dispatcher.cache.life;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

class MJLifeDatabaseReader<T> extends MJLifeCacheReader<T> {
	private MJLifeDatabaseAdapter<T> adapter;
	MJLifeDatabaseReader(MJLifeDatabaseAdapter<T> adapter){
		this.adapter = adapter;
	}
	
	@Override
	T read() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		T content = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(adapter.sql());
			adapter.prepared(pstm);
			rs = pstm.executeQuery();
			content = adapter.resultset(rs);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return content;
	}
}
