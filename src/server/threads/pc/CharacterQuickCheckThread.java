     package server.threads.pc;

     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1World;

     public class CharacterQuickCheckThread
       extends Thread {
       private static CharacterQuickCheckThread _instance;

       public static CharacterQuickCheckThread getInstance() {
         if (_instance == null) {
           _instance = new CharacterQuickCheckThread();
           _instance.start();
         }
         return _instance;
       }

       public CharacterQuickCheckThread() {
         super("server.threads.pc.CharacterQuickCheckThread");
       }


       public void run() {
         while (true) {
           try {
             for (L1PcInstance _client : L1World.getInstance().getAllPlayers()) {
               if (_client.isPrivateShop() || _client.getAI() == null || _client.noPlayerCK || _client.getNetConnection() == null) {
                 continue;
               }
               try {
                 if (_client.getNetConnection().isClosed()) {
                   System.out.println(_client.getName() + " << 無法結束：請確認是否有多個實體同時出現。");
                   _client.logout();
                   _client.getNetConnection().close();
                 }
               } catch (Exception exception) {}
             }

           }
           catch (Exception e) {
             e.printStackTrace();
           } finally {
             try {
               Thread.sleep(60000L);
             } catch (InterruptedException e) {

               e.printStackTrace();
             }
           }
         }
       }
     }


