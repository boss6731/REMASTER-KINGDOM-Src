package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopSearchModel;

class MJMyApiTradeSearchModel extends MJMyApiModel{
	List<MJMyShopSearchModel> models;
	MJMyPageNavigation navigation;
	MJMyApiTradeSearchModel(){
		super();
		navigation = new MJMyPageNavigation();
	}
}
