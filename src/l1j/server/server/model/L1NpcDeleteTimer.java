 package l1j.server.server.model;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1NpcDeleteTimer implements Runnable {
   public boolean npcdead;
   private final L1NpcInstance _npc;
   private final int _timeMillis;

   public L1NpcDeleteTimer(L1NpcInstance npc, int timeMillis) {
     this.npcdead = false;
     this._npc = npc;
     this._timeMillis = timeMillis; } public void run() {
     if (this._npc.getNpcId() == 8502104) {
       if (!this.npcdead) {
         Broadcaster.broadcastPacket((L1Character)this._npc, (ServerBasePacket)new S_DoActionGFX(this._npc.getId(), 11), true);
         this.npcdead = true;
         GeneralThreadPool.getInstance().schedule(this, 500L);
       } else if (this.npcdead) {
         this._npc.NpcDie();
       }
     } else {
       this._npc.deleteMe();
     }
   }


   public void begin() {
     GeneralThreadPool.getInstance().schedule(this, this._timeMillis);
   }
 }


