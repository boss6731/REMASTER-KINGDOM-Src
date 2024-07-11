 package l1j.server.server.model;

 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.server.model.Instance.L1PcInstance;



 class defense
   implements L1ArmorSetEffect
 {
   private final int _defense_water;
   private final int _defense_earth;
   private final int _defense_wind;
   private final int _defense_fire;
   private final int _defense_all;
   private final int _ability_resis;
   private final int _spirit_resis;
   private final int _dragonS_resis;
   private final int _fear_resis;
   private final int _all_resis;

   public defense(int defense_water, int defense_earth, int defense_wind, int defense_fire, int defense_all, int ability_resis, int spirit_resis, int dragonS_resis, int fear_resis, int all_resis) {
     this._defense_water = defense_water;
     this._defense_earth = defense_earth;
     this._defense_wind = defense_wind;
     this._defense_fire = defense_fire;
     this._defense_all = defense_all;
     this._ability_resis = ability_resis;
     this._spirit_resis = spirit_resis;
     this._dragonS_resis = dragonS_resis;
     this._fear_resis = fear_resis;
     this._all_resis = all_resis;
   }

   public void giveEffect(L1PcInstance pc) {
     pc.getResistance().addWater(this._defense_water);
     pc.getResistance().addEarth(this._defense_earth);
     pc.getResistance().addWind(this._defense_wind);
     pc.getResistance().addFire(this._defense_fire);
     pc.getResistance().addAllNaturalResistance(this._defense_all);
     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, this._ability_resis);
     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, this._spirit_resis);
     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, this._dragonS_resis);
     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, this._fear_resis);
     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, this._all_resis);
     SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
   }

   public void cancelEffect(L1PcInstance pc) {
     pc.getResistance().addWater(-this._defense_water);
     pc.getResistance().addEarth(-this._defense_earth);
     pc.getResistance().addWind(-this._defense_wind);
     pc.getResistance().addFire(-this._defense_fire);
     pc.getResistance().addAllNaturalResistance(-this._defense_all);
     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, -this._ability_resis);
     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, -this._spirit_resis);
     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, -this._dragonS_resis);
     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, -this._fear_resis);
     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -this._all_resis);
     SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
   }
 }


