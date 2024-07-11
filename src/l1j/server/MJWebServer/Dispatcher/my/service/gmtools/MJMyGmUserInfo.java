package l1j.server.MJWebServer.Dispatcher.my.service.gmtools;

import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserExpirationListener;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserGroup;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserInfo;
import l1j.server.MJWebServer.ws.MJWebSockInactiveListener;
import l1j.server.MJWebServer.ws.MJWebSockRequest;

public class MJMyGmUserInfo implements MJMyUserExpirationListener, MJWebSockInactiveListener{
	private String uid;
	private String authToken;
	private String callbackName;
	private MJWebSockRequest request;
	MJMyGmUserInfo(MJMyUserInfo uInfo){
		this.authToken = uInfo.authToken();
		uInfo.registeredListener(this);
	}
	
	void uid(String uid){
		this.uid = uid;
	}
	
	public String uid(){
		return uid;
	}
	
	void callbackName(String callbackName){
		this.callbackName = callbackName;
	}
	
	void request(MJWebSockRequest request){
		this.request = request;
	}
	
	public String callbackName(){
		return callbackName;
	}
	
	public MJWebSockRequest request(){
		return request;
	}

	@Override
	public void onExpiration(MJMyUserInfo uInfo) {
		if(request != null){
			request.close();
		}
	}
	
	@Override
	public void onInactive(MJWebSockRequest request) {
		MJMyGmService.service().onInactive(this);
		MJMyGmService.service().callbackService().remove(callbackName());
		MJMyUserInfo uInfo = MJMyUserGroup.group().get(authToken);
		if(uInfo != null){
			uInfo.unregisteredListener(this);
		}
	}
}
