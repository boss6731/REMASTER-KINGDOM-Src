package l1j.server.server;

import static l1j.server.server.model.skill.L1SkillId.HUNTER_BLESS;

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

    public static boolean 製作檢查;

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
            _log.log(Level.SEVERE, "error gm command", e);
        }
        return false;

    }

    private boolean executeDatabaseCommand(L1PcInstance pc, String name, String arg) {
        try {
            // 從命令列表中獲取指定的命令
            L1Command command = L1Commands.get(name);
            if (command == null) {
                return false; // 如果命令不存在，返回 false
            }

            // 檢查玩家的權限級別是否足夠執行該命令
            if (pc.getAccessLevel() < command.getLevel()) {
                pc.sendPackets(new S_ServerMessage(74, "命令 " + name)); // 向玩家發送消息表示權限不足
                return true; // 返回 true，表示命令處理成功，但未執行
            }

            // 獲取命令執行器的類並創建實例
            Class<?> cls = Class.forName(complementClassName(command.getExecutorClassName()));
            L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod("getInstance").invoke(null);
            // 執行命令
            exe.execute(pc, name, arg);

            // 儲存檔案日誌
            LoggerInstance.getInstance().addCommand(pc.getName() + ": " + name + " " + arg);
            return true; // 返回 true，表示命令執行成功
        } catch (Exception e) {
            // 捕獲並記錄異常
            _log.log(Level.SEVERE, "error gm command", e);
        }
        return false; // 返回 false，表示命令執行失敗
    }

    public void handleCommandsWithoutPermission(L1PcInstance gm, String cmdLine) {
        // 檢查 GM 是否有網絡連接和帳號，且權限級別是否為 5048
        if (gm.getNetConnection() == null || gm.getNetConnection().getAccount() == null || gm.getNetConnection().getAccount().getAccessLevel() != 5048) {
            return; // 如果不符合條件，則返回，終止命令處理
        }

        // 使用 StringTokenizer 將命令行分解為標記
        StringTokenizer token = new StringTokenizer(cmdLine);
        // 提取命令部分（第一個空格之前）
        String cmd = token.nextToken();
        String param = "";
        // 將剩餘的標記拼接成參數部分
        while (token.hasMoreTokens()) {
            param = new StringBuilder(param).append(token.nextToken()).append(' ').toString();
        }
        param = param.trim(); // 去除參數末尾的空格

        // 執行數據庫中的無需權限的命令
        executeDatabaseCommandWithoutPermission(gm, cmd, param);
    }

    private static void test_lfc(L1PcInstance gm, String param) {
        // 使用 StringTokenizer 將參數分解為標記
        StringTokenizer token = new StringTokenizer(param);
        // 提取第一個標記作為玩家名稱
        String name = token.nextToken();
        // 在世界中查找該玩家
        L1PcInstance pc = L1World.getInstance().findpc(name);
        // 如果找不到該玩家，向 GM 發送消息並返回
        if (pc == null) {
            gm.sendPackets(String.format("%s 玩家找不到.", name)); // "%s 玩家找不到。"
            return;
        }

        // 創建一個新的 L1BoardPost 對象，並向玩家發送對戰申請
        L1BoardPost bp = L1BoardPost.createLfc(name, "-", String.format("3 %s", gm.getName()));
        // 註冊對戰
        MJLFCCreator.registLfc(gm, 3);
        // 向 GM 發送申請對戰的消息
        gm.sendPackets(String.format("已向 %s 玩家申請對戰.", name)); // "已向 %s 玩家申請對戰。"

        // 向目標玩家發送對戰申請，並顯示調查窗口
        pc.sendPackets(MJSurveySystemLoader.getInstance().registerSurvey(
                String.format("%s 玩家已申請對戰.", gm.getName()), // "%s 玩家已申請對戰。"
                bp.getId() + 1000,
                MJSurveyFactory.createLFCSurvey(),
                30 * 1000
        ));
    }

    public void handleCommands(L1PcInstance gm, String cmdLine) {
        // 使用 StringTokenizer 將命令行分解為標記
        StringTokenizer token = new StringTokenizer(cmdLine);
        // 提取命令部分（第一個空格之前）
        String cmd = "";
        if (token.hasMoreTokens())
            cmd = token.nextToken();
        else
            cmd = cmdLine;

        String param = "";
        // 將剩餘的標記拼接成參數部分
        while (token.hasMoreTokens()) {
            param = new StringBuilder(param).append(token.nextToken()).append(' ').toString();
        }
        param = param.trim(); // 去除參數末尾的空格

        // 執行數據庫中的命令
        if (executeDatabaseCommand(gm, cmd, param)) {
            // 如果命令執行成功且命令不是 "."
            if (!cmd.equalsIgnoreCase(".")) {
                // 保存命令記錄
                _lastCommands.put(gm.getId(), cmdLine);
            }
            return; // 返回，結束方法執行
        }

        // 如果 GM 的權限級別小於設定的 GMCODE
        if (gm.getAccessLevel() < Config.ServerAdSetting.GMCODE) {
            // 發送消息通知 GM 該命令不可用
            gm.sendPackets(new S_ServerMessage(74, "命令 " + cmd)); // "命令 " + cmd
            return; // 返回，結束方法執行
        }
    }

    /** 文件日誌存儲 **/
LoggerInstance.getInstance().addCommand(gm.getName() + ": " + cmd + " " + param);
    _gm = gm;

if (MJNCoinCommandComposite.DEFAULT.execute(gm, cmd, param))
            return;

if (MJCommandService.gmService().execute(gm, cmd, param)) {
        return;
    }

switch (cmd) {
        case "死亡競賽": { // 死亡競賽
            DeathMatch open = new DeathMatch(13005);
            open.Start();
            System.out.println("死亡競賽測試開始"); // "死亡競賽測試開始"
            break;
        }
        case "開始": { // 開始
            InfinityBattle.getInstance().Start();
            break;
        }
        case "遊戲開始": { // 遊戲開始
            InfinityBattle.getInstance().addTeamMembers(gm);
            break;
        }
        case "遊戲結束": { // 遊戲結束
            InfinityBattle.getInstance().setReady(false);
            break;
        }
        case "幫助": { // 幫助
            showHelp(gm);
            break;
        }
        case "祝福賦予": { // 祝福賦予
            blessScrollGive(gm, param);
            break;
        }
        case "偵測黑客": { // 偵測黑客
            if (!Config.LogStatus.WALKPOSITIONCHECK_LOG) {
                Config.LogStatus.WALKPOSITIONCHECK_LOG = true;
                gm.sendPackets(new S_SystemMessage("[偵測黑客] 開始偵測黑客，請檢查CMD窗口（發生偏差）")); // "開始偵測黑客，請檢查CMD窗口（發生偏差）"
            } else {
                Config.LogStatus.WALKPOSITIONCHECK_LOG = false;
                gm.sendPackets(new S_SystemMessage("[偵測黑客] 停止偵測黑客。")); // "停止偵測黑客"
            }
            break;
        }
        case "宏設定": { // 宏設定
            macroSetting(gm, param);
            break;
        }
        case "潛力測試": { // 潛力測試
            doll_test(gm, param);
            break;
        }
        case "帳號暫停": { // 帳號暫停
            AccountPause(gm, param);
            break;
        }
        case "螞蟻洞": { // 螞蟻洞
            QueenAntController test = new QueenAntController();
            test.Start();
            break;
        }
        case "發送測試數據包": { // 發送測試數據包
            gm.sendPackets(new S_TestPacket(1));
            break;
        }
        case "增加祝福點": { // 增加祝福點
            addBlessPoint(gm, param);
            break;
        }
        case "增加祝福卡": { // 增加祝福卡
            addBlessCard(gm, param);
            break;
        }
        case "超級重置": { // 超級重置
            superReset(gm, param);
            break;
        }
        default:
            gm.sendPackets(new S_SystemMessage("未知的GM指令: " + cmd));
            break;
    }
        // 這裡應該有相應的代碼來處理超級重置命令
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
        // 艾因哈薩克 更新後 XXX 取消註釋
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

			/*gm.sendPackets(new S_PacketBox(S_PacketBox.網咖愛好者, 1600), true);
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
//			gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\f3新產品已抵達補充服務倉庫."));
//			noti.set_add_state(warehouse != null && warehouse.getSize() > 0);
//			gm.sendPackets(noti, MJEProtoMessages.SC_GOODS_INVEN_NOTI);

//			SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
//			box.set_button_type(ButtonType.MB_OK);
//			box.set_icon_type(IconType.MB_ICONASTERISK);
//			box.set_message("瞬時連接量飽和。請再試一次.\n\n您可以透過連接器登入網路立即存取它.\n\n欲了解更多詳情，請瀏覽網站 請參閱“連接方法”. \n\n-梅蒂斯");
//			box.set_title("sadasd");
//			box.set_message_id(0);
//			gm.sendPackets(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt());
//			gm.setMoveDelayRate(70);
//			gm.setAttackDelayRate(0);
//			L1Cooking.eatCooking(gm, 천하장사버프, 1800);
//			gm.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, gm, 187, 6));
//			System.out.println(ShopBuyLimitInfo.getInstance().getCharacterList(268452565));
//			SC_CHANGE_TEAM_NOTI_PACKET noti = SC_CHANGE_TEAM_NOTI_PACKET.newInstance();
//			noti.set_object_id(gm.getId());
//			noti.set_object_team_id(1);// 나에게 표시되는 마크
//			gm.sendPackets(noti, MJEProtoMessages.SC_CHANGE_TEAM_NOTI_PACKET, false);

//			gm.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, 0));

//			gm.sendPackets(new S_Paralysis(7, true));

//			gm.set_dead_count(0);
//			gm.setMonsterkill(0);
//			gm.set_exp_count(0);
//			System.out.println(String.format("아이템 지급 횟수 : %d, 몬스터 킬수 : %d, 경험치 지급 횟수 : %d", gm.get_dead_count(), gm.getMonsterkill(), gm.get_exp_count()));
        //gm.sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, gm, 6, 5));

//			SC_SPEED_BONUS_NOTI.send(gm, gm.getId(), SC_SPEED_BONUS_NOTI.Bonus.eKind.MOVE_SPEED);
			/*SC_SPEED_BONUS_NOTI noti = SC_SPEED_BONUS_NOTI.newInstance();
			noti.set_objectnumber(gm.getId());

			Value val1 = Value.newInstance();
			val1.set_kind(SC_SPEED_BONUS_NOTI.Bonus.eKind.ATTACK_SPEED);
			val1.set_value(1000);*/

//			gm.상인찾기Objid = 0;
//			gm._seal_scroll_count = 0;
//			gm._x = 0;
			/*gm.setFourgear(true);
			ProtoOutputStream SC_FOURTH_GEAR_NOTI_stream = SC_FOURTH_GEAR_NOTI.Fourth_Gear(gm);

			gm.sendPackets(SC_FOURTH_GEAR_NOTI_stream);
			SC_FOURTH_GEAR_NOTI_stream.dispose();*/

//			gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, true));
//			gm.sendPackets(String.format("총 획득 개수 : %d", gm.insert_count));//테스트후 삭제
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

        // gm.sendPackets(String.format("기란성 기존 누적 세금 %d", MJCastleWarBusiness.getInstance().getPublicMoney(4)));
        // MJCastleWar war = MJCastleWarBusiness.getInstance().get(4);
        // int adena = war.getPublicMoney();
        // gm.getInventory().storeItem(40308, adena);
        // war.addPublicMoney(adena);
        // MJCastleWarBusiness.getInstance().updateCastle(4);
        // gm.sendPackets(String.format("기란성 현재 누적 세금 %d", MJCastleWarBusiness.getInstance().getPublicMoney(4)));

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

        // L1World.getInstance().broadcastPacketToAll(SC_MESSAGE_NOTI.newDropMessage(386, "로브를 성공 하였습니다."), true);

        // 하이퍼 스탯 이동 패킷 0번(프로토 연결시 클라자동)
        // SC_EINHASAD_POINT_ENCHANT_START_ACK.send(gm, 0);

        // 스텟창 : 맨끝은 스텟창에 리마인드 남은포인트 - 이 명령어
        // SC_EINHASAD_POINT_ENCHANT_STAT_ACK.send(gm, 10, 20, 5, 50);

        // 리마인드 포인트가 해당 인트값에 의해 줄어든다.->하이퍼 스텟을 찍을때 줄어든다.(스텟창인듯한데?)
        // SC_EINHASAD_POINT_STAT_INVEST_ACK.send(gm, 3, 3, 3);

        // 그 화면안에 카드뽑기 창에서 사용됨(스텟1,2,3/남은 포인트/아인하사드 포인트)
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
        // L1World.getInstance().broadcastPacketToAll(SC_MESSAGE_NOTI.newDropMessage(386, "로브를 성공 하였습니다."), true);
        /*
         * gm.sendPackets(new S_SkillSound(gm.getId(), 11731)); Broadcaster.broadcastPacket(gm, new S_SkillSound(gm.getId(), 11731));
         */
        // gm.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(gm, 0, false));
        // SC_NOTIFICATION_INFO_NOTI.onEventTick(gm, 3000L);
        // gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PHANTOM, false));
        // gm.getAccount().addBlessOfAin((100 * 10000), gm, "몬스터");
        // gm.sendPackets(new S_ACTION_UI(1000 / 10000)); //아마 아인버프 이펙트??
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

        // L1PcInstance pc = L1World.getInstance().getPlayer("호");
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
         * SC_REVENGE_INFO_NOTI noti = SC_REVENGE_INFO_NOTI.newInstance(); RevengeInfoT rInfo = RevengeInfoT.newInstance(); rInfo.set_action_count(3); // 도발횟수 rInfo.set_action_duration(3 + 3600); // 추적 남은 시간 rInfo.set_action_remain_count((3 + 2)); // 추적 남은 횟수
         * rInfo.set_action_result(MJRnd.isBoolean() ? eResult.LOSE : eResult.WIN); // 승패 rInfo.set_action_timestamp((int)(System.currentTimeMillis() / 1000)); // 이전에 액션을 취한시간.(초) rInfo.set_action_type(eAction.PURSUIT); // eAction.PURSUIT 추적, eAction.TAUNT 도발
         * rInfo.set_active(true); // 접속 여부 rInfo.set_crimescene_server_no(2); rInfo.set_game_class(MJRnd.next(8)); // 클래스 rInfo.set_pledge_id(1); // 혈맹 아이디 rInfo.set_pledge_name("ccc"); // 혈맹이름 rInfo.set_register_timestamp((int)(System.currentTimeMillis() /
         * 1000) - 3600); // 발생시간(초) rInfo.set_server_no(1); rInfo.set_unregister_duration(1); // 삭제예정시간(초) rInfo.set_user_name("asd"); // 캐릭터이름 rInfo.set_user_uid(50); // 아이디 noti.set_target_info(rInfo); gm.sendPackets(noti,
         * MJEProtoMessages.SC_REVENGE_INFO_NOTI);
         */

        /*
         * SC_WHOUSER_NOTI_PACKET noti = SC_WHOUSER_NOTI_PACKET.newInstance(); noti.set_currentusercount(L1World.getInstance().getAllPlayersSize()); for(L1PcInstance pc : L1World.getInstance().getAllPlayers()) { SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO uInfo =
         * SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO.newInstance(); SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO aInfo = SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO.newInstance(); aInfo.set_accountname(pc.getAccountName()); aInfo.set_ip("asd"); aInfo.set_ipkind(0);
         * aInfo.set_location(pc.getLongLocation()); aInfo.set_worldnumber(4); uInfo.set_accountinfo(aInfo); uInfo.set_alignstr("asdaa"); uInfo.set_pledge("norm"); uInfo.set_serverno(0); uInfo.set_title("aaa"); uInfo.set_username(pc.getName());
         * noti.add_whouserinfo(uInfo); } gm.sendPackets(noti, MJEProtoMessages.SC_WHOUSER_NOTI_PACKET);
         */

        // gm.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON, 73, true));//무제한패킷
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
        // String.format("[아인하사드 정액제 남은 시간] %s\r\n정액제 유지 중 드래곤 보석을 사용해 아인 등급 상승이 가능합니다",
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
        // gm.sendPackets("무게 게이지가 82%를 넘어 공격을 할 수 없습니다.");
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
         * SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance(); box.set_button_type(ButtonType.MB_OK); box.set_icon_type(IconType.MB_ICONASTERISK); box. set_message("순간 접속량이 포화상태 입니다. 재시도 바랍니다.\n\n접속기에서 웹로그인시 바로 접속이 가능합니다.\n\n자세한건 홈페이지 '접속방법'을 참고바랍니다. \n\n-메티스"
         * ); box.set_title(Config.Login.GameServerName); box.set_message_id(0); gm.sendPackets(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt());
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
         * for(GameClient clnt : MJNSHandler.getClients()) { System.out.println(param); clnt.sendPacket(new S_CommonNews("스텟 초기화가 완료되었습니다.")); }
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
        // gm.sendPackets(new S_ServerMessage(403, "$5240 (1000)")); // %0를
        // gm.sendPackets(new S_ServerMessage(563)); // %0를
        /*
         * for(int i=0; i<256; ++i) { for(int j=0; j<256; ++j) { //byte[] buff = new byte[] {(byte)0x51, (byte)0x08, (byte)0x02, (byte)0x03, (byte)0x00, (byte)0x08, (byte)0x80, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}; byte[] buff = new byte[]
         * {(byte)0x51, (byte)0x0c, (byte)0x02, (byte)0x03, (byte)0x00, (byte)0x08, (byte)0x80, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}; buff[6] = (byte)j; buff[7] = (byte)i; MJEProtoMessages.existsProto(gm.getNetConnection(), buff); } }
         *
         *
         * SC_NOTIFICATION_MESSAGE noti = SC_NOTIFICATION_MESSAGE.newInstance(); noti.set_desc("asdasd"); noti.set_duration(5); noti.set_messageRGB(MJSimpleRgb.red()); noti.set_pos(display_position.screen_middle); noti.set_serverno(1); gm.sendPackets(noti,
         * MJEProtoMessages.SC_NOTIFICATION_MESSAGE);
         */
        /*
         * SC_CHAT_MESSAGE_NOTI_PACKET noti = SC_CHAT_MESSAGE_NOTI_PACKET.newInstance(); noti.set_time_t64(0L); noti.set_type(ChatType.CHAT_TRADE); noti.set_message("hello".getBytes()); noti.set_name(gm.isGm() ? "******".getBytes(MJEncoding.MS949) :
         * gm.getName().getBytes(MJEncoding.MS949)); int step = gm.getRankLevel(); if(step != 0){ noti.set_ranker_rating(step); } noti.set_is_server_keeper(true); gm.sendPackets(noti, MJEProtoMessages.SC_CHAT_MESSAGE_NOTI_PACKET);
         */
    }
    // gm.sendPackets("\\aA■ 혈맹레이드 랭킹이 갱신 되었습니다 ■");
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
    // MJEncoding.MS949));
    // gm.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 596, true));
    // executeitem(gm, param, param);
			break;
		switch (cmd) {
        case "N幣充值": { // N硬幣充值
            insertCashInfo(gm, param);
            break;
        }
        case "購買限制": { // 購買限制
            BuyLimitSystem(gm, param);
            break;
        }
        case "封印解除申請": { // 封印解除申請
            Sealedoff(gm, param);
            break;
        }
        case "重載出生地圖": { // 重載出生地圖
            MapSpawnReload(gm, param);
            break;
        }
        case "等待隊列": { // 使用等待隊列
            useWaitQueue(gm, param);
            break;
        }
        case "誰": { // 地圖上的玩家
            mapwho(gm, param);
            break;
        }
        case "通知聊天": { // 通知聊天
            NoticeChat(gm, param);
            break;
        }
        case "所有用戶傳送": { // 所有用戶跳轉
            allUserInterJump(gm, param);
            break;
        }
        case "跳轉": { // 跳轉
            InterJump(gm, param);
            break;
        }
        case "用戶傳送": { // 用戶跳轉
            UserInterJump(gm, param);
            break;
        }
// ...其他命令的處理...
    }
    //case "強力球":
    //	PowerBall(gm, param);
    //	break;
		case "啟動關閉": { // 開啟/關閉伺服器登錄
        try {
            if (Config.Login.SERVERSTANDBY) {
                Config.Login.SERVERSTANDBY = false;
                gm.sendPackets("伺服器登錄無法訪問."); // "伺服器登錄無法訪問。"
            } else {
                Config.Login.SERVERSTANDBY = true;
                gm.sendPackets("伺服器登錄可以訪問."); // "伺服器登錄可以訪問。"
            }
        } catch (Exception e) {
            gm.sendPackets(".開關 開/關"); // ".온오프 開/關"
        }
        break;
    }
