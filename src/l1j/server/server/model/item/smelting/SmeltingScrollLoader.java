 package l1j.server.server.model.item.smelting;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

 public class SmeltingScrollLoader {
   private static SmeltingScrollLoader _instance;

   public static SmeltingScrollLoader getInstance() {
     if (_instance == null)
       _instance = new SmeltingScrollLoader();
     return _instance;
   }
   private HashMap<Integer, SmeltingScrollInfo> _smeltingscroll;
   public static void reload() {
     if (_instance != null) {
       _instance = new SmeltingScrollLoader();
     }
   }


   private SmeltingScrollLoader() {
     load();
   }

   private void load() {
     final HashMap<Integer, SmeltingScrollInfo> bonus = new HashMap<>(256);
     Selector.exec("select * from smelting_scroll", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               SmeltingScrollInfo Info = SmeltingScrollInfo.newInstance(rs);
               if (Info == null)
                 continue;
               bonus.put(Integer.valueOf(Info.get_item_id()), Info);
             }
           }
         });
     this._smeltingscroll = bonus;
   }

   public SmeltingScrollInfo getSmeltingScrollInfo(int itemId) {
     if (this._smeltingscroll.containsKey(Integer.valueOf(itemId))) {
       return this._smeltingscroll.get(Integer.valueOf(itemId));
     }
     return null;
   }
 }


