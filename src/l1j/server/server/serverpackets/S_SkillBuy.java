 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_SkillBuy extends ServerBasePacket {
   private static final String _S__1B_WAR = "[S] S_SkillBuy";

   public S_SkillBuy(int o, L1PcInstance pc) {
     int count = Scount(pc);
     int inCount = 0; int k;
     for (k = 0; k < count; k++) {
       if (!pc.isSkillMastery(k + 1)) {
         inCount++;
       }
     }

     try {
       writeC(44);
       writeD(100);
       writeH(inCount);
       for (k = 0; k < count; k++) {
         if (!pc.isSkillMastery(k + 1)) {
           writeD(k);
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   public int Scount(L1PcInstance player) {
     int RC = 0;

     switch (player.getType()) {





       case 0:
         RC = 16;
         break;





       case 1:
         RC = 8;
         break;







       case 2:
         RC = 23;
         break;







       case 3:
         RC = 23;
         break;






       case 4:
         RC = 16;
         break;




       case 7:
         RC = 8;
         break;
     }


     return RC;
   }






   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_SkillBuy";
   }
 }


