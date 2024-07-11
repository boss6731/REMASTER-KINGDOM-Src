package l1j.server.MJWebServer.Dispatcher;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMethod;
import l1j.server.Config;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Create.MJHttpAccountCreateResponseEx;
import l1j.server.MJWebServer.Dispatcher.Login.MJHttpLoginResponseEx;
import l1j.server.MJWebServer.Dispatcher.PhoneApp.AutoCashResultResponse;
import l1j.server.MJWebServer.Dispatcher.PhoneApp.GmCommandResponse;
import l1j.server.MJWebServer.Dispatcher.PhoneApp.LetterIndexResponse;
import l1j.server.MJWebServer.Dispatcher.PhoneApp.LetterSendIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpCharacterRankResponse;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpFileResponse;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpPowerBookResponse;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Blood.MJBloodIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Blood.API.MJBloodPledgeInfoResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Blood.API.MJBloodPledgeResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Board.MJNoticeBoardDetailIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Board.MJNoticeBoardIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Board.API.MJNoticeBoardDetailResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Board.API.MJNoticeBoardListResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Board.API.MJNoticeBoardTitleResponse;
import l1j.server.MJWebServer.Dispatcher.Template.BossBoard.MJBossBoardDetailIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.BossBoard.MJBossBoardIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.BossBoard.API.MJBossBoardDetailResponse;
import l1j.server.MJWebServer.Dispatcher.Template.BossBoard.API.MJBossBoardListResponse;
import l1j.server.MJWebServer.Dispatcher.Template.BossBoard.API.MJBossBoardTitleResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.MJCashChargeIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.MJCashIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.MJCashLoadResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.MJCashWithdrawIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.API.MJCashChargeInsertDatabase;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.API.MJCashInsertDatabase;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.API.MJCashMyChargeListResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.API.MJCashMyWithdrawListResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.API.MJCashWithdrawInsertDatabase;
import l1j.server.MJWebServer.Dispatcher.Template.Coupon.MJCouponIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Coupon.API.MJCouponChargeInsertDatabase;
import l1j.server.MJWebServer.Dispatcher.Template.GameLog.MJGameLogResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.MJItemDetailResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.MJItemIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.API.MJCommunityResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.API.MJDetailInfoResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.API.MJGraphResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.API.MJMarketFavoriteResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.API.MJRankResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.API.MJRecommandResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.API.MJStatisticResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.API.MJTradeSearchResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Market.MJMarketIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Market.MJMarketItemInfoResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Market.MJMarketSearchResponse;
import l1j.server.MJWebServer.Dispatcher.Template.MiniGame.MJMiniGameIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.MiniGame.API.MJMiniGameInsertDatabase;
import l1j.server.MJWebServer.Dispatcher.Template.MyTradeHistory.MJMyTradeHistoryIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.MyTradeHistory.API.MJMyTradeHistoryListResponse;
//import l1j.server.MJWebServer.Dispatcher.Template.PowerBall.MjPowerBallIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.PowerBall.MjPowerBallViewIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Siege.MJSiegeIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Siege.API.MJSiegeCastleResponse;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.MJTradeBoardDetailIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.MJTradeBoardIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API.MJTradeAdenaAverageResponse;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API.MJTradeBoardButtonResponse;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API.MJTradeBoardBuyResponse;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API.MJTradeBoardDetailResponse;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API.MJTradeBoardInsertDatabase;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API.MJTradeBoardListResponse;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API.MJTradeBoardTitleResponse;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API.MJTreadeBoardDeteleResponse;
import l1j.server.MJWebServer.Dispatcher.Template.mychaTrad.MJChaTradIndexResponse;
import l1j.server.MJWebServer.Dispatcher.Template.mychaTrad.MJErrorIndexResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJHttpDispatcher {

	public static MJHttpResponse dispatch(MJHttpRequest request, ChannelHandlerContext ctx) throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		String request_uri = request.get_request_uri();

		if (request_uri.startsWith("/powerbook/wiki/")) {
			return new MJHttpPowerBookResponse(request, request_uri.replace("/powerbook/wiki/", MJString.EmptyString));
		}

		//System.out.println(request.toString());

		/**
		 * 請求 URL 標示，服務器運行時註釋掉
		 */
		//System.out.println("請求 URL : " + request_uri);
		//System.out.println(request_uri);

		switch (request_uri) {
			// 手機應用相關 開始
			case "/outgame/cash_charge":
				return new AutoCashResultResponse(request);
			case "/ingame/sendLetter/":
				return new LetterSendIndexResponse(request);
			case "/ingame/loadLetter/":
				return new LetterIndexResponse(request);
			case "/ingame/gmcommand/":
				return new GmCommandResponse(request);
			// 手機應用相關 結束
		}

		// 處理未匹配的請求
		return new MJHttpDefaultResponse(request);
	}
}
		
