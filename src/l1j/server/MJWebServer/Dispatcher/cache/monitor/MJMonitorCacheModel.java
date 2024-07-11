package l1j.server.MJWebServer.Dispatcher.cache.monitor;

public interface MJMonitorCacheModel<T> {
	String cacheKey();

	
	
	
	T cacheContent();

	
	
	
	long lastModified();

	
	
	
	boolean isChanged(); 

	
	
	
	public boolean readContent();

	
	
	
	void cacheListener(MJMonitorCacheConverter<T> cacheListener);
}
