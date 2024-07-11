package l1j.server.MJWebServer.Dispatcher.Template.Cash.API;

import com.google.gson.JsonObject;

import MJNCoinSystem.MJNCoinIdFactory;
import MJNCoinSystem.MJNCoinRefundInfo;
import MJNCoinSystem.MJNCoinSettings;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.POJO.AppCashInfo;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.POJO.AppCashWithdrawInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.Account;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_SystemMessage;

public class MJCashWithdrawInsertDatabase extends MJHttpResponse {
	private static String _action;
	private static String _message;

	public MJCashWithdrawInsertDatabase(MJHttpRequest request) {
		super(request);

		try {
			AppCashWithdrawInfo cashInfo = AppCashWithdrawInfo.chshInfoPaser(request.get_parameters(), _user);
			AppCashInfo info = AppCashInfo.loadDatabaseCashInfo(_player.getAccountName());
			if (cashInfo != null) {
				if (checkCashInfo(cashInfo, info))
					insertCashInfo(cashInfo, info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		JsonObject json = new JsonObject();
		json.addProperty(_action, _message);

		HttpResponse response = create_response(HttpResponseStatus.OK, json.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");

		return response;
	}

	private void insertCashInfo(AppCashWithdrawInfo info, AppCashInfo cashinfo) {
		try {
			/**
			 * ��ݽ�û ���� ������
			 */
			if (_player == null)
				return;

			if (cashinfo == null) {
				_player.sendPackets(new S_SystemMessage("���������� ��ϵǾ����� �ʽ��ϴ�."));
				return;
			}

			String current_date = MJString.get_current_datetime();
			MJNCoinRefundInfo rInfo = MJNCoinRefundInfo.newInstance().set_character_object_id(_player.getId())
					.set_character_name(_player.getName()).set_account_name(_player.getAccountName())
					.set_refund_name(cashinfo._user_name).set_bank_name(cashinfo._bank_number)
					.set_bank_account_number(cashinfo._bank_name).set_ncoin_value(Integer.valueOf(info._charge_count))
					.set_is_refund(false).set_generate_date(current_date);

			Account account = _player.getAccount();
			
			account.Ncoin_point -= rInfo.get_ncoin_value();
			account.updateNcoin();
			MJNCoinRefundInfo.do_store(rInfo.set_refund_id(MJNCoinIdFactory.REFUND.next_id()));
			String subject = String.format("[��� ��û] %s", _player.getName());
			int id = LetterTable.getInstance().writeLetter(949, rInfo.get_generate_date(), "��Ƽ��", _player.getName(), 0,
					subject, rInfo.toString());
			if (_player != null) {
				_player.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id,
						S_LetterList.TYPE_RECEIVE, "��Ƽ��", subject));
				_player.send_effect(1091);
				_player.sendPackets(428);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean checkCashInfo(AppCashWithdrawInfo info, AppCashInfo cashinfo) {
		boolean pass_ok = true;

		if (info._character_name == null || info._character_name.length() < 1) {
			pass_ok = false;
			_action = "error";
			_message = "�������� ������ �ƴմϴ�.";
		} else {

			if (_player == null) {
				pass_ok = false;
				_action = "error";
				_message = "�������� ������ �ƴմϴ�.";

			} else {
				if (_player.getAccount().validatePassword(_player.getAccountName(), info._account_pass)) {
					if (_player.getAccount().getCPW() == null) {
						if (Integer.valueOf(info._charge_count) < MJNCoinSettings.REFUND_GENERATE_MIN) {
							pass_ok = false;
							_action = "error";
							_message = "����� �ּ� " + MJNCoinSettings.REFUND_GENERATE_MIN + "������ �����մϴ�.";
						} else {
							if (_player.getAccount().Ncoin_point < Integer.valueOf(info._charge_count)) {
								pass_ok = false;
								_action = "error";
								_message = "���� N������ �����մϴ�.";
							} else {
								if (cashinfo != null) {
									_action = "login_ok";
									_message = "��ݽ�û�� �Ϸ� �Ǿ����ϴ�.";
								} else {
									pass_ok = false;
									_action = "bank_null";
									_message = "���������� ���� ������ּ���.";
								}
							}
						}
					} else {
						if (Integer.valueOf(info._charge_count) < MJNCoinSettings.REFUND_GENERATE_MIN) {
							pass_ok = false;
							_action = "error";
							_message = "����� �ּ� " + MJNCoinSettings.REFUND_GENERATE_MIN + "������ �����մϴ�.";
						} else {
							if (_player.getAccount().Ncoin_point < Integer.valueOf(info._charge_count)) {
								pass_ok = false;
								_action = "error";
								_message = "���� N������ �����մϴ�.";
							} else {
								if (cashinfo != null) {
									_action = "login_ok";
									_message = "��ݽ�û�� �Ϸ� �Ǿ����ϴ�.";
								} else {
									pass_ok = false;
									_action = "bank_null";
									_message = "���������� ���� ������ּ���.";
								}
							}
						}
						/*if (_player.getAccount().getCPW().equals(info._account_secend_pass)) {
							if (Integer.valueOf(info._charge_count) < MJNCoinSettings.REFUND_GENERATE_MIN) {
								pass_ok = false;
								_action = "error";
								_message = "����� �ּ� " + MJNCoinSettings.REFUND_GENERATE_MIN + "������ �����մϴ�.";
							} else {
								if (_player.getAccount().Ncoin_point < Integer.valueOf(info._charge_count)) {
									pass_ok = false;
									_action = "error";
									_message = "���� N������ �����մϴ�.";
								} else {
									if (cashinfo != null) {
										_action = "login_ok";
										_message = "��ݽ�û�� �Ϸ� �Ǿ����ϴ�.";
									} else {
										pass_ok = false;
										_action = "bank_null";
										_message = "���������� ���� ������ּ���.";
									}
								}
							}
						} else {
							pass_ok = false;
							_action = "error";
							_message = "2����� ������ �߸����ϴ�.";
						}*/
					}
				} else {
					pass_ok = false;
					_action = "error";
					_message = "���� ������ �߸����ϴ�.";
				}
			}
		}

		return pass_ok;
	}
}
