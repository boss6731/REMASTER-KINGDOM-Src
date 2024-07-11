 package l1j.server.server.templates;

 import java.util.HashMap;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.server.model.Instance.L1PcInstance;




 public class L1Armor
   extends L1Item
 {
   private static final long serialVersionUID = 1L;
   private int _ac = 0;
   private int _weightReduction = 0;
   private int _HitRate = 0;
   private int _DmgRate = 0;
   private int _bowHitRate = 0;
   private int _BowDmgRate = 0;
   private int _defense_water = 0;
   private int _defense_wind = 0;
   private int _defense_fire = 0;
   private int _defense_earth = 0;
   private int _defense_all = 0;
   private int _regist_calcPcDefense = 0; private HashMap<Integer, Integer> m_itemResistance; private HashMap<Integer, Integer> m_itemPierce;
   private int _regist_PVPweaponTotalDamage = 0; private int m_magicHitup;

   public int get_ac() {
     return this._ac; } public void set_ac(int i) {
     this._ac = i;
   }

   public int getWeightReduction() { return this._weightReduction; } public void setWeightReduction(int i) {
     this._weightReduction = i;
   }

   public int getHitRate() {
     return this._HitRate; } public void setHitRate(int i) {
     this._HitRate = i;
   }

   public int getDmgRate() { return this._DmgRate; } public void setDmgRate(int i) {
     this._DmgRate = i;
   }

   public int getBowHitRate() { return this._bowHitRate; } public void setBowHitRate(int i) {
     this._bowHitRate = i;
   }

   public int getBowDmgRate() { return this._BowDmgRate; } public void setBowDmgRate(int i) {
     this._BowDmgRate = i;
   }

   public int get_defense_water() { return this._defense_water; } public void set_defense_water(int i) {
     this._defense_water = i;
   }

   public int get_defense_wind() { return this._defense_wind; } public void set_defense_wind(int i) {
     this._defense_wind = i;
   }

   public int get_defense_fire() { return this._defense_fire; } public void set_defense_fire(int i) {
     this._defense_fire = i;
   }

   public int get_defense_earth() { return this._defense_earth; } public void set_defense_earth(int i) {
     this._defense_earth = i;
   }

   public int get_defense_all() { return this._defense_all; } public void set_defense_all(int i) {
     this._defense_all = i;
   }

   public int get_regist_calcPcDefense() { return this._regist_calcPcDefense; } public void set_regist_calcPcDefense(int i) {
     this._regist_calcPcDefense = i;
   }

   public int get_regist_PVPweaponTotalDamage() { return this._regist_PVPweaponTotalDamage; } public void set_regist_PVPweaponTotalDamage(int i) {
     this._regist_PVPweaponTotalDamage = i;
   }







   public void setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value) {
     if (value > 0) {
       if (this.m_itemResistance == null)
         this.m_itemResistance = new HashMap<>(4);
       this.m_itemResistance.put(Integer.valueOf(kind.toInt()), Integer.valueOf(value));
     }
   }

   public int getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind) {
     if (this.m_itemResistance == null || !this.m_itemResistance.containsKey(Integer.valueOf(kind.toInt()))) {
       return 0;
     }
     return ((Integer)this.m_itemResistance.get(Integer.valueOf(kind.toInt()))).intValue();
   }


   public void setSpecialResistanceMap(HashMap<Integer, Integer> itemResistance) {
     this.m_itemResistance = itemResistance;
   }

   public HashMap<Integer, Integer> getSpecialResistanceMap() {
     return this.m_itemResistance;
   }

   public void setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value) {
     if (value > 0) {
       if (this.m_itemPierce == null)
         this.m_itemPierce = new HashMap<>(4);
       this.m_itemPierce.put(Integer.valueOf(kind.toInt()), Integer.valueOf(value));
     }
   }

   public int getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind) {
     if (this.m_itemPierce == null || !this.m_itemPierce.containsKey(Integer.valueOf(kind.toInt()))) {
       return 0;
     }
     return ((Integer)this.m_itemPierce.get(Integer.valueOf(kind.toInt()))).intValue();
   }

   public void setSpecialPierceMap(HashMap<Integer, Integer> itemPierce) {
     this.m_itemPierce = itemPierce;
   }

   public HashMap<Integer, Integer> getSpecialPierceMap() {
     return this.m_itemPierce;
   }

   public void equipmentItem(L1PcInstance pc, boolean isEquipped) {
     int signed = isEquipped ? 1 : -1;
     if (this.m_itemResistance != null)
       for (Integer key : this.m_itemResistance.keySet()) {
         pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.fromInt(key.intValue()), ((Integer)this.m_itemResistance.get(key)).intValue() * signed);
       }
     if (this.m_itemPierce != null) {
       for (Integer key : this.m_itemPierce.keySet()) {
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.fromInt(key.intValue()), ((Integer)this.m_itemPierce.get(key)).intValue() * signed);
       }
     }
   }


   public int getMagicHitup() {
     return this.m_magicHitup;
   }

   public void setMagicHitup(int i) {
     this.m_magicHitup = i;
   }
 }


