package l1j.server;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.MJHexHelper;

public final class Config {

    public static final boolean DEBUG = false;
    public static boolean shutdownCheck = false;
    public static int THREAD_P_EFFECTS;
    public static int THREAD_P_GENERAL;
    public static int AI_MAX_THREAD;
    public static boolean Test = false;
    public static boolean version_check = false;
    /******************************************
     * Custom Config Json.(TODO) *
     * Title: json Setting 2018~2019 *
     * Person: Nature:) *
     * Exe: Notepad++ *
     ******************************************/

    // TODO Einhasad_item
    public static EinhasadInfo Einhasad_item = new EinhasadInfo();

    public static class EinhasadInfo {
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
            EINHASAD_LIMIT = 80000000;
            EINHASAD_UNIT = 10000;
            RE_DRAGON_DIAMOND_CHARGE = 1000;
            RE_DRAGON_DIAMOND_FINEST = 2000;
            RE_DRAGON_DIAMOND_ADVANCED = 800;
            RE_DRAGON_DIAMOND_NORMAL = 100;
            RE_DRAGON_SAPPHIRE_NORMAL = 50;
            RE_DRAGON_RUBY_NORMAL = 30;
            RE_DRAGON_DIAMOND_APPLE = 500;
        }
    }

    // TODO MagicDoll_Potential
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
            POTION_ITEMID = 4100676;
            POTION_COUNT = 3;
            POTION_COUNT1 = 5;
            POTION_COUNT2 = 10;
            POTION_COUNT3 = 35;
            POTION_COUNT4 = 100;
            BONUS_RANDOM_PER1 = 1;
            BONUS_RANDOM_PER2 = 1;
            BONUS_RANDOM_PER3 = 1;
            BONUS_RANDOM_PER4 = 10;
            BONUS_RANDOM_PER5 = 20;
            RANDOM_UP_MIN = 0;
            RANDOM_UP_MAX = 84;
            RANDOM_UP_MIN1 = 85;
            RANDOM_UP_MAX1 = 100;
            RANDOM_UP_MIN2 = 0;
            RANDOM_UP_MAX2 = 86;
            RANDOM_UP_MIN3 = 87;
            RANDOM_UP_MAX3 = 100;
            RANDOM_UP_MIN4 = 0;
            RANDOM_UP_MAX4 = 88;
            RANDOM_UP_MIN5 = 89;
            RANDOM_UP_MAX5 = 100;
            RANDOM_UP_MIN6 = 0;
            RANDOM_UP_MAX6 = 90;
            RANDOM_UP_MIN7 = 91;
            RANDOM_UP_MAX7 = 100;
            RANDOM_UP_MIN8 = 0;
            RANDOM_UP_MAX8 = 92;
            RANDOM_UP_MIN9 = 93;
            RANDOM_UP_MAX9 = 100;
            RANDOM_UP_MIN10 = 0;
            RANDOM_UP_MAX10 = 94;
            RANDOM_UP_MIN11 = 95;
            RANDOM_UP_MAX11 = 100;
            BONUS_TYPE1 = 50;
            BONUS_TYPE2 = 40;
            BONUS_TYPE3 = 30;
            BONUS_TYPE4 = 20;
            BONUS_TYPE_BLESS1 = 40;
            BONUS_TYPE_BLESS2 = 30;
            BONUS_TYPE_BLESS3 = 20;
            BONUS_TYPE_BLESS4 = 10;
            MP_SMALL_MIN_PC = 0;
            MP_SMALL_MAX_PC = 1;
            MP_SMALL_CHANCE_PC = 10;
            MP_SMALL_MIN_NPC = 0;
            MP_SMALL_MAX_NPC = 1;
            MP_SMALL_CHANCE_NPC = 10;
            HP_SMALL_MIN_PC = 0;
            HP_SMALL_MAX_PC = 5;
            HP_SMALL_CHANCE_PC = 10;
            HP_SMALL_MIN_NPC = 0;
            HP_SMALL_MAX_NPC = 5;
            HP_SMALL_CHANCE_NPC = 10;
            MP_LARGE_MIN_PC = 0;
            MP_LARGE_MAX_PC = 5;
            MP_LARGE_CHANCE_PC = 10;
            MP_LARGE_MIN_NPC = 0;
            MP_LARGE_MAX_NPC = 5;
            MP_LARGE_CHANCE_NPC = 10;
            HP_LARGE_MIN_PC = 0;
            HP_LARGE_MAX_PC = 5;
            HP_LARGE_CHANCE_PC = 10;
            HP_LARGE_MIN_NPC = 0;
            HP_LARGE_MAX_NPC = 5;
            HP_LARGE_CHANCE_NPC = 10;
            DOLL_SOULOFFLAME = 3;
            DOLL_JUDGEMENT = 3;
            DOLL_DECAY_POTION = 3;
            DOLL_PVP = 40;
        }
    }

    // TODO Fx_Message
    public static MessageInfo Message = new MessageInfo();
    public static Object LogStatus;

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

        /**
         * 訊息資訊類別
         */
        public class MessageInfo {
            /** GM KAKAO 帳號 */
            public String GMKAKAO;
            /** 遊戲伺服器公告 */
            public String GAMESERVERMENT;
            /** 私人商店聊天訊息 */
            public String PrivateShopChat;
            /** 遊戲伺服器名稱 */
            public String GameServerName;
            /** 開放時間提示訊息 */
            public String OpenTimeMont;
            /** 最高等級提示訊息 */
            public String MAX_LEVEL_MESSAGE;
            /** 高級不朽之守護提示訊息 */
            public String GahoMont;
            /** 世界清除清理提示訊息 */
            public String WorldDeleteCleaning;
            /** 限時製作物品數量重置提示訊息 */
            public String CRAFT_LIMIT_TIME;
            /** 地下城時間更新提示訊息 */
            public String DUNGEON_TIME_UPDATOR;
            /** 排行榜時間更新提示訊息 */
            public String RANK_TIME_UPDATOR;
            /** 排行榜強制更新提示訊息 */
            public String RANK_RE_TIME_UPDATOR;
            /** PC咖啡廳剩餘時間提示訊息 */
            public String PC_BUFF_MESSAGE;
            /** 速度超出檢查次數提示訊息 */
            public String SPEESOVERCHECKCOUNT_MESSAGE;
            /** N币訊息 */
            public String NCOIN_MESSAGE;
            /** 額外訊息1 */
            public String Bonus_Message1;
            /** 額外訊息2 */
            public String Bonus_Message2;
            /** 額外訊息3 */
            public String Bonus_Message3;
            /** 額外訊息4 */
            public String Bonus_Message4;
            /** 額外訊息5 */
            public String Bonus_Message5;

            /**
             * 建構子，初始化訊息資訊
             */
            public MessageInfo() {
                GMKAKAO = "NatureT";
                GAMESERVERMENT = "您好";
                PrivateShopChat = "您好";
                GameServerName = "測試伺服器";
                OpenTimeMont = "開放等待";
                MAX_LEVEL_MESSAGE = "已達伺服器最高等級，無法再獲得經驗值。";
                GahoMont = "\\f3高級不朽之守護未持有者的物品/金幣掉落率和機率將減少50%。";
                WorldDeleteCleaning = "過一會兒，阿丹世界地面上的物品將全部刪除。";
                CRAFT_LIMIT_TIME = "限時製作物品數量已重置。";
                DUNGEON_TIME_UPDATOR = "\\f2使用帳號的時間限制地下城使用時間已到。 (每天早上6點重置)";
                RANK_TIME_UPDATOR = "\\aG排行榜系統已自動更新。";
                RANK_RE_TIME_UPDATOR = "\\aG排行榜系統已強制更新。";
                PC_BUFF_MESSAGE = "網咖剩餘時間：";
                SPEESOVERCHECKCOUNT_MESSAGE = "\\f3檢測到速度過快的機器人，將自動重新連線。 (已報告給管理員)";
                NCOIN_MESSAGE = "";
                Bonus_Message1 = "";
                Bonus_Message2 = "";
                Bonus_Message3 = "";
                Bonus_Message4 = "";
                Bonus_Message5 = "";
            }
        }

        // TODO MagicAdSetting.json
        public static ServerMagicInfo MagicAdSetting = new ServerMagicInfo();

        /**
         * 伺服器魔法設定資訊類別
         */
        public static class ServerMagicInfo {
            /** 是否啟用咒語延遲 */
            public boolean SPELLDELAYRUN;
            /** 咒語延遲超時等待時間 */
            public int SPELLDELAYOVERPENDING;
            /** 毒攻擊對玩家造成的傷害 */
            public int PoisonAttack_PC_DMG;
            /** 毒攻擊對玩家的機率 */
            public int PoisonAttack_PC_Chance;
            /** 毒攻擊對玩家的毒效持續時間 */
            public int PoisonAttack_PC_Ms;
            /** 沉默屏障的持續時間 */
            public int Silenpro;
            /** 魔法致命一擊機率 */
            public int PRORCRITICAL_MAGIC;
            /** 魔法致命傷害率 */
            public double MAGICCRITICALDAMAGERATE;
            /** 變身效果陣列 */
            public int[] POLYARRAY;
            /** 類似等級1最小值 */
            public int SIMLEVELMIN1;
            /** 類似等級1最大值 */
            public int SIMLEVELMAX1;
            /** 類似等級1的類似傷害 */
            public double SIMSIMDMG1;
            /** 類似等級2最小值 */
            public int SIMLEVELMIN2;
            /** 類似等級2最大值 */
            public int SIMLEVELMAX2;
            /** 類似等級2的類似傷害 */
            public double SIMSIMDMG2;
            /** 類似等級3最小值 */
            public int SIMLEVELMIN3;
            /** 類似等級3最大值 */
            public int SIMLEVELMAX3;
            /** 類似等級3的類似傷害 */
            public double SIMSIMDMG3;
            /** 類似等級4最小值 */
            public int SIMLEVELMIN4;
            /** 類似等級4最大值 */
            public int SIMLEVELMAX4;
            /** 類似等級4的類似傷害 */
            public double SIMSIMDMG4;
            /** 類似等級5最小值 */
            public int SIMLEVELMIN5;
            /** 類似等級5最大值 */
            public int SIMLEVELMAX5;
            /** 類似等級5的類似傷害 */
            public double SIMSIMDMG5;
            /** 類似等級6最小值 */
            public int SIMLEVELMIN6;
            /** 類似等級6最大值 */
            public int SIMLEVELMAX6;
            /** 類似等級6的類似傷害 */
            public double SIMSIMDMG6;
            /** 目標魔法抵抗力 */
            public int TargetMr;
            /** 目標魔法抵抗力1 */
            public int TargetMr1;
            /** 目標魔法抵抗力2 */
            public int TargetMr2;
            /** 目標魔法抵抗力3 */
            public int TargetMr3;
            /** 目標魔法抵抗力4 */
            public int TargetMr4;
            /** 目標魔法抵抗力5 */
            public int TargetMr5;
            /** 目標魔法抵抗力6 */
            public int TargetMr6;
            /** 目標魔法抵抗力增加值 */
            public double TargetMr_vl;
            /** 目標魔法抵抗力2增加值 */
            public double TargetMr_v2;
            /** 目標魔法抵抗力3增加值 */
            public double TargetMr_v3;
            /** 目標魔法抵抗力4增加值 */
            public double TargetMr_v4;
            /** 目標魔法抵抗力5增加值 */
            public double TargetMr_v5;
            /** 目標魔法抵抗力6增加值 */
            public double TargetMr_v6;
            /** 目標魔法抵抗力7增加值 */
            public double TargetMr_v7;
            /** 每NPC魔力吸取限制 */
            public int MANADRAINLIMITPERNPC;
            /** 導彈致命傷害率 */
            public double MISSILECRITICALDAMAGERATE;
            /** 近戰致命傷害率 */
            public double MELEECRITICALDAMAGERATE;
            /** 角色魔法命中率 */
            public double CHARACTERMAGICHITRATE;
            /** 角色魔法致命率 */
            public double CHARACTERMAGICCRIRATE;
            /** 死亡藥水 */
            public double DEATHPOTION;
            /** 龍珠效果持續時間 */
            public int DRAGON_PEARL_TIME;

            /**
             * 建構子，初始化伺服器魔法設定資訊
             */
            public ServerMagicInfo() {
                SPELLDELAYRUN = true;
                SPELLDELAYOVERPENDING = 9;
                TargetMr = 100;
                TargetMr1 = 200;
                TargetMr2 = 300;
                TargetMr3 = 400;
                TargetMr4 = 500;
                TargetMr5 = 600;
                TargetMr6 = 700;
                TargetMr_vl = 400;
                TargetMr_v2 = 800;
                TargetMr_v3 = 1600;
                TargetMr_v4 = 3200;
                TargetMr_v5 = 6400;
                TargetMr_v6 = 12800;
                TargetMr_v7 = 31600;
                DEATHPOTION = 0.02;
                Silenpro = 250000;
                DRAGON_PEARL_TIME = 600;
            }
        }

        // TODO MagicAdSetting.json
        public static ServerMagicInfo MagicAdSetting = new ServerMagicInfo();

        /**
         * 伺服器魔法設定資訊類別
         */
        public static class ServerMagicInfo {
            // 魔法致命傷害率
            public double MAGICCRITICALDAMAGERATE;
            // 變身效果陣列
            public int[] POLYARRAY;
            // 類似等級1最小值
            public int SIMLEVELMIN1;
            // 類似等級1最大值
            public int SIMLEVELMAX1;
            // 類似等級1的類似傷害
            public double SIMSIMDMG1;
            // 類似等級2最小值
            public int SIMLEVELMIN2;
            // 類似等級2最大值
            public int SIMLEVELMAX2;
            // 類似等級2的類似傷害
            public double SIMSIMDMG2;
            // 類似等級3最小值
            public int SIMLEVELMIN3;
            // 類似等級3最大值
            public int SIMLEVELMAX3;
            // 類似等級3的類似傷害
            public double SIMSIMDMG3;
            // 類似等級4最小值
            public int SIMLEVELMIN4;
            // 類似等級4最大值
            public int SIMLEVELMAX4;
            // 類似等級4的類似傷害
            public double SIMSIMDMG4;
            // 類似等級5最小值
            public int SIMLEVELMIN5;
            // 類似等級5最大值
            public int SIMLEVELMAX5;
            // 類似等級5的類似傷害
            public double SIMSIMDMG5;
            // 類似等級6最小值
            public int SIMLEVELMIN6;
            // 類似等級6最大值
            public int SIMLEVELMAX6;
            // 類似等級6的類似傷害
            public double SIMSIMDMG6;

            // 導彈致命傷害率
            public double MISSILECRITICALDAMAGERATE;
            // 近戰致命傷害率
            public double MELEECRITICALDAMAGERATE;
            // 角色魔法命中率
            public double CHARACTERMAGICHITRATE;
            // 角色魔法致命率
            public double CHARACTERMAGICCRIRATE;

            // 魔法致命一擊機率
            public int PRORCRITICAL_MAGIC;
            // 每NPC魔力吸取限制
            public int MANADRAINLIMITPERNPC;
            // 毒攻擊對玩家造成的傷害
            public int PoisonAttack_PC_DMG;
            // 毒攻擊對玩家的機率
            public int PoisonAttack_PC_Chance;
            // 毒攻擊對玩家的毒效持續時間
            public int PoisonAttack_PC_Ms;

            /**
             * 建構子，初始化伺服器魔法設定資訊
             */
            public ServerMagicInfo() {
                MAGICCRITICALDAMAGERATE = 1.0;
                POLYARRAY = new int[] { 945, 979, 1037, 1039, 15715, 30, 94, 3865, 15673, 15719, 95, 146, 15614, 2376,
                        2377, 3866, 3867, 3868, 3869, 3870, 3871, 3872, 2468, 3874, 3875, 3876, 185, 173, 187, 183,
                        11358, 11396, 11397, 12225, 12226, 11399, 11398, 12227, 15638, 15635, 15636 };
                SIMLEVELMIN1 = 1;
                SIMLEVELMAX1 = 69;
                SIMSIMDMG1 = 1.5;
                SIMLEVELMIN2 = 70;
                SIMLEVELMAX2 = 79;
                SIMSIMDMG2 = 1.8;
                SIMLEVELMIN3 = 80;
                SIMLEVELMAX3 = 81;
                SIMSIMDMG3 = 2.0;
                SIMLEVELMIN4 = 82;
                SIMLEVELMAX4 = 83;
                SIMSIMDMG4 = 2.2;
                SIMLEVELMIN5 = 84;
                SIMLEVELMAX5 = 85;
                SIMSIMDMG5 = 2.5;
                SIMLEVELMIN6 = 86;
                SIMLEVELMAX6 = 90;
                SIMSIMDMG6 = 2.8;
                MISSILECRITICALDAMAGERATE = 1.5;
                MELEECRITICALDAMAGERATE = 1.5;
                CHARACTERMAGICHITRATE = 2.0;
                CHARACTERMAGICCRIRATE = 1.5;
                PRORCRITICAL_MAGIC = 10;
                MANADRAINLIMITPERNPC = 40;
                PoisonAttack_PC_DMG = 30;
                PoisonAttack_PC_Chance = 20;
                PoisonAttack_PC_Ms = 3000;
            }
        }

        // 王族技能的外化
        public static ServerMagicInfo_Prince MagicAdSetting_Prince = new ServerMagicInfo_Prince();

        /**
         * 伺服器王子技能設定資訊類別
         */
        public static class ServerMagicInfo_Prince {
            // 帝國命中至等級
            public int EMPIREHITTOLEVEL;
            // 帝國
            public double EMPIRE;
            // 帝國等級
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
            // 帝國技能毫秒
            public int[] EMPIRE_MS;
            public int[] EMPIRE_MS1;
            public int[] EMPIRE_MS2;
            public int[] EMPIRE_MS3;
            public int[] EMPIRE_MS4;
            public int[] EMPIRE_MS5;
            // 帝國霸主等級
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
            // 帝國霸主技能毫秒
            public int[] EMPIRE_OVERLORD_MS;
            public int[] EMPIRE_OVERLORD_MS1;
            public int[] EMPIRE_OVERLORD_MS2;
            public int[] EMPIRE_OVERLORD_MS3;
            public int[] EMPIRE_OVERLORD_MS4;
            public int[] EMPIRE_OVERLORD_MS5;
            // 征服者昏迷等級
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
            // 征服者昏迷技能毫秒
            public int[] CONQUEROR_STUN_MS;
            public int[] CONQUEROR_STUN_MS1;
            public int[] CONQUEROR_STUN_MS2;
            public int[] CONQUEROR_STUN_MS3;
            public int[] CONQUEROR_STUN_MS4;
            public int[] CONQUEROR_STUN_MS5;
            // 征服者傷害倍率
            public int CONQUEROR_DAMAGE_RATE;
            // 暴君處刑傷害倍率
            public int TYRANT_EXCUTE_DMG_RATE;

            /**
             * 建構子，初始化伺服器王子技能設定資訊
             */
            public ServerMagicInfo_Prince() {
                EMPIREHITTOLEVEL = 3;
                EMPIRE = 50.0;
                EMPIRE_LVL = -5;
                EMPIRE_LVL1 = -5;
                EMPIRE_LVL2 = -3;
                EMPIRE_LVL3 = -2;
                EMPIRE_LVL4 = 2;
                EMPIRE_LVL5 = 3;
                EMPIRE_LVL6 = 5;
                EMPIRE_LVL7 = 5;
                EMPIRE_LVL8 = 10;
                EMPIRE_LVL9 = 10;
                EMPIRE_MS = new int[] { 1000, 2000, 3000 };
                EMPIRE_MS1 = new int[] { 1000, 2000, 3000, 4000 };
                EMPIRE_MS2 = new int[] { 1000, 2000, 3000, 4000, 5000 };
                EMPIRE_MS3 = new int[] { 3000, 4000, 5000, 6000 };
                EMPIRE_MS4 = new int[] { 4000, 5000, 6000 };
                EMPIRE_MS5 = new int[] { 5000, 6000 };
                EMPIRE_OVERLORD_LVL = -5;
                EMPIRE_OVERLORD_LVL1 = -5;
                EMPIRE_OVERLORD_LVL2 = -3;
                EMPIRE_OVERLORD_LVL3 = -2;
                EMPIRE_OVERLORD_LVL4 = 2;
                EMPIRE_OVERLORD_LVL5 = 3;
                EMPIRE_OVERLORD_LVL6 = 5;
                EMPIRE_OVERLORD_LVL7 = 5;
                EMPIRE_OVERLORD_LVL8 = 10;
                EMPIRE_OVERLORD_LVL9 = 10;
                EMPIRE_OVERLORD_MS = new int[] { 1000, 2000, 3000 };
                EMPIRE_OVERLORD_MS1 = new int[] { 1000, 2000, 3000, 4000 };
                EMPIRE_OVERLORD_MS2 = new int[] { 1000, 2000, 3000, 4000, 5000 };
                EMPIRE_OVERLORD_MS3 = new int[] { 3000, 4000, 5000, 6000 };
                EMPIRE_OVERLORD_MS4 = new int[] { 4000, 5000, 6000 };
                EMPIRE_OVERLORD_MS5 = new int[] { 5000, 6000 };
                CONQUEROR_STUN_LVL = -5;
                CONQUEROR_STUN_LVL1 = -5;
                CONQUEROR_STUN_LVL2 = -3;
                CONQUEROR_STUN_LVL3 = -2;
                CONQUEROR_STUN_LVL4 = 2;
                CONQUEROR_STUN_LVL5 = 3;
                CONQUEROR_STUN_LVL6 = 5;
                CONQUEROR_STUN_LVL7 = 5;
                CONQUEROR_STUN_LVL8 = 10;
                CONQUEROR_STUN_LVL9 = 10;
                CONQUEROR_STUN_MS = new int[] { 1000, 2000, 3000 };
                CONQUEROR_STUN_MS1 = new int[] { 1000, 2000, 3000, 4000 };
                CONQUEROR_STUN_MS2 = new int[] { 1000, 2000, 3000, 4000, 5000 };
                CONQUEROR_STUN_MS3 = new int[] { 3000, 4000, 5000, 6000 };
                CONQUEROR_STUN_MS4 = new int[] { 4000, 5000, 6000 };
                CONQUEROR_STUN_MS5 = new int[] { 5000, 6000 };
                CONQUEROR_DAMAGE_RATE = 100;
                TYRANT_EXCUTE_DMG_RATE = 150;
            }
        }

        // 檢查技能外部化
        public static ServerMagicInfo_Fencer MagicAdSetting_Fencer = new ServerMagicInfo_Fencer();

        /**
         * 伺服器劍士技能設定資訊類別
         */
        public static class ServerMagicInfo_Fencer {
            // 豹戰等級
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
            // 豹戰毫秒
            public int[] PANTHERA_MS;
            public int[] PANTHERA_MS1;
            public int[] PANTHERA_MS2;
            public int[] PANTHERA_MS3;
            public int[] PANTHERA_MS4;
            public int[] PANTHERA_MS5;
            // 豹震等級
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
            // 豹震毫秒
            public int[] PANTHERA_SHOCK_MS;
            public int[] PANTHERA_SHOCK_MS1;
            public int[] PANTHERA_SHOCK_MS2;
            public int[] PANTHERA_SHOCK_MS3;
            public int[] PANTHERA_SHOCK_MS4;
            public int[] PANTHERA_SHOCK_MS5;
            // 火焰被動
            public int FLAME_PASSIVE;
            public int FLAME_PASSIVE_DMG;
            // 矛盾機率
            public int PARADOXPROBABILITY;
            // 成長被動
            public int GROWS_PASSIVE;
            // 憤怒機率
            public int RAGEPROBABILITY;
            // 憤怒傷害
            public double RAGEDMG;
            // 存活百分比
            public int SURVIVE_PER;
            // 存活額外
            public int SURVIVE_ADDITION;

            /**
             * 建構子，初始化伺服器劍士技能設定資訊
             */
            public ServerMagicInfo_Fencer() {
                PANTHERA_LVL = -5;
                PANTHERA_LVL1 = -5;
                PANTHERA_LVL2 = -3;
                PANTHERA_LVL3 = -2;
                PANTHERA_LVL4 = 2;
                PANTHERA_LVL5 = 3;
                PANTHERA_LVL6 = 5;
                PANTHERA_LVL7 = 5;
                PANTHERA_LVL8 = 10;
                PANTHERA_LVL9 = 10;
                PANTHERA_MS = new int[] { 1000 };
                PANTHERA_MS1 = new int[] { 1000 };
                PANTHERA_MS2 = new int[] { 1000, 2000 };
                PANTHERA_MS3 = new int[] { 1000, 2000 };
                PANTHERA_MS4 = new int[] { 2000, 3000 };
                PANTHERA_MS5 = new int[] { 2000, 3000 };
                PANTHERA_SHOCK_LVL = -5;
                PANTHERA_SHOCK_LVL1 = -5;
                PANTHERA_SHOCK_LVL2 = -3;
                PANTHERA_SHOCK_LVL3 = -2;
                PANTHERA_SHOCK_LVL4 = 2;
                PANTHERA_SHOCK_LVL5 = 3;
                PANTHERA_SHOCK_LVL6 = 5;
                PANTHERA_SHOCK_LVL7 = 5;
                PANTHERA_SHOCK_LVL8 = 10;
                PANTHERA_SHOCK_LVL9 = 10;
                PANTHERA_SHOCK_MS = new int[] { 1000, 2000 };
                PANTHERA_SHOCK_MS1 = new int[] { 1000, 2000 };
                PANTHERA_SHOCK_MS2 = new int[] { 2000, 3000 };
                PANTHERA_SHOCK_MS3 = new int[] { 2000, 3000, 4000 };
                PANTHERA_SHOCK_MS4 = new int[] { 2000, 3000, 4000 };
                PANTHERA_SHOCK_MS5 = new int[] { 3000, 4000 };
                FLAME_PASSIVE = 5;
                FLAME_PASSIVE_DMG = 6;
                PARADOXPROBABILITY = 500000;
                GROWS_PASSIVE = 20;
                RAGEPROBABILITY = 150000;
                RAGEDMG = 1.5;
                SURVIVE_PER = 30;
                SURVIVE_ADDITION = 30;
            }
        }

        // 黃金槍騎技能外部化
        public static ServerMagicInfo_Lancer MagicAdSetting_Lancer = new ServerMagicInfo_Lancer();

        /**
         * 伺服器黃金槍騎技能設定資訊類別
         */
        public static class ServerMagicInfo_Lancer {
            // 矛模式
            public double SpearMode;
            // 矛模式距離與傷害關聯
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
            // 矛模式PVP傷害
            public int SPEARMODE_PVP_DMG;
            // 閃避破解
            public int DODGE_BREAK;
            // 致命一擊
            public double DEADLY_STRIKE;
            public int DEADLY_STRIKE_PRO;
            // 復仇百分比
            public int VENGEANCE_PERCENT;
            public int VENGEANCE_REDUCTION;
            // 復仇命中百分比
            public int VENGEANCE_HIT_PERCENT;
            public int VENGEANCE_HIT_REDUCTION;
            // 漩渦
            public int MAELSTROM;
            // 惡劣死亡召回最小傷害
            public int PRESHER_DEATH_RECALL_DMG_MIN;
            public int PRESHER_DEATH_RECALL_DMG;
            // 惡劣死亡召回等級
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
            // 惡劣死亡召回毫秒
            public int[] PRESHER_DEATH_RECALL_MS;
            public int[] PRESHER_DEATH_RECALL_MS1;
            public int[] PRESHER_DEATH_RECALL_MS2;
            public int[] PRESHER_DEATH_RECALL_MS3;
            public int[] PRESHER_DEATH_RECALL_MS4;
            public int[] PRESHER_DEATH_RECALL_MS5;
            // 嚴酷等級
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
            // 嚴酷毫秒
            public int[] CRUEL_MS;
            public int[] CRUEL_MS1;
            public int[] CRUEL_MS2;
            public int[] CRUEL_MS3;
            public int[] CRUEL_MS4;
            public int[] CRUEL_MS5;
            // 嚴酷刺激等級
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
            // 嚴酷刺激毫秒
            public int[] CRUEL_CONBICTION_MS;
            public int[] CRUEL_CONBICTION_MS1;
            public int[] CRUEL_CONBICTION_MS2;
            public int[] CRUEL_CONBICTION_MS3;
            public int[] CRUEL_CONBICTION_MS4;
            public int[] CRUEL_CONBICTION_MS5;
            // 惡劣者玩家PC傷害
            public double PRESHER_PCPCDMG;
            // 惡劣者其他PC傷害
            public double PRESHER_ETCPCDMG;
            // 戰術提升值
            public double TACTICAL_ADVANCE_VAL;

            public ServerMagicInfo_Lancer() {
                // 初始化各項屬性值
            }
        }

        // 戰士技能外部化
        public static ServerMagicInfo_Warrior MagicAdSetting_Warrior = new ServerMagicInfo_Warrior();

        /**
         * 伺服器戰士技能設定資訊類別
         */
        public static class ServerMagicInfo_Warrior {
            // 絕望
            public double DESPERADO;
            // 絕望毫秒
            public int[] DESPERADO_MS;
            // 絕望絕對毫秒
            public int[] DESPERADO_ABSOLUTE_MS;
            // 爆破毫秒
            public int[] DEMOLITION_MS;
            // 撕裂毫秒
            public int[] POWERRIP_MS;
            // 泰坦岩石屬性保護
            public int TITANROCKPRO;
            // 泰坦魔法屬性保護
            public int TITANMAGICPRO;
            // 泰坦彈丸屬性保護
            public int TITANBULLETPRO;
            // 泰坦奮起屬性保護
            public int TITANRISINGPRO;
            // 斧魂獵人最小HP
            public int TOMAHAWK_HUNTER_MIN_HP;
            // 斧魂獵人最大HP
            public int TOMAHAWK_HUNTER_MAX_HP;
            // 泰坦野獸減少百分比
            public double TITANBEAST_REDUC_PER;
            // 岩石傷害
            public double ROCKDMG;
            // 衝撞距離
            public int CRASHFO;
            // 衝撞傷害
            public double CRASHDMG;
            // 狂怒距離
            public int FURYFO;
            // 狂怒傷害
            public double FURYDMG;
            // 爆破屬性
            public int DEMOLITIONPRO;
            // 暴風雨傷害率
            public int TEMPEST_DAMAGE_RATE;
            // 暴風雨技能隨機時間
            public int[] TEMPEST_SKILL_RND_TIME;

            public ServerMagicInfo_Warrior() {
                // 初始化各項屬性值
            }
        }

        // 妖精技能外部化
        public static ServerMagicInfo_Elf MagicAdSetting_Elf = new ServerMagicInfo_Elf();

        /**
         * 伺服器妖精技能設定資訊類別
         */
        public static class ServerMagicInfo_Elf {
            // 三重箭玩家傷害
            public double TripleArrow_dmg_pc;
            // 三重箭NPC傷害
            public double TripleArrow_dmg_npc;
            // 三重箭增強玩家傷害
            public double TripleArrow_boost_dmg_pc;
            // 三重箭增強NPC傷害
            public double TripleArrow_boost_dmg_npc;
            // 抹除魔法
            public double ERASEMAGIC;
            // 風之束縛
            public double WINDSHACKLET;
            // 大地束縛
            public double EARTHBINDT;
            // 強擊之石
            public double STRIKERGALET;
            // 汙染之水
            public double POLLUTEWATERT;
            // 颶風騎士
            public double CYCLONEVAL;
            // 烈焰效果
            public int[] INFERNOEFFECTS;
            // 地獄之地機率
            public int GLORYEARTH_CHANCE;
            // 地獄之地傷害
            public int GLORYEARTH_DMG;
            // 解放冷卻
            public double LIBERATION_CO;
            // 強擊風暴射擊毫秒
            public int[] STRIKERGAILSHOT_MS;
            // 三重眩暈時間陣列
            public int[] TRIPLE_STUN_TIME_ARRAY;
            // 精靈轉移祝福
            public int ELFTURNCHANT;
            // 結合機率
            public double COMBINECHANCE;

            public ServerMagicInfo_Elf() {
                // 初始化各項屬性值
            }
        }

        // 法師技能外部化
        public static ServerMagicInfo_Wizard MagicAdSetting_Wizard = new ServerMagicInfo_Wizard();

        /**
         * 伺服器法師技能設定資訊類別
         */
        public static class ServerMagicInfo_Wizard {
            // 免疫等級
            public int IMMUNELEVEL;
            // 免疫傷害
            public double IMMUNEDMG;
            // 免疫等級1
            public int IMMUNELEVEL1;
            // 免疫傷害1
            public double IMMUNEDMG1;
            // 免疫等級2
            public int IMMUNELEVEL2;
            // 免疫傷害2
            public double IMMUNEDMG2;
            // 免疫等級3
            public int IMMUNELEVEL3;
            // 免疫傷害3
            public double IMMUNEDMG3;
            // 免疫等級4
            public int IMMUNELEVEL4;
            // 免疫傷害4
            public double IMMUNEDMG4;
            // 免疫等級5
            public int IMMUNELEVEL5;
            // 免疫傷害5
            public double IMMUNEDMG5;
            // 免疫等級6
            public int IMMUNELEVEL6;
            // 免疫傷害6
            public double IMMUNEDMG6;
            // 免疫傷害7
            public double IMMUNEDMG7;
            // 解散混亂權重
            public int DISINTCHAOTICWEIGHT;
            // 解散秩序權重
            public int DISINTLAWFULWEIGHT;
            // 解散最小傷害
            public int DISINTMINDMG;
            // 解散最大傷害
            public int DISINTMAXDMG;
            // 永恆最小傷害
            public int ETERNITIMINDMG;
            // 永恆最大傷害
            public int ETERNITIMAXDMG;
            // 永恆毫秒
            public int[] ETERNITI_MS;
            // 抗解散時間
            public int ANTIDISINTEGRATETIME;
            // 抗解散
            public double ANTIDISINTEGRATE;
            // 魔法持續傷害百分比
            public double MAGIC_CONTINUE_DMG_PER;
            // 神聖保護屏障
            public int DEVINE_PROTECTION_BARRIER;
            // 解散毫秒
            public int[] DISINTEGRATEMS;
            public int[] DISINTEGRATEMS1;
            public int[] DISINTEGRATEMS2;
            public int[] DISINTEGRATEMS3;
            public int[] DISINTEGRATEMS4;
            public int[] DISINTEGRATEMS5;
            public int[] DISINTEGRATEMS6;
            // 血管循環減少
            public int ARTERIALCIRCLE_REDUC;
            // 血管循環機率
            public int ARTERIALCIRCLE_CHANCE;
            // 召喚等級增傷
            public double SUMMON_LEVEL_ADDDMG;
            // 召喚SP增傷
            public double SUMMON_SP_ADDDMG;
            // 召喚等級增加命中
            public double SUMMON_LEVEL_ADDHIT;
            // 召喚SP增加命中
            public double SUMMON_SP_ADDHIT;
            // 天罰傷害
            public int NEMESISDAMAGE;

            public ServerMagicInfo_Wizard() {
                // 初始化各項屬性值
            }
        }

        // 龍騎士 技能 外部化
        public static ServerMagicInfo_DragonKnight MagicAdSetting_DragonKnight = new ServerMagicInfo_DragonKnight();

        /**
         * 伺服器 龍騎士 技能設定 資訊類別
         */
        public static class ServerMagicInfo_DragonKnight {
            // 獸殺者對玩家傷害百分比
            public double FouSlayer_dmg_pc;
            // 獸殺者對NPC傷害百分比
            public double FouSlayer_dmg_npc;
            // 雷霆掌握
            public double THUNDERGRABT;
            // 狂暴機率
            public int RAMPAGE_P;
            // 狂暴持續時間
            public double RAMPAGE_D;
            // 半獸人傷害
            public int HALPASDMGX;
            // 半獸人機率
            public int HALPASPROBABILITY;
            // 恐怖一擊對應等級
            public double HORRORHITTOLEVEL;
            // 摧毀一擊對應等級
            public int DESTROYHITTOLEVEL;
            // 獸殺者增傷
            public int FouSlayer_Add_per;
            // 野獸等級
            public int BEHEMOTH_LVL, BEHEMOTH_LVL1, BEHEMOTH_LVL2, BEHEMOTH_LVL3, BEHEMOTH_LVL4, BEHEMOTH_LVL5,
                    BEHEMOTH_LVL6, BEHEMOTH_LVL7, BEHEMOTH_LVL8, BEHEMOTH_LVL9;
            // 野獸毫秒
            public int[] BEHEMOTH_MS, BEHEMOTH_MS1, BEHEMOTH_MS2, BEHEMOTH_MS3, BEHEMOTH_MS4, BEHEMOTH_MS5;
            // 獸轉技能冷卻時間
            public int FOU_TURN_REUSE_TIME;

            public ServerMagicInfo_DragonKnight() {
                // 初始化各項屬性值
            }
        }

        // 騎士 技能 外部化
        public static ServerMagicInfo_Knight MagicAdSetting_Knight = new ServerMagicInfo_Knight();

        /**
         * 伺服器 騎士 技能 設定 資訊類別
         */
        public static class ServerMagicInfo_Knight {
            // 擊暈一擊對應等級
            public int STUNHITTOLEVEL;
            // 震擊擊暈
            public double SHOCKSTUN;
            // 震擊擊暈等級
            public int SHOCKSTUN_LVL;
            // 震擊擊暈等級1
            public int SHOCKSTUN_LVL1;
            // 震擊擊暈等級2
            public int SHOCKSTUN_LVL2;
            // 震擊擊暈等級3
            public int SHOCKSTUN_LVL3;
            // 震擊擊暈等級4
            public int SHOCKSTUN_LVL4;
            // 震擊擊暈等級5
            public int SHOCKSTUN_LVL5;
            // 震擊擊暈等級6
            public int SHOCKSTUN_LVL6;
            // 震擊擊暈等級7
            public int SHOCKSTUN_LVL7;
            // 震擊擊暈等級8
            public int SHOCKSTUN_LVL8;
            // 震擊擊暈等級9
            public int SHOCKSTUN_LVL9;
            // 震擊擊暈毫秒
            public int[] SHOCKSTUN_MS;
            // 震擊擊暈毫秒1
            public int[] SHOCKSTUN_MS1;
            // 震擊擊暈毫秒2
            public int[] SHOCKSTUN_MS2;
            // 震擊擊暈毫秒3
            public int[] SHOCKSTUN_MS3;
            // 震擊擊暈毫秒4
            public int[] SHOCKSTUN_MS4;
            // 震擊擊暈毫秒5
            public int[] SHOCKSTUN_MS5;
            // 強力擊暈等級
            public int FORCESTUN_LVL;
            // 強力擊暈等級1
            public int FORCESTUN_LVL1;
            // 強力擊暈等級2
            public int FORCESTUN_LVL2;
            // 強力擊暈等級3
            public int FORCESTUN_LVL3;
            // 強力擊暈等級4
            public int FORCESTUN_LVL4;
            // 強力擊暈等級5
            public int FORCESTUN_LVL5;
            // 強力擊暈等級6
            public int FORCESTUN_LVL6;
            // 強力擊暈等級7
            public int FORCESTUN_LVL7;
            // 強力擊暈等級8
            public int FORCESTUN_LVL8;
            // 強力擊暈等級9
            public int FORCESTUN_LVL9;
            // 強力擊暈毫秒
            public int[] FORCESTUN_MS;
            // 強力擊暈毫秒1
            public int[] FORCESTUN_MS1;
            // 強力擊暈毫秒2
            public int[] FORCESTUN_MS2;
            // 強力擊暈毫秒3
            public int[] FORCESTUN_MS3;
            // 強力擊暈毫秒4
            public int[] FORCESTUN_MS4;
            // 強力擊暈毫秒5
            public int[] FORCESTUN_MS5;
            // 震擊攻擊等級
            public int SHOCKATTACK_LVL;
            // 震擊攻擊等級1
            public int SHOCKATTACK_LVL1;
            // 震擊攻擊等級2
            public int SHOCKATTACK_LVL2;
            // 震擊攻擊等級3
            public int SHOCKATTACK_LVL3;
            // 震擊攻擊等級4
            public int SHOCKATTACK_LVL4;
            // 震擊攻擊等級5
            public int SHOCKATTACK_LVL5;
            // 震擊攻擊等級6
            public int SHOCKATTACK_LVL6;
            // 震擊攻擊等級7
            public int SHOCKATTACK_LVL7;
            // 震擊攻擊等級8
            public int SHOCKATTACK_LVL8;
            // 震擊攻擊等級9
            public int SHOCKATTACK_LVL9;
            // 震擊攻擊毫秒
            public int[] SHOCKATTACK_MS;
            // 震擊攻擊毫秒1
            public int[] SHOCKATTACK_MS1;
            // 震擊攻擊毫秒2
            public int[] SHOCKATTACK_MS2;
            // 震擊攻擊毫秒3
            public int[] SHOCKATTACK_MS3;
            // 震擊攻擊毫秒4
            public int[] SHOCKATTACK_MS4;
            // 震擊攻擊毫秒5
            public int[] SHOCKATTACK_MS5;
            // 反擊達人
            public int COUNTER_VETERAN;
            // 反擊達人等級
            public int COUNTER_VETERAN_LV;
            // 反擊達人等級1
            public int COUNTER_VETERAN_LV1;
            // 反擊達人等級2
            public int COUNTER_VETERAN_LV2;
            // 反擊達人等級3
            public int COUNTER_VETERAN_LV3;
            // 反擊達人等級4
            public int COUNTER_VETERAN_LV4;
            // 反擊達人等級5
            public int COUNTER_VETERAN_LV5;
            // 反擊倍率
            public double COUNTER;
            // 猛攻傷害增加
            public double BLOWATTACKDMG;

            public ServerMagicInfo_Knight() {
                // 初始化各項屬性值
            }
        }

        // 幻術師 技能 外部化
        public static ServerMagicInfo_Illusion MagicAdSetting_Illusion = new ServerMagicInfo_Illusion();

        /**
         * 伺服器 幻術師 技能 設定 資訊類別
         */
        public static class ServerMagicInfo_Illusion {
            // 纏繞對應等級
            public int ENSNARE_LVL;
            // 纏繞對應等級1
            public int ENSNARE_LVL1;
            // 纏繞對應等級2
            public int ENSNARE_LVL2;
            // 纏繞對應等級3
            public int ENSNARE_LVL3;
            // 纏繞對應等級4
            public int ENSNARE_LVL4;
            // 纏繞對應等級5
            public int ENSNARE_LVL5;
            // 纏繞對應等級6
            public int ENSNARE_LVL6;
            // 纏繞對應等級7
            public int ENSNARE_LVL7;
            // 纏繞對應等級8
            public int ENSNARE_LVL8;
            // 纏繞對應等級9
            public int ENSNARE_LVL9;
            // 纏繞毫秒
            public int[] ENSNARE_MS;
            // 纏繞毫秒1
            public int[] ENSNARE_MS1;
            // 纏繞毫秒2
            public int[] ENSNARE_MS2;
            // 纏繞毫秒3
            public int[] ENSNARE_MS3;
            // 纏繞毫秒4
            public int[] ENSNARE_MS4;
            // 纏繞毫秒5
            public int[] ENSNARE_MS5;
            // 歐西里斯對應等級
            public int OSIRIS_LVL;
            // 歐西里斯對應等級1
            public int OSIRIS_LVL1;
            // 歐西里斯對應等級2
            public int OSIRIS_LVL2;
            // 歐西里斯對應等級3
            public int OSIRIS_LVL3;
            // 歐西里斯對應等級4
            public int OSIRIS_LVL4;
            // 歐西里斯對應等級5
            public int OSIRIS_LVL5;
            // 歐西里斯對應等級6
            public int OSIRIS_LVL6;
            // 歐西里斯對應等級7
            public int OSIRIS_LVL7;
            // 歐西里斯對應等級8
            public int OSIRIS_LVL8;
            // 歐西里斯對應等級9
            public int OSIRIS_LVL9;
            // 歐西里斯毫秒
            public int[] OSIRIS_MS;
            // 歐西里斯毫秒1
            public int[] OSIRIS_MS1;
            // 歐西里斯毫秒2
            public int[] OSIRIS_MS2;
            // 歐西里斯毫秒3
            public int[] OSIRIS_MS3;
            // 歐西里斯毫秒4
            public int[] OSIRIS_MS4;
            // 歐西里斯毫秒5
            public int[] OSIRIS_MS5;
            // 骨折機率
            public double BONEBREAKPRO;
            // 幻覺混亂
            public double CONFUSIONPHANTASMT;
            // 骨折震動毫秒
            public int BONE_BREAK_RAST_MS;

            public ServerMagicInfo_Illusion() {
                // 初始化各項屬性值
            }
        }

        // 黑暗妖精 技能外 部化
        public static ServerMagicInfo_DarkElf MagicAdSetting_DarkElf = new ServerMagicInfo_DarkElf();

        /**
         * 伺服器 黑暗妖精 技能 設定 資訊類別
         */
        public static class ServerMagicInfo_DarkElf {
            // 護甲破壞
            public double ARMORBRAKET;
            // 奇襲閃避百分比
            public double UncannyDodgePercent;
            // 護甲破壞命運等級
            public int ARMOR_BREAK_DESTINY_LV;
            // 燃燒之靈機率
            public double BURNINGSPIRITCHANCE;
            // 燃燒之靈玩家傷害百分比
            public double BURNINGSPIRITPC;
            // 燃燒之靈NPC傷害百分比
            public double BURNINGSPIRITNPC;
            // 露西法機率
            public double LUCIFERCHANCE;
            // 雙倍傷害
            public double DOUBLEDMG;
            // 雙倍傷害玩家對玩家機率
            public int DOUBLEPCPCCHANCE;
            // 雙倍傷害玩家對NPC機率
            public int DOUBLEPCNPCCHANCE;
            // 雙倍傷害護甲破壞機率
            public double DOUBLEBREAKCHANCE;
            // 雙倍傷害護甲破壞玩家傷害百分比
            public double DOUBLEBRAKEDMGPC;
            // 雙倍傷害護甲破壞NPC命中機率
            public double DOUBLEBREAKDESTINYNPC;
            // 雙倍傷害護甲破壞NPC傷害百分比
            public double DOUBLEBRAKEDMGNPC;

            public ServerMagicInfo_DarkElf() {
                // 初始化各項屬性值
            }
        }

        // 每週寶箱資訊外部化
        public static WeekBoxInfo WeekBox = new WeekBoxInfo();

        /**
         * 每週寶箱資訊類別
         */
        public static class WeekBoxInfo {
            // 每週寶箱ID
            public int WEEK_BOX_ID;
            // 獎勵數量
            public int[] REWARD_COUNT;
            // 獎勵道具ID
            public int[] REWARD_ITEMID;
            // 獎勵道具數量
            public int[] REWARD_ITEM_COUNT;
            // 需求物品
            public int REWARD_NEED_ITEM;

            public WeekBoxInfo() {
                // 初始化各項屬性值
            }
        }

        // TODO ServerAdSetting.json
        // 伺服器廣告設定資訊類別
        public static ServerAdSettingInfo ServerAdSetting = new ServerAdSettingInfo();

        public static class ServerAdSettingInfo {
            // 想要的亞丁消耗
            public int[] WANTED_ADENA_CONSUME;
            // 想要的能力
            public int[] WANTED_ABILITY;
            // PC識別範圍
            public int PC_RECOGNIZE_RANGE;
            // 時間收集NPC ID
            public int[] TIME_COLLECTION_NPC_IDS;
            // PC主商店ID
            public int PC_MASTER_SHOP_ID;
            // PC黃金Buff地圖1
            public int[] PC_GOLDEN_BUFF_MAP1;
            // PC黃金Buff地圖2
            public int[] PC_GOLDEN_BUFF_MAP2;
            // PC GAHO 數量
            public int PC_GAHO_COUNT;
            // 雲毒傷害
            public int CLOUD_POISON_DAMAGE;
            // 熔煉系統
            public boolean SMELTING_SYSTEM;
            // 喜愛系統
            public boolean FavorSystem;
            // GM代碼
            public int GMCODE;
            // GM商店
            public boolean GMSHOP;
            // GM商店最小ID
            public int GMSHOPMINID;
            // GM商店最大ID
            public int GMSHOPMAXID;
            // 目標圖形
            public boolean TARGETGFX;
            // 宣告週期設定
            public boolean ANNOUNCECYCLESETTING;
            // 宣告週期時間
            public int ANNOUNCECYCLETIME;
            // 基地城鎮
            public boolean BASETOWN;
            // 基地城鎮最小ID
            public int BASETOWNMINID;
            // 基地城鎮最大ID
            public int BASETOWNMAXID;
            // 旅館最大數量
            public int InnMaximumCount;
            // 生成回城點
            public boolean SPAWNHOMEPOINT;
            // 生成回城點範圍
            public int SPAWNHOMEPOINTRANGE;
            // 生成回城點數量
            public int SPAWNHOMEPOINTCOUNT;
            // 七天後刪除角色
            public boolean DELETECHARACTERAFTER7DAYS;
            // 自動拾取設定
            public byte AUTOLOOTINGSETTING;
            // 自動拾取範圍
            public int AUTOLOOTINGRANGE;
            // 物品刪除類型
            public String ITEMDELETIONTYPE;
            // 物品刪除時間
            public int ITEMDELETIONTIME;
            // 物品刪除範圍
            public int ITEMDELETIONRANGE;
            // 伺服器PVP設定
            public boolean SERVERPVPSETTING;
            // 簡化傳送處罰
            public boolean SIMWARPENALTY;
            // 回到床休息
            public boolean GETBACKREST;
            // 元素石數量
            public int ELEMENTALSTONEAMOUNT;
            // NPC最大物品
            public int NPCMAXITEM;
            // 亞丁超過數量
            public int ADEN_OVER_COUNT;
            // 亞丁物品ID
            public int ADEN_ITEMID;
            // 亞丁數量
            public int ADEN_COUNT;
            // 亞丁超過亞丁
            public int ADEN_OVER_ADEN;
            // 獎勵物品ID
            public int REWARD_ITEMID;
            // 獎勵數量
            public int REWARD_COUNT;
            // 用戶鬼魂計數
            public int USERGHOSTCOUNT;
            // 誰是反擊者
            public int WHOISCONTER;
            // 全球聊天等級
            public int GLOBALCHATLEVEL;
            // 私聊等級
            public int WHISPERCHATLEVEL;
            // 王冠血液等級
            public int CROWNBLOODLEVEL;
            // 王冠血液聯盟等級
            public int CROWNBLOOD_ALLIANCE_LEVEL;
            // 公會Buff用戶計數
            public int CLANBUFFUSERCOUNT;
            // 信件等級
            public int LETTERLEVEL;
            // 信件主題
            public int LETTER_SUBJECT;
            // 信件內容
            public int LETTER_CONTENT;
            // 版面等級
            public int BOARDLEVEL;
            // 替代掉落等級限制
            public int ALTDROPLEVELLIMIT;
            // 團隊用戶計數
            public int PARTYUSERCOUNT;
            // 團隊聊天計數
            public int PARTYCHATCOUNT;
            // 初始化BOSS生成
            public boolean INITBOSSSPAWN;
            // 物品欄位計數
            public int Inventory_Count;
            // 倉庫最大物品
            public int WAREHOUSEMAXITEM;
            // 公會倉庫最大物品
            public int CLANWAREHOUSEMAXITEM;
            // 用戶無商店等級
            public int USERNOSHOPLEVEL;
            // 公會首領
            public boolean CLANLEADER;
            // 公會職位設定
            public boolean CLANPCTITLESETTING;
            // 公會聯盟
            public boolean CLANALLIANCE;
            // 公會最大用戶計數
            public int CLANMAXUSERCOUNT;
            // 城堡戰
            public boolean CASTLEWAR;
            // 戰鬥最低等級
            public int WARMINLEVEL;
            // 戰鬥玩家
            public int WARPLAYER;
            // 战胜者奖励物品ID（领主）
            public int WAR_WIN_ITEMID_LEADER;
            // 战胜者奖励物品数量（领主）
            public int WAR_WIN_ITEMID_COUNT_LEADER;
            // 战胜者奖励物品ID
            public int WAR_WIN_ITEMID;
            // 战胜者奖励物品数量
            public int WAR_WIN_ITEMID_COUNT;
            // 戰盟稅收間隔
            public int HOUSETAXINTERVAL;
            // 血盟Buff
            public int LineageBuff;
            // 血盟Buff經驗比例
            public int LineageBuffExpRation;
            // 開始角色胚
            public int StartCharBoho;
            // 新手教程血盟
            public boolean TUTORIALCLAN;
            // 新公會名稱
            public String NEWCLANNAME;
            // 新玩家等級保護
            public int NEWPLAYERLEVELPROTECTION;
            // 新玩家等級淨化
            public int NEWPLAYERLEVELPURGE;
            // 公會設定保護
            public boolean CLANSETTINGPROTECTION;
            // 公會ID保護
            public int CLANIDPROTECTION;
            // 公會標誌保護
            public int CLANEMBPROTECTION;
            // 新玩家保護
            public int NEWPLAYERPROTECTION;
            // 隊伍經驗
            public boolean IsPartyExp;
            // 添加隊伍經驗
            public double AddPartyExp;
            // NPC最大數量YN
            public boolean NpcMaxYN;
            // NPC最大數量
            public int NpcMax;
            // NPC位置
            public int NpcLocation;
            // PC重新載入
            public int PcReload;
            // 經驗回歸
            public int Expreturn;
            // 新角色
            public int NewCha;
            // 新角色1
            public int NewCha1;
            // 召喚HP傷害
            public int summonhpdmg;
            // 新魔法傷害
            public int NewMagicDmg;
            // 紅色騎士死亡計數
            public int RedKnightdieCount;
            // 螞蟻女王物品ID
            public int QueenAnt_Itemid;
            // 螞蟻女王數量
            public int QueenAnt_Count;
            // 螞蟻女王磁場傷害
            public int QueenAnt_Magnetic_Dmg;
            // 螞蟻女王磁場時間
            public int QueenAnt_magnetic_time;
            // 無限戰鬥命中
            public boolean InfinityBattle_hit;
            // 無限戰鬥傷害
            public int InfinityBattle_dmg;
            // 無限戰鬥Boss傷害
            public int InfinityBattle_boss_dmg;
            // 獎勵等級1
            public int Bonus_Lvl1;
            // 獎勵等級2
            public int Bonus_Lvl2;
            // 獎勵等級3
            public int Bonus_Lvl3;
            // 獎勵等級4
            public int Bonus_Lvl4;
            // 獎勵等級5
            public int Bonus_Lvl5;
            // 獎勵物品1
            public int Bonus_Item1;
            // 獎勵物品2
            public int Bonus_Item2;
            // 獎勵物品3
            public int Bonus_Item3;
            // 獎勵物品4
            public int Bonus_Item4;
            // 獎勵物品5
            public int Bonus_Item5;
            // 獎勵物品計數1
            public int Bonus_Item_Count1;
            // 獎勵物品計數2
            public int Bonus_Item_Count2;
            // 獎勵物品計數3
            public int Bonus_Item_Count3;
            // 獎勵物品計數4
            public int Bonus_Item_Count4;
            // 獎勵物品計數5
            public int Bonus_Item_Count5;
            // 天
            public int day;
            // 小時
            public int hour;
            // 分鐘
            public int minute;
            // 秒
            public int second;
            // 限制小時
            public int limit_hour;
            // 限制分鐘
            public int limit_minute;
            // 限制秒
            public int limit_second;
            // 任務真實
            public boolean Quest_true;
            // 任務天
            public int Quest_day;
            // 任務小時
            public int Quest_hour;
            // 任務分鐘
            public int Quest_minute;
            // 任務秒
            public int Quest_second;
            // 刺殺等級1殺數
            public int AssassinationLevel1KillCount;
            // 刺殺等級2殺數
            public int AssassinationLevel2KillCount;
            // 殺敵計數1
            public int KillCount1;
            // 殺敵計數2
            public int KillCount2;
            // GAHO DBAN
            public boolean GAHO_DBAN;
            // 播放支持ARDEN
            public double PLAYSUPPORTARDEN;
            // 播放支持物品
            public double PLAYSUPPORTITEM;
            // 祝福
            public double Blessing;
            // 祝福亞丁比率
            public double AdenaRateOfBlessing;
            // 亞丁取消祝福比率
            public double AdenaRateOfUnBlessing;
            // 經驗位置
            public int ExpPosis;
            // 經驗位置1
            public int ExpPosis1;
            // 恐懼等級
            public int ScareLevel;
            // tamsc
            public int tamsc;
            // tamsc1
            public int tamsc1;
            // tamsc2
            public int tamsc2;
            // SCAEVENTITEMCHANCE
            public int SCAEVENTITEMCHANCE;
            // SCAEVENTITEMCHANCE1
            public int SCAEVENTITEMCHANCE1;
            // SCAEVENTITEMCHANCE2
            public int SCAEVENTITEMCHANCE2;
            // SCAEVENTITEM
            public int SCAEVENTITEM;
            // SCAEVENTITEM1
            public int SCAEVENTITEM1;
            // SCAEVENTITEM2
            public int SCAEVENTITEM2;
            // 字符槽
            public int CharactersCharSlot;
            // 賬戶N緩衝
            public boolean AccountNBuff;
            // 限制N緩衝
            public int LimitNBuff;
            // 時間N緩衝
            public int TimeNBuff;
            // NPC等級
            public int YNpclevel;
            // 稅率
            public int TaxRate;
            // 稅率最小
            public int TaxRateMin;
            // 稅率最大
            public int TaxRateMax;
            // 有效商店系統
            public boolean IsValidShopSystem;
            // 有效物品ID
            public boolean IsValidItemId;
            // 使用勾選框規則
            public boolean IsCheckBoxRulesUse;
            // ArdenTo
            public boolean ArdenTo;
            // Adentype
            public boolean Adentype;
            // 獵殺事件
            public boolean HuntEvent;
            // 武器物品ID
            public int[] WEAPONS_ITEMID;
            // 盔甲物品ID
            public int[] ARMORS_ITEMID;
            // 物品
            public int ITEMS;
            // 機會物品
            public int CHANCE_ITEMS;
            // 機會物品1
            public int CHANCE_ITEMS1;
            // 機會物品2
            public int CHANCE_ITEMS2;
            // RND_RKQT
            public int RND_RKQT;
            // PolyEvent2
            public boolean PolyEvent2;
            // PolyEvent2_level_down
            public int PolyEvent2_level_down;
            // PolyEvent1
            public boolean PolyEvent1;
            // PolyEvent
            public boolean PolyEvent;
            // 羽毛商店號碼
            public int FeatherShopNum;
            // FEATHER
            public boolean FEATHER;
            // BUGFIGHTCONTROL
            public boolean BUGFIGHTCONTROL;
            // COLOSSEUMOPEN
            public boolean COLOSSEUMOPEN;
            // 戰爭時間
            public TimeInfo WarTime;
            // 戰爭間隔
            public TimeInfo WarInterval;
            // 工藝小時
            public int craft_hour;
            // 工藝分鐘
            public int craft_minute;
            // 工藝秒
            public int craft_second;
            // 龍襲擊隨機
            public boolean DragonRaidRandom;
            // 全部工藝清單
            public boolean TotalCraftList;
            // 全部工藝清單類型
            public boolean TotalCraftList_type;
            // EINHASAD點系統
            public boolean EINHASAD_POINT_SYSTEM;
            // EINHASAD信仰系統
            public boolean EINHASAD_FAITH_SYSTEM;
            // 血盟貢獻
            public int CLAN_CONTRIBUTION;
            // 延遲計時器
            public boolean DelayTimer;
            // 魔法人偶系統
            public boolean MAGIC_DOLL_SYSTEM;
            // 大樂透分鐘
            public int LOTTO_MINUTE;
            // 大樂透小時
            public int LOTTO_HOUR;
            // 大樂透最大數字
            public int LOTTO_MAX_NUMBER;
            // 使用大樂透
            public boolean LOTTO_USE;
            // LV100AZIT
            public boolean LV100AZIT;
            // 時間收集工藝ID
            public int[] TIME_COLLECTION_CRAFT_ID;
            // 城堡血盟效果
            public int CASTLE_CLAN_EFFECT;

            // 時間信息
            public class TimeInfo {
                public int hour;
                public int minute;

                public TimeInfo(int hour, int minute) {
                    this.hour = hour;
                    this.minute = minute;
                }

                // 單位：天(5), 小時(11), 分鐘(12)
                // 時間：對應單位的時間
                public static class TimeInfo {
                    public int unit; // 單位
                    public int time; // 時間

                    public TimeInfo() {
                    }

                    public TimeInfo(int unit, int time) {
                        this.unit = unit;
                        this.time = time;
                    }
                }

                // 伺服器附魔信息
                public static ServerEnchantInfo ServerEnchant = new ServerEnchantInfo();

                public static class ServerEnchantInfo {
                    // TJ計數
                    public int TJCount;
                    // 附魔失敗日誌
                    public boolean enchantLostLog;
                    // 附魔值
                    public int Enchant_Value;
                    // 附魔值機率
                    public int Enchant_Value_Chance;
                    public int Enchant_Value_Chance1;
                    public int Enchant_Value_Chance2;
                    public int Enchant_Value1;
                    public int Enchant_Value2;
                    public int Enchant_Value_Chance3;
                    // 武器附魔計數
                    public int Enchant_Count_Weapon;
                    // 盔甲附魔計數
                    public int Enchant_Count_Armor;
                    // 飾品附魔計數
                    public int Enchant_Count_Accessory;
                    // 附魔最大失敗
                    public boolean EnchantMaxFail;
                    // 附魔費用
                    public int EnchantCoent;
                    // 單次附魔失敗率
                    public double EnchantFailRateOnest;
                    public double EnchantFailRateOnesto;
                    // 一次附魔失敗率
                    public double EnchantFailRateOne;
                    // 二次附魔失敗率
                    public double EnchantFailRateTwo;
                    // 大師附魔提示
                    public boolean MasterEnchantMess;
                    // 大師附魔值
                    public int MasterEnchant;
                    // 大師盔甲附魔計數
                    public int MasterArmorEnchant_Count;
                    public int MasterArmorEnchant;
                    public int NoltoArmorEnchant_Count;
                    public int NoltoArmorEnchant;
                    public int NoltoWeaponEnchant_Count;
                    public int NoltoWeaponEnchant;
                    public int MasterEnchant1;
                    public int MasterEnchant2;
                    public int MasterEnchant3;
                    // 雕刻附魔
                    public int CarvingEnchant;
                    // 武器祝福機率
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
                    // 連擊機率
                    public int ComboChance;
                    // 英雄武器附魔機率
                    public int Heroweapon;
                    // 古老符文
                    public int[] Antiquity_Rune;
                    // 普通原始
                    public int[] Normal_orim;
                    // 祝福原始
                    public int[] Bless_orim;
                    // 龍
                    public int[] Dragon;
                    // 羅姆提斯
                    public int[] Roomtis;
                    // 燦泊
                    public int[] Sanpper;
                    // 句子
                    public int[] Sentence;
                    // 徽章附魔
                    public int[] BADGEENCHANT;
                    // 武器附魔限制
                    public int LimitWeapon;
                    public int LimitWeapon2;
                    public int LimitArmor;
                    public int LimitArmor2;
                    // 武器重附魔
                    public int Weapon_ReEnchant;
                    public int Armor_ReEnchant;
                    // 羅姆提斯
                    public int RoomT;
                    public int Snapper;
                    // 附魔最大等級
                    public int SENTENCHMAXLEVEL;
                    // 項鍊
                    public int Pendant;
                    // 飾品限制
                    public int Accessory_Limit;
                    public int Accessory_Antiquity;
                    public int badgeLevel;
                    public int RoomtisLevel;
                    public int SanpperLevel;
                    public int SentenceLevel;
                    // 屬性附魔火
                    public int ATTR_ENCHANT_FIRE;
                    public int ATTR_ENCHANT_FIRE1;
                    public int ATTR_ENCHANT_FIRE2;
                    public int ATTR_ENCHANT_FIRE3;
                    public int ATTR_ENCHANT_FIRE4;
                    // 屬性附魔水
                    public int ATTR_ENCHANT_WATER;
                    public int ATTR_ENCHANT_WATER1;
                    public int ATTR_ENCHANT_WATER2;
                    public int ATTR_ENCHANT_WATER3;
                    public int ATTR_ENCHANT_WATER4;
                    // 屬性附魔風
                    public int ATTR_ENCHANT_WISH;
                    public int ATTR_ENCHANT_WISH1;
                    public int ATTR_ENCHANT_WISH2;
                    public int ATTR_ENCHANT_WISH3;
                    public int ATTR_ENCHANT_WISH4;
                    // 屬性附魔土
                    public int ATTR_ENCHANT_EARTH;
                    public int ATTR_ENCHANT_EARTH1;
                    public int ATTR_ENCHANT_EARTH2;
                    public int ATTR_ENCHANT_EARTH3;
                    public int ATTR_ENCHANT_EARTH4;
                    // 英雄武器附魔機率
                    public int Hero_weapon_per;
                    public int Hero_weapon_limit;
                    public int Hero_armor_per;
                    public int Hero_armor_limit;

                    public ServerEnchantInfo() {
                        // TODO: 初始化默認值
                    }
                }

                // TODO 道具選項信息
                public static ItemOptionInfo ItemOption = new ItemOptionInfo();

                public static class ItemOptionInfo {

                    public int writeC; // 寫入C
                    public boolean writeCBoolean; // 寫入C布爾值
                    public boolean writeDBoolean; // 寫入D布爾值
                    public boolean writeHBoolean; // 寫入H布爾值
                    public int Effect; // 效果

                    public ItemOptionInfo() {
                        writeC = 100;
                        writeCBoolean = false;
                        writeDBoolean = false;
                        writeHBoolean = false;
                        Effect = 0;
                    }
                }

                // TODO 同步信息
                public static SynchronizationInfo Synchronization = new SynchronizationInfo();

                public static class SynchronizationInfo {
                    public String Rent_Account; // 租賃帳號
                    public boolean Operation_Manager; // 運營管理員
                    public int AutosaveInterval; // 自動保存間隔
                    public int AutosaveIntervalOfInventory; // 庫存自動保存間隔
                    public int AutosaveExpInterval; // 經驗自動保存間隔
                    public boolean IsSellingsShopLocked; // 銷售商店已鎖定
                    public String TimeZone; // 時區
                    public boolean AutoCreateAccounts; // 自動創建帳號
                    public boolean CacheMapFiles; // 緩存地圖文件
                    public boolean LoadV2MapFiles; // 裝載V2地圖文件
                    public boolean CHARACTER_SAVED_SYSTEM; // 角色保存系統
                    public boolean CHARACTER_CHECK_SYSTEM; // 角色檢查系統
                    public boolean WALKPOSITIONCHECK; // 步行位置檢查
                    public int CPU_USAGE_USER; // 用戶CPU使用率
                    public boolean NETWORK_ATTACK_DELAY_FALSE; // 網絡攻擊延遲false
                    public int SPEESOVERCHECKCOUNT; // 速度超檢查計數
                    public int SPEESOVERCHECKCOUNT_AUTO; // 速度超檢查計數自動
                    public long NETWORK_ATTACK_DELAY_MILLIS; // 網絡攻擊延遲毫秒
                    public long NETWORK_DELAY_MILLIS; // 網絡延遲毫秒
                    public int _allMerchant_HashMap; // 所有商人哈希映射
                    public int _allClanWars_HashMap; // 所有氏族戰哈希映射
                    public int _allClans_HashMap; // 所有氏族哈希映射
                    public int _allNpc_HashMap; // 所有NPC哈希映射
                    public int _allNpcObjects_HashMap; // 所有NPC對象哈希映射
                    public int _allShopNpc_HashMap; // 所有商店NPC哈希映射
                    public int _allitem_HashMap; // 所有物品哈希映射
                    public int _allGuard_HashMap; // 所有守衛哈希映射
                    public int _allCastleGuard_HashMap; // 所有城堡守衛哈希映射
                    public int _allObjects_HashMap; // 所有對象哈希映射
                    public int Map4in_visibleObjects; // Map4in可見對象
                    public int Map4out_visibleObjects; // Map4out可見對象
                    public int Map4out_visiblePlayers; // Map4out可見玩家
                    public boolean NotBindProtoCode; // 不綁定Proto代碼
                    public boolean FindClientProtoCode; // 查找客戶端Proto代碼
                    public boolean FindServerProtoCode; // 查找服務器Proto代碼
                    public boolean FindServerProtoCodeFull; // 查找服務器Proto代碼完整

                    public SynchronizationInfo() {
                        Rent_Account = "";
                        Operation_Manager = true;
                        TimeZone = "KST";
                        CacheMapFiles = true;
                        LoadV2MapFiles = false;
                        AutoCreateAccounts = true;
                        IsSellingsShopLocked = true;
                        CHARACTER_SAVED_SYSTEM = true;
                        CHARACTER_CHECK_SYSTEM = true;
                        AutosaveInterval = 60;
                        AutosaveIntervalOfInventory = 60;
                        AutosaveExpInterval = 10;
                        WALKPOSITIONCHECK = true;
                        CPU_USAGE_USER = 70;
                        NETWORK_ATTACK_DELAY_FALSE = false;
                        SPEESOVERCHECKCOUNT = 5;
                        SPEESOVERCHECKCOUNT_AUTO = 10;
                        NETWORK_ATTACK_DELAY_MILLIS = 2L;
                        NETWORK_DELAY_MILLIS = 20L;
                        _allMerchant_HashMap = 2048;
                        _allClanWars_HashMap = 64;
                        _allClans_HashMap = 64;
                        _allNpc_HashMap = 5120;
                        _allNpcObjects_HashMap = 5120;
                        _allShopNpc_HashMap = 512;
                        _allitem_HashMap = 180;
                        _allGuard_HashMap = 256;
                        _allCastleGuard_HashMap = 128;
                        _allObjects_HashMap = 5120;
                        Map4in_visibleObjects = 2048;
                        Map4out_visibleObjects = 512;
                        Map4out_visiblePlayers = 128;
                        NotBindProtoCode = true;
                        FindClientProtoCode = false;
                        FindServerProtoCode = false;
                        FindServerProtoCodeFull = false;
                    }
                }

                // TODO 日誌狀態信息
                public static LogStatusInfo LogStatus = new LogStatusInfo();

                public static class LogStatusInfo {
                    public boolean GMATKMSG; // GM攻擊消息
                    public int ATTACKDELAYCOUNT_LOG; // 攻擊延遲計數日誌
                    public boolean WALKPOSITIONCHECK_LOG; // 步行位置檢查日誌
                    public boolean USEPOTIONEFFECTLOGGIN; // 使用藥水效果日誌
                    public boolean USEACTIONTIMELOGGING; // 使用動作時間日誌記錄
                    public byte LoggingWeaponEnchant; // 日誌武器附魔
                    public byte LoggingArmorEnchant; // 日誌盔甲附魔
                    public boolean LoggingChatNormal; // 正常聊天日誌記錄
                    public boolean LoggingChatWhisper; // 耳語聊天日誌記錄
                    public boolean LoggingChatShout; // 喊話聊天日誌記錄
                    public boolean LoggingChatWorld; // 世界聊天日誌記錄
                    public boolean LoggingChatClan; // 氏族聊天日誌記錄
                    public boolean LoggingChatParty; // 隊伍聊天日誌記錄
                    public boolean LoggingChatCombined; // 綜合聊天日誌記錄
                    public boolean LoggingChatChatParty; // 聊天隊伍日誌記錄

                    public LogStatusInfo() {
                        GMATKMSG = true;
                        ATTACKDELAYCOUNT_LOG = 10;
                        WALKPOSITIONCHECK_LOG = false;
                        USEPOTIONEFFECTLOGGIN = false;
                        USEACTIONTIMELOGGING = false;
                        LoggingWeaponEnchant = 1;
                        LoggingArmorEnchant = 1;
                        LoggingChatNormal = false;
                        LoggingChatWhisper = false;
                        LoggingChatShout = false;
                        LoggingChatWorld = false;
                        LoggingChatClan = false;
                        LoggingChatParty = false;
                        LoggingChatCombined = false;
                        LoggingChatChatParty = false;
                    }
                }

                // TODO 登錄狀態信息
                public static LoginStatusInfo Login = new LoginStatusInfo();

                public static class LoginStatusInfo {
                    public boolean InterServerUse; // 內部服務器使用
                    public boolean SERVERSTANDBY; // 服務器待機
                    public boolean UseExConnect; // 使用ExConnect
                    public boolean UseVersionCheck; // 使用版本檢查
                    public boolean LoginEncryption; // 登錄加密
                    public boolean CharPassword; // 角色密碼
                    public int CharPasswordMaximumFailureCount; // 角色密碼最大失敗次數
                    public boolean UseShiftServer; // 使用ShiftServer
                    public boolean StandbyServer; // 後備服務器
                    public String ExternalAddress; // 外部地址
                    public String LocalAddress; // 本地地址
                    public String CoderAddress; // 編碼器地址
                    public int GameserverPort; // 遊戲服務器端口
                    public short MaximumOnlineUsers; // 最大在線用戶數
                    public boolean CharacterConfigInServerSide; // 服務器端的角色配置
                    public boolean Allow2PC; // 允許2PC
                    public int LevelDownRange; // 等級下降範圍
                    public long worldDelayMillis; // 世界延遲毫秒
                    public int STUNSPEEDHACK; // 禁止速度作弊
                    public int PINGCHECK_SECOND; // PING檢查秒數
                    public int PINGCHECK; // PING檢查
                    public String ServerGmPhoneNumber; // 服務器GM電話號碼

                    public LoginStatusInfo() {
                        UseVersionCheck = true;
                        InterServerUse = false;
                        SERVERSTANDBY = true;
                        UseExConnect = true;
                        LoginEncryption = false;
                        CharPassword = true;
                        CharPasswordMaximumFailureCount = 5;
                        UseShiftServer = false;
                        StandbyServer = false;
                        ExternalAddress = "127.0.0.1";
                        LocalAddress = "127.0.0.1";
                        CoderAddress = "127.0.0.1";
                        GameserverPort = 5000;
                        MaximumOnlineUsers = 1024;
                        CharacterConfigInServerSide = true;
                        Allow2PC = true;
                        LevelDownRange = 90;
                        worldDelayMillis = 3000L;
                        STUNSPEEDHACK = 800;
                        PINGCHECK_SECOND = 60;
                        PINGCHECK = 100;
                        ServerGmPhoneNumber = "";
                    }
                }

                // TODO 連接信息
                public static ConnectionInfo Connection = new ConnectionInfo();

                public static class ConnectionInfo {
                    public boolean SendBusiness; // 發送業務
                    public int SCHEDULEDCOREPOOLSIZE; // 預定核心池大小
                    public int GeneralThreadPoolType; // 通用線程池類型
                    public int GeneralThreadPoolSize; // 通用線程池大小
                    public int AutomaticKick; // 自動踢出
                    public int PcRecognizeRange; // PC識別範圍
                    public boolean EnableDatabaseResourceLeaksDetection; // 啟用數據庫資源洩漏檢測

                    public ConnectionInfo() {
                        SendBusiness = false;
                        SCHEDULEDCOREPOOLSIZE = 128;
                        GeneralThreadPoolType = 2;
                        GeneralThreadPoolSize = 0;
                        AutomaticKick = 1;
                        PcRecognizeRange = -1;
                        EnableDatabaseResourceLeaksDetection = true;
                    }
                }

                // TODO 服務器比率信息
                public static ServerRatesInfo ServerRates = new ServerRatesInfo();

                public static class ServerRatesInfo {
                    public double RateXp; // 經驗倍率
                    public double RateDropAdena; // 掉落阿德納倍率
                    public double RateDropItems; // 掉落物品倍率
                    public double BloodBonus; // 血之獎勵
                    public double RateLawful; // 法律倍率
                    public double RateKarma; // 業報倍率
                    public int EnchantChanceWeapon; // 附魔機會（武器）
                    public int EnchantChanceArmor; // 附魔機會（護甲）
                    public int EnchantChanceAccessory; // 附魔機會（飾品）
                    public double RateWeightLimit; // 重量限制倍率
                    public double RateWeightLimitforPet; // 寵物重量限制倍率
                    public int DAMAGEENCHANT; // 傷害附魔
                    public int DAMAGEBLESSENCHANT; // 傷害祝福附魔
                    public double RateShopSellingPrice; // 商店出售價格倍率
                    public double RateShopPurchasingPrice; // 商店購買價格倍率
                    public int CreateChanceDiary; // 創建機會（日記）
                    public int CreateChanceRecollection; // 創建機會（回憶）
                    public int CreateChanceMysterious; // 創建機會（神秘）
                    public int CreateChanceProcessing; // 創建機會（處理）
                    public int CreateChanceProcessingDiamond; // 創建機會（處理鑽石）
                    public int CreateChanceDantes; // 創建機會（丹特斯）
                    public int CreateChanceHistoryBook; // 創建機會（歷史書）
                    public int FeatherTime; // 羽毛時間
                    public int FeatherNum; // 羽毛數量
                    public int FeatherNum1; // 羽毛數量1
                    public int FeatherNum2; // 羽毛數量2
                    public int TamTime; // 泰姆時間
                    public int TamNum; // 泰姆數量
                    public int TamNum1; // 泰姆數量1
                    public int TamNum2; // 泰姆數量2
                    public boolean Eventof; // 活動
                    public int EventTime; // 活動時間
                    public int EventItem; // 活動物品
                    public int EventNumber; // 活動數量
                    public double RateXpClaudia; // 克勞迪亞經驗倍率
                    public int AinBonusPoint_Monster; // 機械人獎勵點（怪物）
                    public int AinBonusPoint_Kard_Lucky; // 機械人獎勵點（卡德幸運）
                    public int AinBonusPoint_Kard_Probability; // 機械人獎勵點（卡德機率）

                    public ServerRatesInfo() {
                        RateXp = 5;
                        RateDropAdena = 5;
                        RateDropItems = 1;
                        BloodBonus = 0.1;
                        RateLawful = 2.0;
                        RateKarma = 3.0;
                        EnchantChanceWeapon = 10;
                        EnchantChanceArmor = 7;
                        EnchantChanceAccessory = 7;
                        RateWeightLimit = 1.2;
                        RateWeightLimitforPet = 3.0;
                        DAMAGEENCHANT = 20;
                        DAMAGEBLESSENCHANT = 10;
                        RateShopSellingPrice = 1.0;
                        RateShopPurchasingPrice = 1.0;
                        CreateChanceDiary = 67;
                        CreateChanceRecollection = 90;
                        CreateChanceMysterious = 90;
                        CreateChanceProcessing = 90;
                        CreateChanceProcessingDiamond = 90;
                        CreateChanceDantes = 40;
                        CreateChanceHistoryBook = 7;
                        FeatherTime = 20;
                        FeatherNum = 10;
                        FeatherNum1 = 5;
                        FeatherNum2 = 5;
                        TamTime = 30;
                        TamNum = 1000;
                        TamNum1 = 1000;
                        TamNum2 = 1000;
                        Eventof = true;
                        EventTime = 30;
                        EventItem = 4100181;
                        EventNumber = 3;
                        RateXpClaudia = 0.3;
                        AinBonusPoint_Monster = 1;
                        AinBonusPoint_Kard_Lucky = 20;
                        AinBonusPoint_Kard_Probability = 10000;
                    }
                }

                // TODO Web服務器信息
                public static WebServerInfo Web = new WebServerInfo();

                public static class WebServerInfo {
                    public boolean webServerParking; // Web服務器停車
                    public boolean webServerOnOff; // Web服務器啟用/停用
                    public int webServerPort; // Web服務器端口
                    public boolean appcenterCacheReset; // App中心緩存重置
                    public int[] adviceItems; // 建議項目
                    public boolean powerBall; // PowerBall
                    public String powerBallTime; // PowerBall時間
                    public int powerBallTicketEndTime; // PowerBall票結束時間
                    public boolean tradeMenu; // 交易菜單

                    public WebServerInfo() {
                        webServerParking = true;
                        webServerOnOff = true;
                        webServerPort = 8085;
                        appcenterCacheReset = true;
                        adviceItems = new int[] { 140074, 240074, 140087, 240087, 810003 };
                        powerBall = true;
                        powerBallTime = "02:50,02:51,02:52,02:53,02:54,02:55,07:50,07:51,07:52,07:53,07:54,07:55";
                        powerBallTicketEndTime = 30;
                        tradeMenu = false;
                    }
                }

                // TODO 玩偶附魔信息
                public static DollEnchantInfo DollEnchant = new DollEnchantInfo();

                public static class DollEnchantInfo {
                    public int WeaponMagicPer; // 武器魔法百分比
                    public int WeaponEnchantPerlvl1; // 武器附魔百分比等級1
                    public int WeaponEnchantPerlvl2; // 武器附魔百分比等級2
                    public int WeaponEnchantPerlvl3; // 武器附魔百分比等級3
                    public int WeaponEnchantPerlvl4; // 武器附魔百分比等級4
                    public int WeaponEnchantDmglvl1; // 武器附魔傷害等級1
                    public int WeaponEnchantDmglvl2; // 武器附魔傷害等級2
                    public int WeaponEnchantDmglvl3; // 武器附魔傷害等級3
                    public int WeaponEnchantDmglvl4; // 武器附魔傷害等級4

                    public DollEnchantInfo() {
                        WeaponMagicPer = 10;
                        WeaponEnchantPerlvl1 = 10;
                        WeaponEnchantPerlvl2 = 10;
                        WeaponEnchantPerlvl3 = 10;
                        WeaponEnchantPerlvl4 = 10;
                        WeaponEnchantDmglvl1 = 10;
                        WeaponEnchantDmglvl2 = 10;
                        WeaponEnchantDmglvl3 = 10;
                        WeaponEnchantDmglvl4 = 10;
                    }
                }

                // TODO 樂透信息
                public static LottoInfo lottoInfo = new LottoInfo();

                public static class LottoInfo {
                    public boolean run; // 運行
                    public int level; // 等級
                    public int batting; // 投注
                    public int bonus; // 獎金
                    public ArrayList<TimeInfo> times; // 時間

                    public LottoInfo() {
                        run = false;
                        level = 80;
                        batting = 10000000;
                        bonus = 20000000;
                        times = new ArrayList<>();
                    }

                    public static class TimeInfo {
                        public int hour; // 小時
                        public int minute; // 分鐘
                    }
                }

                // TODO 寶箱信息
                public static Treasure_Box TreasureBox = new Treasure_Box();

                public static class Treasure_Box {
                    public int[] LUUN_SECRET_BOX_ITEMS; // LUUN秘密箱子物品
                    public int[] LUUN_SECRET_BOX_COUNTS; // LUUN秘密箱子數量
                    public int[] LUUN_SECRET_BOX_PRO; // LUUN秘密箱子機率
                    public int[] EVA_BOX_ITEMS; // EVA箱子物品
                    public int[] EVA_BOX_COUNTS; // EVA箱子數量
                    public int[] EVA_BOX_PRO; // EVA箱子機率

                    public Treasure_Box() {
                        LUUN_SECRET_BOX_ITEMS = new int[] {};
                        LUUN_SECRET_BOX_COUNTS = new int[] {};
                        LUUN_SECRET_BOX_PRO = new int[] {};
                        EVA_BOX_ITEMS = new int[] {};
                        EVA_BOX_COUNTS = new int[] {};
                        EVA_BOX_PRO = new int[] {};
                    }
                }

                // TODO 角色設置信息
                public static CharSettingsInfo CharSettings = new CharSettingsInfo();

                public static class CharSettingsInfo {
                    public int[] NotCreateClass; // 不可創建的職業列表
                    public int STARTLEVEL; // 起始等級
                    public int[] START_LOC_X; // 起始位置X坐標
                    public int[] START_LOC_Y; // 起始位置Y坐標
                    public short MAPID_LIST; // 地圖ID列表
                    public int Level_Dmg; // 等級對應的傷害
                    public int Level_Dmg_Count; // 等級對應的傷害數量
                    public int DEATH_PENALTY_LEVEL_MIN; // 死亡懲罰最小等級
                    public int DEATH_PENALTY_LEVEL_MAX; // 死亡懲罰最大等級
                    public int DEATH_PENALTY_LEVEL_MIN1; // 死亡懲罰等級1的最小值
                    public int DEATH_PENALTY_LEVEL_MAX1; // 死亡懲罰等級1的最大值
                    public int DEATH_PENALTY_LEVEL_MIN2; // 死亡懲罰等級2的最小值
                    public int DEATH_PENALTY_LEVEL_MAX2; // 死亡懲罰等級2的最大值
                    // （以下略）
                    public double EXP_PENALTY; // 經驗懲罰
                    // （以下略）
                    public int aclevel; // AC等級
                    public double DodgePercent; // 閃避百分比
                    public double EvasionPercent; // 回避百分比
                    public double PVP_BOUNS; // PVP獎勵

                    public CharSettingsInfo() {
                        NotCreateClass = new int[] { 0 };
                        STARTLEVEL = 1;
                        START_LOC_X = new int[] { 32731, 32733, 32731, 32728, 32730 };
                        START_LOC_Y = new int[] { 32811, 32812, 32814, 32812, 32809 };
                        MAPID_LIST = 3;
                        Level_Dmg = 70;
                        Level_Dmg_Count = 1;
                        DEATH_PENALTY_LEVEL_MIN = 1;
                        // （以下略）
                        aclevel = 200;
                        DodgePercent = 1.0;
                        EvasionPercent = 1.0;
                        PVP_BOUNS = 0.2;
                        LIMITLEVEL = 100;
                    }
                }

                // TODO 煉金製作設置信息
                public static CraftAlchemyionInfo CraftAlchemySetting = new CraftAlchemyionInfo();

                public static class CraftAlchemyionInfo {
                    public int alchemynum; // 煉金數量
                    public double CRAFTINCREASEPROBBYMILLION; // 煉金增加概率（百萬分之）
                    public int CRAFTTRANSMITSAFELINE; // 煉金傳輸安全線
                    // （以下略）
                    public int[] CRAFT_VERSION_HASH; // 煉金版本哈希
                    public int[] ALCHEMY_VERSION_HASH; // 煉金版本哈希
                    public int[] SMELTING_VERSION_HASH; // 熔煉版本哈希

                    public CraftAlchemyionInfo() {
                        alchemynum = 0;
                        CRAFTINCREASEPROBBYMILLION = 1.50;
                        CRAFTTRANSMITSAFELINE = 1000;
                        // （以下略）
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
                        if (MJString.isNullOrEmpty(hashString)) {
                            return null;
                        }
                        return MJHexHelper.parseHexStringToInt32Array(hashString, " ");
                    }
                }

                public static void onCraftAlchemySettingLoad() {
                    try {
                        CraftAlchemySetting = MJJsonUtil.fromFile("./config/CraftAlchemySetting.json",
                                CraftAlchemyionInfo.class);
                        CraftAlchemySetting.onLoad();
                    } catch (Exception e) {
                        System.out.println("Config->CraftAlchemySetting.json 中出現錯誤。");
                        e.printStackTrace();
                        throw new Error("無法加載 ./config/CraftAlchemySetting.json 文件。");
                    }
                }

                public static void load() {
                    /******************************************
                     * 自訂配置 Json。（待辦事項） *
                     * 標題：json 設置 2018~2019 *
                     * 人物：自然:) *
                     * 執行：Notepad++ *
                     ******************************************/

                    onCraftAlchemySettingLoad();

                    try {
                        Einhasad_item = MJJsonUtil.fromFile("./config/Einhasad_item.json", EinhasadInfo.class);
                    } catch (Exception e) {
                        System.out.println("配置->Einhasad_item.json 發生錯誤。");
                        e.printStackTrace();
                        throw new Error("無法加載 ./config/Einhasad_item.json 文件。");
                    }
                    // （以下省略）
                    try {
                        CharSettings = MJJsonUtil.fromFile("./config/CharSettings.json", CharSettingsInfo.class);
                    } catch (Exception e) {
                        System.out.println("配置->CharSettings.json 發生錯誤。");
                        e.printStackTrace();
                        throw new Error("無法加載 ./config/CharSettings.json 文件。");
                    }
                    // （以下省略）
                }

validate();
            }

            // 驗證設置的方法
            private static void validate() {
                // 檢查項目刪除範圍是否在設定的合法範圍內
                if (!IntRange.includes(Config.ServerAdSetting.ITEMDELETIONRANGE, 0, 5)) {
                    throw new IllegalStateException("ItemDeletionRange 的值超出可設定範圍。");
                }
                // 檢查項目刪除時間是否在設定的合法範圍內
                if (!IntRange.includes(Config.ServerAdSetting.ITEMDELETIONTIME, 1, 35791)) {
                    throw new IllegalStateException("ItemDeletionTime 的值超出可設定範圍。");
                }
            }

            // 設置參數值的方法
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

            // 其他常數
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

            // 數據庫配置內部類
public static class DBConfig{
    public int min;
    public int max;
}
