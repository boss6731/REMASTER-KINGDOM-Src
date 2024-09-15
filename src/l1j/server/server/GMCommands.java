package l1j.server.server;

import static jdk.vm.ci.code.CodeUtil.maxValue;
import static l1j.server.server.model.skill.L1SkillId.HUNTER_BLESS;
import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import MJNCoinSystem.MJNCoinCommandComposite;
import MJNCoinSystem.MJNCoinDepositInfo;
import MJNCoinSystem.MJNCoinIdFactory;
import MJNCoinSystem.MJNCoinSettings;
import MJShiftObject.MJEShiftObjectType;
import MJShiftObject.MJShiftObjectCommandExecutor;
import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Battle.MJShiftBattleCommandExecutor;
import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.FatigueProperty;
import l1j.server.L1DatabaseFactory;
import l1j.server.Server;
import l1j.server.SpecialEventHandler;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.BuyLimitSystem.BuyLimitSystem;
import l1j.server.BuyLimitSystem.BuyLimitSystemAccountTable;
import l1j.server.BuyLimitSystem.BuyLimitSystemCharacterTable;
import l1j.server.DeathMatch.DeathMatch;
import l1j.server.DragonRaidSystem.DragonRaidSystemInfo;
import l1j.server.DragonRaidSystem.DragonRaidSystemLoader;
import l1j.server.DragonRaidSystem.DragonController.RaidOfAntaras;
import l1j.server.DragonRaidSystem.DragonController.RaidOfFafurion;
import l1j.server.DragonRaidSystem.DragonController.RaidOfLindvior;
import l1j.server.DragonRaidSystem.DragonController.RaidOfValakas;
import l1j.server.EventSystem.EventSystemLoader;
import l1j.server.EventSystem.EventSystemSpawner;
import l1j.server.EventSystem.EventSystemTimeController;
//import l1j.server.GameSystem.MiniGame.LottoSystem;
import l1j.server.InfinityBattle.InfinityBattle;
import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.MJCaptchaSystem.Loader.MJCaptchaLoadManager;
import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
import l1j.server.MJCompanion.MJCompanionCommandExecutor;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeLoadManager;
import l1j.server.MJInstanceSystem.Loader.MJInstanceLoadManager;
import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
import l1j.server.MJItemEnchantSystem.MJItemEnchantSystemLoadManager;
import l1j.server.MJKDASystem.MJKDALoadManager;
import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJNetSafeSystem.DriveSafe.MJHddIdChecker;
import l1j.server.MJNetServer.MJClientEntranceService;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJNetServer.ClientManager.MJNSDenialAddress;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJPushitem.Controller.PushItemController;
import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
import l1j.server.MJRankSystem.Loader.MJRankLoadManager;
import l1j.server.MJSurveySystem.MJSurveyFactory;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.MJEPcStatus;
import l1j.server.MJTemplate.MJFiles;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJWhiteIP;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Command.MJCommandService;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoInputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BASECAMP_CHART_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BASECAMP_CHART_NOTI_PACKET.CHART_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHANGE_TEAM_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX.ButtonType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX.IconType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EVENT_COUNTDOWN_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_POLYMORPH_EVENT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SCENE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SERVER_VERSION_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPEED_BONUS_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_USER_START_SUNDRY_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_ENCHANT_START_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_ENCHANT_STAT_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_POINT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.ItemInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ADD_INVENTORY_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.MJTemplate.Spawn.DayAndNight.MJDayAndNightSpawnLoader;
import l1j.server.MJTemplate.Spawn.Normal.MJNormalSpawnLoader;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel;
import l1j.server.Payment.MJPaymentGmHandler;
import l1j.server.Payment.MJPaymentInfo;
import l1j.server.Payment.MJPaymentUserHandler;
import l1j.server.PowerBall.PowerBallController;
import l1j.server.QueenAntSystem.QueenAntController;
import l1j.server.Stadium.StadiumManager;
import l1j.server.TowerOfDominance.DominanceBoss;
import l1j.server.TowerOfDominance.DominanceDataLoader;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv1;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv10;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv11;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv2;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv3;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv4;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv5;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv6;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv7;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv8;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv9;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.clientpackets.C_LoginToServer;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.command.L1Commands;
import l1j.server.server.command.executor.L1AllBuff;
import l1j.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.datatables.AutoLoot;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.MJAlchemyProbabilityBox;
import l1j.server.server.datatables.MJSmeltingProbabilityBox;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.ServerCustomQuestTable;
import l1j.server.server.datatables.ShopBuyLimitInfo;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AuctionBoardInstance;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1GuardInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TeleporterInstance;
import l1j.server.server.model.Warehouse.ClanWarehouse;
import l1j.server.server.model.Warehouse.PrivateWarehouse;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_Chainfo;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatMessageNoti;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_DeleteInventoryItem;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_GMHtml;
import l1j.server.server.serverpackets.S_InvCheck;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_MARK_SEE;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_MoveCharPacket;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_OwnCharPack;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Ping;
import l1j.server.server.serverpackets.S_RangeSkill;
import l1j.server.server.serverpackets.S_Restart;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_ShowCmd;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SpellCheck;
import l1j.server.server.serverpackets.S_SurvivalCry;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TestPacket;
import l1j.server.server.serverpackets.S_Unknown2;
import l1j.server.server.serverpackets.ServerMessage;
import l1j.server.server.templates.CustomQuest;
import l1j.server.server.templates.CustomQuestUser;
import l1j.server.server.templates.L1BoardPost;
import l1j.server.server.templates.L1Command;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.ShopBuyLimit;
import l1j.server.server.templates.eCustomQuestPerformType;
import l1j.server.server.templates.eShopBuyLimitType;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.DeadLockDetector;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.L1QueryUtil;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.MJFullStater;
import l1j.server.server.utils.MJProcessPlayer;
import l1j.server.server.utils.Message;
import l1j.server.server.utils.SQLUtil;

public class GMCommands {

	private static Logger _log = Logger.getLogger(GMCommands.class.getName());
	private static GMCommands _instance;
	public static L1PcInstance _gm;
	private static Random _random = new Random(System.nanoTime());
	public List<L1MonsterInstance> fieldbosslist = new ArrayList<L1MonsterInstance>();

	public static boolean productionCheck;

	public static int ip;

	private GMCommands() {
	}

	public static GMCommands getInstance() {
		if (_instance == null) {
			_instance = new GMCommands();
		}
		return _instance;
	}

	public boolean Stop = true;

	private String complementClassName(String className) {
		if (className.contains(".")) {
			return className;
		}
		return "l1j.server.server.command.executor." + className;
	}

