package l1j.server;

import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.MJHexHelper;

import java.util.ArrayList;

public final class Config {

	public static final boolean DEBUG = false;
	public static boolean shutdownCheck = false;
	public static int THREAD_P_EFFECTS;
	public static int THREAD_P_GENERAL;
	public static int AI_MAX_THREAD;
	public static boolean test = false;
	public static boolean version_check = false;
	/******************************************
	 *  Custom Config Json.(TODO)              *
	 *  Title: json Setting 2018~2019  	       *
	 *  Person: Nature:)                       *
	 *  Exe: Notepad++                         *
	 ******************************************/



	//TODO Einhasad_item
	public static EinhasadInfo Einhasad_item = new EinhasadInfo();
	public static class EinhasadInfo{
		public int EINHASAD_LIMIT;
		public int EINHASAD_UNIT;
		public int RE_DRAGON_DIAMOND_CHARGE;
		public int RE_DRAGON_DIAMOND_FINEST;
		public int RE_DRAGON_DIAMOND_ADVANCED;
		public int RE_DRAGON_DIAMOND_NORMAL;
		public int RE_DRAGON_SAPPHIRE_NORMAL;
		public int RE_DRAGON_RUBY_NORMAL;
		public int RE_DRAGON_DIAMOND_APPLE;
		public EinhasadInfo() {
			EINHASAD_LIMIT				= 80000000;
			EINHASAD_UNIT				= 10000;
			RE_DRAGON_DIAMOND_CHARGE	= 1000;
			RE_DRAGON_DIAMOND_FINEST	= 2000;
			RE_DRAGON_DIAMOND_ADVANCED	= 800;
			RE_DRAGON_DIAMOND_NORMAL	= 100;
			RE_DRAGON_SAPPHIRE_NORMAL	= 50;
			RE_DRAGON_RUBY_NORMAL		= 30;
			RE_DRAGON_DIAMOND_APPLE		= 500;
		}
	}

	//TODO MagicDoll_Potential
	public static MagicDoll_Potential MagicDollInfo = new MagicDoll_Potential();
	public static class MagicDoll_Potential {
		public int POTION_ITEMID;
		public int POTION_COUNT;
		public int POTION_COUNT1;
		public int POTION_COUNT2;
		public int POTION_COUNT3;
		public int POTION_COUNT4;
		public int BONUS_RANDOM_PER1;
		public int BONUS_RANDOM_PER2;
		public int BONUS_RANDOM_PER3;
		public int BONUS_RANDOM_PER4;
		public int BONUS_RANDOM_PER5;
		public int RANDOM_UP_MIN;
		public int RANDOM_UP_MAX;
		public int RANDOM_UP_MIN1;
		public int RANDOM_UP_MAX1;
		public int RANDOM_UP_MIN2;
		public int RANDOM_UP_MAX2;
		public int RANDOM_UP_MIN3;
		public int RANDOM_UP_MAX3;
		public int RANDOM_UP_MIN4;
		public int RANDOM_UP_MAX4;
		public int RANDOM_UP_MIN5;
		public int RANDOM_UP_MAX5;
		public int RANDOM_UP_MIN6;
		public int RANDOM_UP_MAX6;
		public int RANDOM_UP_MIN7;
		public int RANDOM_UP_MAX7;
		public int RANDOM_UP_MIN8;
		public int RANDOM_UP_MAX8;
		public int RANDOM_UP_MIN9;
		public int RANDOM_UP_MAX9;
		public int RANDOM_UP_MIN10;
		public int RANDOM_UP_MAX10;
		public int RANDOM_UP_MIN11;
		public int RANDOM_UP_MAX11;
		public int BONUS_TYPE1;
		public int BONUS_TYPE2;
		public int BONUS_TYPE3;
		public int BONUS_TYPE4;
		public int BONUS_TYPE_BLESS1;
		public int BONUS_TYPE_BLESS2;
		public int BONUS_TYPE_BLESS3;
		public int BONUS_TYPE_BLESS4;
		public int MP_SMALL_MIN_PC;
		public int MP_SMALL_MAX_PC;
		public int MP_SMALL_CHANCE_PC;
		public int MP_SMALL_MIN_NPC;
		public int MP_SMALL_MAX_NPC;
		public int MP_SMALL_CHANCE_NPC;
		public int HP_SMALL_MIN_PC;
		public int HP_SMALL_MAX_PC;
		public int HP_SMALL_CHANCE_PC;
		public int HP_SMALL_MIN_NPC;
		public int HP_SMALL_MAX_NPC;
		public int HP_SMALL_CHANCE_NPC;
		public int HP_LARGE_MIN_PC;
		public int HP_LARGE_MAX_PC;
		public int HP_LARGE_CHANCE_PC;
		public int HP_LARGE_MIN_NPC;
		public int HP_LARGE_MAX_NPC;
		public int HP_LARGE_CHANCE_NPC;
		public int MP_LARGE_MIN_PC;
		public int MP_LARGE_MAX_PC;
		public int MP_LARGE_CHANCE_PC;
		public int MP_LARGE_MIN_NPC;
		public int MP_LARGE_MAX_NPC;
		public int MP_LARGE_CHANCE_NPC;
		public int DOLL_SOULOFFLAME;
		public int DOLL_JUDGEMENT;
		public int DOLL_DECAY_POTION;
		public int DOLL_PVP;
		public MagicDoll_Potential() {
			POTION_ITEMID			= 4100676;
			POTION_COUNT			= 3;
			POTION_COUNT1			= 5;
			POTION_COUNT2			= 10;
			POTION_COUNT3			= 35;
			POTION_COUNT4			= 100;
			BONUS_RANDOM_PER1		= 1;
			BONUS_RANDOM_PER2		= 1;
			BONUS_RANDOM_PER3		= 1;
			BONUS_RANDOM_PER4		= 10;
			BONUS_RANDOM_PER5		= 20;
			RANDOM_UP_MIN			= 0;
			RANDOM_UP_MAX			= 84;
			RANDOM_UP_MIN1			= 85;
			RANDOM_UP_MAX1			= 100;
			RANDOM_UP_MIN2			= 0;
			RANDOM_UP_MAX2			= 86;
			RANDOM_UP_MIN3			= 87;
			RANDOM_UP_MAX3			= 100;
			RANDOM_UP_MIN4			= 0;
			RANDOM_UP_MAX4			= 88;
			RANDOM_UP_MIN5			= 89;
			RANDOM_UP_MAX5			= 100;
			RANDOM_UP_MIN6			= 0;
			RANDOM_UP_MAX6			= 90;
			RANDOM_UP_MIN7			= 91;
			RANDOM_UP_MAX7			= 100;
			RANDOM_UP_MIN8			= 0;
			RANDOM_UP_MAX8			= 92;
			RANDOM_UP_MIN9			= 93;
			RANDOM_UP_MAX9			= 100;
			RANDOM_UP_MIN10			= 0;
			RANDOM_UP_MAX10			= 94;
			RANDOM_UP_MIN11			= 95;
			RANDOM_UP_MAX11			= 100;
			BONUS_TYPE1				= 50;
			BONUS_TYPE2				= 40;
			BONUS_TYPE3				= 30;
			BONUS_TYPE4				= 20;
			BONUS_TYPE_BLESS1		= 40;
			BONUS_TYPE_BLESS2		= 30;
			BONUS_TYPE_BLESS3		= 20;
			BONUS_TYPE_BLESS4		= 10;
			MP_SMALL_MIN_PC 		= 0;
			MP_SMALL_MAX_PC			= 1;
			MP_SMALL_CHANCE_PC		= 10;
			MP_SMALL_MIN_NPC		= 0;
			MP_SMALL_MAX_NPC		= 1;
			MP_SMALL_CHANCE_NPC		= 10;
			HP_SMALL_MIN_PC			= 0;
			HP_SMALL_MAX_PC			= 5;
			HP_SMALL_CHANCE_PC		= 10;
			HP_SMALL_MIN_NPC	 	= 0;
			HP_SMALL_MAX_NPC	 	= 5;
			HP_SMALL_CHANCE_NPC		= 10;
			MP_LARGE_MIN_PC			= 0;
			MP_LARGE_MAX_PC			= 5;
			MP_LARGE_CHANCE_PC		= 10;
			MP_LARGE_MIN_NPC 		= 0;
			MP_LARGE_MAX_NPC		= 5;
			MP_LARGE_CHANCE_NPC		= 10;
			HP_LARGE_MIN_PC			= 0;
			HP_LARGE_MAX_PC			= 5;
			HP_LARGE_CHANCE_PC		= 10;
			HP_LARGE_MIN_NPC	 	= 0;
			HP_LARGE_MAX_NPC	 	= 5;
			HP_LARGE_CHANCE_NPC		= 10;
			DOLL_SOULOFFLAME		= 3;
			DOLL_JUDGEMENT			= 3;
			DOLL_DECAY_POTION		= 3;
			DOLL_PVP				= 40;
		}
	}

	//TODO Fx_Message
	public static MessageInfo Message = new MessageInfo();
	public static class MessageInfo {
		public String GMKAKAO;
		public String GAMESERVERMENT;
		public String PrivateShopChat;
		public String GameServerName;
		public String OpenTimeMont;
		public String MAX_LEVEL_MESSAGE;
		public String GahoMont;
		public String WorldDeleteCleaning;
		public String CRAFT_LIMIT_TIME;
		public String DUNGEON_TIME_UPDATOR;
		public String RANK_TIME_UPDATOR;
		public String RANK_RE_TIME_UPDATOR;
		public String PC_BUFF_MESSAGE;
		public String SPEESOVERCHECKCOUNT_MESSAGE;
		public String NCOIN_MESSAGE;
		public String Bonus_Message1;
		public String Bonus_Message2;
		public String Bonus_Message3;
		public String Bonus_Message4;
		public String Bonus_Message5;

		public MessageInfo() {
			GMKAKAO = "NatureT";
			GAMESERVERMENT = "您好";
			PrivateShopChat = "您好";
			GameServerName = "測試伺服器";
			OpenTimeMont = "開放等待";
			MAX_LEVEL_MESSAGE = "已達到伺服器最高等級，無法再獲得經驗值。";
			GahoMont = "\f3持有高級不朽之庇護，亞丁娜/物品掉落量及機率減少50%。";
			WorldDeleteCleaning = "亞丁世界的地面物品將在稍後被全部刪除。";
			CRAFT_LIMIT_TIME = "限量製作物品數量已重置。";
			DUNGEON_TIME_UPDATOR = "\f2您使用的帳號時間限制副本使用時間已超過。（每天上午6點重置）";
			RANK_TIME_UPDATOR = "\\aG排行榜系統已自動更新。";
			RANK_RE_TIME_UPDATOR = "\\aG排行榜系統已強制更新。";
			PC_BUFF_MESSAGE = "網咖剩餘時間：";
			SPEESOVERCHECKCOUNT_MESSAGE = "\f3疑似開啟超過自動重連的速度，已自動重連。（已報告給管理員）";
			NCOIN_MESSAGE = "";
			Bonus_Message1 = "";
			Bonus_Message2 = "";
			Bonus_Message3 = "";
			Bonus_Message4 = "";
			Bonus_Message5 = "";
		}
	}
	//TODO MagicAdSetting.json
	public static ServerMagicInfo MagicAdSetting = new ServerMagicInfo();
	public static class ServerMagicInfo {
		public boolean SPELLDELAYRUN;
		public int SPELLDELAYOVERPENDING;
		public int PoisonAttack_PC_DMG;
		public int PoisonAttack_PC_Chance;
		public int PoisonAttack_PC_Ms;
		public int Silenpro;
		public int PRORCRITICAL_MAGIC;
		public double MAGICCRITICALDAMAGERATE;
		public int[] POLYARRAY;
		public int SIMLEVELMIN1;
		public int SIMLEVELMAX1;
		public double SIMSIMDMG1;
		public int SIMLEVELMIN2;
		public int SIMLEVELMAX2;
		public double SIMSIMDMG2;
		public int SIMLEVELMIN3;
		public int SIMLEVELMAX3;
		public double SIMSIMDMG3;
		public int SIMLEVELMIN4;
		public int SIMLEVELMAX4;
		public double SIMSIMDMG4;
		public int SIMLEVELMIN5;
		public int SIMLEVELMAX5;
		public double SIMSIMDMG5;
		public int SIMLEVELMIN6;
		public int SIMLEVELMAX6;
		public double SIMSIMDMG6;


		public int TargetMr;
		public int TargetMr1;
		public int TargetMr2;
		public int TargetMr3;
		public int TargetMr4;
		public int TargetMr5;
		public int TargetMr6;

		public double TargetMr_vl;
		public double TargetMr_v2;
		public double TargetMr_v3;
		public double TargetMr_v4;
		public double TargetMr_v5;
		public double TargetMr_v6;
		public double TargetMr_v7;
		public int MANADRAINLIMITPERNPC;




		public double MISSILECRITICALDAMAGERATE;
		public double MELEECRITICALDAMAGERATE;
		public double CHARACTERMAGICHITRATE;
		public double CHARACTERMAGICCRIRATE;


		public double DEATHPOTION;
		public int DRAGON_PEARL_TIME;


		public ServerMagicInfo() {
			SPELLDELAYRUN					= true;
			SPELLDELAYOVERPENDING			= 9;
			TargetMr						= 100;
			TargetMr1						= 200;
			TargetMr2						= 300;
			TargetMr3						= 400;
			TargetMr4						= 500;
			TargetMr5						= 600;
			TargetMr6						= 700;
			TargetMr_vl						= 400;
			TargetMr_v2						= 800;
			TargetMr_v3						= 1600;
			TargetMr_v4						= 3200;
			TargetMr_v5						= 6400;
			TargetMr_v6						= 12800;
			TargetMr_v7						= 31600;
			DEATHPOTION						= 0.02;
			Silenpro						= 250000;
			DRAGON_PEARL_TIME				= 600;

			MAGICCRITICALDAMAGERATE			= 1.0;
			POLYARRAY	 					= new int[] { 945,979,1037,1039,15715,30,94,3865,15673,15719,95,146,15614,2376,2377,3866,3867,3868,3869,3870,3871,3872,2468,3874,3875,3876,185,173,187,183,11358,11396,11397,12225,12226,11399,11398,12227,15638,15635,15636 };
			SIMLEVELMIN1					= 1;
			SIMLEVELMAX1					= 69;
			SIMSIMDMG1						= 1.5;
			SIMLEVELMIN2					= 70;
			SIMLEVELMAX2					= 79;
			SIMSIMDMG2						= 1.8;
			SIMLEVELMIN3					= 80;
			SIMLEVELMAX3					= 81;
			SIMSIMDMG3						= 2.0;
			SIMLEVELMIN4					= 82;
			SIMLEVELMAX4					= 83;
			SIMSIMDMG4						= 2.2;
			SIMLEVELMIN5					= 84;
			SIMLEVELMAX5					= 85;
			SIMSIMDMG5						= 2.5;
			SIMLEVELMIN6					= 86;
			SIMLEVELMAX6					= 90;
			SIMSIMDMG6						= 2.8;

			MISSILECRITICALDAMAGERATE		= 1.5;
			MELEECRITICALDAMAGERATE			= 1.5;
			CHARACTERMAGICHITRATE			= 2.0;
			CHARACTERMAGICCRIRATE			= 1.5;

			PRORCRITICAL_MAGIC				= 10;

			MANADRAINLIMITPERNPC			= 40;
			PoisonAttack_PC_DMG				= 30;
			PoisonAttack_PC_Chance			= 20;
			PoisonAttack_PC_Ms				= 3000;

		}
	}

