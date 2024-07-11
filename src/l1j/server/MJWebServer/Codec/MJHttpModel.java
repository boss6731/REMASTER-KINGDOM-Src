package l1j.server.MJWebServer.Codec;

import io.netty.handler.codec.http.HttpResponse;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;

public interface MJHttpModel {
	public HttpResponse getResponse() throws MJHttpClosedException;
}
