package l1j.server.MJDeathPenalty;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;

public class MJDeathPenaltyService {
	private static final MJDeathPenaltyService service = new MJDeathPenaltyService();
	public static MJDeathPenaltyService service() {
		return service;
	}
	
	private DeathPenaltyInfo model;
	private MJDeathPenaltyService() {
		MJMonitorCacheModel<DeathPenaltyInfo> model = MJMonitorCacheProvider.newJsonFileCacheModel("mj-deathpenalty-setting", "./config/deathpenalty-service.json", DeathPenaltyInfo.class, MJEncoding.MS949);
		model.cacheListener(new DeathPenaltyInfoConverter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}	
	
	public int price_itempenalty() {
		return model.primaryprice_i;
	}
	
	public int need_itemid_itempenalty() {
		return model.need_itemid_i;
	}
	
	public int price_exppenalty() {
		return model.primaryprice_e;
	}
	
	public int need_itemid_expenalty() {
		return model.need_itemid_e;
	}
	
	
	public boolean use() {
		return model.isuse;
	}
	
	static class DeathPenaltyInfo {
		boolean isuse;
		int need_itemid_i;
		int primaryprice_i;
		int need_itemid_e;
		int primaryprice_e;
		DeathPenaltyInfo(){
			isuse = false;
			need_itemid_i = 40308;
			primaryprice_i = 4000000;
			need_itemid_e = 40308;
			primaryprice_e = 4000000;
			
		}
	}
	
	private class DeathPenaltyInfoConverter implements MJMonitorCacheConverter<DeathPenaltyInfo>{
		@Override
		public DeathPenaltyInfo onNewCached(DeathPenaltyInfo t, long modifiedMillis) {
			model = t;
			return null;
		}
	}
}
