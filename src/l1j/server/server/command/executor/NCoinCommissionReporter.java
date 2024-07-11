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
        // ��?��ʫ������������?GM
        if (pc == null || !pc.isGm()) {
            return;
        }

        try {
            // ?����Ѣ?������
            Selector.exec("select sum(commission_ncoin) as commission_report FROM `board_item_trade`",
                    new FullSelectorHandler() {
                        @override
                        public void result(ResultSet rs) throws Exception {
                            if (rs.next()) {
                                pc.sendPackets(String.format("\fH������Ѣ��?�������� : \aG%,d�\fH��", rs.getInt("commission_report")));
                            }
                        }
                    });

            // ?��������?������
            Selector.exec(
                    "select sum(commission_ncoin) as commission_report FROM `board_item_trade` where state='����'",
                    new FullSelectorHandler() {
                        @override
                        public void result(ResultSet rs) throws Exception {
                            if (rs.next()) {
                                pc.sendPackets(String.format("\fH����������?�������� : \aG%,d�\fH��", rs.getInt("commission_report")));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
