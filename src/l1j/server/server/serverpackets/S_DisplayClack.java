     package l1j.server.server.serverpackets;



     public class S_DisplayClack
       extends ServerBasePacket
     {
       private static final String S_DisplayClack = "[S] S_DisplayClack";

       public S_DisplayClack() {
         writeC(186);
         writeC(83);
         writeC(1);
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
         return "[S] S_DisplayClack";
       }
     }


