package l1j.server.MJWebServer.Dispatcher.Template.Item.API;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJMarketFavoriteInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMarketFavoriteResponse extends MJHttpResponse{
	public MJMarketFavoriteResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		Gson gson = new Gson();
		String json = gson.toJson(new MJMarketFavoriteInfo());
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
}
