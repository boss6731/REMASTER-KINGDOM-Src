 package l1j.server.server.serverpackets;


 public class S_TestEffect
   extends ServerBasePacket
 {
   private static final String S_TESTEFFECT = "[S] S_TestEffect";

   public S_TestEffect(int targetId, boolean active) {
     buildPacket(targetId, active);
   }
   private void buildPacket(int targetId, boolean active) {
     writeC(108);
     writeC(194);
     writeD(targetId);
     writeD(13135);
     writeD(active ? 1 : 0);
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_TestEffect";
   }
 }


