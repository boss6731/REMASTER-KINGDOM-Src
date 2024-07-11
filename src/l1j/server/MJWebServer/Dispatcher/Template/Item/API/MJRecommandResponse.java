package l1j.server.MJWebServer.Dispatcher.Template.Item.API;
import com.google.gson.Gson;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJPowerBookItem;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJRecommandItemsInfo;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJRecommandItemsInfo.MJRecommandItemInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJRecommandResponse extends MJHttpResponse{
	public MJRecommandResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		MJRecommandItemsInfo items_info = new MJRecommandItemsInfo();
		items_info.itemList.add(make_item_info(61));
		items_info.itemList.add(make_item_info(62));
		items_info.itemList.add(make_item_info(63));
		Gson gson = new Gson();
		String json = gson.toJson(items_info);
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
	
	private MJRecommandItemInfo make_item_info(int item_id){
		MJRecommandItemInfo iInfo = new MJRecommandItemInfo();
		iInfo.buyNowMaxPrice = 20000000;
		iInfo.buyNowMinPrice = 10000000;
		iInfo.itemId = item_id;
		iInfo.sellNowMaxPrice = 40000000;
		iInfo.sellNowMinPrice = 30000000;
		iInfo.powerbookItem = new MJPowerBookItem(iInfo.itemId);
		return iInfo;
	}
}
