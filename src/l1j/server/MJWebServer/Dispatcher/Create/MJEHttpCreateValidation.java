package l1j.server.MJWebServer.Dispatcher.Create;

public enum MJEHttpCreateValidation {
	SUCCESS(0, "SUCCESS"),											// �α��� ����
	FAIL_HMAC(1, "FAIL_HMAC"),										// mac ������ Ʋ��
	FAIL_NOT_FOUND_PARAMETERS(2, "FAIL_NOT_FOUND_PARAMETERS"),		// �� �Ķ���Ͱ� ����
	FAIL_DUPLICATE_ACCOUNT(3, "FAIL_DUPLICATE_ACCOUNT"),			// �̹� �ִ� �����Դϴ�.
	FAIL_2CHECK_IP(4, "FAIL_2CHECK_IP"),							// ���� ip�� ���� �����ʰ�
	FAIL_HDD_BAN(5, "FAIL_HDD_BAN"),								// �ش� ����ڴ� �̿����� ��� �Դϴ�.
	CLOSE(6, "CLOSE"),												// ���ӱ� ����.
	CLOSE_WINDOW(7, "CLOSE_WINDOW"),								// �ش� ���ӱ� â �޴�����
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