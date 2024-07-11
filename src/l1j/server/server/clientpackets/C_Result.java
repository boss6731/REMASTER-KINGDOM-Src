 package l1j.server.server.clientpackets;

 import MJShiftObject.Battle.MJShiftBattlePlayManager;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.LinkedList;
 import java.util.Map;
 import l1j.server.BuyLimitSystem.BuyLimitSystem;
 import l1j.server.BuyLimitSystem.BuyLimitSystemAccount;
 import l1j.server.BuyLimitSystem.BuyLimitSystemAccountTable;
 import l1j.server.BuyLimitSystem.BuyLimitSystemCharacter;
 import l1j.server.BuyLimitSystem.BuyLimitSystemCharacterTable;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
 import l1j.server.MJDShopSystem.MJDShopItem;
 import l1j.server.MJItemExChangeSystem.S_ItemExSelectPacket;
 import l1j.server.TJ.TJCouponProvider;
 import l1j.server.server.GameClient;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.NoShopAndWare;
 import l1j.server.server.datatables.NpcShopTable;
 import l1j.server.server.datatables.NpcShopTable2;
 import l1j.server.server.datatables.NpcShopTable3;
 import l1j.server.server.datatables.ShopTable;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Inventory;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.Warehouse.ClanWarehouse;
 import l1j.server.server.model.Warehouse.ElfWarehouse;
 import l1j.server.server.model.Warehouse.PrivateWarehouse;
 import l1j.server.server.model.Warehouse.SupplementaryService;
 import l1j.server.server.model.Warehouse.Warehouse;
 import l1j.server.server.model.Warehouse.WarehouseManager;
 import l1j.server.server.model.shop.L1AssessedItem;
 import l1j.server.server.model.shop.L1Shop;
 import l1j.server.server.model.shop.L1ShopBuyOrderList;
 import l1j.server.server.model.shop.L1ShopSellOrderList;
 import l1j.server.server.monitor.Logger;
 import l1j.server.server.monitor.LoggerInstance;
 import l1j.server.server.serverpackets.S_Disconnect;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1ShopItem;
 import l1j.server.server.utils.SQLUtil;









 public class C_Result
   extends ClientBasePacket
 {
   private static final HashMap<Integer, String> afterImpls = new HashMap<>(); static {
     afterImpls.put(Integer.valueOf(0), "L1Merchant");
     afterImpls.put(Integer.valueOf(7320121), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020562), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020563), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020564), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020565), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020566), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020567), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020568), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020569), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020700), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020701), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020702), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020703), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020704), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020705), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020706), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020707), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020708), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020570), "L1Merchant");
     afterImpls.put(Integer.valueOf(2020571), "L1Merchant");

     afterImpls.put(Integer.valueOf(7320085), "L1Merchant");
     afterImpls.put(Integer.valueOf(73201211), "L1Merchant");
     afterImpls.put(Integer.valueOf(73201212), "L1Merchant");
     afterImpls.put(Integer.valueOf(73201213), "L1Merchant");
     afterImpls.put(Integer.valueOf(73201214), "L1Merchant");
     afterImpls.put(Integer.valueOf(73201215), "L1Merchant");
     afterImpls.put(Integer.valueOf(73201216), "L1Merchant");
     afterImpls.put(Integer.valueOf(73201217), "L1Merchant");
     afterImpls.put(Integer.valueOf(73201218), "L1Merchant");
     afterImpls.put(Integer.valueOf(73201219), "L1Merchant");
     afterImpls.put(Integer.valueOf(73201220), "L1Merchant");
   }


   private static final String DWARF = "L1Dwarf";
   private static final String NPC_SHOP = "L1NpcShop";
   private static final String MERCHANT = "L1Merchant";
   private static final String SELECTOR = "L1Selector";
   private static final String TJCOUPON = "L1TjCoupon";
   private static final String ITEMCHANGE = "L1ItemChange";
   private L1PcInstance pc;
   private int npcObjectId;
   private int resultType;
   private int size;
   private L1Object findObject;
   private String npcImpl;
   private String npcName;
   private int npcId;
   private int level;
   private boolean tradable;
   private boolean isPrivateShop;
   private boolean isPrivateNpcShop;
   private int bugCount;

   public C_Result(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);
     this.pc = clientthread.getActiveChar();
     if (this.pc == null)
       return;
     if (this.pc.getOnlineStatus() == 0) {
       clientthread.kick();
       return;
     }
     for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
       if (player.getAccountName().equalsIgnoreCase(clientthread.getAccountName()) && !player.isPrivateShop()) {
         this.bugCount++;
       }
     }
     if (this.bugCount > 1) {
       clientthread.kick();

       return;
     }
     this.npcObjectId = readD();
     this.resultType = readC();
     this.size = readC();

     readP(1);


     if (this.size < 0 || this.size > 2000000000) {
       return;
     }


     this.isPrivateShop = false;
     this.isPrivateNpcShop = false;

     this.level = this.pc.getLevel();
     this.npcId = 0;
     this.npcImpl = "";
     this.tradable = true;
     this.findObject = L1World.getInstance().findObject(this.npcObjectId);
     this.npcName = "";

     if (this.findObject != null) {
       int diffLocX = Math.abs(this.pc.getX() - this.findObject.getX());
       int diffLocY = Math.abs(this.pc.getY() - this.findObject.getY());
       if (this.resultType == 18 && this.pc.isGm() && this.findObject instanceof L1PcInstance && this.npcObjectId != this.pc.getId()) {
         L1PcInstance target = (L1PcInstance)this.findObject;
         L1ItemInstance item = null;

         if (target.getInventory().getItems() == null) {
           return;
         }
         ArrayList<L1ItemInstance> _list = new ArrayList<>(target.getInventory().getItems());
         ArrayList<Integer> _list_count = new ArrayList<>();

         for (int i = 0; i < this.size; i++) {
           int index = readD();
           int count = readD();
           item = _list.get(index);

           if (this.pc.isTwoLogin(this.pc)) {
             return;
           }

           if (item == null) {
             return;
           }

           if (!item.isStackable() && count != 1) {
             this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
             return;
           }
           if (count <= 0 || item.getCount() <= 0) {
             this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
             return;
           }
           if (count > item.getCount()) {
             count = item.getCount();
           }
           _list.add(item);
           _list_count.add(Integer.valueOf(count));

           L1ItemInstance tr = null;
           tr = _list.get(index);
           int item_count = ((Integer)_list_count.get(i)).intValue();
           if (item.isEquipped()) {
             target.getInventory().setEquipped(item, false, false, true, false);
             target.start_teleport(target.getX(), target.getY(), target.getMapId(), target.getHeading(), 18339, false);
           }
             target.getInventory().tradeItem(tr, item_count, (L1Inventory)this.pc.getInventory()); // 將目標的物品交易到玩家的背包中
             this.pc.sendPackets(String.format("物品回收: 從 %s 收回 %s (%d)。", new Object[] { target.getName(), tr.getName(), Integer.valueOf(item_count) })); // 發送系統消息通知玩家物品已回收

         _list.clear();
         _list_count.clear(); return;
       }
       if (this.resultType == 20 && this.findObject instanceof L1PcInstance && this.npcObjectId == this.pc.getId()) {
         LinkedList<ItemCountInfo> items = new LinkedList<>();
         for (int i = 0; i < this.size; i++) {
           int index = readD();
           int count = readD();
           items.add(new ItemCountInfo(index, count));
         }
         TJCouponProvider.provider().onChoiceItem(this.pc, items);

         return;
       }

           if (this.findObject instanceof L1NpcInstance) { // 如果 findObject 是 L1NpcInstance 的實例
               L1NpcInstance targetNpc = (L1NpcInstance) this.findObject; // 將 findObject 轉換為 L1NpcInstance 類型
               if (!targetNpc.is_sub_npc() &&
                       targetNpc.getNpcId() != Config.ServerAdSetting.PC_MASTER_SHOP_ID &&
                       targetNpc.getNpcId() != Config.ServerAdSetting.TIME_COLLECTION_NPC_IDS[0]) { // 如果 targetNpc 不是子 NPC，且 NPC ID 不等於指定的商店 ID 和收集 NPC ID
                   if (diffLocX > 18 || diffLocY > 18) { // 如果與 NPC 的 X 或 Y 座標差距大於 18
                       if (!(this.findObject instanceof L1NpcInstance)) { // 如果 findObject 不是 L1NpcInstance 的實例
                           System.out.println(String.format("偵測到嘗試接近未知的商店。%d %s (%d, %d, %d)", new Object[] {
                                   Integer.valueOf(this.findObject.getId()),
                                   String.valueOf(this.findObject),
                                   Integer.valueOf(this.findObject.getX()),
                                   Integer.valueOf(this.findObject.getY()),
                                   Short.valueOf(this.findObject.getMapId())
                           }));
                           return; // 結束程式
                       }
                       if (this.resultType == 5 && this.size == 0 && targetNpc.getNpcTemplate().getImpl().equalsIgnoreCase("L1Dwarf")) {
                           L1Clan clan = this.pc.getClan(); // 獲取玩家的家族
                           if (clan == null) {
                               return; // 如果玩家無家族，結束程式
                           }
                           clan.deleteClanRetrieveUser(this.pc.getId()); // 刪除家族回收用戶
                           this.pc.sendPackets("距離倉庫太遠，無法取回物品。"); // 發送系統消息通知玩家距離倉庫太遠，無法取回物品
                       } else {
                           this.pc.sendPackets("距離販賣/購買 NPC 太遠，交易已取消。"); // 發送系統消息通知玩家距離 NPC 太遠，交易已取消
                       }
                       return; // 結束程式
                   }
               }
           }

         this.npcId = targetNpc.getNpcTemplate().get_npcId();
         this.npcImpl = targetNpc.getNpcTemplate().getImpl();
         this.npcName = targetNpc.getName();


         if (this.npcImpl.equals("L1NpcShop")) {
           this.isPrivateNpcShop = true;
         }
       } else if (this.findObject instanceof L1PcInstance) {
         if (this.npcObjectId == this.pc.getId() && this.resultType == 9) {
           S_ItemExSelectPacket select_packet = this.pc.get_select_item();
           if (select_packet != null) {
             int select_index = readD();
             int select_count = readD();
             if (select_count <= 0) {
               return;
             }
             select_packet.do_select(this.pc, select_index);
             select_packet.dispose();
             return;
           }
             this.pc.sendPackets("時間已過。"); // 發送系統消息通知玩家時間已過

             return; // 結束程式
         }
         L1PcInstance gm = (L1PcInstance)this.findObject;
         if (gm.isGm() && this.resultType == 3) {
           int objectId = readD();
           int count = readD();
           L1Object tmp = L1World.getInstance().findObject(objectId);
           if (tmp != null && tmp instanceof L1PcInstance) {
             gm.start_teleport(tmp.getX(), tmp.getY(), tmp.getMapId(), gm.getHeading(), 18339, false, false);
           } else {
             return;
           }
         }
         if (this.findObject.getId() == this.pc.getId() && this.pc.getMapId() == 38 && this.resultType == 0) {
           for (int i = 0; i < this.size; i++) {
             int line = readD();
             int count = readD();

             if (count <= 0) {
               return;
             }

             if (count <= 0 || count >= 10000) {
               return;
             }


               try {
                   // 檢查玩家背包中是否有足夠的 40308 物品 (阿登那)，數量為 500 * count
                   if (this.pc.getInventory().checkItem(40308, 500 * count)) {
                       // 消耗玩家背包中的 40308 物品，數量為 500 * count
                       this.pc.getInventory().consumeItem(40308, 500 * count);
                       // 將 4100086 物品存入玩家背包中，數量為 count，並發送通知
                       this.pc.getInventory().storeItem(4100086, count, true);
                   } else {
                       // 如果玩家背包中的 40308 物品不足，發送系統消息通知玩家“阿登那不足”
                       this.pc.sendPackets("金幣不足。");
                   }
               } catch (Exception e) {
                   // 處理可能的例外情況
                   System.err.println("錯誤: " + e.getMessage());
               }
             } catch (Exception e) {
               e.printStackTrace();
               return;
             }
           }
           return;
         }
         this.isPrivateShop = true;
       }
     }



     if (this.npcObjectId == 7626) {
       this.npcId = 5;
       this.npcImpl = "L1Merchant";
     }
     if (afterImpls.containsKey(Integer.valueOf(this.npcObjectId))) {
       this.npcId = this.npcObjectId;
       this.npcImpl = afterImpls.get(Integer.valueOf(this.npcObjectId));
     }
     typeSwitch();
   }
   private void typeSwitch() {
     if (this.resultType == 0 && this.npcImpl.equalsIgnoreCase("L1Merchant")) { nomalShopBuy(); }
     else if (this.resultType == 1 && this.npcImpl.equalsIgnoreCase("L1Merchant")) { nomalShopSell(); }
     else if (this.resultType == 2 && this.npcImpl.equalsIgnoreCase("L1Dwarf")) { nomalWarehouseIn(); }
     else if (this.resultType == 3 && this.npcImpl.equalsIgnoreCase("L1Dwarf")) { nomalWarehouseOut(); }
     else if (this.resultType == 4 && this.npcImpl.equalsIgnoreCase("L1Dwarf")) { clanWarehouseIn(); }
     else if (this.resultType == 5 && this.npcImpl.equalsIgnoreCase("L1Dwarf")) { clanWarehouseOut(); }
     else if (this.resultType == 5 && this.npcImpl.equalsIgnoreCase("L1Dwarf")) { clanWarehouseCancel(); }
     else if (this.resultType == 8 && this.npcImpl.equalsIgnoreCase("L1Dwarf") && this.pc.isElf()) { elfWarehouseIn(); }
     else if (this.resultType == 9 && this.npcImpl.equalsIgnoreCase("L1Dwarf") && this.pc.isElf()) { elfWarehouseOut(); }
     else if (this.resultType == 21) { packageWarehousOut();
        }

     else if (this.resultType == 0 && this.isPrivateNpcShop) { npcPrivateShopBuy(); }
     else if (this.resultType == 1 && this.isPrivateNpcShop) { npcPrivateShopSell(); }
     else if (this.resultType == 0 && this.isPrivateShop) { privateShopBuy(); }
     else if (this.resultType == 1 && this.isPrivateShop) { privateShopSell(); }

   }







   private void nomalShopBuy() {
     // 檢查玩家的等級是否達到 USERNOSHOPLEVEL 並且沒有加入家族且不是 GM
     if (this.pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && this.pc.getClanid() <= 0 && !this.pc.isGm()) {
       // 發送系統消息通知玩家達到一定等級且沒有加入家族的玩家無法使用商店
       this.pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上未加入家族的角色無法使用商店");
       this.pc.sendPackets("\fY加入家族是為了伺服器內更好的遊戲體驗");

       return; // 結束方法
     }

     // 從商店表中獲取 NPC ID 對應的商店
     L1Shop shop = ShopTable.getInstance().get(this.npcId);
     if (shop == null) {
       // 如果找不到對應的商店，輸出錯誤信息
       System.out.println(String.format("[商店]%d 找不到對應的 NPC。", new Object[] { Integer.valueOf(this.npcId) }));
       return; // 結束方法
     }

     // 其他購買邏輯
   }
     L1ShopBuyOrderList orderList = shop.newBuyOrderList();
     int itemNumber = 0;
     long itemcount = 0L;




 // 檢查玩家是否在伺服器關閉期間並且 NPC ID 為特定值之一
