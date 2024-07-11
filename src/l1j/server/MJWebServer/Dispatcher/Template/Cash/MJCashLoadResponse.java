package l1j.server.MJWebServer.Dispatcher.Template.Cash;

import com.google.gson.JsonObject;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.POJO.AppCashInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJCashLoadResponse extends MJHttpResponse {

	public MJCashLoadResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		AppCashInfo aci = AppCashInfo.loadDatabaseCashInfo(_user.getCharName());
		
		JsonObject json = new JsonObject();
		json.addProperty("account_pass", aci._account_pass);
		json.addProperty("account_secend_pass", aci._account_secend_pass);
		json.addProperty("bank_name", aci._bank_name);
		json.addProperty("bank_number", aci._bank_number);
		json.addProperty("user_name", aci._user_name);

		HttpResponse response = create_response(HttpResponseStatus.OK, json.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
}
