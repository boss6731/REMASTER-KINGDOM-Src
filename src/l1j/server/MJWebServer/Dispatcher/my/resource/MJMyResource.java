package l1j.server.MJWebServer.Dispatcher.my.resource;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.Keyword.MJKeywordModelFactory;
import l1j.server.MJTemplate.Keyword.MJKeywordModelProvider;
import l1j.server.MJWebServer.MJWebServerProvider;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.MJWebServer.Dispatcher.my.MJMyUriChain;
import l1j.server.MJWebServer.Dispatcher.my.api.MJMyApiChainHandler;
import l1j.server.MJWebServer.Dispatcher.my.file.MJMyFileChainHandler;
import l1j.server.MJWebServer.Dispatcher.my.outside.MJMyOutsideChainHandler;
import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageChainHandler;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyApiPageService;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyLoginService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.MJMyItemService;
import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService;
import l1j.server.MJWebServer.ws.MJWebSockServerProvider;

public class MJMyResource {
	private static final MJMyResource resource = new MJMyResource();
	
	public static void onApplicationStartup(){
		resource.loadCachedModels();
		resource.registerMyChains();
		resource.registerKeywordProvider();
	}
	
	public static MJMyConstruct construct(){
		return resource.construct;
	}
	
	public static MJMySlick slick(){
		return resource.slick;
	}

	public static MJMyPageChainHandler pageChain(){
		return resource.pageChain;
	}
	
	public static MJMyApiChainHandler apiChain(){
		return resource.apiChain;
	}
	
	public static MJMyOutsideChainHandler outsideChain(){
		return resource.outsideChain;
	}
	
	public static MJMyFileChainHandler fileChain(){
		return resource.fileChain;				
	}
	
	public static MJKeywordModelProvider realTimeKeywordProvider(){
		return resource.realTimeKeywordProvider;
	}
	
	private MJMyConstruct construct;
	private MJMySlick slick;
	
	private MJMyPageChainHandler pageChain;
	private MJMyApiChainHandler apiChain;
	private MJMyOutsideChainHandler outsideChain;
	private MJMyFileChainHandler fileChain;
	private MJKeywordModelProvider realTimeKeywordProvider;
	
	private MJMyResource(){
	}
	
	private void loadCachedModels(){
		loadCachedModel("construct", "./appcenter/my/json/mj-construct.json", MJMyConstruct.class, new MJMonitorCacheConverter<MJMyConstruct>(){
			@Override
			public MJMyConstruct onNewCached(MJMyConstruct t, long modifiedMillis) {
				if(construct != null && construct.webSocket().port() > -1){
					MJWebServerProvider.provider().shutdown(construct.webSocket().port());
				}
				construct = t;
				int port = construct.webSocket().port();
				MJMyLoginService.service().onConstructChanged(t);
				if(port > -1){
					MJWebServerProvider.provider().bind(port, MJWebSockServerProvider.provider().newWebsockServer(construct.webSocket().path()));
				}
				
				return null;
			}
		});
		
		loadCachedModel("mj-my-shortcut", "./appcenter/my/json/mj-shortcut_mapped.json", MJMyShortcutMapped.class, new MJMonitorCacheConverter<MJMyShortcutMapped>(){
			@Override
			public MJMyShortcutMapped onNewCached(MJMyShortcutMapped t, long modifiedMillis) {	
				MJMyApiPageService.service().onShortcutMenuMapped(t);
				return null;
			}
		});
		
		loadCachedModel("mj-my-apps", "./appcenter/my/json/mj-apps_mapped.json", MJMyAppDwonloadMapped.class, new MJMonitorCacheConverter<MJMyAppDwonloadMapped>(){
			@Override
			public MJMyAppDwonloadMapped onNewCached(MJMyAppDwonloadMapped t, long modifiedMillis) {
				MJMyApiPageService.service().onAppDownloadChanged(t);
				return null;
			}
		});
		
		loadCachedModel("mj-my-notice", "./appcenter/my/json/mj-notice.json", MJMyNotice.class, new MJMonitorCacheConverter<MJMyNotice>(){
			@Override
			public MJMyNotice onNewCached(MJMyNotice t, long modifiedMillis) {
				
				MJMyNoticeService.service().onNoticeInfoChanged(t);
				return null;
			}
		});
		
		loadCachedModel("mj-my-slick", "./appcenter/my/json/mj-slick.json", MJMySlick.class, new MJMonitorCacheConverter<MJMySlick>(){
			@Override
			public MJMySlick onNewCached(MJMySlick t, long modifiedMillis) {
				slick = t;
				return null;
			}
		});
		loadCachedModel("mj-my-recommand-items", "./appcenter/my/json/mj-recommand-items.json", MJMyRecommandItem.class, new MJMonitorCacheConverter<MJMyRecommandItem>(){
			@Override
			public MJMyRecommandItem onNewCached(MJMyRecommandItem t, long modifiedMillis) {
				MJMyItemService.service().onNewRecommands(t);
				return null;
			}
		});
	}
	
	private void registerMyChains(){
		MJMyUriChain.chain().registerChain(pageChain = new MJMyPageChainHandler());
		MJMyUriChain.chain().registerChain(apiChain = new MJMyApiChainHandler());
		MJMyUriChain.chain().registerChain(outsideChain = new MJMyOutsideChainHandler());
		MJMyUriChain.chain().registerChain(fileChain = new MJMyFileChainHandler());		
	}
	
	private void registerKeywordProvider(){
		realTimeKeywordProvider = MJKeywordModelProvider.newProvider("item-realtime", MJKeywordModelFactory.newDefault(), 10, 1);
	}
	
	static <T> void loadCachedModel(String cacheName, String path, Class<T> classOf, MJMonitorCacheConverter<T> converter){
		MJMonitorCacheModel<T> model = MJMonitorCacheProvider.newJsonFileCacheModel(String.format("mj-my-resource-%s", cacheName), path, classOf, MJEncoding.MS949);
		model.cacheListener(converter);
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}
}
