 package l1j.server.server.model.item.collection.favor.loader;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.StringTokenizer;
 import java.util.concurrent.ConcurrentHashMap;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookRegistObject;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookTypeObject;
 import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;
 import l1j.server.server.utils.SQLUtil;
 import l1j.server.server.utils.StringUtil;


 public class L1FavorBookLoader
 {
   private static Logger _log = Logger.getLogger(L1FavorBookLoader.class.getName());

   private static L1FavorBookLoader _instance;
   private static final ConcurrentHashMap<L1FavorBookListType, ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>>> DATA = new ConcurrentHashMap<>();
   private static final ConcurrentHashMap<Integer, L1FavorBookObject> DATA_TO_ITEMID = new ConcurrentHashMap<>();
   private static final ConcurrentHashMap<Integer, L1FavorBookTypeObject> TYPE_DATA = new ConcurrentHashMap<>();


   public static boolean isFavorItem(int itemId) {
     return DATA_TO_ITEMID.containsKey(Integer.valueOf(itemId));
   }



   public static L1FavorBookObject getFavor(int itemId) {
     return DATA_TO_ITEMID.get(Integer.valueOf(itemId));
   }


   public static ConcurrentHashMap<L1FavorBookListType, ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>>> getAllData() {
     return DATA;
   }


   public static ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> getListType(L1FavorBookListType listType) {
     return DATA.get(listType);
   }


   public static L1FavorBookObject getFavor(L1FavorBookListType listType, L1FavorBookTypeObject type, int index) {
     if (listType == L1FavorBookListType.ALL) {
       return selectToAll(type, index);
     }
     ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> map = getListType(listType);
     if (map == null || map.isEmpty()) {
       return null;
     }
     ArrayList<L1FavorBookObject> list = map.get(type);
     if (list == null || list.isEmpty()) {
       return null;
     }
     for (L1FavorBookObject obj : list) {
       if (!obj.getType().equals(type) || obj.getIndex() != index) {
         continue;
       }
       return obj;
     }
     return null;
   }


   private static L1FavorBookObject selectToAll(L1FavorBookTypeObject type, int index) {
     for (L1FavorBookObject obj : DATA_TO_ITEMID.values()) {
       if (!obj.getType().equals(type) || obj.getIndex() != index) {
         continue;
       }
       return obj;
     }
     return null;
   }

   public static L1FavorBookTypeObject getType(int type) {
     return TYPE_DATA.get(Integer.valueOf(type));
   }


   public static L1FavorBookLoader getInstance() {
     if (_instance == null) {
       _instance = new L1FavorBookLoader();
     }
     return _instance;
   }


   private L1FavorBookLoader() {
     load();
   }


   private void load() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     L1FavorBookObject favor = null;
     L1FavorBookTypeObject type = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();


       pstm = con.prepareStatement("SELECT * FROM favorbook_type");
       rs = pstm.executeQuery();
       while (rs.next()) {
         type = new L1FavorBookTypeObject(rs);
         TYPE_DATA.put(Integer.valueOf(type.getType()), type);
       }
       SQLUtil.close(rs, pstm);


       pstm = con.prepareStatement("SELECT * FROM favorbook ORDER BY type, widthIndex");
       rs = pstm.executeQuery();
       while (rs.next()) {
         L1FavorBookListType listType = L1FavorBookListType.getListType(rs.getString("listType"));
         type = TYPE_DATA.get(Integer.valueOf(rs.getInt("type")));
         int index = rs.getInt("widthIndex");
         ConcurrentHashMap<Integer, L1FavorBookRegistObject> register = parseRegister(rs.getString("regist"));

         ArrayList<Integer> itemIds = parseItemIds(rs.getString("itemIds"));
         favor = new L1FavorBookObject(listType, type, index, register, itemIds);

         if (itemIds != null && !itemIds.isEmpty()) {
           for (Iterator<Integer> iterator = itemIds.iterator(); iterator.hasNext(); ) { int itemId = ((Integer)iterator.next()).intValue();
             DATA_TO_ITEMID.put(Integer.valueOf(itemId), favor); }

         }

         ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> typeMap = DATA.get(listType);
         if (typeMap == null) {
           typeMap = new ConcurrentHashMap<>();
           DATA.put(listType, typeMap);
         }

         ArrayList<L1FavorBookObject> list = typeMap.get(type);
         if (list == null) {
           list = new ArrayList<>();
           typeMap.put(type, list);
         }
         list.add(favor);
       }
     } catch (SQLException e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } catch (Exception e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }


   private ConcurrentHashMap<Integer, L1FavorBookRegistObject> parseRegister(String str) {
     if (StringUtil.isNullOrEmpty(str)) {
       return null;
     }
     ConcurrentHashMap<Integer, L1FavorBookRegistObject> map = new ConcurrentHashMap<>();
     L1FavorBookRegistObject obj = null;
     StringTokenizer st = new StringTokenizer(str, "\r\n");
     while (st.hasMoreTokens()) {
       String[] pare = st.nextToken().split(":");
       if (pare == null || pare.length != 3) {
         continue;
       }
       obj = new L1FavorBookRegistObject(pare);
       map.put(Integer.valueOf(obj.getDescId()), obj);
     }
     return map;
   }


   private ArrayList<Integer> parseItemIds(String str) {
     if (StringUtil.isNullOrEmpty(str)) {
       return null;
     }
     ItemTable temp = ItemTable.getInstance();
     ArrayList<Integer> list = new ArrayList<>();
     StringTokenizer st = new StringTokenizer(str, "\r\n");
     while (st.hasMoreTokens()) {
       int itemId = Integer.parseInt(st.nextToken().trim());
       if (temp.getTemplate(itemId) == null) {
         System.out.println(String.format("[L1FavorBookLoader] ITEM NOT FOUND! ID: %d", new Object[] { Integer.valueOf(itemId) }));
         continue;
       }
       list.add(Integer.valueOf(itemId));
     }
     return list;
   }


   public void reload() {
     if (!DATA.isEmpty()) {
       for (ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> value : DATA.values()) {
         if (value == null || value.isEmpty()) {
           continue;
         }
         for (ArrayList<L1FavorBookObject> list : value.values()) {
           if (list == null || list.isEmpty()) {
             continue;
           }
           list.clear();
         }
       }
       DATA.clear();
     }
     DATA_TO_ITEMID.clear();
     TYPE_DATA.clear();
     load();
   }
 }


