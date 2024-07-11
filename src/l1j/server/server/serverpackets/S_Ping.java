 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;




 public class S_Ping extends ServerBasePacket {
     private static final int SC_PING_REQ = 1000; // Ping 請求常量
     private static S_Ping _pck; // 用於存儲 Ping 封包實例
     public static long _lastMs; // 上一次 Ping 請求的時間
     public static boolean _isRun; // 指示 Ping 檢查是否運行
     private static final S_Ping[] _pings = new S_Ping[] { new S_Ping(1), new S_Ping(2), new S_Ping(3), new S_Ping(4) }; // 預定義的 Ping 封包實例

     // 根據交易 ID 獲取對應的 S_Ping 實例
     public static S_Ping get(int transaction) {
         return _pings[transaction - 1];
     }

     // 為 GM 獲取 S_Ping 實例
     public static S_Ping getForGM() {
         if (_pck == null) {
             _pck = new S_Ping();
             _pck.writeC(8); // 寫入數據 8
             _pck.writeC(1); // 寫入數據 1
             _pck.writeH(0); // 寫入數據 0
         }
         _lastMs = System.currentTimeMillis(); // 記錄當前時間
         return _pck;
     }

     // 為 GM 處理 Ping 檢查請求
     public static void reqForGM(L1PcInstance pc) {
         if (!_isRun) {
             return; // 如果未運行，直接返回
         }
         long del = System.currentTimeMillis() - _lastMs; // 計算時間差
         String s = String.format("[Ping 檢查] %dms.", del); // 格式化消息
         System.out.println(s); // 輸出到控制台
         pc.sendPackets(new S_SystemMessage(s)); // 發送消息給玩家

         // 計劃下一次 Ping 請求
         GeneralThreadPool.getInstance().schedule(new request(pc), 1000L - del);
     }
 }

   static class request
     implements Runnable {
     request(L1PcInstance pc) {
       this._pc = pc;
     }
     private L1PcInstance _pc;
     public void run() {
       this._pc.sendPackets(S_Ping.getForGM(), false);
     }
   }

   private S_Ping() {
     writeC(19);
     writeH(1000);
   }

   private S_Ping(int i) {
     writeC(19);
     writeH(1000);
     writeC(8);
     writeC(i);
     writeH(0);
   }


   public byte[] getContent() throws IOException {
     return getBytes();
   }
 }


