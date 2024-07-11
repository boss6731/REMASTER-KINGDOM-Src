package l1j.server.MJWebServer.Dispatcher.cache.monitor;

import java.nio.charset.Charset;

public class MJMonitorCacheProvider {
	public static MJMonitorCache monitorCache(){
		return MJMonitorCacheServer.monitorCache;
	}
	
	public static MJMonitorCacheModel<String> newTextFileCacheModel(String cacheKey, String cacheFilePath, Charset charset){
		return MJMonitorFileCacheModel.newTextCache(cacheKey, cacheFilePath, charset);
	}
	
	public static MJMonitorCacheModel<byte[]> newBinaryFileCacheModel(String cacheKey, String cacheFilePath){
		return MJMonitorFileCacheModel.newBinaryCache(cacheKey, cacheFilePath);
	}
	
	public static <T> MJMonitorCacheModel<T> newJsonFileCacheModel(String cacheKey, String cacheFilePath, Class<T> classOf, Charset charset){
		return MJMonitorFileCacheModel.newJsonCache(cacheKey, cacheFilePath, classOf, charset);
	}
	
	public static <T> MJMonitorCacheModel<T> newStaticCacheModel(String cacheKey, T content){
		return new MJStaticCacheModel<T>(cacheKey, content);
	}
	
	private static class MJStaticCacheModel<T> implements MJMonitorCacheModel<T>{
		private String cacheKey;
		private T content;
		private long lastModified;
		private MJStaticCacheModel(String cacheKey, T content){
			this.cacheKey = cacheKey;
			this.content = content;
			this.lastModified = System.currentTimeMillis();
		}
		
		@Override
		public String cacheKey() {
			return cacheKey;
		}

		@Override
		public T cacheContent() {
			return content;
		}

		@Override
		public long lastModified() {
			return lastModified;
		}

		@Override
		public boolean isChanged() {
			return false;
		}

		@Override
		public boolean readContent() {
			return true;
		}

		@Override
		public void cacheListener(MJMonitorCacheConverter<T> cacheListener) {
			throw new UnsupportedOperationException();
		}
	}
}
