 package l1j.server.server.model.Instance;

 import java.util.logging.Logger;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;

 public class L1RequestInstance extends L1NpcInstance {
   private static final long serialVersionUID = 1L;
   private static Logger _log = Logger.getLogger(L1RequestInstance.class.getName());

   public L1RequestInstance(L1Npc template) {
     super(template);
   }


   public void onAction(L1PcInstance player) {
     int objid = getId();

     L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());

     if (talking != null) {
       if (player.getLawful() < -1000) {
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 2));
       } else {
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 1));
       }
     } else {
       _log.finest("npc id 沒有任何操作： " + objid);
     }
   }

   public void onFinalAction(L1PcInstance player, String action) {}

   public void doFinalAction(L1PcInstance player) {}
 }


