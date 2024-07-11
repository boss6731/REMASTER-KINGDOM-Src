package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.pledge.MJMyPledgeCategory;
import l1j.server.MJWebServer.Dispatcher.my.service.pledge.MJMyPledgeResult;
import l1j.server.MJWebServer.Dispatcher.my.service.pledge.MJMyPledgeService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiPledgeController extends MJMyApiController{
	private String query;
	private int page;
	private int categoryIndex;
	
	protected MJMyApiPledgeController(MJHttpRequest request) {
		super(request);
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		query = postDatas.get("query");
		page = MJString.tryParseInt(postDatas.get("page"), -1);
		categoryIndex = MJString.tryParseInt(postDatas.get("category"), -1);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		//TODO 영자캐릭이 아닐경우 페이지 표시불가하여 주석처리
		/*if(!gm()){
			return notFound();
		}*/
		if(page < 0 || categoryIndex < 0){
			return failModel("invalid parameters");
		}
		MJMyPledgeCategory category = MJMyPledgeCategory.findCategory(categoryIndex);
		if(category == null){
			return failModel(String.format("invalid category index %d", categoryIndex));
		}
		MJMyApiPledgeModel model = new MJMyApiPledgeModel();
		model.code = MJMyApiModel.SUCCESS;
		model.query = query;
		model.category = categoryIndex;
		MJMyPledgeResult result = MJMyPledgeService.service().selectPledge(query, category, page);
		model.pledgeList = result.items;
		model.navigation.currentPage = page;
		model.navigation.totalPage = result.totalPage;
		model.navigation.countPerPage = result.countPerPage;
		return new MJMyJsonModel(request(), model, null);
	}
}
