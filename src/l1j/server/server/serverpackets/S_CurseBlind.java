     package l1j.server.server.serverpackets;



     public class S_CurseBlind
       extends ServerBasePacket
     {
       private static final String S_CurseBlind = "[S] S_CurseBlind";

       public S_CurseBlind(int type) {
         buildPacket(type);
       }

       private void buildPacket(int type) {
         writeC(114);
         writeH(type);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_CurseBlind";
       }
     }


