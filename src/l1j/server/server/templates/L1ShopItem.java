package l1j.server.server.templates; 

import l1j.server.server.datatables.ItemTable;

public class L1ShopItem { // 團隊 The Day 由 裘德

	private final int _itemId;

	private final L1Item _item;

	private final int _price;

	private final int _packCount;

	private int _enchant;

	private int _attrenchant;

	private int _count;

	private boolean _timeLimit;

	private int _endTime;

	private boolean _carving;

	private int _bless;

	private int _buylevel;

	private int _classType;

	private int _clan_shop_type;

	private int npcid;

	public L1ShopItem(int itemId, int price, int packCount) {
		this(itemId, price, packCount, 0, false, 0, false, 1, 0, 0, 10, 0);
	}

	public L1ShopItem(int itemId, int price, int packCount, int enchant, boolean timeLimit, int minute, boolean carving, int bless,
			int attrenchant, int buylevel, int classType, int clan_shop_type) {
		_itemId = itemId;
		_item = ItemTable.getInstance().getTemplate(itemId);
		_price = price;
		_packCount = packCount;
		_enchant = enchant;
		_attrenchant = attrenchant;
		_count = 1;
		_timeLimit = timeLimit;
		_endTime = minute;
		_carving = carving;
		_bless = bless;
		_buylevel = buylevel;
		_clan_shop_type = clan_shop_type;
		set_classType(classType);
	}

	public L1ShopItem(int itemId, int price, int packCount, int enchant,
			int bless) {
		_itemId = itemId;
		_item = ItemTable.getInstance().getTemplate(itemId);
		_price = price;
		_packCount = packCount;
		_enchant = enchant;
		_count = 1;
		_bless = bless;
	}

	public int get_count() {
		return _count;
	}
	
	public void set_count(int _count) {
		this._count = _count;
	}

	public int getItemId() {
		return _itemId;
	}

	public L1Item getItem() {
		return _item;
	}

	public int getPrice() {
		return _price;
	}
	
	public int getPackCount() {
		return _packCount;
	}
	
	public int getEnchant() {
		return _enchant;
	}

	public void setEnchant(int i) {
		_enchant = i;
	}

	public int getCount() {
		return _count;
	}

	public void setCount(int i) {
		_count = i;
	}

	public boolean isTimeLimit() {
		return _timeLimit;
	}

	public void setTimeLimit(boolean timeLimit) {
		_timeLimit = timeLimit;
	}

	public int getEndTime() {
		return _endTime;
	}

	public void setEndTime(int t) {
		_endTime = t;
	}

	public boolean isCarving() {
		return _carving;
	}

	public void setCarving(boolean flag) {
		_carving = flag;
	}

	public int getBless() {
		return _bless;
	}

	public void setBless(int flag) {
		_bless = flag;
	}

	public int getAttrEnchant() {
		return _attrenchant;
	}

	public void setAttrEnchant(int i) {
		_attrenchant = i;
	}

	public int getBuyLevel() {
		return _buylevel;
	}

	public void setBuyLevel(int i) {
		_buylevel = i;
	}

	public void setNpcId(int i) {
		npcid = i;
	}

	public int getNpcId() {
		return npcid;
	}

	public int get_classType() {
		return _classType;
	}

	public void set_classType(int _classType) {
		this._classType = _classType;
	}

	public int getClanShopType() {
		return _clan_shop_type;
	}

	public void setClanShopType(int i) {
		_clan_shop_type = i;
	}	
	}