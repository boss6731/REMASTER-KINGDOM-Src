 package l1j.server.server.model.classes;

 class L1ElfClassFeature
   extends L1ClassFeature
 {
   public int getMagicLevel(int playerLevel) {
     return Math.min(6, playerLevel / 8);
   }


   public String getClassInitial() {
     return "E";
   }
 }


