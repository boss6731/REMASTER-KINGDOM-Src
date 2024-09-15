package l1j.server.server.model.Instance;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.BuyLimitSystem.BuyLimitSystem;
import l1j.server.BuyLimitSystem.BuyLimitSystemAccount;
import l1j.server.BuyLimitSystem.BuyLimitSystemAccountTable;
import l1j.server.BuyLimitSystem.BuyLimitSystemCharacter;
import l1j.server.BuyLimitSystem.BuyLimitSystemCharacterTable;
import l1j.server.InvenBonusItem.InvenBonusItemInfo;
import l1j.server.InvenBonusItem.InvenBonusItemLoader;
import l1j.server.MJCTSystem.MJCTObject;
import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.MJCTSystem.Loader.MJCTSystemLoader;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJExpAmpSystem.MJItemExpBonus;
import l1j.server.MJINNSystem.MJINNHelper;
import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SMELTING_UPDATE_SLOT_INFO_NOTI;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.ArmorSetTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.ShopBuyLimitInfo;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1ArmorSet.L1ArmorSetImpl;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EquipmentTimer;
import l1j.server.server.model.L1ItemOwnerTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.item.function.L1BlessTypeEnchant;
import l1j.server.server.model.item.function.L1EnchantBonus;
import l1j.server.server.model.item.function.L1EtcItemViewByte;
import l1j.server.server.model.item.function.L1MagicDoll;
import l1j.server.server.model.item.smelting.SmeltingScrollInfo;
import l1j.server.server.model.item.smelting.SmeltingScrollLoader;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1ArmorSets;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ItemBookMark;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.templates.ShopBuyLimit;
import l1j.server.server.templates.eShopBuyLimitType;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.ItemPresentOutStream;
import l1j.server.server.utils.MJBytesOutputStream;
import l1j.server.server.utils.SQLUtil;
import l1j.server.tempSkillSystem.tempSkillSystemInfo;
import l1j.server.tempSkillSystem.tempSkillSystemLoader;

public class L1ItemInstance extends L1Object {

	/**
	 * 表示物品是否可交易的狀態(enum) <table BORDER CELLPADDING=3
	 * CELLSPACING=1>
	 * <tr>
	 * <td>tradable</td>
	 * <td>可交易</td>
	 * </tr>
	 * <tr>
	 * <td>equipped</td>
	 * <td>裝備中</td>
	 * </tr>
	 * <tr>
	 * <td>seal</td>
	 * <td>封印</td>
	 * </tr>
	 * <tr>
	 * <td>imprinted</td>
	 * <td>刻印</td>
	 * </tr>
	 * <tr>
	 * <td>oblivion</td>
	 * <td>寵物未被遺忘</td>
	 * </tr>
	 * <tr>
	 * <td>summonedPet</td>
	 * <td>召喚中的寵物</td>
	 * </tr>
	 * <tr>
	 * <td>summonedDoll</td>
	 * <td>召喚中的人偶</td>
	 * </tr>
	 * <tr>
	 * <td>nonTradable</td>
	 * <td>不可交易的物品</td>
	 * </tr>
	 * </table>
	 **/
	public enum ItemTradableStatus {
		/**
		 * <b>可交易</b>
		 **/
		tradable,

		/**
		 * <b>裝備中</b>
		 **/
		equipped,

		/**
		 * <b>封印</b>
		 **/
		seal,

		/**
		 * <b>刻印</b>
		 **/
		imprinted,

		/**
		 * <b>寵物未被遺忘</b>
		 **/
		oblivion,

		/**
		 * <b>召喚中的寵物</b>
		 **/
		summonedPet,

		/**
		 * <b>召喚中的人偶</b>
		 **/
		summonedDoll,

		/**
		 * <b>不可交易的</b>
		 **/
		nonTradable,

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
		if (owner.getPetList().containsKey(item.getId())) {
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
		// sb.append(get_blessed_description(bless));
		sb.append(get_attribute_enchant_description(elemental));
		sb.append(get_enchant_description(enchant));
		sb.append(name);
		sb.append(get_level_description(level));
		return sb.toString();
	}

	public static String to_simple_description(String name, int bless, int enchant, int elemental, int dollbonuslevel, int dollbonusvalue, int bless_level, int BlessType, int BlessTypeValue, int level, int count) {
		// TODO 這裡只需加入與交換券相關的名稱，添加後會顯示在名稱旁邊
		StringBuilder sb = new StringBuilder(name.length() + 32);
		// sb.append(get_blessed_description(bless));
		sb.append(get_attribute_enchant_description(elemental));
		sb.append(get_enchant_description(enchant));
		sb.append(name);
//		sb.append(dollbonuslevel);
//		sb.append(dollbonusvalue);
//		sb.append(get_bless_level(bless_level));
//		sb.append(get_level_description(level));
//		sb.append(BlessType);
//		sb.append(BlessTypeValue);

		if (count > 1) {
			sb.append(" (").append(count).append(")");
		}
		return sb.toString();
	}

		public static String get_level_description(int level) {
			switch (level) {
				case 1:
					return "[第1階段]";
				case 2:
					return "[第2階段]";
				case 3:
					return "[第3階段]";
				case 4:
					return "[第4階段]";
				case 5:
					return "[第5階段]";
			}
			return "";
		}

	public static String get_enchant_description(int enchant) {
		if (enchant > 0)
			return String.format("+%d ", enchant);
		else if (enchant < 0)
			return String.format("-%d ", enchant);
		return "";
	}

		public static String get_blessed_description(int bless) {
			switch (bless) {
				case 0:
					return "受到祝福的 ";
				case 2:
					return "被詛咒的 ";
			}
			return "";
		}

		public static String get_bless_level(int bless) {
			switch (bless) {
				case 1:
					return "受到祝福的 ";
				case 2:
					return "受到祝福的 ";
				case 3:
					return "受到祝福的 ";
			}
			return "";
		}

	private static final String[] elemental_descriptions = new String[] { "", // 무속성
			"$6115", // 화령1
			"$6116", // 화령2
			"$6117", // 화령3
			"$14361", // 화령4
			"$14365", // 화령5

			"$6118", // 수령1
			"$6119", // 수령2
			"$6120", // 수령3
			"$14362", // 수령4
			"$14366", // 수령5

			"$6121", // 풍령1
			"$6122", // 풍령2
			"$6123", // 풍령3
			"$14363", // 풍령4
			"$14367", // 풍령5

			"$6124", // 지령1
			"$6125", // 지령2
			"$6126", // 지령3
			"$14364", // 지령4
			"$14368", // 지령5
	};

	public static String get_attribute_enchant_description(int attrEnchantLevel) {
		return attrEnchantLevel >= elemental_descriptions.length ? "" : attrEnchantLevel < 0 ? "" : elemental_descriptions[attrEnchantLevel];
	}

	private static final int[] _attrMask = new int[] { 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4 };

	public static int attrEnchantToElementalType(int attrEnchantLevel) {
		if (attrEnchantLevel < 0) {
			attrEnchantLevel = 0;
			System.out.println(String.format("[L1Iteminstance]: attrEnchantLevel: %d 的數值存在。(character_items) 請確認!", attrEnchantLevel));
		}
		return _attrMask[attrEnchantLevel];
	}

	public static int attrEnchantToElementalType(L1ItemInstance item) {
		return attrEnchantToElementalType(item.getAttrEnchantLevel());
	}

	public static int pureAttrEnchantLevel(int attrEnchantLevel) {
		return attrEnchantLevel <= 0 ? attrEnchantLevel : attrEnchantLevel - ((attrEnchantToElementalType(attrEnchantLevel) - 1) * 5);
	}

	public static int pureAttrEnchantLevel(L1ItemInstance item) {
		return pureAttrEnchantLevel(item.getAttrEnchantLevel());
	}

	public static boolean equalsElement(L1ItemInstance item, int elementalType, int elementalValue) {
		int attr = item.getAttrEnchantLevel();
		int type = attrEnchantToElementalType(attr);
		if (type != elementalType)
			return false;

		int value = attr - ((type - 1) * 5);
		return value == elementalValue;
	}

	public static int calculateElementalEnchant(int elementalType, int elementalValue) {
		if (elementalType == 0 && elementalValue == 0)
			return 0;
		return ((elementalType - 1) * 5) + elementalValue;
	}

	public static final int CHAOS_SPIRIT = 1;
	public static final int CORRUPT_SPIRIT = 2;
	public static final int BALLACAS_SPIRIT = 3;
	public static final int ANTARAS_SPIRIT = 4;
	public static final int LINDBIOR_SPIRIT = 5;
	public static final int PAPURION_SPIRIT = 6;
	public static final int DEATHKNIGHT_SPIRIT = 7;
	public static final int BAPPOMAT_SPIRIT = 8;
	public static final int BALLOG_SPIRIT = 9;
	public static final int ARES_SPIRIT = 10;

	private static final long serialVersionUID = 1L;

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

	/** 패키지상점 **/
	private boolean _isPackage = false;

	private int bless;

	private int _lastWeight;

	/** 인형 착용 여부 **/
	private boolean _isDollOn = false;

	private final LastStatus _lastStatus = new LastStatus();

	private Map<Integer, EnchantTimer> _skillEffect = new HashMap<Integer, EnchantTimer>();

	public L1PcInstance _cha;

	public L1ItemInstance() {
		_count = 1;
		_enchantLevel = 0;
		_specialEnchant = 0;
		_bookmarks = new ArrayList<L1ItemBookMark>();
	}

	public L1ItemInstance(L1Item item, int count) {
		this();
		setItem(item);
		setCount(count);
	}

	public L1ItemInstance(L1Item item) {
		this(item, 1);
	}

	public void clickItem(L1Character cha, ClientBasePacket packet) {
	}

	/** 인형 착용 여부 **/
	public boolean isDollOn() {
		return _isDollOn;
	}

	public void setDollOn(boolean DollOn) {
		_isDollOn = DollOn;
	}

	/** 인형 착용 여부 **/

	public boolean isSpecialEnchantable() {
		return (_specialEnchant & 0xFF) == 1;
	}

	public void setSpecialEnchantable() {
		_specialEnchant = 1;
	}

	public int getSpecialEnchant() {
		return _specialEnchant;
	}

	public int getSpecialEnchant(int index) {
		return ((_specialEnchant >> (8 * index)) & 0xFF);
	}

	public void setSpecialEnchant(int enchant) {
		_specialEnchant = enchant;
	}

	public void setSpecialEnchant(int index, int enchant) {
		_specialEnchant |= enchant << (8 * index);
	}

	public boolean isIdentified() {
		return _isIdentified;
	}

	public void setIdentified(boolean identified) {
		_isIdentified = identified;
	}

	public String getName() {
		return _item.getName();
	}

	public int getCount() {
		return _count;
	}

	public void setCount(int count) {
		_count = count;
	}

	public boolean isEquipped() {
		return _isEquipped;
	}

	public void setEquipped(boolean equipped) {
		_isEquipped = equipped;
	}

	public L1Item getItem() {
		return _item;
	}

	public void setItem(L1Item item) {
		_item = item;
		_itemId = item.getItemId();
	}

	public int getItemDescId(){
		return _itemDescId;
	}

	public int getItemId() {
		return _itemId;
	}

	public void setItemId(int itemId) {
		_itemId = itemId;
	}

	public boolean isStackable() {
		return _item.isStackable();
	}

	@Override
	public void onAction(L1PcInstance player) {
	}

	public int getEnchantLevel() {
		return _enchantLevel;
	}

	public void setEnchantLevel(int enchantLevel) {
		_enchantLevel = enchantLevel;
	}

	public int getAttrEnchantLevel() {
		return _attrenchantLevel;
	}

	public int getHitModifierByAttrEnchant() {
		if (getAttrEnchantLevel() == 0) {
			return 0;
		} else if (getAttrEnchantLevel() % 3 == 0) {
			return 3;
		}

		return getAttrEnchantLevel() % 3;
	}

	public void setAttrEnchantLevel(int attrenchantLevel) {
		_attrenchantLevel = attrenchantLevel;
	}

	public int get_gfxid() {
		return _item.getGfxId();
	}

	public int get_durability() {
		return _durability;
	}

	public int getChargeCount() {
		return _chargeCount;
	}

	public void setChargeCount(int i) {
		_chargeCount = i;
	}

	public int getRemainingTime() {
		return _remainingTime;
	}

	public void setRemainingTime(int i) {
		_remainingTime = i;
	}

	public void setLastUsed(Timestamp t) {
		_lastUsed = t;
	}

	public Timestamp getLastUsed() {
		return _lastUsed;
	}

	public long getLastUsedMillis() {
		return _lastUsed == null ? 0L : _lastUsed.getTime();
	}

	public int getBless() {
		return bless;
	}

	public void setBless(int i) {
		bless = i;
	}

	public int getLastWeight() {
		return _lastWeight;
	}

	public void setLastWeight(int weight) {
		_lastWeight = weight;
	}

	public Timestamp getEndTime() {
		return _endTime;
	}

	public void setEndTime(Timestamp t) {
		_endTime = t;
	}

	/** 패키지상점 **/
	public boolean isPackage() {
		return _isPackage;
	}

	public void setPackage(boolean _isPackage) {
		this._isPackage = _isPackage;
	}

	private long _itemdelay3;

	public long getItemdelay3() {
		return _itemdelay3;
	}

	public void setItemdelay3(long itemdelay3) {
		_itemdelay3 = itemdelay3;
	}