	// 王族技能外部化
	public static ServerMagicInfo_Prince MagicAdSetting_Prince = new ServerMagicInfo_Prince();
	public static class ServerMagicInfo_Prince {
		public int EMPIREHITTOLEVEL;
		public double EMPIRE;
		public int EMPIRE_LVL;
		public int EMPIRE_LVL1;
		public int EMPIRE_LVL2;
		public int EMPIRE_LVL3;
		public int EMPIRE_LVL4;
		public int EMPIRE_LVL5;
		public int EMPIRE_LVL6;
		public int EMPIRE_LVL7;
		public int EMPIRE_LVL8;
		public int EMPIRE_LVL9;
		public int[] EMPIRE_MS;
		public int[] EMPIRE_MS1;
		public int[] EMPIRE_MS2;
		public int[] EMPIRE_MS3;
		public int[] EMPIRE_MS4;
		public int[] EMPIRE_MS5;
		public int EMPIRE_OVERLORD_LVL;
		public int EMPIRE_OVERLORD_LVL1;
		public int EMPIRE_OVERLORD_LVL2;
		public int EMPIRE_OVERLORD_LVL3;
		public int EMPIRE_OVERLORD_LVL4;
		public int EMPIRE_OVERLORD_LVL5;
		public int EMPIRE_OVERLORD_LVL6;
		public int EMPIRE_OVERLORD_LVL7;
		public int EMPIRE_OVERLORD_LVL8;
		public int EMPIRE_OVERLORD_LVL9;
		public int[] EMPIRE_OVERLORD_MS;
		public int[] EMPIRE_OVERLORD_MS1;
		public int[] EMPIRE_OVERLORD_MS2;
		public int[] EMPIRE_OVERLORD_MS3;
		public int[] EMPIRE_OVERLORD_MS4;
		public int[] EMPIRE_OVERLORD_MS5;
		public int CONQUEROR_STUN_LVL;
		public int CONQUEROR_STUN_LVL1;
		public int CONQUEROR_STUN_LVL2;
		public int CONQUEROR_STUN_LVL3;
		public int CONQUEROR_STUN_LVL4;
		public int CONQUEROR_STUN_LVL5;
		public int CONQUEROR_STUN_LVL6;
		public int CONQUEROR_STUN_LVL7;
		public int CONQUEROR_STUN_LVL8;
		public int CONQUEROR_STUN_LVL9;
		public int[] CONQUEROR_STUN_MS;
		public int[] CONQUEROR_STUN_MS1;
		public int[] CONQUEROR_STUN_MS2;
		public int[] CONQUEROR_STUN_MS3;
		public int[] CONQUEROR_STUN_MS4;
		public int[] CONQUEROR_STUN_MS5;
		public int CONQUEROR_DAMAGE_RATE;
		public int TYRANT_EXCUTE_DMG_RATE;
		public ServerMagicInfo_Prince() {
			EMPIREHITTOLEVEL				= 3;
			EMPIRE							= 50.0;
			EMPIRE_LVL						= -5;
			EMPIRE_LVL1						= -5;
			EMPIRE_LVL2						= -3;
			EMPIRE_LVL3						= -2;
			EMPIRE_LVL4						= 2;
			EMPIRE_LVL5						= 3;
			EMPIRE_LVL6						= 5;
			EMPIRE_LVL7						= 5;
			EMPIRE_LVL8						= 10;
			EMPIRE_LVL9						= 10;
			EMPIRE_MS	 					= new int[] { 1000, 2000, 3000 };
			EMPIRE_MS1 						= new int[] { 1000, 2000, 3000, 4000 };
			EMPIRE_MS2 						= new int[] { 1000, 2000, 3000, 4000, 5000 };
			EMPIRE_MS3 						= new int[] { 3000, 4000, 5000, 6000 };
			EMPIRE_MS4 						= new int[] { 4000, 5000, 6000 };
			EMPIRE_MS5 						= new int[] { 5000, 6000 };
			EMPIRE_OVERLORD_LVL				= -5;
			EMPIRE_OVERLORD_LVL1			= -5;
			EMPIRE_OVERLORD_LVL2			= -3;
			EMPIRE_OVERLORD_LVL3			= -2;
			EMPIRE_OVERLORD_LVL4			= 2;
			EMPIRE_OVERLORD_LVL5			= 3;
			EMPIRE_OVERLORD_LVL6			= 5;
			EMPIRE_OVERLORD_LVL7			= 5;
			EMPIRE_OVERLORD_LVL8			= 10;
			EMPIRE_OVERLORD_LVL9			= 10;
			EMPIRE_OVERLORD_MS	 			= new int[] { 1000, 2000, 3000 };
			EMPIRE_OVERLORD_MS1 			= new int[] { 1000, 2000, 3000, 4000 };
			EMPIRE_OVERLORD_MS2 			= new int[] { 1000, 2000, 3000, 4000, 5000 };
			EMPIRE_OVERLORD_MS3 			= new int[] { 3000, 4000, 5000, 6000 };
			EMPIRE_OVERLORD_MS4 			= new int[] { 4000, 5000, 6000 };
			EMPIRE_OVERLORD_MS5 			= new int[] { 5000, 6000 };

			CONQUEROR_STUN_LVL				= -5;
			CONQUEROR_STUN_LVL1				= -5;
			CONQUEROR_STUN_LVL2				= -3;
			CONQUEROR_STUN_LVL3				= -2;
			CONQUEROR_STUN_LVL4				= 2;
			CONQUEROR_STUN_LVL5				= 3;
			CONQUEROR_STUN_LVL6				= 5;
			CONQUEROR_STUN_LVL7				= 5;
			CONQUEROR_STUN_LVL8				= 10;
			CONQUEROR_STUN_LVL9				= 10;
			CONQUEROR_STUN_MS	 			= new int[] { 1000, 2000, 3000 };
			CONQUEROR_STUN_MS1 				= new int[] { 1000, 2000, 3000, 4000 };
			CONQUEROR_STUN_MS2 				= new int[] { 1000, 2000, 3000, 4000, 5000 };
			CONQUEROR_STUN_MS3 				= new int[] { 3000, 4000, 5000, 6000 };
			CONQUEROR_STUN_MS4 				= new int[] { 4000, 5000, 6000 };
			CONQUEROR_STUN_MS5 				= new int[] { 5000, 6000 };
			CONQUEROR_DAMAGE_RATE			= 100;
			TYRANT_EXCUTE_DMG_RATE			= 150;
		}
	}
	// 檢查技能外部化
	public static ServerMagicInfo_Fencer MagicAdSetting_Fencer = new ServerMagicInfo_Fencer();
	public static class ServerMagicInfo_Fencer {

		public int PANTHERA_LVL;
		public int PANTHERA_LVL1;
		public int PANTHERA_LVL2;
		public int PANTHERA_LVL3;
		public int PANTHERA_LVL4;
		public int PANTHERA_LVL5;
		public int PANTHERA_LVL6;
		public int PANTHERA_LVL7;
		public int PANTHERA_LVL8;
		public int PANTHERA_LVL9;
		public int[] PANTHERA_MS;
		public int[] PANTHERA_MS1;
		public int[] PANTHERA_MS2;
		public int[] PANTHERA_MS3;
		public int[] PANTHERA_MS4;
		public int[] PANTHERA_MS5;
		public int PANTHERA_SHOCK_LVL;
		public int PANTHERA_SHOCK_LVL1;
		public int PANTHERA_SHOCK_LVL2;
		public int PANTHERA_SHOCK_LVL3;
		public int PANTHERA_SHOCK_LVL4;
		public int PANTHERA_SHOCK_LVL5;
		public int PANTHERA_SHOCK_LVL6;
		public int PANTHERA_SHOCK_LVL7;
		public int PANTHERA_SHOCK_LVL8;
		public int PANTHERA_SHOCK_LVL9;
		public int[] PANTHERA_SHOCK_MS;
		public int[] PANTHERA_SHOCK_MS1;
		public int[] PANTHERA_SHOCK_MS2;
		public int[] PANTHERA_SHOCK_MS3;
		public int[] PANTHERA_SHOCK_MS4;
		public int[] PANTHERA_SHOCK_MS5;

		public int FLAME_PASSIVE;
		public int FLAME_PASSIVE_DMG;
		public int PARADOXPROBABILITY;
		public int GROWS_PASSIVE;
		public int RAGEPROBABILITY;
		public double RAGEDMG;
		public int SURVIVE_PER;
		public int SURVIVE_ADDITION;

		public ServerMagicInfo_Fencer() {

			PANTHERA_LVL					= -5;
			PANTHERA_LVL1					= -5;
			PANTHERA_LVL2					= -3;
			PANTHERA_LVL3					= -2;
			PANTHERA_LVL4					= 2;
			PANTHERA_LVL5					= 3;
			PANTHERA_LVL6					= 5;
			PANTHERA_LVL7					= 5;
			PANTHERA_LVL8					= 10;
			PANTHERA_LVL9					= 10;
			PANTHERA_MS	 					= new int[] { 1000 };
			PANTHERA_MS1 					= new int[] { 1000 };
			PANTHERA_MS2 					= new int[] { 1000, 2000 };
			PANTHERA_MS3 					= new int[] { 1000, 2000 };
			PANTHERA_MS4 					= new int[] { 2000, 3000 };
			PANTHERA_MS5 					= new int[] { 2000, 3000 };
			PANTHERA_SHOCK_LVL				= -5;
			PANTHERA_SHOCK_LVL1				= -5;
			PANTHERA_SHOCK_LVL2				= -3;
			PANTHERA_SHOCK_LVL3				= -2;
			PANTHERA_SHOCK_LVL4				= 2;
			PANTHERA_SHOCK_LVL5				= 3;
			PANTHERA_SHOCK_LVL6				= 5;
			PANTHERA_SHOCK_LVL7				= 5;
			PANTHERA_SHOCK_LVL8				= 10;
			PANTHERA_SHOCK_LVL9				= 10;
			PANTHERA_SHOCK_MS	 			= new int[] { 1000, 2000 };
			PANTHERA_SHOCK_MS1 				= new int[] { 1000, 2000 };
			PANTHERA_SHOCK_MS2 				= new int[] { 2000, 3000 };
			PANTHERA_SHOCK_MS3 				= new int[] { 2000, 3000, 4000 };
			PANTHERA_SHOCK_MS4 				= new int[] { 2000, 3000, 4000 };
			PANTHERA_SHOCK_MS5 				= new int[] { 3000, 4000 };
			FLAME_PASSIVE					= 5;
			FLAME_PASSIVE_DMG				= 6;
			PARADOXPROBABILITY				= 500000;
			GROWS_PASSIVE					= 20;
			RAGEPROBABILITY					= 150000;
			RAGEDMG							= 1.5;
			SURVIVE_PER						= 30;
			SURVIVE_ADDITION				= 30;
		}
	}

	// 黃金槍騎技能外部化
	public static ServerMagicInfo_Lancer MagicAdSetting_Lancer = new ServerMagicInfo_Lancer();
	public static class ServerMagicInfo_Lancer {
		public double SpearMode;
		public int SPEARMODE_DISTANCE_1;
		public double SPEARMODE_DISTANCE_DMG_1;
		public int SPEARMODE_DISTANCE_2;
		public double SPEARMODE_DISTANCE_DMG_2;
		public int SPEARMODE_DISTANCE_3;
		public double SPEARMODE_DISTANCE_DMG_3;
		public int SPEARMODE_DISTANCE_4;
		public double SPEARMODE_DISTANCE_DMG_4;
		public int SPEARMODE_DISTANCE_5;
		public double SPEARMODE_DISTANCE_DMG_5;


		public int CRUEL_LVL;
		public int CRUEL_LVL1;
		public int CRUEL_LVL2;
		public int CRUEL_LVL3;
		public int CRUEL_LVL4;
		public int CRUEL_LVL5;
		public int CRUEL_LVL6;
		public int CRUEL_LVL7;
		public int CRUEL_LVL8;
		public int CRUEL_LVL9;
		public int[] CRUEL_MS;
		public int[] CRUEL_MS1;
		public int[] CRUEL_MS2;
		public int[] CRUEL_MS3;
		public int[] CRUEL_MS4;
		public int[] CRUEL_MS5;
		public int CRUEL_CONBICTION_LVL;
		public int CRUEL_CONBICTION_LVL1;
		public int CRUEL_CONBICTION_LVL2;
		public int CRUEL_CONBICTION_LVL3;
		public int CRUEL_CONBICTION_LVL4;
		public int CRUEL_CONBICTION_LVL5;
		public int CRUEL_CONBICTION_LVL6;
		public int CRUEL_CONBICTION_LVL7;
		public int CRUEL_CONBICTION_LVL8;
		public int CRUEL_CONBICTION_LVL9;
		public int[] CRUEL_CONBICTION_MS;
		public int[] CRUEL_CONBICTION_MS1;
		public int[] CRUEL_CONBICTION_MS2;
		public int[] CRUEL_CONBICTION_MS3;
		public int[] CRUEL_CONBICTION_MS4;
		public int[] CRUEL_CONBICTION_MS5;
		public int PRESHER_DEATH_RECALL_DMG_MIN;
		public int PRESHER_DEATH_RECALL_DMG;

		public int PRESHER_DEATH_RECALL_LVL;
		public int PRESHER_DEATH_RECALL_LVL1;
		public int PRESHER_DEATH_RECALL_LVL2;
		public int PRESHER_DEATH_RECALL_LVL3;
		public int PRESHER_DEATH_RECALL_LVL4;
		public int PRESHER_DEATH_RECALL_LVL5;
		public int PRESHER_DEATH_RECALL_LVL6;
		public int PRESHER_DEATH_RECALL_LVL7;
		public int PRESHER_DEATH_RECALL_LVL8;
		public int PRESHER_DEATH_RECALL_LVL9;
		public int[] PRESHER_DEATH_RECALL_MS;
		public int[] PRESHER_DEATH_RECALL_MS1;
		public int[] PRESHER_DEATH_RECALL_MS2;
		public int[] PRESHER_DEATH_RECALL_MS3;
		public int[] PRESHER_DEATH_RECALL_MS4;
		public int[] PRESHER_DEATH_RECALL_MS5;
		public double PRESHER_PCPCDMG;
		public double PRESHER_ETCPCDMG;
		public double TACTICAL_ADVANCE_VAL;
		public int DODGE_BREAK;
		public double DEADLY_STRIKE;
		public int DEADLY_STRIKE_PRO;
		public int VENGEANCE_PERCENT;
		public int VENGEANCE_REDUCTION;
		public int VENGEANCE_HIT_PERCENT;
		public int VENGEANCE_HIT_REDUCTION;
		public int MAELSTROM;
		public int SPEARMODE_PVP_DMG;

