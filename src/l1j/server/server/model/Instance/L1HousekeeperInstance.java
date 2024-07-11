 package l1j.server.server.model.Instance;

 import l1j.server.server.datatables.HouseTable;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ChatPacket;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1House;
 import l1j.server.server.templates.L1Npc;






















 public class L1HousekeeperInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;

   public L1HousekeeperInstance(L1Npc template) {
     super(template);
   }


   public void onAction(L1PcInstance pc) {
     L1Attack attack = new L1Attack(pc, this);
     attack.calcHit();
     attack.action();
   }


     public void onTalkAction(L1PcInstance pc) {
// 獲取NPC對象ID
         int objid = getId();
// 獲取NPC對話數據
         L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
// 獲取NPC模板ID
         int npcid = getNpcTemplate().get_npcId();
         String htmlid = null;
         String[] htmldata = null;
         boolean isOwner = false;

// 檢查對話數據是否存在
         if (talking != null) {
             // 獲取玩家的血盟
             L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
             if (clan != null) {
                 // 獲取血盟的房屋ID
                 int houseId = clan.getHouseId();
                 if (houseId != 0) {
                     // 獲取房屋數據
                     L1House house = HouseTable.getInstance().getHouseTable(houseId);
                     // 檢查房屋數據是否存在以及NPC ID是否為130004
                     if (house == null && npcid != 130004) {
                         pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "當前的血盟據點不可用"));
                         return;
                     }
                 }
             }
         }

// 檢查玩家的血盟房屋的守衛ID是否與當前NPC的ID匹配
         if (npcid == house.getKeeperId()) {
             isOwner = true;
         }
     }
 }

// 如果玩家不是擁有者
if (!isOwner) {
        L1House targetHouse = null;
// 遍歷所有血盟房屋
        for (L1House house : HouseTable.getInstance().getHouseTableList()) {
// 如果當前NPC的ID與房屋的守衛ID匹配
        if (npcid == house.getKeeperId()) {
        targetHouse = house;
        break;
        }
        }
// 如果找不到目標房屋且NPC ID不為130004
        if (targetHouse == null && npcid != 130004) {
        pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "當前的血盟據點不可用"));
        return;
        }
        }

         boolean isOccupy = false;
         String clanName = null;
         String leaderName = null;
         for (L1Clan targetClan : L1World.getInstance().getAllClans()) {
           try {
             if (targetHouse.getHouseId() == targetClan.getHouseId()) {
               isOccupy = true;
               clanName = targetClan.getClanName();
               leaderName = targetClan.getLeaderName();
               break;
             }
           } catch (Exception exception) {}
         }

         if (npcid == 130004) {
           htmlid = "total_soloagit";
           String playername = pc.getName();
           htmldata = new String[] { playername };
         } else if (isOccupy) {
           htmlid = "agname";
           htmldata = new String[] { clanName, leaderName, targetHouse.getHouseName() };
         } else {
           htmlid = "agnoname";
           htmldata = new String[] { targetHouse.getHouseName() };
         }
       }

       if (htmlid != null) {
         if (htmldata != null) {
           pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid, htmldata));
         } else {
           pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid));
         }

       } else if (pc.getLawful() < -1000) {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 2));
       } else {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 1));
       }
     }
   }

   public void onFinalAction(L1PcInstance pc, String action) {}

   public void doFinalAction(L1PcInstance pc) {}
 }


