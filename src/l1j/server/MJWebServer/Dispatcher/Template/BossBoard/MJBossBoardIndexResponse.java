package l1j.server.MJWebServer.Dispatcher.Template.BossBoard;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJBossBoardIndexResponse extends MJHttpResponse {
	private static String m_page_document;

	public MJBossBoardIndexResponse(MJHttpRequest request) {
		super(request);

	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
			if (Config.Web.appcenterCacheReset) {
				m_page_document = load_file_string("./appcenter/bosstime_index.html");
			}

		HttpResponse response = create_response(HttpResponseStatus.OK, m_page_document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
}
