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
			return failModel("게임 매니저에 의해 서비스가 중지되어 있습니다.");
		}
		
		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return failModel("설정된 캐릭터가 없어 채팅방에 입장할 수 없습니다.");
		}
		MJMyCharSimpleInfo cInfo = MJMyCharService.service().fromCharacterName(representative);
		if(cInfo == null){
			return failModel("캐릭터를 찾을 수 없습니다.");
		}
		if(MJMyChatService.service().containsRegisteredCallback(authToken())){
			return failModel("이미 채팅방에 입장중 입니다.");
		}
		
		MJMyChatUserInfo uInfo = MJMyChatService.service().registeredCallback(cInfo, userInfo());
		if(uInfo == null){
			return failModel("채팅 서비스를 사용할 수 없습니다. 잠시 후 다시 이용해주세요.");
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
