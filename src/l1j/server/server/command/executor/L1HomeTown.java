package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.Controller.HomeTownTimeController;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1HomeTown implements L1CommandExecutor {

    private L1HomeTown() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1HomeTown();
    }

    @override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer st = new StringTokenizer(arg);
            String para1 = st.nextToken(); // 獲取第一個參數
            if (para1.equalsIgnoreCase("每日處理")) {
                HomeTownTimeController.getInstance().dailyProc(); // 每日處理
            } else if (para1.equalsIgnoreCase("每月處理")) {
                HomeTownTimeController.getInstance().monthlyProc(); // 每月處理
            } else {
                throw new Exception(); // 如果參數無效，拋出異常
            }
        } catch (Exception e) {
                    // 捕捉異常並發送錯誤消息給使用者
            pc.sendPackets(new S_SystemMessage(".家鄉 [每日,每月] 請輸入。"));
        }
    }
}
