package l1j.server.MJWebServer.Dispatcher.cache.life;

class MJLifeCallCountCacheModel<T> extends MJLifeCacheModel<T>{
	private int callCount;
	private int lifeCallCount;
	private T content;
	private long lastModified;
	private MJLifeCacheReader<T> reader;
	protected MJLifeCallCountCacheModel(String cacheKey, int lifeCallCount, MJLifeCacheReader<T> reader){
		super(cacheKey);
		this.callCount = lifeCallCount;
		this.lifeCallCount = lifeCallCount;
		this.content = null;
		this.lastModified = 0;
		this.reader = reader;
	}
	
	private boolean lifeOut(){
		return ++callCount >= lifeCallCount;
	}
	
	@Override
	void onReadContent() {
		if(content == null || lifeOut()){
			content = reader.read();
			callCount = 0;
			lastModified = System.currentTimeMillis();
		}
	}
	
	@Override
	public T cacheContent() {
		return content;
	}
	
	@Override
	public long lastModified() {
		return lastModified;
	}
}
