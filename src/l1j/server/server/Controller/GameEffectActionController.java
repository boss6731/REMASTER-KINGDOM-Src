 package l1j.server.server.Controller;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class GameEffectActionController
   implements Runnable {
   private L1NpcInstance _npc;
   private int _actId;

   public GameEffectActionController(L1NpcInstance npc, int actId, int count, int delay) {
     this._npc = npc;
     this._actId = actId;
     this._count = count;
     this._delay = delay;
   }
   private int _count; private int _delay;
   public void start() {
     GeneralThreadPool.getInstance().execute(this);
   }


   public void run() {
     try {
       while (this._count > 0) {
         if (this._npc.getNpcId() == 8502104) {
           if (this._count == 1)
             Broadcaster.broadcastPacket((L1Character)this._npc, (ServerBasePacket)new S_DoActionGFX(this._npc.getId(), this._actId));
         } else {
           Broadcaster.broadcastPacket((L1Character)this._npc, (ServerBasePacket)new S_DoActionGFX(this._npc.getId(), this._actId));
         }  this._count--;
         Thread.sleep(this._delay);
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
 }


