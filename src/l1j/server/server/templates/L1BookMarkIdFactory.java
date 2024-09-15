package l1j.server.server.templates;

import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class L1BookMarkIdFactory {
	private static L1BookMarkIdFactory m_instance;
	public static L1BookMarkIdFactory getInstance(){
		if(m_instance == null)
			m_instance = new L1BookMarkIdFactory();
		return m_instance;
	}

	private AtomicInteger m_id;
	private L1BookMarkIdFactory(){
		m_id = new AtomicInteger(load());
	}
	
	public int next_id() {
		return m_id.getAndIncrement();
	}

	private int load() {
		MJObjectWrapper<Integer> wrapper = new MJObjectWrapper<Integer>();
		wrapper.value = 0;
		Selector.exec("SELECT max(id)+1 as newid FROM character_teleport", new FullSelectorHandler() {

			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next())
					wrapper.value = rs.getInt("newid");
			}
		});
		return wrapper.value;
	}
}

