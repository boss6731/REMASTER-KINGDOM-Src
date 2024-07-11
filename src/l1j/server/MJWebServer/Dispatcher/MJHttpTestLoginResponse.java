package l1j.server.MJWebServer.Dispatcher;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJHttpTestLoginResponse extends MJHttpResponse {
	public MJHttpTestLoginResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		Map<String, List<String>> m = m_request.get_parameters();
		StringBuilder sb = new StringBuilder();
		sb.append("hkhkhkhk\r\n");
		
		String account = MJString.EmptyString;
		String password = MJString.EmptyString;
		for(String key : m.keySet()) {
			List<String> list = m.get(key);
			switch(key) {
			case "account":
				account = list.get(0);
				break;
				
			case "password":
				password = list.get(0);
				break;
				
			case "hdd_id":
				break;
				
			case "mac_address":
				break;
				
			case "nic_id":
				break;
			}
		}
		
		
		//System.out.println(account + " " + password);
		
		// auth token base64 encoding after send.
		String eee = Base64.getEncoder().encodeToString("4men_server".getBytes());
		//HttpResponse response = create_response(HttpResponseStatus.OK, "M0MxNjRDRUItRDE1Ri1FMDExLTlBMDYtRTYxRjEzNUU5OTJGOjZEQ0RFNUZFLUQ1ODAtNDdBQi1CQ0FELTIwRkE4NTBFOThDNwA=");
		HttpResponse response = create_response(HttpResponseStatus.OK, eee);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

}
