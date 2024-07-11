package l1j.server.MJWebServer.Dispatcher.my.page;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJPath;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.MJWebServer.Dispatcher.my.MJMyController;
import l1j.server.MJWebServer.Dispatcher.my.MJMyUriChainHandler;
import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageMapped.MJMyPageInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyApiPageService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMyPageChainHandler implements MJMyUriChainHandler{
	private static final HashMap<String, MJMyPageControllerFactory> factories;
	static{
		factories = new HashMap<>();
		factories.put("default", new MJMyPageControllerFactory(){
			@Override
			public MJMyPageController controller(MJHttpRequest request, MJMyPageInfo pInfo) {
				return new MJMyPageDefaultController(request, pInfo);
			}
		});
		factories.put("gm-command", new MJMyPageControllerFactory(){
			@Override
			public MJMyPageController controller(MJHttpRequest request, MJMyPageInfo pInfo) {
				return new MJMyPageGmCommandController(request, pInfo);
			}
		});
		factories.put("item-detail", new MJMyPageControllerFactory(){
			@Override
			public MJMyPageController controller(MJHttpRequest request, MJMyPageInfo pInfo) {
				return new MJMyPagePrivateShopDetailController(request, pInfo);
			}
		});
		factories.put("item-search", new MJMyPageControllerFactory(){
			@Override
			public MJMyPageController controller(MJHttpRequest request, MJMyPageInfo pInfo) {
				return new MJMyPagePrivateShopSearchController(request, pInfo);
			}
		});
		factories.put("trade-detail", new MJMyPageControllerFactory(){
			@Override
			public MJMyPageController controller(MJHttpRequest request, MJMyPageInfo pInfo) {
				return new MJMyPageTradeShopDetailController(request, pInfo);
			}
		});
		factories.put("trade-search", new MJMyPageControllerFactory(){
			@Override
			public MJMyPageController controller(MJHttpRequest request, MJMyPageInfo pInfo) {
				return new MJMyPageTradeShopSearchController(request, pInfo);
			}
		});
		factories.put("trade-registered", new MJMyPageControllerFactory(){
			@Override
			public MJMyPageController controller(MJHttpRequest request, MJMyPageInfo pInfo) {
				return new MJMyPageTradeShopRegController(request, pInfo);
			}
		});
		factories.put("trade-me", new MJMyPageControllerFactory(){
			@Override
			public MJMyPageController controller(MJHttpRequest request, MJMyPageInfo pInfo) {
				return new MJMyPageTradeMeController(request, pInfo);
			}
		});
	}
	
	private MJMyPageMapped mapped;
	private ConcurrentHashMap<String, MJMyPageCacheConverter<?>> pageItems;
	public MJMyPageChainHandler(){
		pageItems = new ConcurrentHashMap<>();
		MJMonitorCacheModel<MJMyPageMapped> model = 
				MJMonitorCacheProvider.newJsonFileCacheModel("mj-my-page", "./appcenter/my/json/mj-page_mapped.json", MJMyPageMapped.class, MJEncoding.MS949);
		model.cacheListener(new MJMyPageConverter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}
	
	private void onMappedChanged(MJMyPageMapped mapped){
		this.mapped = mapped;
		for(MJMyPageInfo pInfo : mapped.categories()){
			if(pInfo.binaryCache()){
				makeBinaryCache(pInfo);
			}else{
				makeTextCache(pInfo);
			}
		}
		MJMyApiPageService.service().onMenuChanged(mapped);
	}
	
	private void makeBinaryCache(MJMyPageInfo pInfo){
		MJMonitorCacheModel<byte[]> model = MJMonitorCacheProvider.newBinaryFileCacheModel(String.format("mj-my-page-%s", MJPath.getFileName(pInfo.localPath())), pInfo.localPath());
		model.cacheListener(new MJMyPageBinaryConverter(pInfo));
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}
	
	private void makeTextCache(MJMyPageInfo pInfo){
		MJMonitorCacheModel<String> model = MJMonitorCacheProvider.newTextFileCacheModel(String.format("mj-my-page-%s", MJPath.getFileName(pInfo.localPath())), pInfo.localPath(), MJEncoding.UTF8);
		model.cacheListener(new MJMyPageTextConverter(pInfo));
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}
	
	private void onCacheConverterChanged(String uri, MJMyPageCacheConverter<?> converter){
		pageItems.put(uri, converter);
	}
	
	@Override
	public MJMyController onRequest(MJHttpRequest request, String requestUri) {
		if(mapped == null){
			return null;
		}
		MJMyPageCacheConverter<?> converter = pageItems.get(requestUri);
		if(converter == null || !converter.checkContent()){
			return null;
		}
		
		String controller = converter.pInfo.controller();
		if(MJString.isNullOrEmpty(controller)){
			System.out.println(String.format("empty controller name!!!\r\nname : %s, category : %d", converter.pInfo.name(), converter.pInfo.category()));
			return null;
		}
		
		MJMyPageControllerFactory factory = factories.get(controller);
		if(factory == null){
			System.out.println(String.format("not found controller factory!!!\r\ncontrollerName : %s, name : %s, category : %d", 
					controller, converter.pInfo.name(), converter.pInfo.category()));
			return null;			
		}
		return factory.controller(request, converter.pInfo);
	}

	private class MJMyPageConverter implements MJMonitorCacheConverter<MJMyPageMapped>{
		@Override
		public MJMyPageMapped onNewCached(MJMyPageMapped t, long modifiedMillis) {
			onMappedChanged(t);
			return t;
		}
	}
	
	private abstract class MJMyPageCacheConverter<T> implements MJMonitorCacheConverter<T>{
		MJMyPageInfo pInfo;
		protected MJMyPageCacheConverter(MJMyPageInfo pInfo){
			this.pInfo = pInfo;
		}
		
		abstract boolean checkContent();
	}
	
	private class MJMyPageBinaryConverter extends MJMyPageCacheConverter<byte[]>{
		protected MJMyPageBinaryConverter(MJMyPageInfo pInfo) {
			super(pInfo);
		}

		@Override
		public byte[] onNewCached(byte[] t, long modifiedMillis) {
			pInfo.pageBinary = t;
			onCacheConverterChanged(pInfo.uri(), this);
			return null;
		}

		@Override
		boolean checkContent() {
			return pInfo.pageBinary != null;
		}
	}
	
	private class MJMyPageTextConverter extends MJMyPageCacheConverter<String>{
		protected MJMyPageTextConverter(MJMyPageInfo pInfo) {
			super(pInfo);
		}
		
		@Override
		public String onNewCached(String t, long modifiedMillis) {
			pInfo.pageText = t;
			onCacheConverterChanged(pInfo.uri(), this);
			return null;
		}

		@Override
		boolean checkContent() {
			return !MJString.isNullOrEmpty(pInfo.pageText);
		}
	}
}
