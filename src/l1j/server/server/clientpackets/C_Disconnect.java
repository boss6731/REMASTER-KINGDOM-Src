 package l1j.server.server.clientpackets;

 import java.util.logging.Logger;
 import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class C_Disconnect
   extends ClientBasePacket {
   private static final String C_DISCONNECT = "[C] C_Disconnect";
   private static Logger _log = Logger.getLogger(C_Disconnect.class.getName());

     // C_Disconnect 類的構造函數，處理客戶端斷線操作
     public C_Disconnect(byte[] decrypt, GameClient client) {
         super(decrypt);
         client.setStatus(MJClientStatus.CLNT_STS_HANDSHAKE); // 設置客戶端狀態為握手

            // 獲取當前活動角色
         L1PcInstance pc = client.getActiveChar();
         System.out.println("C_DISCONNECT: 懷疑該連接的角色發送了非法封包，斷開連接，請重新連接！");

         if (pc != null) { // 如果角色不為空
             _log.fine("從角色斷開連接: " + pc.getName());
             pc.logout(); // 執行角色登出
             client.setActiveChar(null); // 將活動角色設置為空
         } else {
             _log.fine("來自帳號的斷開請求: " + client.getAccountName());
         }
     }


   public String getType() {
     return "[C] C_Disconnect";
   }
 }


