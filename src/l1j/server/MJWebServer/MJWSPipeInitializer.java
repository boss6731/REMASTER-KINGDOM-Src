package l1j.server.MJWebServer;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import l1j.server.database.Shutdownable;

public interface MJWSPipeInitializer extends Shutdownable {
	@Override
	public void shutdown();
	public void initialize(SocketChannel ch, ChannelPipeline pipe);
}