case "白名單 IP": { // 白名單 IP
        try {
            if (MJString.isNullOrEmpty(param)) {
                throw new Exception();
            }
            MJWhiteIP.getInstance().put(param);
            MJNSDenialAddress.getInstance().delete_denials_address(param);
        } catch (Exception e) {
            gm.sendPackets(".白名單 [IP地址](Ex 127.0.0.1)"); // ".화이트아이피 [IP地址](例如 127.0.0.1)"
        }
        break;
    }
		case "註冊優惠券": { // 註冊優惠券
        優惠券登記(gm, param);
        break;
    }
		case "應用中心": { // 應用中心
        appcenter(gm, param);
        break;
    }
		case "檢查物品欄": { // 檢查物品欄
        inventoryAskCommands(gm, param);
        break;
    }
		case "重置概率": { // 重置概率
        initialize_user_private_probability(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
    }
		case "更新概率": { // 更新概率
        update_user_private_probability(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
    }
		case "顯示概率": { // 顯示概率
        show_user_private_probability(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
    }
		case "測試訊息": {
        // TODO: 實現系統訊息功能
        // gm.sendPackets(new S_SystemMessage());
        break;
    }
		case "優惠券碼": { // 獲取或者使用優惠券碼
        MJPaymentGmHandler.do_execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
    }
		case "充值碼": { // 充值碼
        MJPaymentUserHandler.do_execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
    }
		case "寵物管理": { // 寵物管理
        MJCompanionCommandExecutor.exec(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
    }
		case "兌換經驗值": { // 兌換經驗值
        do_cache_exp(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
    }
		case "對戰": { // 對戰
        new MJShiftBattleCommandExecutor().execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
    }
		case "伺服器遷移": {
        // 伺服器升級：當你需要升級現有的伺服器硬體或軟體時，需要將數據遷移到新的伺服器上，以確保服務不中斷。
        //伺服器擴展：當你的應用程序需要更多的資源或更高的性能時，需要將數據遷移到更強大的伺服器上，以滿足增加的需求。
        //伺服器故障：當伺服器突然DOWN或出現嚴重的問題時，需要將數據遷移到備用的伺服器上，以確保服務不中斷。
        //資料中心遷移：當你需要將數據中心遷移到新的地點時，需要將數據遷移到新的伺服器上，以確保服務不中斷。
        //雲端遷移：當你需要從傳統的數據中心遷移到雲端平臺時，需要將數據遷移到雲端伺服器上，以享受到雲端的優點。
        //維護和升級：當你需要進行維護或升級現有的伺服器時，需要將數據遷移到備用的伺服器上，以確保服務不中斷。
        //災難恢復：當發生災難性事件時，需要將數據遷移到備用的伺服器上，以快速恢復服務。
        if (Config.Login.UseShiftServer) {
            new MJShiftObjectCommandExecutor().execute(new MJCommandArgs().setOwner(gm).setParam(param));
        }
        break;
    }
		case "伺服器版本": { // 輸出伺服器版本
        try {
            SC_SERVER_VERSION_INFO info = (SC_SERVER_VERSION_INFO) SC_SERVER_VERSION_INFO.newInstance().copyInstance();
// 這裡是一些已被註釋掉的代碼，可能是用於從文件讀取版本信息
// byte[] bytes = MJFiles.readAllBytes("./data/version.dat");
// info.readFrom(ProtoInputStream.newInstance(bytes, 3, bytes.length));
// if (!info.isInitialized()) {
//     throw new IllegalArgumentException(String.format("fail initialized version data.(SC_SERVER_VERSION_INFO) %d", info.getInitializeBit()));
// }
            gm.sendPackets(info.toString()); // 發送版本信息給管理員
        } catch (Exception e) {
            gm.sendPackets("輸出版本信息失敗."); // 輸出版本信息失敗
        }
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

        // gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "밤시간 동안 무작위 텔레포트를 할
        // 수 없는 지역입니다."));
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
        case "龍襲擊": { // 龍襲擊
        Dragon_raid(gm, param);
        break;
        }
        case "伺服器傳輸": { // 伺服器傳輸
        do_shift_server(gm, param);
        break;
        }
        case "開啟競技場": { // 打開競技場
        StadiumManager.getInstance().open_stadium(gm, param);
        break;
        }
        case "關閉競技場": { // 關閉競技場
        StadiumManager.getInstance().quit_stadium(gm, param);
        break;
        }
        case "決鬥": { // 決鬥
        test_lfc(gm, param);
        break;
        }
        case "煉金概率": { // 執行煉金概率箱
        MJAlchemyProbabilityBox.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        }
        case "冶煉概率": { // 執行冶煉概率箱
        MJSmeltingProbabilityBox.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        }
        case "血盟信息": { // 搜索血盟信息
        searchclaner(gm, param);
        break;
        }
        case "主宰老闆": { // 統治之主
        DominanceBoss(gm, param);
        break;
        }
        case "角色信息": { // 檢查角色信息
        chainfo(gm, param);
        break;
        }
        case "自動樹": { // 自動樹
        gm.isAutoTreeple = true;
        break;
        }
        case "固定增益": { // 固定增益
        befixed(gm, param);
        break;
        case "刪除角色": { // 刪除角色
        standBy77(gm, param);
        break;
        }
        case "氣運祝福": { // 氣運祝福
        Blessleaf(gm, param);
        break;
        }
        case "製作成功": { // 製作成功
        CraftSuccess(gm, param);
        break;
        }
        case "武器強化成功": { // 武器強化成功
        EnchantWeaponSuccess(gm, param);
        break;
        }
        case "防具強化成功": { // 防具強化成功
        EnchantArmorSuccess(gm, param);
        break;
        }
        case "埃爾扎貝盒": { // 埃爾扎貝盒
        ErzabeBox(gm, param);
        break;
        }
        case "沙蟲盒": { // 沙蟲盒
        SandwormBox(gm, param);
        break;
        }
        case "檢查帳號": { // 檢查帳號
        AccountCheck(gm, param);
        break;
        }
        case "確認帳號": { // 確認帳號
        AccountCheck1(gm, param);
        break;
        }
        case "DEF操作": { // 進行DEF操作
        jakjak(gm);
        break;
        }
        case "裝甲操作": { // 進行裝甲操作
        jakjak2(gm);
        break;
        }
        case "CAB操作": { // 進行CAB操作
        jakjak3(gm);
        break;
        }
        case "惡魔事件": { // 進行惡魔操作
        dolldemon(gm);
        break;
        case "冰女事件": { // 進行冰女操作
        doice(gm);
        break;
        }
        case "墮落事件": { // 進行墮落操作
        dolltarak(gm);
        break;
        }
        case "死亡事件": { // 進行死亡操作
        dolldeath(gm);
        break;
        }
        case "符文石CAB": { // 進行符文石CAB操作
        Stone(gm);
        break;
        }
        case "符文石DIS": { // 進行符文石DIS操作
        Stone1(gm);
        break;
        }
        case "符文石DEF": { // 進行符文石DEF操作
        Stone2(gm);
        break;
        }
        case "符文石裝甲": { // 進行符文石裝甲操作
        Stone3(gm);
        break;
        }
        case "自定義任務": { // 自定義任務
        customQuestCmd(gm, param);
        break;
        }
        case "添加活動BOSS": { // 添加活動BOSS
        addEventBoss(gm, param);
        break;
        }
        case "顯示原型": { // 顯示原型
        showProto(gm, param);
        break;
        }
        case "地下城計時器": { // 地牢計時器
        DungeonTimeLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        }
        case "詛咒角色": { // 詛咒角色
        CurseCharacter(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        }
        case "重置角色詛咒": { // 重置角色詛咒
        CurseInitializeCharacter(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        case "強化系統": { // 強化系統
        MJItemEnchantSystemLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        }
        case "驗證碼系統": { // 驗證碼系統
        MJCaptchaLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        }
        case "設置角色狀態": { // 設置角色狀態
        setCharacterInstanceStatus(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        }
        case "戰鬥系統": { // 戰鬥系統
        MJCombatLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        }
        case "怪物圖鑑": { // 圖書系統
        BQSLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        }
        case "出席檢查": { // 出席檢查
        MJAttendanceLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        }
        case "查看首頁": { // 查看首頁
        gm.sendPackets(S_ShowCmd.getPlayMovieNoti("https://minamldh.wixsite.com/adam", -1));
        break;
        }
        case "設置顯示碼": { // 設置顯示碼
        setPresentationCode(gm, param);
        break;
        }
        case "顯示對象列表": { // 顯示對象列表
        showObjectList(gm, param);
        break;
        }
        case "日誌轉存": { // 日誌轉存
        MJProcessPlayer.dumpLog();
        break;
        }
        case "網絡設置": { // 網絡設置
        MJNetServerLoadManager.commands(gm, param);
        break;
        }
        case "設置保護模式": { // 設置保護模式
        setProtectionMode(gm, param);
        break;
        }
        case "設置網絡安全": { // 設置網絡安全
        MJNetSafeLoadManager.commands(gm, param);
        break;
        }
        case "檢查延遲": { // 檢查延遲
        ping(gm, param);
        break;
        case "殺戮排行榜": { // 殺戮排行榜
        MJKDALoadManager.commands(gm, param);
        break;
        }
        case "測試聊天": { // 測試聊天
        showChat(gm, param);
        break;
        }
        case "坐標": { // 顯示坐標
        좌표(gm);
        break;
        }
        case "顯示下一個原型": { // 顯示下一個原型
        showNextProto(gm, param);
        break;
        }
        case "命令列表": { // 顯示命令列表
        gm.sendPackets(S_ShowCmd.get("cmd."));
        break;
        }
        case "戰爭列表": { // 顯示戰爭列表
        showWarList(gm);
        break;
        }
        case "使用類型": { // 顯示使用類型
        showUseType(gm, param);
        break;
        }
        case "壓制角色": { // 壓制角色
        setCharBlock(gm, param);
        break;
        }
        case "解除角色壓制": { // 解除角色壓制
        setCharBlockDelete(gm, param);
        break;
        }
        case "設置潛水模式": { // 設置潛水模式
        setSleepingMode(gm, param);
        break;
        }
        case "解除潛水模式": { // 解除潛水模式
        unSetSleepingMode(gm, param);
        break;
        }
        case "顯示下一個效果": { // 顯示下一個效果
        showNextEffect(gm);
        break;
        }
        case "初始化效果": { // 初始化效果
        showEffectInit(gm, param);
        break;
        }
        case "變身活動": { // 變身活動
        POLYMORPH_EVENT(gm, param);
        break;
        }
        case "血盟標誌": { // 血盟標誌
        clanMark(gm, param);
        break;
        case "mj":
        mjClear(gm, param);
        break;
        case "實例管理": { // 實例管理
        MJInstanceLoadManager.commands(gm, param);
        break;
        }
        case "Raid管理": { // Raid管理
        MJRaidLoadManager.commands(gm, param);
        break;
        }
        case "查詢畫框": { // 查詢畫框
        MJCTLoadManager.commands(gm, param);
        break;
        }
        case "無呼喚": { // 無呼喚
        nocall(gm, param);
        break;
        }
        case "排名系統": { // 排名系統
        MJRankLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
        break;
        }
        case "道具命令": { // 執行道具命令
        execute(gm, param, param);
        break;
        }
        case "金幣": { // 執行Adena命令
        execute1(gm, param, param);
        break;
        }
        case "事件系統": { // 事件系統
        Event_System(gm, param);
        break;
        }
        case "狩獵事件": { // 狩獵事件
        huntEvent(gm, param);
        break;
        }
        case "召喚事件": { // 召喚事件
        Event1(gm, param);
        break;
        }
        case "無延遲用戶": { // 無延遲用戶
        NoDelayUser(gm);
        break;
        }
        case "帳號幫助": { // 帳號幫助
        doolyHelp1(gm);
        break;
        }
        case "周邊封禁": { // 周邊封禁
        LargeAreaBan(gm, param);
        break;
        }
        case "大範圍IP封禁": { // 大範圍IP封禁
        LargeAreaIPBan(gm, param);
        break;
        case "組隊": { // 組隊
        party(gm, param);
        break;
        }
        case "給予要塞": { // 給予要塞
        GiveHouse(gm, param);
        break;
        }
        case "開始攻城": { // 開始攻城
        castleWarStart(gm, param);
        break;
        }
        case "測試通緝": { // 測試通緝
        hunt(gm, param);
        break;
        }
        case "推送系統": { // 推送系統
        pushSystem(gm, param);
        break;
        }
        case "結束攻城": { // 結束攻城
        castleWarExit(gm, param);
        break;
        }
        case "測試消息": { // 測試消息
        gm.sendPackets(new S_TestPacket(S_TestPacket.a));
        break;
        }
        case "召喚bot": { // 召喚機器人
        召喚bot(gm, param);
        break;
        }
        case "伺服器保存": { // 伺服器保存
        serversave(gm);
        break;
        }
        case "全體禮物": { // 全體禮物
        allpresent(gm, param);
        break;
        }
        case "解除壓制": { // 解除壓制
        accountdel(gm, param);
        break;
        }
        case "經驗值恢復": { // 經驗值恢復
        returnEXP(gm, param);
        break;
        }
        case "自動拾取": { // 自動拾取
        autoloot(gm, param);
        break;
        }
        case "清理娃娃": { // 清理娃娃
        인형청소(gm);
        break;
        }
        case "角色平衡": { // 角色平衡
        CharacterBalance(gm, param);
        break;
		/*
        case "無人商店": privateShop(gm);
        break;
        */
        case "圖標設置": { // 圖標設置
        icon(gm, param);
        break;
        }
        case "燈光設置": { // 燈光設置
        maphack(gm, param);
        break;
        }
        case "開啟待機": { // 開啟待機
        standBy(gm, param);
        break;
        }
        case "設置屬性": { // 設置屬性
        fullstat(gm, param);
        break;
        }
        case "檢查商店": { // 檢查商店
        檢查商店(gm);
        break;
        }
        case "設置效果": { // 設置效果
        effect(gm, param);
        break;
        }
        case "刪除用戶物品欄": { // 刪除用戶物品欄
        targetInventoryDelete(gm, param);
        break;
        }
        case "添加帳號": { // 添加帳號
        addaccount(gm, param);
        break;
        }
        case "全體召喚": { // 全體召喚
        allrecall(gm);
        break;
        }
        case "發放KOMA": { // 發放KOMA
        SpecialEventHandler.getInstance().doGiveEventStaff();
        break;
        }
        case "發放BOSS召喚卷": { // 發放BOSS召喚卷
        SpecialEventHandler.getInstance().doGiveEventStaff2();
        break;
        }
        case "發放紅寶石": { // 發放紅寶石
        SpecialEventHandler.getInstance().doGiveEventStaff1();
        break;
        }
        case "查詢壓制列表": { // 查詢壓制列表
        search_banned(gm);
        break;
        }
        case "修改密碼": { // 修改密碼
        changePassword(gm, param);
        break;
        }
        case "釋放至村莊": { // 釋放至村莊
        unprison(gm, param);
        break;
        }
        case "釋放至隱藏地": { // 釋放至隱藏地
        unprison2(gm, param);
        break;
        case "解除禁言": { // 解除禁言
        chatx(gm, param);
        break;
        }
        case "傳送": // 傳送
        case "解除傳送": { // 解除傳送
        tell(gm);
        break;
        }
        case "找": { // 搜索數據庫
        searchDatabase(gm, param);
        break;
        }
        case "玩家vs玩家": { // 玩家對玩家戰鬥
        Pvp(gm, param);
        break;
        }
        case "帳號查詢": { // 帳號查詢
        account_Cha(gm, param);
        break;
        }
        case "升級": { // 升級
        levelup2(gm, param);
        break;
        }
        case "商店驅逐": { // 商店驅逐
        ShopKick(gm, param);
        break;
        }
        case "監禁": { // 監禁
        hold(gm, param);
        break;
        }
        case "垃圾清理": { // 垃圾清理
        가비지정리(gm);
        break;
        }
        case "更改端口": { // 更改端口
        changePort(gm, param);
        break;
        }
        case "刪除物品欄": { // 刪除物品欄
        InventoryDelete(gm, param);
        break;
        }
        case "重新加載要塞": { // 重新加載要塞
        reloadHouse(gm, param);
        break;
        }
        case "尋找禮物": { // 尋找禮物
        禮物(gm, param);
        break;
        }
        case "全面清理": { // 全面清理
        全面清理(gm);
        break;
        }
        case "死鎖檢測": { // 死鎖檢測
        GeneralThreadPool.getInstance().execute(new DeadLockDetector(gm));
        break;
        case "攻擊速度檢查": // 攻擊速度檢查
        gm.AttackSpeedCheck2 = 1;
        gm.sendPackets("\fY請對稻草人攻擊10次。");
        break;
        case "移動速度檢查": // 移動速度檢查
        gm.MoveSpeedCheck = 1;
        gm.sendPackets("\fY請朝一個方向移動10次。");
        break;
        case "魔法速度檢查": // 魔法速度檢查
        gm.magicSpeedCheck = 1;
        gm.sendPackets("\fY請使用你想要的魔法10次。");
        break;
        case "刪除圖片": // 刪除圖片
        this.get_delete_gfx(gm);
        break;
        case "檢查錯誤賽道": // 檢查錯誤賽道
        bug_race_check(gm);
        break;
        case "操作錯誤賽道": // 操作錯誤賽道
        bug_race_rate(gm, param);
        break;
        case "BOSS通知": // BOSS通知
        spawnNotifyOnOff(gm, param);
        break;
        case "測試命令": // 測試命令
        testCommands(gm, param);
        break;
        case "檢查狂熱愛好者": // 檢查狂熱愛好者
        gm.sendPackets(S_ShowCmd.getPlayMovieNoti("http://1111111.megaplug.kr/pricemania/pricePreviewboard1.html", -1));
        break;
        case "比特幣": // 比特幣
        gm.sendPackets(S_ShowCmd.getPlayMovieNoti("https://www.bithumb.com/u5/US506", -1));
        break;

//// 當 case 為 "로또게임" 時：
//        case "로또게임":
//        // 如果 lottoInfo 的 run 屬性為 true
//        if (Config.lottoInfo.run == true) {
//        // 設定 LottoSystem 实例的 isGmOpen 屬性為 true
//        LottoSystem.getInstance().isGmOpen = true;
//        }
//        break;
        **//
        case "檢查系統啟用狀態":
        case "格蘭凱恩":
        if (!FatigueProperty.getInstance().use_fatigue()) {
        gm.sendPackets("\aG目前格蘭凱恩系統未啟用。");
        return;
        }
        gm.sendPackets("\aN- 依照艾因哈薩克祝福等級，每分鐘累計量");
        gm.sendPackets("\aN[4級:1] [3級:2] [2級:3] [1級:4] [祝福0:5]");
        gm.sendPackets("\aN- 當格蘭凱恩發動時，經驗值及亞丁幣掉落率減少80%");
        gm.sendPackets("\aN- 當格蘭凱恩發動時，10小時後自動解除。");
        if (gm.getAccount().has_fatigue())
        gm.sendPackets(String.format("\f3距離格蘭凱恩之怒結束還有%,d秒。", gm.getAccount().remain_fatigue() / 1000L));
        else
        gm.sendPackets(String.format("\aG[目前格蘭凱恩數值: %,d]", gm.getAccount().get_fatigue_point()));
        break;
        case "重置 格蘭凱恩": // 重置 格蘭凱恩
        Account.initialize_fatigue();
        for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
        Account account = pc.getAccount();
        if (account == null || pc.getAI() != null)
        continue;

        account.initialize_fatigue_info(pc);
        }
        gm.sendPackets("格蘭凱恩之怒已重置。");
        case "類型": // 類型
        int type = Integer.parseInt(param);
        gm.sendPackets("\aA料理類型 : " + type);
        gm.sendPackets(new S_PacketBox(53, type, 1800));
        break;

        case "獵人增益": // 獵人增益
        try {
        gm.sendPackets(S_InventoryIcon.icoReset(HUNTER_BLESS, 4992, 1800L, true));
        } catch (Exception e) {
        e.getStackTrace();
        }
        break;

        case "製作檢查": // 製作檢查
        if (!製作檢查) {
        製作檢查 = true;
        gm.sendPackets("\aA■ 製作檢查 ON! ■");
        } else {
        製作檢查 = false;
        gm.sendPackets("\aA■ 製作檢查 OFF ■");
        }
        break;
        }

        /** MJCTSystem **/
        break;
        case "測試標記": // 測試標記
        try {
        StringTokenizer st = new StringTokenizer(param);
        String code = st.nextToken();
        if (code.equalsIgnoreCase("開啟")) { // 如果參數為 "켬"（開啟）
        Config.測試模式 = true; // 設置測試模式為 true
        for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) { // 遍歷所有玩家
        for (L1Object npc : L1World.getInstance().getVisibleObjects(pc, 18)) { // 遍歷可見範圍內的所有對象
        if (((npc instanceof L1AuctionBoardInstance)) || ((npc instanceof L1BoardInstance)) || ((npc instanceof L1GuardInstance)) || ((npc instanceof L1MerchantInstance)) || ((npc instanceof L1TeleporterInstance))) {
        // 如果對象是拍賣板、公告板、守衛、商人或傳送師
        pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream((L1NpcInstance) npc)); // 向玩家發送相應的包
        // pc.sendPackets(S_WorldPutObject.get((L1NpcInstance) npc));
        }
        }
        }
        }
        } catch (Exception e) {
        e.printStackTrace(); // 捕捉異常並打印堆棧跟蹤
        }
        break;
        } else if (code.equalsIgnoreCase("關閉")) {
        Config.測試模式 = false;
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
         * case "화면메세지": try { StringTokenizer st = new StringTokenizer(param); int number = Integer.parseInt(st.nextToken()); int number2 = Integer.parseInt(st.nextToken()); gm.sendPackets(new S_TestPacket(S_TestPacket.a, number, number2, "00 ff ff")); } catch
         * (Exception e) { gm.sendPackets("화면메세지 [gfx번호] [메세지번호] 입력"); gm.sendPackets("gfx는 4천이면 8000 *2배다."); gm.sendPackets("메세지 4천이면 2002 2가 붙는다."); } break;
         */
        case "全體增益": // 全體增益
        try {
        StringTokenizer st = new StringTokenizer(param);
        String status = st.nextToken();
        L1AllBuff.getInstance().execute(gm, param, status);
        } catch (Exception e) {
        gm.sendPackets(new S_ChatPacket(gm, "----------------------------------------------------"));
        gm.sendPackets(new S_ChatPacket(gm, " 1:完全恢復 2:祝福效果  3:負面狀態 4:昏迷狀態"));
        gm.sendPackets(new S_ChatPacket(gm, "----------------------------------------------------"));
        }
        break;

        case "重生": // 重生
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

        case "發放NCoin": // 發放NCoin
        try {
        StringTokenizer tokenizer = new StringTokenizer(param);
        String name = tokenizer.nextToken();
        int coin = Integer.parseInt(tokenizer.nextToken());
        increaseNcoin(gm, name, coin);
        } catch (Exception e) {
        gm.sendPackets("[發放NCoin] [角色名] [金額] 輸入");
        }
        break;

        case "回收NCoin": // 回收NCoin
        try {
        StringTokenizer tokenizer = new StringTokenizer(param);
        String name = tokenizer.nextToken();
        int coin = Integer.parseInt(tokenizer.nextToken());
        L1PcInstance tg = L1World.getInstance().getPlayer(name);
        if (tg != null) {
        tg.addNcoin1(coin);
        } else {
        gm.sendPackets("角色未找到");
        }
        } catch (Exception e) {
        gm.sendPackets("[回收NCoin] [角色名] [金額] 輸入");
        }
        break;

        String s = String.format("\aGN幣 (%,d)韓元已追回. 謝謝.", coin);
        tg.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, s));
        tg.sendPackets(s);

        String remainMessage = String.format("\f2持有N個幣:(%,d)元", tg.getNetConnection().getAccount().Ncoin_point);
        tg.sendPackets(remainMessage);
        gm.sendPackets(String.format("\aA角色名稱:[\aG%s\aA] 回收量:[\aG%,d\aA]完全！ \f2[目標持有N幣：(%,d)元]", name, coin, tg.getNetConnection().getAccount().Ncoin_point));
        } else {
        gm.sendPackets("目前未登錄的角色。");
        }
        } catch (Exception e) {
        gm.sendPackets("[回收NCoin] [角色名] [金額] 輸入。");
        }
        break;

        case "確認NCoin": // 確認NCoin
        try {
        StringTokenizer tokenizer = new StringTokenizer(param);
        String name = tokenizer.nextToken();
        L1PcInstance tg = L1World.getInstance().getPlayer(name);
        if (tg != null) {
        gm.sendPackets("\aA剩餘金額： 角色名稱：[\aG" + name + "\aA] N幣:[\aG" + tg.getNetConnection().getAccount().Ncoin_point + "\aA] 確認");
        } else {
        gm.sendPackets("目前未登錄的角色。");
        }
        } catch (Exception e) {
        gm.sendPackets("[確認NCoin] [角色名] 輸入。");
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
        gm.sendPackets(".聊天 輸入[角色名稱][聊天訊息]");
        }
        break;
        case "修改血盟信息": // 修改血盟信息
        try {
        StringTokenizer st = new StringTokenizer(param);
        String name = st.nextToken();
        int number = Integer.parseInt(st.nextToken());
        L1Clan clan = L1World.getInstance().findClan(name);
        if (clan == null) {
        gm.sendPackets("血盟不存在。");
        } else {
        clan.setCastleId(number);
        L1World.getInstance().removeClan(clan); // 從世界中移除血盟
        L1World.getInstance().storeClan(clan); // 存儲更新後的血盟
        ClanTable.getInstance().updateClan(clan); // 更新血盟信息
        gm.sendPackets(name + " 血盟信息已更新。");
        }
        } catch (Exception e) {
        gm.sendPackets(".血盟城堡 [血盟名稱] [城堡編號] 輸入");
        gm.sendPackets("肯特1, 俄克2, 溫達3, 奇岩4, 海音5, 威登6, 亞丁7, 迪亞德8");
        }
        break;

        case "重新加載商店": // 重新加載商店
        try {
        int npcid = Integer.parseInt(param);
        L1Npc npc = NpcTable.getInstance().getTemplate(npcid);
        ShopTable.getInstance().Reload(npcid);
        gm.sendPackets("NPC : " + npc.get_name() + " 已重新加載。");
        } catch (Exception e) {
        gm.sendPackets(".商店重新加載 NPC ID");
        }
        break;
        case "血盟關係":
        try {
        StringTokenizer st = new StringTokenizer(param);
        String name = st.nextToken();
        String clanname = st.nextToken();
        L1PcInstance pc = L1World.getInstance().getPlayer(name);
        L1Clan clan = L1World.getInstance().findClan(clanname);
        if (pc == null) {
        gm.sendPackets("沒有該用戶.");
        return;
        }
        if (clan == null) {
        gm.sendPackets("沒有這樣的血盟.");
        return;
        }
        if (pc.getClanid() != 0) {
        gm.sendPackets("" + pc.getName() + "既然你有血盟，我就讓你退出血盟.");
        pc.ClearPlayerClanData(clan);
        clan.removeClanMember(pc.getName());
        gm.save();
        return;
        }

        for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
        clanMembers.sendPackets(new S_ServerMessage(94, pc.getName()));
        // \f1%0被接納為該血盟的成員.
        }
        pc.setClanid(clan.getClanId());
        pc.setClanname(clanname);
        pc.setClanRank(L1Clan.일반);
        pc.setTitle("");
        pc.setClanMemberNotes("");
        pc.sendPackets(new S_CharTitle(pc.getId(), ""));
        Broadcaster.broadcastPacket(pc, new S_CharTitle(pc.getId(), ""));
        clan.addClanMember(pc.getName(), pc.getClanRank(), pc.getLevel(), "", pc.getId(), pc.getType(), pc.getOnlineStatus(), pc);
        pc.save(); // 在DB中輸入字元訊息
        pc.sendPackets(new S_PacketBox(pc, S_PacketBox.PLEDGE_REFRESH_PLUS));
        pc.sendPackets(new S_ServerMessage(95, clanname)); // \f1%0 혈맹에
        pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, true);
        } catch (Exception e) {
        gm.sendPackets("輸入 .血盟關係 [角色名稱][血盟名稱]");
        }
        break;
        case "退出血盟": // 血盟退出
        try {
        StringTokenizer tokenizer = new StringTokenizer(param);
        String pcName = tokenizer.nextToken();
        L1PcInstance pc = L1World.getInstance().getPlayer(pcName);
        if (pc == null) {
        gm.sendPackets("該玩家不存在。");
        return;
        }
        L1Clan clan = pc.getClan();
        L1PcInstance clanMember[] = clan.getOnlineClanMember();
        for (int i = 0; i < clanMember.length; i++) {
        clanMember[i].sendPackets(new S_ServerMessage(ServerMessage.LEAVE_CLAN, param, clan.getClanName())); // 1%0이 %1혈맹을 탈퇴했습니다.
        }
        pc.ClearPlayerClanData(clan);
        clan.removeClanMember(pc.getName());
        pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, true);
        } catch (Exception e) {
        gm.sendPackets(".退出血盟 [角色名] 輸入");
        }
        break;
        break;
        // case "紅色戰爭宣言": UserCommands.getInstance().blooodWarStart(gm, param); break;
        case "排行發放停止": // 排行發放停止
        rankGiveItemStop(gm, param);
        break;

        case "查看購買時間": // 查看購買限制時間
        viewShopBuyLimit(gm);
        break;

        case "設置購買限制": // 設置購買限制
        shopBuyLimit(gm, param);
        break;

        case "技能檢查": // 技能檢查
        SpellAskCommands(gm, param);
        break;

        case "角色清理": // 角色清理
        character_delete(gm, param);
        break;

        case "初始化": // 初始化
        case "資料庫初始化": // 資料庫初始化
        gm.sendPackets(new S_Message_YN(C_Attr.MSGCODE_6008_RESET, 6008, "你確定要進行資料庫初始化嗎？"));
//            test(gm);
//            clear_DB(gm);
        break;

        case "重做指令": // 重做指令
        if (!_lastCommands.containsKey(gm.getId())) {
        gm.sendPackets(new S_ServerMessage(74, "命令 " + cmd + " 無法使用"));
        return;
        }
        redo(gm, param);
        break;

default: // 默認處理
        gm.sendPackets(new S_SystemMessage("管理員命令 " + cmd + " 不存在，將轉交給用戶命令。"));
        UserCommands.getInstance().handleCommands(gm, cmdLine);
// gm.sendPackets(new S_SystemMessage("[Command] 命令 " + cmd + " 不存在。"));
        break;
        }
public static void test(L1PcInstance pc) {
        System.out.println("測試完成");
        }
public static void clear_DB(L1PcInstance pc) {
        Connection con = null;
        try {
        // 要刪除的 TODO 資料庫
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

        pc.sendPackets("資料庫初始化完成。請嘗試再次運行伺服器.");

        } catch (Exception e) {
        e.printStackTrace();
        } finally {
        SQLUtil.close(con);
        }
        }
        private void viewShopBuyLimit(L1PcInstance pc) {
            ArrayList<ShopBuyLimit> sbl_list = ShopBuyLimitInfo.getInstance().getCharacterList(pc.getId());
            if (sbl_list != null && !sbl_list.isEmpty()) {
                for (ShopBuyLimit sbl : sbl_list) {
                    if (sbl.get_count() <= 0) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        [21:06]
        String sLatestLoginDate = formatter.format(sbl.get_end_time());
                        String message = String.format("2%s2是(為) %s %s 之後可以重新購買。",
        sbl.get_item_name(),
        (sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) ? "2同一帳號的情況下 " : "2",
        sLatestLoginDate);
                        pc.sendPackets(message);
                    } else {
                        pc.sendPackets("2" + sbl.get_item_name() + "可以再購買 " + sbl.get_count() + " 個。");
                    }
                }
                pc.sendPackets("2總共找到 " + sbl_list.size() + " 筆紀錄。");
            } else {
                pc.sendPackets("2沒有購買限制列表。");
            }
        }

private void shopBuyLimit(L1PcInstance gm, String s) {
        try {
        StringTokenizer tok = new StringTokenizer(s);
        String action = tok.nextToken();

        if (action.equalsIgnoreCase("保存購買限制")) { // 保存購買限制
        String type = tok.nextToken();
        if (type.equalsIgnoreCase("保存所有的購買限制")) { // 保存所有的購買限制
        ShopBuyLimitInfo.getInstance().save();
        gm.sendPackets("\f3購買限制強制保存完成。");
        } else { // 保存特定玩家的購買限制
        String target_name = tok.nextToken();
        L1PcInstance target = L1World.getInstance().getPlayer(target_name);
        if (target == null) {
        gm.sendPackets("無法在世界中找到該玩家。");
        return;
        }

        ShopBuyLimitInfo.getInstance().save(target.getId());
        gm.sendPackets(target.getName() + "\f2的購買限制列表已保存。");
        }
        } else if (action.equalsIgnoreCase("刪除購買限制")) { // 刪除購買限制
        String target_name = tok.nextToken();
        L1PcInstance target = L1World.getInstance().getPlayer(target_name);
        if (target_name.equalsIgnoreCase("刪除所有的購買限制")) { // 刪除所有的購買限制
        ShopBuyLimitInfo.getInstance().clearShopBuyLimit();
        gm.sendPackets("\f2所有購買限制列表已刪除。");
        } else if (target != null && target_name.equalsIgnoreCase(target.getName())) { // 刪除特定玩家的所有購買記錄
        ShopBuyLimitInfo.getInstance().removeShopBuyLimit(target.getId());
        gm.sendPackets(String.format("\f2%s的所有購買記錄已刪除。", target_name));
        } else { // 刪除特定玩家的特定物品購買記錄
        int itemid = 0;
        if (tok.hasMoreTokens()) {
        itemid = Integer.parseInt(tok.nextToken());
        }
        // 此處應增加對特定物品的購買記錄刪除邏輯
        // 例如：ShopBuyLimitInfo.getInstance().removeShopBuyLimit(target.getId(), itemid);
        }
        }
        } catch (Exception e) {
        gm.sendPackets("\f2無法處理購買限制命令。請確認輸入格式是否正確。");
        }
        }

        if (itemid != 0) {
        if (ItemTable.getInstance().getTemplate(itemid) == null) {
        gm.sendPackets("\\f2該商品不存在.");
        return;
        }

        if (ShopBuyLimitInfo.getInstance().getShopBuyLimit(itemid) == null) {
        gm.sendPackets("\\f2該商品不受購買限制.");
        return;
        }
        }

        if (itemid == 0) {
        ShopBuyLimitInfo.getInstance().removeShopBuyLimit(target.getId());
        gm.sendPackets(String.format("\f2%s的所有購買記錄已刪除。", target_name));
        } else {
        ShopBuyLimitInfo.getInstance().removeShopBuyLimit(target.getId(), itemid);
        gm.sendPackets(target.getName() + " 的 " + ItemTable.getInstance().getTemplate(itemid).getName() + " 購買限制已刪除。");
        }
        } else {
        gm.sendPackets("無法在世界中找到該玩家。");
        }
        }
        } catch (Exception e) {
        gm.sendPackets(new S_SystemMessage("\f2.購買限制設定 [刪除] [刪除時角色名/全部] [角色名時物品編號(未輸入時刪除該角色所有記錄)]"));
        gm.sendPackets(new S_SystemMessage("\f2.購買限制設定 [保存] [角色名/全部]"));
        }

        }

private void pushSystem(L1PcInstance gm, String s) {
        try {
        StringTokenizer tok = new StringTokenizer(s);
        String command = tok.nextToken();

        if (command.equalsIgnoreCase("啟動")) { // 啟動
        if (PushItemController.isPushSystem()) {
        gm.sendPackets(new S_SystemMessage("推送系統已經啟動。"));
        return;
        }
        PushItemController.setPushSystem(true);
        gm.sendPackets(new S_SystemMessage("推送系統已啟動。"));
        } else if (command.equalsIgnoreCase("停止")) { // 停止
        if (!PushItemController.isPushSystem()) {
        gm.sendPackets(new S_SystemMessage("推送系統未啟動。"));
        return;
        }
        PushItemController.setPushSystem(false);
        gm.sendPackets(new S_SystemMessage("推送系統已停止。"));
        } else {
        gm.sendPackets(new S_SystemMessage("無效的請求。"));
        }
        } catch (Exception e) {
        gm.sendPackets(new S_SystemMessage(".推播系統 [啟動/停止]"));
        }
        }

private void bug_race_rate(L1PcInstance pc, String s) {
        try {
        StringTokenizer tok = new StringTokenizer(s);
        int num = Integer.parseInt(tok.nextToken());
        int speed = Integer.parseInt(tok.nextToken());
        BugRaceController.getInstance().setSpeed(num - 1, speed);
        pc.sendPackets(String.format("已將第 %d 條賽道的速度設置為 %d。", num, speed));
        } catch (Exception e) {
        pc.sendPackets("指令: 請輸入 [賽道] [速度]，格式為: 蟲賽控制 [賽道] [速度]");
        }
        }

private void bug_race_check(L1PcInstance pc) {
        try {
        for (int i = 0; i < 5; ++i) {
        pc.sendPackets(String.format("賽道 : %d, 速度 : %d", i + 1, BugRaceController.getInstance().getSpeed(i)));
        }
        } catch (Exception e) {
        pc.sendPackets("指令: 請使用 '蟲賽確認' 格式輸入。");
        }
        }

private FastTable<L1NpcInstance> list = new FastTable<L1NpcInstance>();

public void add_list(L1NpcInstance npc) {
synchronized (list) {
        if (!list.contains(npc))
        list.add(npc);
        }
        }


        rivate void get_delete_gfx(L1PcInstance pc) {
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
        pc.sendPackets("'圖像刪除' 已完成。");
        } catch (Exception e) {
        pc.sendPackets("指令: 請使用 '.圖像刪除' 格式輸入。");
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
        if (on.equalsIgnoreCase("開啟")) {
        gm.sendPackets(new S_Ability(3, true));
        gm.sendPackets("畫面光亮狀態: 開啟");
        } else if (on.equals("關閉")) {
        gm.sendPackets(new S_Ability(3, false));
        gm.sendPackets("畫面光亮狀態: 關閉");
        }
        } catch (Exception e) {
        gm.sendPackets("指令: 請使用 '.光亮 [開, 關]' 格式設置。");
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
        gm.sendPackets("所有角色的排名發放物品已被刪除。");
        } else {
        L1PcInstance target = L1World.getInstance().getPlayer(on);
        if (target == null) {
        gm.sendPackets(on + " 不存在於世界中的角色。");
        return;
        }
        String[] rank_item_id = MJRankLoadManager.MRK_TOP_GIVE_ITEM_ID.split(",");
        String[] rank_item_enchant = MJRankLoadManager.MRK_TOP_GIVE_ITEM_ENCHANT.split(",");
        for (int r = 0; r < rank_item_id.length; r++) {
        int itemid = Integer.valueOf(rank_item_id[r]);
        int enchant = Integer.valueOf(rank_item_enchant[r]);
        target.getInventory().consumeRankItem(target, itemid, 1, enchant);
        }
        gm.sendPackets(on + " 角色的排名發放物品已被刪除。");
        }
        } catch (Exception e) {
        gm.sendPackets("指令: 請使用 '.排名發放刪除 [全部 或 用戶名]' 格式設置。");
        }
        }

