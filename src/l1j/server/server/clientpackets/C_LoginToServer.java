package l1j.server.server.clientpackets;

import static l1j.server.server.GMCommands.securityBuff;
import static l1j.server.server.model.skill.L1SkillId.ADDITIONAL_FIRE;
import static l1j.server.server.model.skill.L1SkillId.ANTA_BUFF;
import static l1j.server.server.model.skill.L1SkillId.BERSERKERS;
import static l1j.server.server.model.skill.L1SkillId.BLOOD_LUST;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_1;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_DARKELF_1;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_1;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_ELF_1;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_FENCER_1;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_KNIGHT_1;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_PRINCE_1;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_WARRIOR_1;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_WIZARD_1;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_2;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_DARKELF_2;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_2;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_ELF_2;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_FENCER_2;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_KNIGHT_2;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_PRINCE_2;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_WARRIOR_2;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_WIZARD_2;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_3;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_DARKELF_3;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_3;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_ELF_3;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_FENCER_3;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_KNIGHT_3;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_PRINCE_3;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_WARRIOR_3;
import static l1j.server.server.model.skill.L1SkillId.CLASS_RANK_BLESS_WIZARD_3;
import static l1j.server.server.model.skill.L1SkillId.CLEAR_MIND;
import static l1j.server.server.model.skill.L1SkillId.COMA_A;
import static l1j.server.server.model.skill.L1SkillId.COMA_B;
import static l1j.server.server.model.skill.L1SkillId.CONCENTRATION;
import static l1j.server.server.model.skill.L1SkillId.COOKING_BEGIN;
import static l1j.server.server.model.skill.L1SkillId.COOKING_END;
import static l1j.server.server.model.skill.L1SkillId.COOK_DEX_Bless;
import static l1j.server.server.model.skill.L1SkillId.COOK_GROW_Bless;
import static l1j.server.server.model.skill.L1SkillId.COOK_INT_Bless;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR_Bless;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_MIRROR;
import static l1j.server.server.model.skill.L1SkillId.DANCING_BLADES;
import static l1j.server.server.model.skill.L1SkillId.DECAY_POTION;
import static l1j.server.server.model.skill.L1SkillId.DECIDING_BUFF;
import static l1j.server.server.model.skill.L1SkillId.DECREASE_WEIGHT;
import static l1j.server.server.model.skill.L1SkillId.DISEASE;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_ARMOR_BLESSING;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_PUPLE;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_TOPAZ;
import static l1j.server.server.model.skill.L1SkillId.EINHASAD_GRACE;
import static l1j.server.server.model.skill.L1SkillId.ELEMENTAL_FALL_DOWN;
import static l1j.server.server.model.skill.L1SkillId.ELEMENTAL_PROTECTION;
import static l1j.server.server.model.skill.L1SkillId.EMERALD_NO;
import static l1j.server.server.model.skill.L1SkillId.EMERALD_YES;
import static l1j.server.server.model.skill.L1SkillId.ERASE_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.EXP_BUFF;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION_Event;
import static l1j.server.server.model.skill.L1SkillId.FAFU_BUFF;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_A;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_B;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_C;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_D;
import static l1j.server.server.model.skill.L1SkillId.FOCUS_WAVE;
import static l1j.server.server.model.skill.L1SkillId.God_buff;
import static l1j.server.server.model.skill.L1SkillId.HERO_GAHO_BUFF;
import static l1j.server.server.model.skill.L1SkillId.HURRICANE;
import static l1j.server.server.model.skill.L1SkillId.INSIGHT;
import static l1j.server.server.model.skill.L1SkillId.LIFE_BLESSING;
import static l1j.server.server.model.skill.L1SkillId.MORTAL_BODY;
import static l1j.server.server.model.skill.L1SkillId.NATURES_TOUCH;
import static l1j.server.server.model.skill.L1SkillId.NEW_START_BLESSING;
import static l1j.server.server.model.skill.L1SkillId.PANIC;
import static l1j.server.server.model.skill.L1SkillId.POLLUTE_WATER;
import static l1j.server.server.model.skill.L1SkillId.POLY_RING_MASTER;
import static l1j.server.server.model.skill.L1SkillId.POLY_RING_MASTER2;
import static l1j.server.server.model.skill.L1SkillId.REDUCE_WEIGHT;
import static l1j.server.server.model.skill.L1SkillId.REDUCTION_ARMOR;
import static l1j.server.server.model.skill.L1SkillId.RESIST_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.RE_START_BLESSING;
import static l1j.server.server.model.skill.L1SkillId.RIND_BUFF;
import static l1j.server.server.model.skill.L1SkillId.SAND_STORM;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;
import static l1j.server.server.model.skill.L1SkillId.SIDE_OF_ME_BLESSING;
import static l1j.server.server.model.skill.L1SkillId.SILENCE;
import static l1j.server.server.model.skill.L1SkillId.SOUL_OF_FLAME;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL3;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL4;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL5;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL6;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CHAT_PROHIBITED;
import static l1j.server.server.model.skill.L1SkillId.STATUS_DRAGON_PEARL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FRUIT;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;
import static l1j.server.server.model.skill.L1SkillId.STRIKER_GALE;
import static l1j.server.server.model.skill.L1SkillId.SetBuff;
import static l1j.server.server.model.skill.L1SkillId.VENOM_RESIST;
import static l1j.server.server.model.skill.L1SkillId.WEAKNESS;
import static l1j.server.server.model.skill.L1SkillId.LEVEL_UP_BONUS; // "레벨업보너스" 翻譯為 "等級提升獎勵"
import static l1j.server.server.model.skill.L1SkillId.BLESS_OF_MOUNTAIN; // "정상의가호" 翻譯為 "山之祝福"

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.FatigueProperty;
import l1j.server.L1DatabaseFactory;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Info;
import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Loader;
import l1j.server.AinhasadSpecialStat2.L1AinhasadFaithUserLoader;
import l1j.server.CPMWBQSystem.CPMWBQSystemProvider;
import l1j.server.ClanBuffList.ClanBuffListLoader;
import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.MJAttendanceSystem.MJAttendanceRewardsHistory;
import l1j.server.MJBookQuestSystem.Loader.BQSCharacterDataLoader;
import l1j.server.MJDeathPenalty.MJDeathPenaltyService;
import l1j.server.MJDeathPenalty.Exp.MJDeathPenaltyexpDatabaseLoader;
import l1j.server.MJDeathPenalty.Item.MJDeathPenaltyItemDatabaseLoader;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeProgressLoader;
import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJKDASystem.MJKDALoader;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJPassiveSkill.MJPassiveUserLoader;
import l1j.server.MJPushitem.MJPushProvider;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJEPcStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RaceInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RacerInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RacerTicketT;
import l1j.server.MJTemplate.MJProto.MainServer_Client.AttendanceGroupType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_GROUP_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_INFO_EXTEND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_TAB_OPEN_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EXP_BOOSTING_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_POLYMORPH_EVENT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_USER_START_SUNDRY_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_LIST_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_POINT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_STAT_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_GameGate.SC_GAMEGATE_PCCAFE_CHARGE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.SC_INTER_RACING_READY_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.SC_INTER_RACING_START_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ADD_INVENTORY_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SMELTING_UPDATE_SLOT_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SmeltingResult;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOODPLEDGE_USER_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_ENTER_NOTICE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_System.CS_FREE_BUFF_SHIELD_INFO_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_System.SC_FREE_BUFF_SHIELD_UPDATE_NOTI;
import l1j.server.MJTemplate.MJProto.Mainserver_Client_Equip.SC_EXTEND_SLOT_INFO;
import l1j.server.MJTemplate.MJProto.resultCode.CS_PC_MASTER_INFO_REQ;
import l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_FAVOR_UPDATE_NOTI;
import l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_INFO_ACK;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;
import l1j.server.server.GMCommands;
import l1j.server.server.GameClient;
import l1j.server.server.GameServer;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.SkillCheck;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.server.datatables.CharacterCustomQuestTable;
import l1j.server.server.server.datatables.CharacterFreeShieldTable;
import l1j.server.server.server.datatables.CharacterSlotItemTable;
import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.server.datatables.ClanStorageTable;
import l1j.server.server.server.datatables.ExpTable;
import l1j.server.server.server.datatables.GetBackRestartTable;
import l1j.server.server.server.datatables.SkillsTable;
import l1j.server.server.server.datatables.SpamTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.CastleEffect;
import l1j.server.server.model.EventAlramTick;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.item.itemdelay.ItemDelayTimer;
import l1j.server.server.model.item.smelting.SmeltingScrollInfo;
import l1j.server.server.model.item.smelting.SmeltingScrollLoader;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_AddSkill;
import l1j.server.server.serverpackets.S_BookMarkLoad;
import l1j.server.server.serverpackets.S_ChangeCharName;
import l1j.server.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_CharacterConfig;
import l1j.server.server.serverpackets.S_ClanAttention;
import l1j.server.server.serverpackets.S_CollectionNoti;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ElfIcon;
import l1j.server.server.serverpackets.S_Emblem;
import l1j.server.server.serverpackets.S_FairlyConfig;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.server.serverpackets.S_Pc_Login;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SlotChange;
import l1j.server.server.serverpackets.S_SummonPack;
import l1j.server.server.server.serverpackets.S_SurvivalCry;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.server.serverpackets.S_UnityIcon;
import l1j.server.server.serverpackets.S_Unknown1;
import l1j.server.server.server.serverpackets.S_Weather;
import l1j.server.server.server.serverpackets.S_Weight;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1FreeShield;
import l1j.server.server.templates.L1GetBackRestart;
import l1j.server.server.templates.L1ItemBookMark;
import l1j.server.server.utils.CheckInitStat;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.SystemUtil;
import MJFX.UIAdapter.MJUIAdapter;
import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Object.MJShiftObject;
import MJShiftObject.Object.MJShiftObjectOneTimeToken;

public class C_LoginToServer extends ClientBasePacket {

	static class BuffInfo {
		public int skillId;
		public int remainTime;
		public int polyId;
	}

	private static final String C_LOGIN_TO_SERVER = "[C] C_LoginToServer";
	private static Logger _log = Logger.getLogger(C_LoginToServer.class.getName());

	private static void print_message(GameClient clnt, String message, boolean is_kick) throws Exception {
		System.out.println("─────────────────────────────────");
		System.out.println(message);
		System.out.println("─────────────────────────────────");
		if (is_kick) {
			clnt.kick();
			clnt.close();
		}
	}

