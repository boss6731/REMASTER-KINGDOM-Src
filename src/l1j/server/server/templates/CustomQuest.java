     package l1j.server.server.templates;

     import java.util.ArrayList;
     import java.util.List;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.utils.CommonUtil;

     public class CustomQuest
     {
       private int questId;
       private String questName;
       private List<Integer> questApplyMapIds = new ArrayList<>();
       private eCustomQuestType questType;
       private eCustomQuestPerformType questPerformType;
       private int collectItemId;
       private int collectItemDropProb;
       private int successCount;
       private int rewardItemId;
       private int rewardItemCount;
       private int minLevel;
       private int maxLevel;

       public List<Integer> getQuestApplyMapIds() {
         return this.questApplyMapIds;
       }

       public eCustomQuestType getQuestType() {
         return this.questType;
       }

       public eCustomQuestPerformType getQuestPerformType() {
         return this.questPerformType;
       }

       public int getCollectItemId() {
         return this.collectItemId;
       }

       public int getSuccessCount() {
         return this.successCount;
       }

       public int getRewardItemId() {
         return this.rewardItemId;
       }

       public int getRewardItemCount() {
         return this.rewardItemCount;
       }

       public void setQuestApplyMapIds(List<Integer> questApplyMapIds) {
         this.questApplyMapIds = questApplyMapIds;
       }

       public void setQuestType(eCustomQuestType questType) {
         this.questType = questType;
       }

       public void setQuestPerformType(eCustomQuestPerformType questPerformType) {
         this.questPerformType = questPerformType;
       }

       public void setCollectItemId(int collectItemId) {
         this.collectItemId = collectItemId;
       }

       public void setSuccessCount(int successCount) {
         this.successCount = successCount;
       }

       public void setRewardItemId(int rewardItemId) {
         this.rewardItemId = rewardItemId;
       }

       public void setRewardItemCount(int rewardItemCount) {
         this.rewardItemCount = rewardItemCount;
       }

       public int getQuestId() {
         return this.questId;
       }

       public void setQuestId(int questId) {
         this.questId = questId;
       }

       public int getCollectItemDropProb() {
         return this.collectItemDropProb;
       }

       public void setCollectItemDropProb(int collectItemDropProb) {
         this.collectItemDropProb = collectItemDropProb;
       }

       public String getQuestName() {
         return this.questName;
       }

       public void setQuestName(String questName) {
         this.questName = questName;
       }

       public void result(L1PcInstance pc) {
         CustomQuestUser cqu = pc.getCustomQuestUser(this.questId);
         if (cqu != null &&
           cqu.getQuestState() == 1) {
           int success_count = 0;
           if (getQuestPerformType() == eCustomQuestPerformType.KILL_NPC) {
             if (getSuccessCount() > cqu.getSuccessCount()) {
               cqu.addSuccessCount(1);
               success_count = cqu.getSuccessCount();
             }
           } else if (getQuestPerformType() == eCustomQuestPerformType.COLLECT_ITEM &&
             getCollectItemId() != 0) {
             L1ItemInstance collectItem = pc.getInventory().findItemId(getCollectItemId());
             if (collectItem == null) {
               if (CommonUtil.random(1000000) < getCollectItemDropProb()) {
                 pc.getInventory().storeItem(getCollectItemId(), 1);
                 success_count = 1;
               }

             } else if (getSuccessCount() < collectItem.getCount() &&
               CommonUtil.random(1000000) < getCollectItemDropProb()) {
               pc.getInventory().storeItem(getCollectItemId(), 1);
               success_count = collectItem.getCount() + 1;
             }
           }




             // 发送玩家的任务状态信息
             pc.sendPackets(String.format("\fW[任務現況](%s)(%d)(%d)", new Object[] { getQuestName(), Integer.valueOf(success_count), Integer.valueOf(getSuccessCount()) }));

            // 如果成功次數大於等於目標成功次數
             if (success_count >= getSuccessCount()) {
                 // 发送任务完成通知
                 pc.sendPackets(String.format("\fW[任務完成通知] 已完成 %s 的任務。", new Object[] { getQuestName() }));

                 // 設置任務狀態為完成
                 cqu.setQuestState(2);

                 // 觸發完成任務的特效
                 pc.send_effect(18420, false);
             }


       public int getMinLevel() {
         return this.minLevel;
       }

       public void setMinLevel(int minLevel) {
         this.minLevel = minLevel;
       }

       public int getMaxLevel() {
         return this.maxLevel;
       }

       public void setMaxLevel(int maxLevel) {
         this.maxLevel = maxLevel;
       }
     }


