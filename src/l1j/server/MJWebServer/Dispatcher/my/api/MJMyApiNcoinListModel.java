package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.ncoin.MJMyNcoinServiceItem;

class MJMyApiNcoinListModel extends MJMyApiModel{
	
	List<MJMyNcoinServiceItem> items;
	MJMyPageNavigation pageNavigation;
	MJMyApiNcoinListModel(){
		super();
		pageNavigation = new MJMyPageNavigation();
	}
}