	// TODO: 介入伺服器世界連接
	public static void do_direct_enter_world(String charName, GameClient client)
			throws FileNotFoundException, Exception {
		L1PcInstance pc = null;
		try {
			client.setLoginRecord(true);
			Calendar cal = Calendar.getInstance();
			int hour = Calendar.HOUR; // "시간" 翻譯為 "hour"
			int minute = Calendar.MINUTE; // "분" 翻譯為 "minute"
			String amPm = "PM"; // "오후" 翻譯為 "PM"
			if (cal.get(Calendar.AM_PM) == 0) {
				amPm = "AM"; // "오전" 翻譯為 "AM"
			}
			String login = client.getAccountName();
			int loginid = client.getAccount().getAccountId();
			pc = L1PcInstance.load(charName);
			Account account = Account.load(pc.getAccountName());
			if (account == null) {
				print_message(client, String.format("[介入: 帳號為 null %s]", charName), true);
				return;
			}

			if (client.getAccount() == null) {
				print_message(client, String.format("[介入: 帳號 Null 嘗試連接 %s]", charName), true);
				return;
			}

			if (client.getActiveChar() != null) {
				print_message(client, String.format("[介入: 因為重複使用相同 ID 連接，將強制終止 (%s) 的連接]", client.getIp()), true);
				return;
			}

			/** 防止雙角色錯誤 開始 */
			L1PcInstance OtherPc = L1World.getInstance().getPlayer(charName);
			if (OtherPc != null) {
				boolean isPrivateShop = OtherPc.isPrivateShop();
				GameServer.disconnectChar(OtherPc);
				OtherPc = null;

				if (isPrivateShop == false /* && isAutoCrown == false */) {
					print_message(client,
							String.format("[介入: 因為重複使用相同 ID 連接，將強制終止 (%s:%s) 的連接.2]", client.getIp(), charName), true);
					return;
				}
			}

			Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();// 原始版本
			for (L1PcInstance bugpc : pcs) {
				if (bugpc.getAccountName().equals(client.getAccountName())) {
					if (!bugpc.isPrivateShop() || bugpc.getNetConnection() != null) {
						print_message(client,
								String.format("[介入: 因為重複使用相同帳號連接，將強制終止 (%s) 的連接]", client.getIp()), true);
						GameServer.disconnectChar(bugpc);
					}
				}
			}
			pcs = null;
			/** 防止雙角色錯誤 結束 */

			if ((pc == null) || !login.equals(pc.getAccountName())) {
				print_message(client,
						String.format("[介入: 嘗試連接當前帳號中不存在的角色: %s 帳號: %s]", charName, client.getAccountName()), true);
				return;
			}

			if (!pc.isGm() && Config.Login.LevelDownRange != 0) {
				if (pc.getHighLevel() - pc.getLevel() >= Config.Login.LevelDownRange) {
					print_message(client,
							String.format("[介入: 超出允許的降級範圍: %s 帳號: %s 主機: %s]", charName, login, client.getIp()), true);
					return;
				}
			}

			/** 防止防具錯誤的代碼 **/
			if (pc.getAC().getAc() < -Config.CharSettings.aclevel) {
				Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
				client.kick();
				client.close();
				System.out.println("▶ 防具錯誤驅逐: " + pc.getName());
				return;
			}

			/** 防止等級錯誤的代碼 **/
			if (pc.getLevel() > pc.getHighLevel()) {
				client.kick();
				client.close();
				System.out.println("▶ 驅逐等級錯誤: " + pc.getName());
				return;
			}
			if (pc.getLevel() >= Config.CharSettings.LimitLevel && !pc.isGm()) {
				Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
				pc.sendPackets(String.valueOf(new S_SystemMessage(pc.getName() + " 的帳號已被凍結。")));
				pc.sendPackets(new S_Disconnect());

				if (pc.getOnlineStatus() == 1) {
					pc.sendPackets(new S_Disconnect());
				}
				client.kick();
				client.close();
				System.out.println("▶ 重新連接導致 [凍結] 的等級錯誤: " + pc.getName());
				return;
			}

			if (pc.getType() < 0 || pc.getType() > 9) {
				client.kick();
				client.close();
				System.out.println("▶ 嘗試使用已刪除角色連接. 驅逐: " + pc.getName());
			}

			if (pc.isAutoShop()) {
				pc.setAutoShop(false);
			}

			System.out.println(String.format("[介入:連接]:[帳號:%s] [角色:%s] [%s:%d:%d分鐘] [IP:%s] [記憶體:%d]", login, charName,
					getAmPm(), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), client.getIp(), SystemUtil.getUsedMemoryMB()));
			LoggerInstance.getInstance()
					.addConnection("介入: 連接 角色=" + charName + "帳號=" + login + "IP=" + client.getHostname());

			MJMyRepresentativeService.service().updateRepresentativeCharacter(pc.getAccountName(), pc.getName());

			pc.set_instance_status(MJEPcStatus.WORLD);
			pc.setOnlineStatus(1);
			pc.create_captcha();
			CharacterTable.updateOnlineStatus(pc);
			L1World.getInstance().storeObject(pc);

			pc.setNetConnection(client);
			client.setActiveChar(pc);

			pc.sendPackets(new S_Unknown1(pc));

			if (Config.Login.CharacterConfigInServerSide) {
				pc.sendPackets(String.valueOf(new S_CharacterConfig(pc.getId())));
			}

			pc.createTimeCollection(); // 設置展覽
			AinhasadFaithLoad(pc);

			// XXX Ein Point 更新版本
			AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
			if (Info == null) {
				AinhasadSpecialStatLoader.getInstance().addSpecialStat(pc.getId(), pc.getName());
			}
			SC_EINHASAD_POINT_STAT_INFO_NOTI.send_point(pc, Info);// send(pc, Info);

			// XXX Ein Point 更新前版本
//			AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
//			if (Info != null) {
//				SC_EINHASAD_POINT_STAT_INFO_NOTI.send_point(pc, Info.get_bless(), Info.get_lucky(), Info.get_vital(), Info.get_total_stat(), 0, 0);
//			} else {
//				AinhasadSpecialStatLoader.getInstance().addSpecialStat(pc.getId(), pc.getName());
//				SC_EINHASAD_POINT_STAT_INFO_NOTI.send_point(pc, 0, 0, 0, 0, 0, 0);
//			}
			loadSkills(pc);
			MJPassiveUserLoader.load(pc);

			CharacterSlotItemTable.getInstance().selectCharSlot(pc, 0); // 第1個槽位
			CharacterSlotItemTable.getInstance().selectCharSlot(pc, 1); // 第2個槽位
			CharacterSlotItemTable.getInstance().selectCharSlot(pc, 2); // 第3個槽位
			CharacterSlotItemTable.getInstance().selectCharSlot(pc, 3); // 第4個槽位
			CharacterSlotItemTable.getInstance().selectCharSlotcolor(pc);
			MJCopyMapObservable.getInstance().resetPosition(pc);
			L1BookMark.bookmarkDB(pc);
			getItemBookMark(pc);
			pc.sendPackets(new S_BookMarkLoad(pc));
			// 加載藥水消耗量
			pc.sendPackets(String.valueOf(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats())));

			GetBackRestartTable gbrTable = GetBackRestartTable.getInstance();
			L1GetBackRestart[] gbrList = gbrTable.getGetBackRestartTableList();
			for (L1GetBackRestart gbr : gbrList) {
				if (pc.getMapId() == gbr.getArea()) {
					pc.setX(gbr.getLocX());
					pc.setY(gbr.getLocY());
					pc.setMap(gbr.getMapId());
					break;
				}
			}

			MJCopyMapObservable.getInstance().resetPosition(pc);
			MJRaidSpace.getInstance().getBackPc(pc);
			DungeonTimeProgressLoader.load(pc);

			/** 2016.11.26 MJ 應用中心 LFC **/
			MJInstanceSpace.getInstance().getBackPc(pc);

			if (Config.ServerAdSetting.GETBACKREST) {
				int[] loc = Getback.GetBack_Location(pc, true);
				pc.setX(loc[0]);
				pc.setY(loc[1]);
				pc.setMap((short) loc[2]);
			}

			// 如果在戰爭中的情況下，不是城主血盟成員的話將被傳送回去。
			int castle_id = L1CastleLocation.getCastleIdByArea(pc);
			if (pc.getMapId() == 66) {
				castle_id = 6;
			}
			if (0 < castle_id) {
				if (MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
					L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
					if (clan != null && clan.getCastleId() != castle_id) {
						int[] loc = new int[3];
						loc = L1CastleLocation.getGetBackLoc(castle_id);
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
						loc = null;
					} else if (pc.getMapId() == 4) {
						int[] loc = new int[3];
						loc = L1CastleLocation.getGetBackLoc(castle_id);
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
						loc = null;
					}
				}
			}

			L1Map l1map = pc.getMap();
			if (l1map == null || !l1map.isInMap(pc.getX(), pc.getY())) {
				MJCopyMapObservable.getInstance().resetAlwaysPositon(pc);
			}

			pc.sendPackets(String.valueOf(S_CollectionNoti.LOGIN_START)); // 加載展覽

			pc.beginGameTimeCarrier();
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
			MJExpAmplifierLoader.getInstance().set(pc);
			pc.sendPackets(String.valueOf(new S_Weather(L1World.getInstance().getWeather())));
			pc.sendPackets(String.valueOf(new S_ReturnedStat(S_ReturnedStat.Unknown_LOGIN2, 0, 0)));
			pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
			pc.load_private_probability();
			L1World.getInstance().addVisibleObject(pc);
			pc.createFavorBookInventory(); // 聖物庫存
			// 從這裡開始




			List<BuffInfo> buffList = loadBuff(pc);
			processBuff(pc, buffList);
			PcBuffCheck(pc); // 網咖增益
			FreeShieldLoad(loginid, login);
			loadItems(pc, false);
			pc.sendPackets(String.valueOf(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RUNE, 1)));
			loadItems(pc, true);
			
			for (Iterator<L1ItemInstance> partner = pc.getInventory().getItems().iterator(); partner.hasNext();) {
				L1ItemInstance item = (L1ItemInstance) partner.next();
				if (item.getItemId() == 700024)
					pc.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK, item.getItemId(), "$13719",
							L1BookMark.ShowBookmarkitem(pc, item.getItemId())));
				if (item.getItemId() == 700025) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK, item.getId(), "$13719",
							L1BookMark.ShowBookmarkitem(pc, item.getItemId())));
				}
			}
			pc.sendPackets(new S_PacketBox(S_PacketBox.INIT_DODGE, 0x0000));
			pc.sendPackets(new S_PacketBox(S_PacketBox.DODGE, 0));
			pc.getLight().turnOnOffLight();
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_PacketBox(S_PacketBox.INVEN_SAVE)); // 位置變更
			pc.sendPackets(new S_SPMR(pc));
			pc.on_regeneration();
			pc.startObjectAutoUpdate();
			pc.beginExpMonitor();

			if (Config.Login.UseShiftServer) {
				if (client.is_shift_transfer()) {
					final L1PcInstance t = pc;
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							t.start_teleportForGM(32738, 32872, 2236, t.getHeading(), 18339, true, true);
							t.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
									"\f3請在聊天窗口中輸入您在當前伺服器上使用的登錄帳戶名。"));
							t.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3密碼將繼續使用以前的伺服器密碼。"));
							t.sendPackets("\\aH請在聊天窗口中輸入您在當前伺服器上使用的登錄帳戶名。");
							t.sendPackets("\\aH密碼將繼續使用以前的伺服器密碼。");
						}
					}, 1000L);
				} else if (client.is_shift_battle()) {
					final L1PcInstance t = pc;
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							MJShiftObjectManager.getInstance().do_enter_battle_character(t);
						}
					}, 500L);
				}
			}
			if (pc.isPcBuff()) {
				SC_PC_MASTER_FAVOR_UPDATE_NOTI.newInstance().send(pc);
//				pc.sendPackets(new S_Pc_Login(pc)); // 無法找到在網咖時收到的封包協議
				SC_USER_START_SUNDRY_NOTI.send(pc, pc.isPcBuff());
				SC_GAMEGATE_PCCAFE_CHARGE_NOTI.send(pc, pc.isPcBuff());
//				CS_PC_MASTER_INFO_REQ.newInstance();
//				SC_PC_MASTER_FAVOR_UPDATE_NOTI.newInstance().send(pc);
			}
			PcReset(pc);
			
//			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.UI4));
			pc.sendPackets(String.valueOf(new S_ReturnedStat(pc, S_ReturnedStat.UI5)));
			pc.sendVisualEffectAtLogin(); // 顯示皇冠、毒、潛水等視覺效果
			pc.sendPackets(new S_PacketBox(S_PacketBox.LOGIN_UNKNOWN3, 1));
			pc.sendPackets(String.valueOf(new S_ReturnedStat(pc, S_ReturnedStat.LOGIN)));
			pc.sendClanMarks(); // 顯示聖血君主的皇冠標記
			client.setStatus(MJClientStatus.CLNT_STS_ENTERWORLD);

			pc.setCurrentHp(pc.getCurrentHpDB());
			pc.setCurrentMp(pc.getCurrentMpDB());

			L1PcInstance jonje = L1World.getInstance().getPlayer(pc.getName());
			if (jonje == null) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("存在漏洞強制終止！請重新連接")));
				client.kick();
				return;
			}
			if (pc.getCurrentHp() > 0) {
				pc.setDead(false);
				pc.setStatus(0);
			} else {
				pc.setDead(true);
				pc.setStatus(ActionCodes.ACTION_Die);
			}

			serchSummon(pc);
			MJCastleWarBusiness.getInstance().viewNowCastleWarState(pc);
			Clan_Check(pc);
			loadNBuff(pc);
			L1SkillId.recycleTam(pc);
			pc.setSkillEffect(SetBuff, 30 * 1000);

			// 艾因哈薩德
			if (pc.getLevel() > 5) {
				int einhasad = pc.getAccount().getBlessOfAin() + (pc.getLastLoginTime() == null ? 0
						: ((int) (System.currentTimeMillis() - pc.getLastLoginTime().getTime()) / (15 * 60 * 1000)));
				einhasad = Math.min(SC_REST_EXP_INFO_NOTI.EINHASAD_LIMIT, einhasad);
				pc.getAccount().setBlessOfAin(einhasad, pc);

				if (pc.getZoneType() == 1) {
					pc.startEinhasadTimer();
				}
				if (einhasad > 10000) {
					SC_REST_EXP_INFO_NOTI.send(pc);
				}
			}
			SC_EXP_BOOSTING_INFO_NOTI.send(pc);

			/** 載入封鎖列表 **/
			L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(pc.getId());
			if (exList != null) {
				setExcludeList(pc, exList);
			}
			pc.RenewStat();
			pc.sendPackets(new S_Weight(pc));
			MJUIAdapter.on_login_user(client, pc);
			CharacterCustomQuestTable.load(pc);

			// 3.63 物品封包處理
			pc.isWorld = true;
			L1ItemInstance temp = null;
			try {
				// 為了使裝備的物品正常顯示在槽位中，臨時進行了此操作。
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					temp = item;
					if (item.isEquipped())
						pc.getInventory().toSlotPacket(pc, item, true);

				}
			} catch (Exception e) {
				System.out.println("錯誤 懷疑的物品是 ->> " + temp.getItem().getName());
			}
			Clanclan(pc);
			pc.sendPackets(new S_FairlyConfig(pc));
			safetyzone(pc);
			if (pc.getHellTime() > 0) {
				pc.beginHell(false);
			}
			pc.sendPackets(new S_LetterList(pc, 0, 40), true);
			pc.sendPackets(new S_LetterList(pc, 1, 80), true);
			pc.sendPackets(new S_LetterList(pc, 2, 10), true);

			if (CheckMail(pc) > 0) {
				pc.sendPackets(new S_SkillSound(pc.getId(), 1091));
				pc.sendPackets(new S_ServerMessage(428)); // 您有新的信件。
			}
			pc.checkStatus();
			if (!CheckInitStat.CheckPcStat(pc)) {
				client.kick();
				return;
			}
			pc.sendPackets(new S_Karma(pc));
			pc.sendPackets(new S_SlotChange(S_SlotChange.SLOT_CHANGE, pc));
			pc.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(pc, 0, false));
