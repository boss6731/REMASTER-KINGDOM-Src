package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyApiPageService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiPageController extends MJMyApiController{

	MJMyApiPageController(MJHttpRequest request) {
		super(request);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		MJMyApiPageService service = MJMyApiPageService.service();
		MJMyApiPageModel model = new MJMyApiPageModel();
		model.code = MJMyApiModel.SUCCESS;
		model.menuMapped = gm() ? service.gmMenuMapped() : service.menuMapped();
		model.shortcutMenuMapped = service.shortcutMenuMapped();
		model.downloadMapped = service.appDownloadMenuMapped();
		return new MJMyJsonModel(request(), model, null);
	}

}
