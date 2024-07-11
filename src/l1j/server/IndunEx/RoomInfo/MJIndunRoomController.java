package l1j.server.IndunEx.RoomInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.IndunNotiInfo;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaMapKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_GAME_START_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_KICK_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_NOTI_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_ROOM_INFO_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_ROOM_LIST_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_CREATE_INDUN_ROOM_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_ENTER_INDUN_ROOM_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_EXIT_INDUN_ROOM_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_INVITE_INDUN_ROOM_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_GAME_START_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_KICK_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_NOTI_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_CREATE_INDUN_ROOM_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_ENTER_INDUN_ROOM_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_EXIT_INDUN_ROOM_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_INVITE_INDUN_ROOM_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_INVITE_INDUN_ROOM_ACK.eInviteResult;
import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;

/**
 * 處理 Indone Room 使用者輸入的控制器
 * 
 * @author mjsoft.
 **/
// 좌표 (32736, 32863, 13000) 26 -102, false
public class MJIndunRoomController {
	static final boolean LOGGING = false;
	static final int KEY_ITEM_ID = 4200254; // 410096
	static final int MIN_FEE = 5000;
	static final int MAX_FEE = 20000;
	static final int MIN_TITLE = 1;
	static final int MAX_TITLE = 128;

	static final int COUNT_PER_PAGE = 6;
	static final int INVALID_ROOM_ID = -1;

	static final HashSet<Integer> LEVEL_LIMITES;
	static {
		LEVEL_LIMITES = new HashSet<Integer>();
		LEVEL_LIMITES.add(80);
		LEVEL_LIMITES.add(83);
		LEVEL_LIMITES.add(85);
		LEVEL_LIMITES.add(90);
	}
	private static MJIndunRoomController _instance;

	public static MJIndunRoomController getInstance() {
		if (_instance == null)
			_instance = new MJIndunRoomController();
		return _instance;
	}

	public ConcurrentHashMap<Integer, MJIndunRoomModel> _rooms;
	private HashMap<Integer, KindRoomInfo> _kind_rooms;
	private ConcurrentHashMap<Integer, UserIndunInfo> _indun_users;
	private Object _room_sync;
	public int _room_id;

	private MJIndunRoomController() {
		_rooms = new ConcurrentHashMap<>();
		_kind_rooms = new HashMap<>();
		_indun_users = new ConcurrentHashMap<>();
		_room_sync = new Object();
		_room_id = INVALID_ROOM_ID;
		initialize_kind(eArenaMapKind.OrimLab_Minor);
		initialize_kind(eArenaMapKind.CROCODILE);
		initialize_kind(eArenaMapKind.FANTASY);
		initialize_kind(eArenaMapKind.SPACE);
		initialize_kind(eArenaMapKind.AURAKIA);
		// initialize_kind(eArenaMapKind._INSTANCE_DUNGEON_END);
	}

	/**
	 * 重置房間資訊。
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_BYPASS_INDUN_NOTI_REQ}
	 **/
	private void initialize_kind(eArenaMapKind kind) {
		KindRoomInfo rInfo = new KindRoomInfo();
		rInfo.kind = kind;
		_kind_rooms.put(kind.toInt(), rInfo);
	}

