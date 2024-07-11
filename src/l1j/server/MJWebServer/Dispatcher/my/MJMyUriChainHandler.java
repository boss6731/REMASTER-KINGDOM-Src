package l1j.server.MJWebServer.Dispatcher.my;

import l1j.server.MJWebServer.Service.MJHttpRequest;

public interface MJMyUriChainHandler {
	public MJMyController onRequest(MJHttpRequest request, String requestUri);
}