		public ServerMagicInfo_Lancer() {
			SPEARMODE_DISTANCE_1			= 1;
			SPEARMODE_DISTANCE_DMG_1		= 0.9;
			SPEARMODE_DISTANCE_2			= 2;
			SPEARMODE_DISTANCE_DMG_2		= 0.8;
			SPEARMODE_DISTANCE_3			= 3;
			SPEARMODE_DISTANCE_DMG_3		= 0.7;
			SPEARMODE_DISTANCE_4			= 4;
			SPEARMODE_DISTANCE_DMG_4		= 0.6;
			SPEARMODE_DISTANCE_5			= 5;
			SPEARMODE_DISTANCE_DMG_5		= 0.5;
			SPEARMODE_PVP_DMG				= 10;
			DODGE_BREAK						= 20;
			DEADLY_STRIKE					= 2.5;
			DEADLY_STRIKE_PRO				= 10;
			VENGEANCE_PERCENT				= 5;
			VENGEANCE_REDUCTION				= 20;
			VENGEANCE_HIT_PERCENT			= 10;
			VENGEANCE_HIT_REDUCTION			= 30;
			MAELSTROM						= 20;
			PRESHER_DEATH_RECALL_DMG_MIN	= 400;
			PRESHER_DEATH_RECALL_DMG		= 500;
			CRUEL_LVL						= -5;
			CRUEL_LVL1						= -5;
			CRUEL_LVL2						= -3;
			CRUEL_LVL3						= -2;
			CRUEL_LVL4						= 2;
			CRUEL_LVL5						= 3;
			CRUEL_LVL6						= 5;
			CRUEL_LVL7						= 5;
			CRUEL_LVL8						= 10;
			CRUEL_LVL9						= 10;
			CRUEL_MS						= new int[] { 600, 1000, 1400, 1800, 2200 };
			CRUEL_MS1						= new int[] { 800, 1200, 1600, 2000, 2400 };
			CRUEL_MS2						= new int[] { 1000, 1400, 1800, 2200, 2600 };
			CRUEL_MS3						= new int[] { 1200, 1600, 2000, 2400, 2800 };
			CRUEL_MS4						= new int[] { 1400, 1800, 2200, 2600, 3000 };
			CRUEL_MS5						= new int[] { 1600, 2000, 2400, 2800, 3000 };
			CRUEL_CONBICTION_LVL			= -5;
			CRUEL_CONBICTION_LVL1 			= -5;
			CRUEL_CONBICTION_LVL2 			= -3;
			CRUEL_CONBICTION_LVL3 			= -2;
			CRUEL_CONBICTION_LVL4 			= 2;
			CRUEL_CONBICTION_LVL5 			= 3;
			CRUEL_CONBICTION_LVL6 			= 5;
			CRUEL_CONBICTION_LVL7 			= 5;
			CRUEL_CONBICTION_LVL8 			= 10;
			CRUEL_CONBICTION_LVL9 			= 10;
			CRUEL_CONBICTION_MS				= new int[] { 600, 1000, 1400, 1800, 2200, 2600, 3000 };
			CRUEL_CONBICTION_MS1			= new int[] { 800, 1200, 1600, 2000, 2400, 2800, 3200 };
			CRUEL_CONBICTION_MS2			= new int[] { 1000, 1400, 1800, 2200, 2600, 3000, 3400 };
			CRUEL_CONBICTION_MS3			= new int[] { 1200, 1600, 2000, 2400, 2800, 3200, 3600 };
			CRUEL_CONBICTION_MS4			= new int[] { 1400, 1800, 2200, 2600, 3000, 3400, 3800 };
			CRUEL_CONBICTION_MS5			= new int[] { 1600, 2000, 2400, 2800, 3200, 3600, 4000 };
			PRESHER_DEATH_RECALL_LVL		= -5;
			PRESHER_DEATH_RECALL_LVL1		= -5;
			PRESHER_DEATH_RECALL_LVL2		= -3;
			PRESHER_DEATH_RECALL_LVL3		= -2;
			PRESHER_DEATH_RECALL_LVL4		= 2;
			PRESHER_DEATH_RECALL_LVL5		= 3;
			PRESHER_DEATH_RECALL_LVL6		= 5;
			PRESHER_DEATH_RECALL_LVL7		= 5;
			PRESHER_DEATH_RECALL_LVL8		= 10;
			PRESHER_DEATH_RECALL_LVL9		= 10;
			PRESHER_DEATH_RECALL_MS			= new int[] { 600, 1000, 1400, 1800, 2200, 2600, 3000 };
			PRESHER_DEATH_RECALL_MS1		= new int[] { 800, 1200, 1600, 2000, 2400, 2800, 3200 };
			PRESHER_DEATH_RECALL_MS2		= new int[] { 1000, 1400, 1800, 2200, 2600, 3000, 3400 };
			PRESHER_DEATH_RECALL_MS3		= new int[] { 1200, 1600, 2000, 2400, 2800, 3200, 3600 };
			PRESHER_DEATH_RECALL_MS4		= new int[] { 1400, 1800, 2200, 2600, 3000, 3400, 3800 };
			PRESHER_DEATH_RECALL_MS5		= new int[] { 1600, 2000, 2400, 2800, 3200, 3600, 4000 };
			SpearMode						= 0.5;
			PRESHER_PCPCDMG					= 0.50;
			PRESHER_ETCPCDMG				= 0.10;
			TACTICAL_ADVANCE_VAL			= 2;

		}
	}
	//戰士技能外部化
	public static ServerMagicInfo_Warrior MagicAdSetting_Warrior = new ServerMagicInfo_Warrior();
	public static class ServerMagicInfo_Warrior {
		public double DESPERADO;
		public int[] DESPERADO_MS;
		public int[] DESPERADO_ABSOLUTE_MS;
		public int[] DEMOLITION_MS;
		public int[] POWERRIP_MS;
		public int TITANROCKPRO;
		public int TITANMAGICPRO;
		public int TITANBULLETPRO;
		public int TITANRISINGPRO;
		public int TOMAHAWK_HUNTER_MIN_HP;
		public int TOMAHAWK_HUNTER_MAX_HP;
		public double TITANBEAST_REDUC_PER;
		public double ROCKDMG;
		public int CRASHFO;
		public double CRASHDMG;
		public int FURYFO;
		public double FURYDMG;
		public int DEMOLITIONPRO;
		public int TEMPEST_DAMAGE_RATE;
		public int[] TEMPEST_SKILL_RND_TIME;


		public ServerMagicInfo_Warrior() {

			DESPERADO						= 5.0;

			DESPERADO_MS					= new int[] { 2000, 2000, 3000, 3000, 3000, 4000, 4000, 5000 };
			DESPERADO_ABSOLUTE_MS			= new int[] { 3000, 3000, 4000, 4000, 4000, 5000, 5000, 6000, 6000 };
			DEMOLITION_MS					= new int[] { 1000, 2000, 2000, 2000, 3000, 3000, 3000, 4000, 4000, 5000 };
			POWERRIP_MS 					= new int[] { 2000, 2000, 3000, 3000, 3000, 4000, 4000, 5000, 6000 };

			TEMPEST_SKILL_RND_TIME			= new int[] {2000, 2000, 2000, 3000, 3000, 4000, 4000, 4000};
			TITANROCKPRO					= 25;
			TITANMAGICPRO					= 25;
			TITANBULLETPRO					= 25;
			TITANRISINGPRO					= 10;
			TITANBEAST_REDUC_PER			= 0.65;
			TOMAHAWK_HUNTER_MIN_HP			= 1;
			TOMAHAWK_HUNTER_MAX_HP			= 10;

			ROCKDMG							= 1.5;
			CRASHFO							= 23;
			CRASHDMG						= 1.0;
			FURYFO							= 15;
			FURYDMG							= 2.0;
			DEMOLITIONPRO					= 10000;
			TEMPEST_DAMAGE_RATE				= 100;

		}
	}
	// 妖精技能外部化
	public static ServerMagicInfo_Elf MagicAdSetting_Elf = new ServerMagicInfo_Elf();
	public static class ServerMagicInfo_Elf {
		public double TripleArrow_dmg_pc;
		public double TripleArrow_dmg_npc;
		public double TripleArrow_boost_dmg_pc;
		public double TripleArrow_boost_dmg_npc;
		public double ERASEMAGIC;
		public double WINDSHACKLET;
		public double EARTHBINDT;
		public double STRIKERGALET;
		public double POLLUTEWATERT;
		public double CYCLONEVAL;
		public int[] INFERNOEFFECTS;
		public int GLORYEARTH_CHANCE;
		public int GLORYEARTH_DMG;
		public double LIBERATION_CO;
		public int[] STRIKERGAILSHOT_MS;
		public int[] TRIPLE_STUN_TIME_ARRAY;
		public int ELFTURNCHANT;
		public double COMBINECHANCE;

		public ServerMagicInfo_Elf() {

			TripleArrow_dmg_pc				= 0.70;
			TripleArrow_dmg_npc				= 0.70;
			TripleArrow_boost_dmg_pc		= 0.10;
			TripleArrow_boost_dmg_npc		= 0.10;
			ERASEMAGIC						= 2.0;
			WINDSHACKLET					= 2.0;
			EARTHBINDT						= 2.0;
			STRIKERGALET					= 2.0;
			POLLUTEWATERT					= 2.0;
			CYCLONEVAL						= 1.5;
			INFERNOEFFECTS					= new int[] {17561,17563,17565,17567};
			ELFTURNCHANT					= 40;
			GLORYEARTH_CHANCE				= 5;
			GLORYEARTH_DMG					= 30;
			LIBERATION_CO					= 1.5;
			STRIKERGAILSHOT_MS				= new int[] { 2100, 2300, 2500, 2700, 2900, 3100, 3300, 3500, 3700, 3900, 4000, 4200, 4400, 4600, 4800, 5000 };
			TRIPLE_STUN_TIME_ARRAY			= new int[] { 1200, 1600, 2000, 2400, 2800, 3200, 3600, 4000 };
			COMBINECHANCE					= 15.0;

		}
	}
	// 法師技能外部化
	public static ServerMagicInfo_Wizard MagicAdSetting_Wizard = new ServerMagicInfo_Wizard();
	public static class ServerMagicInfo_Wizard {
		public int IMMUNELEVEL;
		public double IMMUNEDMG;
		public int IMMUNELEVEL1;
		public double IMMUNEDMG1;
		public int IMMUNELEVEL2;
		public double IMMUNEDMG2;
		public int IMMUNELEVEL3;
		public double IMMUNEDMG3;
		public int IMMUNELEVEL4;
		public double IMMUNEDMG4;
		public int IMMUNELEVEL5;
		public double IMMUNEDMG5;
		public int IMMUNELEVEL6;
		public double IMMUNEDMG6;
		public double IMMUNEDMG7;
		public int DISINTCHAOTICWEIGHT;
		public int DISINTLAWFULWEIGHT;
		public int DISINTMINDMG;
		public int DISINTMAXDMG;
		public int ETERNITIMINDMG;
		public int ETERNITIMAXDMG;
		public int[] ETERNITI_MS;
		public int ANTIDISINTEGRATETIME;
		public double ANTIDISINTEGRATE;
		public double MAGIC_CONTINUE_DMG_PER;
		public int DEVINE_PROTECTION_BARRIER;
		public int[] DISINTEGRATEMS;
		public int[] DISINTEGRATEMS1;
		public int[] DISINTEGRATEMS2;
		public int[] DISINTEGRATEMS3;
		public int[] DISINTEGRATEMS4;
		public int[] DISINTEGRATEMS5;
		public int[] DISINTEGRATEMS6;
		public int ARTERIALCIRCLE_REDUC;
		public int ARTERIALCIRCLE_CHANCE;
		public double SUMMON_LEVEL_ADDDMG;
		public double SUMMON_SP_ADDDMG;
		public double SUMMON_LEVEL_ADDHIT;
		public double SUMMON_SP_ADDHIT;
		public int NEMESISDAMAGE;
		public ServerMagicInfo_Wizard() {
			NEMESISDAMAGE					= 10;
			IMMUNELEVEL						= 87;
			IMMUNEDMG						= 0.50;
			IMMUNELEVEL1					= 86;
			IMMUNEDMG1						= 0.40;
			IMMUNELEVEL2					= 85;
			IMMUNEDMG2						= 0.20;
			IMMUNELEVEL3					= 80;
			IMMUNEDMG3						= 0.17;
			IMMUNELEVEL4					= 75;
			IMMUNEDMG4						= 0.12;
			IMMUNELEVEL5					= 70;
			IMMUNEDMG5						= 0.09;
			IMMUNELEVEL6					= 65;
			IMMUNEDMG6						= 0.08;
			IMMUNEDMG7						= 0.07;

			DISINTCHAOTICWEIGHT				= 1500;
			DISINTLAWFULWEIGHT				= 2500;
			DISINTMINDMG					= 200;
			DISINTMAXDMG					= 1500;
			ETERNITIMINDMG					= 150;
			ETERNITIMAXDMG					= 1200;
			ETERNITI_MS						= new int[] { 1000, 1000, 1000, 2000, 2000, 3000, 3000, 4000 };
			ANTIDISINTEGRATETIME			= 3000;
			ANTIDISINTEGRATE				= 1.0;

			DISINTEGRATEMS                  = new int[] { 1000, 1000, 1000, 2000, 2000, 3000, 3000, 4000 };
			DISINTEGRATEMS1                 = new int[] { 1000, 1000, 1000, 2000, 2000, 3000, 3000, 4000 };
			DISINTEGRATEMS2                 = new int[] { 1000, 1000, 1000, 2000, 2000, 3000, 3000, 4000 };
			DISINTEGRATEMS3                 = new int[] { 1000, 1000, 1000, 2000, 2000, 3000, 3000, 4000 };
			DISINTEGRATEMS4                 = new int[] { 1000, 1000, 1000, 2000, 2000, 3000, 3000, 4000 };
			DISINTEGRATEMS5                 = new int[] { 1000, 1000, 1000, 2000, 2000, 3000, 3000, 4000 };
			DISINTEGRATEMS6                 = new int[] { 1000, 1000, 1000, 2000, 2000, 3000, 3000, 4000 };
			ARTERIALCIRCLE_REDUC            = 50;
			ARTERIALCIRCLE_CHANCE           = 5;
			DEVINE_PROTECTION_BARRIER       = 1000;
			MAGIC_CONTINUE_DMG_PER          = 0.01;
			SUMMON_LEVEL_ADDDMG             = 1.00;
			SUMMON_SP_ADDDMG                = 1.00;
			SUMMON_LEVEL_ADDHIT             = 1.00;
			SUMMON_SP_ADDHIT                = 1.00;
		}
	}
	// 龍騎士技能外部化
	public static ServerMagicInfo_DragonKnight MagicAdSetting_DragonKnight = new ServerMagicInfo_DragonKnight();
	public static class ServerMagicInfo_DragonKnight {
		public double FouSlayer_dmg_pc;
		public double FouSlayer_dmg_npc;
		public double THUNDERGRABT;
		public int RAMPAGE_P;
		public double RAMPAGE_D;
		public int HALPASDMGX;
		public int HALPASPROBABILITY;
		public double HORRORHITTOLEVEL;
		public int DESTROYHITTOLEVEL;
		public int FouSlayer_Add_per;
		public int BEHEMOTH_LVL, BEHEMOTH_LVL1, BEHEMOTH_LVL2, BEHEMOTH_LVL3, BEHEMOTH_LVL4, BEHEMOTH_LVL5, BEHEMOTH_LVL6, BEHEMOTH_LVL7, BEHEMOTH_LVL8, BEHEMOTH_LVL9;
		public int[] BEHEMOTH_MS, BEHEMOTH_MS1, BEHEMOTH_MS2, BEHEMOTH_MS3, BEHEMOTH_MS4, BEHEMOTH_MS5;
		public int FOU_TURN_REUSE_TIME;

