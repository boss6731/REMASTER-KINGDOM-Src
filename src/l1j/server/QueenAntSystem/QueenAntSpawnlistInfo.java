package l1j.server.QueenAntSystem;



import javolution.util.FastTable;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1NpcInstance;

import java.sql.ResultSet;

// 女王螞蟻生成列表加載器
public class QueenAntSpawnlistLoader {

	// 單例模式的實例變數
	private static QueenAntSpawnlistLoader _instance;

	// 獲取單例實例的方法
	public static QueenAntSpawnlistLoader getInstance() {
		// 若實例尚未創建，則創建一個新實例
		if (_instance == null) {
			_instance = new QueenAntSpawnlistLoader();
		}
		// 返回實例
		return _instance;
	}

	// 根據類型獲取女王螞蟻的生成列表
	public FastTable<L1NpcInstance> spawnlist(int type) {
		// 創建一個新的 FastTable 來存放生成的 NPC 列表
		final FastTable<L1NpcInstance> list = new FastTable<L1NpcInstance>();

		// 執行 SQL 查詢以從 spawnlist_queen_ant 表中獲取數據
		Selector.exec("select * from spawnlist_queen_ant", new FullSelectorHandler() {
			@override
			public void result(ResultSet rs) throws Exception {
				// 遍歷查詢結果集
				while (rs.next()) {
					// 根據結果集和類型創建新的 NPC 生成信息
					FastTable<L1NpcInstance> pInfo = QueenAntSpawnlistInfo.newInstance(rs, type);
					// 如果生成信息為空，則跳過此次循環
					if (pInfo == null) continue;
					// 將生成的信息添加到列表中
					list.addAll(pInfo);
				}
				
			}
		});

		// 返回生成的 NPC 列表
		return list;
	}
	
}
