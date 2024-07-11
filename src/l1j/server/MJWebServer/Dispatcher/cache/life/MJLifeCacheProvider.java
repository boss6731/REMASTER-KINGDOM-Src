package l1j.server.MJWebServer.Dispatcher.cache.life;

public class MJLifeCacheProvider {
	public static MJLifeCache lifeCache(){
		return MJLifeCacheServer.lifeCache;
	}
	
	public static <T> MJLifeCacheModel<T> newTimeDatabaseCacheModel(String cacheKey, long lifeTimeMillis, MJLifeDatabaseAdapter<T> adapter){
		return new MJLifeTimeCacheModel<T>(cacheKey, lifeTimeMillis, new MJLifeDatabaseReader<T>(adapter));
	}
	
	public static <T> MJLifeCacheModel<T> newCallCountDatabaseCacheModel(String cacheKey, int lifeCallCount, MJLifeDatabaseAdapter<T> adapter){
		return new MJLifeCallCountCacheModel<T>(cacheKey, lifeCallCount, new MJLifeDatabaseReader<T>(adapter));
	}
}
