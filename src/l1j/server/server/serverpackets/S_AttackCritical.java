     package l1j.server.server.serverpackets;

     import java.util.concurrent.atomic.AtomicInteger;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Character;



     public class S_AttackCritical
       extends ServerBasePacket
     {
       private static final String S_AttackCritical = "[S] S_AttackCritical";
       private byte[] _byte = null;

       private static AtomicInteger _sequentialNumber = new AtomicInteger(0);


       public S_AttackCritical(L1PcInstance pc, int objid, int type, boolean is_double_weapon) {
         int gfxid = 0;
         writeC(5);
         writeC(1);
         writeD(pc.getId());
         writeD(objid);
         writeH(1);
         writeC(pc.getHeading());
         writeH(0);
         writeH(0);

         switch (type) {
           case 4:
             gfxid = 13411;
             break;
           case 11:
             if (is_double_weapon) {
               gfxid = 13415; break;
             }
             gfxid = 13414;
             break;

           case 24:
             gfxid = 13402;
             break;
           case 40:
             gfxid = 13413;
             break;
           case 46:
             gfxid = 13412;
             break;
           case 50:
             gfxid = 13410;
             break;
           case 54:
             gfxid = 13417;
             break;
           case 58:
             gfxid = 13416;
             break;
           case 90:
             gfxid = 13409;
             break;
           case 91:
             gfxid = 21083;
             break;
           case 92:
             gfxid = 13398;
             break;
           case 99:
             gfxid = 13415;
             break;
         }

         if (gfxid > 0) {
           writeC(2);
           writeD(gfxid);
         }
         writeH(0);
       }


       public S_AttackCritical(L1Character cha, int targetobj, int x, int y, int type, boolean isHit) {
         int gfxid = 0;
         int aid = 1;

         if (cha.getCurrentSpriteId() == 3860 || cha.getCurrentSpriteId() == 7959 || cha.getCurrentSpriteId() == 11382) {
           aid = 21;
         }
         writeC(5);
         writeC(aid);
         writeD(cha.getId());
         writeD(targetobj);
         writeC(isHit ? 6 : 0);
         writeC(0);
         writeC(cha.getHeading());
         writeD(_sequentialNumber.incrementAndGet());
         if (type == 20) {
           gfxid = 13392;
         } else if (type == 62) {
           gfxid = 13398;
         }

         writeH(gfxid);
         writeC(127);
         writeH(x);
         writeH(y);
         writeH(cha.getX());
         writeH(cha.getY());
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(0);
       }


       public byte[] getContent() {
         if (this._byte == null) {
           this._byte = getBytes();
         }
         return this._byte;
       }


       public String getType() {
         return "[S] S_AttackCritical";
       }
     }


