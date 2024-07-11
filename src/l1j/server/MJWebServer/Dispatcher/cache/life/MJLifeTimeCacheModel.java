package l1j.server.MJWebServer.Dispatcher.cache.life;

class MJLifeTimeCacheModel<T> extends MJLifeCacheModel<T> {
	private long lifeTimeMillis;
	private long lastModified;
	private T content;
	private MJLifeCacheReader<T> reader;
	protected MJLifeTimeCacheModel(String cacheKey, long lifeTimeMillis, MJLifeCacheReader<T> reader){
		super(cacheKey);
		this.lastModified = 0;
		this.content = null;
		this.lifeTimeMillis = lifeTimeMillis;
		this.reader = reader;
	}
	
	private boolean lifeOut(){
		return (System.currentTimeMillis() - lastModified) >= lifeTimeMillis;
	}
	
	@Override
	void onReadContent(){
		if(content == null || lifeOut()){
			content = reader.read();
			lastModified = System.currentTimeMillis();
		}
	}

	@Override
	public T cacheContent(){
		return content;
	}

	@Override
	public long lastModified(){
		return lastModified;
	}
}
