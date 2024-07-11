package l1j.server.MJWebServer.Dispatcher.Create;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJNetSafeSystem.DriveSafe.MJAccountDriveInfo;
import l1j.server.MJNetSafeSystem.DriveSafe.MJHddIdChecker;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.Login.MJHttpLoginManager;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.MJWebServer.protect.MJWebAccessMonitorAdapter;
import l1j.server.server.Account;
import l1j.server.server.AccountService;
import l1j.server.server.AccountService.AddressOverException;
import l1j.server.server.AccountService.AlreadyAccountsException;

public class MJHttpAccountCreateResponseEx extends MJHttpResponse{
	private static final MJWebAccessMonitorAdapter adapter = 
			MJWebAccessMonitorAdapter.newAdapter("MJHttpAccountCreateResponseEx", "/outgame/create/...", 10000L, 5, 10, true);
	
	private String account;
	private String password;
	private String phone;
	private String hddId;
	private String macAddress;
	@SuppressWarnings("unused")
	private String nicId;
	private String requestMac;
	public MJHttpAccountCreateResponseEx(MJHttpRequest request){
		super(request, false);
		account = password = phone = hddId = macAddress = nicId = requestMac = MJString.EmptyString;
		parseParameters();
	}
	
	private void parseParameters(){
		account = m_request.read_parameters_at_once("account");
		password = m_request.read_parameters_at_once("password");
		phone = m_request.read_parameters_at_once("phone");
		hddId = m_request.read_parameters_at_once("hdd_id");
		macAddress = m_request.read_parameters_at_once("mac_address");
		nicId = m_request.read_parameters_at_once("nic_id");
		requestMac = m_request.read_parameters_at_once("asdsadasd");
	}
	
	private HttpResponse result(MJEHttpCreateValidation validation) throws MJHttpClosedException{
		MJHttpCreateResult result = new MJHttpCreateResult();
		result.result_code = validation.get_message();
		HttpResponse response = create_response(HttpResponseStatus.OK, MJJsonUtil.toJson(result, false));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
		return response;
	}
	
	private boolean validateParameters(){
		return !MJString.isNullOrEmpty(account) &&
				!MJString.isNullOrEmpty(password) &&
				!MJString.isNullOrEmpty(hddId) &&
				account.length() <= 40 &&
				password.length() <= 40;
	}

	private void print(String message){
		System.out.println(String.format("%s:%d %s", m_request.get_remote_address_string(), m_request.get_remote_address_port(), message));
	}
	
	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		if(adapter.onAccess(m_request)){
			return result(MJEHttpCreateValidation.CLOSE);
		}
		if(!Config.Synchronization.AutoCreateAccounts){
			return notFound("페이지를 찾을 수 없습니다.");
		}
		if(MJString.isNullOrEmpty(requestMac)){
			print("사용자가 웹에서 로그인 시도를 했습니다.");
			return notFound("페이지를 찾을 수 없습니다.");
		}
		if(!validateParameters()){
			return result(MJEHttpCreateValidation.FAIL_NOT_FOUND_PARAMETERS);
		}
		/*String hmac = MJHttpLoginManager.check_hmac(hddId, macAddress, "/outgame/create");
		if (!hmac.equalsIgnoreCase(requestMac)) {
			print(String.format("사용자 로그인 실패 invalid mac value\r\nrequest : %s,\r\nmake mac : %s", requestMac, hmac));
			return result(MJEHttpCreateValidation.FAIL_HMAC);
		}*/
		//if(Config.Login.UseExConnect){
			int reason = MJHddIdChecker.get_denials(hddId);
			if (reason != MJHddIdChecker.DENIALS_TYPE_NONE && reason != MJHddIdChecker.DENIALS_TYPE_INVALID_HDD_ID) {
				print(String.format("하드밴 당한 클라이언트 접속 차단 : %s, %s, %d", account, hddId, reason));
				return result(MJEHttpCreateValidation.FAIL_HDD_BAN);
			}
			if(MJHddIdChecker.numOfHddCount(hddId) >= MJNetServerLoadManager.DESKTOP_ADDRESS2ACCOUNT){
				print(String.format("사용자 하드아이디 계정 생성 초과 : %s %s", account, hddId));
				return result(MJEHttpCreateValidation.FAIL_2CHECK_IP);
			}
		//}
		try {
			Account aInfo = AccountService.service().onNewAccount(account, password, m_request.get_remote_address_string(), phone);
			if(aInfo == null){
				return result(MJEHttpCreateValidation.FAIL_DUPLICATE_ACCOUNT);
			}else{
				MJAccountDriveInfo.update_account_to_drive(account, hddId);
				return result(MJEHttpCreateValidation.SUCCESS);
			}
		} catch (AlreadyAccountsException e) {
			return result(MJEHttpCreateValidation.FAIL_DUPLICATE_ACCOUNT);
		} catch(AddressOverException e){
			return result(MJEHttpCreateValidation.FAIL_2CHECK_IP);
		}
	}
	
	
}
