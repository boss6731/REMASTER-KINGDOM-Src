     package l1j.server.server.serverpackets;

     import java.io.IOException;
     import l1j.server.server.model.L1Object;

     public class S_Explosion
       extends ServerBasePacket
     {
       private static final int SC_OBJECT_EXPLOSION_NOTI = 818;

       public static S_Explosion get(L1Object obj, long timeinMillis) {
         S_Explosion s = new S_Explosion();
         s.writeC(8);
         s.writeBit(obj.getId());
         s.writeC(16);
         s.writeBit(timeinMillis);
         s.writeH(0);
         return s;
       }

       private S_Explosion() {
         super(24);
         writeC(19);
         writeH(818);
       }


       public byte[] getContent() throws IOException {
         return getBytes();
       }
     }