if (this.pc.서버다운중 == true && (
        this.npcId == 70035 || this.npcId == 70041 || this.npcId == 70042 || this.npcId == 170041 || this.npcId == 370041 || this.npcId == 8502074)) {
// 發送系統消息通知玩家在伺服器關閉期間無法進行購買
        this.pc.sendPackets((ServerBasePacket)new S_SystemMessage("伺服器關閉期間無法進行購買。"));

        return; // 結束方法
        }

// 檢查商店銷售的物品數量是否小於玩家嘗試購買的數量
        if (shop.getSellingItems().size() < this.size) {
// 檢查 NPC ID 是否為特定值之一
         if (this.npcId == 70035 || this.npcId == 70041 || this.npcId == 70042 || this.npcId == 8502074) {
// 發送系統消息通知玩家比賽已經開始
         this.pc.sendPackets("比賽已經開始。");
         return; // 結束方法
         }
         try {
// 輸出錯誤信息，提示玩家嘗試購買超過商店銷售的物品數量
         System.out.println("■[BUG防護]■: " + this.pc.getName() + "嘗試購買超過商店銷售的物品數量。商店銷售數量: " + shop.getSellingItems().size() + "，嘗試購買數量: " + this.size);
// 踢出玩家並關閉連接
         this.pc.getNetConnection().kick();
         this.pc.getNetConnection().close();

         return; // 結束方法
         } catch (Exception e) {
// 捕捉並打印例外情況
         e.printStackTrace();
         }
         }
     for (int i = 0; i < this.size; i++) {
       itemNumber = readD();

       itemcount = readD();
       if (itemcount <= 0L) {
         return;
       }
         // 檢查 NPC ID 是否在特定範圍內，且購買數量是否大於 1
         if (this.npcId >= 6100000 && this.npcId <= 6100035 && itemcount > 1L) {
// 發送系統消息通知玩家只能購買 1 個
         this.pc.sendPackets((ServerBasePacket)new S_SystemMessage("只能單個購買。"));
         return; // 結束方法
         }

// 檢查 NPC ID 是否為 370041
         if (this.npcId == 370041) {
// 檢查購買數量是否在有效範圍內
         if (itemcount <= 0L || itemcount >= 10000L) {
// 發送系統消息通知玩家只能購買 1 到 9999 個
         this.pc.sendPackets("只能購買 1 到 9999 個。");
         return; // 結束方法
         }
         } else {
// 檢查購買數量是否在有效範圍內
         if (itemcount <= 0L || itemcount >= 10000L) {
// 發送系統消息通知玩家只能購買 1 到 9999 個
         this.pc.sendPackets("只能購買 1 到 9999 個。");
         return; // 結束方法
         }
         }

// 檢查伺服器設定是否啟用了 GAHO_D BAN 且購買的物品是否為高級不朽的加護
         if (Config.ServerAdSetting.GAHO_DBAN) {
         int inv_itemid = 4100121; // 高級不朽的加護物品 ID
         int inv_count = this.pc.getInventory().checkItemCount(inv_itemid); // 檢查玩家背包中高級不朽的加護數量

// 檢查購買的物品是否為高級不朽的加護，且玩家不在安全區
         if (((L1ShopItem)shop.getSellingItems().get(itemNumber)).getItemId() == inv_itemid &&
         !this.pc.getSafetyZone()) {
// 發送系統消息通知玩家高級不朽的加護只能在村莊購買
         this.pc.sendPackets("\f3高級不朽的加護只能在村莊購買。");
         return; // 結束方法
         }
         }

         // 檢查購買的物品是否為高級不朽的加護
         if (((L1ShopItem)shop.getSellingItems().get(itemNumber)).getItemId() == inv_itemid) {
         // 檢查購買數量是否大於3
         if (3L < itemcount) {
         // 發送系統消息通知玩家高級不朽的加護不能購買超過3個
         this.pc.sendPackets(String.format("\f3高級不朽的加護不能購買超過3個。", new Object[0]));
         return; // 結束方法
         }
         // 檢查玩家背包中是否有高級不朽的加護
         if (this.pc.getInventory().checkItem(inv_itemid)) {
         // 檢查玩家背包中高級不朽的加護數量以及購買數量，並根據不同情況發送系統消息
         if (inv_count == 1 && itemcount >= 3L) {
         this.pc.sendPackets(String.format("\f3高級不朽的加護不能購買超過3個。", new Object[0]));
         continue; // 繼續下一個迴圈
         }
         if (inv_count >= 2 && itemcount >= 2L) {
         this.pc.sendPackets(String.format("\f3高級不朽的加護不能購買超過3個。", new Object[0]));
         continue; // 繼續下一個迴圈
         }
         if (inv_count >= 3 && itemcount >= 1L) {
         this.pc.sendPackets(String.format("\f3高級不朽的加護不能購買超過3個。", new Object[0]));
         continue; // 繼續下一個迴圈
         }
         }
         }
         }
       if (this.npcId == 0 && (
         (L1ShopItem)shop.getSellingItems().get(itemNumber)).getClanShopType() > 0) {
         switch (((L1ShopItem)shop.getSellingItems().get(itemNumber)).getClanShopType()) {






         case 9:
// 檢查玩家的家族等級是否為 8 或 13
         if (this.pc.getClanRank() == 8 || this.pc.getClanRank() == 13) {
// 發送系統消息通知玩家只有守護等級以上才能購買
         this.pc.sendPackets("只有守護等級以上才能購買。");
         return; // 結束方法
         }
         break;

         case 10:
// 檢查玩家的家族等級是否為 10
         if (this.pc.getClanRank() != 10) {
// 發送系統消息通知玩家只有君主才能購買
         this.pc.sendPackets("只有君主才能購買。");
         return; // 結束方法
         }
         break;










       }
       if (!BuyLimitSystem(this.pc, ((L1ShopItem)shop.getSellingItems().get(itemNumber)).getItem().getItemId(), (int)itemcount, ((L1ShopItem)shop.getSellingItems().get(itemNumber)).getPrice()))

         try {
           // 將購買的物品添加到訂單列表中
         orderList.add(itemNumber, (int)itemcount, this.pc);

         // 檢查商店銷售的物品數量是否大於指定的物品編號
         if (shop.getSellingItems().size() > itemNumber) {
           // 將購買記錄添加到日誌中
         LoggerInstance.getInstance().addShop(
                 ((L1ShopItem)shop.getSellingItems().get(itemNumber)).getItem().getName(),
         (int)itemcount,
         ((L1ShopItem)shop.getSellingItems().get(itemNumber)).getPrice() * itemcount,
         this.npcName,
         this.pc.getName()
         );

         // 檢查是否存在購買數量超過限制的情況
         if (orderList.BugOk() != 0) {
           // 向所有玩家（包括 GM 和自己）發送系統消息通知購買數量超過限制
         for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
           if (player.isGm() || this.pc == player) {
             player.sendPackets((ServerBasePacket)new S_SystemMessage(this.pc.getName() + "玩家已超過商店最大購買數量 (" + itemcount + ")。"));
           }
         }
         }
         } else {
           // 如果商店銷售的物品數量不足，輸出錯誤信息提示檢查玩家的背包
         System.out.println(String.format("[商店購買異常信息] %s 請檢查背包。", new Object[] { this.pc.getName() }));
         }
         } catch (Exception e) {
           // 捕捉並處理例外情況，輸出錯誤信息
         System.out.println(String.format("[商店購買異常信息] 角色名 : %s, 物品編號 : %d, 購買數量 : %d", new Object[] { this.pc.getName(), Integer.valueOf(itemNumber), Long.valueOf(itemcount) }));
         e.printStackTrace();
         return;
         }
       continue;
     }
     if (orderList.getList().size() == 0) {
       return;
     }
     int bugok = orderList.BugOk();
     if (bugok == 0) {
       shop.sellItems(this.pc, orderList);
       this.pc.saveInventory();
     }
   }


 private void nomalShopSell() {
// 檢查玩家的等級是否達到 USERNOSHOPLEVEL 且沒有加入家族且不是 GM
         if (this.pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && this.pc.getClanid() <= 0 && !this.pc.isGm()) {
// 發送系統消息通知玩家達到一定等級且沒有加入家族的角色無法使用商店
         this.pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上未加入家族的角色無法使用商店。");
         this.pc.sendPackets("\fY加入家族是為了伺服器內更好的遊戲體驗。");

         return; // 結束方法
         }

// 其他銷售邏輯
         }


     L1Shop shop = ShopTable.getInstance().get(this.npcId);
     L1ShopSellOrderList orderList = shop.newSellOrderList(this.pc);



     for (int i = 0; i < this.size; i++) {
       int itemNumber = readD(); // 讀取物品編號
         long itemcount = readD(); // 讀取物品數量

         // 檢查物品數量是否小於等於 0
         if (itemcount <= 0L) {
           return; // 結束方法
         }

         // 檢查 NPC ID 是否在範圍內且物品是否不是包裹商品
         if (this.npcId >= 6100000 && this.npcId <= 6100035 && !this.pc.getInventory().getItem(itemNumber).isPackage()) {
           // 發送系統消息通知玩家包含了非包裹商品
         this.pc.sendPackets((ServerBasePacket)new S_SystemMessage("包含非由包裹商店購買的物品。"));
         return; // 結束方法
         }

         // 計算物品數量與玩家背包中某特定物品數量的和
         int over_count = (int)(itemcount + this.pc.getInventory().countItems(40308));

         // 檢查物品總數是否超過設定值，且消耗特定數量的物品
         if (over_count >= Config.ServerAdSetting.ADEN_OVER_COUNT &&
         this.pc.getInventory().consumeItem(Config.ServerAdSetting.ADEN_ITEMID, Config.ServerAdSetting.ADEN_COUNT)) {
           // 將獎勵物品存入玩家背包
         this.pc.getInventory().storeItem(Config.ServerAdSetting.REWARD_ITEMID, Config.ServerAdSetting.REWARD_COUNT);
         }
     }


         // 將購買的物品添加到訂單列表中
         orderList.add(itemNumber, (int)itemcount, this.pc);

