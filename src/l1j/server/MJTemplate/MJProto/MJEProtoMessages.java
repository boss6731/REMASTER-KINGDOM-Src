
package l1j.server.MJTemplate.MJProto;

import java.util.HashMap;

import l1j.server.Config;
import l1j.server.MJNetServer.MJInterServerEntranceService;
import l1j.server.server.GameClient;
import l1j.server.server.utils.MJHexHelper;

public enum MJEProtoMessages {

	CS_ALCHEMY_DESIGN_REQ(0x007A, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ALCHEMY_DESIGN_REQ.newInstance()),
	CS_ALCHEMY_MAKE_REQ(0x007C, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ALCHEMY_MAKE_REQ.newInstance()),
	SC_ALCHEMY_DESIGN_ACK(0x007B, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_DESIGN_ACK.newInstance()),
	SC_ALCHEMY_MAKE_ACK(0x007D, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_MAKE_ACK.newInstance()),
	SC_ALCHEMY_EXTRA_INFO_NOTI(0x0080,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_EXTRA_INFO_NOTI.newInstance()),

	CS_ATTENDANCE_BONUS_REQUEST_EXTEND(0x0222,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ATTENDANCE_BONUS_REQUEST_EXTEND.newInstance()),
	CS_ATTENDANCE_TAB_OPEN_REQ(0x0995,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ATTENDANCE_TAB_OPEN_REQ.newInstance()),
	CS_ATTENDANCE_REWARD_REQ(0x03EE,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ATTENDANCE_REWARD_REQ.newInstance()),

	SC_ATTENDANCE_BONUS_GROUP_INFO(0x0224,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_GROUP_INFO.newInstance()),
	SC_ATTENDANCE_BONUS_INFO_EXTEND(0x0220,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_INFO_EXTEND.newInstance()),
	SC_ATTENDANCE_COMPLETE_NOTI(0x03ED,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_COMPLETE_NOTI.newInstance()),
	SC_ATTENDANCE_DATA_UPDATE_EXTEND(0x0223,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_DATA_UPDATE_EXTEND.newInstance()),
	SC_ATTENDANCE_INFO_NOTI(0x03EC,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_INFO_NOTI.newInstance()),
	SC_ATTENDANCE_REWARD_ACK(0x03EF,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_REWARD_ACK.newInstance()),
	SC_ATTENDANCE_REWARD_ITEM_NOTI(0x03F1,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_REWARD_ITEM_NOTI.newInstance()),
	SC_ATTENDANCE_STATUS_INFO_NOTI(0x03F0,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_STATUS_INFO_NOTI.newInstance()),
	SC_ATTENDANCE_TAB_OPEN_ACK(0x0996,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_TAB_OPEN_ACK.newInstance()),
	SC_ATTENDANCE_USER_DATA_EXTEND(0x0221,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND.newInstance()),
	SC_ATTENDANCE_USER_DATA(0x0214,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA.newInstance()),

	CS_ACHIEVEMENT_TELEPORT_REQ(0x0235,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ACHIEVEMENT_TELEPORT_REQ.newInstance()),
	SC_ADD_COMPLETED_ACHIEVEMENT_BATCH(0x022F,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_COMPLETED_ACHIEVEMENT_BATCH.newInstance()),

	CS_BUDDY_INFO_CHANGE_REQ(0x091D,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_BUDDY_INFO_CHANGE_REQ.newInstance()),

	CS_CRAFT_LIST_ALL_REQ(0x0036, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_LIST_ALL_REQ.newInstance()),
	CS_CRAFT_ID_LIST_REQ(0x0038, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_ID_LIST_REQ.newInstance()),
	CS_CRAFT_MAKE_REQ(0x003A, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_MAKE_REQ.newInstance()),
	CS_CRAFT_BATCH_REQ(0x005C, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_BATCH_REQ.newInstance()),
	CS_CHAT_REQ_PACKET(0x0202, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CHAT_REQ_PACKET.newInstance()),
	CS_LIMITED_CRAFT_INFO_REQ(0x0942,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_LIMITED_CRAFT_INFO_REQ.newInstance()),
	// SC_CRAFT_LIST_ALL_ACK(0x0037,
	// l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_LIST_ALL_ACK.newInstance()),
	SC_CRAFT_ID_LIST_ACK(0x0039, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_ID_LIST_ACK.newInstance()),
	SC_CRAFT_MAKE_ACK(0x003B, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_MAKE_ACK.newInstance()),
	SC_CRAFT_BATCH_ACK(0x005D, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_BATCH_ACK.newInstance()),

	CS_COMPLETED_ACHIEVEMENT_REWARD_REQ(0x0233,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_COMPLETED_ACHIEVEMENT_REWARD_REQ.newInstance()),

	CS_CHAR_LIST_ENTER_REQ(0x0254,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CHAR_LIST_ENTER_REQ.newInstance()),

	CS_CLIENT_VERSION_INFO(0x0334,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CLIENT_VERSION_INFO.newInstance()),
	CS_CLIENT_INFO_LOG_REQ(0x03EA,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CLIENT_INFO_LOG_REQ.newInstance()),
	CS_CLIENT_FUNC_LOG_NOTI(0x03EB,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CLIENT_FUNC_LOG_NOTI.newInstance()),
	CS_CUSTOM_MSGBOX_RESPONSE(0x3001,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CUSTOM_MSGBOX_RESPONSE.newInstance()),

	CS_EXCLUDE_REQ_PACKET(0x021F, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_EXCLUDE_REQ_PACKET.newInstance()),
	CS_ENTRANCE_QUEUE_CANCEL_REQ(0x0253,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ENTRANCE_QUEUE_CANCEL_REQ.newInstance()),
	CS_EINHASAD_CHARGE_REQ(0x0914,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_EINHASAD_CHARGE_REQ.newInstance()),

	CS_HIBREED_AUTH_REQ(0x0073,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_HIBREED_AUTH_REQ_PACKET.newInstance()),
	SC_CONNECT_HIBREEDSERVER_NOTI_PACKET(0x0071,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.newInstance()),

	CS_MY_RANKING_REQ(0x03FD, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_MY_RANKING_REQ.newInstance()),

	CS_MONSTER_BOOK_V2_REWARD_REQ(0x032B,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_MONSTER_BOOK_V2_REWARD_REQ.newInstance()),
	CS_MONSTER_BOOK_V2_TELEPORT_REQ(0x032F,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_MONSTER_BOOK_V2_TELEPORT_REQ.newInstance()),

	CS_MOVE_SERVER_AUTH_REQ(0x033E,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_MOVE_SERVER_AUTH_REQ.newInstance()),
	CS_NP_LOGIN_REQ(0x03E5, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_NP_LOGIN_REQ.newInstance()),
	CS_PARTY_CONTROL_REQ(0x033C, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_PARTY_CONTROL_REQ.newInstance()),
	CS_PING_ACK(0x03E9, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_PING_ACK.newInstance()),

	CS_QUEST_PROGRESS_REQ(0x0205, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_QUEST_PROGRESS_REQ.newInstance()),
	CS_QUEST_REVEAL_REQ(0x0208, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_QUEST_REVEAL_REQ.newInstance()),
	CS_QUEST_START_REQ(0x020a, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_QUEST_START_REQ.newInstance()),
	CS_QUEST_FINISH_REQ(0x020c, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_QUEST_FINISH_REQ.newInstance()),
	CS_QUEST_TELEPORT_REQ(0x020f, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_QUEST_TELEPORT_REQ.newInstance()),
	CS_QUEST_SHOW_IN_WINDOW_REQ(0x0211,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_QUEST_SHOW_IN_WINDOW_REQ.newInstance()),
	SC_QUEST_PROGRESS_ACK(0x0206, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_PROGRESS_ACK.newInstance()),
	SC_QUEST_PROGRESS_UPDATE_NOTI(0x0207,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_PROGRESS_UPDATE_NOTI.newInstance()),
	SC_QUEST_REVEAL_ACK(0x0209, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_REVEAL_ACK.newInstance()),
	SC_QUEST_START_ACK(0x020b, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_START_ACK.newInstance()),
	SC_QUEST_FINISH_ACK(0x020d, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_FINISH_ACK.newInstance()),
	SC_QUEST_FINISH_NOTI(0x020e, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_FINISH_NOTI.newInstance()),
	SC_QUEST_TELEPORT_ACK(0x0210, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_TELEPORT_ACK.newInstance()),
	SC_QUEST_SHOW_IN_WINDOW_ACK(0x0212,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_SHOW_IN_WINDOW_ACK.newInstance()),

