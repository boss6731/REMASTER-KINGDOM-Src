package l1j.server.MJWebServer.ws;

import io.netty.channel.ChannelHandlerContext;
import l1j.server.MJWebServer.MJWSPipeInitializer;

public class MJWebSockServerProvider {
	private static final MJWebSockServerProvider provider = new MJWebSockServerProvider();
	public static MJWebSockServerProvider provider(){
		return provider;
	}
	
	public MJWSPipeInitializer newWebsockServer(String websockPath){
		return new MJWebSockServer(websockPath);
	}
	
	public void print(MJWebSockRequest request, String message){
		System.out.println(String.format("[MJWebSockServer]%s message:%s", request.connectionFlow(), message));
	}
	
	public void print(ChannelHandlerContext ctx, String message){
		System.out.println(String.format("[MJWebSockServer]%s message:%s", ctx.channel().remoteAddress().toString(), message));		
	}
}
