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
			return failModel("권한이 부족합니다.");
		}
		if(MJMyGmService.service().containsRegisteredCallback(authToken())){
			return failModel("같은 디바이스에서 이미 사용 중입니다.");
		}
		if(MJMyResource.construct().webSocket().port() == -1){
			return failModel("게임 매니저에 의해 서비스가 중지되어 있습니다.");
		}
		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return failModel("대표 캐릭터가 없으면 사용이 불가능 합니다.");
		}
		MJMyCharSimpleInfo cInfo = MJMyCharService.service().fromCharacterName(representative);
		if(cInfo == null){
			return failModel("캐릭터를 찾을 수 없습니다.");
		}
		if(!cInfo.gm()){
			return failModel("해당 서비스는 계정과 캐릭터 모두 GM권한이 있어야 사용 가능 합니다.");
		}
		
		MJMyGmUserInfo uInfo = MJMyGmService.service().registeredCallback(userInfo());
		if(uInfo == null){
			return failModel("서비스를 이용할 수 없습니다. 잠시 후 다시 이용해주세요.");
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
