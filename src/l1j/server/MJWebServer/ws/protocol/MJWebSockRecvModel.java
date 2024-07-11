package l1j.server.MJWebServer.ws.protocol;

import l1j.server.MJWebServer.ws.MJWebSockRequest;

public abstract class MJWebSockRecvModel{	
	public abstract void callback(MJWebSockRequest request);
}
