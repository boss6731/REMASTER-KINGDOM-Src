package l1j.server.MJWebServer.Dispatcher.PhoneApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.Account;
import l1j.server.server.utils.SQLUtil;

public class LetterIndexResponse extends MJHttpResponse {
	private static String m_page_document;
	private String _account;
	private String _password;
	private String _phoneNumber;
	
	public LetterIndexResponse(MJHttpRequest request) {
		super(request);

		Map<String, String> post_datas = m_request.get_post_datas();
		//System.out.println("\r\npost data");
		for (String k : post_datas.keySet()) {
			//System.out.println(k);
			switch (k) {
			case "Account":
				_account = post_datas.get(k);
				break;
			case "Password":
				_password = post_datas.get(k);
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
				sb.append("계정명이 잘못되었습니다.");
				login_ok = false;
			}

			if (!isValidAccount(_account)) {
				sb.append("계정명이 잘못되었습니다.");
				login_ok = false;
			}

			if (!isValidPassword(_password)) {
				sb.append("계정명이 잘못되었습니다.");
				login_ok = false;
			}
			
			if (Config.Login.ServerGmPhoneNumber.equalsIgnoreCase(_phoneNumber)) {
				sb.append("운영자 핸드폰이 아닙니다.");
				login_ok = false;
			}

			if (login_ok) {
				Account account = Account.load(_account);
				if (account == null || !account.validatePassword(_account, _password)) {
					sb.append("계정명이 잘못되었습니다.");
				} else {
					JSONObject jsonObject = new JSONObject();
					
					loadLetter(jsonObject);
					m_page_document = jsonObject.toJSONString();

					System.out.println(m_page_document);

					HttpResponse response = create_response(HttpResponseStatus.OK, m_page_document);
					response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
					return response;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(!login_ok){
			m_page_document = sb.toString();
		}
		
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

	private void loadLetter(JSONObject jsonObject) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM letter WHERE receiver=?");
			pstm.setString(1, "메티스");
			rs = pstm.executeQuery();
			
			JSONObject data = null;
			JSONArray req_array = new JSONArray();
			while (rs.next()) {
				data = new JSONObject();
				data.put("Title", rs.getString("subject"));
				data.put("Content", rs.getString("content"));
				data.put("Sender", rs.getString("sender"));
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date1 = format1.format(rs.getTimestamp("date").getTime()); 
				data.put("Date", date1);
				req_array.add(data);
			}
			jsonObject.put("DATE_RESULT", req_array);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
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
