 package l1j.server.server.templates;


 public class CustomQuestUser
 {
   private int successCount;
   private int questState;
   private int questId;
   private eCustomQuestType questType;

   public CustomQuestUser(int id, eCustomQuestType type) {
     this.questId = id;
     this.successCount = 0;
     this.questState = 1;
     this.questType = type;
   }

   public int getSuccessCount() {
     return this.successCount;
   }
   public void setSuccessCount(int successCount) {
     this.successCount = successCount;
   }
   public void addSuccessCount(int i) {
     this.successCount += i;
   }
   public int getQuestId() {
     return this.questId;
   }
   public void setQuestId(int questId) {
     this.questId = questId;
   }
   public int getQuestState() {
     return this.questState;
   }
   public void setQuestState(int questState) {
     this.questState = questState;
   }

   public eCustomQuestType getQuestType() {
     return this.questType;
   }

   public void setQuestType(eCustomQuestType questType) {
     this.questType = questType;
   }
 }


