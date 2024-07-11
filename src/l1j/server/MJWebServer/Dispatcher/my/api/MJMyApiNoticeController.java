package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJTemplate.matcher.Matchers;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiNoticeController extends MJMyApiController{
	MJMyApiNoticeController(MJHttpRequest request) {
		super(request);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		MJMyApiNoticeModel model = new MJMyApiNoticeModel();
		model.code = MJMyApiModel.SUCCESS;
		model.pageNavigation.currentPage = 1;
		model.pageNavigation.totalPage = MJMyNoticeService.service().totalPage();
		model.pageNavigation.countPerPage = MJMyNoticeService.service().countPerPage();
		model.categories = MJMyNoticeService.service().categories();
		model.items = MJMyNoticeService.service().selectItemsInfo(Matchers.all(), 1).items;
		return new MJMyJsonModel(request(), model, null);
	}

}
