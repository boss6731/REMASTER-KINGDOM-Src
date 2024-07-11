package l1j.server.MJWebServer;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import l1j.server.MJWebServer.Codec.MJWSHandler;

public class MJDefaultWebServer implements MJWSPipeInitializer{
	private SslContext sslContext;
	private boolean alive;
	public MJDefaultWebServer(SslContext sslContext){
		this.sslContext = sslContext;
		this.alive = true;
	}
	
	@Override
	public void initialize(SocketChannel ch, ChannelPipeline pipe) {
		if(!alive){
			ch.close();
			return;
		}
		
		if(sslContext != null){
			pipe.addLast(sslContext.newHandler(ch.alloc()));
		}
		pipe.addLast(new HttpRequestDecoder());
		pipe.addLast(new HttpObjectAggregator(65536));
		pipe.addLast(new HttpResponseEncoder()); 
		pipe.addLast(new HttpContentCompressor());
		pipe.addLast(new MJWSHandler());
	}

	@Override
	public void shutdown() {
		alive = false;
	}

}
