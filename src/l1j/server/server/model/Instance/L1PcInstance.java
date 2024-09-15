package l1j.server.server.server.model.Instance;

import MJFX.UIAdapter.MJUIAdapter;
import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import MJShiftObject.MJEShiftObjectType;
import l1j.server.AinhasadSpecialStat.AinhasadHpMpRegeneration;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.CPMWBQSystem.Database.CPMWBQUserTable;
import l1j.server.CPMWBQSystem.info.CPMWBQinfo;
import l1j.server.server.ClanBuffList.ClanBuffListLoader;
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
import l1j.server.MJCompanion.Basic.Buff.MJCompanionBuffInfo;
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
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJItemExChangeSystem.S_ItemExSelectPacket;
import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;

import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJPassiveSkill.MJPassiveInfo;
import l1j.server.MJPushitem.model.MJChaPushModel;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.MJTemplate.Attribute.MJAttrMap;
import l1j.server.MJTemplate.MJEPcStatus;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CompanionT.eCommand;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_INDUN_TOWER_HIT_POINT_RATIO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FINISH_PLAY_SUPPORT_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FORCE_FINISH_PLAY_SUPPORT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FORCE_FINISH_PLAY_SUPPORT_NOTI.eReason;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_START_PLAY_SUPPORT_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_START_PLAY_SUPPORT_ACK.eResult;
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
import l1j.server.MJWarSystem.MJWarFactory.WAR_TYPE;
import l1j.server.revenge.MJRevengeService;
import l1j.server.revenge.model.MJRevengeModel;
import l1j.server.revenge.model.MJRevengeProvider;
import l1j.server.server.Controller.FishingTimeController;
import l1j.server.server.Controller.GhostController;
import l1j.server.server.command.executor.L1HpBar;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.L1World;
import l1j.server.server.model.classes.L1ClassFeature;
import l1j.server.server.model.gametime.GameTimeCarrier;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookUserLoader;
import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionUserLoader;
import l1j.server.server.model.item.itemdelay.ItemDelayTimer;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.SkillData;
import l1j.server.server.model.skill.noti.MJNotiSkillModel;
import l1j.server.server.model.skill.noti.MJNotiSkillService;
import l1j.server.server.server.monitor.Logger.ItemActionType;
import l1j.server.server.server.monitor.LoggerInstance;
import l1j.server.server.server.serverpackets.*;
import l1j.server.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.*;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.CustomQuestUser;
import l1j.server.server.types.Point;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static l1j.server.server.model.skill.L1SkillId.*;

public class L1PcInstance extends L1Character {
	public static final MJAttrKey<Long> latestNpcClickMillis = MJAttrKey
			.newInstance("mj-pc-latest-npc-click");
	public static final MJAttrKey<L1PcExpMonitor> expMonitorKey = MJAttrKey
			.newInstance("pc-exp-monitor");
	public static final MJAttrKey<List<MJRevengeModel>> revengeModelKey = MJAttrKey
			.newInstance("mj-pc-revenge-model");
	public static final MJAttrKey<MJRevengeModel> revengePursuitModelKey = MJAttrKey
			.newInstance("mj-pc-revenge-pursuit-model");
	public static final MJAttrKey<List<MJDeathPenaltyItemModel>> deathpenaltyitemModelKey = MJAttrKey
			.newInstance("mj-pc-death-penalty-item-model");
	public static final MJAttrKey<List<MJDeathPenaltyExpModel>> deathpenaltyexpModelKey = MJAttrKey.newInstance("mj-pc-death-penalty-exp-model");
	public static final MJAttrKey<List<MJChaPushModel>> pcpushmodelkey = MJAttrKey
			.newInstance("mj-pc-push-model");

	public boolean _create_password = false;
	public boolean _seal_scroll = false;
	public int _seal_scroll_count = 0;
	public boolean _ClassChange = false;
	public boolean _LottoSelect = false;
	public boolean serverDown;


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

	public int merchantSearchObjid; // 商人搜尋物件ID
	public boolean war_zone = false;

	public boolean _isPacketAttack = false;

	public boolean getIsPacketAttack() {
		return _isPacketAttack;
	}

	public void setIsPacketAttack(boolean flag) {
		_isPacketAttack = flag;
	}

	public int getNcoin() {
		if (getAccount() != null) {
			return getNetConnection().getAccount().Ncoin_point;
		}
		return 0;
	}

	// 與價格指令相關
	private boolean _ismarket;

	public boolean isMarket() {
		return _ismarket;
	}

	public void setMarket(boolean flag) {
		this._ismarket = flag;
	}

	private boolean _isSM;

	public boolean isSM() {
		return _isSM;
	}

	public void setSM(boolean flag) {
		this._isSM = flag;
	}

	// 與行情指令相關
	private int _returnstatus;

	public synchronized int getReturnStatus() {
		return _returnstatus;
	}

	public synchronized void setReturnStatus(int i) {
		_returnstatus = i;
	}

	private l1j.server.server.model.L1StatReset _statReset;

	public void setStatReset(l1j.server.server.model.L1StatReset sr) {
		_statReset = sr;
	}

	public l1j.server.server.model.L1StatReset getStatReset() {
		return _statReset;
	}

	public l1j.server.server.model.L1ReturnStatTemp rst = null;

	public void addNcoin(int coin) {
		if (getNetConnection() != null) {
			if (getNetConnection().getAccount() != null) {
				getNetConnection().getAccount().Ncoin_point += coin;
				getNetConnection().getAccount().updateNcoin();
			}
		} else {
			// System.out.println("觸發陷阱。");
		}
	}

	public void addNcoin1(int coin) {
		if (getNetConnection() != null) {
			if (getNetConnection().getAccount() != null) {
				getNetConnection().getAccount().Ncoin_point -= coin;
				getNetConnection().getAccount().updateNcoin();
			}
		}
	}

	private int boss_spawn_yn;

	public int getBossYN() {
		return boss_spawn_yn;
	}

	public void setBossYN(int i) {
		boss_spawn_yn = i;
	}

	private int boss_spawn_npc;

	public int getBossNpc() {
		return boss_spawn_npc;
	}

	public void setBossNpc(int i) {
		boss_spawn_npc = i;
	}

	private boolean specialBuff = false;

	public void setSpecialBuff(boolean flag) {
		this.specialBuff = flag;
	}

	public boolean isSpecialBuff() {
		return this.specialBuff;
	}

	private ReportDeley _reportdeley; // 新增舉報

	public void startReportDeley() { // 新增舉報
		_reportdeley = new ReportDeley(this);
		if (_regenTimer != null) {
			_regenTimer.cancel();
			_regenTimer = null;
		}
		_regenTimer = new Timer(true);
		_regenTimer.schedule(_reportdeley, 100000); // 延遲時間10分鐘
	}

	// 新增舉報
	private boolean _isReport = true;

	public void setReport(boolean _isreport) {
		_isReport = _isreport;
	}

	public boolean isReport() {
		return _isReport;
	}

	private boolean _halpaspaith = true;

	public boolean ishalpaspaith() {
		return _halpaspaith;
	}

	public void sethalpaspaith(boolean on) {
		_halpaspaith = on;
	}

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
	public static final int CLASSID_WARRIOR_MALE = 20567;
	public static final int CLASSID_WARRIOR_FEMALE = 20577;
	public static final int CLASSID_FENCER_MALE = 18520;
	public static final int CLASSID_FENCER_FEMALE = 18499;
	public static final int CLASSID_LANCER_MALE = 19296;
	public static final int CLASSID_LANCER_FEMALE = 19299;

	public static final int REGENSTATE_NONE = 4;
	public static final int REGENSTATE_MOVE = 2;
	public static final int REGENSTATE_ATTACK = 2;// 原本1

	public long tamtime = 0;

	// 組合系統
	public int getComboCount() {
		return this.comboCount;
	}

	public void setComboCount(int comboCount) {
		this.comboCount = comboCount;
	}

	private boolean isSafetyZone;

	public boolean getSafetyZone() {
		return isSafetyZone;
	}

	public void setSafetyZone(boolean value) {
		if (isSafetyZone == value)
			return;
		isSafetyZone = value;
		if (hasSkillEffect(EXP_POTION)) {
			int time = getSkillEffectTimeSec(EXP_POTION);
			L1SkillUse.on_icons(this, L1SkillId.EXP_POTION, time);
		}
	}

	private int noDelayTime = 0;

	public int getNoDelayTime() {
		return noDelayTime;
	}

	public void setNoDelayTime(int noDelayTime) {
		this.noDelayTime = noDelayTime;
	}

	private int _speedhackCount = 0;

	public int getSpeedHackCount() {
		return _speedhackCount;
	}

	public void setSpeedHackCount(int x) {
		_speedhackCount = x;
	}

	/** 團隊戰 Y_N **/
	private boolean _raid = false;

	public void setRaidGame(boolean flag) {
		this._raid = flag;
	}

	public boolean getRaidGame() {
		return _raid;
	}

	private boolean _Mayo = false;

	public void setMayo(boolean flag) {
		this._Mayo = flag;
	}

	public boolean getMayo() {
		return _Mayo;
	}

	private boolean _Necross = false;

	public void setNecross(boolean flag) {
		this._Necross = flag;
	}

	public boolean getNecross() {
		return _Necross;
	}

	private boolean _Tebeboss = false;

	public void setTebeboss(boolean flag) {
		this._Tebeboss = flag;
	}

	public boolean getTebeboss() {
		return _Tebeboss;
	}

	private boolean _Curch = false;

	public void setCurch(boolean flag) {
		this._Curch = flag;
	}

	public boolean getCurch() {
		return _Curch;
	}

	private boolean _dtah = false;

	public void setDeat(boolean flag) {
		this._dtah = flag;
	}

	public boolean getDeat() {
		return _dtah;
	}

	private boolean _trac = false;

	public void setTrac(boolean flag) {
		this._trac = flag;
	}

	public boolean getTrac() {
		return _trac;
	}

	private boolean _girtas = false;

	public void setGirtas(boolean flag) {
		this._girtas = flag;
	}

	public boolean getGirtas() {
		return _girtas;
	}

	private boolean _orim = false;

	public void setOrim(boolean flag) {
		this._orim = flag;
	}

	public boolean getOrim() {
		return _orim;
	}

	private boolean _erzarbe = false;

	public void setErzarbe(boolean flag) {
		this._erzarbe = flag;
	}

	public boolean getErzarbe() {
		return _erzarbe;
	}

	private boolean _Hondon = false;

	public void setHondon(boolean flag) {
		this._Hondon = flag;
	}

	public boolean getHondon() {
		return _Hondon;
	}

	private boolean _Reper = false;

	public void setReper(boolean flag) {
		this._Reper = flag;
	}

	public boolean getReper() {
		return _Reper;
	}

	private boolean _Rekt = false;

	public void setRekt(boolean flag) {
		this._Rekt = flag;
	}

	public boolean getRekt() {
		return _Rekt;
	}

	private boolean _Rekt1 = false;

	public void setRekt1(boolean flag) {
		this._Rekt1 = flag;
	}

	public boolean getRekt1() {
		return _Rekt1;
	}

	private boolean _Rekt2 = false;

	public void setRekt2(boolean flag) {
		this._Rekt2 = flag;
	}

	public boolean getRekt2() {
		return _Rekt2;
	}

	public boolean getgarmf() {
		return _garmf;
	}

	private boolean _garmf = false;

	public void setgarmf(boolean flag) {
		this._garmf = flag;
	}

	public boolean getTaros() {
		return _Taros;
	}

	private boolean _Taros = false;

	public void setTaros(boolean flag) {
		this._Taros = flag;
	}

	public boolean getCrock() {
		return _Crock;
	}

	private boolean _Crock = false;

	public void setCrock(boolean flag) {
		this._Crock = flag;
	}

	public boolean getCrock1() {
		return _Crock1;
	}

	private boolean _Crock1 = false;

	public void setCrock1(boolean flag) {
		this._Crock1 = flag;
	}

	public boolean getCrock2() {
		return _Crock2;
	}

	private boolean _Crock2 = false;

	public void setCrock2(boolean flag) {
		this._Crock2 = flag;
	}

	// 加載帳戶信息時需要..
	public Account.Account getAccount() {
		if (_netConnection == null)
			return null;

		return this._netConnection.getAccount();
	}

	public boolean FouSlayer = false;
	public boolean TripleArrow = false;

	// 句子監控變數
	public boolean sentenceMonitoring = false;

	public int _x;

	// 與生長有關的釣魚
	private L1ItemInstance _fishingitem;

	public L1ItemInstance getFishingItem() {
		return _fishingitem;
	}

	public void setFishingItem(L1ItemInstance item) {
		_fishingitem = item;
	}

	private boolean _Attacklog = false;

	public void setAttackLog(boolean i) {
		this._Attacklog = i;
	}

	public boolean getAttackLog() {
		return this._Attacklog;
	}

	public long AttackControllerTime = 0;
	/** SPR檢查 **/
	public int AttackSpeedCheck2 = 0;
	public int MoveSpeedCheck = 0;
	public int magicSpeedCheck = 0;
	public long AttackSpeed2;
	public long MoveSpeed;
	public long magicSpeed;
	/** SPR檢查 **/

	public int dx = 0;
	public int dy = 0;
	public short dm = 0;
	public int dh = 0;
	public int shopTransformation = 0;

	public static final String WANTED_TITLE1 = "\fe[緊急通緝中][1階段]";
	public static final String WANTED_TITLE2 = "\fe[緊急通緝中][2階段]";
	public static final String WANTED_TITLE3 = "\fe[緊急通緝中][3階段]";
	private int _Wanted_Level = 0;


	public int get_Wanted_Level() {
		return _Wanted_Level;
	}
	public void set_Wanted_Level(int wanted_level) {
		_Wanted_Level = wanted_level;
	}
	public void add_Wanted_Level() {
		_Wanted_Level += 1;
		if (_Wanted_Level >=3 ) {
			_Wanted_Level = 3;
		}
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
		addDmgRate(-wanted_Dmg * mul);
		addHitup(-wanted_Hitup * mul);
		addBowDmgup(-wanted_BowDmg * mul);
		addBowHitup(-wanted_BowHitUp * mul);
		getAbility().addSp(-wanted_Sp * mul);
		addDamageReductionByArmor(-wanted_Reduction * mul);
		getAC().addAc(wanted_Ac * mul);
		sendPackets(new S_OwnCharAttrDef(this));
		sendPackets(new S_OwnCharStatus2(this));
		sendPackets(new S_OwnCharStatus(this));
		if (isOn) {
			S_CharTitle pck = new S_CharTitle(getId(), WANTED_TITLE1);
			sendPackets(pck, false);
			Broadcaster.broadcastPacket(this, pck);
		} else {
			S_CharTitle pck = new S_CharTitle(getId(), "");
			sendPackets(pck, false);
			Broadcaster.broadcastPacket(this, pck);
		}
	}

	public void Wanted_Level2(boolean isOn, boolean login) {
		int Login = 1;
		if (login) {
			Login = 2;
		}
		int mul = isOn ? Login : -2;
		addDmgRate(-wanted_Dmg * mul);
		addHitup(-wanted_Hitup * mul);
		addBowDmgup(-wanted_BowDmg * mul);
		addBowHitup(-wanted_BowHitUp * mul);
		getAbility().addSp(-wanted_Sp * mul);
		addDamageReductionByArmor(-wanted_Reduction * mul);
		getAC().addAc(wanted_Ac * mul);
		sendPackets(new S_OwnCharAttrDef(this));
		sendPackets(new S_OwnCharStatus2(this));
		sendPackets(new S_OwnCharStatus(this));
		if (isOn) {
			S_CharTitle pck = new S_CharTitle(getId(), WANTED_TITLE2);
			sendPackets(pck, false);
			Broadcaster.broadcastPacket(this, pck);
		} else {
			S_CharTitle pck = new S_CharTitle(getId(), "");
			sendPackets(pck, false);
			Broadcaster.broadcastPacket(this, pck);
		}
	}

	public void Wanted_Level3(boolean isOn, boolean login) {
		int Login = 1;
		if (login) {
			Login = 3;
		}
		int mul = isOn ? Login : -3;
		addDmgRate(-wanted_Dmg * mul);
		addHitup(-wanted_Hitup * mul);
		addBowDmgup(-wanted_BowDmg * mul);
		addBowHitup(-wanted_BowHitUp * mul);
		getAbility().addSp(-wanted_Sp * mul);
		addDamageReductionByArmor(-wanted_Reduction * mul);
		getAC().addAc(wanted_Ac * mul);
		sendPackets(new S_OwnCharAttrDef(this));
		sendPackets(new S_OwnCharStatus2(this));
		sendPackets(new S_OwnCharStatus(this));
		if (isOn) {
			S_CharTitle pck = new S_CharTitle(getId(), WANTED_TITLE3);
			sendPackets(pck, false);
			Broadcaster.broadcastPacket(this, pck);
		} else {
			S_CharTitle pck = new S_CharTitle(getId(), "");
			sendPackets(pck, false);
			Broadcaster.broadcastPacket(this, pck);
		}
	}



	/** 每個角色的額外傷害、額外減免、機率 **/
	private int _AddDamage = 0;
	private int _AddDamageRate = 0;
	private int _AddReduction = 0;
	private int _AddReductionRate = 0;

	public int getAddDamage() {
		return _AddDamage;
	}

	public void setAddDamage(int addDamage) {
		_AddDamage = addDamage;
	}

	public int getAddDamageRate() {
		return _AddDamageRate;
	}

	public void setAddDamageRate(int addDamageRate) {
		_AddDamageRate = addDamageRate;
	}

	public int getAddReduction() {
		return _AddReduction;
	}

	public void setAddReduction(int addReduction) {
		_AddReduction = addReduction;
	}

	public int getAddReductionRate() {
		return _AddReductionRate;
	}

	public void setAddReductionRate(int addReductionRate) {
		_AddReductionRate = addReductionRate;
	}

	/** 每個角色的額外傷害、額外減免、機率 **/

	private int _ubscore;

	public int getUbScore() {
		return _ubscore;
	}

	public void setUbScore(int i) {
		_ubscore = i;
	}

	public byte[] fairyInformation = new byte[512];

	public void grantFairyExperienceReward(int lv) {
		long needExp = ExpTable.getNeedExpNextLevel(lv);
		long addExp = 0;
		addExp = (long) (needExp * 0.01);
		if (addExp != 0) {
			int level = ExpTable.getLevelByExp(get_exp() + addExp);
			if (level > 60) {
				sendPackets(new S_SystemMessage("無法獲得更多經驗值。"));
			} else {
				add_exp(addExp);
			}
		}
	}

	public void saveFairyInformation(int id) {
		int count = getFairyCount(getId());
		int[] fairyInformation = new int[0];
		fairyInformation[id] = 1;
		if (count == 0) {
			storeFairy(getId(), fairyInformation);
		} else {
			updateFairy(getId(), fairyInformation);
		}
	}

	private void updateFairy(int id, int[] fairyInformation) {

	}

	private void storeFairy(int id, int[] fairyInformation) {

	}

	private int getFairyCount(int id) {
		return 0;
	}

	/** 經驗值設定 pc.經驗值獎勵(pc, 52, 0.04); / 這樣放置的話，以52級為基準4% **/
	public void grantExperienceReward(L1PcInstance pc, int level, double rate) {
		long needExp = ExpTable.getNeedExpNextLevel(level);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());

		long exp = 0;
		exp = (long) (needExp * rate * exppenalty);

		pc.add_exp(exp);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
	}

	public int fairlycount(int objectId) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT count(*) as cnt FROM character_Fairly_Config WHERE object_id=?");
			pstm.setInt(1, objectId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cnt");
			}
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
			pstm = con
					.prepareStatement("INSERT INTO character_Fairly_Config SET object_id=?, data=?");
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
			pstm = con
					.prepareStatement("UPDATE character_Fairly_Config SET data=? WHERE object_id=?");
			pstm.setBytes(1, data);
			pstm.setInt(2, objectId);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			// e.printStackTrace();
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
	/** 設定年齡 **/
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

	private int _baseMagicHitup = 0; // 基礎屬性引起的魔法命中
	private int _baseMagicCritical = 0; // 基礎屬性引起的魔法暴擊(％)
	private int _baseMagicDmg = 0; // 基礎屬性引起的魔法傷害
	private int _baseMagicDecreaseMp = 0; // 基礎屬性引起的魔法傷害

	private int _DmgupByArmor = 0; // 防禦裝置引起的近戰武器附加傷害率
	private int _bowDmgupByArmor = 0; // 防禦裝置引起的弓箭附加傷害率

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
	public boolean isInFantasy = false; // 夢想島嶼更新

	/** 火龍的安息處 **/
	public boolean isInValakasBoss = false;
	public boolean isInValakas = false;
	private boolean _ghost = false;
	private boolean _ghostCanTalk = true;
	private boolean _isReserveGhost = false;
	private boolean _isShowTradeChat = true;
	private boolean _isCanWhisper = true;
	private boolean _isFishing = false;
	private boolean _isFishingReady = false;
	private boolean isDeathMatch = false; // 死亡競賽
	private boolean _isSupporting = false;
	private boolean _isShowWorldChat = true;
	private boolean _gm;
	private boolean _monitor;
	private boolean _gmInvis;
	// private boolean _isTeleport = false;
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
	public long ghosttime = 0;

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
	private int _DmgRate = 0; // 防禦裝置引起的近戰武器附加傷害率
	private int _HitRate = 0; // 防禦裝置引起的近戰武器命中率
	private int _bowHitRate = 0; // 防禦裝置引起的弓箭命中率
	private int _bowDmgRate = 0; // 防禦裝置引起的弓箭附加傷害率
	// 220728 用於背包獎勵物品系統的添加 by.沙門
	private int _sp = 0;

	// private int _teleportY = 0;
	// private int _teleportX = 0;
	// private short _teleportMapId = 0;
	// private int _teleportHeading = 0;

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
	private long _fishingTime = 0;
	private int _dessertId = 0;
	private int _callClanId;
	private int _callClanHeading;

	private int _currentWeapon; // 與機器人相關
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



	// private MpDecreaseByScales _mpDecreaseByScales;
	private AHRegeneration _AHRegen;
	private SHRegeneration _SHRegen;
	private HalloweenRegeneration _HalloweenRegen;

	// //-- 修正 [ 封鎖新增的源碼 ]
	// private final L1ExcludingList _excludingList = new L1ExcludingList();
	//
	// public L1ExcludingList getExcludingList() {
	// return _excludingList;
	// }

	// HP MP 再生控制器
	/*
	 * private HpMpRegenController _hpmpRegen; private boolean _hpmpRegenActive;
	 */

	/** HP MP 再生控制器 **/

	/*    public void setHpMpRegenActive(boolean flag) {
	_hpmpRegenActive = flag;
	}

	public void startHpMpRegeneration() {
	if (!_hpmpRegenActive) {
	_hpmpRegen = new HpMpRegenController(this);
	_regenTimer.scheduleAtFixedRate(_hpmpRegen, 1000, 1000);
	_hpmpRegenActive = true;
	}
	}

	public void stopHpMpRegeneration() {
	if (_hpmpRegenActive) {
	_hpmpRegen.cancel();
	_hpmpRegen = null;
	_hpmpRegenActive = false;
	}
	}*/


	private boolean _isPrivateShop = false;
	private boolean _isPrivateReady = false;
	private int _partnersPrivateShopItemCount = 0;

	private long _lastPasswordChangeTime;
	private long _lastLocalTellTime;

	private boolean autoShop = false;

	boolean isExpDrop;
	boolean isItemDrop;

	public final ArrayList<L1BookMark> _speedbookmarks;

	public L1BookMark[] getBookMarkArray() {
		return _bookmarks.toArray(new L1BookMark[_bookmarks.size()]);
	}

	public L1BookMark[] getSpeedBookMarkArray() {
		return _speedbookmarks.toArray(new L1BookMark[_speedbookmarks.size()]);
	}

	private int _markcount;

	public void setMark_count(int i) {
		_markcount = i;
	}

	public int getMark_count() {
		return _markcount;
	}

	public boolean isAutoShop() {
		boolean autoShop = false;
		return autoShop;
	}

	public void setAutoShop(boolean c) {
		boolean autoShop = c;
	}

	/** 血盟增益 **/
	private boolean _clanbuff = false;

	public boolean isClanBuff() {
		return _clanbuff;
	}

	public void setClanBuff(boolean c) {
		_clanbuff = c;
	}

	/** 血盟增益 **/

	// 生存的呼喊
	public int _getLive = 0;

	public int getLive() {
		return _getLive;
	}

	public void addLive(int Live) {
		_getLive += Live;
	}

	public void setLive(int Live) {
		_getLive = Live;
	}

	// 遭遇火焰魔像
	public int[] FireGolem = new int[18];
	public int[] FireEnchant = new int[18];

	// 穿刺
	public int[] PiersItemId = new int[19];
	public int[] PiersEnchant = new int[19];

	private boolean _teleportWait = false; // 傳送等待
	private boolean _isHomnam = false;
	private boolean _isBosMon = false;
	private boolean _isNCoinMon = false;
	private boolean _isSpecialMap = false;
	// private boolean _isNcoin300 = false;
	// private boolean _isNcoin500 = false;
	// private boolean _isNcoin3000 = false;

	private boolean _magicitem = false;
	private int _magicitemid;
	// Added 3.63 item packet
	public boolean isWorld = false;
	// Added 3.63 item packet
	public boolean isDanteasBuff = false;

	public boolean isGotobokBuff = false;

	public boolean isServerDown = false;
	public boolean isBaphoRoom = false;
	// 與製作相關的詹多爾
	public int _getCount;

	private long _npcaction;

	public final ArrayList<L1BookMark> _bookmarks;
	private byte[] _shopChat;
	private AtomicInteger _pinkNameTime;
	private GameClient _netConnection;
	private static Logger _log = Logger.getLogger(L1PcInstance.class.getName());
	private final SkillData skill_data;

	// 構造函數
	public L1PcInstance() {
		_accessLevel = 0;
		_currentWeapon = 0;
		_inventory = new L1PcInventory(this);
		_dwarfForPackage = new L1DwarfForPackageInventory(this);
		_tradewindow = new L1Inventory();
		_bookmarks = new ArrayList<L1BookMark>();
		_speedbookmarks = new ArrayList<L1BookMark>();
		_quest = new L1Quest(this);
		_equipSlot = new L1EquipmentSlot(this);
		_pinkNameTime = new AtomicInteger(0);
		skill_data = new SkillData(this);
	}

	public long get_lastPasswordChangeTime() {
		return _lastPasswordChangeTime;
	}

	public long get_lastLocalTellTime() {
		return _lastLocalTellTime;
	}

	public void update_lastPasswordChangeTime() {
		_lastPasswordChangeTime = System.currentTimeMillis();
	}

	public void update_lastLocalTellTime() {
		_lastLocalTellTime = System.currentTimeMillis();
	}

	private long _lastShellUseTime;

	public long getlastShellUseTime() {
		return _lastShellUseTime;
	}

	public void updatelastShellUseTime() {
		_lastShellUseTime = System.currentTimeMillis();
	}

	public int getPinkNameTime() {
		return _pinkNameTime.get();
	}

	public int DecrementPinkNameTime() {
		return _pinkNameTime.decrementAndGet();
	}

	public int SetPinkNameTime(int timeValue) {
		return _pinkNameTime.getAndSet(timeValue);
	}

	public void setSkillMastery(int skillid) {
		if (!skillList.contains(skillid)) {
			skillList.add(skillid);
		}
	}

	private static L1Aura _aura;

	public boolean isUnmannedShop() {
		return false;
	}

	public boolean isAutomatedStore() {
		return false;
	}

	public boolean isWorld() {
		return false;
	}

	public String getName() {
		return null;
	}

	public int getId() {
		return 0;
	}

	public l1j.server.server.model.Ability getAbility() {
		return null;
	}

	public int getMapId() {
		return 0;
	}

	public double getMaxHp() {
		return 0;
	}

	public double getCurrentHp() {
		return 0;
	}

	public int getHeading() {
		return 0;
	}

	public void setId(int objid) {
	}

	public void setName(String charName) {
	}

	public void set_exp(long exp) {

	}

	public void setCurrentHpDB(short currentHp) {
	}

	public int getCurrentMp() {
		return 0;
	}

	public int getX() {
		return 0;
	}

	public int getY() {
		return 0;
	}

	public void setX(int i) {

	}

	public void setY(int i) {

	}

	public void setMap(short i) {

	}

	public l1j.server.server.model.Light getLight() {
		return null;
	}

	public int getLevel() {
		return 0;
	}

	public int getCurrentSpriteId() {
		return 0;
	}

	public boolean isDead() {
	}

	public boolean hasSkillEffect(int dragonTopaz) {
		return false;
	}

	public void broadcastPacket(l1j.server.server.serverpackets.S_SkillBrave sSkillSound) {

	}

	public double get_exp() {
		return 0;
	}

	public l1j.server.server.model.Resistance getResistance() {
		return null;
	}

	public void sendPackets(S_SystemMessage sSystemMessage, boolean b) {

	}

    public void sendPackets(l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI noti, l1j.server.MJTemplate.MJProto.MJEProtoMessages scSpellBuffNoti, boolean b) {
    }


    private class L1Aura implements Runnable {
		private String _name;
		private boolean _auraStopped = false;
		private L1PcInstance[] _auraMembersList;

		public L1Aura(String name) {
			_name = name;
		}

		@Override
		public void run() {
			while (!_auraStopped) {
				try {
					// System.out.println("每秒運行一次？？");
					L1PcInstance pc = L1World.getInstance().getPlayer(_name);
					if (pc == null) {
						auraStop();
						return new L1PcInstance[0];
					}

					pc.auraBuff(true);

					L1Party party = pc.getParty();
					if (party == null) {
						Thread.sleep(1000);
						continue;
					}
					if (_auraMembersList != null) {
						_auraMembersList = null;
					}
					_auraMembersList = party.getMembers();
					if (_auraMembersList == null
							|| _auraMembersList.length <= 0) {
						Thread.sleep(1000);
						continue;
					}
					for (L1PcInstance member : _auraMembersList) {
						if (member == null || pc.getId() == member.getId()) {
							continue;
						}
						if (member.isDead()) {
							member.auraBuff(false);
							continue;
						}
						if (pc.getMapId() == member.getMapId()
								&& pc.getLocation().isInScreen(
								member.getLocation())) { // 如果在距離範圍內
							if (!member.hasSkillEffect(L1SkillId.AURA)) {
								member.auraBuff(true);
							}
						} else { // 如果不存在..
							if (member.hasSkillEffect(L1SkillId.AURA)) {
								member.auraBuff(false);
							}
						}
					}
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
					auraStop();
				}
			}
			return new L1PcInstance[0];
		}

		public void auraStop() {
			_auraStopped = true;
			if (_auraMembersList != null) {
				for (L1PcInstance member : _auraMembersList) {
					member.auraBuff(false);
				}
			}
		}
	}

	public void auraBuff(boolean onOff) {
		try {
			if (onOff) { // 試圖施加增益效果
				if (hasSkillEffect(L1SkillId.AURA)) { // 如果已經有增益效果則返回
					// System.out.println("已經有了光環，返回：" + L1PcInstance.this.getName());
					return;
				}
				setSkillEffect(L1SkillId.AURA, -1);
				sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 479));
			} else { // 試圖移除增益效果
				if (!hasSkillEffect(L1SkillId.AURA)) { // 如果沒有增益效果則返回
					return;
				}
				killSkillEffectTimer(L1SkillId.AURA);
				sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 0, 479));
			}
			getResistance().addMr(onOff ? 10 : -10);
			addSpecialPierce(eKind.ALL, onOff ? 2: -2);
			// addSpecialResistance(eKind.ALL, onOff ? 2 : -2);
			getAbility().addAddedInt(onOff ? 1 : -1);
			getAbility().addAddedDex(onOff ? 1 : -1);
			getAbility().addAddedStr(onOff ? 1 : -1);
			resetBaseMr();
			sendPackets(new S_SPMR(this));
			sendPackets(new S_OwnCharStatus2(this));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeSkillMastery(int skillid) {
		if (skillList.contains((Object) skillid)) {
			skillList.remove((Object) skillid);
		}
	}

	public boolean isSkillMastery(int skillid) {
		return skillList.contains(skillid);
	}

	public int numOfSpellMastery() {
		return skillList.size();
	}

	public short getHpr() {
		return _hpr;
	}

	public void addHpr(int i) {
		_trueHpr += i;
		_hpr = (short) Math.max(0, _trueHpr);
	}

	public short getHpAr() {
		return _hpar;
	}

	public void addHpAr(int i) {
		_hpar += i;
	}

	public short getMpAr() {
		return _mpar;
	}

	public short getMpAr16() {
		return _mpar16;
	}
	public void addMpAr16(int i) {
		_mpar16 += i;
	}

	public void addMpAr(int i) {
		_mpar += i;
	}

	public short getMpr() {
		return _mpr;
	}

	public void addMpr(int i) {
		_trueMpr += i;
		_mpr = (short) Math.max(0, _trueMpr);
	}

	public void setHomnam(boolean flag) {
		_isHomnam = flag;
	}

	public boolean isHomnam() {
		return _isHomnam;
	}

	public void setBosMon(boolean flag) {
		_isBosMon = flag;
	}

	public boolean isBosMon() {
		return _isBosMon;
	}

	public boolean isSpecialMap() {
		return _isSpecialMap;
	}

	public void setSpecialMap(boolean flag) {
		_isSpecialMap = flag;
	}

	public boolean isNCoinMon() {
		return _isNCoinMon;
	}

	public void setNCoinMon(boolean flag) {
		_isNCoinMon = flag;
	}

	public long getNpcActionTime() {
		return _npcaction;
	}

	public void setNpcActionTime(long flag) {
		_npcaction = flag;
	}

	public boolean isMagicItem() {
		return _magicitem;
	}

	public void setMagicItem(boolean flag) {
		_magicitem = flag;
	}

	public int getMagicItemId() {
		return _magicitemid;
	}

	public void setMagicItemId(int itemid) {
		_magicitemid = itemid;
	}

	private PapuBlessing _PapuRegen;
	private boolean _PapuBlessingActive;

	public void startPapuBlessing() { // 巴普祝福
		final int RegenTime = 150000;
		if (!_PapuBlessingActive) {
			_PapuRegen = new PapuBlessing(this);
			_PapuBlessingActive = true;
			GeneralThreadPool.getInstance().schedule(_PapuRegen, RegenTime);
		}
	}

	public void startAHRegeneration() {
		final int INTERVAL = 600000;
		if (!_AHRegenActive) {
			_AHRegen = new AHRegeneration(this, INTERVAL);
			GeneralThreadPool.getInstance().schedule(_AHRegen, INTERVAL);
			_AHRegenActive = true;
		}
	}

	public void startSHRegeneration() {
		final int INTERVAL = 1800000;
		if (!_SHRegenActive) {
			_SHRegen = new SHRegeneration(this, INTERVAL);
			GeneralThreadPool.getInstance().schedule(_SHRegen, INTERVAL);
			_SHRegenActive = true;
		}
	}

	public void startHalloweenRegeneration() {
		final int INTERVAL = 900000;
		if (!_HalloweenRegenActive) {
			_HalloweenRegen = new HalloweenRegeneration(this, INTERVAL);
			GeneralThreadPool.getInstance().schedule(_HalloweenRegen, INTERVAL);
			_HalloweenRegenActive = true;
		}
	}

	public void stopPapuBlessing() { // 停止巴普祝福
		if (_PapuBlessingActive) {
			_PapuRegen.cancel();
			_PapuRegen = null;
			_PapuBlessingActive = false;
		}
	}

	public void stopAHRegeneration() {
		if (_AHRegenActive) {
			_AHRegen.cancel();
			_AHRegen = null;
			_AHRegenActive = false;
		}
	}

	public void stopSHRegeneration() {
		if (_SHRegenActive) {
			_SHRegen.cancel();
			_SHRegen = null;
			_SHRegenActive = false;
		}
	}

	public void stopHalloweenRegeneration() {
		if (_HalloweenRegenActive) {
			_HalloweenRegen.cancel();
			_HalloweenRegen = null;
			_HalloweenRegenActive = false;
		}
	}

	// TODO 物件自動更新0.3秒
	public void startObjectAutoUpdate() {
		final long INTERVAL_AUTO_UPDATE = 300L;
		removeAllKnownObjects();
		_autoUpdateFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate(new L1PcAutoUpdate(getId()), 0L, INTERVAL_AUTO_UPDATE);
	}

	public void stopEtcMonitor() {
		if (_autoUpdateFuture != null) {
			_autoUpdateFuture.cancel(true);
			_autoUpdateFuture = null;
		}
		if (_expMonitorFuture != null) {
			_expMonitorFuture.cancel(true);
			_expMonitorFuture = null;
			if (attribute().has(expMonitorKey)) {
				attribute().get(expMonitorKey).set(null);
			}
		}
		if (_ghostFuture != null) {
			_ghostFuture.cancel(true);
			_ghostFuture = null;
		}

		if (_hellFuture != null) {
			_hellFuture.cancel(true);
			_hellFuture = null;
		}

	}

	public void stopEquipmentTimer() {
		List<L1ItemInstance> allItems = this.getInventory().getItems();
		for (L1ItemInstance item : allItems) {
			if (item == null)
				continue;
			if (item.isEquipped() && item.getRemainingTime() > 0) {
				item.stopEquipmentTimer(this);
			}
		}
	}

	public void onChangeExp() {
		int level = ExpTable.getLevelByExp(get_exp());
		int char_level = getLevel();
		int gap = level - char_level;
		if (gap == 0) {
			sendPackets(new S_OwnCharStatus(this));
			int percent = ExpTable.getExpPercentage(char_level, get_exp());
			if (char_level >= 60 && char_level <= 64) {
				if (percent >= 10)
					removeSkillEffect(L1SkillId.LEVEL_UP_BONUS);
			} else if (char_level >= 65) {
				if (percent >= 5) {
					removeSkillEffect(L1SkillId.LEVEL_UP_BONUS);
				}
			}
			return;
		} else if (gap != 0) {
			MJDeathPenaltyexpDatabaseLoader.getInstance().do_Select(this);
		}

		int old_level = getLevel();

		if (gap > 0) {
			levelUp(gap);
			if (getLevel() >= 60) {
				setSkillEffect(L1SkillId.LEVEL_UP_BONUS, 10800000);
				sendPackets(new S_PacketBox(10800, true, true), true);
			}

			RenewStat();
		} else if (gap < 0) {
			levelDown(gap);
			RenewStat();
			removeSkillEffect(L1SkillId.LEVEL_UP_BONUS);
		}

		MJAutoMapInfo mInfo = MJAutoMapInfo.get_map_info(this.getMapId());
		if (mInfo != null) {
			if (mInfo.type_check(this, this.get_client_auto_type())) {
				this.do_finish_client_auto(eReason.INVALID_MAP);
			}
		}
		special_resistance_skill(this, old_level, getLevel(), false);
	}

	@Override
	public void onPerceive(L1PcInstance pc) {
		if (isGmInvis() /* || isGhost() || isInvisble() */) {
			return;
		}

		try {
			pc.addKnownObject(this);
			if (getAI() != null && pc.getAI() != null)
				return;
			if (getWorldObject() != null) {
				pc.sendPackets(getWorldObject(), false);
			} else if (pc.is_combat_field()) {
				if (isGm() || pc.isGm()) {
					pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
				} else {
					if (get_current_combat_team_id() == pc.get_current_combat_team_id()) {
						pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, "我軍"));
					} else {
						pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, "敵軍"));
					}
				}
			} else if (pc.getMapId() == 621) {
				if (isGm() || pc.isGm())
					pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
				else
					pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, "ID不公開"));
			} else if (pc.getMapId() == 13006 || pc.getMapId() == 13005) {
				if (pc.getMapId() == 13005) {
/*					if (isGm() || pc.isGm()) {
						pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
					} else {*/
					pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, pc.getClassName()));
