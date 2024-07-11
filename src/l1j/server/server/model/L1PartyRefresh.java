 package l1j.server.server.model;

 import java.util.TimerTask;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Party;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1PartyRefresh extends TimerTask {
     private static Logger _log = Logger.getLogger(L1PartyRefresh.class.getName());

     private final L1PcInstance _pc;

     public L1PartyRefresh(L1PcInstance pc) {
         this._pc = pc;
     }

     public void run() {
         try {
             if (this._pc.isDead() || this._pc.getParty() == null) {
                 return;
             }
             rp();
         } catch (Throwable e) {
             _log.log(Level.WARNING, e.getLocalizedMessage(), e);
         }
     }

     public void rp() {
         this._pc.sendPackets((ServerBasePacket)new S_Party(110, this._pc));
     }
 }


