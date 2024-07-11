package l1j.server.MJNetServer.ClientManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;

public class MJNSDenialAddress {
	public static final int REASON_WELL_KNOWN = 0;            // 知名端口
	public static final int REASON_CONNECTION_OVER = 1;        // 連接數過多
	public static final int REASON_SIZE_OVER = 2;            // 數據包大小過大
	public static final int REASON_FILTER_HANDSHAKE = 3;    // 版本握手
	public static final int REASON_WEB_URI_LENGTH_OVER = 4;    // 網頁 URI 長度異常
	public static final int REASON_WEB_ACCESS_OVER = 5;        // 網頁過度訪問
	
	private static MJNSDenialAddress m_instance;
	public static MJNSDenialAddress getInstance(){
		if(m_instance == null)
			m_instance = new MJNSDenialAddress();
		return m_instance;
	}
	
	public static void reload() {
		MJNSDenialAddress old = m_instance;
		m_instance = new MJNSDenialAddress();
		if(old != null) 
			old.dispose();
	}

	private HashMap<String, Integer> m_denials;
	private Object m_lock;
	private MJNSDenialAddress(){
		m_denials = do_load();
		m_lock = new Object();
	}

	private HashMap<String, Integer> do_load(){
		HashMap<String, Integer> denials = new HashMap<String, Integer>();
		Selector.exec("select * from netsafe_denials", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) 
					denials.put(rs.getString("address"), rs.getInt("reason"));
			}
		});
		
		Selector.exec("select * from netsafe_denials_handshake", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) 
					denials.put(rs.getString("address"), rs.getInt("reason"));
			}
		});
		
		return denials;
	}
	
	public void delete_denials_address(final String address) {
		synchronized(m_lock) {
			m_denials.remove(address);
		}
		Updator.exec("delete from netsafe_denials where address=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, address);
			}
		});
	}

	public boolean is_denials_address(String address) {
		synchronized(m_lock) {
			return m_denials.containsKey(address);
		}
	}

	public void insert_address(final String address, final int reason) {
		synchronized(m_lock) {
			if(m_denials.containsKey(address))
				return;
			Timestamp time = new Timestamp(System.currentTimeMillis());
			Updator.exec("insert into netsafe_denials set address=?, reason=?, datetime=?", new Handler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, address);
					pstm.setInt(2, reason);
					pstm.setTimestamp(3, time);
				}
			});
			m_denials.put(address, reason);
		}
	}
	
	public void insert_address_handshake(final String address, final int reason) {
		synchronized(m_lock) {
			if(m_denials.containsKey(address))
				return;
			Timestamp time = new Timestamp(System.currentTimeMillis());
			Updator.exec("insert into netsafe_denials_handshake set address=?, reason=?, datetime=?", new Handler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, address);
					pstm.setInt(2, reason);
					pstm.setTimestamp(3, time);
				}
			});
			m_denials.put(address, reason);
		}
	}

	public void dispose() {
		m_denials = null;
	}
	
}
