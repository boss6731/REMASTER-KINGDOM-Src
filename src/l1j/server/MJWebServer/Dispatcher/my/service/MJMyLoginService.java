package l1j.server.MJWebServer.Dispatcher.my.service;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJKeyValuePair;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.MJWebServer.Dispatcher.my.MJMyController;
import l1j.server.MJWebServer.Dispatcher.my.MJMyHtmlModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyUriChain;
import l1j.server.MJWebServer.Dispatcher.my.MJMyUriChainHandler;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyConstruct;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyConstruct.MJMyAuth;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserTokenFactory;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMyLoginService {
	private static final MJMyLoginService service = new MJMyLoginService();
	
	public static MJMyLoginService service(){
		return service;
	}
	
	private String uri;
	private String secretUri;
	private MJMyLoginService(){
		MJMyUriChain.chain().registerChain(new MJMyLoginChainHandler());
	}
	
	public void onConstructChanged(MJMyConstruct construct){
		MJMyAuth auth = construct.auth();
		MJMonitorCacheModel<String> model = MJMonitorCacheProvider.newTextFileCacheModel(auth.authUri(), auth.authTemplatePath(), MJEncoding.UTF8);
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
		uri = auth.authUri();
		secretUri = auth.authSecretUri();
	}
	
	private class MJMyLoginChainHandler implements MJMyUriChainHandler{

		@Override
		public MJMyController onRequest(MJHttpRequest request, String requestUri) {
			if(MJString.isNullOrEmpty(uri)){
				return null;
			}
			
			if(requestUri.equalsIgnoreCase(secretUri)){
				return new MJMyLoginSecretController(request);
			}
			
			if(!requestUri.equalsIgnoreCase(uri)){
				return null;				
			}
			return new MJMyLoginController(request);
		}
		
	}
	
	private static class MJMyLoginController extends MJMyController{
		private MJMyLoginController(MJHttpRequest request) {
			super(request);
		}

		@Override
		public MJMyModel viewModel() throws MJHttpClosedException {
			return needLogin();
		}
	}
	
	private static class MJMyLoginSecretController extends MJMyController{
		private MJMyLoginSecretController(MJHttpRequest request) {
			super(request);
		}

		@Override
		public MJMyModel viewModel() throws MJHttpClosedException {
			return needLogin();
		}
		
		@Override
		protected MJMyModel needLogin() throws MJHttpClosedException{
			MJMonitorCacheModel<String> model = MJMonitorCacheProvider.monitorCache().getContent(MJMyResource.construct().auth().authUri());
			if(model == null){
				return MJMyModel.notFound(request());
			}
			String doc = model.cacheContent();
			ArrayList<MJKeyValuePair<String, String>> params = new ArrayList<MJKeyValuePair<String, String>>(1);
			params.add(new MJKeyValuePair<String, String>("{APP_TOKEN}", MJMyUserTokenFactory.createAppToken()));
			return new MJMyHtmlModel(request(), MJString.replace(doc, params), null);	
		}
	}
}
