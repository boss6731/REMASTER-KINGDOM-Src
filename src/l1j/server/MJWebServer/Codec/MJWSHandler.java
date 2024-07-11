package l1j.server.MJWebServer.Codec;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.MJWebServer.protect.MJWebProtectService;

public class MJWSHandler extends SimpleChannelInboundHandler<FullHttpMessage> {
	private MJHttpRequest m_request;

	public MJWSHandler() {
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		try {
			ctx.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
		try {
			if (msg instanceof HttpRequest) {
				channel_read_request(ctx, msg, (HttpRequest) msg);
			}
			if (msg instanceof HttpContent) {
				channel_read_content(ctx, msg, (HttpContent) msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void channel_read_request(ChannelHandlerContext ctx, FullHttpMessage msg, HttpRequest request) {
		m_request = new MJHttpRequest(request, ctx);		
		MJHttpView.view100ContinueExpected(ctx, m_request);
	}

	private void channel_read_content(ChannelHandlerContext ctx, FullHttpMessage msg, HttpContent http_content) {
		if (msg instanceof LastHttpContent) {
			try {
				if(!MJWebProtectService.service().acceptUri(m_request)){
					return;
				}
				MJHttpModel model = MJHttpController.dispatch(m_request, ctx);
				MJHttpView.view(ctx, m_request, model);
			}catch(MJHttpClosedException e) {
				ctx.close();
			} catch (Exception e) {
				ctx.close();
				//e.printStackTrace();
			}
		}
	}

	public static void print(ChannelHandlerContext ctx, String message) {
		InetSocketAddress inetAddr = (InetSocketAddress) ctx.channel().remoteAddress();
		print(inetAddr.getAddress().getHostAddress(), inetAddr.getPort(), message);
	}

	public static void print(String ip, int port, String message) {
		System.out.println(String.format("[WEB 伺服器][%s][%s:%d] %s\r\n", getLocalTime(), ip, port, message));
	}

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getLocalTime() {
		return formatter.format(new GregorianCalendar().getTime());
	}
}
