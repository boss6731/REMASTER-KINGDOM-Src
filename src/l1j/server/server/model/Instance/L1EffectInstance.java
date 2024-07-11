 package l1j.server.server.model.Instance;

 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.L1CastleLocation;
 import l1j.server.server.model.L1Magic;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;


 public class L1EffectInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private static final int FW_DAMAGE_INTERVAL = 1000;

   public L1EffectInstance(L1Npc template) {
     super(template);

     if (getNpcTemplate().get_npcId() == 81157) {
       GeneralThreadPool.getInstance().schedule(new FwDamageTimer(this), 0L);
     }
   }



   public void onAction(L1PcInstance pc) {}


   public void deleteMe() {
     this._destroyed = true;
     if (getInventory() != null) {
       getInventory().clearItems();
     }
     allTargetClear();
     this._master = null;
     setCubePc((L1PcInstance)null);
     L1World.getInstance().removeVisibleObject((L1Object)this);
     L1World.getInstance().removeObject((L1Object)this);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
       if (pc == null)
         continue;  pc.removeKnownObject((L1Object)this);
       pc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)this));
     }
     removeAllKnownObjects();
   }

   class FwDamageTimer implements Runnable {
     private L1EffectInstance _effect;

     public FwDamageTimer(L1EffectInstance effect) {
       this._effect = effect;
     }


     public void run() {
       if (this._effect == null) {
         return;
       }

       L1PcInstance pc = null;
       L1Magic magic = null;
       L1MonsterInstance mob = null;
       try {
         for (L1Object objects : L1World.getInstance().getVisibleObjects((L1Object)this._effect, 0)) {
           if (objects instanceof L1PcInstance) {
             pc = (L1PcInstance)objects;
             if (pc.isDead()) {
               continue;
             }
             if (pc.getId() == this._effect.CubePc().getId()) {
               continue;
             }
             if (pc.getZoneType() == 1) {
               boolean isNowWar = false;
               int castleId = L1CastleLocation.getCastleIdByArea(pc);
               if (castleId > 0) {
                 isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
               }
               if (!isNowWar) {
                 continue;
               }
             }
             magic = new L1Magic(this._effect, pc);
             int damage = magic.calcNpcFireWallDamage();
             if (damage == 0) {
               continue;
             }
             pc.sendPackets((ServerBasePacket)new S_DoActionGFX(pc.getId(), 2));
             pc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(pc.getId(), 2));
             pc.receiveDamage(this._effect, damage); continue;
           }  if (objects instanceof L1MonsterInstance) {
             mob = (L1MonsterInstance)objects;
             if (mob.isDead()) {
               continue;
             }
             magic = new L1Magic(this._effect, mob);
             int damage = magic.calcNpcFireWallDamage();
             if (damage == 0) {
               continue;
             }
             mob.broadcastPacket((ServerBasePacket)new S_DoActionGFX(mob.getId(), 2));
             mob.receiveDamage(this._effect, damage);
           }
         }
         if (!L1EffectInstance.this._destroyed) {
           GeneralThreadPool.getInstance().schedule(this, 1000L);
         }
       } catch (Exception exception) {}
     }
   }
 }


