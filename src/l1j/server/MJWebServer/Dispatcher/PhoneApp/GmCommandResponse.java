package l1j.server.MJWebServer.Dispatcher.PhoneApp;

import java.util.Map;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.Account;
import l1j.server.server.GMCommands;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class GmCommandResponse extends MJHttpResponse {
	private static String m_page_document;
	private String _account;
	private String _password;
	private String _command;
	private String _phoneNumber;
	
	public GmCommandResponse(MJHttpRequest request) {
		super(request);

		Map<String, String> post_datas = m_request.get_post_datas();
		
		for (String k : post_datas.keySet()) {
			switch (k) {
			case "Account":
				_account = post_datas.get(k);
				break;
			case "Password":
				_password = post_datas.get(k);
				break;
			case "Command":
				_command = post_datas.get(k);
				break;
			case "PhoneNumber":
				_phoneNumber = post_datas.get(k);
				break;
			}
		}
	}

	@Override
	public HttpResponse get_response() {

		StringBuilder sb = new StringBuilder();
		boolean login_ok = true;
		try {
			if (_account == null || _account.length() == 0) {
				sb.append("�������� �߸��Ǿ����ϴ�.");
				login_ok = false;
			}

			if (!isValidAccount(_account)) {
				sb.append("�������� �߸��Ǿ����ϴ�.");
				login_ok = false;
			}

			if (!isValidPassword(_password)) {
				sb.append("�������� �߸��Ǿ����ϴ�.");
				login_ok = false;
			}
			
			//System.out.println(Config.Login.ServerGmPhoneNumber + " / " + _phoneNumber);
			if (!Config.Login.ServerGmPhoneNumber.equalsIgnoreCase(_phoneNumber)) {
				sb.append("��� �ڵ����� �ƴմϴ�.");
				login_ok = false;
			}
			
			if (login_ok) {
				Account account = Account.load(_account);
				if (account == null || !account.validatePassword(_account, _password)) {
					sb.append("�������� �߸��Ǿ����ϴ�.");
				} else {
					sb.append("����� �� ó�� �Ǿ����ϴ�.");
					L1PcInstance pc = L1World.getInstance().getPlayer("��Ƽ��");
					if(pc == null) {
						pc = L1PcInstance.load("��Ƽ��");
					}
					
					if(pc == null) {
						sb.append("���(��Ƽ��) ĳ���� �������� �ʽ��ϴ�.");
					} else {
						if(pc.getAccountName().equalsIgnoreCase(_account)) {
							GMCommands.getInstance().handleCommands(pc, _command);
						} else {
							sb.append("�ش������ ���ĳ���Ͱ� ���� ������ �ƴմϴ�.");
						}
					}
				}
			}

		} catch (Exception e) {
			sb.append("�߸��� ��û�Դϴ�.");
			e.printStackTrace();
		}

		m_page_document = sb.toString();

		HttpResponse response = null;
		try {
			response = create_response(HttpResponseStatus.OK, m_page_document);
		} catch (MJHttpClosedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

	private boolean isValidAccount(String account) {
		if (account.length() < 5) {
			return false;
		}

		if (account.length() > 12) {
			return false;
		}

		char[] chars = account.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!Character.isLetterOrDigit(chars[i])) {
				return false;
			}
		}

		return true;
	}

	private boolean isValidPassword(String password) {
		if (password.length() < 6) {
			return false;
		}
		if (password.length() > 16) {
			return false;
		}

		boolean hasLetter = false;
		boolean hasDigit = false;

		char[] chars = password.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i])) {
				hasLetter = true;
			} else if (Character.isDigit(chars[i])) {
				hasDigit = true;
			} else {
				return false;
			}
		}

		if (!hasLetter || !hasDigit) {
			return false;
		}

		return true;
	}
}
