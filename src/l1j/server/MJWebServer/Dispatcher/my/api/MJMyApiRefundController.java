package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.refund.MJMyRefundSelectResult;
import l1j.server.MJWebServer.Dispatcher.my.service.refund.MJMyRefundService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiRefundController extends MJMyApiController{
	MJMyApiRefundController(MJHttpRequest request){
		super(request);
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
		MJMyApiRefundListModel model = new MJMyApiRefundListModel();
		model.code = MJMyApiModel.SUCCESS;
		MJMyRefundSelectResult result = MJMyRefundService.service().selectItemsInfo(1);
		model.items = result.items;
		model.pageNavigation.currentPage = 1;
		model.pageNavigation.totalPage = result.totalPage;
		model.pageNavigation.countPerPage = MJMyRefundService.service().countPerPage();
		return new MJMyJsonModel(request(), model, null);
	}
}
