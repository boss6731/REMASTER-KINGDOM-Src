 package l1j.server.server.model.Warehouse;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.logging.Logger;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.utils.SQLUtil;

 public class SupplementaryService extends Warehouse {
   private static final long serialVersionUID = 1L;
   protected static Logger _log = Logger.getLogger(SupplementaryService.class.getName());

   public SupplementaryService(String an) {
     super(an);
   }


   protected int getMax() {
     return Config.ServerAdSetting.WAREHOUSEMAXITEM;
   }


   public synchronized void loadItems() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_supplementary_service WHERE account_name = ?");
       pstm.setString(1, getName());
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
         item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
         item.setSpecialEnchant(rs.getInt("special_enchant"));
         item.setBless(rs.getInt("bless"));
         item.set_Carving(rs.getInt("Carving"));
         item.set_Doll_Bonus_Level(rs.getInt("doll_bonus_level"));
         item.set_Doll_Bonus_Value(rs.getInt("doll_bonus_value"));
         item.set_bless_level(rs.getInt("bless_level"));
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
     PreparedStatement pstm1 = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm1 = con.prepareStatement("SELECT * FROM character_supplementary_service WHERE id = ?");
       pstm1.setInt(1, item.getId());
       rs = pstm1.executeQuery();

       if (!rs.next()) {
         pstm = con.prepareStatement("INSERT INTO character_supplementary_service SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, attr_enchantlvl = ?, bless = ?, Carving =?, special_enchant = ?, package = ?, doll_bonus_level = ?, doll_bonus_value = ?, bless_level=?, bless_type=?, bless_type_value=?");



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
         pstm.setInt(13, item.getBless());
         pstm.setInt(14, item.get_Carving());
         pstm.setInt(15, item.getSpecialEnchant());
         pstm.setInt(16, !item.isPackage() ? 0 : 1);
         pstm.setInt(17, item.get_Doll_Bonus_Level());
         pstm.setInt(18, item.get_Doll_Bonus_Value());
         pstm.setInt(19, item.get_bless_level());
         pstm.setInt(20, item.getBlessType());
         pstm.setInt(21, item.getBlessTypeValue());
         pstm.executeUpdate();
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(pstm1);
       SQLUtil.close(con);
     }
   }


   public synchronized void updateItem(L1ItemInstance item) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("UPDATE character_supplementary_service SET count = ? WHERE id = ?");
       pstm.setInt(1, item.getCount());
       pstm.setInt(2, item.getId());
       pstm.executeUpdate();
     } catch (Exception e) {
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
       pstm = con.prepareStatement("DELETE FROM character_supplementary_service WHERE id = ?");
       pstm.setInt(1, item.getId());
       pstm.executeUpdate();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     this._items.remove(this._items.indexOf(item));
   }
 }


