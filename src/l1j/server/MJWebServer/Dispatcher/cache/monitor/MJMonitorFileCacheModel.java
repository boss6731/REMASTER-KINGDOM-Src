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
			throw new RuntimeException(String.format("not found file...! %s", cacheFilePath));
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
	 * 파일 데이터를 읽는다.(동기방식.)
	 * 캐시 용도의 리더이기 때문에 간편한 파일 리딩 위주로 사용할 것을 권장(200mb이내)
	 * 소규모 파일에 적합하게 튜닝된 메서드이므로 힙버퍼를 사용한다.
	 * 큰 파일에서 예외가 발생할 수 있다.
	 * 대용량에는 java.nio.file.Files 사용할 것.
	 * @return 읽어들인 바이트 배열
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
