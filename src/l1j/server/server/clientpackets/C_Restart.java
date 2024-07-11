 package l1j.server.server.clientpackets;

 import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
 import l1j.server.MJCombatSystem.MJCombatObserver;
 import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
 import l1j.server.MJInstanceSystem.MJInstanceEnums;
 import l1j.server.MJInstanceSystem.MJInstanceSpace;
 import l1j.server.MJRaidSystem.MJRaidSpace;
 import l1j.server.MJTemplate.Chain.Action.MJDeadRestartChain;
 import l1j.server.MJTemplate.MJEPcStatus;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_DEAD_RESTART_ACK;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.model.Getback;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_CharVisualUpdate;
 import l1j.server.server.serverpackets.S_IvenBuffIcon;
 import l1j.server.server.serverpackets.S_NewCreateItem;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.S_Weather;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Skills;




 public class C_Restart
   extends ClientBasePacket
 {
   private static final String C_RESTART = "[C] C_Restart";

   public C_Restart(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     L1PcInstance pc = clientthread.getActiveChar();



     if (pc == null) {
       return;
     }


     if (pc.is_combat_field()) {

       MJCombatObserver observer = MJCombatLoadManager.getInstance().get_current_observer(pc.get_current_combat_id());
       if (observer != null) {
         observer.remove(pc);
       }
     }

     MJRaidSpace.getInstance().getBackPc(pc);



     if (MJInstanceSpace.isInInstance(pc) && pc.getInstStatus() != MJInstanceEnums.InstStatus.INST_USERSTATUS_NONE) {
       return;
     }


     pc.setCurrentSprite(pc.getClassId());
     if (!pc.isDead()) {
       return;
     }
     if (pc.isGhost()) {
       pc.endGhost();
     }


     processRestart(pc);



     for (L1ItemInstance item : pc.getInventory().getItems()) {
       if (item.getItemId() == 203003 || item.getItemId() == 810006 || item.getItemId() == 810007 || item
         .getItemId() == 30055 || item.getItemId() == 30056) {
         pc.getInventory().removeItem(item);
       }
     }
   }


   public String getType() {
     return "[C] C_Restart";
   }


   public static void processRestart(L1PcInstance pc) {
     synchronized (pc) {
       try {
         int[] loc = MJDeadRestartChain.getInstance().get_death_location(pc);
         if (loc == null) {
           loc = Getback.GetBack_Restart(pc);
           if (pc.getHellTime() > 0) {
             loc = new int[3];
             loc[0] = 32701;
             loc[1] = 32777;
             loc[2] = 666;
           } else {
             loc = Getback.GetBack_Location(pc, true);
           }
         }

         pc.removeAllKnownObjects();
         pc.broadcastPacket((ServerBasePacket)new S_RemoveObject((L1Object)pc));

         pc.setCurrentHp(pc.getLevel());
         pc.set_food(39);
         pc.set_is_non_action(false);
         pc.setDead(false);
         pc.setStatus(0);
         pc.updateObject();
         reloadNBuff(pc);



         pc.set_instance_status(MJEPcStatus.WORLD);
         pc.set_mark_status(0);
         pc.set_current_combat_id(0);
         pc.set_current_combat_team_id(-1);

         L1World.getInstance().moveVisibleObject((L1Object)pc, loc[2]);
         pc.setX(loc[0]);
         pc.setY(loc[1]);
         pc.setMap((short)loc[2]);
         pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
         MJExpAmplifierLoader.getInstance().set(pc);
         pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
         pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));


         pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));


         pc.sendPackets((ServerBasePacket)new S_Weather(L1World.getInstance().getWeather()));







         if (pc.hasSkillEffect(340)) {
           pc.sendPackets((ServerBasePacket)new S_PacketBox(180, 1, 479));
         }
       }
       catch (Exception e) {
         e.printStackTrace();
       }
     }
     SC_DEAD_RESTART_ACK.send_restart(pc, true);
   }




   private static void reloadNBuff(L1PcInstance pc) {
     int[] skills = { 4075, 4076, 4077, 4078, 4079, 4080, 4081, 4082, 4083, 4084, 4085, 4086, 4087, 4088, 4089, 4090, 4091, 4092, 4093, 4094, 4095 }; int arrayOfInt1[], i;
     byte b;
     for (arrayOfInt1 = skills, i = arrayOfInt1.length, b = 0; b < i; ) { Integer number = Integer.valueOf(arrayOfInt1[b]);
       if (pc.hasSkillEffect(number.intValue())) {
         int time = pc.getSkillEffectTimeSec(number.intValue());
         L1Skills _skill = SkillsTable.getInstance().getTemplate(number.intValue());
         pc.sendPackets((ServerBasePacket)new S_IvenBuffIcon(number.intValue(), true, _skill.getSysmsgIdHappen(), time));
       }
       b++; }

     int aftertamtime = (int)pc.TamTime() / 1000;
     int aftertamcount = pc.tamcount();

     if (aftertamcount == 1) {
       pc.sendPackets((ServerBasePacket)new S_NewCreateItem(110, aftertamtime, 8265, 4181));
     } else if (aftertamcount == 2) {
       pc.sendPackets((ServerBasePacket)new S_NewCreateItem(110, aftertamtime, 8266, 4182));
     } else if (aftertamcount == 3) {
       pc.sendPackets((ServerBasePacket)new S_NewCreateItem(110, aftertamtime, 8267, 4183));
     } else if (aftertamcount == 4) {
       pc.sendPackets((ServerBasePacket)new S_NewCreateItem(110, aftertamtime, 8268, 5046));
     } else if (aftertamcount == 5) {
       pc.sendPackets((ServerBasePacket)new S_NewCreateItem(110, aftertamtime, 8269, 5047));
     }
   }
 }


