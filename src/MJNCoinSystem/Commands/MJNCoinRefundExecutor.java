package MJNCoinSystem.Commands;

import MJNCoinSystem.MJNCoinIdFactory;
import MJNCoinSystem.MJNCoinRefundInfo;
import MJNCoinSystem.MJNCoinSettings;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJNCoinRefundExecutor extends MJNCoinExecutor{

	@Override
	public void execute(MJCommandArgs args) {
		try{
			L1PcInstance pc = args.getOwner();
			if(pc == null)
				return;
			
			Account account = pc.getAccount();
			if(account == null)
				return;

			if (account.Ncoin_point < MJNCoinSettings.REFUND_GENERATE_MIN) {
				args.notify(String.format("持有的 N 幣未達到最低 %,d 元。", MJNCoinSettings.REFUND_GENERATE_MIN));
				return;
			}
			
			String current_date = MJString.get_current_datetime();
			MJNCoinRefundInfo rInfo = MJNCoinRefundInfo.newInstance()
					.set_character_object_id(pc.getId())
					.set_character_name(pc.getName())
					.set_account_name(pc.getAccountName())
					.set_refund_name(args.nextString())
					.set_bank_name(args.nextString())
					.set_bank_account_number(args.nextString())
					.set_ncoin_value(args.nextInt())
					.set_is_refund(false)	
					.set_generate_date(current_date);

			if (account.Ncoin_point < rInfo.get_ncoin_value()) {
				args.notify(String.format("N 幣不足，提款申請失敗。當前點數 : %,d", account.Ncoin_point));
				return;
			}

			account.Ncoin_point -= rInfo.get_ncoin_value();
			account.updateNcoin();
			MJNCoinRefundInfo.do_store(rInfo.set_refund_id(MJNCoinIdFactory.REFUND.next_id()));
			String subject = String.format("[提款申請] %s", pc.getName());
			do_write_letter_togm(rInfo.get_generate_date(), subject, rInfo.toString());
			args.notify("提款申請已完成。處理時間為 10 分鐘至 1 小時。");
		} catch(Exception e) {
			args.notify(".提款 [姓名] [銀行] [帳號] [金額]");
		}
	}

	@override
	public String get_command_name() {
		return "提款";
	}
}
