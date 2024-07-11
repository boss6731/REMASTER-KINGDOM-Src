     package l1j.server.server.serverpackets;

     import java.io.IOException;
     import l1j.server.MJTemplate.MJString;





     public class S_EnchantMessage extends ServerBasePacket {

         // 私有構造函數，初始化訊息大小並寫入初始字節
         private S_EnchantMessage(int size) {
             super(size);
             writeC(102); // 寫入操作碼 102
         }

         // 私有靜態方法，創建新的強化訊息
         private static S_EnchantMessage newMessage(int stringId, int enchant, int iconId, String message) {
            // 創建一個新的 S_EnchantMessage 對象，大小為 10 加上消息的長度
             S_EnchantMessage m = new S_EnchantMessage(10 + message.length());
             m.writeH(stringId); // 寫入字符串 ID
             m.writeC(1); // 寫入常量 1
             m.writeC(43); // 寫入常量 43
            // 寫入強化等級和訊息的組合字符串
             m.writeS(MJString.concat(new String[] { String.valueOf(enchant), " ", message }));
             m.writeH(iconId); // 寫入圖標 ID
             m.writeH(0); // 寫入常量 0
             return m; // 返回創建的訊息對象
         }

         // 創建新的藍色強化訊息
         public static S_EnchantMessage newBlueMessage(int enchant, int iconId, String message) {
             return newMessage(4444, enchant, iconId, message + " 玩家"); // 調用 newMessage 方法，字符串 ID 為 4444
         }

         // 創建新的銀色強化訊息
         public static S_EnchantMessage newSilverMessage(int enchant, int iconId, String message) {
             return newMessage(4445, enchant, iconId, message + " 玩家"); // 調用 newMessage 方法，字符串 ID 為 4445
         }

         // 創建新的玩偶強化訊息
         public static S_EnchantMessage newDollMessage(int enchant, int iconId, String message) {
             return newMessage(4433, enchant, iconId, message + " 玩家"); // 調用 newMessage 方法，字符串 ID 為 4433
         }

         // 獲取訊息的字節內容
         public byte[] getContent() throws IOException {
             return getBytes(); // 調用父類的方法獲取字節數組
         }
     }

