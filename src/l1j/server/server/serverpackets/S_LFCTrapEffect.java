 package l1j.server.server.serverpackets;


 public class S_LFCTrapEffect
   extends ServerBasePacket
 {
   public S_LFCTrapEffect(int objId, int gfxId) {
     writeC(86);
     writeD(objId);
     writeH(gfxId);
   }
   public S_LFCTrapEffect(int x, int y, int gfxId) {
     writeC(219);
     writeH(x);
     writeH(y);
     writeH(gfxId);
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "S_LFCTrapEffect";
   }
 }


