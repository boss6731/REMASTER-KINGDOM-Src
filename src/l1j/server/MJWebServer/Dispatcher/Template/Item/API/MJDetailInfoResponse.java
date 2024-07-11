package l1j.server.MJWebServer.Dispatcher.Template.Item.API;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJItemDetailInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

/*parameters
serverId 1 0
tradeType 1 SELL
itemId 1 61
enchant 1 -1
itemStatus 1 ALL
itemAttribute 1 ALL
elenchant 1 -1
info 1 -1
_ 1 1526782164355*/
public class MJDetailInfoResponse extends MJHttpResponse{
	public MJDetailInfoResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		MJItemDetailInfo dInfo = new MJItemDetailInfo(63);
		Gson gson = new Gson();
		String json = gson.toJson(dInfo);
		json = MJString.replace(json, "_public", "public");
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
}
