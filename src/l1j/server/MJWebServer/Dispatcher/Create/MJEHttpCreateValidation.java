package l1j.server.MJWebServer.Dispatcher.Create;

public enum MJEHttpCreateValidation {
	SUCCESS(0, "SUCCESS"),											// 로그인 성공
	FAIL_HMAC(1, "FAIL_HMAC"),										// mac 정보가 틀림
	FAIL_NOT_FOUND_PARAMETERS(2, "FAIL_NOT_FOUND_PARAMETERS"),		// 웹 파라미터가 누락
	FAIL_DUPLICATE_ACCOUNT(3, "FAIL_DUPLICATE_ACCOUNT"),			// 이미 있는 계정입니다.
	FAIL_2CHECK_IP(4, "FAIL_2CHECK_IP"),							// 동일 ip로 계정 생성초과
	FAIL_HDD_BAN(5, "FAIL_HDD_BAN"),								// 해당 사용자는 이용정지 대상 입니다.
	CLOSE(6, "CLOSE"),												// 접속기 종료.
	CLOSE_WINDOW(7, "CLOSE_WINDOW"),								// 해당 접속기 창 메뉴종료
	;
	
	private int m_type;
	private String m_message;
	MJEHttpCreateValidation(int type, String message){
		m_type = type;
		m_message = message;
	}
	
	public int get_int(){
		return m_type;
	}
	
	public String get_message() {
		return m_message;
	}
	
	public boolean equals(MJEHttpCreateValidation validation) {
		return validation.get_int() == get_int();
	}
}