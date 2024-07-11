 package l1j.server.server.model.item.collection.favor.loader;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.concurrent.ConcurrentHashMap;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookTypeObject;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
 import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;
 import l1j.server.server.utils.SQLUtil;


 public class L1FavorBookUserLoader
 {
   private static Logger _log = Logger.getLogger(L1FavorBookUserLoader.class.getName());
   private static L1FavorBookUserLoader _instance;
   private static final ConcurrentHashMap<Integer, ArrayList<L1FavorBookUserObject>> DATA = new ConcurrentHashMap<>();


   private static final String UPSERT_QUERY = "INSERT INTO character_favorbook SET charObjId=?, listType=?, type=?, widthIndex=?, itemObjId=?, itemId=?, itemName=?, count=?, enchantLevel=?, attrLevel=?, bless=?, endTime=? ON DUPLICATE KEY UPDATE itemObjId=?, itemId=?, itemName=?, count=?, enchantLevel=?, attrLevel=?, bless=?, endTime=?";



   public static ArrayList<L1FavorBookUserObject> getFavorUserList(int charObjId) {
     return DATA.get(Integer.valueOf(charObjId));
   }


   public static L1FavorBookUserLoader getInstance() {
     if (_instance == null) {
       _instance = new L1FavorBookUserLoader();
     }
     return _instance;
   }

   private L1FavorBookUserLoader() {
     load();
   }


   private void load() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     L1ItemInstance item = null;
     ItemTable itemTable = ItemTable.getInstance();
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_favorbook");
       rs = pstm.executeQuery();
       while (rs.next()) {
         int charObjId = rs.getInt("charObjId");
         L1FavorBookListType listType = L1FavorBookListType.getListType(rs.getString("listType"));
         L1FavorBookTypeObject type = L1FavorBookLoader.getType(rs.getInt("type"));
         int index = rs.getInt("widthIndex");
         L1FavorBookObject obj = L1FavorBookLoader.getFavor(listType, type, index);
         if (obj == null) {
           System.out.println(String.format("[L1FavorBookUserLoader] FAVOR OBJ NOT FOUND listType(%s), type(%d), index(%d)", new Object[] { listType
                   .getName(), Integer.valueOf(type.getType()), Integer.valueOf(index) }));
           continue;
         }
         int itemObjId = rs.getInt("itemObjId");
         int itemId = rs.getInt("itemId");
         int count = rs.getInt("count");
         int enchantLevel = rs.getInt("enchantLevel");
         int attrLevel = rs.getInt("attrLevel");
         int bless = rs.getInt("bless");
         Timestamp endTime = rs.getTimestamp("endTime");
         item = itemTable.createItem(itemId, itemObjId, true);
         if (item != null) {
           item.setCount(count);
           item.setEnchantLevel(enchantLevel);
           item.setAttrEnchantLevel(attrLevel);
           item.setBless(bless);
           item.setEndTime(endTime);
           item.setIdentified(true);
         }

         ArrayList<L1FavorBookUserObject> list = DATA.get(Integer.valueOf(charObjId));
         if (list == null) {
           list = new ArrayList<>();
           DATA.put(Integer.valueOf(charObjId), list);
         }

         list.add(new L1FavorBookUserObject(listType, type, index, item, obj));
       }
     } catch (SQLException e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } catch (Exception e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }


   public boolean insert(final L1PcInstance pc, final L1FavorBookUserObject user) {
     int result = Updator.exec("INSERT INTO character_favorbook SET charObjId=?, listType=?, type=?, widthIndex=?, itemObjId=?, itemId=?, itemName=?, count=?, enchantLevel=?, attrLevel=?, bless=?, endTime=? ON DUPLICATE KEY UPDATE itemObjId=?, itemId=?, itemName=?, count=?, enchantLevel=?, attrLevel=?, bless=?, endTime=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             L1ItemInstance item = user.getCurrentItem();
             int i = 0;
             pstm.setInt(++i, pc.getId());
             pstm.setString(++i, user.getListType().getName());
             pstm.setInt(++i, user.getType().getType());
             pstm.setInt(++i, user.getIndex());
             pstm.setInt(++i, item.getId());
             pstm.setInt(++i, item.getItemId());
             pstm.setString(++i, item.getName());
             pstm.setInt(++i, item.getCount());
             pstm.setInt(++i, item.getEnchantLevel());
             pstm.setInt(++i, item.getAttrEnchantLevel());
             pstm.setInt(++i, item.getBless());
             pstm.setTimestamp(++i, item.getEndTime());
             pstm.setInt(++i, item.getId());
             pstm.setInt(++i, item.getItemId());
             pstm.setString(++i, item.getName());
             pstm.setInt(++i, item.getCount());
             pstm.setInt(++i, item.getEnchantLevel());
             pstm.setInt(++i, item.getAttrEnchantLevel());
             pstm.setInt(++i, item.getBless());
             pstm.setTimestamp(++i, item.getEndTime());
           }
         });
     if (result > 0) {
       ArrayList<L1FavorBookUserObject> list = DATA.get(Integer.valueOf(pc.getId()));
       if (list == null) {
         list = new ArrayList<>();
         DATA.put(Integer.valueOf(pc.getId()), list);
       }
       list.remove(user);
       return true;
     }
     return false;
   }


   public void merge(L1PcInstance pc) {
     L1FavorBookInventory favorBook = pc.getFavorBook();
     if (favorBook == null) {
       return;
     }
     ArrayList<L1FavorBookUserObject> list = favorBook.getList();
     if (list == null || list.isEmpty()) {
       return;
     }
     DATA.put(Integer.valueOf(pc.getId()), new ArrayList<>(list));

     Connection con = null;
     PreparedStatement pstm = null;
     L1ItemInstance item = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       con.setAutoCommit(false);

       pstm = con.prepareStatement("INSERT INTO character_favorbook SET charObjId=?, listType=?, type=?, widthIndex=?, itemObjId=?, itemId=?, itemName=?, count=?, enchantLevel=?, attrLevel=?, bless=?, endTime=? ON DUPLICATE KEY UPDATE itemObjId=?, itemId=?, itemName=?, count=?, enchantLevel=?, attrLevel=?, bless=?, endTime=?");
       for (L1FavorBookUserObject user : list) {
         int i = 0;
         item = user.getCurrentItem();
         pstm.setInt(++i, pc.getId());
         pstm.setString(++i, user.getListType().getName());
         pstm.setInt(++i, user.getType().getType());
         pstm.setInt(++i, user.getIndex());
         pstm.setInt(++i, item.getId());
         pstm.setInt(++i, item.getItemId());
         pstm.setString(++i, item.getName());
         pstm.setInt(++i, item.getCount());
         pstm.setInt(++i, item.getEnchantLevel());
         pstm.setInt(++i, item.getAttrEnchantLevel());
         pstm.setInt(++i, item.getBless());
         pstm.setTimestamp(++i, item.getEndTime());
         pstm.setInt(++i, item.getId());
         pstm.setInt(++i, item.getItemId());
         pstm.setString(++i, item.getName());
         pstm.setInt(++i, item.getCount());
         pstm.setInt(++i, item.getEnchantLevel());
         pstm.setInt(++i, item.getAttrEnchantLevel());
         pstm.setInt(++i, item.getBless());
         pstm.setTimestamp(++i, item.getEndTime());
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


   public boolean delete(L1PcInstance pc, final L1FavorBookUserObject user) {
     int result = Updator.exec("DELETE FROM character_favorbook WHERE type = ? AND widthIndex = ?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, user.getType().getType());
             pstm.setInt(2, user.getIndex());
           }
         });
     if (result > 0) {
       ArrayList<L1FavorBookUserObject> list = DATA.get(Integer.valueOf(pc.getId()));
       if (list == null) {
         return false;
       }
       list.remove(user);
       return true;
     }
     return false;
   }


   public void remove(int charId) {
     DATA.remove(Integer.valueOf(charId));
   }
 }


