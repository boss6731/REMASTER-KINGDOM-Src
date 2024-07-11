     package l1j.server.server.serverpackets;


     public class S_BlackWindow
       extends ServerBasePacket
     {
       private static final String S_BlackWindow = "[S] S_BlackWindow";

       public S_BlackWindow() {
         writeC(186);
         writeC(83);
         writeC(4);
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
         return "[S] S_BlackWindow";
       }
     }


