 package l1j.server.server.model.classes;

 class L1BlackWizardClassFeature
   extends L1ClassFeature {
   public int getMagicLevel(int playerLevel) {
     return Math.min(10, playerLevel / 6);
   }


   public String getClassInitial() {
     return "I";
   }
 }


