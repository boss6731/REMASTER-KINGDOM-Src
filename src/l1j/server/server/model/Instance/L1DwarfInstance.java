 package l1j.server.server.model.Instance;

 import java.util.logging.Logger;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;



 public class L1DwarfInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private static Logger _log = Logger.getLogger(L1DwarfInstance.class
       .getName());




   public L1DwarfInstance(L1Npc template) {
     super(template);
   }


   public void onAction(L1PcInstance pc) {
     L1Attack attack = new L1Attack(pc, this);
     attack.calcHit();
     attack.action();
   }


   public void onTalkAction(L1PcInstance pc) {
     int objid = getId();



     L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
     int npcId = getNpcTemplate().get_npcId();
     String htmlid = null;

     if (talking != null) {
       if (npcId == 60028 &&
         !pc.isElf()) {
         htmlid = "elCE1";
       }


       if (htmlid != null) {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid));
       }
       else if (pc.getLevel() < 5) {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 2));
       } else {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 1));
       }
     }
   }

     public void onFinalAction(L1PcInstance pc, String Action) {
        // 如果動作為 "retrieve"
         if (Action.equalsIgnoreCase("retrieve")) {
             _log.finest("檢索儲存中的物品"); // 記錄檢索儲存物品的日誌
         } else if (Action.equalsIgnoreCase("retrieve-pledge")) { // 如果動作為 "retrieve-pledge"
             _log.finest("檢索血盟儲存中的物品"); // 記錄檢索血盟儲存物品的日誌

        // 檢查玩家是否在血盟中
             if (pc.getClanname().equalsIgnoreCase(" ")) {
                 _log.finest("玩家不在血盟中"); // 記錄玩家不在血盟中的日誌
                 S_ServerMessage talk = new S_ServerMessage(208, Action); // 創建服務器消息
                 pc.sendPackets((ServerBasePacket)talk); // 發送消息給玩家
             } else {
                 _log.finest("玩家在血盟中"); // 記錄玩家在血盟中的日誌
             }
         }
     }


