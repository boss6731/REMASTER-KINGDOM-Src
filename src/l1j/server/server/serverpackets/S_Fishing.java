     package l1j.server.server.serverpackets;


     public class S_Fishing
       extends ServerBasePacket
     {
       private static final String S_FISHING = "[S] S_Fishing";

       public S_Fishing() {
         buildPacket();
       }

       public S_Fishing(int objectId, int motionNum, int x, int y) {
         buildPacket(objectId, motionNum, x, y);
       }

       private void buildPacket() {
         writeC(226);
         writeC(55);
         writeD(1979721762);
         writeH(35523);
       }

       private void buildPacket(int objectId, int motionNum, int x, int y) {
         writeC(226);
         writeD(objectId);
         writeC(motionNum);
         writeH(x);
         writeH(y);
         writeD(0);
         writeH(0);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_Fishing";
       }
     }


