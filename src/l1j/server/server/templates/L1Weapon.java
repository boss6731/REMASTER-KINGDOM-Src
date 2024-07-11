 package l1j.server.server.templates;

 import java.util.HashMap;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.server.model.Instance.L1PcInstance;

























 public class L1Weapon
   extends L1Item
 {
   private static final long serialVersionUID = 1L;
   private int _doubleDmgChance;

   public int getDoubleDmgChance() {
     return this._doubleDmgChance;
   }

   public void setDoubleDmgChance(int i) {
     this._doubleDmgChance = i;
   }

   private int _canbedmg = 0;
   private HashMap<Integer, Integer> m_itemResistance;

   public int get_canbedmg() {
     return this._canbedmg;
   }
   private HashMap<Integer, Integer> m_itemPierce; private int m_magicHitup;
   public void set_canbedmg(int i) {
     this._canbedmg = i;
   }


   public boolean isTwohandedWeapon() {
     int weapon_type = getType();

     boolean bool = (weapon_type == 3 || weapon_type == 4 || weapon_type == 5 || weapon_type == 11 || weapon_type == 12 || weapon_type == 15 || weapon_type == 16 || weapon_type == 18);




     return bool;
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


