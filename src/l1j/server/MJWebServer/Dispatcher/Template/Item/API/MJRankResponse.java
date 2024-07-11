package l1j.server.MJWebServer.Dispatcher.Template.Item.API;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJItemSearchRanksInfo;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJNameCodeInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJRankResponse extends MJHttpResponse{
	public MJRankResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		MJItemSearchRanksInfo ranksInfo = new MJItemSearchRanksInfo();
		ranksInfo.append_rank_item(61, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ALL);
		ranksInfo.append_rank_item(62, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ALL);
		ranksInfo.append_rank_item(63, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ALL);
		ranksInfo.append_rank_item(64, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ALL);
		ranksInfo.append_rank_item(65, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ALL);
		ranksInfo.append_rank_item(61, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.WEAPON);
		ranksInfo.append_rank_item(62, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.WEAPON);
		ranksInfo.append_rank_item(63, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.WEAPON);
		ranksInfo.append_rank_item(64, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.WEAPON);
		ranksInfo.append_rank_item(65, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.WEAPON);
		ranksInfo.append_rank_item(222304, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ACCESSORY);
		ranksInfo.append_rank_item(222306, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ACCESSORY);
		ranksInfo.append_rank_item(222311, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ACCESSORY);
		ranksInfo.append_rank_item(222318, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ACCESSORY);
		ranksInfo.append_rank_item(222319, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ACCESSORY);		
		ranksInfo.append_rank_item(490000, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ARMOR);
		ranksInfo.append_rank_item(490001, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ARMOR);
		ranksInfo.append_rank_item(490002, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ARMOR);
		ranksInfo.append_rank_item(490003, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ARMOR);
		ranksInfo.append_rank_item(490004, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ARMOR);
		ranksInfo.append_rank_item(714, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ETC);
		ranksInfo.append_rank_item(715, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ETC);
		ranksInfo.append_rank_item(716, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ETC);
		ranksInfo.append_rank_item(717, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ETC);
		ranksInfo.append_rank_item(718, MJNameCodeInfo.TRADE_TYPE.BUY, MJNameCodeInfo.CATEGORY.ETC);
		
		ranksInfo.append_rank_item(61, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ALL);
		ranksInfo.append_rank_item(62, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ALL);
		ranksInfo.append_rank_item(63, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ALL);
		ranksInfo.append_rank_item(64, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ALL);
		ranksInfo.append_rank_item(65, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ALL);
		ranksInfo.append_rank_item(61, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.WEAPON);
		ranksInfo.append_rank_item(62, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.WEAPON);
		ranksInfo.append_rank_item(63, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.WEAPON);
		ranksInfo.append_rank_item(64, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.WEAPON);
		ranksInfo.append_rank_item(65, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.WEAPON);

		ranksInfo.append_rank_item(222304, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ACCESSORY);
		ranksInfo.append_rank_item(222306, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ACCESSORY);
		ranksInfo.append_rank_item(222311, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ACCESSORY);
		ranksInfo.append_rank_item(222318, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ACCESSORY);
		ranksInfo.append_rank_item(222319, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ACCESSORY);	
		ranksInfo.append_rank_item(490000, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ARMOR);
		ranksInfo.append_rank_item(490001, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ARMOR);
		ranksInfo.append_rank_item(490002, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ARMOR);
		ranksInfo.append_rank_item(490003, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ARMOR);
		ranksInfo.append_rank_item(490004, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ARMOR);
		ranksInfo.append_rank_item(714, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ETC);
		ranksInfo.append_rank_item(715, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ETC);
		ranksInfo.append_rank_item(716, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ETC);
		ranksInfo.append_rank_item(717, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ETC);
		ranksInfo.append_rank_item(718, MJNameCodeInfo.TRADE_TYPE.SELL, MJNameCodeInfo.CATEGORY.ETC);
		
		Gson gson = new Gson();
		String json = gson.toJson(ranksInfo);
		json = MJString.replace(json, "_public", "public");
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
}