		public ServerMagicInfo_DragonKnight() {
			HORRORHITTOLEVEL				= 3.0;
			DESTROYHITTOLEVEL				= 3;
			FouSlayer_dmg_pc				= 0.70;
			FouSlayer_dmg_npc				= 0.70;

			THUNDERGRABT					= 2.0;

			HALPASDMGX						= 3;
			HALPASPROBABILITY				= 200000;

			RAMPAGE_P						= 20;
			RAMPAGE_D						= 2.0;
			FouSlayer_Add_per				= 10;
			BEHEMOTH_LVL					= -5;
			BEHEMOTH_LVL1					= -5;
			BEHEMOTH_LVL2					= -3;
			BEHEMOTH_LVL3					= -2;
			BEHEMOTH_LVL4					= -1;
			BEHEMOTH_LVL5					= 0;
			BEHEMOTH_LVL6					= 0;
			BEHEMOTH_LVL7					= 1;
			BEHEMOTH_LVL8					= 3;
			BEHEMOTH_LVL9					= 3;
			BEHEMOTH_MS						= new int[] {1000};
			BEHEMOTH_MS1					= new int[] {1000,2000};
			BEHEMOTH_MS2					= new int[] {1000,2000};
			BEHEMOTH_MS3					= new int[] {1000,2000};
			BEHEMOTH_MS4					= new int[] {1000,2000};
			BEHEMOTH_MS5					= new int[] {1000,2000};
			FOU_TURN_REUSE_TIME				= 6000;



		}
	}
	// 騎士技能外部化
	public static ServerMagicInfo_Knight MagicAdSetting_Knight = new ServerMagicInfo_Knight();
	public static class ServerMagicInfo_Knight {
		public int STUNHITTOLEVEL;
		public double SHOCKSTUN;
		public int SHOCKSTUN_LVL;
		public int SHOCKSTUN_LVL1;
		public int SHOCKSTUN_LVL2;
		public int SHOCKSTUN_LVL3;
		public int SHOCKSTUN_LVL4;
		public int SHOCKSTUN_LVL5;
		public int SHOCKSTUN_LVL6;
		public int SHOCKSTUN_LVL7;
		public int SHOCKSTUN_LVL8;
		public int SHOCKSTUN_LVL9;
		public int[] SHOCKSTUN_MS;
		public int[] SHOCKSTUN_MS1;
		public int[] SHOCKSTUN_MS2;
		public int[] SHOCKSTUN_MS3;
		public int[] SHOCKSTUN_MS4;
		public int[] SHOCKSTUN_MS5;
		public int FORCESTUN_LVL;
		public int FORCESTUN_LVL1;
		public int FORCESTUN_LVL2;
		public int FORCESTUN_LVL3;
		public int FORCESTUN_LVL4;
		public int FORCESTUN_LVL5;
		public int FORCESTUN_LVL6;
		public int FORCESTUN_LVL7;
		public int FORCESTUN_LVL8;
		public int FORCESTUN_LVL9;
		public int[] FORCESTUN_MS;
		public int[] FORCESTUN_MS1;
		public int[] FORCESTUN_MS2;
		public int[] FORCESTUN_MS3;
		public int[] FORCESTUN_MS4;
		public int[] FORCESTUN_MS5;
		public int SHOCKATTACK_LVL;
		public int SHOCKATTACK_LVL1;
		public int SHOCKATTACK_LVL2;
		public int SHOCKATTACK_LVL3;
		public int SHOCKATTACK_LVL4;
		public int SHOCKATTACK_LVL5;
		public int SHOCKATTACK_LVL6;
		public int SHOCKATTACK_LVL7;
		public int SHOCKATTACK_LVL8;
		public int SHOCKATTACK_LVL9;
		public int[] SHOCKATTACK_MS;
		public int[] SHOCKATTACK_MS1;
		public int[] SHOCKATTACK_MS2;
		public int[] SHOCKATTACK_MS3;
		public int[] SHOCKATTACK_MS4;
		public int[] SHOCKATTACK_MS5;
		public int COUNTER_VETERAN;
		public int COUNTER_VETERAN_LV;
		public int COUNTER_VETERAN_LV1;
		public int COUNTER_VETERAN_LV2;
		public int COUNTER_VETERAN_LV3;
		public int COUNTER_VETERAN_LV4;
		public int COUNTER_VETERAN_LV5;
		public double COUNTER;
		public double BLOWATTACKDMG;

		public ServerMagicInfo_Knight() {

			STUNHITTOLEVEL					= 3;
			SHOCKSTUN						= 50.0;
			SHOCKSTUN_LVL					= -5;
			SHOCKSTUN_LVL1					= -5;
			SHOCKSTUN_LVL2					= -3;
			SHOCKSTUN_LVL3					= -2;
			SHOCKSTUN_LVL4					= 2;
			SHOCKSTUN_LVL5					= 3;
			SHOCKSTUN_LVL6					= 5;
			SHOCKSTUN_LVL7					= 5;
			SHOCKSTUN_LVL8					= 10;
			SHOCKSTUN_LVL9					= 10;
			SHOCKSTUN_MS 					= new int[] { 1000, 2000, 3000 };
			SHOCKSTUN_MS1 					= new int[] { 1000, 2000, 3000, 4000 };
			SHOCKSTUN_MS2 					= new int[] { 1000, 2000, 3000, 4000 };
			SHOCKSTUN_MS3 					= new int[] { 2000, 3000, 4000, 5000 };
			SHOCKSTUN_MS4 					= new int[] { 3000, 5000, 6000 };
			SHOCKSTUN_MS5 					= new int[] { 4000, 5000, 6000 };
			FORCESTUN_LVL					= -5;
			FORCESTUN_LVL1					= -5;
			FORCESTUN_LVL2					= -3;
			FORCESTUN_LVL3					= -2;
			FORCESTUN_LVL4					= 2;
			FORCESTUN_LVL5					= 3;
			FORCESTUN_LVL6					= 5;
			FORCESTUN_LVL7					= 5;
			FORCESTUN_LVL8					= 10;
			FORCESTUN_LVL9					= 10;
			FORCESTUN_MS 					= new int[] { 2000, 3000, 4000 };
			FORCESTUN_MS1 					= new int[] { 3000, 4000, 5000 };
			FORCESTUN_MS2 					= new int[] { 3000, 4000, 5000, 6000 };
			FORCESTUN_MS3 					= new int[] { 4000, 5000, 6000, 7000 };
			FORCESTUN_MS4 					= new int[] { 5000, 6000, 7000 };
			FORCESTUN_MS5 					= new int[] { 5000, 6000, 7000 };
			SHOCKATTACK_LVL					= -5;
			SHOCKATTACK_LVL1				= -5;
			SHOCKATTACK_LVL2				= -3;
			SHOCKATTACK_LVL3				= -2;
			SHOCKATTACK_LVL4				= 2;
			SHOCKATTACK_LVL5				= 3;
			SHOCKATTACK_LVL6				= 5;
			SHOCKATTACK_LVL7				= 5;
			SHOCKATTACK_LVL8				= 10;
			SHOCKATTACK_LVL9				= 10;
			SHOCKATTACK_MS 					= new int[] { 2000, 3000, 4000 };
			SHOCKATTACK_MS1 				= new int[] { 3000, 4000, 5000 };
			SHOCKATTACK_MS2 				= new int[] { 3000, 4000, 5000, 6000 };
			SHOCKATTACK_MS3 				= new int[] { 4000, 5000, 6000, 7000 };
			SHOCKATTACK_MS4 				= new int[] { 5000, 6000, 7000 };
			SHOCKATTACK_MS5 				= new int[] { 5000, 6000, 7000 };

			COUNTER_VETERAN					= 0;
			COUNTER_VETERAN_LV				= 84;
			COUNTER_VETERAN_LV1				= 85;
			COUNTER_VETERAN_LV2				= 89;
			COUNTER_VETERAN_LV3				= 90;
			COUNTER_VETERAN_LV4				= 99;
			COUNTER_VETERAN_LV5				= 84;
			COUNTER							= 1.5;
			BLOWATTACKDMG					= 1.1;

		}
	}
	//幻術師技能外包
	public static ServerMagicInfo_Illusion MagicAdSetting_Illusion = new ServerMagicInfo_Illusion();
	public static class ServerMagicInfo_Illusion {
		public int ENSNARE_LVL;
		public int ENSNARE_LVL1;
		public int ENSNARE_LVL2;
		public int ENSNARE_LVL3;
		public int ENSNARE_LVL4;
		public int ENSNARE_LVL5;
		public int ENSNARE_LVL6;
		public int ENSNARE_LVL7;
		public int ENSNARE_LVL8;
		public int ENSNARE_LVL9;
		public int[] ENSNARE_MS;
		public int[] ENSNARE_MS1;
		public int[] ENSNARE_MS2;
		public int[] ENSNARE_MS3;
		public int[] ENSNARE_MS4;
		public int[] ENSNARE_MS5;
		public int OSIRIS_LVL;
		public int OSIRIS_LVL1;
		public int OSIRIS_LVL2;
		public int OSIRIS_LVL3;
		public int OSIRIS_LVL4;
		public int OSIRIS_LVL5;
		public int OSIRIS_LVL6;
		public int OSIRIS_LVL7;
		public int OSIRIS_LVL8;
		public int OSIRIS_LVL9;
		public int[] OSIRIS_MS;
		public int[] OSIRIS_MS1;
		public int[] OSIRIS_MS2;
		public int[] OSIRIS_MS3;
		public int[] OSIRIS_MS4;
		public int[] OSIRIS_MS5;
		public double BONEBREAKPRO;
		public double CONFUSIONPHANTASMT;
		public int BONE_BREAK_RAST_MS;

		public ServerMagicInfo_Illusion() {

			ENSNARE_LVL						= -5;
			ENSNARE_LVL1					= -5;
			ENSNARE_LVL2					= -3;
			ENSNARE_LVL3					= -2;
			ENSNARE_LVL4					= 2;
			ENSNARE_LVL5					= 3;
			ENSNARE_LVL6					= 5;
			ENSNARE_LVL7					= 5;
			ENSNARE_LVL8					= 10;
			ENSNARE_LVL9					= 10;
			ENSNARE_MS 						= new int[] { 2000, 3000, 4000 };
			ENSNARE_MS1 					= new int[] { 3000, 4000, 5000 };
			ENSNARE_MS2 					= new int[] { 3000, 4000, 5000, 6000 };
			ENSNARE_MS3 					= new int[] { 4000, 5000, 6000, 7000 };
			ENSNARE_MS4 					= new int[] { 5000, 6000, 7000 };
			ENSNARE_MS5 					= new int[] { 5000, 6000, 7000 };
			OSIRIS_LVL						= -5;
			OSIRIS_LVL1						= -5;
			OSIRIS_LVL2						= -3;
			OSIRIS_LVL3						= -2;
			OSIRIS_LVL4						= 2;
			OSIRIS_LVL5						= 3;
			OSIRIS_LVL6						= 5;
			OSIRIS_LVL7						= 5;
			OSIRIS_LVL8						= 10;
			OSIRIS_LVL9						= 10;
			OSIRIS_MS 						= new int[] { 2000, 3000, 4000 };
			OSIRIS_MS1 						= new int[] { 3000, 4000, 5000 };
			OSIRIS_MS2 						= new int[] { 3000, 4000, 5000, 6000 };
			OSIRIS_MS3 						= new int[] { 4000, 5000, 6000, 7000 };
			OSIRIS_MS4 						= new int[] { 5000, 6000, 7000 };
			OSIRIS_MS5 						= new int[] { 5000, 6000, 7000 };

			CONFUSIONPHANTASMT				= 2.0;

			BONEBREAKPRO					= 30.0;
			BONE_BREAK_RAST_MS				= 3000;

		}
	}
	//暗黑妖精技能外部化
	public static ServerMagicInfo_DarkElf MagicAdSetting_DarkElf = new ServerMagicInfo_DarkElf();
	public static class ServerMagicInfo_DarkElf {
		public double ARMORBRAKET;
		public double UncannyDodgePercent;
		public int ARMOR_BREAK_DESTINY_LV;
		public double BURNINGSPIRITCHANCE;
		public double BURNINGSPIRITPC;
		public double BURNINGSPIRITNPC;
		public double LUCIFERCHANCE;
		public double DOUBLEDMG;
		public int DOUBLEPCPCCHANCE;
		public int DOUBLEPCNPCCHANCE;
		public double DOUBLEBREAKCHANCE;
		public double DOUBLEBRAKEDMGPC;
		public double DOUBLEBREAKDESTINYNPC;
		public double DOUBLEBRAKEDMGNPC;

		public ServerMagicInfo_DarkElf() {
			UncannyDodgePercent             = 1.0;
			ARMORBRAKET						= 38.0;
			ARMOR_BREAK_DESTINY_LV			= 3;
			BURNINGSPIRITCHANCE				= 20.0;
			BURNINGSPIRITPC					= 1.5;
			BURNINGSPIRITNPC				= 1.5;
			LUCIFERCHANCE					= 0.10;
			DOUBLEDMG						= 2.0;
			DOUBLEPCPCCHANCE				= 100;
			DOUBLEPCNPCCHANCE				= 100;
			DOUBLEBREAKCHANCE				= 20.0;
			DOUBLEBRAKEDMGPC				= 2.0;
			DOUBLEBRAKEDMGNPC				= 2.0;
			DOUBLEBREAKDESTINYNPC			= 0.2;



		}
	}

