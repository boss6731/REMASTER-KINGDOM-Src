package l1j.server.server.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.Config;
import l1j.server.MJINNSystem.MJINNHelper;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.FindInventory.InventoryFindItemFilter;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ALCHEMY_MAKE_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_MAKE_REQ.CraftInputItemSlotInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_SMELTING_MAKE_REQ;
import l1j.server.server.GameClient;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.Warehouse;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
import l1j.server.server.serverpackets.S_EquipmentWindow;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class L1Inventory extends L1Object {
	private static final long serialVersionUID = 1L;
	protected List<L1ItemInstance> _items = new CopyOnWriteArrayList<L1ItemInstance>();
	public static final int MAX_AMOUNT = 2000000000; // 2G
	public static final int MAX_WEIGHT = 1500;
	public static final int OK = 0;
	public static final int SIZE_OVER = 1;
	public static final int WEIGHT_OVER = 2;
	public static final int AMOUNT_OVER = 3;
	public static final int WAREHOUSE_TYPE_PERSONAL = 0;
	public static final int WAREHOUSE_TYPE_CLAN = 1;

	public Map<Integer, Integer> ring_slot = new ConcurrentHashMap<Integer, Integer>();
	public Map<Integer, Integer> earring_slot = new ConcurrentHashMap<Integer, Integer>();

	public int[] slot_ring = new int[4];
	public int[] slot_rune = new int[3];
	public int[] slot_earring = new int[4];
	public int[] slot_blut = new int[2];

	public L1Inventory() {
		for (int i = 0; i < slot_ring.length; ++i)
			slot_ring[i] = 0;
		for (int i = 0; i < slot_rune.length; ++i)
			slot_rune[i] = 0;
		for (int i = 0; i < slot_earring.length; i++)
			slot_earring[i] = 0;
		for (int i = 0; i < slot_blut.length; i++)
			slot_blut[i] = 0;
	}

	public int getTypeAndItemIdEquipped(int type2, int type, int ItemId) {
		int equipeCount = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getType2() == type2 // 0 etc아템 1 무기 2아머
					&& item.getItem().getType() == type // type2 의 세부항목들
					&& item.getItem().getItemId() == ItemId// 아이템의 아이디
					&& item.isEquipped()) { // 착용
				equipeCount++;
			}
		}
		return equipeCount;
	}

	public int getTypeEquipped(int type, int type2) {
		int equipeCount = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getType2() == type // 0 etc아템 1 무기 2아머
					&& item.getItem().getType() == type2 // type2 의 세부항목들
					&& item.isEquipped()) { // 착용
				equipeCount++;
			}
		}
		return equipeCount;
	}

	public int getTypeAndGradeEquipped(int type2, int type, int Grade) {
		int equipeCount = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getType2() == type2 // 0 etc아템 1 무기 2아머
					&& item.getItem().getType() == type // type2 의 세부항목들
					&& item.getItem().getGrade() == Grade && item.isEquipped()) { // 착용
				equipeCount++;
			}
		}
		return equipeCount;
	}

	public void toSlotPacket(L1PcInstance pc, L1ItemInstance item, boolean worldjoin) {
		if (pc.isWorld == false)
			return;

		int select_idx = -1;
		int idx = 0;
		if (item.getItem().getType2() == 2) {
			switch (item.getItem().getType()) {
			case 1:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_HEML;
				break;
			case 2:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_ARMOR;
				break;
			case 3:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_T;
				break;
			case 4:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_CLOAK;
				break;
			case 5:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_GLOVE;
				break;
			case 6:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_BOOTS;
				break;
			case 7:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_SHIELD;
				break;
			case 8:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_NECKLACE;
				break;
			case 9: // ring1
			case 11: // ring2
				if (ring_slot.get(item.getId()) != null) {
					select_idx = ring_slot.get(item.getId());
				} else {
					if (item.isEquipped() && select_idx == -1) {
						if (pc.getQuest().isEnd(L1Quest.QUEST_RING_LEFT_SLOT60) && !pc.getQuest().isEnd(L1Quest.QUEST_RING_RIGHT_SLOT60)) {
							ring_slot.put(item.getId(), 2);
						} else if (pc.getQuest().isEnd(L1Quest.QUEST_RING_RIGHT_SLOT60) && !pc.getQuest().isEnd(L1Quest.QUEST_RING_LEFT_SLOT60)) {
							ring_slot.put(item.getId(), 3);
						} else {
							for (int i = 0; i <= ring_slot.size(); i++) {
								if (!ring_slot.containsValue(i - 1)) {
									ring_slot.put(item.getId(), i - 1);
								}
							}
						}
						select_idx = ring_slot.get(item.getId());
					}
				}

				if (!item.isEquipped() && select_idx != -1) {
					ring_slot.remove(item.getId());
				}

				idx = S_EquipmentWindow.EQUIPMENT_INDEX_RING + select_idx;
				break;
			case 10:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_BELT;
				break;
			case 12:
				if (earring_slot.get(item.getId())!= null) {
					select_idx = earring_slot.get(item.getId());
				} else {
					if (item.isEquipped() && select_idx == -1) {
						for (int i = 0; i<=earring_slot.size(); i++) {
							if (!earring_slot.containsValue(i - 1)) {
								earring_slot.put(item.getId(), i - 1);
							}
						}
						select_idx = earring_slot.get(item.getId());
					}
				}
				if (!item.isEquipped() && select_idx != -1) {
					earring_slot.remove(item.getId());
				}
				
			switch (select_idx){
			case 0:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_EARRING;
				break;
			case 1:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_EARRING1;
				break;
			case 2:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_EARRING2;
				break;
			case 3:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_EARRING3;
				break;
			}
				
				
				
/*				
				for (int i = 0; i < this.slot_earring.length; i++) {
					if (this.slot_earring[i] == item.getId()) {
						select_idx = i;
					}
				}
				if ((item.isEquipped()) && (select_idx == -1)) {
					for (int i = 0; i < this.slot_earring.length; i++) {
						if (this.slot_earring[i] == 0) {
							this.slot_earring[i] = item.getId();
							if (i == 0) {
								idx = S_EquipmentWindow.EQUIPMENT_INDEX_EARRING;
							} else if (i == 1) {
								idx = S_EquipmentWindow.EQUIPMENT_INDEX_EARRING1;
							} else if (i == 2) {
								idx = S_EquipmentWindow.EQUIPMENT_INDEX_EARRING2;
							} else {
								idx = S_EquipmentWindow.EQUIPMENT_INDEX_EARRING3;
							}
							break;
						}
					}
				}

				if ((item.isEquipped()) || (select_idx == -1))
					break;
				this.slot_earring[select_idx] = 0;
				if (select_idx == 0)
					idx = S_EquipmentWindow.EQUIPMENT_INDEX_EARRING;
				else
					idx = S_EquipmentWindow.EQUIPMENT_INDEX_EARRING_2ND;*/
				break;
				case 13: // garder
					idx = S_EquipmentWindow.EQUIPMENT_INDEX_SHIELD;
					// 需要確認護盾應放的插槽編號。
					break;
				case 25: // rune
					// 搜索是否已有裝備中的物品。
					for (int i = 0; i < slot_rune.length; ++i) {
						if (slot_rune[i] == item.getId())
							select_idx = i;
					}
						// 如果需要裝備，只有在沒有已有裝備的情況下更新內存。
					if (item.isEquipped() && select_idx == -1) {
						// 如果正在裝備，放入空插槽。
						for (int i = 0; i < slot_rune.length; ++i) {
							if (slot_rune[i] == 0) {
								slot_rune[i] = item.getId();
								idx = S_EquipmentWindow.EQUIPMENT_INDEX_RUNE + i;
								break;
							}
						}
					}
					// 如果需要解除裝備，只有在已有裝備的情況下更新內存。
					// 如果正在解除裝備，移除之前應用的位置。
					if (!item.isEquipped() && select_idx != -1) {
						slot_rune[select_idx] = 0;
						idx = S_EquipmentWindow.EQUIPMENT_INDEX_RUNE + select_idx;
					}
					break;
				case 30: // 文章裝備
					idx = S_EquipmentWindow.EQUIPMENT_INDEX_SENTENCE;
					break;
				case 15: // 護腿
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_PAIR;
				break;
			case 31:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_SHOULD;
				break;
			case 32:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_BADGE;
				break;
			case 33:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_PANDENT;
				break;
			}
		} else {
			switch (item.getItem().getType1()) {
			case 11: // 도끼
				if (item.isEquipped()) {
					if (worldjoin && pc.getEquipSlot().getWeaponCount() == 2)
						idx = pc.getEquipSlot().worldjoin_weapon_idx++ % 2 == 0 ? S_EquipmentWindow.EQUIPMENT_INDEX_WEAPON : S_EquipmentWindow.EQUIPMENT_INDEX_SHIELD;
					else
						idx = pc.getEquipSlot().getWeaponCount() == 1 ? S_EquipmentWindow.EQUIPMENT_INDEX_WEAPON : S_EquipmentWindow.EQUIPMENT_INDEX_SHIELD;
				} else
					idx = pc.getEquipSlot().getWeaponCount() == 0 ? S_EquipmentWindow.EQUIPMENT_INDEX_WEAPON : S_EquipmentWindow.EQUIPMENT_INDEX_SHIELD;
				break;
			default:
				idx = S_EquipmentWindow.EQUIPMENT_INDEX_WEAPON;
				break;
			}
		}
		if (idx != 0)
			pc.sendPackets(new S_EquipmentWindow(pc, item.getId(), idx, item.isEquipped()));
	}

	public int getSize() {
		return _items.size();
	}

	public List<L1ItemInstance> getItems() {
		return _items;
	}

	public List<L1ItemInstance> getItems(InventoryFindItemFilter filter) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>(_items.size());
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;

			if (!filter.isFilter(item))
				itemList.add(item);
		}
		return itemList;
	}

	public int getWeight() {
		int weight = 0;

		for (L1ItemInstance item : _items) {
			weight += item.getWeight();
		}
		weight /= Config.ServerRates.RateWeightLimit;
		return weight;
	}

	public int checkAddAdena(int count) {
		if (count < 0 || count > MAX_AMOUNT)
			return AMOUNT_OVER;

		if (!checkItem(L1ItemId.ADENA) && getSize() >= 200)
			return SIZE_OVER;

		return OK;
	}

	public int checkAddItem(L1ItemInstance item, int count) {
		if (item == null) {
			return -1;
		}
		if (count < 0 || count > MAX_AMOUNT) {
			return AMOUNT_OVER;
		}

		if (item.getCount() <= 0 || count <= 0) {
			return -1;
		}
		if (getSize() > Config.ServerAdSetting.NPCMAXITEM || (getSize() == Config.ServerAdSetting.NPCMAXITEM && (!item.isStackable() || !checkItem(item.getItem().getItemId())))) {
			return SIZE_OVER;
		}

		int weight = getWeight() + item.getItem().getWeight() * count / 1000 + 1;
		if (weight < 0 || (item.getItem().getWeight() * count / 1000) < 0) {
			return WEIGHT_OVER;
		}
		if (weight > (MAX_WEIGHT * Config.ServerRates.RateWeightLimitforPet)) {
			return WEIGHT_OVER;
		}

		L1ItemInstance itemExist = findItemId(item.getItemId());
		if (itemExist != null && (itemExist.getCount() + count) > MAX_AMOUNT) {
			return AMOUNT_OVER;
		}

		return OK;
	}

	public int checkAddItemToWarehouse(L1ItemInstance item, int count, int type) {
		if (item == null) {
			return -1;
		}
		if (item.getCount() <= 0 || count <= 0) {
			return -1;
		}
		int maxSize = 100;
		if (type == WAREHOUSE_TYPE_PERSONAL) {
			maxSize = Config.ServerAdSetting.WAREHOUSEMAXITEM;
		} else if (type == WAREHOUSE_TYPE_CLAN) {
			maxSize = Config.ServerAdSetting.CLANWAREHOUSEMAXITEM;
		}
		if (getSize() > maxSize || (getSize() == maxSize && (!item.isStackable() || !checkItem(item.getItem().getItemId())))) {
			return SIZE_OVER;
		}

		return OK;
	}

	public synchronized L1ItemInstance storeItem(int id, int count, int enchant) {
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}

		if (temp.isStackable()) {
			L1ItemInstance item = new L1ItemInstance(temp, count);
			if (findItemId(id) == null) {
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = new L1ItemInstance(temp, 1);
			item.setId(IdFactory.getInstance().nextId());
			item.setEnchantLevel(enchant);
			item.setIdentified(true);
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}

	public L1ItemInstance storeItem(int id, int count) {
		return storeItem(id, count, false);
	}
	
	public synchronized L1ItemInstance storeItem(int id, int count, int enchant, Timestamp t) {
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}

		if (temp.isStackable()) {
			L1ItemInstance item = new L1ItemInstance(temp, count);
			if (findItemId(id) == null) {
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = new L1ItemInstance(temp, 1);
			item.setId(IdFactory.getInstance().nextId());
			item.setEnchantLevel(enchant);
			item.setIdentified(true);
			item.setEndTime(t);
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}
	
	public synchronized L1ItemInstance storeItem(int id, int count, boolean isIden) {
		if (count <= 0) {
			return null;
		}
		
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}
		
		if (temp.isStackable()) {
			L1ItemInstance item = new L1ItemInstance(temp, count);
			item.setIdentified(isIden);
			if (findItemId(id) == null) {
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}

			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = new L1ItemInstance(temp, 1);
			item.setIdentified(isIden);
			item.setId(IdFactory.getInstance().nextId());
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}

	public synchronized L1ItemInstance EnchanteestoreItem(int id, int count, boolean carving, boolean isIden) {
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}

		if (temp.isStackable()) {
			L1ItemInstance item = new L1ItemInstance(temp, count);
			if (carving)
				item.set_Carving(1);
			item.setIdentified(isIden);
			if (findItemId(id) == null) {
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}

			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = new L1ItemInstance(temp, 1);
			if (carving)
				item.set_Carving(1);
			item.setIdentified(isIden);
			item.setId(IdFactory.getInstance().nextId());
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}

	public synchronized L1ItemInstance TalkIsland_storeItem(int id, int count) {
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}

		if (temp.isStackable()) {
			L1ItemInstance item = new L1ItemInstance(temp, count);

			if (findItemId(id) == null) {
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}

			item.setIdentified(true);

			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = new L1ItemInstance(temp, 1);
			item.setId(IdFactory.getInstance().nextId());
			item.setIdentified(true);
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}

	public synchronized L1ItemInstance storeItemTrea(L1ItemInstance item) {
		if (item.getCount() <= 0) {
			return null;
		}
		int itemId = item.getItem().getItemId();
		if (item.isStackable()) {
			L1ItemInstance findItem = findItemId(itemId);
			if (findItem != null) {
				findItem.setCount(findItem.getCount() + item.getCount());
				updateItem(findItem);
				return findItem;
			}
		}
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());
		int chargeCount = item.getItem().getMaxChargeCount();

		if (itemId == 20383) {
			chargeCount = 50;
		}

		item.setChargeCount(chargeCount);

		switch (itemId) {
		case 3000048: // 꼬마 요정의 마음
			SetDeleteTime(item, 1439); // 24시간 -1분
			break;
		case L1ItemId.KILLTON_CONTRACT:
		case L1ItemId.MERIN_CONTRACT:
			SetDeleteTime(item, 60);
			break;
		default:
			break;
		}

		if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light
			item.setRemainingTime(item.getItem().getLightFuel());
		} else {
			item.setRemainingTime(item.getItem().getMaxUseTime());
		}

		int bless = item.getBless();
		item.setBless(bless);

		insertItem(item);
		return item;
	}

	public L1ItemInstance storeItem(L1ItemInstance item, boolean flag) {
		item.setIdentified(flag);
		return storeItem(item);
	}

	public synchronized L1ItemInstance storeItem(L1ItemInstance item/* , boolean identified */) {
		if (item.getCount() <= 0) {
			return null;
		}
		// TODO 18억 이상이면 1억 아데나씩 까면서 수표로 만든다.
		int over_count = item.getCount() + this.countItems(L1ItemId.ADENA);
		if (over_count >= Config.ServerAdSetting.ADEN_OVER_COUNT) {
			if (this.consumeItem(Config.ServerAdSetting.ADEN_ITEMID, Config.ServerAdSetting.ADEN_COUNT)) {
				this.storeItem(Config.ServerAdSetting.REWARD_ITEMID, Config.ServerAdSetting.REWARD_COUNT);
			}
		}
		int itemId = item.getItem().getItemId();
		if (item.isStackable()) {
			L1ItemInstance findItem = findItemId(itemId);

			if (findItem != null) {
				findItem.setCount(findItem.getCount() + item.getCount());
				updateItem(findItem);
				return findItem;
			}
		}
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());
		int chargeCount = item.getItem().getMaxChargeCount();
		switch (itemId) {
		case 40006:
		case 40007:
		case 40008:
		case 140006:
		case 140008:
		case 41401:
		case 810006:
		case 810007:
			L1ItemInstance findItem = findItemId(itemId);
			if (findItem != null) {
				Random random = new Random(System.nanoTime());
				chargeCount -= random.nextInt(5);
				findItem.setChargeCount(findItem.getChargeCount() + chargeCount);
				updateItem(findItem);
				return findItem;
			}
			break;
		}

		if (itemId == 20383) {
			chargeCount = 50;
		}

		item.setChargeCount(chargeCount);

		// 시간제 아이템
		switch (itemId) {
		// 호랑이/진돗개
		case 3000048: // 꼬마 요정의 마음
			SetDeleteTime(item, 1439); // 24시간 -1분
			break;
		case L1ItemId.KILLTON_CONTRACT:
		case L1ItemId.MERIN_CONTRACT:
			SetDeleteTime(item, 60);
			break;
		case 80500:
			SetDeleteTime(item, 120);
			break;
		// case L1ItemId.MERIN_CONTRACT:
		// SetDeleteTime(item, 60);
		// break;
		case 100036:
		case 500210: // 테베,쿠쿨 제단 열쇠
		case L1ItemId.DRAGON_KEY: // 드래곤키
		case 490012:
		case 490013:
		case 490014:
		case 40312:
			SetDeleteTime(item, 180); // 3시간
			break;
		case 30022: // 마법인형: 블레그
		case 30023: // 마법인형: 레데그
		case 30024: // 마법인형: 엘레그
		case 30025: // 마법인형: 그레그
			SetDeleteTime(item, 300); // 5시간
			break;
		case 22328: // 베테르랑 방어구
		case 22329:
		case 22330:
		case 22331:
		case 22332:
		case 22333:
		case 22334:
		case 22335:
		case 1126: // 베테르랑 무기
		case 1127:
		case 1128:
		case 1129:
		case 1130:
		case 1131:
		case 1132:
		case 1133:
			SetDeleteTime(item, 4320); // 3일
			break;
		case 41922:
		case 41923:
		case 41924:
		case 41925:
		case 775:
		case 774:
		case 773:
		case 772:
		case 30001462: //루운성 탐험 증표
			SetDeleteTime(item, 1440); // 1일
			break;
		case 210095:
		case 2100950:
		case 2100951:
		case 2100952:
		case 2100953:// 쫄사냥
			SetDeleteTime(item, 180); // 3일
			break;
		/** 신규 초보자 무기 **/
		case 7000137:
		case 7000138:
		case 7000139:
		case 7000140:
		case 7000141:
		case 7000142:
		case 7000143:
		case 7000144:
		case 505001:
		case 505002:
		case 505003:
		case 505004:
		case 505005:
		case 505006:
		case 505007:
		case 505008:
			SetDeleteTime1(item, 1440); // 3일
			break;

		case 4100460:
			SetDeleteTime1(item, 1380); // 23시간
			break;
		default:
			break;
		}

		if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light
			item.setRemainingTime(item.getItem().getLightFuel());
		} else {
			item.setRemainingTime(item.getItem().getMaxUseTime());
		}
		item.setIdentified(item.isIdentified());
		item.setBless(item.getItem().getBless());

		if (item.get_bless_level() > 0) {
			item.setBless(0);
			item.set_bless_level(item.get_bless_level());
		}

		insertItem(item);
		return item;
	}

	public L1ItemInstance storeShopItem(L1ItemInstance item, boolean flag) {
		item.setIdentified(flag);
		return storeItem(item, item.getBless());
	}

	public synchronized L1ItemInstance storeItem(L1ItemInstance item, int bless) {
		if (item.getCount() <= 0) {
			return null;
		}
		int itemId = item.getItem().getItemId();
		if (item.isStackable()) {
			L1ItemInstance findItem = findItemId(itemId);

			if (findItem != null) {
				findItem.setCount(findItem.getCount() + item.getCount());
				updateItem(findItem);
				return findItem;
			}
		}
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());
		int chargeCount = item.getItem().getMaxChargeCount();
		switch (itemId) {
		case 40006:
		case 40007:
		case 40008:
		case 140006:
		case 140008:
		case 41401:
		case 810006:
		case 810007:
			L1ItemInstance findItem = findItemId(itemId);
			if (findItem != null) {
				Random random = new Random(System.nanoTime());
				chargeCount -= random.nextInt(5);
				findItem.setChargeCount(findItem.getChargeCount() + chargeCount);
				updateItem(findItem);
				return findItem;
			}
			break;
		}

		if (itemId == 20383) {
			chargeCount = 50;
		}

		item.setChargeCount(chargeCount);

		// 시간제 아이템
		switch (itemId) {
		// 호랑이/진돗개
		case 3000048: // 꼬마 요정의 마음
			SetDeleteTime(item, 1439); // 24시간 -1분
			break;
		case L1ItemId.KILLTON_CONTRACT:
		case L1ItemId.MERIN_CONTRACT:
			SetDeleteTime(item, 60);
			break;
		case 80500:
			SetDeleteTime(item, 120);
			break;
		// case L1ItemId.MERIN_CONTRACT:
		// SetDeleteTime(item, 60);
		// break;
		case 100036:
		case 500210: // 테베,쿠쿨 제단 열쇠
		case L1ItemId.DRAGON_KEY: // 드래곤키
		case 490012:
		case 490013:
		case 490014:
		case 40312:
			SetDeleteTime(item, 180); // 3시간
			break;
		case 30022: // 마법인형: 블레그
		case 30023: // 마법인형: 레데그
		case 30024: // 마법인형: 엘레그
		case 30025: // 마법인형: 그레그
			SetDeleteTime(item, 300); // 5시간
			break;
		case 22328: // 베테르랑 방어구
		case 22329:
		case 22330:
		case 22331:
		case 22332:
		case 22333:
		case 22334:
		case 22335:
		case 1126: // 베테르랑 무기
		case 1127:
		case 1128:
		case 1129:
		case 1130:
		case 1131:
		case 1132:
		case 1133:
			SetDeleteTime(item, 4320); // 3일
			break;
		case 20282: // 수련자의 방어구
		case 7: // 수련자의 무기
		case 105:
		case 120:
		case 156:
		case 174:
		case 224:
		case 203012:
			SetDeleteTime(item, 4320); // 3일
			break;
		case 41922:
		case 41923:
		case 41924:
		case 41925:
		case 775:
		case 774:
		case 773:
		case 772:
			SetDeleteTime(item, 1440); // 1일
			break;
		case 3000214:// 자동사냥 7일
			SetDeleteTime(item, 8640); // 7일
			break;
		case 210095:
		case 2100950:
		case 2100951:
		case 2100952:
		case 2100953:// 쫄사냥
			SetDeleteTime(item, 180); // 3일
			break;
		/** 신규 초보자 무기 **/
		case 7000137:
		case 7000138:
		case 7000139:
		case 7000140:
		case 7000141:
		case 7000142:
		case 7000143:
		case 7000144:
		case 505001:
		case 505002:
		case 505003:
		case 505004:
		case 505005:
		case 505006:
		case 505007:
		case 505008:
			SetDeleteTime1(item, 1440); // 3일
			break;
		case 4100460:
			SetDeleteTime1(item, 1380); // 23시간
			break;
		default:
			break;
		}

		if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light
			item.setRemainingTime(item.getItem().getLightFuel());
		} else {
			item.setRemainingTime(item.getItem().getMaxUseTime());
		}
		item.setBless(bless);

		insertItem(item);
		return item;
	}

	private void SetDeleteTime(L1ItemInstance item, int minute) {
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * minute));
		item.setEndTime(deleteTime);
	}

	private void SetDeleteTime1(L1ItemInstance item, int minute) {
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(System.currentTimeMillis() + (20000 * minute));
		item.setEndTime(deleteTime);
	}

	public synchronized L1ItemInstance storeTradeItem(L1ItemInstance item) {
		try {
			// TODO 18억 이상이면 1억 아데나씩 까면서 수표로 만든다.
			int over_count = item.getCount() + this.countItems(L1ItemId.ADENA);
			if (over_count >= Config.ServerAdSetting.ADEN_OVER_COUNT) {
				if (this.consumeItem(Config.ServerAdSetting.ADEN_ITEMID, Config.ServerAdSetting.ADEN_COUNT)) {
					this.storeItem(Config.ServerAdSetting.REWARD_ITEMID, Config.ServerAdSetting.REWARD_COUNT);
				}
			}
			
			if (item.isStackable()) {
				L1ItemInstance findItem = findItemId(item.getItem().getItemId());
				if (findItem != null) {
					findItem.setCount(findItem.getCount() + item.getCount());
					findItem.setGiveItem(item.isGiveItem());
					updateItem(findItem);
					return findItem;
				}
			}

			switch (item.getItem().getItemId()) {
			case 40006:
			case 40007:
			case 40008:
			case 140006:
			case 140008:
			case 41401:
			case 810006:
			case 810007:
				L1ItemInstance findItem = findItemId(item.getItem().getItemId());
				if (findItem != null) {
					int chargeCount = item.getChargeCount();
					findItem.setChargeCount(findItem.getChargeCount() + chargeCount);
					updateItem(findItem);
					return findItem;
				}
				break;
			}
			item.setX(getX());
			item.setY(getY());
			item.setMap(getMapId());

			insertItem(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	public boolean createList1(int itemid, int count, int en) {
		if (count <= 0) {
			return false;
		}
		if (ItemTable.getInstance().getTemplate(itemid).isStackable()) {
			L1ItemInstance item = findItemId(itemid);
			if (item != null && item.getCount() >= count && item.getEnchantLevel() == en) {
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsId(itemid);
			if (itemList.length == count) {
				int j = 0;
				for (int i = 0; i < count; ++i) {
					if (itemList[i].getEnchantLevel() == en) {
						removeItem(itemList[i], 1);
						if (++j == count)
							break;
					}
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				int j = 0;
				for (int i = 0; i < itemList.length; ++i) {
					if (itemList[i].getEnchantLevel() == en) {
						removeItem(itemList[i], 1);
						if (++j == count)
							break;
					}
				}
				return true;
			}
		}
		return false;
	}

	public L1ItemInstance[] findItemsId_HighEnchant(int id, int enchant) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id && item.getEnchantLevel() == enchant) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public boolean createList3(int itemid, int count, int en, int bless, int descid, int attr, int attr_en) {
		L1ItemInstance item = findItemDescId(itemid);
		if (item == null) {
			return false;
		}
		if (count <= 0) {
			return false;
		}
		if (attr == 2) {
			attr_en += 5;
		} else if (attr == 3) {
			attr_en += 10;
		} else if (attr == 4) {
			attr_en += 15;
		}
		if (item.isStackable()) {
			if (item != null && item.getCount() >= count && item.getEnchantLevel() == en && item.getBless() == bless && item.getItem().getItemDescId() == descid
					&& item.getAttrEnchantLevel() == attr_en) {
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsId_HighEnchant(item.getItem().getItemId(), en);
			if (itemList.length == count) {
				int j = 0;
				for (int i = 0; i < count; ++i) {
					if (itemList[i].getEnchantLevel() == en && itemList[i].getBless() == bless && itemList[i].getItem().getItemDescId() == descid && itemList[i].getAttrEnchantLevel() == attr_en) {
						removeItem(itemList[i], 1);
						if (++j == count)
							break;
					}
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				int j = 0;
				for (int i = 0; i < itemList.length; ++i) {
					if (itemList[i].getEnchantLevel() == en && itemList[i].getBless() == bless && itemList[i].getItem().getItemDescId() == descid && itemList[i].getAttrEnchantLevel() == attr_en) {
						removeItem(itemList[i], 1);
						if (++j == count)
							break;
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean ConsumeItemDescId(int descId, int enchant, int count) {
		L1ItemInstance item = findItemDescId(descId);
		if (item != null) {
			if (count <= 0)
				return false;
			if (item.isStackable()) {
				if (item.getCount() >= count) {
					removeItem(item, count);
					return true;
				}
			} else {
				L1ItemInstance[] itemList = findItemsId_HighEnchant(item.getItem().getItemId(), enchant);
				if (itemList.length >= count) {
					for (int i = 0; i < count; i++) {
						removeItem(itemList[i], 1);
					}
					return true;
				}
			}
		}
		return false;
	}

	public boolean consumeItem(int itemid, int count) {
		L1Item tem = ItemTable.getInstance().getTemplate(itemid);
		if (tem == null) {
			return false;
		}
		if (count <= 0) {
			return false;
		}

		if (tem.isStackable()) {
			L1ItemInstance item = findItemId(itemid);
			if (item != null && item.getCount() >= count) {
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsId(itemid);
			if (itemList.length == count) {
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			}
		}
		return false;
	}

	public boolean consumeItem2(int descid, int count) {
		if (count <= 0) {
			return false;
		}

		L1ItemInstance[] itemList = findItemsId(descid, true);
		if (itemList.length == count) {
			for (int i = 0; i < count; i++) {
				removeItem(itemList[i], 1);
			}
			return true;
		} else if (itemList.length > count) {
			DataComparator dc = new DataComparator();
			extracted(itemList, dc);
			for (int i = 0; i < count; i++) {
				removeItem(itemList[i], 1);
			}
			return true;
		}

		return false;
	}

	public boolean consumeItem(int itemid, int count, int descid) {
		if (count <= 0) {
			return false;
		}
		if (ItemTable.getInstance().getTemplate(itemid).isStackable()) {
			L1ItemInstance item = findItemId(itemid);
			if (item != null && item.getCount() >= count && item.getItem().getItemDescId() == descid) {
				removeItem(item, count, descid);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsId(itemid, descid);
			if (itemList.length == count) {
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1, descid);
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1, descid);
				}
				return true;
			}
		}
		return false;
	}

	public boolean consumeRankItem(L1PcInstance pc, int itemid, int count, int enchant) {
		if (count <= 0) {
			return false;
		}
		if (ItemTable.getInstance().getTemplate(itemid).isStackable()) {
			L1ItemInstance item = findItemId(itemid);
			if (item != null && item.getCount() >= count && item.getEnchantLevel() == enchant) {
				if (item.isEquipped())
					pc.getInventory().setEquipped(item, false);
				removeItem(item);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findRankItemsId(itemid, enchant);
			if (itemList.length == count) {
				for (int i = 0; i < count; i++) {
					if (itemList[i].isEquipped())
						pc.getInventory().setEquipped(itemList[i], false);
					removeItem(itemList[i], 1);
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				for (int i = 0; i < count; i++) {
					if (itemList[i].isEquipped())
						pc.getInventory().setEquipped(itemList[i], false);
					removeItem(itemList[i], 1);
				}
				return true;
			}
		}
		return false;
	}

	public boolean consumeItem(int itemid) {
		L1ItemInstance item = findItemId(itemid);
		if (item != null) {
			removeItem(item, item.getCount());
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void extracted(L1ItemInstance[] itemList, DataComparator dc) {
		Arrays.sort(itemList, dc);
	}

	@SuppressWarnings("rawtypes")
	public class DataComparator implements java.util.Comparator {
		public int compare(Object item1, Object item2) {
			return ((L1ItemInstance) item1).getEnchantLevel() - ((L1ItemInstance) item2).getEnchantLevel();
		}
	}

	public int removeItem(int objectId) {
		L1ItemInstance item = getItem(objectId);
		return removeItem(item, item.getCount());
	}

	public int removeItem(int objectId, int count) {
		L1ItemInstance item = getItem(objectId);
		return removeItem(item, count);
	}

	public int removeItem(L1ItemInstance item) {
		return removeItem(item, item.getCount());
	}

	public int removeItem(L1ItemInstance item, int count) {
		if (item == null) {
			return 0;
		}
		if (item.getCount() <= 0 || count <= 0) {
			return 0;
		}
		if (item.getCount() < count) {
			count = item.getCount();
		}
		if (item.getCount() == count) {
			int itemId = item.getItem().getItemId();
			if (itemId == 40314 || itemId == 40316) {
				PetTable.getInstance().deletePet(item.getId());
			} else if (itemId >= 49016 && itemId <= 49025) {
				LetterTable lettertable = new LetterTable();
				lettertable.deleteLetter(item.getId());
			} else if (itemId >= 41383 && itemId <= 41400) {
				L1FurnitureInstance furniture = null;
				for (L1Object l1object : L1World.getInstance().getObject()) {
					if (l1object == null)
						continue;
					if (l1object instanceof L1FurnitureInstance) {
						furniture = (L1FurnitureInstance) l1object;
						if (furniture.getItemObjId() == item.getId()) {
							FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
						}
					}
				}
			}
			deleteItem(item);
			L1World.getInstance().removeObject(item);
		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
		}
		return count;
	}

	public int removeItem(L1ItemInstance item, int count, int descid) {
		if (item == null) {
			return 0;
		}
		if (item.getCount() <= 0 || count <= 0) {
			return 0;
		}
		if (item.getItem().getItemDescId() != descid) {
			return 0;
		}
		if (item.getCount() < count) {
			count = item.getCount();
		}
		if (item.getCount() == count) {
			int itemId = item.getItem().getItemId();
			if (itemId == 40314 || itemId == 40316) {
				PetTable.getInstance().deletePet(item.getId());
			} else if (itemId >= 49016 && itemId <= 49025) {
				LetterTable lettertable = new LetterTable();
				lettertable.deleteLetter(item.getId());
			} else if (itemId >= 41383 && itemId <= 41400) {
				L1FurnitureInstance furniture = null;
				for (L1Object l1object : L1World.getInstance().getObject()) {
					if (l1object == null)
						continue;
					if (l1object instanceof L1FurnitureInstance) {
						furniture = (L1FurnitureInstance) l1object;
						if (furniture.getItemObjId() == item.getId()) {
							FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
						}
					}
				}
			}
			deleteItem(item);
			L1World.getInstance().removeObject(item);
		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
		}
		return count;
	}

	public void deleteItem(L1ItemInstance item) {
		_items.remove(item);
	}

	public synchronized L1ItemInstance tradeItem(int objectId, int count, Warehouse inventory) {
		L1ItemInstance item = getItem(objectId);
		return tradeItem(item, count, inventory);
	}

	public synchronized L1ItemInstance tradeItem(int objectId, int count, L1Inventory inventory) {
		L1ItemInstance item = getItem(objectId);
		return tradeItem(item, count, inventory);
	}

	public synchronized L1ItemInstance tradeItem(L1ItemInstance item, int count, Warehouse inventory) {
		if (item == null) {
			return null;
		}
		if (item.getCount() <= 0 || count <= 0) {
			return null;
		}
		if (item.isEquipped()) {
			return null;
		}
		if (!checkItem(item.getItem().getItemId(), count)) {
			return null;
		}

		L1ItemInstance carryItem;
		// 엔진관련 버그 방지 추가
		if (item.getCount() <= count || count < 0) {
			deleteItem(item);
			carryItem = item;
		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
			carryItem = ItemTable.getInstance().createItem(item.getItem().getItemId());
			carryItem.setCount(count);
			carryItem.setEnchantLevel(item.getEnchantLevel());
			carryItem.setIdentified(item.isIdentified());
			carryItem.set_durability(item.get_durability());
			carryItem.setChargeCount(item.getChargeCount());
			carryItem.setRemainingTime(item.getRemainingTime());
			carryItem.setLastUsed(item.getLastUsed());
			carryItem.setBless(item.getItem().getBless());
			carryItem.setAttrEnchantLevel(item.getAttrEnchantLevel());
			carryItem.setSpecialEnchant(item.getSpecialEnchant());
			carryItem.set_bless_level(item.get_bless_level());
			carryItem.set_item_level(item.get_item_level());
			carryItem.setHotel_Town(item.getHotel_Town());
			carryItem.set_Carving(item.get_Carving());
			carryItem.set_Doll_Bonus_Level(item.get_Doll_Bonus_Level());
			carryItem.set_Doll_Bonus_Value(item.get_Doll_Bonus_Value());
			carryItem.setBlessType(item.getBlessType());
			carryItem.setBlessTypeValue(item.getBlessTypeValue());
		}
		return inventory.storeTradeItem(carryItem);
	}

	public synchronized L1ItemInstance tradeItem(L1ItemInstance item, int count, L1Inventory inventory) {
		L1ItemInstance carryItem = null;
		if (item == null) {
			return null;
		}
		if (item.getCount() <= 0 || count <= 0) {
			return null;
		}
		if (item.isEquipped()) {
			return null;
		}
		if (!checkItem(item.getItem().getItemId(), count)) {
//			System.out.println("count: " + count + "/item.getItem().getItemId():" + item.getItem().getItemId());
			return null;
		}

		// TODO 18억 이상이면 1억 아데나씩 까면서 수표로 만든다.
		int over_count = count + inventory.countItems(L1ItemId.ADENA);
		if (over_count >= Config.ServerAdSetting.ADEN_OVER_COUNT) {
			if (inventory.consumeItem(Config.ServerAdSetting.ADEN_ITEMID, Config.ServerAdSetting.ADEN_COUNT)) {
				inventory.storeItem(Config.ServerAdSetting.REWARD_ITEMID, Config.ServerAdSetting.REWARD_COUNT);
			}
		}

		// TODO 엔진관련 버그 방지 추가
		if (item.getCount() <= count || count < 0) {
			deleteItem(item);
			carryItem = item;
		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
			carryItem = ItemTable.getInstance().createItem(item.getItem().getItemId());
			carryItem.setCount(count);
			carryItem.setEnchantLevel(item.getEnchantLevel());
			carryItem.setIdentified(item.isIdentified());
			carryItem.set_durability(item.get_durability());
			carryItem.setChargeCount(item.getChargeCount());
			carryItem.setRemainingTime(item.getRemainingTime());
			carryItem.setLastUsed(item.getLastUsed());
			carryItem.setBless(item.getItem().getBless());
			carryItem.setAttrEnchantLevel(item.getAttrEnchantLevel());
			carryItem.setSpecialEnchant(item.getSpecialEnchant());
			carryItem.set_bless_level(item.get_bless_level());
			carryItem.set_item_level(item.get_item_level());
			carryItem.setHotel_Town(item.getHotel_Town());

			carryItem.set_Carving(item.get_Carving());
			carryItem.set_Doll_Bonus_Level(item.get_Doll_Bonus_Level());
			carryItem.set_Doll_Bonus_Value(item.get_Doll_Bonus_Value());
			carryItem.setBlessType(item.getBlessType());
			carryItem.setBlessTypeValue(item.getBlessTypeValue());
		}
		return inventory.storeTradeItem(carryItem);
	}

	public L1ItemInstance receiveDamage(int objectId) {
		L1ItemInstance item = getItem(objectId);
		return receiveDamage(item);
	}

	public L1ItemInstance receiveDamage(L1ItemInstance item) {
		return receiveDamage(item, 1);
	}

	public L1ItemInstance receiveDamage(L1ItemInstance item, int count) {
		if (item == null) {
			return null;
		}
		int itemType = item.getItem().getType2();
		int currentDurability = item.get_durability();
		if ((currentDurability == 0 && itemType == 0) || currentDurability < 0) {
			item.set_durability(0);
			return null;
		}
		if (itemType == 0) {
			int minDurability = (item.getEnchantLevel() + 5) * -1;
			int durability = currentDurability - count;
			if (durability < minDurability) {
				durability = minDurability;
			}
			if (currentDurability > durability) {
				item.set_durability(durability);
			}
		} else {
			int maxDurability = item.getEnchantLevel() + 5;
			int durability = currentDurability + count;
			if (durability > maxDurability) {
				durability = maxDurability;
			}
			if (currentDurability < durability) {
				item.set_durability(durability);
			}
		}

		updateItem(item, L1PcInventory.COL_DURABILITY);
		return item;
	}

	public L1ItemInstance recoveryDamage(L1ItemInstance item) {
		if (item == null) {
			return null;
		}
		int itemType = item.getItem().getType2();
		int durability = item.get_durability();

		if ((durability == 0 && itemType != 0) || durability < 0) {
			item.set_durability(0);
			return null;
		}

		if (itemType == 0) {
			item.set_durability(durability + 1);
		} else {
			item.set_durability(durability - 1);
		}

		updateItem(item, L1PcInventory.COL_DURABILITY);
		return item;
	}

	public L1ItemInstance findEquippedItemId(int id) {
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if ((item.getItem().getItemId() == id) && item.isEquipped()) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance findItemObjId(int id) {
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance findItemDescId(int descId) {
		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemDescId() == descId)
				return item;
		}
		return null;
	}

	public L1ItemInstance findItemId(int id) {
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getItem().getItemId() == id) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance[] findItemsId(int id) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getItemId() == id) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public L1ItemInstance[] findRankItemsId(int id, int enchant) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getItemId() == id && item.getEnchantLevel() == enchant) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public L1ItemInstance[] findItemsId(int descid, boolean flag) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getItem().getItemDescId() == descid) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public L1ItemInstance[] findItemsId(int id, int descid) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getItemId() == id && item.getItem().getItemDescId() == descid) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public L1ItemInstance[] findItemsId(int itemId, int enchant, int bless, int attr) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getItemId() == itemId && item.getEnchantLevel() == enchant && item.getItem().getBless() == bless && item.getAttrEnchantLevel() == attr) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public L1ItemInstance[] findItemsIdNotEquipped(int id) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getItemId() == id) {
				if (!item.isEquipped()) {
					itemList.add(item);
				}
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public L1ItemInstance getItem(int objectId) {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item == null)
				continue;
			if (item.getId() == objectId) {
				return item;
			}
		}
		return null;
	}

	public boolean contains_at_one(int[] item_id_array) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(item_id_array.length);
		for (int itemId : item_id_array)
			map.put(itemId, itemId);
		return contains_at_one(map);
	}

	public boolean contains_at_one(HashMap<Integer, Integer> map) {
		for (L1ItemInstance item : _items) {
			if (map.containsKey(item.getItemId())) {
				return true;
			}
		}
		return false;
	}

	public boolean checkItem(int id) {
		return checkItem(id, 1);
	}

	public boolean checkItem(int itemId, int count, int enchant, int bless, int attr) {
		if (count <= 0)
			return false;

		L1Item temp = ItemTable.getInstance().getTemplate(itemId);
		if (temp == null)
			return false;

		if (temp.isStackable()) {
			L1ItemInstance item = findItemId(itemId);
			if (item != null && item.getCount() >= count) {
				return true;
			}
		} else {
			Object[] itemList = findItemsId(itemId, enchant, bless, attr);
			if (itemList.length >= count) {
				return true;
			}
		}
		return false;
	}

	public boolean checkItem(int id, int count) {
		if (count < 0) {
			return false;
		}

		if (count == 0) {
			return true;
		}
		L1Item l1item = ItemTable.getInstance().getTemplate(id);
		if (l1item.isStackable()) {
			L1ItemInstance item = findItemId(id);
			if (item != null && item.getCount() >= count) {
				return true;
			}
		} else {
			Object[] itemList = findItemsId(id);
			if (itemList.length >= count) {
				return true;
			}
		}
		return false;
	}

	// ////////////////인첸된 체크 아이템 재코딩
	public boolean checkEnchantItem(int id, int enchant, int count) {
		int num = 0;
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) {
				continue;
			}
			if (item.getItemId() == id && item.getEnchantLevel() == enchant) {
				num++;
				if (num == count) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkEnchantItem(int id, int enchant, int count, int descid) {
		int num = 0;
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) {
				continue;
			}
			if (item.getItemId() == id && item.getEnchantLevel() == enchant && item.getItem().getItemDescId() == descid) {
				num++;
				if (num == count) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean createList(int id, int enchant, int count) {
		int num = 0;
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) {
				continue;
			}
			if (item.getItemId() == id && item.getEnchantLevel() == enchant) {
				num += item.getCount();
				if (num >= count) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean consumeEnchantItem(int id, int enchant, int count) {
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) {
				continue;
			}
			if (item.getItemId() == id && item.getEnchantLevel() == enchant) {
				removeItem(item);
				return true;
			}
		}
		return false;
	}

	public boolean consumeEnchantItem(int id, int enchant, int count, int descid) {
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) {
				continue;
			}
			if (item.getItemId() == id && item.getEnchantLevel() == enchant && item.getItem().getItemDescId() == descid) {
				removeItem(item);
				return true;
			}
		}
		return false;
	}

	// ////////////////////////////////////

	public boolean checkItemNotEquipped(int id, int count) {
		if (count == 0) {
			return true;
		}
		return count <= countItems(id);
	}

	public boolean checkItem(int[] ids) {
		int len = ids.length;
		int[] counts = new int[len];
		for (int i = 0; i < len; i++) {
			counts[i] = 1;
		}
		return checkItem(ids, counts);
	}

	public boolean checkItem(int[] ids, int[] counts) {
		for (int i = 0; i < ids.length; i++) {
			if (!checkItem(ids[i], counts[i])) {
				return false;
			}
		}
		return true;
	}

	public int countItems(int id) {
		L1Item tem = ItemTable.getInstance().getTemplate(id);
		if (tem == null) {
			System.out.println("countItems null 錯誤 (物品編號 : " + id + ")");
			return 0;
		}

		if (tem.isStackable()) {
			L1ItemInstance item = findItemId(id);
			if (item != null) {
				return item.getCount();
			}
		} else {
			Object[] itemList = findItemsIdNotEquipped(id);
			return itemList.length;
		}
		return 0;
	}

	public void shuffle() {
		if (_items == null || _items.size() <= 1)
			return;
		try {
			Collections.shuffle(_items);
		} catch (Exception e) {
		}
	}

	public void clearItems() {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item == null || item.getItemId() == MJINNHelper.INN_KEYID)
				continue;
			L1World.getInstance().removeObject(item);
		}
		_items.clear();
	}

	public void loadItems() {
	}

	public void insertItem(L1ItemInstance item) {
		_items.add(item);
	}

	public void updateItem(L1ItemInstance item) {
	}

	public void updateItem(L1ItemInstance item, int colmn) {
	}

	public L1ItemInstance storeItem(int id, int count, String name) {
		L1Item sTemp = ItemTable.getInstance().getTemplate(id);
		L1Item temp = ItemTable.getInstance().clone(sTemp, name);
		if (temp == null)
			return null;
		if (temp.isStackable()) {
			L1ItemInstance item = new L1ItemInstance(temp, count);
			item.setItem(temp);
			item.setCount(count);
			item.setBless(temp.getBless());
			item.setAttrEnchantLevel(0);
			if (!temp.isStackable() || findItemId(id) == null) {
				// 새롭게 생성할 필요가있는 경우만 ID의발행과 L1World에의등록을 실시한다
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		// 스택 할 수 없는 아이템의 경우
		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = new L1ItemInstance(temp);
			item.setId(IdFactory.getInstance().nextId());
			item.setBless(temp.getBless());
			item.setAttrEnchantLevel(0);
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		// 마지막에 만든 아이템을 돌려준다. 배열을 되돌리도록(듯이) 메소드 정의를 변경하는 편이 좋을지도 모른다.
		return result;
	}

	public void findAndRemoveItemId(int id) {
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;

			if (item.getItemId() == id)
				deleteItem(item);
		}
	}

	public void findAndRemoveItemId(int[] id) {
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;

			int item_id = item.getItemId();
			for (int i : id) {
				if (item_id == i) {
					deleteItem(item);
					break;
				}
			}
		}
	}

	private static int _instanceType = -1;

	@Override
	public int getL1Type() {
		return _instanceType == -1 ? _instanceType = super.getL1Type() | MJL1Type.L1TYPE_INVENTORY : _instanceType;
	}

	public int checkAddItem(int itemId, int count) {
		L1Item item = ItemTable.getInstance().getTemplate(itemId);
		return checkAddItem(item, count);
	}

	public int checkAddItem(L1Item item, int count) {
		if (item == null) {
			return -1;
		}

		if (count < 0 || count > MAX_AMOUNT) {
			return AMOUNT_OVER;
		}

		if (getSize() > 180 || (getSize() == 180 && (!item.isStackable() || !checkItem(item.getItemId()))))
			return SIZE_OVER;

		int weight = getWeight() + item.getWeight() * count / 1000 + 1;
		if (weight < 0 || (item.getWeight() * count / 1000) < 0)
			return WEIGHT_OVER;

		L1ItemInstance itemExist = findItemId(item.getItemId());
		if (itemExist != null && ((itemExist.getCount() + count) < 0 || (itemExist.getCount() + count) > MAX_AMOUNT))
			return AMOUNT_OVER;

		return OK;
	}
	
	/**
	 * 성물 제작 재료 아이템 유효성 검사
	 * @param sInfo
	 * @param favorItem
	 * @return L1ItemInstance
	 */
	public L1ItemInstance findItmeByCraftInputItemFavorSlotInfo(CraftInputItemSlotInfo sInfo, L1ItemInstance favorItem) {
		int descId = sInfo.get_item_name_id();
		int bless = sInfo.get_bless();
		int enchant = sInfo.get_enchant();
		int attrType = sInfo.get_elemental_enchant_type();
		int attrValue = sInfo.get_elemental_enchant_value();
		
		// 성물 제작인 경우 성물 인벤토리에서도 조사 한다.
		if (favorItem != null) {
			boolean flag = true;
			if (descId != favorItem.getItem().getItemDescId()) {
				flag = false;
			}

			if (bless != 3 && bless != favorItem.getBless()) {
				flag = false;
			}

			if (enchant != favorItem.getEnchantLevel()) {
				flag = false;
			}

			if ((attrType >= 1 && attrType <= 4) && !L1ItemInstance.equalsElement(favorItem, attrType, attrValue)) {
				flag = false;
			}
			
			if (flag) {
				// 요구한 재료와 성물인벤토리의 재료가 일치
				return favorItem;
			}
		}
		
		for (L1ItemInstance item : getItems()) {
			if (descId != item.getItem().getItemDescId()) {
				continue;
			}

			if (bless != 3 && bless != item.getBless()) {
				continue;
			}

			if (enchant != item.getEnchantLevel()) {
				continue;
			}

			if ((attrType >= 1 && attrType <= 4) && !L1ItemInstance.equalsElement(item, attrType, attrValue)) {
				continue;
			}
			return item;
		}
		return null;
	}

	/**
	 * 성물 제작 재료 수량 유효성 검사
	 * @param slotInfo
	 * @param favorItem
	 * @return size
	 */
	public int fillCraftInputItemFavorEx(LinkedList<CraftInputItemSlotInfo> slotInfo, L1ItemInstance favorItem) {
		int fillCount = 0;
		try {
			for (CraftInputItemSlotInfo sInfo : slotInfo) {
				if (sInfo.hasRemoved()) {
					continue;
				}
				
				L1ItemInstance item = findItmeByCraftInputItemFavorSlotInfo(sInfo, favorItem);
				
				if (item == null) {
					return 0;
				}
				if (!item.isStackable() && sInfo.get_count() > 1) {
					if (sInfo.isInputEx(getItems())) {
						if (sInfo.hasRemoved()) {
							++fillCount;
						}
					}
				} else {
					if (sInfo.isInput(item)) {
						sInfo.addTemporaryRemovedItems(item, item.isStackable() ? sInfo.get_count() : 1);
						if (sInfo.hasRemoved()) {
							++fillCount;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fillCount;
	}

	public L1ItemInstance findItmeByCraftInputItemSlotInfo(CraftInputItemSlotInfo sInfo) {
		int descId = sInfo.get_item_name_id();
		int bless = sInfo.get_bless();
		int enchant = sInfo.get_enchant();
		int attrType = sInfo.get_elemental_enchant_type();
		int attrValue = sInfo.get_elemental_enchant_value();
		
		for (L1ItemInstance item : getItems()) {
			if (descId != item.getItem().getItemDescId()) {
				continue;
			}

			if (bless != 3 && bless != item.getBless()) {
				continue;
			}

			if (enchant != item.getEnchantLevel()) {
				continue;
			}

			if ((attrType >= 1 && attrType <= 4) && !L1ItemInstance.equalsElement(item, attrType, attrValue)) {
				continue;
			}
			return item;
		}
		return null;
	}

	public int fillCraftInputItemEx(LinkedList<CraftInputItemSlotInfo> slotInfo) {
		int fillCount = 0;
		try {
			for (CraftInputItemSlotInfo sInfo : slotInfo) {
				if (sInfo.hasRemoved()) {
					continue;
				}
				
				// L1ItemInstance item = findItmeByDescId(sInfo.get_item_name_id());
				L1ItemInstance item = findItmeByCraftInputItemSlotInfo(sInfo);
				
				if (item == null) {
					return 0;
				}
				if (!item.isStackable() && sInfo.get_count() > 1) {
					if (sInfo.isInputEx(getItems())) {
						if (sInfo.hasRemoved()) {
							++fillCount;
						}
					}
				} else {
					if (sInfo.isInput(item)) {
						sInfo.addTemporaryRemovedItems(item, item.isStackable() ? sInfo.get_count() : 1);
						if (sInfo.hasRemoved()) {
							++fillCount;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fillCount;
	}

	public int fillCraftInputItem(LinkedList<CraftInputItemSlotInfo> slotInfo) {
		int fillCount = 0;
		int sSize = slotInfo.size();
		for (L1ItemInstance item : getItems()) {
			if (fillCount >= sSize)
				break;

			if (item == null)
				continue;

			for (CraftInputItemSlotInfo sInfo : slotInfo) {
				if (sInfo.hasRemoved()) {
					continue;
				}

				if (sInfo.isInput(item)) {
					sInfo.addTemporaryRemovedItems(item, item.isStackable() ? sInfo.get_count() : 1);
					if (sInfo.hasRemoved()) {
						++fillCount;
					}
					/**
					 * 07/29 동일아이템이 재료에 등록되어있을경우 검색이 무시된다. 문제시 주석해제.
					 */
					if (item.getItem().getItemDescId() != 7) {
						break;
					}
					// break;
				}
			}
		}
		return fillCount;
	}

	public L1ItemInstance findItmeByDescId(int descId) {
		for (L1ItemInstance item : getItems()) {
			if (item.getItem().getItemDescId() == descId) {
				return item;
			}
		}
		return null;
	}

	public int fillAlchemyInputItem(LinkedList<CS_ALCHEMY_MAKE_REQ.Input> inputs) {
		int fillCount = 0;
		int size = inputs.size();
		for (L1ItemInstance item : getItems()) {
			if (fillCount >= size)
				break;

			if (item == null)
				continue;

			for (CS_ALCHEMY_MAKE_REQ.Input input : inputs) {
				if (input.hasRemoved())
					continue;

				if (input.isInput(item)) {
					input.setTemporaryRemovedItems(item);
					++fillCount;
					break;
				}
			}
		}

		return fillCount;
	}
	
	public int fillSmeltingAlchemyInputItem(LinkedList<CS_SMELTING_MAKE_REQ.Input> inputs) {
		int fillCount = 0;
		int size = inputs.size();
		for (L1ItemInstance item : getItems()) {
			if (fillCount >= size)
				break;

			if (item == null)
				continue;

			for (CS_SMELTING_MAKE_REQ.Input input : inputs) {
				if (input.hasRemoved())
					continue;

				if (input.isInput(item)) {
					input.setTemporaryRemovedItems(item);
					++fillCount;
					break;
				}
			}
		}

		return fillCount;
	}
	

	public ArrayList<L1ItemInstance> findItemIds(int id) {
		ArrayList<L1ItemInstance> list = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getItem().getItemId() == id) {
				list.add(item);
			}
		}
		return list;
	}
	
	public synchronized L1ItemInstance storeKeyItem(int id, int count, boolean isIden) {
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}
		
		if (temp.isStackable()) {
			L1ItemInstance item = new L1ItemInstance(temp, count);
			item.setIdentified(isIden);
			if (findItemId(id) == null) {
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = new L1ItemInstance(temp, 1);
			item.setIdentified(isIden);
			item.setId(IdFactory.getInstance().nextId());
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
			CommonUtil.SetTodayDeleteTime(item);
		}
		return result;
	}

}
