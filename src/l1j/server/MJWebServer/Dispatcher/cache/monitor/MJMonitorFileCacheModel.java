package l1j.server.MJWebServer.Dispatcher.cache.monitor;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import l1j.server.MJTemplate.MJJsonUtil;

abstract class MJMonitorFileCacheModel<T> implements MJMonitorCacheModel<T>{
	
	static MJMonitorFileCacheModel<String> newTextCache(String cacheKey, String cacheFilePath, Charset charset){
		return new MJTextFileCacheModel(cacheKey, cacheFilePath, charset);
	}
	
	static MJMonitorFileCacheModel<byte[]> newBinaryCache(String cacheKey, String cacheFilePath){
		return new MJBinaryFileCacheModel(cacheKey, cacheFilePath);
	}
	
	
	static <T> MJMonitorFileCacheModel<T> newJsonCache(String cacheKey, String cacheFilePath, Class<T> classOf, Charset charset){
		return new MJJsonFileCacheModel<T>(cacheKey, cacheFilePath, classOf, charset);
	}
	
	
	
	
	private String cacheKey;
	private File cacheFile;
	private long lastModified;
	private T cacheContent;
	private MJMonitorCacheConverter<T> cacheListener;
	
	protected MJMonitorFileCacheModel(String cacheKey, String cacheFilePath){
		File cacheFile = new File(cacheFilePath);
		if(!cacheFile.exists()){
			throw new RuntimeException(String.format("找不到文件...！ %s", cacheFilePath));
		}
		this.cacheKey = cacheKey;
		this.cacheFile = cacheFile;
		this.lastModified = 0;
	}
	
	
	
	
	
	protected abstract T convertContent(byte[] buff);

	
	
	
	
	
	@Override
	public String cacheKey(){
		return cacheKey;
	}

	
	
	
	
	
	@Override
	public T cacheContent(){
		return cacheContent;
	}

	
	
	
	
	@Override
	public long lastModified(){
		return lastModified;
	}

	
	
	
	
	
	@Override
	public boolean isChanged(){
		return lastModified == 0 || cacheFile.lastModified() != lastModified;
	}

	
	
	
	
	@Override
	public void cacheListener(MJMonitorCacheConverter<T> cacheListener){
		this.cacheListener = cacheListener;
	}

	
	
	
	@Override
	public final boolean readContent(){
		byte[] buff = readContentInternal();
		if(buff == null){
			return false;
		}
		T temporaryContent = convertContent(buff);
		cacheContent = cacheListener == null ? 
				temporaryContent : cacheListener.onNewCached(temporaryContent, lastModified);
		return true;
	}






	/**
	 * 讀取文件數據（同步方式）。
	 * 由於是用於緩存的讀取器，建議在簡單的文件讀取中使用（200MB 以內）。
	 * 此方法經過調優，適合小型文件，因此使用了堆緩衝區。
	 * 可能會在大文件中發生異常。
	 * 對於大容量文件，請使用 java.nio.file.Files。
	 * @return 讀取的字節數組
	 **/
	protected byte[] readContentInternal(){
		byte[] buff = null;
		if (!cacheFile.exists())
			return null;

		int length = (int) cacheFile.length();
		try (RandomAccessFile raf = new RandomAccessFile(cacheFile, "r"); 
				FileChannel channel = raf.getChannel()) {

			ByteBuffer buffer = ByteBuffer.allocate(length);
			buffer.clear();
			raf.seek(0);
			channel.read(buffer);
			buff = buffer.array();
			lastModified = cacheFile.lastModified();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buff;
	}
	
	
	
	
	
	private static class MJBinaryFileCacheModel extends MJMonitorFileCacheModel<byte[]> {

		MJBinaryFileCacheModel(String cacheKey, String cacheFilePath){
			super(cacheKey, cacheFilePath);
		}

		@Override
		protected byte[] convertContent(byte[] buff) {
			return buff;
		}
	}
	
	
	
	
	
	
	private static class MJTextFileCacheModel extends MJMonitorFileCacheModel<String>{
		private Charset charset;
		MJTextFileCacheModel(String cacheKey, String cacheFilePath, Charset charset){
			super(cacheKey, cacheFilePath);
			this.charset = charset;
		}

		@Override
		protected String convertContent(byte[] buff) {
			return new String(buff, charset);
		}
		
	}
	
	
	
	
	
	private static class MJJsonFileCacheModel<T> extends MJMonitorFileCacheModel<T>{
		private Class<T> classOf;
		private Charset charset;
		protected MJJsonFileCacheModel(String cacheKey, String cacheFilePath, Class<T> classOf, Charset charset) {
			super(cacheKey, cacheFilePath);
			this.classOf = classOf;
			this.charset = charset;
		}

		@Override
		protected T convertContent(byte[] buff) {
			String s = new String(buff, charset);
			return MJJsonUtil.fromJson(s, classOf);
		}
		
	}
}
