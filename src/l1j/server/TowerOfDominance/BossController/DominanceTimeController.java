 package l1j.server.TowerOfDominance.BossController;

 import MJFX.UIAdapter.MJUIAdapter;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.List;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.TowerOfDominance.DominanceBoss;
 import l1j.server.TowerOfDominance.DominanceDataLoader;
 import l1j.server.server.GeneralThreadPool;

 public class DominanceTimeController
   implements Runnable {
   private static DominanceTimeController instance;

   public static DominanceTimeController getInstance() {
     if (instance == null) {
       instance = new DominanceTimeController();
       GeneralThreadPool.getInstance().execute(instance);
     }
     return instance;
   }

   private void isSpawnTime() {
     try {
       List<DominanceBoss> list = DominanceDataLoader.getList();
       if (list.size() > 0) {
         long time = System.currentTimeMillis();
         Date date = new Date(time);
         int hour = date.getHours();
         int min = date.getMinutes();
         int sec = date.getSeconds();
         for (DominanceBoss b : list) {
           if (b.isSpawnTime(hour, min, time) && sec == 0) {
             if (!MJRnd.isWinning(1000000, b.getRandomSpawn())) {
               continue;
             }



             if (b.getBossNum() == 1) {
               DominanceFloorLv1 zenis = new DominanceFloorLv1(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
               zenis.Start();
             } else if (b.getBossNum() == 2) {
               DominanceFloorLv2 sier = new DominanceFloorLv2(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
               sier.Start();
             } else if (b.getBossNum() == 3) {
               DominanceFloorLv3 vampire = new DominanceFloorLv3(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
               vampire.Start();
             } else if (b.getBossNum() == 4) {
               DominanceFloorLv4 zombie = new DominanceFloorLv4(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
               zombie.Start();
             } else if (b.getBossNum() == 5) {
               DominanceFloorLv5 kuger = new DominanceFloorLv5(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
               kuger.Start();
             } else if (b.getBossNum() == 6) {
               DominanceFloorLv6 mummy = new DominanceFloorLv6(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
               mummy.Start();
             } else if (b.getBossNum() == 7) {
               DominanceFloorLv7 iris = new DominanceFloorLv7(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
               iris.Start();
             } else if (b.getBossNum() == 8) {
               DominanceFloorLv8 bald = new DominanceFloorLv8(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
               bald.Start();
             } else if (b.getBossNum() == 9) {
               DominanceFloorLv9 rich = new DominanceFloorLv9(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
               rich.Start();
             } else if (b.getBossNum() == 10) {
               DominanceFloorLv10 ugnus = new DominanceFloorLv10(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
               ugnus.Start();
             } else if (b.getBossNum() == 11) {
               DominanceFloorLv11 riper = new DominanceFloorLv11(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
               riper.Start();
             }
             MJUIAdapter.on_boss_append(b.getNpcId(), b.getBossName(), b.getMapX(), b.getMapY(), b.getMapId());
           }

           if (isResetTime(b)) {
             b.resetSpawnTime();
           }
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }


   public void run() {
     try {
       while (true) {
         isSpawnTime();
         Thread.sleep(1000L);
       }
     } catch (Exception e) {
       e.printStackTrace();
       return;
     }
   }
   public boolean isResetTime(DominanceBoss b) {
     Calendar oCalendar = Calendar.getInstance();
     int hour = oCalendar.get(11);
     int min = oCalendar.get(12);
     int sec = oCalendar.get(13);

     if (hour == 0 && min == 0 && sec == 0) {
       return true;
     }
     return false;
   }
 }


