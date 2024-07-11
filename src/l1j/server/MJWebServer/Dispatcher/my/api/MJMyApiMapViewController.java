package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmService;
import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmUserInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.mapview.MJMyMapViewService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiMapViewController extends MJMyApiController{
	private int mapId;
	MJMyApiMapViewController(MJHttpRequest request) {
		super(request);
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		mapId = MJString.tryParseInt(postDatas.get("mapId"), -1);
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
		
		MJMyGmUserInfo uInfo = MJMyGmService.service().unmodifiedRegisteredUsers().get(authToken());
		if(uInfo == null){
			return failModel("권한이 부족합니다.");
		}
		
		
		MJMyApiMapViewModel model = new MJMyApiMapViewModel();
		model.code = MJMyApiModel.SUCCESS;
		if(mapId != -1){
			model.tile = MJMyMapViewService.service().mapViewInfo(mapId);
			if(model.tile != null){
				model.objects = MJMyMapViewService.service().onNewWatcher(mapId, uInfo);
			}
		}
		return new MJMyJsonModel(request(), model, null);
	}

}
