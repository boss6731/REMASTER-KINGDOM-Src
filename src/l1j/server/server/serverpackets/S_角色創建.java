     package l1j.server.server.serverpackets;

     public class S_角色創建
       extends ServerBasePacket
     {
       public static final String S_角色創建 = "[S] S_LoginResult";
       public static final int 케릭비번표시 = 51;
       public static final int 케릭비번성공 = 22;

       public S_角色創建(int code) {
         writeC(43);
         writeC(51);
         writeC(1);
         writeC(0);
         writeC(0);
         writeC(0);
       }

       public S_角色創建() {
         buildPacket();
       }

       private void buildPacket() {
         writeC(43);
         writeC(63);
         writeC(1);
         writeC(0);
         writeC(0);
         writeC(0);
       }

       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_LoginResult";
       }
     }


