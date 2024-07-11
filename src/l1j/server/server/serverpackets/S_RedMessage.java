 package l1j.server.server.serverpackets;



 public class S_RedMessage
   extends ServerBasePacket
 {
   private static final String S_RedMessage = "[S] S_RedMessage";

   public S_RedMessage(String s) {
     writeC(186);
     writeC(84);
     writeC(2);
     writeS("\\f3" + s);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_RedMessage";
   }
 }


