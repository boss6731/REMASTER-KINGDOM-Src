 package l1j.server.server.clientpackets;

 import java.util.logging.Logger;
 import l1j.server.IndunEx.RoomInfo.MJIndunRoomController;
 import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
 import l1j.server.MJCombatSystem.MJCombatObserver;
 import l1j.server.MJInstanceSystem.MJInstanceEnums;
 import l1j.server.MJInstanceSystem.MJInstanceSpace;
 import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
 import l1j.server.MJRaidSystem.MJRaidSpace;
 import l1j.server.MJTemplate.Chain.Action.MJRestartChain;
 import l1j.server.MJTemplate.Lineage2D.MJPoint;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FORCE_FINISH_PLAY_SUPPORT_NOTI;
 import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_CharAmount;
 import l1j.server.server.serverpackets.S_Unknown2;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_NewCharSelect
   extends ClientBasePacket {
   private static final String C_NEW_CHAR_SELECT = "[C] C_NewCharSelect";
   private static Logger _log = Logger.getLogger(C_NewCharSelect.class.getName());


   public C_NewCharSelect(byte[] decrypt, GameClient client) throws Exception {
     super(decrypt);
     if (client.getActiveChar() != null) {
       L1PcInstance pc = client.getActiveChar();
       if (pc == null) {
         return;
       }

       if (pc.hasSkillEffect(230) || pc.hasSkillEffect(243) || pc.hasSkillEffect(5027) || pc.hasSkillEffect(5002)) {
         return;
       }


       if (pc.is_combat_field()) {
         MJCombatObserver observer = MJCombatLoadManager.getInstance().get_current_observer(pc.get_current_combat_id());
         if (observer != null) {
           observer.remove(pc);
         }
       }
       MJPoint pt = MJPoint.newInstance(33437, 32813, 10, (short)4, 50);
       if (pc.getMapId() >= 732 && pc.getMapId() <= 776) {
         pc.setX(pt.x);
         pc.setY(pt.y);
         pc.setMap(pt.mapId);
       }

       MJCopyMapObservable.getInstance().resetPosition(pc);
       MJRaidSpace.getInstance().getBackPc(pc);


       if (MJInstanceSpace.isInInstance(pc))
       {



         if (pc.getInstStatus() != MJInstanceEnums.InstStatus.INST_USERSTATUS_NONE)
           return;
       }
       restartProcess(pc);
     }
   }


   public static void restartProcess(L1PcInstance pc) {
     pc.isWorld = false;

     L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
     if (clan != null) {
       pc.setOnlineStatus(0);
       clan.updateClanMemberOnline(pc);
     }
     _log.fine("斷開連線： " + pc.getName());
     GameClient client = pc.getNetConnection();
     if (client == null) {
       return;
     }
     client.latestRestartMillis(System.currentTimeMillis());

     synchronized (pc) {
       try {
         if (pc.get_is_client_auto()) {
           pc.do_finish_client_auto(SC_FORCE_FINISH_PLAY_SUPPORT_NOTI.eReason.USER_DEAD);
         }

         MJIndunRoomController.getInstance().end_user_room(pc, -1);





         pc.logout();
         client.setStatus2(MJClientStatus.CLNT_STS_AUTHLOGIN);
         client.latestCharacterInstance(pc);
         client.setActiveChar(null);
         client.sendPacket((ServerBasePacket)new S_Unknown2(1));
         if (client.getAccount().is_changed_slot()) {
           int amountOfChars = client.getAccount().countCharacters();
           int slot = client.getAccount().getCharSlot();
           client.sendPacket((ServerBasePacket)new S_CharAmount(amountOfChars, slot));

           C_CommonClick.sendCharPacks(client);
         }
       } catch (Exception e) {
         e.printStackTrace();
       }
     }

     MJRestartChain.getInstance().on_restarted(pc);
   }



   public String getType() {
     return "[C] C_NewCharSelect";
   }
 }