	public int getMr() {
		int result = _item.get_mdef();
		int enchantLevel = getEnchantLevel();

		if (getItemId() == 222334  || getItemId() == 22228) {
			if(getBless() % 128 == 0) {
				result += enchantLevel > 5 ? enchantLevel - 5 : 0;
			} else {
				if(enchantLevel == 9) {
					result += 1;
				}
			}
			if(enchantLevel >= 8) {
				result += (enchantLevel - 7) * 2;
			}
		} else if (getItemId() == 900025 || getItemId() == 900184 || getItemId() == 900198) {
			result += enchantLevel > 4 ? enchantLevel - 1 : 0;
			if (enchantLevel > 7)
				result += enchantLevel == 8 ? 1 : enchantLevel == 9 ? 3 : enchantLevel == 10 ? 5 : 0;
		} else if ((getItemId() == 22231 || getItemId() == 222339) && enchantLevel > 0) {
			if (getItemId() == 222339)
				result += 2;
			enchantLevel = enchantLevel > 9 ? 9 : enchantLevel;
			if(enchantLevel == 8 && getBless() % 128 != 0) {
				result += 2;
			}
			if(enchantLevel == 9 && getBless() % 128 == 0) {
				result += 3;
			}
			if (getBless() % 128 == 0)
				enchantLevel++;

			result += (enchantLevel == 8 ? 13
					: enchantLevel <= 0 ? 0
					: enchantLevel <= 6 ? enchantLevel + 2
					: enchantLevel < 8 ? 1 + (enchantLevel - 4) * 3 : 18);
		} else if (getItemId() == 900120 || getItemId() == 900222) {
			result += enchantLevel > 4 ? enchantLevel - 3 : 0;
		} else if (getItemId() >= 900081 && getItemId() <= 900083) {
			result += enchantLevel > 5 ? enchantLevel - 3 : 0;
			if (enchantLevel >= 7)
				result++;
			if (enchantLevel >= 8)
				result++;
		} else if (getItemId() == 900038 || getItemId() == 900054 || getItemId() == 900035 || getItemId() == 900072) {
			result += enchantLevel > 4 ? (enchantLevel - 4) * 4 : 0;
		} else if ((getItemId() >= 900081 && getItemId() <= 900083)) {
			result += enchantLevel > 3 ? enchantLevel - 3 : 0;
			if (getItemId() >= 900081 && getItemId() <= 900083)
				result *= 2;
		} else if (getItemId() >= 900124 && getItemId() <= 900126) {
			if (enchantLevel >= 5)
				result += 2 + (enchantLevel - 4) * 2;
		} else if (getItemId() == 900084 || getItemId() == 900196) {
			if (enchantLevel >= 6)
				result += 3 + (enchantLevel - 6) * 2;
			// TODO 從 MR強化5以上開始，每強化一次增加+3。
		/*} else if (getItemId() == 20017) {
			if (enchantLevel >= 5)
				result += 3 + (enchantLevel - 5) * 3;*/
		} else if (getItemId() == 900267) {
			result += 2 + (enchantLevel - 1) * 2;
		}
		if (getItem().getType2() == 2 && getItem().getType() == 9
				&& !(getItemId() >= 22224 && getItemId() <= 22228 || getItemId() >= 222290 && getItemId() <= 222291
				|| getItemId() >= 222330 && getItemId() <= 222336 || getItemId() == 900195)) {
			result += enchantLevel > 5 ? 1 + ((getEnchantLevel() - 6) * 2) : 0;
		}
		if (getItem().getType() == 8) {
			if (enchantLevel == 5)
				result += 1;
			else if (enchantLevel == 6)
				result += 3;
			else if (enchantLevel == 7)
				result += 5;
			else if (enchantLevel == 8)
				result += 7;
			else if (enchantLevel == 9)
				result += 10;
			else if (enchantLevel == 10)
				result += 12;
		}
		if (getItemId() == 900234 || getItemId() == 900235 || getItemId() == 900236 || getItemId() == 900237 || getItemId() == 900275
				|| getItemId() == 900276 || getItemId() == 900277) {
			if (enchantLevel >= 2 && enchantLevel <= 4)
				result += 2 + (enchantLevel - 2) * 2;
			else if (enchantLevel >= 5 && enchantLevel <= 8)
				result += 10 + (enchantLevel - 5) * 2;
		}
		if (getItemId() == 900278) {
			if (enchantLevel >= 2 && enchantLevel <= 4) {
				result += 2 + (enchantLevel - 2) * 2;
			} else if (enchantLevel >= 5 && enchantLevel <= 8)
				result += 11 + (enchantLevel - 5) * 2;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getMr(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Mr) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int addhp() {
		int hp = _item.get_addhp();

		return hp;
	}

	public int addmp() {
		int mp = _item.get_addmp();
		return mp;
	}

	public int addsp() {
		int sp = _item.get_addsp();
		return sp;
	}

	public void set_durability(int i) {
		if (i < 0) {
			i = 0;
		}

		if (i > 127) {
			i = 127;
		}
		_durability = i;
	}

	public int getWeight() {
		if (getItem().getWeight() == 0) {
			return 0;
		} else {
			return Math.max(getCount() * getItem().getWeight() / 1000, 1);
		}
	}

	public class LastStatus {
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
			count = getCount();
			itemId = getItemId();
			isEquipped = isEquipped();
			isIdentified = isIdentified();
			enchantLevel = getEnchantLevel();
			durability = get_durability();
			chargeCount = getChargeCount();
			remainingTime = getRemainingTime();
			lastUsed = getLastUsed();
			bless = getBless();
			attrenchantLevel = getAttrEnchantLevel();
			specialEnchant = getSpecialEnchant();
			endTime = getEndTime();
			bless_level = get_bless_level();
			item_level = get_item_level();
			town_name = getHotel_Town();
			_isSupportItem = isSupportItem();
			carving = get_Carving();
			doll_bonus_level = get_Doll_Bonus_Level();
			doll_bonus_value = get_Doll_Bonus_Value();
			_blessType = getBlessType();
			_blessTypeValue = getBlessTypeValue();
			smeltingValue = getSmeltingValue();
			smeltingitemid1 = getSmeltingItemId1();
			smeltingitemid2 = getSmeltingItemId2();
		}

		public void updateSpecialEnchant() {
			specialEnchant = getSpecialEnchant();
		}

		public void updateCount() {
			count = getCount();
		}

		public void updateItemId() {
			itemId = getItemId();
		}

		public void updateEquipped() {
			isEquipped = isEquipped();
		}

		public void updateIdentified() {
			isIdentified = isIdentified();
		}

		public void updateEnchantLevel() {
			enchantLevel = getEnchantLevel();
		}

		public void updateDuraility() {
			durability = get_durability();
		}

		public void updateChargeCount() {
			chargeCount = getChargeCount();
		}

		public void updateRemainingTime() {
			remainingTime = getRemainingTime();
		}

		public void updateLastUsed() {
			lastUsed = getLastUsed();
		}

		public void updateBless() {
			bless = getBless();
		}

		public void updateAttrEnchantLevel() {
			attrenchantLevel = getAttrEnchantLevel();
		}

		public void updateEndTime() {
			endTime = getEndTime();
		}

		public void update_bless_level() {
			bless_level = get_bless_level();
		}

		public void update_item_level() {
			item_level = get_item_level();
		}

		public void update_town_name() {
			town_name = getHotel_Town();
		}

		public void updateSupportItem() {
			_isSupportItem = isSupportItem();
		}

		public void update_Carving() {
			carving = get_Carving();
		}

		public void update_Doll_Bonus_Level() {
			doll_bonus_level = get_Doll_Bonus_Level();
		}

		public void update_Doll_Bonus_Value() {
			doll_bonus_value = get_Doll_Bonus_Value();
		}
		public void updateBlessType() {
			_blessType = getBlessType();
		}

		public void updateBlessTypeValue() {
			_blessTypeValue = getBlessTypeValue();
		}

		public void updateSmeltingValue(){
			smeltingValue = getSmeltingValue();
		}
		public void updateSmeltingItemId1(){
			smeltingitemid1 = getSmeltingItemId1();
		}
		public void updateSmeltingItemId2(){
			smeltingitemid2 = getSmeltingItemId2();
		}
		public void updateSmeltingKind1(){
			smeltingitemid1 = getSmeltingKind1();
		}
		public void updateSmeltingKind2(){
			smeltingitemid2 = getSmeltingKind2();
		}
		public void updateHalpasTime(){
			_halpas_time = getHalpas_Time();
		}
		public void updateCantUnseal() {
			_cant_unseal = get_Cantunseal();
		}
	}

	public LastStatus getLastStatus() {
		return _lastStatus;
	}

	public int getRecordingColumns() {
		int column = 0;

		if (getCount() != _lastStatus.count) {
			column += L1PcInventory.COL_COUNT;
		}
		if (getItemId() != _lastStatus.itemId) {
			column += L1PcInventory.COL_ITEMID;
		}
		if (isEquipped() != _lastStatus.isEquipped) {
			column += L1PcInventory.COL_EQUIPPED;
		}
		if (getEnchantLevel() != _lastStatus.enchantLevel) {
			column += L1PcInventory.COL_ENCHANTLVL;
		}
		if (get_durability() != _lastStatus.durability) {
			column += L1PcInventory.COL_DURABILITY;
		}
		if (getChargeCount() != _lastStatus.chargeCount) {
			column += L1PcInventory.COL_CHARGE_COUNT;
		}
		if (getLastUsed() != _lastStatus.lastUsed) {
			column += L1PcInventory.COL_DELAY_EFFECT;
		}
		if (isIdentified() != _lastStatus.isIdentified) {
			column += L1PcInventory.COL_IS_ID;
		}
		if (getRemainingTime() != _lastStatus.remainingTime) {
			column += L1PcInventory.COL_REMAINING_TIME;
		}
		if (getBless() != _lastStatus.bless) {
			column += L1PcInventory.COL_BLESS;
		}
		if (getAttrEnchantLevel() != _lastStatus.attrenchantLevel) {
			column += L1PcInventory.COL_ATTRENCHANTLVL;
		}
		if (getSpecialEnchant() != _lastStatus.specialEnchant) {
			column += L1PcInventory.COL_ATTRENCHANTLVL;
		}
		if (get_bless_level() != _lastStatus.bless_level) {
			column += L1PcInventory.COL_BLESS;
		}
		if (getEndTime() != _lastStatus.endTime) {
			column += L1PcInventory.COL_REMAINING_TIME;
		}
		if (get_Carving() != _lastStatus.carving) {
			column += L1PcInventory.COL_CARVING;
		}
		if (get_Doll_Bonus_Level() != _lastStatus.doll_bonus_level) {
			column += L1PcInventory.COL_DOLL_LEVEL;
		}
		if (get_Doll_Bonus_Value() != _lastStatus.doll_bonus_value) {
			column += L1PcInventory.COL_DOLL_VALUE;
		}
		if (getSmeltingValue() != _lastStatus.smeltingValue){
			column += L1PcInventory.COL_SMELTING;
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
			name.append("(각인)");
		}

		if (getItem().getType2() == 0 && getItem().getType() == 2) {
			if (isNowLighting()) {
				name.append(" ($10)");
			}
			if (itemId == 40001 || itemId == 40002 || itemId == 7005) {
				if (getRemainingTime() <= 0) {
					name.append(" ($11)");
				}
			}
		}

		if (itemId == MJINNHelper.INN_KEYID) {
			name.append(" (" + getHotel_Town() + ")");
		}

		/** 特殊強化系統 **/
		if (get_item_level() != 0) {
			switch (get_item_level()) {
				case 1:
					name.append(" [第1階段]");
					break;
				case 2:
					name.append(" [第2階段]");
					break;
				case 3:
					name.append(" [第3階段]");
					break;
				case 4:
					name.append(" [第4階段]");
					break;
				case 5:
					name.append(" [第5階段]");
					break;
				default:
					break;
			}
		}
		/** 특수 인챈트 시스템 **/

		if (isEquipped()) {
			if (itemType2 == 1) {
				name.append(" ($9)");
			} else if (itemType2 == 2) {
				name.append(" ($117)");
			} else if (itemType2 == 0 && getItem().getType() == 11) { // petitem
				name.append(" ($117)");
			}
		}

		/*if (itemType2 == 0 && (getItem().getType() == 0 || getItem().getType() == 15)) {
			if (_cha != null) {
				if (_cha.getInventory().getArrowItemId() == getItemId()) {
					name.append(" ($117)");
				}
				if (_cha.getInventory().getStingItemId() == getItemId()) {
					name.append(" ($117)");
				}
			}
		}*/
		return name.toString();
	}

	public String getViewName() {
		return getNumberedViewName(_count);
	}

	public String getLogName() {
		return getNumberedName(_count);
	}

	/** 속성 인챈트 **/
	public String getNumberedName(int count) {
		StringBuilder name = new StringBuilder();

		if (isIdentified()) {
			if (getItem().getType2() == 1 || getItem().getType2() == 2) {
				switch (getAttrEnchantLevel()) {
					case 1:
						name.append("$6115");
						break; // 화령1단
					case 2:
						name.append("$6116");
						break; // 화령2단
					case 3:
						name.append("$6117");
						break; // 화령3단 (불의속성)
					case 4:
						name.append("$14361");
						break; // 화령4단
					case 5:
						name.append("$14365");
						break; // 화령5단
					case 6:
						name.append("$6118");
						break; // 수령1단
					case 7:
						name.append("$6119");
						break; // 수령2단
					case 8:
						name.append("$6120");
						break; // 수령3단 (물의속성)
					case 9:
						name.append("$14362");
						break; // 수령4단
					case 10:
						name.append("$14366");
						break; // 수령5단
					case 11:
						name.append("$6121");
						break; // 풍령1단
					case 12:
						name.append("$6122");
						break; // 풍령2단
					case 13:
						name.append("$6123");
						break; // 풍령3단 (바람의속성)
					case 14:
						name.append("$14363");
						break; // 풍령4단
					case 15:
						name.append("$14367");
						break; // 풍령5단
					case 16:
						name.append("$6124");
						break; // 지령1단
					case 17:
						name.append("$6125");
						break; // 지령2단
					case 18:
						name.append("$6126");
						break; // 지령3단 (땅의속성)
					case 19:
						name.append("$14364");
						break; // 지령4단
					case 20:
						name.append("$14368");
						break; // 지령5단
					default:
						break;
				}
				if (getEnchantLevel() >= 0) {
					name.append("+" + getEnchantLevel() + " ");
				} else if (getEnchantLevel() < 0) {
					name.append(String.valueOf(getEnchantLevel()) + " ");
				}
			}
		}
		// TODO 確認未確認狀態名稱後更改名稱
		String real_name = getItem().getNameView();
		if (getItem().getItemId() == MJCTLoadManager.CTSYSTEM_LOAD_ID
				&& MJCTSystemLoader.getInstance().get(getId()) != null) {
			MJCTObject obj = MJCTSystemLoader.getInstance().get(getId());
			name.append("[").append(obj.name).append("]封印畫框");
		} else {
			if (MJString.isNullOrEmpty(real_name) || isIdentified()) {
				real_name = _item.getNameId();
			}
			name.append(real_name);
		}

		if (isSpecialEnchantable()) {
			for (int i = 1; i <= 3; ++i) {
				if (getSpecialEnchant(i) == 0) {
					break;
				}
				switch (getSpecialEnchant(i)) {
					case CHAOS_SPIRIT:
						name.append("[混沌] ");
						break;
					case CORRUPT_SPIRIT:
						name.append("[墮落] ");
						break;
					case BALLACAS_SPIRIT:
						name.append("[巴拉卡斯] ");
						break;
					case ANTARAS_SPIRIT:
						name.append("[安塔拉斯] ");
						break;
					case LINDBIOR_SPIRIT:
						name.append("[林德拜爾] ");
						break;
					case PAPURION_SPIRIT:
						name.append("[法里昂] ");
						break;
					case DEATHKNIGHT_SPIRIT:
						name.append("[死亡騎士] ");
						break;
					case BAPPOMAT_SPIRIT:
						name.append("[巴風特] ");
						break;
					case BALLOG_SPIRIT:
						name.append("[巴洛克] ");
						break;
					case ARES_SPIRIT:
						name.append("[阿瑞斯] ");
						break;
				}
			}
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

		/*
		 * if (isIdentified()) { if (getItem().getMaxChargeCount() > 0) {
		 * name.append(" (" + getChargeCount() + ")"); } if (getItem().getItemId() ==
		 * 20383) { name.append(" (" + getChargeCount() + ")"); } if
		 * (getItem().getMaxUseTime() > 0 && getItem().getType2() != 0) {
		 * name.append(" [" + getRemainingTime() + "]"); } } if (count > 1) {
		 * name.append(" (" + count + ")"); }
		 */
		return name.toString();
	}

		/**
		 * 從物品狀態生成用於服務器數據包的字節數組並返回。
		 * 1: 打擊值 , 2: 強化等級, 3: 損傷度, 4: 雙手劍
		 * 5: 攻擊成功, 6: 額外打擊, 7: 王子/公主 ,
		 * 8: Str, 9: Dex, 10: Con, 11: Wiz, 12: Int, 13: Cha,
		 * 14: Hp,Mp 15: Mr, 16: 法力吸收, 17: 咒術力, 18: 加速效果, 19: Ac, 20: 運氣,
		 * 21:營養, 22: 亮度, 23: 材質, 24: 弓命中率, 25: 類型[writeH], 26: 等級[writeH],
		 * 27: 火屬性, 28: 水屬性, 29: 風屬性, 30: 地屬性,
		 * 31: 最大Hp, 32: 最大Mp, 33: 恐懼抗性, 34: 生命吸收, 35: 弓打擊值,
		 * 36: branch用dummy, 37: 體力恢復率, 38: 法力恢復率, 40: 魔法命中, 42: 等級,
		 * 47: 近距離傷害, 48: 近距離命中, 50: 魔法暴擊, 51: DG, 55: 加速效果, 56: 額外防禦, 59: PVP 額外傷害, 60: PVP 傷害減少,
		 * 61. 以後自動刪除, 63: 傷害減少, 68: 攜帶重量增加率, 71: 死亡騎士套裝標記, 72: 支援物品使用期限, 75: 性向 正義,
		 * 79: 使用等級, 89: 機率 魔法回避(ME), 93: ER, 92: 額外傷害機率, 94: 貫穿效果, 96: 回復惡化防禦, 97: 無視傷害減少,
		 * 135: PVP魔法傷害減少, 138: PVP傷害減少無視, 139: PVP魔法傷害減少無視,
		 * 149: Hp增加%, 150: MP增加%, 151: 藥水恢復量%, 152: 藥水恢復量, 153: 龍傷害減少%, 154: 祝福EXP%, 155: 全屬性抗性,
		 * 156: ???, 157: 近距離暴擊%, 158: 遠距離暴擊%, 159: 魔法暴擊%,
		 * 160: 三段加速, 161: 162: 163: 164: 165: MR, 166: 魔法傷害減少%, 167: 血盟每日可購買數量,
		 * 168: 血盟每周可購買數量, 169: 170: 171: 172: 173: 174:
		 * 175: 無視免疫, 176: 昏迷持續時間增加, 177: 颶風發動概率增加,
		 * 180: 艾恩哈薩德的祝福狀態%, 192: 傷害減少%,
		 * 194: 魔法傷害增加, 196: 先鋒重用時間減少, 197: 魔法暴擊傷害增加,
		 * 200:
		 * 220: 無法返回持續時間
		 *`, os.writeH(23929) 死亡騎士套裝標記？
		 *
		 * @param armor
		 */

	public static int presentationCode = 0;

	public byte[] getStatusBytes() {
		return getStatusBytes(null);
	}

	@SuppressWarnings("deprecation")
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
			if (sInfo != null){
				try{
					os.write(SmeltingScrollInfo.getItemView(sInfo));
				} catch (IOException e){
					e.printStackTrace();
				}
			}

//			tempSkillSystemInfo tInfo = tempSkillSystemLoader.getInstance().getTempSkillSystemInfo(itemId);
//			if (tInfo != null) {
//				try {
//					os.write(tempSkillSystemInfo.getItemView(pc, tInfo));
//				}catch (IOException e) {
//					e.printStackTrace();
//				}
//			}

			// find for etc item
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



			/*if (getEndTime() != null) {
				System.out.println("dummy id2 : "+ getEndTime());
			}*/

			BuyLimitSystem.L1BuyLimitItems limit_item = BuyLimitSystem.getInstance().getLimitBuyType(itemId);
			if (pc != null) {
				if (limit_item != null) {
					int limit_type = limit_item.isLimitType();
					int current_time = (int) (System.currentTimeMillis() / 1000);
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
						os.writeC(0x93);
						if (limit_account != null) {
							if (current_time < start_time.getTime() / 1000 || current_time > limit_time.getTime() / 1000) {
								if (limit_account.getCount() < limit_item.getBuyCount()) {
									limit_account.setCount(limit_item.getBuyCount());
									BuyLimitSystemAccountTable.getInstance().updateLimitItem(pc.getAccountName(), this, limit_account.getCount(), false);
								}
							}
							os.writeD(limit_account.getCount());
						} else {
							os.writeD(limit_item.getBuyCount());
						}
						os.writeD(start_time.getTime() / 1000);
						os.writeD(0);
						os.writeD(limit_time.getTime() / 1000);
						os.writeD(0);
					} else if (limit_type == 2) {
						BuyLimitSystemCharacter limit_char = BuyLimitSystemCharacterTable.getInstance().getLimitTable(pc, itemId);
						os.writeC(0x93);
						if (limit_char != null) {
							if (current_time < start_time.getTime() / 1000 || current_time > limit_time.getTime() / 1000) {
								if (limit_char.getCount() < limit_item.getBuyCount()) {
									limit_char.setCount(limit_item.getBuyCount());
									BuyLimitSystemCharacterTable.getInstance().updateLimitItem(pc, this, limit_char.getCount(), false);
								}
							}
							os.writeD(limit_char.getCount());
						} else {
							os.writeD(limit_item.getBuyCount());
						}
						os.writeD(start_time.getTime() / 1000);
						os.writeD(0);
						os.writeD(limit_time.getTime() / 1000);
						os.writeD(0);
					} else if (limit_type == 3) {
						BuyLimitSystemAccount limit_1day_account = BuyLimitSystemAccountTable.getInstance().getLimitTable(pc.getAccountName(), itemId);
						os.writeC(0x85);
						if (limit_1day_account != null) {
							if (limit_1day_account.getBuyTime() != null) {
								Timestamp limit_1day = new Timestamp(limit_1day_account.getBuyTime().getTime() + (86400 * 1) * 1000);
								if (current_time > limit_1day.getTime() / 1000) {
									if (limit_1day_account.getCount() < limit_item.getBuyCount()) {
										limit_1day_account.setCount(limit_item.getBuyCount());
										limit_1day_account.setBuyTime(null);
										BuyLimitSystemAccountTable.getInstance().updateLimitItem(pc.getAccountName(), this, limit_1day_account.getCount(), false);
									}
								}
							}
							os.writeD(limit_1day_account.getCount());
						} else {
							os.writeD(limit_item.getBuyCount());
						}
					} else if (limit_type == 4) {
						BuyLimitSystemCharacter limit_1day_char = BuyLimitSystemCharacterTable.getInstance().getLimitTable(pc, itemId);
						//os.writeC(0x85);
						if (limit_1day_char != null) {
							if (limit_1day_char.getBuyTime() != null) {
								Timestamp limit_1day = new Timestamp(limit_1day_char.getBuyTime().getTime() + (86400 * 1) * 1000);
								if (current_time > limit_1day.getTime() / 1000) {
									if (limit_1day_char.getCount() < limit_item.getBuyCount()) {
										limit_1day_char.setCount(limit_item.getBuyCount());
										limit_1day_char.setBuyTime(null);
										BuyLimitSystemCharacterTable.getInstance().updateLimitItem(pc, this, limit_1day_char.getCount(), false);
									}
								}
							}
							if (limit_1day_char.getCount() > 0)
								os.writeStringS(String.format("\fI可購買數量(1日): \\aA%d個", limit_1day_char.getCount()));
							else
								os.writeStringS(String.format("\f3可購買數量(1日): \\aA%d個", limit_1day_char.getCount()));
								//os.writeD(limit_1day_char.getCount());
						} else {
							os.writeStringS(String.format("\fI可購買數量(1日): \\aA%d個", limit_item.getBuyCount()));
							//os.writeD(limit_item.getBuyCount());
						}
					} else if (limit_type == 5) {
						BuyLimitSystemAccount limit_7day_account = BuyLimitSystemAccountTable.getInstance().getLimitTable(pc.getAccountName(), itemId);
						os.writeC(0x86);
						if (limit_7day_account != null) {
							if (limit_7day_account.getBuyTime() != null) {
								Timestamp limit_7day = new Timestamp(limit_7day_account.getBuyTime().getTime() + (86400 * 7) * 1000);
								if (current_time > limit_7day.getTime() / 1000) {
									if (limit_7day_account.getCount() < limit_item.getBuyCount()) {
										limit_7day_account.setCount(limit_item.getBuyCount());
										limit_7day_account.setBuyTime(null);
										BuyLimitSystemAccountTable.getInstance().updateLimitItem(pc.getAccountName(), this, limit_7day_account.getCount(), false);
									}
								}
							}
							os.writeD(limit_7day_account.getCount());
						} else {
							os.writeD(limit_item.getBuyCount());
						}
					} else if (limit_type == 6) {
						BuyLimitSystemCharacter limit_7day_char = BuyLimitSystemCharacterTable.getInstance().getLimitTable(pc, itemId);
						//os.writeC(0x86);
						if (limit_7day_char != null) {
							if (limit_7day_char.getBuyTime() != null) {
								Timestamp limit_7day = new Timestamp(limit_7day_char.getBuyTime().getTime() + (86400 * 7) * 1000);
								if (current_time > limit_7day.getTime() / 1000) {
									if (limit_7day_char.getCount() < limit_item.getBuyCount()) {
										limit_7day_char.setCount(limit_item.getBuyCount());
										limit_7day_char.setBuyTime(null);
										BuyLimitSystemCharacterTable.getInstance().updateLimitItem(pc, this, limit_7day_char.getCount(), false);
									}
								}
							}
							if (limit_7day_char.getCount() > 0)
								os.writeStringS(String.format("\fI可購買數量(1週): \\aA%d個", limit_7day_char.getCount()));
							else
								os.writeStringS(String.format("\f3可購買數量(1週): \\aA%d個", limit_7day_char.getCount()));
								//os.writeD(limit_7day_char.getCount());
						} else {
							os.writeStringS(String.format("\fI可購買數量(1週): \\aA%d個", limit_item.getBuyCount()));
							//os.writeD(limit_item.getBuyCount());
						}
				}
			}

			if (itemType2 == 0) { // etcitem 표기 테스트
				if (getItem().getItemId() == 30001111) {
					os.writeC(Config.ItemOption.writeC);
					if (Config.ItemOption.writeDBoolean){
						os.writeD(1);
					} else if (Config.ItemOption.writeHBoolean){
						os.writeH(1);
					} else if (Config.ItemOption.writeCBoolean){
						os.writeC(1);
					}
				}
				switch (getItem().getType()) {
					case 2: // light
						os.writeC(22);
						os.writeH(getItem().getLightRange());
						os.writeC(getItem().getMaterial());
						os.writeD(getWeight());
						break;
					case 7: // food
						os.writeC(21);
						os.writeH(getItem().getFoodVolume());
						os.writeC(getItem().getMaterial());
						os.writeD(getWeight());
						break;
					case 0: // arrow
					case 15: // sting
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
					case 10:// 스킬(마법) 표기자동으로하기
						int use_class = 0;
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
						L1Skills skill = SkillsTable.getInstance().findByItemName(getItem().getName());
						if (skill != null) {
							int skillLawful = skill.getLawful();
							int skillLevel = skill.getSkillLevel();
							if (skillLevel <= 10) {
								os.writeC(77);
								os.writeC(skillLevel - 1);
							} else if ((skillLevel >= 17) && (skillLevel <= 22)) {
								int level = IntRange.ensure(getItem().getMinLevel() / 10 - 1, 0, 4);
								os.writeC(77);
								os.writeC(level);
							} else {
								os.writeC(79);
								os.writeC(getItem().getMinLevel());
							}

							os.writeC(75);
							os.writeC(skillLawful == 0 ? 0 : skillLawful < 0 ? -1 : 1);
						}

						if (getItem().getMinLevel() != 0) {
							os.writeC(111);
							os.writeC(getItem().getMinLevel());
							os.writeH(getItem().getMaxLevel() == 0 ? 99 : getItem().getMaxLevel());
						}

						os.writeC(23);
						os.writeC(getItem().getMaterial());
						os.writeD(getWeight());
						break;
					case 8: // res (주문서류)
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

						if(getMagicDmgModifier() != 0) {
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
							// os.writeOption("PVP 마법 대미지 감소", getPVPMdmgReduction());
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


						if (getItem().getAttackDelayRate() != 0) {
							os.writeOptionA("攻擊速度", (int)getItem().getAttackDelayRate());
						}

						if (getItem().getMoveDelayRate() != 0) {
							os.writeOptionA("移動速度", (int)getItem().getMoveDelayRate());
						}

						os.writeC(23);
						os.writeC(getItem().getMaterial());
						os.writeD(getWeight());
						break;
					case 4100696: //回憶的燈籠
						os.writeC(39);
						os.writeS("\fI使用等級：\\aA 80");
						os.writeC(39);
						os.writeS("\fI重置角色的特殊屬性");
						os.writeC(39);
						os.writeS("\fI初始化。");
						break;
					default:
						if (itemId > 10000000 && itemgfx == 6438) {
							// os.writeString("단폴", 1.95);
							// os.writeString("묶음", 1.95);
							os.writeString("配當", 1.95);
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
							class_bit |= getItem().isUseWarrior() ? 128 : 0;
							class_bit |= getItem().isUseFencer() ? 256 : 0;
							class_bit |= getItem().isUseLancer() ? 512 : 0;
							if (class_bit > 0) {
								os.writeC(7);
								os.writeH(class_bit);
							}
						}

						/** MJCTSystem **/
						if (getItem().getItemId() == MJCTLoadManager.CTSYSTEM_LOAD_ID) {
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

									Charlevel = (rs.getInt("level"));
									Elixir = (rs.getInt("ElixirStatus"));
									Class = (rs.getInt("Class"));
									Hp = (rs.getInt("MaxHp"));
									Mp = (rs.getInt("MaxMp"));

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
										if (Class == 19296 || Class == 19299)
											bit = 512;

										os.writeAddMaxHP(Hp);
										os.writeMaxMP(Mp);
										os.writeC(39);
										os.writeS("\fI藥水攝取量: \\aA" + Elixir + "");
										os.writeClass(bit);
										os.writeLevel(Charlevel);
									}

								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									SQLUtil.close(rs);
									SQLUtil.close(pstm);
									SQLUtil.close(con);
								}
							}
						}

						// 1997년 1월 1일 17시
						// ((삭제된시간 - 지정된(1997년시간))/ 1000) * 6
						if (getEndTime() != null) {
							if (!isSupportItem() && getItem().isEndedTimeMessage() && getEndTime() != null) {
								/*
								 * long old = 852105600000L; // 1997년 1월 1일 17시 int sec = (int)
								 * ((getEndTime().getTime() - old) / 1000); os.writeC(61); os.writeD(sec * 6);
								 */
								int remainSeconds = (int) ((getEndTime().getTime() - 1483196400065L) / 1000);
								os.writeC(61);
								os.writeD(remainSeconds * 6);
							} else if (isSupportItem() && !getItem().isEndedTimeMessage() && getEndTime() != null) {
								os.writeC(72); // [지원아이템] 사용기한
								os.writeD((int) (getEndTime().getTime() / 1000));
							} else {
								os.writeC(112);
								os.writeD(getEndTime().getTime() / 1000);
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
						/**
						 * 창고 불가 일경우 %s 레벨부터 창고가능 표기
						 */
						os.writeC(148);
						os.writeC(getItem().getWareHouseLimitLevel());
					}
				} else {
					os.writeD(getItem().getWareHouseLimitType().toInt() != 7 ? 6
							: getItem().getWareHouseLimitType().toInt());
				}
				ShopBuyLimit sli = ShopBuyLimitInfo.getInstance().getShopBuyLimit(getItemId());
				if (sli != null) {
					ShopBuyLimit char_sli_by_objid = null;
					ShopBuyLimit char_sli_by_account = null;
					if (_cha != null) {
						char_sli_by_objid = ShopBuyLimitInfo.getInstance().findShopBuyLimitByObjid(_cha.getId(),
								getItem().getItemId());
						char_sli_by_account = ShopBuyLimitInfo.getInstance()
								.findShopBuyLimitByAccount(_cha.getAccount().getName(), getItem().getItemId());
					}
					//System.out.println(getItem().getName() + " -> char_sli_by_objid : " + char_sli_by_objid + " / char_sli_by_account : " + char_sli_by_account);
					if (_cha != null && (char_sli_by_objid != null || char_sli_by_account != null)){
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
							if (char_sli_by_objid != null) {
//								os.writeC(133);
//								os.writeD(char_sli_by_objid.get_count());

								// 全部購買時的顯示 (PC用戶)
								if(char_sli_by_objid.get_count() == 0 ) {
									os.writeC(39);
									os.writeS("\f3每角色購買(1日): " + char_sli_by_objid.get_count() + "個");
								} else {
									os.writeC(39);
									os.writeS("\fI每角色購買(1日):\\aA " + char_sli_by_objid.get_count() + "個");
								}
							}
						} else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT) {
							if (char_sli_by_account != null) {
								os.writeC(133);
								os.writeD(char_sli_by_account.get_count());
							}
						}
					} else {
						if (sli.get_type() == eShopBuyLimitType.CHARACTER_WEEK_LIMIT) {
							os.writeC(134);
							os.writeD(sli.get_count());
						} else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) {
							os.writeC(134);
							os.writeD(sli.get_count());
						} else if (sli.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
//							os.writeC(133);
//							os.writeD(sli.get_count());
							//否則
							os.writeC(39);
							os.writeS("\fI每角色購買(1日):\\aA " + sli.get_count() + "個");
						} else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT) {
							os.writeC(133);
							os.writeD(sli.get_count());
						}
					}
				}
			} else if (itemType2 == 1 || itemType2 == 2) { // weapon | armor
				// find for weapon
				if (itemType2 == 1) { // weapon 무기 타격치
					os.writeC(1);
					os.writeC(getItem().getDmgSmall());
					os.writeC(getItem().getDmgLarge());
					os.writeC(getItem().getMaterial());
					os.writeD(getWeight());

					os.writeC(2);
					os.writeC(enchant);

					os.writeC(107);//대미지 표기 (1) (근거리 대미지)
					os.writeC(getEnchantDmgRate()); // 큰몹추가데미지
					os.writeC(getEnchantDmgRate()); // 작은몹추가데미지

					if (getItem().isTwohandedWeapon()) { // 양손무기
						os.writeC(4);
					}
					// find for armor
				} else if (itemType2 == 2) { // armor AC
					os.writeC(19);
					int ac = getAc();
					if (ac < 0) {
						ac = ac - ac - ac;
					} else if (ac > 0) {
						ac = ac - ac - ac;
					}
					os.writeC(ac); // H
					os.writeC(getItem().getMaterial());
					os.writeH(-1);
					os.writeD(getWeight());
					/*
					 * if(getItem().getType() == 10) { os.writeC(0); }
					 */
					os.writeC(2);
					os.writeC(getAcByEnchantLevel());

				}

				if (get_durability() != 0) { // 손상도
					os.writeC(3);
					os.writeC(get_durability());
				}

				// TODO 安全強化顯示
				if (getEndTime() != null) {
					os.writeC(39);
					os.writeS("\fI強化: \\aA不可");
				} else if (getItem().get_safeenchant() > -1) {
					os.writeC(169);
					os.writeC(getItem().get_safeenchant());
				} else {
					os.writeC(169);
					os.writeC(getItem().get_safeenchant());
				}

				if (getaddAc() != 0) {
					if (get_bless_level() != 0) {
						os.writeC(39);
						os.writeS("\fI祝福選項: \\aAAC +" + getaddAc());
					} else {
						os.writeC(56);
						os.writeC(getaddAc());
					}
				}

				if (getaddHp() != 0) {
					if (get_bless_level() != 0) {
						os.writeC(39);
						os.writeS("\fI祝福選項: \\aAHP +" + getaddHp());
					}
				}
				if (get_bless_level() != 0) {
					int type = getItem().getType();
					if (getItem().getType2() == 1) {
						if (type == 7 || type == 16 || type == 17) {
							os.writeC(39);
							os.writeS("\fI祝福選項: \\aASP +" + this.get_bless_level());
						} else {
							os.writeC(39);
							os.writeS("\fI祝福選項: \\aA傷害 +" + this.get_bless_level());
						}
					}
				}

				/** 特殊強化系統 **/
				if (getItem().getType2() != 0 && get_item_level() != 0) {
					switch (get_item_level()) {
						case 1:
							os.writeC(73);
							os.writeS("\fI特殊選項: \\aA1階段魔法");
							break;
						case 2:
							os.writeC(73);
							os.writeS("\fI特殊選項: \\aA2階段魔法");
							break;
						case 3:
							os.writeC(73);
							os.writeS("\fI特殊選項: \\aA3階段魔法");
							break;
						case 4:
							os.writeC(73);
							os.writeS("\fI特殊選項: \\aA4階段魔法");
							break;
						default:
							break;
					}
				}

				/** 클래스 착용 부분 **/
				int bit = 0;
				bit |= getItem().isUseRoyal() ? 1 : 0;
				bit |= getItem().isUseKnight() ? 2 : 0;
				bit |= getItem().isUseElf() ? 4 : 0;
				bit |= getItem().isUseMage() ? 8 : 0;
				bit |= getItem().isUseDarkelf() ? 16 : 0;
				bit |= getItem().isUseDragonKnight() ? 32 : 0;
				bit |= getItem().isUseBlackwizard() ? 64 : 0;
				bit |= getItem().isUseWarrior() ? 128 : 0;
				bit |= getItem().isUseFencer() ? 256 : 0;  // 此處如果未來添加劍士類別，則取消註釋。
				bit |= getItem().isUseLancer() ? 512 : 0;
				os.writeC(7);
				os.writeH(bit);

				if (getDmgModifier() != 0) {
					os.writeC(47);
					os.writeC(getDmgModifier());
				} else {
					if (isUndeadDmg()) {
						if (getItem().getType1() == 20 || getItem().getType1() == 62) {
							if (getBowDmgModifier() == 0) {
								os.writeC(35);
								os.writeC(0);
							}
						} else {
							os.writeC(47);
							os.writeC(0);
						}
					} else {
						if (getEnchantLevel() != 0) {
							if (getItem().getType1() == 20 || getItem().getType1() == 62) {
								if (getBowDmgModifier() == 0) {
									os.writeC(35);
									os.writeC(0);
								}
							} else {
//								os.writeC(47);
//								os.writeC(0);
							}
						}
					}
				}



				if (getHitModifier() != 0) {
					if (getItem().getType2() == 1) {
						os.writeC(48);
					} else {
						os.writeC(5);
					}
					os.writeC(getHitModifier());
				} else {
					if (getEnchantLevel() >= 0 && getHitModifier() == 0) {
						if (getItem().getType1() != 20 && getItem().getType1() != 62) {
							os.writeC(48);
							os.writeC(0);
						} else {
							os.writeC(24);
							os.writeC(0);
						}
					}
				}

				os.writeC(130);
				if (getItem().getWareHouseLimitType().toInt() == 2) {
					os.writeD(getItem().getWareHouseLimitType().toInt());
					if (getItem().getWareHouseLimitLevel() != 0) {
						/**
						 * 창고 불가 일경우 %s 레벨부터 창고가능 표기
						 */
						os.writeC(148);
						os.writeC(getItem().getWareHouseLimitLevel());
					}
				} else {
					os.writeD(getItem().getWareHouseLimitType().toInt() != 7 ? 6
							: getItem().getWareHouseLimitType().toInt());
				}
				ShopBuyLimit sli = ShopBuyLimitInfo.getInstance().getShopBuyLimit(getItemId());
				if (sli != null) {
					ShopBuyLimit char_sli_by_objid = null;
					ShopBuyLimit char_sli_by_account = null;
					if (_cha != null) {
						char_sli_by_objid = ShopBuyLimitInfo.getInstance().findShopBuyLimitByObjid(_cha.getId(),
								getItem().getItemId());
						char_sli_by_account = ShopBuyLimitInfo.getInstance()
								.findShopBuyLimitByAccount(_cha.getAccount().getName(), getItem().getItemId());
					}

					if (_cha != null && (char_sli_by_objid != null || char_sli_by_account != null)){
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
							if (char_sli_by_objid != null) {
//								os.writeC(133);
//								os.writeD(char_sli_by_objid.get_count());

								if(char_sli_by_objid.get_count() == 0 ) {
									os.writeC(39);
									os.writeS("\f3每角色購買(1日): " + char_sli_by_objid.get_count() + "個");
								} else {
									os.writeC(39);
									os.writeS("\fI每角色購買(1日):\\aA " + char_sli_by_objid.get_count() + "個");
								}
							}
						} else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT) {
							if (char_sli_by_account != null) {
								os.writeC(133);
								os.writeD(char_sli_by_account.get_count());
							}
						}
					} else {
						if (sli.get_type() == eShopBuyLimitType.CHARACTER_WEEK_LIMIT) {
							os.writeC(134);
							os.writeD(sli.get_count());
						} else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) {
							os.writeC(134);
							os.writeD(sli.get_count());
						} else if (sli.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
//							os.writeC(133);
//							os.writeD(sli.get_count());
							// 否則
							os.writeC(39);
							os.writeS("\fI每角色購買(1日):\\aA " + sli.get_count() + "個");
						} else if (sli.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT) {
							os.writeC(133);
							os.writeD(sli.get_count());
						}
					}
				}

				if (getItem().getMinLevel() != 0) {
					os.writeC(111);
					os.writeC(getItem().getMinLevel());
					os.writeH(getItem().getMaxLevel() == 0 ? 99 : getItem().getMaxLevel());
				}

				if (isCanbeDmg()) {
					os.writeC(131);
					os.writeD(1); // 비손상
				}

				if (isUndeadDmg()) {
					os.writeD(114);
					os.writeC(1); // 언데드
				}






				/**
				 * 성별 구분 1: 남자 (표기됨) 2: 여자 (표기됨) 3: 전체 (표기안됨)
				 */
				os.writeC(132);
				os.writeD(3);

				// STR~CHA
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
				/** 칠흑의 망토 인챈별 카리 증가 16.09.28 **/
				if (getCha() != 0) {
					os.writeC(13);
					os.writeC(getCha());
				}

				if (getHp() != 0) {
					os.writeC(14);
					os.writeH(getHp());
				}

				if (getMr() != 0) { // MR
					os.writeC(15);
					os.writeH(getMr());
				}

				// 마나 흡수
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
				} else {
					if (getEnchantLevel() >= 0 && getHitModifier() == 0) {
						if (getItem().getType1() == 20 && getItem().getType1() == 62) {
							os.writeC(24);
							os.writeC(0);
						}
					}
				}

				// 불의 속성
				if (getDefenseFire() != 0) {
					os.writeC(27);
					os.writeC(getDefenseFire());
				}
				// 물의 속성
				if (getDefenseWater() != 0) {
					os.writeC(28);
					os.writeC(getDefenseWater());
				}
				// 바람 속성
				if (getDefenseWind() != 0) {
					os.writeC(29);
					os.writeC(getDefenseWind());
				}
				// 땅의 속성
				if (getDefenseEarth() != 0) {
					os.writeC(30);
					os.writeC(getDefenseEarth());
				}
				// 전체속성
				if (getDefenseAll() != 0) {
					os.writeC(155);
					os.writeC(getDefenseAll());
				}

				if (getMp() != 0) {
					os.writeC(32);
					os.writeH(getMp());
				}

				// 피 흡수
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

				// 피틱 표시
				if (getHpr() != 0) {
					os.writeC(37);
					os.writeC(getHpr());
				}

				// 엠틱 표시
				if (getMpr() != 0) {
					os.writeC(38);
					os.writeC(getMpr());
				}

				/** 마법 적중 **/
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
					// os.writeOption("PVP 마법 대미지 감소", getPVPMdmgReduction());
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
					if (getItem().getItemId() == 7000239) { //아인하사드의 섬광
						os.writeC(176); //스턴지속시간 증가
						os.writeC(1);
					}

					if (getItem().getItemId() == 7000240) { //그랑카인의 심판
						os.writeC(176); //스턴지속시간 증가
						os.writeC(1);
					}
					if (getItem().getItemId() == 203065) { //그랑카인의 공포
						os.writeC(176); //스턴지속시간 증가
						os.writeC(1);
					}
					if (getItem().getItemId() == 203041) { //에바의 서약
						os.writeC(176); //스턴지속시간 증가
						os.writeC(1);
						os.writeC(16);
						os.writeC(0);
//						os.writeC(197); //마법치명타 대미지
//						os.writeH(0);
					}
					if (getItem().getItemId() == 7000262) {//실렌의 결의
						os.writeC(200); //반격,회피 무기 타격
						os.writeC(0);
						os.writeC(34);
						os.writeC(0);
					}
					if (getItem().getItemId() == 203042) { //마프르의 고뇌
						os.writeC(176); //스턴지속시간 증가
						os.writeC(1);
//						os.writeC(196); //뱅가드 재사용시간 감소
//						os.writeD(0);
					}
					if (getItem().getItemId() == 7000264) { //단테스의 시련
						os.writeC(220); //귀환불가 지속시간 증가
						os.writeH(1);
					}
					if (getItem().getItemId() == 7000265) { //테이아의 혼돈
						os.writeC(220); //귀환불가 지속시간 증가
						os.writeH(1);
					}
					if (getItem().getItemId() == 7000267) { //아우라키아의 초월
						os.writeC(176); //스턴지속시간 증가
						os.writeC(1);
					}
				}

				if (getItem().getAttackDelayRate() != 0) {
					os.writeOptionA("攻擊速度", (int)getItem().getAttackDelayRate());
				}

				if (getItem().getMoveDelayRate() != 0) {
					os.writeOptionA("移動速度", (int)getItem().getMoveDelayRate());
				}

				// 1997年1月1日17時
				// ((刪除的時間 - 指定的(1997年時間))/ 1000) * 6
				if (getEndTime() != null) {
					if (!isSupportItem() && getItem().isEndedTimeMessage() && getEndTime() != null) {
						// 1483196400065
						/*
						 * long old = 852105600000L; // 1997年1月1日17時 int sec = (int)
						 * ((getEndTime().getTime() - old) / 1000); os.writeC(61); os.writeD(sec * 6);
						 */

						int remainSeconds = (int) ((getEndTime().getTime() - 1483196400065L) / 1000);
						os.writeC(61);
						os.writeD(remainSeconds * 6);
					} else if (isSupportItem() && !getItem().isEndedTimeMessage() && getEndTime() != null) {
						os.writeC(72); // [지원아이템] 사용기한
						os.writeD((int) (getEndTime().getTime() / 1000));
					} else {
						os.writeC(112);
						os.writeD(getEndTime().getTime() / 1000);
					}
				}

				if (getDamageReduction() != 0) { // 대미지 리덕션
					os.writeC(63);
					os.writeC(getDamageReduction());
				}

				if (getDamageReductionRate() != 0) { // 확률 데미지 리덕션
					os.writeC(64);
					os.writeC(getDamageReductionRate());
					os.writeC(getDamageReductionRateValue());
				} else if (getItem().getItemId() == 22263 || getItem().getItemId() == 900046
						|| getItem().getItemId() == 900071) {
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

				// 사라짐 특성
				/*if (isAccessory() && getItem().getType() != 14 && getItem().get_safeenchant() != -1
						&& getItem().getGrade() >= 0 && !(getItem().getGrade() >= 3 && getItem().getGrade() <= 4)
						&& getItem().getType() != 18) {
					os.writeC(67);
					if (getItem().getType() == 8 || getItem().getType() == 12)
						os.writeC(43);
					else if (getItem().getType() == 9)
						os.writeC(44);
					else if (getItem().getType() == 10) {
						os.writeC(45);
					}
				}*/

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

				if (getWeightReduction() != 0) { // 무게 게이지
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
					os.writeC(39);
					os.writeS("\fIMP 絕對恢復:\\aA +"+getMpAr16()+"(16秒)");
				}
				if (getCCIncrease() != 0) {
					os.writeC(219);
					os.writeH(getCCIncrease());
				}

				if (getSmeltingValue() != 0){
					os.writeC(181);//[재련]
					os.writeC(1);
					for (int i = 0; i < getSmeltingValue(); i++){
						os.writeC(0xe6);
						os.writeC(i);
						if (i == 0){
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
						if (i == 1){
							if (getSmeltingItemId2() == 0) {
								continue;
							}
							SmeltingScrollInfo sInfo2 = SmeltingScrollLoader.getInstance().getSmeltingScrollInfo(getSmeltingItemId2());
							try {
								os.write(SmeltingScrollInfo.getItemView(sInfo2));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
//						SC_SMELTING_UPDATE_SLOT_INFO_NOTI.send(pc, this);
					}
					os.writeC(0xb6);
					os.writeC(0);
				}

				/**
				 * 세트아머효과표기
				 */
				L1ArmorSetImpl armor_sets = ArmorSetTable.getInstance().find(getItemId());
				if (armor_sets != null && !armor_sets.is_signle_items()) {
					L1ArmorSets sets = armor_sets.get_source_effects();
					os.writeC(69); // -- 셋트아이템에 대한 표기 ( 고정값 )
					os.writeC(get_main_set_armor() ? 1 : 2); // -- 1: 셋트 아이템 다 착용시 / 2: 셋트 아이템 미 착용시
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

	private L1PcInstance _owner;

	class EnchantTimer implements Runnable {

		private int _skillId;
		private boolean _active = true;
		private boolean _effectClear = false;
		private long _expireTime;

		public EnchantTimer(int skillId, long expireTime) {
			_skillId = skillId;
			_expireTime = expireTime;
		}

		public int getRemainTime() {
			int remainTime = (int) (_expireTime - System.currentTimeMillis()) / 1000;

			if (remainTime < 1) {
				remainTime = 1;
			}

			return remainTime;
		}

		@Override
		public void run() {
			try {
				if (!_active) {
					return;
				}

				ClearEffect();
			} catch (Exception e) {
			}
		}

		public void cancel() {
			_active = false;
			ClearEffect();
			if (_owner != null)
				_owner.sendPackets(new S_ServerMessage(308, getLogName()));
		}

		@SuppressWarnings("unused")
		public void on_off_icons() {
			if (_owner == null)
				return;

			SC_SPELL_BUFF_NOTI noti = null;
			switch (_skillId) {
				// case
				default:
					break;
			}

			if (noti != null)
				_owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}

		@SuppressWarnings("unused")
		public void on_on_icons() {
			if (_owner == null)
				return;

			SC_SPELL_BUFF_NOTI noti = null;
			switch (_skillId) {
				// case
				default:
					break;
			}
			if (noti != null)
				_owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}

		public void ClearEffect() {
			synchronized (this) {
				if (_effectClear) {
					return;
				}

				_effectClear = true;
			}
			switch (_skillId) {
				default:
					break;
			}
			on_off_icons();
			removeSkillEffectTimer(_skillId);
		}
	}

	private int _acByMagic = 0;

	public int getAcByMagic() {
		return _acByMagic;
	}

	public void addAcByMagic(int i) {
		_acByMagic += i;
	}

	private int _dmgByMagic = 0;

	public int getDmgByMagic() {
		return _dmgByMagic;
	}

	public void addDmgByMagic(int i) {
		_dmgByMagic += i;
	}

	private int _holyDmgByMagic = 0;

	public int getHolyDmgByMagic() {
		return _holyDmgByMagic;
	}

	public void addHolyDmgByMagic(int i) {
		_holyDmgByMagic += i;
	}

	private int _hitByMagic = 0;

	public int getHitByMagic() {
		return _hitByMagic;
	}

	public void addHitByMagic(int i) {
		_hitByMagic += i;
	}

	final int[][] repeatedSkills = { // (아이템(무기/방어구) 전용같은 효과 버프 중복안되게 처리(같은 라인에 맞춰서 적용됨)
			{}, };

	private void remove_repeated_skills(int skill_id) {
		for (int[] skills : repeatedSkills) {
			for (int id : skills) {
				if (id == skill_id) {
					killSkillEffectTimer(skills);
					return;
				}
			}
		}
	}

	public void setSkillWeaponEnchant(L1PcInstance pc, int skillId, int skillTime) {
		setSkillWeaponEnchant(pc, skillId, skillTime, 0);
	}

	public void setSkillArmorEnchant(L1PcInstance pc, int skillId, int skillTime) {

		if (getItem().getType2() != 2 || getItem().getType() != 2) {
			return;
		}

		remove_repeated_skills(skillId);

		switch (skillId) {
		}
		EnchantTimer timer = new EnchantTimer(skillId, System.currentTimeMillis() + skillTime);
		timer.on_on_icons();
		_skillEffect.put(skillId, timer);
		GeneralThreadPool.getInstance().schedule(timer, skillTime);
	}

	public void setSkillWeaponEnchant(L1PcInstance pc, int skillId, int skillTime, int weapon_index) {
		if (getItem().getType2() != 1) {
			return;
		}
		L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
		remove_repeated_skills(skillId);
		switch (skillId) {
			default:
				break;
		}
		EnchantTimer timer = new EnchantTimer(skillId, System.currentTimeMillis() + skillTime);
		timer.on_on_icons();
		_skillEffect.put(skillId, timer);
		GeneralThreadPool.getInstance().schedule(timer, skillTime);
		setEnchantMagic(skill.getCastGfx());
		if (skillId == L1SkillId.HOLY_WEAPON) {
			setEnchantMagic(2165);
		}
	}

	private int _enchantmagic = 0;

	public int getEnchantMagic() {
		return _enchantmagic;
	}

	public void setEnchantMagic(int i) {
		_enchantmagic = i;
	}

	protected void removeSkillEffectTimer(int skillId) {
		_skillEffect.remove(skillId);
	}

	public boolean hasSkillEffectTimer(int skillId) {
		return _skillEffect.containsKey(skillId);
	}

	protected void killSkillEffectTimer(int skillId) {
		EnchantTimer timer = _skillEffect.remove(skillId);
		if (timer != null) {
			timer.cancel();
		}
	}

	protected void killSkillEffectTimer(int[] skills) {
		for (int skillId : skills) {
			killSkillEffectTimer(skillId);
		}
	}

	// 아이콘을 켠다
	public void on_skill_effect_icons() {
		for (EnchantTimer timer : _skillEffect.values())
			timer.on_on_icons();
	}

	// 아이콘을 끈다.
	public void off_skill_effect_icons() {
		for (EnchantTimer timer : _skillEffect.values())
			timer.on_off_icons();
	}

	public int getSkillEffectTimeSec(int skillId) {
		EnchantTimer timer = _skillEffect.get(skillId);
		if (timer == null) {
			return -1;
		}
		return timer.getRemainTime();
	}

	private L1PcInstance _itemOwner;

	public L1PcInstance getItemOwner() {
		return _itemOwner;
	}

	public void setItemOwner(L1PcInstance pc) {
		_itemOwner = pc;
	}

	public void startItemOwnerTimer(L1PcInstance pc) {
		setItemOwner(pc);
		L1ItemOwnerTimer timer = new L1ItemOwnerTimer(this, 10000);
		timer.begin();
	}

	private L1EquipmentTimer _equipmentTimer;

	public void startEquipmentTimer(L1PcInstance pc) {
		if (getRemainingTime() > 0) {
			_equipmentTimer = new L1EquipmentTimer(pc, this, 1000);
			GeneralThreadPool.getInstance().schedule(_equipmentTimer, 1000);
		}
	}

	public void stopEquipmentTimer(L1PcInstance pc) {
		if (getRemainingTime() > 0) {
			_equipmentTimer.cancel();
			_equipmentTimer = null;
		}
	}

	private boolean _isNowLighting = false;

	public boolean isNowLighting() {
		return _isNowLighting;
	}

	public void setNowLighting(boolean flag) {
		_isNowLighting = flag;
	}

	private int _DropMobId = 0;

	public int isDropMobId() {
		return _DropMobId;
	}

	public void setDropMobId(int i) {
		_DropMobId = i;
	}

	private int _keyId = 0;

	public int getKeyId() {
		return _keyId;
	}

	public void setKeyId(int i) {
		_keyId = i;
	}

	public void onEquip(L1PcInstance pc) {
		_owner = pc;
	}

	public void onUnEquip() {
		_owner = null;
	}

	private boolean armor_set;

	public boolean get_armor_set() {
		return armor_set;
	}

	public void set_armor_set(boolean b) {
		this.armor_set = b;
	}

	private boolean main_set_armor;

	public boolean get_main_set_armor() {
		return main_set_armor;
	}

	public void set_main_set_armor(boolean b) {
		this.main_set_armor = b;
	}

	private int bless_level;

	public int get_bless_level() {
		return bless_level;
	}

	public void set_bless_level(int i) {
		this.bless_level = CommonUtil.get_current(i, 0, 3);
	}

	/** 특수 인챈트 시스템 **/
	private int item_level;

	public int get_item_level() {
		return item_level;
	}

	public void set_item_level(int i) {
		this.item_level = i;
	}

	private String _Hotel_Town;

	public String getHotel_Town() {
		return _Hotel_Town;
	}

	public void setHotel_Town(String name) {
		_Hotel_Town = name;
	}

	private int Carving;

	public int get_Carving() {
		return Carving;
	}

	public void set_Carving(int i) {
		this.Carving = i;
	}



	private int _Doll_bonus_value;

	public int get_Doll_Bonus_Value() {
		return _Doll_bonus_value;
	}

	public void set_Doll_Bonus_Value(int i) {
		this._Doll_bonus_value = i;
	}

	private int _Doll_bonus_level;

	public int get_Doll_Bonus_Level() {
		return _Doll_bonus_level;
	}

	public void set_Doll_Bonus_Level(int i) {
		this._Doll_bonus_level = i;
	}

	public ArrayList<L1ItemBookMark> _bookmarks;

	public ArrayList<L1ItemBookMark> getBookMark() {
		return _bookmarks;
	}

	public void addBookMark(L1ItemBookMark list) {
		_bookmarks.add(list);
	}

	public int getAttrEnchantBit(int attr) {
		int attr_bit = 0;
		int result_bit = 0;
		if (attr >= 1 && attr <= 5) {
			attr_bit = 1;
		}
		if (attr >= 6 && attr <= 10) {
			attr_bit = 2;
			attr = attr - 5;
		}
		if (attr >= 11 && attr <= 15) {
			attr_bit = 3;
			attr = attr - 10;
		}
		if (attr >= 16 && attr <= 20) {
			attr_bit = 4;
			attr = attr - 15;
		}

		if (attr > 0) {
			result_bit = attr_bit + (16 * attr);
		}

		return result_bit;
	}

	private int _openEffect = 0;

	public void setOpenEffect(int i) {
		_openEffect = i;
	}

	public int getOpenEffect() {
		return _openEffect;
	}

	public byte[] serialize() {
		byte[] data = null;
		MJBytesOutputStream os = null;
		try {
			os = new MJBytesOutputStream(128);
			os.write(0x08); // object_id
			os.writeBit(getId());
			os.write(0x10); // name_id
			os.writeBit(_item.getItemDescId());
			os.write(0x18); // db_id
			os.writeBit(getId());
			os.write(0x20); // count
			os.writeBit(_count);
			/*
			 * os.write(0x28); // interact_type os.writeBit(_item.getUseType());
			 * os.write(0x30); // number_of_use os.writeBit(0);
			 */
			os.write(0x28);
			os.writeBit(_item.getUseType());

			os.write(0x38); // icon_id
			os.writeBit(_item.getGfxId());
			os.write(0x40); // bless_code_for_display
			os.writeBit(bless);

			/**
			 * 2 : 교환 불가 4 : 삭제 불가 8 : 인챈 불가 16 : 창고 보관 가능 32 : 봉인 64 : 특수 봉인
			 **/
			os.write(0x48); // attribute_bit_set
			int bit = (!_item.isTradable() ? 2 : 16) | (_item.isCantDelete() ? 4 : 0)
					| (_item.get_safeenchant() < 0 ? 8 : 0) | (isIdentified() ? 1 : 0);
			os.writeBit(bit);

			os.write(0x50); // attribute_bit_set_ex(usetype)
			if (_item.isEndedTimeMessage())
				os.writeBit(0x01); // 각인
			else
				os.writeBit(0x08); // 낚시
			os.write(0x58); // is_timeout
			os.writeB(false);
			/*
			 * os.write(0x60); // category os.writeBit(0x1); os.write(0x68); // enchant
			 * os.writeBit(1);
			 */

			/**
			 * 0:창고불가 2:특수가능 3:개인/특수가능 7:개인/혈/특수가능
			 **/
			os.write(0x70); // deposit
			os.write(0x03);
			/*
			 * os.write(0x78); // overlay_surf_id os.writeBit(0); os.writeBit(0x80); //
			 * elemental_enchant_type os.writeBit(0x01); os.writeBit(0x88); //
			 * elemental_enchant_value os.writeBit(0x05);
			 */
			/*
			 * os.writeBit(0x8A); // description byte[] tmp = getStatusBytes();
			 * os.writeBit(tmp.length); os.write(tmp); tmp = null;
			 */
			os.writeBit(0x92); // extra_description
			os.writeS2(getViewName());
			/*
			 * os.writeBit(0x92); // description os.writeS2(getViewName());
			 * if(isIdentified()){ os.writeBit(0x9A); // extra_description byte[] tmp =
			 * getStatusBytes(); os.writeBit(tmp.length); os.write(tmp); tmp = null; }
			 * os.writeBit(0xA0); // left_time_for_pre_notify os.writeBit(100);
			 */
			data = os.toArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			os.close();
			os.dispose();
		}
		return data;
	}

	public byte[] serializeFishingItem() {
		byte[] data = null;
		MJBytesOutputStream os = null;
		try {
			os = new MJBytesOutputStream(128);
			os.write(0x08); // object_id
			os.writeBit(getId());
			os.write(0x10); // name_id
			os.writeBit(_item.getItemDescId());
			os.write(0x18); // db_id
			os.writeBit(getId());
			os.write(0x20); // count
			os.writeBit(_count);
			os.write(0x28);
			os.writeBit(_item.getUseType());
			os.write(0x38); // icon_id
			os.writeBit(_item.getGfxId());
			os.write(0x40); // bless_code_for_display
			os.writeBit(bless);
			os.write(0x48); // attribute_bit_set
			int bit = (!_item.isTradable() ? 2 : 16) | (_item.isCantDelete() ? 4 : 0)
					| (_item.get_safeenchant() < 0 ? 8 : 0) | (isIdentified() ? 1 : 0);
			os.writeBit(bit);

			os.write(0x50); // attribute_bit_set_ex(usetype)

			if (_item.isEndedTimeMessage())
				os.writeBit(0x01); // 각인
			else
				os.writeBit(0x08); // 낚시

			os.write(0x58); // is_timeout
			os.writeB(false);

			os.write(0x70); // deposit
			/**
			 * 0:창고불가 2:특수가능 3:개인/특수가능 7:개인/혈/특수가능
			 **/
			os.writeBit(3);

			os.writeBit(0x92); // extra_description
			os.writeS2(getViewName());
			if (isIdentified()) {
				os.writeBit(0x9A); // extra_description
				byte[] tmp = getStatusBytes();
				os.writeBytes(tmp);
			}
			data = os.toArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			os.close();
			os.dispose();
		}
		return data;
	}

	private static int _instanceType = -1;

	@Override
	public int getL1Type() {
		return _instanceType == -1 ? _instanceType = super.getL1Type() | MJL1Type.L1TYPE_ITEMINSTANCE : _instanceType;
	}

	private boolean m_is_give = false;

	public boolean isGiveItem() {
		return m_is_give;
	}

	public void setGiveItem(boolean is_give) {
		m_is_give = is_give;
	}

	public boolean isCanbeDmg() {
		boolean result = getItem().get_canbedmg() == 0 ? true : false;

		return result;
	}

	public boolean isUndeadDmg() {
		boolean result = false;

		if (getItem().getType2() == 1
				|| (getItem().getType2() == 0 && (getItem().getType() == 0 || getItem().getType() == 15))) {

			if (getItem().getMaterial() == 14 || getItem().getMaterial() == 17 || getItem().getMaterial() == 22)
				result = true;
		}

		return result;
	}

	public int getAcByEnchantLevel() {
		int result = 0;
		int enchantLevel = getEnchantLevel();

		if (isAccessory()) {
			if (getItemId() == 22226 || getItemId() == 22228 || getItemId() == 222290 || getItemId() == 222291
					|| getItemId() == 222332 || getItemId() == 222334 || getItemId() == 222335 || getItemId() == 222336
					|| getItemId() == 900195) {
				result += enchantLevel <= 1 ? 0 : enchantLevel <= 4 ? enchantLevel - 1 : 3;
				if (enchantLevel >= 1 && (getItemId() == 222291 || getItemId() == 222336)) {
					result++;
				}
				if (enchantLevel >= 7 && (getItemId() == 222335)) {
					result++;
				}
				if (enchantLevel >= 6 && (getItemId() == 22226 || getItemId() == 22228 || getItemId() == 222290
						|| getItemId() == 900195)) {
					result++;
				}

				if (enchantLevel > 7 && getItemId() == 222290 || getItemId() == 222291
						|| getItemId() == 22224 || getItemId() == 22225 || getItemId() == 22226 || getItemId() == 22227 || getItemId() == 22228) {
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
					result += enchantLevel - 4 > 4 ? 4 : enchantLevel - 4;
				} else if (getBless() % 128 != 0 && getEnchantLevel() >= 6) {
					result += enchantLevel - 5 > 3 ? 3 : enchantLevel - 5;
				}
			} else if (getItemId() == 22225 || getItemId() == 22227 || getItemId() == 22224 || getItemId() == 222331
					|| getItemId() == 222333 || getItemId() == 222330) {
				result += enchantLevel <= 4 ? enchantLevel - 1 : enchantLevel <= 1 ? 0 : 3;
			} else if (getItemId() == 22230 || getItemId() == 222338) {
				enchantLevel = enchantLevel > 9 ? 9 : enchantLevel;
				if(enchantLevel == 9) {
					result +=1;
					if (getBless() % 128 == 0) {
						result +=1;
					}
				}
				if (getBless() % 128 == 0)
					enchantLevel++;
				result += (enchantLevel == 5 ? 1 : enchantLevel <= 4 ? 0 : (enchantLevel - 1) / 2);
			} else if (getItemId() == 222340 || getItemId() == 222341) {
				enchantLevel = enchantLevel > 9 ? 9 : enchantLevel;
				if (getBless() % 128 == 0 && enchantLevel > 2)
					enchantLevel++;
				result += enchantLevel;
			} else if (getItem().getType2() == 2 && getItem().getType() == 30) {
				if (getItemId() == 900084 || getItemId() == 900196
						|| (getItemId() >= 900081 && getItemId() <= 900083)) {
					if (enchantLevel >= 3)
						result += enchantLevel <= 8 ? enchantLevel - 2 : 7;
					if (enchantLevel >= 6)
						result++;
				} else {
					if (enchantLevel >= 4 && enchantLevel <= 6)
						result += enchantLevel - 3;
					else if (enchantLevel >= 7)
						result += 3;
				}
			} else if (getItem().getType2() == 2 && enchantLevel >= 5
					&& (getItem().getType() == 8 || getItem().getType() == 12)) {
				result += enchantLevel <= 10 ? enchantLevel - 4 : 5;
			}
		} else if (getItem().getType2() == 2 && getItem().getType() == 14) { // 룬일경우인챙당ac증가안됨
			result = 0;
		} else if (getItem().getType2() == 2 && getItem().getType() == 28) { // 문장일경우인챈당ac증가안됨
			result = 0;
		} else if (getItemId() >= 900234 && getItemId() <= 900237) { // 펜던트일경우인챙당ac증가안됨
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
		if (eb != null)
			result += eb.getAc(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Ac) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getaddAc() {
		int result = 0;
		int enchantLevel = getEnchantLevel();
		int type = getItem().getType();
		int blesslevel = get_bless_level();

		if (!(getItemId() >= 22224 && getItemId() <= 22228 || getItemId() >= 222330 && getItemId() <= 222336
				|| getItemId() == 222290 || getItemId() == 222291 || getItemId() == 900195
				|| getItemId() >= 22229 && getItemId() <= 22231 || getItemId() >= 222337 && getItemId() <= 222341
				|| getItemId() == 900194)) {

			/*if (get_bless_level() != 0) {
				if (getItem().getType2() == 2 && !(type >= 8 && type <= 12)) {
					result += get_bless_level();
				}
			}
		}*/

			if (get_bless_level() != 0) {
				if (getItem().getType2() == 2 && !(type >= 8 && type <= 12)) {
					if (blesslevel == 1)
						result += Config.ServerEnchant.blessChance_armor_effect1;
					else if (blesslevel == 2)
						result += Config.ServerEnchant.blessChance_armor_effect2;
					else if (blesslevel == 3)
						result += Config.ServerEnchant.blessChance_armor_effect3;
				}
			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getaddAc(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.addAc) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public boolean isAccessory() {
		return getItem().getType2() == 2
				&& (getItem().getType() == 8 || getItem().getType() == 9 || getItem().getType() == 10
				|| getItem().getType() == 11 || getItem().getType() == 12 || getItem().getType() == 30);
	}

	public int getaddHp() {
		int result = 0;
		int enchantLevel = getEnchantLevel();
		int blesslevel = get_bless_level();
		int type = getItem().getType();

		if (!(getItemId() >= 22224 && getItemId() <= 22228 || getItemId() >= 222330 && getItemId() <= 222336
				|| getItemId() == 222290 || getItemId() == 222291 || getItemId() == 900195
				|| getItemId() >= 22229 && getItemId() <= 22231 || getItemId() >= 222337 && getItemId() <= 222341
				|| getItemId() == 900194)) {
			if (get_bless_level() != 0) {
				if (getItem().getType2() == 2 && type >= 8 && type <= 12) {
					if (blesslevel == 1)
						result += Config.ServerEnchant.blessChance_accessory_effect1;
					else if (blesslevel == 2)
						result += Config.ServerEnchant.blessChance_accessory_effect2;
					else if (blesslevel == 3)
						result += Config.ServerEnchant.blessChance_accessory_effect3;
				}
			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getaddHp(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.addHp) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getHp() {
		int result = getItem().get_addhp();
		int enchantLevel = getEnchantLevel();

		if (getItem().getType2() == 2 && getItem().getGrade() >= 0
				&& !(getItem().getGrade() >= 3 && getItem().getGrade() <= 4) && getItem().getType() != 10) {
			result += (enchantLevel == 3 ? 20
					: enchantLevel <= 2 ? enchantLevel * 5 : enchantLevel <= 0 ? 0 : (enchantLevel + 3) / 2 * 10);
			if(enchantLevel == 10) {
				result += 10;
			}
			if(enchantLevel == 10 && getItem().getType() == 8) {
				result += 30;
			}
		}
		if(enchantLevel == 10 && getItem().getType() == 10) {
			result += 40;
		}

		if (getItemId() == 22224 || getItemId() == 22225 || getItemId() == 22226 || getItemId() == 22227
				|| getItemId() == 22228 || getItemId() == 222330 || getItemId() == 222331 || getItemId() == 222332
				|| getItemId() == 222333 || getItemId() == 222334 || getItemId() == 900195) { // 스냅퍼류 HP
			if (getItemId() == 222332 && enchantLevel <= 5)
				result += 5;
			if (getItemId() == 222334 && enchantLevel <= 7)
				result += 5;
			result += (enchantLevel <= 0 ? 0 : 10 + enchantLevel * 5);
			if (getItemId() == 222332 && getBless() % 128 == 0)
				result += (enchantLevel <= 5 ? 0 : (enchantLevel - 5) * 5);
		} else if (getItemId() == 222290 || getItemId() == 222335) { // 스냅퍼 지혜의
			if (getBless() % 128 == 0) {
				result += (enchantLevel <= 7 ? 5 + enchantLevel * 5
						: enchantLevel <= 2 ? enchantLevel * 5 : enchantLevel <= 0 ? 0 : 10 + enchantLevel * 5);
			} else
				result += (enchantLevel <= 0 ? 0 : enchantLevel * 5);
		} else if (getItemId() == 222291) {
			result += (enchantLevel <= 2 ? 0 : (enchantLevel - 2) * 5);
		} else if (getItemId() == 222336) {
			if (enchantLevel == 8)
				result -= 5;
			result += 5 + (enchantLevel <= 2 ? 0 : (enchantLevel - 2) * 5);
		} else if ((getItemId() == 22229 || getItemId() == 222337) && enchantLevel > 0) { // 룸티스
			if (getBless() % 128 == 0)
				enchantLevel++;
			result += (enchantLevel < 9 ? 10 + enchantLevel * 10 : enchantLevel <= 0 ? 0 : 140);
			if(getBless() % 128 == 0 && enchantLevel == 10) {
				result += 10;
			}
			if(getBless() % 128 != 0 && enchantLevel == 9) {
				result -= 40;
			}
		} else if (getItemId() == 22256) { // 체력의 가더
			result += (enchantLevel < 5 ? 0 : (enchantLevel - 3) / 2 * 25);
		} else if ((getItemId() >= 900025 && getItemId() <= 900028) || (getItemId() >= 900184 && getItemId() <= 900187)
				|| getItemId() == 900198) { // 용티셔츠
			result += enchantLevel > 9 ? 100 : 0;
		} else if (getItem().getType() == 10) {
			result += enchantLevel > 5 ? (enchantLevel - 4) * 10 : 0;
		} else if (getItemId() == 22359 || (getItemId() >= 222307 && getItemId() <= 222309)) {
			result += enchantLevel > 6 ? (enchantLevel - 6) * 20 : 0;
		} else if ((getItemId() >= 900081 && getItemId() <= 900083)) {
			result += 5 + (enchantLevel * 5);
			if (getItemId() >= 900081 && getItemId() <= 900083) {
				if (enchantLevel >= 8)
					result += 5;
			}
		} else if (getItemId() >= 900152 && getItemId() <= 900154 || getItemId() == 900196 || getItemId() == 900084) {
			result += (enchantLevel + 1) * 5;
			if (enchantLevel >= 8)
				result += 5;
		} else if(getItemId() == 22231 || getItemId() == 222339) {
			if(enchantLevel >= 8) {
				result += 50 + ((enchantLevel - 8) * 50) + (getBless() % 128 == 0 ? 50 : 0);
			}
		}
		/*if (getItemId() == 900268) {
			if (enchantLevel == 7)
				result += 50;
			else if (enchantLevel == 8)
				result += 100;
			else if (enchantLevel == 9)
				result += 150;
			else if (enchantLevel >= 10)
				result += 200;
		}*/

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getHp(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Hp) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getMp() {
		int result = getItem().get_addmp();
		int enchantLevel = getEnchantLevel();

		if (getItem().getType2() == 2 && getItem().getType() == 10) {
			result += (enchantLevel == 3 ? 20
					: enchantLevel <= 2 ? enchantLevel * 5 : enchantLevel <= 0 ? 0 : (enchantLevel + 3) / 2 * 10);
			if(enchantLevel == 10) {
				result += 10;
			}
		} else if (getItem().getItemId() == 222335) {
			result += 15;
			if (enchantLevel == 7)
				result += 15;
			else if (enchantLevel == 8)
				result += 20;
			else if (enchantLevel == 9)
				result += 65;
		} else if (getItem().getItemId() == 222290) {
			result += 15;
			if (enchantLevel == 8)
				result += 15;
			else if (enchantLevel == 9)
				result += 55;
		} else if (getItem().getItemId() == 22231 || getItem().getItemId() == 222339) {
			if (getItem().getItemId() == 222339 && enchantLevel <= 3)
				result -= 10;
			if (getItem().getItemId() == 22231 && enchantLevel == 0)
				result -= 25;
			else if (getItem().getItemId() == 22231 && enchantLevel == 1)
				result -= 20;
			else if (getItem().getItemId() == 22231 && enchantLevel == 2)
				result -= 20;
			else if (getItem().getItemId() == 22231 && enchantLevel >= 3 && enchantLevel <= 4)
				result -= 10;

			if(getBless() % 128 == 0 && enchantLevel >=9) {
				result += 30;
			}

			if (getBless() % 128 == 0)
				enchantLevel++;
			result += (enchantLevel == 8 ? 95
					: enchantLevel == 7 ? 70
					: enchantLevel <= 6 ? 25 + enchantLevel * 5
					: enchantLevel <= 4 ? 15 + enchantLevel * 5
					: enchantLevel <= 2 ? 5 + enchantLevel * 5 : enchantLevel <= 0 ? 0 : 125);
		} else if (getItemId() == 410011) {
			result += 40;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getMp(enchantLevel);


		if (getBlessType() == L1BlessTypeEnchant.Mp) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getSp() {
		int result = getItem().get_addsp();
		int enchantLevel = getEnchantLevel();

		if (getItemId() == 222290 || getItemId() == 222335) { // 스냅퍼 지혜의 반지
			if (getBless() % 128 == 0) {
				result += enchantLevel > 3 ? enchantLevel - 3 : 0;
			} else {
				result += enchantLevel > 4 ? enchantLevel - 4 : 0;
			}
			if(enchantLevel >= 8) {
				if (getBless() % 128 == 0) {
					result += 1;
				}
				result += enchantLevel - 8 + 1;
			}
		} else if (getItemId() == 22231 || getItemId() == 222339) { // 룸티스 보랏빛 귀걸이
			if (getBless() % 128 == 0)
				enchantLevel++;
			result += (enchantLevel <= 2 ? 0 : (enchantLevel - 1) / 2);
			if(enchantLevel == 8 && getBless() % 128 != 0) {
				result += 2;
			}
			if(enchantLevel == 9) {
				if(getBless() % 128 != 0) {
					result += 2;
				}
				result += 1;
			}

			if(enchantLevel == 10) {
				result += 5;
			}
		} else if (getItemId() == 22255) { // 마법사의 가더
			result += (enchantLevel < 5 ? 0 : (enchantLevel - 3) / 2);
		} else if (getItemId() == 20107) { // 리치 로브
			result += (enchantLevel > 2 ? enchantLevel - 2 : 0);
		} else if (getItemId() == 900032 || getItemId() == 900228) { // 수룡의 티셔츠, 유니콘의 지식각반
			result += (enchantLevel > 8 ? 1 : 0);
		} else if (getItemId() == 124 || getItemId() == 900119 || getItemId() == 900221) { // 바포메트 지팡이
			result += (enchantLevel > 6 ? enchantLevel - 6 : 0);
		} else if (getItemId() == 900051 || getItemId() == 900095 || getItemId() == 900098) { // 지식의 문장
			result += (enchantLevel > 5 ? enchantLevel - 5 : 0);
		} else if (getItemId() == 4100039 || getItemId() == 410012) {
			result += 3;
		} else if (getItemId() == 900129 || getItemId() == 900126) {
			result += enchantLevel > 4 ? enchantLevel - 4 : 0;
		} else if (getItemId() == 900028) {
			if (enchantLevel == 8 || enchantLevel == 9)
				result += 1;
			else if (enchantLevel >= 10)
				result += 2;
		} else if (getItemId() == 900187) {
			if (enchantLevel >= 7 && enchantLevel <= 9)
				result += 1;
			else if (enchantLevel >= 10)
				result += 2;
		}
		if (getItemId() == 900236 || getItemId() == 900277) {
			if (enchantLevel >= 4 && enchantLevel <= 8)
				result += 1 + (enchantLevel - 4) * 1;
		}

		if (getItem().getType2() == 2 && getItem().getGrade() >= 0
				&& !(getItem().getGrade() >= 3 && getItem().getGrade() <= 4)
				&& (getItem().getType() == 9 || getItem().getType() == 11)) {
			result += (enchantLevel > 6 ? enchantLevel - 6 : 0);
		}

		if(getItemId() == 22384) {
			if (_cha != null) {
				if(_cha.isWizard() || _cha.isBlackwizard()) {
					result += 2;
				}
			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getSp(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Sp) {
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
				result += 1 + ((getEnchantLevel() - 6) * 2);
			} else if (getBless() % 128 != 0 && getEnchantLevel() >= 7) {
				result += 1 + ((getEnchantLevel() - 7) * 2);
			}
			if(enchantLevel >= 8) {
				result += 2 + (enchantLevel - 8) * 2;
			}
			if(enchantLevel == 9 && getBless() % 128 == 0) {
				result += 1;
			}
		} else if (getItem().getItemId() == 222290 || getItem().getItemId() == 222335) {
			if (getBless() % 128 == 0) {
				result += getEnchantLevel() > 5 ? getEnchantLevel() - 5 : 0;
			} else {
				result += getEnchantLevel() > 6 ? getEnchantLevel() - 6 : 0;
			}
			if(enchantLevel >= 8) {
				if (getBless() % 128 == 0) {
					result += 1;
				}
				result += enchantLevel - 8 + 1;
			}
		} else if (getItemId() == 900119 || getItemId() == 900221) {
			result += getEnchantLevel() > 4 ? getEnchantLevel() - 4 : 0;
		} else if (getItemId() == 900083) {
			result += getEnchantLevel() > 5 ? getEnchantLevel() - 5 : 0;
			if (getEnchantLevel() >= 7)
				result++;
			if (getEnchantLevel() >= 8)
				result++;
		} else if (getItemId() == 900129 || getItemId() == 900126) {
			result += enchantLevel > 3 ? enchantLevel - 3 : 0;
			if (getItemId() == 900129 && enchantLevel > 4)
				result--;
		} else if (getItemId() == 900028 || getItemId() == 900187) {
			if (enchantLevel > 7) {
				result += enchantLevel > 9 ? 4 : enchantLevel > 8 ? 3 : enchantLevel > 7 ? 1 : 0;
				if (getBless() % 128 == 0) {
					result++;
				}
			}
		}

		if ((getItemId() == 22383 || getItemId() == 22384)) {
			if (_cha != null) {
				if (_cha.isWizard() || _cha.isBlackwizard()) {
					result += 2;
				}
			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getMagicHit(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.MagicHit) {
			result += getBlessTypeValue();
		}


		return result;
	}

	public int getDamageReduction() {
		int result = getItem().get_damage_reduction();
		int enchantLevel = getEnchantLevel();

		if (getItemId() == 22229 || getItemId() == 222337) {
			result += enchantLevel > 2 ? enchantLevel - 2 : 0;
			if (enchantLevel > 3 && getBless() % 128 != 0) {
				result--;
			}
			if(enchantLevel == 8) {
				if(getBless() % 128 != 0) {
					result += 2;
				} else {
					result += 1;
				}
			}
			if(enchantLevel == 9 && getBless() % 128 != 0) {
				result += 1;
			}
			if(enchantLevel == 9 && getBless() % 128 == 0) {
				result += 2;
			}
		} else if (getItemId() >= 22196 && getItemId() <= 22199) {
			result += enchantLevel > 6 ? enchantLevel - 6 : 0;
		} else if (getItemId() == 900025 || getItemId() == 900184 || getItemId() == 900198
				|| (getItemId() >= 222307 && getItemId() <= 222309) || getItemId() == 22359) {
			result += enchantLevel > 8 ? 1 : 0;
		} else if (getItemId() == 22254 || getItemId() == 900207) {
			result += (enchantLevel < 5 ? 0 : (enchantLevel - 3) / 2);
		} else if (getItemId() == 22226 || getItemId() == 222332) {
			if (getBless() % 128 == 0) {
				result += enchantLevel > 5 ? enchantLevel - 5 : 0;
			} else {
				result += enchantLevel > 6 ? enchantLevel - 6 : 0;
			}
			if(enchantLevel >= 8) {
				if (getBless() % 128 == 0) {
					result += 1;
				}
				result += enchantLevel - 8 + 1;
			}
		} else if (getItemId() == 900084 || getItemId() == 900196 || (getItemId() >= 900081 && getItemId() <= 900083)) {
			result += enchantLevel > 4 ? enchantLevel - 4 : 0;
		} else if (getItemId() == 900120 || getItemId() == 900222) {
			result += enchantLevel > 6 ? enchantLevel - 6 : 0;
		}
		if (getItemId() == 900237) {
			if (enchantLevel >= 6 && enchantLevel <= 8)
				result += 2 + (enchantLevel - 6) * 2;
		}
		if (getItemId() == 900278) {
			if (enchantLevel >= 5 && enchantLevel <= 8)
				result += 1 + (enchantLevel - 5) * 2;
		}

		if (getItem().getType2() == 2 && getItem().getType() == 10) {
			result += enchantLevel > 4 ? enchantLevel - 4 : 0;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getDamageReduction(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.DmgReduction) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getExpByItem() {
		int result = getItem().getAddExp();
		int enchantLevel = getEnchantLevel();

		if (getItemId() == 900237) {
			if (enchantLevel >= 4 && enchantLevel <= 8)
				result += 4 + (enchantLevel - 4) * 4;
		}
		if (getItemId() == 900278) {
			if (enchantLevel == 4)
				result += 4;
			else if (enchantLevel >= 5)
				result += 6 + (enchantLevel - 4) * 4;
		}
		if (getItem().getItemId() == 900020) {
			result += enchantLevel * 2;
			if (enchantLevel > 4) {
				result -= enchantLevel - 4;
			}
		} else if ((getItemId() >= 900093 && getItemId() <= 900095) || getItemId() == 900099) {
			result += enchantLevel > 2 ? enchantLevel + 1 : 0;
			if (enchantLevel > 6)
				result += enchantLevel - 6;
		} else if (getItemId() == 900025 || getItemId() == 900184 || getItemId() == 900198) {
			if (enchantLevel >= 7) {
				result += (enchantLevel - 7) * 2;
				if (getBless() % 128 == 0) {
					result += 2;
				}
			}
		} else if (MJItemExpBonus.get_bonus_exp(this) > 1) {
			result += MJItemExpBonus.get_bonus_exp(this);
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getExpBonus(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Exp) {
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
		if (eb != null)
			result += eb.getMpr(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Mpr) {
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
		if (eb != null)
			result += eb.getHpr(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Hpr) {
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

			if (getItem().getType() == 9
					&& !(getItemId() >= 22224 && getItemId() <= 22228 || getItemId() == 900195 || getItemId() == 222290
					|| getItemId() == 222291 || (getItemId() >= 222330 && getItemId() <= 222336))) {
				if(enchantLevel == 10) {
					result += 7;
				} else if (enchantLevel == 9) {
					result += 5;
				} else {
					result += enchantLevel > 5 ? enchantLevel - 5 : 0;
				}
			}
		}
		if ((getItemId() >= 22224 && getItemId() <= 22228) || getItemId() == 900195 || getItemId() == 222290
				|| getItemId() == 222291 || (getItemId() >= 222330 && getItemId() <= 222336)) {
			if (enchantLevel == 7) {
				result += enchantLevel - 6;
			} else if(enchantLevel == 8) {
				//getBless() % 128
				result += getBless() % 128 == 0 ? enchantLevel - 6 + 3 : enchantLevel - 6 + 1;
			} else if(enchantLevel >= 9) {
				result += getBless() % 128 == 0 ? enchantLevel - 6 + 6 : enchantLevel - 6 + 4;
			}
		} else if ((getItemId() >= 7000214 && getItemId() <= 7000221) || (getItemId() >= 307 && getItemId() <= 314)) {
			result += enchantLevel > 6 ? 3 + ((enchantLevel - 7) * 2) : 0;
		} else if ((getItemId() >= 900025 && getItemId() <= 900028) || (getItemId() >= 900184 && getItemId() <= 900187)
				|| getItemId() == 900198) {
			result += enchantLevel > 9 ? 1 : 0;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getPvpDmg(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.PvpDmg) {
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
					result += enchantLevel > 5 ? enchantLevel - 5 : 0;
				}
			}
		}

		if ((getItem().getItemId() >= 900025 && getItem().getItemId() <= 900028)
				|| (getItem().getItemId() >= 900184 && getItem().getItemId() <= 900187)
				|| getItem().getItemId() == 900198) {
			result += enchantLevel > 9 ? 1 : 0;
		} else if (getItemId() == 4100042 || getItemId() == 4100465 || getItemId() == 4100041
				|| getItemId() == 4100039) {
			result += 3;
		} else if (getItemId() >= 900081 && getItemId() <= 900083) {
			// result += enchantLevel >= 7 ? enchantLevel - 6 : 0;
			if (enchantLevel == 5) {
				result += 1;
			} else if (enchantLevel == 6) {
				result += 2;
			} else if (enchantLevel == 7) {
				result += 3;
			} else if (enchantLevel >= 8) {
				result += 5;
			}
		}
		if (getItemId() >= 900234 && getItemId() <= 900236) {
			if (enchantLevel >= 6 && enchantLevel <= 8)
				result += 2 + (enchantLevel - 6) * 2;
		}
		if (getItemId() >= 900275 && getItemId() <= 900277) {
			if (enchantLevel >= 5 && enchantLevel <= 8)
				result += 1 + (enchantLevel - 5) * 2;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getPvpReduction(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.PvpReduction) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getMagicDmgModifier() {
		int result = getItem().getMagicDmgModifier();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getMagicDmgModifier(enchantLevel);

		return result;
	}

	public int getWeightReduction() {
		int result = getItem().getWeightReduction();
		int enchantLevel = getEnchantLevel();

		if (getItem().getItemId() == 20274) {
			result += enchantLevel > 4 ? (enchantLevel - 4) * 60 : 0;
		}
		if (getItem().getItemId() >= 900275 && getItem().getItemId() <= 900275) {
			// DB에서 360이니 + 140 = 500
			if (enchantLevel >= 5)
				result += 140;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getWeightReduction(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.WeightReduction) {
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
			result += enchantLevel > 6 ? (enchantLevel > 8 ? enchantLevel - 5 : enchantLevel - 6) : 0;
		} else if (getItem().getItemId() == 222332) {
			result += enchantLevel > 5 ? (enchantLevel > 8 ? enchantLevel - 4 : enchantLevel - 5) : 0;
		} else if (getItemId() == 22229 && enchantLevel >= 5) {
			result += enchantLevel - 3;
			if(enchantLevel == 9) {
				result +=1;
			}
		} else if (getItemId() == 222337 && enchantLevel >= 4) {
			result += enchantLevel - 2;
			if(enchantLevel == 9) {
				result +=1;
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
		} else if (getItemId() == 22229 || getItemId() == 222337
				|| getItemId() == 900090) {
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
		int result = getItem().getSpecialResistance(eKind.ABILITY);
		int enchantLevel = getEnchantLevel();

		if ((getItemId() >= 22224 && getItemId() <= 22228) || getItemId() == 900195 || getItemId() == 222290
				|| getItemId() == 222291 || (getItemId() >= 222330 && getItemId() <= 222336)) {
			result += enchantLevel > 5 ? (5 + (getEnchantLevel() - 6) * 2 > 9 ? 9 : 5 + (getEnchantLevel() - 6) * 2) : 0;
		} else if ((getItemId() >= 900026 && getItemId() <= 900028)
				|| (getItemId() >= 900185 && getItemId() <= 900187)) {
			result += enchantLevel > 4 ? enchantLevel + 3 : 0;
			if (enchantLevel > 7)
				result += enchantLevel == 8 ? 1 : enchantLevel == 9 ? 3 : enchantLevel >= 10 ? 5 : 0;
		}
		if (getItem().getType2() == 2 && (getItem().getType() == 8 || getItem().getType() == 12)
				&& !((getItemId() >= 22224 && getItemId() <= 22229) || getItemId() == 22230 || getItemId() == 22231 || getItemId() == 900195
				|| getItemId() == 222290 || getItemId() == 222291
				|| (getItemId() >= 222330 && getItemId() <= 222341))) {
			result += enchantLevel > 6 ? enchantLevel - 5 : 0;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getTechniqueTolerance(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.TechniqueTolerance) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getSpiritTolerance() {
		int result = getItem().getSpecialResistance(eKind.SPIRIT);
		int enchantLevel = getEnchantLevel();

		if (getItemId() >= 900185 && getItemId() <= 900187) {
			int sub_result = enchantLevel > 9 ? 4 : enchantLevel > 8 ? 5 : enchantLevel > 6 ? 6 : 0;
			result += enchantLevel > 6 ? (enchantLevel - sub_result) : 0;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getSpiritTolerance(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.SpiritTolerance) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getDragonLangTolerance() {
		int result = getItem().getSpecialResistance(eKind.DRAGON_SPELL);
		int enchantLevel = getEnchantLevel();

		if (getItemId() == 20235
				|| (getItemId() >= 22196 && getItemId() <= 22203 || getItemId() >= 22208 && getItemId() <= 22211)) {
			result += enchantLevel > 4 ? enchantLevel - 4 : 0;
			if (result > 7) {
				result = 7;
			}
		} else if (getItemId() >= 900185 && getItemId() <= 900187) {
			int sub_result = enchantLevel > 9 ? 4 : enchantLevel > 8 ? 5 : enchantLevel > 6 ? 6 : 0;
			result += enchantLevel > 6 ? (enchantLevel - sub_result) : 0;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getDragonLangTolerance(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.DragonlangTolerance) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getFearTolerance() {
		int result = getItem().getSpecialResistance(eKind.FEAR);
		int enchantLevel = getEnchantLevel();

		if (getItemId() >= 900185 && getItemId() <= 900187) {
			int sub_result = enchantLevel > 9 ? 4 : enchantLevel > 8 ? 5 : enchantLevel > 6 ? 6 : 0;
			result += enchantLevel > 6 ? (enchantLevel - sub_result) : 0;
		}

		if(enchantLevel >= 8) {
			if (getItemId() == 22226 || getItemId() == 22228 || getItemId() == 222290 || getItemId() == 222291
					|| getItemId() == 222332 || getItemId() == 222334 || getItemId() == 222335 || getItemId() == 222336
					|| getItemId() == 900195) {
				if (getBless() % 128 == 0) {
					result += enchantLevel - 7;
				}
				result += enchantLevel - 7;
			}
		}

		if (getItem().getType2() == 2 && (getItem().getType() == 8 || getItem().getType() == 10)
				&& !((getItemId() >= 22224 && getItemId() <= 22229) || getItemId() == 22231 || getItemId() == 22230 || getItemId() == 900195
				|| getItemId() == 222290 || getItemId() == 222291
				|| (getItemId() >= 222330 && getItemId() <= 222341))) {
			if(enchantLevel == 10) {
				result += 2;
			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getFearTolerance(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.FearTolerance) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getAllTolerance() {
		int result = getItem().getSpecialResistance(eKind.ALL);
		int enchantLevel = getEnchantLevel();

		if ((getItemId() >= 900117 && getItemId() <= 900119) || (getItemId() >= 900219 && getItemId() <= 900221)) {
			result += enchantLevel > 5 ? enchantLevel - 5 : 0;
			if (result > 5) {
				result = 5;
			}
		}
		if (getItemId() >= 900234 && getItemId() <= 900236) {
			if (enchantLevel >= 6 && enchantLevel <= 8)
				result += 2 + (enchantLevel - 6) * 2;
		}
		if (getItemId() >= 900275 && getItemId() <= 900277) {
			if (enchantLevel >= 5 && enchantLevel <= 8)
				result += 1 + (enchantLevel - 5) * 2;
		}

		if (getItem().getType2() == 2 && (getItem().getType() == 12 || getItem().getType() == 9)
				&& !((getItemId() >= 22224 && getItemId() <= 22229) || getItemId() == 22231 || getItemId() == 22230 || getItemId() == 900195
				|| getItemId() == 222290 || getItemId() == 222291
				|| (getItemId() >= 222330 && getItemId() <= 222341))) {
			if(enchantLevel == 10) {
				result += 1;
			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getAllTolerance(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.AllTolerance) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getTechniqueHit() {
		int result = getItem().getSpecialPierce(eKind.ABILITY);
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getTechniqueHit(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.TechniqueHit) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getSpiritHit() {
		int result = getItem().getSpecialPierce(eKind.SPIRIT);
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getSpiritHit(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.SpiritHit) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getDragonLangHit() {
		int result = getItem().getSpecialPierce(eKind.DRAGON_SPELL);
		int enchantLevel = getEnchantLevel();

		/*
		 * 섬멸자의 체인소드, 살기의 키링크
		 */
		if (getItemId() == 203017 || getItemId() == 618) {
			result += enchantLevel > 6 ? enchantLevel - 6 : 0;
			if (result > 4) {
				result = 4;
			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getDragonLangHit(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.DragonlangHit) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getFearHit() {
		int result = getItem().getSpecialPierce(eKind.FEAR);
		int enchantLevel = getEnchantLevel();

		if (getItem().getItemId() == 203006 || getItem().getItemId() == 616) {
			result += enchantLevel > 7 ? enchantLevel - 7 : 0;
		} else if (getItem().getItemId() == 547) {
			result += enchantLevel > 1 ? enchantLevel - 1 : 0;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getFearHit(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.FearHit) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getAllHit() {
		int result = getItem().getSpecialPierce(eKind.ALL);
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getAllHit(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.AllHit) {
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
			result += enchantLevel > 6 ? enchantLevel - 6 : 0;
		}

		if (getItem().getItemId() >= 22370 && getItem().getItemId() <= 22372) {
			result += 5;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getReductionCancel(enchantLevel);

		return result;
	}

	public int getTotalER() {
		int result = 0;
		int enchantLevel = getEnchantLevel();

		if (getItemId() >= 900234 && getItemId() <= 900236) {
			if (enchantLevel >= 6 && enchantLevel <= 8)
				result += 2 + (enchantLevel - 6) * 2;
		}
		if (getItemId() >= 900275 && getItemId() <= 900277) {
			if (enchantLevel >= 5 && enchantLevel <= 8)
				result += 1 + (enchantLevel - 5) * 2;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getTotalER(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Er) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getPotionRecoveryRate() {
		int result = 0;
		int enchantLevel = getEnchantLevel();

		if (getItem().getItemId() == 900021 || getItemId() == 900099) {
			result += (enchantLevel + 1) * 2;
		} else if ((getItem().getItemId() >= 900049 && getItem().getItemId() <= 222352)
				|| (getItem().getItemId() >= 900093 && getItem().getItemId() <= 900098)) {
			result += enchantLevel > 4 ? 8 + (enchantLevel - 4) : enchantLevel * 2;
			if ((getItemId() >= 900096 && getItemId() <= 900098) && enchantLevel > 2) {
				result += 2;
				if (enchantLevel > 4) {
					result += enchantLevel - 4;
				}
			}
		} else if (getItem().getItemId() == 22230 || getItem().getItemId() == 222338) {
			result += enchantLevel > 1 ? 6 + ((enchantLevel - 1) * 2) : 2;
			if (getBless() % 128 == 0 && enchantLevel > 2) {
				result += 2;
			}
			if(enchantLevel == 8) {
				result += 2;
				if(getBless() % 128 == 0) {
					result += 1;
				}
			}
			if(enchantLevel == 9) {
				result += 3;
				if(getBless() % 128 == 0) {
					result += 3;
				}
			}
		} else if ((getItemId() >= 900127 && getItemId() <= 900129)
				|| (getItemId() >= 900124 && getItemId() <= 900126)) {
			if (enchantLevel >= 1 && enchantLevel <= 2)
				result += enchantLevel * 2;
			else if (enchantLevel == 3)
				result += 8;
			else if (enchantLevel >= 4)
				result += 2 + (enchantLevel * 2);
		}

		if (getItem().getType2() == 2 && getItem().getGrade() >= 0
				&& !(getItem().getGrade() >= 3 && getItem().getGrade() <= 4)
				&& (getItem().getType() == 8 || getItem().getType() == 12)) {

			if (getItem().getType() == 8) {
				if(enchantLevel >= 9){
					result = enchantLevel == 10 ? 11 : 10;
				} else {
					result += enchantLevel > 4 ? (enchantLevel - 4) * 2 + 1 : 0;
					if (result > 10)
						result = 10;
				}
			} else {
				if (enchantLevel >= 9)
					result -= enchantLevel == 10 ? 2 : 1;
				result += enchantLevel > 4 ? (enchantLevel - 4) * 2 : 0;

			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getPotionRecoveryRate(enchantLevel);

		return result;
	}

	public int getPotionRecoveryRateValue() {
		int result = 0;
		int enchantLevel = getEnchantLevel();

		if (getItem().getItemId() == 900021 || getItemId() == 900099) {
			result += (enchantLevel + 1) * 2;
		} else if ((getItemId() >= 900127 && getItemId() <= 900129)
				|| (getItemId() >= 900124 && getItemId() <= 900126)) {
			if (enchantLevel >= 1 && enchantLevel <= 2)
				result += enchantLevel * 2;
			else if (enchantLevel == 3)
				result += 8;
			else if (enchantLevel >= 4)
				result += 2 + (enchantLevel * 2);
		} else if ((getItemId() >= 900096 && getItemId() <= 900098) && enchantLevel > 2) {
			if (enchantLevel >= 1 && enchantLevel <= 2)
				result += enchantLevel * 2;
			else if (enchantLevel == 3)
				result += 8;
			else if (enchantLevel >= 4)
				result += 2 + (enchantLevel * 2);
		} else if (getItem().getItemId() == 22230 || getItem().getItemId() == 222338) {
			result += enchantLevel > 1 ? 6 + ((enchantLevel - 1) * 2) : 2;
			if (getBless() % 128 == 0 && enchantLevel > 2) {
				result += 2;
			}
			if(enchantLevel == 8) {
				result += 2;
				if(getBless() % 128 == 0) {
					result += 1;
				}
			}
			if(enchantLevel == 9) {
				result += 3;
				if(getBless() % 128 == 0) {
					result += 3;
				}
			}
		} else if ((getItemId() >= 900127 && getItemId() <= 900129)
				|| (getItemId() >= 900124 && getItemId() <= 900126)) {
			if (enchantLevel >= 1 && enchantLevel <= 2)
				result += enchantLevel * 2;
			else if (enchantLevel == 3)
				result += 8;
			else if (enchantLevel >= 4)
				result += 2 + (enchantLevel * 2);
		}

		if (getItem().getType2() == 2 && getItem().getGrade() >= 0
				&& !(getItem().getGrade() >= 3 && getItem().getGrade() <= 4)
				&& (getItem().getType() == 8 || getItem().getType() == 12)) {

			if (getItem().getType() == 8) {
				if (enchantLevel >= 9)
					result += enchantLevel ==10 ? ((enchantLevel - 5) * 2) - 1: ((enchantLevel - 5) * 2);
				else
					result += enchantLevel > 5 ? (enchantLevel - 5) * 2 + 1 : 0;
			} else {
				if (enchantLevel >= 9)
					result -= enchantLevel == 10 ? 2 : 1;
				result += enchantLevel > 5 ? (enchantLevel - 5) * 2 : 0;
			}
		}
		/*
		 * if (enchantLevel >= 9) result -= 1; result += enchantLevel > 5 ?
		 * (enchantLevel - 5) * 2 : 0; } if (getItem().getType() == 8) { if
		 * (enchantLevel >= 9) result -= 1; result += enchantLevel > 5 ? (enchantLevel -
		 * 5) * 2 + 1 : 0; }
		 */

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getPotionRecoveryRate(enchantLevel);

		return result;
	}

	public int getPotionRecoveryCancel() {
		int result = 0;
		int enchantLevel = getEnchantLevel();

		if (getItem().getItemId() == 900021 || getItemId() == 900099) {
			result += (enchantLevel + 1) * 2;
		} else if ((getItem().getItemId() >= 900049 && getItem().getItemId() <= 900051)
				|| (getItem().getItemId() >= 900093 && getItem().getItemId() <= 900098)) {
			result += enchantLevel > 4 ? 8 + (enchantLevel - 4) : enchantLevel * 2;
			if ((getItemId() >= 900096 && getItemId() <= 900098) && enchantLevel > 2) {
				result += 2;
				if (enchantLevel > 4) {
					result += enchantLevel - 4;
				}
			}
		} else if (getItem().getItemId() == 22230 || getItem().getItemId() == 222338) {
			result += enchantLevel > 1 ? 6 + ((enchantLevel - 1) * 2) : 2;
			if (getBless() % 128 == 0 && enchantLevel > 2) {
				result += 2;
			}
			if(enchantLevel == 8) {
				result += 2;
				if(getBless() % 128 == 0) {
					result += 1;
				}
			}
			if(enchantLevel == 9) {
				result += 3;
				if(getBless() % 128 == 0) {
					result += 3;
				}
			}
		} else if ((getItemId() >= 900127 && getItemId() <= 900129)
				|| (getItemId() >= 900124 && getItemId() <= 900126)) {
			if (enchantLevel >= 1 && enchantLevel <= 2)
				result += enchantLevel * 2;
			else if (enchantLevel == 3)
				result += 8;
			else if (enchantLevel >= 4)
				result += 2 + (enchantLevel * 2);
		}

		if (getItem().getType2() == 2 && getItem().getGrade() >= 0
				&& !(getItem().getGrade() >= 3 && getItem().getGrade() <= 4)
				&& (getItem().getType() == 8 || getItem().getType() == 12)) {
			if (enchantLevel >= 9)
				result -= 1;

			if (getItem().getType() == 8) {
				if (enchantLevel >= 9) {
					result += enchantLevel == 10 ? 12 : 11;
				} else {
					result += enchantLevel > 4 ? (enchantLevel - 4) * 2 + 1 : 0;
				}
			} else {
				result += enchantLevel > 4 ? (enchantLevel - 4) * 2 : 0;
				if (enchantLevel >= 10)
					result -= 1;
			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getPotionRecoveryRateCancel(enchantLevel);

		return result;
	}

	private String getMagicName() {
		String name = null;
		int enchantLevel = getEnchantLevel();

		MJItemSkillModel atk = MJItemSkillModelLoader.getInstance().getAtk(getItem().getItemId());
		MJItemSkillModel def = MJItemSkillModelLoader.getInstance().getDef(getItem().getItemId());
		if(atk != null && atk.condition != 0) {
			if(atk.condition <= enchantLevel) {
				name = getItem().getMagicName();
			}
		} else if (def != null && def.condition != 0) {
			if(def.condition <= enchantLevel) {
				name = getItem().getMagicName();
			}
		} else {
			name = getItem().getMagicName();
		}
//		if (getItem().getItemId() == 203006 || getItem().getItemId() == 616 || getItem().getItemId() == 1136
//				|| getItem().getItemId() == 622 || getItem().getItemId() == 203017 || getItem().getItemId() == 618
//				|| getItem().getItemId() == 505010 || getItem().getItemId() == 505012
//				|| getItem().getItemId() >= 7000244 && getItem().getItemId() <= 7000251) {
//			if (enchantLevel > 9) {
//				name = getItem().getMagicName();
//			}
//		} else {
//			name = getItem().getMagicName();
//		}

		return name;
	}

	public boolean isDrainHp() {
		if (getItem().getItemId() == 12)
			return true;
		if (getItem().getItemId() == 601)
			return true;
		if (getItem().getItemId() == 1123)
			return true;

		return false;
	}

	public boolean isDrainMp() {
		if (getItem().getItemId() == 126)
			return true;
		if (getItem().getItemId() == 127)
			return true;

		return false;
	}

	public boolean isHasteItem() {
		boolean flag = getItem().isHasteItem();

		return flag;
	}

	public int getStr() {
		int result = getItem().get_addstr();
		int enchantLevel = getEnchantLevel();

		if ((getItemId() == 22383)) {
			if (_cha != null) {
				if (_cha.isCrown() || _cha.isKnight() || _cha.isDarkelf() || _cha.isDragonknight() || _cha.is전사() || _cha.isFencer() || _cha.isLancer()) {
					result += 2;
				}
			}
		}

		if ((getItemId() == 900282 || getItemId() == 120178)) {
			if (_cha != null) {
				if (_cha.isCrown() || _cha.isKnight() || _cha.isDarkelf() || _cha.isDragonknight() || _cha.is전사() || _cha.isFencer() || _cha.isLancer()) {
					result += 2;
				}
			}
		}


		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getStr(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Str) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getCon() {
		int result = getItem().get_addcon();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getCon(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Con) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getDex() {
		int result = getItem().get_adddex();
		int enchantLevel = getEnchantLevel();

		if ((getItemId() == 22383)) {
			if (_cha != null) {
				if (_cha.isElf()) {
					result += 2;
				}
			}
		}

		if ((getItemId() == 900282|| getItemId() == 120178)) {
			if (_cha != null) {
				if (_cha.isElf()) {
					result += 2;
				}
			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getDex(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Dex) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getWis() {
		int result = getItem().get_addwis();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getWis(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Wis) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getInt() {
		int result = getItem().get_addint();
		int enchantLevel = getEnchantLevel();

		if ((getItemId() == 22383)) {
			if (_cha != null) {
				if (_cha.isWizard() || _cha.isBlackwizard()) {
					result += 2;
				}
			}
		}

		if ((getItemId() == 900282 || getItemId() == 120178)) {
			if (_cha != null) {
				if (_cha.isWizard() || _cha.isBlackwizard()) {
					result += 2;
				}
			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getInt(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Int) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getCha() {
		int result = getItem().get_addcha();
		int enchantLevel = getEnchantLevel();
		if (getItem().getItemId() == 900076 || getItem().getItemId() == 20016 || getItem().getItemId() == 20112
				|| getItem().getItemId() == 120016 || getItem().getItemId() == 120112) {
			result += enchantLevel > 6 ? enchantLevel - 6 : 0;
		} else if (getItem().getItemId() == 118) {
			result += enchantLevel > 8 ? enchantLevel - 8 : 0;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getCha(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Cha) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public boolean isResistPoison() {
		return getItemId() == 20298 || getItemId() == 20117 || getItemId() == 900263 || getItemId() == 900264 || getItemId() == 900265 || (getItemId() >= 22196 && getItemId() <= 22199);
	}

	public int getAttrDmg() {
		int result = 0;

		if (getItemId() == 3000516) {
			result += 3;
		}

		return result;
	}

	public int getDmgModifier() {
		int result = getItem().getDmgRate(); // 방어구에 포함된 근거리
		int enchantLevel = getEnchantLevel();

		if (getItem().getType2() == 1 && (getItem().getType() != 4 && getItem().getType() != 10 && getItem().getType() != 13)) {
			result += getItem().getDmgModifier(); // 무기에 포함된 근거리
		}
		if (getItemId() == 900234 || getItemId() == 900275) {
			if (enchantLevel >= 4 && enchantLevel <= 8)
				result += 1 + (enchantLevel - 4) * 1;
		}
		if ((getItemId() >= 22224 && getItemId() <= 22228) || getItemId() == 900195 || (getItemId() >= 222330 && getItemId() <= 222334) || getItemId() == 222291 || getItemId() == 222336) { //// 악세 20201118
			if (getBless() % 128 == 0)
				enchantLevel++;

			result += (enchantLevel <= 4 ? 0 : enchantLevel >= 9 && getBless() % 128 == 0 || enchantLevel >= 8 && getBless() % 128 == 1 ? enchantLevel - 3 + ((enchantLevel - 8) * 1) : enchantLevel - 4);
		} else if ((getItemId() == 222340 || getItemId() == 222341) && enchantLevel > 2) {
			if (getBless() % 128 == 0)
				enchantLevel++;
			result += (enchantLevel <= 4 ? 1 : enchantLevel <= 2 ? 0 : enchantLevel - 3);
			if (enchantLevel >= 8) {
				result += enchantLevel - 8 + 2;
			}
			if (enchantLevel == 10 && getBless() % 128 == 0) {
				result += 1;
			}
		} else if (getItemId() == 900152 || getItemId() == 900081) {
			result += enchantLevel > 4 ? enchantLevel - 4 : 0;
		} else if (getItemId() == 900030 || getItemId() == 900226) {
			result += enchantLevel > 8 ? 1 : 0;
		} else if (getItemId() == 900049 || getItemId() == 900093 || getItemId() == 900096) {
			result += enchantLevel > 5 ? enchantLevel - 5 : 0;
		} else if (getItemId() == 900026 || getItemId() == 900185) {
			result += enchantLevel > 8 ? 1 : 0;
		} else if (getItemId() == 22003) {
			enchantLevel = enchantLevel > 9 ? 9 : enchantLevel;
			result += (enchantLevel < 5 ? 0 : (enchantLevel - 3) / 2);
		} else if (getItemId() == 900117 || getItemId() == 900219) {
			result += enchantLevel > 6 ? enchantLevel - 6 : 0;
		} else if (getItemId() == 900124) {
			result += enchantLevel > 4 ? enchantLevel - 4 : 0;
		} else if (getItemId() == 900127) {
			if (enchantLevel >= 5) {
				result += enchantLevel - 4;
			}
		}

		if (getItem().getType2() == 2) {
			if (getItem().getGrade() >= 0 && !(getItem().getGrade() >= 3 && getItem().getGrade() <= 4) && (getItem().getType() == 9 || getItem().getType() == 11)) {
				result += (enchantLevel <= 4 ? 0 : enchantLevel - 4);
			}
		}

		if(getItemId() == 22384) {
			if (_cha != null) {
				if(!_cha.isWizard() && !_cha.isBlackwizard() && !_cha.isElf()) {
					result += 2;
				}
			}
		}

		if (getItem().getType2() == 2) {
			L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
			if (eb != null) {
				result += eb.getDmgModifier(enchantLevel);
			}
		}

		if (getBlessType() == L1BlessTypeEnchant.Dmg) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getHitModifier() {
		int result = getItem().getHitRate();
		int enchantLevel = getEnchantLevel();

		if (getItem().getType2() == 1
				&& (getItem().getType() != 4 && getItem().getType() != 10 && getItem().getType() != 13)) {
			result += getItem().getHitModifier();
		}
		if (getItemId() == 222291 || getItemId() == 222336) {
			if (getBless() % 128 == 0)
				enchantLevel++;
			result += (enchantLevel <= 4 ? 0 : enchantLevel >= 9 && getBless() % 128 == 0 || enchantLevel >= 8 && getBless() % 128 == 1 ? enchantLevel - 3 + ((enchantLevel - 8) * 1) : enchantLevel - 4);
		} else if (getItemId() == 22229 || getItemId() == 222337) {
			if (getBless() % 128 == 0 && enchantLevel >= 6) {
				result += 1 + ((enchantLevel - 6) * 2);
			} else if (getBless() % 128 != 0 && getEnchantLevel() >= 7) {
				result += 1 + ((enchantLevel - 7) * 2);
			}
			// TODO 從7附魔開始給予效果，9以上只持續應用6的效果
		} else if (getItemId() == 222317) {
			if (enchantLevel == 7)
				result += 4;
			else if (enchantLevel == 8)
				result += 5;
			else if (enchantLevel >= 9)
				result += 6;
		} else if (getItemId() == 22373) {
			enchantLevel = enchantLevel > 9 ? 9 : enchantLevel;
			result += enchantLevel > 5 ? enchantLevel - 4 : 0;
		} else if (getItemId() == 900049 || getItemId() == 900093 || getItemId() == 900096) {
			result += enchantLevel == 5 ? 1 : enchantLevel > 5 ? enchantLevel - 5 : 0;
		} else if (getItemId() == 900154 || getItemId() == 900117 || getItemId() == 900219 || getItemId() == 900083) {
			result += enchantLevel > 4 ? enchantLevel - 4 : 0;
		} else if (getItemId() == 900124) {
			result += enchantLevel > 3 ? enchantLevel - 3 : 0;
			/*
			 * if (getItemId() == 900127 && enchantLevel > 4) result--;
			 */
		} else if (getItemId() == 900026 || getItemId() == 900185) {
			if (enchantLevel >= 7) {
				result += enchantLevel > 7 ? (enchantLevel - 7) * 2 : 0;
				if (getBless() % 128 == 0) {
					result++;
				}
			}
		} else if (getItemId() == 900127) {
			if (enchantLevel >= 4 && enchantLevel <= 5) {
				result += 1;
			} else if (enchantLevel >= 6) {
				result += 1 + enchantLevel - 5;
			}
		} else if (getItemId() == 900081) {
			if (enchantLevel >= 6) {
				result += 1 + (enchantLevel - 6) * 2;
			}
		}

		if((getItemId() == 22383 || getItemId() == 22384)) {
			if (_cha != null) {
				if(!_cha.isElf() && !_cha.isWizard() && !_cha.isBlackwizard()) {
					result += 2;
				}
			}
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getHitModifier(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Hit) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getBowHitModifier() {
		int result = getItem().getBowHitRate();
		int enchantLevel = getEnchantLevel();

		if (getItem().getType2() == 1
				&& (getItem().getType() == 4 || getItem().getType() == 10 || getItem().getType() == 13)) {
			result += getItem().getHitModifier();
		}

		if (getItemId() == 222291 || getItemId() == 222336) {
			if (getBless() % 128 == 0)
				enchantLevel++;
			result += (enchantLevel <= 4 ? 0 : enchantLevel >= 9 && getBless() % 128 == 0 || enchantLevel >= 8 && getBless() % 128 == 1 ? enchantLevel - 3 + ((enchantLevel - 8) * 1) : enchantLevel - 4);
		} else if (getItemId() == 22229 || getItemId() == 222337) {
			if (getBless() % 128 == 0 && enchantLevel >= 6) {
				result += 1 + ((enchantLevel - 6) * 2);
			} else if (getBless() % 128 != 0 && getEnchantLevel() >= 7) {
				result += 1 + ((enchantLevel - 7) * 2);
			}
		} else if (getItemId() == 900094 || getItemId() == 900097) {
			result += enchantLevel == 5 ? 1 : enchantLevel > 5 ? enchantLevel - 5 : 0;
		} else if (getItemId() == 3000516 || getItemId() == 410012) {
			result += 3;
		} else if (getItemId() == 4100041) {
			result += 5;
		} else if (getItemId() == 900118 || getItemId() == 900220) {
			result += enchantLevel > 4 ? enchantLevel - 4 : 0;
		} else if (getItemId() == 900082) {
			result += enchantLevel > 5 ? enchantLevel - 5 : 0;
			if (enchantLevel >= 7)
				result++;
			if (enchantLevel >= 8)
				result++;
		} else if (getItemId() == 900128 || getItemId() == 900125) {
			result += enchantLevel > 3 ? enchantLevel - 3 : 0;
			if (getItemId() == 900128 && enchantLevel > 4)
				result--;
		} else if (getItemId() == 900027 || getItemId() == 900186) {
			if (enchantLevel >= 7) {
				result += enchantLevel > 7 ? (enchantLevel - 7) * 2 : 0;
				if (getBless() % 128 == 0) {
					result++;
				}
			}
		}

		if((getItemId() == 22383 || getItemId() == 22384)) {
			if (_cha != null) {
				if(_cha.isElf()) {
					result += 2;
				}
			}
		}

		/**
		 * 화살류 표기
		 */
		if (getItem().getType2() == 0 && (getItem().getType() == 0 || getItem().getType() == 15)) {
			result = getItem().getHitModifier();
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getBowHitModifier(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.BowHit) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getBowDmgModifier() {
		int result = getItem().getBowDmgRate();
		int enchantLevel = getEnchantLevel();

		if (getItem().getType2() == 1 && (getItem().getType() == 4 || getItem().getType() == 10 || getItem().getType() == 13)) {
			result += getItem().getDmgModifier(); // 무기에 포함된 원거리
		}

		if (getItem().getType2() == 2) {
			if (getItem().getGrade() >= 0 && !(getItem().getGrade() >= 3 && getItem().getGrade() <= 4)
					&& (getItem().getType() == 9 || getItem().getType() == 11)) {
				result += (enchantLevel <= 4 ? 0 : enchantLevel - 4);
			}
		}

		if ((getItemId() >= 22224 && getItemId() <= 22228) || getItemId() == 900195
				|| (getItemId() >= 222330 && getItemId() <= 222334) || getItemId() == 222291 || getItemId() == 222336) {
			if (getBless() % 128 == 0)
				enchantLevel++;
			result += (enchantLevel <= 4 ? 0 : enchantLevel >= 9 && getBless() % 128 == 0 || enchantLevel >= 8 && getBless() % 128 == 1 ? enchantLevel - 3 + ((enchantLevel - 8) * 1) : enchantLevel - 4);
		} else if ((getItemId() == 222340 || getItemId() == 222341) && enchantLevel > 2) {
			if (getBless() % 128 == 0)
				enchantLevel++;
			result += (enchantLevel <= 4 ? 1 : enchantLevel <= 2 ? 0 : enchantLevel - 3);
			if(enchantLevel >= 8){
				result += enchantLevel - 8 + 2;
			}
			if(enchantLevel == 10 && getBless() % 128 == 0){
				result += 1;
			}
		} else if (getItemId() == 22000) {
			enchantLevel = enchantLevel > 9 ? 9 : enchantLevel;
			result += (enchantLevel < 5 ? 0 : (enchantLevel - 3) / 2);
		} else if (getItemId() == 900050 || getItemId() == 900094 || getItemId() == 900097) {
			result += enchantLevel > 5 ? enchantLevel - 5 : 0;
		} else if (getItemId() == 900027 || getItemId() == 900186) {
			result += enchantLevel > 8 ? 1 : 0;
		} else if (getItemId() == 900031 || getItemId() == 900227) {
			result += enchantLevel > 8 ? 1 : 0;
		} else if (getItemId() == 900153 || getItemId() == 900082) {
			result += enchantLevel > 4 ? enchantLevel - 4 : 0;
		} else if (getItemId() == 410012 || getItemId() == 4100041) {
			result += 3;
		} else if (getItemId() == 900118 || getItemId() == 900220) {
			result += enchantLevel > 6 ? enchantLevel - 6 : 0;
		} else if (getItemId() == 900128 || getItemId() == 900125) {
			result += enchantLevel > 4 ? enchantLevel - 4 : 0;
		}
		if (getItemId() == 900235 || getItemId() == 900276) {
			if (enchantLevel >= 4 && enchantLevel <= 8)
				result += 1 + (enchantLevel - 4) * 1;
		}

		/**
		 * 화살류 표기
		 */
		if (getItem().getType2() == 0 && (getItem().getType() == 0 || getItem().getType() == 15)) {
			result = getItem().getDmgModifier();
		}

		if(getItemId() == 22384) {
			if (_cha != null) {
				if(_cha.isElf()) {
					result += 2;
				}
			}
		}

		if (getItem().getType2() == 2) {
			L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
			if (eb != null) {
				result += eb.getBowDmgModifier(enchantLevel);
			}
		}

		if (getBlessType() == L1BlessTypeEnchant.BowDmg) {
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
			critical += enchant == 6 ? 1 : enchant == 7 ? 3 : enchant >= 8 ? 5 : 0;
		/*} else if (itemid == 202014) {
			critical += enchant;*/
		} else if (itemid >= 22208 && itemid <= 22209) {
			if (enchant == 7)
				critical += 1;
			else if (enchant == 8)
				critical += 2;
			else if (enchant >= 9)
				critical += 3;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			critical += eb.getShortCritical(enchant);

		return critical;
	}

	public int getLongCriticalValue() {
		int itemid = getItem().getItemId();
		int enchant = getEnchantLevel();

		int critical = getItem().get_missile_critical_probability();

		if (itemid == 900153 || itemid == 900082) {
			critical += enchant == 6 ? 1 : enchant == 7 ? 3 : enchant >= 8 ? 5 : 0;
		} else if (itemid == 22210) {
			if (enchant == 7)
				critical += 1;
			else if (enchant == 8)
				critical += 2;
			else if (enchant >= 9)
				critical += 3;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			critical += eb.getLongCritical(enchant);

		return critical;
	}

	public int getMagicCriticalValue() {
		int itemid = getItem().getItemId();
		int enchant = getEnchantLevel();

		int critical = getItem().get_magic_critical_probability();

		if (itemid == 900154 || itemid == 900083) {
			critical += enchant == 6 ? 1 : enchant == 7 ? 2 : enchant >= 8 ? 4 : 0;
		} else if (itemid == 325) {
			critical += enchant > 6 ? enchant - 6 : 0;
		} else if (itemid == 22211) {
			if (enchant == 7)
				critical += 1;
			else if (enchant == 8)
				critical += 2;
			else if (enchant >= 9)
				critical += 3;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			critical += eb.getMagicCritical(enchant);

		return critical;
	}

	public int getAc() {
		int result = _item.get_ac();

		return result;
	}

	public int getTitanPercent() {
		int result = getItem().getTitanPercent();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getTitanRate(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.TitanPercent) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getFoeDmg() {
		int result = 0;
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getFoeDmg(enchantLevel);

		return result;
	}

	public int getDefenseFire() {
		int result = getItem().get_defense_fire();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getDefenseFire(enchantLevel);

		return result;
	}

	public int getDefenseWater() {
		int result = getItem().get_defense_water();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getDefenseWater(enchantLevel);

		return result;
	}

	public int getDefenseWind() {
		int result = getItem().get_defense_wind();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getDefenseWind(enchantLevel);

		return result;
	}

	public int getDefenseEarth() {
		int result = getItem().get_defense_earth();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getDefenseEarth(enchantLevel);

		return result;
	}

	public int getDefenseAll() {
		int result = getItem().get_defense_all();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getDefenseAll(enchantLevel);

		return result;
	}

	public int getDamagePlusRate() {
		int result = 0;
		int enchant = getEnchantLevel();

		if (getItem().getItemId() == 222340 || getItem().getItemId() == 222341) {
			if (getBless() % 128 == 0) {
				result += enchant > 3 ? enchant - 2 : 0;
			} else {
				result += enchant > 4 ? enchant - 3 : 0;
			}
			if(enchant >= 9) {
				result += 1;
			}
		}

		return result;
	}

	public int getDamagePlusValue() {
		int result = 0;
		int enchant = getEnchantLevel();

		if (getItem().getItemId() == 222340 || getItem().getItemId() == 222341) {
			result += 20;
			if(enchant >=8) {
				result += 5;
				if(getBless() % 128 == 0) {
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
				result += enchantLevel > 5 ? 1 + ((enchantLevel - 6) * 2) : 0;
			} else {
				result += enchantLevel > 6 ? 1 + ((enchantLevel - 7) * 2) : 0;
			}
			if(enchantLevel >= 8) {
				if (getBless() % 128 == 0) {
					if(enchantLevel == 8) {
						result += 1;
					}
				}
				result += enchantLevel - 8 + 1;
			}
		}
		if (getItemId() >= 900234 && getItemId() <= 900236) {
			if (enchantLevel >= 6 && enchantLevel <= 8)
				result += 2 + (enchantLevel - 6) * 2;
		}
		if (getItemId() >= 900275 && getItemId() <= 900277) {
			if (enchantLevel >= 5 && enchantLevel <= 8)
				result += 1 + (enchantLevel - 5) * 2;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getMagicDodge(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.MagicDodge) {
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

		/**
		 * 집행급 무기 1인챈에 대미지 2씩 올라가는 무기류
		 */
		if (itemId == 66 || itemId == 61 || itemId == 12 || itemId == 134 || itemId == 86 || itemId == 7000258 || itemId == 7000238 || (itemId >= 202011 && itemId <= 202015 || itemId == 7000265)) {
			result += enchant;
		} else if (itemId == 7000239 || itemId == 7000240 || itemId == 203041 || itemId == 203042 || itemId == 203065 || itemId == 7000264 || itemId == 7000262 || itemId == 7000263 || itemId == 7000267) {
			result *= 5;
		} else {
			if (enchant > 9) {
				result += enchant - 9;
			}
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
		// int itemId = getItem().getItemId();

/*		if ((getItem().getType2() == 2 && getItem().getType() == 28) || (getItem().getType2() == 2 && getItem().getType() == 30)) {
			result += enchant > 4 ? (enchant - 4) * 5 : 0;
			if (enchant > 8)
				result = 20;
		}*/

		if (getBlessType() == L1BlessTypeEnchant.AinEfficiency) {
			result += getBlessTypeValue();
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getEinhasadEfficiency(enchant);


//		System.out.println(getItemId()+", "+result);
		return result;
	}

	public int getAbnormalStatusPvpDamageReduction() {
		int result = 0;
		int enchant = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null) {
			result += eb.getAbnormalStatusPvpDamageReduction(enchant);
		}

//		System.out.println("스턴 데미지 감소"+result+"-"+getName());
		return result;
	}


	public int getMpAr16() {//Mp절대회복 16초
		int result = getItem().getMpAr16();

		return result;
	}

	public int getPVPDmgReducIgnore() {
		int result = getItem().getPVPWeaponReductionCancel();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getPVPDmgReducIgnore(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.PvpReducIgnore) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getPVPMdmgReduction() {
		int result = getItem().getPVPMagicReduction();
		int enchantLevel = getEnchantLevel();
		int itemId = getItem().getItemId();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getPvpMdmgReduction(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.PVPMdmgReduction) {
			result += getBlessTypeValue();
		}

		if (itemId >= 900124 && itemId <= 900126) {
			if (enchantLevel == 5)
				result += 1;
			if (enchantLevel == 6)
				result += 2;
			if (enchantLevel == 7)
				result += 3;
			if (enchantLevel >= 8)
				result += 5;
		}

		return result;
	}

	public int getPVPMDmgReducIgnore() {
		int result = getItem().getPVPMagicReductionCancel();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getPvpMdmgReducIgnore(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.PVPMdmgReducIgnore) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getDG() {
		int result = getItem().getDG();
		int enchantLevel = getEnchantLevel();

		if (getItemId() >= 900234 && getItemId() <= 900236) {
			if (enchantLevel >= 6 && enchantLevel <= 8)
				result += 2 + (enchantLevel - 6) * 2;
		}
		if (getItemId() >= 900275 && getItemId() <= 900277) {
			if (enchantLevel >= 5 && enchantLevel <= 8)
				result += 1 + (enchantLevel - 5) * 2;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getDG(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.Dg) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getHpPercent() {
		int result = getItem().getHpPercent();
		int enchantLevel = getEnchantLevel();

		if (getItemId() == 900234 || getItemId() == 900275) {
			if (enchantLevel >= 0 && enchantLevel <= 6)
				result += 2 + (enchantLevel) * 2;
			else if (enchantLevel == 7)
				result += 17;
			else if (enchantLevel == 8)
				result += 20;
		}
		if (getItemId() == 900235 || getItemId() == 900276) {
			if (enchantLevel >= 0 && enchantLevel <= 2)
				result += 1 + (enchantLevel) * 1;
			else if (enchantLevel >= 3)
				result += 1 + (enchantLevel - 1) * 2;
			else if (enchantLevel == 8)
				result += 20;
		}
		if (getItemId() == 900236 || getItemId() == 900277) {
			if (enchantLevel >= 0 && enchantLevel <= 7)
				result += 1 + (enchantLevel) * 1;
			else if (enchantLevel == 8)
				result += 10;
		}
		if (getItemId() == 900237 || getItemId() == 900278) {
			if (enchantLevel >= 0 && enchantLevel <= 7)
				result += 1 + (enchantLevel) * 1;
			else if (enchantLevel == 8)
				result += 10;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getHpPercent(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.HpPercent) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getMpPercent() {
		int result = getItem().getMpPercent();
		int enchantLevel = getEnchantLevel();

		if (getItemId() == 900234 || getItemId() == 900275) {
			if (enchantLevel >= 0 && enchantLevel <= 7)
				result += 1 + (enchantLevel) * 1;
			else if (enchantLevel == 8)
				result += 10;
		}
		if (getItemId() == 900235 || getItemId() == 900276) {
			if (enchantLevel >= 0 && enchantLevel <= 2)
				result += 1 + (enchantLevel) * 1;
			else if (enchantLevel >= 3)
				result += 1 + (enchantLevel - 1) * 2;
			else if (enchantLevel == 8)
				result += 20;
		}
		if (getItemId() == 900236 || getItemId() == 900277) {
			if (enchantLevel >= 0 && enchantLevel <= 6)
				result += 2 + (enchantLevel) * 2;
			else if (enchantLevel == 7)
				result += 17;
			else if (enchantLevel == 8)
				result += 25;
		}
		if (getItemId() == 900237 || getItemId() == 900278) {
			if (enchantLevel >= 0 && enchantLevel <= 7)
				result += 1 + (enchantLevel) * 1;
			else if (enchantLevel == 8)
				result += 10;
		}

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getMpPercent(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.MpPercent) {
			result += getBlessTypeValue();
		}

		return result;
	}

	public int getImmuneIgnore() {
		int result = getItem().getIIg();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		if (eb != null)
			result += eb.getImmuneIgnore(enchantLevel);

		if (getBlessType() == L1BlessTypeEnchant.ImmuneIgnore) {
			result += getBlessTypeValue();
		}

		return result;
	}
	public int getCCIncrease() {
		int result = getItem().getCCIncrease();
		int enchantLevel = getEnchantLevel();

		L1EnchantBonus eb = L1EnchantBonus.get(getItemId());
		/*if (getItem().getItemId() == 900125 || getItem().getItemId() == 900126 || getItem().getItemId() == 900124) {
			if (getEnchantLevel() == 7) {
				result += 100;
			}
			if (getEnchantLevel() == 8) {
				result += 200;
			}
		}*/
		if (eb != null) {
			result += eb.getCCIncrease(enchantLevel);
		}

		return result;
	}

	public int getAttrLevelPacketNumber() {
		int result = getAttrEnchantBit(getAttrEnchantLevel());

		return result;
	}

	private boolean _isSupportItem;

	public boolean isSupportItem() {
		return _isSupportItem;
	}

	public void setSupportItem(boolean flag) {
		_isSupportItem = flag;
	}

	private int _range;

	public void setRange(int type) {
		_range = type;
	}

	public int getRange() {
		return _range;
	}

	/**
	 * 블레스 타입
	 */
	private int _bless_type;

	public int getBlessType() {
		return _bless_type;
	}

	public void setBlessType(int i) {
		_bless_type = i;
	}

	private int _bless_type_value;

	public int getBlessTypeValue() {
		return _bless_type_value;
	}

	public void setBlessTypeValue(int i) {
		_bless_type_value = i;
	}



	public int _smelting_value;
	public int getSmeltingValue(){
		return _smelting_value;
	}
	public void setSmeltingValue(int itemid){
		_smelting_value = itemid;
	}

	public int _smelting_itemid_1;
	public int getSmeltingItemId1(){
		return _smelting_itemid_1;
	}
	public void setSmeltingItemId1(int itemid){
		_smelting_itemid_1 = itemid;
	}
	public int _smelting_itemid_2;
	public int getSmeltingItemId2(){
		return _smelting_itemid_2;
	}
	public void setSmeltingItemId2(int itemid){
		_smelting_itemid_2 = itemid;
	}
	public int _smelting_kind_1;
	public int getSmeltingKind1(){
		return _smelting_kind_1;
	}
	public void setSmeltingKind1(int kind){
		_smelting_kind_1 = kind;
	}
	public int _smelting_kind_2;
	public int getSmeltingKind2(){
		return _smelting_kind_2;
	}
	public void setSmeltingKind2(int kind){
		_smelting_kind_2 = kind;
	}
	private Timestamp _halpas_time;
	public void setHalpas_Time(Timestamp endtime) {
		_halpas_time = endtime;
	}
	public Timestamp getHalpas_Time() {
		return _halpas_time;
	}

	private int _cant_unseal;

	public void set_Cantunseal(int i) {
		_cant_unseal = i;
	}

	public int get_Cantunseal() {
		return _cant_unseal;
	}

	public int getStatusBit(){
		int b = 0;
		if(this.isIdentified())
			b = 1;
		if(!this.getItem().isTradable()){
			b |= 2;
		}

		if(this.getItem().isCantDelete())
			b |= 4;
		if(this.getItem().get_safeenchant() < 0)
			b |= 8;

		int bless = this.getBless();
		if(bless >= 128 && bless <= 131){
			b |= 2;
			b |= 4;
			b |= 8;
			b |= 32;
		}else if(bless > 131){
			b |= 64;
		}
		if(this.getItem().isStackable())
			b |= 128;

		return b;
	}

}
