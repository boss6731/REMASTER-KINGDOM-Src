 package l1j.server.server.model.Instance;

 import java.io.IOException;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Map;
 import l1j.server.BuyLimitSystem.BuyLimitSystem;
 import l1j.server.BuyLimitSystem.BuyLimitSystemAccount;
 import l1j.server.BuyLimitSystem.BuyLimitSystemAccountTable;
 import l1j.server.BuyLimitSystem.BuyLimitSystemCharacter;
 import l1j.server.BuyLimitSystem.BuyLimitSystemCharacterTable;
 import l1j.server.Config;
 import l1j.server.InvenBonusItem.InvenBonusItemInfo;
 import l1j.server.InvenBonusItem.InvenBonusItemLoader;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJCTSystem.Loader.MJCTSystemLoader;
 import l1j.server.MJCTSystem.MJCTObject;
 import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
 import l1j.server.MJExpAmpSystem.MJItemExpBonus;
 import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
 import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.clientpackets.ClientBasePacket;
 import l1j.server.server.datatables.ArmorSetTable;
 import l1j.server.server.datatables.PetTable;
 import l1j.server.server.datatables.ShopBuyLimitInfo;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.model.L1ArmorSet;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1EquipmentTimer;
 import l1j.server.server.model.L1ItemOwnerTimer;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.item.function.L1EnchantBonus;
 import l1j.server.server.model.item.function.L1EtcItemViewByte;
 import l1j.server.server.model.item.function.L1MagicDoll;
 import l1j.server.server.model.item.smelting.SmeltingScrollInfo;
 import l1j.server.server.model.item.smelting.SmeltingScrollLoader;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1ArmorSets;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.templates.L1ItemBookMark;
 import l1j.server.server.templates.L1Pet;
 import l1j.server.server.templates.L1Skills;
 import l1j.server.server.templates.ShopBuyLimit;
 import l1j.server.server.templates.eShopBuyLimitType;
 import l1j.server.server.utils.CommonUtil;
 import l1j.server.server.utils.IntRange;
 import l1j.server.server.utils.ItemPresentOutStream;
 import l1j.server.server.utils.MJBytesOutputStream;
 import l1j.server.server.utils.SQLUtil;



 public class L1ItemInstance
   extends L1Object
 {
   public enum ItemTradableStatus
   {
     tradable,




     equipped,




     seal,




     imprinted,




     oblivion,




     summonedPet,




     summonedDoll,




     nonTradable;
   }

   public static ItemTradableStatus tradableItem(L1PcInstance owner, L1ItemInstance item) {
     if (item.isEquipped()) {
       return ItemTradableStatus.equipped;
     }
     if (item.getBless() >= 128) {
       return ItemTradableStatus.seal;
     }
     if (item.get_Carving() != 0) {
       return ItemTradableStatus.imprinted;
     }
     if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
       return ItemTradableStatus.oblivion;
     }
     L1Item template = item.getItem();
     if (!template.isTradable()) {
       return ItemTradableStatus.nonTradable;
     }
     if (owner.getPetList().containsKey(Integer.valueOf(item.getId()))) {
       return ItemTradableStatus.summonedPet;
     }
     L1DollInstance doll = owner.getMagicDoll();
     if (doll != null && doll.getItemObjId() == item.getId()) {
       return ItemTradableStatus.summonedDoll;
     }

     return ItemTradableStatus.tradable;
   }

   public static String to_simple_description(L1ItemInstance item) {
     return to_simple_description(item.getName(), item.getBless(), item.getEnchantLevel(), item.getAttrEnchantLevel(), item.get_Doll_Bonus_Level(), item.get_Doll_Bonus_Value(), item.get_bless_level(), item.get_item_level(), item.getBlessType(), item.getBlessTypeValue(), item.getCount());
   }

   public static String to_simple_description(String name, int bless, int enchant, int elemental, int level) {
     StringBuilder sb = new StringBuilder(name.length() + 32);

     sb.append(get_attribute_enchant_description(elemental));
     sb.append(get_enchant_description(enchant));
     sb.append(name);
     sb.append(get_level_description(level));
     return sb.toString();
   }


   public static String to_simple_description(String name, int bless, int enchant, int elemental, int dollbonuslevel, int dollbonusvalue, int bless_level, int BlessType, int BlessTypeValue, int level, int count) {
     StringBuilder sb = new StringBuilder(name.length() + 32);

     sb.append(get_attribute_enchant_description(elemental));
     sb.append(get_enchant_description(enchant));
     sb.append(name);







     if (count > 1) {
       sb.append(" (").append(count).append(")");
     }
     return sb.toString();
   }

     public static String get_level_description(int level) {
         switch (level) {
             case 1:
                 return "[1級]"; // 返回 "[1級]"
             case 2:
                 return "[2級]"; // 返回 "[2級]"
             case 3:
                 return "[3級]"; // 返回 "[3級]"
             case 4:
                 return "[4級]"; // 返回 "[4級]"
             case 5:
                 return "[5級]"; // 返回 "[5級]"
         }
         return ""; // 如果等級不在1到5之間，返回空字符串
     }

   public static String get_enchant_description(int enchant) {
     if (enchant > 0)
       return String.format("+%d ", new Object[] { Integer.valueOf(enchant) });
     if (enchant < 0)
       return String.format("-%d ", new Object[] { Integer.valueOf(enchant) });
     return "";
   }

     public static String get_blessed_description(int bless) {
         switch (bless) {
             case 0:
                 return "被祝福的 "; // "被祝福的 "
             case 2:
                 return "被詛咒的 "; // "被詛咒的 "
         }
         return ""; // 如果不是0或2, 返回空字符串
     }

     public static String get_bless_level(int bless) {
         switch (bless) {
             case 1:
             case 2:
             case 3:
                 return "被祝福的 "; // "被祝福的 "
         }
         return ""; // 如果不是1, 2或3, 返回空字符串
     }

   private static final String[] elemental_descriptions = new String[] { "", "$6115", "$6116", "$6117", "$14361", "$14365", "$6118", "$6119", "$6120", "$14362", "$14366", "$6121", "$6122", "$6123", "$14363", "$14367", "$6124", "$6125", "$6126", "$14364", "$14368" };


   public static String get_attribute_enchant_description(int attrEnchantLevel) {
     return (attrEnchantLevel >= elemental_descriptions.length) ? "" : ((attrEnchantLevel < 0) ? "" : elemental_descriptions[attrEnchantLevel]);
   }

   private static final int[] _attrMask = new int[] { 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4 }; public static final int CHAOS_SPIRIT = 1; public static final int CORRUPT_SPIRIT = 2; public static final int BALLACAS_SPIRIT = 3; public static final int ANTARAS_SPIRIT = 4; public static final int LINDBIOR_SPIRIT = 5; public static final int PAPURION_SPIRIT = 6; public static final int DEATHKNIGHT_SPIRIT = 7; public static final int BAPPOMAT_SPIRIT = 8; public static final int BALLOG_SPIRIT = 9; public static final int ARES_SPIRIT = 10; private static final long serialVersionUID = 1L;

   public static int attrEnchantToElementalType(int attrEnchantLevel) {
     if (attrEnchantLevel < 0) {
       attrEnchantLevel = 0;
       System.out.println(String.format("[L1物品實例]：屬性附魔等級：%d 的數值存在。(character_items) 請確認！", new Object[] { Integer.valueOf(attrEnchantLevel) }));
     }
     return _attrMask[attrEnchantLevel];
   }

   public static int attrEnchantToElementalType(L1ItemInstance item) {
     return attrEnchantToElementalType(item.getAttrEnchantLevel());
   }

   public static int pureAttrEnchantLevel(int attrEnchantLevel) {
     return (attrEnchantLevel <= 0) ? attrEnchantLevel : (attrEnchantLevel - (attrEnchantToElementalType(attrEnchantLevel) - 1) * 5);
   }

   public static int pureAttrEnchantLevel(L1ItemInstance item) {
     return pureAttrEnchantLevel(item.getAttrEnchantLevel());
   }

   public static boolean equalsElement(L1ItemInstance item, int elementalType, int elementalValue) {
     int attr = item.getAttrEnchantLevel();
     int type = attrEnchantToElementalType(attr);
     if (type != elementalType) {
       return false;
     }
     int value = attr - (type - 1) * 5;
     return (value == elementalValue);
   }

   public static int calculateElementalEnchant(int elementalType, int elementalValue) {
     if (elementalType == 0 && elementalValue == 0)
       return 0;
     return (elementalType - 1) * 5 + elementalValue;
   }



   public boolean _isSecond = false;


   private int _count;


   private int _itemId;


   private int _itemDescId;


   private L1Item _item;


   private boolean _isEquipped = false;


   private int _enchantLevel;


   private int _attrenchantLevel;


   private boolean _isIdentified = false;


   private int _durability;


   private int _chargeCount;


   private int _specialEnchant;

   private int _remainingTime;

   private Timestamp _lastUsed = null;

   private Timestamp _endTime = null;


   private boolean _isPackage = false;


   private int bless;

   private int _lastWeight;

   private boolean _isDollOn = false;

   private final LastStatus _lastStatus = new LastStatus();

   private Map<Integer, EnchantTimer> _skillEffect = new HashMap<>();



   public L1PcInstance _cha;


   private long _itemdelay3;



   public L1ItemInstance(L1Item item, int count) {
     this();
     setItem(item);
     setCount(count);
   }

   public L1ItemInstance(L1Item item) {
     this(item, 1);
   }


   public void clickItem(L1Character cha, ClientBasePacket packet) {}


   public boolean isDollOn() {
     return this._isDollOn;
   }

   public void setDollOn(boolean DollOn) {
     this._isDollOn = DollOn;
   }



   public boolean isSpecialEnchantable() {
     return ((this._specialEnchant & 0xFF) == 1);
   }

   public void setSpecialEnchantable() {
     this._specialEnchant = 1;
   }

   public int getSpecialEnchant() {
     return this._specialEnchant;
   }

   public int getSpecialEnchant(int index) {
     return this._specialEnchant >> 8 * index & 0xFF;
   }

   public void setSpecialEnchant(int enchant) {
     this._specialEnchant = enchant;
   }

   public void setSpecialEnchant(int index, int enchant) {
     this._specialEnchant |= enchant << 8 * index;
   }

   public boolean isIdentified() {
     return this._isIdentified;
   }

   public void setIdentified(boolean identified) {
     this._isIdentified = identified;
   }

   public String getName() {
     return this._item.getName();
   }

   public int getCount() {
     return this._count;
   }

   public void setCount(int count) {
     this._count = count;
   }

   public boolean isEquipped() {
     return this._isEquipped;
   }

   public void setEquipped(boolean equipped) {
     this._isEquipped = equipped;
   }

   public L1Item getItem() {
     return this._item;
   }

   public void setItem(L1Item item) {
     this._item = item;
     this._itemId = item.getItemId();
   }

   public int getItemDescId() {
     return this._itemDescId;
   }

   public int getItemId() {
     return this._itemId;
   }

   public void setItemId(int itemId) {
     this._itemId = itemId;
   }

   public boolean isStackable() {
     return this._item.isStackable();
   }


   public void onAction(L1PcInstance player) {}


   public int getEnchantLevel() {
     return this._enchantLevel;
   }

   public void setEnchantLevel(int enchantLevel) {
     this._enchantLevel = enchantLevel;
   }

   public int getAttrEnchantLevel() {
     return this._attrenchantLevel;
   }

   public int getHitModifierByAttrEnchant() {
     if (getAttrEnchantLevel() == 0)
       return 0;
     if (getAttrEnchantLevel() % 3 == 0) {
       return 3;
     }

     return getAttrEnchantLevel() % 3;
   }

   public void setAttrEnchantLevel(int attrenchantLevel) {
     this._attrenchantLevel = attrenchantLevel;
   }

   public int get_gfxid() {
     return this._item.getGfxId();
   }

   public int get_durability() {
     return this._durability;
   }

   public int getChargeCount() {
     return this._chargeCount;
   }

   public void setChargeCount(int i) {
     this._chargeCount = i;
   }

   public int getRemainingTime() {
     return this._remainingTime;
   }

   public void setRemainingTime(int i) {
     this._remainingTime = i;
   }

   public void setLastUsed(Timestamp t) {
     this._lastUsed = t;
   }

   public Timestamp getLastUsed() {
     return this._lastUsed;
   }

   public long getLastUsedMillis() {
     return (this._lastUsed == null) ? 0L : this._lastUsed.getTime();
   }

   public int getBless() {
     return this.bless;
   }

   public void setBless(int i) {
     this.bless = i;
   }

   public int getLastWeight() {
     return this._lastWeight;
   }

   public void setLastWeight(int weight) {
     this._lastWeight = weight;
   }

   public Timestamp getEndTime() {
     return this._endTime;
   }

   public void setEndTime(Timestamp t) {
     this._endTime = t;
   }


   public boolean isPackage() {
     return this._isPackage;
   }

   public void setPackage(boolean _isPackage) {
     this._isPackage = _isPackage;
   }



   public long getItemdelay3() {
     return this._itemdelay3;
   }

   public void setItemdelay3(long itemdelay3) {
     this._itemdelay3 = itemdelay3;
   }

   public int getMr() {
     int result = this._item.get_mdef();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 222334 || getItemId() == 22228) {
       if (getBless() % 128 == 0) {
         result += (enchantLevel > 5) ? (enchantLevel - 5) : 0;
       }
       else if (enchantLevel == 9) {
         result++;
       }

       if (enchantLevel >= 8) {
         result += (enchantLevel - 7) * 2;
       }
     } else if (getItemId() == 900025 || getItemId() == 900184 || getItemId() == 900198) {
       result += (enchantLevel > 4) ? (enchantLevel - 1) : 0;
       if (enchantLevel > 7)
         result += (enchantLevel == 8) ? 1 : ((enchantLevel == 9) ? 3 : ((enchantLevel == 10) ? 5 : 0));
     } else if ((getItemId() == 22231 || getItemId() == 222339) && enchantLevel > 0) {
       if (getItemId() == 222339)
         result += 2;
       enchantLevel = (enchantLevel > 9) ? 9 : enchantLevel;
       if (enchantLevel == 8 && getBless() % 128 != 0) {
         result += 2;
       }
       if (enchantLevel == 9 && getBless() % 128 == 0) {
         result += 3;
       }
       if (getBless() % 128 == 0) {
         enchantLevel++;
       }
       result += (enchantLevel == 8) ? 13 : ((enchantLevel <= 0) ? 0 : ((enchantLevel <= 6) ? (enchantLevel + 2) : ((enchantLevel < 8) ? (1 + (enchantLevel - 4) * 3) : 18)));


     }
     else if (getItemId() == 900120 || getItemId() == 900222) {
       result += (enchantLevel > 4) ? (enchantLevel - 3) : 0;
     } else if (getItemId() >= 900081 && getItemId() <= 900083) {
       result += (enchantLevel > 5) ? (enchantLevel - 3) : 0;
       if (enchantLevel >= 7)
         result++;
       if (enchantLevel >= 8)
         result++;
     } else if (getItemId() == 900038 || getItemId() == 900054 || getItemId() == 900035 || getItemId() == 900072) {
       result += (enchantLevel > 4) ? ((enchantLevel - 4) * 4) : 0;
     } else if (getItemId() >= 900081 && getItemId() <= 900083) {
       result += (enchantLevel > 3) ? (enchantLevel - 3) : 0;
       if (getItemId() >= 900081 && getItemId() <= 900083)
         result *= 2;
     } else if (getItemId() >= 900124 && getItemId() <= 900126) {
       if (enchantLevel >= 5)
         result += 2 + (enchantLevel - 4) * 2;
     } else if (getItemId() == 900084 || getItemId() == 900196) {
       if (enchantLevel >= 6) {
         result += 3 + (enchantLevel - 6) * 2;

       }

     }
     else if (getItemId() == 900267) {
       result += 2 + (enchantLevel - 1) * 2;
     }
     if (getItem().getType2() == 2 && getItem().getType() == 9 && (
       getItemId() < 22224 || getItemId() > 22228) && (getItemId() < 222290 || getItemId() > 222291) && (
       getItemId() < 222330 || getItemId() > 222336) && getItemId() != 900195) {
       result += (enchantLevel > 5) ? (1 + (getEnchantLevel() - 6) * 2) : 0;
     }
     if (getItem().getType() == 8)
       if (enchantLevel == 5) {
         result++;
       } else if (enchantLevel == 6) {
         result += 3;
       } else if (enchantLevel == 7) {
         result += 5;
       } else if (enchantLevel == 8) {
         result += 7;
       } else if (enchantLevel == 9) {
         result += 10;
       } else if (enchantLevel == 10) {
         result += 12;
       }
     if (getItemId() == 900234 || getItemId() == 900235 || getItemId() == 900236 || getItemId() == 900237 || getItemId() == 900275 ||
       getItemId() == 900276 || getItemId() == 900277)
       if (enchantLevel >= 2 && enchantLevel <= 4) {
         result += 2 + (enchantLevel - 2) * 2;
       } else if (enchantLevel >= 5 && enchantLevel <= 8) {
         result += 10 + (enchantLevel - 5) * 2;
       }
     if (getItemId() == 900278) {
       if (enchantLevel >= 2 && enchantLevel <= 4) {
         result += 2 + (enchantLevel - 2) * 2;
       } else if (enchantLevel >= 5 && enchantLevel <= 8) {
         result += 11 + (enchantLevel - 5) * 2;
       }
     }
     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getMr(enchantLevel);
     }
     if (getBlessType() == 12) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int addhp() {
     int hp = this._item.get_addhp();

     return hp;
   }

   public int addmp() {
     int mp = this._item.get_addmp();
     return mp;
   }

   public int addsp() {
     int sp = this._item.get_addsp();
     return sp;
   }

   public void set_durability(int i) {
     if (i < 0) {
       i = 0;
     }

     if (i > 127) {
       i = 127;
     }
     this._durability = i;
   }

   public int getWeight() {
     if (getItem().getWeight() == 0) {
       return 0;
     }
     return Math.max(getCount() * getItem().getWeight() / 1000, 1);
   }

   public class LastStatus
   {
     public int count;
     public int itemId;
     public boolean isEquipped = false;
     public int enchantLevel;
     public boolean isIdentified = true;
     public int durability;
     public int chargeCount;
     public int remainingTime;
     public Timestamp lastUsed = null;
     public int bless;
     public int attrenchantLevel;
     public int specialEnchant;
     public int bless_level;
     public Timestamp endTime = null;
     public int item_level;
     public String town_name;
     public boolean _isSupportItem;
     public int carving;
     public int doll_bonus_level;
     public int doll_bonus_value;
     public int _blessType;
     public int _blessTypeValue;
     public int smeltingValue;
     public int smeltingitemid1;
     public int smeltingitemid2;

     public void updateAll() {
       this.count = L1ItemInstance.this.getCount();
       this.itemId = L1ItemInstance.this.getItemId();
       this.isEquipped = L1ItemInstance.this.isEquipped();
       this.isIdentified = L1ItemInstance.this.isIdentified();
       this.enchantLevel = L1ItemInstance.this.getEnchantLevel();
       this.durability = L1ItemInstance.this.get_durability();
       this.chargeCount = L1ItemInstance.this.getChargeCount();
       this.remainingTime = L1ItemInstance.this.getRemainingTime();
       this.lastUsed = L1ItemInstance.this.getLastUsed();
       this.bless = L1ItemInstance.this.getBless();
       this.attrenchantLevel = L1ItemInstance.this.getAttrEnchantLevel();
       this.specialEnchant = L1ItemInstance.this.getSpecialEnchant();
       this.endTime = L1ItemInstance.this.getEndTime();
       this.bless_level = L1ItemInstance.this.get_bless_level();
       this.item_level = L1ItemInstance.this.get_item_level();
       this.town_name = L1ItemInstance.this.getHotel_Town();
       this._isSupportItem = L1ItemInstance.this.isSupportItem();
       this.carving = L1ItemInstance.this.get_Carving();
       this.doll_bonus_level = L1ItemInstance.this.get_Doll_Bonus_Level();
       this.doll_bonus_value = L1ItemInstance.this.get_Doll_Bonus_Value();
       this._blessType = L1ItemInstance.this.getBlessType();
       this._blessTypeValue = L1ItemInstance.this.getBlessTypeValue();
       this.smeltingValue = L1ItemInstance.this.getSmeltingValue();
       this.smeltingitemid1 = L1ItemInstance.this.getSmeltingItemId1();
       this.smeltingitemid2 = L1ItemInstance.this.getSmeltingItemId2();
     }

     public void updateSpecialEnchant() {
       this.specialEnchant = L1ItemInstance.this.getSpecialEnchant();
     }

     public void updateCount() {
       this.count = L1ItemInstance.this.getCount();
     }

     public void updateItemId() {
       this.itemId = L1ItemInstance.this.getItemId();
     }

     public void updateEquipped() {
       this.isEquipped = L1ItemInstance.this.isEquipped();
     }

     public void updateIdentified() {
       this.isIdentified = L1ItemInstance.this.isIdentified();
     }

     public void updateEnchantLevel() {
       this.enchantLevel = L1ItemInstance.this.getEnchantLevel();
     }

     public void updateDuraility() {
       this.durability = L1ItemInstance.this.get_durability();
     }

     public void updateChargeCount() {
       this.chargeCount = L1ItemInstance.this.getChargeCount();
     }

     public void updateRemainingTime() {
       this.remainingTime = L1ItemInstance.this.getRemainingTime();
     }

     public void updateLastUsed() {
       this.lastUsed = L1ItemInstance.this.getLastUsed();
     }

     public void updateBless() {
       this.bless = L1ItemInstance.this.getBless();
     }

     public void updateAttrEnchantLevel() {
       this.attrenchantLevel = L1ItemInstance.this.getAttrEnchantLevel();
     }

     public void updateEndTime() {
       this.endTime = L1ItemInstance.this.getEndTime();
     }

     public void update_bless_level() {
       this.bless_level = L1ItemInstance.this.get_bless_level();
     }

     public void update_item_level() {
       this.item_level = L1ItemInstance.this.get_item_level();
     }

     public void update_town_name() {
       this.town_name = L1ItemInstance.this.getHotel_Town();
     }

     public void updateSupportItem() {
       this._isSupportItem = L1ItemInstance.this.isSupportItem();
     }

     public void update_Carving() {
       this.carving = L1ItemInstance.this.get_Carving();
     }

     public void update_Doll_Bonus_Level() {
       this.doll_bonus_level = L1ItemInstance.this.get_Doll_Bonus_Level();
     }

     public void update_Doll_Bonus_Value() {
       this.doll_bonus_value = L1ItemInstance.this.get_Doll_Bonus_Value();
     }
     public void updateBlessType() {
       this._blessType = L1ItemInstance.this.getBlessType();
     }

     public void updateBlessTypeValue() {
       this._blessTypeValue = L1ItemInstance.this.getBlessTypeValue();
     }

     public void updateSmeltingValue() {
       this.smeltingValue = L1ItemInstance.this.getSmeltingValue();
     }
     public void updateSmeltingItemId1() {
       this.smeltingitemid1 = L1ItemInstance.this.getSmeltingItemId1();
     }
     public void updateSmeltingItemId2() {
       this.smeltingitemid2 = L1ItemInstance.this.getSmeltingItemId2();
     }
     public void updateSmeltingKind1() {
       this.smeltingitemid1 = L1ItemInstance.this.getSmeltingKind1();
     }
     public void updateSmeltingKind2() {
       this.smeltingitemid2 = L1ItemInstance.this.getSmeltingKind2();
     }
     public void updateHalpasTime() {
       L1ItemInstance.this._halpas_time = L1ItemInstance.this.getHalpas_Time();
     }
     public void updateCantUnseal() {
       L1ItemInstance.this._cant_unseal = L1ItemInstance.this.get_Cantunseal();
     }
   }

   public LastStatus getLastStatus() {
     return this._lastStatus;
   }

   public int getRecordingColumns() {
     int column = 0;

     if (getCount() != this._lastStatus.count) {
       column += 16;
     }
     if (getItemId() != this._lastStatus.itemId) {
       column += 64;
     }
     if (isEquipped() != this._lastStatus.isEquipped) {
       column += 8;
     }
     if (getEnchantLevel() != this._lastStatus.enchantLevel) {
       column += 4;
     }
     if (get_durability() != this._lastStatus.durability) {
       column++;
     }
     if (getChargeCount() != this._lastStatus.chargeCount) {
       column += 128;
     }
     if (getLastUsed() != this._lastStatus.lastUsed) {
       column += 32;
     }
     if (isIdentified() != this._lastStatus.isIdentified) {
       column += 2;
     }
     if (getRemainingTime() != this._lastStatus.remainingTime) {
       column += 256;
     }
     if (getBless() != this._lastStatus.bless) {
       column += 512;
     }
     if (getAttrEnchantLevel() != this._lastStatus.attrenchantLevel) {
       column += 1024;
     }
     if (getSpecialEnchant() != this._lastStatus.specialEnchant) {
       column += 1024;
     }
     if (get_bless_level() != this._lastStatus.bless_level) {
       column += 512;
     }
     if (getEndTime() != this._lastStatus.endTime) {
       column += 256;
     }
     if (get_Carving() != this._lastStatus.carving) {
       column += 24;
     }
     if (get_Doll_Bonus_Level() != this._lastStatus.doll_bonus_level) {
       column += 8192;
     }
     if (get_Doll_Bonus_Value() != this._lastStatus.doll_bonus_value) {
       column += 16384;
     }
     if (getSmeltingValue() != this._lastStatus.smeltingValue) {
       column += 16384;
     }
     return column;
   }

   public String getNumberedViewName(int count) {
     StringBuilder name = new StringBuilder();
     if (isSpecialEnchantable()) {
       name.append("\\f3");
     }
     name.append(getNumberedName(count));
     int itemType2 = getItem().getType2();
     int itemId = getItem().getItemId();

     if (itemId == 40314 || itemId == 40316) {
       L1Pet pet = PetTable.getInstance().getTemplate(getId());
       if (pet != null) {
         name.append("[Lv.");
         name.append(pet.get_level());
         name.append(" ");
         name.append(pet.get_name());
         name.append("]HP");
         name.append(pet.get_hp());
       }
     }

     if (get_Carving() != 0) {
       name.append("(刻印)");
     }

     if (getItem().getType2() == 0 && getItem().getType() == 2) {
       if (isNowLighting()) {
         name.append(" ($10)");
       }
       if ((itemId == 40001 || itemId == 40002 || itemId == 7005) &&
         getRemainingTime() <= 0) {
         name.append(" ($11)");
       }
     }


     if (itemId == 40312) {
       name.append(" (" + getHotel_Town() + ")");
     }


       if (get_item_level() != 0) {
           switch (get_item_level()) {
               case 1:
                   name.append(" [1級]");
                   break;
               case 2:
                   name.append(" [2級]");
                   break;
               case 3:
                   name.append(" [3級]");
                   break;
               case 4:
                   name.append(" [4級]");
                   break;
               case 5:
                   name.append(" [5級]");
                   break;
           }
       }




     }
     if (isEquipped()) {
       if (itemType2 == 1) {
         name.append(" ($9)");
       } else if (itemType2 == 2) {
         name.append(" ($117)");
       } else if (itemType2 == 0 && getItem().getType() == 11) {
         name.append(" ($117)");
       }
     }











     return name.toString();
   }

   public String getViewName() {
     return getNumberedViewName(this._count);
   }

   public String getLogName() {
     return getNumberedName(this._count);
   }


   public String getNumberedName(int count) {
     StringBuilder name = new StringBuilder();

     if (isIdentified() && (
       getItem().getType2() == 1 || getItem().getType2() == 2)) {
       switch (getAttrEnchantLevel()) {
         case 1:
           name.append("$6115");
           break;
         case 2:
           name.append("$6116");
           break;
         case 3:
           name.append("$6117");
           break;
         case 4:
           name.append("$14361");
           break;
         case 5:
           name.append("$14365");
           break;
         case 6:
           name.append("$6118");
           break;
         case 7:
           name.append("$6119");
           break;
         case 8:
           name.append("$6120");
           break;
         case 9:
           name.append("$14362");
           break;
         case 10:
           name.append("$14366");
           break;
         case 11:
           name.append("$6121");
           break;
         case 12:
           name.append("$6122");
           break;
         case 13:
           name.append("$6123");
           break;
         case 14:
           name.append("$14363");
           break;
         case 15:
           name.append("$14367");
           break;
         case 16:
           name.append("$6124");
           break;
         case 17:
           name.append("$6125");
           break;
         case 18:
           name.append("$6126");
           break;
         case 19:
           name.append("$14364");
           break;
         case 20:
           name.append("$14368");
           break;
       }


       if (getEnchantLevel() >= 0) {
         name.append("+" + getEnchantLevel() + " ");
       } else if (getEnchantLevel() < 0) {
         name.append(String.valueOf(getEnchantLevel()) + " ");
       }
     }


       String real_name = getItem().getNameView(); // 獲取物品的視圖名稱
       if (getItem().getItemId() == 3000469 && MJCTSystemLoader.getInstance().get(getId()) != null) {
           // 如果物品ID為3000469且對應的MJCTObject不為空
           MJCTObject obj = MJCTSystemLoader.getInstance().get(getId());
           name.append("[").append(obj.name).append("]封印畫框"); // 在名稱後追加 "[obj.name]封印畫框"
       } else {
           // 否則
           if (MJString.isNullOrEmpty(real_name) || isIdentified()) {
               // 如果 real_name 為空或物品已鑑定
               real_name = this._item.getNameId(); // 使用物品的名稱ID
           }
           name.append(real_name); // 把 real_name 追加到 name 中
       }

     if (isSpecialEnchantable()) {
       for (int i = 1; i <= 3 &&
         getSpecialEnchant(i) != 0; i++) {


           switch (getSpecialEnchant(i)) {
               case 1:
                   name.append("[混沌] "); // "혼돈"
                   break;
               case 2:
                   name.append("[墮落] "); // "타락"
                   break;
               case 3:
                   name.append("[巴拉卡斯] "); // "발라카스"
                   break;
               case 4:
                   name.append("[安塔拉斯] "); // "안타라스"
                   break;
               case 5:
                   name.append("[琳德畢歐爾] "); // "린드비오르"
                   break;
               case 6:
                   name.append("[帕普里翁] "); // "파푸리온"
                   break;
               case 7:
                   name.append("[死亡騎士] "); // "데스나이트"
                   break;
               case 8:
                   name.append("[巴風特] "); // "바포메트"
                   break;
               case 9:
                   name.append("[巴洛克] "); // "발록"
                   break;
               case 10:
                   name.append("[阿瑞斯] "); // "아레스"
                   break;
           }
     if (isIdentified()) {
       if (getItem().getMaxChargeCount() > 0) {
         name.append(" (" + getChargeCount() + ")");
       }
       if (getItem().getItemId() == 20383) {
         name.append(" (" + getChargeCount() + ")");
       }
       if (getItem().getMaxUseTime() > 0 && getItem().getType2() != 0) {
         name.append(" [" + getRemainingTime() + "]");
       }
     }

     if (count > 1) {
       name.append(" (" + count + ")");
     }

     return name.toString();
   }


   public static int presentationCode = 0; private L1PcInstance _owner; private int _acByMagic; private int _dmgByMagic; private int _holyDmgByMagic; private int _hitByMagic; final int[][] repeatedSkills; private int _enchantmagic; private L1PcInstance _itemOwner; private L1EquipmentTimer _equipmentTimer; private boolean _isNowLighting; private int _DropMobId; private int _keyId; private boolean armor_set; private boolean main_set_armor; private int bless_level; private int item_level; private String _Hotel_Town; private int Carving; private int _Doll_bonus_value; private int _Doll_bonus_level; public ArrayList<L1ItemBookMark> _bookmarks; private int _openEffect;

   public byte[] getStatusBytes() {
     return getStatusBytes(null);
   }


   public byte[] getStatusBytes(L1PcInstance pc) {
     int itemType2 = getItem().getType2();
     int itemId = getItemId();
     int enchant = getEnchantLevel();
     int itemgfx = getItem().getGfxId();

     ItemPresentOutStream os = new ItemPresentOutStream();

     try {
       InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(itemId);
       if (info != null) {
         try {
           os.write(InvenBonusItemInfo.getItemView(info));
         } catch (IOException e) {
           e.printStackTrace();
         }
       }

       SmeltingScrollInfo sInfo = SmeltingScrollLoader.getInstance().getSmeltingScrollInfo(itemId);
       if (sInfo != null) {
         try {
           os.write(SmeltingScrollInfo.getItemView(sInfo));
         } catch (IOException e) {
           e.printStackTrace();
         }
       }



       L1EtcItemViewByte eiv = L1EtcItemViewByte.get(itemId);
       if (eiv != null) {
         try {
           os.write(L1EtcItemViewByte.getItemView(eiv, this));
         } catch (IOException e) {
           e.printStackTrace();
         }
       } else if (getItem().getUseType() == 73) {
         L1MagicDoll magicDoll = L1MagicDoll.get(itemId);
         if (magicDoll != null) {
           try {
             os.write(L1MagicDoll.getItemView(magicDoll, this));
           } catch (IOException e) {
             e.printStackTrace();
           }
         }
       }



       BuyLimitSystem.L1BuyLimitItems limit_item = BuyLimitSystem.getInstance().getLimitBuyType(itemId);
       if (pc != null &&
         limit_item != null) {
         int limit_type = limit_item.isLimitType();
         int current_time = (int)(System.currentTimeMillis() / 1000L);
         Timestamp start_time = new Timestamp(System.currentTimeMillis());
         start_time.setHours(limit_item.getStartTime());
         start_time.setMinutes(0);
         start_time.setSeconds(0);
         start_time.setNanos(0);

         Timestamp limit_time = new Timestamp(System.currentTimeMillis());
         limit_time.setHours(limit_item.getEndTime());
         limit_time.setMinutes(0);
         limit_time.setSeconds(0);
         limit_time.setNanos(0);

         if (limit_type == 1) {
           BuyLimitSystemAccount limit_account = BuyLimitSystemAccountTable.getInstance().getLimitTable(pc.getAccountName(), itemId);
           os.writeC(147);
           if (limit_account != null) {
             if ((current_time < start_time.getTime() / 1000L || current_time > limit_time.getTime() / 1000L) &&
               limit_account.getCount() < limit_item.getBuyCount()) {
               limit_account.setCount(limit_item.getBuyCount());
               BuyLimitSystemAccountTable.getInstance().updateLimitItem(pc.getAccountName(), this, limit_account.getCount(), false);
             }

             os.writeD(limit_account.getCount());
           } else {
             os.writeD(limit_item.getBuyCount());
           }
           os.writeD(start_time.getTime() / 1000L);
           os.writeD(0);
           os.writeD(limit_time.getTime() / 1000L);
           os.writeD(0);
         } else if (limit_type == 2) {
           BuyLimitSystemCharacter limit_char = BuyLimitSystemCharacterTable.getInstance().getLimitTable(pc, itemId);
           os.writeC(147);
           if (limit_char != null) {
             if ((current_time < start_time.getTime() / 1000L || current_time > limit_time.getTime() / 1000L) &&
               limit_char.getCount() < limit_item.getBuyCount()) {
               limit_char.setCount(limit_item.getBuyCount());
               BuyLimitSystemCharacterTable.getInstance().updateLimitItem(pc, this, limit_char.getCount(), false);
             }

             os.writeD(limit_char.getCount());
           } else {
             os.writeD(limit_item.getBuyCount());
           }
           os.writeD(start_time.getTime() / 1000L);
           os.writeD(0);
           os.writeD(limit_time.getTime() / 1000L);
           os.writeD(0);
         } else if (limit_type == 3) {
           BuyLimitSystemAccount limit_1day_account = BuyLimitSystemAccountTable.getInstance().getLimitTable(pc.getAccountName(), itemId);
           os.writeC(133);
           if (limit_1day_account != null) {
             if (limit_1day_account.getBuyTime() != null) {
               Timestamp limit_1day = new Timestamp(limit_1day_account.getBuyTime().getTime() + 86400000L);
               if (current_time > limit_1day.getTime() / 1000L &&
                 limit_1day_account.getCount() < limit_item.getBuyCount()) {
                 limit_1day_account.setCount(limit_item.getBuyCount());
                 limit_1day_account.setBuyTime(null);
                 BuyLimitSystemAccountTable.getInstance().updateLimitItem(pc.getAccountName(), this, limit_1day_account.getCount(), false);
               }
             }

             os.writeD(limit_1day_account.getCount());
           } else {
             os.writeD(limit_item.getBuyCount());
           }
         } else if (limit_type == 4) {
           BuyLimitSystemCharacter limit_1day_char = BuyLimitSystemCharacterTable.getInstance().getLimitTable(pc, itemId);

           if (limit_1day_char != null) {
             if (limit_1day_char.getBuyTime() != null) {
               Timestamp limit_1day = new Timestamp(limit_1day_char.getBuyTime().getTime() + 86400000L);
               if (current_time > limit_1day.getTime() / 1000L &&
                 limit_1day_char.getCount() < limit_item.getBuyCount()) {
                 limit_1day_char.setCount(limit_item.getBuyCount());
                 limit_1day_char.setBuyTime(null);
                 BuyLimitSystemCharacterTable.getInstance().updateLimitItem(pc, this, limit_1day_char.getCount(), false);
               }
             }

               if (limit_1day_char.getCount() > 0) {
                   os.writeStringS(String.format("\fI購買可用數量(1日): \aA%d個", new Object[] { Integer.valueOf(limit_1day_char.getCount()) }));
               } else {
                   os.writeStringS(String.format("\f3購買可用數量(1日): \aA%d個", new Object[] { Integer.valueOf(limit_1day_char.getCount()) }));
               }
           } else {
               os.writeStringS(String.format("\fI購買可用數量(1日): \aA%d個", new Object[] { Integer.valueOf(limit_item.getBuyCount()) }));
           }

         } else if (limit_type == 5) {
           BuyLimitSystemAccount limit_7day_account = BuyLimitSystemAccountTable.getInstance().getLimitTable(pc.getAccountName(), itemId);
           os.writeC(134);
           if (limit_7day_account != null) {
             if (limit_7day_account.getBuyTime() != null) {
               Timestamp limit_7day = new Timestamp(limit_7day_account.getBuyTime().getTime() + 604800000L);
               if (current_time > limit_7day.getTime() / 1000L &&
                 limit_7day_account.getCount() < limit_item.getBuyCount()) {
                 limit_7day_account.setCount(limit_item.getBuyCount());
                 limit_7day_account.setBuyTime(null);
                 BuyLimitSystemAccountTable.getInstance().updateLimitItem(pc.getAccountName(), this, limit_7day_account.getCount(), false);
               }
             }

             os.writeD(limit_7day_account.getCount());
           } else {
             os.writeD(limit_item.getBuyCount());
           }
         } else if (limit_type == 6) {
           BuyLimitSystemCharacter limit_7day_char = BuyLimitSystemCharacterTable.getInstance().getLimitTable(pc, itemId);

           if (limit_7day_char != null) {
             if (limit_7day_char.getBuyTime() != null) {
               Timestamp limit_7day = new Timestamp(limit_7day_char.getBuyTime().getTime() + 604800000L);
               if (current_time > limit_7day.getTime() / 1000L &&
                 limit_7day_char.getCount() < limit_item.getBuyCount()) {
                 limit_7day_char.setCount(limit_item.getBuyCount());
                 limit_7day_char.setBuyTime(null);
                 BuyLimitSystemCharacterTable.getInstance().updateLimitItem(pc, this, limit_7day_char.getCount(), false);
               }
             }

               if (limit_7day_char.getCount() > 0) {
                   os.writeStringS(String.format("\fI購買可用數量(1週): \aA%d個", new Object[] { Integer.valueOf(limit_7day_char.getCount()) }));
               } else {
                   os.writeStringS(String.format("\f3購買可用數量(1週): \aA%d個", new Object[] { Integer.valueOf(limit_7day_char.getCount()) }));
               }
           } else {
               os.writeStringS(String.format("\fI購買可用數量(1週): \aA%d個", new Object[] { Integer.valueOf(limit_item.getBuyCount()) }));
           }
         }



       if (itemType2 == 0) {
         int use_class; L1Skills skill; if (getItem().getItemId() == 30001111) {
           os.writeC(Config.ItemOption.writeC);
           if (Config.ItemOption.writeDBoolean) {
             os.writeD(1);
           } else if (Config.ItemOption.writeHBoolean) {
             os.writeH(1);
           } else if (Config.ItemOption.writeCBoolean) {
             os.writeC(1);
           }
         }
         switch (getItem().getType()) {
           case 2:
             os.writeC(22);
             os.writeH(getItem().getLightRange());
             os.writeC(getItem().getMaterial());
             os.writeD(getWeight());
             break;
           case 7:
             os.writeC(21);
             os.writeH(getItem().getFoodVolume());
             os.writeC(getItem().getMaterial());
             os.writeD(getWeight());
             break;
           case 0:
           case 15:
             os.writeC(7);
             os.writeH(255);

             if (getAttrDmg() != 0) {
               os.writeC(109);
               os.writeC(getAttrDmg());
             }

             if (getBowDmgModifier() != 0) {
               os.writeC(35);
               os.writeC(getBowDmgModifier());
             } else if (getBowDmgModifier() == 0 && isUndeadDmg()) {
               os.writeC(35);
               os.writeC(0);
             }
             if (getBowHitModifier() != 0) {
               os.writeC(24);
               os.writeC(getBowHitModifier());
             }

             if (isUndeadDmg()) {
               os.writeD(114);
               os.writeC(1);
             }

             os.writeC(23);
             os.writeC(getItem().getMaterial());
             os.writeD(getWeight());
             break;
           case 10:
             use_class = 0;
             use_class |= getItem().isUseRoyal() ? 1 : 0;
             use_class |= getItem().isUseKnight() ? 2 : 0;
             use_class |= getItem().isUseElf() ? 4 : 0;
             use_class |= getItem().isUseMage() ? 8 : 0;
             use_class |= getItem().isUseDarkelf() ? 16 : 0;
             use_class |= getItem().isUseDragonKnight() ? 32 : 0;
             use_class |= getItem().isUseBlackwizard() ? 64 : 0;
             use_class |= getItem().isUse전사() ? 128 : 0;
             use_class |= getItem().isUseFencer() ? 256 : 0;
             use_class |= getItem().isUseLancer() ? 512 : 0;
             if (use_class > 0) {
               os.writeC(7);
               os.writeH(use_class);
             }
             skill = SkillsTable.getInstance().findByItemName(getItem().getName());
             if (skill != null) {
               int skillLawful = skill.getLawful();
               int skillLevel = skill.getSkillLevel();
               if (skillLevel <= 10) {
                 os.writeC(77);
                 os.writeC(skillLevel - 1);
               } else if (skillLevel >= 17 && skillLevel <= 22) {
                 int level = IntRange.ensure(getItem().getMinLevel() / 10 - 1, 0, 4);
                 os.writeC(77);
                 os.writeC(level);
               } else {
                 os.writeC(79);
                 os.writeC(getItem().getMinLevel());
               }

               os.writeC(75);
               os.writeC((skillLawful == 0) ? 0 : ((skillLawful < 0) ? -1 : 1));
             }

             if (getItem().getMinLevel() != 0) {
               os.writeC(111);
               os.writeC(getItem().getMinLevel());
               os.writeH((getItem().getMaxLevel() == 0) ? 99 : getItem().getMaxLevel());
             }

             os.writeC(23);
             os.writeC(getItem().getMaterial());
             os.writeD(getWeight());
             break;
           case 8:
             if (getShortDmgModifier() != 0) {
               os.writeC(47);
               os.writeC(getShortDmgModifier());
             }

             if (getShortHitModifier() != 0) {
               os.writeC(48);
               os.writeC(getShortHitModifier());
             }

             if (getBowDmgModifier() != 0) {
               os.writeC(35);
               os.writeC(getBowDmgModifier());
             }

             if (getBowHitModifier() != 0) {
               os.writeC(24);
               os.writeC(getBowHitModifier());
             }

             if (getHp() != 0) {
               os.writeC(14);
               os.writeH(getHp());
             }

             if (getMp() != 0) {
               os.writeC(32);
               os.writeH(getMp());
             }

             if (getHpr() != 0) {
               os.writeC(37);
               os.writeC(getHpr());
             }

             if (getMpr() != 0) {
               os.writeC(38);
               os.writeC(getMpr());
             }

             if (getSp() != 0) {
               os.writeC(17);
               os.writeC(getSp());
             }

             if (getMagicHitRate() != 0) {
               os.writeC(40);
               os.writeC(getMagicHitRate());
             }

             if (getPvpReduction() != 0) {
               os.writeC(60);
               os.writeC(getPvpReduction());
             }

             if (getMagicDmgModifier() != 0) {
               os.writeC(194);
               os.writeD(getMagicDmgModifier());
             }

             if (getPVPDmgReducIgnore() != 0) {
               os.writeC(138);
               os.writeC(getPVPDmgReducIgnore());
             }

             if (getPVPMdmgReduction() != 0) {
               os.writeC(135);
               os.writeC(getPVPMdmgReduction());
             }


             if (getPVPMDmgReducIgnore() != 0) {
               os.writeC(139);
               os.writeC(getPVPMDmgReducIgnore());
             }

             if (getDG() != 0) {
               os.writeC(51);
               os.writeC(getDG());
             }

             if (getMagicDodge() != 0) {
               os.writeC(89);
               os.writeD(getMagicDodge());
             }

             if (getTotalER() != 0) {
               os.writeC(93);
               os.writeC(getTotalER());
             }

             if (getHpPercent() != 0) {
               os.writeC(149);
               os.writeC(getHpPercent());
             }

             if (getMpPercent() != 0) {
               os.writeC(150);
               os.writeC(getMpPercent());
             }

             if (getImmuneIgnore() != 0) {
               os.writeC(175);
               os.writeD(getImmuneIgnore());
             }


               if (getItem().getAttackDelayRate() != 0.0D) {
                   os.writeOptionA("攻擊速度", (int) getItem().getAttackDelayRate());
               }

               if (getItem().getMoveDelayRate() != 0.0D) {
                   os.writeOptionA("移動速度", (int) getItem().getMoveDelayRate());
               }

               os.writeC(23);
               os.writeC(getItem().getMaterial());
               os.writeD(getWeight());
               break;

             case 4100696:
                 os.writeC(39);
                 os.writeS("\fI使用等級:\aA 80");
                 os.writeC(39);
                 os.writeS("\fI重置角色的特殊屬性。");
                 os.writeC(39);
                 os.writeS("\fI初期化。");
                 break;

             default:
                 if (itemId > 10000000 && itemgfx == 6438) {
                     os.writeString("分配", 1.95D);
                 }
             if (getItem().getType() == 6 || getItem().getType() == 13) {
               int class_bit = 0;
               class_bit |= getItem().isUseRoyal() ? 1 : 0;
               class_bit |= getItem().isUseKnight() ? 2 : 0;
               class_bit |= getItem().isUseElf() ? 4 : 0;
               class_bit |= getItem().isUseMage() ? 8 : 0;
               class_bit |= getItem().isUseDarkelf() ? 16 : 0;
               class_bit |= getItem().isUseDragonKnight() ? 32 : 0;
               class_bit |= getItem().isUseBlackwizard() ? 64 : 0;
               class_bit |= getItem().isUse전사() ? 128 : 0;
               class_bit |= getItem().isUseFencer() ? 256 : 0;
               class_bit |= getItem().isUseLancer() ? 512 : 0;
               if (class_bit > 0) {
                 os.writeC(7);
                 os.writeH(class_bit);
               }
             }


             if (getItem().getItemId() == 3000469) {
               int Charlevel = 0;
               int Elixir = 0;
               int Class = 0;
               int Hp = 0;
               int Mp = 0;

               MJCTObject obj = MJCTSystemLoader.getInstance().get(getId());
               if (obj != null) {
                 Connection con = null;
                 PreparedStatement pstm = null;
                 ResultSet rs = null;
                 try {
                   con = L1DatabaseFactory.getInstance().getConnection();
                   pstm = con.prepareStatement("SELECT * FROM characters WHERE objid=?");
                   pstm.setInt(1, obj.charId);
                   rs = pstm.executeQuery();

                   if (!rs.next()) {
                     return null;
                   }

                   Charlevel = rs.getInt("level");
                   Elixir = rs.getInt("ElixirStatus");
                   Class = rs.getInt("Class");
                   Hp = rs.getInt("MaxHp");
                   Mp = rs.getInt("MaxMp");

                   if (obj != null) {
                     int bit = 0;
                     if (Class == 0 || Class == 1)
                       bit = 1;
                     if (Class == 20553 || Class == 48)
                       bit = 2;
                     if (Class == 138 || Class == 37)
                       bit = 4;
                     if (Class == 20278 || Class == 20279)
                       bit = 8;
                     if (Class == 2786 || Class == 2796)
                       bit = 16;
                     if (Class == 6658 || Class == 6661)
                       bit = 32;
                     if (Class == 6671 || Class == 6650)
                       bit = 64;
                     if (Class == 20567 || Class == 20577)
                       bit = 128;
                     if (Class == 18520 || Class == 18499)
                       bit = 256;
                     if (Class == 19296 || Class == 19299) {
                       bit = 512;
                     }
                       try {
                           os.writeAddMaxHP(Hp); // 寫入增加的最大HP
                           os.writeMaxMP(Mp); // 寫入最大MP
                           os.writeC(39); // 寫入控制碼39
                           os.writeS("\fI藥水消耗量: \aA" + Elixir + ""); // 寫入字符串 "藥水消耗量: " 和 Elixir 值
                           os.writeClass(bit); // 寫入職業
                           os.writeLevel(Charlevel); // 寫入等級
                       } catch (Exception e) {
                           e.printStackTrace(); // 捕捉異常並打印堆棧跟蹤
                       } finally {
                           SQLUtil.close(rs); // 關閉 ResultSet
                           SQLUtil.close(pstm); // 關閉 PreparedStatement
                           SQLUtil.close(con); // 關閉 Connection
                       }



             if (getEndTime() != null) {
               if (!isSupportItem() && getItem().isEndedTimeMessage() && getEndTime() != null) {




                 int remainSeconds = (int)((getEndTime().getTime() - 1483196400065L) / 1000L);
                 os.writeC(61);
                 os.writeD(remainSeconds * 6);
               } else if (isSupportItem() && !getItem().isEndedTimeMessage() && getEndTime() != null) {
                 os.writeC(72);
                 os.writeD((int)(getEndTime().getTime() / 1000L));
               } else {
                 os.writeC(112);
                 os.writeD(getEndTime().getTime() / 1000L);
               }
             }

             os.writeC(23);
             os.writeC(getItem().getMaterial());
             os.writeD(getWeight());
             break;
         }

         os.writeC(130);
         if (getItem().getWareHouseLimitType().toInt() == 2) {
           os.writeD(getItem().getWareHouseLimitType().toInt());
           if (getItem().getWareHouseLimitLevel() != 0) {



             os.writeC(148);
             os.writeC(getItem().getWareHouseLimitLevel());
           }
         } else {
           os.writeD((getItem().getWareHouseLimitType().toInt() != 7) ? 6 :
               getItem().getWareHouseLimitType().toInt());
         }
         ShopBuyLimit sli = ShopBuyLimitInfo.getInstance().getShopBuyLimit(getItemId());
         if (sli != null) {
           ShopBuyLimit char_sli_by_objid = null;
           ShopBuyLimit char_sli_by_account = null;
           if (this._cha != null) {
             char_sli_by_objid = ShopBuyLimitInfo.getInstance().findShopBuyLimitByObjid(this._cha.getId(),
                 getItem().getItemId());

             char_sli_by_account = ShopBuyLimitInfo.getInstance().findShopBuyLimitByAccount(this._cha.getAccount().getName(), getItem().getItemId());
           }

           if (this._cha != null && (char_sli_by_objid != null || char_sli_by_account != null)) {
             if (sli.get_type() == eShopBuyLimitType.CHARACTER_WEEK_LIMIT) {
               if (char_sli_by_objid != null) {
                 os.writeC(134);
                 os.writeD(char_sli_by_objid.get_count());
               }
             } else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) {
               if (char_sli_by_account != null) {
                 os.writeC(134);
                 os.writeD(char_sli_by_account.get_count());
               }
             } else if (sli.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
               if (char_sli_by_objid != null)
               {



                   if (char_sli_by_objid.get_count() == 0) {
                       os.writeC(39); // 寫入控制碼39
                       os.writeS("\f3每個角色購買(1日): " + char_sli_by_objid.get_count() + "個"); // 寫入字符串，表示每個角色每天可購買0個
                   } else {
                       os.writeC(39); // 寫入控制碼39
                       os.writeS("\fI每個角色購買(1日):\aA " + char_sli_by_objid.get_count() + "個"); // 寫入字符串，表示每個角色每天可購買一定數量
                   }
               }
             } else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT &&
               char_sli_by_account != null) {
               os.writeC(133);
               os.writeD(char_sli_by_account.get_count());
             }

           }
           else if (sli.get_type() == eShopBuyLimitType.CHARACTER_WEEK_LIMIT) {
             os.writeC(134);
             os.writeD(sli.get_count());
           } else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) {
             os.writeC(134);
             os.writeD(sli.get_count());
           } else if (sli.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {



               os.writeC(39); // 寫入控制碼39
               os.writeS("\fI每個角色購買(1日):\aA " + sli.get_count() + "個"); // 寫入字符串，表示每個角色每天可購買一定數量
           } else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT) {
               os.writeC(133); // 寫入控制碼133
               os.writeD(sli.get_count()); // 寫入購買數量
           }

         }
       } else if (itemType2 == 1 || itemType2 == 2) {

         if (itemType2 == 1) {
           os.writeC(1);
           os.writeC(getItem().getDmgSmall());
           os.writeC(getItem().getDmgLarge());
           os.writeC(getItem().getMaterial());
           os.writeD(getWeight());

           os.writeC(2);
           os.writeC(enchant);

           os.writeC(107);
           os.writeC(getEnchantDmgRate());
           os.writeC(getEnchantDmgRate());

           if (getItem().isTwohandedWeapon()) {
             os.writeC(4);
           }
         }
         else if (itemType2 == 2) {
           os.writeC(19);
           int ac = getAc();
           if (ac < 0) {
             ac = ac - ac - ac;
           } else if (ac > 0) {
             ac = ac - ac - ac;
           }
           os.writeC(ac);
           os.writeC(getItem().getMaterial());
           os.writeH(-1);
           os.writeD(getWeight());



           os.writeC(2);
           os.writeC(getAcByEnchantLevel());
         }


         if (get_durability() != 0) {
           os.writeC(3);
           os.writeC(get_durability());
         }


                       if (getEndTime() != null) {
                           os.writeC(39); // 寫入控制碼39
                           os.writeS("\fI附魔: \aA不可"); // 寫入字符串，表示附魔不可
                       } else if (getItem().get_safeenchant() > -1) {
                           os.writeC(169); // 寫入控制碼169
                           os.writeC(getItem().get_safeenchant()); // 寫入安全附魔等級
                       } else {
                           os.writeC(169); // 寫入控制碼169
                           os.writeC(getItem().get_safeenchant()); // 寫入安全附魔等級
                       }

                       if (getaddAc() != 0) {
                           if (get_bless_level() != 0) {
                               os.writeC(39); // 寫入控制碼39
                               os.writeS("\fI祝福選項: \aAAC +" + getaddAc()); // 寫入附加防禦值
                           } else {
                               os.writeC(56); // 寫入控制碼56
                               os.writeC(getaddAc()); // 寫入附加防禦值
                           }
                       }

                       if (getaddHp() != 0 && get_bless_level() != 0) {
                           os.writeC(39); // 寫入控制碼39
                           os.writeS("\fI祝福選項: \aAHP +" + getaddHp()); // 寫入附加HP值
                       }

                       if (get_bless_level() != 0) {
                           int type = getItem().getType();
                           if (getItem().getType2() == 1) {
                               if (type == 7 || type == 16 || type == 17) {
                                   os.writeC(39); // 寫入控制碼39
                                   os.writeS("\fI祝福選項: \aASP +" + get_bless_level()); // 寫入附加SP值
                               } else {
                                   os.writeC(39); // 寫入控制碼39
                                   os.writeS("\fI祝福選項: \aA傷害 +" + get_bless_level()); // 寫入附加傷害值
                               }
                           }
                       }


                   if (getItem().getType2() != 0 && get_item_level() != 0) { // 檢查物品類型和等級是否不為0
                     switch (get_item_level()) {
                       case 1:
                         os.writeC(73); // 寫入控制碼73
                         os.writeS("\fI特殊選項: \aA1級魔法"); // 寫入字符串 "特殊選項: 1級魔法"
                         break;
                       case 2:
                         os.writeC(73); // 寫入控制碼73
                         os.writeS("\fI特殊選項: \aA2級魔法"); // 寫入字符串 "特殊選項: 2級魔法"
                         break;
                       case 3:
                         os.writeC(73); // 寫入控制碼73
                         os.writeS("\fI特殊選項: \aA3級魔法"); // 寫入字符串 "特殊選項: 3級魔法"
                         break;
                       case 4:
                         os.writeC(73); // 寫入控制碼73
                         os.writeS("\fI特殊選項: \aA4級魔法"); // 寫入字符串 "特殊選項: 4級魔法"
                         break;
                     }
                   }
         }
         int bit = 0;
         bit |= getItem().isUseRoyal() ? 1 : 0;
         bit |= getItem().isUseKnight() ? 2 : 0;
         bit |= getItem().isUseElf() ? 4 : 0;
         bit |= getItem().isUseMage() ? 8 : 0;
         bit |= getItem().isUseDarkelf() ? 16 : 0;
         bit |= getItem().isUseDragonKnight() ? 32 : 0;
         bit |= getItem().isUseBlackwizard() ? 64 : 0;
         bit |= getItem().isUse전사() ? 128 : 0;
         bit |= getItem().isUseFencer() ? 256 : 0;
         bit |= getItem().isUseLancer() ? 512 : 0;
         os.writeC(7);
         os.writeH(bit);

         if (getDmgModifier() != 0) {
           os.writeC(47);
           os.writeC(getDmgModifier());
         }
         else if (isUndeadDmg()) {
           if (getItem().getType1() == 20 || getItem().getType1() == 62) {
             if (getBowDmgModifier() == 0) {
               os.writeC(35);
               os.writeC(0);
             }
           } else {
             os.writeC(47);
             os.writeC(0);
           }

         } else if (getEnchantLevel() != 0 && (
           getItem().getType1() == 20 || getItem().getType1() == 62) &&
           getBowDmgModifier() == 0) {
           os.writeC(35);
           os.writeC(0);
         }



         if (getHitModifier() != 0) {
           if (getItem().getType2() == 1) {
             os.writeC(48);
           } else {
             os.writeC(5);
           }
           os.writeC(getHitModifier());
         }
         else if (getEnchantLevel() >= 0 && getHitModifier() == 0) {
           if (getItem().getType1() != 20 && getItem().getType1() != 62) {
             os.writeC(48);
             os.writeC(0);
           } else {
             os.writeC(24);
             os.writeC(0);
           }
         }


         os.writeC(130);
         if (getItem().getWareHouseLimitType().toInt() == 2) {
           os.writeD(getItem().getWareHouseLimitType().toInt());
           if (getItem().getWareHouseLimitLevel() != 0) {



             os.writeC(148);
             os.writeC(getItem().getWareHouseLimitLevel());
           }
         } else {
           os.writeD((getItem().getWareHouseLimitType().toInt() != 7) ? 6 :
               getItem().getWareHouseLimitType().toInt());
         }
         ShopBuyLimit sli = ShopBuyLimitInfo.getInstance().getShopBuyLimit(getItemId());
         if (sli != null) {
           ShopBuyLimit char_sli_by_objid = null;
           ShopBuyLimit char_sli_by_account = null;
           if (this._cha != null) {
             char_sli_by_objid = ShopBuyLimitInfo.getInstance().findShopBuyLimitByObjid(this._cha.getId(),
                 getItem().getItemId());

             char_sli_by_account = ShopBuyLimitInfo.getInstance().findShopBuyLimitByAccount(this._cha.getAccount().getName(), getItem().getItemId());
           }

           if (this._cha != null && (char_sli_by_objid != null || char_sli_by_account != null)) {
             if (sli.get_type() == eShopBuyLimitType.CHARACTER_WEEK_LIMIT) {
               if (char_sli_by_objid != null) {
                 os.writeC(134);
                 os.writeD(char_sli_by_objid.get_count());
               }
             } else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) {
               if (char_sli_by_account != null) {
                 os.writeC(134);
                 os.writeD(char_sli_by_account.get_count());
               }
             } else if (sli.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
               if (char_sli_by_objid != null)
               {


                 if (char_sli_by_objid.get_count() == 0) {
                   os.writeC(39); // 寫入控制碼39
                   os.writeS("\f3每個角色購買(1日): " + char_sli_by_objid.get_count() + "個"); // 寫入字符串，表示每個角色每天可購買0個
                 } else {
                   os.writeC(39); // 寫入控制碼39
                   os.writeS("\fI每個角色購買(1日):\aA " + char_sli_by_objid.get_count() + "個"); // 寫入字符串，表示每個角色每天可購買一定數量
                 }
               }
             } else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT &&
               char_sli_by_account != null) {
               os.writeC(133);
               os.writeD(char_sli_by_account.get_count());
             }

           }
           else if (sli.get_type() == eShopBuyLimitType.CHARACTER_WEEK_LIMIT) {
             os.writeC(134);
             os.writeD(sli.get_count());
           } else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) {
             os.writeC(134);
             os.writeD(sli.get_count());
           } else if (sli.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {



             os.writeC(39); // 寫入控制碼39
             os.writeS("\fI每個角色購買(1日):\aA " + sli.get_count() + "個"); // 寫入字符串，表示每個角色每天可購買一定數量
           } else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT) {
             os.writeC(133); // 寫入控制碼133
             os.writeD(sli.get_count()); // 寫入購買數量
           }
         }


         if (getItem().getMinLevel() != 0) {
           os.writeC(111);
           os.writeC(getItem().getMinLevel());
           os.writeH((getItem().getMaxLevel() == 0) ? 99 : getItem().getMaxLevel());
         }

         if (isCanbeDmg()) {
           os.writeC(131);
           os.writeD(1);
         }

         if (isUndeadDmg()) {
           os.writeD(114);
           os.writeC(1);
         }









         os.writeC(132);
         os.writeD(3);


         if (getStr() != 0) {
           os.writeC(8);
           os.writeC(getStr());
         }
         if (getDex() != 0) {
           os.writeC(9);
           os.writeC(getDex());
         }
         if (getCon() != 0) {
           os.writeC(10);
           os.writeC(getCon());
         }
         if (getWis() != 0) {
           os.writeC(11);
           os.writeC(getWis());
         }
         if (getInt() != 0) {
           os.writeC(12);
           os.writeC(getInt());
         }

         if (getCha() != 0) {
           os.writeC(13);
           os.writeC(getCha());
         }

         if (getHp() != 0) {
           os.writeC(14);
           os.writeH(getHp());
         }

         if (getMr() != 0) {
           os.writeC(15);
           os.writeH(getMr());
         }


         if (isDrainMp()) {
           os.writeC(16);
         }

         if (getSp() != 0) {
           os.writeC(17);
           os.writeC(getSp());
         }

         if (isHasteItem()) {
           os.writeC(18);
         }

         if (getBowHitModifier() != 0) {
           os.writeC(24);
           os.writeC(getBowHitModifier());
         }
         else if (getEnchantLevel() >= 0 && getHitModifier() == 0 &&
           getItem().getType1() == 20 && getItem().getType1() == 62) {
           os.writeC(24);
           os.writeC(0);
         }




         if (getDefenseFire() != 0) {
           os.writeC(27);
           os.writeC(getDefenseFire());
         }

         if (getDefenseWater() != 0) {
           os.writeC(28);
           os.writeC(getDefenseWater());
         }

         if (getDefenseWind() != 0) {
           os.writeC(29);
           os.writeC(getDefenseWind());
         }

         if (getDefenseEarth() != 0) {
           os.writeC(30);
           os.writeC(getDefenseEarth());
         }

         if (getDefenseAll() != 0) {
           os.writeC(155);
           os.writeC(getDefenseAll());
         }

         if (getMp() != 0) {
           os.writeC(32);
           os.writeH(getMp());
         }


         if (isDrainHp()) {
           os.writeC(34);
         }

         if (getBowDmgModifier() != 0) {
           os.writeC(35);
           os.writeC(getBowDmgModifier());
         }

         if (getExpByItem() != 0) {
           os.writeC(36);
           os.writeC(getExpByItem());
         }


         if (getHpr() != 0) {
           os.writeC(37);
           os.writeC(getHpr());
         }


         if (getMpr() != 0) {
           os.writeC(38);
           os.writeC(getMpr());
         }


         if (getMagicHitRate() != 0) {
           os.writeC(40);
           os.writeC(getMagicHitRate());
         }

         if (getMagicCriticalValue() != 0) {
           os.writeC(50);
           os.writeH(getMagicCriticalValue());
         }

         if (getPvPDamage() != 0) {
           os.writeC(59);
           os.writeC(getPvPDamage());
         }

         if (getPvpReduction() != 0) {
           os.writeC(60);
           os.writeC(getPvpReduction());
         }

         if (getMagicDmgModifier() != 0) {
           os.writeC(194);
           os.writeD(getMagicDmgModifier());
         }

         if (getPVPDmgReducIgnore() != 0) {
           os.writeC(138);
           os.writeC(getPVPDmgReducIgnore());
         }

         if (getPVPMdmgReduction() != 0) {
           os.writeC(135);
           os.writeC(getPVPMdmgReduction());
         }


         if (getPVPMDmgReducIgnore() != 0) {
           os.writeC(139);
           os.writeC(getPVPMDmgReducIgnore());
         }

         if (getDG() != 0) {
           os.writeC(51);
           os.writeC(getDG());
         }

         if (getMagicDodge() != 0) {
           os.writeC(89);
           os.writeD(getMagicDodge());
         }

         if (getTotalER() != 0) {
           os.writeC(93);
           os.writeC(getTotalER());
         }

         if (getHpPercent() != 0) {
           os.writeC(149);
           os.writeC(getHpPercent());
         }

         if (getMpPercent() != 0) {
           os.writeC(150);
           os.writeC(getMpPercent());
         }

         if (getImmuneIgnore() != 0) {
           os.writeC(175);
           os.writeD(getImmuneIgnore());
           if (getItem().getItemId() == 7000239) {
             os.writeC(176);
             os.writeC(1);
           }

           if (getItem().getItemId() == 7000240) {
             os.writeC(176);
             os.writeC(1);
           }
           if (getItem().getItemId() == 203065) {
             os.writeC(176);
             os.writeC(1);
           }
           if (getItem().getItemId() == 203041) {
             os.writeC(176);
             os.writeC(1);
             os.writeC(16);
             os.writeC(0);
           }


           if (getItem().getItemId() == 7000262) {
             os.writeC(200);
             os.writeC(0);
             os.writeC(34);
             os.writeC(0);
           }
           if (getItem().getItemId() == 203042) {
             os.writeC(176);
             os.writeC(1);
           }


           if (getItem().getItemId() == 7000264) {
             os.writeC(220);
             os.writeH(1);
           }
           if (getItem().getItemId() == 7000265) {
             os.writeC(220);
             os.writeH(1);
           }
           if (getItem().getItemId() == 7000267) {
             os.writeC(176);
             os.writeC(1);
           }
         }

         if (getItem().getAttackDelayRate() != 0.0D) {
           os.writeOptionA("攻擊速度", (int)getItem().getAttackDelayRate()); // 寫入攻擊速度選項
         }

         if (getItem().getMoveDelayRate() != 0.0D) {
           os.writeOptionA("移動速度", (int)getItem().getMoveDelayRate()); // 寫入移動速度選項
         }



         if (getEndTime() != null) {
           if (!isSupportItem() && getItem().isEndedTimeMessage() && getEndTime() != null) {






             int remainSeconds = (int)((getEndTime().getTime() - 1483196400065L) / 1000L);
             os.writeC(61);
             os.writeD(remainSeconds * 6);
           } else if (isSupportItem() && !getItem().isEndedTimeMessage() && getEndTime() != null) {
             os.writeC(72);
             os.writeD((int)(getEndTime().getTime() / 1000L));
           } else {
             os.writeC(112);
             os.writeD(getEndTime().getTime() / 1000L);
           }
         }

         if (getDamageReduction() != 0) {
           os.writeC(63);
           os.writeC(getDamageReduction());
         }

         if (getDamageReductionRate() != 0) {
           os.writeC(64);
           os.writeC(getDamageReductionRate());
           os.writeC(getDamageReductionRateValue());
         } else if (getItem().getItemId() == 22263 || getItem().getItemId() == 900046 ||
           getItem().getItemId() == 900071) {
           os.writeC(64);
           os.writeC(getDamageReductionRate());
           os.writeC(getDamageReductionRateValue());
         }

         if (getPotionRecoveryRate() != 0) {
           os.writeC(65);
           os.writeC(getPotionRecoveryRate());
           os.writeC(getPotionRecoveryRateValue());
           os.writeC(96);
           os.writeC(getPotionRecoveryCancel());
         }

         if (getTitanPercent() != 0) {
           os.writeC(102);
           os.writeC(getTitanPercent());
         }

         if (isResistPoison()) {
           os.writeC(70);
           os.writeC(2);
         }

         if (getTechniqueTolerance() != 0) {
           os.writeC(117);
           os.writeC(getTechniqueTolerance());
         }

         if (getSpiritTolerance() != 0) {
           os.writeC(118);
           os.writeC(getSpiritTolerance());
         }

         if (getDragonLangTolerance() != 0) {
           os.writeC(119);
           os.writeC(getDragonLangTolerance());
         }

         if (getFearTolerance() != 0) {
           os.writeC(120);
           os.writeC(getFearTolerance());
         }

         if (getAllTolerance() != 0) {
           os.writeC(121);
           os.writeC(getAllTolerance());
         }

         if (getTechniqueHit() != 0) {
           os.writeC(122);
           os.writeC(getTechniqueHit());
         }

         if (getSpiritHit() != 0) {
           os.writeC(123);
           os.writeC(getSpiritHit());
         }

         if (getDragonLangHit() != 0) {
           os.writeC(124);
           os.writeC(getDragonLangHit());
         }

         if (getFearHit() != 0) {
           os.writeC(125);
           os.writeC(getFearHit());
         }

         if (getAllHit() != 0) {
           os.writeC(126);
           os.writeC(getAllHit());
         }

         String magicName = getMagicName();
         if (magicName != null && !magicName.equals("")) {
           if (magicName.contains(":")) {
             String[] arr = magicName.split("\\:");
             for (String name : arr) {
               os.writeC(74);
               os.writeS(name);
             }
           } else {
             os.writeC(74);
             os.writeS(magicName);
           }
         }

         if (getWeightReduction() != 0) {
           os.writeC(90);
           os.writeH(getWeightReduction());
         }

         if (getDamagePlusRate() != 0) {
           os.writeC(95);
           os.writeC(getDamagePlusRate());
           os.writeC(getDamagePlusValue());
         }

         if (getReductionCancel() != 0) {
           os.writeC(97);
           os.writeC(getReductionCancel());
         }

         if (getLongCriticalValue() != 0) {
           os.writeC(99);
           os.writeC(getLongCriticalValue());
         }

         if (getShortCriticalValue() != 0) {
           os.writeC(100);
           os.writeC(getShortCriticalValue());
         }

         if (getFoeDmg() != 0) {
           os.writeC(101);
           os.writeC(getFoeDmg());
         }

         if (getAttrLevelPacketNumber() != 0) {
           os.writeC(110);
           os.writeC(getAttrLevelPacketNumber());
         }

         if (getAinEfficiency() != 0) {
           os.writeC(116);
           os.writeH(getAinEfficiency());
         }
         if (getAbnormalStatusPvpDamageReduction() != 0) {
           os.writeC(212);
           os.writeC(getAbnormalStatusPvpDamageReduction());
         }

                 if (getMpAr16() != 0) {
                   os.writeC(39); // 寫入控制碼39
                   os.writeS("\fIMP 絕對恢復:\aA +" + getMpAr16() + "(16秒)"); // 寫入字符串，顯示MP絕對恢復量
                 }
         if (getCCIncrease() != 0) {
           os.writeC(219);
           os.writeH(getCCIncrease());
         }

         if (getSmeltingValue() != 0) {
           os.writeC(181);
           os.writeC(1);
           for (int i = 0; i < getSmeltingValue(); i++) {
             os.writeC(230);
             os.writeC(i);
             if (i == 0) {
               if (getSmeltingItemId1() == 0) {
                 continue;
               }

               SmeltingScrollInfo sInfo1 = SmeltingScrollLoader.getInstance().getSmeltingScrollInfo(getSmeltingItemId1());
               try {
                 os.write(SmeltingScrollInfo.getItemView(sInfo1));
               } catch (IOException e) {
                 e.printStackTrace();
               }
             }
             if (i == 1 &&
               getSmeltingItemId2() != 0) {


               SmeltingScrollInfo sInfo2 = SmeltingScrollLoader.getInstance().getSmeltingScrollInfo(getSmeltingItemId2());
               try {
                 os.write(SmeltingScrollInfo.getItemView(sInfo2));
               } catch (IOException e) {
                 e.printStackTrace();
               }
             }
             continue;
           }
           os.writeC(182);
           os.writeC(0);
         }




         L1ArmorSet.L1ArmorSetImpl armor_sets = ArmorSetTable.getInstance().find(getItemId());
         if (armor_sets != null && !armor_sets.is_signle_items()) {
           L1ArmorSets sets = armor_sets.get_source_effects();
           os.writeC(69);
           os.writeC(get_main_set_armor() ? 1 : 2);
           if (sets.getPolyDesc() > 0) {
             os.writeC(71);
             os.writeH(sets.getPolyDesc());
           }
           if (sets.getAc() != 0) {
             os.writeAddAc(sets.getAc());
           }
           if (sets.getHp() != 0) {
             os.writeAddMaxHP(sets.getHp());
           }
           if (sets.getMp() != 0) {
             os.writeMaxMP(sets.getMp());
           }
           if (sets.getHpr() != 0) {
             os.writeAddHPPrecovery(sets.getHpr());
           }
           if (sets.getMpr() != 0) {
             os.writeAddMPPrecovery(sets.getMpr());
           }
           if (sets.getMr() != 0) {
             os.writeAddMR(sets.getMr());
           }
           if (sets.getStr() != 0) {
             os.writeaSTR_Bu(sets.getStr());
           }
           if (sets.getDex() != 0) {
             os.writeaDEX_Bu(sets.getDex());
           }
           if (sets.getCon() != 0) {
             os.writeaCON_Bu(sets.getCon());
           }
           if (sets.getWis() != 0) {
             os.writeaWIS_Bu(sets.getWis());
           }
           if (sets.getIntl() != 0) {
             os.writeaINT_Bu(sets.getIntl());
           }
           if (sets.getCha() != 0) {
             os.writeaCHA_Bu(sets.getCha());
           }
           if (sets.get_defense_fire() != 0) {
             os.writeRegistFire(sets.get_defense_fire());
           }
           if (sets.get_defense_water() != 0) {
             os.writeRegistWater(sets.get_defense_water());
           }
           if (sets.get_defense_wind() != 0) {
             os.writeRegistWind(sets.get_defense_wind());
           }
           if (sets.get_defense_earth() != 0) {
             os.writeRegistEarth(sets.get_defense_earth());
           }

           if (sets.get_sp() != 0) {
             os.writeAddSP(sets.get_sp());
           }
           if (sets.get_melee_damage() != 0) {
             os.writeAddDMG(sets.get_melee_damage());
           }
           if (sets.get_melee_hit() != 0) {
             os.writeShortHIT(sets.get_melee_hit());
           }
           if (sets.get_missile_damage() != 0) {
             os.writeLongDMG(sets.get_missile_damage());
           }
           if (sets.get_missile_hit() != 0) {
             os.writeLongHIT(sets.get_missile_hit());
           }
           if (sets.getMagicHitup() != 0) {
             os.writeC(40);
             os.writeC(sets.getMagicHitup());
           }
           if (sets.get_regist_PVPweaponTotalDamage() != 0) {
             os.writePVPAddDMG(sets.get_regist_PVPweaponTotalDamage());
           }

           if (sets.get_regist_calcPcDefense() != 0) {
             os.writePVPAddDMGdown(sets.get_regist_calcPcDefense());
           }
           if (sets.getTechniqueTolerance() != 0) {
             os.writeability_resis(sets.getTechniqueTolerance());
           }

           if (sets.getSpiritTolerance() != 0) {
             os.writeaspirit_resis(sets.getSpiritTolerance());
           }

           if (sets.getDragonLangTolerance() != 0) {
             os.writeadragonS_resis(sets.getDragonLangTolerance());
           }

           if (sets.getFearTolerance() != 0) {
             os.writeafear_resis(sets.getFearTolerance());
           }

           if (sets.getAllTolerance() != 0) {
             os.writeaAll_resis(sets.getAllTolerance());
           }

           if (sets.getTechniqueHit() != 0) {
             os.writeability_pierce(sets.getTechniqueHit());
           }

           if (sets.getSpiritHit() != 0) {
             os.writeaspirit_pierce(sets.getSpiritHit());
           }

           if (sets.getDragonLangHit() != 0) {
             os.writeadragonS_pierce(sets.getDragonLangHit());
           }

           if (sets.getFearHit() != 0) {
             os.writeafear_pierce(sets.getFearHit());
           }

           if (sets.getAllHit() != 0) {
             os.writeaAll_pierce(sets.getAllHit());
           }
         }
       }



       return os.getBytes();
     } catch (Exception e) {
       e.getStackTrace();
     } finally {
       if (os != null) {
         try {
           os.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
       }
     }
     return null;
   }


   class EnchantTimer
     implements Runnable
   {
     private int _skillId;
     private boolean _active = true;
     private boolean _effectClear = false;
     private long _expireTime;

     public EnchantTimer(int skillId, long expireTime) {
       this._skillId = skillId;
       this._expireTime = expireTime;
     }

     public int getRemainTime() {
       int remainTime = (int)(this._expireTime - System.currentTimeMillis()) / 1000;

       if (remainTime < 1) {
         remainTime = 1;
       }

       return remainTime;
     }


     public void run() {
       try {
         if (!this._active) {
           return;
         }

         ClearEffect();
       } catch (Exception exception) {}
     }


     public void cancel() {
       this._active = false;
       ClearEffect();
       if (L1ItemInstance.this._owner != null) {
         L1ItemInstance.this._owner.sendPackets((ServerBasePacket)new S_ServerMessage(308, L1ItemInstance.this.getLogName()));
       }
     }

     public void on_off_icons() {
       if (L1ItemInstance.this._owner == null) {
         return;
       }
       SC_SPELL_BUFF_NOTI noti = null;
       switch (this._skillId) {

       }



       if (noti != null) {
         L1ItemInstance.this._owner.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
       }
     }

     public void on_on_icons() {
       if (L1ItemInstance.this._owner == null) {
         return;
       }
       SC_SPELL_BUFF_NOTI noti = null;
       switch (this._skillId) {

       }


       if (noti != null)
         L1ItemInstance.this._owner.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
     }

     public void ClearEffect() {
       synchronized (this) {
         if (this._effectClear) {
           return;
         }

         this._effectClear = true;
       }
       switch (this._skillId) {

       }

       on_off_icons();
       L1ItemInstance.this.removeSkillEffectTimer(this._skillId);
     }
   }

   public L1ItemInstance() { this._acByMagic = 0;









     this._dmgByMagic = 0;









     this._holyDmgByMagic = 0;









     this._hitByMagic = 0;









     this.repeatedSkills = new int[][] { {} };



     this._enchantmagic = 0;



     this._isNowLighting = false;









     this._DropMobId = 0;









     this._keyId = 0;



     this._openEffect = 0;



     this.m_is_give = false; this._count = 1; this._enchantLevel = 0; this._specialEnchant = 0; this._bookmarks = new ArrayList<>(); }
   public int getAcByMagic() { return this._acByMagic; }
   public void addAcByMagic(int i) { this._acByMagic += i; }
   public int getDmgByMagic() { return this._dmgByMagic; } public void addDmgByMagic(int i) { this._dmgByMagic += i; } public int getHolyDmgByMagic() { return this._holyDmgByMagic; } public void addHolyDmgByMagic(int i) { this._holyDmgByMagic += i; } public int getHitByMagic() { return this._hitByMagic; } public void addHitByMagic(int i) { this._hitByMagic += i; } private void remove_repeated_skills(int skill_id) { for (int[] skills : this.repeatedSkills) { for (int id : skills) { if (id == skill_id) { killSkillEffectTimer(skills); return; }  }  }  } public void setSkillWeaponEnchant(L1PcInstance pc, int skillId, int skillTime) { setSkillWeaponEnchant(pc, skillId, skillTime, 0); } public void setSkillArmorEnchant(L1PcInstance pc, int skillId, int skillTime) { if (getItem().getType2() != 2 || getItem().getType() != 2) return;  remove_repeated_skills(skillId); EnchantTimer timer = new EnchantTimer(skillId, System.currentTimeMillis() + skillTime); timer.on_on_icons(); this._skillEffect.put(Integer.valueOf(skillId), timer); GeneralThreadPool.getInstance().schedule(timer, skillTime); } public void setSkillWeaponEnchant(L1PcInstance pc, int skillId, int skillTime, int weapon_index) { if (getItem().getType2() != 1) return;  L1Skills skill = SkillsTable.getInstance().getTemplate(skillId); remove_repeated_skills(skillId); switch (skillId) {  }  EnchantTimer timer = new EnchantTimer(skillId, System.currentTimeMillis() + skillTime); timer.on_on_icons(); this._skillEffect.put(Integer.valueOf(skillId), timer); GeneralThreadPool.getInstance().schedule(timer, skillTime); setEnchantMagic(skill.getCastGfx()); if (skillId == 8) setEnchantMagic(2165);  } public int getEnchantMagic() { return this._enchantmagic; } public void setEnchantMagic(int i) { this._enchantmagic = i; } protected void removeSkillEffectTimer(int skillId) { this._skillEffect.remove(Integer.valueOf(skillId)); } public boolean hasSkillEffectTimer(int skillId) { return this._skillEffect.containsKey(Integer.valueOf(skillId)); } protected void killSkillEffectTimer(int skillId) { EnchantTimer timer = this._skillEffect.remove(Integer.valueOf(skillId)); if (timer != null) timer.cancel();  } protected void killSkillEffectTimer(int[] skills) { for (int skillId : skills) killSkillEffectTimer(skillId);  } public void on_skill_effect_icons() { for (EnchantTimer timer : this._skillEffect.values()) timer.on_on_icons();  } public void off_skill_effect_icons() { for (EnchantTimer timer : this._skillEffect.values()) timer.on_off_icons();  } public int getSkillEffectTimeSec(int skillId) { EnchantTimer timer = this._skillEffect.get(Integer.valueOf(skillId)); if (timer == null) return -1;  return timer.getRemainTime(); } public L1PcInstance getItemOwner() { return this._itemOwner; } public void setItemOwner(L1PcInstance pc) { this._itemOwner = pc; } public void startItemOwnerTimer(L1PcInstance pc) { setItemOwner(pc); L1ItemOwnerTimer timer = new L1ItemOwnerTimer(this, 10000); timer.begin(); } public void startEquipmentTimer(L1PcInstance pc) { if (getRemainingTime() > 0) { this._equipmentTimer = new L1EquipmentTimer(pc, this, 1000L); GeneralThreadPool.getInstance().schedule((Runnable)this._equipmentTimer, 1000L); }  } public void stopEquipmentTimer(L1PcInstance pc) { if (getRemainingTime() > 0) { this._equipmentTimer.cancel(); this._equipmentTimer = null; }  } public boolean isNowLighting() { return this._isNowLighting; } public void setNowLighting(boolean flag) { this._isNowLighting = flag; } public int isDropMobId() { return this._DropMobId; } public void setDropMobId(int i) { this._DropMobId = i; } public int getKeyId() { return this._keyId; } public void setKeyId(int i) { this._keyId = i; } public void onEquip(L1PcInstance pc) { this._owner = pc; } public void onUnEquip() { this._owner = null; } public boolean get_armor_set() { return this.armor_set; } public boolean isGiveItem() { return this.m_is_give; }
   public void set_armor_set(boolean b) { this.armor_set = b; }
   public boolean get_main_set_armor() { return this.main_set_armor; }
   public void set_main_set_armor(boolean b) { this.main_set_armor = b; }
   public int get_bless_level() { return this.bless_level; } public void set_bless_level(int i) { this.bless_level = CommonUtil.get_current(i, 0, 3); } public int get_item_level() { return this.item_level; } public void set_item_level(int i) { this.item_level = i; } public String getHotel_Town() { return this._Hotel_Town; } public void setHotel_Town(String name) { this._Hotel_Town = name; } public int get_Carving() { return this.Carving; } public void set_Carving(int i) { this.Carving = i; } public int get_Doll_Bonus_Value() { return this._Doll_bonus_value; } public void set_Doll_Bonus_Value(int i) { this._Doll_bonus_value = i; } public int get_Doll_Bonus_Level() { return this._Doll_bonus_level; } public void set_Doll_Bonus_Level(int i) { this._Doll_bonus_level = i; } public ArrayList<L1ItemBookMark> getBookMark() { return this._bookmarks; } public void addBookMark(L1ItemBookMark list) { this._bookmarks.add(list); } public int getAttrEnchantBit(int attr) { int attr_bit = 0; int result_bit = 0; if (attr >= 1 && attr <= 5) attr_bit = 1;  if (attr >= 6 && attr <= 10) { attr_bit = 2; attr -= 5; }  if (attr >= 11 && attr <= 15) { attr_bit = 3; attr -= 10; }  if (attr >= 16 && attr <= 20) { attr_bit = 4; attr -= 15; }  if (attr > 0) result_bit = attr_bit + 16 * attr;  return result_bit; } public void setOpenEffect(int i) { this._openEffect = i; } public int getOpenEffect() { return this._openEffect; } public byte[] serialize() { byte[] data = null; MJBytesOutputStream os = null; try { os = new MJBytesOutputStream(128); os.write(8); os.writeBit(getId()); os.write(16); os.writeBit(this._item.getItemDescId()); os.write(24); os.writeBit(getId()); os.write(32); os.writeBit(this._count); os.write(40); os.writeBit(this._item.getUseType()); os.write(56); os.writeBit(this._item.getGfxId()); os.write(64); os.writeBit(this.bless); os.write(72); int bit = (!this._item.isTradable() ? 2 : 16) | (this._item.isCantDelete() ? 4 : 0) | ((this._item.get_safeenchant() < 0) ? 8 : 0) | (isIdentified() ? 1 : 0); os.writeBit(bit); os.write(80); if (this._item.isEndedTimeMessage()) { os.writeBit(1L); } else { os.writeBit(8L); }  os.write(88); os.writeB(false); os.write(112); os.write(3); os.writeBit(146L); os.writeS2(getViewName()); data = os.toArray(); } catch (Exception e) { e.printStackTrace(); } finally { os.close(); os.dispose(); }  return data; } public byte[] serializeFishingItem() { byte[] data = null; MJBytesOutputStream os = null; try { os = new MJBytesOutputStream(128); os.write(8); os.writeBit(getId()); os.write(16); os.writeBit(this._item.getItemDescId()); os.write(24); os.writeBit(getId()); os.write(32); os.writeBit(this._count); os.write(40); os.writeBit(this._item.getUseType()); os.write(56); os.writeBit(this._item.getGfxId()); os.write(64); os.writeBit(this.bless); os.write(72); int bit = (!this._item.isTradable() ? 2 : 16) | (this._item.isCantDelete() ? 4 : 0) | ((this._item.get_safeenchant() < 0) ? 8 : 0) | (isIdentified() ? 1 : 0); os.writeBit(bit); os.write(80); if (this._item.isEndedTimeMessage()) { os.writeBit(1L); } else { os.writeBit(8L); }  os.write(88); os.writeB(false); os.write(112); os.writeBit(3L); os.writeBit(146L); os.writeS2(getViewName()); if (isIdentified()) { os.writeBit(154L); byte[] tmp = getStatusBytes(); os.writeBytes(tmp); }  data = os.toArray(); } catch (Exception e) { e.printStackTrace(); } finally { os.close(); os.dispose(); }  return data; } private static int _instanceType = -1; private boolean m_is_give; private boolean _isSupportItem; private int _range; private int _bless_type; private int _bless_type_value; public int _smelting_value; public int _smelting_itemid_1; public int _smelting_itemid_2; public int _smelting_kind_1; public int _smelting_kind_2; private Timestamp _halpas_time; private int _cant_unseal; public int getL1Type() { return (_instanceType == -1) ? (_instanceType = super.getL1Type() | 0x200) : _instanceType; } public void setGiveItem(boolean is_give) { this.m_is_give = is_give; }


   public boolean isCanbeDmg() {
     boolean result = (getItem().get_canbedmg() == 0);

     return result;
   }

   public boolean isUndeadDmg() {
     boolean result = false;

     if (getItem().getType2() == 1 || (
       getItem().getType2() == 0 && (getItem().getType() == 0 || getItem().getType() == 15)))
     {
       if (getItem().getMaterial() == 14 || getItem().getMaterial() == 17 || getItem().getMaterial() == 22) {
         result = true;
       }
     }
     return result;
   }

   public int getAcByEnchantLevel() {
     int result = 0;
     int enchantLevel = getEnchantLevel();

     if (isAccessory()) {
       if (getItemId() == 22226 || getItemId() == 22228 || getItemId() == 222290 || getItemId() == 222291 ||
         getItemId() == 222332 || getItemId() == 222334 || getItemId() == 222335 || getItemId() == 222336 ||
         getItemId() == 900195) {
         result += (enchantLevel <= 1) ? 0 : ((enchantLevel <= 4) ? (enchantLevel - 1) : 3);
         if (enchantLevel >= 1 && (getItemId() == 222291 || getItemId() == 222336)) {
           result++;
         }
         if (enchantLevel >= 7 && getItemId() == 222335) {
           result++;
         }
         if (enchantLevel >= 6 && (getItemId() == 22226 || getItemId() == 22228 || getItemId() == 222290 ||
           getItemId() == 900195)) {
           result++;
         }

         if ((enchantLevel > 7 && getItemId() == 222290) || getItemId() == 222291 ||
           getItemId() == 22224 || getItemId() == 22225 || getItemId() == 22226 || getItemId() == 22227 || getItemId() == 22228) {
           result++;
         }

         if (enchantLevel >= 5 && (getItemId() == 222332 || getItemId() == 222334)) {
           result++;
         }
         if (enchantLevel >= 7 && getBless() % 128 == 0) {
           result++;
         }
         if (enchantLevel >= 8 && getBless() % 128 == 0) {
           result += (enchantLevel - 8) * 3 + 2;
         }
         if (enchantLevel >= 8 && getBless() % 128 == 1) {
           result += (enchantLevel - 8) * 2 + 1;
         }
       } else if (getItemId() == 22229 || getItemId() == 222337) {
         if (getBless() % 128 == 0 && getEnchantLevel() >= 5) {
           result += enchantLevel + 2;
         } else if (getBless() % 128 != 0 && getEnchantLevel() >= 6) {
           result += enchantLevel + 1;
         }
       } else if (getItemId() == 22231 || getItemId() == 222339) {
         if (getBless() % 128 == 0 && getEnchantLevel() >= 5) {
           result += (enchantLevel - 4 > 4) ? 4 : (enchantLevel - 4);
         } else if (getBless() % 128 != 0 && getEnchantLevel() >= 6) {
           result += (enchantLevel - 5 > 3) ? 3 : (enchantLevel - 5);
         }
       } else if (getItemId() == 22225 || getItemId() == 22227 || getItemId() == 22224 || getItemId() == 222331 ||
         getItemId() == 222333 || getItemId() == 222330) {
         result += (enchantLevel <= 4) ? (enchantLevel - 1) : ((enchantLevel <= 1) ? 0 : 3);
       } else if (getItemId() == 22230 || getItemId() == 222338) {
         enchantLevel = (enchantLevel > 9) ? 9 : enchantLevel;
         if (enchantLevel == 9) {
           result++;
           if (getBless() % 128 == 0) {
             result++;
           }
         }
         if (getBless() % 128 == 0)
           enchantLevel++;
         result += (enchantLevel == 5) ? 1 : ((enchantLevel <= 4) ? 0 : ((enchantLevel - 1) / 2));
       } else if (getItemId() == 222340 || getItemId() == 222341) {
         enchantLevel = (enchantLevel > 9) ? 9 : enchantLevel;
         if (getBless() % 128 == 0 && enchantLevel > 2)
           enchantLevel++;
         result += enchantLevel;
       } else if (getItem().getType2() == 2 && getItem().getType() == 30) {
         if (getItemId() == 900084 || getItemId() == 900196 || (
           getItemId() >= 900081 && getItemId() <= 900083)) {
           if (enchantLevel >= 3)
             result += (enchantLevel <= 8) ? (enchantLevel - 2) : 7;
           if (enchantLevel >= 6) {
             result++;
           }
         } else if (enchantLevel >= 4 && enchantLevel <= 6) {
           result += enchantLevel - 3;
         } else if (enchantLevel >= 7) {
           result += 3;
         }
       } else if (getItem().getType2() == 2 && enchantLevel >= 5 && (
         getItem().getType() == 8 || getItem().getType() == 12)) {
         result += (enchantLevel <= 10) ? (enchantLevel - 4) : 5;
       }
     } else if (getItem().getType2() == 2 && getItem().getType() == 14) {
       result = 0;
     } else if (getItem().getType2() == 2 && getItem().getType() == 28) {
       result = 0;
     } else if (getItemId() >= 900234 && getItemId() <= 900237) {
       result = 0;
     } else {
       result = enchantLevel;
     }
     if (getItemId() >= 900275 && getItemId() <= 900278) {
       if (enchantLevel >= 5) {
         result = 1;
       } else {
         result = 0;
       }
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getAc(enchantLevel);
     }
     if (getBlessType() == 1) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getaddAc() {
     int result = 0;
     int enchantLevel = getEnchantLevel();
     int type = getItem().getType();
     int blesslevel = get_bless_level();

     if ((getItemId() < 22224 || getItemId() > 22228) && (getItemId() < 222330 || getItemId() > 222336) &&
       getItemId() != 222290 && getItemId() != 222291 && getItemId() != 900195 && (
       getItemId() < 22229 || getItemId() > 22231) && (getItemId() < 222337 || getItemId() > 222341) &&
       getItemId() != 900194)
     {







       if (get_bless_level() != 0 &&
         getItem().getType2() == 2 && (type < 8 || type > 12)) {
         if (blesslevel == 1) {
           result += Config.ServerEnchant.blessChance_armor_effect1;
         } else if (blesslevel == 2) {
           result += Config.ServerEnchant.blessChance_armor_effect2;
         } else if (blesslevel == 3) {
           result += Config.ServerEnchant.blessChance_armor_effect3;
         }
       }
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getaddAc(enchantLevel);
     }
     if (getBlessType() == 45) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public boolean isAccessory() {
     return (getItem().getType2() == 2 && (
       getItem().getType() == 8 || getItem().getType() == 9 || getItem().getType() == 10 ||
       getItem().getType() == 11 || getItem().getType() == 12 || getItem().getType() == 30));
   }

   public int getaddHp() {
     int result = 0;
     int enchantLevel = getEnchantLevel();
     int blesslevel = get_bless_level();
     int type = getItem().getType();

     if ((getItemId() < 22224 || getItemId() > 22228) && (getItemId() < 222330 || getItemId() > 222336) &&
       getItemId() != 222290 && getItemId() != 222291 && getItemId() != 900195 && (
       getItemId() < 22229 || getItemId() > 22231) && (getItemId() < 222337 || getItemId() > 222341) &&
       getItemId() != 900194 &&
       get_bless_level() != 0 &&
       getItem().getType2() == 2 && type >= 8 && type <= 12) {
       if (blesslevel == 1) {
         result += Config.ServerEnchant.blessChance_accessory_effect1;
       } else if (blesslevel == 2) {
         result += Config.ServerEnchant.blessChance_accessory_effect2;
       } else if (blesslevel == 3) {
         result += Config.ServerEnchant.blessChance_accessory_effect3;
       }
     }


     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getaddHp(enchantLevel);
     }
     if (getBlessType() == 46) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getHp() {
     int result = getItem().get_addhp();
     int enchantLevel = getEnchantLevel();

     if (getItem().getType2() == 2 && getItem().getGrade() >= 0 && (
       getItem().getGrade() < 3 || getItem().getGrade() > 4) && getItem().getType() != 10) {
       result += (enchantLevel == 3) ? 20 : ((enchantLevel <= 2) ? (enchantLevel * 5) : ((enchantLevel <= 0) ? 0 : ((enchantLevel + 3) / 2 * 10)));

       if (enchantLevel == 10) {
         result += 10;
       }
       if (enchantLevel == 10 && getItem().getType() == 8) {
         result += 30;
       }
     }
     if (enchantLevel == 10 && getItem().getType() == 10) {
       result += 40;
     }

     if (getItemId() == 22224 || getItemId() == 22225 || getItemId() == 22226 || getItemId() == 22227 ||
       getItemId() == 22228 || getItemId() == 222330 || getItemId() == 222331 || getItemId() == 222332 ||
       getItemId() == 222333 || getItemId() == 222334 || getItemId() == 900195) {
       if (getItemId() == 222332 && enchantLevel <= 5)
         result += 5;
       if (getItemId() == 222334 && enchantLevel <= 7)
         result += 5;
       result += (enchantLevel <= 0) ? 0 : (10 + enchantLevel * 5);
       if (getItemId() == 222332 && getBless() % 128 == 0)
         result += (enchantLevel <= 5) ? 0 : ((enchantLevel - 5) * 5);
     } else if (getItemId() == 222290 || getItemId() == 222335) {
       if (getBless() % 128 == 0)
       { result += (enchantLevel <= 7) ? (5 + enchantLevel * 5) : ((enchantLevel <= 2) ? (enchantLevel * 5) : ((enchantLevel <= 0) ? 0 : (10 + enchantLevel * 5))); }
       else

       { result += (enchantLevel <= 0) ? 0 : (enchantLevel * 5); }
     } else if (getItemId() == 222291) {
       result += (enchantLevel <= 2) ? 0 : ((enchantLevel - 2) * 5);
     } else if (getItemId() == 222336) {
       if (enchantLevel == 8)
         result -= 5;
       result += 5 + ((enchantLevel <= 2) ? 0 : ((enchantLevel - 2) * 5));
     } else if ((getItemId() == 22229 || getItemId() == 222337) && enchantLevel > 0) {
       if (getBless() % 128 == 0)
         enchantLevel++;
       result += (enchantLevel < 9) ? (10 + enchantLevel * 10) : ((enchantLevel <= 0) ? 0 : 140);
       if (getBless() % 128 == 0 && enchantLevel == 10) {
         result += 10;
       }
       if (getBless() % 128 != 0 && enchantLevel == 9) {
         result -= 40;
       }
     } else if (getItemId() == 22256) {
       result += (enchantLevel < 5) ? 0 : ((enchantLevel - 3) / 2 * 25);
     } else if ((getItemId() >= 900025 && getItemId() <= 900028) || (getItemId() >= 900184 && getItemId() <= 900187) ||
       getItemId() == 900198) {
       result += (enchantLevel > 9) ? 100 : 0;
     } else if (getItem().getType() == 10) {
       result += (enchantLevel > 5) ? ((enchantLevel - 4) * 10) : 0;
     } else if (getItemId() == 22359 || (getItemId() >= 222307 && getItemId() <= 222309)) {
       result += (enchantLevel > 6) ? ((enchantLevel - 6) * 20) : 0;
     } else if (getItemId() >= 900081 && getItemId() <= 900083) {
       result += 5 + enchantLevel * 5;
       if (getItemId() >= 900081 && getItemId() <= 900083 &&
         enchantLevel >= 8) {
         result += 5;
       }
     } else if ((getItemId() >= 900152 && getItemId() <= 900154) || getItemId() == 900196 || getItemId() == 900084) {
       result += (enchantLevel + 1) * 5;
       if (enchantLevel >= 8)
         result += 5;
     } else if ((getItemId() == 22231 || getItemId() == 222339) &&
       enchantLevel >= 8) {
       result += 50 + (enchantLevel - 8) * 50 + ((getBless() % 128 == 0) ? 50 : 0);
     }












     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getHp(enchantLevel);
     }
     if (getBlessType() == 8) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getMp() {
     int result = getItem().get_addmp();
     int enchantLevel = getEnchantLevel();

     if (getItem().getType2() == 2 && getItem().getType() == 10) {
       result += (enchantLevel == 3) ? 20 : ((enchantLevel <= 2) ? (enchantLevel * 5) : ((enchantLevel <= 0) ? 0 : ((enchantLevel + 3) / 2 * 10)));

       if (enchantLevel == 10) {
         result += 10;
       }
     } else if (getItem().getItemId() == 222335) {
       result += 15;
       if (enchantLevel == 7)
       { result += 15; }
       else if (enchantLevel == 8)
       { result += 20; }
       else if (enchantLevel == 9)
       { result += 65; }
     } else if (getItem().getItemId() == 222290) {
       result += 15;
       if (enchantLevel == 8)
       { result += 15; }
       else if (enchantLevel == 9)
       { result += 55; }
     } else if (getItem().getItemId() == 22231 || getItem().getItemId() == 222339) {
       if (getItem().getItemId() == 222339 && enchantLevel <= 3)
         result -= 10;
       if (getItem().getItemId() == 22231 && enchantLevel == 0) {
         result -= 25;
       } else if (getItem().getItemId() == 22231 && enchantLevel == 1) {
         result -= 20;
       } else if (getItem().getItemId() == 22231 && enchantLevel == 2) {
         result -= 20;
       } else if (getItem().getItemId() == 22231 && enchantLevel >= 3 && enchantLevel <= 4) {
         result -= 10;
       }
       if (getBless() % 128 == 0 && enchantLevel >= 9) {
         result += 30;
       }

       if (getBless() % 128 == 0)
         enchantLevel++;
       result += (enchantLevel == 8) ? 95 : ((enchantLevel == 7) ? 70 : ((enchantLevel <= 6) ? (25 + enchantLevel * 5) : ((enchantLevel <= 4) ? (15 + enchantLevel * 5) : ((enchantLevel <= 2) ? (5 + enchantLevel * 5) : ((enchantLevel <= 0) ? 0 : 125)))));



     }
     else if (getItemId() == 410011) {
       result += 40;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getMp(enchantLevel);
     }

     if (getBlessType() == 9) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getSp() {
     int result = getItem().get_addsp();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 222290 || getItemId() == 222335) {
       if (getBless() % 128 == 0) {
         result += (enchantLevel > 3) ? (enchantLevel - 3) : 0;
       } else {
         result += (enchantLevel > 4) ? (enchantLevel - 4) : 0;
       }
       if (enchantLevel >= 8) {
         if (getBless() % 128 == 0) {
           result++;
         }
         result += enchantLevel - 8 + 1;
       }
     } else if (getItemId() == 22231 || getItemId() == 222339) {
       if (getBless() % 128 == 0)
         enchantLevel++;
       result += (enchantLevel <= 2) ? 0 : ((enchantLevel - 1) / 2);
       if (enchantLevel == 8 && getBless() % 128 != 0) {
         result += 2;
       }
       if (enchantLevel == 9) {
         if (getBless() % 128 != 0) {
           result += 2;
         }
         result++;
       }

       if (enchantLevel == 10) {
         result += 5;
       }
     } else if (getItemId() == 22255) {
       result += (enchantLevel < 5) ? 0 : ((enchantLevel - 3) / 2);
     } else if (getItemId() == 20107) {
       result += (enchantLevel > 2) ? (enchantLevel - 2) : 0;
     } else if (getItemId() == 900032 || getItemId() == 900228) {
       result += (enchantLevel > 8) ? 1 : 0;
     } else if (getItemId() == 124 || getItemId() == 900119 || getItemId() == 900221) {
       result += (enchantLevel > 6) ? (enchantLevel - 6) : 0;
     } else if (getItemId() == 900051 || getItemId() == 900095 || getItemId() == 900098) {
       result += (enchantLevel > 5) ? (enchantLevel - 5) : 0;
     } else if (getItemId() == 4100039 || getItemId() == 410012) {
       result += 3;
     } else if (getItemId() == 900129 || getItemId() == 900126) {
       result += (enchantLevel > 4) ? (enchantLevel - 4) : 0;
     } else if (getItemId() == 900028) {
       if (enchantLevel == 8 || enchantLevel == 9)
       { result++; }
       else if (enchantLevel >= 10)
       { result += 2; }
     } else if (getItemId() == 900187) {
       if (enchantLevel >= 7 && enchantLevel <= 9) {
         result++;
       } else if (enchantLevel >= 10) {
         result += 2;
       }
     }  if ((getItemId() == 900236 || getItemId() == 900277) &&
       enchantLevel >= 4 && enchantLevel <= 8) {
       result += 1 + (enchantLevel - 4) * 1;
     }

     if (getItem().getType2() == 2 && getItem().getGrade() >= 0 && (
       getItem().getGrade() < 3 || getItem().getGrade() > 4) && (
       getItem().getType() == 9 || getItem().getType() == 11)) {
       result += (enchantLevel > 6) ? (enchantLevel - 6) : 0;
     }

     if (getItemId() == 22384 &&
       this._cha != null && (
       this._cha.isWizard() || this._cha.isBlackwizard())) {
       result += 2;
     }



     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getSp(enchantLevel);
     }
     if (getBlessType() == 13) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getMagicHitRate() {
     int result = getItem().getMagicHitup();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 900051 || getItemId() == 900095 || getItemId() == 900098) {
       if (getEnchantLevel() == 5) {
         result += getEnchantLevel() - 4;
       }
       if (getEnchantLevel() >= 6) {
         result += getEnchantLevel() - 5;
       }
     } else if (getItem().getItemId() == 22231 || getItem().getItemId() == 222339) {
       if (getBless() % 128 == 0 && getEnchantLevel() >= 6) {
         result += 1 + (getEnchantLevel() - 6) * 2;
       } else if (getBless() % 128 != 0 && getEnchantLevel() >= 7) {
         result += 1 + (getEnchantLevel() - 7) * 2;
       }
       if (enchantLevel >= 8) {
         result += 2 + (enchantLevel - 8) * 2;
       }
       if (enchantLevel == 9 && getBless() % 128 == 0) {
         result++;
       }
     } else if (getItem().getItemId() == 222290 || getItem().getItemId() == 222335) {
       if (getBless() % 128 == 0) {
         result += (getEnchantLevel() > 5) ? (getEnchantLevel() - 5) : 0;
       } else {
         result += (getEnchantLevel() > 6) ? (getEnchantLevel() - 6) : 0;
       }
       if (enchantLevel >= 8) {
         if (getBless() % 128 == 0) {
           result++;
         }
         result += enchantLevel - 8 + 1;
       }
     } else if (getItemId() == 900119 || getItemId() == 900221) {
       result += (getEnchantLevel() > 4) ? (getEnchantLevel() - 4) : 0;
     } else if (getItemId() == 900083) {
       result += (getEnchantLevel() > 5) ? (getEnchantLevel() - 5) : 0;
       if (getEnchantLevel() >= 7)
         result++;
       if (getEnchantLevel() >= 8)
         result++;
     } else if (getItemId() == 900129 || getItemId() == 900126) {
       result += (enchantLevel > 3) ? (enchantLevel - 3) : 0;
       if (getItemId() == 900129 && enchantLevel > 4)
         result--;
     } else if ((getItemId() == 900028 || getItemId() == 900187) &&
       enchantLevel > 7) {
       result += (enchantLevel > 9) ? 4 : ((enchantLevel > 8) ? 3 : ((enchantLevel > 7) ? 1 : 0));
       if (getBless() % 128 == 0) {
         result++;
       }
     }


     if ((getItemId() == 22383 || getItemId() == 22384) &&
       this._cha != null && (
       this._cha.isWizard() || this._cha.isBlackwizard())) {
       result += 2;
     }



     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getMagicHit(enchantLevel);
     }
     if (getBlessType() == 20) {
       result += getBlessTypeValue();
     }


     return result;
   }

   public int getDamageReduction() {
     int result = getItem().get_damage_reduction();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 22229 || getItemId() == 222337) {
       result += (enchantLevel > 2) ? (enchantLevel - 2) : 0;
       if (enchantLevel > 3 && getBless() % 128 != 0) {
         result--;
       }
       if (enchantLevel == 8) {
         if (getBless() % 128 != 0) {
           result += 2;
         } else {
           result++;
         }
       }
       if (enchantLevel == 9 && getBless() % 128 != 0) {
         result++;
       }
       if (enchantLevel == 9 && getBless() % 128 == 0) {
         result += 2;
       }
     } else if (getItemId() >= 22196 && getItemId() <= 22199) {
       result += (enchantLevel > 6) ? (enchantLevel - 6) : 0;
     } else if (getItemId() == 900025 || getItemId() == 900184 || getItemId() == 900198 || (
       getItemId() >= 222307 && getItemId() <= 222309) || getItemId() == 22359) {
       result += (enchantLevel > 8) ? 1 : 0;
     } else if (getItemId() == 22254 || getItemId() == 900207) {
       result += (enchantLevel < 5) ? 0 : ((enchantLevel - 3) / 2);
     } else if (getItemId() == 22226 || getItemId() == 222332) {
       if (getBless() % 128 == 0) {
         result += (enchantLevel > 5) ? (enchantLevel - 5) : 0;
       } else {
         result += (enchantLevel > 6) ? (enchantLevel - 6) : 0;
       }
       if (enchantLevel >= 8) {
         if (getBless() % 128 == 0) {
           result++;
         }
         result += enchantLevel - 8 + 1;
       }
     } else if (getItemId() == 900084 || getItemId() == 900196 || (getItemId() >= 900081 && getItemId() <= 900083)) {
       result += (enchantLevel > 4) ? (enchantLevel - 4) : 0;
     } else if (getItemId() == 900120 || getItemId() == 900222) {
       result += (enchantLevel > 6) ? (enchantLevel - 6) : 0;
     }
     if (getItemId() == 900237 &&
       enchantLevel >= 6 && enchantLevel <= 8) {
       result += 2 + (enchantLevel - 6) * 2;
     }
     if (getItemId() == 900278 &&
       enchantLevel >= 5 && enchantLevel <= 8) {
       result += 1 + (enchantLevel - 5) * 2;
     }

     if (getItem().getType2() == 2 && getItem().getType() == 10) {
       result += (enchantLevel > 4) ? (enchantLevel - 4) : 0;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getDamageReduction(enchantLevel);
     }
     if (getBlessType() == 18) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getExpByItem() {
     int result = getItem().getAddExp();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 900237 &&
       enchantLevel >= 4 && enchantLevel <= 8) {
       result += 4 + (enchantLevel - 4) * 4;
     }
     if (getItemId() == 900278)
       if (enchantLevel == 4) {
         result += 4;
       } else if (enchantLevel >= 5) {
         result += 6 + (enchantLevel - 4) * 4;
       }
     if (getItem().getItemId() == 900020) {
       result += enchantLevel * 2;
       if (enchantLevel > 4) {
         result -= enchantLevel - 4;
       }
     } else if ((getItemId() >= 900093 && getItemId() <= 900095) || getItemId() == 900099) {
       result += (enchantLevel > 2) ? (enchantLevel + 1) : 0;
       if (enchantLevel > 6)
         result += enchantLevel - 6;
     } else if (getItemId() == 900025 || getItemId() == 900184 || getItemId() == 900198) {
       if (enchantLevel >= 7) {
         result += (enchantLevel - 7) * 2;
         if (getBless() % 128 == 0) {
           result += 2;
         }
       }
     } else if (MJItemExpBonus.get_bonus_exp(this) > 1.0D) {
       result = (int)(result + MJItemExpBonus.get_bonus_exp(this));
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getExpBonus(enchantLevel);
     }
     if (getBlessType() == 33) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getMpr() {
     int result = getItem().get_addmpr();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 410011) {
       result += 4;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getMpr(enchantLevel);
     }
     if (getBlessType() == 11) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getHpr() {
     int result = getItem().get_addhpr();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 410010) {
       result += 4;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getHpr(enchantLevel);
     }
     if (getBlessType() == 10) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getPvPDamage() {
     int result = 0;
     int enchantLevel = getEnchantLevel();

     if (getItem().getType2() == 1)
       result += getItem().get_regist_PVPweaponTotalDamage();
     if (getItem().getType2() == 2) {
       result += getItem().get_regist_PVPweaponTotalDamage();

       if (getItem().getType() == 9 && (
         getItemId() < 22224 || getItemId() > 22228) && getItemId() != 900195 && getItemId() != 222290 &&
         getItemId() != 222291 && (getItemId() < 222330 || getItemId() > 222336)) {
         if (enchantLevel == 10) {
           result += 7;
         } else if (enchantLevel == 9) {
           result += 5;
         } else {
           result += (enchantLevel > 5) ? (enchantLevel - 5) : 0;
         }
       }
     }
     if ((getItemId() >= 22224 && getItemId() <= 22228) || getItemId() == 900195 || getItemId() == 222290 ||
       getItemId() == 222291 || (getItemId() >= 222330 && getItemId() <= 222336)) {
       if (enchantLevel == 7) {
         result += enchantLevel - 6;
       } else if (enchantLevel == 8) {

         result += (getBless() % 128 == 0) ? (enchantLevel - 6 + 3) : (enchantLevel - 6 + 1);
       } else if (enchantLevel >= 9) {
         result += (getBless() % 128 == 0) ? (enchantLevel - 6 + 6) : (enchantLevel - 6 + 4);
       }
     } else if ((getItemId() >= 7000214 && getItemId() <= 7000221) || (getItemId() >= 307 && getItemId() <= 314)) {
       result += (enchantLevel > 6) ? (3 + (enchantLevel - 7) * 2) : 0;
     } else if ((getItemId() >= 900025 && getItemId() <= 900028) || (getItemId() >= 900184 && getItemId() <= 900187) ||
       getItemId() == 900198) {
       result += (enchantLevel > 9) ? 1 : 0;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getPvpDmg(enchantLevel);
     }
     if (getBlessType() == 37) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getPvpReduction() {
     int result = 0;
     int enchantLevel = getEnchantLevel();

     if (getItem().getType2() == 1)
       result += getItem().get_regist_calcPcDefense();
     if (getItem().getType2() == 2) {
       result += getItem().get_regist_calcPcDefense();

       if (getItem().getType() == 10) {
         if (enchantLevel == 7) {
           result += 3;
         } else if (enchantLevel == 8) {
           result += 5;
         } else if (enchantLevel == 9) {
           result += 7;
         } else if (enchantLevel >= 10) {
           result += 9;
         } else {
           result += (enchantLevel > 5) ? (enchantLevel - 5) : 0;
         }
       }
     }

     if ((getItem().getItemId() >= 900025 && getItem().getItemId() <= 900028) || (
       getItem().getItemId() >= 900184 && getItem().getItemId() <= 900187) ||
       getItem().getItemId() == 900198) {
       result += (enchantLevel > 9) ? 1 : 0;
     } else if (getItemId() == 4100042 || getItemId() == 4100465 || getItemId() == 4100041 ||
       getItemId() == 4100039) {
       result += 3;
     } else if (getItemId() >= 900081 && getItemId() <= 900083) {

       if (enchantLevel == 5) {
         result++;
       } else if (enchantLevel == 6) {
         result += 2;
       } else if (enchantLevel == 7) {
         result += 3;
       } else if (enchantLevel >= 8) {
         result += 5;
       }
     }
     if (getItemId() >= 900234 && getItemId() <= 900236 &&
       enchantLevel >= 6 && enchantLevel <= 8) {
       result += 2 + (enchantLevel - 6) * 2;
     }
     if (getItemId() >= 900275 && getItemId() <= 900277 &&
       enchantLevel >= 5 && enchantLevel <= 8) {
       result += 1 + (enchantLevel - 5) * 2;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getPvpReduction(enchantLevel);
     }
     if (getBlessType() == 38) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getMagicDmgModifier() {
     int result = getItem().getMagicDmgModifier();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getMagicDmgModifier(enchantLevel);
     }
     return result;
   }

   public int getWeightReduction() {
     int result = getItem().getWeightReduction();
     int enchantLevel = getEnchantLevel();

     if (getItem().getItemId() == 20274) {
       result += (enchantLevel > 4) ? ((enchantLevel - 4) * 60) : 0;
     }
     if (getItem().getItemId() >= 900275 && getItem().getItemId() <= 900275)
     {
       if (enchantLevel >= 5) {
         result += 140;
       }
     }
     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getWeightReduction(enchantLevel);
     }
     if (getBlessType() == 19) {
       result += getBlessTypeValue();
     }

     return result;
   }


   public int getDamageReductionRate() {
     int result = 0;
     int enchantLevel = getEnchantLevel();

     if (getItem().getItemId() == 900046 || getItem().getItemId() == 900071 || getItem().getItemId() == 900202) {
       result += enchantLevel;
     } else if (getItem().getItemId() == 22263) {
       result += enchantLevel * 2;
     } else if (getItem().getItemId() == 22226) {
       result += (enchantLevel > 6) ? ((enchantLevel > 8) ? (enchantLevel - 5) : (enchantLevel - 6)) : 0;
     } else if (getItem().getItemId() == 222332) {
       result += (enchantLevel > 5) ? ((enchantLevel > 8) ? (enchantLevel - 4) : (enchantLevel - 5)) : 0;
     } else if (getItemId() == 22229 && enchantLevel >= 5) {
       result += enchantLevel - 3;
       if (enchantLevel == 9) {
         result++;
       }
     } else if (getItemId() == 222337 && enchantLevel >= 4) {
       result += enchantLevel - 2;
       if (enchantLevel == 9) {
         result++;
       }
     } else if (getItemId() == 900090) {
       result += enchantLevel;
     }

     return result;
   }

   public int getDamageReductionRateValue() {
     int result = 0;

     if (getItem().getItemId() == 900046 || getItem().getItemId() == 900071 || getItem().getItemId() == 900202) {
       result += 10;
     } else if (getItem().getItemId() == 22263) {
       result += 50;
     } else if (getItem().getItemId() == 22226 || getItem().getItemId() == 222332) {
       result += 20;
     } else if (getItemId() == 22229 || getItemId() == 222337 ||
       getItemId() == 900090) {
       if (getEnchantLevel() >= 8) {
         result += 5;
       }
       if (getEnchantLevel() >= 8 && getBless() % 128 == 0) {
         result += 5;
       }
       result += 20;
     }

     return result;
   }

   public int getTechniqueTolerance() {
     int result = getItem().getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY);
     int enchantLevel = getEnchantLevel();

     if ((getItemId() >= 22224 && getItemId() <= 22228) || getItemId() == 900195 || getItemId() == 222290 ||
       getItemId() == 222291 || (getItemId() >= 222330 && getItemId() <= 222336)) {
       result += (enchantLevel > 5) ? ((5 + (getEnchantLevel() - 6) * 2 > 9) ? 9 : (5 + (getEnchantLevel() - 6) * 2)) : 0;
     } else if ((getItemId() >= 900026 && getItemId() <= 900028) || (
       getItemId() >= 900185 && getItemId() <= 900187)) {
       result += (enchantLevel > 4) ? (enchantLevel + 3) : 0;
       if (enchantLevel > 7)
         result += (enchantLevel == 8) ? 1 : ((enchantLevel == 9) ? 3 : ((enchantLevel >= 10) ? 5 : 0));
     }
     if (getItem().getType2() == 2 && (getItem().getType() == 8 || getItem().getType() == 12) && (
       getItemId() < 22224 || getItemId() > 22229) && getItemId() != 22230 && getItemId() != 22231 && getItemId() != 900195 &&
       getItemId() != 222290 && getItemId() != 222291 && (
       getItemId() < 222330 || getItemId() > 222341)) {
       result += (enchantLevel > 6) ? (enchantLevel - 5) : 0;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getTechniqueTolerance(enchantLevel);
     }
     if (getBlessType() == 21) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getSpiritTolerance() {
     int result = getItem().getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT);
     int enchantLevel = getEnchantLevel();

     if (getItemId() >= 900185 && getItemId() <= 900187) {
       int sub_result = (enchantLevel > 9) ? 4 : ((enchantLevel > 8) ? 5 : ((enchantLevel > 6) ? 6 : 0));
       result += (enchantLevel > 6) ? (enchantLevel - sub_result) : 0;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getSpiritTolerance(enchantLevel);
     }
     if (getBlessType() == 22) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getDragonLangTolerance() {
     int result = getItem().getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL);
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 20235 || (
       getItemId() >= 22196 && getItemId() <= 22203) || (getItemId() >= 22208 && getItemId() <= 22211)) {
       result += (enchantLevel > 4) ? (enchantLevel - 4) : 0;
       if (result > 7) {
         result = 7;
       }
     } else if (getItemId() >= 900185 && getItemId() <= 900187) {
       int sub_result = (enchantLevel > 9) ? 4 : ((enchantLevel > 8) ? 5 : ((enchantLevel > 6) ? 6 : 0));
       result += (enchantLevel > 6) ? (enchantLevel - sub_result) : 0;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getDragonLangTolerance(enchantLevel);
     }
     if (getBlessType() == 23) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getFearTolerance() {
     int result = getItem().getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR);
     int enchantLevel = getEnchantLevel();

     if (getItemId() >= 900185 && getItemId() <= 900187) {
       int sub_result = (enchantLevel > 9) ? 4 : ((enchantLevel > 8) ? 5 : ((enchantLevel > 6) ? 6 : 0));
       result += (enchantLevel > 6) ? (enchantLevel - sub_result) : 0;
     }

     if (enchantLevel >= 8 && (
       getItemId() == 22226 || getItemId() == 22228 || getItemId() == 222290 || getItemId() == 222291 ||
       getItemId() == 222332 || getItemId() == 222334 || getItemId() == 222335 || getItemId() == 222336 ||
       getItemId() == 900195)) {
       if (getBless() % 128 == 0) {
         result += enchantLevel - 7;
       }
       result += enchantLevel - 7;
     }


     if (getItem().getType2() == 2 && (getItem().getType() == 8 || getItem().getType() == 10) && (
       getItemId() < 22224 || getItemId() > 22229) && getItemId() != 22231 && getItemId() != 22230 && getItemId() != 900195 &&
       getItemId() != 222290 && getItemId() != 222291 && (
       getItemId() < 222330 || getItemId() > 222341) &&
       enchantLevel == 10) {
       result += 2;
     }


     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getFearTolerance(enchantLevel);
     }
     if (getBlessType() == 24) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getAllTolerance() {
     int result = getItem().getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
     int enchantLevel = getEnchantLevel();

     if ((getItemId() >= 900117 && getItemId() <= 900119) || (getItemId() >= 900219 && getItemId() <= 900221)) {
       result += (enchantLevel > 5) ? (enchantLevel - 5) : 0;
       if (result > 5) {
         result = 5;
       }
     }
     if (getItemId() >= 900234 && getItemId() <= 900236 &&
       enchantLevel >= 6 && enchantLevel <= 8) {
       result += 2 + (enchantLevel - 6) * 2;
     }
     if (getItemId() >= 900275 && getItemId() <= 900277 &&
       enchantLevel >= 5 && enchantLevel <= 8) {
       result += 1 + (enchantLevel - 5) * 2;
     }

     if (getItem().getType2() == 2 && (getItem().getType() == 12 || getItem().getType() == 9) && (
       getItemId() < 22224 || getItemId() > 22229) && getItemId() != 22231 && getItemId() != 22230 && getItemId() != 900195 &&
       getItemId() != 222290 && getItemId() != 222291 && (
       getItemId() < 222330 || getItemId() > 222341) &&
       enchantLevel == 10) {
       result++;
     }


     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getAllTolerance(enchantLevel);
     }
     if (getBlessType() == 25) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getTechniqueHit() {
     int result = getItem().getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY);
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getTechniqueHit(enchantLevel);
     }
     if (getBlessType() == 26) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getSpiritHit() {
     int result = getItem().getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT);
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getSpiritHit(enchantLevel);
     }
     if (getBlessType() == 27) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getDragonLangHit() {
     int result = getItem().getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL);
     int enchantLevel = getEnchantLevel();




     if (getItemId() == 203017 || getItemId() == 618) {
       result += (enchantLevel > 6) ? (enchantLevel - 6) : 0;
       if (result > 4) {
         result = 4;
       }
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getDragonLangHit(enchantLevel);
     }
     if (getBlessType() == 28) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getFearHit() {
     int result = getItem().getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR);
     int enchantLevel = getEnchantLevel();

     if (getItem().getItemId() == 203006 || getItem().getItemId() == 616) {
       result += (enchantLevel > 7) ? (enchantLevel - 7) : 0;
     } else if (getItem().getItemId() == 547) {
       result += (enchantLevel > 1) ? (enchantLevel - 1) : 0;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getFearHit(enchantLevel);
     }
     if (getBlessType() == 29) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getAllHit() {
     int result = getItem().getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getAllHit(enchantLevel);
     }
     if (getBlessType() == 30) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getReductionCancel() {
     int result = 0;
     int enchantLevel = getEnchantLevel();

     if (getItem().getType2() == 2) {
       result += getItem().getArmorReductionCancel();
     }
     if (getItem().getType2() == 1) {
       result += getItem().getWeaponReductionCancel();
     }
     if (getItem().getItemId() >= 22208 && getItem().getItemId() <= 22211) {
       result += 3;
       result += (enchantLevel > 6) ? (enchantLevel - 6) : 0;
     }

     if (getItem().getItemId() >= 22370 && getItem().getItemId() <= 22372) {
       result += 5;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getReductionCancel(enchantLevel);
     }
     return result;
   }

   public int getTotalER() {
     int result = 0;
     int enchantLevel = getEnchantLevel();

     if (getItemId() >= 900234 && getItemId() <= 900236 &&
       enchantLevel >= 6 && enchantLevel <= 8) {
       result += 2 + (enchantLevel - 6) * 2;
     }
     if (getItemId() >= 900275 && getItemId() <= 900277 &&
       enchantLevel >= 5 && enchantLevel <= 8) {
       result += 1 + (enchantLevel - 5) * 2;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getTotalER(enchantLevel);
     }
     if (getBlessType() == 34) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getPotionRecoveryRate() {
     int result = 0;
     int enchantLevel = getEnchantLevel();

     if (getItem().getItemId() == 900021 || getItemId() == 900099) {
       result += (enchantLevel + 1) * 2;
     } else if ((getItem().getItemId() >= 900049 && getItem().getItemId() <= 222352) || (
       getItem().getItemId() >= 900093 && getItem().getItemId() <= 900098)) {
       result += (enchantLevel > 4) ? (8 + enchantLevel - 4) : (enchantLevel * 2);
       if (getItemId() >= 900096 && getItemId() <= 900098 && enchantLevel > 2) {
         result += 2;
         if (enchantLevel > 4) {
           result += enchantLevel - 4;
         }
       }
     } else if (getItem().getItemId() == 22230 || getItem().getItemId() == 222338) {
       result += (enchantLevel > 1) ? (6 + (enchantLevel - 1) * 2) : 2;
       if (getBless() % 128 == 0 && enchantLevel > 2) {
         result += 2;
       }
       if (enchantLevel == 8) {
         result += 2;
         if (getBless() % 128 == 0) {
           result++;
         }
       }
       if (enchantLevel == 9) {
         result += 3;
         if (getBless() % 128 == 0) {
           result += 3;
         }
       }
     } else if ((getItemId() >= 900127 && getItemId() <= 900129) || (
       getItemId() >= 900124 && getItemId() <= 900126)) {
       if (enchantLevel >= 1 && enchantLevel <= 2) {
         result += enchantLevel * 2;
       } else if (enchantLevel == 3) {
         result += 8;
       } else if (enchantLevel >= 4) {
         result += 2 + enchantLevel * 2;
       }
     }
     if (getItem().getType2() == 2 && getItem().getGrade() >= 0 && (
       getItem().getGrade() < 3 || getItem().getGrade() > 4) && (
       getItem().getType() == 8 || getItem().getType() == 12))
     {
       if (getItem().getType() == 8) {
         if (enchantLevel >= 9) {
           result = (enchantLevel == 10) ? 11 : 10;
         } else {
           result += (enchantLevel > 4) ? ((enchantLevel - 4) * 2 + 1) : 0;
           if (result > 10)
             result = 10;
         }
       } else {
         if (enchantLevel >= 9)
           result -= (enchantLevel == 10) ? 2 : 1;
         result += (enchantLevel > 4) ? ((enchantLevel - 4) * 2) : 0;
       }
     }


     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getPotionRecoveryRate(enchantLevel);
     }
     return result;
   }

   public int getPotionRecoveryRateValue() {
     int result = 0;
     int enchantLevel = getEnchantLevel();

     if (getItem().getItemId() == 900021 || getItemId() == 900099) {
       result += (enchantLevel + 1) * 2;
     } else if ((getItemId() >= 900127 && getItemId() <= 900129) || (
       getItemId() >= 900124 && getItemId() <= 900126)) {
       if (enchantLevel >= 1 && enchantLevel <= 2)
       { result += enchantLevel * 2; }
       else if (enchantLevel == 3)
       { result += 8; }
       else if (enchantLevel >= 4)
       { result += 2 + enchantLevel * 2; }
     } else if (getItemId() >= 900096 && getItemId() <= 900098 && enchantLevel > 2) {
       if (enchantLevel >= 1 && enchantLevel <= 2)
       { result += enchantLevel * 2; }
       else if (enchantLevel == 3)
       { result += 8; }
       else if (enchantLevel >= 4)
       { result += 2 + enchantLevel * 2; }
     } else if (getItem().getItemId() == 22230 || getItem().getItemId() == 222338) {
       result += (enchantLevel > 1) ? (6 + (enchantLevel - 1) * 2) : 2;
       if (getBless() % 128 == 0 && enchantLevel > 2) {
         result += 2;
       }
       if (enchantLevel == 8) {
         result += 2;
         if (getBless() % 128 == 0) {
           result++;
         }
       }
       if (enchantLevel == 9) {
         result += 3;
         if (getBless() % 128 == 0) {
           result += 3;
         }
       }
     } else if ((getItemId() >= 900127 && getItemId() <= 900129) || (
       getItemId() >= 900124 && getItemId() <= 900126)) {
       if (enchantLevel >= 1 && enchantLevel <= 2) {
         result += enchantLevel * 2;
       } else if (enchantLevel == 3) {
         result += 8;
       } else if (enchantLevel >= 4) {
         result += 2 + enchantLevel * 2;
       }
     }
     if (getItem().getType2() == 2 && getItem().getGrade() >= 0 && (
       getItem().getGrade() < 3 || getItem().getGrade() > 4) && (
       getItem().getType() == 8 || getItem().getType() == 12))
     {
       if (getItem().getType() == 8)
       { if (enchantLevel >= 9) {
           result += (enchantLevel == 10) ? ((enchantLevel - 5) * 2 - 1) : ((enchantLevel - 5) * 2);
         } else {
           result += (enchantLevel > 5) ? ((enchantLevel - 5) * 2 + 1) : 0;
         }  }
       else { if (enchantLevel >= 9)
           result -= (enchantLevel == 10) ? 2 : 1;
         result += (enchantLevel > 5) ? ((enchantLevel - 5) * 2) : 0; }

     }







     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getPotionRecoveryRate(enchantLevel);
     }
     return result;
   }

   public int getPotionRecoveryCancel() {
     int result = 0;
     int enchantLevel = getEnchantLevel();

     if (getItem().getItemId() == 900021 || getItemId() == 900099) {
       result += (enchantLevel + 1) * 2;
     } else if ((getItem().getItemId() >= 900049 && getItem().getItemId() <= 900051) || (
       getItem().getItemId() >= 900093 && getItem().getItemId() <= 900098)) {
       result += (enchantLevel > 4) ? (8 + enchantLevel - 4) : (enchantLevel * 2);
       if (getItemId() >= 900096 && getItemId() <= 900098 && enchantLevel > 2) {
         result += 2;
         if (enchantLevel > 4) {
           result += enchantLevel - 4;
         }
       }
     } else if (getItem().getItemId() == 22230 || getItem().getItemId() == 222338) {
       result += (enchantLevel > 1) ? (6 + (enchantLevel - 1) * 2) : 2;
       if (getBless() % 128 == 0 && enchantLevel > 2) {
         result += 2;
       }
       if (enchantLevel == 8) {
         result += 2;
         if (getBless() % 128 == 0) {
           result++;
         }
       }
       if (enchantLevel == 9) {
         result += 3;
         if (getBless() % 128 == 0) {
           result += 3;
         }
       }
     } else if ((getItemId() >= 900127 && getItemId() <= 900129) || (
       getItemId() >= 900124 && getItemId() <= 900126)) {
       if (enchantLevel >= 1 && enchantLevel <= 2) {
         result += enchantLevel * 2;
       } else if (enchantLevel == 3) {
         result += 8;
       } else if (enchantLevel >= 4) {
         result += 2 + enchantLevel * 2;
       }
     }
     if (getItem().getType2() == 2 && getItem().getGrade() >= 0 && (
       getItem().getGrade() < 3 || getItem().getGrade() > 4) && (
       getItem().getType() == 8 || getItem().getType() == 12)) {
       if (enchantLevel >= 9) {
         result--;
       }
       if (getItem().getType() == 8) {
         if (enchantLevel >= 9) {
           result += (enchantLevel == 10) ? 12 : 11;
         } else {
           result += (enchantLevel > 4) ? ((enchantLevel - 4) * 2 + 1) : 0;
         }
       } else {
         result += (enchantLevel > 4) ? ((enchantLevel - 4) * 2) : 0;
         if (enchantLevel >= 10) {
           result--;
         }
       }
     }
     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getPotionRecoveryRateCancel(enchantLevel);
     }
     return result;
   }

   private String getMagicName() {
     String name = null;
     int enchantLevel = getEnchantLevel();

     MJItemSkillModel atk = MJItemSkillModelLoader.getInstance().getAtk(getItem().getItemId());
     MJItemSkillModel def = MJItemSkillModelLoader.getInstance().getDef(getItem().getItemId());
     if (atk != null && atk.condition != 0) {
       if (atk.condition <= enchantLevel) {
         name = getItem().getMagicName();
       }
     } else if (def != null && def.condition != 0) {
       if (def.condition <= enchantLevel) {
         name = getItem().getMagicName();
       }
     } else {
       name = getItem().getMagicName();
     }











     return name;
   }

   public boolean isDrainHp() {
     if (getItem().getItemId() == 12)
       return true;
     if (getItem().getItemId() == 601)
       return true;
     if (getItem().getItemId() == 1123) {
       return true;
     }
     return false;
   }

   public boolean isDrainMp() {
     if (getItem().getItemId() == 126)
       return true;
     if (getItem().getItemId() == 127) {
       return true;
     }
     return false;
   }

   public boolean isHasteItem() {
     boolean flag = getItem().isHasteItem();

     return flag;
   }

   public int getStr() {
     int result = getItem().get_addstr();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 22383 &&
       this._cha != null && (
       this._cha.isCrown() || this._cha.isKnight() || this._cha.isDarkelf() || this._cha.isDragonknight() || this._cha.is전사() || this._cha.isFencer() || this._cha.isLancer())) {
       result += 2;
     }



     if ((getItemId() == 900282 || getItemId() == 120178) &&
       this._cha != null && (
       this._cha.isCrown() || this._cha.isKnight() || this._cha.isDarkelf() || this._cha.isDragonknight() || this._cha.is전사() || this._cha.isFencer() || this._cha.isLancer())) {
       result += 2;
     }




     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getStr(enchantLevel);
     }
     if (getBlessType() == 2) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getCon() {
     int result = getItem().get_addcon();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getCon(enchantLevel);
     }
     if (getBlessType() == 3) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getDex() {
     int result = getItem().get_adddex();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 22383 &&
       this._cha != null &&
       this._cha.isElf()) {
       result += 2;
     }



     if ((getItemId() == 900282 || getItemId() == 120178) &&
       this._cha != null &&
       this._cha.isElf()) {
       result += 2;
     }



     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getDex(enchantLevel);
     }
     if (getBlessType() == 4) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getWis() {
     int result = getItem().get_addwis();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getWis(enchantLevel);
     }
     if (getBlessType() == 6) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getInt() {
     int result = getItem().get_addint();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 22383 &&
       this._cha != null && (
       this._cha.isWizard() || this._cha.isBlackwizard())) {
       result += 2;
     }



     if ((getItemId() == 900282 || getItemId() == 120178) &&
       this._cha != null && (
       this._cha.isWizard() || this._cha.isBlackwizard())) {
       result += 2;
     }



     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getInt(enchantLevel);
     }
     if (getBlessType() == 5) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getCha() {
     int result = getItem().get_addcha();
     int enchantLevel = getEnchantLevel();
     if (getItem().getItemId() == 900076 || getItem().getItemId() == 20016 || getItem().getItemId() == 20112 ||
       getItem().getItemId() == 120016 || getItem().getItemId() == 120112) {
       result += (enchantLevel > 6) ? (enchantLevel - 6) : 0;
     } else if (getItem().getItemId() == 118) {
       result += (enchantLevel > 8) ? (enchantLevel - 8) : 0;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getCha(enchantLevel);
     }
     if (getBlessType() == 7) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public boolean isResistPoison() {
     return (getItemId() == 20298 || getItemId() == 20117 || getItemId() == 900263 || getItemId() == 900264 || getItemId() == 900265 || (getItemId() >= 22196 && getItemId() <= 22199));
   }

   public int getAttrDmg() {
     int result = 0;

     if (getItemId() == 3000516) {
       result += 3;
     }

     return result;
   }

   public int getDmgModifier() {
     int result = getItem().getDmgRate();
     int enchantLevel = getEnchantLevel();

     if (getItem().getType2() == 1 && getItem().getType() != 4 && getItem().getType() != 10 && getItem().getType() != 13) {
       result += getItem().getDmgModifier();
     }
     if ((getItemId() == 900234 || getItemId() == 900275) &&
       enchantLevel >= 4 && enchantLevel <= 8) {
       result += 1 + (enchantLevel - 4) * 1;
     }
     if ((getItemId() >= 22224 && getItemId() <= 22228) || getItemId() == 900195 || (getItemId() >= 222330 && getItemId() <= 222334) || getItemId() == 222291 || getItemId() == 222336) {
       if (getBless() % 128 == 0) {
         enchantLevel++;
       }
       result += (enchantLevel <= 4) ? 0 : (((enchantLevel >= 9 && getBless() % 128 == 0) || (enchantLevel >= 8 && getBless() % 128 == 1)) ? (enchantLevel - 3 + (enchantLevel - 8) * 1) : (enchantLevel - 4));
     } else if ((getItemId() == 222340 || getItemId() == 222341) && enchantLevel > 2) {
       if (getBless() % 128 == 0)
         enchantLevel++;
       result += (enchantLevel <= 4) ? 1 : ((enchantLevel <= 2) ? 0 : (enchantLevel - 3));
       if (enchantLevel >= 8) {
         result += enchantLevel - 8 + 2;
       }
       if (enchantLevel == 10 && getBless() % 128 == 0) {
         result++;
       }
     } else if (getItemId() == 900152 || getItemId() == 900081) {
       result += (enchantLevel > 4) ? (enchantLevel - 4) : 0;
     } else if (getItemId() == 900030 || getItemId() == 900226) {
       result += (enchantLevel > 8) ? 1 : 0;
     } else if (getItemId() == 900049 || getItemId() == 900093 || getItemId() == 900096) {
       result += (enchantLevel > 5) ? (enchantLevel - 5) : 0;
     } else if (getItemId() == 900026 || getItemId() == 900185) {
       result += (enchantLevel > 8) ? 1 : 0;
     } else if (getItemId() == 22003) {
       enchantLevel = (enchantLevel > 9) ? 9 : enchantLevel;
       result += (enchantLevel < 5) ? 0 : ((enchantLevel - 3) / 2);
     } else if (getItemId() == 900117 || getItemId() == 900219) {
       result += (enchantLevel > 6) ? (enchantLevel - 6) : 0;
     } else if (getItemId() == 900124) {
       result += (enchantLevel > 4) ? (enchantLevel - 4) : 0;
     } else if (getItemId() == 900127 &&
       enchantLevel >= 5) {
       result += enchantLevel - 4;
     }


     if (getItem().getType2() == 2 &&
       getItem().getGrade() >= 0 && (getItem().getGrade() < 3 || getItem().getGrade() > 4) && (getItem().getType() == 9 || getItem().getType() == 11)) {
       result += (enchantLevel <= 4) ? 0 : (enchantLevel - 4);
     }


     if (getItemId() == 22384 &&
       this._cha != null &&
       !this._cha.isWizard() && !this._cha.isBlackwizard() && !this._cha.isElf()) {
       result += 2;
     }



     if (getItem().getType2() == 2) {
       L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
       if (eb != null) {
         result += eb.getDmgModifier(enchantLevel);
       }
     }

     if (getBlessType() == 14) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getHitModifier() {
     int result = getItem().getHitRate();
     int enchantLevel = getEnchantLevel();

     if (getItem().getType2() == 1 &&
       getItem().getType() != 4 && getItem().getType() != 10 && getItem().getType() != 13) {
       result += getItem().getHitModifier();
     }
     if (getItemId() == 222291 || getItemId() == 222336) {
       if (getBless() % 128 == 0)
         enchantLevel++;
       result += (enchantLevel <= 4) ? 0 : (((enchantLevel >= 9 && getBless() % 128 == 0) || (enchantLevel >= 8 && getBless() % 128 == 1)) ? (enchantLevel - 3 + (enchantLevel - 8) * 1) : (enchantLevel - 4));
     } else if (getItemId() == 22229 || getItemId() == 222337) {
       if (getBless() % 128 == 0 && enchantLevel >= 6) {
         result += 1 + (enchantLevel - 6) * 2;
       } else if (getBless() % 128 != 0 && getEnchantLevel() >= 7) {
         result += 1 + (enchantLevel - 7) * 2;
       }

     } else if (getItemId() == 222317) {
       if (enchantLevel == 7)
       { result += 4; }
       else if (enchantLevel == 8)
       { result += 5; }
       else if (enchantLevel >= 9)
       { result += 6; }
     } else if (getItemId() == 22373) {
       enchantLevel = (enchantLevel > 9) ? 9 : enchantLevel;
       result += (enchantLevel > 5) ? (enchantLevel - 4) : 0;
     } else if (getItemId() == 900049 || getItemId() == 900093 || getItemId() == 900096) {
       result += (enchantLevel == 5) ? 1 : ((enchantLevel > 5) ? (enchantLevel - 5) : 0);
     } else if (getItemId() == 900154 || getItemId() == 900117 || getItemId() == 900219 || getItemId() == 900083) {
       result += (enchantLevel > 4) ? (enchantLevel - 4) : 0;
     } else if (getItemId() == 900124) {
       result += (enchantLevel > 3) ? (enchantLevel - 3) : 0;


     }
     else if (getItemId() == 900026 || getItemId() == 900185) {
       if (enchantLevel >= 7) {
         result += (enchantLevel > 7) ? ((enchantLevel - 7) * 2) : 0;
         if (getBless() % 128 == 0) {
           result++;
         }
       }
     } else if (getItemId() == 900127) {
       if (enchantLevel >= 4 && enchantLevel <= 5) {
         result++;
       } else if (enchantLevel >= 6) {
         result += 1 + enchantLevel - 5;
       }
     } else if (getItemId() == 900081 &&
       enchantLevel >= 6) {
       result += 1 + (enchantLevel - 6) * 2;
     }


     if ((getItemId() == 22383 || getItemId() == 22384) &&
       this._cha != null &&
       !this._cha.isElf() && !this._cha.isWizard() && !this._cha.isBlackwizard()) {
       result += 2;
     }



     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getHitModifier(enchantLevel);
     }
     if (getBlessType() == 15) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getBowHitModifier() {
     int result = getItem().getBowHitRate();
     int enchantLevel = getEnchantLevel();

     if (getItem().getType2() == 1 && (
       getItem().getType() == 4 || getItem().getType() == 10 || getItem().getType() == 13)) {
       result += getItem().getHitModifier();
     }

     if (getItemId() == 222291 || getItemId() == 222336) {
       if (getBless() % 128 == 0)
         enchantLevel++;
       result += (enchantLevel <= 4) ? 0 : (((enchantLevel >= 9 && getBless() % 128 == 0) || (enchantLevel >= 8 && getBless() % 128 == 1)) ? (enchantLevel - 3 + (enchantLevel - 8) * 1) : (enchantLevel - 4));
     } else if (getItemId() == 22229 || getItemId() == 222337) {
       if (getBless() % 128 == 0 && enchantLevel >= 6) {
         result += 1 + (enchantLevel - 6) * 2;
       } else if (getBless() % 128 != 0 && getEnchantLevel() >= 7) {
         result += 1 + (enchantLevel - 7) * 2;
       }
     } else if (getItemId() == 900094 || getItemId() == 900097) {
       result += (enchantLevel == 5) ? 1 : ((enchantLevel > 5) ? (enchantLevel - 5) : 0);
     } else if (getItemId() == 3000516 || getItemId() == 410012) {
       result += 3;
     } else if (getItemId() == 4100041) {
       result += 5;
     } else if (getItemId() == 900118 || getItemId() == 900220) {
       result += (enchantLevel > 4) ? (enchantLevel - 4) : 0;
     } else if (getItemId() == 900082) {
       result += (enchantLevel > 5) ? (enchantLevel - 5) : 0;
       if (enchantLevel >= 7)
         result++;
       if (enchantLevel >= 8)
         result++;
     } else if (getItemId() == 900128 || getItemId() == 900125) {
       result += (enchantLevel > 3) ? (enchantLevel - 3) : 0;
       if (getItemId() == 900128 && enchantLevel > 4)
         result--;
     } else if ((getItemId() == 900027 || getItemId() == 900186) &&
       enchantLevel >= 7) {
       result += (enchantLevel > 7) ? ((enchantLevel - 7) * 2) : 0;
       if (getBless() % 128 == 0) {
         result++;
       }
     }


     if ((getItemId() == 22383 || getItemId() == 22384) &&
       this._cha != null &&
       this._cha.isElf()) {
       result += 2;
     }






     if (getItem().getType2() == 0 && (getItem().getType() == 0 || getItem().getType() == 15)) {
       result = getItem().getHitModifier();
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getBowHitModifier(enchantLevel);
     }
     if (getBlessType() == 17) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getBowDmgModifier() {
     int result = getItem().getBowDmgRate();
     int enchantLevel = getEnchantLevel();

     if (getItem().getType2() == 1 && (getItem().getType() == 4 || getItem().getType() == 10 || getItem().getType() == 13)) {
       result += getItem().getDmgModifier();
     }

     if (getItem().getType2() == 2 &&
       getItem().getGrade() >= 0 && (getItem().getGrade() < 3 || getItem().getGrade() > 4) && (
       getItem().getType() == 9 || getItem().getType() == 11)) {
       result += (enchantLevel <= 4) ? 0 : (enchantLevel - 4);
     }


     if ((getItemId() >= 22224 && getItemId() <= 22228) || getItemId() == 900195 || (
       getItemId() >= 222330 && getItemId() <= 222334) || getItemId() == 222291 || getItemId() == 222336) {
       if (getBless() % 128 == 0)
         enchantLevel++;
       result += (enchantLevel <= 4) ? 0 : (((enchantLevel >= 9 && getBless() % 128 == 0) || (enchantLevel >= 8 && getBless() % 128 == 1)) ? (enchantLevel - 3 + (enchantLevel - 8) * 1) : (enchantLevel - 4));
     } else if ((getItemId() == 222340 || getItemId() == 222341) && enchantLevel > 2) {
       if (getBless() % 128 == 0)
         enchantLevel++;
       result += (enchantLevel <= 4) ? 1 : ((enchantLevel <= 2) ? 0 : (enchantLevel - 3));
       if (enchantLevel >= 8) {
         result += enchantLevel - 8 + 2;
       }
       if (enchantLevel == 10 && getBless() % 128 == 0) {
         result++;
       }
     } else if (getItemId() == 22000) {
       enchantLevel = (enchantLevel > 9) ? 9 : enchantLevel;
       result += (enchantLevel < 5) ? 0 : ((enchantLevel - 3) / 2);
     } else if (getItemId() == 900050 || getItemId() == 900094 || getItemId() == 900097) {
       result += (enchantLevel > 5) ? (enchantLevel - 5) : 0;
     } else if (getItemId() == 900027 || getItemId() == 900186) {
       result += (enchantLevel > 8) ? 1 : 0;
     } else if (getItemId() == 900031 || getItemId() == 900227) {
       result += (enchantLevel > 8) ? 1 : 0;
     } else if (getItemId() == 900153 || getItemId() == 900082) {
       result += (enchantLevel > 4) ? (enchantLevel - 4) : 0;
     } else if (getItemId() == 410012 || getItemId() == 4100041) {
       result += 3;
     } else if (getItemId() == 900118 || getItemId() == 900220) {
       result += (enchantLevel > 6) ? (enchantLevel - 6) : 0;
     } else if (getItemId() == 900128 || getItemId() == 900125) {
       result += (enchantLevel > 4) ? (enchantLevel - 4) : 0;
     }
     if ((getItemId() == 900235 || getItemId() == 900276) &&
       enchantLevel >= 4 && enchantLevel <= 8) {
       result += 1 + (enchantLevel - 4) * 1;
     }




     if (getItem().getType2() == 0 && (getItem().getType() == 0 || getItem().getType() == 15)) {
       result = getItem().getDmgModifier();
     }

     if (getItemId() == 22384 &&
       this._cha != null &&
       this._cha.isElf()) {
       result += 2;
     }



     if (getItem().getType2() == 2) {
       L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
       if (eb != null) {
         result += eb.getBowDmgModifier(enchantLevel);
       }
     }

     if (getBlessType() == 16) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getShortDmgModifier() {
     int result = 0;

     if (getItemId() == 4100042 || getItemId() == 4100465 || getItemId() == 410012) {
       result += 3;
     }

     return result;
   }

   public int getShortHitModifier() {
     int result = 0;

     if (getItemId() == 4100042 || getItemId() == 4100465) {
       result += 5;
     } else if (getItemId() == 410012) {
       result += 3;
     }

     return result;
   }

   public int getShortCriticalValue() {
     int itemid = getItem().getItemId();
     int enchant = getEnchantLevel();

     int critical = getItem().get_melee_critical_probability();

     if (itemid == 900152 || itemid == 900081) {
       critical += (enchant == 6) ? 1 : ((enchant == 7) ? 3 : ((enchant >= 8) ? 5 : 0));

     }
     else if (itemid >= 22208 && itemid <= 22209) {
       if (enchant == 7) {
         critical++;
       } else if (enchant == 8) {
         critical += 2;
       } else if (enchant >= 9) {
         critical += 3;
       }
     }
     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       critical += eb.getShortCritical(enchant);
     }
     return critical;
   }

   public int getLongCriticalValue() {
     int itemid = getItem().getItemId();
     int enchant = getEnchantLevel();

     int critical = getItem().get_missile_critical_probability();

     if (itemid == 900153 || itemid == 900082) {
       critical += (enchant == 6) ? 1 : ((enchant == 7) ? 3 : ((enchant >= 8) ? 5 : 0));
     } else if (itemid == 22210) {
       if (enchant == 7) {
         critical++;
       } else if (enchant == 8) {
         critical += 2;
       } else if (enchant >= 9) {
         critical += 3;
       }
     }
     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       critical += eb.getLongCritical(enchant);
     }
     return critical;
   }

   public int getMagicCriticalValue() {
     int itemid = getItem().getItemId();
     int enchant = getEnchantLevel();

     int critical = getItem().get_magic_critical_probability();

     if (itemid == 900154 || itemid == 900083) {
       critical += (enchant == 6) ? 1 : ((enchant == 7) ? 2 : ((enchant >= 8) ? 4 : 0));
     } else if (itemid == 325) {
       critical += (enchant > 6) ? (enchant - 6) : 0;
     } else if (itemid == 22211) {
       if (enchant == 7) {
         critical++;
       } else if (enchant == 8) {
         critical += 2;
       } else if (enchant >= 9) {
         critical += 3;
       }
     }
     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       critical += eb.getMagicCritical(enchant);
     }
     return critical;
   }

   public int getAc() {
     int result = this._item.get_ac();

     return result;
   }

   public int getTitanPercent() {
     int result = getItem().getTitanPercent();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getTitanRate(enchantLevel);
     }
     if (getBlessType() == 31) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getFoeDmg() {
     int result = 0;
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getFoeDmg(enchantLevel);
     }
     return result;
   }

   public int getDefenseFire() {
     int result = getItem().get_defense_fire();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getDefenseFire(enchantLevel);
     }
     return result;
   }

   public int getDefenseWater() {
     int result = getItem().get_defense_water();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getDefenseWater(enchantLevel);
     }
     return result;
   }

   public int getDefenseWind() {
     int result = getItem().get_defense_wind();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getDefenseWind(enchantLevel);
     }
     return result;
   }

   public int getDefenseEarth() {
     int result = getItem().get_defense_earth();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getDefenseEarth(enchantLevel);
     }
     return result;
   }

   public int getDefenseAll() {
     int result = getItem().get_defense_all();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getDefenseAll(enchantLevel);
     }
     return result;
   }

   public int getDamagePlusRate() {
     int result = 0;
     int enchant = getEnchantLevel();

     if (getItem().getItemId() == 222340 || getItem().getItemId() == 222341) {
       if (getBless() % 128 == 0) {
         result += (enchant > 3) ? (enchant - 2) : 0;
       } else {
         result += (enchant > 4) ? (enchant - 3) : 0;
       }
       if (enchant >= 9) {
         result++;
       }
     }

     return result;
   }

   public int getDamagePlusValue() {
     int result = 0;
     int enchant = getEnchantLevel();

     if (getItem().getItemId() == 222340 || getItem().getItemId() == 222341) {
       result += 20;
       if (enchant >= 8) {
         result += 5;
         if (getBless() % 128 == 0) {
           result += 5;
         }
       }
     }

     return result;
   }

   public int getMagicDodge() {
     int result = 0;
     int enchantLevel = getEnchantLevel();

     if (getItem().getItemId() == 22228 || getItem().getItemId() == 222334 || getItemId() == 900195) {
       if (getBless() % 128 == 0) {
         result += (enchantLevel > 5) ? (1 + (enchantLevel - 6) * 2) : 0;
       } else {
         result += (enchantLevel > 6) ? (1 + (enchantLevel - 7) * 2) : 0;
       }
       if (enchantLevel >= 8) {
         if (getBless() % 128 == 0 &&
           enchantLevel == 8) {
           result++;
         }

         result += enchantLevel - 8 + 1;
       }
     }
     if (getItemId() >= 900234 && getItemId() <= 900236 &&
       enchantLevel >= 6 && enchantLevel <= 8) {
       result += 2 + (enchantLevel - 6) * 2;
     }
     if (getItemId() >= 900275 && getItemId() <= 900277 &&
       enchantLevel >= 5 && enchantLevel <= 8) {
       result += 1 + (enchantLevel - 5) * 2;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getMagicDodge(enchantLevel);
     }
     if (getBlessType() == 36) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getEnchantDmgRate() {
     int result = getEnchantLevel();
     int enchantLevel = getEnchantLevel();

     if (getItem().getType2() != 1) {
       return result;
     }

     int enchant = getEnchantLevel();
     int itemId = getItem().getItemId();




     if (itemId == 66 || itemId == 61 || itemId == 12 || itemId == 134 || itemId == 86 || itemId == 7000258 || itemId == 7000238 || (itemId >= 202011 && itemId <= 202015) || itemId == 7000265) {
       result += enchant;
     } else if (itemId == 7000239 || itemId == 7000240 || itemId == 203041 || itemId == 203042 || itemId == 203065 || itemId == 7000264 || itemId == 7000262 || itemId == 7000263 || itemId == 7000267) {
       result *= 5;
     }
     else if (enchant > 9) {
       result += enchant - 9;
     }


     if (getItem().getType2() == 1) {
       L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
       if (eb != null) {
         result += eb.getDmgModifier(enchantLevel);
         result += eb.getBowDmgModifier(enchantLevel);
       }
     }

     return result;
   }

   public int getAinEfficiency() {
     int result = getItem().get_addeinhasadper();
     int enchant = getEnchantLevel();








     if (getBlessType() == 32) {
       result += getBlessTypeValue();
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getEinhasadEfficiency(enchant);
     }


     return result;
   }

   public int getAbnormalStatusPvpDamageReduction() {
     int result = 0;
     int enchant = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getAbnormalStatusPvpDamageReduction(enchant);
     }


     return result;
   }


   public int getMpAr16() {
     int result = getItem().getMpAr16();

     return result;
   }

   public int getPVPDmgReducIgnore() {
     int result = getItem().getPVPWeaponReductionCancel();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getPVPDmgReducIgnore(enchantLevel);
     }
     if (getBlessType() == 39) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getPVPMdmgReduction() {
     int result = getItem().getPVPMagicReduction();
     int enchantLevel = getEnchantLevel();
     int itemId = getItem().getItemId();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getPvpMdmgReduction(enchantLevel);
     }
     if (getBlessType() == 40) {
       result += getBlessTypeValue();
     }

     if (itemId >= 900124 && itemId <= 900126) {
       if (enchantLevel == 5)
         result++;
       if (enchantLevel == 6)
         result += 2;
       if (enchantLevel == 7)
         result += 3;
       if (enchantLevel >= 8) {
         result += 5;
       }
     }
     return result;
   }

   public int getPVPMDmgReducIgnore() {
     int result = getItem().getPVPMagicReductionCancel();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getPvpMdmgReducIgnore(enchantLevel);
     }
     if (getBlessType() == 41) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getDG() {
     int result = getItem().getDG();
     int enchantLevel = getEnchantLevel();

     if (getItemId() >= 900234 && getItemId() <= 900236 &&
       enchantLevel >= 6 && enchantLevel <= 8) {
       result += 2 + (enchantLevel - 6) * 2;
     }
     if (getItemId() >= 900275 && getItemId() <= 900277 &&
       enchantLevel >= 5 && enchantLevel <= 8) {
       result += 1 + (enchantLevel - 5) * 2;
     }

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getDG(enchantLevel);
     }
     if (getBlessType() == 35) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getHpPercent() {
     int result = getItem().getHpPercent();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 900234 || getItemId() == 900275)
       if (enchantLevel >= 0 && enchantLevel <= 6) {
         result += 2 + enchantLevel * 2;
       } else if (enchantLevel == 7) {
         result += 17;
       } else if (enchantLevel == 8) {
         result += 20;
       }
     if (getItemId() == 900235 || getItemId() == 900276)
       if (enchantLevel >= 0 && enchantLevel <= 2) {
         result += 1 + enchantLevel * 1;
       } else if (enchantLevel >= 3) {
         result += 1 + (enchantLevel - 1) * 2;
       } else if (enchantLevel == 8) {
         result += 20;
       }
     if (getItemId() == 900236 || getItemId() == 900277)
       if (enchantLevel >= 0 && enchantLevel <= 7) {
         result += 1 + enchantLevel * 1;
       } else if (enchantLevel == 8) {
         result += 10;
       }
     if (getItemId() == 900237 || getItemId() == 900278) {
       if (enchantLevel >= 0 && enchantLevel <= 7) {
         result += 1 + enchantLevel * 1;
       } else if (enchantLevel == 8) {
         result += 10;
       }
     }
     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getHpPercent(enchantLevel);
     }
     if (getBlessType() == 42) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getMpPercent() {
     int result = getItem().getMpPercent();
     int enchantLevel = getEnchantLevel();

     if (getItemId() == 900234 || getItemId() == 900275)
       if (enchantLevel >= 0 && enchantLevel <= 7) {
         result += 1 + enchantLevel * 1;
       } else if (enchantLevel == 8) {
         result += 10;
       }
     if (getItemId() == 900235 || getItemId() == 900276)
       if (enchantLevel >= 0 && enchantLevel <= 2) {
         result += 1 + enchantLevel * 1;
       } else if (enchantLevel >= 3) {
         result += 1 + (enchantLevel - 1) * 2;
       } else if (enchantLevel == 8) {
         result += 20;
       }
     if (getItemId() == 900236 || getItemId() == 900277)
       if (enchantLevel >= 0 && enchantLevel <= 6) {
         result += 2 + enchantLevel * 2;
       } else if (enchantLevel == 7) {
         result += 17;
       } else if (enchantLevel == 8) {
         result += 25;
       }
     if (getItemId() == 900237 || getItemId() == 900278) {
       if (enchantLevel >= 0 && enchantLevel <= 7) {
         result += 1 + enchantLevel * 1;
       } else if (enchantLevel == 8) {
         result += 10;
       }
     }
     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getMpPercent(enchantLevel);
     }
     if (getBlessType() == 43) {
       result += getBlessTypeValue();
     }

     return result;
   }

   public int getImmuneIgnore() {
     int result = getItem().getIIg();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
     if (eb != null) {
       result += eb.getImmuneIgnore(enchantLevel);
     }
     if (getBlessType() == 44) {
       result += getBlessTypeValue();
     }

     return result;
   }
   public int getCCIncrease() {
     int result = getItem().getCCIncrease();
     int enchantLevel = getEnchantLevel();

     L1EnchantBonus eb = L1EnchantBonus.get(getItemId());








     if (eb != null) {
       result += eb.getCCIncrease(enchantLevel);
     }

     return result;
   }

   public int getAttrLevelPacketNumber() {
     int result = getAttrEnchantBit(getAttrEnchantLevel());

     return result;
   }



   public boolean isSupportItem() {
     return this._isSupportItem;
   }

   public void setSupportItem(boolean flag) {
     this._isSupportItem = flag;
   }



   public void setRange(int type) {
     this._range = type;
   }

   public int getRange() {
     return this._range;
   }






   public int getBlessType() {
     return this._bless_type;
   }

   public void setBlessType(int i) {
     this._bless_type = i;
   }



   public int getBlessTypeValue() {
     return this._bless_type_value;
   }

   public void setBlessTypeValue(int i) {
     this._bless_type_value = i;
   }




   public int getSmeltingValue() {
     return this._smelting_value;
   }
   public void setSmeltingValue(int itemid) {
     this._smelting_value = itemid;
   }


   public int getSmeltingItemId1() {
     return this._smelting_itemid_1;
   }
   public void setSmeltingItemId1(int itemid) {
     this._smelting_itemid_1 = itemid;
   }

   public int getSmeltingItemId2() {
     return this._smelting_itemid_2;
   }
   public void setSmeltingItemId2(int itemid) {
     this._smelting_itemid_2 = itemid;
   }

   public int getSmeltingKind1() {
     return this._smelting_kind_1;
   }
   public void setSmeltingKind1(int kind) {
     this._smelting_kind_1 = kind;
   }

   public int getSmeltingKind2() {
     return this._smelting_kind_2;
   }
   public void setSmeltingKind2(int kind) {
     this._smelting_kind_2 = kind;
   }

   public void setHalpas_Time(Timestamp endtime) {
     this._halpas_time = endtime;
   }
   public Timestamp getHalpas_Time() {
     return this._halpas_time;
   }



   public void set_Cantunseal(int i) {
     this._cant_unseal = i;
   }

   public int get_Cantunseal() {
     return this._cant_unseal;
   }

   public int getStatusBit() {
     int b = 0;
     if (isIdentified())
       b = 1;
     if (!getItem().isTradable()) {
       b |= 0x2;
     }

     if (getItem().isCantDelete())
       b |= 0x4;
     if (getItem().get_safeenchant() < 0) {
       b |= 0x8;
     }
     int bless = getBless();
     if (bless >= 128 && bless <= 131) {
       b |= 0x2;
       b |= 0x4;
       b |= 0x8;
       b |= 0x20;
     } else if (bless > 131) {
       b |= 0x40;
     }
     if (getItem().isStackable()) {
       b |= 0x80;
     }
     return b;
   }
 }


