 package l1j.server.server.model.shop;

 import java.sql.Timestamp;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Random;
 import l1j.server.Config;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_POINT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_OBTAINED_ITEM_INFO;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_CONTRIBUTION_ACK;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.MJWarSystem.MJCastleWar;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.NpcShopCash.NpcShopCashTable;
 import l1j.server.server.Controller.BugRaceController;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.NpcCashShopTable;
 import l1j.server.server.datatables.NpcShopTable;
 import l1j.server.server.datatables.NpcShopTable2;
 import l1j.server.server.datatables.NpcShopTable3;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.datatables.ShopBuyLimitInfo;
 import l1j.server.server.datatables.ShopTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1CastleLocation;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1PcInventory;
 import l1j.server.server.model.L1TaxCalculator;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.Warehouse.SupplementaryService;
 import l1j.server.server.model.Warehouse.WarehouseManager;
 import l1j.server.server.serverpackets.S_ACTION_UI;
 import l1j.server.server.serverpackets.S_Disconnect;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SurvivalCry;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.templates.L1ShopItem;
 import l1j.server.server.templates.ShopBuyLimit;
 import l1j.server.server.templates.eShopBuyLimitType;
 import l1j.server.server.utils.CommonUtil;
 import l1j.server.server.utils.IntRange;

 public class L1Shop
 {
   private final int _npcId;
   private final List<L1ShopItem> _sellingItems;
   private final List<L1ShopItem> _purchasingItems;

   public static boolean is_normal_shop(L1NpcInstance npc) {
     return (ShopTable.getInstance().get(npc.getNpcId()) != null);
   }

     // 定義靜態方法 find_shop，用於查找 NPC 對應的商店
     public static L1Shop find_shop(L1NpcInstance npc) {
// 獲取 NPC 的 ID
         int npcId = npc.getNpcTemplate().get_npcId();
         L1Shop shop = null;

// 如果 NPC 是 L1NpcShopInstance 的實例
         if (npc instanceof l1j.server.server.model.Instance.L1NpcShopInstance) {
// 從不同的商店表中獲取商店
             shop = NpcShopTable.getInstance().get(npcId);
             if (shop == null)
                 shop = NpcShopTable2.getInstance().get(npcId);
             if (shop == null)
                 shop = NpcShopTable3.getInstance().get(npcId);

// 如果 NPC 是 L1NpcCashShopInstance 的實例
         } else if (npc instanceof l1j.server.server.model.Instance.L1NpcCashShopInstance) {
// 從現金商店表中獲取商店
             shop = NpcCashShopTable.getInstance().get(npcId);

// 否則，從普通商店表中獲取商店
         } else {
             shop = ShopTable.getInstance().get(npcId);
         }

// 如果未找到商店，打印錯誤信息
         if (shop == null)
             System.out.println("NPC 商店錯誤：編號" + npc.getNpcId() + " x :" + npc.getX() + " y :" + npc.getY() + " 地圖 :" + npc.getMapId());

// 返回找到的商店
         return shop;
     }


   private static final HashMap<Integer, Integer> _checkbox_rules = new HashMap<>(); static {
     _checkbox_rules.put(Integer.valueOf(4100251), Integer.valueOf(4100251));
     _checkbox_rules.put(Integer.valueOf(4100252), Integer.valueOf(4100252));
     _checkbox_rules.put(Integer.valueOf(4100253), Integer.valueOf(4100253));
   }

   public L1Shop(int npcId, List<L1ShopItem> sellingItems, List<L1ShopItem> purchasingItems) {
     if (sellingItems == null || purchasingItems == null) {
       throw new NullPointerException();
     }
     this._npcId = npcId;
     this._sellingItems = sellingItems;
     this._purchasingItems = purchasingItems;
   }

   public int getNpcId() {
     return this._npcId;
   }

   public List<L1ShopItem> getSellingItems() {
     return this._sellingItems;
   }

   public List<L1ShopItem> getBuyingItems() {
     return this._purchasingItems;
   }

   private boolean isPurchaseableItem(L1ItemInstance item) {
     if (item == null) {
       return false;
     }
     if (item.isEquipped()) {
       return false;
     }
     if (item.getBless() >= 128) {
       return false;
     }
     if (item.get_Carving() != 0) {
       return false;
     }

     if (item.isDollOn()) {
       return false;
     }
     return true;
   }

   public L1ShopItem getSellItem(int itemid) {
     for (L1ShopItem a : this._sellingItems) {
       if (a.getItemId() == itemid) {
         return a;
       }
     }
     return null;
   }

   public L1ShopItem getBuyItem(int itemid) {
     for (L1ShopItem a : this._purchasingItems) {
       if (a.getItemId() == itemid) {
         return a;
       }
     }
     return null;
   }

   public boolean isSellingItem(int itemid) {
     for (L1ShopItem a : this._sellingItems) {
       if (a.getItemId() == itemid) {
         return true;
       }
     }
     return false;
   }

   private L1ShopItem getPurchasingItem(int itemId, int enchant) {
     for (L1ShopItem shopItem : this._purchasingItems) {
       if (shopItem.getItemId() == itemId && shopItem.getEnchant() == enchant) {
         return shopItem;
       }
     }
     return null;
   }

   public L1ShopItem getSellingItem(int itemId) {
     for (L1ShopItem shopItem : this._sellingItems) {
       if (shopItem.getItemId() == itemId) {
         return shopItem;
       }
     }
     return null;
   }

   public L1AssessedItem assessItem(L1ItemInstance item) {
     L1ShopItem shopItem = getPurchasingItem(item.getItemId(), item.getEnchantLevel());
     if (shopItem == null) {
       return null;
     }
     return new L1AssessedItem(item.getId(), getAssessedPrice(shopItem));
   }

   private int getAssessedPrice(L1ShopItem item) {
     return (int)(item.getPrice() * Config.ServerRates.RateShopPurchasingPrice / item.getPackCount());
   }

   public List<L1AssessedItem> assessItems(L1PcInventory inv) {
     List<L1AssessedItem> result = new ArrayList<>();
     for (L1ShopItem item : this._purchasingItems) {
       for (L1ItemInstance targetItem : inv.findItemsId(item.getItemId())) {
         if (isPurchaseableItem(targetItem))
         {

           if (item.getEnchant() == targetItem.getEnchantLevel())
             result.add(new L1AssessedItem(targetItem.getId(), getAssessedPrice(item)));
         }
       }
     }
     return result;
   }

   private boolean ensureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
     int price = orderList.getTotalPriceTaxIncluded();

     if (!IntRange.includes(price, 0, 2000000000)) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(904, "2000000000"));
       return false;
     }
     if (!pc.getInventory().checkItem(40308, price)) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(189));
       return false;
     }

     int currentWeight = (int)(pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);
     if ((currentWeight + orderList.getTotalWeight() / 1000) > pc.getMaxWeight() * Config.ServerRates.RateWeightLimit) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));
       return false;
     }

     int totalCount = pc.getInventory().getSize();
     L1Item temp = null;
     for (L1ShopBuyOrder order : orderList.getList()) {
       temp = order.getItem().getItem();
       if (temp.isStackable()) {
         if (!pc.getInventory().checkItem(temp.getItemId()))
           totalCount++;
         continue;
       }
       totalCount++;
     }


     if (totalCount > 200) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(263));
       return false;
     }

     if (price <= 0 || price > 2000000000) {
       pc.sendPackets((ServerBasePacket)new S_Disconnect());
       return false;
     }
     return true;
   }

   private void payCastleTax(L1ShopBuyOrderList orderList) {
     L1TaxCalculator calc = orderList.getTaxCalculator();

     int price = orderList.getTotalPrice();

     int castleId = L1CastleLocation.getCastleIdByNpcid(this._npcId);
     int castleTax = calc.calcCastleTaxPrice(price);
     int nationalTax = calc.calcNationalTaxPrice(price);
     if (castleId == 7 || castleId == 8) {
       castleTax += nationalTax;
       nationalTax = 0;
     }

     if (castleId != 0 && castleTax > 0) {
       MJCastleWar war = MJCastleWarBusiness.getInstance().get(castleId);
       int money = war.getPublicMoney();
       if (2000000000 > money + castleTax) {
         money += castleTax;
         war.setPublicMoney(money);
         MJCastleWarBusiness.getInstance().updateCastle(castleId);
       }

       if (nationalTax > 0) {
         war = MJCastleWarBusiness.getInstance().get(7);
         money = war.getPublicMoney();
         if (2000000000 > money + castleTax) {
           money += nationalTax;
           war.setPublicMoney(money);
           MJCastleWarBusiness.getInstance().updateCastle(7);
         }
       }
     }
   }


   private void payDiadTax(L1ShopBuyOrderList orderList) {
     L1TaxCalculator calc = orderList.getTaxCalculator();

     int price = orderList.getTotalPrice();

     int diadTax = calc.calcDiadTaxPrice(price);
     if (diadTax <= 0) {
       return;
     }

     MJCastleWar war = MJCastleWarBusiness.getInstance().get(8);
     int money = war.getPublicMoney();
     if (2000000000 > money + diadTax) {
       money += diadTax;
       war.setPublicMoney(money);
       MJCastleWarBusiness.getInstance().updateCastle(8);
     }
   }

   private void payTax(L1ShopBuyOrderList orderList) {
     payCastleTax(orderList);
     payDiadTax(orderList);
   }

     // 定義私有方法 sellItems，用於處理物品的銷售
     private void sellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
                // 檢查玩家背包中是否有足夠的 40308 物品來支付總價格（含稅）
         if (!inv.consumeItem(40308, orderList.getTotalPriceTaxIncluded())) {
                // 如果沒有足夠的物品，拋出非法狀態異常
             throw new IllegalStateException("無法消耗購買所需的金幣。");
         }
     int consume_count = orderList.getTotalPriceTaxIncluded();
     L1ItemInstance item = null;
     Random random = new Random(System.nanoTime());
     for (L1ShopBuyOrder order : orderList.getList()) {
       int itemId = order.getItem().getItemId();
       int amount = order.getCount();
       int enchant = order.getItem().getEnchant();
       int endtime = order.getItem().getEndTime();
       boolean carving = order.getItem().isCarving();
       int bless = order.getItem().getBless();
       int attrenchant = order.getItem().getAttrEnchant();
       int buylevel = order.getItem().getBuyLevel();

       if (this._npcId == 8502074) {
         BugRaceController.BugTicketInfo tInfo = BugRaceController.getInstance().find_ticket_info(itemId);
         if (tInfo != null) {
           itemId = tInfo.converter_itemid;
         }
       }

       item = ItemTable.getInstance().createItem(itemId);
       if (getSellingItems().contains(item)) {
         return;
       }

       if (!isShopBuyLimitItem(inv, order, item, amount)) {
         inv.getOwner().getInventory().storeItem(40308, consume_count);
         return;
       }
       consume_count -= order.getItem().getPrice();

            // 檢查玩家等級是否超過購買限制
         if (inv.getOwner() != null &&
                 buylevel != 0 &&
                 inv.getOwner().getLevel() > buylevel) {
                // 向玩家發送消息，通知其等級超過購買限制
             inv.getOwner().sendPackets(String.format("該物品僅限於等級 %d 或以下的玩家購買。", new Object[] { Integer.valueOf(buylevel) }));

            // 繼續下一個循環
             continue;
         }
       if (attrenchant > 0) {
         item.setAttrEnchantLevel(attrenchant);
       }

       if (carving) {
         item.set_Carving(1);
       }

       item.setBless(bless);

       if (endtime > 0) {
         Timestamp deleteTime = null;
         deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * endtime));
         item.setEndTime(deleteTime);
       }

       if (this._npcId == 70068 || this._npcId == 70020 || this._npcId == 70056) {
         item.setIdentified(false);
         int chance = random.nextInt(150) + 1;
         if (chance <= 15) {
           item.setEnchantLevel(-2);
         } else if (chance >= 16 && chance <= 30) {
           item.setEnchantLevel(-1);
         } else if (chance >= 31 && chance <= 89) {
           item.setEnchantLevel(0);
         } else if (chance >= 90 && chance <= 141) {
           item.setEnchantLevel(random.nextInt(2) + 1);
         } else if (chance >= 142 && chance <= 147) {
           item.setEnchantLevel(random.nextInt(3) + 3);
         } else if (chance >= 148 && chance <= 149) {
           item.setEnchantLevel(6);
         } else if (chance == 150) {
           item.setEnchantLevel(7);
         }
       }
       if (this._npcId == 900173) {
         item.setIdentified(false);
         int chance = random.nextInt(200) + 1;
         if (chance <= 20) {
           item.setEnchantLevel(-2);
         } else if (chance >= 25 && chance <= 35) {
           item.setEnchantLevel(-1);
         } else if (chance >= 40 && chance <= 55) {
           item.setEnchantLevel(0);
         } else if (chance >= 60 && chance <= 70) {
           item.setEnchantLevel(random.nextInt(1));
         } else if (chance >= 100 && chance <= 120) {
           item.setEnchantLevel(random.nextInt(2) + 1);
         } else if (chance >= 150 && chance <= 170) {
           item.setEnchantLevel(3);
         }
       }
       if (this._npcId == 7310125) {
         item.setIdentified(false);
         int chance = random.nextInt(200) + 1;
         if (chance <= 19) {
           item.setEnchantLevel(-2);
         } else if (chance >= 20 && chance <= 30) {
           item.setEnchantLevel(-1);
         } else if (chance >= 31 && chance <= 49) {
           item.setEnchantLevel(0);
         } else if (chance >= 50 && chance <= 60) {
           item.setEnchantLevel(random.nextInt(1) + 3);
         } else if (chance >= 70 && chance <= 80) {
           item.setEnchantLevel(random.nextInt(2) + 2);
         } else if (chance >= 130 && chance <= 150) {
           item.setEnchantLevel(5);
         } else if (chance == 170) {
           item.setEnchantLevel(6);
         }
       }
       if (this._npcId == 7310104) {
         item.setIdentified(false);
         int chance1 = random.nextInt(150) + 2;
         if (chance1 <= 15) {
           item.setEnchantLevel(-3);
         } else if (chance1 >= 31 && chance1 <= 40) {
           item.setEnchantLevel(-2);
         } else if (chance1 >= 41 && chance1 <= 50) {
           item.setEnchantLevel(-1);
         } else if (chance1 >= 10 && chance1 <= 20) {
           item.setEnchantLevel(0);
         } else if (chance1 >= 21 && chance1 <= 30) {
           item.setEnchantLevel(random.nextInt(2) + 1);
         } else if (chance1 >= 51 && chance1 <= 60) {
           item.setEnchantLevel(random.nextInt(3) + 3);
         } else if (chance1 >= 61 && chance1 <= 70) {
           item.setEnchantLevel(2);
         } else if (chance1 >= 71 && chance1 <= 80) {
           item.setEnchantLevel(3);
         } else if (chance1 >= 81 && chance1 <= 90) {
           item.setEnchantLevel(4);
         } else if (chance1 == 100) {
           item.setEnchantLevel(5);
         }
       }






       item.setEnchantLevel(enchant);




       onSellings(inv.getOwner(), order.getItem(), item);

       item.setCount(amount);
       inv.storeShopItem(item, true);
       L1PcInstance pc = inv.getOwner();
       obtained_item(pc, item);
     }
   }

   private void onSellings(L1PcInstance pc, L1ShopItem shopItem, L1ItemInstance item) {
     if (shopItem.isTimeLimit() && pc != null) {
       pc.setFishingShopBuyTime_1(System.currentTimeMillis());
     }
   }

   public void sellItems(L1PcInstance pc, L1ShopBuyOrderList orderList) {
     NpcShopCashTable.L1CashType at = NpcShopCashTable.getInstance().getNpcCashType(getNpcId());

     if (at != null) {
       if (!ensurePremiumSell(pc, orderList, at)) {
         return;
       }
       if (at.getCashType() == 41921);


       sellPremiumItems(pc.getInventory(), orderList, at);
     } else {
       if (getNpcId() == 200060 || getNpcId() == 200061 || getNpcId() == 200062 || getNpcId() == 200063 || getNpcId() == 7310103 || getNpcId() == 523 || getNpcId() == 5000000 ||
         getNpcId() == 900047 || getNpcId() == 5072 || getNpcId() == 5073 || getNpcId() == 519 || getNpcId() == 224 ||
         getNpcId() == 7310113 || getNpcId() == 7320124 || getNpcId() == 6000002) {
         if (!ensurePremiumSell(pc, orderList)) {
           return;
         }
         sellPremiumItems(pc.getInventory(), orderList);

         return;
       }

       if (getNpcId() == 0) {
         if (!ensure_contribution_tokken(pc, orderList)) {
           return;
         }
         sell_contribution_tokken(pc.getInventory(), orderList);

         return;
       }

       if (getNpcId() == 7320121 || getNpcId() == 202056 || getNpcId() == 8502049 || getNpcId() == 2020700 || getNpcId() == 2020701 || getNpcId() == 2020702 ||
         getNpcId() == 2020703 || getNpcId() == 2020704 || getNpcId() == 2020705 || getNpcId() == 2020706 || getNpcId() == 2020707 || getNpcId() == 2020708 ||
         getNpcId() == 7320085 || getNpcId() == 73201211 || getNpcId() == 73201212 || getNpcId() == 73201213 || getNpcId() == 73201214 ||
         getNpcId() == 73201215 || getNpcId() == 73201216 || getNpcId() == 73201217 || getNpcId() == 73201218 || getNpcId() == 73201219 || getNpcId() == 73201220) {
         if (!ensure_certtoken(pc, orderList)) {
           return;
         }
         sell_certtoken(pc.getInventory(), orderList);

         return;
       }
       if (getNpcId() == 7320055 || getNpcId() == 19) {
         if (!ensure_certNcoin(pc, orderList)) {
           return;
         }
         sell_certNcoin(pc, orderList);

         return;
       }
       if ((getNpcId() >= 4000001 && getNpcId() <= 4000061) || getNpcId() == 7320087 || getNpcId() == 7320122 || getNpcId() == 7320123 || getNpcId() == 7320157 ||
         NpcTable.getInstance().getTemplate(getNpcId()).getImpl().equalsIgnoreCase("L1NpcShop")) {
         if (!NoTaxEnsureSell(pc, orderList)) {
           return;
         }
         NpcShopSellItems(pc.getInventory(), orderList);

         return;
       }

       if (getNpcId() == 900107) {
         if (!ensureMarkSell(pc, orderList)) {
           return;
         }
         sellMarkItems(pc.getInventory(), orderList);
         return;
       }
       if (!ensureSell(pc, orderList)) {
         return;
       }
       sellItems(pc.getInventory(), orderList);
       payTax(orderList);
     }
   }


   public void buyItems(L1ShopSellOrderList orderList) {
     L1PcInventory inv = orderList.getPc().getInventory();
     int totalPrice = 0;
     L1Object object = null;
     L1ItemInstance item = null;
     for (L1ShopSellOrder order : orderList.getList()) {
       L1ItemInstance l1ItemInstance = inv.getItem(order.getItem().getTargetId());
       item = l1ItemInstance;
       if (item == null)
         continue;
       if (item.getItem().getBless() < 128) {
         int count = inv.removeItem(item, order.getCount());
         if ((totalPrice + order.getItem().getAssessedPrice() * count) > 2147483647L) {
           return;
         }
         totalPrice += order.getItem().getAssessedPrice() * count;
       }
     }

     if (item == null) {
       return;
     }

     totalPrice = IntRange.ensure(totalPrice, 0, 2000000000);
     if (0 < totalPrice)
     {
       if (getNpcId() >= 6100000 && getNpcId() <= 6100035) {
         inv.storeItem(getNpcId() - 5299999, totalPrice, true);
       } else if (this._npcId == 7000077) {
         if (0 < totalPrice) {
           inv.storeItem(41302, totalPrice, true);
         }
       } else if (this._npcId == 7320055) {
         if (0 < totalPrice) {
           inv.storeItem(4100056, totalPrice, true);
         }
       } else if (this._npcId == 8502050) {
         inv.storeItem(4100463, totalPrice, true);
       } else {
         inv.storeItem(40308, totalPrice, true);
       }
     }
   }

   public L1ShopBuyOrderList newBuyOrderList() {
     return new L1ShopBuyOrderList(this);
   }

   public L1ShopSellOrderList newSellOrderList(L1PcInstance pc) {
     return new L1ShopSellOrderList(this, pc);
   }


     // 定義私有方法 sellPremiumItems，用於處理高級物品的銷售
     private void sellPremiumItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
            // 檢查玩家背包中是否有足夠的 41921 物品來支付總價格（含稅）
         if (!inv.consumeItem(41921, orderList.getTotalPriceTaxIncluded())) {
                // 如果沒有足夠的物品，向玩家發送消息並返回
             inv.getOwner().sendPackets((ServerBasePacket)new S_SystemMessage("無法消耗購買所需的金色小精靈羽毛。"));
             return;
         }
            // 記錄消耗的數量
         int consume_count = orderList.getTotalPriceTaxIncluded();
         L1ItemInstance item = null;

                // 遍歷所有購買訂單
         for (L1ShopBuyOrder order : orderList.getList()) {
                // 獲取物品屬性
             int itemId = order.getItem().getItemId();
             int enchant = order.getItem().getEnchant();
             int attrenchant = order.getItem().getAttrEnchant();
             int amount = order.getCount();
             int endtime = order.getItem().getEndTime();
             boolean carving = order.getItem().isCarving();
             int bless = order.getItem().getBless();
             int buylevel = order.getItem().getBuyLevel();
             L1PcInstance pc = inv.getOwner();

                // 檢查玩家等級是否超過購買限制
             if (buylevel != 0 && pc.getLevel() > buylevel) {
                    // 向玩家發送消息，通知其等級超過購買限制

                 pc.sendPackets(String.format("該物品僅限於等級 %d 或以下的玩家購買。", new Object[] { Integer.valueOf(buylevel) }));

                    // 繼續下一個循環
                 continue;
             }

       Random random = new Random(System.nanoTime());
       item = ItemTable.getInstance().createItem(itemId);
       if (getSellingItems().contains(item)) {
         return;
       }

       if (!isShopBuyLimitItem(inv, order, item, amount)) {
         inv.getOwner().getInventory().storeItem(41921, consume_count);
         return;
       }
       consume_count -= order.getItem().getPrice();

       if (attrenchant > 0) {
         item.setAttrEnchantLevel(attrenchant);
       }

       if (carving) {
         item.set_Carving(1);
       }

       item.setBless(bless);

       if (endtime > 0) {
         Timestamp deleteTime = null;
         deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * endtime));
         item.setEndTime(deleteTime);
       }

       if (this._npcId == 7310103) {
         item.setIdentified(false);
         int chance1 = random.nextInt(100) + 1;
         if (chance1 <= 50) {
           item.setEnchantLevel(7);
         } else if (chance1 >= 60 && chance1 <= 70) {
           item.setEnchantLevel(8);
         } else if (chance1 >= 80 && chance1 <= 90) {
           item.setEnchantLevel(9);
         } else if (chance1 >= 95 && chance1 <= 100) {
           item.setEnchantLevel(10);
         } else {
           item.setEnchantLevel(7);
         }
       }





       item.setEnchantLevel(enchant);
       onSellings(pc, order.getItem(), item);

       item.setCount(amount);
       item = inv.storeShopItem(item, true);
       obtained_item(pc, item);
     }
   }


     // 定義私有方法 ensurePremiumSell，用於確保高級物品銷售的前置條件
     private boolean ensurePremiumSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
            // 獲取訂單的總價格
         int price = orderList.getTotalPrice();
            // 獲取伺服器配置的最大金色小精靈羽毛使用數量
         int FeatherCount = Config.ServerAdSetting.FeatherShopNum;

            // 檢查價格是否在允許範圍內
         if (!IntRange.includes(price, 0, FeatherCount)) {
                // 如果價格超過範圍，向玩家發送消息並返回 false
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("金色小精靈羽毛每次不能使用超過 " + Config.ServerAdSetting.FeatherShopNum + " 個。"));
             return false;
         }

            // 檢查玩家是否擁有足夠的金色小精靈羽毛
         if (!pc.getInventory().checkItem(41921, price)) {
                // 如果羽毛不足，向玩家發送消息並返回 false
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("金色小精靈羽毛不足。"));
             return false;
         }

            // 獲取當前玩家背包的重量
         int currentWeight = (int)(pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);
            // 檢查玩家背包的重量是否超過限制
         if ((currentWeight + orderList.getTotalWeight() / 1000) > pc.getMaxWeight() * Config.ServerRates.RateWeightLimit) {
                // 如果超過重量限制，向玩家發送消息並返回 false
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));
             return false;
         }

     int totalCount = pc.getInventory().getSize();
     L1Item temp = null;
     for (L1ShopBuyOrder order : orderList.getList()) {
       temp = order.getItem().getItem();
       if (temp.isStackable()) {
         if (!pc.getInventory().checkItem(temp.getItemId()))
           totalCount++;
         continue;
       }
       totalCount++;
     }


     if (totalCount > 200) {

       pc.sendPackets((ServerBasePacket)new S_ServerMessage(263));
       return false;
     }
     if (price <= 0 || price > 2000000000) {
       pc.sendPackets((ServerBasePacket)new S_Disconnect());
       return false;
     }
     return true;
   }


   private boolean NoTaxEnsureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
     int price = orderList.getTotalPrice();
     if (!IntRange.includes(price, 0, 2000000000)) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(904, "2000000000"));
       return false;
     }
     if (!pc.getInventory().checkItem(40308, price)) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(189));
       return false;
     }
     int currentWeight = (int)(pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);
     if ((currentWeight + orderList.getTotalWeight() / 1000) > pc.getMaxWeight() * Config.ServerRates.RateWeightLimit) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));
       return false;
     }
     int totalCount = pc.getInventory().getSize();
     L1Item temp = null;
     for (L1ShopBuyOrder order : orderList.getList()) {
       temp = order.getItem().getItem();
       if (temp.isStackable()) {
         if (!pc.getInventory().checkItem(temp.getItemId()))
           totalCount++;
         continue;
       }
       totalCount++;
     }


     if (totalCount > 200) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(263));
       return false;
     }
     if (price <= 0 || price > 2000000000) {
       pc.sendPackets((ServerBasePacket)new S_Disconnect());
       return false;
     }
     return true;
   }

     // 定義私有同步方法 NpcShopSellItems_for_locked，處理 NPC 商店物品銷售
     private synchronized void NpcShopSellItems_for_locked(L1PcInventory inv, L1ShopBuyOrderList orderList) {
            // 獲取訂單的總價格
         int sellings_price = orderList.getTotalPrice();

            // 檢查玩家背包中是否有足夠的 40308 物品來支付總價格
         if (!inv.consumeItem(40308, sellings_price)) {
                    // 如果沒有足夠的物品，向玩家發送消息
             inv.getOwner().sendPackets((ServerBasePacket)new S_SystemMessage("無法消耗購買所需的亞丁幣。"));
         }


     L1NpcInstance npc = L1World.getInstance().findNpc(getNpcId());


     for (L1ShopBuyOrder order : orderList.getList()) {
       int orderid = order.getOrderNumber();
       int amount = order.getCount();
       int remaindcount = ((L1ShopItem)getSellingItems().get(orderid)).getCount();
       if (remaindcount < amount)
         return;
     }
            // 獲取訂單的總價格
         int consume_count = orderList.getTotalPrice();
         L1ItemInstance item = null;
                // 初始化一個布爾數組，用於標記需要從列表中移除的物品
         boolean[] isRemoveFromList = new boolean[this._sellingItems.size()];

            // 遍歷所有購買訂單
         for (L1ShopBuyOrder order : orderList.getList()) {
                // 獲取訂單詳細信息
             int orderid = order.getOrderNumber();
             int itemId = order.getItem().getItemId();
             int amount = order.getCount();
             int enchant = order.getItem().getEnchant();
             int attrenchant = order.getItem().getAttrEnchant();
             int endtime = order.getItem().getEndTime();
             boolean carving = order.getItem().isCarving();
             int bless = order.getItem().getBless();
             int buylevel = order.getItem().getBuyLevel();
             L1PcInstance pc = inv.getOwner();

                // 檢查玩家等級是否超過購買限制
             if (buylevel != 0 && pc.getLevel() > buylevel) {
                    // 向玩家發送消息，通知其等級超過購買限制
                 pc.sendPackets(String.format("該物品僅限於等級 %d 或以下的玩家購買。", new Object[] { Integer.valueOf(buylevel) }));

                // 繼續下一個循環
                 continue;
             }
       int remaindcount = ((L1ShopItem)getSellingItems().get(orderid)).getCount();
       if (remaindcount < amount)
         return;
       item = ItemTable.getInstance().createItem(itemId);

       if (getSellingItems().contains(item)) {
         return;
       }

       if (!isShopBuyLimitItem(inv, order, item, amount)) {
         inv.getOwner().getInventory().storeItem(40308, consume_count);
         return;
       }
       consume_count -= order.getItem().getPrice();

       item.setEnchantLevel(enchant);

       if (attrenchant > 0) {
         item.setAttrEnchantLevel(attrenchant);
       }

       if (carving) {
         item.set_Carving(1);
       }

       item.setBless(bless);

       if (endtime > 0) {
         Timestamp deleteTime = null;
         deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * endtime));
         item.setEndTime(deleteTime);
       }

       if (remaindcount == amount) {
         isRemoveFromList[orderid] = true;
       } else {
         ((L1ShopItem)this._sellingItems.get(orderid)).setCount(remaindcount - amount);
       }
       onSellings(inv.getOwner(), order.getItem(), item);

       item.setCount(amount);
       item = inv.storeShopItem(item, true);
       obtained_item(pc, item);

       for (int i = isRemoveFromList.length - 1; i >= 0; i--) {
         if (isRemoveFromList[i]) {
           this._sellingItems.remove(i);
         }
       }


       npc.updateSellings(item.getId(), item.getCount());
     }
   }


     // 定義私有方法 NpcShopSellItems，用於處理 NPC 商店物品銷售
     private void NpcShopSellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
        // 檢查是否需要鎖定商店銷售
         if (Config.Synchronization.IsSellingsShopLocked) {
            // 如果需要鎖定，調用鎖定版本的銷售方法
             NpcShopSellItems_for_locked(inv, orderList);
             return;
         }
            // 獲取訂單的總價格
         int sellings_price = orderList.getTotalPrice();
            // 檢查玩家是否有足夠的 40308 物品來支付總價格
         if (inv.countItems(40308) < sellings_price) {
                    // 如果沒有足夠的物品，拋出異常
             throw new IllegalStateException("無法消耗購買所需的金幣。");
         }


         // 獲取 NPC 實例
         L1NpcInstance npc = L1World.getInstance().findNpc(getNpcId());

            // 遍歷所有購買訂單
         for (L1ShopBuyOrder order : orderList.getList()) {
             int orderid = order.getOrderNumber();
             int amount = order.getCount();
                // 獲取該訂單物品的剩餘數量
             int remaindcount = ((L1ShopItem)getSellingItems().get(orderid)).getCount();
                // 檢查物品剩餘數量是否足夠
             if (remaindcount < amount) {
                 return;
             }
         }
            // 檢查玩家是否有足夠的 40308 物品來支付總價格
         if (!inv.consumeItem(40308, orderList.getTotalPrice())) {
                // 如果沒有足夠的物品，向玩家發送消息並返回
             inv.getOwner().sendPackets((ServerBasePacket)new S_SystemMessage("無法消耗購買所需的金幣。"));
             return;
         }
            // 獲取訂單的總價格
         int consume_count = orderList.getTotalPrice();
         L1ItemInstance item = null;
                // 初始化一個布爾數組，用於標記需要從列表中移除的物品
         boolean[] isRemoveFromList = new boolean[this._sellingItems.size()];

            // 遍歷所有購買訂單
         for (L1ShopBuyOrder order : orderList.getList()) {
             int orderid = order.getOrderNumber();
             int itemId = order.getItem().getItemId();
             int amount = order.getCount();
             int enchant = order.getItem().getEnchant();
             int attrenchant = order.getItem().getAttrEnchant();
             int endtime = order.getItem().getEndTime();
             boolean carving = order.getItem().isCarving();
             int bless = order.getItem().getBless();
             int buylevel = order.getItem().getBuyLevel();
             L1PcInstance pc = inv.getOwner();

             // 檢查玩家等級是否超過購買限制
             if (buylevel != 0 && pc.getLevel() > buylevel) {
                 // 向玩家發送消息，通知其等級超過購買限制
                 pc.sendPackets(String.format("該物品僅限於等級 %d 或以下的玩家購買。", new Object[] { Integer.valueOf(buylevel) }));

                 // 繼續下一個循環
                 continue;
             }

             // 額外邏輯處理
         }       int remaindcount = ((L1ShopItem)getSellingItems().get(orderid)).getCount();
       if (remaindcount < amount)
         return;
       item = ItemTable.getInstance().createItem(itemId);
       if (getSellingItems().contains(item)) {
         return;
       }
       if (!isShopBuyLimitItem(inv, order, item, amount)) {
         inv.getOwner().getInventory().storeItem(40308, consume_count);
         return;
       }
       consume_count -= order.getItem().getPrice();
       item.setEnchantLevel(enchant);

       if (attrenchant > 0) {
         item.setAttrEnchantLevel(attrenchant);
       }

       if (carving) {
         item.set_Carving(1);
       }

       item.setBless(bless);

       if (endtime > 0) {
         Timestamp deleteTime = null;
         deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * endtime));
         item.setEndTime(deleteTime);
       }

       if (remaindcount == amount) {
         isRemoveFromList[orderid] = true;
       } else {
         ((L1ShopItem)this._sellingItems.get(orderid)).setCount(remaindcount - amount);
       }  onSellings(inv.getOwner(), order.getItem(), item);

       item.setCount(amount);
       item = inv.storeShopItem(item, true);
       obtained_item(pc, item);

       for (int i = isRemoveFromList.length - 1; i >= 0; i--) {
         if (isRemoveFromList[i]) {
           this._sellingItems.remove(i);
         }
       }


       npc.updateSellings(item.getId(), item.getCount());
     }
   }


     // 定義私有方法 sellMarkItems，用於處理標記物品的銷售
     private void sellMarkItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
            // 檢查玩家是否有足夠的 410093 物品來支付總價格
         if (!inv.consumeItem(410093, orderList.getTotalPrice())) {
                // 如果沒有足夠的物品，拋出異常
             throw new IllegalStateException("無法消耗購買所需的滿月的精氣。");
         }
         int consume_count = orderList.getTotalPrice();
         L1ItemInstance item = null;

            // 遍歷所有購買訂單
         for (L1ShopBuyOrder order : orderList.getList()) {
             int itemId = order.getItem().getItemId();
             int amount = order.getCount();
             item = ItemTable.getInstance().createItem(itemId);

                // 檢查物品是否符合商店購買限制
             if (!isShopBuyLimitItem(inv, order, item, amount)) {
                // 如果不符合，將消耗的物品返還給玩家並返回
                 inv.getOwner().getInventory().storeItem(410093, consume_count);
                 return;
             }
             consume_count -= order.getItem().getPrice();
             onSellings(inv.getOwner(), order.getItem(), item);

                // 設置物品數量並存儲到玩家背包中
             item.setCount(amount);
             item = inv.storeShopItem(item, true);
             L1PcInstance pc = inv.getOwner();
             obtained_item(pc, item);
         }
     }

     // 定義私有方法 sell_certNcoin，用於處理 Ncoin 的銷售
     private void sell_certNcoin(L1PcInstance pc, L1ShopBuyOrderList orderList) {
        // 檢查玩家是否有足夠的 Ncoin 來支付總價格，且是否已綁定帳號
         if (pc.getNcoin() < orderList.getTotalPrice() || pc.getAccount() == null) {
        // 如果沒有足夠的 Ncoin 或未綁定帳號，拋出異常
             throw new IllegalStateException("無法消耗購買所需的 Ncoin。");
         }
            // 減少玩家帳號中的 Ncoin 點數
         (pc.getAccount()).Ncoin_point -= orderList.getTotalPrice();
         int consume_count = 0;
         L1ItemInstance item = null;

            // 遍歷所有購買訂單
         for (L1ShopBuyOrder order : orderList.getList()) {
             int itemId = order.getItem().getItemId();
             int amount = order.getCount();

            // 獲取物品模板
             L1Item template = ItemTable.getInstance().getTemplate(itemId);
             if (template == null) {
                    // 如果物品模板為空，拋出異常
                 throw new IllegalStateException(String.format("[Ncoin商店] 無法確認購買的物品。 %d", new Object[] { Integer.valueOf(itemId) }));
             }

                // 獲取補充服務
             SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
             if (template.isStackable()) {
                // 創建堆疊物品
                 item = ItemTable.getInstance().createItem(template);
                 if (item == null) {
                        // 如果創建物品失敗，拋出異常
                     throw new IllegalStateException(String.format("[Ncoin商店] 無法生成購買的物品。 %d", new Object[] { Integer.valueOf(itemId) }));
                 }

                 // 檢查物品是否符合商店購買限制
                 if (!isShopBuyLimitItem(pc.getInventory(), order, item, amount)) {
                    // 如果不符合，累計未消耗的消費金額
                     consume_count += order.getItem().getPrice() * amount;
                     continue;
                 }

                    // 設置物品的識別狀態和充能次數
                 item.setIdentified(true);
                 item.setChargeCount(template.getMaxChargeCount());
                 item.setCount(amount);
                    // 將物品存儲到倉庫中
                 pwh.storeTradeItem(item);
                 continue;
             }

                // 如果購買數量為 1
             if (amount == 1) {
                 for (int i = 0; i < order.getItem().getPackCount(); i++) {
                    // 創建物品實例
                     item = ItemTable.getInstance().createItem(template);
                     if (item == null) {
                            // 如果創建物品失敗，拋出異常
                         throw new IllegalStateException(String.format("[Ncoin商店] 無法生成購買的物品。 %d", new Object[] { Integer.valueOf(itemId) }));
                     }

           if (!isShopBuyLimitItem(pc.getInventory(), order, item, amount)) {
             consume_count += order.getItem().getPrice() * amount;
           }
           else {

                // 設置物品的識別狀態
               item.setIdentified(true);
                // 設置物品的充能次數
               item.setChargeCount(template.getMaxChargeCount());
                // 將物品存儲到倉庫中
               pwh.storeTradeItem(item);
           }
                 }  continue;
             }  for (int j = 0; j < amount; j++) {
                 for (int i = 0; i < order.getItem().getPackCount(); i++) {
                    // 創建物品實例
                     item = ItemTable.getInstance().createItem(template);
                     if (item == null) {
                            // 如果創建物品失敗，拋出異常
                         throw new IllegalStateException(String.format("[Ncoin商店] 無法生成購買的物品。 %d", new Object[] { Integer.valueOf(itemId) }));
                     }
           if (!isShopBuyLimitItem(pc.getInventory(), order, item, amount)) {
             consume_count += order.getItem().getPrice() * amount;
           }
           else {

             item.setIdentified(true);
             item.setChargeCount(template.getMaxChargeCount());
             pwh.storeTradeItem(item);
           }
         }
       }
     }







     (pc.getAccount()).Ncoin_point += consume_count;
     pc.sendPackets((ServerBasePacket)new S_SurvivalCry(2, pc));
     pc.getAccount().updateNcoin();
     SC_GOODS_INVEN_NOTI.do_send(pc);
   }

     // 定義私有方法 sell_certtoken，用於處理特定代幣的銷售
     private void sell_certtoken(L1PcInventory inv, L1ShopBuyOrderList orderList) {
            // 檢查玩家是否有足夠的 4100463 物品來支付總價格
         if (!inv.consumeItem(4100463, orderList.getTotalPrice())) {
                // 如果沒有足夠的物品，向玩家發送消息並返回
             inv.getOwner().sendPackets((ServerBasePacket)new S_SystemMessage("無法消耗購買所需的騎士團代幣。"));
             return;
         }
         int consume_count = orderList.getTotalPrice();
         L1ItemInstance item = null;

            // 遍歷所有購買訂單
         for (L1ShopBuyOrder order : orderList.getList()) {
             int itemId = order.getItem().getItemId();
             int enchant = order.getItem().getEnchant();
             int amount = order.getCount();
             Random random = new Random(System.nanoTime());

            // 創建物品實例
             item = ItemTable.getInstance().createItem(itemId);
             if (getSellingItems().contains(item)) {
                 return;
             }

                // 檢查物品是否符合商店購買限制
             if (!isShopBuyLimitItem(inv, order, item, amount)) {
                // 如果不符合，將消耗的物品返還給玩家並返回
                 inv.getOwner().getInventory().storeItem(4100463, consume_count);
                 return;
             }
       consume_count -= order.getItem().getPrice();
       if (this._npcId == 123010) {
         item.setIdentified(false);
         int chance2 = random.nextInt(150) + 1;
         if (chance2 <= 15) {
           item.setEnchantLevel(-2);
         } else if (chance2 >= 20 && chance2 <= 30) {
           item.setEnchantLevel(-1);
         } else if (chance2 >= 31 && chance2 <= 40) {
           item.setEnchantLevel(0);
         } else if (chance2 >= 51 && chance2 <= 60) {
           item.setEnchantLevel(random.nextInt(1) + 2);
         } else if (chance2 >= 70 && chance2 <= 80) {
           item.setEnchantLevel(random.nextInt(2) + 3);
         } else if (chance2 >= 90 && chance2 <= 110) {
           item.setEnchantLevel(5);
         } else if (chance2 == 148) {
           item.setEnchantLevel(6);
         }
       }



       item.setEnchantLevel(enchant);
       onSellings(inv.getOwner(), order.getItem(), item);

       item.setCount(amount);
       item = inv.storeShopItem(item, true);
       L1PcInstance pc = inv.getOwner();
       obtained_item(pc, item);
     }
   }

     // 定義私有方法 ensure_certtoken，用於確保特定代幣的有效性
     private boolean ensure_certtoken(L1PcInstance pc, L1ShopBuyOrderList orderList) {
         int price = orderList.getTotalPrice();
            // 確認價格是否在合理範圍內
         if (!IntRange.includes(price, 0, 10000000)) {
             pc.sendPackets("一次使用量不能超過1000萬。");
             return false;
         }

            // 檢查玩家背包中是否有足夠的4100463代幣
         if (!pc.getInventory().checkItem(4100463, price)) {
             pc.sendPackets("騎士團代幣不足。");
             return false;
         }

            // 計算當前重量並檢查是否超過負重限制
         int currentWeight = (int)(pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);
         if ((currentWeight + orderList.getTotalWeight() / 1000) > pc.getMaxWeight() * Config.ServerRates.RateWeightLimit) {
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));
             return false;
         }
     int totalCount = pc.getInventory().getSize();
     L1Item temp = null;
     for (L1ShopBuyOrder order : orderList.getList()) {
       temp = order.getItem().getItem();
       if (temp.isStackable()) {
         if (!pc.getInventory().checkItem(temp.getItemId()))
           totalCount++;
         continue;
       }
       totalCount++;
     }


     if (totalCount > 200) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(263));
       return false;
     }
     if (price <= 0 || price > 2000000000) {
       pc.sendPackets((ServerBasePacket)new S_Disconnect());
       return false;
     }
     return true;
   }


























































   private boolean ensure_certNcoin(L1PcInstance pc, L1ShopBuyOrderList orderList) {
     int price = orderList.getTotalPrice();
     if (!IntRange.includes(price, 0, 10000000)) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("한번에 10000000개 이상 사용할 수 없습니다."));
       return false;
     }

     if (pc.getNcoin() < price) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("N코인이 부족 합니다."));
       return false;
     }
     int currentWeight = (int)(pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);
     if ((currentWeight + orderList.getTotalWeight() / 1000) > pc.getMaxWeight() * Config.ServerRates.RateWeightLimit) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));
       return false;
     }
     int totalCount = pc.getInventory().getSize();
     L1Item temp = null;
     for (L1ShopBuyOrder order : orderList.getList()) {
       temp = order.getItem().getItem();
       if (temp.isStackable()) {
         if (!pc.getInventory().checkItem(temp.getItemId()))
           totalCount++;
         continue;
       }
       totalCount++;
     }


     if (totalCount > 200) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(263));
       return false;
     }
     if (price <= 0 || price > 2000000000) {
       pc.sendPackets((ServerBasePacket)new S_Disconnect());
       return false;
     }
     return true;
   }


   private boolean ensureMarkSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
     int price = orderList.getTotalPrice();
     if (!IntRange.includes(price, 0, 1000)) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("만월의 정기는 한번에 1,000개 이상 사용할 수 없습니다."));
       return false;
     }

     if (!pc.getInventory().checkItem(410093, price)) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(337, "$10196"));
       return false;
     }
     int currentWeight = (int)(pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);
     if ((currentWeight + orderList.getTotalWeight() / 1000) > pc.getMaxWeight() * Config.ServerRates.RateWeightLimit) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));
       return false;
     }
     int totalCount = pc.getInventory().getSize();
     L1Item temp = null;
     for (L1ShopBuyOrder order : orderList.getList()) {
       temp = order.getItem().getItem();
       if (temp.isStackable()) {
         if (!pc.getInventory().checkItem(temp.getItemId()))
           totalCount++;
         continue;
       }
       totalCount++;
     }


     if (totalCount > 200) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(263));
       return false;
     }
     if (price <= 0 || price > 2000000000) {
       pc.sendPackets((ServerBasePacket)new S_Disconnect());
       return false;
     }
     return true;
   }

   private boolean ensurePremiumSell(L1PcInstance pc, L1ShopBuyOrderList orderList, NpcShopCashTable.L1CashType at) {
     int price = orderList.getTotalPrice();
     int aden_type = at.getCashType();

     if (at.get_max_use_count() != 0 &&
       !IntRange.includes(price, 0, at.get_max_use_count())) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage(at.getCashName() + "은(는) 한번에 " + MJString.parse_money_string(at.get_max_use_count()) + "개 이상 사용할 수 없습니다."));
       return false;
     }

     if (at.getCashType() == -2) {
       int ainhasad_point = pc.getAccount().getBlessOfAinBonusPoint();
       if (ainhasad_point < price) {
         pc.sendPackets(String.format("%s이(가) 부족합니다.", new Object[] { at.getCashName() }));
         return false;
       }
     } else if (at.getCashType() == 0) {
       int tamCount = pc.getAccount().getTamPoint();
       if (tamCount < price) {
         pc.sendPackets(String.format("%s이(가) 부족합니다.", new Object[] { at.getCashName() }));
         return false;
       }
     } else if (!pc.getInventory().checkItem(aden_type, price)) {
       pc.sendPackets(String.format("%s이(가) 부족합니다.", new Object[] { at.getCashName() }));
       return false;
     }

     int currentWeight = pc.getInventory().getWeight() * 1000;
     if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));
       return false;
     }
     int totalCount = pc.getInventory().getSize();
     L1Item temp = null;
     for (L1ShopBuyOrder order : orderList.getList()) {
       temp = order.getItem().getItem();
       if (temp.isStackable()) {
         if (!pc.getInventory().checkItem(temp.getItemId()))
           totalCount++;  continue;
       }
       totalCount++;
     }


     if (totalCount > 200) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(263));
       return false;
     }
     if (price <= 0 || price > 2000000000) {
       pc.sendPackets((ServerBasePacket)new S_Disconnect());
       return false;
     }
     return true;
   }


   private void sellPremiumItems(L1PcInventory inv, L1ShopBuyOrderList orderList, NpcShopCashTable.L1CashType at) {
     int aden_type = at.getCashType();

     if (aden_type == 0) {
       int tamCount = inv.getOwner().getAccount().getTamPoint();
       if (tamCount < orderList.getTotalPrice()) {
         inv.getOwner().sendPackets("구입에 필요한 TAM을 소비할 수 없습니다.");

         return;
       }
       inv.getOwner().getAccount().addTamPoint(-orderList.getTotalPrice());
       inv.getOwner().sendPackets((ServerBasePacket)new S_ACTION_UI(194, inv.getOwner().getAccount().getTamPoint()));
       inv.getOwner().getNetConnection().getAccount().updateTam();
     } else if (aden_type == -2) {
       int tamCount = inv.getOwner().getAccount().getBlessOfAinBonusPoint();
       if (tamCount < orderList.getTotalPrice()) {
         inv.getOwner().sendPackets("구입에 필요한 아인하사드 포인트를 소비할 수 없습니다.");
         return;
       }
       inv.getOwner().getAccount().minusBlessOfAinBonusPoint(orderList.getTotalPrice());
       SC_EINHASAD_POINT_POINT_NOTI.send_point(inv.getOwner(), inv.getOwner().getAccount().getBlessOfAinBonusPoint());
     }
     else if (aden_type == 41921) {
       if (inv.checkItem(41921, orderList.getTotalPrice())) {
         inv.consumeItem(41921, orderList.getTotalPrice());
         L1PcInstance l1PcInstance = inv.getOwner();
       } else {

         inv.getOwner().sendPackets("구입에 필요한 " + at.getCashName() + " 을(를) 소비할 수 없었습니다.");

         return;
       }
     } else if (!inv.consumeItem(aden_type, orderList.getTotalPrice())) {
       inv.getOwner().sendPackets("구입에 필요한 " + at.getCashName() + " 을(를) 소비할 수 없었습니다.");


       return;
     }

     L1ItemInstance item = null;
     for (L1ShopBuyOrder order : orderList.getList()) {
       int itemId = order.getItem().getItemId();
       int amount = order.getCount();
       int enchantLevel = order.getItem().getEnchant();
       int endtime = order.getItem().getEndTime();
       boolean carving = order.getItem().isCarving();
       int bless = order.getItem().getBless();
       int attrenchant = order.getItem().getAttrEnchant();
       int buylevel = order.getItem().getBuyLevel();

       item = ItemTable.getInstance().createItem(itemId);

       if (!isShopBuyLimitItem(inv, order, item, amount)) {
         if (aden_type == 0) {
           inv.getOwner().getAccount().addTamPoint(order.getItem().getPrice() * amount);
           inv.getOwner().sendPackets((ServerBasePacket)new S_ACTION_UI(194, inv.getOwner().getAccount().getTamPoint()));
           inv.getOwner().getNetConnection().getAccount().updateTam(); continue;
         }
         inv.getOwner().getInventory().storeItem(aden_type, order.getItem().getPrice() * amount);

         continue;
       }

       if (aden_type == 0) {
         if (buylevel != 0 &&
           inv.getOwner().getLevel() > buylevel) {
           inv.getOwner().sendPackets(String.format("해당 아이템은 %d 레벨 이하만 구매 가능합니다.", new Object[] { Integer.valueOf(buylevel) }));


           continue;
         }
       } else if (buylevel != 0 &&
         inv.getOwner().getLevel() > buylevel) {
         inv.getOwner().sendPackets(String.format("해당 아이템은 %d 레벨 이하만 구매 가능합니다.", new Object[] { Integer.valueOf(buylevel) }));


         continue;
       }

       if (attrenchant > 0) {
         item.setAttrEnchantLevel(attrenchant);
       }

       if (carving) {
         item.set_Carving(1);
       }

       item.setBless(bless);

       if (endtime > 0) {
         Timestamp deleteTime = null;
         deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * endtime));
         item.setEndTime(deleteTime);
       }

       if (at.is_random_enchant_use()) {
         int chance = CommonUtil.random(100);
         int size = (at.get_random_enchant()).length;
         int enchant = 0;
         for (int i = 0; i < size; i++) {
           if (Integer.valueOf(at.get_random_enchant_chance()[i]).intValue() < chance) {
             enchant = Integer.valueOf(at.get_random_enchant()[i]).intValue();
             break;
           }
         }
         item.setEnchantLevel(enchant);
       } else {
         item.setEnchantLevel(enchantLevel);
       }

       onSellings(inv.getOwner(), order.getItem(), item);

       item.setCount(amount);
       item = inv.storeShopItem(item, true);
       L1PcInstance pc = inv.getOwner();
       obtained_item(pc, item);
     }
   }

   private boolean isShopBuyLimitItem(L1PcInventory inv, L1ShopBuyOrder order, L1ItemInstance item, int amount) {
     ShopBuyLimit sbl = ShopBuyLimitInfo.getInstance().getShopBuyLimit(order.getItem().getItemId());
     if (sbl != null) {
       ShopBuyLimit char_sbl = null;
       if (sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) {
         char_sbl = ShopBuyLimitInfo.getInstance().findShopBuyLimitByAccount(inv.getOwner().getAccountName(), item.getItem().getItemId());
       } else {
         char_sbl = ShopBuyLimitInfo.getInstance().findShopBuyLimitByObjid(inv.getOwner().getId(), item.getItem().getItemId());
       }
       if (char_sbl != null) {
         if (char_sbl.get_count() > 0) {
           int buy_result = char_sbl.get_count() - amount;
           int set_buy_result = (buy_result < 0) ? 0 : buy_result;
           if (buy_result >= 0) {
             char_sbl.set_count(set_buy_result);
             if (char_sbl.get_count() == 0) {
               if (char_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || char_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
                 char_sbl.set_end_time(new Timestamp(System.currentTimeMillis() + 86400000L));
               } else if (char_sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT || char_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
                 char_sbl.set_end_time(new Timestamp(System.currentTimeMillis() + 604800000L));
               }

               SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               String sLatestLoginDate = formatter.format(char_sbl.get_end_time());
               inv.getOwner()
                 .sendPackets(String.format("\\f3" + item.getLogName() + "\\f3은(는) " + ((char_sbl
                     .get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || char_sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) ? "\\f3동일 계정의 경우 " : "\\f3") + " %s 이후부터 재구매 가능합니다.", new Object[] { sLatestLoginDate }));
             }
           } else {

             String type = (char_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || char_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) ? "일일" : "1주";
             inv.getOwner().sendPackets(char_sbl.get_item_name() + "은(는) " + type + " " + char_sbl.get_count() + "개까지 구매가능합니다.");

             return false;
           }
         } else {
           String type = (char_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || char_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) ? "일일" : "1주";
           inv.getOwner().sendPackets(char_sbl.get_item_name() + "은(는) " + type + " " + char_sbl.get_count() + "개까지 구매가능합니다.");

           return false;
         }
       } else {
         ShopBuyLimit new_sbl = new ShopBuyLimit();
         new_sbl.set_item_id(sbl.get_item_id());
         new_sbl.set_item_name(sbl.get_item_name());
         new_sbl.set_objid(inv.getOwner().getId());
         new_sbl.set_type(sbl.get_type());
         new_sbl.set_account_name(inv.getOwner().getAccount().getName());
         new_sbl.set_count(sbl.get_count());

         int buy_result = new_sbl.get_count() - amount;
         int set_buy_result = (buy_result < 0) ? 0 : buy_result;
         if (buy_result >= 0) {
           new_sbl.set_count(set_buy_result);
           if (new_sbl.get_count() == 0) {
             if (new_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || new_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
               new_sbl.set_end_time(new Timestamp(System.currentTimeMillis() + 86400000L));
             } else if (new_sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT || new_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
               new_sbl.set_end_time(new Timestamp(System.currentTimeMillis() + 604800000L));
             }

             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             String sLatestLoginDate = formatter.format(new_sbl.get_end_time());
             inv.getOwner()
               .sendPackets(String.format("\\f3" + item.getLogName() + "\\f3은(는) " + ((new_sbl
                   .get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || new_sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) ? "\\f3동일 계정의 경우 " : "\\f3") + " %s 이후부터 재구매 가능합니다.", new Object[] { sLatestLoginDate }));
           }

         } else {

           String type = (new_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || new_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) ? "일일" : "1주";
           inv.getOwner().sendPackets(new_sbl.get_item_name() + "은(는) " + type + " " + new_sbl.get_count() + "개까지 구매가능합니다.");

           return false;
         }

         ShopBuyLimitInfo.getInstance().addShopBuyLimit(new_sbl);
       }
     }
     return true;
   }

   private boolean ensure_contribution_tokken(L1PcInstance pc, L1ShopBuyOrderList orderList) {
     int price = orderList.getTotalPrice();
     L1Clan clan = pc.getClan();
     if (!IntRange.includes(price, 0, 1000000000)) {
       pc.sendPackets("한번에 10억개 이상 사용할 수 없습니다.");
       return false;
     }

     if (clan.getContribution() < price) {
       pc.sendPackets("공헌도가 부족 합니다.");
       return false;
     }
     int currentWeight = (int)(pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);
     if ((currentWeight + orderList.getTotalWeight() / 1000) > pc.getMaxWeight() * Config.ServerRates.RateWeightLimit) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));
       return false;
     }
     int totalCount = pc.getInventory().getSize();
     L1Item temp = null;
     for (L1ShopBuyOrder order : orderList.getList()) {
       temp = order.getItem().getItem();
       if (temp.isStackable()) {
         if (!pc.getInventory().checkItem(temp.getItemId()))
           totalCount++;
         continue;
       }
       totalCount++;
     }


     if (totalCount > 200) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(263));
       return false;
     }
     if (price <= 0 || price > 2000000000) {
       pc.sendPackets((ServerBasePacket)new S_Disconnect());
       return false;
     }
     return true;
   }

   private void sell_contribution_tokken(L1PcInventory inv, L1ShopBuyOrderList orderList) {
     L1PcInstance pc = inv.getOwner();
     L1Clan clan = pc.getClan();

     if (clan.getContribution() < orderList.getTotalPrice()) {
       throw new IllegalStateException("구입에 필요한 공헌도를 소비할 수 없습니다.");
     }
     clan.addClanShopContribution(orderList.getTotalPrice());
     ClanTable.getInstance().updateContribution(clan.getClanId(), clan.getContribution());
     clan.createOnlineMembers().forEach(m -> {
           if (m.player != null) {
             SC_BLOOD_PLEDGE_CONTRIBUTION_ACK.clan_contribution_send(m.player);
           }
         });


     L1ItemInstance item = null;
     for (L1ShopBuyOrder order : orderList.getList()) {
       int itemId = order.getItem().getItemId();
       int amount = order.getCount();
       item = ItemTable.getInstance().createItem(itemId);
       if (getSellingItems().contains(item)) {
         return;
       }
       item.setCount(amount);

       item = inv.storeShopItem(item, true);
       obtained_item(pc, item);
     }
   }

   public void obtained_item(L1PcInstance pc, L1ItemInstance item) {
     if (pc != null)
       SC_OBTAINED_ITEM_INFO.send_obtained(pc, item, item.getCount());
   }
 }


