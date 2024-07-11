package l1j.server.MJWebServer.Dispatcher.Template.Item.API;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJItemSearchResult;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJItemSearchResult.MJItemInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

/*parameters
serverId 1 1
query 1 집행검
sort 1 trade
pageNo 1 1
pageSize 1 10
paginationSize 1 5
*/
public class MJTradeSearchResponse extends MJHttpResponse{
	public MJTradeSearchResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		MJItemSearchResult result = new MJItemSearchResult();
		for(int i=0; i<10; ++i){
			MJItemInfo iInfo = new MJItemInfo(61, i);
			int price = 3600000 + i;
			iInfo.allSellHighPrice = price;
			iInfo.allSellLowPrice = price;
			iInfo.allSellCount = i + 1;
			iInfo.normalSellHighPrice = price;
			iInfo.normalSellLowPrice = price;
			iInfo.normalSellCount = i + 1;
			iInfo.blessSellHighPrice = price;
			iInfo.blessSellLowPrice = price;
			iInfo.blessSellCount = i + 1;
			iInfo.curseSellHighPrice = price;
			iInfo.curseSellLowPrice = price;
			iInfo.curseSellCount = i + 1;
			iInfo.notIdentSellHighPrice = price;
			iInfo.notIdentSellLowPrice = price;
			iInfo.notIdentSellCount = i + 1;

			iInfo.allSellHigh = MJString.parse_money_string(price);
			iInfo.allSellLow = MJString.parse_money_string(price);
			iInfo.normalSellHigh= MJString.parse_money_string(price);
			iInfo.normalSellLow = MJString.parse_money_string(price);
			iInfo.blessSellHigh= MJString.parse_money_string(price);
			iInfo.blessSellLow = MJString.parse_money_string(price);
			iInfo.curseSellHigh= MJString.parse_money_string(price);
			iInfo.curseSellLow = MJString.parse_money_string(price);
			iInfo.notIdentSellHigh= MJString.parse_money_string(price);
			iInfo.notIdentSellLow = MJString.parse_money_string(price);
			result.list.add(iInfo);
		}
		result.pagination.currentPage = 1;
		result.pagination.paginationSize = 5;
		result.pagination.displayCount = 10;
		result.pagination.totalCount = 123;
		result.pagination.maxPageNo = 13;
		result.pagination.start = 1;
		result.pagination.end = 5;
		result.pagination.previous = false;
		result.pagination.next = true;
		result.pagination.previousPage = -1;
		result.pagination.nextPage = 6;
		
		Gson gson = new Gson();
		String json = gson.toJson(result);
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
}
