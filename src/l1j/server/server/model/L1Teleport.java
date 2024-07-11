 package l1j.server.server.model;

 import java.util.HashSet;
 import l1j.server.Config;
 import l1j.server.MJDungeonTimer.DungeonTimeInformation;
 import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
 import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
 import l1j.server.MJInstanceSystem.MJInstanceSpace;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJRaidSystem.MJRaidSpace;
 import l1j.server.MJTemplate.Chain.Action.MJTeleportChain;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHARATER_FOLLOW_EFFECT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SCENE_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.QueenAntSystem.QueenAntController;
 import l1j.server.server.Controller.FishingTimeController;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.Instance.L1SummonInstance;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.serverpackets.S_ACTION_UI;
 import l1j.server.server.serverpackets.S_CharVisualUpdate;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_DollPack;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_PinkName;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.S_SkillBrave;
 import l1j.server.server.serverpackets.S_SkillIconAura;
 import l1j.server.server.serverpackets.S_SkillIconWindShackle;
 import l1j.server.server.serverpackets.S_SummonPack;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.CommonUtil;






 public class L1Teleport
 {
     private static L1Teleport instance;
     private boolean _isTeleport;

     public static L1Teleport getInstance() {
         if (instance == null) {
             instance = new L1Teleport();
         }
         return instance;
     }

     public boolean isTeleport() {
         return this._isTeleport;
     }
     public void setTeleport(boolean flag) {
         this._isTeleport = flag;
     }






























     public void doTeleport(L1PcInstance pc, int x, int y, int mapid) {
         int old_mapid = pc.getMapId();
         L1Map map = L1WorldMap.getInstance().getMap((short)mapid);


         if (MJTeleportChain.getInstance().is_teleport(pc, x, y, mapid)) {
             return;
         }
         if (!map.isInMap(x, y) && !pc.isGm()) {
             x = pc.getX();
             y = pc.getY();
             mapid = pc.getMapId();
         }

         pc.getMap().setPassable(pc.getLocation(), true);
         L1World.getInstance().moveVisibleObject((L1Object)pc, mapid);
         pc.setLocation(x, y, mapid);
         pc.setHeading(pc.getHeading());

         pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
         MJExpAmplifierLoader.getInstance().set(pc);
         pc.getMap().setPassable(pc.getLocation(), false);

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

         if (pc.getMapId() == 5490) {
             pc.setFishingTime(0L);
             pc.setFishingReady(false);
             pc.setFishing(false);
             pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));
             pc.broadcastPacket((ServerBasePacket)new S_CharVisualUpdate(pc));
             FishingTimeController.getInstance().removeMember(pc);
         }
         pc.broadcastRemoveAllKnownObjects();
         pc.removeAllKnownObjects();
         pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
         pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
         pc.updateObject();

         if (pc.getMap().isCrackIntheTower()) {
             pc.sendPackets(SC_SCENE_NOTI.make_stream(pc, true), true);
         }



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


         L1Party party = pc.getParty();
         for (L1PcInstance target : L1World.getInstance().getVisiblePlayer((L1Object)pc)) {
             if (target.isPinkName()) {
                 pc.sendPackets((ServerBasePacket)new S_PinkName(target.getId(), target.getPinkNameTime()));
             }

             if (party != null && party.isMember(target)) {
                 party.refreshPartyMemberStatus(target);
             }
         }

         MJCastleWarBusiness.move(pc);

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
                 if (pc.getMapId() == 5125 || pc.getMapId() == 5131 || pc.getMapId() == 5132 || pc.getMapId() == 5133 || pc.getMapId() == 5134 || pc.getMapId() == 781 || pc.getMapId() == 782) {
                     nx = 32799 + CommonUtil.random(5) - 3;
                     ny = 32864 + CommonUtil.random(5) - 3;
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
         if (pc.isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())) {
             pc.getMap().setPassable(pc.getLocation(), true);
         }
         get_teleport_check(pc, old_mapid, pc.getMapId());

         if (pc.getFindMerchantId() != 0) {
             pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(pc.getFindMerchantId(), "usershop"), true);
             pc.setFindMerchantId(0);
         }

         MJTeleportChain.getInstance().teleported(pc, x, y, mapid, old_mapid);

         if (pc.isDead())
             pc.sendPackets((ServerBasePacket)new S_DoActionGFX(pc.getId(), 8));
     }

     public void doTeleportationForGM(L1PcInstance pc) {
         if (pc == null) {
             return;
         }

         int old_mapid = pc.getMapId();
         int x = pc.get_teleport_x();
         int y = pc.get_teleport_y();
         int mapid = pc.get_teleport_map();

         if (MJTeleportChain.getInstance().is_teleport(pc, x, y, mapid)) {
             return;
         }
         L1Map map = L1WorldMap.getInstance().getMap((short)mapid);

         if (!map.isInMap(x, y) && !pc.isGm()) {
             x = pc.getX();
             y = pc.getY();
             mapid = pc.getMapId();
         }

         pc.getMap().setPassable(pc.getLocation(), true);
         L1World.getInstance().moveVisibleObject((L1Object)pc, mapid);
         pc.setLocation(x, y, mapid);

         pc.setHeading(pc.getHeading());

         pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
         MJExpAmplifierLoader.getInstance().set(pc);
         pc.getMap().setPassable(pc.getLocation(), false);

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

         if (pc.getMapId() == 5490) {
             pc.setFishingTime(0L);
             pc.setFishingReady(false);
             pc.setFishing(false);
             pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));
             pc.broadcastPacket((ServerBasePacket)new S_CharVisualUpdate(pc));
             FishingTimeController.getInstance().removeMember(pc);
         }
         pc.broadcastRemoveAllKnownObjects();
         pc.removeAllKnownObjects();
         pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
         pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
         pc.updateObject();

         if (pc.getMap().isCrackIntheTower()) {
             pc.sendPackets(SC_SCENE_NOTI.make_stream(pc, true), true);
         }
         if (pc.isInParty()) {
             pc.getParty().onMoveMember(pc);
         }
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


         L1Party party = pc.getParty();
         for (L1PcInstance target : L1World.getInstance().getVisiblePlayer((L1Object)pc)) {
             if (target.isPinkName()) {
                 pc.sendPackets((ServerBasePacket)new S_PinkName(target.getId(), target.getPinkNameTime()));
             }

             if (party != null && party.isMember(target)) {
                 party.refreshPartyMemberStatus(target);
             }
         }

         MJCastleWarBusiness.move(pc);

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
                 if (pc.getMapId() == 5125 || pc.getMapId() == 5131 || pc.getMapId() == 5132 || pc.getMapId() == 5133 || pc.getMapId() == 5134 || pc.getMapId() == 781 || pc.getMapId() == 782) {
                     nx = 32799 + CommonUtil.random(5) - 3;
                     ny = 32864 + CommonUtil.random(5) - 3;
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

         get_teleport_check(pc, old_mapid, pc.getMapId());

         MJTeleportChain.getInstance().teleported(pc, x, y, mapid, old_mapid);

         if (pc.isDead()) {
             pc.sendPackets((ServerBasePacket)new S_DoActionGFX(pc.getId(), 8));
         }
     }

     public void doTeleportation(L1PcInstance pc) {
         if (pc == null) {
             return;
         }
         if (pc.isMassTel()) {
             pc.set_MassTel(false);
         }
         L1Clan clan = pc.getClan();
         if (clan != null) {
             clan.deleteClanRetrieveUser(pc.getId());
         }


         if (pc.isDead() &&
                 !MJRaidSpace.getInstance().isInInstance(pc) && !MJInstanceSpace.isInInstance(pc)) {
             return;
         }


         int old_mapid = pc.getMapId();
         int x = pc.get_teleport_x();
         int y = pc.get_teleport_y();
         int mapid = pc.get_teleport_map();

         if (MJTeleportChain.getInstance().is_teleport(pc, x, y, mapid)) {
             return;
         }
         L1Map map = L1WorldMap.getInstance().getMap((short)mapid);

         if (!map.isInMap(x, y) && !pc.isGm()) {
             x = pc.getX();
             y = pc.getY();
             mapid = pc.getMapId();
         }
         if (pc.isCastleEffect()) {
             pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send((L1Character)pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false));
         }
         for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer((L1Object)pc)) {
             visiblePc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)pc));
         }



         pc.getMap().setPassable(pc.getLocation(), true);
         L1World.getInstance().moveVisibleObject((L1Object)pc, mapid);

         pc.setLocation(x, y, mapid);

         pc.setHeading(pc.getHeading());

         pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
         MJExpAmplifierLoader.getInstance().set(pc);
         pc.getMap().setPassable(pc.getLocation(), false);

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

         if (pc.getMapId() == 5490) {
             pc.setFishingTime(0L);
             pc.setFishingReady(false);
             pc.setFishing(false);
             pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));
             pc.broadcastPacket((ServerBasePacket)new S_CharVisualUpdate(pc));
             FishingTimeController.getInstance().removeMember(pc);
         }



         pc.broadcastRemoveAllKnownObjects();
         pc.removeAllKnownObjects();
         pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
         pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
         pc.updateObject();
         if (pc.isCastleEffect()) {
             pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send((L1Character)pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, true));
         }

         if (pc.getMap().isCrackIntheTower()) {
             pc.sendPackets(SC_SCENE_NOTI.make_stream(pc, true), true);
         }
         if (pc.isInParty()) {
             pc.getParty().onMoveMember(pc);
         }
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


         L1Party party = pc.getParty();
         for (L1PcInstance target : L1World.getInstance().getVisiblePlayer((L1Object)pc)) {
             if (target.isPinkName()) {
                 pc.sendPackets((ServerBasePacket)new S_PinkName(target.getId(), target.getPinkNameTime()));
             }

             if (party != null && party.isMember(target)) {
                 party.refreshPartyMemberStatus(target);
             }
         }


         MJCastleWarBusiness.move(pc);

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
                 if (pc.getMapId() == 5125 || pc.getMapId() == 5131 || pc.getMapId() == 5132 || pc.getMapId() == 5133 || pc.getMapId() == 5134 || pc.getMapId() == 781 || pc.getMapId() == 782) {
                     nx = 32799 + CommonUtil.random(5) - 3;
                     ny = 32864 + CommonUtil.random(5) - 3;
                 }
                 for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer((L1Object)petNpc)) {
                     visiblePc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)petNpc));

                     subjects.add(visiblePc);
                 }
                 teleport(petNpc, nx, ny, (short)mapid, pc.getMoveState().getHeading());
                 if (petNpc instanceof L1SummonInstance) {
                     L1SummonInstance summon = (L1SummonInstance)petNpc;
                     pc.sendPackets((ServerBasePacket)new S_SummonPack(summon, pc)); continue;
                 }  if (petNpc instanceof L1PetInstance) {
                     L1PetInstance pet = (L1PetInstance)petNpc;
                     SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance((L1NpcInstance)pet);
                     pc.broadcastPacket((MJIProtoMessage)noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, true);
                 }
             }








             L1DollInstance doll = pc.getMagicDoll();
             if (doll != null) {
                 L1Location loc = pc.getLocation().randomLocation(3, false);
                 int nx = loc.getX();
                 int ny = loc.getY();
                 for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer((L1Object)doll)) {

                     visiblePc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)doll));

                     subjects.add(visiblePc);
                 }
                 teleport((L1NpcInstance)doll, nx, ny, (short)mapid, pc.getMoveState().getHeading());
                 pc.sendPackets((ServerBasePacket)new S_DollPack(doll, pc));


             }



         }
         else {


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

         get_teleport_check(pc, old_mapid, pc.getMapId());
         MJTeleportChain.getInstance().teleported(pc, x, y, mapid, old_mapid);

         if (pc.getFindMerchantId() != 0) {
             pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(pc.getFindMerchantId(), "usershop"), true);
             pc.setFindMerchantId(0);
         }

         if (pc.getMapId() == 15871 || pc.getMapId() == 15881 || pc.getMapId() == 15891) {
             pc.sendPackets((ServerBasePacket)new S_PacketBox(153, QueenAntController.Time));
         }

         if (pc.isDead()) {
             pc.sendPackets((ServerBasePacket)new S_DoActionGFX(pc.getId(), 8));
         }

         pc.set_teleport(false);
     }

     private void removeItem(L1PcInstance pc) {
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

     public void get_teleport_check(L1PcInstance pc, int mapid, int new_mapid) {
         if (pc.isInFantasy && (
                 mapid < 1936 || mapid > 2035)) {
             pc.getInventory().consumeItem(810006);
             pc.getInventory().consumeItem(810007);
             pc.isInFantasy = false;
         }

         if (pc.isInValakas &&
                 pc.getMap().getBaseMapId() != 2600 && pc.getMap().getBaseMapId() != 2699) {
             pc.getInventory().consumeItem(203003);
             pc.isInValakas = false;
         }

         if (pc.isInValakasBoss && mapid != 2600) {
             pc.isInValakasBoss = false;
         }
         if ((mapid < 2101 || mapid > 2151) && (mapid < 2151 || mapid > 2201) && (mapid < 12152 || mapid > 12252))
         {

             for (L1ItemInstance item : pc.getInventory().getItems()) {
                 if (item != null && (
                         item.getItemId() == 30055 || item.getItemId() == 30056)) {
                     pc.getInventory().removeItem(item);
                 }
             }
         }


         if (mapid != new_mapid) {
             if (pc.noPlayerCK || pc.noPlayerck2 || pc.getAI() != null) {
                 return;
             }

             DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_map_id(new_mapid);
             if (dtInfo != null) {
                 pc.send_dungeon_progress(dtInfo);
             }
         }
     }

     public L1Location 소환텔레포트(L1Character target, int distance) {
         L1Location loc = new L1Location();
         int locX = target.getX();
         int locY = target.getY();
         int heading = target.getHeading();
         loc.setMap(target.getMapId());
         switch (heading) { case 1:
             locX += distance; locY -= distance; break;
             case 2: locX += distance; break;
             case 3: locX += distance; locY += distance; break;
             case 4: locY += distance; break;
             case 5: locX -= distance; locY += distance; break;
             case 6: locX -= distance; break;
             case 7: locX -= distance; locY -= distance; break;
             case 0: locY -= distance; break; }

         loc.setX(locX); loc.setY(locY);
         return loc;
     }

     public void teleportToTargetFront(L1Character cha, L1Character target, int distance, boolean check) {
         int locX = target.getX();
         int locY = target.getY();
         int heading = target.getHeading();
         L1Map map = target.getMap();
         short mapId = target.getMapId();

         switch (heading) { case 1:
             locX += distance; locY -= distance; break;
             case 2: locX += distance; break;
             case 3: locX += distance; locY += distance; break;
             case 4: locY += distance; break;
             case 5: locX -= distance; locY += distance; break;
             case 6: locX -= distance; break;
             case 7: locX -= distance; locY -= distance; break;
             case 0: locY -= distance;
             break; }



         if (map.isPassable(locX, locY) &&
                 cha instanceof L1PcInstance) {
             L1PcInstance pc = (L1PcInstance)cha;
             pc.start_teleport(locX, locY, mapId, cha.getMoveState().getHeading(), 18339, true, check);
         }
     }


     public void randomTeleport(L1PcInstance pc, boolean effect) {
         L1Location newLocation = pc.getLocation().randomLocation(200, true);
         int newX = newLocation.getX();
         int newY = newLocation.getY();
         int newHeading = pc.getHeading();
         short mapId = (short)newLocation.getMapId();
         pc.start_teleport(newX, newY, mapId, newHeading, 18339, effect, false);
     }

     public void four_gear(L1PcInstance pc, int type) {}
 }


