package l1j.server.MJWebServer.Dispatcher.Create;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;

import MJFX.UIAdapter.MJUIAdapter;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetSafeSystem.Distribution.ConnectedDistributor;
import l1j.server.MJNetSafeSystem.DriveSafe.MJAccountDriveInfo;
import l1j.server.MJNetSafeSystem.DriveSafe.MJHddIdChecker;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Login.MJHttpLoginManager;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.Account;
import l1j.server.server.utils.SQLUtil;

public class MJHttpAccountCreateResponse extends MJHttpResponse {
	public MJHttpAccountCreateResponse(MJHttpRequest request) {
		super(request, false); // application check falsed..
	}

	private static final Object _lock = new Object();
	private static Logger _log = Logger.getLogger(ConnectedDistributor.class.getName());
	private static int check = 0;
	private static int check_window = 0;

	// 여기 로직은 건들지 말고,
	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		InetSocketAddress inetAddr = (InetSocketAddress) m_request.get_remote_address();
		String address = inetAddr.getAddress().getHostAddress();
		int port = inetAddr.getPort();

		Map<String, List<String>> m = m_request.get_parameters();
		String accountName = MJString.EmptyString;
		String password = MJString.EmptyString;
		String phone = MJString.EmptyString;
		String hdd_id = MJString.EmptyString;
		String mac_address = MJString.EmptyString;
		String nic_id = MJString.EmptyString;
		String request_mac = MJString.EmptyString;

		for (String key : m.keySet()) {
			List<String> list = m.get(key);
			switch (key) {
			case "account":
				accountName = list.get(0);
				break;

			case "password":
				password = list.get(0);
				break;
				
			case "phone":
				phone = list.get(0);
				break;

			case "hdd_id":
				hdd_id = list.get(0);
				break;

			case "mac_address":
				mac_address = list.get(0);
				break;

			case "nic_id":
				nic_id = list.get(0);
				break;

			case "asdsadasd":
				request_mac = list.get(0);
				break;
			}
		}

		if (MJString.isNullOrEmpty(request_mac)) {
			System.out.println(String.format("%s:%d 사용자가 웹에서 계정생성 시도를 했습니다.", address, port));
			HttpResponse response = create_response(HttpResponseStatus.OK, "ㅋㅋㅋㅋㅋㅋㅋㅋㅋ");
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
			return response;
		}

		MJHttpCreateResult result = new MJHttpCreateResult();

		if (check >= 5) {
			result.result_code = MJEHttpCreateValidation.CLOSE.get_message();
			Gson gson = new Gson();
			String json = gson.toJson(result);
			HttpResponse response = create_response(HttpResponseStatus.OK, json);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
			check = 0;
			return response;
		}
		
		if (check_window >= 5) {
			result.result_code = MJEHttpCreateValidation.CLOSE_WINDOW.get_message();
			Gson gson = new Gson();
			String json = gson.toJson(result);
			HttpResponse response = create_response(HttpResponseStatus.OK, json);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
			check_window = 0;
			return response;
		}

