package l1j.server.IndunEx.RoomInfo;

import java.util.Collection;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.ArenaUserInfo;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CharacterClass;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.Gender;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_ENTER_INDUN_ROOM_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_EXIT_INDUN_ROOM_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_KICK_NOTI;
import l1j.server.server.model.Instance.L1PcInstance;
import org.jetbrains.annotations.NotNull;


/**
 * 向地牢房間內的成員通知房間各種信息反映的視圖
 * @author mjsoft
 */
public class MJIndunRoomView {



	/**
	 * 當房間內有成員退出時通知
	 * @param model {@link MJIndunRoomModel}
	 * @param exiter_model {@link L1PcInstance}
	 */
	static void notifyExit(MJIndunRoomModel model, MJIndunRoomMemberModel exiter_model){
		SC_ARENACO_BYPASS_EXIT_INDUN_ROOM_NOTI noti = SC_ARENACO_BYPASS_EXIT_INDUN_ROOM_NOTI.newInstance();
		noti.set_room_id(model.get_room_id());
		noti.set_user_info(make_arena_user(exiter_model, model));
		notify(noti, MJEProtoMessages.SC_ARENACO_BYPASS_EXIT_INDUN_ROOM_NOTI, model.get_members());
	}



	/**
	 * 當房間內有成員進入時通知
	 * @param model {@link MJIndunRoomModel}
	 * @param enter_model {@link L1PcInstance}
	 */
	static void notifyEnter(MJIndunRoomModel model, MJIndunRoomMemberModel enter_model){
		SC_ARENACO_BYPASS_ENTER_INDUN_ROOM_NOTI noti = SC_ARENACO_BYPASS_ENTER_INDUN_ROOM_NOTI.newInstance();
		noti.set_room_id(model.get_room_id());
		noti.set_user_info(make_arena_user(enter_model, model));
		notify(noti, MJEProtoMessages.SC_ARENACO_BYPASS_ENTER_INDUN_ROOM_NOTI, model.get_members());
	}




	/**
	 * 當房間內有成員被房主踢出時通知
	 * @param model {@link MJIndunRoomModel}
	 * @param kicker_model {@link L1PcInstance}
	 */
	static void notifyKick(MJIndunRoomModel model, MJIndunRoomMemberModel kicker_model){
		SC_ARENACO_BYPASS_INDUN_KICK_NOTI noti = SC_ARENACO_BYPASS_INDUN_KICK_NOTI.newInstance();
		noti.set_room_id(model.get_room_id());
		noti.set_kick_arena_char_id(kicker_model.member.getId());
		noti.set_kick_char_name(kicker_model.member.getName().getBytes(MJEncoding.MS949));
		notify(noti, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_KICK_NOTI, model.get_members());
	}




	/**
	 * 當房間信息變更時進行更新。
	 * @param model {@link MJIndunRoomModel}
	 */
	static void notifyRoomInfoChanged(@NotNull MJIndunRoomModel model){
		SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI noti = SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI.newInstance();
		noti.set_observer_count(model.get_observer_count());
		noti.set_room_id(model.get_room_id());
		Collection<MJIndunRoomMemberModel> collection = model.get_members();
		noti.set_room_info(model.to_room_info());
		for(MJIndunRoomMemberModel member_model : collection){
			ArenaUserInfo uInfo = make_arena_user(member_model, model);
			noti.add_player_info(uInfo);
		}
		/*ArrayList<MJIndunRoomMemberModel> _list = new ArrayList<MJIndunRoomMemberModel>(model.get_members());
		MJIndunRoomMemberModel member_model = null;
		for (int i = _list.size()-1; i >= 0; i--) {
			member_model = (MJIndunRoomMemberModel)_list.get(i);
			ArenaUserInfo uInfo = make_arena_user(member_model, model);
			noti.add_player_info(uInfo);
		}*/
		notify(noti, MJEProtoMessages.SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI, collection);
	}

	/**
	 * 當房間被創建時進行更新。
	 * @param model {@link MJIndunRoomModel}
	 */
	static void notifyRoomInfoCreate(MJIndunRoomModel model){
		SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI noti = SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI.newInstance();
		noti.set_observer_count(model.get_observer_count());
		noti.set_room_id(model.get_room_id());
		Collection<MJIndunRoomMemberModel> collection = model.get_members();
		noti.set_room_info(model.to_room_info());
		for(MJIndunRoomMemberModel member_model : collection){
			ArenaUserInfo uInfo = make_arena_user(member_model, model);
			noti.add_player_info(uInfo);
		}
		notify(noti, MJEProtoMessages.SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI, collection);
	}




	/**
	 * 방에 대한 정보 변경 시 갱신시켜준다.
	 * @param notify_member {@link MJIndunRoomMemberModel} 변경을 갱신받은 멤버
	 * @param model {@link MJIndunRoomModel}
	 **/
	static void notifyRoomInfoChanged(MJIndunRoomMemberModel notify_member, MJIndunRoomModel model) {
		SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI noti = SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI.newInstance();
		noti.set_observer_count(model.get_observer_count());
		noti.set_room_id(model.get_room_id());
		noti.set_room_info(model.to_room_info());
		Collection<MJIndunRoomMemberModel> collection = model.get_members();
		for(MJIndunRoomMemberModel member_model : collection){
			ArenaUserInfo uInfo = make_arena_user(member_model, model);
			noti.add_player_info(uInfo);
		}
		
		/*ArrayList<MJIndunRoomMemberModel> _list = new ArrayList<MJIndunRoomMemberModel>(model.get_members());
		MJIndunRoomMemberModel member_model = null;
		for (int i = _list.size()-1; i >= 0; i--) {
			member_model = (MJIndunRoomMemberModel)_list.get(i);
			ArenaUserInfo uInfo = make_arena_user(member_model, model);
			noti.add_player_info(uInfo);
		}*/
		
		notify_member.member.sendPackets(noti, MJEProtoMessages.SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI);
	}




	/**
	 * 將 {@link L1PcInstance} 轉換為 {@link ArenaUserInfo}
	 * @param member_model {@link L1PcInstance}
	 * @return 轉換後的 {@link ArenaUserInfo}
	 */
	private static ArenaUserInfo make_arena_user(MJIndunRoomMemberModel member_model, MJIndunRoomModel model){
		ArenaUserInfo uInfo = ArenaUserInfo.newInstance();
		L1PcInstance pc = member_model.member;
		
		uInfo.set_arena_char_id(pc.getId());
		uInfo.set_character_class(CharacterClass.fromInt(pc.getClassNumber()));
		uInfo.set_character_name(pc.getName().getBytes(MJEncoding.MS949));
		uInfo.set_gender(pc.get_sex() == 0 ? Gender.MALE : Gender.FEMALE);
		uInfo.set_in_room(true);
		uInfo.set_ready(member_model.ready);
		uInfo.set_role(member_model.role);
		uInfo.set_room_owner(member_model.room_owner);
		uInfo.set_server_id(0);
		uInfo.set_team_id(member_model.team);
		return uInfo;
	}




	/**
	 * 向指定的對象廣播原型消息。
	 * @param not {@link MJIProtoMessage}
	 * @param proto_message {@link MJEProtoMessages}
	 * @param target_collections {@link Collection<L1PcInstance>}
	 */
	private static void notify(MJIProtoMessage not, MJEProtoMessages proto_message, Collection<MJIndunRoomMemberModel> target_collections){
		ProtoOutputStream stream = not.writeTo(proto_message);
		not.dispose();
		for(MJIndunRoomMemberModel member_model : target_collections){
			member_model.member.sendPackets(stream, false);
		}
		stream.dispose();
	}
}