//			SC_NOTIFICATION_INFO_NOTI.onEventTick(pc, 3000L);
//			Start_Event_Alram(pc);

			BQSCharacterDataLoader.in(pc);
			CPMWBQSystemProvider.provider().BQload(pc);
			MJPushProvider.provider().userLoading(pc);
/*			if (MJDeathPenaltyService.service().use()) {
				MJDeathPenaltyexpDatabaseLoader.getInstance().do_Select(pc);
				MJDeathPenaltyItemDatabaseLoader.getInstance().do_Select(pc);
			}*/

			pc.load_lateral_status();
			if (MJAttendanceLoadManager.ATTEN_IS_RUNNING) {
				if (!client.is_shift_battle()) {
					SC_ATTENDANCE_BONUS_INFO_EXTEND.send(pc);
					SC_ATTENDANCE_BONUS_GROUP_INFO.send(pc);
					SC_ATTENDANCE_USER_DATA_EXTEND.send(pc);
					SC_ATTENDANCE_BONUS_GROUP_INFO.openinfo(pc);
					MJAttendanceRewardsHistory.send_history(pc);
					if (account.getAttendance_Premium()) {
						pc.sendPackets(
								SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(2), 0),
								true);
						pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(2), 0),
								true);
					}
					if (account.getAttendance_Special()) {
						pc.sendPackets(
								SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(3), 0),
								true);
						pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(3), 0),
								true);
					}
					if (account.getAttendance_Brave_Warrior()) {
						pc.sendPackets(
								SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(4), 1),
								true);
						pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(4), 1),
								true);
					}
					if (account.getAttendance_Aden_World()) {
						pc.sendPackets(
								SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(5), 1),
								true);
						pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(5), 1),
								true);
					}
					if (account.getAttendance_Bravery_Medal()) {
						pc.sendPackets(
								SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(6), 1),
								true);
						pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(6), 1),
								true);
					}
					SC_ATTENDANCE_INFO_NOTI.send1(pc);
					SC_ATTENDANCE_INFO_NOTI.send2(pc);

				}
			}

			Buff_Individual(pc);
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			on_fatigue(pc);
			pc.special_resistance_skill(pc, 0, 0, true);
			SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
			ClanBuffListLoader.getInstance().load_clan_buff(pc);
//			PcBuffCheck(pc); // 網吧增益

			/** 凱撒訓練場 **/
			if (pc.getMap().getBaseMapId() == 1400) {
				pc.start_teleport(33491, 32762, 4, 0, 18339, false, false);
			}
			/** 精靈之墓 **/
			if (pc.getMapId() == 430) {
				pc.start_teleport(32779, 32831, 622, 0, 18339, false, false);
			}

			// 冰洞地圖
			if (pc.getMapId() >= 2101 && pc.getMapId() <= 2151 || pc.getMapId() >= 2151 && pc.getMapId() <= 2201) {
				pc.start_teleport(33442, 32809, 4, 5, 18339, false, false);
			}

			// 刪除火焰棒/神秘恢復藥水。
			if (!(pc.getMapId() >= 2101 && pc.getMapId() <= 2151 || pc.getMapId() >= 2151 && pc.getMapId() <= 2201)) {
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					if (item.getItemId() == 30055 || item.getItemId() == 30056) {
						if (item != null) {
							pc.getInventory().removeItem(item, item.getCount());
						}
					}
				}
			}

			if (pc.getReturnStat() != 0) {
				resetStats(pc);
			} else {
				pc.sendBonusStats();
			}

			// TODO 用戶連接通知（管理員用）
			for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
				if (player.isGm()) {
					player.sendPackets("\\aG- [\\aA連接\\aG] " + pc.getName() + " " + client.getAccountName() + " "
							+ client.getIp() + "");
				}
			}

			Contact_Notice(pc);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("◆(登入)連接失敗◆ : 請將上述錯誤截圖。");
		}
	}

	// TODO 一般伺服器世界連接
	public static L1PcInstance doEnterWorld(String charName, GameClient client, boolean is_getback, int getback_x,
			int getback_y, int getback_mapid) throws FileNotFoundException, Exception {
		L1PcInstance pc = null;
		boolean is_returner = false;
		try {
//			System.out.println("連接確認 1");
			if (client.is_shift_battle()) {
				MJShiftObject sobject = client.get_shift_object();
				if (sobject != null) {
					charName = sobject.get_destination_character_name();
					is_returner = true;
				}
			}
			if (Config.Login.UseShiftServer) {
				MJShiftObject sobject = MJShiftObjectManager.getInstance()
						.get_shift_sender_object_from_account(client.getAccountName());
				if (sobject != null) {
					if (!sobject.get_source_character_name().equals(charName)) {
						System.out.println(String.format("【帳號】%s【參加中】%s【登入】%s【%s】【IP】%s 嘗試用其他角色登入參加大戰的帳號。",
								client.getAccountName(), sobject.get_source_character_name(), charName,
								MJNSHandler.getLocalTime(), client.getIp()));
						SC_CUSTOM_MSGBOX.do_kick(client, String.format("%s正在參加大戰，因此不允許其他角色登入。",
								sobject.get_source_character_name()));
						return null;
					}
				}
			}
//			System.out.println("連接確認 2");
			client.setLoginRecord(true);
			Calendar calendar = Calendar.getInstance();
			int hour = Calendar.HOUR;
			int minute = Calendar.MINUTE;
			/** 0 上午 , 1 下午 * */
			String period = "下午";
			if (calendar.get(Calendar.AM_PM) == 0) {
				period = "上午";
			}
			String login = client.getAccountName();
			int loginid = client.getAccount().getAccountId();
//			System.out.println("帳號ID: " + loginid);
			if (GMCommands.isCharacterBlock(charName)) {
				System.out.println("─────────────────────────────────");
				System.out.println(String.format("角色扣押中的角色 [%s] 嘗試連接。", charName));
				System.out.println("─────────────────────────────────");
				client.sendPacket(new S_LoginResult(52));
				return null;
			}

			pc = L1PcInstance.load(charName);
			Account account = null;
			if (pc == null) {
				print_message(client, String.format("找不到角色（世界連接）：%s(%s)", charName, client.getIp()), true);
				return null;
			}
//			System.out.println("連接確認 3");
			if (pc.getAccountName() != null) {
				account = Account.load(pc.getAccountName());// 錯誤附近
			} else {
				System.out.println("─────────────────────────────────");
				System.out.println("pc.getAccountName  Null  " + charName);
				System.out.println("─────────────────────────────────");
				client.kick();
				client.close();
				return null;
			}
			if (account == null) {
				System.out.println("─────────────────────────────────");
				System.out.println("account Null  " + charName);
				System.out.println("─────────────────────────────────");
				client.kick();
				client.close();
				return null;
			}

			if (client.getAccount() == null) {
				System.out.println("─────────────────────────────────");
				System.out.println("帳號 Null 連接嘗試 " + charName);
				System.out.println("─────────────────────────────────");
				client.kick();
				client.close();
				return null;
			}

			if (client.getActiveChar() != null) {
				System.out.println("─────────────────────────────────");
				System.out.println("由於同一ID的重複連接，強制終止 (" + client.getIp() + ") 的連接。");
				System.out.println("─────────────────────────────────");
				client.close();
				return null;
			}

			if (!client.is_shift_client()) {
				GameClient clientByAccount = LoginController.getInstance().getClientByAccount(login);
				if (clientByAccount == null || clientByAccount != client) {
					System.out.println(clientByAccount);
					System.out.println(client);
					System.out.println("─────────────────────────────────");
					System.out.println("由於同一帳號的重複連接，強制終止 (" + client.getIp() + ") 的連接。");
					System.out.println("─────────────────────────────────");
					client.close();
					return null;
				}
			}
//            System.out.println("連接確認 4");
			/** 防止2角色錯誤開始 */
			L1PcInstance OtherPc = L1World.getInstance().getPlayer(charName);
			if (OtherPc != null) {
				boolean isPrivateShop = OtherPc.isPrivateShop();
				GameServer.disconnectChar(OtherPc);
				OtherPc = null;

				if (isPrivateShop == false /* && isAutoCrown == false */) {
					print_message(client,
							String.format("由於同一ID的重複連接，強制終止 (%s:%s) 的連接。2", client.getIp(), charName), true);
					return null;
				}
			}

			Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();// 원본
			for (L1PcInstance bugpc : pcs) {
				if (bugpc.getAccountName().equals(client.getAccountName())) {
					if (!bugpc.isPrivateShop() || bugpc.getNetConnection() != null) {
						print_message(client, String.format("由於同一帳號的重複連接，強制終止 (%s) 的連接。", client.getIp()), true);
						GameServer.disconnectChar(bugpc);
					}
				}
			}
			pcs = null;
			/** 防止2角色錯誤結束 */

			if ((pc == null) || !login.equals(pc.getAccountName())) {
				System.out.println("─────────────────────────────────");
				System.out.println("嘗試連接目前帳號中不存在的角色: " + charName + " 帳號: " + client.getAccountName());
				System.out.println("─────────────────────────────────");
				client.kick();
				client.close();
				return null;
			}

			if (!pc.isGm() && Config.Login.LevelDownRange != 0) {
				if (pc.getHighLevel() - pc.getLevel() >= Config.Login.LevelDownRange) {
					System.out.println("─────────────────────────────────");
					_log.info("超出降級允許範圍: " + charName + " 帳號= " + login + " host= " + client.getIp());
					System.out.println("超出降級允許範圍: " + charName + " 帳號= " + login + " host= " + client.getIp());
					System.out.println("─────────────────────────────────");
					client.kick();
					return null;
				}
			}
			/** 防具錯誤防止源碼 **/
			if (pc.getAC().getAc() < -Config.CharSettings.aclevel) {
				Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
				client.kick();
				client.close();
				System.out.println("▶ 防具錯誤驅逐: " + pc.getName());
				return null;
			}
//            System.out.println("連接確認 5");
			/** 等級錯誤防止源碼 **/
			if (pc.getLevel() > pc.getHighLevel()) {
				client.kick();
				client.close();
				System.out.println("▶ 等級錯誤驅逐: " + pc.getName());
				return null;
			}
			if (pc.getLevel() >= Config.CharSettings.LimitLevel && !pc.isGm()) {
				Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
				pc.sendPackets(String.valueOf(new S_SystemMessage(pc.getName() + " 已被帳號凍結。")));
				pc.sendPackets(new S_Disconnect());

				if (pc.getOnlineStatus() == 1) {
					pc.sendPackets(new S_Disconnect());
				}
				client.kick();
				client.close();
				System.out.println("▶ 配置等級錯誤重新連接後 [凍結]: " + pc.getName());
				return null;
			}

			if (pc.getType() < 0 || pc.getType() > 9) {
				client.kick();
				client.close();
				System.out.println("▶ 以刪除請求的角色登入。驅逐: " + pc.getName());
			}

			if (pc.isAutonomousShop()) {
				pc.setAutonomousShop(false);
			}

			if (!is_getback) {
				pc.setX(getback_x);
				pc.setY(getback_y);
				pc.setMap((short) getback_mapid);
			}

//            System.out.println("連接確認 6");
			System.out.println(String.format("[普通:連接] [帳號:%s] [角色:%s] [%s:%d:%d分] [IP:%s] [記憶體:%d]", login, charName,
					amPm, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), client.getIp(), SystemUtil.getUsedMemoryMB()));
			LoggerInstance.getInstance()
					.addConnection("連接角色=" + charName + " 帳號=" + login + " IP=" + client.getHostname());

			MJMyRepresentativeService.service().updateRepresentativeCharacter(pc.getAccountName(), pc.getName());

			pc.set_instance_status(MJEPcStatus.WORLD);
			pc.setOnlineStatus(1);
			pc.create_captcha();
			CharacterTable.updateOnlineStatus(pc);
			L1World.getInstance().storeObject(pc);
			pc.setNetConnection(client);
			client.setActiveChar(pc);
			pc.sendPackets(new S_Unknown1(pc));

			if (Config.Login.CharacterConfigInServerSide) {
				pc.sendPackets(new S_CharacterConfig(pc.getId()));
			}

			pc.createTimeCollection(); // 展示會設置
			AinhasadFaithLoad(pc);

			// XXX 安因點數更新版本
			AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
			if (Info == null) {
				AinhasadSpecialStatLoader.getInstance().addSpecialStat(pc.getId(), pc.getName());
			}
			// SC_EINHASAD_POINT_STAT_INFO_NOTI.send(pc, Info);
			SC_EINHASAD_POINT_STAT_INFO_NOTI.send_point(pc, Info);

			// XXX 安因點數更新前版本
//			AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
//			if (Info != null) {
//				SC_EINHASAD_POINT_STAT_INFO_NOTI.send_point(pc, Info.get_bless(), Info.get_lucky(), Info.get_vital(), Info.get_total_stat(), 0, 0);
//			} else {
//				AinhasadSpecialStatLoader.getInstance().addSpecialStat(pc.getId(), pc.getName());
//				SC_EINHASAD_POINT_STAT_INFO_NOTI.send_point(pc, 0, 0, 0, 0, 0, 0);
//			}

			loadSkills(pc);
			MJPassiveUserLoader.load(pc);

			CharacterSlotItemTable.getInstance().selectCharSlot(pc, 0); // 1번슬롯
			CharacterSlotItemTable.getInstance().selectCharSlot(pc, 1); // 2번슬롯
			CharacterSlotItemTable.getInstance().selectCharSlot(pc, 2); // 3번슬롯
			CharacterSlotItemTable.getInstance().selectCharSlot(pc, 3); // 4번슬롯
			CharacterSlotItemTable.getInstance().selectCharSlotcolor(pc);
			MJCopyMapObservable.getInstance().resetPosition(pc);
			L1BookMark.bookmarkDB(pc);
			getItemBookMark(pc);
			pc.sendPackets(new S_BookMarkLoad(pc));
			// 讀取藥水攝取量
			pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));

			if (is_getback) {
				GetBackRestartTable gbrTable = GetBackRestartTable.getInstance();
				L1GetBackRestart[] gbrList = gbrTable.getGetBackRestartTableList();
				for (L1GetBackRestart gbr : gbrList) {
					if (pc.getMapId() == gbr.getArea()) {
						pc.setX(gbr.getLocX());
						pc.setY(gbr.getLocY());
						pc.setMap(gbr.getMapId());
						break;
					}
				}
			}
