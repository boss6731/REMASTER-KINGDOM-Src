 package l1j.server.server.utils;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class DelaySender implements Runnable {
   private L1PcInstance _pc;

   public static void send(L1PcInstance pc, ServerBasePacket pck, long delay) {
     if (delay <= 0L) {
       pc.sendPackets(pck, true);
     } else {
       GeneralThreadPool.getInstance().schedule(new DelaySender(pc, pck), delay);
     }
   }
   private ServerBasePacket _pck;

   private DelaySender(L1PcInstance pc, ServerBasePacket pck) {
     this._pc = pc;
     this._pck = pck;
   }


   public void run() {
     try {
       if (this._pc != null)
         this._pc.sendPackets(this._pck, true);
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       this._pc = null;
       this._pck = null;
     }
   }
 }


