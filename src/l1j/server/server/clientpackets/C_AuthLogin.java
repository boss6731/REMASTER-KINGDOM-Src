 package l1j.server.server.clientpackets;

 import java.io.IOException;
 import java.util.logging.Logger;
 import l1j.server.server.GameClient;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.IpTable;
 import l1j.server.server.serverpackets.S_LoginResult;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.DelayClose;

 public class C_AuthLogin extends ClientBasePacket {
   private static final String C_AUTH_LOGIN = "[C] C_AuthLogin";
   private static Logger _log = Logger.getLogger(C_AuthLogin.class.getName());

     public C_AuthLogin(byte[] decrypt, GameClient client) throws IOException { // 註解: 登錄認證類的構造方法
         super(decrypt); // 註解: 調用父類構造方法

         try {
             String ip = client.getIp(); // 註解: 獲取客戶端的IP地址
             String host = client.getHostname(); // 註解: 獲取客戶端的主機名

             if (IpTable.getInstance().isBannedIp(ip)) { // 註解: 如果IP地址被封禁
                 System.out.println("\n┌───────────────────────────────┐");
                 System.out.println("\t已封禁的IP嘗試連接! IP地址=" + ip); // 註解: 輸出封禁IP嘗試連接的信息
                 System.out.println("└───────────────────────────────┘\n");
                         client.sendPacket((ServerBasePacket)new S_LoginResult(100)); // 註解: 向客戶端發送登錄失敗的消息
                 GeneralThreadPool.getInstance().schedule((Runnable)new DelayClose(client), 5000L); // 註解: 計劃在5秒後關閉連接

                 return; // 註解: 返回，停止後續執行
             }
         } catch (Exception e) {
             e.printStackTrace(); // 註解: 輸出異常信息
         }
     }
       String accountName = readS().toLowerCase();
       String password = readS();
       _log.finest("向使用者請求 AuthLogin： " + accountName);


       Authorization.getInstance().auth(client, accountName, password, ip, host);


     }
     catch (Exception exception) {}
   }



   public String getType() {
     return "[C] C_AuthLogin";
   }
 }


