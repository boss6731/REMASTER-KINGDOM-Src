package l1j.server.Beginner.Model;

import l1j.server.Beginner.Controller.MJBeginnerControllerProvider;
import l1j.server.Beginner.View.MJBeginnerViewProvider;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;

public class MJBeginnerSettingModel {
	private MJBeginnerSettingData model;
	MJBeginnerSettingModel(String settingModelPath){
		MJMonitorCacheModel<MJBeginnerSettingData> model = MJMonitorCacheProvider.newJsonFileCacheModel(
				"mj-beginner-setting-model", settingModelPath, MJBeginnerSettingData.class, MJEncoding.MS949);
		model.cacheListener(new MJBeginnerSettingModelConverter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}
	
	MJBeginnerSettingData model(){
		return model;
	}
	
	private class MJBeginnerSettingModelConverter implements MJMonitorCacheConverter<MJBeginnerSettingData>{
		@Override
		public MJBeginnerSettingData onNewCached(MJBeginnerSettingData t, long modifiedMillis) {
			model = t;
			MJBeginnerSettingData.changedSettingsHolder(model);
			MJBeginnerControllerProvider.provider().onDevelopModeChanged(model.developMode());
			MJBeginnerViewProvider.provider().onDevelopModeChanged(model.developMode());
			return null;
		}
	}
}