// 從玩家的背包中獲取物品
         L1ItemInstance item = this.pc.getInventory().getItem(itemNumber);

// 如果物品存在
         if (item != null) {
         // 估價物品
         L1AssessedItem assessedItem = shop.assessItem(item);

         // 檢查物品總價是否超過設定的上限
         if (assessedItem.getAssessedPrice() * itemcount >= Config.ServerAdSetting.ADEN_OVER_ADEN) {
         // 發送系統消息通知玩家不能出售超過上限價格的物品
         this.pc.sendPackets(Config.ServerAdSetting.ADEN_OVER_ADEN + "원 이상出售是不可能的。");
         return; // 結束方法
         }

         // 將銷售記錄添加到日誌中
         LoggerInstance.getInstance().addShopSell(
         item.getItem().getName(),
         (int)itemcount,
         assessedItem.getAssessedPrice() * itemcount,
         this.npcName,
         this.pc.getName()
         );
         }

     int bugok = orderList.BugOk();
     if (bugok == 0) {
       shop.buyItems(orderList);

       this.pc.saveInventory();
     }
   }






 private void nomalWarehouseIn() {
// 檢查玩家等級是否小於 20
         if (this.pc.getLevel() < 20) {
// 發送系統消息通知玩家倉庫使用等級為 20
         this.pc.sendPackets("倉庫使用等級: 20");
         return; // 結束方法
         }

// 檢查玩家是否正在參與 MJShiftBattlePlayManager 的戰鬥
         if (MJShiftBattlePlayManager.is_shift_battle(this.pc)) {
         return; // 結束方法
         }

         L1Object object = null;
         L1ItemInstance item = null;
         Map<Integer, Integer> map = new HashMap<>();

// 遍歷玩家想要存入倉庫的物品
         for (int i = 0; i < this.size; i++) {
         this.tradable = true;
         int objectId = readD(); // 讀取物品的對象 ID
         int count = readD(); // 讀取物品數量
         item = this.pc.getInventory().getItem(objectId); // 從玩家背包中獲取物品

// 檢查物品對象 ID 是否已經在 map 中（防止複製漏洞）
         if (map.containsKey(Integer.valueOf(objectId))) {
         System.out.println("(複製漏洞) 角色名:" + this.pc.getName() + " 物品對象 ID:" + objectId);
         } else {
         map.put(Integer.valueOf(objectId), Integer.valueOf(objectId));

// 如果物品為空，則中斷循環
         if (item == null) {
         break;
         }

// 這裡可以繼續添加其他邏輯來處理物品存入倉庫
         }
         }
         }

         int itemId = item.getItem().getItemId();

