     package MJShiftObject;

     import MJShiftObject.DB.Helper.MJShiftSelector;
     import MJShiftObject.DB.Helper.MJShiftUpdator;
     import MJShiftObject.DB.MJEShiftDBName;
     import MJShiftObject.Object.MJShiftObject;
     import MJShiftObject.Template.CommonServerBattleInfo;
     import MJShiftObject.Template.CommonServerInfo;
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import java.sql.Timestamp;
     import java.util.ArrayList;
     import java.util.List;
     import l1j.server.MJTemplate.MJObjectWrapper;
     import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
     import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
     import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
     import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
     import l1j.server.server.Account;
     import l1j.server.server.datatables.ItemTable;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.templates.L1Item;
     import server.threads.pc.AutoSaveThread;

     public class MJShiftObjectHelper
     {
       public static void update_cache(final int object_id, final AutoSaveThread.ExpCache cache, String server_identity) {
         MJShiftUpdator.exec(String.format("insert into %s set object_id=?, character_name=?, exp=?, lvl=? on duplicate key update character_name=?, exp=?, lvl=?", new Object[] { MJEShiftDBName.CHARACTERS_CACHE

                 .get_table_name(server_identity) }), new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception
               {
                 int idx = 0;
                 pstm.setInt(++idx, object_id);
                 pstm.setString(++idx, cache.character_name);
                 pstm.setLong(++idx, cache.exp);
                 pstm.setInt(++idx, cache.lvl);
                 pstm.setString(++idx, cache.character_name);
                 pstm.setLong(++idx, cache.exp);
                 pstm.setInt(++idx, cache.lvl);
               }
             });
       }

       public static void delete_cache(final int object_id, String server_identity) {
         MJShiftUpdator.exec(String.format("delete from %s where object_id=?", new Object[] { MJEShiftDBName.CHARACTERS_CACHE.get_table_name(server_identity) }), new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setInt(1, object_id);
               }
             });
       }

       public static AutoSaveThread.ExpCache select_cache(final int object_id, String server_identity) {
         final MJObjectWrapper<AutoSaveThread.ExpCache> wrapper = new MJObjectWrapper();
         MJShiftSelector.exec(String.format("select * from %s where object_id=?", new Object[] { MJEShiftDBName.CHARACTERS_CACHE.get_table_name(server_identity) }), new SelectorHandler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setInt(1, object_id);
               }


               public void result(ResultSet rs) throws Exception {
                 if (rs.next()) {
                   wrapper.value = new AutoSaveThread.ExpCache(object_id, rs.getString("character_name"), rs.getInt("exp"), rs.getInt("lvl"));
                 }
               }
             });
         return (AutoSaveThread.ExpCache)wrapper.value;
       }

       public static List<L1ItemInstance> select_pickup_items(final int object_id, String server_identity) {
         final MJObjectWrapper<ArrayList<L1ItemInstance>> wrapper = new MJObjectWrapper();
         wrapper.value = new ArrayList();
         MJShiftSelector.exec(String.format("select * from %s where char_id=?", new Object[] { MJEShiftDBName.CHARACTERS_PICKUP.get_table_name(server_identity) }), new SelectorHandler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setInt(1, object_id);
               }


               public void result(ResultSet rs) throws Exception {
                 while (rs.next()) {
                   int item_id = rs.getInt("item_id");
                   L1Item template = ItemTable.getInstance().getTemplate(item_id);
                   if (template == null) {
                     System.out.println(String.format("如果伺服器之間缺少某個項目，則該項目將被取消。 ID：%d", new Object[] { Integer.valueOf(item_id) }));
                     continue;
                   }
                   L1ItemInstance item = new L1ItemInstance();
                   item.setId(rs.getInt("id"));
                   item.setItem(template);
                   item.setCount(rs.getInt("count"));
                   item.setEquipped((rs.getInt("is_equipped") != 0));
                   item.setEnchantLevel(rs.getInt("enchantlvl"));
                   item.setIdentified((rs.getInt("is_id") != 0));
                   item.set_durability(rs.getInt("durability"));
                   item.setChargeCount(rs.getInt("charge_count"));
                   item.setRemainingTime(rs.getInt("remaining_time"));
                   item.setLastUsed(rs.getTimestamp("last_used"));
                   item.setBless(rs.getInt("bless"));
                   item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
                   item.setEndTime(rs.getTimestamp("end_time"));
                   item.setPackage((rs.getInt("package") != 0));
                   item.set_bless_level(rs.getInt("bless_level"));
                   item.set_item_level(rs.getInt("item_level"));
                   item.setHotel_Town(rs.getString("Hotel_Town"));
                   ((ArrayList<L1ItemInstance>)wrapper.value).add(item);
                 }
               }
             });
         return (List<L1ItemInstance>)wrapper.value;
       }

       public static void delete_pickup_items(final int object_id, String server_identity) {
         MJShiftUpdator.exec(String.format("delete from %s where char_id=?", new Object[] { MJEShiftDBName.CHARACTERS_PICKUP.get_table_name(server_identity) }), new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setInt(1, object_id);
               }
             });
       }

       public static void update_pickup_items(final int object_id, final L1ItemInstance item, String server_identity) {
         MJShiftUpdator.exec(String.format("insert into %s set id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, end_time=?, buy_time=?, package=?, bless_level=?, item_level=?, Hotel_Town=?", new Object[] { MJEShiftDBName.CHARACTERS_PICKUP

                 .get_table_name(server_identity) }), new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception
               {
                 int idx = 0;
                 pstm.setInt(++idx, item.getId());
                 pstm.setInt(++idx, item.getItem().getItemId());
                 pstm.setInt(++idx, object_id);
                 pstm.setString(++idx, item.getItem().getName());
                 pstm.setInt(++idx, item.getCount());
                 pstm.setInt(++idx, 0);
                 pstm.setInt(++idx, item.getEnchantLevel());
                 pstm.setInt(++idx, 0);
                 pstm.setInt(++idx, item.get_durability());
                 pstm.setInt(++idx, item.getChargeCount());
                 pstm.setInt(++idx, item.getRemainingTime());
                 pstm.setTimestamp(++idx, item.getLastUsed());
                 pstm.setInt(++idx, item.getBless());
                 pstm.setInt(++idx, item.getAttrEnchantLevel());
                 pstm.setTimestamp(++idx, item.getEndTime());
                 pstm.setTimestamp(++idx, item.getEndTime());
                 pstm.setInt(++idx, !item.isPackage() ? 0 : 1);
                 pstm.setInt(++idx, item.get_bless_level());
                 pstm.setInt(++idx, item.get_item_level());
                 pstm.setString(++idx, item.getHotel_Town());
               }
             });
       }

       public static void delete_battle_server(final String server_identity) {
         MJShiftUpdator.exec("delete from common_server_battle_reservation where server_identity=?", new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setString(1, server_identity);
               }
             });
       }

       public static List<CommonServerBattleInfo> get_battle_servers_info() {
         final MJObjectWrapper<ArrayList<CommonServerBattleInfo>> wrapper = new MJObjectWrapper();
         wrapper.value = new ArrayList();
         MJShiftSelector.exec("select * from common_server_battle_reservation", (SelectorHandler)new FullSelectorHandler()
             {
               public void result(ResultSet rs) throws Exception {
                 while (rs.next()) {
                   CommonServerBattleInfo bInfo = CommonServerBattleInfo.newInstance(rs);
                   ((ArrayList<CommonServerBattleInfo>)wrapper.value).add(bInfo);
                 }
               }
             });
         return (List<CommonServerBattleInfo>)wrapper.value;
       }

       public static CommonServerBattleInfo get_battle_server_info(final String server_identity) {
         final MJObjectWrapper<CommonServerBattleInfo> wrapper = new MJObjectWrapper();
         MJShiftSelector.exec("select * from common_server_battle_reservation where server_identity=?", new SelectorHandler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setString(1, server_identity);
               }


               public void result(ResultSet rs) throws Exception {
                 while (rs.next()) {
                   CommonServerBattleInfo bInfo = CommonServerBattleInfo.newInstance(rs);
                   wrapper.value = bInfo;
                 }
               }
             });
         return (CommonServerBattleInfo)wrapper.value;
       }

       public static void reservation_server_battle(final String server_identity, final long start_millis, final long ended_millis, final int current_kind, final String battle_name) {
         MJShiftUpdator.exec("insert into common_server_battle_reservation set server_identity=?, start_time=?, ended_time=?, current_kind=?, battle_name=? on duplicate key update start_time=?, ended_time=?, current_kind=?, battle_name=?", new Handler()
             {


               public void handle(PreparedStatement pstm) throws Exception
               {
                 int idx = 0;
                 pstm.setString(++idx, server_identity);
                 Timestamp start_ts = new Timestamp(start_millis);
                 Timestamp ended_ts = new Timestamp(ended_millis);
                 pstm.setTimestamp(++idx, start_ts);
                 pstm.setTimestamp(++idx, ended_ts);
                 pstm.setInt(++idx, current_kind);
                 pstm.setString(++idx, battle_name);
                 pstm.setTimestamp(++idx, start_ts);
                 pstm.setTimestamp(++idx, ended_ts);
                 pstm.setInt(++idx, current_kind);
                 pstm.setString(++idx, battle_name);
               }
             });
       }

       public static List<CommonServerInfo> get_commons_servers(final String server_identity, final boolean is_exclude_my_server) {
         final MJObjectWrapper<ArrayList<CommonServerInfo>> wrapper = new MJObjectWrapper();
         wrapper.value = new ArrayList();
         MJShiftSelector.exec("select * from common_shift_server_info", (SelectorHandler)new FullSelectorHandler()
             {
               public void result(ResultSet rs) throws Exception {
                 while (rs.next()) {
                   CommonServerInfo csInfo = CommonServerInfo.newInstance(rs);
                   if (is_exclude_my_server && csInfo.server_identity.equals(server_identity)) {
                     continue;
                   }
                   ((ArrayList<CommonServerInfo>)wrapper.value).add(csInfo);
                 }
               }
             });
         return (List<CommonServerInfo>)wrapper.value;
       }

       public static void on_shift_server_info(String server_identity) {
         update_shift_server_state(server_identity, true);
       }

       public static void off_shift_server_info(String server_identity) {
         update_shift_server_state(server_identity, false);
       }

       public static void update_shift_server_state(final String server_identity, final boolean is_on) {
         MJShiftUpdator.exec("update common_shift_server_info set server_is_on=? where server_identity=?", new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setString(1, String.valueOf(is_on));
                 pstm.setString(2, server_identity);
               }
             });
       }

       public static void delete_shift_object(final MJShiftObject sobj, String server_identity) {
         MJShiftUpdator.exec(String.format("delete from %s where source_id=?", new Object[] { MJEShiftDBName.OBJECT_CONVERTER.get_table_name(server_identity) }), new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception
               {
                 pstm.setInt(1, sobj.get_source_id());
               }
             });
       }
       public static void truncate_shift_datas(String server_identity, boolean is_truncate_pickup, boolean is_truncate_cache) {
         for (MJEShiftDBName dbname : MJEShiftDBName.values()) {
           if (!dbname.equals(MJEShiftDBName.CHARACTERS_SLOT_ITEMS))
           {
             if (!dbname.equals(MJEShiftDBName.CHARACTERS_TAMS))
             {
               if (is_truncate_pickup || !dbname.equals(MJEShiftDBName.CHARACTERS_PICKUP))
               {

                 if (is_truncate_cache || !dbname.equals(MJEShiftDBName.CHARACTERS_CACHE))
                 {

                   MJShiftUpdator.truncate(dbname.get_table_name(server_identity)); }  }  }  }
         }
       }

       public static MJShiftObject select_shift_object(final int source_id, String server_identity) {
         final MJObjectWrapper<MJShiftObject> wrapper = new MJObjectWrapper();
         MJShiftSelector.exec(String.format("select * from %s where source_id=?", new Object[] { MJEShiftDBName.OBJECT_CONVERTER.get_table_name(server_identity) }), new SelectorHandler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setInt(1, source_id);
               }


               public void result(ResultSet rs) throws Exception {
                 if (rs.next())
                   wrapper.value = MJShiftObject.newInstance(rs);
               }
             });
         return (MJShiftObject)wrapper.value;
       }

       public static void update_shift_object(final MJShiftObject sobj, String server_identity) {
         MJShiftUpdator.exec(String.format("insert into %s set source_id=?, destination_id=?, shift_type=?, source_character_name=?, source_account_name=?, destination_character_name=?, destination_account_name=?, convert_parameters=?on duplicate key update destination_id=?, shift_type=?, source_character_name=?, source_account_name=?, destination_character_name=?, destination_account_name=?, convert_parameters=?", new Object[] { MJEShiftDBName.OBJECT_CONVERTER



                 .get_table_name(server_identity) }), new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception
               {
                 int idx = 0;
                 pstm.setInt(++idx, sobj.get_source_id());
                 pstm.setInt(++idx, sobj.get_destination_id());
                 pstm.setString(++idx, sobj.get_shift_type().to_name());
                 pstm.setString(++idx, sobj.get_source_character_name());
                 pstm.setString(++idx, sobj.get_source_account_name());
                 pstm.setString(++idx, sobj.get_destination_character_name());
                 pstm.setString(++idx, sobj.get_destination_account_name());
                 pstm.setString(++idx, sobj.get_convert_parameters());

                 pstm.setInt(++idx, sobj.get_destination_id());
                 pstm.setString(++idx, sobj.get_shift_type().to_name());
                 pstm.setString(++idx, sobj.get_source_character_name());
                 pstm.setString(++idx, sobj.get_source_account_name());
                 pstm.setString(++idx, sobj.get_destination_character_name());
                 pstm.setString(++idx, sobj.get_destination_account_name());
                 pstm.setString(++idx, sobj.get_convert_parameters());
               }
             });
       }

       public static void shuffle_account_password(final String account_name) {
         Updator.exec("update accounts set login=? where login=?", new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setString(1, String.format("[%s]", new Object[] { this.val$account_name }));
                 pstm.setString(2, account_name);
               }
             });
       }

       public static void shuffle_character_name(final int object_id, final String char_name, final String account_name) {
         Updator.exec("update characters set char_name=?, account_name=? where objid=?", new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setString(1, String.format("[%s]", new Object[] { this.val$char_name }));
                 pstm.setString(2, String.format("[%s]", new Object[] { this.val$account_name }));
                 pstm.setInt(3, object_id);
               }
             });
       }

       public static void update_account_name(final Account account, final String new_account_name) {
         Updator.exec("update accounts set login=? where login=?", new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setString(1, new_account_name);
                 pstm.setString(2, account.getName());
                 account.setName(new_account_name);
               }
             });
       }

       public static void update_account_name(final L1PcInstance pc, final String new_account_name) {
         Updator.exec("update characters set account_name=? where objid=?", new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception
               {
                 pstm.setString(1, new_account_name);
                 pstm.setInt(2, pc.getId());
                 pc.setAccountName(new_account_name);
               }
             });
       }
     }


