package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharService;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharSimpleInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmService;
import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmUserInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiGmToolLoginController extends MJMyApiController{

	MJMyApiGmToolLoginController(MJHttpRequest request) {
		super(request);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(!gm()){
			return failModel("������ �����մϴ�.");
		}
		if(MJMyGmService.service().containsRegisteredCallback(authToken())){
			return failModel("���� ����̽����� �̹� ��� ���Դϴ�.");
		}
		if(MJMyResource.construct().webSocket().port() == -1){
			return failModel("���� �Ŵ����� ���� ���񽺰� �����Ǿ� �ֽ��ϴ�.");
		}
		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return failModel("��ǥ ĳ���Ͱ� ������ ����� �Ұ��� �մϴ�.");
		}
		MJMyCharSimpleInfo cInfo = MJMyCharService.service().fromCharacterName(representative);
		if(cInfo == null){
			return failModel("ĳ���͸� ã�� �� �����ϴ�.");
		}
		if(!cInfo.gm()){
			return failModel("�ش� ���񽺴� ������ ĳ���� ��� GM������ �־�� ��� ���� �մϴ�.");
		}
		
		MJMyGmUserInfo uInfo = MJMyGmService.service().registeredCallback(userInfo());
		if(uInfo == null){
			return failModel("���񽺸� �̿��� �� �����ϴ�. ��� �� �ٽ� �̿����ּ���.");
		}
		
		MJMyApiGmToolLoginModel model = new MJMyApiGmToolLoginModel();
		model.code = MJMyApiModel.SUCCESS;
		model.service = true;
		model.uid = uInfo.uid();
		model.port = MJMyResource.construct().webSocket().port();
		model.uri = MJMyResource.construct().webSocket().path();
		model.callbackName = uInfo.callbackName();
		model.characters = MJMyGmService.service().charViewProvider().onlineUsers();
		return new MJMyJsonModel(request(), model, null);
	}

}
