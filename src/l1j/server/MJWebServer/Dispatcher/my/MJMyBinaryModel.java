package l1j.server.MJWebServer.Dispatcher.my;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Codec.MJHttpResponseFactory;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMyBinaryModel extends MJMyModel{
	private byte[] data;
	private String contentType;
	public MJMyBinaryModel(MJHttpRequest request, byte[] data, String contentType, MJMyHeaderSetter headerSetter) {
		super(request, MJString.EmptyString, headerSetter);
		this.data = data;
		this.contentType = contentType;
	}
	@Override
	protected String contentType() {
		return contentType;
	}
	
	@Override
	protected HttpResponseStatus status() {
		return HttpResponseStatus.OK;
	}

	@Override
	public HttpResponse getResponse() {
		HttpResponse response = MJHttpResponseFactory.createResponse(request(), status(), data);
		String contentType = contentType();
		if(!MJString.isNullOrEmpty(contentType)){
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);			
		}
		fireHeaderSetters(response);
		return response;
	}
}
