package l1j.server.MJWebServer.Dispatcher.cache.monitor;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.GeneralThreadPool;

class MJMonitorCacheServer implements MJMonitorCache{
	static final MJMonitorCacheServer monitorCache = new MJMonitorCacheServer().executeNext();
	
	private ConcurrentHashMap<String, MJMonitorCacheModel<?>> cacheModels;
	private long updateMillis;
	protected MJMonitorCacheServer(){
		this.cacheModels = new ConcurrentHashMap<>();
		this.updateMillis = 10000L;
	}
	
	protected final void onUpdate(){
		for(MJMonitorCacheModel<?> model : cacheModels.values()){

			/* pass_update 新增的Json檔案會自動更新 */
			for(String cacheKeyName : pass_update) {
				if(model.cacheKey().equalsIgnoreCase(cacheKeyName)) {
					//System.out.println("Json 자동업데이트패스 : " + model.cacheKey());
					continue;
				}
			}
			
			if(readContent(model)){
			}
		}
	}
	
	private String[] pass_update = { "mj-entrance-model"/*,"mj-entrance-model"*/ }; // 자동업데이트를 시키지 않을 Json 셋팅파일이름
	
	protected final boolean readContent(MJMonitorCacheModel<?> model){
		return model.isChanged() ? model.readContent() : false;
	}
	
	protected final MJMonitorCacheServer executeNext(){
		GeneralThreadPool.getInstance().schedule(this, updateMillis);
		return this;
	}
	
	@Override
	public void run() {
		try{
			onUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			executeNext();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> MJMonitorCacheModel<T> getContent(String key) {
		MJMonitorCacheModel<?> model = cacheModels.get(key);
		if(model == null){
			return null;
		}
		try{
			return (MJMonitorCacheModel<T>)model;
		}catch(ClassCastException e){
			System.out.println(String.format("MJMonitorCache invalid class type %s", key));
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> MJMonitorCacheModel<T> getSafeContent(String key, Class<T> classOf) {
		MJMonitorCacheModel<?> model = cacheModels.get(key);
		if(model == null){
			return null;
		}
		
		Object cacheContent = model.cacheContent();
		return cacheContent != null && classOf.isInstance(cacheContent) ? 
				(MJMonitorCacheModel<T>)model : null;
	}
	
	@Override
	public boolean appendCacheModel(MJMonitorCacheModel<?> model) {
		cacheModels.put(model.cacheKey(), model);
		return readContent(model);
	}

	@Override
	public Object removeCacheModel(String cacheKey) {
		return cacheModels.remove(cacheKey);
	}

}
