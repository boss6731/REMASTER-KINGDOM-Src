package l1j.server.QueenAntSystem;

import java.sql.ResultSet;

import javolution.util.FastTable;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1NpcInstance;

public class QueenAntSpawnlistLoader {	
	private static QueenAntSpawnlistLoader _instance;
	public static QueenAntSpawnlistLoader getInstance() {
		if(_instance == null)
			_instance = new QueenAntSpawnlistLoader();
		return _instance;
	}
	
	public FastTable<L1NpcInstance> spawnlist(int type) {
		final FastTable<L1NpcInstance> list = new FastTable<L1NpcInstance>();
		Selector.exec("select * from spawnlist_queen_ant", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					FastTable<L1NpcInstance> pInfo = QueenAntSpawnlistInfo.newInstance(rs, type);
					if(pInfo == null)
						continue;
					list.addAll(pInfo);
				}
			}
		});
		return list;
	}
}
