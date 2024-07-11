 package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1Action implements L1CommandExecutor {
     // 獲取 L1Action 的實例
     public static L1CommandExecutor getInstance() {
         return new L1Action();
     }

     public void execute(L1PcInstance pc, String cmdName, String arg) {
         try {
// 使用 StringTokenizer 解析參數
             StringTokenizer st = new StringTokenizer(arg);
// 取得動作 ID 並轉換為整數
             int actId = Integer.parseInt(st.nextToken(), 10);
// 向玩家發送動作封包
             pc.sendPackets((ServerBasePacket) new S_DoActionGFX(pc.getId(), actId));
         } catch (Exception exception) {
// 捕捉異常並向玩家發送系統訊息，提示正確的命令格式
             pc.sendPackets((ServerBasePacket) new S_SystemMessage(cmdName + " [actid] 請輸入正確格式。"));
         }
     }
 }


