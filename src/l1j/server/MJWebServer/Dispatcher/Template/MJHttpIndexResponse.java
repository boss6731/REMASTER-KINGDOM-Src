package l1j.server.MJWebServer.Dispatcher.Template;

import java.util.ArrayList;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJTemplate.MJFormatter;
import l1j.server.MJTemplate.MJKeyValuePair;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJHttpIndexResponse extends MJHttpResponse {
	private static String m_page_document;

	public MJHttpIndexResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		ArrayList<MJKeyValuePair<String, String>> params = new ArrayList<MJKeyValuePair<String, String>>();

		if (Config.Web.appcenterCacheReset) {
			m_page_document = load_file_string("./appcenter/index.html");
		}
		
//		params.add(new MJKeyValuePair<String, String>("{ACCOUNT}", _player.getAccountName()));
//		params.add(new MJKeyValuePair<String, String>("{PASSWORD}", _player.getAccount().get_Password()));
		//////
		params.add(new MJKeyValuePair<String, String>("{CHARACTER_NAME}", _user != null ? _user.getCharName() : "-"));
		params.add(new MJKeyValuePair<String, String>("{LEVEL}", _user != null ? String.valueOf(_user.getLevel()) : "-"));
		params.add(new MJKeyValuePair<String, String>("{NOW}", MJFormatter.get_tdouble_formatter_time()));
		params.add(new MJKeyValuePair<String, String>("{SERVER_NAME}", Config.Message.GameServerName));
		params.add(new MJKeyValuePair<String, String>("{PROFILE_URL}", "https://wstatic-cdn.plaync.com/resource/lineage/profile/char" + _user.getClassId() + "_" + (_user.getGender() == 0 ? "m" : "f") + ".png"));
		params.add(new MJKeyValuePair<String, String>("{NCOIN}", _player != null ? MJString.parse_money_string(_player.getAccount().Ncoin_point) : "0"));

		String document = MJString.replace(m_page_document, params);
		HttpResponse response = create_response(HttpResponseStatus.OK, document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
}
