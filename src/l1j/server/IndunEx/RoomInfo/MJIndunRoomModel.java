package l1j.server.IndunEx.RoomInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.IndunEx.PlayInfo.AurakiaInDungeon;
import l1j.server.IndunEx.PlayInfo.CrocodileInDungeon;
import l1j.server.IndunEx.PlayInfo.FantasyInDungeon;
import l1j.server.IndunEx.PlayInfo.GludioInDungeon;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.IndunBuildInfo;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.IndunEnterCondition;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.IndunRoomDetailInfo;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.IndunRoomInfo;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaMapKind;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eDistributionType;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eDungeonType;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_EXIT_INDUN_ROOM_ACK;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.IntRange;

/**
 * 房間內部資訊模型
 * Indun房間進程{@link MJIndunRoomController} -> MJIndunRoomModel ->
 * {@link MJIndunRoomView}
 * MJIndunRoomController 僅通知模型已過濾房間中的變更資料。
 * MJIndunRoomModel僅接收正常流程的控制通知並通知視圖。
 * MJIndunRoomView僅實現專注於廣播的純視圖功能
 * @作者mjsoft。
 **/
public class MJIndunRoomModel {
	static MJIndunRoomModel newInstance(IndunBuildInfo bInfo, L1PcInstance owner) {
		MJIndunRoomModel model = newInstance();
		model.fee = bInfo.get_fee();
		model.distribution_type = bInfo.get_distribution_type();
		model.closed = bInfo.get_closed();
		model.key_item_id = bInfo.get_key_item_id();
		model.map_kind = bInfo.get_map_kind();
		model.max_player = bInfo.get_max_player();
		model.min_level = bInfo.get_min_level();
		model.password = bInfo.get_password();
		model.title = new String(bInfo.get_title());
		model.title_bytes = bInfo.get_title();
		model.indun_key = MJIndunRoomController.KEY_ITEM_ID;

		MJIndunRoomMemberModel member_model = MJIndunRoomMemberModel.newInstance(owner);
		member_model.room_owner = true;
		member_model.ready = true;
		model.owner_member = member_model;
		model.members.put(member_model.member.getId(), member_model);
		return model;
	}

	private static MJIndunRoomModel newInstance() {
		return new MJIndunRoomModel();
	}

	private int room_id;
	private String title;
	private byte[] title_bytes;
	private int min_level;
	private int fee;
	private int key_item_id;
	private int indun_key;
	private eDistributionType distribution_type;
	private boolean closed;
	private int max_player;
	private String password;
	private eArenaMapKind map_kind;
	private boolean played;
	private int observer_count;
	private MJIndunRoomMemberModel owner_member;
	private ConcurrentHashMap<Integer, MJIndunRoomMemberModel> members;

	private MJIndunRoomModel() {
		played = false;
		members = new ConcurrentHashMap<>();
		observer_count = 0;
	}

	public String to_string() {
		StringBuilder sb = new StringBuilder();
		sb.append("room_id").append(" : ").append(room_id).append("\r\n");
		sb.append("title").append(" : ").append(title).append("\r\n");
		sb.append("min_level").append(" : ").append(min_level).append("\r\n");
		sb.append("fee").append(" : ").append(fee).append("\r\n");
		sb.append("key_item_id").append(" : ").append(key_item_id).append("\r\n");
		sb.append("distribution_type").append(" : ").append(distribution_type).append("\r\n");
		sb.append("closed").append(" : ").append(closed).append("\r\n");
		sb.append("max_player").append(" : ").append(max_player).append("\r\n");
		sb.append("password").append(" : ").append(password).append("\r\n");
		sb.append("map_kind").append(" : ").append(map_kind).append("\r\n");
		String tok = MJString.EmptyString;
		for (MJIndunRoomMemberModel member_model : members.values()) {
			sb.append(tok);
			sb.append(member_model.member.getName());
			tok = ",";
		}
		return sb.toString();
	}

	public int get_room_id() {
		return room_id;
	}

	public MJIndunRoomMemberModel get_owner() {
		return owner_member;
	}

	public String get_title() {
		return title;
	}

	public int get_min_level() {
		return min_level;
	}

	public int get_fee() {
		return fee;
	}

	public int get_key_item_id() {
		return key_item_id;
	}

	public int get_indun_key() {
		return indun_key;
	}

	public eDistributionType get_distribution_type() {
		return distribution_type;
	}

	public boolean is_closed() {
		return closed;
	}

	public int get_max_player() {
		return max_player;
	}

	public String get_password() {
		return password;
	}

