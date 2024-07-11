 package l1j.server.server.model;

 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.IntRange;

 public class AC
 {
   private L1Character _cha;
   private int ac = 0;

   private int baseAc = 0;

   public AC(L1Character cha) {
     this._cha = cha;
   }

   public int getAc() {
     int add_ac = 0;
     if (this._cha instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this._cha;
       if (pc != null && pc.isPassive(MJPassiveID.IllUSION_DIAMONDGOLEM_PASSIVE.toInt())) {
         add_ac -= 10;
       }
     }
     return this.ac + add_ac;
   }

   public void addAc(int i) {
     setAc(this.baseAc + i);

     if (this._cha instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this._cha;
       pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
     }
   }

   public void setAc(int i) {
     this.baseAc = i;
     this.ac = IntRange.ensure(i, -999, 999);
     if (this._cha != null) {
       boolean isChangedDG = false;
       boolean isChangedER = false;
       int decreaseAC = -this.ac - 100;
       if (decreaseAC >= 0) {
         int effectedValue = decreaseAC / 20;
         isChangedDG = this._cha.setCharacterDG(effectedValue);
         isChangedER = this._cha.setCharacterER(effectedValue);
       } else {
         isChangedDG = this._cha.setCharacterDG(0);
         isChangedER = this._cha.setCharacterER(0);
       }
       if ((isChangedDG || isChangedER) && this._cha.instanceOf(4))
         this._cha.sendPackets((ServerBasePacket)new S_OwnCharAttrDef((L1PcInstance)this._cha));
     }
   }
 }


