 package l1j.server.server;

 import MJFX.UIAdapter.MJUIAdapter;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashSet;
 import l1j.server.MJNetServer.MJNetServerLoadManager;
 import l1j.server.MJTemplate.MJObjectWrapper;
 import l1j.server.MJTemplate.MJReadWriteLock;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

 public class AccountService
 {
   private static final AccountService service = new AccountService();
   public static AccountService service() {
     return service;
   }
   private final MJReadWriteLock lock;
   private final HashSet<String> accounts;

   private AccountService() {
     this.lock = new MJReadWriteLock();
     this.accounts = new HashSet<>(256);
   }


   public Account onNewAccount(String account, String password, String address, String phone) throws AlreadyAccountsException, AddressOverException {
     Account aInfo = null;
     try {
       this.lock.writeLock();
       if (this.accounts.contains(account)) {
         throw new AlreadyAccountsException();
       }
       if (alreadyAccountDatabase(account)) {
         throw new AlreadyAccountsException();
       }
       if (numOfAddress(address) >= MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT) {
         throw new AddressOverException();
       }
       Account.create(account, password, address, address, phone);
       aInfo = Account.load(account);
       if (aInfo == null) {
         throw new AlreadyAccountsException();
       }
       MJUIAdapter.on_create_account(account, address);
       this.accounts.add(account);
     } finally {
       this.lock.writeUnlock();
     }
     return aInfo;
   }

   public boolean alreadyAccount(String account) {
     try {
       this.lock.readLock();
       if (this.accounts.contains(account)) {
         return true;
       }
       return alreadyAccountDatabase(account);
     } finally {
       this.lock.readUnlock();
     }
   }

   private boolean alreadyAccountDatabase(final String account) {
     final MJObjectWrapper<Boolean> wrapper = new MJObjectWrapper();
     wrapper.value = Boolean.valueOf(false);
     Selector.exec("select login from accounts where login=? limit 1", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception
           {
             pstm.setString(1, account);
           }


           public void result(ResultSet rs) throws Exception {
             wrapper.value = Boolean.valueOf(rs.next());
           }
         });
     return ((Boolean)wrapper.value).booleanValue();
   }

   public int numOfAddress(final String address) {
     final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper();
     wrapper.value = Integer.valueOf(0);
     Selector.exec("select count(ip) as numOfAddress from accounts where ip=?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception
           {
             pstm.setString(1, address);
           }


           public void result(ResultSet rs) throws Exception {
             if (rs.next()) {
               wrapper.value = Integer.valueOf(rs.getInt("numOfAddress"));
             }
           }
         });
     return ((Integer)wrapper.value).intValue();
   }

   public static class AlreadyAccountsException extends Exception {
     private static final long serialVersionUID = 1L;
   }

   public static class AddressOverException extends Exception {
     private static final long serialVersionUID = 1L;
   }
 }


