 package l1j.server.server.serverpackets;

 public class S_OnlyEffect
   extends ServerBasePacket
 {
   private int _gfxId;

   public S_OnlyEffect(int objId, int gfxId) {
     writeC(86);
     writeD(objId);
     writeH(gfxId);
     this._gfxId = gfxId;
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return S_OnlyEffect.class.getName();
   }
 }