	/**
	 * 當請求房間狀態（事件警報）時調用
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_BYPASS_INDUN_NOTI_REQ}
	 **/
	public void onListRoomAlram(L1PcInstance pc, CS_ARENACO_BYPASS_INDUN_NOTI_REQ req) {
		if (LOGGING) {
			System.out.println("MJIndunRoomController::列出房間警報()");
		}

		SC_ARENACO_BYPASS_INDUN_NOTI_ACK ack = SC_ARENACO_BYPASS_INDUN_NOTI_ACK.newInstance();
		ack.set_result(SC_ARENACO_BYPASS_INDUN_NOTI_ACK.eResult.SUCCESS);
		/*
		 * (1),
		 * ERROR_INTENAL(2),
		 * ERROR_NOT_EXIST_USER(3);
		 */

		for (Map.Entry<Integer, KindRoomInfo> entry : _kind_rooms.entrySet()) {
			if (entry.getKey() == eArenaMapKind.None.toInt())
				continue;
			// 暫時評論
			if (entry.getKey() == 203)
				continue;

			IndunNotiInfo nInfo = IndunNotiInfo.newInstance();
			nInfo.set_count(entry.getValue().models.size());
			nInfo.set_map_kind(eArenaMapKind.fromInt(entry.getKey()));
			ack.add_noti_info(nInfo);
		}

		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_NOTI_ACK);
	}

	/**
	 * 請求房間列表時調用
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_BYPASS_INDUN_ROOM_LIST_REQ}
	 **/
	public void onShowListRooms(L1PcInstance pc, CS_ARENACO_BYPASS_INDUN_ROOM_LIST_REQ req) {
		if (LOGGING) {
			System.out.println("MJIndunRoomController::展示房間列表()");
		}
		SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK ack = SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK.newInstance();
		ack.set_result(SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK.eResult.SUCCESS);

		/*
		 * ERROR_INTENAL(2),
		 * ERROR_NOT_EXIST_USER(3),
		 * ERROR_NOT_EXIST_ROOM(4);
		 */

		int kind_id = req.get_map_kind().toInt();
		List<MJIndunRoomModel> models = null;
		KindRoomInfo rInfo = _kind_rooms.get(kind_id);
		models = rInfo == null ? new ArrayList<>(1) : rInfo.models;

		int rooms_count = models.size();
		if (rooms_count <= 0) {
			ack.set_page_id(1);
			ack.set_total_page(1);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK);
			return;
		}

		int start = (req.get_page_id() - 1) * COUNT_PER_PAGE;
		int last = start + COUNT_PER_PAGE - 1;
		if (last >= rooms_count) {
			start = rooms_count - (rooms_count % COUNT_PER_PAGE);
			last = Math.min(start + COUNT_PER_PAGE - 1, rooms_count - 1);
		}
		ack.set_page_id(calculate_page(start));
		ack.set_total_page(calculate_page(rooms_count - 1));

		// sync 하지 않고 예외로 가능한 만큼만 추가 한다.
		try {
			for (int i = start; i <= last; ++i) {
				MJIndunRoomModel model = models.get(i);
				ack.add_room_info(model.to_room_info());
			}
		} catch (Exception e) {
		}
		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK);
	}

	/**
	 * 當主機嘗試踢球時調用
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_BYPASS_INDUN_KICK_REQ}
	 **/
	public void onKick(L1PcInstance pc, CS_ARENACO_BYPASS_INDUN_KICK_REQ req) {
		if (LOGGING) {
			System.out.println("MJIndunRoomController::踢出()");
		}

		SC_ARENACO_BYPASS_INDUN_KICK_ACK ack = SC_ARENACO_BYPASS_INDUN_KICK_ACK.newInstance();
		ack.set_room_id(req.get_room_id());
		ack.set_kick_arena_char_id(req.get_kick_arena_char_id());
		MJIndunRoomModel model = _rooms.get(req.get_room_id());

		if (model == null) {
			ack.set_result(SC_ARENACO_BYPASS_INDUN_KICK_ACK.eResult.FAIL);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_KICK_ACK);
			return;
		} else if (!model.check_owner(pc) || !check_entered_user_room_id(pc, model.get_room_id())) {
			ack.set_result(SC_ARENACO_BYPASS_INDUN_KICK_ACK.eResult.FAIL_INVALID_ROOM_TYPE);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_KICK_ACK);
			return;
		}

		L1Object obj = L1World.getInstance().findObject((int) req.get_kick_arena_char_id());
		if (obj == null && !(obj instanceof L1PcInstance)) {
			ack.set_result(SC_ARENACO_BYPASS_INDUN_KICK_ACK.eResult.FAIL_INVALID_KICKER);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_KICK_ACK);
			return;
		}

		L1PcInstance kicker = (L1PcInstance) obj;
		if (!check_entered_user_room_id(kicker, model.get_room_id())) {
			ack.set_result(SC_ARENACO_BYPASS_INDUN_KICK_ACK.eResult.FAIL_NOT_EXIST_TARGET);
		} else if (!model.contains_member(kicker)) {
			ack.set_result(SC_ARENACO_BYPASS_INDUN_KICK_ACK.eResult.FAIL_ROOM_EXIT);
		} else {
			update_user_room(kicker, INVALID_ROOM_ID);
			model.onKick(kicker);
			ack.set_result(SC_ARENACO_BYPASS_INDUN_KICK_ACK.eResult.SUCCESS);
		}

		/*
		 * SUCCESS(1),
		 * FAIL(2),
		 * (3),
		 * (4),
		 * (5),
		 * FAIL_ROOM_STATE(6),
		 * (7),
		 * (8);
		 */
		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_KICK_ACK);
	}

	/**
	 * 試圖離開房間時被呼叫
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_EXIT_INDUN_ROOM_REQ}
	 **/
	public void onExitRoom(L1PcInstance pc, CS_ARENACO_EXIT_INDUN_ROOM_REQ req) {
		if (LOGGING) {
			System.out.println("MJIndunRoomController::離開房間()");
		}

		SC_ARENACO_EXIT_INDUN_ROOM_ACK ack = SC_ARENACO_EXIT_INDUN_ROOM_ACK.newInstance();
		ack.set_room_id(req.get_room_id());
		MJIndunRoomModel model = _rooms.get(req.get_room_id());
		if (model == null) {
			ack.set_result(SC_ARENACO_EXIT_INDUN_ROOM_ACK.eResult.FAIL);
		} else {
			ack.set_result(SC_ARENACO_EXIT_INDUN_ROOM_ACK.eResult.SUCCESS);
			update_user_room(pc, INVALID_ROOM_ID);
			model.onExit(pc);
			if (model.get_members_size() <= 0) {
				synchronized (_room_sync) {
					_rooms.remove(model.get_room_id());
					remove_kind_rooms(model.get_mapkind().toInt(), model);
					remove_kind_rooms(eArenaMapKind.None.toInt(), model);
				}
			}
		}
		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_EXIT_INDUN_ROOM_ACK);
	}

	/**
	 * 當主持人邀請時調用
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_INVITE_INDUN_ROOM_REQ}
	 **/
	public void onInviteRoom(L1PcInstance pc, CS_ARENACO_INVITE_INDUN_ROOM_REQ req) {
		if (LOGGING) {
			System.out.println("MJIndunRoomController::邀請進入房間()");
		}
		SC_ARENACO_INVITE_INDUN_ROOM_ACK ack = SC_ARENACO_INVITE_INDUN_ROOM_ACK.newInstance();
		MJIndunRoomModel source_model = _rooms.get(req.get_room_id());
		ack.set_room_id(req.get_room_id());
		ack.set_host_arena_char_id(pc.getId());
		if (source_model == null) {
			ack.set_result(eInviteResult.eRESULT_ERROR);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_INVITE_INDUN_ROOM_ACK);
			return;
		} else if (!source_model.check_owner(pc) || !check_entered_user_room_id(pc, source_model.get_room_id())) {
			ack.set_result(eInviteResult.eRESULT_ERROR);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_INVITE_INDUN_ROOM_ACK);
			return;
		} else if (req.get_guest_char_name() == null) {
			ack.set_result(eInviteResult.eRESULT_ERROR_NOT_EXIST_USER);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_INVITE_INDUN_ROOM_ACK);
			return;
		}

		String guest_name = new String(req.get_guest_char_name());
		if (MJString.isNullOrEmpty(guest_name)) {
			ack.set_result(eInviteResult.eRESULT_ERROR_NOT_EXIST_USER);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_INVITE_INDUN_ROOM_ACK);
			return;
		}
		L1PcInstance guest = L1World.getInstance().getPlayer(guest_name);
		if (guest == null) {
			ack.set_result(eInviteResult.eRESULT_ERROR_NOT_EXIST_USER);
		} else if (already_entered_user(guest)) {
			ack.set_result(eInviteResult.eRESULT_ERROR_REJECT);
		} else {
			ack.set_result(eInviteResult.eRESULT_SUCCESS);
			guest.set_indun_room_num(req.get_room_id());
			guest.sendPackets(new S_Message_YN(C_Attr.MSGCODE_6008_INDUN_INVITE, 6008,
					String.format("%s 邀請您進入格魯迪奧研究所.\n您要進入嗎？", pc.getName())));
		}
		/*
		 * (1),
		 * (2),
		 * (3),
		 * (4),
		 * eRESULT_ERROR_NOT_OPEN_TIME(5);
		 */
		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_INVITE_INDUN_ROOM_ACK);
	}

	/**
	 * 當主機按下開始遊戲按鈕時調用
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_REQ}
	 **/
	public void onGameStart(L1PcInstance pc, CS_ARENACO_BYPASS_INDUN_GAME_START_REQ req) {
		if (LOGGING) {
			System.out.println("MJIndunRoomController::開始遊戲()");
		}
		SC_ARENACO_BYPASS_INDUN_GAME_START_ACK ack = SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.newInstance();
		ack.set_room_id(req.get_room_id());
		MJIndunRoomModel model = _rooms.get(req.get_room_id());
		if (model == null) {
			ack.set_result(SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.eResult.FAIL_INVALID_ROOM);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_GAME_START_ACK);
			return;
		} else if (!model.check_owner(pc) || !check_entered_user_room_id(pc, model.get_room_id())) {
			ack.set_result(SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.eResult.FAIL_INVALID_OWNER);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_GAME_START_ACK);
			return;
		}

		ack.set_result(SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.eResult.SUCCESS);
		if (!model.check_key()) {
			ack.set_result(SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.eResult.FAIL_NOT_ENOUGH_KEY);
		} else if (model.is_played()) {
			ack.set_result(SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.eResult.FAIL_INVALID_STATE);
		} else {
			Collection<MJIndunRoomMemberModel> collections = model.get_members();

			int minplayer = 0;
			if (pc.isGm()) {
				minplayer = 1;
			} else if (model.get_mapkind() == eArenaMapKind.OrimLab_Normal) {
				minplayer = 2;
			} else if (model.get_mapkind() == eArenaMapKind.CROCODILE) {
				minplayer = 1;
			} else if (model.get_mapkind() == eArenaMapKind.FANTASY) {
				minplayer = 4;
			} else if (model.get_mapkind() == eArenaMapKind.AURAKIA) {
				minplayer = 4;
			} else {
				ack.set_result(SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.eResult.FAIL_INVALID_STATE);
			}

			// System.out.println(collections.size());

			// if(collections.size() < MIN_PLAYER) {
			if (collections.size() < minplayer) {
				ack.set_result(SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.eResult.FAIL_INVALID_STATE);
			} else {
				for (MJIndunRoomMemberModel member_model : model.get_members()) {
					if (!member_model.ready) {
						pc.sendPackets(String.format("%s 尚未準備好。", member_model.member.getName()));
						ack.set_result(SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.eResult.FAIL);
						break;
					}
					if (!model.check_user_fee(member_model.member) && !member_model.room_owner) {
						pc.sendPackets(String.format("%s 的參加費不足。", member_model.member.getName()));
						ack.set_result(SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.eResult.FAIL);
						break;
					}
					if (!model.check_min_level(member_model.member.getLevel())) {
						ack.set_result(SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.eResult.FAIL);
						pc.sendPackets(String.format("%s 的等級不足。", member_model.member.getName()));
						break;
					}
				}
			}
		}
		boolean is_success = ack.get_result().toInt() == SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.eResult.SUCCESS.toInt();
		/*
		 * (1),
		 * FAIL(2),
		 * FAIL_INVALID_USER(3),
		 * (4),
		 * (5),
		 * (6),
		 * (7),
		 * (8),
		 * (9),
		 * FAIL_ENTER_LIMIT(10);
		 */
		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_GAME_START_ACK);
		if (is_success) {
			model.onStart(pc, model);
		}
	}

	/**
	 * 當房間成員按就緒鍵時
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_REQ}
	 **/
	public void onChangeReadyEnter(L1PcInstance pc, CS_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_REQ req) {
		if (LOGGING) {
			System.out.println("MJIndunRoomController::準備進入()");
		}

		SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK ack = SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK
				.newInstance();
		ack.set_ready(ack.get_ready());
		ack.set_room_id(ack.get_room_id());
		MJIndunRoomModel model = _rooms.get(req.get_room_id());
		if (model == null) {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK.eResult.FAIL);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK);
			return;
		}

		ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK.eResult.SUCCESS);
		MJIndunRoomMemberModel member_model = model.get_member(pc);
		if (member_model == null) {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK.eResult.FAIL);
		} else if (!member_model.ready) {
			if (!model.check_user_fee(pc)) {
				ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK.eResult.FAIL_NOT_ENOUGH_MONEY);
			} else if (!model.check_user_level(pc.getLevel())) {
				ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK.eResult.FAIL_LEVEL);
			}
		}
		boolean is_success = ack.get_result().toInt() == SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK.eResult.SUCCESS
				.toInt();

		/*
		 * (1),
		 * FAIL(2),
		 * FAIL_NOT_ENOUGH_MONEY(3),
		 * FAIL_LEVEL(4);
		 */
		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK);

		if (is_success) {
			model.onReady(pc);
		}
	}

	/**
	 * 檢查房間資訊時顯示錯誤
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_BYPASS_INDUN_ROOM_INFO_REQ}
	 **/
	public void onSelectRoom(L1PcInstance pc, CS_ARENACO_BYPASS_INDUN_ROOM_INFO_REQ req) {
		if (LOGGING) {
			System.out.println("MJIndunRoomController::選擇房間()");
		}

		SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK ack = SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK.newInstance();
		MJIndunRoomModel model = _rooms.get(req.get_room_id());
		if (model == null) {
			ack.set_result(SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK.eResult.ERROR_NOT_EXIST_ROOM);
		} else {
			ack.set_result(SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK.eResult.SUCCESS);
			ack.set_room_info(model.to_detail_info());
		}

		if (pc.get_indun_room_num() > -1) {
			MJIndunRoomModel invitemodel = _rooms.get(pc.get_indun_room_num());
			if (invitemodel == null) {
				SC_ARENACO_EXIT_INDUN_ROOM_ACK pck = SC_ARENACO_EXIT_INDUN_ROOM_ACK.newInstance();
				pck.set_room_id(pc.get_indun_room_num());
				pck.set_result(SC_ARENACO_EXIT_INDUN_ROOM_ACK.eResult.SUCCESS);
				pc.sendPackets(pck, MJEProtoMessages.SC_ARENACO_EXIT_INDUN_ROOM_ACK);
				pc.set_indun_room_num(-1);
				pc.sendPackets("房間資訊不存在。請進入其他房間。.");
				return;
			}
			update_user_room(pc, invitemodel.get_room_id());
			invitemodel.onEnter(pc, invitemodel);
			pc.set_indun_room_num(-1);
			return;
		}

		/*
		 * (1),
		 * ERROR_INTENAL(2),
		 * ERROR_NOT_EXIST_USER(3),
		 * (4);
		 */
		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK);
	}

	/**
	 * 進入房間時被叫
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_ENTER_INDUN_ROOM_REQ}
	 **/
	public void onEnterRoom(L1PcInstance pc, CS_ARENACO_ENTER_INDUN_ROOM_REQ req) {
		if (LOGGING) {
			System.out.println("MJIndunRoomController::進入房間()");
		}

		SC_ARENACO_ENTER_INDUN_ROOM_ACK ack = SC_ARENACO_ENTER_INDUN_ROOM_ACK.newInstance();
		ack.set_room_id(req.get_room_id());
		if (already_entered_user(pc)) {
			ack.set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult.FAIL_OTHER_ROOM_ENTERED);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_ENTER_INDUN_ROOM_ACK);
			return;
		}

		MJIndunRoomModel model = _rooms.get(req.get_room_id());
		if (model == null) {
			ack.set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult.FAIL_NOT_FOUND_ROOM);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_ENTER_INDUN_ROOM_ACK);
			return;
		}

		if (!model.check_current_player()) {
			ack.set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult.FAIL_FULL);
		} else if (!model.check_user_fee(pc)) {
			ack.set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult.FAIL_NOT_ENOUGH_MONEY);
		} else if (!model.check_user_password(req.get_password())) {
			ack.set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult.FAIL_INVALID_PASSWORD);
		} else if (!model.check_user_level(pc.getLevel())) {
			ack.set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult.FAIL_LEVEL);
		} else if (model.contains_member(pc)) {
			ack.set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult.FAIL_ALREADY_ENTERED_PLAYER);
		} else {
			ack.set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult.SUCCESS);
			update_user_room(pc, model.get_room_id());
			model.onEnter(pc, model);
		}

		/*
		 * FAIL(2),
		 * FAIL_TO_ALREADY_PLAY(3),
		 * (4),
		 * (5),
		 * FAIL_INVALID_ROOM_STATUS(6),
		 * (9),
		 * (10),
		 * (11),
		 * FAIL_INVALID_SERVER(12),
		 * FAIL_ENTER_LIMIT(13);
		 */
		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_ENTER_INDUN_ROOM_ACK);
	}

	/**
	 * 房間設定更改時調用
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ}
	 **/
	public void onChangedRoomProperty(L1PcInstance pc, CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ req) {
		if (LOGGING) {
			System.out.println("MJIndunRoomController::房間屬性變更()");
		}

		MJIndunRoomModel source_model = _rooms.get(req.get_room_id());
		SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK ack = SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK
				.newInstance();
		ack.set_room_id(req.get_room_id());
		if (source_model == null) {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK.eResult.FAIL_NOT_FOUND_ROOM);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK);
			return;
		} else if (!source_model.check_owner(pc) || !check_entered_user_room_id(pc, source_model.get_room_id())) {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK.eResult.FAIL_NOT_OWNER);
			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK);
			return;
		}

		MJIndunRoomModel model = source_model.cloneAs(req);
		if (!model.check_password()) {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK.eResult.FAIL_PASSWORD);
		} else if (!model.check_min_level(pc.getLevel())) {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK.eResult.FAIL_MIN_LEVEL);
		} else if (!model.check_max_player(model)) {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK.eResult.FAIL_MAX_PLAYER);
		} else if (!model.check_title()) {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK.eResult.FAIL_TITLE);
		} else if (!model.check_fee()) {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK.eResult.FAIL);
		} else if (!model.check_kind()) {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK.eResult.FAIL_INVALID_ROOM_STATUS);
		} else if (!model.check_distribution_type()) {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK.eResult.FAIL_DISTRIBUTION_TYPE);
		} else {
			ack.set_result(SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK.eResult.SUCCESS);
			source_model.drains(model);
		}

		/*
		 * (1),
		 * FAIL(2),
		 * FAIL_NOT_EXIST_USER(3),
		 * (4),
		 * (5),
		 * FAIL_CLOSED(6),
		 * (7),
		 * (8),
		 * (9),
		 * (10),
		 * (11),
		 * FAIL_INVALID_ROOMID(12),
		 * (13);
		 */
		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK);
	}

	/**
	 * 建立房間時調用
	 * 
	 * @param pc  {@link L1PcInstance}
	 * @param req {@link CS_ARENACO_CREATE_INDUN_ROOM_REQ}
	 **/

	public void onCreateRoom(L1PcInstance pc, CS_ARENACO_CREATE_INDUN_ROOM_REQ req) {
		if (LOGGING) {
			System.out.println("MJIndunRoomController::創建房間()");
		}
		MJIndunRoomModel model = MJIndunRoomModel.newInstance(req.get_build_info(), pc);
		// System.out.println("地下城類型： "+model.get_mapkind());
		if (LOGGING) {
			System.out.println("MJIndunRoomController: " + model.to_string());
			System.out.println("MJIndunRoomController: " + already_entered_user(pc));
		}
		if (already_entered_user(pc)) {
			// System.out.println("already_entered_user");
			SC_ARENACO_CREATE_INDUN_ROOM_ACK.send_fail(pc,
					SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.ERROR_OTHER_ROOM_ENTERED);
			return;
		}

		if (!model.check_password()) {
			// System.out.println("密碼不同");
			SC_ARENACO_CREATE_INDUN_ROOM_ACK.send_fail(pc, SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.ERROR_PASSWORD);
			return;
		}

		if (!model.check_min_level(pc.getLevel())) {
			// System.out.println("無最低等級： "+model.get_min_level()+"+ PC實驗室: "+
			// pc.getLevel());
			SC_ARENACO_CREATE_INDUN_ROOM_ACK.send_fail(pc, SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.ERROR_LEVEL);
			return;
		}

		if (!model.check_max_player(model)) {
			// System.out.println("房間已滿： "+ model.get_max_player());
			SC_ARENACO_CREATE_INDUN_ROOM_ACK.send_fail(pc,
					SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.ERROR_TEAM_MEMBER_COUNT);
			return;
		}

		if (!model.check_key()) {// 無法創建，因為沒有地牢鑰匙。
			// System.out.println("沒有地牢鑰匙");
			SC_ARENACO_CREATE_INDUN_ROOM_ACK.send_fail(pc,
					SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.ERROR_NOT_ENOUGH_KEY);
			return;
		}

		if (!model.check_title()) {
			// System.out.println("無標題: "+ model.get_title());
			SC_ARENACO_CREATE_INDUN_ROOM_ACK.send_fail(pc,
					SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.ERROR_INVALID_TITLE);
			return;
		}

		if (!model.check_fee()) {
			// System.out.println("金額不符: "+ model.get_fee());
			SC_ARENACO_CREATE_INDUN_ROOM_ACK.send_fail(pc, SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.ERROR_INVALID_FEE);
			return;
		}

		if (!model.check_kind()) {
			// System.out.println("種類不符: "+ model.get_mapkind());
			SC_ARENACO_CREATE_INDUN_ROOM_ACK.send_fail(pc,
					SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.ERROR_CANNOT_CREATE_ARENA);
			return;
		}

		if (!model.check_distribution_type()) {
			// System.out.println("distribution_type: "+model.check_distribution_type());
			SC_ARENACO_CREATE_INDUN_ROOM_ACK.send_fail(pc,
					SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.ERROR_CANNOT_CREATE_ARENA);
			return;
		}

		synchronized (_room_sync) {
			model.update_room_id(++_room_id);
			_rooms.put(_room_id, model);
			put_kind_rooms(model.get_mapkind().toInt(), model);
			put_kind_rooms(eArenaMapKind.None.toInt(), model);
		}
		int model_room_id = model.get_room_id();
		if (model_room_id == INVALID_ROOM_ID) {
			SC_ARENACO_CREATE_INDUN_ROOM_ACK.send_fail(pc,
					SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.ERROR_EXCEED_MAX_ROOM_COUNT);
			return;
		}
		pc.set_indun_model(model);
		update_user_room(pc, model_room_id);
		SC_ARENACO_CREATE_INDUN_ROOM_ACK ack = SC_ARENACO_CREATE_INDUN_ROOM_ACK.newInstance();
		ack.set_room_id(model_room_id);
		ack.set_result(SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.SUCCESS);
		model.onCreated(pc);
		reload_room_info();
		/*
		 * (1),
		 * ERROR_INTERNAL(2),
		 * ERROR_NOT_EXIST_USER(3),
		 * (4),
		 * ERROR_EXCEED_MAX_ARENA_COUNT(5),
		 * (6),
		 * (7),
		 * (8),
		 * ERROR_TEAM_COUNT(9),
		 * (10),
		 * ERROR_ALLOCATE_ARENA(11),
		 * ERROR_JOIN_ROOM(12),
		 * (13),
		 * (14),
		 * (15),
		 * (16),
		 * ERROR_ENTER_LIMIT(17);
		 */
		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_CREATE_INDUN_ROOM_ACK);
	}

	/**
	 * 檢查使用者是否已經在另一個房間。
	 *
	 * @param pc {@link L1PcInstance}
	 * @return 如果您在另一個房間，則傳回 true。
	 **/
	private boolean already_entered_user(L1PcInstance pc) {
		return _indun_users.containsKey(pc.getId()) && !check_entered_user_room_id(pc, INVALID_ROOM_ID);
	}

	/**
	 * 查看使用者室內ID狀態。
	 *
	 * @param pc {@link L1PcInstance}
	 * @參數checked_room_id
	 * @return 如果目前房間ID與checked_room_id相同則傳回true。
	 **/
	private boolean check_entered_user_room_id(L1PcInstance pc, int checked_room_id) {
		UserIndunInfo iInfo = _indun_users.get(pc.getId());
		if (iInfo == null)
			return false;

		return iInfo.room_info.room_id == checked_room_id;
	}

	/**
	 * 更改使用者的室內ID狀態。
	 *
	 * @param pc {@link L1PcInstance}
	 * @paramchanged_room_id 要更改的房間ID
	 **/
	private void update_user_room(L1PcInstance pc, int changed_room_id) {
		UserIndunInfo iInfo = _indun_users.get(pc.getId());
		if (iInfo == null) {
			iInfo = new UserIndunInfo();
			iInfo.owner = pc;
			_indun_users.put(pc.getId(), iInfo);
		}
		iInfo.room_info.room_id = changed_room_id;
	}

	/**
	 * 終止使用者內部房間ID狀態。
	 *
	 * @param pc {@link L1PcInstance}
	 * @paramchanged_room_id 要更改的房間ID
	 **/
	public void end_user_room(L1PcInstance pc, int changed_room_id) {
		UserIndunInfo iInfo = _indun_users.get(pc.getId());
		if (iInfo == null) {
			iInfo = new UserIndunInfo();
			iInfo.owner = pc;
			_indun_users.put(pc.getId(), iInfo);
		}
		iInfo.room_info.room_id = changed_room_id;
	}

	/**
	 * 依種類分別儲存房間資訊。
	 *
	 * @param kind {@link eArenaMapKind}
	 * @param 模型   {@link MJIndunRoomModel}
	 **/
	private void put_kind_rooms(int kind, MJIndunRoomModel model) {
		KindRoomInfo rInfo = _kind_rooms.get(kind);
		if (rInfo == null) {
			rInfo = new KindRoomInfo();
			_kind_rooms.put(kind, rInfo);
		}
		rInfo.models.add(model);
	}

	public void remove_kind_rooms(int kind, MJIndunRoomModel model) {
		KindRoomInfo rInfo = _kind_rooms.get(kind);
		if (rInfo == null)
			return;

		rInfo.models.remove(model);
	}

	private int calculate_page(int offset) {
		return offset < COUNT_PER_PAGE ? 1 : (offset / COUNT_PER_PAGE) + 1;
	}

	static class KindRoomInfo {
		CopyOnWriteArrayList<MJIndunRoomModel> models;
		eArenaMapKind kind;

		KindRoomInfo() {
			models = new CopyOnWriteArrayList<>();
			kind = eArenaMapKind.None;
		}
	}

	static class UserIndunInfo {
		L1PcInstance owner;
		UserIndunRoomInfo room_info;

		UserIndunInfo() {
			room_info = new UserIndunRoomInfo();
		}
	}

	static class UserIndunRoomInfo {
		int room_id;

		UserIndunRoomInfo() {
			room_id = INVALID_ROOM_ID;
		}
	}

	public void clear_room_info(MJIndunRoomModel model) {
		synchronized (_room_sync) {
			_rooms.remove(model.get_room_id());
			remove_kind_rooms(model.get_mapkind().toInt(), model);
			remove_kind_rooms(eArenaMapKind.None.toInt(), model);
		}
	}

	public void reload_room_info() {
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			SC_ARENACO_BYPASS_INDUN_NOTI_ACK ack = SC_ARENACO_BYPASS_INDUN_NOTI_ACK.newInstance();
			ack.set_result(SC_ARENACO_BYPASS_INDUN_NOTI_ACK.eResult.SUCCESS);

			for (Map.Entry<Integer, KindRoomInfo> entry : _kind_rooms.entrySet()) {
				if (entry.getKey() == eArenaMapKind.None.toInt())
					continue;
				// 임時로 주석
				if (entry.getKey() == 203)
					continue;
				IndunNotiInfo nInfo = IndunNotiInfo.newInstance();
				nInfo.set_count(entry.getValue().models.size());
				nInfo.set_map_kind(eArenaMapKind.fromInt(entry.getKey()));
				ack.add_noti_info(nInfo);
			}

			pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_NOTI_ACK);
		}
	}
}
