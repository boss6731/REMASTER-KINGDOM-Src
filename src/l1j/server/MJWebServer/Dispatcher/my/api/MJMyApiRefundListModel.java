package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.refund.MJMyRefundServiceItem;

class MJMyApiRefundListModel extends MJMyApiModel{
	List<MJMyRefundServiceItem> items;
	MJMyPageNavigation pageNavigation;
	MJMyApiRefundListModel(){
		super();
		pageNavigation = new MJMyPageNavigation();
	}
}
