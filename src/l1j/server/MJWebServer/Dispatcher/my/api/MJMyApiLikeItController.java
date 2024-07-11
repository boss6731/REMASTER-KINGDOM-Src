package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService;
import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService.MJMyNoticeServiceItem;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiLikeItController extends MJMyApiController {
	private int articleId;
	MJMyApiLikeItController(MJHttpRequest request) {
		super(request);
		articleId = -1;
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		articleId = MJString.tryParseInt(postDatas.get("articleId"), -1);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(articleId == -1){
			return failModel("페이지를 찾을 수 없습니다.");
		}
		MJMyNoticeServiceItem item = MJMyNoticeService.service().getItem(articleId);
		if(item == null){
			return failModel(String.format("페이지를 찾을 수 없습니다. %d", articleId));
		}
		
		MJMyApiLikeItModel model = new MJMyApiLikeItModel();
		model.code = MJMyApiModel.SUCCESS;
		model.likeItStatus = MJMyNoticeService.service().onLike(articleId, userInfo().account()) ? "success" : "fail";
		return new MJMyJsonModel(request(), model, null);
	}
	

}
