 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.Random;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;







 public class L1Cube
 {
   private ArrayList<L1NpcInstance>[] CUBE = (ArrayList<L1NpcInstance>[])new ArrayList[4]; private static L1Cube instance; public static L1Cube getInstance() {
     if (instance == null)
       instance = new L1Cube();
     return instance;
   } private static Random _random = new Random(System.nanoTime());
   private L1NpcInstance[] toArray(int index) { return this.CUBE[index].<L1NpcInstance>toArray(new L1NpcInstance[this.CUBE[index].size()]); }
   public void add(int index, L1NpcInstance npc) { if (!this.CUBE[index].contains(npc))
       this.CUBE[index].add(npc);  } private L1Cube() {
     for (int i = 0; i < this.CUBE.length; i++) {
       this.CUBE[i] = new ArrayList<>();
     }




     GeneralThreadPool.getInstance().execute((Runnable)new CUBE1());
     GeneralThreadPool.getInstance().execute((Runnable)new CUBE2());
     GeneralThreadPool.getInstance().execute((Runnable)new CUBE3());
     GeneralThreadPool.getInstance().execute((Runnable)new CUBE4());
   } private void remove(int index, L1NpcInstance npc) {
     if (this.CUBE[index].contains(npc))
       this.CUBE[index].remove(npc);
   }
   class CUBE1 extends RepeatTask { public CUBE1() {
       super(1000L);
     }


     public void execute() {
       try {
         for (L1NpcInstance npc : L1Cube.this.toArray(0)) {

           if (npc == null || npc.Cube()) {
             try {
               npc.setCubePc(null);
               L1Cube.this.remove(0, npc);
             } catch (Exception exception) {}


           }
           else {


             for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)npc, 3)) {
               if (pc == null) {
                 continue;
               }
               if (npc.CubePc().getId() == pc.getId() || (npc.CubePc().getClanid() > 0 && npc
                 .CubePc().getClanid() == pc.getClanid())) {
                 if (!pc.hasSkillEffect(20075)) {
                   pc.getResistance().addFire(30);
                   pc.setSkillEffect(20075, 8000L);
                   pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                 } else {
                   pc.setSkillEffect(20075, 8000L);
                   pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                 }
                 pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 6708)); continue;
               }  if (npc.CubePc().getClanid() > 0 && npc.CubePc().getClanid() != pc.getClanid()) {
                 boolean isNowWar = false;
                 int castleId = L1CastleLocation.getCastleIdByArea((L1Character)pc);
                 if (castleId != 0) {
                   isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
                 }

                 if (isNowWar && !pc.hasSkillEffect(70705) &&
                   !pc.hasSkillEffect(30003) &&
                   !pc.hasSkillEffect(30004) &&
                   !pc.hasSkillEffect(157) && pc
                   .getZoneType() == 0 && L1Cube
                   ._random.nextInt(100) < 20) {
                   pc.receiveDamage((L1Character)npc.CubePc(), 15);
                   pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 6709));
                 }
               }
             }

             npc.setCubeTime(4);
           }
         }
       } catch (Exception e) {
         e.printStackTrace();
       }
     } }


   class CUBE2
     extends RepeatTask {
     public CUBE2() {
       super(1000L);
     }


     public void execute() {
       try {
         for (L1NpcInstance npc : L1Cube.this.toArray(1)) {

           if (npc == null || npc.Cube()) {
             try {
               npc.setCubePc(null);
               L1Cube.this.remove(1, npc);
             } catch (Exception exception) {}


           }
           else {


             for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)npc, 3)) {
               if (pc == null ||
                 npc.CubePc() == null)
                 continue;
               if (npc.CubePc().getId() == pc.getId() || (npc.CubePc().getClanid() > 0 && npc
                 .CubePc().getClanid() == pc.getClanid())) {
                 if (!pc.hasSkillEffect(20076)) {
                   pc.getResistance().addEarth(30);
                   pc.setSkillEffect(20076, 8000L);
                   pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                 } else {
                   pc.setSkillEffect(20076, 8000L);
                   pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                 }
                 pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 6714)); continue;
               }  if (npc.CubePc().getClanid() > 0 && npc.CubePc().getClanid() != pc.getClanid()) {
                 boolean isNowWar = false;
                 int castleId = L1CastleLocation.getCastleIdByArea((L1Character)pc);
                 if (castleId != 0) {
                   isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
                 }

                 if (isNowWar && pc.getZoneType() == 0 && L1Cube
                   ._random.nextInt(100) < 20) {
                   pc.sendPackets((ServerBasePacket)new S_Paralysis(6, true));
                   pc.setSkillEffect(10071, 1000L);
                   pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 6715));
                 }
               }
             }

             npc.setCubeTime(4);
           }
         }
       } catch (Exception e) {
         e.printStackTrace();
       }
     }
   }

   class CUBE3
     extends RepeatTask {
     public CUBE3() {
       super(1000L);
     }


     public void execute() {
       try {
         for (L1NpcInstance npc : L1Cube.this.toArray(2)) {

           if (npc == null || npc.Cube()) {
             try {
               npc.setCubePc(null);
               L1Cube.this.remove(2, npc);
               npc.deleteMe();
             } catch (Exception exception) {}


           }
           else {


             for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)npc, 3)) {
               if (pc == null) {
                 continue;
               }
               if (npc == null || npc.CubePc() == null) {
                 break;
               }

               if (npc.CubePc().getId() == pc.getId() || (npc.CubePc().getClanid() > 0 && npc
                 .CubePc().getClanid() == pc.getClanid())) {
                 if (!pc.hasSkillEffect(20077)) {
                   pc.getResistance().addWind(30);
                   pc.setSkillEffect(20077, 8000L);
                   pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                 } else {
                   pc.setSkillEffect(20077, 8000L);
                   pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                 }
                 pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 6720)); continue;
               }  if (npc.CubePc().getClanid() > 0 && npc.CubePc().getClanid() != pc.getClanid()) {
                 boolean isNowWar = false;
                 int castleId = L1CastleLocation.getCastleIdByArea((L1Character)pc);
                 if (castleId != 0) {
                   isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
                 }

                 if (isNowWar && pc.getZoneType() == 0 && L1Cube
                   ._random.nextInt(100) < 20) {
                   pc.setSkillEffect(20083, 8000L);
                   pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 6721));
                 }
               }
             }

             npc.setCubeTime(4);
           }
         }
       } catch (Exception e) {
         e.printStackTrace();
       }
     }
   }

   class CUBE4
     extends RepeatTask {
     CUBE4() {
       super(1000L);
     }


     public void execute() {
       try {
         for (L1NpcInstance npc : L1Cube.this.toArray(3)) {

           if (npc == null || npc.Cube()) {
             try {
               npc.setCubePc(null);
               L1Cube.this.remove(3, npc);
             } catch (Exception exception) {}





           }
           else if (npc.getZoneType() != 1) {



             for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)npc, 3)) {
               if (pc != null &&
                 pc.getCurrentHp() > 25 && !pc.hasSkillEffect(70705) &&
                 !pc.hasSkillEffect(30003) &&
                 !pc.hasSkillEffect(30004) &&
                 !pc.hasSkillEffect(157)) {

                 pc.receiveDamage((L1Character)npc.CubePc(), 5);
                 pc.setCurrentMp(pc.getCurrentMp() + 1);
                 pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 6727));
               }
             }

             npc.setCubeTime(5);
           }
         }
       } catch (Exception e) {
         e.printStackTrace();
       }
     }
   }
 }