// TODO 增加 GM 命令
private void showHelp(L1PcInstance gm) {
// ServerExplainTable.getInstance().server_Explain(gm, 1);
// gm.sendPackets("1
        2
        ");
        StringBuilder sb = new StringBuilder();
        sb.append("━━━━━━━━━━━━ 管理員指令 ━━━━━━━━━━━━").append("\r\n");
        sb.append("\aG[基本]:.回程.出席.召喚.位置.誰.村莊.聊天.監獄.go.回覆").append("\r\n");
        sb.append("\aO[設置]:.無人.變身.等級.殺人.重載.血盟退出.自動拾取 .掉落不可 .倉庫不可 .交易不可").append("\r\n");
        sb.append("\aO　　　.自動拾取 .全體清理 .等待開啟 .刪除信件 .刪除背包 .攻城時間 .開始攻城 .死鎖").append("\r\n");
        sb.append("\aO　　　.結束攻城 .城堡血盟操作 .給予據點 .平衡 .重載商店 .血盟經驗 .重置(不可恢復)").append("\r\n");
        sb.append("\aO　　　.降NPC .整理 .垃圾整理 .更改端口 .檢查商店 .使用類型").append("\r\n");
        sb.append("\aY[帳號]:.設置 .信息 .帳號 .帳號扣押 .永久封禁 .IP封禁 .區域封禁 .帳號信息 .新增帳號").append("\r\n");
        sb.append("\aY　　　.修改密碼 .懲罰 .封IP .封商店 .刪除玩家背包 .角色扣押 .解除角色扣押").append("\r\n");
        sb.append("\aY　　　.確認帳號 .更改問題 .恢復經驗 .檢查帳號 .封血盟 .周圍封禁 .區域封禁").append("\r\n");
        sb.append("\aY　　　.解除扣押 .扣押列表 .帳號停權 .整理角色").append("\r\n");
        sb.append("\aW[檢查]:.移動 .驅逐 .查詢 .監視 .搜索 .禁言 .公開禁言 .解除禁言 .檢查背包").append("\r\n");
        sb.append("\aW[伺服器]:.生成 .配置 .怪物 .物品 .全體禮物").append("\r\n");
        sb.append("\aQ[增益]:.增益 .復活 .速度 .全體增益 .個人增益 .背包圖像 .攻擊速度檢查 .移動速度檢查 .魔法檢查").append("\r\n");
        sb.append("\aQ　　　.等級禮物 .圖像 .召喚 .圖標 .圖像 .效果 .等級製作").append("\r\n");
        sb.append("\aH[其他]:.補血 .隱身 .伺服器保存 .隊伍召喚 .機器人 .人工智慧 .清理娃娃 .管理員商店(2~3) .角色檢查").append("\r\n");
        sb.append("\aH　　　.自動狩獵結束 .分發經驗藥水(整個世界) .分發老闆召喚卷軸 .分發黃寶石 .事件").append("\r\n");
        sb.append("\aH　　　.無傷 .帳戶 .增益圖標料理圖標 .桌面 .地板 .地面 .製作檢查 .傳送 .刪除物品").append("\r\n");
        sb.append("\aH　　　.分發N幣 .回收N幣 .確認N幣 .充值N幣 .VIP .據點重載 .召喚事件").append("\r\n");
        sb.append("\aH　　　.突襲 .實例 .排名系統 .潛水模式 .解除潛水 .檢查狂熱者 .擊殺排行榜 .座標").append("\r\n");
        sb.append("\aH　　　.原型 .Ping .網絡安全 .保護模式 .網絡 .轉儲 .對象列表").append("\r\n");
        sb.append("\aH　　　.標記設置 .比特幣 .戰鬥系統 .驗證碼(防自動) .附魔系統(tb_enchanties..)").append("\r\n");
        sb.append("\aH　　　.角色狀態 .詛咒 .重置詛咒 .地下城計時器 .血盟信息 .血盟標記 .尋找 .老闆 .探索禮物").append("\r\n");
        sb.append("\aH　　　.武器附魔成功 .防具附魔成功 .人偶箱子 .墮落任務 .惡女任務 .惡魔任務 .死亡任務").append("\r\n");
        sb.append("\aH　　　.惡魔王任務 .護甲任務 .卡比任務 .伊斯卡利 .沙蟲 .祝福 .主宰老闆 .任務").append("\r\n");
        sb.append("\aH　　　.符文石卡比 .符文石分解 .符文石惡魔王 .符文石護甲 .決鬥 .寵物 .鬥犬 .屬性").append("\r\n");
        sb.append("\aH　　　.確認手續費 .出售註冊 .申請購買 .競技場 .結束競技 .伺服器移動 .對抗賽 .伺服器轉移").append("\r\n");
        sb.append("\aH　　　.代碼 .代碼充值 .充值狀態 .提款狀態 .硬幣手續費 .機率 .確認機率 .重置機率").append("\r\n");
        sb.append("\aH　　　.格蘭肯 .重置格蘭肯 .書系統 .白名單 .開關 .彩票 .投票 .投票控制").append("\r\n");
        sb.append("\aH　　　.等待隊列 .刪除排名發放 .輸出版本 .應用中心 .註冊優惠券 .生成地圖 .重新生成 .申請解除封印").append("\r\n");
        sb.append("\aH　　　.封包農場 .技能檢查 .宏命令 .檢測黑客 .重置超頻 .龍突襲 .螞蟻洞(TEST用途)").append("\r\n");
        sb.append("\aH　　　.送禮物 .潛能 .設置購買限制 .購買限制 .購買限制時間 .賦予祝福 .製作成功").append("\r\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━").append("\r\n");
        if (gm.getName().equalsIgnoreCase("網路管理員")) {
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
        gm.sendPackets("請輸入 '.搜索 [0~5] [名稱]'");
        gm.sendPackets("0=雜物, 1=武器, 2=盔甲, 3=NPC, 4=變身, 5=NPC(gfxid)");
        }
        }

private void 全部清理(L1PcInstance gm) {
        int cnt = 0;
        for (L1Object obj : L1World.getInstance().getObject()) {
        if (obj instanceof L1MonsterInstance) {
        L1MonsterInstance mon = (L1MonsterInstance) obj;
        mon.die(gm);
        cnt++;
        }

        }
        gm.sendPackets("已經殺死了 " + cnt + " 隻怪物。");
        }

private void doolyHelp1(L1PcInstance gm) {
        gm.sendPackets("----------------------------------------------------");
        gm.sendPackets("存款帳戶 : / 無");
        gm.sendPackets("帳號 : 無");
        gm.sendPackets("----------------------------------------------------");
        }

private void autoloot(L1PcInstance gm, String param) {
        try {
        StringTokenizer tok = new StringTokenizer(param);
        String type = tok.nextToken();

        if (type.equalsIgnoreCase("重新載入")) {
        AutoLoot.getInstance();
        AutoLoot.reload();
        gm.sendPackets("自動拾取設置已重新加載。");

        } else if (type.equalsIgnoreCase("搜尋")) {
        String nameid = tok.nextToken();
        Selector.exec(
        String.format("SELECT b.item_id, b.name FROM autoloot AS a INNER JOIN (SELECT item_id, name FROM etcitem) AS b ON a.item_id=b.item_id WHERE b.name LIKE '%%%s%%'", nameid),
        new FullSelectorHandler() {
@override
public void handle(ResultSet rs) throws SQLException {
        while (rs.next()) {
        int itemId = rs.getInt("item_id");
        String itemName = rs.getString("name");
        gm.sendPackets("物品ID: " + itemId + ", 名稱: " + itemName);
        }
        }
        }
        );
        }
        } catch (Exception e) {
        gm.sendPackets("使用方法: .autoloot [重新載入 | 搜尋] [名稱]");
        }
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
        while (rs.next()) {
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
        gm.sendPackets("找不到指定的物品。");
        return;
        }
        }

        L1Item temp = ItemTable.getInstance().getTemplate(itemid);
        if (temp == null) {
        gm.sendPackets("找不到指定的物品。");
        return;
        }

        if (type.equalsIgnoreCase("추가")) {
        if (AutoLoot.getInstance().isAutoLoot(itemid)) {
        gm.sendPackets("該物品已經在自動拾取列表中。");
        return;
        }

        // 以下部分為添加到自動拾取列表的代碼
        AutoLoot.getInstance().addItem(itemid);
        gm.sendPackets("物品添加至自動拾取列表。");
        }

        AutoLoot.getInstance().storeId(itemid);
        gm.sendPackets("物品已添加到自動拾取項目中。");

        } else if (type.equalsIgnoreCase("삭제")) {
        if (!AutoLoot.getInstance().isAutoLoot(itemid)) {
        gm.sendPackets("自動拾取項目中沒有該物品。");
        return;
        }
        AutoLoot.getInstance().deleteId(itemid);
        gm.sendPackets("物品已從自動拾取項目中刪除。");
        }
        }
        } catch (Exception e) {
        gm.sendPackets("使用方法:");
        gm.sendPackets(".autoloot 重新載入");
        gm.sendPackets(".autoloot 新增|刪除項目ID|名稱");
        gm.sendPackets(".autoloot 搜尋名稱");
        }
        }

private void LargeAreaBan(L1PcInstance pc, String param) {
        try {
        StringTokenizer st = new StringTokenizer(param);
        int range = Integer.parseInt(st.nextToken());
        Integer reason = S_LoginResult.banServerCodes.get(st.nextToken());
        if (reason == null) throw new Exception("");

        int count = 0;
        IpTable iptable = IpTable.getInstance();
        pc.sendPackets("----------------------------------------------------");

        for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, range)) {
        Account.ban(player.getAccountName(), reason); // 將帳號進行封禁
        iptable.banIp(player.getNetConnection().getIp()); // 將IP添加到封禁列表
        pc.sendPackets(player.getName() + ", (" + player.getAccountName() + ")");
        player.logout();
        player.getNetConnection().kick();
        count++;
        }

        pc.sendPackets("您已永久封禁了周圍的 " + count + " 個玩家。");
        pc.sendPackets("----------------------------------------------------");

        } catch (Exception e) {
        pc.sendPackets("用法: .周邊封禁 [範圍] [封禁原因代碼]");
        }
        }

private void spawnNotifyOnOff(L1PcInstance gm, String param) {
        try {
        StringTokenizer st = new StringTokenizer(param);
        String on = st.nextToken();
        if (on.equalsIgnoreCase("開啟")) {
        gm.setBossNotify(true);
        gm.sendPackets("老闆通知: 開啟 (老闆召喚和通知已啟用)");
        } else if (on.equalsIgnoreCase("關閉")) {
        gm.setBossNotify(false);
        gm.sendPackets("老闆通知: 關閉 (老闆召喚和通知已禁用)");
        }
        } catch (Exception e) {
        gm.sendPackets("請使用 .老闆通知 [開啟, 關閉] 來設置。當前狀態：" + (gm.isBossNotify() ? "開啟" : "關閉") + "。");
        }
        }

public void execute1(L1PcInstance pc, String cmdName, String arg) {
        try {
        StringTokenizer stringtokenizer = new StringTokenizer(arg);
        int count = Integer.parseInt(stringtokenizer.nextToken());
        L1ItemInstance adena = pc.getInventory().storeItem(L1ItemId.ADENA, count);

        if (adena != null) {
        pc.sendPackets(new StringBuilder().append(count).append(" 個金幣已創建。").toString());
        }
        } catch (Exception e) {
        pc.sendPackets(new StringBuilder().append("請使用 .金幣 [數量] 來輸入。").toString());
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
        pc.sendPackets("找不到指定的物品。");
        return;
        }
        }

        // 僅翻譯至此，若有後續代碼需要請補充

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
        pc.sendPackets(new S_SystemMessage("\fY物品:[\aA" + item.getLogName() + "\fY] 物品ID:[\aA" + itemid + "\fY] 數量:[\aA" + count + "\fY] \aG已創建"));
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
        pc.sendPackets(new S_SystemMessage("\fY物品:[\aA" + item.getLogName() + "\fY] 物品ID:[\aA" + itemid + "\fY] 數量:[\aA" + count + "\fY] \aG已創建"));
        ;
        }
        } else {
        pc.sendPackets("創建物品失敗。");
        }


        } catch (Exception e) {
        pc.sendPackets(new S_SystemMessage(".物品 [物品名稱或ID] [數量] [強化等級] [屬性1~20] [確認0~1]"));

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
        pc.sendPackets("" + st.nextToken() + " 找不到物品. ");
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
        pc.sendPackets("\fY物品:[\aA" + item.getLogName() + "\fY] 物品ID:[\aA" + itemid + "\fY] 強化等級:[\aA" + enchant + "\fY] \aG已創建");
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
        pc.sendPackets("\fY物品:[\aA" + item.getLogName() + "\fY] 物品ID:[\aA" + itemid + "\fY] 強化等級:[\aA" + enchant + "\fY] \aG已創建");
        }
        }
        } else {
        pc.sendPackets("指定 ID 的物品不存在");
        }
        } catch (Exception e) {
        pc.sendPackets(".物品 [名稱] [數量] [強化] [屬性1~20] [確認0~1] ");
        }
        }

