package l1j.server.MJWebServer.Dispatcher.Template.Cash.API;

import java.sql.Connection;

import com.google.gson.JsonObject;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.POJO.AppCashInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.SQLUtil;

public class MJCashInsertDatabase extends MJHttpResponse {
	private static String _action;
	private static String _message;

	public MJCashInsertDatabase(MJHttpRequest request) {
		super(request);

		try {
			//System.out.println(request.get_parameters());
			AppCashInfo cashInfo = AppCashInfo.chshInfoPaser(request.get_parameters(), _user);
			if(cashInfo != null) {
				if(checkCashInfo(cashInfo))
					insertCashInfo(cashInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		JsonObject json = new JsonObject();
		json.addProperty(_action, _message);
		if(!_action.equalsIgnoreCase("error"))
			json.addProperty("error", "");	
		
		HttpResponse response = create_response(HttpResponseStatus.OK, json.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");

		return response;
	}
	
	/*private void insertCashInfo(AppCashInfo info) {
		Connection con = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sql = "INSERT INTO cash_info (account_name,character_name,user_name,bank_number,bank_name,account_pass, account_secend_pass) VALUES (?, ?, ?, ?, ?, ?, ?) "
					+ "ON DUPLICATE KEY UPDATE account_name=?,character_name=?,user_name=?,bank_number=?,bank_name=?,account_pass=?,account_secend_pass=?";
			SQLUtil.execute(con, sql,
					new Object[] { _player.getAccount().getName(), info._character_name, info._user_name, info._bank_number, info._bank_name,
							info._account_pass, info._account_secend_pass, 
							_player.getAccount().getName(), info._character_name, info._user_name,
							info._bank_number, info._bank_name, info._account_pass, info._account_secend_pass });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
	}*/
	
	private void insertCashInfo(AppCashInfo info) {
		Connection con = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sql = "INSERT INTO cash_info (account_name,character_name,user_name,bank_number,bank_name,account_pass) VALUES (?, ?, ?, ?, ?, ?) "
					+ "ON DUPLICATE KEY UPDATE account_name=?,character_name=?,user_name=?,bank_number=?,bank_name=?,account_pass=?";
			SQLUtil.execute(con, sql,
					new Object[] { _player.getAccount().getName(), info._character_name, info._user_name, info._bank_number, info._bank_name,
							info._account_pass,
							_player.getAccount().getName(), info._character_name, info._user_name,
							info._bank_number, info._bank_name, info._account_pass});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
	}

	private boolean checkCashInfo(AppCashInfo info) {
		boolean pass_ok = true;

		if (info._character_name == null || info._character_name.length() < 1) {
			pass_ok = false;
			_action = "error";
			_message = "這不是正常的訪問。";
		} else {
			if (_player == null) {
				pass_ok = false;
				_action = "error";
				_message = "這不是正常的訪問。";
			} else {
				if (_player.getAccount().validatePassword(_player.getAccountName(), info._account_pass)) {
					if (_player.getAccount().getCPW() != null) {
						if (_player.getAccount().getCPW().equals(info._account_secend_pass)) {
							_action = "login_ok";
							_message = "輸入已完成。";
						} else {
							pass_ok = false;
							_action = "error";
							_message = "二次密碼信息不正確。";
						}
					} else {
						_action = "login_ok";
						_message = "輸入已完成。";
					}
				} else {
					pass_ok = false;
					_action = "error";
					_message = "賬號信息不正確。";
				}
			}
		}

		return pass_ok;
	}


