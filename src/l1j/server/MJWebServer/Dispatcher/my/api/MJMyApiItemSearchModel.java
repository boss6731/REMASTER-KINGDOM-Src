package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopSearchModel;

class MJMyApiItemSearchModel extends MJMyApiModel{
	List<MJMyShopSearchModel> models;
	MJMyPageNavigation navigation;
	MJMyApiItemSearchModel(){
		super();
		navigation = new MJMyPageNavigation();
	}
}