		if (MJString.isNullOrEmpty(accountName) || MJString.isNullOrEmpty(password) || MJString.isNullOrEmpty(hdd_id)) {

			result.result_code = MJEHttpCreateValidation.FAIL_NOT_FOUND_PARAMETERS.get_message();
		} else {
			String hmac = MJHttpLoginManager.check_hmac(hdd_id, mac_address, "/outgame/create");
			if (!hmac.equalsIgnoreCase(request_mac)) {
				System.out.println(String.format("%s:%d 사용자 로그인 실패 invalid mac value\r\nrequest : %s,\r\nmake mac : %s",
						address, port, request_mac, hmac));
				result.result_code = MJEHttpCreateValidation.FAIL_HMAC.get_message();
				check++;
			} else {
				if (Config.Login.UseExConnect) {
					int reason = MJHddIdChecker.get_denials(hdd_id);
					int accountHddCount = getAccountHddCount(hdd_id);
					
					if (reason != MJHddIdChecker.DENIALS_TYPE_NONE
							&& reason != MJHddIdChecker.DENIALS_TYPE_INVALID_HDD_ID) {
						result.result_code = MJEHttpCreateValidation.FAIL_HDD_BAN.get_message();
						System.out.println(String.format("하드밴 당한 클라이언트 접속 차단 : %s, %s, %d", accountName, hdd_id, reason));

						Gson gson = new Gson();
						String json = gson.toJson(result);
						HttpResponse response = create_response(HttpResponseStatus.OK, json);
						response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
						check++;
						return response;
					}
					
					if (accountHddCount >= MJNetServerLoadManager.DESKTOP_ADDRESS2ACCOUNT) {
						result.result_code = MJEHttpCreateValidation.FAIL_2CHECK_IP.get_message();
						System.out.println(String.format("◆[하드] 계정생성 초과 : Account : %s, Hdd : %s, Ip : %s 최대: %d개", accountName, hdd_id, address, MJNetServerLoadManager.DESKTOP_ADDRESS2ACCOUNT));

						Gson gson = new Gson();
						String json = gson.toJson(result);
						HttpResponse response = create_response(HttpResponseStatus.OK, json);
						response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
						check++;
						return response;
					}
					
					MJAccountDriveInfo.update_account_to_drive(accountName, hdd_id);
				}

				String ip = address;
				Account account = null;

				synchronized (_lock) {
					account = Account.load(accountName);
					if (account == null) {
						if (Config.Synchronization.AutoCreateAccounts) {
							int accountIpCount = getAccountIpCount(ip);						
							int accountHddCount = getAccountHddCount(hdd_id);
							
							if (accountIpCount >= MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT) {
								processAccountOver(accountName, result);

								Gson gson = new Gson();
								String json = gson.toJson(result);
								HttpResponse response = create_response(HttpResponseStatus.OK, json);
								response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
								return response;
							} else if (accountHddCount > MJNetServerLoadManager.DESKTOP_ADDRESS2ACCOUNT) {
								processAccountOver(accountName, result);

								Gson gson = new Gson();
								String json = gson.toJson(result);
								HttpResponse response = create_response(HttpResponseStatus.OK, json);
								response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
								return response;
							} else {
								account = Account.create(accountName, password, ip, ip, phone);
								account = Account.load(accountName);
								MJUIAdapter.on_create_account(accountName, ip);
								result.result_code = MJEHttpCreateValidation.SUCCESS.get_message();

								Gson gson = new Gson();
								String json = gson.toJson(result);
								HttpResponse response = create_response(HttpResponseStatus.OK, json);
								response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
							}
						} else {
							String s = String.format("account missing for user %s", accountName);
							simplyLog(s);
							check++;
						}

						if (account == null) {
							result.result_code = MJEHttpCreateValidation.FAIL_DUPLICATE_ACCOUNT.get_message();

							Gson gson = new Gson();
							String json = gson.toJson(result);
							HttpResponse response = create_response(HttpResponseStatus.OK, json);
							response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
							check++;
							return response;
						}
					} else {// 이미 존재하는 계정
						result.result_code = MJEHttpCreateValidation.FAIL_DUPLICATE_ACCOUNT.get_message(); // 실패.. 코드
						
						Gson gson = new Gson();
						String json = gson.toJson(result);
						HttpResponse response = create_response(HttpResponseStatus.OK, json);
						response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
						check++;
						return response;
					}

					if (!account.validatePassword(accountName, password)) {
						result.result_code = MJEHttpCreateValidation.FAIL_DUPLICATE_ACCOUNT.get_message();

						Gson gson = new Gson();
						String json = gson.toJson(result);
						HttpResponse response = create_response(HttpResponseStatus.OK, json);
						response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
						check++;
						return response;
					}
				}

				// 계성 생성 조건들 만드세요.
				/*
				 * if(true) { result.result_code =
				 * MJEHttpCreateValidation.SUCCESS.get_message(); // 성공 }else {
				 * result.result_code =
				 * MJEHttpCreateValidation.FAIL_DUPLICATE_ACCOUNT.get_message(); // 실패.. 코드 필요하면
				 * ㄴ에넘 코드 추가 }
				 */
			}
		}

		Gson gson = new Gson();
		String json = gson.toJson(result);
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");

		return response;
	}

	private void processAccountOver(String accountName, MJHttpCreateResult result) {
		result.result_code = MJEHttpCreateValidation.FAIL_2CHECK_IP.get_message();
		check_window++;
	}

	private int getAccountIpCount(String ip) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(ip) as cnt FROM accounts WHERE ip=? ");
			pstm.setString(1, ip);
			rs = pstm.executeQuery();

			if (rs.next())
				return rs.getInt("cnt");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return 0;
	}
	
	private int getAccountHddCount(String hdd) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(hdd_id) as cnt FROM netsafe_account_to_drive WHERE hdd_id=? ");
			pstm.setString(1, hdd);
			rs = pstm.executeQuery();

			if (rs.next())
				return rs.getInt("cnt");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return 0;
	}

	private void simplyLog(String log) {
		_log.info(log);
		System.out.println("MJHttpAccountCreateResponse:" +log);
	}

}