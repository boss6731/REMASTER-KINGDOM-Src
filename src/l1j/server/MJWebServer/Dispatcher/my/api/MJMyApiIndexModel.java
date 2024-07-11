package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.resource.MJMySlick;
import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService.MJMyNoticeServiceItemInfo;

class MJMyApiIndexModel extends MJMyApiModel{
	MJMySlick slickItems;
	MJMyApiIndexNoticeItem noticeItems;
	MJMyApiIndexModel(){
		noticeItems = new MJMyApiIndexNoticeItem();
	}
	
	static class MJMyApiIndexNoticeItem{
		List<MJMyNoticeServiceItemInfo> items;
	}
}
