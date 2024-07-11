 package l1j.server.server.serverpackets;

 public class S_ShockWave
   extends ServerBasePacket
 {
   private static final String S_ShockWave = "[S] S_ShockWave";

   public S_ShockWave() {
     writeC(186);
     writeC(83);
     writeC(2);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(1);
     writeC(29);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_ShockWave";
   }
 }


