package l1j.server.Beginner.Model;

import java.util.HashMap;

import l1j.server.Beginner.Model.MJBeginnerSubEventData.MJBeginnerSubEventListener;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;

class MJBeginnerSubEventModel {
	private MJBeginnerSubEventData subEventDataModel;
	private HashMap<Integer, MJBeginnerSubEventListener> models;
	MJBeginnerSubEventModel(String subEventModelPath){
		MJMonitorCacheModel<MJBeginnerSubEventData> model = MJMonitorCacheProvider.newJsonFileCacheModel(
				"mj-beginner-sub-event-model", subEventModelPath, MJBeginnerSubEventData.class, MJEncoding.MS949);
		model.cacheListener(new MJBeginnerSubEventModelConverter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}
	
	MJBeginnerSubEventData subEventDataModel(){
		return subEventDataModel;
	}
	
	MJBeginnerSubEventListener model(int questId){
		return models.get(questId);
	}
	
	private class MJBeginnerSubEventModelConverter implements MJMonitorCacheConverter<MJBeginnerSubEventData>{
		@Override
		public MJBeginnerSubEventData onNewCached(MJBeginnerSubEventData t, long modifiedMillis) {
			subEventDataModel = t;
			models = t.subEventModels();
			return null;
		}
		
	}
}
