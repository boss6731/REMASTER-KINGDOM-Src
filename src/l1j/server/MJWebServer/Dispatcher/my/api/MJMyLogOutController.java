package l1j.server.MJWebServer.Dispatcher.my.api;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyHeaderSetter;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserGroup;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyLogOutController extends MJMyApiController {
	MJMyLogOutController(MJHttpRequest request) {
		super(request);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		final MJMyLogOutModel model = new MJMyLogOutModel();
		model.authToken = authToken();
		if(!MJString.isNullOrEmpty(model.authToken)){
			MJMyUserGroup.group().remove(model.authToken);
		}
		model.code = MJMyApiModel.REDIRECT;
		model.nextLocation = MJMyResource.construct().auth().authUri();
		return new MJMyJsonModel(request(), model, new MJMyHeaderSetter(){
			@Override
			public void onHeaderSet(HttpResponse response) {
				if(!MJString.isNullOrEmpty(model.authToken)){
					Cookie cookie = new DefaultCookie("authToken", model.authToken);
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.headers().set(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
				}
			}
		}); 
	}

}