//			System.out.println("連接確認 7");
			if (is_getback) {
				MJCopyMapObservable.getInstance().resetPosition(pc);
				MJRaidSpace.getInstance().getBackPc(pc);
			}
			DungeonTimeProgressLoader.load(pc);

			if (is_getback) {
				/** 2016.11.26 MJ 應用中心 LFC **/
				MJInstanceSpace.getInstance().getBackPc(pc);
				/** 2016.11.26 MJ 應用中心 LFC **/
			}
			if (is_getback && Config.ServerAdSetting.GETBACKREST) {
				int[] loc = Getback.GetBack_Location(pc, true);
				pc.setX(loc[0]);
				pc.setY(loc[1]);
				pc.setMap((short) loc[2]);
			}

			if (pc.getMapId() >= 732 && pc.getMapId() <= 776) {
				pc.setX(33443);
				pc.setY(32797);
				pc.setMap((short) 4);
				for (int i = 420100; i <= 420111; i++) {
					if (!pc.getInventory().checkItem(i)) {
						continue;
					}
					pc.getInventory().consumeItem(i);
				}
			}

			// 在戰爭中，如果在機艙內，將非城主血盟成員遣返.
			int castle_id = L1CastleLocation.getCastleIdByArea(pc);
			if (pc.getMapId() == 66) {
				castle_id = 6;
			}
			if (0 < castle_id) {
				if (MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
					L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
					if (clan != null && clan.getCastleId() != castle_id) {
						int[] loc = new int[3];
						loc = L1CastleLocation.getGetBackLoc(castle_id);
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
						loc = null;
					} else if (pc.getMapId() == 4) {
						int[] loc = new int[3];
						loc = L1CastleLocation.getGetBackLoc(castle_id);
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
						loc = null;
					}
				}
			}
//			System.out.println("連接確認 8");
			L1Map l1map = pc.getMap();
			if (l1map == null || !l1map.isInMap(pc.getX(), pc.getY())) {
				MJCopyMapObservable.getInstance().resetAlwaysPositon(pc);
			}
//			System.out.println("連接確認 8-1");
			pc.sendPackets(S_CollectionNoti.LOGIN_START);
			pc.beginGameTimeCarrier();
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
			MJExpAmplifierLoader.getInstance().set(pc);
			pc.sendPackets(new S_Weather(L1World.getInstance().getWeather()));
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.Unknown_LOGIN2, 0, 0));
			pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
			pc.load_private_probability();
			L1World.getInstance().addVisibleObject(pc);
			pc.createFavorBookInventory();// 聖物庫存

			// 從這裡開始

//            System.out.println("連接確認 8-2");

			List<BuffInfo> buffList = loadBuff(pc);
//            System.out.println("連接確認 8-3");
			processBuff(pc, buffList);
//            System.out.println("連接確認 8-4");
			PcBuffCheck(pc); // 網吧Buff
			FreeShieldLoad(loginid, login);
//            System.out.println("連接確認 8-5");
			loadItems(pc, false);
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RUNE, 1));
			loadItems(pc, true);
//			System.out.println("連接確認 8-4");
			for (Iterator<L1ItemInstance> partner = pc.getInventory().getItems().iterator(); partner.hasNext();) {
				L1ItemInstance item = (L1ItemInstance) partner.next();
				if (item.getItemId() == 700024)
					pc.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK, item.getItemId(), "$13719",
							L1BookMark.ShowBookmarkitem(pc, item.getItemId())));
				if (item.getItemId() == 700025) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK, item.getId(), "$13719",
							L1BookMark.ShowBookmarkitem(pc, item.getItemId())));
				}
			}
//			System.out.println("連接確認 8-5");
			pc.sendPackets(new S_PacketBox(S_PacketBox.INIT_DODGE, 0x0000));
			pc.sendPackets(new S_PacketBox(S_PacketBox.DODGE, 0));
			pc.getLight().turnOnOffLight();
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_PacketBox(S_PacketBox.INVENTORY_SAVE)); // 位置變更
			pc.sendPackets(new S_SPMR(pc));
			pc.on_regeneration();
			pc.startObjectAutoUpdate();
			pc.beginExpMonitor();
//            System.out.println("連接確認 8-3");
			if (pc.isPcBuff()) {
				SC_PC_MASTER_FAVOR_UPDATE_NOTI.newInstance().send(pc);
//                pc.sendPackets(new S_Pc_Login(pc)); // 無法找到網吧Buff時發送的封包協議
				SC_USER_START_SUNDRY_NOTI.send(pc, pc.isPcBuff());
				SC_GAMEGATE_PCCAFE_CHARGE_NOTI.send(pc, pc.isPcBuff());
//				CS_PC_MASTER_INFO_REQ.newInstance();
//				SC_PC_MASTER_FAVOR_UPDATE_NOTI.newInstance().send(pc);
			}
//			System.out.println("連接確認 9");
			PcReset(pc);
//			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.UI4));
			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.UI5));
			pc.sendVisualEffectAtLogin(); // 顯示皇冠、中毒、水中等的視覺效果
			pc.sendPackets(new S_PacketBox(S_PacketBox.LOGIN_UNKNOWN3, 1));
			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LOGIN));
			pc.sendClanMarks(); // 聖血君主皇冠顯示
			client.setStatus(MJClientStatus.CLNT_STS_ENTERWORLD);
			pc.setCurrentHp(pc.getCurrentHpDB());
			pc.setCurrentMp(pc.getCurrentMpDB());

			L1PcInstance jonje = L1World.getInstance().getPlayer(pc.getName());
			if (jonje == null) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("存在Bug強制關閉！請重新連接")));
				client.kick();
				return null;
			}
			if (pc.getCurrentHp() > 0) {
				pc.setDead(false);
				pc.setStatus(0);
			} else {
				pc.setDead(true);
				pc.setStatus(ActionCodes.ACTION_Die);
			}

			serchSummon(pc);
			MJCastleWarBusiness.getInstance().viewNowCastleWarState(pc);
			Clan_Check(pc);
			loadNBuff(pc);
			L1SkillId.recycleTam(pc);
			pc.setSkillEffect(SetBuff, 30 * 1000);
			//System.out.println("連接確認 10");
			// 安塔拉斯
			if (pc.getLevel() > 5) {
				int einhasad = pc.getAccount().getBlessOfAin() + (pc.getLastLoginTime() == null ? 0
						: ((int) (System.currentTimeMillis() - pc.getLastLoginTime().getTime()) / (15 * 60 * 1000)));
				einhasad = Math.min(SC_REST_EXP_INFO_NOTI.EINHASAD_LIMIT, einhasad);
				pc.getAccount().setBlessOfAin(einhasad, pc);

				if (pc.getZoneType() == 1) {
					pc.startEinhasadTimer();
				}
				if (einhasad > 10000) {
					SC_REST_EXP_INFO_NOTI.send(pc);
				}
			}
			SC_EXP_BOOSTING_INFO_NOTI.send(pc);

			/** 載入屏蔽名單 **/
			L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(pc.getId());
			if (exList != null) {
				setExcludeList(pc, exList);
			}
			pc.RenewStat();
			pc.sendPackets(new S_Weight(pc));
			MJUIAdapter.on_login_user(client, pc);
			CharacterCustomQuestTable.load(pc);
			//System.out.println("連接確認 11");
			// 3.63 道具封包處理
			pc.isWorld = true;
			L1ItemInstance temp = null;
			try {
				// 為了使裝備的物品能正常顯示在槽位中，進行的臨時作業。
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					temp = item;
					if (item.isEquipped())
						pc.getInventory().toSlotPacket(pc, item, true);

				}
			} catch (Exception e) {
				System.out.println("發生錯誤，疑似問題的物品是 ->> " + temp.getItem().getName());
			}
			Clanclan(pc);
			pc.sendPackets(new S_FairlyConfig(pc));
			safetyzone(pc);
//			System.out.println("連接確認 12");
			if (pc.getHellTime() > 0) {
				pc.beginHell(false);
			}
			pc.sendPackets(new S_LetterList(pc, 0, 40), true);
			pc.sendPackets(new S_LetterList(pc, 1, 80), true);
			pc.sendPackets(new S_LetterList(pc, 2, 10), true);

			if (CheckMail(pc) > 0) {
				pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 1091)));
				pc.sendPackets(String.valueOf(new S_ServerMessage(428))); // 편지가 도착했습니다.
			}
			pc.checkStatus();
			if (!CheckInitStat.CheckPcStat(pc)) {
				client.kick();
				return null;
			}
			pc.sendPackets(new S_Karma(pc));
			pc.sendPackets(new S_SlotChange(S_SlotChange.SLOT_CHANGE, pc));
			pc.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(pc, 0, false));
//			SC_NOTIFICATION_INFO_NOTI.onEventTick(pc, 3000L);
//			Start_Event_Alram(pc);

//			System.out.println("連接確認 13");
			BQSCharacterDataLoader.in(pc);
//			System.out.println("連接確認 14");
			CPMWBQSystemProvider.provider().BQload(pc);
//			System.out.println("連接確認 15");
			MJPushProvider.provider().userLoading(pc);
//			System.out.println("連接確認 16");
/*			if (MJDeathPenaltyService.service().use()) {
				MJDeathPenaltyexpDatabaseLoader.getInstance().do_Select(pc);
				MJDeathPenaltyItemDatabaseLoader.getInstance().do_Select(pc);
			}*/
//			System.out.println("連接確認 17");
			pc.load_lateral_status();
			//System.out.println("連接確認 18");
			if (MJAttendanceLoadManager.ATTEN_IS_RUNNING) {
				if (!client.is_shift_battle()) {
					SC_ATTENDANCE_BONUS_INFO_EXTEND.send(pc);

					SC_ATTENDANCE_BONUS_GROUP_INFO.send(pc);
					SC_ATTENDANCE_USER_DATA_EXTEND.send(pc);
					SC_ATTENDANCE_BONUS_GROUP_INFO.openinfo(pc);
					MJAttendanceRewardsHistory.send_history(pc);
//					System.out.println("登入簽到封包");
					if (account.getAttendance_Premium()) {
						pc.sendPackets(
								SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(2), 0),
								true);
						pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(2), 0),
								true);
					}
					if (account.getAttendance_Special()) {
						pc.sendPackets(
								SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(3), 0),
								true);
						pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(3), 0),
								true);
					}

					if (account.getAttendance_Brave_Warrior()) {
						pc.sendPackets(
								SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(4), 1),
								true);
						pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(4), 1),
								true);
					}
					if (account.getAttendance_Aden_World()) {
						pc.sendPackets(
								SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(5), 1),
								true);
						pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(5), 1),
								true);
					}
					if (account.getAttendance_Bravery_Medal()) {
						pc.sendPackets(
								SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(6), 1),
								true);
						pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(6), 1),
								true);
					}
					SC_ATTENDANCE_INFO_NOTI.send1(pc);
					SC_ATTENDANCE_INFO_NOTI.send2(pc);
				}
			}
//			System.out.println("連接確認 19");
			Buff_Individual(pc);
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			on_fatigue(pc);

			pc.special_resistance_skill(pc, 0, 0, true);
			SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
			ClanBuffListLoader.getInstance().load_clan_buff(pc);

//			PcBuffCheck(pc); // 網咖Buff

			/** 凱撒訓練所 **/
			if (pc.getMap().getBaseMapId() == 1400) {
				pc.start_teleport(33491, 32762, 4, 0, 18339, false, false);
			}
			/** 精靈之墓 **/
			if (pc.getMapId() == 430) {
				pc.start_teleport(32779, 32831, 622, 0, 18339, false, false);
			}

			// 冰地圖 氣
			if (pc.getMapId() >= 2101 && pc.getMapId() <= 2151 || pc.getMapId() >= 2151 && pc.getMapId() <= 2201) {
				pc.start_teleport(33442, 32809, 4, 5, 18339, false, false);
			}

			// 火焰之杖/神秘恢復藥水 刪除.
			if (!(pc.getMapId() >= 2101 && pc.getMapId() <= 2151 || pc.getMapId() >= 2151 && pc.getMapId() <= 2201)) {
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					if (item.getItemId() == 30055 || item.getItemId() == 30056) {
						if (item != null) {
							pc.getInventory().removeItem(item, item.getCount());
						}
					}
				}
			}
