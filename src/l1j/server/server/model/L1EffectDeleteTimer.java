 package l1j.server.server.model;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1NpcInstance;

 public class L1EffectDeleteTimer implements Runnable {
   public boolean deleted;
   private final L1NpcInstance _npc;
   private final int _timeMillis;

   public L1EffectDeleteTimer(L1NpcInstance npc) {
     this.deleted = false;
     this._npc = npc;
     this._timeMillis = 500; } public void run() {
     if (!this.deleted) {
       if (this._npc.getTarget().isDead()) {
         this._npc.deleteMe();
         this.deleted = true;
       }
       if (this._npc.getTarget().hasSkillEffect(this._npc.getSkillId())) {
         GeneralThreadPool.getInstance().schedule(this, 500L);
         return;
       }
       this._npc.deleteMe();
       this.deleted = true;

       Thread.currentThread().interrupt();
     }
   }


   public void begin() {
     GeneralThreadPool.getInstance().schedule(this, this._timeMillis);
   }
 }


