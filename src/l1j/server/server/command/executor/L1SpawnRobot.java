package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1SpawnRobot implements L1CommandExecutor {

    // 私有構造函數，防止直接實例化
    private L1SpawnRobot() {}

    // 獲取實例的方法
    public static L1CommandExecutor getInstance() {
        return new L1SpawnRobot();
    }

    // 覆蓋 execute 方法
    @override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
// 發送一條系統消息給玩家，內容為 "비어 있음" (空白)
            pc.sendPackets(new S_SystemMessage("空白"));
        } catch (Exception e) {
// 捕獲異常，發送一條系統消息說明命令格式
            pc.sendPackets(new S_SystemMessage(cmdName + " [類型 : 0.靜止, 1.移動]"));
        }
    }
}