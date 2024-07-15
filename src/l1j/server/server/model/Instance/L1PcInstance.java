package l1j.server.server.model.Instance;

import MJFX.UIAdapter.MJUIAdapter;
import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import MJShiftObject.MJEShiftObjectType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import l1j.server.AinhasadSpecialStat.AinhasadHpMpRegeneration;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.CPMWBQSystem.Database.CPMWBQUserTable;
import l1j.server.CPMWBQSystem.info.CPMWBQinfo;
import l1j.server.ClanBuffList.ClanBuffListLoader;
import l1j.server.Config;
import l1j.server.DeathMatch.DeathMatchSystem;
import l1j.server.FatigueProperty;
import l1j.server.IndunEx.RoomInfo.MJIndunRoomController;
import l1j.server.IndunEx.RoomInfo.MJIndunRoomModel;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJActionListener.ActionListenerLoader;
import l1j.server.MJActionListener.Npc.ListenerFinderTable;
import l1j.server.MJActionListener.Npc.TeleporterActionListener;
import l1j.server.MJAutoSystem.MJAutoMapInfo;
import l1j.server.MJBookQuestSystem.Loader.BQSCharacterDataLoader;
import l1j.server.MJBookQuestSystem.UserSide.BQSCharacterData;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJCaptchaSystem.MJCaptcha;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
import l1j.server.MJCombatSystem.MJCombatObserver;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJDeathPenalty.Exp.MJDeathPenaltyExpModel;
import l1j.server.MJDeathPenalty.Exp.MJDeathPenaltyexpDatabaseLoader;
import l1j.server.MJDeathPenalty.Item.MJDeathPenaltyItemDatabaseLoader;
import l1j.server.MJDeathPenalty.Item.MJDeathPenaltyItemModel;
import l1j.server.MJDeathPenalty.MJDeathPenaltyProvider;
import l1j.server.MJDeathPenalty.MJDeathPenaltyService;
import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.DungeonTimeUserInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeProgressLoader;
import l1j.server.MJDungeonTimer.Progress.AccountTimeProgress;
import l1j.server.MJDungeonTimer.Progress.CharacterTimeProgress;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJExpAmpSystem.MJExpAmplifier;
import l1j.server.MJInstanceSystem.MJInstanceEnums;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJItemExChangeSystem.S_ItemExSelectPacket;
import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.MJLevelupBonus.MJLevelBonus;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJPassiveSkill.MJPassiveInfo;
import l1j.server.MJPushitem.model.MJChaPushModel;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.MJTemplate.Attribute.MJAttrMap;
import l1j.server.MJTemplate.MJEPcStatus;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CompanionT;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_HUNTING_QUEST_REWARD_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EXP_BOOSTING_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_INSTANCE_HP_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_STAT_RENEWAL_BASESTAT_INFO_RES;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_STAT_RENEWAL_BASE_STAT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_STAT_RENEWAL_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_INDUN_TOWER_HIT_POINT_RATIO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FINISH_PLAY_SUPPORT_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FORCE_FINISH_PLAY_SUPPORT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_START_PLAY_SUPPORT_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOODPLEDGE_USER_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_System.SC_FREE_BUFF_SHIELD_INFO_ACK;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventHandler;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;
import l1j.server.MJTemplate.Regen.MJReGenerator;
import l1j.server.MJTemplate.Regen.MJRegeneratorLatestActions;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.MJWarSystem.MJWarFactory;
import l1j.server.revenge.MJRevengeService;
import l1j.server.revenge.model.MJRevengeModel;
import l1j.server.revenge.model.MJRevengeProvider;
import l1j.server.server.Account;
import l1j.server.server.Controller.FishingTimeController;
import l1j.server.server.Controller.GhostController;
import l1j.server.server.GMCommands;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.RepeatTask;
import l1j.server.server.SkillCheck;
import l1j.server.server.command.executor.L1HpBar;
import l1j.server.server.datatables.CharBuffTable;
import l1j.server.server.datatables.CharacterFreeShieldTable;
import l1j.server.server.datatables.CharacterSkillDelayTable;
import l1j.server.server.datatables.CharacterSlotItemTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.CharactersGiftItemTable;
import l1j.server.server.datatables.ClanBuffTable;
import l1j.server.server.datatables.ClanStorageTable;
import l1j.server.server.datatables.DropDelayItemTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LevelupBookmark;
import l1j.server.server.datatables.NoDropItemTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.AHRegeneration;
import l1j.server.server.model.Ability;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.HalloweenRegeneration;
import l1j.server.server.model.HpARegeneration;
import l1j.server.server.model.HpRegeneration;
import l1j.server.server.model.L1Astar;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1DwarfForPackageInventory;
import l1j.server.server.model.L1EquipmentSlot;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1HateList;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Karma;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Node;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1ReturnStatTemp;
import l1j.server.server.model.L1StatReset;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.MpARegeneration;
import l1j.server.server.model.MpRegeneration;
import l1j.server.server.model.PapuBlessing;
import l1j.server.server.model.ReportDeley;
import l1j.server.server.model.SHRegeneration;
import l1j.server.server.model.ValakasBlessing;
import l1j.server.server.model.classes.L1ClassFeature;
import l1j.server.server.model.gametime.GameTimeCarrier;
import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookUserLoader;
import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionUserLoader;
import l1j.server.server.model.item.itemdelay.ItemDelayTimer;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.monitor.L1PcAutoUpdate;
import l1j.server.server.model.monitor.L1PcExpMonitor;
import l1j.server.server.model.monitor.L1PcGhostMonitor;
import l1j.server.server.model.monitor.L1PcHellMonitor;
import l1j.server.server.model.monitor.L1PcInvisDelay;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.SkillData;
import l1j.server.server.model.skill.noti.MJNotiSkillModel;
import l1j.server.server.model.skill.noti.MJNotiSkillService;
import l1j.server.server.monitor.Logger;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_BlueMessage;
import l1j.server.server.serverpackets.S_CastleMaster;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharSpeedUpdate;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ClanAttention;
import l1j.server.server.serverpackets.S_ClanName;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_Fishing;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NewChat;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_Teleport;
import l1j.server.server.serverpackets.S_TrueTargetNew;
import l1j.server.server.serverpackets.S_Weight;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.CustomQuestUser;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1DropDelayItem;
import l1j.server.server.templates.L1FreeShield;
import l1j.server.server.templates.L1InventorySlot;
import l1j.server.server.templates.eCustomQuestType;
import l1j.server.server.types.Point;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class L1PcInstance extends L1Character {
    public static final MJAttrKey<Long> latestNpcClickMillis = MJAttrKey.newInstance("mj-pc-latest-npc-click");

    public static final MJAttrKey<L1PcExpMonitor> expMonitorKey = MJAttrKey.newInstance("pc-exp-monitor");

    public static final MJAttrKey<List<MJRevengeModel>> revengeModelKey = MJAttrKey.newInstance("mj-pc-revenge-model");

    public static final MJAttrKey<MJRevengeModel> revengePursuitModelKey = MJAttrKey.newInstance("mj-pc-revenge-pursuit-model");

    public static final MJAttrKey<List<MJDeathPenaltyItemModel>> deathpenaltyitemModelKey = MJAttrKey.newInstance("mj-pc-death-penalty-item-model");

    public static final MJAttrKey<List<MJDeathPenaltyExpModel>> deathpenaltyexpModelKey = MJAttrKey.newInstance("mj-pc-death-penalty-exp-model");

    public static final MJAttrKey<List<MJChaPushModel>> pcpushmodelkey = MJAttrKey.newInstance("mj-pc-push-model");

    public boolean _create_password = false;

    public boolean _seal_scroll = false;

    public int _seal_scroll_count = 0;

    public boolean _ClassChange = false;

    public boolean _LottoSelect = false;

    private long FishingShopBuyTime_1;

    public boolean _BonusExpItem = false;

    public boolean Notice = false;

    public boolean _PolyMasterCheck = false;

    public boolean _PolyMaster2Check = false;

    public boolean _GahoCheck = false;

    public boolean _CubeEffect = false;

    public boolean _Blessleaf = false;

    public boolean _EnchantWeaponSuccess = false;

    public boolean _EnchantArmorSuccess = false;

    public boolean _CraftSuccess = false;

    public boolean _ErzabeBox = false;

    public boolean _SandwormBox = false;

    public int  = 0;

    public boolean war_zone = false;

    public boolean _isPacketAttack = false;

    private boolean _ismarket;

    private boolean _isSM;

    private int _returnstatus;

    private L1StatReset _statReset;

    public boolean getIsPacketAttack() {
        return this._isPacketAttack;
    }

    public void setIsPacketAttack(boolean flag) {
        this._isPacketAttack = flag;
    }

    public int getNcoin() {
        if (getAccount() != null)
            return (getNetConnection().getAccount()).Ncoin_point;
        return 0;
    }

    public boolean isMarket() {
        return this._ismarket;
    }

    public void setMarket(boolean flag) {
        this._ismarket = flag;
    }

    public boolean isSM() {
        return this._isSM;
    }

    public void setSM(boolean flag) {
        this._isSM = flag;
    }

    public synchronized int getReturnStatus() {
        return this._returnstatus;
    }

    public synchronized void setReturnStatus(int i) {
        this._returnstatus = i;
    }

    public void setStatReset(L1StatReset sr) {
        this._statReset = sr;
    }

    public L1StatReset getStatReset() {
        return this._statReset;
    }

    public L1ReturnStatTemp rst = null;

    private int boss_spawn_yn;

    private int boss_spawn_npc;

    public void addNcoin(int coin) {
        if (getNetConnection() != null &&
                getNetConnection().getAccount() != null) {
            (getNetConnection().getAccount()).Ncoin_point += coin;
            getNetConnection().getAccount().updateNcoin();
        }
    }

    public void addNcoin1(int coin) {
        if (getNetConnection() != null &&
                getNetConnection().getAccount() != null) {
            (getNetConnection().getAccount()).Ncoin_point -= coin;
            getNetConnection().getAccount().updateNcoin();
        }
    }

    public int getBossYN() {
        return this.boss_spawn_yn;
    }

    public void setBossYN(int i) {
        this.boss_spawn_yn = i;
    }

    public int getBossNpc() {
        return this.boss_spawn_npc;
    }

    public void setBossNpc(int i) {
        this.boss_spawn_npc = i;
    }

    private boolean specialBuff = false;

    private ReportDeley _reportdeley;

    public void setSpecialBuff(boolean flag) {
        this.specialBuff = flag;
    }

    public boolean isSpecialBuff() {
        return this.specialBuff;
    }

    public void startReportDeley() {
        this._reportdeley = new ReportDeley(this);
        if (_regenTimer != null) {
            _regenTimer.cancel();
            _regenTimer = null;
        }
        _regenTimer = new Timer(true);
        _regenTimer.schedule((TimerTask)this._reportdeley, 100000L);
    }

    private boolean _isReport = true;

    public void setReport(boolean _isreport) {
        this._isReport = _isreport;
    }

    public boolean isReport() {
        return this._isReport;
    }

    private boolean _halpaspaith = true;

    private static final long serialVersionUID = 1L;

    public static final int CLASSID_PRINCE = 0;

    public static final int CLASSID_PRINCESS = 1;

    public static final int CLASSID_KNIGHT_MALE = 20553;

    public static final int CLASSID_KNIGHT_FEMALE = 48;

    public static final int CLASSID_ELF_MALE = 138;

    public static final int CLASSID_ELF_FEMALE = 37;

    public static final int CLASSID_WIZARD_MALE = 20278;

    public static final int CLASSID_WIZARD_FEMALE = 20279;

    public static final int CLASSID_DARK_ELF_MALE = 2786;

    public static final int CLASSID_DARK_ELF_FEMALE = 2796;

    public static final int CLASSID_DRAGONKNIGHT_MALE = 6658;

    public static final int CLASSID_DRAGONKNIGHT_FEMALE = 6661;

    public static final int CLASSID_BLACKWIZARD_MALE = 6671;

    public static final int CLASSID_BLACKWIZARD_FEMALE = 6650;

    public static final int CLASSID_ = 20567;

    public static final int CLASSID_ = 20577;

    public static final int CLASSID_FENCER_MALE = 18520;

    public static final int CLASSID_FENCER_FEMALE = 18499;

    public static final int CLASSID_LANCER_MALE = 19296;

    public static final int CLASSID_LANCER_FEMALE = 19299;

    public static final int REGENSTATE_NONE = 4;

    public static final int REGENSTATE_MOVE = 2;

    public static final int REGENSTATE_ATTACK = 2;

    public boolean ishalpaspaith() {
        return this._halpaspaith;
    }

    public void sethalpaspaith(boolean on) {
        this._halpaspaith = on;
    }

    public long tamtime = 0L;

    private boolean isSafetyZone;

    public int getComboCount() {
        return this.comboCount;
    }

    public void setComboCount(int comboCount) {
        this.comboCount = comboCount;
    }

    public boolean getSafetyZone() {
        return this.isSafetyZone;
    }

    public void setSafetyZone(boolean value) {
        if (this.isSafetyZone == value)
            return;
        this.isSafetyZone = value;
        if (hasSkillEffect(23069)) {
            int time = getSkillEffectTimeSec(23069);
            L1SkillUse.on_icons(this, 23069, time);
        }
    }

    private int noDelayTime = 0;

    public int getNoDelayTime() {
        return this.noDelayTime;
    }

    public void setNoDelayTime(int noDelayTime) {
        this.noDelayTime = noDelayTime;
    }

    private int _speedhackCount = 0;

    public int getSpeedHackCount() {
        return this._speedhackCount;
    }

    public void setSpeedHackCount(int x) {
        this._speedhackCount = x;
    }

    private boolean _raid = false;

    public void setRaidGame(boolean flag) {
        this._raid = flag;
    }

    public boolean getRaidGame() {
        return this._raid;
    }

    private boolean _Mayo = false;

    public void setMayo(boolean flag) {
        this._Mayo = flag;
    }

    public boolean getMayo() {
        return this._Mayo;
    }

    private boolean _Necross = false;

    public void setNecross(boolean flag) {
        this._Necross = flag;
    }

    public boolean getNecross() {
        return this._Necross;
    }

    private boolean _Tebeboss = false;

    public void setTebeboss(boolean flag) {
        this._Tebeboss = flag;
    }

    public boolean getTebeboss() {
        return this._Tebeboss;
    }

    private boolean _Curch = false;

    public void setCurch(boolean flag) {
        this._Curch = flag;
    }

    public boolean getCurch() {
        return this._Curch;
    }

    private boolean _dtah = false;

    public void setDeat(boolean flag) {
        this._dtah = flag;
    }

    public boolean getDeat() {
        return this._dtah;
    }

    private boolean _trac = false;

    public void setTrac(boolean flag) {
        this._trac = flag;
    }

    public boolean getTrac() {
        return this._trac;
    }

    private boolean _girtas = false;

    public void setGirtas(boolean flag) {
        this._girtas = flag;
    }

    public boolean getGirtas() {
        return this._girtas;
    }

    private boolean _orim = false;

    public void setOrim(boolean flag) {
        this._orim = flag;
    }

    public boolean getOrim() {
        return this._orim;
    }

    private boolean _erzarbe = false;

    public void setErzarbe(boolean flag) {
        this._erzarbe = flag;
    }

    public boolean getErzarbe() {
        return this._erzarbe;
    }

    private boolean _Hondon = false;

    public void setHondon(boolean flag) {
        this._Hondon = flag;
    }

    public boolean getHondon() {
        return this._Hondon;
    }

    private boolean _Reper = false;

    public void setReper(boolean flag) {
        this._Reper = flag;
    }

    public boolean getReper() {
        return this._Reper;
    }

    private boolean _Rekt = false;

    public void setRekt(boolean flag) {
        this._Rekt = flag;
    }

    public boolean getRekt() {
        return this._Rekt;
    }

    private boolean _Rekt1 = false;

    public void setRekt1(boolean flag) {
        this._Rekt1 = flag;
    }

    public boolean getRekt1() {
        return this._Rekt1;
    }

    private boolean _Rekt2 = false;

    public void setRekt2(boolean flag) {
        this._Rekt2 = flag;
    }

    public boolean getRekt2() {
        return this._Rekt2;
    }

    public boolean getgarmf() {
        return this._garmf;
    }

    private boolean _garmf = false;

    public void setgarmf(boolean flag) {
        this._garmf = flag;
    }

    public boolean getTaros() {
        return this._Taros;
    }

    private boolean _Taros = false;

    public void setTaros(boolean flag) {
        this._Taros = flag;
    }

    public boolean getCrock() {
        return this._Crock;
    }

    private boolean _Crock = false;

    public void setCrock(boolean flag) {
        this._Crock = flag;
    }

    public boolean getCrock1() {
        return this._Crock1;
    }

    private boolean _Crock1 = false;

    public void setCrock1(boolean flag) {
        this._Crock1 = flag;
    }

    public boolean getCrock2() {
        return this._Crock2;
    }

    private boolean _Crock2 = false;

    public void setCrock2(boolean flag) {
        this._Crock2 = flag;
    }

    public Account getAccount() {
        if (this._netConnection == null)
            return null;
        return this._netConnection.getAccount();
    }

    public boolean FouSlayer = false;

    public boolean TripleArrow = false;

    public boolean  = false;

    public int _x;

    private L1ItemInstance _fishingitem;

    public L1ItemInstance getFishingItem() {
        return this._fishingitem;
    }

    public void setFishingItem(L1ItemInstance item) {
        this._fishingitem = item;
    }

    private boolean _Attacklog = false;

    public void setAttackLog(boolean i) {
        this._Attacklog = i;
    }

    public boolean getAttackLog() {
        return this._Attacklog;
    }

    public long AttackControllerTime = 0L;

    public int AttackSpeedCheck2 = 0;

    public int MoveSpeedCheck = 0;

    public int magicSpeedCheck = 0;

    public long AttackSpeed2;

    public long MoveSpeed;

    public long magicSpeed;

    public int dx = 0;

    public int dy = 0;

    public short dm = 0;

    public int dh = 0;

    public int  = 0;

    public static final String WANTED_TITLE1 = "\\fe[;

    public static final String WANTED_TITLE2 = "\\fe[;

    public static final String WANTED_TITLE3 = "\\fe[;

    private int _Wanted_Level = 0;

    public int get_Wanted_Level() {
        return this._Wanted_Level;
    }

    public void set_Wanted_Level(int wanted_level) {
        this._Wanted_Level = wanted_level;
    }

    public void add_Wanted_Level() {
        this._Wanted_Level++;
        if (this._Wanted_Level >= 3)
            this._Wanted_Level = 3;
    }

    public void doWanted(boolean isOn, boolean login) {
        if (get_Wanted_Level() == 1) {
            Wanted_Level1(isOn, login);
        } else if (get_Wanted_Level() == 2) {
            Wanted_Level2(isOn, login);
        } else if (get_Wanted_Level() == 3) {
            Wanted_Level3(isOn, login);
        }
    }

    private int wanted_Dmg = Config.ServerAdSetting.WANTED_ABILITY[0];

    private int wanted_Hitup = Config.ServerAdSetting.WANTED_ABILITY[1];

    private int wanted_BowDmg = Config.ServerAdSetting.WANTED_ABILITY[2];

    private int wanted_BowHitUp = Config.ServerAdSetting.WANTED_ABILITY[3];

    private int wanted_Sp = Config.ServerAdSetting.WANTED_ABILITY[4];

    private int wanted_Reduction = Config.ServerAdSetting.WANTED_ABILITY[5];

    private int wanted_Ac = Config.ServerAdSetting.WANTED_ABILITY[6];

    public void Wanted_Level1(boolean isOn, boolean login) {
        int mul = isOn ? 1 : -1;
        addDmgRate(-this.wanted_Dmg * mul);
        addHitup(-this.wanted_Hitup * mul);
        addBowDmgup(-this.wanted_BowDmg * mul);
        addBowHitup(-this.wanted_BowHitUp * mul);
        getAbility().addSp(-this.wanted_Sp * mul);
        addDamageReductionByArmor(-this.wanted_Reduction * mul);
        getAC().addAc(this.wanted_Ac * mul);
        sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
        sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
        sendPackets((ServerBasePacket)new S_OwnCharStatus(this));
        if (isOn) {
            S_CharTitle pck = new S_CharTitle(getId(), "\\fe[);
                    sendPackets((ServerBasePacket)pck, false);
            Broadcaster.broadcastPacket(this, (ServerBasePacket)pck);
        } else {
            S_CharTitle pck = new S_CharTitle(getId(), "");
            sendPackets((ServerBasePacket)pck, false);
            Broadcaster.broadcastPacket(this, (ServerBasePacket)pck);
        }
    }

    public void Wanted_Level2(boolean isOn, boolean login) {
        int Login = 1;
        if (login)
            Login = 2;
        int mul = isOn ? Login : -2;
        addDmgRate(-this.wanted_Dmg * mul);
        addHitup(-this.wanted_Hitup * mul);
        addBowDmgup(-this.wanted_BowDmg * mul);
        addBowHitup(-this.wanted_BowHitUp * mul);
        getAbility().addSp(-this.wanted_Sp * mul);
        addDamageReductionByArmor(-this.wanted_Reduction * mul);
        getAC().addAc(this.wanted_Ac * mul);
        sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
        sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
        sendPackets((ServerBasePacket)new S_OwnCharStatus(this));
        if (isOn) {
            S_CharTitle pck = new S_CharTitle(getId(), "\\fe[);
                    sendPackets((ServerBasePacket)pck, false);
            Broadcaster.broadcastPacket(this, (ServerBasePacket)pck);
        } else {
            S_CharTitle pck = new S_CharTitle(getId(), "");
            sendPackets((ServerBasePacket)pck, false);
            Broadcaster.broadcastPacket(this, (ServerBasePacket)pck);
        }
    }

    public void Wanted_Level3(boolean isOn, boolean login) {
        int Login = 1;
        if (login)
            Login = 3;
        int mul = isOn ? Login : -3;
        addDmgRate(-this.wanted_Dmg * mul);
        addHitup(-this.wanted_Hitup * mul);
        addBowDmgup(-this.wanted_BowDmg * mul);
        addBowHitup(-this.wanted_BowHitUp * mul);
        getAbility().addSp(-this.wanted_Sp * mul);
        addDamageReductionByArmor(-this.wanted_Reduction * mul);
        getAC().addAc(this.wanted_Ac * mul);
        sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
        sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
        sendPackets((ServerBasePacket)new S_OwnCharStatus(this));
        if (isOn) {
            S_CharTitle pck = new S_CharTitle(getId(), "\\fe[);
                    sendPackets((ServerBasePacket)pck, false);
            Broadcaster.broadcastPacket(this, (ServerBasePacket)pck);
        } else {
            S_CharTitle pck = new S_CharTitle(getId(), "");
            sendPackets((ServerBasePacket)pck, false);
            Broadcaster.broadcastPacket(this, (ServerBasePacket)pck);
        }
    }

    private int _AddDamage = 0;

    private int _AddDamageRate = 0;

    private int _AddReduction = 0;

    private int _AddReductionRate = 0;

    private int _ubscore;

    public int getAddDamage() {
        return this._AddDamage;
    }

    public void setAddDamage(int addDamage) {
        this._AddDamage = addDamage;
    }

    public int getAddDamageRate() {
        return this._AddDamageRate;
    }

    public void setAddDamageRate(int addDamageRate) {
        this._AddDamageRate = addDamageRate;
    }

    public int getAddReduction() {
        return this._AddReduction;
    }

    public void setAddReduction(int addReduction) {
        this._AddReduction = addReduction;
    }

    public int getAddReductionRate() {
        return this._AddReductionRate;
    }

    public void setAddReductionRate(int addReductionRate) {
        this._AddReductionRate = addReductionRate;
    }

    public int getUbScore() {
        return this._ubscore;
    }

    public void setUbScore(int i) {
        this._ubscore = i;
    }

    public byte[]  = new byte[512];

    public void (int lv) {
        long needExp = ExpTable.getNeedExpNextLevel(lv);
        long addexp = 0L;
        addexp = (long)(needExp * 0.01D);
        if (addexp != 0L) {
            int level = ExpTable.getLevelByExp(get_exp() + addexp);
            if (level > 60) {
                sendPackets((ServerBasePacket)new S_SystemMessage("));
            } else {
                add_exp(addexp);
            }
        }
    }

    public void (int id) {
        int count = fairlycount(getId());
        this.[id] = 1;
        if (count == 0) {
            fairlystore(getId(), this.);
        } else {
            fairlupdate(getId(), this.);
        }
    }

    public void (L1PcInstance pc, int level, double rate) {
        long needExp = ExpTable.getNeedExpNextLevel(level);
        double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
        long exp = 0L;
        exp = (long)(needExp * rate * exppenalty);
        pc.add_exp(exp);
        Broadcaster.broadcastPacket(pc, (ServerBasePacket)new S_SkillSound(pc.getId(), 3944));
        pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 3944));
    }

    public int fairlycount(int objectId) {
        int result = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT count(*) as cnt FROM character_Fairly_Config WHERE object_id=?");
            pstm.setInt(1, objectId);
            rs = pstm.executeQuery();
            if (rs.next())
                result = rs.getInt("cnt");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return result;
    }

    public void fairlystore(int objectId, byte[] data) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO character_Fairly_Config SET object_id=?, data=?");
            pstm.setInt(1, objectId);
            pstm.setBytes(2, data);
            pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void fairlupdate(int objectId, byte[] data) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("UPDATE character_Fairly_Config SET data=? WHERE object_id=?");
            pstm.setBytes(1, data);
            pstm.setInt(2, objectId);
            pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public L1ItemInstance _fishingRod = null;

    private static Random _random = new Random(System.nanoTime());

    private L1ClassFeature _classFeature = null;

    private L1EquipmentSlot _equipSlot;

    private String _accountName;

    private int _classId;

    private int _type;

    private int _age;

    private int _exp;

    private short _accessLevel;

    private short _baseMaxHp = 0;

    private int _baseMaxMp = 0;

    private int _baseAc = 0;

    private int _originalMagicHit = 0;

    private int _baseBowDmgup = 0;

    private int _baseDmgup = 0;

    private int _baseHitup = 0;

    private int _baseBowHitup = 0;

    private int _baseDecreaseCoolTime = 0;

    private int _baseDecreaseCCDuration = 0;

    private int _baseMagicHitup = 0;

    private int _baseMagicCritical = 0;

    private int _baseMagicDmg = 0;

    private int _baseMagicDecreaseMp = 0;

    private int _DmgupByArmor = 0;

    private int _bowDmgupByArmor = 0;

    private int _PKcount;

    public int _fishingX = 0;

    public int _fishingY = 0;

    private int _clanid;

    private int _redKnightClanId;

    private int _redKnightDamage;

    private int _redKnightKill;

    private String clanname;

    private int _clanRank;

    private byte _sex;

    private long _returnstat;

    private short _hpr = 0;

    private short _trueHpr = 0;

    private short _hpar = 0;

    private short _mpr = 0;

    private short _mpar = 0;

    private short _trueMpr = 0;

    private short _mpar16 = 0;

    private int _advenHp;

    private int _advenMp;

    private int _magicBuffHp;

    private int _highLevel;

    public boolean isInFantasy = false;

    public boolean isInValakasBoss = false;

    public boolean isInValakas = false;

    private boolean _ghost = false;

    private boolean _ghostCanTalk = true;

    private boolean _isReserveGhost = false;

    private boolean _isShowTradeChat = true;

    private boolean _isCanWhisper = true;

    private boolean _isFishing = false;

    private boolean _isFishingReady = false;

    private boolean isDeathMatch = false;

    private boolean _isSupporting = false;

    private boolean _isShowWorldChat = true;

    private boolean _gm;

    private boolean _monitor;

    private boolean _gmInvis;

    private boolean _isDrink = false;

    private boolean _isGres = false;

    private boolean _isPinkName = false;

    private boolean _banned;

    private boolean _gresValid;

    private boolean _tradeOk;

    private boolean _AHRegenActive;

    private boolean _SHRegenActive;

    private boolean _HalloweenRegenActive;

    public boolean RootMent = true;

    public boolean noPlayerck2 = false;

    public boolean noPlayerCK = false;

    public boolean noPlayerRobot = false;

    private int invisDelayCounter = 0;

    private Object _invisTimerMonitor = new Object();

    private int _ghostSaveLocX = 0;

    private int _ghostSaveLocY = 0;

    private short _ghostSaveMapId = 0;

    public byte _ghostCount = 0;

    public long ghosttime = 0L;

    private ScheduledFuture<?> _ghostFuture;

    private ScheduledFuture<?> _hellFuture;

    private ScheduledFuture<?> _autoUpdateFuture;

    private ScheduledFuture<?> _expMonitorFuture;

    private Timestamp _lastPk;

    private Timestamp _deleteTime;

    private Timestamp _lastLoginTime;

    private int _weightReduction = 0;

    private int _hasteItemEquipped = 0;

    private int _dragonpearItemEquipped = 0;

    private int _damageReductionByArmor = 0;

    private int _damageReductionIgnore = 0;

    private int _DamageReduction = 0;

    private int _DmgRate = 0;

    private int _HitRate = 0;

    private int _bowHitRate = 0;

    private int _bowDmgRate = 0;

    private int _sp = 0;

    private int _tempCharGfxAtDead;

    private int _fightId;

    private byte _chatCount = 0;

    private long _oldChatTimeInMillis = 0L;

    private int _elixirStats;

    private int _elfAttr;

    private long _expRes;

    private int _onlineStatus;

    private int _homeTownId;

    private int _contribution;

    private int _food;

    private int _hellTime;

    private int _partnerId;

    private long _fishingTime = 0L;

    private int _dessertId = 0;

    private int _callClanId;

    private int _callClanHeading;

    private int _currentWeapon;

    private final L1Karma _karma = new L1Karma();

    private final L1PcInventory _inventory;

    private final L1DwarfForPackageInventory _dwarfForPackage;

    private final L1Inventory _tradewindow;

    private L1ItemInstance _weapon;

    private L1ItemInstance _secondweapon;

    private L1ItemInstance _armor;

    private L1ItemInstance _armory;

    private L1Party _party;

    private L1ChatParty _chatParty;

    private int _cookingId = 0;

    private int _partyID;

    private int _partyType;

    private int _tradeID;

    private int _tempID;

    private L1Quest _quest;

    private HpRegeneration _hpRegen;

    private MpRegeneration _mpRegen;

    private HpARegeneration _hpArRegen;

    private MpARegeneration _mpArRegen;

    private boolean _hpARegeneration;

    private boolean _mpARegeneration;

    private AHRegeneration _AHRegen;

    private SHRegeneration _SHRegen;

    private HalloweenRegeneration _HalloweenRegen;

    private boolean _isPrivateShop = false;

    private boolean _isPrivateReady = false;

    private int _partnersPrivateShopItemCount = 0;

    private long _lastPasswordChangeTime;

    private long _lastLocalTellTime;

    private boolean  = false;

    boolean isExpDrop;

    boolean isItemDrop;

    public final ArrayList<L1BookMark> _speedbookmarks;

    private int _markcount;

    public L1BookMark[] getBookMarkArray() {
        return this._bookmarks.<L1BookMark>toArray(new L1BookMark[this._bookmarks.size()]);
    }

    public L1BookMark[] getSpeedBookMarkArray() {
        return this._speedbookmarks.<L1BookMark>toArray(new L1BookMark[this._speedbookmarks.size()]);
    }

    public void setMark_count(int i) {
        this._markcount = i;
    }

    public int getMark_count() {
        return this._markcount;
    }

    public boolean is() {
        return this.;
    }

    public void set(boolean c) {
        this. = c;
    }

    private boolean _clanbuff = false;

    public boolean isClanBuff() {
        return this._clanbuff;
    }

    public void setClanBuff(boolean c) {
        this._clanbuff = c;
    }

    public int _getLive = 0;

    public int getLive() {
        return this._getLive;
    }

    public void addLive(int Live) {
        this._getLive += Live;
    }

    public void setLive(int Live) {
        this._getLive = Live;
    }

    public int[] FireGolem = new int[18];

    public int[] FireEnchant = new int[18];

    public int[] PiersItemId = new int[19];

    public int[] PiersEnchant = new int[19];

    private boolean _ = false;

    private boolean _isHomnam = false;

    private boolean _isBosMon = false;

    private boolean _isNCoinMon = false;

    private boolean _isSpecialMap = false;

    private boolean _magicitem = false;

    private int _magicitemid;

    public boolean isWorld = false;

    public boolean isDanteasBuff = false;

    public boolean isGotobokBuff = false;

    public boolean  = false;

    public boolean  = false;

    public int _getCount;

    private long _npcaction;

    public final ArrayList<L1BookMark> _bookmarks;

    private byte[] _shopChat;

    private AtomicInteger _pinkNameTime;

    private GameClient _netConnection;

    private static Logger _log = Logger.getLogger(L1PcInstance.class.getName());

    private final SkillData skill_data;

    private long _lastShellUseTime;

    private static L1Aura _aura;

    private PapuBlessing _PapuRegen;

    private boolean _PapuBlessingActive;

    private final Object updateSync;

    public int CubeMr;

    private Ability pc;

    public boolean _destroyed;

    private static Timer _regenTimer;

    private static Timer _blessingTimer;

    private int _special_size;

    private int birthday;

    private int _TelType;

    private int AinState;

    public int[] DragonPortalLoc;

    public static final int STAT_INCREASE_LEVEL = 50;

    public static final int STAT_BASE_AMOUNT = 75;

    private boolean _morning;

    private long SurvivalGauge;

    private int _maxweight;

    public boolean _fourgear;

    private int _pearl;

    private Object m_add_exp_sync;

    private static final long INTERVAL_EXP_MONITOR = 500L;

    private static final int BONUS_LEVEL_80 = 1;

    private static final int BONUS_LEVEL_82 = 2;

    private static final int BONUS_LEVEL_84 = 4;

    private static final int BONUS_LEVEL_86 = 8;

    private static final int BONUS_LEVEL_88 = 16;

    private static final int BONUS_LEVEL_90 = 32;

    private Timestamp _lastLogoutTime;

    private Timestamp _tamTime;

    private int _tamreserve;

    private boolean returnStatus;

    private boolean returnStatus_Start;

    private boolean returnStatus_Levelup;

    private String _sealingPW;

    public String TempQuiz;

    int _sealScrollTime;

    int _sealScrollCount;

    public int pvp_defense;

    private int CashStep;

    private int teleportTime;

    private int teleportTime2;

    private int skillTime;

    private int skillTime2;

    private long _quiztime;

    private long _quiztime2;

    private long _quiztime3;

    private int currentSkillCount;

    private int currentSkillCount2;

    private int comboCount;

    private int m_bapo_level;

    private int _deathmatch;

    private int _petrace;

    private int _ultimatebattle;

    private int _petmatch;

    private int _ghosthouse;

    private int _enchantitemid;

    public int _accessoryHeal;

    private boolean _isGambling;

    private int _gamblingmoney;

    private boolean _isGambling3;

    private int _gamblingmoney3;

    private ArrayList<String> _cmalist;

    private ArrayList<Integer> skillList;

    private int _clanMemberId;

    private String _clanMemberNotes;

    EinhasadTimer _einhasadTimer;

    private int risingUp;

    private int impactUp;

    private int graceLv;

    private int _moveSkillX;

    private int _moveSkillY;

    private short _moveSkillMapId;

    private byte _moveSkillHeading;

    private boolean teleport;

    private int teleport_x;

    private int teleport_y;

    private int teleport_map;

    private int teleport_count;

    private int _attackDelay;

    private int _attackSpeed;

    private int _polyIdByEquip;

    private L1HateList _autoTargetList;

    private boolean _HUNTER_BLESS3;

    private boolean _isAutoSetting;

    private int _autoPolyId;

    private int _autoLocX;

    private int _autoLocY;

    private L1Character _autoTarget;

    private L1Astar _autoAStar;

    private int[][] _autoPath;

    private int _autoMoveCount;

    private long _autoSkillDelay;

    private int _autoStatus;

    private L1Node _autoTail;

    private boolean _autoPathFirst;

    public int _autoCurrentPath;

    private int _autoPotion;

    private long _autoTimeAttack;

    private long _autoTimeMove;

    private boolean _autoDead;

    private int _autoDeadTime;

    private int _autoTeleportTime;

    private int _autoRange;

    private long _autoAiTime;

    private int _autoMapId;

    private List<Integer> _slotItemOne;

    private List<Integer> _slotItemTwo;

    private List<Integer> _slotItemThree;

    private List<Integer> _slotItemFour;

    private HashMap<Integer, L1InventorySlot> _slotcolor;

    private int slotNumber;

    private long _buffTime;

    private boolean _isOneTel;

    private int _robotAIType;

    private int _robotPattern;

    private long _robotStopTime;

    private long _robotPotionTime;

    private int attackRange;

    public boolean isCastle;

    private boolean _isPolyRingMaster;

    private boolean _isPolyRingMaster2;

    private int _elfAttrInitCount;

    private int _lastImmuneLevel;

    private static final long _valaRegenTime = 16000L;

    private boolean _isValakasBlessing;

    private ValakasBlessing _vBlessing;

    private boolean _isBossNotify;

    private MJInstanceEnums.InstStatus _instStatus;

    private int _dmgLfc;

    private int _findMerchantId;

    private boolean _isValakasProduct;

    private MJExpAmplifier _expAmplifier;

    private ProtoOutputStream _wrdPck;

    private L1ItemInstance _kdInitItem;

    private boolean _isOutsideChat;

    private long _lastNpcClickMs;

    private int _charLevelBonus;

    private L1ItemInstance _NameInstance;

    private int _magicDodgeProb;

    private int _temporaryItemObjectId;

    public long get_lastPasswordChangeTime() {
        return this._lastPasswordChangeTime;
    }

    public long get_lastLocalTellTime() {
        return this._lastLocalTellTime;
    }

    public void update_lastPasswordChangeTime() {
        this._lastPasswordChangeTime = System.currentTimeMillis();
    }

    public void update_lastLocalTellTime() {
        this._lastLocalTellTime = System.currentTimeMillis();
    }

    public long getlastShellUseTime() {
        return this._lastShellUseTime;
    }

    public void updatelastShellUseTime() {
        this._lastShellUseTime = System.currentTimeMillis();
    }

    public int getPinkNameTime() {
        return this._pinkNameTime.get();
    }

    public int DecrementPinkNameTime() {
        return this._pinkNameTime.decrementAndGet();
    }

    public int SetPinkNameTime(int timeValue) {
        return this._pinkNameTime.getAndSet(timeValue);
    }

    public void setSkillMastery(int skillid) {
        if (!this.skillList.contains(Integer.valueOf(skillid)))
            this.skillList.add(Integer.valueOf(skillid));
    }

    private class L1Aura implements Runnable {
        private String _name;

        private boolean _auraStopped = false;

        private L1PcInstance[] _auraMembersList;

        public L1Aura(String name) {
            this._name = name;
        }

        public void run() {
            while (!this._auraStopped) {
                try {
                    L1PcInstance pc = L1World.getInstance().getPlayer(this._name);
                    if (pc == null) {
                        auraStop();
                        return;
                    }
                    pc.auraBuff(true);
                    L1Party party = pc.getParty();
                    if (party == null) {
                        Thread.sleep(1000L);
                        continue;
                    }
                    if (this._auraMembersList != null)
                        this._auraMembersList = null;
                    this._auraMembersList = party.getMembers();
                    if (this._auraMembersList == null || this._auraMembersList.length <= 0) {
                        Thread.sleep(1000L);
                        continue;
                    }
                    for (L1PcInstance member : this._auraMembersList) {
                        if (member != null && pc.getId() != member.getId())
                            if (member.isDead()) {
                                member.auraBuff(false);
                            } else if (pc.getMapId() == member.getMapId() && pc
                                    .getLocation().isInScreen((Point)member
                                            .getLocation())) {
                                if (!member.hasSkillEffect(340))
                                    member.auraBuff(true);
                            } else if (member.hasSkillEffect(340)) {
                                member.auraBuff(false);
                            }
                    }
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                    auraStop();
                }
            }
        }

        public void auraStop() {
            this._auraStopped = true;
            if (this._auraMembersList != null)
                for (L1PcInstance member : this._auraMembersList)
                    member.auraBuff(false);
        }
    }

    public void auraBuff(boolean onOff) {
        try {
            if (onOff) {
                if (hasSkillEffect(340))
                    return;
                setSkillEffect(340, -1L);
                sendPackets((ServerBasePacket)new S_PacketBox(180, 1, 479));
            } else {
                if (!hasSkillEffect(340))
                    return;
                killSkillEffectTimer(340);
                sendPackets((ServerBasePacket)new S_PacketBox(180, 0, 479));
            }
            getResistance().addMr(onOff ? 10 : -10);
            addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, onOff ? 2 : -2);
            getAbility().addAddedInt(onOff ? 1 : -1);
            getAbility().addAddedDex(onOff ? 1 : -1);
            getAbility().addAddedStr(onOff ? 1 : -1);
            resetBaseMr();
            sendPackets((ServerBasePacket)new S_SPMR(this));
            sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
            SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeSkillMastery(int skillid) {
        if (this.skillList.contains(Integer.valueOf(skillid)))
            this.skillList.remove(Integer.valueOf(skillid));
    }

    public boolean isSkillMastery(int skillid) {
        return this.skillList.contains(Integer.valueOf(skillid));
    }

    public int numOfSpellMastery() {
        return this.skillList.size();
    }

    public short getHpr() {
        return this._hpr;
    }

    public void addHpr(int i) {
        this._trueHpr = (short)(this._trueHpr + i);
        this._hpr = (short)Math.max(0, this._trueHpr);
    }

    public short getHpAr() {
        return this._hpar;
    }

    public void addHpAr(int i) {
        this._hpar = (short)(this._hpar + i);
    }

    public short getMpAr() {
        return this._mpar;
    }

    public short getMpAr16() {
        return this._mpar16;
    }

    public void addMpAr16(int i) {
        this._mpar16 = (short)(this._mpar16 + i);
    }

    public void addMpAr(int i) {
        this._mpar = (short)(this._mpar + i);
    }

    public short getMpr() {
        return this._mpr;
    }

    public void addMpr(int i) {
        this._trueMpr = (short)(this._trueMpr + i);
        this._mpr = (short)Math.max(0, this._trueMpr);
    }

    public void setHomnam(boolean flag) {
        this._isHomnam = flag;
    }

    public boolean isHomnam() {
        return this._isHomnam;
    }

    public void setBosMon(boolean flag) {
        this._isBosMon = flag;
    }

    public boolean isBosMon() {
        return this._isBosMon;
    }

    public boolean isSpecialMap() {
        return this._isSpecialMap;
    }

    public void setSpecialMap(boolean flag) {
        this._isSpecialMap = flag;
    }

    public boolean isNCoinMon() {
        return this._isNCoinMon;
    }

    public void setNCoinMon(boolean flag) {
        this._isNCoinMon = flag;
    }

    public long getNpcActionTime() {
        return this._npcaction;
    }

    public void setNpcActionTime(long flag) {
        this._npcaction = flag;
    }

    public boolean isMagicItem() {
        return this._magicitem;
    }

    public void setMagicItem(boolean flag) {
        this._magicitem = flag;
    }

    public int getMagicItemId() {
        return this._magicitemid;
    }

    public void setMagicItemId(int itemid) {
        this._magicitemid = itemid;
    }

    public void startPapuBlessing() {
        int RegenTime = 150000;
        if (!this._PapuBlessingActive) {
            this._PapuRegen = new PapuBlessing(this);
            this._PapuBlessingActive = true;
            GeneralThreadPool.getInstance().schedule((Runnable)this._PapuRegen, 150000L);
        }
    }

    public void startAHRegeneration() {
        int INTERVAL = 600000;
        if (!this._AHRegenActive) {
            this._AHRegen = new AHRegeneration(this, 600000L);
            GeneralThreadPool.getInstance().schedule((Runnable)this._AHRegen, 600000L);
            this._AHRegenActive = true;
        }
    }

    public void startSHRegeneration() {
        int INTERVAL = 1800000;
        if (!this._SHRegenActive) {
            this._SHRegen = new SHRegeneration(this, 1800000L);
            GeneralThreadPool.getInstance().schedule((Runnable)this._SHRegen, 1800000L);
            this._SHRegenActive = true;
        }
    }

    public void startHalloweenRegeneration() {
        int INTERVAL = 900000;
        if (!this._HalloweenRegenActive) {
            this._HalloweenRegen = new HalloweenRegeneration(this, 900000L);
            GeneralThreadPool.getInstance().schedule((Runnable)this._HalloweenRegen, 900000L);
            this._HalloweenRegenActive = true;
        }
    }

    public void stopPapuBlessing() {
        if (this._PapuBlessingActive) {
            this._PapuRegen.cancel();
            this._PapuRegen = null;
            this._PapuBlessingActive = false;
        }
    }

    public void stopAHRegeneration() {
        if (this._AHRegenActive) {
            this._AHRegen.cancel();
            this._AHRegen = null;
            this._AHRegenActive = false;
        }
    }

    public void stopSHRegeneration() {
        if (this._SHRegenActive) {
            this._SHRegen.cancel();
            this._SHRegen = null;
            this._SHRegenActive = false;
        }
    }

    public void stopHalloweenRegeneration() {
        if (this._HalloweenRegenActive) {
            this._HalloweenRegen.cancel();
            this._HalloweenRegen = null;
            this._HalloweenRegenActive = false;
        }
    }

    public void startObjectAutoUpdate() {
        long INTERVAL_AUTO_UPDATE = 300L;
        removeAllKnownObjects();
        this._autoUpdateFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)new L1PcAutoUpdate(getId()), 0L, 300L);
    }

    public void stopEtcMonitor() {
        if (this._autoUpdateFuture != null) {
            this._autoUpdateFuture.cancel(true);
            this._autoUpdateFuture = null;
        }
        if (this._expMonitorFuture != null) {
            this._expMonitorFuture.cancel(true);
            this._expMonitorFuture = null;
            if (attribute().has(expMonitorKey))
                attribute().get(expMonitorKey).set(null);
        }
        if (this._ghostFuture != null) {
            this._ghostFuture.cancel(true);
            this._ghostFuture = null;
        }
        if (this._hellFuture != null) {
            this._hellFuture.cancel(true);
            this._hellFuture = null;
        }
    }

    public void stopEquipmentTimer() {
        List<L1ItemInstance> allItems = getInventory().getItems();
        for (L1ItemInstance item : allItems) {
            if (item == null)
                continue;
            if (item.isEquipped() && item.getRemainingTime() > 0)
                item.stopEquipmentTimer(this);
        }
    }

    public void onChangeExp() {
        int level = ExpTable.getLevelByExp(get_exp());
        int char_level = getLevel();
        int gap = level - char_level;
        if (gap == 0) {
            sendPackets((ServerBasePacket)new S_OwnCharStatus(this));
            int percent = ExpTable.getExpPercentage(char_level, get_exp());
            if (char_level >= 60 && char_level <= 64) {
                if (percent >= 10)
                    removeSkillEffect(80007);
            } else if (char_level >= 65 &&
                    percent >= 5) {
                removeSkillEffect(80007);
            }
            return;
        }
        if (gap != 0)
            MJDeathPenaltyexpDatabaseLoader.getInstance().do_Select(this);
        int old_level = getLevel();
        if (gap > 0) {
            levelUp(gap);
            if (getLevel() >= 60) {
                setSkillEffect(80007, 10800000L);
                sendPackets((ServerBasePacket)new S_PacketBox(10800, true, true), true);
            }
            RenewStat();
        } else if (gap < 0) {
            levelDown(gap);
            RenewStat();
            removeSkillEffect(80007);
        }
        MJAutoMapInfo mInfo = MJAutoMapInfo.get_map_info(getMapId());
        if (mInfo != null &&
                mInfo.type_check(this, get_client_auto_type()))
            do_finish_client_auto(SC_FORCE_FINISH_PLAY_SUPPORT_NOTI.eReason.INVALID_MAP);
        special_resistance_skill(this, old_level, getLevel(), false);
    }

    public void onPerceive(L1PcInstance pc) {
        if (isGmInvis())
            return;
        try {
            pc.addKnownObject((L1Object)this);
            if (getAI() != null && pc.getAI() != null)
                return;
            if (getWorldObject() != null) {
                pc.sendPackets(getWorldObject(), false);
            } else if (pc.is_combat_field()) {
                if (isGm() || pc.isGm()) {
                    pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
                } else if (get_current_combat_team_id() == pc
                        .get_current_combat_team_id()) {
                    pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, "));
                } else {
                    pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, "));
                }
            } else if (pc.getMapId() == 621) {
                if (isGm() || pc.isGm()) {
                    pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
                } else {
                    pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, "ID));
                }
            } else if (pc.getMapId() == 13006 || pc.getMapId() == 13005) {
                if (pc.getMapId() == 13005) {
                    pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, pc.getClassName()));
                } else if (pc.getMapId() == 13006) {
                    for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamRedList()) {
                        if (player == this)
                            pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, "Red, -32767));
                    }
                    for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamBlueList()) {
                        if (player == this)
                            pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, "Blue, 32767));
                    }
                }
            } else {
                pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
            }
            if (isPinkName())
                pc.sendPackets((ServerBasePacket)new S_PinkName(pc.getId(), pc.getPinkNameTime()));
            for (L1PcInstance target : L1World.getInstance().getVisiblePlayer((L1Object)pc)) {
                if (target.isPinkName())
                    pc.sendPackets((ServerBasePacket)new S_PinkName(target.getId(), target
                            .getPinkNameTime()));
            }
            if (isPrivateShop())
                pc.sendPackets((ServerBasePacket)new S_DoActionShop(getId(), 70,
                        getShopChat()));
            if (isFishing())
                pc.sendPackets((ServerBasePacket)new S_Fishing(getId(), 71, this._fishingX, this._fishingY));
            L1Clan clan = L1World.getInstance().getClan(getClanid());
            if (clan != null)
                if (getMapId() != 13005 && getMapId() != 13006)
                    if (clan.getCastleId() != 0 &&
                            isCrown() &&
                            getId() == clan.getLeaderId())
                        pc.sendPackets((ServerBasePacket)new S_CastleMaster(clan.getCastleId(), getId()));
            if (hasSkillEffect(113) && (
                    getTrueTargetClan() == pc.getClanid() ||
                            getTrueTargetParty() == pc.getPartyID()))
                pc.sendPackets((ServerBasePacket)new S_TrueTargetNew(getId(), true));
        } finally {
            L1Party party = getParty();
            if (party != null && party.isMember(pc))
                party.handshakePartyMemberStatus(this, pc);
        }
    }

    public void broadcastRemoveAllKnownObjects() {
        for (L1Object known : getKnownObjects()) {
            if (known == null)
                continue;
            sendPackets((ServerBasePacket)new S_RemoveObject(known));
        }
    }

    public void updateObject() {
        GeneralThreadPool.getInstance().execute(new Runnable() {
            public void run() {
                L1PcInstance.this.updateObject0();
            }
        });
    }

    public L1PcInstance() {
        this.updateSync = new Object();
        this._destroyed = false;
        this._TelType = 0;
        this.AinState = 0;
        this.DragonPortalLoc = new int[3];
        this._morning = false;
        this._maxweight = 0;
        this.m_add_exp_sync = new Object();
        this.returnStatus = false;
        this.returnStatus_Start = false;
        this.returnStatus_Levelup = false;
        this.TempQuiz = "";
        this.CashStep = 0;
        this.teleportTime = 0;
        this.teleportTime2 = 0;
        this.skillTime = 0;
        this.skillTime2 = 0;
        this._quiztime = 0L;
        this._quiztime2 = 0L;
        this._quiztime3 = 0L;
        this.currentSkillCount = 0;
        this.currentSkillCount2 = 0;
        this.m_bapo_level = 6;
        this._enchantitemid = 0;
        this._accessoryHeal = 0;
        this._isGambling = false;
        this._gamblingmoney = 0;
        this._isGambling3 = false;
        this._gamblingmoney3 = 0;
        this._cmalist = new ArrayList<>();
        this.skillList = new ArrayList<>();
        this.risingUp = 0;
        this.impactUp = 0;
        this.graceLv = 0;
        this._polyIdByEquip = 0;
        this._autoTargetList = new L1HateList();
        this._autoAStar = new L1Astar();
        this._autoPath = new int[300][2];
        this._autoMoveCount = CommonUtil.random(50, 200);
        this._autoDeadTime = 5;
        this._slotItemOne = new ArrayList<>();
        this._slotItemTwo = new ArrayList<>();
        this._slotItemThree = new ArrayList<>();
        this._slotItemFour = new ArrayList<>();
        this._slotcolor = new HashMap<>();
        this.slotNumber = 0;
        this._isOneTel = false;
        this._robotPattern = -1;
        this._isPolyRingMaster = false;
        this._isPolyRingMaster2 = false;
        this._lastImmuneLevel = 0;
        this._isValakasBlessing = false;
        this._isBossNotify = true;
        this._instStatus = MJInstanceEnums.InstStatus.INST_USERSTATUS_NONE;
        this._findMerchantId = 0;
        this._isOutsideChat = true;
        this._lastNpcClickMs = 0L;
        this._magicDodgeProb = 0;
        this._temporaryItemObjectId = -1;
        this.lastSpellUseMillis = 0L;
        this.lastSpellUsePending = 0;
        this.lastSpeedUsePending = 0;
        this._isOnTargetEffect = Config.ServerAdSetting.TARGETGFX;
        this._instance_status = MJEPcStatus.NONE;
        this._current_combat_id = 0;
        this._current_combat_team_id = -1;
        this._is_non_action = false;
        this._mark_status = 0;
        this
                ._dtInfo = DungeonTimeUserInformation.newInstance();
        this.lastSavedTime = System.currentTimeMillis();
        this.lastSavedTime_inventory = System.currentTimeMillis();
        this.lastSavedTime_exp = System.currentTimeMillis();
        this.lastSavedTime_Fatigue = System.currentTimeMillis();
        this.isAutoTreeple = false;
        this._ClanBuffMap = 0;
        this._EinhasadBlessper = 0;
        this.m_is_ready_server_shift = false;
        this.m_item_exp_bonus = 0.0D;
        this.m_is_using_items = false;
        this.m_is_client_auto = false;
        this.m_private_porbability = null;
        this.attribute = MJAttrMap.newConcurrentHash();
        this
                .eventHandler = MJObjectEventProvider.provider().newHandler();
        this.InstanceDungeon = false;
        this.indun_room_num = -1;
        this._bot_wait = 0;
        this._bot_wait_check = 0;
        this._bot_warehouse = false;
        this._bot_shop = false;
        this._bot_buff = false;
        this._bot_teleport = false;
        this._bot_success = true;
        this._Prime_war_zone = false;
        this._Bonus_drop_npc = 0;
        this._Destroy_pier = false;
        this._Destroy_horror = false;
        this._Erzabe_circle = false;
        this._Insert_Tel_xym = new int[3];
        this._macro_list = new ArrayList<>();
        this._move_speed_over_loc = new int[3];
        this.doll_update_option = new int[2];
        this.customQuestList = new HashMap<>();
        this._spear_mode_type = false;
        this._vanguard_type = false;
        this._attack_delay_checker = 0.0D;
        this._attack_delay_count = 0;
        this._divine_protection = 0;
        this._empire_overlord = false;
        this._titan_berserk = false;
        this._titan_beast = false;
        this._TitanBeastChaList = new ArrayList<>();
        this._emblem = 0;
        this._dragonbless = false;
        this._inven_bonus_items = new ArrayList<>();
        this._temp_skill_active = new ArrayList<>();
        this._temp_skill_passive = new ArrayList<>();
        this._pc_golden_status = false;
        this._pc_golden_buff = new ArrayList<>();
        this._ainhasad_faith = new HashMap<>();
        this._potionRecoveryRate = 0;
        this._AurakiaCircle = false;
        this._threeItemEquipped = 0;
        this._CastleEffect = false;
        this._isMassTel = false;
        this._accessLevel = 0;
        this._currentWeapon = 0;
        this._inventory = new L1PcInventory(this);
        this._dwarfForPackage = new L1DwarfForPackageInventory(this);
        this._tradewindow = new L1Inventory();
        this._bookmarks = new ArrayList<>();
        this._speedbookmarks = new ArrayList<>();
        this._quest = new L1Quest(this);
        this._equipSlot = new L1EquipmentSlot(this);
        this._pinkNameTime = new AtomicInteger(0);
        this.skill_data = new SkillData(this);
    }

    public void updateObject0() {
        synchronized (this.updateSync) {
            removeOutOfRangeObjects();
            ArrayList<L1Object> _Vlist = null;
            _Vlist = L1World.getInstance().getVisibleObjects((L1Object)this, Config.Connection.PcRecognizeRange);
            for (L1Object visible : _Vlist) {
                if (visible == null)
                    continue;
                try {
                    if (!knownsObject(visible)) {
                        visible.onPerceive(this);
                        if (hasSkillEffect(2001) && L1HpBar.isHpBarTarget(visible))
                            sendPackets((ServerBasePacket)new S_HPMeter((L1Character)visible));
                    } else if (visible instanceof L1NpcInstance) {
                        L1NpcInstance npc = (L1NpcInstance)visible;
                        if (getLocation().isInScreen((Point)npc.getLocation()) && npc.getHiddenStatus() != 0)
                            npc.approachPlayer(this);
                    }
                    if (visible instanceof L1PcInstance) {
                        L1PcInstance pc = (L1PcInstance)visible;
                        if (pc.hasSkillEffect(113) && (pc.getTrueTargetClan() == getClanid() || pc.getTrueTargetParty() == getPartyID()))
                            sendPackets((ServerBasePacket)new S_TrueTargetNew(pc.getId(), true));
                    } else if (visible instanceof L1NpcInstance) {
                        L1NpcInstance npc = (L1NpcInstance)visible;
                        if (npc.hasSkillEffect(113) && (npc.getTrueTargetClan() == getClanid() || npc.getTrueTargetParty() == getPartyID()))
                            sendPackets((ServerBasePacket)new S_TrueTargetNew(npc.getId(), true));
                        if (npc instanceof L1MonsterInstance) {
                            L1MonsterInstance monster = (L1MonsterInstance)npc;
                            monster.onNpcAI();
                        }
                    }
                    if (visible instanceof L1NpcInstance) {
                        L1NpcInstance npc = (L1NpcInstance)visible;
                        if (npc.getNpcId() == 50000220) {
                            SC_INDUN_TOWER_HIT_POINT_RATIO_NOTI.send_tower_hit(this, npc);
                            sendPackets((ServerBasePacket)new S_HPMeter(npc));
                        }
                    }
                    if (hasSkillEffect(2001) && L1HpBar.isHpBarTarget(visible)) {
                        L1Character c = (L1Character)visible;
                        if (c.isChangedHpAndUpdate())
                            sendPackets((ServerBasePacket)new S_HPMeter(c));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void removeOutOfRangeObjects() {
        for (L1Object known : getKnownObjects()) {
            if (known == null)
                continue;
            if (Config.Connection.PcRecognizeRange == -1) {
                if (!getLocation().isInScreen((Point)known.getLocation())) {
                    removeKnownObject(known);
                    sendPackets((ServerBasePacket)new S_RemoveObject(known));
                }
                continue;
            }
            if (getLocation().getTileLineDistance((Point)known.getLocation()) > Config.Connection.PcRecognizeRange) {
                removeKnownObject(known);
                sendPackets((ServerBasePacket)new S_RemoveObject(known));
            }
        }
    }

    private void sendVisualEffect() {
        int poisonId = 0;
        if (getPoison() != null)
            poisonId = getPoison().getEffectId();
        if (getParalysis() != null)
            poisonId = getParalysis().getEffectId();
        if (poisonId != 0) {
            sendPackets((ServerBasePacket)new S_Poison(getId(), poisonId));
            broadcastPacket((ServerBasePacket)new S_Poison(getId(), poisonId));
        }
    }

    public void sendClanMarks() {
        if (getClanid() != 0) {
            L1Clan clan = L1World.getInstance().getClan(getClanid());
            if (clan != null && isCrown() && getId() == clan.getLeaderId() && clan.getCastleId() != 0) {
                sendPackets((ServerBasePacket)new S_CastleMaster(clan.getCastleId(), getId()));
            } else {
                sendPackets((ServerBasePacket)new S_CastleMaster(clan.getCastleId(), 0));
            }
        }
    }

    public void sendVisualEffectAtLogin() {
        sendVisualEffect();
    }

    public void sendVisualEffectAtTeleport() {
        if (isDrink());
        sendVisualEffect();
    }

    public void setCurrentHp(int i) {
        if (getCurrentHp() == i)
            return;
        if (getInstStatus() == MJInstanceEnums.InstStatus.INST_USERSTATUS_LFC && i > getCurrentHp())
            addDamageFromLfc(i - getCurrentHp());
        super.setCurrentHp(i);
        sendPackets((ServerBasePacket)new S_HPUpdate(getCurrentHp(), getMaxHp()));
        if (isInParty())
            getParty().refreshPartyMemberStatus(this);
        if (isPassive(MJPassiveID.BERSERK.toInt())) {
            int percent = (int)Math.round(getCurrentHp() / getMaxHp() * 100.0D);
            if (percent <= 50) {
                if (!isTitanBerserk()) {
                    SC_SPELL_BUFF_NOTI.sendBerserk(this, true);
                    setTitanBerserk(true);
                    addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, 5);
                    getResistance().addPVPweaponTotalDamage(5);
                    getResistance().addcalcPcDefense(8);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this);
                }
            } else if (isTitanBerserk()) {
                SC_SPELL_BUFF_NOTI.sendBerserk(this, false);
                setTitanBerserk(false);
                addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -5);
                getResistance().addPVPweaponTotalDamage(-5);
                getResistance().addcalcPcDefense(-8);
                SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this);
            }
        }
        if (getHpAr() > 0) {
            startHpARegeneration();
        } else {
            stopHpARegeneration();
        }
        if (getMpAr() > 0) {
            startMpARegeneration();
        } else {
            stopMpARegeneration();
        }
    }

    public void setCurrentMp(int i) {
        if (getCurrentMp() == i)
            return;
        if (isGm())
            i = getMaxMp();
        super.setCurrentMp(i);
        sendPackets((ServerBasePacket)new S_MPUpdate(getCurrentMp(), getMaxMp()));
        if (isInParty())
            getParty().refreshPartyMemberStatus(this);
    }

    public L1PcInventory getInventory() {
        return this._inventory;
    }

    public L1DwarfForPackageInventory getDwarfForPackageInventory() {
        return this._dwarfForPackage;
    }

    public L1Inventory getTradeWindowInventory() {
        return this._tradewindow;
    }

    public boolean isGmInvis() {
        return this._gmInvis;
    }

    public void setGmInvis(boolean flag) {
        this._gmInvis = flag;
    }

    public int getCurrentWeapon() {
        return this._currentWeapon;
    }

    public void setCurrentWeapon(int i) {
        this._currentWeapon = i;
    }

    public int getType() {
        return this._type;
    }

    public void setType(int i) {
        this._type = i;
    }

    public short getAccessLevel() {
        return this._accessLevel;
    }

    public void setAccessLevel(short i) {
        this._accessLevel = i;
    }

    public int getClassId() {
        return this._classId;
    }

    public void setClassId(int i) {
        this._classId = i;
        this._classFeature = L1ClassFeature.newClassFeature(i);
    }

    public L1ClassFeature getClassFeature() {
        return this._classFeature;
    }

    public synchronized long getReturnStat() {
        return this._returnstat;
    }

    public synchronized void setReturnStat(long i) {
        this._returnstat = i;
    }

    private L1PcInstance getStat() {
        return null;
    }

    public void reduceCurrentHp(double d, L1Character l1character) {
        getStat().reduceCurrentHp(d, l1character);
    }

    private void notifyPlayersLogout(Collection<L1PcInstance> playersArray) {
        for (L1PcInstance player : playersArray) {
            if (player == null)
                continue;
            if (player.knownsObject((L1Object)this)) {
                player.removeKnownObject((L1Object)this);
                player.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)this));
            }
        }
    }

    private void quitGame() {
        try {
            remove_companion();
            if (!this.noPlayerCK && !this.noPlayerck2 && !isPrivateShop() && this.pc == null) {
                MJUIAdapter.on_logout_user(getNetConnection(), this);
                System.out.println(String.format("[[[[IP:%s]", new Object[] { getAccountName(), getName(), getNetConnection().getHostname() }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (isFishing())
                FishingTimeController.getInstance().endFishing(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getMap().setPassable((Point)getLocation(), true);
            if (isDead()) {
                int[] loc = Getback.GetBack_Location(this, true);
                setX(loc[0]);
                setY(loc[1]);
                setMap((short)loc[2]);
                setCurrentHp(getLevel());
                set_food(39);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getTradeID() != 0) {
                L1Trade trade = new L1Trade();
                trade.TradeCancel(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getFightId() != 0) {
                L1PcInstance fightPc = (L1PcInstance)L1World.getInstance().findObject(getFightId());
                if (fightPc != null) {
                    fightPc.setFightId(0);
                    fightPc.sendPackets((ServerBasePacket)new S_PacketBox(5, 0, 0));
                }
                setFightId(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (isInParty() || getParty() != null)
                getParty().leaveMember(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (isInChatParty())
                getChatParty().leaveMember(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Object[] petList = getPetList().values().toArray();
            L1PetInstance pet = null;
            for (Object petObject : petList) {
                if (petObject instanceof L1PetInstance) {
                    pet = (L1PetInstance)petObject;
                    pet.unloadMaster();
                }
                if (petObject instanceof L1SummonInstance) {
                    L1SummonInstance summon = (L1SummonInstance)petObject;
                    summon.onLeaveMaster();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            L1DollInstance doll = getMagicDoll();
            if (doll != null)
                doll.deleteDoll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Object[] followerList = getFollowerList().values().toArray();
            L1FollowerInstance follower = null;
            for (Object followerObject : followerList) {
                if (followerObject != null) {
                    follower = (L1FollowerInstance)followerObject;
                    follower.setParalyzed(true);
                    follower.spawn(follower.getNpcTemplate().get_npcId(), follower.getX(), follower.getY(), follower.getHeading(), follower.getMapId());
                    follower.deleteMe();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BQSCharacterDataLoader.out(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Config.ServerAdSetting.DelayTimer)
                ItemDelayTimer.SaveItemDelay(this);
            CharBuffTable.DeleteBuff(this);
            CharBuffTable.SaveBuff(this);
            clearSkillEffectTimer();
            SkillCheck.getInstance().QuitDelSkill(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (L1ItemInstance item : getInventory().getItems()) {
                if (item == null)
                    continue;
                if (item.getCount() <= 0)
                    getInventory().deleteItem(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            AinhasadSpecialStatLoader.getInstance().updateSpecialStat(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopEtcMonitor();
        setOnlineStatus(0);
        try {
            save();
            saveInventory();
            L1BookMark.WriteBookmark(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        try {
            synchronized (this) {
                if (this._destroyed)
                    return;
                this._destroyed = true;
                stopMacroTimer();
                for (int i = 0; i <= 80; i++) {
                    sendPackets((ServerBasePacket)new S_LetterList(this, 48, i, true));
                    sendPackets((ServerBasePacket)new S_LetterList(this, 49, i, true));
                    sendPackets((ServerBasePacket)new S_LetterList(this, 50, i, true));
                }
                if (indun_model() != null) {
                    MJIndunRoomModel model = indun_model();
                    model.onClearRoom(this);
                }
                remove_companion();
                if (getAccount() != null) {
                    getAccount().updateBlessOfAin();
                    getAccount().updateLastLogOut();
                }
                DungeonTimeProgressLoader.update(this);
                if (!is()) {
                    SC_ATTENDANCE_USER_DATA_EXTEND userData = getAttendanceData();
                    if (userData != null) {
                        SC_ATTENDANCE_USER_DATA_EXTEND.update(getAccountName(), userData);
                        setAttendanceData((SC_ATTENDANCE_USER_DATA_EXTEND)null);
                    }
                }
                if (is_combat_field()) {
                    MJCombatObserver observer = MJCombatLoadManager.getInstance().get_current_observer(get_current_combat_id());
                    if (observer != null)
                        observer.remove(this);
                }
                MJIndunRoomController.getInstance().end_user_room(this, -1);
                MJRankUserLoader.getInstance().offUser(this);
                MJRaidSpace.getInstance().getBackPc(this);
                MJInstanceSpace.getInstance().getBackPc(this);
                CharacterSlotItemTable.getInstance().updateCharSlotItems(this);
                this._slotItemTwo.clear();
                this._slotItemOne.clear();
                this._slotItemThree.clear();
                this._slotItemFour.clear();
                this._slotcolor.clear();
                quitGame();
                L1World world = L1World.getInstance();
                notifyPlayersLogout(getKnownPlayers());
                world.removeVisibleObject((L1Object)this);
                world.removeObject((L1Object)this);
                notifyPlayersLogout(world.getRecognizePlayer((L1Object)this));
                this._inventory.clearItems();
                this._dwarfForPackage.clearItems();
                removeAllKnownObjects();
                dispose_regenerator();
                stopHalloweenRegeneration();
                stopAHRegeneration();
                stopSHRegeneration();
                stopEquipmentTimer();
                setDead(true);
                setNetConnection((GameClient)null);
                stopEinhasadTimer();
                stopPapuBlessing();
                getAC().addAc(1);
                StoreBQ();
                CharacterSkillDelayTable.getInstace().updatedata(this);
                dispose();
                allTimerDispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this._timeCollection != null) {
                L1TimeCollectionUserLoader.getInstance().merge(this);
                this._timeCollection.dispose();
                this._timeCollection = null;
            }
        } catch (Exception e) {
            System.out.println(String.format("[(%s)", new Object[] { getName() }));
            e.printStackTrace();
        }
        try {
            if (this._favorBook != null) {
                L1FavorBookUserLoader.getInstance().merge(this);
                this._favorBook.dispose();
                this._favorBook = null;
            }
        } catch (Exception e) {
            System.out.println(String.format("[(%s)", new Object[] { getName() }));
            e.printStackTrace();
        }
    }

    private void allTimerDispose() {
        if (_regenTimer != null) {
            _regenTimer.cancel();
            _regenTimer = null;
        }
        if (_blessingTimer != null) {
            _blessingTimer.cancel();
            _blessingTimer = null;
        }
    }

    public GameClient getNetConnection() {
        return this._netConnection;
    }

    public void setNetConnection(GameClient clientthread) {
        this._netConnection = clientthread;
    }

    public boolean isInParty() {
        return (getParty() != null);
    }

    public L1Party getParty() {
        return this._party;
    }

    public void setParty(L1Party p) {
        this._party = p;
    }

    public boolean isInChatParty() {
        return (getChatParty() != null);
    }

    public L1ChatParty getChatParty() {
        return this._chatParty;
    }

    public void setChatParty(L1ChatParty cp) {
        this._chatParty = cp;
    }

    public int getPartyID() {
        return this._partyID;
    }

    public void setPartyID(int partyID) {
        this._partyID = partyID;
    }

    public int getPartyType() {
        return this._partyType;
    }

    public void setPartyType(int partyType) {
        this._partyType = partyType;
    }

    public int getTradeID() {
        return this._tradeID;
    }

    public void setTradeID(int tradeID) {
        this._tradeID = tradeID;
    }

    public void setTradeOk(boolean tradeOk) {
        this._tradeOk = tradeOk;
    }

    public boolean getTradeOk() {
        return this._tradeOk;
    }

    public int getTempID() {
        return this._tempID;
    }

    public void setTempID(int tempID) {
        this._tempID = tempID;
    }

    public boolean () {
        return this._;
    }

    public void (boolean flag) {
        this._ = flag;
    }

    public boolean isDrink() {
        return this._isDrink;
    }

    public void setDrink(boolean flag) {
        this._isDrink = flag;
    }

    public boolean isGres() {
        return this._isGres;
    }

    public void setGres(boolean flag) {
        this._isGres = flag;
    }

    public boolean isPinkName() {
        return this._isPinkName;
    }

    public void setPinkName(boolean flag) {
        this._isPinkName = flag;
    }

    public void setShopChat(byte[] chat) {
        this._shopChat = chat;
    }

    public byte[] getShopChat() {
        return this._shopChat;
    }

    public boolean isPrivateShop() {
        return this._isPrivateShop;
    }

    public void setPrivateShop(boolean flag) {
        this._isPrivateShop = flag;
    }

    public boolean isPrivateReady() {
        return this._isPrivateReady;
    }

    public void setPrivateReady(boolean b) {
        this._isPrivateReady = b;
    }

    public int get_SpecialSize() {
        return this._special_size;
    }

    public void set_SpecialSize(int special_size) {
        this._special_size = special_size;
    }

    public int getPartnersPrivateShopItemCount() {
        return this._partnersPrivateShopItemCount;
    }

    public void setPartnersPrivateShopItemCount(int i) {
        this._partnersPrivateShopItemCount = i;
    }

    public int getBirthDay() {
        return this.birthday;
    }

    public void setBirthDay(int t) {
        this.birthday = t;
    }

    public int getTelType() {
        return this._TelType;
    }

    public void setTelType(int i) {
        this._TelType = i;
    }

    public int getAinState() {
        return this.AinState;
    }

    public void setAinState(int AinState) {
        this.AinState = AinState;
    }

    public void sendPackets(MJIProtoMessage message, int messageId) {
        sendPackets(message, messageId, true);
    }

    public void sendPackets(MJIProtoMessage message, MJEProtoMessages e, boolean isClear) {
        sendPackets(message, e.toInt(), isClear);
    }

    public void sendPackets(MJIProtoMessage message, MJEProtoMessages e) {
        sendPackets(message, e, true);
    }

    public void sendPackets(MJIProtoMessage message, int messageId, boolean isClear) {
        if (getNetConnection() != null)
            getNetConnection().sendPacket(message, messageId, isClear);
    }

    public void sendPackets(ProtoOutputStream[] streams, boolean isClear) {
        if (getNetConnection() != null) {
            GameClient clnt = getNetConnection();
            for (ProtoOutputStream stream : streams)
                clnt.sendPacket(stream, isClear);
        }
    }

    public void sendPackets(ProtoOutputStream stream, boolean isClear) {
        if (getNetConnection() != null)
            getNetConnection().sendPacket(stream, isClear);
    }

    public void sendPackets(ProtoOutputStream stream) {
        if (getNetConnection() != null)
            getNetConnection().sendPacket(stream);
    }

    public void sendPackets(ServerBasePacket[] pcks, boolean clear) {
        if (getNetConnection() != null)
            for (ServerBasePacket pck : pcks)
                getNetConnection().sendPacket(pck, clear);
    }

    public void sendPackets(ServerBasePacket serverbasepacket, boolean clear) {
        try {
            if (getNetConnection() != null)
                getNetConnection().sendPacket(serverbasepacket, clear);
        } catch (Exception exception) {}
    }

    public void sendPackets(String s) {
        sendPackets((ServerBasePacket)new S_SystemMessage(s), true);
    }

    public void sendPackets(int code, String value) {
        sendPackets((ServerBasePacket)new S_ServerMessage(code, value), true);
    }

    public void sendPackets(int code) {
        sendPackets((ServerBasePacket)new S_ServerMessage(code), true);
    }

    public void sendPackets(ServerBasePacket serverbasepacket) {
        if (getNetConnection() == null)
            return;
        try {
            getNetConnection().sendPacket(serverbasepacket);
        } catch (Exception exception) {}
    }

    public void onAction(L1PcInstance attacker) {
        if (attacker == null)
            return;
        if (get_teleport() && getAI() == null)
            return;
        if (getZoneType() == 1 || attacker.getZoneType() == 1) {
            L1Attack attack_mortion = new L1Attack(attacker, this);
            attack_mortion.action();
            return;
        }
        if (checkNonPvP(this, attacker) == true)
            return;
        if (getCurrentHp() > 0 && !isDead()) {
            attacker.delInvis();
            boolean isCounterBarrier = false;
            boolean isMortalBody = false;
            boolean isConqure = false;
            L1Attack attack = new L1Attack(attacker, this);
            L1Magic magic = null;
            if (attack.calcHit()) {
                if (hasSkillEffect(91)) {
                    if (!hasSkillEffect(87) && !hasSkillEffect(5056) && !hasSkillEffect(123) && !hasSkillEffect(208) && !hasSkillEffect(5037) && !hasSkillEffect(15037) && !hasSkillEffect(242) && !hasSkillEffect(100242) && !hasSkillEffect(30006) && !hasSkillEffect(30005) && !hasSkillEffect(30081) && !hasSkillEffect(707113) && !hasSkillEffect(22055) && !hasSkillEffect(707041) && !hasSkillEffect(707119) && !hasSkillEffect(707056) && !hasSkillEffect(707099) && !hasSkillEffect(707054) && !hasSkillEffect(22025) && !hasSkillEffect(22026) && !hasSkillEffect(22027) && !hasSkillEffect(22031) && !hasSkillEffect(51006) && !hasSkillEffect(707152) && !hasSkillEffect(707159)) {
                        magic = new L1Magic(this, attacker);
                        boolean isProbability = magic.calcProbabilityMagic(91);
                        boolean isShortDistance = attack.isShortDistance();
                        if (isProbability && isShortDistance)
                            if (isPassive(MJPassiveID.COUNTER_BARRIER_MASTER.toInt())) {
                                int hp_bonus = getCurrentHp() + getAbility().getTotalCon() / 2;
                                isCounterBarrier = true;
                                setCurrentHp(hp_bonus);
                            } else if (attacker != null && attacker.isPassive(MJPassiveID.PARADOX.toInt()) && MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY)) {
                                attacker.send_effect(18518);
                                isCounterBarrier = false;
                            } else {
                                isCounterBarrier = true;
                            }
                    }
                } else if (hasSkillEffect(394)) {
                    magic = new L1Magic(this, attacker);
                    boolean isProbability = magic.calcProbabilityMagic(394);
                    boolean isShortDistance1 = attack.isShortDistance1();
                    if (isProbability && isShortDistance1)
                        if (attacker != null && attacker.isPassive(MJPassiveID.PARADOX.toInt()) && MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY)) {
                            attacker.send_effect(18518);
                            isMortalBody = false;
                        } else {
                            isMortalBody = true;
                        }
                } else if (isPassive(MJPassiveID.CONQUEROR.toInt()) && getWeapon() != null) {
                    L1ItemInstance weapon = getWeapon();
                    if (weapon.getItem().getType() == 1 || weapon.getItem().getType() == 2) {
                        magic = new L1Magic(this, attacker);
                        boolean isProbability = magic.calcProbabilityMagic(995048);
                        boolean isShortDistance = attack.isShortDistance();
                        if (isShortDistance & isProbability)
                            isConqure = true;
                    }
                }
                if (!isCounterBarrier && !isMortalBody) {
                    attacker.set_pet_target(this);
                    attack.calcDamage();
                    applySpecialEnchant(attacker);
                    attack.addPcPoisonAttack(attacker, this);
                }
            }
            if (getMapId() == 13005) {
                isCounterBarrier = false;
                isMortalBody = false;
                isConqure = false;
            }
            if (isCounterBarrier) {
                attack.actionCounterBarrier();
                attack.commitCounterBarrier();
                attack.commit();
            } else if (isMortalBody) {
                attack.calcDamage();
                attack.actionMortalBody();
                attack.commitMortalBody();
                attack.commit();
            } else if (isConqure) {
                attack.commitConqure();
                attack.actionConqure();
            } else {
                attack.action();
                attack.commit();
            }
        }
    }

    private void applySpecialEnchant(L1PcInstance attacker) {
        if (getWeapon() == null || !getWeapon().isSpecialEnchantable())
            return;
        for (int i = 1; i <= 3; i++) {
            int specialEnchant = getWeapon().getSpecialEnchant(i);
            if (specialEnchant == 0)
                break;
            if (_random.nextInt(100) < 1) {
                boolean success = true;
                switch (specialEnchant) {
                    case 1:
                        success = false;
                        break;
                    case 2:
                        (new L1SkillUse()).handleCommands(this, 31, getId(), getX(), getY(), null, 0, 4);
                        break;
                    case 3:
                    case 4:
                    case 5:
                        success = false;
                        break;
                    case 6:
                        if (attacker.hasSkillEffect(1000) || attacker.hasSkillEffect(1001) || attacker.hasSkillEffect(52) || attacker.hasSkillEffect(101)) {
                            attacker.killSkillEffectTimer(1000);
                            attacker.killSkillEffectTimer(1001);
                            attacker.killSkillEffectTimer(52);
                            attacker.killSkillEffectTimer(101);
                            attacker.sendPackets((ServerBasePacket)new S_SkillBrave(attacker.getId(), 0, 0));
                            attacker.broadcastPacket((ServerBasePacket)new S_SkillBrave(attacker.getId(), 0, 0));
                            attacker.setBraveSpeed(0);
                            attacker.sendPackets((ServerBasePacket)new S_SkillHaste(attacker.getId(), 0, 0));
                            attacker.broadcastPacket((ServerBasePacket)new S_SkillHaste(attacker.getId(), 0, 0));
                            attacker.setMoveSpeed(0);
                        }
                        break;
                    case 7:
                    case 8:
                        success = false;
                        break;
                    case 10:
                        success = false;
                        break;
                }
                if (success)
                    break;
            }
        }
    }

    public boolean checkNonPvP(L1PcInstance pc, L1Character target) {
        L1PcInstance targetpc = null;
        if (target instanceof L1PcInstance) {
            targetpc = (L1PcInstance)target;
        } else if (target instanceof MJCompanionInstance) {
            targetpc = ((MJCompanionInstance)target).get_master();
        } else if (target instanceof L1PetInstance) {
            targetpc = ((L1PetInstance)target).getMaster();
        } else if (target instanceof L1SummonInstance) {
            targetpc = (L1PcInstance)((L1SummonInstance)target).getMaster();
        }
        if (targetpc == null)
            return false;
        if (!Config.ServerAdSetting.SERVERPVPSETTING) {
            if (getMap().isCombatZone((Point)getLocation()))
                return false;
            L1Clan clan = pc.getClan();
            L1Clan enemyclan = targetpc.getClan();
            if (clan != null && enemyclan != null) {
                MJWar war = clan.getCurrentWar();
                MJWar enemyWar = enemyclan.getCurrentWar();
                if (war != null && enemyWar != null && war.equals(enemyWar))
                    return false;
            }
            if (target instanceof L1PcInstance) {
                L1PcInstance targetPc = (L1PcInstance)target;
                if (isInWarAreaAndWarTime(pc, targetPc))
                    return false;
            }
            return true;
        }
        return false;
    }

    public boolean isInWarAreaAndWarTime(L1PcInstance pc, L1PcInstance target) {
        int castleId = L1CastleLocation.getCastleIdByArea(pc);
        int targetCastleId = L1CastleLocation.getCastleIdByArea(target);
        if (castleId != 0 && targetCastleId != 0 && castleId == targetCastleId && MJCastleWarBusiness.getInstance().isNowWar(castleId))
            return true;
        return false;
    }

    public void setPetTarget(L1Character target) {
        Object[] petList = getPetList().values().toArray();
        L1PetInstance pets = null;
        L1SummonInstance summon = null;
        for (Object pet : petList) {
            if (pet != null)
                if (pet instanceof L1PetInstance) {
                    pets = (L1PetInstance)pet;
                    pets.setMasterTarget(target);
                } else if (pet instanceof L1SummonInstance) {
                    summon = (L1SummonInstance)pet;
                    summon.setMasterTarget(target);
                }
        }
    }

    public void set_pet_target(L1Character target) {
        setPetTarget(target);
        if (this.m_companion != null)
            this.m_companion.set_target(target);
    }

    public boolean isstop() {
        return (hasSkillEffect(87) || hasSkillEffect(5003) || hasSkillEffect(123) || hasSkillEffect(70705) || hasSkillEffect(243) || hasSkillEffect(242) || hasSkillEffect(208) || hasSkillEffect(157) || hasSkillEffect(5056) || hasSkillEffect(30005) || hasSkillEffect(30081) || hasSkillEffect(22055) || hasSkillEffect(707113) || hasSkillEffect(707152) || hasSkillEffect(707054) || hasSkillEffect(707056) || hasSkillEffect(22025) || hasSkillEffect(707159) || hasSkillEffect(707041) || hasSkillEffect(707119) || hasSkillEffect(707099) || hasSkillEffect(22026) || hasSkillEffect(22027) || hasSkillEffect(22031) || hasSkillEffect(5027) || hasSkillEffect(995049) || hasSkillEffect(7320183) || hasSkillEffect(77) || hasSkillEffect(51006));
    }

    public void delInvis() {
        if (isGm())
            return;
        if (hasSkillEffect(60)) {
            killSkillEffectTimer(60);
            sendPackets((ServerBasePacket)new S_Invis(getId(), 0));
            broadcastPacket((ServerBasePacket)new S_Invis(getId(), 0));
            broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
            if (getMagicDoll() != null) {
                sendPackets((ServerBasePacket)new S_Invis(getMagicDoll().getId(), 0));
                broadcastPacket((ServerBasePacket)new S_Invis(getMagicDoll().getId(), 0));
                broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(getMagicDoll()));
            }
        }
        if (hasSkillEffect(97)) {
            killSkillEffectTimer(97);
            sendPackets((ServerBasePacket)new S_Invis(getId(), 0));
            broadcastPacket((ServerBasePacket)new S_Invis(getId(), 0));
            broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
            if (getMagicDoll() != null) {
                sendPackets((ServerBasePacket)new S_Invis(getMagicDoll().getId(), 0));
                broadcastPacket((ServerBasePacket)new S_Invis(getMagicDoll().getId(), 0));
                broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(getMagicDoll()));
            }
            if (isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())) {
                sendPackets((ServerBasePacket)new S_Invis(getId(), 0));
                broadcastPacket((ServerBasePacket)new S_Invis(getId(), 0));
                broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
                addMoveDelayRate(-50.0D);
            }
        }
        L1DollInstance doll = getMagicDoll();
        if (doll != null)
            for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this))
                doll.onPerceive(pc);
    }

    public void delBlindHiding() {
        killSkillEffectTimer(97);
        if (isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt()))
            addMoveDelayRate(-50.0D);
        sendPackets((ServerBasePacket)new S_Invis(getId(), 0));
        broadcastPacket((ServerBasePacket)new S_Invis(getId(), 0));
        broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
    }

    public void receiveDamage(L1Character attacker, int damage, int attr) {
        if (damage == 0)
            return;
        Random random = new Random(System.nanoTime());
        int player_mr = getResistance().getEffectedMrBySkill();
        int rnd = random.nextInt(100) + 1;
        if (player_mr >= rnd)
            damage /= 2;
        receiveDamage(attacker, damage);
    }

    public void receiveManaDamage(L1Character attacker, int mpDamage) {
        if (mpDamage > 0 && !isDead()) {
            delInvis();
            if (attacker instanceof L1PcInstance)
                L1PinkName.onAction(this, attacker);
            int newMp = getCurrentMp() - mpDamage;
            setCurrentMp(newMp);
        }
    }

    public boolean isInWarArea() {
        boolean isNowWar = false;
        int castleId = L1CastleLocation.getCastleIdByArea(this);
        if (castleId != 0)
            isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
        return isNowWar;
    }

    public void receiveCounterBarrierDamage(L1Character attacker, int damage) {
        try {
            if (getCurrentHp() > 0 && !isDead()) {
                if (attacker != null && attacker != this && !knownsObject((L1Object)attacker) && attacker.getMapId() == getMapId())
                    attacker.onPerceive(this);
                if (damage > 0) {
                    delInvis();
                    if (hasSkillEffect(66)) {
                        removeSkillEffect(66);
                    } else if (hasSkillEffect(212)) {
                        removeSkillEffect(212);
                    }
                    if (attacker.instanceOf(4) && is_combat_field()) {
                        MJCombatObserver observer = MJCombatLoadManager.getInstance().get_current_observer(get_current_combat_id());
                        if (observer != null) {
                            observer.on_damage((L1PcInstance)attacker, this, damage);
                        } else {
                            set_instance_status(MJEPcStatus.WORLD);
                        }
                    }
                } else if (damage < 0) {
                    return;
                }
                if (getInventory().checkEquipped(145) || getInventory().checkEquipped(149))
                    damage = (int)(damage * 1.5D);
                if (hasSkillEffect(5055)) {
                    double presher_dmg = 0.0D;
                    if (attacker == getPresherPc()) {
                        presher_dmg = damage * Config.MagicAdSetting_Lancer.PRESHER_PCPCDMG;
                    } else {
                        presher_dmg = damage * Config.MagicAdSetting_Lancer.PRESHER_ETCPCDMG;
                    }
                    addPresherDamage((int)presher_dmg);
                }
                int newHp = getCurrentHp() - damage;
                if (newHp > getMaxHp())
                    newHp = getMaxHp();
                if (newHp <= 10)
                    if (newHp <= 10 && is_halpas_armor() && ishalpaspaith() && checkHalpasTime()) {
                        setCurrentHp(getMaxHp());
                        setCurrentMp(getMaxMp());
                        sendPackets((ServerBasePacket)new S_HPUpdate(getCurrentHp(), getMaxHp()));
                        sendPackets((ServerBasePacket)new S_MPUpdate(getCurrentMp(), getMaxMp()));
                        int enchant_gap = get_halpas_armor_enchant();
                        int DRAGON_ARMOR_BLESSING_TIME = 3600 * (22 - enchant_gap * 2) * 1000;
                        int DRAGON_ARMOR_BLESSING_REDUC_TIME = 12;
                        this.HalpasArmor.setHalpas_Time(new Timestamp(System.currentTimeMillis() + DRAGON_ARMOR_BLESSING_TIME));
                        getInventory().updateItem(this.HalpasArmor, 4096);
                        getInventory().saveItem(this.HalpasArmor, 4096);
                        L1SkillUse.off_icons(this, 8003);
                        L1SkillUse.on_icons(this, 8001, DRAGON_ARMOR_BLESSING_TIME / 1000);
                        set_halpas_faith_pvp_reduc(get_halpas_armor_enchant());
                        getResistance().addcalcPcDefense(12 + get_halpas_armor_enchant());
                        setSkillEffect(8002, DRAGON_ARMOR_BLESSING_REDUC_TIME);
                        L1SkillUse.on_icons(this, 8002, DRAGON_ARMOR_BLESSING_REDUC_TIME);
                        send_party_effect(19074);
                    } else if (isElf() && hasSkillEffect(135)) {
                        if (isPassive(MJPassiveID.SOUL_BARRIER_ARMOR.toInt()))
                            if (attacker instanceof L1PcInstance) {
                                int level = getLevel();
                                int rate = 8;
                                if (level > 90)
                                    rate += (level - 90) / 2 * 2;
                                if (rate > 20)
                                    rate = 20;
                                damage -= rate;
                            } else {
                                int level = getLevel();
                                int rate = 4;
                                if (level > 90)
                                    rate += (level - 90) / 2;
                                if (rate > 10)
                                    rate = 10;
                                damage -= rate;
                            }
                        int newMp = getCurrentMp() - damage;
                        setCurrentHp(10);
                        if (newMp <= 0) {
                            death(attacker, true);
                            setCurrentHp(0);
                        }
                        send_effect(14541, true);
                        setCurrentMp(newMp);
                    } else if (newHp <= 0) {
                        if (isGm()) {
                            setCurrentHp(getMaxHp());
                        } else if (isDeathMatch()) {
                            if (getMapId() == 5153) {
                                try {
                                    save();
                                    beginGhost(getX(), getY(), getMapId(), true);
                                    sendPackets((ServerBasePacket)new S_ServerMessage(1271));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        } else {
                            death(attacker, false);
                        }
                    }
                if (newHp > 0)
                    setCurrentHp(newHp);
            } else if (!isDead()) {
                death(attacker, false);
                System.out.println(String.format("HP \r\n%s -> %s : %d / HP:", new Object[] { (attacker == null) ? "null" : attacker.getName(), getName(), Integer.valueOf(damage), Integer.valueOf(getMaxHp()) }));
                sendPackets(");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("%s -> %s : %d", new Object[] { (attacker == null) ? "null" : attacker.getName(), getName(), Integer.valueOf(damage) }));
        }
    }

    public void receiveConqureDamage(L1Character attacker, int damage) {
        try {
            if (getCurrentHp() > 0 && !isDead()) {
                if (attacker != null && attacker != this && !knownsObject((L1Object)attacker) && attacker.getMapId() == getMapId())
                    attacker.onPerceive(this);
                if (damage > 0) {
                    delInvis();
                    if (hasSkillEffect(66)) {
                        removeSkillEffect(66);
                    } else if (hasSkillEffect(212)) {
                        removeSkillEffect(212);
                    }
                    if (attacker.instanceOf(4) && is_combat_field()) {
                        MJCombatObserver observer = MJCombatLoadManager.getInstance().get_current_observer(get_current_combat_id());
                        if (observer != null) {
                            observer.on_damage((L1PcInstance)attacker, this, damage);
                        } else {
                            set_instance_status(MJEPcStatus.WORLD);
                        }
                    }
                    L1Magic magic = new L1Magic(attacker, this);
                    boolean isStun = magic.calcProbabilityMagic(995049);
                    if (isStun);
                } else if (damage < 0) {
                    return;
                }
                if (hasSkillEffect(5055)) {
                    double presher_dmg = 0.0D;
                    if (attacker == getPresherPc()) {
                        presher_dmg = damage * Config.MagicAdSetting_Lancer.PRESHER_PCPCDMG;
                    } else {
                        presher_dmg = damage * Config.MagicAdSetting_Lancer.PRESHER_ETCPCDMG;
                    }
                    addPresherDamage((int)presher_dmg);
                }
                int newHp = getCurrentHp() - damage;
                if (newHp > getMaxHp())
                    newHp = getMaxHp();
                if (newHp <= 10)
                    if (newHp <= 10 && is_halpas_armor() && ishalpaspaith() && checkHalpasTime()) {
                        setCurrentHp(getMaxHp());
                        setCurrentMp(getMaxMp());
                        sendPackets((ServerBasePacket)new S_HPUpdate(getCurrentHp(), getMaxHp()));
                        sendPackets((ServerBasePacket)new S_MPUpdate(getCurrentMp(), getMaxMp()));
                        int enchant_gap = get_halpas_armor_enchant();
                        int DRAGON_ARMOR_BLESSING_TIME = 3600 * (22 - enchant_gap * 2) * 1000;
                        int DRAGON_ARMOR_BLESSING_REDUC_TIME = 12;
                        this.HalpasArmor.setHalpas_Time(new Timestamp(System.currentTimeMillis() + DRAGON_ARMOR_BLESSING_TIME));
                        getInventory().updateItem(this.HalpasArmor, 4096);
                        getInventory().saveItem(this.HalpasArmor, 4096);
                        L1SkillUse.off_icons(this, 8003);
                        L1SkillUse.on_icons(this, 8001, DRAGON_ARMOR_BLESSING_TIME / 1000);
                        set_halpas_faith_pvp_reduc(get_halpas_armor_enchant());
                        getResistance().addcalcPcDefense(12 + get_halpas_armor_enchant());
                        setSkillEffect(8002, DRAGON_ARMOR_BLESSING_REDUC_TIME);
                        L1SkillUse.on_icons(this, 8002, DRAGON_ARMOR_BLESSING_REDUC_TIME);
                        send_party_effect(19074);
                    } else if (isElf() && hasSkillEffect(135)) {
                        if (isPassive(MJPassiveID.SOUL_BARRIER_ARMOR.toInt()))
                            if (attacker instanceof L1PcInstance) {
                                int level = getLevel();
                                int rate = 8;
                                if (level > 90)
                                    rate += (level - 90) / 2 * 2;
                                if (rate > 20)
                                    rate = 20;
                                damage -= rate;
                            } else {
                                int level = getLevel();
                                int rate = 4;
                                if (level > 90)
                                    rate += (level - 90) / 2;
                                if (rate > 10)
                                    rate = 10;
                                damage -= rate;
                            }
                        int newMp = getCurrentMp() - damage;
                        setCurrentHp(10);
                        if (newMp <= 0) {
                            death(attacker, true);
                            setCurrentHp(0);
                        }
                        send_effect(14541, true);
                        setCurrentMp(newMp);
                    } else if (newHp <= 0) {
                        if (isGm()) {
                            setCurrentHp(getMaxHp());
                        } else if (isDeathMatch()) {
                            if (getMapId() == 5153) {
                                try {
                                    save();
                                    beginGhost(getX(), getY(), getMapId(), true);
                                    sendPackets((ServerBasePacket)new S_ServerMessage(1271));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        } else {
                            death(attacker, false);
                        }
                    }
                if (newHp > 0)
                    setCurrentHp(newHp);
            } else if (!isDead()) {
                death(attacker, false);
                System.out.println(String.format("HP \r\n%s -> %s : %d / HP:", new Object[] { (attacker == null) ? "null" : attacker.getName(), getName(), Integer.valueOf(damage), Integer.valueOf(getMaxHp()) }));
                sendPackets(");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("%s -> %s : %d", new Object[] { (attacker == null) ? "null" : attacker.getName(), getName(), Integer.valueOf(damage) }));
        }
    }

    private int calculate_sprite_pvp_damage(int sprite_id) {
        switch (sprite_id) {
            case 13715:
            case 13717:
            case 13721:
            case 13723:
            case 13725:
            case 13727:
            case 13729:
            case 13731:
            case 13733:
            case 13735:
            case 13737:
            case 13739:
            case 13741:
            case 13743:
            case 13745:
            case 15115:
            case 15986:
            case 16002:
            case 16008:
            case 16014:
            case 16027:
            case 16040:
            case 16053:
            case 16056:
            case 16074:
            case 16284:
            case 17515:
            case 17531:
            case 17535:
            case 17541:
            case 17545:
            case 17549:
            case 20085:
            case 20086:
            case 20087:
            case 20088:
            case 20089:
            case 20090:
            case 20091:
            case 20092:
            case 20093:
            case 20094:
            case 20095:
            case 20096:
            case 20097:
            case 20098:
            case 20099:
            case 20100:
            case 20101:
            case 20102:
            case 20103:
            case 20104:
            case 20438:
            case 20442:
            case 20446:
                return 2;
        }
        return 0;
    }

    public void receiveDamage(L1Character attacker, int damage) {
        if (damage > 0) {
            if (attacker == this)
                return;
            if (hasSkillEffect(5152) && get_divine_protection() - damage >= 0) {
                set_divine_protection(get_divine_protection() - damage);
                sendPackets(SC_INSTANCE_HP_NOTI.make_stream(this), true);
                return;
            }
            if (hasSkillEffect(5152) && get_divine_protection() - damage < 0) {
                damage -= get_divine_protection();
                send_effect(20141, true);
                removeSkillEffect(5152);
                sendPackets(SC_INSTANCE_HP_NOTI.make_stream(this), true);
            }
            if (hasSkillEffect(234) && damage > 0)
                damage = (int)(damage - damage * Config.MagicAdSetting_DarkElf.LUCIFERCHANCE);
            if (FatigueProperty.getInstance().use_fatigue() && attacker instanceof L1MonsterInstance) {
                Account account = getAccount();
                if (account != null && getAI() == null && account.has_fatigue())
                    damage = (int)(damage + damage * FatigueProperty.getInstance().get_fatigue_effect_reduction());
            }
            if (hasSkillEffect(103))
                removeSkillEffect(103);
            if (attacker instanceof L1PcInstance) {
                damage += calculate_sprite_pvp_damage(attacker.getCurrentSpriteId());
                if (is_combat_field()) {
                    L1PcInstance attacker_pc = (L1PcInstance)attacker;
                    if (get_current_combat_id() == attacker_pc.get_current_combat_id() && get_current_combat_team_id() == attacker_pc.get_current_combat_team_id())
                        return;
                }
            }
        }
        if ((getCurrentHp() > 0 && !isDead()) || getAI() != null || isGm()) {
            if (isLock())
                damage = 0;
            if (attacker != null && attacker != this && !knownsObject((L1Object)attacker) && attacker.getMapId() == getMapId())
                attacker.onPerceive(this);
            if (damage > 0) {
                if (attacker instanceof L1PcInstance) {
                    L1PcInstance attacker_pc = (L1PcInstance)attacker;
                    L1PinkName.onAction(this, attacker);
                    if (is_combat_field()) {
                        MJCombatObserver observer = MJCombatLoadManager.getInstance().get_current_observer(get_current_combat_id());
                        if (observer != null) {
                            observer.on_damage(attacker_pc, this, damage);
                        } else {
                            set_instance_status(MJEPcStatus.WORLD);
                        }
                    }
                    MJCompanionInstance companion = attacker_pc.get_companion();
                    if (companion != null && companion.get_command_state().equals(CompanionT.eCommand.TM_Aggressive))
                        if (getZoneType() == 0 && companion.getZoneType() == 0)
                            companion.do_pink_name();
                } else if (attacker instanceof MJCompanionInstance) {
                    MJCompanionInstance companion = (MJCompanionInstance)attacker;
                    if (getZoneType() == 0 && companion.getZoneType() == 0)
                        companion.do_pink_name();
                }
                for (L1ItemInstance item : this._equipSlot.getArmors()) {
                    MJItemSkillModel model = MJItemSkillModelLoader.getInstance().getDef(item.getItemId());
                    if (model != null)
                        damage = (int)(damage - model.get(attacker, this, item, damage));
                }
                if (damage <= 0)
                    damage = 10;
                if (getAI() != null && getAI().getBotType() == MJBotType.REDKNIGHT)
                    damage += getMaxHp() / Config.ServerAdSetting.RedKnightdieCount;
                delInvis();
                if (hasSkillEffect(66)) {
                    removeSkillEffect(66);
                } else if (hasSkillEffect(212)) {
                    removeSkillEffect(212);
                }
            } else if (damage < 0 && attacker instanceof L1PcInstance) {
                L1PinkName.onHelp(this, attacker);
            }
            if (getInventory().checkEquipped(145) || getInventory().checkEquipped(149))
                damage = (int)(damage * 1.5D);
            if (hasSkillEffect(5055)) {
                double presher_dmg = 0.0D;
                if (attacker == getPresherPc()) {
                    presher_dmg = damage * Config.MagicAdSetting_Lancer.PRESHER_PCPCDMG;
                } else {
                    presher_dmg = damage * Config.MagicAdSetting_Lancer.PRESHER_ETCPCDMG;
                }
                addPresherDamage((int)presher_dmg);
            }
            int newHp = getCurrentHp() - damage;
            if (newHp > getMaxHp())
                newHp = getMaxHp();
            if (newHp <= 10)
                if (is_halpas_armor() && ishalpaspaith() && checkHalpasTime()) {
                    setCurrentHp(getMaxHp());
                    setCurrentMp(getMaxMp());
                    sendPackets((ServerBasePacket)new S_HPUpdate(getCurrentHp(), getMaxHp()));
                    sendPackets((ServerBasePacket)new S_MPUpdate(getCurrentMp(), getMaxMp()));
                    int enchant_gap = get_halpas_armor_enchant();
                    int DRAGON_ARMOR_BLESSING_TIME = 3600 * (22 - enchant_gap * 2) * 1000;
                    int DRAGON_ARMOR_BLESSING_REDUC_TIME = 12;
                    this.HalpasArmor.setHalpas_Time(new Timestamp(System.currentTimeMillis() + DRAGON_ARMOR_BLESSING_TIME));
                    getInventory().updateItem(this.HalpasArmor, 4096);
                    getInventory().saveItem(this.HalpasArmor, 4096);
                    L1SkillUse.off_icons(this, 8003);
                    L1SkillUse.on_icons(this, 8001, DRAGON_ARMOR_BLESSING_TIME / 1000);
                    set_halpas_faith_pvp_reduc(get_halpas_armor_enchant());
                    getResistance().addcalcPcDefense(12 + get_halpas_armor_enchant());
                    setSkillEffect(8002, DRAGON_ARMOR_BLESSING_REDUC_TIME);
                    L1SkillUse.on_icons(this, 8002, DRAGON_ARMOR_BLESSING_REDUC_TIME);
                    send_party_effect(19074);
                } else if (isElf() && hasSkillEffect(135)) {
                    if (isPassive(MJPassiveID.SOUL_BARRIER_ARMOR.toInt()))
                        if (attacker instanceof L1PcInstance) {
                            int level = getLevel();
                            int rate = 8;
                            if (level > 90)
                                rate += (level - 90) / 2 * 2;
                            if (rate > 20)
                                rate = 20;
                            damage -= rate;
                        } else {
                            int level = getLevel();
                            int rate = 4;
                            if (level > 90)
                                rate += (level - 90) / 2;
                            if (rate > 10)
                                rate = 10;
                            damage -= rate;
                        }
                    int newMp = getCurrentMp() - damage;
                    setCurrentHp(10);
                    if (newMp <= 0) {
                        death(attacker, true);
                        setCurrentHp(0);
                    }
                    send_effect(14541, true);
                    setCurrentMp(newMp);
                } else if (newHp <= 0) {
                    if (isGm()) {
                        setCurrentHp(getMaxHp());
                    } else {
                        if (attacker instanceof L1PcInstance)
                            death(attacker, true);
                        if (isDeathMatch()) {
                            if (getMapId() == 5153) {
                                try {
                                    save();
                                    beginGhost(getX(), getY(), getMapId(), true);
                                    sendPackets((ServerBasePacket)new S_ServerMessage(1271));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        } else {
                            death(attacker, true);
                        }
                    }
                }
            if (newHp > 0)
                setCurrentHp(newHp);
        } else if (!isDead()) {
            death(attacker, true);
            System.out.println(String.format("HP \r\n%s -> %s : %d / HP:", new Object[] { (attacker == null) ? "null" : attacker.getName(), getName(), Integer.valueOf(damage), Integer.valueOf(getMaxHp()) }));
            sendPackets(");
        }
    }

    public void death(L1Character lastAttacker, boolean deathPenalty) {
        L1Clan clan = getClan();
        if (clan != null)
            clan.deleteClanRetrieveUser(getId());
        synchronized (this) {
            if (isDead())
                return;
            remove_companion();
            if (hasSkillEffect(1020))
                killSkillEffectTimer(1020);
            setCurrentHp(0);
            setDead(true);
            setStatus(8);
            if (lastAttacker != null && lastAttacker instanceof L1PcInstance) {
                L1PcInstance player = (L1PcInstance)lastAttacker;
                int lawful = getLawful();
                if (!isPinkName() && lawful >= 0 && !getMap().isCombatZone((Point)getLocation())) {
                    long currenttime = System.currentTimeMillis();
                    player.getAccount().set_Pk_Time(currenttime / 1000L);
                }
                if (getRedKnightClanId() != 0 && player.getRedKnightClanId() != 0) {
                    player.addRedKnightKill(1);
                    if (player.getRedKnightKill() >= 5) {
                        player.setRedKnightKill(0);
                        player.setRedKnightDamage(0);
                        player.setRedKnightClanId(0);
                        player.sendPackets((ServerBasePacket)new S_SystemMessage("5));
                    }
                }
                try {
                    if (MJRevengeService.service().use())
                        MJRevengeProvider.provider().onNewKill(player, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (getAI() != null) {
                    MJBotType type = getAI().getBotType();
                    if (type == MJBotType.REDKNIGHT || type == MJBotType.PROTECTOR) {
                        broadcastPacket((ServerBasePacket)new S_SkillSound(getId(), 2236));
                        return;
                    }
                }
                if (is_combat_field()) {
                    MJCombatObserver observer = MJCombatLoadManager.getInstance().get_current_observer(get_current_combat_id());
                    if (observer == null) {
                        set_instance_status(MJEPcStatus.WORLD);
                        getKDA().onKill(player, this);
                    } else {
                        try {
                            observer.on_kill(player, this);
                            L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { (ServerBasePacket)new S_ChatPacket(String.format("\\aG\\aA=> \\aL, new Object[] { player.getName(), getName() }), 153), (ServerBasePacket)new S_SystemMessage("[) });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (getKDA() != null) {
                    getKDA().onKill(player, this);
                }
            }
        }
        GeneralThreadPool.getInstance().execute(new Death(lastAttacker, deathPenalty));
    }

    private class Death implements Runnable {
        L1Character _lastAttacker;

        Death(L1Character cha, boolean deathPenalty) {
            this._lastAttacker = cha;
            if (GMCommands.IS_PROTECTION);
        }

        public void run() {
            L1Character lastAttacker = this._lastAttacker;
            this._lastAttacker = null;
            L1PcInstance.this.setCurrentHp(0);
            L1PcInstance.this.setGresValid(false);
            int targetobjid = L1PcInstance.this.getId();
            L1PcInstance.this.getMap().setPassable((Point)L1PcInstance.this.getLocation(), true);
            int tempchargfx = 0;
            if (L1PcInstance.this.hasSkillEffect(67)) {
                L1PcInstance.this.removeSkillEffect(67);
                tempchargfx = L1PcInstance.this.getCurrentSpriteId();
                L1PcInstance.this.setTempCharGfxAtDead(tempchargfx);
            } else {
                L1PcInstance.this.setTempCharGfxAtDead(L1PcInstance.this.getClassId());
            }
            if (L1PcInstance.this.hasSkillEffect(5055)) {
                L1PcInstance.this.setPresherPc(null);
                L1PcInstance.this.setPresherDamage(0);
                if (L1PcInstance.this.getPresherDeathRecall())
                    L1PcInstance.this.setPresherDeathRecall(false);
                L1PcInstance.this.removeSkillEffect(5055);
            }
            L1PcInstance.this.setCurrentSprite(L1PcInstance.this.getClassId());
            L1PcInstance.this.auraBuff(false);
            if (L1PcInstance.this.getParty() != null)
                L1PcInstance.this.getParty().onDeadMember(L1PcInstance.this);
            L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(L1PcInstance.this, 44, L1PcInstance.this.getId(), L1PcInstance.this.getX(), L1PcInstance.this.getY(), null, 0, 1);
            if (tempchargfx == 5727 || tempchargfx == 5730 || tempchargfx == 5733 || tempchargfx == 5736)
                tempchargfx = 0;
            if (tempchargfx != 0)
                L1PcInstance.this.sendShape(tempchargfx);
            L1PcInstance.this.sendShape(L1PcInstance.this.getTempCharGfxAtDead());
            L1PcInstance.this.sendPackets((ServerBasePacket)new S_DoActionGFX(targetobjid, 8));
            L1PcInstance.this.broadcastPacket((ServerBasePacket)new S_DoActionGFX(targetobjid, 8));
            L1PcInstance.this.isExpDrop = true;
            L1PcInstance.this.isItemDrop = true;
            if (lastAttacker != L1PcInstance.this) {
                L1PcInstance player = null;
                if (lastAttacker instanceof L1PcInstance) {
                    player = (L1PcInstance)lastAttacker;
                } else if (lastAttacker instanceof MJCompanionInstance) {
                    player = ((MJCompanionInstance)lastAttacker).get_master();
                } else if (lastAttacker instanceof L1PetInstance) {
                    player = ((L1PetInstance)lastAttacker).getMaster();
                } else if (lastAttacker instanceof L1SummonInstance) {
                    player = (L1PcInstance)((L1SummonInstance)lastAttacker).getMaster();
                }
                if (player != null && L1PcInstance.this.getZoneType() == -1)
                    return;
                boolean sim_ret = L1PcInstance.this.simWarResult(lastAttacker);
                if (sim_ret == true)
                    return;
            }
            if (!L1PcInstance.this.getMap().isEnabledDeathPenalty()) {
                L1PcInstance.this.sendPackets(3800);
                return;
            }
            L1PcInstance fightPc = null;
            if (lastAttacker instanceof L1PcInstance)
                fightPc = (L1PcInstance)lastAttacker;
            if (fightPc != null && L1PcInstance.this.getFightId() == fightPc.getId() && fightPc.getFightId() == L1PcInstance.this.getId()) {
                L1PcInstance.this.setFightId(0);
                L1PcInstance.this.sendPackets((ServerBasePacket)new S_PacketBox(5, 0, 0));
                fightPc.setFightId(0);
                fightPc.sendPackets((ServerBasePacket)new S_PacketBox(5, 0, 0));
                return;
            }
            if (L1PcInstance.this.is_combat_field())
                return;
            if (GMCommands.IS_PROTECTION) {
                L1PcInstance.this.isExpDrop = false;
                L1PcInstance.this.isItemDrop = false;
                if (L1PcInstance.this.getKDA() != null)
                    L1PcInstance.this.getKDA().onProtection(L1PcInstance.this);
                return;
            }
            boolean castle_ret1 = L1PcInstance.this.castleWarResult();
            if (lastAttacker instanceof L1PcInstance && !castle_ret1 && L1PcInstance.this.getLevel() < Config.ServerAdSetting.NEWPLAYERPROTECTION && lastAttacker.getLevel() - L1PcInstance.this.getLevel() >= 10) {
                L1PcInstance.this.isExpDrop = false;
                L1PcInstance.this.isItemDrop = false;
            }
            if (castle_ret1 == true) {
                L1PcInstance.this.isExpDrop = false;
                L1PcInstance.this.isItemDrop = false;
                L1PcInstance.this.sendPackets((ServerBasePacket)new S_ServerMessage(3798));
            }
            if (L1PcInstance.this.getZoneType() == 1 || L1PcInstance.this.getZoneType() == -1) {
                L1PcInstance.this.isExpDrop = false;
                L1PcInstance.this.isItemDrop = false;
                L1PcInstance.this.sendPackets((ServerBasePacket)new S_ServerMessage(3798));
            }
            if (L1PcInstance.this.getMapId() == 10500 || L1PcInstance.this.getMapId() == 10501) {
                L1PcInstance.this.isExpDrop = false;
                L1PcInstance.this.isItemDrop = false;
                L1PcInstance.this.sendPackets((ServerBasePacket)new S_ServerMessage(3798));
            }
            if (L1PcInstance.this.getLevel() < Config.ServerAdSetting.StartCharBoho) {
                L1PcInstance.this.isExpDrop = false;
                L1PcInstance.this.isItemDrop = false;
                L1PcInstance.this.sendPackets(3801);
            }
            boolean gahouse = false;
            if (L1PcInstance.this.getAccount().get_Pk_Time() + 1800L < System.currentTimeMillis() / 1000L)
                if (L1PcInstance.this.getMapId() == 624 || L1PcInstance.this.getMapId() == 430) {
                    if (L1PcInstance.this.isPcBuff()) {
                        L1PcInstance.this.isExpDrop = false;
                        L1PcInstance.this.isItemDrop = false;
                        gahouse = true;
                    }
                } else if (!L1PcInstance.this.isPinkName()) {
                    L1FreeShield shield = CharacterFreeShieldTable.getInstance().getFreeShield(L1PcInstance.this);
                    if (L1PcInstance.this.isPcBuff() && shield.get_Pc_Gaho() > 0) {
                        L1PcInstance.this.isExpDrop = false;
                        L1PcInstance.this.isItemDrop = false;
                        gahouse = true;
                        CharacterFreeShieldTable.getInstance().usePcGaho(L1PcInstance.this);
                        L1PcInstance.this.sendPackets("PC:" + shield.get_Pc_Gaho() + ");
                                SC_FREE_BUFF_SHIELD_INFO_ACK.send(L1PcInstance.this);
                    } else if (shield.get_Free_Gaho() > 0) {
                        CharacterFreeShieldTable.getInstance().useFreeGaho(L1PcInstance.this);
                        shield.set_Pc_Gaho(shield.get_Pc_Gaho() - 1);
                        shield.set_Pc_Gaho_use(shield.get_Pc_Gaho_use() + 1);
                        L1PcInstance.this.isExpDrop = false;
                        L1PcInstance.this.isItemDrop = false;
                        gahouse = true;
                        L1PcInstance.this.sendPackets(":" + shield.get_Free_Gaho() + ");
                                SC_FREE_BUFF_SHIELD_INFO_ACK.send(L1PcInstance.this);
                    } else if (shield.get_Event_Gaho() > 0) {
                        L1PcInstance.this.isExpDrop = false;
                        L1PcInstance.this.isItemDrop = false;
                        gahouse = true;
                        CharacterFreeShieldTable.getInstance().useEventGaho(L1PcInstance.this);
                        L1PcInstance.this.sendPackets(":" + shield.get_Event_Gaho() + ");
                                SC_FREE_BUFF_SHIELD_INFO_ACK.send(L1PcInstance.this);
                    }
                }
            if (!gahouse && L1PcInstance.this.getInventory().checkItem(4100529) && L1PcInstance.this.getLevel() <= Config.ServerAdSetting.NEWPLAYERLEVELPROTECTION)
                if (lastAttacker instanceof L1PcInstance) {
                    if (L1PcInstance.this.getInventory().checkItem(4100529)) {
                        L1PcInstance.this.isExpDrop = false;
                        L1PcInstance.this.isItemDrop = false;
                        gahouse = true;
                        L1PcInstance.this.specialgahodrop1(lastAttacker);
                    }
                } else {
                    int chance = L1PcInstance._random.nextInt(100);
                    if (chance < 100 && L1PcInstance.this.getInventory().checkItem(4100529)) {
                        L1PcInstance.this.isExpDrop = false;
                        L1PcInstance.this.isItemDrop = false;
                        gahouse = true;
                        L1PcInstance.this.specialgahodrop1(lastAttacker);
                    }
                }
            if (!gahouse && L1PcInstance.this.getInventory().checkItem(4100121))
                if (lastAttacker instanceof L1PcInstance) {
                    if (L1PcInstance.this.getInventory().checkItem(4100121)) {
                        L1PcInstance.this.isExpDrop = false;
                        L1PcInstance.this.isItemDrop = false;
                        gahouse = true;
                        L1PcInstance.this.specialgahodrop(lastAttacker);
                    }
                } else {
                    int chance = L1PcInstance._random.nextInt(100);
                    if (chance < 100 && L1PcInstance.this.getInventory().checkItem(4100121)) {
                        L1PcInstance.this.isExpDrop = false;
                        L1PcInstance.this.isItemDrop = false;
                        gahouse = true;
                        L1PcInstance.this.specialgahodrop(lastAttacker);
                    }
                }
            if (!gahouse && L1PcInstance.this.getInventory().checkItem(4100122))
                if (lastAttacker instanceof L1PcInstance) {
                    if (L1PcInstance.this.getInventory().checkItem(4100122)) {
                        L1PcInstance.this.isExpDrop = false;
                        gahouse = true;
                        L1PcInstance.this.gahodrop(lastAttacker);
                    }
                } else {
                    int chance = L1PcInstance._random.nextInt(100);
                    if (chance < 100 && L1PcInstance.this.getInventory().checkItem(4100122)) {
                        L1PcInstance.this.isExpDrop = false;
                        gahouse = true;
                        L1PcInstance.this.gahodrop(lastAttacker);
                    }
                }
            if (lastAttacker instanceof L1PcInstance) {
                if (L1PcInstance.this.getMapId() >= 1708 && L1PcInstance.this.getMapId() <= 1709 && L1PcInstance.this.getInventory().checkEquipped(900022)) {
                    L1PcInstance.this.isExpDrop = false;
                    L1PcInstance.this.isItemDrop = false;
                    L1PcInstance.this.drop1(lastAttacker);
                }
            } else {
                int chance = L1PcInstance._random.nextInt(100);
                if (chance < 100 && L1PcInstance.this.getMapId() >= 1708 && L1PcInstance.this.getMapId() <= 1709 && L1PcInstance.this.getInventory().checkEquipped(900022)) {
                    L1PcInstance.this.isExpDrop = false;
                    L1PcInstance.this.isItemDrop = false;
                    L1PcInstance.this.drop1(lastAttacker);
                }
            }
            if (lastAttacker instanceof L1PcInstance) {
                if (L1PcInstance.this.getInventory().checkEquipped(10000)) {
                    L1PcInstance.this.isExpDrop = false;
                    L1PcInstance.this.isItemDrop = false;
                    L1PcInstance.this.drop2(lastAttacker);
                }
            } else {
                int chance = L1PcInstance._random.nextInt(100);
                if (chance < 100 && L1PcInstance.this.getInventory().checkEquipped(10000)) {
                    L1PcInstance.this.isExpDrop = false;
                    L1PcInstance.this.isItemDrop = false;
                    L1PcInstance.this.drop2(lastAttacker);
                }
            }
            if (lastAttacker instanceof L1PcInstance) {
                if (L1PcInstance.this.getInventory().checkEquipped(10001)) {
                    L1PcInstance.this.isExpDrop = false;
                    L1PcInstance.this.isItemDrop = false;
                    L1PcInstance.this.drop3(lastAttacker);
                }
            } else {
                int chance = L1PcInstance._random.nextInt(100);
                if (chance < 100 && L1PcInstance.this.getInventory().checkEquipped(10001)) {
                    L1PcInstance.this.isExpDrop = false;
                    L1PcInstance.this.isItemDrop = false;
                    L1PcInstance.this.drop3(lastAttacker);
                }
            }
            if (lastAttacker instanceof L1CastleGuardInstance)
                L1PcInstance.this.setLastPk((Timestamp)null);
            if (L1PcInstance.this.isExpDrop) {
                L1PcInstance.this.deathPenalty();
                L1PcInstance.this.setGresValid(true);
                if (L1PcInstance.this.get_exp_res() == 0L && (!(lastAttacker instanceof L1PcInstance) || L1PcInstance.this.getLevel() >= Config.ServerAdSetting.NEWPLAYERPROTECTION || lastAttacker.getLevel() - L1PcInstance.this.getLevel() < 10))
                    L1PcInstance.this.set_exp_res(1L);
                if (lastAttacker instanceof L1GuardInstance) {
                    if (L1PcInstance.this.get_PKcount() > 0)
                        L1PcInstance.this.set_PKcount(L1PcInstance.this.get_PKcount() - 1);
                    L1PcInstance.this.setLastPk((Timestamp)null);
                }
            }
            if (L1PcInstance.this.isItemDrop) {
                int lawful = L1PcInstance.this.getLawful();
                int dropCount = Config.CharSettings.LAWFUL_DROP_COUNT;
                Random random = new Random();
                int rnd = random.nextInt(1000) + 1;
                int lostRate = (int)((32768.0D - L1PcInstance.this.getLawful()) * 3.0D / 100.0D) / 3;
                lostRate += 100;
                if (lawful <= 0)
                    lostRate *= 2;
                if (rnd <= lostRate)
                    if (lawful >= 0 && lawful <= Config.CharSettings.DROP_LAWFUL_MIN) {
                        dropCount = MJRnd.next(Config.CharSettings.DROP_LAWFUL_COUNT) + 1;
                    } else if (lawful >= Config.CharSettings.DROP_LAWFUL1 && lawful <= Config.CharSettings.DROP_LAWFUL_MAX) {
                        dropCount = MJRnd.next(Config.CharSettings.DROP_LAWFUL_COUNT1) + 1;
                    } else if (lawful >= -Config.CharSettings.DROP_LAWFUL3 && lawful <= -Config.CharSettings.DROP_LAWFUL2) {
                        dropCount = MJRnd.next(Config.CharSettings.DROP_LAWFUL_COUNT2) + 1;
                    } else if (lawful >= -Config.CharSettings.DROP_LAWFUL5 && lawful <= -Config.CharSettings.DROP_LAWFUL4) {
                        dropCount = MJRnd.next(Config.CharSettings.DROP_LAWFUL_COUNT3) + 1;
                    } else {
                        dropCount = 0;
                    }
                if (dropCount > 0)
                    L1PcInstance.this.caoPenaltyResult(lastAttacker, dropCount);
            }
            L1PcInstance.this.isExpDrop = false;
            L1PcInstance.this.isItemDrop = false;
            boolean castle_ret = L1PcInstance.this.castleWarResult();
            if (castle_ret == true)
                return;
        }
    }

    private void caoPenaltyResult(L1Character lastAttacker, int count) {
        int castle_id = L1CastleLocation.getCastleIdByArea(this);
        if (castle_id != 0)
            return;
        if (getAI() != null)
            return;
        if (count > 0)
            for (Integer i : Config.CharSettings.PVPDROPNO1) {
                if (createAddDropItem(i.intValue()))
                    break;
            }
        ArrayList<L1ItemInstance> dropItems = getInventory().getPossibleDropItems();
        int size = 0;
        for (L1ItemInstance item : dropItems) {
            int rnd = MJRnd.next(dropItems.size());
            item = dropItems.get(rnd);
            NoDropItemTable.L1NoDropItems nodrop_item = NoDropItemTable.getInstance().getPresentItem(item.getItemId());
            if (nodrop_item != null)
                continue;
            if (item.isEquipped())
                getInventory().setEquipped(item, false);
            if (item.getBless() > 3 || item.get_Carving() == 1) {
                if (getInventory().removeItem(item, item.isStackable() ? item.getCount() : 1) > 0) {
                    if (MJDeathPenaltyService.service().use()) {
                        add_deathpenalty_item(item);
                        MJDeathPenaltyProvider.provider().senditeminfo(this);
                        MJDeathPenaltyItemDatabaseLoader.getInstance().update(getId(), item);
                    }
                    sendPackets((ServerBasePacket)new S_ServerMessage(158, item.getLogName()));
                    LoggerInstance.getInstance().addItemAction(Logger.ItemActionType.del, this, item, count);
                    size++;
                }
            } else {
                if (lastAttacker instanceof L1PcInstance) {
                    L1PcInstance pc = (L1PcInstance)lastAttacker;
                    if (DropDelayItemTable.getInstance().isItem(item.getItemId())) {
                        L1DropDelayItem temp = DropDelayItemTable.getInstance().getItem(item.getItemId());
                        if (temp != null && temp.getItemId() == item.getItemId())
                            item.startItemOwnerTimer(pc);
                    }
                }
                item.setGiveItem(true);
                if (getInventory().tradeItem(item, item.isStackable() ? item.getCount() : 1, (L1Inventory)L1World.getInstance().getInventory(getX(), getY(), getMapId())) != null) {
                    sendPackets((ServerBasePacket)new S_ServerMessage(638, item.getLogName()));
                    LoggerInstance.getInstance().addItemAction(Logger.ItemActionType.DeathDrop, this, item, count);
                    size++;
                }
            }
            if (size >= count)
                break;
        }
    }

    private boolean createAddDropItem(int itemId) {
        L1ItemInstance item = getInventory().findItemId(itemId);
        if (item == null || !MJRnd.isWinning(10000, Config.CharSettings.PENALTY_POR))
            return false;
        int item_count = item.getCount();
        if (getLawful() >= 0)
            item_count = (item_count <= 2) ? 1 : (item_count / 2);
        item.setGiveItem(true);
        getInventory().tradeItem(item, item_count, (L1Inventory)L1World.getInstance().getInventory(getX(), getY(), getMapId()));
        sendPackets((ServerBasePacket)new S_ServerMessage(638, item.getLogName()));
        LoggerInstance.getInstance().addItemAction(Logger.ItemActionType.DeathDrop, this, item, item_count);
        return true;
    }

    public boolean castleWarResult() {
        int castleId = 0;
        boolean isNowWar = false;
        castleId = L1CastleLocation.getCastleIdByArea(this);
        if (castleId != 0)
            isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
        return isNowWar;
    }

    public boolean simWarResult(L1Character lastAttacker) {
        if (getClanid() == 0)
            return false;
        if (Config.ServerAdSetting.SIMWARPENALTY)
            return false;
        L1PcInstance attacker = null;
        if (lastAttacker instanceof L1PcInstance) {
            attacker = (L1PcInstance)lastAttacker;
        } else if (lastAttacker instanceof MJCompanionInstance) {
            attacker = ((MJCompanionInstance)lastAttacker).get_master();
        } else if (lastAttacker instanceof L1PetInstance) {
            attacker = ((L1PetInstance)lastAttacker).getMaster();
        } else if (lastAttacker instanceof L1SummonInstance) {
            attacker = (L1PcInstance)((L1SummonInstance)lastAttacker).getMaster();
        } else {
            return false;
        }
        L1Clan clan = getClan();
        L1Clan enemy = attacker.getClan();
        if (clan == null || enemy == null || clan.getLeaderId() != getId())
            return false;
        MJWar war = clan.getCurrentWar();
        MJWar enemyWar = enemy.getCurrentWar();
        if (war == null || enemyWar == null || !war.equals(enemyWar))
            return false;
        MJWarFactory.WAR_TYPE type = war.getWarType();
        if (!type.equals(MJWarFactory.WAR_TYPE.NORMAL))
            return false;
        war.notifyWinner(enemy, clan);
        war.notifyEndWar(clan, enemy);
        war.dispose();
        return true;
    }

    public void resExp() {
        double ratio;
        int oldLevel = getLevel();
        long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
        long exp = 0L;
        if (oldLevel < 45) {
            ratio = 0.05D;
        } else if (oldLevel >= 49) {
            ratio = 0.025D;
        } else {
            ratio = 0.05D - (oldLevel - 44) * 0.005D;
        }
        exp = (long)(needExp * ratio);
        if (exp == 0L)
            return;
        add_exp(exp);
    }

    public void resExpToTemple() {
        double ratio;
        int oldLevel = getLevel();
        long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
        long exp = 0L;
        if (oldLevel < 45) {
            ratio = 0.05D;
        } else if (oldLevel >= 45 && oldLevel < 49) {
            ratio = 0.05D - (oldLevel - 44) * 0.005D;
        } else if (oldLevel >= 49 && oldLevel < 52) {
            ratio = 0.025D;
        } else if (oldLevel == 52) {
            ratio = 0.026D;
        } else if (oldLevel > 52 && oldLevel < 74) {
            ratio = 0.026D + (oldLevel - 52) * 0.001D;
        } else if (oldLevel >= 74 && oldLevel < 79) {
            ratio = 0.025D - (oldLevel - 73) * 5.0E-4D;
        } else {
            ratio = 0.03D;
        }
        exp = (long)(needExp * ratio);
        if (exp == 0L)
            return;
        int level = ExpTable.getLevelByExp(this._exp + exp);
        if (level >= 100) {
            S_SystemMessage sm = new S_SystemMessage(" );
                    sendPackets((ServerBasePacket)sm, true);
            return;
        }
        add_exp(exp);
    }

    public void deathPenalty() {
        int oldLevel = getLevel();
        long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
        long exp = 0L;
        if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN && oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX) {
            exp = (long)(needExp * Config.CharSettings.EXP_PENALTY);
        } else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN1 && oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX1) {
            exp = (long)(needExp * Config.CharSettings.EXP_PENALTY1);
        } else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN2 && oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX2) {
            exp = (long)(needExp * Config.CharSettings.EXP_PENALTY2);
        } else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN3 && oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX3) {
            exp = (long)(needExp * Config.CharSettings.EXP_PENALTY3);
        } else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN4 && oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX4) {
            exp = (long)(needExp * Config.CharSettings.EXP_PENALTY4);
        } else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN5 && oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX5) {
            exp = (long)(needExp * Config.CharSettings.EXP_PENALTY5);
        } else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN6 && oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX6) {
            exp = (long)(needExp * Config.CharSettings.EXP_PENALTY6);
        }
        if (exp == 0L)
            return;
        add_exp(-exp);
    }

    public L1BookMark getBookMark(String name) {
        L1BookMark element = null;
        int size = this._bookmarks.size();
        for (int i = 0; i < size; i++) {
            element = this._bookmarks.get(i);
            if (element != null)
                if (element.getName().equalsIgnoreCase(name))
                    return element;
        }
        return null;
    }

    public L1BookMark getBookMark(int id) {
        L1BookMark element = null;
        int size = this._bookmarks.size();
        for (int i = 0; i < size; i++) {
            element = this._bookmarks.get(i);
            if (element != null)
                if (element.getId() == id)
                    return element;
        }
        return null;
    }

    public int getBookMarkSize() {
        return this._bookmarks.size();
    }

    public void addBookMark(L1BookMark book) {
        this._bookmarks.add(book);
    }

    public void removeBookMark(L1BookMark book) {
        this._bookmarks.remove(book);
    }

    public L1ItemInstance getWeapon() {
        return this._weapon;
    }

    public void setWeapon(L1ItemInstance weapon) {
        this._weapon = weapon;
    }

    public L1ItemInstance getWeaponSwap() {
        return getEquipSlot().getWeaponSwap();
    }

    public L1ItemInstance getArmor() {
        return this._armor;
    }

    public void setArmor(L1ItemInstance armor) {
        this._armor = armor;
    }

    public L1ItemInstance getAmory() {
        return this._armory;
    }

    public void setAmory(L1ItemInstance armor) {
        this._armory = armor;
    }

    public L1ItemInstance getSecondWeapon() {
        return this._secondweapon;
    }

    public void setSecondWeapon(L1ItemInstance weapon) {
        this._secondweapon = weapon;
    }

    public L1Quest getQuest() {
        return this._quest;
    }

    public String getClassName() {
        if (isCrown())
            return ";
        if (isKnight())
            return ";
        if (isElf())
            return ";
        if (isWizard())
            return ";
        if (isDarkelf())
            return ";
        if (isDragonknight())
            return ";
        if (isBlackwizard())
            return ";
        if (is())
            return ";
        if (isFencer())
            return ";
        if (isLancer())
            return ";
        return ";
    }

    public int getClassNumber() {
        if (isCrown())
            return 0;
        if (isKnight())
            return 1;
        if (isElf())
            return 2;
        if (isWizard())
            return 3;
        if (isDarkelf())
            return 4;
        if (isDragonknight())
            return 5;
        if (isBlackwizard())
            return 6;
        if (is())
            return 7;
        if (isFencer())
            return 8;
        return 9;
    }

    public boolean isCrown() {
        return (getClassId() == 0 || getClassId() == 1);
    }

    public boolean isKnight() {
        return (getClassId() == 20553 || getClassId() == 48);
    }

    public boolean isElf() {
        return (getClassId() == 138 || getClassId() == 37);
    }

    public boolean isWizard() {
        return (getClassId() == 20278 || getClassId() == 20279);
    }

    public boolean isDarkelf() {
        return (getClassId() == 2786 || getClassId() == 2796);
    }

    public boolean isDragonknight() {
        return (getClassId() == 6658 || getClassId() == 6661);
    }

    public boolean isBlackwizard() {
        return (getClassId() == 6671 || getClassId() == 6650);
    }

    public boolean is() {
        return (getClassId() == 20567 || getClassId() == 20577);
    }

    public boolean isFencer() {
        return (getClassId() == 18520 || getClassId() == 18499);
    }

    public boolean isLancer() {
        return (getClassId() == 19296 || getClassId() == 19299);
    }

    public String getAccountName() {
        return this._accountName;
    }

    public void setAccountName(String s) {
        this._accountName = s;
    }

    public short getBaseMaxHp() {
        return this._baseMaxHp;
    }

    public void addBaseMaxHp(short i) {
        i = (short)(i + this._baseMaxHp);
        if (i >= Short.MAX_VALUE) {
            i = Short.MAX_VALUE;
        } else if (i < 1) {
            i = 1;
        }
        addMaxHp(i - this._baseMaxHp);
        this._baseMaxHp = i;
    }

    public int getBaseMaxMp() {
        return this._baseMaxMp;
    }

    public void addBaseMaxMp(int i) {
        i += this._baseMaxMp;
        if (i >= 32767) {
            i = 32767;
        } else if (i < 0) {
            i = 0;
        }
        addMaxMp(i - this._baseMaxMp);
        this._baseMaxMp = i;
    }

    public int getOriginalMagicHit() {
        return this._originalMagicHit;
    }

    public int getBaseAc() {
        return this._baseAc;
    }

    public int getBaseDmgup() {
        return this._baseDmgup;
    }

    public int getBaseBowDmgup() {
        return this._baseBowDmgup;
    }

    public int getBaseHitup() {
        return this._baseHitup;
    }

    public int getBaseBowHitup() {
        return this._baseBowHitup;
    }

    public int getBaseDecreaseCoolTime() {
        return this._baseDecreaseCoolTime;
    }

    public int getBaseDecreaseCCDuration() {
        return this._baseDecreaseCCDuration;
    }

    public void setBaseDecreaseCoolTime(int i) {
        this._baseDecreaseCoolTime = i;
    }

    public void setBaseDecreaseCCDuration(int i) {
        this._baseDecreaseCCDuration = i;
    }

    public void addBaseDecreaseCCDuration(int i) {
        this._baseDecreaseCCDuration += i;
    }

    public void addBaseDecreaseCoolTime(int i) {
        this._baseDecreaseCoolTime += i;
    }

    public void setBaseMagicHitUp(int i) {
        this._baseMagicHitup = i;
    }

    public void addBaseMagicHitUp(int i) {
        this._baseMagicHitup += i;
    }

    public int getBaseMagicHitUp() {
        return this._baseMagicHitup;
    }

    public int getTotalMagicHitup() {
        return getBaseMagicHitUp() + getOriginalMagicHit();
    }

    public void setBaseMagicCritical(int i) {
        this._baseMagicCritical = i;
    }

    public int getBaseMagicCritical() {
        return this._baseMagicCritical;
    }

    public void addBaseMagicCritical(int i) {
        this._baseMagicCritical += i;
    }

    public void setBaseMagicDmg(int i) {
        this._baseMagicDmg = i;
    }

    public int getBaseMagicDmg() {
        return this._baseMagicDmg;
    }

    public void setBaseMagicDecreaseMp(int i) {
        this._baseMagicDecreaseMp = i;
    }

    public int getBaseMagicDecreaseMp() {
        return this._baseMagicDecreaseMp;
    }

    public int getAdvenHp() {
        return this._advenHp;
    }

    public void setAdvenHp(int i) {
        this._advenHp = i;
    }

    public int getAdvenMp() {
        return this._advenMp;
    }

    public void setAdvenMp(int i) {
        this._advenMp = i;
    }

    public int getMagicBuffHp() {
        return this._magicBuffHp;
    }

    public void setMagicBuffHp(int i) {
        this._magicBuffHp = i;
    }

    public int getHighLevel() {
        return this._highLevel;
    }

    public void setHighLevel(int i) {
        this._highLevel = i;
    }

    public int totalBonusStats() {
        return Math.min(Math.max(getHighLevel() - 50, 0), 127);
    }

    public int remainBonusStats() {
        if (getHighLevel() <= 50)
            return 0;
        int statAmounts = getAbility().getAmount();
        return totalBonusStats() + getElixirStats() + 75 - statAmounts;
    }

    public void sendBonusStats() {
        if (getLevel() > 50 && remainBonusStats() > 0)
            sendPackets((ServerBasePacket)new S_Message_YN(479, String.valueOf(remainBonusStats())));
    }

    public void StartCharBoho() {
        if (getLevel() > 1 && getLevel() < Config.ServerAdSetting.StartCharBoho) {
            sendPackets((ServerBasePacket)S_InventoryIcon.iconNewUnLimit(2563, 3804, true));
        } else {
            sendPackets((ServerBasePacket)S_InventoryIcon.icoEnd(2563));
        }
    }

    public int getElixirStats() {
        return this._elixirStats;
    }

    public void setElixirStats(int i) {
        this._elixirStats = i;
    }

    public int getElfAttr() {
        return this._elfAttr;
    }

    public void setElfAttr(int i) {
        this._elfAttr = i;
    }

    public long get_exp_res() {
        return this._expRes;
    }

    public void set_exp_res(long i) {
        this._expRes = i;
    }

    public int getPartnerId() {
        return this._partnerId;
    }

    public void setPartnerId(int i) {
        this._partnerId = i;
    }

    public int getOnlineStatus() {
        return this._onlineStatus;
    }

    public void setOnlineStatus(int i) {
        this._onlineStatus = i;
    }

    public int getHomeTownId() {
        return this._homeTownId;
    }

    public void setHomeTownId(int i) {
        this._homeTownId = i;
    }

    public int getContribution() {
        return this._contribution;
    }

    public void setContribution(int i) {
        this._contribution = i;
    }

    public int getHellTime() {
        return this._hellTime;
    }

    public void setHellTime(int i) {
        this._hellTime = i;
    }

    public void setMorning(boolean flag) {
        this._morning = flag;
    }

    public boolean getMorning() {
        return this._morning;
    }

    public boolean isBanned() {
        return this._banned;
    }

    public void setBanned(boolean flag) {
        this._banned = flag;
    }

    public int get_food() {
        return this._food;
    }

    public void set_food(int i) {
        this._food = i;
        if (this._food < 225);
    }

    public void add_food(int i) {
        this._food += i;
        if (this._food > 225) {
            this._food = 225;
            if (getCryOfSurvivalTime() == 0L)
                this.SurvivalGauge = System.currentTimeMillis() / 1000L;
        } else if (this._food < 1) {
            this._food = 1;
        }
    }

    public long getCryOfSurvivalTime() {
        return this.SurvivalGauge;
    }

    public void setCryOfSurvivalTime() {
        if (get_food() >= 225)
            this.SurvivalGauge = System.currentTimeMillis() / 1000L;
    }

    public long getSurvivalGauge() {
        return this.SurvivalGauge;
    }

    public void setSurvivalGauge(long SurvivalGauge) {
        this.SurvivalGauge = SurvivalGauge;
    }

    public L1EquipmentSlot getEquipSlot() {
        return this._equipSlot;
    }

    public static L1PcInstance load(String charName) {
        L1PcInstance result = null;
        try {
            result = CharacterTable.getInstance().loadCharacter(charName);
            if (result != null)
                MJLevelBonus.loadCharacterBonus(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void save() {
        try {
            if (isGhost())
                return;
            if (this.noPlayerCK || this.noPlayerRobot || this.noPlayerck2)
                return;
            CharacterTable.getInstance().storeCharacter(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {}
    }

    public void saveInventory() {
        if (this.noPlayerCK || this.noPlayerRobot || this.noPlayerck2)
            return;
        for (L1ItemInstance item : getInventory().getItems()) {
            if (item != null)
                getInventory().saveItem(item, (item.getRecordingColumns() != 0) ? 4096 : 0);
        }
    }

    public void setRegenState(int state) {
        if (this._mpRegen != null)
            this._mpRegen.setState(state);
        if (this._hpRegen != null)
            this._hpRegen.setState(state);
    }

    public int getMaxWeight() {
        try {
            this._maxweight = CalcStat.getMaxWeight(getAbility().getTotalStr(), getAbility().getTotalCon());
            this._maxweight += getWeightReduction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this._maxweight;
    }

    public boolean isFastMovable() {
        return (hasSkillEffect(52) || hasSkillEffect(101));
    }

    public boolean isBlood_lust() {
        return hasSkillEffect(186);
    }

    public int getBraveSpeed() {
        if (hasSkillEffect(20079))
            return isPassive(MJPassiveID.DARK_HORSE.toInt()) ? 3 : 4;
        if (hasSkillEffect(101)) {
            if (isPassive(MJPassiveID.MOVING_ACCELERATION_PASS.toInt()))
                return 3;
            return 4;
        }
        if (hasSkillEffect(52)) {
            if (isPassive(MJPassiveID.HOLY_WALK_EVOLUTION.toInt()))
                return 3;
            return 4;
        }
        return super.getBraveSpeed();
    }

    public boolean isBrave() {
        return hasSkillEffect(1000);
    }

    public boolean isElfBraveMagicShort() {
        return (hasSkillEffect(155) || hasSkillEffect(179));
    }

    public boolean isElfBraveMagicLong() {
        return (hasSkillEffect(178) || hasSkillEffect(177));
    }

    public boolean isDragonPearl() {
        return (hasSkillEffect(22017) || getPearl() == 1);
    }

    public boolean isElfBrave() {
        return hasSkillEffect(1016);
    }

    public boolean isFruit() {
        return hasSkillEffect(20079);
    }

    public boolean isFourgear() {
        return this._fourgear;
    }

    public void setFourgear(boolean onoff) {
        this._fourgear = onoff;
    }

    public int getPearl() {
        return this._pearl;
    }

    public void setPearl(int i) {
        this._pearl = i;
    }

    public boolean isInvisDelay() {
        return (this.invisDelayCounter > 0);
    }

    public void addInvisDelayCounter(int counter) {
        synchronized (this._invisTimerMonitor) {
            this.invisDelayCounter += counter;
        }
    }

    public void beginInvisTimer() {
        long DELAY_INVIS = 3000L;
        addInvisDelayCounter(1);
        GeneralThreadPool.getInstance().schedule((Runnable)new L1PcInvisDelay(getId()), 3000L);
    }

    public void add_exp(long exp) {
        if (Config.Login.StandbyServer)
            return;
        add_exp_for_ready(exp);
    }

    public void add_exp_for_ready(long exp) {
        synchronized (this.m_add_exp_sync) {
            long current_exp = get_exp() + exp;
            long max_exp = ExpTable.get_max_exp();
            if (current_exp >= max_exp)
                current_exp = max_exp;
            set_exp(current_exp);
        }
    }

    public synchronized void addContribution(int contribution) {
        this._contribution += contribution;
    }

    public void beginExpMonitor() {
        if (!attribute().has(expMonitorKey) || attribute().get(expMonitorKey).get() == null) {
            L1PcExpMonitor monitor = new L1PcExpMonitor(this, getId());
            attribute().getNotExistsNew(expMonitorKey).set(monitor);
            this._expMonitorFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)monitor, 0L, 500L);
        }
    }

    private void levelUp(int gap) {
        Random random = new Random();
        boolean isTeleport = false;
        resetLevel();
        int oldHighLevel = getHighLevel();
        setHighLevel(getLevel());
        SC_REST_EXP_INFO_NOTI.send(this);
        MJRankUserLoader.getInstance().onUser(this);
        String BloodName = getClanname();
        if ((getLevel() >= Config.ServerAdSetting.NEWPLAYERLEVELPURGE && BloodName.equalsIgnoreCase(Config.ServerAdSetting.NEWCLANNAME)) || BloodName.equalsIgnoreCase("))
        try {
            L1Clan clan = L1World.getInstance().findClan(");
                    L1PcInstance[] clanMember = clan.getOnlineClanMember();
            String player_name = getName();
            String clan_name = getClanname();
            for (int j = 0; j < clanMember.length; j++)
                clanMember[j].sendPackets((ServerBasePacket)new S_ServerMessage(178, player_name, clan_name));
            ClearPlayerClanData(clan);
            clan.removeClanMember(player_name);
            start_teleport(getX(), getY(), getMapId(), getHeading(), 18339, false, false);
            isTeleport = true;
            save();
            saveInventory();
        } catch (Exception exception) {}
        if (getLevel() > 90 && getTitle().contains(Config.Message.GameServerName)) {
            setTitle("");
            sendPackets((ServerBasePacket)new S_CharTitle(getId(), ""));
            broadcastPacket((ServerBasePacket)new S_CharTitle(getId(), ""));
        }
        for (int i = 0; i < gap; i++) {
            int minmp = CalcStat.MinincreaseMp(getType(), getAbility().getWis());
            int maxmp = CalcStat.MaxincreaseMp(getType(), getAbility().getWis());
            short randomHp = (short)(CalcStat.PureHp(getType(), getAbility().getCon()) + random.nextInt(2));
            int randomMp = (int)(Math.random() * (maxmp - minmp) + minmp);
            if (minmp == 0)
                randomMp = random.nextInt(maxmp + 1);
            addBaseMaxHp(randomHp);
            addBaseMaxMp(randomMp);
        }
        setCurrentHp(getMaxHp());
        setCurrentMp(getMaxMp());
        resetBaseHitup();
        resetBaseDmgup();
        resetBaseAc();
        resetBaseMr();
        if (getLevel() > oldHighLevel && getReturnStat() == 0L) {
            LevelupBookmark.getInstance().on_level(this);
            MJObjectEventProvider.provider().pcEventFactory().fireLevelChanged(this);
        }
        try {
            save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        L1Quest quest = getQuest();
        int lv = getLevel();
        for (int _lv = 1; _lv <= lv; _lv++) {
            CharactersGiftItemTable.Item _levelItem = null;
            CharactersGiftItemTable.Item[] _levelItems = CharactersGiftItemTable.getInstance().getItems(_lv);
            if (_levelItems != null && _levelItems.length > 0) {
                int level_quest_step = quest.get_step(_lv);
                if (level_quest_step != 255) {
                    for (int j = 0; j < _levelItems.length; j++) {
                        _levelItem = _levelItems[j];
                        if (_levelItem != null)
                            if (_levelItem.getType() == getType())
                                createNewItem(this, _levelItem.getItemId(), _levelItem.getCount(), _levelItem.getEnchant(), _levelItem.getAttrLevel(), _levelItem.getBless());
                    }
                    sendPackets((ServerBasePacket)new S_ChatPacket("Level(" + _lv + ")));
                            getQuest().set_end(_lv);
                }
            }
        }
        int lv59_step = quest.get_step(62);
        if (getLevel() == 60 && lv59_step != 255) {
            sendPackets((ServerBasePacket)new S_PacketBox(84, "Lv.60 ));
                    sendPackets((ServerBasePacket)new S_NewCreateItem(126, 0));
        }
        int lv76_step = quest.get_step(60);
        if (getLevel() == 60 && lv76_step != 255)
            sendPackets((ServerBasePacket)new S_PacketBox(84, "Lv.60 ));
        int lv81_step = quest.get_step(61);
        if (getLevel() == 60 && lv81_step != 255)
            sendPackets((ServerBasePacket)new S_PacketBox(84, "Lv.60 ));
                    sendBonusStats();
        StartCharBoho();
        if (getLevel() > 1 && getLevel() <= Config.ServerAdSetting.LineageBuff) {
            sendPackets((ServerBasePacket)S_InventoryIcon.iconNewUnLimit(9904, 4126, true));
        } else {
            sendPackets((ServerBasePacket)S_InventoryIcon.icoEnd(9904));
        }
        if (getLevel() >= Config.ServerAdSetting.NewCha1 && ((getMapId() >= 1 && getMapId() <= 2) || (getMapId() >= 10 && getMapId() <= 12) || getMapId() == 785 || getMapId() == 1911 || getMapId() == 1912)) {
            int[] loc = L1TownLocation.getGetBackLoc(7);
            start_teleport(loc[0], loc[1], loc[2], getHeading(), 18339, true, false);
            isTeleport = true;
        }
        if (getLevel() >= 75)
            if (getMapId() >= 25 && getMapId() <= 28) {
                int[] loc = L1TownLocation.getGetBackLoc(7);
                start_teleport(loc[0], loc[1], loc[2], getHeading(), 18339, true, false);
                isTeleport = true;
            } else if (getMapId() == 778 || getMapId() == 779) {
                start_teleport(32608, 33178, 4, 5, 18339, true, false);
                isTeleport = true;
            } else if (getMapId() == 2010) {
                int[] loc = L1TownLocation.getGetBackLoc(2);
                start_teleport(loc[0], loc[1], loc[2], getHeading(), 18339, true, false);
                isTeleport = true;
            }
        if (getLevel() >= Config.CharSettings.MaxLevel) {
            sendPackets(Config.Message.MAX_LEVEL_MESSAGE);
            sendPackets((ServerBasePacket)new S_PacketBox(84, Config.Message.MAX_LEVEL_MESSAGE));
        }
        int lvlBonus = getCharLevelBonus();
        int lvlBonusOrgn = lvlBonus;
        if (getLevel() >= Config.ServerAdSetting.Bonus_Lvl1 && (lvlBonus & 0x1) == 0) {
            getInventory().storeItem(Config.ServerAdSetting.Bonus_Item1, Config.ServerAdSetting.Bonus_Item_Count1, true);
            sendPackets(Config.Message.Bonus_Message1);
            sendPackets((ServerBasePacket)new S_PacketBox(84, Config.Message.Bonus_Message1));
            lvlBonus |= 0x1;
        }
        if (getLevel() >= Config.ServerAdSetting.Bonus_Lvl2 && (lvlBonus & 0x2) == 0) {
            getInventory().storeItem(Config.ServerAdSetting.Bonus_Item2, Config.ServerAdSetting.Bonus_Item_Count2, true);
            sendPackets(Config.Message.Bonus_Message2);
            sendPackets((ServerBasePacket)new S_PacketBox(84, Config.Message.Bonus_Message2));
            lvlBonus |= 0x2;
        }
        if (getLevel() >= Config.ServerAdSetting.Bonus_Lvl3 && (lvlBonus & 0x4) == 0) {
            getInventory().storeItem(Config.ServerAdSetting.Bonus_Item3, Config.ServerAdSetting.Bonus_Item_Count3, true);
            sendPackets(Config.Message.Bonus_Message3);
            sendPackets((ServerBasePacket)new S_PacketBox(84, Config.Message.Bonus_Message3));
            lvlBonus |= 0x4;
        }
        if (getLevel() >= Config.ServerAdSetting.Bonus_Lvl4 && (lvlBonus & 0x8) == 0) {
            getInventory().storeItem(Config.ServerAdSetting.Bonus_Item4, Config.ServerAdSetting.Bonus_Item_Count4, true);
            sendPackets(Config.Message.Bonus_Message4);
            sendPackets((ServerBasePacket)new S_PacketBox(84, Config.Message.Bonus_Message4));
            lvlBonus |= 0x8;
        }
        if (getLevel() >= Config.ServerAdSetting.Bonus_Lvl5 && (lvlBonus & 0x10) == 0) {
            getInventory().storeItem(Config.ServerAdSetting.Bonus_Item5, Config.ServerAdSetting.Bonus_Item_Count5, true);
            sendPackets(Config.Message.Bonus_Message5);
            sendPackets((ServerBasePacket)new S_PacketBox(84, Config.Message.Bonus_Message5));
            lvlBonus |= 0x10;
        }
        if (getLevel() == 90 && (lvlBonus & 0x20) == 0) {
            sendPackets("90);
                    isTeleport = true;
            start_teleport(33437, 32813, 4, getHeading(), 18339, false, false);
            GeneralThreadPool.getInstance().schedule(new Runnable() {
                public void run() {
                    DungeonTimeProgress<?> progress = L1PcInstance.this._dtInfo.remove_dungeon_progress(2);
                    if (progress != null)
                        DungeonTimeProgressLoader.delete(L1PcInstance.this, 2, true);
                    progress = L1PcInstance.this._dtInfo.remove_dungeon_progress(14);
                    if (progress != null)
                        DungeonTimeProgressLoader.delete(L1PcInstance.this, 14, true);
                }
            }500L);
            lvlBonus |= 0x20;
        }
        if (lvlBonus != lvlBonusOrgn) {
            setCharLevelBonus(lvlBonus);
            MJLevelBonus.storeCharacterBonus(this);
        }
        if (getMapId() != 5490 && !isFishing() && getLevel() >= Config.ServerAdSetting.PcReload && !isTeleport) {
            start_teleport(getX(), getY(), getMapId(), getHeading(), 18339, false, false);
            GeneralThreadPool.getInstance().schedule(new Runnable() {
                public void run() {
                    L1PcInstance.this.sendPackets((ServerBasePacket)new S_OwnCharStatus(L1PcInstance.this));
                }
            },  500L);
        } else {
            sendPackets((ServerBasePacket)new S_OwnCharStatus(this));
        }
        TeleporterActionListener listener = ActionListenerLoader.getInstance().findListener(ListenerFinderTable.getInstance().getListenerInfo(getMapId()));
        if (listener != null && listener.get_limit_level() > 0 && getLevel() > listener.get_limit_level()) {
            int[] loc = Getback.GetBack_Location(this, false);
            start_teleport(loc[0], loc[1], loc[2], getHeading(), 18339, true);
        }
    }

    private void levelDown(int gap) {
        Random random = new Random();
        resetLevel();
        SC_REST_EXP_INFO_NOTI.send(this);
        for (int i = 0; i > gap; i--) {
            int minmp = CalcStat.MinincreaseMp(getType(), getAbility().getWis());
            int maxmp = CalcStat.MaxincreaseMp(getType(), getAbility().getWis());
            short randomHp = (short)(CalcStat.PureHp(getType(), getAbility().getCon()) + random.nextInt(2));
            int randomMp = (int)(Math.random() * (maxmp - minmp) + minmp);
            if (minmp == 0)
                randomMp = random.nextInt(maxmp + 1);
            addBaseMaxHp((short)-randomHp);
            addBaseMaxMp((short)-randomMp);
        }
        resetBaseHitup();
        resetBaseDmgup();
        resetBaseAc();
        resetBaseMr();
        if (!isGm() && Config.Login.LevelDownRange != 0) {
            if (getHighLevel() - getLevel() == Config.Login.LevelDownRange - 1)
                sendPackets(");
            if (!isGm() && getHighLevel() - getLevel() >= Config.Login.LevelDownRange) {
                sendPackets((ServerBasePacket)new S_ServerMessage(64));
                sendPackets((ServerBasePacket)new S_Disconnect());
                _log.info(String.format("%s, new Object[] { getName() }));
            }
        }
        try {
            save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StartCharBoho();
        sendPackets((ServerBasePacket)new S_OwnCharStatus(this));
        start_teleport(getX(), getY(), getMapId(), getHeading(), 18339, false, false);
    }

    public void beginGameTimeCarrier() {
        (new GameTimeCarrier(this)).start();
    }

    public boolean isGhost() {
        return this._ghost;
    }

    public void setGhost(boolean flag) {
        this._ghost = flag;
    }

    public boolean isGhostCanTalk() {
        return this._ghostCanTalk;
    }

    public void setGhostCanTalk(boolean flag) {
        this._ghostCanTalk = flag;
    }

    public boolean isReserveGhost() {
        return this._isReserveGhost;
    }

    public void setReserveGhost(boolean flag) {
        this._isReserveGhost = flag;
    }

    public void beginGhost() {
        if (!isGhost()) {
            setGhost(true);
            this._ghostSaveLocX = getX();
            this._ghostSaveLocY = getY();
            this._ghostSaveMapId = getMapId();
            GhostController.getInstance().addMember(this);
        }
    }

    public void beginGhost(int locx, int locy, short mapid, boolean canTalk) {
        beginGhost(locx, locy, mapid, canTalk, 0);
    }

    public void beginGhost(int locx, int locy, short mapid, boolean canTalk, int sec) {
        if (isGhost())
            return;
        this._ghostSaveLocX = getX();
        this._ghostSaveLocY = getY();
        this._ghostSaveMapId = getMapId();
        setGhost(true);
        setGhostCanTalk(canTalk);
        setReserveGhost(false);
        start_teleport(locx, locy, mapid, 5, 18339, true, false);
        if (sec > 0)
            this._ghostFuture = GeneralThreadPool.getInstance().schedule((Runnable)new L1PcGhostMonitor(getId()), (sec * 1000));
    }

    public void makeReadyEndGhost() {
        setGhost(false);
        setReserveGhost(true);
        start_teleport(this._ghostSaveLocX, this._ghostSaveLocY, this._ghostSaveMapId, 5, 18339, true, false);
        GhostController.getInstance().removeMember(this);
    }

    public void DeathMatchEndGhost() {
        setReserveGhost(true);
        start_teleport(32614, 32735, 4, 5, 18339, true, false);
    }

    public void endGhost() {
        setGhost(false);
        setGhostCanTalk(true);
        setReserveGhost(false);
    }

    public void beginHell(boolean isFirst) {
        if (getMapId() != 666) {
            int locx = 32701;
            int locy = 32777;
            int mapid = 666;
            start_teleport(locx, locy, mapid, 5, 18339, false, false);
        }
        if (isFirst) {
            if (get_PKcount() <= 10) {
                setHellTime(180);
            } else {
                setHellTime(300 * (get_PKcount() - 100) + 300);
            }
            sendPackets((ServerBasePacket)new S_BlueMessage(552, String.valueOf(get_PKcount()), String.valueOf(getHellTime() / 60)));
        } else {
            sendPackets((ServerBasePacket)new S_BlueMessage(637, String.valueOf(getHellTime())));
        }
        if (this._hellFuture == null)
            this._hellFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)new L1PcHellMonitor(getId()), 0L, 1000L);
    }

    public void endHell() {
        if (this._hellFuture != null) {
            this._hellFuture.cancel(false);
            this._hellFuture = null;
        }
        int[] loc = L1TownLocation.getGetBackLoc(4);
        start_teleport(loc[0], loc[1], loc[2], 5, 18339, true, false);
        try {
            save();
        } catch (Exception exception) {}
    }

    public void setPoisonEffect(int effectId) {
        sendPackets((ServerBasePacket)new S_Poison(getId(), effectId));
        if (!isGmInvis() && !isGhost() && !isInvisble())
            broadcastPacket((ServerBasePacket)new S_Poison(getId(), effectId));
    }

    public void healHp(int pt) {
        super.healHp(pt);
        sendPackets((ServerBasePacket)new S_HPUpdate(this));
    }

    public void addDg(int i) {
        super.addDg(i);
        sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
    }

    public int getKarma() {
        return this._karma.get();
    }

    public void setKarma(int i) {
        this._karma.set(i);
    }

    public void addKarma(int i) {
        synchronized (this._karma) {
            this._karma.add(i);
        }
    }

    public int getKarmaLevel() {
        return this._karma.getLevel();
    }

    public int getKarmaPercent() {
        return this._karma.getPercent();
    }

    public Timestamp getLastPk() {
        return this._lastPk;
    }

    public void setLastPk(Timestamp time) {
        this._lastPk = time;
    }

    public void setLastPk() {
        this._lastPk = new Timestamp(System.currentTimeMillis());
    }

    public boolean isWanted() {
        if (this._lastPk == null)
            return false;
        if (System.currentTimeMillis() - this._lastPk.getTime() > 86400000L) {
            setLastPk((Timestamp)null);
            return false;
        }
        return true;
    }

    public Timestamp getDeleteTime() {
        return this._deleteTime;
    }

    public void setDeleteTime(Timestamp time) {
        this._deleteTime = time;
    }

    public Timestamp getLastLoginTime() {
        return this._lastLoginTime;
    }

    public void setLastLoginTime(Timestamp time) {
        this._lastLoginTime = time;
    }

    public void setLastLogoutTime(Timestamp time) {
        this._lastLogoutTime = time;
    }

    public Timestamp getLastLogoutTime() {
        return this._lastLogoutTime;
    }

    public int getMagicLevel() {
        return getClassFeature().getMagicLevel(getLevel());
    }

    public int getWeightReduction() {
        return this._weightReduction;
    }

    public void addWeightReduction(int i) {
        this._weightReduction += i;
        if (getAI() == null)
            sendPackets((ServerBasePacket)new S_Weight(this));
    }

    public int getDragonPearItemEquipped() {
        return this._dragonpearItemEquipped;
    }

    public void addDragonPearItemEquipped(int i) {
        this._dragonpearItemEquipped += i;
    }

    public void removeDragonPearSkillEffect() {
        if (hasSkillEffect(22017))
            removeSkillEffect(22017);
    }

    public int getHasteItemEquipped() {
        return this._hasteItemEquipped;
    }

    public void addHasteItemEquipped(int i) {
        this._hasteItemEquipped += i;
    }

    public void removeHasteSkillEffect() {
        if (hasSkillEffect(29))
            removeSkillEffect(29);
        if (hasSkillEffect(43))
            removeSkillEffect(43);
        if (hasSkillEffect(707114))
            removeSkillEffect(707114);
        if (hasSkillEffect(54))
            removeSkillEffect(54);
        if (hasSkillEffect(1001))
            removeSkillEffect(1001);
    }

    public Timestamp getTamTime() {
        return this._tamTime;
    }

    public void setTamTime(Timestamp time) {
        this._tamTime = time;
    }

    public int getTamReserve() {
        return this._tamreserve;
    }

    public void setTamReserve(int i) {
        this._tamreserve = i;
    }

    public boolean isReturnStatus() {
        return this.returnStatus;
    }

    public void setReturnStatus(boolean returnStatus) {
        this.returnStatus = returnStatus;
    }

    public boolean isReturnStatus_Start() {
        return this.returnStatus_Start;
    }

    public void setReturnStatus_Start(boolean returnStatus_Start) {
        this.returnStatus_Start = returnStatus_Start;
    }

    public boolean isReturnStatus_Levelup() {
        return this.returnStatus_Levelup;
    }

    public void setReturnStatus_Levelup(boolean returnStatus_Levelup) {
        this.returnStatus_Levelup = returnStatus_Levelup;
    }

    public void resetBaseDmgup() {
        int newBaseDmgup = 0;
        int newBaseBowDmgup = 0;
        int newBaseStatDmgup = CalcStat.calcDmgup(getAbility().getTotalStr()) + CalcStat.calcPureMeleeDmgup(getAbility().getStr());
        int newBaseStatBowDmgup = CalcStat.calcBowDmgup(getAbility().getTotalDex()) + CalcStat.calcPureMissileDmgup(getAbility().getDex());
        if (isKnight() || isDragonknight() || isDarkelf()) {
            newBaseDmgup = getLevel() / 10;
            newBaseBowDmgup = 0;
        } else if (isElf()) {
            newBaseDmgup = 0;
            newBaseBowDmgup = getLevel() / 10;
        }
        addDmgup(newBaseDmgup + newBaseStatDmgup - this._baseDmgup);
        addBowDmgup(newBaseBowDmgup + newBaseStatBowDmgup - this._baseBowDmgup);
        this._baseDmgup = newBaseDmgup + newBaseStatDmgup;
        this._baseBowDmgup = newBaseBowDmgup + newBaseStatBowDmgup;
    }

    public void resetBaseHitup() {
        int newBaseHitup = 0;
        int newBaseBowHitup = 0;
        int newBaseStatHitup = CalcStat.calcHitup(getAbility().getTotalStr()) + CalcStat.calcPureMeleeHitup(getAbility().getStr());
        int newBaseStatBowHitup = CalcStat.calcBowHitup(getAbility().getTotalDex()) + CalcStat.calcPureMissileHitup(getAbility().getDex());
        newBaseHitup = Math.max(0, getLevel() - 50);
        newBaseBowHitup = Math.max(0, getLevel() - 50);
        addHitup(newBaseHitup + newBaseStatHitup - this._baseHitup);
        addBowHitup(newBaseBowHitup + newBaseStatBowHitup - this._baseBowHitup);
        this._baseHitup = newBaseHitup + newBaseStatHitup;
        this._baseBowHitup = newBaseBowHitup + newBaseStatBowHitup;
    }

    public void resetOriginalMagicHit() {
        this._originalMagicHit = CalcStat.calcMagicHitUp(getAbility().getTotalInt());
        this._baseMagicDmg = CalcStat.calcMagicDmg(getAbility().getTotalInt());
    }

    public void resetBaseAc() {
        int newAc = 10 + CalcStat.calcAc(getAbility().getTotalDex());
        if (this._type == 3) {
            newAc -= getLevel() / 8;
        } else if (this._type == 4) {
            newAc -= getLevel() / 6;
        } else {
            newAc -= getLevel() / 7;
        }
        this.ac.addAc(newAc - this._baseAc);
        this._baseAc = newAc;
        sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
    }

    public void resetBaseMr() {
        this.resistance.setBaseMr(CalcStat.calcStatMr(this._type, getAbility().getTotalWis()));
        sendPackets((ServerBasePacket)new S_SPMR(this));
    }

    public void resetLevel() {
        setLevel(ExpTable.getLevelByExp(get_exp()));
        if (this._hpRegen != null)
            this._hpRegen.updateLevel();
    }

    public void refresh() {
        CheckChangeExp();
        resetLevel();
        resetBaseHitup();
        resetBaseDmgup();
        resetBaseMr();
        resetBaseAc();
    }

    public void checkChatInterval() {
        long nowChatTimeInMillis = System.currentTimeMillis();
        if (this._chatCount == 0) {
            this._chatCount = (byte)(this._chatCount + 1);
            this._oldChatTimeInMillis = nowChatTimeInMillis;
            return;
        }
        long chatInterval = nowChatTimeInMillis - this._oldChatTimeInMillis;
        if (chatInterval > 2000L) {
            this._chatCount = 0;
            this._oldChatTimeInMillis = 0L;
        } else {
            if (this._chatCount >= 3) {
                setSkillEffect(1005, 120000L);
                sendPackets((ServerBasePacket)new S_SkillIconGFX(36, 120));
                sendPackets((ServerBasePacket)new S_ServerMessage(153));
                this._chatCount = 0;
                this._oldChatTimeInMillis = 0L;
            }
            this._chatCount = (byte)(this._chatCount + 1);
        }
    }

    public void CheckChangeExp() {
        int level = ExpTable.getLevelByExp(get_exp());
        int char_level = CharacterTable.getInstance().PcLevelInDB(getId());
        if (char_level == 0)
            return;
        int gap = level - char_level;
        if (gap == 0) {
            sendPackets((ServerBasePacket)new S_OwnCharStatus(this));
            int percent = ExpTable.getExpPercentage(char_level, get_exp());
            if (char_level >= 60 && char_level <= 64) {
                if (percent >= 10)
                    removeSkillEffect(80007);
            } else if (char_level >= 65 && percent >= 5) {
                removeSkillEffect(80007);
            }
            return;
        }
        if (gap > 0) {
            levelUp(gap);
            if (getLevel() >= 60) {
                setSkillEffect(80007, 10800000L);
                sendPackets((ServerBasePacket)new S_PacketBox(10800, true, true), true);
            }
        } else if (gap < 0) {
            levelDown(gap);
            removeSkillEffect(80007);
        }
    }

    public void checkStatus() throws Exception {
        int remain = remainBonusStats();
        if (remain < 0 && !isGm()) {
            if (getNetConnection() != null)
                getNetConnection().kick();
            throw new Exception(String.format("%s , new Object[] { getName() }));
        }
    }

    public long TamTime() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Timestamp tamtime = null;
        long time = 0L;
        long sysTime = System.currentTimeMillis();
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT `TamEndTime` FROM `characters` WHERE account_name = ? ORDER BY `TamEndTime` ASC");
            pstm.setString(1, getAccountName());
            rs = pstm.executeQuery();
            while (rs.next()) {
                tamtime = rs.getTimestamp("TamEndTime");
                if (tamtime != null && sysTime < tamtime.getTime()) {
                    time = tamtime.getTime() - sysTime;
                    break;
                }
            }
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            return time;
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public int getRankLevel() {
        return MJRankUserLoader.getInstance().getRankLevel(this);
    }

    public int getClassRankLevel() {
        return MJRankUserLoader.getInstance().getClassRankLevel(this);
    }

    public int tamcount() {
        Connection con = null;
        Connection con2 = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        PreparedStatement pstm2 = null;
        Timestamp tamtime = null;
        int count = 0;
        long sysTime = System.currentTimeMillis();
        int char_objid = 0;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `characters` WHERE account_name = ?");
            pstm.setString(1, getAccountName());
            rs = pstm.executeQuery();
            while (rs.next()) {
                tamtime = rs.getTimestamp("TamEndTime");
                char_objid = rs.getInt("objid");
                if (tamtime != null) {
                    if (sysTime / 1000L + 2L <= tamtime.getTime() / 1000L) {
                        count++;
                        continue;
                    }
                    if (Tam_wait_count(char_objid) != 0) {
                        int day = Nexttam(char_objid);
                        if (day != 0) {
                            Timestamp deleteTime = null;
                            deleteTime = new Timestamp(sysTime + 86400000L * day + 10000L);
                            if (getId() == char_objid)
                                setTamTime(deleteTime);
                            try {
                                con2 = L1DatabaseFactory.getInstance().getConnection();
                                pstm2 = con2.prepareStatement("UPDATE `characters` SET TamEndTime=? WHERE account_name = ? AND objid = ?");
                                pstm2.setTimestamp(1, deleteTime);
                                pstm2.setString(2, getAccountName());
                                pstm2.setInt(3, char_objid);
                                pstm2.executeUpdate();
                                tamdel(char_objid);
                                count++;
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                SQLUtil.close(pstm2, con2);
                            }
                        }
                    }
                }
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return count;
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm2);
            SQLUtil.close(con2);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public static void tamdel(int objectId) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("delete from Tam where objid = ? order by id asc limit 1");
            pstm.setInt(1, objectId);
            pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public int Nexttam(int objectId) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int day = 0;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT day FROM `tam` WHERE objid = ? order by id asc limit 1");
            pstm.setInt(1, objectId);
            rs = pstm.executeQuery();
            while (rs.next())
                day = rs.getInt("Day");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return day;
    }

    public int Tam_wait_count(int charid) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `tam` WHERE objid = ?");
            pstm.setInt(1, charid);
            rs = pstm.executeQuery();
            while (rs.next())
                count = getId();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return count;
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void cancelAbsoluteBarrier() {
        if (hasSkillEffect(78))
            removeSkillEffect(78);
    }

    public int get_PKcount() {
        return this._PKcount;
    }

    public void set_PKcount(int i) {
        this._PKcount = i;
    }

    public int getClanid() {
        return this._clanid;
    }

    public void setClanid(int i) {
        this._clanid = i;
    }

    public String getClanname() {
        return this.clanname;
    }

    public void setClanname(String s) {
        String previous = this.clanname;
        this.clanname = s;
        if (previous != s)
            MJObjectEventProvider.provider().pcEventFactory().firePledgeChanged(this, s, previous);
    }

    public int getRedKnightClanId() {
        return this._redKnightClanId;
    }

    public void setRedKnightClanId(int i) {
        this._redKnightClanId = i;
    }

    public int getRedKnightDamage() {
        return this._redKnightDamage;
    }

    public void setRedKnightDamage(int i) {
        this._redKnightDamage = i;
    }

    public void addRedKnightDamage(int i) {
        this._redKnightDamage += i;
    }

    public int getRedKnightKill() {
        return this._redKnightKill;
    }

    public void setRedKnightKill(int i) {
        this._redKnightKill = i;
    }

    public void addRedKnightKill(int i) {
        this._redKnightKill += i;
    }

    public String getSealingPW() {
        return this._sealingPW;
    }

    public void setSealingPW(String s) {
        this._sealingPW = s;
    }

    public void setSealScrollTime(int sealScrollTime) {
        this._sealScrollTime = sealScrollTime;
    }

    public int getSealScrollTime() {
        return this._sealScrollTime;
    }

    public void setSealScrollCount(int sealScrollCount) {
        this._sealScrollCount = sealScrollCount;
    }

    public int getSealScrollCount() {
        return this._sealScrollCount;
    }

    public L1Clan getClan() {
        return L1World.getInstance().getClan(getClanid());
    }

    public int getClanRank() {
        return this._clanRank;
    }

    public void setClanRank(int i) {
        this._clanRank = i;
    }

    public byte get_sex() {
        return this._sex;
    }

    public void set_sex(int i) {
        this._sex = (byte)i;
    }

    public int getAge() {
        return this._age;
    }

    public void setAge(int i) {
        this._age = i;
    }

    public boolean isGm() {
        return this._gm;
    }

    public void setGm(boolean flag) {
        this._gm = flag;
    }

    public boolean isMonitor() {
        return this._monitor;
    }

    public void setMonitor(boolean flag) {
        this._monitor = flag;
    }

    public int getDamageReductionByArmor() {
        if (this != null && isPassive(MJPassiveID.INFINITI_ARMOR.toInt())) {
            int Level = getLevel();
            if (Level < 86)
                Level = 86;
            int point = (Level - 86) / 2 * 2 + 5;
            if (point >= 15)
                point = 15;
            return this._damageReductionByArmor + point;
        }
        return this._damageReductionByArmor;
    }

    public void addDamageReductionByArmor(int i) {
        this._damageReductionByArmor += i;
    }

    public int getDamageReductionIgnore() {
        return this._damageReductionIgnore;
    }

    public void addDamageReductionIgnore(int i) {
        this._damageReductionIgnore += i;
    }

    public int getDamageReduction() {
        return this._DamageReduction;
    }

    public void addDamageReduction(int i) {
        this._DamageReduction += i;
    }

    public int get_pvp_defense() {
        int pvpreduc = 0;
        if (isCrown()) {
            pvpreduc += (getLevel() >= 60) ? ((getLevel() - 56) / 4) : 0;
        } else if (isKnight()) {
            pvpreduc += (getLevel() >= 60) ? ((getLevel() - 58) / 2) : 0;
        } else if (isElf()) {
            pvpreduc += (getLevel() >= 60) ? ((getLevel() - 56) / 4) : 0;
        } else if (isWizard()) {
            pvpreduc += (getLevel() >= 60) ? ((getLevel() - 56) / 4) : 0;
        } else if (isDarkelf()) {
            pvpreduc += (getLevel() >= 60) ? ((getLevel() - 57) / 3) : 0;
        } else if (isBlackwizard()) {
            pvpreduc += (getLevel() >= 60) ? ((getLevel() - 57) / 3) : 0;
        } else if (isDragonknight()) {
            pvpreduc += (getLevel() >= 60) ? ((getLevel() - 57) / 3) : 0;
        } else if (is()) {
            pvpreduc += (getLevel() >= 60) ? ((getLevel() - 57) / 3) : 0;
        } else if (isFencer()) {
            pvpreduc += (getLevel() >= 60) ? ((getLevel() - 58) / 2) : 0;
        } else if (isLancer()) {
            pvpreduc += (getLevel() >= 60) ? ((getLevel() - 58) / 2) : 0;
        }
        if (pvpreduc > 0)
            return this.pvp_defense + pvpreduc;
        return this.pvp_defense;
    }

    public void set_pvp_defense(int i) {
        this.pvp_defense = CommonUtil.get_current(i, 0, 30);
    }

    public int getBowDmgRate() {
        return this._bowDmgRate;
    }

    public void addBowDmgRate(int i) {
        this._bowDmgRate += i;
    }

    public int getDmgRate() {
        return this._DmgRate;
    }

    public void addDmgRate(int i) {
        this._DmgRate += i;
    }

    public int getBowHitRate() {
        return this._bowHitRate;
    }

    public void addBowHitRate(int i) {
        this._bowHitRate += i;
    }

    public int getHitRate() {
        return this._HitRate;
    }

    public void addHitRate(int i) {
        this._HitRate += i;
    }

    public int getDmgupByArmor() {
        return this._DmgupByArmor;
    }

    public void addDmgupByArmor(int i) {
        this._DmgupByArmor += i;
    }

    public int getBowDmgupByArmor() {
        return this._bowDmgupByArmor;
    }

    public void addBowDmgupByArmor(int i) {
        this._bowDmgupByArmor += i;
    }

    private void setGresValid(boolean valid) {
        this._gresValid = valid;
    }

    public boolean isGresValid() {
        return this._gresValid;
    }

    public long getFishingTime() {
        return this._fishingTime;
    }

    public void setFishingTime(long i) {
        this._fishingTime = i;
    }

    public boolean isFishing() {
        return this._isFishing;
    }

    public boolean isFishingReady() {
        return this._isFishingReady;
    }

    public void setFishing(boolean flag) {
        this._isFishing = flag;
    }

    public void setFishingReady(boolean flag) {
        this._isFishingReady = flag;
    }

    public int getCookingId() {
        return this._cookingId;
    }

    public void setCookingId(int i) {
        this._cookingId = i;
    }

    public int getDessertId() {
        return this._dessertId;
    }

    public void setDessertId(int i) {
        this._dessertId = i;
    }

    public int getCashStep() {
        return this.CashStep;
    }

    public void setCashStep(int cashStep) {
        this.CashStep = cashStep;
    }

    public long getQuizTime() {
        return this._quiztime;
    }

    public void setQuizTime(long l) {
        this._quiztime = l;
    }

    public long getQuizTime2() {
        return this._quiztime2;
    }

    public void setQuizTime2(long l) {
        this._quiztime2 = l;
    }

    public long getQuizTime3() {
        return this._quiztime3;
    }

    public void setQuizTime3(long l) {
        this._quiztime3 = l;
    }

    public int getTeleportTime() {
        return this.teleportTime;
    }

    public void setTeleportTime(int teleportTime) {
        this.teleportTime = teleportTime;
    }

    public int getTeleportTime2() {
        return this.teleportTime2;
    }

    public void setTeleportTime2(int teleportTime2) {
        this.teleportTime2 = teleportTime2;
    }

    public int getSkillTime2() {
        return this.skillTime2;
    }

    public void setSkillTime2(int skillTime2) {
        this.skillTime2 = skillTime2;
    }

    public int getSkillTime() {
        return this.skillTime;
    }

    public void setSkillTime(int skillTime) {
        this.skillTime = skillTime;
    }

    public int getCurrentSkillCount() {
        return this.currentSkillCount;
    }

    public void setCurrentSkillCount(int currentSkillCount) {
        this.currentSkillCount = currentSkillCount;
    }

    public int getCurrentSkillCount2() {
        return this.currentSkillCount2;
    }

    public void setCurrentSkillCount2(int currentSkillCount2) {
        this.currentSkillCount2 = currentSkillCount2;
    }

    public int getTempCharGfxAtDead() {
        return this._tempCharGfxAtDead;
    }

    public void setTempCharGfxAtDead(int i) {
        this._tempCharGfxAtDead = i;
    }

    public boolean isCanWhisper() {
        return this._isCanWhisper;
    }

    public void setCanWhisper(boolean flag) {
        this._isCanWhisper = flag;
    }

    public boolean isShowTradeChat() {
        return this._isShowTradeChat;
    }

    public void setShowTradeChat(boolean flag) {
        this._isShowTradeChat = flag;
    }

    public boolean isShowWorldChat() {
        return this._isShowWorldChat;
    }

    public void setShowWorldChat(boolean flag) {
        this._isShowWorldChat = flag;
    }

    public int getFightId() {
        return this._fightId;
    }

    public void setFightId(int i) {
        this._fightId = i;
    }

    public void setDeathMatch(boolean i) {
        this.isDeathMatch = i;
    }

    public boolean isDeathMatch() {
        return this.isDeathMatch;
    }

    public boolean isSupporting() {
        return this._isSupporting;
    }

    public void setSupporting(boolean flag) {
        this._isSupporting = flag;
    }

    public int getCallClanId() {
        return this._callClanId;
    }

    public void setCallClanId(int i) {
        this._callClanId = i;
    }

    public int getCallClanHeading() {
        return this._callClanHeading;
    }

    public void setCallClanHeading(int i) {
        this._callClanHeading = i;
    }

    public void set_bapo_level(int bapo_level) {
        this.m_bapo_level = bapo_level;
    }

    public int get_bapo_level() {
        return this.m_bapo_level;
    }

    public int getDeathMatchPiece() {
        return this._deathmatch;
    }

    public void setDeathMatchPiece(int i) {
        this._deathmatch = i;
    }

    public int getPetRacePiece() {
        return this._petrace;
    }

    public void setPetRacePiece(int i) {
        this._petrace = i;
    }

    public int getUltimateBattlePiece() {
        return this._ultimatebattle;
    }

    public void setUltimateBattlePiece(int i) {
        this._ultimatebattle = i;
    }

    public int getPetMatchPiece() {
        return this._petmatch;
    }

    public void setPetMatchPiece(int i) {
        this._petmatch = i;
    }

    public int getGhostHousePiece() {
        return this._ghosthouse;
    }

    public void setGhostHousePiece(int i) {
        this._ghosthouse = i;
    }

    public int getLastEnchantItemid() {
        return this._enchantitemid;
    }

    public int getAccessoryHeal() {
        return this._accessoryHeal;
    }

    public void setAccessoryHeal(int i) {
        this._accessoryHeal = i;
    }

    public void addAccessoryHeal() {
        this._accessoryHeal++;
    }

    public void setLastEnchantItemid(int i, L1ItemInstance item) {
        if (getLastEnchantItemid() == i && i != 0) {
            sendPackets((ServerBasePacket)new S_Disconnect());
            getInventory().removeItem(item, item.getCount());
            return;
        }
        this._enchantitemid = i;
    }

    public boolean isGambling() {
        return this._isGambling;
    }

    public void setGambling(boolean flag) {
        this._isGambling = flag;
    }

    public int getGamblingMoney() {
        return this._gamblingmoney;
    }

    public void setGamblingMoney(int i) {
        this._gamblingmoney = i;
    }

    public boolean isGambling3() {
        return this._isGambling3;
    }

    public void setGambling3(boolean flag) {
        this._isGambling3 = flag;
    }

    public int getGamblingMoney3() {
        return this._gamblingmoney3;
    }

    public void setGamblingMoney3(int i) {
        this._gamblingmoney3 = i;
    }

    public void addCMAList(String name) {
        if (this._cmalist.contains(name))
            return;
        this._cmalist.add(name);
    }

    public void removeCMAList(String name) {
        if (!this._cmalist.contains(name))
            return;
        this._cmalist.remove(name);
    }

    public ArrayList<String> getCMAList() {
        return this._cmalist;
    }

    public int getClanMemberId() {
        return this._clanMemberId;
    }

    public void setClanMemberId(int i) {
        this._clanMemberId = i;
    }

    public String getClanMemberNotes() {
        return this._clanMemberNotes;
    }

    public void setClanMemberNotes(String s) {
        this._clanMemberNotes = s;
    }

    private void drop1(L1Character lastAttacker) {
        if (getMapId() >= 1708 && getMapId() <= 1709 && getInventory().checkEquipped(900022)) {
            L1ItemInstance drop = ItemTable.getInstance().createItem(3000122);
            for (L1ItemInstance item : getInventory().getItems()) {
                if ((((item.getItemId() == 900022) ? 1 : 0) & item.isEquipped()) != 0) {
                    sendPackets((ServerBasePacket)new S_ServerMessage(3802));
                    sendPackets((ServerBasePacket)new S_ServerMessage(158, "$22172"));
                    getInventory().removeItem(item, 1);
                    if (lastAttacker instanceof L1PcInstance) {
                        L1PcInstance pc = (L1PcInstance)lastAttacker;
                        if (DropDelayItemTable.getInstance().isItem(drop.getItemId())) {
                            L1DropDelayItem temp = DropDelayItemTable.getInstance().getItem(drop.getItemId());
                            if (temp != null && temp.getItemId() == drop.getItemId())
                                drop.startItemOwnerTimer(pc);
                        }
                    }
                    L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
                    LoggerInstance.getInstance().addItemAction(Logger.ItemActionType.DeathDrop, this, item, 1);
                    break;
                }
            }
        }
    }

    private void gahodrop(L1Character lastAttacker) {
        if (getInventory().checkItem(4100122)) {
            L1ItemInstance drop = ItemTable.getInstance().createItem(4100120);
            for (L1ItemInstance item : getInventory().getItems()) {
                if (item.getItemId() == 4100122) {
                    sendPackets((ServerBasePacket)new S_ServerMessage(3802));
                    sendPackets((ServerBasePacket)new S_ServerMessage(158, "$27280"));
                    getInventory().removeItem(item, 1);
                    if (lastAttacker instanceof L1PcInstance) {
                        L1PcInstance pc = (L1PcInstance)lastAttacker;
                        if (DropDelayItemTable.getInstance().isItem(drop.getItemId())) {
                            L1DropDelayItem temp = DropDelayItemTable.getInstance().getItem(drop.getItemId());
                            if (temp != null && temp.getItemId() == drop.getItemId())
                                drop.startItemOwnerTimer(pc);
                        }
                    }
                    L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
                    LoggerInstance.getInstance().addItemAction(Logger.ItemActionType.DeathDrop, this, item, 1);
                    break;
                }
            }
        }
    }

    private void specialgahodrop(L1Character lastAttacker) {
        if (getInventory().checkItem(4100121)) {
            L1ItemInstance drop = ItemTable.getInstance().createItem(4100119);
            for (L1ItemInstance item : getInventory().getItems()) {
                if (item.getItemId() == 4100121) {
                    sendPackets((ServerBasePacket)new S_ServerMessage(3802));
                    sendPackets((ServerBasePacket)new S_ServerMessage(158, "\\f3$26712"));
                    getInventory().removeItem(item, 1);
                    if (lastAttacker instanceof L1PcInstance) {
                        L1PcInstance pc = (L1PcInstance)lastAttacker;
                        if (DropDelayItemTable.getInstance().isItem(drop.getItemId())) {
                            L1DropDelayItem temp = DropDelayItemTable.getInstance().getItem(drop.getItemId());
                            if (temp != null && temp.getItemId() == drop.getItemId())
                                drop.startItemOwnerTimer(pc);
                        }
                    }
                    L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
                    LoggerInstance.getInstance().addItemAction(Logger.ItemActionType.DeathDrop, this, item, 1);
                    break;
                }
            }
        }
    }

    private void specialgahodrop1(L1Character lastAttacker) {
        if (getInventory().checkItem(4100529))
            for (L1ItemInstance item : getInventory().getItems()) {
                if (item.getItemId() == 4100529) {
                    sendPackets((ServerBasePacket)new S_ServerMessage(3802));
                    sendPackets((ServerBasePacket)new S_ServerMessage(158, "\\f3$26712"));
                    getInventory().removeItem(item, 1);
                    LoggerInstance.getInstance().addItemAction(Logger.ItemActionType.DeathDrop, this, item, 1);
                    break;
                }
            }
    }

    private void drop2(L1Character lastAttacker) {
        if (getInventory().checkEquipped(10000)) {
            L1ItemInstance drop = ItemTable.getInstance().createItem(738);
            for (L1ItemInstance item : getInventory().getItems()) {
                if ((((item.getItemId() == 10000) ? 1 : 0) & item.isEquipped()) != 0) {
                    sendPackets((ServerBasePacket)new S_ServerMessage(3802));
                    sendPackets((ServerBasePacket)new S_ServerMessage(158, "$25382"));
                    getInventory().removeItem(item, 1);
                    if (lastAttacker instanceof L1PcInstance) {
                        L1PcInstance pc = (L1PcInstance)lastAttacker;
                        if (DropDelayItemTable.getInstance().isItem(drop.getItemId())) {
                            L1DropDelayItem temp = DropDelayItemTable.getInstance().getItem(drop.getItemId());
                            if (temp != null && temp.getItemId() == drop.getItemId())
                                drop.startItemOwnerTimer(pc);
                        }
                    }
                    L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
                    LoggerInstance.getInstance().addItemAction(Logger.ItemActionType.DeathDrop, this, item, 1);
                    break;
                }
            }
        }
    }

    private void drop3(L1Character lastAttacker) {
        if (getInventory().checkEquipped(10001)) {
            L1ItemInstance drop = ItemTable.getInstance().createItem(739);
            for (L1ItemInstance item : getInventory().getItems()) {
                if ((((item.getItemId() == 10001) ? 1 : 0) & item.isEquipped()) != 0) {
                    sendPackets((ServerBasePacket)new S_ServerMessage(3802));
                    sendPackets((ServerBasePacket)new S_ServerMessage(158, "$25384"));
                    getInventory().removeItem(item, 1);
                    if (lastAttacker instanceof L1PcInstance) {
                        L1PcInstance pc = (L1PcInstance)lastAttacker;
                        if (DropDelayItemTable.getInstance().isItem(drop.getItemId())) {
                            L1DropDelayItem temp = DropDelayItemTable.getInstance().getItem(drop.getItemId());
                            if (temp != null && temp.getItemId() == drop.getItemId())
                                drop.startItemOwnerTimer(pc);
                        }
                    }
                    L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
                    LoggerInstance.getInstance().addItemAction(Logger.ItemActionType.DeathDrop, this, item, 1);
                    break;
                }
            }
        }
    }

    public void movePlayerClanData(L1Clan move_clan) {
        setClanid(move_clan.getClanId());
        setClanname(move_clan.getClanName());
        setTitle("");
        setClanMemberNotes("");
        setClanRank(8);
        save();
        move_clan.addClanMember(getName(), getClanRank(), getLevel(), "", getId(), getType(), getOnlineStatus(), null);
    }

    public void ClearPlayerClanData(L1Clan clan) throws Exception {
        ClanStorageTable.getInstance().delete_Storage_List(this, getName());
        setClanid(0);
        setClanname("");
        setTitle("");
        setClanMemberId(0);
        setClanMemberNotes("");
        setClanRank(0);
        setClanContribution(0L);
        setClanJoinDate((Timestamp)null);
        if (this != null) {
            String broadcastTitle = null;
            if (hasSkillEffect(32423423))
                broadcastTitle = "\\fe[;
            sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo("", 11, this));
            sendPackets((ServerBasePacket)new S_CharTitle(getId(), broadcastTitle));
            Broadcaster.broadcastPacket(this, (ServerBasePacket)new S_CharTitle(getId(), broadcastTitle));
            sendPackets((ServerBasePacket)new S_ClanName(this));
            sendPackets((ServerBasePacket)new S_ReturnedStat(getId(), 0));
            Broadcaster.broadcastPacket(this, (ServerBasePacket)new S_ReturnedStat(getId(), 0));
            sendPackets((ServerBasePacket)new S_ClanAttention());
            ClanBuffListLoader.getInstance().leave_clan_remove_buff(this);
        }
        save();
    }

    public void startEinhasadTimer() {
        synchronized (this) {
            if (this._einhasadTimer != null)
                return;
            this._einhasadTimer = new EinhasadTimer();
            GeneralThreadPool.getInstance().schedule((Runnable)this._einhasadTimer, 900000L);
        }
    }

    public void stopEinhasadTimer() {
        synchronized (this) {
            if (this._einhasadTimer == null)
                return;
            this._einhasadTimer.cancel();
            this._einhasadTimer = null;
        }
    }

    class EinhasadTimer extends RepeatTask {
        public static final int INTERVAL = 900000;

        public EinhasadTimer() {
            super(900000L);
        }

        public void execute() {
            if (L1PcInstance.this.getAccount().getBlessOfAin() >= 2000000)
                return;
            L1PcInstance.this.getAccount().addBlessOfAin(1, L1PcInstance.this);
            SC_REST_EXP_INFO_NOTI.send(L1PcInstance.this);
        }
    }

    public long getFishingShopBuyTime_1() {
        return this.FishingShopBuyTime_1;
    }

    public void setFishingShopBuyTime_1(long fishingShopBuyTime_1) {
        this.FishingShopBuyTime_1 = fishingShopBuyTime_1;
    }

    private boolean createNewItem(L1PcInstance pc, int item_id, int count, int EnchantLevel, int AttEnchantLevel, int Bless) {
        L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
        if (item != null) {
            item.setCount(count);
            item.setEnchantLevel(EnchantLevel);
            item.setAttrEnchantLevel(AttEnchantLevel);
            item.setIdentified(true);
            pc.getInventory().storeItem(item);
            item.setBless(Bless);
            pc.getInventory().updateItem(item, 512);
            pc.getInventory().saveItem(item, 512);
            return true;
        }
        return false;
    }

    public int getRisingUp() {
        return this.risingUp;
    }

    public void setRisingUp(int i) {
        this.risingUp = i;
    }

    public void setImpactUp(int i) {
        this.impactUp = i;
    }

    public int getImpactUp() {
        return this.impactUp;
    }

    public int getGraceLv() {
        return this.graceLv;
    }

    public void setGraceLv(int i) {
        this.graceLv = i - 80;
        if (this.graceLv < 0) {
            this.graceLv = 0;
        } else if (this.graceLv > 15) {
            this.graceLv = 15;
        }
    }

    public void start_teleportForGM(int x, int y, int map, int heading, int effect_id, boolean effect_check, boolean skill_check) {
        try {
            if (isFishing())
                FishingTimeController.getInstance().endFishing(this);
            if (FishingTimeController.getInstance().isMember(this))
                FishingTimeController.getInstance().endFishing(this);
            if (hasSkillEffect(87) || hasSkillEffect(123) || hasSkillEffect(70705) || hasSkillEffect(208) || hasSkillEffect(157) || hasSkillEffect(230) || hasSkillEffect(5037) || hasSkillEffect(15037) || hasSkillEffect(329) || hasSkillEffect(379) || hasSkillEffect(328) || hasSkillEffect(243) || hasSkillEffect(242) || hasSkillEffect(5027) || hasSkillEffect(5002) || hasSkillEffect(51006) || hasSkillEffect(5057) || hasSkillEffect(5056) || hasSkillEffect(395) || hasSkillEffect(77) || isParalyzed() || isSleeped()) {
                sendPackets((ServerBasePacket)new S_Paralysis(7, false));
                return;
            }
            this.teleport = true;
            this.teleport_x = x;
            this.teleport_y = y;
            this.teleport_map = map;
            setHeading(heading);
            sendPackets((ServerBasePacket)new S_Teleport(this));
            if (getInventory().checkEquipped(900022)) {
                boolean mapcheck = (getMapId() >= 1708 && getMapId() <= 1712);
                if (!mapcheck)
                    sendPackets((ServerBasePacket)new S_PacketBox(180, 484, false));
            }
            clearTemporaryEffect();
            if (skill_check) {
                if (effect_check) {
                    S_SkillSound ss = new S_SkillSound(getId(), effect_id);
                    sendPackets((ServerBasePacket)ss, false);
                    Broadcaster.broadcastPacket(this, (ServerBasePacket)ss, false);
                }
                Runnable teleport = () -> L1Teleport.getInstance().doTeleportation(this);
                GeneralThreadPool.getInstance().schedule(teleport, 200L);
            } else if (effect_check) {
                setTemporaryEffect((ServerBasePacket)new S_SkillSound(getId(), effect_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sendPackets((ServerBasePacket)new S_Paralysis(7, false));
        }
    }

    public void start_teleport(int x, int y, int map, int heading, int effect_id, boolean effect_check) {
        start_teleport(x, y, map, heading, effect_id, effect_check, false);
    }

    public void start_teleport(int x, int y, int map, int heading, int effect_id, boolean effect_check, boolean isAlways) {
        try {
            if (hasSkillEffect(5158)) {
                sendPackets((ServerBasePacket)new S_Paralysis(7, false));
                return;
            }
            if (hasSkillEffect(5157)) {
                send_effect(20462);
                sendPackets((ServerBasePacket)new S_Paralysis(7, false));
                return;
            }
            if (getshadowstepchaser()) {
                sendPackets((ServerBasePacket)new S_Paralysis(7, false));
                return;
            }
            if (getPresherDeathRecall()) {
                if (!isDead() && getCurrentHp() > 0) {
                    int dmg = MJRnd.next(Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_DMG_MIN, Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_DMG);
                    receiveDamage(getPresherPc(), dmg);
                    send_effect(19349);
                }
                sendPackets((ServerBasePacket)new S_Paralysis(7, false));
                return;
            }
            if (effect_check) {
                S_SkillSound ss = new S_SkillSound(getId(), 18339);
                sendPackets((ServerBasePacket)ss, false);
                Broadcaster.broadcastPacket(this, (ServerBasePacket)ss, false);
            }
            if (isFishing())
                FishingTimeController.getInstance().endFishing(this);
            if (FishingTimeController.getInstance().isMember(this))
                FishingTimeController.getInstance().endFishing(this);
            if (hasSkillEffect(87) || hasSkillEffect(123) || hasSkillEffect(70705) || hasSkillEffect(208) || hasSkillEffect(157) || hasSkillEffect(230) || hasSkillEffect(5037) || hasSkillEffect(15037) || hasSkillEffect(5056) || hasSkillEffect(5027) || hasSkillEffect(329) || hasSkillEffect(379) || hasSkillEffect(328) || hasSkillEffect(243) || hasSkillEffect(242) || hasSkillEffect(5002) || hasSkillEffect(5003) || hasSkillEffect(51006) || hasSkillEffect(5057) || hasSkillEffect(5056) || hasSkillEffect(395) || hasSkillEffect(77) || isParalyzed() || isSleeped()) {
                sendPackets((ServerBasePacket)new S_Paralysis(7, false));
                return;
            }
            final MJCompanionInstance companion = get_companion();
            if (companion != null && companion.hasSkillEffect(4001))
                try {
                    GeneralThreadPool.getInstance().schedule(new Runnable() {
                        public void run() {
                            companion.sendSkillContinueEff();
                        }
                    },  500L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (getClan() != null) {
                getClan().deleteClanRetrieveUser(getId());
                if (getClan().getEinhasadBlessBuff() != 0) {
                    ClanBuffTable.ClanBuff Buff = ClanBuffTable.getBuffList(getClan().getEinhasadBlessBuff());
                    String[] Buffmap = null;
                    Buffmap = Buff.buffmaplist.split(",");
                    if (getClan().getEinhasadBlessBuff() != 0)
                        for (int j = 0; j < Buffmap.length; j++) {
                            int mapid = 0;
                            mapid = Integer.parseInt(Buffmap[j]);
                            if (map == mapid) {
                                int mapnum = mapid;
                                if (map == mapnum && getClanBuffMap() == 0) {
                                    setClanBuffMap(mapnum);
                                    addEinhasadBlessper(5);
                                    SC_REST_EXP_INFO_NOTI.send(this);
                                }
                            }
                        }
                    if (map != getClanBuffMap() && getClanBuffMap() != 0) {
                        setClanBuffMap(0);
                        addEinhasadBlessper(-5);
                        SC_REST_EXP_INFO_NOTI.send(this);
                    }
                }
            }
            if (isDead() && !isAlways && !MJRaidSpace.getInstance().isInInstance(this) && !MJInstanceSpace.isInInstance(this)) {
                sendPackets((ServerBasePacket)new S_Paralysis(7, false));
                return;
            }
            this.teleport = true;
            this.teleport_x = x;
            this.teleport_y = y;
            this.teleport_map = map;
            setHeading(heading);
            sendPackets((ServerBasePacket)new S_Teleport(this));
            sendPackets((ServerBasePacket)new S_PacketBox(160, this, getWeapon()), true);
            if (getInventory().checkEquipped(900022)) {
                boolean mapcheck = (getMapId() >= 1708 && getMapId() <= 1712);
                if (!mapcheck)
                    sendPackets((ServerBasePacket)new S_PacketBox(180, 484, false));
            }
            if (getZoneType() <= 0) {
                if (getSafetyZone() == true) {
                    sendPackets((ServerBasePacket)new S_ACTION_UI(207, false));
                    setSafetyZone(false);
                }
            } else if (!getSafetyZone()) {
                sendPackets((ServerBasePacket)new S_ACTION_UI(207, true));
                setSafetyZone(true);
            }
            clearTemporaryEffect();
            if (effect_check) {
                S_SkillSound ss = new S_SkillSound(getId(), effect_id);
                sendPackets((ServerBasePacket)ss, false);
                Broadcaster.broadcastPacket(this, (ServerBasePacket)ss, false);
            }
            if (isMassTel()) {
                Runnable teleport = () -> L1Teleport.getInstance().doTeleportation(this);
                GeneralThreadPool.getInstance().schedule(teleport, 287L);
            }
            if (getSpeedOverCount() != 0)
                this._move_speed_over_count = 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sendPackets((ServerBasePacket)new S_Paralysis(7, false));
        }
    }

    public void do_simple_teleport(int x, int y, int mapid) {
        if (isFishing())
            FishingTimeController.getInstance().endFishing(this);
        if (FishingTimeController.getInstance().isMember(this))
            FishingTimeController.getInstance().endFishing(this);
        if (getInventory().checkEquipped(900022)) {
            boolean mapcheck = (getMapId() >= 1708 && getMapId() <= 1712);
            if (!mapcheck)
                sendPackets((ServerBasePacket)new S_PacketBox(180, 484, false));
        }
        S_SkillSound ss = new S_SkillSound(getId(), 169);
        sendPackets((ServerBasePacket)ss, false);
        Broadcaster.broadcastPacket(this, (ServerBasePacket)ss, false);
        ss.clear();
        L1Teleport.getInstance().doTeleport(this, x, y, mapid);
    }

    public void Cruel_attack_Short(L1Character attacker, int skill_effect) {
        if (attacker.get_Maelstrom())
            return;
        int attacker_x = attacker.getX();
        int attacker_y = attacker.getY();
        int x = getX();
        int y = getY();
        int moveX = 0;
        int moveY = 0;
        int range = 3;
        if (attacker.isPassive(MJPassiveID.CRUEL_CONBICTION.toInt()))
            range = 4;
        int heading = MJCommons.calcheading(attacker_x, attacker_y, x, y);
        int Range = attacker.getLocation().getTileDistance(getX(), getY());
        boolean isRange = (getLocation().getTileLineDistance(new Point(attacker.getX(), attacker.getY())) <= range);
        L1Map map = L1WorldMap.getInstance().getMap(getMapId());
        boolean firstFail = false;
        int rangecheck = 1;
        switch (heading) {
            case 0:
            case 2:
            case 4:
            case 6:
                rangecheck = 1;
                break;
            case 1:
            case 3:
            case 5:
            case 7:
                rangecheck = 2;
                break;
        }
        if (isRange) {
            int checkX = x, checkY = y;
            if (Math.abs(attacker_x - x) + Math.abs(attacker_y - y) > rangecheck) {
                while (heading != -1) {
                    heading = MJCommons.calcheading(attacker_x, attacker_y, checkX, checkY);
                    switch (heading) {
                        case 0:
                            checkY++;
                            break;
                        case 1:
                            checkX--;
                            checkY++;
                            break;
                        case 2:
                            checkX--;
                            break;
                        case 3:
                            checkX--;
                            checkY--;
                            break;
                        case 4:
                            checkY--;
                            break;
                        case 5:
                            checkX++;
                            checkY--;
                            break;
                        case 6:
                            checkX++;
                            break;
                        case 7:
                            checkX++;
                            checkY++;
                            break;
                    }
                    if (attacker.getLocation().getLineDistance(new Point(checkX, checkY)) < 1.0D)
                        break;
                    if ((checkX == attacker.getX() && checkY == attacker.getY()) || !map.isPassable(checkX, checkY))
                        break;
                    moveX = checkX;
                    moveY = checkY;
                }
                L1Teleport.getInstance().doTeleport(this, moveX, moveY, getMapId());
            }
        }
    }

    public void Cruel_attack_Long(L1Character attacker, int skill_effect) {
        if (attacker.get_Maelstrom())
            return;
        int attacker_x = attacker.getX();
        int attacker_y = attacker.getY();
        int x = getX();
        int y = getY();
        int moveX = 0;
        int moveY = 0;
        int range = 3;
        if (attacker.isPassive(MJPassiveID.CRUEL_CONBICTION.toInt()))
            range = 4;
        int heading = MJCommons.calcheading(attacker_x, attacker_y, x, y);
        int Range = attacker.getLocation().getTileDistance(getX(), getY());
        boolean isRange = (getLocation().getTileLineDistance(new Point(attacker.getX(), attacker.getY())) <= range);
        L1Map map = L1WorldMap.getInstance().getMap(getMapId());
        boolean firstFail = false;
        if (isRange) {
            int checkX = x, checkY = y;
            if (Math.abs(attacker_x - x) + Math.abs(attacker_y - y) < range) {
                while (heading != -1) {
                    heading = MJCommons.calcheading(attacker_x, attacker_y, checkX, checkY);
                    switch (heading) {
                        case 0:
                            checkY--;
                            break;
                        case 1:
                            checkX++;
                            checkY--;
                            break;
                        case 2:
                            checkX++;
                            break;
                        case 3:
                            checkX++;
                            checkY++;
                            break;
                        case 4:
                            checkY++;
                            break;
                        case 5:
                            checkX--;
                            checkY++;
                            break;
                        case 6:
                            checkX--;
                            break;
                        case 7:
                            checkX--;
                            checkY--;
                            break;
                    }
                    int i = 0;
                    if (attacker.getLocation().getTileLineDistance(new Point(checkX, checkY)) > range)
                        break;
                    if (!map.isPassable(checkX, checkY))
                        break;
                    if (i == 0 && !map.isArrowPassable(checkX, checkY, attacker.targetDirection(checkX, checkY)))
                        break;
                    moveX = checkX;
                    moveY = checkY;
                }
                if (moveX == 0 || moveY == 0) {
                    moveX = x;
                    moveY = y;
                }
                L1Teleport.getInstance().doTeleport(this, moveX, moveY, getMapId());
            }
        }
    }

    public void Panthera_attack(L1Character target, int skill_effect, int attack_effect, int skillId) {
        if (get_Maelstrom())
            return;
        int x = target.getX();
        int y = target.getY();
        if (getX() == target.getX() && getY() == target.getY()) {
            x = target.getX();
            y = target.getY();
        } else {
            switch (targetDirection(x, y)) {
                case 7:
                    x++;
                    y++;
                    break;
                case 6:
                    x++;
                    break;
                case 5:
                    x++;
                    y--;
                    break;
                case 4:
                    y--;
                    break;
                case 3:
                    x--;
                    y--;
                    break;
                case 2:
                    x--;
                    break;
                case 1:
                    x--;
                    y++;
                    break;
                case 0:
                    y++;
                    break;
            }
            if (getX() != x || getY() != y) {
                L1Map map = (new L1Location(x, y, getMapId())).getMap();
                if (!map.isInMap(x, y))
                    return;
                if ((skillId != 199 || !isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())) && !map.isPassable(x, y))
                    return;
            }
        }
        if (skillId == 199 && isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())) {
            L1Map map = (new L1Location(x, y, getMapId())).getMap();
            set_MassTel(true);
            start_teleport(x, y, getMapId(), getHeading(), skill_effect, false, false);
            S_SkillSound skill = new S_SkillSound(getId(), skill_effect);
            sendPackets((ServerBasePacket)skill, false);
            Broadcaster.broadcastPacket(this, (ServerBasePacket)skill, false);
            skill.clear();
            S_SkillSound attack = new S_SkillSound(getId(), attack_effect);
            sendPackets((ServerBasePacket)attack, false);
            Broadcaster.broadcastPacket(this, (ServerBasePacket)attack, false);
            attack.clear();
        } else {
            L1Teleport.getInstance().doTeleport(this, x, y, getMapId());
            S_SkillSound skill = new S_SkillSound(getId(), skill_effect);
            sendPackets((ServerBasePacket)skill, false);
            Broadcaster.broadcastPacket(this, (ServerBasePacket)skill, false);
            skill.clear();
            S_SkillSound attack = new S_SkillSound(getId(), attack_effect);
            sendPackets((ServerBasePacket)attack, false);
            Broadcaster.broadcastPacket(this, (ServerBasePacket)attack, false);
            attack.clear();
        }
    }

    public int getMoveSkillX() {
        return this._moveSkillX;
    }

    public void setMoveSkillX(int i) {
        this._moveSkillX = i;
    }

    public int getMoveSkillY() {
        return this._moveSkillY;
    }

    public void setMoveSkillY(int i) {
        this._moveSkillY = i;
    }

    public short getMoveSkillMapId() {
        return this._moveSkillMapId;
    }

    public void setMoveSkillMapId(short i) {
        this._moveSkillMapId = i;
    }

    public byte getMoveSkillHeading() {
        return this._moveSkillHeading;
    }

    public void setMoveSkillHeading(byte i) {
        this._moveSkillHeading = i;
    }

    public boolean get_teleport() {
        return this.teleport;
    }

    public void set_teleport(boolean b) {
        this.teleport = b;
        if (this.teleport);
    }

    public int get_teleport_x() {
        return this.teleport_x;
    }

    public void set_teleport_x(int i) {
        this.teleport_x = i;
    }

    public int get_teleport_y() {
        return this.teleport_y;
    }

    public void set_teleport_y(int i) {
        this.teleport_y = i;
    }

    public int get_teleport_map() {
        return this.teleport_map;
    }

    public void set_teleport_map(int i) {
        this.teleport_map = i;
    }

    public int get_teleport_count() {
        return this.teleport_count;
    }

    public void set_teleport_count(int i) {
        this.teleport_count = i;
    }

    public SkillData get_skill() {
        return this.skill_data;
    }

    public boolean isTwoLogin(L1PcInstance c) {
        boolean bool = false;
        for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
            if (target.noPlayerCK)
                continue;
            if (c.getId() != target.getId() && !target.isPrivateShop() && c.getNetConnection().getAccountName().equalsIgnoreCase(target.getNetConnection().getAccountName())) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    public int getAttackDelay() {
        return this._attackDelay;
    }

    public void setAttackDelay(int i) {
        this._attackDelay = i;
    }

    public int getAttackSpeed() {
        return this._attackSpeed;
    }

    public void setAttackSpeed() {
        int status = 0;
        L1PolyMorph poly = PolyTable.getInstance().find(getCurrentSpriteId());
        int level = getLevel();
        if (poly != null)
            level = Math.max(level, poly.getMinLevel());
        status = SpriteInformationLoader.levelToIndex(level, getCurrentSpriteId());
        int armorGfx = getPolyIdByEquip();
        if (armorGfx == 11365)
            status = Math.max(status, 9);
        if (this._attackSpeed != status) {
            sendPackets((ServerBasePacket)new S_CharSpeedUpdate(getId(), status, getLevel()));
            broadcastPacket((ServerBasePacket)new S_CharSpeedUpdate(getId(), status, getLevel()));
        }
        this._attackSpeed = status;
    }

    public int getPolyIdByEquip() {
        return this._polyIdByEquip;
    }

    public void setPolyIdByEquip(int i) {
        this._polyIdByEquip = i;
    }

    public L1HateList getAutoTargetList() {
        return this._autoTargetList;
    }

    public void setAutoTargetList(L1HateList attackList) {
        this._autoTargetList = attackList;
    }

    public void addAutoTargetList(L1Character mon) {
        if (this._autoTargetList.containsKey(mon))
            return;
        this._autoTargetList.add(mon, 0);
    }

    public void removeAutoTargetList(L1Character mon) {
        if (mon == null || !this._autoTargetList.containsKey(mon))
            return;
        this._autoTargetList.remove(mon);
    }

    public void setHUNTER_BLESS3(boolean b) {
        this._HUNTER_BLESS3 = b;
    }

    public boolean isHUNTER_BLESS3() {
        return this._HUNTER_BLESS3;
    }

    public boolean isAutoSetting() {
        return this._isAutoSetting;
    }

    public void setAutoSetting(boolean b) {
        this._isAutoSetting = b;
    }

    public int getAutoPolyID() {
        return this._autoPolyId;
    }

    public void setAutoPolyID(int i) {
        this._autoPolyId = i;
    }

    public int getAutoLocX() {
        return this._autoLocX;
    }

    public void setAutoLocX(int i) {
        this._autoLocX = i;
    }

    public int getAutoLocY() {
        return this._autoLocY;
    }

    public void setAutoLocY(int i) {
        this._autoLocY = i;
    }

    public L1Character getAutoTarget() {
        return this._autoTarget;
    }

    public void setAutoTarget(L1Character mon) {
        this._autoTarget = mon;
    }

    public L1Astar getAutoAstar() {
        return this._autoAStar;
    }

    public void setAutoAstar(L1Astar a) {
        this._autoAStar = a;
    }

    public int[][] getAutoPath() {
        return this._autoPath;
    }

    public void setAutoPath(int[][] i) {
        this._autoPath = i;
    }

    public int getAutoMoveCount() {
        return this._autoMoveCount;
    }

    public void setAutoMoveCount(int i) {
        this._autoMoveCount = i;
    }

    public long getAutoSkillDelay() {
        return this._autoSkillDelay;
    }

    public void setAutoSkillDelay(long i) {
        this._autoSkillDelay = i;
    }

    public int getAutoStatus() {
        return this._autoStatus;
    }

    public void setAutoStatus(int i) {
        this._autoStatus = i;
    }

    public L1Node getAutoTail() {
        return this._autoTail;
    }

    public void setAutoTail(L1Node node) {
        this._autoTail = node;
    }

    public boolean isAutoPathFirst() {
        return this._autoPathFirst;
    }

    public void setAutoPathFirst(boolean a) {
        this._autoPathFirst = a;
    }

    public int getAutoPotion() {
        return this._autoPotion;
    }

    public void setAutoPotion(int i) {
        this._autoPotion = i;
    }

    public long getAutoTimeAttack() {
        return this._autoTimeAttack;
    }

    public void setAutoTimeAttack(long time) {
        this._autoTimeAttack = time;
    }

    public long getAutoTimeMove() {
        return this._autoTimeMove;
    }

    public void setAutoTimeMove(long time) {
        this._autoTimeMove = time;
    }

    public boolean isAutoDead() {
        return this._autoDead;
    }

    public void setAutoDead(boolean b) {
        this._autoDead = b;
    }

    public int getAutoDeadTime() {
        return this._autoDeadTime;
    }

    public void setAutoDeadTime(int i) {
        this._autoDeadTime = i;
    }

    public int getAutoTeleportTime() {
        return this._autoTeleportTime;
    }

    public void setAutoTeleportime(int i) {
        this._autoTeleportTime = i;
    }

    public int getAutoRange() {
        return this._autoRange;
    }

    public void setAutoRange(int i) {
        this._autoRange = i;
    }

    public long getAutoAiTime() {
        return this._autoAiTime;
    }

    public void setAutoAiTime(long l) {
        this._autoAiTime = l;
    }

    public int getAutoMapId() {
        return this._autoMapId;
    }

    public void setAutoMapId(int i) {
        this._autoMapId = i;
    }

    public void addslotsetting(int slotNum, int slorcolr, String Slotname) {
        L1InventorySlot slot = get_slot_info(slotNum);
        if (slot == null)
            slot = new L1InventorySlot();
        slot.set_Color(slorcolr);
        slot.set_Slotname(Slotname);
        this._slotcolor.put(Integer.valueOf(slotNum), slot);
    }

    public HashMap<Integer, L1InventorySlot> get_slot_color() {
        return this._slotcolor;
    }

    public L1InventorySlot get_slot_info(int slottype) {
        return this._slotcolor.get(Integer.valueOf(slottype));
    }

    public void addSlotItem(int slotNum, int itemobjid, boolean flag) {
        if (flag) {
            if (slotNum == 0) {
                this._slotItemOne.clear();
                for (L1ItemInstance item : getInventory().getItems()) {
                    if (item.isEquipped())
                        this._slotItemOne.add(Integer.valueOf(item.getId()));
                }
            } else if (slotNum == 1) {
                this._slotItemTwo.clear();
                for (L1ItemInstance item : getInventory().getItems()) {
                    if (item.isEquipped())
                        this._slotItemTwo.add(Integer.valueOf(item.getId()));
                }
            } else if (slotNum == 2) {
                this._slotItemThree.clear();
                for (L1ItemInstance item : getInventory().getItems()) {
                    if (item.isEquipped())
                        this._slotItemThree.add(Integer.valueOf(item.getId()));
                }
            } else if (slotNum == 3) {
                this._slotItemFour.clear();
                for (L1ItemInstance item : getInventory().getItems()) {
                    if (item.isEquipped())
                        this._slotItemFour.add(Integer.valueOf(item.getId()));
                }
            }
        } else if (slotNum == 0) {
            this._slotItemOne.add(Integer.valueOf(itemobjid));
        } else if (slotNum == 1) {
            this._slotItemTwo.add(Integer.valueOf(itemobjid));
        } else if (slotNum == 2) {
            this._slotItemThree.add(Integer.valueOf(itemobjid));
        } else if (slotNum == 3) {
            this._slotItemFour.add(Integer.valueOf(itemobjid));
        }
    }

    public List<Integer> getSlotItems(int slotNum) {
        if (slotNum == 0)
            return this._slotItemOne;
        if (slotNum == 1)
            return this._slotItemTwo;
        if (slotNum == 2)
            return this._slotItemThree;
        if (slotNum == 3)
            return this._slotItemFour;
        return null;
    }

    public void getChangeSlot(int slotNum) {
        if (slotNum == 0) {
            for (L1ItemInstance item : getInventory().getItems()) {
                if (!this._slotItemOne.contains(Integer.valueOf(item.getId())) && item.isEquipped()) {
                    if (item.getItemId() == 20077) {
                        beginInvisTimer();
                    } else if (item.getItemId() == 20062) {
                        beginInvisTimer();
                    } else if (item.getItemId() == 120077) {
                        beginInvisTimer();
                    }
                    getInventory().setEquipped(item, false);
                }
            }
            for (L1ItemInstance item : getInventory().getItems()) {
                if (this._slotItemOne.contains(Integer.valueOf(item.getId()))) {
                    if (item.isEquipped()) {
                        if (item.getItem().getType2() == 1 && !L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType()))
                            getInventory().setEquipped(item, false);
                        continue;
                    }
                    if (item.getItem().getType2() == 1 && !L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType()))
                        continue;
                    getInventory().setEquipped(item, true);
                }
            }
        } else if (slotNum == 1) {
            for (L1ItemInstance item : getInventory().getItems()) {
                if (!this._slotItemTwo.contains(Integer.valueOf(item.getId())) && item.isEquipped()) {
                    if (item.getItemId() == 20077) {
                        beginInvisTimer();
                    } else if (item.getItemId() == 20062) {
                        beginInvisTimer();
                    } else if (item.getItemId() == 120077) {
                        beginInvisTimer();
                    }
                    getInventory().setEquipped(item, false);
                }
            }
            for (L1ItemInstance item : getInventory().getItems()) {
                if (this._slotItemTwo.contains(Integer.valueOf(item.getId()))) {
                    if (item.isEquipped()) {
                        if (item.getItem().getType2() == 1 && !L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType()))
                            getInventory().setEquipped(item, false);
                        continue;
                    }
                    if (item.getItem().getType2() == 1 && !L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType()))
                        continue;
                    getInventory().setEquipped(item, true);
                }
            }
        } else if (slotNum == 2) {
            for (L1ItemInstance item : getInventory().getItems()) {
                if (!this._slotItemThree.contains(Integer.valueOf(item.getId())) && item.isEquipped()) {
                    if (item.getItemId() == 20077) {
                        beginInvisTimer();
                    } else if (item.getItemId() == 20062) {
                        beginInvisTimer();
                    } else if (item.getItemId() == 120077) {
                        beginInvisTimer();
                    }
                    getInventory().setEquipped(item, false);
                }
            }
            for (L1ItemInstance item : getInventory().getItems()) {
                if (this._slotItemThree.contains(Integer.valueOf(item.getId()))) {
                    if (item.isEquipped()) {
                        if (item.getItem().getType2() == 1 && !L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType()))
                            getInventory().setEquipped(item, false);
                        continue;
                    }
                    if (item.getItem().getType2() == 1 && !L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType()))
                        continue;
                    getInventory().setEquipped(item, true);
                }
            }
        } else if (slotNum == 3) {
            for (L1ItemInstance item : getInventory().getItems()) {
                if (!this._slotItemFour.contains(Integer.valueOf(item.getId())) && item.isEquipped()) {
                    if (item.getItemId() == 20077) {
                        beginInvisTimer();
                    } else if (item.getItemId() == 20062) {
                        beginInvisTimer();
                    } else if (item.getItemId() == 120077) {
                        beginInvisTimer();
                    }
                    getInventory().setEquipped(item, false);
                }
            }
            for (L1ItemInstance item : getInventory().getItems()) {
                if (this._slotItemFour.contains(Integer.valueOf(item.getId()))) {
                    if (item.isEquipped()) {
                        if (item.getItem().getType2() == 1 && !L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType()))
                            getInventory().setEquipped(item, false);
                        continue;
                    }
                    if (item.getItem().getType2() == 1 && !L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType()))
                        continue;
                    getInventory().setEquipped(item, true);
                }
            }
        }
        sendPackets((ServerBasePacket)new S_SPMR(this));
        sendPackets((ServerBasePacket)new S_OwnCharStatus(this));
        sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
        sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
    }

    public int getSlotNumber() {
        return this.slotNumber;
    }

    public void setSlotNumber(int i) {
        this.slotNumber = i;
    }

    public long getBuffTime() {
        return this._buffTime;
    }

    public void setBuffTime(long i) {
        this._buffTime = i;
    }

    public void setOneTel(boolean a) {
        this._isOneTel = a;
    }

    public boolean isOneTel() {
        return this._isOneTel;
    }

    public int getRobotAIType() {
        return this._robotAIType;
    }

    public void setRobotAIType(int i) {
        this._robotAIType = i;
    }

    public int getRobotPattern() {
        return this._robotPattern;
    }

    public void setRobotPattern(int i) {
        this._robotPattern = i;
    }

    public long getRobotStopTime() {
        return this._robotStopTime;
    }

    public void setRobotStopTime(long time) {
        this._robotStopTime = time;
    }

    public long getRobotPotionTime() {
        return this._robotPotionTime;
    }

    public void setRobotPotionTime(long time) {
        this._robotPotionTime = time;
    }

    public void setAttackRang(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getAttackRang() {
        return this.attackRange;
    }

    public boolean isPolyRingMaster() {
        return this._isPolyRingMaster;
    }

    public void setPolyRingMaster(boolean flag) {
        this._isPolyRingMaster = flag;
    }

    public boolean isPolyRingMaster2() {
        return this._isPolyRingMaster2;
    }

    public void setPolyRingMaster2(boolean flag) {
        this._isPolyRingMaster2 = flag;
    }

    public int getElfAttrInitCount() {
        return this._elfAttrInitCount;
    }

    public void setElfAttrInitCount(int i) {
        if (i >= 20)
            i = 20;
        this._elfAttrInitCount = i;
    }

    public void addElfAttrInitCount(int i) {
        int count = this._elfAttrInitCount + i;
        if (count >= 20)
            count = 20;
        this._elfAttrInitCount = count;
    }

    public double getImmuneReduction() {
        if (this._lastImmuneLevel < 36)
            return 0.0D;
        if (this._lastImmuneLevel >= Config.MagicAdSetting_Wizard.IMMUNELEVEL)
            return Config.MagicAdSetting_Wizard.IMMUNEDMG;
        if (this._lastImmuneLevel >= Config.MagicAdSetting_Wizard.IMMUNELEVEL1)
            return Config.MagicAdSetting_Wizard.IMMUNEDMG1;
        if (this._lastImmuneLevel >= Config.MagicAdSetting_Wizard.IMMUNELEVEL2)
            return Config.MagicAdSetting_Wizard.IMMUNEDMG2;
        if (this._lastImmuneLevel >= Config.MagicAdSetting_Wizard.IMMUNELEVEL3)
            return Config.MagicAdSetting_Wizard.IMMUNEDMG3;
        if (this._lastImmuneLevel >= Config.MagicAdSetting_Wizard.IMMUNELEVEL4)
            return Config.MagicAdSetting_Wizard.IMMUNEDMG4;
        return Config.MagicAdSetting_Wizard.IMMUNEDMG7;
    }

    public int getLastImmuneLevel() {
        return this._lastImmuneLevel;
    }

    public void setLastImmuneLevel(int i) {
        this._lastImmuneLevel = i;
    }

    public void startValaBlessing() {
        if (!this._isValakasBlessing) {
            this._isValakasBlessing = true;
            this._vBlessing = new ValakasBlessing(this);
            if (_blessingTimer != null) {
                _blessingTimer.cancel();
                _blessingTimer = null;
            }
            _blessingTimer = new Timer(true);
            _blessingTimer.scheduleAtFixedRate((TimerTask)this._vBlessing, 16000L, 16000L);
        }
    }

    public void stopValaBlessing() {
        if (this._isValakasBlessing) {
            if (this._vBlessing != null) {
                this._vBlessing.cancel();
                this._vBlessing = null;
            }
            this._isValakasBlessing = false;
        }
    }

    public boolean isValakasBlessing() {
        return this._isValakasBlessing;
    }

    public boolean isBossNotify() {
        return this._isBossNotify;
    }

    public void setBossNotify(boolean b) {
        this._isBossNotify = b;
    }

    public MJInstanceEnums.InstStatus getInstStatus() {
        return this._instStatus;
    }

    public void setInstStatus(MJInstanceEnums.InstStatus status) {
        this._instStatus = status;
    }

    public int getDamageFromLfc() {
        return this._dmgLfc;
    }

    public void addDamageFromLfc(int i) {
        this._dmgLfc = i;
    }

    public void setDamageFromLfc(int i) {
        this._dmgLfc = i;
    }

    public int getFindMerchantId() {
        return this._findMerchantId;
    }

    public void setFindMerchantId(int i) {
        this._findMerchantId = i;
    }

    public boolean isValakasProduct() {
        return this._isValakasProduct;
    }

    public void setValakasProduct(boolean b) {
        this._isValakasProduct = b;
    }

    public MJExpAmplifier getExpAmplifier() {
        return this._expAmplifier;
    }

    public void setExpAmplifier(MJExpAmplifier amp) {
        if (this._expAmplifier != null) {
            if (this._expAmplifier.equals(amp))
                return;
            killSkillEffectTimer(6432);
            sendPackets((ServerBasePacket)S_InventoryIcon.icoEnd(6432));
        }
        this._expAmplifier = amp;
        if (amp != null) {
            setSkillEffect(6432, -1L);
            sendPackets((ServerBasePacket)S_InventoryIcon.iconNewUnLimitAndPriority(0, 6432, amp.getMessageId(), true));
        }
    }

    public ProtoOutputStream getWorldObject() {
        return this._wrdPck;
    }

    public void setWorldObject(ProtoOutputStream wrdPck) {
        this._wrdPck = wrdPck;
    }

    public L1ItemInstance getKillDeathInitializeItem() {
        return this._kdInitItem;
    }

    public void setKillDeathInitializeItem(L1ItemInstance item) {
        this._kdInitItem = item;
    }

    public void RenewStat() {
        SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 1, getType());
        SC_STAT_RENEWAL_BASESTAT_INFO_RES.send_bonus_stat(this, 25);
        SC_STAT_RENEWAL_BASESTAT_INFO_RES.send_bonus_stat(this, 35);
        SC_STAT_RENEWAL_BASESTAT_INFO_RES.send_bonus_stat(this, 45);
        SC_STAT_RENEWAL_BASESTAT_INFO_RES.send_bonus_stat(this, 55);
        SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
    }

    public boolean isOutsideChat() {
        return this._isOutsideChat;
    }

    public void setOutSideChat(boolean b) {
        this._isOutsideChat = b;
    }

    public long getLastNpcClickMs() {
        return this._lastNpcClickMs;
    }

    public void setLastNpcClickMs(long l) {
        this._lastNpcClickMs = l;
    }

    public int getCharLevelBonus() {
        return this._charLevelBonus;
    }

    public void setCharLevelBonus(int i) {
        this._charLevelBonus = i;
    }

    public synchronized boolean onStat(String s) throws Exception {
        if (remainBonusStats() <= 0)
            return false;
        if (getOnlineStatus() != 1) {
            sendPackets((ServerBasePacket)new S_Disconnect());
            return false;
        }
        if (s.toLowerCase().equals("力量".toLowerCase())) {
            if (getAbility().getStr() < 50 || (getLevel() >= 90 && getAbility().getStr() < 60)) {
                getAbility().addStr(1);
                sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
                sendPackets((ServerBasePacket)new S_CharVisualUpdate(this));
                sendPackets((ServerBasePacket)new S_Weight(this));
                save();
            } else {
                sendPackets((ServerBasePacket)new S_ServerMessage(481));
                throw new Exception(s);
            }
        } else if (s.toLowerCase().equals("敏捷".toLowerCase())) {
            if (getAbility().getDex() < 50 || (getLevel() >= 90 && getAbility().getDex() < 60)) {
                getAbility().addDex(1);
                resetBaseAc();
                sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
                sendPackets((ServerBasePacket)new S_CharVisualUpdate(this));
                save();
            } else {
                sendPackets((ServerBasePacket)new S_ServerMessage(481));
                throw new Exception(s);
            }
        } else if (s.toLowerCase().equals("體質".toLowerCase())) {
            if (getAbility().getCon() < 50 || (getLevel() >= 90 && getAbility().getCon() < 60)) {
                getAbility().addCon(1);
                sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
                sendPackets((ServerBasePacket)new S_CharVisualUpdate(this));
                sendPackets((ServerBasePacket)new S_Weight(this));
                save();
            } else {
                sendPackets((ServerBasePacket)new S_ServerMessage(481));
                throw new Exception(s);
            }
        } else if (s.toLowerCase().equals("智力".toLowerCase())) {
            if (getAbility().getInt() < 50 || (getLevel() >= 90 && getAbility().getInt() < 60)) {
                getAbility().addInt(1);
                sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
                sendPackets((ServerBasePacket)new S_CharVisualUpdate(this));
                save();
            } else {
                sendPackets((ServerBasePacket)new S_ServerMessage(481));
                throw new Exception(s);
            }
        } else if (s.toLowerCase().equals("精神".toLowerCase())) {
            if (getAbility().getWis() < 50 || (getLevel() >= 90 && getAbility().getWis() < 60)) {
                getAbility().addWis(1);
                sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
                sendPackets((ServerBasePacket)new S_CharVisualUpdate(this));
                save();
            } else {
                sendPackets((ServerBasePacket)new S_ServerMessage(481));
                throw new Exception(s);
            }
        } else if (s.toLowerCase().equals("魅力".toLowerCase())) {
            if (getAbility().getCha() < 50 || (getLevel() >= 90 && getAbility().getCha() < 60)) {
                getAbility().addCha(1);
                sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
                sendPackets((ServerBasePacket)new S_CharVisualUpdate(this));
                save();
            } else {
                sendPackets((ServerBasePacket)new S_ServerMessage(481));
                throw new Exception(s);
            }
        } else if (s.toLowerCase().equals("")) {
            sendPackets((ServerBasePacket)new S_ServerMessage(480));
        } else {
            throw new Exception(s);
        }
        checkStatus();
        sendBonusStats();
        return true;
    }

    public L1ItemInstance getNameInstance() {
        return this._NameInstance;
    }

    public void setNameInstance(L1ItemInstance item) {
        this._NameInstance = item;
    }

    public int getMagicDodgeProbability() {
        return this._magicDodgeProb;
    }

    public void setMagicDodgeProbability(int i) {
        this._magicDodgeProb = i;
    }

    public void addMagicDodgeProbability(int i) {
        this._magicDodgeProb += i;
        sendPackets((ServerBasePacket)new S_ACTION_UI2(488, this._magicDodgeProb), true);
    }

    public int getTemporaryItemObjectId() {
        return this._temporaryItemObjectId;
    }

    public void setTemporaryItemObjectId(int i) {
        this._temporaryItemObjectId = i;
    }

    public void clearTemporaryItemObjectId() {
        this._temporaryItemObjectId = -1;
    }

    private static int _instanceType = -1;

    private CharacterActionExecutor _actionExecutor;

    private ServerBasePacket _tempEffect;

    public long lastSpellUseMillis;

    public int lastSpellUsePending;

    public int lastSpeedUsePending;

    private SC_ATTENDANCE_USER_DATA_EXTEND _attendance_data;

    private boolean _isOnTargetEffect;

    private Timestamp _lastTopBless;

    private BQSCharacterData _bqsData;

    private MJEPcStatus _instance_status;

    private int _current_combat_id;

    private int _current_combat_team_id;

    private boolean _is_non_action;

    private int _mark_status;

    private MJCaptcha _captcha;

    private int _lateral_damage;

    private int _lateral_reduction;

    private int _lateral_magic_rate;

    private DungeonTimeUserInformation _dtInfo;

    private S_ItemExSelectPacket _select_item;

    private long lastSavedTime;

    private long lastSavedTime_inventory;

    private long lastSavedTime_exp;

    private long lastSavedTime_Fatigue;

    private int _lotto;

    private long _PostDelay;

    private HashMap<Integer, MJPassiveInfo> m_passives;

    public boolean isAutoTreeple;

    private int _ClanBuffMap;

    private long _lastMoveActionMillis;

    private int _EinhasadBlessper;

    private boolean m_is_ready_server_shift;

    private MJShiftBattleCharacterInfo m_battle_info;

    private MJCompanionInstance m_companion;

    private double m_item_exp_bonus;

    private boolean m_is_using_items;

    private boolean m_is_client_auto;

    public int _client_auto_type;

    private HashMap<Integer, Integer> m_private_porbability;

    private boolean _is_stat_reset;

    public int pvp_dmg_ignore;

    public int pvp_mdmg;

    public int pvp_mdmg_ignore;

    public int immune_ignore;

    public int immune_minus_per;

    public int Moebius_ignore;

    private MJReGenerator _regenerator;

    private PolyTrigger polyTrigger;

    private MJAttrMap attribute;

    private MJObjectEventHandler eventHandler;

    public boolean InstanceDungeon;

    public int indun_room_num;

    public MJIndunRoomModel indun_model;

    private L1ItemInstance equipmentChangeItem;

    private int equipmentChangeUseItemId;

    private ArrayList<L1ItemInstance> _eqcList;

    private ArrayList<L1ItemInstance> _itemselectorlist;

    public int _bot_wait;

    public int _bot_wait_check;

    private boolean _bot_warehouse;

    private boolean _bot_shop;

    private boolean _bot_buff;

    private boolean _bot_teleport;

    private boolean _bot_success;

    private int _judgement;

    private boolean _Prime_war_zone;

    private boolean _isPrimeCast;

    private int _glory_earth_attr;

    private int _Bonus_drop_npc;

    private boolean _Destroy_pier;

    private boolean _Destroy_horror;

    private int _craftUseType;

    private int _virualEinhasad;

    private boolean _Erzabe_circle;

    private int _console_type;

    private int _doll_judgement_type;

    public int[] _Insert_Tel_xym;

    private ArrayList<String> _macro_list;

    private Macro _macro_timer;

    private int _move_speed_over_count;

    public int[] _move_speed_over_loc;

    public double _AdenBonus;

    public double _ItemBonus;

    public int pvp_defense_per;

    public int Magic_defense_per;

    private int _infinity_team_id;

    public int[] doll_update_option;

    private long _clan_contribution;

    private Timestamp _class_rank_bless;

    private Map<Integer, CustomQuestUser> customQuestList;

    private int customQuestId;

    private int customQuestNpcObjId;

    private boolean _spear_mode_type;

    private boolean _vanguard_type;

    public int getL1Type() {
        return (_instanceType == -1) ? (_instanceType = super.getL1Type() | 0x4) : _instanceType;
    }

    public long getCurrentSpriteInterval(EActionCodes actionCode) {
        return (long)this._currentSpriteInfo.getInterval(this, actionCode);
    }

    public void sendShape(int poly) {
        S_ChangeShape shape = new S_ChangeShape(getId(), poly, getCurrentWeapon());
        sendPackets((ServerBasePacket)shape, false);
        broadcastPacket((ServerBasePacket)shape);
    }

    public void offFishing() {
        if (isFishing())
            try {
                setFishing(false);
                setFishingTime(0L);
                setFishingReady(false);
                sendPackets((ServerBasePacket)new S_CharVisualUpdate(this));
                Broadcaster.broadcastPacket(this, (ServerBasePacket)new S_CharVisualUpdate(this));
                FishingTimeController.getInstance().removeMember(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void registerActionHandler(int idx, AbstractActionHandler handler) {
        if (this._actionExecutor == null)
            this._actionExecutor = CharacterActionExecutor.execute(this);
        this._actionExecutor.register(idx, handler);
    }

    public void unreigsterActionHandler(int idx) {
        if (this._actionExecutor != null)
            this._actionExecutor.unregister(idx);
    }

    public boolean hasAction(int idx) {
        return (this._actionExecutor != null) ? this._actionExecutor.hasAction(idx) : false;
    }

    public void setTemporaryEffect(ServerBasePacket pck) {
        clearTemporaryEffect();
        this._tempEffect = pck;
    }

    public ServerBasePacket getTemporaryEffect() {
        return this._tempEffect;
    }

    public void clearTemporaryEffect() {
        if (this._tempEffect != null) {
            this._tempEffect.clear();
            this._tempEffect = null;
        }
    }

    public void setAttendanceData(SC_ATTENDANCE_USER_DATA_EXTEND attendance_data) {
        this._attendance_data = attendance_data;
    }

    public SC_ATTENDANCE_USER_DATA_EXTEND getAttendanceData() {
        return this._attendance_data;
    }

    public boolean isOnTargetEffect() {
        return this._isOnTargetEffect;
    }

    public void setOnTargetEffect(boolean b) {
        this._isOnTargetEffect = b;
    }

    public void setLastTopBless(Timestamp ts) {
        this._lastTopBless = ts;
    }

    public Timestamp getLastTopBless() {
        return this._lastTopBless;
    }

    public BQSCharacterData getBqs() {
        return this._bqsData;
    }

    public void setBqs(BQSCharacterData bqs) {
        this._bqsData = bqs;
    }

    public void set_instance_status(MJEPcStatus instance_status) {
        this._instance_status = instance_status;
    }

    public MJEPcStatus get_instance_status() {
        return this._instance_status;
    }

    public boolean is_none() {
        return this._instance_status.equals(MJEPcStatus.NONE);
    }

    public boolean is_world() {
        return this._instance_status.equals(MJEPcStatus.WORLD);
    }

    public boolean is_combat_field() {
        return this._instance_status.equals(MJEPcStatus.COMBAT_FIELD);
    }

    public void set_current_combat_id(int current_combat_id) {
        this._current_combat_id = current_combat_id;
    }

    public int get_current_combat_id() {
        return this._current_combat_id;
    }

    public void set_current_combat_team_id(int current_combat_team_id) {
        this._current_combat_team_id = current_combat_team_id;
    }

    public int get_current_combat_team_id() {
        return this._current_combat_team_id;
    }

    public boolean is_non_action() {
        return this._is_non_action;
    }

    public void set_is_non_action(boolean b) {
        this._is_non_action = b;
    }

    public void set_mark_status(int mark_status) {
        this._mark_status = mark_status;
    }

    public int get_mark_status() {
        return this._mark_status;
    }

    public MJCaptcha get_captcha() {
        return this._captcha;
    }

    public MJCaptcha create_captcha() {
        return this._captcha = MJCaptcha.newInstance(getId());
    }

    public void set_lateral_damage(int lateral_damage) {
        this._lateral_damage = lateral_damage;
    }

    public void add_lateral_damage(int lateral_damage) {
        this._lateral_damage += lateral_damage;
    }

    public int get_lateral_damage() {
        return this._lateral_damage;
    }

    public void set_lateral_reduction(int lateral_reduction) {
        this._lateral_reduction = lateral_reduction;
    }

    public void add_lateral_reduction(int lateral_reduction) {
        this._lateral_reduction += lateral_reduction;
    }

    public int get_lateral_reduction() {
        return this._lateral_reduction;
    }

    public void set_lateral_magic_rate(int lateral_magic_rate) {
        this._lateral_magic_rate = lateral_magic_rate;
    }

    public void add_lateral_magic_rate(int lateral_magic_rate) {
        this._lateral_magic_rate += lateral_magic_rate;
    }

    public int get_lateral_magic_rate() {
        return this._lateral_magic_rate;
    }

    public void load_lateral_status() {
        Selector.exec("select * from tb_lateral_status where character_id=?", new SelectorHandler() {
            public void handle(PreparedStatement pstm) throws Exception {
                pstm.setInt(1, L1PcInstance.this.getId());
            }

            public void result(ResultSet rs) throws Exception {
                while (rs.next()) {
                    L1PcInstance.this.add_lateral_damage(rs.getInt("lateral_damage"));
                    L1PcInstance.this.add_lateral_reduction(rs.getInt("lateral_reduction"));
                    L1PcInstance.this.add_lateral_magic_rate(rs.getInt("lateral_magic_rate"));
                }
            }
        });
    }

    public void delete_lateral_status() {
        set_lateral_damage(0);
        set_lateral_reduction(0);
        set_lateral_magic_rate(0);
        Updator.exec("delete from tb_lateral_status where character_id=?", new Handler() {
            public void handle(PreparedStatement pstm) throws Exception {
                pstm.setInt(1, L1PcInstance.this.getId());
            }
        });
    }

    public void update_lateral_status() {
        Updator.exec("insert into tb_lateral_status set character_id=?, lateral_damage=?, lateral_reduction=?, lateral_magic_rate=? on duplicate key update  lateral_damage=?, lateral_reduction=?, lateral_magic_rate=?", new Handler() {
            public void handle(PreparedStatement pstm) throws Exception {
                int idx = 0;
                pstm.setInt(++idx, L1PcInstance.this.getId());
                pstm.setInt(++idx, L1PcInstance.this.get_lateral_damage());
                pstm.setInt(++idx, L1PcInstance.this.get_lateral_reduction());
                pstm.setInt(++idx, L1PcInstance.this.get_lateral_magic_rate());
                pstm.setInt(++idx, L1PcInstance.this.get_lateral_damage());
                pstm.setInt(++idx, L1PcInstance.this.get_lateral_reduction());
                pstm.setInt(++idx, L1PcInstance.this.get_lateral_magic_rate());
            }
        });
    }

    public DungeonTimeUserInformation get_dungeon_information() {
        return this._dtInfo;
    }

    public DungeonTimeProgress<?> get_progress(DungeonTimeInformation dtInfo) {
        return this._dtInfo.get_progress(dtInfo);
    }

    public void dec_dungeon_progress(DungeonTimeInformation dtInfo) {
        this._dtInfo.dec_dungeon_progress(this, dtInfo);
    }

    public Collection<DungeonTimeProgress<?>> get_character_progresses() {
        return this._dtInfo.get_character_progresses().values();
    }

    public Collection<DungeonTimeProgress<?>> get_account_progresses() {
        return this._dtInfo.get_account_progresses().values();
    }

    public void put_dungeon_progress(int timer_id, AccountTimeProgress progress) {
        this._dtInfo.put_dungeon_progress(timer_id, progress);
    }

    public void put_dungeon_progress(int timer_id, CharacterTimeProgress progress) {
        this._dtInfo.put_dungeon_progress(timer_id, progress);
    }

    public void send_dungeon_progress(DungeonTimeInformation dtInfo) {
        send_dungeon_progress(dtInfo, true);
    }

    public void send_dungeon_progress(DungeonTimeInformation dtInfo, boolean send) {
        this._dtInfo.send_dungeon_progress(this, dtInfo, send);
    }

    public void initialize_dungeon_progress() {
        this._dtInfo.initialize();
    }

    public void on_select_item(S_ItemExSelectPacket pck) {
        final int id = pck.get_id();
        this._select_item = pck;
        sendPackets((ServerBasePacket)pck);
        GeneralThreadPool.getInstance().schedule(new Runnable() {
            public void run() {
                S_ItemExSelectPacket pck = L1PcInstance.this._select_item;
                if (pck != null && pck.get_id() == id) {
                    L1PcInstance.this._select_item = null;
                    pck.dispose();
                }
            }
        },  20000L);
    }

    public S_ItemExSelectPacket get_select_item() {
        S_ItemExSelectPacket pck = this._select_item;
        this._select_item = null;
        return pck;
    }

    public boolean is_ranking_buff() {
        return (hasSkillEffect(3521) || hasSkillEffect(3522) || hasSkillEffect(3523) || hasSkillEffect(3524) || hasSkillEffect(3525) || hasSkillEffect(3526) || hasSkillEffect(3527) || hasSkillEffect(3528) || hasSkillEffect(3529) || hasSkillEffect(3530) || hasSkillEffect(3531) || hasSkillEffect(3532) || hasSkillEffect(3533) || hasSkillEffect(3534) || hasSkillEffect(3535) || hasSkillEffect(3536));
    }

    public boolean is_top_ranker() {
        return (hasSkillEffect(3528) || hasSkillEffect(3529) || hasSkillEffect(3530) || hasSkillEffect(3531) || hasSkillEffect(3532) || hasSkillEffect(3533) || hasSkillEffect(3534) || hasSkillEffect(3535) || hasSkillEffect(3536));
    }

    public boolean isPacketSendOK() {
        if (getNetConnection() == null || getAccount() == null)
            return false;
        if (!this.isWorld)
            return false;
        if (getAccountName().equals("") || getAccountName().equals("))
        return false;
        if (is())
            return false;
        if (isPrivateShop())
            return false;
        if (getAI() != null)
            return false;
        if (getOnlineStatus() == 0)
            return false;
        return true;
    }

    public long getlastSavedTime() {
        return this.lastSavedTime;
    }

    public long getlastSavedTime_inventory() {
        return this.lastSavedTime_inventory;
    }

    public long getlastSavedTime_exp() {
        return this.lastSavedTime_exp;
    }

    public long getlastSavedTime_Fatigue() {
        return this.lastSavedTime_Fatigue;
    }

    public void setlastSavedTime(long stime) {
        this.lastSavedTime = stime;
    }

    public void setlastSavedTime_inventory(long stime) {
        this.lastSavedTime_inventory = stime;
    }

    public void setlastSavedTime_exp(long stime) {
        this.lastSavedTime_exp = stime;
    }

    public void setlastSavedTime_Fatigue(long stime) {
        this.lastSavedTime_Fatigue = stime;
    }

    public int getLotto() {
        return this._lotto;
    }

    public void setLotto(int i) {
        this._lotto = i;
    }

    public long getPostDelay() {
        return this._PostDelay;
    }

    public void setPostDelay(long i) {
        this._PostDelay = i;
    }

    public void addPassive(MJPassiveInfo pInfo) {
        if (this.m_passives == null)
            this.m_passives = new HashMap<>();
        this.m_passives.put(Integer.valueOf(pInfo.getPassiveId()), pInfo);
        if (pInfo.getPassiveId() == MJPassiveID.AURA_PASSIVE.toInt()) {
            if (_aura != null) {
                _aura.auraStop();
                _aura = null;
            }
            _aura = new L1Aura(getName());
            GeneralThreadPool.getInstance().schedule(_aura, 5000L);
        }
    }

    public ArrayList<MJPassiveInfo> getPassives() {
        if (this.m_passives == null)
            return null;
        return new ArrayList<>(this.m_passives.values());
    }

    public MJPassiveInfo getPassive(int passiveId) {
        if (this.m_passives == null)
            return null;
        return this.m_passives.get(Integer.valueOf(passiveId));
    }

    public MJPassiveInfo delPassive(int passiveId) {
        if (this.m_passives == null)
            return null;
        return this.m_passives.remove(Integer.valueOf(passiveId));
    }

    public boolean isPassive(int passiveId) {
        return (getPassive(passiveId) != null);
    }

    public int getClanBuffMap() {
        return this._ClanBuffMap;
    }

    public void setClanBuffMap(int i) {
        this._ClanBuffMap = i;
    }

    public void setLastMoveActionMillis(long lastMoveActionMillis) {
        this._lastMoveActionMillis = lastMoveActionMillis;
    }

    public long getLastMoveActionMillis() {
        return this._lastMoveActionMillis;
    }

    public int getEinhasadBlessper() {
        AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(getId());
        int special_point = 0;
        if (Info != null)
            special_point = CalcStat.calcAinhasadStatFirst(Info.get_bless());
        int sum = 0;
        int einblesser = this._EinhasadBlessper;
        int pcCafe = isPcBuff() ? 10 : 0;
        sum = einblesser + special_point + pcCafe;
        return sum;
    }

    public void setEinhasadBlessper(int einhasadblessper) {
        this._EinhasadBlessper = einhasadblessper;
        if (this.noPlayerCK || getAI() != null)
            return;
        SC_REST_EXP_INFO_NOTI.send(this);
    }

    public void addEinhasadBlessper(int i) {
        this._EinhasadBlessper += i;
        if (this.noPlayerCK || getAI() != null)
            return;
        SC_REST_EXP_INFO_NOTI.send(this);
    }

    public MJEShiftObjectType get_shift_type() {
        return (this._netConnection == null) ? MJEShiftObjectType.NONE : this._netConnection.get_shift_type();
    }

    public void set_shift_type(MJEShiftObjectType shift_type) {
        if (this._netConnection != null)
            this._netConnection.set_shift_type(shift_type);
    }

    public boolean is_shift_client() {
        return (this._netConnection != null && this._netConnection.is_shift_client());
    }

    public boolean is_shift_transfer() {
        return (this._netConnection == null) ? false : this._netConnection.is_shift_transfer();
    }

    public boolean is_shift_battle() {
        return (this._netConnection == null) ? false : this._netConnection.is_shift_battle();
    }

    public boolean is_ready_server_shift() {
        return this.m_is_ready_server_shift;
    }

    public void set_ready_server_shift(boolean is_ready_server_shift) {
        this.m_is_ready_server_shift = is_ready_server_shift;
    }

    public MJShiftBattleCharacterInfo get_battle_info() {
        return this.m_battle_info;
    }

    public void set_battle_info(MJShiftBattleCharacterInfo bInfo) {
        this.m_battle_info = bInfo;
    }

    public String get_server_description() {
        return (this._netConnection == null) ? "" : this._netConnection.get_server_description();
    }

    public String get_server_identity() {
        return (this._netConnection == null) ? "" : this._netConnection.get_server_identity();
    }

    public void set_companion(MJCompanionInstance companion) {
        if (companion != null)
            remove_companion();
        this.m_companion = companion;
    }

    public MJCompanionInstance get_companion() {
        return this.m_companion;
    }

    public void remove_companion() {
        if (this.m_companion != null) {
            this.m_companion.deleteMe();
            this.m_companion = null;
        }
    }

    public void send_pink_name(int remain_seconds) {
        S_PinkName pnk = new S_PinkName(getId(), remain_seconds);
        sendPackets((ServerBasePacket)pnk, false);
        if (!isGmInvis())
            broadcastPacket((ServerBasePacket)pnk, false);
        pnk.clear();
    }

    public double get_item_exp_bonus() {
        AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(getId());
        int special_point = 0;
        if (Info != null)
            special_point = CalcStat.calcAinhasadStatSecond(Info.get_bless());
        return this.m_item_exp_bonus + special_point;
    }

    public void set_item_exp_bonus(double item_exp_bonus) {
        this.m_item_exp_bonus = item_exp_bonus;
    }

    public void add_item_exp_bonus(double item_exp_bonus) {
        this.m_item_exp_bonus += item_exp_bonus;
        SC_EXP_BOOSTING_INFO_NOTI.send(this);
    }

    public void set_is_using_items(boolean is_using_items) {
        this.m_is_using_items = is_using_items;
    }

    public boolean get_is_using_items() {
        return this.m_is_using_items;
    }

    public boolean get_is_client_auto() {
        return this.m_is_client_auto;
    }

    public void set_is_client_auto(boolean is_client_auto) {
        this.m_is_client_auto = is_client_auto;
    }

    public boolean hasClientAutoItemRatePenalty() {
        if (!get_is_client_auto())
            return false;
        if (getInventory().checkItem(4100121))
            return false;
        if (getInventory().checkItem(4100529) && getLevel() <= Config.ServerAdSetting.NEWPLAYERLEVELPROTECTION)
            return false;
        return true;
    }

    public void do_finish_client_auto(SC_FORCE_FINISH_PLAY_SUPPORT_NOTI.eReason reason) {
        set_is_client_auto(false);
        SC_FORCE_FINISH_PLAY_SUPPORT_NOTI noti = SC_FORCE_FINISH_PLAY_SUPPORT_NOTI.newInstance();
        noti.set_reason(reason);
        noti.set_remain_time(0);
        sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_FORCE_FINISH_PLAY_SUPPORT_NOTI, true);
        set_client_auto_type(0);
    }

    public void do_finish_client_auto_ack() {
        set_is_client_auto(false);
        SC_FINISH_PLAY_SUPPORT_ACK ack = SC_FINISH_PLAY_SUPPORT_ACK.newInstance();
        ack.set_remain_time(0);
        DungeonTimeInformation dInfo = DungeonTimeInformationLoader.getInstance().from_map_id(getMapId());
        if (dInfo != null) {
            DungeonTimeProgress<?> progress = get_dungeon_information().get_progress(dInfo);
            if (progress != null)
                ack.set_remain_time(progress.dec_remain_seconds());
        }
        sendPackets((MJIProtoMessage)ack, MJEProtoMessages.SC_FINISH_PLAY_SUPPORT_ACK, true);
        set_client_auto_type(0);
    }

    public void do_start_client_auto_ack(int type) {
        SC_START_PLAY_SUPPORT_ACK ack = SC_START_PLAY_SUPPORT_ACK.newInstance();
        ack.set_result(SC_START_PLAY_SUPPORT_ACK.eResult.VALID);
        sendPackets((MJIProtoMessage)ack, MJEProtoMessages.SC_START_PLAY_SUPPORT_ACK);
        set_is_client_auto(true);
        set_client_auto_type(type);
    }

    public int get_client_auto_type() {
        return this._client_auto_type;
    }

    public void set_client_auto_type(int i) {
        this._client_auto_type = i;
    }

    public boolean is_apply_tam() {
        return (hasSkillEffect(7791) || hasSkillEffect(7792) || hasSkillEffect(7793) || hasSkillEffect(7794) || hasSkillEffect(7795));
    }

    public void on_tam_ended() {
        if (is_apply_tam())
            return;
        DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_timer_id(9);
        if (dtInfo == null) {
            System.out.println(String.format(": %d", new Object[] { Integer.valueOf(9) }));
            return;
        }
        DungeonTimeProgress<?> progress = get_progress(dtInfo);
        if (progress == null)
            return;
        progress.set_remain_seconds(dtInfo.get_amount_seconds());
        DungeonTimeProgressLoader.update(progress);
        if (5490 == getMapId())
            send_dungeon_progress(dtInfo);
    }

    public void setSkillEffect(int skillId, long timeMillis) {
        super.setSkillEffect(skillId, timeMillis);
        if (!L1SkillId.is_tam_buff(skillId))
            return;
        DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_timer_id(9);
        if (dtInfo == null) {
            System.out.println(String.format(": %d", new Object[] { Integer.valueOf(9) }));
            return;
        }
        DungeonTimeProgress<?> progress = get_progress(dtInfo);
        if (progress == null)
            return;
        int map_id = getMapId();
        int remain = 86400 - progress.get_remain_seconds();
        progress.set_remain_seconds(remain + progress.get_remain_seconds());
        DungeonTimeProgressLoader.update(progress);
        if (5490 == map_id)
            send_dungeon_progress(dtInfo);
    }

    public void load_private_probability() {
        final HashMap<Integer, Integer> probabilities = new HashMap<>();
        Selector.exec("select * from characters_private_probability where object_id=?", new SelectorHandler() {
            public void handle(PreparedStatement pstm) throws Exception {
                pstm.setInt(1, L1PcInstance.this.getId());
            }

            public void result(ResultSet rs) throws Exception {
                while (rs.next())
                    probabilities.put(Integer.valueOf(rs.getInt("skill_id")), Integer.valueOf(rs.getInt("probability")));
            }
        });
        this.m_private_porbability = probabilities;
    }

    public void add_private_probability(final int skill_id, final int probability) {
        if (this.m_private_porbability != null)
            this.m_private_porbability.put(Integer.valueOf(skill_id), Integer.valueOf(probability));
        Updator.exec("insert into characters_private_probability set object_id=?, skill_id=?, probability=? on duplicate key update skill_id=?, probability=?", new Handler() {
            public void handle(PreparedStatement pstm) throws Exception {
                int idx = 0;
                pstm.setInt(++idx, L1PcInstance.this.getId());
                pstm.setInt(++idx, skill_id);
                pstm.setInt(++idx, probability);
                pstm.setInt(++idx, skill_id);
                pstm.setInt(++idx, probability);
            }
        });
    }

    public int get_private_probability(int skill_id) {
        return (this.m_private_porbability != null && this.m_private_porbability.containsKey(Integer.valueOf(skill_id))) ? ((Integer)this.m_private_porbability.get(Integer.valueOf(skill_id))).intValue() : 0;
    }

    public void truncate_private_probability() {
        if (this.m_private_porbability != null)
            this.m_private_porbability.clear();
    }

    public boolean isStatReset() {
        return this._is_stat_reset;
    }

    public void setStatReset(boolean flag) {
        this._is_stat_reset = flag;
    }

    public String to_shop_title() {
        byte[] b = getShopChat();
        if (b != null)
            try {
                return new String(b, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        return null;
    }

    public byte[] sendItemPacket(L1PcInstance pc, L1ItemInstance item) {
        BinaryOutputStream os = new BinaryOutputStream();
        os.writeC(8);
        os.writeBit(item.getId());
        os.writeC(16);
        os.writeBit((item.getItem().getItemDescId() == 0) ? -1L : item.getItem().getItemDescId());
        os.writeC(24);
        os.writeBit(item.getItem().getItemId());
        os.writeC(32);
        os.writeBit(item.getCount());
        os.writeC(40);
        int use_type = item.getItem().getUseType();
        os.writeBit(use_type);
        if (item.getChargeCount() > 0) {
            os.writeC(48);
            os.writeBit(item.getChargeCount());
        }
        os.writeC(56);
        os.writeBit(item.get_gfxid());
        os.writeC(64);
        os.writeBit(item.getBless());
        os.writeC(80);
        os.writeBit(0L);
        os.writeC(88);
        os.writeBit(0L);
        os.writeC(96);
        os.writeBit(item.getItem().getType2());
        if (item.getItem().getType2() != 0) {
            os.writeC(104);
            os.writeBit(item.getEnchantLevel());
        }
        os.writeC(112);
        os.writeBit(2L);
        if (item.getAttrEnchantLevel() > 0) {
            int attrenchant = L1ItemInstance.attrEnchantToElementalType(item.getAttrEnchantLevel());
            os.writeBit(128L);
            os.writeBit(attrenchant);
            os.writeBit(136L);
            os.writeBit(attrenchant);
        }
        int size = (item.getViewName().getBytes()).length;
        os.writeBit(146L);
        os.writeBit(size);
        os.writeByte(item.getViewName().getBytes());
        if (item.isIdentified()) {
            os.writeBit(154L);
            byte[] status = item.getStatusBytes();
            os.writeBit(status.length);
            os.writeByte(status);
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.getBytes();
    }

    public int get_class_level_pvp_reduction() {
        int level = getLevel();
        if (level < 60)
            return 0;
        if (level == 60)
            return 1;
        int bonus_level = level - 60;
        switch (getType()) {
            case 0:
            case 2:
            case 3:
                return bonus_level / 4 + 1;
            case 1:
                return bonus_level / 2 + 1;
            case 4:
            case 5:
            case 6:
            case 7:
                return bonus_level / 3 + 1;
        }
        return 0;
    }

    public int get_pvp_dmg_ignore() {
        return this.pvp_dmg_ignore;
    }

    public void set_pvp_dmg_ignore(int i) {
        this.pvp_dmg_ignore = i;
    }

    public void add_pvp_dmg_ignore(int i) {
        this.pvp_dmg_ignore += i;
    }

    public int get_pvp_mdmg() {
        return this.pvp_mdmg;
    }

    public void set_pvp_mdmg(int i) {
        this.pvp_mdmg = i;
    }

    public void add_pvp_mdmg(int i) {
        this.pvp_mdmg += i;
    }

    public int get_pvp_mdmg_ignore() {
        return this.pvp_mdmg_ignore;
    }

    public void set_pvp_mdmg_ignore(int i) {
        this.pvp_mdmg_ignore = i;
    }

    public void add_pvp_mdmg_ignore(int i) {
        this.pvp_mdmg_ignore += i;
    }

    public int get_immune_ignore() {
        return this.immune_ignore;
    }

    public void set_immune_ignore(int i) {
        this.immune_ignore = i;
    }

    public void add_immune_ignore(int i) {
        this.immune_ignore += i;
    }

    public int get_immune_minus_per() {
        return this.immune_minus_per;
    }

    public void set_immune_minus_per(int i) {
        this.immune_minus_per = i;
    }

    public void add_immune_minus_per(int i) {
        this.immune_minus_per += i;
    }

    public int get_Moebius_ignore() {
        return this.Moebius_ignore;
    }

    public void set_Moebius_ignore(int i) {
        this.Moebius_ignore = i;
    }

    public void on_regeneration() {
        this._regenerator = MJReGenerator.newInstance(this);
    }

    public MJRegeneratorLatestActions get_latest_action() {
        return (this._regenerator == null) ? MJRegeneratorLatestActions.LATEST_ACTION_STANDING : this._regenerator.get_latest_action();
    }

    public void set_latest_action(MJRegeneratorLatestActions latest_action) {
        if (this._regenerator != null)
            this._regenerator.update_latest_action(latest_action);
    }

    public void run_regenerator() {
        if (this._regenerator != null)
            this._regenerator.set_is_stopped(false);
    }

    public void stop_regenerator() {
        if (this._regenerator != null)
            this._regenerator.set_is_stopped(true);
    }

    public void dispose_regenerator() {
        if (this._regenerator != null) {
            this._regenerator.dispose();
            this._regenerator = null;
        }
    }

    public void polyTrigger(PolyTrigger polyTrigger) {
        this.polyTrigger = polyTrigger;
    }

    public PolyTrigger polyTrigger() {
        return this.polyTrigger;
    }

    public MJAttrMap attribute() {
        return this.attribute;
    }

    public void attributeInitialized() {}

    public MJObjectEventHandler eventHandler() {
        return this.eventHandler;
    }

    public void eventHandlerInitialized() {}

    public void Stat_Reset_Str(boolean base_stat) {
        SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2, getType());
        SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
        sendPackets((ServerBasePacket)new S_Weight(this));
        sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
        if (base_stat) {
            resetBaseHitup();
            resetBaseDmgup();
        }
    }

    public void Stat_Reset_Dex(boolean base_stat) {
        SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2, getType());
        SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
        sendPackets((ServerBasePacket)new S_PacketBox(132, getTotalER()));
        sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
        sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
        resetBaseAc();
        if (base_stat) {
            resetBaseHitup();
            resetBaseDmgup();
        }
    }

    public void Stat_Reset_Con() {
        SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2, getType());
        SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
        sendPackets((ServerBasePacket)new S_Weight(this));
        sendPackets((ServerBasePacket)new S_HPUpdate(this));
    }

    public void Stat_Reset_Int() {
        SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2, getType());
        SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
        sendPackets((ServerBasePacket)new S_SPMR(this));
        resetOriginalMagicHit();
    }

    public void Stat_Reset_Wis() {
        SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2, getType());
        SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
        sendPackets((ServerBasePacket)new S_SPMR(this));
        resetBaseMr();
    }

    public void Stat_Reset_Cha() {
        SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2, getType());
        SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
    }

    public void ShapePolyRingMaster() {
        sendPackets((ServerBasePacket)new S_Message_YN(getId(), 180, ""));
    }

    public void () {
        L1SkillUse l1skilluse = new L1SkillUse();
        l1skilluse.handleCommands(this, 44, getId(), getX(), getY(), null, 0, 1);
        if (getWeapon() != null)
            getInventory().setEquipped(getWeapon(), false, false, false, false);
        for (L1ItemInstance armor : getInventory().getItems()) {
            if (armor != null && armor.isEquipped())
                getInventory().setEquipped(armor, false, false, false, false);
        }
        sendPackets((ServerBasePacket)new S_CharVisualUpdate(this));
        sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
        sendPackets((ServerBasePacket)new S_Paralysis(7, false));
        setReturnStat(get_exp());
        sendPackets((ServerBasePacket)new S_SPMR(this));
        sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
        sendPackets((ServerBasePacket)new S_OwnCharStatus2(this));
        sendPackets((ServerBasePacket)new S_ReturnedStat(this, 1));
        try {
            save();
        } catch (Exception e) {
            System.out.println(");
                    e.printStackTrace();
        }
    }

    public int get_indun_room_num() {
        return this.indun_room_num;
    }

    public void set_indun_room_num(int i) {
        this.indun_room_num = i;
    }

    public MJIndunRoomModel indun_model() {
        return this.indun_model;
    }

    public void set_indun_model(MJIndunRoomModel model) {
        this.indun_model = model;
    }

    public void setEquipmentChangeItem(L1ItemInstance targetItem) {
        this.equipmentChangeItem = targetItem;
    }

    public L1ItemInstance getEquipmentChangeItem() {
        return this.equipmentChangeItem;
    }

    public void setEquipmentChangeUseItemId(int useItemId) {
        this.equipmentChangeUseItemId = useItemId;
    }

    public int getEquipmentChangeUseItemId() {
        return this.equipmentChangeUseItemId;
    }

    public ArrayList<L1ItemInstance> getEQCList() {
        return this._eqcList;
    }

    public void setEQCList(ArrayList<L1ItemInstance> list) {
        this._eqcList = list;
    }

    public ArrayList<L1ItemInstance> getItemSelectorList() {
        return this._itemselectorlist;
    }

    public void setItemSelectorList(ArrayList<L1ItemInstance> list) {
        this._itemselectorlist = list;
    }

    public boolean isBotWareHouse() {
        return this._bot_warehouse;
    }

    public void setBotWareHouse(boolean flag) {
        this._bot_warehouse = flag;
    }

    public boolean isBotShop() {
        return this._bot_shop;
    }

    public void setBotShop(boolean flag) {
        this._bot_shop = flag;
    }

    public boolean isBotBuff() {
        return this._bot_buff;
    }

    public void setBotBuff(boolean flag) {
        this._bot_buff = flag;
    }

    public boolean isBotTeleport() {
        return this._bot_teleport;
    }

    public void setBotTeleport(boolean flag) {
        this._bot_teleport = flag;
    }

    public boolean isBotSuccess() {
        return this._bot_success;
    }

    public void setBotSuccess(boolean flag) {
        this._bot_success = flag;
    }

    public int getJudgementPoint() {
        return this._judgement;
    }

    public void setJudgementPoint(int i) {
        this._judgement = i;
    }

    public boolean isPrime_War_Zone() {
        return this._Prime_war_zone;
    }

    public void setPrime_War_Zone(boolean flag) {
        this._Prime_war_zone = flag;
    }

    public void setIsPrimeCast(boolean f) {
        this._isPrimeCast = f;
    }

    public boolean isPrimeCast() {
        return this._isPrimeCast;
    }

    public int getGlory_Earth_Attr() {
        return this._glory_earth_attr;
    }

    public void setGlory_Earth_Attr(int i) {
        this._glory_earth_attr = i;
    }

    public int getBonusDropNpc() {
        return this._Bonus_drop_npc;
    }

    public void setBonusDropNpc(int value) {
        this._Bonus_drop_npc = value;
    }

    public boolean isDestroy_pier() {
        return this._Destroy_pier;
    }

    public void setDestroy_pier(boolean flag) {
        this._Destroy_pier = flag;
    }

    public boolean isDestroy_horror() {
        return this._Destroy_horror;
    }

    public void setDestroy_horror(boolean flag) {
        this._Destroy_horror = flag;
    }

    public void special_resistance_skill(L1PcInstance pc, int oldlevel, int newlevel, boolean login) {
        int point = 0;
        int point1 = 0;
        int rd_point = 0;
        int rd_point1 = 0;
        int calcPurePierceAll = CalcStat.calcPurePierceAll(getAbility().getCha()) + CalcStat.calcPierceAll(getAbility().getTotalCha());
        if (login) {
            if (isPassive(MJPassiveID.RISING_POSS.toInt())) {
                int RealSteelLevel = pc.getLevel();
                if (RealSteelLevel < 80)
                    RealSteelLevel = 80;
                point = (RealSteelLevel - 80) / 3 + 1;
                pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, point);
                int rd_RealSteelLevel = pc.getLevel();
                if (rd_RealSteelLevel < 80)
                    rd_RealSteelLevel = 80;
                rd_point = (rd_RealSteelLevel - 80) / 4 + 1;
                if (rd_point > 5)
                    rd_point = 5;
                pc.getResistance().addPVPweaponTotalDamage(rd_point);
                SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
            }
        } else if (oldlevel > newlevel) {
            if (isPassive(MJPassiveID.RISING_POSS.toInt())) {
                int RealSteelLevel = pc.getLevel();
                if (RealSteelLevel < 80)
                    RealSteelLevel = 80;
                point1 = (oldlevel - 80) / 3 - (newlevel - 80) / 3 + 1;
                pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, -point1);
                int rd_RealSteelLevel = pc.getLevel();
                if (rd_RealSteelLevel < 80)
                    rd_RealSteelLevel = 80;
                rd_point = (oldlevel - 80) / 4 - (newlevel - 80) / 4 + 1;
                if (rd_point > 5)
                    rd_point = 5;
                pc.getResistance().addPVPweaponTotalDamage(-rd_point);
                SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
            }
        } else if (isPassive(MJPassiveID.RISING_POSS.toInt())) {
            int RealSteelLevel = pc.getLevel();
            if (RealSteelLevel < 80)
                RealSteelLevel = 80;
            point1 = (oldlevel - 80) / 3 - (newlevel - 80) / 3 + 1;
            pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, -point1);
            int rd_RealSteelLevel = pc.getLevel();
            if (rd_RealSteelLevel < 80)
                rd_RealSteelLevel = 80;
            rd_point1 = (oldlevel - 80) / 4 - (newlevel - 80) / 4 + 1;
            if (rd_point1 > 5)
                rd_point1 = 5;
            pc.getResistance().addPVPweaponTotalDamage(-rd_point1);
            SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
        }
        pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, calcPurePierceAll);
        SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
    }

    public void startBloodToSoul() {
        L1PcTimerControlHandler.getInstance().begin(getName(), L1PcTimerControlHandler.TimerType.BLOOD_TO_SOUL);
    }

    public void stopBloodToSoul() {
        L1PcTimerControlHandler.getInstance().stop(getName());
    }

    public void doAsura() {
        L1PcTimerControlHandler.getInstance().begin(getName(), L1PcTimerControlHandler.TimerType.ASURA);
    }

    public void disposeAsura() {
        L1PcTimerControlHandler.getInstance().stop(getName());
    }

    public void setCraftUseType(int type) {
        this._craftUseType = type;
    }

    public int getCraftUseType() {
        return this._craftUseType;
    }

    public void addVirualEinhasad(int i) {
        this._virualEinhasad += i;
    }

    public void setVirualEinhasad(int i) {
        this._virualEinhasad = i;
    }

    public int getVirualEinhasad() {
        return this._virualEinhasad;
    }

    public boolean isErzabe_circle() {
        return this._Erzabe_circle;
    }

    public void setErzabe_circle(boolean flag) {
        this._Erzabe_circle = flag;
    }

    public void setConsole_type(int type) {
        this._console_type = type;
    }

    public int getConsole_type() {
        return this._console_type;
    }

    public void setdoll_judgement_type(int type) {
        this._doll_judgement_type = type;
    }

    public int getdoll_judgement_type() {
        return this._doll_judgement_type;
    }

    public void isEnd_Tel_xym(boolean Teleport) {
        isInsert_Tel_xym(0, 0, 0, false);
    }

    public void isInsert_Tel_xym(int x, int y, int map, boolean Teleport) {
        try {
            if (this._Insert_Tel_xym == null) {
                this._Insert_Tel_xym[0] = 33439 + CommonUtil.random(10);
                this._Insert_Tel_xym[1] = 32804 + CommonUtil.random(10);
                this._Insert_Tel_xym[2] = 4;
            } else if (!Teleport) {
                start_teleport(this._Insert_Tel_xym[0], this._Insert_Tel_xym[1], this._Insert_Tel_xym[2], getHeading(), 18339, false);
            } else {
                this._Insert_Tel_xym[0] = getX();
                this._Insert_Tel_xym[1] = getY();
                this._Insert_Tel_xym[2] = getMapId();
                start_teleport(x, y, map, getHeading(), 18339, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getMacroList() {
        return this._macro_list;
    }

    public void addMacroList(String ment) {
        this._macro_list.add(ment);
    }

    public void getMacroListIdentify() {
        this._macro_list.forEach(ment -> sendPackets((ServerBasePacket)new S_SystemMessage(this._macro_list.indexOf(ment) + ". " + ment)));
    }

    public boolean isMacroTimerStart() {
        return (this._macro_timer != null);
    }

    public void startMacroTimer() {
        synchronized (this) {
            if (this._macro_timer != null)
                return;
            if (this._macro_list.size() == 0)
                return;
            this._macro_timer = new Macro(10000L, this._macro_list);
            GeneralThreadPool.getInstance().execute(this._macro_timer);
        }
    }

    public void stopMacroTimer() {
        synchronized (this) {
            if (this._macro_timer == null)
                return;
            this._macro_timer.cancel();
            this._macro_timer = null;
        }
    }

    private class Macro implements Runnable {
        private boolean _active;

        private long _interval;

        public ArrayList<String> _list;

        public void run() {
            try {
                for (int i = 0; i < this._list.size() && this._active; i++) {
                    for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
                        L1ExcludingList spamList15 = SpamTable.getInstance().getExcludeTable(listner.getId());
                        if (!spamList15.contains(0, L1PcInstance.this.getName()) && listner.isShowWorldChat())
                            listner.sendPackets((ServerBasePacket)new S_NewChat(3, this._list.get(i), i, L1PcInstance.this));
                    }
                    Thread.sleep(10000L);
                }
                GeneralThreadPool.getInstance().schedule(this, this._interval);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Macro(long interval, ArrayList<String> list) {
            this._interval = interval;
            this._list = list;
            this._active = true;
        }

        public void cancel() {
            this._active = false;
        }
    }

    public int getSpeedOverCount() {
        return this._move_speed_over_count;
    }

    public void addSpeedOverCount() {
        this._move_speed_over_count++;
    }

    public void delSpeedOverCount() {
        if (this._move_speed_over_count == 0) {
            this._move_speed_over_count = 0;
        } else {
            this._move_speed_over_count--;
        }
    }

    public double getAdenBonus() {
        return this._AdenBonus;
    }

    public void setAdenBonus(double i) {
        this._AdenBonus = i;
    }

    public void addAdenBonus(double i) {
        this._AdenBonus += i;
    }

    public double getItemBonus() {
        return this._ItemBonus;
    }

    public void setItemBonus(double i) {
        this._ItemBonus = i;
    }

    public void addItemBonus(double i) {
        this._ItemBonus += i;
    }

    public int get_pvp_defense_per() {
        return this.pvp_defense_per;
    }

    public void set_pvp_defense_per(int i) {
        this.pvp_defense_per = i;
    }

    public void add_pvp_defense_per(int i) {
        this.pvp_defense_per += i;
    }

    public int get_Magic_defense_per() {
        return this.Magic_defense_per;
    }

    public void set_Magic_defense_per(int i) {
        this.Magic_defense_per = i;
    }

    public void add_Magic_defense_per(int i) {
        this.Magic_defense_per += i;
    }

    public int getInfinityTeamId() {
        return this._infinity_team_id;
    }

    public void setInfinityTeamId(int i) {
        this._infinity_team_id = i;
    }

    public long getClanContribution() {
        return this._clan_contribution;
    }

    public void setClanContribution(long i) {
        this._clan_contribution = i;
    }

    public void addClanContribution(int i) {
        this._clan_contribution += (i * Config.ServerAdSetting.CLAN_CONTRIBUTION);
        if (this._clan_contribution > 10000000L) {
            this._clan_contribution = 10000000L;
        } else if (this._clan_contribution < 0L) {
            this._clan_contribution = 0L;
        }
    }

    public void setClassRankBlessTime(Timestamp ts) {
        this._class_rank_bless = ts;
    }

    public Timestamp getClassRankBlessTime() {
        return this._class_rank_bless;
    }

    public int getCustomQuestId() {
        return this.customQuestId;
    }

    public void setCustomQuestId(int getCunstomQuestId) {
        this.customQuestId = getCunstomQuestId;
    }

    public int getCustomQuestNpcObjId() {
        return this.customQuestNpcObjId;
    }

    public void setCustomQuestNpcObjId(int customQuestNpcObjId) {
        this.customQuestNpcObjId = customQuestNpcObjId;
    }

    public void addCustomQuest(int questid, CustomQuestUser quest) {
        if (!this.customQuestList.containsKey(Integer.valueOf(questid)))
            this.customQuestList.put(Integer.valueOf(questid), quest);
    }

    public void addCustomQuest(int questid, eCustomQuestType type) {
        if (!this.customQuestList.containsKey(Integer.valueOf(questid)))
            this.customQuestList.put(Integer.valueOf(questid), new CustomQuestUser(questid, type));
    }

    public void removeCustomQuest(int questid) {
        if (this.customQuestList.containsKey(Integer.valueOf(questid)))
            this.customQuestList.remove(Integer.valueOf(questid));
    }

    public void addCustomSuccessCount(int questid, int count) {
        if (this.customQuestList.containsKey(Integer.valueOf(questid)))
            ((CustomQuestUser)this.customQuestList.get(Integer.valueOf(questid))).setSuccessCount(count);
    }

    public Map<Integer, CustomQuestUser> getCustomQuestList() {
        return this.customQuestList;
    }

    public CustomQuestUser getCustomQuestUser(int questid) {
        return this.customQuestList.get(Integer.valueOf(questid));
    }

    public void call_clan_advence(L1PcInstance target) {
        L1Location loc = new L1Location();
        int locX = getX();
        int locY = getY();
        int heading = getHeading();
        loc.setMap(getMapId());
        switch (heading) {
            case 1:
                locX++;
                locY--;
                break;
            case 2:
                locX++;
                break;
            case 3:
                locX++;
                locY++;
                break;
            case 4:
                locY++;
                break;
            case 5:
                locX--;
                locY++;
                break;
            case 6:
                locX--;
                break;
            case 7:
                locX--;
                locY--;
                break;
            case 0:
                locY--;
                break;
        }
        loc.setX(locX);
        loc.setY(locY);
        target.send_effect(19583);
        L1Teleport.getInstance().doTeleport(target, locX, locY, getMapId());
    }

    public boolean isSpearModeType() {
        return this._spear_mode_type;
    }

    public void setSpear_Mode_Type(boolean type) {
        this._spear_mode_type = type;
    }

    public boolean getVanguardType() {
        return this._vanguard_type;
    }

    public void setVanguardType(boolean flag) {
        this._vanguard_type = flag;
    }

    public static final MJAttrKey<ArrayList<CPMWBQinfo>> pcbookquestinfo = MJAttrKey.newInstance("cpmw-pc-bookqeust-model");

    public double _attack_delay_checker;

    public int _attack_delay_count;

    public long _slotsavetime;

    public int _magic_add_count;

    public int _divine_protection;

    private long _lastMotionMillis;

    private boolean _empire_overlord;

    private boolean _titan_berserk;

    private boolean _titan_beast;

    private ArrayList<L1Character> _TitanBeastChaList;

    private int _emblem;

    private int _MagicHit;

    public int Get_BQ_Size() {
        return ((ArrayList)attribute().getNotExistsNew(pcbookquestinfo).get()).size();
    }

    public ArrayList<CPMWBQinfo> Get_BQ_Info() {
        return (ArrayList<CPMWBQinfo>)attribute().getNotExistsNew(pcbookquestinfo).get();
    }

    public void Add_BQ_Info(int mapid, int mapdesc, CPMWBQinfo model) {
        model.setMapid(mapid);
        model.setMapdesc(mapdesc);
        model.setMoncount(0);
        model.setIsclear(false);
        ((ArrayList<CPMWBQinfo>)attribute().getNotExistsNew(pcbookquestinfo).get()).add(model);
        MJObjectEventProvider.provider().pcEventFactory().fireCPMWBQAdded(this);
    }

    public void Update_BQ(int index, CPMWBQinfo model, CS_HUNTING_QUEST_REWARD_REQ req) {
        model.setMapid(req.get_map_number());
        model.setMapdesc(req.get_location_desc());
        model.setMoncount(0);
        model.setIsclear(true);
        ((ArrayList<CPMWBQinfo>)attribute().getNotExistsNew(pcbookquestinfo).get()).set(index, model);
    }

    private void StoreBQ() {
        if (getAI() != null || this.noPlayerCK)
            return;
        CPMWBQUserTable Uinfo = CPMWBQUserTable.getInstance();
        for (CPMWBQinfo BQinfo : Get_BQ_Info())
            Uinfo.Update_Info(getId(), BQinfo);
        if (Get_BQ_Info() != null)
            Get_BQ_Info().clear();
    }

    public double getAttackDelayChecker() {
        return this._attack_delay_checker;
    }

    public void setAttackDelayChecker(double value) {
        this._attack_delay_checker = value;
    }

    public int getAttackDelayCount() {
        return this._attack_delay_count;
    }

    public void addAttackDelayCount(int value) {
        this._attack_delay_count += value;
    }

    public void setAttackDelayCount(int value) {
        this._attack_delay_count = value;
    }

    public long get_slotsavetime() {
        return this._slotsavetime;
    }

    public void set_slotsavetime(long _slot) {
        this._slotsavetime = _slot;
    }

    public List<MJDeathPenaltyItemModel> get_deathpenalty_item() {
        return (List<MJDeathPenaltyItemModel>)attribute().getNotExistsNew(deathpenaltyitemModelKey).get();
    }

    public List<MJDeathPenaltyExpModel> get_deathpenalty_exp() {
        return (List<MJDeathPenaltyExpModel>)attribute().getNotExistsNew(deathpenaltyexpModelKey).get();
    }

    public void add_deathpenalty_exp(long exp) {
        int cost = 0;
        int level = getLevel();
        int lawful = getLawful();
        if (level < 45) {
            cost = level * level * level * 50;
        } else {
            cost = level * level * level * 150;
        }
        if (lawful >= 0)
            cost = (int)(cost * 0.7D);
        MJDeathPenaltyExpModel model = new MJDeathPenaltyExpModel();
        model.setOwnerId(getId());
        model.setExp_ratio(50000);
        model.setDeathLevel(getLevel());
        model.setRecovery_cost(cost);
        model.setDelete_time(System.currentTimeMillis() / 1000L + 86400L);
        ((List<MJDeathPenaltyExpModel>)attribute().getNotExistsNew(deathpenaltyexpModelKey).get()).add(model);
        MJDeathPenaltyexpDatabaseLoader.getInstance().update(this, model);
        MJDeathPenaltyexpDatabaseLoader.getInstance().do_Select(this);
    }

    public void delete_deathpenalty_exp(final int i) {
        Updator.exec("delete from characters_deathpenalty_exp where id=?", new Handler() {
            public void handle(PreparedStatement pstm) throws Exception {
                pstm.setInt(1, i);
            }
        });
    }

    public void add_deathpenalty_item(L1ItemInstance item) {
        MJDeathPenaltyItemModel model = new MJDeathPenaltyItemModel();
        model.setAttr_enchantlvl(item.getAttrEnchantLevel());
        model.setBless(item.getBless());
        model.setBless_level(item.get_bless_level());
        model.setCarving(item.get_Carving());
        model.setCharge_count(item.getChargeCount());
        model.setCount(item.getCount());
        model.setDollpoten(item.get_Doll_Bonus_Value());
        model.setDurability(item.get_durability());
        model.setEnchantlvl(item.getEnchantLevel());
        model.setEnd_time(item.getEndTime());
        model.setHotel_Town(item.getHotel_Town());
        model.setIs_id(item.isIdentified() ? 1 : 0);
        model.setItem_id(item.getItemId());
        model.setItem_level(item.get_item_level());
        model.setItemobjid(item.getId());
        model.setLast_used(item.getLastUsed());
        model.setOwnerId(getId());
        model.setRemaining_time(item.getRemainingTime());
        model.setSpecial_enchant(item.getSpecialEnchant());
        model.setDelete_time(System.currentTimeMillis() / 1000L + 86400L);
        ((List<MJDeathPenaltyItemModel>)attribute().getNotExistsNew(deathpenaltyitemModelKey).get()).add(model);
    }

    public List<MJChaPushModel> get_push_info() {
        return (List<MJChaPushModel>)attribute().getNotExistsNew(pcpushmodelkey).get();
    }

    public int get_magic_add_count() {
        return this._magic_add_count;
    }

    public void set_magic_add_count(int count) {
        this._magic_add_count = count;
    }

    public int get_divine_protection() {
        return this._divine_protection;
    }

    public void set_divine_protection(int devine) {
        this._divine_protection = devine;
    }

    public void setLastMotionMillis(long lastMotionMillis) {
        this._lastMotionMillis = lastMotionMillis;
    }

    public long getLastMotionMillis() {
        return this._lastMotionMillis;
    }

    public boolean isEmpireOverlord() {
        return this._empire_overlord;
    }

    public void setEmpireOverlord(boolean value) {
        this._empire_overlord = value;
    }

    public boolean isTitanBerserk() {
        return this._titan_berserk;
    }

    public void setTitanBerserk(boolean value) {
        this._titan_berserk = value;
    }

    public boolean isTitanBeast() {
        return this._titan_beast;
    }

    public void setTitanBeast(boolean value) {
        this._titan_beast = value;
    }

    public ArrayList<L1Character> getTitanBeastChaList() {
        return this._TitanBeastChaList;
    }

    public void addTitanBeastChaList(L1Character cha) {
        if (!this._TitanBeastChaList.contains(cha))
            this._TitanBeastChaList.add(cha);
    }

    public void removeTitanBeastChaList(L1Character cha) {
        if (!this._TitanBeastChaList.contains(cha))
            this._TitanBeastChaList.remove(cha);
    }

    public int getExpEmblem() {
        AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(getId());
        int special_point = 0;
        if (Info != null)
            special_point = CalcStat.calcAinhasadStatSecond(Info.get_bless());
        return this._emblem + special_point;
    }

    public void setExpEmblem(int i) {
        this._emblem = i;
    }

    public void addExpEmblem(int i) {
        this._emblem += i;
    }

    public int getMagicHit() {
        return this._MagicHit;
    }

    public void addMagicHit(int i) {
        this._MagicHit += i;
    }

    private static Timer _ainhasad_re_time = new Timer();

    private AinhasadHpMpRegeneration _ainhasad_Timer;

    public boolean _massTeleportSwitch;

    public Timestamp _einhasdgrace;

    private int _ain_exp_bonus;

    public boolean _dragonbless;

    private L1TimeCollectionHandler _timeCollection;

    private ArrayList<Integer> _inven_bonus_items;

    private ArrayList<Integer> _temp_skill_active;

    private ArrayList<Integer> _temp_skill_passive;

    private boolean _pc_golden_status;

    private int _pc_golden_buff_index0_time;

    private int _pc_golden_buff_index1_time;

    private ArrayList<Integer> _pc_golden_buff;

    private HashMap<Integer, Timestamp> _ainhasad_faith;

    private int _armor_magic_pro;

    private int _status_time_reduce;

    private int _potionRecoveryRate;

    private L1FavorBookInventory _favorBook;

    public boolean _AurakiaCircle;

    private int _threeItemEquipped;

    private L1ItemInstance HalpasArmor;

    private boolean _CastleEffect;

    private boolean _isMassTel;

    private int _ForcePolyId;

    private Timestamp _clanjointime;

    private boolean _tyrant_excute;

    private L1PcInstance _behemoth_attacker;

    private int _behemoth_dmg_sum;

    private boolean _chainsword_expose;

    private int _chainsword_step;

    private int _abnormal_status_pvp_damage_reduction;

    public void startAinhasadTimer(AinhasadSpecialStatInfo Info) {
        if (Info != null && Info.get_restore() > 0 && this._ainhasad_Timer == null) {
            int interval = 32000;
            this._ainhasad_Timer = new AinhasadHpMpRegeneration(this, Info);
            _ainhasad_re_time.scheduleAtFixedRate((TimerTask)this._ainhasad_Timer, interval, interval);
        }
    }

    public void stopAinhasadTimer() {
        if (this._ainhasad_Timer != null) {
            this._ainhasad_Timer.cancel();
            this._ainhasad_Timer = null;
        }
    }

    public Timestamp getEinhasadGraceTime() {
        return this._einhasdgrace;
    }

    public void setEinhasadGraceTime(Timestamp ts) {
        this._einhasdgrace = ts;
    }

    public int getBlessOfAinEfficiency() {
        int efficiency = 0;
        if (getLevel() == 80) {
            efficiency += 5;
        } else if (getLevel() == 81) {
            efficiency += 6;
        } else if (getLevel() == 82) {
            efficiency += 7;
        } else if (getLevel() == 83) {
            efficiency += 8;
        } else if (getLevel() == 84) {
            efficiency += 9;
        } else if (getLevel() == 85) {
            efficiency += 10;
        } else if (getLevel() == 86) {
            efficiency += 12;
        } else if (getLevel() == 87) {
            efficiency += 14;
        } else if (getLevel() == 88) {
            efficiency += 16;
        } else if (getLevel() == 89) {
            efficiency += 18;
        } else if (getLevel() == 90) {
            efficiency += 20;
        } else if (getLevel() == 91) {
            efficiency += 23;
        } else if (getLevel() == 92) {
            efficiency += 26;
        } else if (getLevel() == 93) {
            efficiency += 29;
        } else if (getLevel() == 94) {
            efficiency += 32;
        } else if (getLevel() == 95) {
            efficiency += 35;
        } else if (getLevel() == 96) {
            efficiency += 38;
        } else if (getLevel() == 97) {
            efficiency += 41;
        } else if (getLevel() == 98) {
            efficiency += 44;
        } else if (getLevel() == 99) {
            efficiency += 47;
        } else if (getLevel() == 100) {
            efficiency += 50;
        }
        AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(getId());
        int special_point = 0;
        if (Info != null)
            special_point = CalcStat.calcAinhasadStatFirst(Info.get_bless());
        return efficiency + special_point;
    }

    public int getBlessOfAinExp() {
        int bless_exp = 0;
        if (hasSkillEffect(4005))
            if (getLevel() < 55) {
                bless_exp += 15300;
            } else if (getLevel() >= 55 && getLevel() < 60) {
                bless_exp += 14300;
            } else if (getLevel() >= 60 && getLevel() < 65) {
                bless_exp += 13300;
            } else if (getLevel() >= 65 && getLevel() < 70) {
                bless_exp += 12300;
            }
        if (hasSkillEffect(80009))
            bless_exp += 8000;
        if (hasSkillEffect(4004))
            bless_exp += 5400;
        return bless_exp;
    }

    public int getAinExpBonus() {
        return this._ain_exp_bonus;
    }

    public L1TimeCollectionHandler getTimeCollection() {
        return this._timeCollection;
    }

    public void createTimeCollection() {
        this._timeCollection = new L1TimeCollectionHandler(this);
    }

    public void addInvenBonusItems(int itemid) {
        this._inven_bonus_items.add(Integer.valueOf(itemid));
    }

    public void delInvenBonusItems(int itemid) {
        this._inven_bonus_items.remove(Integer.valueOf(itemid));
    }

    public boolean isInvenBonusItems(int itemid) {
        return this._inven_bonus_items.contains(Integer.valueOf(itemid));
    }

    public void addTempSkillActive(int skillId) {
        this._temp_skill_active.add(Integer.valueOf(skillId));
    }

    public void delTempSkillActive(int skillId) {
        this._temp_skill_active.remove(Integer.valueOf(skillId));
    }

    public boolean isTempSkillActive(int skillId) {
        return this._temp_skill_active.contains(Integer.valueOf(skillId));
    }

    public void addTempSkillPassive(int PassiveId) {
        this._temp_skill_passive.add(Integer.valueOf(PassiveId));
    }

    public void delTempSkillPassive(int PassiveId) {
        this._temp_skill_passive.remove(Integer.valueOf(PassiveId));
    }

    public boolean isTempSkillPassive(int PassiveId) {
        return this._temp_skill_passive.contains(Integer.valueOf(PassiveId));
    }

    public boolean isPcGoldenStatus() {
        return this._pc_golden_status;
    }

    public void set_PcGoldenSstatus(boolean b) {
        this._pc_golden_status = b;
    }

    public int get_PcGoldenBuffIndex0Time() {
        return this._pc_golden_buff_index0_time;
    }

    public int get_PcGoldenBuffIndex1Time() {
        return this._pc_golden_buff_index1_time;
    }

    public void set_PcGoldenBuffIndex0Time(int i) {
        this._pc_golden_buff_index0_time = i;
    }

    public void set_PcGoldenBuffIndex1Time(int i) {
        this._pc_golden_buff_index1_time = i;
    }

    public void addPcGoldenBuff(int buffid) {
        this._pc_golden_buff.add(Integer.valueOf(buffid));
    }

    public void delPcGoldenBuff(int buffid) {
        this._pc_golden_buff.remove(Integer.valueOf(buffid));
    }

    public boolean isPcGoldenBuff(int buffid) {
        return this._pc_golden_buff.contains(Integer.valueOf(buffid));
    }

    public ArrayList<Integer> getPcGoldenBuffList() {
        return this._pc_golden_buff;
    }

    public void addAinhasad_faith(int index, Timestamp endTime) {
        this._ainhasad_faith.put(Integer.valueOf(index), endTime);
    }

    public void delAinhasad_faith(int index) {
        if (isAinhasad_faith(index))
            this._ainhasad_faith.remove(Integer.valueOf(index));
    }

    public Timestamp getAinhasad_faith_EndTime(int index) {
        return this._ainhasad_faith.get(Integer.valueOf(index));
    }

    public boolean isAinhasad_faith(int index) {
        return this._ainhasad_faith.containsKey(Integer.valueOf(index));
    }

    public HashMap<Integer, Timestamp> getAinHasd_faith() {
        return this._ainhasad_faith;
    }

    public void add_armor_magic_pro(int i) {
        this._armor_magic_pro += i;
    }

    public int get_armor_magic_pro() {
        return this._armor_magic_pro;
    }

    public void add_status_time_reduce(int i) {
        this._status_time_reduce += i;
    }

    public int get_status_time_reduce() {
        return this._status_time_reduce;
    }

    public int getPotionRecoveryRatePct() {
        return this._potionRecoveryRate;
    }

    public void addPotionRecoveryRatePct(int i) {
        this._potionRecoveryRate += i;
    }

    public L1FavorBookInventory getFavorBook() {
        return this._favorBook;
    }

    public void createFavorBookInventory() {
        this._favorBook = new L1FavorBookInventory(this);
    }

    public void setAurakiaCircle(boolean a) {
        if (a) {
            this._AurakiaCircle = true;
        } else {
            this._AurakiaCircle = false;
        }
    }

    public boolean isAurakiaCircle() {
        return this._AurakiaCircle;
    }

    public int getThreeItemEquipped() {
        return this._threeItemEquipped;
    }

    public void addThreeItemEquipped(int i) {
        this._threeItemEquipped += i;
    }

    public void removeThreeSkillEffect() {
        if (hasSkillEffect(22017))
            removeSkillEffect(22017);
        MJNotiSkillModel model = MJNotiSkillService.service().model(22017);
        model.icons(this, 0, false);
        setPrime_War_Zone(false);
    }

    public void startHpARegeneration() {
        if (getHpAr() == 0)
            return;
        if (!this._hpARegeneration) {
            long interval = 32000L;
            this._hpArRegen = new HpARegeneration(this, 32000L);
            GeneralThreadPool.getInstance().schedule((Runnable)this._hpArRegen, 32000L);
            this._hpARegeneration = true;
        }
    }

    public void stopHpARegeneration() {
        if (this._hpARegeneration) {
            this._hpArRegen.cancel();
            this._hpArRegen = null;
            this._hpARegeneration = false;
        }
    }

    public void startMpARegeneration() {
        if (getMpAr() == 0)
            return;
        if (!this._mpARegeneration) {
            long interval = 16000L;
            this._mpArRegen = new MpARegeneration(this, 16000L);
            GeneralThreadPool.getInstance().schedule((Runnable)this._mpArRegen, 16000L);
            this._mpARegeneration = true;
        }
    }

    public void stopMpARegeneration() {
        if (this._mpARegeneration) {
            this._mpArRegen.cancel();
            this._mpArRegen = null;
            this._mpARegeneration = false;
        }
    }

    public boolean isPcBuff() {
        long sysTime = System.currentTimeMillis();
        if (getAccount().getBuff_PC() == null)
            return false;
        if (getAccount().getBuff_PC().getTime() < sysTime)
            return false;
        return true;
    }

    public void setHalpasArmor(L1ItemInstance item) {
        this.HalpasArmor = item;
    }

    public L1ItemInstance getHalpasArmor() {
        return this.HalpasArmor;
    }

    public void removeHalpasArmor() {
        this.HalpasArmor = null;
    }

    public boolean checkHalpasTime() {
        long endTime;
        if (this.HalpasArmor == null)
            return false;
        long currentTime = System.currentTimeMillis();
        if (this.HalpasArmor.getHalpas_Time() != null) {
            endTime = Timestamp.valueOf(this.HalpasArmor.getHalpas_Time().toString()).getTime();
        } else {
            endTime = 0L;
        }
        int coolTime = Long.valueOf((endTime - currentTime) / 1000L).intValue();
        if (coolTime < 0)
            return true;
        return false;
    }

    public boolean isCastleEffect() {
        return this._CastleEffect;
    }

    public void setCastleEffect(boolean b) {
        this._CastleEffect = b;
    }

    public void set_MassTel(boolean flag) {
        this._isMassTel = flag;
    }

    public boolean isMassTel() {
        return this._isMassTel;
    }

    public void set_ForcePolyId(int polyid) {
        this._ForcePolyId = polyid;
    }

    public int get_ForcePolyId() {
        return this._ForcePolyId;
    }

    public void setClanJoinDate(Timestamp t) {
        this._clanjointime = t;
    }

    public void set_Tyrant_Excute(boolean flag) {
        this._tyrant_excute = flag;
    }

    public boolean is_Tyrant_Excute() {
        return this._tyrant_excute;
    }

    public void setBehemoth_Attacker(L1PcInstance attacker) {
        this._behemoth_attacker = attacker;
    }

    public L1PcInstance getBehemoth_Attacker() {
        return this._behemoth_attacker;
    }

    public void clear_Behemoth_Damage() {
        this._behemoth_dmg_sum = 0;
    }

    public void add_Behemoth_Damage(int damage) {
        this._behemoth_dmg_sum += damage;
    }

    public int get_Behemoth_Heal() {
        int heal = this._behemoth_dmg_sum * 40 / 100;
        if (heal >= 2000)
            heal = 2000;
        return heal;
    }

    public void setChainSwordExposed(boolean flag) {
        this._chainsword_expose = flag;
    }

    public boolean isChainSwordExposed() {
        return this._chainsword_expose;
    }

    public void setChainSwordStep(int i) {
        this._chainsword_step = i;
    }

    public int getChainSwordStep() {
        return this._chainsword_step;
    }

    public int getAbnormalStatusPvPReduction() {
        return this._abnormal_status_pvp_damage_reduction;
    }

    public void setAbnormalStatusPvPReduction(int i) {
        this._abnormal_status_pvp_damage_reduction = i;
    }

    public void addAbnormalStatusPvPReduction(int i) {
        this._abnormal_status_pvp_damage_reduction = i;
    }

    public static interface PolyTrigger {
        void onWork();
    }
}
