package MJShiftObject.Object.Converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class MJShiftObjectReceiver {
	public static int TEST_INDEX = 0;

	private String m_server_description;
	private String m_server_identity;
	private String m_server_address;
	private int m_server_address_bigendian;
	private int m_server_port;
	private int m_server_battle_team_id;
	private ConcurrentHashMap<Integer, MJShiftObject> m_objects;
	private IMJShiftObjectDBConverter m_receiver;
	private MJShiftObjectSelector m_local_selector;

	/**
	 * 私有構造函數，用於初始化 MJShiftObjectReceiver 實例。
	 *
	 * @param server_description 伺服器描述
	 * @param server_identity 伺服器身份
	 * @param server_address 伺服器地址
	 * @param server_port 伺服器端口
	 * @param server_battle_team_id 伺服器戰鬥隊伍 ID
	 */
	private MJShiftObjectReceiver(String server_description, String server_identity, String server_address,
								  int server_port, int server_battle_team_id) {
		this.m_server_description = server_description;
		this.m_server_identity = server_identity;
		this.m_server_address = server_address;
		this.m_server_port = server_port;
		this.m_server_battle_team_id = server_battle_team_id;

		StringTokenizer tok = new StringTokenizer(this.m_server_address, ".");
		this.m_server_address_bigendian = 0;
		for (int i = 3; i >= 0; i--) {
			int bit = i * 8;
			this.m_server_address_bigendian |= (Integer.parseInt(tok.nextToken()) << bit) & (255 << bit);
		}

		this.m_objects = new ConcurrentHashMap<>();
		this.m_receiver = MJShiftObjectDBConverterFactory.create_receiver(this.m_server_identity);
		this.m_local_selector = MJShiftObjectSelector.newInstance("", new Selector());
	}

	/**
	 * 使用 ResultSet 創建一個新的 MJShiftObjectReceiver 實例。
	 *
	 [09:40]
	 @param rs ResultSet 對象
	 @return 初始化好的 MJShiftObjectReceiver 實例
	 @throws SQLException 當 SQL 操作失敗時拋出
	 */
	public static MJShiftObjectReceiver newInstance(ResultSet rs) throws SQLException {
		return newInstance(rs.getString("server_description"), rs.getString("server_identity"),
				rs.getString("server_address"), rs.getInt("server_port"), rs.getInt("server_battle_team_id"));}

	/**

	創建一個新的 MJShiftObjectReceiver 實例。*
	@param server_description 伺服器描述
	@param server_identity 伺服器身份
	@param server_address 伺服器地址
	@param server_port 伺服器端口
	@param server_battle_team_id 伺服器戰鬥隊伍 ID
	@return 初始化好的 MJShiftObjectReceiver 實例
	*/
	public static MJShiftObjectReceiver newInstance(String server_description, String server_identity,
													String server_address, int server_port, int server_battle_team_id) {
		return new MJShiftObjectReceiver(server_description, server_identity, server_address, server_port,
				server_battle_team_id);}

    /**

	獲取伺服器描述。*
			@return 伺服器描述
	 */
	public String get_server_description() {
		return this.m_server_description;}

    /**

	獲取伺服器身份。*
			@return 伺服器身份*/
	public String get_server_identity() {
		return this.m_server_identity;}

    /**

	獲取伺服器地址。*
			@return 伺服器地址*/
	public String get_server_address() {
		return this.m_server_address;}

    /**

	獲取伺服器地址的大端格式。*
			@return 伺服器地址的大端格式*/
	public int get_server_address_bigendian() {
		return this.m_server_address_bigendian;}

    /**

	獲取伺服器端口。*
			@return 伺服器端口*/
	public int get_server_port() {
		return this.m_server_port;}

    /**

	獲取伺服器戰鬥隊伍 ID。*
			@return 伺服器戰鬥隊伍 ID*/
	public int get_server_battle_team_id() {
		return this.m_server_battle_team_id;}

    /**
	獲取特定 ID 的 MJShiftObject 對象。*
	@param object_id 對象 ID
	@return 對應的 MJShiftObject 對象
*/
	public MJShiftObject get_object(int object_id) {
		return this.m_objects.get(object_id);}

    /**

	移除特定 ID 的 MJShiftObject 對象。*
	@param object_id 對象 ID*/
	public void remove_object(int object_id) {
		this.m_objects.remove(object_id);}

    /**

	將所有角色信息返回到資料庫。*/
	public void do_return_database() {
		MJShiftObjectUpdator local_updator = MJShiftObjectUpdator.newInstance("", new Updator());
		for (MJShiftObject sobject : this.m_objects.values()) {
			int destination_id = sobject.get_destination_id();
			local_updator.delete_character_buff(destination_id);
			local_updator.delete_character_items(destination_id);
			local_updator.delete_accounts(sobject.get_destination_account_name());
			local_updator.delete_character_info(destination_id);
			local_updator.delete_character_level_bonus(destination_id);
			local_updator.delete_character_passive(destination_id);
			local_updator.delete_character_quest_info(destination_id);
			local_updator.delete_character_skills(destination_id);
			local_updator.delete_character_slot_items(destination_id);
			local_updator.delete_character_tams(destination_id);
			local_updator.delete_character_config(destination_id);}}

	/**

	 返回玩家到伺服器。*
	 @param pc   L1PcInstance 對象
	 @param kind 傳送類型
	 */
	public void do_return_player(L1PcInstance pc, int kind) {
		MJShiftObject sobject = get_object(pc.getId());
		try {
			pc.getNetConnection().set_shift_type(MJEShiftObjectType.NONE);
			SC_CONNECT_HIBREEDSERVER_NOTI_PACKET pck = SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.newInstance();
			pck.set_destIP(this.m_server_address_bigendian);
			pck.set_destPort(this.m_server_port);
			pck.set_domainname(this.m_server_address);
			pck.set_interkind(99); // 可根據需求調整
			pck.set_onetimetoken(
					(new MJShiftObjectOneTimeToken(this.m_server_identity, true, sobject, this.m_server_identity, false))
							.to_onetime_token().getBytes());
			pck.set_reservednumber(sobject.get_source_id());
			pc.sendPackets((MJIProtoMessage) pck, MJEProtoMessages.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
	返回所有玩家到伺服器。*
	@param kind 傳送類型*/
	public void do_return_players(int kind) {
		for (MJShiftObject sobject : this.m_objects.values()) {
			L1Object obj = L1World.getInstance().findObject(sobject.get_destination_id());
			if (obj == null) {
				continue;}
			try {
				L1PcInstance pc = (L1PcInstance) obj;
				pc.sendPackets((ServerBasePacket) new S_CharacterConfig(pc.getId()));
				pc.getNetConnection().set_shift_type(MJEShiftObjectType.NONE);
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET pck = SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.newInstance();
				pck.set_destIP(this.m_server_address_bigendian);
				pck.set_destPort(this.m_server_port);
				pck.set_domainname(this.m_server_address);
				pck.set_interkind(99);
				pck.set_onetimetoken((new MJShiftObjectOneTimeToken(this.m_server_identity, true, sobject, this.m_server_identity, false)).to_onetime_token().getBytes());
				pck.set_reservednumber(sobject.get_source_id());
				pc.sendPackets((MJIProtoMessage) pck, MJEProtoMessages.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET, true);} catch (Exception e) {
				e.printStackTrace();}}}

    /**
			[09:40]
	清除所有 MJShiftObject 對象。*/
	public void clear_objects() {
		this.m_objects.clear();}

	/**

	 处理返回玩家到伺服器的方法。*
	 @param clnt             客戶端對象
	 @param source_object_id 來源對象 ID
	 @param token            MJShiftObjectOneTimeToken 對象
	 @throws GameServerFullException    當伺服器滿時拋出
	 @throws AccountAlreadyLoginException 當帳戶已登入時拋出
	 */
	private void do_getback_player(final GameClient clnt, int source_object_id, MJShiftObjectOneTimeToken token)
			throws GameServerFullException, AccountAlreadyLoginException {
		final MJShiftObject sobject = token.shift_object;
		Account account = this.m_local_selector.select_accounts_for_returner(sobject.get_source_account_name());
		account.setValid(true);
		clnt.setAccount(account);
		clnt.set_server_identity("");
		clnt.set_shift_type(MJEShiftObjectType.NONE);
		clnt.set_shift_object(null);
		LoginController.getInstance().login(clnt, account);

		String homeserver_identity = MJShiftObjectManager.getInstance().get_home_server_identity();
		final AutoSaveThread.ExpCache cache = MJShiftObjectHelper.select_cache(source_object_id, homeserver_identity);
		if (cache != null) {
			MJShiftObjectHelper.delete_cache(source_object_id, homeserver_identity);
			Updator.exec("update characters set level=?, exp=? where char_name=?", new Handler() {
				public void handle(PreparedStatement pstm) throws Exception {
					int idx = 0;
					pstm.setInt(++idx, cache.lvl);
					pstm.setLong(++idx, cache.exp);
					pstm.setString(++idx, sobject.get_source_character_name());
				}
			});
		}

		GeneralThreadPool.getInstance().schedule(new Runnable() {
			public void run() {
				try {
					SC_HIBREED_AUTH_ACK_PACKET pck = SC_HIBREED_AUTH_ACK_PACKET.newInstance();
					pck.set_result(Result.Result_sucess);
					clnt.sendPacket((MJIProtoMessage) pck, MJEProtoMessages.SC_HIBREED_AUTH_ACK_PACKET.toInt(), true);
					clnt.setStatus(MJClientStatus.CLNT_STS_AUTHLOGIN);
					C_LoginToServer.do_direct_enter_world(sobject.get_source_character_name(), clnt);
					Thread.sleep(1000L);
					try {
						if (clnt.getActiveChar() != null) {
							clnt.getActiveChar().sendPackets(
									MJPacketFactory.create_duplicate_message_packets("如果您沒有收到簽到獎勵，請重新連結您的角色。"),
									true);
						}
					} catch (Exception exception) {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1000L);
	}

    /**

	檢查並返回 MJShiftObject 對象。*
	@param token MJShiftObjectOneTimeToken 對象
	@return 檢查後的 MJShiftObject 對象
*/
	private MJShiftObject check_token(MJShiftObjectOneTimeToken token) {
		MJShiftObject sobject = token.shift_object;
		if (token.is_reconnected) {
			sobject = MJShiftObjectHelper.select_shift_object(sobject.get_source_id(), token.home_server_identity);
			if (sobject == null || !this.m_objects.containsKey(Integer.valueOf(sobject.get_destination_id()))) {
				sobject = token.shift_object;
				token.is_reconnected = false;} else {
				sobject = this.m_objects.get(Integer.valueOf(sobject.get_destination_id()));}}
		return sobject;}

    /**

	檢查和設置玩家的傳送類型。*
	@param clnt    客戶端對象
	@param token   MJShiftObjectOneTimeToken 對象
	@param sobject MJShiftObject 對象
*/
	private void check_shift_type(GameClient clnt, MJShiftObjectOneTimeToken token, MJShiftObject sobject) {
		if (sobject.get_shift_type().equals(MJEShiftObjectType.TRANSFER)) {
			MJShiftObjectHelper.delete_shift_object(sobject, token.home_server_identity);
		} else if (sobject.get_shift_type().equals(MJEShiftObjectType.BATTLE)) {
			clnt.set_server_identity(token.home_server_identity);
			clnt.set_server_description(this.m_server_description);
			if (!token.is_reconnected)
				this.m_objects.put(Integer.valueOf(sobject.get_destination_id()), sobject);
		} else {
			throw new IllegalArgumentException(String.format("invalid shift server type %s : %s",
					new Object[] { sobject.get_shift_type(), sobject.get_shift_type().to_name() }));
		}
		clnt.set_shift_type(sobject.get_shift_type());
	}

	/**

	 接收並處理客戶端的傳送請求。*
	 @param clnt             客戶端對象
	 @param source_object_id 來源對象 ID
	 @param token            MJShiftObjectOneTimeToken 對象
	 @throws GameServerFullException    當伺服器滿時拋出
	 @throws AccountAlreadyLoginException 當帳戶已登入時拋出
	 */
	public void do_receive(GameClient clnt, int source_object_id, MJShiftObjectOneTimeToken token)
			throws GameServerFullException, AccountAlreadyLoginException {
		if (token.is_returner) {
			do_getback_player(clnt, source_object_id, token);
			return;}

		MJShiftObject sobject = check_token(token);
		if (!token.is_reconnected) {
			if (sobject.get_destination_id() == 0) {
				sobject.set_destination_id(IdFactory.getInstance().nextId());
			}
			int result = this.m_receiver.work(sobject);
			if (result != 1) {
				System.out.println(String.format("%s(%d)(%s)(%s) 的資料庫轉換失敗.(%d) receive", new Object[] {
						sobject.get_source_character_name(), Integer.valueOf(sobject.get_source_id()), sobject.get_shift_type().to_name(),
						[09:40]
				clnt.getIp(), Integer.valueOf(result) }));
				MJShiftObjectManager.do_fail_send(clnt);
				return;
			}
		}

		clnt.set_shift_object(sobject);
		do_login(clnt, sobject);
		check_shift_type(clnt, token, sobject);
		do_enter_world(clnt, sobject);
	}

    /**

	登錄玩家到伺服器。*
	@param clnt    客戶端對象
	@param sobject MJShiftObject 對象
	@throws GameServerFullException    當伺服器滿時拋出
@throws AccountAlreadyLoginException 當帳戶已登入時拋出
*/
	private void do_login(GameClient clnt, MJShiftObject sobject) throws GameServerFullException, AccountAlreadyLoginException {
		Account account = this.m_local_selector.select_accounts(sobject.get_destination_account_name());
		account.setValid(true);
		clnt.setAccount(account);
		try {
			LoginController.getInstance().login(clnt, account);} catch (AccountAlreadyLoginException e) {
			SC_CUSTOM_MSGBOX.do_kick(clnt, "它將結束，因為有一個角色已經登入。");
			throw new AccountAlreadyLoginException();}}

    /**

	進入遊戲世界。*
	@param clnt    客戶端對象
	@param sobject MJShiftObject 對象
*/
	private void do_enter_world(final GameClient clnt, final MJShiftObject sobject) {
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			public void run() {
				try {
					SC_HIBREED_AUTH_ACK_PACKET pck = SC_HIBREED_AUTH_ACK_PACKET.newInstance();
					pck.set_result(Result.Result_sucess);
					clnt.sendPacket((MJIProtoMessage) pck, MJEProtoMessages.SC_HIBREED_AUTH_ACK_PACKET.toInt(), true);
					clnt.setStatus(MJClientStatus.CLNT_STS_AUTHLOGIN);
					C_LoginToServer.do_direct_enter_world(sobject.get_destination_character_name(), clnt);} catch (Exception e) {
					e.printStackTrace();}}}, 1000L);

	}

    /**

	獲取源角色名稱。*
	@param destination_id 目標ID
@return 源角色名稱
*/
	public String get_source_character_name(int destination_id) {
		MJShiftObject sobject = this.m_objects.get(Integer.valueOf(destination_id));
		return (sobject == null) ? "" : sobject.get_source_character_name();}

    /**

	獲取源角色ID。*
	@param destination_id 目標ID
@return 源角色ID
*/
	public int get_source_character_id(int destination_id) {
		MJShiftObject sobject = this.m_objects.get(Integer.valueOf(destination_id));
		return (sobject == null) ? 0 : sobject.get_source_id();}


