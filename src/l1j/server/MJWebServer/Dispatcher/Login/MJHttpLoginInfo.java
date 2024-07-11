package l1j.server.MJWebServer.Dispatcher.Login;

import java.util.Base64;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.utils.MJCommons;

public class MJHttpLoginInfo {
	public static MJHttpLoginInfo newInstance(){
		return new MJHttpLoginInfo();
	}

	private String m_client_ip;
	private int m_client_port;
	private String m_account;
	private String m_password;
	private String m_auth_token;
	private String m_mac_address;
	private String m_hdd_id;
	private String m_nic_id;
	private MJHttpLoginInfo(){
		m_client_ip = MJString.EmptyString;
		m_client_port = 0;
		m_account = MJString.EmptyString;
		m_password = MJString.EmptyString;
		m_auth_token = MJString.EmptyString;
		m_mac_address = MJString.EmptyString;
		m_hdd_id = MJString.EmptyString;
		m_nic_id = MJString.EmptyString;
	}

	public MJHttpLoginInfo set_client_ip(String client_ip){
		m_client_ip = client_ip;
		return this;
	}
	public MJHttpLoginInfo set_client_port(int client_port){
		m_client_port = client_port;
		return this;
	}
	public MJHttpLoginInfo set_account(String account){
		m_account = account;
		return this;
	}
	public MJHttpLoginInfo set_password(String password){
		m_password = password;
		return this;
	}
	public MJHttpLoginInfo set_auth_token(String auth_token){
		m_auth_token = auth_token;
		return this;
	}
	public String get_client_ip(){
		return m_client_ip;
	}
	public int get_client_port() {
		return m_client_port;
	}
	public String get_account(){
		return m_account;
	}
	public String get_password(){
		return m_password;
	}
	public String get_auth_token(){
		return m_auth_token;
	}
	public MJHttpLoginInfo set_mac_address(String mac_address){
		m_mac_address = mac_address;
		return this;
	}
	public MJHttpLoginInfo set_hdd_id(String hdd_id){
		m_hdd_id = hdd_id;
		return this;
	}
	public MJHttpLoginInfo set_nic_id(String nic_id){
		m_nic_id = nic_id;
		return this;
	}
	public String get_mac_address(){
		return m_mac_address;
	}
	public String get_hdd_id(){
		return m_hdd_id;
	}
	public String get_nic_id(){
		return m_nic_id;
	}
	
	// 계정 비번 관련 유효성 검사만 해줄 것.
	public MJEHttpLoginValidation validation() {
		if(!check_parameters())
			return MJEHttpLoginValidation.FAIL_NOT_FOUND_PARAMETERS;
		
		Account account = Account.load(get_account());
		if(account == null){
			return MJEHttpLoginValidation.FAIL_NOT_FOUND_ACCOUNT;
		}
		if(!account.get_Password().equalsIgnoreCase(get_password())){
			return MJEHttpLoginValidation.FAIL_INVALID_ACCOUNT;
		}
		
		GameClient clnt = LoginController.getInstance().getClientByAccount(get_account());
		if(clnt != null) {
			try {
				SC_CUSTOM_MSGBOX.do_kick(clnt, String.format("누군가가 %s 계정으로 접속을 시도하여 종료합니다.", get_account()));
				System.out.println(String.format("[IP:%s]에서 %s 계정으로 접속을 시도하여 종료합니다.", get_client_ip(), get_account()));
				clnt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// if(없는 계정) -> MJEHttpLoginValidation.FAIL_NOT_FOUND_ACCOUNT
		// 하위 조건 체크....아이피, 계정 길이, 공격 여부, 밴, 기타 이미 로그인됐는지 등등
		
		return MJEHttpLoginValidation.SUCCESS;
	}
	
	private boolean check_parameters() {
		return !MJString.isNullOrEmpty(m_client_ip) &&
				!MJString.isNullOrEmpty(m_account) &&
				!MJString.isNullOrEmpty(m_password) &&
				m_account.length() <= 40 &&
				m_password.length() <= 40;
	}
	
	public void make_auth_token() {
		byte[] encoded = String.format("%s.%d", m_account, System.currentTimeMillis()).getBytes(MJEncoding.UTF8);
		byte[] password = m_password.getBytes(MJEncoding.UTF8);
		MJCommons.encode_xor(encoded, password, 0, encoded.length);
		MJCommons.PK_T_LOG(encoded);
		m_auth_token = String.format("%s=", Base64.getEncoder().encodeToString(encoded));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		sb.append("ip : ").append(m_client_ip).append(", ");
		sb.append("account : ").append(m_account).append(", ");
		sb.append("password : ").append(m_password).append(", ");
		sb.append("auth_token : ").append(m_auth_token).append(", ");
		sb.append("mac_address : ").append(m_mac_address).append(", ");
		sb.append("hdd_id : ").append(m_hdd_id).append(", ");
		sb.append("nic_id : ").append(m_nic_id).append("");
		return sb.toString();
	}

}

