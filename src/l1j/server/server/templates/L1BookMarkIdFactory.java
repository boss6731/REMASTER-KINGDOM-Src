 package l1j.server.server.templates;
 import java.sql.ResultSet;
 import java.util.concurrent.atomic.AtomicInteger;
 import l1j.server.MJTemplate.MJObjectWrapper;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

 public class L1BookMarkIdFactory {
   private static L1BookMarkIdFactory m_instance;

   public static L1BookMarkIdFactory getInstance() {
     if (m_instance == null)
       m_instance = new L1BookMarkIdFactory();
     return m_instance;
   }
   private AtomicInteger m_id;

   private L1BookMarkIdFactory() {
     this.m_id = new AtomicInteger(load());
   }

   public int next_id() {
     return this.m_id.getAndIncrement();
   }

   private int load() {
     final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper();
     wrapper.value = Integer.valueOf(0);
     Selector.exec("SELECT max(id)+1 as newid FROM character_teleport", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception
           {
             if (rs.next())
               wrapper.value = Integer.valueOf(rs.getInt("newid"));
           }
         });
     return ((Integer)wrapper.value).intValue();
   }
 }


