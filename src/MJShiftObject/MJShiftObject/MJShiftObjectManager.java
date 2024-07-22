package MJShiftObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import MJShiftObject.Battle.MJIShiftBattleNotify;
import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import MJShiftObject.Battle.MJShiftBattleItemWhiteList;
import MJShiftObject.Battle.MJShiftBattleManager;
import MJShiftObject.Battle.MJShiftBattlePlayManager;
import MJShiftObject.DB.MJShiftDBArgs;
import MJShiftObject.DB.MJShiftDBFactory;
import MJShiftObject.DB.Helper.MJShiftSelector;
import MJShiftObject.Object.MJShiftObject;
import MJShiftObject.Object.MJShiftObjectOneTimeToken;
import MJShiftObject.Object.Converter.MJShiftObjectReceiver;
import MJShiftObject.Object.Converter.MJShiftObjectSender;
import MJShiftObject.Template.CommonServerBattleInfo;
import MJShiftObject.Template.CommonServerInfo;
import l1j.server.MJTemplate.Chain.Chat.MJIWhisperChatFilterHandler;
import l1j.server.MJTemplate.Chain.Chat.MJIWorldChatFilterHandler;
import l1j.server.MJTemplate.Chain.Chat.MJWhisperChatFilterChain;
import l1j.server.MJTemplate.Chain.Chat.MJWorldChatFilterChain;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.Result;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ARENA_PLAY_EVENT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_HIBREED_AUTH_ACK_PACKET;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.PacketHelper.MJPacketFactory;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import server.threads.pc.AutoSaveThread.ExpCache;

public class MJShiftObjectManager implements MJIShiftBattleNotify, MJIWhisperChatFilterHandler, MJIWorldChatFilterHandler {

	// 單例模式的實例
	private static MJShiftObjectManager _instance;

	/**
	 * 獲取單例實例
	 * @return 單例實例
	 */
	public static MJShiftObjectManager getInstance() {
		if (_instance == null) {
			_instance = new MJShiftObjectManager();
		}
		return _instance;
	}

	// 資料庫參數
	private MJShiftDBArgs m_db_args;

	// 資料庫工廠
	private MJShiftDBFactory m_db_factory;

	// 接收者映射表
	private HashMap<String, MJShiftObjectReceiver> m_receivers;

	// 發送者
	private MJShiftObjectSender m_sender;

	// 戰鬥管理器
	private MJShiftBattleManager m_battle_manager;

	// 當前類型
	private int m_current_kind;

