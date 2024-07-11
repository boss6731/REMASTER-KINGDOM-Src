package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyPrivateShopDetailModel;

class MJMyApiItemShopDetailModel extends MJMyApiModel{
	List<MJMyPrivateShopDetailModel> models;
	MJMyPageNavigation navigation;
	MJMyApiItemShopDetailModel(){
		super();
		navigation = new MJMyPageNavigation();
	}
}