// 檢查玩家是否是 GM，或物品是否不能進行存倉庫操作或物品是否有過期時間
         if (!this.pc.isGm() && (NoShopAndWare.getInstance().isNoShopAndWare(itemId) || item.getEndTime() != null)) {
// 發送系統消息通知玩家此物品不能使用倉庫
         this.pc.sendPackets(String.format("%s無法使用倉庫.", new Object[]{item.getLogName()}));
         break; // 中斷循環
         }

         long nowtime = System.currentTimeMillis();
// 檢查物品的延遲時間是否大於當前時間
         if (item.getItemdelay3() >= nowtime) {
         break; // 中斷循環
         }

// 檢查物品的對象 ID 是否和物品 ID 匹配
         if (objectId != item.getId()) {
// 發送斷線消息
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         break; // 中斷循環
         }

// 檢查物品是否不可堆疊且數量不為 1
         if (!item.isStackable() && count != 1) {
// 發送斷線消息
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         break; // 中斷循環
         }

// 檢查物品數量是否為負數或物品數量是否小於等於 0
         if (count <= 0 || item.getCount() <= 0) {
// 發送斷線消息
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         break; // 中斷循環
         }

// 檢查物品數量是否大於物品擁有的數量
         if (count > item.getCount()) {
         count = item.getCount(); // 將數量設置為物品擁有的最大數量
         }

// 檢查物品擁有的數量是否超過 2000000000
         if (item.getCount() > 2000000000) {
         break; // 中斷循環
         }

// 檢查存入數量是否超過 2000000000
         if (count > 2000000000) {
         break; // 中斷循環
         }


         if (item.getItem().getWareHouseLimitType().toInt() == 0) {
           this.tradable = false;
           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));
         } else if (item.getItem().getWareHouseLimitType().toInt() == 2 &&
           item.getItem().getWareHouseLimitLevel() != 0 &&
           item.getItem().getWareHouseLimitLevel() > item.getEnchantLevel()) {
           this.tradable = false;
           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));
         }













         if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
           this.tradable = false;
           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

           break;
         }
         Object[] petlist = this.pc.getPetList().values().toArray();
         for (Object petObject : petlist) {
           if (petObject instanceof L1PetInstance) {
             L1PetInstance pet = (L1PetInstance)petObject;
             if (item.getId() == pet.getItemObjId()) {
               this.tradable = false;

               this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

               break;
             }
           }
         }
         L1DollInstance doll = this.pc.getMagicDoll();
         if (doll != null &&
           item.getId() == doll.getItemObjId()) {

           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

           break;
         }

         PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(this.pc.getAccountName());
         if (warehouse == null) {
           break;
         }
         if (warehouse.checkAddItemToWarehouse(item, count) == 1) {
           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(75));

           break;
         }
         if (item.getBless() >= 128) {
           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

           break;
         }
         if (this.tradable) {
           this.pc.getInventory().tradeItem(objectId, count, (Warehouse)warehouse);
           this.pc.getLight().turnOnOffLight();

           LoggerInstance.getInstance().addWarehouse(Logger.WarehouseType.Private, true, this.pc, item, count);
         }
       }
     }
   }


 private void nomalWarehouseOut() {
// 檢查玩家等級是否小於 20
         if (this.pc.getLevel() < 20) {
// 發送系統消息通知玩家倉庫使用等級為 20
         this.pc.sendPackets("倉庫使用等級: 20");
         return; // 結束方法
         }

// 檢查玩家是否正在參與 MJShiftBattlePlayManager 的戰鬥
         if (MJShiftBattlePlayManager.is_shift_battle(this.pc)) {
         return; // 結束方法
         }

// 檢查玩家背包中是否有足夠的金幣（假設 40308 是金幣 ID），數量為 100 * this.size
         if (!this.pc.getInventory().checkItem(40308, 100 * this.size)) {
// 發送系統消息通知玩家金幣不足
         this.pc.sendPackets("1金幣不足。");
         return; // 結束方法
         }

// 這裡可以繼續添加其他邏輯來處理物品從倉庫取出的操作
         }
     PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(this.pc.getAccountName());
     if (warehouse == null) {
       return;
     }

         L1ItemInstance item = null;
         Map<Integer, Integer> map = new HashMap<>();
         ArrayList<L1ItemInstance> itemList = new ArrayList<>();
         ArrayList<Integer> list_count = new ArrayList<>();

// 遍歷玩家想要從倉庫取出的物品
         for (int i = 0; i < this.size; i++) {
         int objectId = readD(); // 讀取物品的對象 ID
         int count = readD(); // 讀取物品數量
         item = warehouse.getItem(objectId); // 從倉庫中獲取物品

// 檢查物品對象 ID 是否已經在 map 中（防止複製漏洞）
         if (map.containsKey(Integer.valueOf(objectId))) {
         System.out.println("(複製漏洞) 角色名:" + this.pc.getName() + " 物品對象 ID:" + objectId);
         } else {
         map.put(Integer.valueOf(objectId), Integer.valueOf(objectId)); // 將物品對象 ID 添加到 map 中

// 如果物品為空，則中斷循環
         if (item == null) {
         break;
         }

// 這裡可以繼續添加其他邏輯來處理物品取出
         }
         }
         // 檢查物品的對象 ID 是否和物品 ID 匹配
         if (objectId != item.getId()) {
         // 發送斷線消息
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         break; // 中斷循環
         }

// 檢查物品是否不可堆疊且數量不為 1
         if (!item.isStackable() && count != 1) {
         // 發送斷線消息
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         break; // 中斷循環
         }

// 檢查物品數量是否為負數或物品數量是否小於等於 0
         if (count <= 0 || item.getCount() <= 0) {
         // 發送斷線消息
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         break; // 中斷循環
         }

// 檢查物品數量是否大於倉庫中的物品數量
         if (count > item.getCount()) {
         count = item.getCount(); // 將數量設置為倉庫中物品的最大數量
         }

// 檢查玩家背包是否有足夠的空間來添加這些物品
         if (this.pc.getInventory().checkAddItem(item, count) != 0) {
         // 發送系統消息通知玩家背包過重，無法交易
         this.pc.sendPackets("背包過重，無法交易。");
         break; // 中斷循環
         }

// 將物品添加到待取出的物品列表
         itemList.add(item);
         list_count.add(Integer.valueOf(count));
         }
         }


     L1ItemInstance item1 = null;

     for (int j = 0; j < itemList.size(); j++) {
       item1 = itemList.get(j);
       int item_count = ((Integer)list_count.get(j)).intValue();
       if (this.pc.getInventory().checkAddItem(item1, item_count) == 0) {
         if (this.pc.getInventory().consumeItem(40308, 100)) {
           warehouse.tradeItem(item1, item_count, (L1Inventory)this.pc.getInventory());

           LoggerInstance.getInstance().addWarehouse(Logger.WarehouseType.Private, false, this.pc, item1, item_count);
         } else {
           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(189));
           break;
         }
       } else {
         this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(270));
       }
     }
     itemList.clear();
     list_count.clear();
   }





 private void clanWarehouseIn() {
// 檢查玩家是否有加入任何公會
         if (this.pc.getClanid() <= 0) {
// 發送系統消息通知玩家未加入任何公會
         this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(208));
         return; // 結束方法
         }

