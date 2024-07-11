 package l1j.server.server.templates;

 public enum WareHouseLeaveType {
   NO_WAREHOUSE(0),
   SPECIAL_OK(2),
   SPECIAL_INDIVIDUAL_OK(3),
   OK_WAREHOUSE(7),
   OK_WAREHOUSE_LIMIT(2);
   private int value;

   WareHouseLeaveType(int val) {
     this.value = val;
   }
   public int toInt() {
     return this.value;
   }
   public boolean equals(WareHouseLeaveType v) {
     return (this.value == v.value);
   }
   public static WareHouseLeaveType fromInt(int i) {
     switch (i) {
       case 0:
         return NO_WAREHOUSE;
       case 2:
         return SPECIAL_OK;
       case 3:
         return SPECIAL_INDIVIDUAL_OK;
       case 7:
         return OK_WAREHOUSE;
       case 8:
         return OK_WAREHOUSE_LIMIT;
     }
     return null;
   }


   public static WareHouseLeaveType fromString(String i) {
     switch (i) {
       case "NO_WAREHOUSE(0)":
         return NO_WAREHOUSE;
       case "SPECIAL_OK(2)":
         return SPECIAL_OK;
       case "SPECIAL_INDIVIDUAL_OK(3)":
         return SPECIAL_INDIVIDUAL_OK;
       case "OK_WAREHOUSE(7)":
         return OK_WAREHOUSE;
       case "OK_WAREHOUSE_LIMIT(8)":
         return OK_WAREHOUSE_LIMIT;
     }
     return null;
   }
 }


