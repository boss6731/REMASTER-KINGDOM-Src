package l1j.server.MJWebServer.Dispatcher.Login;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJNetSafeSystem.DriveSafe.MJAccountDriveInfo;
import l1j.server.MJNetSafeSystem.DriveSafe.MJHddIdChecker;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.Account;

public class MJHttpLoginResponse extends MJHttpResponse {
	public MJHttpLoginResponse(MJHttpRequest request) {
		super(request, false);	// application check falsed..
	}
	
	//private int check = 0;

	// 여기 로직은 건들지 말고,
	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		
		MJHttpLoginInfo lInfo = MJHttpLoginInfo.newInstance();
		InetSocketAddress 	inetAddr 	= (InetSocketAddress)m_request.get_remote_address();
		lInfo.set_client_ip(inetAddr.getAddress().getHostAddress());
		lInfo.set_client_port(inetAddr.getPort());
		
		Map<String, List<String>> m = m_request.get_parameters();
		String request_mac = MJString.EmptyString;
		for(String key : m.keySet()) {
			List<String> list = m.get(key);
			switch(key) {
			case "account":
				lInfo.set_account(list.get(0));
				break;
				
			case "password":
				lInfo.set_password(list.get(0));
				break;
				
			case "hdd_id":
				lInfo.set_hdd_id(list.get(0));
				break;
				
			case "mac_address":
				lInfo.set_mac_address(list.get(0));
				break;

			case "nic_id":
				lInfo.set_nic_id(list.get(0));
				break;

			case "asdsadasd":
				request_mac = list.get(0);
				break;
			}
		}
		if(MJString.isNullOrEmpty(request_mac)) {
			System.out.println(String.format("%s:%d 사용자가 웹에서 로그인 시도를 했습니다.", lInfo.get_client_ip(), lInfo.get_client_port()));
			HttpResponse response = create_response(HttpResponseStatus.OK, "ㅋㅋㅋㅋㅋㅋㅋㅋㅋ");
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
			return response;	
		}
		
		String hmac =  MJHttpLoginManager.check_hmac(lInfo.get_hdd_id(), lInfo.get_mac_address(), "/outgame/login");
		MJHttpLoginResult result = new MJHttpLoginResult();
		
		/*if (check >= 5) {
			result.result_code = MJEHttpLoginValidation.CLOSE.get_message();
			Gson gson = new Gson();
			String json = gson.toJson(result);
			HttpResponse response = create_response(HttpResponseStatus.OK, json);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
			check = 0;
			return response;
		}*/
		
		if (Config.Login.UseExConnect) {
			int reason = MJHddIdChecker.get_denials(lInfo.get_hdd_id());
			if (reason != MJHddIdChecker.DENIALS_TYPE_NONE
					&& reason != MJHddIdChecker.DENIALS_TYPE_INVALID_HDD_ID) {
				result.result_code = MJEHttpLoginValidation.FAIL_HDD_BAN.get_message();
				System.out.println(String.format("하드밴 당한 클라이언트 접속 차단 : %s, %s, %d", lInfo.get_account(), lInfo.get_hdd_id(), reason));

				Gson gson = new Gson();
				String json = gson.toJson(result);
				HttpResponse response = create_response(HttpResponseStatus.OK, json);
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
				//check++;
				return response;
			}
			MJAccountDriveInfo.update_account_to_drive(lInfo.get_account(), lInfo.get_hdd_id());
		}
		
		Account account = null;
		account = Account.load(lInfo.get_account());
		if (account == null) {
			result.result_code = MJEHttpLoginValidation.FAIL_NOT_FOUND_ACCOUNT.get_message();		
			Gson gson = new Gson();
			String json = gson.toJson(result);
			HttpResponse response = create_response(HttpResponseStatus.OK, json);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
			//check++;
			return response;
		}
		
		if (!account.get_Password().equalsIgnoreCase(lInfo.get_password())) {
			result.result_code = MJEHttpLoginValidation.FAIL_INVALID_ACCOUNT.get_message();		
			Gson gson = new Gson();
			String json = gson.toJson(result);
			HttpResponse response = create_response(HttpResponseStatus.OK, json);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
			//check++;
			return response;
		}
		
		if(!hmac.equalsIgnoreCase(request_mac)) {
			System.out.println(String.format("%s:%d 사용자 로그인 실패 invalid mac value\r\nrequest : %s,\r\nmake mac : %s", lInfo.get_client_ip(), lInfo.get_client_port(), request_mac, hmac));
			result.result_code = MJEHttpLoginValidation.FAIL_HMAC.get_message();
			//check++;
		}else {
			MJEHttpLoginValidation validate = lInfo.validation();
			result.result_code = validate.get_message();
			if(MJEHttpLoginValidation.SUCCESS.equals(validate)) {
				lInfo.make_auth_token();
				result.auth_token = lInfo.get_auth_token();
				MJHttpLoginManager.getInstance().put_login_info(lInfo);
			}else {
				System.out.println(String.format("[invalid http login parameters] %s", lInfo.toString()));
				//check++;
			}
		}
		Gson gson = new Gson();
		String json = gson.toJson(result);
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

}