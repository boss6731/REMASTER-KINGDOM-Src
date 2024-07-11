 package l1j.server.server.utils;

 import java.util.HashSet;
 import java.util.Random;
 import l1j.server.MJDungeonTimer.DungeonTimeInformation;
 import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
 import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
 import l1j.server.MJInstanceSystem.MJInstanceSpace;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJRaidSystem.MJRaidSpace;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.Instance.L1SummonInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Location;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.serverpackets.S_ACTION_UI;
 import l1j.server.server.serverpackets.S_CharVisualUpdate;
 import l1j.server.server.serverpackets.S_DollPack;
 import l1j.server.server.serverpackets.S_Liquor;
 import l1j.server.server.serverpackets.S_PinkName;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillBrave;
 import l1j.server.server.serverpackets.S_SkillIconAura;
 import l1j.server.server.serverpackets.S_SkillIconWindShackle;
 import l1j.server.server.serverpackets.S_SummonPack;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.types.Point;



 public class Teleportation
 {
   private static Random _random = new Random(System.nanoTime());




   public static void doTeleportation(L1PcInstance pc) {
     if (pc == null) {
       return;
     }
     doTeleportation(pc, false);
   }

   public static void doTeleportation(L1PcInstance pc, boolean type) {
     if (pc == null) {
       return;
     }

     L1Clan clan = pc.getClan();
     if (clan != null) {
       clan.deleteClanRetrieveUser(pc.getId());
     }
     if (pc.get_teleport()) {
       return;
     }
     if (pc.isDead() &&
       !MJRaidSpace.getInstance().isInInstance(pc) && !MJInstanceSpace.isInInstance(pc)) {
       return;
     }
     int x = pc.get_teleport_x();
     int y = pc.get_teleport_y();
     int mapid = pc.get_teleport_map();

     L1Map map = L1WorldMap.getInstance().getMap((short)mapid);

     if (!map.isInMap(x, y) && !pc.isGm()) {
       x = pc.getX();
       y = pc.getY();
       mapid = pc.getMapId();
     }

     pc.getMap().setPassable((Point)pc.getLocation(), true);
     L1World.getInstance().moveVisibleObject((L1Object)pc, mapid);
     pc.setLocation(x, y, mapid);
     pc.setHeading(pc.getMoveState().getHeading());
     pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
     MJExpAmplifierLoader.getInstance().set(pc);
     pc.getMap().setPassable((Point)pc.getLocation(), false);

     if (pc.getZoneType() == 0) {
       if (pc.getSafetyZone() == true) {
         pc.sendPackets((ServerBasePacket)new S_ACTION_UI(207, false));
         pc.setSafetyZone(false);
       }

     } else if (!pc.getSafetyZone()) {
       pc.sendPackets((ServerBasePacket)new S_ACTION_UI(207, true));
       pc.setSafetyZone(true);
     }


     if (pc.isReserveGhost()) {
       pc.endGhost();
     }

     if (pc.getMapId() != 631) {
       pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
     }

     pc.broadcastRemoveAllKnownObjects();
     pc.removeAllKnownObjects();
     pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
     pc.updateObject();
     pc.sendVisualEffectAtTeleport();
     pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));

     if (!pc.isPassive(MJPassiveID.MEDITATION_BEYOND.toInt())) {
       pc.removeSkillEffect(32);
     }

     pc.setCallClanId(0);
     HashSet<L1PcInstance> subjects = new HashSet<>();
     subjects.add(pc);

     if (pc.isPinkName()) {
       pc.sendPackets((ServerBasePacket)new S_PinkName(pc.getId(), pc.getPinkNameTime()));
     }

     if (pc.hasSkillEffect(22017)) {
       int reminingtime = pc.getSkillEffectTimeSec(22017);
       pc.sendPackets((ServerBasePacket)new S_Liquor(pc.getId(), 8));
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(1065, reminingtime));
       pc.setPearl(1);
     }
     for (L1PcInstance target : L1World.getInstance().getVisiblePlayer((L1Object)pc)) {
       if (target.isPinkName()) {
         pc.sendPackets((ServerBasePacket)new S_PinkName(target.getId(), target.getPinkNameTime()));
       }
     }



     if (pc.getMapId() == 781 || pc.getMapId() == 782) {
       Object[] petList = pc.getPetList().values().toArray();
       L1PetInstance pet = null;
       L1SummonInstance summon = null;
       for (Object petObject : petList) {
         if (petObject instanceof L1PetInstance) {
           pet = (L1PetInstance)petObject;
           pet.dropItem();
           pc.getPetList().remove(Integer.valueOf(pet.getId()));
           pet.deleteMe();
         }
         if (petObject instanceof L1SummonInstance) {
           summon = (L1SummonInstance)petObject;
           for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer((L1Object)summon)) {
             visiblePc.sendPackets((ServerBasePacket)new S_SummonPack(summon, visiblePc, false));
           }
         }
       }
     } else if (!pc.isGhost() && pc.getMap().isTakePets() && pc.getMapId() != 5153 && pc.getMapId() != 5140) {
       for (L1NpcInstance petNpc : pc.getPetList().values()) {
         L1Location loc = pc.getLocation().randomLocation(3, false);
         int nx = loc.getX();
         int ny = loc.getY();
         if (pc.getMapId() == 5125 || pc.getMapId() == 5131 || pc.getMapId() == 5132 || pc.getMapId() == 5133 || pc
           .getMapId() == 5134 || pc.getMapId() == 781 || pc.getMapId() == 782) {
           nx = 32799 + _random.nextInt(5) - 3;
           ny = 32864 + _random.nextInt(5) - 3;
         }
         teleport(petNpc, nx, ny, (short)mapid, pc.getMoveState().getHeading());
         if (petNpc instanceof L1SummonInstance) {
           L1SummonInstance summon = (L1SummonInstance)petNpc;
           pc.sendPackets((ServerBasePacket)new S_SummonPack(summon, pc));
         } else if (petNpc instanceof L1PetInstance) {
           L1PetInstance pet = (L1PetInstance)petNpc;
           SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance((L1NpcInstance)pet);
           pc.broadcastPacket((MJIProtoMessage)noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, true);
         }

         for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer((L1Object)petNpc)) {
           visiblePc.removeKnownObject((L1Object)petNpc);
           subjects.add(visiblePc);
         }
       }


       L1DollInstance doll = pc.getMagicDoll();
       if (doll != null) {
         L1Location loc = pc.getLocation().randomLocation(3, false);
         int nx = loc.getX();
         int ny = loc.getY();

         teleport((L1NpcInstance)doll, nx, ny, (short)mapid, pc.getMoveState().getHeading());
         pc.sendPackets((ServerBasePacket)new S_DollPack(doll, pc));

         for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer((L1Object)doll)) {
           visiblePc.removeKnownObject((L1Object)doll);
           subjects.add(visiblePc);
         }
       }
     } else {
       Object[] petList = pc.getPetList().values().toArray();
       L1PetInstance pet = null;
       L1SummonInstance summon = null;
       for (Object petObject : petList) {
         if (petObject instanceof L1PetInstance) {
           pet = (L1PetInstance)petObject;
           pet.dropItem();
           pc.getPetList().remove(Integer.valueOf(pet.getId()));
           pet.deleteMe();
         }
         if (petObject instanceof L1SummonInstance) {
           summon = (L1SummonInstance)petObject;
           for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer((L1Object)summon)) {
             visiblePc.sendPackets((ServerBasePacket)new S_SummonPack(summon, visiblePc, false));
           }
         }
       }

       L1DollInstance doll = pc.getMagicDoll();
       if (doll != null) {
         doll.deleteDoll();
       }
     }

     for (L1PcInstance updatePc : subjects) {
       updatePc.updateObject();
     }

     pc.set_teleport(false);

     DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_map_id(pc.getMapId());
     if (dtInfo != null) {
       pc.send_dungeon_progress(dtInfo);
     }


     if (pc.getMap().getBaseMapId() != 1936 && pc.getMap().getBaseMapId() != 2600 && pc.getMap().getBaseMapId() != 2699) {
       removeItem(pc);
     }


     if (pc.hasSkillEffect(777777)) {
       pc.sendPackets((ServerBasePacket)new S_SkillIconWindShackle(pc.getId(), pc.getSkillEffectTimeSec(777777)));
     } else if (pc.hasSkillEffect(155)) {
       pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 1, pc.getSkillEffectTimeSec(155)));
       pc.sendPackets((ServerBasePacket)new S_SkillIconAura(154, pc.getSkillEffectTimeSec(155)));
     } else if (pc.hasSkillEffect(179)) {
       pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 1, pc.getSkillEffectTimeSec(179)));
     } else if (pc.hasSkillEffect(178)) {
       pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 9, pc.getSkillEffectTimeSec(178)));
     } else if (pc.hasSkillEffect(177)) {
       pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 10, pc.getSkillEffectTimeSec(177)));
     }
   }


   private static void removeItem(L1PcInstance pc) {
     for (L1ItemInstance item : pc.getInventory().getItems()) {
       if (item.getItemId() == 203003 || item.getItemId() == 810006 || item.getItemId() == 810007)
         pc.getInventory().removeItem(item);
     }
   }

   public static void teleport(L1NpcInstance npc, int x, int y, short map, int head) {
     L1World.getInstance().moveVisibleObject((L1Object)npc, map);
     L1WorldMap.getInstance().getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), true);
     npc.setX(x);
     npc.setY(y);
     npc.setMap(map);
     npc.setHeading(head);
     if (!(npc instanceof L1DollInstance) && !(npc instanceof l1j.server.server.model.Instance.L1TowerInstance))
       L1WorldMap.getInstance().getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), false);
   }
 }


