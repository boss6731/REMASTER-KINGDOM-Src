 package l1j.server.server.model.Warehouse;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.utils.SQLUtil;

 public class ClanWarehouse extends Warehouse {
   private int _warehouse = 0; private static final long serialVersionUID = 1L;
   public ClanWarehouse(String clan) {
     super(clan);
   }


   protected int getMax() {
     return Config.ServerAdSetting.CLANWAREHOUSEMAXITEM;
   }


   public synchronized void loadItems() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM clan_warehouse WHERE clan_name = ?");
       pstm.setString(1, getName());
       rs = pstm.executeQuery();
       L1ItemInstance item = null;
       L1Item itemTemplate = null;
       while (rs.next()) {
         item = new L1ItemInstance();
         int objectId = rs.getInt("id");
         item.setId(objectId);
         int itemId = rs.getInt("item_id");
         itemTemplate = ItemTable.getInstance().getTemplate(itemId);
         if (itemTemplate == null) {
           try {
             throw new IllegalArgumentException(String.format("item_id=%d not found.", new Object[] { Integer.valueOf(itemId) }));
           } catch (Exception e) {
             e.printStackTrace();
             continue;
           }
         }
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
         item.set_Doll_Bonus_Level(rs.getInt("doll_bonus_level"));
         item.set_Doll_Bonus_Value(rs.getInt("doll_bonus_value"));
         item.setBlessType(rs.getInt("bless_type"));
         item.setBlessTypeValue(rs.getInt("bless_type_value"));
         this._items.add(item);
         L1World.getInstance().storeObject((L1Object)item);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public synchronized void insertItem(L1ItemInstance item) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("INSERT INTO clan_warehouse SET id = ?, clan_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id= ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, attr_enchantlvl = ?, special_enchant = ?, package = ?, item_level = ?, Hotel_Town =?, end_time =?, doll_bonus_level =?, doll_bonus_value =?,bless_type=?, bless_type_value=?");



       pstm.setInt(1, item.getId());
       pstm.setString(2, getName());
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
       pstm.setInt(14, !item.isPackage() ? 0 : 1);

       pstm.setInt(15, item.get_item_level());

       pstm.setString(16, item.getHotel_Town());
       pstm.setTimestamp(17, item.getEndTime());
       pstm.setInt(18, item.get_Doll_Bonus_Level());
       pstm.setInt(19, item.get_Doll_Bonus_Value());
       pstm.setInt(20, item.getBlessType());
       pstm.setInt(21, item.getBlessTypeValue());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public synchronized void updateItem(L1ItemInstance item) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("UPDATE clan_warehouse SET count = ? WHERE id = ?");
       pstm.setInt(1, item.getCount());
       pstm.setInt(2, item.getId());
       pstm.execute();
     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public synchronized void deleteItem(L1ItemInstance item) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM clan_warehouse WHERE id = ?");
       pstm.setInt(1, item.getId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     this._items.remove(item);
   }

   public synchronized void deleteAllItems() {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM clan_warehouse WHERE clan_name = ?");
       pstm.setString(1, getName());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
   public int getWarehouseUsingChar() {
     return this._warehouse;
   }

   public boolean setWarehouseUsingChar(int objid, int prevOwner) {
     synchronized (this) {
       if (objid == this._warehouse) {
         return true;
       }

       if (objid != 0 && this._warehouse != 0 &&
         prevOwner != this._warehouse) {
         return false;
       }

       this._warehouse = objid;

       return true;
     }
   }
 }


