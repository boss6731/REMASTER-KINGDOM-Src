package l1j.server.MJWebServer.Dispatcher.my.outside;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.MJWebServer.Dispatcher.my.MJMyController;
import l1j.server.MJWebServer.Dispatcher.my.MJMyUriChainHandler;
import l1j.server.MJWebServer.Dispatcher.my.outside.MJMyOutsideMapped.MJMyOutsideInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMyOutsideChainHandler implements MJMyUriChainHandler{
	private MJMyOutsideMapped mapped;
	private ConcurrentHashMap<String, byte[]> outsideItems;
	public MJMyOutsideChainHandler(){
		outsideItems = new ConcurrentHashMap<>();
		MJMonitorCacheModel<MJMyOutsideMapped> model = MJMonitorCacheProvider.newJsonFileCacheModel("mj-my-outside", "./appcenter/my/json/mj-outside_mapped.json", MJMyOutsideMapped.class, MJEncoding.MS949);
		model.cacheListener(new MJMyOutsideConverter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}
	
	private void onMappedChanged(MJMyOutsideMapped mapped){
		this.mapped = mapped;
		for(MJMyOutsideInfo oInfo : mapped.categories()){
			MJMonitorCacheModel<byte[]> model = MJMonitorCacheProvider.newBinaryFileCacheModel(String.format("mj-my-outside-%s", oInfo.name()), oInfo.localPath());
			model.cacheListener(new MJMyOutsideBinaryConverter(oInfo.uri()));
			MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
		}
	}
	
	private void onBinaryChanged(String uri, byte[] data){
		outsideItems.put(uri, data);
	}

	@Override
	public MJMyController onRequest(MJHttpRequest request, String requestUri) {
		if(mapped == null){
			return null;
		}
		
		byte[] data = outsideItems.get(requestUri);
		if(data == null){
			return null;
		}
		
		return new MJMyOutsideController(request, data);
	}
	
	private class MJMyOutsideConverter implements MJMonitorCacheConverter<MJMyOutsideMapped>{
		@Override
		public MJMyOutsideMapped onNewCached(MJMyOutsideMapped t, long modifiedMillis) {
			onMappedChanged(t);
			return t;
		}
	}
	
	private class MJMyOutsideBinaryConverter implements MJMonitorCacheConverter<byte[]>{
		private String uri;
		private MJMyOutsideBinaryConverter(String uri){
			this.uri = uri;
		}
		@Override
		public byte[] onNewCached(byte[] t, long modifiedMillis) {
			onBinaryChanged(uri, t);
			return null;
		}
	}
}
