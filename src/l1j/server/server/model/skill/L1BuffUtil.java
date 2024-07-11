 package l1j.server.server.model.skill;

 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Iterator;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_Liquor;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillBrave;
 import l1j.server.server.serverpackets.S_SkillHaste;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Skills;




 public class L1BuffUtil
 {
   public static void haste(L1PcInstance pc, int timeMillis) {
     pc.setSkillEffect(1001, timeMillis);
     int objId = pc.getId();
     pc.sendPackets((ServerBasePacket)new S_SkillHaste(objId, 1, timeMillis / 1000));
     pc.broadcastPacket((ServerBasePacket)new S_SkillHaste(objId, 1, 0));
     pc.sendPackets((ServerBasePacket)new S_SkillSound(objId, 191));
     pc.broadcastPacket((ServerBasePacket)new S_SkillSound(objId, 191));
     pc.setMoveSpeed(1);
   }

   public static void barrier(L1PcInstance pc, int timeMillis) {
     pc.setSkillEffect(78, timeMillis);
   }

   public static void cancelBarrier(L1PcInstance pc) {
     if (pc.hasSkillEffect(78)) {
       pc.removeSkillEffect(78);
     }
   }

   public static void brave(L1PcInstance pc, int timeMillis) {
     pc.setSkillEffect(1000, timeMillis);
     int objId = pc.getId();
     pc.sendPackets((ServerBasePacket)new S_SkillBrave(objId, 1, timeMillis / 1000));
     pc.broadcastPacket((ServerBasePacket)new S_SkillBrave(objId, 1, 0));
     pc.sendPackets((ServerBasePacket)new S_SkillSound(objId, 751));
     pc.broadcastPacket((ServerBasePacket)new S_SkillSound(objId, 751));
     pc.setBraveSpeed(1);
   }

   public static void thirdSpeed(L1PcInstance pc, int timeMillis) {
     if (pc.hasSkillEffect(22017)) {
       pc.killSkillEffectTimer(22017);
     }
     pc.sendPackets((ServerBasePacket)new S_ServerMessage(1065, timeMillis / 1000));
     pc.setSkillEffect(22017, timeMillis);
     pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 8031));
     pc.broadcastPacket((ServerBasePacket)new S_SkillSound(pc.getId(), 8031));
     pc.sendPackets((ServerBasePacket)new S_Liquor(pc.getId(), 8));
     pc.broadcastPacket((ServerBasePacket)new S_Liquor(pc.getId(), 8));
   }

   public static void useBuff(L1PcInstance pc, int skillId) {
     (new L1SkillUse()).handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 4);
   }

   public static void takeAllBuff(Collection<L1PcInstance> pcList, int buffType, int time) {
     ArrayList<Integer> buffList = null;
     if (buffType == 1) {
       buffList = getBuffList_1();
     } else if (buffType == 2) {
       buffList = new ArrayList<>();
       buffList.add(Integer.valueOf(22000));
     } else if (buffType == 3) {
       buffList = new ArrayList<>();
       buffList.add(Integer.valueOf(4914));
     } else if (buffType == 4) {
       buffList = new ArrayList<>();
       buffList.add(Integer.valueOf(50007));
     }
     for (L1PcInstance target : pcList) {
       if (target == null || target.isFishing()) {
         continue;
       }
       if (buffType == 2) {
         if (target.hasSkillEffect(22000)) {
           target.removeSkillEffect(22000);
         }
         if (target.hasSkillEffect(22001)) {
           target.removeSkillEffect(22001);
         }
         if (target.hasSkillEffect(22002)) {
           target.removeSkillEffect(22002);
         }
         if (target.hasSkillEffect(22003)) {
           target.removeSkillEffect(22003);
         }
       } else if (buffType == 3) {
         if (target.hasSkillEffect(4914)) {
           target.removeSkillEffect(4914);
         }
       } else if (buffType == 4 &&
         target.hasSkillEffect(50007)) {
         target.removeSkillEffect(50007);
       }

       if (target != null && !target.isDead()) {
         if (target.noPlayerRobot) {
           for (Iterator<Integer> iterator = buffList.iterator(); iterator.hasNext(); ) {
             int skillId = ((Integer)iterator.next()).intValue();
             L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
             target.broadcastPacket((ServerBasePacket)new S_SkillSound(target.getId(), skill.getCastGfx()));
           }  continue;
         }
         for (Iterator<Integer> localIterator2 = buffList.iterator(); localIterator2.hasNext(); ) {
           int skillId = ((Integer)localIterator2.next()).intValue();
           if (skillId == 163 && target.getCurrentWeapon() == 20) {
             skillId = 166;
           } else if (skillId == 148 && target.getCurrentWeapon() == 20) {
             skillId = 149;
           } else if (skillId == 376 && target.is전사()) {
             skillId = 383;
           }
           if (skillId <= 240) {
             (new L1SkillUse()).handleCommands(target, skillId, target.getId(), target.getX(), target
                 .getY(), null, time * 60, 1);
             L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
             target.sendPackets((ServerBasePacket)new S_PacketBox(154, skill.getCastGfx(), time * 60)); continue;
           }
           useBuff(target, skillId);
         }
       }
     }
   }



   public static void startAllBuffThread(L1PcInstance user, String range, int buffType) {
     (new AllBuffThread(user, range, buffType)).begin();
   }

   public static class AllBuffThread implements Runnable {
     L1PcInstance _user;
     String _range;
     int _buffType;
     int _time;

     AllBuffThread(L1PcInstance user, String range, int buffType) {
       this._user = user;
       this._range = range;
       this._buffType = buffType;
     }

     public void run() {
       try {
         ArrayList<Integer> buffList = null;
         if (this._buffType == 1) {
           buffList = L1BuffUtil.getBuffList_1();
         } else if (this._buffType == 2) {
           buffList = new ArrayList<>();
           buffList.add(Integer.valueOf(22000));
         } else if (this._buffType == 3) {
           buffList = new ArrayList<>();
           buffList.add(Integer.valueOf(4914));
         } else if (this._buffType == 4) {
           buffList = new ArrayList<>();
           buffList.add(Integer.valueOf(50007));
         }
         Iterator<Integer> iter = buffList.iterator();
         while (iter.hasNext()) {
           int skillId = ((Integer)iter.next()).intValue();
           Collection<L1PcInstance> pcList = new ArrayList<>();

           if (this._range == null) {
             pcList = L1World.getInstance().getAllPlayers();
           } else if (this._range.equals("!")) {
             pcList = L1World.getInstance().getRecognizePlayer((L1Object)this._user);
           } else {
             L1PcInstance pc = L1World.getInstance().getPlayer(this._range);
             if (pc == null) {
               return;
             }
             pcList.add(pc);
           }
           for (L1PcInstance target : pcList) {
             if (target == null || target.isFishing()) {
               continue;
             }
             if (this._buffType == 2) {
               if (target.hasSkillEffect(22000)) {
                 target.removeSkillEffect(22000);
               }
               if (target.hasSkillEffect(22001)) {
                 target.removeSkillEffect(22001);
               }
               if (target.hasSkillEffect(22002)) {
                 target.removeSkillEffect(22002);
               }
               if (target.hasSkillEffect(22003)) {
                 target.removeSkillEffect(22003);
               }
             } else if (this._buffType == 3) {
               if (target.hasSkillEffect(4914)) {
                 target.removeSkillEffect(4914);
               }
             } else if (this._buffType == 4 &&
               target.hasSkillEffect(50007)) {
               target.removeSkillEffect(50007);
             }

             if (target != null && !target.isDead()) {
               int eachSkillId = skillId;
               if (skillId == 163 && target.getCurrentWeapon() == 20) {
                 eachSkillId = 166;
               } else if (skillId == 148 && target.getCurrentWeapon() == 20) {
                 eachSkillId = 149;
               } else if (skillId == 376 && target.is전사()) {
                 eachSkillId = 383;
               }

               L1Skills skill = SkillsTable.getInstance().getTemplate(eachSkillId);
               target.setBuffnoch(1);
               if (target.noPlayerRobot) {
                 target.broadcastPacket((ServerBasePacket)new S_SkillSound(target.getId(), skill.getCastGfx()));
               } else if (eachSkillId <= 240) {
                 (new L1SkillUse()).handleCommands(target, eachSkillId, target.getId(), target.getX(), target
                     .getY(), null, 0, 4);
               } else {
                 L1BuffUtil.useBuff(target, eachSkillId);
               }
               target.setBuffnoch(0);
             }
           }
           Thread.sleep(2000L);
         }
       } catch (Exception exception) {}
     }


     private void begin() {
       GeneralThreadPool.getInstance().execute(this);
     }
   }

   private static ArrayList<Integer> getBuffList_1() {
     ArrayList<Integer> buffList = new ArrayList<>();
     buffList.add(Integer.valueOf(12));
     buffList.add(Integer.valueOf(21));
     buffList.add(Integer.valueOf(48));
     buffList.add(Integer.valueOf(54));
     buffList.add(Integer.valueOf(168));

     buffList.add(Integer.valueOf(26));
     buffList.add(Integer.valueOf(42));
     buffList.add(Integer.valueOf(149));
     buffList.add(Integer.valueOf(150));
     buffList.add(Integer.valueOf(169));
     buffList.add(Integer.valueOf(219));
     buffList.add(Integer.valueOf(114));
     buffList.add(Integer.valueOf(106));
     buffList.add(Integer.valueOf(105));
     buffList.add(Integer.valueOf(376));
     buffList.add(Integer.valueOf(68));
     buffList.add(Integer.valueOf(4914));
     return buffList;
   }
 }