	CS_REVENGE_INFO_REQ(0x041B, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_REVENGE_INFO_REQ.newInstance()),
	CS_REVENGE_TAUNT_REQ(0x041E, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_REVENGE_TAUNT_REQ.newInstance()),
	CS_REVENGE_START_PURSUIT_REQ(0x0420,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_REVENGE_START_PURSUIT_REQ.newInstance()),
	CS_REVENGE_PURSUIT_INFO_REQ(0x0422,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_REVENGE_PURSUIT_INFO_REQ.newInstance()),
	CS_REVENGE_DELETE_REQ(0x0424, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_REVENGE_DELETE_REQ.newInstance()),
	SC_REVENGE_INFO_ACK(0x041C, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REVENGE_INFO_ACK.newInstance()),
	SC_REVENGE_INFO_NOTI(0x041D, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REVENGE_INFO_NOTI.newInstance()),
	SC_REVENGE_TAUNT_ACK(0x041F, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REVENGE_TAUNT_ACK.newInstance()),
	SC_REVENGE_START_PURSUIT_ACK(0x0421,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REVENGE_START_PURSUIT_ACK.newInstance()),
	SC_REVENGE_PURSUIT_INFO_ACK(0x0423,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REVENGE_PURSUIT_INFO_ACK.newInstance()),
	SC_REVENGE_DELETE_ACK(0x0425, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REVENGE_DELETE_ACK.newInstance()),

	CS_STAT_RENEWAL_CALC_INFO_REQ(0x01E4,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_STAT_RENEWAL_CALC_INFO_REQ.newInstance()),
	CS_STAT_RENEWAL_BASESTAT_INFO_REQ(0x01E6,
			l1j.server.MJTemplate.MJProto.MainServer_Client.CS_STAT_RENEWAL_BASESTAT_INFO_REQ.newInstance()),
	SC_STAT_RENEWAL_INFO_NOTI(0x01E3,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_STAT_RENEWAL_INFO_NOTI.newInstance()),
	SC_STAT_RENEWAL_CARRY_WEIGHT_INFO_NOTI(0x01E5,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_STAT_RENEWAL_CARRY_WEIGHT_INFO_NOTI.newInstance()),
	SC_STAT_RENEWAL_BASESTAT_INFO_RES(0x01E7,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_STAT_RENEWAL_BASESTAT_INFO_RES.newInstance()),
	SC_STAT_RENEWAL_BASE_STAT_NOTI(0x01EA,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_STAT_RENEWAL_BASE_STAT_NOTI.newInstance()),

	CS_SEAL_ITEM_REQ(0x0239, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_SEAL_ITEM_REQ.newInstance()),

	CS_TOP_RANKER_REQ(0x0087, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_TOP_RANKER_REQ.newInstance()),

	CS_USER_PLAY_INFO_REQ(0x0322, l1j.server.MJTemplate.MJProto.MainServer_Client.CS_USER_PLAY_INFO_REQ.newInstance()),

	SC_ALL_SPELL_PASSIVE_NOTI(0x0191,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALL_SPELL_PASSIVE_NOTI.newInstance()),
	// SC_ADD_ACTIVE_SPELL_EX_NOTI(0x0192,
	// l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_ACTIVE_SPELL_EX_NOTI.newInstance()),
	SC_ADD_SPELL_PASSIVE_NOTI(0x0192,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_SPELL_PASSIVE_NOTI.newInstance()),

	SC_ADD_CRITERIA_PROGRESS_BATCH(0x0230,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_CRITERIA_PROGRESS_BATCH.newInstance()),
	SC_ACHIEVEMENT_TELEPORT_ACk(0x0236,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ACHIEVEMENT_TELEPORT_ACk.newInstance()),
	SC_ACHIEVEMENT_COMPLETE_NOTI(0x0238,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ACHIEVEMENT_COMPLETE_NOTI.newInstance()),
	SC_AVAILABLE_SPELL_CHANGE_NOTI(0x024A,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_CHANGE_NOTI.newInstance()),
	SC_ARENA_PLAY_EVENT_NOTI(0x02DF,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ARENA_PLAY_EVENT_NOTI.newInstance()),

	SC_AVAILABLE_SPELL_NOTI(0x0411,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI.newInstance()),
	SC_BOX_ATTR_CHANGE_NOTI_PACKET(0x0081,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BOX_ATTR_CHANGE_NOTI_PACKET.newInstance()),
	SC_BASECAMP_CHART_NOTI_PACKET(0x007F,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BASECAMP_CHART_NOTI_PACKET.newInstance()),
	SC_BASECAMP_POINTRANK_NOTI_PACKET(0x0085,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BASECAMP_POINTRANK_NOTI_PACKET.newInstance()),
	SC_BUDDY_INFO_CHANGE_ACK(0x091E,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BUDDY_INFO_CHANGE_ACK.newInstance()),
	SC_BUDDY_LIST_NOTI(0x0151, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BUDDY_LIST_NOTI.newInstance()),

	SC_CLIENTGATE_APPTOKEN_NOTI(0x0070,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CLIENTGATE_APPTOKEN_NOTI.newInstance()),
	SC_CHANGE_TEAM_NOTI_PACKET(0x0082,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHANGE_TEAM_NOTI_PACKET.newInstance()),
	SC_CHAT_ACK_PACKET(0x0203, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHAT_ACK_PACKET.newInstance()),
	SC_CHAT_MESSAGE_NOTI_PACKET(0x0204,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHAT_MESSAGE_NOTI_PACKET.newInstance()),
	SC_COMPLETED_ACHIEVEMENT_REWARD_ACK(0x0234,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.newInstance()),
	SC_CRITERIA_UPDATE_NOTI(0x0237,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRITERIA_UPDATE_NOTI.newInstance()),
	SC_CROWD_CONTROL_NOTI(0x024F, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CROWD_CONTROL_NOTI.newInstance()),
	SC_CUSTOM_MSGBOX(0x3000, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX.newInstance()),
	SC_CHARATER_FOLLOW_EFFECT_NOTI(0x09d7,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHARATER_FOLLOW_EFFECT_NOTI.newInstance()),
	SC_DAMAGE_OF_TIME_NOTI(0x0193,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_DAMAGE_OF_TIME_NOTI.newInstance()),
	SC_DISCONNECT_SOCKET_NOTI_PACKET(0x0083,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_DISCONNECT_SOCKET_NOTI_PACKET.newInstance()),
	SC_DASH_MOVE_NOTI(0x0193, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_DASH_MOVE_NOTI.newInstance()),
	SC_DIALOGUE_MESSAGE_NOTI(0x0244,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_DIALOGUE_MESSAGE_NOTI.newInstance()),
	SC_EXP_BOOSTING_INFO_NOTI(0x025b,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EXP_BOOSTING_INFO_NOTI.newInstance()),
	SC_EVENT_COUNTDOWN_NOTI_PACKET(0x021C,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EVENT_COUNTDOWN_NOTI_PACKET.newInstance()),
	SC_ENTRANCE_INFO_NOTI(0x0252, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ENTRANCE_INFO_NOTI.newInstance()),
	SC_FATIGUE_NOTI(0x014E, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_FATIGUE_NOTI.newInstance()),
	SC_FOURTH_GEAR_NOTI(0x094E, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_FOURTH_GEAR_NOTI.newInstance()),
	SC_HIBREED_AUTH_ACK_PACKET(0x0074,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_HIBREED_AUTH_ACK_PACKET.newInstance()),
	SC_HYPERTEXT(0x033A, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_HYPERTEXT.newInstance()),
	SC_INSTANCE_HP_NOTI(0x09d6, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_INSTANCE_HP_NOTI.newInstance()),
	SC_LIMITED_CRAFT_INFO_ACK(0x0943,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_LIMITED_CRAFT_INFO_ACK.newInstance()),
	SC_MESSAGE_BOX(0x0333, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MESSAGE_BOX.newInstance()),
	SC_MY_RANKING_ACK(0x03FE, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MY_RANKING_ACK.newInstance()),
	SC_MAGIC_EVASION_NOTI(0x01E8, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MAGIC_EVASION_NOTI.newInstance()),
	SC_MESSAGE_NOTI(0x0250, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MESSAGE_NOTI.newInstance()),
	SC_MONSTER_BOOK_V2_INFO_NOTI(0x032A,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_INFO_NOTI.newInstance()),
	SC_MONSTER_BOOK_V2_REWARD_ACK(0x032C,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_REWARD_ACK.newInstance()),
	SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI(0x032D,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI.newInstance()),
	SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI(0x032E,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI.newInstance()),
	SC_MONSTER_BOOK_V2_TELEPORT_ACK(0x0330,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_TELEPORT_ACK.newInstance()),
	SC_MOVE_SERVER_AUTH_ERROR_ACK(0x033F,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MOVE_SERVER_AUTH_ERROR_ACK.newInstance()),
	SC_NOTIFICATION_MESSAGE(0x013C,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE.newInstance()),
	SC_NOTIFICATION_MESSAGE_NOT(0x0040,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE_NOT.newInstance()),
	SC_NOTIFICATION_STRINGKINDEX_NOTI(0x0067,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_STRINGKINDEX_NOTI.newInstance()),
	SC_NOTIFICATION_INFO_NOTI(0x008D,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_INFO_NOTI.newInstance()),
	SC_NOTIFICATION_CHANGE_NOTI(0x008E,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_CHANGE_NOTI.newInstance()),
	SC_NPC_SPEED_VALUE_FLAG_NOTI(0x0246,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NPC_SPEED_VALUE_FLAG_NOTI.newInstance()),
	SC_POLYMORPH_EVENT_NOTI(0x01D0,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_POLYMORPH_EVENT_NOTI.newInstance()),
	SC_PARTY_MEMBER_MARK_CHANGE_NOTI(0x0153,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_MARK_CHANGE_NOTI.newInstance()),
	SC_PARTY_OPERATION_RESULT_NOTI(0x021B,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_OPERATION_RESULT_NOTI.newInstance()),
	SC_PK_MESSAGE_AT_BATTLE_SERVER(0x0247,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PK_MESSAGE_AT_BATTLE_SERVER.newInstance()),
	SC_PARTY_MEMBER_LIST(0x0337, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_LIST.newInstance()),
	SC_PARTY_MEMBER_LIST_CHANGE(0x0338,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_LIST_CHANGE.newInstance()),
	SC_PARTY_MEMBER_STATUS(0x0339,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_STATUS.newInstance()),
	SC_PARTY_SYNC_PERIODIC_INFO(0x033B,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_SYNC_PERIODIC_INFO.newInstance()),
	SC_PING_REQ(0x03E8, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PING_REQ.newInstance()),
	SC_POLYMORPH_NOTI(0x040D, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_POLYMORPH_NOTI.newInstance()),