//					}
				} else if (pc.getMapId() == 13006) {

					for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamRedList()) {
						if (player == this) {
							pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, "紅隊", -32767));
						}
					}

					for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamBlueList()) {
						if (player == this) {
							pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, "藍隊", 32767));

						}
					}

				}
//				}

			}else {
				if (SC_WORLD_PUT_OBJECT_NOTI.IS_PRESENTATION_MARK) {
					if (((pc.getMapId() >= 12852 && pc.getMapId() <= 12862
							|| pc.getMapId() >= 1708 && pc.getMapId() <= 1710
							|| pc.getMapId() == 15871 || pc.getMapId() == 15881
							|| pc.getMapId() == 15891 || pc.getMapId() == 10500 || pc
							.getMapId() == 10502))) {
						if (pc.getClan() != null && getClan() != null && pc.getClan() == getClan()) {
							pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, this.getName(), true));
							sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc, pc.getName(), true));
						} else {
							pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this, this.getName(), false));
						}
					} else {
						if (pc.isInvisble()) {
							if ((pc.isInParty() && pc.getParty().isMember(this))
									|| (pc.getClanid() != 0 && pc.getClanid() == getClanid())) {
								pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI
										.make_stream(this, this.getName(), true));
								sendPackets(SC_WORLD_PUT_OBJECT_NOTI
										.make_stream(pc, pc.getName(), true));
							} else {
								pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI
										.make_stream(this, this.getName(),
												false));
							}
						} else {
							pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI
									.make_stream(this, this.getName(), false));
						}
					}
				} else {
					pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
				}
			}

			if (isPinkName()) {
				pc.sendPackets(new S_PinkName(pc.getId(), pc.getPinkNameTime()));
			}

			for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(
					pc)) {
				if (target.isPinkName()) {
					pc.sendPackets(new S_PinkName(target.getId(), target
							.getPinkNameTime()));
				}
			}

			if (isPrivateShop()) {
				pc.sendPackets(new S_DoActionShop(getId(),
						ActionCodes.ACTION_Shop, getShopChat()));
			}
			if (isFishing()) {
				pc.sendPackets(new S_Fishing(getId(),
						ActionCodes.ACTION_Fishing, _fishingX, _fishingY));
			}

			L1Clan clan = L1World.getInstance().getClan(getClanid());
			if (clan != null) {
//				System.out.println(clan.getCastleId());
				if (getMapId() == 13005 || getMapId() == 13006) {

				} else if (clan.getCastleId() != 0) {
					if (isCrown()) {
						if (getId() == clan.getLeaderId()) {
							pc.sendPackets(new S_CastleMaster(clan.getCastleId(), getId()));
						}
					}
					/*SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(this, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, true);
					broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(this, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, true));
				} else {
					SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(this, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false);
					broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(this, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false));
				*/}


			}

			if (hasSkillEffect(L1SkillId.TRUE_TARGET)) {
				if (getTrueTargetClan() == pc.getClanid()
						|| getTrueTargetParty() == pc.getPartyID()) {
					pc.sendPackets(new S_TrueTargetNew(getId(), true));
				}
			}

		} finally {
			L1Party party = getParty();
			if (party != null && party.isMember(pc)) {
				party.handshakePartyMemberStatus(this, pc);
			}
		}
	}

	public void broadcastRemoveAllKnownObjects() {
		for (L1Object known : getKnownObjects()) {
			if (known == null) {
				continue;
			}

			sendPackets(new S_RemoveObject(known));
		}
	}

	/**
	 * TODO 移除try-catch（例外）（速度降低）的處理，導致 物件 不能更新
	 **/
	public void updateObject() {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				updateObject0();
				return new L1PcInstance[0];
			}
		});
	}

	private final Object updateSync = new Object();

	public void updateObject0() {
		synchronized (updateSync) {
			removeOutOfRangeObjects();// 新增
			ArrayList<L1Object> _Vlist = null;
			_Vlist = L1World.getInstance().getVisibleObjects(this,	Config.Connection.PcRecognizeRange);
			for (L1Object visible : _Vlist) {
				if (visible == null)
					continue;
				try {
					if (!knownsObject(visible)) {
						visible.onPerceive(this);
						if ((hasSkillEffect(L1SkillId.GMSTATUS_HPBAR) && L1HpBar.isHpBarTarget(visible))) {
							sendPackets(new S_HPMeter((L1Character) visible));
						}
					} else {
						if (visible instanceof L1NpcInstance) {
							L1NpcInstance npc = (L1NpcInstance) visible;
							if (getLocation().isInScreen(npc.getLocation())	&& npc.getHiddenStatus() != 0) {
								npc.approachPlayer(this);
							}
						}
					}
					if (visible instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) visible;
						if (pc.hasSkillEffect(L1SkillId.TRUE_TARGET)) {
							if (pc.getTrueTargetClan() == getClanid() || pc.getTrueTargetParty() == getPartyID()) {
								sendPackets(new S_TrueTargetNew(pc.getId(),	true));
							}
						}
					} else if (visible instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) visible;
						if (npc.hasSkillEffect(L1SkillId.TRUE_TARGET)) {
							if (npc.getTrueTargetClan() == getClanid()|| npc.getTrueTargetParty() == getPartyID()) {
								sendPackets(new S_TrueTargetNew(npc.getId(), true));
							}
						}
						if (npc instanceof L1MonsterInstance) {
							L1MonsterInstance monster = (L1MonsterInstance) npc;
							monster.onNpcAI();
						}
					}

					if (visible instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) visible;
						if (npc.getNpcId() == 50000220) {
							SC_INDUN_TOWER_HIT_POINT_RATIO_NOTI.send_tower_hit(
									this, npc);
							sendPackets(new S_HPMeter(npc));
						}
					}

					if (hasSkillEffect(L1SkillId.GMSTATUS_HPBAR)&& L1HpBar.isHpBarTarget(visible)) {
						L1Character c = (L1Character) visible;
						if (c.isChangedHpAndUpdate())
							sendPackets(new S_HPMeter(c));
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
				if (!getLocation().isInScreen(known.getLocation())) {
					removeKnownObject(known);
					sendPackets(new S_RemoveObject(known));
				}
			} else {
				if (getLocation().getTileLineDistance(known.getLocation()) > Config.Connection.PcRecognizeRange) {
					removeKnownObject(known);
					sendPackets(new S_RemoveObject(known));
				}
			}
		}
	}

	private void sendVisualEffect() {
		int poisonId = 0;
		if (getPoison() != null) {
			poisonId = getPoison().getEffectId();
		}
		if (getParalysis() != null) {
			poisonId = getParalysis().getEffectId();
		}
		if (poisonId != 0) {
			sendPackets(new S_Poison(getId(), poisonId));
			broadcastPacket(new S_Poison(getId(), poisonId));
		}
	}

	public void sendClanMarks() {
		if (getClanid() != 0) {
			L1Clan clan = L1World.getInstance().getClan(getClanid());
			if (clan != null && isCrown() && getId() == clan.getLeaderId()
					&& clan.getCastleId() != 0) {
				sendPackets(new S_CastleMaster(clan.getCastleId(), getId()));
			} else {
				sendPackets(new S_CastleMaster(clan.getCastleId(), 0));
			}
		}
	}

	public void sendVisualEffectAtLogin() {
		sendVisualEffect();
	}

	public void sendVisualEffectAtTeleport() {
		if (isDrink()) {
		}

		sendVisualEffect();
	}

	@Override
	public void setCurrentHp(int i) {
		// System.out.println("從DB設置的HP值: " + i);
		if (getCurrentHp() == i)
			return;

		/** 2016.11.26 MJ 應用中心 LFC **/
		/* 執行LFC且HP已減少時, */
		if ((getInstStatus() == InstStatus.INST_USERSTATUS_LFC)
				&& i > getCurrentHp())
			addDamageFromLfc(i - getCurrentHp());
		super.setCurrentHp(i);
		sendPackets(new S_HPUpdate(getCurrentHp(), getMaxHp()));
		if (isInParty()) {
			getParty().refreshPartyMemberStatus(this);
		}
		if (isPassive(MJPassiveID.BERSERK.toInt())) {
			int percent = (int) Math
					.round(((double) getCurrentHp() / (double) getMaxHp()) * 100);
			if (percent <= 50) {
				if (!isTitanBerserk()) {
					SC_SPELL_BUFF_NOTI.sendBerserk(this, true);
					setTitanBerserk(true);
					addSpecialResistance(eKind.ALL, 5);
					getResistance().addPVPweaponTotalDamage(5);
					getResistance().addcalcPcDefense(8);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this);
				}
			} else {
				if (isTitanBerserk()) {
					SC_SPELL_BUFF_NOTI.sendBerserk(this, false);
					setTitanBerserk(false);
					addSpecialResistance(eKind.ALL, -5);
					getResistance().addPVPweaponTotalDamage(-5);
					getResistance().addcalcPcDefense(-8);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this);
				}
			}
		}
		if (getHpAr() >0){
			startHpARegeneration();
		} else {
			stopHpARegeneration();
		}
		if (getMpAr() >0){
			startMpARegeneration();
		} else {
			stopMpARegeneration();
		}

	}

	@Override
	public void setCurrentMp(int i) {
		if (getCurrentMp() == i)
			return;
		if (isGm()) // 原本
			i = getMaxMp();

		// i = getMaxMp(); // MP消耗不能用，測試用，請刪除

		super.setCurrentMp(i);
		sendPackets(new S_MPUpdate(getCurrentMp(), getMaxMp()));

		if (isInParty()) {
			// TODO 派對協議
			getParty().refreshPartyMemberStatus(this);
		}
	}


	@Override
	public L1PcInventory getInventory() {
		return _inventory;
	}

	public L1DwarfForPackageInventory getDwarfForPackageInventory() {
		return _dwarfForPackage;
	}

	public L1Inventory getTradeWindowInventory() {
		return _tradewindow;
	}

	public boolean isGmInvis() {
		return _gmInvis;
	}

	public void setGmInvis(boolean flag) {
		_gmInvis = flag;
	}

	public int CubeMr;

	public int getCurrentWeapon() {
		return _currentWeapon;
	}

	public void setCurrentWeapon(int i) {
		_currentWeapon = i;
	}

	private Ability pc;

	public int getType() {
		return _type;
	}

	public void setType(int i) {
		_type = i;
	}

	public short getAccessLevel() {
		return _accessLevel;
	}

	public void setAccessLevel(short i) {
		_accessLevel = i;
	}

	public int getClassId() {
		return _classId;
	}

	public void setClassId(int i) {
		_classId = i;
		_classFeature = L1ClassFeature.newClassFeature(i);
	}

	public L1ClassFeature getClassFeature() {
		return _classFeature;
	}

	public synchronized long getReturnStat() {
		return _returnstat;
	}

	public synchronized void setReturnStat(long i) {
		_returnstat = i;
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
			if (player.knownsObject(this)) {
				player.removeKnownObject(this);
				player.sendPackets(new S_RemoveObject(this));
			}
		}
	}

	private void quitGame() {
		try {
			remove_companion();
			if (!(noPlayerCK || noPlayerck2 || isPrivateShop() || pc != null)) {
				/** 保存日誌檔案 **/
				MJUIAdapter.on_logout_user(getNetConnection(), this);
				System.out.println(String.format("[登出&結束] [帳號:%s] [角色:%s] [IP:%s]", getAccountName(), getName(), getNetConnection().getHostname()));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (isFishing()) {
				FishingTimeController.getInstance().endFishing(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			getMap().setPassable(getLocation(), true);
			// 如果正在死亡（本人），則返回原地，並設為饑餓狀態
			if (isDead()) {
				int[] loc = Getback.GetBack_Location(this, true);
				setX(loc[0]);
				setY(loc[1]);
				setMap((short) loc[2]);
				setCurrentHp(getLevel());
				set_food(39); // 10%
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 停止交易
		try {
			if (getTradeID() != 0) { // 交易中
				L1Trade trade = new L1Trade();
				trade.TradeCancel(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 決鬥中
		try {
			if (getFightId() != 0) {
				L1PcInstance fightPc = (L1PcInstance) L1World.getInstance()
						.findObject(getFightId());
				if (fightPc != null) {
					fightPc.setFightId(0);
					fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL,
							0, 0));
				}
				setFightId(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 登出時從隊伍列表移除
		try {
			if (isInParty() || getParty() != null) { // 參加隊伍中
				getParty().leaveMember(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 離開聊天隊伍
		try {
			if (isInChatParty()) { // 參加聊天隊伍中
				getChatParty().leaveMember(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 從世界地圖中移除寵物

		try {
			Object[] petList = getPetList().values().toArray();
			L1PetInstance pet = null;
			// L1SummonInstance summon = null;
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					pet.unloadMaster();
				}
				// 召喚
				if (petObject instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) petObject;
					summon.onLeaveMaster();
					// summon.Death(null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 從世界地圖中移除魔法娃娃
		try {
			L1DollInstance doll = getMagicDoll();
			if (doll != null) {
				doll.deleteDoll();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Object[] followerList = getFollowerList().values().toArray();
			L1FollowerInstance follower = null;
			for (Object followerObject : followerList) {
				if (followerObject == null)
					continue;
				follower = (L1FollowerInstance) followerObject;
				follower.setParalyzed(true);
				follower.spawn(follower.getNpcTemplate().get_npcId(),
						follower.getX(), follower.getY(),
						follower.getHeading(), follower.getMapId());
				follower.deleteMe();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BQSCharacterDataLoader.out(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 將附魔保存到資料庫的 character_buff 中
		try {
			if (Config.ServerAdSetting.DelayTimer) {
				ItemDelayTimer.SaveItemDelay(this);
			}
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
				/*if (item.getItemId() == 41921) {
					if (getAccount().getTotalFeatherCount() != item.getCount()) {
						getAccount().setTotalFeatherCount(item.getCount());
					}
				}*/
				if (item.getCount() <= 0) {
					getInventory().deleteItem(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 更新阿因點數
		try {

			AinhasadSpecialStatLoader.getInstance().updateSpecialStat(this);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 停止PC的監控
		stopEtcMonitor();
		// 將在線狀態設為OFF，並將角色資訊寫入資料庫
		setOnlineStatus(0);

		try {
			save();
			saveInventory();
			L1BookMark.WriteBookmark(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean _destroyed = false;

	public void logout() {
		// StackTraceElement[] a = new Throwable().getStackTrace();
		// for(int i = a.length - 1; i > 0 ; i--){
		// System.out.print("類別 - " + a[i].getClassName());
		// System.out.print(", 方法 - "+a[i].getMethodName());
		// System.out.print(", 行號 - "+a[i].getLineNumber());
		// System.out.print(", 檔案 - "+a[i].getFileName());
		// System.out.println();
		// }
		try {
			synchronized (this) {
				if (_destroyed) {
					return;
				}
				_destroyed = true;

				stopMacroTimer();

				for (int i = 0; i <= 80; ++i) {
					sendPackets(new S_LetterList(this, 48, i, true));
					sendPackets(new S_LetterList(this, 49, i, true));
					sendPackets(new S_LetterList(this, 50, i, true));
				}

				if (indun_model() != null) {
					MJIndunRoomModel model = indun_model();
					model.onClearRoom(this);
				}

				remove_companion();

				if (getAccount() != null) {
					getAccount().updateBlessOfAin();
					/** 最後的帳號登出更新 **/
					getAccount().updateLastLogOut();
				}

				// CharacterCustomQuestTable.save(this); // 註釋

				DungeonTimeProgressLoader.update(this);
				if (!isUnmannedShop()) {
					SC_ATTENDANCE_USER_DATA_EXTEND userData = getAttendanceData();
					if (userData != null) {
						SC_ATTENDANCE_USER_DATA_EXTEND.update(getAccountName(), userData);
						setAttendanceData(null);
					}
				}
				if (is_combat_field()) {
					MJCombatObserver observer = MJCombatLoadManager
							.getInstance().get_current_observer(
									get_current_combat_id());
					if (observer != null) {
						observer.remove(this);
					}
				}

				MJIndunRoomController.getInstance().end_user_room(this, -1);
				MJRankUserLoader.getInstance().offUser(this);
				MJRaidSpace.getInstance().getBackPc(this);

				/** 2016.11.26 MJ 應用中心 LFC **/
				MJInstanceSpace.getInstance().getBackPc(this);
				/** 2016.11.26 MJ 應用中心 LFC **/

				CharacterSlotItemTable.getInstance().updateCharSlotItems(this);
				_slotItemTwo.clear();
				_slotItemOne.clear();
				_slotItemThree.clear();
				_slotItemFour.clear();
				_slotcolor.clear();

				quitGame();
				L1World world = L1World.getInstance();
				notifyPlayersLogout(getKnownPlayers());
				world.removeVisibleObject(this);
				world.removeObject(this);
				notifyPlayersLogout(world.getRecognizePlayer(this));
				_inventory.clearItems();
				/*
				 * WarehouseManager w = WarehouseManager.getInstance();
				 * w.delPrivateWarehouse(this.getAccountName());
				 * w.delElfWarehouse(this.getAccountName());
				 * w.delSpecialWarehouse(this.getName());
				 */
				_dwarfForPackage.clearItems();
				removeAllKnownObjects();

				// 重生註釋
				dispose_regenerator();
				// stopHpMpRegeneration();
				// stopMpRegeneration();
				stopHalloweenRegeneration();
				stopAHRegeneration();
				stopSHRegeneration();
				// stopMpDecreaseByScales();
				stopEquipmentTimer();
				setDead(true);
				setNetConnection(null);
				stopEinhasadTimer();
				stopPapuBlessing();// 帕普里昂的祝福
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
			if (_timeCollection != null) {
				L1TimeCollectionUserLoader.getInstance().merge(this);
				_timeCollection.dispose();
				_timeCollection = null;
			}
		} catch (Exception e) {
			System.out.println(String.format("[登出] Selektis 展覽更新錯誤 (%s)", getName()));
			e.printStackTrace();
		}
		try {
			if (_favorBook != null) {
				L1FavorBookUserLoader.getInstance().merge(this);
				_favorBook.dispose();
				_favorBook = null;
			}
		} catch (Exception e) {
			System.out.println(String.format("[登出] 聖物庫存更新錯誤 (%s)", getName()));
			e.printStackTrace();
		}
	}

	private static Timer _regenTimer;
	private static Timer _blessingTimer;

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
		return _netConnection;
	}

	public void setNetConnection(GameClient clientthread) {
		_netConnection = clientthread;
	}

	public boolean isInParty() {
		return getParty() != null;
	}

	public L1Party getParty() {
		return _party;
	}

	public void setParty(L1Party p) {
		_party = p;
	}

	public boolean isInChatParty() {
		return getChatParty() != null;
	}

	public L1ChatParty getChatParty() {
		return _chatParty;
	}

	public void setChatParty(L1ChatParty cp) {
		_chatParty = cp;
	}

	public int getPartyID() {
		return _partyID;
	}

	public void setPartyID(int partyID) {
		_partyID = partyID;
	}

	public int getPartyType() {
		return _partyType;
	}

	public void setPartyType(int partyType) {
		_partyType = partyType;
	}

	public int getTradeID() {
		return _tradeID;
	}

	public void setTradeID(int tradeID) {
		_tradeID = tradeID;
	}

	public void setTradeOk(boolean tradeOk) {
		_tradeOk = tradeOk;
	}

	public boolean getTradeOk() {
		return _tradeOk;
	}

	public int getTempID() {
		return _tempID;
	}

	public void setTempID(int tempID) {
		_tempID = tempID;
	}

	// public boolean isTeleport() {
	// return _isTeleport;
	// }

	// public void setTeleport(boolean flag) {
	// _isTeleport = flag;
	// if (flag)
	// AttackController.stop(this);
	// }

	public boolean isTeleportWaiting() {
		boolean _teleportWaiting = false;
		return _teleportWaiting;
	}

	public void setTeleportWaiting(boolean flag) {
		boolean _teleportWaiting = flag;
	}

	public boolean isDrink() {
		return _isDrink;
	}

	public void setDrink(boolean flag) {
		_isDrink = flag;
	}

	public boolean isGres() {
		return _isGres;
	}

	public void setGres(boolean flag) {
		_isGres = flag;
	}

	public boolean isPinkName() {
		return _isPinkName;
	}

	public void setPinkName(boolean flag) {
		_isPinkName = flag;
	}

	public void setShopChat(byte[] chat) {
		_shopChat = chat;
	}

	public byte[] getShopChat() {
		return _shopChat;
	}

	public boolean isPrivateShop() {
		return _isPrivateShop;
	}

	public void setPrivateShop(boolean flag) {
		_isPrivateShop = flag;
	}

	public boolean isPrivateReady() {
		return _isPrivateReady;
	}

	public void setPrivateReady(boolean b) {
		_isPrivateReady = b;
	}

	private int _special_size;

	public int get_SpecialSize() {
		return _special_size;
	}

	public void set_SpecialSize(int special_size) {
		_special_size = special_size;
	}

	public int getPartnersPrivateShopItemCount() {
		return _partnersPrivateShopItemCount;
	}

	public void setPartnersPrivateShopItemCount(int i) {
		_partnersPrivateShopItemCount = i;
	}

	private int birthday;// 生日

	public int getBirthDay() {
		return birthday;
	}

	public void setBirthDay(int t) {
		birthday = t;
	}

	private int _TelType = 0;

	public int getTelType() {
		return _TelType;
	}

	public void setTelType(int i) {
		_TelType = i;
	}

	private int AinState = 0;

	public int getAinState() {
		return AinState;
	}

	public void setAinState(int AinState) {
		this.AinState = AinState;
	}

	public int[] DragonPortalLoc = new int[3];// 龍之門戶

	public void sendPackets(MJIProtoMessage message, int messageId) {
		sendPackets(message, messageId, true);
	}

	public void sendPackets(MJIProtoMessage message, MJEProtoMessages e,
	                        boolean isClear) {
		sendPackets(message, e.toInt(), isClear);
	}

	public void sendPackets(MJIProtoMessage message, MJEProtoMessages e) {
		sendPackets(message, e, true);
	}

	public void sendPackets(MJIProtoMessage message, int messageId,
	                        boolean isClear) {
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

	/** boolean 值表示在發送數據包後是否清除。 */
	public void sendPackets(ProtoOutputStream stream, boolean isClear) {
		if (getNetConnection() != null)
			getNetConnection().sendPacket(stream, isClear);
	}

	public void sendPackets(ProtoOutputStream stream) {
		if (getNetConnection() != null)
			getNetConnection().sendPacket(stream);
	}

	public void sendPackets(ServerBasePacket[] pcks, boolean clear) {
		if (getNetConnection() != null) {
			for (ServerBasePacket pck : pcks)
				getNetConnection().sendPacket(pck, clear);
		}
	}

	public void sendPackets(ServerBasePacket serverbasepacket, boolean clear) {
		try {
			if (getNetConnection() != null) {
				getNetConnection().sendPacket(serverbasepacket, clear);
			}
		} catch (Exception e) {
		}
	}

	public void sendPackets(String s) {
		sendPackets(new S_SystemMessage(s), true);
	}

	public void sendPackets(int code, String value) {
		sendPackets(new S_ServerMessage(code, value), true);
	}

	public void sendPackets(int code) {
		sendPackets(new S_ServerMessage(code), true);
	}

	public void sendPackets(ServerBasePacket serverbasepacket) {
		if (getNetConnection() == null)
			return;

		try {
			getNetConnection().sendPacket(serverbasepacket);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	@Override
	public void onAction(L1PcInstance attacker) {
		if (attacker == null) {
			return;
		}

		if (get_teleport() && getAI() == null) {
			return;
		}

		if (getZoneType() == 1 || attacker.getZoneType() == 1) {
			L1Attack attack_mortion = new L1Attack(attacker, this);
			attack_mortion.action();
			return;
		}

		if (checkNonPvP(this, attacker) == true) {
			return;
		}

		if (getCurrentHp() > 0 && !isDead()) {
			attacker.delInvis();

			boolean isCounterBarrier = false;
			boolean isMortalBody = false;
			boolean isConqure = false;
			L1Attack attack = new L1Attack(attacker, this);
			L1Magic magic = null;

			if (attack.calcHit()) {
				if (hasSkillEffect(L1SkillId.COUNTER_BARRIER)) {
					// TODO 當處於反擊屏障狀態時，如果有下面的技能則不會觸發
					if (!hasSkillEffect(L1SkillId.SHOCK_STUN)
							&& !hasSkillEffect(L1SkillId.CRUEL)
							&& !hasSkillEffect(L1SkillId.EMPIRE)
							&& !hasSkillEffect(L1SkillId.BONE_BREAK)
							&& !hasSkillEffect(L1SkillId.OSIRIS)
							&& !hasSkillEffect(L1SkillId.OSIRIS_TICK)
							&& !hasSkillEffect(L1SkillId.FORCE_STUN)
							&& !hasSkillEffect(L1SkillId.FORCE_STUN_FAIL)
							&& !hasSkillEffect(L1SkillId.MOB_RANGESTUN_18)
							&& !hasSkillEffect(L1SkillId.MOB_RANGESTUN_19)
							&& !hasSkillEffect(L1SkillId.MOB_SHOCKSTUN_30)
							&& !hasSkillEffect(L1SkillId.BOS_STUN18)
							&& !hasSkillEffect(L1SkillId.OMAN_STUN)
							&& !hasSkillEffect(L1SkillId.fornos_STUN)
							&& !hasSkillEffect(L1SkillId.MOSTER_STUN_1)
							&& !hasSkillEffect(L1SkillId.Besi_STUN)
							&& !hasSkillEffect(L1SkillId.Moster_STUN)
							&& !hasSkillEffect(L1SkillId.Maeno_STUN)
							&& !hasSkillEffect(L1SkillId.ANTA_MESSAGE_6)
							&& !hasSkillEffect(L1SkillId.ANTA_MESSAGE_7)
							&& !hasSkillEffect(L1SkillId.ANTA_MESSAGE_8)
							&& !hasSkillEffect(L1SkillId.ANTA_SHOCKSTUN)
							&& !hasSkillEffect(L1SkillId.CHAINSWORD_STUN)
							&& !hasSkillEffect(L1SkillId.BALOCH_STUN)
							&& !hasSkillEffect(L1SkillId.DRAGON_HALPAS_STUN)) {
						magic = new L1Magic(this, attacker);
						boolean isProbability = magic
								.calcProbabilityMagic(L1SkillId.COUNTER_BARRIER);
						boolean isShortDistance = attack.isShortDistance();
						if (isProbability && isShortDistance) {
							if (isPassive(MJPassiveID.COUNTER_BARRIER_MASTER
									.toInt())) {
								int hp_bonus = getCurrentHp()
										+ (getAbility().getTotalCon() / 2);
								isCounterBarrier = true;
								this.setCurrentHp(hp_bonus);
							} else {
								if (attacker != null
										&& attacker
										.isPassive(MJPassiveID.PARADOX
												.toInt())
										&& MJRnd.isWinning(
										1000000,
										Config.MagicAdSetting_Fencer.PARADOXPROBABILITY)) {
									attacker.send_effect(18518);
									isCounterBarrier = false;
								} else {
									isCounterBarrier = true;
								}
							}
						}
					}
				} else if (hasSkillEffect(L1SkillId.MORTAL_BODY)) {
					magic = new L1Magic(this, attacker);
					boolean isProbability = magic
							.calcProbabilityMagic(L1SkillId.MORTAL_BODY);
					boolean isShortDistance1 = attack.isShortDistance1();
					if (isProbability && isShortDistance1) {
						if (attacker != null
								&& attacker.isPassive(MJPassiveID.PARADOX
								.toInt())
								&& MJRnd.isWinning(
								1000000,
								Config.MagicAdSetting_Fencer.PARADOXPROBABILITY)) {
							attacker.send_effect(18518);
							isMortalBody = false;
						} else {
							isMortalBody = true;
						}
					}
				} else if (isPassive(MJPassiveID.CONQUEROR.toInt())) { // 需要處理概率
					if (getWeapon() != null) {
						L1ItemInstance weapon = getWeapon();
						if (weapon.getItem().getType() == 1 ||weapon.getItem().getType() == 2) {
							magic = new L1Magic(this, attacker);
							boolean isProbability = magic.calcProbabilityMagic(L1SkillId.CONQUEROR);
							boolean isShortDistance = attack.isShortDistance();
							if (isShortDistance & isProbability) {
								isConqure = true;
							}
						}
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
				/** 讓施法者也會扣血 **/
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

		if (getWeapon() == null || !getWeapon().isSpecialEnchantable()) {
			return;
		}

		for (int i = 1; i <= 3; ++i) {
			int specialEnchant = getWeapon().getSpecialEnchant(i);

			if (specialEnchant == 0) {
				break;
			}

			if (_random.nextInt(100) >= 1) {
				continue;
			}

			boolean success = true;

			switch (specialEnchant) {
				// 這裡按性能處理
				case L1ItemInstance.CHAOS_SPIRIT:
					success = false;
					break;
				case L1ItemInstance.CORRUPT_SPIRIT:
					new L1SkillUse().handleCommands(this, L1SkillId.COUNTER_MAGIC,
							getId(), getX(), getY(), null, 0,
							L1SkillUse.TYPE_GMBUFF);
					break;
				case L1ItemInstance.ANTARAS_SPIRIT:
				case L1ItemInstance.BALLACAS_SPIRIT:
				case L1ItemInstance.LINDBIOR_SPIRIT:
					success = false;
					break;
				case L1ItemInstance.PAPURION_SPIRIT:
					if (attacker.hasSkillEffect(L1SkillId.STATUS_BRAVE)
							|| attacker.hasSkillEffect(L1SkillId.STATUS_HASTE)
							|| attacker.hasSkillEffect(L1SkillId.HOLY_WALK)
							|| attacker
							.hasSkillEffect(L1SkillId.MOVING_ACCELERATION)) {
						attacker.killSkillEffectTimer(L1SkillId.STATUS_BRAVE);
						attacker.killSkillEffectTimer(L1SkillId.STATUS_HASTE);
						attacker.killSkillEffectTimer(L1SkillId.HOLY_WALK);
						attacker.killSkillEffectTimer(L1SkillId.MOVING_ACCELERATION);
						attacker.sendPackets(new S_SkillBrave(attacker.getId(), 0,
								0));
						attacker.broadcastPacket(new S_SkillBrave(attacker.getId(),
								0, 0));
						attacker.setBraveSpeed(0);
						attacker.sendPackets(new S_SkillHaste(attacker.getId(), 0,
								0));
						attacker.broadcastPacket(new S_SkillHaste(attacker.getId(),
								0, 0));
						attacker.setMoveSpeed(0);
					}
					break;
				case L1ItemInstance.DEATHKNIGHT_SPIRIT:
				case L1ItemInstance.BAPPOMAT_SPIRIT:
					success = false;
					break;
				case L1ItemInstance.BALLOG_SPIRIT:
					break;
				case L1ItemInstance.ARES_SPIRIT:
					success = false;
					break;
			}

			if (success) {
				break; // 同時不能觸發兩個以上。
			}
		}
	}

	public boolean checkNonPvP(L1PcInstance pc, L1Character target) {
		L1PcInstance targetpc = null;
		if (target instanceof L1PcInstance) {
			targetpc = (L1PcInstance) target;
		} else if (target instanceof MJCompanionInstance) {
			targetpc = ((MJCompanionInstance) target).get_master();
		} else if (target instanceof L1PetInstance) {
			targetpc = (L1PcInstance) ((L1PetInstance) target).getMaster();
		} else if (target instanceof L1SummonInstance) {
			targetpc = (L1PcInstance) ((L1SummonInstance) target).getMaster();
		}
		if (targetpc == null) {
			return false;
		}
		if (!Config.ServerAdSetting.SERVERPVPSETTING) {
			if (getMap().isCombatZone(getLocation())) {
				return false;
			}

			L1Clan clan = pc.getClan();
			L1Clan enemyclan = targetpc.getClan();
			if (clan != null && enemyclan != null) {
				MJWar war = clan.getCurrentWar();
				MJWar enemyWar = enemyclan.getCurrentWar();
				if (war != null && enemyWar != null && war.equals(enemyWar))
					return false;
			}

			if (target instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) target;
				if (isInWarAreaAndWarTime(pc, targetPc)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean isInWarAreaAndWarTime(L1PcInstance pc, L1PcInstance target) {
		int castleId = L1CastleLocation.getCastleIdByArea(pc);
		int targetCastleId = L1CastleLocation.getCastleIdByArea(target);
		if (castleId != 0 && targetCastleId != 0 && castleId == targetCastleId) {
			if (MJCastleWarBusiness.getInstance().isNowWar(castleId)) {
				return true;
			}
		}
		return false;
	}

	public void setPetTarget(L1Character target) {
		Object[] petList = getPetList().values().toArray();
		L1PetInstance pets = null;
		L1SummonInstance summon = null;
		for (Object pet : petList) {
			if (pet == null)
				continue;
			if (pet instanceof L1PetInstance) {
				pets = (L1PetInstance) pet;
				pets.setMasterTarget(target);
			} else if (pet instanceof L1SummonInstance) {
				summon = (L1SummonInstance) pet;
				summon.setMasterTarget(target);
			}
		}
	}

	public void set_pet_target(L1Character target) {
		setPetTarget(target);
		if (m_companion != null)
			m_companion.set_target(target);
	}

	// TODO 添加昏迷類型
	public boolean isstop() {
		return (hasSkillEffect(SHOCK_STUN))
				|| hasSkillEffect(L1SkillId.PANTHERA)
				|| hasSkillEffect(L1SkillId.EMPIRE)
				|| (hasSkillEffect(ICE_LANCE))
				|| hasSkillEffect(L1SkillId.ETERNITI)
				/* || hasSkillEffect(L1SkillId.TEMPEST) */
				|| hasSkillEffect(L1SkillId.FORCE_STUN)
				|| (hasSkillEffect(BONE_BREAK))
				|| (hasSkillEffect(EARTH_BIND))
				|| (hasSkillEffect(L1SkillId.CRUEL))
				|| (hasSkillEffect(MOB_RANGESTUN_19))
				|| (hasSkillEffect(MOB_SHOCKSTUN_30))
				|| (hasSkillEffect(OMAN_STUN))
				|| (hasSkillEffect(BOS_STUN18))
				|| (hasSkillEffect(BALOCH_STUN))
				|| (hasSkillEffect(Maeno_STUN))
				|| (hasSkillEffect(Besi_STUN))
				|| (hasSkillEffect(ANTA_MESSAGE_6))
				|| (hasSkillEffect(DRAGON_HALPAS_STUN))
				|| (hasSkillEffect(fornos_STUN))
				|| (hasSkillEffect(MOSTER_STUN_1))
				|| (hasSkillEffect(Moster_STUN))
				|| (hasSkillEffect(ANTA_MESSAGE_7))
				|| (hasSkillEffect(ANTA_MESSAGE_8))
				|| (hasSkillEffect(ANTA_SHOCKSTUN))
				|| hasSkillEffect(L1SkillId.TEMPEST)
				|| hasSkillEffect(L1SkillId.CONQUEROR_STUN)
				|| hasSkillEffect(L1SkillId.TRIPLE_STUN)
				|| (hasSkillEffect(L1SkillId.DISINTEGRATE)) || (hasSkillEffect(CHAINSWORD_STUN));
	}

	public void delInvis() {
		if (isGm()){
			return;
		}
		if (hasSkillEffect(L1SkillId.INVISIBILITY)) {
			killSkillEffectTimer(L1SkillId.INVISIBILITY);
			sendPackets(new S_Invis(getId(), 0));
			broadcastPacket(new S_Invis(getId(), 0));
			broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
			if (getMagicDoll() != null) {
				sendPackets(new S_Invis(getMagicDoll().getId(), 0));
				broadcastPacket(new S_Invis(getMagicDoll().getId(), 0));
				broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI
						.make_stream(getMagicDoll()));
			}
		}
		if (hasSkillEffect(L1SkillId.BLIND_HIDING)) {
			killSkillEffectTimer(L1SkillId.BLIND_HIDING);
			sendPackets(new S_Invis(getId(), 0));
			broadcastPacket(new S_Invis(getId(), 0));
			broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
			if (getMagicDoll() != null) {
				sendPackets(new S_Invis(getMagicDoll().getId(), 0));
				broadcastPacket(new S_Invis(getMagicDoll().getId(), 0));
				broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI
						.make_stream(getMagicDoll()));
			}
			if (isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())) {
				sendPackets(new S_Invis(getId(), 0));
				broadcastPacket(new S_Invis(getId(), 0));
				broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
				addMoveDelayRate(-50);
			}

		}

		L1DollInstance doll = getMagicDoll();
		if (doll != null) {
			for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(
					this)) {
				doll.onPerceive(pc);
			}
		}
	}

	public void delBlindHiding() {
		killSkillEffectTimer(L1SkillId.BLIND_HIDING);
		if (isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())) {
			addMoveDelayRate(-50);
		}
		sendPackets(new S_Invis(getId(), 0));
		broadcastPacket(new S_Invis(getId(), 0));
		broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
	}

	public void receiveDamage(L1Character attacker, int damage, int attr) {
		if (damage == 0)
			return;
		Random random = new Random(System.nanoTime());
		int player_mr = getResistance().getEffectedMrBySkill();
		int rnd = random.nextInt(100) + 1;
		if (player_mr >= rnd) {
			damage /= 2;
		}

		receiveDamage(attacker, damage);
	}

	public void receiveManaDamage(L1Character attacker, int mpDamage) {
		if (mpDamage > 0 && !isDead()) {
			delInvis();
			if (attacker instanceof L1PcInstance) {
				L1PinkName.onAction(this, attacker);
			}
			int newMp = getCurrentMp() - mpDamage;
			this.setCurrentMp(newMp);
		}
	}

	public boolean isInWarArea() {
		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(this);

		if (castleId != 0) {
			isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
		}
		return isNowWar;
	}

	public void receiveCounterBarrierDamage(L1Character attacker, int damage) {
		try {
			if (getCurrentHp() > 0 && !isDead()) {
				if (attacker != null && attacker != this
						&& !knownsObject(attacker)
						&& attacker.getMapId() == this.getMapId()) {
					attacker.onPerceive(this);
				}

				if (damage > 0) {
					delInvis();
					if (hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
						removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
					} else if (hasSkillEffect(L1SkillId.PHANTASM)) {
						removeSkillEffect(L1SkillId.PHANTASM);
					}
					if (attacker.instanceOf(MJL1Type.L1TYPE_PC)) {
						if (is_combat_field()) {
							MJCombatObserver observer = MJCombatLoadManager
									.getInstance().get_current_observer(
											get_current_combat_id());
							if (observer != null) {
								observer.on_damage((L1PcInstance) attacker,
										this, damage);
							} else {
								set_instance_status(MJEPcStatus.WORLD);
							}
						}
					}
				} else if (damage < 0) {
					return;
				}
				if (getInventory().checkEquipped(145)
						|| getInventory().checkEquipped(149)) {
					damage *= 1.5;
				}

				if (hasSkillEffect(L1SkillId.PRESHER)) {
					double presher_dmg = 0;
					if (attacker == getPresherPc()) {
						presher_dmg = damage
								* Config.MagicAdSetting_Lancer.PRESHER_PCPCDMG;
					} else {
						presher_dmg = damage
								* Config.MagicAdSetting_Lancer.PRESHER_ETCPCDMG;
					}
					addPresherDamage((int) presher_dmg);
				}

				int newHp = getCurrentHp() - damage;
				if (newHp > getMaxHp()) {
					newHp = getMaxHp();
				}

				if (newHp <= 10) {
//                    if (newHp <= 10 && is_halpas_armor() && ishalpaspaith()
//                            && !hasSkillEffect(L1SkillId.DRAGON_ARMOR_BLESSING)) { // 將 0 更改為 11
// 修改
					if (newHp <= 10 && is_halpas_armor() && ishalpaspaith()
							&& checkHalpasTime()) {

						setCurrentHp(getMaxHp());
						setCurrentMp(getMaxMp());
						sendPackets(new S_HPUpdate(getCurrentHp(), getMaxHp()));
						sendPackets(new S_MPUpdate(getCurrentMp(), getMaxMp()));
						int enchant_gap = get_halpas_armor_enchant();
						int DRAGON_ARMOR_BLESSING_TIME = ((3600 * (22 - (enchant_gap * 2)) * 1000));
						int DRAGON_ARMOR_BLESSING_REDUC_TIME = 12000 / 1000;
						HalpasArmor.setHalpas_Time(new Timestamp(System.currentTimeMillis()+DRAGON_ARMOR_BLESSING_TIME));
						getInventory().updateItem(HalpasArmor, L1PcInventory.COL_SAVE_ALL);
						getInventory().saveItem(HalpasArmor, L1PcInventory.COL_SAVE_ALL);
//                        setSkillEffect(L1SkillId.DRAGON_ARMOR_BLESSING,
//                                DRAGON_ARMOR_BLESSING_TIME); // 22小時
// 冷卻時間
						L1SkillUse.off_icons(this, L1SkillId.DRAGON_ARMOR_EQUIP);
						L1SkillUse.on_icons(this, L1SkillId.DRAGON_ARMOR_BLESSING,DRAGON_ARMOR_BLESSING_TIME / 1000);
						set_halpas_faith_pvp_reduc(get_halpas_armor_enchant());
						getResistance().addcalcPcDefense(12 + get_halpas_armor_enchant());
						setSkillEffect(L1SkillId.DRAGON_ARMOR_BLESSING_REDUC,DRAGON_ARMOR_BLESSING_REDUC_TIME); // 秒
						L1SkillUse.on_icons(this,L1SkillId.DRAGON_ARMOR_BLESSING_REDUC,DRAGON_ARMOR_BLESSING_REDUC_TIME);
						send_party_effect(19074);
					} else if (isElf()
							&& hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
						if (isPassive(MJPassiveID.SOUL_BARRIER_ARMOR.toInt())) {
							if (attacker instanceof L1PcInstance) {
								int level = getLevel();
								int rate = 8;

								if (level > 90)
									rate += ((level - 90) / 2) * 2;

								if (rate > 20)
									rate = 20;

								damage -= rate;
							} else {
								int level = getLevel();
								int rate = 4;

								if (level > 90)
									rate += ((level - 90) / 2);

								if (rate > 10)
									rate = 10;

								damage -= rate;
							}
						}
						int newMp = getCurrentMp() - damage;
						this.setCurrentHp(10);
						if (newMp <= 0) {
							death(attacker, true);
							this.setCurrentHp(0);
						}
						this.send_effect(14541, true);
						this.setCurrentMp(newMp);
					} else if (newHp <= 0) {
						if (isGm()) {
							this.setCurrentHp(getMaxHp());
						} else {
							/** 處理反擊屏障和泰坦傷害 **/
							if (isDeathMatch()) {
								if (getMapId() == 5153) {
									try {
										save();
										beginGhost(getX(), getY(),
												(short) getMapId(), true);
										sendPackets(new S_ServerMessage(1271));
									} catch (Exception e) {
										e.printStackTrace();
									}
									return;
								}
							} else {
								death(attacker, false);
							}
						}
					}
				}
				// TODO 別使用 else if！特定技能時 HP 不低於 6（嚴重）哄不好那種 XD
				if (newHp > 0) {
					this.setCurrentHp(newHp);
				}
			} else if (!isDead()) {
				death(attacker, false);
				System.out.println(String.format(
						"■■■■ HP 減少處理錯誤 \r\n%s -> %s : %d / HP:",
						attacker == null ? "null" : attacker.getName(),
						getName(), damage, getMaxHp()));
				sendPackets("如果無法使用技能或物品，請重啟遊戲。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(String.format("%s -> %s : %d",
					attacker == null ? "null" : attacker.getName(), getName(),
					damage));
		}
	}

	public void receiveConqureDamage(L1Character attacker, int damage) {
		try {
			if (getCurrentHp() > 0 && !isDead()) {
				if (attacker != null && attacker != this
						&& !knownsObject(attacker)
						&& attacker.getMapId() == this.getMapId()) {
					attacker.onPerceive(this);
				}

				if (damage > 0) {
					delInvis();
					if (hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
						removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
					} else if (hasSkillEffect(L1SkillId.PHANTASM)) {
						removeSkillEffect(L1SkillId.PHANTASM);
					}
					if (attacker.instanceOf(MJL1Type.L1TYPE_PC)) {
						if (is_combat_field()) {
							MJCombatObserver observer = MJCombatLoadManager
									.getInstance().get_current_observer(
											get_current_combat_id());
							if (observer != null) {
								observer.on_damage((L1PcInstance) attacker,
										this, damage);
							} else {
								set_instance_status(MJEPcStatus.WORLD);
							}
						}
					}
					L1Magic magic = new L1Magic(attacker, this);
					boolean isStun = magic.calcProbabilityMagic(L1SkillId.CONQUEROR_STUN);
					if (isStun) {

					}


				} else if (damage < 0) {
					return;
				}
				if (hasSkillEffect(L1SkillId.PRESHER)) {
					double presher_dmg = 0;
					if (attacker == getPresherPc()) {
						presher_dmg = damage
								* Config.MagicAdSetting_Lancer.PRESHER_PCPCDMG;
					} else {
						presher_dmg = damage
								* Config.MagicAdSetting_Lancer.PRESHER_ETCPCDMG;
					}
					addPresherDamage((int) presher_dmg);
				}

				int newHp = getCurrentHp() - damage;
				if (newHp > getMaxHp()) {
					newHp = getMaxHp();
				}

				if (newHp <= 10) {
//                    if (newHp <= 10 && is_halpas_armor() && ishalpaspaith()
//                            && !hasSkillEffect(L1SkillId.DRAGON_ARMOR_BLESSING)) { // 將 0 更改為 11
// 修改
					if (newHp <= 10 && is_halpas_armor() && ishalpaspaith()
							&& checkHalpasTime()) {

						setCurrentHp(getMaxHp());
						setCurrentMp(getMaxMp());
						sendPackets(new S_HPUpdate(getCurrentHp(), getMaxHp()));
						sendPackets(new S_MPUpdate(getCurrentMp(), getMaxMp()));
						int enchant_gap = get_halpas_armor_enchant();
						int DRAGON_ARMOR_BLESSING_TIME = ((3600 * (22 - (enchant_gap * 2)) * 1000));
						int DRAGON_ARMOR_BLESSING_REDUC_TIME = 12000 / 1000;
						HalpasArmor.setHalpas_Time(new Timestamp(System.currentTimeMillis()+DRAGON_ARMOR_BLESSING_TIME));
						getInventory().updateItem(HalpasArmor, L1PcInventory.COL_SAVE_ALL);
						getInventory().saveItem(HalpasArmor, L1PcInventory.COL_SAVE_ALL);
//                        setSkillEffect(L1SkillId.DRAGON_ARMOR_BLESSING,
//                                DRAGON_ARMOR_BLESSING_TIME); // 22小時
// 冷卻時間
						L1SkillUse.off_icons(this, L1SkillId.DRAGON_ARMOR_EQUIP);
						L1SkillUse.on_icons(this, L1SkillId.DRAGON_ARMOR_BLESSING,DRAGON_ARMOR_BLESSING_TIME / 1000);
						set_halpas_faith_pvp_reduc(get_halpas_armor_enchant());
						getResistance().addcalcPcDefense(12 + get_halpas_armor_enchant());
						setSkillEffect(L1SkillId.DRAGON_ARMOR_BLESSING_REDUC,DRAGON_ARMOR_BLESSING_REDUC_TIME); // 秒
						L1SkillUse.on_icons(this,L1SkillId.DRAGON_ARMOR_BLESSING_REDUC,DRAGON_ARMOR_BLESSING_REDUC_TIME);
						send_party_effect(19074);
					} else if (isElf()
							&& hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
						if (isPassive(MJPassiveID.SOUL_BARRIER_ARMOR.toInt())) {
							if (attacker instanceof L1PcInstance) {
								int level = getLevel();
								int rate = 8;

								if (level > 90)
									rate += ((level - 90) / 2) * 2;

								if (rate > 20)
									rate = 20;

								damage -= rate;
							} else {
								int level = getLevel();
								int rate = 4;

								if (level > 90)
									rate += ((level - 90) / 2);

								if (rate > 10)
									rate = 10;

								damage -= rate;
							}
						}
						int newMp = getCurrentMp() - damage;
						this.setCurrentHp(10);
						if (newMp <= 0) {
							death(attacker, true);
							this.setCurrentHp(0);
						}
						this.send_effect(14541, true);
						this.setCurrentMp(newMp);
					} else if (newHp <= 0) {
						if (isGm()) {
							this.setCurrentHp(getMaxHp());
						} else {
							/** 反擊屏障和泰坦傷害處理 **/
							if (isDeathMatch()) {
								if (getMapId() == 5153) {
									try {
										save();
										beginGhost(getX(), getY(),
												(short) getMapId(), true);
										sendPackets(new S_ServerMessage(1271));
									} catch (Exception e) {
										e.printStackTrace();
									}
									return;
								}
							} else {
								death(attacker, false);
							}
						}
					}
				}
				// TODO 別使用 else if！特定技能時 HP 不低於 6（嚴重）哄不好那種 XD
				if (newHp > 0) {
					this.setCurrentHp(newHp);
				}
			} else if (!isDead()) {
				death(attacker, false);
				System.out.println(String.format(
						"■■■■ HP 減少處理錯誤 \r\n%s -> %s : %d / HP:",
						attacker == null ? "null" : attacker.getName(),
						getName(), damage, getMaxHp()));
				sendPackets("如果無法使用技能或物品，請重新啟動遊戲。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(String.format("%s -> %s : %d",
					attacker == null ? "null" : attacker.getName(), getName(),
					damage));
		}
	}

	private int calculate_sprite_pvp_damage(int sprite_id) { // 根據變身狀態進行 PVP 加成
		// 傷害
		switch (sprite_id) {
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
			case 13715:
			case 13717:
			case 15115:
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
			case 20102:
			case 20101:
			case 20103:
			case 20104:
			case 20442:
			case 20438:
			case 20446: // 阿頓
				return 2;
		}
		return 0;
	}

	public void receiveDamage(L1Character attacker, int damage) {
		if (damage > 0) {
			if (attacker == this) {

				return;
			}
			if (hasSkillEffect(L1SkillId.DEVINE_PROTECTION)
					&& get_divine_protection() - damage >= 0) {
				set_divine_protection(get_divine_protection() - damage);
				sendPackets(SC_INSTANCE_HP_NOTI.make_stream(this), true);
				return;
			} else if (hasSkillEffect(L1SkillId.DEVINE_PROTECTION)
					&& get_divine_protection() - damage < 0) {
				damage -= get_divine_protection();
				send_effect(20141, true);
				removeSkillEffect(L1SkillId.DEVINE_PROTECTION);
				sendPackets(SC_INSTANCE_HP_NOTI.make_stream(this), true);
			}

			if (hasSkillEffect(L1SkillId.LUCIFER) && damage > 0) {
				damage -= (damage * Config.MagicAdSetting_DarkElf.LUCIFERCHANCE);
			}

			if (FatigueProperty.getInstance().use_fatigue()
					&& attacker instanceof L1MonsterInstance) {
				Account.Account account = getAccount();
				if (account != null && getAI() == null && account.has_fatigue()) {
					damage += (damage * FatigueProperty.getInstance()
							.get_fatigue_effect_reduction());
				}
			}

			if (hasSkillEffect(L1SkillId.DARK_BLIND)) {
				removeSkillEffect(L1SkillId.DARK_BLIND);
			}

			if (attacker instanceof L1PcInstance) {
				damage += calculate_sprite_pvp_damage(attacker
						.getCurrentSpriteId());
				if (is_combat_field()) {
					L1PcInstance attacker_pc = (L1PcInstance) attacker;
					if (get_current_combat_id() == attacker_pc
							.get_current_combat_id()
							&& get_current_combat_team_id() == attacker_pc
							.get_current_combat_team_id()) {
						return;
					}
				}
			}
		}

		if ((getCurrentHp() > 0 && !isDead()) || getAI() != null || isGm()) {
			// 玩家攻擊時逃跑
			/*
			 * if (getAI() != null) { if (attacker instanceof L1PcInstance) {
			 * L1Character t = getAI().getCurrentTarget(); if (t == null ||
			 * t.getId() != attacker.getId())
			 * MJBotUtil.sendBotOnDamageMent(getAI(), attacker); }
			 *
			 * if (damage > 0 && getAI() instanceof MJBotMovableAI && attacker
			 * instanceof L1PcInstance) ((MJBotMovableAI)
			 * getAI()).addTarget(attacker); }
			 */

			if (isLock())
				damage = 0;

			if (attacker != null && attacker != this && !knownsObject(attacker)
					&& attacker.getMapId() == this.getMapId()) {
				attacker.onPerceive(this);
			}

			if (damage > 0) {
				if (attacker instanceof L1PcInstance) {
					L1PcInstance attacker_pc = (L1PcInstance) attacker;
					L1PinkName.onAction(this, attacker);
					if (is_combat_field()) {
						MJCombatObserver observer = MJCombatLoadManager
								.getInstance().get_current_observer(
										get_current_combat_id());
						if (observer != null) {
							observer.on_damage(attacker_pc, this, damage);
						} else {
							set_instance_status(MJEPcStatus.WORLD);
						}
					}
					MJCompanionInstance companion = attacker_pc.get_companion();
					if (companion != null
							&& companion.get_command_state().equals(
							eCommand.TM_Aggressive)) {
						if (getZoneType() == 0 && companion.getZoneType() == 0)
							companion.do_pink_name();
					}
				} else if (attacker instanceof MJCompanionInstance) {
					MJCompanionInstance companion = (MJCompanionInstance) attacker;
					if (getZoneType() == 0 && companion.getZoneType() == 0)
						companion.do_pink_name();
				}

				for (L1ItemInstance item : _equipSlot.getArmors()) {
					MJItemSkillModel model = MJItemSkillModelLoader
							.getInstance().getDef(item.getItemId());
					if (model != null)
						damage -= model.get(attacker, this, item, damage);
				}

				if (damage <= 0)
					damage = 10;
				if (getAI() != null
						&& getAI().getBotType() == MJBotType.REDKNIGHT) {
					damage += (getMaxHp() / Config.ServerAdSetting.RedKnightdieCount);
				}

				delInvis();
				if (hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
					removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
				} else if (hasSkillEffect(L1SkillId.PHANTASM)) {
					removeSkillEffect(L1SkillId.PHANTASM);
				}
			} else if (damage < 0) {
				if (attacker instanceof L1PcInstance) {
					L1PinkName.onHelp(this, attacker);
				}
			}
			if (getInventory().checkEquipped(145)
					|| getInventory().checkEquipped(149)) {
				damage *= 1.5;
			}

			// TODO 若玩家在安全區，則不允許攻擊，適用於特化地圖
			/*
			 * if (attacker instanceof L1PcInstance) { if (getZoneType() == 1)
			 * return; }
			 */

			if (hasSkillEffect(L1SkillId.PRESHER)) {
				double presher_dmg = 0;
				if (attacker == getPresherPc()) {
					presher_dmg = damage
							* Config.MagicAdSetting_Lancer.PRESHER_PCPCDMG;
				} else {
					presher_dmg = damage
							* Config.MagicAdSetting_Lancer.PRESHER_ETCPCDMG;
				}
				addPresherDamage((int) presher_dmg);
			}

			int newHp = getCurrentHp() - damage;
			if (newHp > getMaxHp()) {
				newHp = getMaxHp();
			}

			if (newHp <= 10) {
				if (is_halpas_armor() && ishalpaspaith() && checkHalpasTime()) { // 0->11로
					setCurrentHp(getMaxHp());
					setCurrentMp(getMaxMp());
					sendPackets(new S_HPUpdate(getCurrentHp(), getMaxHp()));
					sendPackets(new S_MPUpdate(getCurrentMp(), getMaxMp()));
					int enchant_gap = get_halpas_armor_enchant();
					int DRAGON_ARMOR_BLESSING_TIME = ((3600 * (22 - (enchant_gap * 2)) * 1000));
					int DRAGON_ARMOR_BLESSING_REDUC_TIME = 12000 / 1000;
					HalpasArmor.setHalpas_Time(new Timestamp(System.currentTimeMillis()+DRAGON_ARMOR_BLESSING_TIME));
					getInventory().updateItem(HalpasArmor, L1PcInventory.COL_SAVE_ALL);
					getInventory().saveItem(HalpasArmor, L1PcInventory.COL_SAVE_ALL);
//                    setSkillEffect(L1SkillId.DRAGON_ARMOR_BLESSING,
//                            DRAGON_ARMOR_BLESSING_TIME); // 22小時
// 冷卻時間
					L1SkillUse.off_icons(this, L1SkillId.DRAGON_ARMOR_EQUIP);
					L1SkillUse.on_icons(this, L1SkillId.DRAGON_ARMOR_BLESSING,DRAGON_ARMOR_BLESSING_TIME / 1000);
					set_halpas_faith_pvp_reduc(get_halpas_armor_enchant());
					getResistance().addcalcPcDefense(12 + get_halpas_armor_enchant());
					setSkillEffect(L1SkillId.DRAGON_ARMOR_BLESSING_REDUC,DRAGON_ARMOR_BLESSING_REDUC_TIME); // 秒
					L1SkillUse.on_icons(this,L1SkillId.DRAGON_ARMOR_BLESSING_REDUC,DRAGON_ARMOR_BLESSING_REDUC_TIME);
					send_party_effect(19074);
				} else if (isElf() && hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
					if (isPassive(MJPassiveID.SOUL_BARRIER_ARMOR.toInt())) {
						if (attacker instanceof L1PcInstance) {
							int level = getLevel();
							int rate = 8;

							if (level > 90)
								rate += ((level - 90) / 2) * 2;

							if (rate > 20)
								rate = 20;

							damage -= rate;
						} else {
							int level = getLevel();
							int rate = 4;

							if (level > 90)
								rate += ((level - 90) / 2);

							if (rate > 10)
								rate = 10;

							damage -= rate;
						}
					}
					int newMp = getCurrentMp() - damage;
					this.setCurrentHp(10);
					if (newMp <= 0) {
						death(attacker, true);
						this.setCurrentHp(0);
					}
					this.send_effect(14541, true);
					this.setCurrentMp(newMp);
				} else if (newHp <= 0) {
					if (isGm()) {
						setCurrentHp(getMaxHp());
					} else {
						if (attacker instanceof L1PcInstance) {
							death(attacker, true);
						}

						if (isDeathMatch()) {
							if (getMapId() == 5153) {
								try {
									save();
									beginGhost(getX(), getY(),
											(short) getMapId(), true);
									sendPackets(new S_ServerMessage(1271));
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
			}
			// TODO 不要用 else if ！特定技能時 HP 不低於6（嚴重）哄不好那種 XD
			if (newHp > 0) {
				this.setCurrentHp(newHp);
			}
		} else if (!isDead()) {
			death(attacker, true);
			System.out.println(String.format(
					"■■■■ 處理 HP 減少錯誤 \r\n%s -> %s : %d / HP:",
					attacker == null ? "null" : attacker.getName(), getName(),
					damage, getMaxHp()));
			sendPackets("如果無法使用技能或物品，請重新啟動遊戲。");
		}
	}

	public void death(L1Character lastAttacker, boolean deathPenalty) {
		L1Clan clan = getClan();
		if (clan != null) {
			clan.deleteClanRetrieveUser(getId());
		}

/*		if (get_is_client_auto()) {
			do_finish_client_auto(eReason.USER_DEAD);
		}*/

		synchronized (this) {
			if (isDead()) {
				return;
			}
			remove_companion();
			if (hasSkillEffect(L1SkillId.STATUS_TOMAHAWK)) {
				killSkillEffectTimer(L1SkillId.STATUS_TOMAHAWK);
			}
			setCurrentHp(0);
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
			if (lastAttacker != null && lastAttacker instanceof L1PcInstance) {
				L1PcInstance player = (L1PcInstance) lastAttacker;
				int lawful = getLawful();
				if (!(isPinkName() || lawful < 0)) {
					if (!getMap().isCombatZone(getLocation())) {
						long currenttime = System.currentTimeMillis();
						player.getAccount().set_Pk_Time(currenttime / 1000);
					}
				}
				if (getRedKnightClanId() != 0
						&& player.getRedKnightClanId() != 0) {
					player.addRedKnightKill(1);
					if (player.getRedKnightKill() >= 5) {
						player.setRedKnightKill(0);
						player.setRedKnightDamage(0);
						player.setRedKnightClanId(0);
						player.sendPackets(new S_SystemMessage(
								"您已經殺死紅色騎士團成員超過5次，被強制退出。"));
					}
				}
				try {
					if (MJRevengeService.service().use()) {
						MJRevengeProvider.provider().onNewKill(player,
								L1PcInstance.this);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (getAI() != null) {
					MJBotType type = getAI().getBotType();
					if (type == MJBotType.REDKNIGHT
							|| type == MJBotType.PROTECTOR) {
						broadcastPacket(new S_SkillSound(getId(), 2236));
						return;
					}
				}

				if (is_combat_field()) {
					MJCombatObserver observer = MJCombatLoadManager
							.getInstance().get_current_observer(
									get_current_combat_id());
					if (observer == null) {
						set_instance_status(MJEPcStatus.WORLD);
						getKDA().onKill(player, this);
					} else {
						try {
							observer.on_kill(player, this);
							L1World.getInstance()
									.broadcastPacketToAll(
											new ServerBasePacket[] {
													new S_ChatPacket(
															String.format(
																	"\\aG勝利[%s] \\aA=> \\aL敗北[%s]",
																	player.getName(),
																	getName()),
															Opcodes.S_MESSAGE),
													new S_SystemMessage(
															"[戰爭力量/戰場/戰鬥錦標賽]"), });
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					if (getKDA() != null)
						getKDA().onKill(player, this);
				}
			}
		}
		GeneralThreadPool.getInstance().execute(
				new Death(lastAttacker, deathPenalty));
	}

	private class Death implements Runnable {
		L1Character _lastAttacker;

		Death(L1Character cha, boolean deathPenalty) {
			_lastAttacker = cha;
			if (GMCommands.IS_PROTECTION) {
			} else {
			}
		}

		public void run() {
			// if (isTeleport()) {
			// GeneralThreadPool.getInstance().schedule(this, 300);
			// return;
			// }

			L1Character lastAttacker = _lastAttacker;
			_lastAttacker = null;
			setCurrentHp(0);
			setGresValid(false);

			// 重生註釋
			// stopHpMpRegeneration();

			int targetobjid = getId();
			getMap().setPassable(getLocation(), true);

			int tempchargfx = 0;
			if (hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
				removeSkillEffect(L1SkillId.SHAPE_CHANGE);
				tempchargfx = getCurrentSpriteId();
				setTempCharGfxAtDead(tempchargfx);
			} else {
				setTempCharGfxAtDead(getClassId());
			}

			if (hasSkillEffect(L1SkillId.PRESHER)) {
				setPresherPc(null);
				setPresherDamage(0);

				if (getPresherDeathRecall()) {
					setPresherDeathRecall(false);
				}
				removeSkillEffect(L1SkillId.PRESHER);
			}

			setCurrentSprite(getClassId());

			L1PcInstance.this.auraBuff(false); // 處理光環
			if (getParty() != null) { // 因有buff處理，所以在取消之前進行處理。
				getParty().onDeadMember(L1PcInstance.this);
			}

			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(L1PcInstance.this,
					L1SkillId.CANCELLATION, getId(), getX(), getY(), null, 0,
					L1SkillUse.TYPE_LOGIN);

			if (tempchargfx == 5727 || tempchargfx == 5730
					|| tempchargfx == 5733 || tempchargfx == 5736) {
				tempchargfx = 0;
			}
			if (tempchargfx != 0)
				sendShape(tempchargfx);
			sendShape(getTempCharGfxAtDead());

			sendPackets(new S_DoActionGFX(targetobjid, ActionCodes.ACTION_Die));
			broadcastPacket(new S_DoActionGFX(targetobjid,
					ActionCodes.ACTION_Die));

			isExpDrop = true;
			isItemDrop = true;

			if (lastAttacker != L1PcInstance.this) {
				L1PcInstance player = null;
				if (lastAttacker instanceof L1PcInstance) {
					player = (L1PcInstance) lastAttacker;
					/** 殺死時觸發效果 **/
					// player.sendPackets(new S_SkillSound(player.getId(),
					// 6354));
					// player.broadcastPacket(new S_SkillSound(player.getId(),
					// 6354));
					/** 殺死時觸發效果 **/
				} else if (lastAttacker instanceof MJCompanionInstance) {
					player = ((MJCompanionInstance) lastAttacker).get_master();
				} else if (lastAttacker instanceof L1PetInstance) {
					player = (L1PcInstance) ((L1PetInstance) lastAttacker)
							.getMaster();
				} else if (lastAttacker instanceof L1SummonInstance) {
					player = (L1PcInstance) ((L1SummonInstance) lastAttacker)
							.getMaster();
				}
				if (player != null) {
					if (getZoneType() == -1) {
						return new L1PcInstance[0];
					}
				}

				boolean sim_ret = simWarResult(lastAttacker);
				if (sim_ret == true) {
					return new L1PcInstance[0];
				}
			}

			if (!getMap().isEnabledDeathPenalty()) {
				sendPackets(3800);
				return new L1PcInstance[0];
			}

			L1PcInstance fightPc = null;
			if (lastAttacker instanceof L1PcInstance) {
				fightPc = (L1PcInstance) lastAttacker;
			}
			if (fightPc != null) {
				if (getFightId() == fightPc.getId()
						&& fightPc.getFightId() == getId()) {
					setFightId(0);
					sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0));
					fightPc.setFightId(0);
					fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL,
							0, 0));
					return new L1PcInstance[0];
				}
			}

			if (is_combat_field())
				return new L1PcInstance[0];

			if (GMCommands.IS_PROTECTION) {
				isExpDrop = false;
				isItemDrop = false;
				if (getKDA() != null)
					getKDA().onProtection(L1PcInstance.this);
				return new L1PcInstance[0];
			}

			// TODO 防止因特定條件造成經驗值下降
			boolean castle_ret1 = castleWarResult();
			if (lastAttacker instanceof L1PcInstance) {
				if (!castle_ret1
						&& getLevel() < Config.ServerAdSetting.NEWPLAYERPROTECTION
						&& (lastAttacker.getLevel() - getLevel()) >= 10) {
					isExpDrop = false;
					isItemDrop = false;
				}
			}
			if (castle_ret1 == true) {
				isExpDrop = false;
				isItemDrop = false;
				sendPackets(new S_ServerMessage(3798));
				// 無經驗值損失區域：經驗值未損失。
			}

			if (getZoneType() == 1 || getZoneType() == -1) {
				isExpDrop = false;
				isItemDrop = false;
				sendPackets(new S_ServerMessage(3798));
				// 無經驗值損失區域：經驗值未損失。
			}
			if (getMapId() == 10500 || getMapId() == 10501) {
				isExpDrop = false;
				isItemDrop = false;
				sendPackets(new S_ServerMessage(3798));
			}
			if (getLevel() < Config.ServerAdSetting.StartCharBoho) {
				isExpDrop = false;
				isItemDrop = false;
				sendPackets(3801);
			}
//			System.out.println(isPcBuff()+"+"+getAccount().getPcGaho());

			boolean gahouse = false;

			if (/*getAccount().get_Pk_Time() == 0 ||*/ (getAccount().get_Pk_Time()+(30*60) < System.currentTimeMillis() / 1000)) {
				if (getMapId() == 624 ||getMapId() == 430) { // 在PC房地圖中不掉落祝福
					if (isPcBuff()) {
						isExpDrop = false;
						isItemDrop = false;
						gahouse = true;
					}
				} else {
					if (!isPinkName()) {
						L1FreeShield shield = CharacterFreeShieldTable.getInstance().getFreeShield(L1PcInstance.this);
						if (isPcBuff() && shield.get_Pc_Gaho() > 0) { // PC房 buff 祝福
							isExpDrop = false;
							isItemDrop = false;
							gahouse = true;
							CharacterFreeShieldTable.getInstance().usePcGaho(L1PcInstance.this);
							sendPackets("PC房高級不朽的祝福已消失。剩餘數量：" + shield.get_Pc_Gaho() + "個");
							SC_FREE_BUFF_SHIELD_INFO_ACK.send(L1PcInstance.this);
						} else if (shield.get_Free_Gaho() > 0) { // 免費祝福
							CharacterFreeShieldTable.getInstance().useFreeGaho(L1PcInstance.this);
							shield.set_Pc_Gaho(shield.get_Pc_Gaho() - 1);
							shield.set_Pc_Gaho_use(shield.get_Pc_Gaho_use() + 1);
							isExpDrop = false;
							isItemDrop = false;
							gahouse = true;
							sendPackets("免費高級不朽的祝福已消失。剩餘數量：" + shield.get_Free_Gaho() + "個");
							SC_FREE_BUFF_SHIELD_INFO_ACK.send(L1PcInstance.this);
						} else if (shield.get_Event_Gaho() > 0) { // 活動祝福
							isExpDrop = false;
							isItemDrop = false;
							gahouse = true;
							CharacterFreeShieldTable.getInstance().useEventGaho(L1PcInstance.this);
							sendPackets("活動高級不朽的祝福已消失。剩餘數量：" + shield.get_Event_Gaho() + "個");
							SC_FREE_BUFF_SHIELD_INFO_ACK.send(L1PcInstance.this);
						}
					}
				}
			}

			if (!gahouse) {
				if (getInventory().checkItem(4100529) && getLevel() <= Config.ServerAdSetting.NEWPLAYERLEVELPROTECTION) { // TODO 修練者的高級不朽祝福
					if (lastAttacker instanceof L1PcInstance) {
						if (getInventory().checkItem(4100529)) {
							isExpDrop = false;
							isItemDrop = false;
							gahouse = true;
							specialgahodrop1(lastAttacker);
						}
					} else {
						int chance = _random.nextInt(100);
						if (chance < 100) {
							if (getInventory().checkItem(4100529)) {
								isExpDrop = false;
								isItemDrop = false;
								gahouse = true;
								specialgahodrop1(lastAttacker);
							}
						}
					}
				}
			}
			if (!gahouse) {
				if (getInventory().checkItem(4100121)){ // TODO 高級不朽的祝福 - 不掉落經驗值/物品
					if (lastAttacker instanceof L1PcInstance) {
						if (getInventory().checkItem(4100121)) {
							isExpDrop = false;
							isItemDrop = false;
							gahouse = true;
							specialgahodrop(lastAttacker);
						}
					} else {
						int chance = _random.nextInt(100);
						if (chance < 100) {
							if (getInventory().checkItem(4100121)) {
								isExpDrop = false;
								isItemDrop = false;
								gahouse = true;
								specialgahodrop(lastAttacker);
							}
						}
					}
				}
			}
			if (!gahouse) {
				if (getInventory().checkItem(4100122)){// TODO 不朽的祝福 - 只不掉落經驗值
					if (lastAttacker instanceof L1PcInstance) {
						if (getInventory().checkItem(4100122)) {
							isExpDrop = false;
							gahouse = true;
							gahodrop(lastAttacker);
						}
					} else {
						int chance = _random.nextInt(100);
						if (chance < 100) {
							if (getInventory().checkItem(4100122)) {
								isExpDrop = false;
								gahouse = true;
								gahodrop(lastAttacker);
							}
						}
					}
				}
			}

			if (lastAttacker instanceof L1PcInstance) {
				if ((getMapId() >= 1708 && getMapId() <= 1709)/** 古代祝福效果實現 **/
						&& getInventory().checkEquipped(900022)) {
					isExpDrop = false;
					isItemDrop = false;
					drop1(lastAttacker);
				}
			} else {
				int chance = _random.nextInt(100);
				if (chance < 100) {
					if ((getMapId() >= 1708 && getMapId() <= 1709)
							&& getInventory().checkEquipped(900022)) {
						isExpDrop = false;
						isItemDrop = false;
						drop1(lastAttacker);
					}
				}
			}
			if (lastAttacker instanceof L1PcInstance) {
				if (getInventory().checkEquipped(10000)) {
					isExpDrop = false;
					isItemDrop = false;
					drop2(lastAttacker);
				}
			} else {
				int chance = _random.nextInt(100);
				if (chance < 100) {
					if (getInventory().checkEquipped(10000)) { // 裝備的物品
						isExpDrop = false;
						isItemDrop = false;
						drop2(lastAttacker);
					}
				}
			}
			if (lastAttacker instanceof L1PcInstance) {
				if (getInventory().checkEquipped(10001)) { // 裝備的物品
					isExpDrop = false;
					isItemDrop = false;
					drop3(lastAttacker);
				}
			} else {
				int chance = _random.nextInt(100);
				if (chance < 100) {
					if (getInventory().checkEquipped(10001)) { // 裝備的物品
						isExpDrop = false;
						isItemDrop = false;
						drop3(lastAttacker);
					}
				}
			}

			if (lastAttacker instanceof L1CastleGuardInstance) {
				setLastPk(null);
			}

			if (isExpDrop) {
				deathPenalty();
				setGresValid(true);

				if (get_exp_res() == 0) {// 修復以相遇的祝福來恢復經驗值
					if (lastAttacker instanceof L1PcInstance
							&& getLevel() < Config.ServerAdSetting.NEWPLAYERPROTECTION
							&& (lastAttacker.getLevel() - getLevel()) >= 10) {
					} else {
						set_exp_res(1);
					}
				}

				/** 切割時攻擊守衛 **/
				if (lastAttacker instanceof L1GuardInstance) {
					if (get_PKcount() > 0) {
						set_PKcount(get_PKcount() - 1);
					}
					setLastPk(null);
				}
			}

			/** 死亡時物品與魔法掉落概率。算為系統化。 **/
			if (isItemDrop) {
				int lawful = getLawful();
				// 死亡時的基本掉落計數（大於等於此值時掉落）
				int dropCount = Config.CharSettings.LAWFUL_DROP_COUNT;

				Random random = new Random();
				int rnd = random.nextInt(1000) + 1;
				int lostRate = (int) (((32768D - getLawful()) * 3) / 100D) / 3;

				lostRate += 100; // 基本掉落概率增加10%（從1000開始計算）
				if (lawful <= 0) {
					lostRate *= 2;
				}

				if (rnd <= lostRate) {
					if (lawful >= 0
							&& lawful <= Config.CharSettings.DROP_LAWFUL_MIN) {
						dropCount = MJRnd
								.next(Config.CharSettings.DROP_LAWFUL_COUNT) + 1;
					} else if (lawful >= Config.CharSettings.DROP_LAWFUL1
							&& lawful <= Config.CharSettings.DROP_LAWFUL_MAX) {
						dropCount = MJRnd
								.next(Config.CharSettings.DROP_LAWFUL_COUNT1) + 1;
					} else if (lawful >= -Config.CharSettings.DROP_LAWFUL3
							&& lawful <= -Config.CharSettings.DROP_LAWFUL2) {
						dropCount = MJRnd
								.next(Config.CharSettings.DROP_LAWFUL_COUNT2) + 1;
					} else if (lawful >= -Config.CharSettings.DROP_LAWFUL5
							&& lawful <= -Config.CharSettings.DROP_LAWFUL4) {
						dropCount = MJRnd
								.next(Config.CharSettings.DROP_LAWFUL_COUNT3) + 1;
					} else { // 例外.
						dropCount = 0;
					}
				}

				if (dropCount > 0) {
					caoPenaltyResult(lastAttacker, dropCount);
				}
			}

			isExpDrop = false;
			isItemDrop = false;

			boolean castle_ret = castleWarResult();
			if (castle_ret == true) {
				return new L1PcInstance[0];
			}
			return new L1PcInstance[0];
		}
	}

	// private static final Integer[] _addDropItemIds = new Integer[] {
	// Config.CharSettings.PVPDROPNO1 };
	// private static final Integer[] _addDropItemIds =
	// Config.ServerAdSetting.PVPDROPNO1;

	private void caoPenaltyResult(L1Character lastAttacker, int count) {
		/** 城戰區域內不掉落 **/
		int castle_id = L1CastleLocation.getCastleIdByArea(this);
		if (castle_id != 0) {
			return;
		}
		/** 機器人系統 **/
		if (getAI() != null/* || getAccessLevel() == Config.GMCODE */) {
			return;
		}
		// 當是正義玩家時（100%優先順序），若持有1000個哈爾巴斯，則掉落一半（500個）
		// 當是邪惡玩家時（100%優先順序），若持有1000個哈爾巴斯，則全部掉落
		/*
		 * if (getMapId() >= 53 && getMapId() <= 56 || getMapId() >= 1708 &&
		 * getMapId() <= 1710 || getMapId() == 15403 || getMapId() == 15404 ||
		 * getMapId() >= 30 && getMapId() <= 36 || getMapId() >= 101 &&
		 * getMapId() <= 111 || getMapId() >= 121 && getMapId() <= 131) {
		 */

		if (count > 0) {
			for (Integer i : Config.CharSettings.PVPDROPNO1) {
				if (createAddDropItem(i))
					break;
			}
		}
		// }
		ArrayList<L1ItemInstance> dropItems = getInventory()
				.getPossibleDropItems();
		int size = 0;
		for (L1ItemInstance item : dropItems) {
			int rnd = MJRnd.next(dropItems.size());
			item = dropItems.get(rnd);
			L1NoDropItems nodrop_item = NoDropItemTable.getInstance()
					.getPresentItem(item.getItemId());
			if (nodrop_item != null)
				continue;
			if (item.isEquipped()) {
				getInventory().setEquipped(item, false);
			}

			if (item.getBless() > 3 || item.get_Carving() == 1) {
				if (getInventory().removeItem(item,	item.isStackable() ? item.getCount() : 1) > 0) {
					if (MJDeathPenaltyService.service().use()) {
						add_deathpenalty_item(item);
						MJDeathPenaltyProvider.provider().senditeminfo(this);
						MJDeathPenaltyItemDatabaseLoader.getInstance().update(getId(), item);
					}
					sendPackets(new S_ServerMessage(158, item.getLogName()));
					/** 파일로그저장 **/
					LoggerInstance.getInstance().addItemAction(ItemActionType.del, this, item, count);
					++size;
				}
			} else {
				if (lastAttacker instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) lastAttacker;
					if (DropDelayItemTable.getInstance().isItem(item.getItemId())) {
						L1DropDelayItem temp = DropDelayItemTable.getInstance().getItem(item.getItemId());
						if (temp != null) {
							if (temp.getItemId() == item.getItemId()) {
								item.startItemOwnerTimer(pc);
							}
						}
					}
				}
				item.setGiveItem(true);
				if (getInventory().tradeItem(item, item.isStackable() ? item.getCount() : 1, L1World.getInstance().getInventory(getX(), getY(),	getMapId())) != null) {
					sendPackets(new S_ServerMessage(638, item.getLogName())); // 蒸發
					/** 儲存文件日誌 **/
					LoggerInstance.getInstance().addItemAction(
							ItemActionType.DeathDrop, this, item, count);
					++size;
				}
			}
			if (size >= count)
				break;
		}
	}

	private boolean createAddDropItem(int itemId) {
		L1ItemInstance item = getInventory().findItemId(itemId);
		if (item == null
				|| !MJRnd.isWinning(10000, Config.CharSettings.PENALTY_POR)) // 掉落
			// 概率
			// 1000
			return false;

		int item_count = item.getCount();
		if (getLawful() >= 0)
			item_count = item_count <= 2 ? 1 : item_count / 2;
		item.setGiveItem(true);
		getInventory().tradeItem(item, item_count,
				L1World.getInstance().getInventory(getX(), getY(), getMapId()));
		sendPackets(new S_ServerMessage(638, item.getLogName()));
		LoggerInstance.getInstance().addItemAction(ItemActionType.DeathDrop,
				this, item, item_count);
		return true;
	}

	// 君主死亡時血盟成員自動使用變換卷軸
	public boolean castleWarResult() {
		/** 根據正式伺服器攻城戰系統變更，君主角色死亡時 */
		// L1Clan clan = getClan();
		// if (clan != null && isCrown() && getId() == clan.getLeaderId()) {
		// MJWar war = clan.getCurrentWar();
		// if (war != null) {
		// WAR_TYPE type = war.getWarType();
		// if (type.equals(WAR_TYPE.CASTLE) &&
		// war.getOffenseClan(clan.getClanId()) != null) {
		// L1Clan defense = war.getDefenseClan();
		// clan.outOfWarArea(defense.getCastleId());
		// war.notifyEndWar(defense, clan); // 終結戰爭
		// }
		// }
		// }

		int castleId = 0;
		boolean isNowWar = false;
		castleId = L1CastleLocation.getCastleIdByArea(this);
		if (castleId != 0) {
			isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
		}
		return isNowWar;
	}

	// 君主死亡時血盟成員自動使用變換卷軸 //
	public boolean simWarResult(L1Character lastAttacker) {
		if (getClanid() == 0) {
			return false;
		}
		if (Config.ServerAdSetting.SIMWARPENALTY) {
			return false;
		}
		L1PcInstance attacker = null;

		if (lastAttacker instanceof L1PcInstance) {
			attacker = (L1PcInstance) lastAttacker;
		} else if (lastAttacker instanceof MJCompanionInstance) {
			attacker = ((MJCompanionInstance) lastAttacker).get_master();
		} else if (lastAttacker instanceof L1PetInstance) {
			attacker = (L1PcInstance) ((L1PetInstance) lastAttacker)
					.getMaster();
		} else if (lastAttacker instanceof L1SummonInstance) {
			attacker = (L1PcInstance) ((L1SummonInstance) lastAttacker)
					.getMaster();
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

		WAR_TYPE type = war.getWarType();
		if (!type.equals(WAR_TYPE.NORMAL))
			return false;

		war.notifyWinner(enemy, clan);
		war.notifyEndWar(clan, enemy);
		war.dispose();
		return true;
	}

	public void resExp() {
		int oldLevel = getLevel();
		long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
		long exp = 0;
		double ratio;

		if (oldLevel < 45)
			ratio = 0.05;
		else if (oldLevel >= 49)
			ratio = 0.025;
		else
			ratio = 0.05 - (oldLevel - 44) * 0.005;

		exp = (long) (needExp * ratio);

		if (exp == 0)
			return;

		add_exp(exp);
	}

	public void resExpToTemple() {
		int oldLevel = getLevel();
		long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
		long exp = 0;
		double ratio;
		if (oldLevel < 45) {
			ratio = 0.05;
		}else if (oldLevel >= 45 && oldLevel < 49) {
			ratio = 0.05 - (oldLevel - 44) * 0.005;
		}else if (oldLevel >= 49 && oldLevel < 52) {
			ratio = 0.025;
		}else if (oldLevel == 52) {
			ratio = 0.026;
		}else if (oldLevel > 52 && oldLevel < 74) {
			ratio = 0.026 + (oldLevel - 52) * 0.001;
		}else if (oldLevel >= 74 && oldLevel < 79) {
			ratio = 0.025 - (oldLevel - 73) * 0.0005;

		}else {
			/* if (oldLevel >= 79) */
			ratio = 0.03; // 從79級開始恢復4.9% 0.049
		}

		// ratio = 0.025; // 從79級開始恢復4.9%

		exp = (long) (needExp * ratio);
		if (exp == 0)
			return;
		int level = ExpTable.getLevelByExp(_exp + exp);
		if (level >= 100) {
			S_SystemMessage sm = new S_SystemMessage(
					"由於等級限制，無法再獲得經驗值。");
			sendPackets(sm, true);
			return;
		}
		add_exp(exp);
	}

	// TODO 當玩家角色死亡時的經驗懲罰
	public void deathPenalty() {
		int oldLevel = getLevel();
		long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
		long exp = 0;
		if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN
				&& oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX)
			exp = (long) (needExp * Config.CharSettings.EXP_PENALTY);
		else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN1
				&& oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX1)
			exp = (long) (needExp * Config.CharSettings.EXP_PENALTY1);
		else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN2
				&& oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX2)
			exp = (long) (needExp * Config.CharSettings.EXP_PENALTY2);
		else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN3
				&& oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX3)
			exp = (long) (needExp * Config.CharSettings.EXP_PENALTY3);
		else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN4
				&& oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX4)
			exp = (long) (needExp * Config.CharSettings.EXP_PENALTY4);
		else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN5
				&& oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX5)
			exp = (long) (needExp * Config.CharSettings.EXP_PENALTY5);
		else if (oldLevel >= Config.CharSettings.DEATH_PENALTY_LEVEL_MIN6
				&& oldLevel <= Config.CharSettings.DEATH_PENALTY_LEVEL_MAX6)
			exp = (long) (needExp * Config.CharSettings.EXP_PENALTY6);

		// System.out.println(exp);
		// System.out.println(needExp);

		if (exp == 0)
			return;

//		add_deathpenalty_exp(exp);
//		MJDeathPenaltyProvider.provider().sendexpinfo(this);

		add_exp(-exp);
	}

	public L1BookMark getBookMark(String name) {
		L1BookMark element = null;
		int size = _bookmarks.size();
		for (int i = 0; i < size; i++) {
			element = _bookmarks.get(i);
			if (element == null)
				continue;
			if (element.getName().equalsIgnoreCase(name)) {
				return element;
			}
		}
		return null;
	}

	public L1BookMark getBookMark(int id) {
		L1BookMark element = null;
		int size = _bookmarks.size();
		for (int i = 0; i < size; i++) {
			element = _bookmarks.get(i);
			if (element == null)
				continue;
			if (element.getId() == id) {
				return element;
			}
		}
		return null;
	}

	public int getBookMarkSize() {
		return _bookmarks.size();
	}

	public void addBookMark(L1BookMark book) {
		_bookmarks.add(book);
	}

	public void removeBookMark(L1BookMark book) {
		_bookmarks.remove(book);
	}

	public L1ItemInstance getWeapon() {
		return _weapon;
	}

	public void setWeapon(L1ItemInstance weapon) {
		_weapon = weapon;
	}

	public L1ItemInstance getWeaponSwap() {
		return getEquipSlot().getWeaponSwap();
	}

	public L1ItemInstance getArmor() {
		return _armor;
	}

	public void setArmor(L1ItemInstance armor) {
		_armor = armor;
	}

	// TODO 儲存穿戴盔甲的物品信息
	public L1ItemInstance getAmory() {
		return _armory;
	}

	public void setAmory(L1ItemInstance armor) {
		_armory = armor;
	}

	public L1ItemInstance getSecondWeapon() {
		return _secondweapon;
	}

	public void setSecondWeapon(L1ItemInstance weapon) {
		_secondweapon = weapon;
	}

	public L1Quest getQuest() {
		return _quest;
	}

	public String getClassName() {
		if (isCrown()) {
			return "王族";
		} else if (isKnight()) {
			return "騎士";
		} else if (isElf()) {
			return "妖精";
		} else if (isWizard()) {
			return "法師";
		} else if (isDarkelf()) {
			return "黑暗妖精";
		} else if (isDragonknight()) {
			return "龍騎士";
		} else if (isBlackwizard()) {
			return "幻術師";
		} else if (isWarrior()) { // 戰士
			return "戰士";
		} else if (isFencer()) {
			return "劍士";
		} else if (isLancer()) {
			return "黃金槍騎";
		}

		return "職業名稱";
	}



	public int getClassNumber() {
		if (isCrown()) {
			return 0;
		} else if (isKnight()) {
			return 1;
		} else if (isElf()) {
			return 2;
		} else if (isWizard()) {
			return 3;
		} else if (isDarkelf()) {
			return 4;
		} else if (isDragonknight()) {
			return 5;
		} else if (isBlackwizard()) {
			return 6;
		} else if (isWarrior()) { // 戰士
			return 7;
		} else if (isFencer()) {
			return 8;
		} else {
			return 9;
		}
	}

	public boolean isWarrior() {
		return false;
	}

	public boolean isCrown() {
		return (getClassId() == CLASSID_PRINCE || getClassId() == CLASSID_PRINCESS);
	}

	public boolean isKnight() {
		return (getClassId() == CLASSID_KNIGHT_MALE || getClassId() == CLASSID_KNIGHT_FEMALE);
	}

	public boolean isElf() {
		return (getClassId() == CLASSID_ELF_MALE || getClassId() == CLASSID_ELF_FEMALE);
	}

	public boolean isWizard() {
		return (getClassId() == CLASSID_WIZARD_MALE || getClassId() == CLASSID_WIZARD_FEMALE);
	}

	public boolean isDarkelf() {
		return (getClassId() == CLASSID_DARK_ELF_MALE || getClassId() == CLASSID_DARK_ELF_FEMALE);
	}

	public boolean isDragonknight() {
		return (getClassId() == CLASSID_DRAGONKNIGHT_MALE || getClassId() == CLASSID_DRAGONKNIGHT_FEMALE);
	}

	public boolean isBlackwizard() {
		return (getClassId() == CLASSID_BLACKWIZARD_MALE || getClassId() == CLASSID_BLACKWIZARD_FEMALE);
	}

	public boolean isWARRIOR() {
		return (getClassId() == CLASSID_WARRIOR_MALE || getClassId() == CLASSID_WARRIOR_FEMALE);
	}

	public boolean isFencer() {
		return (getClassId() == CLASSID_FENCER_MALE || getClassId() == CLASSID_FENCER_FEMALE);
	}

	public boolean isLancer() {
		return (getClassId() == CLASSID_LANCER_MALE || getClassId() == CLASSID_LANCER_FEMALE);
	}

	public String getAccountName() {
		return _accountName;
	}

	public void setAccountName(String s) {
		_accountName = s;
	}

	public short getBaseMaxHp() {
		return _baseMaxHp;
	}

	public void addBaseMaxHp(short i) {
		i += _baseMaxHp;
		if (i >= 32767) {
			i = 32767;
		} else if (i < 1) {
			i = 1;
		}
		addMaxHp(i - _baseMaxHp);
		_baseMaxHp = i;
	}

	public int getBaseMaxMp() {
		return _baseMaxMp;
	}

	public void addBaseMaxMp(int i) {
		i += _baseMaxMp;
		if (i >= 32767) {
			i = 32767;
		} else if (i < 0) {
			i = 0;
		}
		addMaxMp(i - _baseMaxMp);
		_baseMaxMp = i;
	}

	public int getOriginalMagicHit() {
		return _originalMagicHit;
	}

	public int getBaseAc() {
		return _baseAc;
	}

	public int getBaseDmgup() {
		return _baseDmgup;
	}

	public int getBaseBowDmgup() {
		return _baseBowDmgup;
	}

	public int getBaseHitup() {
		return _baseHitup;
	}

	public int getBaseBowHitup() {
		return _baseBowHitup;
	}

	public int getBaseDecreaseCoolTime() {
		return _baseDecreaseCoolTime;
	}

	public int getBaseDecreaseCCDuration() {
		return _baseDecreaseCCDuration;
	}

	public void setBaseDecreaseCoolTime(int i) {
		_baseDecreaseCoolTime = i;
	}

	public void setBaseDecreaseCCDuration(int i) {
		_baseDecreaseCCDuration = i;
	}

	public void addBaseDecreaseCCDuration(int i) {
		_baseDecreaseCCDuration += i;
	}

	public void addBaseDecreaseCoolTime(int i) {
		_baseDecreaseCoolTime += i;
	}

	public void setBaseMagicHitUp(int i) {
		_baseMagicHitup = i;
	}

	public void addBaseMagicHitUp(int i) {
		_baseMagicHitup += i;
	}

	public int getBaseMagicHitUp() {
		return _baseMagicHitup;
	}

	public int getTotalMagicHitup() {
		return getBaseMagicHitUp() + getOriginalMagicHit();
	}

	public void setBaseMagicCritical(int i) {
		_baseMagicCritical = i;
	}

	public int getBaseMagicCritical() {
		return _baseMagicCritical;
	}

	public void addBaseMagicCritical(int i) {
		_baseMagicCritical += i;
	}

	public void setBaseMagicDmg(int i) {
		_baseMagicDmg = i;
	}

	public int getBaseMagicDmg() {
		return _baseMagicDmg;
	}

	public void setBaseMagicDecreaseMp(int i) {
		_baseMagicDecreaseMp = i;
	}

	public int getBaseMagicDecreaseMp() {
		return _baseMagicDecreaseMp;
	}

	public int getAdvenHp() {
		return _advenHp;
	}

	public void setAdvenHp(int i) {
		_advenHp = i;
	}

	public int getAdvenMp() {
		return _advenMp;
	}

	public void setAdvenMp(int i) {
		_advenMp = i;
	}

	public int getMagicBuffHp() {
		return _magicBuffHp;
	}

	public void setMagicBuffHp(int i) {
		_magicBuffHp = i;
	}

	public int getHighLevel() {
		return _highLevel;
	}

	public void setHighLevel(int i) {
		_highLevel = i;
	}

	// TODO 涉及所有屬性
	public static final int STAT_INCREASE_LEVEL = 50;
	public static final int STAT_BASE_AMOUNT = 75;

	public int totalBonusStats() {
		return Math.min(Math.max(getHighLevel() - STAT_INCREASE_LEVEL, 0), 127);
	}

	public int remainBonusStats() {
		if (getHighLevel() <= STAT_INCREASE_LEVEL) {
			return 0;
		}

		int statAmounts = getAbility().getAmount();
		return (totalBonusStats() + getElixirStats() + STAT_BASE_AMOUNT)
				- statAmounts;
	}

	public void sendBonusStats() {
		if (getLevel() > 50) {
			if (remainBonusStats() > 0)
				sendPackets(new S_Message_YN(479,String.valueOf(remainBonusStats())));
		}
	}

	public void StartCharBoho() {
		if (getLevel() > 1 && getLevel() < Config.ServerAdSetting.StartCharBoho) {
			sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.Start_BUFF,
					3804, true));
		} else {
			sendPackets(S_InventoryIcon.icoEnd(L1SkillId.Start_BUFF));
		}
	}

	public int getElixirStats() {
		return _elixirStats;
	}

	public void setElixirStats(int i) {
		_elixirStats = i;
	}

	public int getElfAttr() {
		return _elfAttr;
	}

	public void setElfAttr(int i) {
		_elfAttr = i;
	}

	public long get_exp_res() {
		return _expRes;
	}

	public void set_exp_res(long i) {
		_expRes = i;
	}

	public int getPartnerId() {
		return _partnerId;
	}

	public void setPartnerId(int i) {
		_partnerId = i;
	}

	public int getOnlineStatus() {
		return _onlineStatus;
	}

	public void setOnlineStatus(int i) {
		_onlineStatus = i;
	}

	public int getHomeTownId() {
		return _homeTownId;
	}

	public void setHomeTownId(int i) {
		_homeTownId = i;
	}

	public int getContribution() {
		return _contribution;
	}

	public void setContribution(int i) {
		_contribution = i;
	}

	public int getHellTime() {
		return _hellTime;
	}

	public void setHellTime(int i) {
		_hellTime = i;
	}

	private boolean _morning = false;

	public void setMorning(boolean flag) {
		this._morning = flag;
	}

	public boolean getMorning() {
		return _morning;
	}

	public boolean isBanned() {
		return _banned;
	}

	public void setBanned(boolean flag) {
		_banned = flag;
	}

	public int get_food() {
		return _food;
	}

	public void set_food(int i) {
		_food = i;

		if (_food < 225) {
			// sendPackets("無法自然恢復:飽足狀態:(" + _food + "% : 200%) です。");
			// sendPackets(S_InventoryIcon.icoEnd(L1SkillId.FOOD_BUFF));
			// sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.NO_FOOD_BUFF,
			// 3116,
			// true));
			// } else {
			// sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.FOOD_BUFF,
			// 1082, true));
			// sendPackets(S_InventoryIcon.icoEnd(L1SkillId.NO_FOOD_BUFF));
		}

	}

	/** 生存的呼喊 **/
	public void add_food(int i) {
		_food += i;
		if (_food > 225) {
			_food = 225;
			if (getCryOfSurvivalTime() == 0) {
				SurvivalGauge = System.currentTimeMillis() / 1000;
			}
		} else if (_food < 1) {
			_food = 1;
		}
	}

	public long getCryOfSurvivalTime() {
		return SurvivalGauge;
	}

	// 初始化.
	public void setCryOfSurvivalTime() {
		if (get_food() >= 225) {
			SurvivalGauge = System.currentTimeMillis() / 1000;
		}
	}

	private long SurvivalGauge; // 生存的呼喊計量表

	public long getSurvivalGauge() {
		return SurvivalGauge;
	}

	public void setSurvivalGauge(long SurvivalGauge) {
		this.SurvivalGauge = SurvivalGauge;
	}

	/** 可以穿戴並附魔的物品 **/
	public L1EquipmentSlot getEquipSlot() {
		return _equipSlot;
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
			if (isGhost()) {
				return;
			}
			if (noPlayerCK || noPlayerRobot || noPlayerck2)
				return;

			CharacterTable.getInstance().storeCharacter(this);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/** 修改與機器人相關的後端 **/
	public void saveInventory() {
		if (noPlayerCK || noPlayerRobot || noPlayerck2)
			return;
		for (L1ItemInstance item : getInventory().getItems()) {
			if (item != null)
				getInventory()
						.saveItem(
								item,
								item.getRecordingColumns() != 0 ? L1PcInventory.COL_SAVE_ALL
										: 0);
		}
	}

	public void setRegenState(int state) {
		if (_mpRegen != null) {
			_mpRegen.setState(state);
		}
		if (_hpRegen != null) {
			_hpRegen.setState(state);
		}
	}

	private int _maxweight = 0;

	public int getMaxWeight() {
		try {
			_maxweight = CalcStat.getMaxWeight(getAbility().getTotalStr(),
					getAbility().getTotalCon());
			_maxweight += getWeightReduction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _maxweight;
	}

	public boolean isFastMovable() {
		return (hasSkillEffect(L1SkillId.HOLY_WALK) || hasSkillEffect(L1SkillId.MOVING_ACCELERATION));
	}

	public boolean isBlood_lust() {
		return hasSkillEffect(L1SkillId.BLOOD_LUST);
	}

	@Override
	public int getBraveSpeed() {
		// 幻術容器速度
		if (hasSkillEffect(L1SkillId.STATUS_FRUIT))
			return isPassive(MJPassiveID.DARK_HORSE.toInt()) ? 3 : 4;

		if (this.hasSkillEffect(L1SkillId.MOVING_ACCELERATION)) {
			if (this.isPassive(MJPassiveID.MOVING_ACCELERATION_PASS.toInt())) {
				return 3;
			}
			return 4;
		}

		if (this.hasSkillEffect(L1SkillId.HOLY_WALK)) {
			if (this.isPassive(MJPassiveID.HOLY_WALK_EVOLUTION.toInt())) {
				return 3;
			}
			return 4;
		}

		return super.getBraveSpeed();
	}

	public boolean isBrave() {
		return (hasSkillEffect(L1SkillId.STATUS_BRAVE));
	}

	public boolean isElfBraveMagicShort() {
		return hasSkillEffect(L1SkillId.DANCING_BLADES)
				|| hasSkillEffect(L1SkillId.SAND_STORM);
	}

	public boolean isElfBraveMagicLong() {
		return hasSkillEffect(L1SkillId.HURRICANE)
				|| hasSkillEffect(L1SkillId.FOCUS_WAVE);
	}

	public boolean isDragonPearl() {
		return (hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL) || getPearl() == 1);
	}

	public boolean isElfBrave() {
		return hasSkillEffect(L1SkillId.STATUS_ELFBRAVE);
	}

	public boolean isFruit() {
		return hasSkillEffect(L1SkillId.STATUS_FRUIT);
	}

	public boolean _fourgear;

	public boolean isFourgear() {
		return _fourgear;
	}

	public void setFourgear(boolean onoff) {
		_fourgear = onoff;
	}

	private int _pearl;

	public int getPearl() {
		return _pearl;
	}

	public void setPearl(int i) {
		_pearl = i;
	}

	public boolean isInvisDelay() {
		return (invisDelayCounter > 0);
	}

	public void addInvisDelayCounter(int counter) {
		synchronized (_invisTimerMonitor) {
			invisDelayCounter += counter;
		}
	}

	public void beginInvisTimer() {
		final long DELAY_INVIS = 3000L;
		addInvisDelayCounter(1);
		GeneralThreadPool.getInstance().schedule(new L1PcInvisDelay(getId()),DELAY_INVIS);
	}

	private Object m_add_exp_sync = new Object();

	public void add_exp(long exp) {
		if (Config.Login.StandbyServer)
			return;

		add_exp_for_ready(exp);
	}

	public void add_exp_for_ready(long exp) {
		synchronized (m_add_exp_sync) {
			long current_exp = get_exp() + exp;
			long max_exp = ExpTable.get_max_exp();
			if (current_exp >= max_exp)
				current_exp = max_exp;
			set_exp(current_exp);
		}
	}

	public synchronized void addContribution(int contribution) {
		_contribution += contribution;
	}

	private static final long INTERVAL_EXP_MONITOR = 500;

	public void beginExpMonitor() {
		if (!attribute().has(expMonitorKey)
				|| attribute().get(expMonitorKey).get() == null) {
			L1PcExpMonitor monitor = new L1PcExpMonitor(this, getId());
			attribute().getNotExistsNew(expMonitorKey).set(monitor);
			_expMonitorFuture = GeneralThreadPool.getInstance()
					.scheduleAtFixedRate(monitor, 0L, INTERVAL_EXP_MONITOR);
		}
	}

	/** 當等級達到指定值時，輸出條件 **/
	private void levelUp(int gap) {
		Random random = new Random();
		boolean isTeleport = false;
		resetLevel();
		int oldHighLevel = getHighLevel();
		setHighLevel(getLevel());

		/** 2017-11-06 阿因哈薩德祝福減少機率更新 **/
		SC_REST_EXP_INFO_NOTI.send(this);
		MJRankUserLoader.getInstance().onUser(this);

		/** 特定等級以上玩家自動退出新手血盟 **/
		String BloodName = getClanname();
		if (getLevel() >= Config.ServerAdSetting.NEWPLAYERLEVELPURGE
				&& BloodName.equalsIgnoreCase(Config.ServerAdSetting.NEWCLANNAME)
				|| BloodName.equalsIgnoreCase("新手")) {
			try {
				// L1Clan clan = L1World.getInstance().findClan(Config.新手血盟名稱);
				L1Clan clan = L1World.getInstance().findClan("新手保護");
				L1PcInstance clanMember[] = clan.getOnlineClanMember();
				String player_name = getName();
				String clan_name = getClanname();
				for (int i = 0; i < clanMember.length; i++) {
					clanMember[i].sendPackets(new S_ServerMessage(
							ServerMessage.LEAVE_CLAN, player_name, clan_name));
				}
				ClearPlayerClanData(clan);
				clan.removeClanMember(player_name);
				this.start_teleport(this.getX(), this.getY(), this.getMapId(),
						this.getHeading(), 18339, false, false);
				isTeleport = true;
				save();
				saveInventory();
			} catch (Exception e) {
			}
		}
		if (getLevel() > 90
				&& getTitle().contains(Config.Message.GameServerName)) {
			setTitle("");
			sendPackets(new S_CharTitle(getId(), ""));
			broadcastPacket(new S_CharTitle(getId(), ""));
		}

		for (int i = 0; i < gap; i++) {
			int minmp = CalcStat.MinincreaseMp(getType(), getAbility().getWis());
			int maxmp = CalcStat.MaxincreaseMp(getType(), getAbility().getWis());
			short randomHp = (short) (CalcStat.PureHp(getType(), getAbility().getCon()) + random.nextInt(2));
			int randomMp = (int) ((Math.random() * (maxmp - minmp)) + minmp);
			if (minmp == 0){
				randomMp = random.nextInt(maxmp + 1);
			}
			addBaseMaxHp(randomHp);
			addBaseMaxMp(randomMp);
		}

		this.setCurrentHp(getMaxHp());
		this.setCurrentMp(getMaxMp());
		resetBaseHitup();
		resetBaseDmgup();
		resetBaseAc();
		resetBaseMr();
		if (getLevel() > oldHighLevel && getReturnStat() == 0) {
			LevelupBookmark.getInstance().on_level(this);
			MJObjectEventProvider.provider().pcEventFactory()
					.fireLevelChanged(this);
		}

		try {
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}

		L1Quest quest = getQuest();
		// 升級（將任務獎勵外部化）從1級到目前級別進行迭代搜索
		int lv = getLevel();
		for (int _lv = 1; _lv <= lv; _lv++) {
			CharactersGiftItemTable.Item _levelItem = null;
			CharactersGiftItemTable.Item[] _levelItems = CharactersGiftItemTable
					.getInstance().getItems(_lv);
			if (_levelItems != null && _levelItems.length > 0) {
				int level_quest_step = quest.get_step(_lv);
				if (level_quest_step != L1Quest.QUEST_END) {
					for (int i = 0; i < _levelItems.length; i++) {
						_levelItem = _levelItems[i];
						if (_levelItem == null)
							continue;
						if (_levelItem.getType() != getType())
							continue;
						createNewItem(this, _levelItem.getItemId(),
								_levelItem.getCount(), _levelItem.getEnchant(),
								_levelItem.getAttrLevel(),
								_levelItem.getBless());
					}
					sendPackets(new S_ChatPacket("Level(" + _lv + ")任務已完成。"));
					getQuest().set_end(_lv);
				}
			}
		}
		// TODO 開放Snapper
		int lv59_step = quest.get_step(L1Quest.QUEST_EARRING_SLOT60);
		if (getLevel() == 60 && lv59_step != L1Quest.QUEST_END) {
			this.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					"Lv.60 現在可以開放耳環了。"));
			this.sendPackets(new S_NewCreateItem(S_NewCreateItem.NEW_PACKET_10, 0));
		}
		int lv76_step = quest.get_step(L1Quest.QUEST_RING_LEFT_SLOT60);
		if (getLevel() == 60 && lv76_step != L1Quest.QUEST_END) {
			this.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					"Lv.60 現在可以開放戒指了。"));
		}
		int lv81_step = quest.get_step(L1Quest.QUEST_RING_RIGHT_SLOT60);
		if (getLevel() == 60 && lv81_step != L1Quest.QUEST_END) {
			this.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					"Lv.60 現在可以開放戒指了。"));
		}

		sendBonusStats();
		StartCharBoho();

		if (getLevel() > 1 && getLevel() <= Config.ServerAdSetting.LineageBuff) {
			sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.HUNTER_BLESS3,
					4126, true));

		} else {
			sendPackets(S_InventoryIcon.icoEnd(L1SkillId.HUNTER_BLESS3));
		}

		// TODO 當達到特定等級時，將玩家從該地圖驅逐
		if (getLevel() >= Config.ServerAdSetting.NewCha1) { // 說話之島地牢 天堂之谷
			if ((getMapId() >= 1 && getMapId() <= 2) || getMapId() >= 10
					&& getMapId() <= 12 || getMapId() == 785
					|| getMapId() == 1911 || getMapId() == 1912) {
				int[] loc = L1TownLocation
						.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
				this.start_teleport(loc[0], loc[1], loc[2], this.getHeading(),
						18339, true, false);
				isTeleport = true;
			}
		}
		if (getLevel() >= 75) {
			if (getMapId() >= 25 && getMapId() <= 28) {
				int[] loc = L1TownLocation
						.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
				this.start_teleport(loc[0], loc[1], loc[2], this.getHeading(),
						18339, true, false);
				isTeleport = true;
			} else if (getMapId() == 778 || getMapId() == 779) {
				this.start_teleport(32608, 33178, 4, 5, 18339, true, false);
				isTeleport = true;
			} else if (getMapId() == 2010) {
				int[] loc = L1TownLocation
						.getGetBackLoc(L1TownLocation.TOWNID_SILVER_KNIGHT_TOWN);
				this.start_teleport(loc[0], loc[1], loc[2], this.getHeading(),
						18339, true, false);
				isTeleport = true;
			}
		}

		/*
		 * if (getLevel() == 55) { getAccount().setBlessOfAin(1000000);
		 * SC_REST_EXP_INFO_NOTI.send(this); }
		 *
		 * if (getZoneType() == 1) { startEinhasadTimer(); } }
		 */

		if (getLevel() >= Config.CharSettings.MaxLevel /* && !pc.isGm() */) {
			sendPackets(Config.Message.MAX_LEVEL_MESSAGE);
			sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					Config.Message.MAX_LEVEL_MESSAGE));
		}

		// TODO 根據角色等級發放獎勵
		int lvlBonus = getCharLevelBonus();
		int lvlBonusOrgn = lvlBonus;
		if (getLevel() >= Config.ServerAdSetting.Bonus_Lvl1
				&& (lvlBonus & BONUS_LEVEL_80) == 0) {
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1966), 500L);
			getInventory().storeItem(Config.ServerAdSetting.Bonus_Item1,
					Config.ServerAdSetting.Bonus_Item_Count1, true);
			sendPackets(Config.Message.Bonus_Message1);
			sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					Config.Message.Bonus_Message1));
			// 80 bonus
			lvlBonus |= BONUS_LEVEL_80;
		}
		if (getLevel() >= Config.ServerAdSetting.Bonus_Lvl2
				&& (lvlBonus & BONUS_LEVEL_82) == 0) {
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1967), 500L);
			getInventory().storeItem(Config.ServerAdSetting.Bonus_Item2,
					Config.ServerAdSetting.Bonus_Item_Count2, true);
			sendPackets(Config.Message.Bonus_Message2);
			sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					Config.Message.Bonus_Message2));
			// 82 bonus
			lvlBonus |= BONUS_LEVEL_82;
		}
		if (getLevel() >= Config.ServerAdSetting.Bonus_Lvl3
				&& (lvlBonus & BONUS_LEVEL_84) == 0) {
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1968), 500L);
			getInventory().storeItem(Config.ServerAdSetting.Bonus_Item3,
					Config.ServerAdSetting.Bonus_Item_Count3, true);
			sendPackets(Config.Message.Bonus_Message3);
			sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					Config.Message.Bonus_Message3));
			// 84 bonus
			lvlBonus |= BONUS_LEVEL_84;
		}
		if (getLevel() >= Config.ServerAdSetting.Bonus_Lvl4
				&& (lvlBonus & BONUS_LEVEL_86) == 0) {
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1969), 500L);
			getInventory().storeItem(Config.ServerAdSetting.Bonus_Item4,
					Config.ServerAdSetting.Bonus_Item_Count4, true);
			sendPackets(Config.Message.Bonus_Message4);
			sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					Config.Message.Bonus_Message4));
			// 86 bonus
			lvlBonus |= BONUS_LEVEL_86;
		}
		if (getLevel() >= Config.ServerAdSetting.Bonus_Lvl5
				&& (lvlBonus & BONUS_LEVEL_88) == 0) {
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1970), 500L);
			getInventory().storeItem(Config.ServerAdSetting.Bonus_Item5,
					Config.ServerAdSetting.Bonus_Item_Count5, true);
			sendPackets(Config.Message.Bonus_Message5);
			sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					Config.Message.Bonus_Message5));
			// 88 bonus
			lvlBonus |= BONUS_LEVEL_88;
		}
		if (getLevel() == 90 && (lvlBonus & BONUS_LEVEL_90) == 0) {
			sendPackets("達到等級90，傳送到村莊。（象牙塔時間重置）");
			isTeleport = true;
			start_teleport(33437, 32813, 4, getHeading(), 18339, false, false);
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					// TODO 達到90級後重置該副本的時間
					DungeonTimeProgress<?> progress = _dtInfo.remove_dungeon_progress(2); // 1
					if (progress != null) {
						DungeonTimeProgressLoader.delete(L1PcInstance.this, 2, true); // 1
					}
					progress = _dtInfo.remove_dungeon_progress(14); // 7
					if (progress != null) {
						DungeonTimeProgressLoader.delete(L1PcInstance.this, 14, true); // 7
					}
					// TODO 可以像下面的格式添加
					// progress = _dtInfo.remove_dungeon_progress(14);
					// if (progress != null) {
					// DungeonTimeProgressLoader.delete(L1PcInstance.this, 14, true);
					// }
					return new L1PcInstance[0];
				}
			}, 500L);
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1996), 500L);
			// getInventory().storeItem(40308, 1, true);
			// sendPackets("[90] 等級升級獎勵已發放。");
			// sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
			// "[90] 等級升級獎勵已發放。"));
			lvlBonus |= BONUS_LEVEL_90;
		}

		if (lvlBonus != lvlBonusOrgn) {
			setCharLevelBonus(lvlBonus);
			MJLevelBonus.storeCharacterBonus(this);
		}

		// TODO 達到特定級別後，為了變身的攻速自動施放解除魔法
		if (getMapId() != 5490 && !isFishing()
				&& getLevel() >= Config.ServerAdSetting.PcReload && !isTeleport) {
			start_teleport(getX(), getY(), getMapId(), getHeading(), 18339,
					false, false);
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					sendPackets(new S_OwnCharStatus(L1PcInstance.this));
					return new L1PcInstance[0];
				}
			}, 500L);
		} else {
			sendPackets(new S_OwnCharStatus(this));
		}

		/*
		 * if (get_is_client_auto()) { MJAutoMapInfo mInfo =
		 * MJAutoMapInfo.get_map_info(getMapId()); if (mInfo != null) {
		 * do_finish_client_auto(eReason.INVALID_MAP); } }
		 */

		TeleporterActionListener listener = ActionListenerLoader.getInstance()
				.findListener(
						ListenerFinderTable.getInstance().getListenerInfo(
								getMapId()));
		if (listener != null) {
			if (listener.get_limit_level() > 0) {
				if (getLevel() > listener.get_limit_level()) {
					int[] loc = Getback.GetBack_Location(this, false);
					start_teleport(loc[0], loc[1], loc[2], getHeading(), 18339,
							true);
				}
			}
		}
	}

	private static final int BONUS_LEVEL_80 = 1;
	private static final int BONUS_LEVEL_82 = 2;
	private static final int BONUS_LEVEL_84 = 4;
	private static final int BONUS_LEVEL_86 = 8;
	private static final int BONUS_LEVEL_88 = 16;
	private static final int BONUS_LEVEL_90 = 32;

	private void levelDown(int gap) {
		Random random = new Random();
		resetLevel();

		/** 2017-11-06 伊娃的祝福減少概率翻新 **/
		SC_REST_EXP_INFO_NOTI.send(this);

		for (int i = 0; i > gap; i--) {
			int minmp = CalcStat
					.MinincreaseMp(getType(), getAbility().getWis());
			int maxmp = CalcStat
					.MaxincreaseMp(getType(), getAbility().getWis());
			short randomHp = (short) (CalcStat.PureHp(getType(), getAbility()
					.getCon()) + random.nextInt(2));
			int randomMp = (int) ((Math.random() * (maxmp - minmp)) + minmp);
			if (minmp == 0)
				randomMp = random.nextInt(maxmp + 1);
			addBaseMaxHp((short) -randomHp);
			addBaseMaxMp((short) -randomMp);
		}
		resetBaseHitup();
		resetBaseDmgup();
		resetBaseAc();
		resetBaseMr();

		if (!isGm() && Config.Login.LevelDownRange != 0) {
			if (getHighLevel() - getLevel() == Config.Login.LevelDownRange - 1) {
				sendPackets("如果再降級一次，角色將被沒收。");
			}
			if (!isGm() && getHighLevel() - getLevel() >= Config.Login.LevelDownRange) {
				sendPackets(new S_ServerMessage(64));
				sendPackets(new S_Disconnect());
				_log.info(String.format(
						"由於超出了降級的允許範圍，已強制斷開 %s。", getName()));
			}
		}

		try {
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		StartCharBoho();
		sendPackets(new S_OwnCharStatus(this));
		start_teleport(getX(), getY(), getMapId(), getHeading(), 18339, false,
				false);
	}

	public void beginGameTimeCarrier() {
		new GameTimeCarrier(this).start();
	}

	public boolean isGhost() {
		return _ghost;
	}

	public void setGhost(boolean flag) {
		_ghost = flag;
	}

	public boolean isGhostCanTalk() {
		return _ghostCanTalk;
	}

	public void setGhostCanTalk(boolean flag) {
		_ghostCanTalk = flag;
	}

	public boolean isReserveGhost() {
		return _isReserveGhost;
	}

	public void setReserveGhost(boolean flag) {
		_isReserveGhost = flag;
	}

	public void beginGhost() {
		if (!isGhost()) {
			setGhost(true);
			_ghostSaveLocX = getX();
			_ghostSaveLocY = getY();
			_ghostSaveMapId = getMapId();
			GhostController.getInstance().addMember(this);
		}
	}

	public void beginGhost(int locx, int locy, short mapid, boolean canTalk) {
		beginGhost(locx, locy, mapid, canTalk, 0);
	}

	public void beginGhost(int locx, int locy, short mapid, boolean canTalk,
	                       int sec) {
		if (isGhost()) {
			return;
		}
		_ghostSaveLocX = getX();
		_ghostSaveLocY = getY();
		_ghostSaveMapId = getMapId();
		// _ghostSaveHeading = getHeading();
		setGhost(true);
		setGhostCanTalk(canTalk);
		setReserveGhost(false);
		// L1Teleport.teleport(this, locx, locy, mapid, 5, true);
		this.start_teleport(locx, locy, mapid, 5, 18339, true, false);
		if (sec > 0) {
			_ghostFuture = GeneralThreadPool.getInstance().schedule(
					new L1PcGhostMonitor(getId()), sec * 1000);
		}
	}

	public void makeReadyEndGhost() {

		setGhost(false);
		setReserveGhost(true);
		// L1Teleport.teleport(this, _ghostSaveLocX, _ghostSaveLocY, (short)
		// _ghostSaveMapId, 5, true);
		this.start_teleport(_ghostSaveLocX, _ghostSaveLocY, _ghostSaveMapId, 5,
				18339, true, false);
		GhostController.getInstance().removeMember(this);

		// setReserveGhost(true);
		// L1Teleport.teleport(this, _ghostSaveLocX, _ghostSaveLocY,
		// _ghostSaveMapId, _ghostSaveHeading, true);
	}

	public void DeathMatchEndGhost() {
		setReserveGhost(true);
		this.start_teleport(32614, 32735, 4, 5, 18339, true, false);
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
			// short mapid = 666;
			// L1Teleport.teleport(this, locx, locy, mapid, 5, false);
			this.start_teleport(locx, locy, mapid, 5, 18339, false, false);
		}

		if (isFirst) {
			if (get_PKcount() <= 10) {
				setHellTime(180);
			} else {
				setHellTime(300 * (get_PKcount() - 100) + 300);
			}
			sendPackets(new S_BlueMessage(552, String.valueOf(get_PKcount()),
					String.valueOf(getHellTime() / 60)));
		} else {
			sendPackets(new S_BlueMessage(637, String.valueOf(getHellTime())));
		}
		if (_hellFuture == null) {
			_hellFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate(
					new L1PcHellMonitor(getId()), 0L, 1000L);
		}
	}

	public void endHell() {
		if (_hellFuture != null) {
			_hellFuture.cancel(false);
			_hellFuture = null;
		}
		int[] loc = L1TownLocation
				.getGetBackLoc(L1TownLocation.TOWNID_ORCISH_FOREST);
		this.start_teleport(loc[0], loc[1], loc[2], 5, 18339, true, false);
		try {
			save();
		} catch (Exception ignore) {
		}
	}

	@Override
	public void setPoisonEffect(int effectId) {
		sendPackets(new S_Poison(getId(), effectId));
		if (!isGmInvis() && !isGhost() && !isInvisble()) {
			broadcastPacket(new S_Poison(getId(), effectId));
		}
	}

	@Override
	public void healHp(int pt) {
		super.healHp(pt);
		sendPackets(new S_HPUpdate(this));
	}

	@Override
	public void addDg(int i) {
		super.addDg(i);
		sendPackets(new S_OwnCharAttrDef(this));
	}

	@Override
	public int getKarma() {
		return _karma.get();
	}

	@Override
	public void setKarma(int i) {
		_karma.set(i);
	}

	public void addKarma(int i) {
		synchronized (_karma) {
			_karma.add(i);
		}
	}

	public int getKarmaLevel() {
		return _karma.getLevel();
	}

	public int getKarmaPercent() {
		return _karma.getPercent();
	}

	public Timestamp getLastPk() {
		return _lastPk;
	}

	public void setLastPk(Timestamp time) {
		_lastPk = time;
	}

	public void setLastPk() {
		_lastPk = new Timestamp(System.currentTimeMillis());
	}

	public boolean isWanted() {
		if (_lastPk == null) {
			return false;
		} else if (System.currentTimeMillis() - _lastPk.getTime() > 24 * 3600 * 1000) {
			setLastPk(null);
			return false;
		}
		return true;
	}

	public Timestamp getDeleteTime() {
		return _deleteTime;
	}

	public void setDeleteTime(Timestamp time) {
		_deleteTime = time;
	}

	public Timestamp getLastLoginTime() {
		return _lastLoginTime;
	}

	public void setLastLoginTime(Timestamp time) {
		_lastLoginTime = time;
	}
	private Timestamp _lastLogoutTime;
	public void setLastLogoutTime (Timestamp time) {
		_lastLogoutTime = time;
	}

	public Timestamp getLastLogoutTime () {
		return _lastLogoutTime;
	}

	@Override
	public int getMagicLevel() {
		return getClassFeature().getMagicLevel(getLevel());
	}

	public int getWeightReduction() {
		return _weightReduction;
	}

	public void addWeightReduction(int i) {
		_weightReduction += i;
		if (getAI() == null)
			this.sendPackets(new S_Weight(this));
	}

	//龍之珍珠效果物品

	public int getDragonPearItemEquipped() {
		return _dragonpearItemEquipped;
	}

	public void addDragonPearItemEquipped(int i) {
		_dragonpearItemEquipped += i;
	}

	public void removeDragonPearSkillEffect() {
		if (hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL))
			removeSkillEffect(L1SkillId.STATUS_DRAGON_PEARL);
	}

	public int getHasteItemEquipped() {
		return _hasteItemEquipped;
	}

	public void addHasteItemEquipped(int i) {
		_hasteItemEquipped += i;
	}

	public void removeHasteSkillEffect() {
		if (hasSkillEffect(L1SkillId.SLOW))
			removeSkillEffect(L1SkillId.SLOW);
		if (hasSkillEffect(L1SkillId.HASTE))
			removeSkillEffect(L1SkillId.HASTE);
		if (hasSkillEffect(L1SkillId.MOB_HASTE))
			removeSkillEffect(L1SkillId.MOB_HASTE);
		if (hasSkillEffect(L1SkillId.GREATER_HASTE))
			removeSkillEffect(L1SkillId.GREATER_HASTE);
		if (hasSkillEffect(L1SkillId.STATUS_HASTE))
			removeSkillEffect(L1SkillId.STATUS_HASTE);
	}

	private Timestamp _tamTime;

	public Timestamp getTamTime() {
		return _tamTime;
	}

	public void setTamTime(Timestamp time) {
		_tamTime = time;
	}

	private int _tamreserve;

	public int getTamReserve() {
		return _tamreserve;
	}

	public void setTamReserve(int i) {
		_tamreserve = i;
	}

	private boolean returnStatus = false;
	private boolean returnStatus_Start = false;
	private boolean returnStatus_Levelup = false;

	public boolean isReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(boolean returnStatus) {
		this.returnStatus = returnStatus;
	}

	public boolean isReturnStatus_Start() {
		return returnStatus_Start;
	}

	public void setReturnStatus_Start(boolean returnStatus_Start) {
		this.returnStatus_Start = returnStatus_Start;
	}

	public boolean isReturnStatus_Levelup() {
		return returnStatus_Levelup;
	}

	public void setReturnStatus_Levelup(boolean returnStatus_Levelup) {
		this.returnStatus_Levelup = returnStatus_Levelup;
	}

	public void resetBaseDmgup() {
		int newBaseDmgup = 0;
		int newBaseBowDmgup = 0;
		int newBaseStatDmgup = CalcStat.calcDmgup(getAbility().getTotalStr())
				+ CalcStat.calcPureMeleeDmgup(getAbility().getStr());
		int newBaseStatBowDmgup = CalcStat.calcBowDmgup(getAbility()
				.getTotalDex())
				+ CalcStat.calcPureMissileDmgup(getAbility().getDex());
		if (isKnight() || isDragonknight() || isDarkelf()) {
			newBaseDmgup = getLevel() / 10;
			newBaseBowDmgup = 0;
		} else if (isElf()) {
			newBaseDmgup = 0;
			newBaseBowDmgup = getLevel() / 10;
		}
		addDmgup((newBaseDmgup + newBaseStatDmgup) - _baseDmgup);
		addBowDmgup((newBaseBowDmgup + newBaseStatBowDmgup) - _baseBowDmgup);
		_baseDmgup = newBaseDmgup + newBaseStatDmgup;
		_baseBowDmgup = newBaseBowDmgup + newBaseStatBowDmgup;
	}

	public void resetBaseHitup() {
		int newBaseHitup = 0;
		int newBaseBowHitup = 0;
		int newBaseStatHitup = CalcStat.calcHitup(getAbility().getTotalStr())
				+ CalcStat.calcPureMeleeHitup(getAbility().getStr());
		int newBaseStatBowHitup = CalcStat.calcBowHitup(getAbility()
				.getTotalDex())
				+ CalcStat.calcPureMissileHitup(getAbility().getDex());

		newBaseHitup = Math.max(0, this.getLevel() - 50);
		newBaseBowHitup = Math.max(0, this.getLevel() - 50);

		addHitup((newBaseHitup + newBaseStatHitup) - _baseHitup);
		addBowHitup((newBaseBowHitup + newBaseStatBowHitup) - _baseBowHitup);
		_baseHitup = newBaseHitup + newBaseStatHitup;
		_baseBowHitup = newBaseBowHitup + newBaseStatBowHitup;
	}

	public void resetOriginalMagicHit() {
		_originalMagicHit = CalcStat.calcMagicHitUp(getAbility().getTotalInt());
		_baseMagicDmg = CalcStat.calcMagicDmg(getAbility().getTotalInt());
	}

	public void resetBaseAc() {
		int newAc = 10 + CalcStat.calcAc(getAbility().getTotalDex());
		if (_type == 3)// 精靈
			newAc -= (getLevel() / 8);
		else if (_type == 4)
			newAc -= (getLevel() / 6);
		else
			newAc -= (getLevel() / 7);
		ac.addAc(newAc - _baseAc);
		_baseAc = newAc;
		sendPackets(new S_OwnCharAttrDef(this));
	}

	public void resetBaseMr() {
		resistance.setBaseMr(CalcStat.calcStatMr(_type, getAbility().getTotalWis()));
		sendPackets(new S_SPMR(this));
	}

	public void resetLevel() {
		setLevel(ExpTable.getLevelByExp(get_exp()));
		if (_hpRegen != null) {
			_hpRegen.updateLevel();
		}
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
		if (_chatCount == 0) {
			_chatCount++;
			_oldChatTimeInMillis = nowChatTimeInMillis;
			return;
		}

		long chatInterval = nowChatTimeInMillis - _oldChatTimeInMillis;
		if (chatInterval > 2000) {
			_chatCount = 0;
			_oldChatTimeInMillis = 0;
		} else {
			if (_chatCount >= 3) {
				setSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED, 120 * 1000);
				sendPackets(new S_SkillIconGFX(36, 120));
				sendPackets(new S_ServerMessage(153));
				_chatCount = 0;
				_oldChatTimeInMillis = 0;
			}
			_chatCount++;
		}
	}

	public void CheckChangeExp() {
		int level = ExpTable.getLevelByExp(get_exp());
		int char_level = CharacterTable.getInstance().PcLevelInDB(getId());
		if (char_level == 0) {
			return;
		}
		int gap = level - char_level;
		if (gap == 0) {
			sendPackets(new S_OwnCharStatus(this));
			int percent = ExpTable.getExpPercentage(char_level, get_exp());
			if (char_level >= 60 && char_level <= 64) {
				if (percent >= 10)
					removeSkillEffect(L1SkillId.LEVEL_UP_BONUS);
			} else if (char_level >= 65) {
				if (percent >= 5) {
					removeSkillEffect(L1SkillId.LEVEL_UP_BONUS);
				}
			}
			return;
		}

		if (gap > 0) {
			levelUp(gap);
			if (getLevel() >= 60) {
				setSkillEffect(L1SkillId.LEVEL_UP_BONUS, 10800000);
				sendPackets(new S_PacketBox(10800, true, true), true);
			}
		} else if (gap < 0) {
			levelDown(gap);
			removeSkillEffect(L1SkillId.LEVEL_UP_BONUS);
		}
	}

	public void checkStatus() throws Exception {
		int remain = remainBonusStats();
		if (remain < 0 && !isGm()) {
			if (getNetConnection() != null) {
				// sendPackets(new S_ReturnedStat(this, S_ReturnedStat.START));
				getNetConnection().kick();
			}

			throw new Exception(String.format("%s 屬性異常 踢除", getName()));
		}
	}

	public long TamTime() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Timestamp tamtime = null;
		long time = 0;
		long sysTime = System.currentTimeMillis();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT `TamEndTime` FROM `characters` WHERE account_name = ? ORDER BY `TamEndTime` ASC"); // 角色
			pstm.setString(1, getAccountName());
			rs = pstm.executeQuery();
			while (rs.next()) {
				tamtime = rs.getTimestamp("TamEndTime");
				if (tamtime != null) {
					if (sysTime < tamtime.getTime()) {
						time = tamtime.getTime() - sysTime;
						break;
					}
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
			pstm = con
					.prepareStatement("SELECT * FROM `characters` WHERE account_name = ?");
			pstm.setString(1, getAccountName());
			rs = pstm.executeQuery();
			while (rs.next()) {
				tamtime = rs.getTimestamp("TamEndTime");
				char_objid = rs.getInt("objid");
				if (tamtime != null) {
					if ((sysTime / 1000) + 2 <= (tamtime.getTime() / 1000)) {
						count++;
					} else {
						if (Tam_wait_count(char_objid) != 0) {
							int day = Nexttam(char_objid);
							if (day != 0) {
								Timestamp deleteTime = null;
								deleteTime = new Timestamp(sysTime
										+ (86400000 * (long) day) + 10000);// 7日
								if (getId() == char_objid) {
									setTamTime(deleteTime);
								}
								try {
									con2 = L1DatabaseFactory.getInstance()
											.getConnection();
									pstm2 = con2
											.prepareStatement("UPDATE `characters` SET TamEndTime=? WHERE account_name = ? AND objid = ?");
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
			pstm = con
					.prepareStatement("delete from Tam where objid = ? order by id asc limit 1");
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
			pstm = con
					.prepareStatement("SELECT day FROM `tam` WHERE objid = ? order by id asc limit 1");
			pstm.setInt(1, objectId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				day = rs.getInt("Day");
			}
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
			while (rs.next()) {
				count = getId();
			}
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
		if (hasSkillEffect(ABSOLUTE_BARRIER)) {
			removeSkillEffect(ABSOLUTE_BARRIER);
		}
	}

	public int get_PKcount() {
		return _PKcount;
	}

	public void set_PKcount(int i) {
		_PKcount = i;
	}

	public int getClanid() {
		return _clanid;
	}

	public void setClanid(int i) {
		_clanid = i;
	}

	public String getClanname() {
		return clanname;
	}

	public void setClanname(String s) {
		String previous = clanname;
		clanname = s;
		if (previous != s) {
			MJObjectEventProvider.provider().pcEventFactory()
					.firePledgeChanged(this, s, previous);
		}
	}

	public int getRedKnightClanId() {
		return _redKnightClanId;
	}

	public void setRedKnightClanId(int i) {
		_redKnightClanId = i;
	}

	public int getRedKnightDamage() {
		return _redKnightDamage;
	}

	public void setRedKnightDamage(int i) {
		_redKnightDamage = i;
	}

	public void addRedKnightDamage(int i) {
		_redKnightDamage += i;
	}

	public int getRedKnightKill() {
		return _redKnightKill;
	}

	public void setRedKnightKill(int i) {
		_redKnightKill = i;
	}

	public void addRedKnightKill(int i) {
		_redKnightKill += i;
	}

	private String _sealingPW; //

	public String TempQuiz = "";

	public String getSealingPW() {
		return _sealingPW;
	}

	public void setSealingPW(String s) {
		_sealingPW = s;
	}

	int _sealScrollTime;

	public void setSealScrollTime(int sealScrollTime) {
		_sealScrollTime = sealScrollTime;
	}

	public int getSealScrollTime() {
		return _sealScrollTime;
	}

	int _sealScrollCount;

	public void setSealScrollCount(int sealScrollCount) {
		_sealScrollCount = sealScrollCount;
	}

	public int getSealScrollCount() {
		return _sealScrollCount;
	}

	public L1Clan getClan() {
		return L1World.getInstance().getClan(getClanid());
	}

	public int getClanRank() {
		return _clanRank;
	}

	public void setClanRank(int i) {
		_clanRank = i;
	}

	public byte get_sex() {
		return _sex;
	}

	public void set_sex(int i) {
		_sex = (byte) i;
	}

	/** 나이설정 **/
	public int getAge() {
		return _age;
	}

	public void setAge(int i) {
		_age = i;
	}

	public boolean isGm() {
		return _gm;
	}

	public void setGm(boolean flag) {
		_gm = flag;
	}

	public boolean isMonitor() {
		return _monitor;
	}

	public void setMonitor(boolean flag) {
		_monitor = flag;
	}

	public int getDamageReductionByArmor() {
		if (this != null && this.isPassive(MJPassiveID.INFINITI_ARMOR.toInt())) {
			int Level = this.getLevel();
			if (Level < 86) {
				Level = 86;
			}
			int point = ((Level - 86) / 2) * 2 + 5;
			if (point >= 15) {
				point = 15;
			}
			return _damageReductionByArmor + point;
		}
		return _damageReductionByArmor;
	}

	public void addDamageReductionByArmor(int i) {
		_damageReductionByArmor += i;
	}

	public int getDamageReductionIgnore() {
		return _damageReductionIgnore;
	}

	public void addDamageReductionIgnore(int i) {
		_damageReductionIgnore += i;
	}

	public int getDamageReduction() {
		return _DamageReduction;
	}

	public void addDamageReduction(int i) {
		_DamageReduction += i;
	}

	public int pvp_defense;

	public int get_pvp_defense() {
		/**
		 * TODO 等級60以上開始增加按職業分類的PVP傷害減少（實現）
		 **/
		int pvpreduc = 0;
		if (isCrown()) {
			pvpreduc += getLevel() >= 60 ? (getLevel() - 56) / 4 : 0;
		} else if (isKnight()) {
			pvpreduc += getLevel() >= 60 ? (getLevel() - 58) / 2 : 0;
		} else if (isElf()) {
			pvpreduc += getLevel() >= 60 ? (getLevel() - 56) / 4 : 0;
		} else if (isWizard()) {
			pvpreduc += getLevel() >= 60 ? (getLevel() - 56) / 4 : 0;
		} else if (isDarkelf()) {
			pvpreduc += getLevel() >= 60 ? (getLevel() - 57) / 3 : 0;
		} else if (isBlackwizard()) {
			pvpreduc += getLevel() >= 60 ? (getLevel() - 57) / 3 : 0;
		} else if (isDragonknight()) {
			pvpreduc += getLevel() >= 60 ? (getLevel() - 57) / 3 : 0;
		} else if (isWarrior()) {
			pvpreduc += getLevel() >= 60 ? (getLevel() - 57) / 3 : 0;
		} else if (isFencer()) {
			pvpreduc += getLevel() >= 60 ? (getLevel() - 58) / 2 : 0;
		} else if (isLancer()) {
			pvpreduc += getLevel() >= 60 ? (getLevel() - 58) / 2 : 0;
		}

		if (pvpreduc > 0) {
			return pvp_defense + pvpreduc;
		}

		return pvp_defense;
	}

	public void set_pvp_defense(int i) {
		this.pvp_defense = CommonUtil.get_current(i, 0, 30);
	}

	public int getBowDmgRate() {
		return _bowDmgRate;
	}

	public void addBowDmgRate(int i) {
		_bowDmgRate += i;
	}

	public int getDmgRate() {
		return _DmgRate;
	}

	public void addDmgRate(int i) {
		_DmgRate += i;
	}

	public int getBowHitRate() {
		return _bowHitRate;
	}

	public void addBowHitRate(int i) {
		_bowHitRate += i;
	}

	public int getHitRate() {
		return _HitRate;
	}

	public void addHitRate(int i) {
		_HitRate += i;
	}

	public int getDmgupByArmor() {
		return _DmgupByArmor;
	}

	public void addDmgupByArmor(int i) {
		_DmgupByArmor += i;
	}

	public int getBowDmgupByArmor() {
		return _bowDmgupByArmor;
	}

	public void addBowDmgupByArmor(int i) {
		_bowDmgupByArmor += i;
	}

	private void setGresValid(boolean valid) {
		_gresValid = valid;
	}

	public boolean isGresValid() {
		return _gresValid;
	}

	public long getFishingTime() {
		return _fishingTime;
	}

	public void setFishingTime(long i) {
		_fishingTime = i;
	}

	public boolean isFishing() {
		return _isFishing;
	}

	public boolean isFishingReady() {
		return _isFishingReady;
	}

	public void setFishing(boolean flag) {
		_isFishing = flag;
	}

	public void setFishingReady(boolean flag) {
		_isFishingReady = flag;
	}

	public int getCookingId() {
		return _cookingId;
	}

	public void setCookingId(int i) {
		_cookingId = i;
	}

	public int getDessertId() {
		return _dessertId;
	}

	public void setDessertId(int i) {
		_dessertId = i;
	}

	/** 套裝商店 **/
	private int CashStep = 0;

	public int getCashStep() {
		return CashStep;
	}

	public void setCashStep(int cashStep) {
		CashStep = cashStep;
	}

	/** 套裝商店 **/

	/** 機器人啟動 **/
	private int teleportTime = 0;
	private int teleportTime2 = 0;
	private int skillTime = 0;
	private int skillTime2 = 0;
	private long _quiztime = 0;
	private long _quiztime2 = 0;
	private long _quiztime3 = 0;
	// private int currentTeleportCount = 0;
	private int currentSkillCount = 0;
	private int currentSkillCount2 = 0;

	// 連擊系統
	private int comboCount;

	public long getQuizTime() {
		return _quiztime;
	}

	public void setQuizTime(long l) {
		_quiztime = l;
	}

	public long getQuizTime2() {
		return _quiztime2;
	}

	public void setQuizTime2(long l) {
		_quiztime2 = l;
	}

	public long getQuizTime3() {
		return _quiztime3;
	}

	public void setQuizTime3(long l) {
		_quiztime3 = l;
	}

	public int getTeleportTime() {
		return teleportTime;
	}

	public void setTeleportTime(int teleportTime) {
		this.teleportTime = teleportTime;
	}

	public int getTeleportTime2() {
		return teleportTime2;
	}

	public void setTeleportTime2(int teleportTime2) {
		this.teleportTime2 = teleportTime2;
	}

	public int getSkillTime2() {
		return skillTime2;
	}

	public void setSkillTime2(int skillTime2) {
		this.skillTime2 = skillTime2;
	}

	public int getSkillTime() {
		return skillTime;
	}

	public void setSkillTime(int skillTime) {
		this.skillTime = skillTime;
	}

	public int getCurrentSkillCount() {
		return currentSkillCount;
	}

	public void setCurrentSkillCount(int currentSkillCount) {
		this.currentSkillCount = currentSkillCount;
	}

	public int getCurrentSkillCount2() {
		return currentSkillCount2;
	}

	public void setCurrentSkillCount2(int currentSkillCount2) {
		this.currentSkillCount2 = currentSkillCount2;
	}

	public int getTempCharGfxAtDead() {
		return _tempCharGfxAtDead;
	}

	public void setTempCharGfxAtDead(int i) {
		_tempCharGfxAtDead = i;
	}

	public boolean isCanWhisper() {
		return _isCanWhisper;
	}

	public void setCanWhisper(boolean flag) {
		_isCanWhisper = flag;
	}

	public boolean isShowTradeChat() {
		return _isShowTradeChat;
	}

	public void setShowTradeChat(boolean flag) {
		_isShowTradeChat = flag;
	}

	public boolean isShowWorldChat() {
		return _isShowWorldChat;
	}

	public void setShowWorldChat(boolean flag) {
		_isShowWorldChat = flag;
	}

	public int getFightId() {
		return _fightId;
	}

	public void setFightId(int i) {
		_fightId = i;
	}

	public void setDeathMatch(boolean i) {
		this.isDeathMatch = i;
	}

	public boolean isDeathMatch() {
		return isDeathMatch;
	}

	public boolean isSupporting() {
		return _isSupporting;
	}

	public void setSupporting(boolean flag) {
		_isSupporting = flag;
	}

	public int getCallClanId() {
		return _callClanId;
	}

	public void setCallClanId(int i) {
		_callClanId = i;
	}

	public int getCallClanHeading() {
		return _callClanHeading;
	}

	public void setCallClanHeading(int i) {
		_callClanHeading = i;
	}

	/** 巴風特系統 **/
	private int m_bapo_level = L1PcExpMonitor.NONE_STATE_BAPO_LEVEL;

	public void set_bapo_level(int bapo_level) {
		m_bapo_level = bapo_level;
	}

	public int get_bapo_level() {
		return m_bapo_level;
	}

	/** 昏迷增益啟動 **/
	private int _deathmatch;

	public int getDeathMatchPiece() {
		return _deathmatch;
	}

	public void setDeathMatchPiece(int i) {
		_deathmatch = i;
	}

	private int _petrace;

	public int getPetRacePiece() {
		return _petrace;
	}

	public void setPetRacePiece(int i) {
		_petrace = i;
	}

	private int _ultimatebattle;

	public int getUltimateBattlePiece() {
		return _ultimatebattle;
	}

	public void setUltimateBattlePiece(int i) {
		_ultimatebattle = i;
	}

	private int _petmatch;

	public int getPetMatchPiece() {
		return _petmatch;
	}

	public void setPetMatchPiece(int i) {
		_petmatch = i;
	}

	private int _ghosthouse;

	public int getGhostHousePiece() {
		return _ghosthouse;
	}

	public void setGhostHousePiece(int i) {
		_ghosthouse = i;
	}

	/** 昏迷增益結束 **/
	/** 強化錯誤處理 **/
	private int _enchantitemid = 0;

	public int getLastEnchantItemid() {
		return _enchantitemid;
	}

	/** 飾品強化更新 **/
	public int _accessoryHeal = 0;

	public int getAccessoryHeal() {
		return _accessoryHeal;
	}

	public void setAccessoryHeal(int i) {
		_accessoryHeal = i;
	}

	public void addAccessoryHeal() {
		_accessoryHeal += 1;
	}

	public void setLastEnchantItemid(int i, L1ItemInstance item) {
		// 為防萬一的臨時新增
		if (getLastEnchantItemid() == i && i != 0) {
			sendPackets(new S_Disconnect());
			getInventory().removeItem(item, item.getCount());
			return;
		}
		_enchantitemid = i;
	}

	/*
	 * private int _healpotion = 0; // 藥水回復量
	 *
	 * public int getHealPotion() { return _healpotion; }
	 *
	 * public void addHealPotion(int value) { _healpotion += value; }
	 */
	/** 小遊戲 **/
	// 骰子
	private boolean _isGambling = false;

	public boolean isGambling() {
		return _isGambling;
	}

	public void setGambling(boolean flag) {
		_isGambling = flag;
	}

	private int _gamblingmoney = 0;

	public int getGamblingMoney() {
		return _gamblingmoney;
	}

	public void setGamblingMoney(int i) {
		_gamblingmoney = i;
	}

	// 牛騾
	private boolean _isGambling3 = false;

	public boolean isGambling3() {
		return _isGambling3;
	}

	public void setGambling3(boolean flag) {
		_isGambling3 = flag;
	}

	private int _gamblingmoney3 = 0;

	public int getGamblingMoney3() {
		return _gamblingmoney3;
	}

	public void setGamblingMoney3(int i) {
		_gamblingmoney3 = i;
	}

	private ArrayList<String> _cmalist = new ArrayList<String>();

	/**
	 * 當用戶使用氏族匹配申請和請求清單時，將氏族名稱放入陣列中；當君主使用時，將申請者的名字放入陣列中。
	 */
	public void addCMAList(String name) {
		if (_cmalist.contains(name)) {
			return;
		}
		_cmalist.add(name);
	}

	public void removeCMAList(String name) {
		if (!_cmalist.contains(name)) {
			return;
		}
		_cmalist.remove(name);
	}

	public ArrayList<String> getCMAList() {
		return _cmalist;
	}

	private ArrayList<Integer> skillList = new ArrayList<Integer>();

	private int _clanMemberId;

	public int getClanMemberId() {
		return _clanMemberId;
	}

	public void setClanMemberId(int i) {
		_clanMemberId = i;
	}

	private String _clanMemberNotes;

	public String getClanMemberNotes() {
		return _clanMemberNotes;
	}

	public void setClanMemberNotes(String s) {
		_clanMemberNotes = s;
	}

	// 實現古代的祝福效果
	private void drop1(L1Character lastAttacker) {
		if ((getMapId() >= 1708 && getMapId() <= 1709)
				&& getInventory().checkEquipped(900022)) { // 穿戴的物品
			L1ItemInstance drop = ItemTable.getInstance().createItem(3000122); // 將要掉落的物品
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item.getItemId() == 900022 & item.isEquipped()) {
					sendPackets(new S_ServerMessage(3802));
					sendPackets(new S_ServerMessage(158, "$22172"));
					getInventory().removeItem(item, 1);

					if (lastAttacker instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) lastAttacker;
						if (DropDelayItemTable.getInstance().isItem(
								drop.getItemId())) {
							L1DropDelayItem temp = DropDelayItemTable
									.getInstance().getItem(drop.getItemId());
							if (temp != null) {
								if (temp.getItemId() == drop.getItemId()) {
									drop.startItemOwnerTimer(pc);
								}
							}
						}
					}

					L1World.getInstance()
							.getInventory(getX(), getY(), getMapId())
							.storeItem(drop);
					LoggerInstance.getInstance().addItemAction(
							ItemActionType.DeathDrop, this, item, 1);
					break;
				}
			}
		}
	}

	// 不死的祝福
	private void gahodrop(L1Character lastAttacker) {
		if (getInventory().checkItem(4100122)) {
			L1ItemInstance drop = ItemTable.getInstance().createItem(4100120);
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item.getItemId() == 4100122) {
					sendPackets(new S_ServerMessage(3802));
					sendPackets(new S_ServerMessage(158, "$27280"));
					getInventory().removeItem(item, 1);

					if (lastAttacker instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) lastAttacker;
						if (DropDelayItemTable.getInstance().isItem(
								drop.getItemId())) {
							L1DropDelayItem temp = DropDelayItemTable
									.getInstance().getItem(drop.getItemId());
							if (temp != null) {
								if (temp.getItemId() == drop.getItemId()) {
									drop.startItemOwnerTimer(pc);
								}
							}
						}
					}

					L1World.getInstance()
							.getInventory(getX(), getY(), getMapId())
							.storeItem(drop);
					LoggerInstance.getInstance().addItemAction(
							ItemActionType.DeathDrop, this, item, 1);
					break;
				}
			}
		}
	}

	// 高級不死的祝福
	private void specialgahodrop(L1Character lastAttacker) {
		if (getInventory().checkItem(4100121)) {
			L1ItemInstance drop = ItemTable.getInstance().createItem(4100119);
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item.getItemId() == 4100121) {
					sendPackets(new S_ServerMessage(3802));
					sendPackets(new S_ServerMessage(158, "\\f3$26712"));
					getInventory().removeItem(item, 1);

					if (lastAttacker instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) lastAttacker;
						if (DropDelayItemTable.getInstance().isItem(
								drop.getItemId())) {
							L1DropDelayItem temp = DropDelayItemTable
									.getInstance().getItem(drop.getItemId());
							if (temp != null) {
								if (temp.getItemId() == drop.getItemId()) {
									drop.startItemOwnerTimer(pc);
								}
							}
						}
					}

					L1World.getInstance()
							.getInventory(getX(), getY(), getMapId())
							.storeItem(drop);
					LoggerInstance.getInstance().addItemAction(
							ItemActionType.DeathDrop, this, item, 1);
					break;
				}
			}
		}
	}

	private void specialgahodrop1(L1Character lastAttacker) {
		if (getInventory().checkItem(4100529)) {
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item.getItemId() == 4100529) {
					sendPackets(new S_ServerMessage(3802));
					sendPackets(new S_ServerMessage(158, "\\f3$26712"));
					getInventory().removeItem(item, 1);
					LoggerInstance.getInstance().addItemAction(
							ItemActionType.DeathDrop, this, item, 1);
					break;
				}
			}
		}
	}

	private void drop2(L1Character lastAttacker) {
		if (getInventory().checkEquipped(10000)) { // 裝備的物品
			L1ItemInstance drop = ItemTable.getInstance().createItem(738); // 掉落
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item.getItemId() == 10000 & item.isEquipped()) {
					sendPackets(new S_ServerMessage(3802));
					sendPackets(new S_ServerMessage(158, "$25382"));
					getInventory().removeItem(item, 1);

					if (lastAttacker instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) lastAttacker;
						if (DropDelayItemTable.getInstance().isItem(drop.getItemId())) {
							L1DropDelayItem temp = DropDelayItemTable.getInstance().getItem(drop.getItemId());
							if (temp != null) {
								if (temp.getItemId() == drop.getItemId()) {
									drop.startItemOwnerTimer(pc);
								}
							}
						}
					}

					L1World.getInstance()
							.getInventory(getX(), getY(), getMapId())
							.storeItem(drop);
					LoggerInstance.getInstance().addItemAction(ItemActionType.DeathDrop, this, item, 1);
					break;
				}
			}
		}
	}

	private void drop3(L1Character lastAttacker) {
		if (getInventory().checkEquipped(10001)) { // 裝備的物品
			L1ItemInstance drop = ItemTable.getInstance().createItem(739); // 掉落
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item.getItemId() == 10001 & item.isEquipped()) {
					sendPackets(new S_ServerMessage(3802));
					sendPackets(new S_ServerMessage(158, "$25384"));
					getInventory().removeItem(item, 1);

					if (lastAttacker instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) lastAttacker;
						if (DropDelayItemTable.getInstance().isItem(
								drop.getItemId())) {
							L1DropDelayItem temp = DropDelayItemTable
									.getInstance().getItem(drop.getItemId());
							if (temp != null) {
								if (temp.getItemId() == drop.getItemId()) {
									drop.startItemOwnerTimer(pc);
								}
							}
						}
					}

					L1World.getInstance()
							.getInventory(getX(), getY(), getMapId())
							.storeItem(drop);
					LoggerInstance.getInstance().addItemAction(
							ItemActionType.DeathDrop, this, item, 1);
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
		// Sets the clan rank to normal
		setClanRank(L1Clan.NORMAL);
		save();
		move_clan.addClanMember(getName(), getClanRank(), getLevel(), "",
				getId(), getType(), getOnlineStatus(), null);
	}

	public void ClearPlayerClanData(L1Clan clan) throws Exception {
		ClanStorageTable.getInstance().delete_Storage_List(this, this.getName());

		setClanid(0);
		setClanname("");
		setTitle("");
		setClanMemberId(0);
		setClanMemberNotes("");
		setClanRank(0);
		setClanContribution(0);
		setClanJoinDate(null);
		if (this != null) {
			String broadcastTitle = null;
			if (hasSkillEffect(L1SkillId.USER_WANTED1)) {
				broadcastTitle = WANTED_TITLE1;
			}

			sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo("", 11, this));// 必須發送11號
			// 這樣當使用/氏族命令時
			// 應用中心才會彈出
			sendPackets(new S_CharTitle(getId(), broadcastTitle));
			Broadcaster.broadcastPacket(this, new S_CharTitle(getId(),
					broadcastTitle));
			sendPackets(new S_ClanName(this));
			sendPackets(new S_ReturnedStat(getId(), 0));
			Broadcaster.broadcastPacket(this, new S_ReturnedStat(getId(), 0));
			sendPackets(new S_ClanAttention());
			ClanBuffListLoader.getInstance().leave_clan_remove_buff(this);
		}
		save();
	}

	public void startEinhasadTimer() {
		synchronized (this) {
			if (_einhasadTimer != null) {
				return;
			}
			_einhasadTimer = new EinhasadTimer();
			GeneralThreadPool.getInstance().schedule(_einhasadTimer,
					EinhasadTimer.INTERVAL);
		}
	}

	public void stopEinhasadTimer() {
		synchronized (this) {
			if (_einhasadTimer == null) {
				return;
			}
			_einhasadTimer.cancel();
			_einhasadTimer = null;
		}
	}

	EinhasadTimer _einhasadTimer;

	class EinhasadTimer extends RepeatTask {

		public static final int INTERVAL = 15 * 60 * 1000;

		public EinhasadTimer() {
			super(INTERVAL);
		}

		@Override
		public void execute() {
			if (getAccount().getBlessOfAin() >= 2000000) {
				return;
			}
			getAccount().addBlessOfAin(1, L1PcInstance.this);
			SC_REST_EXP_INFO_NOTI.send(L1PcInstance.this);
		}
	}

	public long getFishingShopBuyTime_1() {
		return FishingShopBuyTime_1;
	}

	public void setFishingShopBuyTime_1(long fishingShopBuyTime_1) {
		FishingShopBuyTime_1 = fishingShopBuyTime_1;
	}

	private boolean createNewItem(L1PcInstance pc, int item_id, int count,
	                              int EnchantLevel, int AttEnchantLevel, int Bless) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setAttrEnchantLevel(AttEnchantLevel);
			item.setIdentified(true);
			pc.getInventory().storeItem(item);
			item.setBless(Bless);
			pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
			pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			return true;
		} else {
			return false;
		}
	}

	private int risingUp = 0;

	public int getRisingUp() {
		return risingUp;
	}

	public void setRisingUp(int i) {
		risingUp = i;
	}

	private int impactUp = 0;

	public void setImpactUp(int i) {
		impactUp = i;
	}

	public int getImpactUp() {
		return impactUp;
	}

	private int graceLv = 0;

	public int getGraceLv() {
		return graceLv;
	}

	public void setGraceLv(int i) {
		graceLv = i - 80;
		if (graceLv < 0) {
			graceLv = 0;
		} else if (graceLv > 15) {
			graceLv = 15;
		}
	}

	public void start_teleportForGM(final int x, final int y, final int map,
	                                final int heading, final int effect_id, boolean effect_check,
	                                boolean skill_check) {
		try {
			/** 釣魚時若被瞬移或召喚，則自動結束釣魚 **/
			if (isFishing()) {
				FishingTimeController.getInstance().endFishing(this);
			}
			if (FishingTimeController.getInstance().isMember(this)) {
				FishingTimeController.getInstance().endFishing(this);
			}

			if (hasSkillEffect(SHOCK_STUN) || hasSkillEffect(L1SkillId.EMPIRE)
					|| hasSkillEffect(ICE_LANCE) || hasSkillEffect(BONE_BREAK)
					|| hasSkillEffect(EARTH_BIND) || hasSkillEffect(DESPERADO)
					|| hasSkillEffect(OSIRIS) || hasSkillEffect(L1SkillId.OSIRIS_TICK)
					|| hasSkillEffect(L1SkillId.PHANTOM_DEATH)
					|| hasSkillEffect(L1SkillId.PHANTOM_REQUIEM)
					|| hasSkillEffect(L1SkillId.PHANTOM_RIPER)
					|| hasSkillEffect(L1SkillId.ETERNITI)
					|| hasSkillEffect(L1SkillId.FORCE_STUN)
					|| hasSkillEffect(L1SkillId.TEMPEST)
					|| hasSkillEffect(L1SkillId.PHANTOM)
					|| hasSkillEffect(L1SkillId.CHAINSWORD_STUN) || hasSkillEffect(L1SkillId.BEHEMOTH)
					|| hasSkillEffect(L1SkillId.CRUEL) || hasSkillEffect(L1SkillId.SHADOW_STEP_CHASER)
					|| hasSkillEffect(L1SkillId.DISINTEGRATE) || isParalyzed()
					|| this.isSleeped()) {
				// sendPackets(new S_Paralysis(7, false));
				sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
						false));
				return;
			}
			teleport = true;
			teleport_x = x;
			teleport_y = y;
			teleport_map = map;
			this.setHeading(heading);
			this.sendPackets(new S_Teleport(this));

			if (getInventory().checkEquipped(900022)) {
				boolean mapcheck = getMapId() >= 1708 && getMapId() <= 1712;
				if (!mapcheck) {
					sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1,
							484, false));
				}
			}

			clearTemporaryEffect();
			if (skill_check) {
				if (effect_check) {
					S_SkillSound ss = new S_SkillSound(getId(), effect_id);
					sendPackets(ss, false);
					Broadcaster.broadcastPacket(this, ss, false);
				}
				Runnable teleport = () -> {
					L1Teleport.getInstance().doTeleportation(this);
				};
				GeneralThreadPool.getInstance().schedule(teleport, 200);// 瞬移
				// 延遲
			} else {
				if (effect_check)
					setTemporaryEffect(new S_SkillSound(getId(), effect_id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// sendPackets(new S_Paralysis(7, false));
			sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
		}
	}

	/**
	 * @param x          The x-coordinate
	 * @param y          The y-coordinate
	 * @param map        The map identifier
	 * @param heading    Literally the direction
	 * @param effect_id  The effect ID to be applied
	 * @param effect_check  If true, the effect will be displayed; if false, it will not be displayed
	 */

	public void start_teleport(final int x, final int y, final int map,
	                           final int heading, final int effect_id, boolean effect_check) {
		start_teleport(x, y, map, heading, effect_id, effect_check, false);
	}

	public void start_teleport(final int x, final int y, final int map,
	                           final int heading, final int effect_id, boolean effect_check,
	                           boolean isAlways) {
		try {
//            System.out.println("確認5");
			if (this.hasSkillEffect(L1SkillId.BURNING_SHOT)) {
				sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
						false));
				return;
			}

			if (this.hasSkillEffect(L1SkillId.SHOCK_ATTACK)) {
				this.send_effect(20462); // 19720
				sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
						false));
				return;
			}

			if (this.getshadowstepchaser()) {
				sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
						false));
				return;
			}

			if (this.getPresherDeathRecall()) {
				if (!isDead()) {
					if (getCurrentHp() > 0) {
						int dmg = MJRnd.next(Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_DMG_MIN, Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_DMG);
						this.receiveDamage(this.getPresherPc(), dmg);
						this.send_effect(19349); // 19720
					}
				}
				sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,false));
				return;
			}

			// if (this.getPresherDeathRecall()) {
			// if (!isDead()) {
			// if (getCurrentHp() > 0) {
			// this.receiveDamage(this.getPresherPc(),
			// Config.MagicAdSetting.PRESHER_DEATH_RECALL_DMG);
			// this.send_effect(19349); // 19720
			// }
			// }
			// return;
			// }

			if (effect_check) {
				S_SkillSound ss = new S_SkillSound(getId(), 18339);
				sendPackets(ss, false);
				Broadcaster.broadcastPacket(this, ss, false);
			}

			/** 釣魚時若被瞬移或召喚，則自動結束釣魚 **/
			if (isFishing()) {
				FishingTimeController.getInstance().endFishing(this);
			}
			if (FishingTimeController.getInstance().isMember(this)) {
				FishingTimeController.getInstance().endFishing(this);
			}

			if (hasSkillEffect(SHOCK_STUN) || hasSkillEffect(L1SkillId.EMPIRE)
					|| hasSkillEffect(ICE_LANCE) || hasSkillEffect(BONE_BREAK)
					|| hasSkillEffect(EARTH_BIND) || hasSkillEffect(DESPERADO)
					|| hasSkillEffect(OSIRIS)|| hasSkillEffect(L1SkillId.OSIRIS_TICK)
					|| hasSkillEffect(L1SkillId.CRUEL)
					|| hasSkillEffect(L1SkillId.TEMPEST)
					|| hasSkillEffect(L1SkillId.PHANTOM_DEATH)
					|| hasSkillEffect(L1SkillId.PHANTOM_REQUIEM)
					|| hasSkillEffect(L1SkillId.PHANTOM_RIPER)
					|| hasSkillEffect(L1SkillId.ETERNITI)
					|| hasSkillEffect(L1SkillId.FORCE_STUN)
					|| hasSkillEffect(L1SkillId.PHANTOM)
					|| hasSkillEffect(L1SkillId.PANTHERA)
					|| hasSkillEffect(CHAINSWORD_STUN) || hasSkillEffect(L1SkillId.BEHEMOTH)
					|| hasSkillEffect(L1SkillId.CRUEL) || hasSkillEffect(L1SkillId.SHADOW_STEP_CHASER)
					|| hasSkillEffect(L1SkillId.DISINTEGRATE) || isParalyzed()
					|| this.isSleeped()) {
				sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
						false));
				return;
			}

			MJCompanionInstance companion = get_companion();
			if (companion != null) {
				if (companion.hasSkillEffect(MJCompanionBuffInfo.DOGBLOOD)) {
					try {
						GeneralThreadPool.getInstance().schedule(
								new Runnable() {
									@Override
									public void run() {
										companion.sendSkillContinueEff();
										return new L1PcInstance[0];
									}
								}, 500);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			// TODO 血盟BUFF重整 2017-11-12
			if (getClan() != null) {
				getClan().deleteClanRetrieveUser(getId());
				if (getClan().getEinhasadBlessBuff() != 0) {
					ClanBuff Buff = ClanBuffTable.getBuffList(getClan()
							.getEinhasadBlessBuff());
					String[] Buffmap = null;
					Buffmap = Buff.buffmaplist.split(",");
					if (getClan().getEinhasadBlessBuff() != 0) {
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
					}

					if (map != getClanBuffMap() && getClanBuffMap() != 0) {
						setClanBuffMap(0);
						addEinhasadBlessper(-5);
						SC_REST_EXP_INFO_NOTI.send(this);
					}
				}
			}

			/** 2016.11.26 MJ 應用中心 LFC **/
			if (isDead() && !isAlways) {
				if (!(MJRaidSpace.getInstance().isInInstance(this) || MJInstanceSpace.isInInstance(this))) {
					sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
					return;
				}
			}

			teleport = true;
			teleport_x = x;
			teleport_y = y;
			teleport_map = map;
			this.setHeading(heading);
			this.sendPackets(new S_Teleport(this));

			this.sendPackets(
					new S_PacketBox(S_PacketBox.ATTACK_RANGE, this, this.getWeapon()),
					true);

			if (getInventory().checkEquipped(900022)) {
				boolean mapcheck = getMapId() >= 1708 && getMapId() <= 1712;
				if (!mapcheck) {
					sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1,
							484, false));
				}
			}

			if (getZoneType() <= 0) {
				if (getSafetyZone() == true) {
					sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, false));
					setSafetyZone(false);
				}
			} else {
				if (getSafetyZone() == false) {
					sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, true));
					setSafetyZone(true);
				}
			}

			clearTemporaryEffect();
			// L1Teleport.getInstance().doTeleportation(this);

			if (effect_check) {
				S_SkillSound ss = new S_SkillSound(getId(), effect_id);
				sendPackets(ss, false);
				Broadcaster.broadcastPacket(this, ss, false);
			}
			if (isMassTel()) {
				Runnable teleport = () -> {
					L1Teleport.getInstance().doTeleportation(this);
				};
				GeneralThreadPool.getInstance().schedule(teleport, 287L); // 瞬移魔法
				// 延遲
			}

			if (getSpeedOverCount() != 0) {
				_move_speed_over_count = 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
		}
	}

	public void do_simple_teleport(int x, int y, int mapid) {
		if (isFishing()) {
			FishingTimeController.getInstance().endFishing(this);
		}
		if (FishingTimeController.getInstance().isMember(this)) {
			FishingTimeController.getInstance().endFishing(this);
		}

		if (getInventory().checkEquipped(900022)) {
			boolean mapcheck = getMapId() >= 1708 && getMapId() <= 1712;
			if (!mapcheck) {
				sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 484,
						false));
			}
		}

		S_SkillSound ss = new S_SkillSound(getId(), 169);
		sendPackets(ss, false);
		Broadcaster.broadcastPacket(this, ss, false);
		ss.clear();
		L1Teleport.getInstance().doTeleport(L1PcInstance.this, x, y, mapid);
	}

	public void Cruel_attack_Short(L1Character attacker, int skill_effect) {
		// 新增 警衛 或者 士兵 槍兵
		if (attacker.get_Maelstrom()) {
			return;
		}
		int attacker_x = attacker.getX();
		int attacker_y = attacker.getY();
		int x = this.getX();
		int y = this.getY();
		int moveX = 0;
		int moveY = 0;
		int range = 3;
		if (attacker.isPassive(MJPassiveID.CRUEL_CONBICTION.toInt())) {
			range = 4;
		}
		int heading = MJCommons.calcheading(attacker_x, attacker_y, x, y);


		int Range = attacker.getLocation().getTileDistance(this.getX(), this.getY());

//		double Range = attacker.getLocation().getLineDistance(new Point(this.getX(), this.getY()));
		boolean isRange = (this.getLocation().getTileLineDistance(new Point(attacker.getX(), attacker.getY())) <= range);
		L1Map map = L1WorldMap.getInstance().getMap(this.getMapId());
		boolean firstFail = false;
		int rangecheck = 1;
		switch(heading) {
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
//			System.out.println(Math.abs(attacker_x - getX()) + Math.abs(attacker_y - getY()));
//			System.out.println(Math.abs(attacker_x - x) + Math.abs(attacker_y - y));
			if (Math.abs(attacker_x - x) + Math.abs(attacker_y - y) > rangecheck) {
				while(true){
					if(heading == -1)break;// 모든 방향 실패시
					heading = MJCommons.calcheading(attacker_x, attacker_y, checkX, checkY);
					switch (heading) {
						case 0:checkY++;break;
						case 1:checkX--;checkY++;break;
						case 2:checkX--;break;
						case 3:checkX--;checkY--;break;
						case 4:checkY--;break;
						case 5:checkX++;checkY--;break;
						case 6:checkX++;break;
						case 7:checkX++;checkY++;break;
						default:break;
					}
					//				if (attacker.getLocation().getTileLineDistance(new Point(checkX, checkY)) == 1) {
//					System.out.println("tile:"+attacker.getLocation().getTileDistance(checkX, checkY));
					if (attacker.getLocation().getLineDistance(new Point(checkX, checkY)) < 1) {
						break;
					}
//					System.out.println(checkX+":"+checkY);
//					System.out.println(map.isPassable(checkX, checkY));
					if((checkX == attacker.getX() && checkY == attacker.getY()) || !map.isPassable(checkX, checkY)) {//檢查指定的座標是否是可移動的位置
						break;
					}
					moveX = checkX;
					moveY = checkY;
				}
				// System.out.println("傳送");
				L1Teleport.getInstance().doTeleport(L1PcInstance.this, moveX, moveY, this.getMapId());
			} else {
				// System.out.println("不傳送");
			}

		}
/*		S_SkillSound skill = new S_SkillSound(attacker.getId(), skill_effect);
		attacker.sendPackets(skill, false);
		Broadcaster.broadcastPacket(attacker, skill, false);
		skill.clear();*/

	}

	public void Cruel_attack_Long(L1Character attacker, int skill_effect) {
		// 新增槍兵
		if (attacker.get_Maelstrom()) {
			return;
		}
		int attacker_x = attacker.getX();
		int attacker_y = attacker.getY();
		int x = this.getX();
		int y = this.getY();
		int moveX = 0;
		int moveY = 0;
		int range = 3;
		if (attacker.isPassive(MJPassiveID.CRUEL_CONBICTION.toInt())) {
			range = 4;
		}
		int heading = MJCommons.calcheading(attacker_x, attacker_y, x, y);


		int Range = attacker.getLocation().getTileDistance(this.getX(), this.getY());
		boolean isRange = (this.getLocation().getTileLineDistance(new Point(attacker.getX(), attacker.getY())) <= range);
		L1Map map = L1WorldMap.getInstance().getMap(this.getMapId());
		boolean firstFail = false;
		if (isRange) {
			int checkX = x, checkY = y;
			if (Math.abs(attacker_x - x) + Math.abs(attacker_y - y) < range) {
				while(true){
					if(heading == -1)break;// 所有方向都失敗時
					heading = MJCommons.calcheading(attacker_x, attacker_y, checkX, checkY);
					switch (heading) {

						case 0:checkY--;break;
						case 1:checkX++;checkY--;break;
						case 2:checkX++;break;
						case 3:checkX++;checkY++;break;
						case 4:checkY++;break;
						case 5:checkX--;checkY++;break;
						case 6:checkX--;break;
						case 7:checkX--;checkY--;break;
						default:
							break;
					}
					int i = 0;
					if (attacker.getLocation().getTileLineDistance(new Point(checkX, checkY)) > range) {
						break;
					}
					if(!map.isPassable(checkX, checkY)) {// 檢查指定的座標是否是可移動的位置
						break;
					}
					if (i == 0) {
						if (!map.isArrowPassable(checkX, checkY, attacker.targetDirection(checkX, checkY))) {
							break;
						}
					}

					moveX = checkX;
					moveY = checkY;

//					System.out.println(moveX+":"+moveY);
				}
				if (moveX == 0 || moveY == 0) {
					moveX = x;
					moveY = y;
				}

				L1Teleport.getInstance().doTeleport(L1PcInstance.this, moveX, moveY, this.getMapId());
			} else {
				// System.out.println("不傳送");
			}
		}

/*		S_SkillSound skill = new S_SkillSound(attacker.getId(), skill_effect);
		attacker.sendPackets(skill, false);
		Broadcaster.broadcastPacket(attacker, skill, false);
		skill.clear();*/

	}

	// TODO 潘特拉/暗影步
	public void Panthera_attack(L1Character target, int skill_effect,
	                            int attack_effect, int skillId) {
		if (this.get_Maelstrom()) {
			return;
		}
		int x = target.getX();
		int y = target.getY();

		if (this.getX() == target.getX() && this.getY() == target.getY()) {
			x = target.getX();
			y = target.getY();
		} else {
			switch (this.targetDirection(x, y)) {
				case 7:
					x += 1;
					y += 1;
					break;
				case 6:
					x += 1;
					break;
				case 5:
					x += 1;
					y -= 1;
					break;
				case 4:
					y -= 1;
					break;
				case 3:
					x -= 1;
					y -= 1;
					break;
				case 2:
					x -= 1;
					break;
				case 1:
					x -= 1;
					y += 1;
					break;
				case 0:
					y += 1;
					break;
			}
			if (!(this.getX() == x && this.getY() == y)) {
				L1Map map = new L1Location(x, y, this.getMapId()).getMap();
				if (!(map.isInMap(x, y))) {
					return;// 檢查計算出的位置
				}
				if (!(skillId == L1SkillId.SHADOW_STEP && isPassive(MJPassiveID.SHADOW_STEP_CHASER
						.toInt())) && !map.isPassable(x, y)) {
					return;
				}
			}

		}
		// synchronized (pc) {
		// pc.setTeleport(true);
		// }

		if (skillId == L1SkillId.SHADOW_STEP
				&& isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())) {
			L1Map map = new L1Location(x, y, this.getMapId()).getMap();

			// System.out.println(diffX+" + "+ diffY);
			// map.setPassable(x, y, true);
			// getMap().setPassable(pc.getLocation(), true);
			set_MassTel(true);
			start_teleport(x, y, this.getMapId(), this.getHeading(),
					skill_effect, false, false);
			S_SkillSound skill = new S_SkillSound(getId(), skill_effect);
			sendPackets(skill, false);
			Broadcaster.broadcastPacket(this, skill, false);
			skill.clear();

			// map.setPassable(x, y, false);

			S_SkillSound attack = new S_SkillSound(getId(), attack_effect);
			sendPackets(attack, false);
			Broadcaster.broadcastPacket(this, attack, false);
			attack.clear();

		} else {
			L1Teleport.getInstance().doTeleport(L1PcInstance.this, x, y,
					this.getMapId());
			S_SkillSound skill = new S_SkillSound(getId(), skill_effect);
			sendPackets(skill, false);
			Broadcaster.broadcastPacket(this, skill, false);
			skill.clear();

			S_SkillSound attack = new S_SkillSound(getId(), attack_effect);
			sendPackets(attack, false);
			Broadcaster.broadcastPacket(this, attack, false);
			attack.clear();
		}
	}

	/** 移動技能 **/
	private int _moveSkillX, _moveSkillY;
	private short _moveSkillMapId;
	private byte _moveSkillHeading;

	public int getMoveSkillX() {
		return _moveSkillX;
	}

	public void setMoveSkillX(int i) {
		_moveSkillX = i;
	}

	public int getMoveSkillY() {
		return _moveSkillY;
	}

	public void setMoveSkillY(int i) {
		_moveSkillY = i;
	}

	public short getMoveSkillMapId() {
		return _moveSkillMapId;
	}

	public void setMoveSkillMapId(short i) {
		_moveSkillMapId = i;
	}

	public byte getMoveSkillHeading() {
		return _moveSkillHeading;
	}

	public void setMoveSkillHeading(byte i) {
		_moveSkillHeading = i;
	}

	private boolean teleport;

	public boolean get_teleport() {
		return teleport;
	}

	public void set_teleport(boolean b) {
		this.teleport = b;
		if (teleport) {
		}
	}

	private int teleport_x;

	public int get_teleport_x() {
		return teleport_x;
	}

	public void set_teleport_x(int i) {
		this.teleport_x = i;
	}

	private int teleport_y;

	public int get_teleport_y() {
		return teleport_y;
	}

	public void set_teleport_y(int i) {
		this.teleport_y = i;
	}

	private int teleport_map;

	public int get_teleport_map() {
		return teleport_map;
	}

	public void set_teleport_map(int i) {
		this.teleport_map = i;
	}

	// -- 有關機器人傳送
	private int teleport_count;

	public int get_teleport_count() {
		return teleport_count;
	}

	public void set_teleport_count(int i) {
		this.teleport_count = i;
	}

	public SkillData get_skill() {
		return skill_data;
	}

	public boolean isTwoLogin(L1PcInstance c) {// 重複檢查變更
		boolean bool = false;
		for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
			if (target.noPlayerCK)
				continue;

			if (c.getId() != target.getId() && !target.isPrivateShop()) {
				if (c.getNetConnection()
						.getAccountName()
						.equalsIgnoreCase(
								target.getNetConnection().getAccountName())) {
					bool = true;
					break;
				}
			}
		}
		return bool;
	}

	private int _attackDelay;

	public int getAttackDelay() {
		return this._attackDelay;
	}

	public void setAttackDelay(int i) {
		this._attackDelay = i;
	}

	private int _attackSpeed;

	public int getAttackSpeed() {
		return this._attackSpeed;
	}

	public void setAttackSpeed() {
		int status = 0;

		L1PolyMorph poly = PolyTable.getInstance().find(getCurrentSpriteId());

		int level = getLevel();
		if (poly != null) {
			level = Math.max(level, poly.getMinLevel());
		}
		status = SpriteInformationLoader.levelToIndex(level,
				getCurrentSpriteId());
		int armorGfx = getPolyIdByEquip();
		if (armorGfx == 11365) {
			status = Math.max(status, 9);
		}

		if (this._attackSpeed != status) {
			sendPackets(new S_CharSpeedUpdate(getId(), status, getLevel()));
			broadcastPacket(new S_CharSpeedUpdate(getId(), status, getLevel()));
		}

		this._attackSpeed = status;
	}

	private int _polyIdByEquip = 0;

	public int getPolyIdByEquip() {
		return this._polyIdByEquip;
	}

	public void setPolyIdByEquip(int i) {
		this._polyIdByEquip = i;
	}

	private L1HateList _autoTargetList = new L1HateList();

	public L1HateList getAutoTargetList() {
		return _autoTargetList;
	}

	public void setAutoTargetList(L1HateList attackList) {
		this._autoTargetList = attackList;
	}

	public void addAutoTargetList(L1Character mon) {
		if (_autoTargetList.containsKey(mon)) {
			return;
		}
		_autoTargetList.add(mon, 0);
	}

	public void removeAutoTargetList(L1Character mon) {
		if (mon == null || !_autoTargetList.containsKey(mon))
			return;
		_autoTargetList.remove(mon);
	}

	private boolean _HUNTER_BLESS3;

	public void setHUNTER_BLESS3(boolean b) {
		_HUNTER_BLESS3 = b;
	}

	public boolean isHUNTER_BLESS3() {
		return _HUNTER_BLESS3;
	}

	private boolean _isAutoSetting;

	public boolean isAutoSetting() {
		return _isAutoSetting;
	}

	public void setAutoSetting(boolean b) {
		_isAutoSetting = b;
	}

	private int _autoPolyId;

	public int getAutoPolyID() {
		return _autoPolyId;
	}

	public void setAutoPolyID(int i) {
		_autoPolyId = i;
	}

	private int _autoLocX;

	public int getAutoLocX() {
		return _autoLocX;
	}

	public void setAutoLocX(int i) {
		_autoLocX = i;
	}

	private int _autoLocY;

	public int getAutoLocY() {
		return _autoLocY;
	}

	public void setAutoLocY(int i) {
		_autoLocY = i;
	}

	private L1Character _autoTarget;

	public L1Character getAutoTarget() {
		return _autoTarget;
	}

	public void setAutoTarget(L1Character mon) {
		_autoTarget = mon;
	}

	private L1Astar _autoAStar = new L1Astar();

	public L1Astar getAutoAstar() {
		return _autoAStar;
	}

	public void setAutoAstar(L1Astar a) {
		_autoAStar = a;
	}

	private int[][] _autoPath = new int[300][2];

	public int[][] getAutoPath() {
		return _autoPath;
	}

	public void setAutoPath(int[][] i) {
		_autoPath = i;
	}

	private int _autoMoveCount = CommonUtil.random(50, 200);

	public int getAutoMoveCount() {
		return _autoMoveCount;
	}

	public void setAutoMoveCount(int i) {
		_autoMoveCount = i;
	}

	private long _autoSkillDelay;

	public long getAutoSkillDelay() {
		return _autoSkillDelay;
	}

	public void setAutoSkillDelay(long i) {
		_autoSkillDelay = i;
	}

	private int _autoStatus;

	public int getAutoStatus() {
		return _autoStatus;
	}

	public void setAutoStatus(int i) {
		_autoStatus = i;
	}

	private L1Node _autoTail;

	public L1Node getAutoTail() {
		return _autoTail;
	}

	public void setAutoTail(L1Node node) {
		_autoTail = node;
	}

	private boolean _autoPathFirst;

	public boolean isAutoPathFirst() {
		return _autoPathFirst;
	}

	public void setAutoPathFirst(boolean a) {
		_autoPathFirst = a;
	}

	public int _autoCurrentPath;

	private int _autoPotion;

	public int getAutoPotion() {
		return _autoPotion;
	}

	public void setAutoPotion(int i) {
		_autoPotion = i;
	}

	private long _autoTimeAttack;

	public long getAutoTimeAttack() {
		return _autoTimeAttack;
	}

	public void setAutoTimeAttack(long time) {
		_autoTimeAttack = time;
	}

	private long _autoTimeMove;

	public long getAutoTimeMove() {
		return _autoTimeMove;
	}

	public void setAutoTimeMove(long time) {
		_autoTimeMove = time;
	}

	private boolean _autoDead;

	public boolean isAutoDead() {
		return _autoDead;
	}

	public void setAutoDead(boolean b) {
		_autoDead = b;
	}

	private int _autoDeadTime = 5;

	public int getAutoDeadTime() {
		return _autoDeadTime;
	}

	public void setAutoDeadTime(int i) {
		_autoDeadTime = i;
	}

	private int _autoTeleportTime;

	public int getAutoTeleportTime() {
		return _autoTeleportTime;
	}

	public void setAutoTeleportime(int i) {
		_autoTeleportTime = i;
	}

	private int _autoRange;

	public int getAutoRange() {
		return _autoRange;
	}

	public void setAutoRange(int i) {
		_autoRange = i;
	}

	private long _autoAiTime;

	public long getAutoAiTime() {
		return _autoAiTime;
	}

	public void setAutoAiTime(long l) {
		_autoAiTime = l;
	}

	private int _autoMapId;

	public int getAutoMapId() {
		return _autoMapId;
	}

	public void setAutoMapId(int i) {
		_autoMapId = i;
	}

	/** 插槽更換 **/
	private List<Integer> _slotItemOne = new ArrayList<Integer>();
	private List<Integer> _slotItemTwo = new ArrayList<Integer>();
	private List<Integer> _slotItemThree = new ArrayList<Integer>();
	private List<Integer> _slotItemFour = new ArrayList<Integer>();
	private HashMap<Integer, L1InventorySlot> _slotcolor = new HashMap<Integer, L1InventorySlot>();

	public void addslotsetting(int slotNum, int slorcolr, String Slotname) {
		L1InventorySlot slot = get_slot_info(slotNum);
		if (slot == null) {
			slot = new L1InventorySlot();
		}
		slot.set_Color(slorcolr);
		slot.set_Slotname(Slotname);
		_slotcolor.put(slotNum, slot);
	}

	public HashMap<Integer, L1InventorySlot> get_slot_color() {
		return _slotcolor;
	}

	public L1InventorySlot get_slot_info(int slottype) {
		return _slotcolor.get(slottype);
	}

	public void addSlotItem(int slotNum, int itemobjid, boolean flag) {
		if (flag) {
			if (slotNum == 0) {
				_slotItemOne.clear();
				for (L1ItemInstance item : getInventory().getItems()) {
					if (item.isEquipped()) {
						_slotItemOne.add(item.getId());
					}
				}
			} else if (slotNum == 1) {
				_slotItemTwo.clear();
				for (L1ItemInstance item : getInventory().getItems()) {
					if (item.isEquipped()) {
						_slotItemTwo.add(item.getId());
					}
				}
			} else if (slotNum == 2) {
				_slotItemThree.clear();
				for (L1ItemInstance item : getInventory().getItems()) {
					if (item.isEquipped()) {
						_slotItemThree.add(item.getId());
					}
				}
			} else if (slotNum == 3) {
				_slotItemFour.clear();
				for (L1ItemInstance item : getInventory().getItems()) {
					if (item.isEquipped()) {
						_slotItemFour.add(item.getId());
					}
				}
			}
		} else {
			if (slotNum == 0) {
				_slotItemOne.add(itemobjid);
			} else if (slotNum == 1) {
				_slotItemTwo.add(itemobjid);
			} else if (slotNum == 2) {
				_slotItemThree.add(itemobjid);
			} else if (slotNum == 3) {
				_slotItemFour.add(itemobjid);
			}
		}
	}

	public List<Integer> getSlotItems(int slotNum) {
		if (slotNum == 0) {
			return _slotItemOne;
		} else if (slotNum == 1) {
			return _slotItemTwo;
		} else if (slotNum == 2) {
			return _slotItemThree;
		} else if (slotNum == 3) {
			return _slotItemFour;
		}
		return null;
	}

	public void getChangeSlot(int slotNum) {
		if (slotNum == 0) {
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (!_slotItemOne.contains(item.getId())) {
					if (item.isEquipped()) {
						if (item.getItemId() == 20077)
							this.beginInvisTimer();
						else if (item.getItemId() == 20062)
							this.beginInvisTimer();
						else if (item.getItemId() == 120077)
							this.beginInvisTimer();

						getInventory().setEquipped(item, false);
					} else {

					}
				}
			}
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (_slotItemOne.contains(item.getId())) {
					if (item.isEquipped()) {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(
									getCurrentSpriteId(), item.getItem()
											.getType())) {
								getInventory().setEquipped(item, false);
							}
						}
					} else {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(
									getCurrentSpriteId(), item.getItem()
											.getType())) {
								continue;
							}
						}
						getInventory().setEquipped(item, true);
					}
				}
			}
		} else if (slotNum == 1) {
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (!_slotItemTwo.contains(item.getId())) {
					if (item.isEquipped()) {
						if (item.getItemId() == 20077)
							this.beginInvisTimer();
						else if (item.getItemId() == 20062)
							this.beginInvisTimer();
						else if (item.getItemId() == 120077)
							this.beginInvisTimer();

						getInventory().setEquipped(item, false);
					} else {

					}
				}
			}
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (_slotItemTwo.contains(item.getId())) {
					if (item.isEquipped()) {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(
									getCurrentSpriteId(), item.getItem()
											.getType())) {
								getInventory().setEquipped(item, false);
							}
						}
					} else {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(
									getCurrentSpriteId(), item.getItem()
											.getType())) {
								continue;
							}
						}
						getInventory().setEquipped(item, true);
					}
				}
			}
		} else if (slotNum == 2) {
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (!_slotItemThree.contains(item.getId())) {
					if (item.isEquipped()) {
						if (item.getItemId() == 20077)
							this.beginInvisTimer();
						else if (item.getItemId() == 20062)
							this.beginInvisTimer();
						else if (item.getItemId() == 120077)
							this.beginInvisTimer();

						getInventory().setEquipped(item, false);
					} else {

					}
				}
			}
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (_slotItemThree.contains(item.getId())) {
					if (item.isEquipped()) {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(
									getCurrentSpriteId(), item.getItem()
											.getType())) {
								getInventory().setEquipped(item, false);
							}
						}
					} else {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(
									getCurrentSpriteId(), item.getItem()
											.getType())) {
								continue;
							}
						}
						getInventory().setEquipped(item, true);
					}
				}
			}
		} else if (slotNum == 3) {
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (!_slotItemFour.contains(item.getId())) {
					if (item.isEquipped()) {
						if (item.getItemId() == 20077)
							this.beginInvisTimer();
						else if (item.getItemId() == 20062)
							this.beginInvisTimer();
						else if (item.getItemId() == 120077)
							this.beginInvisTimer();

						getInventory().setEquipped(item, false);
					} else {

					}
				}
			}
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (_slotItemFour.contains(item.getId())) {
					if (item.isEquipped()) {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(
									getCurrentSpriteId(), item.getItem()
											.getType())) {
								getInventory().setEquipped(item, false);
							}
						}
					} else {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(
									getCurrentSpriteId(), item.getItem()
											.getType())) {
								continue;
							}
						}
						getInventory().setEquipped(item, true);
					}
				}
			}
		}
		this.sendPackets(new S_SPMR(this));
		this.sendPackets(new S_OwnCharStatus(this));
		this.sendPackets(new S_OwnCharStatus2(this));
		this.sendPackets(new S_OwnCharAttrDef(this));
	}

	private int slotNumber = 0;

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int i) {
		slotNumber = i;
	}

	private long _buffTime;

	public long getBuffTime() {
		return _buffTime;
	}

	public void setBuffTime(long i) {
		_buffTime = i;
	}

	private boolean _isOneTel = false;

	public void setOneTel(boolean a) {
		_isOneTel = a;
	}

	public boolean isOneTel() {
		return _isOneTel;
	}

	private int _robotAIType;

	public int getRobotAIType() {
		return _robotAIType;
	}

	public void setRobotAIType(int i) {
		_robotAIType = i;
	}

	private int _robotPattern = -1;

	public int getRobotPattern() {
		return _robotPattern;
	}

	public void setRobotPattern(int i) {
		_robotPattern = i;
	}

	private long _robotStopTime;

	public long getRobotStopTime() {
		return _robotStopTime;
	}

	public void setRobotStopTime(long time) {
		_robotStopTime = time;
	}

	private long _robotPotionTime;

	public long getRobotPotionTime() {
		return _robotPotionTime;
	}

	public void setRobotPotionTime(long time) {
		_robotPotionTime = time;
	}

	private int attackRange;

	public void setAttackRang(int attackRange) {
		this.attackRange = attackRange;
	}

	public int getAttackRang() {
		return attackRange;
	}

	public boolean isCastle;

	private boolean _isPolyRingMaster = false;

	public boolean isPolyRingMaster() {
		return _isPolyRingMaster;
	}

	public void setPolyRingMaster(boolean flag) {
		_isPolyRingMaster = flag;
	}

	private boolean _isPolyRingMaster2 = false;

	public boolean isPolyRingMaster2() {
		return _isPolyRingMaster2;
	}

	public void setPolyRingMaster2(boolean flag) {
		_isPolyRingMaster2 = flag;
	}

	private int _elfAttrInitCount;

	public int getElfAttrInitCount() {
		return _elfAttrInitCount;
	}

	public void setElfAttrInitCount(int i) {
		if (i >= 20) {
			i = 20;
		}
		_elfAttrInitCount = i;
	}

	public void addElfAttrInitCount(int i) {
		int count = _elfAttrInitCount + i;
		if (count >= 20) {
			count = 20;
		}
		_elfAttrInitCount = count;
	}

	private int _lastImmuneLevel = 0;

	// TODO 免疫傷害減少百分比
	public double getImmuneReduction() {

		if (_lastImmuneLevel < 36)
			return 0;
		else if (_lastImmuneLevel >= Config.MagicAdSetting_Wizard.IMMUNELEVEL) // 95級
			// 以上
			return Config.MagicAdSetting_Wizard.IMMUNEDMG;
		else if (_lastImmuneLevel >= Config.MagicAdSetting_Wizard.IMMUNELEVEL1) // 93級
			// 以上
			return Config.MagicAdSetting_Wizard.IMMUNEDMG1;
		else if (_lastImmuneLevel >= Config.MagicAdSetting_Wizard.IMMUNELEVEL2) // 91級
			// 以上
			return Config.MagicAdSetting_Wizard.IMMUNEDMG2;
		else if (_lastImmuneLevel >= Config.MagicAdSetting_Wizard.IMMUNELEVEL3) // 89級
			// 以上
			return Config.MagicAdSetting_Wizard.IMMUNEDMG3;
		else if (_lastImmuneLevel >= Config.MagicAdSetting_Wizard.IMMUNELEVEL4) // 87級
			// 以上
			return Config.MagicAdSetting_Wizard.IMMUNEDMG4;
			// else if (_lastImmuneLevel >= Config.MagicAdSetting.IMMUNELEVEL5)
			// //85렙 이상
			// return Config.MagicAdSetting.IMMUNEDMG5;
			// else if (_lastImmuneLevel >= Config.MagicAdSetting.IMMUNELEVEL6)
			// return Config.MagicAdSetting.IMMUNEDMG6;
		else
			return Config.MagicAdSetting_Wizard.IMMUNEDMG7;
	}

	public int getLastImmuneLevel() {
		return _lastImmuneLevel;
	}

	public void setLastImmuneLevel(int i) {
		_lastImmuneLevel = i;
	}

	private static final long _valaRegenTime = 16000;
	private boolean _isValakasBlessing = false;
	private ValakasBlessing _vBlessing;

	public void startValaBlessing() {
		if (!_isValakasBlessing) {
			_isValakasBlessing = true;
			_vBlessing = new ValakasBlessing(this);
			if (_blessingTimer != null) {
				_blessingTimer.cancel();
				_blessingTimer = null;
			}
			_blessingTimer = new Timer(true);
			_blessingTimer.scheduleAtFixedRate(_vBlessing, _valaRegenTime,
					_valaRegenTime);
		}
	}

	public void stopValaBlessing() {
		if (_isValakasBlessing) {
			if (_vBlessing != null) {
				_vBlessing.cancel();
				_vBlessing = null;
			}
			_isValakasBlessing = false;
		}
	}

	public boolean isValakasBlessing() {
		return _isValakasBlessing;
	}

	private boolean _isBossNotify = true;

	public boolean isBossNotify() {
		return _isBossNotify;
	}

	public void setBossNotify(boolean b) {
		_isBossNotify = b;
	}

	/** 2016.11.26 MJ 應用中心 LFC **/
	/** 表示實例空間中的某種狀態 **/
	private InstStatus _instStatus = InstStatus.INST_USERSTATUS_NONE;

	public InstStatus getInstStatus() {
		return _instStatus;
	}

	public void setInstStatus(InstStatus status) {
		_instStatus = status;
	}

	/** 積累接收到的損害 **/
	private int _dmgLfc;

	public int getDamageFromLfc() {
		return _dmgLfc;
	}

	public void addDamageFromLfc(int i) {
		_dmgLfc = +i;
	}

	public void setDamageFromLfc(int i) {
		_dmgLfc = i;
	}

	/** 2016.11.26 MJ 應用中心 LFC **/

	/** 2016.12.01 MJ 應用中心 LFC **/
	private int _findMerchantId = 0;

	public int getFindMerchantId() {
		return _findMerchantId;
	}

	public void setFindMerchantId(int i) {
		_findMerchantId = i;
	}

	/** 2016.12.01 MJ 應用中心 LFC **/

	private boolean _isValakasProduct;

	public boolean isValakasProduct() {
		return _isValakasProduct;
	}

	public void setValakasProduct(boolean b) {
		_isValakasProduct = b;
	}

	private MJExpAmplifier _expAmplifier;

	public MJExpAmplifier getExpAmplifier() {
		return _expAmplifier;
	}

	public void setExpAmplifier(MJExpAmplifier amp) {
		if (_expAmplifier != null) {
			if (_expAmplifier.equals(amp))
				return;

			killSkillEffectTimer(L1SkillId.EINHASAD_AMPLIFIER);
			sendPackets(S_InventoryIcon.icoEnd(L1SkillId.EINHASAD_AMPLIFIER));
		}
		_expAmplifier = amp;
		if (amp != null) {
			setSkillEffect(L1SkillId.EINHASAD_AMPLIFIER, -1);
			sendPackets(S_InventoryIcon.iconNewUnLimitAndPriority(0,
					L1SkillId.EINHASAD_AMPLIFIER, amp.getMessageId(), true));
		}
	}

	private ProtoOutputStream _wrdPck;

	public ProtoOutputStream getWorldObject() {
		return _wrdPck;
	}

	public void setWorldObject(ProtoOutputStream wrdPck) {
		_wrdPck = wrdPck;
	}

	/*
	 * private S_WorldPutObject _wrdPck;
	 *
	 * public S_WorldPutObject getWorldObject() { return _wrdPck; }
	 *
	 * public void setWorldObject(S_WorldPutObject wrdPck) { _wrdPck = wrdPck; }
	 */

	/** kill death initialize temporary item instance. **/
	private L1ItemInstance _kdInitItem;

	public L1ItemInstance getKillDeathInitializeItem() {
		return _kdInitItem;
	}

	public void setKillDeathInitializeItem(L1ItemInstance item) {
		_kdInitItem = item;
	}

	public void RenewStat() {
		SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 1,
				this.getType());
		SC_STAT_RENEWAL_BASESTAT_INFO_RES.send_bonus_stat(this, 25);
		SC_STAT_RENEWAL_BASESTAT_INFO_RES.send_bonus_stat(this, 35);
		SC_STAT_RENEWAL_BASESTAT_INFO_RES.send_bonus_stat(this, 45);
		SC_STAT_RENEWAL_BASESTAT_INFO_RES.send_bonus_stat(this, 55);
		SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
	}

	private boolean _isOutsideChat = true;

	public boolean isOutsideChat() {
		return _isOutsideChat;
	}

	public void setOutSideChat(boolean b) {
		_isOutsideChat = b;
	}

	private long _lastNpcClickMs = 0L;

	public long getLastNpcClickMs() {
		return _lastNpcClickMs;
	}

	public void setLastNpcClickMs(long l) {
		_lastNpcClickMs = l;
	}

	private int _charLevelBonus;

	public int getCharLevelBonus() {
		return _charLevelBonus;
	}

	public void setCharLevelBonus(int i) {
		_charLevelBonus = i;
	}

	public synchronized boolean onStat(String s) throws Exception {
		if (remainBonusStats() <= 0) {
			return false;
		}

		if (getOnlineStatus() != 1) { // 127屬性錯誤修復
			sendPackets(new S_Disconnect());
			return false;
		}

		if (s.toLowerCase().equals("str".toLowerCase())) {
			if (getAbility().getStr() < 50 || (getLevel() >= 90 && getAbility().getStr() < 60)) {
				getAbility().addStr((byte) 1); // STR屬性+1
				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this)); // 更新角色資訊
				sendPackets(new S_Weight(this)); // 更新重量資訊
				save(); // 保存角色信息到資料庫
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else if (s.toLowerCase().equals("dex".toLowerCase())) {
			if (getAbility().getDex() < 50
					|| (getLevel() >= 90 && getAbility().getDex() < 60)) {
				getAbility().addDex((byte) 1);
				resetBaseAc();

				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this));
				save();
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else if (s.toLowerCase().equals("con".toLowerCase())) {
			if (getAbility().getCon() < 50
					|| (getLevel() >= 90 && getAbility().getCon() < 60)) {
				getAbility().addCon((byte) 1);
				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this));
				sendPackets(new S_Weight(this));
				save();
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else if (s.toLowerCase().equals("int".toLowerCase())) {
			if (getAbility().getInt() < 50
					|| (getLevel() >= 90 && getAbility().getInt() < 60)) {
				getAbility().addInt((byte) 1);
				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this));
				save();
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else if (s.toLowerCase().equals("wis".toLowerCase())) {
			if (getAbility().getWis() < 50
					|| (getLevel() >= 90 && getAbility().getWis() < 60)) {
				getAbility().addWis((byte) 1);
				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this));
				save();
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else if (s.toLowerCase().equals("cha".toLowerCase())) {
			if (getAbility().getCha() < 50
					|| (getLevel() >= 90 && getAbility().getCha() < 60)) {
				getAbility().addCha((byte) 1);
				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this));
				save();
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else if (s.toLowerCase().equals("")) {
			sendPackets(new S_ServerMessage(480));
		} else
			throw new Exception(s);

		checkStatus();
		sendBonusStats();
		return true;
	}

	private L1ItemInstance _NameInstance;

	public L1ItemInstance getNameInstance() {
		return _NameInstance;
	}

	public void setNameInstance(L1ItemInstance item) {
		_NameInstance = item;
	}

	private int _magicDodgeProb = 0;

	public int getMagicDodgeProbability() {
		return _magicDodgeProb;
	}

	public void setMagicDodgeProbability(int i) {
		_magicDodgeProb = i;
	}

	public void addMagicDodgeProbability(int i) {
		_magicDodgeProb += i;

		sendPackets(
				new S_ACTION_UI2(S_ACTION_UI2.MAGICEVASION, _magicDodgeProb),
				true);
	}

	private int _temporaryItemObjectId = -1;

	public int getTemporaryItemObjectId() {
		return _temporaryItemObjectId;
	}

	public void setTemporaryItemObjectId(int i) {
		_temporaryItemObjectId = i;
	}

	public void clearTemporaryItemObjectId() {
		_temporaryItemObjectId = -1;
	}

	private static int _instanceType = -1;

	@Override
	public int getL1Type() {
		return _instanceType == -1 ? _instanceType = super.getL1Type()
				| MJL1Type.L1TYPE_PC : _instanceType;
	}

	@Override
	public long getCurrentSpriteInterval(EActionCodes actionCode) {
		return (long) _currentSpriteInfo.getInterval(this, actionCode);
	}

	@Override
	public void sendShape(int poly) {
		S_ChangeShape shape = new S_ChangeShape(getId(), poly,
				getCurrentWeapon());
		sendPackets(shape, false);
		broadcastPacket(shape);
	}

	public void offFishing() {
		if (isFishing()) {
			try {
				setFishing(false);
				setFishingTime(0);
				setFishingReady(false);
				sendPackets(new S_CharVisualUpdate(this));
				Broadcaster.broadcastPacket(this, new S_CharVisualUpdate(this));
				FishingTimeController.getInstance().removeMember(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private CharacterActionExecutor _actionExecutor;

	public void registerActionHandler(int idx, AbstractActionHandler handler) {
		if (_actionExecutor == null)
			_actionExecutor = CharacterActionExecutor.execute(this);
		_actionExecutor.register(idx, handler);
	}

	/*
	 * public CallbackActionHandler callbackActionHandler() { if
	 * (_actionExecutor == null) { _actionExecutor =
	 * CharacterActionExecutor.execute(this); } return
	 * _actionExecutor.callbackHandler(); }
	 */

	public void unreigsterActionHandler(int idx) {
		if (_actionExecutor != null)
			_actionExecutor.unregister(idx);
	}

	public boolean hasAction(int idx) {
		return _actionExecutor != null ? _actionExecutor.hasAction(idx) : false;
	}

	private ServerBasePacket _tempEffect;

	public void setTemporaryEffect(ServerBasePacket pck) {
		clearTemporaryEffect();
		_tempEffect = pck;
	}

	public ServerBasePacket getTemporaryEffect() {
		return _tempEffect;
	}

	public void clearTemporaryEffect() {
		if (_tempEffect != null) {
			_tempEffect.clear();
			_tempEffect = null;
		}
	}

	public long lastSpellUseMillis = 0L;
	public int lastSpellUsePending = 0;

	public int lastSpeedUsePending = 0;

	private SC_ATTENDANCE_USER_DATA_EXTEND _attendance_data;

	public void setAttendanceData(SC_ATTENDANCE_USER_DATA_EXTEND attendance_data) {
		_attendance_data = attendance_data;
	}

	public SC_ATTENDANCE_USER_DATA_EXTEND getAttendanceData() {
		return _attendance_data;
	}

	// 是否使用目標效果 _isOnTargetEffect
	private boolean _isOnTargetEffect = Config.ServerAdSetting.TARGETGFX;

	public boolean isOnTargetEffect() {
		return _isOnTargetEffect;
	}

	public void setOnTargetEffect(boolean b) {
		_isOnTargetEffect = b;
	}

	private Timestamp _lastTopBless;

	public void setLastTopBless(Timestamp ts) {
		_lastTopBless = ts;
	}

	public Timestamp getLastTopBless() {
		return _lastTopBless;
	}

	private BQSCharacterData _bqsData;

	public BQSCharacterData getBqs() {
		return _bqsData;
	}

	public void setBqs(BQSCharacterData bqs) {
		_bqsData = bqs;
	}

	private MJEPcStatus _instance_status = MJEPcStatus.NONE;
	private int _current_combat_id = 0;
	private int _current_combat_team_id = -1;

	public void set_instance_status(MJEPcStatus instance_status) {
		_instance_status = instance_status;
	}

	public MJEPcStatus get_instance_status() {
		return _instance_status;
	}

	public boolean is_none() {
		return _instance_status.equals(MJEPcStatus.NONE);
	}

	public boolean is_world() {
		return _instance_status.equals(MJEPcStatus.WORLD);
	}

	public boolean is_combat_field() {
		return _instance_status.equals(MJEPcStatus.COMBAT_FIELD);
	}

	public void set_current_combat_id(int current_combat_id) {
		_current_combat_id = current_combat_id;
	}

	public int get_current_combat_id() {
		return _current_combat_id;
	}

	public void set_current_combat_team_id(int current_combat_team_id) {
		_current_combat_team_id = current_combat_team_id;
	}

	public int get_current_combat_team_id() {
		return _current_combat_team_id;
	}

	private boolean _is_non_action = false;

	public boolean is_non_action() {
		return _is_non_action;
	}

	public void set_is_non_action(boolean b) {
		_is_non_action = b;
	}

	private int _mark_status = 0;

	public void set_mark_status(int mark_status) {
		_mark_status = mark_status;
	}

	public int get_mark_status() {
		return _mark_status;
	}

	private MJCaptcha _captcha;

	public MJCaptcha get_captcha() {
		return _captcha;
	}

	public MJCaptcha create_captcha() {
		return (_captcha = MJCaptcha.newInstance(getId()));
	}

	private int _lateral_damage;
	private int _lateral_reduction;
	private int _lateral_magic_rate;

	public void set_lateral_damage(int lateral_damage) {
		_lateral_damage = lateral_damage;
	}

	public void add_lateral_damage(int lateral_damage) {
		_lateral_damage += lateral_damage;
	}

	public int get_lateral_damage() {
		return _lateral_damage;
	}

	public void set_lateral_reduction(int lateral_reduction) {
		_lateral_reduction = lateral_reduction;
	}

	public void add_lateral_reduction(int lateral_reduction) {
		_lateral_reduction += lateral_reduction;
	}

	public int get_lateral_reduction() {
		return _lateral_reduction;
	}

	public void set_lateral_magic_rate(int lateral_magic_rate) {
		_lateral_magic_rate = lateral_magic_rate;
	}

	public void add_lateral_magic_rate(int lateral_magic_rate) {
		_lateral_magic_rate += lateral_magic_rate;
	}

	public int get_lateral_magic_rate() {
		return _lateral_magic_rate;
	}

	public void load_lateral_status() {
		Selector.exec("select * from tb_lateral_status where character_id=?",
				new SelectorHandler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, getId());
					}

					@Override
					public void result(ResultSet rs) throws Exception {
						while (rs.next()) {
							add_lateral_damage(rs.getInt("lateral_damage"));
							add_lateral_reduction(rs
									.getInt("lateral_reduction"));
							add_lateral_magic_rate(rs
									.getInt("lateral_magic_rate"));
						}
					}
				});
	}

	public void delete_lateral_status() {
		set_lateral_damage(0);
		set_lateral_reduction(0);
		set_lateral_magic_rate(0);
		Updator.exec("delete from tb_lateral_status where character_id=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, getId());
					}
				});
	}

	public void update_lateral_status() {
		Updator.exec(
				"insert into tb_lateral_status set character_id=?, lateral_damage=?, lateral_reduction=?, lateral_magic_rate=? on duplicate key update  lateral_damage=?, lateral_reduction=?, lateral_magic_rate=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, getId());
						pstm.setInt(++idx, get_lateral_damage());
						pstm.setInt(++idx, get_lateral_reduction());
						pstm.setInt(++idx, get_lateral_magic_rate());
						pstm.setInt(++idx, get_lateral_damage());
						pstm.setInt(++idx, get_lateral_reduction());
						pstm.setInt(++idx, get_lateral_magic_rate());
					}
				});
	}

	private DungeonTimeUserInformation _dtInfo = DungeonTimeUserInformation
			.newInstance();

	public DungeonTimeUserInformation get_dungeon_information() {
		return _dtInfo;
	}

	public DungeonTimeProgress<?> get_progress(DungeonTimeInformation dtInfo) {
		return _dtInfo.get_progress(dtInfo);
	}

	public void dec_dungeon_progress(DungeonTimeInformation dtInfo) {
		_dtInfo.dec_dungeon_progress(this, dtInfo);
	}

	public Collection<DungeonTimeProgress<?>> get_character_progresses() {
		return _dtInfo.get_character_progresses().values();
	}

	public Collection<DungeonTimeProgress<?>> get_account_progresses() {
		return _dtInfo.get_account_progresses().values();
	}

	public void put_dungeon_progress(int timer_id, AccountTimeProgress progress) {
		_dtInfo.put_dungeon_progress(timer_id, progress);
	}

	public void put_dungeon_progress(int timer_id,
	                                 CharacterTimeProgress progress) {
		_dtInfo.put_dungeon_progress(timer_id, progress);
	}

	public void send_dungeon_progress(DungeonTimeInformation dtInfo) {
		send_dungeon_progress(dtInfo, true);
	}

	public void send_dungeon_progress(DungeonTimeInformation dtInfo,
	                                  boolean send) {
		_dtInfo.send_dungeon_progress(this, dtInfo, send);
	}

	public void initialize_dungeon_progress() {
		_dtInfo.initialize();
	}

	private S_ItemExSelectPacket _select_item;

	public void on_select_item(S_ItemExSelectPacket pck) {
		final int id = pck.get_id();
		_select_item = pck;
		sendPackets(pck);
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				S_ItemExSelectPacket pck = _select_item;
				if (pck != null) {
					if (pck.get_id() == id) {
						_select_item = null;
						pck.dispose();
					}
				}
				return new L1PcInstance[0];
			}
		}, 20000L);
	}

	public S_ItemExSelectPacket get_select_item() {
		S_ItemExSelectPacket pck = _select_item;
		_select_item = null;
		return pck;
	}

	public boolean is_ranking_buff() {
/*		return hasSkillEffect(L1SkillId.RANK_BUFF_1)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_2)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_3)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_4)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_5)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_6)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_7)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_8)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_9)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_10_STR)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_10_DEX)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_10_INT)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_11_STR)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_11_DEX)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_11_INT);*/
		return hasSkillEffect(L1SkillId.RANK_BUFF_1)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_2)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_3)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_4)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_5_STR)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_5_DEX)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_5_INT)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_6_STR)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_6_DEX)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_6_INT)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_7_STR)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_7_DEX)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_7_INT)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_8_STR)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_8_DEX)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_8_INT);

	}

	public boolean is_top_ranker() {
		return hasSkillEffect(L1SkillId.RANK_BUFF_6_STR)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_6_DEX)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_6_INT)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_7_STR)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_7_DEX)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_7_INT)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_8_STR)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_8_DEX)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_8_INT);
	}

	public boolean isPacketSendOK() {
		if (getNetConnection() == null || getAccount() == null)
			return false;
		boolean is_world = false;
		if (!is_world)
			return false;
		if (getAccountName().equals("") || getAccountName().equals("人工智能"))
			return false;
		if (isUnmannedShop())	//if (is無人商店())
			return false;
		if (isPrivateShop())
			return false;
		if (getAI() != null)
			return false;
		if (getOnlineStatus() == 0)
			return false;

		return true;
	}

	/** 角色資訊自動保存系統 **/
	private long lastSavedTime = System.currentTimeMillis();
	private long lastSavedTime_inventory = System.currentTimeMillis();
	private long lastSavedTime_exp = System.currentTimeMillis();
	private long lastSavedTime_Fatigue = System.currentTimeMillis();

	public long getlastSavedTime() {
		return lastSavedTime;
	}

	public long getlastSavedTime_inventory() {
		return lastSavedTime_inventory;
	}

	public long getlastSavedTime_exp() {
		return lastSavedTime_exp;
	}

	public long getlastSavedTime_Fatigue() {
		return lastSavedTime_Fatigue;
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

	/** 角色資訊自動保存系統 **/

	/** 樂透系統 **/
	private int _lotto;

	public int getLotto() {
		return _lotto;
	}

	public void setLotto(int i) {
		_lotto = i;
	}

	/** 樂透系統 **/

	private long _PostDelay;

	public long getPostDelay() {
		return _PostDelay;
	}

	public void setPostDelay(long i) {
		_PostDelay = i;
	}

	private HashMap<Integer, MJPassiveInfo> m_passives;

	public void addPassive(MJPassiveInfo pInfo) {
		if (m_passives == null) {
			m_passives = new HashMap<Integer, MJPassiveInfo>();
		}
		m_passives.put(pInfo.getPassiveId(), pInfo);
		if (pInfo.getPassiveId() == MJPassiveID.AURA_PASSIVE.toInt()) {
			if (_aura != null) {
				_aura.auraStop();
				_aura = null;
			}
			_aura = new L1Aura(L1PcInstance.this.getName());
			GeneralThreadPool.getInstance().schedule(_aura, 5000);
		}
	}

	public ArrayList<MJPassiveInfo> getPassives() {
		if (m_passives == null)
			return null;
		return new ArrayList<MJPassiveInfo>(m_passives.values());
	}

	public MJPassiveInfo getPassive(int passiveId) {
		if (m_passives == null)
			return null;
		return m_passives.get(passiveId);
	}

	public MJPassiveInfo delPassive(int passiveId) {
		if (m_passives == null)
			return null;
		return m_passives.remove(passiveId);
	}

	@Override
	public boolean isPassive(int passiveId) {
		return getPassive(passiveId) != null;
	}

	public boolean isAutoTreeple = false;

	// TODO 血盟 buff 重新更新 2017-11-12
	private int _ClanBuffMap = 0;

	public int getClanBuffMap() {
		return _ClanBuffMap;
	}

	public void setClanBuffMap(int i) {
		_ClanBuffMap = i;
	}

	private long _lastMoveActionMillis;

	public void setLastMoveActionMillis(long lastMoveActionMillis) {
		_lastMoveActionMillis = lastMoveActionMillis;
	}

	public long getLastMoveActionMillis() {
		return _lastMoveActionMillis;
	}

	/** 2017-11-06 阿因哈薩德祝福減少機率重製 **/
	private int _EinhasadBlessper = 0;

	public int getEinhasadBlessper() {
		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance()
				.getSpecialStat(this.getId());
		int special_point = 0;
		if (Info != null) {
			special_point = CalcStat.calcAinhasadStatFirst(Info.get_bless());
		}
		int sum = 0;
		int einblesser = _EinhasadBlessper;
		int pcCafe = isPcBuff() ? 10 : 0;
		sum = einblesser + special_point + pcCafe;

		return sum;
	}

	public void setEinhasadBlessper(int einhasadblessper) {
		_EinhasadBlessper = einhasadblessper;

		if (noPlayerCK || getAI() != null)
			return;

		SC_REST_EXP_INFO_NOTI.send(this);
	}

	public void addEinhasadBlessper(int i) {
		_EinhasadBlessper += i;

		if (noPlayerCK || getAI() != null)
			return;

		SC_REST_EXP_INFO_NOTI.send(this);
	}

	public MJEShiftObjectType get_shift_type() {
		return _netConnection == null ? MJEShiftObjectType.NONE
				: _netConnection.get_shift_type();
	}

	public void set_shift_type(MJEShiftObjectType shift_type) {
		if (_netConnection != null)
			_netConnection.set_shift_type(shift_type);
	}

	public boolean is_shift_client() {
		return _netConnection != null && _netConnection.is_shift_client();
	}

	public boolean is_shift_transfer() {
		return _netConnection == null ? false : _netConnection
				.is_shift_transfer();
	}

	public boolean is_shift_battle() {
		return _netConnection == null ? false : _netConnection
				.is_shift_battle();
	}

	private boolean m_is_ready_server_shift = false;

	public boolean is_ready_server_shift() {
		return m_is_ready_server_shift;
	}

	public void set_ready_server_shift(boolean is_ready_server_shift) {
		m_is_ready_server_shift = is_ready_server_shift;
	}

	private MJShiftBattleCharacterInfo m_battle_info;

	public MJShiftBattleCharacterInfo get_battle_info() {
		return m_battle_info;
	}

	public void set_battle_info(MJShiftBattleCharacterInfo bInfo) {
		m_battle_info = bInfo;
	}

	public String get_server_description() {
		return _netConnection == null ? "" : _netConnection
				.get_server_description();
	}

	public String get_server_identity() {
		return _netConnection == null ? "" : _netConnection
				.get_server_identity();
	}

	private MJCompanionInstance m_companion;

	public void set_companion(MJCompanionInstance companion) {
		if (companion != null)
			remove_companion();
		m_companion = companion;
	}

	public MJCompanionInstance get_companion() {
		return m_companion;
	}

	public void remove_companion() {
		if (m_companion != null) {
			m_companion.deleteMe();
			m_companion = null;
		}
	}

	@Override
	public void send_pink_name(int remain_seconds) {
		S_PinkName pnk = new S_PinkName(getId(), remain_seconds);
		sendPackets(pnk, false);
		if (!isGmInvis())
			broadcastPacket(pnk, false);
		pnk.clear();
	}

	private double m_item_exp_bonus = 0;

	public double get_item_exp_bonus() {
		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance()
				.getSpecialStat(this.getId());
		int special_point = 0;
		if (Info != null) {
			special_point = CalcStat.calcAinhasadStatSecond(Info.get_bless());
		}
		return m_item_exp_bonus + special_point;
	}

	public void set_item_exp_bonus(double item_exp_bonus) {
		m_item_exp_bonus = item_exp_bonus;
	}

	public void add_item_exp_bonus(double item_exp_bonus) {
		m_item_exp_bonus += item_exp_bonus;
		SC_EXP_BOOSTING_INFO_NOTI.send(this);
	}

	private boolean m_is_using_items = false;

	public void set_is_using_items(boolean is_using_items) {
		m_is_using_items = is_using_items;
	}

	public boolean get_is_using_items() {
		return m_is_using_items;
	}

	private boolean m_is_client_auto = false;

	public boolean get_is_client_auto() {
		return m_is_client_auto;
	}

	public void set_is_client_auto(boolean is_client_auto) {
		m_is_client_auto = is_client_auto;
	}

	/**
	 * 返回自動狩獵時掉落懲罰的適用與否。
	 *
	 * @return boolean 懲罰適用時返回 true，否則返回 false
	 **/
	public boolean hasClientAutoItemRatePenalty() {
		if (!get_is_client_auto()) {
			return false;
		}
		if (getInventory().checkItem(4100121)) {
			return false;
		} else if (getInventory().checkItem(4100529)
				&& this.getLevel() <= Config.ServerAdSetting.NEWPLAYERLEVELPROTECTION) {
			return false;
		}

		return true;
	}

	public void do_finish_client_auto(eReason reason) {
		set_is_client_auto(false);
		SC_FORCE_FINISH_PLAY_SUPPORT_NOTI noti = SC_FORCE_FINISH_PLAY_SUPPORT_NOTI.newInstance();
		noti.set_reason(reason);
		noti.set_remain_time(0);
		sendPackets(noti, MJEProtoMessages.SC_FORCE_FINISH_PLAY_SUPPORT_NOTI, true);
		// sendPackets(6871);
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
		sendPackets(ack, MJEProtoMessages.SC_FINISH_PLAY_SUPPORT_ACK, true);
		// sendPackets(6871);
		set_client_auto_type(0);
	}

	public void do_start_client_auto_ack(int type) {
		SC_START_PLAY_SUPPORT_ACK ack = SC_START_PLAY_SUPPORT_ACK.newInstance();
		ack.set_result(eResult.VALID);
		sendPackets(ack, MJEProtoMessages.SC_START_PLAY_SUPPORT_ACK);
		set_is_client_auto(true);
		set_client_auto_type(type);
/*		if(MJObjectEventProvider.provider().pcEventFactory().pcPssStartedKey() != null) {
			MJObjectEventProvider.provider().pcEventFactory().firePssStarted(this);
		}*/
	}

	public int _client_auto_type;

	public int get_client_auto_type() {
		return _client_auto_type;
	}

	public void set_client_auto_type(int i) {
		this._client_auto_type = i;
	}

	public boolean is_apply_tam() {
		return hasSkillEffect(L1SkillId.Tam_Fruit1)
				|| hasSkillEffect(L1SkillId.Tam_Fruit2)
				|| hasSkillEffect(L1SkillId.Tam_Fruit3)
				|| hasSkillEffect(L1SkillId.Tam_Fruit4)
				|| hasSkillEffect(L1SkillId.Tam_Fruit5);
	}

	public void on_tam_ended() {
		if (is_apply_tam())
			return;

		DungeonTimeInformation dtInfo = DungeonTimeInformationLoader
				.getInstance().from_timer_id(
						DungeonTimeInformationLoader.FISH_TIMER_ID);
		if (dtInfo == null) {
			// 設置釣魚計時器時解開註釋
			System.out.println(String.format(
					"無法找到地下城計時器。(釣魚場) 計時器 ID: %d",
					DungeonTimeInformationLoader.FISH_TIMER_ID));
			return;
		}

		DungeonTimeProgress<?> progress = get_progress(dtInfo);
		if (progress == null)
			return;

		progress.set_remain_seconds(dtInfo.get_amount_seconds());
		DungeonTimeProgressLoader.update(progress);
		if (5490 == getMapId()) {
			send_dungeon_progress(dtInfo);
		}
	}

	@Override
	public void setSkillEffect(int skillId, long timeMillis) {
		super.setSkillEffect(skillId, timeMillis);
		if (!L1SkillId.is_tam_buff(skillId))
			return;
		// if(pc.getCha()>= 12){
		// int ccduration = CalcStat.calcDecreaseCCDuration(pc.getCha()) +
		// CalcStat.calcPureDecreaseCCDuration(pc.getCha());
		// timeMillis -= ccduration * 100;
		// }
		DungeonTimeInformation dtInfo = DungeonTimeInformationLoader
				.getInstance().from_timer_id(
						DungeonTimeInformationLoader.FISH_TIMER_ID);
		if (dtInfo == null) {
			// 設置釣魚計時器時解除註釋
			System.out.println(String.format(
					"無法找到地下城計時器。(釣魚場) 計時器 ID: %d",
					DungeonTimeInformationLoader.FISH_TIMER_ID));
			return;
		}

		DungeonTimeProgress<?> progress = get_progress(dtInfo);
		if (progress == null)
			return;

		int map_id = getMapId();
		int remain = RealTimeClock.DAYS_SECONDS - progress.get_remain_seconds();
		progress.set_remain_seconds(remain + progress.get_remain_seconds());
		DungeonTimeProgressLoader.update(progress);
		if (5490 == map_id) {
			send_dungeon_progress(dtInfo);
		}
	}

	private HashMap<Integer, Integer> m_private_porbability = null;

	public void load_private_probability() {
		final HashMap<Integer, Integer> probabilities = new HashMap<Integer, Integer>();
		Selector.exec(
				"select * from characters_private_probability where object_id=?",
				new SelectorHandler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, getId());
					}

					@Override
					public void result(ResultSet rs) throws Exception {
						while (rs.next()) {
							probabilities.put(rs.getInt("skill_id"),
									rs.getInt("probability"));
						}
					}
				});
		m_private_porbability = probabilities;
	}

	public void add_private_probability(final int skill_id,
	                                    final int probability) {
		if (m_private_porbability != null)
			m_private_porbability.put(skill_id, probability);
		Updator.exec(
				"insert into characters_private_probability set object_id=?, skill_id=?, probability=? on duplicate key update skill_id=?, probability=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, getId());
						pstm.setInt(++idx, skill_id);
						pstm.setInt(++idx, probability);
						pstm.setInt(++idx, skill_id);
						pstm.setInt(++idx, probability);
					}
				});

	}

	public int get_private_probability(int skill_id) {
		return m_private_porbability != null
				&& m_private_porbability.containsKey(skill_id) ? m_private_porbability
				.get(skill_id) : 0;
	}

	public void truncate_private_probability() {
		if (m_private_porbability != null)
			m_private_porbability.clear();
	}

	private boolean _is_stat_reset;

	public boolean isStatReset() {
		return _is_stat_reset;
	}

	public void setStatReset(boolean flag) {
		_is_stat_reset = flag;
	}

	public String to_shop_title() {
		byte[] b = getShopChat();
		if (b != null) {
			try {
				return new String(b, "MS949");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public byte[] sendItemPacket(L1PcInstance pc, L1ItemInstance item) {
		BinaryOutputStream os = new BinaryOutputStream();
		os.writeC(8);
		os.writeBit(item.getId());
		os.writeC(16);
		os.writeBit(item.getItem().getItemDescId() == 0 ? -1L : item.getItem()
				.getItemDescId());
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
		os.writeBit(0);

		os.writeC(88);
		os.writeBit(0L);

		os.writeC(96);
		os.writeBit(item.getItem().getType2());

		if (item.getItem().getType2() != 0) {
			os.writeC(104);
			os.writeBit(item.getEnchantLevel());
		}

		os.writeC(112);
		os.writeBit(2);

		if (item.getAttrEnchantLevel() > 0) {
			int attrenchant = L1ItemInstance.attrEnchantToElementalType(item
					.getAttrEnchantLevel());
			os.writeBit(128L);
			os.writeBit(attrenchant);
			os.writeBit(136L);
			os.writeBit(attrenchant);
		}

		int size = item.getViewName().getBytes().length;

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
		switch (this.getType()) {
			case 0:
			case 2:
			case 3:
				return (bonus_level / 4) + 1;
			case 1:
				return (bonus_level / 2) + 1;
			case 4:
			case 5:
			case 6:
			case 7:
				return (bonus_level / 3) + 1;
		}
		return 0;
	}

	public int pvp_dmg_ignore;

	public int get_pvp_dmg_ignore() {
		return pvp_dmg_ignore;
	}

	public void set_pvp_dmg_ignore(int i) {
		this.pvp_dmg_ignore = i;
	}

	public void add_pvp_dmg_ignore(int i) {
		pvp_dmg_ignore += i;
	}

	public int pvp_mdmg;

	public int get_pvp_mdmg() {
		return pvp_mdmg;
	}

	public void set_pvp_mdmg(int i) {
		this.pvp_mdmg = i;
	}

	public void add_pvp_mdmg(int i) {
		pvp_mdmg += i;
	}

	public int pvp_mdmg_ignore;

	public int get_pvp_mdmg_ignore() {
		return pvp_mdmg_ignore;
	}

	public void set_pvp_mdmg_ignore(int i) {
		this.pvp_mdmg_ignore = i;
	}

	public void add_pvp_mdmg_ignore(int i) {
		pvp_mdmg_ignore += i;
	}

	public int immune_ignore;

	public int get_immune_ignore() {
		return immune_ignore;
	}

	public void set_immune_ignore(int i) {
		this.immune_ignore = i;
	}

	public void add_immune_ignore(int i) {
		immune_ignore += i;
	}

	public int immune_minus_per;

	public int get_immune_minus_per() {
		return immune_minus_per;
	}

	public void set_immune_minus_per(int i) {
		this.immune_minus_per = i;
	}

	public void add_immune_minus_per(int i) {
		immune_minus_per += i;
	}

	public int Moebius_ignore;

	public int get_Moebius_ignore() {
		return Moebius_ignore;
	}

	public void set_Moebius_ignore(int i) {
		this.Moebius_ignore = i;
	}

	private MJReGenerator _regenerator;

	public void on_regeneration() {
		_regenerator = MJReGenerator.newInstance(this);
	}

	public MJRegeneratorLatestActions get_latest_action() {
		return _regenerator == null ? MJRegeneratorLatestActions.LATEST_ACTION_STANDING
				: _regenerator.get_latest_action();
	}

	public void set_latest_action(MJRegeneratorLatestActions latest_action) {
		if (_regenerator != null)
			_regenerator.update_latest_action(latest_action);
	}

	public void run_regenerator() {
		if (_regenerator != null)
			_regenerator.set_is_stopped(false);
	}

	public void stop_regenerator() {
		if (_regenerator != null)
			_regenerator.set_is_stopped(true);
	}

	public void dispose_regenerator() {
		if (_regenerator != null) {
			_regenerator.dispose();
			_regenerator = null;
		}
	}

	private PolyTrigger polyTrigger;

	public void polyTrigger(PolyTrigger polyTrigger) {
		this.polyTrigger = polyTrigger;
	}

	public PolyTrigger polyTrigger() {
		return polyTrigger;
	}

	public interface PolyTrigger {
		public void onWork();
	}

	private MJAttrMap attribute = MJAttrMap.newConcurrentHash();

	public MJAttrMap attribute() {
		return attribute;
	}

	public void attributeInitialized() {
		// attribute = MJAttrMap.newConcurrentHash();
	}

	private MJObjectEventHandler eventHandler = MJObjectEventProvider
			.provider().newHandler();

	public MJObjectEventHandler eventHandler() {
		return eventHandler;
	}

	public void eventHandlerInitialized() {
		// eventHandler = MJObjectEventProvider.provider().newHandler();
	}

	// 新增屬性更新部分
	public void Stat_Reset_Str(boolean base_stat) {
		SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2,
				getType());
		SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
		sendPackets(new S_Weight(this));
		sendPackets(new S_OwnCharStatus2(this));

		if (base_stat) {
			resetBaseHitup();
			resetBaseDmgup();
		}
	}

	public void Stat_Reset_Dex(boolean base_stat) {
		SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2,
				getType());
		SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
		sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, this.getTotalER()));
		sendPackets(new S_OwnCharStatus2(this));
		sendPackets(new S_OwnCharAttrDef(this));

		resetBaseAc();

		if (base_stat) {
			resetBaseHitup();
			resetBaseDmgup();
		}
	}

	public void Stat_Reset_Con() {
		SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2,getType());
		SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
		sendPackets(new S_Weight(this));
		sendPackets(new S_HPUpdate(this));
	}

	public void Stat_Reset_Int() {
		SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2,getType());
		SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
		sendPackets(new S_SPMR(this));
		resetOriginalMagicHit();
	}

	public void Stat_Reset_Wis() {
		SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2,getType());
		SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
		sendPackets(new S_SPMR(this));
		resetBaseMr();
	}

	public void Stat_Reset_Cha() {
		SC_STAT_RENEWAL_INFO_NOTI.send_base_stat_renewal_info(this, 2,getType());
		SC_STAT_RENEWAL_BASE_STAT_NOTI.send_base_stat(this);
	}

	public void ShapePolyRingMaster() {
		sendPackets(new S_Message_YN(this.getId(), 180, ""));
	}

	public void resetStats() {

		L1SkillUse l1skilluse = new L1SkillUse();
		l1skilluse.handleCommands(this, L1SkillId.CANCELLATION, getId(),
				getX(), getY(), null, 0, L1SkillUse.TYPE_LOGIN);

		if (getWeapon() != null) {
			getInventory().setEquipped(getWeapon(), false, false, false, false);
		}

		for (L1ItemInstance armor : getInventory().getItems()) {
			if (armor != null && armor.isEquipped()) {
				getInventory().setEquipped(armor, false, false, false, false);
			}
		}

		// int bless = 0;
		// int lucky = 0;
		// int vital = 0;
		// / int invoke = 0;
		// int invoke_val_1 = 0;
		// int invoke_val_2 = 0;
		// int restore = 0;
		// int restore_val_1 = 0;
		// int restore_val_2 = 0;
		// int potion = 0;
		// int potion_val_1 = 0;
		// int potion_val_2 = 0;
		// AinhasadSpecialStatInfo Info =
		// AinhasadSpecialStatLoader.getInstance().getSpecialStat(getId());
		// if (Info != null) {
		// bless = Info.get_bless();
		// / lucky = Info.get_lucky();
		// vital = Info.get_vital();
		// invoke = Info.get_invoke();
		// invoke_val_1 = Info.get_invoke_val_1();
		// / invoke_val_2 = Info.get_invoke_val_2();
		// restore = Info.get_restore();
		// restore_val_1 = Info.get_restore_val_1();
		// restore_val_2 = Info.get_restore_val_2();
		// potion = Info.get_potion();
		// potion_val_1 = Info.get_potion_val_1();
		// potion_val_2 = Info.get_potion_val_2();
		// AinhasadSpecialStatLoader.getInstance().updateSpecialStat(this);
		// }

		sendPackets(new S_CharVisualUpdate(this));
		// System.out.println("1" +Info.get_bless());
		sendPackets(new S_OwnCharStatus2(this));
		// System.out.println("2"+Info.get_bless());
		sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
		// System.out.println("3"+Info.get_bless());
		setReturnStat(get_exp());
		// System.out.println("4"+Info.get_bless());
		sendPackets(new S_SPMR(this));
		// System.out.println("5"+Info.get_bless());
		sendPackets(new S_OwnCharAttrDef(this));
		// System.out.println("6"+Info.get_bless());
		sendPackets(new S_OwnCharStatus2(this));
		// System.out.println("7"+Info.get_bless());
		sendPackets(new S_ReturnedStat(this, S_ReturnedStat.START));
		// System.out.println("8"+Info.get_bless());

		try {
			save();
			// System.out.println("9"+Info.get_bless());
			// Info.set_bless(bless);
			// Info.set_lucky(lucky);
			// Info.set_vital(vital);
			// Info.set_invoke(invoke);
			// Info.set_invoke_val_1(invoke_val_1);
			// Info.set_invoke_val_2(invoke_val_2);
			// Info.set_restore(restore);
			// Info.set_restore_val_1(restore_val_1);
			// Info.set_restore_val_2(restore_val_2);
			// / Info.set_potion(potion);
			// Info.set_potion_val_1(potion_val_1);
			// Info.set_potion_val_2(potion_val_2);
			// AinhasadSpecialStatLoader.getInstance().updateSpecialStat(this);

		} catch (Exception e) {
			System.out.println("屬性重置命令錯誤");
			e.printStackTrace();
		}
		// System.out.println("10"+Info.get_bless());
	}

	// TODO 地下城系統
	public boolean InstanceDungeon = false;

	public int indun_room_num = -1;

	public int get_indun_room_num() {
		return indun_room_num;
	}

	public void set_indun_room_num(int i) {
		this.indun_room_num = i;
	}

	public MJIndunRoomModel indun_model;

	public MJIndunRoomModel indun_model() {
		return indun_model;
	}

	public void set_indun_model(MJIndunRoomModel model) {
		this.indun_model = model;
	}

	private L1ItemInstance equipmentChangeItem;

	public void setEquipmentChangeItem(L1ItemInstance targetItem) {
		equipmentChangeItem = targetItem;
	}

	public L1ItemInstance getEquipmentChangeItem() {
		return equipmentChangeItem;
	}

	private int equipmentChangeUseItemId;

	public void setEquipmentChangeUseItemId(int useItemId) {
		equipmentChangeUseItemId = useItemId;
	}

	public int getEquipmentChangeUseItemId() {
		return equipmentChangeUseItemId;
	}

	private ArrayList<L1ItemInstance> _eqcList;

	public ArrayList<L1ItemInstance> getEQCList() {
		return _eqcList;
	}

	public void setEQCList(ArrayList<L1ItemInstance> list) {
		_eqcList = list;
	}

	private ArrayList<L1ItemInstance> _itemselectorlist;

	public ArrayList<L1ItemInstance> getItemSelectorList() {
		return _itemselectorlist;
	}

	public void setItemSelectorList(ArrayList<L1ItemInstance> list) {
		_itemselectorlist = list;
	}

	// 機器人村莊位置
	public int _bot_wait = 0;
	public int _bot_wait_check = 0;

	private boolean _bot_warehouse = false;

	public boolean isBotWareHouse() {
		return _bot_warehouse;
	}

	public void setBotWareHouse(boolean flag) {
		_bot_warehouse = flag;
	}

	private boolean _bot_shop = false;

	public boolean isBotShop() {
		return _bot_shop;
	}

	public void setBotShop(boolean flag) {
		_bot_shop = flag;
	}

	private boolean _bot_buff = false;

	public boolean isBotBuff() {
		return _bot_buff;
	}

	public void setBotBuff(boolean flag) {
		_bot_buff = flag;
	}

	private boolean _bot_teleport = false;

	public boolean isBotTeleport() {
		return _bot_teleport;
	}

	public void setBotTeleport(boolean flag) {
		_bot_teleport = flag;
	}

	private boolean _bot_success = true;

	public boolean isBotSuccess() {
		return _bot_success;
	}

	public void setBotSuccess(boolean flag) {
		_bot_success = flag;
	}

	private int _judgement;

	public int getJudgementPoint() {
		return _judgement;
	}

	public void setJudgementPoint(int i) {
		this._judgement = i;
	}

	private boolean _Prime_war_zone = false;

	public boolean isPrime_War_Zone() {
		return _Prime_war_zone;
	}

	public void setPrime_War_Zone(boolean flag) {
		_Prime_war_zone = flag;
	}

	private boolean _isPrimeCast;

	public void setIsPrimeCast(boolean f) {
		_isPrimeCast = f;
	}

	public boolean isPrimeCast() {
		return _isPrimeCast;
	}

	private int _glory_earth_attr;

	public int getGlory_Earth_Attr() {
		return _glory_earth_attr;
	}

	public void setGlory_Earth_Attr(int i) {
		_glory_earth_attr = i;
	}

	private int _Bonus_drop_npc = 0;

	public int getBonusDropNpc() {
		return _Bonus_drop_npc;
	}

	public void setBonusDropNpc(int value) {
		_Bonus_drop_npc = value;
	}

	private boolean _Destroy_pier = false;

	public boolean isDestroy_pier() {
		return _Destroy_pier;
	}

	public void setDestroy_pier(boolean flag) {
		_Destroy_pier = flag;
	}

	private boolean _Destroy_horror = false;

	public boolean isDestroy_horror() {
		return _Destroy_horror;
	}

	public void setDestroy_horror(boolean flag) {
		_Destroy_horror = flag;
	}

	public void special_resistance_skill(L1PcInstance pc, int oldlevel,
	                                     int newlevel, boolean login) {
		int point = 0;
		int point1 = 0;
		int rd_point = 0;
		int rd_point1 = 0;
		int calcPurePierceAll = CalcStat.calcPurePierceAll(getAbility().getCha()) + CalcStat.calcPierceAll(getAbility().getTotalCha());
		if (login) {
			if (this.isPassive(MJPassiveID.RISING_POSS.toInt())) {
				int RealSteelLevel = pc.getLevel();
				if (RealSteelLevel < 80) {
					RealSteelLevel = 80;
				}
				point = (int) ((RealSteelLevel - 80) / 3) + 1;
				pc.addSpecialPierce(eKind.ABILITY, point);

				int rd_RealSteelLevel = pc.getLevel();
				if (rd_RealSteelLevel < 80) {
					rd_RealSteelLevel = 80;
				}
				rd_point = (int) ((rd_RealSteelLevel - 80) / 4) + 1;

				if (rd_point > 5)
					rd_point = 5;

				pc.getResistance().addPVPweaponTotalDamage(rd_point);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
		} else {
			if (oldlevel > newlevel) {
				if (this.isPassive(MJPassiveID.RISING_POSS.toInt())) {
					int RealSteelLevel = pc.getLevel();
					if (RealSteelLevel < 80) {
						RealSteelLevel = 80;
					}
					point1 = (int) ((oldlevel - 80) / 3)
							- (int) ((newlevel - 80) / 3) + 1;
					pc.addSpecialPierce(eKind.ABILITY, -point1);

					int rd_RealSteelLevel = pc.getLevel();
					if (rd_RealSteelLevel < 80) {
						rd_RealSteelLevel = 80;
					}
					rd_point = (int) ((oldlevel - 80) / 4)
							- (int) ((newlevel - 80) / 4) + 1;

					if (rd_point > 5)
						rd_point = 5;

					pc.getResistance().addPVPweaponTotalDamage(-rd_point);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				}
			} else {
				if (this.isPassive(MJPassiveID.RISING_POSS.toInt())) {
					int RealSteelLevel = pc.getLevel();
					if (RealSteelLevel < 80) {
						RealSteelLevel = 80;
					}
					point1 = (int) ((oldlevel - 80) / 3)
							- (int) ((newlevel - 80) / 3) + 1;
					pc.addSpecialPierce(eKind.ABILITY, -point1);

					int rd_RealSteelLevel = pc.getLevel();
					if (rd_RealSteelLevel < 80) {
						rd_RealSteelLevel = 80;
					}
					rd_point1 = (int) ((oldlevel - 80) / 4)
							- (int) ((newlevel - 80) / 4) + 1;

					if (rd_point1 > 5)
						rd_point1 = 5;

					pc.getResistance().addPVPweaponTotalDamage(-rd_point1);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				}
			}
		}
		pc.addSpecialPierce(eKind.ALL, calcPurePierceAll);
		SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
	}

	public void startBloodToSoul() {
		L1PcTimerControlHandler.getInstance().begin(getName(),
				L1PcTimerControlHandler.TimerType.BLOOD_TO_SOUL);
	}

	public void stopBloodToSoul() {
		L1PcTimerControlHandler.getInstance().stop(getName());
	}

	public void doAsura() {
		L1PcTimerControlHandler.getInstance().begin(getName(),
				L1PcTimerControlHandler.TimerType.ASURA);
	}

	public void disposeAsura() {
		L1PcTimerControlHandler.getInstance().stop(getName());
	}

	private int _craftUseType;

	public void setCraftUseType(int type) {
		_craftUseType = type;
	}

	/** 1:npc, 2:combine */
	public int getCraftUseType() {
		return _craftUseType;
	}

	private int _virualEinhasad;

	public void addVirualEinhasad(int i) {
		_virualEinhasad += i;
	}

	public void setVirualEinhasad(int i) {
		_virualEinhasad = i;
	}

	public int getVirualEinhasad() {
		return _virualEinhasad;
	}

	private boolean _Erzabe_circle = false;

	public boolean isErzabe_circle() {
		return _Erzabe_circle;
	}

	public void setErzabe_circle(boolean flag) {
		_Erzabe_circle = flag;
	}

	private int _console_type;

	public void setConsole_type(int type) {
		_console_type = type;
	}

	public int getConsole_type() {
		return _console_type;
	}

	private int _doll_judgement_type;

	public void setdoll_judgement_type(int type) {
		_doll_judgement_type = type;
	}

	public int getdoll_judgement_type() {
		return _doll_judgement_type;
	}

	public int[] _Insert_Tel_xym = new int[3];

	public void isEnd_Tel_xym(boolean Teleport) {
		isInsert_Tel_xym(0, 0, 0, false);
	}

	public void isInsert_Tel_xym(int x, int y, int map, boolean Teleport) {
		try {
			if (_Insert_Tel_xym == null) {
				_Insert_Tel_xym[0] = 33439 + CommonUtil.random(10);
				_Insert_Tel_xym[1] = 32804 + CommonUtil.random(10);
				_Insert_Tel_xym[2] = 4;
			} else {
				if (!Teleport) {
					this.start_teleport(_Insert_Tel_xym[0], _Insert_Tel_xym[1],
							_Insert_Tel_xym[2], this.getHeading(), 18339, false);
				} else {
					_Insert_Tel_xym[0] = this.getX();
					_Insert_Tel_xym[1] = this.getY();
					_Insert_Tel_xym[2] = this.getMapId();
					this.start_teleport(x, y, map, this.getHeading(), 18339,
							false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ArrayList<String> _macro_list = new ArrayList<String>();

	public ArrayList<String> getMacroList() {
		return _macro_list;
	}

	public void addMacroList(String ment) {
		_macro_list.add(ment);
	}

	public void getMacroListIdentify() {
		_macro_list.forEach(ment -> sendPackets(new S_SystemMessage(_macro_list
				.indexOf(ment) + ". " + ment)));
	}

	public boolean isMacroTimerStart() {
		return _macro_timer != null;
	}

	private Macro _macro_timer;

	public void startMacroTimer() {
		synchronized (this) {
			if (_macro_timer != null)
				return;

			if (_macro_list.size() == 0)
				return;

			_macro_timer = new Macro(10000, _macro_list);
			GeneralThreadPool.getInstance().execute(_macro_timer);
		}
	}

	public void stopMacroTimer() {
		synchronized (this) {
			if (_macro_timer == null) {
				return;
			}
			_macro_timer.cancel();
			_macro_timer = null;
		}
	}

	private class Macro implements Runnable {
		private boolean _active;
		private long _interval;
		public ArrayList<String> _list;

		@Override
		public void run() {
			try {

				for (int i = 0; i < _list.size(); i++) {
					if (!_active) {
						break;
					}
					for (L1PcInstance listner : L1World.getInstance()
							.getAllPlayers()) {
						L1ExcludingList spamList15 = SpamTable.getInstance()
								.getExcludeTable(listner.getId());
						if (!spamList15.contains(0, getName())) {
							if (listner.isShowWorldChat()) {
								listner.sendPackets(new S_NewChat(3, _list
										.get(i), i, L1PcInstance.this));
							}
						}
					}

					Thread.sleep(10000);
				}

				GeneralThreadPool.getInstance().schedule(this, _interval);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new L1PcInstance[0];
		}

		public Macro(long interval, ArrayList<String> list) {
			_interval = interval;
			_list = list;
			_active = true;
		}

		public void cancel() {
			_active = false;
		}
	}

	private int _move_speed_over_count;

	public int getSpeedOverCount() {
		return _move_speed_over_count;
	}

	public void addSpeedOverCount() {
		_move_speed_over_count += 1;
	}

	public void delSpeedOverCount() {
		if (_move_speed_over_count == 0)
			_move_speed_over_count = 0;
		else
			_move_speed_over_count -= 1;
	}

	public int[] _move_speed_over_loc = new int[3];

	public double _AdenBonus;

	public double getAdenBonus() {
		return _AdenBonus;
	}

	public void setAdenBonus(double i) {
		_AdenBonus = i;
	}

	public void addAdenBonus(double i) {
		_AdenBonus += i;
	}

	public double _ItemBonus;

	public double getItemBonus() {
		return _ItemBonus;
	}

	public void setItemBonus(double i) {
		_ItemBonus = i;
	}

	public void addItemBonus(double i) {
		_ItemBonus += i;
	}

	// XXX 免疫和路西法專用方法
	public int pvp_defense_per;

	public int get_pvp_defense_per() {
		return pvp_defense_per;
	}

	public void set_pvp_defense_per(int i) {
		this.pvp_defense_per = i;
	}

	public void add_pvp_defense_per(int i) {
		pvp_defense_per += i;
	}

	public int Magic_defense_per;

	public int get_Magic_defense_per() {
		return Magic_defense_per;
	}

	public void set_Magic_defense_per(int i) {
		this.Magic_defense_per = i;
	}

	public void add_Magic_defense_per(int i) {
		Magic_defense_per += i;
	}

	private int _infinity_team_id;

	public int getInfinityTeamId() {
		return _infinity_team_id;
	}

	public void setInfinityTeamId(int i) {
		_infinity_team_id = i;
	}

	public int[] doll_update_option = new int[2];

	private long _clan_contribution;

	public long getClanContribution() {
		return _clan_contribution;
	}

	public void setClanContribution(long i) {
		_clan_contribution = i;
	}

	public void addClanContribution(int i) {
		_clan_contribution += i * Config.ServerAdSetting.CLAN_CONTRIBUTION;
		if (_clan_contribution > 10000000)
			_clan_contribution = 10000000;
		else if (_clan_contribution < 0)
			_clan_contribution = 0;
	}

	private Timestamp _class_rank_bless;

	public void setClassRankBlessTime(Timestamp ts) {
		_class_rank_bless = ts;
	}

	public Timestamp getClassRankBlessTime() {
		return _class_rank_bless;
	}

	private Map<Integer, CustomQuestUser> customQuestList = new HashMap<Integer, CustomQuestUser>();

	private int customQuestId;

	public int getCustomQuestId() {
		return customQuestId;
	}

	public void setCustomQuestId(int getCunstomQuestId) {
		this.customQuestId = getCunstomQuestId;
	}

	private int customQuestNpcObjId;

	public int getCustomQuestNpcObjId() {
		return customQuestNpcObjId;
	}

	public void setCustomQuestNpcObjId(int customQuestNpcObjId) {
		this.customQuestNpcObjId = customQuestNpcObjId;
	}

	public void addCustomQuest(int questid, CustomQuestUser quest) {
		if (!customQuestList.containsKey(questid)) {
			customQuestList.put(questid, quest);
		}
	}

	public void addCustomQuest(int questid, eCustomQuestType type) {
		if (!customQuestList.containsKey(questid)) {
			customQuestList.put(questid, new CustomQuestUser(questid, type));
		}
	}

	public void removeCustomQuest(int questid) {
		if (customQuestList.containsKey((Object) questid)) {
			customQuestList.remove((Object) questid);
		}
	}

	public void addCustomSuccessCount(int questid, int count) {
		if (customQuestList.containsKey(questid)) {
			customQuestList.get((Object) questid).setSuccessCount(count);
		}
	}

	public Map<Integer, CustomQuestUser> getCustomQuestList() {
		return customQuestList;
	}

	public CustomQuestUser getCustomQuestUser(int questid) {
		return customQuestList.get((Object) questid);
	}

	public void call_clan_advence(L1PcInstance target) {
		L1Location loc = new L1Location();
		int locX = this.getX();
		int locY = this.getY();
		int heading = this.getHeading();
		loc.setMap(this.getMapId());
		switch (heading) {
			case 1:
				locX += 1;
				locY -= 1;
				break;
			case 2:
				locX += 1;
				break;
			case 3:
				locX += 1;
				locY += 1;
				break;
			case 4:
				locY += 1;
				break;
			case 5:
				locX -= 1;
				locY += 1;
				break;
			case 6:
				locX -= 1;
				break;
			case 7:
				locX -= 1;
				locY -= 1;
				break;
			case 0:
				locY -= 1;
				break;
		}
		loc.setX(locX);
		loc.setY(locY);

		target.send_effect(19583);
		L1Teleport.getInstance().doTeleport(target, locX, locY, getMapId());
	}

	private boolean _spear_mode_type = false;

	public boolean isSpearModeType() {
		return _spear_mode_type;
	}

	public void setSpear_Mode_Type(boolean type) {
		_spear_mode_type = type;
	}

	private boolean _vanguard_type = false;

	public boolean getVanguardType() {
		return _vanguard_type;
	}

	public void setVanguardType(boolean flag) {
		_vanguard_type = flag;
	}

	public static final MJAttrKey<ArrayList<CPMWBQinfo>> pcbookquestinfo = MJAttrKey
			.newInstance("cpmw-pc-bookqeust-model");

	public int Get_BQ_Size() {
		return this.attribute().getNotExistsNew(L1PcInstance.pcbookquestinfo)
				.get().size();
	}

	public ArrayList<CPMWBQinfo> Get_BQ_Info() {
		return this.attribute().getNotExistsNew(L1PcInstance.pcbookquestinfo)
				.get();
	}

	public void Add_BQ_Info(int mapid, int mapdesc, CPMWBQinfo model) {
		model.setMapid(mapid);
		model.setMapdesc(mapdesc);
		model.setMoncount(0);
		model.setIsclear(false);
		this.attribute().getNotExistsNew(L1PcInstance.pcbookquestinfo).get()
				.add(model);
		MJObjectEventProvider.provider().pcEventFactory().fireCPMWBQAdded(this);
	}

	public void Update_BQ(int index, CPMWBQinfo model,
	                      CS_HUNTING_QUEST_REWARD_REQ req) {
		model.setMapid(req.get_map_number());
		model.setMapdesc(req.get_location_desc());
		model.setMoncount(0);
		model.setIsclear(true);
		this.attribute().getNotExistsNew(L1PcInstance.pcbookquestinfo).get()
				.set(index, model);
	}

	private void StoreBQ() {
		if (getAI() != null || noPlayerCK) {
			return;
		}
		CPMWBQUserTable Uinfo = CPMWBQUserTable.getInstance();
		for (CPMWBQinfo BQinfo : Get_BQ_Info()) {
			Uinfo.Update_Info(getId(), BQinfo);
		}
		if (Get_BQ_Info() != null) {
			Get_BQ_Info().clear();
		}
	}

	public double _attack_delay_checker = 0;

	public double getAttackDelayChecker() {
		return _attack_delay_checker;
	}

	public void setAttackDelayChecker(double value) {
		_attack_delay_checker = value;
	}

	public int _attack_delay_count = 0;

	public int getAttackDelayCount() {
		return _attack_delay_count;
	}

	public void addAttackDelayCount(int value) {
		_attack_delay_count += value;
	}

	public void setAttackDelayCount(int value) {
		_attack_delay_count = value;
	}

	/*
	 * private boolean _Attendance_premium = false; private boolean
	 * _Attendance_Special = false;
	 *
	 * public boolean get_Attendance_premium() { return _Attendance_premium; }
	 *
	 * public void set_Attendance_premium(boolean premium) {
	 * this._Attendance_premium = premium; }
	 *
	 * public boolean get_Attendance_Special() { return _Attendance_Special; }
	 *
	 * public void set_Attendance_Special(boolean Special) {
	 * this._Attendance_Special = Special; }
	 */

	public long _slotsavetime;

	public long get_slotsavetime() {
		return _slotsavetime;
	}

	public void set_slotsavetime(long _slot) {
		this._slotsavetime = _slot;
	}

	public List<MJDeathPenaltyItemModel> get_deathpenalty_item() {
		return this.attribute()
				.getNotExistsNew(L1PcInstance.deathpenaltyitemModelKey).get();
	}

	public List<MJDeathPenaltyExpModel> get_deathpenalty_exp() {
		return this.attribute().getNotExistsNew(L1PcInstance.deathpenaltyexpModelKey).get();
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
		if (lawful >= 0) {
			cost = (int) (cost * 0.7);
		}
		MJDeathPenaltyExpModel model = new MJDeathPenaltyExpModel();
		model.setOwnerId(getId());
//		model.set_Lost_Exp(exp);
		model.setExp_ratio(50000);//5% = 50000 db記錄數值
		model.setDeathLevel(this.getLevel());
//		model.setRecovery_cost(MJDeathPenaltyService.service().price_exppenalty());
		model.setRecovery_cost(cost);
		model.setDelete_time(System.currentTimeMillis() / 1000 + 3600 * 24);
		this.attribute().getNotExistsNew(L1PcInstance.deathpenaltyexpModelKey).get().add(model);
		MJDeathPenaltyexpDatabaseLoader.getInstance().update(this, model);
		MJDeathPenaltyexpDatabaseLoader.getInstance().do_Select(this);
	}

	public void delete_deathpenalty_exp(int i){
		Updator.exec("delete from characters_deathpenalty_exp where id=?", new Handler(){
			@Override
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
		model.setDelete_time(System.currentTimeMillis() / 1000 + 3600 * 24);
		this.attribute().getNotExistsNew(L1PcInstance.deathpenaltyitemModelKey)
				.get().add(model);
	}

	public List<MJChaPushModel> get_push_info() {
		return this.attribute().getNotExistsNew(L1PcInstance.pcpushmodelkey)
				.get();
	}

	public int _magic_add_count;

	public int get_magic_add_count() {
		return _magic_add_count;
	}

	public void set_magic_add_count(int count) {
		_magic_add_count = count;
	}

	public int _divine_protection = 0;

	public int get_divine_protection() {
		return _divine_protection;
	}

	public void set_divine_protection(int devine) {
		_divine_protection = devine;
	}

	private long _lastMotionMillis;

	public void setLastMotionMillis(long lastMotionMillis) {
		_lastMotionMillis = lastMotionMillis;
	}

	public long getLastMotionMillis() {
		return _lastMotionMillis;
	}

	private boolean _empire_overlord = false;

	public boolean isEmpireOverlord() {
		return _empire_overlord;
	}

	public void setEmpireOverlord(boolean value) {
		_empire_overlord = value;
	}

	private boolean _titan_berserk = false;

	public boolean isTitanBerserk() {
		return _titan_berserk;
	}

	public void setTitanBerserk(boolean value) {
		_titan_berserk = value;
	}

	private boolean _titan_beast = false;

	public boolean isTitanBeast() {
		return _titan_beast;
	}

	public void setTitanBeast(boolean value) {
		_titan_beast = value;
	}

	private ArrayList<L1Character> _TitanBeastChaList = new ArrayList<L1Character>();

	public ArrayList<L1Character> getTitanBeastChaList() {
		return _TitanBeastChaList;
	}

	public void addTitanBeastChaList(L1Character cha) {
		if (!_TitanBeastChaList.contains(cha)) {
			_TitanBeastChaList.add(cha);
		}
	}

	public void removeTitanBeastChaList(L1Character cha) {
		if (!_TitanBeastChaList.contains(cha)) {
			_TitanBeastChaList.remove(cha);
		}
	}

	private int _emblem = 0;

	public int getExpEmblem() {
		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance()
				.getSpecialStat(this.getId());
		int special_point = 0;
		if (Info != null) {
			special_point = CalcStat.calcAinhasadStatSecond(Info.get_bless());
		}
		return this._emblem + special_point;
	}

	public void setExpEmblem(int i) {
		this._emblem = i;
	}

	public void addExpEmblem(int i) {
		this._emblem += i;
	}

	private int _MagicHit;

	public int getMagicHit() {
		return _MagicHit;
	}

	public void addMagicHit(int i) {
		_MagicHit += i;
	}

	private static Timer _ainhasad_re_time = new Timer();
	private AinhasadHpMpRegeneration _ainhasad_Timer;

	public void startAinhasadTimer(AinhasadSpecialStatInfo Info) {
		if (Info != null) {
			if (Info.get_restore() > 0) {
				if (_ainhasad_Timer == null) {
					int interval = 32 * 1000;
					_ainhasad_Timer = new AinhasadHpMpRegeneration(this, Info);
					_ainhasad_re_time.scheduleAtFixedRate(_ainhasad_Timer,
							interval, interval);
				}
			}
		}
	}

	public void stopAinhasadTimer() {
		if (_ainhasad_Timer != null) {
			_ainhasad_Timer.cancel();
			_ainhasad_Timer = null;
		}
	}

	public boolean _massTeleportSwitch;

	/** Einhasad的祝福 **/
	public Timestamp _einhasdgrace;

	public Timestamp getEinhasadGraceTime() {
		return _einhasdgrace;
	}

	public void setEinhasadGraceTime(Timestamp ts) {
		_einhasdgrace = ts;
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
		} else if (getLevel() == 96) {// 修改
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

		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance()
				.getSpecialStat(this.getId());
		int special_point = 0;
		if (Info != null) {
			special_point = CalcStat.calcAinhasadStatFirst(Info.get_bless());
		}
		return efficiency + special_point;
	}

	public int getBlessOfAinExp() {
		int bless_exp = 0;
		if (hasSkillEffect(4005)) {
			if (getLevel() < 55) {
				bless_exp += 15300;
			} else if ((getLevel() >= 55) && (getLevel() < 60)) {
				bless_exp += 14300;
			} else if ((getLevel() >= 60) && (getLevel() < 65)) {
				bless_exp += 13300;
			} else if ((getLevel() >= 65) && (getLevel() < 70)) {
				bless_exp += 12300;
			}
		}
		if (hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
			bless_exp += 8000;
		}
		if (hasSkillEffect(L1SkillId.DRAGON_EMERALD)) {
			bless_exp += 5400;
		}
		return bless_exp;
	}

	private int _ain_exp_bonus;
	public boolean _dragonbless = false;

	public int getAinExpBonus() {
		return _ain_exp_bonus;
	}

	// 展覽會
	private L1TimeCollectionHandler _timeCollection;

	public L1TimeCollectionHandler getTimeCollection() {
		return _timeCollection;
	}

	public void createTimeCollection() {
		_timeCollection = new L1TimeCollectionHandler(this);
	}

	// 背包獎勵
	private ArrayList<Integer> _inven_bonus_items = new ArrayList<Integer>();

	public void addInvenBonusItems(int itemid) {
		_inven_bonus_items.add(itemid);
	}

	public void delInvenBonusItems(int itemid) {
		_inven_bonus_items.remove((Object) itemid);
	}

	public boolean isInvenBonusItems(int itemid) {
		return _inven_bonus_items.contains(itemid);
	}

	// 臨時技能
	private ArrayList<Integer> _temp_skill_active = new ArrayList<Integer>();

	public void addTempSkillActive(int skillId) {
		_temp_skill_active.add(skillId);
	}
	public void delTempSkillActive(int skillId) {
		_temp_skill_active.remove((Object) skillId);
	}
	public boolean isTempSkillActive(int skillId) {
		return _temp_skill_active.contains(skillId);
	}

	// 臨時被動

	private ArrayList<Integer> _temp_skill_passive = new ArrayList<Integer>();

	public void addTempSkillPassive(int PassiveId) {
		_temp_skill_passive.add(PassiveId);
	}
	public void delTempSkillPassive(int PassiveId) {
		_temp_skill_passive.remove((Object)PassiveId);
	}
	public boolean isTempSkillPassive(int PassiveId) {
		return _temp_skill_passive.contains(PassiveId);
	}


	//pc_golden_buff
	private boolean _pc_golden_status = false;
	public boolean isPcGoldenStatus() {
		return _pc_golden_status;
	}
	public void set_PcGoldenSstatus(boolean b) {
		_pc_golden_status = b;
	}
	private int _pc_golden_buff_index0_time;
	private int _pc_golden_buff_index1_time;
	public int get_PcGoldenBuffIndex0Time() {
		return _pc_golden_buff_index0_time;
	}
	public int get_PcGoldenBuffIndex1Time() {
		return _pc_golden_buff_index1_time;
	}
	public void set_PcGoldenBuffIndex0Time(int i) {
		_pc_golden_buff_index0_time = i;
	}
	public void set_PcGoldenBuffIndex1Time(int i) {
		_pc_golden_buff_index1_time = i;
	}

	private ArrayList<Integer> _pc_golden_buff = new ArrayList<Integer>();

	public void addPcGoldenBuff(int buffid) {
		_pc_golden_buff.add(buffid);
	}
	public void delPcGoldenBuff(int buffid) {
		_pc_golden_buff.remove((Object) buffid);
	}

	public boolean isPcGoldenBuff(int buffid) {
		return _pc_golden_buff.contains(buffid);
	}

	public ArrayList<Integer> getPcGoldenBuffList() {
		return _pc_golden_buff;
	}

	// Einhasad的神力
	private HashMap<Integer, Timestamp> _ainhasad_faith = new HashMap<Integer, Timestamp>();

	public void addAinhasad_faith(int index, Timestamp endTime) {
		_ainhasad_faith.put(index, endTime);
	}
	public void delAinhasad_faith(int index) {
		if (isAinhasad_faith(index)) {
			_ainhasad_faith.remove(index);
		}
	}

	public Timestamp getAinhasad_faith_EndTime(int index) {
		return _ainhasad_faith.get(index);
	}
	public boolean isAinhasad_faith(int index) {
		return _ainhasad_faith.containsKey(index);
	}

	public HashMap<Integer, Timestamp> getAinHasd_faith(){
		return _ainhasad_faith;
	}

	private int _armor_magic_pro;

	public void add_armor_magic_pro(int i) {
		_armor_magic_pro += i;
	}

	public int get_armor_magic_pro() {
		return _armor_magic_pro;
	}

	private int _status_time_reduce;

	public void add_status_time_reduce(int i) {
		_status_time_reduce += i;
	}

	public int get_status_time_reduce() {
		return _status_time_reduce;
	}

	/** 藥水恢復量 **/
	private int _potionRecoveryRate = 0;

	public int getPotionRecoveryRatePct() {
		return _potionRecoveryRate;
	}

	public void addPotionRecoveryRatePct(int i) {
		_potionRecoveryRate += i;
	}

	// 聖物
	private L1FavorBookInventory _favorBook;

	public L1FavorBookInventory getFavorBook() {
		return _favorBook;
	}

	public void createFavorBookInventory() {
		_favorBook = new L1FavorBookInventory(this);
	}

	public boolean _AurakiaCircle = false;

	public void setAurakiaCircle(boolean a) {
		if (a) {
			_AurakiaCircle = true;
		} else {
			_AurakiaCircle = false;
		}
	}

	public boolean isAurakiaCircle() {
		return _AurakiaCircle;
	}

	private int _threeItemEquipped = 0;

	public int getThreeItemEquipped() {
		return _threeItemEquipped;
	}

	public void addThreeItemEquipped(int i) {
		_threeItemEquipped += i;
	}

	public void removeThreeSkillEffect() {
		if (hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL)) {
			removeSkillEffect(L1SkillId.STATUS_DRAGON_PEARL);
		}
		MJNotiSkillModel model = MJNotiSkillService.service().model(
				L1SkillId.STATUS_DRAGON_PEARL);
		model.icons(this, 0, false);
		this.setPrime_War_Zone(false);
	}
	public void startHpARegeneration(){
		if (getHpAr() == 0){
			return;
		}
		if (!_hpARegeneration){
			final long interval = 32000L;
			_hpArRegen = new HpARegeneration(this, interval);
			GeneralThreadPool.getInstance().schedule(_hpArRegen, interval);
			_hpARegeneration = true;
		}
	}
	public void stopHpARegeneration(){
		if (_hpARegeneration){
			_hpArRegen.cancel();
			_hpArRegen = null;
			_hpARegeneration = false;
		}
	}

	public void startMpARegeneration(){
		if (getMpAr() == 0){
			return;
		}
		if (!_mpARegeneration){
			final long interval = 16000L;
			_mpArRegen = new MpARegeneration(this, interval);
			GeneralThreadPool.getInstance().schedule(_mpArRegen, interval);
			_mpARegeneration = true;
		}
	}
	public void stopMpARegeneration(){
		if (_mpARegeneration){
			_mpArRegen.cancel();
			_mpArRegen = null;
			_mpARegeneration = false;
		}
	}
	public boolean isPcBuff() {
		long sysTime = System.currentTimeMillis();
		// If the PC room buff of the account is null
		if (getAccount().Buff_PC_Room() == null) {
			return false;
		}
		if (getAccount().getBuff_PC_Room().getTime() < sysTime) {
			Time getTime;(int columnIndex;)
			return false;
		}
		return true;
	}
	private L1ItemInstance HalpasArmor;
	public void setHalpasArmor(L1ItemInstance item) {
		HalpasArmor = item;
	}
	public L1ItemInstance getHalpasArmor() {
		return HalpasArmor;
	}
	public void removeHalpasArmor() {
		HalpasArmor = null;
	}

	public boolean checkHalpasTime() {
		if (HalpasArmor == null) {
			return false;
		}
		long currentTime = System.currentTimeMillis();
		long endTime;
		if (HalpasArmor.getHalpas_Time() !=null) {
			endTime = Timestamp.valueOf(HalpasArmor.getHalpas_Time().toString()).getTime();
		} else {
			endTime = 0;
		}

		int coolTime = Long.valueOf((endTime-currentTime) / 1000).intValue();
		if (coolTime < 0) {
			return true;
		}
		return false;
	}
	private boolean _CastleEffect = false;
	public boolean isCastleEffect() {
		return _CastleEffect;
	}
	public void setCastleEffect(boolean b) {
		_CastleEffect = b;
	}
	private boolean _isMassTel = false;

	public void set_MassTel(boolean flag) {
		_isMassTel = flag;
	}
	public boolean isMassTel() {
		return _isMassTel;
	}

	private int _ForcePolyId;
	public void set_ForcePolyId(int polyid) {
		_ForcePolyId = polyid;
	}
	public int get_ForcePolyId() {
		return _ForcePolyId;
	}
	private Timestamp _clanjointime;

	public void setClanJoinDate(Timestamp t) {
		_clanjointime = t;
	}
	private boolean _tyrant_excute;

	public void set_Tyrant_Excute(boolean flag) {
		_tyrant_excute = flag;
	}
	public boolean is_Tyrant_Excute() {
		return _tyrant_excute;
	}

	private L1PcInstance _behemoth_attacker;

	public void setBehemoth_Attacker(L1PcInstance attacker) {
		_behemoth_attacker = attacker;
	}
	public L1PcInstance getBehemoth_Attacker() {
		return _behemoth_attacker;
	}
	private int _behemoth_dmg_sum;

	public void clear_Behemoth_Damage() {
		_behemoth_dmg_sum = 0;
	}
	public void add_Behemoth_Damage(int damage) {
		_behemoth_dmg_sum += damage;
	}

	public int get_Behemoth_Heal() {
		int heal = _behemoth_dmg_sum * 40 / 100;
		if (heal >= 2000) {
			heal = 2000;
		}
		return heal;
	}
	private boolean _chainsword_expose;
	public void setChainSwordExposed(boolean flag) {
		_chainsword_expose = flag;
	}
	public boolean isChainSwordExposed() {
		return _chainsword_expose;
	}
	private int _chainsword_step;
	public void setChainSwordStep(int i) {
		_chainsword_step = i;
	}
	public int getChainSwordStep() {
		return _chainsword_step;
	}
	private int _abnormal_status_pvp_damage_reduction;
	public int getAbnormalStatusPvPReduction() {
		return _abnormal_status_pvp_damage_reduction;
	}
	public void setAbnormalStatusPvPReduction(int i) {
		_abnormal_status_pvp_damage_reduction = i;
	}
	public void addAbnormalStatusPvPReduction(int i) {
		_abnormal_status_pvp_damage_reduction =+ i;
	}

}