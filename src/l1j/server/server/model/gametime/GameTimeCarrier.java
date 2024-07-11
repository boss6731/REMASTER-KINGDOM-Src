 package l1j.server.server.model.gametime;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_GameTime;
 import l1j.server.server.serverpackets.ServerBasePacket;
















 public class GameTimeCarrier
   implements Runnable
 {
   private L1PcInstance _pc;
   private boolean on = true;

   public GameTimeCarrier(L1PcInstance pc) {
     this._pc = pc;
   }


   public void run() {
     try {
       if (!this.on || this._pc == null) {
         return;
       }
       if (this._pc.getNetConnection() == null || this._pc.getNetConnection().isClosed()) {
         this._pc.logout();

         return;
       }
       long serverTime = GameTimeClock.getInstance().getGameTime().getSeconds();
       if (serverTime % 300L == 0L) {
         this._pc.sendPackets((ServerBasePacket)new S_GameTime(serverTime));
       }

       GeneralThreadPool.getInstance().schedule(this, 1000L);
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   public void start() {
     GeneralThreadPool.getInstance().execute(this);
   }

   public void stop() {
     this.on = false;
   }
 }


