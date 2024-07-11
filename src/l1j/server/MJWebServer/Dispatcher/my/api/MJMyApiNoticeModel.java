package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyNotice.MJMyNoticeCategory;
import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService.MJMyNoticeServiceItemInfo;

class MJMyApiNoticeModel extends MJMyApiModel{
	MJMyPageNavigation pageNavigation;
	List<MJMyNoticeServiceItemInfo> items;
	List<MJMyNoticeCategory> categories;
	MJMyApiNoticeModel(){
		super();
		pageNavigation = new MJMyPageNavigation();
	}
}
