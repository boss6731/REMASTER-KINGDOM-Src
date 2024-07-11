package l1j.server.MJWebServer.Dispatcher.my.page;

import java.util.ArrayList;
import java.util.List;

import MJNCoinSystem.MJNCoinSettings;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJTimesFormatter;
import l1j.server.MJWebServer.Dispatcher.my.MJMyHtmlModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageMapped.MJMyPageInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopTradeListType;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel.MJMyTradeShopItemModel;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel.MJMyTradeShopUserModel;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.item.L1ItemId;

class MJMyPageTradeMeController extends MJMyPageController{
	private MJMyShopTradeListType tradeListType;
	
	MJMyPageTradeMeController(MJHttpRequest request, MJMyPageInfo pInfo) {
		super(request, pInfo);
		
		parseParameters();
	}

	private void parseParameters(){
		MJHttpRequest request = request();
		tradeListType = MJMyShopTradeListType.fromName(request.read_parameters_at_once("tradeListType"));
	}
	
	@Override
	protected MJMyModel viewModelInternal() {
		String account = userInfo().account();
		List<MJMyTradeShopModel> models = MJMyShopService.service().selectMeList(account, tradeListType);
		MJMyPageTradeMeModel meModel = new MJMyPageTradeMeModel();
		meModel.dataList = new ArrayList<>(models.size());
		meModel.tradeListType = tradeListType.name();
		for(MJMyTradeShopModel detailModel : models){
			MJMyTradeShopUserModel regModel = detailModel.registeredModel();
			MJMyTradeShopItemModel itemModel = detailModel.itemModel();
			MJMyTradeShopUserModel consModel = detailModel.consumerModel();
			MJMyPageTradeMeShopsModel shopsModel = new MJMyPageTradeMeShopsModel();
			shopsModel.tradeNo = detailModel.tradeNo();
			shopsModel.itemName = itemModel.itemName();
			shopsModel.displayPart = detailModel.displayPart();
			shopsModel.price = itemModel.price();
			shopsModel.iconId = itemModel.iconId();
			shopsModel.quantity = itemModel.quantity();
			shopsModel.enchant = itemModel.enchant();
			shopsModel.identified = itemModel.identified();
			shopsModel.bless = itemModel.bless();
			shopsModel.elementalEnchant = itemModel.elementalEnchant();
			
			shopsModel.dollbonuslevel = itemModel.dollbonuslevel();
			shopsModel.dollbonusvalue = itemModel.dollbonusvalue();
			shopsModel.blesslevel = itemModel.blesslevel();
			
			shopsModel.BlessType = itemModel.BlessType();
			shopsModel.BlessTypeValue = itemModel.BlessTypeValue();
			
			shopsModel.regDate = MJTimesFormatter.BASIC.toString(regModel.actionMillis());
			if(consModel != null){
				shopsModel.consDate = MJTimesFormatter.BASIC.toString(consModel.actionMillis());
			}
			if(tradeListType == MJMyShopTradeListType.SELLING && itemModel.itemId() == L1ItemId.ADENA){
				shopsModel.fee = MJNCoinSettings.USER_COMMISSION;
			}else{
				shopsModel.fee = detailModel.commission();
			}
			meModel.dataList.add(shopsModel);
		}
		String json = MJJsonUtil.toJson(meModel, false);
		String doc = MJString.replace(pInfo.pageText, "{ME_PAGE_INFO}", json);
		return new MJMyHtmlModel(request(), doc, null);
	}

	@SuppressWarnings("unused")
	private static class MJMyPageTradeMeModel{
		String tradeListType;
		List<MJMyPageTradeMeShopsModel> dataList;
		MJMyPageTradeMeModel(){
		}
	}

	@SuppressWarnings("unused")
	private static class MJMyPageTradeMeShopsModel{
		int tradeNo;
		String itemName;
		String displayPart;
		int price;
		double fee;
		int iconId;
		int quantity;
		int enchant;
		boolean identified;
		int bless;
		int elementalEnchant;
		String regDate;
		String consDate;
		int dollbonuslevel;
		int dollbonusvalue;
		int blesslevel;
		int BlessType;
		int BlessTypeValue;
	}
}