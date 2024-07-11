 package l1j.server.server.datatables;

 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

 public class CraftProbability {
   public static final CraftProbability DEFAULT = new CraftProbability("craft_probability");
   public static final CraftProbability EVENT = new CraftProbability("craft_probability_event");
   private String m_table_name;

   private CraftProbability(String table_name) {
     this.m_table_name = table_name;
   }
   private HashMap<Integer, Integer> m_craft_probabilities;
   public void load_probabilities() {
     final HashMap<Integer, Integer> probs = new HashMap<>(256);
     Selector.exec(String.format("select * from %s", new Object[] { this.m_table_name }), (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next())
               probs.put(Integer.valueOf(rs.getInt("craft_id")), Integer.valueOf(rs.getInt("prob_by_million")));
           }
         });
     this.m_craft_probabilities = probs;
   }

   public static void reload() {
     DEFAULT.load_probabilities();
     EVENT.load_probabilities();
   }

   public Integer get(Integer craft_id) {
     return this.m_craft_probabilities.get(craft_id);
   }
 }


