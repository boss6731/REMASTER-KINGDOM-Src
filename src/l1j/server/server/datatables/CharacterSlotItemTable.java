 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.templates.L1InventorySlot;
 import l1j.server.server.utils.SQLUtil;


 public class CharacterSlotItemTable
 {
   public static CharacterSlotItemTable _instance;

   public static CharacterSlotItemTable getInstance() {
     if (_instance == null) {
       _instance = new CharacterSlotItemTable();
     }
     return _instance;
   }

   public void selectCharSlot(L1PcInstance pc, int slotNum) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_slot_items WHERE char_id=? AND slot_number=?");
       pstm.setInt(1, pc.getId());
       pstm.setInt(2, slotNum);
       rs = pstm.executeQuery();
       while (rs.next()) {
         pc.addSlotItem(slotNum, rs.getInt("item_objid"), false);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public void updateCharSlotItems(L1PcInstance pc) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM character_slot_items WHERE char_id=?");
       pstm.setInt(1, pc.getId());
       pstm.executeUpdate();
       pstm.close();

       if (pc.getSlotItems(0) != null) {
         for (int i = 0; i < pc.getSlotItems(0).size(); i++) {
           pstm = con.prepareStatement("INSERT INTO character_slot_items SET item_objid=?, char_id=? ,slot_number=?");
           pstm.setInt(1, ((Integer)pc.getSlotItems(0).get(i)).intValue());
           pstm.setInt(2, pc.getId());
           pstm.setInt(3, 0);
           pstm.execute();
           pstm.close();
         }
       }

       if (pc.getSlotItems(1) != null) {
         for (int i = 0; i < pc.getSlotItems(1).size(); i++) {
           pstm = con.prepareStatement("INSERT INTO character_slot_items SET item_objid=?, char_id=? ,slot_number=?");
           pstm.setInt(1, ((Integer)pc.getSlotItems(1).get(i)).intValue());
           pstm.setInt(2, pc.getId());
           pstm.setInt(3, 1);
           pstm.execute();
           pstm.close();
         }
       }

       if (pc.getSlotItems(2) != null) {
         for (int i = 0; i < pc.getSlotItems(2).size(); i++) {
           pstm = con.prepareStatement("INSERT INTO character_slot_items SET item_objid=?, char_id=? ,slot_number=?");
           pstm.setInt(1, ((Integer)pc.getSlotItems(2).get(i)).intValue());
           pstm.setInt(2, pc.getId());
           pstm.setInt(3, 2);
           pstm.execute();
           pstm.close();
         }
       }

       if (pc.getSlotItems(3) != null) {
         for (int i = 0; i < pc.getSlotItems(3).size(); i++) {
           pstm = con.prepareStatement("INSERT INTO character_slot_items SET item_objid=?, char_id=? ,slot_number=?");
           pstm.setInt(1, ((Integer)pc.getSlotItems(3).get(i)).intValue());
           pstm.setInt(2, pc.getId());
           pstm.setInt(3, 3);
           pstm.execute();
           pstm.close();
         }

       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public void selectCharSlotcolor(final L1PcInstance pc) {
     Selector.exec("select * from character_slot_color where char_id=?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, pc.getId());
           }


           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               pc.addslotsetting(rs.getInt("slotNum"), rs.getInt("slot_color"), rs.getString("slot_name"));
             }
           }
         });
   }

   public void update_CharSlotcolor(final L1PcInstance pc, final int slotnum, final L1InventorySlot slot) {
     try {
       Updator.exec(String.format("insert into %s set char_id=?, slotNum=?, slot_color=?, slot_name=? on duplicate key update slot_color=?, slot_name=?", new Object[] { "character_slot_color" }), new Handler()
           {
             public void handle(PreparedStatement pstm) throws Exception {
               int idx = 0;
               pstm.setInt(++idx, pc.getId());
               pstm.setInt(++idx, slotnum);
               pstm.setInt(++idx, slot.get_Color());
               pstm.setString(++idx, slot.get_Slotname());
               pstm.setInt(++idx, slot.get_Color());
               pstm.setString(++idx, slot.get_Slotname());
             }
           });
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
 }


