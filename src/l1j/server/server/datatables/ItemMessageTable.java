 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Set;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1ItemMessage;
 import l1j.server.server.utils.SQLUtil;



 public class ItemMessageTable
 {
   public static ItemMessageTable _instance;
   public Map<Integer, L1ItemMessage> _drop_list = new HashMap<>();
   public Map<Integer, L1ItemMessage> _enchant_list = new HashMap<>();
   public Map<Integer, L1ItemMessage> _craft_list = new HashMap<>();

   public static ItemMessageTable getInstance() {
     if (_instance == null) {
       _instance = new ItemMessageTable();
     }
     return _instance;
   }

   public static void reload() {
     ItemMessageTable oldInstance = _instance;
     _instance = new ItemMessageTable();
     oldInstance._drop_list.clear();
     oldInstance._enchant_list.clear();
     oldInstance._craft_list.clear();
   }

   private ItemMessageTable() {
     loadItemMessage();
   }

   private void loadItemMessage() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM item_message");
       rs = pstm.executeQuery();
       while (rs.next()) {
         L1ItemMessage CM = new L1ItemMessage();
         String type = rs.getString("type");
         int itemid = rs.getInt("itemid");
         CM.setItemId(rs.getInt("itemid"));
         CM.setOption(rs.getInt("option"));
         CM.setItemName(rs.getString("itemname"));
         CM.setMentuse(rs.getBoolean("ment_use"));
         CM.setMent(rs.getString("ment"));
         if (type.equalsIgnoreCase("Drop")) {
           CM.setType(1);
           this._drop_list.put(Integer.valueOf(itemid), CM); continue;
         }  if (type.equalsIgnoreCase("Enchant")) {
           CM.setType(2);
           this._enchant_list.put(Integer.valueOf(itemid), CM); continue;
         }  if (type.equalsIgnoreCase("Craft")) {
           CM.setType(3);
           this._craft_list.put(Integer.valueOf(itemid), CM);
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public L1ItemMessage getPickUpMessage(int itemid) {
     return this._drop_list.get(Integer.valueOf(itemid));
   }

   public boolean isPickUpMessage(int itemid) {
     Set<Integer> keys = this._drop_list.keySet();

     boolean OK = false;
     for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext(); ) {
       int itemmsg = ((Integer)iterator.next()).intValue();
       if (itemmsg == itemid) {
         OK = true;
         break;
       }
     }
     return OK;
   }

   public L1ItemMessage getEnchantMessage(int itemid) {
     return this._enchant_list.get(Integer.valueOf(itemid));
   }

   public boolean isEnchantMessage(int itemid) {
     Set<Integer> keys = this._enchant_list.keySet();

     boolean OK = false;
     for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext(); ) {
       int itemmsg = ((Integer)iterator.next()).intValue();
       if (itemmsg == itemid) {
         OK = true;
         break;
       }
     }
     return OK;
   }

   public L1ItemMessage getCraftMessage(int itemid) {
     return this._craft_list.get(Integer.valueOf(itemid));
   }

   public boolean isCraftMessage(int itemid) {
     Set<Integer> keys = this._craft_list.keySet();

     boolean OK = false;
     for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext(); ) {
       int itemmsg = ((Integer)iterator.next()).intValue();
       if (itemmsg == itemid) {
         OK = true;
         break;
       }
     }
     return OK;
   }
 }


