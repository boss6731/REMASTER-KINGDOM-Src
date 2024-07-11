 package l1j.server.server.model.skill.noti;

 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

 public abstract class MJNotiSkillDatabaseProvider {
   public static MJNotiSkillDatabaseProvider entire() {
     return new MJNotiSkillEntireProvider();
   }

   abstract Map<Integer, MJNotiSkillModel> models();

   private static class MJNotiSkillEntireProvider
     extends MJNotiSkillDatabaseProvider {
     Map<Integer, MJNotiSkillModel> models() {
       final Map<Integer, MJNotiSkillModel> models = new HashMap<>();
       Selector.exec("select * from skills_noti", (SelectorHandler)new FullSelectorHandler()
           {
             public void result(ResultSet rs) throws Exception {
               while (rs.next()) {
                 MJNotiSkillModel model = MJNotiSkillModel.newModel(rs);
                 models.put(Integer.valueOf(model.skillId()), model);
               }
             }
           });
       return models;
     }

     private MJNotiSkillEntireProvider() {}
   }
 }


