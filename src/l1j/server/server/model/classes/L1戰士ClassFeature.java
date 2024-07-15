  package l1j.server.server.model.classes;

 public class L1戰士ClassFeature extends L1ClassFeature {
     public int getMagicLevel(int playerLevel) {
         // 計算魔法等級
         return playerLevel / 50;
     }

     public String getClassInitial() {
         // 獲取類別首字母
         return "W";
     }
 }




