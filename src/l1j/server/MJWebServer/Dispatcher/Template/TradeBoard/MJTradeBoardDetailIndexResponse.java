package l1j.server.MJWebServer.Dispatcher.Template.TradeBoard;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJTradeBoardDetailIndexResponse extends MJHttpResponse {
	private static String m_page_document;

	public MJTradeBoardDetailIndexResponse(MJHttpRequest request) {
		super(request);

	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		if (Config.Web.appcenterCacheReset) {
			m_page_document = load_file_string("./appcenter/itemtrade_detail.html");
		}

		HttpResponse response = create_response(HttpResponseStatus.OK, m_page_document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

}
