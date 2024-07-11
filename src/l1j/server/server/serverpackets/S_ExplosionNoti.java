     package l1j.server.server.serverpackets;

     import l1j.server.server.model.L1Object;

     public class S_ExplosionNoti
       extends ServerBasePacket {
       private static final int SC_OBJECT_EXPLOSION_NOTI = 818;

       public static S_ExplosionNoti get(L1Object obj, long mills) {
         S_ExplosionNoti s = new S_ExplosionNoti();
         s.writeC(8);
         s.writeBit(obj.getId());
         s.writeC(16);
         s.writeBit(mills);
         s.writeH(0);
         return s;
       }

       private S_ExplosionNoti() {
         writeC(19);
         writeH(818);
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


