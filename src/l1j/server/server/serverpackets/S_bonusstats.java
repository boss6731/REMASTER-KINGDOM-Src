     package l1j.server.server.serverpackets;



     public class S_bonusstats
       extends ServerBasePacket
     {
       public S_bonusstats(int i, int j) {
         buildPacket(i, j);
       }

       private void buildPacket(int i, int j) {
         writeC(144);
         writeD(i);
         writeS("RaiseAttr");
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_bonusstats";
       }
     }