//			System.out.println("連接確認 15");
			if (pc.getReturnStat() != 0) {
				resetStats(pc);
			} else {
				pc.sendBonusStats();
			}

			// TODO 用戶連接通知（管理員用）
			for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
				if (player.isGm()) {
					player.sendPackets("\\aG- [\\aA連接\\aG] " + pc.getName() + " " + client.getAccountName() + " "
							+ client.getIp() + "");
				}
			}
			Contact_Notice(pc);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("◆（登入）連接失敗◆：請截圖並回報此錯誤。");
		}

		if (pc != null && is_returner) {
			final L1PcInstance p = pc;
			GeneralThreadPool.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					MJShiftObjectManager.getInstance().do_returner(p);
				}
			});
		}
		// TODO 從跨服返回後重新刷新對象
		pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false);

		if (pc != null && Config.Login.UseShiftServer) {
			final MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_sender_object(pc.getId());
			if (sobject != null) {
				if (MJShiftObjectManager.getInstance().is_battle_server_running()) {
					MJShiftObjectManager.getInstance().do_send_battle_server(pc, sobject.get_convert_parameters());
					System.out.println(String.format("【帳號】%s【角色】%s【%s】【IP】%s 參與對抗戰的角色重新連接完成！",
							pc.getAccountName(), charName, MJNSHandler.getLocalTime(), client.getIp()));
					return pc;
				}

				String homeserveridentity = MJShiftObjectManager.getInstance().get_home_server_identity();
				MJShiftObjectManager.getInstance().do_receive(client, pc.getId(),
						new MJShiftObjectOneTimeToken(homeserveridentity, true, sobject, homeserveridentity, false)
								.to_onetime_token());
				return pc;
			}
		}


		return pc;
	}

	public C_LoginToServer(byte abyte0[], GameClient client) throws FileNotFoundException, Exception {
		super(abyte0);

		String charName = readS();

		if (charName.startsWith("_L")) {
			client.sendPacket(S_ChangeCharName.getChangedStart());
			return;
		}
		doEnterWorld(charName, client, true, 0, 0, 0);
	}

	private static void getItemBookMark(L1PcInstance pc) {
		L1ItemInstance[] items = pc.getInventory().findItemsId(700023);
		for (int i = 0; i < items.length; i++) {
			L1ItemBookMark.bookmarItemkDB(pc, items[i]);
		}
	}

	private static void loadItems(L1PcInstance pc, boolean sendOption) {
		// 從DB讀取角色和倉庫的物品
		if (sendOption)
			pc.getInventory().sendOptioon();
		else {

			CharacterTable.getInstance().restoreInventory(pc);
			SC_ADD_INVENTORY_NOTI.sendLoginInventoryNoti(pc);
		}
		if (sendOption) {
			MJRankUserLoader.getInstance().onUser(pc);
		}
	}

	/** 顯示安全區 **/
	private static void safetyzone(L1PcInstance pc) {
		if (pc.getZoneType() == 0) {
			if (pc.getSafetyZone() == true) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, false));
				pc.setSafetyZone(false);
			}
		} else {
			if (pc.getSafetyZone() == false) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, true));
				pc.setSafetyZone(true);
			}
		}
	}

	private static int CheckMail(L1PcInstance pc) {
		int count = 0;
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement(" SELECT count(*) as cnt FROM letter where receiver = ? AND isCheck = 0");
			pstm1.setString(1, pc.getName());

			rs = pstm1.executeQuery();
			if (rs.next()) {
				count = rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}

		return count;
	}

	public static void AinhasadFaithLoad(L1PcInstance pc) {
		
		
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_special_stat2 WHERE obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
//			Map<Integer, Integer> ainhasadFaithList2 = new HashMap<Integer, Integer>();
			HashMap<Integer, Timestamp> ainhasadFaithList = new HashMap<Integer, Timestamp>();
			SC_EINHASAD_FAITH_LIST_NOTI noti = SC_EINHASAD_FAITH_LIST_NOTI.newInstance();
			
			while(rs.next()) {
				int index = rs.getInt("index_id");
				int group = rs.getInt("group_id");
				int type = rs.getInt("type");
				
				
				AinhasadSpecialStat2Info info = AinhasadSpecialStat2Loader.getInstance().getSpecialStat(index);
				int desc_id = info.get_desc_id();
				
				Timestamp endTime = rs.getTimestamp("endTime");
				String endTimeStr = endTime.toString();
				long ts = Timestamp.valueOf(endTimeStr).getTime();
				long currentTime = System.currentTimeMillis();
//				long eTime = Long.parseLong(endTimeint);
				long diffTime = (ts - currentTime);
				int remainTime = Long.valueOf(ts/1000).intValue();
				if (type ==2) {
					if (diffTime < 0) {
						AinhasadSpecialStat2Loader.getInstance().deleteSpecialStat2(pc.getId(), pc.getName(), index);
						SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI.send(pc, index);
						continue;
					}
				}
				ainhasadFaithList.put(index, endTime);
			}
			if (ainhasadFaithList.containsKey(1) && ainhasadFaithList.containsKey(2) && ainhasadFaithList.containsKey(3) && ainhasadFaithList.containsKey(4)) {
				noti.send(pc, 1, 0, 1, null, true);
			} else {
				noti.send(pc, 1, 0, 1, null, false);
			}
			if (ainhasadFaithList.containsKey(5) && ainhasadFaithList.containsKey(6) && ainhasadFaithList.containsKey(7) && ainhasadFaithList.containsKey(8)) {
				noti.send(pc, 1, 0, 2, null, true);
			} else {
				noti.send(pc, 1, 0, 2, null, false);
			}
			
			for (int i = 1; i <=8; i++) {
				if (!ainhasadFaithList.containsKey(i)) {
					noti.send(pc, 2, i, i>=1 && i<=4 ? 1 : 2, null, false);
				} else {
					noti.send(pc, 2, i, i>=1 && i<=4 ? 1 : 2, ainhasadFaithList.get(i), true);
				}
			}
			pc.sendPackets(noti, MJEProtoMessages.SC_EINHASAD_FAITH_LIST_NOTI, true);

			for (int i = 1; i <=8; i++) {
				if (ainhasadFaithList.containsKey(i)) {
					SC_EINHASAD_FAITH_BUFF_NOTI.send_index(pc, 2 , 9280, i);		
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static void loadSkills(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			// 1 火 2 水 3 火/水 4 風 5 風/火 6 水/風 7 水/風/火 8 地 9 地/火 10 地/水 11 地/水/火 12 地/風
			// 13 地/風/火 14 地/水/風 15 全部
			// 1 火 2 水 4 風 8 地 NPC屬性

			if (pc.isElf()) {
				Glory_Earth_Attr(pc);
			}
			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			List<Integer> skillIdList = new ArrayList<Integer>();
			SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
			while (rs.next()) {
				int skillId = rs.getInt("skill_id");
				noti.appendNewSpell(skillId, true);
				skillIdList.add(skillId);
				pc.setSkillMastery(skillId);
			}

			SkillCheck.getInstance().AddSkill(pc.getId(), skillIdList);
			pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private static void 보안버프(L1PcInstance pc) {
		pc.getAC().addAc(-1);
		pc.sendPackets(new S_PacketBox(pc, S_PacketBox.ICON_SECURITY_SERVICES));
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	private static void serchSummon(L1PcInstance pc) {
		try {
			for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
				if (summon.getMaster().getId() == pc.getId()) {
					summon.setMaster(pc);
					pc.addPet(summon);
					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
						visiblePc.sendPackets(new S_SummonPack(summon, visiblePc));
					}
				}
			}
		} catch (Exception e) {

		}
	}

	private static void Clanclan(L1PcInstance pc) {
		// 3245 君主的召喚：請加入血盟
		// 3246 君主的召喚：請招募血盟成員
		// 3247 創建血盟並輕鬆通知
		// 3248 收到血盟加入請求
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan == null && pc.isCrown()) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(3247))); // 創建血盟並輕鬆通知
		} else if (clan != null && pc.isCrown()) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(3246))); // 請招募血盟成員
		} else if (clan == null && !pc.isCrown()) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(3245))); // 請加入血盟
		}
	}

	private static List<BuffInfo> loadBuff(L1PcInstance pc) {
		List<BuffInfo> buffList = new ArrayList<BuffInfo>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"SELECT skill_id, remaining_time, poly_id FROM character_buff WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();

			while (rs.next()) {
				BuffInfo buffInfo = new BuffInfo();
				buffInfo.skillId = rs.getInt("skill_id");
				buffInfo.remainTime = rs.getInt("remaining_time");
				buffInfo.polyId = rs.getInt("poly_id");

				buffList.add(buffInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return buffList;
	}

	private static void processBuff(L1PcInstance pc, List<BuffInfo> buffList) {
		int[] icon = new int['±'];
		Arrays.fill(icon, 0);

		for (BuffInfo buffInfo : buffList) {
			int skillid = buffInfo.skillId;
			int remaining_time = buffInfo.remainTime;

			if ((skillid >= COOKING_BEGIN && skillid <= COOKING_END) || skillid == 1541 || skillid == COOK_STR_Bless
					|| skillid == COOK_DEX_Bless || skillid == COOK_INT_Bless || skillid == COOK_GROW_Bless
					|| skillid == L1SkillId.MetisSoup || skillid == L1SkillId.MetisCooking || skillid == L1SkillId.SuperStrengthBuff) {
				if (skillid != L1SkillId.EXP_POTION_Event) {
					L1Cooking.eatCooking(pc, skillid, remaining_time);
					continue;
				}
			}
			if (skillid == DRAGON_TOPAZ) {
				final int r_time = remaining_time;
				pc.setSkillEffect(skillid, remaining_time * 1000);
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						pc.sendPackets(new S_PacketBox(r_time, 2, true, true));
					}
				}, 1000);
				SC_REST_EXP_INFO_NOTI.send(pc);
				continue;
			}
			if (skillid == L1SkillId.PC_EXP_UP) {
				if (pc.isPcBuff()) {
//					System.out.println(remaining_time);
					pc.setSkillEffect(L1SkillId.PC_EXP_UP, remaining_time * 1000);
					L1SkillUse.on_icons(pc, skillid, remaining_time);
				} else {
					pc.killSkillEffectTimer(L1SkillId.PC_EXP_UP);
					L1SkillUse.off_icons(pc, L1SkillId.PC_EXP_UP);
				}
			}
			switch (skillid) {
			case L1SkillId.MANADECREASEPOTION:
				pc.setSkillEffect(L1SkillId.MANADECREASEPOTION, remaining_time * 1000);
				L1SkillUse.on_icons(pc, skillid, remaining_time);
				break;
			case REDUCTION_ARMOR:
				int reduc_armor_value = pc.getLevel() >= 50 ? (pc.getLevel() - 45) / 5 : 0;
				pc.addDamageReductionByArmor(reduc_armor_value);
				pc.set_reducreduction_value(reduc_armor_value);
				if (pc != null && pc.isPassive(MJPassiveID.REDUCTION_ARMOR_VETERAN.toInt())) {
					int bonus = 0;
					if (pc.getLevel() >= 80 && pc.getLevel() <= 100) {
						bonus = ((pc.getLevel() - 80) / 4) + 1;
					} else {
						bonus = 5;
					}
					pc.set_pvp_defense(bonus);
					pc.addSpecialResistance(eKind.FEAR, 3);
					pc.set_reduction_armor_veteran(true);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				}
				SC_SPELL_BUFF_NOTI.sendDatabaseIcon(pc, SkillsTable.getInstance().getTemplate(skillid), remaining_time,
						true);
				break;
			case L1SkillId.USER_WANTED1:
				pc.setSkillEffect(L1SkillId.USER_WANTED1, -1);
				pc.set_Wanted_Level(1);
				pc.doWanted(true, true);
				break;
			case L1SkillId.USER_WANTED2:
				pc.setSkillEffect(L1SkillId.USER_WANTED2, -1);
				pc.set_Wanted_Level(2);
				pc.doWanted(true, true);
				break;
			case L1SkillId.USER_WANTED3:
				pc.setSkillEffect(L1SkillId.USER_WANTED3, -1);
				pc.set_Wanted_Level(3);
				pc.doWanted(true, true);
				break;
			case God_buff: // 黑蛇增益
				pc.getAC().addAc(-2);
				pc.addSpecialResistance(eKind.SPIRIT, 10);
				pc.addMaxHp(20);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.addMaxMp(13);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 4914, remaining_time));
				break;
			case SHAPE_CHANGE:
				int poly_id = buffInfo.polyId;
				L1PolyMorph.doPoly(pc, poly_id, remaining_time, L1PolyMorph.MORPH_BY_LOGIN, false, false);
				break;
			case STATUS_BRAVE:
				pc.sendPackets(new S_SkillBrave(pc.getId(), 1, remaining_time));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0));
				pc.setBraveSpeed(1);
				break;
			case STATUS_ELFBRAVE:
				pc.sendPackets(new S_SkillBrave(pc.getId(), 3, remaining_time));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0));
				pc.setBraveSpeed(3);
				break;
			case STATUS_HASTE:
				pc.sendPackets(new S_SkillHaste(pc.getId(), 1, remaining_time).toString());
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
				pc.setMoveSpeed(1);
				break;
			case STATUS_BLUE_POTION:
			case STATUS_BLUE_POTION2:
				pc.sendPackets(new S_SkillIconGFX(34, remaining_time, true).toString());
				break;
			case STATUS_CHAT_PROHIBITED:
				pc.sendPackets(new S_SkillIconGFX(36, remaining_time).toString());
				break;
			case COUNTER_MIRROR:
				// icon[0] = remaining_time / 16;
				pc.addWeightReduction(300);
				break;
				case DECREASE_WEIGHT:// 法師減少負重
				case REDUCE_WEIGHT:// 幻術師減少負重
				icon[0] = remaining_time / 16;
				pc.addWeightReduction(180);
				break;
			case DECAY_POTION:
				icon[1] = remaining_time / 4;
				break;
			case SILENCE:
				icon[2] = remaining_time / 4;
				break;
			case VENOM_RESIST:
				icon[3] = remaining_time / 4;
				break;
			case WEAKNESS:
				icon[4] = remaining_time / 4;
				pc.addDmgup(-5);
				pc.addHitup(-1);
				break;
			case DISEASE:
				icon[5] = remaining_time / 4;
				pc.addHitup(-6);
				pc.getAC().addAc(12);
				break;
			case BERSERKERS:
				if (pc.isWizard()) {
					pc.addDmgup(2);
					pc.addHitup(8);
				} else {
					pc.getAC().addAc(10);
					pc.addDmgup(2);
					pc.addHitup(8);
				}
				L1SkillUse.on_icons(pc, BERSERKERS, remaining_time);
				break;
			case NATURES_TOUCH:
				icon[8] = remaining_time / 4;
				break;
			case L1SkillId.WIND_SHACKLE:
				icon[9] = remaining_time / 4;
				break;
			case ERASE_MAGIC:
				icon[10] = remaining_time / 4;
				break;
			case ADDITIONAL_FIRE:
				icon[11] = remaining_time / 4;
				break;
			case ELEMENTAL_FALL_DOWN:
				icon[12] = remaining_time / 4;
				int playerAttr = pc.getElfAttr();
				int i = -50;
				switch (playerAttr) {
				case 0:
					pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
					break;
				case 1:
					pc.getResistance().addEarth(i);
					pc.setAddAttrKind(1);
					break;
				case 2:
					pc.getResistance().addFire(i);
					pc.setAddAttrKind(2);
					break;
				case 4:
					pc.getResistance().addWater(i);
					pc.setAddAttrKind(4);
					break;
				case 8:
					pc.getResistance().addWind(i);
					pc.setAddAttrKind(8);
					break;
				default:
					break;
				}
				break;
			case STRIKER_GALE:
				icon[14] = remaining_time / 4;
				break;
			case SOUL_OF_FLAME:
				icon[15] = remaining_time / 4;
				break;
			case POLLUTE_WATER:
				icon[16] = remaining_time / 4;
			case COMA_A:
				icon[30] = (remaining_time + 16) / 32;
				icon[31] = 40;
				pc.getAbility().addAddedCon(1);
				pc.getAbility().addAddedDex(5);
				pc.getAbility().addAddedStr(5);
				pc.addHitRate(3);
				pc.getAC().addAc(-3);
				break;
			case COMA_B:
				icon[30] = (remaining_time + 16) / 32;
				icon[31] = 41;
				pc.getAbility().addSp(1);
				pc.getAbility().addAddedCon(3);
				pc.getAbility().addAddedDex(5);
				pc.getAbility().addAddedStr(5);
				pc.addHitRate(5);
				pc.getAC().addAc(-8);
				pc.add_item_exp_bonus(20);
				break;
			case EXP_POTION:
				pc.send_effect(false, 134, remaining_time);
				L1SkillUse.on_icons(pc, skillid, remaining_time);
				break;
			case EXP_POTION_Event:
				SC_SPELL_BUFF_NOTI EXP_POTION_Event = SC_SPELL_BUFF_NOTI.newInstance();
				EXP_POTION_Event.set_noti_type(eNotiType.RESTAT);
				EXP_POTION_Event.set_spell_id(L1SkillId.EXP_POTION_Event);
				EXP_POTION_Event.set_duration(remaining_time);
				EXP_POTION_Event.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				EXP_POTION_Event.set_on_icon_id(3069);
				EXP_POTION_Event.set_off_icon_id(6768);
				EXP_POTION_Event.set_icon_priority(10);
				EXP_POTION_Event.set_tooltip_str_id(5095);
				EXP_POTION_Event.set_new_str_id(5095);
				EXP_POTION_Event.set_end_str_id(0);
				EXP_POTION_Event.set_is_good(true);
				pc.sendPackets(EXP_POTION_Event, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
				break;
			case EINHASAD_GRACE:
				pc.setSkillEffect(L1SkillId.EINHASAD_GRACE, (int) remaining_time * 1000);
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.EINHASAD, pc));
				pc.sendPackets(new S_ACTION_UI2("安加保護", (long) remaining_time * 1000), true);
				SC_EXP_BOOSTING_INFO_NOTI.send(pc);
				break;
			case EXP_BUFF:
				long hasad = pc.getAccount().getBlessOfAin();
				if (hasad < 10000) {
					pc.sendPackets(S_InventoryIcon.iconNewUnLimit(EXP_BUFF + 1, 5087, true)); // 安全區
				} else {
					pc.sendPackets(S_InventoryIcon.icoNew(EXP_BUFF, 5087, remaining_time, true)); // 狩獵場
				}
				break;
			case STATUS_CASHSCROLL:
				int time = (remaining_time / 4) - 255;
				if (time <= 0) {
					time += 255;
				}
				icon[18] = time;
				icon[19] = 61;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.addMaxHp(50);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// TODO 派對原型
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				break;
			case STATUS_CASHSCROLL2:
				int time2 = (remaining_time / 4) - 255;
				if (time2 <= 0) {
					time2 += 255;
				}
				icon[18] = time2;
				icon[19] = 62;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.addMaxMp(40);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				break;
			case STATUS_CASHSCROLL3:
				int time3 = (remaining_time / 4) - 255;
				if (time3 <= 0) {
					time3 += 255;
				}
				icon[18] = time3;
				icon[19] = 63;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.addDmgup(3);
				pc.addHitup(3);
				pc.getAbility().addSp(3);
				break;
			case STATUS_CASHSCROLL4:
				int time4 = (remaining_time / 4) - 255;
				if (time4 <= 0) {
					time4 += 255;
				}
				icon[18] = time4;
				icon[19] = 63;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.getAbility().addSp(3);
				pc.addBaseMagicHitUp(5);
				pc.getResistance().addcalcPcDefense(3);
				break;
			case STATUS_CASHSCROLL5:
				int time5 = (remaining_time / 4) - 255;
				if (time5 <= 0) {
					time5 += 255;
				}
				icon[18] = time5;
				icon[19] = 63;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.addBowDmgup(3);
				pc.addBowHitup(5);
				pc.getResistance().addcalcPcDefense(3);
				break;
			case STATUS_CASHSCROLL6:
				int time6 = (remaining_time / 4) - 255;
				if (time6 <= 0) {
					time6 += 255;
				}
				icon[18] = time6;
				icon[19] = 63;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.addDmgRate(3);
				pc.addHitup(5);
				pc.getResistance().addcalcPcDefense(3);
				break;
			case CONCENTRATION:
				icon[20] = remaining_time / 16;
				break;
			case INSIGHT:
				icon[21] = remaining_time / 16;
				pc.getAbility().addAddedStr((byte) 1);
				pc.getAbility().addAddedDex((byte) 1);
				pc.getAbility().addAddedCon((byte) 1);
				pc.getAbility().addAddedInt((byte) 1);
				pc.getAbility().addAddedWis((byte) 1);
				pc.resetBaseMr();
				break;
			case PANIC:
				icon[22] = remaining_time / 16;
				pc.getAbility().addAddedStr((byte) -1);
				pc.getAbility().addAddedDex((byte) -1);
				pc.getAbility().addAddedCon((byte) -1);
				pc.getAbility().addAddedInt((byte) -1);
				pc.getAbility().addAddedWis((byte) -1);
				pc.getAbility().addAddedCha((byte) -1);
				pc.resetBaseMr();
				break;
			case MORTAL_BODY:
				icon[23] = remaining_time / 4;
				break;
