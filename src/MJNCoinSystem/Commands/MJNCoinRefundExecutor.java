package MJNCoinSystem.Commands;

import MJNCoinSystem.MJNCoinIdFactory;
import MJNCoinSystem.MJNCoinRefundInfo;
import MJNCoinSystem.MJNCoinSettings;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJNCoinRefundExecutor extends MJNCoinExecutor {

	//@override
	public void execute(MJCommandArgs args) {
		try {
			L1PcInstance pc = args.getOwner();
			if (pc == null)
				return; // 如果角色為空，則返回

			Account account = pc.getAccount();
			if (account == null)
				return; // 如果帳戶為空，則返回

			// 檢查帳戶中的 Ncoin 點數是否達到最低申請退款的要求
			if (account.Ncoin_point < MJNCoinSettings.REFUND_GENERATE_MIN) {
				args.notify(String.format("持有的 Ncoin 不足最低 %,d 點。", MJNCoinSettings.REFUND_GENERATE_MIN));
				return;
			}

			String current_date = MJString.get_current_datetime();
			MJNCoinRefundInfo rInfo = MJNCoinRefundInfo.newInstance()
					.set_character_object_id(pc.getId())
					.set_character_name(pc.getName())
					.set_account_name(pc.getAccountName())
					.set_refund_name(args.nextString()) // 設定退款人姓名
					.set_bank_name(args.nextString()) // 設定銀行名稱
					.set_bank_account_number(args.nextString()) // 設定銀行帳號
					.set_ncoin_value(args.nextInt()) // 設定退款的 Ncoin 點數
					.set_is_refund(false)
					.set_generate_date(current_date);

				// 檢查帳戶中的 Ncoin 點數是否足夠進行退款
			if (account.Ncoin_point < rInfo.get_ncoin_value()) {
				args.notify(String.format("Ncoin 不足，申請退款失敗。當前點數 : %,d", account.Ncoin_point));
				return;
			}

			// 扣除帳戶中的 Ncoin 點數
			account.Ncoin_point -= rInfo.get_ncoin_value();
			account.updateNcoin();
			MJNCoinRefundInfo.do_store(rInfo.set_refund_id(MJNCoinIdFactory.REFUND.next_id()));

			String subject = String.format("[退款申請] %s", pc.getName());
			do_write_letter_togm(rInfo.get_generate_date(), subject, rInfo.toString());

			args.notify("退款申請已完成，處理時間約為 10 分鐘至 1 小時內。");
		} catch (Exception e) {
			// 捕捉異常並通知用戶正確的命令格式
			args.notify(".退款 [姓名] [銀行] [帳號] [金額]");
		}
	}

	//@override
	public String get_command_name() {
		return "退款";
	}
}
