 package l1j.server.server.serverpackets;



 public class S_YellowMessage
   extends ServerBasePacket
 {
   private static final String S_YellowMessage = "[S] S_YellowMessage";

   public S_YellowMessage(String s) {
     writeC(186);
     writeC(84);
     writeC(2);
     writeS("\\f=" + s);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_YellowMessage";
   }
 }


