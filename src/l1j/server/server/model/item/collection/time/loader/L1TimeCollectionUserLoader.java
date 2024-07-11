 package l1j.server.server.model.item.collection.time.loader;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
 import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
 import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
 import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
 import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;
 import l1j.server.server.utils.SQLUtil;
 import l1j.server.server.utils.StringUtil;


 public class L1TimeCollectionUserLoader
 {
   private static Logger _log = Logger.getLogger(L1TimeCollectionUserLoader.class.getName());

   private static L1TimeCollectionUserLoader _instance;
   private static final ConcurrentHashMap<Integer, ArrayList<L1TimeCollectionUser>> DATA = new ConcurrentHashMap<>();


   private static final String UPSERT_QUERY = "INSERT INTO character_time_collection SET charObjId=?, flag=?, type=?, collectionIndex=?, registItem=?, registComplet=?, buffType=?, buffTime=?, refill_count=? ON DUPLICATE KEY UPDATE type=?, collectionIndex=?, registItem=?, registComplet=?, buffType=?, buffTime=?, refill_count=?";

   private static final String DIVID_STRING = "===============";


   public static ArrayList<L1TimeCollectionUser> getUserList(int charObjId) {
     ArrayList<L1TimeCollectionUser> list = DATA.get(Integer.valueOf(charObjId));
     if (list != null && !list.isEmpty()) {
       long currentTime = System.currentTimeMillis();
       ArrayList<L1TimeCollectionUser> deleteList = null;
       for (L1TimeCollectionUser user : list) {
         if (user.getBuffTime() != null && user.getBuffTime().getTime() <= currentTime) {
           if (deleteList == null) {
             deleteList = new ArrayList<>();
           }
           deleteList.add(user);
         }
       }
       if (deleteList != null && !deleteList.isEmpty()) {
         delete(deleteList);
         for (L1TimeCollectionUser delete : deleteList) {
           list.remove(delete);
         }
         deleteList.clear();
         deleteList = null;
       }
     }
     return list;
   }


   public static L1TimeCollectionUserLoader getInstance() {
     if (_instance == null) {
       _instance = new L1TimeCollectionUserLoader();
     }
     return _instance;
   }


   private L1TimeCollectionUserLoader() {
     load();
   }

   private void load() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     L1TimeCollectionUser user = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();


       pstm = con.prepareStatement("DELETE FROM character_time_collection WHERE (buffTime IS NOT NULL AND buffTime < NOW()) OR NULLIF(registItem, '') IS NULL");
       pstm.execute();
       SQLUtil.close(pstm);


       pstm = con.prepareStatement("SELECT * FROM character_time_collection");
       rs = pstm.executeQuery();
       while (rs.next()) {
         int charObjId = rs.getInt("charObjId");
         int flag = rs.getInt("flag");

         L1TimeCollection obj = L1TimeCollectionLoader.getData(flag);
         if (obj == null) {
           System.out.println(String.format("[L1TimeCollectionUserLoader] TEMPLATE_NOT_FOUND FLAG(%d)", new Object[] { Integer.valueOf(flag) }));

           continue;
         }
         L1TimeCollectionType type = L1TimeCollectionType.getType(rs.getString("type"));
         int collectionIndex = rs.getInt("collectionIndex");
         ConcurrentHashMap<Integer, L1ItemInstance> registItem = parseRegistItemToLoad(rs.getString("registItem"));
         boolean registComplet = Boolean.valueOf(rs.getString("registComplet")).booleanValue();
         int sumEnchant = 0;
         if (registComplet && registItem != null && !registItem.isEmpty()) {
           for (L1ItemInstance item : registItem.values()) {
             sumEnchant += item.getEnchantLevel();
           }
         }
         L1TimeCollectionBuffType buffType = L1TimeCollectionBuffType.getType(rs.getString("buffType"));
         Timestamp buffTime = rs.getTimestamp("buffTime");
         int refill_count = rs.getInt("refill_count");

         user = new L1TimeCollectionUser(charObjId, flag, type, collectionIndex, registItem, registComplet, sumEnchant, buffType, buffTime, obj, refill_count);

         ArrayList<L1TimeCollectionUser> list = DATA.get(Integer.valueOf(user.getCharObjId()));
         if (list == null) {
           list = new ArrayList<>();
           DATA.put(Integer.valueOf(user.getCharObjId()), list);
         }
         list.add(user);
       }
     } catch (SQLException e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } catch (Exception e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public boolean insert(L1TimeCollectionUser user) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("INSERT INTO character_time_collection SET charObjId=?, flag=?, type=?, collectionIndex=?, registItem=?, registComplet=?, buffType=?, buffTime=?, refill_count=? ON DUPLICATE KEY UPDATE type=?, collectionIndex=?, registItem=?, registComplet=?, buffType=?, buffTime=?, refill_count=?");

       String registItem = parseRegistItemToInsert(user.getRegistItem());

       int i = 0;
       pstm.setInt(++i, user.getCharObjId());
       pstm.setInt(++i, user.getFlag());
       pstm.setString(++i, user.getType().getName());
       pstm.setInt(++i, user.getCollectionIndex());
       pstm.setString(++i, registItem);
       pstm.setString(++i, Boolean.toString(user.isRegistComplet()));
       pstm.setString(++i, user.getBuffType().getName());
       pstm.setTimestamp(++i, user.getBuffTime());
       pstm.setInt(++i, user.getRefill_count());

       pstm.setString(++i, user.getType().getName());
       pstm.setInt(++i, user.getCollectionIndex());
       pstm.setString(++i, registItem);
       pstm.setString(++i, Boolean.toString(user.isRegistComplet()));
       pstm.setString(++i, user.getBuffType().getName());
       pstm.setTimestamp(++i, user.getBuffTime());
       pstm.setInt(++i, user.getRefill_count());

       if (pstm.executeUpdate() > 0) {
         ArrayList<L1TimeCollectionUser> list = DATA.get(Integer.valueOf(user.getCharObjId()));
         if (list == null) {
           list = new ArrayList<>();
           DATA.put(Integer.valueOf(user.getCharObjId()), list);
         }
         list.add(user);
         return true;
       }
     } catch (SQLException e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } catch (Exception e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       SQLUtil.close(pstm, con);
     }
     return false;
   }

   public void merge(L1PcInstance pc) {
     L1TimeCollectionHandler handler = pc.getTimeCollection();
     if (handler == null) {
       return;
     }
     ConcurrentHashMap<Integer, L1TimeCollectionUser> map = handler.getData();
     if (map == null || map.isEmpty()) {
       return;
     }
     DATA.put(Integer.valueOf(pc.getId()), new ArrayList<>(map.values()));

     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       con.setAutoCommit(false);

       pstm = con.prepareStatement("INSERT INTO character_time_collection SET charObjId=?, flag=?, type=?, collectionIndex=?, registItem=?, registComplet=?, buffType=?, buffTime=?, refill_count=? ON DUPLICATE KEY UPDATE type=?, collectionIndex=?, registItem=?, registComplet=?, buffType=?, buffTime=?, refill_count=?");
       for (L1TimeCollectionUser user : map.values()) {
         if (user.getRegistItem() == null || user.getRegistItem().isEmpty()) {
           continue;
         }
         String registItem = parseRegistItemToInsert(user.getRegistItem());
         int i = 0;
         pstm.setInt(++i, user.getCharObjId());
         pstm.setInt(++i, user.getFlag());
         pstm.setString(++i, user.getType().getName());
         pstm.setInt(++i, user.getCollectionIndex());
         pstm.setString(++i, registItem);
         pstm.setString(++i, Boolean.toString(user.isRegistComplet()));
         pstm.setString(++i, user.getBuffType().getName());
         pstm.setTimestamp(++i, user.getBuffTime());
         pstm.setInt(++i, user.getRefill_count());

         pstm.setString(++i, user.getType().getName());
         pstm.setInt(++i, user.getCollectionIndex());
         pstm.setString(++i, registItem);
         pstm.setString(++i, Boolean.toString(user.isRegistComplet()));
         pstm.setString(++i, user.getBuffType().getName());
         pstm.setTimestamp(++i, user.getBuffTime());
         pstm.setInt(++i, user.getRefill_count());
         pstm.addBatch();
         pstm.clearParameters();
       }

       pstm.executeBatch();
       pstm.clearBatch();
       con.commit();
     } catch (SQLException e) {
       try {
         con.rollback();
       } catch (SQLException sqle) {
         sqle.printStackTrace();
       }
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } catch (Exception e) {
       try {
         con.rollback();
       } catch (SQLException sqle) {
         sqle.printStackTrace();
       }
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       try {
         con.setAutoCommit(true);
       } catch (SQLException e) {
         e.printStackTrace();
       }
       SQLUtil.close(pstm, con);
     }
   }


   public boolean delete(L1TimeCollectionUser user) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM character_time_collection WHERE charObjId=? AND flag=?");
       pstm.setInt(1, user.getCharObjId());
       pstm.setInt(2, user.getFlag());
       if (pstm.executeUpdate() > 0) {
         ArrayList<L1TimeCollectionUser> list = DATA.get(Integer.valueOf(user.getCharObjId()));
         if (list == null) {
           return false;
         }
         list.remove(user);
         return true;
       }
     } catch (SQLException e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } catch (Exception e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       SQLUtil.close(pstm, con);
     }
     return false;
   }

   private static void delete(ArrayList<L1TimeCollectionUser> deleteList) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       con.setAutoCommit(false);

       pstm = con.prepareStatement("DELETE FROM character_time_collection WHERE charObjId=? AND flag=?");
       for (L1TimeCollectionUser user : deleteList) {
         pstm.setInt(1, user.getCharObjId());
         pstm.setInt(2, user.getFlag());
         pstm.addBatch();
         pstm.clearParameters();
       }
       pstm.executeBatch();
       pstm.clearBatch();
       con.commit();
     } catch (SQLException e) {
       try {
         con.rollback();
       } catch (SQLException sqle) {
         sqle.printStackTrace();
       }
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } catch (Exception e) {
       try {
         con.rollback();
       } catch (SQLException sqle) {
         sqle.printStackTrace();
       }
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       try {
         con.setAutoCommit(true);
       } catch (SQLException e) {
         e.printStackTrace();
       }
       SQLUtil.close(pstm, con);
     }
   }


   public void remove(int charObjId) {
     DATA.remove(Integer.valueOf(charObjId));
   }


   private ConcurrentHashMap<Integer, L1ItemInstance> parseRegistItemToLoad(String str) {
     if (StringUtil.isNullOrEmpty(str)) {
       return null;
     }
     ConcurrentHashMap<Integer, L1ItemInstance> map = new ConcurrentHashMap<>();
     String[] array = str.split("===============");

     ItemTable itemTable = ItemTable.getInstance();
     L1ItemInstance item = null;
     for (int i = 0; i < array.length; i++) {
       String one = array[i];
       if (!StringUtil.isNullOrEmpty(one)) {


         String[] tempArray = one.split("\r\n");
         int slotIndex = 0, itemId = 0, enchant = 0;
         for (int j = 0; j < tempArray.length; j++) {
           String temp = tempArray[j].trim();
           if (!StringUtil.isNullOrEmpty(temp))
           {

             if (temp.startsWith("SLOT_INDEX:")) {
               slotIndex = Integer.parseInt(temp.replace("SLOT_INDEX:", "").trim());
             } else if (temp.startsWith("商品編號：")) {
               itemId = Integer.parseInt(temp.replace("商品編號：", "").trim());
             } else if (temp.startsWith("附魔：")) {
               enchant = Integer.parseInt(temp.replace("附魔：", "").trim());
             }  }
         }
         if (slotIndex > 0 && itemId > 0 && enchant > 0)

         {
           item = itemTable.createItem(itemId);
           if (item == null)
           { System.out.println(String.format("[L1TimeCollectionUserLoader] 找不到項目範本 ITEMID(%d)", new Object[] { Integer.valueOf(itemId) })); }
           else

           { item.setEnchantLevel(enchant);
             item.setIdentified(true);

             map.put(Integer.valueOf(slotIndex), item); }  }
       }
     }  return map;
   }

   private String parseRegistItemToInsert(ConcurrentHashMap<Integer, L1ItemInstance> map) {
     if (map == null || map.isEmpty()) {
       return "";
     }
     StringBuilder sb = new StringBuilder();
     int key = 0;
     L1ItemInstance value = null;
     int cnt = 0;
     for (Map.Entry<Integer, L1ItemInstance> entry : map.entrySet()) {
       key = ((Integer)entry.getKey()).intValue();
       value = entry.getValue();
       sb.append("SLOT_INDEX:").append(key).append("\r\n");
       sb.append("商品編號：").append(value.getItemId()).append("\r\n");
       sb.append("附魔：").append(value.getEnchantLevel()).append("\r\n");
       sb.append("===============");
       if (++cnt < map.size()) {
         sb.append("\r\n");
       }
     }
     return sb.toString();
   }
 }