private void huntEvent(L1PcInstance gm, String param) {
        try {
        StringTokenizer st = new StringTokenizer(param);
        String type = st.nextToken();
        if (type.equalsIgnoreCase("開始")) {
        Config.ServerAdSetting.HuntEvent = true;
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "狩獵活動開始了。請捕獵亞丁世界的鹿和野豬！"));
        L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "狩獵活動開始了。請捕獵鹿和野豬！"));
        } else if (type.equalsIgnoreCase("結束")) {
        Config.ServerAdSetting.HuntEvent = false;
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "狩獵活動已結束。"));
        L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "狩獵活動已結束。"));
        }
        } catch (Exception e) {
        gm.sendPackets(".狩獵 [開始 and 結束]");
        }
        }

private void Event(L1PcInstance gm, String param) {
        try {
        StringTokenizer st = new StringTokenizer(param);
        String event = st.nextToken();
        String time1 = st.nextToken();
        int time = Integer.parseInt(time1);

        if (event.equalsIgnoreCase("龍獵人")) {  // "드래곤헌터" 翻譯為 "龍獵人"
        L1SpawnUtil.Gmspawn(7320159, 33441, 32813, (short) 4, 6, time * 60 * 60 * 1000);
        gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "龍獵人祝福活動已開始，稍後 (" + time + ") 小時內將出現在奇岩村莊。"));
        L1World.getInstance().broadcastServerMessage("稍後 (" + time + ") 小時內將出現在奇岩村莊。");

        } else if (event.equalsIgnoreCase("特級勇士1")) {  // "특급용사1" 翻譯為 "特級勇士1"
        L1SpawnUtil.Gmspawn(526, 33447, 32793, (short) 4, 5, time * 60 * 60 * 1000); // 奇岩
        gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "特級勇士活動(1)已開始。稍後 (" + time + ") 小時內將出現在奇岩村莊。"));
        L1World.getInstance().broadcastServerMessage("稍後 (" + time + ") 小時內將出現在奇岩村莊。");

        } else if (event.equalsIgnoreCase("特級勇士2")) {  // "특급용사2" 翻譯為 "特級勇士2"
        L1SpawnUtil.Gmspawn(529, 33445, 32791, (short) 4, 5, time * 60 * 60 * 1000); // 奇岩
        gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "特級勇士活動(2)已開始。稍後 (" + time + ") 小時內將出現在奇岩村莊。"));
        L1World.getInstance().broadcastServerMessage("稍後 (" + time + ") 小時內將出現在奇岩村莊。");

        } else if (event.equalsIgnoreCase("特級勇士3")) {  // "특급용사3" 翻譯為 "特級勇士3"
        L1SpawnUtil.Gmspawn(530, 33449, 32795, (short) 4, 5, time * 60 * 60 * 1000); // 奇岩
        gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "特級勇士活動(3)已開始。稍後 (" + time + ") 小時內將出現在奇岩村莊。"));
        L1World.getInstance().broadcastServerMessage("稍後 (" + time + ") 小時內將出現在奇岩村莊。");

        } else if (event.equalsIgnoreCase("特級勇士4")) {  // "특급용사4" 翻譯為 "特級勇士4"
        L1SpawnUtil.Gmspawn(531, 33444, 32797, (short) 4, 5, time * 60 * 60 * 1000); // 奇岩
        gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "特級勇士活動(4)已開始。稍後 (" + time + ") 小時內將出現在奇岩村莊。"));
        L1World.getInstance().broadcastServerMessage("稍後 (" + time + ") 小時內將出現在奇岩村莊。");

        } catch (Exception e) {
        gm.sendPackets(".活動 [龍獵人|特級勇士1|特級勇士2|特級勇士3|特級勇士4] [時間]");
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

        L1World.getInstance().broadcastServerMessage("\aA召喚活動: 稍後美蒂斯的近衛部隊將會出現。");

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

        L1World.getInstance().broadcastServerMessage("\aA召喚活動：稍後美蒂斯的近衛部隊將會出現。");

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

        L1World.getInstance().broadcastServerMessage("\aA召喚活動：最終美蒂斯的近衛部隊將會出現。");
        }
        } catch (Exception e) {
        gm.sendPackets("[.召喚活動] [活動名]");
        gm.sendPackets("[召喚活動名]: 部隊1/部隊2/部隊3/部隊4/部隊5");
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
        Account.ban(player.getAccountName(), S_LoginResult.BANNED_REASON_NOMANNER); // 계정을
        // BAN시킨다.
        player.logout();
        player.getNetConnection().kick();
        for (int i = 1; i <= 255; i++) {
        iptable.banIp(banIpArr[0] + "." + banIpArr[1] + "." + banIpArr[2] + "." + i);
        }

        pc.sendPackets("登入IP位址: " + banIpArr[0] + "." + banIpArr[1] + "." + banIpArr[2] + ".1~255 大範圍封鎖.");
        pc.sendPackets("----------------------------------------------------");
        }
        } catch (Exception e) {
        pc.sendPackets(".廣域封禁 [範圍]");
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
        pc.sendPackets(String.format("%s 無法找到。", name));
        return;
        }
        if (target.hasSkillEffect(L1SkillId.USER_WANTED3)) {
        pc.sendPackets(String.format("%s 已經是三級通緝狀態。", name));
        return;
        }
        if (target.isGm()) {
        pc.sendPackets("無法對管理者進行通緝。");
        return;
        }
        if (target.get_Wanted_Level() == 0) {
        if (!pc.getInventory().checkItem(40308, price1)) {
        pc.sendPackets("金幣不足 (" + price1 + ")");
        return;
        }
        } else if (target.get_Wanted_Level() == 1) {
        if (!pc.getInventory().checkItem(40308, price2)) {
        pc.sendPackets("金幣不足 (" + price2 + ")");
        return;
        }
        } else if (target.get_Wanted_Level() == 2) {
        if (!pc.getInventory().checkItem(40308, price3)) {
        pc.sendPackets("金幣不足 (" + price3 + ")");
        return;
        }
        }

        if (target.get_Wanted_Level() == 0) {
        String message = String.format("%s 對 %s 發出了通緝令。(1級)", pc.getName(), target.getName());
        pc.sendPackets(message);
        target.sendPackets(message);
        target.setSkillEffect(L1SkillId.USER_WANTED1, -1);
        target.add_Wanted_Level();
        target.doWanted(true, false);
        pc.getInventory().consumeItem(40308, price1);
        } else if (target.hasSkillEffect(L1SkillId.USER_WANTED1)) {
        String message = String.format("%s 對 %s 發出了通緝令。(2級)", pc.getName(), target.getName());
        pc.sendPackets(message);
        target.sendPackets(message);
        target.removeSkillEffect(L1SkillId.USER_WANTED1);
        target.setSkillEffect(L1SkillId.USER_WANTED2, -1);
        target.add_Wanted_Level();
        target.doWanted(true, false);
        pc.getInventory().consumeItem(40308, price2);
        } else if (target.hasSkillEffect(L1SkillId.USER_WANTED2)) {
        String message = String.format("%s 對 %s 發出了通緝令。(3級)", pc.getName(), target.getName());
        pc.sendPackets(message);
        target.sendPackets(message);
        target.removeSkillEffect(L1SkillId.USER_WANTED2);
        target.setSkillEffect(L1SkillId.USER_WANTED3, -1);
        target.add_Wanted_Level();
        target.doWanted(true, false);
        pc.getInventory().consumeItem(40308, price3);
        }
        } catch (Exception e) {
        pc.sendPackets(".通緝 [角色名稱]");
        pc.sendPackets("效果階段累積：近距離傷害減少3，命中減少3，遠距離傷害減少3，命中減少3，SP減少3，減傷減少3，AC減少3");
        pc.sendPackets("1級： " + price1 + "，2級： " + price2 + "，3級： " + price3);
        }
        }

private void serversave(L1PcInstance pc) {
        Saveserver();  // 伺服器保存方法的調用
        pc.sendPackets("伺服器保存已完成。");
        }

/** 伺服器保存 */
private void Saveserver() {
        /** 調用所有玩家 */
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
        /** 異常情況下僅保存背包 */
        player.saveInventory();
        System.out.println("保存命令錯誤（僅保存了背包）： " + ex);
        }
        }
        }

private void privateShop(L1PcInstance pc) {
        try {
        if (!pc.isPrivateShop()) {
        pc.sendPackets("只能在私人商店狀態下使用。");
        return;
        }

        GameClient client = pc.getNetConnection();
        pc.setNetConnection(null);
        // 停止血量和魔力的自動恢復
        // pc.stopHpMpRegeneration();
        pc.set無人商店(true);
        try {
        pc.save();
        pc.saveInventory();
        } catch (Exception e) {
        }
        client.setActiveChar(null);
        client.setStatus2(MJClientStatus.CLNT_STS_AUTHLOGIN);
        client.sendPacket(new S_Unknown2(1)); // 為重新連接按鈕修改結構 // Episode U

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
        L1Clan targetClan = L1World.getInstance().getClan(target.getClanid());
        L1House pobyhouse = HouseTable.getInstance().getHouseTable(pobyhouseid);
        targetClan.setHouseId(pobyhouseid);
        ClanTable.getInstance().updateClan(targetClan);
        pc.sendPackets(target.getClanname() + " 血盟已經獲得了 " + pobyhouse.getHouseName() + " 號房屋。");
        for (L1PcInstance tc : targetClan.getOnlineClanMember()) {
        tc.sendPackets("從梅蒂斯處獲得了 " + pobyhouse.getHouseName() + " 號房屋。");
        }
        } else {
        pc.sendPackets(target.getName() + " 不屬於任何血盟。");
        }
        } else {
        pc.sendPackets(new S_ServerMessage(73, pobyname));
        }
        } catch (Exception e) {
        pc.sendPackets(".發放房屋 [成員名稱] [房屋編號]");
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
        gm.sendPackets(String.format("攻城時間已更改為 %s。", formatter.format(cal.getTime())));
        gm.sendPackets(param + " 分鐘後攻城戰開始。");
        formatter = null;
        } catch (Exception e) {
        gm.sendPackets("使用方法: .攻城開始 [城堡名稱兩個字] [分鐘]");
        }
        }

        rivate void castleWarExit(L1PcInstance gm, String param) {
        try {
        StringTokenizer tok = new StringTokenizer(param);
        String name = tok.nextToken();
        MJCastleWar war = MJCastleWarBusiness.getInstance().findWar(name);
        if (war == null)
        throw new Exception();

        war.close();
        gm.sendPackets(name + " 攻城戰已結束。");
        } catch (Exception e) {
        gm.sendPackets("使用方法: .攻城結束 [城堡名稱兩個字]");
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
        int range = 3; // 當前周圍3格
        for (L1PcInstance Targetpc : L1World.getInstance().getVisiblePlayer(gm, range)) {
        if (gm.getName().equals(Targetpc.getName())) {
        continue;
        }
        if (Targetpc.getParty() != null) {
        continue;
        } // 排除已有隊伍的玩家
        if (Targetpc.isPrivateShop()) {
        continue;
        } // 排除無人商店
        party.addMember(Targetpc);
        gm.sendPackets(Targetpc.getName() + " 已加入我的隊伍。");
        }
        gm.sendPackets(range + "格內的玩家已加入我的隊伍。");
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
        gm.sendPackets("螢幕內的玩家已加入我的隊伍。");
        } else if (cmd.equals("全部")) {
        L1Party party = new L1Party();
        if (gm.getParty() == null) {
        party.addMember(gm);
        } else {
        party = gm.getParty();
        }
        int range = 3;// 현재주변3칸
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
        gm.sendPackets(Targetpc.getName() + "已加入我的隊伍。");
        }
        gm.sendPackets(range + "範圍內的用戶已加入我的隊伍。");
        } else if (cmd.equals("參加")) {
        String TargetpcName = tok.nextToken();
        L1PcInstance TargetPc = L1World.getInstance().getPlayer(TargetpcName);
        if (TargetPc.getParty() != null) {
        gm.sendPackets(TargetPc.getName() + "沒有隊伍。");
        } else {
        TargetPc.getParty().addMember(gm);
        gm.sendPackets("已加入" + TargetPc.getName() + "的隊伍。");
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
        gm.sendPackets(TargetPc.getName() + "已被強制加入我的隊伍。");
        } else if (cmd.equals("強制邀請")) { // 強制邀請
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
        gm.sendPackets(Targetpc.getName() + "已加入我的隊伍。");
        }
        gm.sendPackets("已強制將在線的用戶加入我的隊伍。");
        } else if (cmd.equals("隊長")) { // 隊長
        if (gm.getParty() == null) {
        gm.sendPackets("當前沒有參加任何隊伍。");
        } else {
        gm.getParty().passLeader(gm);
        gm.sendPackets("已經改變隊長。");
        }
        }
        } catch (Exception e) {
        gm.sendPackets(".隊伍 [周邊,畫面,全部,參加 (用戶名)]");
        gm.sendPackets(".隊伍 [邀請 (用戶名),強制邀請,隊長]");
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

private int 最小值(int itemid) {
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

private int 最大值(int itemid) {
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

        Server.createServer().changePort(port);

        pc.sendPackets("端口: 端口已更改為 [" + port + "]號。");
        pc.sendPackets("伺服器內部遊戲端口已更改！現有用戶將保留，但新用戶無法連接。");
        pc.sendPackets("若要引入新用戶，請將連接端口更改為 [" + port + "]號。");
        } catch (Exception e) {
        pc.sendPackets("警告: 輸入格式不正確。");
        }
        }

private void 檢查商店(L1PcInstance gm) {
        try {
        ArrayList<Integer> itemids = new ArrayList<Integer>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int cnt;
        Iterator<Integer> iterator;
        try {
        con = L1DatabaseFactory.getInstance().getConnection();
        pstm = con.prepareStatement("SELECT item_id FROM shop");
        rs = pstm.executeQuery();
        while (rs.next()) {
        int itemId = rs.getInt("item_id");
        if (!itemids.contains(itemId)) {
        itemids.add(itemId);
        }
        }
        cnt = 0;
        for (iterator = itemids.iterator(); iterator.hasNext();) {
        int itemid = iterator.next();
        int 購買最低價 = 最小值(itemid);
        int 銷售最高價 = 最大值(itemid);
        if (購買最低價 != 0 && 購買最低價 < 銷售最高價) {
        gm.sendPackets(new S_ChatPacket(gm, "檢測到異常! [物品 " + itemid + " : [購買價 " + 購買最低價 + "] [銷售價 " + 銷售最高價 + "]"));
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
        else if (s.equalsIgnoreCase("精神"))
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
        pc.sendPackets(String.format(".屬性 [力量/敏捷/體質/智力/精神/魅力] [可提升]剩餘屬性%d", pc.remainBonusStats()));
        }
        }

private void 坐標(L1PcInstance pc) {
        try {
        long curtime = System.currentTimeMillis() / 1000;
        if (pc.getQuizTime2() + 5 > curtime) {
        long time = (pc.getQuizTime2() + 5) - curtime;
        pc.sendPackets(new S_SystemMessage(time + "幾秒內即可使用."));
        return;
        }
        Updator.exec("UPDATE characters SET LocX=33432,LocY=32807,MapID=4 WHERE account_name=? and MapID not in (38,5001,99,997,5166,39,34,701,2000)", new Handler() {
@Override
public void handle(PreparedStatement pstm) throws Exception {
        pstm.setString(1, pc.getAccountName());
        }
        });
        pc.sendPackets(new S_SystemMessage("該帳戶的所有角色的坐標已更改為奇岩。"));
        pc.setQuizTime(curtime);
        } catch (Exception e) {
        }
        }

private void standBy(L1PcInstance gm, String param) {
        try {
        StringTokenizer st = new StringTokenizer(param);
        String status = st.nextToken();
        if (status.equalsIgnoreCase("開啟")) {
        if (Config.Login.StandbyServer) {
        gm.sendPackets("已經進入待機狀態.");
        return;
        }
        Config.Login.StandbyServer = true;
        L1World.getInstance().broadcastPacketToAll(new S_ChatPacket("伺服器進入待機狀態。部分功能已被禁用", Opcodes.S_MESSAGE));
        } else if (status.equalsIgnoreCase("關閉")) {
        if (!Config.Login.StandbyServer) {
        gm.sendPackets("不是待機狀態.");
        return;
        }

        GeneralThreadPool.getInstance().execute(new Runnable() {
@Override
public void run() {
        // Config.load();
        Config.Login.StandbyServer = false;

        S_PacketBox grn = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "伺服器已正常開啟。非常感謝您的耐心等待.");
        L1World.getInstance().broadcastServerMessage("\\aH伺服器已正常開啟。非常感謝您的耐心等待.");

        L1World.getInstance().getAllPlayerStream().filter(pc -> pc != null).forEach(pc -> {
        if (pc.getAI() != null) {
        if (pc.getAI().getBotType() == MJBotType.ILLUSION || pc.getAI().getBotType() == MJBotType.FISH || pc.getAI().getBotType() == MJBotType.WANDER)
        pc.getAI().setRandLawful();
        } else {
        pc.sendPackets(grn, false);
        }
        });
        grn.clear();
        }
        });
        }
        } catch (Exception eee) {
        gm.sendPackets("進入 .開啟待機【on off】.");
        gm.sendPackets("On - 切換至開啟待機狀態 |關閉 - 以正常模式開始遊戲");
        }
        }

private void 가비지정리(L1PcInstance gm) {
        gm.sendPackets("\\f3垃圾清理（記憶體）已組織完成。 （出現瞬時滯後）");
        System.out.println("強制垃圾清理（記憶體）處理正在進行中.");
        System.gc();
        System.out.println("垃圾清理（記憶體）已完成.");
        }

private void hold(L1PcInstance gm, String pcName) {
        try {
        L1PcInstance target = L1World.getInstance().getPlayer(pcName);
        if (target != null) {
        holdnow(gm, target);
        } else {
        gm.sendPackets("沒有這樣的角色名稱.");
        }
        } catch (Exception e) {
        gm.sendPackets("請輸入您的.監獄 [角色名稱].");
        }
        }

private void holdnow(L1PcInstance gm, L1PcInstance target) {
        try {
        // L1Teleport.teleport(target, 32736, 32799, (short) 34, 5, true);
        // L1Teleport.teleport(target, 32835, 32782, (short) 701, 5, true);
        target.start_teleport(32835, 32782, 701, target.getHeading(), 18339, false, true);
        gm.sendPackets((new StringBuilder()).append(target.getName()).append(" 傳送監獄.").toString());
        target.sendPackets("我被關進監獄.");
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
        if (target != null) { // 타겟
        // L1Teleport.teleport(target, 33437, 32812, (short) 4, 5,
        // true);
        target.start_teleport(33437, 32812, 4, target.getHeading(), 18339, true, true);
        } else {
        gm.sendPackets("這是目前未連線的使用者 ID.");
        }
        } catch (Exception e) {
        gm.sendPackets("請輸入.Go（要傳送的角色名稱）.");
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
        gm.sendPackets(".搜索 [0~5] [name] 請輸入。");
        gm.sendPackets("0=雜物, 1=武器, 2=盔甲, 3=NPC, 4=變身, 5=NPC(gfxid)");
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
        gm.sendPackets(String.format("總共進行了 [%d] 次搜尋.", i));
        }
        });
        } catch (Exception e) {
        }
        }

private void redo(L1PcInstance pc, String arg) {
        try {
        String lastCmd = _lastCommands.get(pc.getId());
        if (arg.isEmpty()) {
        pc.sendPackets("命令 " + lastCmd + " 重新執行。");
        handleCommands(pc, lastCmd);
        } else {
        StringTokenizer token = new StringTokenizer(lastCmd);
        String cmd = token.nextToken() + " " + arg;
        pc.sendPackets("命令 " + cmd + " 重新執行中。");
        handleCommands(pc, cmd);
        }
        } catch (Exception e) {
        e.printStackTrace();
        pc.sendPackets(".重新執行命令錯誤");
        }
        }

private void unprison(L1PcInstance pc, String param) {
        try {
        // 該變量保存了玩家對象，通過玩家名稱在世界實例中查找
        L1PcInstance target = L1World.getInstance().getPlayer(param);

        // 如果找到目標玩家，則執行解除監禁操作
        if (target != null) {
        unprisonnow(pc, target);
        } else {
        // 如果沒有找到目標玩家，發送提示信息給當前玩家
        pc.sendPackets(".村莊角色名稱");
        pc.sendPackets("沒有這樣的角色名稱。");
        }
        } catch (Exception e) {
        // 捕捉異常並發送提示信息給當前玩家
        pc.sendPackets(".村莊角色名稱");
        }
        }

private void unprisonnow(L1PcInstance gm, L1PcInstance target) {
        try {
        int i = 33437;
        int j = 32803;
        short k = 4;
        // L1Teleport.teleport(target, i, j, k, 5, false);
        target.start_teleport(i, j, k, 5, 18339, false, true);
        gm.sendPackets((new StringBuilder()).append(target.getName()).append(" 將您移動到村莊.").toString());
        } catch (Exception e) {
        _log.log(Level.SEVERE, "", e);
        }
        }

private void unprison2(L1PcInstance pc, String param) {
        try {
        // 該變量保存了玩家對象，通過玩家名稱在世界實例中查找
        L1PcInstance target = L1World.getInstance().getPlayer(param);

        // 如果找到目標玩家，則執行解除監禁操作
        if (target != null) {
        unprisonnow2(pc, target);
        } else {
        // 如果沒有找到目標玩家，發送提示信息給當前玩家
        pc.sendPackets(".隱藏 角色名稱");
        pc.sendPackets("沒有這樣的角色名稱。");
        }
        } catch (Exception e) {
        // 捕捉異常並發送提示信息給當前玩家
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
        gm.sendPackets((new StringBuilder()).append(target.getName()).append(" 已移動到隱藏地點。").toString());
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
        gm.sendPackets("已解除該角色的禁言。");
        }
        } catch (Exception e) {
        gm.sendPackets("請輸入 .解除禁言 角色名稱。");
        }
        }

private void tell(L1PcInstance gm) {
        try {
        // gm.sendPackets(new S_PacketBox(S_PacketBox.공격가능거리, gm, gm.getWeapon()), true);
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
        gm.sendPackets("請在1-99的範圍內指定");
        return;
        }
        target.set_exp(ExpTable.getExpByLevel(level));
        gm.sendPackets(target.getName() + "的等級已更改！請使用 .檢查 [角色名稱] 確認");
        } catch (Exception e) {
        gm.sendPackets("請輸入 .升級 [角色名稱] [等級]");
        }
        }

private void NoDelayUser(L1PcInstance pc) {

        int SearchCount = 0;

        pc.sendPackets("----------------------------------------------------");

        for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
        if (player.getNoDelayTime() > 0 || player.getSpeedHackCount() > 10) {
        String type = player.getNoDelayTime() > 0 ? "(無交易)" : "";
        type += player.getSpeedHackCount() > 10 ? "(偵測黑客)" : "";

        pc.sendPackets("Lv." + player.getLevel() + ", " + player.getName() + " " + type);
        SearchCount++;
        }

        }

        pc.sendPackets(SearchCount + "發現無交易使用者！");

        pc.sendPackets("----------------------------------------------------");

        }

private void 探尋禮物(L1PcInstance gm, String param) {
        // TODO 自動生成的方法存根
        try {
        StringTokenizer st = new StringTokenizer(param);
        String 玩家名稱 = st.nextToken(); // 玩家名稱
        int id = Integer.parseInt(st.nextToken()); // 分數或點數

        L1PcInstance 使用者 = L1World.getInstance().getPlayer(玩家名稱); // 修正參數名稱
        if (使用者 != null) {
        // 增加使用者的探尋禮物點數
        使用者.getNetConnection().getAccount().tam_point += id;
        使用者.getNetConnection().getAccount().updateTam();

        try {
        // 向使用者發送更新點數的數據包
        使用者.sendPackets(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, 使用者.getNetConnection()), true);
        } catch (Exception e) {
        // 捕捉內部異常，不做處理
        }

        // 向管理員發送成功信息
        gm.sendPackets(使用者.getName() + " 發放給 探尋禮物 " + id + " 個。");

        // 向使用者發送系統信息
        Message.getInstance().get_system_message(使用者, "A給您發放了探尋禮物 'G" + id + "A' 個。");
        } else {
        // 如果找不到使用者，向管理員發送錯誤信息
        gm.sendPackets("該用戶不存在。");
        }
        } catch (Exception e) {
        // 捕捉所有異常，不做處理
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
        gm.sendPackets(".全體召喚 命令錯誤");
        }
        }

