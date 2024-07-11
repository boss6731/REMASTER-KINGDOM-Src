package l1j.server.MJWebServer.Dispatcher.my.service.gmtools;

import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJWebServer.ws.MJWebSockRequest;

public interface MJMyGmWriter {
	public void write(Matcher<MJWebSockRequest> matcher, MJMyGmModel model);
}
