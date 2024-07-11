 package l1j.server.server.datatables;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

 public class MonsterParalyzeDelay {
   private static MonsterParalyzeDelay _instance;

   public static MonsterParalyzeDelay getInstance() {
     if (_instance == null)
       _instance = new MonsterParalyzeDelay();
     return _instance;
   }
   private HashMap<Integer, MonsterParalyze> m_paralyzes; private MonsterParalyze m_default_paralyze;
   public static void reload() {
     _instance = new MonsterParalyzeDelay();
   }



   private MonsterParalyzeDelay() {
     this.m_paralyzes = new HashMap<>();
     Selector.exec("select * from monster_paralyze", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               MonsterParalyzeDelay.MonsterParalyze p = new MonsterParalyzeDelay.MonsterParalyze();
               p.skill_id = rs.getInt("skill_id");
               p.paralyze_delay = rs.getInt("paralyze_delay");
               p.paralyze_millis = rs.getInt("paralyze_millis");
               MonsterParalyzeDelay.this.m_paralyzes.put(Integer.valueOf(p.skill_id), p);
             }
           }
         });

     this.m_default_paralyze = new MonsterParalyze();
     this.m_default_paralyze.skill_id = 0;
     this.m_default_paralyze.paralyze_delay = 6000;
     this.m_default_paralyze.paralyze_millis = 10000;
   }

   public boolean contains_paralyze(int skill_id) {
     return this.m_paralyzes.containsKey(Integer.valueOf(skill_id));
   }

     public MonsterParalyze get_paralyze(int skill_id) {
         // 從 m_paralyzes 字典中取得對應技能 ID 的 MonsterParalyze 物件
         MonsterParalyze paralyze = this.m_paralyzes.get(Integer.valueOf(skill_id));

         // 如果找不到對應的物件，輸出提示訊息並返回預設的 MonsterParalyze 物件
         if (paralyze == null) {
             // 輸出提示訊息，表示缺少對應的怪物麻痺資訊
             System.out.println(String.format("怪物麻痺資訊遺失。技能ID : %d", new Object[] { Integer.valueOf(skill_id) }));
             // 返回預設的怪物麻痺物件
             return this.m_default_paralyze;
         }

         // 返回找到的 MonsterParalyze 物件
         return paralyze;
     }


     public static class MonsterParalyze {
     public int skill_id;
     public int paralyze_delay;
     public int paralyze_millis;
   }
 }