	/**
	 * 私有構造函數，防止外部實例化
	 */
	private MJShiftObjectManager() {
		try {
			m_db_args = new MJShiftDBArgs("./config/mj_shiftserver.properties");
			m_db_factory = new MJShiftDBFactory(m_db_args);
			m_current_kind = 3;
			MJWhisperChatFilterChain.getInstance().add_handler(this);
			MJWorldChatFilterChain.getInstance().add_handler(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 處理發送失敗的邏輯
	 * @param clnt 遊戲客戶端
	 */
	public static void do_fail_send(final GameClient clnt) {
		SC_HIBREED_AUTH_ACK_PACKET pck = SC_HIBREED_AUTH_ACK_PACKET.newInstance();
		pck.set_result(Result.Result_wrong_clientip);
		clnt.sendPacket(pck, MJEProtoMessages.SC_HIBREED_AUTH_ACK_PACKET.toInt(), true);
	}

	/**
	 * 重新加載配置
	 */
	public void reload_config() {
		m_db_args = new MJShiftDBArgs("./config/mj_shiftserver.properties");
	}

	/**
	 * 加載通用伺服器信息
	 * @return 當前實例
	 * @throws Exception 如果未找到伺服器信息則拋出異常
	 */
	public MJShiftObjectManager load_common_server_info() throws Exception {
		m_receivers = new HashMap<String, MJShiftObjectReceiver>();
		m_sender = new MJShiftObjectSender(m_db_args.SERVER_IDENTITY);

		MJShiftSelector.exec("select * from common_shift_server_info", new FullSelectorHandler() {
			@override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					MJShiftObjectReceiver receiver = MJShiftObjectReceiver.newInstance(rs);

					m_receivers.put(receiver.get_server_identity(), receiver);
				}
			}
		});

		MJShiftObjectReceiver receiver = m_receivers.get(m_db_args.SERVER_IDENTITY);
		if (receiver == null)
			throw new Exception("無法在通用資料庫中找到此伺服器的信息。"); // 通用資料庫中找不到此伺服器的信息

		MJShiftObjectHelper.on_shift_server_info(m_db_args.SERVER_IDENTITY);
		return this;
	}

	/**
		釋放伺服器信息
	*/
	public void release() {
		MJShiftObjectHelper.off_shift_server_info(m_db_args.SERVER_IDENTITY);
	}

	/**
		獲取通用伺服器信息
		@param is_exclude_my_server 是否排除當前伺服器
	@return 通用伺服器信息列表
	*/
	public List<CommonServerInfo> get_commons_servers(boolean is_exclude_my_server) {
		return MJShiftObjectHelper.get_commons_servers(m_db_args.SERVER_IDENTITY, is_exclude_my_server);
	}

	/**
		獲取戰鬥伺服器信息
		@return 戰鬥伺服器信息列表
	*/
	public List<CommonServerBattleInfo> get_battle_servers_info() {
		return MJShiftObjectHelper.get_battle_servers_info();
	}

	/**
		獲取接收者團隊ID
		@param server_identity 伺服器標識
		@return 接收者團隊ID，如果找不到則返回-1
	*/
	public int get_receiver_team_id(String server_identity) {
		MJShiftObjectReceiver receiver = m_receivers.get(server_identity);
		if (receiver == null)
			return -1;

		return receiver.get_server_battle_team_id();
	}

	/**
	 處理接收邏輯
	 @param clnt 遊戲客戶端
	 @param reserved_number 保留號碼
	 @param onetimetoken 一次性令牌
	 @throws Exception 當發生錯誤時拋出異常
	 */
	public void do_receive(GameClient clnt, int reserved_number, String onetimetoken) throws Exception {
		final MJShiftObjectOneTimeToken token = MJShiftObjectOneTimeToken.from_onetimetoken(onetimetoken);
		if (token == null) {
			System.out.println(String.format("無法識別的角色信息傳入。(IP: %s) (Token: %s)", clnt.getIp(), onetimetoken));
			do_fail_send(clnt);
			return;
		}

		MJShiftObjectReceiver receiver = m_receivers.get(token.home_server_identity);
		if (receiver == null) {
			System.out.println(String.format("來自未知伺服器的角色信息傳入。(IP: %s) (Token: %s)", clnt.getIp(), onetimetoken));
			do_fail_send(clnt);
			return;
		}

		if (!token.is_returner && token.shift_object.get_shift_type().equals(MJEShiftObjectType.BATTLE)) {
			if (!is_my_battle_server()) {
				System.out.println(String.format("當前沒有創建服務器對抗戰，但收到對抗戰角色信息。(IP: %s) (Token: %s)", clnt.getIp(), onetimetoken));
				do_fail_send(clnt);
				return;
			}
		}

		if (token.is_returner) {
			m_sender.do_getback(clnt, reserved_number, token);
		}
		receiver.do_receive(clnt, reserved_number, token);
	}

	/**
	 發送到戰鬥伺服器
	 @param pc 玩家角色實例
	 @param parameters 參數
	 @throws Exception 當發生錯誤時拋出異常
	 */
	public void do_send_battle_server(L1PcInstance pc, String parameters) throws Exception {
		if (!is_battle_server_running()) {
			pc.sendPackets("當前沒有進行中的對抗戰。");
			return;
		}
		if (m_sender.get_object_count() >= m_db_args.BATTLE_SERVER_SEND_COUNT) {
			pc.sendPackets("參加人數已滿。");
			return;
		}
		if (get_current_kind() == 7) {
			pc.sendPackets(MJPacketFactory.create_duplicate_message_packets("請稍等片刻，將依次進行傳送。"), true);
		}
		try {
			pc.save();
			pc.saveInventory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String server_identity = get_battle_server_identity();
		do_send(pc, MJEShiftObjectType.BATTLE, server_identity, parameters);
	}

	/**
		處理發送邏輯
		@param pc 玩家角色實例
		@param shift_type 轉移類型
		@param server_identity 伺服器標識
		@param parameters 參數
	 	@throws Exception 當發生錯誤時拋出異常
	*/
	public void do_send(L1PcInstance pc, MJEShiftObjectType shift_type, String server_identity, String parameters) throws Exception {
		MJShiftObjectReceiver receiver = m_receivers.get(server_identity);
		m_sender.do_send(pc, shift_type, receiver, parameters, get_current_kind());
	}

	/**
		檢查發送者是否包含特定對象
		@param object_id 對象ID
	    @return 如果包含則返回 true，否則返回 false
	*/
	public boolean is_shift_sender_contains(int object_id) {
		return m_sender.get_object(object_id) != null;
	}

	/**
		獲取發送者對象
		@param object_id 對象ID
		@return 發送者對象
	*/
	public MJShiftObject get_shift_sender_object(int object_id) {
		return m_sender.get_object(object_id);
	}

	/**
		根據帳號獲取發送者對象
		@param account 帳號
		@return 發送者對象
	*/
	public MJShiftObject get_shift_sender_object_from_account(String account) {
		return m_sender.get_object_from_accounts(account);
	}

	/**
		處理返回邏輯
		@param pc 玩家角色實例
	*/

	public void do_returner(L1PcInstance pc) {
		if (get_current_kind() == 7) {
			pc.sendPackets(MJPacketFactory.create_duplicate_message_packets("請稍等片刻，將依次進行傳送。"), true);
		}
		try {
			pc.save();
			pc.saveInventory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		MJShiftObjectReceiver receiver = m_receivers.get(pc.getNetConnection().get_server_identity());
		ExpCache cache = new ExpCache(pc.getId(), pc.getName(), pc.get_exp(), pc.getLevel());
		MJShiftObjectHelper.update_cache(get_source_character_id(pc), cache, pc.getNetConnection().get_server_identity());

		receiver.do_return_database(pc);
		receiver.do_return_player(pc, get_current_kind());
		receiver.remove_object(pc.getId());
	}

	/**
		處理所有返回邏輯
	*/
	public void do_returner() {
		for (MJShiftObjectReceiver receiver : m_receivers.values()) {
			receiver.do_return_database();
		}
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@override
			public void run() {
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
					if (cInfo == null)
						continue;

					SC_ARENA_PLAY_EVENT_NOTI.sendRestartLock(pc);
				}
			}
		});
		for (int i = m_db_args.MY_BATTLE_SERVER_QUIT_READY_SECONDS; i > 0; --i) {
			try {
				String message = String.format("%d秒後對抗戰即將結束。", i);
				S_PacketBox box = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message);
				//                System.out.println(message);
				L1World.getInstance().broadcastPacketToAll(message);
				L1World.getInstance().broadcastPacketToAll(box);
				Thread.sleep(1000L);
				if (i == m_db_args.MY_BATTLE_SERVER_RESERVATION_STORE_READY_SECONDS) {
					m_sender.do_getback();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (MJShiftObjectReceiver receiver : m_receivers.values()) {
			receiver.do_return_players(get_current_kind());
		}
		for (MJShiftObjectReceiver receiver : m_receivers.values()) {
			receiver.clear_objects();
		}
		m_battle_manager = null;
	}

	/**
		處理返回邏輯
	*/
	public void do_getbacker() {
		m_sender.do_getback();
		m_battle_manager = null;
	}

	/**
	 獲取資料庫連接
	 @return 資料庫連接
	 @throws Exception 如果資料庫工廠未初始化，則拋出異常
	 */
	public Connection get_connection() throws Exception {
		if (m_db_factory == null)
			throw new Exception("遠端資料庫未初始化。");

		return m_db_factory.get_connection();
	}

	/**
	獲取本伺服器標識
	@return 本伺服器標識
	*/
	public String get_home_server_identity() {
		return m_db_args.SERVER_IDENTITY;
	}

	/**
	獲取角色轉移物品ID
	@return 角色轉移物品ID
	*/
	public int get_character_transfer_itemid() {
		return m_db_args.CHARACTER_TRANSFER_ITEMID;
	}

	/**
	獲取本伺服器戰鬥準備秒數
	@return 戰鬥準備秒數
	*/
	public int get_my_server_battle_ready_seconds() {
		return m_db_args.MY_BATTLE_SERVER_QUIT_READY_SECONDS;
	}

	/**
	獲取本伺服器戰鬥存儲準備秒數
	@return 戰鬥存儲準備秒數
	*/
	public int get_my_server_battle_store_ready_seconds() {
		return m_db_args.MY_BATTLE_SERVER_RESERVATION_STORE_READY_SECONDS;
	}

	/**
	檢查是否可以進入戰鬥伺服器
	@return 如果可以進入則返回 true，否則返回 false
	*/
	public boolean is_battle_server_enter() {
		return m_battle_manager != null;
	}

	/**
	檢查戰鬥伺服器是否正在運行
	@return 如果正在運行則返回 true，否則返回 false
	*/
	public boolean is_battle_server_running() {
		return m_battle_manager != null && m_battle_manager.is_battle_server_running();
	}

	/**
		檢查戰鬥伺服器是否準備就緒
		@return 如果準備就緒則返回 true，否則返回 false
	*/
	public boolean is_battle_server_ready() {
		return m_battle_manager != null && m_battle_manager.is_battle_server_ready();
	}

	/**
		檢查戰鬥伺服器是否為 Thebes 模式
		@return 如果為 Thebes 模式則返回 true，否則返回 false
	*/
	public boolean is_battle_server_thebes() {
		return m_battle_manager != null && m_battle_manager.is_battle_server_thebes();
	}

	/**
	檢查戰鬥伺服器是否為 Dom Tower 模式
	@return 如果為 Dom Tower 模式則返回 true，否則返回 false
	*/
	public boolean is_battle_server_domtower() {
		return m_battle_manager != null && m_battle_manager.is_battle_server_domtower();
	}

	/**
	檢查戰鬥伺服器是否為 F Island 模式
	@return 如果為 F Island 模式則返回 true，否則返回 false
	*/
	public boolean is_battle_server_fisland() {
		return m_battle_manager != null && m_battle_manager.is_battle_server_fisland();
	}

	/**
	 獲取戰鬥伺服器標識
	 @return 戰鬥伺服器標識
	 */
	public String get_battle_server_identity() {
		return m_battle_manager == null ? "" : m_battle_manager.get_battle_server_identity();
	}

	/**
		重新加載白名單
	*/
	public void do_reload_whitelist() {
		if (m_battle_manager != null)
			m_battle_manager.do_reload_whitelist();
	}

	/**
	使用物品白名單
	@param pc 玩家角色實例
	@param item 物品實例
	@return 如果允許使用則返回 true，否則返回 false
	*/
	public boolean use_item_white_list(L1PcInstance pc, L1ItemInstance item) {
		return m_battle_manager != null ? m_battle_manager.use(pc, item) : true;
	}

	/**
	獲取當前類型
	@return 當前類型
	*/
	public int get_current_kind() {
		return m_current_kind;
	}

	/**
	設置當前類型
	@param kind 當前類型
	*/
	public void set_current_kind(int kind) {
		m_current_kind = kind;
	}

	/**
	進入戰鬥伺服器
	@param bInfo 戰鬥伺服器信息
	@param manager 戰鬥管理器
	@param white_list 白名單
	@param inter_kind 類型
	*/
	public void do_enter_battle_server(CommonServerBattleInfo bInfo, MJShiftBattlePlayManager<?> manager, MJShiftBattleItemWhiteList white_list, int inter_kind) {
		if (m_battle_manager != null)
			return;
		m_current_kind = inter_kind;
		m_battle_manager = new MJShiftBattleManager(bInfo, manager, white_list);
		m_battle_manager.add_notify(this);
		m_battle_manager.execute();
	}

	/**
	進入戰鬥伺服器
	@param bInfo 戰鬥伺服器信息
	@param enter_type 進入類型
	@param inter_kind 類型
	*/
	public void do_enter_battle_server(CommonServerBattleInfo bInfo, int enter_type, int inter_kind) {
		if (m_battle_manager != null)
			return;
		m_current_kind = inter_kind;
		m_battle_manager = new MJShiftBattleManager(bInfo, enter_type);
		m_battle_manager.add_notify(this);
		m_battle_manager.execute();
	}

	/**
	取消戰鬥伺服器
 	*/
	public void do_cancel_battle_server() {
		if (m_battle_manager == null)
			return;

		m_battle_manager.set_cancel_state(true);
		m_battle_manager = null;
	}

	/**
	檢查是否為本戰鬥伺服器
	@return 如果是本戰鬥伺服器則返回 true，否則返回 false
 	*/
	public boolean is_my_battle_server() {
		return m_battle_manager != null && m_battle_manager.get_battle_server_identity().equals(m_db_args.SERVER_IDENTITY);
	}

	/**
	 進入戰鬥伺服器角色
	 @param pc 玩家角色實例
	 */
	public void do_enter_battle_character(L1PcInstance pc) {

		if (m_battle_manager != null)
			m_battle_manager.do_enter_battle_character(pc);
	}

	/**
	獲取源角色名稱
	@param pc 玩家角色實例
	@return 源角色名稱
	*/
	public String get_source_character_name(L1PcInstance pc) {
		MJShiftObjectReceiver receiver = m_receivers.get(pc.getNetConnection().get_server_identity());
		return receiver.get_source_character_name(pc.getId());
	}

	/**
	獲取源角色ID
	@param pc 玩家角色實例
	@return 源角色ID
	*/
	public int get_source_character_id(L1PcInstance pc) {
		MJShiftObjectReceiver receiver = m_receivers.get(pc.getNetConnection().get_server_identity());
		return receiver.get_source_character_id(pc.getId());
	}

	/**
	 獲取轉移對象
	 @param pc 玩家角色實例
	 @return 轉移對象
	 */
	public MJShiftObject get_shift_object(L1PcInstance pc) {
		MJShiftObjectReceiver receiver = m_receivers.get(pc.get

