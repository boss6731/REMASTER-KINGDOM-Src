package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiEmptyController extends MJMyApiController{
	MJMyApiEmptyController(MJHttpRequest request) {
		super(request);
	}

	@Override
	public MJMyModel viewModel() {
		return failModel("������ Ʈ�������� �ǽɵǾ� �Ͻ������� ����� ���ѵ˴ϴ�.");
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
