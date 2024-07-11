package l1j.server.MJWebServer.Dispatcher.Template.Blood;

import java.util.ArrayList;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJTemplate.MJKeyValuePair;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJBloodIndexResponse extends MJHttpResponse {
	private static String m_page_document;

	public MJBloodIndexResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
			if (Config.Web.appcenterCacheReset) {
				m_page_document = load_file_string("./appcenter/blood_index.html");
			}

		ArrayList<MJKeyValuePair<String, String>> params = new ArrayList<MJKeyValuePair<String, String>>();
		params.add(new MJKeyValuePair<String, String>("{CHARACTER_NAME}", _player != null ? _player.getName() : "¹æ¹®ÀÚ"));
		String document = MJString.replace(m_page_document, params);
		HttpResponse response = create_response(HttpResponseStatus.OK, document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

}