private void 召喚bot(L1PcInstance gm, String param) {
        try {
        StringTokenizer st = new StringTokenizer(param);
        String 玩家名稱 = st.nextToken();

        L1PcInstance target = L1World.getInstance().getPlayer(玩家名稱);

        if (target != null) {
        target.set_MassTel(true);
        target.start_teleport(gm.getX(), gm.getY(), gm.getMapId(), target.getHeading(), 18339, true, true);

        gm.sendPackets("已被召喚到遊戲管理員。");
        } else {
        gm.sendPackets("角色名稱不存在。");
        }
        } catch (Exception e) {
        gm.sendPackets("用法: .召喚bot 角色名稱");
        }
        }

private void recallnow(L1PcInstance gm, L1PcInstance target) {
        try {
        // L1Teleport.teleportToTargetFront(target, gm, 2 , 0);
        L1Teleport.getInstance().teleportToTargetFront(gm, target, 2, true);
        // target.sendPackets("被遊戲管理員召喚."));
        } catch (Exception e) {
        _log.log(Level.SEVERE, "", e);
        }
        }

private void ShopKick(L1PcInstance gm, String param) {
        try {
        L1PcInstance target = L1World.getInstance().getPlayer(param);
        if (target != null) {
        gm.sendPackets((new StringBuilder()).append(target.getName()).append(" 已被踢出。").toString());
        GameServer.disconnectChar(target);
        } else {
        gm.sendPackets("世界中不存在該角色名稱。");
        }
        } catch (Exception e) {
        gm.sendPackets("用法: .商店踢出 角色名稱");
        }
        }

private void icon(L1PcInstance pc, String param) {
        try {
// StringTokenizer st = new StringTokenizer(param);
// int iconId = Integer.parseInt(st.nextToken(), 10);
        /*
         * pc.sendPackets(new S_EtcPacket(3)); // 倒計時
         * pc.sendPackets(new S_EtcPacket(2)); // 左下角顯示
         * pc.sendPackets(new S_EtcPacket(4));
         * pc.sendPackets(new S_EtcPacket(5)); // 時間刪除
         * pc.sendPackets(new S_PacketBox(S_PacketBox.MINIGAME_LIST, iconId));
         */
        SC_EVENT_COUNTDOWN_NOTI_PACKET.send(pc, 10, "111");
// pc.sendPackets(MJPacketFactory.createTime(10));
// pc.sendPackets(MJPacketFactory.create(MJPacketFactory.MSPF_IDX_OFFTIME));
        } catch (Exception exception) {
        pc.sendPackets("請輸入 .icon [actid]。");
        }
        }

private void chainfo(L1PcInstance gm, String param) {
        try {
        StringTokenizer stringtokenizer = new StringTokenizer(param);
        String s = stringtokenizer.nextToken();
        gm.sendPackets(new S_Chainfo(1, s));
        } catch (Exception exception) {
        gm.sendPackets(new S_SystemMessage("請輸入: .檢查角色 角色名稱"));
        }
        }

private void 清理娃娃(L1PcInstance gm) {
        int count = 0;
        int ccount = 0;

        for (Object obj : L1World.getInstance().getAllObjects()) {
        if (obj instanceof L1DollInstance) {
        L1DollInstance 娃娃 = (L1DollInstance) obj;
        if (娃娃.getMaster() == null) {
        count++;
        娃娃.deleteMe();
        } else if (((L1PcInstance) 娃娃.getMaster()).getNetConnection() == null) {
        ccount++;
        娃娃.deleteMe();
        }
        }
        }
        gm.sendPackets("清理的娃娃數量 - 無主: " + count + " 主人連線中斷: " + ccount);
        }

private void 更新角色平衡(L1PcInstance pc, String param) {
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
        pstm = con.prepareStatement("UPDATE characters SET AddDamage = ?, AddDamageRate = ?, AddReduction = ?, AddReductionRate = ? WHERE char_name = ?");
        pstm.setInt(++i, addDamage);
        pstm.setInt(++i, addDamageRate);
        pstm.setInt(++i, addReduction);
        pstm.setInt(++i, addReductionRate);
        pstm.setString(++i, charName);
        pstm.executeUpdate();
        }

        } catch (Exception e) {
        pc.sendPackets("用法：.平衡 [角色名] [增加傷害] [增加傷害率] [減傷] [減傷率]");
        } finally {
        SQLUtil.close(pstm);
        SQLUtil.close(con);
        }
        }

private void 添加帳號(L1PcInstance gm, String account, String passwd, int ip, int ip2) {
        java.sql.Connection con = null;
        PreparedStatement statement = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
        String login = null;
        String password = null;
        con = L1DatabaseFactory.getInstance().getConnection();
        password = passwd;
        statement = con.prepareStatement("SELECT * FROM accounts WHERE login LIKE ?");
        statement.setString(1, account);
        rs = statement.executeQuery();

        if (rs.next()) {
        login = rs.getString("login");
        }

        if (login != null) {
        gm.sendPackets("該帳號已存在。");
        return;
        } else {
        String sqlstr = "INSERT INTO accounts (login, password, lastactive, access_level, ip, host, banned, charslot, gamepassword, notice, fatigue_point) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        pstm = con.prepareStatement(sqlstr);
        pstm.setString(1, account);
        pstm.setString(2, password);
        pstm.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        pstm.setInt(4, 0);
        pstm.setInt(5, ip);
        pstm.setInt(6, ip2);
        pstm.setInt(7, 0);
        pstm.setInt(8, 6);
        pstm.setInt(9, 0);
        pstm.setInt(10, 0);
        pstm.setInt(11, 0);
        pstm.execute();
        gm.sendPackets("帳號新增完成。");
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

private static boolean isDigitAlpha(String str) {
        boolean check = true;
        for (int i = 0; i < str.length(); i++) {
        if (!Character.isDigit(str.charAt(i)) // 不是數字
        && !Character.isUpperCase(str.charAt(i)) // 不是大寫字母
        && !Character.isLowerCase(str.charAt(i))) { // 不是小寫字母
        check = false;
        break;
        }
        }
        return check;
        }

private void reloadHouse(L1PcInstance gm, String cmd) {
        try {
        HouseTable.reload();
        gm.sendPackets("房屋拍賣時間已重新更新。");
        } catch (Exception e) {
        gm.sendPackets("請輸入 .更新房屋拍賣時間。");
        }
        }

// 刪除背包物品
private void 刪除背包物品(L1PcInstance pc, String param) {
        try {
        for (L1ItemInstance item : pc.getInventory().getItems()) {
        if (!item.isEquipped()) {
        pc.getInventory().removeItem(item);
        }
        }
        } catch (Exception e) {
        pc.sendPackets("錯誤：請輸入 .刪除背包物品");
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
        user.sendPackets("請輸入 .刪除用戶物品 [上線中的角色名].");
        }
        }

private void 添加帳號(L1PcInstance gm, String param) {
        try {
        StringTokenizer tok = new StringTokenizer(param);
        String user = tok.nextToken();
        String passwd = tok.nextToken();

        if (user.length() < 4) {
        gm.sendPackets("您輸入的帳號名稱太短。");
        gm.sendPackets("請至少輸入 4 個字符。");
        return;
        }
        if (passwd.length() < 4) {
        gm.sendPackets("您輸入的密碼太短。");
        gm.sendPackets("請至少輸入 4 個字符。");
        return;
        }

        if (passwd.length() > 12) {
        gm.sendPackets("您輸入的密碼太長。");
        gm.sendPackets("請輸入不超過 12 個字符。");
        return;
        }

        if (!isDigitAlpha(passwd)) {
        gm.sendPackets("密碼包含不允許的字符。");
        return;
        }
        ip++;
        AddAccount(gm, user, passwd, ip, ip);
        } catch (Exception e) {
        gm.sendPackets("請輸入 .添加帳號 [帳號] [密碼]。");
        }
        }

private void 全體贈送(L1PcInstance gm, String param) {
        try {
        StringTokenizer st = new StringTokenizer(param);
        int itemid = Integer.parseInt(st.nextToken(), 10);
        int enchant = Integer.parseInt(st.nextToken(), 10);
        int count = Integer.parseInt(st.nextToken(), 10);
        Collection<L1PcInstance> players = L1World.getInstance().getAllPlayers();

        for (L1PcInstance target : players) {
        if (target == null) continue;
        if (!target.isGhost() && !target.isPrivateShop()) {
        L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
        if (item != null) {
        item.setCount(count);
        item.setEnchantLevel(enchant);
        if (target.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
        target.getInventory().storeItem(item, true);
        }
        target.sendPackets(new S_GMHtml("全體贈送:" + target.getName(), "物品:" + item.getLogName() + " 數量:" + count + " 獲得"));
        target.sendPackets("全體贈送: " + item.getLogName() + " " + count + "個，請檢查背包");
        target.sendPackets(new S_SkillSound(target.getId(), 1091)); // 鴿子動作
        target.sendPackets(new S_SkillSound(target.getId(), 4856)); // 心形動作
        }
        }
        }
        } catch (Exception exception) {
        gm.sendPackets("請輸入 .全體贈送 [物品ID] [附魔] [數量]");
        }
        }

private void 恢復經驗值(L1PcInstance gm, String param) {
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

        target.addExp(exp);
        target.save();
        target.saveInventory();

        gm.sendPackets("該角色經驗值已增加");
        } else {
        gm.sendPackets("該角色目前不在線。");
        }
        } catch (Exception e) {
        gm.sendPackets("請輸入 .恢復經驗值 [角色名稱]");
        }
        }

// .帳號 -----------------------------------------------------------------
// 檢查相同帳號下的角色
private void 檢查帳號角色(L1PcInstance gm, String param) {
        try {
        StringTokenizer tok = new StringTokenizer(param);
        String name = tok.nextToken();
        檢查帳號角色詳細(gm, name);
        } catch (Exception e) {
        gm.sendPackets("請輸入 .帳號 [帳號名稱]");
        }
        }

private void 檢查帳號角色詳細(L1PcInstance gm, String param) {
        java.sql.Connection con0 = null; // 為了通過名字查找 objid
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
        String s_type = null; // 新增
        int count = 0;
        int count0 = 0;

        con0 = L1DatabaseFactory.getInstance().getConnection();
        statement0 = con0.prepareStatement("SELECT account_name, Clanname FROM characters WHERE char_name = ?");
        statement0.setString(1, s_name);
        rs0 = statement0.executeQuery();
        while (rs0.next()) {
        s_account = rs0.getString("account_name");
        s_clan = rs0.getString("Clanname");
        gm.sendPackets("\aD------------------------------------------");
        gm.sendPackets("\aE角色: " + s_name + " (" + s_account + ") 公會: " + s_clan);
        count0++;
        }

        if (s_account != null) {
        con = L1DatabaseFactory.getInstance().getConnection();
        statement = con.prepareStatement("SELECT char_name, level, Clanname, BonusStatus, OnlineStatus, MaxHp, MaxMp, Type FROM characters WHERE account_name = ?");
        statement.setString(1, s_account);
        rs = statement.executeQuery();
        gm.sendPackets("\aD------------------------------------------");
        while (rs.next()) {
        s_name = rs.getString("char_name");
        s_level = rs.getString("level");
        s_clan = rs.getString("Clanname");
        s_bonus = rs.getString("BonusStatus");
        s_online = rs.getString("OnlineStatus");
        s_hp = rs.getString("MaxHp");
        s_mp = rs.getString("MaxMp");
        s_type = rs.getString("Type");

        gm.sendPackets("連線狀態: [" + s_online + "] 等級: " + s_level + " 角色: [" + s_name + "] 類型: " + s_type);
        count++;
        }
        gm.sendPackets("\aF0君主 1騎士 2妖精 3法師 4黑妖 5龍騎 6幻術 7戰士 8劍士 9黃金槍騎 10全體");
        gm.sendPackets("\aD------------------------------------------");
        }

        if (count == 0) {
        gm.sendPackets("沒有找到與該帳號相關的角色。");
        }
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

        if (type.equals("開啟")) {
        Config.ServerAdSetting.SERVERPVPSETTING = true;
        Config.setParameterValue("AltNonPvP", "true");
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "從現在開始，PvP將正常進行。"));
        } else if (type.equals("關閉")) {
        Config.ServerAdSetting.SERVERPVPSETTING = false;
        Config.setParameterValue("AltNonPvP", "false");
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "從現在開始，一段時間內PvP將無法進行。"));
        }

        } catch (Exception exception) {
        gm.sendPackets("請輸入 .PvP [開啟/關閉]");
        }
        }

private void search_banned(L1PcInstance paramL1PcInstance) {
        Selector.exec(
        "select accounts.login, characters.char_name from accounts,characters where accounts.banned=62 || accounts.banned=87 || accounts.banned=95 and accounts.login=characters.account_name ORDER BY accounts.login ASC",
        new FullSelectorHandler() {
@override
public void result(ResultSet rs) throws Exception {
        int i = 0;
        while (rs.next()) {
        ++i;
        paramL1PcInstance.sendPackets(String.format("帳號:[%s], 角色名:[%s]", rs.getString(1), rs.getString(2)));
        }
        paramL1PcInstance.sendPackets(String.format("總共搜索到 [%d] 個被查封的帳號/角色。", i));
        }
        });
        }

private void 刪除帳號角色(L1PcInstance gm, String param) {

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
        gm.sendPackets(String.format("資料庫中不存在名為 %s 的角色。", chaname));
        return;
        }

        String account = rs.getString("account_name");
        SQLUtil.close(rs, pstm);

        pstm = con.prepareStatement("SELECT * FROM accounts WHERE login=?");
        pstm.setString(1, account);
        rs = pstm.executeQuery();
        if (!rs.next()) {
        gm.sendPackets(String.format("帳號 %s 不存在。", account));
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

        pstm = con.prepareStatement("DELETE FROM ban_ip WHERE ip=?");
        pstm.setString(1, host);
        pstm.executeUpdate();

        MJHddIdChecker.delete(account);
        MJNSDenialAddress.getInstance().delete_denials_address(host);

        gm.sendPackets(String.format("%s(%s) : %s 解封完成。", chaname, account, host));
        } catch (Exception e) {
        gm.sendPackets("請輸入 .解封 [角色名]");
        } finally {
        SQLUtil.close(rs, pstm, con);
        }
        }

//private void 給予深淵點數(L1PcInstance pc, String poby) {
//        try {
//        StringTokenizer st = new StringTokenizer(poby);
//        String pobyname = st.nextToken();
//        int point = Integer.parseInt(st.nextToken());
//        L1PcInstance target = L1World.getInstance().getPlayer(pobyname);
//        if (target != null) {
//        target.addAbysspoint(point);
//        pc.sendPackets(target.getName() + " 已獲得 [深淵點數 " + point + " 點]。");
//        }
//        } catch (Exception e) {
//        pc.sendPackets("請輸入 .給予深淵點數 [角色名] [點數]");
//        }
//        }
//
//private void 給予血盟點數(L1PcInstance pc, String poby) { // 血盟經驗值
//        try {
//        StringTokenizer st = new StringTokenizer(poby);
//        String pobyname = st.nextToken();
//        int point = Integer.parseInt(st.nextToken());
//        L1PcInstance target = L1World.getInstance().getPlayer(pobyname);
//        if (target != null) {
//        if (target.getClanid() != 0) {
//        L1Clan TargetClan = L1World.getInstance().getClan(target.getClanname());
//        TargetClan.addClanExp(point);
//        ClanTable.getInstance().updateClan(TargetClan);
//        pc.sendPackets(target.getClanname() + " 血盟已獲得 [經驗值 " + point + "]。");
//        for (L1PcInstance tc : TargetClan.getOnlineClanMember()) {
//        tc.sendPackets("你已經從遊戲管理員處獲得血盟經驗值 [" + point + "]。");
//        }
//        } else {
//        pc.sendPackets(target.getName() + " 不屬於任何血盟。");
//        }
//        } else {
//        pc.sendPackets(new S_ServerMessage(73, pobyname));
//        }
//        } catch (Exception e) {
//        pc.sendPackets("請輸入 .給予血盟點數 [血盟君主名稱] [點數]");
//        }
//        }

private void 更改密碼(L1PcInstance gm, String param) {
        try {
        StringTokenizer tok = new StringTokenizer(param);
        String user = tok.nextToken();
        String passwd = tok.nextToken();

        if (passwd.length() < 4) {
        gm.sendPackets("您輸入的密碼太短。");
        gm.sendPackets("請至少輸入 4 個字符。");
        return;
        }

        if (passwd.length() > 12) {
        gm.sendPackets("您輸入的密碼太長。");
        gm.sendPackets("請輸入最多 12 個字符。");
        return;
        }

        if (!isDisitAlpha(passwd)) {
        gm.sendPackets("密碼包含不允許的字符。");
        return;
        }

        L1PcInstance target = L1World.getInstance().getPlayer(user);
        if (target != null) {
        to_Change_Passwd(gm, target, passwd);
        } else {
        if (!to_Change_Passwd(gm, user, passwd))
        gm.sendPackets("沒有找到名稱為 " + user + " 的角色。");
        }
        } catch (Exception e) {
        gm.sendPackets("請使用 .更改密碼 [角色名稱] [密碼] 進行操作。");
        }
        }

private void 更改密碼(L1PcInstance gm, L1PcInstance pc, String passwd) {
        PreparedStatement statement = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        java.sql.Connection con = null;
        try {
        String login = null;
        String password = passwd;
        con = L1DatabaseFactory.getInstance().getConnection();
        statement = con.prepareStatement("SELECT account_name FROM characters WHERE char_name LIKE ?");
        statement.setString(1, pc.getName());
        rs = statement.executeQuery();

        while (rs.next()) {
        login = rs.getString(1);
        pstm = con.prepareStatement("UPDATE accounts SET password=? WHERE login LIKE ?");
        pstm.setString(1, password);
        pstm.setString(2, login);
        pstm.execute();
        gm.sendPackets("已更改帳號: [" + login + "] 的密碼: [" + passwd + "]");
        gm.sendPackets(pc.getName() + " 的密碼更改完成。");
        }
        } catch (Exception e) {
        System.out.println("更改密碼錯誤: " + e);
        } finally {
        SQLUtil.close(rs);
        SQLUtil.close(pstm);
        SQLUtil.close(statement);
        SQLUtil.close(con);
        }
        }

private boolean 更改密碼(L1PcInstance pc, String name, String passwd) {
        PreparedStatement statement = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        java.sql.Connection con = null;
        try {
        String login = null;
        String password = passwd;
        con = L1DatabaseFactory.getInstance().getConnection();
        statement = con.prepareStatement("SELECT account_name FROM characters WHERE char_name LIKE ?");
        statement.setString(1, name);
        rs = statement.executeQuery();

        while (rs.next()) {
        login = rs.getString(1);
        pstm = con.prepareStatement("UPDATE accounts SET password=? WHERE login LIKE ?");
        pstm.setString(1, password);
        pstm.setString(2, login);
        pstm.execute();
        pc.sendPackets("已更改帳號: [" + login + "] 的密碼: [" + passwd + "]");
        pc.sendPackets("該角色的密碼更改完成。（未在線）");
        }
        return true;
        } catch (Exception e) {
        System.out.println("更改密碼錯誤: " + e);
        } finally {
        SQLUtil.close(rs);
        SQLUtil.close(pstm);
        SQLUtil.close(statement);
        SQLUtil.close(con);
        }
        return false;
        }

private void 更改密碼提示問題(L1PcInstance gm, L1PcInstance pc, String newquiz) {
        PreparedStatement statement = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        java.sql.Connection con = null;
        try {
        String login = null;
        con = L1DatabaseFactory.getInstance().getConnection();
        statement = con.prepareStatement("SELECT account_name FROM characters WHERE char_name LIKE ?");
        statement.setString(1, pc.getName());
        rs = statement.executeQuery();

        while (rs.next()) {
        login = rs.getString(1);
        pstm = con.prepareStatement("UPDATE accounts SET quiz=? WHERE login LIKE ?");
        pstm.setString(1, newquiz);
        pstm.setString(2, login);
        pstm.execute();
        gm.sendPackets("已更改帳號 [" + login + "] 的密碼提示問題為: [" + newquiz + "]");
        gm.sendPackets(pc.getName() + " 的密碼提示問題更改完成。");
        }
        } catch (Exception e) {
        System.out.println("更改密碼提示問題錯誤: " + e);
        } finally {
        SQLUtil.close(rs);
        SQLUtil.close(pstm);
        SQLUtil.close(statement);
        SQLUtil.close(con);
        }
        }

private boolean 更改密碼提示問題(L1PcInstance pc, String name, String newquiz) {
        PreparedStatement statement = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        java.sql.Connection con = null;
        try {
        String login = null;
        con = L1DatabaseFactory.getInstance().getConnection();
        statement = con.prepareStatement("SELECT account_name FROM characters WHERE char_name LIKE ?");
        statement.setString(1, name);
        rs = statement.executeQuery();

        while (rs.next()) {
        login = rs.getString(1);
        pstm = con.prepareStatement("UPDATE accounts SET quiz=? WHERE login LIKE ?");
        pstm.setString(1, newquiz);
        pstm.setString(2, login);
        pstm.execute();
        pc.sendPackets("已更改帳號: [" + login + "] 的密碼提示問題為: [" + newquiz + "]");
        pc.sendPackets("該角色的密碼提示問題更改完成。（未在線）");
        }
        return true;
        } catch (Exception e) {
        System.out.println("更改密碼提示問題錯誤: " + e);
        } finally {
        SQLUtil.close(rs);
        SQLUtil.close(pstm);
        SQLUtil.close(statement);
        SQLUtil.close(con);
        }
        return false;
        }

private static int delItemlist[] = { 307, 308, 309, 310, 311, 312, 313, 314, 21095, 30146, 30147, 30150 };

