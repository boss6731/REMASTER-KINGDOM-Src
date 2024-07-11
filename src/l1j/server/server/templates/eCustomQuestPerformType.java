 package l1j.server.server.templates;


 public enum eCustomQuestPerformType
 {
   KILL_NPC(1),
   COLLECT_ITEM(2);
   private int value;

   eCustomQuestPerformType(int val) {
     this.value = val;
   }
   public int toInt() {
     return this.value;
   }
   public boolean equals(eCustomQuestPerformType v) {
     return (this.value == v.value);
   }
   public static eCustomQuestPerformType fromInt(int i) {
     switch (i) {
       case 1:
         return KILL_NPC;
       case 2:
         return COLLECT_ITEM;
     }
     return null;
   }

   public static eCustomQuestPerformType fromString(String name) {
     switch (name) {
       case "KILL_NPC":
         return KILL_NPC;
       case "COLLECT_ITEM":
         return COLLECT_ITEM;
     }
     return null;
   }
 }


