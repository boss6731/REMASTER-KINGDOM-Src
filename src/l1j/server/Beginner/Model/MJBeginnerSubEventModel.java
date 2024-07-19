package l1j.server.Beginner.Model;

import java.util.HashMap;

import l1j.server.Beginner.Model.MJBeginnerSubEventData.MJBeginnerSubEventListener;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;

// 定義一個類，用於管理初學者子事件模型
class MJBeginnerSubEventModel {
	private MJBeginnerSubEventData subEventDataModel; // 保存子事件數據模型
	private HashMap<Integer, MJBeginnerSubEventListener> models; // 保存子事件監聽器的映射

	// 構造函數，接受子事件模型路徑作為參數
	MJBeginnerSubEventModel(String subEventModelPath) {
		// 創建一個新的 JSON 文件緩存模型
		MJMonitorCacheModel<MJBeginnerSubEventData> model = MJMonitorCacheProvider.newJsonFileCacheModel(
				"mj-beginner-sub-event-model", subEventModelPath, MJBeginnerSubEventData.class, MJEncoding.MS949);

		// 設置緩存監聽器
		model.cacheListener(new MJBeginnerSubEventModelConverter());
		// 將模型添加到緩存監控器中
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}

	// 返回子事件數據模型
	MJBeginnerSubEventData subEventDataModel() {
		return subEventDataModel;
	}

	// 根據任務 ID 返回對應的子事件監聽器
	MJBeginnerSubEventListener model(int questId) {
		return models.get(questId);
	}

	// 內部類，實現緩存轉換器接口，用於處理新的緩存數據
	private class MJBeginnerSubEventModelConverter implements MJMonitorCacheConverter<MJBeginnerSubEventData> {
		@override
		public MJBeginnerSubEventData onNewCached(MJBeginnerSubEventData t, long modifiedMillis) {
			// 更新子事件數據模型
			subEventDataModel = t;
			// 更新子事件監聽器映射
			models = t.subEventModels();
			return null;
		}
	}
}
