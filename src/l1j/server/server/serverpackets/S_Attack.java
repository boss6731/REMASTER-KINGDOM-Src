         package l1j.server.server.serverpackets;

         import java.io.IOException;
         import java.util.concurrent.atomic.AtomicInteger;
         import l1j.server.server.model.L1Object;
         import l1j.server.server.utils.MJCommons;


         public class S_Attack
           extends ServerBasePacket
         {
           private static AtomicInteger _sequence = new AtomicInteger(0);

           public static S_Attack getKeylink2(L1Object own, L1Object tar, int type, boolean isHit) {
             S_Attack s = new S_Attack();
             s.writeC(59);
             s.writeD(own.getId());
             s.writeD(tar.getId());
             if (isHit) {
               s.writeH(60);
             } else {
               s.writeH(0);
             }  int h = MJCommons.calcheading(own.getX(), own.getY(), tar.getX(), tar.getY());
             s.writeC(h);
             s.writeD(_sequence.incrementAndGet());
             s.writeH(21081);
             s.writeC(0);
             s.writeH(own.getX());
             s.writeH(own.getY());
             s.writeH(tar.getX());
             s.writeH(tar.getY());
             s.writeD(0);
             s.writeC(0);
             return s;
           }

           public static S_Attack getKeylink(L1Object own, L1Object tar, int type, boolean isHit) {
             S_Attack s = new S_Attack();
             s.writeC(59);
             s.writeD(own.getId());
             s.writeD(tar.getId());
             if (isHit) {
               s.writeH(60);
             } else {
               s.writeH(0);
             }  int h = MJCommons.calcheading(own.getX(), own.getY(), tar.getX(), tar.getY());
             s.writeC(h);
             s.writeD(_sequence.incrementAndGet());
             s.writeH(21081);
             s.writeC(0);
             s.writeH(own.getX());
             s.writeH(own.getY());
             s.writeH(tar.getX());
             s.writeH(tar.getY());
             s.writeD(0);
             s.writeC(0);
             return s;
           }
           public static S_Attack getKeylink_Critical(L1Object own, L1Object tar, int type, boolean isHit) {
             S_Attack s = new S_Attack();
             s.writeC(59);
             s.writeD(own.getId());
             s.writeD(tar.getId());
             if (isHit) {
               s.writeH(60);
             } else {
               s.writeH(0);
             }  int h = MJCommons.calcheading(own.getX(), own.getY(), tar.getX(), tar.getY());
             s.writeC(h);
             s.writeD(_sequence.incrementAndGet());
             s.writeH(21083);
             s.writeC(0);
             s.writeH(own.getX());
             s.writeH(own.getY());
             s.writeH(tar.getX());
             s.writeH(tar.getY());
             s.writeD(0);
             s.writeC(0);
             return s;
           }

           public static S_Attack getSpear(L1Object own, L1Object tar, int type, boolean isHit) {
             S_Attack s = new S_Attack();
             s.writeC(1);
             s.writeD(own.getId());
             s.writeD(tar.getId());
             s.writeH(34);
             int h = MJCommons.calcheading(own.getX(), own.getY(), tar.getX(), tar.getY());
             s.writeC(h);
             s.writeD(_sequence.incrementAndGet());
             s.writeH(19282);
             s.writeC(0);
             s.writeH(own.getX());
             s.writeH(own.getY());
             s.writeH(tar.getX());
             s.writeH(tar.getY());
             s.writeD(0);
             s.writeC(0);
             return s;
           }

           private S_Attack() {
             writeC(5);
           }

           public byte[] getContent() throws IOException {
             return getBytes();
           }
         }


