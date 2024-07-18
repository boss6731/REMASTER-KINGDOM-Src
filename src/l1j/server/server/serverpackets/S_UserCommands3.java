 package l1j.server.server.serverpackets;



 public class S_UserCommands3
   extends ServerBasePacket
 {
   private static final String S_UserCommands3 = "[C] S_UserCommands3";

   public S_UserCommands3(int number) {
     buildPacket(number);
   }

     private void buildPacket(int number) {
         writeC(248); // 寫入一個 byte 類型的數值，值為 248
         writeD(number); // 寫入一個整數類型的數值，這裡是方法的參數 number
         writeS(" 梅蒂斯 "); // 寫入一個字符串，值為 "메티스"
         writeS(" 傳說武器製作秘本 "); // 寫入一個字符串，值為 "製作秘法書"
         writeS(""); // 寫入一個空字符串
         writeS("\n=== 黑王刀 ===\n\n"+
                 "龍的心臟(1個)\n"+
                 "最高級紅寶石(3個)\n"+
                 "黑光的雙刀(1個)\n"+
                 "金幣(100,000元)\n"+
                 "被詛咒的皮革(10個)\n"+
                 "冰女王的氣息(9個)\n"+
                 "格蘭肯的眼淚(3個)\n"+
                 "\n==========================");

         // 寫入多行字符串，包含製作「黑王刀」的材料清單
     }



   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[C] S_UserCommands3";
   }
 }


