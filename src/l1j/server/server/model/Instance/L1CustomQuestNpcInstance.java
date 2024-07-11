 package l1j.server.server.model.Instance;

 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.datatables.ServerCustomQuestTable;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.CustomQuest;
 import l1j.server.server.templates.CustomQuestUser;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.templates.eCustomQuestPerformType;
 import l1j.server.server.templates.eCustomQuestType;

 public class L1CustomQuestNpcInstance extends L1NpcInstance {
   private static final long serialVersionUID = 1L;

   public L1CustomQuestNpcInstance(L1Npc template) {
     super(template);
   }


   public void onTalkAction(L1PcInstance player) {
     if (player == null || this == null)
       return;
     L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());

     if (talking != null) {
       player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), talking.getNormalAction()));
     }
   }


   public void onFinalAction(L1PcInstance pc, String action) {

       // 如果當前對象或玩家對象為空，則返回

       if (this == null || pc == null) {

           return;

       }



       // 如果動作以 "reward" 開頭

       if (action.startsWith("reward")) {


           // 提取任務ID

           String quest_id = action.replace("reward_quest_", "");

           int questid = Integer.valueOf(quest_id).intValue();


           // 獲取自定義任務對象

           CustomQuest cq = ServerCustomQuestTable.getInstance().getCustomQuest(questid);

           if (cq != null) {

               CustomQuestUser cqu = pc.getCustomQuestUser(questid); // 獲取玩家的自定義任務用戶對象

               if (cqu != null) {

                   if (cqu.getQuestState() == 3) { // 檢查任務狀態是否已經完成

                       pc.sendPackets("\fW[任務通知] 您已經完成此任務。");

                   } else {

                       if (cq.getQuestPerformType() == eCustomQuestPerformType.KILL_NPC) { // 檢查任務類型是否為擊殺NPC

                           if (cq.getSuccessCount() == cqu.getSuccessCount() && cqu.getQuestState() == 2) { // 檢查是否達到成功次數且任務狀態為進行中


                               L1ItemInstance tem = pc.getInventory().storeItem(cq.getRewardItemId(), cq.getRewardItemCount());
                               // 如果任務條件已滿足，發送獎勵信息和相應效果
                               pc.sendPackets(String.format("\fW[任務獎勵] 您獲得了 %s 的獎勵 %s (%d)。", new Object[] { cq.getQuestName(), tem.getName(), Integer.valueOf(cq.getRewardItemCount()) }));
                               pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "")); // 發送NPC對話返回數據包
                               pc.send_effect(19255, false); // 發送效果
                               cqu.setQuestState(3); // 更新任務狀態為已完成
                           } else {
                               pc.sendPackets("\fW[任務通知] 任務條件未滿足。"); // 發送任務未完成的通知
                           }
                       } else if (cq.getQuestPerformType() == eCustomQuestPerformType.COLLECT_ITEM) { // 如果任務類型是收集物品

                            // 檢查玩家是否有足夠的物品來完成任務
                           if (pc.getInventory().consumeItem(cq.getCollectItemId(), cq.getSuccessCount())) {
                               L1ItemInstance tem = pc.getInventory().storeItem(cq.getRewardItemId(), cq.getRewardItemCount()); // 發放獎勵物品
                               pc.sendPackets(String.format("\fW[任務獎勵] 您獲得了 %s 的獎勵 %s (%d)。", new Object[] { cq.getQuestName(), tem.getName(), Integer.valueOf(cq.getRewardItemCount()) }));
                               pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "")); // 發送NPC對話返回數據包
                               pc.send_effect(19255, false); // 發送效果
                               cqu.setQuestState(3); // 更新任務狀態為已完成
                           } else {
                               pc.sendPackets("\fW[任務通知] 您的物品不足，無法完成任務。"); // 發送物品不足的通知
                           }
                       }

                       // 檢查任務類型是否為可重複
                       if (cq.getQuestType() == eCustomQuestType.REPEAT) {
                           pc.removeCustomQuest(questid); // 移除玩家的自定義任務
                       }
                   }
               }
               else {
                   pc.sendPackets("\fW[任務通知] 找不到進行中的任務。"); // 發送找不到進行中的任務的通知
                   pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "")); // 發送NPC對話返回數據包
               }
           } else {
               pc.sendPackets("\f3[任務通知] 任務尚未準備好。"); // 發送任務尚未準備好的通知
               pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "")); // 發送NPC對話返回數據包
           }
       }

     } else {

// 從動作字符串中提取任務ID
         String quest_id = action.replace("quest_", "");
         int questid = Integer.valueOf(quest_id).intValue();

// 獲取自定義任務對象
         CustomQuest cq = ServerCustomQuestTable.getInstance().getCustomQuest(questid);
         if (cq != null) {
             CustomQuestUser cqu = pc.getCustomQuestUser(questid); // 獲取玩家的自定義任務用戶對象
             if (cqu != null) {
                 if (cqu.getQuestState() == 3) { // 檢查任務狀態是否已經完成
                     pc.sendPackets("\fW[任務通知] 您已經完成此任務。");
                     pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "")); // 發送NPC對話返回數據包
                 } else {
                     pc.sendPackets("\fW[任務通知] 任務正在進行中。"); // 發送任務進行中的通知
                     pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), ""));
                 }
             } else {
                 pc.setCustomQuestNpcObjId(getId()); // 設置自定義任務的NPC對象ID
                 pc.setCustomQuestId(cq.getQuestId()); // 設置自定義任務ID
                 pc.sendPackets((ServerBasePacket)new S_Message_YN(6008, cq.getQuestName() + " 你接受這個任務嗎？")); // 發送接受任務的確認消息
             }
         } else {
             pc.sendPackets("\f3[任務通知] 任務尚未準備好。"); // 發送任務尚未準備好的通知
             pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "")); // 發送NPC對話返回數據包
         }
     }