	public eArenaMapKind get_mapkind() {
		return map_kind;
	}

	public boolean is_played() {
		return played;
	}

	public int get_observer_count() {
		return observer_count;
	}

	public Collection<MJIndunRoomMemberModel> get_members() {
		return Collections.unmodifiableCollection(members.values());
	}

	MJIndunRoomMemberModel get_member(L1PcInstance pc) {
		return members.get(pc.getId());
	}

	int get_members_size() {
		return members.size();
	}

	IndunRoomInfo to_room_info() {
		IndunRoomInfo rInfo = IndunRoomInfo.newInstance();
		rInfo.set_closed(closed);
		rInfo.set_distribution_type(distribution_type);
		rInfo.set_fee(fee);
		rInfo.set_is_locked(false);
		rInfo.set_is_playing(played);
		rInfo.set_map_kind(map_kind);
		rInfo.set_member_count_cur(members.size());
		int maxplayer = 0;
		int minplayer = 0;
		if (rInfo.get_map_kind() == eArenaMapKind.OrimLab_Normal) {
			minplayer = 2;
			maxplayer = 4;
		} else if (rInfo.get_map_kind() == eArenaMapKind.CROCODILE) {
			minplayer = 1;
			maxplayer = 3;
		} else if (rInfo.get_map_kind() == eArenaMapKind.FANTASY) {
			minplayer = 4;
			maxplayer = 8;
		} else if (rInfo.get_map_kind() == eArenaMapKind.AURAKIA) {
			minplayer = 4;
			maxplayer = 8;
		}
		rInfo.set_member_count_max(maxplayer);
		rInfo.set_member_count_min(minplayer);
		// rInfo.set_member_count_max(MJIndunRoomController.MAX_PLAYER);
		// rInfo.set_member_count_min(MJIndunRoomController.MIN_PLAYER);
		rInfo.set_min_level(min_level);
		rInfo.set_room_id(room_id);
		rInfo.set_title(title_bytes);
		return rInfo;
	}

	IndunRoomDetailInfo to_detail_info() {
		IndunRoomDetailInfo dInfo = IndunRoomDetailInfo.newInstance();
		IndunEnterCondition condition = IndunEnterCondition.newInstance();
		condition.set_fee(fee);
		condition.set_key_item_id(key_item_id);
		dInfo.set_condition(condition);
		dInfo.set_distribution_type(distribution_type);
		dInfo.set_dungeon_type(eDungeonType.DEFENCE_TYPE);
		dInfo.set_map_kind(map_kind);
		dInfo.set_max_player(max_player);
		dInfo.set_min_level(min_level);
		return dInfo;
	}

	MJIndunRoomModel cloneAs() {
		MJIndunRoomModel model = newInstance();
		model.drains(this);
		return model;
	}

	MJIndunRoomModel cloneAs(CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ req) {
		MJIndunRoomModel model = cloneAs();
		model.title_bytes = req.get_title();
		model.title = new String(model.title_bytes);
		model.min_level = req.get_min_level();
		model.closed = req.get_closed();
		model.password = req.get_password();
		model.distribution_type = req.get_distribution_type();
		model.max_player = req.get_max_player();
		model.map_kind = req.get_map_kind();
		return model;
	}

	void drains(MJIndunRoomModel model) {
		room_id = model.room_id;
		owner_member = model.owner_member;
		title = model.title;
		title_bytes = model.title_bytes;
		min_level = model.min_level;
		fee = model.fee;
		key_item_id = model.key_item_id;
		distribution_type = model.distribution_type;
		closed = model.closed;
		max_player = model.max_player;
		password = model.password;
		map_kind = model.map_kind;
		played = model.played;
		members = model.members;
	}

	void update_room_id(int room_id) {
		this.room_id = room_id;
	}

	boolean contains_member(L1PcInstance pc) {
		return members.containsKey(pc.getId());
	}

	void onCreated(L1PcInstance pc) {
		doReadyMove(owner_member.member);
		MJIndunRoomView.notifyRoomInfoCreate(this);
	}

	void onChangeProperty() {
		MJIndunRoomView.notifyRoomInfoChanged(this);
	}

	void onClear() {
		// System.out.println("MJIndunRoomModel::清除");
		ArrayList<MJIndunRoomMemberModel> members_model = new ArrayList<MJIndunRoomMemberModel>(members.values());
		for (MJIndunRoomMemberModel member_model : members_model) {
			MJIndunRoomView.notifyExit(this, member_model);
		}
		MJIndunRoomView.notifyRoomInfoChanged(this);
	}

