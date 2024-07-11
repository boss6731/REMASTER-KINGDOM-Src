 package l1j.server.server.model.trap;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_EffectLocation;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.storage.TrapStorage;

















 public abstract class L1Trap
 {
   protected final int _id;
   protected final int _gfxId;
   protected final boolean _isDetectionable;

   public L1Trap(TrapStorage storage) {
     this._id = storage.getInt("id");
     this._gfxId = storage.getInt("gfxId");
     this._isDetectionable = storage.getBoolean("isDetectionable");
   }

   public L1Trap(int id, int gfxId, boolean detectionable) {
     this._id = id;
     this._gfxId = gfxId;
     this._isDetectionable = detectionable;
   }

   public int getId() {
     return this._id;
   }

   public int getGfxId() {
     return this._gfxId;
   }

   protected void sendEffect(L1Object trapObj) {
     if (getGfxId() == 0) {
       return;
     }

     S_EffectLocation effect = new S_EffectLocation(trapObj.getLocation(), getGfxId());

     for (L1PcInstance pc : L1World.getInstance()
       .getRecognizePlayer(trapObj)) {
       pc.sendPackets((ServerBasePacket)effect);
     }
   }

   public abstract void onTrod(L1PcInstance paramL1PcInstance, L1Object paramL1Object);

   public void onDetection(L1PcInstance caster, L1Object trapObj) {
     if (this._isDetectionable) {
       sendEffect(trapObj);
     }
   }

   public static L1Trap newNull() {
     return new L1NullTrap();
   }
 }


