package l1j.server.server.command.executor;

import java.sql.ResultSet;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;

public class NCoinCommissionReporter implements L1CommandExecutor {
    @suppresswarnings("unused")

    private NCoinCommissionReporter() {
    }

    public static L1CommandExecutor getInstance() {
        return new NCoinCommissionReporter();
    }

    public void execute(L1PcInstance pc, String cmdName, String arg) {
        // Ëş?èÌÊ«ãÀÜúğíî¤ó¦ãÀÜú?GM
        if (pc == null || !pc.isGm()) {
            return;
        }

        try {
            // ?âóçèÑ¢?ÑÑõÅûú
            Selector.exec("select sum(commission_ncoin) as commission_report FROM `board_item_trade`",
                    new FullSelectorHandler() {
                        @override
                        public void result(ResultSet rs) throws Exception {
                            if (rs.next()) {
                                pc.sendPackets(String.format("\fHÙÍîñçèÑ¢îÜ?ÑÑõÅûúãÀ : \aG%,dêª\fH¡£", rs.getInt("commission_report")));
                            }
                        }
                    });

            // ?âóèÇà÷îÜ?ÑÑõÅûú
            Selector.exec(
                    "select sum(commission_ncoin) as commission_report FROM `board_item_trade` where state='èÇà÷'",
                    new FullSelectorHandler() {
                        @override
                        public void result(ResultSet rs) throws Exception {
                            if (rs.next()) {
                                pc.sendPackets(String.format("\fHÙÍîñèÇà÷îÜ?ÑÑõÅûúãÀ : \aG%,dêª\fH¡£", rs.getInt("commission_report")));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
