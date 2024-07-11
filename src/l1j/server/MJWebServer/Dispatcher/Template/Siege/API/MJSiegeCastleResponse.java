package l1j.server.MJWebServer.Dispatcher.Template.Siege.API;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Siege.POJO.MJSiegeCastleInfo;
import l1j.server.MJWebServer.Dispatcher.Template.Siege.POJO.MJSiegeCastleInfo.CastleInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJSiegeCastleResponse extends MJHttpResponse {
	public MJSiegeCastleResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		MJSiegeCastleInfo cInfo = new MJSiegeCastleInfo();

		ConcurrentHashMap<Integer, MJCastleWar> castleList = MJCastleWarBusiness.getInstance().loadedCastleWars();

		Set<Integer> keys = castleList.keySet();
		
		int castleid;
		MJCastleWar mjcw;
		for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
			castleid = iterator.next();
			mjcw = castleList.get(castleid);
			
			cInfo.siegeCastleList.add(new CastleInfo(castleid, mjcw.getCastleName(), castleid));
		}

		Gson gson = new Gson();
		String json = gson.toJson(cInfo);
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
}
