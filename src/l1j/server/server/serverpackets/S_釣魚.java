 package l1j.server.server.serverpackets;

 public class S_釣魚
   extends ServerBasePacket
 {
   private final int FishUI = 63;

   private final int CAUI = 76;

   public S_釣魚(int t) {
     writeC(19);
     writeH(63);
     writeH(264);
     writeC(16);
     writeBit(t);
     writeBit(24L);
     writeBit(2L);
     writeH(0);
   }

   public S_釣魚(int type, int test, int time, String CName, boolean swich) {
     writeC(19);
     writeH(76);
     writeH(264);
     writeC(16);
     if (swich) {
       writeH(test);
       writeH(time);
       writeS(CName);
     } else {
       writeC(0);
     }
     writeH(0);
   }

   public byte[] getContent() {
     return getBytes();
   }
 }


