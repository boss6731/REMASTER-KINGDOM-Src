     package l1j.server.server.templates;


     public enum eCustomQuestType
     {
       REPEAT(1),
       ONEDAY(2),
       WEEK(3);
       private int value;

       eCustomQuestType(int val) {
         this.value = val;
       }
       public int toInt() {
         return this.value;
       }
       public boolean equals(eCustomQuestType v) {
         return (this.value == v.value);
       }
       public static eCustomQuestType fromInt(int i) {
         switch (i) {
           case 1:
             return REPEAT;
           case 2:
             return ONEDAY;
           case 3:
             return WEEK;
         }
         return null;
       }

         public static eCustomQuestType fromString(String name) {
        // 根據字符串名稱返回對應的自定義任務類型
             switch (name) {
                 case "重複任務": // 重複任務
                     return REPEAT;
                 case "每日任務": // 每日任務
                     return ONEDAY;
                 case "每週任務": // 每週任務
                     return WEEK;
             }
            // 如果沒有匹配的名稱，返回 null
             return null;
         }
     }


