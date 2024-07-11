package l1j.server.MJWebServer.Dispatcher.my.service.gmtools;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.AttributeKey;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.ws.MJWebSockCallbackService;
import l1j.server.MJWebServer.ws.MJWebSockExchangeHandler;
import l1j.server.MJWebServer.ws.MJWebSockRequest;

class MJMyGmExchangeHandler extends MJWebSockExchangeHandler{
	public static final AttributeKey<MJMyGmUserInfo> gmUserKey = AttributeKey.newInstance("gmUserKey");
	MJMyGmExchangeHandler(){
		super("my-gm-tools-group", 4);
	}

	@Override
	public boolean matches(MJWebSockRequest request) {
		String uid = MJString.EmptyString;
		LOOP_END: for(Cookie cookie : request.cookies()){
			switch(cookie.name()){
			case "authToken":
				uid = cookie.value();
				break LOOP_END;
			}
		}
		if(MJString.isNullOrEmpty(uid)){
			return false;
		}
		MJMyGmUserInfo gInfo = MJMyGmService.service().unmodifiedRegisteredUsers().get(uid);
		if(gInfo == null){
			return false;
		}
		
		gInfo.request(request);
		request.attr(gmUserKey).set(gInfo);
		request.addInactiveListener(gInfo);
		return true;
	}

	@Override
	public MJWebSockCallbackService service() {
		return MJMyGmService.service().callbackService();
	}

	@Override
	protected void onHandshakeComplete(MJWebSockRequest request) {
	}
}
