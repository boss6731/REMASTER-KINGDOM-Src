package l1j.server.MJWebServer.Codec;

import MJFX.MJFxEntry;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJHttpView {
	public static void view100ContinueExpected(ChannelHandlerContext ctx, MJHttpRequest request){
		if (HttpUtil.is100ContinueExpected(request)) {
			view100ContinueExpectedInternal(ctx);
		}
	}
	
	public static void view(ChannelHandlerContext ctx, MJHttpRequest request, MJHttpModel model) throws MJHttpClosedException{
		if(model == null){
			viewEof(ctx);
			return;
		}
		
		HttpResponse response = model.getResponse();
		if(response != null){
			viewRequest(request, response);
			viewInternal(ctx, request, response);
		}else{
			viewEof(ctx);
		}
	}

	private static final FullHttpResponse view100ContiinueCached = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE).duplicate();
	private static void view100ContinueExpectedInternal(ChannelHandlerContext ctx) {
		ctx.write(view100ContiinueCached.retainedDuplicate());
	}

	private static void viewInternal(ChannelHandlerContext ctx, MJHttpRequest request, HttpResponse response) {
		boolean keepAlive = request.is_keep_alive();
		if (keepAlive) {
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			ctx.write(response);
			ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}else{
			ctx.write(response);
		}
	}

	private static void viewRequest(MJHttpRequest request, HttpResponse response){
		if(MJFxEntry.IS_DEBUG_MODE) {
			if (response.status() == HttpResponseStatus.NOT_FOUND && request.get_request_uri().lastIndexOf(".gif") == -1) {
				System.out.println("MJHttpView:" +request.toString());
			}
		}
	}

	private static void viewEof(ChannelHandlerContext ctx){
		ctx.write(MJString.Eof);
	}
	
}