	SC_REST_GAUGE_CHARGE_NOTI(0x0092,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REST_GAUGE_CHARGE_NOTI.newInstance()),
	SC_SIEGE_ZONE_UPDATE_NOT(0x0042,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_ZONE_UPDATE_NOT.newInstance()),
	SC_SIEGE_INJURY_TIME_NOIT(0x004C,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_INJURY_TIME_NOIT.newInstance()),
	SC_SPELL_BUFF_NOTI(0x006E, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.newInstance()),

	SC_SERVER_VERSION_INFO(0x0335,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SERVER_VERSION_INFO.newInstance()),
	SC_SPECIAL_RESISTANCE_NOTI(0x03F7,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.newInstance()),
	SC_SPEED_BONUS_NOTI(0x040C, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPEED_BONUS_NOTI.newInstance()),
	SC_SPELL_DELAY_NOTI(0x040F, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_DELAY_NOTI.newInstance()),
	SC_SUMMON_PET_NOTI(0x0911, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SUMMON_PET_NOTI.newInstance()),
	SC_SCENE_NOTI(0x0098, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SCENE_NOTI.newInstance()),
	SC_TOP_RANKER_ACK(0x0088, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_ACK.newInstance()),
	SC_TOP_RANKER_NOTI(0x0089, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI.newInstance()),
	SC_THEBE_CAPTURE_INFO_NOTI_PACKET(0x0086,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_THEBE_CAPTURE_INFO_NOTI_PACKET.newInstance()),
	SC_TOTAL_DRINKED_ELIXIR_NOTI(0x01e9,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOTAL_DRINKED_ELIXIR_NOTI.newInstance()),
	SC_TEAM_EMBLEM_SWITCH_NOTI(0x022E,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TEAM_EMBLEM_SWITCH_NOTI.newInstance()),
	SC_TEAM_ID_SERVER_NO_MAPPING_INFO(0x0336,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TEAM_ID_SERVER_NO_MAPPING_INFO.newInstance()),
	SC_USER_START_SUNDRY_NOTI(0x007E,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_USER_START_SUNDRY_NOTI.newInstance()),
	SC_USER_PLAY_INFO_NOTI(0x0323,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_USER_PLAY_INFO_NOTI.newInstance()),
	SC_VOICE_CHAT_ROOM_INFO_NOTI(0x091B,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_VOICE_CHAT_ROOM_INFO_NOTI.newInstance()),
	SC_WORLD_PUT_NOTI(0x0076, l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI.newInstance()),
	SC_WORLD_PUT_OBJECT_NOTI(0x0077,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI.newInstance()),
	SC_WHOUSER_NOTI_PACKET(0x0078,
			l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WHOUSER_NOTI_PACKET.newInstance()),

	// 아레나
	SC_ARENACO_SERVER_STATUS_NOTI(0x02BC,
			l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo.SC_ARENACO_SERVER_STATUS_NOTI.newInstance()),
	SC_ARENA_GAME_INFO_NOTI(0x02DD,
			l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo.SC_ARENA_GAME_INFO_NOTI.newInstance()),
	SC_ARENA_PLAY_STATUS_NOTI(0x02DE,
			l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo.SC_ARENA_PLAY_STATUS_NOTI.newInstance()),

	SC_EQUIP_INFO_NOTI(0x0329,
			l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_EQUIP_INFO_NOTI.newInstance()),
	CS_BUILDER_TELEPORT_USER_REQ(0x099d,
			l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.CS_BUILDER_TELEPORT_USER_REQ.newInstance()),
	SC_LIST_USER_NOTI(0x024E,
			l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_LIST_USER_NOTI.newInstance()),
	SC_MSG_ANNOUNCE(0x0342,
			l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_MSG_ANNOUNCE.newInstance()),
	SC_SPELL_LATE_HANDLING_NOTI(0x0410,
			l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_SPELL_LATE_HANDLING_NOTI.newInstance()),

	// 펫관련
	SC_COMPANION_STATUS_NOTI(0x07D0,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_STATUS_NOTI.newInstance()),
	CS_COMPANION_NAME_CHANGE_REQ(0x07D1,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_COMPANION_NAME_CHANGE_REQ.newInstance()),
	SC_COMPANION_NAME_CHANGE_ACK(0x07D2,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_NAME_CHANGE_ACK.newInstance()),
	CS_COMPANION_STAT_INCREASE_REQ(0x07D3,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_COMPANION_STAT_INCREASE_REQ.newInstance()),
	SC_COMPANION_BUFF_NOTI(0x07D5,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_BUFF_NOTI.newInstance()),
	CS_COMPANION_TM_COMMAND_REQ(0x07D4,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_COMPANION_TM_COMMAND_REQ.newInstance()),
	SC_COMPANION_SKILL_NOTI(0x07D6,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_SKILL_NOTI.newInstance()),
	CS_COMPANION_SKILL_NEXT_TIER_OPEN_REQ(0x07D7,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_COMPANION_SKILL_NEXT_TIER_OPEN_REQ
					.newInstance()),
	CS_COMPANION_SKILL_ENCHANT_REQ(0x07D8,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_COMPANION_SKILL_ENCHANT_REQ.newInstance()),
	SC_COMPANION_SKILL_ENCHANT_ACK(0x07D9,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_SKILL_ENCHANT_ACK.newInstance()),
	SC_COMPANION_COMBAT_DATA_NOTI(0x07DA,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_COMBAT_DATA_NOTI.newInstance()),
	CS_SUMMON_TM_COMMAND_REQ(0x09cf,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_SUMMON_TM_COMMAND_REQ.newInstance()),
	CS_SUMMON_DISSMISS_REQ(0x09d0,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_SUMMON_DISSMISS_REQ.newInstance()),

