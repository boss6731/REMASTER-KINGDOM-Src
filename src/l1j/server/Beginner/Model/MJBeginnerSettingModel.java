package l1j.server.Beginner.Model;

import l1j.server.Beginner.Controller.MJBeginnerControllerProvider;
import l1j.server.Beginner.View.MJBeginnerViewProvider;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;

public class MJBeginnerSettingModel {
	private MJBeginnerSettingData model;

	// 構造函數，接受設置模型的路徑
	MJBeginnerSettingModel(String settingModelPath) {
		// 創建一個新的 JSON 文件緩存模型，指定緩存名稱、路徑、數據類型和編碼
		MJMonitorCacheModel<MJBeginnerSettingData> model = MJMonitorCacheProvider.newJsonFileCacheModel(
				"mj-beginner-setting-model", settingModelPath, MJBeginnerSettingData.class, MJEncoding.MS949);

		// 為緩存模型設置一個緩存監聽器
		model.cacheListener(new MJBeginnerSettingModelConverter());

		// 將緩存模型附加到緩存提供程序的監控緩存中
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}

	// 返回當前的設置數據模型
	MJBeginnerSettingData model() {
		return model;
	}

	// 私有內部類，用於將緩存數據轉換為設置模型數據
	private class MJBeginnerSettingModelConverter implements MJMonitorCacheConverter<MJBeginnerSettingData> {
		@override
		public MJBeginnerSettingData onNewCached(MJBeginnerSettingData t, long modifiedMillis) {
			// 更新當前的設置數據模型
			model = t;

			// 通知設置數據的變更持有者
			MJBeginnerSettingData.changedSettingsHolder(model);

			// 通知控制器提供程序開發模式的變更
			MJBeginnerControllerProvider.provider().onDevelopModeChanged(model.developMode());

			// 通知視圖提供程序開發模式的變更
			MJBeginnerViewProvider.provider().onDevelopModeChanged(model.developMode());

			return null;
		}
	}
}
