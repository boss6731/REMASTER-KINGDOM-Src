     package MJShiftObject.Object.Converter;

     import MJShiftObject.DB.Helper.MJShiftSelector;
     import MJShiftObject.DB.Helper.MJShiftUpdator;
     import MJShiftObject.MJShiftObjectHelper;
     import MJShiftObject.Object.Converter.Selector.MJShiftObjectSelector;
     import MJShiftObject.Object.Converter.Updator.MJShiftObjectUpdator;
     import MJShiftObject.Object.MJShiftObject;
     import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
     import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
     import l1j.server.server.Account;
     import l1j.server.server.model.Instance.L1PcInstance;

     public class MJShiftObjectDBConverterFactory
     {
       public static IMJShiftObjectDBConverter create_sender(String server_identity) {
         return new MJShiftObjectDBSender(server_identity);
       }

       public static IMJShiftObjectDBConverter create_receiver(String server_identity) {
         return new MJShiftObjectDBReceiver(server_identity);
       }

       private static class MJShiftObjectDBSender
         implements IMJShiftObjectDBConverter {
         private MJShiftObjectUpdator m_updator;
         private MJShiftObjectSelector m_selector;

         MJShiftObjectDBSender(String server_identity) {
           this.m_updator = MJShiftObjectUpdator.newInstance(server_identity, (Updator)new MJShiftUpdator());


           this.m_selector = MJShiftObjectSelector.newInstance("", new Selector());
         }


         public int work(MJShiftObject sobject) {
           try {
             int object_id = sobject.get_source_id();
             int update_id = sobject.get_source_id();
             L1PcInstance pc = this.m_selector.select_character_info(object_id);
             if (pc == null) {
               return 2;
             }
             Account account = this.m_selector.select_accounts(pc.getAccountName());
             if (account == null) {
               return 4;
             }
             this.m_updator.update_character_info(pc, update_id);
             this.m_updator.update_accounts(account); this.m_updator.update_character_buff(this.m_selector.select_character_buff(object_id), update_id);
             this.m_updator.update_character_config(this.m_selector.select_character_config(object_id), update_id);
             this.m_updator.update_character_items(this.m_selector.select_character_items(object_id), update_id);






             this.m_updator.update_character_skills(this.m_selector.select_character_skills(object_id), update_id);
             this.m_updator.update_character_passive(this.m_selector.select_character_passive(object_id), update_id);
             this.m_updator.update_character_quest_info(this.m_selector.select_character_quest_info(object_id), update_id);
             this.m_updator.update_character_level_bonus(this.m_selector.select_character_level_bonus(object_id).intValue(), update_id);
             MJShiftObjectHelper.update_shift_object(sobject, this.m_updator.get_server_identity());
             return 1;
           } catch (Exception e) {
             e.printStackTrace();

             return 8;
           }
         }

         public int delete(MJShiftObject sobject) {
           try {
             int object_id = sobject.get_source_id();
             this.m_updator.delete_character_info(object_id);
             this.m_updator.delete_accounts(sobject.get_source_account_name());
             this.m_updator.delete_character_buff(object_id);
             this.m_updator.delete_character_config(object_id);
             this.m_updator.delete_character_items(object_id);
             this.m_updator.delete_character_skills(object_id);
             this.m_updator.delete_character_passive(object_id);
             this.m_updator.delete_character_quest_info(object_id);
             this.m_updator.delete_character_level_bonus(object_id);
             MJShiftObjectHelper.delete_shift_object(sobject, this.m_updator.get_server_identity());
           } catch (Exception e) {
             e.printStackTrace();
           }
           return 8;
         }
       }

       private static class MJShiftObjectDBReceiver
         implements IMJShiftObjectDBConverter {
         private MJShiftObjectUpdator m_updator;
         private MJShiftObjectSelector m_selector;

         public MJShiftObjectDBReceiver(String server_identity) {
           this.m_updator = MJShiftObjectUpdator.newInstance("", new Updator());


           this.m_selector = MJShiftObjectSelector.newInstance(server_identity, (Selector)new MJShiftSelector());
         }

         public int work(MJShiftObject sobject) {
           try {
             int object_id = sobject.get_source_id();
             int update_id = sobject.get_destination_id();
             L1PcInstance pc = this.m_selector.select_character_info(object_id);
             if (pc == null) {
               return 2;
             }
             Account account = this.m_selector.select_accounts(pc.getAccountName());
             if (account == null) {
               return 4;
             }

             pc.setId(update_id);
             pc.setName(sobject.get_destination_character_name());
             pc.setAccountName(sobject.get_destination_account_name());
             account.setName(sobject.get_destination_account_name());

             this.m_updator.delete_character_info(sobject.get_destination_character_name());
             this.m_updator.update_character_info(pc, update_id);
             this.m_updator.delete_accounts(sobject.get_destination_account_name());
             this.m_updator.update_accounts(account);
             this.m_updator.update_character_buff(this.m_selector.select_character_buff(object_id), update_id);
             this.m_updator.update_character_config(this.m_selector.select_character_config(object_id), update_id);
             this.m_updator.update_character_items(this.m_selector.select_character_items(object_id), update_id);
             this.m_updator.update_character_skills(this.m_selector.select_character_skills(object_id), update_id);
             this.m_updator.update_character_passive(this.m_selector.select_character_passive(object_id), update_id);
             this.m_updator.update_character_quest_info(this.m_selector.select_character_quest_info(object_id), update_id);
             this.m_updator.update_character_level_bonus(this.m_selector.select_character_level_bonus(object_id).intValue(), update_id);
             MJShiftObjectHelper.update_shift_object(sobject, this.m_selector.get_server_identity());
             return 1;
           } catch (Exception e) {
             e.printStackTrace();

             return 8;
           }
         }


         public int delete(MJShiftObject sobject) {
           return 0;
         }
       }
     }


