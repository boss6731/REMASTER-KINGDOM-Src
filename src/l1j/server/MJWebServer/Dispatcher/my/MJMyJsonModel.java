package l1j.server.MJWebServer.Dispatcher.my;

import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMyJsonModel extends MJMyModel{
	public MJMyJsonModel(MJHttpRequest request, String responseString, MJMyHeaderSetter headerSetter) {
		super(request, responseString, headerSetter);
	}
	
	public MJMyJsonModel(MJHttpRequest request, MJJsonUtil.MJToJsonable jsonable, MJMyHeaderSetter headerSetter){
		this(request, jsonable.toJson(false), headerSetter);
	}
	
	public MJMyJsonModel(MJHttpRequest request, Object o, MJMyHeaderSetter headerSetter){
		this(request, MJJsonUtil.toJson(o, false), headerSetter);
	}

	@Override
	protected String contentType() {
		return "application/json; charset=utf-8";
	}

	@Override
	protected HttpResponseStatus status() {
		return HttpResponseStatus.OK;
	}
	
	
}
