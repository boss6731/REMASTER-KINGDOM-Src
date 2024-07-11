package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.ncoin.MJMyNcoinSelectResult;
import l1j.server.MJWebServer.Dispatcher.my.service.ncoin.MJMyNcoinService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiNcoinController extends MJMyApiController{
	MJMyApiNcoinController(MJHttpRequest request) {
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
		
		MJMyApiNcoinListModel model = new MJMyApiNcoinListModel();
		model.code = MJMyApiModel.SUCCESS;
		MJMyNcoinSelectResult result = MJMyNcoinService.service().selectItemsInfo(1);
		model.items = result.items;
		model.pageNavigation.currentPage = 1;
		model.pageNavigation.totalPage = result.totalPage;
		model.pageNavigation.countPerPage = MJMyNcoinService.service().countPerPage();
		return new MJMyJsonModel(request(), model, null);
	}

}
