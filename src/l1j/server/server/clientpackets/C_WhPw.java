 package l1j.server.server.clientpackets;

 import java.util.HashMap;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;





 public class C_WhPw extends ClientBasePacket {
     private static final String C_WhPw = "[C] C_WhPw";

     public C_WhPw(byte[] data, GameClient client) {
         super(data);

         // 獲取當前活動的玩家角色
         L1PcInstance pc = client.getActiveChar();
         if (pc == null)
             return;

         // 獲取遊戲和商店密碼
         int gamepassword = client.getAccount().getGamePassword();
         int shoppassword = client.getAccount().getShopPassword();

         // 讀取操作類型
         int type = readC();

         // 如果操作類型為 0，則發送錯誤訊息並返回
         if (type == 0) {
             pc.sendPackets("\f3倉庫密碼無法使用。(由於內存篡改，稍後將斷開連接) 請重新連接。");
             return;
         }
     }
 }



   public String getType() {
     return "[C] C_WhPw";
   }




   public static final HashMap<Integer, Integer> PASSWORD_CHANGE = new HashMap<>();
   static {
     PASSWORD_CHANGE.put(Integer.valueOf(83), Integer.valueOf(1));
     PASSWORD_CHANGE.put(Integer.valueOf(80), Integer.valueOf(2));
     PASSWORD_CHANGE.put(Integer.valueOf(81), Integer.valueOf(3));
     PASSWORD_CHANGE.put(Integer.valueOf(86), Integer.valueOf(4));
     PASSWORD_CHANGE.put(Integer.valueOf(87), Integer.valueOf(5));
     PASSWORD_CHANGE.put(Integer.valueOf(84), Integer.valueOf(6));
     PASSWORD_CHANGE.put(Integer.valueOf(85), Integer.valueOf(7));
     PASSWORD_CHANGE.put(Integer.valueOf(90), Integer.valueOf(8));
     PASSWORD_CHANGE.put(Integer.valueOf(91), Integer.valueOf(9));
     PASSWORD_CHANGE.put(Integer.valueOf(82), Integer.valueOf(0));
     PASSWORD_CHANGE.put(Integer.valueOf(173), Integer.valueOf(-1));
   }
 }


