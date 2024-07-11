package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.ncoin.MJMyNcoinCategory;
import l1j.server.MJWebServer.Dispatcher.my.service.ncoin.MJMyNcoinSearchColumn;
import l1j.server.MJWebServer.Dispatcher.my.service.ncoin.MJMyNcoinSelectResult;
import l1j.server.MJWebServer.Dispatcher.my.service.ncoin.MJMyNcoinService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiNcoinListController extends MJMyApiController{
	private int page;
	private int categoryIndex;
	private String search;
	private String query;
	MJMyApiNcoinListController(MJHttpRequest request) {
		super(request);
		parseParameters();
	}

	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		page = MJString.tryParseInt(postDatas.get("page"), -1);
		categoryIndex = MJString.tryParseInt(postDatas.get("category"), -1);
		search = postDatas.get("search");
		query = postDatas.get("query");
	}
	
	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(!gm()){
			return notFound();
		}
		if(page < 0 || categoryIndex < 0 || MJString.isNullOrEmpty(search)){
			return failModel("invalid parameters");
		}
		
		MJMyNcoinCategory category = MJMyNcoinCategory.findCategory(categoryIndex);
		if(category == null){
			return failModel(String.format("invalid category index %d", categoryIndex));
		}
		
		MJMyNcoinSearchColumn column = MJMyNcoinSearchColumn.fromColumn(search);
		if(column == null){
			return failModel(String.format("invalid search column %s", search));
		}
		
		MJMyApiNcoinListModel model = new MJMyApiNcoinListModel();
		model.code = MJMyApiModel.SUCCESS;
		MJMyNcoinSelectResult result = MJMyNcoinService.service().selectItemsInfo(category, column, query, page);
		model.items = result.items;
		model.pageNavigation.currentPage = page;
		model.pageNavigation.totalPage = result.totalPage;
		model.pageNavigation.countPerPage = MJMyNcoinService.service().countPerPage();
		return new MJMyJsonModel(request(), model, null);
	}

}
