 package l1j.server.server.clientpackets;
 import java.io.IOException;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.Collection;
 import java.util.Random;
 import java.util.logging.Logger;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJNetServer.MJNetServerLoadManager;
 import l1j.server.server.Account;
 import l1j.server.server.AccountAlreadyLoginException;
 import l1j.server.server.Controller.LoginController;
 import l1j.server.server.GameClient;
 import l1j.server.server.GameServer;
 import l1j.server.server.GameServerFullException;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_CommonNews;
 import l1j.server.server.serverpackets.S_Disconnect;
 import l1j.server.server.serverpackets.S_LoginResult;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.types.Point;
 import l1j.server.server.utils.SQLUtil;

 public class Authorization {
   private static Authorization uniqueInstance = null;
   private static Logger _log = Logger.getLogger(C_AuthLogin.class.getName());

   public static Authorization getInstance() {
     if (uniqueInstance == null)
       synchronized (Authorization.class) {
         if (uniqueInstance == null) {
           uniqueInstance = new Authorization();
         }
       }
     return uniqueInstance;
   }


     public synchronized void auth(GameClient client, String accountName, String password, String ip, String host) throws IOException {
         if (!Config.Login.Allow2PC) { // 註解: 如果不允許兩台PC同時登錄
             if (LoginController.getInstance().getIpCount(ip) > 0) { // 註解: 如果相同IP的登錄次數大於0
                 _log.info("拒絕同一IP登錄的兩台PC。帳戶=" + accountName + " IP=" + ip); // 註解: 記錄日誌信息
                 System.out.println("拒絕同一IP登錄的兩台PC。帳戶=" + accountName + " IP=" + ip); // 註解: 輸出到控制台
                 client.sendPacket((ServerBasePacket)new S_CommonNews("當前IP已經有另一個帳戶在連接中。")); // 註解: 發送系統消息，通知玩家當前IP已經有另一個帳戶在連接中
                 try {
                     client.close(); // 註解: 嘗試關閉客戶端連接
                 } catch (Exception e) {
                     e.printStackTrace(); // 註解: 捕捉並打印異常信息
                 }
                 return; // 註解: 返回，停止後續執行
             }
         } else if (LoginController.getInstance().getIpCount(ip) > MJNetServerLoadManager.NETWORK_CLIENT_PERMISSION && !ip.equals(Integer.valueOf(MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT))) {
                        // 註解: 如果相同IP的登錄次數超過允許數量，且IP不匹配指定地址
             _log.info("拒絕同一IP登錄的兩台PC。帳戶=" + accountName + " IP=" + ip); // 註解: 記錄日誌信息
             System.out.println("拒絕同一IP登錄的兩台PC。帳戶=" + accountName + " IP=" + ip); // 註解: 輸出到控制台
             client.sendPacket((ServerBasePacket)new S_CommonNews("當前IP已經有另一個帳戶在連接中。")); // 註解: 發送系統消息，通知玩家當前IP已經有另一個帳戶在連接中
             try {
                 client.close(); // 註解: 嘗試關閉客戶端連接
             } catch (Exception e) {
                 e.printStackTrace(); // 註解: 捕捉並打印異常信息
             }
             return; // 註解: 返回，停止後續執行
         }
         Account account = Account.load(accountName);
         if (account == null) { // 註解: 如果帳戶不存在
             if (Config.Synchronization.AutoCreateAccounts) { // 註解: 如果允許自動創建帳戶

                 if (isCheckIP(ip)) { // 註解: 檢查IP是否超過創建帳戶的限額
                     _log.info(" ■■■■■■■■ 帳戶創建超過限制：請確認帳戶名稱是否正確 -> accountName = " + accountName); // 註解: 記錄日誌信息
                     System.out.println(" ■■■■■■■■ 帳戶創建超過限制：請確認帳戶名稱是否正確 -> accountName = " + accountName); // 註解: 輸出到控制台
                     client.sendPacket((ServerBasePacket)new S_CommonNews(String.format("無法再創建更多帳戶。(最多 %d 個)", new Object[] { Integer.valueOf(MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT), Integer.valueOf(MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT) }))); // 註解: 發送系統消息，通知玩家無法再創建更多帳戶
                     try {
                         Runnable r = () -> client.kick(); // 註解: 創建一個任務來踢下線客戶端

                         GeneralThreadPool.getInstance().schedule(r, 1500L); // 註解: 在1.5秒後執行踢下線任務
                     } catch (Exception exception) {} // 註解: 捕捉並忽略異常
                     return; // 註解: 返回，停止後續執行
                 }
         if (!isValidAccount(accountName)) {
           client.sendPacket((ServerBasePacket)new S_LoginResult(9));
           return;
         }
         if (!isValidPassword(password)) {
           client.sendPacket((ServerBasePacket)new S_LoginResult(10));
           return;
         }
         account = Account.create(accountName, password, ip, host, null);
         account = Account.load(accountName);
       } else {

         _log.warning("使用者帳戶缺失 " + accountName);
         System.out.println("使用者帳戶缺失 " + accountName);
       }
     }
     if (account == null || !account.validatePassword(accountName, password)) {
       client.sendPacket((ServerBasePacket)new S_LoginResult(8));

       return;
     }
         Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers(); // 註解: 獲取所有玩家實例集合
         boolean find = false; // 註解: 初始化一個標誌變量，用於標記是否找到目標玩家
         for (L1PcInstance bugpc : pcs) { // 註解: 遍歷所有玩家
             if (!bugpc.getAccountName().equalsIgnoreCase(accountName) || (bugpc.isPrivateShop() && bugpc.getNetConnection() == null) || (
                     bugpc.getMapId() >= 6000 && bugpc.getMapId() <= 6999) || (bugpc.getMapId() >= 9000 && bugpc.getMapId() <= 9100)) {
                 continue; // 註解: 如果玩家帳戶名不匹配，或玩家在私商狀態且無網絡連接，或玩家在特定地圖區域，則跳過
             }
             bugpc.getMap().setPassable((Point)bugpc.getLocation(), true); // 註解: 設置玩家當前位置的地圖為可通行
             bugpc.setX(33080); // 註解: 設置玩家X坐標
             bugpc.setY(33392); // 註解: 設置玩家Y坐標
             bugpc.setMap((short)4); // 註解: 設置玩家地圖ID
             System.out.println("─────────────────────────────────"); // 註解: 分隔線
             System.out.println("因為同一帳號多重連接，將進行斷開操作 ▶帳號名稱◀: " + accountName); // 註解: 輸出斷開連接的帳號名稱
             System.out.println("─────────────────────────────────"); // 註解: 分隔線
             GameServer.disconnectChar(bugpc); // 註解: 斷開玩家連接
             bugpc.sendPackets((ServerBasePacket)new S_Disconnect()); // 註解: 向玩家客戶端發送斷開連接的數據包
             find = true; // 註解: 設置標誌變量為真，表示已找到並處理目標玩家
         }

     if (find) {
       client.kick();

       return;
     }
     pcs = null;


         int reason = account.getBannedCode(); // 註解: 獲取帳戶被封禁的原因代碼
         if (reason != 0) { // 註解: 如果有封禁原因
             System.out.println("\n┌───────────────────────────────┐"); // 註解: 輸出頂部邊框
             System.out.println("拒絕被封禁帳號的登錄。帳號=" + accountName + " 登入IP位址=" + ip); // 註解: 輸出封禁信息
             System.out.println("└───────────────────────────────┘ \n"); // 註解: 輸出底部邊框

                     client.sendPacket((ServerBasePacket)new S_LoginResult(reason)); // 註解: 向客戶端發送登錄結果數據包，包含封禁原因代碼

             return; // 註解: 返回，停止後續執行
         }
     if (account.getAccessLevel() == Config.ServerAdSetting.GMCODE) {
       Random random = new Random();

       ip = Integer.toString(random.nextInt(80) + 100) + "." + Integer.toString(random.nextInt(100) + 50) + "." + Integer.toString(random.nextInt(100) + 50) + "." + Integer.toString(random.nextInt(100) + 50);
       account.setIp(ip);
     }
         try {
             LoginController.getInstance().login(client, account); // 註解: 嘗試讓客戶端登錄帳戶
             Account.updateLastActive(account, ip); // 註解: 更新帳號的最後活躍時間和IP地址
             client.setAccount(account); // 註解: 為客戶端設置帳戶
             sendNotice(client); // 註解: 發送公告給客戶端
         } catch (GameServerFullException e) { // 註解: 捕捉伺服器滿員異常
             client.kick(); // 註解: 踢下線客戶端
             _log.info("超過最大連接人數: (" + client.getIp() + ") 登錄已被拒絕。"); // 註解: 記錄日誌信息
             System.out.println("超過最大連接人數: (" + client.getIp() + ") 登錄已被拒絕。"); // 註解: 輸出到控制台
             return; // 註解: 返回，停止後續執行
         } catch (AccountAlreadyLoginException e) { // 註解: 捕捉帳號已登錄異常
             _log.info("同一ID的多重連接: (" + client.getIp() + ") 已被強制斷開。"); // 註解: 記錄日誌信息
             System.out.println("同一ID的多重連接: (" + client.getIp() + ") 已被強制斷開。"); // 註解: 輸出到控制台
             client.sendPacket((ServerBasePacket)new S_CommonNews("帳號已經在線。連接將被強制終止。")); // 註解: 發送系統消息，通知玩家帳號已經在線
             client.kick(); // 註解: 踢下線客戶端
             return; // 註解: 返回，停止後續執行
         } catch (Exception e) { // 註解: 捕捉所有其他異常
             _log.info("異常登錄錯誤。帳號=" + accountName + " 主機=" + host); // 註解: 記錄日誌信息
             System.out.println("異常登錄錯誤。帳號=" + accountName + " 主機=" + host); // 註解: 輸出到控制台
             client.kick(); // 註解: 踢下線客戶端
             return; // 註解: 返回，停止後續執行
         } finally {
             account = null; // 註解: 最終將account設置為null
         }

   private void sendNotice(GameClient client) {
     String accountName = client.getAccountName();


     if (S_CommonNews.NoticeCount(accountName) > 0) {
       client.sendPacket((ServerBasePacket)new S_CommonNews(accountName, client));
     } else {
       new C_CommonClick(client);
     }
     accountName = null;
   }

         private boolean isValidAccount(String account) {
             if (account.length() < 5 || account.length() > 12) { // 註解: 如果帳號長度小於5或大於12
             }

             System.out.println("帳號長度檢查（無效）：" + account.length()); // 註解: 輸出帳號長度檢查結果

             return false; // 註解: 返回false，表示帳號無效

         }

     char[] chars = account.toCharArray();
     for (int i = 0; i < chars.length; i++) {
       if (!Character.isLetterOrDigit(chars[i])) {
         return false;
       }
     }

     return true;
   }

   private boolean isValidPassword(String password) {
     if (password.length() < 6) {
       return false;
     }
     if (password.length() > 16) {
       return false;
     }

     boolean hasLetter = false;
     boolean hasDigit = false;

     char[] chars = password.toCharArray();
     for (int i = 0; i < chars.length; i++) {
       if (Character.isLetter(chars[i])) {
         hasLetter = true;
       } else if (Character.isDigit(chars[i])) {
         hasDigit = true;
       } else {
         return false;
       }
     }

     if (!hasLetter || !hasDigit) {
       return false;
     }

     return true;
   }

   private boolean isCheckIP(String ip) {
     int num = 0;
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT count(ip) as cnt FROM accounts WHERE ip=? ");

       pstm.setString(1, ip);
       rs = pstm.executeQuery();

       if (rs.next()) num = rs.getInt("cnt");

       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);


       if (num < MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT) {
         return false;
       }
       return true;
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }  return false;
   }
 }


