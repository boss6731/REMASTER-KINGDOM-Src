 package l1j.server.server.serverpackets;


 public class S_TamBuff
   extends ServerBasePacket
 {
   private static final String S_TamBuff = "[S] S_TamBuff";

   public S_TamBuff(int id, int time, int type) {
     writeC(19);
     writeH(110);
     writeC(8);
     writeC(2);
     writeC(16);
     write7B(id);
     writeC(24);
     write7B(time);
     writeC(32);
     writeC(8);
     writeC(40);
     write7B((type == 1) ? 6100L : ((type == 2) ? 6546L : 6547L));
     writeC(48);
     writeC(0);
     writeC(56);
     writeC(1);
     writeC(64);
     write7B((type + 4180));
     writeC(72);
     writeH(8405);
     writeC(80);
     writeC(0);
     writeC(88);
     writeC(1);
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_TamBuff";
   }
 }


