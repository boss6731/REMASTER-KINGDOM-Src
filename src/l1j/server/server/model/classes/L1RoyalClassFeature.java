 package l1j.server.server.model.classes;

 class L1RoyalClassFeature
   extends L1ClassFeature {
   public int getMagicLevel(int playerLevel) {
     return Math.min(2, playerLevel / 10);
   }


   public String getClassInitial() {
     return "P";
   }
 }


