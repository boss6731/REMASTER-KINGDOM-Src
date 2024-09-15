package MJNCoinSystem.Commands;

import java.sql.ResultSet;

import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJNCoinAdenaCommissionReporter extends MJNCoinExecutor {

	@Override
	public void execute(MJCommandArgs args) {
		L1PcInstance pc = args.getOwner();
		if (pc == null || !pc.isGm())
			return;

		try {
			Selector.exec("select sum(commission_ncoin) as commission_report FROM `ncoin_trade_adena`", new FullSelectorHandler() {
				@override
				public void result(ResultSet rs) throws Exception {
					if (rs.next()) {
						args.notify(String.format("目前預估的總手續費收入為: %,d元。", rs.getInt("commission_report")));
					}
				}
			});
			Selector.exec("select sum(commission_ncoin) as commission_report FROM `ncoin_trade_adena` where complete_date is not null", new FullSelectorHandler() {
				@override
				public void result(ResultSet rs) throws Exception {
					if (rs.next()) {
						args.notify(String.format("目前完成的總手續費收入為: %,d元。", rs.getInt("commission_report")));
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@override
	public String get_command_name() {
		return "手續費確認";
	}

}