	private boolean executeDatabaseCommandWithoutPermission(L1PcInstance pc, String name, String arg) {
		try {
			L1Command command = L1Commands.get(name);
			if (command == null) {
				return false;
			}
			Class<?> cls = Class.forName(complementClassName(command.getExecutorClassName()));
			L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod("getInstance").invoke(null);
			exe.execute(pc, name, arg);
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, "Gm 指令 錯誤", e);
		}
		return false;

	}

	private boolean executeDatabaseCommand(L1PcInstance pc, String name, String arg) {
		try {
			L1Command command = L1Commands.get(name);
			if (command == null) {
				return false;
			}
			if (pc.getAccessLevel() < command.getLevel()) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(74, "指令 " + name)));
				return true;
			}

			Class<?> cls = Class.forName(complementClassName(command.getExecutorClassName()));
			L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod("getInstance").invoke(null);
			exe.execute(pc, name, arg);

			/** 文件日誌保存 **/
			LoggerInstance.getInstance().addCommand(pc.getName() + ": " + name + " " + arg);
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, " Gm 指令 錯誤", e);
		}
		return false;
	}

	public void handleCommandsWithoutPermission(L1PcInstance gm, String cmdLine) {
		if (gm.getNetConnection() == null || gm.getNetConnection().getAccount() == null || gm.getNetConnection().getAccount().getAccessLevel() != 5048) {
			return;
		}

		StringTokenizer token = new StringTokenizer(cmdLine);
		// 直到第一個空格為止是指令，之後的部分將被視為以空格分隔的参数
		String cmd = token.nextToken();
		String param = "";
		while (token.hasMoreTokens()) {
			param = new StringBuilder(param).append(token.nextToken()).append(' ').toString();
		}
		param = param.trim();

		// 数据库化的指令
		executeDatabaseCommandWithoutPermission(gm, cmd, param);
	}

	private static void test_lfc(L1PcInstance gm, String param) {
		StringTokenizer token = new StringTokenizer(param);
		String name = token.nextToken();
		L1PcInstance pc = L1World.getInstance().findpc(name);
		if (pc == null) {
			gm.sendPackets(String.format("找不到 %s 先生/小姐。", name));
			return;
		}
		L1BoardPost bp = L1BoardPost.createLfc(name, "-", String.format("3 %s", gm.getName()));
		MJLFCCreator.registLfc(gm, 3);
		gm.sendPackets(String.format("已向 %s 先生/小姐提出決鬥請求。", name));
		pc.sendPackets(MJSurveySystemLoader.getInstance().registerSurvey(String.format("%s 先生/小姐已提出決鬥請求。", gm.getName()), bp.getId() + 1000, MJSurveyFactory.createLFCSurvey(), 30 * 1000));
	}

	public void handleCommands(L1PcInstance gm, String cmdLine) {
		StringTokenizer token = new StringTokenizer(cmdLine);
		// 直到第一個空格為止是指令，之後的部分將被視為以空格分隔的參數
		String cmd = "";
		if (token.hasMoreTokens())
			cmd = token.nextToken();
		else
			cmd = cmdLine;
		String param = "";
		while (token.hasMoreTokens()) {
			param = new StringBuilder(param).append(token.nextToken()).append(' ').toString();
		}
		param = param.trim();

		// 已資料庫化的指令
		if (executeDatabaseCommand(gm, cmd, param)) {
			if (!cmd.equalsIgnoreCase(".")) {
				_lastCommands.put(gm.getId(), cmdLine);
			}
			return;
		}

		if (gm.getAccessLevel() < Config.ServerAdSetting.GMCODE) {
			gm.sendPackets(String.valueOf(new S_ServerMessage(74, "指令 " + cmd)));
			return;
		}

		/** 保存文件日志 **/
		LoggerInstance.getInstance().addCommand(gm.getName() + ": " + cmd + " " + param);
		_gm = gm;

		if (MJNCoinCommandComposite.DEFAULT.execute(gm, cmd, param))
			return;

		if (MJCommandService.gmService().execute(gm, cmd, param)) {
			return;
		}

		switch (cmd) {
			case "死亡競賽": {
				DeathMatch open = new DeathMatch(13005);
				open.Start();
				System.out.println("死亡競賽測試開始");
				break;
			}
			case "開始": {
				InfinityBattle.getInstance().Start();
				break;
			}
			case "遊戲開始": {
				InfinityBattle.getInstance().addTeamMembers(gm);
				break;
			}
			case "遊戲結束": {
				InfinityBattle.getInstance().setReady(false);
				break;
			}
			case "幫助": {
				showHelp(gm);
				break;
			}
			case "賦予祝福": {
				blessScrollGive(gm, param);
				break;
			}
			case "檢測": {
				if (!Config.LogStatus.WALKPOSITIONCHECK_LOG) {
					Config.LogStatus.WALKPOSITIONCHECK_LOG = true;
					gm.sendPackets("\f3[檢測] 已開始檢測。請檢查 CMD 窗口（發生偏差）");
				} else {
					Config.LogStatus.WALKPOSITIONCHECK_LOG = false;
					gm.sendPackets("\f3[檢測] 已結束檢測。");
				}
				break;
			}
			case "巨集": {
				macroSetting(gm, param);
				break;
			}
			case "潛力": {
				doll_test(gm, param);
				break;
			}
			case "帳號停權": {
				AccountPause(gm, param);
				break;
			}
			case "螞蟻洞": {
				QueenAntController test = new QueenAntController();
				test.Start();
				break;
			}
			case "封包灌水": {
				gm.sendPackets(new S_TestPacket(1));
				break;
			}
			case "艾恩禮物": {
				addBlessPoint(gm, param);
				break;
			}
			case "艾恩卡片":
				addBlessCard(gm, param);
			case "超級重置": {
				AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(gm.getId());
				Info.set_bless(0);
				Info.set_lucky(0);
				Info.set_vital(0);
				Info.set_invoke(0);
				Info.set_restore(0);
				Info.set_potion(0);
				Info.set_cur_enchant_level(0);
				Info.set_current_stat(0);
				Info.set_total_stat(0);
				Info.set_point(0);
				Info.set_invoke_val_1(0);
				Info.set_invoke_val_2(0);
				Info.set_potion_val_1(0);
				Info.set_potion_val_2(0);
				// XXX 艾恩哈薩德更新後解除註解
//			SC_EINHASAD_POINT_STAT_INFO_NOTI.send(gm, Info);
				break;
			}
			case "ㅡ": {
				//gm.sendPackets(new S_PacketBox(S_PacketBox.FOOD, 224));
			/*SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(293);
			noti.set_duration(-1);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(9653);
			noti.set_off_icon_id(9653);
			noti.set_icon_priority(0);
			noti.set_tooltip_str_id(2156);
			noti.set_new_str_id(2156);
			noti.set_end_str_id(0);
			noti.set_is_good(true);
			gm.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);*/

			/*gm.sendPackets(new S_PacketBox(S_PacketBox.PC방버프, 1600), true);
			gm.sendPackets(new S_ServerMessage(185));*/
				//gm.sendPackets(SC_SCENE_NOTI.make_stream(gm, true), true);
				//SC_USER_START_SUNDRY_NOTI.send(gm, true);
//			gm.getQuest().set_end(L1Quest.QUEST_RING_LEFT_SLOT60);
//			gm.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_LRING95));

			/*gm.getQuest().set_end(L1Quest.QUEST_RING_LEFT_SLOT60);
			gm.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_LRING));*/


//			SC_SPEED_BONUS_NOTI.speed_send(gm, SC_SPEED_BONUS_NOTI.Bonus.eKind.SPELL_SPEED, (int) 50);


//			SC_GOODS_INVEN_NOTI noti = SC_GOODS_INVEN_NOTI.newInstance();
//			SupplementaryService warehouse = WarehouseManager.getInstance().getSupplementaryService(gm.getAccountName());
//			gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3附加服務倉庫有新商品到達。"));
//			noti.set_add_state(warehouse != null && warehouse.getSize() > 0);
//			gm.sendPackets(noti, MJEProtoMessages.SC_GOODS_INVEN_NOTI);

//			SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
//			box.set_button_type(ButtonType.MB_OK);
//			box.set_icon_type(IconType.MB_ICONASTERISK);
//			box.set_message("當前連接量達到飽和狀態，請稍後再試。\n\n通過登錄器進行網頁登錄即可立即連接。\n\n詳細信息請參考官網的「連接方法」頁面。\n\n-梅蒂斯");
//			box.set_title("sadasd");
//			box.set_message_id(0);
//			gm.sendPackets(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt());
//			gm.setMoveDelayRate(70);
//			gm.setAttackDelayRate(0);
//			L1Cooking.eatCooking(gm, 天下壯士Buff, 1800);
//			gm.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, gm, 187, 6));
//			System.out.println(ShopBuyLimitInfo.getInstance().getCharacterList(268452565));
//			SC_CHANGE_TEAM_NOTI_PACKET noti = SC_CHANGE_TEAM_NOTI_PACKET.newInstance();
//			noti.set_object_id(gm.getId());
//			noti.set_object_team_id(1);// 顯示給我的標記
//			gm.sendPackets(noti, MJEProtoMessages.SC_CHANGE_TEAM_NOTI_PACKET, false);

//			gm.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, 0));

//			gm.sendPackets(new S_Paralysis(7, true));

//			gm.set_dead_count(0);
//			gm.setMonsterkill(0);
//			gm.set_exp_count(0);
//			System.out.println(String.format("物品發放次數 : %d, 怪物擊殺數 : %d, 經驗值發放次數 : %d", gm.get_dead_count(), gm.getMonsterkill(), gm.get_exp_count()));
				//gm.sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, gm, 6, 5));

//			SC_SPEED_BONUS_NOTI.send(gm, gm.getId(), SC_SPEED_BONUS_NOTI.Bonus.eKind.MOVE_SPEED);
			/*SC_SPEED_BONUS_NOTI noti = SC_SPEED_BONUS_NOTI.newInstance();
			noti.set_objectnumber(gm.getId());

			Value val1 = Value.newInstance();
			val1.set_kind(SC_SPEED_BONUS_NOTI.Bonus.eKind.ATTACK_SPEED);
			val1.set_value(1000);*/

//			gm.merchantSearchObjid = 0;
//			gm._seal_scroll_count = 0;
//			gm._x = 0;
			/*gm.setFourgear(true);
			ProtoOutputStream SC_FOURTH_GEAR_NOTI_stream = SC_FOURTH_GEAR_NOTI.Fourth_Gear(gm);

			gm.sendPackets(SC_FOURTH_GEAR_NOTI_stream);
			SC_FOURTH_GEAR_NOTI_stream.dispose();*/

//			gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, true));
//			gm.sendPackets(String.format("總獲得數量: %d", gm.insert_count)); // 測試後刪除
//			ProtoOutputStream SC_FOURTH_GEAR_NOTI_stream = SC_FOURTH_GEAR_NOTI.Fourth_Gear(gm);
//			gm.sendPackets(SC_FOURTH_GEAR_NOTI_stream);
//			SC_FOURTH_GEAR_NOTI_stream.dispose();
//			gm.sendPackets(new S_PacketBox(1, 2, true, true));
				//gm.sendPackets(new S_PacketBox(0, 2, true, true));

//			gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, false));
//			AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(gm.getId());
//			Info.set_cur_enchant_level(1);
//			gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
//			gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
				//gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));

				// gm.sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo("", 11));
				// gm.getInventory().storeItem(40308, 1, true);

				// gm.sendPackets(String.format("奇岩城現有累積稅金 %d", MJCastleWarBusiness.getInstance().getPublicMoney(4)));
				// MJCastleWar war = MJCastleWarBusiness.getInstance().get(4);
				// int adena = war.getPublicMoney();
				// gm.getInventory().storeItem(40308, adena);
				// war.addPublicMoney(adena);
				// MJCastleWarBusiness.getInstance().updateCastle(4);
				// gm.sendPackets(String.format("奇岩城目前累積稅金 %d", MJCastleWarBusiness.getInstance().getPublicMoney(4)));

				/*
				 * gm.sendPackets(new S_SkillSound(gm.getId(), 19264, 19)); gm.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 19264, 20));
				 */

				// SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(gm, 32800, 32800, 4, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);

				// gm.sendPackets(new S_DisplayEffect(S_DisplayEffect.TEST_DISPLAY));

				// gm.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 10703, 10));
				// gm.setPoisonEffect(0);
				// gm.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 10705, 10));
				// gm.sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, gm, 1, 30));

				// long curTime = System.currentTimeMillis() / 1000;
				// System.out.println("curTime:" + curTime);

				// gm.sendPackets(new S_DisplayEffect(S_DisplayEffect.LIFECRY_DISPLAY));

				// SC_NOTIFICATION_CHANGE_NOTI noti = SC_NOTIFICATION_CHANGE_NOTI.newInstance();
				// NotificationInfomation info = NotificationInfomation.newInstance();
				// info.set_notification_id(23);
				// info.set_objectid(0);
				// info.set_hyperlink(null);
				// info.set_displaydesc("$32010".getBytes());
				// info.set_startdate(System.currentTimeMillis() / 1000);
				// info.set_enddate((System.currentTimeMillis() + 3600) / 1000);

				// TeleportData td = TeleportData.newInstance();
				// td.set_stringk("$4654".getBytes());
				// td.set_adenacount(1000);
				// info.set_teleport(td);

				// info.set_eventnpc(null);
				// info.set_rest_gauge_icon_display(false);
				// noti.add_change(info);
				// gm.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_CHANGE_NOTI, true);

				// gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));

				// SC_LIMITED_CRAFT_INFO_ACK.send(gm, 5727);
				// gm.set_food(225);
				// gm.sendPackets(new S_PacketBox(S_PacketBox.FOOD, 225));

				// gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));

				// SC_LIMITED_CRAFT_INFO_ACK.send(gm, 103, 1, 2);

				/*
				 * CraftIdInfo test = CraftIdInfo.newInstance(); test.set_craft_id(1); test.set_cur_success_cnt(2); test.set_max_success_cnt(3); gm.sendPackets(test, MJEProtoMessages.SC_LIMITED_CRAFT_INFO_ACK, true);
				 */

				// SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(gm, gm.getX(), gm.getY(), gm.getMapId(), SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_BACK);

				/*
				 * SC_NOTIFICATION_INFO_NOTI noti = SC_NOTIFICATION_INFO_NOTI.newInstance(); noti.set_currentpagecount(1); noti.set_maxpagecount(1);
				 *
				 * NotificationInfomation nInfo = NotificationInfomation.newInstance(); nInfo.set_notification_id(17); nInfo.set_hyperlink("http://www.naver.com".getBytes()); nInfo.set_displaydesc("$26237".getBytes()); nInfo.set_startdate(System.currentTimeMillis() /
				 * 1000); nInfo.set_enddate((System.currentTimeMillis() + 3600) / 1000);
				 *
				 * TeleportData data = TeleportData.newInstance(); data.set_adenacount(300); data.set_stringk("4654".getBytes()); nInfo.set_teleport(data);
				 *
				 * EventNpcData npc = EventNpcData.newInstance(); EventNpcInfo npcInfo = EventNpcInfo.newInstance(); npcInfo.set_displaydesc("$30144".getBytes()); npcInfo.set_npc_id(gm.getId()); npc.add_eventinfo(npcInfo); nInfo.set_eventnpc(npc);
				 * noti.add_notificationinfo(nInfo); npcInfo.set_rest_gauge_bonus_display(1); gm.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_INFO_NOTI);
				 */

				/*
				 * SC_NOTIFICATION_INFO_NOTI noti = SC_NOTIFICATION_INFO_NOTI.newInstance(); noti.set_currentpagecount(1); noti.set_maxpagecount(1);
				 *
				 * NotificationInfomation nInfo = NotificationInfomation.newInstance(); nInfo.set_notification_id(17); nInfo.set_hyperlink("http://www.naver.com".getBytes()); nInfo.set_displaydesc("$26237".getBytes()); nInfo.set_new(false);
				 * nInfo.set_startdate(System.currentTimeMillis() / 1000); nInfo.set_enddate((System.currentTimeMillis() + 3600) / 1000);
				 */

				/*
				 * for (L1ItemInstance item : gm.getInventory().getItems()) { if (item.getItemId() == 59) { //L1PcInstance pc, item, SC_ENCHANT_RESULT.eResult boolean bm, int amount, int effect, boolean high, boolean element SC_ENCHANT_RESULT.send(gm, item,
				 * SC_ENCHANT_RESULT.eResult.SUCCESS, true, 1, 1, false, false); break; } }
				 */

				// SC_ENCHANT_RESULT pck = SC_ENCHANT_RESULT.newInstance();
				// L1Item temp = ItemTable.getInstance().getTemplate(59);
				// pck.set_object_id(gm.getId());
				// pck.set_bless(1);
				// pck.set_enchant_result(SC_ENCHANT_RESULT.eResult.SUCCESS);
				// pck.set_bm_scroll(0);
				// pck.set_enchant_amount(1);
				// pck.set_special_enchant_effect(0);
				// pck.set_high_enchant(true);
				// pck.set_is_element_enchant(true);
				// gm.sendPackets(pck, MJEProtoMessages.SC_ENCHANT_RESULT, true);

				/*
				 * SC_EVENT_COUNTDOWN_NOTI_PACKET pck = SC_EVENT_COUNTDOWN_NOTI_PACKET.newInstance(); pck.set_remain_time(10); pck.set_timer_type(eType.CONTENTS_TIMER); try { pck.set_event_desc("\\fW$12125".getBytes("MS949")); } catch (UnsupportedEncodingException e) {
				 * e.printStackTrace(); } gm.sendPackets(pck, MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET, true);
				 */

				/*
				 * SC_CHAT_MESSAGE_NOTI_PACKET noti = SC_CHAT_MESSAGE_NOTI_PACKET.newInstance(); noti.set_time_t64(0L); noti.set_type(ChatType.CHAT_NORMAL); noti.set_message("hello".getBytes()); noti.set_name(gm.isGm() ? "******".getBytes(MJEncoding.MS949) :
				 * gm.getName().getBytes(MJEncoding.MS949)); int step = gm.getRankLevel(); if (step != 0) { noti.set_ranker_rating(step); } noti.set_is_server_keeper(true); gm.sendPackets(noti, MJEProtoMessages.SC_CHAT_MESSAGE_NOTI_PACKET);
				 */

				/*
				 * SC_VOICE_CHAT_ROOM_INFO_NOTI noti = SC_VOICE_CHAT_ROOM_INFO_NOTI.newInstance(); noti.set_roomKey(0); noti.set_serverKey(0); noti.set_gameRoomId(0); noti.set_roomType(eChatRoomType.NONE); gm.sendPackets(noti,
				 * MJEProtoMessages.SC_VOICE_CHAT_ROOM_INFO_NOTI, true);
				 */

				/*
				 * SC_USER_PLAY_INFO_NOTI info2 = SC_USER_PLAY_INFO_NOTI.newInstance(); MAP_TIME_LIMIT_INFO info3 = MAP_TIME_LIMIT_INFO.newInstance(); info3.set_description("didididi".getBytes()); info3.set_time_limit_serial(0); info3.set_time_limit_stay(100);
				 * info3.set_time_remained(100); info2.add_map_time_limit_info(info3); CHARGED_TIME_MAP_INFO info = CHARGED_TIME_MAP_INFO.newInstance(); info.set_charged_count(1); info.set_charged_time(1800); info.set_max_charge_count(1);
				 * info2.set_charged_time_map_info(info); gm.sendPackets(info2, MJEProtoMessages.SC_USER_PLAY_INFO_NOTI, true);
				 */

				// CHARGED_TIME_MAP_INFO.send(gm, 1, 2, 3);

				// L1World.getInstance().broadcastPacketToAll(SC_MESSAGE_NOTI.newDropMessage(386, "成功獲得長袍。"), true);

				// 超級屬性移動封包 0號（連接原型時客戶端自動）
				// SC_EINHASAD_POINT_ENCHANT_START_ACK.send(gm, 0);

				// 屬性窗口：最後一項是屬性窗口中剩餘的提醒點數 - 這個指令
				// SC_EINHASAD_POINT_ENCHANT_STAT_ACK.send(gm, 10, 20, 5, 50);

				// 提醒點數因為這個整數值而減少。 -> 當投資超級屬性時減少。（看起來像是屬性窗口？）
				// SC_EINHASAD_POINT_STAT_INVEST_ACK.send(gm, 3, 3, 3);

				// 用於卡片抽取窗口內（屬性1, 2, 3/剩餘點數/愛因哈薩德點數）
				// SC_EINHASAD_POINT_STAT_INFO_NOTI.send(gm, 1, 2, 3, 4, 5, 100000);

				// SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(gm);
				// SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(gm, 1, gm.getType());

				/*
				 * SC_PK_MESSAGE_AT_BATTLE_SERVER noti = SC_PK_MESSAGE_AT_BATTLE_SERVER.newInstance(); noti.set_interkind(0); noti.set_killer_name("메티스"); noti.set_killer_cache_no(1); noti.set_die_name("빠이"); noti.set_die_cache_no(2); noti.set_killer_name_str(0);
				 * gm.sendPackets(noti, MJEProtoMessages.SC_PK_MESSAGE_AT_BATTLE_SERVER, true);
				 */

				/*
				 * for(L1PcInstance pc : L1World.getInstance().getAllPlayers()) { L1SkillId.onHolyWalkHandler(pc); }
				 */

				/*
				 * CraftListLoader.getInstance().updateLimitItem(); CraftListLoader.reload();
				 */
				/*
				 * SC_SPELL_PASSIVE_ONOFF_ACK noti = SC_SPELL_PASSIVE_ONOFF_ACK.newInstance(); noti.set_passive_id(38); noti.set_onoff(true); noti.set_result(eRES.eRES_OK); gm.sendPackets(noti, MJEProtoMessages.SC_SPELL_PASSIVE_ONOFF_ACK, true);
				 */

				// SC_ALL_SPELL_PASSIVE_NOTI noti = SC_ALL_SPELL_PASSIVE_NOTI.newInstance();
				// noti.add_passives(43, 8);
				// gm.sendPackets(noti, MJEProtoMessages.SC_ALL_SPELL_PASSIVE_NOTI, true);
				// SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(gm);
				// L1World.getInstance().broadcastPacketToAll(SC_MESSAGE_NOTI.newDropMessage(386, "成功獲得長袍。"), true);
				/*
				 * gm.sendPackets(new S_SkillSound(gm.getId(), 11731)); Broadcaster.broadcastPacket(gm, new S_SkillSound(gm.getId(), 11731));
				 */
				// gm.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(gm, 0, false));
				// SC_NOTIFICATION_INFO_NOTI.onEventTick(gm, 3000L);
				// gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PHANTOM, false));
				// gm.getAccount().addBlessOfAin((100 * 10000), gm, "怪物");
				// gm.sendPackets(new S_ACTION_UI(1000 / 10000)); // 可能是愛因祝福效果？
				// gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));

				// MJBeginnerControllerProvider.provider().clientController().onStart(gm, 273);
				// remove
				// gm.removeSkillEffect(L1SkillId.EINHASAD_GREAT_FLAT);

				// SC_SPELL_DELAY_NOTI.UseSkillDelay(gm, 1000);

				/*
				 * SC_SPELL_DELAY_NOTI noti = SC_SPELL_DELAY_NOTI.newInstance(); noti.set_next_spell_delay(10); noti.set_next_spell_global_delay(999); noti.set_spell_group_id(0); gm.sendPackets(noti, MJEProtoMessages.SC_SPELL_DELAY_NOTI, true);
				 */

				/*
				 * SC_SPELL_DELAY_NOTI not = SC_SPELL_DELAY_NOTI.newInstance(); not.set_next_spell_delay(10000);
				 */

				// L1PcInstance pc = L1World.getInstance().getPlayer("浩");
				// pc.sendPackets(new S_Invis(pc.getId(), 0));
				// pc.broadcastPacket(new S_Invis(pc.getId(), 0));
				// S_Sound ss = new S_Sound(147);
				// pc.sendPackets(ss, false);
				// pc.broadcastPacket(ss, true);

				/*
				 * for(L1PcInstance pc : L1World.getInstance().getAllPlayers()) { L1SkillId.onHolyWalkHandler(pc); //pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.END)); }
				 */

				/*
				 * SC_NOTIFICATION_MESSAGE_NOT not = SC_NOTIFICATION_MESSAGE_NOT.newInstance(); not.set_duration(1); not.set_messageRGB(MJSimpleRgb.red()); not.set_notificationMessage(param); not.set_suffileNumber(38 * 2); gm.sendPackets(not,
				 * MJEProtoMessages.SC_NOTIFICATION_MESSAGE_NOT);
				 */

				/*
				 * for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){ pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START)); }
				 */
				/*
				* SC_REVENGE_INFO_NOTI noti = SC_REVENGE_INFO_NOTI.newInstance();
				* RevengeInfoT rInfo = RevengeInfoT.newInstance();
				* rInfo.set_action_count(3); // 挑釁次數
				* rInfo.set_action_duration(3 + 3600); // 追蹤剩餘時間
				* rInfo.set_action_remain_count((3 + 2)); // 追蹤剩餘次數
				* rInfo.set_action_result(MJRnd.isBoolean() ? eResult.LOSE : eResult.WIN); // 勝敗
				* rInfo.set_action_timestamp((int)(System.currentTimeMillis() / 1000)); // 之前執行動作的時間（秒）
				* rInfo.set_action_type(eAction.PURSUIT); // eAction.PURSUIT 追蹤, eAction.TAUNT 挑釁
				* rInfo.set_active(true); // 在線狀態
				* rInfo.set_crimescene_server_no(2);
				* rInfo.set_game_class(MJRnd.next(8)); // 職業
				* rInfo.set_pledge_id(1); // 血盟ID
				* rInfo.set_pledge_name("ccc"); // 血盟名稱
				* rInfo.set_register_timestamp((int)(System.currentTimeMillis() / 1000) - 3600); // 發生時間（秒）
				* rInfo.set_server_no(1);
				* rInfo.set_unregister_duration(1); // 預計刪除時間（秒）
				* rInfo.set_user_name("asd"); // 角色名稱

				/*
				 * SC_WHOUSER_NOTI_PACKET noti = SC_WHOUSER_NOTI_PACKET.newInstance(); noti.set_currentusercount(L1World.getInstance().getAllPlayersSize()); for(L1PcInstance pc : L1World.getInstance().getAllPlayers()) { SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO uInfo =
				 * SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO.newInstance(); SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO aInfo = SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO.newInstance(); aInfo.set_accountname(pc.getAccountName()); aInfo.set_ip("asd"); aInfo.set_ipkind(0);
				 * aInfo.set_location(pc.getLongLocation()); aInfo.set_worldnumber(4); uInfo.set_accountinfo(aInfo); uInfo.set_alignstr("asdaa"); uInfo.set_pledge("norm"); uInfo.set_serverno(0); uInfo.set_title("aaa"); uInfo.set_username(pc.getName());
				 * noti.add_whouserinfo(uInfo); } gm.sendPackets(noti, MJEProtoMessages.SC_WHOUSER_NOTI_PACKET);
				 */

				// gm.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON, 73, true));// 無限封包
				// gm.sendPackets(new S_Liquor(gm.getId(), 8));
				/*
				 * byte[] buff = new byte[] {(byte)0xdc, (byte)0xb4, (byte)0x98, (byte)0xb7, (byte)0xe4, (byte)0xac, (byte)0x58, (byte)0xc7, (byte)0x20, (byte)0x00, (byte)0xc5, (byte)0xc9, (byte)0xfc, (byte)0xc8, (byte)0x20, (byte)0x00, (byte)0xc1, (byte)0xc0,
				 * (byte)0x90, (byte)0xc7, (byte)0x00, (byte)0x00, (byte)0x00};
				 *
				 * System.out.println(new String(buff, MJEncoding.EUCKR));
				 */

				// System.out.println(System.currentTimeMillis() + (3600 * 24 * 7));
				/*
				 * try { if(HikariSourceInfo.defaultSource != null) { HikariSourceInfo.defaultSource.getHikariConfigMXBean(). setLeakDetectionThreshold(3000); HikariSourceInfo.defaultSource.getHikariConfigMXBean().setConnectionTimeout( 250); } Connection con =
				 * L1DatabaseFactory.getInstance().getConnection(); for(int i=0; i<5; ++i) { gm.sendPackets(String.valueOf(i)); Thread.sleep(1000); } SQLUtil.close(con); } catch (Exception e) { e.printStackTrace(); }
				 */

				/*
				 * for(L1ItemInstance item : gm.getInventory().getItems()) { System.out.println(item.getName() + " " + item.getItemId()); }
				 */
				// L1EffectSpawn.getInstance().spawnEffect(81162, 3500 + 10,
				// gm.getX(),gm.getY(), gm.getMapId());
				// gm.sendPackets(new S_ChangeName(gm.getId(), "abc"));
				// gm.sendPackets(new S_CharTitle(gm.getId(), "abc"));
				/*
				 * SC_HYPERTEXT sc = SC_HYPERTEXT.newInstance(); sc.set_url("magos"); for(int i=0; i<5; ++i) { String s = i +""; sc.add_text(s); sc.add_text(s); sc.add_text(s); } //sc.add_text(""); gm.sendPackets(sc, MJEProtoMessages.SC_HYPERTEXT);
				 */
				/*
				 * L1PcInstance pc = L1World.getInstance().getPlayer("기사"); gm.sendPackets(new S_SkillHaste(pc.getId(), 2, 1800)); gm.broadcastPacket(new S_SkillHaste(pc.getId(), 2, 0));
				 */

				// L1SkillUse.on_icons(pc, L1SkillId.FOG_OF_SLEEPING, 10);
				// gm.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(gm));
				// gm.broadcastPacket(new S_Invis(gm.getId(), 1));
				// MJClientEntranceService.service().useWaitQueue()
				// gm.send_effect(3942);
				// gm.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 11772, 60));
				// gm.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 3942, 3600));
				// gm.sendPackets(new S_TestPacket(1));
				// gm.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 293, true));
				// gm.sendPackets(new S_LetterList(C_MailBox.WRITE_BLOODPLEDGE_MAIL, true));
				// gm.sendPackets(new S_LetterList(gm, 65, 0, true));

				/*
				 * double remainExp = ExpTable.getExpByLevel(84) * 1; int i = 0; while(remainExp > 0) { int currentLevel = gm.getLevel() + i; double currentPenalty = ExpTable.getPenaltyRate(currentLevel); System.out.println(currentLevel);
				 *
				 * double needExp = ExpTable.getNeedExpNextLevel(currentLevel); needExp = Math.min(needExp / currentPenalty, remainExp); remainExp -= needExp;
				 *
				 * gm.sendPackets(currentLevel + " " + (long)(needExp * currentPenalty)); gm.add_exp((long)(needExp * currentPenalty)); ++i; }
				 */
				/*
				 * for(L1Object obj : L1World.getInstance().getVisibleObjects(gm, 30)) { if(obj == null) { continue; } gm.sendPackets(new S_HPMeter(obj.getId(), 100, 100)); } System.out.println("ended");
				 */

				/*
				 * SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance(); noti.set_noti_type(eNotiType.RESTAT); noti.set_spell_id(4066); noti.set_duration(-1); noti.set_duration_show_type(eDurationShowType.TYPE_EFF_EINHASAD_FAVOR); noti.set_on_icon_id(7235);
				 * noti.set_off_icon_id(0); noti.set_icon_priority(-1); noti.set_tooltip_str_id(5402); noti.set_new_str_id(5402); noti.set_end_str_id(0); noti.set_is_good(true); gm.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
				 */

				// gm.RootMent = !gm.RootMent;
				// gm.sendPackets(gm.RootMent + "");
				// gm.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(gm, 1, true));

				// gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
				// String.format("[愛因哈薩德訂閱剩餘時間] %s訂閱期間可使用龍之寶石提升愛因等級",
				// MJString.remainTimeString(86400))));
				// gm.sendPackets(new S_SkillIconGFX(15, 0));
				// gm.sendPackets(new S_AddSkill(255, 255, 127, 255, 255, 255, 0, 0, 0, 0, 0, 0,
				// 0, 0, 0, 0, 127, 3, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				// gm.start_teleport(gm.getX() - 1, gm.getY() - 1, gm.getMapId(),
				// gm.getHeading(), 169, false, false);
				// L1SpawnUtil.spawn4(gm.getX(), gm.getY(), gm.getMapId(), 6, 50000261, 0, 0,
				// 0);
				// SC_ARENA_GAME_INFO_NOTI.send_info(gm, 60 * 15);
				// SC_ARENA_PLAY_STATUS_NOTI.time_send(gm, 60 * 15, true);
				// SC_ARENA_PLAY_STATUS_NOTI.end_time_send(gm);
				// SC_ARENA_PLAY_EVENT_NOTI.sendRestartLock(gm);
				// SC_ARENA_GAME_INFO_NOTI.send_info(gm, 60);
				/*
				 * SC_BLOODPLEDGE_USER_INFO_NOTI noti = SC_BLOODPLEDGE_USER_INFO_NOTI.newInstance(); noti.set_bloodpledge_name(new byte[] { 0x00 }); noti.set_rank(0); gm.sendPackets(noti, MJEProtoMessages.SC_BLOODPLEDGE_USER_INFO_NOTI);
				 */
				// SC_BLOOD_PLEDGE_JOIN_ACK.ack(gm,
				// ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_JOIN_OK);
				// gm.sendPackets(SC_BLOOD_PLEDGE_JOIN_OPTION_ACK.sendJoinSetting(gm,
				// gm.getClan().getJoinSetting(), gm.getClan().getJoinType()));
				// gm.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 0),
				// true);
				// gm.sendPackets(SC_BLOOD_PLEDGE_JOIN_OPTION_ACK.sendJoinSetting(2, false, 3));
				// gm.sendPackets(new S_ServerMessage(3561));
				// gm.sendPackets("重量指數超過82%，無法進行攻擊。");
				// gm.sendPackets(new S_OwnCharStatus(gm));
				/*
				 * L1ItemInstance item = null; item = ItemTable.getInstance().createItem(61); SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(gm.getAccountName()); pwh.storeTradeItem(item);
				 */
				// gm.sendPackets(S_RepairItem.get(gm));
				// gm.ShapePolyRingMaster();
				/*
				 * for(GameClient clnt : MJNSHandler.getClients()) { if(clnt.getActiveChar() != null) { SC_SPELL_LATE_HANDLING_NOTI.send(clnt.getActiveChar(), true, eLevel.NOT_CORRECTION); } }
				 */

				/*
				* SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
				* box.set_button_type(ButtonType.MB_OK);
				* box.set_icon_type(IconType.MB_ICONASTERISK);
				* box.set_message("當前連接量已達飽和狀態。請重試。\n\n您可以在連接器上進行網頁登錄後直接連接。\n\n詳情請參考官網的 '連接方法'。\n\n-梅蒂斯"
				* );
				* box.set_title(Config.Login.GameServerName);
				* box.set_message_id(0);
				* gm.sendPackets(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt());
				*/

				// L1SkillUse.on_icons(gm, L1SkillId.EXP_POTION, 1000 * 1000);
				// gm.send_effect(gm.getMap().isSafetyZone(gm.getX(), gm.getY()), 134, 1000);

				// gm.sendPackets(new S_TestPacket(gm.getId(),0,0,""));
				// gm.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.ICON_BUFF, 8228, true, 5006,
				// 1800));
				/*
				 * SC_POLYMORPH_NOTI noti = SC_POLYMORPH_NOTI.newInstance(); noti.set_objId(gm.getId()); noti.set_sprite(16284); noti.set_action(0); gm.sendPackets(noti, MJEProtoMessages.SC_POLYMORPH_NOTI);
				 */
				// gm.send_effect(12753, 1000);
				/*
				 * byte[] buff = new byte[200]; for (int i = 0; i < 200; ++i) buff[i] = 127; gm.sendPackets(new S_UnityIcon(buff));
				 */
				/*
				 * for(int i=0; i<255; ++i) { gm.sendPackets(new S_SkillIconGFX(35, i, true)); gm.sendPackets(i); try { Thread.sleep(100); } catch (InterruptedException e) { // TODO Auto-generated catch block e.printStackTrace(); } }
				 */
				// gm.sendPackets(new S_SkillIconGFX(36, 0, 0, 0));
				/*
				 * for(GameClient clnt : MJNSHandler.getClients()) {
				 *     System.out.println(param);
				 *     clnt.sendPacket(new S_CommonNews("屬性重置已完成。"));
				 * }
				 */

				// gm.sendPackets(S_EnchantMessage.newBlueMessage(9, 2804, ""));
				// gm.sendPackets(new S_Message_YN(gm.getId(), 180, ""));
				/*
				 * for(int i=0; i<255; ++i) { gm.sendPackets(new S_SkillIconGFX(35, i, true)); gm.sendPackets(i); try { Thread.sleep(100); } catch (InterruptedException e) { // TODO Auto-generated catch block e.printStackTrace(); } }
				 */
				// System.out.println(gm.totalBonusStats());
				// System.out.println(gm.getLevel() - 49);
				// gm.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 293, false));
				// gm.sendPackets(new S_ACTION_UI(S_ACTION_UI.PCBANG_SET, true));
				// gm.sendPackets(S_ShowCmd.getPkMessageAtBattleServer("2323", "3434"));
				// gm.sendPackets(new S_BuilderPacket(0xfe, new byte[] {(byte)0x32, (byte)0x02,
				// (byte)0x01, (byte)0x30, (byte)0x00 }));
				// gm.sendPackets(new S_ServerMessage(403, "$5240 (1000)")); // %0
				// gm.sendPackets(new S_ServerMessage(563)); // %0
				/*
				 * for(int i=0; i<256; ++i) { for(int j=0; j<256; ++j) { //byte[] buff = new byte[] {(byte)0x51, (byte)0x08, (byte)0x02, (byte)0x03, (byte)0x00, (byte)0x08, (byte)0x80, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}; byte[] buff = new byte[]
				 * {(byte)0x51, (byte)0x0c, (byte)0x02, (byte)0x03, (byte)0x00, (byte)0x08, (byte)0x80, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}; buff[6] = (byte)j; buff[7] = (byte)i; MJEProtoMessages.existsProto(gm.getNetConnection(), buff); } }
				 *
				 *
				 * SC_NOTIFICATION_MESSAGE noti = SC_NOTIFICATION_MESSAGE.newInstance(); noti.set_desc("asdasd"); noti.set_duration(5); noti.set_messageRGB(MJSimpleRgb.red()); noti.set_pos(display_position.screen_middle); noti.set_serverno(1); gm.sendPackets(noti,
				 * MJEProtoMessages.SC_NOTIFICATION_MESSAGE);
				 */
				/*
				 * SC_CHAT_MESSAGE_NOTI_PACKET noti = SC_CHAT_MESSAGE_NOTI_PACKET.newInstance(); noti.set_time_t64(0L); noti.set_type(ChatType.CHAT_TRADE); noti.set_message("hello".getBytes()); noti.set_name(gm.isGm() ? "******".getBytes(MJEncoding.UTF-8) :
				 * gm.getName().getBytes(MJEncoding.UTF-8)); int step = gm.getRankLevel(); if(step != 0){ noti.set_ranker_rating(step); } noti.set_is_server_keeper(true); gm.sendPackets(noti, MJEProtoMessages.SC_CHAT_MESSAGE_NOTI_PACKET);
				 */
			}
			// gm.sendPackets("\aA■ 血盟團隊突襲排名已更新 ■");
			/*
			 * cal.setTimeInMillis(1483196401293L); System.out.println(MJFormatter.get_tdouble_formatter_time(cal));
			 *
			 * cal.set(Calendar.YEAR, 2019); cal.set(Calendar.MONTH, 0); cal.set(Calendar.DAY_OF_MONTH, 2); cal.set(Calendar.HOUR_OF_DAY, 16); cal.set(Calendar.MINUTE, 7); cal.set(Calendar.SECOND, 31); System.out.println(cal.get(Calendar.MONTH));
			 * System.out.println(cal.getTimeInMillis());
			 */
			// System.out.println(MJFormatter.get_tdouble_formatter_time(cal));
			// gm.sendPackets(new S_TrueTargetNew(gm.getId(), false));
			/*
			 * if(gm.getParty() != null) { gm.getParty().refreshPartyList(); }
			 */
			// gm.sendPackets(new S_SkillIconAura(114, 1200));
			// System.out.println(new String(new byte[] {(byte)0xBE, (byte)0xC8, (byte)0xB3,
			// (byte)0xE7, (byte)0xC7, (byte)0xCF, (byte)0xBC, (byte)0xBC, (byte)0xBE,
			// (byte)0xE4, (byte)0x00, (byte)0x77, (byte)0x76, (byte)0xE3},
			// MJEncoding.UTF-8));
			// gm.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 596, true));
			// executeitem(gm, param, param);
			break;
			case "N幣充值": {
				insertCashInfo(gm, param);
				break;
			}
			case "購買限制": {
				BuyLimitSystem(gm, param);
				break;
			}
			case "解除封印申請": {
				Sealedoff(gm, param);
				break;
			}
			case "生成地圖": {
				MapSpawnReload(gm, param);
				break;
			}
			case "等待隊列": {
				useWaitQueue(gm, param);
				break;
			}
			case "地圖玩家":
				mapwho(gm, param);
				break;
			case "公告":
				NoticeChat(gm, param);
				break;
			case "全體跳躍":
				allUserInterJump(gm, param);
				break;
			case "跳躍":
				InterJump(gm, param);
				break;
			case "用戶跳躍":
				UserInterJump(gm, param);
				break;
			//case "強力球":
			//	PowerBall(gm, param);
			//	break;
			case "開關":
				try {
					if (Config.Login.SERVERSTANDBY) {
						Config.Login.SERVERSTANDBY = false;
						gm.sendPackets("無法進行伺服器登錄。");
					} else {
						Config.Login.SERVERSTANDBY = true;
						gm.sendPackets("伺服器登錄可行。");
					}
				} catch (Exception e) {
					gm.sendPackets(".開關 開/關");
				}
			case "白名單IP":
				try {
					if (MJString.isNullOrEmpty(param)) {
						throw new Exception();
					}
					MJWhiteIP.getInstance().put(param);
					MJNSDenialAddress.getInstance().delete_denials_address(param);
				} catch (Exception e) {
					gm.sendPackets(".白名單IP [IP地址](例如 127.0.0.1)");
				}
				break;
			case "優惠券註冊":
				registerCoupon(gm, param);
				break;
			case "應用中心":
				appcenter(gm, param);
				break;
			case "檢查背包":
				inventoryAskCommands(gm, param);
				break;
			case "重置機率":
				initialize_user_private_probability(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "機率":
				update_user_private_probability(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "確認機率":
				show_user_private_probability(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "測試":
				// gm.sendPackets(new S_SystemMessage()
				break;
			case "優惠券": // 代碼
				MJPaymentGmHandler.do_execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "代碼充值":
				MJPaymentUserHandler.do_execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "寵物":
				MJCompanionCommandExecutor.exec(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "現金經驗值":
				do_cache_exp(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "對抗戰":
				new MJShiftBattleCommandExecutor().execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "伺服器移動":
				if (Config.Login.UseShiftServer)
					new MJShiftObjectCommandExecutor().execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "版本輸出": {
				try {
					SC_SERVER_VERSION_INFO info = (SC_SERVER_VERSION_INFO) SC_SERVER_VERSION_INFO.newInstance().copyInstance();
//				byte[] bytes = MJFiles.readAllBytes("./data/version.dat");
//				info.readFrom(ProtoInputStream.newInstance(bytes, 3, bytes.length));
//				if (!info.isInitialized()){
//					throw new IllegalArgumentException(String.format("fail initialized version data.(SC_SERVER_VERSION_INFO) %d", info.getInitializeBit()));
//				}
					info.do_print();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case "test123": {
				Collection<L1Object> o = L1World.getInstance().getVisibleObjects(gm, 10);
				ArrayList<L1Character> list = new ArrayList<>();
				for (L1Object o1 : o) {
					if (!(o1 instanceof L1Character)) {
						continue;
					}
					list.add((L1Character) o1);
				}
				S_RangeSkill s = new S_RangeSkill(gm, list.toArray(new L1Character[0]), 171, 18, S_RangeSkill.TYPE_DIR);
				gm.sendPackets(s);
				// testCommand(gm, param);

				// InterRaceProvider.loadProvider();
				// gm.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 450, true));

				/*
				 * SC_ARENA_GAME_INFO_NOTI noti = SC_ARENA_GAME_INFO_NOTI.newInstance(); noti.set_round(1); noti.set_map_kind(eArenaMapKind.OrimLab_Minor); noti.set_result_display_time_sec(0); noti.set_round_time_sec(60); gm.sendPackets(noti,
				 * MJEProtoMessages.SC_ARENA_GAME_INFO_NOTI);
				 *
				 * SC_ARENA_PLAY_STATUS_NOTI noti2 = SC_ARENA_PLAY_STATUS_NOTI.newInstance(); ArenaGameStatus status = ArenaGameStatus.newInstance(); status.set_is_timer_run(true); status.set_observer_count(0); status.set_play_time_msec(60);
				 *
				 * RoundScoreBoard board = RoundScoreBoard.newInstance(); RoundScore score = RoundScore.newInstance(); score.set_attack_amount(0); score.set_is_win(false); board.add_team_a(score); status.set_round_score_board(board);
				 *
				 * TeamStatus team_status = TeamStatus.newInstance(); team_status.set_cheer_msg_count(0); team_status.set_team_id(eArenaTeam.TEAM_A); team_status.set_team_kill_count(0); status.add_team_status(team_status); noti2.set_game_status(status);
				 * gm.sendPackets(noti2, MJEProtoMessages.SC_ARENA_PLAY_STATUS_NOTI);
				 *
				 * SC_ARENA_PLAY_EVENT_NOTI noti3 = SC_ARENA_PLAY_EVENT_NOTI.newInstance(); noti3.set_arena_char_id_causer(gm.getId()); noti3.set_arena_char_id_target(0); noti3.set_spell_id(0); noti3.set_team_id(eArenaTeam.TEAM_A); noti3.set_type(eType.YourSelfEntered);
				 * gm.sendPackets(noti3, MJEProtoMessages.SC_ARENA_PLAY_EVENT_NOTI);
				 *
				 * noti3 = SC_ARENA_PLAY_EVENT_NOTI.newInstance(); noti3.set_type(eType.GameCountDown10Sec); gm.sendPackets(noti3, MJEProtoMessages.SC_ARENA_PLAY_EVENT_NOTI);
				 */
				/*
				 * SC_ARENA_GAME_INFO_NOTI noti = SC_ARENA_GAME_INFO_NOTI.newInstance(); ArenaActorInfo aInfo = ArenaActorInfo.newInstance(); aInfo.set_arena_char_id(1); aInfo.set_character_class(CharacterClass.MAGICIAN);
				 * aInfo.set_character_name(gm.getName().getBytes()); aInfo.set_gender(Gender.MALE); aInfo.set_marker_id(0); aInfo.set_role(eRole.Player); aInfo.set_server_id(0); aInfo.set_team_id(eArenaTeam.TEAM_A); noti.add_player_info(aInfo); noti.set_round(1);
				 * noti.set_map_kind(eArenaMapKind.OrimLab_Minor); noti.set_result_display_time_sec(0); noti.set_round_time_sec(60); gm.sendPackets(noti, MJEProtoMessages.SC_ARENA_GAME_INFO_NOTI);
				 *
				 * SC_ARENA_PLAY_STATUS_NOTI noti2 = SC_ARENA_PLAY_STATUS_NOTI.newInstance(); ArenaGameStatus status = ArenaGameStatus.newInstance(); status.set_is_timer_run(true); status.set_observer_count(0); status.set_play_time_msec(60);
				 *
				 * RoundScoreBoard board = RoundScoreBoard.newInstance(); RoundScore score = RoundScore.newInstance(); score.set_attack_amount(0); score.set_is_win(false); board.add_team_a(score); status.set_round_score_board(board);
				 *
				 * TeamStatus team_status = TeamStatus.newInstance(); team_status.set_cheer_msg_count(0); team_status.set_team_id(eArenaTeam.TEAM_A); team_status.set_team_kill_count(0); status.add_team_status(team_status);
				 */
				/*
				 * ArenaPlayerStatus player = ArenaPlayerStatus.newInstance(); player.set_arena_char_id(1); player.set_attack_amount(0); player.set_damaged_amount(0); player.set_death_count(0); player.set_heal_amount(0); player.set_hp_ratio(100);
				 * player.set_is_live(true); player.set_is_ready(true); player.set_kill_count(1); player.set_loc_x(gm.getX()); player.set_loc_y(gm.getY()); player.set_mp_ratio(100); player.set_obj_id(gm.getId()); player.set_paralysed(false); player.set_poisoned(false);
				 * noti2.add_player_status(player); noti2.set_game_status(status); gm.sendPackets(noti2, MJEProtoMessages.SC_ARENA_PLAY_STATUS_NOTI);
				 */

				/*
				 * noti = SC_ARENA_GAME_INFO_NOTI.newInstance(); aInfo = ArenaActorInfo.newInstance(); aInfo.set_arena_char_id(1); aInfo.set_character_class(CharacterClass.MAGICIAN); aInfo.set_character_name(gm.getName().getBytes()); aInfo.set_gender(Gender.MALE);
				 * aInfo.set_marker_id(1); aInfo.set_role(eRole.Player); aInfo.set_server_id(0); aInfo.set_team_id(eArenaTeam.TEAM_A); noti.add_player_info(aInfo); noti.set_round(1); noti.set_map_kind(eArenaMapKind.OrimLab_Minor); noti.set_result_display_time_sec(0);
				 * noti.set_round_time_sec(60); gm.sendPackets(noti, MJEProtoMessages.SC_ARENA_GAME_INFO_NOTI);
				 *
				 */
				/*
				 * SC_ARENA_PLAY_EVENT_NOTI noti3 = SC_ARENA_PLAY_EVENT_NOTI.newInstance();
				 *
				 * ArenaActorInfo aInfo11 = ArenaActorInfo.newInstance();
				 *
				 * aInfo11.set_arena_char_id(1); aInfo11.set_character_class(CharacterClass.MAGICIAN); aInfo11.set_character_name(gm.getName().getBytes()); aInfo11.set_gender(Gender.MALE); aInfo11.set_marker_id(0); aInfo11.set_role(eRole.Player);
				 * aInfo11.set_server_id(0); aInfo11.set_team_id(eArenaTeam.TEAM_A); noti3.set_actor_info(aInfo11); noti3.set_arena_char_id_causer(gm.getId()); noti3.set_arena_char_id_target(0); noti3.set_team_id(eArenaTeam.TEAM_A);
				 * noti3.set_type(eType.PlayerSpellSucceded); gm.sendPackets(noti3, MJEProtoMessages.SC_ARENA_PLAY_EVENT_NOTI);
				 */

				/*
				 * SC_ARENA_PLAY_EVENT_NOTI noti3 = SC_ARENA_PLAY_EVENT_NOTI.newInstance();
				 *
				 * ArenaActorInfo aInfo11 = ArenaActorInfo.newInstance(); aInfo11.set_arena_char_id(1); aInfo11.set_character_class(CharacterClass.MAGICIAN); aInfo11.set_character_name(gm.getName().getBytes()); aInfo11.set_gender(Gender.MALE); aInfo11.set_marker_id(0);
				 * aInfo11.set_role(eRole.Player); aInfo11.set_server_id(0); aInfo11.set_team_id(eArenaTeam.TEAM_A); noti3.set_actor_info(aInfo11); noti3.set_arena_char_id_causer(gm.getId()); noti3.set_arena_char_id_target(0); noti3.set_spell_id(0);
				 * noti3.set_team_id(eArenaTeam.TEAM_A); noti3.set_type(eType.YourSelfEntered); gm.sendPackets(noti3, MJEProtoMessages.SC_ARENA_PLAY_EVENT_NOTI);
				 *
				 * noti3 = SC_ARENA_PLAY_EVENT_NOTI.newInstance(); noti3.set_type(eType.GameCountDown10Sec); gm.sendPackets(noti3, MJEProtoMessages.SC_ARENA_PLAY_EVENT_NOTI);
				 */

				/*
				 * int[] surf = new int[] {22905, 22906, 22906}; int[] dialogs = new int[] {1998, 1999, 2000}; SC_DIALOGUE_MESSAGE_NOTI noti = SC_DIALOGUE_MESSAGE_NOTI.newInstance(); for(int i=0; i<3; ++i) { Dialogue dialog = Dialogue.newInstance();
				 * dialog.set_talker_id(surf[i]); dialog.set_duration(10); dialog.set_dialogue_id(dialogs[i]); noti.add_dialogues(dialog); } gm.sendPackets(noti, MJEProtoMessages.SC_DIALOGUE_MESSAGE_NOTI);
				 */
				/*
				 * SC_MONSTER_BOOK_V2_INFO_NOTI noti = SC_MONSTER_BOOK_V2_INFO_NOTI.newInstance(); try { ProtoInputStream stream = ProtoInputStream.newInstance("./data/test_wq.dat"); stream.readRawByte(); stream.readRawByte(); stream.readRawByte(); noti.readFrom(stream);
				 * }catch(Exception e) { e.printStackTrace(); }
				 *
				 * MonsterBookV2Info v2Info = noti.get_info(); SystemT sysT = v2Info.get_system(); ArrayList<GradeRewardT> grades = sysT.get_grade_reward(); for(GradeRewardT grt : grades) { System.out.println(grt.get_grade()); ConditionalRewardsT conditional_reward_t =
				 * grt.get_rewards(); System.out.println("conditional_reward_t use_item_id : " + conditional_reward_t.get_UsedItemID()); System.out.println("conditional_reward_t size :" + (conditional_reward_t.get_ConditionalReward() == null ? 0 :
				 * conditional_reward_t.get_ConditionalReward().size())); if(conditional_reward_t.get_ConditionalReward() != null) { } }
				 */

				// System.out.println(sysT.get_rewards());

				/*
				 * SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK ack = SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK.newInstance(); ack.set_page_id(1); ack.set_total_page(5); ack.set_result(SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK.eResult.SUCCESS); gm.sendPackets(ack,
				 * MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK);
				 */

				// -> room enter.
				/*
				 * SC_ARENACO_CREATE_INDUN_ROOM_ACK ack = SC_ARENACO_CREATE_INDUN_ROOM_ACK.newInstance(); ack.set_result(SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.SUCCESS); ack.set_room_id(1); gm.sendPackets(ack, MJEProtoMessages.SC_ARENACO_CREATE_INDUN_ROOM_ACK);
				 */

				/*
				 * SC_ARENACO_EXIT_INDUN_ROOM_ACK ack = SC_ARENACO_EXIT_INDUN_ROOM_ACK.newInstance(); ack.set_result(SC_ARENACO_EXIT_INDUN_ROOM_ACK.eResult.SUCCESS); ack.set_room_id(1); gm.sendPackets(ack, MJEProtoMessages.SC_ARENACO_EXIT_INDUN_ROOM_ACK);
				 */
				/*
				 * SC_ARENACO_BYPASS_INDUN_KICK_NOTI noti = SC_ARENACO_BYPASS_INDUN_KICK_NOTI.newInstance(); noti.set_kick_arena_char_id(gm.getId()); noti.set_kick_char_name("메티스".getBytes()); noti.set_room_id(1); gm.sendPackets(noti,
				 * MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_KICK_NOTI);
				 */
				/*
				 * SC_ARENACO_BYPASS_EXIT_INDUN_ROOM_NOTI noti = SC_ARENACO_BYPASS_EXIT_INDUN_ROOM_NOTI.newInstance(); noti.set_room_id(1); ArenaUserInfo uInfo = ArenaUserInfo.newInstance(); uInfo.set_arena_char_id(gm.getId() + 1);
				 * uInfo.set_character_class(CharacterClass.DARKELF); uInfo.set_character_name("ddd".getBytes()); uInfo.set_gender(Gender.FEMALE); uInfo.set_in_room(true); uInfo.set_ready(false); uInfo.set_role(eRole.Player); uInfo.set_room_owner(true);
				 * uInfo.set_server_id(0); uInfo.set_team_id(eArenaTeam.TEAM_A); noti.set_user_info(uInfo); gm.sendPackets(noti, MJEProtoMessages.SC_ARENACO_BYPASS_EXIT_INDUN_ROOM_NOTI);
				 */

				/*
				 * SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK ack = SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK.newInstance(); ack.set_result(SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK.eResult.SUCCESS);
				 *
				 * IndunRoomDetailInfo dInfo = IndunRoomDetailInfo.newInstance(); IndunEnterCondition condition = IndunEnterCondition.newInstance(); condition.set_fee(1); condition.set_key_item_id(gm.getInventory().getItems().get(0).getItem(). getItemDescId());
				 * dInfo.set_condition(condition); dInfo.set_distribution_type(eDistributionType.GET_PRIORITY); dInfo.set_dungeon_type(eDungeonType.DEFENCE_TYPE); dInfo.set_map_kind(eArenaMapKind.OrimLab_Normal); dInfo.set_max_player(8); dInfo.set_min_level(80);
				 * ack.set_room_info(dInfo); gm.sendPackets(ack, MJEProtoMessages.SC_ARENACO_BYPASS_INDUN_ROOM_INFO_ACK);
				 */
				/*
				 * SC_ARENACO_BYPASS_ENTER_INDUN_ROOM_NOTI noti =SC_ARENACO_BYPASS_ENTER_INDUN_ROOM_NOTI.newInstance(); noti.set_room_id(1); ArenaUserInfo uInfo = ArenaUserInfo.newInstance(); uInfo.set_arena_char_id(gm.getId() + 1);
				 * uInfo.set_character_class(CharacterClass.DARKELF); uInfo.set_character_name("ddd".getBytes()); uInfo.set_gender(Gender.FEMALE); uInfo.set_in_room(true); uInfo.set_ready(false); uInfo.set_role(eRole.Player); uInfo.set_room_owner(true);
				 * uInfo.set_server_id(0); uInfo.set_team_id(eArenaTeam.TEAM_A); noti.set_user_info(uInfo); gm.sendPackets(noti, MJEProtoMessages.SC_ARENACO_BYPASS_ENTER_INDUN_ROOM_NOTI);
				 */

				/*
				 * SC_NOTIFICATION_INFO_NOTI noti = SC_NOTIFICATION_INFO_NOTI.newInstance(); noti.set_currentpagecount(1); noti.set_maxpagecount(1);
				 *
				 * NotificationInfomation nInfo = NotificationInfomation.newInstance(); nInfo.set_notification_id(17); nInfo.set_hyperlink("http://www.naver.com".getBytes()); nInfo.set_displaydesc("$26237".getBytes()); nInfo.set_startdate(System.currentTimeMillis() /
				 * 1000); nInfo.set_enddate((System.currentTimeMillis() + 3600) / 1000);
				 *
				 * TeleportData data = TeleportData.newInstance(); data.set_adenacount(300); data.set_stringk("4654".getBytes()); nInfo.set_teleport(data);
				 *
				 * EventNpcData npc = EventNpcData.newInstance(); EventNpcInfo npcInfo = EventNpcInfo.newInstance(); npcInfo.set_displaydesc("$30144".getBytes()); npcInfo.set_npc_id(gm.getId()); npc.add_eventinfo(npcInfo); nInfo.set_eventnpc(npc);
				 * noti.add_notificationinfo(nInfo); gm.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_INFO_NOTI);
				 */
				/*
				 * for(L1ItemInstance item : gm.getInventory().getItems()) { item.getSkillEffectTimeSec(0); }
				 */

				// gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "在夜間無法進行隨機傳送的地區。"));
				/*
				 * System.out.println(GameTimeClock.getInstance().getGameTime().getSeconds()); System.out.println(GameTimeClock.getInstance().getGameTime().get(Calendar. HOUR_OF_DAY) + " " + GameTimeClock.getInstance().getGameTime().get(Calendar.MINUTE));
				 */
				/*
				 * SC_POLYMORPH_NOTI noti = SC_POLYMORPH_NOTI.newInstance(); noti.set_objId(gm.getId()); noti.set_class_id(2927); noti.set_action(99); noti.set_desc("test".getBytes()); noti.set_sprite(6452); gm.sendPackets(noti, MJEProtoMessages.SC_POLYMORPH_NOTI);
				 */
				// SC_GOODS_INVEN_NOTI.do_send(gm);
				/*
				 * for(int i=0; i<500; ++i) { try { Thread.sleep(100); } catch (InterruptedException e) { // TODO Auto-generated catch block e.printStackTrace(); } } gm.sendPackets("test..!");
				 */
				// gm.do_start_client_auto_ack();
				break;
			}
			case "test2": {
				SC_CHANGE_TEAM_NOTI_PACKET noti = SC_CHANGE_TEAM_NOTI_PACKET.newInstance();
				noti.set_object_id(gm.getId());
				noti.set_object_team_id(Integer.parseInt(param));
				gm.sendPackets(noti, MJEProtoMessages.SC_CHANGE_TEAM_NOTI_PACKET, true);
				break;
			}
			case "test3": {
				SC_BASECAMP_CHART_NOTI_PACKET noti = SC_BASECAMP_CHART_NOTI_PACKET.newInstance();
				noti.set_team_points(1000000);
				noti.set_winner_team_id(1);
				for (int i = 1; i <= 10; ++i) {
					CHART_INFO cInfo = CHART_INFO.newInstance();
					cInfo.set_id(i);
					cInfo.set_user_name(String.format("men%d", i));
					cInfo.set_user_points(i * 10000);
					noti.add_charts(cInfo);
				}
				gm.sendPackets(noti, MJEProtoMessages.SC_BASECAMP_CHART_NOTI_PACKET, true);
				break;
			}
			case "龍襲擊": {
				Dragon_raid(gm, param);
				break;
			}
			case "伺服器轉移":
				do_shift_server(gm, param);
				break;
			case "競技場":
				StadiumManager.getInstance().open_stadium(gm, param);
				break;
			case "比賽結束":
				StadiumManager.getInstance().quit_stadium(gm, param);
				break;
			case "決鬥":
				test_lfc(gm, param);
				break;
			case "娃娃盒":
				MJAlchemyProbabilityBox.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "精煉盒":
				MJSmeltingProbabilityBox.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "血盟信息":
				searchclaner(gm, param);
				break;
			case "傲慢之塔首領":
				DominanceBoss(gm, param);
				break;
			case "角色檢查":
				chainfo(gm, param);
				break;
			case "自動樹":
				gm.isAutoTreeple = true;
				break;
			case "固定增益":
				befixed(gm, param);
				break;
			case "角色刪除":
				standBy77(gm, param);
				break;
			case "氣運祝福":
				Blessleaf(gm, param);
				break;
			case "製作成功":
				CraftSuccess(gm, param);
				break;
			case "武器強化成功":
				EnchantWeaponSuccess(gm, param);
				break;
			case "防具強化成功":
				EnchantArmorSuccess(gm, param);
				break;
			case "艾爾札貝":
				ErzabeBox(gm, param);
				break;
			case "沙蟲":
				SandwormBox(gm, param);
				break;
			case "帳號檢查":
				AccountCheck(gm, param);
				break;
			case "帳號確認":
				AccountCheck1(gm, param);
				break;
			case "活動任務":
				jakjak(gm);
				break;
			case "防具任務":
				jakjak2(gm);
				break;
			case "卡片任務":
				jakjak3(gm);
				break;
			case "惡魔任務":
				dolldemon(gm);
				break;
			case "冰女任務":
				doice(gm);
				break;
			case "墮落作業":
				dolltarak(gm);
				break;
			case "死亡作業":
				dolldeath(gm);
				break;
			case "符文石卡片":
				Stone(gm);
				break;
			case "符文石分解":
				Stone1(gm);
				break;
			case "符文石防禦":
				Stone2(gm);
				break;
			case "符文石護甲":
				Stone3(gm);
				break;
			case "任務":
				customQuestCmd(gm, param);
				break;
			case "首領":
				addEventBoss(gm, param);
				break;
			case "原型":
				showProto(gm, param);
				break;
			case "地下城計時器":
				DungeonTimeLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "詛咒":
				CurseCharacter(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "詛咒重置":
				CurseInitializeCharacter(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "附魔系統":
				MJItemEnchantSystemLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "驗證碼":
				MJCaptchaLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "角色狀態":
				setCharacterInstanceStatus(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "戰鬥系統":
				MJCombatLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "書籍系統":
				BQSLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "出席檢查":
				MJAttendanceLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "查看首頁":
				gm.sendPackets(S_ShowCmd.getPlayMovieNoti("https://minamldh.wixsite.com/adam", -1));
				break;
			case "標記設置":
				setPresentationCode(gm, param);
				break;
			case "物件列表":
				showObjectList(gm, param);
				break;
			case "導出日志":
				MJProcessPlayer.dumpLog();
				break;
			case "網絡":
				MJNetServerLoadManager.commands(gm, param);
				break;
			case "保護模式":
				setProtectionMode(gm, param);
				break;
			case "網絡安全":
				MJNetSafeLoadManager.commands(gm, param);
				break;
			case "延遲":
				ping(gm, param);
				break;
			case "擊殺圖表":
				MJKDALoadManager.commands(gm, param);
				break;
			case "聊天測試":
				showChat(gm, param);
				break;
			case "座標":
				coordinates(gm);
				break;
			case ",":
				showNextProto(gm, param);
				break;
			case "指令":
				gm.sendPackets(S_ShowCmd.get("cmd."));
				break;
			case "戰爭列表":
				showWarList(gm);
				break;
			case "使用類型":
				showUseType(gm, param);
				break;
			case "角色封鎖":
				setCharBlock(gm, param);
				break;
			case "解除角色封鎖":
				setCharBlockDelete(gm, param);
				break;
			case "潛水模式":
				setSleepingMode(gm, param);
				break;
			case "解除潛水模式":
				unSetSleepingMode(gm, param);
				break;
			case "/":
				showNextEffect(gm);
				break;
			case "效果初始化":
				showEffectInit(gm, param);
				break;
			case "變身事件":
				POLYMORPH_EVENT(gm, param);
				break;
			case "血印":
				clanMark(gm, param);
				break;
			case "mj":
				mjClear(gm, param);
				break;
			case "實例":
				MJInstanceLoadManager.commands(gm, param);
				break;
			case "突襲":
				MJRaidLoadManager.commands(gm, param);
				break;
			case "框架查詢":
				MJCTLoadManager.commands(gm, param);
				break;
			case "停止通話":
				nocall(gm, param);
				break;
			case "排行系統":
				MJRankLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
				break;
			case "物品":
				execute(gm, param, param);
				break;
			case "金幣":
				execute1(gm, param, param);
				break;
			case "事件":
				Event_System(gm, param);
				break;
			case "狩獵":
				huntEvent(gm, param);
				break;
			case "召喚事件":
				Event1(gm, param);
				break;
			case "無延遲":
				NoDelayUser(gm);
				break;
			case "帳戶":
				doolyHelp1(gm);
				break;
			case "周邊封禁":
				LargeAreaBan(gm, param);
				break;
			case "廣域封禁":
				LargeAreaIPBan(gm, param);
				break;
			case "隊伍":
				party(gm, param);
				break;
			case "發放營地":
				GiveHouse(gm, param);
				break;
			case "攻城開始":
				castleWarStart(gm, param);
				break;
			case "通緝測試":
				hunt(gm, param);
				break;
			case "推送系統":
				pushSystem(gm, param);
				break;
			case "攻城結束":
				castleWarExit(gm, param);
				break;
			case "消息測試":
				gm.sendPackets(new S_TestPacket(S_TestPacket.a));
				break;
			case "召喚機器人":
				summonBot(gm, param);
				break;
			case "伺服器保存":
				serversave(gm);
				break;
			case "全體禮物":
				allpresent(gm, param);
				break;
			case "解除封鎖":
				accountdel(gm, param);
				break;
			case "恢復經驗值":
				returnEXP(gm, param);
				break;
			case "自動拾取":
				autoloot(gm, param);
				break;
			case "清理娃娃":
				cleanDoll(gm);
				break;
			case "平衡":
				CharacterBalance(gm, param);
				break;
			/*
			 * case "無人商店": privateShop(gm); break;
			 */
			case "圖標":
				icon(gm, param);
				break;
			case "光照":
				maphack(gm, param);
				break;
			case "開放等待":
				standBy(gm, param);
				break;
			case "屬性":
				fullstat(gm, param);
				break;
			case "商店檢查":
				shopCheck(gm);
				break;
			case "效果":
				effect(gm, param);
				break;
			case "刪除用戶物品欄":
				targetInventoryDelete(gm, param);
				break;
			case "新增帳號":
				addaccount(gm, param);
				break;
			case "全體召喚":
				allrecall(gm);
				break;
			case "發放科馬":
				SpecialEventHandler.getInstance().doGiveEventStaff();
				break;
			case "發放首領召喚卷軸":
				SpecialEventHandler.getInstance().doGiveEventStaff2();
				break;
			case "發放托帕茲":
				SpecialEventHandler.getInstance().doGiveEventStaff1();
				break;
			case "封禁列表":
				search_banned(gm);
				break;
			case "更改密碼":
				changePassword(gm, param);
				break;
			case "村莊":
				unprison(gm, param);
				break;
			case "隱藏村莊":
				unprison2(gm, param);
				break;
			case "解除禁言":
				chatx(gm, param);
				break;
			case "傳送":
			case "傳":
			case "解除傳送":
				tell(gm);
				break;
			case "搜尋":
				searchDatabase(gm, param);
				break;
			case "PK":
				Pvp(gm, param);
				break;
			case "帳號":
				account_Cha(gm, param);
				break;
			case "升級":
				levelup2(gm, param);
				break;
			case "商店驅逐":
				ShopKick(gm, param);
				break;
			case "監獄":
				hold(gm, param);
				break;
			case "垃圾整理":
				garbageCollection(gm);
				break;
			case "更改端口":
				changePort(gm, param);
				break;
			case "刪除庫存":
				InventoryDelete(gm, param);
				break;
			case "重載房子":
				reloadHouse(gm, param);
				break;
			case "搜尋物品":
				searchItem(gm, param);
				break;
			case "全部清理":
				cleanAll(gm);
				break;
			case "死鎖檢測":
				GeneralThreadPool.getInstance().execute(new DeadLockDetector(gm));
				break;
			case "攻速檢查":
				gm.AttackSpeedCheck2 = 1;
				gm.sendPackets("\fY請攻擊假人10次。");
				break;
			case "移速檢查":
				gm.MoveSpeedCheck = 1;
				gm.sendPackets("\fY請向一個方向移動10次。");
				break;
			case "魔法檢查":
				gm.magicSpeedCheck = 1;
				gm.sendPackets("\fY請使用您想要的魔法10次。");
				break;
			case "刪除圖片":
				this.get_delete_gfx(gm);
				break;
			case "錯誤檢查":
				bug_race_check(gm);
				break;
			case "錯誤操作":
				bug_race_rate(gm, param);
				break;
			case "Boss通知":
				spawnNotifyOnOff(gm, param);
				break;
			case "測試":
				testCommands(gm, param);
				break;
			case "瘋狂檢查":
				gm.sendPackets(S_ShowCmd.getPlayMovieNoti("http://1111111.megaplug.kr/pricemania/pricePreviewboard1.html", -1));
				break;
			case "比特幣":
				gm.sendPackets(S_ShowCmd.getPlayMovieNoti("https://www.bithumb.com/u5/US506", -1));
				break;
/*		case "彩券遊戲":
			if (Config.lottoInfo.run == true) {
				LottoSystem.getInstance().isGmOpen = true;
			}
			break;*/
			case "格蘭":
			case "格蘭卡因":
				if (!FatigueProperty.getInstance().use_fatigue()) {
					gm.sendPackets("\\aG目前格蘭卡因系統未啟動。");
					return;
				}
				gm.sendPackets("\\aN- 艾因哈薩德祝福階段每分鐘累積量");
				gm.sendPackets("\\aN[第4階段:1] [第3階段:2] [第2階段:3] [第1階段:4] [祝福0:5]");
				gm.sendPackets("\\aN- 格蘭卡因啟動時經驗值及阿登那掉落率減少80%");
				gm.sendPackets("\\aN- 格蘭卡因啟動10小時後自動解除。");
				if (gm.getAccount().has_fatigue())
					gm.sendPackets(String.format("\f3目前格蘭卡因的憤怒結束還剩 %,d秒。", gm.getAccount().remain_fatigue() / 1000L));
				else
					gm.sendPackets(String.format("\\aG[目前格蘭卡因數值 : %,d]", gm.getAccount().get_fatigue_point()));
				break;
			case "格蘭卡因重置":
				l1j.server.server.Account.initialize_fatigue();
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					l1j.server.server.Account account = pc.getAccount();
					if (account == null || pc.getAI() != null)
						continue;

					account.initialize_fatigue_info(pc);
				}
				gm.sendPackets("格蘭卡因的憤怒已經重置。");
				break;
			case "類型":
				int type = Integer.parseInt(param);
				gm.sendPackets("\\aA料理類型 : " + type);
				gm.sendPackets(new S_PacketBox(53, type, 1800));
				break;
			case "獵人增益":
				try {
					gm.sendPackets(S_InventoryIcon.icoReset(HUNTER_BLESS, 4992, 1800L, true));
				} catch (Exception e) {
					e.getStackTrace();
				}
				break;
			case "製作檢查":
				if (!productionCheck) {
					productionCheck = true;
					gm.sendPackets("\\aA■ 製作檢查 開啟! ■");
				} else {
					productionCheck = false;
					gm.sendPackets("\\aA■ 製作檢查 關閉 ■");
				}
				/** MJCTSystem **/
				break;
			case "測試標記":
				try {
					StringTokenizer st = new StringTokenizer(param);
					String code = st.nextToken();
					if (code.equalsIgnoreCase("開")) {
						Config.test = true;
						for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							for (L1Object npc : L1World.getInstance().getVisibleObjects(pc, 18)) {
								if (((npc instanceof L1AuctionBoardInstance)) || ((npc instanceof L1BoardInstance)) || ((npc instanceof L1GuardInstance)) || ((npc instanceof L1MerchantInstance))
										|| ((npc instanceof L1TeleporterInstance)))
									pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream((L1NpcInstance) npc));
								// pc.sendPackets(S_WorldPutObject.get((L1NpcInstance) npc));
							}
						}
					} else if (code.equalsIgnoreCase("關")) {
						Config.test = false;
						for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							for (L1Object npc : L1World.getInstance().getVisibleObjects(pc, 18)) {
								if (((npc instanceof L1AuctionBoardInstance)) || ((npc instanceof L1BoardInstance)) || ((npc instanceof L1GuardInstance)) || ((npc instanceof L1MerchantInstance))
										|| ((npc instanceof L1TeleporterInstance)))
									pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream((L1NpcInstance) npc));
								// pc.sendPackets(S_WorldPutObject.get((L1NpcInstance) npc));
							}
						}
					}

				} catch (Exception e) {
					gm.sendPackets(".測試標記 [開/關]");
				}
				break;
			/*
			 * case "畫面訊息": try { StringTokenizer st = new StringTokenizer(param); int number = Integer.parseInt(st.nextToken()); int number2 = Integer.parseInt(st.nextToken()); gm.sendPackets(new S_TestPacket(S_TestPacket.a, number, number2, "00 ff ff")); } catch
			 * (Exception e) { gm.sendPackets("畫面訊息 [gfx編號] [訊息編號] 輸入"); gm.sendPackets("gfx是4000的話就是8000的2倍。"); gm.sendPackets("訊息是4000的話，訊息編號會成為2002，2會附加。"); } break;
			 */
			case "全體增益":
				try {
					StringTokenizer st = new StringTokenizer(param);
					String status = st.nextToken();
					L1AllBuff.getInstance().execute(gm, param, status);
				} catch (Exception e) {
					gm.sendPackets(new S_ChatPacket(gm, "----------------------------------------------------"));
					gm.sendPackets(new S_ChatPacket(gm, " 1:全體增益 2:祝福 3:黑死 4:昏迷"));
					gm.sendPackets(new S_ChatPacket(gm, "----------------------------------------------------"));
				}
				break;
			case "重生":
				try {
					StringTokenizer st = new StringTokenizer(param);
					int mapid = Integer.parseInt(st.nextToken());
					L1World.getInstance().getMapObject(mapid);
					MJNormalSpawnLoader.getInstance().do_map_load(mapid);
					gm.sendPackets("spawnlist_ex_normal(指定地圖)重載(重生) 完成");
				} catch (Exception e) {
					gm.sendPackets(".重生 (地圖ID)");
				}
				break;
			case "發放NCoin":
				try {
					StringTokenizer tokenizer = new StringTokenizer(param);
					String name = tokenizer.nextToken();
					int coin = Integer.parseInt(tokenizer.nextToken());
					increaseNcoin(gm, name, coin);
				} catch (Exception e) {
					gm.sendPackets("[發放NCoin] [角色名稱] [金額] 輸入");
				}
				break;
			case "回收NCoin":
				try {
					StringTokenizer tokenizer = new StringTokenizer(param);
					String name = tokenizer.nextToken();
					int coin = Integer.parseInt(tokenizer.nextToken());
					L1PcInstance tg = L1World.getInstance().getPlayer(name);
					if (tg != null) {
						tg.addNcoin1(coin);

						String s = String.format("\\aGNCoin (%,d) 已被回收。謝謝。", coin);
						tg.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, s));
						tg.sendPackets(s);

						String remainMessage = String.format("\f2剩餘的 NCoin: (%,d)", tg.getNetConnection().getAccount().Ncoin_point);
						tg.sendPackets(remainMessage);
						gm.sendPackets(String.format("\\aA角色名稱:[\\aG%s\\aA]" + " 回收金額:[\\aG%,d\\aA] 完成! \f2[目標剩餘 NCoin:(%,d)]", name, coin, tg.getNetConnection().getAccount().Ncoin_point));
					} else {
						gm.sendPackets("該角色目前未在線。");
					}
				} catch (Exception e) {
					gm.sendPackets("[回收NCoin] [角色名稱] [金額] 輸入。");
				}
				break;
			case "檢查NCoin":
				try {
					StringTokenizer tokenizer = new StringTokenizer(param);
					String name = tokenizer.nextToken();
					L1PcInstance tg = L1World.getInstance().getPlayer(name);
					if (tg != null) {
						gm.sendPackets("\\aA剩餘金額: 角色名稱:[\\aG" + name + "\\aA] NCoin:[\\aG" + tg.getNetConnection().getAccount().Ncoin_point + "\\aA] 已確認");
					} else {
						gm.sendPackets("該角色目前未在線。");
					}
				} catch (Exception e) {
					gm.sendPackets("[檢查NCoin] [角色名稱] [金額] 輸入。");
				}
				break;
			case "聊天":
				try {
					StringTokenizer st = new StringTokenizer(param);
					String name = st.nextToken();
					String msg = st.nextToken();
					for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
						listner.sendPackets(new S_ChatPacket(name, 0x03, msg));
					}
				} catch (Exception e) {
					gm.sendPackets(".聊天 [角色名稱] [聊天內容] 輸入");
				}
				break;
			case "操作血盟":
				try {
					StringTokenizer st = new StringTokenizer(param);
					String name = st.nextToken();
					int number = Integer.parseInt(st.nextToken());
					L1Clan clan = L1World.getInstance().findClan(name);
					if (clan == null) {
						gm.sendPackets("血盟不存在。");
					} else {
						clan.setCastleId(number);
						L1World.getInstance().removeClan(clan);
						L1World.getInstance().storeClan(clan);
						ClanTable.getInstance().updateClan(clan);
						gm.sendPackets(name + " 的血盟資訊已更改。");
					}
				} catch (Exception e) {
					gm.sendPackets(".操作血盟 [血盟名稱] [城堡編號] 輸入");
					gm.sendPackets("肯特1, 亞克2, 風木3, 奇岩4, 海音5, 威頓6, 亞丁7, 狄亞德8");
				}
				break;
			case "重新加載商店":
				try {
					int npcid = Integer.parseInt(param);
					L1Npc npc = NpcTable.getInstance().getTemplate(npcid);
					ShopTable.getInstance().Reload(npcid);
					gm.sendPackets("NPC: " + npc.get_name() + " 已重新加載。");
				} catch (Exception e) {
					gm.sendPackets(".重新加載商店 NPCID");
				}
				break;
			case "加入血盟":
				try {
					StringTokenizer st = new StringTokenizer(param);
					String name = st.nextToken();
					String clanname = st.nextToken();
					L1PcInstance pc = L1World.getInstance().getPlayer(name);
					L1Clan clan = L1World.getInstance().findClan(clanname);
					if (pc == null) {
						gm.sendPackets("沒有這樣的用戶。");
						return;
					}
					if (clan == null) {
						gm.sendPackets("沒有這樣的血盟。");
						return;
					}
					if (pc.getClanid() != 0) {
						gm.sendPackets("" + pc.getName() + " 有血盟，將被移除。");
						pc.ClearPlayerClanData(clan);
						clan.removeClanMember(pc.getName());
						gm.save();
						return;
					}

					for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
						clanMembers.sendPackets(new S_ServerMessage(94, pc.getName()));
						// 1%0 已被接受為血盟的一員。
					}
					pc.setClanid(clan.getClanId());
					pc.setClanname(clanname);
					pc.setClanRank(L1Clan.MEMBER);
					pc.setTitle("");
					pc.setClanMemberNotes("");
					pc.sendPackets(new S_CharTitle(pc.getId(), ""));
					Broadcaster.broadcastPacket(pc, new S_CharTitle(pc.getId(), ""));
					clan.addClanMember(pc.getName(), pc.getClanRank(), pc.getLevel(), "", pc.getId(), pc.getType(), pc.getOnlineStatus(), pc);
					pc.save(); // 將角色信息寫入資料庫
					pc.sendPackets(new S_PacketBox(pc, S_PacketBox.PLEDGE_REFRESH_PLUS));
					pc.sendPackets(new S_ServerMessage(95, clanname)); // 1%0 到血盟
					pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, true);
				} catch (Exception e) {
					gm.sendPackets(".血盟加入 [角色名] [血盟名] 輸入");
				}
				break;
			case "血盟退出":
				try {
					StringTokenizer tokenizer = new StringTokenizer(param);
					String pcName = tokenizer.nextToken();
					L1PcInstance pc = L1World.getInstance().getPlayer(pcName);
					if (pc == null) {
						gm.sendPackets("沒有這樣的用戶。");
						return;
					}
					L1Clan clan = pc.getClan();
					L1PcInstance clanMember[] = clan.getOnlineClanMember();
					for (int i = 0; i < clanMember.length; i++) {
						clanMember[i].sendPackets(new S_ServerMessage(ServerMessage.LEAVE_CLAN, param, clan.getClanName()));// 1%0 是
						// %1 已經退出了血盟
					}
					pc.ClearPlayerClanData(clan);
					clan.removeClanMember(pc.getName());
					pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, true);
				} catch (Exception e) {
					gm.sendPackets(".血盟退出 [角色名] 輸入");
				}
				break;
			// case "紅色戰爭宣告": UserCommands.getInstance().blooodWarStart(gm, param); break;
			case "排名物品發放停止":
				rankGiveItemStop(gm, param);
				break;
			case "查看購買限制時間":
				viewShopBuyLimit(gm);
				break;
			case "設置購買限制":
				shopBuyLimit(gm, param);
				break;
			case "技能檢查":
				SpellAskCommands(gm, param);
				break;
			case "角色整理":
				character_delete(gm, param);
				break;
			case "初始化":
			case "數據庫初始化":
				gm.sendPackets(new S_Message_YN(C_Attr.MSGCODE_6008_RESET, 6008, "您想進行數據庫重置嗎？"));
				break;
//			test(gm);
//			clear_DB(gm);
				break;
			case ".":
				if (!_lastCommands.containsKey(gm.getId())) {
					gm.sendPackets(new S_ServerMessage(74, "命令 " + cmd));
					// 1%0 無法使用。
					return;
				}
				redo(gm, param);
				break;
			default:
				gm.sendPackets(new S_SystemMessage("管理員命令 " + cmd + " 不存在，轉接至用戶命令。"));
				UserCommands.getInstance().handleCommands(gm, cmdLine);
				// gm.sendPackets(new S_SystemMessage("[Command] 命令 " + cmd + " 不存在。"));
				break;
		}
	}
	public static void test(L1PcInstance pc) {
		System.out.println("測試完成");
	}
	public static void clear_DB(L1PcInstance pc) {
		Connection con = null;
		try {
			// TODO: 要刪除的數據庫
			con = L1DatabaseFactory.getInstance().getConnection();

			L1QueryUtil.execute(con, "DELETE FROM accounts", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM attendance_startup", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM attendance_userinfo", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM character_app_cash", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_blocks", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_buddys", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_buff", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_config", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_custom_quest", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_delayitems", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_elf_warehouse", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_exclude", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_exp_cache", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_fairly_config", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_favorbook", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_items", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_package_warehouse", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_pushlist", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_quests", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_representative", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_shop", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_shop_buy_limit", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_shop_store", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_skills", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_skills_delay", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_slot_color", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_slot_items", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_soldier", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_special_stat", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_special_warehouse", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_supplementary_service", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_tams", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_time_collection", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_warehouse", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM characters", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM characters_deathpenalty_exp", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM characters_deathpenalty_item", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM characters_private_probability", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM character_free_shield", new Object[0]);


			L1QueryUtil.execute(con, "DELETE FROM clan_data", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM clan_buff_list", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM clan_warehouse", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM clan_warehouse_list", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM clan_warehouse_log", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM clan_warehousehistory", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM clan_storage", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM clan_ban", new Object[0]);


			L1QueryUtil.execute(con, "DELETE FROM event_boss_log", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM mw_auto_banker", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM mw_auto_banker_unknown", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM ncoin_character_report", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM ncoin_trade_adena", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM ncoin_trade_deposit", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM ncoin_trade_item", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM ncoin_trade_refund", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM passive_user_info", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM recovery_system_exp", new Object[0]);


			L1QueryUtil.execute(con, "DELETE FROM revenge_loser", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM revenge_winners", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_dungeon_time_account_information", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_dungeon_time_char_information", new Object[0]);


			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_characterinfo", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mrace_history", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mrace_racer", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mrace_ticket", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tj_lost_items", new Object[0]);

			pc.sendPackets("數據庫重置完成。請重新啟動服務器。");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
	}
	private void viewShopBuyLimit(L1PcInstance pc) {
		ArrayList<ShopBuyLimit> sbl_list = ShopBuyLimitInfo.getInstance().getCharacterList(pc.getId());
		if (sbl_list != null && sbl_list.size() > 0) {
			for (ShopBuyLimit sbl : sbl_list) {
				if (sbl.get_count() <= 0) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String sLatestLoginDate = formatter.format(sbl.get_end_time());
					pc.sendPackets(String.format("\f2" + sbl.get_item_name() + "\f2" + " 無法在 %s 之前重新購買" +
							(sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT ? " 對於相同帳戶" : "") + "。", sLatestLoginDate));
				} else {
					pc.sendPackets("\f2" + sbl.get_item_name() + " 可以再購買 " + sbl.get_count() + " 次。");
				}
			}

			pc.sendPackets("\f2共搜尋到 " + sbl_list.size() + " 項。");
		} else {
			pc.sendPackets("\f2沒有購買限制列表。");
		}
	}

	private void shopBuyLimit(L1PcInstance gm, String s) {
		try {
			StringTokenizer tok = new StringTokenizer(s);
			String name = tok.nextToken();

			if (name.equalsIgnoreCase("保存")) {
				String type = tok.nextToken();
				if (type.equalsIgnoreCase("全部")) {
					ShopBuyLimitInfo.getInstance().save();
					gm.sendPackets("\f3購買限制強制保存已完成。");
				} else {
					String target_name = tok.nextToken();
					L1PcInstance target = L1World.getInstance().getPlayer(target_name);
					if (target == null) {
						gm.sendPackets("無法在世界中找到該用戶。");
						return;
					}

					ShopBuyLimitInfo.getInstance().save(target.getId());
					gm.sendPackets(target.getName() + "\f2的購買限制列表已保存。");
				}
			} else if (name.equalsIgnoreCase("刪除")) {
				String target_name = tok.nextToken();
				L1PcInstance target = L1World.getInstance().getPlayer(target_name);
				if (target_name.equalsIgnoreCase("全部")) {
					ShopBuyLimitInfo.getInstance().clearShopBuyLimit();
					gm.sendPackets("\f2購買限制列表已全部刪除。");
				} else if (target_name.equalsIgnoreCase(target.getName())) {
					ShopBuyLimitInfo.getInstance().removeShopBuyLimit(target.getId());
					gm.sendPackets(String.format("\f2%s的所有物品購買記錄已被刪除。", target_name));
				} else {
					int itemid = 0;
					if (tok.hasMoreTokens()) {
						itemid = Integer.parseInt(tok.nextToken());
					}

					if (itemid != 0) {
						if (ItemTable.getInstance().getTemplate(itemid) == null) {
							gm.sendPackets("\f2該物品不存在。");
							return;
						}

						if (ShopBuyLimitInfo.getInstance().getShopBuyLimit(itemid) == null) {
							gm.sendPackets("\f2此物品不受購買限制。");
							return;
						}
					}

					if (itemid == 0) {
						ShopBuyLimitInfo.getInstance().removeShopBuyLimit(target.getId());
					} else {
						ShopBuyLimitInfo.getInstance().removeShopBuyLimit(target.getId(), itemid);
					}

					gm.sendPackets(target.getName() + "的" + ItemTable.getInstance().getTemplate(itemid).getName() + "購買限制已被刪除。");
				}
			}
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage("\f2.購買限制設置 [刪除] [刪除時角色名/全部] [角色名指定時輸入物品編號（未輸入則刪除該角色所有物品）]"));
			gm.sendPackets(new S_SystemMessage("\f2.購買限制設置 [保存] [角色名/全部]"));
		}
	}

	private void pushSystem(L1PcInstance gm, String s) {
		try {
			StringTokenizer tok = new StringTokenizer(s);
			String name = tok.nextToken();

			if (name.equalsIgnoreCase("開")) {
				if (PushItemController.isPushSystem()) {
					gm.sendPackets(new S_SystemMessage("推送系統已經啟用。"));
					return;
				}
				PushItemController.setPushSystem(true);
				gm.sendPackets(new S_SystemMessage("推送系統已經開始運行。"));
			} else if (name.equalsIgnoreCase("關")) {
				if (!PushItemController.isPushSystem()) {
					gm.sendPackets(new S_SystemMessage("推送系統未處於啟用狀態。"));
					return;
				}
				PushItemController.setPushSystem(false);
				gm.sendPackets(new S_SystemMessage("推送系統已停止運行。"));
			} else {
				gm.sendPackets(new S_SystemMessage("錯誤的請求。"));
			}
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage(".推送系統 [開/關]"));
		}
	}

	private void bug_race_rate(L1PcInstance pc, String s) {
		try {
			StringTokenizer tok = new StringTokenizer(s);
			int num = Integer.parseInt(tok.nextToken());
			int speed = Integer.parseInt(tok.nextToken());
			BugRaceController.getInstance().setSpeed(num - 1, speed);
			pc.sendPackets(String.format("已將第 %d 條車道的速度更改為 %d。", num, speed));
		} catch (Exception e) {
			pc.sendPackets("指令：請輸入 '變更控制 [車道] [速度]'。");
		}
	}

	private void bug_race_check(L1PcInstance pc) {
		try {
			for (int i = 0; i < 5; ++i)
				pc.sendPackets(String.format("車道：%d, 速度：%d", i + 1, BugRaceController.getInstance().getSpeed(i)));
		} catch (Exception e) {
			pc.sendPackets("指令：請輸入 '檢查控制'。");
		}
	}

	private FastTable<L1NpcInstance> list = new FastTable<L1NpcInstance>();

	public void add_list(L1NpcInstance npc) {
		synchronized (list) {
			if (!list.contains(npc))
				list.add(npc);
		}
	}

	private void get_delete_gfx(L1PcInstance pc) {
		try {
			Iterator<L1NpcInstance> iter = list.iterator();
			L1NpcInstance npc = null;

			while (iter.hasNext()) {
				npc = iter.next();
				if (npc == null)
					continue;
				npc.deleteMe();
			}

			list.clear();
			pc.sendPackets("'圖片刪除' 已完成。");
		} catch (Exception e) {
			pc.sendPackets("指令：請輸入 '.刪除圖片'。");
		}
	}

	private static Random _rnd = new Random();

	public static Random getRnd() {
		return _rnd;
	}

	private void maphack(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String on = st.nextToken();
			if (on.equalsIgnoreCase("開")) {
				gm.sendPackets(new S_Ability(3, true));
				gm.sendPackets("螢幕燈光狀態：開");
			} else if (on.equals("關")) {
				gm.sendPackets(new S_Ability(3, false));
				gm.sendPackets("螢幕燈光狀態：關");
			}
		} catch (Exception e) {
			gm.sendPackets("請設定 .light 為 [開, 關]。");
		}
	}

	public static int get_random(int min, int max) {
		if (min > max)
			return min;
		return _rnd.nextInt(max - min + 1) + min;
	}

	private void rankGiveItemStop(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String on = st.nextToken();
			if (on.equalsIgnoreCase("全部")) {
				for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
					String[] rank_item_id = MJRankLoadManager.MRK_TOP_GIVE_ITEM_ID.split(",");
					String[] rank_item_enchant = MJRankLoadManager.MRK_TOP_GIVE_ITEM_ENCHANT.split(",");
					for (int r = 0; r < rank_item_id.length; r++) {
						int itemid = Integer.valueOf(rank_item_id[r]);
						int enchant = Integer.valueOf(rank_item_enchant[r]);
						target.getInventory().consumeRankItem(target, itemid, 1, enchant);
					}
				}

				gm.sendPackets("所有角色的排名獎勵物品已被刪除。");
			} else {
				L1PcInstance target = L1World.getInstance().getPlayer(on);
				if (target == null) {
					gm.sendPackets(on + " 不存在於這個世界。");
					return;
				}

				String[] rank_item_id = MJRankLoadManager.MRK_TOP_GIVE_ITEM_ID.split(",");
				String[] rank_item_enchant = MJRankLoadManager.MRK_TOP_GIVE_ITEM_ENCHANT.split(",");
				for (int r = 0; r < rank_item_id.length; r++) {
					int itemid = Integer.valueOf(rank_item_id[r]);
					int enchant = Integer.valueOf(rank_item_enchant[r]);
					target.getInventory().consumeRankItem(target, itemid, 1, enchant);
				}

				gm.sendPackets(on + " 的排名獎勵物品已被刪除。");
			}
		} catch (Exception e) {
			gm.sendPackets("請設定 .deleteRankingRewards 為 [全部或使用者名稱]。");
		}
	}

	// TODO: 新增 GM 命令
	private void showHelp(L1PcInstance gm) {
		// ServerExplainTable.getInstance().server_Explain(gm, 1);
		// gm.sendPackets("1\r\n2\r\n");
		StringBuilder sb = new StringBuilder();
		sb.append("━━━━━━━━━━━━ 管理員命令 ━━━━━━━━━━━━").append("\r\n");
		sb.append("\\aG[基本]:.回歸 .出現 .召喚 .位置 .誰 .村莊 .聊天 .監獄 .去 .回復").append("\r\n");
		sb.append("\\aO[設定]:.無人 .變身 .等級 .PK .重新載入 .退出血盟 .自動拾取 .禁止丟棄 .禁止倉庫 .禁止交易").append("\r\n");
		sb.append("\\aO　　　.自動拾取 .清理全部 .等待開啟 .刪除信件 .刪除背包 .攻城時間 .開始攻城 .死鎖").append("\r\n");
		sb.append("\\aO　　　.結束攻城 .操作血盟 .給予會館 .平衡 .重新載入商店 .血盟經驗 .重置 (不可逆)").append("\r\n");
		sb.append("\\aO　　　.en (NPC 下降) .清理 .垃圾清理 .更改埠 .檢查商店 .使用類型").append("\r\n");
		sb.append("\\aY[賬戶]:.設置 .信息 .賬戶 .賬戶扣押 .永久封禁 .IP封禁 .區域封禁 .賬戶信息 .添加賬戶").append("\r\n");
		sb.append("\\aY　　　.改密碼 .懲罰 .封IP .封商店 .刪除用戶背包 .角色扣押 .解除角色扣押").append("\r\n");
		sb.append("\\aY　　　.檢查賬戶 .變更問答 .恢復經驗 .檢查賬戶 .封血盟 .周邊封禁 .廣域封禁").append("\r\n");
		sb.append("\\aY　　　.解除扣押 .扣押列表 .賬戶停權 .清理角色").append("\r\n");
		sb.append("\\aW[檢查]:.移動 .驅逐 .查詢 .監視 .搜尋 .禁言 .公開禁言 .解除禁言 .檢查背包").append("\r\n");
		sb.append("\\aW[伺服器]:.生成 .部署 .怪物 .道具 .全域贈送").append("\r\n");
		sb.append("\\aQ[增益]:.增益 .復活 .速度 .全域增益 .個人增益 .背包圖片 .攻速檢查 .移速檢查 .魔法檢查").append("\r\n");
		sb.append("\\aQ　　　.等級禮物 .圖片 .召喚 .圖標 .圖片 .效果 .等級提升").append("\r\n");
		sb.append("\\aH[其他]:.PVP .隱身 .伺服器保存 .隊伍召喚 .機器人 .AI .清理人偶 .管理員商店 (2~3) .檢查角色").append("\r\n");
		sb.append("\\aH　　　.結束自動狩獵 .給予經驗藥水 (全世界) .給予BOSS召喚卷 .給予紅寶石 .活動").append("\r\n");
		sb.append("\\aH　　　.自動狩獵結束 .經驗藥水提供 (全球) .BOSS召喚卷提供 .托帕茲提供 .活動").append("\r\n");
		sb.append("\\aH　　　.無交易 .賬戶 .Buff圖標尤里圖標 .桌面 .代辦 .代務 .製作檢查 .電傳 .刪除物品").append("\r\n");
		sb.append("\\aH　　　.提供恩硬幣 .收回恩硬幣 .檢查恩硬幣 .充值恩硬幣 .VIP .重載隱藏地點 .召喚事件").append("\r\n");
		sb.append("\\aH　　　.突襲 .實例 .排名系統 .潛伏模式 .解除潛伏 .狂熱檢查 .擊殺圖表 .坐標").append("\r\n");
		sb.append("\\aH　　　.原型 .Ping .網絡安全 .保護模式 .網絡 .轉儲 .對象列表").append("\r\n");
		sb.append("\\aH　　　.顯示設置 .比特幣 .戰鬥系統 .驗證碼 (防自動) .附魔系統 (tb_enchanties...)").append("\r\n");
		sb.append("\\aH　　　.角色狀態 .詛咒 .詛咒重置 .地牢計時器 .血盟信息 .血盟標記 .查找 .BOSS .探索禮物").append("\r\n");
		sb.append("\\aH　　　.武器附魔成功 .防具附魔成功 .人偶盒 .腐化工作 .冰雪工作 .惡魔工作 .死亡工作").append("\r\n");
		sb.append("\\aH　　　.歐貝工作 .防具工作 .卡巴工作 .艾薩貝 .沙蟲 .祝福能量 .統治BOSS .任務").append("\r\n");
		sb.append("\\aH　　　.符石卡巴 .符石迪斯 .符石德佩 .符石防具 .決鬥 .寵物 .鬥犬 .屬性").append("\r\n");
		sb.append("\\aH　　　.查看手續費 .註冊銷售 .申請購買 .競技場 .結束比賽 .伺服器移動 .對抗戰 .伺服器轉移").append("\r\n");
		sb.append("\\aH　　　.代碼 .充電代碼 .充電狀態 .取款狀態 .硬幣手續費 .概率 .查看概率 .重置概率").append("\r\n");
		sb.append("\\aH　　　.格蘭卡因 .格蘭卡因重置 .書系統 .白名單IP .開關 .強力球 .煙草 .煙草控制").append("\r\n");
		sb.append("\\aH　　　.排隊 .刪除排名獎勵 .打印版本 .應用中心 .註冊優惠券 .生成地圖 .重新生成 .解封申請").append("\r\n");
		sb.append("\\aH　　　.數據包苦工 .技能檢查 .宏 .S Hack .超級重置 .龍突襲 .螞蟻巢穴 (測試用)").append("\r\n");
		sb.append("\\aH　　　.提供愛因 .潛力 .設置購買限制 .購買限制 .購買限制時間 .賦予祝福 .製作成功").append("\r\n");
		sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━").append("\r\n");
		if (gm.getName().equalsIgnoreCase("網頁管理員")) {
			gm.sendPackets(sb.toString());
		} else {
			gm.sendPackets(new S_ChatPacket(gm, sb.toString()));
		}
	}

	private static Map<Integer, String> _lastCommands = new HashMap<Integer, String>();

	private void searchDatabase(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			int type = Integer.parseInt(tok.nextToken());
			String name = tok.nextToken();
			searchObject(gm, type, "%" + name + "%");
		} catch (Exception e) {
			gm.sendPackets(".搜尋 [0~5] [名稱] 請輸入。");
			gm.sendPackets("0=雜物, 1=武器, 2=防具, 3=NPC, 4=變身, 5=NPC(gfxid)");
		}
	}

	private void clearAll(L1PcInstance gm) {
		int cnt = 0;
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				mon.die(gm);
				cnt++;
			}

		}
		gm.sendPackets("你已經殺死了 " + cnt + " 隻怪物。");
	}

	private void doolyHelp1(L1PcInstance gm) {
		gm.sendPackets("----------------------------------------------------");
		gm.sendPackets("存款帳戶：/ 無");
		gm.sendPackets("帳號：無");
		gm.sendPackets("----------------------------------------------------");
	}

	private void autoloot(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String type = tok.nextToken();
			if (type.equalsIgnoreCase("重新載入")) {
				AutoLoot.getInstance();
				AutoLoot.reload();
				gm.sendPackets("自動撿取設置已重新載入。");
			} else if (type.equalsIgnoreCase("搜尋")) {
				String nameid = tok.nextToken();
				Selector.exec(
						String.format("select b.item_id, b.name FROM autoloot as a inner join (select item_id, name from etcitem) as b on a.item_id=b.item_id where b.name like '%%%s%%'", nameid),
						new FullSelectorHandler() {
							@Override
							public void result(ResultSet rs) throws Exception {
								while (rs.next())
									gm.sendPackets(String.format("[%d]%s", rs.getInt("item_id"), rs.getString("name")));
							}
						});
				Selector.exec(String.format("select b.item_id, b.name FROM autoloot as a inner join (select item_id, name from armor) as b on a.item_id=b.item_id where b.name like '%%%s%%'", nameid),
						new FullSelectorHandler() {
							@Override
							public void result(ResultSet rs) throws Exception {
								while (rs.next())
									gm.sendPackets(String.format("[%d]%s", rs.getInt("item_id"), rs.getString("name")));
							}
						});
				Selector.exec(String.format("select b.item_id, b.name FROM autoloot as a inner join (select item_id, name from weapon) as b on a.item_id=b.item_id where b.name like '%%%s%%'", nameid),
						new FullSelectorHandler() {
							@Override
							public void result(ResultSet rs) throws Exception {
								while (rs.next())
									gm.sendPackets(String.format("[%d]%s", rs.getInt("item_id"), rs.getString("name")));
							}
						});
			} else {
				String nameid = tok.nextToken();
				int itemid = 0;
				try {
					itemid = Integer.parseInt(nameid);
				} catch (NumberFormatException e) {
					itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(nameid);
					if (itemid == 0) {
						gm.sendPackets("未發現該項目。");
						return;
					}
				}

				L1Item temp = ItemTable.getInstance().getTemplate(itemid);
				if (temp == null) {
					gm.sendPackets("未發現該物品。");
					return;
				}
				if (type.equalsIgnoreCase("新增")) {
					if (AutoLoot.getInstance().isAutoLoot(itemid)) {
						gm.sendPackets("已存在於自動撿取清單中。");
						return;
					}
					AutoLoot.getInstance().storeId(itemid);
					gm.sendPackets("已新增至自動撿取清單。");
				} else if (type.equalsIgnoreCase("刪除")) {
					if (!AutoLoot.getInstance().isAutoLoot(itemid)) {
						gm.sendPackets("自動撿取清單中沒有該物品。");
						return;
					}
					gm.sendPackets("已從自動撿取清單中刪除。");
					AutoLoot.getInstance().deleteId(itemid);
				}
			}
		} catch (Exception e) {
			gm.sendPackets(".自動撿取 重新加載");
			gm.sendPackets(".自動撿取 新增|刪除 物品ID|名稱");
			gm.sendPackets(".自動撿取 搜索 名稱");
		}
	}

	private void LargeAreaBan(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			int range = Integer.parseInt(st.nextToken());
			Integer reason = S_LoginResult.banServerCodes.get(st.nextToken());
			if (reason == null)
				throw new Exception("");
			int count = 0;
			IpTable iptable = IpTable.getInstance();
			pc.sendPackets("----------------------------------------------------");
			for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, range)) {
				Account.ban(player.getAccountName(), reason); // 封鎖該帳號。
				iptable.banIp(player.getNetConnection().getIp()); // 將 IP 添加到封鎖列表中。.
				pc.sendPackets(player.getName() + ", (" + player.getAccountName() + ")");
				player.logout();
				player.getNetConnection().kick();
				count++;
			}
			pc.sendPackets("已永久驅逐周圍的 " + count + " 名用戶。");
			pc.sendPackets("----------------------------------------------------");

		} catch (Exception e) {
			pc.sendPackets(".周圍封禁 [範圍] [封禁原因代碼]");
		}
	}

	private void spawnNotifyOnOff(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String on = st.nextToken();
			if (on.equalsIgnoreCase("攝像")) {
				gm.setBossNotify(true);
				if (on.equalsIgnoreCase("開")) {
					gm.sendPackets("Boss通知:開 (Boss召喚及通知已啟動)");
				} else if (on.equals("關")) {
					gm.setBossNotify(false);
					gm.sendPackets("Boss通知:關 (Boss召喚及通知已結束)");
				}
			}
		} catch (Exception e) {
			gm.sendPackets("請將Boss通知設置為 [.bossnotify 開, 關]。目前狀態：(" + (gm.isBossNotify() == true ? "已開啟" : "已關閉") + ")。");
		}
	}

	public void execute1(L1PcInstance pc, String cmdName, String arg) {
		try {

			StringTokenizer stringtokenizer = new StringTokenizer(arg);

			int count = Integer.parseInt(stringtokenizer.nextToken());
			L1ItemInstance adena = pc.getInventory().storeItem(L1ItemId.ADENA, count);

			if (adena != null) {
				pc.sendPackets((new StringBuilder()).append(count).append(" 金幣已創建。").toString());
			}
		} catch (Exception e) {
			pc.sendPackets((new StringBuilder()).append("請以 .金幣 [數量] 的格式輸入。").toString());
		}
	}

	public void executeitem(L1PcInstance pc, String param, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String nameid = st.nextToken();

			int itemid = 0;
			try {
				itemid = Integer.parseInt(nameid);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(nameid);
				if (itemid == 0) {
					pc.sendPackets("未找到指定的物品。");
					return;
				}
			}

			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}

			L1Item temp = ItemTable.getInstance().getTemplate(itemid);
			if (temp != null) {
				if (temp.isStackable()) {
					L1ItemInstance item = null;
					for (int i = itemid; i <= itemid + count; i++) {
						item = ItemTable.getInstance().createItem(i);
						item.setEnchantLevel(0);
						item.setCount(1);
						item.setIdentified(true);
						if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
							pc.getInventory().storeItem(item, true);
						}
					}
					pc.sendPackets(new S_SystemMessage("\fY物品:[\\aA" + item.getLogName() + "\fY] 物品ID:[\\aA" + itemid + "\fY] 附魔:[\\aA" + count + "\fY] \\aG已創建"));
				} else {
					L1ItemInstance item = null;
					for (int i = itemid; i <= itemid + count; i++) {
						item = ItemTable.getInstance().createItem(i);
						if (item == null)
							continue;
						item.setIdentified(true);
						if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
							pc.getInventory().storeItem(item, true);
						} else {
							break;
						}
					}
					pc.sendPackets(new S_SystemMessage("\fY物品:[\\aA" + item.getLogName() + "\fY] 物品ID:[\\aA" + itemid + "\fY] 附魔:[\\aA" + count + "\fY] \\aG已創建"));
					;
				}
			} else {
				pc.sendPackets("指定ID的物品不存在。");
			}
		} catch (Exception e) {
			pc.sendPackets(".物品 [名稱] [數量] [附魔] [屬性1~20] [確認0~1] ");
		}
	}

	public void execute(L1PcInstance pc, String param, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String nameid = st.nextToken();
			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			int attrenchant = 0;
			if (st.hasMoreTokens()) {
				attrenchant = Integer.parseInt(st.nextToken());
			}

			int itemid = 0;
			try {
				itemid = Integer.parseInt(nameid);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(nameid);
				if (itemid == 0) {
					pc.sendPackets("" + st.nextToken() + " 物品未找到。");
					return;
				}
			}
			L1Item temp = ItemTable.getInstance().getTemplate(itemid);
			if (temp != null) {
				if (temp.isStackable()) {
					L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
					item.setEnchantLevel(0);
					item.setCount(count);
					item.setIdentified(true);
					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
						pc.getInventory().storeItem(item, true);
						pc.sendPackets("\fY物品:[\\aA" + item.getLogName() + "\fY] 物品ID:[\\aA" + itemid + "\fY] 附魔:[\\aA" + enchant + "\fY] \\aG已創建");
					}
				} else {
					L1ItemInstance item = null;
					int createCount;
					for (createCount = 0; createCount < count; createCount++) {
						item = ItemTable.getInstance().createItem(itemid);
						item.setEnchantLevel(enchant);
						item.setAttrEnchantLevel(attrenchant);
						item.setIdentified(true);
						if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
							pc.getInventory().storeItem(item, true);
						} else {
							break;
						}
					}
					if (createCount > 0) {
						pc.sendPackets("\fY物品:[\\aA" + item.getLogName() + "\fY] 物品ID:[\\aA" + itemid + "\fY] 附魔:[\\aA" + enchant + "\fY] \\aG已創建");
					}
				}
			} else {
				pc.sendPackets("指定 ID 的物品不存在。");
			}
		} catch (Exception e) {
			pc.sendPackets(".物品 [名稱] [數量] [附魔] [屬性1~20] [確認0~1] ");
		}
	}

	private void huntEvent(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String type = st.nextToken();
			if (type.equalsIgnoreCase("開始")) {
				Config.ServerAdSetting.HuntEvent = true;
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "狩獵活動正在進行中。請把阿登世界的所有鹿和野豬都抓住！"));
				L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "\\aD狩獵活動正在進行中。請把所有鹿和野豬都抓住！"));
			} else if (type.equalsIgnoreCase("結束")) {
				Config.ServerAdSetting.HuntEvent = false;
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "狩獵活動已經結束。"));
				L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "\\aD狩獵活動已經結束。"));
			}
		} catch (Exception e) {
			gm.sendPackets(".狩獵 [開始 和 結束]");
		}
	}

	private void Event(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String event = st.nextToken();
			String time1 = st.nextToken();
			int time = Integer.parseInt(time1);
			if (event.equalsIgnoreCase("龍獵人")) {
				L1SpawnUtil.Gmspawn(7320159, 33441, 32813, (short) 4, 6, time * 60 * 60 * 1000);

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "龍獵人的祝福活動已經開始。將在 (" + time + ") 小時內出現在奇岩村莊。"));
				L1World.getInstance().broadcastServerMessage("將在 (" + time + ") 小時內出現在奇岩村莊。");

			} else if (event.equalsIgnoreCase("特級勇士1")) {
				L1SpawnUtil.Gmspawn(526, 33447, 32793, (short) 4, 5, time * 60 * 60 * 1000);// 奇岩

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "特級勇士活動(1) 已經開始。將在 (" + time + ") 小時內出現在奇岩村莊。"));
				L1World.getInstance().broadcastServerMessage("將在 (" + time + ") 小時內出現在奇岩村莊。");

			} else if (event.equalsIgnoreCase("特級勇士2")) {
				L1SpawnUtil.Gmspawn(529, 33445, 32791, (short) 4, 5, time * 60 * 60 * 1000);// 奇岩

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "特級勇士活動(2) 已經開始。將在 (" + time + ") 小時內出現在奇岩村莊。"));
				L1World.getInstance().broadcastServerMessage("將在 (" + time + ") 小時內出現在奇岩村莊。");

			} else if (event.equalsIgnoreCase("特級勇士3")) {
				L1SpawnUtil.Gmspawn(530, 33449, 32795, (short) 4, 5, time * 60 * 60 * 1000);// 奇岩

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "特級勇士活動(3) 已經開始。將在 (" + time + ") 小時內出現在奇岩村莊。"));
				L1World.getInstance().broadcastServerMessage("將在 (" + time + ") 小時內出現在奇岩村莊。");

			} else if (event.equalsIgnoreCase("特級勇士4")) {
				L1SpawnUtil.Gmspawn(531, 33444, 32797, (short) 4, 5, time * 60 * 60 * 1000);// 奇岩

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "特級勇士活動(4) 已經開始。將在 (" + time + ") 小時內出現在奇岩村莊。"));
				L1World.getInstance().broadcastServerMessage("將在 (" + time + ") 小時內出現在奇岩村莊。");
			}
		} catch (Exception e) {
			gm.sendPackets("[.活動] [活動名稱] [時間]");
			gm.sendPackets("[活動名稱]: 龍獵人/特級勇士1~4");
		}
	}

	private void Event1(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String event = st.nextToken();

			if (event.equalsIgnoreCase("部隊1")) {
				int rx = _random.nextInt(5);
				int ry = _random.nextInt(5);

				int rx1 = _random.nextInt(10);
				int ry1 = _random.nextInt(10);

				int rx2 = _random.nextInt(15);
				int ry2 = _random.nextInt(15);

				int ux = 32926 + rx;
				int uy = 33250 + ry;
				int um = 4;

				int ux1 = 32926 + rx1;
				int uy1 = 33250 + ry1;

				int ux2 = 32926 + rx2;
				int uy2 = 33250 + ry2;

				L1SpawnUtil.spawnfieldboss(ux, uy, (short) um, 45545, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux1, uy1, (short) um, 7000091, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 7000092, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 7000089, 0, 0, 0);
				System.out.println("▶召喚活動開始：第一部隊出現◀");

				L1World.getInstance().broadcastServerMessage("\\aA召喚活動：梅蒂斯的近衛部隊將在稍後出現。");

			} else if (event.equalsIgnoreCase("部隊2")) {
				int rx = _random.nextInt(5);
				int ry = _random.nextInt(5);

				int rx1 = _random.nextInt(10);
				int ry1 = _random.nextInt(10);

				int rx2 = _random.nextInt(15);
				int ry2 = _random.nextInt(15);

				int ux = 32926 + rx;
				int uy = 33250 + ry;
				int um = 4;

				int ux1 = 32926 + rx1;
				int uy1 = 33250 + ry1;

				int ux2 = 32926 + rx2;
				int uy2 = 33250 + ry2;

				L1SpawnUtil.spawnfieldboss(ux, uy, (short) um, 45203, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux1, uy1, (short) um, 45206, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 45257, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 45263, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux1, uy1, (short) um, 45341, 0, 0, 0);
				System.out.println("▶召喚活動開始：第二部隊出現◀");

				L1World.getInstance().broadcastServerMessage("\\aA召喚活動：梅蒂斯的近衛部隊將在不久後出現。");

			} else if (event.equalsIgnoreCase("部隊3")) {
				int rx = _random.nextInt(5);
				int ry = _random.nextInt(5);

				int rx1 = _random.nextInt(10);
				int ry1 = _random.nextInt(10);

				int rx2 = _random.nextInt(15);
				int ry2 = _random.nextInt(15);

				int ux = 32926 + rx;
				int uy = 33250 + ry;
				int um = 4;

				int ux1 = 32926 + rx1;
				int uy1 = 33250 + ry1;

				int ux2 = 32926 + rx2;
				int uy2 = 33250 + ry2;

				L1SpawnUtil.spawnfieldboss(ux, uy, (short) um, 707001, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux1, uy1, (short) um, 707002, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 707007, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 707008, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 707013, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 707015, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 707016, 0, 0, 0);
				System.out.println("▶召喚活動開始：第三部隊出現◀");

				L1World.getInstance().broadcastServerMessage("\\aA召喚活動：最終的梅蒂斯近衛部隊將出現。");
			}
		} catch (Exception e) {
			gm.sendPackets("[.召喚活動] [活動名稱]");
			gm.sendPackets("[召喚活動名稱]: 部隊1/部隊2/ 部隊3/部隊4/部隊5");
		}
	}

	private void LargeAreaIPBan(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);

			String charName = st.nextToken();
			String banIp = "";

			L1PcInstance player = L1World.getInstance().getPlayer(charName);

			if (player != null) {
				banIp = player.getNetConnection().getIp();

				String[] banIpArr = banIp.split("\\.");

				IpTable iptable = IpTable.getInstance();
				pc.sendPackets("----------------------------------------------------");
				l1j.server.server.Account.ban(player.getAccountName(), S_LoginResult.BANNED_REASON_NOMANNER); // 該帳號
				// 封鎖用戶。
				player.logout();
				player.getNetConnection().kick();
				for (int i = 1; i <= 255; i++) {
					iptable.banIp(banIpArr[0] + "." + banIpArr[1] + "." + banIpArr[2] + "." + i);
				}

				pc.sendPackets("IP: " + banIpArr[0] + "." + banIpArr[1] + "." + banIpArr[2] + ".封鎖範圍從 1 到 255（廣域封鎖）。");
				pc.sendPackets("----------------------------------------------------");
			}
		} catch (Exception e) {
			pc.sendPackets(".廣域封鎖 [範圍]");
		}
	}

	private void hunt(L1PcInstance pc, String cmd) {
		int price1 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[0];
		int price2 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[1];
		int price3 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[2];
		try {
			StringTokenizer tok = new StringTokenizer(cmd);
			String name = tok.nextToken();
			if (name == null || name.equals(""))
				throw new Exception();

			L1PcInstance target = L1World.getInstance().getPlayer(name);
			if (target == null) {
				pc.sendPackets(String.format("無法找到 %s。", name));
				return;
			}
			if (target.hasSkillEffect(L1SkillId.USER_WANTED3)) {
				pc.sendPackets(String.format("%s 已經是三級通緝犯。", name));
				return;
			}
			if (target.isGm()) {
				pc.sendPackets("無法對管理員設置通緝。");
				return;
			}
			if (target.get_Wanted_Level() == 0) {
				if (!(pc.getInventory().checkItem(40308, price1))) {
					pc.sendPackets("金幣不足（" + price1 + "）");
					return;
				}
			} else if (target.get_Wanted_Level() == 1) {
				if (!(pc.getInventory().checkItem(40308, price2))) {
					pc.sendPackets("金幣不足（" + price2 + "）");
					return;
				}
			} else if (target.get_Wanted_Level() == 2) {
				if (!(pc.getInventory().checkItem(40308, price3))) {
					pc.sendPackets("金幣不足（" + price3 + "）");
					return;
				}
			}

			if (target.get_Wanted_Level() == 0) {
				String message = String.format("%s 已對 %s 設置了通緝。(1級)", pc.getName(), target.getName());
				pc.sendPackets(message);
				target.sendPackets(message);
				target.setSkillEffect(L1SkillId.USER_WANTED1, -1);
				target.add_Wanted_Level();
				target.doWanted(true, false);
				pc.getInventory().consumeItem(40308, price1);
			} else if (target.hasSkillEffect(L1SkillId.USER_WANTED1)){
				String message = String.format("%s 已對 %s 設置了通緝。(2級)", pc.getName(), target.getName());
				pc.sendPackets(message);
				target.sendPackets(message);
				target.removeSkillEffect(L1SkillId.USER_WANTED1);
				target.setSkillEffect(L1SkillId.USER_WANTED2, -1);
				target.add_Wanted_Level();
				target.doWanted(true, false);
				pc.getInventory().consumeItem(40308, price2);
			} else if (target.hasSkillEffect(L1SkillId.USER_WANTED2)){
				String message = String.format("%s 已對 %s 設置了通緝。(3級)", pc.getName(), target.getName());
				pc.sendPackets(message);
				target.sendPackets(message);
				target.removeSkillEffect(L1SkillId.USER_WANTED2);
				target.setSkillEffect(L1SkillId.USER_WANTED3, -1);
				target.add_Wanted_Level();
				target.doWanted(true, false);
				pc.getInventory().consumeItem(40308, price3);
			}
		} catch (Exception e) {
			pc.sendPackets(".通緝 [角色名]");
			pc.sendPackets("各階段累計效果：近距離傷害減少3、命中減少3、遠距離傷害減少3、命中減少3、SP減少3、傷害減少3、防禦力（AC）減少3");
			pc.sendPackets("1級: " + price1 + ", 2級: " + price2 + ", 3級: " + price3);
		}
	}

	private void serversave(L1PcInstance pc) {
		Saveserver();// 伺服器保存方法宣告
		pc.sendPackets("伺服器保存完成。");
	}

	/** 伺服器保存 */
	private void Saveserver() {
		/** 呼叫所有玩家 */
		Collection<L1PcInstance> list = null;
		list = L1World.getInstance().getAllPlayers();
		for (L1PcInstance player : list) {
			if (player == null)
				continue;
			try {
				/** 保存玩家數據 */
				player.save();
				/** 保存背包 */
				player.saveInventory();

			} catch (Exception ex) {
				/** 發生例外情況，只保存背包 */
				player.saveInventory();
				System.out.println("保存命令錯誤（只保存了背包）: " + ex);
			}
		}
	}

	private void privateShop(L1PcInstance pc) {
		try {
			if (!pc.isPrivateShop()) {
				pc.sendPackets("可以在個人商店狀態下使用。");
				return;
			}

			GameClient client = pc.getNetConnection();
			pc.setNetConnection(null);
			// 重生註釋
			// pc.stopHpMpRegeneration();
			pc.setUnmannedShop(true);
			try {
				pc.save();
				pc.saveInventory();
			} catch (Exception e) {
			}
			client.setActiveChar(null);
			client.setStatus2(MJClientStatus.CLNT_STS_AUTHLOGIN);
			client.sendPacket(new S_Unknown2(1)); // 為了重置按鈕的結構變更	// Episode U

		} catch (Exception e) {
		}
	}

	private void GiveHouse(L1PcInstance pc, String poby) {
		try {
			StringTokenizer st = new StringTokenizer(poby);
			String pobyname = st.nextToken();
			int pobyhouseid = Integer.parseInt(st.nextToken());
			L1PcInstance target = L1World.getInstance().getPlayer(pobyname);
			if (target != null) {
				if (target.getClanid() != 0) {
					L1Clan TargetClan = L1World.getInstance().getClan(target.getClanid());
					L1House pobyhouse = HouseTable.getInstance().getHouseTable(pobyhouseid);
					TargetClan.setHouseId(pobyhouseid);
					ClanTable.getInstance().updateClan(TargetClan);
					pc.sendPackets(target.getClanname() + " 血盟已獲得 " + pobyhouse.getHouseName() + " 號房屋。");
					for (L1PcInstance tc : TargetClan.getOnlineClanMember()) {
						tc.sendPackets("從梅蒂斯獲得了 " + pobyhouse.getHouseName() + " 號房屋。");
					}
				} else {
					pc.sendPackets(target.getName() + " 不屬於任何血盟。");
				}
			} else {
				pc.sendPackets(new S_ServerMessage(73, pobyname));
			}
		} catch (Exception e) {
			pc.sendPackets(".給予宅邸 [要給予的血盟成員] [宅邸編號]");
		}
	}

	private void castleWarStart(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String name = tok.nextToken();
			int minute = Integer.parseInt(tok.nextToken());
			Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
			if (minute != 0)
				cal.add(Calendar.MINUTE, minute);

			MJCastleWar war = MJCastleWarBusiness.getInstance().findWar(name);
			if (war == null)
				throw new Exception();

			war.nextCalendar(cal);
			MJCastleWarBusiness.getInstance().updateCastle(war.getCastleId());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			gm.sendPackets(String.format(".攻城時間已更改為 %s。", formatter.format(cal.getTime())));
			gm.sendPackets(param + " 分鐘後攻城開始。");
			formatter = null;
		} catch (Exception e) {
			gm.sendPackets(".攻城開始 [城堡名稱（兩個字）] [分鐘]");
		}
	}

	private void castleWarExit(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String name = tok.nextToken();
			MJCastleWar war = MJCastleWarBusiness.getInstance().findWar(name);
			if (war == null)
				throw new Exception();

			war.close();
			gm.sendPackets(name + " 攻城已結束。");
		} catch (Exception e) {
			gm.sendPackets(".攻城結束 [城堡名稱（兩個字）]");
		}
	}

	private void party(L1PcInstance gm, String cmdName) {
		try {
			StringTokenizer tok = new StringTokenizer(cmdName);
			String cmd = tok.nextToken();
			if (cmd.equals("周圍")) {
				L1Party party = new L1Party();
				if (gm.getParty() == null) {
					party.addMember(gm);
				} else {
					party = gm.getParty();
				}
				int range = 3;// 當前周圍3格
				for (L1PcInstance Targetpc : L1World.getInstance().getVisiblePlayer(gm, range)) {
					if (gm.getName().equals(Targetpc.getName())) {
						continue;
					}
					if (Targetpc.getParty() != null) {
						continue;
					} // 排除有組隊的用戶
					if (Targetpc.isPrivateShop()) {
						continue;
					} // 排除無人
					party.addMember(Targetpc);
					gm.sendPackets(Targetpc.getName() + " 已加入我的隊伍。");
				}
				gm.sendPackets(range + " 格內的用戶已加入我的隊伍。");
			} else if (cmd.equals("畫面")) {
				L1Party party = new L1Party();
				if (gm.getParty() == null) {
					party.addMember(gm);
				} else {
					party = gm.getParty();
				}
				for (L1PcInstance Targetpc : L1World.getInstance().getVisiblePlayer(gm)) {
					if (gm.getName().equals(Targetpc.getName())) {
						continue;
					}
					if (Targetpc.getParty() != null) {
						continue;
					}
					if (Targetpc.isPrivateShop()) {
						continue;
					}
					party.addMember(Targetpc);
					gm.sendPackets(Targetpc.getName() + " 已加入我的隊伍。");
				}
				gm.sendPackets("畫面內的用戶已加入我的隊伍。");
			} else if (cmd.equals("全部")) {
				L1Party party = new L1Party();
				if (gm.getParty() == null) {
					party.addMember(gm);
				} else {
					party = gm.getParty();
				}
				int range = 3;// 當前周圍3格
				for (L1PcInstance Targetpc : L1World.getInstance().getAllPlayers()) {
					if (gm.getName().equals(Targetpc.getName())) {
						continue;
					}
					if (Targetpc.getParty() != null) {
						continue;
					}
					if (Targetpc.isPrivateShop()) {
						continue;
					}
					party.addMember(Targetpc);
					gm.sendPackets(Targetpc.getName() + " 已加入我的隊伍。");
				}
				gm.sendPackets(range + " 格內的用戶已加入我的隊伍。");
			} else if (cmd.equals("加入")) {
				String TargetpcName = tok.nextToken();
				L1PcInstance TargetPc = L1World.getInstance().getPlayer(TargetpcName);
				if (TargetPc.getParty() != null) {
					gm.sendPackets(TargetPc.getName() + " 沒有隊伍。");
				} else {
					TargetPc.getParty().addMember(gm);
					gm.sendPackets(TargetPc.getName() + " 已加入隊伍。");
				}
			} else if (cmd.equals("邀請")) {
				String TargetpcName = tok.nextToken();
				L1PcInstance TargetPc = L1World.getInstance().getPlayer(TargetpcName);
				L1Party party = new L1Party();
				if (gm.getParty() == null) {
					party.addMember(gm);
				} else {
					party = gm.getParty();
				}
				if (TargetPc.getParty() != null) {
					TargetPc.getParty().kickMember(TargetPc);
				}
				party.addMember(TargetPc);
				gm.sendPackets(TargetPc.getName() + " 被強制加入我的隊伍。");
			} else if (cmd.equals("強制邀請")) {
				L1Party party = new L1Party();
				if (gm.getParty() == null) {
					party.addMember(gm);
				} else {
					party = gm.getParty();
				}
				// int range = 3;// 當前周圍3格
				for (L1PcInstance Targetpc : L1World.getInstance().getAllPlayers()) {
					if (gm.getName().equals(Targetpc.getName())) {
						continue;
					}
					if (Targetpc.isPrivateShop()) {
						continue;
					}
					if (Targetpc.getParty() != null) {
						Targetpc.getParty().kickMember(Targetpc);
					}
					party.addMember(Targetpc);
					gm.sendPackets(Targetpc.getName() + " 已加入我的隊伍。");
				}
				gm.sendPackets("已將在線用戶強制加入我的隊伍。");
			} else if (cmd.equals("解散")) {
				if (gm.getParty() == null) {
					gm.sendPackets("您目前沒有參加任何隊伍。");
				} else {
					gm.getParty().passLeader(gm);
					gm.sendPackets("隊長已被移除。");
				}
			}
		} catch (Exception e) {
			gm.sendPackets(".隊伍 [附近,畫面,全部,加入 (用戶名)]");
			gm.sendPackets(".隊伍 [邀請 (用戶名),強制邀請,解散]");
		}
	}

	private void effect(L1PcInstance pc, String param) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			int sprid = Integer.parseInt(stringtokenizer.nextToken());
			pc.sendPackets(new S_SkillSound(pc.getId(), sprid));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), sprid));
		} catch (Exception e) {
			pc.sendPackets("請輸入 .效果 [數字]。");
		}
	}

	private int minValue(int itemid) {
		try {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT * FROM shop WHERE item_id = ? AND selling_price NOT IN (-1) ORDER BY selling_price ASC limit 1");
				pstm.setInt(1, itemid);
				rs = pstm.executeQuery();
				if (rs.next()) {
					int temp = 0;
					if (rs.getInt("pack_count") > 1)
						temp = rs.getInt("selling_price") / rs.getInt("pack_count");
					else {
						temp = rs.getInt("selling_price");
					}
					int i = temp;
					return i;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private int 최대값(int itemid) {
		try {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT purchasing_price FROM shop WHERE item_id = ? ORDER BY purchasing_price DESC limit 1");
				pstm.setInt(1, itemid);
				rs = pstm.executeQuery();
				if (rs.next()) {
					int i = rs.getInt("purchasing_price");
					return i;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	private void changePort(L1PcInstance pc, String poby) {
		try {
			StringTokenizer token = new StringTokenizer(poby);
			int port = Integer.valueOf(token.nextToken()).intValue();

			Server.CreateServer().ChangePort(port);

			pc.sendPackets("\\aA端口: 端口已更改為 [\\aG" + port + "\\aA] 號。");
			pc.sendPackets("\\aH伺服器內部遊戲端口已更改！現有用戶仍然在線，新的連接不允許。");
			pc.sendPackets("\\aH若要招募新用戶，請將客戶端端口更改為 [\\aG" + port + "]\\aH 號。");
		} catch (Exception e) {
			pc.sendPackets("警告: 輸入方式錯誤。");
		}
	}

	private void checkShop(L1PcInstance gm) {
		try {
			ArrayList<Integer> itemids = new ArrayList<Integer>();
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int cnt;
			Iterator i$;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT item_id FROM shop");
				rs = pstm.executeQuery();
				while (rs.next()) {
					if (!itemids.contains(Integer.valueOf(rs.getInt("item_id")))) {
						itemids.add(Integer.valueOf(rs.getInt("item_id")));
					}
				}
				cnt = 0;
				for (i$ = itemids.iterator(); i$.hasNext();) {
					int itemid = ((Integer) i$.next()).intValue();
					int minPurchasePrice = minValue(itemid);
					int maxSalePrice = maxValue(itemid);
					if ((minPurchasePrice != 0) && (minPurchasePrice < maxSalePrice)) {
						gm.sendPackets(new S_ChatPacket(gm, "檢測到! [物品 " + itemid + " : [購買價 " + minPurchasePrice + "] [銷售價 " + maxSalePrice + "]"));
					}
					cnt++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String parseStat(String s) throws Exception {
		if (s.equalsIgnoreCase("力量"))
			return "str";
		else if (s.equalsIgnoreCase("敏捷"))
			return "dex";
		else if (s.equalsIgnoreCase("體質"))
			return "con";
		else if (s.equalsIgnoreCase("智力"))
			return "int";
		else if (s.equalsIgnoreCase("智慧"))
			return "wis";
		else if (s.equalsIgnoreCase("魅力"))
			return "cha";
		throw new Exception(s);
	}

	public void fullstat(L1PcInstance pc, String param) {
		try {
			String[] arr = param.split(" ");
			if (arr == null || arr.length < 2)
				throw new Exception();

			String s = parseStat(arr[0]);
			MJFullStater.running(pc, s, Integer.parseInt(arr[1]));
		} catch (Exception e) {
			pc.sendPackets(String.format(".屬性 [力量/敏捷/體質/智力/智慧/魅力] [增加數量] 剩餘屬性點 %d", pc.remainBonusStats()));
		}
	}

	private void coordinates(L1PcInstance pc) {
		try {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime2() + 5 > curtime) {
				long time = (pc.getQuizTime2() + 5) - curtime;
				pc.sendPackets(new S_SystemMessage(time + "秒後可以使用。"));
				return;
			}
			Updator.exec("UPDATE characters SET LocX=33432,LocY=32807,MapID=4 WHERE account_name=? and MapID not in (38,5001,99,997,5166,39,34,701,2000)", new Handler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, pc.getAccountName());
				}
			});
			pc.sendPackets(new S_SystemMessage("該帳號的所有角色已經移動到奇岩。"));
			pc.setQuizTime(curtime);
		} catch (Exception e) {
		}
	}

	private void standBy(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String status = st.nextToken();
			if (status.equalsIgnoreCase("開")) {
				if (Config.Login.StandbyServer) {
					gm.sendPackets("已經進入待機狀態。");
					return;
				}
				Config.Login.StandbyServer = true;
				L1World.getInstance().broadcastPacketToAll(new S_ChatPacket("伺服器進入開放待機狀態。某些功能已被限制。", Opcodes.S_MESSAGE));
			} else if (status.equalsIgnoreCase("關")) {
				if (!Config.Login.StandbyServer) {
					gm.sendPackets("現在不是待機狀態。");
					return;
				}

				GeneralThreadPool.getInstance().execute(new Runnable() {
					@Override
					public void run() {
						// Config.load();
						Config.Login.StandbyServer = false;

						S_PacketBox grn = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "伺服器已成功開啟。真心感謝大家的等待。");
						L1World.getInstance().broadcastServerMessage("\\aH伺服器已成功開啟。真心感謝大家的等待。");

						L1World.getInstance().getAllPlayerStream().filter(pc -> pc != null).forEach(pc -> {
							if (pc.getAI() != null) {
								if (pc.getAI().getBotType() == MJBotType.ILLUSION || pc.getAI().getBotType() == MJBotType.FISH || pc.getAI().getBotType() == MJBotType.WANDER)
									pc.getAI().setRandLawful();
							} else {
								pc.sendPackets(grn, false);
							}
						});
						grn.clear();
						return null;
					}
				});
			}
		} catch (Exception eee) {
			gm.sendPackets("請輸入 .開放待機 [開/關]。");
			gm.sendPackets("開 - 切換到開放待機狀態 | 關 - 以普通模式開始遊戲");
		}
	}

	private void garbageCollection(L1PcInstance gm) {
		gm.sendPackets("\f3垃圾回收（內存）已完成。（出現短暫的整體延遲）");
		System.out.println("正在進行強制垃圾回收（內存）處理。");
		System.gc();
		System.out.println("垃圾回收（內存）已完成。");
	}

	private void hold(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				holdnow(gm, target);
			} else {
				gm.sendPackets("沒有這樣的角色。");
			}
		} catch (Exception e) {
			gm.sendPackets("請輸入 .監獄 角色名稱。");
		}
	}

	private void holdnow(L1PcInstance gm, L1PcInstance target) {
		try {
			// L1Teleport.teleport(target, 32736, 32799, (short) 34, 5, true);
			// L1Teleport.teleport(target, 32835, 32782, (short) 701, 5, true);
			target.start_teleport(32835, 32782, 701, target.getHeading(), 18339, false, true);
			gm.sendPackets((new StringBuilder()).append(target.getName()).append(" 已被移動到監獄。").toString());
			target.sendPackets("你已被關押在監獄中。");
		} catch (Exception e) {
			_log.log(Level.SEVERE, "", e);
		}
	}

	private void nocall(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String pcName = tokenizer.nextToken();
			L1PcInstance target = null; // q
			target = L1World.getInstance().getPlayer(pcName);
			if (target != null) { // 目標
				// L1Teleport.teleport(target, 33437, 32812, (short) 4, 5,
				// true);
				target.start_teleport(33437, 32812, 4, target.getHeading(), 18339, true, true);
			} else {
				gm.sendPackets("該用戶 ID 目前未在線。");
			}
		} catch (Exception e) {
			gm.sendPackets("請輸入 .傳送 （角色名稱）。");
		}
	}

	private void searchObject(L1PcInstance gm, int type, String real_name_id_view) {
		try {
			String qry = null;
			switch (type) {
				case 0:// etcitem
					qry = String.format("select item_id, real_name_id_view, real_name_id from etcitem where real_name_id_view Like '%s'", real_name_id_view);
					break;
				case 1:// weapon
					qry = String.format("select item_id, real_name_id_view, real_name_id from weapon where real_name_id_view Like '%s'", real_name_id_view);
					break;
				case 2: // armor
					qry = String.format("select item_id, real_name_id_view, real_name_id from armor where real_name_id_view Like ' '%s'", real_name_id_view);
					break;
				case 3: // npc
					qry = String.format("select npcid, desc_view, note from npc where desc_view Like ' '%s'", real_name_id_view);
					break;
				case 4: // polymorphs
					qry = String.format("select polyid, name,id from polymorphs where name Like ' '%s'", real_name_id_view);
					break;
				case 5: // npc(gfxid)
					qry = String.format("select sprite_id, desc_view,note from npc where desc_view Like ' '%s'", real_name_id_view);
					break;
				default:
					gm.sendPackets("請輸入 .搜尋 [0~5] [名稱]。");
					gm.sendPackets("0=雜項, 1=武器, 2=護甲, 3=NPC, 4=變身, 5=NPC(gfxid)");
					return;
			}

			Selector.exec(qry, new FullSelectorHandler() {
				@Override
				public void result(ResultSet rs) throws Exception {
					int i = 0;
					while (rs.next()) {
						++i;
						gm.sendPackets(String.format("[%s]-[%s]-%s", rs.getString(1), rs.getString(2), rs.getString(3)));
					}
					gm.sendPackets(String.format("總共搜索到 [%d] 個項目。", i));
				}
			});
		} catch (Exception e) {
		}
	}

	private void redo(L1PcInstance pc, String arg) {
		try {
			String lastCmd = _lastCommands.get(pc.getId());
			if (arg.isEmpty()) {
				pc.sendPackets("重新執行命令 " + lastCmd + "。");
				handleCommands(pc, lastCmd);
			} else {
				StringTokenizer token = new StringTokenizer(lastCmd);
				String cmd = token.nextToken() + " " + arg;
				pc.sendPackets("重新執行命令 " + cmd + "。");
				handleCommands(pc, cmd);
			}
		} catch (Exception e) {
			e.printStackTrace();
			pc.sendPackets(".重新執行 命令錯誤");
		}
	}

	private void unprison(L1PcInstance pc, String param) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(param);
			if (target != null) {
				unprisonnow(pc, target);
			} else {
				pc.sendPackets(".村莊 角色名稱");
				pc.sendPackets("沒有這樣名稱的角色。");
			}
		} catch (Exception e) {
			pc.sendPackets(".村莊 角色名稱");
		}
	}

	private void unprisonnow(L1PcInstance gm, L1PcInstance target) {
		try {
			int i = 33437;
			int j = 32803;
			short k = 4;
			// L1Teleport.teleport(target, i, j, k, 5, false);
			target.start_teleport(i, j, k, 5, 18339, false, true);
			gm.sendPackets((new StringBuilder()).append(target.getName()).append(" 被移動到村莊。").toString());
		} catch (Exception e) {
			_log.log(Level.SEVERE, "", e);
		}
	}

	private void unprison2(L1PcInstance pc, String param) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(param);
			if (target != null) {
				unprisonnow2(pc, target);
			} else {
				pc.sendPackets(".隱藏 角色名稱");
				pc.sendPackets("沒有這樣名稱的角色。");
			}
		} catch (Exception e) {
			pc.sendPackets(".隱藏 角色名稱");
		}
	}

	private void unprisonnow2(L1PcInstance gm, L1PcInstance target) {
		try {
			int i = 32681;
			int j = 32853;
			short k = 2005;
			// L1Teleport.teleport(target, i, j, k, 5, false);
			target.start_teleport(i, j, k, target.getHeading(), 18339, false, true);
			gm.sendPackets((new StringBuilder()).append(target.getName()).append(" 被移動到隱藏模式。").toString());
		} catch (Exception e) {
			_log.log(Level.SEVERE, "", e);
		}
	}

	private void chatx(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String pcName = tokenizer.nextToken();
			L1PcInstance target = null;
			target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target.killSkillEffectTimer(L1SkillId.STATUS_CHAT_PROHIBITED);
				target.sendPackets(new S_SkillIconGFX(36, 0));
				target.sendPackets(new S_ServerMessage(288));
				gm.sendPackets("該角色的禁言已解除。");
			}
		} catch (Exception e) {
			gm.sendPackets("請輸入 '.解除禁言 角色名稱'。");
		}
	}

	private void tell(L1PcInstance gm) {
		try {
			// gm.sendPackets(new S_PacketBox(S_PacketBox.ATTACKABLE_DISTANCE, gm, gm.getWeapon()), true);
			gm.start_teleport(gm.getX(), gm.getY(), gm.getMapId(), gm.getHeading(), 18339, false, false);
		} catch (Exception e) {
		}
	}

	public void levelup2(L1PcInstance gm, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String user = tok.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(user);
			int level = Integer.parseInt(tok.nextToken());
			if (level == target.getLevel()) {
				return;
			}
			if (!IntRange.includes(level, 1, 105)) {
				gm.sendPackets("請指定1到99的範圍。");
				return;
			}
			target.set_exp(ExpTable.getExpByLevel(level));
			gm.sendPackets(target.getName() + "的等級已更改！請使用 '.檢查 [角色名稱]' 進行確認。");
		} catch (Exception e) {
			gm.sendPackets("請輸入 '.設置等級 [角色名稱] [等級]'。");
		}
	}

	private void NoDelayUser(L1PcInstance pc) {
		int SearchCount = 0;
		pc.sendPackets("----------------------------------------------------");
		for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
			if (player.getNoDelayTime() > 0 || player.getSpeedHackCount() > 10) {
				String type = player.getNoDelayTime() > 0 ? "(無延遲)" : "";
				type += player.getSpeedHackCount() > 10 ? "(加速外掛)" : "";

				pc.sendPackets("Lv." + player.getLevel() + ", " + player.getName() + " " + type);
				SearchCount++;
			}
		}
		pc.sendPackets("發現 " + SearchCount + " 名使用無延遲的用戶！");
		pc.sendPackets("----------------------------------------------------");
	}

	private void exploreReward(L1PcInstance gm, String param) {
		// TODO: 自動生成的方法存根
		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();
			int id = Integer.parseInt(st.nextToken());
			L1PcInstance user = L1World.getInstance().getPlayer(name);
			if (user != null) {
				user.getNetConnection().getAccount().tam_point += id;
				user.getNetConnection().getAccount().updateTam();
				try {
					user.sendPackets(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, user.getNetConnection()), true);
				} catch (Exception e) {
					// Handle exception
				}
				gm.sendPackets(user.getName() + " 已經獲得 " + id + " 個探。");
				Message.getInstance().get_system_message(user, "\\aA您已經獲得來自梅蒂斯的探 '\\aG" + id + "\\aA'。");
			} else
				gm.sendPackets("該用戶不存在。");
		} catch (Exception e) {
		}
	}

	private void allrecall(L1PcInstance gm) {
		try {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (!pc.isGm() && !pc.isPrivateShop()) {
					recallnow(gm, pc);
				}
			}
		} catch (Exception e) {
			gm.sendPackets(".全體召喚指令錯誤");
		}
	}

	private void summonBot(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();

			L1PcInstance target = L1World.getInstance().getPlayer(name);
			// L1Teleport.teleport(target, gm.getX(), gm.getY(), gm.getMapId(),
			// target.getHeading(), true);
			target.set_MassTel(true);
			target.start_teleport(gm.getX(), gm.getY(), gm.getMapId(), target.getHeading(), 18339, true, true);
			gm.sendPackets("您已被召喚至遊戲管理員。");
		} catch (Exception e) {
			gm.sendPackets(".召喚機器人 角色名");
		}
	}

	private void recallnow(L1PcInstance gm, L1PcInstance target) {
		try {
			// L1Teleport.teleportToTargetFront(target, gm, 2 , 0);
			L1Teleport.getInstance().teleportToTargetFront(gm, target, 2, true);
			// target.sendPackets("您已被遊戲管理員召喚。"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, "", e);
		}
	}

	private void ShopKick(L1PcInstance gm, String param) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(param);
			if (target != null) {
				gm.sendPackets((new StringBuilder()).append(target.getName()).append(" 已被驅逐。").toString());
				GameServer.disconnectChar(target);
			} else {
				gm.sendPackets("世界中不存在這樣名稱的角色。");
			}
		} catch (Exception e) {
			gm.sendPackets(".驅逐商店 角色名");
		}
	}

	private void icon(L1PcInstance pc, String param) {
		try {
			// StringTokenizer st = new StringTokenizer(param);
			// int iconId = Integer.parseInt(st.nextToken(), 10);
			/*
			 * pc.sendPackets(new S_EtcPacket(3)); // 倒數計時
			 * pc.sendPackets(new S_EtcPacket(2)); // 左下角顯示
			 * pc.sendPackets(new S_EtcPacket(4));
			 * pc.sendPackets(new S_EtcPacket(5)); // 刪除時間
			 * pc.sendPackets(new S_PacketBox(S_PacketBox.MINIGAME_LIST, iconId));
			 */
			SC_EVENT_COUNTDOWN_NOTI_PACKET.send(pc, 10, "111");
			// pc.sendPackets(MJPacketFactory.createTime(10));
			// pc.sendPackets(MJPacketFactory.create(MJPacketFactory.MSPF_IDX_OFFTIME));
		} catch (Exception exception) {
			pc.sendPackets(".圖標 請輸入 [actid]。");
		}
	}

	private void chainfo(L1PcInstance gm, String param) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			String s = stringtokenizer.nextToken();
			gm.sendPackets(new S_Chainfo(1, s));
		} catch (Exception exception21) {
			gm.sendPackets(new S_SystemMessage(".檢查角色 角色名"));
		}
	}

	private void cleanDoll(L1PcInstance gm) {
		int count = 0;
		int ccount = 0;
		for (Object obj : L1World.getInstance().getObject()) {
			if (obj instanceof L1DollInstance) {
				L1DollInstance doll = (L1DollInstance) obj;
				if (doll.getMaster() == null) {
					count++;
					doll.deleteMe();
				} else if (((L1PcInstance) doll.getMaster()).getNetConnection() == null) {
					ccount++;
					doll.deleteMe();
				}
			}
		}
		gm.sendPackets("娃娃清理數量 - 無主: " + count + "  主人已斷線: " + ccount);
	}

	private void CharacterBalance(L1PcInstance pc, String param) {
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			StringTokenizer st = new StringTokenizer(param);

			String charName = st.nextToken();
			int addDamage = Integer.parseInt(st.nextToken());
			int addDamageRate = Integer.parseInt(st.nextToken());
			int addReduction = Integer.parseInt(st.nextToken());
			int addReductionRate = Integer.parseInt(st.nextToken());

			L1PcInstance player = L1World.getInstance().getPlayer(charName);

			if (player != null) {
				player.setAddDamage(addDamage);
				player.setAddDamageRate(addDamageRate);
				player.setAddReduction(addReduction);
				player.setAddReductionRate(addReductionRate);
				player.save();
			} else {
				int i = 0;
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("update characters set AddDamage = ?, AddDamageRate = ?, AddReduction = ?, AddReductionRate = ? where char_name = ?");
				pstm.setInt(++i, addDamage);
				pstm.setInt(++i, addDamageRate);
				pstm.setInt(++i, addReduction);
				pstm.setInt(++i, addReductionRate);
				pstm.setString(++i, charName);
				pstm.executeQuery();
			}

		} catch (Exception e) {
			pc.sendPackets(".平衡 [角色名] [追加傷害] [追加傷害機率] [減少] [減少機率]");
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void AddAccount(L1PcInstance gm, String account, String passwd, int Ip, int ip2) {
		java.sql.Connection con = null;
		PreparedStatement statement = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			String login = null;
			String password = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			password = passwd;
			statement = con.prepareStatement("select * from accounts where login Like '" + account + "'");
			rs = statement.executeQuery();
			if (rs.next()) {
				login = rs.getString(1);
			}
			if (login != null) {
				gm.sendPackets("帳號已經存在。");
				return;
			} else {
				String sqlstr = "INSERT INTO accounts SET login=?,password=?,lastactive=?,access_level=?,ip=?,host=?,banned=?,charslot=?,gamepassword=?,notice=?,fatigue_point=?";
				pstm = con.prepareStatement(sqlstr);
				pstm.setString(1, account);
				pstm.setString(2, password);
				pstm.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				pstm.setInt(4, 0);
				pstm.setInt(5, Ip);
				pstm.setInt(6, ip2);
				pstm.setInt(7, 0);
				pstm.setInt(8, 6);
				pstm.setInt(9, 0);
				pstm.setInt(10, 0);
				pstm.setInt(11, 0);
				pstm.execute();
				gm.sendPackets("帳號添加已完成。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(statement);
			SQLUtil.close(con);
		}
	}

	private static boolean isDisitAlpha(String str) {
		boolean check = true;
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i)) // 若不是數字
					&& !Character.isUpperCase(str.charAt(i)) // 若不是大寫字母
					&& !Character.isLowerCase(str.charAt(i))) { // 若不是小寫字母
				check = false;
				break;
			}
		}
		return check;
	}

	private void reloadHouse(L1PcInstance gm, String cmd) {
		try {
			HouseTable.reload();
			gm.sendPackets("據點的競標時間已重新更新。");
		} catch (Exception e) {
			gm.sendPackets("請輸入 .據點更新。");
		}
	}

	// 從背包中刪除
	private void InventoryDelete(L1PcInstance pc, String param) {
		try {
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (!item.isEquipped()) {
					pc.getInventory().removeItem(item);
				}
			}
		} catch (Exception e) {
			pc.sendPackets(".刪除背包");
		}
	}

	private void targetInventoryDelete(L1PcInstance user, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String char_name = st.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(char_name);
			for (L1ItemInstance item : user.getInventory().getItems()) {
				if (!item.isEquipped()) {
					target.getInventory().removeItem(item);
				}
			}
		} catch (Exception e) {
			user.sendPackets("請輸入 .刪除用戶背包 [正在上線的角色名稱]。");
		}
	}

	private void addaccount(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String user = tok.nextToken();
			String passwd = tok.nextToken();

			if (user.length() < 4) {
				gm.sendPackets("您輸入的帳號名稱太短。");
				gm.sendPackets("請輸入至少4個字符。");
				return;
			}
			if (passwd.length() < 4) {
				gm.sendPackets("您輸入的密碼太短。");
				gm.sendPackets("請輸入至少4個字符。");
				return;
			}

			if (passwd.length() > 12) {
				gm.sendPackets("您輸入的密碼太長。");
				gm.sendPackets("請輸入最多12個字符。");
				return;
			}

			if (isDisitAlpha(passwd) == false) {
				gm.sendPackets("密碼包含無效字符。");
				return;
			}
			ip++;
			AddAccount(gm, user, passwd, ip, ip);
		} catch (Exception e) {
			gm.sendPackets("請輸入 .新增帳號 [帳號名稱] [密碼]。");
		}
	}

	private void allpresent(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			int itemid = Integer.parseInt(st.nextToken(), 10);
			int enchant = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			Collection<L1PcInstance> player = null;
			player = L1World.getInstance().getAllPlayers();
			for (L1PcInstance target : player) {
				if (target == null)
					continue;
				if (!target.isGhost() && !target.isPrivateShop()) {
					L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
					item.setCount(count);
					item.setEnchantLevel(enchant);
					if (item != null) {
						if (target.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
							target.getInventory().storeItem(item, true);
						}
					}
					target.sendPackets(new S_GMHtml("總禮物:" + target.getName() + "", "物品:" + item.getLogName() + " 數量:" + count + " 獲得"));
					target.sendPackets("總禮物: " + item.getLogName() + " " + count + "個獲得。請檢查您的背包。");
					target.sendPackets(new S_SkillSound(target.getId(), 1091)); // 鴿子動作
					target.sendPackets(new S_SkillSound(target.getId(), 4856)); // 心形動作
				}
			}
		} catch (Exception exception) {
			gm.sendPackets("請輸入 .全體贈送 [物品ID] [附魔] [數量]");
		}
	}

	private void returnEXP(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String pcName = tokenizer.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				int oldLevel = target.getLevel();
				long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
				long exp = 0;
				if (oldLevel >= 1 && oldLevel < 11) {
					exp = 0;
				} else if (oldLevel >= 11 && oldLevel < 45) {
					exp = (long) (needExp * 0.1);
				} else if (oldLevel == 45) {
					exp = (long) (needExp * 0.09);
				} else if (oldLevel == 46) {
					exp = (long) (needExp * 0.08);
				} else if (oldLevel == 47) {
					exp = (long) (needExp * 0.07);
				} else if (oldLevel == 48) {
					exp = (long) (needExp * 0.06);
				} else if (oldLevel >= 49) {
					exp = (long) (needExp * 0.05);
				}
				target.add_exp(+exp);
				target.save();
				target.saveInventory();

				gm.sendPackets("該角色已上升 +5。");
			} else {
				gm.sendPackets("該角色未上線。");
			}
		} catch (Exception e) {
			gm.sendPackets(".經驗恢復 [角色名稱]");
		}
	}

	// .帳號 -----------------------------------------------------------------
	// 檢查同一帳號中的角色
	private void account_Cha(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String name = tok.nextToken();
			account_Cha2(gm, name);
		} catch (Exception e) {
			gm.sendPackets("請輸入 .帳號 [ID]");
		}
	}

	private void account_Cha2(L1PcInstance gm, String param) {
		java.sql.Connection con0 = null; // 為了通過名稱搜索 objid
		PreparedStatement statement0 = null;
		ResultSet rs0 = null;

		java.sql.Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			String s_account = null;
			String s_name = param;
			String s_level = null;
			String s_clan = null;
			String s_bonus = null;
			String s_online = null;
			String s_hp = null;
			String s_mp = null;
			String s_type = null;// 添加
			int count = 0;
			int count0 = 0;
			con0 = L1DatabaseFactory.getInstance().getConnection();
			statement0 = con0.prepareStatement("select account_name, Clanname  from characters where char_name = '" + s_name + "'");
			rs0 = statement0.executeQuery();
			while (rs0.next()) {
				s_account = rs0.getString(1);
				s_clan = rs0.getString(2);
				gm.sendPackets("\\aD------------------------------------------");
				gm.sendPackets("\\aE角色 : " + s_name + " (" + s_account + ")  公會 : " + s_clan);
				count0++;
			}

			con = L1DatabaseFactory.getInstance().getConnection();

			statement = con.prepareStatement("select " + "char_name," + "level," + "Clanname," + "BonusStatus," + "OnlineStatus," + "MaxHp," + "MaxMp, " + "Type "
					+ " from characters where account_name = '" + s_account + "'");
			gm.sendPackets("\\aD------------------------------------------");
			rs = statement.executeQuery();
			while (rs.next()) {
				s_name = rs.getString(1);
				s_level = rs.getString(2);
				s_clan = rs.getString(3);
				s_bonus = rs.getString(4);
				s_online = rs.getString(5);
				s_hp = rs.getString(6);
				s_mp = rs.getString(7);
				s_type = rs.getString(8);
				gm.sendPackets("上線: [" + s_online + "] 等級: " + s_level + " [" + s_name + "] 職業: " + s_type);
				count++;
			}
			gm.sendPackets("\aF0 王族 1 騎士 2 妖精 3 法師 4 黑暗妖精 5 龍騎士 6 幻術師 7 戰士 8 劍士 9 黃金槍騎 10 全部");
			gm.sendPackets("\\aD------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs0, statement0, con0);
			SQLUtil.close(rs, statement, con);
		}
	}

	// .帳號 -----------------------------------------------------------------

	private void Pvp(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String type = st.nextToken();

			if (type.equals("開")) {
				// 當 type 等於 "開" 時執行的代碼
				Config.ServerAdSetting.SERVERPVPSETTING = true;
				Config.setParameterValue("AltNonPvP", "true");
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "現在開始 PvP 正常開放。"));
			} else if (type.equals("關")) {
				Config.ServerAdSetting.SERVERPVPSETTING = false;
				Config.setParameterValue("AltNonPvP", "false");
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "現在起 PvP 將在一段時間內無法使用。"));
			}

		} catch (Exception exception) {
			gm.sendPackets(".pvp [開/關]");
		}
	}

	private void search_banned(L1PcInstance paramL1PcInstance) {
		Selector.exec(
				"select accounts.login, characters.char_name from accounts,characters where accounts.banned=62 || accounts.banned=87 || accounts.banned=95 and accounts.login=characters.account_name ORDER BY accounts.login ASC",
				new FullSelectorHandler() {
					@Override
					public void result(ResultSet rs) throws Exception {
						int i = 0;
						while (rs.next()) {
							++i;
							paramL1PcInstance.sendPackets(String.format("帳號: [%s], 角色名: [%s]", rs.getString(1), rs.getString(2)));
						}
						paramL1PcInstance.sendPackets(String.format("總共有 [%d] 個被查封的帳號/角色被找到。", i));
					}
				});
	}

	private void accountdel(L1PcInstance gm, String param) {

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String chaname = tokenizer.nextToken();
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
			pstm.setString(1, chaname);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				gm.sendPackets(String.format("資料庫中不存在名稱為 %s 的角色。", chaname));
				return;
			}

			String account = rs.getString("account_name");
			SQLUtil.close(rs, pstm);

			pstm = con.prepareStatement("select * from accounts WHERE login=?");
			pstm.setString(1, account);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				gm.sendPackets(String.format("帳號名 %s 不存在。", account));
				return;
			}

			IpTable.getInstance();
			IpTable.reload();

			String host = rs.getString("ip");
			SQLUtil.close(rs, pstm);

			pstm = con.prepareStatement("UPDATE accounts SET banned = 0 WHERE login= ?");
			pstm.setString(1, account);
			pstm.executeUpdate();
			SQLUtil.close(rs, pstm);

			pstm = con.prepareStatement("delete from ban_ip where ip=?");
			pstm.setString(1, host);
			pstm.executeUpdate();

			MJHddIdChecker.delete(account);
			MJNSDenialAddress.getInstance().delete_denials_address(host);

			gm.sendPackets(String.format("%s(%s) : %s 解禁完成。", chaname, account, host));
		} catch (Exception e) {
			gm.sendPackets(".請輸入要解封的角色名稱。");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	// private void GiveAbyssPoint(L1PcInstance pc, String poby) {
	// try {
	// StringTokenizer st = new StringTokenizer(poby);
	// String pobyname = st.nextToken();
	// int point = Integer.parseInt(st.nextToken());
	// L1PcInstance target = L1World.getInstance().getPlayer(pobyname);
	// if (target != null) {
	// target.addAbysspoint(point);
	// pc.sendPackets(target.getName() + " 以 [深淵點數 " + point + " 點] 給予。"));
	// }
	// } catch (Exception e) {
	// pc.sendPackets(".深淵點數給予 [給予對象的角色名] [要給予的點數]"));
	// }
	//
	// }

	// private void GiveClanPoint(L1PcInstance pc, String poby) { // 給予血盟經驗值
	// try {
	//     StringTokenizer st = new StringTokenizer(poby);
	//     String pobyname = st.nextToken();
	//     int point = Integer.parseInt(st.nextToken());
	//     L1PcInstance target = L1World.getInstance().getPlayer(pobyname);
	//     if (target != null) {
	//         if (target.getClanid() != 0) {
	//             L1Clan TargetClan = L1World.getInstance().getClan(target.getClanname());
	//             TargetClan.addClanExp(point);
	//             ClanTable.getInstance().updateClan(TargetClan);
	//             pc.sendPackets(target.getClanname() + " 血盟獲得了 [經驗值 " + point + "]。");
	//             for (L1PcInstance tc : TargetClan.getOnlineClanMember()) {
	//                 tc.sendPackets("從遊戲管理員那裡獲得了血盟經驗值 [" + point + "]。");
	//             }
	//         } else {
	//             pc.sendPackets(target.getName() + " 不屬於任何血盟。");
	//         }
	//     } else {
	//         pc.sendPackets(new S_ServerMessage(73, pobyname));
	//     }
	// } catch (Exception e) {
	//     pc.sendPackets(".血盟經驗值 [要給予的血盟君主名字] [要給予的點數]");
	// }
	// }

	private void changePassword(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String user = tok.nextToken();
			String passwd = tok.nextToken();

			if (passwd.length() < 4) {
				gm.sendPackets("您輸入的密碼長度太短。");
				gm.sendPackets("請輸入至少4個字元。");
				return;
			}

			if (passwd.length() > 12) {
				gm.sendPackets("您輸入的密碼長度太長。");
				gm.sendPackets("請輸入不超過12個字元。");
				return;
			}

			if (isDisitAlpha(passwd) == false) {
				gm.sendPackets("密碼包含不允許的字符。");
				return;
			}
			L1PcInstance target = L1World.getInstance().getPlayer(user);
			if (target != null) {
				to_Change_Passwd(gm, target, passwd);
			} else {
				if (!to_Change_Passwd(gm, user, passwd))
					gm.sendPackets("沒有該名稱的角色。");
			}
		} catch (Exception e) {
			gm.sendPackets(".請輸入 .變更密碼 [角色名] [密碼]。");
		}
	}

	private void to_Change_Passwd(L1PcInstance gm, L1PcInstance pc, String passwd) {
		PreparedStatement statement = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			String login = null;
			String password = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			password = passwd;
			statement = con.prepareStatement("select account_name from characters where char_name Like '" + pc.getName() + "'");
			rs = statement.executeQuery();

			while (rs.next()) {
				login = rs.getString(1);
				pstm = con.prepareStatement("UPDATE accounts SET password=? WHERE login Like '" + login + "'");
				pstm.setString(1, password);
				pstm.execute();
				gm.sendPackets("密碼變更 帳號: [" + login + "] 新密碼: [" + passwd + "]");
				gm.sendPackets(pc.getName() + " 密碼變更完成。");
			}
		} catch (Exception e) {
			System.out.println("to_Change_Passwd() Error : " + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(statement);
			SQLUtil.close(con);
		}
	}

	private boolean to_Change_Passwd(L1PcInstance pc, String name, String passwd) {
		PreparedStatement statement = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			String login = null;
			String password = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			password = passwd;
			statement = con.prepareStatement("select account_name from characters where char_name Like '" + name + "'");
			rs = statement.executeQuery();

			while (rs.next()) {
				login = rs.getString(1);
				pstm = con.prepareStatement("UPDATE accounts SET password=? WHERE login Like '" + login + "'");
				pstm.setString(1, password);
				pstm.execute();
				pc.sendPackets("密碼變更 帳號: [" + login + "] 新密碼: [" + passwd + "]");
				pc.sendPackets("該角色密碼變更完成。(未登入)");
			}
			return true;
		} catch (Exception e) {
			System.out.println("to_Change_Passwd() Error : " + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(statement);
			SQLUtil.close(con);
		}
		return false;
	}

	private void changeQuiz(L1PcInstance gm, L1PcInstance pc, String newQuiz) {
		PreparedStatement statement = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			String login = null;
			String quiz = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			quiz = newquiz;
			statement = con.prepareStatement("select account_name from characters where char_name Like '" + pc.getName() + "'");
			rs = statement.executeQuery();

			while (rs.next()) {
				login = rs.getString(1);
				pstm = con.prepareStatement("UPDATE accounts SET quiz=? WHERE login Like '" + login + "'");
				pstm.setString(1, quiz);
				pstm.execute();
				gm.sendPackets("安全問題變更 帳號: [" + login + "] 問題: [" + quiz + "]");
				gm.sendPackets(pc.getName() + " 的安全問題變更完成。");
			}
		} catch (Exception e) {
			System.out.println("to_Change_Passwd() Error : " + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(statement);
			SQLUtil.close(con);
		}
	}

	private boolean changeQuiz(L1PcInstance pc, String name, String newQuiz) {
		PreparedStatement statement = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			String login = null;
			String quiz = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			quiz = newquiz;
			statement = con.prepareStatement("select account_name from characters where char_name Like '" + name + "'");
			rs = statement.executeQuery();

			while (rs.next()) {
				login = rs.getString(1);
				pstm = con.prepareStatement("UPDATE accounts SET quiz=? WHERE login Like '" + login + "'");
				pstm.setString(1, quiz);
				pstm.execute();
				pc.sendPackets("安全問題變更 帳號: [" + login + "] 問題: [" + quiz + "]");
				pc.sendPackets("該角色安全問題變更完成。(未登入)");
			}
			return true;
		} catch (Exception e) {
			System.out.println("to_Change_Passwd() Error : " + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(statement);
			SQLUtil.close(con);
		}
		return false;
	}

	private static int delItemlist[] = { 307, 308, 309, 310, 311, 312, 313, 314, 21095, 30146, 30147, 30150 };

	public synchronized static void deleteArnoldEvent() {
		try {
			if (delItemlist.length <= 0)
				return;

			for (L1PcInstance tempPc : L1World.getInstance().getAllPlayers()) {
				if (tempPc == null)
					continue;
				for (int i = 0; i < delItemlist.length; i++) {
					L1ItemInstance[] item = tempPc.getInventory().findItemsId(delItemlist[i]);
					if (item != null && item.length > 0) {
						for (int o = 0; o < item.length; o++) {
							tempPc.getInventory().removeItem(item[o]);
						}
					}
					try {
						PrivateWarehouse pw = WarehouseManager.getInstance().getPrivateWarehouse(tempPc.getAccountName());
						L1ItemInstance[] item2 = pw.findItemsId(delItemlist[i]);
						if (item2 != null && item2.length > 0) {
							for (int o = 0; o < item2.length; o++) {
								pw.removeItem(item2[o]);
							}
						}
					} catch (Exception e) {
					}
					try {
						if (tempPc.getClanid() > 0) {
							ClanWarehouse cw = WarehouseManager.getInstance().getClanWarehouse(tempPc.getClanname());
							L1ItemInstance[] item3 = cw.findItemsId(delItemlist[i]);
							if (item3 != null && item3.length > 0) {
								for (int o = 0; o < item3.length; o++) {
									cw.removeItem(item3[o]);
								}
							}
						}
					} catch (Exception e) {
					}
					try {
						if (tempPc.getPetList().size() > 0) {
							for (L1NpcInstance npc : tempPc.getPetList().values()) {
								L1ItemInstance[] pitem = npc.getInventory().findItemsId(delItemlist[i]);
								if (pitem != null && pitem.length > 0) {
									for (int o = 0; o < pitem.length; o++) {
										npc.getInventory().removeItem(pitem[o]);
									}
								}
							}
						}
					} catch (Exception e) {
					}
				}
			}
			try {
				for (L1Object obj : L1World.getInstance().getAllItem()) {
					if (!(obj instanceof L1ItemInstance))
						continue;
					L1ItemInstance temp_item = (L1ItemInstance) obj;
					if (temp_item.getItemOwner() == null) {
						if (temp_item.getX() == 0 && temp_item.getY() == 0)
							continue;
					}
					for (int ii = 0; ii < delItemlist.length; ii++) {
						if (delItemlist[ii] == temp_item.getItemId()) {
							L1Inventory groundInventory = L1World.getInstance().getInventory(temp_item.getX(), temp_item.getY(), temp_item.getMapId());
							groundInventory.removeItem(temp_item);
							break;
						}
					}

				}
			} catch (Exception e) {
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < delItemlist.length; i++) {
				sb.append(+delItemlist[i]);
				if (i < delItemlist.length - 1) {
					sb.append(",");
				}
			}
			Delete(sb.toString());

			/*
			 * for(int i = 0; i < delItemlist.length; i++){ Delete(delItemlist[i]); wareDelete(delItemlist[i]); ClanwareDelete(delItemlist[i]); }
			 */
		} catch (Exception e) {
		}
	}

	private static void Delete(String id_name) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete FROM character_warehouse WHERE item_id in (" + id_name + ")");
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete FROM clan_warehouse WHERE item_id in (" + id_name + ")");
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void mjClear(L1PcInstance gm, String param) {
		int id = 0;
		int space = 0;
		int yspace = 0;
		try {
			String[] arr = param.split(" ");
			id = Integer.parseInt(arr[0]);
			space = Integer.parseInt(arr[1]);
			yspace = Integer.parseInt(arr[2]);
		} catch (Exception e) {
			return;
		}

		GeneralThreadPool.getInstance().execute(new EffectThread(gm, id, space, yspace));

	}

	public class EffectThread implements Runnable {
		private L1PcInstance _owner;
		private int _effect;
		private int _x_space;
		private int _z_space;

		public EffectThread(L1PcInstance owner, int effect, int x, int z) {
			_owner = owner;
			_effect = effect;
			_x_space = x;
			_z_space = z;
		}

		@Override
		public void run() {
			if (_owner == null)
				return null;

			ArrayList<L1Object> objs = L1World.getInstance().getVisibleObjects(_owner);
			int tx = 0;
			int ty = 0;
			int cx = 0;
			int cy = 0;
			int[][] cpos = new int[4][2];

			try {
				S_EffectLocation[] pcks = new S_EffectLocation[4];
				for (int width = 15; width >= 0; width -= _z_space) {
					int left = _owner.getX() - width;
					int top = _owner.getY() - width;
					int right = _owner.getX() + width;
					int bottom = _owner.getY() + width;
					for (int i = 0; i < width * 2; i += _x_space) {
						cpos[0][0] = left + i;
						cpos[0][1] = top;

						cpos[1][0] = right;
						cpos[1][1] = top + i;

						cpos[2][0] = right - i;
						cpos[2][1] = bottom;

						cpos[3][0] = left;
						cpos[3][1] = bottom - i;
						for (int j = 0; j < 4; j++) {
							pcks[j] = new S_EffectLocation(cpos[j][0], cpos[j][1], _effect);
							_owner.sendPackets(pcks[j], false);
							Broadcaster.broadcastPacket(_owner, pcks[j], true);

							for (L1Object obj : objs) {
								if (!(obj instanceof L1MonsterInstance))
									continue;

								cx = Math.abs(obj.getX() - cpos[j][0]);
								cy = Math.abs(obj.getY() - cpos[j][1]);
								if (cx < 3 && cy < 3)
									((L1MonsterInstance) obj).receiveDamage(_owner, ((L1MonsterInstance) obj).getCurrentHp());
							}
						}
					}
					Thread.sleep(500);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@SuppressWarnings("resource")
	private void testCommands(L1PcInstance gm, String param) {
		gm.getLocation().set(33436, 32815);
		gm.setLastMoveActionMillis(System.currentTimeMillis());

		gm.sendPackets(new S_MoveCharPacket(gm));
		gm.sendPackets(new S_OwnCharPack(gm));
		gm.sendPackets(new S_SkillSound(gm.getId(), 18503), true);
		gm.updateObject();
		gm.broadcastRemoveAllKnownObjects();
		gm.removeAllKnownObjects();
		gm.setAttackSpeed();
		gm.sendVisualEffectAtTeleport();
		gm.updateObject();
		gm.sendPackets(new S_CharVisualUpdate(gm));

		// SC_ATTENDANCE_REWARD_ITEM_NOTI noti =
		// SC_ATTENDANCE_REWARD_ITEM_NOTI.newInstance();
		// for (int i = 10; i < 15; ++i) {
		// ATTENDANCE_REWARD_ITEM item = ATTENDANCE_REWARD_ITEM.newInstance();
		// item.set_attendance_id(i);
		// item.set_group_id(0);
		// item.set_item_count(1);
		// item.set_item_name_id(4166);
		// noti.add_reward_item_info(item);
		// }
		// gm.sendPackets(noti, MJEProtoMessages.SC_ATTENDANCE_REWARD_ITEM_NOTI, true);

		// SC_SPELL_BUFF_NOTI.sendFatigueOn(gm, 2, 10);
		// gm.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, gm, 1, 15));

		/*
		 * int x = gm.getX(); int y = gm.getY(); MJRectangle rt = (MJRectangle)MJRectangle.newInstance(gm.getX() - 1, gm.getY() - 1, gm.getX() + 1, gm.getY() + 1, gm.getMapId()); Box[] boxes = rt.to_line_box(); for(Box box : boxes)
		 * gm.sendPackets(SC_BOX_ATTR_CHANGE_NOTI_PACKET.create_box(gm.getMapId( ), box, 0));
		 */

		// gm.sendPackets(new S_EffectLocation(gm.getX(), gm.getY(), 13923));
		// gm.sendPackets(S_Einhasad.newInstance(gm));
		// gm.sendPackets(new S_CharVisualUpdate(gm));
		// gm.sendPackets(new S_Paralysis(new byte[]{ 0x0d, (byte)0xff,
		// (byte)0xff, 0x07, 0x00 }));

		/*
		 * \\ StringTokenizer st = new StringTokenizer(param); try { int first = 0; //int second = 0; try { first = Integer.parseInt(st.nextToken()); //second = Integer.parseInt(st.nextToken()); gm.sendPackets(new S_SkillIconGFX(first, -1)); } catch
		 * (NumberFormatException e) { } } catch (NumberFormatException e) { }
		 */
		// gm.sendPackets(new S_SkillIconGFX(25, 10));

		/*
		 * S_PacketBox box = new S_PacketBox(S_PacketBox.BUFFICON, 2949, 0, false, true); gm.sendPackets(box);
		 */

		/*
		 * byte[] buff = new byte[10]; for (int i = 7; i < 10; ++i) buff[i] = 60; gm.sendPackets(new S_UnityIcon(buff));
		 */

		/*
		 * L1Object obj = L1World.getInstance().findNpc(param); if(obj != null) gm.sendPackets(new S_UseAttackSkill(gm, obj.getId(), 17229, obj.getX(), obj.getY(), 18));
		 */
		// gm.sendPackets(info, MJEProtoMessages.SC_PARTY_SYNC_PERIODIC_INFO,
		// true);
		/*
		 * SC_PARTY_MEMBER_LIST list = SC_PARTY_MEMBER_LIST.newInstance(); PartyMember member = PartyMember.newInstance(); member.set_accountid(0); member.set_alive_time_stamp(0); member.set_game_class(gm.getType()); member.set_gender(1);
		 * member.set_hp_ratio(100); int pt = (gm.getY() << 16) & 0xffff0000; pt |= (gm.getX() & 0x0000ffff); member.set_location(pt); member.set_mp_ratio(100); member.set_name(gm.getName()); member.set_object_id(gm.getId()); member.set_party_mark(1);
		 * member.set_server_no(1); member.set_world(4); // mapid list.add_member(member); list.set_leader_name(gm.getName()); gm.sendPackets(list, MJEProtoMessages.SC_PARTY_MEMBER_LIST, true);
		 */

		/*
		 * SC_PARTY_OPERATION_RESULT_NOTI noti = SC_PARTY_OPERATION_RESULT_NOTI.newInstance(); noti.set_type(ePARTY_OPERATION_TYPE. ePARTY_OPERATION_TYPE_INVITE_ACCEPT); noti.set_actor_name("hello"); ProtoOutputStream stream =
		 * ProtoOutputStream.newInstance(noti.getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, 0x033D); gm.sendPackets(stream, true);
		 */

		// gm.sendPackets(new S_Party("party", gm.getId(), gm.getName(),
		// gm.getName()));
		/*
		 * GeneralThreadPool.getInstance().execute(new Runnable(){
		 *
		 * @Override public void run(){ for(int i=400; i<2000; ++i){ gm.sendPackets(S_ShowCmd.get(String.valueOf(i))); gm.sendPackets(S_ShowCmd.getProto8(i)); gm.sendPackets(S_ShowCmd.getProtoA(i));
		 *
		 * try{ SC_ACTIVE_SPELL_EX_INFO exInfo = SC_ACTIVE_SPELL_EX_INFO.newInstance(); info inf = info.newInstance(); inf.set_spellid(L1SkillId.DESPERADO - 1); inf.set_spelltype(1); inf.set_graphic(17235); inf.add_value(1000); exInfo.add_infos(inf);
		 * ProtoOutputStream stream = ProtoOutputStream.newInstance(exInfo.getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, i); exInfo.writeTo(stream); gm.sendPackets(stream, true); Thread.sleep(500); }catch(Exception e){} } } });
		 */
		/*
		 * SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance(); noti.set_noti_type(eNotiType.END); noti.set_spell_id(L1SkillId.CUBE_AVATAR); noti.set_duration(0); noti.set_off_icon_id(3101); noti.set_is_good(true); gm.sendPackets(noti,
		 * MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		 */
		// gm.send_effect(17233);
		// gm.send_effect(17235);

		/*
		 * SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance(); noti.set_noti_type(eNotiType.RESTAT); noti.set_spell_id(L1SkillId.FOCUS_SPRITS); noti.set_duration(15); noti.set_duration_show_type(eDurationShowType. TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
		 * noti.set_on_icon_id(4832); noti.set_off_icon_id(4832); noti.set_icon_priority(3); noti.set_tooltip_str_id(5272); noti.set_new_str_id(0); noti.set_end_str_id(5273); noti.set_is_good(true); gm.sendPackets(noti,
		 * MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true); gm.sendPackets(S_InventoryIcon.icoNew(L1SkillId.CUBE_OGRE, 15, true)); gm.sendPackets(S_InventoryIcon.icoNew(L1SkillId.CUBE_RICH, 15, true));
		 * gm.sendPackets(S_InventoryIcon.icoNew(L1SkillId.CUBE_AVATAR, 15, true));
		 */
		// gm.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		// 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0)); // 전사꺼

		// CS_PARTY_CONTROL_REQ pck = CS_PARTY_CONTROL_REQ.newInstance();
		// pck.set_remain_time(10);
		// try {
		// pck.set_event_desc("\\fW$12125".getBytes("UTF-8"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// gm.sendPackets(pck, MJEProtoMessages.CS_PARTY_CONTROL_REQ, true);

		// gm.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16,
		// 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, gm.getElfAttr()));
		// gm.sendPackets(new S_ACTION_UI(146, 10));
		/*
		 * GeneralThreadPool.getInstance().execute(new Runnable(){
		 *
		 * @Override public void run(){ int j=0; for(int i=10; i<=14; ++i, ++j){ gm.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, gm.getElfAttr())); gm.sendPackets(new S_ACTION_UI(145, i));
		 * gm.sendPackets(String.valueOf(i)); try { Thread.sleep(100); } catch (InterruptedException e) { // TODO Auto-generated catch block e.printStackTrace(); } } } });
		 */

		/*
		 * SC_EVENT_COUNTDOWN_NOTI_PACKET pck = SC_EVENT_COUNTDOWN_NOTI_PACKET.newInstance(); pck.set_remain_time(10); try { pck.set_event_desc("\\fW$12125".getBytes("UTF-8")); } catch (UnsupportedEncodingException e) { e.printStackTrace(); } gm.sendPackets(pck,
		 * MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET, true);
		 */
		// gm.sendPackets(new S_OwnCharStatus(gm));
		/*
		 * SC_SPECIAL_RESISTANCE_NOTI noti = SC_SPECIAL_RESISTANCE_NOTI.newInstance(); Value v = Value.newInstance(); v.set_kind(eKind.FEAR); v.set_value(4); noti.add_pierce(v);
		 *
		 * v = Value.newInstance(); v.set_kind(eKind.SPIRIT); v.set_value(30); noti.add_resistance(v); ProtoOutputStream stream = ProtoOutputStream.newInstance(noti.getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, 0x03F7); try { noti.writeTo(stream);
		 * gm.sendPackets(stream, true); } catch (IOException e) { e.printStackTrace(); }
		 */

		// gm.sendPackets(new S_SkillIconShield(4, 0));

		/*
		 * MJCommandArgs args = new MJCommandArgs().setOwner(gm).setParam(param);
		 *
		 *
		 * int mark = 0; L1PcInstance pc = null; try { mark = args.nextInt(); pc = L1World.getInstance().getPlayer(args.nextString()); } catch (MJCommandArgsIndexException e) { // TODO Auto-generated catch block e.printStackTrace(); } if(pc == null) return;
		 *
		 * pc.set_mark_status(mark); pc.do_simple_teleport(pc.getX(), pc.getY(), pc.getMapId());
		 */
		/*
		 * SC_BOX_ATTR_CHANGE_NOTI_PACKET box_pck = SC_BOX_ATTR_CHANGE_NOTI_PACKET.newInstance(); Integer r = 2; Integer a = Integer.parseInt(param); int left = gm.getX() - r; int top = gm.getY() - r; int right = gm.getX() + r; int bottom = gm.getY() + r;
		 *
		 * box_pck.set_worldNumber(gm.getMapId()); Box box = Box.newInstance(); box_pck.set_box(box); box_pck.set_attribute(a);
		 *
		 * // - box.set_sx(left); box.set_sy(top); box.set_ex(right); box.set_ey(top + 1); gm.sendPackets(box_pck.writeTo(MJEProtoMessages. SC_BOX_ATTR_CHANGE_NOTI_PACKET));
		 *
		 * // | box.set_sx(right - 1); box.set_sy(top); box.set_ex(right); box.set_ey(bottom); gm.sendPackets(box_pck.writeTo(MJEProtoMessages. SC_BOX_ATTR_CHANGE_NOTI_PACKET));
		 *
		 * // _ box.set_sx(left); box.set_sy(bottom - 1); box.set_ex(right); box.set_ey(bottom); gm.sendPackets(box_pck.writeTo(MJEProtoMessages. SC_BOX_ATTR_CHANGE_NOTI_PACKET));
		 *
		 * // | box.set_sx(left); box.set_sy(top); box.set_ex(left + 1); box.set_ey(bottom); gm.sendPackets(box_pck.writeTo(MJEProtoMessages. SC_BOX_ATTR_CHANGE_NOTI_PACKET));
		 */

		// gm.sendPackets(new S_War(4, gm.getClanname(), "컨트롤"));
		// gm.sendPackets("$" + param);
		// L1World.getInstance().broadcastPacketToAll(new S_IconMessage(true));

		// gm.sendPackets(gm.hasSkillEffect(L1SkillId.SHAPE_CHANGE) + "");
		// gm.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 490),
		// true);
		/*
		 * L1Object obj = L1World.getInstance().findObject(269064985);
		 * L1Character c = (L1Character)obj;
		 * MJServerPacketBuilder builder = null;
		 * try {
		 *   // 隊員死亡，重新復活
		 *   //builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x6C).addD(269064817).addH(0x00);
		 *   // 更換隊長
		 *   //builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x6A).addD(269064817).addH(0x00);
		 *   // 加入左側隊伍窗
		 *   //builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x69).addD(269064817).addS(c.getName()).addC(0).addH(0x00).addD(c.getMapId()).addH(c.getX()).addH(c.getY());
		 *   //builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x6E).addC(0x01).addD(269064817).addD(c.getMapId()).addH(c.getX()).addH(c.getY()).addC(0x00);
		 *   //builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(159).addD(269064817).addD(c.getMapId()).addH(c.getX()).addH(c.getY()).addC(0x00);
		 *   builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(111).addS(c.getName()).addH(c.getMapId()).addH(c.getX()).addH(c.getY());
		 * } catch (IOException e) {
		 *   // TODO 自動生成的 catch 區塊
		 *   e.printStackTrace();
		 * }
		 * byte[] b = builder.toArray();
		 * S_BuilderPacket s = new S_BuilderPacket(b.length, b);
		 * gm.sendPackets(s, true);
		 * builder.close();
		 * builder.dispose();
		 */
		// 269064817
		// gm.sendPackets(new S_Message_YN(3325, 3325, param));
		// gm.sendPackets(S_IconMessage.getGmMessage(String.format("[%s]님의
		// "Loudspeaker (Normal) message", gm.getName())));
		// gm.sendPackets(S_NotificationMessage.get(S_NotificationMessage.DISPLAY_POSITION_TOP,
		// param, MJSimpleRgb.red(), 15));
		// gm.sendPackets(new S_IconMessage(false));

		// gm.start_teleport(gm.getX(), gm.getY(), gm.getMapId(),
		// gm.getHeading(), 169, false, true);
		// gm.sendPackets(new S_OwnCharStatus2(gm));
		// gm.sendPackets(S_WorldPutObject.put(gm));
		/*
		 * if(EventThread.getInstance()._leftBDoor.isClose()){ EventThread.getInstance()._leftBDoor.down(); EventThread.getInstance()._rightBDoor.down(); }else{ EventThread.getInstance()._leftBDoor.up(); EventThread.getInstance()._rightBDoor.up(); }
		 *
		 * if(EventThread.getInstance()._centerBDoor.isClose()){ EventThread.getInstance()._centerBDoor.down(); }else{ EventThread.getInstance()._centerBDoor.up(); }
		 */

		// gm.sendPackets(new S_DisplayEffect(Integer.parseInt(param)));
		/*
		 * Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar(); String[] command = new String[]{ "cmd", "/C", "C:\\Program Files\\Java\\jdk1.8.0_101\\bin\\jstack", MJProcessPlayer.getPid(), ">", String.format("dump\\[%02d-%02d-%02d]dump.txt",
		 * cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)) }; MJProcessPlayer mpp = new MJProcessPlayer(); try { mpp.byRuntime(command); } catch (Exception e) { e.printStackTrace(); }
		 */

		// gm.sendPackets(new S_IconMessage(false));
		// gm.sendPackets(new S_IconMessage(false));
		// gm.sendPackets(new S_IconMessage(gm, param));
		/*
		 * try{ int i = Integer.parseInt(param); gm.sendPackets(S_IconMessage.getMessage(param, MJSimpleRgb.red(), i, 10)); }catch(Exception e){ e.printStackTrace(); }
		 */
		// gm.sendPackets(S_ChatMessageNoti.getNotice(param, "메티스"));
		// gm.sendPackets(S_ShowCmd.getQuestDesc(3511, 1465));

		/*
		 * L1NpcInstance obj = L1World.getInstance().findNpc(param); if(obj == null) System.out.println("null"); else{ System.out.println(param + " " + obj.getX() + " " + obj.getY()); gm.sendPackets(new S_SkillSound(obj.getId(), 8150)); }
		 */
		// gm.sendPackets(new S_IconMessage(false));
		// gm.sendPackets(S_ShowCmd.getPkMessageAtBattleServer("\\aGtest\\aL",
		// "test2"));
		// gm.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int)
		// System.currentTimeMillis() / 1000, 8265, 4181));
		// gm.sendPackets(new S_IconMessage(false));
		/*
		 * L1PcInstance pc = L1World.getInstance().getPlayer(param); if(pc != null) pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PERADO, false));
		 */
		// gm.sendPackets(new S_SkillSound(gm.getId(), 1043));
		// gm.setPoisonEffect(0);
	}

	private static int _effectNum = 0;
	private static Object _effectLock = new Object();

	private void showNextEffect(L1PcInstance gm) {
		int num = 1;

		synchronized (_effectLock) {
			num = _effectNum;
			_effectNum++;
		}
		gm.sendPackets(new S_SkillSound(gm.getId(), num));
		gm.sendPackets(String.format("[%d]", num));
	}

	private void showNameId(L1PcInstance gm, String param) {
		try {
			S_ServerMessage sm = new S_ServerMessage(403, "$" + param);
			gm.sendPackets(sm, true);
		} catch (Exception e) {

		}
	}

	private void showEffectInit(L1PcInstance gm, String param) {
		int effNum = 0;
		try {
			StringTokenizer st = new StringTokenizer(param);
			effNum = Integer.parseInt(st.nextToken(), 10);
		} catch (Exception e) {
		}
		synchronized (_effectLock) {
			_effectNum = effNum;
			gm.sendPackets(effNum + " 已被初始化。");
		}
	}

	private void POLYMORPH_EVENT(L1PcInstance gm, String param) {
		try {
			if (param.equalsIgnoreCase("開")) {
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					SC_POLYMORPH_EVENT_NOTI noti = SC_POLYMORPH_EVENT_NOTI.newInstance();
					noti.set_eventEnable(true);
					pc.sendPackets(noti, MJEProtoMessages.SC_POLYMORPH_EVENT_NOTI, true);
					pc.sendPackets("稍後將進行變身活動。變身等級將被調整。");
				}
				Config.ServerAdSetting.PolyEvent2 = true;
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "稍後將進行變身活動。變身等級將被調整。"));
			} else if (param.equalsIgnoreCase("關")) {
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					SC_POLYMORPH_EVENT_NOTI noti = SC_POLYMORPH_EVENT_NOTI.newInstance();
					noti.set_eventEnable(false);
					pc.sendPackets(noti, MJEProtoMessages.SC_POLYMORPH_EVENT_NOTI, false);
					pc.sendPackets("稍後將結束變身活動。變身等級將被重新調整。");
				}
				Config.ServerAdSetting.PolyEvent2 = false;
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "稍後將結束變身活動。變身等級將被重新調整。"));
			} else
				throw new Exception("");
		} catch (Exception e) {
			gm.sendPackets(".變身活動 [開/關]");
		}
	}

	public static String _sleepingMessage;
	public static String _sleepingTitle;

	private void setSleepingMode(L1PcInstance gm, String param) {
		int num = 0;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			StringTokenizer st = new StringTokenizer(param);
			num = Integer.parseInt(st.nextToken(), 10);
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from tb_sleeping_messages where id=?");
			pstm.setInt(1, num);
			rs = pstm.executeQuery();
			if (rs.next()) {
				_sleepingTitle = rs.getString("title");
				_sleepingMessage = rs.getString("content");
				gm.sendPackets(String.format("暫時離開的消息已設置為 [%s]。", _sleepingMessage));
			} else
				gm.sendPackets(String.format("編號 [%d] 的暫時離開消息不存在。", num));
		} catch (Exception e) {
			gm.sendPackets(".暫時離開模式 [暫時離開消息編號] 請輸入。");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void unSetSleepingMode(L1PcInstance gm, String param) {
		_sleepingTitle = null;
		_sleepingMessage = null;
		gm.sendPackets("暫時離開模式已被解除。");
	}

	private void deleteUseType(L1PcInstance gm) {
		try {
			for (L1ItemInstance item : m_use_type_items) {
				gm.sendPackets(new S_DeleteInventoryItem(item));
			}
			m_use_type_items.clear();
		} catch (Exception e) {
		}
	}

	private static ArrayList<L1ItemInstance> m_use_type_items = new ArrayList<L1ItemInstance>();

	private void showUseType(L1PcInstance gm, String param) {
		try {
			if (param.equalsIgnoreCase("刪除")) {
				deleteUseType(gm);
				return;
			}

			StringTokenizer st = new StringTokenizer(param);
			int start = Integer.parseInt(st.nextToken());
			int end = Integer.parseInt(st.nextToken()) + start;
			SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
			L1Item temp = ItemTable.getInstance().getTemplate(40005);
			for (int i = start; i <= end; i++) {
				L1ItemInstance item = new L1ItemInstance();
				item.setId(i);
				item.setItem(temp);
				item.setBless(temp.getBless());
				item.setIdentified(false);
				noti.add_item_info(ItemInfo.newInstance(gm, item, i, String.valueOf(i).getBytes()));
				m_use_type_items.add(item);
			}
			gm.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
		} catch (Exception e) {
			gm.sendPackets(".使用類型 [起始編號] [結束編號]");
		}
	}

	private void grangKinDB_Reset() {
		PreparedStatement pstm = null;
		Connection con = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET GrangKinAngerTime=?");
			pstm.setInt(1, 0);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	private void showWarList(L1PcInstance gm) {
		try {
			int i = 0;
			L1World.getInstance().createWarStream().forEach((MJWar war) -> {
				gm.sendPackets(war.toString());
			});
		} catch (Exception e) {
		}
	}

	private static void setCharBlock(L1PcInstance gm, String param) {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();
			if (name == null || name.equalsIgnoreCase(""))
				throw new Exception("");

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from characters where char_name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();

			if (!rs.next()) {
				gm.sendPackets(String.format("\f3[%s]無法找到。", name));
				return;
			}

			int objid = rs.getInt("objid");
			L1PcInstance pc = L1World.getInstance().getPlayer(name);
			SQLUtil.close(rs, pstm);

			pstm = con.prepareStatement("insert ignore into tb_character_block set objid=?, name=?");
			pstm.setInt(1, objid);
			pstm.setString(2, name);
			pstm.executeUpdate();

			gm.sendPackets(String.format("\f3角色 [%s] 已被壓制。", name));

			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3您帳戶中的當前角色已被壓制。"));
			pc.sendPackets("\f3您帳戶中的當前角色已被壓制。");
			// GeneralThreadPool.getInstance().schedule(new DelayRestart(pc), 1000);
			// C_NewCharSelect.restartProcess(pc);// 解鎖

			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					GameClient clnt = pc.getNetConnection();
					String name = pc.getName();
					int x = pc.getX();
					int y = pc.getY();
					int mapId = pc.getMapId();
					C_NewCharSelect.restartProcess(pc);// 先執行解鎖操作
					try {
						Thread.sleep(800L);// 1.0 秒
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
						C_LoginToServer.doEnterWorld(name, clnt, false, x, y, mapId);// 嘗試重新連接
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}, 1000);
		} catch (Exception e) {
			gm.sendPackets(".角色壓制 [壓制角色名稱]");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	static class DelayRestart implements Runnable {
		private L1PcInstance _pc;

		DelayRestart(L1PcInstance pc) {
			_pc = pc;
		}

		@Override
		public void run() {
			try {
				if (_pc != null) {
					_pc.sendPackets(new S_Restart(_pc.getId(), 1), true);
					_pc.logout();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private static void initialize_user_private_probability(MJCommandArgs args) {
		try {
			String type_name = args.nextString();
			if (type_name.equalsIgnoreCase("全部")) {
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					pc.truncate_private_probability();
				}
				Updator.truncate("characters_private_probability");
				args.notify("所有角色的概率已被重置。");
			} else {
				String character_name = args.nextString();
				L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
				if (pc == null) {
					args.notify(String.format("無法找到 %s。", character_name));
					return;
				}
				pc.truncate_private_probability();
				Updator.exec("delete from characters_private_probability where object_id=?", new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, pc.getId());
					}
				});
				args.notify(String.format("%s 的概率已被重置。", character_name));
			}
		} catch (Exception e) {
			args.notify(".重置概率 角色 [角色名稱]");
			args.notify(".重置概率 全部");
		}
	}

	private static void update_user_private_probability(MJCommandArgs args) {
		try {
			String character_name = args.nextString();
			int skill_id = args.nextInt();
			int added_probability = args.nextInt();
			L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
			if (pc == null) {
				args.notify(String.format("無法找到 %s。", character_name));
				return;
			}

			pc.add_private_probability(skill_id, added_probability);
			args.notify(String.format("%s 對使用技能 ID %d 應用了額外的 %d%% 概率。", character_name, skill_id, added_probability));
		} catch (Exception e) {
			args.notify(".概率 [角色名稱] [技能ID] [附加概率]");
		}
	}

	private static void show_user_private_probability(MJCommandArgs args) {
		try {
			String character_name = args.nextString();
			int skill_id = args.nextInt();
			L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
			if (pc == null) {
				args.notify(String.format("無法找到 %s。", character_name));
				return;
			}
			args.notify(String.format("%s 的技能 ID %d 具有 %d%% 的附加概率。", character_name, skill_id, pc.get_private_probability(skill_id)));
		} catch (Exception e) {
			args.notify(".查詢概率 [角色名稱] [技能ID]");
		}
	}

	private static void setCharBlockDelete(L1PcInstance gm, String param) {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();
			if (name == null || name.equalsIgnoreCase(""))
				throw new Exception("");

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete from tb_character_block where name=?");
			pstm.setString(1, name);
			pstm.executeUpdate();
			gm.sendPackets(String.format("角色 [%s] 已解除壓制。", name));
		} catch (Exception e) {
			gm.sendPackets(".解除角色壓制 [解除角色名稱]");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public static boolean isCharacterBlock(String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from tb_character_block where name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return true;
	}

	private int _currentNum = -1 + 825;

	private void showProto(L1PcInstance gm, String param) {
		try {
			_currentNum = Integer.parseInt(param);
		} catch (Exception e) {
		}
	}

	private void showNextProto(L1PcInstance gm, String param) {
		// _currentNum = 603;
		/*
		 * for(GameClient clnt : MJNSHandler.getClients()){ clnt.sendPacket(S_ShowCmd.get(String.format("%d 0x%04X", _currentNum, _currentNum))); if (_currentNum != 13 && _currentNum != 14) { clnt.sendPacket(S_ShowCmd.getProto8(_currentNum));
		 * clnt.sendPacket(S_ShowCmd.getProtoA(_currentNum)); } }
		 */

		/*
		 * for(GameClient clnt : MJNSHandler.getClients()) { System.out.println(_currentNum); clnt.sendPacket(new S_LoginResult(_currentNum)); }
		 */

		gm.sendPackets(S_ShowCmd.get(String.format("%d 0x%04X", _currentNum, _currentNum)));

		System.out.println(String.format("%d 0x%04X", _currentNum, _currentNum));

		if (_currentNum != 13 && _currentNum != 14) {
			gm.sendPackets(S_ShowCmd.getProto8(_currentNum));
			gm.sendPackets(S_ShowCmd.getProtoA(_currentNum));
		}
		_currentNum++;
	}

	private void showChat(L1PcInstance gm, String param) {
		gm.sendPackets(S_ChatMessageNoti.getNotice(param, "哈哈"));
		// gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_WHISPER,
		// param, null, "梅蒂斯", null, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_ARENA_OBSERVER, "競技場觀察者", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_ARENA_TEAM, "競技場隊伍", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_CLASS, "職業", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_PLEDGE_ALLIANCE, "血盟聯盟", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_PLEDGE_NOTICE, "血盟公告", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_PLEDGE_PRINCE, "血盟王子", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_ROOM_ARENA_ALL, "房間競技場全體", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_TEAM, "隊伍", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_TRADE, "交易", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_CHAT_PARTY, "聊天隊伍", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_SHOUT, "喊話", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_HUNT_PARTY, "狩獵隊伍", MJSimpleRgb.red(), gm.getName(), gm, -1));
	}

	private void ping(L1PcInstance gm, String param) {
		if (param.equalsIgnoreCase("開")) {
			S_Ping._isRun = true;
			gm.sendPackets(S_Ping.getForGM(), false);
			gm.sendPackets("Ping 檢查已開啟。(SC_PING_REQ.java 已添加，因此不再可用)");
		} else if (param.equalsIgnoreCase("關")) {
			S_Ping._isRun = false;
			S_Ping._lastMs = 0L;
			gm.sendPackets("Ping 檢查已關閉。(SC_PING_REQ.java 已添加，因此不再可用)");
		}
	}

	public static boolean IS_PROTECTION = false;

	private void setProtectionMode(L1PcInstance gm, String param) {
		try {
			if (param.equalsIgnoreCase("開")) {
				IS_PROTECTION = true;
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[用戶保護模式啟動] 死亡時無懲罰。"), true);
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
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
					pc.sendPackets("\f3[保護模式啟動] 死亡時不會降低經驗值且不掉落物品。");
				}
			} else if (param.equalsIgnoreCase("關")) {
				IS_PROTECTION = false;
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[用戶保護模式關閉] 死亡時適用懲罰。"), true);
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
					noti.set_noti_type(eNotiType.END);
					noti.set_spell_id(L1SkillId.SAFE_MODE);
					pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					pc.sendPackets("\f3[保護模式啟動] 死亡時會降低經驗值且會掉落物品。");
				}
			} else if (param.equalsIgnoreCase("狀態")) {
			} else
				throw new Exception("");
			gm.sendPackets(String.format("當前保護模式狀態 [%s]", IS_PROTECTION ? "開" : "關"));
		} catch (Exception e) {
			gm.sendPackets(".保護模式 [開/關/狀態]");
		}
	}

	// 新增指令
	private void AccountCheck(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();
			CharacterTable.getInstance().CharacterAccountCheck(pc, name);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".帳號檢查 [角色名]"));
		}
	}

	private void AccountCheck1(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();
			CharacterTable.getInstance().CharacterAccountCheck1(pc, name);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".帳號確認 [角色名]"));
		}
	}

	private void jakjak(L1PcInstance gm) { // 數據包
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(210125);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人從沙蟲的沙袋中獲得了 (" + item.getName() + ")。"));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("有人從沙蟲的沙袋中獲得了 (" + item.getName() + ")。"));
	}

	private void jakjak2(L1PcInstance gm) { // 盔甲
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(5559);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "某人從沙蟲的沙袋中獲得了 (" + item.getName() + ")。"));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("某人從沙蟲的沙袋中獲得了 (" + item.getName() + ")。"));
	}

	private void jakjak3(L1PcInstance gm) { // 反擊屏障
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(41148);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "某人從艾爾扎貝的蛋中獲得了 (" + item.getName() + ")。"));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("某人從艾爾扎貝的蛋中獲得了 (" + item.getName() + ")。"));
	}

	private void dolldemon(L1PcInstance gm) { // 惡魔玩偶
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(745);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "某人成功合成了 " + item.getName() + "。"));
		L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "\fH某人成功合成了 " + item.getName() + "。"));
	}

	private void dolldeath(L1PcInstance gm) { // 死亡玩偶
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(746);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "某人成功合成了 " + item.getName() + "。"));
		L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "\fH某人成功合成了 " + item.getName() + "。"));
	}

	private void dolltarak(L1PcInstance gm) { // 墮落玩偶
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(3000352);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "某人成功合成了 " + item.getName() + "。"));
		L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "\fH某人成功合成了 " + item.getName() + "。"));
	}

	private void doice(L1PcInstance gm) { // 冰女工作
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(3000352);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "某人成功合成了 " + item.getName() + "。"));
		L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "\fH某人成功合成了 " + item.getName() + "。"));
	}

	private void Stone(L1PcInstance gm) { // 符文石合成
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(41148);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "某人從閃亮的符文石中獲得了 " + item.getName() + "。"));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("某人從閃亮的符文石中獲得了 (" + item.getName() + ")。"));
	}

	private void Stone1(L1PcInstance gm) { // 符文石獲得
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(40222);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "某人從閃亮的符文石中獲得了 " + item.getName() + "。"));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("某人從閃亮的符文石中獲得了 (" + item.getName() + ")。"));
	}

	private void Stone2(L1PcInstance gm) { // 符文石獲取
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(210125);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "某人從閃亮的符文石中獲得了 " + item.getName() + "。"));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("某人從閃亮的符文石中獲得了 (" + item.getName() + ")。"));
	}

	private void Stone3(L1PcInstance gm) { // 符文石防具
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(5559);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "某人從閃亮的符文石中獲得了 " + item.getName() + "。"));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("某人從閃亮的符文石中獲得了 (" + item.getName() + ")。"));
	}

	private void addEventBoss(L1PcInstance gm, String param) {
		// TODO 自動生成的函數存根
		try {
			StringTokenizer st = new StringTokenizer(param);
			String BossName = st.nextToken();
			if (BossName.equalsIgnoreCase("庫茲")) {
				// Boss-specific logic here
				L1SpawnUtil.spawn2(32854, 33261, (short) 4, 45600, 0, 3600 * 1000, 0);
				L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
				L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
				L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
				L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[活動頭目(1/6)]:衝鋒！庫茲的部隊被召喚。");
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動頭目(1/6)]:衝鋒！庫茲的部隊被召喚。"), true);
			} else if (BossName.equalsIgnoreCase("死亡騎士")) {
				L1SpawnUtil.spawn2(32856, 33263, (short) 4, 45601, 0, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[活動頭目(2/6)]:衝鋒！死亡騎士的部隊被召喚出來了。");
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動頭目(2/6)]:衝鋒！死亡騎士的部隊被召喚出來了。"), true);
			} else if (BossName.equalsIgnoreCase("不死鳥")) {
				L1SpawnUtil.spawn2(32853, 33265, (short) 4, 45617, 50, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[活動頭目(3/6)]:衝鋒！不死鳥的部隊被召喚出來了。");
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動頭目(3/6)]:衝鋒！不死鳥的部隊被召喚出來了。"), true);
			} else if (BossName.equalsIgnoreCase("魔靈君王萊亞")) {
				L1SpawnUtil.spawn2(32854, 33259, (short) 4, 45863, 0, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[活動頭目(4/6)]:衝鋒！魔靈君王萊亞的部隊被召喚出來了。");
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動頭目(4/6)]:衝鋒！魔靈君王萊亞的部隊被召喚出來了。"), true);
			} else if (BossName.equalsIgnoreCase("死神格林里珀")) {
				L1SpawnUtil.spawn2(32854, 33266, (short) 4, 7310077, 0, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[活動頭目(5/6)]:衝鋒！死神格林里珀的部隊被召喚出來了。");
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動頭目(5/6)]:衝鋒！死神格林里珀的部隊被召喚出來了。"), true);
			} else if (BossName.equalsIgnoreCase("恐怖的安塔瑞斯")) {
				L1SpawnUtil.spawn2(32848, 33260, (short) 4, 7310154, 0, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[活動頭目(6/6)]:衝鋒！最後的怪物被召喚出來了。");
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動頭目(6/6)]:衝鋒！最後的怪物被召喚出來了。"), true);
			}
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage(".boss [BossName] ex<庫茲/死亡騎士/不死鳥/魔靈君王萊亞/死神格林里珀/恐怖的安塔瑞斯"));
		}
	}

	/*
	 * private void setMaxLevel(L1PcInstance gm, String param){ try{ String[] arr = param.split(" "); if(arr[0].equalsIgnoreCase("設定")){ int i = Integer.parseInt(arr[1]); Config.MAX_LEVEL = i; Config.MAX_LEVEL_EXP = ExpTable.getExpByLevel(i) + 5; }else
	 * if(arr[0].equalsIgnoreCase("確認")){ }else throw new Exception(); gm.sendPackets(String.format( "當前設定的最大等級 : %d(exp : %d)", Config.MAX_LEVEL, Config.MAX_LEVEL_EXP))); }catch(Exception e){ gm.sendPackets(".最大等級 [確認]")); gm.sendPackets(
	 * ".最大等級 [設定] [等級]")); } }
	 */

	public void setPresentationCode(L1PcInstance gm, String param) {
		try {
			L1ItemInstance.presentationCode = Integer.parseInt(param);
			gm.sendPackets(String.format("顯示編號已設置為 %d。", L1ItemInstance.presentationCode));
		} catch (Exception e) {
			gm.sendPackets(String.format(".顯示設置 數字"));
		}
	}

	public void showObjectList(L1PcInstance gm, String param) {
		StringBuilder sb = new StringBuilder(256);
		for (L1Object obj : gm.getKnownObjects()) {
			if (obj instanceof L1Character) {
				L1Character c = (L1Character) obj;
				sb.append(c.getName()).append(" ").append(obj.getX()).append(" ").append(obj.getY()).append("\n");
			}
		}

		gm.sendPackets(sb.toString());

		/*
		 * StringBuilder sb = new StringBuilder(256); for (L1Object obj : gm.getKnownObjects()) { if (obj instanceof L1NpcInstance) { L1NpcInstance c = (L1NpcInstance) obj; c.removeKnownObject(obj); c.deleteMe();
		 * sb.append(c.getName()).append(" ").append(obj.getX()).append(" ").append(obj. getY()).append("\n"); System.out.println(c); } } gm.sendPackets(sb.toString());
		 */
	}

	public static void setCharacterInstanceStatus(MJCommandArgs args) {
		try {
			String name = args.nextString();
			int status = args.nextInt();
			L1PcInstance pc = L1World.getInstance().getPlayer(name);
			if (pc == null) {
				args.notify(String.format("找不到角色名稱：%s。", name));
				return;
			}
			MJEPcStatus e_status = MJEPcStatus.fromInt(status);
			if (e_status.equals(MJEPcStatus.NONE)) {
				StringBuilder sb = new StringBuilder(256);
				MJEPcStatus[] statuses = MJEPcStatus.values();
				sb.append("請正確輸入狀態值 ->\r\n");
				for (int i = 0; i < statuses.length; ++i) {
					if (i != 0)
						sb.append(", ");
					sb.append("[").append(statuses[i].toInt()).append(".").append(statuses[i]).append("]");
				}
				args.notify(sb.toString());
				return;
			}
			pc.set_instance_status(e_status);
		} catch (Exception e) {
			args.notify(".角色狀態 [角色名稱] [狀態值]");
		} finally {
			args.dispose();
		}
	}

	private void CurseCharacter(MJCommandArgs args) {
		try {
			String name = args.nextString();
			if (name == null || name.equalsIgnoreCase(""))
				throw new Exception();

			L1PcInstance pc = L1World.getInstance().getPlayer(name);
			if (pc == null) {
				args.notify(String.format("找不到%s。", name));
				return;
			}

			pc.add_lateral_damage(args.nextInt());
			pc.add_lateral_reduction(args.nextInt());
			pc.add_lateral_magic_rate(args.nextInt());
			pc.update_lateral_status();
			args.notify(String.format("%s 的當前狀態 - 傷害: %d, 減傷: %d, 魔法傷害: %d", name, pc.get_lateral_damage(), pc.get_lateral_reduction(), pc.get_lateral_magic_rate()));
		} catch (Exception e) {
			args.notify(".詛咒 [角色名稱] [傷害] [減傷] [魔法傷害]");
		}
	}

	private void CurseInitializeCharacter(MJCommandArgs args) {
		try {
			String command_type = args.nextString();
			if (command_type.equalsIgnoreCase("全部")) {
				L1World.getInstance().getAllPlayerStream().filter((L1PcInstance pc) -> {
					return pc != null && pc.getAI() == null;
				}).forEach((L1PcInstance pc) -> {
					pc.set_lateral_damage(0);
					pc.set_lateral_reduction(0);
					pc.set_lateral_magic_rate(0);
				});
				Updator.truncate("tb_lateral_status");
				args.notify("已初始化所有數據。");
			} else if (command_type.equalsIgnoreCase("角色")) {
				String name = args.nextString();
				if (name == null || name.equalsIgnoreCase(""))
					throw new Exception();
				L1PcInstance pc = L1World.getInstance().getPlayer(name);
				if (pc == null) {
					args.notify(String.format("找不到%s。", name));
					return;
				}
				pc.delete_lateral_status();
				args.notify(String.format("已初始化 %s 的數據。", name));
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			args.notify(".詛咒初始化 [全部] | [角色(角色名稱)]");
		}
	}

	private void clanMark(L1PcInstance pc, String param) {
		// TODO 自動生成的方法存根
		try {
			StringTokenizer st = new StringTokenizer(param);
			String onoff = st.nextToken();
			if (onoff.equalsIgnoreCase("開")) {
				pc.sendPackets(new S_MARK_SEE(pc, 2, true), true);
				pc.sendPackets(new S_MARK_SEE(pc, 0, true), true);
				pc.sendPackets("開始顯示血跡標記。");
			} else if (onoff.equalsIgnoreCase("關")) {
				pc.sendPackets(new S_MARK_SEE(pc, 2, false), true);
				pc.sendPackets(new S_MARK_SEE(pc, 1, false), true);
				pc.sendPackets("結束顯示血跡標記。");
			} else {
				pc.sendPackets(new S_SystemMessage(".血跡標記 [開 / 關]"));
				return;
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".血印標記 [開 / 關]"));
		}
	}

	private void character_delete(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			int level = Integer.valueOf(tokenizer.nextToken());
			int day = Integer.valueOf(tokenizer.nextToken());

			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT * FROM characters WHERE HighLevel < ?");
				pstm.setInt(1, level);
				rs = pstm.executeQuery();
				while (rs.next()) {
					long last_logout_check_time = rs.getTimestamp("lastLogoutTime").getTime() + (86400000 * day);
					if (last_logout_check_time < System.currentTimeMillis()) {
						// 刪除
						String account_name = rs.getString("account_name");
						String char_name = rs.getString("char_name");
						gm.sendPackets(new S_SystemMessage(account_name + "帳戶的 " + char_name + " 的角色已被刪除。"), true);
						CharacterTable.getInstance().deleteCharacter(account_name, char_name);
						gm.sendPackets(new S_SystemMessage("已成功刪除該用戶。"), true);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage(".角色清理 [最低等級] [閒置天數]"));
		}
	}

	private void standBy77(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String pcName = tokenizer.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			int objid = 0;
			String acname = null;
			if (target != null) {
				target.sendPackets(new S_Disconnect());
			}
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT objid, account_name FROM characters WHERE char_name=?");
				pstm.setString(1, pcName);
				rs = pstm.executeQuery();
				while (rs.next()) {
					objid = rs.getInt(1);
					acname = rs.getString(2);
				}
				if (objid == 0) {
					gm.sendPackets(new S_SystemMessage("資料庫中不存在該用戶名。"), true);

				} else {
					gm.sendPackets(new S_SystemMessage(acname + "帳戶的 " + pcName + " 的角色已被刪除。"), true);
					CharacterTable.getInstance().deleteCharacter(acname, pcName);
					gm.sendPackets(new S_SystemMessage("已成功刪除該用戶。"), true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}

		} catch (Exception eee) {
			gm.sendPackets(new S_SystemMessage(".角色刪除 [角色名]"), true);
		}
	}

	private void Blessleaf(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target._Blessleaf = true;
				gm.sendPackets("" + target.getName() + " 您的失去氣運的物品成功率一次性成為100%。");
			} else {
				gm.sendPackets("沒有這樣的角色。");
			}
		} catch (Exception e) {
			gm.sendPackets("請輸入 '.氣運祝福 角色名'。");
		}
	}

	private void CraftSuccess(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target._CraftSuccess = true;
				gm.sendPackets("\f3" + target.getName() + " 您的製作成功率一次性成為100%。");
			} else {
				gm.sendPackets("\f3沒有這樣的角色。");
			}
		} catch (Exception e) {
			gm.sendPackets("\f3請輸入 '.製作成功 角色名'。");
		}
	}

	private void EnchantWeaponSuccess(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target._EnchantWeaponSuccess = true;
				gm.sendPackets("" + target.getName() + " 您的武器附魔成功率一次性成為100%。");
			} else {
				gm.sendPackets("沒有這樣的角色。");
			}
		} catch (Exception e) {
			gm.sendPackets("請輸入 '.武器強化成功 角色名稱'。");
		}
	}

	private void EnchantArmorSuccess(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target._EnchantArmorSuccess = true;
				gm.sendPackets("" + target.getName() + " 您的防具強化成功率一次性成為100%。");
			} else {
				gm.sendPackets("沒有這樣的角色。");
			}
		} catch (Exception e) {
			gm.sendPackets("請輸入 '.防具強化成功 角色名稱'。");
		}
	}

	private void ErzabeBox(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target._ErzabeBox = true;
				gm.sendPackets("(艾爾札貝的蛋)" + target.getName() + " 您的四大魔法習得率一次性成為100%。");
			} else {
				gm.sendPackets("沒有這樣的角色。");
			}
		} catch (Exception e) {
			gm.sendPackets("請輸入 '.艾爾札貝 角色名稱'。");
		}
	}

	private void SandwormBox(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target._SandwormBox = true;
				gm.sendPackets("(沙蟲的袋子)" + target.getName() + " 您的四大魔法習得率一次性成為100%。");
			} else {
				gm.sendPackets("沒有這樣的角色。");
			}
		} catch (Exception e) {
			gm.sendPackets("請輸入 '.沙蟲 角色名稱'。");
		}
	}

	private void befixed(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String phone = tok.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(param);
			if (target == null) {
				gm.sendPackets("角色不存在。");
				return;
			}

			Account account = Account.load(target.getAccountName());
			if (account.getphone() != null) {
				gm.sendPackets("您已經是固定會員了。");
				return;
			}
			account.setphone("00000000000");
			Account.updatePhone(account);
			gm.sendPackets(" " + phone + " 設定完成。");
			target.sendPackets("固定申請已完成，增益效果將啟動。");
			securityBuff(target);
		} catch (Exception e) {
		}
	}

	private static void securityBuff(L1PcInstance gm) {
		gm.getAC().addAc(-1);
		gm.sendPackets(new S_PacketBox(gm, S_PacketBox.ICON_SECURITY_SERVICES));
		gm.sendPackets(new S_OwnCharStatus(gm));
	}

	private void searchclaner(L1PcInstance gm, String name) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(name);
			String charinfo = null;
			charinfo = String.format("select account_name, char_name, level, clanname, OnlineStatus from characters where clanname Like '%s'", target.getClanname());

			if (target.getClan() == null) {
				gm.sendPackets("您查詢的角色尚未加入血盟。");
				return;
			}

			Selector.exec(charinfo, new FullSelectorHandler() {
				@Override
				public void result(ResultSet rs) throws Exception {
					int i = 0;
					gm.sendPackets(String.format("================== 血盟信息 ==================", i));
					while (rs.next()) {
						++i;
						if (rs.getInt(5) > 0) {
							gm.sendPackets(String.format("\fY帳號:%s 角色名:%s 等級:%s 血盟:%s", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
						}
					}
				}
			});

			String claninfo = null;
			claninfo = String.format("select total_m, current_m from clan_data where clan_name Like '%s'", target.getClanname());

			Selector.exec(claninfo, new FullSelectorHandler() {
				@Override
				public void result(ResultSet rs) throws Exception {
					while (rs.next()) {
						gm.sendPackets(String.format("\fU總血盟員人數 : [%s] 目前連線的血盟員 : %s", rs.getString(1), rs.getString(2)));
					}
				}
			});
		} catch (Exception e) {
			gm.sendPackets("您搜尋的角色名稱有誤或不存在。");
		}
	}

	private void DominanceBoss(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			int commandnum = Integer.parseInt(st.nextToken(), 10);
			List<DominanceBoss> list = DominanceDataLoader.getList();
			if (commandnum == 0) {
				DominanceDataLoader.reload();
				gm.sendPackets("正在重新加載支配之塔首領信息。");
			}
			if (list.size() > 0) {
				for (DominanceBoss b : list) {
					try {
						switch (commandnum) {
							case 1:
								if (b.getBossNum() == 1) {
									DominanceFloorLv1 zenis = new DominanceFloorLv1(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
									zenis.Start();
									gm.sendPackets("正在進行支配之塔一樓首領召喚活動");
								}
								break;
							case 2:
								if (b.getBossNum() == 2) {
									DominanceFloorLv2 sier = new DominanceFloorLv2(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
									sier.Start();
									gm.sendPackets("正在進行支配之塔二樓首領召喚活動");
								}
								break;
							case 3:
								if (b.getBossNum() == 3) {
									DominanceFloorLv3 vampire = new DominanceFloorLv3(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
									vampire.Start();
									gm.sendPackets("正在進行支配之塔三樓首領召喚活動");
								}
								break;
							case 4:
								if (b.getBossNum() == 4) {
									DominanceFloorLv4 zombie = new DominanceFloorLv4(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
									zombie.Start();
									gm.sendPackets("正在進行支配之塔四樓首領召喚活動");
								}
								break;
							case 5:
								if (b.getBossNum() == 5) {
									DominanceFloorLv5 kuger = new DominanceFloorLv5(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
									kuger.Start();
									gm.sendPackets("正在進行支配之塔五樓首領召喚活動");
								}
								break;
							case 6:
								if (b.getBossNum() == 6) {
									DominanceFloorLv6 mummy = new DominanceFloorLv6(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
									mummy.Start();
									gm.sendPackets("正在進行支配之塔六樓首領召喚活動");
								}
								break;
							case 7:
								if (b.getBossNum() == 7) {
									DominanceFloorLv7 iris = new DominanceFloorLv7(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
									iris.Start();
									gm.sendPackets("正在進行支配之塔七樓首領召喚活動");
								}
								break;
							case 8:
								if (b.getBossNum() == 8) {
									DominanceFloorLv8 bald = new DominanceFloorLv8(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
									bald.Start();
									gm.sendPackets("正在進行支配之塔八樓首領召喚活動");
								}
								break;
							case 9:
								if (b.getBossNum() == 9) {
									DominanceFloorLv9 rich = new DominanceFloorLv9(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
									rich.Start();
									gm.sendPackets("正在進行支配之塔九樓首領召喚活動");
								}
								break;
							case 10:
								if (b.getBossNum() == 10) {
									DominanceFloorLv10 ugnus = new DominanceFloorLv10(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
									ugnus.Start();
									gm.sendPackets("正在進行支配之塔十樓首領召喚活動");
								}
								break;
							case 11:
								if (b.getBossNum() == 11) {
									DominanceFloorLv11 riper = new DominanceFloorLv11(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
									riper.Start();
									gm.sendPackets("正在進行支配之塔頂樓首領召喚活動");
								}
								break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			gm.sendPackets("首領生成：.支配首領 0~11（0=重載，1~11=各層首領）");
		}
	}

	public static void do_test_returner_server(L1PcInstance gm, String param) {
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				try {
					MJShiftObjectManager.getInstance().do_returner();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}, 1000);
	}

	public static void do_shift_server(L1PcInstance gm, String param) {
		try {
			String[] array = param.split(" ");
			L1PcInstance pc = L1World.getInstance().findpc(array[0]);
			if (pc == null) {
				gm.sendPackets(String.format("%s 是不存在的角色。", param));
				return;
			}

			try {
				MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.TRANSFER, array[1], MJString.EmptyString);
				gm.sendPackets(String.format("已移動 %s。", param));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			gm.sendPackets(".伺服器轉移 [角色名] [原伺服器識別名稱]");
		}
	}

	private void do_cache_exp(MJCommandArgs args) {
		try {
			final String character_name = args.nextString();
			if (l1j.server.MJTemplate.MJString.isNullOrEmpty(character_name))
				throw new Exception();

			MJObjectWrapper<LevelExpPair> wrapper = new MJObjectWrapper<LevelExpPair>();
			Selector.exec("select * from character_exp_cache where character_name=?", new SelectorHandler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, character_name);
				}

				@Override
				public void result(ResultSet rs) throws Exception {
					if (rs.next()) {
						wrapper.value = new LevelExpPair();
						wrapper.value.level = rs.getInt("lvl");
						wrapper.value.exp = rs.getLong("exp");
					}
				}
			});
			if (wrapper.value == null) {
				args.notify(String.format("無法找到 %s 的現金經驗值數據。", character_name));
				return;
			}
			L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
			if (pc == null) {
				Updator.exec("update characters set level=?, exp=? where char_name=?", new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, wrapper.value.level);
						pstm.setLong(++idx, wrapper.value.exp);
						pstm.setString(++idx, character_name);
					}
				});
				args.notify(String.format("已更新 %s 的資料庫數據。(等級:%d, 經驗值:%d)", character_name, wrapper.value.level, wrapper.value.exp));
			} else {
				pc.set_exp(wrapper.value.exp);
				args.notify(String.format("已將 %s 的經驗值更改為 %d。", character_name, wrapper.value.exp));
			}
		} catch (Exception e) {
			args.notify(".現金經驗值 [角色名]");
		}
	}

	static class LevelExpPair {
		int level;
		long exp;
	}

	public static void inventoryAskCommands(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String name = tokenizer.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(name);

			if (target == null) {
				gm.sendPackets("無法找到角色信息。");
				return;
			}

			if (target == gm) {
				gm.sendPackets("無法對自己使用。");
				return;
			}

			if (target != null)
				gm.sendPackets(S_InvCheck.get(target));
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage(".檢查背包 [角色名]"), true);
		}
	}

	private void registerCoupon(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String code = st.nextToken();

			if (pc == null)
				return;

			MJPaymentInfo m_pInfo = MJPaymentInfo.newInstance(code.toUpperCase());
			if (m_pInfo == null) {
				pc.sendPackets(".註冊優惠券 [號碼]");
				pc.sendPackets("優惠券註冊號碼不存在。");
				return;
			}

			if(Check_Coupon(code.toUpperCase())) {
				pc.sendPackets("此優惠券號碼已被使用。");
				return;
			}

			SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
			if (pwh == null)
				return;

			m_pInfo.set_account_name(pc.getAccountName()).set_character_name(pc.getName()).set_expire_date(MJNSHandler.getLocalTime()).set_is_use(true).do_update();

			L1Item tempItem = ItemTable.getInstance().getTemplate(m_pInfo.get_itemid());
			if (tempItem.isStackable()) {
				L1ItemInstance item = ItemTable.getInstance().createItem(tempItem.getItemId());
				item.setIdentified(true);
				item.setCount(m_pInfo.get_count());
				pwh.storeTradeItem(item);
			} else {
				L1ItemInstance item = null;
				int createCount;
				for (createCount = 0; createCount < m_pInfo.get_count(); createCount++) {
					item = ItemTable.getInstance().createItem(tempItem.getItemId());
					item.setIdentified(true);
					pwh.storeTradeItem(item);
				}
			}

			// _player.getInventory().storeItem(m_pInfo.get_itemid(), m_pInfo.get_count());//直接發放到背包
			SC_GOODS_INVEN_NOTI.do_send(pc);
			pc.send_effect(2048);
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format("\f3已發放 %s 個優惠券物品。請在附加物品倉庫中領取。", new DecimalFormat("#,##0").format(m_pInfo.get_count()))));
			pc.sendPackets(String.format("已發放 %s 個優惠券物品。", new DecimalFormat("#,##0").format(m_pInfo.get_count())));
		} catch (Exception e) {
			pc.sendPackets(".註冊優惠券 [號碼]");
		}
	}

	private boolean Check_Coupon(String coupon) {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			int code = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select is_use from payment_info where code Like '"+coupon+"'");
			rs = pstm.executeQuery();
			if (rs.next()) {
				code = rs.getInt(1);
			}
			if (code != 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}

	private void appcenter(L1PcInstance pc, String param) {
		// TODO 自動生成的方法存根
		try {
			StringTokenizer st = new StringTokenizer(param);
			String onoff = st.nextToken();
			if (onoff.equalsIgnoreCase("開")) {
				Config.Web.webServerOnOff = true;
				pc.sendPackets("\\f2앱센터를 활성화합니다.");
				// L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\f2應用中心的臨時維護已結束。現在可以使用了。"));
				// L1World.getInstance().broadcastPacketToAll(new
				// S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f2應用中心的臨時維護已結束。現在可以使用了。"));
			} else if (onoff.equalsIgnoreCase("關")) {
				Config.Web.webServerOnOff = false;
				pc.sendPackets("\f3正在停用應用中心。");
				// L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aG應用中心的臨時維護即將開始。無法使用。"));
				// L1World.getInstance().broadcastPacketToAll(new
				// S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aG應用中心的臨時維護即將開始。無法使用。"));
			} else if (onoff.equalsIgnoreCase("更改端口")) {
				int port = Integer.parseInt(st.nextToken(), 10);
				Config.Web.webServerPort = port;
				pc.sendPackets("正在更改應用中心端口。");
			} else if (onoff.equalsIgnoreCase("交易")) {
				if (Config.Web.tradeMenu) {
					Config.Web.tradeMenu = false;
					pc.sendPackets("正在啟用應用中心交易公告板菜單。");
				} else {
					Config.Web.tradeMenu = true;
					pc.sendPackets("正在暫停應用中心交易公告板菜單。");
				}
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".應用中心 [開/關/端口變更(變更前為停用狀態)/交易]"));
		}
	}

	/*private void PowerBall(L1PcInstance pc, String param) {
	// TODO 自動生成的方法存根
	try {
	StringTokenizer st = new StringTokenizer(param);
	String onoff = st.nextToken();
	if (onoff.equalsIgnoreCase("開")) {
	//PowerBallController.getInstance()._executeStatus = 0;
	//pc.sendPackets("啟動 PowerBall 系統。");
	// L1World.getInstance().broadcastPacketToAll(new
	// S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f2應用中心的臨時維護已結束。現在可以使用了。"));
	} else if (onoff.equalsIgnoreCase("關")) {
	//PowerBallController.getInstance()._executeStatus = 5;
	//pc.sendPackets("關閉 PowerBall 系統。");
	// L1World.getInstance().broadcastPacketToAll(new
	// S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\aG應用中心的臨時維護即將開始。無法使用。"));
	}
	} catch (Exception e) {
	pc.sendPackets(new S_SystemMessage(".PowerBall [開 / 關]"));
	}
	}*/


	public static void increaseNcoin(final L1PcInstance gm, final String name, final int coin) {
		L1PcInstance tg = L1World.getInstance().getPlayer(name);
		if (tg != null) {
			tg.addNcoin(coin);
			tg.getNetConnection().getAccount().updateNcoin();

			String s = String.format("\\aGN幣 (%,d) 元已充值成功。謝謝。", coin);
			tg.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, s));
			tg.sendPackets(s);
//			additem(tg, 4100681, coin, 0, 129, 0, true);

			String remainMessage = String.format("\f2持有 N幣:(%,d) 元", tg.getNetConnection().getAccount().Ncoin_point);
			tg.sendPackets(remainMessage);
			gm.sendPackets(String.format("\\aA角色名:[\\aG%s\\aA]" + " 充值金額:[\\aG%,d\\aA] 完成! \f2[目標持有 N幣:(%,d) 元]", name, coin, tg.getNetConnection().getAccount().Ncoin_point));
		} else {
			final MJObjectWrapper<String> accountWrapper = new MJObjectWrapper<String>();
			accountWrapper.value = MJString.EmptyString;
			Selector.exec("select account_name from characters where char_name=?", new SelectorHandler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, name);
				}

				@Override
				public void result(ResultSet rs) throws Exception {
					if (rs.next()) {
						accountWrapper.value = rs.getString("account_name");
					}
				}
			});
			if (MJString.isNullOrEmpty(accountWrapper.value)) {
				gm.sendPackets(String.format("%s 是不存在的角色。", name));
			} else {
				Updator.exec("update accounts set Ncoin_Point=Ncoin_Point+? where login=?", new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, coin);
						pstm.setString(2, accountWrapper.value);
					}
				});
				gm.sendPackets(String.format("\\aA角色名:[\\aG%s(%s)\\aA] 充值金額:[\\aG%d\\aA] 完成", name, accountWrapper.value, coin));
			}
		}
	}

	public static void increaseNcoinshop(final L1PcInstance gm, final String name, final int coin, MJMyTradeShopModel shop) {
		L1PcInstance tg = L1World.getInstance().getPlayer(name);
		if (tg != null) {
			tg.addNcoin(coin);
			tg.getNetConnection().getAccount().updateNcoin();
			tg.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f2應用中心 (CTRL+Z) 交易所中銷售的物品已賣出，N幣已經積累。"));
			tg.sendPackets(String.format("\f2您在交易所中銷售的物品已賣出，N幣 (%,d) 元已經積累。", shop.itemModel().price()));
			tg.sendPackets(String.format("\f2持有 N幣:(%,d) 元", tg.getNetConnection().getAccount().Ncoin_point));
		} else {
			final MJObjectWrapper<String> accountWrapper = new MJObjectWrapper<String>();
			accountWrapper.value = MJString.EmptyString;
			Selector.exec("select account_name from characters where char_name=?", new SelectorHandler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, name);
				}

				@Override
				public void result(ResultSet rs) throws Exception {
					if (rs.next()) {
						accountWrapper.value = rs.getString("account_name");
					}
				}
			});
			if (MJString.isNullOrEmpty(accountWrapper.value)) {
				gm.sendPackets(String.format("%s 是不存在的角色。", name));
			} else {
				Updator.exec("update accounts set Ncoin_Point=Ncoin_Point+? where login=?", new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, coin);
						pstm.setString(2, accountWrapper.value);
					}
				});
				gm.sendPackets(String.format("\\aA角色名:[\\aG%s(%s)\\aA] 充值金額:[\\aG%d\\aA] 完成", name, accountWrapper.value, coin));
			}
		}
	}

	private void InterJump(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();
			if (name.equalsIgnoreCase("本地")) {
				MJPoint pt = MJPoint.newInstance(33441, 32813, 5, (short) 4, 50);
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(gm, pt.x, pt.y, pt.mapId, 1);
			} else if (name.equalsIgnoreCase("程式員")) {
				MJPoint pt = MJPoint.newInstance(33441, 32813, 5, (short) 4, 50);
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(gm, pt.x, pt.y, pt.mapId, 2);
			} else if (name.equalsIgnoreCase("伺服器")) {
				MJPoint pt = MJPoint.newInstance(33441, 32813, 5, (short) 4, 50);
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(gm, pt.x, pt.y, pt.mapId, 3);
			}
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage(".跳轉 本地/伺服器"));
		}
	}

	private void UserInterJump(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String charname = st.nextToken();
			String server = st.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(charname);
			if (target == null) {
				gm.sendPackets("該用戶目前未在線。");
				return;
			}
			if (server.equalsIgnoreCase("本地")) {
				MJPoint pt = MJPoint.newInstance(33441, 32813, 5, (short) 4, 50);
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(target, pt.x, pt.y, pt.mapId, 1);
				gm.sendPackets(target.getName() + " 已將您移動至本地伺服器。");
			} else if (server.equalsIgnoreCase("程式員")) {
				MJPoint pt = MJPoint.newInstance(33441, 32813, 5, (short) 4, 50);
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(target, pt.x, pt.y, pt.mapId, 2);
				gm.sendPackets(target.getName() + " 已將您移動至程式員伺服器。");
			} else if (server.equalsIgnoreCase("伺服器")) {
				MJPoint pt = MJPoint.newInstance(33441, 32813, 5, (short) 4, 50);
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(target, pt.x, pt.y, pt.mapId, 3);
				gm.sendPackets(target.getName() + " 已將您移動至正常伺服器。");
			}
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage(".用戶跳轉 角色名 本地/伺服器"));
		}
	}

	private void allUserInterJump(L1PcInstance gm, String param) {
		int count = 0;
		try {
			StringTokenizer st = new StringTokenizer(param);
			String server = st.nextToken();
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (!pc.isGm() && !pc.isPrivateShop() && !pc.noPlayerCK && !pc.noPlayerck2) {
					if (server.equalsIgnoreCase("本地")) {
						MJPoint pt = MJPoint.newInstance(33432, 32811, 10, (short) 4, 50);
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(pc, pt.x, pt.y, pt.mapId, 1);
					} else if (server.equalsIgnoreCase("程式員")) {
						MJPoint pt = MJPoint.newInstance(33432, 32811, 10, (short) 4, 50);
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(pc, pt.x, pt.y, pt.mapId, 2);
					} else if (server.equalsIgnoreCase("伺服器")) {
						MJPoint pt = MJPoint.newInstance(33432, 32811, 10, (short) 4, 50);
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(pc, pt.x, pt.y, pt.mapId, 3);
					}
					count++;
				}
			}
			gm.sendPackets(String.format("通知: 已將 %d 名用戶移動至 [" + server + "]。", count));
		} catch (Exception e) {
			gm.sendPackets(".全部跳轉 本地/伺服器");
		}
	}

	public static void NoticeChat(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String Chat = tokenizer.nextToken();
			if (Chat.equalsIgnoreCase("開")) {
				gm.Notice = true;
				gm.sendPackets(new S_SystemMessage("將全體聊天切換到公告模式。"));
			} else if (Chat.equalsIgnoreCase("關")) {
				gm.Notice = false;
				gm.sendPackets(new S_SystemMessage("將公告模式切換回普通全體聊天。"));
			}
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage(".公告 [開/關]"));
		}
	}

	private static void testCommand(L1PcInstance gm, String param) {
		try {
			int ratio = 4;
			int mapid = Integer.parseInt(param);
			L1Map m = L1WorldMap.getInstance().getMap((short) mapid);
			int sx = MapsTable.getInstance().getStartX(mapid);
			int sy = MapsTable.getInstance().getStartY(mapid);
			int width = m.getWidth();
			int height = m.getHeight();
			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			Color c = new Color(0, 160, 0);
			int color = c.getRGB();
			for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					// int a = m.getOriginalTile(x, y);
					// img.setRGB(x, y, rgb);
					if (m.isPassable(x + sx, y + sy)) {
						img.setRGB(x, y, Color.WHITE.getRGB());
					} else {
						img.setRGB(x, y, color);
					}
				}
			}

			for (L1Object obj : L1World.getInstance().getVisibleObjects(mapid).values()) {
				if (obj == null || !(obj instanceof L1NpcInstance)) {
					continue;
				}
				L1NpcInstance npc = (L1NpcInstance) obj;
				int x = npc.getX() - sx;
				int y = npc.getY() - sy;

				img.setRGB(x, y, Color.RED.getRGB());
				Graphics2D graphics = img.createGraphics();
				try {
					graphics.setColor(Color.RED);
					graphics.drawString(npc.getName(), x, y);
				} finally {
					graphics.dispose();
				}
			}

			ImageIO.write(img, "PNG", new File("test.png"));
			gm.sendPackets("test!!!");
		} catch (Exception e) {
			gm.sendPackets("map no");
		}
	}

	private void useWaitQueue(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);

			String type = st.nextToken();

			if (type.equalsIgnoreCase("設定")) {
				int minute = Integer.valueOf(st.nextToken());
				MJClientEntranceService.service().useWaitQueue(true);
				gm.sendPackets(minute + "秒內等待系統啟用");
				l1j.server.server.GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						MJClientEntranceService.service().useWaitQueue(false);
						return null;
					}
				}, minute * 1000L);
			} else if (type.equalsIgnoreCase("重新加載")) {
				MJMonitorCacheProvider.monitorCache().getContent("mj-entrance-model").readContent();
				gm.sendPackets("mj-entrance-model 重新加載完成");
			}

		} catch (Exception e) {
			gm.sendPackets(".排隊 [設定/重新加載] - 設定時請輸入要維持的秒數。");
		}
	}

	private void mapwho(L1PcInstance gm, String param) {
		// TODO 自動生成的方法存根
		try {
			StringTokenizer st = new StringTokenizer(param);
			int i = 0;
			try {
				i = Integer.parseInt(st.nextToken(), 10);
			} catch (Exception e) {
				i = gm.getMapId();
			}

			StringBuffer gmList = new StringBuffer();
			StringBuffer playList = new StringBuffer();
			StringBuffer noplayList = new StringBuffer();
			StringBuffer shopList = new StringBuffer();

			int countGM = 0, nocountPlayer = 0, countPlayer = 0, countShop = 0;

			for (L1Object each1 : L1World.getInstance().getVisibleObjects(i).values()) {
				if (each1 instanceof L1PcInstance) {
					L1PcInstance eachpc = (L1PcInstance) each1;

					if (eachpc.isGm()) {
						gmList.append("名稱 : " + eachpc.getName() + " / 等級 : " + eachpc.getLevel() + "\n");
						countGM++;
						continue;
					}
					if (!eachpc.isPrivateShop() && !eachpc.isPrivateShop()) {
						if (eachpc.noPlayerCK) {
							noplayList.append(eachpc.getName() + ", ");
							nocountPlayer++;
							continue;
						} else {
							playList.append("名稱 : " + eachpc.getName() + " / 等級 : " + eachpc.getLevel() + "\n");
							countPlayer++;
							continue;
						}
					}
					if (eachpc.isPrivateShop() && eachpc.isPrivateShop()) {
						shopList.append(eachpc.getName() + ", ");
						countShop++;
					}
				}
			}

			if (gmList.length() > 0) {
				gm.sendPackets(new S_SystemMessage("-- 管理員 (" + countGM + "名)"));
				gm.sendPackets(new S_SystemMessage(gmList.toString()));
			}

			if (noplayList.length() > 0) {
				gm.sendPackets(new S_SystemMessage("-- 幻象 (" + nocountPlayer + "個)"));
				gm.sendPackets(new S_SystemMessage(noplayList.toString()));
			}

			if (playList.length() > 0) {
				gm.sendPackets(new S_SystemMessage("-- 玩家 (" + countPlayer + "名)"));
				gm.sendPackets(new S_SystemMessage(playList.toString()));
			}

			if (shopList.length() > 0) {
				gm.sendPackets(new S_SystemMessage("-- 個人商店 (" + countShop + "名)"));
				gm.sendPackets(new S_SystemMessage(shopList.toString()));
			}

		} catch (Exception e) {
		}

	public static void Event_System(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String code = tokenizer.nextToken();
			int i = Integer.parseInt(tokenizer.nextToken(), 10);

			if (EventSystemLoader.getInstance().getEventSystemSize() < i) {
				gm.sendPackets("請輸入正確的事件編號。");
				return;
			}
			if (code.equalsIgnoreCase("開始")) {
				EventSystemTimeController.getInstance().initScs(i);
				GeneralThreadPool.getInstance().execute(new EventSystemSpawner(i, EventSystemTimeController.FS_START));
			} else if (code.equalsIgnoreCase("結束")) {
				GeneralThreadPool.getInstance().execute(new EventSystemSpawner(i, EventSystemTimeController.FS_END));
			}
		} catch (Exception e) {
			EventSystemLoader.getInstance().getEventSystemInfoCheck(gm);
			gm.sendPackets(new S_SystemMessage(".事件 [開始/結束] 事件編號"));
		}
	}

	public static void MapSpawnReload(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String code = tokenizer.nextToken();
			int mapid = Integer.parseInt(tokenizer.nextToken(), 10);

			if (code.equalsIgnoreCase("一般")) {
				MJNormalSpawnLoader.getInstance().do_reload_map(mapid);
			} else if (code.equalsIgnoreCase("晝夜")) {
				MJDayAndNightSpawnLoader.getInstance().do_reload_map(mapid);
			}
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage("\f2[.生成地圖 [一般/晝夜] 地圖編號]"));
		}
	}

	private void Sealedoff(L1PcInstance L1PcInstance pc;
		pc, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String param1 = tok.nextToken();
			int off = Integer.parseInt(param1);
			if (off > 10 || off < 0) {
				pc.sendPackets("\f2一次最多可以申請 (10) 張。");
				return;
			}
			if (pc._create_password) {
				pc.sendPackets("\f2密碼註冊失敗時，(30) 秒後可以重新嘗試。\f3(更改密碼時將會被踢出)");
				return;
			}

			if (pc._seal_scroll) {
				pc.sendPackets("\f2密碼驗證失敗時，(30) 秒後可以重新嘗試。\f3(更改密碼時將會被踢出)");
				return;
			}

			if (pc.getAccount().getShopPassword() == 0) {
				pc._create_password = true;
				pc.sendPackets("\f2初次使用時，請輸入密碼。");
			} else {
				pc._seal_scroll = true;
				pc._seal_scroll_count = off;
				pc.sendPackets("\f230秒內請完成密碼驗證。");
			}
			pc.sendPackets(834);

			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						if (pc._seal_scroll) {
							pc._seal_scroll = false;
							pc.sendPackets("\f2時間已過，密碼認證失敗，請重新嘗試。");
						}
						if (pc._create_password) {
							pc._create_password = false;
							pc.sendPackets("\f2時間已過，密碼生成失敗，請重新嘗試。");
						}
						pc._seal_scroll_count = 0;
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}, 30000);
		} catch (Exception e) {
			pc.sendPackets("\f2.解封申請 (申請的張數)");
		}
	}

	public static void BuyLimitSystem(L1PcInstance gm, String param){
			try {
				StringTokenizer tokenizer = new StringTokenizer(param);
				String first_code = tokenizer.nextToken();
				if (first_code.equalsIgnoreCase("重新加載")) {
					BuyLimitSystem.getInstance().reload();
					gm.sendPackets("\f2正在重新加載限購物品列表。");
				} else if (first_code.equalsIgnoreCase("重置")) {
					String second_code = tokenizer.nextToken();
					if (second_code.equalsIgnoreCase("帳號")) {
						BuyLimitSystemAccountTable.reload();
						gm.sendPackets("\f2正在重置限購物品的所有購買記錄。（帳號）");
					} else if (second_code.equalsIgnoreCase("角色")) {
						BuyLimitSystemCharacterTable.reload();
						gm.sendPackets("\f2正在重置限購物品的所有購買記錄。（角色）");
					} else {
						gm.sendPackets("\f2.限購重置 帳號/角色");
					}
				}
			} catch (Exception e) {
				gm.sendPackets("\f2[.限購 [重新加載/重置（帳號/角色）]");
			}
		}

	private void Dragon_raid(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			int commandnum = Integer.parseInt(st.nextToken(), 10);
			List<DragonRaidSystemInfo> list = DragonRaidSystemLoader.getList();
			if (commandnum == 0) {
				DragonRaidSystemLoader.reload();
				gm.sendPackets("正在重新加載龍突襲信息。");
			}
			if (list.size() > 0) {
				for (DragonRaidSystemInfo b : list) {
					try {
						switch (commandnum) {
							case 1:
								if (b.getBossNum() == 1) {
									RaidOfAntaras anta = new RaidOfAntaras();
									L1Object teleporter = L1World.getInstance().findNpc(73201221);
									if (teleporter == null) {
										anta.Start();
										gm.sendPackets("\f3安塔瑞斯突襲強制開始");
									} else {
										L1NpcInstance npc = (L1NpcInstance) teleporter;
										npc.getMap().setPassable(npc.getX(), npc.getY(), true);
										npc.deleteMe();
										gm.sendPackets("\f3安塔瑞斯突襲強制結束");
									}
								}
								break;
							case 2:
								if (b.getBossNum() == 2) {
									RaidOfFafurion fafu = new RaidOfFafurion();
									L1Object teleporter = L1World.getInstance().findNpc(73201222);
									if (teleporter == null) {
										fafu.Start();
										gm.sendPackets("\f3法利昂突襲強制開始");
									} else {
										L1NpcInstance npc = (L1NpcInstance) teleporter;
										npc.getMap().setPassable(npc.getX(), npc.getY(), true);
										npc.deleteMe();
										gm.sendPackets("\f3法利昂突襲強制結束");
									}
								}
								break;
							case 3:
								if (b.getBossNum() == 3) {
									RaidOfLindvior lind = new RaidOfLindvior();
									L1Object teleporter = L1World.getInstance().findNpc(73201223);
									if (teleporter == null) {
										lind.Start();
										gm.sendPackets("\f3林德拜爾突襲強制開始");
									} else {
										L1NpcInstance npc = (L1NpcInstance) teleporter;
										npc.getMap().setPassable(npc.getX(), npc.getY(), true);
										npc.deleteMe();
										gm.sendPackets("\f3林德拜爾突襲強制結束");
									}
								}
								break;
							case 4:
								if (b.getBossNum() == 4) {
									RaidOfValakas vala = new RaidOfValakas();
									L1Object teleporter = L1World.getInstance().findNpc(73201224);
									if (teleporter == null) {
										vala.Start();
										gm.sendPackets("\f3巴拉卡斯突襲強制開始");
									} else {
										L1NpcInstance npc = (L1NpcInstance) teleporter;
										npc.getMap().setPassable(npc.getX(), npc.getY(), true);
										npc.deleteMe();
										gm.sendPackets("\f3巴拉卡斯突襲強制結束");
									}
								}
								break;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			gm.sendPackets("\f2[.龍襲擊 0~4 (0=重載, 1=安塔瑞斯, 2=法利昂, 3=林德拜爾, 4=巴拉卡斯)]");
		}
	}
	public static void addBlessPoint(L1PcInstance gm, String param){
			try {
				StringTokenizer st = new StringTokenizer(param);
				String name = st.nextToken();
				int point = Integer.parseInt(st.nextToken());
				L1PcInstance pc = L1World.getInstance().getPlayer(name);
				if (pc != null) {
					pc.getAccount().addGmBlessOfAinBonusPoint(point);
					SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
					pc.sendPackets(String.format("艾因哈薩德點數 %,d 點已處理。", point));
				} else {
					gm.sendPackets(String.format("%s 玩家當前在世界上不存在。", name));
				}
			} catch (Exception e) {
				gm.sendPackets("\f2[.艾因禮物 角色名 點數 (回收為 -)]");
			}
		}

	public static void addBlessCard(L1PcInstance gm, String param){
			try {
				StringTokenizer st = new StringTokenizer(param);
				String name = st.nextToken();
				int point = Integer.parseInt(st.nextToken());
				L1PcInstance pc = L1World.getInstance().getPlayer(name);
				AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
				int originstat = 0;
				originstat = Info.get_total_stat();
				if (pc != null) {
					if (originstat + point >= 180) {
						gm.sendPackets(String.format("無法向 %s 玩家支付該數量的點數。", name));
						gm.sendPackets(String.format("可支付的點數: %s", 180 - Info.get_total_stat()));
						return;
					} else {
						SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
						SC_EINHASAD_POINT_ENCHANT_START_ACK.send_enchant_start(pc, Info.get_cur_enchant_level());

						Info.set_cur_enchant_level(originstat + point);
						Info.add_total_stat(originstat + point);

						SC_EINHASAD_POINT_ENCHANT_STAT_ACK.send_point_enchant_stat(pc, point, 0, Info.get_cur_enchant_level(), Info.get_total_stat());

						AinhasadSpecialStatLoader.getInstance().updateSpecialStat(pc);
						pc.sendPackets(String.format("艾因哈薩德點數 %,d 點已處理。", point));
					}
				} else {
					gm.sendPackets(String.format("%s 玩家當前在世界上不存在。", name));
				}
			} catch (Exception e) {
				gm.sendPackets("\f2[.艾因卡 角色名 點數 (回收為 -)]");
			}
		}

	public static void SpellAskCommands(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String name = tokenizer.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(name);

			if (target == null) {
				gm.sendPackets("找不到角色信息。");
				return;
			}

			if (target == gm) {
				gm.sendPackets("無法對自己使用。");
				return;
			}

			if (target != null)
				gm.sendPackets(S_SpellCheck.get(target));
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage(".技能檢查 [角色名]"), true);
		}
	}

	private static boolean additem(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr, boolean identi) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(identi);
			item.setEnchantLevel(EnchantLevel);
			item.setAttrEnchantLevel(attr);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item, Bless);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			} else { // 無法獲取的情況下，不取消將其掉落在地面的處理（防止作弊）
				pc.sendPackets(new S_ServerMessage(82));
				// 由於重量不足或背包已滿，無法再攜帶更多物品。
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName())); //
			return true;
		} else {
			return false;
		}
	}

	private void blessScrollGive(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String cmd_type = st.nextToken();

			if (cmd_type.equalsIgnoreCase("確認")) {
				String target = st.nextToken();

				L1PcInstance tr = L1World.getInstance().getPlayer(target);
				if(tr != null) {
					for(L1ItemInstance item : tr.getInventory().getItems()) {
						if(item.getItem().getType2() == 0)
							continue;

						pc.sendPackets(new S_SystemMessage("[[ 物品 : " + item.getName() + " / 物件ID : " + item.getId() + "]]"), true);
					}
				} else {
					pc.sendPackets(new S_SystemMessage("不存在的用戶。"), true);
				}
			} else if (cmd_type.equalsIgnoreCase("賦予")) {
				String target = st.nextToken();
				int objid = Integer.valueOf(st.nextToken());
				int type = Integer.valueOf(st.nextToken());
				int value = Integer.valueOf(st.nextToken());

				L1PcInstance tr = L1World.getInstance().getPlayer(target);
				if(tr != null) {
					L1ItemInstance item = tr.getInventory().findItemObjId(objid);

					if(item != null) {
						if (item.isEquipped()) {
							tr.getEquipSlot().remove(item);
							item.setBlessType(type);
							item.setBlessTypeValue(value);
							item.setBless(0);
							tr.getEquipSlot().set(item);
						} else {
							item.setBlessType(type);
							item.setBlessTypeValue(value);
							item.setBless(0);
						}

						tr.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
						tr.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
						tr.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);

						tr.sendPackets(new S_SkillSound(pc.getId(), 9268));
						tr.sendPackets(new S_SystemMessage(item.getLogName() + " 被祝福的氣息所覆蓋。"));
						pc.sendPackets(new S_SystemMessage(target + " 的 " + item.getName() + " 獲得了祝福效果 " + type + " 號。 [數值: " + value + "]"), true);
					}
				}
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".祝福賦予 [確認/賦予]"), true);
			pc.sendPackets(new S_SystemMessage(".祝福賦予 [確認] [目標用戶名稱]"), true);
			pc.sendPackets(new S_SystemMessage(".祝福賦予 [賦予] [目標用戶名稱] [目標物品物件ID] [祝福編號] [數值]"), true);
		}
	}

	public void AccountPause(L1PcInstance pc, String param){
			try {
				StringTokenizer st = new StringTokenizer(param);
				String name = st.nextToken();
				int pause_time = Integer.parseInt(st.nextToken());
				String reason = st.nextToken();
				if (name == null || name.equalsIgnoreCase(""))
					throw new Exception("");

				L1PcInstance target = L1World.getInstance().getPlayer(name);
				if (target == null) {
					target = CharacterTable.getInstance().restoreCharacter(name);
				}

				if (target != null) { // 封禁帳號
					final GameClient clnt = target.getNetConnection();
					updateAccountPauseTime(target.getAccountName(), pause_time, reason);
					pc.sendPackets(String.format("已因 [%s] 理由封禁 %s 的帳號 %d 小時。", target.getName(), pause_time, reason));
					target.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3目前您的帳號已被封禁。"));
					target.sendPackets("\f3目前您的帳號已被封禁。");
					target.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
					// target.sendPackets(new S_Disconnect());

					if (target.getOnlineStatus() == 1) {
						target.sendPackets(new S_Disconnect());
					}

					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							if (clnt != null && clnt.isConnected()) {
								try {
									clnt.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							return null;
						}
					}, 1000L);
				} else {
					pc.sendPackets(String.format("%s 是不存在的角色。", name));
				}
			} catch (Exception e) {
				pc.sendPackets(".帳號停用 [角色名] [停用時間(1=1小時)][解除停用時用 - 處理] [封禁理由]");
			}
		}

	public void updateAccountPauseTime(String account_name, int time, String reason) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			Account account = Account.load(account_name);
			account.updateLastLogOut();
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET account_pause=?, account_pause_reason=?  WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			Timestamp pause_time = new Timestamp(System.currentTimeMillis() + ((3600 * 1000) * time));
			pstm.setTimestamp(1, pause_time);
			pstm.setString(2, reason);
			pstm.setString(3, account_name);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void doll_test(L1PcInstance gm, String param){
			// TODO 自動生成的方法存根
			try {
				StringTokenizer st = new StringTokenizer(param);
				String name = st.nextToken();
				int itemid = Integer.parseInt(st.nextToken());
				int count = Integer.parseInt(st.nextToken());
				int doll_level = Integer.parseInt(st.nextToken());
				int doll_val = Integer.parseInt(st.nextToken());
				L1PcInstance target = L1World.getInstance().getPlayer(name);
				if (target == null) {
					gm.sendPackets(String.format("\f2找不到 %s 。", name));
					return;
				}
				L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
				item.setCount(count);
				item.set_Doll_Bonus_Level(doll_level);
				item.set_Doll_Bonus_Value(doll_val);

				if (item.getItem().getUseType() != 73) {
					gm.sendPackets("\f2只有娃娃類型的物品可以贈送。請使用 (.物品) 命令來贈送其他物品。");
					return;
				}
				if (item != null) {
					if (target.getInventory().checkAddItem_doll(item, count, doll_level, doll_val) == L1Inventory.OK) {
						target.getInventory().storeItem(item, true);
					}
				}
				target.sendPackets(String.format("\f2[物品名稱: %s] [數量: %d]個獲得，請確認您的背包!", item.getName(), count));
				gm.sendPackets(String.format("\f2[角色名稱: %s] [物品名稱: %s] [數量: %d] [等級: %d] [效果: %d] 發放完成!", target.getName(), item.getName(), count, doll_level, doll_val));
			} catch (Exception exception) {
				gm.sendPackets("\f2.潛能 [角色名稱] [itemid(娃娃)] [數量] [等級:1~4] [效果:1~143]");
				gm.sendPackets("\f2[潛能等級]" + "潛能存在1~4等級。" + "[潛能效果]" + "效果存在1~143。" + "請在天堂官網搜索潛能，查看普通~傳說的說明。" + "例如，如果要在普通等級上附加遠距離致命一擊 +1 的選項，"
						+ ".潛能 梅蒂斯(角色名稱) 746(物品編號) 1(數量) 1(等級) 2(效果)" + "這樣發放的話會附加1級遠距離致命一擊 +1。" + "(134/135不存在(排除)");
			}
		}

	private void macroSetting(L1PcInstance pc, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String setting = tok.nextToken();

			if (setting.equalsIgnoreCase("設定")) {
				StringBuilder str = new StringBuilder();
				while (tok.hasMoreTokens()) {
					str.append(tok.nextToken() + " ");
				}

				String ment = str.toString();
				if (ment.length() > 1) {
					pc.addMacroList(ment);
					pc.sendPackets("已將訊息添加到宏中。");
					pc.sendPackets(" - [" + ment + "]");
				} else {
					pc.sendPackets(".宏 [設定/開始/停止/刪除/確認] [設定時要添加的訊息] [刪除時要刪除的順序號]，請輸入。");
				}
			} else if (setting.equalsIgnoreCase("確認")) {
				pc.getMacroListIdentify();
			} else if (setting.equalsIgnoreCase("刪除")) {

				String index = tok.nextToken();
				if (index.equalsIgnoreCase("全部")) {
					pc.getMacroList().clear();
					pc.sendPackets("所有的宏訊息已經被刪除。");
				} else {
					if (isStringDouble(index)) {
						int real_index = Integer.valueOf(index);
						if (real_index > pc.getMacroList().size()) {
							pc.sendPackets("指定順序號的宏不存在。");
							return;
						}
						pc.getMacroList().remove(real_index);
						pc.sendPackets("[" + real_index + "號] 宏訊息已被刪除。");
					} else {
						pc.sendPackets("刪除順序號必須是數字。");
					}
				}

			} else if (setting.equalsIgnoreCase("開始")) {
				if (pc.getMacroList().size() <= 0) {
					pc.sendPackets("輸入的宏不存在。");
					return;
				}

				if (pc.isMacroTimerStart()) {
					pc.sendPackets("宏已經在執行中。");
					return;
				}

				pc.startMacroTimer();
				pc.sendPackets("宏已開始。");
			} else if (setting.equalsIgnoreCase("停止")) {
				if (!pc.isMacroTimerStart()) {
					pc.sendPackets("當前沒有正在執行的宏。");
					return;
				}

				pc.stopMacroTimer();
				pc.sendPackets("宏已停止。");
			} else if (setting.equalsIgnoreCase("全部停止")) {
				for (L1PcInstance tr : L1World.getInstance().getAllPlayers()) {
					if (tr == null || tr.noPlayerCK)
						continue;


					tr.stopMacroTimer();
				}
//				pc.sendPackets("已停止當前所有用戶的宏。");
			L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\f3所有正在運行的用戶的宏已被停止。"));
			} else {
			pc.sendPackets("無效的請求。");
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".宏 [設定/開始/停止/全部停止/刪除/確認] [設定時添加的註釋] [刪除時要刪除的序號] 請輸入。"));
		}
	}

	private boolean isStringDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void insertCashInfo(L1PcInstance pc, String param){
			try {
				StringTokenizer st = new StringTokenizer(param);
				int charge_count = Integer.parseInt(st.nextToken());

				if (pc == null)
					return;

				if (charge_count < 10000) {
					pc.sendPackets("最低充值金額為10000韓元。");
					return;
				}

				if (charge_count > 500000) {
					pc.sendPackets("最高充值金額為500000韓元。");
					return;
				}

				if (getDepositInfo(pc) == 0) {
					pc.sendPackets("已經有待處理的請求。");
					return;
				}

				String current_date = MJString.get_current_datetime();
				MJNCoinDepositInfo dInfo = MJNCoinDepositInfo.newInstance()
						.set_deposit_id(MJNCoinIdFactory.DEPOSIT.next_id()).set_character_object_id(pc.getId())
						.set_character_name(pc.getName()).set_account_name(pc.getAccountName())
						.set_deposit_name(pc.getAccountName()).set_ncoin_value(Integer.valueOf(charge_count))
						.set_generate_date(current_date).set_is_deposit(0);

				MJNCoinDepositInfo.do_store(dInfo);
				do_write_letter_command(pc, MJNCoinSettings.DEPOSIT_LETTER_ID);

				String subject = String.format("[充值申請] %s", pc.getName());
				do_write_letter_togm(current_date, subject, dInfo.toString());
			} catch (Exception e) {
				pc.sendPackets(".Ncoin充值 [金額]");
			}
		}
	private void do_write_letter_command(L1PcInstance pc, final int notify_id) {
		final MJObjectWrapper<String> subject = new MJObjectWrapper<String>();
		final MJObjectWrapper<String> content = new MJObjectWrapper<String>();
		Selector.exec("select * from letter_command where id=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, notify_id);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					subject.value = rs.getString("subject");
					content.value = rs.getString("content");
				} else {
					subject.value = MJString.EmptyString;
					content.value = MJString.EmptyString;
				}
			}
		});
		if (MJString.isNullOrEmpty(subject.value) && MJString.isNullOrEmpty(content.value)) {
			try {
				throw new Exception(String.format("找不到命令信。ID：%d\r\n堆棧追蹤", notify_id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		String current_date = MJString.get_current_datetime();
		do_write_letter(pc.getName(), current_date, subject.value, content.value);
	}

	private void do_write_letter_togm(String generate_date, String subject, String content) {
			do_write_letter("梅蒂斯", generate_date, subject, content);
	}

	private void do_write_letter(String receiver, String generate_date, String subject, String content) {
			int id = LetterTable.getInstance().writeLetter(949, generate_date, "梅蒂斯", receiver, 0, subject, content);
		L1PcInstance pc = L1World.getInstance().getPlayer(receiver);
		if (pc != null) {
			pc.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_RECEIVE, "梅蒂斯",
					subject));
			// pc.sendPackets(new S_LetterList(pc, 0, 20));
			pc.send_effect(1091);
			pc.sendPackets(428);
		}
	}

	private int getDepositInfo(L1PcInstance pc) {
		int check = -1;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ncoin_trade_deposit WHERE account_name=?");
			pstm.setString(1, pc.getAccount().getName());
			rs = pstm.executeQuery();
			while (rs.next()) {
				check = rs.getInt("is_deposit");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}

		return check;
	}

	private void customQuestCmd(L1PcInstance pc, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String type = tokenizer.nextToken();
			String name = tokenizer.nextToken();
			String quest_id = tokenizer.nextToken();

			L1PcInstance target = L1World.getInstance().getPlayer(name);

			if (target == null) {
				pc.sendPackets("[" + name + "] 不是在線用戶。");
				return;
			}

			if (type.equalsIgnoreCase("完成")) {
				if (quest_id.equalsIgnoreCase("全部")) {
					Set<Integer> keys = target.getCustomQuestList().keySet();
					int questid;
					CustomQuestUser cqu = null;
					CustomQuest cq = null;
					for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
						questid = iterator.next();
						cqu = target.getCustomQuestList().get(questid);
						if (cqu != null) {
							cq = ServerCustomQuestTable.getInstance().getCustomQuest(questid);

							if (cq.getQuestPerformType() == eCustomQuestPerformType.KILL_NPC) {

								cqu.setSuccessCount(cq.getSuccessCount());
								cqu.setQuestState(2);
							} else if (cq.getQuestPerformType() == eCustomQuestPerformType.COLLECT_ITEM) {

								L1ItemInstance collectItem = pc.getInventory().findItemId(cq.getCollectItemId());
								if (collectItem == null) {
									target.getInventory().storeItem(cq.getCollectItemId(), cq.getSuccessCount());
								} else {
									int gap_count = cq.getSuccessCount() - collectItem.getCount();
									target.getInventory().storeItem(cq.getCollectItemId(), gap_count);
								}
							}
						}
					}
					target.sendPackets("管理員已將所有任務標記為完成。");
					pc.sendPackets("[" + name + "] 您的所有任務記錄已被刪除。");
				} else {
					// 檢查輸入的字符是否是數字
					if (CommonUtil.isNumber(quest_id)) {
						CustomQuestUser cqu = target.getCustomQuestList().get((Object) Integer.valueOf(quest_id));
						CustomQuest cq = ServerCustomQuestTable.getInstance().getCustomQuest(Integer.valueOf(quest_id));

						if (cqu != null) {
							if (cq != null) {
								if (cq.getQuestPerformType() == eCustomQuestPerformType.KILL_NPC) {

									cqu.setSuccessCount(cq.getSuccessCount());
									cqu.setQuestState(2);
								} else if (cq.getQuestPerformType() == eCustomQuestPerformType.COLLECT_ITEM) {

									L1ItemInstance collectItem = pc.getInventory().findItemId(cq.getCollectItemId());
									if (collectItem == null) {
										target.getInventory().storeItem(cq.getCollectItemId(), cq.getSuccessCount());
									} else {
										int gap_count = cq.getSuccessCount() - collectItem.getCount();
										target.getInventory().storeItem(cq.getCollectItemId(), gap_count);
									}
								}
								target.sendPackets("管理員已將 " + cq.getQuestName() + " 任務標記為完成。");
								pc.sendPackets("[" + name + "] 您的 " + cq.getQuestName() + " 任務已標記為完成。");
							} else {
								pc.sendPackets("伺服器中不存在這個任務請求。");
							}
						} else {
							pc.sendPackets("您試圖完成一個尚未獲取的任務。");
						}

					} else {
						pc.sendPackets("無效的請求。");
					}
				}

			} else if (type.equalsIgnoreCase("刪除")) {
				if (quest_id.equalsIgnoreCase("全部")) {
					target.getCustomQuestList().clear();
					target.sendPackets("管理員已刪除所有任務記錄。");
					pc.sendPackets("[" + name + "] 您的所有任務記錄已被刪除。");
				} else {
					if (CommonUtil.isNumber(quest_id)) {
						CustomQuest cq = ServerCustomQuestTable.getInstance().getCustomQuest(Integer.valueOf(quest_id));

						target.getCustomQuestList().remove((Object) Integer.valueOf(quest_id));
						target.sendPackets("管理員已刪除 " + cq.getQuestName() + " 任務。");
						pc.sendPackets("[" + name + "] 您的 " + cq.getQuestName() + " 任務已被刪除。");
					} else {
						pc.sendPackets("無效的請求。");
					}
				}
			}
		} catch (Exception e) {
			pc.sendPackets(new S_ChatPacket(pc, ".任務 [完成/刪除] [對象名字] [任務編號或全部]"));
		}
	}

}

	private void cleanAll(L1PcInstance gm) {

	}}

	
