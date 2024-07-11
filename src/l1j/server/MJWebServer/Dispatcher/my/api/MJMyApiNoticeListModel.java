package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService.MJMyNoticeServiceItemInfo;

class MJMyApiNoticeListModel extends MJMyApiModel {
	MJMyPageNavigation pageNavigation;
	List<MJMyNoticeServiceItemInfo> items;
	MJMyApiNoticeListModel(){
		super();
		pageNavigation = new MJMyPageNavigation();
	}
}
