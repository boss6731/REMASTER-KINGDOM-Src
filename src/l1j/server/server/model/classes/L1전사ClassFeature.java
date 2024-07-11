 package l1j.server.server.model.classes;

 public class L1전사ClassFeature
   extends L1ClassFeature {
   public int getMagicLevel(int playerLevel) {
     return playerLevel / 50;
   }


   public String getClassInitial() {
     return "W";
   }
 }


