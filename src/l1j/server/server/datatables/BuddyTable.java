 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.L1Buddy;
 import l1j.server.server.utils.SQLUtil;


 public class BuddyTable
 {
   private static BuddyTable _instance;
   private Map<Integer, ArrayList<L1Buddy>> _buddys = new HashMap<>();

   public static BuddyTable getInstance() {
     if (_instance == null) {
       _instance = new BuddyTable();
     }
     return _instance;
   }

   private BuddyTable() {
     loadBuddy(this._buddys);
   }

   public void reload() {
     Map<Integer, ArrayList<L1Buddy>> buddys = new HashMap<>();
     loadBuddy(buddys);
     this._buddys = buddys;
   }

   public ArrayList<L1Buddy> getBuddyList(int objid) {
     return this._buddys.get(Integer.valueOf(objid));
   }

   public L1Buddy getBuddy(int ojbid, String name) {
     ArrayList<L1Buddy> buddy_list = this._buddys.get(Integer.valueOf(ojbid));
     if (buddy_list == null) {
       return null;
     }
     L1Buddy buddy = null;
     for (int i = 0; i < buddy_list.size(); i++) {
       buddy = buddy_list.get(i);
       if (buddy != null &&
         buddy.getCharName().equalsIgnoreCase(name)) {
         return buddy;
       }
     }


     return null;
   }

   public boolean isBuddy(int ojbid, String name) {
     ArrayList<L1Buddy> buddy_list = this._buddys.get(Integer.valueOf(ojbid));
     if (buddy_list == null) {
       return false;
     }
     L1Buddy buddy = null;
     for (int i = 0; i < buddy_list.size(); i++) {
       buddy = buddy_list.get(i);
       if (buddy != null &&
         buddy.getCharName().equalsIgnoreCase(name)) {
         return true;
       }
     }


     return false;
   }


   private void loadBuddy(Map<Integer, ArrayList<L1Buddy>> buddys) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     L1Buddy buddy = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_buddys");
       rs = pstm.executeQuery();

       while (rs.next()) {
         int owner_obj_id = rs.getInt("char_id");

         ArrayList<L1Buddy> buddy_list = buddys.get(Integer.valueOf(owner_obj_id));
         if (buddy_list == null) {
           buddy_list = new ArrayList<>();
           buddys.put(Integer.valueOf(owner_obj_id), buddy_list);
         }

         buddy = new L1Buddy();

         buddy.setCharName(rs.getString("buddy_name"));
         buddy.setMemo(rs.getString("buddy_memo"));

         buddy_list.add(buddy);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public void addAndSetBuddy(int charId, String name, String memo) {
     ArrayList<L1Buddy> buddy_list = this._buddys.get(Integer.valueOf(charId));
     if (buddy_list == null) {
       buddy_list = new ArrayList<>();
       this._buddys.put(Integer.valueOf(charId), buddy_list);
     }

     L1Buddy buddy = null;
     L1Buddy new_buddy = null;
     for (int i = 0; i < buddy_list.size(); i++) {
       buddy = buddy_list.get(i);
       if (buddy != null &&
         buddy.getCharName().equalsIgnoreCase(name)) {
         new_buddy = buddy;

         break;
       }
     }

     if (new_buddy == null) {
       new_buddy = new L1Buddy();
       new_buddy.setCharName(name);
       new_buddy.setMemo(memo);
       buddy_list.add(new_buddy);
     } else {
       new_buddy.setCharName(name);
       new_buddy.setMemo(memo);
     }

     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("INSERT INTO character_buddys (char_id, buddy_name, buddy_memo) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE char_id=?,buddy_name=?,buddy_memo=?");

       pstm.setInt(1, charId);
       pstm.setString(2, name);
       pstm.setString(3, memo);
       pstm.setInt(4, charId);
       pstm.setString(5, name);
       pstm.setString(6, memo);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public void removeBuddy(int charId, String buddyName) {
     ArrayList<L1Buddy> buddy_list = this._buddys.get(Integer.valueOf(charId));

     if (buddy_list == null) {
       return;
     }
     L1Buddy buddy = null;

     for (Iterator<L1Buddy> iter = buddy_list.iterator(); iter.hasNext(); ) {
       buddy = iter.next();
       if (buddy.getCharName().equalsIgnoreCase(buddyName)) {
         iter.remove();
       }
     }

     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM character_buddys WHERE char_id=? AND buddy_name=?");
       pstm.setInt(1, charId);
       pstm.setString(2, buddyName);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }


