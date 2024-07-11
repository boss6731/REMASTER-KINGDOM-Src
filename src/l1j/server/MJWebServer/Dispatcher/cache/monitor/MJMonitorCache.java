package l1j.server.MJWebServer.Dispatcher.cache.monitor;

public interface MJMonitorCache extends Runnable {	
	
	
	
	
	
	public <T> MJMonitorCacheModel<T> getContent(String key);	
	
	
	
	
	
	public <T> MJMonitorCacheModel<T> getSafeContent(String key, Class<T> classOf);
	
	
	
	
	
	public boolean appendCacheModel(MJMonitorCacheModel<?> model);
	
	
	
	
	
	public Object removeCacheModel(String cacheKey);
	
	
	
	
	
	
}
