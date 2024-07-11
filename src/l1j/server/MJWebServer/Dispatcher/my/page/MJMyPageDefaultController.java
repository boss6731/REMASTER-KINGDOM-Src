package l1j.server.MJWebServer.Dispatcher.my.page;

import l1j.server.MJWebServer.Dispatcher.my.MJMyBinaryModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyHtmlModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageMapped.MJMyPageInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyPageDefaultController extends MJMyPageController{
	MJMyPageDefaultController(MJHttpRequest request, MJMyPageInfo pInfo) {
		super(request, pInfo);
	}

	@Override
	protected MJMyModel viewModelInternal() {
		if(pInfo.binaryCache()){
			return new MJMyBinaryModel(request(), pInfo.pageBinary, MJMyHtmlModel.htmlContentType(), null);
		}else{
			return new MJMyHtmlModel(request(), pInfo.pageText, null);
		}
	}
}
