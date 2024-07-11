package l1j.server.MJWebServer.Dispatcher.Template.Blood.API;

import com.google.gson.JsonObject;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJBloodPledgeInfoResponse extends MJHttpResponse{
	public MJBloodPledgeInfoResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		JsonObject element = new JsonObject();
		element.addProperty("name", "hello");
		json.add("pledge", element);
		HttpResponse response = create_response(HttpResponseStatus.OK, json.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
}
