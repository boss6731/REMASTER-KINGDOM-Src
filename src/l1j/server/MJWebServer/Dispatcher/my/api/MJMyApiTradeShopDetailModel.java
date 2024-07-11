package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopDetailModel;

class MJMyApiTradeShopDetailModel extends MJMyApiModel{
	List<MJMyTradeShopDetailModel> models;
	MJMyPageNavigation navigation;
	MJMyApiTradeShopDetailModel(){
		super();
		navigation = new MJMyPageNavigation();
	}
}
