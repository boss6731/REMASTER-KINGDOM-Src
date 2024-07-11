 package l1j.server.server.datatables;

 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_DELAY_NOTI;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class CharacterSkillDelayTable {
   private static CharacterSkillDelayTable _instance;

   public static CharacterSkillDelayTable getInstace() {
     if (_instance == null) {
       _instance = new CharacterSkillDelayTable();
     }
     return _instance;
   }

   private HashMap<Integer, HashMap<Integer, Long>> _list = new HashMap<>();


   private CharacterSkillDelayTable() {
     Selector.exec("SELECT * FROM character_skills_delay", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               int id = rs.getInt("char_id");
               HashMap<Integer, Long> delays = (HashMap<Integer, Long>)CharacterSkillDelayTable.this._list.get(Integer.valueOf(id));
               if (delays == null) {
                 delays = new HashMap<>();
                 CharacterSkillDelayTable.this._list.put(Integer.valueOf(id), delays);
               }
               delays.put(Integer.valueOf(rs.getInt("skill_id")), Long.valueOf(rs.getLong("delay")));
             }
           }
         });
   }

   public boolean checkDelay(L1PcInstance pc, int skillId) {
     HashMap<Integer, Long> delays = this._list.get(Integer.valueOf(pc.getId()));
     if (delays != null && delays.get(Integer.valueOf(skillId)) != null) {
       long global_delay = SpriteInformationLoader.getInstance().getUseSpellGlobalInterval(pc, skillId);
       int group_id = SpriteInformationLoader.getInstance().getUseSpellGroupId(skillId);
       long currentMillis = System.currentTimeMillis();
       long curDelay = ((Long)delays.get(Integer.valueOf(skillId))).longValue();
       if (currentMillis < curDelay) {
         long delay = curDelay - currentMillis;


         SC_SPELL_DELAY_NOTI.UseSkillDelay(pc, (int)delay, (int)global_delay, group_id);

         return false;
       }
     }










     return true;
   }

   public void update(L1PcInstance pc, int skillId, long curDelay) {
     if (SpriteInformationLoader.getInstance().isUseSpellDelayRecord(skillId)) {
       HashMap<Integer, Long> delays = this._list.get(Integer.valueOf(pc.getId()));
       if (delays == null) {
         delays = new HashMap<>();
         this._list.put(Integer.valueOf(pc.getId()), delays);
       }
       delays.put(Integer.valueOf(skillId), Long.valueOf(curDelay));
     }
   }

   public void updatedata(L1PcInstance pc) {
     HashMap<Integer, Long> delays = this._list.get(Integer.valueOf(pc.getId()));
     if (delays == null) {
       return;
     }
     if (delays.size() == 0) {
       return;
     }
     delays.forEach((k, v) -> updatedb(pc, k.intValue(), v.longValue()));
   }

   public void updatedb(final L1PcInstance pc, final int skillId, final long curDelay) {
     Updator.exec("INSERT INTO character_skills_delay SET char_id=?, skill_id=?, delay=? ON DUPLICATE KEY UPDATE skill_id=?, delay=?", new Handler()
         {

           public void handle(PreparedStatement pstm) throws Exception
           {
             int idx = 0;
             pstm.setInt(++idx, pc.getId());
             pstm.setInt(++idx, skillId);
             pstm.setLong(++idx, curDelay);
             pstm.setInt(++idx, skillId);
             pstm.setLong(++idx, curDelay);
           }
         });
   }
 }


