 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1PetItem;
 import l1j.server.server.utils.SQLUtil;




















 public class PetItemTable
 {
   private static Logger _log = Logger.getLogger(PetItemTable.class
       .getName());

   private static PetItemTable _instance;

   private final HashMap<Integer, L1PetItem> _petItemIdIndex = new HashMap<>();


   public static PetItemTable getInstance() {
     if (_instance == null) {
       _instance = new PetItemTable();
     }
     return _instance;
   }

   private PetItemTable() {
     loadPetItem();
   }

   private void loadPetItem() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM petitem");
       rs = pstm.executeQuery();
       fillPetItemTable(rs);
     } catch (Exception e) {
       _log.log(Level.SEVERE, "error while creating etcitem_petitem table", e);
     } finally {

       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   private void fillPetItemTable(ResultSet rs) throws SQLException {
     L1PetItem petItem = null;
     while (rs.next()) {
       petItem = new L1PetItem();
       petItem.setItemId(rs.getInt("item_id"));
       petItem.setHitModifier(rs.getInt("hitmodifier"));
       petItem.setDamageModifier(rs.getInt("dmgmodifier"));
       petItem.setAddAc(rs.getInt("ac"));
       petItem.setAddStr(rs.getInt("add_str"));
       petItem.setAddCon(rs.getInt("add_con"));
       petItem.setAddDex(rs.getInt("add_dex"));
       petItem.setAddInt(rs.getInt("add_int"));
       petItem.setAddWis(rs.getInt("add_wis"));
       petItem.setAddHp(rs.getInt("add_hp"));
       petItem.setAddMp(rs.getInt("add_mp"));
       petItem.setAddSp(rs.getInt("add_sp"));
       petItem.setAddMr(rs.getInt("m_def"));
       this._petItemIdIndex.put(Integer.valueOf(petItem.getItemId()), petItem);
     }
   }

   public L1PetItem getTemplate(int itemId) {
     return this._petItemIdIndex.get(Integer.valueOf(itemId));
   }
 }


