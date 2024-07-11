 package l1j.server.server.clientpackets;

 import java.util.ArrayList;
 import java.util.Random;
 import l1j.server.Config;
 import l1j.server.EventSystem.EventSystemInfo;
 import l1j.server.EventSystem.EventSystemLoader;
 import l1j.server.IndunSystem.MiniGame.L1Gambling;
 import l1j.server.MJINNSystem.Loader.MJINNHelperLoader;
 import l1j.server.MJTemplate.Lineage2D.MJPoint;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.NpcActionTable;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.npc.L1NpcHtml;
 import l1j.server.server.model.npc.action.L1NpcAction;
 import l1j.server.server.serverpackets.S_AuctionBoard;
 import l1j.server.server.serverpackets.S_Board;
 import l1j.server.server.serverpackets.S_ChatPacket;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.S_NcoinShopSellList;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_Pc_Master_Shop;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_SurvivalCry;
 import l1j.server.server.serverpackets.S_TelePortUi;
 import l1j.server.server.serverpackets.S_아덴상점;
 import l1j.server.server.serverpackets.ServerBasePacket;




 public class C_NPCTalk
   extends ClientBasePacket
 {
   private static final String C_NPC_TALK = "[C] C_NPCTalk";
   private static String[] action = new String[] { "T_talk island", "T_gludio", "T_orc", "T_woodbec", "T_silver knight", "T_kent", "T_giran", "T_heine", "T_werldern", "T_oren", "T_aden", "T_scave", "T_behemoth", "T_silveria", "T_pcbang", "D_talk island", "D_gludio", "D_elven", "D_training", "D_barlog", "D_dragon valley", "D_eva king", "D_ivory tower", "D_yahee", "F_shelob", "F_orc forest", "F_ruin of death", "F_desert", "F_dragon valley", "F_halpas", "F_valakas", "F_jungle", "F_heine", "F_mirror", "F_elmor", "F_lindvior", "F_giant", "F_orenwall", "D_eva kingdom", "T_gludio_lab", "D_atuba", "chaoticD", "T_luun", "F_luun", "D_luun" };







   private static int[] T_talk_island = new int[] { 0, 1175, 1481, 1458, 1884, 1461, 1727, 2123, 2087, 2470, 2429, 2024, 2107, 2470, 0, 140, 1343, 1717, 1905, 1717, 1945, 2328, 2532, 2663, 47, 1447, 1327, 1647, 1799, 1662, 2085, 1928, 2060, 2251, 2247, 2155, 2493, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_gludio = new int[] { 1175, 0, 321, 283, 709, 298, 552, 948, 927, 1310, 1254, 864, 947, 1405, 0, 1315, 169, 557, 731, 542, 785, 1153, 1372, 1503, 1207, 287, 167, 473, 639, 487, 925, 768, 885, 1077, 1087, 981, 1318, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_orc = new int[] { 1481, 321, 0, 567, 845, 421, 687, 1083, 671, 989, 1389, 543, 627, 1085, 0, 1554, 340, 237, 866, 677, 464, 1289, 1051, 1182, 1527, 76, 153, 608, 328, 623, 605, 874, 1021, 1212, 979, 1116, 1453, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_woodbec = new int[] { 1458, 283, 567, 0, 426, 544, 794, 665, 1173, 1556, 971, 1110, 1193, 1651, 0, 1598, 227, 803, 447, 259, 1031, 870, 1618, 1749, 1411, 533, 413, 189, 885, 453, 1171, 1014, 602, 793, 1333, 1147, 1137, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_silver_knight = new int[] { 1884, 709, 845, 426, 0, 423, 629, 447, 1008, 1391, 590, 945, 1029, 1487, 0, 2024, 541, 832, 59, 266, 866, 444, 1453, 1584, 1837, 921, 735, 237, 721, 289, 1007, 849, 224, 471, 1169, 982, 973, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_kent = new int[] { 1461, 298, 421, 544, 423, 0, 266, 662, 629, 1012, 968, 566, 649, 1107, 0, 1601, 317, 409, 445, 645, 487, 867, 1074, 1205, 1505, 497, 312, 440, 341, 201, 627, 470, 599, 791, 789, 695, 1032, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_giran = new int[] { 1727, 552, 687, 794, 629, 266, 0, 396, 379, 762, 702, 380, 399, 857, 0, 1867, 567, 675, 571, 895, 275, 601, 824, 855, 1755, 763, 578, 690, 359, 341, 377, 220, 405, 2251, 539, 429, 766, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_heine = new int[] { 2123, 948, 1083, 665, 447, 662, 396, 0, 561, 945, 306, 776, 582, 1040, 0, 2263, 779, 1071, 388, 713, 671, 205, 1007, 1137, 2076, 1159, 974, 507, 755, 461, 560, 403, 223, 129, 772, 535, 526, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_werldern = new int[] { 2087, 927, 671, 1173, 1008, 629, 379, 561, 0, 383, 718, 364, 111, 479, 0, 2160, 946, 659, 949, 1274, 259, 725, 445, 576, 2133, 747, 759, 1069, 343, 719, 123, 203, 784, 541, 307, 445, 782, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_oren = new int[] { 2470, 1310, 989, 1556, 1391, 1012, 762, 945, 383, 0, 801, 453, 363, 95, 0, 2543, 1329, 753, 133, 1657, 2251, 1109, 91, 193, 2517, 1023, 1143, 1452, 671, 1103, 385, 542, 1167, 920, 223, 409, 693, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_aden = new int[] { 2429, 1254, 1389, 971, 590, 968, 702, 306, 718, 801, 0, 1082, 829, 897, 0, 2569, 1085, 1377, 531, 856, 977, 307, 863, 994, 2382, 1465, 1280, 781, 1061, 767, 841, 515, 369, 177, 579, 392, 383, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_scave = new int[] { 2024, 864, 543, 1110, 945, 566, 380, 776, 364, 453, 1082, 0, 253, 541, 0, 2097, 883, 307, 887, 1211, 105, 971, 508, 639, 2071, 577, 697, 1006, 225, 657, 241, 567, 721, 905, 671, 809, 1146, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_behemoth = new int[] { 2107, 947, 627, 1193, 1029, 649, 399, 582, 111, 363, 829, 253, 0, 458, 0, 2181, 967, 547, 970, 1295, 163, 746, 425, 555, 2154, 660, 780, 1089, 308, 740, 22, 314, 805, 652, 419, 556, 893, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_silveria = new int[] { 2565, 1405, 1085, 1651, 1487, 1107, 857, 1040, 479, 95, 897, 541, 458, 0, 0, 2639, 1425, 848, 1428, 1753, 621, 1204, 37, 129, 2612, 1118, 1238, 1547, 766, 1198, 480, 637, 1263, 1015, 318, 505, 747, 539, 735, 543, 675, 578, 1100, 1500, 2000 };



   private static int[] T_teleport = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };






     public C_NPCTalk(byte[] abyte0, GameClient client) throws Exception {
         super(abyte0);
         int objid = readD(); // 讀取對象 ID
         L1Object obj = L1World.getInstance().findObject(objid); // 根據 ID 查找對象

         if (obj == null)
             return; // 如果對象為空，結束程式

         if (!(obj instanceof L1NpcInstance))
             return; // 如果對象不是 NPC 實例，結束程式

         L1PcInstance pc = client.getActiveChar(); // 獲取當前玩家角色
         if (pc == null || pc.isGhost())
             return; // 如果玩家角色為空或是鬼魂狀態，結束程式

         L1NpcInstance npc = (L1NpcInstance)obj; // 將對象轉換為 NPC 實例
         int npcid = npc.getNpcTemplate().get_npcId(); // 獲取 NPC ID

         EventSystemInfo EventInfo = EventSystemLoader.getInstance().getEventSystemInfo2(npcid); // 獲取事件系統信息
         if (EventInfo != null &&
                 EventInfo.get_npc_id() == npc.getNpcId()) { // 檢查事件信息和 NPC ID 是否匹配
             if (EventInfo.get_console_type().equalsIgnoreCase("true")) {
                 pc.setConsole_type(EventInfo.get_npc_id()); // 設置玩家控制台類型
                 pc.sendPackets((ServerBasePacket)new S_Message_YN(96, 6008, String.format("要進入 %s 嗎？", new Object[] { EventInfo.get_event_name() }))); // 發送確認消息
             }
             else if (EventInfo.get_action_id() != null &&
                     EventInfo.is_event()) {
                 pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, EventInfo.get_action_id())); // 發送 NPC 對話返回封包

                 return; // 結束程式
             }
         }

       if (npcid == Config.ServerAdSetting.TIME_COLLECTION_NPC_IDS[0]) {
           pc.sendPackets((ServerBasePacket)new S_Pc_Master_Shop(pc, obj));
           return; // 如果 NPC 的 ID 是時間收集 NPC 的第一個 ID，則發送 PC 主店的封包，然後結束程式
       }

       if (npcid == 5) {
           if (Config.ServerAdSetting.ArdenTo) {
               pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "目前金幣商店正在維護中。請稍後再使用。"));
               pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "目前金幣商店正在維護中。請稍後再使用。", 1));
               return; // 如果 NPC 的 ID 是 5 且阿登商店正在維護中，則發送相關消息並結束程式
           }

           if (Config.ServerAdSetting.Adentype) {
               client.sendPacket((ServerBasePacket)new S_SurvivalCry(0, pc));
               client.sendPacket((ServerBasePacket)new S_SurvivalCry(1, pc));
               client.sendPacket((ServerBasePacket)new S_SurvivalCry(2, pc));
               pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "[重複消息] N 幣請聯繫 'Metis'。"));
               pc.sendPackets("\aG[重複消息] N 幣請聯繫 Metis。");
               return; // 如果 NPC 的 ID 是 5 且 Adentype 設置為真，則發送相關封包和消息，然後結束程式
           }
       }
       pc.sendPackets((ServerBasePacket)new S_金幣商店(pc, S_金幣商店.Currency.ADENA));
       return;
     }
       if (npcid == 19) {
           pc.sendPackets(String.format(Config.Message.NCOIN_MESSAGE, new Object[] { Integer.valueOf((pc.getAccount()).Ncoin_point) }));

           // 發送消息：玩家的Ncoin點數

           pc.sendPackets((ServerBasePacket)new S_NcoinShopSellList(pc, objid));

           // 發送Ncoin商店銷售列表

           pc.sendPackets((ServerBasePacket)new S_NcoinShopSellList(pc, objid));

           // 再次發送Ncoin商店銷售列表

           return; // 結束程式
       }

       if (npcid != Config.ServerAdSetting.PC_MASTER_SHOP_ID) { // 檢查npcid是否不等於PC主店ID
           if (npcid == 7800228) { // 檢查npcid是否等於7800228
               if (pc.getInventory().checkItem(30001462)) { // 檢查玩家是否擁有指定物品（30001462）
                   pc.sendPackets("您持有魯恩城探險徽章，無法進入。"); // 發送消息：持有魯恩城探險徽章無法進入
                   return; // 結束程式
               }
         ArrayList<L1PcInstance> playmember = new ArrayList<>();
         int _mapnum = 0;
         Random rnd = new Random(System.currentTimeMillis());

         _mapnum = ((L1NpcInstance)obj).get_mapnum();

         for (L1Object obj1 : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
           if (obj1 instanceof L1PcInstance) {
             L1PcInstance pc1 = (L1PcInstance)obj1;
             if (!playmember.contains(pc1)) {
               playmember.add(pc1);
             }
           }
         }


           if (_mapnum == 0) {
               System.out.println("地圖創建錯誤"); // 發送消息：地圖創建錯誤
           }
           else if (playmember.size() < 16) {
               MJPoint pt = MJPoint.newInstance(32615, 32745, 5, (short)_mapnum, 50);
               pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true); // 開始傳送
           } else {
               pc.sendPackets(String.format("該地下城最多只能 %d 人參加。", new Object[] { Integer.valueOf(playmember.size()) })); // 發送消息：該地下城最多只能參加指定人數
           }
       } else {
         if (npcid == 7800244) {
           if (pc.hasSkillEffect(11176)) {
             pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "whale_tel"));
             return;
           }
           pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "whale_telf"));
           return;
         }
         if (npc.is_sub_npc() &&
           npc.getEventSubActid() != null) {
           pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, npc.getEventSubActid()));
           return;
         }
       }
     }
     if (pc.isOnTargetEffect()) {
       long cur = System.currentTimeMillis();
       if (cur - pc.getLastNpcClickMs() > 1000L) {
         pc.setLastNpcClickMs(cur);
         pc.sendPackets((ServerBasePacket)new S_SkillSound(npc.getId(), 14486));
       }
     }

     if (MJINNHelperLoader.getInstance().onTalk(npc, pc)) {
       return;
     }
     int mapvalue = 0;
     mapvalue = C_NPCTalk.action.length;
     switch (npcid) {
       case 9000:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_teleport, mapvalue));
         break;
       case 50015:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_talk_island, mapvalue));
         break;
       case 50024:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_gludio, mapvalue));
         break;
       case 50082:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_orc, mapvalue));
         break;
       case 50054:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_woodbec, mapvalue));
         break;
       case 50056:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_silver_knight, mapvalue));
         break;
       case 50020:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_kent, mapvalue));
         break;
       case 50036:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_giran, mapvalue));
         break;
       case 5069:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_giran, mapvalue));
         break;
       case 5091:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_giran, mapvalue));
         break;
       case 50066:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_heine, mapvalue));
         break;
       case 50039:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_werldern, mapvalue));
         break;
       case 50051:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_oren, mapvalue));
         break;
       case 120836:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_giran, mapvalue));
         break;
       case 50044:
       case 50046:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_aden, mapvalue));
         break;
       case 50079:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_scave, mapvalue));
         break;
       case 3000005:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_behemoth, mapvalue));
         break;
       case 3100005:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_silveria, mapvalue));
         break;
       case 7320051:
         pc.sendPackets((ServerBasePacket)new S_TelePortUi(objid, C_NPCTalk.action, T_orc, mapvalue));
         break;
     }

     L1Gambling gam = new L1Gambling();
     if (obj != null && pc != null) {
       L1NpcAction action = NpcActionTable.getInstance().get(pc, obj);
       if (obj instanceof l1j.server.server.model.Instance.L1AuctionBoardInstance) {
         pc.sendPackets((ServerBasePacket)new S_AuctionBoard(npc));
         return;
       }
       if (action != null) {
         L1NpcHtml html = action.execute("", pc, obj, new byte[0]);
         if (html != null) {
           pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(obj.getId(), html));
         }
         return;
       }
       if (obj instanceof L1NpcInstance &&
         npc.getNpcTemplate().get_npcId() == 400064) {
         gam.dealerTrade(pc);
       }

       if ((npc.getNpcTemplate().get_npcId() == 80006 || npc.getNpcTemplate().get_npcId() == 500002) &&
         obj instanceof L1NpcInstance) {
         L1NpcInstance NEWNPC = (L1NpcInstance)obj;
         pc.sendPackets((ServerBasePacket)new S_Board(NEWNPC));
       }


       obj.onTalkAction(pc);
     }
   }



   public String getType() {
     return "[C] C_NPCTalk";
   }
 }


