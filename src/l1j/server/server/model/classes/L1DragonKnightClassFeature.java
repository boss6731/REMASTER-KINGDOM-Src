 package l1j.server.server.model.classes;

 class L1DragonKnightClassFeature
   extends L1ClassFeature
 {
   public int getMagicLevel(int playerLevel) {
     return Math.min(4, playerLevel / 9);
   }


   public String getClassInitial() {
     return "R";
   }
 }


