 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import javolution.util.FastMap;
 import javolution.util.FastTable;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.utils.SQLUtil;




 public class ItemSelectorTable
 {
   private static Logger _log = Logger.getLogger(ItemSelectorTable.class.getName());

   public class SelectorData {
     public int _itemId;
     public int _index;
     public int _selectItemId;
     public int _enchantLevel;

     public SelectorData(ResultSet rs) throws SQLException {
       this._itemId = rs.getInt("itemId");
       this._index = rs.getInt("index");
       this._selectItemId = rs.getInt("selectItemId");
       this._enchantLevel = rs.getInt("enchantLevel");
       this._attrLevel = rs.getInt("attrLevel");
       this._count = rs.getInt("count");

       this._endTime = rs.getInt("endTime");
       this._item = ItemTable.getInstance().createItem(this._selectItemId);
       if (this._item != null) {
         this._item.setIdentified(true);
         this._item.setEnchantLevel(this._enchantLevel);
         this._item.setAttrEnchantLevel(this._attrLevel);
       }
     }




     public int _attrLevel;



     public int _count;


     public int _endTime;


     public L1ItemInstance _item;


     public boolean _isDelete;
   }



   public void reload() {
     for (FastTable<SelectorData> data : (Iterable<FastTable<SelectorData>>)_dataMap.values()) data.clear();
     _dataMap.clear();
     load();
   }

   private static final FastMap<Integer, FastTable<SelectorData>> _dataMap = new FastMap();
   private static ItemSelectorTable _instance;

   public static ItemSelectorTable getInstance() {
     if (_instance == null) _instance = new ItemSelectorTable();
     return _instance;
   }

   public static boolean isSelectorInfo(int itemId) {
     return _dataMap.containsKey(Integer.valueOf(itemId));
   }


   public static FastTable<SelectorData> getSelectorInfo(int itemId) {
     return _dataMap.containsKey(Integer.valueOf(itemId)) ? (FastTable<SelectorData>)_dataMap.get(Integer.valueOf(itemId)) : null;
   }


   private ItemSelectorTable() {
     load();
   }

   private void load() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     SelectorData data = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM item_selector");
       rs = pstm.executeQuery();
       while (rs.next()) {
         data = new SelectorData(rs);
         FastTable<SelectorData> list = (FastTable<SelectorData>)_dataMap.get(Integer.valueOf(data._itemId));
         if (list == null) {
           list = new FastTable();
           _dataMap.put(Integer.valueOf(data._itemId), list);
         }
         list.add(data);
       }

     } catch (Exception e) {
       _log.log(Level.SEVERE, "ItemSelectorTable[]Error", e);
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }
 }


