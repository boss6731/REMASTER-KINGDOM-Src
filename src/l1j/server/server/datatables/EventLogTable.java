 package l1j.server.server.datatables;

 import java.sql.PreparedStatement;
 import java.sql.Timestamp;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.server.model.Instance.L1NpcInstance;

 public class EventLogTable
 {
   private static final EventLogTable table = new EventLogTable();

   public static EventLogTable table() {
     return table;
   }





   public void newBossSpawn(final L1NpcInstance npc, final long beginMillis, final long endMillis) {
     Updator.exec("insert into event_boss_log set notification_id=?, npc_id=?, npc_name=?, spawn_begin=?, spawn_end=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setInt(++idx, npc.get_boss_type());
             pstm.setInt(++idx, npc.getNpcId());
             pstm.setString(++idx, npc.getName());
             pstm.setTimestamp(++idx, new Timestamp(beginMillis));
             pstm.setTimestamp(++idx, new Timestamp(endMillis));
           }
         });
   }
 }


