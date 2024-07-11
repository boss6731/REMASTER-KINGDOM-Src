 package MJNCoinSystem;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;

 public class MJNCoinCharacterReport {
   private static MJNCoinCharacterReport m_instance;
   private ConcurrentHashMap<Integer, MJNCoinCharacterInfo> m_characters_report;

   public static MJNCoinCharacterReport getInstance() {
     if (m_instance == null)
       m_instance = new MJNCoinCharacterReport();
     return m_instance;
   }


   private MJNCoinCharacterReport() {
     do_load();
   }

   public MJNCoinCharacterInfo get_character_info_notfound_created(int object_id, String character_name) {
     MJNCoinCharacterInfo cInfo = get_character_info(object_id);
     return (cInfo == null) ? create_character_info(object_id, character_name) : cInfo;
   }

   public MJNCoinCharacterInfo get_character_info(int object_id) {
     return this.m_characters_report.get(Integer.valueOf(object_id));
   }






   public MJNCoinCharacterInfo create_character_info(int object_id, String character_name) {
     MJNCoinCharacterInfo cInfo = MJNCoinCharacterInfo.newInstance().set_character_object_id(object_id).set_character_name(character_name).set_buy_price(0L).set_selling_price(0L).do_store_new_character();
     this.m_characters_report.put(Integer.valueOf(cInfo.get_character_object_id()), cInfo);
     return cInfo;
   }

   private void do_load() {
     final ConcurrentHashMap<Integer, MJNCoinCharacterInfo> characters_report = new ConcurrentHashMap<>(512);
     Selector.exec("select * from ncoin_character_report", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               MJNCoinCharacterReport.MJNCoinCharacterInfo o = MJNCoinCharacterReport.MJNCoinCharacterInfo.newInstance(rs);
               characters_report.put(Integer.valueOf(o.get_character_object_id()), o);
             }
           }
         });
     this.m_characters_report = characters_report;
   }
   public static class MJNCoinCharacterInfo { private int m_character_object_id; private String m_character_name;

     static MJNCoinCharacterInfo newInstance(ResultSet rs) throws SQLException {
       return newInstance()
         .set_character_object_id(rs.getInt("character_object_id"))
         .set_character_name(rs.getString("character_name"))
         .set_selling_price(rs.getLong("selling_price"))
         .set_buy_price(rs.getLong("buy_price"));
     }
     private long m_selling_price; private long m_buy_price;
     static MJNCoinCharacterInfo newInstance() {
       return new MJNCoinCharacterInfo();
     }

     MJNCoinCharacterInfo do_store_new_character() {
       Updator.exec("insert into ncoin_character_report set character_object_id=?, character_name=?, selling_price=?, buy_price=?", new Handler()
           {
             public void handle(PreparedStatement pstm) throws Exception {
               int idx = 0;
               pstm.setInt(++idx, MJNCoinCharacterReport.MJNCoinCharacterInfo.this.get_character_object_id());
               pstm.setString(++idx, MJNCoinCharacterReport.MJNCoinCharacterInfo.this.get_character_name());
               pstm.setLong(++idx, MJNCoinCharacterReport.MJNCoinCharacterInfo.this.get_selling_price());
               pstm.setLong(++idx, MJNCoinCharacterReport.MJNCoinCharacterInfo.this.get_buy_price());
             }
           });
       return this;
     }

     public void do_update() {
       Updator.exec("update ncoin_character_report set selling_price=?, buy_price=? where character_object_id=?", new Handler()
           {
             public void handle(PreparedStatement pstm) throws Exception
             {
               int idx = 0;
               pstm.setLong(++idx, MJNCoinCharacterReport.MJNCoinCharacterInfo.this.get_selling_price());
               pstm.setLong(++idx, MJNCoinCharacterReport.MJNCoinCharacterInfo.this.get_buy_price());
               pstm.setInt(++idx, MJNCoinCharacterReport.MJNCoinCharacterInfo.this.get_character_object_id());
             }
           });
     }







     public MJNCoinCharacterInfo set_character_object_id(int character_object_id) {
       this.m_character_object_id = character_object_id;
       return this;
     }
     public MJNCoinCharacterInfo set_character_name(String character_name) {
       this.m_character_name = character_name;
       return this;
     }
     public MJNCoinCharacterInfo set_selling_price(long selling_price) {
       this.m_selling_price = selling_price;
       return this;
     }
     public MJNCoinCharacterInfo add_selling_price(long added_selling_price) {
       this.m_selling_price += added_selling_price;
       return this;
     }
     public MJNCoinCharacterInfo set_buy_price(long buy_price) {
       this.m_buy_price = buy_price;
       return this;
     }
     public MJNCoinCharacterInfo add_buy_price(long added_buy_price) {
       this.m_buy_price += added_buy_price;
       return this;
     }
     public int get_character_object_id() {
       return this.m_character_object_id;
     }
     public String get_character_name() {
       return this.m_character_name;
     }
     public long get_selling_price() {
       return this.m_selling_price;
     }
     public long get_buy_price() {
       return this.m_buy_price;
     } }

 }


