package l1j.server.MJWebServer.protect;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import l1j.server.MJNetServer.ClientManager.MJNSDenialAddress;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJTemplate.matcher.Matchers;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJWebProtectService{
	private static final MJWebProtectService service = new MJWebProtectService();
	public static MJWebProtectService service(){
		return service;
	}
	
	private static Matcher<String> makeAcceptMatcher(){
		List<Matcher<String>> matchers = Arrays.asList(new Matcher<String>(){
			@Override
			public boolean matches(String t) {
				return MJNSDenialAddress.getInstance().is_denials_address(t);
			}
		}, MJWebConnectionMonitor.monitor());
		return Matchers.composite(matchers, false);
	}
	
	private static Matcher<MJHttpRequest> makeUriMatcher(){
		List<Matcher<MJHttpRequest>> matchers = Arrays.asList(new Matcher<MJHttpRequest>(){
			@Override
			public boolean matches(MJHttpRequest request) {
				if(request.uri().length() >= service.protectDefine().uriMonitor().uriLengthLimit()){
					MJNSDenialAddress.getInstance().insert_address(request.get_remote_address_string(), MJNSDenialAddress.REASON_WEB_URI_LENGTH_OVER);
					return true;
				}
				return false;
			}
		}, MJWebUriMonitor.monitor());
		return Matchers.composite(matchers, false);
	}
	
	private MJWebProtectInfo protectDefine;
	private Matcher<String> acceptMatcher;
	private Matcher<MJHttpRequest> uriMatcher;
	private MJWebProtectService(){
		MJMonitorCacheModel<MJWebProtectInfo> model = MJMonitorCacheProvider.newJsonFileCacheModel("mj-web-protect-service", "./config/web-protect-service.json", MJWebProtectInfo.class, MJEncoding.MS949);
		model.cacheListener(new MJMonitorCacheConverter<MJWebProtectInfo>(){
			@Override
			public MJWebProtectInfo onNewCached(MJWebProtectInfo t, long modifiedMillis) {
				protectDefine = t;
				MJWebUriMonitor.monitor().onUpdateMonitorSettings(protectDefine.uriMonitor().monitors());
				return null;
			}
		});
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
		acceptMatcher = makeAcceptMatcher();
		uriMatcher = makeUriMatcher();
	}
	
	MJWebProtectInfo protectDefine(){
		return protectDefine;
	}
	
	public boolean acceptSocket(SocketChannel ch){
		String address = ch.remoteAddress().getAddress().getHostAddress();
		if(acceptMatcher.matches(address)){
			ch.close();
			return false;
		}
		return true;
	}
	
	public boolean acceptUri(MJHttpRequest request){
		if(uriMatcher.matches(request)){
			request.get_ctx().close();
			return false;
		}
		return true;
	}
	
/*	public boolean acceptOutsideLogin(String address) {
		return protectDefine.acceptOutsideAddresses().containsKey(address);
	}*/
	
	public boolean denialOutsideLogin(String address) {
		return protectDefine.denialOutsideAddresses().containsKey(address);
	}
	
	public boolean webSocketActive(ChannelHandlerContext ctx){
		InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		String address = remoteAddress.getAddress().getHostAddress();
		return MJWebSockConcurrentService.service().onActive(address);
	}
	
	public void webSocketInactive(ChannelHandlerContext ctx){
		InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		String address = remoteAddress.getAddress().getHostAddress();
		MJWebSockConcurrentService.service().onInActive(address);
	}

}
