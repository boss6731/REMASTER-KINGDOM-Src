package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyRepresentativeController extends MJMyApiController{
	private String newCharacterName;
	MJMyRepresentativeController(MJHttpRequest request) {
		super(request);
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		newCharacterName = postDatas.get("newCharacterName");
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(MJString.isNullOrEmpty(newCharacterName)){
			return failModel(String.format("알 수 없는 캐릭터 이름 입니다. %s", newCharacterName));
		}
		
		MJMyUserInfo uInfo = userInfo();
		MJMyRepresentativeService.service().updateRepresentativeCharacter(uInfo.account(), newCharacterName);
		MJMyApiModel model = new MJMyApiModel();
		model.code = MJMyApiModel.SUCCESS;
		return new MJMyJsonModel(request(), model, null);
	}
	
	

}
