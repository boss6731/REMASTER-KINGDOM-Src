 package l1j.server.server.model;

 import java.util.TimerTask;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class HpRegenerationByDoll extends TimerTask {
   private static Logger _log = Logger.getLogger(HpRegenerationByDoll.class.getName());
   private final L1PcInstance _pc;
   private final L1DollInstance _doll;
   private final int _hpr;

   public HpRegenerationByDoll(L1PcInstance pc, L1DollInstance doll, int hpr) {
     this._pc = pc;
     this._doll = doll;
     this._hpr = hpr;
   }


   public void run() {
     try {
       if (this._pc == null || this._pc.isDead()) {
         if (this._doll != null) {
           this._doll.deleteDoll();
         }
         return;
       }
       regenHp();
     } catch (Throwable e) {
       _log.log(Level.WARNING, e.getLocalizedMessage(), e);
     }
   }

   public void regenHp() {
     int newHp = this._pc.getCurrentHp() + this._hpr;
     if (newHp < 0) {
       newHp = 0;
     }
     this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._pc.getId(), 6321));
     this._pc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._pc.getId(), 6321));
     this._pc.setCurrentHp(newHp);
   }
 }


