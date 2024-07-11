package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiEmptyController extends MJMyApiController{
	MJMyApiEmptyController(MJHttpRequest request) {
		super(request);
	}

	@Override
	public MJMyModel viewModel() {
		return failModel("과도한 트래픽으로 의심되어 일시적으로 사용이 제한됩니다.");
	}
	
	@Override
	protected boolean isNeedLogin() {
		return false;
	}

	@Override
	protected MJMyModel responseModel() {
		return null;
	}

}