	// 데스페널티
	CS_DEATH_PENALTY_RECOVERY_EXP_REQ(0x09b1,
			l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY.CS_DEATH_PENALTY_RECOVERY_EXP_REQ
					.newInstance()),
	CS_DEATH_PENALTY_RECOVERY_ITEM_REQ(0x09b0,
			l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY.CS_DEATH_PENALTY_RECOVERY_ITEM_REQ
					.newInstance()),
	SC_DEATH_PENALTY_RECOVERY_EXP_LIST_NOTI(0x09b3,
			l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY.SC_DEATH_PENALTY_RECOVERY_EXP_LIST_NOTI
					.newInstance()),
	SC_DEATH_PENALTY_RECOVERY_ITEM_LIST_NOTI(0x09b2,
			l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY.SC_DEATH_PENALTY_RECOVERY_ITEM_LIST_NOTI
					.newInstance()),

	// 아인하사드 포인트
	CS_EINHASAD_POINT_ENCHANT_START_REQ(0x0924,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.CS_EINHASAD_POINT_ENCHANT_START_REQ
					.newInstance()),
	SC_EINHASAD_POINT_ENCHANT_START_ACK(0x0925,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_ENCHANT_START_ACK
					.newInstance()),
	CS_EINHASAD_POINT_ENCHANT_STAT_REQ(0x0926,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.CS_EINHASAD_POINT_ENCHANT_STAT_REQ
					.newInstance()),
	SC_EINHASAD_POINT_ENCHANT_STAT_ACK(0x0927,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_ENCHANT_STAT_ACK
					.newInstance()),
	CS_EINHASAD_POINT_ENCHANT_END_REQ(0x0928,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.CS_EINHASAD_POINT_ENCHANT_END_REQ
					.newInstance()),
	CS_EINHASAD_POINT_STAT_INVEST_REQ(0x0929,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.CS_EINHASAD_POINT_STAT_INVEST_REQ
					.newInstance()),
	SC_EINHASAD_POINT_STAT_INVEST_ACK(0x092A,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_STAT_INVEST_ACK
					.newInstance()),
	SC_EINHASAD_POINT_POINT_NOTI(0x092B,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_POINT_NOTI.newInstance()),
	SC_EINHASAD_POINT_STAT_INFO_NOTI(0x092C,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_STAT_INFO_NOTI
					.newInstance()),
	CS_EINHASAD_STAT_INVEST_REQ(0x092d,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.CS_EINHASAD_STAT_INVEST_REQ.newInstance()),
	SC_EINHASAD_STAT_INVEST_ACK(0x092e,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_STAT_INVEST_ACK.newInstance()),

	// 아인하사드의신의
	CS_EINHASAD_FAITH_ENABLE_INDEX_REQ(0x0a7b,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.CS_EINHASAD_FAITH_ENABLE_INDEX_REQ
					.newInstance()),
	SC_EINHASAD_FAITH_ENABLE_INDEX_ACK(0x0a7C,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_ENABLE_INDEX_ACK
					.newInstance()),

	SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI(0x0a7d,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI
					.newInstance()),
	SC_EINHASAD_FAITH_BUFF_NOTI(0x0a88,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_BUFF_NOTI.newInstance()),
	CS_EINHASAD_FAITH_LIST_REQ(0x0a78,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.CS_EINHASAD_FAITH_LIST_REQ.newInstance()),
	SC_EINHASAD_FAITH_LIST_NOTI(0x0a79,
			l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_LIST_NOTI.newInstance()),

	// 장비 작용 슬롯
	CS_EXTEND_SLOT_CURRENCY_SELECT(0x095C,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_Equip.CS_EXTEND_SLOT_CURRENCY_SELECT.newInstance()),
	SC_EXTEND_SLOT_CURRENCY_NOTI(0x095D,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_Equip.SC_EXTEND_SLOT_CURRENCY_NOTI.newInstance()),
	CS_EXTEND_SLOT_REQ(0x095E, l1j.server.MJTemplate.MJProto.Mainserver_Client_Equip.CS_EXTEND_SLOT_REQ.newInstance()),
	SC_EXTEND_SLOT_INFO(0x095F,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_Equip.SC_EXTEND_SLOT_INFO.newInstance()),
	SC_EXTEND_SLOT_RESULT_NOTI(0x0960,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_Equip.SC_EXTEND_SLOT_RESULT_NOTI.newInstance()),

	// 이벤트 푸쉬
	SC_EVENT_PUSH_ADD_NOTI(0x099f,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_ADD_NOTI.newInstance()),
	CS_EVENT_PUSH_INFO_LIST_REQ(0x09a1,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.CS_EVENT_PUSH_INFO_LIST_REQ.newInstance()),
	SC_EVENT_PUSH_INFO_LIST_ACK(0x09a2,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_INFO_LIST_ACK.newInstance()),
	CS_EVENT_PUSH_LIST_STATUS_READ_REQ(0x09a3,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.CS_EVENT_PUSH_LIST_STATUS_READ_REQ.newInstance()),
	SC_EVENT_PUSH_LIST_STATUS_READ_ACK(0x09a4,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_LIST_STATUS_READ_ACK.newInstance()),
	CS_EVENT_PUSH_LIST_STATUS_DELETE_REQ(0x09a5,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.CS_EVENT_PUSH_LIST_STATUS_DELETE_REQ
					.newInstance()),
	SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK(0x09a6,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK
					.newInstance()),
	CS_EVENT_PUSH_ITEM_LIST_RECEIVE_REQ(0x09a7,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.CS_EVENT_PUSH_ITEM_LIST_RECEIVE_REQ
					.newInstance()),
	SC_EVENT_PUSH_ITEM_LIST_RECEIVE_ACK(0x09a8,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_ITEM_LIST_RECEIVE_ACK
					.newInstance()),
	CS_EVENT_PUSH_UPDATE_INTO_LIST_REQ(0x09a9,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.CS_EVENT_PUSH_UPDATE_INTO_LIST_REQ.newInstance()),
	SC_EVENT_PUSH_UPDATE_INTO_LIST_ACK(0x09aa,
			l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_UPDATE_INTO_LIST_ACK.newInstance()),

	SC_GAMEGATE_PCCAFE_CHARGE_NOTI(0x0343,
			l1j.server.MJTemplate.MJProto.MainServer_Client_GameGate.SC_GAMEGATE_PCCAFE_CHARGE_NOTI.newInstance()),

	// 헌팅 퀘스트
	CS_HUNTING_QUEST_MAP_SELECT_REQ(0x0989,
			l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.CS_HUNTING_QUEST_MAP_SELECT_REQ.newInstance()),
	SC_HUNTING_QUEST_MAP_LIST_NOTI(0x098a,
			l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.SC_HUNTING_QUEST_MAP_LIST_NOTI.newInstance()),
	CS_HUNTING_QUEST_REWARD_REQ(0x098b,
			l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.CS_HUNTING_QUEST_REWARD_REQ.newInstance()),
	SC_HUNTING_QUEST_MAP_UPDATE_AMOUNT_NOTI(0x098c,
			l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.SC_HUNTING_QUEST_MAP_UPDATE_AMOUNT_NOTI
					.newInstance()),
	CS_HUNTING_GIVE_THE_USER_VALUE_REQ(0x098d,
			l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.CS_HUNTING_GIVE_THE_USER_VALUE_REQ
					.newInstance()),
	SC_HUNTING_GIVE_THE_USER_VALUE_ACK(0x098e,
			l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.SC_HUNTING_GIVE_THE_USER_VALUE_ACK
					.newInstance()),
	CS_HUNTING_TELEPORT_REQ(0x098f,
			l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.CS_HUNTING_TELEPORT_REQ.newInstance()),
	CS_HUNTING_GUIDE_BOOK_MAP_INFO_REQ(0x0990,
			l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.CS_HUNTING_GUIDE_BOOK_MAP_INFO_REQ
					.newInstance()),
	SC_HUNTING_GUIDE_BOOK_MAP_INFO_ACK(0x0991,
			l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.SC_HUNTING_GUIDE_BOOK_MAP_INFO_ACK
					.newInstance()),
	SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK(0x0992,
			l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK
					.newInstance()),

