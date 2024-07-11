 package l1j.server.server.Controller;

 import java.util.Calendar;
 import java.util.Collection;
 import java.util.Date;
 import java.util.Iterator;
 import l1j.server.DeathMatch.DeathMatch;
 import l1j.server.ForgottenIsland.FIController;
 import l1j.server.InfinityBattle.InfinityBattle;
 import l1j.server.MJTemplate.Builder.MJLiftGateBuilder;
 import l1j.server.MJTemplate.Interface.MJMonsterDeathHandler;
 import l1j.server.MJTemplate.Interface.MJMonsterTransformHandler;
 import l1j.server.MJTemplate.L1Instance.MJL1LiftGateInstance;
 import l1j.server.MJTemplate.Lineage2D.MJPoint;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_CHANGE_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_INFO_NOTI;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.QueenAntSystem.QueenAntController;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.EventLogTable;
 import l1j.server.server.datatables.EventTimeTable;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.serverpackets.S_DisplayEffect;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.L1SpawnUtil;




 public class EventThread
   implements Runnable
 {
   private static EventThread instance;
   L1Map[] CrackIntheTower = new L1Map[1];

   private static final int[] _Crackfloor = new int[] { 101, 102, 103, 104, 105, 106, 107, 108, 109 };


   public int num1 = 0;
   public static MJL1LiftGateInstance _leftBDoor;

   public static EventThread getInstance() {
     if (instance == null) {
       instance = new EventThread();
     }
     return instance;
   }


   public static MJL1LiftGateInstance _rightBDoor;

   public static MJL1LiftGateInstance _centerBDoor;
   public static int _CrackInTheTower;

   public static int getCrackIntheTower() {
     return _CrackInTheTower;
   }

   public static void setCrackIntheTower(int num) {
     _CrackInTheTower = num;
   }

   private EventThread() {
     if (_leftBDoor == null) {
       MJLiftGateBuilder builder = (new MJLiftGateBuilder()).setGfx(16115);
       _leftBDoor = builder.build(32731, 32852, (short)15404, false, 3);
       _leftBDoor.down();
       _rightBDoor = builder.build(32731, 32878, (short)15404, false, 3);
       _rightBDoor.down();
       _centerBDoor = builder.build(16116, 32718, 32863, (short)15404, true, 5);

       _centerBDoor.down();
     }
   }

   private static void on(int id) {
     GeneralThreadPool.getInstance().schedule(new BalogDoorController(id), 30000L);
   }

   public static class BalogDoorController
     implements Runnable {
     private int _id;

     BalogDoorController(int ownerId) {
       this._id = ownerId;
     }



     public void run() {
       L1MonsterInstance m = (L1MonsterInstance)L1World.getInstance().findObject(this._id);
       if (m == null || m.isDead()) {
         EventThread._leftBDoor.down();
         EventThread._rightBDoor.down();
         EventThread._centerBDoor.down();

         return;
       }
       try {
         if (MJRnd.isBoolean()) {
           EventThread._leftBDoor.takeClose(10000L);
           EventThread._rightBDoor.up();
           EventThread._centerBDoor.up();
         } else if (MJRnd.isBoolean()) {
           EventThread._leftBDoor.up();
           EventThread._rightBDoor.takeClose(10000L);
           EventThread._centerBDoor.up();
         } else {
           EventThread._leftBDoor.up();
           EventThread._rightBDoor.up();
           EventThread._centerBDoor.takeClose(10000L);
         }
       } finally {
         GeneralThreadPool.getInstance().schedule(this, 30000L);
       }
     }
   }

   private void start_event_boss() {
     try {
       Iterator<L1NpcInstance> npc_iter = EventTimeTable.getInstance().get_npc_iter();
       L1NpcInstance npc = null;


       while (npc_iter.hasNext()) {
         npc = npc_iter.next();
         if (npc == null) {
           continue;
         }

         if (get_boss_spawn_day_check(npc.getYoil(), npc.get_next_day_index()) && get_boss_spawn_time(npc.get_boss_hour(), npc.get_boss_minute())) {

           EventLogTable.table().newBossSpawn(npc, System.currentTimeMillis(), npc.get_end_boss_time());

           if (!npc.is_boss_alarm()) {
             continue;
           }
           if (npc.getNpcId() == 81111 || npc.getNpcId() == 8500129 || npc
             .getNpcId() == 8502042) {
             continue;
           }

           if (npc.get_boss_type() == 5) {
             InfinityBattle.getInstance().Start();
           }


           SC_NOTIFICATION_INFO_NOTI.onEventTicks(L1World.getInstance().getAllPlayers(), 500L);
           Collection<L1PcInstance> allPlayers = L1World.getInstance().getAllPlayers();
           for (L1PcInstance pc : allPlayers) {
             if (pc.isBossNotify() &&
               npc.get_boss_msg() != null) {
               pc.sendPackets((ServerBasePacket)new S_PacketBox(84, npc.get_boss_msg().toString()));


               if (npc.get_boss_effect() == 0 ||
                 pc.isPrivateShop() || !pc.isBossNotify())
                 continue;
               if (npc.get_boss_effect() > 10) {
                 pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), npc.get_boss_effect())); continue;
               }
               pc.sendPackets((ServerBasePacket)S_DisplayEffect.newInstance(npc.get_boss_effect()));
             }
           }



           if (npc.get_boss_yn()) {
             for (L1PcInstance pc : allPlayers) {
               if (!pc.isBossNotify() ||
                 pc.isPrivateShop())
                 continue;
               pc.setBossNpc(npc.getNpcId());
               pc.sendPackets((ServerBasePacket)new S_Message_YN(12, 6008, npc.get_boss_yn_msg()));
             }
           }


           if (npc.getNpcId() == 8503163) {
             for (L1PcInstance pc : allPlayers) {
               SC_NOTIFICATION_CHANGE_NOTI.sendQueenAnt(pc);
             }
             QueenAntController open = new QueenAntController();
             open.Start();
           }

             if (npc.getNpcId() == 120717) { // 註解: 如果 NPC 的 ID 是 120717
                 int i = _Crackfloor[MJRnd.next(_Crackfloor.length)]; // 註解: 隨機選擇一個破裂樓層
                 this.CrackIntheTower[0] = L1WorldMap.getInstance().getMap((short)i).set_CrackIntheTower(true); // 註解: 設置選中的樓層有裂縫
                 this.num1 = i; // 註解: 記錄選中的樓層

                 setCrackIntheTower(this.num1); // 註解: 設置塔的裂縫
                 int floor = this.num1 - 100; // 註解: 計算樓層數
                 for (L1PcInstance pc : allPlayers) { // 註解: 遍歷所有玩家
                     SC_NOTIFICATION_CHANGE_NOTI.AnimationAlam(pc, npc); // 註解: 發送動畫警報
                     pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "傲慢之塔 " + floor + "層開始出現可疑的影子。")); // 註解: 發送消息通知玩家
                 }
             }

             if (npc.get_boss_type() == 50) { // 註解: 如果 NPC 的 boss 類型是 50
                 FIController.getInstance().Start(); // 註解: 開始 FI 控制器
                 for (L1PcInstance pc : allPlayers) { // 註解: 遍歷所有玩家
                     pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "被遺忘的島嶼已經開放。")); // 註解: 發送消息通知玩家
                 }
             }

             if (npc.get_boss_type() == 51 || npc.get_boss_type() == 52 || npc.get_boss_type() == 53 || npc.get_boss_type() == 54) { // 註解: 如果 NPC 的 boss 類型是 51、52、53 或 54
                 DeathMatch open = new DeathMatch(13005); // 註解: 開始一個新的 DeathMatch
                 for (L1PcInstance pc : allPlayers) { // 註解: 遍歷所有玩家
                     pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "團隊死亡競賽已經開始。")); // 註解: 發送消息通知玩家
                 }
                 System.out.println("DeathMatch 開始"); // 註解: 在控制台打印消息
                 open.Start(); // 註解: 開始 DeathMatch
             }


           MJPoint pt = MJPoint.newInstance(npc.getHomeX(), npc
               .getHomeY(), npc.getHomeRnd(), npc
               .getMapId(), 50);
           L1NpcInstance realNpc = L1SpawnUtil.Gmspawn(npc.getNpcId(), pt.x, pt.y, pt.mapId, npc
               .getHeading(), npc
               .getBossIngTime() * 60 * 1000);
           if (npc.getNpcId() == 45752) {
             _leftBDoor.up();
             _rightBDoor.up();
             L1MonsterInstance m = (L1MonsterInstance)realNpc;
             m.setTransformHandler(new MJMonsterTransformHandler()
                 {
                   public void onTransFormNotify(L1MonsterInstance m) {
                     EventThread._centerBDoor.up();
                     EventThread.on(m.getId());
                   }
                 });
             m.setDeathHandler(new MJMonsterDeathHandler()
                 {
                   public boolean onDeathNotify(L1MonsterInstance m) {
                     EventThread._leftBDoor.down();
                     EventThread._rightBDoor.down();
                     EventThread._centerBDoor.down();
                     return false;
                   }
                 });
           }
         }

         Calendar end_calendar = Calendar.getInstance();
         long end_time = npc.get_end_boss_time();
         Date end = new Date(end_time);
         end_calendar.setTime(end);


         int end_hour = end_calendar.get(11);
         int end_min = end_calendar.get(12);



         if (EventTimeTable.getNowDay() >= end_calendar.get(7) &&
           get_boss_delete_time(end_hour, end_min)) {
           int next_index = npc.get_next_day_index() + 1;
           if (next_index == (npc.getYoil()).length) {
             next_index--;
           } else if (next_index > (npc.getYoil()).length) {
             next_index = 0;
           }

           if (npc.getNpcId() == 120717 &&
             this.CrackIntheTower[0] != null) {
             this.CrackIntheTower[0].set_CrackIntheTower(false);
             this.num1 = 0;
             setCrackIntheTower(0);
           }

           if (npc.get_boss_type() == 50) {
             FIController.getInstance().End();
           }

           int now_day = EventTimeTable.getNowDay();
           int week_day = EventTimeTable.getNowDatByString(npc.getYoil()[next_index]);
           int day_gap = now_day - week_day;

           if (day_gap > 0) {
             day_gap = (7 - day_gap) * -1;
           }

           long spawn_time = npc.get_boss_time() + (day_gap * -1 * 86400000);





           npc.set_boss_time(spawn_time);
           npc.set_next_day_index(next_index);
           npc.set_end_boss_time(npc.get_boss_time() + (npc
               .getBossIngTime() * 60000));

           if (!npc.is_boss_alarm()) {
             continue;
           }
           npc.set_boss_time(npc.get_boss_time() + 86400L);

           Iterator<L1PcInstance> pc_iter = L1World.getInstance().getAllPlayers().iterator();
           L1PcInstance pc = null;

           while (pc_iter.hasNext()) {
             pc = pc_iter.next();
             if (pc == null) {
               continue;
             }

             if (pc.getBossNpc() != 0) {
               pc.setBossNpc(0);
             }
             pc.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(pc, npc.getNpcId(), true));



           }




         }



       }



     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }

   public boolean get_boss_spawn_day_check(String[] yoil, int index) {
     int now_day = EventTimeTable.getNowDay();
     int week_day = EventTimeTable.getNowDatByString(yoil[index]);

     if (now_day == week_day) {
       return true;
     }
     return false;
   }



   public boolean get_boss_spawn_time(int h, int m) {
     Date set = new Date(System.currentTimeMillis());
     int hour = set.getHours();
     int minute = set.getMinutes();

     if (m >= 60) {
       h += m / 60;
       m -= m / 60 * 60;
     }

     if (hour == h && minute == m) {
       return true;
     }
     return false;
   }

   public boolean get_boss_delete_time(int h, int m) {
     Date set = new Date(System.currentTimeMillis());
     int hour = set.getHours();
     int minute = set.getMinutes();

     if (m >= 60) {
       h += m / 60;
       m -= m / 60 * 60;
     }

     if (hour >= h && minute >= m) {
       return true;
     }
     return false;
   }






   public void run() {
     try {
       start_event_boss();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       GeneralThreadPool.getInstance().schedule(this, 60000L);
     }
   }
 }


