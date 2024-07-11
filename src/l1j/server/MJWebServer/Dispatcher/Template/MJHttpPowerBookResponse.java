package l1j.server.MJWebServer.Dispatcher.Template;

import java.util.ArrayList;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJTemplate.MJKeyValuePair;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJHttpPowerBookResponse  extends MJHttpResponse{
	private static String m_page_document;
	
	private String m_query;
	public MJHttpPowerBookResponse(MJHttpRequest request, String query) {
		super(request);
		m_query = query;
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		if(Config.Web.appcenterCacheReset) {
			m_page_document = load_file_string("./appcenter/powerbook_search.html");
		}
		ArrayList<MJKeyValuePair<String, String>> params = new ArrayList<MJKeyValuePair<String, String>>();
		params.add(new MJKeyValuePair<String, String>("{QUERY}", m_query));
		String document = MJString.replace(m_page_document, params);
		HttpResponse response = create_response(HttpResponseStatus.OK, document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
}
