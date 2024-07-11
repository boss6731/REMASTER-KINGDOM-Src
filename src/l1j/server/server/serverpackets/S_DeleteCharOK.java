     package l1j.server.server.serverpackets;


     public class S_DeleteCharOK
       extends ServerBasePacket
     {
       private static final String S_DELETE_CHAR_OK = "[S] S_DeleteCharOK";
       public static final int DELETE_CHAR_NOW = 5;
       public static final int DELETE_CHAR_AFTER_7DAYS = 81;

       public static S_DeleteCharOK deleteRemainSeconds(int remainSeconds) {
         S_DeleteCharOK s = new S_DeleteCharOK();
         s.writeC(81);
         s.writeD(remainSeconds);
         return s;
       }

       public S_DeleteCharOK(int type) {
         writeC(143);
         writeC(type);
       }

       private S_DeleteCharOK() {
         writeC(143);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_DeleteCharOK";
       }
     }


