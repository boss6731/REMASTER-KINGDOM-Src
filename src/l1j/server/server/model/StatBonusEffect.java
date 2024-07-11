 package l1j.server.server.model;

 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SPMR;
 import l1j.server.server.serverpackets.ServerBasePacket;


 class StatBonusEffect
         implements L1ArmorSetEffect
 {
     private final int _str;
     private final int _dex;
     private final int _con;
     private final int _wis;
     private final int _cha;
     private final int _intl;
     private final int _sp;
     private final int _melee_damage;
     private final int _melee_hit;
     private final int _missile_damage;
     private final int _missile_hit;
     private final int _magic_hit_up;
     private final int _regist_calcPcDefense;
     private final int _regist_PVPweaponTotalDamage;
     private final int _ability_pierce;
     private final int _spirit_pierce;
     private final int _dragonS_pierce;
     private final int _fear_pierce;
     private final int _all_pierce;

     public StatBonusEffect(int str, int dex, int con, int wis, int cha, int intl, int sp, int melee_damage, int melee_hit, int missile_damage, int missile_hit, int magic_hit_up, int regist_calcPcDefense, int regist_PVPweaponTotalDamage, int ability_pierce, int spirit_pierce, int dragonS_pierce, int fear_pierce, int all_pierce) {
         this._str = str;
         this._dex = dex;
         this._con = con;
         this._wis = wis;
         this._cha = cha;
         this._intl = intl;
         this._sp = sp;
         this._melee_damage = melee_damage;
         this._melee_hit = melee_hit;
         this._missile_damage = missile_damage;
         this._missile_hit = missile_hit;
         this._magic_hit_up = magic_hit_up;
         this._regist_calcPcDefense = regist_calcPcDefense;
         this._regist_PVPweaponTotalDamage = regist_PVPweaponTotalDamage;
         this._ability_pierce = ability_pierce;
         this._spirit_pierce = spirit_pierce;
         this._dragonS_pierce = dragonS_pierce;
         this._fear_pierce = fear_pierce;
         this._all_pierce = all_pierce;
     }



     public void giveEffect(L1PcInstance pc) {
         pc.getAbility().addAddedStr((byte)this._str);
         pc.getAbility().addAddedDex((byte)this._dex);
         pc.getAbility().addAddedCon((byte)this._con);
         pc.getAbility().addAddedWis((byte)this._wis);
         pc.getAbility().addAddedCha((byte)this._cha);
         pc.getAbility().addAddedInt((byte)this._intl);
         pc.getAbility().addSp(this._sp);
         if (this._sp != 0) {
             pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
         }
         pc.addDmgupByArmor(this._melee_damage);
         pc.addHitup(this._melee_hit);
         pc.addBowDmgupByArmor(this._missile_damage);
         pc.addBowHitup(this._missile_hit);
         pc.addBaseMagicHitUp(this._magic_hit_up);
         pc.getResistance().addcalcPcDefense(this._regist_calcPcDefense);
         pc.getResistance().addPVPweaponTotalDamage(this._regist_PVPweaponTotalDamage);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, this._ability_pierce);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, this._spirit_pierce);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, this._dragonS_pierce);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, this._fear_pierce);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, this._all_pierce);
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
     }



     public void cancelEffect(L1PcInstance pc) {
         pc.getAbility().addAddedStr((byte)-this._str);
         pc.getAbility().addAddedDex((byte)-this._dex);
         pc.getAbility().addAddedCon((byte)-this._con);
         pc.getAbility().addAddedWis((byte)-this._wis);
         pc.getAbility().addAddedCha((byte)-this._cha);
         pc.getAbility().addAddedInt((byte)-this._intl);
         pc.getAbility().addSp(-this._sp);
         if (this._sp != 0)
             pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
         pc.addDmgupByArmor(-this._melee_damage);
         pc.addHitup(-this._melee_hit);
         pc.addBowDmgupByArmor(-this._missile_damage);
         pc.addBowHitup(-this._missile_hit);
         pc.addBaseMagicHitUp(-this._magic_hit_up);
         pc.getResistance().addcalcPcDefense(-this._regist_calcPcDefense);
         pc.getResistance().addPVPweaponTotalDamage(-this._regist_PVPweaponTotalDamage);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, -this._ability_pierce);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, -this._spirit_pierce);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, -this._dragonS_pierce);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, -this._fear_pierce);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -this._all_pierce);
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
     }
 }


