 package l1j.server.server.serverpackets;

 import java.util.HashMap;




 public class S_LoginResult
   extends ServerBasePacket
 {
   public static final String S_LOGIN_RESULT = "[S] S_LoginResult";
   public static final int BANNED_REASON_HACK = 95;
   public static final int BANNED_REASON_NOMANNER = 62;
   public static final int BANNED_REASON_COMMERCE = 87;
   public static final HashMap<Integer, Integer> banServerCodes = new HashMap<>(8); static {
     banServerCodes.put(Integer.valueOf(1), Integer.valueOf(95));
     banServerCodes.put(Integer.valueOf(2), Integer.valueOf(62));
     banServerCodes.put(Integer.valueOf(3), Integer.valueOf(87));
   }





   public static final int REASON_LOGIN_OK = 0;




   public static final int REASON_ACCOUNT_IN_USE = 22;




   public static final int REASON_ACCOUNT_ALREADY_EXISTS = 100;




   public static final int REASON_ACCESS_FAILED = 8;




   public static final int REASON_USER_OR_PASS_WRONG = 8;



   public static final int REASON_BUG_WRONG = 57;



   public static final int REASON_WRONG_ACCOUNT = 9;



   public static final int REASON_WRONG_PASSWORD = 10;



   public static final int REASON_BENNED = 62;



   public static final int REASON_MAX_USER = 217;



   public static final int REASON_SUCCESS = 104;




   public S_LoginResult(int reason) {
     buildPacket(reason);
   }

   private void buildPacket(int reason) {
     writeC(81);
     writeC(reason);
     writeD(0);
     writeD(0);
     writeD(0);
   }

   public static S_LoginResult newLoginAccept() {
     S_LoginResult result = new S_LoginResult();
     result.writeC(81);
     result.writeC(51);
     result.writeD(0);
     result.writeD(6912);
     result.writeD(0);
     result.writeD(-1);
     result.writeD(200);
     result.writeD(0);
     result.writeD(0);
     result.writeD(0);
     result.writeD(0);
     return result;
   }


   private S_LoginResult() {}


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_LoginResult";
   }
 }


