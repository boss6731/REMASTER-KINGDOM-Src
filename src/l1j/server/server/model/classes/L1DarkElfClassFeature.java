 package l1j.server.server.model.classes;

 class L1DarkElfClassFeature
   extends L1ClassFeature {
   public int getMagicLevel(int playerLevel) {
     return Math.min(2, playerLevel / 12);
   }


   public String getClassInitial() {
     return "D";
   }
 }


