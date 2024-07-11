package l1j.server.MJWebServer.Dispatcher.Template;

import MJFX.MJFxEntry;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJHttpFileResponse extends MJHttpResponse{
	public MJHttpFileResponse(MJHttpRequest request){
		super(request);
	}
	
	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		String request_uri = m_request.get_request_uri();
		
		if(request_uri.startsWith("/ingame/item/"))
			request_uri = MJString.replace(request_uri, "/ingame/item/", "/img/powerbook/");
		String filename = String.format("./appcenter%s", request_uri);
		if(filename.equals("./appcenter/")){
			return create_empty(HttpResponseStatus.NOT_FOUND);
		}
		//String filename = "./appcenter/minigame_index2.html";
			
		byte[] buff = load_file(filename);
		if(buff == null){
			append_log(String.format("載入失敗文件。 %s", filename));
			return create_empty(HttpResponseStatus.NOT_FOUND);
		}
		
		HttpResponse response = create_response(HttpResponseStatus.OK, buff);
		if(!MJFxEntry.IS_DEBUG_MODE) {
			response.headers().set(HttpHeaderNames.CACHE_CONTROL, "max-age=604800");
		}
		return response;
	}

}
