package l1j.server.Beginner.Model;

import java.util.HashMap;

public class MJBeginnerCollectModel {
	private MJBeginnerCollectData models;

	public MJBeginnerCollectModel(String collectModelPath) {
		// 初始化 JSON 文件緩存模型
		MJMonitorCacheModel<MJBeginnerCollectListData> model = MJMonitorCacheProvider.newJsonFileCacheModel(
				"mj-beginner-collect-model", collectModelPath, MJBeginnerCollectListData.class, MJEncoding.MS949);

		// 設置緩存監聽
		model.cacheListener(new MJBeginnerCollectModelConverter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);

		// 確保 models 的初始化
		this.models = new MJBeginnerCollectData(new HashMap<>(), new HashMap<>());
	}

	// 獲取來自 NPC 對話的收集數據
	public MJBeginnerCollectDataFromNpcTalk collectDataFromNpcTalk(int itemAssetId) {
		return models.hasCollectsFromNpcTalks() ? models.getCollectsFromNpcTalks().get(itemAssetId) : null;
	}

	// 獲取來自怪物的收集數據
	public MJBeginnerCollectDataFromMonsters collectDataFromMonsters(int itemAssetId) {
		return models.hasCollectsFromMonsters() ? models.getCollectsFromMonsters().get(itemAssetId) : null;
	}

	// 初學者收集數據模型轉換器
	private class MJBeginnerCollectModelConverter implements MJMonitorCacheConverter<MJBeginnerCollectListData> {
		@override
		public MJBeginnerCollectListData onNewCached(MJBeginnerCollectListData t, long modifiedMillis) {
			// 初始化來自 NPC 對話的收集數據
			HashMap<Integer, MJBeginnerCollectDataFromNpcTalk> collectsFromNpcTalks = new HashMap<>(t.collectsFromNpcTalks.size());
			// 初始化來自怪物的收集數據
			HashMap<Integer, MJBeginnerCollectDataFromMonsters> collectsFromMonsters = new HashMap<>(t.collectsFromMonsters.size());

			// 將收集數據從列表轉換為映射
			for(MJBeginnerCollectDataFromNpcTalk data : t.collectsFromNpcTalks) {
				collectsFromNpcTalks.put(data.getItemAssetId(), data);
			}
			for(MJBeginnerCollectDataFromMonsters data : t.collectsFromMonsters) {
				collectsFromMonsters.put(data.getItemAssetId(), data);
			}

			// 更新模型數據
			models = new MJBeginnerCollectData(collectsFromNpcTalks, collectsFromMonsters);
			return null;
		}
	}
}

// 初學者收集列表數據
private static class MJBeginnerCollectListData {
	// 來自 NPC 對話的收集數據列表
	private ArrayList<MJBeginnerCollectDataFromNpcTalk> collectsFromNpcTalks;
	// 來自怪物的收集數據列表
	private ArrayList<MJBeginnerCollectDataFromMonsters> collectsFromMonsters;
}
