package l1j.server.MJWebServer.Dispatcher.cache.life;

public abstract class MJLifeCacheModel<T> {
	private String cacheKey;
	protected MJLifeCacheModel(String cacheKey){
		this.cacheKey = cacheKey;
	}
	
	public String cacheKey(){
		return cacheKey;
	}
	
	abstract void onReadContent();
	public abstract T cacheContent();
	public abstract long lastModified();
}
