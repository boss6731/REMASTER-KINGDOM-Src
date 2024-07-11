package l1j.server.MJWebServer.Dispatcher.Template.Item.API;

import java.util.Calendar;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJDistrict;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJItemStatistic;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJMarketPriceSummary;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJNameCodeInfo;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJStatisticInfo;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJStoreList;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJStoreList.ItemPriceDetail;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJTradeUser;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.gametime.RealTimeClock;

public class MJStatisticResponse extends MJHttpResponse{
	public MJStatisticResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		MJItemStatistic statistic = new MJItemStatistic();
		statistic.tradeType = MJNameCodeInfo.TRADE_TYPE.BUY;

		statistic.marketPriceSummary = new MJMarketPriceSummary();
		statistic.marketPriceSummary.itemPriceSummary.lowestPrice = "12,000";
		statistic.marketPriceSummary.itemPriceSummary.highestPrice = "548,098";
		statistic.marketPriceSummary.itemPriceSummary.publicStoreCount = 35;
		statistic.marketPriceSummary.itemPriceSummary.secretStoreCount = 0;

		statistic.marketPriceSummary.itemStatisticSummary.lowestPrice = "2";
		statistic.marketPriceSummary.itemStatisticSummary.highestPrice = "595,814";
		statistic.marketPriceSummary.itemStatisticSummary.averagePrice = "194,884";
		statistic.marketPriceSummary.itemStatisticSummary.tradeCount = 53910;
	}
		
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		int lowestPrice = 10000;
		int highestPrice = 553483;
		int averagePrice = 192702;
		int amount = 1629;
		for(int i=0; i>-30; --i){
			MJStatisticInfo tInfo = new MJStatisticInfo(cal);
			lowestPrice += (MJRnd.isBoolean() ? -1 : 1);
			highestPrice += (MJRnd.isBoolean() ? -1 : 1);
			averagePrice += (MJRnd.isBoolean() ? -1 : 1);

			tInfo.itemId = 63;
			tInfo.lowestPrice = MJString.parse_money_string(lowestPrice);
			tInfo.highestPrice = MJString.parse_money_string(highestPrice);
			tInfo.averagePrice = MJString.parse_money_string(averagePrice);
			tInfo.amount = amount--;
			statistic.statisticList.add(tInfo);
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

	statistic.storeList = new MJStoreList();
	for(int i = 0; i < 5; ++i) {
		ItemPriceDetail detail = new ItemPriceDetail();
		detail.itemId = 63;
		detail.tradeType = statistic.tradeType;
		detail.itemPrice = MJString.parse_money_string(540000 + MJRnd.next(10000));
		detail.count = MJRnd.next(100) + 1;
		detail.enchant = 0;
		detail.bless = 0;
		detail.ident = 1;
		detail.itemAttribute = MJNameCodeInfo.ATTRIBUTE.NONE;
		detail.tradeUser = new MJTradeUser();
		detail.tradeUser.traderId = i + 1;
		detail.tradeUser.traderName = String.format("ØÞ?ÞÙ%d", i);
		detail.tradeUser.district = new MJDistrict();
		detail.tradeUser.district.displayName = "ñéäçÏ¡æ´";
		detail.info = 2;
		detail.itemStatus = MJNameCodeInfo.STATUS.BLESS;
		detail.displayAttributeName = detail.itemAttribute.displayName;
		statistic.storeList.itemPriceDetailList.add(detail);
	}
		statistic.storeList.pagination.currentPage = 1;
		statistic.storeList.pagination.displayCount = 5;
		statistic.storeList.pagination.totalCount = 35;
		statistic.storeList.pagination.start = 1;
		statistic.storeList.pagination.end = 5;
		statistic.storeList.pagination.previous = false;
		statistic.storeList.pagination.next = true;
		statistic.storeList.pagination.maxPageNo = 7;
		statistic.storeList.secretCount = 0;
		statistic.storeList.publicCount = 35;
		Gson gson = new Gson();
		String json = gson.toJson(statistic);
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
}
