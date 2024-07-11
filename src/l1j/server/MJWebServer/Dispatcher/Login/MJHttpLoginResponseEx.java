package l1j.server.MJWebServer.Dispatcher.Login;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJNetSafeSystem.DriveSafe.MJAccountDriveInfo;
import l1j.server.MJNetSafeSystem.DriveSafe.MJHddIdChecker;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.MJWebServer.protect.MJWebAccessMonitorAdapter;

public class MJHttpLoginResponseEx extends MJHttpResponse{
	private static final MJWebAccessMonitorAdapter adapter = 
			MJWebAccessMonitorAdapter.newAdapter("MJHttpLoginResponseEx", "/outgame/login/...", 10000L, 5, 10, true);
	
	private String requestMac;
	private MJHttpLoginInfo login;
	public MJHttpLoginResponseEx(MJHttpRequest request) {
		super(request, false);
		parseParameters();
	}

	private void parseParameters(){
		login = MJHttpLoginInfo.newInstance();
		login.set_client_ip(m_request.get_remote_address_string());
		login.set_client_port(m_request.get_remote_address_port());
		login.set_account(m_request.read_parameters_at_once("account"));
		login.set_password(m_request.read_parameters_at_once("password"));
		login.set_hdd_id(m_request.read_parameters_at_once("hdd_id"));
		login.set_mac_address(m_request.read_parameters_at_once("mac_address"));
		login.set_nic_id(m_request.read_parameters_at_once("nic_id"));
		requestMac = m_request.read_parameters_at_once("asdsadasd");		
	}

	private HttpResponse result(MJEHttpLoginValidation validation) throws MJHttpClosedException{
		return result(validation, MJString.EmptyString);
	}
	
	private HttpResponse result(MJEHttpLoginValidation validation, String authToken) throws MJHttpClosedException{
		MJHttpLoginResult result = new MJHttpLoginResult();
		result.result_code = validation.get_message();
		result.auth_token = authToken;
		HttpResponse response = create_response(HttpResponseStatus.OK, MJJsonUtil.toJson(result, false));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
		return response;
	}
	
	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		if(adapter.onAccess(m_request)){
			return result(MJEHttpLoginValidation.CLOSE);
		}
		
		if(MJString.isNullOrEmpty(requestMac)){
			System.out.println(String.format("%s:%d 사용자가 웹에서 로그인 시도를 했습니다.", login.get_client_ip(), login.get_client_port()));
			return notFound("페이지를 찾을 수 없습니다.");
		}
		
		/*String hmac = MJHttpLoginManager.check_hmac(login.get_hdd_id(), login.get_mac_address(), "/outgame/login");
		if(!hmac.equalsIgnoreCase(requestMac)) {
			System.out.println(String.format("%s:%d 사용자 로그인 실패 invalid mac value\r\nrequest : %s,\r\nmake mac : %s", 
					login.get_client_ip(), login.get_client_port(), requestMac, hmac));
			return result(MJEHttpLoginValidation.FAIL_HMAC);
		}*/
		if(Config.Login.UseExConnect){
			int reason = MJHddIdChecker.get_denials(login.get_hdd_id());
			if(reason != MJHddIdChecker.DENIALS_TYPE_NONE && reason != MJHddIdChecker.DENIALS_TYPE_INVALID_HDD_ID){
				System.out.println(String.format("하드밴 당한 클라이언트 접속 차단 : %s, %s, %d", login.get_account(), login.get_hdd_id(), reason));
				return result(MJEHttpLoginValidation.FAIL_HDD_BAN);
			}
		}
		MJEHttpLoginValidation validate = login.validation();
		if(validate.equals(MJEHttpLoginValidation.SUCCESS)){
			login.make_auth_token();
			MJHttpLoginManager.getInstance().put_login_info(login);
			MJAccountDriveInfo.update_account_to_drive(login.get_account(), login.get_hdd_id());
			return result(validate, login.get_auth_token());
		}
		//TODO 원래는 주석아닌데 id가 존재하지 않으면 계속 떠서 주석함
//		System.out.println(String.format("[invalid http login parameters] %s", login.toString()));
		return result(validate);
	}


	
	
}
