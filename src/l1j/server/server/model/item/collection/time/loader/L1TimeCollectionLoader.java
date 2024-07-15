 package l1j.server.server.model.item.collection.time.loader;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.concurrent.ConcurrentHashMap;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
 import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionAblity;
 import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionDuration;
 import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionMaterial;
 import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;
 import l1j.server.server.utils.SQLUtil;


 public class L1TimeCollectionLoader
 {
   private static Logger _log = Logger.getLogger(L1TimeCollectionLoader.class.getName());

   private static L1TimeCollectionLoader _instance;
   private static final ConcurrentHashMap<L1TimeCollectionType, ConcurrentHashMap<Integer, L1TimeCollection>> DATA = new ConcurrentHashMap<>();
   private static final ConcurrentHashMap<Integer, L1TimeCollection> FLAG_DATA = new ConcurrentHashMap<>();


   public static ConcurrentHashMap<L1TimeCollectionType, ConcurrentHashMap<Integer, L1TimeCollection>> getAllData() {
     return DATA;
   }

   public static ConcurrentHashMap<Integer, L1TimeCollection> getTypeData(L1TimeCollectionType type) {
     return DATA.get(type);
   }

   public static L1TimeCollection getData(L1TimeCollectionType type, int collectionIndex) {
     ConcurrentHashMap<Integer, L1TimeCollection> map = getTypeData(type);
     if (map == null || map.isEmpty()) {
       return null;
     }
     return map.get(Integer.valueOf(collectionIndex));
   }


   public static L1TimeCollection getData(int flag) {
     return FLAG_DATA.get(Integer.valueOf(flag));
   }

   public static L1TimeCollectionLoader getInstance() {
     if (_instance == null) {
       _instance = new L1TimeCollectionLoader();
     }
     return _instance;
   }

   private L1TimeCollectionLoader() {
     load();
   }

   private void load() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     L1TimeCollection obj = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();


       pstm = con.prepareStatement("SELECT * FROM time_collection");
       rs = pstm.executeQuery();
       while (rs.next()) {
         obj = new L1TimeCollection(rs);
         ConcurrentHashMap<Integer, L1TimeCollection> map = DATA.get(obj.getType());
         if (map == null) {
           map = new ConcurrentHashMap<>();
           DATA.put(obj.getType(), map);
         }
         map.put(Integer.valueOf(obj.getCollectionIndex()), obj);
         FLAG_DATA.put(Integer.valueOf(obj.getFlag()), obj);
       }
       SQLUtil.close(rs, pstm);


       pstm = con.prepareStatement("SELECT * FROM time_collection_duration");
       rs = pstm.executeQuery();
       while (rs.next()) {
         int flag = rs.getInt("flag");
         obj = FLAG_DATA.get(Integer.valueOf(flag));
         if (obj == null) {
           System.out.println(String.format("[L1TimeCollectionLoader] 找不到持續時間標誌 (%d)", new Object[] { Integer.valueOf(flag) }));
           continue;
         }
         obj.putDuration(new L1TimeCollectionDuration(rs));
       }
       SQLUtil.close(rs, pstm);


       pstm = con.prepareStatement("SELECT * FROM time_collection_material");
       rs = pstm.executeQuery();
       while (rs.next()) {
         int flag = rs.getInt("flag");
         obj = FLAG_DATA.get(Integer.valueOf(flag));
         if (obj == null) {
           System.out.println(String.format("[L1TimeCollectionLoader] 材料找不到標誌 (%d)", new Object[] { Integer.valueOf(flag) }));
           continue;
         }
         obj.putMaterial(new L1TimeCollectionMaterial(rs));
       }
       SQLUtil.close(rs, pstm);


       pstm = con.prepareStatement("SELECT * FROM time_collection_ablity");
       rs = pstm.executeQuery();
       while (rs.next()) {
         int flag = rs.getInt("flag");
         obj = FLAG_DATA.get(Integer.valueOf(flag));
         if (obj == null) {
           System.out.println(String.format("[L1TimeCollectionLoader] 找不到能力標誌 (%d)", new Object[] { Integer.valueOf(flag) }));
           continue;
         }
         obj.putAblity(new L1TimeCollectionAblity(rs));
       }


       for (L1TimeCollection last : FLAG_DATA.values()) {
         last.setLastValue();
       }
     } catch (SQLException e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } catch (Exception e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }


   public void reload() {
     if (!DATA.isEmpty()) {
       for (ConcurrentHashMap<Integer, L1TimeCollection> map : DATA.values()) {
         if (map == null || map.isEmpty()) {
           continue;
         }
         map.clear();
       }
       DATA.clear();
     }
     FLAG_DATA.clear();
     load();
   }
 }


