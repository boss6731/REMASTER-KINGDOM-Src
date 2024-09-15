package l1j.server.MJWebServer.ws;

import java.util.concurrent.CopyOnWriteArrayList;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.ws.MJWebSockCallbackService.CallModelJsonConverterException;
import l1j.server.MJWebServer.ws.MJWebSockCallbackService.CallModelNotFoundException;
import l1j.server.database.Shutdownable;

public class MJWebSockExchangeComposite implements Shutdownable {
	private static final MJWebSockExchangeComposite composite = new MJWebSockExchangeComposite();
	public static MJWebSockExchangeComposite composite(){
		return composite;
	}
	
	private final CopyOnWriteArrayList<MJWebSockExchangeHandler> groupHandlers;
	private MJWebSockExchangeComposite(){
		groupHandlers = new CopyOnWriteArrayList<>();
	}
	
	public void appendHandler(MJWebSockExchangeHandler handler){
		if(!groupHandlers.contains(handler)){
			groupHandlers.add(handler);
		}
	}
	
	public void removeHandler(MJWebSockExchangeHandler handler){
		groupHandlers.remove(handler);
	}
	
	public boolean accept(MJWebSockRequest request){
		for(MJWebSockExchangeHandler handler : groupHandlers){
			if(handler.accept(request)){
				return true;
			}
		}
		return false;
	}
	
	public MJWebSockRequest find(ChannelHandlerContext ctx){
		for(MJWebSockExchangeHandler handler : groupHandlers){
			MJWebSockRequest request = handler.find(ctx);
			if(request != null){
				return request;
			}
		}
		return null;
	}
	
	public boolean onChannelRead(ChannelHandlerContext ctx, TextWebSocketFrame msg){
		for(MJWebSockExchangeHandler handler : groupHandlers){
			MJWebSockRequest request = handler.find(ctx);
			if(request == null){
				continue;
			}
			String text = msg.text();
			if(MJString.isNullOrEmpty(text)){
				MJWebSockServerProvider.provider().print(ctx, "empty request string.");
				return false;
			}
			int idx = text.indexOf("{");
			if(idx == -1){
				MJWebSockServerProvider.provider().print(ctx, "not found protocol delimiter");
				return false;
			}
			String callbackName = text.substring(0, idx);
			String body = text.substring(idx, text.length());
			try {
				handler.service().callback(request, callbackName, body);
			}catch(CallModelNotFoundException e) {
				MJWebSockServerProvider.provider().print(request, String.format("not found callback calss...\r\n\t-callbackName:%s\r\n\t-body:%s", callbackName, body));
				return false;				
			}catch(CallModelJsonConverterException e){
				MJWebSockServerProvider.provider().print(request, String.format("body data converter fail...\r\n\t-callbackName:%s\r\n\t-body:%s", callbackName, body));
				return false;				
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void shutdown(){
		for(MJWebSockExchangeHandler handler : groupHandlers){
			L1DatabaseFactory.shutdown(handler);
		}
		groupHandlers.clear();
	}
}
