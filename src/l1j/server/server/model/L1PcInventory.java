package l1j.server.server.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;



import l1j.server.InvenBonusItem.InvenBonusItemInfo;
import l1j.server.InvenBonusItem.InvenBonusItemLoader;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJExpAmpSystem.MJItemExpBonus;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.ItemInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ADD_INVENTORY_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SMELTING_UPDATE_SLOT_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SmeltingResult;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.CharacterFreeShieldTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
import l1j.server.server.model.item.smelting.SmeltingItemInfo;
import l1j.server.server.model.item.smelting.SmeltingScrollInfo;
import l1j.server.server.model.item.smelting.SmeltingScrollLoader;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_ArrowsEquipment;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_DeleteInventoryItem;
import l1j.server.server.serverpackets.S_ItemColor;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_ItemStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_Weight;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1FreeShield;
import l1j.server.server.templates.L1Item;
import l1j.server.tempSkillSystem.tempSkillSystemInfo;
import l1j.server.tempSkillSystem.tempSkillSystemLoader;

public class L1PcInventory extends L1Inventory {

	private static final long serialVersionUID = 1L;

	//private static final int MAX_SIZE = 180;
	private static final int MAX_SIZE = 200;

	private final L1PcInstance _owner;

	private int _arrowId;

	private int _stingId;

	private long timeVisible = 0;
	private long timeVisibleDelay = 3000;
	// private int _teleportRulerCount;


	private L1ItemInstance craftBoxOpenResult;
	public L1ItemInstance getCraftBoxOpenResult() {
		return craftBoxOpenResult;
	}
	public void setCraftBoxOpenResult(L1ItemInstance item) {
		craftBoxOpenResult = item;
	}

	public L1PcInventory(L1PcInstance owner) {
		_owner = owner;
		_arrowId = 0;
		_stingId = 0;
		// _teleportRulerCount = 0;
	}

	public void setTimeVisible(long l) {
		timeVisible = l;
	}

	public L1PcInstance getOwner() {
		return _owner;
	}

	public int getWeight100() {
		return calcWeight100(getWeight());
	}

	public int calcWeight100(int weight) {
		return weight * 100 / _owner.getMaxWeight();
	}

	@Override
	public int checkAddItem(L1Item item, int count) {
		int code = super.checkAddItem(item, count);
		if (code == OK) {
			int weight = getWeight() + item.getWeight() * count / 1000 + 1;
			if (calcWeight100(weight) >= 100)
				return WEIGHT_OVER;
		}
		return code;
	}

	@Override
	public int checkAddItem(L1ItemInstance item, int count) {
		return checkAddItem(item, count, true);
	}

	public int checkAddItem_doll(L1ItemInstance item, int count, int doll_lvl, int doll_val) {
		return checkAddItem_doll(item, count, true, doll_lvl, doll_val);
	}

	public int checkAddItem(L1ItemInstance item, int count, boolean message) {
		if (item == null) {
			return -1;
		}

		if (count < 0 || count > MAX_AMOUNT) {
			return AMOUNT_OVER;
		}

		if (getSize() > MAX_SIZE || (getSize() == MAX_SIZE && (!item.isStackable() || !checkItem(item.getItem().getItemId())))) {
			if (message) {
				sendOverMessage(263);
			}
			return SIZE_OVER;
		}

		int weight = getWeight() + item.getItem().getWeight() * count / 1000 + 1;
		if (weight < 0 || (item.getItem().getWeight() * count / 1000) < 0) {
			if (message) {
				sendOverMessage(82); // 物品太重，無法再持有。
			}
			return WEIGHT_OVER;
		}
		if (calcWeight100(weight) >= 100) {
			if (message) {
				sendOverMessage(82); // 物品太重，無法再持有。
			}
			return WEIGHT_OVER;
		}

		L1ItemInstance itemExist = findItemId(item.getItemId());
		if (itemExist != null && ((itemExist.getCount() + count) < 0 || (itemExist.getCount() + count) > MAX_AMOUNT)) {
			if (message) {
				getOwner().sendPackets(new S_ServerMessage(166, "持有的金幣", "超過了2,000,000,000。"));
			}
			return AMOUNT_OVER;
		}

		return OK;
	}

	public int checkAddItem_doll(L1ItemInstance item, int count, boolean message, int doll_lvl, int doll_val) {
		if (item == null) {
			return -1;
		}

		if (count < 0 || count > MAX_AMOUNT) {
			return AMOUNT_OVER;
		}

		if (getSize() > MAX_SIZE || (getSize() == MAX_SIZE && (!item.isStackable() || !checkItem(item.getItem().getItemId())))) {
			if (message) {
				sendOverMessage(263);
			}
			return SIZE_OVER;
		}

		int weight = getWeight() + item.getItem().getWeight() * count / 1000 + 1;
		if (weight < 0 || (item.getItem().getWeight() * count / 1000) < 0) {
			if (message) {
				sendOverMessage(82); // 物品太重，無法再持有。
			}
			return WEIGHT_OVER;
		}
		if (calcWeight100(weight) >= 100) {
			if (message) {
				sendOverMessage(82); // 物品太重，無法再持有。
			}
			return WEIGHT_OVER;
		}

		L1ItemInstance itemExist = findItemId(item.getItemId());
		if (itemExist != null && ((itemExist.getCount() + count) < 0 || (itemExist.getCount() + count) > MAX_AMOUNT)) {
			if (message) {
				getOwner().sendPackets(new S_ServerMessage(166, "持有的金幣", "超過了2,000,000,000。"));
			}
			return AMOUNT_OVER;
		}

		return OK;
	}

	public void sendOverMessage(int message_id) {
		_owner.sendPackets(new S_ServerMessage(message_id));
	}

