package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService.MJMyNoticeServiceItemInfo;

class MJMyApiNoticeReadModel extends MJMyApiModel {
	boolean alreadyLike;
	MJMyNoticeServiceItemInfo noticeItem;
	String content;
	MJMyApiNoticeReadModel(){
		super();
	}
}
