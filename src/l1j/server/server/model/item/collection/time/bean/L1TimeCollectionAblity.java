 package l1j.server.server.model.item.collection.time.bean;

 import java.sql.ResultSet;
 import java.sql.SQLException;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
 import l1j.server.server.serverpackets.S_HPUpdate;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.S_OwnCharStatus;
 import l1j.server.server.serverpackets.S_SPMR;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1TimeCollectionAblity
 {
   private int flag;
   private int sum;
   private L1TimeCollectionBuffType buffType;
   private int ac;
   private int str;
   private int con;
   private int dex;
   private int inti;
   private int wis;
   private int cha;
   private int shortDamage;
   private int shortHit;
   private int shortCritical;
   private int longDamage;
   private int longHit;
   private int longCritical;
   private int spellpower;
   private int magicHit;
   private int magicCritical;
   private int hp;
   private int mp;
   private int hpr;
   private int mpr;
   private int attrFire;
   private int attrWater;
   private int attrWind;
   private int attrEarth;
   private int attrAll;
   private int mr;

   public L1TimeCollectionAblity(ResultSet rs) throws SQLException {
     this(rs
         .getInt("flag"), rs.getInt("sum"),
         L1TimeCollectionBuffType.getType(rs.getString("buffType")), rs
         .getInt("ac"), rs
         .getInt("str"), rs.getInt("con"), rs.getInt("dex"), rs.getInt("int"), rs.getInt("wis"), rs.getInt("cha"), rs
         .getInt("shortDamage"), rs.getInt("shortHit"), rs.getInt("shortCritical"), rs
         .getInt("longDamage"), rs.getInt("longHit"), rs.getInt("longCritical"), rs
         .getInt("spellpower"), rs.getInt("magicHit"), rs.getInt("magicCritical"), rs
         .getInt("hp"), rs.getInt("mp"), rs.getInt("hpr"), rs.getInt("mpr"), rs
         .getInt("attrFire"), rs.getInt("attrWater"), rs.getInt("attrWind"), rs.getInt("attrEarth"), rs.getInt("attrAll"), rs
         .getInt("mr"), rs.getInt("weight"), rs.getInt("dg"), rs.getInt("er"), rs.getInt("me"), rs
         .getInt("reduction"), rs.getInt("reductionEgnor"), rs.getInt("reductionMagic"), rs
         .getInt("PVPDamage"), rs.getInt("PVPReduction"), rs.getInt("PVPReductionEgnor"), rs.getInt("PVPReductionMagic"), rs.getInt("PVPReductionMagicEgnor"), rs
         .getInt("toleranceSkill"), rs.getInt("toleranceSpirit"), rs.getInt("toleranceDragon"), rs.getInt("toleranceFear"), rs.getInt("toleranceAll"), rs
         .getInt("hitupSkill"), rs.getInt("hitupSpirit"), rs.getInt("hitupDragon"), rs.getInt("hitupFear"), rs.getInt("hitupAll"), rs
         .getInt("exp"), rs.getInt("imunEgnor"), rs.getInt("strangeTime"),
         Boolean.valueOf(rs.getString("firstSpeed")).booleanValue(), Boolean.valueOf(rs.getString("secondSpeed")).booleanValue(), Boolean.valueOf(rs.getString("thirdSpeed")).booleanValue(), Boolean.valueOf(rs.getString("forthSpeed")).booleanValue());
   }

   private int weight;
   private int dg;
   private int er;
   private int me;
   private int reduction;
   private int reductionEgnor;
   private int reductionMagic;
   private int PVPDamage;
   private int PVPReduction;
   private int PVPReductionEgnor;
   private int PVPReductionMagic;
   private int PVPReductionMagicEgnor;
   private int toleranceSkill;
   private int toleranceSpirit;

   public L1TimeCollectionAblity(int flag, int sum, L1TimeCollectionBuffType buffType, int ac, int str, int con, int dex, int inti, int wis, int cha, int shortDamage, int shortHit, int shortCritical, int longDamage, int longHit, int longCritical, int spellpower, int magicHit, int magicCritical, int hp, int mp, int hpr, int mpr, int attrFire, int attrWater, int attrWind, int attrEarth, int attrAll, int mr, int weight, int dg, int er, int me, int reduction, int reductionEgnor, int reductionMagic, int pVPDamage, int pVPReduction, int pVPReductionEgnor, int pVPReductionMagic, int pVPReductionMagicEgnor, int toleranceSkill, int toleranceSpirit, int toleranceDragon, int toleranceFear, int toleranceAll, int hitupSkill, int hitupSpirit, int hitupDragon, int hitupFear, int hitupAll, int exp, int imunEgnor, int strangeTime, boolean firstSpeed, boolean secondSpeed, boolean thirdSpeed, boolean forthSpeed) {
     this.flag = flag;
     this.sum = sum;
     this.buffType = buffType;
     this.ac = ac;
     this.str = str;
     this.con = con;
     this.dex = dex;
     this.inti = inti;
     this.wis = wis;
     this.cha = cha;
     this.shortDamage = shortDamage;
     this.shortHit = shortHit;
     this.shortCritical = shortCritical;
     this.longDamage = longDamage;
     this.longHit = longHit;
     this.longCritical = longCritical;
     this.spellpower = spellpower;
     this.magicHit = magicHit;
     this.magicCritical = magicCritical;
     this.hp = hp;
     this.mp = mp;
     this.hpr = hpr;
     this.mpr = mpr;
     this.attrFire = attrFire;
     this.attrWater = attrWater;
     this.attrWind = attrWind;
     this.attrEarth = attrEarth;
     this.attrAll = attrAll;
     this.mr = mr;
     this.weight = weight;
     this.dg = dg;
     this.er = er;
     this.me = me;
     this.reduction = reduction;
     this.reductionEgnor = reductionEgnor;
     this.reductionMagic = reductionMagic;
     this.PVPDamage = pVPDamage;
     this.PVPReduction = pVPReduction;
     this.PVPReductionEgnor = pVPReductionEgnor;
     this.PVPReductionMagic = pVPReductionMagic;
     this.PVPReductionMagicEgnor = pVPReductionMagicEgnor;
     this.toleranceSkill = toleranceSkill;
     this.toleranceSpirit = toleranceSpirit;
     this.toleranceDragon = toleranceDragon;
     this.toleranceFear = toleranceFear;
     this.toleranceAll = toleranceAll;
     this.hitupSkill = hitupSkill;
     this.hitupSpirit = hitupSpirit;
     this.hitupDragon = hitupDragon;
     this.hitupFear = hitupFear;
     this.hitupAll = hitupAll;
     this.exp = exp;
     this.imunEgnor = imunEgnor;
     this.strangeTime = strangeTime;
     this.firstSpeed = firstSpeed;
     this.secondSpeed = secondSpeed;
     this.thirdSpeed = thirdSpeed;
     this.forthSpeed = forthSpeed;
   }
   private int toleranceDragon; private int toleranceFear; private int toleranceAll; private int hitupSkill; private int hitupSpirit; private int hitupDragon; private int hitupFear; private int hitupAll; private int exp; private int imunEgnor; private int strangeTime; private boolean firstSpeed; private boolean secondSpeed; private boolean thirdSpeed; private boolean forthSpeed;
   public int getFlag() {
     return this.flag;
   }
   public int getSum() {
     return this.sum;
   }
   public L1TimeCollectionBuffType getBuffType() {
     return this.buffType;
   }

   public void ablity(L1PcInstance owner, boolean active) {
     int value = active ? 1 : -1;



     if (this.ac != 0) owner.getAC().addAc(-this.ac * value);


     if (this.str > 0) owner.getAbility().addAddedStr((byte)this.str * value);

     if (this.con > 0) owner.getAbility().addAddedCon((byte)this.con * value);

     if (this.dex > 0) owner.getAbility().addAddedDex((byte)this.dex * value);

     if (this.inti > 0) owner.getAbility().addAddedInt((byte)this.inti * value);

     if (this.wis > 0) owner.getAbility().addAddedWis((byte)this.wis * value);

     if (this.cha > 0) owner.getAbility().addAddedCha((byte)this.cha * value);

     if (this.shortDamage > 0) owner.addDmgup(this.shortDamage * value);

     if (this.shortHit > 0) owner.addHitup(this.shortHit * value);

     if (this.shortCritical > 0) owner.add_melee_critical_rate(this.shortCritical * value);

     if (this.longDamage > 0) owner.addBowDmgup(this.longDamage * value);

     if (this.longHit > 0) owner.addBowHitup(this.longHit * value);

     if (this.longCritical > 0) owner.add_missile_critical_rate(this.longCritical * value);

     if (this.spellpower > 0) owner.getAbility().addSp(this.spellpower * value);

     if (this.magicHit > 0) owner.addBaseMagicHitUp(this.magicHit * value);

     if (this.magicCritical > 0) owner.addBaseMagicCritical(this.magicCritical * value);

     if (this.hp > 0) owner.addMaxHp(this.hp * value);
     if (this.mp > 0) owner.addMaxMp(this.mp * value);
     if (this.hpr > 0) owner.addHpr(this.hpr * value);
     if (this.mpr > 0) owner.addMpr(this.mpr * value);


     if (this.attrFire > 0) owner.getResistance().addFire(this.attrFire * value);

     if (this.attrWater > 0) owner.getResistance().addWater(this.attrWater * value);

     if (this.attrWind > 0) owner.getResistance().addWind(this.attrWind * value);

     if (this.attrEarth > 0) owner.getResistance().addEarth(this.attrEarth * value);

     if (this.attrAll > 0) owner.getResistance().addAllNaturalResistance(this.attrAll * value);


     if (this.mr > 0) owner.addMr(this.mr * value);
     if (this.weight > 0) owner.addWeightReduction(this.weight * value);

     if (this.dg > 0) owner.addDg(this.dg * value);

     if (this.er > 0) owner.addEffectedER(this.er * value);

     if (this.me > 0) owner.addMagicDodgeProbability(this.me * value);

     if (this.reduction > 0) owner.addDamageReduction(this.reduction * value);

     if (this.reductionEgnor > 0) owner.addDamageReductionIgnore(this.reductionEgnor * value);



     if (this.PVPDamage > 0) owner.getResistance().addPVPweaponTotalDamage(this.PVPDamage * value);

     if (this.PVPReduction > 0) owner.getResistance().addcalcPcDefense(this.PVPReduction * value);

     if (this.PVPReductionEgnor > 0) owner.add_pvp_dmg_ignore(this.PVPReductionEgnor * value);

     if (this.PVPReductionMagicEgnor > 0) owner.add_pvp_mdmg_ignore(this.PVPReductionMagicEgnor * value);

     if (this.toleranceSkill > 0) owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, this.toleranceSkill * value);

     if (this.toleranceSpirit > 0) owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, this.toleranceSpirit * value);

     if (this.toleranceDragon > 0) owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, this.toleranceDragon * value);

     if (this.toleranceFear > 0) owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, this.toleranceFear * value);

     if (this.toleranceAll > 0) owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, this.toleranceAll * value);

     if (this.hitupSkill > 0) owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, this.hitupSkill * value);

     if (this.hitupSpirit > 0) owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, this.hitupSpirit * value);

     if (this.hitupDragon > 0) owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, this.hitupDragon * value);

     if (this.hitupFear > 0) owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, this.hitupFear * value);

     if (this.hitupAll > 0) owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, this.hitupAll * value);

     if (this.exp > 0) owner.add_item_exp_bonus((this.exp * value));

     if (this.imunEgnor > 0) owner.add_immune_ignore(this.imunEgnor * value);


     owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(owner), true);
     owner.sendPackets((ServerBasePacket)new S_OwnCharStatus(owner), true);
     owner.sendPackets((ServerBasePacket)new S_SPMR(owner), true);
     owner.sendPackets((ServerBasePacket)new S_HPUpdate(owner));
     owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(owner));
     SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(owner);
   }


   public String toString() {
     StringBuilder sb = new StringBuilder();
     sb.append("flag: ").append(this.flag).append("\r\n");
     sb.append("sum: ").append(this.sum).append("\r\n");
     sb.append("buffType: ").append(this.buffType.getName()).append("\r\n");
     return sb.toString();
   }
 }


