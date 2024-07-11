 package l1j.server.server.datatables;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.HashMap;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

 public class CraftLimitTimeTrigger {
   private static HashMap<Integer, CraftLimitTimeTrigger> m_triggers;
   private int m_craft_id;

   public static void do_load() {
     final HashMap<Integer, CraftLimitTimeTrigger> triggers = new HashMap<>();
     Selector.exec("select * from craft_limit_trigger", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               CraftLimitTimeTrigger o = CraftLimitTimeTrigger.newInstance(rs);
               triggers.put(Integer.valueOf(o.get_craft_id()), o);
             }
           }
         });
     m_triggers = triggers;
   }
   private String m_item_name; private int m_inherit_item_id; private long m_limit_millis;
   public static CraftLimitTimeTrigger get_trigger(int craft_id) {
     return m_triggers.get(Integer.valueOf(craft_id));
   }

   private static CraftLimitTimeTrigger newInstance(ResultSet rs) throws SQLException {
     return newInstance()
       .set_craft_id(rs.getInt("craft_id"))
       .set_item_name(rs.getString("item_name"))
       .set_limit_millis(rs.getLong("limit_seconds") * 1000L)
       .set_inherit_item_id(rs.getInt("inherit_item_id"));
   }

   private static CraftLimitTimeTrigger newInstance() {
     return new CraftLimitTimeTrigger();
   }







   public CraftLimitTimeTrigger set_craft_id(int craft_id) {
     this.m_craft_id = craft_id;
     return this;
   }
   public CraftLimitTimeTrigger set_item_name(String item_name) {
     this.m_item_name = item_name;
     return this;
   }
   public CraftLimitTimeTrigger set_limit_millis(long limit_millis) {
     this.m_limit_millis = limit_millis;
     return this;
   }
   public CraftLimitTimeTrigger set_inherit_item_id(int inherit_item_id) {
     this.m_inherit_item_id = inherit_item_id;
     return this;
   }
   public int get_craft_id() {
     return this.m_craft_id;
   }
   public String get_item_name() {
     return this.m_item_name;
   }
   public long get_limit_millis() {
     return this.m_limit_millis;
   }
   public int get_inherit_item_id() {
     return this.m_inherit_item_id;
   }
 }


