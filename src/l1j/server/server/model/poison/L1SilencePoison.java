 package l1j.server.server.model.poison;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1SilencePoison extends L1Poison {
   private final L1Character _target;

   public static boolean doInfection(L1Character cha) {
     return doInfection(cha, 0);
   }

   public static boolean doInfection(L1Character cha, int time) {
     if (!L1Poison.isValidTarget(cha)) {
       return false;
     }

     cha.setPoison(new L1SilencePoison(cha, time));
     return true;
   }

   private L1SilencePoison(L1Character cha, int time) {
     this._target = cha;

     doInfection(time);
   }

   private void doInfection(int time) {
     this._target.setPoisonEffect(1);
     sendMessageIfPlayer(this._target, 310);
     if (this._target instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this._target;
       pc.sendPackets((ServerBasePacket)new S_PacketBox(161, pc, 6, (time == 0) ? -1 : time));
     }
     this._target.setSkillEffect(1007, (time * 1000));
   }


   public int getEffectId() {
     return 1;
   }


   public void cure() {
     this._target.setPoisonEffect(0);
     sendMessageIfPlayer(this._target, 311);

     if (this._target instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this._target;
       pc.sendPackets((ServerBasePacket)new S_PacketBox(161, pc, 0, 0));
     }

     this._target.killSkillEffectTimer(1007);
     this._target.setPoison(null);
   }
 }