	public static WeekBoxInfo WeekBox = new WeekBoxInfo();
	public static class WeekBoxInfo{
		public int WEEK_BOX_ID;
		public int[] REWARD_COUNT;
		public int[] REWARD_ITEMID;
		public int[] REWARD_ITEM_COUNT;
		public int REWARD_NEED_ITEM;
		public WeekBoxInfo() {
			WEEK_BOX_ID			= 30001853;
			REWARD_COUNT		= new int[] {};
			REWARD_NEED_ITEM	= 0;
			REWARD_ITEMID		= new int[] {};
			REWARD_ITEM_COUNT	= new int[] {};

		}
	}


	public static DeathMatchInfo DeathMatch = new DeathMatchInfo();
	public static class DeathMatchInfo{
		public int WINNER_REWARD_ITEM_ID;
		public int WINNER_REWARD_ITEM_COUNT;
		public int DEATH_MATCH_REWARD_ITEM_ID;
		public int DEATH_MATCH_REWARD_ITEM_COUNT;
		public String TEAM_RED_NAME;
		public String TEAM_BLUE_NAME;
		public String TEAM_RED_TITLE;
		public String TEAM_BLUE_TITLE;
		public String TEAM_DEATH_MATCH_NAME;
		public int TEAM_RED_EMBLEM;
		public int TEAM_BLUE_EMBLEM;

		public DeathMatchInfo() {
			WINNER_REWARD_ITEM_ID			= 0;
			WINNER_REWARD_ITEM_COUNT		= 0;
			DEATH_MATCH_REWARD_ITEM_ID		= 0;
			DEATH_MATCH_REWARD_ITEM_COUNT	= 0;
			TEAM_RED_NAME					= "";
			TEAM_BLUE_NAME					= "";
			TEAM_RED_TITLE					= "";
			TEAM_BLUE_TITLE					= "";
			TEAM_DEATH_MATCH_NAME			= "팀";
			TEAM_RED_EMBLEM					= 0;
			TEAM_BLUE_EMBLEM				= 0;

		}
	}




	//TODO ServerAdSetting.json
	public static ServerAdSettingInfo ServerAdSetting = new ServerAdSettingInfo();
	public static class ServerAdSettingInfo{
		public int[] WANTED_ADENA_CONSUME;
		public int[] WANTED_ABILITY;
		public int PC_RECOGNIZE_RANGE;
		public int[] TIME_COLLECTION_NPC_IDS;
		public int PC_MASTER_SHOP_ID;
		public int[] PC_GOLDEN_BUFF_MAP1;
		public int[] PC_GOLDEN_BUFF_MAP2;
		public int PC_GAHO_COUNT;
		public int CLOUD_POISON_DAMAGE;
		public boolean SMELTING_SYSTEM;
		public boolean FavorSystem;
		public int GMCODE;
		public boolean GMSHOP;
		public int GMSHOPMINID;
		public int GMSHOPMAXID;
		public boolean TARGETGFX;
		public boolean ANNOUNCECYCLESETTING;
		public int ANNOUNCECYCLETIME;
		public boolean BASETOWN;
		public int BASETOWNMINID;
		public int BASETOWNMAXID;
		public int InnMaximumCount;
		public boolean SPAWNHOMEPOINT;
		public int SPAWNHOMEPOINTRANGE;
		public int SPAWNHOMEPOINTCOUNT;
		public int SPAWNHOMEPOINTDELAY;
		public boolean DELETECHARACTERAFTER7DAYS;
		public byte AUTOLOOTINGSETTING;
		public int AUTOLOOTINGRANGE;
		public String ITEMDELETIONTYPE;
		public int ITEMDELETIONTIME;
		public int ITEMDELETIONRANGE;
		public boolean SERVERPVPSETTING;
		public boolean SIMWARPENALTY;
		public boolean GETBACKREST;
		public int ELEMENTALSTONEAMOUNT;
		public int NPCMAXITEM;
		public int ADEN_OVER_COUNT;
		public int ADEN_ITEMID;
		public int ADEN_COUNT;
		public int ADEN_OVER_ADEN;
		public int REWARD_ITEMID;
		public int REWARD_COUNT;
		public int USERGHOSTCOUNT;
		public int WHOISCONTER;
		public int GLOBALCHATLEVEL;
		public int WHISPERCHATLEVEL;
		public int CROWNBLOODLEVEL;
		public int CROWNBLOOD_ALLIANCE_LEVEL;
		public int CLANBUFFUSERCOUNT;
		public int LETTERLEVEL;
		public int LETTER_SUBJECT;
		public int LETTER_CONTENT;
		public int BOARDLEVEL;
		public int ALTDROPLEVELLIMIT;
		public int PARTYUSERCOUNT;
		public int PARTYCHATCOUNT;
		public boolean INITBOSSSPAWN;
		public int Inventory_Count;
		public int WAREHOUSEMAXITEM;
		public int CLANWAREHOUSEMAXITEM;
		public int USERNOSHOPLEVEL;
		public boolean CLANLEADER;
		public boolean CLANPCTITLESETTING;
		public boolean CLANALLIANCE;
		public int CLANMAXUSERCOUNT;
		public boolean CASTLEWAR;
		public int WARMINLEVEL;
		public int WARPLAYER;
		public int WAR_WIN_ITEMID_LEADER;
		public int WAR_WIN_ITEMID_COUNT_LEADER;
		public int WAR_WIN_ITEMID;
		public int WAR_WIN_ITEMID_COUNT;
		public int HOUSETAXINTERVAL;
		public int LineageBuff;
		public int LineageBuffExpRation;
		public int StartCharBoho;
		public boolean TUTORIALCLAN;
		public String NEWCLANNAME;
		public int NEWPLAYERLEVELPROTECTION;
		public int NEWPLAYERLEVELPURGE;
		public boolean CLANSETTINGPROTECTION;
		public int CLANIDPROTECTION;
		public int CLANEMBPROTECTION;
		public int NEWPLAYERPROTECTION;
		public boolean IsPartyExp;
		public double AddPartyExp;
		public boolean NpcMaxYN;
		public int NpcMax;
		public int NpcLocation;
		public int PcReload;
		public int Expreturn;
		public int NewCha;
		public int NewCha1;
		public int summonhpdmg;
		public int NewMagicDmg;
		public int RedKnightdieCount;
		public int QueenAnt_Itemid;
		public int QueenAnt_Count;
		public int QueenAnt_Magnetic_Dmg;
		public int QueenAnt_magnetic_time;
		public boolean InfinityBattle_hit;
		public int InfinityBattle_dmg;
		public int InfinityBattle_boss_dmg;
		public int Bonus_Lvl1;
		public int Bonus_Lvl2;
		public int Bonus_Lvl3;
		public int Bonus_Lvl4;
		public int Bonus_Lvl5;
		public int Bonus_Item1;
		public int Bonus_Item2;
		public int Bonus_Item3;
		public int Bonus_Item4;
		public int Bonus_Item5;
		public int Bonus_Item_Count1;
		public int Bonus_Item_Count2;
		public int Bonus_Item_Count3;
		public int Bonus_Item_Count4;
		public int Bonus_Item_Count5;
		public int day;
		public int hour;
		public int minute;
		public int second;
		public int limit_hour;
		public int limit_minute;
		public int limit_second;
		public boolean Quest_true;
		public int Quest_day;
		public int Quest_hour;
		public int Quest_minute;
		public int Quest_second;
		public int AssassinationLevel1KillCount;
		public int AssassinationLevel2KillCount;
		public int KillCount1;
		public int KillCount2;
		public boolean GAHO_DBAN;
		public double PLAYSUPPORTARDEN;
		public double PLAYSUPPORTITEM;
		public double Blessing;
		public double AdenaRateOfBlessing;
		public double AdenaRateOfUnBlessing;
		public int ExpPosis;
		public int ExpPosis1;
		public int ScareLevel;
		public int tamsc;
		public int tamsc1;
		public int tamsc2;
		public int SCAEVENTITEMCHANCE;
		public int SCAEVENTITEMCHANCE1;
		public int SCAEVENTITEMCHANCE2;
		public int SCAEVENTITEM;
		public int SCAEVENTITEM1;
		public int SCAEVENTITEM2;
		public int CharactersCharSlot;
		public boolean AccountNBuff;
		public int LimitNBuff;
		public int TimeNBuff;
		public int YNpclevel;
		public int TaxRate;
		public int TaxRateMin;
		public int TaxRateMax;
		public boolean IsValidShopSystem;
		public boolean IsValidItemId;
		public boolean IsCheckBoxRulesUse;
		public boolean ArdenTo;
		public boolean Adentype;
		public boolean HuntEvent;
		public int[] WEAPONS_ITEMID;
		public int[] ARMORS_ITEMID;
		public int ITEMS;
		public int CHANCE_ITEMS;
		public int CHANCE_ITEMS1;
		public int CHANCE_ITEMS2;
		public int RND_RKQT;
		public boolean PolyEvent2;
		public int PolyEvent2_level_down;
		public boolean PolyEvent1;
		public boolean PolyEvent;
		public int FeatherShopNum;
		public boolean FEATHER;
		public boolean BUGFIGHTCONTROL;
		public boolean COLOSSEUMOPEN;
		public TimeInfo WarTime;
		public TimeInfo WarInterval;
		public int craft_hour;
		public int craft_minute;
		public int craft_second;
		public boolean DragonRaidRandom;
		public boolean TotalCraftList;
		public boolean TotalCraftList_type;
		public boolean EINHASAD_POINT_SYSTEM;
		public boolean EINHASAD_FAITH_SYSTEM;
		public int CLAN_CONTRIBUTION;
		public boolean DelayTimer;
		public boolean MAGIC_DOLL_SYSTEM;
		public int LOTTO_MINUTE;
		public int LOTTO_HOUR;
		public int LOTTO_MAX_NUMBER;
		public boolean LOTTO_USE;
		public boolean LV100AZIT;
		public int[] TIME_COLLECTION_CRAFT_ID;
		public int CASTLE_CLAN_EFFECT;
		public boolean AdenTo;

		public ServerAdSettingInfo() {
			WANTED_ADENA_CONSUME	= new int[] {500000, 1000000, 2000000};
			WANTED_ABILITY			= new int[] {3,3,3,3,3,3,3};
			PC_RECOGNIZE_RANGE		= 20;
			TIME_COLLECTION_NPC_IDS	= new int[] {};
			TIME_COLLECTION_CRAFT_ID= new int[] {};
			PC_MASTER_SHOP_ID		= 8500331;
			PC_GOLDEN_BUFF_MAP1		= new int[] {624, 430};
			PC_GOLDEN_BUFF_MAP2		= new int[] {7531, 7532, 7533, 7534};
			PC_GAHO_COUNT			= 3;
			CLOUD_POISON_DAMAGE		= 100;
			SMELTING_SYSTEM			= false;
			FavorSystem				= true;
			GMCODE					= 4933; //2705
			GMSHOP					= false;
			GMSHOPMINID				= 89508;
			GMSHOPMAXID				= 89535;
			TARGETGFX				= false;
			ANNOUNCECYCLESETTING	= true;
			ANNOUNCECYCLETIME		= 10;
			BASETOWN				= false;
			BASETOWNMINID			= 1310111499;
			BASETOWNMAXID			= 1310111513;
			InnMaximumCount			= 20;
			SPAWNHOMEPOINT			= true;
			SPAWNHOMEPOINTRANGE		= 3;
			SPAWNHOMEPOINTCOUNT		= 2;
			DELETECHARACTERAFTER7DAYS = true;
			AUTOLOOTINGSETTING		= 0;
			AUTOLOOTINGRANGE		= 8;
			ITEMDELETIONTYPE		= "auto";
			ITEMDELETIONTIME		= 20;
			ITEMDELETIONRANGE		= 2;
			SERVERPVPSETTING		= true;
			SIMWARPENALTY			= true;
			GETBACKREST				= false;
			ELEMENTALSTONEAMOUNT	= 20;
			NPCMAXITEM				= 8;
			ADEN_OVER_COUNT			= 1800000000;
			ADEN_ITEMID				= 40308;
			ADEN_COUNT				= 100000000;
			REWARD_ITEMID			= 400254;
			ADEN_OVER_ADEN			= 100000000;
			REWARD_COUNT			= 1;
			USERGHOSTCOUNT			= 3;
			WHOISCONTER				= 0;
			GLOBALCHATLEVEL			= 55;
			WHISPERCHATLEVEL		= 1;
			CROWNBLOODLEVEL			= 1;
			CROWNBLOOD_ALLIANCE_LEVEL = 1;
			CLANBUFFUSERCOUNT		= 3;
			LETTERLEVEL				= 1;
			LETTER_SUBJECT			= 20;
			LETTER_CONTENT			= 2000;
			BOARDLEVEL				= 55;
			ALTDROPLEVELLIMIT		= 70;
			PARTYUSERCOUNT			= 8;
			PARTYCHATCOUNT			= 8;
			INITBOSSSPAWN			= true;
			Inventory_Count			= 150;
			WAREHOUSEMAXITEM		= 150;
			CLANWAREHOUSEMAXITEM	= 150;
			USERNOSHOPLEVEL			= 86;
			CLANLEADER				= false;
			CLANPCTITLESETTING		= false;
			CLANALLIANCE			= false;
			CASTLEWAR				= false;
			WARMINLEVEL				= 56;
			WARPLAYER				= 3;
			WAR_WIN_ITEMID_LEADER	= 3000231;
			WAR_WIN_ITEMID_COUNT_LEADER	= 1;
			WAR_WIN_ITEMID			= 3000231;
			WAR_WIN_ITEMID_COUNT	= 1;
			CLANMAXUSERCOUNT		= 50;
			HOUSETAXINTERVAL		= 5;
			LineageBuff				= 1;
			LineageBuffExpRation		= 100;
			StartCharBoho			= 57;
			TUTORIALCLAN			= false;
			NEWCLANNAME 			= "新保護";
			NEWPLAYERLEVELPROTECTION = 1;
			NEWPLAYERLEVELPURGE		= 1;
			CLANSETTINGPROTECTION	= true;
			CLANIDPROTECTION		= 268569961;
			CLANEMBPROTECTION		= 268581390;
			NEWPLAYERPROTECTION		= 10;
			IsPartyExp				= false;
			AddPartyExp				= 1.2;
			NpcMaxYN				= true;
			NpcMax					= 80;
			NpcLocation				= 20;
			PcReload				= 70;
			Expreturn				= 55;
			NewCha					= 55;
			NewCha1					= 1;
			summonhpdmg				= 100;
			NewMagicDmg				= 3;
			RedKnightdieCount		= 10;
			QueenAnt_Itemid			= 4100675;
			QueenAnt_Count			= 1;
			QueenAnt_Magnetic_Dmg	= 300;
			QueenAnt_magnetic_time	= 600;
			InfinityBattle_hit		= true;
			InfinityBattle_dmg		= 500;
			InfinityBattle_boss_dmg = 1000;
			Bonus_Lvl1				= 70;
			Bonus_Lvl2				= 70;
			Bonus_Lvl3				= 70;
			Bonus_Lvl4				= 70;
			Bonus_Lvl5				= 70;
			Bonus_Item1				= 725;
			Bonus_Item2				= 725;
			Bonus_Item3				= 725;
			Bonus_Item4				= 725;
			Bonus_Item5				= 725;
			Bonus_Item_Count1		= 1;
			Bonus_Item_Count2		= 1;
			Bonus_Item_Count3		= 1;
			Bonus_Item_Count4		= 1;
			Bonus_Item_Count5		= 1;
			day						= 1;
			hour					= 6;
			minute					= 0;
			second					= 0;
			limit_hour				= 6;
			limit_minute			= 1;
			limit_second			= 0;
			Quest_true				= true;
			Quest_day				= 4;
			Quest_hour				= 9;
			Quest_minute			= 0;
			Quest_second			= 0;
			AssassinationLevel1KillCount	= 5;
			AssassinationLevel2KillCount	= 10;
			KillCount1				= 50;
			KillCount2				= 100;
			GAHO_DBAN				= true;
			PLAYSUPPORTARDEN		= 0.50;
			PLAYSUPPORTITEM			= 0.50;
			Blessing				= 0.5;
			AdenaRateOfBlessing		= 1.12;
			AdenaRateOfUnBlessing	= 1.0;
			ExpPosis				= 86;
			ExpPosis1				= 87;
			ScareLevel				= 5;
			tamsc					= 0;
			tamsc1					= 0;
			tamsc2					= 0;
			SCAEVENTITEMCHANCE		= 250;
			SCAEVENTITEMCHANCE1		= 200;
			SCAEVENTITEMCHANCE2		= 100;
			SCAEVENTITEM			= 65648;
			SCAEVENTITEM1			= 4100040;
			SCAEVENTITEM2			= 30104;
			CharactersCharSlot		= 5;
			AccountNBuff 			= true;
			LimitNBuff				= 20;
			TimeNBuff				= 3;
			YNpclevel				= 55;
			TaxRate					= 10;
			TaxRateMin				= 10;
			TaxRateMax				= 10;
			IsValidShopSystem 		= false;
			IsValidItemId 			= false;
			IsCheckBoxRulesUse		= false;
			ArdenTo					= false;
			Adentype				= false;
			HuntEvent		 		= false;
			WEAPONS_ITEMID	 		= new int[] { 40087, 140087, 240087, 4100147 };
			ARMORS_ITEMID	 		= new int[] { 40074, 140074, 240074, 4100147 };
			ITEMS					= 4;
			CHANCE_ITEMS			= 90;
			CHANCE_ITEMS1			= 60;
			CHANCE_ITEMS2			= 40;
			RND_RKQT				= 4;
			PolyEvent1				= false;
			PolyEvent2		 		= false;
			PolyEvent		 		= false;
			FeatherShopNum			= 100000;
			FEATHER					= false;
			BUGFIGHTCONTROL			= false;
			COLOSSEUMOPEN			= false;
			craft_hour				= 20;
			craft_minute			= 0;
			craft_second			= 0;
			DragonRaidRandom		= false;
			TotalCraftList			= true;
			TotalCraftList_type		= false;
			EINHASAD_POINT_SYSTEM	= true;
			EINHASAD_FAITH_SYSTEM	= true;
			CLAN_CONTRIBUTION		= 1;
			DelayTimer				= false;
			MAGIC_DOLL_SYSTEM		= true;
			WarTime 				= new TimeInfo(12, 60);
			WarInterval 			= new TimeInfo(5, 2);
			LV100AZIT				= false;
			LOTTO_HOUR				= 23;
			LOTTO_MINUTE			= 59;
			LOTTO_USE				= false;
			LOTTO_MAX_NUMBER		= 15;
			CASTLE_CLAN_EFFECT		= 20531;
			PolyEvent2_level_down	= 10;

		}

