package l1j.server.MJWebServer.Dispatcher.my;

import java.util.ArrayList;

import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMyUriChain {
	private static final MJMyUriChain chain = new MJMyUriChain();
	public static MJMyUriChain chain(){
		return chain;
	}
	
	private ArrayList<MJMyUriChainHandler> chainsHandler;
	private MJMyUriChain(){
		chainsHandler = new ArrayList<>();
	}
	
	public void registerChain(MJMyUriChainHandler handler){
		chainsHandler.add(handler);
	}
	
	public MJMyModel handle(MJHttpRequest request, String requestUri) throws MJHttpClosedException{
		if(MJMyResource.construct().running()) {
			for(MJMyUriChainHandler handler : chainsHandler){
				MJMyController controller = handler.onRequest(request, requestUri);
				if(controller != null){
					return controller.newViewModel();
				}
			}
		}
		return MJMyModel.notFound(request);
	}
}
