package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.LinkedList;
import java.util.List;

import MJNCoinSystem.MJNCoinSettings;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvItemInfo;
import l1j.server.server.model.item.L1ItemId;

class MJMyApiTradeRegModel extends MJMyApiModel{
	String characterName;
	MJMyApiTradeRegRestrict restrict;
	int totalInventoryItemsCount;
	int tradableItemsCount;
	List<MJMyApiTradeRegItemModel> tradableItems;
	MJMyApiTradeRegModel(){
		characterName = MJString.EmptyString;
		restrict = new MJMyApiTradeRegRestrict();
		tradableItems = new LinkedList<>();
	}
	
	static class MJMyApiTradeRegItemModel{
		static MJMyApiTradeRegItemModel newModel(MJMyCharInvItemInfo item){
			MJMyApiTradeRegItemModel model = new MJMyApiTradeRegItemModel();
			model.objectId = item.objectId;
			model.name = item.name;
			model.display = item.display;
			model.iconId = item.iconId;
			model.quantity = item.count;
			model.identified = item.identified;
			model.bless = item.templateBless;
			model.enchant = item.enchantLevel;
			model.elementalEnchant = item.elementalEnchantLevel;
			model.category = item.category.text();
			model.adena = item.itemId == L1ItemId.ADENA;
			return model;
		}
		
		int objectId;
		String name;
		String display;
		int iconId;
		int quantity;
		boolean identified;
		int bless;
		int enchant;
		int elementalEnchant;
		String category;
		boolean adena;
	}
	
	static class MJMyApiTradeRegRestrict{
		int adenaGenerateMax;
		int adenaGenerateUnit;
		int adenaMarketPrice;
		double gmCommision;
		double userCommision;
		MJMyApiTradeRegRestrict(){
			adenaGenerateMax = MJNCoinSettings.ADENA_GENERATE_MAX;
			adenaGenerateUnit = MJNCoinSettings.ADENA_GENERATE_UNIT;
			adenaMarketPrice = MJNCoinSettings.ADENA_MARKET_PRICE;
			gmCommision = MJNCoinSettings.GM_COMMISSION;
			userCommision = MJNCoinSettings.USER_COMMISSION;
		}
	}
}
