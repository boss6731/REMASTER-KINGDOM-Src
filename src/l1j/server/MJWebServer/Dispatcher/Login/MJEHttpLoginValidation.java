package l1j.server.MJWebServer.Dispatcher.Login;

public enum MJEHttpLoginValidation {
	SUCCESS(0, "SUCCESS"),										// 로그인 성공
	FAIL_NOT_FOUND_PARAMETERS(1, "FAIL_NOT_FOUND_PARAMETERS"),	// 웹 파라미터가 누락
	FAIL_NOT_FOUND_ACCOUNT(2, "FAIL_NOT_FOUND_ACCOUNT"),		// 계정 정보를 찾을 수 없음
	FAIL_INVALID_ACCOUNT(3, "FAIL_INVALID_ACCOUNT"),			// 계정 정보가 틀림
	FAIL_HMAC(4, "FAIL_HMAC"),									// mac 정보가 틀림
	FAIL_HDD_BAN(5, "FAIL_HDD_BAN"),							// 해당 사용자는 이용정지 대상 입니다.
	CLOSE(6, "CLOSE"),											// 접속기 종료.
	;
	
	private int m_type;
	private String m_message;
	MJEHttpLoginValidation(int type, String message){
		m_type = type;
		m_message = message;
	}
	
	public int get_int(){
		return m_type;
	}
	
	public String get_message() {
		return m_message;
	}
	
	public boolean equals(MJEHttpLoginValidation validation) {
		return validation.get_int() == get_int();
	}
}
