     package l1j.server.server.serverpackets;



     public class S_GreenMessage
       extends ServerBasePacket
     {
       private static final String S_GREENMESSAGE = "[S] S_GreenMessage";

       public S_GreenMessage(String s) {
         writeC(186);
         writeC(84);
         writeC(2);
         writeS(s);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_GreenMessage";
       }
     }


