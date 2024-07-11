package l1j.server.MJWebServer.Dispatcher.Template.Cash.API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.google.gson.JsonObject;

import MJNCoinSystem.MJNCoinDepositInfo;
import MJNCoinSystem.MJNCoinIdFactory;
import MJNCoinSystem.MJNCoinSettings;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.POJO.AppCashChargeInfo;
import l1j.server.MJWebServer.Dispatcher.Template.Cash.POJO.AppCashInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.MWautoBankerProvider.MWautoBankerDataTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.utils.SQLUtil;

public class MJCashChargeInsertDatabase extends MJHttpResponse {
	private static String _action;
	private static String _message;

	public MJCashChargeInsertDatabase(MJHttpRequest request) {
		super(request);
		try {
			AppCashChargeInfo cashInfo = AppCashChargeInfo.chshInfoPaser(request.get_parameters(), _user);
			AppCashInfo info = AppCashInfo.loadDatabaseCashInfo(_player.getAccountName());
			if (cashInfo != null) {
				if (checkCashInfo(cashInfo, info))
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

		HttpResponse response = create_response(HttpResponseStatus.OK, json.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");

		return response;
	}

	private void insertCashInfo(AppCashChargeInfo info) {
		try {
            /**
             * 發送充值申請信件
             */
			if (_player == null)
				return;

			String current_date = MJString.get_current_datetime();
			MWautoBankerDataTable banker = new MWautoBankerDataTable();
			MJNCoinDepositInfo dInfo = MJNCoinDepositInfo.newInstance()
					.set_deposit_id(MJNCoinIdFactory.DEPOSIT.next_id()).set_character_object_id(_player.getId())
					.set_character_name(_player.getName()).set_account_name(_player.getAccountName())
					.set_deposit_name(info._account_name).set_ncoin_value(Integer.valueOf(info._charge_count))
					.set_generate_date(current_date).set_is_deposit(0);

			MJNCoinDepositInfo.do_store(dInfo);
			do_write_letter_command(_player, MJNCoinSettings.DEPOSIT_LETTER_ID);
			/**自動充值相關*/
			banker.StoreDepositorInfo(_player, info._account_name, "等待入金中", Integer.valueOf(info._charge_count));
			String subject = String.format("[充值申請] %s", _player.getName());
			do_write_letter_togm(current_date, subject, dInfo.toString());
			banker = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private boolean checkCashInfo(AppCashChargeInfo info, AppCashInfo cashinfo) {
        boolean pass_ok = true;
        MWautoBankerDataTable banker = new MWautoBankerDataTable();

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
                //                if (_player.getAccount().validatePassword(_player.getAccountName(), info._account_pass)) {
                if (_player.getAccount().getCPW() == null) {
                    if (Integer.valueOf(info._charge_count) < MJNCoinSettings.CHARGE_GENERATEMIN) {
                        pass_ok = false;
                        _action = "error";
                        _message = "充值金額至少需要 " + MJNCoinSettings.CHARGE_GENERATEMIN + " 元。";
                    } else {
                        //TODO 0:等待 1:完成 2:取消 允許非0的情況申請充值
                        if (getDepositInfo() == 0) {
                            pass_ok = false;
                            _action = "error";
                            _message = "已存在申請中的記錄。";
                        } else if(!banker.Depositorkeyworld(_player, info._account_name)) {
                            pass_ok = false;
                            _action = "error";
                            _message = "使用該關鍵詞的交易已在進行中，請使用其他關鍵詞。";
                        } else {
                            _action = "login_ok";
                            _message = "充值申請已完成。";
                        }
                    }
                }
                return pass_ok;
            }
        } else {
            if (Integer.valueOf(info._charge_count) < MJNCoinSettings.CHARGE_GENERATEMIN) {
                pass_ok = false;
                _action = "error";
                _message = "充值金額至少需要 " + MJNCoinSettings.CHARGE_GENERATEMIN + " 元。";
            } else {
                    // TODO 0:等待 1:完成 2:取消 允許非0的情況申請充值
                if (getDepositInfo() == 0) {
                    pass_ok = false;
                    _action = "error";
                    _message = "已存在申請中的記錄。";
                } else {
                    _action = "login_ok";
                    _message = "充值申請已完成。";
                }
            }
						/*/*if (_player.getAccount().getCPW().equals(info._account_secend_pass)) {
if (Integer.valueOf(info._charge_count) < MJNCoinSettings.CHARGE_GENERATEMIN) {
pass_ok = false;
_action = "error";
_message = "充值金額至少需要 " + MJNCoinSettings.CHARGE_GENERATEMIN + " 元。";
} else {
// TODO 0:等待 1:完成 2:取消 允許非0的情況申請充值
if (getDepositInfo() == 0) {
pass_ok = false;
_action = "error";
_message = "已存在申請中的記錄。";
} else if(!banker.Depositorkeyworld(_player, info._account_name)) {
pass_ok = false;
_action = "error";
_message = "使用該關鍵詞的交易已在進行中，請使用其他關鍵詞。";
} else {
_action = "login_ok";
_message = "充值申請已完成。";
}
}
} else {
pass_ok = false;
_action = "error";
_message = "二次密碼信息不正確。";
}*/
        }
/*} else {
pass_ok = false;
_action = "error";
_message = "賬號信息不正確。";
}*/*/
			}
		}

		return pass_ok;
                }

public String toString(AppCashChargeInfo info, L1PcInstance pc) {
        StringBuilder sb = new StringBuilder();
        sb.append("入金 ID : ").append(MJNCoinIdFactory.DEPOSIT.next_id()).append("\n");
        sb.append("帳號名稱 : ").append(_player.getAccountName()).append("\n");
        sb.append("角色名稱 : ").append(_player.getName()).append("\n");
        sb.append("入金者名稱 : ").append(info._account_name).append("\n");
        sb.append("N 幣數量 : ").append(info._charge_count).append("\n");
        String current_date = MJString.get_current_datetime();
        sb.append("登錄日期 : ").append(current_date);
        return sb.toString();
        }

	private int getDepositInfo() {
		int check = -1;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ncoin_trade_deposit WHERE account_name=?");
			pstm.setString(1, _player.getAccount().getName());
			rs = pstm.executeQuery();
			while (rs.next()) {
				check = rs.getInt("is_deposit");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}

		return check;
	}

	private void do_write_letter_command(L1PcInstance pc, final int notify_id) {
		final MJObjectWrapper<String> subject = new MJObjectWrapper<String>();
		final MJObjectWrapper<String> content = new MJObjectWrapper<String>();
		Selector.exec("select * from letter_command where id=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, notify_id);
			}

			@Override
            public void result(ResultSet rs) throws Exception {
        if (rs.next()) {
        subject.value = rs.getString("subject");
        content.value = rs.getString("content");
        } else {
        subject.value = MJString.EmptyString;
        content.value = MJString.EmptyString;
        }
        }
        });
        if (MJString.isNullOrEmpty(subject.value) && MJString.isNullOrEmpty(content.value)) {
        try {
        throw new Exception(String.format("找不到命令信件。ID : %d
        堆疊追蹤", notify_id));
        } catch (Exception e) {
        e.printStackTrace();
        }
        return;
        }

        String current_date = MJString.get_current_datetime();
        do_write_letter(pc.getName(), current_date, subject.value, content.value);
        }

private void do_write_letter_togm(String generate_date, String subject, String content) {
        do_write_letter("梅蒂斯", generate_date, subject, content);
        }

private void do_write_letter(String receiver, String generate_date, String subject, String content) {
        int id = LetterTable.getInstance().writeLetter(949, generate_date, "梅蒂斯", receiver, 0, subject, content);
        L1PcInstance pc = L1World.getInstance().getPlayer(receiver);
        if (pc != null) {
        pc.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_RECEIVE, "梅蒂斯", subject));
        // pc.sendPackets(new S_LetterList(pc, 0, 20));
        pc.send_effect(1091);
        pc.sendPackets(428);
        }
        }
        }
