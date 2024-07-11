 package l1j.server.server.model.classes;

 class L1WizardClassFeature
   extends L1ClassFeature {
   public int getMagicLevel(int playerLevel) {
     return Math.min(10, playerLevel / 4);
   }


   public String getClassInitial() {
     return "M";
   }
 }


