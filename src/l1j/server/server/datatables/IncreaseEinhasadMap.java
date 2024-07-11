 package l1j.server.server.datatables;

 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

 public class IncreaseEinhasadMap {
   private static IncreaseEinhasadMap _instance;

   public static IncreaseEinhasadMap getInstance() {
     if (_instance == null)
       _instance = new IncreaseEinhasadMap();
     return _instance;
   }
   private HashMap<Integer, Double> m_increaseInfo;
   public static void release() {
     if (_instance != null) {
       _instance.dispose();
       _instance = null;
     }
   }

   public static void reload() {
     IncreaseEinhasadMap old = _instance;
     _instance = new IncreaseEinhasadMap();
     if (old != null) {
       old.dispose();
       old = null;
     }
   }


   private IncreaseEinhasadMap() {
     load();
   }

   private void load() {
     this.m_increaseInfo = new HashMap<>(32);
     Selector.exec("select * from tb_increase_einhasad_map", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {}



           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               int ratio = rs.getInt("ratio");

               IncreaseEinhasadMap.this.m_increaseInfo.put(Integer.valueOf(rs.getInt("target_map_id")), new Double(ratio * 0.01D));
             }
           }
         });
   }

   public void dispose() {
     if (this.m_increaseInfo != null) {
       this.m_increaseInfo.clear();
       this.m_increaseInfo = null;
     }
   }

   public double increaseEinhasadValue(int mapId, double val) {
     if (!this.m_increaseInfo.containsKey(Integer.valueOf(mapId))) {
       return val;
     }
     double increase = ((Double)this.m_increaseInfo.get(Integer.valueOf(mapId))).doubleValue();
     return val * increase;
   }
 }


