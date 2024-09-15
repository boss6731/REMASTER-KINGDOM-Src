package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.ANTA_MAAN;
import static l1j.server.server.model.skill.L1SkillId.BIRTH_MAAN;
import static l1j.server.server.model.skill.L1SkillId.BLACK_DRAGON_MAAN;
import static l1j.server.server.model.skill.L1SkillId.CURSE_BLIND;
import static l1j.server.server.model.skill.L1SkillId.DARKNESS;
import static l1j.server.server.model.skill.L1SkillId.DECAY_POTION;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_PUPLE;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_TOPAZ;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.ENCHANT_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.FAFU_MAAN;
import static l1j.server.server.model.skill.L1SkillId.GREATER_HASTE;
import static l1j.server.server.model.skill.L1SkillId.HASTE;
import static l1j.server.server.model.skill.L1SkillId.HOLY_WALK;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.LIFE_MAAN;
import static l1j.server.server.model.skill.L1SkillId.LINDBIOR_SPIRIT_EFFECT;
import static l1j.server.server.model.skill.L1SkillId.LIND_MAAN;
import static l1j.server.server.model.skill.L1SkillId.MOVING_ACCELERATION;
import static l1j.server.server.model.skill.L1SkillId.NAVER_BLACK_DRAGON_MAAN;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_MAAN;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;
import static l1j.server.server.model.skill.L1SkillId.SLOW;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL3;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL4;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL5;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL6;
import static l1j.server.server.model.skill.L1SkillId.STATUS_DRAGON_PEARL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FLOATING_EYE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FRUIT;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_MITHRIL_POWDER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER_OF_EVA;
import static l1j.server.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;
import static l1j.server.server.model.skill.L1SkillId.STATUS_WISDOM_POTION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_WISDOM_POTION_POWER;
import static l1j.server.server.model.skill.L1SkillId.VALA_MAAN;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.Collections;


import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Template.CommonServerInfo;

import l1j.server.Config;
import l1j.server.FatigueProperty;
import l1j.server.L1DatabaseFactory;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.server.ClanBuffList.ClanBuffListLoader;

import l1j.server.ExpMerge.ExpMergeController;
import l1j.server.ForgottenIsland.FIController;
import l1j.server.GameSystem.Colosseum.L1Colosseum;
import l1j.server.GameSystem.SkillBook.SkillBookLoader;
import l1j.server.IndunEx.AurakiaInDungeonEx;
import l1j.server.IndunSystem.Luun_Secret.Luun_Secret;
import l1j.server.IndunSystem.Luun_Secret.Luun_Secret_System;
import l1j.server.InvenBonusItem.InvenBonusItemInfo;
import l1j.server.InvenBonusItem.InvenBonusItemLoader;
import l1j.server.ItemSelector.ItemSelectorLoader;
import l1j.server.MJActionListener.ActionListenerLinkageLoader;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJCTSystem.MJCTHandler;
import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.MJCharacterActionSystem.WandActionHandlerFactory;
import l1j.server.MJCompanion.Basic.Potion.MJCompanionPotionInfo;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJDTSSystem.MJDTSLoader;
import l1j.server.MJDungeonTimer.Loader.DungeonTimePotionLoader;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJItemEnchantSystem.MJItemEnchanterLoader;
import l1j.server.MJItemExChangeSystem.MJItemExChangeLoader;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJPassiveSkill.MJPassiveLoader;
import l1j.server.MJRaidSystem.Loader.MJRaidCreatorLoader;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHARATER_FOLLOW_EFFECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EXP_BOOSTING_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SUMMON_PET_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_USER_START_SUNDRY_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ENCHANT_RESULT;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SMELTING_SLOT_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SMELTING_UPDATE_SLOT_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SmeltingResult;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FORCE_FINISH_PLAY_SUPPORT_NOTI.eReason;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.SpellExtractor.SpellExtractorLoader;
import l1j.server.Stadium.StadiumManager;
import l1j.server.TJ.TJCouponProvider;
import l1j.server.lotto.lotto_system;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.server.Controller.BugRaceController;
import l1j.server.server.Controller.SkillDataController;
import l1j.server.server.server.clientpackets.C_Attr;
import l1j.server.server.server.clientpackets.C_CommonClick;
import l1j.server.server.server.datatables.*;
import l1j.server.server.model.Beginner;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanJoin;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ClanJoinInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1EffectInstance;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.server.model.Instance.L1PcInstance.PolyTrigger;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.model.item.function.Fishing;
//import l1j.server.server.model.item.function.ItemSelector.SelectorType;
//import l1j.server.server.model.item.function.ItemSelector;
import l1j.server.server.model.item.function.L1BlessTypeEnchant;
import l1j.server.server.model.item.function.L1BoxItem;
import l1j.server.server.model.item.function.L1BuffItem;
import l1j.server.server.model.item.function.L1EtcItemViewByte;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.item.function.L1ItemSelector;
import l1j.server.server.model.item.function.L1MagicDoll;
import l1j.server.server.model.item.function.L1Material;
import l1j.server.server.model.item.function.L1MeterialChoice;
import l1j.server.server.model.item.function.L1ProtectEnchantScroll;
import l1j.server.server.model.item.function.L1SpawnWand;
import l1j.server.server.model.item.function.OmanAmulet;
import l1j.server.server.model.item.function.TelBook;
import l1j.server.server.model.item.function.Telbookitem;
import l1j.server.server.model.item.function.additem;
import l1j.server.server.model.item.function.additem2;
import l1j.server.server.model.item.function.omanTel;
import l1j.server.server.model.item.function.L1MagicDoll.Effect;
import l1j.server.server.model.item.function.L1MagicDoll.Info;
import l1j.server.server.model.item.smelting.SmeltingScrollInfo;
import l1j.server.server.model.item.smelting.SmeltingScrollLoader;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_ArrowsEquipment;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_AttackStatus;
import l1j.server.server.serverpackets.S_ChangeCharName;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EnchantMessage;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_IdentifyDesc;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_ItemSelector;
import l1j.server.server.serverpackets.S_ItemStatus;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Pledge;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_ShowPolyList;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TamWindow;
import l1j.server.server.serverpackets.S_TestEffect;
import l1j.server.server.server.serverpackets.S_TrueTargetNew;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.server.serverpackets.S_UserCommands5;
import l1j.server.server.server.serverpackets.S_War;
import l1j.server.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ItemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class C_ItemUSe extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_ITEM_USE = "[C] C_ItemUSe";
	private static Logger _log = Logger.getLogger(C_ItemUSe.class.getName());
	private static Random _random = new Random(System.nanoTime());
	private int previousCount;
	private boolean previousLighting;
	private boolean previousEquipped;
	private L1ItemInstance useItem;
	private L1PcInstance useUser;
	private long useItemlatestUsed;

	public boolean invoke() {
		if (changed()) {
			MJObjectEventProvider.provider().inventoryEventFactory().fireInventoryItemUsed(useUser, useItem);
			return true;
		}
		return false;
	}

	boolean changed() {
		if (useItem != null) {
		}

		return useItem != null && (previousCount != useItem.getCount() || previousLighting != useItem.isNowLighting()
				|| previousEquipped != useItem.isEquipped() || useItemlatestUsed != useItem.getLastUsedMillis()
				|| (useItem.getItemOwner() == null || useItem.getItemOwner().getId() != useUser.getId()));
	}

	public C_ItemUSe(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		int itemObjid = readD();

		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost() || pc.getMapId() == 5166 || pc.getMapId() == 5167) {
			return;
		}
		/*
		 * if (!_randomInit) {// 為了美觀的分布，進行一些初始化。 for (int i = 0; i < 100000; ++i) {
		 * _random.nextInt(2000000000); } _randomInit = true; }
		 */

		if (MJCommons.isLock(pc) || MJCommons.isNonAction(pc)) {
			if (!(pc.hasSkillEffect(L1SkillId.OSIRIS) || pc.hasSkillEffect(L1SkillId.DESPERADO)))
				return;
		}

		L1ItemInstance l1iteminstance = pc.getInventory().getItem(itemObjid);
		if (pc.isStatReset()) {
			return;
		}

		if (l1iteminstance == null || l1iteminstance.getItem() == null) {
			// LoggerInstance.getInstance().addError(String.format("C_ItemUse.java: %s(%d)
			// "找不到物品對象。",
			// pc.getName(), itemObjid));
			return;
		}

		if (l1iteminstance.getItem().getUseType() == -1) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(74, l1iteminstance.getLogName())));
			return;
		}
		if (Config.Login.UseShiftServer
				&& !MJShiftObjectManager.getInstance().use_item_white_list(pc, l1iteminstance)) {
			pc.sendPackets(String.format("%s目前無法使用。", l1iteminstance.getName()));
			return;
		}

		int pcObjid = pc.getId();
		if (pc.get_teleport()) { // 傳送處理中
			return;
		}

		// 存在錯誤相關補充
		L1PcInstance jonje = L1World.getInstance().getPlayer(pc.getName());
		if (jonje == null && pc.getAccessLevel() != 200) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("存在錯誤強制終止！請重新連接")));
			client.kick();
			return;
		}

		if (pc.isDead() == true) {
			return;
		}
		if (!pc.getMap().isUsableItem() && !pc.isGm()) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(563)));
			// pc.sendPackets(new S_Paralysis(7, false));
			pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
			return;
		}
		if (CommonUtil.teleport_check(pc, l1iteminstance)) {
			// pc.sendPackets(new S_Paralysis(7, false));
			pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
			pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
			return;
		}

		int itemId;
		try {
			itemId = l1iteminstance.getItem().getItemId();

			if (Config.ServerAdSetting.DelayTimer) {
				if (l1iteminstance.getItem().get_delayEffect() <= 0) {
					if (pc.hasItemDelayTime(l1iteminstance)) {
						if (l1iteminstance.getItem().getType() == 17)
							pc.sendPackets(String.valueOf(new S_Paralysis(7, false)));

						// XXX 特定物品冷卻時間顯示
						// if (l1iteminstance.getItemId() >= 410032
						// || l1iteminstance.getItemId() <= 410038 ||
						// l1iteminstance.getItemId() == 410176
						// || l1iteminstance.getItemId() == 410177) {
						// if (pc.getItemDelayLogTime(l1iteminstance) != null) {
						// SimpleDateFormat sdf = new
						// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						// String fm =
						// sdf.format(pc.getItemDelayLogTime(l1iteminstance).getTime());
						// pc.sendPackets(String.format("%s 之後可使用。",
						// fm));
						// }
						return;
						// }
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		useUser = pc;
		useItem = l1iteminstance;
		previousCount = useItem.getCount();
		previousLighting = useItem.isNowLighting();
		previousEquipped = useItem.isEquipped();
		useItemlatestUsed = useItem.getLastUsedMillis();
		MJObjectEventProvider.provider().inventoryEventFactory().fireInventoryItemClicked(pc, l1iteminstance);

		if (MJRaidCreatorLoader.getInstance().createRaid(pc, l1iteminstance))
			return;

		/** MJCTSystem **/
		if (itemId == MJCTLoadManager.CTSYSTEM_LOAD_ID) {
			MJCTHandler.load(pc, l1iteminstance);
			return;
		}

		if (MJCompanionInstance.use_item(pc, l1iteminstance)) // 如果是寵物物品，執行後返回.
			return;

		MJCompanionPotionInfo pInfo = MJCompanionPotionInfo.get_potion_info(l1iteminstance.getItemId());
		if (pInfo != null) {
			pInfo.use_item(pc, l1iteminstance);
			return;
		}

		if (MJBotLoadManager.useBotItem(pc, itemId))
			return;

		MJPacketParser parser = WandActionHandlerFactory.create(l1iteminstance);
		if (parser != null) {
			parser.parse(pc, this);
			parser.doWork();
			return;
		}

		if (ClanBuffListLoader.getInstance().useItem(pc, l1iteminstance))
			return;

		if (MJPassiveLoader.getInstance().useItem(pc, l1iteminstance))
			return;

		if (SkillBookLoader.getInstance().useItem(pc, l1iteminstance))// 魔法書更新
			return;


		if (DungeonTimePotionLoader.getInstance().use_potion(pc, l1iteminstance))
			return;

		if (ActionListenerLinkageLoader.getInstance().use_item(pc, l1iteminstance))
			return;

		if (L1ClanJoinInstance.use_item(pc, l1iteminstance))
			return;

		if (MJDTSLoader.getInstance().use_item(pc, l1iteminstance))
			return;

		if (Config.Login.UseShiftServer
				&& itemId == MJShiftObjectManager.getInstance().get_character_transfer_itemid()) {
			if (MJShiftObjectManager.getInstance().is_battle_server_running()) {
				pc.sendPackets("伺服器對抗賽進行中無法使用伺服器轉移。");
				return;
			}
			if (pc.getInventory().checkItem(itemId)) {
				List<CommonServerInfo> servers = MJShiftObjectManager.getInstance().get_commons_servers(true);
				if (servers == null || servers.size() <= 0) {
					pc.sendPackets("目前沒有可轉移的伺服器。");
					return;
				}
				int success_count = servers.size();
				for (CommonServerInfo csInfo : servers) {
					String message = "可轉移";
					if (!csInfo.server_is_on) {
						--success_count;
						message = "不可轉移(伺服器關閉)";
					}
					if (!csInfo.server_is_transfer) {
						--success_count;
						message = "不可轉移(功能關閉)";
					}
					pc.sendPackets(String.format("- [%s] %s", csInfo.server_description, message));
				}
				if (success_count <= 0) {
					pc.sendPackets("目前沒有可轉移的伺服器。");
					return;
				}
				pc.sendPackets("請輸入要轉移的伺服器。");
				pc.set_ready_server_shift(true);
				return;
			}
		}

		if (IntRange.includes(itemId, 4100255, 4100259)) {
			if (!pc.isGm())
				return;

			int idx = itemId - 4100255;

			/*
			 * MJIRRacer[] racers = MJIRTemplateLoader.getInstance().getCurrentRacers();
			 * MJIRRacer racer = racers[idx]; racer.isTumble = true;
			 * pc.sendPackets(String.format("已使第 %d 號魔法娃娃跌倒。", idx + 1));
			 */
			BugRaceController.getInstance()._is_downs[idx] = true;
			pc.sendPackets(String.format("已擊倒第%d隻蟲熊。", idx + 1));
			return;
		}

		if (l1iteminstance.getItemId() == 4100383) {
			if (!FatigueProperty.getInstance().use_fatigue()) {
				pc.sendPackets("目前格蘭肯系統未啟動。");
				return;
			}
			Account.Account account = pc.getAccount();
			if (!account.has_fatigue()) {
				pc.sendPackets("當前不受增益效果影響。");
				return;
			}

			if (pc.getInventory().consumeItem(l1iteminstance, 1)) {
				account.done_fatigue(pc);
				pc.sendPackets("格蘭肯增益效果已重置。");
			} else {
				pc.sendPackets("找不到物品信息。");
			}
			return;
		}

		if (TJCouponProvider.provider().onUseCoupon(pc, l1iteminstance)) {
			return;
		}

		// if(ItemSelectorTable.isSelectorInfo(itemId)){ // 物品選擇器
		// L1ItemInstance item = new
		// l1j.server.server.model.item.function.ItemSelector(L1Item,
		// SelectorType.NORMAL);
		// }
		/**
		 * 增益物品
		 */
		L1BuffItem bi = L1BuffItem.get(itemId);
		if (bi != null) {
			bi.use(pc, l1iteminstance);
			return;
		}

		L1Material material = L1Material.get(itemId);
		if (material != null) {
			material.use(pc, l1iteminstance);
			return;
		}

		int l_id = readD();
		if (MJItemExChangeLoader.getInstance().use_item(pc, l1iteminstance, l_id)) {
			return;
		} else {
			readP(-4);
		}

		// TODO 使用（獲取）類型排序
		int use_type = l1iteminstance.getItem().getUseType();
		int get_type = l1iteminstance.getItem().getType();
		int readed_int_1 = 0;
		int readed_short_1 = 0;
		int readed_short_2 = 0;
		int readed_short_3 = 0;
		int readed_byte_1 = 0;
		int readed_byte_2 = 0;
		String readed_string_1 = MJString.EmptyString;

		// TODO getType 在這裡添加
		switch (get_type) {
			case 17:
				readed_int_1 = (short) readH();
				readed_short_1 = readH();
				readed_short_2 = readH();
				break;

			default:
				// TODO getUseType 在這裡添加
				switch (use_type) {
					case 5:
						readed_int_1 = readD();
						readed_short_1 = readH();
						readed_short_2 = readH();
						break;
					case 6:
					case 7:
					case 8:
					case 9:
					case 14:
					case 26:
					case 27:
					case 28:
					case 46:
					case 68:
					case 74:
						readed_int_1 = readD();
						break;
					case 79:
					case 16:
						readed_string_1 = readS();
						break;
					case 39:
						readed_int_1 = readD();
						readed_string_1 = readS();
						break;
					case 17: // teleport_scroll
						readed_int_1 = readD();
						readed_short_1 = readH();
						readed_short_2 = readH();
						break;
					case 29:
						readed_short_1 = readH();
						readed_short_2 = readH();
						readed_short_3 = readH();
						break;
					case 42:
						readed_short_1 = readH();
						readed_short_2 = readH();
						break;
					case 64:
						readed_short_1 = readC();
						readed_short_2 = readC();
						if (SpellExtractorLoader.getInstance().useItem(pc, l1iteminstance, readed_short_1, readed_short_2 + 1))
							return;
						break;
					// case 601:

					// break;
					default:
						// TODO 在此處添加物品編號
					case 560025:
					case 560027:
					case 560028:
					case 560029:
					case 4100653:
					case 4100135:
						readed_byte_1 = readC();
						break;
				}
				break;
		}

		if (pc.getCurrentHp() > 0) {
			int delay_id = 0;
			/*
			 * if (l1iteminstance.getItem().getType2() == 0) { // 種別：其他物品 if
			 * (l1iteminstance.getItem() instanceof L1EtcItem) { delay_id = ((L1EtcItem)
			 * l1iteminstance.getItem()).get_delayid(); } } if (delay_id != 0) { // 有延遲設置
			 * if (pc.hasItemDelay(delay_id) == true) { if
			 * (l1iteminstance.getItem().getType() == 17) pc.sendPackets(new
			 * S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)); return; } }
			 */
			// 再使用檢查
			boolean isDelayEffect = false;

			// 如果是票券則返回
			if (itemId >= 8000000 && itemId <= 12000000) {
				return;
			}

			if (l1iteminstance.getItem().getType2() == 0) {
				int delayEffect = ((L1EtcItem) l1iteminstance.getItem()).get_delayEffect();
				if (delayEffect > 0) {
					isDelayEffect = true;
					Timestamp lastUsed = l1iteminstance.getLastUsed();
					if (lastUsed != null) {
						Calendar cal = Calendar.getInstance();
						if ((cal.getTimeInMillis() - lastUsed.getTime()) / 1000 <= delayEffect) {
							pc.sendPackets(String.valueOf(new S_SystemMessage(
									((delayEffect - (cal.getTimeInMillis() - lastUsed.getTime()) / 1000) / 60)
											+ "分 "+ ((delayEffect - (cal.getTimeInMillis() - lastUsed.getTime()) / 1000) % 60)
											+ "秒後可使用。")));
							return;
						}
					}
				}
			}
			L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(readed_int_1);
			_log.finest("request item use (obj) = " + itemObjid + " action = " + readed_int_1 + " value = "
					+ readed_string_1);
			if (l1iteminstance.getItem().getType2() == 0) { // 類別：其他物品
				L1EtcItemViewByte eiv = L1EtcItemViewByte.get(itemId);
				if (eiv != null) {
					if (pc.isCrown() && !l1iteminstance.getItem().isUseRoyal()
							|| pc.isKnight() && !l1iteminstance.getItem().isUseKnight()
							|| pc.isElf() && !l1iteminstance.getItem().isUseElf()
							|| pc.isWizard() && !l1iteminstance.getItem().isUseMage()
							|| pc.isDarkelf() && !l1iteminstance.getItem().isUseDarkelf()
							|| pc.isDragonknight() && !l1iteminstance.getItem().isUseDragonKnight()
							|| pc.isBlackwizard() && !l1iteminstance.getItem().isUseBlackwizard()
							|| pc.isWarrior() && !l1iteminstance.getItem().isUseWarrior()
							|| pc.isFencer() && !l1iteminstance.getItem().isUseFencer()
							|| pc.isLancer() && !l1iteminstance.getItem().isUseLancer()) {
						pc.sendPackets(264);
						return;
					}
				}

				int item_minlvl = ((L1EtcItem) l1iteminstance.getItem()).getMinLevel();
				int item_maxlvl = ((L1EtcItem) l1iteminstance.getItem()).getMaxLevel();
				if (item_minlvl != 0 && item_minlvl > pc.getLevel() && !pc.isGm()) {
					pc.sendPackets(String.valueOf(new S_SystemMessage(item_minlvl + "級以上才能使用該物品。")));
					pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
					return;
				} else if (item_maxlvl != 0 && item_maxlvl < pc.getLevel() && !pc.isGm()) {
					pc.sendPackets(String.valueOf(new S_SystemMessage(item_maxlvl + "級以下才能使用該物品。")));
					pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
					return;
				}
				if ((itemId == 40576 && !pc.isElf()) || (itemId == 40577 && !pc.isWizard()) // 靈魂的
						|| (itemId == 40578 && (!pc.isKnight() || !pc.isFencer()))) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(264))); // 1您的職業無法使用這
					return;
				}
				int use_effect_id = l1iteminstance.getItem().getUseEffectId();
				if (use_effect_id != 0) {
					pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), use_effect_id)));
				}

				if (l1iteminstance.getItem().getType() == 0) { // 箭矢
					if (pc.getWeapon() == null || pc.getWeapon().getItem().getType() != 4) {
						return;
					} else if (pc.getWeapon().getItem().getType() != 4) {
						L1ItemInstance arrow = pc.getInventory().findItemId(pc.getInventory().getArrowItemId());
						if (arrow != null) {
							pc.getInventory().setArrow(0);
							pc.sendPackets(new S_ArrowsEquipment(arrow).toString());
							pc.sendPackets(new S_ArrowsEquipment(l1iteminstance).toString());
						}
					} else if (pc.getInventory().getArrow() != null) {
						L1ItemInstance arrow = pc.getInventory().findItemId(pc.getInventory().getArrow().getItemId());
						pc.getInventory().setArrow(l1iteminstance.getItem().getItemId());
						pc.sendPackets(new S_ArrowsEquipment(arrow).toString());
						pc.sendPackets(String.valueOf(new S_ServerMessage(452, l1iteminstance.getLogName()))); // %0已經

						pc.sendPackets(new S_ArrowsEquipment(l1iteminstance).toString());
					} else {
						pc.getInventory().setArrow(l1iteminstance.getItem().getItemId());
						pc.sendPackets(String.valueOf(new S_ServerMessage(452, l1iteminstance.getLogName()))); // %0已經
						pc.sendPackets(new S_ArrowsEquipment(l1iteminstance).toString());
					}
				} else if (l1iteminstance.getItem().getType() == 15) { // 刺痛
					if (pc.getWeapon() == null || pc.getWeapon().getItem().getType() != 10) {
						return;
					} else if (pc.getWeapon().getItem().getType() != 10) {
						L1ItemInstance arrow = pc.getInventory().findItemId(pc.getInventory().getStingItemId());
						if (arrow != null) {
							pc.getInventory().setSting(0);
							pc.sendPackets(new S_ArrowsEquipment(arrow).toString());
							pc.sendPackets(new S_ArrowsEquipment(l1iteminstance).toString());
						}
					} else if (pc.getInventory().getSting() != null) {
						L1ItemInstance arrow = pc.getInventory().findItemId(pc.getInventory().getSting().getItemId());
						pc.getInventory().setSting(l1iteminstance.getItem().getItemId());
						pc.sendPackets(new S_ArrowsEquipment(arrow).toString());
						pc.sendPackets(String.valueOf(new S_ServerMessage(452, l1iteminstance.getLogName()))); // %0已經
						pc.sendPackets(new S_ArrowsEquipment(l1iteminstance).toString());
					} else {
						pc.getInventory().setSting(l1iteminstance.getItem().getItemId());
						pc.sendPackets(String.valueOf(new S_ServerMessage(452, l1iteminstance.getLogName()))); // %0已經
						pc.sendPackets(new S_ArrowsEquipment(l1iteminstance).toString());
					}
				} else if (l1iteminstance.getItem().getType() == 16) { // treasure_box
					L1TreasureBox box = L1TreasureBox.get(itemId);
					if (pc.getInventory().getSize() > 170) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("攜帶的物品太多了")));
						return;
					}
					if (pc.getInventory().getWeight100() > 82) { // 這部分修改會出錯
						pc.sendPackets(String.valueOf(new S_SystemMessage("物品太重，無法使用。")));
						return;
					}
					if (box != null) {
						if (box.open(pc)) {
							L1EtcItem temp = (L1EtcItem) l1iteminstance.getItem();
							if (temp.get_delayEffect() > 0) {
								isDelayEffect = true;
							} else {
								pc.getInventory().removeItem(l1iteminstance.getId(), 1);
							}
						}
					}

				} else if (l1iteminstance.getItem().getType() == 601) {
//					SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI.send(pc, l1iteminstance);
					ItemSelectorLoader.getInstance().processItemSelector(pc, l1iteminstance);
				} else if (l1iteminstance.getItem().getType() == 100) { // 精煉石
					useSmeltingScroll(pc, l1iteminstance, l1iteminstance1);
				} else if (l1iteminstance.getItem().getType() == 101) {
					if (Config.SmeltingSetting.ejectAll) {
						useSmeltingRemoveScroll(pc, l1iteminstance, l1iteminstance1);
					} else {
						useSmeltingRemoveScroll2(pc, l1iteminstance, l1iteminstance1);
					}

				} else if (l1iteminstance.getItem().getType() == 301) {
					useWeekBox(pc, l1iteminstance);
				} else if (l1iteminstance.getItem().getType() == 401) {
					useDollPotential(pc, l1iteminstance, l1iteminstance1);
				} else if (l1iteminstance.getItem().getType() == 2) { // light
					if (l1iteminstance.getRemainingTime() <= 0 && itemId != 40004) {
						return;
					}
					if (l1iteminstance.isNowLighting()) {
						l1iteminstance.setNowLighting(false);
						pc.getLight().turnOnOffLight();
					} else {
						l1iteminstance.setNowLighting(true);
						pc.getLight().turnOnOffLight();
					}
					pc.sendPackets(String.valueOf(new S_ItemName(l1iteminstance)));
				}

				// 騎士團物品無法使用，統一處理
				switch (itemId) {
					case 40095:
						if (!(pc.getMapId() == 0 || pc.getMapId() == 1 || pc.getMapId() == 2 || pc.getMapId() == 3
								|| pc.getMapId() == 7 || pc.getMapId() == 8 || pc.getMapId() == 9 || pc.getMapId() == 10
								|| pc.getMapId() == 11 || pc.getMapId() == 12 || pc.getMapId() == 12146
								|| pc.getMapId() == 12147 || pc.getMapId() >= 25 && pc.getMapId() <= 28
								|| pc.getMapId() == 777)) {
							if (get_type == 17) {
								// pc.sendPackets(new S_Paralysis(7, false));
								pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
							}
							pc.sendPackets(3274);
							return;
						}
						break;
					case 7005:
					case 7006:
					case 7007:
					case 7008:
					case 9004:
					case 9005:
					case 30073:
					case 30074:
					case 30075:
					case 30076:
					case 30077:
					case 30078:
					case 30079:
					case 30080:
					case 30081:
					case 30082:
					case 30083:
					case 30084:
					case 30085:
					case 30086:
					case 30087:
					case 30088:
					case 30089:
					case 40029:
					case 40030:
					case 40096:
					case 40097:
					case 40098:
					case 40099:
					case 42650:
					case 42651:
					case 42652:
					case 42653:
					case 42654:
					case 4100450:
					case 4100461:
					case 4100462:
					case 4100464:
					case 4100475:
					case L1ItemId.B_SCROLL_CHIVALRIC_ENCHANT_WEAPON:
						if (!(pc.getMapId() == 0 || pc.getMapId() == 1 || pc.getMapId() == 2 || pc.getMapId() == 3
								|| pc.getMapId() == 7 || pc.getMapId() == 8 || pc.getMapId() == 9 || pc.getMapId() == 10
								|| pc.getMapId() == 11 || pc.getMapId() == 12 || pc.getMapId() == 12146
								|| pc.getMapId() == 777 || pc.getMapId() == 12147
								|| pc.getMapId() >= 25 && pc.getMapId() <= 28)) {
							if (get_type == 17)
								// pc.sendPackets(new S_Paralysis(7, false));
								pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
							pc.sendPackets(3274);
							return;
						}
						break;
				}

				switch (itemId) {
					case 30001874:
					case 30001875:
					case 30001876:
						useAdenScroll(pc, l1iteminstance);
						break;

					case 30001882:
						System.out.println("確認");
//					L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(73201284, -1, pc.getX(), pc.getY(), pc.getMapId(), pc);
						SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(pc, Config.ItemOption.Effect, true);
						pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(pc,
								Config.ItemOption.Effect, true));
//					pc.sendPackets(new S_TrueTargetNew(pc.getId(), true));
//					pc.broadcastPacket(new S_TrueTargetNew(pc.getId(), true));
						break;
					case 420103:
					case 420105:
					case 420106:
					case 420107:
					case 420109:
					case 420110:
						useAreaWand(pc, itemId);
						break;
					case 420122:// 奧拉奇亞的箭矢
						L1MonsterInstance target = null;
						for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 10)) {
							if (!(obj instanceof L1MonsterInstance)) {
								continue;
							}
							if (((L1MonsterInstance) obj).getNpcId() == 7800300) {
								target = (L1MonsterInstance) obj;
								break;
							}
						}
						if (target == null) {
							break;
						}
						AurakiaInDungeonEx.useArrow(pc, target);
						break;
					case 14000005:
						useLuunSpawn(pc, 7800228, l1iteminstance);
						break;
					case 4100667:
						if (!(pc.getMapId() == 15482 || pc.getMapId() == 15492)) {
							if (get_type == 17)
								pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
							pc.sendPackets(3274);
							return;
						}
						break;
					case 4100653:
					case 4100654:
					case 4100655:
					case 4100656:
					case 4100657:
					case 4100658:
					case 4100659:
					case 4100660:
					case 4100661:
					case 4100662:
					case 4100663:
					case 4100664:
					case 4100665:
					case 4100670:
						if (!(pc.getMapId() == 15482 || pc.getMapId() == 15492)) {
							if (get_type == 17)
								pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
							pc.sendPackets(3274);
							return;
						}
						break;
				}

				switch (itemId) {
					// switch語句開始
					case 30001398:
					case 30001399:
						taras_buff(pc, l1iteminstance);
						break;
					case 30001376: { // 魔力減少藥水
						if (pc.hasSkillEffect(L1SkillId.MANADECREASEPOTION)) {
							pc.killSkillEffectTimer(L1SkillId.MANADECREASEPOTION);
							L1SkillUse.off_icons(pc, L1SkillId.MANADECREASEPOTION);
						}
						pc.setSkillEffect(L1SkillId.MANADECREASEPOTION, 10 * 60 * 1000);
						L1SkillUse.on_icons(pc, L1SkillId.MANADECREASEPOTION, 10 * 60);
						pc.getInventory().consumeItem(30001376, 1);
					}
					break;
					case 30001373: { // 美味的河豚
						int durationtime = 10 * 60 * 1000;
						if (pc.hasSkillEffect(L1SkillId.TastyPufferfish)) {
							pc.removeSkillEffect(L1SkillId.TastyPufferfish);
						}
						pc.setSkillEffect(L1SkillId.TastyPufferfish, durationtime);
						L1SkillUse.on_icons(pc, L1SkillId.TastyPufferfish, durationtime / 1000);
						pc.getInventory().consumeItem(30001373, 1);
					}
					break;
					case 30001374: { // 船長的水鏡
						int durationtime = 10 * 60 * 1000;
						if (pc.hasSkillEffect(L1SkillId.WaterMirror)) {
							pc.removeSkillEffect(L1SkillId.WaterMirror);
						}
						pc.setSkillEffect(L1SkillId.WaterMirror, durationtime);
						L1SkillUse.on_icons(pc, L1SkillId.WaterMirror, durationtime / 1000);
						pc.getInventory().consumeItem(30001374, 1);
					}
					break;
					case 700085:
					case 700086:
						if (pc.getLevel() >= 82) {
							pc.setTemporaryItemObjectId(l1iteminstance.getId());
							pc.sendPackets("請輸入要擴聲的消息。");
						} else
							pc.sendPackets("82級以下的勇士無法使用。");
						break;
				case 42656: // 戰士的幻影藥水箱
							// -- 使用時次數減少
					l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
					// -- 更新
					pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
					// -- 次數為0時刪除
					if (l1iteminstance.getChargeCount() == 0) {
						pc.getInventory().removeItem(l1iteminstance);
						pc.sendPackets(String.valueOf(new S_SystemMessage(l1iteminstance.getLogName() + " 已全部使用完。")));
					}
						break;
					case 90736:
					case 90735:
					case 3000245:
					case 3000244:
					case 3000242:
					case 90737:
					case 6016:
					case 410095: {
						int random = CommonUtil.random(100);
						L1ItemInstance item = null;
						if (itemId == 90737) { // -- 馬文的立方體100次
							item = pc.getInventory().storeItem(700013, 1);
							pc.sendPackets(String.valueOf(new S_SystemMessage("獲得: " + item.getLogName())));
						} else if (itemId == 6016) { // -- 修練者的補給箱20次
							createNewItem(pc, 40098, 10);
							createNewItem(pc, 40099, 10);
							createNewItem(pc, 40029, 100);
							createNewItem(pc, 40096, 3);
							createNewItem(pc, 40096, 2);
							if (pc.isElf()) {
								createNewItem(pc, 42651, 3);
							} else if (pc.isWizard() || pc.isBlackwizard()) {
								createNewItem(pc, 42652, 3);
							} else {
								createNewItem(pc, 42650, 3);
							}
						}
						// -- 使用時次數減少
						l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
						// -- 更新
						pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
						// -- 次數為0時刪除
						if (l1iteminstance.getChargeCount() == 0) {
							pc.getInventory().removeItem(l1iteminstance);
							pc.sendPackets(String.valueOf(new S_SystemMessage(l1iteminstance.getLogName() + " 已全部使用完。")));
					}
					break;
					case 41922:
					case 41923:
					case 41924:
					case 41925:
					{
						int buffId = itemId - 34243;
						L1SkillUse l1Skilluse = new L1SkillUse();
						l1Skilluse.handleCommands(pc, buffId, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
					}
					break;
						case 50020: // 封印卷軸
							useItemSealScroll(pc, l1iteminstance, l1iteminstance1);
							break;
						case 50021: // 解除封印卷軸
						useItemSealUnlockScroll(pc, l1iteminstance, l1iteminstance1);
						break;
					case 4100384:
						onUseEinhasadPrimiumFlat(pc, l1iteminstance, 86400);
						break;
					case 4100385:
						onUseEinhasadPrimiumFlat(pc, l1iteminstance, 259200);
						break;
					case 4100386:
						onUseEinhasadPrimiumFlat(pc, l1iteminstance, 604800);
						break;
					case 4100387:
						onUseEinhasadGreatFlat(pc, l1iteminstance, 86400);
						break;
					case 4100388:
						onUseEinhasadGreatFlat(pc, l1iteminstance, 259200);
						break;
					case 4100389:
						onUseEinhasadGreatFlat(pc, l1iteminstance, 604800);
						break;
					case 4100655:
						case L1ItemId.DRAGON_PEARL: // 龍之珍珠
						case L1ItemId.DRAGON_PEARL1: // 龍之珍珠（不可交易）
						useDragonPearl(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 3000224:
						if (l1iteminstance1.getItem().getItemId() != 1000004
								&& l1iteminstance1.getItem().getItemId() != 410064) {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 什麼也沒發生。
							return;
						}
						pc.sendPackets(String.valueOf(new S_SystemMessage("將龍之鑽石/翡翠轉換為2顆龍之黃玉。")));
						pc.getInventory().storeItem(7241, 2);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case L1ItemId.DRAGON_DIAMOND2:
						use_einhasad_potion(pc, l1iteminstance, SC_REST_EXP_INFO_NOTI.RE_DRAGON_DIAMOND_FINEST);
						break;
					case 1000007:
						use_einhasad_potion(pc, l1iteminstance, SC_REST_EXP_INFO_NOTI.RE_DRAGON_DIAMOND_ADVANCED);
						break;
					case L1ItemId.DRAGON_DIAMOND1:
						if (pc.getLevel() >= 82) {
							pc.sendPackets("82級以上無法使用。");
							return;
						}
					case L1ItemId.DRAGON_DIAMOND:
						use_einhasad_potion(pc, l1iteminstance, SC_REST_EXP_INFO_NOTI.RE_DRAGON_DIAMOND_NORMAL);
						break;
					case L1ItemId.DRAGON_DIAMOND_APPLE:
						use_einhasad_potion(pc, l1iteminstance, SC_REST_EXP_INFO_NOTI.RE_DRAGON_DIAMOND_APPLE);
						break;
					case 6017:
					case 410138:
					case 410064:
					case 1000002:
					case 1000003:
					case 7241:
					case 3000457:
					case 60255:
						useDragonTopaz(pc, l1iteminstance);
						break;
					case 410032:
					case 410033:
					case 410034:
					case 410035:
					case 410036:
					case 410037:
					case 410038:
					case 410176:
					case 410177:
						// case 3000159:
						useMaan(pc, l1iteminstance);
						break;
					case 410010:
					case 410011:
					case 410012:
					case 4100039:
					case 4100041:
					case 4100042:
					case 4100465:
					case 30063:
						if (itemId == 30063) {
							if (!(pc.getMapId() >= 1005 && pc.getMapId() <= 1022
									|| pc.getMapId() > 6000 && pc.getMapId() < 6999)) {
								pc.sendPackets(String.valueOf(new S_ServerMessage(1891)));
								return;
							}
						}
						useCashScroll(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100524:
						Deciding_Buff(pc);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100527:
						HEROGAHOBUFF(pc);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
						/** TODO 處理藥水恢復量 **/
					case 4100464:
//				case 4100657:
					case 4100475:
//				case 4100658:
//				case 4100656:
					case 40010:
					case 40011:
					case 40012:
					case 4100021:
					case 40019:
					case 40020:
					case 40021:
					case 40022:
					case 40023:
					case 40024:
					case 4100300:
					case 40026:
					case 40027:
					case 40028:
					case 40043:
					case 40058:
					case 40071:
					case 40506:
					case 40930:
					case 41141:
					case 41337:
					case 60029:
					case 60030:
					case 140010:
					case 140011:
					case 140012:
					case 140506:
					case 240010:
					case 41403:
					case 410000:
					case 410003:
					case 30062:
					case 30056:
					case 42658: // 戰士的體力恢復藥水
					case 40029:
					case 4100152:
					case 4100153:
					case 4100154:
//				case 4100691:
//				case 4100693:
					case 30001866:
					case 30001867:
					case 30001868:
					case 30001869:
					case 30001870:
					case 30001871:
					case 30001872:
						if (pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) {// 使用絕對屏障時無法使用藥水
							return;
						}
						if (itemId >= 4100691 && itemId <= 4100693) {
							if (!pc.get_is_client_auto() || pc.isPinkName() != false) {
								pc.sendPackets("\f3無法使用紫色紋章狀態或只能在遊戲支持期間使用。");
								return;
							}
						}
						if (itemId == 30062) {
							if (!(pc.getMapId() >= 1005 && pc.getMapId() <= 1022
									|| pc.getMapId() > 6000 && pc.getMapId() < 6999)) {
								pc.sendPackets(String.valueOf(new S_ServerMessage(1891)));
								return;
							}
						}

						if (itemId == 30056) {
							if (!(pc.getMapId() >= 2101 && pc.getMapId() <= 2151
									|| pc.getMapId() >= 12152 && pc.getMapId() <= 12200)) {
								pc.sendPackets(String.valueOf(new S_SystemMessage("僅能在特定地區使用。")));
								return;
							}
						}

						L1HealingPotion healingPotion = L1HealingPotion.get(itemId);
						healingPotion.use(pc, l1iteminstance);
						break;
						case 40858:// 酒
							pc.setDrink(true);
							pc.getInventory().removeItem(l1iteminstance, 1);
							break;
						case 708:
							UseExpPotion1(pc, itemId);
							pc.getInventory().removeItem(l1iteminstance, 1);
							break;
						case 3000456:
						case 30105:// 戰鬥藥水
						case 210094:// 天堂藥水
							UseExpPotion(pc, itemId);
							pc.getInventory().removeItem(l1iteminstance, 1);
							break;
						case 4100306:// 真·死亡騎士成長藥水
						UseExpPotionEvent(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 40030:
						useGreenPotion(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100662:
					case 40013:
					case 40018:
					case 40039:
					case 40040:
					case 41338:
					case 41261:
					case 41262:
					case 41268:
					case 41269:
					case 41271:
					case 41272:
					case 41273:
					case 41342:
					case 30067:
					case 140013:
					case 140018:
					case 30158:

						/*
						 * 綠色藥水, 強化綠色藥水, 葡萄酒, 威士忌, 象牙塔的速度增強藥水, 祝福葡萄酒, 飯糰, 雞肉串燒, 披薩片, 烤玉米
						 * 爆米花, 魚糕, 鬆餅, 美杜莎之血, 勇士的速度增強藥水,
						 */

						useGreenPotion(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 7006:
						useGreenPotion(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100663:
					case 40014:
					case 140014:
					case 41415:
						// 勇氣藥水, 福利勇氣藥水, 象牙塔的勇氣藥水
						if (pc.isKnight() || pc.isWarrior() || pc.isCrown() || pc.isFencer() || pc.isLancer()) {
							useBravePotion(pc, itemId);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// 1 什麼事都沒有發生。
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 30073:
						// 勇氣藥水, 福利勇氣藥水, 象牙塔的勇氣藥水
						if (pc.isKnight() || pc.isWarrior() || pc.isCrown() || pc.isFencer() || pc.isLancer()) {
							useBravePotion(pc, itemId);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// 1 什麼事都沒有發生。
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100664:
					case 40068:
					case 140068:
					case 210110:
						// 精靈鬆餅, 福利精靈鬆餅, 象牙塔的精靈鬆餅
						if (pc.isElf()) {
							useBravePotion(pc, itemId);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// 1 什麼事都沒有發生。
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
						case 30076:
							// 精靈鬆餅, 福利精靈鬆餅, 象牙塔的精靈鬆餅
							if (pc.isElf()) {
								useBravePotion(pc, itemId);
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
								// 1 什麼事都沒有發生。
							}
							pc.getInventory().removeItem(l1iteminstance, 1);
							break;
						case 712:
						case 40031:
							// 惡魔之血, 象牙塔的惡魔之血, 福利惡魔之血
							if (pc.isCrown()) {
								useBravePotion(pc, itemId);
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
								// 1 什麼事都沒有發生。
							}
							pc.getInventory().removeItem(l1iteminstance, 1);
							break;
						case 30075:
							// 惡魔之血, 象牙塔的惡魔之血, 福利惡魔之血
						if (pc.isCrown()) {
							useBravePotion(pc, itemId);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// \f1 什麼事都沒有發生.
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100665:
					case 713:
					case 210036:
						// 尤格德拉果實, 象牙塔的尤格德拉果實
						if (pc.isBlackwizard()) {
							useFruit(pc, itemId);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));// 1 什麼事都沒有發生
							// 沒有發生。
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
						case 30077:
						// 尤格德拉果實, 象牙塔的尤格德拉果實
							if (pc.isBlackwizard()) {
								useFruit(pc, itemId);
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(79)));// 1 什麼事都沒有發生
							}
							pc.getInventory().removeItem(l1iteminstance, 1);
							break;
						case 40733:// 榮譽幣
							useBravePotion(pc, itemId);
							pc.getInventory().removeItem(l1iteminstance, 1);
							break;
						case 210115:// 濃縮呼吸藥水
						case 40032:// 伊娃的祝福
						case 40041:// 美人魚的鱗片
						case 41344:// 水之精華
						useBlessOfEva(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
						case 30083:// 藍色藥水
							useBluePotion(pc, itemId);
							pc.getInventory().removeItem(l1iteminstance, 1);
							break;
						case 4100659:
						case 40015:
						case 140015:
						case 40736:// 智慧幣
						case 41142:// 精靈的魔力藥水
						case 210114:// 福利藍色藥水
							useBluePotion(pc, itemId);
							pc.getInventory().removeItem(l1iteminstance, 1);
							break;
						case 30089:// 智慧藥水
						if (pc.isWizard() || pc.isBlackwizard()) {
							useWisdomPotion(pc, itemId);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// \f1 什麼事都沒有發生.
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100660:
					case 40016:
					case 140016:
						if (pc.isWizard() || pc.isBlackwizard()) {
							useWisdomPotion(pc, itemId);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// \f1 什麼事都沒有發生.
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 210113:
						if (pc.isWizard() || pc.isBlackwizard()) {
							useWisdomPotion_Power(pc, itemId);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// \f1 什麼事都沒有發生.
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 40025:// 不透明藥水
						useBlindPotion(pc);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100133:
						pc.sendPackets(new S_ShowPolyList(pc.getId(), "jindeath2017").toString());
						break;
					case 4100500:
					case 4100610:
						/*
						 * pc.polyTrigger(new PolyTrigger() {
						 *
						 * @Override public void onWork() { pc.setPolyRingMaster(false);
						 * pc._PolyMasterCheck = false; } });
						 *
						 * pc.setPolyRingMaster(true); pc._PolyMasterCheck = true;
						 */
						usePolyScroll(pc, itemId, readed_string_1);
						break;
					case 30060:// 精靈變身棒
						pc.sendPackets(new S_ShowPolyList(pc.getId(), "pixies").toString());
						if (!pc.isMagicItem()) {
							pc.setMagicItem(true);
							pc.setMagicItemId(itemId);
						}
						break;
					case 3000421:// 邂逅的變身卷軸
						pc.sendPackets(new S_ShowPolyList(pc.getId(), "tam60").toString());
						if (!pc.isMagicItem()) {
							pc.setMagicItem(true);
							pc.setMagicItemId(itemId);
						}
						break;
					case 600198:
					case 600199:
					case 600200:
					case 600201:
					case 600202:
					case 600203:
					case 600204:
					case 600205:
					case 600206:
					case 600207:
					case 600208:
					case 600209:
					case 600210:
					case 600211:
					case 600212:
					case 600213:
					case 600214:
					case 600215:
					case 600216:
					case 600217:
					case 600218:
						applyEnhanceBuff(pc, itemId, l1iteminstance);
						break;
					case 600223:
					case 3000237:
					case 600225:
					case 3000201:
						usePcBuff(pc, l1iteminstance);
						break;
					/** MJCTSystem **/
					case MJCTLoadManager.CTSYSTEM_STORE_ID:
						MJCTHandler.store(pc, l1iteminstance, readed_int_1);
						break;
					case 4100520:
						ExpMergeController.controller().onMerge(pc, l1iteminstance, readed_int_1);
						break;
						case 600226:// 令人垂涎的成長果實 (3天)
						case 3000235:// 令人垂涎的成長果實 (7天)
						case 600227:// 令人垂涎的成長果實 (30天)
						// System.out.println("objid "+ pc.getId());
						if (pc.getId() == 0) {
							return;
						}
						int day = 0;
						if (itemId == 600226)
							day = 3; // 期間
							if (itemId == 3000235)
							day = 7; // 期間
						if (itemId == 600227)
							day = 30;
							useGrowthFruit(pc, readed_int_1, l1iteminstance, day);
						break;
					case 40096:// 象牙塔的變身卷軸
						if (usePolyScroll(pc, itemId, readed_string_1)) {
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(181)));// \f1 這樣的
						}
						break;
					// case 4100610:
					// if (usePolyScroll(pc, itemId, readed_string_1)) {
					// pc.getInventory().removeItem(l1iteminstance, 1);
					// } else {
					// if (!Config.ServerAdSetting.PolyEvent2)
					// pc.sendPackets(new S_ServerMessage(181));
					// }
					// break;
					case 210112:// 福利變身卷軸
						if (usePolyScroll(pc, itemId, readed_string_1)) {
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							if (!Config.ServerAdSetting.PolyEvent2)
								pc.sendPackets(String.valueOf(new S_ServerMessage(181)));
						}
						break;
					case 3000467:
						if (!Config.ServerAdSetting.PolyEvent) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("僅在變身活動期間可用。")));
							return;
						}
						pc.sendPackets(new S_ShowPolyList(pc.getId()));
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 40088:
						/*
					 {// 變身卷軸
					    final L1PcInstance fpc = pc;
					    final L1ItemInstance fl1iteminstance = l1iteminstance;
					    final int fitemId = itemId;

					    pc.polyTrigger(new PolyTrigger() {
					        @override
					        public void onWork() {
					            pc.getInventory().removeItem(l1iteminstance, 1);
					            // onRemovePolyItem(pc, fl1iteminstance, fitemId);
					        }
					    });

					    System.out.println(1);
					    pc.sendPackets(new S_Ability(7, false));
					    pc.sendPackets(new S_Message_YN(pc.getId(), 180, ""));
					    pc.sendPackets(new S_Ability(7, true));
					    return;
					}*/
					case 140088:// 祝福變身卷軸
						if (usePolyScroll(pc, itemId, readed_string_1)) {
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							if (!Config.ServerAdSetting.PolyEvent2)
								pc.sendPackets(String.valueOf(new S_ServerMessage(181)));// \f1 這樣的
						}
						break;
						/** 排行榜變身卷軸 **/
					case 3000392:
						usePolyRangking(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 3000470:
					case 3000471:
					case 3000472:
					case 3000473:
						usePolyScale4(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
						/** 英雄變身卷軸 **/
					case 4100023:
					case 4100024:
					case 4100025:
					case 4100026:
					case 4100027:
					case 4100028:
					case 4100029:
					case 4100030:
					case 4100031:
					case 4100032:
						usePolyScale3(pc, itemId);
						// pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 3000474:
					case 3000475:
					case 3000476:
					case 3000477:
					case 3000478:
					case 3000479:
						usePolyScale3(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
						/** 傳說、神話變身卷軸 **/
					case 4100160:
					case 4100161:
					case 4100162:
					case 4100163:
						usePolyScale4(pc, itemId);
						break;
						/** 賽變身卷軸 */
					case 220001:
					case 220002:
					case 220003:

					case 3000600:
					case 3000601:
					case 3000602:
					case 3000603:
					case 3000604:
					case 3000605:
					case 3000606:
					case 3000607:
					case 3000608:
					case 3000609:
					case 3000610:
					case 3000611:
					case 3000612:
					case 3000613:
					case 3000614:
					case 3000615:
					case 3000616:
					case 3000617:
					case 3000618:
					case 3000619:

						usePolyScale2(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
						/** 賽變身卷軸 */
						case 41154:// 暗影之鱗
						case 41155:// 烈火之鱗
						case 41156:// 背德之鱗
						case 41157:// 憎惡之鱗
						usePolyScale(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 42655:
						usePolyPotion(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100290:
						usePolyPotion(pc, itemId);
						break;
					case 4100284:
					case 4100285:
					case 4100286:
					case 4100287:
					case 4100288:
					case 4100289:
						case 41143:// 拉巴本頭部變身藥水
						case 41144:// 拉巴本士兵變身藥水
						case 41145:// 拉巴本刀手變身藥水
						case 30057:// 小小雞（藍）變身錘
						case 30058:// 小小雞（黃）變身錘
						case 30059:// 小小雞（粉紅）變身錘
						case 8000: // 真死亡騎士變身
						case 8001: // 真槍騎兵變身
						case 8002: // 82級真死亡騎士變身
						usePolyPotion(pc, itemId);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;

						/*
						 * case 210097:// 薩爾納的變身卷軸 (等級 30)
						 * case 210098:// 薩爾納的變身卷軸 (等級 40)
						 * case 210099:// 薩爾納的變身卷軸 (等級 52)
						 * case 210100:// 薩爾納的變身卷軸 (等級 55)
						 * case 210101:// 薩爾納的變身卷軸 (等級 60)
						 * case 210102:// 薩爾納的變身卷軸 (等級 65)
						 * case 210103:// 薩爾納的變身卷軸 (等級 70)
						 * case 210116:// 薩爾納的變身卷軸 (等級 75)
						 * case 210117:// 薩爾納的變身卷軸 (等級 80)
						 * // case 813: useLevelPolyScroll(pc, itemId);
						 * pc.getInventory().removeItem(l1iteminstance, 1);
						 * break;
						 */

					case 30001887:
					case 30001888:
					case 30001889:
					case 30001890:
					case 30001891:
					case 30001892:
					case 30001893:
					case 30001894:
					case 30001895:
						usePolyCard(pc, itemId);
						break;
						case 40317:// 磨刀石
						case 30087:// 僅限武器或防具
						if (l1iteminstance1.getItem().getType2() != 0 && l1iteminstance1.get_durability() > 0) {
							String msg0;
							if (l1iteminstance1.getItem().getType2() == 2 && l1iteminstance1.isEquipped()) {
								pc.getAC().addAc(-1);
								pc.sendPackets(new S_OwnCharAttrDef(pc));
								pc.getInventory().recoveryDamage(l1iteminstance1);
							} else {
								pc.getInventory().recoveryDamage(l1iteminstance1);
							}
							msg0 = l1iteminstance1.getLogName();
							if (l1iteminstance1.get_durability() == 0) {
								pc.sendPackets(String.valueOf(new S_ServerMessage(464, msg0))); // %0%s
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(463, msg0))); // %0狀態
							}
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 什麼都沒有發生。
							return;
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 849: // 職業變更包
						pc._ClassChange = true;
						pc.sendPackets(String.valueOf(new S_SystemMessage("\\aD請在聊天窗口輸入您要更改的職業。")));
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fH[請在聊天窗口輸入您想要的職業。]"));
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
								"\fH[ \f2王族 \fHor \f2騎士 \fHor \f2戰士 \fHor \f2龍騎士 \fHor \f2黑暗妖精 \fHor \f2妖精 \fHor \f2法師 \fHor \f2幻術師\fHor \f2黃金槍騎 \fH]"));
						break;
					case 30001880: // 樂透
						if (lotto_system.getInstance().lottopossible(pc)) {
							pc._LottoSelect = true;
							pc.sendPackets("請在聊天窗口輸入 1~15 之間的兩個數字 (例如: 3, 6)");
							pc.sendPackets("或輸入 自動 。");
						}
						break;
						case 210073:// 低級奧西里斯的寶箱碎片(下)
						case 210077:// 高級奧西里斯的寶箱碎片(下)
						case 500205:// 低級庫庫爾坎的寶箱碎片(下)
						case 500207: {// 高級庫庫爾坎的寶箱碎片(下)
						int itemId2 = l1iteminstance1.getItem().getItemId();
						if (itemId == 210073 && itemId2 == 210074) {
							if (pc.getInventory().checkItem(210074)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(l1iteminstance, 1);
								pc.getInventory().storeItem(210075, 1);
							}
						} else if (itemId == 210077 && itemId2 == 210078) {
							if (pc.getInventory().checkItem(210078)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(l1iteminstance, 1);
								pc.getInventory().storeItem(210079, 1);
							}
						}
						if (itemId == 500205 && itemId2 == 500204) {
							if (pc.getInventory().checkItem(500204)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(l1iteminstance, 1);
								pc.getInventory().storeItem(500208, 1);
							}
						} else if (itemId == 500207 && itemId2 == 500206) {
							if (pc.getInventory().checkItem(500206)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(l1iteminstance, 1);
								pc.getInventory().storeItem(500209, 1);
							}
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 1 什麼都沒有
																			// 發生。
						}
						}
						break;
						case 40126:// 確認卷軸
						case 40098: {// 象牙塔的確認卷軸
						int add_mpr = l1iteminstance1.getItem().get_addmpr();
						int add_hpr = l1iteminstance1.getItem().get_addhpr();
						int safe_enchant = l1iteminstance1.getItem().get_safeenchant();
						if (!l1iteminstance1.isIdentified()) {
							l1iteminstance1.setIdentified(true);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
						}
						pc.sendPackets(new S_IdentifyDesc(l1iteminstance1));

							// pc.sendPackets("若要查看詳細說明，請按 (CTRL+F) 後點擊物品。");
							pc.getInventory().removeItem(l1iteminstance, 1);
							StringBuffer sb = new StringBuffer();
							if (l1iteminstance1.getItem().getType2() == 1 || l1iteminstance1.getItem().getType2() == 2) {
								if (safe_enchant == -1) {
									sb.append("HP回復: " + add_hpr + " /");
									sb.append("MP回復: " + add_mpr + " /");
									sb.append("基本附魔: 不可能");
								} else if (safe_enchant == 0) {
									sb.append("HP回復: " + add_hpr + " /");
									sb.append("MP回復: " + add_mpr + " /");
									sb.append("基本附魔: 0");
								} else {
									sb.append("HP回復: " + add_hpr + " /");
									sb.append("MP回復: " + add_mpr + " /");
									sb.append("基本附魔: " + safe_enchant + "");
								}
							}
						}
						pc.sendPackets(new S_SystemMessage(sb.toString()));
						sb = null;
					}
					break;
					case 41048:
					case 41049:
					case 41050:
					case 41051:
					case 41052:
					case 41053:
					case 41054:
					case 41055: {
						// 填滿的航海日誌頁面：第 1~8 頁
						int logbookId = l1iteminstance1.getItem().getItemId();
						if (logbookId == (itemId + 8034)) {
							createNewItem(pc, logbookId + 2, 1);
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 1 什麼都沒有
																			// 發生。
						}
					}
					break;

					case 41056:
					case 41057: {
						// 填滿的航海日誌頁面：第 9~10 頁
						int logbookId = l1iteminstance1.getItem().getItemId();
						if (logbookId == (itemId + 8034)) {
							createNewItem(pc, 41058, 1);
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 1 什麼都沒有
																		// 發生。
						}
					}
					break;
					case 40931:
					case 40932:
					case 40933:
					case 40934:
					case 40935:
					case 40936:
					case 40937:
					case 40938:
					case 40939:
					case 40940:
					case 40941:
					case 40942:
						// 加工的寶石類（藍寶石、紅寶石、祖母綠）
						int earing3Id = l1iteminstance1.getItem().getItemId();
						int earinglevel = 0;
						if (earing3Id >= 41161 && 41172 >= earing3Id) {
							// 神秘的耳環類
							if (earing3Id == (itemId + 230)) {
								if ((_random.nextInt(99) + 1) < Config.ServerRates.CreateChanceProcessing) {
									switch (earing3Id) {
										case 41161:
											earinglevel = 21014;
											break;
										case 41162:
											earinglevel = 21006;
											break;
										case 41163:
											earinglevel = 21007;
											break;
										case 41164:
											earinglevel = 21015;
											break;
										case 41165:
											earinglevel = 21009;
											break;
										case 41166:
											earinglevel = 21008;
											break;
										case 41167:
											earinglevel = 21016;
											break;
										case 41168:
											earinglevel = 21012;
											break;
										case 41169:
											earinglevel = 21010;
											break;
										case 41170:
											earinglevel = 21017;
											break;
										case 41171:
											earinglevel = 21013;
											break;
										case 41172:
											earinglevel = 21011;
											break;
									}
									createNewItem(pc, earinglevel, 1);
								} else {
									pc.sendPackets(String.valueOf(new S_ServerMessage(158, l1iteminstance1.getName())));
									// 1%0 沒有蒸發。
								}
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 1 什麼都沒有
																				// 發生。
							}
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 1 什麼都沒有
																		// 發生。
						}
						break;

					case 40943:
					case 40944:
					case 40945:
					case 40946:
					case 40947:
					case 40948:
					case 40949:
					case 40950:
					case 40951:
					case 40952:
					case 40953:
					case 40954:
					case 40955:
					case 40956:
					case 40957:
					case 40958:
						// 加工的鑽石（藍寶石·紅寶石·祖母綠）
						int ringId = l1iteminstance1.getItem().getItemId();
						int ringlevel = 0;
						int gmas = 0;
						int gmam = 0;
						if (ringId >= 41185 && 41200 >= ringId) {
						// 精工戒指類
							if (itemId == 40943 || itemId == 40947 || itemId == 40951 || itemId == 40955) {
								gmas = 443;
								gmam = 447;
							} else if (itemId == 40944 || itemId == 40948 || itemId == 40952 || itemId == 40956) {
								gmas = 442;
								gmam = 446;
							} else if (itemId == 40945 || itemId == 40949 || itemId == 40953 || itemId == 40957) {
								gmas = 441;
								gmam = 445;
							} else if (itemId == 40946 || itemId == 40950 || itemId == 40954 || itemId == 40958) {
								gmas = 444;
								gmam = 448;
							}
							if (ringId == (itemId + 242)) {
								if ((_random.nextInt(99) + 1) < Config.ServerRates.CreateChanceProcessingDiamond) {
									switch (ringId) {
										case 41185:
											ringlevel = 20435;
											break;
										case 41186:
											ringlevel = 20436;
											break;
										case 41187:
											ringlevel = 20437;
											break;
										case 41188:
											ringlevel = 20438;
											break;
										case 41189:
											ringlevel = 20439;
											break;
										case 41190:
											ringlevel = 20440;
											break;
										case 41191:
											ringlevel = 20441;
											break;
										case 41192:
											ringlevel = 20442;
											break;
										case 41193:
											ringlevel = 20443;
											break;
										case 41194:
											ringlevel = 20444;
											break;
										case 41195:
											ringlevel = 20445;
											break;
										case 41196:
											ringlevel = 20446;
											break;
										case 41197:
											ringlevel = 20447;
											break;
										case 41198:
											ringlevel = 20448;
											break;
										case 41199:
											ringlevel = 20449;
											break;
										case 41200:
											ringlevel = 20450;
											break;
									}
									pc.sendPackets(String.valueOf(new S_ServerMessage(gmas, l1iteminstance1.getName())));
									createNewItem(pc, ringlevel, 1);
									pc.getInventory().removeItem(l1iteminstance1, 1);
									pc.getInventory().removeItem(l1iteminstance, 1);
								} else {
									pc.sendPackets(String.valueOf(new S_ServerMessage(gmam, l1iteminstance.getName())));
									pc.getInventory().removeItem(l1iteminstance, 1);
								}
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
								// \f1 什麼都沒有發生。.
							}
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// \f1 什麼事都沒有發生.
						}
						break;

					case 41029: // 召喚球碎片
						int dantesId = l1iteminstance1.getItem().getItemId();
						if (dantesId >= 41030 && 41034 >= dantesId) {
						// 召喚師的核心·各階段
							if ((_random.nextInt(99) + 1) < Config.ServerRates.CreateChanceDantes) {
								createNewItem(pc, dantesId + 1, 1);
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(158, l1iteminstance1.getName())));
								// 1%0 沒有蒸發。
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// 1 什麼都沒有發生。
						}
						break;

					case 40090:
					case 40091:
					case 40092:
					case 40093:
					case 40094: // 空白卷軸（等級 1）~空白卷軸（等級 5）
						if (pc.isWizard()) { // 法師
							if (itemId == 40090 && readed_int_1 <= 7 || // 空白
								// 卷軸（等級 1）可學習 1 級及以下的魔法
									itemId == 40091 && readed_int_1 <= 15 || // 空白
								// 卷軸（等級 2）可學習 2 級及以下的魔法
									itemId == 40092 && readed_int_1 <= 22 || // 空白
								// 卷軸（等級 3）可學習 3 級及以下的魔法
									itemId == 40093 && readed_int_1 <= 31 || // 空白
								// 卷軸（等級 4）可學習 4 級及以下的魔法
									itemId == 40094 && readed_int_1 <= 39) { // 空白
								// 卷軸（等級 5）可學習 5 級及以下的魔法
								L1ItemInstance spellsc = ItemTable.getInstance().createItem(40859 + readed_int_1);
								if (spellsc != null) {
									if (pc.getInventory().checkAddItem(spellsc, 1) == L1Inventory.OK) {
										L1Skills l1skills = SkillsTable.getInstance().getTemplate(readed_int_1 + 1);
										// readed_int_1 從 0 開始
										if (pc.getCurrentHp() + 1 < l1skills.getHpConsume() + 1) {
											pc.sendPackets(String.valueOf(new S_ServerMessage(279)));
											// 1HP不足，無法使用魔法。
											return;
										}
										if (pc.getCurrentMp() < l1skills.getMpConsume()) {
											pc.sendPackets(String.valueOf(new S_ServerMessage(278)));
											return;
										}
										if (l1skills.getItemConsumeId() != 0) {
											if (!pc.getInventory().checkItem(l1skills.getItemConsumeId(),
													l1skills.getItemConsumeCount())) {
												pc.sendPackets(String.valueOf(new S_ServerMessage(299)));
												return;
											}
										}
										pc.setCurrentHp(pc.getCurrentHp() - l1skills.getHpConsume());
										pc.setCurrentMp(pc.getCurrentMp() - l1skills.getMpConsume());
										int lawful = pc.getLawful() + l1skills.getLawful();
										if (lawful > 32767) {
											lawful = 32767;
										}
										if (lawful < -32767) {
											lawful = -32767;
										}
										pc.setLawful(lawful);
										if (l1skills.getItemConsumeId() != 0) {
											pc.getInventory().consumeItem(l1skills.getItemConsumeId(),
													l1skills.getItemConsumeCount());
										}
										pc.getInventory().removeItem(l1iteminstance, 1);
										pc.getInventory().storeItem(spellsc);
										pc.sendPackets(String.valueOf(new S_SystemMessage(spellsc.getName() + " 獲得")));
									}
								}
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(591)));
								// 1卷軸太弱，無法記錄如此強大的魔法。
							}
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(264)));
							// 1此物品無法由您的職業使用。
						}
						break;
					case 40314: // 寵物項圈
					case 40316: // 高級寵物項圈
						if (pc.getInventory().checkItem(41160)) {
							// 召喚的笛子
							if (withdrawPet(pc, itemObjid)) {
								pc.getInventory().consumeItem(41160, 1);
							}
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// 1 什麼事都沒有發生。
						}
						break;

					case 40315: // 寵物哨子
						pc.sendPackets(new S_Sound(437));
						pc.broadcastPacket(new S_Sound(437));
						Object[] petList = pc.getPetList().values().toArray();
						for (Object petObject : petList) {
							if (petObject instanceof L1PetInstance) { // 寵物
								L1PetInstance pet = (L1PetInstance) petObject;
								pet.call();
							}
						}
						break;
					case 40089: // 復活卷軸
					case 140089: // 祝福復活卷軸
					case 30074: // 象牙塔祝福復活卷軸
						if (StadiumManager.getInstance().is_on_stadium(pc.getMapId())) {
							pc.sendPackets("無法使用此區域。");
							return;
						}

						if (L1World.getInstance().findObject(readed_int_1) instanceof L1Character) {
							L1Character resobject = (L1Character) L1World.getInstance().findObject(readed_int_1);
							if (resobject != null) {
								if (resobject instanceof L1PcInstance) {
									L1PcInstance target = (L1PcInstance) resobject;
									if (pc.getId() == target.getId()) {
										return;
									}

									/** 在攻城區域不能復活 **/
									// int castle_id =
									// L1CastleLocation.getCastleIdByArea(pc);
									// if (castle_id != 0) {
									// pc.sendPackets(new
									// S_SystemMessage("無法使用此區域。"));
									// return;
									// }
									if (L1World.getInstance().getVisiblePlayer(target, 0).size() > 0) {
										for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(target, 0)) {
											if (!visiblePc.isDead()) {
												pc.sendPackets(String.valueOf(new S_ServerMessage(592)));
												return;
											}
										}
									}
									if (/* target.getCurrentHp() == 0 && */target.isDead() == true) {
										if (pc.getMap().isUseResurrection()) {
											target.setTempID(pc.getId());
											if (itemId == 40089 || itemId == 30074) {
												// 是否要再次復活？（Y/N）
												target.sendPackets(String.valueOf(new S_Message_YN(321, "")));
											} else if (itemId == 140089) {
												// 是否要再次復活？（Y/N）
												target.sendPackets(String.valueOf(new S_Message_YN(322, "")));
											}
										} else {
											return;
										}
									}
								} else if (resobject instanceof L1NpcInstance) {
									if (!(resobject instanceof L1TowerInstance)) {
										L1NpcInstance npc = (L1NpcInstance) resobject;
										int npcId = npc.getNpcId();
										if (npcId == 7320052 || npcId == 45021 || npcId == 45022 || npcId == 45040
												|| npcId == 45048 || npcId == 44999 || npcId == 44997 || npcId == 44998) {
											pc.sendPackets("無法復活的怪物。");
											return;
										}

										if ((npc instanceof L1PetInstance || npc instanceof MJCompanionInstance)
												&& L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
											for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
												if (!visiblePc.isDead()) {
													pc.sendPackets(String.valueOf(new S_ServerMessage(592)));
													return;
												}
											}
										} else if (npc.getNpcTemplate().isCantResurrect()
												&& !(npc instanceof L1PetInstance || npc instanceof MJCompanionInstance)) {
											pc.getInventory().removeItem(l1iteminstance, 1);
											return;
										}
										if ((npc instanceof L1PetInstance || npc instanceof MJCompanionInstance)
												&& L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
											for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
												if (!visiblePc.isDead()) {
													// 1由於有人站在那個位置，無法復活。
													pc.sendPackets(String.valueOf(new S_ServerMessage(592)));
													return;
												}
											}
										}
										if (npc.getCurrentHp() == 0 && npc.isDead()) {
											npc.resurrect(npc.getMaxHp() / 4);
											npc.setResurrect(true);
										}
									}
								}
							}
							pc.getInventory().removeItem(l1iteminstance, 1);
						}

						break;
					case 40095: // 騎士團的回歸卷軸
						if (pc.get_client_auto_type() == 1) { // 周邊
							pc.do_finish_client_auto_ack();
						}
						if (pc.getMap().isEscapable() || pc.isGm()) {
							int objid = pc.getId();
							int[] loc = null;
							loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_TALKING_ISLAND);
							pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true, true);
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
						}
						cancelAbsoluteBarrier(pc); // 解除吸收屏障
						break;
					case 4100667:
						if (pc.getMap().isEscapable() || pc.isGm()) {
							int newX = 33632 + 3;
							int newY = 32791 + 3;
							short mapId = 15482;

							pc.send_effect(12261, true);
							SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, newX, newY, mapId,
									SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
						}
						cancelAbsoluteBarrier(pc); // 解除吸收屏障
						break;
					case 40079:
					case 40521: // 回城卷軸
						if (pc.get_client_auto_type() == 1) { // 周邊
							pc.do_finish_client_auto_ack();
						}
						if (pc.getMap().isEscapable() || pc.isGm()) {
							int[] loc = Getback.GetBack_Location(pc, false);
							pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true, false);
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
						}
						cancelAbsoluteBarrier(pc); // 解除吸收屏障
						break;
					case 30001881: // 榮耀的根據地鑰匙
						SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().get(pc.getId());
						if (!pc.isGm()) {
							if (noti.get_total_ranker().get_rank() != 1) {
								pc.sendPackets("如果不是排名第一，無法使用。");
								return;
							}
						}
						if (pc.get_client_auto_type() == 1) { // 周邊
							pc.do_finish_client_auto_ack();
						}
						if (pc.getMap().isEscapable() || pc.isGm()) { // 放置地圖號碼
//						x,y,mapid
							pc.start_teleport(32773, 32828, 2237, pc.getHeading(), 18339, true, false);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
						}
						cancelAbsoluteBarrier(pc); // 解除吸收屏障
						break;
					case 40117: // 銀騎士村回城卷軸
						break;
					case 40099: // 象牙塔瞬間移動卷軸
						Telbookitem.clickItem(pc, itemId, readed_short_2, readed_short_3, (short) readed_short_1, l1iteminstance);
						break;
					case 40100: // 瞬間移動卷軸
					case 40086: // 群體傳送卷軸
					case 40863: // 魔法卷軸 (傳送)
						Telbookitem.clickItem(pc, itemId, readed_short_1, readed_short_2, (short) readed_int_1, l1iteminstance);
						break;
					case 140100: // 祝福瞬間移動卷軸
						Telbookitem.clickItem(pc, itemId, readed_short_2, readed_short_3, (short) readed_short_1, l1iteminstance);
						break;
					/** 傲慢之塔移動卷軸 **/
					case 830001:
					case 830002:
					case 830003:
					case 830004:
					case 830005:
					case 830006:
					case 830007:
					case 830008:
					case 830009:
					case 830010:
					case 830011:
						omanTel.clickItem(pc, itemId, l1iteminstance);
						break;
					/** 傲慢之塔移動護符、支配護符 **/
					case 830012:
					case 830013:
					case 830014:
					case 830015:
					case 830016:
					case 830017:
					case 830018:
					case 830019:
					case 830020:
					case 830021:
					case 830022:
					case 830023:
					case 830024:
					case 830025:
					case 830026:
					case 830027:
					case 830028:
					case 830029:
					case 830030:
					case 830031:
						OmanAmulet.clickItem(pc, itemId, l1iteminstance);
						break;
					case 40901:
					case 40902:
					case 40903:
					case 40904:
					case 40905:
					case 40906:
					case 40907:
					case 40908: { // 各種訂婚戒指
						L1PcInstance partner = null;
						boolean partner_stat = false;
						if (pc.getHellTime() > 0) {
							pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "在地獄中無法使用。")));
							return;
						}
						int chargeCount = l1iteminstance.getChargeCount();
						if (pc.getPartnerId() != 0) { // 已婚
							partner = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
							if (chargeCount > 0) {
								if (partner != null && partner.getPartnerId() != 0 && pc.getPartnerId() == partner.getId()
										&& partner.getPartnerId() == pc.getId()) {
									partner_stat = true;
									l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
									pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
								}
							}
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(662))); // 1你尚未結婚。
							return;
						}

						if (partner_stat) {
							boolean castle_area = L1CastleLocation.checkInAllWarArea(
									// 幾個
									// 城堡區域
									partner.getX(), partner.getY(), partner.getMapId());
							if (castle_area == true || partner.isDead() || partner.getMapId() == 603
									|| partner.getMapId() == 255 || partner.getMapId() == 777 || partner.getMapId() == 778
									|| partner.getMapId() == 39 || partner.getMapId() == 5167 || partner.getMapId() == 5153
									|| partner.getMapId() == 5001 || (partner.getMapId() > 190 && partner.getMapId() < 201)
									|| (partner.getMapId() > 255 && partner.getMapId() < 260) || partner.getMapId() == 23
									|| partner.getMapId() == 5153 || partner.getMapId() == 5001 || partner.getMapId() == 24
									|| (partner.getMapId() > 239 && partner.getMapId() < 244)
									|| (partner.getMapId() > 247 && partner.getMapId() < 252)
									|| (partner.getMapId() > 280 && partner.getMapId() < 289)
									|| (partner.getMapId() > 1 && partner.getMapId() < 2)
									|| (partner.getMapId() > 1700 && partner.getMapId() < 1712)) {
								pc.sendPackets(String.valueOf(new S_SystemMessage("你的伴侶已經死亡或在無法到達的地方。")));
							}
						} else if (l1iteminstance.getChargeCount() > 0) {
							pc.sendPackets(String.valueOf(new S_ServerMessage(546)));
							// 1你的伴侶現在沒有在遊戲中。
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// 1什麼事都沒有發生。
						}
					}
					break;

					case 40555:// 秘密房間的鑰匙
						if (pc.isKnight() && (pc.getX() >= 32806 && pc.getX() <= 32814)
								&& (pc.getY() >= 32798 && pc.getY() <= 32807) && pc.getMapId() == 13) {
							// short mapid = 13;
							// L1Teleport.teleport(pc, 32815, 32810, mapid, 5,
							// false);
							pc.start_teleport(32815, 32810, 13, 5, 18339, false, false);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							// 什麼事都沒有發生.
						}
						break;

					case 40572: // 刺客的證明
						if (pc.getX() == 32778 && pc.getY() == 32738 && pc.getMapId() == 21) {
							// L1Teleport.teleport(pc, 32781, 32728, (short) 21, 5,
							// true);
							pc.start_teleport(32781, 32728, 21, 5, 18339, true, false);
						} else if (pc.getX() == 32781 && pc.getY() == 32728 && pc.getMapId() == 21) {
							// L1Teleport.teleport(pc, 32778, 32738, (short) 21, 5,
							// true);
							pc.start_teleport(32778, 32738, 21, 5, 18339, true, false);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
						}
						break;
					case 700022:// 記憶擴展珠
						if (pc.getMark_count() < 100) {
							int booksize = pc.getMark_count() + 10;
							pc.setMark_count(booksize);
							pc.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK_SIZE_PLUS_10, booksize));
							pc.getInventory().removeItem(l1iteminstance, 1);
							pc.save();
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(2930)));
						}
						break;
					case 40420:
					case 40421:
					case 40422:
					case 40423: // 煉金術士之石
						pc.sendPackets(String.valueOf(new S_UserCommands5(1)));
						if (!pc.getInventory().checkItem(40420, 1)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("缺少古代人的咒術書第1冊。")));
							return;
						} else if (!pc.getInventory().checkItem(40421, 1)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("缺少古代人的咒術書第2冊。")));
							return;

						} else if (!pc.getInventory().checkItem(40422, 1)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("缺少古代人的咒術書第3冊。")));
							return;

						} else if (!pc.getInventory().checkItem(40423, 1)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("缺少古代人的咒術書第4冊。")));
							return;
						} else {
							pc.getInventory().consumeItem(40420, 1);
							pc.getInventory().consumeItem(40421, 1);
							pc.getInventory().consumeItem(40422, 1);
							pc.getInventory().consumeItem(40423, 1);
							pc.getInventory().storeItem(702, 1);
							pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "煉金術士之石已經製作完成。")));
						}
						break;
					case 4100595:
					case 4100596:
					case 4100597:
					case 4100598:
						if (!pc.getInventory().checkItem(4100595, 1)) {
							pc.sendPackets("缺少重置卡片中的 [重]。");
							return;
						} else if (!pc.getInventory().checkItem(4100596, 1)) {
							pc.sendPackets("缺少重置卡片中的 [置]。");
							return;
						} else if (!pc.getInventory().checkItem(4100597, 1)) {
							pc.sendPackets("缺少重置卡片中的 [卡]。");
							return;
						} else if (!pc.getInventory().checkItem(4100598, 1)) {
							pc.sendPackets("缺少重置卡片中的 [片]。");
							return;
						} else {
							pc.getInventory().consumeItem(4100595, 1);
							pc.getInventory().consumeItem(4100596, 1);
							pc.getInventory().consumeItem(4100597, 1);
							pc.getInventory().consumeItem(4100598, 1);
							pc.getInventory().storeItem(4100599, 1, true);
							pc.send_effect(2048, false);
							pc.sendPackets("\f3重置卡片已組合，(天堂重置禮物箱)已發放。");
						}
						break;
					case 3000455:
					case 30104: {
						int[] allBuffSkill = { 50007 };
						L1SkillUse l1skilluse = new L1SkillUse();
						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF /*
									 * L1SkillUse.TYPE_SPELLSC
									 */);
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					break;
					case 3000508:
						additem2.clickItem(pc, itemId, l1iteminstance);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100080:
						additem2.clickItem(pc, itemId, l1iteminstance);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100164:
					case 4100165:
						additem.clickItem(pc, itemId, l1iteminstance);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 30144: // LV52 精靈的祝賀禮物
						if (pc.getInventory().getSize() > 120) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("攜帶的物品過多。")));
							return;
						}
						if (pc.getInventory().getWeight100() > 82) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("物品過重，無法使用。")));
							return;
						}
						if (pc.getInventory().checkItem(30144, 1)) {
							pc.getInventory().consumeItem(30144, 1);
							pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 8473)));

							createNewItem2(pc, 60032, 1, 0); // 破舊的古書
							createNewItem2(pc, 200000, 1, 0); // 回憶的蠟燭

							if (pc.isCrown()) {
								createNewItem2(pc, 51, 1, 0); // 黃金指揮棒
								createNewItem2(pc, 20051, 1, 0); // 君主的威嚴
								createNewItem2(pc, 40226, 1, 0); // 真正的目標
							}
							if (pc.isKnight()) {
								createNewItem2(pc, 56, 1, 0); // 死亡之刃
								createNewItem2(pc, 20318, 1, 0); // 勇氣腰帶
							}
							if (pc.isWizard()) {
								createNewItem2(pc, 20225, 1, 0); // 魔力水晶球
								createNewItem2(pc, 20055, 1, 0); // 魔力斗篷
								createNewItem2(pc, 40188, 1, 0); // 加速術
								createNewItem2(pc, 40170, 1, 0); // 火球術
								createNewItem2(pc, 40176, 1, 0); // 冥想
							}
							if (pc.isElf()) {
								createNewItem2(pc, 50, 1, 0); // 火焰之劍
								createNewItem2(pc, 184, 1, 0); // 火焰之弓
								createNewItem2(pc, 40243, 1, 0); // 召喚次級元素
								createNewItem2(pc, 40240, 1, 0); // 三重箭
								createNewItem2(pc, 40233, 1, 0); // 身心轉換
								createNewItem2(pc, 40234, 1, 0); // 傳送至母親樹
							}
							if (pc.isDarkelf()) {
								createNewItem2(pc, 13, 1, 0); // 死亡之指
								createNewItem2(pc, 20195, 1, 0); // 暗影之靴
								createNewItem2(pc, 40276, 1, 0); // 奇異躲避
								createNewItem2(pc, 40270, 1, 0); // 加速移動
								createNewItem2(pc, 40268, 1, 0); // 召喚石
							}
							if (pc.isDragonknight()) {
								createNewItem2(pc, 500, 1, 0); // 滅亡者鏈劍
								createNewItem2(pc, 22001, 1, 0); // 龍鱗護腰
								createNewItem2(pc, 210025, 1, 0); // 血之渴望
								createNewItem2(pc, 210026, 1, 0); // 屠殺者
								createNewItem2(pc, 210020, 1, 0); // 龍之皮膚
								createNewItem2(pc, 210021, 1, 0); // 燃燒一擊
							}
							if (pc.isBlackwizard()) {
								createNewItem2(pc, 503, 1, 0); // 藍寶石鑰匙
								createNewItem2(pc, 22006, 1, 0); // 幻術師魔法書
								createNewItem2(pc, 210014, 1, 0); // 立方體(震撼)
								createNewItem2(pc, 210004, 1, 0); // 立方體(燃燒)
								createNewItem2(pc, 210000, 1, 0); // 鏡像
								createNewItem2(pc, 210001, 1, 0); // 混亂
							}
							if (pc.isWarrior()) {
								createNewItem2(pc, 22365, 1, 0); // 戰士團頭盔
								createNewItem2(pc, 203014, 1, 0); // 鐵匠之斧
								createNewItem2(pc, 210126, 1, 0); // 戰士印章(衝擊)
								createNewItem2(pc, 210121, 1, 0); // 戰士印章(咆哮)
								createNewItem2(pc, 210128, 1, 0); // 戰士印章(屠殺)
							}
						}
						break;
					case 30127: // 52級任務物品箱
						if (pc.getInventory().getSize() > 120) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("攜帶的物品過多。")));
							return;
						}
						if (pc.getInventory().getWeight100() > 82) { // 28 == 100%
							pc.sendPackets(String.valueOf(new S_SystemMessage("物品過重，無法使用。")));
							return;
						}
						if (pc.getInventory().checkItem(30127, 1)) {
							pc.getInventory().consumeItem(30127, 1);
							pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 8473)));

							if (pc.isCrown()) {
								createNewItem2(pc, 51, 1, 0);
							}
							if (pc.isKnight()) {
								createNewItem2(pc, 56, 1, 0);
							}
							if (pc.isWizard()) {
								createNewItem2(pc, 20225, 1, 0);
							}
							if (pc.isElf()) {
								createNewItem2(pc, 184, 1, 0);
							}
							if (pc.isDarkelf()) {
								createNewItem2(pc, 13, 1, 0);
							}
							if (pc.isDragonknight()) {
								createNewItem2(pc, 500, 1, 0);
							}
							if (pc.isBlackwizard()) {
								createNewItem2(pc, 503, 1, 0);
							}
							if (pc.isWarrior()) {
								createNewItem2(pc, 22365, 1, 0);
							}
						}
						break;
					case 30124:// 炮彈
						pc.sendPackets(String.valueOf(new S_SystemMessage("攻城戰時使用投石機的消耗性物品")));
						break;

					/*
					 * case 500219:// 巴洛克奉獻書(友好度)
					 * if (pc.getKarma() <= 10000000) {
					 * pc.addKarma((int) (+15000 * Config.ServerRates.RateKarma));
					 * pc.sendPackets(new S_Karma(pc));
					 * pc.sendPackets(new S_SystemMessage(pc.getName() + "的友好度提高了。"));
					 * // pc.sendPackets(new //
					 * S_PacketBox(S_PacketBox.GREEN_MESSAGE,"按Ctrl+A並在第四個窗口查看當前的友好狀態。"));
					 * pc.getInventory().removeItem(l1iteminstance, 1);
					 * } else
					 * pc.sendPackets(new S_ServerMessage(79));
					 * break;
					 */
					case 500218:// 耶希奉獻書(友好度)
						if (pc.getKarma() >= -10000000) {
							pc.addKarma((int) (-15000 * Config.ServerRates.RateKarma));
							pc.sendPackets(new S_Karma(pc));
							pc.sendPackets(String.valueOf(new S_SystemMessage(pc.getName() + "的友好度提高了。")));
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
						break;
					case 1000010:// 象牙塔套裝箱
						pc.getInventory().removeItem(l1iteminstance, 1);
						Beginner.getInstance().GiveItemToActivePc(pc);
						break;
					case 40008:
					case 140008:
					case 40410:// 楓葉棒
						if (pc.getMapId() == 552 || pc.getMapId() == 555 || pc.getMapId() == 557 || pc.getMapId() == 558
								|| pc.getMapId() == 779) {
							pc.sendPackets(563);
						} else {
							// temp
							final L1PcInstance fpc = pc;
							final L1ItemInstance fl1iteminstance = l1iteminstance;
							final int fitemId = itemId;
							L1Object obj = L1World.getInstance().findObject(readed_int_1);
							if (obj == null)
								return;

							if (obj instanceof L1PcInstance) {
								if (pc.getInventory().checkItem(4100500) && obj.getId() == pc.getId()) {
									pc.polyTrigger(new PolyTrigger() {
										@Override
										public void onWork() {
											onRemovePolyItem(pc, fl1iteminstance, fitemId);
										}
									});
									pc.sendPackets(String.valueOf(new S_Message_YN(pc.getId(), 180, "")));
									return;
								}
							}
							onPoly(pc, l1iteminstance, itemId, readed_int_1, readed_string_1);
						}
						break;
					case 40289:
					case 40290:
					case 40291:
					case 40292:
					case 40293:
					case 40294:
					case 40295:
					case 40296:
					case 40297:
						// 傲慢之塔傳送護身符11~91
						useToiTeleportAmulet(pc, itemId, l1iteminstance);
						break;
					case 34103:
					case 34104:
					case 34105:
						AinhasadBlessing(pc, itemId, l1iteminstance);
						break;
					case 40280:
					case 40281:
					case 40282:
					case 40283:
					case 40284:
					case 40285:
					case 40286:
					case 40287:
					case 40288:
						// 封印的傲慢之塔傳送護身符 11~91層
						pc.getInventory().removeItem(l1iteminstance, 1);
						L1ItemInstance item3 = pc.getInventory().storeItem(itemId + 9, 1);
						if (item3 != null) {
							pc.sendPackets(String.valueOf(new S_ServerMessage(403, item3.getLogName())));
						}
						break;
					case 830032:
					case 830033:
					case 830034:
					case 830035:
					case 830036:
					case 830037:
					case 830038:
					case 830039:
					case 830040:
					case 830041:
						// System.out.println(String.format("%s玩家 護身符解封 - (護身符:%s[%d])",
						// pc.getName(), l1iteminstance.getName(),
						// l1iteminstance.getItemId()));
						// 封印的傲慢之塔傳送護身符 1~10層
						pc.getInventory().removeItem(l1iteminstance, 1);
						L1ItemInstance item1 = pc.getInventory().storeItem(itemId - 20, 1);
						if (item1 != null) {
							pc.sendPackets(String.valueOf(new S_ServerMessage(403, item1.getLogName())));
						}
						break;

					case 40056:
					case 40057:
					case 40059:
					case 40060:
					case 40061:
					case 40062:
					case 40063:
					case 40064:
					case 40065:
					case 40069:
					case 40072:
					case 40073:
					case 41297:
					case 49092:
					case 41266:
					case 41267:
					case 41274:
					case 41275:
					case 41276:
					case 41252:
					case 49040:
					case 49041:
					case 49042:
					case 49043:
					case 49044:
					case 49045:
					case 49046:
					case 49047:
					case 140061:
					case 140062:
					case 140065:
					case 140069:
					case 140072:
					case 410056:
					case 210039:
					case 4100654:
					case 30085:
						pc.getInventory().removeItem(l1iteminstance, 1);
						// XXX 每種食物的飽腹度沒有差異
						if (itemId == 40057) { // 漂浮眼肉
							pc.setSkillEffect(STATUS_FLOATING_EYE, 0);
							if (pc.hasSkillEffect(CURSE_BLIND) || pc.hasSkillEffect(DARKNESS)
									|| pc.hasSkillEffect(LINDBIOR_SPIRIT_EFFECT)
									|| pc.hasSkillEffect(L1SkillId.INVISIBILITY)
									|| pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
								pc.sendPackets(String.valueOf(new S_CurseBlind(2)));
							}
						}
						if (pc.get_food() < 225) {
							pc.set_food(pc.get_food() + 10);
							if (itemId == 210039 || itemId == 30085 || itemId == 4100654) { // 허브
								pc.set_food(pc.get_food() + 90);
							}
							int foodvolume = (l1iteminstance.getItem().getFoodVolume() / 10);
							pc.add_food(foodvolume <= 0 ? 5 : foodvolume);
							pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
							pc.sendPackets(String.valueOf(new S_ServerMessage(76, l1iteminstance.getItem().getNameId())));
						}
						break;
					case 40070:// 進化之果
						pc.sendPackets(String.valueOf(new S_ServerMessage(76, l1iteminstance.getLogName())));
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 408991:
						if (pc.is_combat_field() || !pc.getSafetyZone()
								|| StadiumManager.getInstance().is_on_stadium(pc.getMapId())) {
							pc.sendPackets("在該地區無法使用。");
							return;
						}

						if (!pc.getInventory().checkItem(408991)) {
							return;
						}

						if (pc.getClanid() != 0) {
							pc.sendPackets("請先退出血盟。");
							return;
						}

						if (MJInstanceSpace.isInInstance(pc)) {
							if (pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE) {
								pc.sendPackets("在該地區無法使用。");
								return;
							}
						}

							// pc.getInventory().removeItem(l1iteminstance, 1);
						String message = String.format("\f3正在移動到角色名更改窗口，請稍候。");
						String message2 = String.format("\f3正在移動到角色名更改窗口，請稍候。");
						L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { new S_SystemMessage(message),
								new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message2) });
						pc.sendPackets(String.valueOf(new S_DisplayEffect(S_DisplayEffect.BLACK_DISPLAY)));
						pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true)));
						int locx = 32723 + CommonUtil.random(5);
						int locy = 32851 + CommonUtil.random(5);
						int[] loc = null;
						loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
						pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true, false);
						GeneralThreadPool.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								int random_name = MJRnd.next(1000000000, 2000000000);
								String new_name = "_L" + random_name;
								ServerBasePacket packet = S_ChangeCharName.doChangeCharName(client, pc.getName(), new_name,
										false);
								if (packet != null)
									client.sendPacket(packet);
								Account acc = client.getAccount();
								client.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
								if (acc.countCharacters() > 0) {
									C_CommonClick.sendCharPacks(client);
								}
							}
						}, 2500L);
						break;
					case 41146:// 德羅蒙德的邀請函
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei001")));
						break;
					case 41209:// 波菲蕾亞的委託書
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei002")));
						break;
					case 41210:// 研磨材
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei003")));
						break;
					case 41211:// 草藥
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei004")));
						break;
					case 41212:// 特製糖果
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei005")));
						break;
					case 41213:// 提米的籃子
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei006")));
						break;
					case 41214:// 運之證據
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei012")));
						break;
					case 41215:// 智之證據
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei010")));
						break;
					case 41216:// 力之證據
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei011")));
						break;
					case 41222:// 瑪修爾
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei008")));
						break;
					case 41223:// 武器的碎片
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei007")));
						break;
					case 41224:// 徽章
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei009")));
						break;
					case 41225:// 凱斯金的訂購單
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei013")));
						break;
					case 41226:// 帕戈的藥
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei014")));
						break;
					case 41227:// 亞歷克斯的介紹信
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei033")));
						break;
					case 41228:// 律法博士的護身符
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei034")));
						break;
					case 41229:// 骷髏頭
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei025")));
						break;
					case 41230:// 吉娜的信
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei020")));
						break;
					case 41231:// 馬蒂的信
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei021")));
						break;
					case 41233:// 凱伊的信
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei019")));
						break;
					case 41234: // 裝有骨頭的信封
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei023")));
						break;
					case 41235:// 材料表
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei024")));
						break;
					case 41236:// 博納恰的骨頭
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei026")));
						break;
					case 41237:// 骷髏尖刺的骨頭
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei027")));
						break;
					case 41239:// 布特的信
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei018")));
						break;
					case 41240:// 佩達的信
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "ei022")));
						break;
					case 41060:// 諾納梅的推薦信
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "nonames")));
						break;
					case 41061:// 調查團的證書：精靈地區 杜達瑪拉卡梅
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "kames")));
						break;
					case 41062:// 調查團的證書：人類地區 內爾加巴科莫
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "bakumos")));
						break;
					case 41063:// 調查團的證書：精靈地區 杜達瑪拉布卡
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "bukas")));
						break;
					case 41064:// 調查團的證書：半獸人地區 內爾加胡烏莫
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "huwoomos")));
						break;
					case 41065:// 調查團的證書：調查團長 阿特巴諾亞
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "noas")));
						break;
					case 41356:// 帕魯姆的資源列表
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "rparum3")));
						break;
					case 40701:// 小寶藏地圖
						if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 1) {
							pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "firsttmap")));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 2) {
							pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "secondtmapa")));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 3) {
							pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "secondtmapb")));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 4) {
							pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "secondtmapc")));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 5) {
							pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "thirdtmapd")));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 6) {
							pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "thirdtmape")));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 7) {
							pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "thirdtmapf")));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 8) {
							pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "thirdtmapg")));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 9) {
							pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "thirdtmaph")));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 10) {
							pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "thirdtmapi")));
						}
						break;
					case 40663:// 兒子的信
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "sonsletter")));
						break;
					case 40630:// 迭戈的舊日記
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "diegodiary")));
						break;
					case 41340:// 傭兵團長提恩
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "tion")));
						break;
					case 41317:// 蘭爾森的推薦信
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "rarson")));
						break;
					case 41318:// 庫恩的便箋
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "kuen")));
						break;
					case 41329:// 標本製作委托書
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "anirequest")));
						break;
					case 41346:// 羅賓漢的便箋 1
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "robinscroll")));
						break;
					case 41347:// 羅賓漢的便箋 2
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "robinscroll2")));
						break;
					case 41348:// 羅賓漢的介紹信
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "robinhood")));
						break;
					case 41007:
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "erisscroll")));
						break;
					case 41009:
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "erisscroll2")));
						break;
					case 41019:
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "lashistory1")));
						break;
					case 41020:
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "lashistory2")));
						break;
					case 41021:
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "lashistory3")));
						break;
					case 41022:
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "lashistory4")));
						break;
					case 41023:
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "lashistory5")));
						break;
					case 41024:
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "lashistory6")));
						break;
					case 41025:
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "lashistory7")));
						break;
					case 41026:
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "lashistory8")));
						break;
					case 210087:
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "first_p")));
						break;
					case 210093:// 希爾蕾恩的第一封信
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "silrein1lt")));
						break;
					case 410106:// 哈丁的日記 11月10日
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "j_ep0s00")));
						break;
					case 410101:// 哈丁的日記:6月2日
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "j_ep0s01")));
						break;
					case 410103:// 哈丁的日記:8月9日
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "j_ep0s02")));
						break;
					case 410105:// 哈丁的日記:10月12日
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "j_ep0s03")));
						break;
					case 410098:// 哈丁的日記:2月24日
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "j_ep0s04")));
						break;
					case 410099:// 哈丁的日記:2月25日
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "j_ep0s05")));
						break;
					case 410100:// 哈丁的日記:5月5日
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "j_ep0s06")));
						break;
					case 410097:// 哈丁的日記:1月1日
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "j_ep0s07")));
						break;
					case 410102:// 哈丁的日記:6月9日
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "j_ep0s08")));
						break;
					case 410104:// 哈丁的日記:8月19日
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "j_ep0s09")));
						break;
					case 410107:// 黑暗哈丁的日記
						pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "j_ep0s10")));
						break;

					case 40615:// 影子神殿二樓的鑰匙
						if ((pc.getX() >= 32701 && pc.getX() <= 32705) && (pc.getY() >= 32894 && pc.getY() <= 32898)
								&& pc.getMapId() == 522) { // 影子神殿
							pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(),
									l1iteminstance.getItem().get_mapid(), 5, 18339, true, false);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
						}
						break;
					case 42501:// 風暴行走
						pc.start_teleport(readed_short_1, readed_short_2, pc.getMapId(), pc.getHeading(), 18339, false,
								false);
						pc.send_effect(2235, false);// 2235 124461
						break;
					case 41293:
					case 41294:
					case 41305:
					case 41306:
					case 4100293:
					case 600229:
					case 87058:
					case 87059:
					case 9991:// 釣魚竿
						Fishing.clickItem(pc, l1iteminstance, readed_short_1, readed_short_2);
						break;
					case 7024: {
						long curtime = System.currentTimeMillis() / 1000;
						if (pc.getQuizTime() + 5 > curtime) {
							long time = (pc.getQuizTime() + 5) - curtime;
							pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), time + " 秒後可以使用。")));
							return;
						}
						int i = 1;
						if (pc.isWatchingSentence) {
							i = 3;
							pc.isWatchingSentence = false;
						} else {
							pc.isWatchingSentence = true;
							// 顯示或關閉所有血盟的標記。
							pc.sendPackets(String.valueOf(new S_SystemMessage("所有血盟的標記已顯示或關閉。")));
						for (L1Clan clan : L1World.getInstance().getAllClans()) {
							if (clan != null) {
								pc.sendPackets(String.valueOf(new S_War(i, pc.getClanname(), clan.getClanName())));
							}
							pc.setQuizTime(curtime);
						}
					}
					break;
						// case 200000: { // -- 回憶的蠟燭
						// if (!pc.getMap().isSafetyZone(pc.getLocation())) {
						// pc.sendPackets(new S_SystemMessage("只能在安全區域使用。"));
						// return;
						// }
						// if (pc.getLevel() != pc.getHighLevel()) {
						// pc.sendPackets(new S_SystemMessage("角色等級已經降低，請升級後再使用。"));
						// return;
						// }
						// if (pc.getLevel() > 54) {
						// pc.getInventory().consumeItem(200000, 1);
						// int locx2 = 32723 + CommonUtil.random(10);
						// int locy2 = 32851 + CommonUtil.random(10);
						// pc.start_teleport(locx2, locy2, 5166, 5, 169, true, false);
						// // pc.setStatReset(true);
						// pc.resetStats(); // 스텟초기화
						// } else {
						// pc.sendPackets(new S_SystemMessage("只有55級以上可以重置屬性。"));
						// return;
						// }
						// }
						// break;
						/** 特殊附魔系統 **/
						case 300000: { // 特殊附魔卷軸
							if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() == 0
									|| l1iteminstance1.getItem().getType2() == 2) {
								pc.sendPackets("只能用於武器。");
							return;
						}

						if (!(l1iteminstance1.getItemId() == 12 || l1iteminstance1.getItemId() == 61
								|| l1iteminstance1.getItemId() == 86 || l1iteminstance1.getItemId() == 66
								|| l1iteminstance1.getItemId() == 134 || l1iteminstance1.getItemId() == 7000262
								|| l1iteminstance1.getItemId() == 7000263 || l1iteminstance1.getItemId() == 7000238
								|| l1iteminstance1.getItemId() == 7000239 || l1iteminstance1.getItemId() == 7000240
								|| l1iteminstance1.getItemId() == 7000264 || l1iteminstance1.getItemId() == 203065
								|| l1iteminstance1.getItemId() == 7000264
								|| l1iteminstance1.getItemId() >= 202011 && l1iteminstance1.getItemId() <= 202014)) {
							pc.sendPackets("只能用於傳說級武器。");
							return;
						}

							if (l1iteminstance1.getEnchantLevel() < 5 && l1iteminstance1.get_item_level() == 0) { // 機會
								pc.sendPackets("\\aG[1階段]只能用於武器附魔+5以上。");
								return;
							} else if (l1iteminstance1.getEnchantLevel() < 7 && l1iteminstance1.get_item_level() == 1) { // 機會
								pc.sendPackets("\\aG[2階段]只能用於武器附魔+7以上。");
								return;
							} else if (l1iteminstance1.getEnchantLevel() < 8 && l1iteminstance1.get_item_level() == 2) { // 機會
								pc.sendPackets("\\aG[3階段]只能用於武器附魔+8以上。");
								return;
							} else if (l1iteminstance1.getEnchantLevel() < 9 && l1iteminstance1.get_item_level() == 3) { // 機會
								pc.sendPackets("\\aG[4階段]只能用於武器附魔+9以上。");
								return;
							} else if (l1iteminstance1.get_item_level() == 4) {
								pc.sendPackets("\\aG4階段以上無法進行強化。");
							return;
						}

						int random = CommonUtil.random(100);
						if (l1iteminstance1.getEnchantLevel() >= 5 && l1iteminstance1.get_item_level() == 0
								&& random < Config.DollEnchant.WeaponEnchantPerlvl1) { // 機會
							if (l1iteminstance1.getItem().getType2() == 1) {
								l1iteminstance1.set_item_level(1);
							}
						} else if (l1iteminstance1.getEnchantLevel() >= 7 && l1iteminstance1.get_item_level() == 1
								&& random < Config.DollEnchant.WeaponEnchantPerlvl2) { // 機會
							if (l1iteminstance1.getItem().getType2() == 1) {
								l1iteminstance1.set_item_level(2);
							}
						} else if (l1iteminstance1.getEnchantLevel() >= 8 && l1iteminstance1.get_item_level() == 2
								&& random < Config.DollEnchant.WeaponEnchantPerlvl3) { // 機會
							if (l1iteminstance1.getItem().getType2() == 1) {
								l1iteminstance1.set_item_level(3);
							}
						} else if (l1iteminstance1.getEnchantLevel() >= 9 && l1iteminstance1.get_item_level() == 3
								&& random < Config.DollEnchant.WeaponEnchantPerlvl4) { // 機會
							if (l1iteminstance1.getItem().getType2() == 1) {
								l1iteminstance1.set_item_level(4);
							}
						} else {
							pc.sendPackets(l1iteminstance1.getLogName() + "\fH未能注入魔法氣息。");
							pc.getInventory().removeItem(l1iteminstance, 1);
							return;
						}
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
							pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
							pc.getInventory().removeItem(l1iteminstance, 1);
							pc.sendPackets(l1iteminstance1.getLogName() + "\\aL注入了魔法氣息。");
						pc.save();
					}
					break;
					case 4100694:
						case 3000049: // 救護證書
							if (pc.get_exp_res() == 1) {
								pc.sendPackets(String.valueOf(new S_Message_YN(2551, "")));
							} else {
								pc.sendPackets(739);
								return;
							}
							break;
						case 4100696: // 回憶的燈 TeamTheDay by.jude
							if (!pc.getMap().isSafetyZone(pc.getLocation())) {
								pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "只能在安全區域使用。")));
							return;
						}
						if (pc.getLevel() > 79) {
							pc.getInventory().consumeItem(4100696, 1);
							AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance()
									.getSpecialStat(pc.getId());
							if (Info != null) {
								Info.set_bless(0);
								Info.set_lucky(0);
								Info.set_vital(0);
								Info.set_invoke(0);
								Info.set_invoke_val_1(0);
								Info.set_invoke_val_2(0);
								Info.set_restore(0);
								Info.set_restore_val_1(0);
								Info.set_restore_val_2(0);
								Info.set_potion(0);
								Info.set_potion_val_1(0);
								Info.set_potion_val_2(0);
								AinhasadSpecialStatLoader.getInstance().updateSpecialStat(pc);
							}
							// pc.save();
							l1j.server.server.clientpackets.C_NewCharSelect.restartProcess(pc); // 先重啟

						} else {
							pc.sendPackets(String.valueOf(new S_SystemMessage("特殊屬性重置僅限80級以上使用。")));
						}

							break;
							// pc.sendPackets(new
							// S_SystemMessage("可以在亞丁大教堂免費恢復經驗值。"));
							// break;
						case 3000155: // 贖罪聖書
							if (pc.getLawful() <= -1) {
								if (pc.getInventory().consumeItem(3000155, 1)) {
									pc.addLawful(3000);
									pc.sendPackets(String.valueOf(new S_ServerMessage(674)));
								}
							} else {
								pc.sendPackets("只有在屬性為混沌時才能使用。");
							}
							break;
						case 4100470:
							if (pc.getLevel() >= 80) {
								pc.sendPackets("僅限79級以下使用。");
							return;
						}
						long curtimeN = System.currentTimeMillis() / 1000;
						if (pc.getQuizTime() + 1 > curtimeN) {
							long time = (pc.getQuizTime() + 1) - curtimeN;
							// pc.sendPackets(new S_ChatPacket(pc, time +
							// " 秒後可使用。"));
							return;
						}

						long exp = 0;
						int level = ExpTable.getLevelByExp(pc.get_exp());

						exp = (long) ((ExpTable.getExpByLevel(pc.getLevel() + 1) - 1) - pc.get_exp() + 100L);
						pc.add_exp(exp);
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
						pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 3944)));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
						pc.setQuizTime(curtimeN);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
						case 3000124: { // -- 召喚怪物卷軸
							if (!pc.getMap().isRecallPets() || pc.isInWarArea()) {
								pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
								return;
							}
							int pet_size = pc.getPetList().values().size();
							if (pet_size >= 1) {
								pc.sendPackets(String.valueOf(new S_SystemMessage("無法再召喚更多。")));
							return;
						}

						if (pet_size == 0) {
							pet_size = 1;
						}

						int[] summon_id = { 810848, 810849 };
						int random_id = CommonUtil.random(2);// 根據上述召喚數量進行調整
						int pet_cost = 0;

						Iterator<L1NpcInstance> iter = pc.getPetList().values().iterator();
						L1NpcInstance npc = null;

						while (iter.hasNext()) {
							npc = iter.next();
							if (npc == null) {
								continue;
							}
							pet_cost = npc.getPetcost();
						}

						L1Npc npc_temp = NpcTable.getInstance().getTemplate(summon_id[random_id]);

						if (npc_temp == null) {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							return;
						}

						L1SummonInstance summon = null;

						for (int i = 0; i < pet_size; i++) {
							summon = new L1SummonInstance(npc_temp, pc);
							summon.setPetcost(pet_cost);
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					break;
						case 210118: { // 新手保護血盟
							if (pc.getLevel() >= Config.ServerAdSetting.NEWPLAYERLEVELPURGE) {
								pc.sendPackets(String.valueOf(new S_SystemMessage(Config.ServerAdSetting.NEWPLAYERLEVELPURGE + "級以上不可加入新手血盟。")));
								return;
							}

							if (pc.getClanid() != 0) {
								pc.sendPackets(String.valueOf(new S_SystemMessage("您已經加入了一個血盟。")));
								break;
							}
// 							String[] clan_id = { "星", "神話", "戰鬥" };
							String[] clan_id = { "新手保護" };
						int random_id = CommonUtil.random(1);

						L1Clan clan = L1World.getInstance().findClan(clan_id[random_id]);
						L1ClanJoin.getInstance().tutorialJoin(clan, pc);
						pc.getInventory().removeItem(l1iteminstance, 1);
						pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, false);
						break;
					}
						case 7643: // 祝福的精華
							if (pc.getInventory().consumeItem(itemId, 1)) {
								if (pc.getClan()!= null)
									pc.getClan().addBlessCount(10000000);
								pc.sendPackets(String.valueOf(new S_SystemMessage("祝福的氣運 1000 增加了。")));
							}
							break;
						case 7643: // 祝福的精華
							if (pc.getInventory().consumeItem(itemId, 1)) {
								if (pc.getClan() != null)
									pc.getClan().addBlessCount(10000000);
								pc.sendPackets(String.valueOf(new S_SystemMessage("祝福的氣運 1000 增加了。")));
							}
							break;
						case 400253: // 1億金幣存摺
							if (pc.getInventory().checkItem(40308, 100000000)) {
								pc.getInventory().consumeItem(40308, 100000000);
								pc.getInventory().storeItem(400254, 1, true); // 確認狀態TRUE
								pc.sendPackets("\f3金幣1億已被轉換為支票并生成在庫存中。");
							} else {
								pc.sendPackets(String.valueOf(new S_SystemMessage("\f2金幣1億元不足。")));
							}
							break;
						case 3000121: // 神秘武器箱
							if (pc.getInventory().checkItem(3000121, 1)) { // 檢查的項目和
								// 數量
								pc.getInventory().consumeItem(3000121, 1); // 刪除的項目和
								// 數量
							Random random = new Random();
							L1ItemInstance item = null;
								int[] itemrnd = { 12, 1136, 293, 134, 61, 203017, 202003, 203007 }; // 隨機物品編號
								int[] enchantrnd = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1,
										1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2 }; // 隨機增強數值
							int ran1 = random.nextInt(itemrnd.length);
							int ran2 = random.nextInt(enchantrnd.length);
							item = pc.getInventory().storeItem(itemrnd[ran1], 1);
							item.setEnchantLevel(enchantrnd[ran2]);
						}
							pc.sendPackets(String.valueOf(new S_SystemMessage("您獲得了 [物品]。")));
							break;
						case 3000190: { // 金幣隨機箱
							// -- 隨機值的次數
							// -- 隨機值
							int[] itemid = { 40308 };
							int random500 = CommonUtil.random(100);

							if (random500 <= 60) { // -- 2000萬 到 4000萬。
								createNewItem(pc, itemid[0], CommonUtil.random(42000000, 52000000));
							} else { // -- 5萬 到 500萬。
							createNewItem(pc, itemid[0], CommonUtil.random(45000000, 55000000));
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					break;
					case 810006:
					case 810007: {
						if (!(pc.getMapId() >= 1936 && pc.getMapId() <= 2035)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("只能在中央寺廟使用。")));
							return;
						}
						/*
						 * if (delay_id != 0) { // 如果有延遲設置 if (pc.hasItemDelay(delay_id) == true) {
						 * return; } }
						 */
						int chargeCount = l1iteminstance.getChargeCount();

						if (chargeCount <= 0) {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
							return;
						}

						if (pc.isInvisble()) {
							pc.sendPackets(String.valueOf(new S_ServerMessage(1003)));
							return;
						}

						int gfx = 0;
						int dmg = 0;
						int range = 0;
						if (itemId == 810006) {
							gfx = 1819;
							dmg = 150;
							range = 3;
						} else {
							gfx = 3934;
							dmg = 500;
							range = 22;
						}
						L1MonsterInstance mon = null;
						for (L1Object object : L1World.getInstance().getVisibleObjects(pc, range)) {
							if (object == null) {
								continue;
							}
							if (!(object instanceof L1Character)) {
								continue;
							}
							if (object.getId() == pc.getId()) {
								continue;
							}

							if (object instanceof L1MonsterInstance) {
								mon = (L1MonsterInstance) object;
								if (mon.getNpcId() != 7200003) {
									Broadcaster.broadcastPacket(mon,
											new S_DoActionGFX(mon.getId(), ActionCodes.ACTION_Damage));
									mon.receiveDamage(pc, (int) dmg);
								}
							}
						}
						pc.sendPackets(new S_UseAttackSkill(pc, 0, gfx, pc.getX(), pc.getY(), 18));
						Broadcaster.broadcastPacket(pc, new S_UseAttackSkill(pc, 0, gfx, pc.getX(), pc.getY(), 18));
						l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
						pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);

						if (chargeCount <= 1) {
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					break;
					case 410140: { // 保羅的快速卷軸
						int item = l1iteminstance1.getItem().getItemId();
						if (item == 41293) {
							createNewItem(pc, 41294, 1);
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
						if (item == 41294) {
							if (l1iteminstance1.getChargeCount() >= 500) {
								pc.sendPackets(String.valueOf(new S_ServerMessage(3457)));
								// 無法再使用快速卷軸。
								return;
							} else if (l1iteminstance1.getChargeCount() > 4900) {
								l1iteminstance1.setChargeCount(5000);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							} else {
								l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 100);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							}
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					break;
						case 410141: { // 銀色卷軸
							int item = l1iteminstance1.getItem().getItemId();
							if (item == 41293) {
								createNewItem(pc, 41305, 1);
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
							if (item == 41305) {
								if (l1iteminstance1.getChargeCount() >= 5000) {
									pc.sendPackets(String.valueOf(new S_ServerMessage(3457)));
									// 無法再使用快速卷軸。
								return;
							} else if (l1iteminstance1.getChargeCount() > 4950) {
								l1iteminstance1.setChargeCount(5000);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							} else {
								l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 50);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							}
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					break;
					case 4101611:
						L1SpawnUtil.spawnAndGet(pc, 120848, 0, 0);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4101617:
						pc.getAccount().addBlessOfAin((L1Colosseum.Info.infinity_sign_ain * 10000), pc, "圖鑑");
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 410142: { // 金色卷軸
						int item = l1iteminstance1.getItem().getItemId();
						if (item == 41293) {
							createNewItem(pc, 41306, 1);
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
						if (item == 41306) {
							if (l1iteminstance1.getChargeCount() >= 5000) {
								pc.sendPackets(String.valueOf(new S_ServerMessage(3457)));
								// 無法再使用快速卷軸。
								return;
							} else if (l1iteminstance1.getChargeCount() > 4950) {
								l1iteminstance1.setChargeCount(5000);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							} else {
								l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 50);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							}
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					break;
						case 87056: { // 古代的銀色卷軸 300次
							int item = l1iteminstance1.getItem().getItemId();
							if (item == 41293) {
								createNewItem(pc, 87058, 1);
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
							if (item == 87058) {
								if (l1iteminstance1.getChargeCount() >= 4000) { // 次數
									// 本服1000
									pc.sendPackets(String.valueOf(new S_ServerMessage(3457))); // 無法再使用快速
									// 卷軸。
								return;
							} else if (l1iteminstance1.getChargeCount() > 3900) {
								l1iteminstance1.setChargeCount(4000);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							} else {
								l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 300);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							}
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					break;
					case 87057: { // 古代的金色卷軸 300次
						int item = l1iteminstance1.getItem().getItemId();
						if (item == 41293) {
							createNewItem(pc, 87059, 1);
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
						if (item == 87059) {
							if (l1iteminstance1.getChargeCount() >= 4000) { // 次數
								pc.sendPackets(String.valueOf(new S_ServerMessage(3457))); // 無法再使用快速卷軸
								return;
							} else if (l1iteminstance1.getChargeCount() > 3900) {
								l1iteminstance1.setChargeCount(4000);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							} else {
								l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 300);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							}
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					break;
					case 600228: { // 成長的卷軸
						int item = l1iteminstance1.getItem().getItemId();
						if (item == 41293) {
							createNewItem(pc, 600229, 1);
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
						if (item == 600229) { // 成長釣竿
							if (l1iteminstance1.getChargeCount() >= 10000) { // 次數
								pc.sendPackets(String.valueOf(new S_ServerMessage(3457))); // 無法再使用快速卷軸
								return;
							} else if (l1iteminstance1.getChargeCount() > 9900) {
								l1iteminstance1.setChargeCount(10000);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							} else {
								l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 100);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							}
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					break;
						case 4100292: { // 高級成長的卷軸 50次
							int item = l1iteminstance1.getItem().getItemId();
							if (item == 41293) {
								createNewItem(pc, 4100293, 1);
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
							if (item == 4100293) { // 高級成長的釣竿
								if (l1iteminstance1.getChargeCount() >= 10000) { // 次數
									// 本服1000
									pc.sendPackets(String.valueOf(new S_ServerMessage(3457))); // 無法再使用快速
									// 卷軸。
								return;
							} else if (l1iteminstance1.getChargeCount() > 9900) {
								l1iteminstance1.setChargeCount(10000);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							} else {
								l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 50);
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
							}
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					break;
					case 700081:
						LordWideStun(pc); // 君主廣域昏厥
						break;
					case 123123:// 管理者套裝
						if (pc.getInventory().checkItem(123123, 1)) {
							pc.getInventory().consumeItem(123123, 1);
							createGMItem(pc, 60, 1);
							createGMItem(pc, 900010, 1);
							createGMItem(pc, 42023, 100);
							createGMItem(pc, 40088, 100);
							createGMItem(pc, 3000204, 1);
							createGMItem(pc, 46162, 1);
							createGMItem(pc, 46160, 1);
							createGMItem(pc, 700078, 1);
							createGMItem(pc, 700079, 1);
							createGMItem(pc, 5563, 1);
							createGMItem(pc, 5564, 1);
							createGMItem(pc, 5565, 1);
							createGMItem(pc, 5566, 1);
							createGMItem(pc, 5567, 1);
							createGMItem(pc, 5568, 1);
							createGMItem(pc, 42501, 1);
							createGMItem(pc, 410014, 1);
							createGMItem(pc, 410015, 1);
							createGMItem(pc, 50020, 500);
							createGMItem(pc, 50021, 500);
							createGMItem(pc, 40126, 1000);
							createGMItem(pc, 140100, 100);
							createGMItem(pc, 410063, 100);
							createGMItem(pc, 40308, 1000000000);
							createGMItem(pc, 41246, 100000);
							createGMItem(pc, 4100254, 1);
							createGMItem(pc, 4100255, 1);
							createGMItem(pc, 4100256, 1);
							createGMItem(pc, 4100257, 1);
							createGMItem(pc, 4100258, 1);
							createGMItem(pc, 4100259, 1);
							createGMItem(pc, 4100260, 1);
							createGMItem(pc, 4100410, 1);
							createGMItem(pc, 4100677, 1);
							createGMItem(pc, 4100610, 1);
							createGMItem(pc, 560028, 1);
							createGMItem(pc, 4100135, 1);
							createGMItem(pc, 3000470, 100);
							createGMItem(pc, 3000471, 100);
							createGMItem(pc, 3000472, 100);
							createGMItem(pc, 3000473, 100);
							createGMItem(pc, 3000474, 100);
							createGMItem(pc, 3000475, 100);
							createGMItem(pc, 3000476, 100);
							createGMItem(pc, 3000477, 100);
							createGMItem(pc, 3000478, 100);
							createGMItem(pc, 3000479, 100);
						}
						break;
					case 700078:
						int objid = pc.getId();
						pc.send_effect(4856);
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
						for (L1PcInstance tg : L1World.getInstance().getVisiblePlayer(pc)) {
							if (tg.getCurrentHp() == 0 && tg.isDead()) {
								tg.sendPackets(String.valueOf(new S_SystemMessage("GM已經復活了您。")));
								tg.send_effect(3944);
								tg.setTempID(objid);
								tg.sendPackets(String.valueOf(new S_Message_YN(322, "")));
							} else {
								tg.send_effect(832);
								tg.setCurrentHp(tg.getMaxHp());
								tg.setCurrentMp(tg.getMaxMp());
							}
						}
						break;
					case 700079:
						for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 17)) {
							if (obj instanceof L1MonsterInstance) { // 如果對象是怪物
								L1NpcInstance npc = (L1NpcInstance) obj;
								npc.receiveDamage(pc, 300000); // 傷害
							}
							break;
							case 7010: { // 武器祝福卷軸機率
								boolean check = false;
								if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() == 0
										|| l1iteminstance1.getItem().getType2() == 2) {
									pc.sendPackets("該項目僅能用於武器類。");
							return;
						}

						if (l1iteminstance1.isEquipped()) {
							pc.getInventory().setEquipped(l1iteminstance1, false);
							check = true;
						}

						int random = CommonUtil.random(100);
						if (random < Config.ServerEnchant.blessChance_weapon) {
							l1iteminstance1.setBless(0);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS);
							pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS);

							if (l1iteminstance1.getItem().getType2() == 1) {
								int type = l1iteminstance1.getItem().getType();
								if (type == 7 || type == 16 || type == 17) {
									// int random = CommonUtil.random(100);
									if (random <= Config.ServerEnchant.blessChance_weapon_chance) {
										l1iteminstance1.set_bless_level(CommonUtil.random(1, 2)); // 隨機加成 1~3
									} else {
										l1iteminstance1.set_bless_level(3); // 隨機加成
										// 1~3
									}
								} else {
									if (random <= Config.ServerEnchant.blessChance_weapon_chance) {
										l1iteminstance1.set_bless_level(CommonUtil.random(1, 2)); // 隨機加成 1~3
									} else {
										l1iteminstance1.set_bless_level(3); // 隨機加成
										// 1~3
									}
								}
								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
								pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
							}
							if (check) {
								pc.getInventory().setEquipped(l1iteminstance1, true);
								check = false;
							}
							pc.sendPackets(new S_SPMR(pc));
							pc.sendPackets(new S_OwnCharStatus(pc));
							pc.getInventory().removeItem(l1iteminstance, 1);
							pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 9268)));
							pc.sendPackets("祝福效果: 隨機適用 物理攻擊力, SP +1~3。");
							pc.sendPackets(String.valueOf(new S_SystemMessage(l1iteminstance1.getLogName() + " 被注入了祝福的力量。")));
							pc.save();
						} else {
							pc.sendPackets("祝福的力量未能滲透。");
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					break;
							case 7011: {// 武器祝福卷軸 100%
								boolean check = false;
								if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() == 0
										|| l1iteminstance1.getItem().getType2() == 2) { // 武器和
									pc.sendPackets("只能使用於武器類。");
							return;
						}

						if (l1iteminstance1.isEquipped()) {
							pc.getInventory().setEquipped(l1iteminstance1, false);
							check = true;
						}

						int random = CommonUtil.random(100);
						l1iteminstance1.setBless(0);// 更改為祝福的圖像
						pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS);
						pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS);
						if (l1iteminstance1.getItem().getType2() == 1) {
							int type = l1iteminstance1.getItem().getType();
							if (type == 7 || type == 16 || type == 17) {
								if (random <= 70) {
									l1iteminstance1.set_bless_level(CommonUtil.random(1, 2)); // 加成
								} else {
									l1iteminstance1.set_bless_level(3); // 隨機加成 1~3
								}
							} else {
								if (random <= 70) {
									l1iteminstance1.set_bless_level(CommonUtil.random(1, 2)); // 加成
								} else {
									l1iteminstance1.set_bless_level(3); // 隨機加成 1~3
								}
							}
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
							pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
						}
						if (check) {
							pc.getInventory().setEquipped(l1iteminstance1, true);
							check = false;
						}
						pc.sendPackets(new S_SPMR(pc));
						pc.sendPackets(new S_OwnCharStatus(pc));
								pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 9268))); // 發生效果
								pc.sendPackets("祝福效果: 隨機適用 物理攻擊力, SP +1~3。");
								pc.sendPackets(String.valueOf(new S_SystemMessage(l1iteminstance1.getLogName() + " 被注入了祝福的力量。")));
						pc.save();
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					break;
							case 7012: { // 防具祝福卷軸機率
								int type = l1iteminstance1.getItem().getType();
								int item = l1iteminstance1.getItem().getItemId();
								boolean check = false;
								if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() == 0
										|| l1iteminstance1.getItem().getType2() == 1) {
									pc.sendPackets("只能使用於防具類。");
									return;
								}

								if (item == 900233) {
									pc.sendPackets("無法使用於該物品。");
									return;
								}

								if (type >= 8 && type <= 12 || item >= 900234 && item <= 900237
										|| item >= 900275 && item <= 900278) {
									pc.sendPackets("只能使用於防具類。");
							return;
						}

						if (l1iteminstance1.isEquipped()) {
							pc.getInventory().setEquipped(l1iteminstance1, false);
							check = true;
						}

						int random = CommonUtil.random(100);
						if (random < Config.ServerEnchant.blessChance_armor) {
							l1iteminstance1.setBless(0);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS);
							pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS);
							if (l1iteminstance1.getItem().getType2() == 2) {
								if (random <= Config.ServerEnchant.blessChance_armor_chance) {
									l1iteminstance1.set_bless_level(CommonUtil.random(1, 2));
								} else {
									l1iteminstance1.set_bless_level(3);
								}

								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
								pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
							}
							if (check) {
								pc.getInventory().setEquipped(l1iteminstance1, true);
								check = false;
							}
							pc.sendPackets(new S_SPMR(pc));
							pc.sendPackets(new S_OwnCharStatus(pc));

							pc.getInventory().removeItem(l1iteminstance, 1);
							pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 9268)));
							pc.sendPackets("祝福效果: 隨機適用 Ac -1~3。");
							pc.sendPackets(String.valueOf(new S_SystemMessage(l1iteminstance1.getLogName() + " 被注入了祝福的力量。")));
							pc.save();
						} else {
							pc.sendPackets("祝福的力量未能滲透。");
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					break;
							case 7013: { // 防具祝福卷軸 100%
								int type = l1iteminstance1.getItem().getType();
								int item = l1iteminstance1.getItem().getItemId();
								boolean check = false;
								if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() == 0
										|| l1iteminstance1.getItem().getType2() == 1) {
									pc.sendPackets("只能使用於防具類。");
									return;
								}

								if (item == 900233) {
									pc.sendPackets("無法使用於該物品。");
									return;
								}

								if (type >= 8 && type <= 12 || item >= 900234 && item <= 900237
										|| item >= 900275 && item <= 900278) {
									pc.sendPackets("只能使用於防具類。");
							return;
						}

						if (l1iteminstance1.isEquipped()) {
							pc.getInventory().setEquipped(l1iteminstance1, false);
							check = true;
						}

						int random = CommonUtil.random(100);
						l1iteminstance1.setBless(0);
						pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS);
						pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS);
						if (l1iteminstance1.getItem().getType2() == 2) {
							if (random <= 70) {
								l1iteminstance1.set_bless_level(CommonUtil.random(1, 2));
							} else {
								l1iteminstance1.set_bless_level(3);
							}
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
							pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
						}

						if (check) {
							pc.getInventory().setEquipped(l1iteminstance1, true);
							check = false;
						}
						pc.sendPackets(new S_SPMR(pc));
						pc.sendPackets(new S_OwnCharStatus(pc));

						pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 9268)));
								pc.sendPackets("祝福效果: 隨機適用 Ac -1~3。");
								pc.sendPackets(String.valueOf(new S_SystemMessage(l1iteminstance1.getLogName() + " 被注入了祝福的力量。")));
						pc.save();
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					break;
							case 7014: { // 飾品祝福卷軸機率
								int type = l1iteminstance1.getItem().getType();
								boolean check = false;
								if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() == 0
										|| l1iteminstance1.getItem().getType2() == 1 || !(type >= 8 && type <= 12)
										|| l1iteminstance1.getItemId() >= 22224 && l1iteminstance1.getItemId() <= 22228
										|| l1iteminstance1.getItemId() >= 222330 && l1iteminstance1.getItemId() <= 222336
										|| l1iteminstance1.getItemId() == 222290 || l1iteminstance1.getItemId() == 222291
										|| l1iteminstance1.getItemId() == 900195 || l1iteminstance1.getItemId() == 900194
										|| l1iteminstance1.getItemId() >= 22229 && l1iteminstance1.getItemId() <= 22231
										|| l1iteminstance1.getItemId() >= 222337 && l1iteminstance1.getItemId() <= 222341
										|| l1iteminstance1.getItemId() >= 900234 && l1iteminstance1.getItemId() <= 900237) {
									pc.sendPackets("只能使用於飾品類（不包括斯纳普和鲁姆蒂斯）。");
							return;
						}

						if (l1iteminstance1.isEquipped()) {
							pc.getInventory().setEquipped(l1iteminstance1, false);
							check = true;
						}

						int random = CommonUtil.random(100);
						if (random < Config.ServerEnchant.blessChance_accessory) {
							l1iteminstance1.setBless(0);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS);
							pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS);
							if (l1iteminstance1.getItem().getType2() == 2) {
								if (random <= Config.ServerEnchant.blessChance_accessory_chance) {
									l1iteminstance1.set_bless_level(CommonUtil.random(1, 2));
								} else {
									l1iteminstance1.set_bless_level(3);
								}

								pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
								pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
							}
							if (check) {
								pc.getInventory().setEquipped(l1iteminstance1, true);
								check = false;
							}
							pc.sendPackets(new S_SPMR(pc));
							pc.sendPackets(new S_OwnCharStatus(pc));

							pc.getInventory().removeItem(l1iteminstance, 1);
							pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 9268)));
							pc.sendPackets("祝福效果: 隨機適用額外 HP +10~50。");
							pc.sendPackets(String.valueOf(new S_SystemMessage(l1iteminstance1.getLogName() + " 被注入了祝福的力量。")));
							pc.save();
						} else {
							pc.sendPackets("祝福的力量未能滲透。");
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					break;
							case 7015: { // 飾品祝福卷軸 100%
								int type = l1iteminstance1.getItem().getType();
								boolean check = false;
								if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() == 0
										|| l1iteminstance1.getItem().getType2() == 1 || !(type >= 8 && type <= 12)
										|| l1iteminstance1.getItemId() >= 22224 && l1iteminstance1.getItemId() <= 22228
										|| l1iteminstance1.getItemId() >= 222330 && l1iteminstance1.getItemId() <= 222336
										|| l1iteminstance1.getItemId() == 222290 || l1iteminstance1.getItemId() == 222291
										|| l1iteminstance1.getItemId() == 900195 || l1iteminstance1.getItemId() == 900194
										|| l1iteminstance1.getItemId() >= 22229 && l1iteminstance1.getItemId() <= 22231
										|| l1iteminstance1.getItemId() >= 222337 && l1iteminstance1.getItemId() <= 222341
										|| l1iteminstance1.getItemId() >= 900234 && l1iteminstance1.getItemId() <= 900237) {
									pc.sendPackets("只能使用於飾品類（不包括斯纳普和鲁姆蒂斯）。");
							return;
						}

						if (l1iteminstance1.isEquipped()) {
							pc.getInventory().setEquipped(l1iteminstance1, false);
							check = true;
						}

						int random = CommonUtil.random(100);
						l1iteminstance1.setBless(0);
						pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS);
						pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS);
						if (l1iteminstance1.getItem().getType2() == 2) {
							if (random <= 70) {
								l1iteminstance1.set_bless_level(CommonUtil.random(1, 2));
							} else {
								l1iteminstance1.set_bless_level(3);
							}
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
							pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
						}

						if (check) {
							pc.getInventory().setEquipped(l1iteminstance1, true);
							check = false;
						}
						pc.sendPackets(new S_SPMR(pc));
						pc.sendPackets(new S_OwnCharStatus(pc));

						pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 9268)));
								pc.sendPackets("祝福效果: 隨機適用額外 HP +10~50。");
								pc.sendPackets(String.valueOf(new S_SystemMessage(l1iteminstance1.getLogName() + " 被注入了祝福的力量。")));
						pc.save();
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					break;
					case 65648: {// 黑蛇的硬幣
						int[] allBuffSkill = { 4914 };
						L1SkillUse l1skilluse = new L1SkillUse();
						if (pc.hasSkillEffect(L1SkillId.God_buff))
							pc.removeSkillEffect(L1SkillId.God_buff);
						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					break;
					case 3000210:
						long curtime1 = System.currentTimeMillis() / 1000;
						if (pc.getQuizTime() + 1 > curtime1) {
							long sec = (pc.getQuizTime() + 1) - curtime1;
							pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), sec + " 秒後可用。")));
							return;
						}
						pc.sendPackets(String.valueOf(new S_SystemMessage("你的 MP 已經恢復。")));
						pc.setCurrentMp(pc.getCurrentMp() + 300);
						pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 190))); // 發生效果
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
							case 400254: // 1億 金幣 支票
								int check = pc.getInventory().countItems(400254);
								int adenaAmount = pc.getInventory().countItems(40308);
								if (adenaAmount >= 1800000000) {
							pc.sendPackets(3369);
							pc.sendPackets(
									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aG金幣超過 18 億了，請存入倉庫。"));
									return;
								}
								if (check >= 1) {
									pc.getInventory().storeItem(40308, 100000000);
									pc.sendPackets("\f3支票已兌換成 1 億金幣。");
									pc.sendPackets("\f7超過 18 億金幣無法交易！請存入倉庫。");
									pc.getInventory().removeItem(l1iteminstance, 1);
								} else {
									pc.sendPackets("支票不足。");
								}
								break;
							case 41245: // 溶劑
						useResolvent(pc, l1iteminstance1, l1iteminstance);
						break;
					case 700076:
						if ((pc.getX() >= 33311 && pc.getX() <= 33351) && (pc.getY() >= 32432 && pc.getY() <= 32472)
								&& pc.getMapId() == 4) {
							pc.getInventory().removeItem(l1iteminstance, 1);
							L1SpawnUtil.spawn(pc, 45529, 0, 60 * 20000);
							L1World.getInstance()
									.broadcastPacketToAll(new ServerBasePacket[] {
											new S_SystemMessage(
													pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
																	String.format("%s 已在龍之谷三叉路口召喚了德雷克。", pc.getName())),
															new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
																	String.format("%s 已在龍之谷三叉路口召喚了德雷克。", pc.getName())) });
						} else {
							pc.sendPackets(String.valueOf(new S_SystemMessage("在龍之谷入口使用時會召喚巨大的德雷克。")));
						}
						break;
							case 41303: // 大銀色貝利亞那
						int random  = _random.nextInt(120) + 1;
						pc.getInventory().storeItem(40308, 500000);
								pc.sendPackets(String.valueOf(new S_SystemMessage("獲得金幣 (500,000)。")));
								if (random >= 1 && random <= 12) {
									pc.getInventory().storeItem(20315, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得營養滿分腰帶。")));
								} else if (random >= 13 && random <= 24) {
									pc.getInventory().storeItem(20262, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得營養滿分項鍊。")));
								} else if (random >= 25 && random <= 36) {
									pc.getInventory().storeItem(20291, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得營養滿分戒指。")));
								} else if (random >= 37 && random <= 48) {
									pc.getInventory().storeItem(40087, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得對武器施法的卷軸。")));
								} else if (random >= 49 && random <= 59) {
									pc.getInventory().storeItem(40074, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得對盔甲施法的卷軸。")));
								} else if (random >= 60 && random <= 65) {
									pc.getInventory().storeItem(41248, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得魔法娃娃(熊怪)。")));
								} else if (random >= 66 && random <= 71) {
									pc.getInventory().storeItem(210096, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得魔法娃娃(艾蒂)。")));
								} else if (random >= 72 && random <= 74) {
									pc.getInventory().storeItem(210105, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得魔法娃娃(科卡特里斯)。")));
								} else if (random >= 75 && random <= 77) {
									pc.getInventory().storeItem(20422, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得閃耀的古代項鍊。")));
								} else if (random >= 78 && random <= 79) {
									pc.getInventory().storeItem(22000, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得古代名弓腰帶。")));
								} else if (random >= 80 && random <= 81) {
									pc.getInventory().storeItem(22003, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得古代鬥士腰帶。")));
								} else if (random >= 82 && random <= 86) {
									pc.getInventory().storeItem(30127, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得 52 級任務物品箱。")));
								} else {
									pc.sendPackets(String.valueOf(new S_SystemMessage("未能獲得物品。")));
								}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
							case 41304: // 大金色貝利亞那
								int random1 = _random.nextInt(170) + 1;
								pc.getInventory().storeItem(40308, 5000000);
								pc.sendPackets(String.valueOf(new S_SystemMessage("獲得金幣 (5,000,000)。")));
								if (random1 >= 1 && random1 <= 25) {
									pc.getInventory().storeItem(41249, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得魔法娃娃(魅魔)。")));
								} else if (random1 >= 26 && random1 <= 51) {
									pc.getInventory().storeItem(41250, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得魔法娃娃(狼人)。")));
								} else if (random1 >= 52 && random1 <= 77) {
									pc.getInventory().storeItem(210070, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得魔法娃娃(石巨人)。")));
								} else if (random1 >= 78 && random1 <= 88) {
									pc.getInventory().storeItem(40038, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得萬能藥(CHA)。")));
								} else if (random1 >= 89 && random1 <= 99) {
									pc.getInventory().storeItem(140087, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得祝福的對武器施法的卷軸。")));
								} else if (random1 >= 100 && random1 <= 110) {
									pc.getInventory().storeItem(140074, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得祝福的對盔甲施法的卷軸。")));
								} else if (random1 >= 111 && random1 <= 112) {
									pc.getInventory().storeItem(202002, 1);
									pc.sendPackets(String.valueOf(new S_SystemMessage("獲得紅色騎士的巨劍。")));
								}
						} else if (random1 >= 113 && random1 <= 114) {
						pc.getInventory().storeItem(504, 1);
						pc.sendPackets(String.valueOf(new S_SystemMessage("獲得黑曜石麒麟臂環。")));
					} else if (random1 >= 115 && random1 <= 116) {
						pc.getInventory().storeItem(205, 1);
						pc.sendPackets(String.valueOf(new S_SystemMessage("獲得月之長弓。")));
					} else if (random1 >= 117 && random1 <= 118) {
						pc.getInventory().storeItem(20165, 1);
						pc.sendPackets(String.valueOf(new S_SystemMessage("獲得惡魔的手套。")));
					} else if (random1 >= 119 && random1 <= 120) {
						pc.getInventory().storeItem(20197, 1);
						pc.sendPackets(String.valueOf(new S_SystemMessage("獲得惡魔的靴子。")));
					} else if (random1 >= 121 && random1 <= 122) {
						pc.getInventory().storeItem(20160, 1);
						pc.sendPackets(String.valueOf(new S_SystemMessage("獲得黑長老的長袍。")));
					} else if (random1 >= 123 && random1 <= 124) {
						pc.getInventory().storeItem(20218, 1);
						pc.sendPackets(String.valueOf(new S_SystemMessage("獲得黑長老的涼鞋。")));
					} else if (random1 >= 125 && random1 <= 126) {
						pc.getInventory().storeItem(20298, 1);
						pc.sendPackets(String.valueOf(new S_SystemMessage("獲得宙斯的戒指。")));
					} else if (random1 >= 127 && random1 <= 131) {
						pc.getInventory().storeItem(30127, 1);
						pc.sendPackets(String.valueOf(new S_SystemMessage("獲得 52 級任務物品箱。")));
					} else {
						pc.sendPackets(String.valueOf(new S_SystemMessage("未能獲得物品。")));
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;

						case 500035: // 自己血盟加入書
							if (pc.getInventory().checkItem(500035, 1)) {
								pc.getInventory().consumeItem(500035, 1);
								if (pc.isCrown()) { // 如果是君主
									if (pc.get_sex() == 0) { // 如果是王子
										pc.sendPackets(String.valueOf(new S_ServerMessage(87)));
										// 你是王子
									} else {
										pc.sendPackets(String.valueOf(new S_ServerMessage(88)));
										// 你是公主
									}
									return;
								}
								if (pc.getClanid() != 0) { // 如果已有血盟
									pc.sendPackets(String.valueOf(new S_ServerMessage(89)));
									// 你已經有血盟了
								return;
							}
							Connection con = null;
							Statement pstm2 = null;
							ResultSet rs2 = null;
							try {
								con = L1DatabaseFactory.getInstance().getConnection();
								pstm2 = con.createStatement();
								rs2 = pstm2.executeQuery(
										"SELECT `account_name`, `char_name`, `ClanID`, `Clanname` FROM `characters` WHERE Type = 0");
								while (rs2.next()) {
									if (pc.getNetConnection().getAccountName()
											.equalsIgnoreCase(rs2.getString("account_name"))) {
										if (rs2.getInt("ClanID") != 0) { // 君主的血盟如果
											// 存在
											L1Clan clan = L1World.getInstance().findClan(rs2.getString("Clanname"));
											// 加入君主的血盟
											L1PcInstance clanMember[] = clan.getOnlineClanMember();
											for (int cnt = 0; cnt < clanMember.length; cnt++) {
												// 向在線的血盟成員發送消息
												clanMember[cnt].sendPackets(String.valueOf(new S_ServerMessage(94, pc.getName())));
												// 1%0 被接受為血盟成員。
											}
											pc.setClanid(rs2.getInt("ClanID"));
											pc.setClanname(rs2.getString("Clanname"));
											pc.setClanRank(L1Clan.Normal);
											pc.save(); // 將角色信息寫入數據庫
											clan.addClanMember(pc.getName(), pc.getClanRank(), pc.getLevel(), "",
													pc.getId(), pc.getType(), pc.getOnlineStatus(), pc);
											pc.setClanMemberNotes("");
											pc.sendPackets(String.valueOf(new S_Pledge(pc.getClanid())));
											pc.sendPackets(String.valueOf(new S_ReturnedStat(pc.getId(), clan.getClanId())));
											pc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS,
													pc.getClan().getEmblemStatus()));
											for (L1PcInstance player : clan.getOnlineClanMember()) {
												player.sendPackets(
														new S_ReturnedStat(pc.getId(), pc.getClan().getEmblemId()));
												player.broadcastPacket(
														new S_ReturnedStat(player.getId(), pc.getClan().getEmblemId()));
											}
											pc.sendPackets(String.valueOf(new S_ServerMessage(95, rs2.getString("Clanname"))));
											pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339,
													false, false);
											pc.getInventory().removeItem(l1iteminstance, 1);
											break;
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								SQLUtil.close(rs2, pstm2, con);
							}
								if (pc.getClanid() == 0) { // 如果有血盟
									pc.sendPackets(String.valueOf(new S_SystemMessage("帳號中無君主或血盟未創建。")));
								}
							}
							break;
						case 400246:
							pc.sendPackets(String.valueOf(new S_Message_YN(C_Attr.MSGCODE_6008_KDINIT, 6008, "您要重置您的擊殺/死亡記錄嗎？")));
							pc.setKillDeathInitializeItem(l1iteminstance);
							break;
						case 410009:
							pc.sendPackets(String.valueOf(new S_Message_YN(C_Attr.MSGCODE_6008_Name, 6008, "您要更改您的性別嗎？")));
						pc.setNameInstance(l1iteminstance);
						break;
					case 87052:
						if (pc.getInventory().checkItem(87052, 1)) {
							pc.getInventory().consumeItem(87052, 1);
							pc.getInventory().storeItem(87054, 1);
							pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 8473)));
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 8473));
							pc.sendPackets(String.valueOf(new S_SystemMessage("您獲得了虎舍。")));
						}
						break;
					case 87053:
						if (pc.getInventory().checkItem(87053, 1)) {
							pc.getInventory().consumeItem(87053, 1);
							pc.getInventory().storeItem(87055, 1);
							pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 8473)));
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 8473));
							pc.sendPackets(String.valueOf(new S_SystemMessage("您獲得了珍島犬籃子。")));
						}
						break;
						case 87054: // 虎舍
							pc.getInventory().removeItem(l1iteminstance, 1);
							L1SpawnUtil.spawn(pc.get_companion(), 45313, 0, 120000); // 78161 寵物ID
							break;
						case 87055: // 珍島犬籃子
							pc.getInventory().removeItem(l1iteminstance, 1);
							L1SpawnUtil.spawn(pc.get_companion(), 45711, 0, 120000); // 78161 寵物ID
						break;
					case 560025:
					case 560027:
					case 560028:
					case 560029:
					case 4100653:
					case 4100135:
						TelBook.clickItem(pc, itemId, readed_byte_1, l1iteminstance);
						break;
						case 41401:// 家具移除棒
							useFurnitureRemovalWand(pc, readed_int_1, l1iteminstance);
							break;
						case 410014:// 倉庫召喚棒
							useNpcSpownWand(pc, 60001, l1iteminstance);
							break;
						case 5568:
							MJBotLoadManager.delBotItem(pc, readed_int_1);
							break;

						case 46160:// NPC移除棒
							useFieldObjectRemovalWand(pc, readed_int_1, l1iteminstance);
							break;
						case 46162:// NPC確認棒
							useFieldObjectRemovalWand1(pc, readed_int_1, l1iteminstance);
							break;
						case 410015:// 商店召喚棒
							useNpcSpownWand(pc, 7320002, l1iteminstance);
							break;
						case 41345:// 酸性液
							if (pc.getZoneType() == 1) {
								pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 1 沒有任何東西
							return;
						}
						L1DamagePoison.doInfection(pc, pc, 3000, 5, false);
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
						case 41315:// 聖水
							if (pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
								pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 1 沒有任何東西
							return;
						}
						if (pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
							pc.removeSkillEffect(STATUS_HOLY_MITHRIL_POWDER);
						}
						pc.setSkillEffect(STATUS_HOLY_WATER, 900 * 1000);
						pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 190)));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
						pc.sendPackets(String.valueOf(new S_ServerMessage(1141)));
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
						case 30055:// 暴風之杖
							if (!(pc.getMapId() >= 2101 && pc.getMapId() <= 2200 || pc.getMapId() >= 12152 && pc.getMapId() <= 12200)) {
								pc.sendPackets(String.valueOf(new S_SystemMessage("只能在特定區域使用。")));
							return;
						}

							/*
							 * if (delay_id != 0) { // 有延遲設置
							 * if (pc.hasItemDelay(delay_id) == true) {
							 * return;
							 * }
							 * }
							 */

						if (pc.isInvisble()) {
							pc.sendPackets(String.valueOf(new S_ServerMessage(1003)));
							return;
						}

						int gfx = 758;
						int dmg = 250 + _random.nextInt(100);
						int range = 4;
						L1MonsterInstance mon = null;
						for (L1Object object : L1World.getInstance().getVisibleObjects(pc, range)) {
							if (object == null) {
								continue;
							}
							if (!(object instanceof L1Character)) {
								continue;
							}
							if (object.getId() == pc.getId()) {
								continue;
							}

							if (object instanceof L1MonsterInstance) {
								mon = (L1MonsterInstance) object;
								Broadcaster.broadcastPacket(mon, new S_DoActionGFX((Integer) mon.getId(), ActionCodes.ACTION_Damage));
								mon.receiveDamage(pc, (int) dmg);
							}
						}

						pc.sendPackets(new S_UseAttackSkill(pc, 0, gfx, pc.getX(), pc.getY(), 18));
						Broadcaster.broadcastPacket(pc, new S_UseAttackSkill(pc, 0, gfx, pc.getX(), pc.getY(), 18));

						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
						case 3000425:// 炸藥
							if (!(pc.getMapId() >= 1911 && pc.getMapId() <= 1912 || pc.getMapId() == 623 || pc.getMapId() >= 1 && pc.getMapId() <= 2)) {
								pc.sendPackets(String.valueOf(new S_SystemMessage("只能在該地圖上使用。")));
								return;
							}
							if (pc.getLevel() <= 70) {
								pc.sendPackets(String.valueOf(new S_SystemMessage("只能在69級以下使用。")));
							return;
						}
						if (pc.isInvisble()) {
							return;
						}
						cancelAbsoluteBarrier(pc); // 解除吸收障壁
						L1Object target1 = L1World.getInstance().findObject(readed_int_1);
						if (target1 != null) {
							for (L1Object object : L1World.getInstance().getVisiblePoint(target1.getLocation(), 4)) {
								if (object instanceof L1MonsterInstance) {
									L1NpcInstance npc = (L1NpcInstance) object;
									if (!npc.isDead() && npc.getId() != target1.getId()) {
										npc.setStatus(ActionCodes.ACTION_Damage);
										Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), 2));
									}
									npc.receiveDamage(pc, 50);
								}
							}
							pc.sendPackets(
									new S_UseAttackSkill(pc, target1.getId(), 5789, target1.getX(), target1.getY(), 18));
							Broadcaster.broadcastPacket(pc,
									new S_UseAttackSkill(pc, target1.getId(), 5789, target1.getX(), target1.getY(), 18));
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100451:
						if (!(pc.getMapId() == 1911)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("只能在特定地圖上使用。")));
							return;
						}
						if (!(pc.getLevel() <= 72)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("只能在72級以下使用。")));
							return;
						}
						if (pc.isInvisble()) {
							return;
						}
						cancelAbsoluteBarrier(pc); // 解除吸收屏障
						L1Object target3 = L1World.getInstance().findObject(readed_int_1);
						if (target3 != null) {
							for (L1Object object : L1World.getInstance().getVisiblePoint(target3.getLocation(), 4)) {
								if (object instanceof L1MonsterInstance) {
									L1NpcInstance npc = (L1NpcInstance) object;
									if (!npc.isDead() && npc.getId() != target3.getId()) {
										npc.setStatus(ActionCodes.ACTION_Damage);
										Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), 2));
									}
									npc.receiveDamage(pc, 150);
								}
							}
							pc.sendPackets(
									new S_UseAttackSkill(pc, target3.getId(), 17583, target3.getX(), target3.getY(), 18));
							Broadcaster.broadcastPacket(pc,
									new S_UseAttackSkill(pc, target3.getId(), 17583, target3.getX(), target3.getY(), 18));
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100452:
						if (!(pc.getMapId() == 1911)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("只能在特定地圖上使用。")));
							return;
						}
						if (!(pc.getLevel() <= 72)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("只能在72級以下使用。")));
							return;
						}
						if (pc.isInvisble()) {
							return;
						}
						cancelAbsoluteBarrier(pc); // 解除吸收屏障
						L1Object target4 = L1World.getInstance().findObject(readed_int_1);
						if (target4 != null) {
							for (L1Object object : L1World.getInstance().getVisiblePoint(target4.getLocation(), 4)) {
								if (object instanceof L1MonsterInstance) {
									L1NpcInstance npc = (L1NpcInstance) object;
									if (!npc.isDead() && npc.getId() != target4.getId()) {
										npc.setStatus(ActionCodes.ACTION_Damage);
										Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), 2));
									}
									npc.receiveDamage(pc, 150);
								}
							}
							pc.sendPackets(
									new S_UseAttackSkill(pc, target4.getId(), 17587, target4.getX(), target4.getY(), 18));
							Broadcaster.broadcastPacket(pc,
									new S_UseAttackSkill(pc, target4.getId(), 17587, target4.getX(), target4.getY(), 18));
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 4100453:
						if (!(pc.getMapId() == 1911)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("只能在特定地圖上使用。")));
							return;
						}
						if (!(pc.getLevel() <= 72)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("只能在72級以下使用。")));
							return;
						}
						if (pc.isInvisble()) {
							return;
						}
						cancelAbsoluteBarrier(pc); // 解除吸收屏障
						L1Object target5 = L1World.getInstance().findObject(readed_int_1);
						if (target5 != null) {
							for (L1Object object : L1World.getInstance().getVisiblePoint(target5.getLocation(), 4)) {
								if (object instanceof L1MonsterInstance) {
									L1NpcInstance npc = (L1NpcInstance) object;
									if (!npc.isDead() && npc.getId() != target5.getId()) {
										npc.setStatus(ActionCodes.ACTION_Damage);
										Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), 2));
									}
									npc.receiveDamage(pc, 150);
								}
							}
							pc.sendPackets(
									new S_UseAttackSkill(pc, target5.getId(), 17591, target5.getX(), target5.getY(), 18));
							Broadcaster.broadcastPacket(pc,
									new S_UseAttackSkill(pc, target5.getId(), 17591, target5.getX(), target5.getY(), 18));
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 711:
						if (!(pc.getMapId() == 1911)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("只能在特定地圖上使用。")));
							return;
						}
						if (!(pc.getLevel() <= 72)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("只能在72級以下使用。")));
							return;
						}
						if (pc.isInvisble()) {
							return;
						}
						cancelAbsoluteBarrier(pc); // 解除吸收屏障
						L1Object target2 = L1World.getInstance().findObject(readed_int_1);
						if (target2 != null) {
							for (L1Object object : L1World.getInstance().getVisiblePoint(target2.getLocation(), 4)) {
								if (object instanceof L1MonsterInstance) {
									L1NpcInstance npc = (L1NpcInstance) object;
									if (!npc.isDead() && npc.getId() != target2.getId()) {
										npc.setStatus(ActionCodes.ACTION_Damage);
										Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), 2));
									}
									npc.receiveDamage(pc, 100);
								}
							}
							pc.sendPackets(
									new S_UseAttackSkill(pc, target2.getId(), 16060, target2.getX(), target2.getY(), 18));
							Broadcaster.broadcastPacket(pc,
									new S_UseAttackSkill(pc, target2.getId(), 16060, target2.getX(), target2.getY(), 18));
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
						break;
					case 30071:// 亞丁王國裝備箱
						pc.getInventory().removeItem(l1iteminstance, 1);
						int[] Weapon = null;
						int[] Armor = null;
						int[] ArmorEnchant = null;
						int[] Accessory = null;
						int[] AccessoryEnchant = null;
						int MagicDoll = 0;
						int[] SpellBook = null;
						if (pc.isCrown()) {
							Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
							AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
							MagicDoll = 500214; // 斯巴托伊
							SpellBook = new int[] { 40226, 40227, 40229, 40230 };
						} else if (pc.isKnight()) {
							Weapon = new int[] { 9, 62, 180 }; // 奧里哈魯剛短劍, 無冠的雙手劍, 十字弓
							// 魔鬥, 力量項鍊, 虎皮鎧, 魔力護身符, 鋼鐵手套, 強化手套, 妖精防護, 高級鬥士
							Armor = new int[] { 20011, 21028, 21060, 20056, 20187, 20194, 20236, 22003 };
							ArmorEnchant = new int[] { 8, 8, 8, 8, 8, 8, 9, 0 };
							// 翡翠耳環, 光之項鍊, 純白魔法防護, 純白之戒, 引誘之戒, 守護之戒
							Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
							AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
							MagicDoll = 500214; // 斯巴托伊
							SpellBook = new int[] { 40164, 40165 };
						} else if (pc.isElf()) {
							Weapon = new int[] { 9, 508 }; // 奧里哈魯剛短劍, 特貝奧西里斯之弓
							// 魔鬥, 敏捷項鍊, 虎皮鎧, 魔力護身符, 鋼鐵手套, 強化手套, 名弓
							Armor = new int[] { 20011, 21029, 21060, 20056, 20187, 20194, 22000 };
							ArmorEnchant = new int[] { 8, 8, 8, 8, 8, 8, 0 };
							// 翡翠耳環, 光之項鍊, 純白魔法防護, 純白之戒, 引誘之戒, 守護之戒
							Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
							AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
							40242, 40244, 40170, 40171, 40172, 40173, 40174, 40175, 40176, 40177, 40178, 40179,
									40180, 40181, 40182, 40183, 40184, 40185, 40186, 40187, 40188, 40189, 40190, 40191,
									40192, 40193 };
					} else if (pc.isWizard()) {
						Weapon = new int[] { 509, 169 }; // 特貝奧西里斯之杖, 獵人之弓
						// 魔鬥, 酷瑪, 魔力戒指, 高級羅布, 魔力護身符, 魔力護腕, 鋼鐵手套, 強化手套, 神聖魔法, 魔法水晶球, 魔法紋章
						Armor = new int[] { 20011, 22192, 21031, 20093, 20056, 20055, 20187, 20194, 20233, 20225,
								22255 };
						ArmorEnchant = new int[] { 8, 8, 8, 0, 8, 7, 8, 8, 8, 5, 0 };
						// 翡翠耳環, 藍寶石項鍊, 純白魔法防護, 純白集中, 引誘之戒, 守護之戒
						Accessory = new int[] { 22231, 20257, 22228, 22228, 22225, 22225, 20288, 20317 };
						AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
						MagicDoll = 210071; // 長老
						SpellBook = new int[] { 40170, 40171, 40172, 40173, 40174, 40175, 40176, 40177, 40178,
								40179, 40180, 40181, 40182, 40183, 40184, 40185, 40186, 40187, 40188, 40189, 40190, 40191,
								40192, 40193, 40197, 40224, 40213 };
					} else if (pc.isDarkelf()) {
						Weapon = new int[] { 507, 180 }; // 特貝奧西里斯之雙刀, 十字弓
						// 魔鬥, 力量項鍊, 虎皮鎧, 魔力護身符, 鋼鐵手套, 金屬手套, 妖精防護, 高級鬥士
						Armor = new int[] { 20011, 21028, 21060, 20056, 20187, 20195, 20236, 22003 };
						ArmorEnchant = new int[] { 8, 8, 8, 8, 8, 8, 9, 0 };
							// 翡翠耳環, 光之項鍊, 純白魔法防護, 純白之戒, 引誘之戒, 守護之戒
							Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
							AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
						Armor = new int[] { 20011, 21028, 21060, 20056, 20187, 20194, 20236, 22003 };
						ArmorEnchant = new int[] { 8, 8, 8, 8, 8, 8, 9, 0 };
						// 翡翠耳環, 光之項鍊, 純白魔法防護, 純白之戒, 引誘之戒, 守護之戒
						Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
						AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
						MagicDoll = 500214; // 斯巴托伊
						SpellBook = new int[] { 210021, 210022, 210023, 210024, 210026, 210027, 210028, 210029, 210030,
								210031, 210032, 210033, 210034 };
					} else if (pc.isBlackwizard()) {
						Weapon = new int[] { 509, 504 }; // 特貝奧西里斯之杖, 黑曜石之戒, 特貝奧西里斯之弓
						// 魔鬥, 力量項鍊, 虎皮鎧, 魔力護身符, 鋼鐵手套, 強化手套, 神聖魔法, 高級鬥士
						Armor = new int[] { 20011, 21028, 21060, 20056, 20187, 20194, 20233, 22003 };
						ArmorEnchant = new int[] { 8, 8, 8, 8, 8, 8, 8, 0 };
						// 翡翠耳環, 光之項鍊, 純白魔法防護, 純白之戒, 引誘之戒, 守護之戒
						Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
						AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
						MagicDoll = 500214; // 斯巴托伊
							SpellBook = new int[] { 210000, 210001, 210002, 210003, 210005, 210006, 210007, 210008, 210009,
									210010, 210011, 210012, 210013, 210015, 210016, 210017, 210018, 210019 };
						}
					for (int i = 0; i < Weapon.length; i++) { // 武器
						createNewItemTrade(pc, Weapon[i], 1, 9, 129, 3, true);
					}
					for (int i = 0; i < Armor.length; i++) { // 防具
						createNewItemTrade(pc, Armor[i], 1, ArmorEnchant[i], 129, 0, true);
					}
					for (int i = 0; i < Accessory.length; i++) { // 飾品
						createNewItemTrade(pc, Accessory[i], 1, AccessoryEnchant[i], 129, 0, true);
					}
					for (int i = 0; i < SpellBook.length; i++) { // 法書
						createNewItemTrade(pc, SpellBook[i], 1, 0, 1, 0, false);
					}
					createNewItemTrade(pc, MagicDoll, 1, 0, 129, 0, false);
					createNewItemTrade(pc, 30072, 200, 0, 129, 0, false); // 但丁的遺物袋
					createNewItemTrade(pc, 40308, 2000000, 0, 1, 0, false); // 金幣
					break;
					case 700025:
					case 700024: // 模糊記憶之珠
						L1BookMark.Bookmarkitem(pc, l1iteminstance, itemId, false);
						break;
					case 3000065:
					case 3000256:
					case 3000509:
					case 4100446:
						MJItemEnchanterLoader.getInstance().do_enchant(pc, l1iteminstance, l1iteminstance1);
						break;
					case 4100603: // 5階段魔法娃娃碎片
						if (pc.getInventory().checkItem(4100603, 50)) {
							pc.getInventory().consumeItem(4100603, 50);
							pc.getInventory().storeItem(5552, 1);
						}
						break;
					case 4100602: // 4階段魔法娃娃碎片
						if (pc.getInventory().checkItem(4100602, 100)) {
							pc.getInventory().consumeItem(4100602, 100);
							pc.getInventory().storeItem(5551, 1);

						}
						break;
					case 5558:
						Blessing(pc, l1iteminstance);
						break;
					case 4100697:
					case 4100698:
					case 4100699:
					case 4100700:
					case 4100701:
					case 4100702:
					case 4100703:
					case 4100704:
					case 4100705:
					case 4100733:
						Class_Rank_Bless(pc, l1iteminstance, 1);
						break;
					case 30001756:
					case 30001757:
					case 30001758:
					case 30001759:
					case 30001760:
					case 30001761:
					case 30001763:
					case 30001764:
					case 30001765:
						Class_Rank_Bless(pc, l1iteminstance, 2);
						break;
					case 30001766:
					case 30001767:
					case 30001768:
					case 30001769:
					case 30001770:
					case 30001771:
					case 30001772:
					case 30001773:
					case 30001774:
					case 30001775:
						Class_Rank_Bless(pc, l1iteminstance, 3);
						break;
					case 30107:
					case 30108:
					case 30109:
					case 30110:
					case 30111:
					case 30112:
					case 30113:
					case 30114:
					case 30115: {
						// 純白的T恤印章
						int targetItem = l1iteminstance1.getItemId();
						int[] item = new int[] { 30107, 30108, 30109, 30110, 30111, 30112, 30113, 30114, 30115 };
						int[] t = new int[] { 22349, 22350, 22351, 22352, 22353, 22354, 22355, 22356, 22357 };
						int[] elf_t = new int[] { 22340, 22341, 22342, 22343, 22344, 22345, 22346, 22347, 22348 };
						if (targetItem == 20084) { // 妖精族T恤
							for (int i = 0; i < item.length; i++) {
								if (l1iteminstance.getItemId() == item[i]) {
									createNewItem2(pc, elf_t[i], 1, l1iteminstance1.getEnchantLevel());
									pc.getInventory().DeleteEnchant(l1iteminstance1.getItemId(),
											l1iteminstance1.getEnchantLevel());
									pc.getInventory().removeItem(l1iteminstance, 1);
									break;
								}
							}
						} else if (targetItem == 20085) { // T恤
							for (int i = 0; i < item.length; i++) {
								if (l1iteminstance.getItemId() == item[i]) {
									createNewItem2(pc, t[i], 1, l1iteminstance1.getEnchantLevel());
									pc.getInventory().DeleteEnchant(l1iteminstance1.getItemId(),
											l1iteminstance1.getEnchantLevel());
									pc.getInventory().removeItem(l1iteminstance, 1);
									break;
								}
							}
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
						}
					}
					break;
					case 30116: // 天然皂：純白的T恤
						int targetItem = l1iteminstance1.getItemId();
						if (targetItem >= 22340 && targetItem <= 22348) { // 純白的妖精族T恤
							createNewItem2(pc, 20084, 1, l1iteminstance1.getEnchantLevel());
							pc.getInventory().DeleteEnchant(l1iteminstance1.getItemId(), l1iteminstance1.getEnchantLevel());
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else if (targetItem >= 22349 && targetItem <= 22357) { // 純白的T恤
							// 其他邏輯
							createNewItem2(pc, 20085, 1, l1iteminstance1.getEnchantLevel());
							pc.getInventory().DeleteEnchant(l1iteminstance1.getItemId(), l1iteminstance1.getEnchantLevel());
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
						}
						break;
					/**************************
					 * 武器強化附魔相關卷軸
					 ***********************************************/
					/** 屬性轉換卷軸 **/
					case 560030:
					case 560031:
					case 560032:
					case 560033:
					/** 屬性轉換卷軸 **/
					case 40130:
					case 140130:
					case L1ItemId.SCROLL_OF_ENCHANT_WEAPON:
					case L1ItemId.SCROLL_OF_ENCHANT_QUEST_WEAPON:
					case L1ItemId.SCROLL_OF_ENCHANT_FANTASY_WEAPON: // 幻想武器魔法卷軸
					case L1ItemId.B_SCROLL_OF_ENCHANT_WEAPON:
					case L1ItemId.C_SCROLL_OF_ENCHANT_WEAPON:
					case L1ItemId.IVORYTOWER_WEAPON_SCROLL:
					case L1ItemId.ENCHANT_WEAPONA:
					case L1ItemId.B_SCROLL_CHIVALRIC_ENCHANT_WEAPON:
					case L1ItemId.C_SCROLL_CHIVALRIC_ENCHANT_WEAPON:
					case 4100462:
					case 210085:
					case 210064:
					case 724:
					case 210065:
					case 210066:
					case 210067:
					case 810003:
					case 4100148:
					case 127000:
					case 30146:
					case 30068:
					case 30001360:
					case 30001361:
					case 30001362:
					case 30001363:
					case 30001897: {
						WeaponEnchant(pc, l1iteminstance1, l1iteminstance, client);
					}
					break;
					case 30001885:{// 執行級武器魔法卷軸
						int itemid = l1iteminstance1.getItemId();
						if (itemid == 61 || itemid == 12 || itemid == 134 || itemid == 86 || itemid == 7000238
								|| itemid == 202011 || itemid == 202013 || itemid == 202012 || itemid == 202014 || itemid == 7000258) {
							int enchant_level = l1iteminstance1.getEnchantLevel();
							pc.getInventory().removeItem(l1iteminstance, 1);
							int rnd = _random.nextInt(1000) + 1;
							int per = Config.ServerEnchant.Hero_weapon_per;
							int limit = Config.ServerEnchant.Hero_weapon_limit;
							if (enchant_level >= limit) {
								pc.sendPackets("該物品無法再進行強化。");
								break;
							}
							if (rnd <= per) {
								SuccessEnchant(pc, l1iteminstance1, client, 1);
							} else {
								EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN, 0,
										l1iteminstance1);
							}
						}
					}

					break;
					case 30001886: { // 神話防具魔法卷軸
						int itemid = l1iteminstance1.getItemId();
						if (itemid == 900281 || itemid == 13505) {
							int enchant_level = l1iteminstance1.getEnchantLevel();
							pc.getInventory().removeItem(l1iteminstance, 1);
							int rnd = _random.nextInt(1000) + 1;
							int per = Config.ServerEnchant.Hero_armor_per;
							int limit = Config.ServerEnchant.Hero_armor_limit;
							if (enchant_level >= limit) {
								pc.sendPackets("該物品無法再進行強化。");
								break;
							}
							if (rnd <= per) {
								SuccessEnchant(pc, l1iteminstance1, client, 1);
							} else {
								EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN, 0,
										l1iteminstance1);
							}
						}
					}
					break;
					// TODO 防具強化附魔相關代碼
					case L1ItemId.SCROLL_OF_ENCHANT_ARMOR:
					case L1ItemId.SCROLL_OF_ENCHANT_ARMOR4:
					case L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR5:
					case L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR6:
					case L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR:
					case L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR:
					case L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR:
					case L1ItemId.ENCHANT_ARMOR:
					case L1ItemId.Inadril_T_ScrollA:
					case L1ItemId.Inadril_T_ScrollB:
					case L1ItemId.Inadril_T_ScrollC:
					case L1ItemId.Pure_white_Scroll:
					case L1ItemId.Roomtis_Scroll:
					case L1ItemId.IVORYTOWER_ARMOR_SCROLL:
					case L1ItemId.IVORYTOWER_GKFFHDNLS:
					case L1ItemId.Inadril_T_ScrollA1:
					case L1ItemId.Inadril_T_ScrollB2:
					case L1ItemId.Inadril_T_ScrollB3:
					case L1ItemId.Inadril_T_ScrollB4: // TeamTheday by.Jude
					case L1ItemId.ENCHANT_TEST1:
					case L1ItemId.ENCHANT_TEST2:
					case L1ItemId.ENCHANT_TEST3:
					case L1ItemId.ENCHANT_TEST4:
					case L1ItemId.ENCHANT_TEST5:
					case L1ItemId.ENCHANT_TEST6:
					case L1ItemId.Pendant_Scroll:
					case L1ItemId.B_SCROLL_CHIVALRIC_ENCHANT_ARMOR:
					case L1ItemId.C_SCROLL_CHIVALRIC_ENCHANT_ARMOR:
					case 4100671:
					case 4100461:
					case 7004:
					case 40129:
					case 140129:
					case 210084:
					case 3000100:// 紋章強化石
					case 5991:
					case 3000517:
					case 210068:
					case 4100692:
					case 4100616:
					case 30069:
					case 30147:
					case 810012:
					case 810013:
					case 30001896: {
						ArmorEnchant(pc, l1iteminstance1, l1iteminstance, client);
					}
					break;
					default:
						if (l1iteminstance.getItem().getType() == 81) {
							L1BoxItem boxItem = L1BoxItem.get(itemId);
							if (boxItem != null) {
								boxItem.use(pc, l1iteminstance);
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(74, l1iteminstance.getLogName())));
							}
						}

						else if (l1iteminstance.getItem().getType() == 74) {
							L1SpawnWand spawn = L1SpawnWand.get(itemId);
							if (spawn != null) {
								if (!spawn.use(pc, l1iteminstance)) {
									// pc.sendPackets(new S_ServerMessage(79));
								}
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(74, l1iteminstance.getLogName())));
							}
						} else if (l1iteminstance.getItem().getType() == 80) {
							L1MeterialChoice mc = L1MeterialChoice.get(itemId);
							if (mc != null) {
								mc.use(pc, l1iteminstance, l1iteminstance1);
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(74, l1iteminstance.getLogName())));
							}
						} else if (l1iteminstance.getItem().getType() == 501) {
							L1BlessTypeEnchant blessType = L1BlessTypeEnchant.get(itemId);
							if (blessType != null) {
								blessType.use(pc, l1iteminstance, l1iteminstance1);
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(74, l1iteminstance.getLogName())));
							}
						} else if (l1iteminstance.getItem().getType() == 21) {
							L1ProtectEnchantScroll pes = L1ProtectEnchantScroll.get(itemId);
							if (pes != null) {
								pes.use(pc, l1iteminstance, l1iteminstance1);
							} else {
								pc.sendPackets(String.valueOf(new S_ServerMessage(74, l1iteminstance.getLogName())));
							}
						} else if (use_type == 73) {
							L1MagicDoll magicDollItem = L1MagicDoll.get(itemId);
							if (magicDollItem != null)
								magicDollItem.use(pc, l1iteminstance);
							else
								pc.sendPackets(new S_ServerMessage(74, l1iteminstance.getLogName()));
						} else if (itemId >= 40136 && itemId <= 40161 || itemId == 410027) {
							int soundid = 3198;
							if (itemId == 40154) {
								soundid = 3198;
							} else if (itemId == 40152) {
								soundid = 2031;
							} else if (itemId == 40141) {
								soundid = 2028;
							} else if (itemId == 40160) {
								soundid = 2030;
							} else if (itemId == 40145) {
								soundid = 2029;
							} else if (itemId == 40159) {
								soundid = 2033;
							} else if (itemId == 40151) {
								soundid = 2032;
							} else if (itemId == 40161) {
								soundid = 2037;
							} else if (itemId == 40142) {
								soundid = 2036;
							} else if (itemId == 40146) {
								soundid = 2039;
							} else if (itemId == 40148) {
								soundid = 2043;
							} else if (itemId == 40143) {
								soundid = 2041;
							} else if (itemId == 40156) {
								soundid = 2042;
							} else if (itemId == 40139) {
								soundid = 2040;
							} else if (itemId == 40137) {
								soundid = 2047;
							} else if (itemId == 40136) {
								soundid = 2046;
							} else if (itemId == 40138) {
								soundid = 2048;
							} else if (itemId == 40140) {
								soundid = 2051;
							} else if (itemId == 40144) {
								soundid = 2053;
							} else if (itemId == 40147) {
								soundid = 2045;
							} else if (itemId == 40149) {
								soundid = 2034;
							} else if (itemId == 40150) {
								soundid = 2055;
							} else if (itemId == 40153) {
								soundid = 2038;
							} else if (itemId == 40155) {
								soundid = 2044;
							} else if (itemId == 40157) {
								soundid = 2035;
							} else if (itemId == 40158) {
								soundid = 2049;
							} else {
								soundid = 3198;
							}

							S_SkillSound s_skillsound = new S_SkillSound(pc.getId(), soundid);
							pc.sendPackets(s_skillsound);
							pc.broadcastPacket(s_skillsound);
							pc.getInventory().removeItem(l1iteminstance, 1);
							// 法術卷軸
						} else if ((itemId >= 40859 && itemId <= 40898) && itemId != 40863) {
							if (pc.isSkillDelay()) {
								pc.sendPackets(String.valueOf(new S_ServerMessage(281)));
								return;
							}

							int useType = l1iteminstance.getItem().getUseType();
							if (useType == 30 || useType == 0) {
								readed_int_1 = pc.getId();
							} else {
								if (readed_int_1 == pc.getId() || readed_int_1 <= 0) {
									pc.sendPackets(String.valueOf(new S_ServerMessage(281))); // 1魔法已
									return;
								}
							}
							pc.getInventory().removeItem(l1iteminstance, 1);

							cancelAbsoluteBarrier(pc);
							// 解除吸收屏障
							int skillid = itemId - 40858;
							L1Skills skill = SkillsTable.getInstance().getTemplate(skillid);

							if (skill == null) {
								pc.sendPackets(String.valueOf(new S_SystemMessage("此技能未使用。請聯繫管理員。")));
								System.out.println("此技能未使用。請聯繫管理員。 Skillid: " + skillid);
								return;
							}
							if (skillid == L1SkillId.BLESSED_ARMOR) {
								L1SkillUse l1skilluse = new L1SkillUse();
								l1skilluse.handleCommands(client.getActiveChar(), skillid, readed_int_1, readed_short_1,
										readed_short_2, null, 0, L1SkillUse.TYPE_SPELLSC);
							} else if (skillid == L1SkillId.ENCHANT_WEAPON) {
								L1SkillUse l1skilluse = new L1SkillUse();
								l1skilluse.handleCommands(client.getActiveChar(), skillid, readed_int_1, readed_short_1,
										readed_short_2, null, 0, L1SkillUse.TYPE_SPELLSC);
							} else {
								L1SkillUse l1skilluse = new L1SkillUse();
								l1skilluse.handleCommands(client.getActiveChar(), skillid, readed_int_1, readed_short_1,
										readed_short_2, null, 0, L1SkillUse.TYPE_SPELLSC);
							}
						} else if (itemId >= 41357 && itemId <= 41382) {
							// 알파벳 불꽃
							int soundid = itemId - 34946;
							S_SkillSound s_skillsound = new S_SkillSound(pc.getId(), soundid);
							pc.sendPackets(s_skillsound);
							pc.broadcastPacket(s_skillsound);
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else if ((itemId >= 41277 && itemId <= 41292) || (itemId >= 49049 && itemId <= 49064)
								|| (itemId >= 210048 && itemId <= 210063) || (itemId >= 30051 && itemId <= 30054)
								|| (itemId >= 4100156 && itemId <= 4100159) || (itemId >= 3000129 && itemId <= 3000130)
								|| (itemId >= 42650 && itemId <= 42653 || (itemId >= 30001858 && itemId <= 30001865))) { // Yoria物品
							L1Cooking.useCookingItem(pc, l1iteminstance);
						} else if (itemId >= 41383 && itemId <= 41400) { // 家具
							useFurnitureItem(pc, itemId, itemObjid);
						} /*else if (itemId > 40169 && itemId < 40224 || itemId == 40225 || itemId >= 45000 && itemId <= 45022
							|| itemId >= 30001566 && itemId <= 30001592 || itemId == 3000095 || itemId == 4100538
							|| itemId == 4100717 || itemId == 4101512 || itemId == 30001584 || itemId == 30001688
							|| itemId >= 30001749 && itemId <= 30001755 || itemId == 30001580 || itemId == 30001794) { // 魔法書
						useSpellBook(pc, l1iteminstance, itemId);
					} else if (itemId > 40225 && itemId < 40232 || itemId == 5560 || itemId == 3000090
							|| itemId == 3000089 || itemId == 40231 || itemId == 4100543 || itemId == 4100713
							|| itemId == 4101640 || itemId >= 30001624 && itemId <= 30001636 || itemId == 30001682
							|| itemId == 30001706 || itemId == 30001857) {
						if (pc.isCrown() || pc.isGm()) {
							if (itemId == 40226 && pc.getLevel() >= 50) {
								SpellBook4(pc, l1iteminstance, client);
							} else if (itemId == 40227 || itemId == 30001626 && pc.getLevel() >= 60) {
								SpellBook4(pc, l1iteminstance, client);
							} else if ((itemId == 40232) && pc.getLevel() >= 45) {
								SpellBook4(pc, l1iteminstance, client);
							} else if (itemId == 40230 || itemId == 30001624 && pc.getLevel() >= 70) {
								SpellBook4(pc, l1iteminstance, client);
							} else if (itemId == 40229 || itemId == 30001625 && pc.getLevel() >= 75) {
								SpellBook4(pc, l1iteminstance, client);
							} else if (itemId == 5560 || itemId == 3000090 || itemId == 3000089 || itemId == 40231
									|| itemId == 40228 || itemId == 4101640 || itemId == 30001628 || itemId == 30001629
									|| itemId == 30001634 || itemId == 30001635
									|| itemId == 30001706 && pc.getLevel() >= 80) {
								SpellBook4(pc, l1iteminstance, client);
							} else if (itemId == 4100543 || itemId == 30001631
									|| itemId == 30001682 && pc.getLevel() >= 85) {
								SpellBook4(pc, l1iteminstance, client);
							} else if (itemId == 30001630 || itemId == 30001632 || itemId == 4100713
									|| itemId == 30001857 && pc.getLevel() >= 90) {
								SpellBook4(pc, l1iteminstance, client);
							} else {
								pc.sendPackets(new S_ServerMessage(312)); // 等級太低
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						}
						// 精靈的水晶
					} else if ((itemId >= 40232 && itemId <= 40264) || (itemId >= 41149 && itemId <= 41153)
							|| itemId == 3000091 || itemId == 4100714 || itemId == 4101636 || itemId == 4101639
							|| (itemId >= 3000511 && itemId <= 3000513) || itemId == 4100295 || itemId == 4100567
							|| itemId >= 30001494 && itemId <= 30001522 || itemId == 30001713 || itemId == 30001714
							|| itemId >= 30001715 && itemId <= 30001718) {
						useElfSpellBook(pc, l1iteminstance, itemId);
					} else if (itemId > 40264 && itemId < 40280 || itemId >= 30001475 && itemId <= 30001480
							|| itemId == 5559 || itemId == 4100103 || itemId == 4100104 || itemId == 4100105
							|| itemId == 4100542 || itemId == 4100613 || itemId == 30001690 || itemId == 30001720) {
						if (pc.isDarkelf() || pc.isGm()) {
							if (itemId >= 40265 && itemId <= 40269 && pc.getLevel() >= 20) {
								SpellBook1(pc, l1iteminstance, client);
							} else if (itemId >= 40270 && itemId <= 40274 && pc.getLevel() >= 40) {
								SpellBook1(pc, l1iteminstance, client);
							} else if (itemId >= 40275 && itemId <= 40279 && pc.getLevel() >= 60) {
								SpellBook1(pc, l1iteminstance, client);
							} else if (itemId == 5559 || itemId == 30001720 && pc.getLevel() >= 60) {
								SpellBook1(pc, l1iteminstance, client);
							} else if ((itemId == 4100103 || itemId == 4100104 || itemId == 4100613)
									&& pc.getLevel() >= 80) {
								SpellBook1(pc, l1iteminstance, client);
							} else if ((itemId == 4100105 || itemId == 4100542 || itemId == 30001475
									|| itemId == 30001690) && pc.getLevel() >= 85) {
								SpellBook1(pc, l1iteminstance, client);
							} else {
								pc.sendPackets(new S_ServerMessage(312));
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							// (原文:黑暗精靈的水晶只有黑暗妖精可以獲得。)
						}
						// 騎士技能書
					} else if (itemId >= 40164 && itemId <= 40166 || itemId >= 41147 && itemId <= 41148
							|| itemId == 3000092 || itemId == 4100100 || itemId == 4100101 || itemId == 4100102
							|| itemId == 4100537 || itemId == 4101635 || itemId >= 30001593 && itemId <= 30001607
							|| itemId == 30001684 || itemId == 30001707 || itemId == 30001708) {
						if (pc.isKnight() || pc.isGm()) {
							if (itemId == 40165 && pc.getLevel() >= 50) {
								SpellBook3(pc, l1iteminstance, client);
							} else if (itemId == 41147 && pc.getLevel() >= 55) {
								SpellBook3(pc, l1iteminstance, client);
							} else if (itemId == 40164 || itemId == 30001599 && pc.getLevel() >= 60) {
								SpellBook3(pc, l1iteminstance, client);
							} else if ((itemId == 40166 || itemId == 30001593) && pc.getLevel() >= 65) {
								SpellBook3(pc, l1iteminstance, client);
							} else if ((itemId == 4100100) && pc.getLevel() >= 60) {
								SpellBook3(pc, l1iteminstance, client);
							} else if (itemId == 4100101 || itemId == 30001598 && pc.getLevel() >= 75) {
								SpellBook3(pc, l1iteminstance, client);
							} else if (itemId == 41148 || itemId == 30001597 || itemId == 30001707
									|| itemId == 30001708 && pc.getLevel() >= 80) {
								SpellBook3(pc, l1iteminstance, client);
							} else if (itemId == 4100102 || itemId == 3000092 || itemId == 4100537 || itemId == 4101635
									|| itemId == 30001603 || itemId == 30001606
									|| itemId == 30001684 && pc.getLevel() >= 85) {
								SpellBook3(pc, l1iteminstance, client);
							} else {
								pc.sendPackets(new S_ServerMessage(312));
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						} // 龍騎士
					} else if ((itemId >= 210021 && itemId <= 210034) || itemId == 4100106 || itemId == 4100541
							|| itemId >= 30001608 && itemId <= 30001623 || itemId == 30001692 || itemId == 30001899 || itemId == 30001900) {
						if (pc.isDragonknight() || pc.isGm()) {
							if (itemId >= 210021 && itemId <= 210024 || itemId == 30001608 && pc.getLevel() >= 15) {
								SpellBook5(pc, l1iteminstance, client);
							} else if (itemId >= 210025 && itemId <= 210029 && pc.getLevel() >= 30) {
								SpellBook5(pc, l1iteminstance, client);
							} else if (itemId >= 210301 && itemId <= 210304 || itemId == 4100566 || itemId == 30001609
									|| itemId == 30001610 || itemId == 30001612 && pc.getLevel() >= 45) {
								SpellBook5(pc, l1iteminstance, client);
							} else if (itemId == 210030 && pc.getLevel() == 60) {
								SpellBook5(pc, l1iteminstance, client);
							} else if (itemId == 4100106 || itemId == 30001613 && pc.getLevel() == 80) {
								SpellBook5(pc, l1iteminstance, client);
							} else if (itemId == 4100541 || itemId == 30001618
									|| itemId == 30001692 && pc.getLevel() == 85) {
								SpellBook5(pc, l1iteminstance, client);
							} else if (itemId == 30001899 || itemId == 30001900){
								SpellBook5(pc, l1iteminstance, client);
							} else {
								pc.sendPackets(new S_ServerMessage(312));
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						} // 幻術
					} else if ((itemId >= 210000 && itemId <= 210019) || itemId == 3000096 || itemId == 4100109
							|| itemId == 4100448 || itemId == 4100449 || itemId == 4100544 || itemId == 4100737
							|| itemId == 4100738 || itemId >= 30001481 && itemId <= 30001493 || itemId == 30001694
							|| itemId == 30001728 || itemId == 30001729) {
						if (pc.isBlackwizard() || pc.isGm()) {
							if (itemId >= 210000 && itemId <= 210004 && pc.getLevel() >= 15) {
								SpellBook6(pc, l1iteminstance, client);
							} else if (itemId >= 210005 && itemId <= 210009 && pc.getLevel() >= 30) {
								SpellBook6(pc, l1iteminstance, client);
							} else if ((itemId >= 210010 && itemId <= 210014 || itemId == 30001484)
									&& pc.getLevel() >= 45) {
								SpellBook6(pc, l1iteminstance, client);
							} else if ((itemId >= 210015 && itemId <= 210019
									|| itemId >= 30001485 && itemId <= 30001488) && pc.getLevel() >= 60) {
								SpellBook6(pc, l1iteminstance, client);
							} else if ((itemId == 4100109 || itemId == 30001489) && pc.getLevel() >= 75) {
								SpellBook6(pc, l1iteminstance, client);
							} else if ((itemId == 30001483 || itemId == 3000096 || itemId == 4100448
									|| itemId == 30001728 || itemId == 30001729) && pc.getLevel() >= 80) {
								SpellBook6(pc, l1iteminstance, client);
							} else if ((itemId == 4100449 || itemId == 4100544 || itemId == 30001491
									|| itemId == 30001694) && pc.getLevel() >= 85) {
								SpellBook6(pc, l1iteminstance, client);
							} else if ((itemId == 4100738 || itemId == 4100737 || itemId == 30001492
									|| itemId == 30001493) && pc.getLevel() >= 90) {
								SpellBook6(pc, l1iteminstance, client);
							} else {
								pc.sendPackets(new S_ServerMessage(312));
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						} // 戰士
					} else if (itemId >= 210121 && itemId <= 210132 || itemId == 3000094 || itemId == 4100540
							|| itemId == 4100712 || itemId == 4100736 || itemId == 30001553 || itemId == 30001555
							|| itemId == 30001560 || itemId == 30001561 || itemId == 30001565 || itemId == 30001640
							|| itemId >= 30001737 && itemId <= 30001739) {
						if (pc.is전사()) {
							if (itemId == 210121 && pc.getLevel() >= 30) {
								SpellBook8(pc, l1iteminstance, false);
							} else if (itemId == 210122 || itemId == 210129 && pc.getLevel() >= 60) {
								SpellBook8(pc, l1iteminstance, false);
							} else if (itemId == 210130 || itemId == 210123
									|| itemId == 30001555 && pc.getLevel() >= 75) {
								SpellBook8(pc, l1iteminstance, false);
							} else if (itemId == 210131 || itemId == 210125 || itemId == 30001553 || itemId == 30001560
									|| itemId == 300015640 || itemId == 30001737
									|| itemId == 30001738 && pc.getLevel() >= 80) {
								SpellBook8(pc, l1iteminstance, false);
							} else if (itemId == 3000094 || itemId == 30001565
									|| itemId == 30001739 && pc.getLevel() >= 80) {
								SpellBook8(pc, l1iteminstance, false);
							} else if (itemId == 4100540 || itemId == 4100712 || itemId == 4100736
									|| itemId == 30001561 && pc.getLevel() >= 85) {
								SpellBook8(pc, l1iteminstance, false);
							} else {
								SpellBook8(pc, l1iteminstance, true);
							}
						} // 劍士
					} else if (itemId >= 4100545 && itemId <= 4100549 || itemId == 4100568
							|| itemId >= 30001536 && itemId <= 30001543 || itemId >= 30001740 && itemId <= 30001742) {
						if (pc.isFencer()) {
							if (itemId == 4100546 || itemId == 4100549 && pc.getLevel() >= 70) {
								SpellBook7(pc, l1iteminstance);
							} else if (itemId == 4100547 || itemId == 4100548 && pc.getLevel() >= 75) {
								SpellBook7(pc, l1iteminstance);
							} else if (itemId == 4100545 || itemId == 4100568 || itemId == 30001538
									|| itemId >= 30001740 && itemId <= 30001742 && pc.getLevel() >= 80) {
								SpellBook7(pc, l1iteminstance);
							} else {
								SpellBook7(pc, l1iteminstance);
							}
						}
						// 新增矛兵技能
					} else if (itemId >= 4100719 && itemId <= 4100724 || itemId >= 30001524 && itemId <= 30001535) {
						if (pc.isLancer()) {
							if (itemId == 4100719 && pc.getLevel() >= 50) {
								LancerSpellBook(pc, l1iteminstance);
							} else if (itemId == 4100720 && pc.getLevel() >= 60) {
								LancerSpellBook(pc, l1iteminstance);
							} else if (itemId == 4100721 || itemId == 30001524
									|| itemId == 30001534 && pc.getLevel() >= 70) {
								LancerSpellBook(pc, l1iteminstance);
							} else if (itemId == 4100722 || itemId == 4100723
									|| itemId == 30001525 && pc.getLevel() >= 75) {
								LancerSpellBook(pc, l1iteminstance);
							} else if (itemId == 4100724 || itemId == 30001528 && pc.getLevel() >= 80) {
								LancerSpellBook(pc, l1iteminstance);
							}
						}
					}*/ else {
							int locX = ((L1EtcItem) l1iteminstance.getItem()).get_locx();
							int locY = ((L1EtcItem) l1iteminstance.getItem()).get_locy();
							short mapId = ((L1EtcItem) l1iteminstance.getItem()).get_mapid();
							if (locX != 0 && locY != 0) {
								if (pc.getMap().isEscapable() || pc.isGm()) {
									if (l1iteminstance.getItemId() == 42007) {
										if (!FIController.isReady()) {
											pc.sendPackets("被遺忘的島嶼尚未開放。");
											break;
										}
									}
									pc.start_teleport(locX, locY, mapId, pc.getHeading(), 18339, true, false);
									pc.getInventory().removeItem(l1iteminstance, 1);
								} else {
									pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
								}
								cancelAbsoluteBarrier(pc);
							} else {
								if (l1iteminstance.getCount() < 1) {
									pc.sendPackets(String.valueOf(new S_ServerMessage(329, l1iteminstance.getLogName())));
								}
							}
						}
						break;
				}
			} else if (l1iteminstance.getItem().getType2() == 1) {
				int min = l1iteminstance.getItem().getMinLevel();
				int max = l1iteminstance.getItem().getMaxLevel();
				if (min != 0 && min > pc.getLevel()) { // 此物品僅能在%0級以上使用
					pc.sendPackets(String.valueOf(new S_ServerMessage(318, String.valueOf(min))));
				} else if (max != 0 && max < pc.getLevel()) { // 此物品僅限%d級以下使用
					if (max < 50) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_LEVEL_OVER, max));
					} else {
						pc.sendPackets(String.valueOf(new S_SystemMessage("此物品僅能在" + max + "級以下使用。")));
					}
				} else {
					if (pc.isGm()) {
						UseWeapon(pc, l1iteminstance);
					} else if (pc.isCrown() && l1iteminstance.getItem().isUseRoyal()
							|| pc.isKnight() && l1iteminstance.getItem().isUseKnight()
							|| pc.isElf() && l1iteminstance.getItem().isUseElf()
							|| pc.isWizard() && l1iteminstance.getItem().isUseMage()
							|| pc.isDarkelf() && l1iteminstance.getItem().isUseDarkelf()
							|| pc.isDragonknight() && l1iteminstance.getItem().isUseDragonKnight()
							|| pc.isBlackwizard() && l1iteminstance.getItem().isUseBlackwizard()
							|| pc.isWarrior() && l1iteminstance.getItem().isUseWarrior()
							|| pc.isFencer() && l1iteminstance.getItem().isUseFencer()
							|| pc.isLancer() && l1iteminstance.getItem().isUseLancer()) {
						UseWeapon(pc, l1iteminstance);
					} else {
						// 1您的職業無法使用此物品。
						pc.sendPackets(String.valueOf(new S_ServerMessage(264)));

					}
				}
			} else if (l1iteminstance.getItem().getType2() == 2) { // 種類：防禦裝備
				if (pc.isGm()) {
					UseArmor(pc, l1iteminstance);
				} else if (pc.isCrown() && l1iteminstance.getItem().isUseRoyal()
						|| pc.isKnight() && l1iteminstance.getItem().isUseKnight()
						|| pc.isElf() && l1iteminstance.getItem().isUseElf()
						|| pc.isWizard() && l1iteminstance.getItem().isUseMage()
						|| pc.isDarkelf() && l1iteminstance.getItem().isUseDarkelf()
						|| pc.isDragonknight() && l1iteminstance.getItem().isUseDragonKnight()
						|| pc.isBlackwizard() && l1iteminstance.getItem().isUseBlackwizard()
						|| pc.isWarrior() && l1iteminstance.getItem().isUseWarrior()
						|| pc.isFencer() && l1iteminstance.getItem().isUseFencer()
						|| pc.isLancer() && l1iteminstance.getItem().isUseLancer()) {

					int min = ((L1Armor) l1iteminstance.getItem()).getMinLevel();
					int max = ((L1Armor) l1iteminstance.getItem()).getMaxLevel();
					if (min != 0 && min > pc.getLevel()) {
						// 此物品僅能在%0級以上使用。
						pc.sendPackets(String.valueOf(new S_ServerMessage(318, String.valueOf(min))));
					} else if (max != 0 && max < pc.getLevel()) {
						if (max < 50) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_LEVEL_OVER, max));
						} else {
							pc.sendPackets(String.valueOf(new S_SystemMessage("此物品僅能在" + max + "級以下使用。")));
						}
					} else {
						UseArmor(pc, l1iteminstance);
					}
				} else {
					// 1您的職業無法使用此物品。
					pc.sendPackets(String.valueOf(new S_ServerMessage(264)));
				}

			}
// TODO: 調整效果延遲或其他物品的計數以消耗的物品ID列表
			if (isDelayEffect) {
				if (itemId == 410008 || itemId == 40414 || itemId == 700012 || itemId == 4200253 || itemId == 30043
						|| itemId == 30045 || itemId == 702 || itemId == 30026 || itemId == 4100131 || itemId == 4100132
						|| itemId == 4100340) {
					int chargeCount = l1iteminstance.getChargeCount();
					Timestamp ts = new Timestamp(System.currentTimeMillis());
					l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
					if (chargeCount <= 1) {
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						l1iteminstance.setLastUsed(ts);
						pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
						pc.getInventory().saveItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
					}
				} else {
					Timestamp ts = new Timestamp(System.currentTimeMillis());
					l1iteminstance.setLastUsed(ts);
					pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_DELAY_EFFECT);
					pc.getInventory().saveItem(l1iteminstance, L1PcInventory.COL_DELAY_EFFECT);
				}
			}

			// pc.addItemDelayTime(l1iteminstance);
			// L1ItemDelay.onItemUse(pc, l1iteminstance); // 開始物品延遲

		}
	}

	private void useItemSealScroll(L1PcInstance pc, L1ItemInstance l1iteminstance, L1ItemInstance l1iteminstance1) {
	}

	@SuppressWarnings("deprecation")
	private void BlessingOfTheSummit(L1PcInstance pc, L1ItemInstance useItem) {
		if (!pc.is_top_ranker()) {
			pc.sendPackets(String.format("當前您的總排名為第 %d 名，因此無法使用此物品。", pc.getRankLevel()));
			// pc.getInventory().removeItem(useItem);
			return;
		}

		Calendar currentDate = RealTimeClock.getInstance().getRealTimeCalendar();
		Timestamp lastUsed = pc.getLastTopBless();
		if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 60 * 60 * 1)) {
			pc.send_effect(12536);
			if (pc.hasSkillEffect(L1SkillId.TOP_RANKER)) {
				pc.setSkillEffect(L1SkillId.TOP_RANKER, 600 * 1000);
			} else {
				pc.setSkillEffect(L1SkillId.TOP_RANKER, 600 * 1000);
			}

			L1SkillUse.on_icons(pc, L1SkillId.TOP_RANKER, 600);

			pc.setLastTopBless(new Timestamp(currentDate.getTimeInMillis()));
		} else {
			long i = (lastUsed.getTime() + (1000 * 60 * 60 * 1)) - currentDate.getTimeInMillis();
			Calendar cal = (Calendar) currentDate.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			pc.sendPackets(new S_SystemMessage(i / 60000 + "分鐘 (" + cal.getTime().getHours() + ":"
					+ cal.getTime().getMinutes() + " 之前無法使用。"), true);
		}
	}

	private boolean createNewItem(L1PcInstance pc, int item_id, int count) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(82)));
				return false;
			}
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getLogName())));
			return true;
		} else {
			return false;
		}
	}

	private boolean createGMItem(L1PcInstance pc, int item_id, int count) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(82)));
				return false;
			}
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getLogName())));
			return true;
		} else {
			return false;
		}
	}

	private boolean createNewItem2(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(82)));
				// 重量負荷不足或背包已滿，無法再攜帶更多物品。
				return false;
			}
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getLogName()))); // %0
			// 已經
			// 拿到手了。
			return true;
		} else {
			return false;
		}
	}

	private boolean createNewItemTrade(L1PcInstance pc, int item_id, int count, int enchant, int bless, int attr,
									   boolean identi) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(identi);
			item.setEnchantLevel(enchant);
			item.setAttrEnchantLevel(attr);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				item.setBless(bless);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(82))); // 重量負荷不足或背包已滿，無法再攜帶更多物品。
				return false;
			}
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getLogName()))); // %0
			// 已經
			// 拿到手了。
			return true;
		} else {
			return false;
		}
	}

	private void AttrEnchant(L1PcInstance pc, L1ItemInstance item, int item_id) {
		int attr_level = item.getAttrEnchantLevel();
		int chance = _random.nextInt(80) + 1;
		int weapon_id = item.getItemId();
		int enchant_floor = 0;
		boolean success = false;
		if (L1ItemInstance.pureAttrEnchantLevel(attr_level) >= 5) {
			pc.sendPackets("無法再進行強化。");
			return;
		}

		if (item_id == 210067 || item_id == 30001363) { // 火之武器強化卷軸
			if (attr_level == 0) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_FIRE) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(1);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 1;
			} else if (attr_level == 1) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_FIRE1) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(2);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 2;
			} else if (attr_level == 2) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_FIRE2) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(3);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 3;
			} else if (attr_level == 3) {
				if (item.getEnchantLevel() >= 9 || is_legend_weapon(weapon_id) || is_ancient_weapon(weapon_id)) {
					if (chance < Config.ServerEnchant.ATTR_ENCHANT_FIRE3) {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
						item.setAttrEnchantLevel(4);
						success = true;
					} else {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
					}
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
					return;
				}
				enchant_floor = 4;
			} else if (attr_level == 4) {
				if (item.getEnchantLevel() >= 10 || is_legend_weapon(weapon_id) || is_ancient_weapon(weapon_id)) {
					if (chance < Config.ServerEnchant.ATTR_ENCHANT_FIRE4) {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
						item.setAttrEnchantLevel(5);
						success = true;
					} else {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
					}
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
					return;
				}
				enchant_floor = 5;
			} else if (attr_level == 14) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
		} else if (item_id == 210066 || item_id == 30001362) { // 水之武器強化卷軸
			if (attr_level == 0) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_WATER) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(6);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 1;
			} else if (attr_level == 6) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_WATER1) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(7);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 2;
			} else if (attr_level == 7) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_WATER2) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(8);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 3;
			} else if (attr_level == 8) {
				if (item.getEnchantLevel() >= 9 || is_legend_weapon(weapon_id) || is_ancient_weapon(weapon_id)) {
					if (chance < Config.ServerEnchant.ATTR_ENCHANT_WATER3) {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
						item.setAttrEnchantLevel(9);
						success = true;
					} else {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
					}
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
					return;
				}
				enchant_floor = 4;
			} else if (attr_level == 9) {
				if (item.getEnchantLevel() >= 10 || is_legend_weapon(weapon_id) || is_ancient_weapon(weapon_id)) {
					if (chance < Config.ServerEnchant.ATTR_ENCHANT_WATER4) {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
						item.setAttrEnchantLevel(10);
						success = true;
					} else {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
					}
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
					return;
				}
				enchant_floor = 5;
			} else if (attr_level == 10) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
		} else if (item_id == 210064 || item_id == 30001360) { // ....
			if (attr_level == 0) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_WISH) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(11);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 1;
			} else if (attr_level == 11) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_WISH1) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(12);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 2;
			} else if (attr_level == 12) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_WISH2) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(13);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 3;
			} else if (attr_level == 13) {// 9以下強化 3階段
				if (item.getEnchantLevel() >= 9 || is_legend_weapon(weapon_id) || is_ancient_weapon(weapon_id)) {
					if (chance < Config.ServerEnchant.ATTR_ENCHANT_WISH3) {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
						item.setAttrEnchantLevel(14);
						success = true;
					} else {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
					}
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
					return;
				}
				enchant_floor = 4;
			} else if (attr_level == 14) {// 10或以上 4階段
				if (item.getEnchantLevel() >= 10 || is_legend_weapon(weapon_id) || is_ancient_weapon(weapon_id)) {
					if (chance < Config.ServerEnchant.ATTR_ENCHANT_WISH4) {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
						item.setAttrEnchantLevel(15);
						success = true;
					} else {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
					}
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
					return;
				}
				enchant_floor = 5;
			} else if (attr_level == 15) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
		} else if (item_id == 210065 || item_id == 30001361) { // 地之武器強化卷軸
			if (attr_level == 0) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_EARTH) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(16);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 1;
			} else if (attr_level == 16) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_EARTH1) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(17);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 2;
			} else if (attr_level == 17) {
				if (chance < Config.ServerEnchant.ATTR_ENCHANT_EARTH2) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
					item.setAttrEnchantLevel(18);
					success = true;
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
				}
				enchant_floor = 3;
			} else if (attr_level == 18) {
				if (item.getEnchantLevel() >= 9 || is_legend_weapon(weapon_id) || is_ancient_weapon(weapon_id)) {
					if (chance < Config.ServerEnchant.ATTR_ENCHANT_EARTH3) {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
						item.setAttrEnchantLevel(19);
						success = true;
					} else {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
					}
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
					return;
				}
				enchant_floor = 4;
			} else if (attr_level == 19) {
				if (item.getEnchantLevel() >= 10 || is_legend_weapon(weapon_id) || is_ancient_weapon(weapon_id)) {
					if (chance < Config.ServerEnchant.ATTR_ENCHANT_EARTH4) {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
						item.setAttrEnchantLevel(20);
						success = true;
					} else {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1411, item.getLogName())));
					}
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
					return;
				}
				enchant_floor = 5;
			} else if (attr_level == 20) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
		SC_ENCHANT_RESULT.send_attr_enchant(pc, item, success ? 0 : 2, attr_level, enchant_floor);
		pc.getInventory().consumeItem(item_id, 1);
		pc.getInventory().updateItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
		pc.getInventory().saveItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
	}

	public void AttrChangeEnchant(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int attr_level = item.getAttrEnchantLevel();
		int AttrScroll = 0;
		boolean success = false;

		if (itemId == 560030) { // 火之屬性(火靈的屬性變換卷軸)
			AttrScroll = 0;
		} else if (itemId == 560031) { // 水之屬性(水靈的屬性變換卷軸)
			AttrScroll = 5;
		} else if (itemId == 560032) { // 風之屬性(風靈的屬性變換卷軸)
			AttrScroll = 10;
		} else if (itemId == 560033) { // 地之屬性(地靈的屬性變換卷軸)
			AttrScroll = 15;
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
			// 什麼都沒有發生。
			return;
		}
		if (!pc.getInventory().checkItem(itemId, 1)) {
			return;
		}
		if (attr_level > 0) {
			if (AttrScroll + 1 <= attr_level && attr_level <= AttrScroll + 5) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(3319)));
				// 無法使用於相同屬性。
				return;
			}
			if (attr_level % 5 == 0) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(3296, item.getLogName())));
				// 強化: 輝煌的大自然之力滲透在 %0。
				item.setAttrEnchantLevel(AttrScroll + 5);
				success = true;
				SC_ENCHANT_RESULT.send_attr_enchant(pc, item, success ? 0 : 2, AttrScroll + 5, 5);
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(1410, item.getLogName())));
				// 強化: %0中滲透著玲瓏的大自然之力。
				item.setAttrEnchantLevel(attr_level % 5 + AttrScroll);
				success = true;
				SC_ENCHANT_RESULT.send_attr_enchant(pc, item, success ? 0 : 2, attr_level % 5 + AttrScroll, 5);
			}
			pc.getInventory().consumeItem(itemId, 1);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
			pc.getInventory().saveItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 什麼都沒有發生。
		}
	}

	private void enchant_badge(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
		int target_enchant = target.getEnchantLevel();
		String target_Name = target.getItem().getName();
		if (target.getBless() >= 128) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
			return;
		}
		if (target_enchant < 3) {
			pc.sendPackets(String.format("徽章保護卷軸可使用到 +%d。", 3));
			return;
		}
		if (target_enchant >= Config.ServerEnchant.badgeLevel) {
			pc.sendPackets(String.format("徽章無法使用保護卷軸進行 +%d 以上的強化。", Config.ServerEnchant.badgeLevel));
			return;
		}

		int index = target_enchant < 0 ? 0
				: target_enchant >= Config.ServerEnchant.BADGEENCHANT.length
				? Config.ServerEnchant.BADGEENCHANT.length - 1
				: target_enchant;
		if (MJRnd.isWinning(1000000, Config.ServerEnchant.BADGEENCHANT[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(String.valueOf(new S_SystemMessage("瞬間強烈的光芒，使強化成功。")));
		} else {
			pc.sendPackets(String.format("%s 瞬間發出強烈的光芒，但強化失敗了。", target_Name));
			EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN, target_enchant - target_enchant,
					target);
		}
		pc.getInventory().removeItem(scroll, 1);
	}

	private void enchant_sentence(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
		int target_enchant = target.getEnchantLevel();
		String target_Name = target.getItem().getName();
		if (target.getBless() >= 128) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
			return;
		}
		if (target_enchant < 3) {
			pc.sendPackets(String.format("徽章保護卷軸可使用到 +%d。", 3));
			return;
		}
		if (target_enchant >= Config.ServerEnchant.SentenceLevel) {
			pc.sendPackets(String.format("徽章無法使用保護卷軸進行 +%d 以上的強化。", Config.ServerEnchant.SentenceLevel));
			return;
		}

		int index = target_enchant < 0 ? 0
				: target_enchant >= Config.ServerEnchant.Sentence.length ? Config.ServerEnchant.Sentence.length - 1
				: target_enchant;
		if (MJRnd.isWinning(1000000, Config.ServerEnchant.Sentence[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(String.valueOf(new S_SystemMessage("瞬間強烈的光芒，使強化成功。")));
		} else {
			pc.sendPackets(String.format("%s 瞬間發出強烈的光芒，但強化失敗了。", target_Name));
			EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN, target_enchant - target_enchant,
					target);
		}
		pc.getInventory().removeItem(scroll, 1);
	}

	private void enchant_roomtis(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
		int target_type = target.getItem().getType();
		int target_enchant = target.getEnchantLevel();
		String target_Name = target.getItem().getName();
		if (target_type < 8 || target_type > 12 || target.getBless() >= 128) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
			return;
		}
		if (target_enchant < 3) {
			pc.sendPackets(String.format("羅姆提斯保護卷軸可使用到 +%d。", 3));
			return;
		}
		if (target_enchant >= Config.ServerEnchant.RoomtisLevel) {
			pc.sendPackets(String.format("羅姆提斯類飾品無法使用保護卷軸進行 +%d 以上的強化。", Config.ServerEnchant.RoomtisLevel));
			return;
		}

		int index = target_enchant < 0 ? 0
				: target_enchant >= Config.ServerEnchant.Roomtis.length ? Config.ServerEnchant.Roomtis.length - 1
				: target_enchant;
		if (MJRnd.isWinning(1000000, Config.ServerEnchant.Roomtis[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(String.valueOf(new S_SystemMessage("瞬間強烈的光芒，使強化成功。")));
		} else {
			pc.sendPackets(String.format("%s 瞬間發出強烈的光芒，但強化失敗了。", target_Name));
			EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN, target_enchant - target_enchant,
					target);
		}
		pc.getInventory().removeItem(scroll, 1);
	}

	private void enchant_snapper(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
		int target_type = target.getItem().getType();
		int target_enchant = target.getEnchantLevel();
		String target_Name = target.getItem().getName();
		if (target_type < 8 || target_type > 12 || target.getBless() >= 128) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
			return;
		}
		if (target_enchant < 3) {
			pc.sendPackets(String.format("斯納普保護卷軸可使用到 +%d。", 3));
			return;
		}
		if (target_enchant >= Config.ServerEnchant.SanpperLevel) {
			pc.sendPackets(String.format("斯納普類飾品無法使用保護卷軸進行 +%d 以上的強化。", Config.ServerEnchant.SanpperLevel));
			return;
		}

		int index = target_enchant < 0 ? 0
				: target_enchant >= Config.ServerEnchant.Sanpper.length ? Config.ServerEnchant.Sanpper.length - 1
				: target_enchant;
		if (MJRnd.isWinning(1000000, Config.ServerEnchant.Sanpper[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(String.valueOf(new S_SystemMessage("瞬間強烈的光芒，使強化成功。")));
		} else {
			pc.sendPackets(String.format("%s 瞬間發出強烈的光芒，但強化失敗了。", target_Name));
			EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN, target_enchant - target_enchant,
					target);
		}
		pc.getInventory().removeItem(scroll, 1);
	}

	private void enchant_orim(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
		int target_type = target.getItem().getType();
		int target_enchant = target.getEnchantLevel();
		String target_Name = target.getItem().getName();
		if (target_type < 8 || target_type > 12 || target.getBless() >= 128) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
			return;
		}
		if (target_enchant >= Config.ServerEnchant.Accessory_Limit) {
			pc.sendPackets(String.format("飾品無法進行 +%d 以上的強化。", Config.ServerEnchant.Accessory_Limit));
			return;
		}

		int index = target_enchant < 0 ? 0
				: target_enchant >= Config.ServerEnchant.Normal_orim.length
				? Config.ServerEnchant.Normal_orim.length - 1
				: target_enchant;
		if (MJRnd.isWinning(1000000, Config.ServerEnchant.Normal_orim[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(String.valueOf(new S_SystemMessage("瞬間強烈的光芒，使強化成功。")));
		} else if (target_enchant == 0) {
			SuccessEnchant(pc, target, pc.getNetConnection(), 0);
		} else {
			pc.sendPackets(String.format("%s 瞬間發出強烈的光芒，但強化失敗了。", target_Name));
			SuccessEnchant(pc, target, pc.getNetConnection(), -1);
		}
		pc.getInventory().removeItem(scroll, 1);
	}

	private void enchant_bless_orim(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
		int target_type = target.getItem().getType();
		int target_enchant = target.getEnchantLevel();
		String target_Name = target.getItem().getName();
		if (target_type < 8 || target_type > 12 || target.getBless() >= 128) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
			return;
		}
		if (target_enchant >= Config.ServerEnchant.Accessory_Limit) {
			pc.sendPackets(String.format("飾品無法進行 +%d 以上的強化。", Config.ServerEnchant.Accessory_Limit));
			return;
		}

		int index = target_enchant < 0 ? 0
				: target_enchant >= Config.ServerEnchant.Bless_orim.length ? Config.ServerEnchant.Bless_orim.length - 1
				: target_enchant;
		if (MJRnd.isWinning(1000000, Config.ServerEnchant.Bless_orim[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(String.valueOf(new S_SystemMessage("瞬間強烈的光芒，使強化成功。")));
		} else {
			pc.sendPackets(String.format("%s 瞬間發出強烈的光芒，但強化失敗了。", target_Name));
			EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN, target_enchant - target_enchant,
					target);
		}
		pc.getInventory().removeItem(scroll, 1);
	}

	private void enchant_dragon(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) { // TeamTheday by.쥬드
		int target_enchant = target.getEnchantLevel();
		String target_Name = target.getItem().getName();
		if (target.getBless() >= 128) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
			return;
		}
		if (target_enchant < 7) {
			pc.sendPackets(String.format("龍之襯衫保護卷軸可使用到 +%d。", 7));
			return;
		}
		if (target_enchant >= Config.ServerEnchant.DragonLevel) {
			pc.sendPackets(String.format("龍之襯衫無法使用保護卷軸進行 +%d 以上的強化。", Config.ServerEnchant.DragonLevel));
			return;
		}

		int index = target_enchant < 0 ? 0
				: target_enchant >= Config.ServerEnchant.Dragon.length ? Config.ServerEnchant.Dragon.length - 1
				: target_enchant;
		if (MJRnd.isWinning(1000000, Config.ServerEnchant.Dragon[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(String.valueOf(new S_SystemMessage("瞬間強烈的光芒，使強化成功。")));
		} else {
			pc.sendPackets(String.format("%s 瞬間發出強烈的光芒，但強化失敗了。", target_Name));
			EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN, target_enchant - target_enchant,
					target);
		}
		pc.getInventory().removeItem(scroll, 1);
	}

	public static void SuccessEnchant(L1PcInstance pc, L1ItemInstance item, GameClient client, int i) {
		boolean equipcheck = false;
		String s = "";
		String sa = "";
		String sb = "";
		String s1 = item.getName();
		String pm = "";
		int msgnum = 161;

		if (item.isEquipped()) {
			pc.getInventory().setEquipped(item, false);
			equipcheck = true;
		}

		if (item.getEnchantLevel() > 0) {
			pm = "+";
		}
		if (item.getItem().getType2() == 1) {
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				switch (i) {
					case -1:
						s = s1;
						sa = "$246";
						sb = "$247";
						break;

					case 1: // '\001'
						s = s1;
						sa = "$245";
						sb = "$247";
						break;

					case 2: // '\002'
						s = s1;
						sa = "$245";
						sb = "$248";
						break;

					case 3: // '\003'
						s = s1;
						sa = "$245";
						sb = "$248";
						break;
				}
			} else {
				switch (i) {
					case -1:
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
						// 1%0 瞬間發出 %2%1 的光芒。
						sa = "$246";
						sb = "$247";
						break;

					case 1: // ''
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
						// 1%0 瞬間發出 %2%1 的光芒。
						sa = "$245";
						sb = "$247";
						break;

					case 2: // ''
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
						// 1%0 瞬間發出 %2%1 的光芒。
						sa = "$245";
						sb = "$248";
						break;

					case 3: // ''
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
						// 1%0 瞬間發出 %2%1 的光芒。
						sa = "$245";
						sb = "$248";
						break;
				}
			}
		} else if (item.getItem().getType2() == 2) {
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				switch (i) {
					case -1:
						s = s1;
						sa = "$246";
						sb = "$247";
						break;
					case 0:
						msgnum = 4056;
						s = s1;
						sa = "$246";
						sb = "$247";
						break;
					case 1: // '\001'
						s = s1;
						sa = "$252";
						sb = "$247 ";
						break;

					case 2: // '\002'
						s = s1;
						sa = "$252";
						sb = "$248 ";
						break;

					case 3: // '\003'
						s = s1;
						sa = "$252";
						sb = "$248 ";
						break;
				}
			} else {
				switch (i) {
					case -1:
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
						// 1%0 瞬間發出 %2%1 的光芒。
						sa = "$246";
						sb = "$247";
						break;

					case 1: // ''
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
						// 1%0 瞬間發出 %2%1 的光芒。
						sa = "$252";
						sb = "$247 ";
						break;

					case 2: // ''
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
						// 1%0 瞬間發出 %2%1 的光芒。
						sa = "$252";
						sb = "$248 ";
						break;

					case 3: // ''
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
						// 1%0 瞬間發出 %2%1 的光芒。
						sa = "$252";
						sb = "$248 ";
						break;
				}
			}
		}
		pc.sendPackets(String.valueOf(new S_ServerMessage(msgnum, s, sa, sb)));
		int oldEnchantLvl = item.getEnchantLevel();
		int newEnchantLvl = item.getEnchantLevel() + i;

		if (oldEnchantLvl <= newEnchantLvl) {
			EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.SUCCESS, newEnchantLvl - oldEnchantLvl,
					item);
		} else if (newEnchantLvl > 0) {
			EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.SUCCESS, -oldEnchantLvl - newEnchantLvl,
					item);
		} else {
			EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.SUCCESS, -oldEnchantLvl - -newEnchantLvl,
					item);
		}

		int safe_enchant = item.getItem().get_safeenchant();
		item.setEnchantLevel(newEnchantLvl);
		client.getActiveChar().getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
		pc.saveInventory();
		if (newEnchantLvl > safe_enchant) {
			client.getActiveChar().getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
			pc.saveInventory();
			/** 儲存日誌文件 **/
			LoggerInstance.getInstance().addEnchant(pc, item, true);
			// TODO 指定物品強化信息
			if (ItemMessageTable.getInstance().isEnchantMessage(item.getItemId())) {
				L1ItemMessage temp = ItemMessageTable.getInstance().getEnchantMessage(item.getItemId());
				if (temp != null) {
					if (temp.getType() == 2 && newEnchantLvl >= temp.getOption()) {
						if (temp.isMentuse()) {
							broadcastEnchantMessage(item.getItem(), oldEnchantLvl, item.get_Carving() == 1);
						}
					}
				}
				// TODO 武器強化信息
			} else if (Config.ServerEnchant.MasterEnchantMess && item.getItem().getType2() == 1
					&& newEnchantLvl >= Config.ServerEnchant.Enchant_Count_Weapon) {
				broadcastEnchantMessage(item.getItem(), oldEnchantLvl, item.get_Carving() == 1);
				// TODO 防具強化信息
			} else if (Config.ServerEnchant.MasterEnchantMess && item.getItem().getType2() == 2
					[09:39]
        && newEnchantLvl >= Config.ServerEnchant.Enchant_Count_Armor) {
				broadcastEnchantMessage(item.getItem(), oldEnchantLvl, item.get_Carving() == 1);
				// TODO 飾品強化信息
		} else if (Config.ServerEnchant.MasterEnchantMess && item.getItem().getType2() == 2
				&& (item.getItem().getType() == 8 || item.getItem().getType() == 9 || item.getItem().getType() == 10
				|| item.getItem().getType() == 11 || item.getItem().getType() == 12
				|| item.getItem().getType() == 28 || item.getItem().getType() == 30)
				&& newEnchantLvl >= Config.ServerEnchant.Enchant_Count_Accessory) {
			if (oldEnchantLvl < newEnchantLvl) {
				broadcastEnchantMessage(item.getItem(), oldEnchantLvl, item.get_Carving() == 1);
			}
		}
		if (item.getItem().getType2() == 1 && Config.LogStatus.LoggingWeaponEnchant != 0) {
			if (safe_enchant == 0 || newEnchantLvl >= Config.LogStatus.LoggingWeaponEnchant) {
			}
		}
		if (item.getItem().getType2() == 2 && Config.LogStatus.LoggingArmorEnchant != 0) {
			if (safe_enchant == 0 || newEnchantLvl >= Config.LogStatus.LoggingArmorEnchant) {
			}
		}

		if (equipcheck) {
			pc.getInventory().setEquipped(item, true);
			equipcheck = false;
		}
		pc.sendPackets(new S_SPMR(pc));
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	public static void broadcastEnchantMessage(L1Item item, int enchantLevel, boolean carving) {
		String message = carving ? MJString.concat(item.getNameId(), "$28473") : item.getNameId();
		if (item.getType2() == 2) {
			L1World.getInstance()
					.broadcastPacketToAll(S_EnchantMessage.newSilverMessage(enchantLevel, item.getGfxId(), message));
		} else {
			L1World.getInstance()
					.broadcastPacketToAll(S_EnchantMessage.newBlueMessage(enchantLevel, item.getGfxId(), message));
		}
	}

	public static void FailureEnchant(L1PcInstance pc, L1ItemInstance item, GameClient client) {
		// String s = "";
		// String sa = "";
		int itemType = item.getItem().getType2();
		int itemId = item.getItem().getItemId();
		// String nameId = item.getName();
		// String pm = "";

			// if (itemType == 1) { // 武器
// if (!item.isIdentified() || item.getEnchantLevel() == 0) {
// s = nameId;
// // 1%0 強烈地 %1 發光後，沒有消失。
// sa = "$245";
// } else {
// if (item.getEnchantLevel() > 0) {
// pm = "+";
//
// }
// s = (new StringBuilder()).append(pm +
// item.getEnchantLevel()).append(" ").append(nameId).toString();
// // 1%0 強烈地 %1 發光後，沒有消失。
// sa = "$245";
// }
// } else if (itemType == 2) { // 防具
// if (!item.isIdentified() || item.getEnchantLevel() == 0) {
// s = nameId;
// // 1%0 強烈地 %1 發光後，沒有消失。
// sa = "$252";
// } else {
// if (item.getEnchantLevel() > 0) {
// pm = "+";
// }
// s = (new StringBuilder()).append(pm +
// item.getEnchantLevel()).append(" ").append(nameId).toString();
// // 1%0 強烈地 %1 發光後，沒有消失。
// sa = "$252";
// }
// }
// if ((itemId >= 1115 && itemId <= 1118) || (itemId >= 22250 && itemId
// <= 22252)) { // 神秘的日期在推進部分？
		if ((itemId >= 1115 && itemId <= 1118)) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(1310)));
			pc.getInventory().setEquipped(item, false);
			item.setEnchantLevel(0);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
			pc.saveInventory();
			if (itemType == 1) {
			} else if (itemType == 2) {
			}
		} else {
			EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_DESTROY, 0, item);
			// pc.sendPackets(new S_ServerMessage(164, s, sa)); // 消失的訊息已在客戶端顯示
			// 註釋掉
			pc.getInventory().removeItem(item, item.getCount());
			/** 로그파일저장 **/
			LoggerInstance.getInstance().addEnchant(pc, item, false);
			TJCouponProvider.provider().onLostItem(pc, item);
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	private void UseExpPotion1(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) { // 衰退藥水狀態
			pc.sendPackets(String.valueOf(new S_ServerMessage(698, "")));
			return;
		}
		cancelAbsoluteBarrier(pc);

		int time = 1800;

		pc.setSkillEffect(L1SkillId.EXP_BUFF, time * 1000);
		S_SkillSound s = new S_SkillSound(pc.getId(), 10049);
		pc.sendPackets(s, false);
		pc.broadcastPacket(s);
		long hasad = pc.getAccount().getBlessOfAin();
		if (hasad < 10000) {
			pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.EXP_BUFF + 1, 5087, true).toString()); // 安全區域
		} else {
			pc.sendPackets(S_InventoryIcon.icoNew(L1SkillId.EXP_BUFF, 5087, time, true).toString()); // 狩獵場
		}
			pc.sendPackets(String.valueOf(new S_ServerMessage(5087)));
		}

		private void UseExpPotion(L1PcInstance pc, int item_id) {
			if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) { // 衰退藥水狀態
			pc.sendPackets(String.valueOf(new S_ServerMessage(698, "")));
			return;
		}
		if (pc.hasSkillEffect(L1SkillId.EXP_POTION_Event)) {
			pc.removeSkillEffect(L1SkillId.EXP_POTION_Event);
		}

		cancelAbsoluteBarrier(pc);

		if (item_id != 210094 && item_id != 30105 && item_id != 3000456)
			return;
		int time = 1800;
		pc.send_effect(13249);
		L1SkillUse.on_icons(pc, L1SkillId.EXP_POTION, time);
		pc.setSkillEffect(L1SkillId.EXP_POTION, time * 1000);
		pc.sendPackets(1313);
	}

	private void UseExpPotionEvent(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) { // 衰退藥水狀態
			pc.sendPackets(String.valueOf(new S_ServerMessage(698, "")));
			return;
		}
		if (pc.hasSkillEffect(L1SkillId.EXP_POTION)) {
			pc.removeSkillEffect(L1SkillId.EXP_POTION);
		}

		cancelAbsoluteBarrier(pc);

		if (item_id != 210094 && item_id != 4100306 && item_id != 3000456)
			return;
		int time = 1800;

		pc.setSkillEffect(L1SkillId.EXP_POTION_Event, time * 1000);
		S_SkillSound s = new S_SkillSound(pc.getId(), 13249);
		pc.sendPackets(s, false);
		pc.broadcastPacket(s);
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.RESTAT);
		noti.set_spell_id(L1SkillId.EXP_POTION_Event);
		noti.set_duration(time);
		noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
		noti.set_on_icon_id(3069);
		noti.set_off_icon_id(6768);
		noti.set_icon_priority(10);
		noti.set_tooltip_str_id(5095);
		noti.set_new_str_id(5095);
		noti.set_end_str_id(0);
		noti.set_is_good(true);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);

		pc.sendPackets(String.valueOf(new S_ServerMessage(1313)));
	}

	private void useBluePotion(L1PcInstance pc, int itemId) {
		if (pc.hasSkillEffect(DECAY_POTION)) {// 衰退藥水
			pc.sendPackets(String.valueOf(new S_ServerMessage(698)));
			return;
		}

		cancelAbsoluteBarrier(pc);// 絕對解除

		int time = 0;
		switch (itemId) {
			case 4100659:
			case 30083:
			case 40015:
			case 40736:
//			time = 600; // 週六特化
				time = 1200;
				break;
			case 140015:
				time = 700;
				break;
			case 41142:
				time = 300;
				break;
			case 210114:
				time = 1800;
				break;
			default:
				break;
		}
		pc.sendPackets(new S_SkillIconGFX(34, time, true).toString());
		pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 190)));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
		if (itemId == 41142) {
			pc.setSkillEffect(STATUS_BLUE_POTION2, time * 1000);
		} else {
			pc.setSkillEffect(STATUS_BLUE_POTION, time * 1000);
		}
			pc.sendPackets(String.valueOf(new S_ServerMessage(1007))); // MP 恢復速度變快
	}

		/**
		 * @param pc
		 * @param itemId
		 */
		public static void useGreenPotion(L1PcInstance pc, int itemId) {
			if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) { // 衰退藥水狀態
				pc.sendPackets(String.valueOf(new S_ServerMessage(698))); // 1受到魔力的影響，什麼也無法飲用。
				// 不能飲用任何東西。
				return;
			}

			// 絕對屏障解除
			cancelAbsoluteBarrier(pc);

			int time = 0; // 秒
			if (itemId == L1ItemId.POTION_OF_HASTE_SELF || itemId == 7006 || itemId == 4100662) { // 綠藥水
				//time = 300; // 週六特化
				time = 1200;
			} else if (itemId == L1ItemId.B_POTION_OF_HASTE_SELF) { // 祝福部分綠藥水
				time = 350;
			} else if (itemId == 40018 || itemId == 140018 || itemId == 41342) { // 強化
				// 綠藥水部分,
				// 祝福葡萄酒,
				time = 1800;
			} else if (itemId == 30158) { // 濃縮速度藥水
				time = 1200;
			} else if (itemId == 41338) { // 祝福葡萄酒
				time = 2250;
			} else if (itemId == 40039) { // 葡萄酒
				time = 600;
			} else if (itemId == 40040) { // 威士忌
				time = 900;
			} else if (itemId == 30067) {
				time = 300;
			} else if (itemId == 40030) { // 象牙塔的加速藥水部分
			time = 1800;
		} else if (itemId == 41261 || itemId == 41262 || itemId == 41268 || itemId == 41269 || itemId == 41271
				|| itemId == 41272 || itemId == 41273) {
			time = 23;
		} else if (itemId == 41921) {
			time = 60 * 60;
		}

		pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 191)));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 191));
		pc.sendPackets(String.valueOf(new S_ServerMessage(183)));
			// XXX: 不明白在裝備加速物品時，是否會解除醉酒狀態
			if (pc.getHasteItemEquipped() > 0) {
				return;
			}
			// 解除醉酒狀態
			pc.setDrink(false);

			// 不會與加速效果和強化加速效果重複
		if (pc.hasSkillEffect(HASTE)) {
			pc.killSkillEffectTimer(HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0).toString());
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		} else if (pc.hasSkillEffect(GREATER_HASTE)) {
			pc.killSkillEffectTimer(GREATER_HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0).toString());
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		} else if (pc.hasSkillEffect(STATUS_HASTE)) {
			pc.killSkillEffectTimer(STATUS_HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0).toString());
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		}
			/*
			 * else if (pc.hasSkillEffect(STATUS_HASTE)) { // 加速效果重疊
			 * int currentTime = 0;
			 * currentTime = pc.getSkillEffectTimeSec(STATUS_HASTE);
			 * if (currentTime >= 7200) {
			 * time = 7200;
			 * } else {
			 * time += currentTime;
			 * }
			 * }
			 */

			// 只會解除緩速、強化緩速、糾纏術的緩速狀態
			if (pc.hasSkillEffect(SLOW)) { // 緩速
			pc.killSkillEffectTimer(SLOW);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0).toString());
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
		} else {
			pc.sendPackets(new S_SkillHaste(pc.getId(), 1, time >= 7200 ? 7200 : time).toString());
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
			pc.setMoveSpeed(1);
			pc.setSkillEffect(STATUS_HASTE, (time >= 7200 ? 7200 : time) * 1000);
		}
	}

	private void useBravePotion(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(698)));
			return;
		}
			// 絕對屏障解除
			cancelAbsoluteBarrier(pc);

			int time = 0;
			if (item_id == L1ItemId.POTION_OF_EMOTION_BRAVERY || item_id == 30073 || item_id == 4100663) {
				//time = 300; // 週六特化
				time = 1200;
			} else if (item_id == L1ItemId.B_POTION_OF_EMOTION_BRAVERY) {
				time = 350;
			} else if (item_id == 41415) {
				time = 1800;
			} else if (item_id == 712) {
				time = 1800;
			} else if (item_id == 40068 || item_id == 30076 || item_id == 4100664) {
			//time = 480; // 週六特化
			time = 1200;
			if (pc.hasSkillEffect(STATUS_BRAVE)) {
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0).toString());
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			pc.remove_elf_second_brave();
		} else if (item_id == 140068) {
			time = 700;
			if (pc.hasSkillEffect(STATUS_BRAVE)) {
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0).toString());
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			pc.remove_elf_second_brave();
		} else if (item_id == 210110) {
			time = 1800;
			if (pc.hasSkillEffect(STATUS_BRAVE)) {
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0).toString());
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
		} else if (item_id == 40031 || item_id == 30075) {
			time = 600;
		} else if (item_id == 40733) {
			time = 600;
			if (pc.hasSkillEffect(STATUS_ELFBRAVE)) {
				pc.killSkillEffectTimer(STATUS_ELFBRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0).toString());
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(HOLY_WALK)) {
				pc.killSkillEffectTimer(HOLY_WALK);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0).toString());
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(MOVING_ACCELERATION)) {
				pc.killSkillEffectTimer(MOVING_ACCELERATION);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0).toString());
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(STATUS_FRUIT)) {
				pc.killSkillEffectTimer(STATUS_FRUIT);
				pc.setBraveSpeed(0);
			}
		}

		if (item_id == 40068 || item_id == 140068 || item_id == 210110 || item_id == 30076 || item_id == 4100664) { // 엘븐
			pc.sendPackets(new S_SkillBrave(pc.getId(), 3, time).toString());
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0));
			pc.setSkillEffect(STATUS_ELFBRAVE, time * 1000);
			pc.setBraveSpeed(3);
		} else {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, time).toString());
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0));
			pc.setSkillEffect(STATUS_BRAVE, time * 1000);
			pc.setBraveSpeed(1);
		}
		pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 751)));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 751));
	}

		public static void useDragonPearl(L1PcInstance pc, int itemId) { // 龍之珍珠
			if (pc.hasSkillEffect(DECAY_POTION) == true) { // 衰退藥水狀態
			pc.sendPackets(String.valueOf(new S_ServerMessage(698)));
			return;
		}
		cancelAbsoluteBarrier(pc);
		int time = Config.MagicAdSetting.DRAGON_PEARL_TIME;
		if (itemId == 41921) {
			time = 60 * 60;
		}
		time = 1200; // 週六特化
		if (pc.hasSkillEffect(STATUS_DRAGON_PEARL)) {
			pc.killSkillEffectTimer(STATUS_DRAGON_PEARL);
			pc.sendPackets(String.valueOf(new S_Liquor(pc.getId(), 0)));
			pc.setPearl(0);
		}
		pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 197)));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 197));
		pc.setSkillEffect(STATUS_DRAGON_PEARL, time * 1000);

		pc.sendPackets(String.valueOf(new S_Liquor(pc.getId(), 8)));
		pc.broadcastPacket(new S_Liquor(pc.getId(), 8));
		pc.setPearl(1);
		pc.sendPackets(new S_ServerMessage(1065, time));
	}

	private void useFruit(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(DECAY_POTION) == true) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(698)));
			return;
		}
		cancelAbsoluteBarrier(pc);

		int time = 0;
		if (item_id == 210036 || item_id == 30077 || item_id == 4100665) {
			//time = 480;
			time = 1200;
		}
		if (item_id == 713) {
			time = 1800;
		}
		pc.removeSkillEffect(STATUS_FRUIT);
		pc.setSkillEffect(STATUS_FRUIT, time * 1000);
			// TODO 添加黑馬效果
			pc.sendPackets(new S_SkillBrave(pc.getId(), pc.getBraveSpeed(), time));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), pc.getBraveSpeed(), time));
			pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 7110)));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 7110));
		}

		private void useWisdomPotion(L1PcInstance pc, int item_id) {
			if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) { // 衰退藥水狀態
				pc.sendPackets(String.valueOf(new S_ServerMessage(698))); // 1由於魔力無法飲用任何物品。
															// 無法飲用。
				return;
			}
			// 絕對屏障解除
			cancelAbsoluteBarrier(pc);

			int time = 0; // 時間應設定為4的倍數
			switch (item_id) {
				case 4100660:
				case 40016:
				case 30089:
//            time = 300;// 300 // 週六特化
				time = 1200;
				break;
			case 140016:
				time = 360;// 360
				break;
			default:
				return;
		}

		if (pc.hasSkillEffect(L1SkillId.STATUS_WISDOM_POTION_POWER)) {
			pc.removeSkillEffect(L1SkillId.STATUS_WISDOM_POTION_POWER);
		}

		if (!pc.hasSkillEffect(STATUS_WISDOM_POTION)) {
			pc.getAbility().addSp(2);
			pc.addMpr(2);
		}

		L1SkillUse.on_icons(pc, STATUS_WISDOM_POTION, time);
		// pc.sendPackets(new S_SkillIconWisdomPotion((int) (time)));
		pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 750)));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));
		pc.setSkillEffect(STATUS_WISDOM_POTION, time * 1000);
	}

	private void useWisdomPotion_Power(L1PcInstance pc, int item_id) {
			if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) { // 衰退藥水狀態
				pc.sendPackets(String.valueOf(new S_ServerMessage(698))); // 1由於魔力無法飲用任何物品
				return;
			}
			// 絕對屏障解除
			cancelAbsoluteBarrier(pc);

			int time = 0; // 時間應設定為4的倍數
		switch (item_id) {
			case 210113:
				time = 1200;
				break;
			default:
				return;
		}

		if (pc.hasSkillEffect(L1SkillId.STATUS_WISDOM_POTION)) {
			pc.removeSkillEffect(L1SkillId.STATUS_WISDOM_POTION);
		}

		if (!pc.hasSkillEffect(STATUS_WISDOM_POTION_POWER)) {
			pc.getAbility().addSp(2);
			pc.addMpr(2);
		}

		L1SkillUse.on_icons(pc, STATUS_WISDOM_POTION_POWER, time);
		pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 750)));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));
		pc.setSkillEffect(STATUS_WISDOM_POTION_POWER, time * 1000);
	}

	private void useBlessOfEva(L1PcInstance pc, int item_id) {
			if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) { // 衰退藥水狀態
				pc.sendPackets(String.valueOf(new S_ServerMessage(698))); // 1由於魔力無法飲用任何物品
														// 無法飲用。
				return;
			}

			// 絕對屏障解除
			cancelAbsoluteBarrier(pc);

			int time = 0;
			switch (item_id) {
				case 210115:// 濃縮呼吸藥水
					time = 7200;
					break;
				case 40032:// 伊娃的祝福
				//time = 1800;// 週六特化

				time = 3600;
				break;
			case 40041:
				time = 300;
				break;
			case 41344:
				time = 2100;
				break;
			default:
				return;
		}
		if (pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) {
			int timeSec = pc.getSkillEffectTimeSec(STATUS_UNDERWATER_BREATH);
			time += timeSec;
			if (time > 3600) {
				time = 3600;
			}
		}
		pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), time).toString());
		pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 190)));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
		pc.setSkillEffect(STATUS_UNDERWATER_BREATH, time * 1000);
	}

	private void useBlindPotion(L1PcInstance pc) {
		if (pc.hasSkillEffect(DECAY_POTION)) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(698))); // 1由於魔力無法飲用任何物品
			// 無法飲用。
			return;
		}

		// 絕對屏障解除
		cancelAbsoluteBarrier(pc);

		int time = 480;
		if (pc.hasSkillEffect(CURSE_BLIND)) {
			pc.killSkillEffectTimer(CURSE_BLIND);
		} else if (pc.hasSkillEffect(DARKNESS)) {
			pc.killSkillEffectTimer(DARKNESS);
		} else if (pc.hasSkillEffect(LINDBIOR_SPIRIT_EFFECT)) {
			pc.killSkillEffectTimer(LINDBIOR_SPIRIT_EFFECT);
		}

		if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
			pc.sendPackets(String.valueOf(new S_CurseBlind(2)));
		} else {
			pc.sendPackets(String.valueOf(new S_CurseBlind(1)));
		}

		pc.setSkillEffect(CURSE_BLIND, time * 1000);
	}

	private void useCashScroll(L1PcInstance pc, int item_id) {
		int time = 1800;
		int scroll = 0;

		if (pc.hasSkillEffect(STATUS_CASHSCROLL)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL);
			pc.addMaxHp(-50);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				// TODO 派對原型
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		}
		if (pc.hasSkillEffect(STATUS_CASHSCROLL2)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL2);
			pc.addMaxMp(-40);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		}
		if (pc.hasSkillEffect(STATUS_CASHSCROLL3)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL3);
			pc.addDmgup(-3);
			pc.addHitup(-3);
			pc.getAbility().addSp(-3);
		}
		if (pc.hasSkillEffect(STATUS_CASHSCROLL4)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL4);
			pc.getAbility().addSp(-3);
			pc.addBaseMagicHitUp(-5);
			pc.getResistance().addcalcPcDefense(-3);
		}
		if (pc.hasSkillEffect(STATUS_CASHSCROLL5)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL5);
			pc.addBowDmgup(-3);
			pc.addBowHitup(-5);
			pc.getResistance().addcalcPcDefense(-3);
		}
		if (pc.hasSkillEffect(STATUS_CASHSCROLL6)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL6);
			pc.addDmgRate(-3);
			pc.addHitup(-5);
			pc.getResistance().addcalcPcDefense(-3);
		}

		if (item_id == 410010) {
			scroll = 7893;
			pc.addMaxHp(50);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				// TODO 派對原型
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		} else if (item_id == 410011) {
			scroll = 7894;
			pc.addMaxMp(40);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		} else if (item_id == 410012 || item_id == 30063) {
			scroll = 7895;
			pc.addDmgup(3);
			pc.addHitup(3);
			pc.getAbility().addSp(3);
		} else if (item_id == 4100039) {
			scroll = 16553;
			pc.getAbility().addSp(3);
			pc.addBaseMagicHitUp(5);
			pc.getResistance().addcalcPcDefense(3);
		} else if (item_id == 4100041) {
			scroll = 16552;
			pc.addBowDmgup(3);
			pc.addBowHitup(5);
			pc.getResistance().addcalcPcDefense(3);
		} else if (item_id == 4100042) {
			scroll = 16551;
			pc.addDmgRate(3);
			pc.addHitup(5);
			pc.getResistance().addcalcPcDefense(3);
		} else if (item_id == 4100465) {
			scroll = 16551;
			pc.addDmgRate(3);
			pc.addHitup(5);
			pc.getResistance().addcalcPcDefense(3);
		}
		pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), scroll)));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), scroll));
		pc.setSkillEffect(scroll, time * 1000);
	}

	private void Deciding_Buff(L1PcInstance pc) {
		int time = 300;
		if (pc.hasSkillEffect(L1SkillId.DECIDING_BUFF)) {
			pc.killSkillEffectTimer(L1SkillId.DECIDING_BUFF);
			pc.getAC().addAc(5);
			pc.addBowHitup(-5);
			pc.addHitup(-5);
			pc.addBaseMagicHitUp(-2);
			pc.getResistance().addcalcPcDefense(-5);
			pc.getResistance().addPVPweaponTotalDamage(-5);
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharStatus(pc));
			L1SkillUse.off_icons(pc, L1SkillId.DECIDING_BUFF);
		}

		pc.getAC().addAc(-5);
		pc.addBowHitup(5);
		pc.addHitup(5);
		pc.addBaseMagicHitUp(2);
		pc.getResistance().addcalcPcDefense(5);
		pc.getResistance().addPVPweaponTotalDamage(5);
		pc.sendPackets(new S_SPMR(pc));
		pc.sendPackets(new S_OwnCharStatus(pc));

		pc.setSkillEffect(L1SkillId.DECIDING_BUFF, time * 1000);
		pc.send_effect(12431);
		L1SkillUse.on_icons(pc, L1SkillId.DECIDING_BUFF, time);
	}

	private void HEROGAHOBUFF(L1PcInstance pc) {
		int time = 3600;
		if (pc.hasSkillEffect(L1SkillId.HERO_GAHO_BUFF)) {
			pc.killSkillEffectTimer(L1SkillId.HERO_GAHO_BUFF);
			pc.getAbility().addAddedStr(-1);
			pc.getAbility().addAddedDex(-1);
			pc.getAbility().addAddedInt(-1);
			pc.addBowHitup(-3);
			pc.addHitup(-3);
			pc.addBaseMagicHitUp(-3);
			pc.addSpecialPierce(eKind.ALL, -3);
			pc.getResistance().addPVPweaponTotalDamage(-3);
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharStatus(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
		}

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
		L1SkillUse.off_icons(pc, L1SkillId.HERO_GAHO_BUFF);

		pc.setSkillEffect(L1SkillId.HERO_GAHO_BUFF, time * 1000);
		pc.send_effect(12431);
		L1SkillUse.on_icons(pc, L1SkillId.HERO_GAHO_BUFF, time);
	}

	private void useWeekBox(L1PcInstance pc, L1ItemInstance l1iteminstance) {
		long timeItem = System.currentTimeMillis();
		int useTimeItem = 7 * 24 * 60 * 60;
		if (!pc.isGm()) {
			if (pc.hasSkillEffect(L1SkillId.WEEK_BOX)) {
				int sec = pc.getSkillEffectTimeSec(L1SkillId.WEEK_BOX);
				int min = 0;
				int hour = 0;
				int day = 0;
				if (sec > 60) {
					min = sec / 60;
				}
				if (min > 60) {
					hour = min / 60;
				}
				if (hour > 24) {
					day = hour / 24;
				}
				int leftday = day;
				int lefthour = hour - (day * 24);
				int leftmin = min - (hour * 60);
				String ment = "";
				if (leftday > 0) {
					ment = leftday + "天" + lefthour + "小時" + leftmin + "分鐘後可以使用。";
				} else if (leftday == 0) {
					if (lefthour > 0) {
						ment = lefthour + "小時" + leftmin + "分鐘後可以使用。";
					} else if (lefthour == 0) {
						ment = leftmin + "分鐘後可以使用。";
					} else if (leftday == 0) {
						if (lefthour > 0) {
							ment = lefthour + "小時" + leftmin + "分鐘後可以使用。";
						} else if (lefthour == 0) {
							ment = leftmin + "分鐘後可以使用。";
					}
				}
				pc.sendPackets(ment);
				return;
			}
		}

		int itemid = l1iteminstance.getItemId();
		int need_item_id = Config.WeekBox.REWARD_NEED_ITEM;
		int[] need_item_count = Config.WeekBox.REWARD_COUNT;
		int[] reward_itemid = Config.WeekBox.REWARD_ITEMID;
		int[] reward_item_count = Config.WeekBox.REWARD_ITEM_COUNT;
		ArrayList<Integer> Need_Item_Count = new ArrayList<Integer>();
		for (int i = 0; i < need_item_count.length; i++) {
			Need_Item_Count.add(need_item_count[i]);
		}

		if (Need_Item_Count != null) {
			Collections.sort(Need_Item_Count);
		}

		if (itemid != Config.WeekBox.WEEK_BOX_ID) {
			System.out.println("config/Week_Box 中未註冊的獎勵箱。");
			return;
		}

		for (int i = Need_Item_Count.size() - 1; i >= 0; i--) {
			if (pc.getInventory().checkItem(need_item_id, Need_Item_Count.get(i))) {
				pc.getInventory().consumeItem(need_item_id, Need_Item_Count.get(i));
				L1ItemInstance item = pc.getInventory().storeItem(reward_itemid[i], reward_item_count[i]);
				pc.sendPackets("週獎勵：" + item.getName() + "（" + reward_item_count[i] + "個）已發放。");
				pc.setSkillEffect(L1SkillId.WEEK_BOX, useTimeItem * 1000);
				break;
			} else {
				if (i == 0) {
					L1Item template = ItemTable.getInstance().getTemplate(need_item_id);

					if (template != null) {
						pc.sendPackets(template.getName() + "的數量不足。");
					}

//                    System.out.println("檢查封包1");
				}
				continue;
			}
		}

	}

			/**
			 * 移除精鍊石
			 *
			 * @param pc 玩家角色
			 * @param l1iteminstance 物品實例
			 * @param l1iteminstance1 物品實例1
			 */
	private void useDollPotential(L1PcInstance pc, L1ItemInstance l1iteminstance, L1ItemInstance l1iteminstance1) {
		int itemid = l1iteminstance.getItemId();
		L1MagicDoll magicDollItem = L1MagicDoll.get(l1iteminstance1.getItemId());
		if (magicDollItem == null) {
			pc.sendPackets("該物品無法使用。");
			return;
		}
				if (magicDollItem.getInfo().getGrade() < 4) {
					pc.sendPackets("選定的人偶無法使用。");
					return;
				}
				if (magicDollItem.getInfo().getGrade() == 4) {
					if (itemid >= 30001464 && itemid <= 30001468) {
						pc.sendPackets("選定的人偶無法使用。");
				return;
			}
		}

		Info info = magicDollItem.getInfo();
		Effect effect = magicDollItem.getEffect();
		if (info != null && effect != null) {
			L1DollInstance doll = pc.getMagicDoll();
			if (doll != null && doll.getItemObjId() == l1iteminstance1.getId()) {
				SC_SUMMON_PET_NOTI.off_summoned(pc);
				doll.deleteDoll();
			}
		}

		switch (itemid) {
			case 30001463: // HP吸收/經驗值10（英雄）
				l1iteminstance1.set_Doll_Bonus_Level(3);
				l1iteminstance1.set_Doll_Bonus_Value(149);
				break;
			case 30001464: // 四段加速/經驗值15（傳說）
				l1iteminstance1.set_Doll_Bonus_Level(4);
				l1iteminstance1.set_Doll_Bonus_Value(146);
				break;
			case 30001465: // 免疫效果減少 -30（傳說）
				l1iteminstance1.set_Doll_Bonus_Level(4);
				l1iteminstance1.set_Doll_Bonus_Value(133);
				break;
			case 30001466: // 四段加速（傳說）
				l1iteminstance1.set_Doll_Bonus_Level(4);
				l1iteminstance1.set_Doll_Bonus_Value(142);
				break;
			case 30001467: // PvP傷害減少無視40/抗性2（傳說）
				l1iteminstance1.set_Doll_Bonus_Level(4);
				l1iteminstance1.set_Doll_Bonus_Value(144);
				break;
			case 30001468: // 免疫效果減少/抗性2（傳說）
				l1iteminstance1.set_Doll_Bonus_Level(4);
				l1iteminstance1.set_Doll_Bonus_Value(145);
				break;
			default:
				break;
		}
		pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_DOLL_LEVEL);
		pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_DOLL_VALUE);
		pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_DOLL_LEVEL);
		pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_DOLL_VALUE);
		pc.getInventory().consumeItem(l1iteminstance, 1);

	}

	private void usePcBuff(L1PcInstance pc, L1ItemInstance l1iteminstance) {
		int itemId = l1iteminstance.getItemId();
		int day = 0;

		switch (itemId) {
			case 600223:
				day = 7;
				break;
			case 3000237:
				day = 3;
				break;
			case 600225:
				day = 30;
				break;
			case 3000201:
				day = 1;
				break;
		}
		CharacterFreeShieldTable.getInstance().getPCBuff(pc);

		if (pc.isPcBuff()) {
			pc.sendPackets("已經應用了龍之祝福：網咖增益效果。");
			return;
		} else {
			PcBangCoin(pc, itemId, l1iteminstance, day);
			pc.sendPackets("網咖使用時間: " + day + "天內享受網咖福利。");
		}

	}

	private void useMaan(L1PcInstance pc, L1ItemInstance l1iteminstance) {
		int itemId = l1iteminstance.getItemId();
		if (l1iteminstance.getLastUsed() == null) {
			Timestamp ts1 = new Timestamp(System.currentTimeMillis());
			l1iteminstance.setLastUsed(ts1);
		}
		Timestamp lastUsed = l1iteminstance.getLastUsed();
		Calendar cal = Calendar.getInstance();
		if ((cal.getTimeInMillis() - lastUsed.getTime()) / 1000 >= 0) {
			if (itemId == 410032) { // 水龍
				L1SkillUse l1Skilluse = new L1SkillUse();
				l1Skilluse.handleCommands(pc, FAFU_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				l1iteminstance.setLastUsed(ts);
			}
			if (itemId == 410033) { // 風龍
				L1SkillUse l1Skilluse = new L1SkillUse();
				l1Skilluse.handleCommands(pc, LIND_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				l1iteminstance.setLastUsed(ts);
			}
			if (itemId == 410034) { // 地龍
				L1SkillUse l1Skilluse = new L1SkillUse();
				l1Skilluse.handleCommands(pc, ANTA_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				l1iteminstance.setLastUsed(ts);
			}
			if (itemId == 410035) { // 火龍
				L1SkillUse l1Skilluse = new L1SkillUse();
				l1Skilluse.handleCommands(pc, VALA_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				l1iteminstance.setLastUsed(ts);
			}
			if (itemId == 410036) { // 誕生
				L1SkillUse l1Skilluse = new L1SkillUse();
				l1Skilluse.handleCommands(pc, BIRTH_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				l1iteminstance.setLastUsed(ts);
			}
			if (itemId == 410037) { // 形狀
				L1SkillUse l1Skilluse = new L1SkillUse();
				l1Skilluse.handleCommands(pc, SHAPE_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				l1iteminstance.setLastUsed(ts);
			}
			if (itemId == 410038) { // 生命
				L1SkillUse l1Skilluse = new L1SkillUse();
				l1Skilluse.handleCommands(pc, LIFE_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				l1iteminstance.setLastUsed(ts);
			}
			/*
			 * if (itemId == 3000159) { // 生命 一次性
			 * L1SkillUse l1Skilluse = new L1SkillUse();
			 * l1Skilluse.handleCommands(pc, LIFE_MAAN, pc.getId(), pc.getX(), pc.getY(),
			 * null, 0, L1SkillUse.TYPE_GMBUFF);
			 * Timestamp ts = new Timestamp(System.currentTimeMillis());
			 * l1iteminstance.setLastUsed(ts);
			 * pc.getInventory().removeItem(l1iteminstance, 1);
			 * }
			 */
			if (itemId == 410176) { // 黑龍
				L1SkillUse l1Skilluse = new L1SkillUse();
				l1Skilluse.handleCommands(pc, BLACK_DRAGON_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				l1iteminstance.setLastUsed(ts);
			}
			if (itemId == 410177) { // 絕對的黑龍
				L1SkillUse l1Skilluse = new L1SkillUse();
				l1Skilluse.handleCommands(pc, NAVER_BLACK_DRAGON_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				l1iteminstance.setLastUsed(ts);
			}
		}
	}

	private void useDragonTopaz(L1PcInstance pc, L1ItemInstance l1iteminstance) {
		int itemId = l1iteminstance.getItemId();
		if (itemId == L1ItemId.DRAGON_SAPPHIRE) {
			if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(2146)));
				return;
			}
			use_einhasad_potion(pc, l1iteminstance, SC_REST_EXP_INFO_NOTI.RE_DRAGON_SAPPHIRE_NORMAL);
		} else if (itemId == L1ItemId.DRAGON_RUBY || itemId == 6017) {
			if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(2146)));
				return;
			}
			use_einhasad_potion(pc, l1iteminstance, SC_REST_EXP_INFO_NOTI.RE_DRAGON_RUBY_NORMAL);
		} else if (itemId == L1ItemId.EMERALD || itemId == L1ItemId.EMERALD1) {
			if (pc.hasSkillEffect(L1SkillId.EMERALD_NO) == true) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(2145)));
				return;
			} else if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(2147)));
				return;
			} else if (pc.hasSkillEffect(L1SkillId.DRAGON_PUPLE) || pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(2147)));
				return;
			}
			pc.getAccount().addBlessOfAin(1000000, pc);
			pc.setSkillEffect(L1SkillId.EMERALD_YES, 1800 * 1000);
			pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, 0x02, 1800));
			pc.sendPackets(String.valueOf(new S_ServerMessage(2140)));
			pc.getInventory().removeItem(l1iteminstance, 1);
		} else if (itemId == 60255) {
			if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(2146)));
				return;
			}
			if (pc.getAccount().getBlessOfAin() <= 10000) {
				pc.sendPackets("艾恩哈薩德的祝福: 必須有一些數值才能使用。");
				return;
			}
			if (pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
				pc.removeSkillEffect(L1SkillId.DRAGON_TOPAZ);
			}

			pc.setSkillEffect(DRAGON_PUPLE, 1800 * 1000);
			pc.sendPackets(new S_PacketBox(1800, 1, true, true));
			pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 197)));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 197));
			pc.getInventory().removeItem(l1iteminstance, 1);
		} else if (itemId == 7241 || itemId == 3000457) {
			if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(2146)));
				return;
			}
			if (pc.getAccount().getBlessOfAin() <= 10000) {
				pc.sendPackets("艾恩哈薩德的祝福: 數值必須有一點才能使用。");
				return;
			}
			// if (pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
			// pc.sendPackets("龍的黃玉效果還在，無法再次使用。");
			// return;
			// }
			if (pc.hasSkillEffect(L1SkillId.DRAGON_PUPLE)) {
				pc.removeSkillEffect(L1SkillId.DRAGON_PUPLE);
			}
			// pc.setSkillEffect(DRAGON_TOPAZ, 1800 * 1000);
			int time = 0;
			int time2 = 0;
			if (pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
				time = pc.getSkilleffect(DRAGON_TOPAZ).remainingSeconds();
			}

			time2 = time + 1800;
			// System.out.println(time);

			if (time2 / 3600 >= 12) {
				pc.sendPackets("龍的黃玉效果最多可以疊加到12小時。");
				return;
			}

			pc.setSkillEffect(DRAGON_TOPAZ, time2 * 1000);

			SC_REST_EXP_INFO_NOTI.send(pc);
			pc.sendPackets(new S_PacketBox(time2, 2, true, true));
			pc.sendPackets(new S_SkillSound(pc.getId(), 197));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 197));
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
	}

	private void useItemSealUnlockScroll(L1PcInstance pc, L1ItemInstance l1iteminstance,
										 L1ItemInstance l1iteminstance1) {
		if (l1iteminstance1.getSmeltingValue() != 0) {
			pc.sendPackets("由於鑲嵌了精煉石，無法解封。");
			return;
		}
				if (l1iteminstance1.get_Cantunseal() == 1) {
					pc.sendPackets("該物品無法解封。");
			return;
		}

		if (l1iteminstance1.getBless() == 128 || l1iteminstance1.getBless() == 129 || l1iteminstance1.getBless() == 130
				|| l1iteminstance1.getBless() == 131) {
			int Bless = 0;
			switch (l1iteminstance1.getBless()) {
				case 128:
					Bless = 0;
					break; // 祝福
				case 129:
					Bless = 1;
					break; // 普通
				case 130:
					Bless = 2;
					break; // 詛咒
				case 131:
					Bless = 3;
					break; // 未確認
			}
			l1iteminstance1.setBless(Bless);
			int st = 0;
			if (l1iteminstance1.isIdentified())
				st += 1;
			if (!l1iteminstance1.getItem().isTradable())
				st += 2;
			if (l1iteminstance1.getItem().isCantDelete())
				st += 4;
			if (l1iteminstance1.getItem().get_safeenchant() < 0)
				st += 8;
			if (l1iteminstance1.getBless() >= 128) {
				st = 32;
				if (l1iteminstance1.isIdentified()) {
					st += 15;
				} else {
					st += 14;
				}
			}
			pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, l1iteminstance1, st));
			pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
			pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
			pc.getInventory().removeItem(l1iteminstance, 1);
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
		}
	}

	private void useItemSealScroll(L1PcInstance pc, L1ItemInstance l1iteminstance, L1ItemInstance l1iteminstance1) {
		if (l1iteminstance1.getBless() == 0 || l1iteminstance1.getBless() == 1 || l1iteminstance1.getBless() == 2
				|| l1iteminstance1.getBless() == 3) {
			int Bless = 0;
			switch (l1iteminstance1.getBless()) {
				case 0:
					Bless = 128;
					break; // 祝福
				case 1:
					Bless = 129;
					break; // 普通
				case 2:
					Bless = 130;
					break; // 詛咒
				case 3:
					Bless = 131;
					break; // 未確認
			}
			l1iteminstance1.setBless(Bless);
			int st = 0;
			if (l1iteminstance1.isIdentified())
				st += 1;
			if (!l1iteminstance1.getItem().isTradable())
				st += 2;
			if (l1iteminstance1.getItem().isCantDelete())
				st += 4;
			if (l1iteminstance1.getItem().get_safeenchant() < 0)
				st += 8;
			if (l1iteminstance1.getBless() >= 128) {
				st = 32;
				if (l1iteminstance1.isIdentified()) {
					st += 15;
				} else {
					st += 14;
				}
			}
			pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, l1iteminstance1, st));
			pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
			pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
			pc.getInventory().removeItem(l1iteminstance, 1);
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
		}
	}

	private void useSmeltingRemoveScroll2(L1PcInstance pc, L1ItemInstance l1iteminstance,
										  L1ItemInstance l1iteminstance1) {
		SC_SMELTING_UPDATE_SLOT_INFO_NOTI.eject(pc, l1iteminstance1, 0, SmeltingResult.SMELTING_EJECT_START);
	}

	private void useSmeltingRemoveScroll(L1PcInstance pc, L1ItemInstance l1iteminstance,
										 L1ItemInstance l1iteminstance1) {
		int smeltingitem1 = l1iteminstance1.getSmeltingItemId1();
		int smeltingitem2 = l1iteminstance1.getSmeltingItemId2();
		int st = 0;
		int Bless = 0;
		if (!Config.SmeltingSetting.SmeltingUse) {
			pc.sendPackets("精煉系統尚未可使用。");
			return;
		}
				if (l1iteminstance1.getSmeltingValue() == 0) {
					pc.sendPackets("未鑲嵌精煉石。");
			return;
		}
		if (l1iteminstance1.isEquipped()) {
			pc.getInventory().setEquipped(l1iteminstance1, false);
		}
		if (l1iteminstance1.getSmeltingValue() >= 1) {
			if (smeltingitem1 != 0) {
				pc.getInventory().storeItem(smeltingitem1, 1, true);
				SC_SMELTING_UPDATE_SLOT_INFO_NOTI.eject(pc, l1iteminstance1, 0, SmeltingResult.SMELTING_EJECT_SUCCESS);
			}
			if (smeltingitem2 != 0) {
				pc.getInventory().storeItem(smeltingitem2, 1, true);
				SC_SMELTING_UPDATE_SLOT_INFO_NOTI.eject(pc, l1iteminstance1, 1, SmeltingResult.SMELTING_EJECT_SUCCESS);
			}
			if (l1iteminstance1.getBless() == 128 || l1iteminstance1.getBless() == 129
					|| l1iteminstance1.getBless() == 130 || l1iteminstance1.getBless() == 131) {

				switch (l1iteminstance1.getBless()) {
					case 128:
						Bless = 0;
						break; // 祝福
					case 129:
						Bless = 1;
						break; // 普通
					case 130:
						Bless = 2;
						break; // 詛咒
					case 131:
						Bless = 3;
						break; // 未確認
				}
				l1iteminstance1.setBless(Bless);
				if (l1iteminstance1.isIdentified())
					st += 1;
				if (!l1iteminstance1.getItem().isTradable())
					st += 2;
				if (l1iteminstance1.getItem().isCantDelete())
					st += 4;
				if (l1iteminstance1.getItem().get_safeenchant() < 0)
					st += 8;
				if (l1iteminstance1.getBless() >= 128) {
					st = 32;
					if (l1iteminstance1.isIdentified()) {
						st += 15;
					} else {
						st += 14;
					}
				}
				pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, l1iteminstance1, st));
			}
			l1iteminstance1.setSmeltingValue(0);
			l1iteminstance1.setSmeltingItemId1(0);
			l1iteminstance1.setSmeltingKind1(0);
			l1iteminstance1.setSmeltingItemId2(0);
			l1iteminstance1.setSmeltingKind2(0);
			pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_SMELTING);
			pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_SMELTING);
			pc.getInventory().consumeItem(l1iteminstance, 1);
			pc.sendPackets("精煉石提取已完成。");

		} else {
			System.out.println("<精煉石數量異常> 帳號:" + pc.getAccount() + " 物品ID: " + l1iteminstance1.getItemId());
			pc.sendPackets("");
			return;
		}
	}

			/**
			 * 精煉石鑲嵌
			 *
			 * @param pc
			 * @param l1iteminstance
			 * @param l1iteminstance1
			 */
			private void useSmeltingScroll(L1PcInstance pc, L1ItemInstance l1iteminstance, L1ItemInstance l1iteminstance1) {
				SmeltingScrollInfo info = SmeltingScrollLoader.getInstance().getSmeltingScrollInfo(l1iteminstance.getItemId());
				int type = info.getType();
				int st = 0;
				int Bless = 0;
				if (!Config.SmeltingSetting.SmeltingUse) {
					pc.sendPackets("精煉系統尚未可使用。");
					return;
				}
				/**
				 * @return 0 如果是 L1EtcItem, 1 如果是 L1Weapon, 2 如果是 L1Armor
				 */
				if (l1iteminstance1.getItem().getType2() == 1) {
					if (!Config.SmeltingSetting.weapon) {
						pc.sendPackets("僅適用於裝甲。");
						return;
					}

		} else if (l1iteminstance1.getItem().getType2() == 2) {
					/**
					 * 返回物品的種類。<br>
					 *
					 * <p>
					 * [裝甲]<br>
					 * 1:頭盔, 2:鎧甲, 3:襯衫, 4:披風, 5:手套, 6:靴子, 7:盾牌, 8:護身符, 9:戒指,
					 * 10:腰帶, 11:第二個戒指, 12:耳環, 31:吊墜
					 */
					if (l1iteminstance1.getItem().getType() == 1) { // 頭盔
						if (!Config.SmeltingSetting.helmet) {
							pc.sendPackets("僅適用於裝甲。");
							return;
						}
					} else if (l1iteminstance1.getItem().getType() == 5) { // 手套
						if (!Config.SmeltingSetting.glove) {
							pc.sendPackets("僅適用於裝甲。");
							return;
						}
					} else if (l1iteminstance1.getItem().getType() == 3) { // 襯衫
						if (!Config.SmeltingSetting.T) {
							pc.sendPackets("僅適用於裝甲。");
							return;
						}
					} else if (l1iteminstance1.getItem().getType() != 2) {
						pc.sendPackets("僅適用於裝甲。");
						return;
					}
				} else {
					pc.sendPackets("僅適用於裝甲。");
					return;
				}

				if (l1iteminstance1.getEndTime() != null) {
					pc.sendPackets("無法使用於限時物品。");
			return;
		}
		if (l1iteminstance1.isEquipped()) {
			pc.getInventory().setEquipped(l1iteminstance1, false);
		}
		if (l1iteminstance1.getSmeltingValue() == 0) {
			l1iteminstance1.setSmeltingItemId1(l1iteminstance.getItemId());
			l1iteminstance1.setSmeltingKind1(type);
			l1iteminstance1.setSmeltingValue(1);
//			l1iteminstance1.getItem().setSmeltingSlotValue(1);
			pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_SMELTING);
			pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_SMELTING);
			pc.getInventory().consumeItem(l1iteminstance, 1);
			SC_SMELTING_UPDATE_SLOT_INFO_NOTI.send(pc, l1iteminstance, l1iteminstance1, 0,
					SmeltingResult.SMELTING_INSERT_SUCCESS);
		} else if (l1iteminstance1.getSmeltingValue() == 1) {
			if (l1iteminstance1.getSmeltingItemId1() == 0) {
				if (l1iteminstance1.getSmeltingKind2() != type) {
					l1iteminstance1.setSmeltingItemId1(l1iteminstance.getItemId());
					l1iteminstance1.setSmeltingKind1(type);
					l1iteminstance1.setSmeltingValue(2);
					pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_SMELTING);
					pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_SMELTING);
					pc.getInventory().consumeItem(l1iteminstance, 1);
					SC_SMELTING_UPDATE_SLOT_INFO_NOTI.send(pc, l1iteminstance, l1iteminstance1, 0,
							SmeltingResult.SMELTING_INSERT_SUCCESS);
				} else {
					pc.sendPackets("同一種類的精煉石只能應用一個。");
					return;
				}
			} else if (l1iteminstance1.getSmeltingItemId2() == 0) {
				if (l1iteminstance1.getSmeltingKind1() != type) {
					l1iteminstance1.setSmeltingItemId2(l1iteminstance.getItemId());
					l1iteminstance1.setSmeltingKind2(type);
					l1iteminstance1.setSmeltingValue(2);
					pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_SMELTING);
					pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_SMELTING);
					pc.getInventory().consumeItem(l1iteminstance, 1);
					SC_SMELTING_UPDATE_SLOT_INFO_NOTI.send(pc, l1iteminstance, l1iteminstance1, 1,
							SmeltingResult.SMELTING_INSERT_SUCCESS);
				} else {
					pc.sendPackets("同一種類的精煉石只能應用一個。");
					return;
				}
			}

		} else if (l1iteminstance1.getSmeltingValue() >= 2) {
			pc.sendPackets("無法應用兩個以上的精煉石。");
			return;
		}
		if (l1iteminstance1.getBless() == 0 || l1iteminstance1.getBless() == 1 || l1iteminstance1.getBless() == 2
				|| l1iteminstance1.getBless() == 3) {
			switch (l1iteminstance1.getBless()) {
				case 0:
					Bless = 128;
					break; // 祝福
				case 1:
					Bless = 129;
					break; // 普通
				case 2:
					Bless = 130;
					break; // 詛咒
				case 3:
					Bless = 131;
					break; // 未確認
			}
			l1iteminstance1.setBless(Bless);
			if (l1iteminstance1.isIdentified())
				st += 1;
			if (!l1iteminstance1.getItem().isTradable())
				st += 2;
			if (l1iteminstance1.getItem().isCantDelete())
				st += 4;
			if (l1iteminstance1.getItem().get_safeenchant() < 0)
				st += 8;
			if (l1iteminstance1.getBless() >= 128) {
				st = 32;
				if (l1iteminstance1.isIdentified()) {
					st += 15;
				} else {
					st += 14;
				}
			}
			pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, l1iteminstance1, st));
		}

		pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_SMELTING);
		pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_SMELTING);
		pc.sendPackets(new S_SPMR(pc));
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	private void usePolyScale2(L1PcInstance pc, int itemId) {
		int polyId = 0;
				if (itemId == 220001) { // 賽變身
					polyId = 11232;
				} else if (itemId == 220002) { // 賽變身
					polyId = 11234;
				} else if (itemId == 220003) { // 賽變身
					polyId = 11236;
				} else if (itemId == 3000600) { // 黑暗騎士變身卷軸
					polyId = 19624;
				} else if (itemId == 3000601) { // 黑暗魔導士變身卷軸
					polyId = 19666;
				} else if (itemId == 3000602) { // 黑暗槍術師變身卷軸
					polyId = 19326;
				} else if (itemId == 3000603) { // 黑暗遊俠變身卷軸
					polyId = 19586;
				} else if (itemId == 3000604) { // 銀騎士變身卷軸
					polyId = 19621;
				} else if (itemId == 3000605) { // 銀魔導士變身卷軸
					polyId = 19669;
				} else if (itemId == 3000606) { // 銀槍術師變身卷軸
					polyId = 19387;
				} else if (itemId == 3000607) { // 銀遊俠變身卷軸
					polyId = 19592;
				} else if (itemId == 3000608) { // 金騎士變身卷軸
					polyId = 19618;
				} else if (itemId == 3000609) { // 金魔導士變身卷軸
					polyId = 19671;
				} else if (itemId == 3000610) { // 金槍術師變身卷軸
					polyId = 19618;
				} else if (itemId == 3000611) { // 金遊俠變身卷軸
					polyId = 19589;
				} else if (itemId == 3000612) { // 大地騎士變身卷軸
					polyId = 19471;
				} else if (itemId == 3000613) { // 大地魔導士變身卷軸
					polyId = 19673;
				} else if (itemId == 3000614) { // 大地槍術師變身卷軸
					polyId = 19391;
				} else if (itemId == 3000615) { // 大地遊俠變身卷軸
					polyId = 19494;
				} else if (itemId == 3000616) { // 死亡騎士(98等級)
					if (itemId == 3000616) { // 死亡騎士(98等級)
						polyId = 19689;
					} else if (itemId == 3000617) { // 巴風特(98等級)
						polyId = 19738;
					} else if (itemId == 3000618) { // 弓箭警衛(98等級)
						polyId = 19726;
					} else if (itemId == 3000619) { // 槍警衛(98等級)
						polyId = 19722;
					}
		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC, false, false);
	}

	public static boolean usePolyScroll(L1PcInstance pc, int item_id, String s) {
//        System.out.println(s); // 找變身動作時

		int time = 0;
		boolean ring = false;
		boolean ring2 = false;
		switch (item_id) {
			case 4100500:
			case 4100610:
				time = 3600;
				break;
			case 40088:
			case 40096:
				//time = 1800; // 週六特化
				time = 3600;
				break;
			case 140088:
				time = 2100;
				break;
			case 210112:
				time = 3600;
				break;
			case 40008:
				time = 7200;
				break;
			case 140008:
				time = 7200;
				break;
			default:
				return false;
		}
		boolean maple = false;
		if (s.startsWith("maple")) {
			maple = true;
			if (!pc.isPolyRingMaster() && !pc.isPolyRingMaster2())
				return false;
		}
		if (s.startsWith("maple")) {
			String aa = s;
			String bb = aa.replace("maple ", "");
			s = bb;
		}
		if (s.endsWith(" 100")) {
			String aa = s;
			String bb = aa.replace(" 100", "");
			s = bb;
		}
		if (s.equalsIgnoreCase("ranking class polymorph")) {
			s = "rangking " + L1PolyMorph.getReplacePolyName(pc.getType()) + (pc.get_sex() == 0 ? " male" : " female");
		}

		if (s.startsWith("rangking")) {
			if (!MJRankUserLoader.getInstance().isRankPoly(pc)) {
				return false;
			}
		}
//		System.out.println(s);
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);

					// TODO 變身時輸出圖像
//		System.out.println(poly.getPolyId());
//		System.out.println(poly + s);
		if (poly != null || s.equals("")) {
			if (s.equals("")) {
				int spriteId = pc.getCurrentSpriteId();
				if (spriteId == 6034 || spriteId == 6035) {
					return true;
				} else {
					if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER)) {
						pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER);
					} else if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2)) {
						pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER2);
					} else {
						pc.removeSkillEffect(SHAPE_CHANGE);
					}
					return true;
				}
			}else if (Config.ServerAdSetting.PolyEvent2) {
				int minlevel = 0;
				int downlevel = Config.ServerAdSetting.PolyEvent2_level_down;
				minlevel = poly.getMinLevel() - downlevel;

				if (minlevel <= pc.getLevel()) {
					if ((item_id == 40008 || item_id == 140008 || item_id == 40410)) {
						if (pc.isPolyRingMaster2()) {
							ring = false;
							ring2 = true;
						} else if (pc.isPolyRingMaster()) {
							ring = true;
							ring2 = false;
						}
					}
					if (ring && ring2) {
						ring = false;
						ring2 = true;
					}
//					System.out.println(ring + " + "+ring2);
					L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC, ring, ring2);
					return true;
				}
			} /*else if (Config.ServerAdSetting.PolyEvent2) {
				int minlevel = 0;
				switch (poly.getPolyId()) {
				case 17541:
				case 17531:
				case 17545:
				case 17515:
				case 17535:
				case 17549:
				case 16014:
				case 16284:
				case 15986:
				case 16053:
				case 16008:
				case 16056:
				case 16002:
				case 16074:
				case 16027:
				case 16040:
					minlevel = 80;
					break;
				case 13152:
				case 13153:
				case 12681:
				case 15868:
				case 11389:
				case 15866:
				case 15539:
				case 15537:
				case 15534:
				case 13635:
				case 13631:
				case 15814:
				case 15550:
				case 15548:
				case 15545:
				case 15831:
				case 15833:
				case 15830:
				case 15832:
				case 15528:
				case 15531:
					minlevel = 65;
					break;
				case 12702:
				case 15850:
				case 11385:
				case 15847:
				case 12240:
				case 15599:
				case 13346:
				case 15848:
				case 15865:
				case 15849:
					minlevel = 60;
					break;
				default:
					minlevel = poly.getMinLevel();
					break;
				}
				if (minlevel <= pc.getLevel()) {
					if ((item_id == 40008 || item_id == 140008 || item_id == 40410)) {
						if (pc.isPolyRingMaster2()) {
							ring = false;
							ring2 = true;
						} else if (pc.isPolyRingMaster()) {
							ring = true;
							ring2 = false;
						}
					}
					if (ring && ring2) {
						ring = false;
						ring2 = true;
					}
//					System.out.println(ring + " + "+ring2);
					L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC, ring, ring2);
					return true;
				}
			}*/ else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
				if (pc.isPolyRingMaster2() && maple) {
					ring = false;
					ring2 = true;
				} else if (pc.isPolyRingMaster() && maple) {
					ring = true;
					ring2 = false;
				}
				if (ring && ring2) {
					ring = false;
					ring2 = true;
				}
//				System.out.println(ring + " + "+ring2);
//				System.out.println("시간: " +time);
				L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC, ring, ring2);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private void usePolyScale(L1PcInstance pc, int itemId) {
		int polyId = 0;
					if (itemId == 41154) { // 暗影之鱗
						polyId = 3101;
					} else if (itemId == 41155) { // 烈火之鱗
						polyId = 3126;
					} else if (itemId == 41156) { // 背德者之鱗
						polyId = 3888;
					} else if (itemId == 41157) { // 憎恨之鱗
			polyId = 3784;
		}
		L1PolyMorph.doPoly(pc, polyId, 600, L1PolyMorph.MORPH_BY_ITEMMAGIC, false, false);
	}

	private void usePolyScale3(L1PcInstance pc, int itemId) {
		int polyId = 0;
					if (itemId == 4100023) { // 暗黑城庫茲英雄變身卷軸
						polyId = 16014;
					} else if (itemId == 4100024) { // 暗黑城奴隸英雄變身卷軸
						polyId = 15986;
					} else if (itemId == 4100025) { // 暗黑城愛麗絲英雄變身卷軸
						polyId = 16008;
					} else if (itemId == 4100026) { // 暗黑城赫爾賓英雄變身卷軸
						polyId = 16002;
					} else if (itemId == 4100027) { // 暗黑城哈丁英雄變身卷軸
						polyId = 16027;
					} else if (itemId == 4100028) { // 守護城昆特英雄變身卷軸
						polyId = 16284;
					} else if (itemId == 4100029) { // 守護城布魯迪卡英雄變身卷軸
						polyId = 16053;
					} else if (itemId == 4100030) { // 守護城普羅克英雄變身卷軸
						polyId = 16056;
					} else if (itemId == 4100031) { // 守護城吉利安英雄變身卷軸
						polyId = 16074;
					} else if (itemId == 4100032) { // 守護城喬英雄變身卷軸
						polyId = 16040;
					} else if (itemId == 3000474) { // 警衛(槍:綠色)變身卷軸
						polyId = 15531;
					} else if (itemId == 3000475) { // 警衛(弓:綠色)變身卷軸
						polyId = 15528;
					} else if (itemId == 3000476) { // 警衛(槍:藍色)變身卷軸
						polyId = 15832;
					} else if (itemId == 3000477) { // 警衛(弓:藍色)變身卷軸
						polyId = 15830;
					} else if (itemId == 3000478) { // 警衛(槍:紅色)變身卷軸
						polyId = 15833;
					} else if (itemId == 3000479) { // 警衛(弓:紅色)變身卷軸
						polyId = 15831;
					}

		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC, false, false);
	}

	private void usePolyScale4(L1PcInstance pc, int itemId) {
		int polyId = 0;
					if (itemId == 4100160) { // 傳說變身券
						polyId = 11685;
					} else if (itemId == 4100161) { // 神話變身券
						polyId = 12015;
					} else if (itemId == 4100162) { // 傳說變身券
						polyId = 11621;
						pc.sendPackets("解除武器時速度會降低。");
					} else if (itemId == 4100163) { // 神話變身券
						polyId = 11620;
						pc.sendPackets("解除武器時速度會降低。");

					} else if (itemId == 3000470) { // 阿圖恩
						polyId = 15073;
					} else if (itemId == 3000471) { // 月之吉利安
						polyId = 19019;
					} else if (itemId == 3000472) { // 黑暗喬
						polyId = 19022;
					} else if (itemId == 3000473) { // 騎士屠殺者
						polyId = 19026;
					}

		L1PolyMorph.doPoly(pc, polyId, 3600, L1PolyMorph.MORPH_BY_ITEMMAGIC, false, false);
	}

	private void usePolyCard(L1PcInstance pc, int itemId) {
		int polyid = 0;
		boolean ring = false;
		boolean ring2 = false;
		switch (itemId) {
			case 30001887: // 月之騎士吉利安變身卡
				polyid = 20442;
				break;
			case 30001888: // 影之騎士克里斯特變身卡
				polyid = 20469;
				break;
			case 30001889: // 多佩爾根格王變身卡
				polyid = 20438;
				break;
			case 30001890: // 鐵之騎士阿圖恩變身卡
				polyid = 20446;
				break;
			case 30001891: // 白鵝騎士伊西洛特變身卡
				polyid = 20471;
				break;
			case 30001892: // 幸運魔法師喬變身卡
				polyid = 20449;
				break;
			case 30001893: // 等級變身
				if (pc.isCrown()) {
					if (pc.get_sex() == 0) // 男
						polyid = 21817;
					else
						polyid = 21789;
				} else if (pc.isKnight()) {
					if (pc.get_sex() == 0)
						polyid = 20087;
					else
						polyid = 20088;
				} else if (pc.isElf()) {
					if (pc.get_sex() == 0)
						polyid = 20089;
					else
						polyid = 20090;
				} else if (pc.isWizard()) {
					if (pc.get_sex() == 0)
						polyid = 20263;
					else
						polyid = 20270;
				} else if (pc.isDarkelf()) {
					if (pc.get_sex() == 0)
						polyid = 20093;
					else
						polyid = 20094;
				} else if (pc.isDragonknight()) {
					if (pc.get_sex() == 0)
						polyid = 20095;
					else
						polyid = 20096;
				} else if (pc.isBlackwizard()) {
					if (pc.get_sex() == 0)
						polyid = 21094;
					else
						polyid = 20098;
				} else if (pc.isWarrior()) {
					if (pc.get_sex() == 0)
						polyid = 20619;
					else
						polyid = 20577;
				} else if (pc.isFencer()) {
					if (pc.get_sex() == 0)
						polyid = 20101;
					else
						polyid = 20102;
				} else if (pc.isLancer()) {
					if (pc.get_sex() == 0)
						polyid = 20103;
					else
						polyid = 20104;
				}
				break;
			case 30001894: // 真•死亡騎士（紅）變身卡
				polyid = 17541;
				break;
			case 30001895: // 真•死亡騎士（黑）變身卡
				polyid = 19689;
				break;
		}

		if (pc.isPolyRingMaster2()) {
			ring = false;
			ring2 = true;
		} else if (pc.isPolyRingMaster()) {
			ring = true;
			ring2 = false;
		}
		if (ring && ring2) {
			ring = false;
			ring2 = true;
		}

		L1PolyMorph.doPoly(pc, polyid, 3600, L1PolyMorph.MORPH_BY_ITEMMAGIC, ring, ring2);

	}
	private void usePolyRangking(L1PcInstance pc, int itemId) {
		int polyid = 0;
		if (itemId == 3000392) {
			if (pc.isCrown()) {
				if (pc.get_sex() == 0)
					polyid = 20085;
				else
					polyid = 20086;
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0)
					polyid = 20087;
				else
					polyid = 20088;
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0)
					polyid = 20089;
				else
					polyid = 20090;
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0)
					polyid = 20091;
				else
					polyid = 20092;
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0)
					polyid = 20093;
				else
					polyid = 20094;
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0)
					polyid = 20095;
				else
					polyid = 20096;
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0)
					polyid = 20097;
				else
					polyid = 20098;
			} else if (pc.isWarrior()) {
				if (pc.get_sex() == 0)
					polyid = 20099;
				else
					polyid = 20100;
			} else if (pc.isFencer()) {
				if (pc.get_sex() == 0)
					polyid = 20101;
				else
					polyid = 20102;
			} else if (pc.isLancer()) {
				if (pc.get_sex() == 0)
					polyid = 20103;
				else
					polyid = 20104;
			}
			L1PolyMorph.doPoly(pc, polyid, 600, L1PolyMorph.MORPH_BY_ITEMMAGIC, false, false);
		}
	}

	private void usePolyPotion(L1PcInstance pc, int itemId) {
		int polyId = 0;
		if (itemId == 41143) {
			polyId = 6086;
		} else if (itemId == 41144) {
			polyId = 6087;
		} else if (itemId == 41145) {
			polyId = 6088;
		} else if (itemId == 30057) {
			polyId = 10429;
		} else if (itemId == 30058) {
			polyId = 10431;
		} else if (itemId == 30059) {
			polyId = 10430;
		} else if (itemId == 8000) {
			polyId = 12792;
		} else if (itemId == 8001) { // 7580槍術大師變身
			polyId = 12237;
		} else if (itemId == 8002) { // 82真死亡騎士-極速
			polyId = 12015;
		} else if (itemId == 42655) { // 新英雄變身卷軸
			if (pc.isKnight() || pc.isDragonknight() || pc.isWarrior() || pc.isCrown() || pc.isFencer() || pc.isLancer()) {
				polyId = 12283;
			} else if (pc.isDarkelf()) {
				polyId = 12280;
			} else if (pc.isWizard()) {
				polyId = 12295;
			} else if (pc.isBlackwizard()) {
				polyId = 12286;
			} else if (pc.isElf()) {
				polyId = 12314;
			}
		} else if (itemId == 4100290) { // 真變身卷軸
			if (pc.isKnight() || pc.isWarrior() || pc.isCrown() || pc.isFencer() || pc.isLancer()) {
				polyId = 17541;
			} else if (pc.isDragonknight()) {
				polyId = 17549;
			} else if (pc.isDarkelf()) {
				polyId = 17531;
			} else if (pc.isWizard()) {
				polyId = 17515;
			} else if (pc.isBlackwizard()) {
				polyId = 17541;
			} else if (pc.isElf()) {
				polyId = 17535;
				if (pc.getWeapon() == null)
					polyId = 17541;
				else {
					int type = pc.getWeapon().getItem().getType1();
					if (type != 20 && type != 62)
						polyId = 17541;
				}
			}
		} else if (itemId == 4100284) { // 真死亡騎士變身卷軸
			polyId = 17541;
		} else if (itemId == 4100285) { // 真影子變身卷軸
			polyId = 17531;
		} else if (itemId == 4100286) { // 真槍術大師變身卷軸
			polyId = 17545;
		} else if (itemId == 4100287) { // 真巴風特變身卷軸
			polyId = 17515;
		} else if (itemId == 4100288) { // 真警衛(弓)變身卷軸
			polyId = 17535;
		} else if (itemId == 4100289) { // 真警衛(槍)變身卷軸
			polyId = 17549;
		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC, false, false);
	}

	// private void useLevelPolyScroll(L1PcInstance pc, int itemId) {// 夏爾娜
	// int polyId = 0;
	// if (itemId == 210097) { // 30
	// if(pc.isCrown()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6822;
	// } else {
	// polyId = 6823;
	// }
	// } else if (pc.isKnight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6824;
	// } else {
	// polyId = 6825;
	// }
	// } else if (pc.isElf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6826;
	// } else {
	// polyId = 6827;
	// }
	// } else if (pc.isWizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6828;
	// } else {
	// polyId = 6829;
	// }
	// } else if (pc.isDarkelf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6830;
	// } else {
	// polyId = 6831;
	// }
	// } else if (pc.isDragonknight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7139;
	// } else {
	// polyId = 7140;
	// }
	// } else if (pc.isBlackwizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7141;
	// } else {
	// polyId = 7142;
	// }
	// } else if (pc.isWarrior()) {
	// if (pc.get_sex() == 0) {
	// polyId = 12490;
	// } else {
	// polyId = 12494;
	// }
	// } else if (pc.isFencer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 18520;
	// } else {
	// polyId = 18499;
	// }
	// } else if (pc.isLancer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 19414;
	// } else {
	// polyId = 19468;
	// }
	// }
	// } else if (itemId == 210098) { // 40
	// if(pc.isCrown()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6832;
	// } else {
	// polyId = 6833;
	// }
	// } else if (pc.isKnight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6834;
	// } else {
	// polyId = 6835;
	// }
	// } else if (pc.isElf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6836;
	// } else {
	// polyId = 6837;
	// }
	// } else if (pc.isWizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6838;
	// } else {
	// polyId = 6839;
	// }
	// } else if (pc.isDarkelf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6840;
	// } else {
	// polyId = 6841;
	// }
	// } else if (pc.isDragonknight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7143;
	// } else {
	// polyId = 7144;
	// }
	// } else if (pc.isBlackwizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7145;
	// } else {
	// polyId = 7146;
	// }
	// } else if (pc.isWarrior()) {
	// if (pc.get_sex() == 0) {
	// polyId = 12490;
	// } else {
	// polyId = 12494;
	// }
	// } else if (pc.isFencer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 18520;
	// } else {
	// polyId = 18499;
	// }
	// } else if (pc.isLancer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 19414;
	// } else {
	// polyId = 19468;
	// }
	// }
	// } else if (itemId == 210099) { // 52
	// if(pc.isCrown()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6842;
	// } else {
	// polyId = 6843;
	// }
	// } else if (pc.isKnight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6844;
	// } else {
	// polyId = 6845;
	// }
	// } else if (pc.isElf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6846;
	// } else {
	// polyId = 6847;
	// }
	// } else if (pc.isWizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6848;
	// } else {
	// polyId = 6849;
	// }
	// } else if (pc.isDarkelf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6850;
	// } else {
	// polyId = 6851;
	// }
	// } else if (pc.isDragonknight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7147;
	// } else {
	// polyId = 7148;
	// }
	// } else if (pc.isBlackwizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7149;
	// } else {
	// polyId = 7150;
	// }
	// } else if (pc.isWarrior()) {
	// if (pc.get_sex() == 0) {
	// polyId = 12490;
	// } else {
	// polyId = 12494;
	// }
	// } else if (pc.isFencer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 18520;
	// } else {
	// polyId = 18499;
	// }
	// } else if (pc.isLancer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 19414;
	// } else {
	// polyId = 19468;
	// }
	// }
	// } else if (itemId == 210100) { // 55
	// if(pc.isCrown()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6852;
	// } else {
	// polyId = 6853;
	// }
	// } else if (pc.isKnight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6854;
	// } else {
	// polyId = 6855;
	// }
	// } else if (pc.isElf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6856;
	// } else {
	// polyId = 6857;
	// }
	// } else if (pc.isWizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6858;
	// } else {
	// polyId = 6859;
	// }
	// } else if (pc.isDarkelf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6860;
	// } else {
	// polyId = 6861;
	// }
	// } else if (pc.isDragonknight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7151;
	// } else {
	// polyId = 7152;
	// }
	// } else if (pc.isBlackwizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7153;
	// } else {
	// polyId = 7154;
	// }
	// } else if (pc.isWarrior()) {
	// if (pc.get_sex() == 0) {
	// polyId = 12490;
	// } else {
	// polyId = 12494;
	// }
	// } else if (pc.isFencer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 18520;
	// } else {
	// polyId = 18499;
	// }
	// } else if (pc.isLancer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 19414;
	// } else {
	// polyId = 19468;
	// }
	// }
	// } else if (itemId == 210101) { // 60
	// if(pc.isCrown()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6862;
	// } else {
	// polyId = 6863;
	// }
	// } else if (pc.isKnight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6864;
	// } else {
	// polyId = 6865;
	// }
	// } else if (pc.isElf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6866;
	// } else {
	// polyId = 6867;
	// }
	// } else if (pc.isWizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6868;
	// } else {
	// polyId = 6869;
	// }
	// } else if (pc.isDarkelf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6870;
	// } else {
	// polyId = 6871;
	// }
	// } else if (pc.isDragonknight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7155;
	// } else {
	// polyId = 7156;
	// }
	// } else if (pc.isBlackwizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7157;
	// } else {
	// polyId = 7158;
	// }
	// } else if (pc.isWarrior()) {
	// if (pc.get_sex() == 0) {
	// polyId = 12490;
	// } else {
	// polyId = 12494;
	// }
	// } else if (pc.isFencer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 18520;
	// } else {
	// polyId = 18499;
	// }
	// } else if (pc.isLancer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 19414;
	// } else {
	// polyId = 19468;
	// }
	// }
	// } else if (itemId == 210102) { // 65
	// if(pc.isCrown()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6872;
	// } else {
	// polyId = 6873;
	// }
	// } else if (pc.isKnight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6874;
	// } else {
	// polyId = 6875;
	// }
	// } else if (pc.isElf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6876;
	// } else {
	// polyId = 6877;
	// }
	// } else if (pc.isWizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6878;
	// } else {
	// polyId = 6879;
	// }
	// } else if (pc.isDarkelf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6880;
	// } else {
	// polyId = 6881;
	// }
	// } else if (pc.isDragonknight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7159;
	// } else {
	// polyId = 7160;
	// }
	// } else if (pc.isBlackwizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7161;
	// } else {
	// polyId = 7162;
	// }
	// } else if (pc.isWarrior()) {
	// if (pc.get_sex() == 0) {
	// polyId = 12490;
	// } else {
	// polyId = 12494;
	// }
	// } else if (pc.isFencer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 18520;
	// } else {
	// polyId = 18499;
	// }
	// } else if (pc.isLancer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 19414;
	// } else {
	// polyId = 19468;
	// }
	// }
	// } else if (itemId == 210103) { // 70
	// if(pc.isCrown()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6882;
	// } else {
	// polyId = 6883;
	// }
	// } else if (pc.isKnight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6884;
	// } else {
	// polyId = 6885;
	// }
	// } else if (pc.isElf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6886;
	// } else {
	// polyId = 6887;
	// }
	// } else if (pc.isWizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6888;
	// } else {
	// polyId = 6889;
	// }
	// } else if (pc.isDarkelf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 6890;
	// } else {
	// polyId = 6891;
	// }
	// } else if (pc.isDragonknight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7163;
	// } else {
	// polyId = 7164;
	// }
	// } else if (pc.isBlackwizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 7165;
	// } else {
	// polyId = 7166;
	// }
	// } else if (pc.isWarrior()) {
	// if (pc.get_sex() == 0) {
	// polyId = 12490;
	// } else {
	// polyId = 12494;
	// }
	// } else if (pc.isFencer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 18520;
	// } else {
	// polyId = 18499;
	// }
	// } else if (pc.isLancer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 19414;
	// } else {
	// polyId = 19468;
	// }
	// }
	// } else if (itemId == 210116) { // 75
	// if (pc.isCrown()) {
	// if (pc.get_sex() == 0) {
	// polyId = 10987;
	// } else {
	// polyId = 10988;
	// }
	// } else if (pc.isKnight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 10989;
	// } else {
	// polyId = 10990;
	// }
	// } else if (pc.isElf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 10991;
	// } else {
	// polyId = 10992;
	// }
	// } else if (pc.isWizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 10993;
	// } else {
	// polyId = 10994;
	// }
	// } else if (pc.isDarkelf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 10995;
	// } else {
	// polyId = 10996;
	// }
	// } else if (pc.isDragonknight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 10997;
	// } else {
	// polyId = 10998;
	// }
	// } else if (pc.isBlackwizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 10999;
	// } else {
	// polyId = 11000;
	// }
	// } else if (pc.isWarrior()) {
	// if (pc.get_sex() == 0) {
	// polyId = 12490;
	// } else {
	// polyId = 12494;
	// }
	// } else if (pc.isFencer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 18520;
	// } else {
	// polyId = 18499;
	// }
	// } else if (pc.isLancer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 19414;
	// } else {
	// polyId = 19468;
	// }
	// }
	// } else if (itemId == 210117) { // 80
	// if (pc.isCrown()) {
	// if (pc.get_sex() == 0) {
	// polyId = 11001;
	// } else {
	// polyId = 11002;
	// }
	// } else if (pc.isKnight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 11003;
	// } else {
	// polyId = 11004;
	// }
	// } else if (pc.isElf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 11005;
	// } else {
	// polyId = 11006;
	// }
	// } else if (pc.isWizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 11007;
	// } else {
	// polyId = 11008;
	// }
	// } else if (pc.isDarkelf()) {
	// if (pc.get_sex() == 0) {
	// polyId = 11009;
	// } else {
	// polyId = 11010;
	// }
	// } else if (pc.isDragonknight()) {
	// if (pc.get_sex() == 0) {
	// polyId = 11011;
	// } else {
	// polyId = 11012;
	// }
	// } else if (pc.isBlackwizard()) {
	// if (pc.get_sex() == 0) {
	// polyId = 11013;
	// } else {
	// polyId = 11014;
	// }
	// } else if (pc.isWarrior()) {
	// if (pc.get_sex() == 0) {
	// polyId = 12490;
	// } else {
	// polyId = 12494;
	// }
	// } else if (pc.isFencer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 18520;
	// } else {
	// polyId = 18499;
	// }
	// } else if (pc.isLancer()) {
	// if (pc.get_sex() == 0) {
	// polyId = 19414;
	// } else {
	// polyId = 19468;
	// }
	// }
	// }
	// L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC,
	// false);
	// }
	private int range = 0;
	private int dmg = 0;
	private int effect = 0;

	private void useAreaWand(L1PcInstance pc, int itemId) {
		if ((itemId >= 420103 && itemId <= 420111) && !(pc.getMapId() >= 732 && pc.getMapId() <= 776)) {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(563))); // 這裡不能使用。
			return;
		}
		int chargeCount = useItem.getChargeCount();
		if (chargeCount <= 0) {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79))); // 什麼事都沒有發生。
			pc.getInventory().consumeItem(itemId);
			return;
		}
		if (pc.isInvisble()) {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1003))); // 無法在隱形狀態下使用。
			return;
		}

		switch (itemId) {
			case 420103:
				effect = 19124;
				range = 4;
				dmg = 100;
				break;
			case 420105:
				effect = 1819;
				range = 4;
				dmg = 100;
				break;
			case 420107:
				effect = 19138;
				range = 4;
				dmg = 100;
				break;
			case 420110:
				effect = 18931;
				range = 4;
				dmg = 100;
				break;
			case 420106:
				effect = 757;
				range = 16;
				dmg = 100;
				break;
			case 420109:
				effect = 19110;
				range = 16;
				dmg = 100;
				break;
			default:
				break;
		}
		L1MonsterInstance mon = null;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, range)) {
			if (obj == null) {
				continue;
			}
			if (!(obj instanceof L1Character)) {
				continue;
			}
			if (obj.getId() == pc.getId()) {
				continue;
			}
			if (obj instanceof L1MonsterInstance) {
				mon = (L1MonsterInstance) obj;
				Broadcaster.broadcastPacket(mon, new S_DoActionGFX(mon.getId(), ActionCodes.ACTION_Damage), true);
				mon.receiveDamage(pc, dmg);
			}
		}

//		System.out.println(itemId+"+"+effect);
		pc.send_effect(effect);
		pc.broadcastPacket(new S_UseAttackSkill(pc, 0, effect, pc.getX(), pc.getY(), 18), true);

		if (chargeCount <= 1) {
			pc.getInventory().removeItem(useItem, 1);
		} else {
			useItem.setChargeCount(chargeCount - 1);
			pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
		}
		L1ItemDelay.onItemUse(pc, useItem);
		effect = 0;
		range = 0;
		dmg = 0;

	}

	private void UseArmor(L1PcInstance activeChar, L1ItemInstance armor) {
		int type = armor.getItem().getType();
		L1PcInventory pcInventory = activeChar.getInventory();

		if (armor.getItemId() == 900111 && !armor.isEquipped()) {
			if (pcInventory.checkEquippedAtOnce(new int[] { 900111, 20288 })) {
				activeChar.sendPackets("無法同時佩戴多個瞬間移動支配戒指或與瞬間移動控制戒指一起佩戴。");
				return;
			}
		}

		boolean equipeSpace; // 是否有可裝備的位置
		if (type == 9) { // 如果是戒指
			equipeSpace = pcInventory.getTypeEquipped(2, 9) <= 6;
		} else if (type == 12) { // 如果是耳環
			equipeSpace = pcInventory.getTypeEquipped(2, 12) <= 4;
		} else {
			equipeSpace = pcInventory.getTypeEquipped(2, type) <= 0;

			if (!equipeSpace && !armor.isEquipped()) {
				L1ItemInstance off_armor = null;
				for (L1ItemInstance on_armor : activeChar.getEquipSlot().getArmors()) {
					if (on_armor.getItem().getType() == type) {
						off_armor = on_armor;
						equipeSpace = true;
					}
				}
				pcInventory.setEquipped(off_armor, false, false, false, false);
			}
		}

		if (equipeSpace && !armor.isEquipped()) {
			if (type == 9) { // 如果類型是 9
				int slot_count = 2;
				if (activeChar.getQuest().isEnd(L1Quest.QUEST_RING_LEFT_SLOT60))
					slot_count++;
				if (activeChar.getQuest().isEnd(L1Quest.QUEST_RING_RIGHT_SLOT60))
					slot_count++;
				if (activeChar.getQuest().isEnd(L1Quest.QUEST_RING_LEFT_SLOT95))
					slot_count++;
				if (activeChar.getQuest().isEnd(L1Quest.QUEST_RING_RIGHT_SLOT100))
					slot_count++;

				if (pcInventory.getTypeEquipped(2, 9) == 6) { // 如果已經裝備了 6 個
					activeChar.sendPackets(144); // 插槽中已經裝備了物品。
					return;
				}
				if (pcInventory.getTypeEquipped(2, 9) >= slot_count) {
					activeChar.sendPackets(new S_SystemMessage("插槽開放後可裝備"));
					return;
				}

			}

			if (pcInventory.getTypeAndItemIdEquipped(2, 9, armor.getItem().getItemId()) == 2) { // 已經
				activeChar.sendPackets(new S_ServerMessage(3278)); // 插槽擴展：相應
				return;
			} else if (pcInventory.getTypeAndGradeEquipped(2, 9, armor.getItem().getGrade()) == 2) { // Snapper 2->3 變更
				if (type == 9) { // 當試圖裝備的物品的唯一屬性編號為 3 的裝備有 2 個時
					activeChar.sendPackets(new S_ServerMessage(3279)); // 插槽擴展：
					return;
				}
			}

			if (type == 12) { // 如果類型是 12
				int slot_count = 1;
				if (activeChar.getQuest().isEnd(L1Quest.QUEST_EARRING_SLOT60)) {
					slot_count++;
				}
				if (activeChar.getQuest().isEnd(L1Quest.QUEST_EARRING_LEFT_SLOT101)) {
					slot_count++;
				}
				if (activeChar.getQuest().isEnd(L1Quest.QUEST_EARRING_RIGHT_SLOT103)) {
					slot_count++;
				}
				if (pcInventory.getTypeEquipped(2, 12) == 4) {
					activeChar.sendPackets(144); // 插槽中已經裝備了物品。
					return;
				}
				if (pcInventory.getTypeEquipped(2, 12) >= slot_count) {
					activeChar.sendPackets(new S_SystemMessage("插槽開放後可裝備"));
					return;
				}

			} else if (type == 31) { // 肩甲
				if (!activeChar.getQuest().isEnd(L1Quest.QUEST_SLOT_SHOULD)) {
					activeChar.sendPackets(new S_ServerMessage(5077));
					return;
				} else if (pcInventory.getTypeEquipped(2, 31) >= 1) {
					activeChar.sendPackets(144); // 插槽中已經裝備了物品。
					return;
				}
			} else if (type == 32) { // 徽章
				if (!activeChar.getQuest().isEnd(L1Quest.QUEST_SLOT_BADGE)) {
					activeChar.sendPackets(new S_ServerMessage(5078));
					return;
				} else if (pcInventory.getTypeEquipped(2, 32) >= 1) {
					activeChar.sendPackets(144); // 插槽中已經裝備了物品。
					return;
				}
			}
			if (pcInventory.getTypeAndItemIdEquipped(2, 12, armor.getItem().getItemId()) >= 1) { // 已經
				activeChar.sendPackets(new S_ServerMessage(3278)); // 插槽擴展：相應
				return;
			} else if (type == 12) {
				if (pcInventory.getNameEquipped(2, 12, armor.getName()) >= 1) {
					activeChar.sendPackets(new S_ServerMessage(3278)); // 插槽擴展：
					return;
				}
				if (pcInventory.getTypeAndGradeEquipped(2, 12, armor.getItem().getGrade()) == 2) {
					activeChar.sendPackets(new S_ServerMessage(3279)); // 插槽擴展：
					return;
				}
			}

			int polyid = activeChar.getCurrentSpriteId();
			if (!L1PolyMorph.isEquipableArmor(polyid, type)) { // 該變身狀態下無法裝備
				return;
			}
			if (type == 7 && pcInventory.getTypeEquipped(2, 13) >= 1
					|| type == 13 && pcInventory.getTypeEquipped(2, 7) >= 1) {
				activeChar.sendPackets(new S_ServerMessage(124));
				return;
			}

			if ((type == 7 || type == 13) && activeChar.getEquipSlot().getWeaponCount() == 2) {
				activeChar.sendPackets(new S_ServerMessage(129));
				return;
			}

			if (type == 7 && activeChar.getWeapon() != null) {
				if (activeChar.getWeapon().getItem().isTwohandedWeapon() && armor.getItem().getUseType() != 13) {
					activeChar.sendPackets(new S_ServerMessage(129));
					return;
				}
			}
			cancelAbsoluteBarrier(activeChar); // 解除絕對屏障
			pcInventory.setEquipped(armor, true);
		} else if (armor.isEquipped()) { // 如果已經裝備了使用的防具（嘗試卸下）
			pcInventory.setEquipped(armor, false);
		} else {
			activeChar.sendPackets(new S_ServerMessage(124)); // 1 已經裝備了某些東西
		}
		activeChar.setCurrentHp(activeChar.getCurrentHp());
		activeChar.setCurrentMp(activeChar.getCurrentMp());
		activeChar.sendPackets(new S_OwnCharAttrDef(activeChar));
		activeChar.sendPackets(new S_OwnCharStatus(activeChar));
		activeChar.sendPackets(new S_SPMR(activeChar));
	}

	private void UseWeapon(L1PcInstance activeChar, L1ItemInstance weapon) {
		boolean shieldWeapon = false;
		L1PcInventory pcInventory = activeChar.getInventory();
		L1ItemInstance current_weapon = activeChar.getEquipSlot().getWeapon();
		if (current_weapon == null || !activeChar.getEquipSlot().isWeapon(weapon)) {
			// 檢查指定武器是否與當前裝備的武器不同，並確認是否可以裝備
			int weapon_type = weapon.getItem().getType();
			int polyid = activeChar.getCurrentSpriteId();

			if (!L1PolyMorph.isEquipableWeapon(polyid, weapon_type)) {
					// 該變身狀態下無法裝備
				return;
			}

			if (weapon.getItem().isTwohandedWeapon() && pcInventory.getTypeEquipped(2, 7) >= 1) {
				// 如果是雙手武器，確認是否裝備了盾牌
				activeChar.sendPackets(new S_ServerMessage(128));
				// 1當裝備了盾牌時，無法使用雙手武器。
				return;
			}
		}

						cancelAbsoluteBarrier(activeChar); // 解除絕對屏障

						if (current_weapon != null) {
							// 已經裝備了某些東西的情況下，卸下之前的裝備
							if (current_weapon.getItem().getBless() == 2) {
								// 如果已被詛咒
								activeChar.sendPackets(new S_ServerMessage(150));
								// 1 無法卸下。看起來被詛咒了。
								return;
							}

							// 當所裝備的武器與請求的武器相同時。
							if (activeChar.getEquipSlot().isWeapon(weapon)) {
								if (activeChar.getEquipSlot().getWeaponCount() >= 2) {
									// 卸下已裝備的兩個武器。
									L1ItemInstance slot_1 = activeChar.getEquipSlot().getWeapon();
									pcInventory.setEquipped(slot_1, false, false, false, false);
									L1ItemInstance slot_2 = activeChar.getEquipSlot().getWeapon();
									pcInventory.setEquipped(slot_2, false, false, false, false);
									// 只重新裝備一個。
									if (slot_2.getId() == weapon.getId())
										pcInventory.setEquipped(slot_1, true, false, false, false);
									else
										pcInventory.setEquipped(slot_2, true, false, false, false);
								} else {
									// 不進行裝備交換，只是卸下
									pcInventory.setEquipped(weapon, false, false, false, false);
								}
				return;
					// 當所裝備的武器與請求的武器不同時。
					} else {
					// 當欲裝備的物品和已裝備的物品都是單手斧時。
								/*
								 * if (activeChar.isPassive(MJPassiveID.SLAYER.toInt()) &&
								 * weapon.getItem().getType1() == 11 && current_weapon.getItem().getType1() ==
								 * 11 && weapon.getItem().getType() == 6 && current_weapon.getItem().getType()
								 * == 6) { if (pcInventory.getTypeEquipped(2, 7) >= 1) { // 1 當裝備了盾牌時，無法使用雙手武器。
								 * activeChar.sendPackets(new S_ServerMessage(128)); return; } // 如果裝備了護臂 if
								 * (pcInventory.getTypeEquipped(2, 13) >= 1) { // 1 當裝備了盾牌時，無法使用雙手武器。
								 * activeChar.sendPackets(new S_ServerMessage(128)); return; }
								 *
								 * if (activeChar.getEquipSlot().getWeaponCount() >= 1) { // 已經裝備中。 for
								 * (L1ItemInstance item : activeChar.getEquipSlot().getWeapons())
								 * pcInventory.setEquipped(item, false, false, true, false);
								 * //activeChar.sendPackets(new S_ServerMessage(124)); return; }
								 *
								 * // 通知裝備槽更改為盾牌。 shieldWeapon = true; } else {
								 */
				// 解除當前裝備的武器。
				for (L1ItemInstance item : activeChar.getEquipSlot().getWeapons())
					pcInventory.setEquipped(item, false, false, true, false);
				// }
			}
		}

		if (weapon.getItemId() == 200002) { // 被詛咒的骰子匕首
			activeChar.sendPackets(new S_ServerMessage(149, weapon.getLogName())); // 1%0已
		}
		pcInventory.setEquipped(weapon, true, false, false, shieldWeapon);
	}

	private int RandomELevel(L1ItemInstance item, int itemId) {
		int j = _random.nextInt(100) + 1;
		if (itemId == L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR || itemId == L1ItemId.B_SCROLL_OF_ENCHANT_WEAPON
				|| itemId == L1ItemId.Inadril_T_ScrollB || itemId == L1ItemId.Inadril_T_ScrollB2
				|| itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR || itemId == L1ItemId.B_SCROLL_CHIVALRIC_ENCHANT_ARMOR
				|| itemId == L1ItemId.C_SCROLL_CHIVALRIC_ENCHANT_ARMOR
				|| itemId == L1ItemId.B_SCROLL_CHIVALRIC_ENCHANT_WEAPON) {
			if (item.getEnchantLevel() <= -1) {
				return 1;
			} else if (item.getEnchantLevel() <= Config.ServerEnchant.Enchant_Value) {
				if (j < Config.ServerEnchant.Enchant_Value_Chance) { // +1
					return 1;
				} else if (j >= Config.ServerEnchant.Enchant_Value_Chance1
						&& j <= Config.ServerEnchant.Enchant_Value_Chance2) { // +2
					return 2;
					// } else if (j >= 71 && j <= 100) { // +2
					// System.out.println("cccccccc : " + j);
					// return 2;// 원본3
				}
			} else if (item.getEnchantLevel() >= Config.ServerEnchant.Enchant_Value1
					&& item.getEnchantLevel() <= Config.ServerEnchant.Enchant_Value2) {
				if (j < Config.ServerEnchant.Enchant_Value_Chance3) {
					return 2;
				} else {
					return 1;
				}
			}
			return 1;
		} else if (itemId == 140129 || itemId == 140130) {
			if (item.getEnchantLevel() < 0) {
				if (j < 30) {
					return 2;
				} else {
					return 1;
				}
			} else if (item.getEnchantLevel() <= 2) {
				if (j < 32) {
					return 1;
				} else if (j >= 33 && j <= 60) {
					return 2;
				} else if (j >= 61 && j <= 100) {
					return 3;
				}
			} else if (item.getEnchantLevel() >= 3 && item.getEnchantLevel() <= 5) {
				if (j < 60) {
					return 2;
				} else {
					return 1;
				}
			}
			return 1;
		}
		return 1;
	}

	private void useSpellBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int itemAttr = 1;
		int locAttr = 1; // 0:other 1:law 2:chaos
		boolean isLawful = true;
		// int pcX = pc.getX();
		// int pcY = pc.getY();
		// int mapId = pc.getMapId();
		int level = pc.getLevel();
		if (itemId == 45000 || itemId == 45008 || itemId == 45018 || itemId == 45021 || itemId == 40171
				|| itemId == 40179 || itemId == 40180 || itemId == 40182 || itemId == 40194 || itemId == 40197
				|| itemId == 40202 || itemId == 40206 || itemId == 40213 || itemId == 40220 || itemId == 40222) {
			itemAttr = 1;
		}
		if (itemId == 45009 || itemId == 45010 || itemId == 45019 || itemId == 40172 || itemId == 40173
				|| itemId == 40178 || itemId == 40185 || itemId == 40186 || itemId == 40192 || itemId == 40196
				|| itemId == 40201 || itemId == 40204 || itemId == 40211 || itemId == 40221 || itemId == 40225) {
			itemAttr = 1;
		}

		if (pc.isGm()) {
			SpellBook(pc, item, isLawful);
		} else if ((itemAttr == locAttr || itemAttr == 0) && locAttr != 0) {
			if (pc.isKnight() || pc.isWarrior()) {
				if (itemId >= 45000 && itemId <= 45007 && level >= 50) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45000 && itemId <= 45007) {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(312)));
				} else {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				}
			} else if (pc.isCrown() || pc.isDarkelf() || pc.isFencer() || pc.isDragonknight() || pc.isLancer()) {
				if (itemId >= 45000 && itemId <= 45007 && level >= 15) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45008 && itemId <= 45015 && level >= 30) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45008 && itemId <= 45015 || itemId >= 45000 && itemId <= 45007) {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(312)));
				} else {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				}
			} else if (pc.isElf()) {
				if (itemId >= 45000 && itemId <= 45007 && level >= 10) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45008 && itemId <= 45015 && level >= 20) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45016 && itemId <= 45022 && level >= 30) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40170 && itemId <= 40177 && level >= 40) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40178 && itemId <= 40185 || itemId == 30001575 && level >= 50) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40186 && itemId <= 40193 && level >= 60) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45000 && itemId <= 45022 || itemId >= 40170 && itemId <= 40193) {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(312)));
				} else {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				}
			} else if (pc.isWizard()) {
				if (itemId >= 45000 && itemId <= 45007 && level >= 8) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45008 && itemId <= 45015 && level >= 16) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45016 && itemId <= 45022 && level >= 24) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40170 && itemId <= 40177 && level >= 32) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40178 && itemId <= 40185 || itemId == 30001575 && level >= 40) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40186 && itemId <= 40193 && level >= 48) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40194 && itemId <= 40201 || itemId == 30001566 || itemId == 30001568
						|| itemId == 30001569 || itemId == 30001574 && level >= 56) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40202 && itemId <= 40209 || itemId == 30001571 && level >= 64) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40210 && itemId <= 40217 || itemId == 30001567 && level >= 72) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40218 && itemId <= 40225 || itemId == 30001576 || itemId == 30001577
						|| itemId == 30001579 || itemId == 30001580 || itemId == 30001572 || itemId == 30001587
						|| itemId >= 30001752 && itemId <= 30001755 && level >= 80) {
					SpellBook(pc, item, isLawful);
				} else if (itemId == 3000095 || itemId == 30001580
						|| itemId >= 30001749 && itemId <= 30001751 && level >= 80) {
					SpellBook(pc, item, isLawful);
				} else if (itemId == 4100538 || itemId == 4100717 || itemId == 4101512 || itemId == 30001583
						|| itemId == 30001584 || itemId == 30001586 || itemId == 30001591 || itemId == 30001688
						|| itemId == 30001794 && level >= 85) {
					SpellBook(pc, item, isLawful);
				} else {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(312)));
				}
			}
		} else if (itemAttr != locAttr && itemAttr != 0 && locAttr != 0) {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
			S_SkillSound effect = new S_SkillSound(pc.getId(), 10);
			pc.sendPackets(effect);
			pc.broadcastPacket(effect);
			pc.setCurrentHp(Math.max(pc.getCurrentHp() - 45, 0));
			if (pc.getCurrentHp() <= 0) {
				pc.death(null, true);
			}
			pc.getInventory().removeItem(item, 1);
		} else {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
		}
	}

	private void useElfSpellBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int level = pc.getLevel();
		if ((pc.isElf() || pc.isGm())/* && isLearnElfMagic(pc) */) {
			if (itemId >= 40232 && itemId <= 40234 && level >= 15) { // 精靈 1階段
				SpellBook2(pc, item);
			} else if (itemId >= 40235 && itemId <= 40236 && level >= 30) { // 精靈 2階段
				SpellBook2(pc, item);
			} else if ((itemId >= 40237 && itemId <= 40240 || itemId == 30001494 || itemId == 30001519)
					&& level >= 45) { // 精靈 3階段
				SpellBook2(pc, item);
			} else if ((itemId >= 40241 && itemId <= 40243 || itemId == 30001495 || itemId == 30001496
					|| itemId == 30001497 || itemId == 30001501 || itemId == 30001503 || itemId == 30001505)
					&& level >= 60) { // 精靈 4階段
				SpellBook2(pc, item);
			} else if ((itemId >= 40244 && itemId <= 40246 || itemId == 30001498 || itemId == 30001499
					|| itemId == 30001500 || itemId == 30001502 || itemId == 30001504 || itemId == 30001510
					|| itemId == 30001518) && level >= 75) { // 精靈 5階段
				SpellBook2(pc, item);
			} else if (itemId >= 40247 && itemId <= 40248 && level >= 30) {
				SpellBook2(pc, item);
			} else if (itemId == 40250 && level >= 40) {
				SpellBook2(pc, item);
			} else if (itemId >= 40251 && itemId <= 40252 && level >= 50) {
				SpellBook2(pc, item);
			} else if (itemId == 40253 && level >= 30) {
				SpellBook2(pc, item);
			} else if (itemId == 40254 && level >= 40) {
				SpellBook2(pc, item);
			} else if (itemId == 40255 && level >= 50) {
				SpellBook2(pc, item);
			} else if (itemId == 40256 && level >= 30) {
				SpellBook2(pc, item);
			} else if (itemId == 40257 && level >= 40) {
				SpellBook2(pc, item);
			} else if (itemId >= 40258 && itemId <= 40259 && level >= 50) {
				SpellBook2(pc, item);
			} else if (itemId >= 40260 && itemId <= 40261 && level >= 30) {
				SpellBook2(pc, item);
			} else if (itemId == 40262 && level >= 40) {
				SpellBook2(pc, item);
			} else if (itemId >= 40263 && itemId <= 40264 && level >= 50) {
				SpellBook2(pc, item);
			} else if (itemId == 41150 && level >= 50) {
				SpellBook2(pc, item);
			} else if (itemId == 41151 && level >= 40) {
				SpellBook2(pc, item);
			} else if ((itemId == 3000512 || itemId == 3000513) && level >= 60) {
				SpellBook2(pc, item);
			} else if (itemId == 3000511 && level >= 75) {
				SpellBook2(pc, item);
			} else if (itemId == 3000091 && level >= 80) {
				SpellBook2(pc, item);
			} else if ((itemId == 40249 || itemId == 41149 || itemId == 41152 || itemId == 41153 || itemId == 30001506
					|| itemId == 30001507 || itemId == 30001508 || itemId == 30001509 || itemId == 4100295
					|| itemId == 30001511 || itemId == 30001713 || itemId == 30001714
					|| itemId >= 30001715 && itemId <= 30001718) && level >= 80) {
				SpellBook2(pc, item);
			} else if ((itemId == 4100567 || itemId == 4100714 || itemId == 4101639 || itemId == 30001512
					|| itemId == 30001514 || itemId == 30001516 || itemId == 30001515 || itemId == 30001520
					|| itemId == 30001523) && level >= 85) {
				SpellBook2(pc, item);
			} else if ((itemId == 4101636 || itemId == 30001521) && level >= 90) {
				SpellBook2(pc, item);
			}
		} else {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
		}
	}

					// TODO 使其在妖精水晶村學習
	/*
	 * private boolean isLearnElfMagic(L1PcInstance pc) { int pcX = pc.getX(); int
	 * pcY = pc.getY(); int pcMapId = pc.getMapId(); if (pcX >= 1 && pcX <= 40000 &&
	 * pcY >= 1 && pcY <= 40000 && pcMapId == 4 || pcX >= 32448 && pcX <= 32831 &&
	 * pcY >= 32704 && pcY <= 33023 && pcMapId == 7783 || pcX >= 32704 && pcX <=
	 * 32831 && pcY >= 32768 && pcY <= 32959 && pcMapId == 12152 || pcX >= 32704 &&
	 * pcX <= 32895 && pcY >= 32768 && pcY <= 32959 && pcMapId == 12149 || pcX >=
	 * 32704 && pcX <= 32831 && pcY >= 32768 && pcY <= 32895 && pcMapId == 12146 ||
	 * pcX >= 32640 && pcX <= 32831 && pcY >= 32896 && pcY <= 33151 && pcMapId ==
	 * 12147 || pcX >= 32640 && pcX <= 32831 && pcY >= 32768 && pcY <= 32895 &&
	 * pcMapId == 12148 || pcX >= 32512 && pcX <= 32639 && pcY >= 32832 && pcY <=
	 * 33023 && pcMapId == 12150 || pcX >= 32512 && pcX <= 32639 && pcY >= 32832 &&
	 * pcY <= 33023 && pcMapId == 12257 || pcX >= 32768 && pcX <= 32831 && pcY >=
	 * 32768 && pcY <= 32831 && pcMapId == 12358 || pc.getLocation().isInScreen(new
	 * Point(33055, 32336)) && pcMapId == 4) { return true; } return false; }
	 */

	private void LancerSpellBook(L1PcInstance pc, L1ItemInstance item) {
		String s = "";
		int spellId = 0;
		L1Skills l1skills = null;
		for (int skillId = 5051; skillId <= 5056; skillId++) {
			l1skills = SkillsTable.getInstance().getTemplate(skillId);
			if (l1skills == null) {
				System.out.println(String.format("遺漏的技能 ID : %d", skillId));
				continue;
			}
			String s1 = "槍術書 (" + l1skills.getName() + ")";
			if (item.getItem().getName().startsWith(s1)) {
				s = l1skills.getName();
				spellId = l1skills.getSkillId();
			}
		}

		if (pc.isSkillMastery(spellId)) {
			pc.sendPackets(new S_SystemMessage("這是一個已經學會的魔法。"), true);
			return;
		}

		int objid = pc.getId();

		SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
		noti.appendNewSpell(spellId, true);
		pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		S_SkillSound s_skillSound = new S_SkillSound(objid, 231);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(objid, spellId, s, 0, 0);
		pc.getInventory().removeItem(item, 1);
	}

	private void SpellBook(L1PcInstance pc, L1ItemInstance item, boolean isLawful) {
		String s = "";
		int spellId = 0;
		L1Skills l1skills = null;
		for (int skillId = 1; skillId < 81; skillId++) {
			l1skills = SkillsTable.getInstance().getTemplate(skillId);
			if (l1skills == null) {
				System.out.println(String.format("遺漏的技能 ID : %d", skillId));
				continue;
			}
			String s1 = "魔法書 (" + l1skills.getName() + ")";
			if (s1.endsWith("(刻印)")) {
				String s2 = s1.replaceAll("(刻印)", "");
				String s3 = s2.replaceAll(")", "");
				s1 = s3;

			}
			if (item.getItem().getName().startsWith(s1)) {
				s = l1skills.getName();
				spellId = l1skills.getSkillId();
			}
		}

		l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.ETERNITI);
		String s1 = "魔法書 (" + l1skills.getName() + ")";
		if (item.getItem().getName().startsWith(s1)) {
			s = l1skills.getName();
			spellId = l1skills.getSkillId();
		}

		l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.MATH_IMMUNE_TO_HARM);
		String s2 = "魔法書 (" + l1skills.getName() + ")";
		if (item.getItem().getName().startsWith(s2)) {
			s = l1skills.getName();
			spellId = l1skills.getSkillId();
		}

		l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.DEVINE_PROTECTION);
		String s3 = "魔法書 (" + l1skills.getName() + ")";
		if (item.getItem().getName().startsWith(s3)) {
			s = l1skills.getName();
			spellId = l1skills.getSkillId();
		}

		if (pc.isSkillMastery(spellId)) {
			pc.sendPackets(new S_SystemMessage("這是一個已經學會的魔法。"), true);
			return;
		}

		int objid = pc.getId();

		SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
		noti.appendNewSpell(spellId, true);
		pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		S_SkillSound s_skillSound = new S_SkillSound(objid, isLawful ? 224 : 231);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(objid, spellId, s, 0, 0);
		pc.getInventory().removeItem(item, 1);
	}

	private void SpellBook1(L1PcInstance pc, L1ItemInstance l1iteminstance, GameClient clientthread) {
		String s = "";
		int spellId = 0;
		L1Skills l1skills = null;
		for (int j6 = 97; j6 <= 241; j6++) {
			l1skills = SkillsTable.getInstance().getTemplate(j6);
			if (l1skills == null) {
				System.out.println(String.format("遺漏的技能 ID : %d", j6));
				continue;
			}
			String s1 = "黑精靈的水晶 (" + l1skills.getName() + ")";
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				s = l1skills.getName();
				spellId = l1skills.getSkillId();
				break;
			}
		}

		l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.AVENGER);
		String s1 = "黑精靈的水晶 (" + l1skills.getName() + ")";
		if (l1iteminstance.getItem().getName().startsWith(s1)) {
			s = l1skills.getName();
			spellId = l1skills.getSkillId();
		}

		if (pc.isSkillMastery(spellId)) {
			pc.sendPackets(new S_SystemMessage("這是一個已經學會的魔法。"), true);
			return;
		}
		int k6 = pc.getId();

		SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
		noti.appendNewSpell(spellId, true);
		pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		S_SkillSound s_skillSound = new S_SkillSound(k6, 231);
		pc.sendPackets(s_skillSound);
		Broadcaster.broadcastPacket(pc, s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, spellId, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	// 精靈技能
					private void SpellBook2(L1PcInstance pc, L1ItemInstance l1iteminstance) {

		String s = "";

		int spellId = 0;

		L1Skills l1skills = null;

		for (int j6 = 129; j6 <= 179; j6++) {

			l1skills = SkillsTable.getInstance().getTemplate(j6);

			if (l1skills == null) {

				System.out.println(String.format("遺漏的技能 ID : %d", j6));

				continue;

			}

			String s1 = "精靈的水晶 (" + l1skills.getName() + ")";
			if (l1iteminstance.getItem().getName().startsWith(s1)) {
				if (!pc.isGm() && l1skills.getAttr() != 0 && pc.getElfAttr() != l1skills.getAttr()
						&& pc.getGlory_Earth_Attr() != l1skills.getAttr()) {
					if (pc.getElfAttr() == 0 || pc.getElfAttr() == 1 || pc.getElfAttr() == 2 || pc.getElfAttr() == 4
							|| pc.getElfAttr() == 8) {
						pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
						return;
					}
				}
				s = l1skills.getName();
				spellId = l1skills.getSkillId();
			}
		}

		l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.LIBERATION);
		String s2 = "精靈的水晶 (" + l1skills.getName() + ")";
		if (l1iteminstance.getItem().getName().startsWith(s2)) {
			s = l1skills.getName();
			spellId = l1skills.getSkillId();
		}
		l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.BURNING_SHOT);
		String s3 = "精靈的水晶 (" + l1skills.getName() + ")";
		if (l1iteminstance.getItem().getName().startsWith(s3)) {
			s = l1skills.getName();
			spellId = l1skills.getSkillId();
		}
		l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.VISION_TELEPORT);
		String s4 = "精靈的水晶 (" + l1skills.getName() + ")";
		if (l1iteminstance.getItem().getName().startsWith(s4)) {
			s = l1skills.getName();
			spellId = l1skills.getSkillId();
		}

		if (pc.isSkillMastery(spellId)) {
			pc.sendPackets(new S_SystemMessage("這是一個已經學會的魔法。"), true);
			return;
		}
		int k6 = pc.getId();
		SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
		noti.appendNewSpell(spellId, true);
		pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, spellId, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}
		// 騎士技能
		private void SpellBook3(L1PcInstance pc, L1ItemInstance l1iteminstance, GameClient clientthread) {
			String s = "";
			int spellId = 0;
			L1Skills l1skills = null;
			for (int j6 = 87; j6 <= 96; j6++) {
				l1skills = SkillsTable.getInstance().getTemplate(j6);
				if (l1skills == null) {
					System.out.println(String.format("遺漏的技能 ID : %d", j6));
					continue;
				}
				String s1 = (new StringBuilder()).append("技能書 (").append(l1skills.getName()).append(")").toString();
				if (l1iteminstance.getItem().getName().startsWith(s1)) {
					s = l1skills.getName();
					spellId = l1skills.getSkillId();
					break;
				}
						}

		}
		int k6 = pc.getId();
		SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
		noti.appendNewSpell(spellId, true);
		pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, spellId, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

        // 君主技能
        private void SpellBook4(L1PcInstance pc, L1ItemInstance l1iteminstance, GameClient clientthread) {
            String s = "";
            int spellId = 0;
            L1Skills l1skills = null;
            for (int j6 = 113; j6 <= 123; j6++) {
                l1skills = SkillsTable.getInstance().getTemplate(j6);
                if (l1skills == null) {
                    System.out.println(String.format("遺漏的技能 ID : %d", j6));
                    continue;
                }
                String s1 = "魔法書 (" + l1skills.getName() + ")";
                if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
                    s = l1skills.getName();
                    spellId = l1skills.getSkillId();
                    break;
                }
            }

            l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.PRIME);
            String s1 = "魔法書 (" + l1skills.getName() + ")";
            if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
                s = l1skills.getName();
                spellId = l1skills.getSkillId();
            }
            l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.CALL_CLAN_ADVENCE);
            String s2 = "魔法書 (" + l1skills.getName() + ")";
            if (l1iteminstance.getItem().getName().equalsIgnoreCase(s2)) {
                s = l1skills.getName();
                spellId = l1skills.getSkillId();
            }
            l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.TYRANT);
            String s3 = "魔法書 (" + l1skills.getName() + ")";
            if (l1iteminstance.getItem().getName().equalsIgnoreCase(s3)) {
                s = l1skills.getName();
                spellId = l1skills.getSkillId();
            }

            l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.BRAVE_UNION);
            String s4 = "魔法書 (" + l1skills.getName() + ")";
            if (l1iteminstance.getItem().getName().equalsIgnoreCase(s4)) {
                s = l1skills.getName();
                spellId = l1skills.getSkillId();
                    }

		if (pc.isSkillMastery(spellId)) {
            pc.sendPackets(new S_SystemMessage("這是一個已經學會的魔法。"), true);
			return;
		}
		int k6 = pc.getId();
		SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
		noti.appendNewSpell(spellId, true);
		pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, spellId, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook5(L1PcInstance pc, L1ItemInstance l1iteminstance, GameClient clientthread) {
		String s = "";
		int spellId = 0;
		L1Skills l1skills = null;
		for (int j6 = 181; j6 < 200; j6++) {
			l1skills = SkillsTable.getInstance().getTemplate(j6);
			if (l1skills == null) {
                System.out.println(String.format("遺漏的技能 ID : %d", j6));
				continue;
			}
            String s1 = "龍騎士的石板 (" + l1skills.getName() + ")";
			if (l1iteminstance.getItem().getName().startsWith(s1)) {
				s = l1skills.getName();
				spellId = l1skills.getSkillId();
				break;
			}
		}

		l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.HALPAS);
		String s1 = "龍騎士的石板 (" + l1skills.getName() + ")";
		if (l1iteminstance.getItem().getName().startsWith(s1)) {
			s = l1skills.getName();
			spellId = l1skills.getSkillId();
		}

		l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.BEHEMOTH);
		String s2 = "龍騎士的石板 (" + l1skills.getName() + ")";
		if (l1iteminstance.getItem().getName().startsWith(s2)) {
			s = l1skills.getName();
			spellId = l1skills.getSkillId();
		}

		l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.CHAIN_REACTION);
		String s3 = "龍騎士的石板 (" + l1skills.getName() + ")";
		if (l1iteminstance.getItem().getName().startsWith(s3)) {
			s = l1skills.getName();
			spellId = l1skills.getSkillId();
		}

		if (pc.isSkillMastery(spellId)) {
            pc.sendPackets(new S_SystemMessage("這個魔法已經學過了。"), true);
			return;
		}
		int k6 = pc.getId();
		SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
		noti.appendNewSpell(spellId, true);
		pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, spellId, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook6(L1PcInstance pc, L1ItemInstance l1iteminstance, GameClient clientthread) {
		String s = "";
		int spellId = 0;
		for (int j6 = 201; j6 <= 224; j6++) {
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(j6);
			if (l1skills == null) {
                System.out.println(String.format("遺漏的技能 ID : %d", j6));
                continue;
            }
            String s1 = "記憶水晶 (" + l1skills.getName() + ")";
			if (l1iteminstance.getItem().getName().startsWith(s1)) {
				s = l1skills.getName();
				spellId = l1skills.getSkillId();
				break;
			}
		}

        L1Skills l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.POTENTIAL);
        String s1 = "記憶水晶 (" + l1skills.getName() + ")";
        if (l1iteminstance.getItem().getName().startsWith(s1)) {
            s = l1skills.getName();
            spellId = l1skills.getSkillId();
        }

        l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.ENSNARE);
        String s2 = "記憶水晶 (" + l1skills.getName() + ")";
        if (l1iteminstance.getItem().getName().startsWith(s2)) {
            s = l1skills.getName();
            spellId = l1skills.getSkillId();
        }
        l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.OSIRIS);
        String s3 = "記憶水晶 (" + l1skills.getName() + ")";
        if (l1iteminstance.getItem().getName().startsWith(s3)) {
            s = l1skills.getName();
            spellId = l1skills.getSkillId();
        }

        if (pc.isSkillMastery(spellId)) {
            pc.sendPackets(new S_SystemMessage("這個魔法已經學過了。"), true);
            return;
        }
		int k6 = pc.getId();
		SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
		noti.appendNewSpell(spellId, true);
		pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, spellId, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook7(L1PcInstance pc, L1ItemInstance l1iteminstance) {
		String s = "";
		int spellId = 0;
		L1Skills l1skills = null;
		for (int j6 = 5001; j6 <= 5005; j6++) {
			l1skills = SkillsTable.getInstance().getTemplate(j6);
			if (l1skills == null) {
                System.out.println(String.format("遺漏的技能 ID（劍士）: %d", j6));
                continue;
            }
            String s1 = "劍士之書 (" + l1skills.getName() + ")";
			if (l1iteminstance.getItem().getName().startsWith(s1)) {
				s = l1skills.getName();
				spellId = l1skills.getSkillId();
				break;
			}
		}

		l1skills = SkillsTable.getInstance().getTemplate(L1SkillId.ASURA);
		String s1 = "劍士之書 (" + l1skills.getName() + ")";
		if (l1iteminstance.getItem().getName().startsWith(s1)) {
			s = l1skills.getName();
			spellId = l1skills.getSkillId();
		}

		if (pc.isSkillMastery(spellId)) {
            pc.sendPackets(new S_SystemMessage("這個魔法已經學過了。"), true);
			return;
		}
		int k6 = pc.getId();
		SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
		noti.appendNewSpell(spellId, true);
		pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);

		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, spellId, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook8(L1PcInstance pc, L1ItemInstance item, boolean ispassibe) {
		L1Skills skill = SkillsTable.getInstance().getTemplateByItem(item.getItemId());

		if (skill != null) {
			int skillId = skill.getSkillId();
			int objid = pc.getId();

			if (pc.isSkillMastery(skillId)) {
                pc.sendPackets(new S_SystemMessage("已經學過的魔法。"), true);
				return;
			}

			SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
			noti.appendNewSpell(skillId, true);
			pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
			S_SkillSound s_skillSound = new S_SkillSound(objid, 224);
			pc.sendPackets(s_skillSound);
			Broadcaster.broadcastPacket(pc, s_skillSound);
			SkillsTable.getInstance().spellMastery(objid, skillId, skill.getName(), 0, 0);
		}
		pc.getInventory().removeItem(item, 1);
	}

	private void polyAction(L1PcInstance attacker, L1Character cha, int itemId, String s) {
		boolean isSameClan = false;

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getClanid() != 0 && attacker.getClanid() == pc.getClanid()) {
				isSameClan = true;
			}

			if (attacker.getId() != pc.getId() && (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER)
					|| pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2))) {
                // 當對象變身為變身支配戒指時
				pc.sendPackets(new S_SkillSound(pc.getId(), 15846));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), 15846));
				attacker.sendPackets(new S_ServerMessage(280));
				return;
			}
		}
		if (cha instanceof L1MonsterInstance) {
			return;
		}
		if (attacker.getId() != cha.getId() && !isSameClan) {
			int probability = 3 * (attacker.getLevel() - cha.getLevel()) - cha.getResistance().getEffectedMrBySkill();
			int rnd = _random.nextInt(100) + 1;
			if (rnd > probability) {
				attacker.sendPackets(new S_ServerMessage(280));
				return;
			}
		}

		int[] polyArray = { 15552, 979, 1037, 1039, 15715, 3861, 3862, 3864, 3865, 3866, 3871, 15600, 15739, 15740,
				16145, 14094, 15553, 15743, 3881, 3882, 3883, 3884, 3885, 15719, 15554, 15575, 30, 2376, 2377, 2378,
				11358, 11396, 11397, 12225, 11399, 11398 }; // 楓葉棒

		int pid = _random.nextInt(polyArray.length);
		int polyId = polyArray[pid];
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getInventory().checkEquipped(20281)
					|| (!pc._PolyMasterCheck && (pc.isPolyRingMaster() || pc.isPolyRingMaster2()))) {
				if (usePolyScroll(pc, itemId, s)) {
				} else {
					pc.sendPackets(new S_ServerMessage(181));
				}
			} else {
				L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SHAPE_CHANGE);
				L1PolyMorph.doPoly(pc, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC, false,
						false);
				if (attacker.getId() != pc.getId()) {
					pc.sendPackets(new S_ServerMessage(241, attacker.getName()));
				}
			}
		} else if (cha instanceof L1MonsterInstance) {
			L1MonsterInstance mob = (L1MonsterInstance) cha;
			if (mob.getLevel() < 50) {
				int npcId = mob.getNpcTemplate().get_npcId();
				if (npcId != 45338 && npcId != 45370 && npcId != 45456 && npcId != 45464 && npcId != 45473
						&& npcId != 45488 && npcId != 45497 && npcId != 45516 && npcId != 45529 && npcId != 45458) {
					L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SHAPE_CHANGE);
					L1PolyMorph.doPoly(mob, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC, false,
							false);
				}
			}
		}
	}

	private static void cancelAbsoluteBarrier(L1PcInstance pc) { // 解除吸收屏障
		if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			pc.removeSkillEffect(ABSOLUTE_BARRIER);
		}
	}

	private void useToiTeleportAmulet(L1PcInstance pc, int itemId, L1ItemInstance item) {
		boolean isTeleport = false;
                    /** 傲慢護身符可以在除回歸地區外的任何地方使用 **/
		if (itemId >= 40289 && itemId <= 40297) {
			if (pc.getMap().isEscapable()) {
				isTeleport = true;
			}
		}

		if (isTeleport) {
			pc.start_teleport(item.getItem().get_locx(), item.getItem().get_locy(), item.getItem().get_mapid(), 5,
					18339, true, false);
		} else {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
			// \f1 什麼事都沒有發生.
		}
	}

	private boolean withdrawPet(L1PcInstance pc, int itemObjectId) {
		if (!pc.getMap().isTakePets()) {
            pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(563))); // 1 這裡不能使用。
            return false;
        }

        if (pc.getMapId() == 781 || pc.getMapId() == 782) {
            pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(563))); // 1 這裡不能使用。
            return false;
        }

        if (pc.isInWarArea()) {
            pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(563))); // 1 這裡不能使用。
            return false;
        }

		int petCost = 0;
		Object[] petList = pc.getPetList().values().toArray();
		for (Object pet : petList) {
			if (pet instanceof L1PetInstance) {
				if (((L1PetInstance) pet).getItemObjId() == itemObjectId) {
					return false;
				}
			}

			if (pet instanceof L1PetInstance) {
				petCost += ((L1NpcInstance) pet).getPetcost();
			}
		}
		int charisma = pc.getAbility().getTotalCha();
		if (pc.isCrown()) { // CROWN
			charisma += 6;
		} else if (pc.isElf()) { // ELF
			charisma += 12;
		} else if (pc.isWizard()) { // WIZ
			charisma += 6;
		} else if (pc.isDarkelf()) { // DE
			charisma += 6;
        } else if (pc.isDragonknight()) { // 龍騎士
            charisma += 6;
        } else if (pc.isBlackwizard()) { // 幻術師
			charisma += 6;
		}

		charisma -= petCost;
		int petCount = charisma / 6;
		if (petCount <= 0) {
            pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(489))); // 想要離開的寵物太多了。
			return false;
		}

		L1Pet l1pet = PetTable.getInstance().getTemplate(itemObjectId);
		if (l1pet != null) {
			L1Npc npcTemp = NpcTable.getInstance().getTemplate(l1pet.get_npcid());
			L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
			pet.setPetcost(6);
		}
		return true;
	}

	private void useResolvent(L1PcInstance pc, L1ItemInstance item, L1ItemInstance resolvent) {
		if (item == null || resolvent == null) {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79))); // \f1 什麼事都沒有發生.
			return;
		}
        if (item.getItem().getType2() == 1 || item.getItem().getType2() == 2) { // 武器·防具
            // 裝備
            if (item.getEnchantLevel() != 0) { // 強化完成的狀態
                pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1161))); // 無法溶解。
                return;
            }
            if (item.isEquipped()) { // 裝備中
                pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1161))); // 無法溶解。
                return;
            }
            if (item.getBless() >= 128) { // 密封中
                pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1161))); // 無法溶解。
                return;
            }
        }
		int crystalCount = ResolventTable.getInstance().getCrystalCount(item.getItem().getItemId());
		if (crystalCount == 0) {
            pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1161))); // 無法溶解。
            return;
        }

        int rnd = _random.nextInt(100) + 1;
        if (rnd >= 1 && rnd <= 20) {
            crystalCount = 0;
            pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(158, item.getName()))); // 1%0已經消失了。
		} else if (rnd >= 21 && rnd <= 90) {
			crystalCount *= 1;
		} else if (rnd >= 91 && rnd <= 100) {
			crystalCount *= 1.5;
			pc.getInventory().storeItem(41246, (int) (crystalCount * 1.5));
		}
		if (crystalCount != 0) {
			L1ItemInstance crystal = ItemTable.getInstance().createItem(41246);
			crystal.setCount(crystalCount);
			if (pc.getInventory().checkAddItem(crystal, 1) == L1Inventory.OK) {
				pc.getInventory().storeItem(crystal);
                pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(403, crystal.getLogName()))); // %0了
            } else { // 無法擁有的情況下，不會取消丟到地面的處理（防止作弊）
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(crystal);
			}
		}
		pc.getInventory().removeItem(item, 1);
		pc.getInventory().removeItem(resolvent, 1);
	}

	private void useFurnitureItem(L1PcInstance pc, int itemId, int itemObjectId) {
		if (!L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId())) {
            pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(563))); // 1 這裡不能使用。
			return;
		}

		boolean isAppear = true;
		L1FurnitureInstance furniture = null;
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object instanceof L1FurnitureInstance) {
				furniture = (L1FurnitureInstance) l1object;
                if (furniture.getItemObjId() == itemObjectId) { // 已經取出的家具
					isAppear = false;
					break;
				}
			}
		}

		if (isAppear) {
			if (pc.getHeading() != 0 && pc.getHeading() != 2) {
				return;
			}
			int npcId = 0;
			switch (itemId) {
				case 41383:
					npcId = 80109;
					break;
				case 41384:
					npcId = 80110;
					break;
				case 41385:
					npcId = 80113;
					break;
				case 41386:
					npcId = 80114;
					break;
				case 41387:
					npcId = 80115;
					break;
				case 41388:
					npcId = 80124;
					break;
				case 41389:
					npcId = 80118;
					break;
				case 41390:
					npcId = 80118;
					break;
				case 41391:
					npcId = 80120;
					break;
				case 41392:
					npcId = 80121;
					break;
				case 41393:
					npcId = 80126;
					break;
				case 41394:
					npcId = 80125;
					break;
				case 41395:
					npcId = 80111;
					break;
				case 41396:
					npcId = 80112;
					break;
				case 41397:
					npcId = 80116;
					break;
				case 41398:
					npcId = 80117;
					break;
				case 41399:
					npcId = 80122;
					break;
				case 41400:
					npcId = 80123;
					break;
			}

			try {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);
				if (l1npc != null) {
					try {
						String s = l1npc.getImpl();
						Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance")
								.getConstructors()[0];
						Object aobj[] = { l1npc };
						furniture = (L1FurnitureInstance) constructor.newInstance(aobj);
						furniture.setId(IdFactory.getInstance().nextId());
						furniture.setMap(pc.getMapId());
						if (pc.getHeading() == 0) {
							furniture.setX(pc.getX());
							furniture.setY(pc.getY() - 1);
						} else if (pc.getHeading() == 2) {
							furniture.setX(pc.getX() + 1);
							furniture.setY(pc.getY());
						}
						furniture.setHomeX(furniture.getX());
						furniture.setHomeY(furniture.getY());
						furniture.setHeading(0);
						furniture.setItemObjId(itemObjectId);

						L1World.getInstance().storeObject(furniture);
						L1World.getInstance().addVisibleObject(furniture);
						FurnitureSpawnTable.getInstance().insertFurniture(furniture);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception exception) {
			}
		} else {
			furniture.deleteMe();
			FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
		}
	}

	private void useFieldObjectRemovalWand(L1PcInstance pc, int targetId, L1ItemInstance item) {
		S_AttackPacket s_attackStatus = new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand);
		pc.sendPackets(s_attackStatus);
		Broadcaster.broadcastPacket(pc, s_attackStatus);
		L1Object target = L1World.getInstance().findObject(targetId);
		if (target != null && target instanceof L1NpcInstance && !(target instanceof L1DollInstance)) {
			L1NpcInstance npc = (L1NpcInstance) target;
			NpcSpawnTable.getInstance().removeSpawn(npc);
			npc.setRespawn(false);
			new L1NpcDeleteTimer(npc, 1 * 1).begin();
            pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("GM: 該NPC已被刪除。")));
			pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
		}
	}
     private void EinhasadBlessing(L1PcInstance pc, int itemId, L1ItemInstance useItem) {
		long sysTime = System.currentTimeMillis();
		Timestamp deleteTime1 = null;
		Timestamp deleteTime2 = null;
		Timestamp deleteTime3 = null;
        deleteTime1 = new Timestamp(sysTime + (86400000 * (long) 3) + 10000); // 3天
        deleteTime2 = new Timestamp(sysTime + (86400000 * (long) 7) + 10000); // 7天
        deleteTime3 = new Timestamp(sysTime + (86400000 * (long) 30) + 10000); // 30天

        try {
            if (pc.hasSkillEffect(L1SkillId.EINHASAD_GRACE)) {
                pc.sendPackets(new S_ServerMessage(7030)); // 已經受到了艾因哈薩德的祝福效果。
                return;
            }

			if (itemId == 34103) {
				pc.setSkillEffect(L1SkillId.EINHASAD_GRACE, (long) 86400000 * 3);
				pc.setEinhasadGraceTime(deleteTime1);
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.EINHASAD, pc));
                pc.sendPackets(new S_ACTION_UI2("艾因加護", (long) 86400000 * 3), true);
            } else if (itemId == 34104) {
                pc.setSkillEffect(L1SkillId.EINHASAD_GRACE, (long) 86400000 * 7);
                pc.setEinhasadGraceTime(deleteTime2);
                pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.EINHASAD, pc));
                pc.sendPackets(new S_ACTION_UI2("艾因加護", (long) 86400000 * 7), true);
            } else if (itemId == 34105) {
                pc.setSkillEffect(L1SkillId.EINHASAD_GRACE, (long) 86400000 * 30);
                pc.setEinhasadGraceTime(deleteTime3);
                pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.EINHASAD, pc));
                pc.sendPackets(new S_ACTION_UI2("艾因加護", (long) 86400000 * 30), true);
            }
			pc.getInventory().removeItem(useItem, 1);
			SC_EXP_BOOSTING_INFO_NOTI.send(pc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void useFieldObjectRemovalWand1(L1PcInstance pc, int targetId, L1ItemInstance item) {
		S_AttackPacket s_attackStatus = new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand);
		pc.sendPackets(s_attackStatus);
		Broadcaster.broadcastPacket(pc, s_attackStatus);
		L1Object target = L1World.getInstance().findObject(targetId);
		if (target != null && target instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) target;
            pc.sendPackets(java.lang.String.valueOf(new S_ChatPacket(java.lang.String.valueOf(pc), "編號 : (" + npc.getNpcId() + "-" + targetId + ") / " + "名稱 : (" + npc.getName() + ")")));
            pc.sendPackets(new S_ChatPacket(pc, "位置 : " + "(x:" + npc.getX() + "), (y:" + npc.getY() + "), (" + "地圖:"
                    + npc.getMapId() + ") // GfxId : (" + npc.getCurrentSpriteId() + ")"));
            pc.sendPackets(java.lang.String.valueOf(new S_ChatPacket(java.lang.String.valueOf(pc), "當前 HP : (" + npc.getCurrentHp() + "/" + npc.getMaxHp() + ")")));
			pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
		}
	}

	private void useFurnitureRemovalWand(L1PcInstance pc, int targetId, L1ItemInstance item) {
		S_AttackStatus s_attackStatus = new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand);
		pc.sendPackets(s_attackStatus);
		pc.broadcastPacket(s_attackStatus);
		int chargeCount = item.getChargeCount();
		if (chargeCount <= 0) {
			return;
		}

		L1Object target = L1World.getInstance().findObject(targetId);
		if (target != null && target instanceof L1FurnitureInstance) {
			L1FurnitureInstance furniture = (L1FurnitureInstance) target;
			furniture.deleteMe();
			FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
			item.setChargeCount(item.getChargeCount() - 1);
			if (item.getChargeCount() == 0) {
				pc.getInventory().removeItem(item);
			} else {
				pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
			}
		}
	}

	private void useLuunSpawn(L1PcInstance pc, int targetId, L1ItemInstance item) {
//		System.out.println("사용!!");
		S_AttackStatus s_attackStatus = new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand);
		pc.sendPackets(s_attackStatus);
		pc.broadcastPacket(s_attackStatus);
		int _mapnum;

		if (Luun_Secret_System.getInstance().countLuun_Secret() >= 50) {
            pc.sendPackets(java.lang.String.valueOf(new S_ChatPacket(java.lang.String.valueOf(pc), "進入副本的玩家數量過多")));
			return;
		}

		_mapnum = Luun_Secret_System.getInstance().blankMapId();
		if (_mapnum != 815) {
			L1WorldMap.getInstance().cloneMap(815, _mapnum);
		}
		Luun_Secret_System.getInstance().Luun_SecretStart(pc, _mapnum);
//		System.out.println(_mapnum);
		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(targetId);
			String s = l1npc.getImpl();
			Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance")
					.getConstructors()[0];
			Object aobj[] = { l1npc };
			L1NpcInstance npc = (L1NpcInstance) constructor.newInstance(aobj);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());
			npc.setX(pc.getX());
			npc.setY(pc.getY());
			npc.setHomeX(pc.getX());
			npc.setHomeY(pc.getY());
			npc.setMap(pc.getMapId());
			npc.setHeading(2);
			npc.set_Mapnum(_mapnum);
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, 3 * 60 * 1000); // 3分鐘後刪除處理
			timer.begin();
		} catch (Exception e) {
			e.printStackTrace();
		}
		pc.getInventory().consumeItem(item, 1);
	}

	private void useNpcSpownWand(L1PcInstance pc, int targetId, L1ItemInstance item) {
		S_AttackStatus s_attackStatus = new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand);
		pc.sendPackets(s_attackStatus);
		pc.broadcastPacket(s_attackStatus);
		int chargeCount = item.getChargeCount();
		if (chargeCount <= 0) {
			return;
		}

		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(targetId);
			String s = l1npc.getImpl();
			Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance")
					.getConstructors()[0];
			Object aobj[] = { l1npc };
			L1NpcInstance npc = (L1NpcInstance) constructor.newInstance(aobj);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());
			npc.setX(pc.getX());
			npc.setY(pc.getY());
			npc.setHomeX(pc.getX());
			npc.setHomeY(pc.getY());
			npc.setMap(pc.getMapId());
			npc.setHeading(2);
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
            // 60秒後立即刪除處理
			L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, 60000);
			timer.begin();
		} catch (Exception e) {
			e.printStackTrace();
		}
		item.setChargeCount(item.getChargeCount() - 1);
		if (item.getChargeCount() == 0) {
			pc.getInventory().removeItem(item);
		} else {
			pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
		}
	}

    public static void MonarchAreaStun(L1PcInstance pc) {
		if (pc.isCrown() || pc.isGm()) {
            if (pc.hasSkillEffect(L1SkillId.DELAY)) { // 延遲
                pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("現在還不能使用範圍暈眩技能。")));
                return;
            }
            if (pc.isInvisble()) {
                pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("範圍暈眩無法在隱身狀態下施放。")));
                return;
            }
            if (pc.getMapId() == 800) {
                pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("範圍暈眩無法在市場中施放。")));
                return;
            }
            if (pc.getZoneType() == 1) {
                pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("範圍暈眩無法在村莊中施放。")));
                return;
            }
            if (pc.getCurrentMp() < 30) {
                pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(278))); // 1因為MP不足，無法使用魔法。
                return;
            }
			pc.setCurrentMp(pc.getCurrentMp() - 30);
            pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("施放範圍暈眩。")));
			pc.setSkillEffect(L1SkillId.DELAY, 10 * 1000);
			pc.sendPackets(new S_SkillIconGFX(74, 3).toString());

			int actionId = ActionCodes.ACTION_SkillBuff;
			S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), actionId);
			pc.sendPackets(gfx);
			Broadcaster.broadcastPacket(pc, gfx);

			for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 10)) {
				Random random = new Random();
				int[] stunTimeArray = { 2000, 2500, 3000, 3500, 4000 };
				int rnd = random.nextInt(stunTimeArray.length);
				int probability = random.nextInt(100) + 1;

				if (probability < 50) {
					int _shockStunDuration = stunTimeArray[rnd];
					if (obj instanceof L1PcInstance) {
						L1PcInstance target = (L1PcInstance) obj;
						L1PinkName.onAction(target, pc);
						if ((pc.getClanid() > 0 && (pc.getClanid() == target.getClanid())) || target.isGm()) {
						} else {
							L1Character cha = (L1Character) obj;

							if (!cha.hasSkillEffect(SHOCK_STUN) && !cha.hasSkillEffect(L1SkillId.EMPIRE)
									&& !cha.hasSkillEffect(EARTH_BIND) && !cha.hasSkillEffect(ICE_LANCE)) {
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, target.getX(),
										target.getY(), target.getMapId());
								target.sendPackets(java.lang.String.valueOf(new S_Paralysis(S_Paralysis.TYPE_STUN, true)));
								target.setSkillEffect(SHOCK_STUN, _shockStunDuration);
								target.sendPackets(java.lang.String.valueOf(new S_SkillSound(target.getId(), 15101))); // 暈眩
								Broadcaster.broadcastPacket(target, new S_SkillSound(target.getId(), 15101));
							}
						}
					} else if (obj instanceof L1MonsterInstance || obj instanceof L1SummonInstance
							|| obj instanceof L1PetInstance) {
						L1NpcInstance targetnpc = (L1NpcInstance) obj;
						L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, targetnpc.getX(),
								targetnpc.getY(), targetnpc.getMapId());
						targetnpc.setParalyzed(true);
						targetnpc.setSkillEffect(SHOCK_STUN, _shockStunDuration);
						Broadcaster.broadcastPacket(targetnpc, new S_SkillSound(obj.getId(), 15101));
					}
				}
			}
		} else {
            pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("該技能只能由王族施放。")));
		}
		System.currentTimeMillis();
		return;
	}

    private void PCBangCoin(L1PcInstance pc, int itemId, L1ItemInstance useItem, int day) {
		long sysTime = System.currentTimeMillis();
		Timestamp deleteTime = new Timestamp(sysTime + (86400000 * (long) day) + 10000);
		// Timestamp deleteTime = new Timestamp(sysTime + 60000);
		try {
            if (pc.getAccount().getBuff_PCbang() != null) {
                if (sysTime <= pc.getAccount().getBuff_PCbang().getTime()) {
					// if (pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
                    pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("已經在使用網咖Buff商品。")));
					return;
				}
			}

            pc.getAccount().setBuff_PCBang(deleteTime);
            pc.getAccount().updatePCBang();
			L1SkillId.onPcCafeBuff(pc, deleteTime.getTime() - System.currentTimeMillis());
			pc.getInventory().removeItem(useItem, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private void EnhanceBuff(L1PcInstance pc, int itemId, L1ItemInstance useItem) {
		if (itemId >= 600198 && itemId <= 600217) {
			int buffId = itemId - 596123;
			int time = 86400000 * Config.ServerAdSetting.TimeNBuff;

			if (pc.hasSkillEffect(buffId)) {
                pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("已經在使用中的強化Buff。")));
				return;
			}

			ArrayList<Integer> skillIdList = new ArrayList<Integer>();
			for (int skillId = 4075; skillId <= 4094; skillId++) {
				if (pc.hasSkillEffect(skillId)) {
					skillIdList.add(Integer.valueOf(skillId));
				}
			}

			if (skillIdList.size() >= Config.ServerAdSetting.LimitNBuff) {
                pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("強化Buff最多可以接受" + Config.ServerAdSetting.LimitNBuff + "種。")));
				return;
			}

			new L1SkillUse().handleCommands(pc, buffId, pc.getId(), pc.getX(), pc.getY(), null, time / 1000,
					L1SkillUse.TYPE_GMBUFF);
			Updator.exec(
					"insert into character_tams set "
							+ "account_id=?, char_id=?, char_name=?, skill_id=?, expiration_time=?, reserve=? "
							+ "on duplicate key update " + "account_id=?, char_name=?, expiration_time=?",
					new Handler() {
						@Override
						public void handle(PreparedStatement pstm) throws Exception {
							int idx = 0;
							Timestamp ts = new Timestamp(System.currentTimeMillis() + time);
							pstm.setInt(++idx, pc.getAccount().getAccountId());
							pstm.setInt(++idx, pc.getId());
							pstm.setString(++idx, pc.getName());
							pstm.setInt(++idx, buffId);
							pstm.setTimestamp(++idx, ts);
							pstm.setInt(++idx, 0);
							pstm.setInt(++idx, pc.getAccount().getAccountId());
							pstm.setString(++idx, pc.getName());
							pstm.setTimestamp(++idx, ts);
						}
					});
			pc.getInventory().removeItem(useItem, 1);
		} else if (itemId == 600218) {
			for (int skillId = 4075; skillId <= 4094; skillId++) {
				if (pc.hasSkillEffect(skillId)) {
					pc.removeSkillEffect(skillId);
				}
			}
			if (Config.ServerAdSetting.AccountNBuff) {
				SQLUtil.execute("DELETE FROM character_tams WHERE account_id=? AND skill_id BETWEEN ? AND ?",
						new Object[] { Integer.valueOf(pc.getAccount().getAccountId()), Integer.valueOf(4075),
								Integer.valueOf(4094) });
			} else {
				SQLUtil.execute("DELETE FROM character_tams WHERE char_id=? AND skill_id BETWEEN ? AND ?",
						new Object[] { Integer.valueOf(pc.getId()), Integer.valueOf(4075), Integer.valueOf(4094) });
			}
            pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("所有N商店Buff已經重置。")));
			pc.getInventory().removeItem(useItem, 1);
		}
	}

	public void tamadd(String _name, int objectId, int _day, String _encobjid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO Tam SET objid=?, Name=?, Day=? , encobjid=?");
			pstm.setInt(1, objectId);
			pstm.setString(2, _name);
			pstm.setInt(3, _day);
			pstm.setString(4, _encobjid);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void tamupdate(int objectId, Timestamp date) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET TamEndTime=? WHERE objid=?");
			pstm.setTimestamp(1, date);
			pstm.setInt(2, objectId);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c,
			0x8d, 0x8e, 0x8f, 0x90, 0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b, 0x9c, 0x9d, 0x9e,
			0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6, 0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0,
			0xb1, 0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc, 0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2,
			0xc3, 0xc4, 0xc5, 0xc6, 0xc7, 0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2, 0xd3, 0xd4,
			0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdd, 0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6,
			0xe7, 0xe8, 0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2, 0xf3, 0xf4, 0xf5, 0xf6, 0xf7, 0xf8,
			0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe, 0xff };

	private String byteWrite(long value) {
		long temp = value / 128;
		StringBuffer sb = new StringBuffer();
		if (temp > 0) {
			sb.append((byte) hextable[(int) value % 128]);
			while (temp >= 128) {
				sb.append((byte) hextable[(int) temp % 128]);
				temp = temp / 128;
			}
			if (temp > 0)
				sb.append((int) temp);
		} else {
			if (value == 0) {
				sb.append(0);
			} else {
				sb.append((byte) hextable[(int) value]);
				sb.append(0);
			}
		}
		return sb.toString();
	}

    private void GreedFruit(L1PcInstance pc, int _objid, L1ItemInstance item, int day) {
		try {
			Timestamp tamtime = null;
			long time = 0;
			long sysTime = System.currentTimeMillis();
			String _Name = null;
			int tamcount = pc.tamcount();

			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT TamEndTime, char_name FROM characters WHERE objid=?");
				pstm.setInt(1, _objid);
				rs = pstm.executeQuery();
				while (rs.next()) {
					_Name = rs.getString("char_name");
					tamtime = rs.getTimestamp("TamEndTime");
					if (tamtime != null) {
						if (sysTime < tamtime.getTime()) {
							time = tamtime.getTime() - sysTime;
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}

			if (time != 0) {
				tamadd(_Name, _objid, day, byteWrite(_objid));
				pc.sendPackets(java.lang.String.valueOf(new S_TamWindow(pc.getAccountName())));
                pc.sendPackets(new S_SystemMessage("[" + _Name + "] 已經有正在使用的商品，因此已被預訂。"), true);
                pc.getInventory().removeItem(item, 1);
                return;
            } else if (tamcount >= 5) {// 這裡應該檢查每個帳戶是否已經使用了5個
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(3904)));
				return;
			}
			if (tamcount + 1 >= 1) {
				CharacterFreeShieldTable.getInstance().getTam(pc, tamcount + 1);
			}

			Timestamp deleteTime = null;
			deleteTime = new Timestamp(sysTime + (86400000 * (long) day) + 10000);// 7day
			// deleteTime = new Timestamp(sysTime + 1000*60);//7day

			if (pc.getId() == _objid) {
				pc.setTamTime(deleteTime);
				try {
					pc.save();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				tamupdate(_objid, deleteTime);
			}

			pc.sendPackets(new S_TamWindow(pc.getAccountName()));
			if (pc.hasSkillEffect(L1SkillId.Tam_Fruit1)) {
				pc.removeSkillEffect(L1SkillId.Tam_Fruit1);
			} else if (pc.hasSkillEffect(L1SkillId.Tam_Fruit2)) {
				pc.removeSkillEffect(L1SkillId.Tam_Fruit2);
			} else if (pc.hasSkillEffect(L1SkillId.Tam_Fruit3)) {
				pc.removeSkillEffect(L1SkillId.Tam_Fruit3);
			} else if (pc.hasSkillEffect(L1SkillId.Tam_Fruit4)) {
				pc.removeSkillEffect(L1SkillId.Tam_Fruit4);
			} else if (pc.hasSkillEffect(L1SkillId.Tam_Fruit5)) {
				pc.removeSkillEffect(L1SkillId.Tam_Fruit5);
			} else {
				L1SkillId.recycleTam(pc);
			}
			pc.sendPackets(new S_ServerMessage(3916));
			pc.sendPackets(new S_SkillSound(pc.getId(), 2028), true);
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 2028), true);
			pc.getInventory().removeItem(item, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** TAM **/
	private FastMap<Integer, L1PcInstance> charlist = new FastMap<Integer, L1PcInstance>();

	public FastMap<Integer, L1PcInstance> getCharList() {
		return charlist;
	}

	public void addCharList(int id, L1PcInstance pc) {
		charlist.put(id, pc);
	}

	public void deleteCharList(int id) {
		charlist.remove(id);
	}

	@Override
	public String getType() {
		return C_ITEM_USE;
	}

    // TODO 每次強化增加2攻擊力的武器（執行武器）（適用於5級屬性）
	public static boolean is_legend_weapon(int itemId) {
		switch (itemId) {
			case 12:
			case 61:
			case 66:
			case 86:
			case 134:
			case 202011:
			case 202012:
			case 202013:
			case 202014:
			case 7000238:
			case 7000258:
				return true;
		}

		return false;
	}

                // TODO 每次強化增加2攻擊力的武器（執行武器）（適用於5級屬性）
                public static boolean is_ancient_weapon(int itemId) {
                    switch (itemId) {
                        case 7000239: // 盈海薩德的閃光
                        case 7000240: // 格蘭肯的審判
                        case 203041: // 伊娃的誓約
                        case 203042: // 瑪弗爾的苦痛
                        case 7000263: // 賽哈的執念
                        case 203065: // 格蘭肯的恐懼
                        case 7000264: // 但丁的試煉
                        case 7000265: // 特亞的混亂
                        case 7000262: // 塞倫的決心
                        case 7000267: // 奧拉基亞的超越
                            return true;
                    }
                    return false;
                }

	public static void use_einhasad_potion(L1PcInstance pc, L1ItemInstance use_item, int charge_point) {
		if (pc.hasSkillEffect(L1SkillId.EMERALD_YES)) {
			pc.sendPackets(new S_ServerMessage(2146));
			return;
		}

		int usage_point = SC_REST_EXP_INFO_NOTI.EINHASAD_LIMIT - charge_point;
		if (pc.getAccount().getBlessOfAin() > usage_point) {
			pc.sendPackets(
                    String.format("只能在祝福指數低於%d%%時使用。", usage_point / SC_REST_EXP_INFO_NOTI.EINHASAD_UNIT));
			return;
		}

		pc.getAccount().addBlessOfAin(charge_point, pc);
		pc.sendPackets(new S_SystemMessage(
                String.format("盈海薩德的祝福增加了%d%%。", charge_point / SC_REST_EXP_INFO_NOTI.EINHASAD_UNIT));
		pc.send_effect(198);// 15357
		pc.getInventory().removeItem(use_item, 1);
	}

	void onRemovePolyItem(L1PcInstance pc, L1ItemInstance l1iteminstance, int itemId) {
		if (itemId == 40008 || itemId == 140008) {
			l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
			if (l1iteminstance.getChargeCount() == 0) {
				pc.getInventory().removeItem(l1iteminstance);
			} else {
				pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
			}
		} else {
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
	}

	void onPoly(L1PcInstance pc, L1ItemInstance l1iteminstance, int itemId, int readed_int_1, String readed_string_1) {
		pc.sendPackets(new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand));
		pc.broadcastPacket(new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand));
		int chargeCount = l1iteminstance.getChargeCount();
		int spriteId = pc.getCurrentSpriteId();
		if (chargeCount <= 0 && itemId != 40410 || spriteId == 6034 || spriteId == 6035) {
			pc.sendPackets(79);
			return;
		}
		L1Object target = L1World.getInstance().findObject(readed_int_1);
		if (!pc.glanceCheck(target.getX(), target.getY())) {
			pc.sendPackets(281);
			return;
		}
		if (readed_int_1 == pc.getId() || (target != null && target instanceof L1Character)) {
			L1Character cha = readed_int_1 == pc.getId() ? pc : (L1Character) target;
			if (cha.getAI() == null)
				polyAction(pc, cha, itemId, readed_string_1);
			cancelAbsoluteBarrier(pc);
			onRemovePolyItem(pc, l1iteminstance, itemId);
		} else {
			pc.sendPackets(79);
		}
	}

	public static void onUseEinhasadPrimiumFlat(L1PcInstance pc, L1ItemInstance item, int remainSeconds) {
		if (pc.hasSkillEffect(L1SkillId.EINHASAD_PRIMIUM_FLAT)) {
            pc.sendPackets("由於盈海薩德的包月效果，無法使用。");
			return;
		}

		if (pc.hasSkillEffect(L1SkillId.EINHASAD_GREAT_FLAT)) {
			pc.removeSkillEffect(L1SkillId.EINHASAD_GREAT_FLAT);
		}

		if (pc.getInventory().consumeItem(item, 1)) {
			pc.setSkillEffect(L1SkillId.EINHASAD_PRIMIUM_FLAT, remainSeconds * 1000);
			L1SkillId.onEinhasadPrimiumFlat(pc);

			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
                    String.format("[盈海薩德高級包月剩餘時間] %s\n包月期間內祝福數值不會減少並保持。",
                            MJString.remainTimeString(remainSeconds))));
            pc.sendPackets("\f3- 盈海薩德高級包月剩餘時間");
            pc.sendPackets("\f3    盈海數值保持並不減少。");
            pc.sendPackets("\f3  即使安海數值保持，也能獲得安海點數！");
        }
                }

	public static void onUseEinhasadGreatFlat(L1PcInstance pc, L1ItemInstance item, int remainSeconds) {
		if (pc.hasSkillEffect(L1SkillId.EINHASAD_GREAT_FLAT)) {
            pc.sendPackets("由於存在盈海薩德的定額制效果，因此無法使用。");
			return;
		}

		if (pc.hasSkillEffect(L1SkillId.EINHASAD_PRIMIUM_FLAT)) {
			pc.removeSkillEffect(L1SkillId.EINHASAD_PRIMIUM_FLAT);
		}

		if (pc.getInventory().consumeItem(item, 1)) {
			pc.setSkillEffect(L1SkillId.EINHASAD_GREAT_FLAT, remainSeconds * 1000);
			L1SkillId.onEinhasadGreatFlat(pc);

			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
                    String.format("[盈海薩德高級定額制剩餘時間] %s\n當祝福數值全部消耗（0%）時，保持（基本）效果。",
                            MJString.remainTimeString(remainSeconds))));
            pc.sendPackets("\\aN- 盈海薩德高級定額制剩餘時間");
            pc.sendPackets("\\aN   盈海數值為1000時會消耗。");
            pc.sendPackets("\\aN   當達到0%時，保持基本庇護效果（基本）。");
            pc.sendPackets("\\aN- 盈海數值消耗後仍可獲得盈海點數！");
        }
                }

	@SuppressWarnings("deprecation")
	private void Class_Rank_Bless(L1PcInstance pc, L1ItemInstance useItem, int rank) {
		if (pc.getClassRankLevel() > 3) {
            pc.sendPackets(String.format("目前職業排名第 %d 位，因此無法使用。", pc.getClassRankLevel()));
			// pc.getInventory().removeItem(useItem);
			return;
		}

		int skill_id = 0;
		if (rank == 1) {
			if (pc.isCrown()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_PRINCE_1;
			} else if (pc.isKnight()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_KNIGHT_1;
			} else if (pc.isElf()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_ELF_1;
			} else if (pc.isWizard()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_WIZARD_1;
			} else if (pc.isDarkelf()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_DARKELF_1;
			} else if (pc.isDragonknight()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_1;
			} else if (pc.isBlackwizard()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_1;
            } else if (pc.isWarrior()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_WARRIOR_1;
			} else if (pc.isFencer()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_FENCER_1;
			} else if (pc.isLancer()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_LANCER_1;
			}
		} else if (rank == 2) {
			if (pc.isCrown()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_PRINCE_2;
			} else if (pc.isKnight()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_KNIGHT_2;
			} else if (pc.isElf()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_ELF_2;
			} else if (pc.isWizard()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_WIZARD_2;
			} else if (pc.isDarkelf()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_DARKELF_2;
			} else if (pc.isDragonknight()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_2;
			} else if (pc.isBlackwizard()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_2;
            } else if (pc.isWarrior()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_WARRIOR_2;
			} else if (pc.isFencer()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_FENCER_2;
			} else if (pc.isLancer()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_LANCER_2;
			}
		} else if (rank == 3) {
			if (pc.isCrown()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_PRINCE_3;
			} else if (pc.isKnight()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_KNIGHT_3;
			} else if (pc.isElf()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_ELF_3;
			} else if (pc.isWizard()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_WIZARD_3;
			} else if (pc.isDarkelf()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_DARKELF_3;
			} else if (pc.isDragonknight()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_3;
			} else if (pc.isBlackwizard()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_3;
            } else if (pc.isWarrior()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_WARRIOR_3;
			} else if (pc.isFencer()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_FENCER_3;
			} else if (pc.isLancer()) {
				skill_id = L1SkillId.CLASS_RANK_BLESS_LANCER_3;
			}
		}

		if (skill_id == 0)
			return;

		Calendar currentDate = RealTimeClock.getInstance().getRealTimeCalendar();
		Timestamp lastUsed = pc.getClassRankBlessTime();
		if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 60 * 60 * 1)) {
			if (pc.getInventory().consumeItem(41246, 1000)) {
				pc.send_effect(19266);
				if (pc.hasSkillEffect(skill_id)) {
					pc.removeSkillEffect(skill_id);
					class_buff_info(pc, skill_id);
				} else {
					class_buff_info(pc, skill_id);
				}

				pc.setClassRankBlessTime(new Timestamp(currentDate.getTimeInMillis()));
			} else {
				pc.sendPackets(299);
			}
		} else {
			long i = (lastUsed.getTime() + (1000 * 60 * 60 * 1)) - currentDate.getTimeInMillis();
			Calendar cal = (Calendar) currentDate.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
            pc.sendPackets(new S_SystemMessage(i / 60000 + "分鐘內（直到 " + cal.getTime().getHours() + ":"
                    + cal.getTime().getMinutes() + "）無法使用。"), true);
		}
	}
	private void useAdenScroll(L1PcInstance pc, L1ItemInstance item) {
		int buffDuration = 900;
		int str_id = 0;
		int icon_id = 0;
		int itemid = item.getItemId();
		/*
	case 30001874:
	case 30001875:
	case 30001876:*/

		if (pc.get_is_client_auto()) {
			if (itemid == 30001874 && pc.hasSkillEffect(L1SkillId.STR_ADEN_SCROLL_BUFF)) {
				return;
			}
			if (itemid == 30001875 && pc.hasSkillEffect(L1SkillId.DEX_ADEN_SCROLL_BUFF)) {
				return ;
			}
			if (itemid == 30001876 && pc.hasSkillEffect(L1SkillId.INT_ADEN_SCROLL_BUFF)) {
				return;
			}
		}

		if (pc.hasSkillEffect(L1SkillId.STR_ADEN_SCROLL_BUFF)) {
			pc.removeSkillEffect(L1SkillId.STR_ADEN_SCROLL_BUFF);
			L1SkillUse.off_icons(pc, L1SkillId.STR_ADEN_SCROLL_BUFF);
		}
		if (pc.hasSkillEffect(L1SkillId.DEX_ADEN_SCROLL_BUFF)) {
			pc.removeSkillEffect(L1SkillId.DEX_ADEN_SCROLL_BUFF);
			L1SkillUse.off_icons(pc, L1SkillId.DEX_ADEN_SCROLL_BUFF);
		}
		if (pc.hasSkillEffect(L1SkillId.INT_ADEN_SCROLL_BUFF)) {
			pc.removeSkillEffect(L1SkillId.INT_ADEN_SCROLL_BUFF);
			L1SkillUse.off_icons(pc, L1SkillId.INT_ADEN_SCROLL_BUFF);
		}

		switch(item.getItemId()) {
            case 30001874: //力量
                pc.setSkillEffect(L1SkillId.STR_ADEN_SCROLL_BUFF, 900 * 1000);
                pc.getAbility().addAddedStr(1);
                pc.addDmgup(3);
                pc.addHitup(5);
                L1SkillUse.on_icons(pc, L1SkillId.STR_ADEN_SCROLL_BUFF, 900);
                break;
            case 30001875: //敏捷
                pc.setSkillEffect(L1SkillId.DEX_ADEN_SCROLL_BUFF, 900 * 1000);
                pc.getAbility().addAddedDex(1);
                pc.addBowDmgup(3);
                pc.addBowHitup(5);
                L1SkillUse.on_icons(pc, L1SkillId.DEX_ADEN_SCROLL_BUFF, 900);
                break;
            case 30001876: //智力
                pc.setSkillEffect(L1SkillId.INT_ADEN_SCROLL_BUFF, 900 * 1000);
                pc.getAbility().addAddedInt(1);
                pc.getAbility().addSp(1);
                pc.addBaseMagicHitUp(3);
                L1SkillUse.on_icons(pc, L1SkillId.INT_ADEN_SCROLL_BUFF, 900);
                break;
        }

		pc.getInventory().consumeItem(itemid, 1);

	/*
		str_aden_scroll = true;
		set_str_aden_scroll_time(900);
		pc.addHitup(3);
		pc.addDmgup(3);
		pc.getAbility().addAddedStr(1);
		pc.setSkillEffect(L1SkillId.STR_ADEN_SCROLL_BUFF, str_aden_time * 1000);
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
		SkillDataController.getInstance().add_int_ice(pc);
		pc.send_effect(9818);
//		pc.sendPackets(new S_SkillSound(pc.getId(), 7976), true);
//		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7976), true);

		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.RESTAT);
		noti.set_spell_id(L1SkillId.INT_BUFF);
		noti.set_duration(int_ice_time);
		noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
		noti.set_on_icon_id(4354);
		noti.set_off_icon_id(4354);
		noti.set_tooltip_str_id(1721);
		noti.set_new_str_id(1721);
		noti.set_end_str_id(2854);
		noti.set_is_good(true);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);*/









	}

	public void class_buff_info(L1PcInstance pc, int skillid) {
		switch (skillid) {
			case L1SkillId.CLASS_RANK_BLESS_PRINCE_1:
			case L1SkillId.CLASS_RANK_BLESS_KNIGHT_1:
			case L1SkillId.CLASS_RANK_BLESS_ELF_1:
			case L1SkillId.CLASS_RANK_BLESS_WIZARD_1:
			case L1SkillId.CLASS_RANK_BLESS_DARKELF_1:
			case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_1:
			case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_1:
			case L1SkillId.CLASS_RANK_BLESS_WARRIOR_1:
			case L1SkillId.CLASS_RANK_BLESS_FENCER_1:
			case L1SkillId.CLASS_RANK_BLESS_LANCER_1:
				pc.addMaxHp(200);
				pc.getResistance().addcalcPcDefense(10);
				break;
			case L1SkillId.CLASS_RANK_BLESS_PRINCE_2:
			case L1SkillId.CLASS_RANK_BLESS_KNIGHT_2:
			case L1SkillId.CLASS_RANK_BLESS_ELF_2:
			case L1SkillId.CLASS_RANK_BLESS_WIZARD_2:
			case L1SkillId.CLASS_RANK_BLESS_DARKELF_2:
			case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_2:
			case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_2:
			case L1SkillId.CLASS_RANK_BLESS_WARRIOR_2:
			case L1SkillId.CLASS_RANK_BLESS_FENCER_2:
			case L1SkillId.CLASS_RANK_BLESS_LANCER_2:
				pc.addMaxHp(150);
				pc.getResistance().addcalcPcDefense(5);
				break;
			case L1SkillId.CLASS_RANK_BLESS_PRINCE_3:
			case L1SkillId.CLASS_RANK_BLESS_KNIGHT_3:
			case L1SkillId.CLASS_RANK_BLESS_ELF_3:
			case L1SkillId.CLASS_RANK_BLESS_WIZARD_3:
			case L1SkillId.CLASS_RANK_BLESS_DARKELF_3:
			case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_3:
			case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_3:
			case L1SkillId.CLASS_RANK_BLESS_WARRIOR_3:
			case L1SkillId.CLASS_RANK_BLESS_FENCER_3:
			case L1SkillId.CLASS_RANK_BLESS_LANCER_3:
				pc.addMaxHp(100);
				pc.getResistance().addcalcPcDefense(3);
				break;

			default:
				return;
		}
		L1SkillUse.on_icons(pc, skillid, 600);
		pc.setSkillEffect(skillid, 600 * 1000);
	}

	public void taras_buff(L1PcInstance pc, L1ItemInstance item) {
		if (!pc.getInventory().checkItem(41246, 100)) {
            pc.sendPackets("由於決定體不足，無法使用。");
			return;
		}
		pc.getInventory().consumeItem(41246, 100);
		List<Integer> bufflist = new ArrayList<Integer>();

//		int[] allBuffSkill = {};
		if (item.getItemId() == 30001399) {
			bufflist.add(26);
			bufflist.add(42);
			bufflist.add(48);
//			allBuffSkill = { 26, 42, 48, 5033, 5531 };
		} else if (item.getItemId() == 30001398) {
			bufflist.add(26);
			bufflist.add(42);
			bufflist.add(48);
			bufflist.add(5033);
			bufflist.add(5531);
//			allBuffSkill = { 26, 42, 48, 5033, 5531 };
		}

		pc.setBuffnoch(1);
		L1SkillUse l1skilluse = new L1SkillUse();
		/*
		 * if (pc.hasSkillEffect(L1SkillId.TARAS_MOVE_SPEED)) {
		 * System.out.println("1: "+pc.getSkillEffectTimeSec(L1SkillId.TARAS_MOVE_SPEED)
		 * ); } if (pc.hasSkillEffect(L1SkillId.TARAS_ATTACK_SPEED)) {
		 * System.out.println("2: "+pc.getSkillEffectTimeSec(L1SkillId.
		 * TARAS_ATTACK_SPEED)); }
		 */
		for (int i = 0; i < bufflist.size(); i++) {
			l1skilluse.handleCommands(pc, bufflist.get(i), pc.getId(), pc.getX(), pc.getY(), null, 1200,
					L1SkillUse.TYPE_GMBUFF);
//			l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 1200, L1SkillUse.TYPE_GMBUFF);
		}
		pc.setBuffnoch(0);
	}

	private void WeaponEnchant(L1PcInstance pc, L1ItemInstance l1iteminstance1, L1ItemInstance l1iteminstance, GameClient client) {
                    /*
                     * if (l1iteminstance1 == null || l1iteminstance1.isEquipped()) {
                     * pc.sendPackets(new S_ServerMessage(4302));// 裝備中的物品無法使用
                     * return;
                     * }
                     */

		int itemId = l1iteminstance.getItemId();
		if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() != 1) {
			pc.sendPackets(79);
			return;
		}

		if (l1iteminstance1.getEndTime() != null) {
			pc.sendPackets(79);
			return;
		}


		int enchant_level = l1iteminstance1.getEnchantLevel();



		int safe_enchant = l1iteminstance1.getItem().get_safeenchant();
		if (safe_enchant < 0) { // 無法強化
			pc.sendPackets(79);
			return;
		}



		int weaponId = l1iteminstance1.getItem().getItemId();
            if (weaponId >= 246 && weaponId <= 249) { // 無法強化
                if (itemId == L1ItemId.SCROLL_OF_ENCHANT_QUEST_WEAPON) { // 試煉的
                } else {
                    pc.sendPackets(79); // 可能是傳送失敗信息的封包
                    return;
                }
            }

            if (itemId == L1ItemId.SCROLL_OF_ENCHANT_QUEST_WEAPON) { // 試煉的卷軸
                if (weaponId >= 246 && weaponId <= 249) { // 無法強化
			} else {
				pc.sendPackets(79);
				return;
			}
		}

                    /** 騎士團的武器魔法卷軸 **/
		if ((itemId == 4100462 || (itemId >= 30001360 && itemId <= 30001363))
				&& !(weaponId >= 7000224 && weaponId <= 7000230 || weaponId == 7000252)) {
			pc.sendPackets(79);
			return;
		}
		if (weaponId >= 7000224 && weaponId <= 7000230 || weaponId == 7000252) {
			if (!(itemId == 4100462 || (itemId >= 30001360 && itemId <= 30001363))) {
				pc.sendPackets(79);
				return;
			}
			if (l1iteminstance1.getEnchantLevel() >= 9) { //限制9
				pc.sendPackets(79);
				return;
			}
		}
                    /** 阿諾德武器魔法卷軸 **/
		if (weaponId >= 307 && weaponId <= 314) {
			if (itemId == 30146) {
			} else {
				pc.sendPackets(79);
				return;
			}
		}
		if (itemId == 30146) {
			if (weaponId >= 307 && weaponId <= 314) {
			} else {
				pc.sendPackets(79);
				return;
			}
		}

        /** 幻象的武器魔法卷軸 **/
        if (weaponId >= 413000 && weaponId <= 413007) { // 其他無法強化
            if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_WEAPON) { // 幻象的武器魔法卷軸
            } else {
                pc.sendPackets(79);
                return;
            }
        }
        if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_WEAPON) { // 幻象的武器魔法卷軸
            if (weaponId >= 413000 && weaponId <= 413007) { // 其他
            } else {
                pc.sendPackets(79);
                return;
            }
        }

        // 勇士的武器魔法卷軸
        if (weaponId >= 1126 && weaponId <= 1133) { // 其他無法強化
            if (itemId == 30068) {
            } else {
                pc.sendPackets(79);
                return;
            }
        }
        if (itemId == 30068) {
            if (weaponId >= 1126 && weaponId <= 1133) { // 其他無法強化
            } else {
                pc.sendPackets(79);
                return;
            }
        }

        /** 蒼天的武器魔法卷軸 **/
        if (itemId == 210085) {
            if ((weaponId >= 231 && weaponId <= 240) || (weaponId >= 510 && weaponId <= 539)) {
            } else {
                pc.sendPackets(79);
                return;
            }
        }
		if ((weaponId >= 231 && weaponId <= 240) || (weaponId >= 510 && weaponId <= 539)) {
			if (itemId == 210085) {// 蒼天的武器魔法卷軸
			} else {
				pc.sendPackets(79);
				return;
			}
		}
        /*
         * 根據itemId的值進行切換，如果是140087、40087、240087中的任何一個，
         * 並且weaponId匹配特定的一系列值，
         * 則告訴玩家只能使用[古人的手稿:武器]進行附魔。
         * 如果不符合條件，則發送封包79並返回（停止操作）。
         */
            /** 騎士團的武器魔法卷軸 **/
		/*
		 * if (weaponId >= 7000224 && weaponId <= 7000230) { if (itemId == 4100462 ||
		 * itemId == L1ItemId.B_SCROLL_CHIVALRIC_ENCHANT_WEAPON || itemId ==
		 * L1ItemId.C_SCROLL_CHIVALRIC_ENCHANT_WEAPON) { if
		 * (l1iteminstance1.getEnchantLevel() >= 9) { pc.sendPackets(79); return; } }
		 * else { pc.sendPackets(79); return; } } if (itemId == 4100462 || itemId ==
		 * L1ItemId.B_SCROLL_CHIVALRIC_ENCHANT_WEAPON || itemId ==
		 * L1ItemId.C_SCROLL_CHIVALRIC_ENCHANT_WEAPON) { if (weaponId >= 7000224 &&
		 * weaponId <= 7000230) { } else { pc.sendPackets(79); return; } }
		 */
                    /** 象牙塔的武器魔法卷軸 **/
		if (itemId == L1ItemId.IVORYTOWER_WEAPON_SCROLL) {
			if (weaponId == 7 || weaponId == 35 || weaponId == 48 || weaponId == 73 || weaponId == 105
					|| weaponId == 120 || weaponId == 147 || weaponId == 156 || weaponId == 174
					|| weaponId == 9000 || weaponId == 9001 || weaponId == 9002 || weaponId == 9003
					|| weaponId == 175 || weaponId == 224 || weaponId == 203012 || weaponId == 7000222) {
				if (l1iteminstance1.getEnchantLevel() >= 6) {
					pc.sendPackets(79);
					return;
				}
			} else {
				pc.sendPackets(79);
				return;
			}
		}
		if (weaponId == 7 || weaponId == 35 || weaponId == 48 || weaponId == 73 || weaponId == 105
				|| weaponId == 120 || weaponId == 147 || weaponId == 156 || weaponId == 174
				|| weaponId == 9003 || weaponId == 175 || weaponId == 224 || weaponId == 203012
				|| weaponId == 7000222) {
			if (itemId != L1ItemId.IVORYTOWER_WEAPON_SCROLL) {
				pc.sendPackets(79);
				return;
			}
		}
		if (l1iteminstance1.getBless() >= 128 && (!(itemId >= 210064 && itemId <= 210067
				|| itemId >= 560030 && itemId <= 560033 || itemId == 810003 || itemId == 4100148))) { // 封印物品
			pc.sendPackets(79);
			return;
		}
                    /** 屬性附魔 **/
		if ((itemId == 210064 || itemId == 30001360) && l1iteminstance1.getAttrEnchantLevel() != 0
				&& l1iteminstance1.getAttrEnchantLevel() != 11
				&& l1iteminstance1.getAttrEnchantLevel() != 12
				&& l1iteminstance1.getAttrEnchantLevel() != 13
				&& l1iteminstance1.getAttrEnchantLevel() != 14
				&& l1iteminstance1.getAttrEnchantLevel() != 15) {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1294)));
			return;
		}
		if ((itemId == 210065 || itemId == 30001361) && l1iteminstance1.getAttrEnchantLevel() != 0
				&& l1iteminstance1.getAttrEnchantLevel() != 16
				&& l1iteminstance1.getAttrEnchantLevel() != 17
				&& l1iteminstance1.getAttrEnchantLevel() != 18
				&& l1iteminstance1.getAttrEnchantLevel() != 19
				&& l1iteminstance1.getAttrEnchantLevel() != 20) {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1294)));
			return;
		}
		if ((itemId == 210066 || itemId == 30001362) && l1iteminstance1.getAttrEnchantLevel() != 0
				&& l1iteminstance1.getAttrEnchantLevel() != 6 && l1iteminstance1.getAttrEnchantLevel() != 7
				&& l1iteminstance1.getAttrEnchantLevel() != 8 && l1iteminstance1.getAttrEnchantLevel() != 9
				&& l1iteminstance1.getAttrEnchantLevel() != 10) {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1294)));
			return;
		}
		if ((itemId == 210067 || itemId == 30001363) && l1iteminstance1.getAttrEnchantLevel() != 0
				&& l1iteminstance1.getAttrEnchantLevel() != 1 && l1iteminstance1.getAttrEnchantLevel() != 2
				&& l1iteminstance1.getAttrEnchantLevel() != 3 && l1iteminstance1.getAttrEnchantLevel() != 4
				&& l1iteminstance1.getAttrEnchantLevel() != 5) {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1294)));
			return;
		}



		if (enchant_level >= Config.ServerEnchant.LimitWeapon && (!(itemId >= 210064 && itemId <= 210067))
				&& (!(itemId >= 560030 && itemId <= 560033))) {
            pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("武器無法強化超過+" + Config.ServerEnchant.LimitWeapon + "。")));
			return;
		}
		if (safe_enchant == 0) {
			if (enchant_level >= Config.ServerEnchant.LimitWeapon2
					&& (!(itemId >= 210064 && itemId <= 210067))
					&& (!(itemId >= 560030 && itemId <= 560033))) {
				if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_WEAPON || itemId == L1ItemId.ENCHANT_WEAPONA) {
					pc.getInventory().removeItem(l1iteminstance, 1);
					SuccessEnchant(pc, l1iteminstance1, client, -1);
				} else {
                    pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("武器無法強化超過+" + Config.ServerEnchant.LimitWeapon2 + "。")));
				}
				return;
			}
		}
		if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_WEAPON || itemId == L1ItemId.ENCHANT_WEAPONA
				|| itemId == L1ItemId.C_SCROLL_CHIVALRIC_ENCHANT_WEAPON) { // c-dai
			pc.getInventory().removeItem(l1iteminstance, 1);
			int rnd = _random.nextInt(100) + 1;
			if (safe_enchant == 0 && rnd <= 30) {
				FailureEnchant(pc, l1iteminstance1, client);
				return;
			}
			if (enchant_level < -6) { // 無法強化超過 -7。
				FailureEnchant(pc, l1iteminstance1, client);
			} else {
				SuccessEnchant(pc, l1iteminstance1, client, -1);
			}

		} else if (itemId == 210064 || itemId == 210065 || itemId == 210066 || itemId == 210067
				|| itemId == 30001360 || itemId == 30001361 || itemId == 30001362 || itemId == 30001363) {
			AttrEnchant(pc, l1iteminstance1, itemId);

            /** 屬性轉換卷軸 **/
		} else if (itemId >= 560030 && itemId <= 560033) {
			AttrChangeEnchant(pc, l1iteminstance1, itemId);
		} else if (itemId == 724) {
			if (l1iteminstance1 != null && weaponId == 315 || weaponId == 316 || weaponId == 317
					|| weaponId == 318 || weaponId == 319 || weaponId == 320 || weaponId == 1104
					|| weaponId == 7000136 || weaponId == 7000213 || weaponId == 259 || weaponId == 260
					|| weaponId == 261 || weaponId == 262 || weaponId == 263 || weaponId == 264
					|| weaponId == 265 || weaponId == 266 || weaponId == 267) {
                if (enchant_level > 10) { // 使用最大強化數值
                        pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("強化無法再進行。")));
					return;
				}
				int k3 = CommonUtil.random(100);

				if (k3 >= 0 && k3 <= Config.ServerEnchant.Heroweapon) {
					if (Config.ServerEnchant.EnchantMaxFail && enchant_level >= 11) {
                        pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("強烈地閃耀，但強化失敗了。")));
                        pc.getInventory().removeItem(l1iteminstance, 1);
                    } else {
                        SuccessEnchant(pc, l1iteminstance1, client, +1);
                        pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("強烈地閃耀，強化成功了。")));
                        pc.getInventory().removeItem(l1iteminstance, 1);
                    }
                } else {
                    pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("強烈地閃耀，但強化失敗了。")));
					pc.getInventory().removeItem(l1iteminstance, 1);
					FailureEnchant(pc, l1iteminstance1, client);
				}
			}
		} else if (itemId == 810003) {// 匠人的對武器施法的卷軸
			if (!(l1iteminstance1.getItem().getMaterial() == 9
					|| l1iteminstance1.getItem().getMaterial() == 18)) {
				if (enchant_level == 9) {
					if (MJRnd.isWinning(1000000, Config.ServerEnchant.MasterEnchant)) {
						SuccessEnchant(pc, l1iteminstance1, client, 1);
					} else {
                        // 強化：強烈地閃耀，但什麼也沒發生.
						pc.sendPackets(new S_ServerMessage(1310));
						EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN,
								enchant_level - enchant_level, l1iteminstance1);
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				} else {
                    pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("僅限於武器強化 +9 以下使用。")));
                    // 僅限於強化 +9 的武器使用
                }
            } else {
                pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1294)));
                // 強化：無法使用該強化卷軸
            }
        } else if (itemId == 30001897) { // 盧托的對武器施法的卷軸
			if (!(l1iteminstance1.getItem().getMaterial() == 9
					|| l1iteminstance1.getItem().getMaterial() == 18)) {
				if (enchant_level >= Config.ServerEnchant.NoltoWeaponEnchant_Count) {
					if (MJRnd.isWinning(1000000, Config.ServerEnchant.NoltoWeaponEnchant)) {
						SuccessEnchant(pc, l1iteminstance1, client, 1);
					} else {
                        // 強化：強烈地閃耀，但什麼也沒發生。
						pc.sendPackets(new S_ServerMessage(1310));
						EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN,
								enchant_level - enchant_level, l1iteminstance1);
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				} else {
                    pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("僅限於武器強化 +" + Config.ServerEnchant.NoltoWeaponEnchant_Count + " 以上使用。")));
                    // 僅限於強化 +9 的武器使用
				}
			} else {
				pc.sendPackets(new S_ServerMessage(1294));
                // 強化：無法使用該強化卷軸
			}
		} else if (itemId == 4100148) {// 死神的對武器施法的卷軸
			if (!(l1iteminstance1.getItem().getMaterial() == 9)) {
				if (enchant_level >= Config.ServerEnchant.MasterEnchant1
						&& enchant_level <= Config.ServerEnchant.MasterEnchant2) {
					int rnd = _random.nextInt(100);
					if (rnd <= Config.ServerEnchant.MasterEnchant3) {
						SuccessEnchant(pc, l1iteminstance1, client, 1);
					} else {
                        // 強化：雖然發出了強烈的光芒，但什麼事也沒有發生。
						pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1310)));
						EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN,
								enchant_level - enchant_level, l1iteminstance1);
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				} else {
                    pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("僅限於武器強化 +" + Config.ServerEnchant.MasterEnchant1 + " 以上 " + Config.ServerEnchant.MasterEnchant2 + " 以下使用。")));
                    // 僅限於強化 +9 的武器使用
				}
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(1294)));
                // 強化：無法使用該強化卷軸
			}
		} else if (enchant_level < safe_enchant) {
			pc.getInventory().removeItem(l1iteminstance, 1);
			SuccessEnchant(pc, l1iteminstance1, client, RandomELevel(l1iteminstance1, itemId));
		} else {
			pc.getInventory().removeItem(l1iteminstance, 1);
			int rnd = _random.nextInt(100) + 1;
			int enchant_chance_wepon;
			int chance = 0;
			try {
				chance = WeaponEnchantList.getInstance().getWeaponEnchant(l1iteminstance1.getItemId());
			} catch (Exception e) {
                System.out.println("武器強化列表機率錯誤");
			}
			int weaponChance = WeaponEnchantInformationTable.getInstance().getChance(weaponId,
					enchant_level);
			if (enchant_level >= 7 && enchant_level < 8) {
				enchant_chance_wepon = 10 / ((enchant_level - safe_enchant + 1) * 2)
						/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
				rnd = _random.nextInt(100) + 1;
				enchant_chance_wepon *= Config.ServerEnchant.EnchantFailRateOnest;
			}
			if (enchant_level >= 8 && enchant_level < 9) {
				enchant_chance_wepon = 10 / ((enchant_level - safe_enchant + 1) * 2)
						/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
				rnd = _random.nextInt(120) + 1;
				enchant_chance_wepon *= Config.ServerEnchant.EnchantFailRateOnesto;
			}
			if (enchant_level >= 9 && enchant_level < 10) {
				enchant_chance_wepon = 10 / ((enchant_level - safe_enchant + 1) * 2)
						/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
				rnd = _random.nextInt(150) + 1;
				enchant_chance_wepon *= Config.ServerEnchant.EnchantFailRateOne;
			}
			if (enchant_level >= 10 && enchant_level < 11) {
				enchant_chance_wepon = 1 / ((enchant_level - safe_enchant + 1) * 2)
						/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
				rnd = _random.nextInt(200) + 1;
				enchant_chance_wepon *= Config.ServerEnchant.EnchantFailRateTwo;
			}
			if (enchant_level >= Config.ServerEnchant.EnchantCoent && enchant_level <= 15) { // 11以上
				if (!Config.ServerEnchant.EnchantMaxFail) {
					enchant_chance_wepon = 3 / ((enchant_level - safe_enchant + 1) * 2)
							/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
				} else {
					enchant_chance_wepon = 0;
				}
			} else {
                if (l1iteminstance1.getItem().get_safeenchant() == 0) { // 安全強化時
                    enchant_chance_wepon = 70 / ((enchant_level - safe_enchant + 1) * 2)
                            / (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
                } else { // 6~8級
                    enchant_chance_wepon = 60 / ((enchant_level - safe_enchant + 1) * 2)
                            / (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance;
                }
			}
            if (pc.isGm()) {
                pc.sendPackets("\\aA[Pro/DB/Bonus/Rnd]: \f2[成功機率]:\\aA(\\aG" + enchant_chance_wepon + " + "
                        + weaponChance + " + " + chance + "\\aA) \f6[失敗機率]:\\aA(\\aG " + rnd + " \\aA)");
                pc.sendPackets("\\aA[說明]: Pro+DB+Bonus 的總和需要高於 Rnd 才能強化成功！");
            }

			if (pc._EnchantWeaponSuccess == true) {
				int randomEnchantLevel = 0;
                // TODO 執行武器 +1 以上時只增加 1
				if (enchant_level >= 1 && (weaponId == 12 || weaponId == 61 || weaponId == 86
						|| weaponId == 7000263 || weaponId == 134 || weaponId == 7000136
						|| weaponId == 7000213 || weaponId == 1104 || itemId == 7000262 || weaponId == 66
						|| weaponId == 7000238 || weaponId == 7000239 || weaponId == 7000264
						|| weaponId == 7000240 || weaponId == 203065 || weaponId == 7000264
						|| (weaponId >= 20201 && weaponId <= 202015))) {
					randomEnchantLevel = 1;
				} else {
					randomEnchantLevel = RandomELevel(l1iteminstance1, itemId);
				}
				SuccessEnchant(pc, l1iteminstance1, client, randomEnchantLevel);
				pc._EnchantWeaponSuccess = false;
			} else if (rnd < enchant_chance_wepon) {
				int randomEnchantLevel = 0;
                // TODO 執行武器 +1 以上時，每次只增加 1
				if (enchant_level >= 1 && (weaponId == 12 || weaponId == 61 || weaponId == 86
						|| weaponId == 134 || weaponId == 7000136 || weaponId == 7000213 || weaponId == 1104
						|| itemId == 7000262 || weaponId == 66 || weaponId == 7000238 || weaponId == 7000239
						|| weaponId == 7000264 || weaponId == 7000240 || weaponId == 203065
						|| weaponId == 7000264 || weaponId == 7000263
						|| (weaponId >= 20201 && weaponId <= 202015))) {
					randomEnchantLevel = 1;
				} else {
					randomEnchantLevel = RandomELevel(l1iteminstance1, itemId);
				}
				SuccessEnchant(pc, l1iteminstance1, client, randomEnchantLevel);
			} else {
				if (enchant_level >= 9 && MJRnd.isWinning(100, Config.ServerEnchant.Weapon_ReEnchant)) {
					EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN, 0,
							l1iteminstance1);
					pc.sendPackets(new S_ServerMessage(160, l1iteminstance1.getLogName(), "$245", "$248"));
				} else {
					FailureEnchant(pc, l1iteminstance1, client);
				}
			}
		}
	}
	private void ArmorEnchant(L1PcInstance pc, L1ItemInstance l1iteminstance1, L1ItemInstance l1iteminstance, GameClient client) {
		int itemId = l1iteminstance.getItemId();
		if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() != 2) {
			pc.sendPackets(new S_ServerMessage(79));
			return;
		}

                    /** 根據防具過濾強化卷軸 **/
		int fill_itemId = l1iteminstance.getItemId();
		int armor_type = l1iteminstance1.getItem().getType();
		int armorId = l1iteminstance1.getItem().getItemId();
		int armortype = l1iteminstance1.getItem().getType();
		int enchant_level = l1iteminstance1.getEnchantLevel();

		switch (l1iteminstance1.getItem().getType()) {
			case 30: // 句子
				if (fill_itemId != 3000547 && fill_itemId != 3000100) {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
					return;
				}
				break;
			case 32: // 徽章
				if (fill_itemId != 3000546 && fill_itemId != 5991) {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
					return;
				}
				break;
		}

        /*

         * if (l1iteminstance1.isEquipped()) { pc.sendPackets(new

         * S_ServerMessage(4302));// 穿戴中的物品不能進行操作 return; }

         */

		int safe_enchant = ((L1Armor) l1iteminstance1.getItem()).get_safeenchant();
		if (safe_enchant < 0) { // 強化不可
            pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79))); // 1 沒有任何東西
			return;
		}

		if (l1iteminstance1.getEndTime() != null) {
			pc.sendPackets(79);
			return;
		}

                    /** 阿諾德對盔甲施法的卷軸 **/
		if (armorId == 21095) {
			if (itemId == 30147) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
		if (itemId == 30147) {
			if (armorId == 21095) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}

                    /** 伊娜翠爾T恤盔甲魔法卷軸 **/
		if (armorId >= 22215 && armorId <= 22223 || armorId >= 490000 && armorId <= 490008) {
			if (itemId >= 410066 && itemId <= 410068) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
		if (itemId >= 410066 && itemId <= 410068) {
			if (armorId >= 22215 && armorId <= 22223 || armorId >= 490000 && armorId <= 490008) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}


        /** 龍之T恤盔甲魔法卷軸 **/

        // TeamTheday by.쥬德
                    if (armorId >= 900025 && armorId <= 900028 || armorId >= 900184 && armorId <= 900187
                            || armorId == 900198) {
                        if ((itemId >= 3000160 && itemId <= 3000162) || itemId == 4100616 || itemId == 4100753) {
                        } else {
                            pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
                            return;
                        }
                    }
		if (itemId >= 3000160 && itemId <= 3000162 || itemId == 4100753) {
			if (armorId >= 900025 && armorId <= 900028 || armorId >= 900184 && armorId <= 900187
					|| armorId == 900198) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}

        /** 徽章強化卷軸 **/
        if (armorId >= 900081 && armorId <= 900084 || armorId >= 900152 && armorId <= 900154) {
            if ((itemId == 5991 || itemId == L1ItemId.ENCHANT_TEST4)) {
                // 這裡可以添加強化的邏輯
            } else {
                pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79))); // 發送伺服器訊息79
                return;
            }
            if (enchant_level >= 8) {
                pc.sendPackets("無法再進行強化。"); // 發送無法再進行強化的訊息
                return;
            }
        }
		if ((itemId == 5991 || itemId == L1ItemId.ENCHANT_TEST4)) {
			if (armorId >= 900081 && armorId <= 900084 || armorId >= 900152 && armorId <= 900154) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
			if (enchant_level >= 8) {
                pc.sendPackets("無法再進行強化。");
				return;
			}
		}

        /** 徽章強化石 **/

        if (armorId == 900020 || armorId == 900021 || armorId == 900049 || armorId == 900050
                || armorId == 900051 || armorId == 900124 || armorId == 900125 || armorId == 900126
                || armorId >= 900093 && armorId <= 900099 || armorId >= 900127 && armorId <= 900130) {
            if ((itemId == 3000100 || itemId == L1ItemId.ENCHANT_TEST5)) {
            } else {
                pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
                return;
            }

        }
		if (itemId == 3000100 || itemId == L1ItemId.ENCHANT_TEST5) {
			if (armorId == 900020 || armorId == 900021 || armorId == 900049 || armorId == 900050
					|| armorId == 900051 || armorId == 900124 || armorId == 900125 || armorId == 900126
					|| armorId >= 900093 && armorId <= 900099 || armorId >= 900127 && armorId <= 900130) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
        /** 古代符文強化石 **/
        if (itemId == 3000517 || itemId == L1ItemId.ENCHANT_TEST3) {
            if (enchant_level >= Config.ServerEnchant.Accessory_Antiquity) {
                pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage("僅可用於該強化等級以下的古代符文。")));
                return;
            }
        }
		if (armorId == 900116) {
			if (itemId == 3000517 || itemId == 3000518) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}

        /** 幻想的盔甲魔法卷軸 **/

        if (armorId >= 423000 && armorId <= 423008) {
            if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR) {
            } else {
                pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
                return;
            }

        }
		if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR) {
			if (armorId >= 423000 && armorId <= 423008) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
        if (itemId == 30069) { // 勇士的盔甲魔法卷軸

            if (armorId >= 22328 && armorId <= 22335) { // 其他無法強化

            } else {

                pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79))); // 1 什麼都沒有發生

                return;

            }
        }
		if (armorId >= 22328 && armorId <= 22335) {
			if (itemId == 30069) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
                    /** 蒼穹的盔甲魔法卷軸 **/
		if (itemId == 210084) {
			if (armorId >= 22034 && armorId <= 22064) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
		if (armorId >= 22034 && armorId <= 22064) {
			if (itemId == 210084) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}

                    /** 飾品強化卷軸 */
		if (itemId == 210068 || itemId == 4100692 || itemId == L1ItemId.Pure_white_Scroll
				|| itemId == L1ItemId.Roomtis_Scroll || itemId == 4100671
				|| itemId == L1ItemId.IVORYTOWER_GKFFHDNLS || itemId == 810012 || itemId == 810013
				|| itemId == L1ItemId.Pendant_Scroll || itemId == 7004 || itemId == L1ItemId.ENCHANT_TEST1
				|| itemId == L1ItemId.ENCHANT_TEST2) {
			if (armortype >= 8 && armortype <= 12 || armortype == 33) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
		if (armortype >= 8 && armortype <= 12 || armortype == 33) {
			if (itemId == 210068 || itemId == 4100692 || itemId == L1ItemId.Pure_white_Scroll
					|| itemId == L1ItemId.ENCHANT_TEST1 || itemId == 7004
					|| itemId == L1ItemId.ENCHANT_TEST2 || itemId == 4100671
					|| itemId == L1ItemId.Roomtis_Scroll || itemId == 810012 || itemId == 810013
					|| itemId == L1ItemId.Pendant_Scroll || itemId == L1ItemId.IVORYTOWER_GKFFHDNLS) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}

		if ((itemId == L1ItemId.ENCHANT_TEST3 || itemId == 3000517) && armorId != 900116) {
			pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
			return;
		}
                    // 萬聖節魔法卷軸
		if (itemId == L1ItemId.IVORYTOWER_GKFFHDNLS) {
			if (armorId == 22367 || armorId == 900024 || armorId == 20380) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
		if (armorId == 22367 || armorId == 900024 || armorId == 20380) {
			if (itemId == L1ItemId.IVORYTOWER_GKFFHDNLS) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
                    // 血鑽強化卷軸
		if (itemId == 4100671) {
			if (armorId == 900274) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
		if (armorId == 900274) {
			if (itemId == 4100671) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
                    // 盧姆蒂斯強化卷軸
		if (itemId == L1ItemId.Roomtis_Scroll || itemId == L1ItemId.ENCHANT_TEST2) {
			if (armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339
					|| armorId == 222340 || armorId == 222341) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
		if (armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339
				|| armorId == 222340 || armorId == 222341) {
			if (itemId == L1ItemId.Roomtis_Scroll || itemId == L1ItemId.ENCHANT_TEST2) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
                    // 斯納普的戒指強化卷軸
		if (itemId == L1ItemId.Pure_white_Scroll || itemId == L1ItemId.ENCHANT_TEST1) {
			if (armorId >= 22224 && armorId <= 22228 || armorId == 222290 || armorId == 222291
					|| armorId >= 222330 && armorId <= 222336) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
		if (armorId >= 22224 && armorId <= 22228 || armorId == 222290 || armorId == 222291
				|| armorId >= 222330 && armorId <= 222336) {
			if (itemId == L1ItemId.Pure_white_Scroll || itemId == L1ItemId.ENCHANT_TEST1) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
                    // 盧姆蒂斯的墜飾強化卷軸
		if (itemId == L1ItemId.Pendant_Scroll) {
			if (armorId >= 900234 && armorId <= 900237 || armorId >= 900275 && armorId <= 900278) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
		if (armorId >= 900234 && armorId <= 900237 || armorId >= 900275 && armorId <= 900278) {
			if (itemId == L1ItemId.Pendant_Scroll) {
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
                    // 修煉者的首飾魔法卷軸
		if (itemId == 7004) {
			if ((armorId >= 22337 && armorId <= 22339 || armorId == 22073)) {
				if (l1iteminstance1.getEnchantLevel() >= 4) {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
					return;
				}
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
                    /** 騎士團的盔甲魔法卷軸 **/
		if (itemId == 4100461 || itemId == L1ItemId.B_SCROLL_CHIVALRIC_ENCHANT_ARMOR
				|| itemId == L1ItemId.C_SCROLL_CHIVALRIC_ENCHANT_ARMOR) {
			if (armorId >= 900172 && armorId <= 900179) {
				if (l1iteminstance1.getEnchantLevel() >= 9) {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
					return;
				}
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
		if (itemId == 4100461 || itemId == L1ItemId.B_SCROLL_CHIVALRIC_ENCHANT_ARMOR
				|| itemId == L1ItemId.C_SCROLL_CHIVALRIC_ENCHANT_ARMOR) {
			if (armorId >= 900172 && armorId <= 900179) {
			} else {
				pc.sendPackets(79);
				return;
			}
		}
                    // 象牙塔的盔甲魔法卷軸
		if (itemId == L1ItemId.IVORYTOWER_ARMOR_SCROLL) {
			if (armorId == 20028 || armorId == 20082 || armorId == 20126 || armorId == 20173
					|| armorId >= 22300 && armorId <= 22311 || armorId == 20206 || armorId == 20232
					|| armorId == 20283) {
				if (l1iteminstance1.getEnchantLevel() >= 6) {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
					return;
				}
			} else if (armorId == 22312 || armorId == 321515 || armorId >= 20500 && armorId <= 20505) {
				if (l1iteminstance1.getEnchantLevel() >= 4) {
					pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
					return;
				}
			} else {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}

		if (armorId == 20028 || armorId == 20082 || armorId == 20126 || armorId == 20173 || armorId == 20206
				|| armorId == 20232 || armorId == 20283 || (armorId >= 22300 && armorId <= 22312)
				|| (armorId >= 20500 && armorId <= 20505)) {
			if (itemId != L1ItemId.IVORYTOWER_ARMOR_SCROLL) {
				pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79)));
				return;
			}
		}
        if (l1iteminstance1.getBless() >= 128) { // 封印物品
            pc.sendPackets(java.lang.String.valueOf(new S_ServerMessage(79))); // 1 什麼都沒有
        }
        return;
		}

        // 盧姆蒂斯的耳環強化限制
                if (armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339
                        || armorId >= 222340 && armorId <= 222341) {
                    if (enchant_level >= Config.ServerEnchant.RoomT) {
                        pc.sendPackets(
                                java.lang.String.valueOf(new S_SystemMessage("盧姆蒂斯耳環不能強化超過 +" + Config.ServerEnchant.RoomT + "。")));
                        return;
                    }
                } else if (armorId >= 22224 && armorId <= 22228 || armorId == 222290 || armorId == 222291 // 斯納普的
                        || armorId >= 222330 && armorId <= 222336) {
                    if (enchant_level >= Config.ServerEnchant.Snapper) {
                        pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage(
                                "斯納普耳環不能強化超過 +" + Config.ServerEnchant.Snapper + "。")));
                        return;
                    }
                } else if (armorId >= 900020 && armorId <= 900021 || armorId >= 900049 && armorId <= 900051
                        || armorId >= 900093 && armorId <= 900099 || armorId >= 900124 && armorId <= 900126
                        || armorId >= 900127 && armorId <= 900130) {
                    if (enchant_level >= Config.ServerEnchant.SENTENCHMAXLEVEL) {
                        pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage(
                                "印章不能強化超過 +" + Config.ServerEnchant.SENTENCHMAXLEVEL + "。")));
                        return;
                    }
                } else if (armorId >= 900234 && armorId <= 900237 || armorId >= 900275 && armorId <= 900278) {
                    if (enchant_level >= Config.ServerEnchant.Pendant) {
                        pc.sendPackets(java.lang.String.valueOf(new S_SystemMessage(
                                "盧姆蒂斯的墜飾不能強化超過 +" + Config.ServerEnchant.Pendant + "。")));
                        return;
                    }
                }
		} else if (armortype >= 8 && armortype <= 12 || armortype == 33) {
			if (!(armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339
					|| armorId >= 222340 && armorId <= 222341 || armorId >= 22224 && armorId <= 22228
					|| armorId == 222290 || armorId == 222291 || armorId >= 222330 && armorId <= 222336
					|| armorId >= 900234 && armorId <= 900237 || armorId >= 900275 && armorId <= 900278)) {
				if (enchant_level >= Config.ServerEnchant.Accessory_Limit) {
                    pc.sendPackets(String.valueOf(new S_SystemMessage(
                            "飾品不能強化超過 +" + Config.ServerEnchant.Accessory_Limit + "。")));
                    return;
                }
            }
            } else {
                if (safe_enchant == 0) {
                    if (enchant_level >= Config.ServerEnchant.LimitArmor2) {
                        if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR || itemId == L1ItemId.ENCHANT_ARMOR) { // 強化防具的卷軸
                            pc.getInventory().removeItem(l1iteminstance, 1);
                            SuccessEnchant(pc, l1iteminstance1, client, -1);
                        } else {
                            pc.sendPackets(String.valueOf(new S_SystemMessage("防具不能強化超過 +" + Config.ServerEnchant.LimitArmor2 + "。")));
					}
					return;
				}
			} else {
                    if (enchant_level >= Config.ServerEnchant.LimitArmor) { // 強化限制
                        pc.sendPackets(String.valueOf(new S_SystemMessage("防具不能強化超過 +" + Config.ServerEnchant.LimitArmor + "。")));
                    }
                    return;
				}
			}
		}

		if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR || itemId == L1ItemId.Inadril_T_ScrollC
				|| itemId == L1ItemId.ENCHANT_ARMOR || itemId == L1ItemId.Inadril_T_ScrollB3
				|| itemId == L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR5
				|| itemId == L1ItemId.C_SCROLL_CHIVALRIC_ENCHANT_ARMOR) { // 詛咒
			pc.getInventory().removeItem(l1iteminstance, 1);
			int rnd = _random.nextInt(100) + 1;
			if (safe_enchant == 0 && rnd <= 30) {
				FailureEnchant(pc, l1iteminstance1, client);
				return;

            } else if (enchant_level < -1) { // 基本物品由 -2 開始最低
                FailureEnchant(pc, l1iteminstance1, client);
            } else {
                SuccessEnchant(pc, l1iteminstance1, client, -1);
            }
        } else if (itemId == 810012) { // 奧林的飾品魔法卷軸
            enchant_orim(pc, l1iteminstance, l1iteminstance1);
            return;
        } else if (itemId == 810013) { // 受祝福的奧林飾品魔法卷軸
            enchant_bless_orim(pc, l1iteminstance, l1iteminstance1);
            return;
        } else if (itemId == 4100753) { // 龍之T恤保護卷軸 TeamTheday 由.쥬드
                                                                    // by.쥬드
			enchant_dragon(pc, l1iteminstance, l1iteminstance1);
			return;
		} else if (itemId == 3000428) {
			enchant_snapper(pc, l1iteminstance, l1iteminstance1);
			return;
		} else if (itemId == 3000430) {
			enchant_roomtis(pc, l1iteminstance, l1iteminstance1);
			return;
		} else if (itemId == 3000547) {
			enchant_sentence(pc, l1iteminstance, l1iteminstance1);
			return;
		} else if (itemId == 3000546) {
			enchant_badge(pc, l1iteminstance, l1iteminstance1);
			return;
            /** 匠人的盔甲魔法卷軸 **/
        } else if (itemId == 4100616) {
            if (l1iteminstance1 != null && l1iteminstance1.getItem().getType2() == 2) {
                if (enchant_level == Config.ServerEnchant.MasterArmorEnchant_Count) {
                    if (MJRnd.isWinning(1000000, Config.ServerEnchant.MasterArmorEnchant)) {
                        SuccessEnchant(pc, l1iteminstance1, client, +1);
                        pc.sendPackets(String.valueOf(new S_SystemMessage("瞬間發出強烈的光芒，強化成功。")));
                        pc.getInventory().removeItem(l1iteminstance, 1);
                    } else {
                        pc.sendPackets(String.valueOf(new S_SystemMessage("雖然瞬間發出強烈的光芒，但強化失敗了。")));
                        pc.getInventory().removeItem(l1iteminstance, 1);
                        EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN,
                                enchant_level - enchant_level, l1iteminstance1);
                    }
                } else {
                    pc.sendPackets(String.valueOf(new S_SystemMessage("只能在強化 +" + Config.ServerEnchant.MasterArmorEnchant + " 的防具上使用。")));
                    return;
                }
            }
		} else if (itemId == 30001896) {
			if (l1iteminstance1 != null && l1iteminstance1.getItem().getType2() == 2) {
				if (enchant_level >= Config.ServerEnchant.NoltoArmorEnchant_Count) {
					if (MJRnd.isWinning(1000000, Config.ServerEnchant.NoltoArmorEnchant)) {
						SuccessEnchant(pc, l1iteminstance1, client, +1);
                        pc.sendPackets(String.valueOf(new S_SystemMessage("瞬間發出強烈的光芒，強化成功。")));
                        pc.getInventory().removeItem(l1iteminstance, 1);
                    } else {
                        pc.sendPackets(String.valueOf(new S_SystemMessage("雖然瞬間發出強烈的光芒，但強化失敗了。")));
                        pc.getInventory().removeItem(l1iteminstance, 1);
                        EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN,
                                enchant_level - enchant_level, l1iteminstance1);
                    }
                } else {
                    pc.sendPackets(String.valueOf(new S_SystemMessage("只能在強化 +"+ Config.ServerEnchant.NoltoArmorEnchant_Count+" 的防具上使用。")));
                    return;
                }
            }
            // 待辦：古代符文保護石
        } else if (itemId == 3000518) {
            if (l1iteminstance1 != null) {
                if (enchant_level < 3) {
                    pc.sendPackets(String.valueOf(new S_SystemMessage("古代符文保護石至少需要 +3 以上才能使用。")));
                    return;
                }
                if (enchant_level >= Config.ServerEnchant.Accessory_Antiquity) {
                    pc.sendPackets(String.valueOf(new S_SystemMessage("只能在該強化等級以下的古代符文上使用。")));
                    return;
                }

				int enchant = enchant_level < 0 ? 0
						: enchant_level >= Config.ServerEnchant.Antiquity_Rune.length
						? Config.ServerEnchant.Antiquity_Rune.length - 1
						: enchant_level;
				if (MJRnd.isWinning(1000000, Config.ServerEnchant.Antiquity_Rune[enchant])) {
					SuccessEnchant(pc, l1iteminstance1, client, +1);
                    pc.sendPackets(String.valueOf(new S_SystemMessage("瞬間發出強烈的光芒，強化成功。")));
                    pc.getInventory().removeItem(l1iteminstance, 1);
                } else {
                    pc.sendPackets(String.valueOf(new S_SystemMessage("雖然瞬間發出強烈的光芒，但強化失敗了。")));
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
			}
		} else if (enchant_level < safe_enchant) {
			pc.getInventory().removeItem(l1iteminstance, 1);
			SuccessEnchant(pc, l1iteminstance1, client, RandomELevel(l1iteminstance1, itemId));
		} else {
			pc.getInventory().removeItem(l1iteminstance, 1);

			int rnd = _random.nextInt(100) + 1;
			int enchant_chance_armor;
			int enchant_level_tmp;
			int chance1 = 0;

            if (safe_enchant == 0) { // 骨頭、黑米用校正
                enchant_level_tmp = 2;
            } else {
                enchant_level_tmp = 1;
            }

            if (armortype >= 8 && armortype <= 12 || armortype == 33) { // 飾品強化成功率
                int acceChance = AccessoryEnchantInformationTable.getInstance().getChance(armorId, enchant_level);
                pc.getInventory().setEquipped(l1iteminstance1, false); // 強化時自動卸下
                if (acceChance > 0) {
                    enchant_chance_armor = acceChance;
                } else if (enchant_level <= 0) {
                    enchant_chance_armor = 9 * acceChance; // 設定值為5時，0級成功率=45%
                } else {
                    enchant_chance_armor = (8 * acceChance) / enchant_level;
                }
            }

            /*
             * if (enchant_level <= 0) { enchant_chance_armor = 9 * acceChance; // 設定值為5時，0級成功率=45%
             * } else { enchant_chance_armor = (8 * acceChance) / enchant_level; }
             */
            /** 如是管理員 */
            if (pc.isGm()) {
                pc.sendPackets(
                        new S_SystemMessage("\\aA機率 : [\\aG " + enchant_chance_armor + "\\aA ]"));
                pc.sendPackets(String.valueOf(new S_SystemMessage("\\aA機會 : [\\aG " + rnd + " \\aA]")));
            }

            /** 給予特定的盧姆蒂斯強化機會 **/
            /*
             * if (armorId == 22229 || armorId == 22230 || armorId == 22231 || armorId ==
             * 222337 || armorId == 222338 || armorId == 222339 || armorId == 222340 ||
             * 222341) {
             * if (enchant_level == 4) {
             * enchant_chance_armor = 15;
             * } else if (enchant_level >= 5) {
             * // -- 如果強化等級是4
             * enchant_chance_armor = 10;
             * }
             * }
             */

			} else {
				int chance = 0;

				/*
				 * try { chance = ArmorEnchantList.getInstance().getArmorEnchant
				 * (l1iteminstance1.getItemId()); } catch (Exception e) {
				 * System.out.println("WeaponEnchantList chance Error" ); }
				 */

				int armorChance = ArmorEnchantInformationTable.getInstance().getChance(armorId,
						enchant_level);
				if (enchant_level >= 6) {
					if (l1iteminstance1.getMr() > 0) {
						enchant_chance_armor = 80 / ((enchant_level - safe_enchant + 1) * 2)
								/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp) + armorChance;
					} else {
						enchant_chance_armor = 90 / ((enchant_level - safe_enchant + 1) * 2)
								/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp) + armorChance;
					}
				} else {
					if (l1iteminstance1.getItem().get_safeenchant() == 0) {
						if (l1iteminstance1.getMr() > 0) {
							enchant_chance_armor = 80 / ((enchant_level - safe_enchant + 1) * 2)
									/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp)
									+ armorChance;
						} else {
							enchant_chance_armor = 90 / ((enchant_level - safe_enchant + 1) * 2)
									/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp)
									+ armorChance;
						}
					} else {
						if (l1iteminstance1.getMr() > 0) {
							enchant_chance_armor = 80 / ((enchant_level - safe_enchant + 1) * 2)
									/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp)
									+ armorChance;
						} else {
							enchant_chance_armor = 90 / ((enchant_level - safe_enchant + 1) * 2)
									/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp)
									+ armorChance;
						}
					}
				}
            if (pc.isGm()) {
                pc.sendPackets(
                        new S_SystemMessage("\\aA機率 : [\\aG " + enchant_chance_armor + "\\aA ]"));
                pc.sendPackets(
                        new S_SystemMessage("\\aA機會 : [\\aG " + rnd + " \\aA]"));
                pc.sendPackets(
                        String.valueOf(new S_SystemMessage("\\aA數據庫 : [\\aG " + armorChance + " \\aA]")));
            }

			if (pc._EnchantArmorSuccess == true) {
				int randomEnchantLevel = 0;
				randomEnchantLevel = RandomELevel(l1iteminstance1, itemId);
				SuccessEnchant(pc, l1iteminstance1, client, randomEnchantLevel);
				pc._EnchantArmorSuccess = false;
			} else if (rnd < enchant_chance_armor) {
				int randomEnchantLevel = RandomELevel(l1iteminstance1, itemId);

                /** 腰帶類 +0 以上時設為 1 （正式服化） **/
                if ((enchant_level >= 0 && armorId >= 22000 && armorId <= 22003)
                        || (armorId >= 22254 && armorId <= 22256) || armorId == 22252 || armorId == 20190) {
                    randomEnchantLevel = 1;
                }

                /** 特別物品 +1 以上時設為 1 （正式服化） **/
                if ((enchant_level >= 1 && armorId == 900188 || armorId == 900121 || armorId == 900123
                        || armorId == 900189)) {
                    randomEnchantLevel = 1;
                }

                /** 龍之T恤 +6 以上時設為 1 **/
                if ((enchant_level >= 6 && armorId >= 900025 && armorId <= 900028
                        || armorId >= 900184 && armorId <= 900187)) {
                    randomEnchantLevel = 1;
                }

                /** 古代岩石怪物類 +6 以上時設為 1 **/
                if ((enchant_level >= 6 && armorId >= 900011 && armorId <= 900018)) {
                    randomEnchantLevel = 1;
                }
				SuccessEnchant(pc, l1iteminstance1, client, randomEnchantLevel);
                /*
                 * } else if (itemId == 810012) { // 強化: %0%s 克服了消亡的枷鎖。
                 * if (enchant_level == 0) {
                 * SuccessEnchant(pc, l1iteminstance1, client, 0);
                 * } else {
                 * SuccessEnchant(pc, l1iteminstance1, client, -1);
                 * }
                 */
			} else if (itemId == 810013) {
                // pc.sendPackets(new S_ServerMessage(4056, l1iteminstance1.getLogName()));
                // 強化: %0%s 克服了消亡的枷鎖。
			} else {
				if (enchant_level >= 8 && MJRnd.isWinning(100, Config.ServerEnchant.Armor_ReEnchant)) {
					pc.sendPackets(new S_ServerMessage(160, l1iteminstance1.getLogName(), "$252", "$248"));
					EnchantResultTable.getIns().send(pc, SC_ENCHANT_RESULT.eResult.FAIL_REMAIN, 0,
							l1iteminstance1);
				} else {
					FailureEnchant(pc, l1iteminstance1, client);
				}
			}
		}
	}

	// public L1ItemInstance doItemSelector(L1Item temp) {
	// L1ItemInstance item ;
	// L1ItemTypeConstruct itemType = temp.getItemType();
	// if(ItemSelectorTable.isSelectorInfo(temp.getItemId()))
	// item = new l1j.server.server.model.item.function.ItemSelector(temp,
	// SelectorType.NORMAL);
	// return new L1ItemInstance(temp);
	// }

}

	private <__TMP__> __TMP__ useItemSealScroll() {
	}

	private <__TMP__> __TMP__ useDragonPearl() {
	}

	private <__TMP__> __TMP__ onUseEinhasadGreatFlat() {
	}