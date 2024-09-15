package MJNCoinSystem.Commands;

import MJNCoinSystem.MJNCoinDepositInfo;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.GMCommands;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJCoinCompleteDepositExecutor extends MJNCoinExecutor {
	public static final int ALREADY = 0;
	public static final int COMPLETE = 1;
	public static final int CANCEL = 2;

	@Override
	public void execute(MJCommandArgs args) {
		try {
			L1PcInstance pc = args.getOwner();
			if (pc == null)
				return;

			Account account = pc.getAccount();
			if (account == null)
				return;

			if (!pc.isGm())
				return;

			int refund_id = args.nextInt();
			String type = args.nextString();
			MJNCoinDepositInfo rInfo = MJNCoinDepositInfo.from_deposit_id(refund_id);
			if (rInfo == null) {
				args.notify(String.format("找不到提款信息。提款ID: %d", refund_id));
				return;
			}

			if (rInfo.is_deposit() == 1) {
				args.notify(String.format("該信息已經完成提款。提款ID: %d", refund_id));
				return;
			}
			// 0 等待
			// 2 取消
			// 1 完成
			int type_int = type.equalsIgnoreCase("等待") ? ALREADY : type.equalsIgnoreCase("取消") ? CANCEL : COMPLETE;

			if (type_int != ALREADY && rInfo.is_deposit() == 2) {
				args.notify(String.format("該信息已經取消提款。提款ID: %d", refund_id));
				return;
			}

			String current_date = MJString.get_current_datetime();
			MJNCoinDepositInfo.update_is_deposit(refund_id, type_int, current_date);
			String content = type_int == CANCEL ? "充值申請已取消。" : type_int == ALREADY ? "已轉為充值等待狀態。" : "充值已完成。請確認\\n\\。";
			String subject = type_int == CANCEL ? "[充值取消]" : type_int == ALREADY ? "[充值等待]" : "[充值完成]";

			if (type_int == COMPLETE) {
				GMCommands.increaseNcoin(args.getOwner(), rInfo.get_character_name(), rInfo.get_ncoin_value());
			}

			do_write_letter(rInfo.get_character_name(), current_date, subject, content);
			args.notify(String.format("充值狀態檢查。提款ID: %d", refund_id));
		} catch (Exception e) {
			args.notify(".充值狀態 [充值ID] [等待/完成/取消]");
		}

		@override
		public String get_command_name () {
			return "充值狀態";
		}
	}
}