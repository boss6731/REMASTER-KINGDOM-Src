 package l1j.server.server.model;

 public class L1SoulStoneSystem {
     private static L1SoulStoneSystem _instance;

     public static L1SoulStoneSystem getInstance() {
         if (_instance == null)
             _instance = new L1SoulStoneSystem();
         return _instance;
     }

     public static void release() {
         if (_instance != null) {
             _instance.clear();
             _instance = null;
         }
     }

     public static void reload() {
         L1SoulStoneSystem tmp = _instance;
         _instance = new L1SoulStoneSystem();
         if (tmp != null) {
             tmp.clear();
             tmp = null;
         }
     }

     public void clear() {}
 }


