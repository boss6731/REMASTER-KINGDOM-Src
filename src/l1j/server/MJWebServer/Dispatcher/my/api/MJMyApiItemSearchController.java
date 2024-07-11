package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopPriceSort;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopSearchArgs;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopSearchResult;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiItemSearchController extends MJMyApiController {
	private String keyword;
	private MJMyShopPriceSort priceSort;
	private int page;
	
	MJMyApiItemSearchController(MJHttpRequest request) {
		super(request);
		
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		keyword = postDatas.get("keyword");
		priceSort = MJMyShopPriceSort.fromName(postDatas.get("priceSort"));
		page = MJString.tryParseInt(postDatas.get("page"), -1);
	}
	
	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(MJString.isNullOrEmpty(keyword)){
			return failModel("알 수 없는 키워드 입니다.");						
		}
		
		if(page < 0){
			return failModel(String.format("invalid parameters. page:%d", page));
		}
		
		MJMyShopSearchArgs args = new MJMyShopSearchArgs();
		args.keyword = keyword;
		args.priceSort = priceSort;
		
		MJMyShopSearchResult result = MJMyShopService.service().privateShopProvider().selectSearch(args, page, request().get_remote_address_string());
		MJMyApiItemSearchModel model = new MJMyApiItemSearchModel();
		model.code = MJMyApiModel.SUCCESS;
		model.models = result.models();
		model.navigation.countPerPage = result.countPerPage();
		model.navigation.totalPage = result.totalPage();
		model.navigation.totalCount = result.totalCount();
		model.navigation.currentPage = page;
		return new MJMyJsonModel(request(), model, null);
	}
}