package l1j.server.MJWebServer.Dispatcher.cache.life;

public interface MJLifeCache {
	public <T> MJLifeCacheModel<T> getContent(String cacheKey);
	public <T> MJLifeCacheModel<T> getSafeContent(String cacheKey, Class<T> classOf);
	public boolean appendCacheModel(MJLifeCacheModel<?> model);
	public Object removeCacheModel(String cacheKey);
}
