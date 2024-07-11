 package l1j.server.server.model;

 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_SPMR;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.IntRange;


 public class Resistance
 {
   private static final int LIMIT_MIN = -128;
   private static final int LIMIT_MAX = 999;
   private static final int LIMIT_MIN_MR = 0;
   private static final int LIMIT_MAX_MR = 999;
   private int baseMr = 0;
   private int addedMr = 0;
   private int levelMr = 0;

   private int fire = 0;
   private int water = 0;
   private int wind = 0;
   private int earth = 0;

   private int calcPcDefense = 0;
   private int PVPweaponTotalDamage = 0;

   private L1Character character = null;

   public void dispose() {
     this.character = null;
   }


   public Resistance() {}

   public Resistance(L1Character cha) {
     init();
     this.character = cha;
   }

   public void init() {
     this.baseMr = this.addedMr = 0;
     this.fire = this.water = this.wind = this.earth = 0;
     this.calcPcDefense = this.PVPweaponTotalDamage = 0;
   }

   private int checkMrRange(int i, int MIN) {
     return IntRange.ensure(i, MIN, 999);
   }

   private byte checkRange(int i) {
     return (byte)IntRange.ensure(i, -128, 999);
   }

   public int getEffectedMrBySkill() {
     int effectedMr = 0;
     if (this.character != null) {
       effectedMr = getMr();

       if (this.character.hasSkillEffect(153))
         effectedMr /= 2;
     }
     return effectedMr;
   }

   public int getAddedMr() {
     return this.addedMr;
   }

   public int getMr() {
     int add_mr = this.baseMr + this.addedMr + getLevelMr();
     int Level = this.character.getLevel();
     int passive_mr = 0;
     if (this.character != null && this.character.isPassive(MJPassiveID.TACTICAL_ADVANCE.toInt())) {
       if (Level < 80) {
         Level = 80;
       }
       passive_mr += 3 + (Level - 80) / 3 * 2;

       if (passive_mr > 15) {
         passive_mr = 15;
       }
     }
     if (this.character == null) {
       return 0;
     }
     if (this.character.hasSkillEffect(246)) {
       add_mr = (int)(add_mr + add_mr * 0.2D);
     }
     if (this.character.isPassive(MJPassiveID.RESIST_ELEMENT.toInt())) {
       add_mr += 5;
     }
     if (this.character.hasSkillEffect(7687) || this.character.hasSkillEffect(7688)) {
       add_mr = (int)(add_mr + add_mr * 0.1D);
     }



     return checkMrRange(add_mr + passive_mr, 0);
   }

   public int getBaseMr() {
     return this.baseMr;
   }

   public void addMr(int i) {
     setAddedMr(this.addedMr + i);

     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
     }
   }


   public void setBaseMr(int i) {
     this.baseMr = checkMrRange(i, 0);
   }

   private void setAddedMr(int i) {
     this.addedMr = checkMrRange(i, -this.baseMr);
   }

   public int getLevelMr() {
     this.levelMr = this.character.getLevel() / 2;
     return this.levelMr;
   }














   public int getcalcPcDefense() {
     return this.calcPcDefense;
   }














   public int getPVPweaponTotalDamage() {
     return this.PVPweaponTotalDamage;
   }

   public int getFire() {
     return increase_elemental_resist(this.fire);
   }

   public int getWater() {
     return increase_elemental_resist(this.water);
   }

   public int getWind() {
     return increase_elemental_resist(this.wind);
   }

   public int getEarth() {
     return increase_elemental_resist(this.earth);
   }

   private int increase_elemental_resist(int source_resist) {
     return (this.character == null || !this.character.isPassive(MJPassiveID.RESIST_ELEMENT.toInt())) ? source_resist : (source_resist + 5);
   }

   public void addFire(int i) {
     this.fire = checkRange(this.fire + i);
   }

   public void addWater(int i) {
     this.water = checkRange(this.water + i);
   }

   public void addWind(int i) {
     this.wind = checkRange(this.wind + i);
   }

   public void addEarth(int i) {
     this.earth = checkRange(this.earth + i);
   }

   public void addcalcPcDefense(int i) {
     this.calcPcDefense = checkRange(this.calcPcDefense + i);
   }



   public void addPVPweaponTotalDamage(int i) {
     this.PVPweaponTotalDamage = checkRange(this.PVPweaponTotalDamage + i);
   }

   public void addAllNaturalResistance(int i) {
     addFire(i);
     addWater(i);
     addWind(i);
     addEarth(i);
   }

   public int getMrAfterEraseRemove() {
     int effectedMr = getMr();
     if (this.character.hasSkillEffect(153)) {
       this.character.removeSkillEffect(153);
       this.character.sendPackets((ServerBasePacket)new S_PacketBox(180, 55, false));
       effectedMr /= 2;
     }
     return effectedMr;
   }
 }


