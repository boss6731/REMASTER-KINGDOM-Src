package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJWebServer.Dispatcher.my.service.MJMyApiPageService.AppDownloadMenuMapped;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyApiPageService.MenuMapped;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyApiPageService.ShortcutMenuMapped;

class MJMyApiPageModel extends MJMyApiModel{	
	MenuMapped menuMapped;
	ShortcutMenuMapped shortcutMenuMapped;
	AppDownloadMenuMapped downloadMapped;
	MJMyApiPageModel(){
		super();
	}
}