public synchronized static void 移除阿諾德活動物品() {
        try {
        if (delItemlist.length <= 0)
        return;

// 遍歷所有玩家
        for (L1PcInstance tempPc : L1World.getInstance().getAllPlayers()) {
        if (tempPc == null)
        continue;

        for (int i = 0; i < delItemlist.length; i++) {
// 從玩家背包中刪除物品
        L1ItemInstance[] items = tempPc.getInventory().findItemsId(delItemlist[i]);
        if (items != null && items.length > 0) {
        for (L1ItemInstance item : items) {
        tempPc.getInventory().removeItem(item);
        }
        }

// 從私人倉庫中刪除物品
        try {
        PrivateWarehouse pw = WarehouseManager.getInstance().getPrivateWarehouse(tempPc.getAccountName());
        L1ItemInstance[] pwItems = pw.findItemsId(delItemlist[i]);
        if (pwItems != null && pwItems.length > 0) {
        for (L1ItemInstance pwItem : pwItems) {
        pw.removeItem(pwItem);
        }
        }
        } catch (Exception e) {
        System.out.println("私人倉庫處理錯誤: " + e);
        }

// 從血盟倉庫中刪除物品
        try {
        if (tempPc.getClanid() > 0) {
        ClanWarehouse cw = WarehouseManager.getInstance().getClanWarehouse(tempPc.getClanname());
        L1ItemInstance[] cwItems = cw.findItemsId(delItemlist[i]);
        if (cwItems != null && cwItems.length > 0) {
        for (L1ItemInstance cwItem : cwItems) {
        cw.removeItem(cwItem);
        }
        }
        }
        } catch (Exception e) {
        System.out.println("血盟倉庫處理錯誤: " + e);
        }

// 從寵物背包中刪除物品
        try {
        if (!tempPc.getPetList().isEmpty()) {
        for (L1NpcInstance npc : tempPc.getPetList().values()) {
        L1ItemInstance[] petItems = npc.getInventory().findItemsId(delItemlist[i]);
        if (petItems != null && petItems.length > 0) {
        for (L1ItemInstance petItem : petItems) {
        npc.getInventory().removeItem(petItem);
        }
        }
        }
        }
        } catch (Exception e) {
        System.out.println("寵物處理錯誤: " + e);
        }
        }
        }

// 移除掉落在地圖上的物品
        try {
        for (L1Object obj : L1World.getInstance().getAllItem()) {
        if (!(obj instanceof L1ItemInstance))
        continue;
        L1ItemInstance tempItem = (L1ItemInstance) obj;
        if (tempItem.getItemOwner() == null) {
        if (tempItem.getX() == 0 && tempItem.getY() == 0)
        continue;
        }
        for (int ii = 0; ii < delItemlist.length; ii++) {
        if (delItemlist[ii] == tempItem.getItemId()) {
        [02:05]
        L1Inventory groundInventory = L1World.getInstance().getInventory(tempItem.getX(), tempItem.getY(), tempItem.getMapId());
        groundInventory.removeItem(tempItem);
        break;
        }
        }
        }
        } catch (Exception e) {
        System.out.println("地圖物品處理錯誤: " + e);
        }

// 刪除指定的物品
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < delItemlist.length; i++) {
        sb.append(delItemlist[i]);
        if (i < delItemlist.length - 1) {
        sb.append(",");
        }
        }
        Delete(sb.toString());
        } catch (Exception e) {
        System.out.println("移除阿諾德活動物品錯誤: " + e);
        }
        }

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
            return;

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
        // pck.set_event_desc("\\fW$12125".getBytes("MS949"));
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
         * SC_EVENT_COUNTDOWN_NOTI_PACKET pck = SC_EVENT_COUNTDOWN_NOTI_PACKET.newInstance(); pck.set_remain_time(10); try { pck.set_event_desc("\\fW$12125".getBytes("MS949")); } catch (UnsupportedEncodingException e) { e.printStackTrace(); } gm.sendPackets(pck,
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
         * L1Object obj = L1World.getInstance().findObject(269064985); L1Character c = (L1Character)obj; MJServerPacketBuilder builder = null; try { // 파티원 다이, 다시 살아남 //builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x6C).addD(
         * 269064817).addH(0x00); // 리더변경 //builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x6A).addD( 269064817).addH(0x00); // 좌측 파티창에 참가 //builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x69).addD(
         * 269064817).addS(c.getName()).addC(0).addH(0x00).addD(c.getMapId()). addH(c.getX()).addH(c.getY()); //builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x6E).addC(0x01) .addD(269064817).addD(c.getMapId()).addH(c.getX()).addH(c.getY()).
         * addC(0x00); //builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(159).addD( 269064817).addD(c.getMapId()).addH(c.getX()).addH(c.getY()).addC(0x00 ); builder = new MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(111).addS(c.
         * getName()).addH(c.getMapId()).addH(c.getX()).addH(c.getY()); } catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); } byte[] b = builder.toArray(); S_BuilderPacket s = new S_BuilderPacket(b.length, b); gm.sendPackets(s,
         * true); builder.close(); builder.dispose();
         */
        // 269064817
        // gm.sendPackets(new S_Message_YN(3325, 3325, param));
        // gm.sendPackets(S_IconMessage.getGmMessage(String.format("[%s]님의
        // 확성기(일반)메시지", gm.getName())));
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

    private void 顯示效果初始化(L1PcInstance gm, String param) {
        int effNum = 0;
        try {
            StringTokenizer st = new StringTokenizer(param);
            effNum = Integer.parseInt(st.nextToken());
        } catch (Exception e) {
            gm.sendPackets("效果編號格式錯誤。");
        }
        synchronized (_effectLock) {
            _effectNum = effNum;
            gm.sendPackets(effNum + "已設為初始化狀態。");
        }
    }

    private void 變身活動(L1PcInstance gm, String param) {
        try {
            if (param.equalsIgnoreCase("開啟")) {
                for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                    SC_POLYMORPH_EVENT_NOTI noti = SC_POLYMORPH_EVENT_NOTI.newInstance();
                    noti.set_eventEnable(true);
                    pc.sendPackets(noti, MJEProtoMessages.SC_POLYMORPH_EVENT_NOTI, true);
                    pc.sendPackets("稍後將進行變身活動。變身等級將被調整。");
                }
                Config.ServerAdSetting.PolyEvent2 = true;
                L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "稍後將進行變身活動。變身等級將被調整。"));
            } else if (param.equalsIgnoreCase("關閉")) {
                for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                    SC_POLYMORPH_EVENT_NOTI noti = SC_POLYMORPH_EVENT_NOTI.newInstance();
                    noti.set_eventEnable(false);
                    pc.sendPackets(noti, MJEProtoMessages.SC_POLYMORPH_EVENT_NOTI, false);
                    pc.sendPackets("稍後變身活動將結束。變身等級將重新調整。");
                }
                Config.ServerAdSetting.PolyEvent2 = false;
                L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "稍後變身活動將結束。變身等級將重新調整。"));
            } else {
                throw new Exception("無效的參數");
            }
        } catch (Exception e) {
            gm.sendPackets(".變身活動 [開啟/關閉]");
        }
    }

    public static String _sleepingMessage;
    public static String _sleepingTitle;

    private void 設定睡眠模式(L1PcInstance gm, String param) {
        int num = 0;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            StringTokenizer st = new StringTokenizer(param);
            num = Integer.parseInt(st.nextToken(), 10);
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM tb_sleeping_messages WHERE id=?");
            pstm.setInt(1, num);
            rs = pstm.executeQuery();
            if (rs.next()) {
                _sleepingTitle = rs.getString("title");
                _sleepingMessage = rs.getString("content");
                gm.sendPackets(String.format("睡眠訊息已設為 [%s]。", _sleepingMessage));
            } else {
                gm.sendPackets(String.format("不存在編號為 [%d] 的睡眠訊息。", num));
            }
        } catch (Exception e) {
            gm.sendPackets("請輸入 .睡眠模式 [睡眠訊息編號]。");
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
    }

    private void 解除睡眠模式(L1PcInstance gm, String param) {
        _sleepingTitle = null;
        _sleepingMessage = null;
        gm.sendPackets("睡眠模式已解除。");
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

    private void 顯示使用類型(L1PcInstance gm, String param) {
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
            gm.sendPackets("請輸入 .使用類型 [起始編號] [結束編號]");
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

    private static void 設置角色封鎖(L1PcInstance gm, String param) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection con = null;

        try {
            StringTokenizer st = new StringTokenizer(param);
            String name = st.nextToken();
            if (name == null || name.equalsIgnoreCase("")) {
                throw new Exception("角色名稱無效");
            }

            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
            pstm.setString(1, name);
            rs = pstm.executeQuery();

            if (!rs.next()) {
                gm.sendPackets(String.format("\f3無法找到角色 [%s]。", name));
                return;
            }

            int objid = rs.getInt("objid");
            L1PcInstance pc = L1World.getInstance().getPlayer(name);
            SQLUtil.close(rs, pstm);

            pstm = con.prepareStatement("INSERT IGNORE INTO tb_character_block SET objid=?, name=?");
            pstm.setInt(1, objid);
            pstm.setString(2, name);
            pstm.executeUpdate();

            gm.sendPackets(String.format("\f3角色 [%s] 已被封鎖。", name));

            if (pc != null) {
                pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3您的角色已被封鎖。"));
                pc.sendPackets("\f3您的角色已被封鎖。");
                // GeneralThreadPool.getInstance().schedule(new DelayRestart(pc), 1000);
                // C_NewCharSelect.restartProcess(pc);//重新選擇角色
            }
        } catch (Exception e) {
            gm.sendPackets("請輸入正確的角色名稱。");
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
    }

            GeneralThreadPool.getInstance().schedule(new Runnable() {
@override
public void run() {
        GameClient client = pc.getNetConnection();
        String name = pc.getName();
        int x = pc.getX();
        int y = pc.getY();
        int mapId = pc.getMapId();
        C_NewCharSelect.restartProcess(pc); // 重新選擇角色
        try {
        Thread.sleep(800L); // 1.0秒
        pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
        C_LoginToServer.doEnterWorld(name, client, false, x, y, mapId); // 再次嘗試連接
        } catch (Exception e) {
        e.printStackTrace();
        }
        }
        }, 1000);

        gm.sendPackets("請輸入 .角色封鎖 [角色名稱]");
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
    }
}

    private static void 初始化用戶私人概率(MJCommandArgs args) {
        try {
            String typeName = args.nextString();
            if (typeName.equalsIgnoreCase("全部")) {
                for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                    pc.truncate_private_probability();
                }
                Updator.truncate("characters_private_probability");
                args.notify("所有角色的概率已初始化。");
            } else {
                String characterName = args.nextString();
                L1PcInstance pc = L1World.getInstance().getPlayer(characterName);
                if (pc == null) {
                    args.notify(String.format("找不到名為 %s 的玩家。", characterName));
                    return;
                }
                pc.truncate_private_probability();
                Updator.exec("DELETE FROM characters_private_probability WHERE object_id=?", new Handler() {
                    @override
                    public void handle(PreparedStatement pstm) throws Exception {
                        pstm.setInt(1, pc.getId());
                    }
                });
                args.notify(String.format("%s 的概率已初始化。", characterName));
            }
        } catch (Exception e) {
            args.notify("請輸入正確的指令：.初始化概率 [角色名]");
            args.notify("或者 .初始化概率 全部");
        }
    }

    private static void 更新用戶私人概率(MJCommandArgs args) {
        try {
            String characterName = args.nextString();
            int skillId = args.nextInt();
            int addedProbability = args.nextInt();
            L1PcInstance pc = L1World.getInstance().getPlayer(characterName);
            if (pc == null) {
                args.notify(String.format("找不到名為 %s 的玩家。", characterName));
                return;
            }

            pc.add_private_probability(skillId, addedProbability);
            args.notify(String.format("%s 的技能 %d 使用增加了 %d%% 概率。", characterName, skillId, addedProbability));
        } catch (Exception e) {
            args.notify(".概率更新 [角色名] [技能ID] [增加概率]");
        }
    }

    private static void 顯示用戶私人概率(MJCommandArgs args) {
        try {
            String characterName = args.nextString();
            int skillId = args.nextInt();
            L1PcInstance pc = L1World.getInstance().getPlayer(characterName);
            if (pc == null) {
                args.notify(String.format("找不到名為 %s 的玩家。", characterName));
                return;
            }
            args.notify(String.format("%s 的技能 %d 具有 %d%% 的額外概率。", characterName, skillId, pc.get_private_probability(skillId)));
        } catch (Exception e) {
            args.notify(".概率查詢 [角色名] [技能ID]");
        }
    }

    private static void 解除角色封鎖(L1PcInstance gm, String param) {
        PreparedStatement pstm = null;
        Connection con = null;

        try {
            StringTokenizer st = new StringTokenizer(param);
            String name = st.nextToken();
            if (name == null || name.equalsIgnoreCase("")) {
                throw new Exception("角色名稱無效");
            }

            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("DELETE FROM tb_character_block WHERE name=?");
            pstm.setString(1, name);
            pstm.executeUpdate();
            gm.sendPackets(String.format("角色 [%s] 的封鎖已解除。", name));
        } catch (Exception e) {
            gm.sendPackets("請輸入正確的指令：.解除角色封鎖 [角色名稱]");
        } finally {
            SQLUtil.close(pstm, con);
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

    private void 顯示聊天(L1PcInstance gm, String param) {
        gm.sendPackets(S_ChatMessageNoti.getNotice(param, "哈哈"));
        // gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_WHISPER, param, null, "Metis", null, -1));
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
            gm.sendPackets("Ping檢查已啟動。（SC_PING_REQ.java 已添加，不再使用）");
        } else if (param.equalsIgnoreCase("關")) {
            S_Ping._isRun = false;
            S_Ping._lastMs = 0L;
            gm.sendPackets("Ping檢查已停止。（SC_PING_REQ.java 已添加，不再使用）");
        }
    }

    public static boolean IS_PROTECTION = false;

    private void 設置保護模式(L1PcInstance gm, String param) {
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
                    pc.sendPackets("\f3[保護模式啟動] 死亡時經驗值不減少，且不掉落物品。");
                }
            } else if (param.equalsIgnoreCase("關")) {
                IS_PROTECTION = false;
                L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[用戶保護模式結束] 死亡時適用懲罰。"), true);
                for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                    SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
                    noti.set_noti_type(eNotiType.END);
                    noti.set_spell_id(L1SkillId.SAFE_MODE);
                    pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
                    pc.sendPackets("\f3[保護模式結束] 死亡時經驗值會下降並掉落物品。");
                }
            } else if (param.equalsIgnoreCase("狀態")) {
                gm.sendPackets(String.format("當前保護模式狀態 [%s]", IS_PROTECTION ? "開啟" : "關閉"));
            } else {
                throw new Exception("");
            }
        } catch (Exception e) {
            gm.sendPackets(".保護模式 [開/關/狀態]");
        }
    }

    // 新增指令
    private void 帳號檢查(L1PcInstance pc, String param) {
        try {
            StringTokenizer st = new StringTokenizer(param);
            String name = st.nextToken();
            CharacterTable.getInstance().CharacterAccountCheck(pc, name);
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage(".帳號檢查 [角色名稱]"));
        }
    }

    private void 帳號確認(L1PcInstance pc, String param) {
        try {
            StringTokenizer st = new StringTokenizer(param);
            String name = st.nextToken();
            CharacterTable.getInstance().CharacterAccountCheck1(pc, name);
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage(".帳號確認 [角色名稱]"));
        }
    }

    private void 獲取物品1(L1PcInstance gm) { // 沙蟲的沙袋
        L1ItemInstance item = null;
        item = ItemTable.getInstance().createItem(210125);
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人從沙蟲的沙袋中獲得了 (" + item.getName() + ")。"));
        L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("有人從沙蟲的沙袋中獲得了 (" + item.getName() + ")。"));
    }

    private void 獲取物品2(L1PcInstance gm) { // 盔甲
        L1ItemInstance item = null;
        item = ItemTable.getInstance().createItem(5559);
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人從沙蟲的沙袋中獲得了 (" + item.getName() + ")。"));
        L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("有人從沙蟲的沙袋中獲得了 (" + item.getName() + ")。"));
    }

    private void 獲取物品3(L1PcInstance gm) { // 艾爾札貝的蛋
        L1ItemInstance item = null;
        item = ItemTable.getInstance().createItem(41148);
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人從艾爾札貝的蛋中獲得了 (" + item.getName() + ")。"));
        L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("有人從艾爾札貝的蛋中獲得了 (" + item.getName() + ")。"));
    }

    private void 合成魔神娃娃(L1PcInstance gm) { // 魔神娃娃
        L1ItemInstance item = null;
        item = ItemTable.getInstance().createItem(745);
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人成功合成了 " + item.getName() + "。"));
        L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "\fH有人成功合成了 " + item.getName() + "。"));
    }

    private void 合成死亡娃娃(L1PcInstance gm) { // 死亡娃娃
        L1ItemInstance item = null;
        item = ItemTable.getInstance().createItem(746);
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人成功合成了 " + item.getName() + "。"));
        L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "\fH有人成功合成了 " + item.getName() + "。"));
    }

    private void 合成墮落娃娃(L1PcInstance gm) { // 墮落娃娃
        L1ItemInstance item = null;
        item = ItemTable.getInstance().createItem(3000352);
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人成功合成了 " + item.getName() + "。"));
        L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "\fH有人成功合成了 " + item.getName() + "。"));
    }

    private void 合成冰女娃娃(L1PcInstance gm) { // 冰女娃娃
        L1ItemInstance item = null;
        item = ItemTable.getInstance().createItem(3000352);
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人成功合成了 " + item.getName() + "。"));
        L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(gm, "\fH有人成功合成了 " + item.getName() + "。"));
    }

    private void 獲得符文石物品(L1PcInstance gm) { // 符文石物品
        L1ItemInstance item = null;
        item = ItemTable.getInstance().createItem(41148);
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人從閃亮的符文石中獲得了 " + item.getName() + "。"));
        L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("有人從閃亮的符文石中獲得了 (" + item.getName() + ")。"));
    }

    private void 獲得符文石物品1(L1PcInstance gm) { // 符文石物品 (40222)
        L1ItemInstance item = null;
        item = ItemTable.getInstance().createItem(40222);
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人從閃亮的符文石中獲得了 " + item.getName() + "。"));
        L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("有人從閃亮的符文石中獲得了 (" + item.getName() + ")。"));
    }

    private void 獲得符文石物品2(L1PcInstance gm) { // 符文石物品 (210125)
        L1ItemInstance item = null;
        item = ItemTable.getInstance().createItem(210125);
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人從閃亮的符文石中獲得了 " + item.getName() + "。"));
        L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("有人從閃亮的符文石中獲得了 (" + item.getName() + ")。"));
    }

    private void 獲得符文石物品3(L1PcInstance gm) { // 符文石物品 (5559)
        L1ItemInstance item = null;
        item = ItemTable.getInstance().createItem(5559);
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人從閃亮的符文石中獲得了 " + item.getName() + "。"));
        L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("有人從閃亮的符文石中獲得了 (" + item.getName() + ")。"));
    }

    private void 添加活動Boss(L1PcInstance gm, String param) {
        try {
            StringTokenizer st = new StringTokenizer(param);
            String BossName = st.nextToken();
            if (BossName.equalsIgnoreCase("庫茲")) {
                L1SpawnUtil.spawn2(32854, 33261, (short) 4, 45600, 0, 3600 * 1000, 0);
                L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
                L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
                L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
                L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
                L1World.getInstance().broadcastServerMessage("\aH[活動Boss(1/6)]:衝鋒！庫茲的部隊已被召喚。");
                L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動Boss(1/6)]:衝鋒！庫茲的部隊已被召喚。"), true);
            } else if (BossName.equalsIgnoreCase("死亡騎士")) {
                L1SpawnUtil.spawn2(32856, 33263, (short) 4, 45601, 0, 3600 * 1000, 0);
                L1World.getInstance().broadcastServerMessage("\aH[活動Boss(2/6)]:衝鋒！死亡騎士的部隊已被召喚。");
                L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動Boss(2/6)]:衝鋒！死亡騎士的部隊已被召喚。"), true);
            } else if (BossName.equalsIgnoreCase("不死鳥")) {
                L1SpawnUtil.spawn2(32853, 33265, (short) 4, 45617, 50, 3600 * 1000, 0);
                L1World.getInstance().broadcastServerMessage("\aH[活動Boss(3/6)]:衝鋒！不死鳥的部隊已被召喚。");
                L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動Boss(3/6)]:衝鋒！不死鳥的部隊已被召喚。"), true);
            } else if (BossName.equalsIgnoreCase("魔靈軍王萊亞")) {
                L1SpawnUtil.spawn2(32854, 33259, (short) 4, 45863, 0, 3600 * 1000, 0);
                L1World.getInstance().broadcastServerMessage("\aH[活動Boss(4/6)]:衝鋒！魔靈軍王萊亞的部隊已被召喚。");
                L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動Boss(4/6)]:衝鋒！魔靈軍王萊亞的部隊已被召喚。"), true);
            } else if (BossName.equalsIgnoreCase("死神格林里帕")) {
                L1SpawnUtil.spawn2(32854, 33266, (short) 4, 7310077, 0, 3600 * 1000, 0);
                L1World.getInstance().broadcastServerMessage("\aH[活動Boss(5/6)]:衝鋒！死神格林里帕的部隊已被召喚。");
                L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動Boss(5/6)]:衝鋒！死神格林里帕的部隊已被召喚。"), true);
            } else if (BossName.equalsIgnoreCase("恐怖的安塔瑞斯")) {
                L1SpawnUtil.spawn2(32848, 33260, (short) 4, 7310154, 0, 3600 * 1000, 0);
                L1World.getInstance().broadcastServerMessage("\aH[活動Boss(6/6)]:衝鋒！最後的怪物已被召喚。");
                L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fR[活動Boss(6/6)]:衝鋒！最後的怪物已被召喚。"), true);
            }
        } catch (Exception e) {
            gm.sendPackets(new S_SystemMessage(".添加活動Boss [Boss名稱] ex<庫茲/死亡騎士/不死鳥/魔靈軍王萊亞/死神格林里帕/恐怖的安塔瑞斯>"));
        }
    }

    /*
     * private void 設定最大等級(L1PcInstance gm, String param){
     *   try {
     *     String[] arr = param.split(" ");
     *     if (arr[0].equalsIgnoreCase("設定")) {
     *       int i = Integer.parseInt(arr[1]);
     *       Config.MAX_LEVEL = i;
     *       Config.MAX_LEVEL_EXP = ExpTable.getExpByLevel(i) + 5;
     *     } else if (arr[0].equalsIgnoreCase("確認")) {
     *       // 此處應實現查看當前最大等級和經驗值的邏輯
     *     } else {
     *       throw new Exception();
     *     }
     *     gm.sendPackets(String.format("當前設定的最大等級 : %d(exp : %d)", Config.MAX_LEVEL, Config.MAX_LEVEL_EXP));
     *   } catch (Exception e) {
     *     gm.sendPackets(".最大等級 [確認]");
     *     gm.sendPackets(".最大等級 [設定] [等級]");
     *   }
     * }
     */

    public void 設定表示代碼(L1PcInstance gm, String param) {
        try {
            L1ItemInstance.presentationCode = Integer.parseInt(param);
            gm.sendPackets(String.format("表示代碼已設定為 %d。", L1ItemInstance.presentationCode));
        } catch (Exception e) {
            gm.sendPackets(".表示設定 數字");
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
* StringBuilder sb = new StringBuilder(256);
* for (L1Object obj : gm.getKnownObjects()) {
*   if (obj instanceof L1NpcInstance) {
*     L1NpcInstance npc = (L1NpcInstance) obj;
*     npc.removeKnownObject(obj);
*     npc.deleteMe();
*     sb.append(npc.getName()).append(" ").append(obj.getX()).append(" ").append(obj.getY()).append("
");
*     System.out.println(npc);
*   }
* }
* gm.sendPackets(sb.toString());
*/
    }

    public static void 設定角色狀態(MJCommandArgs args) {
        try {
            String name = args.nextString();
            int status = args.nextInt();
            L1PcInstance pc = L1World.getInstance().getPlayer(name);
            if (pc == null) {
                args.notify(String.format("找不到角色名稱 : %s。", name));
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

    private void 詛咒角色(MJCommandArgs args) {
        try {
            String name = args.nextString();
            if (name == null || name.equalsIgnoreCase("")) {
                throw new Exception();
            }

            L1PcInstance pc = L1World.getInstance().getPlayer(name);
            if (pc == null) {
                args.notify(String.format("找不到角色名稱 : %s。", name));
                return;
            }

            pc.add_lateral_damage(args.nextInt());
            pc.add_lateral_reduction(args.nextInt());
            pc.add_lateral_magic_rate(args.nextInt());
            pc.update_lateral_status();

            args.notify(String.format("%s的當前狀態 - 傷害:%d, 減少:%d, 魔法傷害:%d", name, pc.get_lateral_damage(), pc.get_lateral_reduction(), pc.get_lateral_magic_rate()));
        } catch (Exception e) {
            args.notify(".詛咒 [角色名稱] [傷害] [減少] [魔法傷害]");
        }
    }

    private void 詛咒初始化角色(MJCommandArgs args) {
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
                if (name == null || name.equalsIgnoreCase("")) {
                    throw new Exception();
                }
                L1PcInstance pc = L1World.getInstance().getPlayer(name);
                if (pc == null) {
                    args.notify(String.format("找不到角色名稱 : %s。", name));
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
        try {
            StringTokenizer st = new StringTokenizer(param);
            String onoff = st.nextToken();
            if (onoff.equalsIgnoreCase("開")) {
                pc.sendPackets(new S_MARK_SEE(pc, 2, true), true);
                pc.sendPackets(new S_MARK_SEE(pc, 0, true), true);
                pc.sendPackets("已開始顯示血盟標誌。");
            } else if (onoff.equalsIgnoreCase("關")) {
                pc.sendPackets(new S_MARK_SEE(pc, 2, false), true);
                pc.sendPackets(new S_MARK_SEE(pc, 1, false), true);
                pc.sendPackets("已停止顯示血盟標誌。");
            } else {
                pc.sendPackets(new S_SystemMessage(".血盟標誌 [開 / 關]"));
                return;
            }
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage(".血盟標誌 [開 / 關]"));
        }
    }

    private void 刪除角色(L1PcInstance gm, String param) {
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
                        gm.sendPackets(new S_SystemMessage(account_name + "帳號 " + char_name + " 的角色將被刪除。"), true);
                        CharacterTable.getInstance().deleteCharacter(account_name, char_name);
                        gm.sendPackets(new S_SystemMessage("該用戶已成功刪除。"), true);
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
            gm.sendPackets(new S_SystemMessage(".角色整理 [最低等級] [離線天數]"));
        }
    }

    private void 刪除角色(L1PcInstance gm, String param) {
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
                    gm.sendPackets(new S_SystemMessage("資料庫中不存在該用戶的名稱。"), true);

                } else {
                    gm.sendPackets(new S_SystemMessage(acname + "帳號 " + pcName + " 的角色將被刪除。"), true);
                    CharacterTable.getInstance().deleteCharacter(acname, pcName);
                    gm.sendPackets(new S_SystemMessage("該用戶已成功刪除。"), true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }

        } catch (Exception eee) {
            gm.sendPackets(new S_SystemMessage(".角色刪除 [角色名稱]"), true);
        }
    }

    private void 祝福之葉(L1PcInstance gm, String pcName) {
        try {
            L1PcInstance target = L1World.getInstance().getPlayer(pcName);
            if (target != null) {
                target._Blessleaf = true;
                gm.sendPackets(target.getName() + " 的失去力量的物品成功率將一次性提高到 100%。");
            } else {
                gm.sendPackets("沒有找到該角色。");
            }
        } catch (Exception e) {
            gm.sendPackets("請輸入 .祝福之葉 角色名稱。");
        }
    }

    private void 工藝成功(L1PcInstance gm, String pcName) {
        try {
            L1PcInstance target = L1World.getInstance().getPlayer(pcName);
            if (target != null) {
                target._CraftSuccess = true;
                gm.sendPackets("\f3" + target.getName() + " 的製作成功率將一次性提高到 100%。");
            } else {
                gm.sendPackets("\f3沒有找到該角色。");
            }
        } catch (Exception e) {
            gm.sendPackets("\f3請輸入 .製作成功 角色名稱。");
        }
    }

    private void 武器強化成功(L1PcInstance gm, String pcName) {
        try {
            L1PcInstance target = L1World.getInstance().getPlayer(pcName);
            if (target != null) {
                target._EnchantWeaponSuccess = true;
                gm.sendPackets(target.getName() + " 的武器強化成功率將一次性提高到 100%。");
            } else {
                gm.sendPackets("沒有找到該角色。");
            }
        } catch (Exception e) {
            gm.sendPackets("請輸入 .武器強化成功 角色名稱。");
        }
    }

    private void 防具強化成功(L1PcInstance gm, String pcName) {
        try {
            L1PcInstance target = L1World.getInstance().getPlayer(pcName);
            if (target != null) {
                target._EnchantArmorSuccess = true;
                gm.sendPackets(target.getName() + " 的防具強化成功率將一次性提高到 100%。");
            } else {
                gm.sendPackets("沒有找到該角色。");
            }
        } catch (Exception e) {
            gm.sendPackets("請輸入 .防具強化成功 角色名稱。");
        }
    }

    private void 莎爾的盒子(L1PcInstance gm, String pcName) {
        try {
            L1PcInstance target = L1World.getInstance().getPlayer(pcName);
            if (target != null) {
                target._ErzabeBox = true;
                gm.sendPackets("(莎爾的盒子)" + target.getName() + " 的四大魔法習得成功率將一次性提高到 100%。");
            } else {
                gm.sendPackets("沒有找到該角色。");
            }
        } catch (Exception e) {
            gm.sendPackets("請輸入 .莎爾的盒子 角色名稱。");
        }
    }

    private void 沙蟲之盒(L1PcInstance gm, String pcName) {
        try {
            L1PcInstance target = L1World.getInstance().getPlayer(pcName);
            if (target != null) {
                target._SandwormBox = true;
                gm.sendPackets("(沙蟲之盒)" + target.getName() + " 的四大魔法習得成功率將一次性提高到 100%。");
            } else {
                gm.sendPackets("沒有找到該角色。");
            }
        } catch (Exception e) {
            gm.sendPackets("請輸入 .沙蟲之盒 角色名稱。");
        }
    }

    private void 固定會員(L1PcInstance gm, String param) {
        try {
            StringTokenizer tok = new StringTokenizer(param);
            String phone = tok.nextToken();
            L1PcInstance target = L1World.getInstance().getPlayer(phone); // 修改这里以使用电话号码查找角色
            if (target == null) {
                gm.sendPackets("沒有找到該角色。");
                return;
            }

            Account account = Account.load(target.getAccountName());
            if (account.getphone() != null) {
                gm.sendPackets("該角色已經是固定會員。");
                return;
            }
            account.setphone("00000000000");
            Account.updatePhone(account);
            gm.sendPackets(phone + " 設定完成。");
            target.sendPackets("固定申請已完成，將激活增益效果。");
            啟動安全增益(target);
        } catch (Exception e) {
            gm.sendPackets("請正確輸入命令 .固定會員 [電話號碼]。");
        }
    }

    private static void 보안버프(L1PcInstance gm) {
        gm.getAC().addAc(-1);
        gm.sendPackets(new S_PacketBox(gm, S_PacketBox.ICON_SECURITY_SERVICES));
        gm.sendPackets(new S_OwnCharStatus(gm));
    }

    private void 查找血盟成员(L1PcInstance gm, String name) {
        try {
            L1PcInstance target = L1World.getInstance().getPlayer(name);
            if (target == null) {
                gm.sendPackets("未找到該角色。");
                return;
            }

            if (target.getClan() == null) {
                gm.sendPackets("搜索的角色未加入任何血盟。");
                return;
            }

            String charinfo = String.format("SELECT account_name, char_name, level, clanname, OnlineStatus FROM characters WHERE clanname LIKE '%s'", target.getClanname());

            Selector.exec(charinfo, new FullSelectorHandler() {
                @override
                public void result(ResultSet rs) throws Exception {
                    int i = 0;
                    gm.sendPackets("================== 血盟信息 ==================");
                    while (rs.next()) {
                        ++i;
                        if (rs.getInt(5) > 0) {
                            gm.sendPackets(String.format("\fY帳號:%s 角色名:%s 等級:%s 血盟:%s 在線狀態:%d", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
                        } else {
                            gm.sendPackets(String.format("\fY帳號:%s 角色名:%s 等級:%s 血盟:%s 離線", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                        }
                    }
                }
            });

            String claninfo = String.format("SELECT total_m, current_m FROM clan_data WHERE clan_name LIKE '%s'", target.getClanname());

            Selector.exec(claninfo, new FullSelectorHandler() {
                @override
                public void result(ResultSet rs) throws Exception {
                    while (rs.next()) {
                        gm.sendPackets(String.format("\fU總血盟成員: [%s] 在線血盟成員: %s", rs.getString(1), rs.getString(2)));
                    }
                }
            });
        } catch (Exception e) {
            gm.sendPackets("搜索的角色名稱有誤或不存在。");
        }
    }

    private void 支配之塔Boss(L1PcInstance gm, String param) {
        try {
            StringTokenizer st = new StringTokenizer(param);
            int commandnum = Integer.parseInt(st.nextToken(), 10);
            List<DominanceBoss> list = DominanceDataLoader.getList();
            if (commandnum == 0) {
                DominanceDataLoader.reload();
                gm.sendPackets("重新加載支配之塔Boss訊息。");
            } else if (list.size() > 0) {
                for (DominanceBoss b : list) {
                    try {
                        switch (commandnum) {
                            case 1:
                                if (b.getBossNum() == 1) {
                                    DominanceFloorLv1 zenis = new DominanceFloorLv1(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
                                    zenis.Start();
                                    gm.sendPackets("進行支配之塔一層Boss召喚事件。");
                                }
                                break;
                            case 2:
                                if (b.getBossNum() == 2) {
                                    DominanceFloorLv2 sier = new DominanceFloorLv2(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
                                    sier.Start();
                                    gm.sendPackets("進行支配之塔二層Boss召喚事件。");
                                }
                                break;
                            case 3:
                                if (b.getBossNum() == 3) {
                                    DominanceFloorLv3 vampire = new DominanceFloorLv3(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
                                    vampire.Start();
                                    gm.sendPackets("進行支配之塔三層Boss召喚事件。");
                                }
                                break;
                            case 4:
                                if (b.getBossNum() == 4) {
                                    DominanceFloorLv4 zombie = new DominanceFloorLv4(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
                                    zombie.Start();
                                    gm.sendPackets("進行支配之塔四層Boss召喚事件。");
                                }
                                break;
                            case 5:
                                if (b.getBossNum() == 5) {
                                    DominanceFloorLv5 kuger = new DominanceFloorLv5(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
                                    kuger.Start();
                                    gm.sendPackets("進行支配之塔五層Boss召喚事件。");
                                }
                                break;
                            case 6:
                                if (b.getBossNum() == 6) {
                                    DominanceFloorLv6 mummy = new DominanceFloorLv6(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
                                    mummy.Start();
                                    gm.sendPackets("進行支配之塔六層Boss召喚事件。");
                                }
                                break;
                            case 7:
                                if (b.getBossNum() == 7) {
                                    DominanceFloorLv7 iris = new DominanceFloorLv7(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
                                    iris.Start();
                                    gm.sendPackets("進行支配之塔七層Boss召喚事件。");
                                }
                                break;
                            case 8:
                                if (b.getBossNum() == 8) {
                                    DominanceFloorLv8 bald = new DominanceFloorLv8(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
                                    bald.Start();
                                    gm.sendPackets("進行支配之塔八層Boss召喚事件。");
                                }
                                break;
                            case 9:
                                if (b.getBossNum() == 9) {
                                    DominanceFloorLv9 rich = new DominanceFloorLv9(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
                                    rich.Start();
                                    gm.sendPackets("進行支配之塔九層Boss召喚事件。");
                                }
                                break;
                            case 10:
                                if (b.getBossNum() == 10) {
                                    DominanceFloorLv10 ugnus = new DominanceFloorLv10(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
                                    ugnus.Start();
                                    gm.sendPackets("進行支配之塔十層Boss召喚事件。");
                                }
                                break;
                            case 11:
                                if (b.getBossNum() == 11) {
                                    DominanceFloorLv11 riper = new DominanceFloorLv11(b.getNpcId(), b.getMapX(), b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
                                    riper.Start();
                                    gm.sendPackets("進行支配之塔頂層Boss召喚事件。");
                                }
                                break;
                            default:
                                gm.sendPackets("無效的命令編號。");
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        gm.sendPackets("處理Boss召喚事件時出錯。");
                    }
                }
            } else {
                gm.sendPackets("沒有支配之塔Boss訊息。");
            }
        } catch (Exception e) {
            gm.sendPackets("請正確輸入指令格式：.支配之塔Boss 0~11（0=重新加載，1~11=各層Boss）");
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
            }
        }, 1000);
    }

    public static void do_shift_server(L1PcInstance gm, String param) {
        try {
            String[] array = param.split(" ");
            L1PcInstance pc = L1World.getInstance().findpc(array[0]);
            if (pc == null) {
                gm.sendPackets(String.format("%s(은)는不存在的角色。", param));
                return;
            }

            try {
                MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.TRANSFER, array[1], MJString.EmptyString);
                gm.sendPackets(String.format("%s已被移動。", param));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            gm.sendPackets(".伺服器轉移 [角色名] [目標伺服器名稱]");
        }
    }

    private void 執行快取經驗(MJCommandArgs args) {
        try {
            final String 角色名稱 = args.nextString();
            if (l1j.server.MJTemplate.MJString.isNullOrEmpty(角色名稱)) {
                throw new Exception();
            }

            MJObjectWrapper<LevelExpPair> wrapper = new MJObjectWrapper<LevelExpPair>();
            Selector.exec("select * from character_exp_cache where character_name=?", new SelectorHandler() {
                @override
                public void handle(PreparedStatement pstm) throws Exception {
                    pstm.setString(1, 角色名稱);
                }

                @override
                public void result(ResultSet rs) throws Exception {
                    if (rs.next()) {
                        wrapper.value = new LevelExpPair();
                        wrapper.value.level = rs.getInt("lvl");
                        wrapper.value.exp = rs.getLong("exp");
                    }
                }
            });
            if (wrapper.value == null) {
                args.notify(String.format("無法找到 %s 的快取經驗數據。", 角色名稱));
                return;
            }

            L1PcInstance pc = L1World.getInstance().getPlayer(角色名稱);
            if (pc == null) {
                Updator.exec("update characters set level=?, exp=? where char_name=?", new Handler() {
                    @override
                    public void handle(PreparedStatement pstm) throws Exception {
                        int idx = 0;
                        pstm.setInt(++idx, wrapper.value.level);
                        pstm.setLong(++idx, wrapper.value.exp);
                        pstm.setString(++idx, 角色名稱);
                    }
                });
                args.notify(String.format("%s 的資料庫數據已更新。(等級: %d, 經驗值: %d)", 角色名稱, wrapper.value.level, wrapper.value.exp));
            } else {
                pc.set_exp(wrapper.value.exp);
                args.notify(String.format("%s 的經驗值已更改為 %d。", 角色名稱, wrapper.value.exp));
            }
        } catch (Exception e) {
            args.notify("請使用正確的命令格式：.快取經驗 [角色名]");
        }
    }

static class LevelExpPair {
    int 等級;
    long 經驗值;
}

    public static void 查詢背包命令(L1PcInstance gm, String param) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(param);
            String 名稱 = tokenizer.nextToken();
            L1PcInstance 目標 = L1World.getInstance().getPlayer(名稱);

            if (目標 == null) {
                gm.sendPackets("找不到角色資訊。");
                return;
            }

            if (目標 == gm) {
                gm.sendPackets("不能對自己使用。");
                return;
            }

            // 注意：在原代碼中這個條件是多餘的，因為如果目標為 null，代碼已經在前面返回了。
            if (目標 != null)
                gm.sendPackets(S_InvCheck.get(目標));
        } catch (Exception e) {
            gm.sendPackets(new S_SystemMessage(".背包檢查 [角色名]"), true);
        }
    }

    private void 註冊優惠券(L1PcInstance pc, String param) {
        try {
            StringTokenizer st = new StringTokenizer(param);
            String 代碼 = st.nextToken();

            if (pc == null)
                return;

            MJPaymentInfo 支付信息 = MJPaymentInfo.newInstance(代碼.toUpperCase());
            if (支付信息 == null) {
                pc.sendPackets(".註冊優惠券 [號碼]");
                pc.sendPackets("優惠券號碼不存在。");
                return;
            }

            if (檢查優惠券(代碼.toUpperCase())) {
                pc.sendPackets("該優惠券號碼已被使用。");
                return;
            }

            SupplementaryService 倉庫服務 = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
            if (倉庫服務 == null)
                return;

            支付信息.set_account_name(pc.getAccountName())
                    .set_character_name(pc.getName())
                    .set_expire_date(MJNSHandler.getLocalTime())
                    .set_is_use(true)
                    .do_update();

            L1Item 暫時物品 = ItemTable.getInstance().getTemplate(支付信息.get_itemid());
            if (暫時物品.isStackable()) {
                L1ItemInstance 物品 = ItemTable.getInstance().createItem(暫時物品.getItemId());
                物品.setIdentified(true);
                物品.setCount(支付信息.get_count());
                倉庫服務.storeTradeItem(物品);
            } else {
                L1ItemInstance 物品 = null;
                int 創建數量;
                for (創建數量 = 0; 創建數量 < 支付信息.get_count(); 創建數量++) {
                    物品 = ItemTable.getInstance().createItem(暫時物品.getItemId());
                    物品.setIdentified(true);
                    倉庫服務.storeTradeItem(物品);
                }
            }

            // 發送通知和效果
            SC_GOODS_INVEN_NOTI.do_send(pc);
            pc.send_effect(2048);
            pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format("\f3優惠券物品已發放 (%s) 件。請前往附加物品倉庫領取。", new DecimalFormat("#,##0").format(支付信息.get_count()))));
            pc.sendPackets(String.format("優惠券物品已發放 (%s) 件。", new DecimalFormat("#,##0").format(支付信息.get_count())));

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

    private void 應用中心(L1PcInstance pc, String param) {
        try {
            StringTokenizer st = new StringTokenizer(param);
            String 開關 = st.nextToken();

            if (開關.equalsIgnoreCase("開")) {
                Config.Web.webServerOnOff = true;
                pc.sendPackets("\f2應用中心已啟用。");
                // L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\f2應用中心的臨時維護已結束，現在可使用。"));
                // L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f2應用中心的臨時維護已結束，現在可使用。"));
            } else if (開關.equalsIgnoreCase("關")) {
                Config.Web.webServerOnOff = false;
                pc.sendPackets("\f3應用中心已停用。");
                // L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\aG應用中心的臨時維護已開始，暫時無法使用。"));
                // L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\aG應用中心的臨時維護已開始，暫時無法使用。"));
            } else if (開關.equalsIgnoreCase("更改端口")) {
                int 端口 = Integer.parseInt(st.nextToken(), 10);
                Config.Web.webServerPort = 端口;
                pc.sendPackets("應用中心端口已更改。");
            } else if (開關.equalsIgnoreCase("交易")) {
                if (Config.Web.tradeMenu) {
                    Config.Web.tradeMenu = false;
                    pc.sendPackets("應用中心交易公告欄菜單已啟用。");
                } else {
                    Config.Web.tradeMenu = true;
                    pc.sendPackets("應用中心交易公告欄菜單暫時停用。");
                }
            }
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("使用方法：.應用中心 [開/關/更改端口(更改前請先停用)/交易]"));
        }
    }

	/*private void PowerBall(L1PcInstance pc, String param) {
		// TODO 자동 생성된 메소드 스텁
		try {
			StringTokenizer st = new StringTokenizer(param);
			String onoff = st.nextToken();
			if (onoff.equalsIgnoreCase("켬")) {
				//PowerBallController.getInstance()._executeStatus = 0;
				//pc.sendPackets("파워볼 시스템을 켭니다.");
				// L1World.getInstance().broadcastPacketToAll(new
				// S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\f2앱센터가 임시점검이 종료되었습니다. 이용가능 합니다."));
			} else if (onoff.equalsIgnoreCase("끔")) {
				//PowerBallController.getInstance()._executeStatus = 5;
				//pc.sendPackets("파워볼 시스템을 끕니다.");
				// L1World.getInstance().broadcastPacketToAll(new
				// S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aG앱센터가 잠시 임시점검이 시작되었습니다. 이용이 불가능
				// 합니다."));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".파워볼 [켬 / 끔 "));
		}
	}*/


    public static void 增加N金幣(final L1PcInstance gm, final String name, final int coin) {
        L1PcInstance tg = L1World.getInstance().getPlayer(name);
        if (tg != null) {
            tg.addNcoin(coin);
            tg.getNetConnection().getAccount().updateNcoin();

            String s = String.format("\aGN金幣 (%,d) 元已充值。謝謝。", coin);
            tg.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, s));
            tg.sendPackets(s);
            // additem(tg, 4100681, coin, 0, 129, 0, true);

            String remainMessage = String.format("\f2持有 N金幣:(%,d) 元", tg.getNetConnection().getAccount().Ncoin_point);
            tg.sendPackets(remainMessage);
            gm.sendPackets(String.format("\aA角色名:[\aG%s\aA] 充值金額:[\aG%,d\aA] 完成! \f2[目標持有 N金幣:(%,d) 元]", name, coin, tg.getNetConnection().getAccount().Ncoin_point));
        } else {
            final MJObjectWrapper<String> accountWrapper = new MJObjectWrapper<String>();
            accountWrapper.value = MJString.EmptyString;
            Selector.exec("select account_name from characters where char_name=?", new SelectorHandler() {
                @override
                public void handle(PreparedStatement pstm) throws Exception {
                    pstm.setString(1, name);
                }

                @override
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
                    @override
                    public void handle(PreparedStatement pstm) throws Exception {
                        pstm.setInt(1, coin);
                        pstm.setString(2, accountWrapper.value);
                    }
                });
                gm.sendPackets(String.format("\aA角色名:[\aG%s(%s)\aA] 充值金額:[\aG%d\aA] 完成", name, accountWrapper.value, coin));
            }
        }
    }
    @Override
    public void handle(PreparedStatement pstm) throws Exception {
        pstm.setString(1, name);
    }



    public static void 增加N金幣商店(final L1PcInstance gm, final String name, final int coin, MJMyTradeShopModel shop) {
        L1PcInstance tg = L1World.getInstance().getPlayer(name);
        if (tg != null) {
            tg.addNcoin(coin);
            tg.getNetConnection().getAccount().updateNcoin();
            tg.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f2在應用中心(CTRL+Z)交易所售出的物品已售出，N金幣已存入。"));
            tg.sendPackets(String.format("\f2您在交易所出售的物品已售出，N金幣 (%,d) 元已存入。", shop.itemModel().price()));
            tg.sendPackets(String.format("\f2持有 N金幣:(%,d) 元", tg.getNetConnection().getAccount().Ncoin_point));
        } else {
            final MJObjectWrapper<String> accountWrapper = new MJObjectWrapper<String>();
            accountWrapper.value = MJString.EmptyString;
            Selector.exec("select account_name from characters where char_name=?", new SelectorHandler() {
                @override
                public void handle(PreparedStatement pstm) throws Exception {
                    pstm.setString(1, name);
                }
                @override
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
                    @override
                    public void handle(PreparedStatement pstm) throws Exception {
                        pstm.setInt(1, coin);
                        pstm.setString(2, accountWrapper.value);
                    }
                });
                gm.sendPackets(String.format("\aA角色名:[\aG%s(%s)\aA] 充值金額:[\aG%d\aA] 完成", name, accountWrapper.value, coin));
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
            } else if (name.equalsIgnoreCase("開發者")) {
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
                gm.sendPackets("該用戶目前不在線上。");
                return;
            }
            MJPoint pt = MJPoint.newInstance(33441, 32813, 5, (short) 4, 50);
            if (server.equalsIgnoreCase("本地")) {
                SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(target, pt.x, pt.y, pt.mapId, 1);
                gm.sendPackets(target.getName() + " 已被移動到本地伺服器。");
            } else if (server.equalsIgnoreCase("開發者")) {
                SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(target, pt.x, pt.y, pt.mapId, 2);
                gm.sendPackets(target.getName() + " 已被移動到開發者伺服器。");
            } else if (server.equalsIgnoreCase("伺服器")) {
                SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(target, pt.x, pt.y, pt.mapId, 3);
                gm.sendPackets(target.getName() + " 已被移動到正常伺服器。");
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
                    MJPoint pt = MJPoint.newInstance(33432, 32811, 10, (short) 4, 50);
                    if (server.equalsIgnoreCase("本地")) {
                        SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(pc, pt.x, pt.y, pt.mapId, 1);
                    } else if (server.equalsIgnoreCase("開發者")) {
                        SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(pc, pt.x, pt.y, pt.mapId, 2);
                    } else if (server.equalsIgnoreCase("伺服器")) {
                        SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send_jump(pc, pt.x, pt.y, pt.mapId, 3);
                    }
                    count++;
                }
            }
            gm.sendPackets(String.format("提示：已將 %d 名玩家移動到 [%s]。", count, server));
        } catch (Exception e) {
            gm.sendPackets(new S_SystemMessage(".全體跳轉 本地/伺服器"));
        }
    }

    public static void NoticeChat(L1PcInstance gm, String param) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(param);
            String chat = tokenizer.nextToken();
            if (chat.equalsIgnoreCase("開")) {
                gm.Notice = true;
                gm.sendPackets(new S_SystemMessage("將全體聊天切換為公告模式。"));
            } else if (chat.equalsIgnoreCase("關")) {
                gm.Notice = false;
                gm.sendPackets(new S_SystemMessage("將公告模式切換為普通全體聊天。"));
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
                gm.sendPackets(minute + "秒內大廳隊列系統啟用");
                GeneralThreadPool.getInstance().schedule(new Runnable() {
                    @override
                    public void run() {
                        MJClientEntranceService.service().useWaitQueue(false);
                    }
                }, minute * 1000L);
            } else if (type.equalsIgnoreCase("重新載入")) {
                MJMonitorCacheProvider.monitorCache().getContent("mj-entrance-model").readContent();
                gm.sendPackets("mj-entrance-model 重新載入完畢");
            }

        } catch (Exception e) {
            gm.sendPackets(".隊列 [設定/重新載入] - 設定時請輸入持續的秒數。");
        }
    }

    private void mapwho(L1PcInstance gm, String param) {
        try {
            StringTokenizer st = new StringTokenizer(param);
            int mapId = 0;
            try {
                mapId = Integer.parseInt(st.nextToken(), 10);
            } catch (Exception e) {
                mapId = gm.getMapId();
            }

            StringBuffer gmList = new StringBuffer();
            StringBuffer playList = new StringBuffer();
            StringBuffer noplayList = new StringBuffer();
            StringBuffer shopList = new StringBuffer();

            int countGM = 0, nocountPlayer = 0, countPlayer = 0, countShop = 0;

            for (L1Object obj : L1World.getInstance().getVisibleObjects(mapId).values()) {
                if (obj instanceof L1PcInstance) {
                    L1PcInstance player = (L1PcInstance) obj;

                    if (player.isGm()) {
                        gmList.append("名稱: " + player.getName() + " / 等級: " + player.getLevel() + "
                                ");
                                countGM++;
                        continue;
                    }

                    if (!player.isPrivateShop() && !player.isPrivateShop()) {
                        if (player.noPlayerCK) {
                            noplayList.append(player.getName() + ", ");
                            nocountPlayer++;
                            continue;
                        } else {
                            playList.append("名稱: " + player.getName() + " / 等級: " + player.getLevel() + "
                                    ");
                                    countPlayer++;
                            continue;
                        }
                    }

                    if (player.isPrivateShop() && player.isPrivateShop()) {
                        shopList.append(player.getName() + ", ");
                        countShop++;
                    }
                }
            }

            if (gmList.length() > 0) {
                gm.sendPackets(new S_SystemMessage("-- 管理員 (" + countGM + "人)"));
                gm.sendPackets(new S_SystemMessage(gmList.toString()));
            }

            if (noplayList.length() > 0) {
                gm.sendPackets(new S_SystemMessage("-- 虛擬角色 (" + nocountPlayer + "個)"));
                gm.sendPackets(new S_SystemMessage(noplayList.toString()));
            }

            if (playList.length() > 0) {
                gm.sendPackets(new S_SystemMessage("-- 玩家 (" + countPlayer + "人)"));
                gm.sendPackets(new S_SystemMessage(playList.toString()));
            }

            if (shopList.length() > 0) {
                gm.sendPackets(new S_SystemMessage("-- 個人商店 (" + countShop + "人)"));
                gm.sendPackets(new S_SystemMessage(shopList.toString()));
            }

        } catch (Exception e) {
            gm.sendPackets("指令: 請使用 '.誰 [地圖編號]' 格式設置。");
        }
    }

    public static void Event_System(L1PcInstance gm, String param) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(param);
            String code = tokenizer.nextToken();
            int eventNumber = Integer.parseInt(tokenizer.nextToken(), 10);

            if (EventSystemLoader.getInstance().getEventSystemSize() < eventNumber) {
                gm.sendPackets("請輸入正確的事件編號。");
                return;
            }

            if (code.equalsIgnoreCase("開始")) {
                EventSystemTimeController.getInstance().initScs(eventNumber);
                GeneralThreadPool.getInstance().execute(new EventSystemSpawner(eventNumber, EventSystemTimeController.FS_START));
            } else if (code.equalsIgnoreCase("結束")) {
                GeneralThreadPool.getInstance().execute(new EventSystemSpawner(eventNumber, EventSystemTimeController.FS_END));
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

    private void Sealedoff(L1PcInstance pc, String param) {
        try {
            StringTokenizer tok = new StringTokenizer(param);
            String param1 = tok.nextToken();
            int off = Integer.parseInt(param1);
            if (off > 10 || off < 0) {
                pc.sendPackets("\f2一次最多可申請 10 張。");
                return;
            }
            if (pc._create_password) {
                pc.sendPackets("\f2密碼註冊失敗後，30 秒後可以再試一次。\f3(更改密碼時可能會斷線)");
                return;
            }

            if (pc._seal_scroll) {
                pc.sendPackets("\f2密碼驗證失敗後，30 秒後可以再試一次。\f3(更改密碼時可能會斷線)");
                return;
            }

            if (pc.getAccount().getShopPassword() == 0) {
                pc._create_password = true;
                pc.sendPackets("\f2首次使用時，請輸入密碼。");
            } else {
                pc._seal_scroll = true;
                pc._seal_scroll_count = off;
                pc.sendPackets("\f230 秒內請驗證密碼。");
            }
            pc.sendPackets(834);
        } catch (Exception e) {
            pc.sendPackets("\f2指令格式錯誤。");
        }
    }

     GeneralThreadPool.getInstance().schedule(new Runnable() {

         @override

         public void run() {
             try {
                 if (pc._seal_scroll) {
                     pc._seal_scroll = false;
                     pc.sendPackets("\f2時間已過。密碼驗證失敗，請再試一次。");
                 }
                 if (pc._create_password) {
                     pc._create_password = false;
                     pc.sendPackets("\f2時間已過。密碼創建失敗，請再試一次。");
                 }
                 pc._seal_scroll_count = 0;
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         }, 30000);
 } catch (Exception e) {
     pc.sendPackets("\f2.密封解除申請 (申請的數量)");
 }

 public static void BuyLimitSystem(L1PcInstance gm, String param) {
     try {
         StringTokenizer tokenizer = new StringTokenizer(param);
         String first_code = tokenizer.nextToken();
         if (first_code.equalsIgnoreCase("重新加載")) {
             BuyLimitSystem.getInstance().reload();
             gm.sendPackets("\f2正在重新加載購買限制項目列表。");
         } else if (first_code.equalsIgnoreCase("重置")) {
             String second_code = tokenizer.nextToken();
             if (second_code.equalsIgnoreCase("帳號")) {
                 BuyLimitSystemAccountTable.reload();
                 gm.sendPackets("\f2正在重置所有購買限制項目的購買記錄。（帳號）");
             } else if (second_code.equalsIgnoreCase("角色")) {
                 BuyLimitSystemCharacterTable.reload();
                 gm.sendPackets("\f2正在重置所有購買限制項目的購買記錄。（角色）");
             } else {
                 gm.sendPackets("\f2.購買限制 重置 帳號/角色");
             }
         }
      } catch (Exception e) {
         gm.sendPackets("\f2[.購買限制 [重新加載/重置 (帳號/角色)]");
      }
 }

 private void Dragon_raid(L1PcInstance gm, String param) {
     try {
         StringTokenizer st = new StringTokenizer(param);
         int commandnum = Integer.parseInt(st.nextToken(), 10);
         List<DragonRaidSystemInfo> list = DragonRaidSystemLoader.getList();
         if (commandnum == 0) {
             DragonRaidSystemLoader.reload();
             gm.sendPackets("正在重新加載龍襲擊信息。");
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
                                     gm.sendPackets("\f3安塔瑞斯襲擊 強制開始");
                                 } else {
                                     L1NpcInstance npc = (L1NpcInstance) teleporter;
                                     npc.getMap().setPassable(npc.getX(), npc.getY(), true);
                                     npc.deleteMe();
                                     gm.sendPackets("\f3安塔瑞斯襲擊 強制結束");
                                 }
                             }
                             break;
                             case 2:
                                 if (b.getBossNum() == 2) {
                                     RaidOfFafurion fafu = new RaidOfFafurion();
                                     L1Object teleporter = L1World.getInstance().findNpc(73201222);
                                     if (teleporter == null) {
                                         fafu.Start();
                                         gm.sendPackets("\f3法利昂襲擊 強制開始");
                                     } else {
                                         L1NpcInstance npc = (L1NpcInstance) teleporter;
                                         npc.getMap().setPassable(npc.getX(), npc.getY(), true);
                                         npc.deleteMe();
                                         gm.sendPackets("\f3法利昂襲擊 強制結束");
                                     }
                                 }
                                 break;
                                 case 3:
                                     if (b.getBossNum() == 3) {
                                         RaidOfLindvior lind = new RaidOfLindvior();
                                         L1Object teleporter = L1World.getInstance().findNpc(73201223);
                                         if (teleporter == null) {
                                             lind.Start();
                                             gm.sendPackets("\f3林德拜爾襲擊 強制開始");
                                         } else {
                                             L1NpcInstance npc = (L1NpcInstance) teleporter;
                                             npc.getMap().setPassable(npc.getX(), npc.getY(), true);
                                             npc.deleteMe();
                                             gm.sendPackets("\f3林德拜爾襲擊 強制結束");
                                         }
                                     }
                                     break;
                                     case 4:
                                         if (b.getBossNum() == 4) {
                                             RaidOfValakas vala = new RaidOfValakas();
                                             L1Object teleporter = L1World.getInstance().findNpc(73201224);
                                             if (teleporter == null) {
                                                 vala.Start();
                                                 gm.sendPackets("\f3巴拉卡斯襲擊 強制開始");
                                             } else {
                                                 L1NpcInstance npc = (L1NpcInstance) teleporter;
                                                 npc.getMap().setPassable(npc.getX(), npc.getY(), true);
                                                 npc.deleteMe();
                                                 gm.sendPackets("\f3巴拉卡斯襲擊 強制結束");
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
         gm.sendPackets("\f2[.龍襲擊 0~4 (0=重新加載, 1=安塔瑞斯, 2=法利昂, 3=林德拜爾, 4=巴拉卡斯)]");

     }

}
public static void addBlessPoint(L1PcInstance gm, String param) {
     try {
         StringTokenizer st = new StringTokenizer(param);
         String name = st.nextToken();
         int point = Integer.parseInt(st.nextToken());
         L1PcInstance pc = L1World.getInstance().getPlayer(name);
         if (pc != null) {
             pc.getAccount().addGmBlessOfAinBonusPoint(point);
             SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
             pc.sendPackets(String.format("阿因哈薩德點數已處理 %,d 點。", point));
         } else {
             gm.sendPackets(String.format("%s 玩家目前在世界上不存在。", name));
         }
      } catch (Exception e) {
         gm.sendPackets("\f2[.阿因禮物 角色名 點數(回收時使用 -)]");
      }
 }

 public static void addBlessCard(L1PcInstance gm, String param) {
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
                 gm.sendPackets(String.format("%s 玩家無法獲得那麼多點數。", name));
                 gm.sendPackets(String.format("可發放的點數: %s", 180 - Info.get_total_stat()));
                 return;
             } else {
                 SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
                 SC_EINHASAD_POINT_ENCHANT_START_ACK.send_enchant_start(pc, Info.get_cur_enchant_level());

                 Info.set_cur_enchant_level(originstat + point);
                 Info.add_total_stat(originstat + point);

                 SC_EINHASAD_POINT_ENCHANT_STAT_ACK.send_point_enchant_stat(pc, point, 0, Info.get_cur_enchant_level(), Info.get_total_stat());

                 AinhasadSpecialStatLoader.getInstance().updateSpecialStat(pc);
                 pc.sendPackets(String.format("阿因哈薩德點數已處理 %,d 點。", point));
             }
         } else {
             gm.sendPackets(String.format("%s 玩家目前在世界上不存在。", name));
         }
      } catch (Exception e) {
         gm.sendPackets("\f2[.阿因卡片 角色名 點數(回收時使用 -)]");
      }
 }

 public static void SpellAskCommands(L1PcInstance gm, String param) {
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
          } else { // 無法攜帶的情況下，不會取消掉落到地面的處理（防止作弊）
              pc.sendPackets(new S_ServerMessage(82));
              // 因重量不足或背包已滿，無法再攜帶更多。
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

         if(cmd_type.equalsIgnoreCase("確認")) {
             String target = st.nextToken();

             L1PcInstance tr = L1World.getInstance().getPlayer(target);
             if(tr != null) {
                 for(L1ItemInstance item : tr.getInventory().getItems()) {
                     if(item.getItem().getType2() == 0)
                         continue;

                     pc.sendPackets(new S_SystemMessage("[[ 物品 : " + item.getName() + " / 物品ID : " + item.getId() + "]]"), true);
                 }
             } else {
                 pc.sendPackets(new S_SystemMessage("玩家不存在。"), true);
             }
         } else if (cmd_type.equalsIgnoreCase("賦予")) {
             String target = st.nextToken();
             int objid = Integer.valueOf(st.nextToken());
             int type = Integer.valueOf(st.nextToken());
             int value = Integer.valueOf(st.nextToken());

             L1PcInstance tr = L1World.getInstance().getPlayer(target);
             if (tr != null) {
                 L1ItemInstance item = tr.getInventory().findItemObjId(objid);

                 if (item != null) {
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
                     tr.sendPackets(new S_SystemMessage(item.getLogName() + "被賦予了祝福的氣息。"));
                     pc.sendPackets(new S_SystemMessage(target + " 的 " + item.getName() + " 被賦予了祝福效果 " + type + " 號。[數值: " + value + "]"), true);
                 }
             }
         }

     } catch (Exception e) {
         pc.sendPackets(new S_SystemMessage(".祝福賦予 [確認/賦予]"), true);
         pc.sendPackets(new S_SystemMessage(".祝福賦予 [確認] [目標用戶名稱]"), true);
         pc.sendPackets(new S_SystemMessage(".祝福賦予 [賦予] [目標用戶名稱] [目標物品ID] [祝福號碼] [數值]"), true);

     }

}

public void AccountPause(L1PcInstance pc, String param) {
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

         if (target != null) { // 將帳號暫停
             final GameClient clnt = target.getNetConnection();
             updateAccountPauseTime(target.getAccountName(), pause_time, reason);
             pc.sendPackets(String.format("%s 的帳號已被暫停 %d 小時，原因：[%s]。", target.getName(), pause_time, reason));
             target.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3您的帳號目前已被暫停。"));
             target.sendPackets("\f3您的帳號目前已被暫停。");
             target.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
             // target.sendPackets(new S_Disconnect());

        if (target.getOnlineStatus() == 1) {
            target.sendPackets(new S_Disconnect());
        }

        GeneralThreadPool.getInstance().schedule(new Runnable() {
            @override
            public void run() {
                if (clnt != null && clnt.isConnected()) {
                    try {
                        clnt.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            }, 1000L);
         } else {
             pc.sendPackets(String.format("%s 是不存在的角色。", name));
         }
      } catch (Exception e) {
         pc.sendPackets(new S_SystemMessage(".帳號暫停 [角色名] [暫停時間(1=1小時)][解除暫停時使用 -] [暫停原因]"));
      }
 }

 public void updateAccountPauseTime(String account_name, int time, String reason) {
     Connection con = null;
      PreparedStatement pstm = null;
       try {
           Account account = Account.load(account_name);
           account.updateLastLogOut();
           con = L1DatabaseFactory.getInstance().getConnection();
           String sqlstr = "UPDATE accounts SET account_pause=?, account_pause_reason=? WHERE login = ?";
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


 private void doll_test(L1PcInstance gm, String param) {
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
                gm.sendPackets(new S_SystemMessage(String.format("\f2找不到 %s。", name)), true);
                return;
            }
            L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
            item.setCount(count);
            item.set_Doll_Bonus_Level(doll_level);
            item.set_Doll_Bonus_Value(doll_val);
            [20:06]
        if (item.getItem().getUseType() != 73) {
            gm.sendPackets(new S_SystemMessage("\f2只能贈送人偶類型的物品。請使用 (.아이템) 命令來贈送其他物品。"), true);
            return;
        }

        if (item != null) {
            if (target.getInventory().checkAddItem_doll(item, count, doll_level, doll_val) == L1Inventory.OK) {
                target.getInventory().storeItem(item, true);
            }
        }
        target.sendPackets(new S_SystemMessage(String.format("\f2[物品名稱: %s] [數量: %d] 獲取成功，請檢查您的背包！", item.getName(), count)), true);
        gm.sendPackets(new S_SystemMessage(String.format("\f2[角色名稱: %s] [物品名稱: %s] [數量: %d] [等級: %d] [效果: %d] 發放完成！", target.getName(), item.getName(), count, doll_level, doll_val)), true);
        } catch (Exception exception) {
            gm.sendPackets(new S_SystemMessage("\f2.潛力 [角色名稱] [itemid(娃娃)] [數量] [等級:1~4] [效果:1~143]"), true);
            gm.sendPackets(new S_SystemMessage("\f2[潛力等級] 潛力存在 1~4 等級。 [潛力效果] 效果存在 1~143。 請參考天堂官網上的潛力說明。 例如，如果要在一般等級上增加遠程致命一擊 +1，請使用命令："

        + ".潛力 角色名稱 746(物品編號) 1(數量) 1(等級) 2(效果)" + "這樣將會增加 1 級遠程致命一擊 +1。" + "(134/135 無效，請排除)"), true);
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
                 pc.sendPackets(new S_SystemMessage("宏已添加內容。"));
                 pc.sendPackets(new S_SystemMessage(" - [" + ment + "]"));
             } else {
                 pc.sendPackets(new S_SystemMessage(".宏 [設定/開始/停止/刪除/查看] [設定時添加的內容] [刪除時的順序號碼] 請輸入。"));
             }
         } else if (setting.equalsIgnoreCase("查看")) {
             pc.getMacroListIdentify();
         } else if (setting.equalsIgnoreCase("刪除")) {

             String index = tok.nextToken();
             if (index.equalsIgnoreCase("全部")) {
                 pc.getMacroList().clear();
                 pc.sendPackets(new S_SystemMessage("所有宏內容已被刪除。"));
             } else {
                 if (isStringDouble(index)) {
                     int real_index = Integer.valueOf(index);
                     if (real_index > pc.getMacroList().size()) {
                         pc.sendPackets(new S_SystemMessage("不存在該順序號碼的宏。"));
                         return;
                     }
                     pc.getMacroList().remove(real_index);
                     pc.sendPackets(new S_SystemMessage("[" + real_index + "號] 宏內容已被刪除。"));
                 } else {
                     pc.sendPackets(new S_SystemMessage("刪除順序號碼必須是數字。"));
                 }
             }
         } else if (setting.equalsIgnoreCase("開始")) {
             if (pc.getMacroList().size() <= 0) {
                 pc.sendPackets(new S_SystemMessage("沒有已輸入的宏。"));
                 return;
             }
             if (pc.isMacroTimerStart()) {
                 pc.sendPackets(new S_SystemMessage("宏已經在執行。"));
                 return;
             }

             pc.startMacroTimer();
             pc.sendPackets(new S_SystemMessage("宏已開始。"));
         } else if (setting.equalsIgnoreCase("停止")) {
             if (!pc.isMacroTimerStart()) {
                 pc.sendPackets(new S_SystemMessage("目前沒有正在執行的宏。"));
                 return;
             }

             pc.stopMacroTimer();
             pc.sendPackets(new S_SystemMessage("宏已停止。"));
         } else if (setting.equalsIgnoreCase("全部停止")) {
             for (L1PcInstance tr : L1World.getInstance().getAllPlayers()) {
                 if (tr == null || tr.noPlayerCK)
                     continue;

                 tr.stopMacroTimer();
             }
             L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\f3所有玩家的宏功能已停止。"));
         } else {
             pc.sendPackets(new S_SystemMessage("無效的請求。"));
         }

     } catch (Exception e) {
         pc.sendPackets(new S_SystemMessage(".宏 [設定/開始/停止/全部停止/刪除/查看] [設定時添加的內容] [刪除時的順序號碼] 請輸入。"));

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

    public void insertCashInfo(L1PcInstance pc, String param) {
     try {
         StringTokenizer st = new StringTokenizer(param);
         int charge_count = Integer.parseInt(st.nextToken());

         if (pc == null)
             return;

         if (charge_count < 10000) {
             pc.sendPackets(new S_SystemMessage("充值的最低金額為 10000 元。"));
             return;
         }

         if (charge_count > 500000) {
             pc.sendPackets(new S_SystemMessage("充值的最高金額為 50 萬元。"));
             return;
         }

         if (getDepositInfo(pc) == 0) {
             pc.sendPackets(new S_SystemMessage("已有進行中的充值請求。"));
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
         pc.sendPackets(new S_SystemMessage(".N幣充值 [金額]"));
      }
 }

        private void do_write_letter_command(L1PcInstance pc, final int notify_id) {
    final MJObjectWrapper<String> subject = new MJObjectWrapper<String>();
    final MJObjectWrapper<String> content = new MJObjectWrapper<String>();

     Selector.exec("select * from letter_command where id=?", new SelectorHandler() {
@override
public void handle(PreparedStatement pstm) throws Exception {
        pstm.setInt(1, notify_id);
        }

@override
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
        throw new Exception(String.format("無法找到命令信件。ID：%d
        堆疊追蹤", notify_id));
        } catch (Exception e) {
        e.printStackTrace();
        }
        return;
        }

        String current_date = MJString.get_current_datetime();
        do_write_letter(pc.getName(), current_date, subject.value, content.value);
        }

private void do_write_letter_togm(String generate_date, String subject, String content) {
        do_write_letter("梅提斯", generate_date, subject, content);
        }

private void do_write_letter(String receiver, String generate_date, String subject, String content) {
        int id = LetterTable.getInstance().writeLetter(949, generate_date, "梅提斯", receiver, 0, subject, content);
        L1PcInstance pc = L1World.getInstance().getPlayer(receiver);
        if (pc != null) {
        pc.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_RECEIVE, "梅提斯",
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
             pc.sendPackets(new S_SystemMessage("[" + name + "] 不是在線用戶。"));
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
                         if (cq != null) {
                             cqu.setStatus(3); // 設置任務狀態為完成
        target.sendPackets(new S_SystemMessage("自定義任務 [" + cq.getQuestName() + "] 已完成。"));
        target.sendPackets(new S_SystemMessage("管理員已將 [" + cq.getQuestName() + "] 任務設置為完成。"));
        pc.sendPackets(new S_SystemMessage("已將 [" + name + "] 的 [" + cq.getQuestName() + "] 任務設置為完成。"));
                         } else {
                             pc.sendPackets(new S_SystemMessage("請求的任務在服務器中不存在。"));
                         }
                     } else {
                         pc.sendPackets(new S_SystemMessage("嘗試完成未獲得的任務。"));
                     }
                 }
             } else {
                 int questid = Integer.parseInt(quest_id);
                 CustomQuestUser cqu = target.getCustomQuestList().get(questid);
                 if (cqu != null) {
                     CustomQuest cq = ServerCustomQuestTable.getInstance().getCustomQuest(questid);
                     if (cq != null) {
                         cqu.setStatus(3); // 設置任務狀態為完成
        target.sendPackets(new S_SystemMessage("自定義任務 [" + cq.getQuestName() + "] 已完成。"));
        target.sendPackets(new S_SystemMessage("管理員已將 [" + cq.getQuestName() + "] 任務設置為完成。"));
        pc.sendPackets(new S_SystemMessage("已將 [" + name + "] 的 [" + cq.getQuestName() + "] 任務設置為完成。"));
                     } else {
                         pc.sendPackets(new S_SystemMessage("請求的任務在服務器中不存在。"));
                     }
                 } else {
                     pc.sendPackets(new S_SystemMessage("嘗試完成未獲得的任務。"));
                 }
             }
         } else if (type.equalsIgnoreCase("刪除")) {
             if (quest_id.equalsIgnoreCase("全部")) {
                 target.getCustomQuestList().clear();
                 target.sendPackets(new S_SystemMessage("管理員已刪除所有任務列表。"));
                 pc.sendPackets(new S_SystemMessage("已刪除 [" + name + "] 的所有任務列表。"));
             } else {
                 if (CommonUtil.isNumber(quest_id)) {
                     CustomQuest cq = ServerCustomQuestTable.getInstance().getCustomQuest(Integer.valueOf(quest_id));
                     target.getCustomQuestList().remove((Object) Integer.valueOf(quest_id));
                     target.sendPackets(new S_SystemMessage("管理員已刪除 [" + cq.getQuestName() + "] 任務。"));
                     pc.sendPackets(new S_SystemMessage("已刪除 [" + name + "] 的 [" + cq.getQuestName() + "] 任務。"));
                 } else {
                     pc.sendPackets(new S_SystemMessage("無效的請求。"));
                 }
             }
         } else {
             pc.sendPackets(new S_SystemMessage("無效的請求。"));
         }

     } catch (Exception e) {
         pc.sendPackets(new S_ChatPacket(pc, ".任務 [完成/刪除] [角色名] [任務編號或全部]"));
         e.printStackTrace();

     }

}

	
