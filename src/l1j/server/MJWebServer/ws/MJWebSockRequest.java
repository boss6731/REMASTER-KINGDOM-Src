package l1j.server.MJWebServer.ws;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.ws.protocol.MJWebSockSendModel;

public class MJWebSockRequest implements AutoCloseable{
	private Channel channel;
	private ChannelId channelId;
	private HttpHeaders headers;
	private Collection<Cookie> cookies;
	private InetSocketAddress localAddress;
	private InetSocketAddress remoteAddress;
	private CopyOnWriteArrayList<MJWebSockInactiveListener> listeners;
	MJWebSockRequest(ChannelHandlerContext ctx, HandshakeComplete complete){
		this.channel = ctx.channel();
		this.channelId = this.channel.id();
		this.localAddress = (InetSocketAddress)channel.localAddress();
		this.remoteAddress = (InetSocketAddress)channel.remoteAddress();
		this.headers = complete.requestHeaders();
		this.listeners = new CopyOnWriteArrayList<>();
		this.channel.closeFuture().addListener(new ChannelRemoveListener());
		this.parseCookie();
	}
	
	private class ChannelRemoveListener implements ChannelFutureListener{
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			if(channel == null){
				return;
			}
			channel.closeFuture().removeListener(this);
			fireInactive();
			channel = null;
		}	
	}
	
	private void fireInactive(){
		if(listeners != null){
			for(MJWebSockInactiveListener listener : listeners){
				listener.onInactive(this);
			}
			listeners = null;
		}
	}
	
	private void parseCookie(){
		String header = headers().get(HttpHeaderNames.COOKIE);
		if(MJString.isNullOrEmpty(header)){
			cookies = Collections.emptySet();
		}else{
			cookies = ServerCookieDecoder.LAX.decode(header);
		}
	}
	
	public ChannelId channelId(){
		return channelId;
	}
	
	public InetSocketAddress localAddress(){
		return localAddress;
	}
	
	public InetSocketAddress remoteAddress(){
		return remoteAddress;
	}
	
	public String remoteAddressString(){
		return remoteAddress()
				.getAddress()
				.getHostAddress();
	}
	
	public boolean alive(){
		return channel != null && channel.isActive();
	}
	
	public HttpHeaders headers() {
		return headers;
	}
	
	public Collection<Cookie> cookies(){
		return cookies;
	}

	public String connectionFlow() {
		return new StringBuilder(64)
				.append("[")
				.append(remoteAddress().toString())
				.append("] -> [")
				.append(localAddress().toString())
				.append("]")
				.toString();
	}
	
	public <T> Attribute<T> attr(AttributeKey<T> key){
		return channel == null ? emptyAttribute(key) : channel.attr(key);
	}
	
	@Override
	public void close() {
		if(channel != null){
			try{
				channel.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			channel = null;
		}
	}
	
	public void disconnect(){
		close();
	}
	
	public void addInactiveListener(MJWebSockInactiveListener listener){
		if(listeners != null){
			listeners.add(listener);
		}
	}
	
	public void removeInactiveListener(MJWebSockInactiveListener listener){
		if(listeners != null){
			listeners.remove(listener);
		}
	}
	
	public void write(MJWebSockSendModel<?> model){
		if(!alive()){
			return;
		}
		channel.writeAndFlush(model.frame());
	}
	
	private String toString = MJString.EmptyString;
	@Override
	public String toString(){
		if(MJString.isNullOrEmpty(toString)){
			HttpHeaders headers = headers();
			toString = new StringBuilder(1024)
					.append("[MJWebSock請求]").append(connectionFlow()).append("\r\n")
					.append(" -標題： ").append("\r\n").append(MJString.join("\r\n", "   ", headers, headers.size())).append("\r\n")
					.append(" -cookies : ").append("\r\n").append(MJString.join("\r\n", cookies())).append("\r\n")
					.toString();
		}
		return toString;
	}
	

	private static <T> Attribute<T> emptyAttribute(AttributeKey<T> key){
		return new EmptyAttribute<T>(key);
	}
	private static class EmptyAttribute<T> implements Attribute<T>{
		private AttributeKey<T> key;
		EmptyAttribute(AttributeKey<T> key){
			this.key = key;
		}
		
		@Override
		public AttributeKey<T> key() {
			return key;
		}

		@Override
		public T get() {
			return null;
		}

		@Override
		public void set(T value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public T getAndSet(T value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public T setIfAbsent(T value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public T getAndRemove() {
			return null;
		}

		@Override
		public boolean compareAndSet(T oldValue, T newValue) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void remove() {
		}
	}
}
