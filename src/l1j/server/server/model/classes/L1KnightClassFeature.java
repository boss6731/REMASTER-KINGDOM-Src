 package l1j.server.server.model.classes;

 class L1KnightClassFeature
   extends L1ClassFeature {
   public int getMagicLevel(int playerLevel) {
     return playerLevel / 50;
   }


   public String getClassInitial() {
     return "K";
   }
 }


