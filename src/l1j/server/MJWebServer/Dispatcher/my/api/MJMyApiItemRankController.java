package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.item.MJMyItemService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiItemRankController extends MJMyApiController {
	MJMyApiItemRankController(MJHttpRequest request) {
		super(request);
	}
	
	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		MJMyItemService service = MJMyItemService.service();
		MJMyApiItemRankModel model = new MJMyApiItemRankModel();
		model.code = MJMyApiModel.SUCCESS;
		model.realTimeKeywords = service.realTimeKeywords();
		model.itemRankAllModel = service.itemRankAllModel();
		model.itemRankWeaponModel = service.itemRankWeaponModel();
		model.itemRankArmorModel = service.itemRankArmorModel();
		model.itemRankAccessoryModel = service.itemRankAccessoryModel();
		model.itemRankEtcModel = service.itemRankEtcModel();
		model.gm = gm();
		return new MJMyJsonModel(request(), model, null);
	}
}
