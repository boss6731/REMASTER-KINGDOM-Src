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
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.ItemInfo;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ADD_INVENTORY_NOTI;
 import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
 import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
 import l1j.server.server.model.item.smelting.SmeltingItemInfo;
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
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.storage.CharactersItemStorage;
 import l1j.server.server.templates.L1BookMark;
 import l1j.server.server.templates.L1Item;
 import l1j.server.tempSkillSystem.tempSkillSystemInfo;
 import l1j.server.tempSkillSystem.tempSkillSystemLoader;
















 public class L1PcInventory
         extends L1Inventory
 {
     private static final long serialVersionUID = 1L;
     private static final int MAX_SIZE = 200;
     private final L1PcInstance _owner;
     private int _arrowId;
     private int _stingId;
     private long timeVisible = 0L; private L1ItemInstance craftBoxOpenResult; public static final int COL_DURABILITY = 1; public static final int COL_IS_ID = 2; public static final int COL_ENCHANTLVL = 4; public static final int COL_EQUIPPED = 8; public static final int COL_COUNT = 16; public static final int COL_DELAY_EFFECT = 32; public static final int COL_ITEMID = 64; public static final int COL_CHARGE_COUNT = 128; public static final int COL_REMAINING_TIME = 256;
     private long timeVisibleDelay = 3000L; public static final int COL_BLESS = 512; public static final int COL_ATTRENCHANTLVL = 1024; public static final int COL_SPECIAL_ENCHANT = 2048; public static final int COL_SAVE_ALL = 4096; public static final int COL_BLESS_LEVEL = 8192; public static final int COL_SMELTING = 16384; public static final int COL_CARVING = 24;
     public static final int COL_DOLL_LEVEL = 8192;
     public static final int COL_DOLL_VALUE = 16384;
     private L1ItemInstance _arrow;

     public L1ItemInstance getCraftBoxOpenResult() {
         return this.craftBoxOpenResult;
     }
     public void setCraftBoxOpenResult(L1ItemInstance item) {
         this.craftBoxOpenResult = item;
     }

     public L1PcInventory(L1PcInstance owner) {
         this._owner = owner;
         this._arrowId = 0;
         this._stingId = 0;
     }


     public void setTimeVisible(long l) {
         this.timeVisible = l;
     }

     public L1PcInstance getOwner() {
         return this._owner;
     }

     public int getWeight100() {
         return calcWeight100(getWeight());
     }

     public int calcWeight100(int weight) {
         return weight * 100 / this._owner.getMaxWeight();
     }


     public int checkAddItem(L1Item item, int count) {
         int code = super.checkAddItem(item, count);
         if (code == 0) {
             int weight = getWeight() + item.getWeight() * count / 1000 + 1;
             if (calcWeight100(weight) >= 100)
                 return 2;
         }
         return code;
     }


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

         if (count < 0 || count > 2000000000) {
             return 3;
         }

         if (getSize() > 200 || (getSize() == 200 && (!item.isStackable() || !checkItem(item.getItem().getItemId())))) {
             if (message) {
                 sendOverMessage(263);
             }
             return 1;
         }

         int weight = getWeight() + item.getItem().getWeight() * count / 1000 + 1;
         if (weight < 0 || item.getItem().getWeight() * count / 1000 < 0) {
             if (message) {
                 sendOverMessage(82);
             }
             return 2;
         }
         if (calcWeight100(weight) >= 100) {
             if (message) {
                 sendOverMessage(82);
             }
             return 2;
         }

         L1ItemInstance itemExist = findItemId(item.getItemId()); // 查找是否已存在相同 ID 的物品
         if (itemExist != null && (itemExist.getCount() + count < 0 || itemExist.getCount() + count > 2000000000)) {
             // 如果物品存在且增加後的數量超過上限或小於 0
             if (message) {
                 getOwner().sendPackets((ServerBasePacket)new S_ServerMessage(166, "持有的金幣", "已超過 2,000,000,000。")); // 發送訊息通知玩家持有的亞丁幣已超過上限
             }
             return 3; // 返回 3 表示物品數量超過上限
         }

         return 0; // 返回 0 表示操作成功

     public int checkAddItem_doll(L1ItemInstance item, int count, boolean message, int doll_lvl, int doll_val) {
         if (item == null) {
             return -1;
         }

         if (count < 0 || count > 2000000000) {
             return 3;
         }

         if (getSize() > 200 || (getSize() == 200 && (!item.isStackable() || !checkItem(item.getItem().getItemId())))) {
             if (message) {
                 sendOverMessage(263);
             }
             return 1;
         }

         int weight = getWeight() + item.getItem().getWeight() * count / 1000 + 1;
         if (weight < 0 || item.getItem().getWeight() * count / 1000 < 0) {
             if (message) {
                 sendOverMessage(82);
             }
             return 2;
         }
         if (calcWeight100(weight) >= 100) {
             if (message) {
                 sendOverMessage(82);
             }
             return 2;
         }

             L1ItemInstance itemExist = findItemId(item.getItemId()); // 查找是否已存在相同 ID 的物品
             if (itemExist != null && (itemExist.getCount() + count < 0 || itemExist.getCount() + count > 2000000000)) {
// 如果物品存在且增加後的數量小於 0 或超過 2,000,000,000
                 if (message) {
                     getOwner().sendPackets((ServerBasePacket)new S_ServerMessage(166, "持有的 金幣", "已超過 2,000,000,000。")); // 發送訊息通知玩家持有的亞丁幣已超過上限
                 }
                 return 3; // 返回 3 表示物品數量超過上限
             }

             return 0; // 返回 0 表示操作成功

     public void sendOverMessage(int message_id) {
         this._owner.sendPackets((ServerBasePacket)new S_ServerMessage(message_id));
     }

     public void sendOptioon() {
         try {
             for (L1ItemInstance item : this._items) {
                 if (item.isEquipped()) {
                     item.setEquipped(false);
                     this._owner.getEquipSlot().removeSetItems(item.getItemId());








                     setEquipped(item, true, true, false, false);
                 }

             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }










































     public void loadItems() {
         try {
             CharactersItemStorage storage = CharactersItemStorage.create();

             for (L1ItemInstance item : storage.loadItems(this._owner.getId())) {
                 item._cha = this._owner;

                 if (item.getItemId() == 40308) {
                     L1ItemInstance itemExist = findItemId(item.getItemId());

                     if (itemExist != null) {
                         storage.deleteItem(item);

                         int newCount = itemExist.getCount() + item.getCount();

                         if (newCount <= 2000000000) {
                             if (newCount < 0) {
                                 newCount = 0;
                             }
                             itemExist.setCount(newCount);

                             storage.updateItemCount(itemExist);
                         }  continue;
                     }
                     this._items.add(item);
                     L1World.getInstance().storeObject((L1Object)item);
                     continue;
                 }
                 InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(item.getItemId());
                 L1FavorBookObject favor = L1FavorBookLoader.getFavor(item.getItemId());
                 tempSkillSystemInfo sinfo = tempSkillSystemLoader.getInstance().getTempSkillSystemInfo(item.getItemId());

                 if (favor != null && this._owner.getFavorBook().registFavor(favor, item)) {
                     if (info != null) {
                         InvenBonusItemInfo.inven_option(this._owner, item.getItemId(), true);
                     }
                     storage.deleteItem(item);
                     continue;
                 }
                 if (info != null) {
                     InvenBonusItemInfo.inven_option(this._owner, item.getItemId(), true);
                 }
                 if (sinfo != null) {
                     tempSkillSystemInfo.temp_skill(this._owner, item.getItemId(), true);
                 }
                 if (this._owner.getInventory().checkEquipped(item.getItemId()));



                 if (MJItemExpBonus.get_bonus_exp(item) > 1.0D && !this._owner._BonusExpItem) {
                     this._owner.add_item_exp_bonus(MJItemExpBonus.get_bonus_exp(item));
                     this._owner._BonusExpItem = true;
                 }

                 if (item.getItemId() == 4100500) {
                     if (!this._owner._PolyMasterCheck) {
                         this._owner.sendPackets((ServerBasePacket)new S_Ability(7, true));
                         this._owner.setPolyRingMaster(true);
                         this._owner._PolyMasterCheck = true;
                     } else if (this._owner._PolyMasterCheck &&
                             this._owner.isPolyRingMaster2()) {
                         this._owner.setPolyRingMaster(true);
                     }
                 }

                 if (item.getItemId() == 4100610) {
                     if (!this._owner._PolyMasterCheck) {
                         this._owner.sendPackets((ServerBasePacket)new S_Ability(7, true));
                         this._owner.setPolyRingMaster2(true);
                         this._owner._PolyMasterCheck = true;
                     } else if (this._owner._PolyMasterCheck &&
                             this._owner.isPolyRingMaster()) {
                         this._owner.setPolyRingMaster2(true);
                     }
                 }














                 this._items.add(item);
                 L1World.getInstance().storeObject((L1Object)item);




             }





         }
         catch (Exception e) {
             e.printStackTrace();
         }
         onGaho(false);
     }







     private boolean isGaho(int itemId) {
         if (itemId == 4100121) {
             return true;
         }



         return false;
     }

     private boolean hasGaho() {
         for (L1ItemInstance item : this._items) {
             if (isGaho(item.getItemId())) {
                 return true;
             }
         }
         return false;
     }

     private void onGaho(boolean deleted) {
         if (!this._owner._GahoCheck) {
             if (!deleted && hasGaho()) {
                 this._owner._GahoCheck = true;
                 this._owner.getResistance().addPVPweaponTotalDamage(3);
                 this._owner.addWeightReduction(500);
                 this._owner.sendPackets((ServerBasePacket)new S_OwnCharStatus(this._owner));
             }

         } else if (deleted && !hasGaho()) {
             this._owner._GahoCheck = false;
             this._owner.getResistance().addPVPweaponTotalDamage(-3);
             this._owner.addWeightReduction(-500);
             this._owner.sendPackets((ServerBasePacket)new S_OwnCharStatus(this._owner));
         }
     }



     public void insertItem(L1ItemInstance item) {
         item._cha = this._owner;
         if (MJItemExpBonus.get_bonus_exp(item) > 1.0D && !this._owner._BonusExpItem) {
             this._owner.add_item_exp_bonus(MJItemExpBonus.get_bonus_exp(item));
             this._owner._BonusExpItem = true;
         }

         if (!item.getItem().isEndedTimeMessage() && (
                 item.getEnchantLevel() > 0 || item.getAttrEnchantLevel() > 0)) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemStatus(item));
             this._owner.sendPackets((ServerBasePacket)new S_PacketBox(172, item));
         }


         if (item.getItemId() == 700024) {
             this._owner.sendPackets((ServerBasePacket)new S_PacketBox(142, item.getId(), "$13719", L1BookMark.ShowBookmarkitem(this._owner, item.getItemId())));
         }
         if (item.getItemId() == 700025) {
             this._owner.sendPackets((ServerBasePacket)new S_PacketBox(142, item.getId(), "$13719", L1BookMark.ShowBookmarkitem(this._owner, item.getItemId())));
         }
         if (item.getItemId() == 30001373 || item.getItemId() == 30001374) {
             Timestamp deltime = new Timestamp(System.currentTimeMillis() + 86400000L);
             item.setEndTime(deltime);
         }

         InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(item.getItemId());
         L1FavorBookObject favor = L1FavorBookLoader.getFavor(item.getItemId());
         tempSkillSystemInfo sinfo = tempSkillSystemLoader.getInstance().getTempSkillSystemInfo(item.getItemId());
         if (favor != null && this._owner.getFavorBook().registFavor(favor, item)) {
             if (info != null) {
                 InvenBonusItemInfo.inven_option(this._owner, item.getItemId(), true);
             }

             return;
         }
         if (info != null) {
             InvenBonusItemInfo.inven_option(this._owner, item.getItemId(), true);
         }
         if (sinfo != null) {
             tempSkillSystemInfo.temp_skill(this._owner, item.getItemId(), true);
         }

         try {
             CharactersItemStorage storage = CharactersItemStorage.create();
             storage.storeItem(this._owner.getId(), item);
         } catch (Exception e) {
             e.printStackTrace();
             e.printStackTrace();
         }

         if (item.getItemId() == 4100500) {
             if (!this._owner._PolyMasterCheck) {
                 this._owner.sendPackets((ServerBasePacket)new S_Ability(7, true));
                 this._owner.setPolyRingMaster(true);
                 this._owner._PolyMasterCheck = true;
             } else if (this._owner._PolyMasterCheck &&
                     this._owner.isPolyRingMaster2()) {
                 this._owner.setPolyRingMaster(true);
             }
         }


         if (item.getItemId() == 4100610) {
             if (!this._owner._PolyMasterCheck) {
                 this._owner.sendPackets((ServerBasePacket)new S_Ability(7, true));
                 this._owner.setPolyRingMaster2(true);
                 this._owner._PolyMasterCheck = true;
             } else if (this._owner._PolyMasterCheck &&
                     this._owner.isPolyRingMaster()) {
                 this._owner.setPolyRingMaster2(true);
             }
         }



         MJObjectEventProvider.provider().inventoryEventFactory().fireInventoryItemChanged(this._owner, item);

         if (item.getItemId() == 4100121) {
             L1SkillUse.on_icons(this._owner, 12000, -1);
             onGaho(false);
         }

         if (this._owner.isFishing()) {
             SC_ADD_INVENTORY_NOTI noti; switch (item.getItemId()) {
                 case 41296:
                     case 41297:
                         case 41301:
                             case 41303:
                                 case 41304:
                                     case 49092:
                                         case 49093:
                                             case 49094:
                                                 case 49095:
                                                     case 600230:
                                                         case 820018:
                                                             noti = SC_ADD_INVENTORY_NOTI.newInstance();
                                                             noti.add_item_info(ItemInfo.newInstance(this._owner, item));
                                                             this._owner.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
                                                             break;

                                                             default:
                                                                 noti = SC_ADD_INVENTORY_NOTI.newInstance();
                                                                 noti.add_item_info(ItemInfo.newInstance(this._owner, item));
                                                                 this._owner.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
                                                                 break;
             }

         } else {
             SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
             ItemInfo iInfo = ItemInfo.newInstance(this._owner, item);
             noti.add_item_info(iInfo);
             this._owner.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
         }
         this._items.add(item);
         if (item.getItem().getWeight() != 0) {
             this._owner.sendPackets((ServerBasePacket)new S_Weight(this._owner));
         }
     }


























     public void updateItem(L1ItemInstance item) {
         updateItem(item, 16);
         if (item.getItem().isToBeSavedAtOnce()) {
             saveItem(item, 16);
         }
     }










     public void updateItem(L1ItemInstance item, int column) {
         if (column >= 16384) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemStatus(item));
             this._owner.sendPackets((ServerBasePacket)new S_ItemColor(item));

             column -= 16384;
         }
         if (column >= 2048) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemName(item));
             column -= 2048;
         }
         if (column >= 8192) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemStatus(item));
             column -= 8192;
         }
         if (column >= 1024) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemStatus(item));
             this._owner.sendPackets((ServerBasePacket)new S_PacketBox(172, item));
             column -= 1024;
         }
         if (column >= 512) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemColor(item));
             column -= 512;
         }
         if (column >= 256) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemName(item));
             column -= 256;
         }
         if (column >= 128) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemName(item));
             column -= 128;
         }
         if (column >= 64) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemStatus(item));
             this._owner.sendPackets((ServerBasePacket)new S_ItemColor(item));
             this._owner.sendPackets((ServerBasePacket)new S_Weight(this._owner));
             column -= 64;
         }
         if (column >= 32) {
             column -= 32;
         }
         if (column >= 16) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemStatus(item));

             int weight = item.getWeight();
             if (weight != item.getLastWeight()) {
                 item.setLastWeight(weight);
                 this._owner.sendPackets((ServerBasePacket)new S_ItemStatus(item));
             } else {
                 this._owner.sendPackets((ServerBasePacket)new S_ItemName(item));
             }
             if (item.getItem().getWeight() != 0) {
                 this._owner.sendPackets((ServerBasePacket)new S_Weight(this._owner));
             }
             MJObjectEventProvider.provider().inventoryEventFactory().fireInventoryItemChanged(this._owner, item);
             column -= 16;
         }
         if (column >= 8) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemName(item));
             column -= 8;
         }
         if (column >= 4) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemStatus(item));
             this._owner.sendPackets((ServerBasePacket)new S_PacketBox(172, item));
             column -= 4;
         }
         if (column >= 2) {
             item._cha = this._owner;
             this._owner.sendPackets((ServerBasePacket)new S_ItemStatus(item));
             this._owner.sendPackets((ServerBasePacket)new S_ItemColor(item));
             column -= 2;
         }
         if (column >= 1) {
             this._owner.sendPackets((ServerBasePacket)new S_ItemStatus(item));
             column--;
         }
     }









     public void saveItem(L1ItemInstance item, int column) {
         if (column == 0) {
             return;
         }

         if (this._owner != null && this._owner.getAI() != null) {
             return;
         }
         try {
             CharactersItemStorage storage = CharactersItemStorage.create();

             if (column >= 4096) {
                 storage.updateItemAll(item);
                 return;
             }
             if (column >= 16384) {
                 storage.updateSmeltingValue(item);
                 column -= 16384;
             }

             if (column >= 2048) {
                 storage.updateSpecialEnchant(item);
                 column -= 2048;
             }
             if (column >= 1024) {
                 storage.updateItemAttrEnchantLevel(item);
                 column -= 1024;
             }
             if (column >= 8192) {
                 storage.updateItemBlessLevel(item);
                 column -= 8192;
             }
             if (column >= 512) {
                 storage.updateItemBless(item);
                 storage.updateBlessType(item);
                 storage.updateBlessTypeValue(item);
                 column -= 512;
             }
             if (column >= 256) {
                 storage.updateItemRemainingTime(item);
                 column -= 256;
             }
             if (column >= 128) {
                 storage.updateItemChargeCount(item);
                 column -= 128;
             }
             if (column >= 64) {
                 storage.updateItemId(item);
                 column -= 64;
             }
             if (column >= 32) {
                 storage.updateItemDelayEffect(item);
                 column -= 32;
             }
             if (column >= 16) {
                 storage.updateItemCount(item);
                 column -= 16;
             }
             if (column >= 8) {
                 storage.updateItemEquipped(item);
                 column -= 8;
             }
             if (column >= 4) {
                 storage.updateItemEnchantLevel(item);
                 column -= 4;
             }
             if (column >= 2) {
                 storage.updateItemIdentified(item);
                 storage.updateSupportItem(item);
                 column -= 2;
             }
             if (column >= 1) {
                 storage.updateItemDurability(item);
                 column--;
             }
             if (column >= 24) {
                 storage.updatecarving(item);
                 column -= 24;
             }
             if (column >= 8192) {
                 storage.updateDollLevel(item);
                 column -= 8192;
             }
             if (column >= 16384) {
                 storage.updateDollValue(item);
                 column -= 16384;
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }



     public void deleteItem(L1ItemInstance item) {
         InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(item.getItemId());
         L1FavorBookObject favor = L1FavorBookLoader.getFavor(item.getItemId());
         tempSkillSystemInfo sinfo = tempSkillSystemLoader.getInstance().getTempSkillSystemInfo(item.getItemId());
         if (info != null) {
             if (favor == null) {
                 InvenBonusItemInfo.inven_option(this._owner, item.getItemId(), false);
             } else {

                 InvenBonusItemInfo.inven_option(this._owner, item.getItemId(), true);
             }
         }
         if (sinfo != null) {
             tempSkillSystemInfo.temp_skill(this._owner, item.getItemId(), false);
         }

         if (MJItemExpBonus.get_bonus_exp(item) > 1.0D && this._owner._BonusExpItem) {
             this._owner.add_item_exp_bonus(-MJItemExpBonus.get_bonus_exp(item));
             this._owner._BonusExpItem = false;
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
         this._owner.sendPackets((ServerBasePacket)new S_DeleteInventoryItem(item));
         this._items.remove(item);
         if (item.getItem().getWeight() != 0) {
             this._owner.sendPackets((ServerBasePacket)new S_Weight(this._owner));
         }
         if (item.getItemId() == 4100500 && this._owner._PolyMasterCheck) {
             L1PolyMorph.undoPoly((L1Character)this._owner);
             if (!checkItem(4100500) && !checkItem(4100610)) {
                 this._owner.sendPackets((ServerBasePacket)new S_Ability(7, false));
                 this._owner.setPolyRingMaster(false);
                 this._owner._PolyMasterCheck = false;
                 this._owner.sendPackets((ServerBasePacket)new S_OwnCharStatus(this._owner));
             } else if (!checkItem(4100500) && checkItem(4100610)) {
                 this._owner.setPolyRingMaster(false);
                 this._owner.sendPackets((ServerBasePacket)new S_OwnCharStatus(this._owner));
             }
         }

         if (item.getItemId() == 4100610 && this._owner._PolyMasterCheck) {
             L1PolyMorph.undoPoly((L1Character)this._owner);
             if (!checkItem(4100610) && !checkItem(4100500)) {
                 this._owner.sendPackets((ServerBasePacket)new S_Ability(7, false));
                 this._owner.setPolyRingMaster2(false);
                 this._owner._PolyMasterCheck = false;
                 this._owner.sendPackets((ServerBasePacket)new S_OwnCharStatus(this._owner));
                 L1SkillUse.off_icons(this._owner, 80013);
             } else if (!checkItem(4100610) && checkItem(4100500)) {
                 this._owner.setPolyRingMaster2(false);
                 this._owner.sendPackets((ServerBasePacket)new S_OwnCharStatus(this._owner));
             }
         }


         if (item.getItemId() == 4100121 && this._owner._GahoCheck &&
                 !this._owner.getInventory().checkItem(4100121)) {
             this._owner.getResistance().addPVPweaponTotalDamage(-3);
             this._owner.getAbility().addSp(-2);
             this._owner.sendPackets((ServerBasePacket)new S_SPMR(this._owner));
             this._owner._GahoCheck = false;
             this._owner.sendPackets((ServerBasePacket)new S_OwnCharStatus(this._owner));
         }




         MJObjectEventProvider.provider().inventoryEventFactory().fireInventoryItemChanged(this._owner, item);


         if (item.getItemId() == 4100121 &&
                 !this._owner.getInventory().checkItem(4100121)) {
             L1SkillUse.off_icons(this._owner, 12000);
             onGaho(true);
         }
     }


     public L1ItemInstance getItemEquippend(int itemId) {
         L1ItemInstance equipeitem = null;
         L1ItemInstance item = null;
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
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
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
             if (item.getItem().getItemId() == itemId) {
                 equipeitem = item;
                 break;
             }
         }
         return equipeitem;
     }

     public void setEquipped(L1ItemInstance item, boolean equipped, boolean loaded, boolean changeWeapon, boolean shieldWeapon) {
         this._owner.sendPackets((ServerBasePacket)new S_OwnCharStatus(this._owner));
         if (item.isEquipped() != equipped) {
             L1Item temp = item.getItem();
             SmeltingItemInfo.smelting_option(this._owner, item, equipped);
             if (equipped) {
                 if ((temp.getItemId() == 20077 || temp.getItemId() == 20062 || temp.getItemId() == 120077) &&
                         System.currentTimeMillis() - this.timeVisible < this.timeVisibleDelay) {
                     return;
                 }


                 if (item.getItem().getType2() == 1 && item.getItem().getType() != 4 && this._owner.getInventory().getArrowItemId() != 0) {
                     L1ItemInstance arrow = this._owner.getInventory().findItemId(this._owner.getInventory().getArrowItemId());
                     if (arrow != null) {
                         this._owner.getInventory().setArrow(0);
                         this._owner.sendPackets((ServerBasePacket)new S_ArrowsEquipment(arrow));
                     }
                 }

                 if (item.getItem().getType2() == 1 && item.getItem().getType() != 10 && this._owner.getInventory().getStingItemId() != 0) {
                     L1ItemInstance arrow = this._owner.getInventory().findItemId(this._owner.getInventory().getStingItemId());
                     if (arrow != null) {
                         this._owner.getInventory().setSting(0);
                         this._owner.sendPackets((ServerBasePacket)new S_ArrowsEquipment(arrow));
                     }
                 }

                 item.setEquipped(true);

                 item.onEquip(this._owner);

                 this._owner.getEquipSlot().set(item);
                 int type = item.getItem().getType1();
                 if (type == 11 &&
                         this._owner.isPassive(MJPassiveID.SLAYER.toInt())) {
                     this._owner.addAttackDelayRate(10.0D);

                 }

             }
             else {


                 if (!loaded && (
                         temp.getItemId() == 20077 || temp.getItemId() == 20062 || temp.getItemId() == 120077)) {
                     if (this._owner.isInvisble()) {
                         this._owner.delInvis();
                     }

                     this.timeVisible = System.currentTimeMillis();
                 }

                 if (item.getItem().getType2() == 1 && item.getItem().getType1() == 4 &&
                         this._owner.hasSkillEffect(136)) {
                     this._owner.removeSkillEffect(136);
                 }

                 if (item.getItem().getType2() == 1 && item.getItem().getType1() == 24 &&
                         this._owner.hasSkillEffect(245)) {
                     this._owner.removeSkillEffect(245);
                 }



                 if (item.getItem().isTwohandedWeapon() &&
                         this._owner.hasSkillEffect(91)) {
                     this._owner.removeSkillEffect(91);
                     this._owner.sendPackets((ServerBasePacket)new S_PacketBox(180, 71, false));
                 }

                 if (item.getItem().getType2() == 1) {
                     this._owner.remove_elf_second_brave();
                     if (item.getItem().getType() == 4 && this._owner.getInventory().getArrowItemId() != 0) {
                         L1ItemInstance arrow = this._owner.getInventory().findItemId(this._owner.getInventory().getArrowItemId());
                         if (arrow != null) {
                             this._owner.getInventory().setArrow(0);
                             this._owner.sendPackets((ServerBasePacket)new S_ArrowsEquipment(arrow));
                         }
                     }

                     if (item.getItem().getType() == 10 && this._owner.getInventory().getStingItemId() != 0) {
                         L1ItemInstance arrow = this._owner.getInventory().findItemId(this._owner.getInventory().getStingItemId());
                         if (arrow != null) {
                             this._owner.getInventory().setSting(0);
                             this._owner.sendPackets((ServerBasePacket)new S_ArrowsEquipment(arrow));
                         }
                     }
                 }

                 item.setEquipped(false);
                 this._owner.getEquipSlot().remove(item);
                 item.onUnEquip();
                 int type = item.getItem().getType1();
                 if (type == 11 &&
                         this._owner.isPassive(MJPassiveID.SLAYER.toInt())) {
                     this._owner.addAttackDelayRate(-10.0D);
                 }
             }



             if (!loaded) {
                 this._owner.setCurrentHp(this._owner.getCurrentHp());
                 this._owner.setCurrentMp(this._owner.getCurrentMp());
                 updateItem(item, 8);
                 this._owner.sendPackets((ServerBasePacket)new S_OwnCharStatus(this._owner));
             }

             this._owner.getInventory().toSlotPacket(this._owner, item, false);
             if (item.getItem().getType2() == 1) {
                 this._owner.sendPackets((ServerBasePacket)new S_PacketBox(160, this._owner, item), true);
             }


             if (!loaded &&
                     temp.getType2() == 1 && !changeWeapon) {
                 this._owner.sendPackets((ServerBasePacket)new S_CharVisualUpdate(this._owner));
                 this._owner.broadcastPacket((ServerBasePacket)new S_CharVisualUpdate(this._owner));
             }
         }
     }


     public boolean checkEquipped(int id) {
         L1ItemInstance item = null;
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
             if (item.getItem().getItemId() == id && item.isEquipped()) {
                 return true;
             }
         }
         return false;
     }


             public int getNameEquipped(int type2, int type, String name) {
                 int equipeCount = 0; // 初始化裝備計數
                 L1ItemInstance item = null; // 初始化物品變數
                 String tName = null; // 初始化臨時名稱變數
                 String aName = null; // 初始化目標名稱變數
                 if (name.indexOf("祝福的") != -1) { // 如果名稱中包含 "祝福的"
                     aName = name.replace("祝福的 ", ""); // 去掉名稱中的 "祝福的"
                 } else {
                     aName = name; // 否則使用原名稱
                 }

                 for (L1ItemInstance itemObject : this._items) { // 遍歷所有物品
                     item = itemObject;
                     if (item.getItem().getType2() == type2 && item.getItem().getType() == type && item.isEquipped() &&
                             item != null) { // 如果物品的 type2 和 type 匹配，且物品已裝備
                         tName = item.getName(); // 獲取物品名稱
                         if (tName.indexOf("祝福的") != -1) { // 如果物品名稱中包含 "祝福的"
                             tName = tName.replace("祝福的 ", ""); // 去掉名稱中的 "祝福的"
                         }
                         if (tName.equals(aName)) { // 如果物品名稱匹配目標名稱
                             equipeCount++; // 裝備計數加一
                         }
                     }
                 }
                 return equipeCount; // 返回裝備計數
             }

     public int getTypeEquipped(int type2, int type) {
         int equipeCount = 0;
         L1ItemInstance item = null;
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
             if (item.getItem().getType2() == type2 && item.getItem().getType() == type && item.isEquipped()) {
                 equipeCount++;
             }
         }
         return equipeCount;
     }

     public L1ItemInstance getItemEquipped(int type2, int type) {
         L1ItemInstance equipeitem = null;
         L1ItemInstance item = null;
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
             if (item.getItem().getType2() == type2 && item.getItem().getType() == type && item.isEquipped()) {
                 equipeitem = item;
                 break;
             }
         }
         return equipeitem;
     }

     public L1ItemInstance[] getRingEquipped() {
         L1ItemInstance[] equipeItem = new L1ItemInstance[4];
         int equipeCount = 0;
         L1ItemInstance item = null;
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
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
         if (this._owner.getWeapon() == null) {
             return;
         }

         boolean takeoff = false;
         int weapon_type = this._owner.getWeapon().getItem().getType();
         takeoff = !L1PolyMorph.isEquipableWeapon(polyid, weapon_type);

         if (takeoff) {
             setEquipped(this._owner.getWeapon(), false, false, false, false);
         }

         if (this._owner.getSecondWeapon() != null) {
             boolean second_takeoff = false;
             int second_weapon_type = this._owner.getSecondWeapon().getItem().getType();
             second_takeoff = !L1PolyMorph.isEquipableWeapon(polyid, second_weapon_type);

             if (second_takeoff) {
                 setEquipped(this._owner.getSecondWeapon(), false, false, false, false);
             }
         }
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




     public L1ItemInstance getArrow() {
         if (this._owner.getAI() != null) {
             if (this._arrow == null) {
                 this._arrow = ItemTable.getInstance().createItem(3000516);
             }
             this._arrow.setCount(2);
             return this._arrow;
         }
         return getBullet(0);
     }




     public L1ItemInstance getSting() {
         return getBullet(15);
     }



     private L1ItemInstance getBullet(int type) {
         int priorityId = 0;
         if (type == 0) {
             priorityId = this._arrowId;
         }
         if (type == 15) {
             priorityId = this._stingId;
         }
         if (priorityId > 0) {
             L1ItemInstance bullet = findItemId(priorityId);
             if (bullet != null) {
                 return bullet;
             }
             if (type == 0) {
                 this._arrowId = 0;
             }
             if (type == 15) {
                 this._stingId = 0;
             }
         }


         for (L1ItemInstance itemObject : this._items) {
             L1ItemInstance bullet = itemObject;
             if (bullet.getItem().getType() == type) {
                 if (type == 0) {
                     this._arrowId = bullet.getItem().getItemId();
                 }
                 if (type == 15) {
                     this._stingId = bullet.getItem().getItemId();
                 }
                 this._owner.sendPackets((ServerBasePacket)new S_ArrowsEquipment(bullet));
                 this._owner.sendPackets((ServerBasePacket)new S_ServerMessage(452, bullet.getName()));
                 return bullet;
             }
         }
         return null;
     }

     public int getArrowItemId() {
         return this._arrowId;
     }

     public void setArrow(int id) {
         this._arrowId = id;
     }

     public int getStingItemId() {
         return this._stingId;
     }

     public void setSting(int id) {
         this._stingId = id;
     }

     public int hpRegenPerTick() {
         int hpr = 0;
         L1ItemInstance item = null;
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
             if (item.isEquipped()) {
                 hpr += item.getItem().get_addhpr();
             }
         }
         return hpr;
     }

     public int mpRegenPerTick() {
         int mpr = 0;
         L1ItemInstance item = null;
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
             if (item.isEquipped()) {
                 mpr += item.getItem().get_addmpr();
             }
         }
         return mpr;
     }

     public ArrayList<L1ItemInstance> getPossibleDropItems() {
         ArrayList<L1ItemInstance> possibles = new ArrayList<>(this._items.size());
         Object[] petlist = this._owner.getPetList().values().toArray();

         HashMap<Integer, Integer> impossibles = new HashMap<>();
         if (this._owner.get_companion() != null) {
             MJCompanionInstance companion = this._owner.get_companion();
             impossibles.put(Integer.valueOf(companion.get_control_object_id()), Integer.valueOf(companion.get_control_object_id()));
         }

         for (Object petObject : petlist) {
             if (petObject instanceof L1PetInstance) {
                 L1PetInstance pet = (L1PetInstance)petObject;
                 Integer itg = Integer.valueOf(pet.getItemObjId());
                 impossibles.put(itg, itg);
             }
         }

         L1DollInstance doll = this._owner.getMagicDoll();
         if (doll != null) {
             Integer itg = Integer.valueOf(doll.getItemObjId());
             impossibles.put(itg, itg);
         }

         for (L1ItemInstance item : this._items) {
             if (item.getItem().getItemId() == 40308 || !item.getItem().isTradable()) {
                 continue;
             }
             if (impossibles.containsKey(Integer.valueOf(item.getId()))) {
                 continue;
             }
             possibles.add(item);
         }
         return possibles;
     }


     public L1ItemInstance CaoPenalty() {
         Random random = new Random(System.nanoTime());
         int rnd = random.nextInt(this._items.size());
         L1ItemInstance penaltyItem = this._items.get(rnd);
         if (penaltyItem.getItem().getItemId() == 40308 || !penaltyItem.getItem().isTradable() || penaltyItem.get_Carving() == 1) {
             return null;
         }
         if (penaltyItem.get_Carving() != 0) {
             return null;
         }
         if (!MJCompanionInstanceCache.is_companion_oblivion(penaltyItem.getId())) {
             return null;
         }
         Object[] petlist = this._owner.getPetList().values().toArray();
         L1PetInstance pet = null;
         for (Object petObject : petlist) {
             if (petObject instanceof L1PetInstance) {
                 pet = (L1PetInstance)petObject;
                 if (penaltyItem.getId() == pet.getItemObjId()) {
                     return null;
                 }
             }
         }

         L1DollInstance doll = this._owner.getMagicDoll();
         if (doll != null &&
                 penaltyItem.getId() == doll.getItemObjId()) {
             return null;
         }


         setEquipped(penaltyItem, false);
         return penaltyItem;
     }









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
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
             if (item.getItem().getItemId() == id && item.getEnchantLevel() == enchant) {
                 return true;
             }
         }
         return false;
     }

     public boolean DeleteEnchant(int id, int enchant) {
         L1ItemInstance item = null;
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
             if (item.getItem().getItemId() == id && item.getEnchantLevel() == enchant) {
                 removeItem(item, 1);
                 return true;
             }
         }
         return false;
     }

     public int getEnchantCount(int id) {
         int cnt = 0;
         L1ItemInstance item = null;
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
             if (item.getItemId() == id) {
                 cnt = item.getEnchantLevel();
             }
         }
         return cnt;
     }

     public L1ItemInstance get_etc_itemid(int itemid) {
         Iterator<L1ItemInstance> iter = this._items.iterator();
         L1ItemInstance item = null;

         while (iter.hasNext()) {
             item = iter.next();
             if (item == null || item.getItem().getType2() != 0) {
                 continue;
             }
             if (item.getItemId() == itemid) {
                 return item;
             }
         }
         return null;
     }

     public L1ItemInstance get_set_item_eq(int itemid) {
         Iterator<L1ItemInstance> iter = this._items.iterator();
         L1ItemInstance item = null;

         while (iter.hasNext()) {
             item = iter.next();
             if (item == null || item.getItem().getType2() == 0)
                 continue;
             if (item.getItemId() == itemid && item.isEquipped()) {
                 return item;
             }
         }
         return null;
     }

     public int checkEquippedcount(int id) {
         int equipeCount = 0;
         L1ItemInstance item = null;
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
             if (item.getItem().getItemId() == id && item.isEquipped())
                 equipeCount++;
         }
         return equipeCount;
     }

     public boolean slotItemFind(L1PcInstance pc, int id, int itemobjId, int enchant, int bless, int attr) {
         for (L1ItemInstance item : this._items) {
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
         for (L1ItemInstance itemObject : this._items) {
             item = itemObject;
             if (item.getItemId() == id)
                 cnt += item.getCount();
         }
         return cnt;
     }

     public L1ItemInstance findItemObjId(int id) {
         for (L1ItemInstance item : this._items) {
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
         for (L1ItemInstance item : this._items) {
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
         if (find == null || find.getCount() < count) {
             return false;
         }
         removeItem(item, count);
         return true;
     }

     public boolean consumeItem(L1ItemInstance[] items, int[] counts) {
         int size = items.length; int i;
         for (i = size - 1; i >= 0; i--) {
             L1ItemInstance item = items[i];
             int count = counts[i];
             L1ItemInstance find = findItemObjId(item.getId());
             if (find == null || find.getCount() < count) {
                 return false;
             }
         }
         for (i = size - 1; i >= 0; i--) {
             removeItem(items[i], counts[i]);
         }
         return true;
     }


     public void setDollOn(int itemId, boolean equipped) {
         L1ItemInstance item = getItem(itemId);
         setDollOn(item, equipped, false, false, false);
     }

     public void setDollOn(L1ItemInstance item, boolean equipped, boolean loaded, boolean changeWeapon, boolean shieldWeapon) {
         if (item.isDollOn() != equipped)
             if (equipped) {
                 item.setDollOn(true);
             } else {
                 item.setDollOn(false);
             }
     }
 }


