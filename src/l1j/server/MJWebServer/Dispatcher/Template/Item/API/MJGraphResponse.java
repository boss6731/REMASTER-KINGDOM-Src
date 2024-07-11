package l1j.server.MJWebServer.Dispatcher.Template.Item.API;

import java.util.Calendar;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJItemTradeHistory;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJItemTradeHistory.ItemTradeHistory;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.gametime.RealTimeClock;

/*serverId 1 1
tradeType 1 SELL
itemId 1 62
enchant 1 -1
itemStatus 1 ALL
itemAttribute 1 ALL
elenchant 1 -1
info 1 -1*/
public class MJGraphResponse extends MJHttpResponse{
	public MJGraphResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		MJItemTradeHistory history = new MJItemTradeHistory();
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		cal.add(Calendar.DAY_OF_MONTH, -30);
		int price = 196489;
		for(int i=0; i<30; ++i){
			cal.add(Calendar.DAY_OF_MONTH, 1);
			if(i % 2 == 0)
				continue;
			
			ItemTradeHistory th = new ItemTradeHistory();
			th.date = String.format("%02d.%02d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
			th.averagePrice = price + i;
			history.append_history(th);
		}
		Gson gson = new Gson();
		String json = gson.toJson(history);
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
}
