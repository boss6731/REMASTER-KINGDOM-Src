package l1j.server.MJWebServer.Dispatcher.my.service.chat;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.AttributeKey;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.ws.MJWebSockCallbackService;
import l1j.server.MJWebServer.ws.MJWebSockExchangeHandler;
import l1j.server.MJWebServer.ws.MJWebSockRequest;

class MJMyChatExchangeHandler extends MJWebSockExchangeHandler{
	public static final AttributeKey<MJMyChatUserInfo> chatUserKey = AttributeKey.newInstance("chatUserKey");
	MJMyChatExchangeHandler() {
		super("my-chat-group", 256);
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
		
		MJMyChatUserInfo cuInfo = MJMyChatService.service().unmodifiedRegisteredUsers().get(uid);
		if(cuInfo == null){
			return false;
		}
		try {
			cuInfo.request(request);
			request.attr(chatUserKey).set(cuInfo);
			request.addInactiveListener(cuInfo);
			if(cuInfo.character().gm()){
				MJMyChatCommandInstance.instance().addCommandListener(cuInfo);
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			cuInfo.onInactive(null);
			request.close();
		}
		return false;
	}

	@Override
	public MJWebSockCallbackService service() {
		return MJMyChatService.service().callbackService();
	}

	@Override
	protected void onHandshakeComplete(MJWebSockRequest request) {
	}
}
