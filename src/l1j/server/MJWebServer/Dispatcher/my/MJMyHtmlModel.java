package l1j.server.MJWebServer.Dispatcher.my;

import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMyHtmlModel extends MJMyModel{
	public MJMyHtmlModel(MJHttpRequest request, String responseString, MJMyHeaderSetter headerSetter) {
		super(request, responseString, headerSetter);
	}

	@Override
	protected String contentType() {
		return htmlContentType();
	}

	@Override
	protected HttpResponseStatus status() {
		return HttpResponseStatus.OK;
	}
	
	public static String htmlContentType(){
		return "text/html; charset=utf-8";
	}
}
