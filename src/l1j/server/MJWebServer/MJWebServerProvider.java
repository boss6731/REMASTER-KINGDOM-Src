package l1j.server.MJWebServer;

import java.util.HashMap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJReadWriteLock;
import l1j.server.MJWebServer.protect.MJWebProtectService;

public class MJWebServerProvider {
	private static final MJWebServerProvider provider = new MJWebServerProvider();
	
	public static MJWebServerProvider provider(){
		return provider;
	}
	
	private HashMap<Integer, OpenedServerInfo> openeds;
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workGroup;
	private MJReadWriteLock lock;
	private boolean alive;
	private MJWebServerProvider(){
		this.lock = new MJReadWriteLock();
		this.openeds = new HashMap<>();
		this.alive = true;
		this.bossGroup = new NioEventLoopGroup(1);
		this.workGroup = new NioEventLoopGroup(2);
	}

	public boolean alive(){
		return alive;
	}
	
	public OpenedServerInfo opened(int port){
		try{
			lock.readLock();
			return openeds.get(port);
		}finally{
			lock.readUnlock();
		}
	}
	
	public void bind(int port, final MJWSPipeInitializer initializer){
		if(!alive()){
			return;
		}
		lock.fullyLock();
		try{
			if(openeds.containsKey(port)){
				throw new RuntimeException(String.format("已綁定連接埠 %d", port));
			}
			
			OpenedServerInfo osInfo = new OpenedServerInfo();
			osInfo.initializer = initializer;
			ServerBootstrap bootstrap = new ServerBootstrap().group(bossGroup, workGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>(){
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						if (!Config.Web.webServerOnOff || !alive()) {
							ch.close();
							return;
						}
						if(!MJWebProtectService.service().acceptSocket(ch)){
							ch.close();
							return;
						}
						initializer.initialize(ch, ch.pipeline());
					}
				});
			
			osInfo.future = bootstrap.bind(port);
			openeds.put(port, osInfo);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			lock.fullyUnlock();
		}
	}
	
	public void shutdown(){
		lock.fullyLock();
		alive = false;
		try{
			for(OpenedServerInfo osInfo : openeds.values()){
				try{
					osInfo.closeFuture();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			openeds.clear();
			openeds = null;
		}finally{
			lock.fullyUnlock();
		}
	}

	public void shutdown(int port){
		lock.fullyLock();
		try{
			OpenedServerInfo osInfo = openeds.remove(port);
			if(osInfo == null){
				return;
			}
			osInfo.closeFuture();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			lock.fullyUnlock();
		}
	}
	
	public static class OpenedServerInfo{
		private ChannelFuture future;
		private MJWSPipeInitializer initializer;
		
		private void closeFuture(){
			L1DatabaseFactory.shutdown(initializer);
			future.channel().close().awaitUninterruptibly();
		}
	}
}
