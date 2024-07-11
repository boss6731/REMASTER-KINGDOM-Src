package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJWebServer.Dispatcher.my.MJMyController;
import l1j.server.MJWebServer.Dispatcher.my.MJMyUriChainHandler;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMyApiChainHandler implements MJMyUriChainHandler{

	@Override
	public MJMyController onRequest(MJHttpRequest request, String requestUri) {
		if(!requestUri.startsWith("/my/api")){
			return null;
		}
		
		if(MJMyApiMonitorService.service().matches(request)){
			return new MJMyApiEmptyController(request);
		}
		switch(requestUri){
		case "/my/api/login":
			return new MJMyLoginController(request);
		case "/my/api/logout":
			return new MJMyLogOutController(request);
		case "/my/api/account":
			return new MJMyAccountController(request);
		case "/my/api/representative":
			return new MJMyRepresentativeController(request);
		case "/my/api/page":
			return new MJMyApiPageController(request);
		case "/my/api/pi/index":
			return new MJMyApiIndexController(request);
		case "/my/api/pi/notice":
			return new MJMyApiNoticeController(request);
		case "/my/api/pi/noticeRead":
			return new MJMyApiNoticeReadController(request);
		case "/my/api/pi/ncoin":
			return new MJMyApiNcoinController(request);
		case "/my/api/pi/refund":
			return new MJMyApiRefundController(request);
		case "/my/api/pi/character/detail":
			return new MJMyApiCharDetailController(request);
		case "/my/api/pi/character/inv/index":
			return new MJMyApiCharInvIndexController(request);
		case "/my/api/pi/gm/tools":
			return new MJMyApiGmToolLoginController(request);
		case "/my/api/pi/siege":
			return new MJMyApiSiegeController(request);
		case "/my/api/pi/item":
			return new MJMyApiItemRankController(request);
		case "/my/api/pi/trade-registered":
			return new MJMyApiTradeRegController(request);
		case "/my/api/noticeList":
			return new MJMyApiNoticeListController(request);
		case "/my/api/likeIt":
			return new MJMyApiLikeItController(request);
		case "/my/api/ncoinList":
			return new MJMyApiNcoinListController(request);
		case "/my/api/ncoinCmd":
			return new MJMyApiNcoinCommandController(request);
		case "/my/api/refundList":
			return new MJMyApiRefundListController(request);
		case "/my/api/refundCmd":
			return new MJMyApiRefundCommandController(request);
		case "/my/api/rank":
			return new MJMyApiRankController(request);
		case "/my/api/chat/login":
			return new MJMyApiChatLoginController(request);
		case "/my/api/inventory":
			return new MJMyApiInventoryController(request);
		case "/my/api/gm/map-view":
			return new MJMyApiMapViewController(request);
		case "/my/api/pledgeList":
			return new MJMyApiPledgeController(request);
		case "/my/api/item/detail":
			return new MJMyApiItemShopDetailController(request);
		case "/my/api/item/search":
			return new MJMyApiItemSearchController(request);
		case "/my/api/trade/detail":
			return new MJMyApiTradeShopDetailController(request);
		case "/my/api/trade/search":
			return new MJMyApiTradeSearchController(request);
		case "/my/api/trade/reg-sell":
			return new MJMyApiTradeRegSellController(request);
		case "/my/api/trade/cancel":
			return new MJMyApiTradeCancelController(request);
		case "/my/api/trade/confirm":
			return new MJMyApiTradeConfirmController(request);
		case "/my/api/trade/statistics":
			return new MJMyApiTradeStatisticsController(request);
		}
		return null;
	}
}
