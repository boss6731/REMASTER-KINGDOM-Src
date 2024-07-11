package l1j.server.MJWebServer.Dispatcher.Template.PowerBall;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MjPowerBallViewIndexResponse extends MJHttpResponse {
	private static String m_page_document;

	public MjPowerBallViewIndexResponse(MJHttpRequest request) {
		super(request, false);
	}

	@override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
// TODO: 如果 appcenterCacheReset 為 false 且 m_page_document 為 null，需要處理異常，確保頁面能正常加載。
		if (Config.Web.appcenterCacheReset || m_page_document == null) {
			StringBuilder sb = new StringBuilder(512);
			sb.append("<iframe width="1030" height="720" src="http://ntry.com/scores/powerball/live.php" width="830" height="641" scrolling="no" style="position: absolute; top:-80px; left:-250px;"></iframe>");
			m_page_document = sb.toString();
// m_page_document = load_file_string("./appcenter/poweball_view_index.html");
		}

		HttpResponse response = create_response(HttpResponseStatus.OK, m_page_document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
}
