package l1j.server.Beginner.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;

class MJBeginnerQuestDataModel {
	private HashMap<Integer, MJBeginnerQuestData> models;
	MJBeginnerQuestDataModel(String questModelPath){
		MJMonitorCacheModel<MJBeginnerQuestListData> model = MJMonitorCacheProvider.newJsonFileCacheModel(
				"mj-beginner-quest-model", questModelPath, MJBeginnerQuestListData.class, MJEncoding.MS949);
		
		model.cacheListener(new MJBeginnerQuestModelConverter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}
	
	MJBeginnerQuestData questData(int questId){
		return models.get(questId);
	}
	
	Collection<MJBeginnerQuestData> questValues(){
		return models.values();
	}
	
	private class MJBeginnerQuestModelConverter implements MJMonitorCacheConverter<MJBeginnerQuestListData>{
		@Override
		public MJBeginnerQuestListData onNewCached(MJBeginnerQuestListData t, long modifiedMillis) {
			HashMap<Integer, MJBeginnerQuestData> models = new HashMap<>(t.items().size());
			for(MJBeginnerQuestData data : t.items()){
				data.initialize();
				models.put(data.id(), data);
			}
			MJBeginnerQuestDataModel.this.models = models;
			return null;
		}
	}
	
	private static class MJBeginnerQuestListData {
		private ArrayList<MJBeginnerQuestData> items;
		ArrayList<MJBeginnerQuestData> items(){
			return items;
		}
	}
}
