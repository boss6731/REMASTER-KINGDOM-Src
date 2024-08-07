package l1j.server.MJWebServer.Dispatcher.my.page;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import io.netty.handler.codec.http.cookie.Cookie;
import l1j.server.Config;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyHtmlModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageMapped.MJMyPageInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopPriceSort;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyPagePrivateShopSearchController extends MJMyPageController{
	
	private String keyword;
	private MJMyShopPriceSort priceSort;
	MJMyPagePrivateShopSearchController(MJHttpRequest request, MJMyPageInfo pInfo) {
		super(request, pInfo);
		
		parseParameters();
	}

	private void parseParameters(){
		MJHttpRequest request = request();
		keyword = request.read_parameters_at_once("keyword");
		priceSort = MJMyShopPriceSort.fromName(request.read_parameters_at_once("priceSort"));
		if(MJString.isNullOrEmpty(keyword)) {
			for(Cookie cookie : request.get_cookies()){
				switch(cookie.name()){	
				case "lineage1_itemQuery":
					try {
						keyword = URLDecoder.decode(cookie.value(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					return;
				}
			}
		}
	}
	
	@Override
	protected MJMyModel viewModelInternal() {
		if(MJString.isNullOrEmpty(keyword)){
			return notFound();
		}
		
		MJMyItemSearchResponseModel model = new MJMyItemSearchResponseModel();
		model.serverName = Config.Message.GameServerName;
		model.searchKeyword = keyword;
		model.searchSort = priceSort.name();

		String json = MJJsonUtil.toJson(model, false);
		String doc = MJString.replace(pInfo.pageText, "{SEARCH_PAGE_INFO}", json);
		return new MJMyHtmlModel(request(), doc, null);
	}

	@SuppressWarnings("unused")
	private static class MJMyItemSearchResponseModel{
		String serverName;
		String searchKeyword;
		String searchSort;
	}
}