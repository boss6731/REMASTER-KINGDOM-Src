package l1j.server.MJWebServer.Dispatcher.Template.MiniGame;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJMiniGameIndexResponse extends MJHttpResponse {
	private static String m_page_document;

	public MJMiniGameIndexResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		L1PcInstance pc = find_player();
		String doc = MJString.EmptyString;
		if(pc == null) {
/*			doc = "<html><head></head><body>funcking</body></html>";*/
			if (Config.Web.appcenterCacheReset) {
//				m_page_document = load_file_string("./appcenter/minigame_index.html");
				m_page_document = load_file_string("./appcenter/Error_minigame_index.html");
			}	
			doc = m_page_document;
		}else {
			if (Config.Web.appcenterCacheReset) {
//				m_page_document = load_file_string("./appcenter/minigame_index.html");
				m_page_document = load_file_string("./appcenter/Error_minigame_index.html");
			}			
			doc = MJString.replace(m_page_document, "{MONSTER_POINT}", String.valueOf(pc.get_exp()));
		}	
		
		HttpResponse response = create_response(HttpResponseStatus.OK, doc);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
	
	private L1PcInstance find_player() {
		return _user == null ? null : L1World.getInstance().getPlayer(_user.getCharName());
	}
}