	// 인던 관련
	CS_INDUN_MATCHING_CANCEL_REQ(0x08C1,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_INDUN_MATCHING_CANCEL_REQ.newInstance()),
	SC_ARENACO_BYPASS_INDUN_MATCHING_REGISTER_ACK(0x08C2,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_MATCHING_REGISTER_ACK
					.newInstance()),
	CS_INDUN_MATCHING_REGISTER_REQ(0x08C3,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_INDUN_MATCHING_REGISTER_REQ.newInstance()),
	SC_ARENACO_BYPASS_INDUN_MATCHING_CANCEL_ACK(0x08C4,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_MATCHING_CANCEL_ACK
					.newInstance()),
	SC_ARENACO_BYPASS_INDUN_MATCHING_READY_NOTI(0x08C5,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_MATCHING_READY_NOTI
					.newInstance()),
	CS_ARENACO_BYPASS_INDUN_MATCHING_ACCEPT_REQ(0x08C6,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_MATCHING_ACCEPT_REQ
					.newInstance()),
	SC_ARENACO_BYPASS_INDUN_MATCHING_SUCCESS_NOTI(0x08C8,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_MATCHING_SUCCESS_NOTI
					.newInstance()),
	CS_ARENACO_BYPASS_INDUN_QUICK_START_REQ(0x08C9,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_QUICK_START_REQ
					.newInstance()),
	SC_ARENACO_BYPASS_INDUN_QUICK_START_ACK(0x08CA,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_QUICK_START_ACK
					.newInstance()),
	CS_INDUN_LEAVE_LOBBY_NOTI(0x08CC,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_INDUN_LEAVE_LOBBY_NOTI.newInstance()),
	SC_ARENACO_BYPASS_INDUN_KICK_AFK_OWNER_NOTI(0x08CD,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_KICK_AFK_OWNER_NOTI
					.newInstance()),
	CS_ARENACO_BYPASS_INDUN_ROOM_LIST_REQ(0x08a3,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_ROOM_LIST_REQ.newInstance()),
	SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK(0x08a4,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK.newInstance()),
	CS_ARENACO_BYPASS_INDUN_ROOM_INFO_REQ(0x08a5,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_ROOM_INFO_REQ.newInstance()),
	SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK(0x08a6,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK.newInstance()),
	CS_ARENACO_CREATE_INDUN_ROOM_REQ(0x08a7,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_CREATE_INDUN_ROOM_REQ.newInstance()),
	SC_ARENACO_CREATE_INDUN_ROOM_ACK(0x08a8,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_CREATE_INDUN_ROOM_ACK.newInstance()),
	CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ(0x08a9,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_REQ
					.newInstance()),
	SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK(0x08aa,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_PROPERTY_ACK
					.newInstance()),
	SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI(0x08ab,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI
					.newInstance()),
	CS_ARENACO_ENTER_INDUN_ROOM_REQ(0x08ac,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_ENTER_INDUN_ROOM_REQ.newInstance()),
	SC_ARENACO_ENTER_INDUN_ROOM_ACK(0x08ad,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_ENTER_INDUN_ROOM_ACK.newInstance()),
	SC_ARENACO_BYPASS_ENTER_INDUN_ROOM_NOTI(0x08ae,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_ENTER_INDUN_ROOM_NOTI
					.newInstance()),
	CS_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_REQ(0x08af,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_REQ
					.newInstance()),
	SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK(0x08b0,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_CHANGE_INDUN_READY_ENTER_ACK
					.newInstance()),
	CS_ARENACO_BYPASS_INDUN_GAME_START_REQ(0x08b1,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_GAME_START_REQ.newInstance()),
	SC_ARENACO_BYPASS_INDUN_GAME_START_ACK(0x08b2,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_GAME_START_ACK.newInstance()),
	CS_ARENACO_INVITE_INDUN_ROOM_REQ(0x08b3,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_INVITE_INDUN_ROOM_REQ.newInstance()),
	SC_ARENACO_INVITE_INDUN_ROOM_ACK(0x08b4,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_INVITE_INDUN_ROOM_ACK.newInstance()),
	CS_ARENACO_BYPASS_INDUN_NOTI_REQ(0x08b5,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_NOTI_REQ.newInstance()),
	SC_ARENACO_BYPASS_INDUN_NOTI_ACK(0x08b6,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_NOTI_ACK.newInstance()),
	CS_ARENACO_EXIT_INDUN_ROOM_REQ(0x08b7,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_EXIT_INDUN_ROOM_REQ.newInstance()),
	SC_ARENACO_EXIT_INDUN_ROOM_ACK(0x08b8,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_EXIT_INDUN_ROOM_ACK.newInstance()),
	SC_ARENACO_BYPASS_EXIT_INDUN_ROOM_NOTI(0x08b9,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_EXIT_INDUN_ROOM_NOTI.newInstance()),
	CS_ARENACO_BYPASS_INDUN_KICK_REQ(0x08ba,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.CS_ARENACO_BYPASS_INDUN_KICK_REQ.newInstance()),
	SC_ARENACO_BYPASS_INDUN_KICK_ACK(0x08bb,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_KICK_ACK.newInstance()),
	SC_ARENACO_BYPASS_INDUN_KICK_NOTI(0x08bc,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_BYPASS_INDUN_KICK_NOTI.newInstance()),
	SC_INDUN_TOWER_HIT_POINT_RATIO_NOTI(0x08bd,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_INDUN_TOWER_HIT_POINT_RATIO_NOTI.newInstance()),
	SC_DEAD_RESTART_ACK(0x08cf,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_DEAD_RESTART_ACK.newInstance()),

	// 인터레이스
	CS_MAPUI_TELEPORT_REQ(0x08FD,
			l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.CS_MAPUI_TELEPORT_REQ.newInstance()),
	SC_INTER_RACING_READY_NOTI(0x08FE,
			l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.SC_INTER_RACING_READY_NOTI.newInstance()),
	SC_INTER_RACING_START_NOTI(0x08FF,
			l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.SC_INTER_RACING_START_NOTI.newInstance()),

	SC_INTER_RACING_STOP_NOTI(0x0900,
			l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.SC_INTER_RACING_STOP_NOTI.newInstance()),
	SC_INTER_RACING_MOVING_NOTI(0x0901,
			l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.SC_INTER_RACING_MOVING_NOTI.newInstance()),
	CS_INTER_RACING_STATUS_INFO_REQ(0x0902,
			l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.CS_INTER_RACING_STATUS_INFO_REQ.newInstance()),
	SC_INTER_RACING_STATUS_INFO_ACK(0x0903,
			l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.SC_INTER_RACING_STATUS_INFO_ACK.newInstance()),
	SC_INTER_RACING_TICKET_SELL_LIST_NOTI(0x0904,
			l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.SC_INTER_RACING_TICKET_SELL_LIST_NOTI
					.newInstance()),
	CS_INTER_RACING_RANK_INFO_REQ(0x0905,
			l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.CS_INTER_RACING_RANK_INFO_REQ.newInstance()),
	SC_INTER_RACING_RANK_INFO_ACK(0x0906,
			l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.SC_INTER_RACING_RANK_INFO_ACK.newInstance()),

	// 인벤토리 관련
	SC_ADD_INVENTORY_NOTI(0x024C,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ADD_INVENTORY_NOTI.newInstance()),
	SC_UPDATE_INVENTORY_NOTI(0x024D,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_UPDATE_INVENTORY_NOTI.newInstance()),

	SC_OBTAINED_ITEM_INFO(0x0260,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_OBTAINED_ITEM_INFO.newInstance()),

	CS_BMTYPE_DEL_CHECK_REQ(0x025C,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_BMTYPE_DEL_CHECK_REQ.newInstance()),
	SC_BMTYPE_DEL_CHECK_ACK(0x025D,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_BMTYPE_DEL_CHECK_ACK.newInstance()),
	SC_ENCHANT_RESULT(0x025E,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ENCHANT_RESULT.newInstance()),

	SC_PERSONAL_SHOP_ITEM_LIST_NOTI(0x0407,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_PERSONAL_SHOP_ITEM_LIST_NOTI.newInstance()),
	SC_WAREHOUSE_ITEM_LIST_NOTI(0x0408,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_WAREHOUSE_ITEM_LIST_NOTI.newInstance()),
	SC_EXCHANGE_ITEM_LIST_NOTI(0x0409,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_EXCHANGE_ITEM_LIST_NOTI.newInstance()),
	SC_GOODS_INVEN_NOTI(0x040E,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI.newInstance()),

	CS_GET_TOGGLE_INFO_REQ(0x0a04,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_GET_TOGGLE_INFO_REQ.newInstance()),
	SC_GET_TOGGLE_INFO_ACK(0x0a05,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GET_TOGGLE_INFO_ACK.newInstance()),
	// CS_SET_TOGGLE_INFO_REQ(0x0a06,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_SET_TOGGLE_INFO_REQ.newInstance()),
	// SC_SET_TOGGLE_INFO_ACK(0x0a07,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SET_TOGGLE_INFO_ACK.newInstance()),
	// SC_NOTI_TOGGLE_INFO(0x0a08,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_NOTI_TOGGLE_INFO.newInstance()),

