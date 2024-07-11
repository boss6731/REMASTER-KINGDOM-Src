package l1j.server.MJWebServer.Dispatcher.my.page;

import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.my.MJMyController;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageMapped.MJMyPageInfo;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Service.MJHttpRequest;

abstract class MJMyPageController extends MJMyController {
	protected MJMyPageInfo pInfo;
	MJMyPageController(MJHttpRequest request, MJMyPageInfo pInfo) {
		super(request);
		this.pInfo = pInfo;
	}

	@Override
	public MJMyModel viewModel() throws MJHttpClosedException{
		if(!loggedIn()){
			return needLogin();
		}
		if(pInfo.gm() && !gm()){
			return redirect(MJMyResource.construct().auth().nextUri());
		}
		
		return viewModelInternal();
	}
	
	protected abstract MJMyModel viewModelInternal();
}
