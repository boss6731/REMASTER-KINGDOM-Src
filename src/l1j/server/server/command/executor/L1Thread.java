package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Thread implements L1CommandExecutor {

    private L1Thread() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Thread();
    }

    @override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            Thread[] th = new Thread[Thread.activeCount()]; // 獲取當前活躍的所有線程
            Thread.enumerate(th); // 將所有活躍的線程複製到陣列中
            for (int i = 0; i < th.length; i++) {
                pc.sendPackets(new S_SystemMessage("[" + i + "] 使用中的线程 : [" + th[i] + "]"));
            }
            pc.sendPackets(new S_SystemMessage("目前使用中的线程数量 : [" + Thread.activeCount() + "]"));
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage(cmdName + "請輸入 數量")); // 請輸入正確的命令格式
        }
    }
}
