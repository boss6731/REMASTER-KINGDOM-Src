package l1j.server.MJTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;

public class MJWhiteIP {
	private static MJWhiteIP m_instance;
	public static MJWhiteIP getInstance(){
		if(m_instance == null)
			m_instance = new MJWhiteIP();
		return m_instance;
	}

	private HashSet<String> m_whitelist;
	private Object m_lock;
	private MJWhiteIP(){
		m_lock = new Object();
		HashSet<String> whitelist = new HashSet<String>();
		Selector.exec("select * from whitelist", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					whitelist.add(rs.getString("ip"));
				}
			}
		});
		m_whitelist = whitelist;
	}
	
	public boolean contains(String address) {
		synchronized(m_lock) {
			return m_whitelist.contains(address);
		}
	}
	
	public void put(final String address) {
		synchronized(m_lock) {
			if(m_whitelist.contains(address))
				return;
			
			m_whitelist.add(address);
		}
		Updator.exec("insert ignore into whitelist set ip=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, address);
			}
		});
	}
}
