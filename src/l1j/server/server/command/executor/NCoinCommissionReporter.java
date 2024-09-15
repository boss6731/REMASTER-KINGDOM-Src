package l1j.server.server.server.command.executor;

import java.sql.ResultSet;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.model.Instance.L1PcInstance;

public class NCoinCommissionReporter implements L1CommandExecutor {
	@SuppressWarnings("unused")

	private NCoinCommissionReporter() {
	}

	public static L1CommandExecutor getInstance() {
		return new NCoinCommissionReporter();
	}

	public void execute(L1PcInstance pc, String cmdName, String arg) {
		if (pc == null || !pc.isGm())
			return;

		try {
			Selector.exec("select sum(commission_ncoin) as commission_report FROM `board_item_trade`",
					new FullSelectorHandler() {
						@Override
						public void result(ResultSet rs) throws Exception {
							if (rs.next()) {
								pc.sendPackets(String.format("\fH目前手續費預計總收益為 : \\aG%,d金幣\fH。", rs.getInt("commission_report")));
							}
						}
					});
			Selector.exec(
					"select sum(commission_ncoin) as commission_report FROM `board_item_trade` where state='완료'",
					new FullSelectorHandler() {
						@Override
						public void result(ResultSet rs) throws Exception {
							if (rs.next()) {
								pc.sendPackets(
										String.format("\fH目前手續費完成總收益為 : \\aG%,d金幣\fH。", rs.getInt("commission_report")));
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