		// 單位: 天(5), 小時(11), 分鐘(12)
		// 時間: 對應單位的時間
		public static class TimeInfo{
			public int unit;
			public int time;
			public TimeInfo() {}
			public TimeInfo(int unit, int time) {
				this.unit = unit;
				this.time = time;
			}
		}
	}

	//TODO ServerEnchant.json
	public static ServerEnchantInfo ServerEnchant = new ServerEnchantInfo();
	public static class ServerEnchantInfo{
		public int TJCount;
		public boolean enchantLostLog;
		public int Enchant_Value;
		public int Enchant_Value_Chance;
		public int Enchant_Value_Chance1;
		public int Enchant_Value_Chance2;
		public int Enchant_Value1;
		public int Enchant_Value2;
		public int Enchant_Value_Chance3;
		public int Enchant_Count_Weapon;
		public int Enchant_Count_Armor;
		public int Enchant_Count_Accessory;
		public boolean EnchantMaxFail;
		public int EnchantCoent;
		public double EnchantFailRateOnest;
		public double EnchantFailRateOnesto;
		public double EnchantFailRateOne;
		public double EnchantFailRateTwo;
		public boolean MasterEnchantMess;
		public int MasterEnchant;
		public int MasterArmorEnchant_Count;
		public int MasterArmorEnchant;
		public int NoltoArmorEnchant_Count;
		public int NoltoArmorEnchant;
		public int NoltoWeaponEnchant_Count;
		public int NoltoWeaponEnchant;
		public int MasterEnchant1;
		public int MasterEnchant2;
		public int MasterEnchant3;
		public int CarvingEnchant;
		public int blessChance_weapon;
		public int blessChance_weapon_chance;
		public int blessChance_armor;
		public int blessChance_armor_chance;
		public int blessChance_armor_effect1;
		public int blessChance_armor_effect2;
		public int blessChance_armor_effect3;
		public int blessChance_accessory;
		public int blessChance_accessory_chance;
		public int blessChance_accessory_effect1;
		public int blessChance_accessory_effect2;
		public int blessChance_accessory_effect3;
		public int ComboChance;
		public int Heroweapon;
		public int[] Antiquity_Rune;
		public int[] Normal_orim;
		public int[] Bless_orim;
		public int[] Dragon;
		public int[] Roomtis;
		public int[] Sanpper;
		public int[] Sentence;
		public int[] BADGEENCHANT;
		public int LimitWeapon;
		public int LimitWeapon2;
		public int LimitArmor;
		public int LimitArmor2;
		public int Weapon_ReEnchant;
		public int Armor_ReEnchant;
		public int RoomT;
		public int Snapper;
		public int SENTENCHMAXLEVEL;
		public int Pendant;
		public int Accessory_Limit;
		public int Accessory_Antiquity;
		public int badgeLevel;
		public int RoomtisLevel;
		public int SanpperLevel;
		public int SentenceLevel;
		public int DragonLevel; //TeamTheday by.裘德
		public int ATTR_ENCHANT_FIRE;
		public int ATTR_ENCHANT_FIRE1;
		public int ATTR_ENCHANT_FIRE2;
		public int ATTR_ENCHANT_FIRE3;
		public int ATTR_ENCHANT_FIRE4;
		public int ATTR_ENCHANT_WATER;
		public int ATTR_ENCHANT_WATER1;
		public int ATTR_ENCHANT_WATER2;
		public int ATTR_ENCHANT_WATER3;
		public int ATTR_ENCHANT_WATER4;
		public int ATTR_ENCHANT_WISH;
		public int ATTR_ENCHANT_WISH1;
		public int ATTR_ENCHANT_WISH2;
		public int ATTR_ENCHANT_WISH3;
		public int ATTR_ENCHANT_WISH4;
		public int ATTR_ENCHANT_EARTH;
		public int ATTR_ENCHANT_EARTH1;
		public int ATTR_ENCHANT_EARTH2;
		public int ATTR_ENCHANT_EARTH3;
		public int ATTR_ENCHANT_EARTH4;
		public int Hero_weapon_per;
		public int Hero_weapon_limit;
		public int Hero_armor_per;
		public int Hero_armor_limit;
		public ServerEnchantInfo() {
			TJCount					= 300;
			enchantLostLog			= true;
			Enchant_Value			= 2;
			Enchant_Value_Chance	= 30;
			Enchant_Value_Chance1	= 31;
			Enchant_Value_Chance2	= 100;
			Enchant_Value1			= 3;
			Enchant_Value2			= 5;
			Enchant_Value_Chance3	= 50;
			Enchant_Count_Weapon	= 10;
			Enchant_Count_Armor		= 9;
			Enchant_Count_Accessory	= 7;
			EnchantMaxFail 			= true;
			EnchantCoent			= 9;
			EnchantFailRateOnest	= 2.0;
			EnchantFailRateOnesto	= 0.70;
			EnchantFailRateOne		= 0.50;
			EnchantFailRateTwo		= 0.030;
			MasterEnchantMess 		= true;
			MasterEnchant			= 10;
			MasterArmorEnchant_Count= 9;
			MasterArmorEnchant		= 10;
			NoltoArmorEnchant_Count= 9;
			NoltoArmorEnchant		= 10;
			NoltoWeaponEnchant_Count= 9;
			NoltoWeaponEnchant		= 10;
			MasterEnchant1			= 10;
			MasterEnchant2			= 10;
			MasterEnchant3			= 10;
			CarvingEnchant 			= 9;
			blessChance_weapon		= 20;
			blessChance_weapon_chance	= 80;
			blessChance_armor		= 20;
			blessChance_armor_chance	= 80;
			blessChance_armor_effect1	= 10;
			blessChance_armor_effect2	= 30;
			blessChance_armor_effect3	= 50;
			blessChance_accessory	= 20;
			blessChance_accessory_chance	= 80;
			blessChance_accessory_effect1	= 10;
			blessChance_accessory_effect2	= 30;
			blessChance_accessory_effect3	= 50;
			ComboChance				= 2;
			Heroweapon				= 20;
			Antiquity_Rune 			= new int[] { 500000,300000,200000,150000,120000,100000,80000,60000,50000,20000,10000,10000,10000,10000,10000 };
			Normal_orim 			= new int[] { 550000,350000,250000,200000,150000,100000,70000,40000,20000,10000,10000,10000,10000,10000,10000 };
			Bless_orim 				= new int[] { 500000,300000,200000,150000,100000,50000,40000,20000,10000,10000,10000,10000,10000,10000,10000 };
			Dragon 					= new int[] { 500000,300000,200000,150000,100000,50000,40000,20000,10000,10000,10000,10000,10000,10000,10000 };
			Roomtis 				= new int[] { 500000,300000,200000,100000,80000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000 };
			Sanpper 				= new int[] { 500000,300000,200000,100000,80000,20000,10000,10000,10000,10000,10000,10000,10000,10000,10000 };
			Sentence 				= new int[] { 500000,300000,200000,100000,60000,20000,10000,10000,10000,10000,10000,10000,10000,10000,10000 };
			BADGEENCHANT 			= new int[] { 500000,300000,200000,100000,70000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000 };
			LimitWeapon				= 12;
			LimitWeapon2			= 5;
			LimitArmor				= 10;
			LimitArmor2				= 10;
			Weapon_ReEnchant		= 30;
			Armor_ReEnchant			= 40;
			RoomT					= 8;
			Snapper					= 8;
			SENTENCHMAXLEVEL		= 8;
			Pendant					= 8;
			Accessory_Limit			= 9;
			Accessory_Antiquity		= 10;
			badgeLevel				= 8;
			RoomtisLevel			= 8;
			SanpperLevel			= 8;
			SentenceLevel			= 8;
			ATTR_ENCHANT_FIRE		= 30;
			ATTR_ENCHANT_FIRE1		= 20;
			ATTR_ENCHANT_FIRE2		= 10;
			ATTR_ENCHANT_FIRE3		= 7;
			ATTR_ENCHANT_FIRE4		= 4;
			ATTR_ENCHANT_WATER		= 30;
			ATTR_ENCHANT_WATER1		= 20;
			ATTR_ENCHANT_WATER2		= 10;
			ATTR_ENCHANT_WATER3		= 7;
			ATTR_ENCHANT_WATER4		= 4;
			ATTR_ENCHANT_WISH		= 30;
			ATTR_ENCHANT_WISH1		= 20;
			ATTR_ENCHANT_WISH2		= 10;
			ATTR_ENCHANT_WISH3		= 7;
			ATTR_ENCHANT_WISH4		= 4;
			ATTR_ENCHANT_EARTH		= 30;
			ATTR_ENCHANT_EARTH1		= 20;
			ATTR_ENCHANT_EARTH2		= 10;
			ATTR_ENCHANT_EARTH3		= 7;
			ATTR_ENCHANT_EARTH4		= 4;
			Hero_weapon_per			= 10;
			Hero_weapon_limit		= 5;
			Hero_armor_per			= 10;
			Hero_armor_limit		= 5;
		}
	}
	//TODO ItemOption.json
	public static ItemOptionInfo ItemOption = new ItemOptionInfo();
	public static class ItemOptionInfo{

		public int writeC;
		public boolean writeCBoolean;
		public boolean writeDBoolean;
		public boolean writeHBoolean;
		public int Effect;
		public ItemOptionInfo() {
			writeC						= 100;
			writeCBoolean				= false;
			writeDBoolean				= false;
			writeHBoolean				= false;
			Effect						= 0;
		}
	}


