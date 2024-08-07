     package l1j.server.server.serverpackets;



     public class S_UserCommands11
       extends ServerBasePacket
     {
       private static final String S_UserCommands1 = "[C] S_UserCommands1";

       public S_UserCommands11(int number) {
         buildPacket(number);
       }

         private void buildPacket(int number) {
             writeC(248); // 寫入一個 byte 類型的數值，值為 248
             writeD(number); // 寫入一個整數類型的數值，這裡是方法的參數 number
             writeS(" 梅蒂斯 "); // 寫入一個字符串，值為 "메蒂斯"
             writeS(" 傳說武器製作秘本 "); // 寫入一個字符串，值為 "製作秘法書"
             writeS(""); // 寫入一個空字符串
             writeS("\n ── +10 真·戰士大劍 ── \n"+
                     "\n水龍鱗片 (10)個"+
                     "\n風龍鱗片 (10)個"+
                     "\n地龍鱗片 (10)個"+
                     "\n火龍鱗片 (10)個"+
                     "\n魔物的氣息 (270)個"+
                     "\n匠人的武器魔法書 (3)個"+
                     "\n+8 真·戰士大劍 (1)個"+
                     "\n"+
                     "\n──────────────製作是天堂的花朵。\n");
            // 寫入多行字符串，包含製作「+10 真·戰士大劍」的材料清單和補充說明
         }



       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[C] S_UserCommands1";
       }
     }


