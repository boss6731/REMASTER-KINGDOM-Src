     package l1j.server.server.serverpackets;

























     public class S_CharCreateStatus
       extends ServerBasePacket
     {
       private static final String S_CHAR_CREATE_STATUS = "[S] S_CharCreateStatus";
       public static final int REASON_OK = 2;
       public static final int REASON_ALREADY_EXSISTS = 6;
       public static final int REASON_INVALID_NAME = 9;
       public static final int REASON_WRONG_AMOUNT = 21;
       public static final S_CharCreateStatus NAME_FAIL = new S_CharCreateStatus(9);
       public static final S_CharCreateStatus ALREADY_EXSISTS = new S_CharCreateStatus(6);
       public static final S_CharCreateStatus AMOUNT_MAX = new S_CharCreateStatus(21);
       public static final S_CharCreateStatus OK = new S_CharCreateStatus(2);

       public S_CharCreateStatus(int reason) {
         writeC(215);
         writeC(reason);
         writeD(0);
         writeD(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_CharCreateStatus";
       }
     }