// 檢查玩家等級是否小於 20
         if (this.pc.getLevel() < 20) {
// 發送系統消息通知玩家倉庫使用等級為 20
         this.pc.sendPackets("倉庫使用等級: 20");
         return; // 結束方法
         }

// 檢查玩家是否正在參與 MJShiftBattlePlayManager 的戰鬥
         if (MJShiftBattlePlayManager.is_shift_battle(this.pc)) {
         return; // 結束方法
         }

         L1Object object = null;
         L1ItemInstance item = null;
         L1Clan clan = null;
         Map<Integer, Integer> map = new HashMap<>();

// 遍歷玩家想要存入公會倉庫的物品
         for (int i = 0; i < this.size; i++) {
         this.tradable = true;
         int objectId = readD(); // 讀取物品的對象 ID
         int count = readD(); // 讀取物品數量
         item = this.pc.getInventory().getItem(objectId); // 從玩家背包中獲取物品

// 如果物品為空，則中斷循環
         if (item == null) {
         break;
         }

// 檢查物品對象 ID 是否已經在 map 中（防止複製漏洞）
         if (map.containsKey(Integer.valueOf(objectId))) {
         System.out.println("(複製漏洞) 角色名:" + this.pc.getName() + " 物品對象 ID:" + objectId);
         } else {
         map.put(Integer.valueOf(objectId), Integer.valueOf(objectId)); // 將物品對象 ID 添加到 map 中

// 這裡可以繼續添加其他邏輯來處理物品存入公會倉庫
         }
         }
         }
       else {

         map.put(Integer.valueOf(objectId), Integer.valueOf(objectId));
         clan = L1World.getInstance().getClan(this.pc.getClanid());


         int itemId = item.getItem().getItemId();

// 檢查玩家是否不是 GM 且物品是否不能進行商店和倉庫操作，或者物品是否有過期時間
         if (!this.pc.isGm() && (NoShopAndWare.getInstance().isNoShopAndWare(itemId) || item.getEndTime() != null)) {
// 發送系統消息通知玩家此物品不能使用倉庫
         this.pc.sendPackets(String.format("%s無法使用倉庫.", new Object[]{item.getLogName()}));
         break; // 中斷循環
         }
         long nowtime = System.currentTimeMillis();
         if (item.getItemdelay3() >= nowtime) {
           break;
         }

         if (objectId != item.getId()) {
           this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
           break;
         }
         if (!item.isStackable() && count != 1) {
           this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
           break;
         }
         if (count <= 0 || item.getCount() <= 0) {
           this.pc.sendPackets((ServerBasePacket)new S_Disconnect());

           break;
         }
         if (item.getCount() > 2000000000) {
           break;
         }
         if (count > 2000000000) {
           break;
         }

         if (count > item.getCount()) {
           count = item.getCount();
         }


         if (item.getBless() >= 128) {
           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));
           break;
         }
         if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
           this.tradable = false;
           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

           break;
         }
         L1DollInstance doll = this.pc.getMagicDoll();
         if (doll != null &&
           item.getId() == doll.getItemObjId()) {

           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

           break;
         }

         // 檢查公會對象是否不為 null
         if (clan != null) {

         // 檢查物品是否可交易
         if (!item.getItem().isTradable()) {
         this.tradable = false;
         // 發送系統消息通知玩家物品不可交易
         this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));
         }

         // 檢查物品是否有雕刻標記（某種特殊屬性）
         if (item.get_Carving() != 0) {
         this.tradable = false;
         // 發送系統消息通知玩家刻印的物品不能使用公會倉庫
         this.pc.sendPackets("刻印的物品不能使用公會倉庫。");
         }
         }

           Object[] petlist = this.pc.getPetList().values().toArray();
           for (Object petObject : petlist) {
             if (petObject instanceof L1PetInstance) {
               L1PetInstance pet = (L1PetInstance)petObject;
               if (item.getId() == pet.getItemObjId()) {
                 this.tradable = false;

                 this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

                 break;
               }
             }
           }
           ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());

           if (clanWarehouse.checkAddItemToWarehouse(item, count) == 1) {
             this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(75));

             break;
           }
           if (this.tradable) {
             this.pc.getInventory().tradeItem(objectId, count, (Warehouse)clanWarehouse);
             this.pc.getLight().turnOnOffLight();
             history(this.pc, item, count, 1);

             LoggerInstance.getInstance().addWarehouse(Logger.WarehouseType.Clan, true, this.pc, item, count);
           }
         }
       }
     }
   }





 private void clanWarehouseOut() {
         // 輸出日誌信息
         System.out.println("尋找血盟倉庫");

         // 檢查玩家等級是否小於 20
         if (this.pc.getLevel() < 20) {
         // 發送系統消息通知玩家倉庫使用等級為 20
         this.pc.sendPackets("倉庫使用等級: 20");
         return; // 結束方法
         }

         // 檢查玩家是否正在參與 MJShiftBattlePlayManager 的戰鬥
         if (MJShiftBattlePlayManager.is_shift_battle(this.pc)) {
         return; // 結束方法
         }

         // 這裡可以繼續添加其他邏輯來處理從公會倉庫取出物品
         }

     if (this.pc.getInventory().checkEnchantItem(40308, 0, 71)) {
       this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(189));


       return;
     }


     L1Clan clan = L1World.getInstance().getClan(this.pc.getClanid());
     if (clan == null) {
       return;
     }
     if (clan != null) {
       clan.deleteClanRetrieveUser(this.pc.getId());
     }
         // 獲取公會倉庫對象
         ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());

// 初始化物品列表和數量列表
         ArrayList<L1ItemInstance> itemList = new ArrayList<>();
         ArrayList<Integer> list_count = new ArrayList<>();
         Map<Integer, Integer> map = new HashMap<>();

