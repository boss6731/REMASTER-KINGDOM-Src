 package l1j.server.server.model.Warehouse;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.concurrent.CopyOnWriteArrayList;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.L1Inventory;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.utils.SQLUtil;

 public abstract class Warehouse
   extends L1Object
 {
   private static final long serialVersionUID = 1L;
   protected List<L1ItemInstance> _items = new CopyOnWriteArrayList<>();

   private final String name;

   public Warehouse(String n) {
     this.name = n;
   }

   public String getName() {
     return this.name;
   }

   public abstract void loadItems();

   public abstract void deleteItem(L1ItemInstance paramL1ItemInstance);

   public abstract void insertItem(L1ItemInstance paramL1ItemInstance);

   public abstract void updateItem(L1ItemInstance paramL1ItemInstance);

   protected abstract int getMax();

   public L1ItemInstance findItemId(int id) {
     for (L1ItemInstance item : this._items) {
       if (item.getItem().getItemId() == id) {
         return item;
       }
     }
     return null;
   }

   public L1ItemInstance findId(int objectId) {
     for (L1ItemInstance item : this._items) {
       if (item.getId() == objectId) {
         return item;
       }
     }
     return null;
   }

   public synchronized L1ItemInstance storeTradeItem(L1ItemInstance item) {
     if (item.isStackable()) {
       L1ItemInstance findItem = findItemId(item.getItem().getItemId());
       if (findItem != null) {
         findItem.setCount(findItem.getCount() + item.getCount());
         updateItem(findItem);
         return findItem;
       }
     }

       L1ItemInstance previousItem = findId(item.getId());
       if (previousItem != null) {
           // 探測到非堆疊物品重複時，打印堆疊追蹤信息
           (new Throwable(String.format("檢測到非堆疊倉庫物品重複。(objectId : %d, name : %s)", new Object[] { Integer.valueOf(item.getId()), item.getName() }))).printStackTrace();
           return null; // 返回null，表示操作失敗
       }

        // 設定物品的位置和地圖
       item.setX(getX());
       item.setY(getY());
       item.setMap(getMapId());

        // 將物品添加到當前物品列表
       this._items.add(item);

        // 將物品插入到數據庫或其他存儲結構中
       insertItem(item);

        // 返回插入的物品
       return item;
   public synchronized L1ItemInstance tradeItem(L1ItemInstance item, int count, L1Inventory inventory) {
     L1ItemInstance carryItem;
     if (item == null)
       return null;
     if (item.getCount() <= 0 || count <= 0)
       return null;
     if (item.isEquipped())
       return null;
     if (!checkItem(item.getItem().getItemId(), count)) {
       return null;
     }
     int over_count = count + inventory.countItems(40308);
     if (over_count > Config.ServerAdSetting.ADEN_OVER_COUNT &&
       inventory.consumeItem(Config.ServerAdSetting.ADEN_ITEMID, Config.ServerAdSetting.ADEN_COUNT)) {
       inventory.storeItem(Config.ServerAdSetting.REWARD_ITEMID, Config.ServerAdSetting.REWARD_COUNT);
     }





     if (item.getCount() <= count || count < 0) {
       carryItem = item;
       deleteItem(item);
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

       carryItem.setPackage(item.isPackage());
       carryItem.set_bless_level(item.get_bless_level());
       carryItem.setEndTime(item.getEndTime());

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

   public static void present(String account, int itemid, int enchant, int count) throws Exception {
     L1Item temp = ItemTable.getInstance().getTemplate(itemid);
     if (temp == null) {
       return;
     }
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       if (account.compareToIgnoreCase("*") == 0) {
         pstm = con.prepareStatement("SELECT * FROM accounts");
       } else {
         pstm = con.prepareStatement("SELECT * FROM accounts WHERE login=?");
         pstm.setString(1, account);
       }
       rs = pstm.executeQuery();

       ArrayList<String> accountList = new ArrayList<>();
       while (rs.next()) {
         accountList.add(rs.getString("login"));
       }

       present(accountList, itemid, enchant, count);
     }
     catch (Exception e) {
       e.printStackTrace();
       throw e;
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public static void present(int minlvl, int maxlvl, int itemid, int enchant, int count) throws Exception {
     L1Item temp = ItemTable.getInstance().getTemplate(itemid);
     if (temp == null) {
       return;
     }
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       pstm = con.prepareStatement("SELECT distinct(account_name) as account_name FROM characters WHERE level between ? and ?");
       pstm.setInt(1, minlvl);
       pstm.setInt(2, maxlvl);
       rs = pstm.executeQuery();

       ArrayList<String> accountList = new ArrayList<>();
       while (rs.next()) {
         accountList.add(rs.getString("account_name"));
       }

       present(accountList, itemid, enchant, count);
     }
     catch (Exception e) {
       e.printStackTrace();
       throw e;
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }



       private static void present(ArrayList<String> accountList, int itemid, int enchant, int count) throws Exception {
           // 獲取物品模板
           L1Item temp = ItemTable.getInstance().getTemplate(itemid);
           if (temp == null) {
               throw new Exception("不存在的物品 ID");
           }
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       con.setAutoCommit(false);
       L1ItemInstance item = null;
       for (String account : accountList) {
         if (temp.isStackable()) {
           item = ItemTable.getInstance().createItem(itemid);
           item.setEnchantLevel(enchant);
           item.setCount(count);

           pstm = con.prepareStatement("INSERT INTO character_supplementary_service SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?");
           pstm.setInt(1, item.getId());
           pstm.setString(2, account);
           pstm.setInt(3, item.getItemId());
           pstm.setString(4, item.getName());
           pstm.setInt(5, item.getCount());
           pstm.setInt(6, item.getEnchantLevel());
           pstm.setInt(7, item.isIdentified() ? 1 : 0);
           pstm.setInt(8, item.get_durability());
           pstm.setInt(9, item.getChargeCount());
           pstm.setInt(10, item.getRemainingTime());
           pstm.executeUpdate(); continue;
         }
         item = null;

         for (int createCount = 0; createCount < count; createCount++) {
           item = ItemTable.getInstance().createItem(itemid);
           item.setEnchantLevel(enchant);

           pstm = con.prepareStatement("INSERT INTO character_supplementary_service SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?");
           pstm.setInt(1, item.getId());
           pstm.setString(2, account);
           pstm.setInt(3, item.getItemId());
           pstm.setString(4, item.getName());
           pstm.setInt(5, item.getCount());
           pstm.setInt(6, item.getEnchantLevel());
           pstm.setInt(7, item.isIdentified() ? 1 : 0);
           pstm.setInt(8, item.get_durability());
           pstm.setInt(9, item.getChargeCount());
           pstm.setInt(10, item.getRemainingTime());
           pstm.executeUpdate();
         }
       }


         con.commit(); // 提交數據庫事務
         con.setAutoCommit(true); // 設置自動提交為 true
     } catch (Exception e) {
         try {
             con.rollback(); // 回滾數據庫事務
         } catch (Exception ignore) {
             ignore.printStackTrace(); // 打印回滾時的異常（如果有）
         }

         e.printStackTrace(); // 打印原始異常
         throw new Exception("present 處理中發生錯誤。"); // 拋出新的異常，帶有錯誤信息
     } finally {
         SQLUtil.close(pstm); // 關閉 PreparedStatement
         SQLUtil.close(con); // 關閉數據庫連接
     }

   public L1ItemInstance getItem(int objectId) {
     L1ItemInstance item = null;
     for (L1ItemInstance itemObject : this._items) {
       item = itemObject;
       if (item.getId() == objectId) {
         return item;
       }
     }
     return null;
   }

   public synchronized void removeItem(L1ItemInstance item) {
     if (this._items.contains(item))
       this._items.remove(item);
     deleteItem(item);
   }

   public List<L1ItemInstance> getItems() {
     return this._items;
   }

   public void clearItems() {
     L1ItemInstance item = null;
     for (L1ItemInstance itemObject : this._items) {
       item = itemObject;
       L1World.getInstance().removeObject((L1Object)item);
     }
     this._items.clear();
   }

   public L1ItemInstance[] findItemsId(int id) {
     ArrayList<L1ItemInstance> itemList = new ArrayList<>();
     for (L1ItemInstance item : this._items) {
       if (item.getItemId() == id) {
         itemList.add(item);
       }
     }
     return itemList.<L1ItemInstance>toArray(new L1ItemInstance[0]);
   }

   public boolean checkItem(int id, int count) {
     if (count == 0)
       return true;
     if (ItemTable.getInstance().getTemplate(id).isStackable()) {
       L1ItemInstance item = findItemId(id);
       if (item != null && item.getCount() >= count) {
         return true;
       }
     } else {
       L1ItemInstance[] arrayOfL1ItemInstance = findItemsId(id);
       if (arrayOfL1ItemInstance.length >= count) {
         return true;
       }
     }
     return false;
   }

   public int getSize() {
     return this._items.size();
   }

   public int checkAddItemToWarehouse(L1ItemInstance item, int count) {
     if (item == null) {
       return -1;
     }
     if (item.getCount() <= 0 || count <= 0) {
       return -1;
     }
     int OK = 0, SIZE_OVER = 1;
     int maxSize = getMax(), SIZE = getSize();

     if (SIZE > maxSize || (SIZE == maxSize && (!item.isStackable() || !checkItem(item.getItem().getItemId(), 1)))) {
       return 1;
     }
     return 0;
   }
 }


