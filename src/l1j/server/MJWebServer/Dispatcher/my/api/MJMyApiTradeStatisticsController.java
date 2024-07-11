package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopCompleteCode;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiTradeStatisticsController extends MJMyApiController {
	private MJMyTradeShopCompleteCode code;
	MJMyApiTradeStatisticsController(MJHttpRequest request) {
		super(request);
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		code = MJMyTradeShopCompleteCode.fromInt(MJString.tryParseInt(postDatas.get("code"), MJMyTradeShopCompleteCode.COMPLETE.val()));
	}
	
	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(!gm()){
			return failModel("접근 권한이 부족합니다.");
		}
		
		MJMyApiTradeStatisticsModel model = new MJMyApiTradeStatisticsModel();
		model.code = MJMyApiModel.SUCCESS;
		model.model = MJMyShopService.service().selectStatisticsGmModel(code);
		return new MJMyJsonModel(request(), model, null);
	}
}