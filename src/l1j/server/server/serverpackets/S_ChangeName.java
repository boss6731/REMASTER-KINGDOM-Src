     package l1j.server.server.serverpackets;

     public class S_ChangeName
       extends ServerBasePacket
     {
       private static final String S_CHANGE_NAME = "[S] S_ChangeName";

       public S_ChangeName(int objectId, String name) {
         writeC(71);
         writeD(objectId);
         writeS(name);
         writeH(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_ChangeName";
       }
     }


