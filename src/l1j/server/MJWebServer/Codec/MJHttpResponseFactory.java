package l1j.server.MJWebServer.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJHttpResponseFactory {
	public static HttpResponse createResponse(MJHttpRequest request, HttpResponseStatus status){
		FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), status);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
		return response;
	}
	
	public static HttpResponse createResponse(MJHttpRequest request, HttpResponseStatus status, byte[] buff){
		ByteBuf response_buff = Unpooled.wrappedBuffer(buff);
		FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), status, response_buff);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response_buff.readableBytes());
		return response;
	}
	
	public static HttpResponse createResponse(MJHttpRequest request, HttpResponseStatus status, String document){
		return createResponse(request, status, document.getBytes(MJEncoding.UTF8));
	}

    private static final byte[] notFoundMessage = "頁面無法找到。".getBytes(MJEncoding.UTF8);

    public static HttpResponse notFound(MJHttpRequest request) {
        return createResponse(request, HttpResponseStatus.NOT_FOUND, notFoundMessage);
    }

    private static final byte[] redirectMessage = "頁面正在重定向，請稍候。".getBytes(MJEncoding.UTF8);
	public static HttpResponse redirect(MJHttpRequest request, String redirectUri){
		HttpResponse response = createResponse(request, HttpResponseStatus.MOVED_PERMANENTLY, redirectMessage);
		response.headers().set(HttpHeaderNames.LOCATION, redirectUri);
		return response;
	}
}