	//TODO Synchronization.json
	public static SynchronizationInfo Synchronization = new SynchronizationInfo();
	public static class SynchronizationInfo{
		public String Rent_Account;
		public boolean Operation_Manager;
		public int AutosaveInterval;
		public int AutosaveIntervalOfInventory;
		public int AutosaveExpInterval;
		public boolean IsSellingsShopLocked;
		public String TimeZone;
		public boolean AutoCreateAccounts;
		public boolean CacheMapFiles;
		public boolean LoadV2MapFiles;
		public boolean CHARACTER_SAVED_SYSTEM;
		public boolean CHARACTER_CHECK_SYSTEM;
		public boolean WALKPOSITIONCHECK;
		public int CPU_USAGE_USER;
		public boolean NETWORK_ATTACK_DELAY_FALSE;
		public int SPEESOVERCHECKCOUNT;
		public int SPEESOVERCHECKCOUNT_AUTO;
		public long NETWORK_ATTACK_DELAY_MILLIS;
		public long NETWORK_DELAY_MILLIS;
		public int _allMerchant_HashMap;
		public int _allClanWars_HashMap;
		public int _allClans_HashMap;
		public int _allNpc_HashMap;
		public int _allNpcObjects_HashMap;
		public int _allShopNpc_HashMap;
		public int _allitem_HashMap;
		public int _allGuard_HashMap;
		public int _allCastleGuard_HashMap;
		public int _allObjects_HashMap;
		public int Map4in_visibleObjects;
		public int Map4out_visibleObjects;
		public int Map4out_visiblePlayers;
		public boolean NotBindProtoCode;
		public boolean FindClientProtoCode;
		public boolean FindServerProtoCode;
		public boolean FindServerProtoCodeFull;
		public SynchronizationInfo() {
			Rent_Account							= "";
			Operation_Manager						= true;
			TimeZone								= "NST";
			CacheMapFiles							= true;
			LoadV2MapFiles							= false;
			AutoCreateAccounts						= true;
			IsSellingsShopLocked					= true;
			CHARACTER_SAVED_SYSTEM					= true;
			CHARACTER_CHECK_SYSTEM					= true;
			AutosaveInterval						= 60;
			AutosaveIntervalOfInventory				= 60;
			AutosaveExpInterval						= 10;
			WALKPOSITIONCHECK						= true;
			CPU_USAGE_USER							= 70;
			NETWORK_ATTACK_DELAY_FALSE				= false;
			SPEESOVERCHECKCOUNT						= 5;
			SPEESOVERCHECKCOUNT_AUTO				= 10;
			NETWORK_ATTACK_DELAY_MILLIS				= 2L;
			NETWORK_DELAY_MILLIS					= 20L;
			_allMerchant_HashMap					= 2048;
			_allClanWars_HashMap					= 64;
			_allClans_HashMap						= 64;
			_allNpc_HashMap							= 5120;
			_allNpcObjects_HashMap					= 5120;
			_allShopNpc_HashMap						= 512;
			_allitem_HashMap						= 180;
			_allGuard_HashMap						= 256;
			_allCastleGuard_HashMap					= 128;
			_allObjects_HashMap						= 5120;
			Map4in_visibleObjects					= 2048;
			Map4out_visibleObjects					= 512;
			Map4out_visiblePlayers					= 128;
			NotBindProtoCode						= true;
			FindClientProtoCode						= false;
			FindServerProtoCode						= false;
			FindServerProtoCodeFull					= false;
		}
	}
	//TODO LogStatus.json
	public static LogStatusInfo LogStatus = new LogStatusInfo();
	public static class LogStatusInfo{
		public boolean GMATKMSG;
		public int ATTACKDELAYCOUNT_LOG;
		public boolean WALKPOSITIONCHECK_LOG;
		public boolean USEPOTIONEFFECTLOGGIN;
		public boolean USEACTIONTIMELOGGING;
		public byte LoggingWeaponEnchant;
		public byte LoggingArmorEnchant;
		public boolean LoggingChatNormal;
		public boolean LoggingChatWhisper;
		public boolean LoggingChatShout;
		public boolean LoggingChatWorld;
		public boolean LoggingChatClan;
		public boolean LoggingChatParty;
		public boolean LoggingChatCombined;
		public boolean LoggingChatChatParty;
		public LogStatusInfo() {
			GMATKMSG								= true;
			ATTACKDELAYCOUNT_LOG					= 10;
			WALKPOSITIONCHECK_LOG					= false;
			USEPOTIONEFFECTLOGGIN					= false;
			USEACTIONTIMELOGGING					= false;
			LoggingWeaponEnchant					= 1;
			LoggingArmorEnchant						= 1;
			LoggingChatNormal						= false;
			LoggingChatWhisper						= false;
			LoggingChatShout						= false;
			LoggingChatWorld						= false;
			LoggingChatClan							= false;
			LoggingChatParty						= false;
			LoggingChatCombined						= false;
			LoggingChatChatParty					= false;
		}
	}
	//TODO LoginStatus.json
	public static LoginStatusInfo Login = new LoginStatusInfo();
	public static class LoginStatusInfo{
		public boolean InterServerUse;
		public boolean SERVERSTANDBY;
		public boolean UseExConnect;
		public boolean UseVersionCheck;
		public boolean LoginEncryption;
		public boolean CharPassword;
		public int CharPasswordMaximumFailureCount;
		public boolean UseShiftServer;
		public boolean StandbyServer;
		public String ExternalAddress;
		public String LocalAddress;
		public String CoderAddress;
		public int GameserverPort;
		public short MaximumOnlineUsers;
		public boolean CharacterConfigInServerSide;
		public boolean Allow2PC;
		public int LevelDownRange;
		public long worldDelayMillis;
		public int STUNSPEEDHACK;
		public int PINGCHECK_SECOND;
		public int PINGCHECK;
		public String ServerGmPhoneNumber;
		public LoginStatusInfo() {
			UseVersionCheck							= true;
			InterServerUse							= false;
			SERVERSTANDBY 							= true;
			UseExConnect 							= true;
			LoginEncryption 						= false;
			CharPassword							= true;
			CharPasswordMaximumFailureCount			= 5;
			UseShiftServer 							= false;
			StandbyServer 							= false;
			ExternalAddress							= "127.0.0.1";
			LocalAddress							= "127.0.0.1";
			CoderAddress							= "127.0.0.1";
			GameserverPort							= 5000;
			MaximumOnlineUsers						= 1024;
			CharacterConfigInServerSide				= true;
			Allow2PC								= true;
			LevelDownRange							= 90;
			worldDelayMillis 						= 3000L;
			STUNSPEEDHACK							= 800;
			PINGCHECK_SECOND						= 60;
			PINGCHECK								= 100;
			ServerGmPhoneNumber						= "";
		}
	}
	//TODO Connection.json
	public static ConnectionInfo Connection = new ConnectionInfo();
	public static class ConnectionInfo{
		public boolean SendBusiness;
		public int SCHEDULEDCOREPOOLSIZE;
		public int GeneralThreadPoolType;
		public int GeneralThreadPoolSize;
		public int AutomaticKick;
		public int PcRecognizeRange;
		public boolean EnableDatabaseResourceLeaksDetection;
		public ConnectionInfo() {
			SendBusiness 							= false;
			SCHEDULEDCOREPOOLSIZE					= 128;
			GeneralThreadPoolType					= 2;
			GeneralThreadPoolSize					= 0;
			AutomaticKick							= 1;
			PcRecognizeRange						= -1;
			EnableDatabaseResourceLeaksDetection	= true;
		}
	}
	//TODO ServerRates.json
	public static ServerRatesInfo ServerRates = new ServerRatesInfo();
	public static class ServerRatesInfo{
		public double RateXp;
		public double RateDropAdena;
		public double RateDropItems;
		public double BloodBonus;
		public double RateLawful;
		public double RateKarma;
		public int EnchantChanceWeapon;
		public int EnchantChanceArmor;
		public int EnchantChanceAccessory;
		public double RateWeightLimit;
		public double RateWeightLimitforPet;
		public int DAMAGEENCHANT;
		public int DAMAGEBLESSENCHANT;
		public double RateShopSellingPrice;
		public double RateShopPurchasingPrice;
		public int CreateChanceDiary;
		public int CreateChanceRecollection;
		public int CreateChanceMysterious;
		public int CreateChanceProcessing;
		public int CreateChanceProcessingDiamond;
		public int CreateChanceDantes;
		public int CreateChanceHistoryBook;
		public int FeatherTime;
		public int FeatherNum;
		public int FeatherNum1;
		public int FeatherNum2;
		public int TamTime;
		public int TamNum;
		public int TamNum1;
		public int TamNum2;
		public boolean Eventof;
		public int EventTime;
		public int EventItem;
		public int EventNumber;
		public double RateXpClaudia;
		public int AinBonusPoint_Monster;
		public int AinBonusPoint_Kard_Lucky;
		public int AinBonusPoint_Kard_Probability;
		public int ExplorationNum;


        public ServerRatesInfo() {
			RateXp 							= 5;
			RateDropAdena					= 5;
			RateDropItems					= 1;
			BloodBonus						= 0.1;
			RateLawful						= 2.0;
			RateKarma						= 3.0;
			EnchantChanceWeapon				= 10;
			EnchantChanceArmor				= 7;
			EnchantChanceAccessory			= 7;
			RateWeightLimit					= 1.2;
			RateWeightLimitforPet			= 3.0;
			DAMAGEENCHANT					= 20;
			DAMAGEBLESSENCHANT				= 10;
			RateShopSellingPrice			= 1.0;
			RateShopPurchasingPrice			= 1.0;
			CreateChanceDiary				= 67;
			CreateChanceRecollection		= 90;
			CreateChanceMysterious			= 90;
			CreateChanceProcessing			= 90;
			CreateChanceProcessingDiamond	= 90;
			CreateChanceDantes				= 40;
			CreateChanceHistoryBook			= 7;

			FeatherTime						= 20;
			FeatherNum						= 10;
			FeatherNum1						= 5;
			FeatherNum2						= 5;
			TamTime							= 30;
			TamNum							= 1000;
			TamNum1							= 1000;
			TamNum2							= 1000;
			Eventof							= true;
			EventTime						= 30;
			EventItem						= 4100181;
			EventNumber						= 3;
			RateXpClaudia					= 0.3;
			AinBonusPoint_Monster			= 1;
			AinBonusPoint_Kard_Lucky		= 20;
			AinBonusPoint_Kard_Probability	= 10000;
		}
	}

	//TODO WebServer.json
	public static WebServerInfo Web = new WebServerInfo();
	public static class WebServerInfo{
		public boolean webServerParking;
		public boolean webServerOnOff;
		public int webServerPort;
		public boolean appcenterCacheReset;
		public int[] adviceItems;
		public boolean powerBall;
		public String powerBallTime;
		public int powerBallTicketEndTime;
		public boolean tradeMenu;
		public WebServerInfo() {
			webServerParking 		= true;
			webServerOnOff 			= true;
			webServerPort 			= 8085;	//網頁端口
			appcenterCacheReset 	= true;
			adviceItems 			= new int[] { 140074,240074,140087,240087,810003 };
			powerBall 				= true;
			powerBallTime			= "02:50,02:51,02:52,02:53,02:54,02:55,07:50,07:51,07:52,07:53,07:54,07:55";
			powerBallTicketEndTime	= 30;
			tradeMenu 				= false;
		}
	}
	//TODO DollEnchant.json
	public static DollEnchantInfo DollEnchant = new DollEnchantInfo();
	public static class DollEnchantInfo{
		public int WeaponMagicPer;
		public int WeaponEnchantPerlvl1;
		public int WeaponEnchantPerlvl2;
		public int WeaponEnchantPerlvl3;
		public int WeaponEnchantPerlvl4;
		public int WeaponEnchantDmglvl1;
		public int WeaponEnchantDmglvl2;
		public int WeaponEnchantDmglvl3;
		public int WeaponEnchantDmglvl4;
		public DollEnchantInfo() {
			WeaponMagicPer 				= 10;
			WeaponEnchantPerlvl1 		= 10;
			WeaponEnchantPerlvl2 		= 10;
			WeaponEnchantPerlvl3 		= 10;
			WeaponEnchantPerlvl4 		= 10;
			WeaponEnchantDmglvl1 		= 10;
			WeaponEnchantDmglvl2 		= 10;
			WeaponEnchantDmglvl3 		= 10;
			WeaponEnchantDmglvl4 		= 10;
		}
	}
	//TODO lotto.json
	public static LottoInfo lottoInfo = new LottoInfo();
	public static class LottoInfo{
		public boolean run;
		public int level;
		public int batting;
		public int bonus;
		public ArrayList<TimeInfo> times;
		public LottoInfo() {
			run = false;
			level = 80;
			batting = 10000000;
			bonus = 20000000;
			times = new ArrayList<>();
		}

		public static class TimeInfo{
			public int hour;
			public int minute;
		}
	}

	public static Treasure_Box TreasureBox = new Treasure_Box();
	public static class Treasure_Box{
		public int[] LUUN_SECRET_BOX_ITEMS;
		public int[] LUUN_SECRET_BOX_COUNTS;
		public int[] LUUN_SECRET_BOX_PRO;
		public int[] EVA_BOX_ITEMS;
		public int[] EVA_BOX_COUNTS;
		public int[] EVA_BOX_PRO;
		public Treasure_Box(){
			LUUN_SECRET_BOX_ITEMS		= new int[] {};
			LUUN_SECRET_BOX_COUNTS		= new int[] {};
			LUUN_SECRET_BOX_PRO			= new int[] {};
			EVA_BOX_ITEMS				= new int[] {};
			EVA_BOX_COUNTS				= new int[] {};
			EVA_BOX_PRO					= new int[] {};

		}
	}

	//TODO CharSettings.json
	public static CharSettingsInfo CharSettings = new CharSettingsInfo();
	public static class CharSettingsInfo{
		public int[] NotCreateClass;
		public int STARTLEVEL;
		public int[] START_LOC_X;
		public int[] START_LOC_Y;
		public short MAPID_LIST;
		public int Level_Dmg;
		public int Level_Dmg_Count;
		public int DEATH_PENALTY_LEVEL_MIN;
		public int DEATH_PENALTY_LEVEL_MAX;
		public int DEATH_PENALTY_LEVEL_MIN1;
		public int DEATH_PENALTY_LEVEL_MAX1;
		public int DEATH_PENALTY_LEVEL_MIN2;
		public int DEATH_PENALTY_LEVEL_MAX2;
		public int DEATH_PENALTY_LEVEL_MIN3;
		public int DEATH_PENALTY_LEVEL_MAX3;
		public int DEATH_PENALTY_LEVEL_MIN4;
		public int DEATH_PENALTY_LEVEL_MAX4;
		public int DEATH_PENALTY_LEVEL_MIN5;
		public int DEATH_PENALTY_LEVEL_MAX5;
		public int DEATH_PENALTY_LEVEL_MIN6;
		public int DEATH_PENALTY_LEVEL_MAX6;
		public double EXP_PENALTY;
		public double EXP_PENALTY1;
		public double EXP_PENALTY2;
		public double EXP_PENALTY3;
		public double EXP_PENALTY4;
		public double EXP_PENALTY5;
		public double EXP_PENALTY6;
		public int LIMITLEVEL;
		public Integer[] PVPDROPNO1;
		public int LAWFUL_DROP_COUNT;
		public int DROP_LAWFUL_MIN;
		public int DROP_LAWFUL_MAX;
		public int DROP_LAWFUL_COUNT;
		public int DROP_LAWFUL1;
		public int DROP_LAWFUL_COUNT1;
		public int DROP_LAWFUL2;
		public int DROP_LAWFUL3;
		public int DROP_LAWFUL4;
		public int DROP_LAWFUL5;
		public int DROP_LAWFUL_COUNT2;
		public int DROP_LAWFUL_COUNT3;
		public int PENALTY_POR;
		public int MaxLevel;
		public int LimitLevel;
		public int aclevel;
		public double DodgePercent;
		public double EvasionPercent;
		public double PVP_BOUNS;
		public CharSettingsInfo() {
			NotCreateClass			= new int[] {0};
			STARTLEVEL				= 1;
			START_LOC_X 			= new int[] { 32731, 32733, 32731, 32728, 32730 };
			START_LOC_Y				= new int[] { 32811, 32812, 32814, 32812, 32809 };
			MAPID_LIST				= 3;
			Level_Dmg				= 70;
			Level_Dmg_Count			= 1;
			DEATH_PENALTY_LEVEL_MIN	= 1;
			DEATH_PENALTY_LEVEL_MAX	= 10;
			DEATH_PENALTY_LEVEL_MIN1= 11;
			DEATH_PENALTY_LEVEL_MAX1= 44;
			DEATH_PENALTY_LEVEL_MIN2= 45;
			DEATH_PENALTY_LEVEL_MAX2= 46;
			DEATH_PENALTY_LEVEL_MIN3= 47;
			DEATH_PENALTY_LEVEL_MAX3= 48;
			DEATH_PENALTY_LEVEL_MIN4= 49;
			DEATH_PENALTY_LEVEL_MAX4= 50;
			DEATH_PENALTY_LEVEL_MIN5= 51;
			DEATH_PENALTY_LEVEL_MAX5= 52;
			DEATH_PENALTY_LEVEL_MIN6= 53;
			DEATH_PENALTY_LEVEL_MAX6= 99;
			EXP_PENALTY				= 0.0;
			EXP_PENALTY1			= 0.1;
			EXP_PENALTY2			= 0.09;
			EXP_PENALTY3			= 0.08;
			EXP_PENALTY4			= 0.07;
			EXP_PENALTY5			= 0.06;
			EXP_PENALTY6			= 0.05;
			PVPDROPNO1 				= new Integer[] { 41921,3000028,3000246 };
			LAWFUL_DROP_COUNT		= 0;
			DROP_LAWFUL_MIN			= 0;
			DROP_LAWFUL_MAX			= 30000;
			DROP_LAWFUL_COUNT		= 1;
			DROP_LAWFUL1			= 0;
			DROP_LAWFUL2			= 29999;
			DROP_LAWFUL_COUNT1		= 2;
			DROP_LAWFUL3			= 30000;
			DROP_LAWFUL4			= 1;
			DROP_LAWFUL5			= 29999;
			DROP_LAWFUL_COUNT2		= 2;
			DROP_LAWFUL_COUNT3		= 4;
			PENALTY_POR				= 1000;
			MaxLevel 				= 10;
			LimitLevel 				= 10;
			aclevel 				= 200;
			DodgePercent 			= 1.0;
			EvasionPercent		 	= 1.0;
			PVP_BOUNS				= 0.2;
			LIMITLEVEL				= 100;
		}
	}
	public static SmeltingSettingInfo SmeltingSetting = new SmeltingSettingInfo();
	public static class SmeltingSettingInfo{
		public boolean SmeltingUse;
		public boolean ejectAll;
		public boolean helmet;
		public boolean T;
		public boolean weapon;
		public boolean glove;
		public SmeltingSettingInfo() {
			ejectAll					= true;
			SmeltingUse 				= true;
			helmet				 		= false;
			T 							= false;
			weapon 						= false;
			glove 						= false;
		}
	}

