     package MJShiftObject.DB;

     import l1j.server.MJTemplate.MJPropertyReader;

     public class MJShiftDBArgs
     {
       public String SERVER_IDENTITY;
       public String DRIVER_NAME;
       public String URL;
       public String USER_NAME;
       public String PASSWORD;
       public int MIN_POOL_SIZE;
       public int MAX_POOL_SIZE;
       public int CHARACTER_TRANSFER_ITEMID;
       public int MY_BATTLE_SERVER_QUIT_READY_SECONDS;
       public int MY_BATTLE_SERVER_RESERVATION_STORE_READY_SECONDS;
       public int BATTLE_SERVER_SEND_COUNT;

       public MJShiftDBArgs(String file_name) {
         MJPropertyReader reader = new MJPropertyReader(file_name);
         try {
           this.SERVER_IDENTITY = reader.readString("ServerIdentity", "");
           this.DRIVER_NAME = reader.readString("DriverName", "");
           this.URL = reader.readString("URL", "");
           this.USER_NAME = reader.readString("UserName", "");
           this.PASSWORD = reader.readString("Password", "");
           this.MIN_POOL_SIZE = reader.readInt("MinPoolSize", "32");
           this.MAX_POOL_SIZE = reader.readInt("MaxPoolSize", "128");
           this.CHARACTER_TRANSFER_ITEMID = reader.readInt("CharacterTransferItemId", "4100209");
           this.MY_BATTLE_SERVER_QUIT_READY_SECONDS = reader.readInt("MyBattleServerQuitReadySeconds", "30");
           this.MY_BATTLE_SERVER_RESERVATION_STORE_READY_SECONDS = reader.readInt("MyBattleServerStoreReadySeconds", "10");
           this.BATTLE_SERVER_SEND_COUNT = reader.readInt("BattleServerSendCount", "50");
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           if (reader != null) {
             reader.dispose();
             reader = null;
           }
         }
       }
     }


