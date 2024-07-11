 package l1j.server.server.model.item.itemdelay;

 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.LinkedList;
 import java.util.List;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;

 public abstract class ItemDelayTimer
   implements Runnable {
   public static ItemDelayTimer newTimer(L1Character cha, int item_id, String item_name, long delay_time) {
     long current_time = System.currentTimeMillis();
     Timestamp total_time = new Timestamp(delay_time + current_time);
     return new ItemDelayTimerController(cha, item_id, item_name, delay_time * 10L, total_time);
   }


   public abstract L1Character owner();


   public abstract int ItemId();


   public abstract String ItemName();


   public abstract long timeMillis();

   public abstract boolean stopped();

   public void run() {
     if (stopped()) {
       return;
     }

     int itemId = ItemId();
     L1Character character = owner();
     int remainingSeconds = remainingSeconds();

     remainingSeconds(remainingSeconds - 1);

     if (remainingSeconds() <= 0) {
       character.removeItemDelayTime(itemId);

       return;
     }
     GeneralThreadPool.getInstance().schedule(this, 100L);
   }
   public abstract int remainingSeconds();
   public abstract void remainingSeconds(int paramInt);
   public abstract Timestamp LogDelayTime();
   public abstract void begin();
   public abstract void end();
   public abstract void kill();
   static class DelayInfo {
     public int ItemId; public String ItemName; public int remainTime; public Timestamp skilltime; }
   public static void loadItemDelay(final L1PcInstance pc) {
     final List<DelayInfo> delayList = new ArrayList<>();

     Selector.exec("select * from character_delayitems where char_id=?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, pc.getId());
           }


           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               try {
                 ItemDelayTimer.DelayInfo Info = new ItemDelayTimer.DelayInfo();
                 Info.ItemId = rs.getInt("item_id");
                 Info.ItemName = rs.getString("item_name");
                 Info.remainTime = rs.getInt("remaining_time");
                 Info.skilltime = rs.getTimestamp("log_delay_time");

                 if (Info != null)
                   delayList.add(Info);
               } catch (Exception e) {
                 e.printStackTrace();
               }
             }
           }
         });

     if (delayList != null) {
       for (DelayInfo Info : delayList) {
         final int itemid = Info.ItemId;
         String itemName = Info.ItemName;
         long log_skill_time = Info.skilltime.getTime() - System.currentTimeMillis();

         Updator.exec("delete from character_delayitems where char_id=? and item_id=?", new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 int idx = 0;
                 pstm.setInt(++idx, pc.getId());
                 pstm.setInt(++idx, itemid);
               }
             });

         pc.addItemDelayTime(itemid, itemName, log_skill_time);
       }
     }
   }

   public static void SaveItemDelay(L1PcInstance pc) {
     if (pc.getAI() != null) {
       return;
     }
     final LinkedList<ItemDelayModel> delayModels = new LinkedList<>();
     for (ItemDelayTimer delay_info : pc.hasItemDelayTimeList()) {
       if (!pc.hasItemDelayTime(delay_info.ItemId()))
         continue;
       int timeSec = pc.getItemDelayTimeSec(delay_info.ItemId());
       Timestamp skilltime = pc.getItemDelayLogTime(delay_info.ItemId());
       delayModels.add(new ItemDelayModel(delay_info.ItemId(), delay_info.ItemName(), timeSec, skilltime));
     }
     int size = delayModels.size();
     if (size <= 0) {
       return;
     }

     final int characterId = pc.getId();
     Updator.batch("insert into character_delayitems SET char_id=?, item_id=?, item_name=?, remaining_time=?, log_delay_time=?", new BatchHandler()
         {
           public void handle(PreparedStatement pstm, int callNumber) throws Exception {
             ItemDelayTimer.ItemDelayModel model = delayModels.get(callNumber);
             int idx = 0;
             pstm.setInt(++idx, characterId);
             pstm.setInt(++idx, model.itemId);
             pstm.setString(++idx, model.ItemName);
             pstm.setInt(++idx, model.timeSec);
             pstm.setTimestamp(++idx, model.delaytime);
           }
         }size);
   }

   private static class ItemDelayModel { int itemId;
     String ItemName;
     int timeSec;
     Timestamp delaytime;

     ItemDelayModel(int itemId, String itemName, int timeSec, Timestamp delaytime) {
       this.itemId = itemId;
       this.ItemName = itemName;
       this.timeSec = timeSec;
       this.delaytime = delaytime;
     } }

 }


