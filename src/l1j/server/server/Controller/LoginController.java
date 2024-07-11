 package l1j.server.server.Controller;

 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.Config;
 import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
 import l1j.server.server.Account;
 import l1j.server.server.AccountAlreadyLoginException;
 import l1j.server.server.GameClient;
 import l1j.server.server.GameServerFullException;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class LoginController
 {
   private static LoginController _instance;
   private Map<String, GameClient> _accounts = new ConcurrentHashMap<>(Config.Login.MaximumOnlineUsers);
   private Map<String, Integer> _countIp = new ConcurrentHashMap<>(Config.Login.MaximumOnlineUsers);
   private Map<String, Integer> _countCClass = new ConcurrentHashMap<>(Config.Login.MaximumOnlineUsers);


   private int _maxAllowedOnlinePlayers;



   public static LoginController getInstance() {
     if (_instance == null) {
       _instance = new LoginController();
     }
     return _instance;
   }

   public GameClient[] getAllAccounts() {
     return (GameClient[])this._accounts.values().toArray((Object[])new GameClient[this._accounts.size()]);
   }

   public int getOnlinePlayerCount() {
     return this._accounts.size();
   }

   public int getMaxAllowedOnlinePlayers() {
     return this._maxAllowedOnlinePlayers;
   }

   public void setMaxAllowedOnlinePlayers(int maxAllowedOnlinePlayers) {
     this._maxAllowedOnlinePlayers = maxAllowedOnlinePlayers;
   }

   private void kickClient(final GameClient client) {
     if (client == null) {
       return;
     }

     if (client.getActiveChar() != null) {
       client.getActiveChar().sendPackets((ServerBasePacket)new S_ServerMessage(357));
     }

     GeneralThreadPool.getInstance().schedule(new Runnable()
         {
           public void run() {
             client.kick();
           }
         },  1000L);
   }

   public static String getCClass(String ip) {
     return ip.substring(0, ip.lastIndexOf('.'));
   }

     public void login(GameClient client, Account account) throws GameServerFullException, AccountAlreadyLoginException {
         synchronized (this) { // 註解: 同步方法，確保線程安全
             if (!account.isValid()) { // 註解: 檢查帳戶是否有效
                 throw new IllegalArgumentException("未經驗證的帳戶"); // 註解: 如果帳戶無效，拋出非法參數異常
             }
             if (getMaxAllowedOnlinePlayers() <= getOnlinePlayerCount() && !account.isGameMaster()) // 註解: 如果在線玩家數量達到上限，且帳戶不是遊戲管理員
                 throw new GameServerFullException(); // 註解: 拋出伺服器滿員異常
             if (this._accounts.containsKey(account.getName())) { // 註解: 如果帳戶已經在伺服器上登錄
                 kickClient(this._accounts.remove(account.getName())); // 註解: 踢下線並移除該帳戶
                 throw new AccountAlreadyLoginException(); // 註解: 拋出帳戶已登錄異常
             }
       String ip = client.getIp();
       this._accounts.put(account.getName(), client);
       this._countIp.put(ip, Integer.valueOf(getIpCount(ip) + 1));

       String cClass = getCClass(ip);

       if (this._countCClass.containsKey(cClass)) {
         this._countCClass.put(cClass, Integer.valueOf(((Integer)this._countCClass.get(cClass)).intValue() + 1));
       } else {
         this._countCClass.put(cClass, Integer.valueOf(1));
       }
     }
   }


   public boolean containsAccount(String name) {
     return this._accounts.containsKey(name);
   }

   public GameClient getClientByAccount(String name) {
     if (this._accounts.containsKey(name)) {
       return this._accounts.get(name);
     }

     return null;
   }

   public int getIpCount(String ip) {
     if (this._countIp.containsKey(ip)) {
       return ((Integer)this._countIp.get(ip)).intValue();
     }
     return 0;
   }

   public int getCClassCount(String ip) {
     if (this._countCClass.containsKey(getCClass(ip))) {
       return ((Integer)this._countCClass.get(getCClass(ip))).intValue();
     }

     return 0;
   }

   public boolean logout(GameClient client) {
     synchronized (this) {
       if (client == null || client.getAccountName() == null) {
         return false;
       }
       client.setLoginRecord(false);
       client.setStatus(MJClientStatus.CLNT_STS_CONNECTED);
       String ip = client.getIp();
       if (this._countIp.containsKey(ip)) {
         this._countIp.put(ip, Integer.valueOf(getIpCount(ip) - 1));
         if (((Integer)this._countIp.get(ip)).intValue() <= 0) {
           this._countIp.remove(ip);
         }
       }

       String cClass = getCClass(ip);
       if (cClass != null && this._countCClass.containsKey(cClass)) {
         this._countCClass.put(cClass, Integer.valueOf(((Integer)this._countCClass.get(cClass)).intValue() - 1));
         if (((Integer)this._countCClass.get(cClass)).intValue() == 0) {
           this._countCClass.remove(cClass);
         }
       }
       return (this._accounts.remove(client.getAccountName()) != null);
     }
   }

   public void removeClientByAccounts(String accounts) {
     this._accounts.remove(accounts);
   }
 }


