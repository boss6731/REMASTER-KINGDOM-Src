package l1j.server.MJWebServer.Dispatcher.Template;

import com.google.gson.JsonObject;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
public class MJHttpCharacterRankResponse extends MJHttpResponse{

	public MJHttpCharacterRankResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		JsonObject json = new JsonObject();
		
		L1PcInstance pc = L1World.getInstance().getPlayer(_user.getCharName());
		
		SC_TOP_RANKER_NOTI no = null;
		if(pc != null)
			no = MJRankUserLoader.getInstance().get(pc.getId());
		
		json.addProperty("serverRank", (no != null ? String.valueOf(no.get_total_ranker().get_rank()) : "-"));
		json.addProperty("classRank", (no != null ? String.valueOf(no.get_class_ranker().get_rank()) : "-"));
		HttpResponse response = create_response(HttpResponseStatus.OK, json.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
}
