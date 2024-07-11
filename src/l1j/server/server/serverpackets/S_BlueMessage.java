     package l1j.server.server.serverpackets;


     public class S_BlueMessage
       extends ServerBasePacket
     {
       private static final String _S__18_BLUEMESSAGE = "[S] S_BlueMessage";

       public S_BlueMessage(int type, String msg1) {
         buildPacket(type, msg1, (String)null, (String)null, 1);
       }

       public S_BlueMessage(int type, String msg1, String msg2) {
         buildPacket(type, msg1, msg2, (String)null, 2);
       }

       public S_BlueMessage(int type, String msg1, String msg2, String msg3) {
         buildPacket(type, msg1, msg2, msg3, 3);
       }


       private void buildPacket(int type, String msg1, String msg2, String msg3, int check) {
         writeC(102);
         writeH(type);
         if (check == 1) {
           if (msg1.length() <= 0) {
             writeC(0);
           } else {
             writeC(1);
             writeS(msg1);
           }
         } else if (check == 2) {
           writeC(2);
           writeS(msg1);
           writeS(msg2);
         } else if (check == 3) {
           writeC(3);
           writeS(msg1);
           writeS(msg2);
           writeS(msg3);
         }
         writeD(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_BlueMessage";
       }
     }


