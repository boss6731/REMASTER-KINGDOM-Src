 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.utils.SQLUtil;





 public class KeyTable
 {
   public static void StoreKey(L1ItemInstance item) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("INSERT INTO item_key_boss SET item_obj_id=?, key_id=?");

       pstm.setInt(1, item.getId());
       pstm.setInt(2, item.getKeyId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static void DeleteKey(L1ItemInstance item) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM item_key_boss WHERE item_obj_id=?");
       pstm.setInt(1, item.getId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public static void DeleteKeyId(int keyId) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM item_key_boss WHERE key_id=?");
       pstm.setInt(1, keyId);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public static void initBossKey() {
     Connection con = null;
     PreparedStatement pstm = null;
     PreparedStatement pstm1 = null;
     PreparedStatement pstm2 = null;
     PreparedStatement pstm3 = null;
     PreparedStatement pstm4 = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("truncate `item_key_boss`;");
       pstm1 = con.prepareStatement("DELETE FROM character_items WHERE item_id = 80500");
       pstm2 = con.prepareStatement("DELETE FROM character_elf_warehouse WHERE item_id = 80500");
       pstm3 = con.prepareStatement("DELETE FROM clan_warehouse WHERE item_id = 80500");
       pstm4 = con.prepareStatement("DELETE FROM character_warehouse WHERE item_id = 80500");
       pstm.execute();
       pstm1.execute();
       pstm2.execute();
       pstm3.execute();
       pstm4.execute();
     } catch (SQLException e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(pstm1);
       SQLUtil.close(pstm2);
       SQLUtil.close(pstm3);
       SQLUtil.close(pstm4);
       SQLUtil.close(con);
     }
   }


   public static boolean checkey(L1ItemInstance item) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM item_key_boss WHERE item_obj_id=?");

       pstm.setInt(1, item.getId());
       rs = pstm.executeQuery();
       while (rs.next()) {
         int itemObj = rs.getInt("item_obj_id");
         if (item.getId() == itemObj) {
           item.setKeyId(rs.getInt("key_id"));
           return true;
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return false;
   }
 }


