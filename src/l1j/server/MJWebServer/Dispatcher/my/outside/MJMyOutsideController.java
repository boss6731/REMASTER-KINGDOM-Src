package l1j.server.MJWebServer.Dispatcher.my.outside;

import l1j.server.MJWebServer.Dispatcher.my.MJMyBinaryModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyController;
import l1j.server.MJWebServer.Dispatcher.my.MJMyHtmlModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyOutsideController extends MJMyController{
	private byte[] data;
	MJMyOutsideController(MJHttpRequest request, byte[] data) {
		super(request);
		this.data = data;
	}

	@Override
	public MJMyModel viewModel(){
		return new MJMyBinaryModel(request(), data, MJMyHtmlModel.htmlContentType(), null);
	}
}
