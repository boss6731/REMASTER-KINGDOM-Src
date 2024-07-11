package l1j.server.MJWebServer.ws.protocol;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;

public class MJWebSockSendModel<T>{
	private String callbackName;
	private T body;
	public MJWebSockSendModel(String callbackName, T body){
		this.callbackName = callbackName;
		this.body = body;
	}
	
	public TextWebSocketFrame frame(){
		String msg = MJJsonUtil.toJson(body, false);
		return new TextWebSocketFrame(MJString.concat(callbackName, msg));
	}
}
