 package l1j.server.server.Controller;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.gametime.GameTimeClock;


 public class ShipTimeController
   implements Runnable
 {
   private static ShipTimeController _instance;

   public static ShipTimeController getInstance() {
     if (_instance == null) {
       _instance = new ShipTimeController();
     }
     return _instance;
   }


   public void run() {
     try {
       checkShipTime();
       GeneralThreadPool.getInstance().schedule(this, 5000L);
     } catch (Exception exception) {}
   }


   private void checkShipTime() {
     int servertime = GameTimeClock.getInstance().getGameTime().getSeconds();
     int nowtime = servertime % 86400;
     if ((nowtime >= 12240 && nowtime < 12600) || (nowtime >= 23040 && nowtime < 23400) || (nowtime >= 33840 && nowtime < 34200) || (nowtime >= 44640 && nowtime < 45000) || (nowtime >= 55440 && nowtime < 55800) || (nowtime >= 66240 && nowtime < 66600) || (nowtime >= 77040 && nowtime < 77400) || (nowtime >= 1440 && nowtime < 1800))
     {


       for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
         if (pc.getMapId() == 5) {
           pc.start_teleport(32538, 32728, 4, pc.getHeading(), 18339, true, false);
           pc.getInventory().consumeItem(40299, 1); continue;
         }  if (pc.getMapId() == 6) {
           pc.start_teleport(32631, 32983, 0, pc.getHeading(), 18339, true, false);
           pc.getInventory().consumeItem(40298, 1); continue;
         }  if (pc.getMapId() == 447) {
           pc.start_teleport(32297, 33087, 440, pc.getHeading(), 18339, true, false);
           pc.getInventory().consumeItem(40302, 1); continue;
         }  if (pc.getMapId() == 446) {
           pc.start_teleport(32750, 32874, 445, pc.getHeading(), 18339, true, false);
           pc.getInventory().consumeItem(40303, 1);
         }
       }
     }
   }
 }