switch (request_uri) {
		case "/outgame/create": // 創建帳號
		if (request.method() == HttpMethod.GET) {
		return new MJHttpAccountCreateResponseEx(request);
		} else {
		System.out.println(String.format("無效的方法.... %s", request.method()));
		}
		break;

		case "/outgame/login": // 登錄
		if (request.method() == HttpMethod.GET) {
		//return new MJHttpTestLoginResponse(request);
		return new MJHttpLoginResponseEx(request);
		} else {
		System.out.println(String.format("無效的方法.... %s", request.method()));
		}
		break;

		// TODO: 基本應用中心
		case "/ingame/index":
		/*InetSocketAddress inetAddr = (InetSocketAddress)ctx.channel().remoteAddress();
		print(inetAddr.getAddress().getHostAddress(), inetAddr.getPort(), "嘗試連接應用中心");*/
		return new MJHttpIndexResponse(request);

		// TODO: 加載賬戶信息
		case "/ingame/btn_account/loadCashInfo":
		return new MJCashLoadResponse(request);

		// 其他的 case 可以在這裡繼續添加
		}
		switch (request_uri) {
		// TODO: 排行榜菜單
		case "/api/my/character/rank":
		return new MJHttpCharacterRankResponse(request);

		// TODO: 優惠券註冊菜單
		case "/ingame/Event_Coupon/index":
		return new MJCouponIndexResponse(request);
		case "/ingame/Event_Coupon/insert":
		return new MJCouponChargeInsertDatabase(request);

		// TODO: 賬戶註冊菜單
		case "/ingame/btn_account/index":
		return new MJCashIndexResponse(request);
		case "/ingame/btn_account/insert":
		return new MJCashInsertDatabase(request);

		// TODO: 提現菜單
		case "/ingame/btn_withdrawal/index":
		return new MJCashWithdrawIndexResponse(request);
		case "/ingame/btn_withdrawal/insert":
		return new MJCashWithdrawInsertDatabase(request);
		case "/api/trade/board/mywithdrawlist":
		return new MJCashMyWithdrawListResponse(request);

		// TODO: 充值菜單
		case "/ingame/btn_charge/index":
		return new MJCashChargeIndexResponse(request);
		case "/ingame/btn_charge/insert":
		return new MJCashChargeInsertDatabase(request);
		case "/api/trade/board/mychargelist":
		return new MJCashMyChargeListResponse(request);
		}

		switch (request_uri) {
		// TODO: 消息
		case "/ingame/notice/list":
		case "/board/board/index":
		return new MJNoticeBoardIndexResponse(request);
		case "/ingame/notice/content":
		case "/board/notice/detail":
		return new MJNoticeBoardDetailIndexResponse(request);

		// TODO: 市場價格（舊版價格應用）
		case "/ingame/item/intro":
		case "/ingame/item/favorite":
		return new MJMarketIndexResponse(request);
		// return new MJItemFavoriteResponse(request);
		case "/ingame/item/search":
		case "/ingame/item/itemSearch":
		/*if (request.get_parameters().size() > 0)
		return new MJMarketSearchResponse(request);
		else
		return new MJMarketIndexResponse(request);*/
		// return new MJItemSearchResponse(request);
		return new MJMarketSearchResponse(request);
		case "/ingame/item/itemInfo":
		return new MJMarketItemInfoResponse(request);

		// TODO: 遊樂場遊戲日誌
		case "/ingame/Gamelog/index":
		return new MJGameLogResponse(request);

		// TODO: Powerball 列表
		// case "/ingame/poweball/index":
		// return new MjPowerBallIndexResponse(request);

		case "/ingame/poweball_view/index":
		return new MjPowerBallViewIndexResponse(request);

		// TODO: 迷你遊戲
		case "/ingame/minigame/index":
		return new MJMiniGameIndexResponse(request);
		case "/ingame/minigame/insert":
		return new MJMiniGameInsertDatabase(request);
		}

		switch (request_uri) {
		// TODO: 市場價格菜單
		// case "/ingame/minigame/index2":
		// return new MJItemFavoriteResponse(request);

		// TODO: 市場相關菜單
		case "/ingame/item/index":
		return new MJItemIndexResponse(request);

		// TODO: 市場相關菜單
		case "/ingame/item/detail":
		return new MJItemDetailResponse(request);

		// TODO: 市場相關搜索菜單
		// case "/ingame/item/search":
		//     return new MJItemSearchResponse(request);

		// TODO: 攻城搜索菜單
		case "/ingame/siege/index":
		return new MJSiegeIndexResponse(request);

		// TODO: 血盟搜索菜單
		case "/ingame/blood/index":
		return new MJBloodIndexResponse(request);

		// TODO: 物品交易
		case "/ingame/itemtrade/index":
		if (Config.Web.tradeMenu)
		return new MJErrorIndexResponse(request);
		return new MJTradeBoardIndexResponse(request);

		case "/api/board/list/sell/insert":
		if (Config.Web.tradeMenu)
		return new MJErrorIndexResponse(request);
		return new MJTradeBoardInsertDatabase(request);

		case "/ingame/itemtrade/detail":
		if (Config.Web.tradeMenu)
		return new MJErrorIndexResponse(request);
		return new MJTradeBoardDetailIndexResponse(request);

		// TODO: 角色銷售菜單
		case "/ingame/ChaTrad/index":
		return new MJChaTradIndexResponse(request);

		// TODO: 我的交易記錄菜單
		case "/ingame/mytradelist/index":
		return new MJMyTradeHistoryIndexResponse(request);
		}

		// TODO 보스시간표 메뉴
		case "/ingame/bosstime/index":
			return new MJBossBoardIndexResponse(request);
		case "/ingame/bosstime/detail":
			return new MJBossBoardDetailIndexResponse(request);

		case "/api/marketprice/favorite":
			return new MJMarketFavoriteResponse(request);
		case "/api/marketprice/recommendItems/":
			return new MJRecommandResponse(request);
		case "/api/marketprice/trade/searchRank":
			return new MJRankResponse(request);
		case "/api/marketprice/trade/community":
			return new MJCommunityResponse(request);
		case "/api/marketprice/trade/itemInfo":
			return new MJDetailInfoResponse(request);
		case "/api/marketprice/trade/detailInfo":
			return new MJStatisticResponse(request);
		case "/api/marketprice/trade/statistic/graph":
			return new MJGraphResponse(request);
		case "/api/marketprice/trade/search":
			return new MJTradeSearchResponse(request);
		case "/api/world/castle/siege":
			return new MJSiegeCastleResponse(request);
		case "/api/world/pledge/list":
			return new MJBloodPledgeResponse(request);
		}
		
		if (request_uri.startsWith("/api/world/pledge/1/")) {
			return new MJBloodPledgeInfoResponse(request);
		} else if (request_uri.startsWith("/api/board/list/detail/")) {
			return new MJTradeBoardDetailResponse(request);
		} else if (request_uri.startsWith("/api/board/list/title/")) {
			return new MJTradeBoardTitleResponse(request);
		} else if (request_uri.startsWith("/api/board/list/buy_")) {
			return new MJTradeBoardBuyResponse(request);
		} else if (request_uri.startsWith("/api/trade/board/list/")) {
			return new MJTradeBoardListResponse(request);
		} else if (request_uri.startsWith("/api/trade/board/mychargelist/")) {
			return new MJCashMyChargeListResponse(request);
		} else if (request_uri.startsWith("/api/trade/board/mywithdrawlist/")) {
			return new MJCashMyWithdrawListResponse(request);
		} else if (request_uri.startsWith("/api/trade/adena/average/")) {
			return new MJTradeAdenaAverageResponse(request);
		} else if (request_uri.startsWith("/api/notice/board/list/")) {
			return new MJNoticeBoardListResponse(request);
		} else if (request_uri.startsWith("/api/board/notice/detail/")) {
			return new MJNoticeBoardDetailResponse(request);
		} else if (request_uri.startsWith("/api/board/notice/title/")) {
			return new MJNoticeBoardTitleResponse(request);
		} else if (request_uri.startsWith("/api/notice/board/boss_time/")) {
			return new MJBossBoardListResponse(request);
		} else if (request_uri.startsWith("/api/board/boss/title/")) {
			return new MJBossBoardTitleResponse(request);
		} else if (request_uri.startsWith("/api/board/boss/detail/")) {
			return new MJBossBoardDetailResponse(request);
		} else if (request_uri.startsWith("/api/notice/board/mytradelist/")) {
			return new MJMyTradeHistoryListResponse(request);
		} else if (request_uri.startsWith("/api/board/list/button/")) {
			return new MJTradeBoardButtonResponse(request);
		} else if (request_uri.startsWith("/api/board/list/delete/")) {
			return new MJTreadeBoardDeteleResponse(request);
		}
		
		return new MJHttpFileResponse(request);
	}

	public static void print(String ip, int port, String message) {
		System.out.println(String.format("[WEB 伺服器][%s][%s:%d] %s\r\n", getLocalTime(), ip, port, message));
	}

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getLocalTime() {
		return formatter.format(new GregorianCalendar().getTime());
	}
	
	
}
