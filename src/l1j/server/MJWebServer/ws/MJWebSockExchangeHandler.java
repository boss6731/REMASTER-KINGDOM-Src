package l1j.server.MJWebServer.ws;

import io.netty.channel.ChannelHandlerContext;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.database.Shutdownable;

public abstract class MJWebSockExchangeHandler implements Matcher<MJWebSockRequest>, Shutdownable{
	private MJWebSockRequestGroup group;
	protected MJWebSockExchangeHandler(String name, int defaultCapacity){
		group = new MJWebSockRequestGroup(name, defaultCapacity);
	}
	
	boolean accept(MJWebSockRequest request){
		if(matches(request)){
			group.add(request);
			onHandshakeComplete(request);
			return true;
		}
		return false;
	}

	MJWebSockRequest find(ChannelHandlerContext ctx){
		return group.get(ctx.channel().id());
	}
	
	public MJWebSockRequestGroup group(){
		return group;
	}
	
	@Override
	public void shutdown(){
		group.close();
	}
	
	@Override
	public abstract boolean matches(MJWebSockRequest request);
	
	public abstract MJWebSockCallbackService service();
	protected abstract void onHandshakeComplete(MJWebSockRequest request);
}
