package l1j.server.MJWebServer.Dispatcher.Login;

public enum MJEHttpLoginValidation {
	SUCCESS(0, "SUCCESS"),										// �α��� ����
	FAIL_NOT_FOUND_PARAMETERS(1, "FAIL_NOT_FOUND_PARAMETERS"),	// �� �Ķ���Ͱ� ����
	FAIL_NOT_FOUND_ACCOUNT(2, "FAIL_NOT_FOUND_ACCOUNT"),		// ���� ������ ã�� �� ����
	FAIL_INVALID_ACCOUNT(3, "FAIL_INVALID_ACCOUNT"),			// ���� ������ Ʋ��
	FAIL_HMAC(4, "FAIL_HMAC"),									// mac ������ Ʋ��
	FAIL_HDD_BAN(5, "FAIL_HDD_BAN"),							// �ش� ����ڴ� �̿����� ��� �Դϴ�.
	CLOSE(6, "CLOSE"),											// ���ӱ� ����.
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
