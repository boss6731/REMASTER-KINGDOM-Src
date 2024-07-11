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

	public boolean onChannelRead(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
		for (MJWebSockExchangeHandler handler : groupHandlers) {
			MJWebSockRequest request = handler.find(ctx);
			if (request == null) {
				continue; // 如果沒有找到請求，繼續下一個處理器
			}
			String text = msg.text();
			if (MJString.isNullOrEmpty(text)) {
				MJWebSockServerProvider.provider().print(ctx, "請求字符串為空。");
				return false; // 如果請求字符串為空，返回false
			}
			int idx = text.indexOf("{");
			if (idx == -1) {
				MJWebSockServerProvider.provider().print(ctx, "未找到協議分隔符。");
				return false; // 如果沒有找到協議分隔符，返回false
			}
			String callbackName = text.substring(0, idx);
			String body = text.substring(idx, text.length());
			try {
				handler.service().callback(request, callbackName, body);
			} catch (CallModelNotFoundException e) {
				MJWebSockServerProvider.provider().print(request, String.format("未找到回調類...
						-callbackName:%s
						-body:%s", callbackName, body));
				return false; // 如果未找到回調類，捕獲異常並返回false
			} catch (CallModelJsonConverterException e) {
				MJWebSockServerProvider.provider().print(request, String.format("數據轉換失敗...
						-callbackName:%s
						-body:%s", callbackName, body));
				return false; // 如果數據轉換失敗，捕獲異常並返回false
			}
			return true; // 成功處理請求，返回true
	}
	
	@Override
	public void shutdown(){
		for(MJWebSockExchangeHandler handler : groupHandlers){
			L1DatabaseFactory.shutdown(handler);
		}
		groupHandlers.clear();
	}
}
