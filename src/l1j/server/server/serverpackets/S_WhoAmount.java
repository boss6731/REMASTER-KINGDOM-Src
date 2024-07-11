 package l1j.server.server.serverpackets;




 public class S_WhoAmount
   extends ServerBasePacket
 {
   private static final String S_WHO_AMOUNT = "[S] S_WhoAmount";

   public S_WhoAmount(String amount) {
     writeC(102);
     writeH(81);
     writeC(1);
     writeS(amount);

     writeD(0);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_WhoAmount";
   }
 }


