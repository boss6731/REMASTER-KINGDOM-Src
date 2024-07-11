package l1j.server.MJWebServer.Codec;

import io.netty.channel.ChannelHandlerContext;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.MJHttpDispatcher;
import l1j.server.MJWebServer.Dispatcher.my.MJMyUriChain;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJHttpController {
	static MJHttpModel dispatch(MJHttpRequest request, ChannelHandlerContext ctx) throws MJHttpClosedException{
		String request_uri = request.get_request_uri();
		if(request_uri.startsWith("/my/")){
			return MJMyUriChain.chain().handle(request, request_uri);
		}
		return MJHttpDispatcher.dispatch(request, ctx);
	}
}
