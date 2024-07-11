package l1j.server.MJWebServer.ws;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.MJWSPipeInitializer;

class MJWebSockServer implements MJWSPipeInitializer{	
	private boolean alive;
	private String websockPath;
	MJWebSockServer(String websockPath){
		this.alive = true;
		this.websockPath = websockPath.startsWith("/") ? websockPath : MJString.concat("/", websockPath); 
	}
	
	@Override
	public void initialize(SocketChannel ch, ChannelPipeline pipe) {
		if(!alive){
			ch.close();
			return;
		}
		pipe.addLast(new HttpRequestDecoder());
		pipe.addLast(new HttpObjectAggregator(65536));
		pipe.addLast(new HttpResponseEncoder());
		pipe.addLast(new WebSocketServerProtocolHandler(websockPath));
		pipe.addLast(new MJWebSockHandler());
	}

	@Override
	public void shutdown() {
		alive = false;
		L1DatabaseFactory.shutdown(MJWebSockExchangeComposite.composite());
	}
}