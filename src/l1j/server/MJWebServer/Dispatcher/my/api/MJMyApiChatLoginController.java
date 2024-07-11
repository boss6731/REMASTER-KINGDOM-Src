package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharSimpleInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharService;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatService;
import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatUserInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiChatLoginController extends MJMyApiController{
	MJMyApiChatLoginController(MJHttpRequest request) {
		super(request);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(MJMyResource.construct().webSocket().port() == -1){
			return failModel("���� �Ŵ����� ���� ���񽺰� �����Ǿ� �ֽ��ϴ�.");
		}
		
		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return failModel("������ ĳ���Ͱ� ���� ä�ù濡 ������ �� �����ϴ�.");
		}
		MJMyCharSimpleInfo cInfo = MJMyCharService.service().fromCharacterName(representative);
		if(cInfo == null){
			return failModel("ĳ���͸� ã�� �� �����ϴ�.");
		}
		if(MJMyChatService.service().containsRegisteredCallback(authToken())){
			return failModel("�̹� ä�ù濡 ������ �Դϴ�.");
		}
		
		MJMyChatUserInfo uInfo = MJMyChatService.service().registeredCallback(cInfo, userInfo());
		if(uInfo == null){
			return failModel("ä�� ���񽺸� ����� �� �����ϴ�. ��� �� �ٽ� �̿����ּ���.");
		}
		
		MJMyApiChatLoginModel model = new MJMyApiChatLoginModel();
		model.code = MJMyApiModel.SUCCESS;
		model.service = true;
		model.callbackName = uInfo.callbackName();
		model.uid = uInfo.uid();
		model.port = MJMyResource.construct().webSocket().port();
		model.uri = MJMyResource.construct().webSocket().path();
		model.gm = gm();
		return new MJMyJsonModel(request(), model, null);
	}
}
