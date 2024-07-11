     package MJShiftObject;
     import MJShiftObject.Battle.MJIShiftBattleNotify;
     import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
     import MJShiftObject.Battle.MJShiftBattleItemWhiteList;
     import MJShiftObject.Battle.MJShiftBattleManager;
     import MJShiftObject.Battle.MJShiftBattlePlayManager;
     import MJShiftObject.DB.Helper.MJShiftSelector;
     import MJShiftObject.DB.MJShiftDBArgs;
     import MJShiftObject.DB.MJShiftDBFactory;
     import MJShiftObject.Object.Converter.MJShiftObjectReceiver;
     import MJShiftObject.Object.Converter.MJShiftObjectSender;
     import MJShiftObject.Object.MJShiftObject;
     import MJShiftObject.Object.MJShiftObjectOneTimeToken;
     import MJShiftObject.Template.CommonServerBattleInfo;
     import java.sql.Connection;
     import java.sql.ResultSet;
     import java.util.HashMap;
     import java.util.List;
     import l1j.server.MJTemplate.Chain.Chat.MJIWorldChatFilterHandler;
     import l1j.server.MJTemplate.Chain.Chat.MJWorldChatFilterChain;
     import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
     import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ARENA_PLAY_EVENT_NOTI;
     import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_HIBREED_AUTH_ACK_PACKET;
     import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
     import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
     import l1j.server.MJTemplate.PacketHelper.MJPacketFactory;
     import l1j.server.server.GameClient;
     import l1j.server.server.GeneralThreadPool;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1World;
     import l1j.server.server.serverpackets.S_PacketBox;
     import l1j.server.server.serverpackets.ServerBasePacket;
     import server.threads.pc.AutoSaveThread;

     public class MJShiftObjectManager implements MJIShiftBattleNotify, MJIWhisperChatFilterHandler, MJIWorldChatFilterHandler {
       private static MJShiftObjectManager _instance;
       private MJShiftDBArgs m_db_args;
       private MJShiftDBFactory m_db_factory;

       public static void do_fail_send(GameClient clnt) {
         SC_HIBREED_AUTH_ACK_PACKET pck = SC_HIBREED_AUTH_ACK_PACKET.newInstance();
         pck.set_result(Result.Result_wrong_clientip);
         clnt.sendPacket((MJIProtoMessage)pck, MJEProtoMessages.SC_HIBREED_AUTH_ACK_PACKET.toInt(), true);
       }
       private HashMap<String, MJShiftObjectReceiver> m_receivers; private MJShiftObjectSender m_sender; private MJShiftBattleManager m_battle_manager; private int m_current_kind;

       public static MJShiftObjectManager getInstance() {
         if (_instance == null)
           _instance = new MJShiftObjectManager();
         return _instance;
       }







       private MJShiftObjectManager() {
         try {
           this.m_db_args = new MJShiftDBArgs("./config/mj_shiftserver.properties");
           this.m_db_factory = new MJShiftDBFactory(this.m_db_args);
           this.m_current_kind = 3;
           MJWhisperChatFilterChain.getInstance().add_handler(this);
           MJWorldChatFilterChain.getInstance().add_handler(this);
         } catch (Exception e) {
           e.printStackTrace();
         }
       }

       public void reload_config() {
         this.m_db_args = new MJShiftDBArgs("./config/mj_shiftserver.properties");
       }

       public MJShiftObjectManager load_common_server_info() throws Exception {
         this.m_receivers = new HashMap<>();
         this.m_sender = new MJShiftObjectSender(this.m_db_args.SERVER_IDENTITY);
         MJShiftSelector.exec("select * from common_shift_server_info", (SelectorHandler)new FullSelectorHandler()
             {
               public void result(ResultSet rs) throws Exception {
                 while (rs.next()) {
                   MJShiftObjectReceiver receiver = MJShiftObjectReceiver.newInstance(rs);
                   MJShiftObjectManager.this.m_receivers.put(receiver.get_server_identity(), receiver);
                 }
               }
             });
         MJShiftObjectReceiver receiver = this.m_receivers.get(this.m_db_args.SERVER_IDENTITY);
         if (receiver == null) {
           throw new Exception("我在公共資料庫中找不到我的伺服器信息.");
         }
         MJShiftObjectHelper.on_shift_server_info(this.m_db_args.SERVER_IDENTITY);
         return this;
       }

       public void release() {
         MJShiftObjectHelper.off_shift_server_info(this.m_db_args.SERVER_IDENTITY);
       }

       public List<CommonServerInfo> get_commons_servers(boolean is_exclude_my_server) {
         return MJShiftObjectHelper.get_commons_servers(this.m_db_args.SERVER_IDENTITY, is_exclude_my_server);
       }

       public List<CommonServerBattleInfo> get_battle_servers_info() {
         return MJShiftObjectHelper.get_battle_servers_info();
       }
       public int get_receiver_team_id(String server_identity) {
         MJShiftObjectReceiver receiver = this.m_receivers.get(server_identity);
         if (receiver == null) {
           return -1;
         }
         return receiver.get_server_battle_team_id();
       }

       public void do_receive(GameClient clnt, int reserved_number, String onetimetoken) throws Exception {
         MJShiftObjectOneTimeToken token = MJShiftObjectOneTimeToken.from_onetimetoken(onetimetoken);
         if (token == null) {
           System.out.println(String.format("收到無法辨識的字元資訊.(%s)(%s)", new Object[] { clnt.getIp(), onetimetoken }));
           do_fail_send(clnt);
           return;
         }
         MJShiftObjectReceiver receiver = this.m_receivers.get(token.home_server_identity);
         if (receiver == null) {
           System.out.println(String.format("從未知伺服器接收到角色資訊。.(%s)(%s)", new Object[] { clnt.getIp(), onetimetoken }));
           do_fail_send(clnt);
           return;
         }
         if (!token.is_returner && token.shift_object.get_shift_type().equals(MJEShiftObjectType.BATTLE) &&
           !is_my_battle_server()) {
           System.out.println(String.format("目前還未創建伺服器競賽，但角色資訊已轉移至伺服器競賽。(%s)(%s)", new Object[] { clnt.getIp(), onetimetoken }));
           do_fail_send(clnt);

           return;
         }
         if (token.is_returner) {
           this.m_sender.do_getback(clnt, reserved_number, token);
         }
         receiver.do_receive(clnt, reserved_number, token);
       }

       public void do_send_battle_server(L1PcInstance pc, String parameters) throws Exception {
         if (!is_battle_server_running()) {
           pc.sendPackets("目前沒有正在進行的比賽。");
           return;
         }
         if (this.m_sender.get_object_count() >= this.m_db_args.BATTLE_SERVER_SEND_COUNT) {
           pc.sendPackets("參加人數已滿。");
           return;
         }
         if (get_current_kind() == 7) {
           pc.sendPackets(MJPacketFactory.create_duplicate_message_packets("請稍等，它將按順序移動。"), true);
         }
         try {
           pc.save();
           pc.saveInventory();
         } catch (Exception exception) {}
         String server_identity = get_battle_server_identity();
         do_send(pc, MJEShiftObjectType.BATTLE, server_identity, parameters);
       }

       public void do_send(L1PcInstance pc, MJEShiftObjectType shift_type, String server_identity, String parameters) throws Exception {
         MJShiftObjectReceiver receiver = this.m_receivers.get(server_identity);
         this.m_sender.do_send(pc, shift_type, receiver, parameters, get_current_kind());
       }
       public boolean is_shift_sender_contains(int object_id) {
         return (this.m_sender.get_object(object_id) != null);
       }
       public MJShiftObject get_shift_sender_object(int object_id) {
         return this.m_sender.get_object(object_id);
       }
       public MJShiftObject get_shift_sender_object_from_account(String account) {
         return this.m_sender.get_object_from_accounts(account);
       }
       public void do_returner(L1PcInstance pc) {
         if (get_current_kind() == 7) {
           pc.sendPackets(MJPacketFactory.create_duplicate_message_packets("請稍等，它將按順序移動。"), true);
         }
         try {
           pc.save();
           pc.saveInventory();
         } catch (Exception exception) {}
         MJShiftObjectReceiver receiver = this.m_receivers.get(pc.getNetConnection().get_server_identity());
         AutoSaveThread.ExpCache cache = new AutoSaveThread.ExpCache(pc.getId(), pc.getName(), pc.get_exp(), pc.getLevel());
         MJShiftObjectHelper.update_cache(get_source_character_id(pc), cache, pc.getNetConnection().get_server_identity());

         receiver.do_return_database(pc);
         receiver.do_return_player(pc, get_current_kind());
         receiver.remove_object(pc.getId());
       }
       public void do_returner() {
         for (MJShiftObjectReceiver receiver : this.m_receivers.values()) {
           receiver.do_return_database();
         }
         GeneralThreadPool.getInstance().execute(new Runnable()
             {
               public void run() {
                 for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                   MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
                   if (cInfo == null) {
                     continue;
                   }
                   SC_ARENA_PLAY_EVENT_NOTI.sendRestartLock(pc);
                 }
               }
             });
         for (int i = this.m_db_args.MY_BATTLE_SERVER_QUIT_READY_SECONDS; i > 0; i--) {
           try {
             String message = String.format("比賽將在 %d 秒後結束。", new Object[] { Integer.valueOf(i) });
             S_PacketBox box = new S_PacketBox(84, message);

             L1World.getInstance().broadcastPacketToAll(message);
             L1World.getInstance().broadcastPacketToAll((ServerBasePacket)box);
             Thread.sleep(1000L);
             if (i == this.m_db_args.MY_BATTLE_SERVER_RESERVATION_STORE_READY_SECONDS) {
               this.m_sender.do_getback();
             }
           } catch (InterruptedException e) {
             e.printStackTrace();
           }
         }
         for (MJShiftObjectReceiver receiver : this.m_receivers.values()) {
           receiver.do_return_players(get_current_kind());
         }
         for (MJShiftObjectReceiver receiver : this.m_receivers.values()) {
           receiver.clear_objects();
         }
         this.m_battle_manager = null;
       }

       public void do_getbacker() {
         this.m_sender.do_getback();
         this.m_battle_manager = null;
       }

       public Connection get_connection() throws Exception {
         if (this.m_db_factory == null) {
           throw new Exception("not initialized remote database.");
         }
         return this.m_db_factory.get_connection();
       }
       public String get_home_server_identity() {
         return this.m_db_args.SERVER_IDENTITY;
       }
       public int get_character_transfer_itemid() {
         return this.m_db_args.CHARACTER_TRANSFER_ITEMID;
       }
       public int get_my_server_battle_ready_seconds() {
         return this.m_db_args.MY_BATTLE_SERVER_QUIT_READY_SECONDS;
       }
       public int get_my_server_battle_store_ready_seconds() {
         return this.m_db_args.MY_BATTLE_SERVER_RESERVATION_STORE_READY_SECONDS;
       }
       public boolean is_battle_server_enter() {
         return (this.m_battle_manager != null);
       }
       public boolean is_battle_server_running() {
         return (this.m_battle_manager != null && this.m_battle_manager.is_battle_server_running());
       }
       public boolean is_battle_server_ready() {
         return (this.m_battle_manager != null && this.m_battle_manager.is_battle_server_ready());
       }
       public boolean is_battle_server_thebes() {
         return (this.m_battle_manager != null && this.m_battle_manager.is_battle_server_thebes());
       }
       public boolean is_battle_server_domtower() {
         return (this.m_battle_manager != null && this.m_battle_manager.is_battle_server_domtower());
       }
       public boolean is_battle_server_fisland() {
         return (this.m_battle_manager != null && this.m_battle_manager.is_battle_server_fisland());
       }
       public String get_battle_server_identity() {
         return (this.m_battle_manager == null) ? "" : this.m_battle_manager.get_battle_server_identity();
       }
       public void do_reload_whitelist() {
         if (this.m_battle_manager != null)
           this.m_battle_manager.do_reload_whitelist();
       }
       public boolean use_item_white_list(L1PcInstance pc, L1ItemInstance item) {
         return (this.m_battle_manager != null) ? this.m_battle_manager.use(pc, item) : true;
       }
       public int get_current_kind() {
         return this.m_current_kind;
       }
       public void set_current_kind(int kind) {
         this.m_current_kind = kind;
       }

       public void do_enter_battle_server(CommonServerBattleInfo bInfo, MJShiftBattlePlayManager<?> manager, MJShiftBattleItemWhiteList white_list, int inter_kind) {
         if (this.m_battle_manager != null)
           return;
         this.m_current_kind = inter_kind;
         this.m_battle_manager = new MJShiftBattleManager(bInfo, manager, white_list);
         this.m_battle_manager.add_notify(this);
         this.m_battle_manager.execute();
       }

       public void do_enter_battle_server(CommonServerBattleInfo bInfo, int enter_type, int inter_kind) {
         if (this.m_battle_manager != null)
           return;
         this.m_current_kind = inter_kind;
         this.m_battle_manager = new MJShiftBattleManager(bInfo, enter_type);
         this.m_battle_manager.add_notify(this);
         this.m_battle_manager.execute();
       }

       public void do_cancel_battle_server() {
         if (this.m_battle_manager == null) {
           return;
         }
         this.m_battle_manager.set_cancel_state(true);
         this.m_battle_manager = null;
       }
       public boolean is_my_battle_server() {
         return (this.m_battle_manager != null && this.m_battle_manager.get_battle_server_identity().equals(this.m_db_args.SERVER_IDENTITY));
       }

       public void do_enter_battle_character(L1PcInstance pc) {
         if (this.m_battle_manager != null)
           this.m_battle_manager.do_enter_battle_character(pc);
       }

       public String get_source_character_name(L1PcInstance pc) {
         MJShiftObjectReceiver receiver = this.m_receivers.get(pc.getNetConnection().get_server_identity());
         return receiver.get_source_character_name(pc.getId());
       }
       public int get_source_character_id(L1PcInstance pc) {
         MJShiftObjectReceiver receiver = this.m_receivers.get(pc.getNetConnection().get_server_identity());
         return receiver.get_source_character_id(pc.getId());
       }
       public MJShiftObject get_shift_object(L1PcInstance pc) {
         MJShiftObjectReceiver receiver = this.m_receivers.get(pc.getNetConnection().get_server_identity());
         return receiver.get_object(pc.getId());
       }


       public void do_ended(CommonServerBattleInfo bInfo) {
         if (bInfo.get_server_identity().equals(this.m_db_args.SERVER_IDENTITY)) {
           do_returner();
         } else {
           System.out.println(String.format("%s 比賽的參與已結束。", new Object[] { bInfo.get_server_identity() }));
           do_getbacker();
         }
       }


       public boolean is_chat(L1PcInstance from, String to_name, String message) {
         if (from.is_shift_client()) {
           from.sendPackets("悄悄話目前不可用。");
           return true;
         }
         return false;
       }


       public boolean is_chat(L1PcInstance owner, String message) {
         if (owner.is_shift_client() && !owner.is_shift_battle()) {
           owner.sendPackets("目前，完整聊天功能不可用。");
           return true;
         }
         return false;
       }
     }


