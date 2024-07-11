package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvService;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharService;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharSimpleInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiCharInvIndexController extends MJMyApiController{

	MJMyApiCharInvIndexController(MJHttpRequest request) {
		super(request);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return failModel("������ ��ǥ ĳ���Ͱ� �����ϴ�.");
		}
		
		MJMyCharSimpleInfo cInfo = MJMyCharService.service().fromCharacterName(representative);
		if(cInfo == null){
			return failModel("ĳ���� ������ �ҷ����µ� �����߽��ϴ�.");
		}
		
		final String account = userInfo().account();
		final MJMyCharInvService service = MJMyCharInvService.service();
		MJMyApiCharInvIndexModel model = new MJMyApiCharInvIndexModel();
		model.code = MJMyApiModel.SUCCESS;
		model.size.inventory = service.numOfInventoryItems(cInfo.objectId());
		model.size.account = service.numOfAccountWarehouseItems(account);
		model.size.character = service.numOfCharacterWarehouseItems(account);
		return new MJMyJsonModel(request(), model, null);
	}
}
