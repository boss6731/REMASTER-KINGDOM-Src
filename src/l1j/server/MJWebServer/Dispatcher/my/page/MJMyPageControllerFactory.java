package l1j.server.MJWebServer.Dispatcher.my.page;

import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageMapped.MJMyPageInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

interface MJMyPageControllerFactory {
	MJMyPageController controller(MJHttpRequest request, MJMyPageInfo pInfo);
}
