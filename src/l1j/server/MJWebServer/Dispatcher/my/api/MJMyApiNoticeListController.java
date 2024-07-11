package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyNotice.MJMyNoticeCategory;
import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeSelectResult;
import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiNoticeListController extends MJMyApiController{
	private int page;
	private int categoryIndex;
	private String query;
	private boolean onlyTitle;
	MJMyApiNoticeListController(MJHttpRequest request) {
		super(request);
		page = -1;
		categoryIndex = -1;
		onlyTitle = false;
		query = MJString.EmptyString;
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		page = MJString.tryParseInt(postDatas.get("page"), -1);
		categoryIndex = MJString.tryParseInt(postDatas.get("category"), -1);
		query = postDatas.get("query");
		onlyTitle = MJString.tryParseBool(postDatas.get("onlyTitle"), true);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(page < 0 || categoryIndex < 0){
			return failModel("invalid parameters");
		}
		
		MJMyNoticeCategory category = MJMyNoticeService.service().findCategory(categoryIndex);
		if(category == null){
			return failModel(String.format("invalid category index %d", categoryIndex));
		}
		MJMyApiNoticeListModel model = new MJMyApiNoticeListModel();
		model.code = MJMyApiModel.SUCCESS;
		MJMyNoticeSelectResult result = MJMyNoticeService.service().selectItemsInfo(category, page, query, onlyTitle);
		model.pageNavigation.currentPage = page;
		model.pageNavigation.totalPage = result.totalPage;
		model.pageNavigation.countPerPage = MJMyNoticeService.service().countPerPage();
		model.items = result.items;
		return new MJMyJsonModel(request(), model, null);
	}

}
