package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJTemplate.matcher.Matchers;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiIndexController extends MJMyApiController{
	protected MJMyApiIndexController(MJHttpRequest request) {
		super(request);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		MJMyApiIndexModel model = new MJMyApiIndexModel();
		model.code = MJMyApiModel.SUCCESS;
		model.slickItems = MJMyResource.slick();
		model.noticeItems.items = MJMyNoticeService.service().selectItemsInfo(Matchers.all(), 1, 5).items;
		return new MJMyJsonModel(request(), model, null);
	}

}
