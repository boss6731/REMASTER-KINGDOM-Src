 package l1j.server.server.datatables;

 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

 public class AinhasadBonusMonsterTable
 {
   private static AinhasadBonusMonsterTable _instance;

   public static AinhasadBonusMonsterTable getInstance() {
     if (_instance == null) {
       _instance = new AinhasadBonusMonsterTable();
     }
     return _instance;
   }

   private HashMap<Integer, Integer> _ainhasadBonusList = new HashMap<>();
   private HashMap<Integer, Integer> _alarmBonusList = new HashMap<>();

   public void reLoad() {
     AinhasadBonusMonsterTable old = _instance;
     _instance = new AinhasadBonusMonsterTable();
     old._ainhasadBonusList.clear();
     old._alarmBonusList.clear();
     old = null;
   }

   private AinhasadBonusMonsterTable() {
     Selector.exec("SELECT * FROM einhasad_monster", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               AinhasadBonusMonsterTable.this._ainhasadBonusList.put(Integer.valueOf(rs.getInt("monster_id")), Integer.valueOf(rs.getInt("einhasad")));
               AinhasadBonusMonsterTable.this._alarmBonusList.put(Integer.valueOf(rs.getInt("alarm_monster_id")), Integer.valueOf(rs.getInt("einhasad")));
             }
           }
         });
   }

   public Integer getAinhasadBonus(int npcId) {
     return this._ainhasadBonusList.get(Integer.valueOf(npcId));
   }

   public Integer getAlarmAinhasadBonus(int npcId) {
     return this._alarmBonusList.get(Integer.valueOf(npcId));
   }
 }