	void onKick(L1PcInstance kicker) {
		MJIndunRoomMemberModel member_model = members.remove(kicker.getId());
		if (member_model != null && members.size() > 0) {
			MJIndunRoomView.notifyKick(this, member_model);
			kicker.sendPackets("您已被踢出，請進入其他房間。");
			onexit(kicker);
		}
		MJIndunRoomView.notifyRoomInfoChanged(this);
		doSafetyMove(kicker);
	}

	void onExit(L1PcInstance exiter) {
		MJIndunRoomMemberModel member_model = members.remove(exiter.getId());
		boolean check = false;
		if (member_model != null) {
			if (member_model.room_owner) {
				int size = get_members_size();
				if (size > 0) {
					/*
					 * for(MJIndunRoomMemberModel next_owner : members.values()) {
					 * next_owner.room_owner = true;
					 * next_owner.ready = true;
					 * break;
					 * }
					 */
					check = true;
				}
			}
			if (check) {
				for (MJIndunRoomMemberModel members : get_members()) {
					members.member.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "房主已離開，請進入其他房間。"));
					members.member.sendPackets("房主已離開，請進入其他房間。");
					onquit(members.member);
					onexit(members.member);
				}
			}
			if (members.size() > 0) {
				MJIndunRoomView.notifyExit(this, member_model);
			}
		}
		MJIndunRoomView.notifyRoomInfoChanged(this);
		doSafetyMove(exiter);
	}

	public void onexit(L1PcInstance exiter) {
		SC_ARENACO_EXIT_INDUN_ROOM_ACK ack = SC_ARENACO_EXIT_INDUN_ROOM_ACK.newInstance();
		ack.set_room_id(get_room_id());
		ack.set_result(SC_ARENACO_EXIT_INDUN_ROOM_ACK.eResult.SUCCESS);
		exiter.sendPackets(ack, MJEProtoMessages.SC_ARENACO_EXIT_INDUN_ROOM_ACK);
	}

	public void onquit(L1PcInstance exiter) {
		MJIndunRoomMemberModel member_model = members.remove(exiter.getId());
		if (member_model != null) {
			MJIndunRoomController.getInstance().end_user_room(exiter, -1);
		}
	}

	public void onClearRoom(L1PcInstance exiter) {
		MJIndunRoomMemberModel member_model = members.remove(exiter.getId());
		boolean check = false;
		if (member_model != null) {
			if (member_model.room_owner) {
				int size = get_members_size();
				if (size > 0) {
					check = true;
				}
			}
			if (check) {
				for (MJIndunRoomMemberModel members : get_members()) {
					members.member.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "房主已離開，請進入其他房間。"));
					members.member.sendPackets("房主已離開，請進入其他房間。");
					onquit(members.member);
					onexit(members.member);
				}
			}
			if (members.size() > 0) {
				MJIndunRoomView.notifyExit(this, member_model);
			}
		}
		MJIndunRoomController.getInstance().clear_room_info(exiter.indun_model());
		MJIndunRoomController.getInstance().reload_room_info();
		MJIndunRoomView.notifyRoomInfoChanged(this);
	}

	void onEnter(L1PcInstance enter, MJIndunRoomModel model) {
		MJIndunRoomMemberModel member_model = MJIndunRoomMemberModel.newInstance(enter);
		members.put(member_model.member.getId(), member_model);
		if (members.size() > 0) {
			MJIndunRoomView.notifyEnter(this, member_model);
		}
		MJIndunRoomView.notifyRoomInfoChanged(member_model, this);
		doReadyMove(enter);
	}

	void onReady(L1PcInstance user) {
		MJIndunRoomMemberModel member_model = members.get(user.getId());
		if (member_model == null)
			return;

		member_model.ready = !member_model.ready;
		MJIndunRoomView.notifyRoomInfoChanged(this);
	}

	void onStart(L1PcInstance pc, MJIndunRoomModel model) {
		played = true;
		switch (model.get_mapkind()) {
			case OrimLab_Minor:
				GludioInDungeon.getInstance().GludioInstanceDungeonStart(fee, model, false);
				break;
			case OrimLab_Normal:
				GludioInDungeon.getInstance().GludioInstanceDungeonStart(fee, model, true);
				break;
			case CROCODILE:
				CrocodileInDungeon.getInstance().CrocodileInstanceDungeonStart(fee, model);
				break;
			case FANTASY:
				FantasyInDungeon.getInstance().FantasyInstanceDungeonStart(fee, model);
				break;
			case AURAKIA:
				AurakiaInDungeon.getInstance().AurakiaInstanceDungeonStart(fee, model);
				break;
			default:
				break;
		}

		/*
		 * if (model.get_mapkind() == eArenaMapKind.OrimLab_Minor) {
		 * GludioInDungeon.getInstance().GludioInstanceDungeonStart(fee, model, false);
		 * } else if (model.get_mapkind() == eArenaMapKind.OrimLab_Normal) {
		 * GludioInDungeon.getInstance().GludioInstanceDungeonStart(fee, model, true);
		 * } else if (model.get_mapkind() == eArenaMapKind.CROCODILE){
		 * 
		 * } else if (model.get_mapkind() == eArenaMapKind.FANTASY){
		 * 
		 * } else if (model.get_mapkind() == eArenaMapKind.AURAKIA){
		 * 
		 * }
		 */
	}

	private void doReadyMove(L1PcInstance pc) {
		pc.start_teleport(32736, 32863, 13000, pc.getHeading(), 18339, false);
	}

	private void doSafetyMove(L1PcInstance pc) {
		// SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_back(pc, 33423, 32813, 4);
		pc.start_teleport(33423, 32813, 4, pc.getHeading(), 18339, false);
	}

	boolean check_owner(L1PcInstance pc) {
		return pc.getId() == owner_member.member.getId();
	}

	boolean check_password() {
		if (!MJString.isNullOrEmpty(password)) {
			if (password.length() != 4 || !closed) {
				return false;
			}
		}
		return true;
	}

	boolean check_min_level(int user_level) {

		return MJIndunRoomController.LEVEL_LIMITES.contains(min_level) && user_level >= min_level;
	}

	boolean check_max_player(MJIndunRoomModel model) {
		int minplayer = 0;
		int maxplayer = 0;
		int curplayer = 0;
		switch (model.get_mapkind()) {
			case OrimLab_Minor:
				// minplayer=2;
				maxplayer = 4;
				// curplayer = IndunRoomInfo.newInstance().get_member_count_cur();;
				break;
			case CROCODILE:
				// minplayer=1;
				maxplayer = 3;
				// curplayer = IndunRoomInfo.newInstance().get_member_count_cur();;
				break;
			case FANTASY:
				// minplayer=4;
				maxplayer = 8;
				// curplayer = IndunRoomInfo.newInstance().get_member_count_cur();;
				break;
			case AURAKIA:
				// minplayer=4;
				maxplayer = 8;
				// curplayer = IndunRoomInfo.newInstance().get_member_count_cur();;
				break;
			default:
				// minplayer=0;
				maxplayer = 0;
				// curplayer = IndunRoomInfo.newInstance().get_member_count_cur();;
				break;
		}

		return IntRange.includes(curplayer, minplayer, maxplayer);
		// return IntRange.includes(max_player, MJIndunRoomController.MIN_PLAYER,
		// MJIndunRoomController.MAX_PLAYER);
	}

	boolean check_title() {
		return !MJString.isNullOrEmpty(title) &&
				IntRange.includes(title.length(), MJIndunRoomController.MIN_TITLE, MJIndunRoomController.MAX_TITLE);
	}

	boolean check_fee() {
		return IntRange.includes(fee, MJIndunRoomController.MIN_FEE, MJIndunRoomController.MAX_FEE);
	}

	boolean check_kind() {
		return map_kind.equals(eArenaMapKind.OrimLab_Minor) || map_kind.equals(eArenaMapKind.OrimLab_Normal)
				|| map_kind.equals(eArenaMapKind.CROCODILE) || map_kind.equals(eArenaMapKind.FANTASY)
				|| map_kind.equals(eArenaMapKind.AURAKIA)/* || map_kind.equals(eArenaMapKind._INSTANCE_DUNGEON_END) */;
	}

	boolean check_distribution_type() {
		return distribution_type != null;
	}

	boolean check_key() {
		return owner_member.member.getInventory().findItemId(MJIndunRoomController.KEY_ITEM_ID) != null;
	}

	boolean check_user_level(int user_level) {
		return user_level >= min_level;
	}

	boolean check_user_password(String user_password) {
		if (MJString.isNullOrEmpty(password))
			return true;

		if (MJString.isNullOrEmpty(user_password))
			return false;

		return password.equalsIgnoreCase(user_password);
	}

	boolean check_user_fee(L1PcInstance user) {
		L1ItemInstance item = user.getInventory().findItemId(L1ItemId.ADENA);
		return item != null && item.getCount() >= fee;
	}

	boolean check_current_player() {
		return members.size() < max_player;
	}
}
