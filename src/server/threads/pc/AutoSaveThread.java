     package server.threads.pc;

     import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
     import MJShiftObject.MJShiftObjectHelper;
     import MJShiftObject.MJShiftObjectManager;
     import MJShiftObject.Object.MJShiftObject;
     import java.sql.PreparedStatement;
     import java.util.ArrayList;
     import l1j.server.Config;
     import l1j.server.FatigueProperty;
     import l1j.server.MJRankSystem.Business.MJRankBusiness;
     import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
     import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
     import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
     import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
     import l1j.server.MJTemplate.MJString;
     import l1j.server.server.Account;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1World;

     public class AutoSaveThread
       implements Runnable
     {
       private static AutoSaveThread _instance;
       private final int _saveCharTime;
       private final int _saveInvenTime;
       private final int _saveExpTime;
       private final long _saveFatigueime;

       public static AutoSaveThread getInstance() {
         if (_instance == null) {
           _instance = new AutoSaveThread();
         }
         return _instance;
       }

       public AutoSaveThread() {
         this._saveCharTime = Config.Synchronization.AutosaveInterval;
         this._saveInvenTime = Config.Synchronization.AutosaveIntervalOfInventory;
         this._saveExpTime = Config.Synchronization.AutosaveExpInterval;
         this._saveFatigueime = FatigueProperty.getInstance().get_fatigue_point_stackable_millis();
       }


       public void run() {
         while (true) {
           try {
             final ArrayList<ExpCache> cache_datas = new ArrayList<>(1024);
             for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
               if (pc == null || pc.getNetConnection() == null) {
                 continue;
               }
               Account account = pc.getAccount();
               if (this._saveFatigueime < System.currentTimeMillis() - pc.getlastSavedTime_Fatigue()) {
                 if (account.get_fatigue_point() > 0 &&
                   pc.getSafetyZone() &&
                   this._saveFatigueime < System.currentTimeMillis() - pc.getlastSavedTime_Fatigue()) {
                   account.add_fatigue_point(-1);
                 }


                 account.update_fatigue_info();
                 pc.setlastSavedTime_Fatigue(System.currentTimeMillis());
               }


               if ((this._saveCharTime * 1000) < System.currentTimeMillis() - pc.getlastSavedTime()) {
                 pc.save();
                 pc.setlastSavedTime(System.currentTimeMillis());
               }


               if ((this._saveInvenTime * 1000) < System.currentTimeMillis() - pc.getlastSavedTime_inventory()) {
                 pc.saveInventory();
                 pc.setlastSavedTime_inventory(System.currentTimeMillis());
               }


               if ((this._saveExpTime * 1000) < System.currentTimeMillis() - pc.getlastSavedTime_exp()) {
                 ExpCache cache = new ExpCache(pc.getId(), pc.getName(), pc.get_exp(), pc.getLevel());
                 MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
                 if (cInfo != null) {
                   String homeserver_identity = pc.getNetConnection().get_server_identity();
                   if (!MJString.isNullOrEmpty(homeserver_identity)) {
                     MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_object(pc);
                     if (sobject != null) {
                       MJShiftObjectHelper.update_cache(sobject.get_source_id(), cache, homeserver_identity);

                       continue;
                     }
                   }
                 }
                 if (!MJRankBusiness.getInstance().is_update()) {
                   SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().get(pc.getId());
                   if (noti != null) {
                     noti.set_exp(cache.exp);
                     noti.set_characterInstance(pc);
                   } else {
                     MJRankUserLoader.getInstance().onUser(pc);
                   }
                 }
                 cache_datas.add(cache);

                 if (cache_datas.size() > 0) {
                   Updator.batch("insert into character_exp_cache set object_id=?, character_name=?, exp=?, lvl=? on duplicate key update character_name=?, exp=?, lvl=?", new BatchHandler()
                       {
                         public void handle(PreparedStatement pstm, int callNumber) throws Exception {
                           int idx = 0;
                           AutoSaveThread.ExpCache cache = cache_datas.get(callNumber);
                           pstm.setInt(++idx, cache.object_id);
                           pstm.setString(++idx, cache.character_name);
                           pstm.setLong(++idx, cache.exp);
                           pstm.setInt(++idx, cache.lvl);
                           pstm.setString(++idx, cache.character_name);
                           pstm.setLong(++idx, cache.exp);
                           pstm.setInt(++idx, cache.lvl);
                         }
                       }cache_datas.size());
                 }
                 pc.setlastSavedTime_exp(System.currentTimeMillis());
               }

             }
           } catch (Exception e) {
             e.printStackTrace();
           } finally {
             try {
               Thread.sleep(1000L);
             } catch (InterruptedException e) {

               e.printStackTrace();
             }
           }
         }
       }

       public static class ExpCache {
         public int object_id;
         public String character_name;
         public long exp;
         public int lvl;

         public ExpCache(int object_id, String character_name, long exp, int lvl) {
           this.object_id = object_id;
           this.character_name = character_name;
           this.exp = exp;
           this.lvl = lvl;
         }
       }
     }


