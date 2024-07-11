     package l1j.server.server.serverpackets;





















     public class S_CharTitle
       extends ServerBasePacket
     {
       private static final String _S__0B_CHARTITLE = "[S] S_CharTitle";

       public S_CharTitle(int objid, String title) {
         writeC(36);
         writeD(objid);
         writeS(title);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_CharTitle";
       }
     }


