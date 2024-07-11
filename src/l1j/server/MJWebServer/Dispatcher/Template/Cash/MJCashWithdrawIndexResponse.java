package l1j.server.MJWebServer.Dispatcher.Template.Cash;

import java.util.ArrayList;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJTemplate.MJKeyValuePair;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpCharacterInfo;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJCashWithdrawIndexResponse extends MJHttpResponse {
	private static String m_page_document;

	public MJCashWithdrawIndexResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		if (Config.Web.appcenterCacheReset) {
			m_page_document = load_file_string("./appcenter/btn_withdrawal_index.html");
		}

		ArrayList<MJKeyValuePair<String, String>> params = MJHttpCharacterInfo.getCharacterInfo(_user);

		String document = MJString.replace(m_page_document, params);
		HttpResponse response = create_response(HttpResponseStatus.OK, document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
}
