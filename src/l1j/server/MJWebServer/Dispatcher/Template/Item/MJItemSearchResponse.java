package l1j.server.MJWebServer.Dispatcher.Template.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJTemplate.MJKeyValuePair;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJItemSearchResponse extends MJHttpResponse {
	private static String m_page_document;

	public MJItemSearchResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		if (Config.Web.appcenterCacheReset) {
			m_page_document = load_file_string("./appcenter/item_search.html");
		}

		Map<String, List<String>> map = m_request.get_parameters();
		List<String> queries = map.get("query");
		String query = queries == null || queries.size() <= 0 ? MJString.EmptyString : queries.get(0);
		ArrayList<MJKeyValuePair<String, String>> params = new ArrayList<MJKeyValuePair<String, String>>();
		params.add(new MJKeyValuePair<String, String>("{SEARCH_QUERY}", query));
		String document = MJString.replace(m_page_document, params);
		HttpResponse response = create_response(HttpResponseStatus.OK, document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
}
