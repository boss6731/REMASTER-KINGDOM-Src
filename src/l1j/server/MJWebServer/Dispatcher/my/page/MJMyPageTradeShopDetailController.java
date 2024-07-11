package l1j.server.MJWebServer.Dispatcher.my.page;

import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJWebServer.Dispatcher.my.MJMyHtmlModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageMapped.MJMyPageInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopDetailArgs;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopElemental;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopStatisticsModel;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopStatus;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopTradeType;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.templates.L1Item;

class MJMyPageTradeShopDetailController extends MJMyPageController{
	private String itemName;
	private int enchant;
	private MJMyShopStatus status;
	private MJMyShopElemental elemental;
	private MJMyShopTradeType tradeType;
	
	MJMyPageTradeShopDetailController(MJHttpRequest request, MJMyPageInfo pInfo) {
		super(request, pInfo);
		
		parseParameters();
	}

	private void parseParameters(){
		MJHttpRequest request = request();
		itemName = request.read_parameters_at_once("itemName");
		enchant = MJString.tryParseInt(request.read_parameters_at_once("enchant"), -1);
		status = MJMyShopStatus.fromName(request.read_parameters_at_once("status"));
		elemental = MJMyShopElemental.fromName(request.read_parameters_at_once("elemental"));
		tradeType = MJMyShopTradeType.fromName(request.read_parameters_at_once("tradeType"));
	}
	
	@Override
	protected MJMyModel viewModelInternal() {
		if(MJString.isNullOrEmpty(itemName)){
			return notFound();
		}
		
		L1Item item = ItemTable.getInstance().findItem(new Matcher<L1Item>(){
			@Override
			public boolean matches(L1Item t) {
				return t.getBless() == 1 && t.getName().equalsIgnoreCase(itemName);
			}
		});
		if(item == null){
			return notFound();
		}
		
		MJMyShopDetailArgs args = new MJMyShopDetailArgs();
		args.itemName = itemName;
		args.enchant = enchant;
		args.status = status;
		args.elemental = elemental;
		args.tradeType = tradeType;
		
		MJMyItemDetailResponseModel model = new MJMyItemDetailResponseModel();
		model.itemName = itemName;
		model.iconId = item.getGfxId();
		model.pageEnchant = enchant;
		model.pageStatus = status.name();
		model.pageElemental = elemental.name();
		model.tradeType = tradeType.name();
		model.statistics = MJMyShopService.service().tradeShopProvider().selectStatisticsModel(args);
		
		String json = MJJsonUtil.toJson(model, false);
		String doc = MJString.replace(pInfo.pageText, "{ITEM_INFO}", json);
		return new MJMyHtmlModel(request(), doc, null);
	}

	@SuppressWarnings("unused")
	private static class MJMyItemDetailResponseModel{
		String itemName;
		int iconId;
		int pageEnchant;
		String pageStatus;
		String pageElemental;
		String tradeType;
		MJMyShopStatisticsModel statistics;
	}
}