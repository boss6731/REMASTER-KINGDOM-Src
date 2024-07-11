 package l1j.server.server.serverpackets;



 public class S_UserCommands7
   extends ServerBasePacket
 {
   private static final String S_UserCommands1 = "[C] S_UserCommands1";

   public S_UserCommands7(int number) {
     buildPacket(number);
   }

     private void buildPacket(int number) {
         writeC(248); // 寫入一個 byte 類型的數值，值為 248
         writeD(number); // 寫入一個整數類型的數值，這裡是方法的參數 number
         writeS(" 梅蒂斯 "); // 寫入一個字符串，值為 "메티스"
         writeS(" 製作秘法書 "); // 寫入一個字符串，值為 "製作秘法書"
         writeS(""); // 寫入一個空字符串
         writeS("\n === 歷史書 === \n"+
                 "\n完成的拉斯塔巴德歷史書(1頁)\n"+
                 "\n==========================");
         // 寫入多行字符串，包含製作「歷史書」的材料清單
     }



   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[C] S_UserCommands1";
   }
 }


