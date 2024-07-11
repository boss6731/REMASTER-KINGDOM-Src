     package l1j.server.server.serverpackets;

     public class S_Drawal
       extends ServerBasePacket {
       private static final String _S__37_DRAWAL = "[S] S_Drawal";

       public S_Drawal(int objectId, int count) {
         writeC(12);
         writeD(objectId);
         writeD(count);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_Drawal";
       }
     }


