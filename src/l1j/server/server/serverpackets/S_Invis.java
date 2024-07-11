     package l1j.server.server.serverpackets;

     public class S_Invis
       extends ServerBasePacket {
       private static final String S_INVIS = "[S] S_Invis";

       public S_Invis(int objid, int type) {
         buildPacket(objid, type);
       }

       private void buildPacket(int objid, int type) {
         writeC(160);
         writeD(objid);
         writeC(type);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_Invis";
       }
     }


