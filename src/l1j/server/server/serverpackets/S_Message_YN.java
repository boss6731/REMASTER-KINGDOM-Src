 package l1j.server.server.serverpackets;


 public class S_Message_YN
   extends ServerBasePacket
 {
   public S_Message_YN(int type) {
     buildPacket(type, (String)null, (String)null, (String)null, 1);
   }

   public S_Message_YN(int type, String msg1) {
     buildPacket(type, msg1, (String)null, (String)null, 1);
   }

   public S_Message_YN(boolean b) {
     writeC(85);
     writeH(0);
     writeD(0);
     writeH(0);
     writeS("");
   }

   public S_Message_YN(int type, String msg1, String msg2) {
     buildPacket(type, msg1, msg2, (String)null, 2);
   }

   public S_Message_YN(int type, String msg1, String msg2, String msg3) {
     buildPacket(type, msg1, msg2, msg3, 3);
   }

   private void buildPacket(int type, String msg1, String msg2, String msg3, int check) {
     writeC(85);
     writeH(0);
     writeD(0);
     writeH(type);
     if (check == 1) {
       writeS(msg1);
     } else if (check == 2) {
       writeS(msg1);
       writeS(msg2);
     } else if (check == 3) {
       writeS(msg1);
       writeS(msg2);
       writeS(msg3);
     }
   }

   public S_Message_YN(int idx, int type, String msg) {
     writeC(85);
     writeH(0);
     writeD(idx);
     writeH(type);
     writeS(msg);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_Message_YN";
   }
 }


