package l1j.server.Beginner.Model;

import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.Beginner.Model.MJBeginnerCollectData.MJBeginnerCollectDataFromMonsters;
import l1j.server.Beginner.Model.MJBeginnerCollectData.MJBeginnerCollectDataFromNpcTalk;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;

class MJBeginnerCollectModel {
    private MJBeginnerCollectData models;
    MJBeginnerCollectModel(String collectModelPath){
        MJMonitorCacheModel<MJBeginnerCollectListData> model = MJMonitorCacheProvider.newJsonFileCacheModel(
                "mj-beginner-collect-model", collectModelPath, MJBeginnerCollectListData.class, MJEncoding.MS949);

        model.cacheListener(new MJBeginnerCollectModelConverter());
        MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
    }

    MJBeginnerCollectDataFromNpcTalk collectDataFromNpcTalk(int itemAssetId){
        return models.hasCollectsFromNpcTalks() ? models.collectsFromNpcTalks().get(itemAssetId) : null;
    }

    MJBeginnerCollectDataFromMonsters collectDataFromMonsters(int itemAssetId){
        return models.hasCollectsFromMonsters() ? models.collectsFromMonsters().get(itemAssetId) : null;
    }

    private class MJBeginnerCollectModelConverter implements MJMonitorCacheConverter<MJBeginnerCollectListData>{
        @Override
        public MJBeginnerCollectListData onNewCached(MJBeginnerCollectListData t, long modifiedMillis) {
            HashMap<Integer, MJBeginnerCollectDataFromNpcTalk> collectsFromNpcTalks = new HashMap<>(t.collectsFromNpcTalks.size());
            HashMap<Integer, MJBeginnerCollectDataFromMonsters> collectsFromMonsters = new HashMap<>(t.collectsFromMonsters.size());
            for(MJBeginnerCollectDataFromNpcTalk data : t.collectsFromNpcTalks){
                collectsFromNpcTalks.put(data.itemAssetId(), data);
            }
            for(MJBeginnerCollectDataFromMonsters data : t.collectsFromMonsters){
                collectsFromMonsters.put(data.itemAssetId(), data);
            }
            models = new MJBeginnerCollectData(collectsFromNpcTalks, collectsFromMonsters);
            return null;
        }
    }

    private static class MJBeginnerCollectListData{
        private ArrayList<MJBeginnerCollectDataFromNpcTalk> collectsFromNpcTalks;
        private ArrayList<MJBeginnerCollectDataFromMonsters> collectsFromMonsters;
    }
}
