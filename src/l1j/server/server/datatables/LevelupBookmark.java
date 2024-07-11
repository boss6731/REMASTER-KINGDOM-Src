 package l1j.server.server.datatables;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.HashMap;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.templates.L1BookMark;

 public class LevelupBookmark {
   private static LevelupBookmark m_instance;

   public static LevelupBookmark getInstance() {
     if (m_instance == null)
       m_instance = new LevelupBookmark();
     return m_instance;
   }
   private HashMap<Integer, ArrayList<L1BookMark>> m_bookmarks;
   public static void reload() {
     m_instance = new LevelupBookmark();
   }


   private LevelupBookmark() {
     this.m_bookmarks = do_load();
   }

   private HashMap<Integer, ArrayList<L1BookMark>> do_load() {
     final HashMap<Integer, ArrayList<L1BookMark>> bookmarks = new HashMap<>();
     Selector.exec("select * from levelup_addteleport order by id asc", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               int level = rs.getInt("level");
               ArrayList<L1BookMark> list = (ArrayList<L1BookMark>)bookmarks.get(Integer.valueOf(level));
               if (list == null) {
                 list = new ArrayList<>();
                 bookmarks.put(Integer.valueOf(level), list);
               }
               L1BookMark bookmark = new L1BookMark();
               bookmark.setId(rs.getInt("id"));
               bookmark.setNumId(rs.getInt("num_id"));
               bookmark.setSpeed_id(-1);
               bookmark.setCharId(0);
               bookmark.setName(rs.getString("name"));
               bookmark.setLocX(rs.getInt("locx"));
               bookmark.setLocY(rs.getInt("locy"));
               bookmark.setMapId(rs.getShort("mapid"));
               bookmark.setRandomX(rs.getInt("randomX"));
               bookmark.setRandomY(rs.getInt("randomY"));
               list.add(bookmark);
             }
           }
         });
     return bookmarks;
   }

   public void on_level(L1PcInstance pc) {
     ArrayList<L1BookMark> list = this.m_bookmarks.get(Integer.valueOf(pc.getHighLevel()));
     if (list == null) {
       return;
     }

     L1BookMark.addBookmark(pc, list);
   }
 }


