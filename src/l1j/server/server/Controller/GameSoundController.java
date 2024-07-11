 package l1j.server.server.Controller;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.serverpackets.S_Sound;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class GameSoundController
   implements Runnable {
   private L1NpcInstance _npc;
   private int _soundNum;

   public GameSoundController(L1NpcInstance npc, int soundNum, int count, int delay) {
     this._npc = npc;
     this._soundNum = soundNum;
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
         Broadcaster.broadcastPacket((L1Character)this._npc, (ServerBasePacket)new S_Sound(this._soundNum));
         this._count--;
         Thread.sleep(this._delay);
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
 }


