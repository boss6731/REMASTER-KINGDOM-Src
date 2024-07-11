package l1j.server.MJWebServer.Dispatcher.cache.life;

import java.util.concurrent.ConcurrentHashMap;

class MJLifeCacheServer implements MJLifeCache{
	static final MJLifeCacheServer lifeCache = new MJLifeCacheServer();
	
	private ConcurrentHashMap<String, MJLifeCacheModel<?>> cacheModels;
	protected MJLifeCacheServer(){
		cacheModels = new ConcurrentHashMap<>();
	}
	
	private MJLifeCacheModel<?> readContentInternal(String cacheKey){
		MJLifeCacheModel<?> model = cacheModels.get(cacheKey);
		if(model == null){
			return null;
		}
		model.onReadContent();
		return model;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> MJLifeCacheModel<T> getContent(String cacheKey) {
		MJLifeCacheModel<?> model = readContentInternal(cacheKey);
		if(model == null){
			return null;
		}
		
		try{
			return (MJLifeCacheModel<T>)model;
		}catch(ClassCastException e){
			System.out.println(String.format("MJLifeCache invalid class type %s", cacheKey));
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> MJLifeCacheModel<T> getSafeContent(String cacheKey, Class<T> classOf) {
		MJLifeCacheModel<?> model = readContentInternal(cacheKey);
		if(model == null){
			return null;
		}
		
		Object cacheContent = model.cacheContent();
		return cacheContent != null && classOf.isInstance(cacheContent) ? 
				(MJLifeCacheModel<T>)model : null;
	}
	
	@Override
	public boolean appendCacheModel(MJLifeCacheModel<?> model) {
		cacheModels.put(model.cacheKey(), model);
		model.onReadContent();
		return true;
	}
	@Override
	public Object removeCacheModel(String cacheKey) {
		return cacheModels.remove(cacheKey);
	}
}
