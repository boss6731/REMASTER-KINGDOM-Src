 package l1j.server.server.clientpackets;

 import java.util.logging.Logger;
 import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
 import l1j.server.server.Controller.LoginController;
 import l1j.server.server.GameClient;

 public class C_ReturnToLogin
   extends ClientBasePacket
 {
   private static final String C_RETURN_TO_LOGIN = "[C] C_ReturnToLogin";
   private static Logger _log = Logger.getLogger(C_ReturnToLogin.class.getName());

     public C_ReturnToLogin(byte[] decrypt, GameClient client) throws Exception {
// 調用父類的構造函數
         super(decrypt);

         try {
// 獲取客戶端的賬戶名稱
             String account = client.getAccountName();
// 使用 StringBuilder 構建賬戶名稱的日誌信息
             StringBuilder sb = new StringBuilder();
             _log.finest(sb.append(" 帳號 : ").append(account).toString());
             sb = null;

// 檢查客戶端是否有活動角色且狀態不等於更改名稱狀態
             if (client.getActiveChar() != null && client.getStatus().toInt() != MJClientStatus.CLNT_STS_CHANGENAME.toInt()) {
// 如果條件成立，踢出客戶端並關閉連接
                 client.kick();
                 client.close();
// 輸出懷疑是錯誤的日誌信息
                 System.out.println("─────────────────────────────────");
                 System.out.println("疑似錯誤 -- 帳號名 " + client.getAccountName());
                 System.out.println("─────────────────────────────────");

                 return;
             }

// 執行客戶端登出操作
             LoginController.getInstance().logout(client);
         } catch (Exception e) {
// 捕獲並打印異常信息
             e.printStackTrace();
         } finally {
// 清理資源
             clear();
         }
     }


   public String getType() {
     return "[C] C_ReturnToLogin";
   }
 }


