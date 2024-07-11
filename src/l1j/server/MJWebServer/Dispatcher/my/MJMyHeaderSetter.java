package l1j.server.MJWebServer.Dispatcher.my;

import io.netty.handler.codec.http.HttpResponse;

public interface MJMyHeaderSetter {
	public void onHeaderSet(HttpResponse response);
}