// 遍歷所有需要操作的物品
         for (int i = 0; i < this.size; i++) {
         // 讀取物品的對象 ID 和數量
         int objectId = readD();
         int count = readD();

         // 檢查是否已經處理過該物品（防止複製漏洞）
         if (map.containsKey(Integer.valueOf(objectId))) {
         // 輸出警告信息
         System.out.println("(複製漏洞) 角色名:" + this.pc.getName() + " 物品對象 ID:" + objectId);
         } else {
         // 將物品對象 ID 添加到 map 中
         map.put(Integer.valueOf(objectId), Integer.valueOf(objectId));

         // 從公會倉庫中獲取物品
         L1ItemInstance item = clanWarehouse.getItem(objectId);

         // 如果物品為空，則中斷循環
         if (item == null) {
         break;
         }

         // 這裡可以繼續添加其他邏輯來處理物品的取出操作
         }
         }
         if (objectId != item.getId()) {
           this.pc.sendPackets((ServerBasePacket)new S_Disconnect());

           break;
         }
         if (!item.isStackable() && count != 1) {
           this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
           break;
         }
         if (count <= 0 || item.getCount() <= 0 || item.getCount() > 2000000000) {
           this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
           break;
         }
         if (count >= item.getCount()) {
           count = item.getCount();
         }
         itemList.add(item);
         list_count.add(Integer.valueOf(count));
       }
     }

     L1ItemInstance item1 = null;
     for (int j = 0; j < itemList.size(); j++) {
       item1 = itemList.get(j);
       int item_count = ((Integer)list_count.get(j)).intValue();

       if (this.pc.getInventory().checkAddItem(item1, item_count) == 0) {
         if (this.pc.getInventory().consumeItem(40308, 100 * this.size)) {
           clanWarehouse.tradeItem(item1, item_count, (L1Inventory)this.pc.getInventory());
           history(this.pc, item1, item_count, 2);

           LoggerInstance.getInstance().addWarehouse(Logger.WarehouseType.Clan, false, this.pc, item1, item_count);
         } else {
           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(189));

           break;
         }
       } else {
         this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(270));
         break;
       }
     }
     itemList.clear();
     list_count.clear();
     clanWarehouse.setWarehouseUsingChar(0, 0);
   }





   private void clanWarehouseCancel() {
     L1Clan clan = L1World.getInstance().getClan(this.pc.getClanid());
     if (clan == null) {
       return;
     }
     if (MJShiftBattlePlayManager.is_shift_battle(this.pc)) {
       return;
     }
     if (this.pc.hasSkillEffect(90008)) {
       return;
     }
     clan.deleteClanRetrieveUser(this.pc.getId());
   }



 private void elfWarehouseIn() {
// 檢查玩家等級是否小於 20
         if (this.pc.getLevel() < 20) {
// 發送系統消息通知玩家倉庫使用等級為 20
         this.pc.sendPackets("倉庫使用等級: 20");
         return; // 結束方法
         }

// 檢查玩家是否正在參與 MJShiftBattlePlayManager 的戰鬥
         if (MJShiftBattlePlayManager.is_shift_battle(this.pc)) {
         return; // 結束方法
         }

// 這裡可以繼續添加其他邏輯來處理精靈倉庫物品存入操作
         }

     L1Object object = null;
     L1ItemInstance item = null;
     for (int i = 0; i < this.size; i++) {
       this.tradable = true;
       int objectId = readD();
       int count = readD();
       item = this.pc.getInventory().getItem(objectId);
       if (item == null) {
         break;
       }

       int itemId = item.getItem().getItemId();
       if (!this.pc.isGm() && NoShopAndWare.getInstance().isNoShopAndWare(itemId)) {
         this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(189));
         break;
       }
       if (objectId != item.getId()) {
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         break;
       }
       if (!item.isStackable() && count != 1) {
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         break;
       }
       if (count <= 0 || item.getCount() <= 0) {
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         break;
       }
       if (count > item.getCount()) {
         count = item.getCount();
       }
       if (item.getCount() > 2000000000) {
         break;
       }
       if (count > 2000000000) {
         break;
       }


       if (item.getItem().getWareHouseLimitType().toInt() == 0) {
         this.tradable = false;
         this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));
       }









       if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
         this.tradable = false;
         this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

         break;
       }
       Object[] petlist = this.pc.getPetList().values().toArray();
       for (Object petObject : petlist) {
         if (petObject instanceof L1PetInstance) {
           L1PetInstance pet = (L1PetInstance)petObject;
           if (item.getId() == pet.getItemObjId()) {
             this.tradable = false;

             this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));
             break;
           }
         }
       }
       ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(this.pc.getAccountName());
       if (elfwarehouse.checkAddItemToWarehouse(item, count) == 1) {
         this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(75));

         break;
       }

       if (this.tradable) {
         this.pc.getInventory().tradeItem(objectId, count, (Warehouse)elfwarehouse);
         this.pc.getLight().turnOnOffLight();


         LoggerInstance.getInstance().addWarehouse(Logger.WarehouseType.Elf, true, this.pc, item, count);
       }
     }
   }



 private void elfWarehouseOut() {
// 檢查玩家等級是否小於 20
         if (this.pc.getLevel() < 20) {
// 發送系統消息通知玩家倉庫使用等級為 20
         this.pc.sendPackets("倉庫使用等級: 20");
         return; // 結束方法
         }

// 檢查玩家是否正在參與 MJShiftBattlePlayManager 的戰鬥
         if (MJShiftBattlePlayManager.is_shift_battle(this.pc)) {
         return; // 結束方法
         }

// 檢查玩家的背包中是否有足夠的 40494 這個物品 (例如：4 * this.size 的數量)
         if (!this.pc.getInventory().checkItem(40494, 4 * this.size)) {
// 發送系統消息通知玩家缺少足夠的秘銀
         this.pc.sendPackets("秘銀不足。");
         return; // 結束方法
         }

// 這裡可以繼續添加其他邏輯來處理從精靈倉庫取出物品的操作
         }
     ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(this.pc.getAccountName());
     if (elfwarehouse == null) {
       return;
     }
     ArrayList<L1ItemInstance> itemList = new ArrayList<>();
     ArrayList<Integer> list_count = new ArrayList<>();

     L1ItemInstance item = null;
     L1ItemInstance item1 = null; int i;
     for (i = 0; i < this.size; i++) {
       int objectId = readD();
       int count = readD();
       item = elfwarehouse.getItem(objectId);


       if (item == null) {
         return;
       }
       if (objectId != item.getId()) {
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         return;
       }
       if (!item.isStackable() && count != 1) {
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         return;
       }
       if (count <= 0 || item.getCount() <= 0) {
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         return;
       }
       if (count > item.getCount()) {
         count = item.getCount();
       }
       itemList.add(item);
       list_count.add(Integer.valueOf(count));
     }


     for (i = 0; i < itemList.size(); i++) {

       item1 = itemList.get(i);
       int item_count = ((Integer)list_count.get(i)).intValue();
       if (this.pc.getInventory().checkAddItem(item1, item_count) == 0) {
         if (this.pc.getInventory().consumeItem(40494, 2)) {
           elfwarehouse.tradeItem(item1, item_count, (L1Inventory)this.pc.getInventory());

           LoggerInstance.getInstance().addWarehouse(Logger.WarehouseType.Elf, false, this.pc, item1, item_count);
         } else {
           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(337, "$767"));
           break;
         }
       } else {
         this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(270));
         break;
       }
     }
   }



   private void unk() {
     if (this.resultType == 10 && this.size != 0 && this.npcImpl.equalsIgnoreCase("L1Dwarf")) {
       if (MJShiftBattlePlayManager.is_shift_battle(this.pc)) {
         return;
       }

       L1ItemInstance item = null;
       for (int i = 0; i < this.size; i++) {
         int objectId = readD();
         int count = readD();
         item = this.pc.getDwarfForPackageInventory().getItem(objectId);


         if (item == null) {
           return;
         }

         if (objectId != item.getId()) {
           this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
           return;
         }
         if (!item.isStackable() && count != 1) {
           this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
           return;
         }
         if (count <= 0 || item.getCount() <= 0) {
           this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
           return;
         }
         if (count > item.getCount()) {
           count = item.getCount();
         }


         if (this.pc.getInventory().checkAddItem(item, count) == 0) {
           this.pc.getDwarfForPackageInventory().tradeItem(item, count, (L1Inventory)this.pc.getInventory());
         } else {
           this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(270));
           break;
         }
       }
     }
   }



 private void npcPrivateShopBuy() {
// 檢查玩家是否達到一定等級並且無公會且不是 GM（遊戲管理員）
         if (this.pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && this.pc.getClanid() <= 0 && !this.pc.isGm()) {
// 發送系統消息通知玩家無法使用商店
         this.pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上且無公會的角色無法使用商店");
         this.pc.sendPackets("\fY至少加入一個公會是為了保證伺服器內的遊戲平衡");

// 結束方法
         return;
         }

// 從 NpcShopTable 中獲取商店對象
         L1Shop shop = NpcShopTable.getInstance().get(this.npcId);
// 如果未找到，從 NpcShopTable2 中獲取
         if (shop == null) {
         shop = NpcShopTable2.getInstance().get(this.npcId);
         }
// 如果仍未找到，從 NpcShopTable3 中獲取
         if (shop == null) {
         shop = NpcShopTable3.getInstance().get(this.npcId);
         }

// 這裡可以繼續添加其他邏輯來處理 NPC 私人商店購買操作
         }
         // 創建一個新的購買訂單列表
         L1ShopBuyOrderList orderList = shop.newBuyOrderList();

// 遍歷所有購買的物品
         for (int i = 0; i < this.size; i++) {
         // 讀取物品編號和購買數量
         int itemNumber = readD();
         long itemcount = readD();

         // 如果購買數量小於等於 0，結束方法
         if (itemcount <= 0L) {
         return;
         }

         // 如果購買多於一種不同的物品，發送系統消息並結束方法
         if (this.size >= 2) {
         this.pc.sendPackets((ServerBasePacket)new S_SystemMessage("一次不能購買多種不同的物品。"));
         return;
         }

         // 如果玩家位於地圖 ID 為 800 且購買數量大於 10000，發送系統消息並結束方法
         if (this.pc.getMapId() == 800 && itemcount > 10000L) {
         this.pc.sendPackets((ServerBasePacket)new S_SystemMessage("最大購買數量：普通物品（10000）/ 裝備（1）"));
         return;
         }

         // 將購買的物品添加到訂單列表中
         orderList.add(itemNumber, (int)itemcount, this.pc);

         // 檢查購買的物品是否在商店的銷售列表中，如果不在，發送系統消息並結束方法
         if (shop.getSellingItems().size() <= itemNumber) {
         this.pc.sendPackets("目前無法購買該物品。");
         return;
         }
         }