	public void sendOptioon() {
		try {
			for (L1ItemInstance item : _items) {
				if (item.isEquipped()) {
					item.setEquipped(false);
					_owner.getEquipSlot().removeSetItems(item.getItemId());
					/*if (item.getItem().getType2() == 1) {
						if (_owner.getEquipSlot().getWeaponCount() >= 1) {
							if (!_owner.isPassive(MJPassiveID.SLAYER.toInt())) {
								// sendItemPacket(_owner, item);
								continue;
							}
						}
					}*/
					setEquipped(item, true, true, false, false);
				}
				//sendItemPacket(_owner, item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void sendOptioon() {
	// //List<ItemInfo> itemsInfo = new ArrayList<>(_items.size());
	// SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
	// noti.set_on_start(true);
	// //noti.set_owner_oid(_owner.getId());
	// try {
	// for (L1ItemInstance item : _items) {
	// noti.add_item_info(ItemInfo.newInstance(item));
	// if (item.isEquipped()) {
	// item.setEquipped(false);
	// _owner.getEquipSlot().removeSetItems(item.getItemId());
	// if (item.getItem().getType2() == 1) {
	// if (_owner.getEquipSlot().getWeaponCount() >= 1) {
	// if (!_owner.isPassive(MJPassiveID.SLAYER.toInt())) {
	// //noti.add_item_info(ItemInfo.newInstance(item));
	// //sendItemPacket(_owner, item);
	// continue;
	// }
	// }
	// }
	// setEquipped(item, true, true, false, false);
	// }
	// //noti.add_item_info(ItemInfo.newInstance(item));
	// //sendItemPacket(_owner, item);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// _owner.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
	/*
	 * for(L1ItemInstance item : _items) { if(item.getBless() != 1) { _owner.sendPackets(new S_ItemColor(item)); } }
	 */
	// }

	// 원본
	/*
	 * private static void sendItemPacket(L1PcInstance pc, L1ItemInstance item) { SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance(); noti.add_item_info(ItemInfo.newInstance(pc, item)); noti.set_on_start(true); noti.set_owner_oid(pc.getId());
	 * pc.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true); }
	 */

	@Override
	public void loadItems() {
		try {
			CharactersItemStorage storage = CharactersItemStorage.create();

			for (L1ItemInstance item : storage.loadItems(_owner.getId())) {
				item._cha = _owner;

				if (item.getItemId() == L1ItemId.ADENA) {
					L1ItemInstance itemExist = findItemId(item.getItemId());

					if (itemExist != null) {
						storage.deleteItem(item);

						int newCount = itemExist.getCount() + item.getCount();

						if (newCount <= MAX_AMOUNT) {
							if (newCount < 0) {
								newCount = 0;
							}
							itemExist.setCount(newCount);

							storage.updateItemCount(itemExist);
						}
					} else {
						_items.add(item);
						L1World.getInstance().storeObject(item);
					}
				} else {
					InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(item.getItemId());
					L1FavorBookObject favor = L1FavorBookLoader.getFavor(item.getItemId());//성물
					tempSkillSystemInfo sinfo = tempSkillSystemLoader.getInstance().getTempSkillSystemInfo(item.getItemId());

					if (favor != null && _owner.getFavorBook().registFavor(favor, item)){
						if (info != null) {
							InvenBonusItemInfo.inven_option(_owner, item.getItemId(), true);
						}
						storage.deleteItem(item);
						continue;
					}
					if (info != null) {
						InvenBonusItemInfo.inven_option(_owner, item.getItemId(), true);
					}
					if (sinfo != null) {
						tempSkillSystemInfo.temp_skill(_owner, item.getItemId(), true);
					}
					if (_owner.getInventory().checkEquipped(item.getItemId())){

					}

					if (MJItemExpBonus.get_bonus_exp(item) > 1 && !_owner._BonusExpItem) {
						_owner.add_item_exp_bonus(MJItemExpBonus.get_bonus_exp(item));
						_owner._BonusExpItem = true;
					}

					if (item.getItemId() == 4100500 ){
						if(!_owner._PolyMasterCheck) {
							_owner.sendPackets(new S_Ability(7, true));
							_owner.setPolyRingMaster(true);
							_owner._PolyMasterCheck = true;
						}else if(_owner._PolyMasterCheck) {
							if (_owner.isPolyRingMaster2()){
								_owner.setPolyRingMaster(true);
							}
						}
					}
					if (item.getItemId() == 4100610){
						if(!_owner._PolyMasterCheck) {
							_owner.sendPackets(new S_Ability(7, true));
							_owner.setPolyRingMaster2(true);
							_owner._PolyMasterCheck = true;
						} else if (_owner._PolyMasterCheck) {
							if (_owner.isPolyRingMaster()){
								_owner.setPolyRingMaster2(true);
							}
						}
					}



					/*
					 * if (item.getItemId() == 4100500 && !_owner._PolyMasterCheck) { _owner.sendPackets(new S_Ability(7, true)); _owner.sendPackets(new S_Ability(2, true)); _owner.setPolyRingMaster(true); _owner._PolyMasterCheck = true; _owner.sendPackets(new
					 * S_OwnCharStatus(_owner)); }
					 */
					/*
					 * if (item.getItemId() == 900111) { if (++_teleportRulerCount == 1) { _owner.setSkillEffect(L1SkillId.TELEPORT_RULER, -1); SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance(); noti.set_noti_type(eNotiType.NEW);
					 * noti.set_spell_id(L1SkillId.TELEPORT_RULER); noti.set_duration(1); noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT); noti.set_on_icon_id(L1SkillId.TELEPORT_RULER); noti.set_off_icon_id(0x00); noti.set_icon_priority(3);
					 * noti.set_tooltip_str_id(5119); noti.set_new_str_id(0); noti.set_end_str_id(0); noti.set_is_good(true); _owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI); } }
					 */

					_items.add(item);
					L1World.getInstance().storeObject(item);
				}
			}
/*			if (_owner.getAccount().getTotalFeatherCount() != 0) {
				int feacount = _owner.getAccount().getTotalFeatherCount();
				int count = _owner.getInventory().checkItemCount(41921);
				if (feacount > count) {
					_owner.getInventory().storeItem(41921, feacount - count, true);
				} else if (feacount < count) {
					_owner.getInventory().consumeItem(41921, count - feacount);
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		onGaho(false);
	}

	private boolean isGaho(int itemId) {
/*		L1FreeShield shield = CharacterFreeShieldTable.getInstance().getFreeShield(_owner.getAccount().getAccountId());
		if (_owner.isPcBuff()) {
			if (shield.get_Event_Gaho() > 0 || shield.get_Free_Gaho() >0 || shield.get_Event_Gaho() > 0) {
				return true;
			}
		}*/
		if (itemId == 4100121) {
			return true;
		}
		/*
		 * if (itemId == 4100529 && _owner.getLevel() <= Config.ServerAdSetting.NEWPLAYERLEVELPROTECTION) { return true; }
		 */
		return false;
	}

	private boolean hasGaho() {
		for (L1ItemInstance item : _items) {
			if (isGaho(item.getItemId())) {
				return true;
			}
		}
		return false;
	}

	private void onGaho(boolean deleted) {
		if (!_owner._GahoCheck) {
			if (!deleted && hasGaho()) {
				_owner._GahoCheck = true;
				_owner.getResistance().addPVPweaponTotalDamage(3);
				_owner.addWeightReduction(500);
				_owner.sendPackets(new S_OwnCharStatus(_owner));
			}
		} else {
			if (deleted && !hasGaho()) {
				_owner._GahoCheck = false;
				_owner.getResistance().addPVPweaponTotalDamage(-3);
				_owner.addWeightReduction(-500);
				_owner.sendPackets(new S_OwnCharStatus(_owner));
			}
		}
	}

	@Override
	public void insertItem(L1ItemInstance item) {
		item._cha = _owner;
		if (MJItemExpBonus.get_bonus_exp(item) > 1 && !_owner._BonusExpItem) {
			_owner.add_item_exp_bonus(MJItemExpBonus.get_bonus_exp(item));
			_owner._BonusExpItem = true;
		}

		if (!item.getItem().isEndedTimeMessage()) {
			if (item.getEnchantLevel() > 0 || item.getAttrEnchantLevel() > 0) {
				_owner.sendPackets(new S_ItemStatus(item));
				_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ENCHANT_UPDATE, item));
			}
		}

		if (item.getItemId() == 700024) {
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK, item.getId(), "$13719", L1BookMark.ShowBookmarkitem(_owner, item.getItemId())));
		}
		if (item.getItemId() == 700025) {
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK, item.getId(), "$13719", L1BookMark.ShowBookmarkitem(_owner, item.getItemId())));
		}
		if (item.getItemId() == 30001373 || item.getItemId() == 30001374){
			Timestamp deltime = new Timestamp(System.currentTimeMillis()+ 24*60*60*1000);
			item.setEndTime(deltime);
		}

		InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(item.getItemId());
		L1FavorBookObject favor = L1FavorBookLoader.getFavor(item.getItemId());//성물
		tempSkillSystemInfo sinfo = tempSkillSystemLoader.getInstance().getTempSkillSystemInfo(item.getItemId());
		if (favor != null && _owner.getFavorBook().registFavor(favor, item)) {
			if (info != null) {
				InvenBonusItemInfo.inven_option(_owner, item.getItemId(), true);
			}
			return;
		}

		if (info != null) {
			InvenBonusItemInfo.inven_option(_owner, item.getItemId(), true);
		}
		if (sinfo != null) {
			tempSkillSystemInfo.temp_skill(_owner, item.getItemId(), true);
		}

		try {
			CharactersItemStorage storage = CharactersItemStorage.create();
			storage.storeItem(_owner.getId(), item);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		}

		if (item.getItemId() == 4100500 ){
			if(!_owner._PolyMasterCheck) {
				_owner.sendPackets(new S_Ability(7, true));
				_owner.setPolyRingMaster(true);
				_owner._PolyMasterCheck = true;
			}else if(_owner._PolyMasterCheck) {
				if (_owner.isPolyRingMaster2()){
					_owner.setPolyRingMaster(true);
				}
			}
		}

		if (item.getItemId() == 4100610){
			if(!_owner._PolyMasterCheck) {
				_owner.sendPackets(new S_Ability(7, true));
				_owner.setPolyRingMaster2(true);
				_owner._PolyMasterCheck = true;
			} else if (_owner._PolyMasterCheck) {
				if (_owner.isPolyRingMaster()){
					_owner.setPolyRingMaster2(true);
				}
			}
		}


		MJObjectEventProvider.provider().inventoryEventFactory().fireInventoryItemChanged(_owner, item);
		// TODO 고급 불멸의 가호 중첩 소지시 효과 중첩 현상 효과 주석
		if (item.getItemId() == 4100121) {
			L1SkillUse.on_icons(_owner, L1SkillId.HIGH_CLASS_GAHO_BUFF, -1);
			onGaho(false);
		}

		if (_owner.isFishing()) {
			switch (item.getItemId()) {
				case 41297:
				case 41296:
				case 41301:
				case 41304:
				case 41303:
				case 600230:
				case 820018:
				case 49092:
				case 49093:
				case 49094:
				case 49095: {
					SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
					noti.add_item_info(ItemInfo.newInstance(_owner, item));
					_owner.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
				}
				break;
				default: {
					SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
					noti.add_item_info(ItemInfo.newInstance(_owner, item));
					_owner.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
				}
				break;
			}
		} else {
			SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
			ItemInfo iInfo = ItemInfo.newInstance(_owner, item);
			noti.add_item_info(iInfo);
			this._owner.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
		}
		_items.add(item);
		if (item.getItem().getWeight() != 0) {
			_owner.sendPackets(new S_Weight(_owner));
		}
	}



	public static final int COL_DURABILITY = 1;
	public static final int COL_IS_ID = 2;
	public static final int COL_ENCHANTLVL = 4;
	public static final int COL_EQUIPPED = 8;
	public static final int COL_COUNT = 16;
	public static final int COL_DELAY_EFFECT = 32;
	public static final int COL_ITEMID = 64;
	public static final int COL_CHARGE_COUNT = 128;
	public static final int COL_REMAINING_TIME = 256;
	public static final int COL_BLESS = 512;
	public static final int COL_ATTRENCHANTLVL = 1024;
	public static final int COL_SPECIAL_ENCHANT = 2048;
	public static final int COL_SAVE_ALL = 4096;
	public static final int COL_BLESS_LEVEL = 8192;
	public static final int COL_SMELTING = 16384;


	public static final int COL_CARVING = 24;

	public static final int COL_DOLL_LEVEL = 8192;
	public static final int COL_DOLL_VALUE = 16384;

	@Override
	public void updateItem(L1ItemInstance item) {
		updateItem(item, COL_COUNT);
		if (item.getItem().isToBeSavedAtOnce()) {
			saveItem(item, COL_COUNT);
		}
	}

	/**
	 * 목록내의 아이템 상태를 갱신한다.
	 *
	 * @param item
	 *            - 갱신 대상의 아이템
	 * @param column
	 *            - 갱신하는 스테이터스의 종류
	 */
	@Override
	public void updateItem(L1ItemInstance item, int column) {
		if (column >= COL_SMELTING){
			_owner.sendPackets(new S_ItemStatus(item));
			_owner.sendPackets(new S_ItemColor(item));
//			SC_SMELTING_UPDATE_SLOT_INFO_NOTI.send(_owner, item);
			column -= COL_SMELTING;
		}
		if (column >= COL_SPECIAL_ENCHANT) {
			_owner.sendPackets(new S_ItemName(item));
			column -= COL_SPECIAL_ENCHANT;
		}
		if (column >= COL_BLESS_LEVEL) {
			_owner.sendPackets(new S_ItemStatus(item));
			column -= COL_BLESS_LEVEL;
		}
		if (column >= COL_ATTRENCHANTLVL) {
			_owner.sendPackets(new S_ItemStatus(item));
			_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ENCHANT_UPDATE, item));
			column -= COL_ATTRENCHANTLVL;
		}
		if (column >= COL_BLESS) {
			_owner.sendPackets(new S_ItemColor(item));
			column -= COL_BLESS;
		}
		if (column >= COL_REMAINING_TIME) {
			_owner.sendPackets(new S_ItemName(item));
			column -= COL_REMAINING_TIME;
		}
		if (column >= COL_CHARGE_COUNT) {
			_owner.sendPackets(new S_ItemName(item));
			column -= COL_CHARGE_COUNT;
		}
		if (column >= COL_ITEMID) {
			_owner.sendPackets(new S_ItemStatus(item));
			_owner.sendPackets(new S_ItemColor(item));
			_owner.sendPackets(new S_Weight(_owner));
			column -= COL_ITEMID;
		}
		if (column >= COL_DELAY_EFFECT) {
			column -= COL_DELAY_EFFECT;
		}
		if (column >= COL_COUNT) {
			_owner.sendPackets(new S_ItemStatus(item));

			int weight = item.getWeight();
			if (weight != item.getLastWeight()) {
				item.setLastWeight(weight);
				_owner.sendPackets(new S_ItemStatus(item));
			} else {
				_owner.sendPackets(new S_ItemName(item));
			}
			if (item.getItem().getWeight() != 0) {
				_owner.sendPackets(new S_Weight(_owner));
			}
			MJObjectEventProvider.provider().inventoryEventFactory().fireInventoryItemChanged(_owner, item);
			column -= COL_COUNT;
		}
		if (column >= COL_EQUIPPED) {
			_owner.sendPackets(new S_ItemName(item));
			column -= COL_EQUIPPED;
		}
		if (column >= COL_ENCHANTLVL) {
			_owner.sendPackets(new S_ItemStatus(item));
			_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ENCHANT_UPDATE, item));
			column -= COL_ENCHANTLVL;
		}
		if (column >= COL_IS_ID) {
			item._cha = _owner;
			_owner.sendPackets(new S_ItemStatus(item));
			_owner.sendPackets(new S_ItemColor(item));
			column -= COL_IS_ID;
		}
		if (column >= COL_DURABILITY) {
			_owner.sendPackets(new S_ItemStatus(item));
			column -= COL_DURABILITY;
		}
	}

	/**
	 * 목록내의 아이템 상태를 DB에 보존한다.
	 *
	 * @param item
	 *            - 갱신 대상의 아이템
	 * @param column
	 *            - 갱신하는 스테이터스의 종류
	 */
	public void saveItem(L1ItemInstance item, int column) {
		if (column == 0) {
			return;
		}

		if (_owner != null && _owner.getAI() != null)
			return;

		try {
			CharactersItemStorage storage = CharactersItemStorage.create();

			if (column >= COL_SAVE_ALL) {
				storage.updateItemAll(item);
				return;
			}
			if (column >= COL_SMELTING){
				storage.updateSmeltingValue(item);
				column -= COL_SMELTING;
			}

			if (column >= COL_SPECIAL_ENCHANT) {
				storage.updateSpecialEnchant(item);
				column -= COL_SPECIAL_ENCHANT;
			}
			if (column >= COL_ATTRENCHANTLVL) {
				storage.updateItemAttrEnchantLevel(item);
				column -= COL_ATTRENCHANTLVL;
			}
			if (column >= COL_BLESS_LEVEL) {
				storage.updateItemBlessLevel(item);
				column -= COL_BLESS_LEVEL;
			}
			if (column >= COL_BLESS) {
				storage.updateItemBless(item);
				storage.updateBlessType(item);
				storage.updateBlessTypeValue(item);
				column -= COL_BLESS;
			}
			if (column >= COL_REMAINING_TIME) {
				storage.updateItemRemainingTime(item);
				column -= COL_REMAINING_TIME;
			}
			if (column >= COL_CHARGE_COUNT) {
				storage.updateItemChargeCount(item);
				column -= COL_CHARGE_COUNT;
			}
			if (column >= COL_ITEMID) {
				storage.updateItemId(item);
				column -= COL_ITEMID;
			}
			if (column >= COL_DELAY_EFFECT) {
				storage.updateItemDelayEffect(item);
				column -= COL_DELAY_EFFECT;
			}
			if (column >= COL_COUNT) {
				storage.updateItemCount(item);
				column -= COL_COUNT;
			}
			if (column >= COL_EQUIPPED) {
				storage.updateItemEquipped(item);
				column -= COL_EQUIPPED;
			}
			if (column >= COL_ENCHANTLVL) {
				storage.updateItemEnchantLevel(item);
				column -= COL_ENCHANTLVL;
			}
			if (column >= COL_IS_ID) {
				storage.updateItemIdentified(item);
				storage.updateSupportItem(item);
				column -= COL_IS_ID;
			}
			if (column >= COL_DURABILITY) {
				storage.updateItemDurability(item);
				column -= COL_DURABILITY;
			}
			if (column >= COL_CARVING) {
				storage.updatecarving(item);
				column -= COL_CARVING;
			}
			if (column >= COL_DOLL_LEVEL) {
				storage.updateDollLevel(item);
				column -= COL_DOLL_LEVEL;
			}
			if (column >= COL_DOLL_VALUE) {
				storage.updateDollValue(item);
				column -= COL_DOLL_VALUE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteItem(L1ItemInstance item) {

		InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(item.getItemId());
		L1FavorBookObject favor = L1FavorBookLoader.getFavor(item.getItemId());//성물
		tempSkillSystemInfo sinfo = tempSkillSystemLoader.getInstance().getTempSkillSystemInfo(item.getItemId());
		if (info != null) {
			if (favor == null){
				InvenBonusItemInfo.inven_option(_owner, item.getItemId(), false);
			}
			else {
				InvenBonusItemInfo.inven_option(_owner, item.getItemId(), true);
			}
		}
		if (sinfo != null) {
			tempSkillSystemInfo.temp_skill(_owner, item.getItemId(), false);
		}

		if (MJItemExpBonus.get_bonus_exp(item) > 1 && _owner._BonusExpItem) {
			_owner.add_item_exp_bonus(-MJItemExpBonus.get_bonus_exp(item));
			_owner._BonusExpItem = false;
		}

		try {
			CharactersItemStorage storage = CharactersItemStorage.create();
			storage.deleteItem(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (item.isEquipped()) {
			setEquipped(item, false);
		}
		_owner.sendPackets(new S_DeleteInventoryItem(item));
		_items.remove(item);
		if (item.getItem().getWeight() != 0) {
			_owner.sendPackets(new S_Weight(_owner));
		}
		if (item.getItemId() == 4100500 && _owner._PolyMasterCheck) {
			L1PolyMorph.undoPoly(_owner);
			if (!checkItem(4100500) && !checkItem(4100610)) {
				_owner.sendPackets(new S_Ability(7, false));
				_owner.setPolyRingMaster(false);
				_owner._PolyMasterCheck = false;
				_owner.sendPackets(new S_OwnCharStatus(_owner));
			} else if (!checkItem(4100500) && checkItem(4100610)){
				_owner.setPolyRingMaster(false);
				_owner.sendPackets(new S_OwnCharStatus(_owner));
			}
		}

		if (item.getItemId() == 4100610 && _owner._PolyMasterCheck) {
			L1PolyMorph.undoPoly(_owner);
			if (!checkItem(4100610) && !checkItem(4100500)) {
				_owner.sendPackets(new S_Ability(7, false));
				_owner.setPolyRingMaster2(false);
				_owner._PolyMasterCheck = false;
				_owner.sendPackets(new S_OwnCharStatus(_owner));
				L1SkillUse.off_icons(_owner, L1SkillId.POLY_RING_MASTER2);
			} else if (!checkItem(4100610) && checkItem(4100500)){
				_owner.setPolyRingMaster2(false);
				_owner.sendPackets(new S_OwnCharStatus(_owner));
			}
		}


		if (item.getItemId() == 4100121 && _owner._GahoCheck) {
			if (!_owner.getInventory().checkItem(4100121)) {
				_owner.getResistance().addPVPweaponTotalDamage(-3);
				_owner.getAbility().addSp(-2);
				_owner.sendPackets(new S_SPMR(_owner));
				_owner._GahoCheck = false;
				_owner.sendPackets(new S_OwnCharStatus(_owner));
			}
		}



		MJObjectEventProvider.provider().inventoryEventFactory().fireInventoryItemChanged(_owner, item);

		// TODO 고급 불멸의 가호 중첩 소지시 효과 중첩 현상 효과 주석
		if (item.getItemId() == 4100121) {
			if (!_owner.getInventory().checkItem(4100121)) {
				L1SkillUse.off_icons(_owner, L1SkillId.HIGH_CLASS_GAHO_BUFF);
				onGaho(true);
			}
		}
	}

	public L1ItemInstance getItemEquippend(int itemId) {// 아이템 착용 상태 확인의 오브젝트 인식
		L1ItemInstance equipeitem = null;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == itemId && item.isEquipped()) {
				equipeitem = item;
				break;
			}
		}
		return equipeitem;
	}

	public void setEquipped(L1ItemInstance item, boolean equipped) {
		setEquipped(item, equipped, false, false, false);
	}

	public L1ItemInstance getEquippedItem(int itemId) {
		L1ItemInstance equipeitem = null;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == itemId) {
				equipeitem = item;
				break;
			}
		}
		return equipeitem;
	}

	public void setEquipped(final L1ItemInstance item, final boolean equipped, final boolean loaded, final boolean changeWeapon, final boolean shieldWeapon) {
		_owner.sendPackets(new S_OwnCharStatus(_owner));
		if (item.isEquipped() != equipped) {
			L1Item temp = item.getItem();
			SmeltingItemInfo.smelting_option(_owner, item, equipped);
			if (equipped) {
				if (temp.getItemId() == 20077 || temp.getItemId() == 20062 || temp.getItemId() == 120077) {
					if (System.currentTimeMillis() - timeVisible < timeVisibleDelay) {
						return;
					}
				}

				if (item.getItem().getType2() == 1 && item.getItem().getType() != 4 && _owner.getInventory().getArrowItemId() != 0) {
					L1ItemInstance arrow = _owner.getInventory().findItemId(_owner.getInventory().getArrowItemId());
					if (arrow != null) {
						_owner.getInventory().setArrow(0);
						_owner.sendPackets(new S_ArrowsEquipment(arrow));
					}
				}

				if (item.getItem().getType2() == 1 && item.getItem().getType() != 10 && _owner.getInventory().getStingItemId() != 0) {
					L1ItemInstance arrow = _owner.getInventory().findItemId(_owner.getInventory().getStingItemId());
					if (arrow != null) {
						_owner.getInventory().setSting(0);
						_owner.sendPackets(new S_ArrowsEquipment(arrow));
					}
				}

				item.setEquipped(true);
				// TODO 마법 아이콘 안보이는 현상 순서변경 10/17
				item.onEquip(_owner);
				// TODO 마법 아이콘 안보이는 현상 순서변경 10/17
				_owner.getEquipSlot().set(item);
				int type = item.getItem().getType1();
				if (type == 11) {
					if (_owner.isPassive(MJPassiveID.SLAYER.toInt())) {
						_owner.addAttackDelayRate(10);
					}
				}




			} else {
				if (!loaded) {
					if (temp.getItemId() == 20077 || temp.getItemId() == 20062 || temp.getItemId() == 120077) {
						if (_owner.isInvisble()) {
							_owner.delInvis();
							// return;
						}
						timeVisible = System.currentTimeMillis();
					}
				}
				if (item.getItem().getType2() == 1 && item.getItem().getType1() == 4) {
					if (_owner.hasSkillEffect(L1SkillId.INFERNO)) {
						_owner.removeSkillEffect(L1SkillId.INFERNO);
					}
				}
				if (item.getItem().getType2() == 1 && item.getItem().getType1() == 24) {
					if (_owner.hasSkillEffect(L1SkillId.HALPAS)) {
						_owner.removeSkillEffect(L1SkillId.HALPAS);
					}
				}

				// 양손검을 착용해제 했을때 카운터배리어 효과해제
				if (item.getItem().isTwohandedWeapon()) {
					if (_owner.hasSkillEffect(L1SkillId.COUNTER_BARRIER)) {
						_owner.removeSkillEffect(L1SkillId.COUNTER_BARRIER);
						_owner.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 71, false));
					}
				}
				if (item.getItem().getType2() == 1) {
					_owner.remove_elf_second_brave();
					if (item.getItem().getType() == 4 && _owner.getInventory().getArrowItemId() != 0) {
						L1ItemInstance arrow = _owner.getInventory().findItemId(_owner.getInventory().getArrowItemId());
						if (arrow != null) {
							_owner.getInventory().setArrow(0);
							_owner.sendPackets(new S_ArrowsEquipment(arrow));
						}
					}

					if (item.getItem().getType() == 10 && _owner.getInventory().getStingItemId() != 0) {
						L1ItemInstance arrow = _owner.getInventory().findItemId(_owner.getInventory().getStingItemId());
						if (arrow != null) {
							_owner.getInventory().setSting(0);
							_owner.sendPackets(new S_ArrowsEquipment(arrow));
						}
					}
				}

				item.setEquipped(false);
				_owner.getEquipSlot().remove(item);
				item.onUnEquip();
				int type = item.getItem().getType1();
				if (type == 11) {
					if (_owner.isPassive(MJPassiveID.SLAYER.toInt())) {
						_owner.addAttackDelayRate(-10);
					}
				}

			}

			if (!loaded) {
				_owner.setCurrentHp(_owner.getCurrentHp());
				_owner.setCurrentMp(_owner.getCurrentMp());
				updateItem(item, COL_EQUIPPED);
				_owner.sendPackets(new S_OwnCharStatus(_owner));
			}
			// 아이템 착용 처리에 대한 패킷 처리.
			_owner.getInventory().toSlotPacket(_owner, item, false);
			if (item.getItem().getType2() == 1) {
				_owner.sendPackets(new S_PacketBox(S_PacketBox.공격가능거리, _owner, item), true);
			}

			// 아이템패킷추가
			if (!loaded) {
				if (temp.getType2() == 1 && changeWeapon == false) {
					_owner.sendPackets(new S_CharVisualUpdate(_owner));
					_owner.broadcastPacket(new S_CharVisualUpdate(_owner));
				}
			}
		}
	}

	public boolean checkEquipped(int id) {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == id && item.isEquipped()) {
				return true;
			}
		}
		return false;
	}

	/** 若物品名稱前有'祝福的'字樣，則移除之後進行物品名稱比對。 **/
	public int getNameEquipped(int type2, int type, String name) {
		int equipeCount = 0;
		L1ItemInstance item = null;
		String tName = null;
		String aName = null;
		if (name.indexOf("祝福的") != -1) {
			aName = name.replace("祝福的 ", "");
		} else {
			aName = name;
		}

		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getType2() == type2 && item.getItem().getType() == type && item.isEquipped()) {
				if (item != null) {
					tName = item.getName();
					if (tName.indexOf("祝福的") != -1)
						tName = tName.replace("祝福的 ", "");
					if (tName.equals(aName))
						equipeCount++;
				}
			}
		}
		return equipeCount;
	}

	public int getTypeEquipped(int type2, int type) {
		int equipeCount = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getType2() == type2 && item.getItem().getType() == type && item.isEquipped()) {
				equipeCount++;
			}
		}
		return equipeCount;
	}

	public L1ItemInstance getItemEquipped(int type2, int type) {
		L1ItemInstance equipeitem = null;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getType2() == type2 && item.getItem().getType() == type && item.isEquipped()) {
				equipeitem = item;
				break;
			}
		}
		return equipeitem;
	}

	public L1ItemInstance[] getRingEquipped() {
		L1ItemInstance equipeItem[] = new L1ItemInstance[4];
		int equipeCount = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getType2() == 2 && item.getItem().getType() == 9 && item.isEquipped()) {
				equipeItem[equipeCount] = item;
				equipeCount++;
				if (equipeCount == 4) {
					break;
				}
			}
		}
		return equipeItem;
	}

	public void takeoffEquip(int polyid) {
		takeoffWeapon(polyid);
		takeoffArmor(polyid);
	}

	private void takeoffWeapon(int polyid) {
		if (_owner.getWeapon() == null) {
			return;
		}

		boolean takeoff = false;
		int weapon_type = _owner.getWeapon().getItem().getType();
		takeoff = !L1PolyMorph.isEquipableWeapon(polyid, weapon_type);

		if (takeoff) {
			setEquipped(_owner.getWeapon(), false, false, false, false);
		}

		if (_owner.getSecondWeapon() != null) {// 변신버그추가
			boolean second_takeoff = false;
			int second_weapon_type = _owner.getSecondWeapon().getItem().getType();
			second_takeoff = !L1PolyMorph.isEquipableWeapon(polyid, second_weapon_type);

			if (second_takeoff) {
				setEquipped(_owner.getSecondWeapon(), false, false, false, false);
			}
		} // 변신버그추가

	}

	private void takeoffArmor(int polyid) {
		L1ItemInstance armor = null;

		for (int type = 0; type <= 12; type++) {
			if (getTypeEquipped(2, type) != 0 && !L1PolyMorph.isEquipableArmor(polyid, type)) {
				if (type == 9) {
					armor = getItemEquipped(2, type);
					if (armor != null) {
						setEquipped(armor, false, false, false, false);
					}
					armor = getItemEquipped(2, type);
					if (armor != null) {
						setEquipped(armor, false, false, false, false);
					}
				} else {
					armor = getItemEquipped(2, type);
					if (armor != null) {
						setEquipped(armor, false, false, false, false);
					}
				}
			}
		}
	}

	/** 로봇시스템 **/
	private L1ItemInstance _arrow;

	public L1ItemInstance getArrow() {
		if (_owner.getAI() != null) {
			if (_arrow == null) {
				_arrow = ItemTable.getInstance().createItem(3000516);
			}
			_arrow.setCount(2);
			return _arrow;
		} else {
			return getBullet(0);
		}
	}

	/** 로봇시스템 **/

	public L1ItemInstance getSting() {
		return getBullet(15);
	}

	// 스팅 타입수정
	private L1ItemInstance getBullet(int type) {
		L1ItemInstance bullet;
		int priorityId = 0;
		if (type == 0) {
			priorityId = _arrowId;
		}
		if (type == 15) {
			priorityId = _stingId;
		}
		if (priorityId > 0) {
			bullet = findItemId(priorityId);
			if (bullet != null) {
				return bullet;
			} else {
				if (type == 0) {
					_arrowId = 0;
				}
				if (type == 15) {
					_stingId = 0;
				}
			}
		}

		for (Object itemObject : _items) {
			bullet = (L1ItemInstance) itemObject;
			if (bullet.getItem().getType() == type) {
				if (type == 0) {
					_arrowId = bullet.getItem().getItemId();
				}
				if (type == 15) {
					_stingId = bullet.getItem().getItemId();
				}
				_owner.sendPackets(new S_ArrowsEquipment(bullet));
				_owner.sendPackets(new S_ServerMessage(452, bullet.getName()));
				return bullet;
			}
		}
		return null;
	}

	public int getArrowItemId() {
		return _arrowId;
	}

	public void setArrow(int id) {
		_arrowId = id;
	}

	public int getStingItemId() {
		return _stingId;
	}

	public void setSting(int id) {
		_stingId = id;
	}

	public int hpRegenPerTick() {
		int hpr = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.isEquipped()) {
				hpr += item.getItem().get_addhpr();
			}
		}
		return hpr;
	}

	public int mpRegenPerTick() {
		int mpr = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.isEquipped()) {
				mpr += item.getItem().get_addmpr();
			}
		}
		return mpr;
	}

	public ArrayList<L1ItemInstance> getPossibleDropItems() {
		ArrayList<L1ItemInstance> possibles = new ArrayList<L1ItemInstance>(_items.size());
		Object[] petlist = _owner.getPetList().values().toArray();

		HashMap<Integer, Integer> impossibles = new HashMap<Integer, Integer>();
		if (_owner.get_companion() != null) {
			MJCompanionInstance companion = _owner.get_companion();
			impossibles.put(companion.get_control_object_id(), companion.get_control_object_id());
		}

		for (Object petObject : petlist) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				Integer itg = pet.getItemObjId();
				impossibles.put(itg, itg);
			}
		}

		L1DollInstance doll = _owner.getMagicDoll();
		if (doll != null) {
			Integer itg = doll.getItemObjId();
			impossibles.put(itg, itg);
		}

		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemId() == L1ItemId.ADENA || !item.getItem().isTradable()) {
				continue;
			}
			if (impossibles.containsKey(item.getId())) {
				continue;
			}
			possibles.add(item);
		}
		return possibles;
	}

	// 해당아이템은 드랍불가
	public L1ItemInstance CaoPenalty() {
		Random random = new Random(System.nanoTime());
		int rnd = random.nextInt(_items.size());
		L1ItemInstance penaltyItem = _items.get(rnd);
		if (penaltyItem.getItem().getItemId() == L1ItemId.ADENA || !penaltyItem.getItem().isTradable() || penaltyItem.get_Carving() == 1) {
			return null;
		}
		if (penaltyItem.get_Carving() != 0) {
			return null;
		}
		if (!MJCompanionInstanceCache.is_companion_oblivion(penaltyItem.getId()))
			return null;

		Object[] petlist = _owner.getPetList().values().toArray();
		L1PetInstance pet = null;
		for (Object petObject : petlist) {
			if (petObject instanceof L1PetInstance) {
				pet = (L1PetInstance) petObject;
				if (penaltyItem.getId() == pet.getItemObjId()) {
					return null;
				}
			}
		}

		L1DollInstance doll = _owner.getMagicDoll();
		if (doll != null) {
			if (penaltyItem.getId() == doll.getItemObjId()) {
				return null;
			}
		}

		setEquipped(penaltyItem, false);
		return penaltyItem;
	}

	/**
	 * 造神的石巨人（刪除附魔物品）
	 *
	 * @param itemid
	 *            - 鍛造所需的武器編號
	 * @param enchantLevel
	 *            - 鍛造所需的武器附魔等級
	 */
	public boolean MakeDeleteEnchant(int itemid, int enchantLevel) {
		L1ItemInstance[] items = findItemsId(itemid);

		for (L1ItemInstance item : items) {
			if (item.getEnchantLevel() == enchantLevel) {
				removeItem(item, 1);
				return true;
			}
		}
		return false;
	}

	/**
	 * 造神的石巨人（檢查附魔物品）
	 *
	 * @param id
	 *            - 鍛造所需的武器編號
	 *
	 * @param enchantLevel
	 *            - 鍛造所需的武器附魔等級
	 *
	 */
	public boolean MakeCheckEnchant(int id, int enchantLevel) {
		L1ItemInstance[] items = findItemsId(id);

		for (L1ItemInstance item : items) {
			if (item.getEnchantLevel() == enchantLevel && item.getCount() == 1) {
				return true;
			}
		}

		return false;
	}

	public boolean checkEnchant(int id, int enchant) {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == id && item.getEnchantLevel() == enchant) {
				return true;
			}
		}
		return false;
	}

	public boolean DeleteEnchant(int id, int enchant) {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == id && item.getEnchantLevel() == enchant) {
				removeItem(item, 1);
				return true;
			}
		}
		return false;
	}

	public int getEnchantCount(int id) {// 인첸 레벨
		int cnt = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItemId() == id) {
				cnt = item.getEnchantLevel();
			}
		}
		return cnt;
	}

	public L1ItemInstance get_etc_itemid(int itemid) {
		Iterator<L1ItemInstance> iter = _items.iterator();
		L1ItemInstance item = null;

		while (iter.hasNext()) {
			item = iter.next();
			if (item == null || item.getItem().getType2() != 0) { // -- 잡화만 검색
				continue;
			}
			if (item.getItemId() == itemid) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance get_set_item_eq(int itemid) {
		Iterator<L1ItemInstance> iter = _items.iterator();
		L1ItemInstance item = null;

		while (iter.hasNext()) {
			item = iter.next();
			if (item == null || item.getItem().getType2() == 0)
				continue; // -- 잡화는 검색 조건에서 제외
			if (item.getItemId() == itemid && item.isEquipped()) {
				return item;
			}
		}
		return null;
	}

	public int checkEquippedcount(int id) {
		int equipeCount = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == id && item.isEquipped())
				equipeCount++;
		}
		return equipeCount;
	}

	public boolean slotItemFind(L1PcInstance pc, int id, int itemobjId, int enchant, int bless, int attr) {
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id && item.getId() == itemobjId && item.getEnchantLevel() == enchant && item.getBless() == bless && item.getAttrEnchantLevel() == attr) {
				pc.getInventory().setEquipped(item, true);
				return true;
			}
		}
		return false;
	}

	public int checkItemCount(int id) {
		int cnt = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItemId() == id)
				cnt += item.getCount();
		}
		return cnt;
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

	public boolean checkEquipped(int[] ids) {
		for (int id : ids) {
			if (!checkEquipped(id)) {
				return false;
			}
		}
		return true;
	}

	public boolean checkEquippedAtOnce(int[] ids) {
		for (L1ItemInstance item : _items) {
			int itemId = item.getItemId();
			for (int id : ids) {
				if (id == itemId && item.isEquipped())
					return true;
			}
		}
		return false;
	}

	public boolean consumeItem(L1ItemInstance item, int count) {
		L1ItemInstance find = findItemObjId(item.getId());
		if (find == null || find.getCount() < count)
			return false;

		removeItem(item, count);
		return true;
	}

	public boolean consumeItem(L1ItemInstance[] items, int[] counts) {
		int size = items.length;
		for (int i = size - 1; i >= 0; --i) {
			L1ItemInstance item = items[i];
			int count = counts[i];
			L1ItemInstance find = findItemObjId(item.getId());
			if (find == null || find.getCount() < count)
				return false;
		}

		for (int i = size - 1; i >= 0; --i) {
			removeItem(items[i], counts[i]);
		}
		return true;
	}

	/** 인형 착용 여부 **/
	public void setDollOn(int itemId, boolean equipped) {
		L1ItemInstance item = getItem(itemId);
		setDollOn(item, equipped, false, false, false);
	}

	public void setDollOn(L1ItemInstance item, boolean equipped, boolean loaded, boolean changeWeapon, boolean shieldWeapon) {
		if (item.isDollOn() != equipped) {
			if (equipped) {
				item.setDollOn(true);
			} else {
				item.setDollOn(false);
			}
		}
	}

}