	//TODO CraftAlchemySetting.json
	public static CraftAlchemyionInfo CraftAlchemySetting = new CraftAlchemyionInfo();
	public static class CraftAlchemyionInfo{
		public int alchemynum;
		public double CRAFTINCREASEPROBBYMILLION;
		public int CRAFTTRANSMITSAFELINE;
		public int CRAFTFLAGMENTATIONSIZE;
		public int ALCHEMYHYPERSUCCESSPROBBYMILLION;
		public int ALCHEMYNOTIFYLEVEL;
		private String mCraftVersionHashString;
		private String mAlchemyVersionHashString;
		private String mSmeltingVersionHashString;
		public int[] CRAFT_VERSION_HASH;
		public int[] ALCHEMY_VERSION_HASH;
		public int[] SMELTING_VERSION_HASH;
		public CraftAlchemyionInfo() {
			alchemynum							= 0;
			CRAFTINCREASEPROBBYMILLION			= 1.50;
			CRAFTTRANSMITSAFELINE				= 1000;
			CRAFTFLAGMENTATIONSIZE				= 10;
			ALCHEMYHYPERSUCCESSPROBBYMILLION	= 10000;
			ALCHEMYNOTIFYLEVEL					= 4;
			mCraftVersionHashString = "2E C7 F5 5D 04 06 FD 9E 39 17 2A 27 DA C8 42 AE CB 60 C6 DD";
			mAlchemyVersionHashString = "15 1C 27 C1 1B 5A DE A4 8F 47 0A FE 1E 1A 3C 2F 72 27 6C 9A";
			mSmeltingVersionHashString = "15 1C 27 C1 1B 5A DE A4 8F 47 0A FE 1E 1A 3C 2F 72 27 6C 9A";
		}

		private void onLoad() {
			CRAFT_VERSION_HASH = convertHexHash(mCraftVersionHashString);
			ALCHEMY_VERSION_HASH = convertHexHash(mAlchemyVersionHashString);
			SMELTING_VERSION_HASH = convertHexHash(mSmeltingVersionHashString);
		}

		private int[] convertHexHash(String hashString) {
			if(MJString.isNullOrEmpty(hashString)) {
				return null;
			}
			return MJHexHelper.parseHexStringToInt32Array(hashString, " ");
		}
	}

	public static void onCraftAlchemySettingLoad() {
		try {
			CraftAlchemySetting = MJJsonUtil.fromFile("./config/CraftAlchemySetting.json", CraftAlchemyionInfo.class);
			CraftAlchemySetting.onLoad();
		} catch (Exception e) {
			System.out.println("Config->CraftAlchemySetting.json 發生錯誤。");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/CraftAlchemySetting.json 文件。");
		}
	}




	public static void load() {
		/******************************************
		 *  Custom Config Json.(TODO)              *
		 *  Title: json Setting 2018~2019  	       *
		 *  Person: Nature:)                       *
		 *  Exe: Notepad++                         *
		 ******************************************/

		onCraftAlchemySettingLoad();

		try {
			Einhasad_item = MJJsonUtil.fromFile("./config/Einhasad_item.json", EinhasadInfo.class);
		} catch (Exception e) {
			System.out.println("Config->Einhasad_item.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/Einhasad_item.json 文件.");
		}
		try {
			MagicDollInfo = MJJsonUtil.fromFile("./config/MagicDoll_Potential.json", MagicDoll_Potential.class);
		} catch (Exception e) {
			System.out.println("Config->MagicDoll_Potential.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicDoll_Potential.json 文件.");
		}
		try {
			Message = MJJsonUtil.fromFile("./config/Fx_Message.json", MessageInfo.class);
		} catch (Exception e) {
			System.out.println("Config->Fx_Message.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/Fx_Message.json 文件.");
		}
		try {
			MagicAdSetting = MJJsonUtil.fromFile("./config/MagicAdSetting.json", ServerMagicInfo.class);
		} catch (Exception e) {
			System.out.println("Config->MagicAdSetting.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicAdSetting.json 文件.");
		}

		try {
			MagicAdSetting_DarkElf = MJJsonUtil.fromFile("./config/MagicAdSetting_DarkElf.json", ServerMagicInfo_DarkElf.class);
		} catch (Exception e) {
			System.out.println("Config->MagicAdSetting_DarkElf.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicAdSetting_DarkElf.json 文件.");
		}
		try {
			MagicAdSetting_DragonKnight = MJJsonUtil.fromFile("./config/MagicAdSetting_DragonKnight.json", ServerMagicInfo_DragonKnight.class);
		} catch (Exception e) {
			System.out.println("Config->MagicAdSetting_DragonKnight.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicAdSetting_DragonKnight.json 文件.");
		}try {
			MagicAdSetting_Elf = MJJsonUtil.fromFile("./config/MagicAdSetting_Elf.json", ServerMagicInfo_Elf.class);
		} catch (Exception e) {
			System.out.println("Config->MagicAdSetting_Elf.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicAdSetting_Elf.json 文件.");
		}try {
			MagicAdSetting_Fencer = MJJsonUtil.fromFile("./config/MagicAdSetting_Fencer.json", ServerMagicInfo_Fencer.class);
		} catch (Exception e) {
			System.out.println("Config->MagicAdSetting_Fencer.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicAdSetting_Fencer.json 文件.");
		}try {
			MagicAdSetting_Illusion = MJJsonUtil.fromFile("./config/MagicAdSetting_Illusion.json", ServerMagicInfo_Illusion.class);
		} catch (Exception e) {
			System.out.println("Config->MagicAdSetting_Illusion.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicAdSetting_Illusion.json 文件.");
		}try {
			MagicAdSetting_Knight = MJJsonUtil.fromFile("./config/MagicAdSetting_Knight.json", ServerMagicInfo_Knight.class);
		} catch (Exception e) {
			System.out.println("Config->MagicAdSetting_Knight.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicAdSetting_Knight.json 文件.");
		}try {
			MagicAdSetting_Lancer = MJJsonUtil.fromFile("./config/MagicAdSetting_Lancer.json", ServerMagicInfo_Lancer.class);
		} catch (Exception e) {
			System.out.println("Config->MagicAdSetting_Lancer.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicAdSetting_Lancer.json 文件.");
		}try {
			MagicAdSetting_Prince = MJJsonUtil.fromFile("./config/MagicAdSetting_Prince.json", ServerMagicInfo_Prince.class);
		} catch (Exception e) {
			System.out.println("Config->MagicAdSetting_Prince.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicAdSetting_Prince.json 文件.");
		}try {
			MagicAdSetting_Warrior = MJJsonUtil.fromFile("./config/MagicAdSetting_Warrior.json", ServerMagicInfo_Warrior.class);
		} catch (Exception e) {
			System.out.println("Config->MagicAdSetting_Warrior.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicAdSetting_Warrior.json 文件.");
		}try {
			MagicAdSetting_Wizard = MJJsonUtil.fromFile("./config/MagicAdSetting_Wizard.json", ServerMagicInfo_Wizard.class);
		} catch (Exception e) {
			System.out.println("Config->MagicAdSetting_Wizard.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/MagicAdSetting_Wizard.json 文件.");
		}try {
			ServerAdSetting = MJJsonUtil.fromFile("./config/ServerAdSetting.json", ServerAdSettingInfo.class);
		} catch (Exception e) {
			System.out.println("Config->ServerAdSetting.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/ServerAdSetting.json 文件.");
		}
		try {
			ServerEnchant = MJJsonUtil.fromFile("./config/ServerEnchant.json", ServerEnchantInfo.class);
		} catch (Exception e) {
			System.out.println("Config->ServerEnchant.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/ServerEnchant.json 文件.");
		}
		try {
			Synchronization = MJJsonUtil.fromFile("./config/Synchronization.json", SynchronizationInfo.class);
		} catch (Exception e) {
			System.out.println("Config->Synchronization.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/Synchronization.json 文件.");
		}
		try {
			LogStatus = MJJsonUtil.fromFile("./config/LogStatus.json", LogStatusInfo.class);
		} catch (Exception e) {
			System.out.println("Config->LogStatus.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/LogStatus.json 文件.");
		}
		try {
			Login = MJJsonUtil.fromFile("./config/LoginStatus.json", LoginStatusInfo.class);
		} catch (Exception e) {
			System.out.println("Config->LoginStatus.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/LoginStatus.json 文件.");
		}
		try {
			Connection = MJJsonUtil.fromFile("./config/Connection.json", ConnectionInfo.class);
		} catch (Exception e) {
			System.out.println("Config->Connection.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/Connection.json 文件.");
		}
		try {
			ServerRates = MJJsonUtil.fromFile("./config/ServerRates.json", ServerRatesInfo.class);
		} catch (Exception e) {
			System.out.println("Config->ServerRates.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/ServerRates.json 文件.");
		}
		try {
			Web = MJJsonUtil.fromFile("./config/WebServer.json", WebServerInfo.class);
		} catch (Exception e) {
			System.out.println("Config->WebServer.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/WebServer.json 文件.");
		}
		try {
			lottoInfo = MJJsonUtil.fromFile("./config/lotto.json", LottoInfo.class);
		} catch (Exception e) {
			System.out.println("Config->lotto.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/lotto.json 文件.");
		}
		try {
			DollEnchant = MJJsonUtil.fromFile("./config/DollEnchant.json", DollEnchantInfo.class);
		} catch (Exception e) {
			System.out.println("Config->DollEnchant.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/DollEnchant.json 文件.");
		}
		try {
			CharSettings = MJJsonUtil.fromFile("./config/CharSettings.json", CharSettingsInfo.class);
		} catch (Exception e) {
			System.out.println("Config->CharSettings.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/CharSettings.json 文件.");
		}
		try {
			ItemOption = MJJsonUtil.fromFile("./config/ItemOption.json", ItemOptionInfo.class);
		} catch (Exception e) {
			System.out.println("Config->ItemOption.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/ItemOption.json 文件.");
		}
		try {
			SmeltingSetting = MJJsonUtil.fromFile("./config/SmeltingSetting.json", SmeltingSettingInfo.class);
		} catch (Exception e) {
			System.out.println("Config->SemltingSetting.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/SmeltingSetting.json 文件.");
		}
		try {
			TreasureBox = MJJsonUtil.fromFile("./config/Treasure_Box.json", Treasure_Box.class);
		} catch (Exception e) {
			System.out.println("Config->Treasure_Box.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/Treasure_Box.json 文件.");
		}
		try {
			WeekBox = MJJsonUtil.fromFile("./config/Week_Box.json", WeekBoxInfo.class);
		} catch (Exception e) {
			System.out.println("Config->Week_Box.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/Week_Box.json 文件.");
		}
		try {
			DeathMatch = MJJsonUtil.fromFile("./config/Death_Match.json", DeathMatchInfo.class);
		} catch (Exception e) {
			System.out.println("Config->Death_Match.json 發生錯誤.");
			e.printStackTrace();
			throw new Error("載入失敗 ./config/Death_Match.json 文件.");
		}


		validate();
	}

	private static void validate() {
		if (!IntRange.includes(Config.ServerAdSetting.ITEMDELETIONRANGE, 0, 5)) {
			throw new IllegalStateException("ItemDeletionRange 的值超出了設定範圍。");
		}
		if (!IntRange.includes(Config.ServerAdSetting.ITEMDELETIONTIME, 1, 35791)) {
			throw new IllegalStateException("ItemDeletionTime 的值超出了設定範圍。");
		}
	}

	public static boolean setParameterValue(String pName, String pValue) {
		if (pName.equalsIgnoreCase("SERVERPVPSETTING")) {
			Config.ServerAdSetting.SERVERPVPSETTING = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("GMATKMSG")) {
			Config.LogStatus.GMATKMSG = Boolean.valueOf(pValue);
		} else {
			return false;
		}
		return true;
	}

	private Config() {
	}

	public final static int etc_arrow = 0;

	public final static int etc_wand = 1;

	public final static int etc_light = 2;

	public final static int etc_gem = 3;

	public final static int etc_potion = 6;

	public final static int etc_firecracker = 5;

	public final static int etc_food = 7;

	public final static int etc_scroll = 8;

	public final static int etc_questitem = 9;

	public final static int etc_spellbook = 10;

	public final static int etc_other = 12;

	public final static int etc_material = 13;

	public final static int etc_sting = 15;

	public final static int etc_treasurebox = 16;

	public static class DBConfig{
		public int min;
		public int max;
	}
}