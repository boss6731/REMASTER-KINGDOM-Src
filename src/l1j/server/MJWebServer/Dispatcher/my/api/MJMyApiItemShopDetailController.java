package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyPrivateShopDetailModel;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopDetailArgs;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopDetailResult;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopElemental;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopStatus;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopTradeType;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.datatables.ItemTable;

class MJMyApiItemShopDetailController extends MJMyApiController {
	private String itemName;
	private int enchant;
	private int page;
	private MJMyShopStatus status;
	private MJMyShopElemental elemental;
	private MJMyShopTradeType tradeType;
	
	MJMyApiItemShopDetailController(MJHttpRequest request) {
		super(request);
		
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		itemName = postDatas.get("itemName");
		enchant = MJString.tryParseInt(postDatas.get("enchant"), -1);
		page = MJString.tryParseInt(postDatas.get("page"), -1);
		status = MJMyShopStatus.fromName(postDatas.get("status"));
		elemental = MJMyShopElemental.fromName(postDatas.get("elemental"));
		tradeType = MJMyShopTradeType.fromName(postDatas.get("tradeType"));
	}
	
	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(MJString.isNullOrEmpty(itemName)){
			return failModel("알 수 없는 아이템입니다.");						
		}
		
		if(ItemTable.getInstance().findItemIdByName(itemName) == 0){
			return failModel("알 수 없는 아이템입니다.");			
		}

		if(page < 0){
			return failModel(String.format("invalid parameters. page:%d", page));
		}
		
		MJMyShopDetailArgs args = new MJMyShopDetailArgs();
		args.itemName = itemName;
		args.enchant = enchant;
		args.status = status;
		args.elemental = elemental;
		args.tradeType = tradeType;

		MJMyShopDetailResult<MJMyPrivateShopDetailModel> result = MJMyShopService.service().privateShopProvider().selectDetail(args, page);		
		MJMyApiItemShopDetailModel model = new MJMyApiItemShopDetailModel();
		model.code = MJMyApiModel.SUCCESS;
		model.models = result.models();
		model.navigation.countPerPage = result.countPerPage();
		model.navigation.totalPage = result.totalPage();
		model.navigation.currentPage = page;
		return new MJMyJsonModel(request(), model, null);
	}
}
