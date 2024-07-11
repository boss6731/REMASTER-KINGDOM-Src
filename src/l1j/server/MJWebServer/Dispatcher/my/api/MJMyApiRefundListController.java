package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.refund.MJMyRefundCategory;
import l1j.server.MJWebServer.Dispatcher.my.service.refund.MJMyRefundSearchColumn;
import l1j.server.MJWebServer.Dispatcher.my.service.refund.MJMyRefundSelectResult;
import l1j.server.MJWebServer.Dispatcher.my.service.refund.MJMyRefundService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiRefundListController extends MJMyApiController{

	private int page;
	private int categoryIndex;
	private String search;
	private String query;
	protected MJMyApiRefundListController(MJHttpRequest request) {
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
		
		MJMyRefundCategory category = MJMyRefundCategory.findCategory(categoryIndex);
		if(category == null){
			return failModel(String.format("invalid category index %d", categoryIndex));
		}
		MJMyRefundSearchColumn column = MJMyRefundSearchColumn.fromColumn(search);
		if(column == null){
			return failModel(String.format("invalid search column %s", search));
		}
		
		MJMyApiRefundListModel model = new MJMyApiRefundListModel();
		model.code = MJMyApiModel.SUCCESS;
		MJMyRefundSelectResult result = MJMyRefundService.service().selectItemsInfo(category, column, query, page);
		model.items = result.items;
		model.pageNavigation.currentPage = page;
		model.pageNavigation.totalPage = result.totalPage;
		model.pageNavigation.countPerPage = MJMyRefundService.service().countPerPage();
		return new MJMyJsonModel(request(), model, null);
	}

}
