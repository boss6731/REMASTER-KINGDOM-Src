 package l1j.server.server.model;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.utils.SQLUtil;


 public class L1DwarfForPackageInventory
   extends L1Inventory
 {
   private static final long serialVersionUID = 1L;
   private final L1PcInstance _owner;

   public L1DwarfForPackageInventory(L1PcInstance owner) {
     this._owner = owner;
   }


   public void loadItems() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_package_warehouse WHERE account_name = ?");
       pstm.setString(1, this._owner.getAccountName());

       rs = pstm.executeQuery();
       L1ItemInstance item = null;
       L1Item itemTemplate = null;
       while (rs.next()) {
         item = new L1ItemInstance();
         int objectId = rs.getInt("id");
         item.setId(objectId);
         itemTemplate = ItemTable.getInstance().getTemplate(rs.getInt("item_id"));
         item.setItem(itemTemplate);
         item.setCount(rs.getInt("count"));
         item.setEquipped(false);
         item.setEnchantLevel(rs.getInt("enchantlvl"));
         item.setIdentified((rs.getInt("is_id") != 0));
         item.set_durability(rs.getInt("durability"));
         item.setChargeCount(rs.getInt("charge_count"));
         item.setRemainingTime(rs.getInt("remaining_time"));
         item.setLastUsed(rs.getTimestamp("last_used"));
         item.setBless(item.getItem().getBless());
         item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
         item.setSpecialEnchant(rs.getInt("special_enchant"));
         item.set_bless_level(rs.getInt("bless_level"));

         item.set_item_level(rs.getInt("item_level"));

         item.setHotel_Town(rs.getString("Hotel_Town"));
         item.setEndTime(rs.getTimestamp("end_time"));

         if (item.getItemId() == 40308) {

           L1ItemInstance itemExist = findItemId(item.getItemId());

           if (itemExist != null) {

             deleteItem(item);

             int newCount = itemExist.getCount() + item.getCount();

             if (newCount <= 2000000000) {

               if (newCount < 0)
               {
                 newCount = 0;
               }
               itemExist.setCount(newCount);

               updateItem(itemExist);
             }

             continue;
           }
           this._items.add(item);
           L1World.getInstance().storeObject((L1Object)item);

           continue;
         }

         this._items.add(item);
         L1World.getInstance().storeObject((L1Object)item);
       }

     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public void insertItem(L1ItemInstance item) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       pstm = con.prepareStatement("INSERT INTO character_package_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, attr_enchantlvl = ?, special_enchant = ?, item_level = ?, Hotel_Town =?, end_time =?");
       pstm.setInt(1, item.getId());
       pstm.setString(2, this._owner.getAccountName());
       pstm.setInt(3, item.getItemId());
       pstm.setString(4, item.getName());
       pstm.setInt(5, item.getCount());
       pstm.setInt(6, item.getEnchantLevel());
       pstm.setInt(7, item.isIdentified() ? 1 : 0);
       pstm.setInt(8, item.get_durability());
       pstm.setInt(9, item.getChargeCount());
       pstm.setInt(10, item.getRemainingTime());
       pstm.setTimestamp(11, item.getLastUsed());
       pstm.setInt(12, item.getAttrEnchantLevel());
       pstm.setInt(13, item.getSpecialEnchant());

       pstm.setInt(14, item.get_item_level());

       pstm.setString(15, item.getHotel_Town());
       pstm.setTimestamp(16, item.getEndTime());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     this._items.add(item);
   }


   public void updateItem(L1ItemInstance item) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       pstm = con.prepareStatement("UPDATE character_package_warehouse SET count = ? WHERE id = ?");
       pstm.setInt(1, item.getCount());
       pstm.setInt(2, item.getId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public void deleteItem(L1ItemInstance item) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       pstm = con.prepareStatement("DELETE FROM character_package_warehouse WHERE id = ?");
       pstm.setInt(1, item.getId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }

     this._items.remove(this._items.indexOf(item));
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
       // 獲取指定itemid對應的物品模板
         L1Item temp = ItemTable.getInstance().getTemplate(itemid);
         // 如果物品模板為空，則拋出異常
         if (temp == null) {
             throw new Exception("不存在的物品 ID");
         }

         Connection con = null;
         PreparedStatement pstm = null;
         try {
             // 獲取數據庫連接
             con = L1DatabaseFactory.getInstance().getConnection();
             // 設置自動提交為false
             con.setAutoCommit(false);

             L1ItemInstance item = null;
             // 遍歷所有帳戶
             for (String account : accountList) {
                 // 這裡可能會有更多的代碼來創建和分配物品給帳戶
                 // 你可以在此處添加具體的實現
             }
         } catch (Exception e) {
             // 捕捉並處理異常
             throw e;
         } finally {
             // 確保語句和連接在最終塊中被關閉
             SQLUtil.close(pstm);
             SQLUtil.close(con);
         }
    }

         if (temp.isStackable()) {
           item = ItemTable.getInstance().createItem(itemid);
           item.setEnchantLevel(enchant);
           item.setCount(count);

           pstm = con.prepareStatement("INSERT INTO character_package_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?");
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
           pstm.execute(); continue;
         }
         item = null;

         for (int createCount = 0; createCount < count; createCount++) {
           item = ItemTable.getInstance().createItem(itemid);
           item.setEnchantLevel(enchant);


           pstm = con.prepareStatement("INSERT INTO character_package_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?");
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
           pstm.execute();
         }
       }

    private static void present(ArrayList<String> accountList, int itemid, int enchant, int count) throws Exception {
     // 獲取指定itemid對應的物品模板
           L1Item temp = ItemTable.getInstance().getTemplate(itemid);
           // 如果物品模板為空，則拋出異常
           if (temp == null) {
               throw new Exception("不存在的物品 ID");
           }

           Connection con = null;
           PreparedStatement pstm = null;
           try {
               // 獲取數據庫連接
               con = L1DatabaseFactory.getInstance().getConnection();
               // 設置自動提交為false
               con.setAutoCommit(false);

               L1ItemInstance item = null;
               // 遍歷所有帳戶
               for (String account : accountList) {
                   // 這裡應該包含具體的物品創建和分配邏輯
                   // 你可以在此處添加具體的實現
               }

               // 提交數據庫更改
               con.commit();
               // 恢復自動提交模式
               con.setAutoCommit(true);
           } catch (Exception e) {
               try {
                   // 發生異常時回滾更改
                   con.rollback();
               } catch (Exception ignore) {
                   ignore.printStackTrace();
               }
               // 打印異常信息
               e.printStackTrace();
               // 拋出新的異常，包含具體的錯誤信息
               throw new Exception("在處理 .present 過程中發生錯誤。");
           } finally {
               // 確保語句和連接在最終塊中被關閉
               SQLUtil.close(pstm);
               SQLUtil.close(con);
           }
  }