// 這裡可以繼續添加其他邏輯來處理訂單的支付和物品的交付
       if (!BuyLimitSystem(this.pc, ((L1ShopItem)shop.getSellingItems().get(itemNumber)).getItem().getItemId(), (int)itemcount, ((L1ShopItem)shop.getSellingItems().get(itemNumber)).getPrice())) {


         if (((L1ShopItem)shop.getSellingItems().get(itemNumber)).get_count() == (int)itemcount) {
           delete_NpcShopInfo(this.npcObjectId, itemNumber, this.resultType);
         }
         if (((L1ShopItem)shop.getSellingItems().get(itemNumber)).get_count() > 1) {
           update_NpcShopInfo((int)itemcount, this.npcObjectId, itemNumber, this.resultType);
         }
         L1ShopItem shopItem = shop.getSellingItems().get(itemNumber);
         LoggerInstance.getInstance().addShop(String.format("+%d %s", new Object[] { Integer.valueOf(shopItem.getEnchant()), shopItem.getItem().getName() }), (int)itemcount, shopItem.getPrice() * itemcount, this.npcName, this.pc
             .getName());
       }
     }
     if (orderList.getList().size() == 0) {
       return;
     }
     int bugok = orderList.BugOk();
     if (bugok == 0) {
       shop.sellItems(this.pc, orderList);

       this.pc.saveInventory();
     }
   }










 private void privateShopBuy() {
// 檢查玩家是否達到一定等級並且無公會且不是 GM（遊戲管理員）
         if (this.pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && this.pc.getClanid() <= 0 && !this.pc.isGm()) {
// 發送系統消息通知玩家無法使用商店
         this.pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上且無公會的角色無法使用商店");
         this.pc.sendPackets("\fY至少加入一個公會是為了保證伺服器內的遊戲平衡");

// 結束方法
         return;
         }

// 這裡可以繼續添加其他邏輯來處理私人商店購買操作
         }




     L1PcInstance targetPc = null;
     if (this.findObject instanceof L1PcInstance) {
       targetPc = (L1PcInstance)this.findObject;
     }
     if (targetPc == null) {
       return;
     }


     ArrayList<MJDShopItem> sells = targetPc.getSellings();
     MJDShopItem ditem = null;
     synchronized (sells) {

       if (this.pc.getPartnersPrivateShopItemCount() != sells.size()) {
         return;
       }

       int[] orders = new int[this.size];
       int[] counts = new int[this.size];
       MJDShopItem[] dItems = new MJDShopItem[this.size]; int i;
       for (i = 0; i < this.size; i++) {
         orders[i] = readD();
         counts[i] = readD();
         dItems[i] = sells.get(orders[i]);
       }

       for (i = 0; i < this.size; i++) {
         int order = orders[i];
         int count = counts[i];
         ditem = dItems[i];



         int itemObjectId = ditem.objId;
         int sellPrice = ditem.price;
         int sellCount = ditem.count;
         L1ItemInstance item = targetPc.getInventory().getItem(itemObjectId);
         if (item != null) {


           long nowtime = System.currentTimeMillis();
           if (item.getItemdelay3() >= nowtime) {
             break;
           }
           if (count > sellCount) {
             count = sellCount;
           }
           if (count > 0)
           {

             if (item.isEquipped()) {
               this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(905, ""));


             }
             else if (this.pc.getInventory().checkAddItem(item, count) == 0) {
               for (int j = 0; j < count; j++) {
                 if (sellPrice * j > 2000000000 || sellPrice * j < 0) {
                   this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(904, "2000000000"));
                   return;
                 }
               }
               int price = count * sellPrice;



               if (itemObjectId != item.getId()) {
                 this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
                 targetPc.sendPackets((ServerBasePacket)new S_Disconnect());
                 return;
               }
               if (!item.isStackable() && count != 1) {
                 this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
                 targetPc.sendPackets((ServerBasePacket)new S_Disconnect());
                 return;
               }
               if (count <= 0 || item.getCount() <= 0 || item.getCount() < count) {
                 this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
                 targetPc.sendPackets((ServerBasePacket)new S_Disconnect());
                 return;
               }
               if (count >= item.getCount()) {
                 count = item.getCount();
               }

         // 檢查物品是否已經被裝備
         if (item.isEquipped()) {
// 發送系統消息通知玩家物品已經被裝備
         this.pc.sendPackets((ServerBasePacket)new S_SystemMessage("對方正在裝備該物品。"));
         return; // 結束方法
         }

// 檢查價格是否小於等於 0 或超過 20 億
         if (price <= 0 || price > 2000000000) {
         return; // 結束方法
         }

// 這裡可以繼續添加其他邏輯來處理購買操作

               if (this.pc.getInventory().checkItem(40308, price)) {
                 try {
                   L1ItemInstance adena = this.pc.getInventory().findItemId(40308);
                   if (targetPc != null && adena != null) {
                     if (targetPc.getInventory().tradeItem(item, count, (L1Inventory)this.pc.getInventory()) == null) {
                       return;
                     }
                     this.pc.getInventory().tradeItem(adena, price, (L1Inventory)targetPc.getInventory());


                     targetPc.updateSellings(itemObjectId, count);


                     String message = item.getItem().getName() + " (" + String.valueOf(count) + ")";
                     targetPc.sendPackets((ServerBasePacket)new S_ServerMessage(877, this.pc.getName(), message));


                     writeLogbuyPrivateShop(this.pc, targetPc, item, count, price);
                     try {
                       this.pc.saveInventory();
                       targetPc.saveInventory();
                     } catch (Exception e) {
                       e.printStackTrace();
                     }
                   }
                 } catch (Exception e) {
                   e.printStackTrace();
                 }
               } else {
                 this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(189));
                 break;
               }
             } else {
               this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(270));
               break;
             }
           }
         }
       }
     }
   }






 private void npcPrivateShopSell() {
// 檢查玩家是否達到一定等級並且無公會且不是 GM（遊戲管理員）
         if (this.pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && this.pc.getClanid() <= 0 && !this.pc.isGm()) {
// 發送系統消息通知玩家無法使用商店
         this.pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上且無公會的角色無法使用商店");
         this.pc.sendPackets("\fY至少加入一個公會是為了保證伺服器內的遊戲平衡");

// 結束方法
         return;
         }

// 從 NpcShopTable 中獲取商店對象
         L1Shop shop = NpcShopTable.getInstance().get(this.npcId);
// 如果未找到，從 NpcShopTable2 中獲取
         if (shop == null) {
         shop = NpcShopTable2.getInstance().get(this.npcId);
         }
// 如果仍未找到，從 NpcShopTable3 中獲取
         if (shop == null) {
         shop = NpcShopTable3.getInstance().get(this.npcId);
         }

// 創建一個新的賣出訂單列表
         L1ShopSellOrderList orderList = shop.newSellOrderList(this.pc);

// 這裡可以繼續添加其他邏輯來處理 NPC 私人商店賣出操作
         }



     for (int i = 0; i < this.size; i++) {
       int itemNumber = readD();
       long itemcount = readD();
       if (itemcount <= 0L) {
         return;
       }
       orderList.add(itemNumber, (int)itemcount, this.pc);

       if (((L1ShopItem)shop.getBuyingItems().get(itemNumber)).get_count() == (int)itemcount) {
         delete_NpcShopInfo(this.npcObjectId, itemNumber, this.resultType);
       }
       if (((L1ShopItem)shop.getBuyingItems().get(itemNumber)).get_count() > 1) {
         update_NpcShopInfo((int)itemcount, this.npcObjectId, itemNumber, this.resultType);
       }
     }
     int bugok = orderList.BugOk();
     if (bugok == 0) {
       shop.buyItems(orderList);

       this.pc.saveInventory();
     }
   }







 private void privateShopSell() {
// 檢查玩家是否達到一定等級並且無公會且不是 GM（遊戲管理員）
         if (this.pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && this.pc.getClanid() <= 0 && !this.pc.isGm()) {
// 發送系統消息通知玩家無法使用商店
         this.pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上且無公會的角色無法使用商店");
         this.pc.sendPackets("\fY至少加入一個公會是為了保證伺服器內的遊戲平衡");

// 結束方法
         return;
         }

// 這裡可以繼續添加其他邏輯來處理私人商店賣出操作
         }


     L1ItemInstance item = null;



     L1PcInstance targetPc = null;
     if (this.findObject instanceof L1PcInstance) {
       targetPc = (L1PcInstance)this.findObject;
     }
     if (targetPc == null) {
       return;
     }


     ArrayList<MJDShopItem> purs = targetPc.getPurchasings();
     MJDShopItem ditem = null;
     synchronized (purs) {
       for (int i = 0; i < this.size; i++) {
         int itemObjectId = readD();
         int count = readCH();
         int order = readC();
         item = this.pc.getInventory().getItem(itemObjectId);
         if (item != null) {



           ditem = purs.get(order);
           int buyPrice = ditem.price;
           int buyCount = ditem.count;
           if (count > buyCount) {
             count = buyCount;
           }

           if (item.isEquipped()) {
             this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(905));


           }
           else if (targetPc.getInventory().checkAddItem(item, count) == 0) {
             for (int j = 0; j < count; j++) {
               if (buyPrice * j > 2000000000 || buyPrice * j < 0) {
                 targetPc.sendPackets((ServerBasePacket)new S_ServerMessage(904, "2000000000"));

                 return;
               }
             }
             if (itemObjectId != item.getId()) {
               this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
               targetPc.sendPackets((ServerBasePacket)new S_Disconnect());

               return;
             }
             if (count >= item.getCount()) {
               count = item.getCount();
             }

             if (!item.isStackable() && count != 1)
               return;
             if (item.getCount() <= 0 || count <= 0)
               return;
             if (buyPrice * count <= 0 || buyPrice * count > 2000000000) {
               return;
             }

             if (targetPc.getInventory().checkItem(40308, count * buyPrice)) {
               L1ItemInstance adena = targetPc.getInventory().findItemId(40308);
               if (adena != null) {
                 targetPc.getInventory().tradeItem(adena, count * buyPrice, (L1Inventory)this.pc.getInventory());
                 this.pc.getInventory().tradeItem(item, count, (L1Inventory)targetPc.getInventory());

                 targetPc.updatePurchasings(ditem.objId, count);


                 try {
                   this.pc.saveInventory();
                   targetPc.saveInventory();
                 } catch (Exception e) {
                   e.printStackTrace();
                 }
               }
             } else {
               targetPc.sendPackets((ServerBasePacket)new S_ServerMessage(189));
               break;
             }
           } else {
             this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(271));
             break;
           }
         }
       }
     }
   }





 private void packageWarehousOut() {
// 初始化物品實例
         L1ItemInstance item = null;
// 從倉庫管理器中獲取玩家的附加服務倉庫實例
         SupplementaryService warehouse = WarehouseManager.getInstance().getSupplementaryService(this.pc.getAccountName());
// 如果未找到倉庫實例，直接返回
         if (warehouse == null) {
         return;
         }
// 如果請求的物品數量超過 100，直接返回
         if (this.size > 100) {
         return;
         }
// 遍歷所有請求的物品
         for (int i = 0; i < this.size; i++) {
// 讀取物品對象 ID 和數量
         int objectId = readD();
         int count = readD();
// 讀取無效標誌
         int invalid = readD();
// 從倉庫中獲取物品實例
         item = warehouse.getItem(objectId);

// 如果物品不存在，直接返回
         if (item == null) {
         return;
         }
// 如果無效標誌不等於 1，輸出調試信息
         if (invalid != 1) {
         System.out.println("附加服務倉庫無效標誌: " + invalid + ", 物品名稱: " + item.getName());
         }

// 這裡可以繼續添加其他邏輯來處理物品的提取操作
         }
         }

       if (objectId != item.getId()) {
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         return;
       }
       if (!item.isStackable() && count != 1) {
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         return;
       }
       if (count <= 0 || item.getCount() <= 0) {
         this.pc.sendPackets((ServerBasePacket)new S_Disconnect());
         return;
       }
       if (count > item.getCount()) {
         count = item.getCount();
       }

       if (this.pc.getInventory().checkAddItem(item, count) == 0) {

         if (item.getItem().getItemId() >= 758 && item.getItem().getItemId() <= 761) {
           SetDeleteTime(item, 4320);
         } else if (item.getItem().getItemId() == 41922 || item.getItem().getItemId() == 41923 || item.getItem().getItemId() == 41924 || item
           .getItem().getItemId() == 41925 || item.getItem().getItemId() == 41925 || item.getItem().getItemId() == 900077 || item.getItem().getItemId() == 772 || item
           .getItem().getItemId() == 773 || item.getItem().getItemId() == 774 || item.getItem().getItemId() == 775) {
           SetDeleteTime(item, 1440);
         } else if (item.getItem().getItemId() == 210095 || item.getItem().getItemId() == 2100950) {
           SetDeleteTime(item, 180);
         }
         warehouse.tradeItem(item, count, (L1Inventory)this.pc.getInventory());
         this.pc.saveInventory();

         LoggerInstance.getInstance().addWarehouse(Logger.WarehouseType.Private, false, this.pc, item, count);

         if (count >= 500);
       }
       else {

         this.pc.sendPackets((ServerBasePacket)new S_ServerMessage(270));
         break;
       }
     }
   }


   public String getType() {
     return "[C] C_Result";
   }

   private void writeLogbuyPrivateShop(L1PcInstance pc, L1PcInstance targetPc, L1ItemInstance item, int count, int price) {
     LoggerInstance.getInstance().개인상점구매(true, pc, targetPc, item, item.getCount());
   }

 private void history(L1PcInstance pc, L1ItemInstance item, int count, int i) {
// 初始化用於存儲物品名稱的 StringBuilder
         StringBuilder itemname = new StringBuilder();
// 初始化資料庫連接和語句
         Connection con = null;
         PreparedStatement pstm = null;
// 獲取玩家的公會 ID
         int clanid = pc.getClanid();
// 獲取玩家名稱
         String char_name = pc.getName();
// 獲取物品的強化等級
         int item_enchant = item.getEnchantLevel();
// 獲取當前時間（以秒為單位）
         int elapsed_time = (int)(System.currentTimeMillis() / 1000L);
// 初始化變量以存儲操作類型的描述
         String type = null;
// 根據參數 i 決定操作類型的描述
         if (i == 1) {
         type = "存入了。";
         } else {
         type = "取出了。";
         }

// 如果物品的類型不為 0（即物品是裝備）
         if (item.getItem().getType2() != 0) {
// 如果物品的強化等級為正或零
         if (item_enchant >= 0) {
         itemname.append("+" + item_enchant + " ");
         } else {
         itemname.append(item_enchant + " ");
         }
         }

// 這裡可以繼續添加其他邏輯來完成歷史記錄的處理，例如將記錄寫入資料庫
     itemname.append(item.getName());
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("INSERT INTO clan_warehousehistory SET id =?, clan_id = ?, char_name = ?, item_name = ?, item_count = ?, elapsed_time = ?, item_getorput = ?");
       pstm.setInt(1, IdFactory.getInstance().nextId());
       pstm.setInt(2, clanid);
       pstm.setString(3, char_name);
       pstm.setString(4, itemname.toString());
       pstm.setInt(5, count);
       pstm.setInt(6, elapsed_time);
       pstm.setString(7, type);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   private void SetDeleteTime(L1ItemInstance item, int minute) {
     Timestamp deleteTime = null;
     deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * minute));
     item.setEndTime(deleteTime);
   }




   public static class ItemCountInfo
   {
     public int itemObjectId;



     public int itemCount;



     ItemCountInfo(int itemObjectId, int itemCount) {
       this.itemObjectId = itemObjectId;
       this.itemCount = itemCount;
     }
   }

   public static void update_NpcShopInfo(int count, int npcObjID, int itemObjId, int type) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("UPDATE character_shop SET count=count-" + count + " WHERE objid=? AND item_objid=? AND type=?");
       pstm.setInt(1, npcObjID);
       pstm.setInt(2, itemObjId);
       pstm.setInt(3, type);
       pstm.executeUpdate();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm, con);
     }
   }

   public static void delete_NpcShopInfo(int npcObjID, int itemObjId, int type) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("delete from character_shop where objid=? AND item_objid=? AND type=?");
       pstm.setInt(1, npcObjID);
       pstm.setInt(2, itemObjId);
       pstm.setInt(3, type);
       pstm.executeUpdate();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm, con);
     }
   }


   public static boolean BuyLimitSystem(L1PcInstance pc, int itemId, int count, int price) {
     L1ItemInstance item = null;
     item = ItemTable.getInstance().createItem(itemId);
     BuyLimitSystem.L1BuyLimitItems limit_item = BuyLimitSystem.getInstance().getLimitBuyType(itemId);
     if (limit_item != null) {
       int limit_type = limit_item.isLimitType();
       int current_time = (int)(System.currentTimeMillis() / 1000L);
       if (limit_type == 1 || limit_type == 2) {
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

         if (current_time < start_time.getTime() / 1000L || current_time > limit_time.getTime() / 1000L) {
           pc.sendPackets(5436);
           return true;
         }
       }

       if (limit_type == 1 || limit_type == 3 || limit_type == 5) {
         BuyLimitSystemAccount limit_account = BuyLimitSystemAccountTable.getInstance().getLimitTable(pc.getAccountName(), itemId);
         if (limit_account != null) {



           if (limit_account.getCount() <= 0) {
             pc.sendPackets(3460);
             return true;
           }
           if (count > limit_account.getCount()) {
             pc.sendPackets(3460);
             return true;
           }
           limit_account.setCount(limit_account.getCount() - count);
           limit_account.setBuyTime(new Timestamp(System.currentTimeMillis()));
           if (limit_type == 1)
           { BuyLimitSystemAccountTable.getInstance().updateLimitItem(pc.getAccountName(), item, limit_account.getCount(), false); }
           else
           { BuyLimitSystemAccountTable.getInstance().updateLimitItem(pc.getAccountName(), item, limit_account.getCount(), true); }
         }
       } else if (limit_type == 2 || limit_type == 4 || limit_type == 6) {
         BuyLimitSystemCharacter limit_char = BuyLimitSystemCharacterTable.getInstance().getLimitTable(pc, itemId);
         if (limit_char != null) {



           if (limit_char.getCount() <= 0) {
             pc.sendPackets(3460);
             return true;
           }
           if (count > limit_char.getCount()) {
             pc.sendPackets(3460);
             return true;
           }
           limit_char.setCount(limit_char.getCount() - count);
           limit_char.setBuyTime(new Timestamp(System.currentTimeMillis()));
           if (limit_type == 2) {
             BuyLimitSystemCharacterTable.getInstance().updateLimitItem(pc, item, limit_char.getCount(), false);
           } else {
             BuyLimitSystemCharacterTable.getInstance().updateLimitItem(pc, item, limit_char.getCount(), true);
           }
         }
       }
     }  return false;
   }
 }


