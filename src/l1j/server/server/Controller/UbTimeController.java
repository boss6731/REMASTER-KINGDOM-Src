 package l1j.server.server.Controller;

 import l1j.server.server.datatables.UBTable;
 import l1j.server.server.model.L1UltimateBattle;



 public class UbTimeController
   implements Runnable
 {
   public static final int SLEEP_TIME = 15000;
   private static UbTimeController _instance;

   public static UbTimeController getInstance() {
     if (_instance == null) {
       _instance = new UbTimeController();
     }
     return _instance;
   }


   public void run() {
     try {
       checkUbTime();
     } catch (Exception e1) {
       e1.printStackTrace();
     }
   }

   private void checkUbTime() {
     for (L1UltimateBattle ub : UBTable.getInstance().getAllUb()) {
       if (ub.checkUbTime() && !ub.isActive())
         ub.start();
     }
   }
 }