	CS_EXTEND_CHAR_SLOT_REQ(0x09d4,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_EXTEND_CHAR_SLOT_REQ.newInstance()),
	SC_EXTEND_CHAR_SLOT_ACK(0x09d5,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_EXTEND_CHAR_SLOT_ACK.newInstance()),

	SC_ENCHANT_RESULT_NOTI(0x0242,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ENCHANT_RESULT_NOTI.newInstance()),

	// 아이템 셀렉터
	CS_SELECTION_BAG_I_WANT_IT_REQ(0x09CD,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_SELECTION_BAG_I_WANT_IT_REQ.newInstance()),
	CS_SELECTION_BAG_CANCEL_REQ(0x09CB,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_SELECTION_BAG_CANCEL_REQ.newInstance()),
	SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI(0x09ca,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI
					.newInstance()),

	// 시간충전석
	CS_MAP_TIME_CHARGE_REQ(0x09c7,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_MAP_TIME_CHARGE_REQ.newInstance()),
	SC_CHARGED_MAP_TIME_INFO_NOTI(0x09c6,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_CHARGED_MAP_TIME_INFO_NOTI.newInstance()),

	// CS_FAVOR_CRAFT_MAKE_REQ(0x0A5b,
	// l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_MAKE_REQ.newInstance()),
	CS_FAVOR_ENGRAVE_REQ(0x0A5b,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_FAVOR_ENGRAVE_REQ.newInstance()),
	CS_FAVOR_BOOK_LIST_REQ(0xA59,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_FAVOR_BOOK_LIST_REQ.newInstance()),

	// 재련시스템
	CS_SYNTHESIS_SMELTING_START_REQ(0x09c3,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_SYNTHESIS_SMELTING_START_REQ.newInstance()),
	CS_SYNTHESIS_SMELTING_END_REQ(0x0A72,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_SYNTHESIS_SMELTING_END_REQ.newInstance()),
	CS_SYNTHESIS_SMELTING_DESIGN_REQ(0x09bd,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_SYNTHESIS_SMELTING_DESIGN_REQ.newInstance()),
	// CS_SYNTHESIS_SMELTING_PROB_REQ(0x0002,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_SYNTHESIS_SMELTING_PROB_REQ.newInstance()),

	CS_SMELTING_MAKE_REQ(0x09bf,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_SMELTING_MAKE_REQ.newInstance()),
	// CS_INSERT_SMELTING_SLOT_REQ(0x09c1,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_INSERT_SMELTING_SLOT_REQ.newInstance()),

	SC_SYNTHESIS_SMELTING_START_ACK(0x09c4,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SYNTHESIS_SMELTING_START_ACK.newInstance()),
	SC_SYNTHESIS_SMELTING_DESIGN_ACK(0x09be,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SYNTHESIS_SMELTING_DESIGN_ACK.newInstance()),
	// SC_SYNTHESIS_SMELTING_PROB_ACK(0x0000,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SYNTHESIS_SMELTING_PROB_ACK.newInstance()),

	SC_SMELTING_MAKE_ACK(0x09c0,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SMELTING_MAKE_ACK.newInstance()),
	// SC_SMELTING_SLOT_INFO_NOTI(0x09ba,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SMELTING_SLOT_INFO_NOTI.newInstance()),
	SC_SMELTING_UPDATE_SLOT_INFO_NOTI(0x09c2,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SMELTING_UPDATE_SLOT_INFO_NOTI.newInstance()),
	// SC_ITEM_SMELTING_SLOT_TOTAL_COUNT_NOTI(0x09ba,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ITEM_SMELTING_SLOT_TOTAL_COUNT_NOTI.newInstance()),

