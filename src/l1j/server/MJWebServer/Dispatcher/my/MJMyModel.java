package l1j.server.MJWebServer.Dispatcher.my;

import java.util.LinkedList;
import java.util.List;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Codec.MJHttpModel;
import l1j.server.MJWebServer.Codec.MJHttpResponseFactory;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public abstract class MJMyModel implements MJHttpModel{
	
	public static MJMyModel notFound(final MJHttpRequest request){
		return new MJMyModel(request, MJString.EmptyString, null){
			@Override
			protected String contentType() {
				return MJString.EmptyString;
			}
			@Override
			protected HttpResponseStatus status() {
				return HttpResponseStatus.NOT_FOUND;
			}
			@Override
			public HttpResponse getResponse() {
				HttpResponse response = MJHttpResponseFactory.notFound(request());
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
				return response;
			}
		};
	}
	
	static MJMyModel back(final MJHttpRequest request, final String message){
		return new MJMyModel(request, MJString.EmptyString, null){
			@Override
			protected String contentType() {
				return MJString.EmptyString;
			}
			@Override
			protected HttpResponseStatus status() {
				return HttpResponseStatus.OK;
			}
			@Override
			public HttpResponse getResponse() {
				HttpResponse response = MJHttpResponseFactory.createResponse(request, status(), String.format("<html><body><script>alert('%s'); history.back();</script></body></html>", message.replace("\r\n", "\\r\\n")));
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
				return response;
			}
		};
	}
	
	static MJMyModel redirect(final MJHttpRequest request, final String uri){
		return new MJMyModel(request, MJString.EmptyString, null){

			@Override
			protected String contentType() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected HttpResponseStatus status() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public HttpResponse getResponse() {
				HttpResponse response = MJHttpResponseFactory.redirect(request(), uri);
				return response;
			}
		};
	}
	
	
	private MJHttpRequest request;
	private String responseString;
	private LinkedList<MJMyHeaderSetter> headerSetters;
	protected MJMyModel(MJHttpRequest request, String responseString, MJMyHeaderSetter headerSetter){
		this.request = request;
		this.responseString = responseString;
		addHeaderSetter(headerSetter);
	} 

	protected MJHttpRequest request(){
		return request;
	}
	
	protected List<MJMyHeaderSetter> headerSetter(){
		return headerSetters;
	}
	
	protected void fireHeaderSetters(HttpResponse response) {
		if(headerSetters != null) {
			for(MJMyHeaderSetter setter : headerSetters) {
				setter.onHeaderSet(response);
			}
		}
	}
	
	protected void addHeaderSetter(MJMyHeaderSetter headerSetter){
		if(headerSetter == null) {
			return;
		}
		
		if(headerSetters == null) {
			headerSetters = new LinkedList<>();
		}
		headerSetters.add(headerSetter);
	}
	
	protected abstract String contentType();
	protected abstract HttpResponseStatus status();
	
	@Override
	public HttpResponse getResponse() {
		HttpResponse response = MJHttpResponseFactory.createResponse(request, status(), responseString);
		String contentType = contentType();
		if(!MJString.isNullOrEmpty(contentType)){
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);			
		}
		fireHeaderSetters(response);
		return response;
	}
	
}
