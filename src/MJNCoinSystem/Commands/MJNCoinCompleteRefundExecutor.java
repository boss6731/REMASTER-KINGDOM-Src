package MJNCoinSystem.Commands;

import MJNCoinSystem.MJNCoinRefundInfo;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJNCoinCompleteRefundExecutor extends MJNCoinExecutor {

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
			MJNCoinRefundInfo rInfo = MJNCoinRefundInfo.from_refund_id(refund_id);
			if (rInfo == null) {
				args.notify(String.format("找不到提現信息。提現ID: %d", refund_id));
				return;
			}
			if (rInfo.get_is_refund()) {
				args.notify(String.format("該提現信息已完成。提現ID: %d", refund_id));
				return;
			}
			String current_date = MJString.get_current_datetime();
			MJNCoinRefundInfo.update_is_refund(refund_id, true, current_date);
			String content = "提現已完成。請確認。";
			String subject = "[提現完成]";
			do_write_letter(rInfo.get_character_name(), current_date, subject, content);
			args.notify(String.format("提現狀態::已檢查。提現ID: %d", refund_id));
		} catch (Exception e) {
			args.notify(".提現狀態 [提現ID]");
		}
	}

	@override
	public String get_command_name() {
		return "提現狀態";
	}
}
