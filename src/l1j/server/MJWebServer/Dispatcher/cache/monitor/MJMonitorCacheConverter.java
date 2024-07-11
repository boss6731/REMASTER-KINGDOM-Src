package l1j.server.MJWebServer.Dispatcher.cache.monitor;

public interface MJMonitorCacheConverter<T> {
	public T onNewCached(T t, long modifiedMillis);
}
