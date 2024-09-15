package MJNCoinSystem;

import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class MJNCoinIdFactory {
	public static final MJNCoinIdFactory REFUND = new MJNCoinIdFactory("ncoin_trade_refund", "refund_id", 0);
	public static final MJNCoinIdFactory DEPOSIT = new MJNCoinIdFactory("ncoin_trade_deposit", "deposit_id", 0);
	public static final MJNCoinIdFactory ADENA = new MJNCoinIdFactory("ncoin_trade_adena", "trade_id", 5);
	public static final MJNCoinIdFactory[] VALUES = new MJNCoinIdFactory[]{
		REFUND,
		DEPOSIT,
		ADENA,
	};
	
	public static void do_values_load(){
		for(MJNCoinIdFactory factory : VALUES){
			factory.do_load();
		}
		MJNCoinAdenaManager.DEFAULT.do_load();
		MJNCoinSettings.do_load();
		MJNCoinCommandComposite.DEFAULT.load_commands();
	}
	
	private AtomicInteger m_default_id;
	private Object[] m_syncs;
	private String m_table_name;
	private String m_id_column_name;
	private MJNCoinIdFactory(String table_name, String id_column_name, int sync_size){
		if(sync_size > 0){
			m_syncs = new Object[sync_size];
			for(int i=sync_size - 1; i>=0; --i)
				m_syncs[i] = new Object();
		}
		m_default_id = new AtomicInteger(1);
		m_table_name = table_name;
		m_id_column_name = id_column_name;
	}
	
	void do_load(){
		Selector.exec(String.format("select max(%s) as next_id from %s", m_id_column_name, m_table_name), new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next())
					m_default_id.set(rs.getInt("next_id") + 1);
			}
		});
	}
	
	public int next_id() {
		return m_default_id.getAndIncrement();
	}
	
	public Object get_sync_object(int ncoin_id){
		return m_syncs[ncoin_id % m_syncs.length];
	}
}
