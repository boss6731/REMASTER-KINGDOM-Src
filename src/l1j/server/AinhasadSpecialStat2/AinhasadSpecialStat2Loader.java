 package l1j.server.AinhasadSpecialStat2;

 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class AinhasadSpecialStat2Loader
 {
   private static AinhasadSpecialStat2Loader _instance;
   private HashMap<Integer, AinhasadSpecialStat2Info> _special_stat2;
   private HashMap<L1PcInstance, AinhasadSpecialStat2Info> _special_stat2_user_list;

   public static AinhasadSpecialStat2Loader getInstance() {
     if (_instance == null)
       _instance = new AinhasadSpecialStat2Loader();
     return _instance;
   }
   public static void reload() {
     if (_instance != null) {
       _instance = new AinhasadSpecialStat2Loader();
     }
   }




   private AinhasadSpecialStat2Loader() {
     load();
   }

   private void load() {
     final HashMap<Integer, AinhasadSpecialStat2Info> bonus = new HashMap<>();
     Selector.exec("select * from einpoint_effect_faith", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               AinhasadSpecialStat2Info pInfo = AinhasadSpecialStat2Info.newInstance(rs);
               if (pInfo == null)
                 continue;
               bonus.put(Integer.valueOf(pInfo.get_index()), pInfo);
             }
           }
         });
     this._special_stat2 = bonus;
   }

   public AinhasadSpecialStat2Info getSpecialStat(int index) {
     if (this._special_stat2.containsKey(Integer.valueOf(index))) {
       return this._special_stat2.get(Integer.valueOf(index));
     }
     return null;
   }



   public void deleteSpecialStat2(final int charId, String charname, final int index) {
     Updator.exec("DELETE from character_special_stat2 WHERE obj_id=? and index_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setInt(++idx, charId);
             pstm.setInt(++idx, index);
           }
         });
   }
 }


