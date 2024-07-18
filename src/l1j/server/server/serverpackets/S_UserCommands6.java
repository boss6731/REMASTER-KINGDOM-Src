 package l1j.server.server.serverpackets;



 public class S_UserCommands6
   extends ServerBasePacket
 {
   private static final String S_UserCommands1 = "[C] S_UserCommands1";

   public S_UserCommands6(int number) {
     buildPacket(number);
   }

     private void buildPacket(int number) {
         writeC(248); // 寫入一個 byte 類型的數值，值為 248
         writeD(number); // 寫入一個整數類型的數值，這裡是方法的參數 number
         writeS(" 梅蒂斯 "); // 寫入一個字符串，值為 "메티스"
         writeS(" 傳說武器製作秘本 "); // 寫入一個字符串，值為 "製作秘法書"
         writeS(""); // 寫入一個空字符串
          writeS("\n === 生命之眼 === \n"+
                 "\n生命之眼 碎片(100個)\n"+
                 "\n==========================");
        // 寫入多行字符串，包含製作「生命之眼」的材料清單
     }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[C] S_UserCommands1";
   }
 }


