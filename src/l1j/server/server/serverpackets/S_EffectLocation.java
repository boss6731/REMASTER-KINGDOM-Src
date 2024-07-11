     package l1j.server.server.serverpackets;

     import l1j.server.server.model.L1Location;
     import l1j.server.server.types.Point;




     public class S_EffectLocation
       extends ServerBasePacket
     {
       public S_EffectLocation(Point pt, int gfxId) {
         this(pt.getX(), pt.getY(), gfxId);
       }







       public S_EffectLocation(L1Location loc, int gfxId) {
         this(loc.getX(), loc.getY(), gfxId);
       }








       public S_EffectLocation(int x, int y, int gfxId) {
         super(16);
         writeC(219);
         writeH(x);
         writeH(y);
         writeH(gfxId);
         writeC(0);
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


