package l1j.server.MJWebServer.ws;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;
import l1j.server.MJWebServer.protect.MJWebProtectService;

public class MJWebSockHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception{
		if(!MJWebProtectService.service().webSocketActive(ctx)){
			ctx.close();
			return;
		}
		super.channelActive(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception{		
		MJWebSockServerProvider.provider().print(ctx, "closed.");
		MJWebProtectService.service().webSocketInactive(ctx);
		super.channelInactive(ctx);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		MJWebSockServerProvider.provider().print(ctx, "exceptionCaught.");
		ctx.close();
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof HandshakeComplete){
			HandshakeComplete complete = (HandshakeComplete)evt;
			MJWebSockRequest request = new MJWebSockRequest(ctx, complete);
			if(!MJWebSockExchangeComposite.composite().accept(request)){
				request.close();
				return;
			}
			MJWebSockServerProvider.provider().print(request, "handshake success.");
		}
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		if(!MJWebSockExchangeComposite.composite().onChannelRead(ctx, msg)){
			ctx.close();
		}
	}
}