	// SC_ITEM_NAME_ID_IN_SELECTION_BAG(
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ITEM_NAME_ID_IN_SELECTION_BAG.newInstance()),

	// 실렉티스 전시관
	SC_TIME_COLLECTION_DATA_LOAD_NOTI(0x0A5f,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_DATA_LOAD_NOTI.newInstance()),
	SC_TIME_COLLECTION_SET_DATA_NOTI(0x0A60,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_SET_DATA_NOTI.newInstance()),

	CS_TIME_COLLECTION_REGIST_ITEM_REQ(0x0A61,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_TIME_COLLECTION_REGIST_ITEM_REQ.newInstance()),
	SC_TIME_COLLECTION_REGIST_ITEM_ACK(0x0A62,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_REGIST_ITEM_ACK.newInstance()),

	CS_TIME_COLLECTION_RESET_REQ(0x0A63,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_TIME_COLLECTION_RESET_REQ.newInstance()),
	SC_TIME_COLLECTION_RESET_ACK(0x0A64,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_RESET_ACK.newInstance()),
	// CS_TIME_COLLECTION_SELECT_BONUS_REQ(0x0A65,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_TIME_COLLECTION_SELECT_BONUS_REQ.newInstance()),
	CS_TIME_COLLECTION_ADENA_REFILL_REQ(0x0A65,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_TIME_COLLECTION_ADENA_REFILL_REQ
					.newInstance()),
	SC_TIME_COLLECTION_ADENA_REFILL_ACK(0x0a66,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_ADENA_REFILL_ACK
					.newInstance()),

	CS_TIME_COLLECTION_CHANGE_BUFF_REQ(0x0A67,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.CS_TIME_COLLECTION_CHANGE_BUFF_REQ.newInstance()),
	SC_TIME_COLLECTION_CHANGE_BUFF_ACK(0x0A68,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_CHANGE_BUFF_ACK.newInstance()),

	SC_TIME_COLLECTION_BUFF_NOTI(0x0A6B,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_BUFF_NOTI.newInstance()),

	// SC_TIME_COLLECTION_SELECT_BONUS_ACK(0x0A60,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_SELECT_BONUS_ACK.newInstance()),

	// PSS
	CS_PARTY_ASSIST_TARGET_BROADCAST_REQ(0x0257,
			l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.CS_PARTY_ASSIST_TARGET_BROADCAST_REQ
					.newInstance()),
	SC_PARTY_ASSIST_TARGET_BROADCAST_NOTI(0x0258,
			l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_PARTY_ASSIST_TARGET_BROADCAST_NOTI
					.newInstance()),

	CS_START_PLAY_SUPPORT_REQ(0x0835,
			l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.CS_START_PLAY_SUPPORT_REQ.newInstance()),
	SC_START_PLAY_SUPPORT_ACK(0x0836,
			l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_START_PLAY_SUPPORT_ACK.newInstance()),

	CS_FINISH_PLAY_SUPPORT_REQ(0x0837,
			l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.CS_FINISH_PLAY_SUPPORT_REQ.newInstance()),
	SC_FINISH_PLAY_SUPPORT_ACK(0x0838,
			l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FINISH_PLAY_SUPPORT_ACK.newInstance()),
	SC_FORCE_FINISH_PLAY_SUPPORT_NOTI(0x0839,
			l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FORCE_FINISH_PLAY_SUPPORT_NOTI
					.newInstance()),

	CS_PSS_TIME_CHECK_REQ(0x083A,
			l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.CS_PSS_TIME_CHECK_REQ.newInstance()),
	SC_PSS_TIME_CHECK_ACK(0x083B,
			l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_PSS_TIME_CHECK_ACK.newInstance()),

	CS_CHANGE_PLAY_SUPPORT_SETTING_REQ(0x083F,
			l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.CS_CHANGE_PLAY_SUPPORT_SETTING_REQ
					.newInstance()),
	CS_PLAYSUPORT_POLYMORPH_REQ(0x0841,
			l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.CS_PLAY_SUPPORT_POLYMORPH_REQ.newInstance()),
	SC_REST_EXP_INFO_NOTI(0x03FC,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI.newInstance()),

	// 혈맹 관련
	CS_BLOOD_PLEDGE_JOIN_REQ(0x0142,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_JOIN_REQ.newInstance()),
	SC_BLOOD_PLEDGE_JOIN_ACK(0x0143,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOIN_ACK.newInstance()),
	CS_BLOOD_PLEDGE_JOINING_LIST_REQ(0x0144,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_JOINING_LIST_REQ.newInstance()),
	SC_BLOOD_PLEDGE_JOINING_LIST_ACK(0x0145,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOINING_LIST_ACK.newInstance()),
	CS_BLOOD_PLEDGE_JOIN_OPTION_CHANGE_REQ(0x0146,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_JOIN_OPTION_CHANGE_REQ
					.newInstance()),
	SC_BLOOD_PLEDGE_JOIN_OPTION_CHANGE_ACK(0x0147,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOIN_OPTION_CHANGE_ACK
					.newInstance()),
	CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ(0x0148,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ.newInstance()),
	SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK(0x0149,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK.newInstance()),
	CS_BLOOD_PLEDGE_JOIN_CANCEL_REQ(0x014A,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_JOIN_CANCEL_REQ.newInstance()),
	SC_BLOOD_PLEDGE_JOIN_CANCEL_ACK(0x014B,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOIN_CANCEL_ACK.newInstance()),
	CS_BLOOD_PLEDGE_JOIN_OPTION_REQ(0x014C,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_JOIN_OPTION_REQ.newInstance()),
	SC_BLOOD_PLEDGE_JOIN_OPTION_ACK(0x014D,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOIN_OPTION_ACK.newInstance()),
	SC_BLOODPLEDGE_USER_INFO_NOTI(0x0219,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOODPLEDGE_USER_INFO_NOTI.newInstance()),
	CS_BLESS_OF_BLOOD_PLEDGE_PICK_REQ(0x03F8,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLESS_OF_BLOOD_PLEDGE_PICK_REQ.newInstance()),
	CS_BLESS_OF_BLOOD_PLEDGE_SHUFFLE_REQ(0x03F9,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLESS_OF_BLOOD_PLEDGE_SHUFFLE_REQ.newInstance()),
	SC_BLESS_OF_BLOOD_PLEDGE_ACK(0x03FA,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLESS_OF_BLOOD_PLEDGE_ACK.newInstance()),
	SC_BLESS_OF_BLOOD_PLEDGE_UPDATE_NOTI(0x03FB,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLESS_OF_BLOOD_PLEDGE_UPDATE_NOTI.newInstance()),
	SC_BLOOD_PLEDGE_ALLY_LIST(0x040A,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_ALLY_LIST.newInstance()),
	SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE(0x040B,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.newInstance()),
	CS_BLOOD_PLEDGE_CREATE_REQ(0x0966,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_CREATE_REQ.newInstance()),
	CS_INTRODUCTION_MESSAGE_CHANGE_REQ(0x0967,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_INTRODUCTION_MESSAGE_CHANGE_REQ.newInstance()),
	CS_BLOOD_PLEDGE_LIST_REQ(0x0968,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_LIST_REQ.newInstance()),
	SC_BLOOD_PLEDGE_LIST_ACK(0x0969,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_LIST_ACK.newInstance()),
	CS_BLOOD_PLEDGE_FIND_REQ(0x096A,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_FIND_REQ.newInstance()),
	SC_BLOOD_PLEDGE_FIND_ACK(0x096B,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_FIND_ACK.newInstance()),
	CS_BLOOD_PLEDGE_PRE_JOIN_REQ(0x096D,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_PRE_JOIN_REQ.newInstance()),
	CS_BLOOD_PLEDGE_MERCHANT_GOODS_LIST_REQ(0x096E,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_MERCHANT_GOODS_LIST_REQ
					.newInstance()),
	SC_BLOOD_PLEDGE_CONTRIBUTION_ACK(0x096F,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_CONTRIBUTION_ACK.newInstance()),
	CS_BLOOD_PLEDGE_CONTRIBUTION_REQ(0x0970,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_CONTRIBUTION_REQ.newInstance()),
	// SC_BLOODPLEDGE_MEMBER_CHANGED_NOTI(0x0000,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOODPLEDGE_MEMBER_CHANGED_NOTI.newInstance()),

	CS_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_REQ(0x0aaf,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_REQ
					.newInstance()),
	SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK(0x0ab0,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK
					.newInstance()),
	CS_BLOOD_PLEDGE_JOIN_LIMIT_USER_DEL_REQ(0x0ab1,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_JOIN_LIMIT_USER_DEL_REQ
					.newInstance()),

	CS_BLOOD_PLEDGE_JOIN_LIMIT_INFO_REQ(0x0aac,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_JOIN_LIMIT_INFO_REQ.newInstance()),
	SC_BLOOD_PLEDGE_JOIN_LIMIT_INFO_ACK(0x0aad,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOIN_LIMIT_INFO_ACK.newInstance()),
	CS_BLOOD_PLEDGE_JOIN_LIMIT_LEVEL_UPDATE_REQ(0x0aae,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_JOIN_LIMIT_LEVEL_UPDATE_REQ
					.newInstance()),

	SC_BLOOD_PLEDGE_ENTER_NOTICE_NOTI(0x0ab2,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_ENTER_NOTICE_NOTI.newInstance()),
	CS_BLOOD_PLEDGE_UPDATE_ENTER_NOTICE_REQ(0x0ab3,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_UPDATE_ENTER_NOTICE_REQ
					.newInstance()),

	CS_BLOOD_PLEDGE_STORE_ALLOW_ADD_REQ(0x0ac9,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_STORE_ALLOW_ADD_REQ.newInstance()),
	CS_BLOOD_PLEDGE_STORE_ALLOW_DEL_REQ(0x0acb,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_STORE_ALLOW_DEL_REQ.newInstance()),
	CS_BLOOD_PLEDGE_STORE_ALLOW_INFO_REQ(0x0ac7,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.CS_BLOOD_PLEDGE_STORE_ALLOW_INFO_REQ.newInstance()),

	SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK(0x0aca,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK.newInstance()),
	SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_ACK(0x0ac8,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_ACK.newInstance()),
	SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI(0x0acc,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI.newInstance()),

	// 포텐셜
	CS_PETBALL_CONTENTS_START_REQ(0x0948,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Potential.CS_PETBALL_CONTENTS_START_REQ.newInstance()),
	CS_PETBALL_CONTENTS_END_REQ(0x0949,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Potential.CS_PETBALL_CONTENTS_END_REQ.newInstance()),
	CS_ENCHANT_POTENTIAL_REQ(0x094A,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Potential.CS_ENCHANT_POTENTIAL_REQ.newInstance()),
	CS_ENCHANT_POTENTIAL_UPDATE_REQ(0x094C,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Potential.CS_ENCHANT_POTENTIAL_UPDATE_REQ.newInstance()),

	SC_PETBALL_CONTENTS_START_ACK(0x094F,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Potential.SC_PETBALL_CONTENTS_START_ACK.newInstance()),
	SC_ENCHANT_POTENTIAL_RESULT_NOTI(0x094B,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Potential.SC_ENCHANT_POTENTIAL_RESULT_NOTI.newInstance()),
	SC_ENCHANT_POTENTIAL_RESTART_NOTI(0x094D,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Potential.SC_ENCHANT_POTENTIAL_RESTART_NOTI.newInstance()),

	// 스펠
	CS_SPELL_PASSIVE_ONOFF_REQ(0x0198,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Spell.CS_SPELL_PASSIVE_ONOFF_REQ.newInstance()),
	SC_SPELL_PASSIVE_ONOFF_ACK(0x0199,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Spell.SC_SPELL_PASSIVE_ONOFF_ACK.newInstance()),
	SC_ADD_TEMP_PASSIVE_SPELL_NOTI(0x0939,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Spell.SC_ADD_TEMP_PASSIVE_SPELL_NOTI.newInstance()),
	SC_REMOVE_TEMP_PASSIVE_SPELL_NOTI(0x093a,
			l1j.server.MJTemplate.MJProto.MainServer_Client_Spell.SC_REMOVE_TEMP_PASSIVE_SPELL_NOTI.newInstance()),

	SC_USER_FORM_NOTI(0x0957, l1j.server.MJTemplate.MJProto.MainServer_Client_UserForm.SC_USER_FORM_NOTI.newInstance()),
	SC_MASS_TELEPORT_STATE_NOTI(0x0A22,
			l1j.server.MJTemplate.MJProto.MainServer_Client_UserForm.SC_MASS_TELEPORT_STATE_NOTI.newInstance()),
	CS_MASS_TELEPORT_STATE_REQ(0x0A21,
			l1j.server.MJTemplate.MJProto.MainServer_Client_UserForm.CS_MASS_TELEPORT_STATE_REQ.newInstance()),

	// 웨폰마스터리
	// SC_WEAPON_MASTERY_INFO_NOTI(0x0867,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery.SC_WEAPON_MASTERY_INFO_NOTI.newInstance()),
	// SC_WEAPON_MASTERY_GAUGE_NOTI(0x0868,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery.SC_WEAPON_MASTERY_GAUGE_NOTI.newInstance()),
	// SC_WEAPON_MASTERY_POINT_NOTI(0x0869,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery.SC_WEAPON_MASTERY_POINT_NOTI.newInstance()),
	// CS_WEAPON_MASTERY_ENCHANT_REQ(0x086A,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery.CS_WEAPON_MASTERY_ENCHANT_REQ.newInstance()),
	// SC_WEAPON_MASTERY_ENCHANT_ACK(0x086B,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery.SC_WEAPON_MASTERY_ENCHANT_ACK.newInstance()),
	// SC_WEAPON_MASTERY_SLOT_OPEN_NOTI(0x086C,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery.SC_WEAPON_MASTERY_SLOT_OPEN_NOTI.newInstance()),
	// CS_WEAPON_MASTERY_ROULETTE_TRY_REQ(0x086D,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery.CS_WEAPON_MASTERY_ROULETTE_TRY_REQ.newInstance()),
	// SC_WEAPON_MASTERY_ROULETTE_TRY_ACK(0x086E,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery.SC_WEAPON_MASTERY_ROULETTE_TRY_ACK.newInstance()),
	// CS_WEAPON_MASTERY_ROULETTE_SLOT_LOCK_REQ(0x086F,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery.CS_WEAPON_MASTERY_ROULETTE_SLOT_LOCK_REQ.newInstance()),
	// SC_WEAPON_MASTERY_ROULETTE_SLOT_LOCK_ACK(0x0870,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery.SC_WEAPON_MASTERY_ROULETTE_SLOT_LOCK_ACK.newInstance()),
	// SC_WEAPON_MASTERY_ROULETTE_SLOT_OPEN_NOTI(0x0871,
	// l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery.SC_WEAPON_MASTERY_ROULETTE_SLOT_OPEN_NOTI.newInstance()),

	// 무한대전
	SC_INFINITY_BATTLE_BOARD_INFO_NOTI(0x093B,
			l1j.server.MJTemplate.MJProto.TeamInfo.SC_INFINITY_BATTLE_BOARD_INFO_NOTI.newInstance()),
	SC_INFINITY_BATTLE_ENTER_MAP_NOTI(0x093C,
			l1j.server.MJTemplate.MJProto.TeamInfo.SC_INFINITY_BATTLE_ENTER_MAP_NOTI.newInstance()),
	SC_INFINITY_BATTLE_LEAVE_MAP_NOTI(0x093D,
			l1j.server.MJTemplate.MJProto.TeamInfo.SC_INFINITY_BATTLE_LEAVE_MAP_NOTI.newInstance()),

	// resultcode
	CS_PC_MASTER_INFO_REQ(0x0a7e, l1j.server.MJTemplate.MJProto.resultCode.CS_PC_MASTER_INFO_REQ.newInstance()),
	CS_PC_MASTER_UTILITY_REQ(0x0a80, l1j.server.MJTemplate.MJProto.resultCode.CS_PC_MASTER_UTILITY_REQ.newInstance()),

	SC_PC_MASTER_FAVOR_UPDATE_NOTI(0x0a81,
			l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_FAVOR_UPDATE_NOTI.newInstance()),
	SC_PC_MASTER_INFO_ACK(0x0a7f, l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_INFO_ACK.newInstance()),

	CS_PC_MASTER_GOLDEN_BUFF_ENFORCE_REQ(0x0a82,
			l1j.server.MJTemplate.MJProto.resultCode.CS_PC_MASTER_GOLDEN_BUFF_ENFORCE_REQ.newInstance()),
	CS_PC_MASTER_GOLDEN_BUFF_SWITCH_TYPE_REQ(0x0a83,
			l1j.server.MJTemplate.MJProto.resultCode.CS_PC_MASTER_GOLDEN_BUFF_SWITCH_TYPE_REQ.newInstance()),

	SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI(0x0a85,
			l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.newInstance()),
	SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI(0x0a84,
			l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.newInstance()),
	CS_PC_MASTER_MERCHANT_GOODS_LIST_REQ(0x0a86,
			l1j.server.MJTemplate.MJProto.resultCode.CS_PC_MASTER_MERCHANT_GOODS_LIST_REQ.newInstance()),

	// system
	CS_FREE_BUFF_SHIELD_INFO_REQ(0x0ab6,
			l1j.server.MJTemplate.MJProto.MainServer_Client_System.CS_FREE_BUFF_SHIELD_INFO_REQ.newInstance()),
	SC_FREE_BUFF_SHIELD_INFO_ACK(0x0ab7,
			l1j.server.MJTemplate.MJProto.MainServer_Client_System.SC_FREE_BUFF_SHIELD_INFO_ACK.newInstance()),
	SC_FREE_BUFF_SHIELD_UPDATE_NOTI(0x0ab8,
			l1j.server.MJTemplate.MJProto.MainServer_Client_System.SC_FREE_BUFF_SHIELD_UPDATE_NOTI.newInstance()),

	// CS_SHELTER_OWNER_CHANGE_REQ(0x0000,
	// l1j.server.MJTemplate.MJProto.resultCode.CS_SHELTER_OWNER_CHANGE_REQ.newInstance()),
	// SC_SHELTER_OWNER_CHANGE_ACK(0x0000,
	// l1j.server.MJTemplate.MJProto.resultCode.SC_SHELTER_OWNER_CHANGE_ACK.newInstance()),

	;

	private int value;
	private MJIProtoMessage message;

	MJEProtoMessages(int value, MJIProtoMessage message) {
		this.value = value;
		this.message = message;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(MJEProtoMessages v) {
		return value == v.value;
	}

	public MJIProtoMessage getMessageInstance() {
		return message;
	}

	public void reloadMessage() {
		MJIProtoMessage tmp = message;
		message = message.reloadInstance();
		if (tmp != null) {
			tmp.dispose();
			tmp = null;
		}
	}

	public MJIProtoMessage copyInstance() {
		return message == null ? null : message.copyInstance();
	}

	private static final HashMap<Integer, MJEProtoMessages> messages;
	static {
		MJEProtoMessages[] msgs = MJEProtoMessages.values();
		messages = new HashMap<Integer, MJEProtoMessages>(msgs.length);
		for (MJEProtoMessages m : msgs)
			messages.put(m.toInt(), m);
	}

	public static MJEProtoMessages fromInt(int messageId) {
		return messages.get(messageId);
	}

	public static boolean existsProto(GameClient clnt, byte[] bytes) {
		int messageId = bytes[1] & 0xff | bytes[2] << 8 & 0xff00;
		MJEProtoMessages message = fromInt(messageId);

		if (message == null) {
/**
 * TODO 在CMD窗口打印未找到的PROTO代碼
 * 測試0x800時，使用0x100會顯示所有結果
 * 查找副代碼時，從100到2500逐漸增加會更方便
 **/
			if (Config.Synchronization.NotBindProtoCode && messageId >= 0x0000) {
				System.out.println(MJHexHelper.toString(bytes, bytes.length));
			}
			return false;
		}

		if (message == CS_HIBREED_AUTH_REQ) {
			MJInterServerEntranceService.service().offer(clnt, bytes);
			return true;
		}

		MJIProtoMessage iMessage = message.message.copyInstance().readFrom(clnt, bytes);
		if (!iMessage.isInitialized()) {
			System.out.println(MJHexHelper.toString(bytes, bytes.length));
			printNotInitialized(clnt.getActiveChar() == null ? clnt.getIp() : clnt.getActiveChar().getName(), messageId,
					iMessage.getInitializeBit());
		}
		iMessage.dispose();
		return true;
	}

	public static void printNotInitialized(String ownerInfo, int messageId, long bit) {
		System.out.println(createNotInitializedMessage(ownerInfo, messageId, bit));
	}

	public static String createNotInitializedMessage(String ownerInfo, int messageId, long bit) {
		return String.format("MJProto 未初始化。擁有者資訊 : %s, 訊息 ID : %s, 初始化位 : %08X", ownerInfo, fromInt(messageId).name(), bit);
	}

	public static void getInstance() {
// System.out.println("MJEProtoMessages Initialized.");
	}
}
