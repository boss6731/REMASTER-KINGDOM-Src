 package l1j.server.server.model;

 import l1j.server.server.model.Instance.L1PcInstance;



 class AcHpMpBonusEffect
   implements L1ArmorSetEffect
 {
   private final int _ac;
   private final int _addHp;
   private final int _addMp;
   private final int _regenHp;
   private final int _regenMp;
   private final int _addMr;
   private final int _weightreduction;

   public AcHpMpBonusEffect(int ac, int addHp, int addMp, int regenHp, int regenMp, int addMr, int weightreduction) {
     this._ac = ac;
     this._addHp = addHp;
     this._addMp = addMp;
     this._regenHp = regenHp;
     this._regenMp = regenMp;
     this._addMr = addMr;
     this._weightreduction = weightreduction;
   }


   public void giveEffect(L1PcInstance pc) {
     pc.getAC().addAc(this._ac);
     pc.addMaxHp(this._addHp);
     pc.addMaxMp(this._addMp);
     pc.addHpr(this._regenHp);
     pc.addMpr(this._regenMp);
     pc.getResistance().addMr(this._addMr);
     pc.addWeightReduction(this._weightreduction);
   }



   public void cancelEffect(L1PcInstance pc) {
     pc.getAC().addAc(-this._ac);
     pc.addMaxHp(-this._addHp);
     pc.addMaxMp(-this._addMp);
     pc.addHpr(-this._regenHp);
     pc.addMpr(-this._regenMp);
     pc.getResistance().addMr(-this._addMr);
     pc.addWeightReduction(-this._weightreduction);
   }
 }