//			case DRAGON_SKIN:
//				icon[28] = remaining_time / 16;
//				break;
			case STATUS_FRUIT:
				icon[29] = remaining_time / 4;
				break;
			case RESIST_MAGIC:
				pc.getResistance().addMr(10);
				pc.sendPackets(new S_ElfIcon(remaining_time / 16, 0, 0, 0).toString());
				break;
			case CLEAR_MIND:
				pc.getAbility().addAddedStr((byte) 1);
				pc.getAbility().addAddedDex((byte) 1);
				pc.getAbility().addAddedInt((byte) 1);
				break;
			case ELEMENTAL_PROTECTION:
				int attr = pc.getElfAttr();
				if (attr == 1) {
					pc.getResistance().addEarth(50);
				} else if (attr == 2) {
					pc.getResistance().addFire(50);
				} else if (attr == 4) {
					pc.getResistance().addWater(50);
				} else if (attr == 8) {
					pc.getResistance().addWind(50);
				}
				pc.sendPackets(new S_ElfIcon(0, 0, 0, remaining_time / 16).toString());
				break;
			case FEATHER_BUFF_A:
				icon[36] = remaining_time / 16;
				icon[37] = 70;
				pc.addHpr(3);
				pc.addMpr(3);
				pc.addDmgup(2);
				pc.addHitup(2);
				pc.addMaxHp(50);
				pc.addMaxMp(30);
				// pc.addSp(2);
				pc.getAbility().addSp(2);
				break;
			case FEATHER_BUFF_B:
				icon[36] = remaining_time / 16;
				icon[37] = 71;
				pc.addHitup(2);
				// pc.addSp(1);
				pc.getAbility().addSp(1);
				pc.addMaxHp(50);
				pc.addMaxMp(30);
				break;
			case FEATHER_BUFF_C:
				icon[36] = remaining_time / 16;
				icon[37] = 72;
				pc.addMaxHp(50);
				pc.addMaxMp(30);
				pc.getAC().addAc(-2);
				break;
			case FEATHER_BUFF_D:
				icon[36] = remaining_time / 16;
				icon[37] = 73;
				pc.getAC().addAc(-1);
				break;
			case ANTA_BUFF:
				pc.getAC().addAc(-2);
				pc.getResistance().addWater(50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, remaining_time / 60));
				break;
			case FAFU_BUFF:
				pc.addHpr(3);
				pc.addMpr(1);
				pc.getResistance().addWind(50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, remaining_time / 60));
				break;
			case RIND_BUFF:
				pc.addHitup(3);
				pc.addBowHitup(3);
				pc.getResistance().addFire(50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 88, remaining_time / 60));
				break;
			case BLOOD_LUST:
				pc.sendPackets(new S_SkillBrave(pc.getId(), 6, remaining_time).toString());
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 6, remaining_time));
				pc.setBraveSpeed(1);
				break;
			case SAND_STORM:
				pc.setBraveSpeed(1);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 1, remaining_time).toString());
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 1, 0));
				pc.setAttackSpeed();
				break;
			case DANCING_BLADES:
				pc.setBraveSpeed(1);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 8, remaining_time).toString());
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 8, 0));
				pc.sendPackets(new S_SkillIconAura(154, remaining_time).toString());
				pc.setAttackSpeed();
				break;
			case FOCUS_WAVE:
				pc.setBraveSpeed(1);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 10, remaining_time).toString());
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 10, 0));
				pc.setAttackSpeed();
				break;
			case HURRICANE:
				pc.setBraveSpeed(9);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 9, remaining_time).toString());
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 9, 0));
				pc.setAttackSpeed();
				break;
			case STATUS_DRAGON_PEARL:
				pc.sendPackets(String.valueOf(new S_Liquor(pc.getId(), 8)));
				pc.setPearl(1);
				pc.sendPackets(String.valueOf(new S_ServerMessage(1065, remaining_time)));
				break;
			case DRAGON_ARMOR_BLESSING:
				L1SkillUse.on_icons(pc, skillid, remaining_time);
				break;
			case 레벨업보너스:
				pc.sendPackets(new S_PacketBox(remaining_time, true, true));
				break;
			case DRAGON_PUPLE:
				pc.sendPackets(new S_PacketBox(remaining_time, 1, true, true));
				break;
			case EMERALD_NO:
				pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, 0x01, remaining_time));
				break;
			case EMERALD_YES:
				pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, 0x02, remaining_time));
				break;
			case 정상의가호:
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 12536, remaining_time));
				break;
			case SetBuff:
				remaining_time = 30;
				break;
			case POLY_RING_MASTER:
				poly_id = buffInfo.polyId;
				L1PolyMorph.doPoly(pc, poly_id, remaining_time, L1PolyMorph.MORPH_BY_LOGIN, true, false);
				break;
			case POLY_RING_MASTER2:
				poly_id = buffInfo.polyId;
				L1PolyMorph.doPoly(pc, poly_id, remaining_time, L1PolyMorph.MORPH_BY_LOGIN, false, true);
				break;
			case SIDE_OF_ME_BLESSING:
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 14646, remaining_time));
				break;
			case RE_START_BLESSING:
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 14647, remaining_time));
				break;
			case NEW_START_BLESSING:
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 14648, remaining_time));
				break;
			case LIFE_BLESSING:
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 14649, remaining_time));
				break;
			case CLASS_RANK_BLESS_PRINCE_1:
			case CLASS_RANK_BLESS_KNIGHT_1:
			case CLASS_RANK_BLESS_ELF_1:
			case CLASS_RANK_BLESS_WIZARD_1:
			case CLASS_RANK_BLESS_DARKELF_1:
			case CLASS_RANK_BLESS_DRAGONKNIGHT_1:
			case CLASS_RANK_BLESS_BLACKWIZARD_1:
			case CLASS_RANK_BLESS_WARRIOR_1:
			case CLASS_RANK_BLESS_FENCER_1:
				pc.addMaxHp(200);
				pc.getResistance().addcalcPcDefense(10);
				L1SkillUse.on_icons(pc, skillid, remaining_time);
				break;
			case CLASS_RANK_BLESS_PRINCE_2:
			case CLASS_RANK_BLESS_KNIGHT_2:
			case CLASS_RANK_BLESS_ELF_2:
			case CLASS_RANK_BLESS_WIZARD_2:
			case CLASS_RANK_BLESS_DARKELF_2:
			case CLASS_RANK_BLESS_DRAGONKNIGHT_2:
			case CLASS_RANK_BLESS_BLACKWIZARD_2:
			case CLASS_RANK_BLESS_WARRIOR_2:
			case CLASS_RANK_BLESS_FENCER_2:
				pc.addMaxHp(150);
				pc.getResistance().addcalcPcDefense(5);
				L1SkillUse.on_icons(pc, skillid, remaining_time);
				break;
			case CLASS_RANK_BLESS_PRINCE_3:
			case CLASS_RANK_BLESS_KNIGHT_3:
			case CLASS_RANK_BLESS_ELF_3:
			case CLASS_RANK_BLESS_WIZARD_3:
			case CLASS_RANK_BLESS_DARKELF_3:
			case CLASS_RANK_BLESS_DRAGONKNIGHT_3:
			case CLASS_RANK_BLESS_BLACKWIZARD_3:
			case CLASS_RANK_BLESS_WARRIOR_3:
			case CLASS_RANK_BLESS_FENCER_3:
				pc.addMaxHp(100);
				pc.getResistance().addcalcPcDefense(3);
				L1SkillUse.on_icons(pc, skillid, remaining_time);
				break;
			case L1SkillId.STR_BUFF:
				pc.addHitup(5);
				pc.addDmgup(3);
				pc.getAbility().addAddedStr(1);

				pc.setSkillEffect(L1SkillId.STR_BUFF, remaining_time);
				pc.sendPackets(new S_OwnCharStatus(pc), true);

				SC_SPELL_BUFF_NOTI STR_BUFF = SC_SPELL_BUFF_NOTI.newInstance();
				STR_BUFF.set_noti_type(eNotiType.RESTAT);
				STR_BUFF.set_spell_id(L1SkillId.STR_BUFF);
				STR_BUFF.set_duration(remaining_time);
				STR_BUFF.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				STR_BUFF.set_on_icon_id(4354);
				STR_BUFF.set_off_icon_id(4354);
				STR_BUFF.set_tooltip_str_id(1720);
				STR_BUFF.set_new_str_id(1720);
				STR_BUFF.set_end_str_id(2854);
				STR_BUFF.set_is_good(true);
				pc.sendPackets(STR_BUFF, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				break;
			case L1SkillId.DEX_BUFF:
				pc.addBowHitup(5);
				pc.addBowDmgup(3);
				pc.getAbility().addAddedDex((byte) 1);
				pc.setSkillEffect(L1SkillId.DEX_BUFF, remaining_time);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()), true);

				SC_SPELL_BUFF_NOTI DEX_BUFF = SC_SPELL_BUFF_NOTI.newInstance();
				DEX_BUFF.set_noti_type(eNotiType.RESTAT);
				DEX_BUFF.set_spell_id(L1SkillId.DEX_BUFF);
				DEX_BUFF.set_duration(remaining_time);
				DEX_BUFF.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				DEX_BUFF.set_on_icon_id(4354);
				DEX_BUFF.set_off_icon_id(4354);
				DEX_BUFF.set_tooltip_str_id(1719);
				DEX_BUFF.set_new_str_id(1719);
				DEX_BUFF.set_end_str_id(2854);
				DEX_BUFF.set_is_good(true);
				pc.sendPackets(DEX_BUFF, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				break;
			case L1SkillId.INT_BUFF:
				pc.addMaxMp(50);
				pc.getAbility().addSp(2);
				pc.getAbility().addAddedInt(1);
				pc.setSkillEffect(L1SkillId.INT_BUFF, remaining_time);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);

				SC_SPELL_BUFF_NOTI INT_BUFF = SC_SPELL_BUFF_NOTI.newInstance();
				INT_BUFF.set_noti_type(eNotiType.RESTAT);
				INT_BUFF.set_spell_id(L1SkillId.INT_BUFF);
				INT_BUFF.set_duration(remaining_time);
				INT_BUFF.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				INT_BUFF.set_on_icon_id(4354);
				INT_BUFF.set_off_icon_id(4354);
				INT_BUFF.set_tooltip_str_id(1721);
				INT_BUFF.set_new_str_id(1721);
				INT_BUFF.set_end_str_id(2854);
				INT_BUFF.set_is_good(true);
				pc.sendPackets(INT_BUFF, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				break;
			case DECIDING_BUFF:
				pc.getAC().addAc(-5);
				pc.addBowHitup(5);
				pc.addHitup(5);
				pc.addBaseMagicHitUp(2);
				pc.getResistance().addcalcPcDefense(5);
				pc.getResistance().addPVPweaponTotalDamage(5);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharStatus(pc));
				L1SkillUse.on_icons(pc, skillid, remaining_time);
				break;
			case HERO_GAHO_BUFF:
				pc.getAbility().addAddedStr(1);
				pc.getAbility().addAddedDex(1);
				pc.getAbility().addAddedInt(1);
				pc.addBowHitup(3);
				pc.addHitup(3);
				pc.addBaseMagicHitUp(3);
				pc.addSpecialPierce(eKind.ALL, 3);
				pc.getResistance().addPVPweaponTotalDamage(3);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharStatus(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				L1SkillUse.on_icons(pc, skillid, remaining_time);
				break;
			case L1SkillId.EINHASAD_PRIMIUM_FLAT:
			case L1SkillId.EINHASAD_GREAT_FLAT:
				// 不呼叫 skillUse
				break;
			default:
				L1SkillUse l1skilluse = new L1SkillUse();
				l1skilluse.handleCommands(pc, skillid, pc.getId(), pc.getX(), pc.getY(), null, remaining_time, L1SkillUse.TYPE_LOGIN);
				break;
			}
			pc.setSkillEffect(skillid, remaining_time * 1000);
			if (skillid == STATUS_FRUIT) {
				if (pc.isElfBrave())
					pc.sendPackets(new S_SkillBrave(pc.getId(), 3, remaining_time).toString());
				else
					pc.sendPackets(new S_SkillBrave(pc.getId(), 4, remaining_time).toString());
			} else if (skillid == L1SkillId.EINHASAD_PRIMIUM_FLAT) {
				L1SkillId.onEinhasadPrimiumFlat(pc);
			} else if (skillid == L1SkillId.EINHASAD_GREAT_FLAT) {
				L1SkillId.onEinhasadGreatFlat(pc);
			}
			// TODO 移動速度封包（緩速，珍珠）
			L1SkillId.onSlowHandle(pc);
		}
		SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
		pc.sendPackets(new S_UnityIcon(icon[0], icon[1], icon[2], icon[3], icon[4], icon[5], icon[6], icon[7], icon[8],
				icon[9], icon[10], icon[11], icon[12], icon[13], icon[14], icon[15], icon[16], icon[17], icon[18],
				icon[19], icon[20], icon[21], icon[22], icon[23], icon[24], icon[25], icon[26], icon[27], icon[28],
				icon[29], icon[30], icon[31], icon[32], icon[33], icon[34], icon[35], icon[36], icon[37], icon[38]).toString());

	}

	private static void setExcludeList(L1PcInstance pc, L1ExcludingList exList) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_exclude WHERE char_id = ?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();

			while (rs.next()) {
				int type = rs.getInt("type");
				String name = rs.getString("exclude_name");
				if (!exList.contains(type, name)) {
					exList.add(type, name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void on_fatigue(L1PcInstance pc) {
		if (!FatigueProperty.getInstance().use_fatigue())
			return;

		Account account = pc.getAccount();
		pc.sendPackets(String.format("當前帳號的格蘭肯憤怒點數是 %,d點。", account.get_fatigue_point()));
		if (!account.has_fatigue()) {
			return;
		}
		account.send_fatigue(pc);
		pc.sendPackets(String.format("\f3當前帳號剩餘的格蘭肯憤怒時間為 %,d秒。", account.remain_fatigue() / 1000L));
	}

	public static void loadNBuff(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			if (Config.ServerAdSetting.AccountNBuff) {
				pstm = con.prepareStatement(
						"SELECT * FROM character_tams WHERE skill_id BETWEEN ? AND ? AND account_id=?");
			} else {
				pstm = con
						.prepareStatement("SELECT * FROM character_tams WHERE skill_id BETWEEN ? AND ? AND char_id=?");
			}
			pstm.setInt(1, 4075);
			pstm.setInt(2, 4095);
			if (Config.ServerAdSetting.AccountNBuff) {
				pstm.setInt(3, pc.getAccount().getAccountId());
			} else {
				pstm.setInt(3, pc.getId());
			}
			rs = pstm.executeQuery();
			while (rs.next()) {
				int skillId = rs.getInt("skill_id");
				Timestamp expirationTime = rs.getTimestamp("expiration_time");
				if (Config.ServerAdSetting.AccountNBuff) {
					if (expirationTime.getTime() <= System.currentTimeMillis()) {
						SQLUtil.execute("DELETE FROM character_tams WHERE account_id=? AND skill_id=?", new Object[] {
								Integer.valueOf(pc.getAccount().getAccountId()), Integer.valueOf(skillId) });
						continue;
					}
				} else {
					if (expirationTime.getTime() <= System.currentTimeMillis()) {
						SQLUtil.execute("DELETE FROM character_tams WHERE char_id=? AND skill_id=?",
								new Object[] { Integer.valueOf(pc.getId()), Integer.valueOf(skillId) });
						continue;
					}
				}

				if (pc.hasSkillEffect(skillId)) {
					continue;
				}
				int time = (int) (expirationTime.getTime() - System.currentTimeMillis()) / 1000;

				new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, time, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void resetStats(L1PcInstance pc) {
		try {
			if (pc.getAbility().getBaseAmount() != 75) {
				int basestr = 0;
				int basedex = 0;
				int basecon = 0;
				int baseint = 0;
				int basewis = 0;
				int basecha = 0;
				switch (pc.getType()) {
				case 0:
					basestr = 13;
					basedex = 9;
					basecon = 11;
					basewis = 11;
					basecha = 13;
					baseint = 9;
					break;
				case 1:
					basestr = 16;
					basedex = 12;
					basecon = 16;
					basewis = 9;
					basecha = 10;
					baseint = 8;
					break;
				case 2:
					basestr = 10;
					basedex = 12;
					basecon = 12;
					basewis = 12;
					basecha = 9;
					baseint = 12;
					break;
				case 3:
					basestr = 8;
					basedex = 7;
					basecon = 12;
					basewis = 14;
					basecha = 8;
					baseint = 14;
					break;
				case 4:
					basestr = 15;
					basedex = 12;
					basecon = 12;
					basewis = 10;
					basecha = 8;
					baseint = 11;
					break;
				case 5:
					basestr = 13;
					basedex = 11;
					basecon = 14;
					basewis = 10;
					basecha = 8;
					baseint = 10;
					break;
				case 6:
					basestr = 9;
					basedex = 10;
					basecon = 12;
					basewis = 14;
					basecha = 8;
					baseint = 12;
					break;
				case 7:
					basestr = 16;
					basedex = 13;
					basecon = 16;
					basewis = 7;
					basecha = 9;
					baseint = 10;
					break;
				case 8:
					basestr = 16;
					basedex = 13;
					basecon = 11;
					basewis = 11;
					basecha = 15;
					baseint = 5;
					break;
				case 9: // 槍騎士
					basestr = 14;
					basedex = 12;
					basecon = 16;
					basewis = 12;
					basecha = 6;
					baseint = 9;
					break;
				default:
					System.out.println(String.format("invalid type : %d", pc.getType()));
					break;
				}
				pc.getAbility().init();
				pc.getAbility().setBaseStr(basestr);
				pc.getAbility().setBaseInt(baseint);
				pc.getAbility().setBaseWis(basewis);
				pc.getAbility().setBaseDex(basedex);
				pc.getAbility().setBaseCon(basecon);
				pc.getAbility().setBaseCha(basecha);
				pc.save();
//				System.out.println(String.format("[因為重置屬性錯誤重新設置基礎屬性]: %s", pc.getName()));
			}

			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), null, 0,
					L1SkillUse.TYPE_LOGIN);

			if (pc.getWeapon() != null) {
				pc.getInventory().setEquipped(pc.getWeapon(), false, false, false, false);
			}

			for (L1ItemInstance armor : pc.getInventory().getItems()) {
				if (armor != null && armor.isEquipped()) {
					pc.getInventory().setEquipped(armor, false, false, false, false);
				}

				pc.sendPackets(new S_CharVisualUpdate(pc));
				pc.sendPackets(new S_OwnCharStatus2(pc));
			}
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.sendPackets(new S_OwnCharStatus2(pc));
			pc.sendPackets(String.valueOf(new S_ReturnedStat(pc, S_ReturnedStat.START)));
		} catch (Exception e) {
			System.out.println("重置屬性命令錯誤");
			e.printStackTrace();
		}
	}

	// TODO 關於增益
	public static void Buff_Individual(L1PcInstance pc) {
		if (Config.ServerAdSetting.DelayTimer) {
			ItemDelayTimer.loadItemDelay(pc);
		}
		MJKDALoader.getInstance().install(pc, false);
		pc.getKDA().assassination_level = 0;
		pc.getKDA().do_assassination(pc);
		if (GMCommands.IS_PROTECTION) {
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.SAFE_MODE);
			noti.set_duration(-1);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(4760);
			noti.set_off_icon_id(4760);
			noti.set_icon_priority(10);
			noti.set_tooltip_str_id(4017);
			noti.set_is_good(false);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[保護模式啟動] 死亡時不會受到懲罰。"));
			pc.sendPackets("\f3[保護模式啟動] 死亡時經驗值不會下降且物品不會掉落。");;
		}
		if (pc.getMapId() == 1501) {
			pc.getAC().addAc(-3);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(4066);
			noti.set_duration(-1);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
			noti.set_on_icon_id(7235);
			noti.set_off_icon_id(0);
			noti.set_icon_priority(-1);
			noti.set_tooltip_str_id(5402);
			noti.set_new_str_id(5402);
			noti.set_end_str_id(0);
			noti.set_is_good(true);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
		}
		if (pc.getInventory().checkItem(4100121)) {
			L1SkillUse.on_icons(pc, L1SkillId.HIGH_CLASS_GAHO_BUFF, -1);
		}
		if (pc.getLevel() == 1) {
			pc.set_exp(ExpTable.getExpByLevel(Config.CharSettings.STARTLEVEL));
		}

		SC_EXTEND_SLOT_INFO.slot_info_send(pc);

		Account a = pc.getAccount();
		if (a.getCPW() != null && a.getCPW().length() >= 6) {
			securityBuff(pc);
		}

		if (pc.getLevel() > 1 && pc.getLevel() < Config.ServerAdSetting.StartCharBoho) {
			pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.Start_BUFF, 3804, true));
		} else {
			pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.Start_BUFF));
		}

		long curTime = System.currentTimeMillis() / 1000;
		if ((pc.getCryOfSurvivalTime() + (60 * 180)) <= curTime) { // 如果外叫時間已經超過當前時間，則可以立即使用
			pc.sendPackets(new S_SurvivalCry(1));
		} else { // 如果還有剩餘時間，則計算時間並發送
			long time = (pc.getCryOfSurvivalTime() + (60 * 180)) - (System.currentTimeMillis() / 1000);
			pc.sendPackets(new S_SurvivalCry(time));
		}

		long sysTime = System.currentTimeMillis();
		if (pc.getNetConnection().getAccount().getDragonRaid() != null) {
			if (sysTime <= pc.getNetConnection().getAccount().getDragonRaid().getTime()) {
				long BloodTime = pc.getNetConnection().getAccount().getDragonRaid().getTime() - sysTime;
				pc.removeSkillEffect(L1SkillId.DRAGONRAID_BUFF);
				pc.setSkillEffect(L1SkillId.DRAGONRAID_BUFF, (int) BloodTime);
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONRAID_BUFF, (int) BloodTime / 1000), true);
			}
		}

		pc.getInventory().consumeItem(810006);
		pc.getInventory().consumeItem(810007);
	}
	private static EventAlramTick es ;
	
	public static void Start_Event_Alram(L1PcInstance pc) {
		final long inteval = 60 * 60 * 1000L;
		es = new EventAlramTick(pc, inteval, true);
		GeneralThreadPool.getInstance().schedule(es, 10);
	}
	
	public static void PcReset(L1PcInstance pc) {
		Timestamp time = pc.getAccount().getLastLogOut();
		GregorianCalendar today = new GregorianCalendar();
		int year = today.get(today.YEAR);
		int month = today.get(today.MONTH);
		int day = today.get(today.DATE);
		int hour = today.get(today.HOUR);
		
/*		L1FreeShield shield = CharacterFreeShieldTable.getInstance().getFreeShield(pc.getAccount().getAccountId());
		if (shield == null) {
			CharacterFreeShieldTable.getInstance().UpdateFreeShieldInfo(pc);
		}*/
		if (time == null) {
			CharacterFreeShieldTable.getInstance().resetGaho(pc);
/*			pc.getAccount().setPcGaho(3);
			pc.getAccount().setPcGahoUse(0);
			pc.getAccount().setFeatherCount(0);*/
		} else if (time.getDate() < day || time.getMonth() < month) {
			CharacterFreeShieldTable.getInstance().resetGaho(pc);
/*			pc.getAccount().setPcGaho(3);
			pc.getAccount().setPcGahoUse(0);
			pc.getAccount().setFeatherCount(0);*/
		} else if (time.getDate() == day) {
			if (time.getHours() < 6 ) {
				CharacterFreeShieldTable.getInstance().resetGaho(pc);
/*				pc.getAccount().setPcGaho(3);
				pc.getAccount().setPcGahoUse(0);
				pc.getAccount().setFeatherCount(0);	*/
			}
		}
	}
	public static void FreeShieldLoad(int loginid, String loginname) {
		L1FreeShield shield = CharacterFreeShieldTable.getInstance().getFreeShieldLogin(loginid, loginname);
	}
	
	public static void PcBuffCheck(L1PcInstance pc) {
		// 網咖增益
//		SC_FREE_BUFF_SHIELD_UPDATE_NOTI.send(pc);
		long sysTime = System.currentTimeMillis();
		if (pc.isPcBuff()) {
			long pcCafeTime = pc.getAccount().getBuff_PCCafe().getTime() - sysTime;
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String
					.format(Config.Message.PC_BUFF_MESSAGE + " %s", MJString.remainTimeString((int) (pcCafeTime / 1000))));
//            System.out.println("網咖增益應用");
			L1SkillId.onPcCafeBuff(pc, pcCafeTime);
		} else {
			SC_USER_START_SUNDRY_NOTI.send(pc, false);
		}
	}

	// TODO 檢查公會
	public static void Clan_Check(L1PcInstance pc) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		// 通知上線.
		if (clan != null) {
			clan.updateClanMemberOnline(pc);
//			ClanStorageTable.getInstance().is_ClanStorageUse(pc, pc.getName());
		}

		if (pc.getClanid() != 0) { // 屬於公會中
			if (clan != null) {
				if (clan.getEntranceNotice() != null) {
					SC_BLOOD_PLEDGE_ENTER_NOTICE_NOTI.send(pc);
				}
				if (clan.getBless() != 0) {
					new L1SkillUse().handleCommands(pc, 504 + clan.getBless(), pc.getId(), pc.getX(), pc.getY(), null,
							clan.getBuffTime()[clan.getBless() - 1], L1SkillUse.TYPE_LOGIN);
				}
				pc.sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo(clan.getClanName(), pc.getClanRank(), pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, pc.getClan().getEmblemStatus()));
				if (clan.getGazeSize() != 0) {
					pc.sendPackets(String.valueOf(new S_ClanAttention(clan.getGazeSize(), clan.getGazeList())));
				}
				if (pc.getClanid() == clan.getClanId() &&
						// 解散公會，並在重新創建同名公會時的對策
						pc.getClanname().toLowerCase().equals(clan.getClanName().toLowerCase())) {
					for (L1PcInstance clanMember : clan.getOnlineClanMember()) {
						if (clanMember.getId() != pc.getId()) {
							clanMember.sendPackets(String.valueOf(new S_ServerMessage(843, pc.getName())));
							// 現在，公會成員%0%s已經登錄遊戲。
						}
					}

					MJWar war = clan.getCurrentWar();
					if (war != null) {
						war.notifyEnenmy(pc);
						if (war instanceof MJCastleWar) {
							MJCastleWar castleWar = (MJCastleWar) war;
							if (castleWar.isRun())
								castleWar.onLordBuff(pc);
						}
					}

				} else {
					pc.setClanid(0);
					pc.setClanname("");
					pc.setClanRank(0);
					pc.save(); // 將角色信息寫入資料庫
				}
			} else {
				pc.setClanid(0);
				pc.setClanname("");
				pc.setClanRank(0);
				pc.save(); // 將角色信息寫入資料庫
			}
		}
		if (pc.getPartnerId() != 0) { // 結婚中
			L1PcInstance partner = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
			if (partner != null && partner.getPartnerId() != 0) {
				if (pc.getPartnerId() == partner.getId() && partner.getPartnerId() == pc.getId()) {
					pc.sendPackets(new S_ServerMessage(548));
					// 你的夥伴現在正在遊戲中。
					partner.sendPackets(new S_ServerMessage(549));
					// 你的夥伴剛剛登錄了。
				}
			}
		}
	}

	// TODO 通知相關
	public static void Contact_Notice(L1PcInstance pc) {
		SupplementaryService warehouse = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
		int size = warehouse.getSize();
		if (size > 0) {
			SC_GOODS_INVEN_NOTI.do_send(pc);
			pc.sendPackets(String.valueOf(new S_SystemMessage("\\aG附加物品倉庫中有未領取的物品。")));
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "附加物品倉庫中有未領取的物品。"));
		}
		if (Config.Login.StandbyServer) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, Config.Message.OpenTimeMont));
			pc.sendPackets(Config.Message.OpenTimeMont);
		}
		if (pc.getLevel() <= 95) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, Config.Message.GAMESERVERMENT));
		}
		if (pc.getLevel() >= Config.CharSettings.MaxLevel /* && !pc.isGm() */) {
			pc.sendPackets(Config.Message.MAX_LEVEL_MESSAGE);
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, Config.Message.MAX_LEVEL_MESSAGE));
		}
		if (!Config.Web.webServerOnOff) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("\\aG應用中心目前正在檢查中。")));
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aG應用中心目前正在檢查中。無法使用。"));
		}
		if (Config.ServerAdSetting.FEATHER) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[活動進行中] 幸運商店活動"));
		}
		if (Config.ServerAdSetting.PolyEvent2) {
			SC_POLYMORPH_EVENT_NOTI noti = SC_POLYMORPH_EVENT_NOTI.newInstance();
			noti.set_eventEnable(true);
			pc.sendPackets(noti, MJEProtoMessages.SC_POLYMORPH_EVENT_NOTI, true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "變身事件正在進行中。"));
		}

		if (pc.getLevel() > 1 && pc.getLevel() <= Config.ServerAdSetting.LineageBuff) {
			pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.HUNTER_BLESS3, 4126, true));
		} else {
			pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.HUNTER_BLESS3));
		}
	}

	// TODO 登錄時檢查精靈屬性部分
	private static void Glory_Earth_Attr(L1PcInstance pc) {
		if (pc.getElfAttr() == 1 && pc.getGlory_Earth_Attr() == 0) {
			pc.setElfAttr(1);
			pc.sendPackets(new S_SkillIconGFX(15, 1).toString());
		} else if (pc.getElfAttr() == 2 && pc.getGlory_Earth_Attr() == 0) {
			pc.setElfAttr(2);
			pc.sendPackets(new S_SkillIconGFX(15, 2).toString());
		} else if (pc.getElfAttr() == 4 && pc.getGlory_Earth_Attr() == 0) {
			pc.setElfAttr(4);
			pc.sendPackets(new S_SkillIconGFX(15, 4).toString());
		} else if (pc.getElfAttr() == 8 && pc.getGlory_Earth_Attr() == 0) {
			pc.setElfAttr(8);
			pc.sendPackets(new S_SkillIconGFX(15, 8).toString());

			// TODO 學習榮耀之地時參考以下條件
		} else if (pc.getElfAttr() == 1 && pc.getGlory_Earth_Attr() == 2) {
			pc.setElfAttr(1);
			pc.setGlory_Earth_Attr(2);
			pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()).toString());
		} else if (pc.getElfAttr() == 1 && pc.getGlory_Earth_Attr() == 4) {
			pc.setElfAttr(1);
			pc.setGlory_Earth_Attr(4);
			pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()).toString());
		} else if (pc.getElfAttr() == 1 && pc.getGlory_Earth_Attr() == 8) {
			pc.setElfAttr(1);
			pc.setGlory_Earth_Attr(8);
			pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()).toString());
		} else if (pc.getElfAttr() == 2 && pc.getGlory_Earth_Attr() == 4) {
			pc.setElfAttr(2);
			pc.setGlory_Earth_Attr(4);
			pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()).toString());
		} else if (pc.getElfAttr() == 2 && pc.getGlory_Earth_Attr() == 8) {
			pc.setElfAttr(2);
			pc.setGlory_Earth_Attr(8);
			pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()).toString());
		} else if (pc.getElfAttr() == 4 && pc.getGlory_Earth_Attr() == 8) {
			pc.setElfAttr(4);
			pc.setGlory_Earth_Attr(8);
			pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()).toString());
		}
	}



	@Override
	public String getType() {
		return C_LOGIN_TO_SERVER;
	}
}
