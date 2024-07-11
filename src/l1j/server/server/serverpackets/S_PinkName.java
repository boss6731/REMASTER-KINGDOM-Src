 package l1j.server.server.serverpackets;


 public class S_PinkName
   extends ServerBasePacket
 {
   private static final String _S__2C_PINKNAME = "[S] S_PinkName";

   public S_PinkName(int objecId, int time) {
     writeC(24);
     writeD(objecId);
     writeC(time);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_PinkName";
   }
 }


