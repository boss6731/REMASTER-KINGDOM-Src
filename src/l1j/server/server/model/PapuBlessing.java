 package l1j.server.server.model;

 import java.util.TimerTask;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class PapuBlessing
   extends TimerTask
 {
   private static Logger _log = Logger.getLogger(PapuBlessing.class.getName());
   private final L1PcInstance _pc;

   public PapuBlessing(L1PcInstance pc) {
     this._pc = pc;
   }


   public void run() {
     try {
       if (this._pc.isDead()) {
         return;
       }
       Papuregen();
     } catch (Throwable e) {
       _log.log(Level.WARNING, e.getLocalizedMessage(), e);
     }
   }

   public void Papuregen() {
     this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._pc.getId(), 2245));
     this._pc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._pc.getId(), 2245));
   }
 }


