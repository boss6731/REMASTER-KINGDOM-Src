 package l1j.server.server.Controller;

 import MJFX.UIAdapter.MJUIAdapter;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.List;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.Config;
 import l1j.server.MJBotSystem.AI.MJBotAI;
 import l1j.server.MJBotSystem.AI.MJBotMovableAI;
 import l1j.server.MJBotSystem.Loader.MJBotBossNotifierLoader;
 import l1j.server.MJBotSystem.MJBotBossNotifier;
 import l1j.server.MJBotSystem.MJBotType;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.BossMonsterSpawnList;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1MobGroupSpawn;
 import l1j.server.server.model.L1NpcDeleteTimer;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_DisplayEffect;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Boss;
 import l1j.server.server.types.Point;


 public class BossController
   implements Runnable
 {
   private static BossController instance;
   private static final ConcurrentHashMap<Integer, Integer> _bossesId = new ConcurrentHashMap<>(256);

   public static BossController getInstance() {
     if (instance == null) {
       instance = new BossController();
       GeneralThreadPool.getInstance().execute(instance);
     }
     return instance;
   }

   private void spawn_check() {
     try {
       List<L1Boss> list = BossMonsterSpawnList.getList();
       if (list.size() > 0) {
         long time = System.currentTimeMillis();
         Date date = new Date(time);
         int hour = date.getHours();
         int min = date.getMinutes();
         int sec = date.getSeconds();
         for (L1Boss b : list) {

           try {
             if (b.isSpawnTime(hour, min, time) && sec == 0) {
               Integer id = _bossesId.get(Integer.valueOf(b.getNpcId()));
               if (id != null) {
                 L1Object obj = L1World.getInstance().findObject(id.intValue());
                 if (obj != null && obj.instanceOf(1024)) {
                   L1Character c = (L1Character)obj;
                   if (!c.isDead()) {
                     continue;
                   }
                 }
               }
               int delete_time = b.getDeleteTime() * 1000;


               if (b.getNpcId() == 8502091) {
                 for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                   if (pc.getMapId() == 111) {
                     pc.start_teleport(32619, 32807, 111, pc.getHeading(), 18339, true);
                   }
                 }
               }

               if (b.getNpcType().equalsIgnoreCase("Monster")) {
                 if (b.getnonespawntime() != 0 && MJRnd.isWinning(1000000, b.getnonespawntime())) {
                   return;
                 }



                 L1MonsterInstance boss = boss_spawn(b, b.getX(), b.getY(), (short)b.getMap(), b.getNpcId(), b.getRndLoc(), delete_time, b.isMent(), b.isYn(), b.get_display_effect());

                 if (b.getGroupId() != 0) {
                   L1MobGroupSpawn.getInstance().doSpawn((L1NpcInstance)boss, b.getGroupId(), false, false);
                 }

                 if (boss != null) {
                   MJUIAdapter.on_boss_append(boss.getNpcId(), boss.getName(), boss.getX(), boss.getY(), boss.getMapId());
                   _bossesId.put(Integer.valueOf(boss.getNpcId()), Integer.valueOf(boss.getId()));
                 }
               } else {
                 L1NpcInstance boss = npc_spawn(b, b.getX(), b.getY(), (short)b.getMap(), b.getNpcId(), b.getRndLoc(), delete_time, b.isMent(), b.isYn(), b.get_display_effect());

                 if (boss != null) {
                   MJUIAdapter.on_boss_append(boss.getNpcId(), boss.getName(), boss.getX(), boss.getY(), boss.getMapId());
                   _bossesId.put(Integer.valueOf(boss.getNpcId()), Integer.valueOf(boss.getId()));
                 }
               }
             }

             if (isResetTime(b)) {
               b.resetSpawnTime();
             }
           } catch (Exception e) {
             e.printStackTrace();
           }
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }





   public void run() {
     try {
       spawn_check();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       GeneralThreadPool.getInstance().schedule(this, 1000L);
     }
   }


   public boolean isResetTime(L1Boss b) {
     Calendar oCalendar = Calendar.getInstance();
     int hour = oCalendar.get(11);
     int min = oCalendar.get(12);
     int sec = oCalendar.get(13);

     if (hour == 0 && min == 0 && sec == 0) {
       return true;
     }
     return false;
   }

   public static L1MonsterInstance boss_spawn(L1Boss b, int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, boolean ment, boolean ynment, int display_effect) {
     L1NpcInstance npc = null;
     try {
       npc = NpcTable.getInstance().newNpcInstance(npcId);
       npc.setId(IdFactory.getInstance().nextId());
       npc.setMap(map);

       if (b.getMovement_distance() > 0) {
         npc.setMovementDistance(b.getMovement_distance());
       }
       if (randomRange == 0) {
         npc.getLocation().set(x, y, map);
       } else {

         int tryCount = 0;
         do {
           tryCount++;
           npc.setX(x + (int)(Math.random() * randomRange) - (int)(Math.random() * randomRange));
           npc.setY(y + (int)(Math.random() * randomRange) - (int)(Math.random() * randomRange));
           if (npc.getMap().isInMap((Point)npc.getLocation()) && npc.getMap().isPassable((Point)npc.getLocation())) {
             break;
           }

           Thread.sleep(1L);
         } while (tryCount < 50);

         if (tryCount >= 50) {
           npc.getLocation().set(x, y, map);
           npc.getLocation().forward(5);
         }
       }


       if (npc.getNpcId() == 900007 || npc.getNpcId() == 900015 || npc.getNpcId() == 900036 || npc.getNpcId() == 900219) {
         for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer((L1Object)npc)) {
           npc.onPerceive(_pc);
           S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), 11);
           _pc.sendPackets((ServerBasePacket)gfx);
         }
       }

       npc.setHomeX(npc.getX());
       npc.setHomeY(npc.getY());
       npc.setHeading(5);

       L1World.getInstance().storeObject((L1Object)npc);
       L1World.getInstance().addVisibleObject((L1Object)npc);


       if (ment) {
         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_SystemMessage(b.getMentMessage()));
         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, b.getMentMessage()));
       }

       S_DisplayEffect effect = null;
       if (ynment && !Config.Login.StandbyServer) {
         if (display_effect > 0)
           effect = S_DisplayEffect.newInstance(display_effect);
         for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
           if (pc.isPrivateShop() || !pc.isBossNotify() || !pc.is_world() || !pc.getMap().isEscapable() || pc.is_shift_battle())
             continue;
           if (effect != null) {
             pc.sendPackets((ServerBasePacket)effect, false);
           }
           if (pc.getAI() != null && pc.getAI().getBotType() == MJBotType.HUNT) {
             MJBotBossNotifier ntf = MJBotBossNotifierLoader.getInstance().get(npc.getNpcId());
             MJBotAI ai = pc.getAI();
             if (ai instanceof MJBotMovableAI && (
               (MJBotMovableAI)ai).getWarCastle() != -1) {
               continue;
             }

             if (ntf != null && ai.getBrain().toRand(100 - ntf.aggro) < ai.getBrain().getHormon())
               ai.teleport(npc.getX(), npc.getY(), npc.getMapId());
             continue;
           }
           pc.setBossYN(npc.getNpcId());

           pc.sendPackets((ServerBasePacket)new S_Message_YN(1, 6008, b.getYnMessage()));
         }
         if (effect != null)
           effect.clear();
         try {
           GeneralThreadPool.getInstance().schedule(new Runnable()
               {
                 public void run() {
                   for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                     if (pc.getBossYN() != 0) {
                       pc.setBossYN(0);
                     }
                   }
                 }
               },  10000L);
         } catch (Exception exception) {}
       }

       npc.getLight().turnOnOffLight();
       npc.startChat(4);
       npc.startChat(0);
       if (0 < timeMillisToDelete) {
         L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
         timer.begin();
       }

     } catch (Exception e) {
       e.printStackTrace();
     }
     return (npc instanceof L1MonsterInstance) ? (L1MonsterInstance)npc : null;
   }

   public static L1NpcInstance npc_spawn(L1Boss b, int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, boolean ment, boolean ynment, int display_effect) {
     L1NpcInstance npc = null;
     try {
       npc = NpcTable.getInstance().newNpcInstance(npcId);
       npc.setId(IdFactory.getInstance().nextId());
       npc.setMap(map);
       if (randomRange == 0) {
         npc.getLocation().set(x, y, map);
       } else {

         int tryCount = 0;
         do {
           tryCount++;
           npc.setX(x + (int)(Math.random() * randomRange) - (int)(Math.random() * randomRange));
           npc.setY(y + (int)(Math.random() * randomRange) - (int)(Math.random() * randomRange));
           if (npc.getMap().isInMap((Point)npc.getLocation()) && npc.getMap().isPassable((Point)npc.getLocation())) {
             break;
           }
           Thread.sleep(1L);
         } while (tryCount < 50);

         if (tryCount >= 50) {
           npc.getLocation().set(x, y, map);
           npc.getLocation().forward(5);
         }
       }

       if (npc.getNpcId() == 900007 || npc.getNpcId() == 900015 || npc.getNpcId() == 900036 || npc.getNpcId() == 900219) {
         for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer((L1Object)npc)) {
           npc.onPerceive(_pc);
           S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), 11);
           _pc.sendPackets((ServerBasePacket)gfx);
         }
       }

       npc.setHomeX(npc.getX());
       npc.setHomeY(npc.getY());
       npc.setHeading(5);

       L1World.getInstance().storeObject((L1Object)npc);
       L1World.getInstance().addVisibleObject((L1Object)npc);

       if (ment) {
         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_SystemMessage(b.getMentMessage()));
         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, b.getMentMessage()));
       }

       S_DisplayEffect effect = null;
       if (ynment && !Config.Login.StandbyServer) {
         if (display_effect > 0)
           effect = S_DisplayEffect.newInstance(display_effect);
         for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
           if (pc.isPrivateShop() || !pc.isBossNotify() || !pc.is_world() || !pc.getMap().isEscapable() || pc.is_shift_battle())
             continue;
           if (effect != null) {
             pc.sendPackets((ServerBasePacket)effect, false);
           }
           if (pc.getAI() != null && pc.getAI().getBotType() == MJBotType.HUNT) {
             MJBotBossNotifier ntf = MJBotBossNotifierLoader.getInstance().get(npc.getNpcId());
             MJBotAI ai = pc.getAI();
             if (ai instanceof MJBotMovableAI && (
               (MJBotMovableAI)ai).getWarCastle() != -1) {
               continue;
             }

             if (ntf != null && ai.getBrain().toRand(100 - ntf.aggro) < ai.getBrain().getHormon())
               ai.teleport(npc.getX(), npc.getY(), npc.getMapId());
             continue;
           }
           pc.setBossYN(npc.getNpcId());

           pc.sendPackets((ServerBasePacket)new S_Message_YN(1, 6008, b.getYnMessage()));
         }
         if (effect != null)
           effect.clear();
         try {
           GeneralThreadPool.getInstance().schedule(new Runnable()
               {
                 public void run() {
                   for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                     if (pc.getBossYN() != 0) {
                       pc.setBossYN(0);
                     }
                   }
                 }
               },  10000L);
         } catch (Exception exception) {}
       }

       npc.getLight().turnOnOffLight();
       npc.startChat(4);
       npc.startChat(0);
       if (0 < timeMillisToDelete) {
         L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
         timer.begin();
       }

     } catch (Exception e) {
       e.printStackTrace();
     }
     return (npc instanceof L1NpcInstance) ? npc : null;
   }
 }


