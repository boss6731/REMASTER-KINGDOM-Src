package l1j.server.Beginner.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;

class MJBeginnerQuestDataModel {
	private HashMap<Integer, MJBeginnerQuestData> models; // 存儲任務數據的哈希表

	// 構造函數，初始化任務數據模型
	MJBeginnerQuestDataModel(String questModelPath) {
		MJMonitorCacheModel<MJBeginnerQuestListData> model = MJMonitorCacheProvider.newJsonFileCacheModel(
				"mj-beginner-quest-model", questModelPath, MJBeginnerQuestListData.class, MJEncoding.MS949);

		model.cacheListener(new MJBeginnerQuestModelConverter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}

	// 根據任務ID獲取任務數據
	MJBeginnerQuestData questData(int questId) {
		return models.get(questId);
	}

	// 獲取所有任務數據的集合
	Collection<MJBeginnerQuestData> questValues() {
		return models.values();
	}

	// 內部類，處理任務數據模型的轉換
	private class MJBeginnerQuestModelConverter implements MJMonitorCacheConverter<MJBeginnerQuestListData> {
		@override
		public MJBeginnerQuestListData onNewCached(MJBeginnerQuestListData t, long modifiedMillis) {
			HashMap<Integer, MJBeginnerQuestData> models = new HashMap<>(t.items().size());
			for (MJBeginnerQuestData data : t.items()) {
				data.initialize();
				models.put(data.id(), data);
			}
			MJBeginnerQuestDataModel.this.models = models;
			return null;
		}
	}

	// 內部靜態類，表示任務列表數據
	private static class MJBeginnerQuestListData {
		private ArrayList<MJBeginnerQuestData> items; // 任務數據列表

		// 獲取任務數據列表
		ArrayList<MJBeginnerQuestData> items() {
			return items;
		}
	}
}
