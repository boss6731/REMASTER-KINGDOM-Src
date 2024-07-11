package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJRankSystem.Loader.MJRankLoadManager;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.rank.MJMyRankService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiRankController extends MJMyApiController {
	private int classId;
	private int rank;
	MJMyApiRankController(MJHttpRequest request) {
		super(request);
		parseParameters();
	}

	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		classId = MJString.tryParseInt(postDatas.get("classId"), -1);
		rank = MJString.tryParseInt(postDatas.get("rank"), -1);
	}
	
	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(classId < 0 || rank < 0){
			return failModel("invalid parameters");
		}
		MJMyApiRankModel model = new MJMyApiRankModel();
		model.code = MJMyApiModel.SUCCESS;
		model.service = MJRankLoadManager.MRK_SYS_ISON;
		model.limit = classId == 8 ? MJRankLoadManager.MRK_SYS_TOTAL_RANGE : MJRankLoadManager.MRK_SYS_CLASS_RANGE;
		model.minLevel = MJRankLoadManager.MRK_SYS_MINLEVEL;
		model.lastUpdateMillis = MJMyRankService.service().lastUpdateMillis();
		model.items = MJMyRankService.service().selectModels(rank - 1, classId);
		return new MJMyJsonModel(request(), model, null);
	}

}
