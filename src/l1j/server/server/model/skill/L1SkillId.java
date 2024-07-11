 package l1j.server.server.model.skill;

 import java.util.HashMap;
 import l1j.server.Config;
 import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.AttendanceUserFlag;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_FOURTH_GEAR_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_USER_START_SUNDRY_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_GameGate.SC_GAMEGATE_PCCAFE_CHARGE_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
 import l1j.server.MJTemplate.MJProto.resultCode.CS_PC_MASTER_INFO_REQ;
 import l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_FAVOR_UPDATE_NOTI;
 import l1j.server.MJTemplate.MJSimpleRgb;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Liquor;
 import l1j.server.server.serverpackets.S_NewCreateItem;
 import l1j.server.server.serverpackets.S_OwnCharStatus;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillBrave;
 import l1j.server.server.serverpackets.S_SkillHaste;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1SkillId
 {
   public static final int NONE_EFFECTSKILL = 0;
   public static final int USER_WANTED1 = 32423423;
   public static final int USER_WANTED2 = 32423424;
   public static final int USER_WANTED3 = 32423425;
   public static final int TARAS_MOVE_SPEED = 5033;
   public static final int TARAS_ATTACK_SPEED = 5531;
   public static final int TARAS_1 = 5031;
   public static final int TARAS_2 = 5032;
   public static final int TELEPORT_RULER = 8463;
   public static final int POLY_RING_MASTER = 80012;
   public static final int POLY_RING_MASTER2 = 80013;
   public static final int DOMINION_TEL = 10959;
   public static final int TOP_RANKER = 40005;
   // 定義常量，表示特定的遊戲道具或效果ID
   public static final int 頂峰的祝福 = 80004; // 정상의가호
   public static final int PC_EXP_UP = 11223;
   public static final int RANK_BUFF_1 = 3521;
   public static final int RANK_BUFF_2 = 3522;
   public static final int RANK_BUFF_3 = 3523;
   public static final int RANK_BUFF_4 = 3524;
   public static final int RANK_BUFF_5_STR = 3525;
   public static final int RANK_BUFF_5_DEX = 3526;
   public static final int RANK_BUFF_5_INT = 3527;
   public static final int RANK_BUFF_6_STR = 3528;
   public static final int RANK_BUFF_6_DEX = 3529;
   public static final int RANK_BUFF_6_INT = 3530;
   public static final int RANK_BUFF_7_STR = 3531;
   public static final int RANK_BUFF_7_DEX = 3532;
   public static final int RANK_BUFF_7_INT = 3533;
   public static final int RANK_BUFF_8_STR = 3534;
   public static final int RANK_BUFF_8_DEX = 3535;
   public static final int RANK_BUFF_8_INT = 3536;
   // 定義常量，表示不同的遊戲道具或獎勵ID
   public static final int 火影感謝糖果 = 80005; // 나루토감사캔디
   public static final int 等級提升獎勵 = 80007; // 레벨업보너스
   public static final int DRAGON_PUPLE = 80008;
   public static final int DRAGON_TOPAZ = 80009;
   public static final int miso_Buff = 8132;
   public static final int miso_Buff1 = 8133;
   public static final int miso_Buff2 = 8134;
   public static final int DRAGON_SET = 3281;
   public static final int HUNTER_BLESS = 5934;
   public static final int HUNTER_BLESS3 = 9904;
   public static final int FOOD_BUFF = 124;
   public static final int NO_FOOD_BUFF = 8172;
   public static final int Fishing_etc = 6834;
   public static final int EXP_BUFF = 8382;
   public static final int EXP_BUFF = 8382;
   // 定義常量，表示特定的遊戲Buff ID
   public static final int 天下壯士Buff = 1541; // 천하장사버프
   public static final int SKILLS_BEGIN = 1;
   public static final int CLANBUFF_YES = 7789;
   public static final int God_buff = 4914;
   public static final int STATUS_PET_FOOD = 1019;
   public static final int NO_DIS = 60001;
   public static final int DELAY = 9999;
   // 定義常量，表示不同的遊戲Buff和道具的ID
   public static final int 主君的Buff = 10534; // 주군의버프
   public static final int 網咖Buff = 777888;  // 피씨방_버프
   public static final int 美味的河豚 = 11176; // 먹음직스러운복어
   public static final int 水鏡 = 11177;      // 수경
   public static final int MANADECREASEPOTION = 10676;
   public static final int N_BUFF_PVP_DMG = 4075;
   public static final int N_BUFF_HPMP = 4076;
   public static final int N_BUFF_DMG = 4077;
   public static final int N_BUFF_REDUCT = 4078;
   public static final int N_BUFF_SP = 4079;
   public static final int N_BUFF_STUN = 4080;
   public static final int N_BUFF_HOLD = 4081;
   public static final int N_BUFF_WATER_DMG = 4082;
   public static final int N_BUFF_WIND_DMG = 4083;
   public static final int N_BUFF_EARTH_DMG = 4084;
   public static final int N_BUFF_FIRE_DMG = 4085;
   public static final int N_BUFF_STR = 4086;
   public static final int N_BUFF_DEX = 4087;
   public static final int N_BUFF_INT = 4088;
   public static final int N_BUFF_WIS = 4089;
   public static final int N_BUFF_WATER = 4090;
   public static final int N_BUFF_WIND = 4091;
   public static final int N_BUFF_EARTH = 4092;
   public static final int N_BUFF_FIRE = 4093;
   public static final int N_BUFF_ALL_RESIST = 4094;
   public static final int CLAN_BUFF1 = 505;
   public static final int CLAN_BUFF2 = 506;
   public static final int CLAN_BUFF3 = 507;
   public static final int CLAN_BUFF4 = 508;
   public static final int Tam_Fruit1 = 7791;
   public static final int Tam_Fruit2 = 7792;
   public static final int Tam_Fruit3 = 7793;
   public static final int Tam_Fruit4 = 7794;
   public static final int Tam_Fruit5 = 7795;
   public static final int CRASH = 301;
   public static final int FURY = 302;
   public static final int SLAYER = 303;
   public static final int ARMORGUARD = 305;
   public static final int TITAN_LOCK = 306;
   public static final int TITAN_BLICK = 307;
   public static final int TITAN_MAGIC = 308;
   public static final int COUNTER_BARRIER_VETERAN = 310;
   public static final int ARMOR_BREAK_DESTINY = 311;
   public static final int DOUBLE_BRAKE_DESTINY = 312;
   public static final int DESPERADO_ABSOLUTE = 313;
   public static final int THUNDER_GRAB_BRAVE = 314;
   public static final int FOU_SLAYER_BRAVE = 315;
   public static final int FOU_SLAYER_FORCE = 7320184;
   public static final int AURAKIA = 316;
   public static final int DARKHORSE = 317;
   public static final int FINAL_BURN = 318;
   public static final int RESIST_ELEMENTAL = 319;
   public static final int GLORY_EARTH = 320;
   public static final int INFINITI_ARMOR = 321;
   public static final int INFINITI_BLICK = 322;
   public static final int INFINITI_DODGE = 323;
   public static final int DAMASCUS = 324;
   public static final int PARADOX = 325;
   public static final int GROWS = 326;
   public static final int RAGE = 327;
   public static final int PHANTOM_RIPER = 328;
   public static final int PHANTOM_DEATH = 329;
   public static final int FRAME = 330;
   public static final int INFINITI_BLOOD = 331;
   public static final int SUVIVE = 332;
   public static final int PANTERA_SHOCK = 334;
   public static final int BLOOD_TO_SOUL = 336;
   public static final int IMMUNE_TO_HARM_SAINT = 337;
   public static final int DESTROY_FEAR = 338;
   public static final int DESTROY_HORROR = 339;
   public static final int AURA = 340;
   public static final int REDUCTION_ARMOR_VETERAN = 341;
   public static final int RASING_FORCE = 342;
   public static final int IllUSION_LICH = 343;
   public static final int IllUSION_GOLEM = 344;
   public static final int BURNING_SPIRIT = 345;
   public static final int DRESS_EVASION = 346;
   public static final int LUCIFER_DESTINY = 347;
   public static final int MIYSTER_ACCURACY = 348;
   public static final int CRUEL_CONVICTION = 350;
   public static final int PRESSURE_DEATH_RECALL = 351;
   public static final int DODGE_BREAK = 353;
   public static final int MAELSTROM = 354;
   public static final int DEADLY_STRIKE = 355;
   public static final int VENGEANCE = 356;
   public static final int TACTICAL_ADVANCE = 357;
   public static final int INCREASE_RANGE = 358;
   public static final int DRAGON_SKIN = 359;
   public static final int SOLID_CARRIAGE = 361;
   public static final int PRIDE = 362;
   public static final int MEDITATION_BEYOND = 363;
   public static final int SHADOW_ARMOR_DESTINY = 364;
   public static final int SOLID_NOTE = 365;
   public static final int RAMPAGE = 366;
   public static final int BONE_BREAK_LAST = 367;
   public static final int MOVING_ACCELERATION_LAST = 369;
   public static final int DISINTEGRATE_NEMESIS = 374;
   public static final int HOLY_WALK_EVOLUTION = 375;
   public static final int ADVANCE_SPIRIT = 376;
   public static final int GREAT_SUMMON_MONSTER = 377;
   public static final int ARTERIAL_CIRCLE = 378;
   public static final int PHANTOM_REQUIEM = 379;
   public static final int EMPIRE_OVERLOAD = 380;
   public static final int SHINING_ARMOR = 381;
   public static final int MAJESTY = 382;
   public static final int GIGANTIC = 383;
   public static final int COUNTER_BARRIER_MASTER = 384;
   public static final int SOUL_BARRIER_ARMOR = 385;
   public static final int RAIGING_WEAPONE = 386;
   public static final int STRIKER_GALE_SHOT = 387;
   public static final int DEMOLITION = 388;
   public static final int BERSERK = 389;
   public static final int TITAN_BEAST = 390;
   public static final int TOMAHAWK_HUNTER = 391;
   public static final int TRIPLE_ARROW_BOOST = 392;
   public static final int FINE_SITE = 393;
   public static final int MORTAL_BODY = 394;
   public static final int SHADOW_STEP_CHASER = 395;
   public static final int BLIND_HIDING_ASSASSIN = 396;
   public static final int HEAL = 1;
   public static final int LIGHT = 2;
   public static final int SHIELD = 3;
   public static final int ENERGY_BOLT = 4;
   public static final int TELEPORT = 5;
   public static final int ICE_DAGGER = 6;
   public static final int WIND_CUTTER = 7;
   public static final int HOLY_WEAPON = 8;
   public static final int CURE_POISON = 9;
   public static final int CHILL_TOUCH = 10;
   public static final int CURSE_POISON = 11;
   public static final int ENCHANT_WEAPON = 12;
   public static final int DETECTION = 13;
   public static final int DECREASE_WEIGHT = 14;
   public static final int FIRE_ARROW = 15;
   public static final int STALAC = 16;
   public static final int LIGHTNING = 17;
   public static final int TURN_UNDEAD = 18;
   public static final int EXTRA_HEAL = 19;
   public static final int CURSE_BLIND = 20;
   public static final int BLESSED_ARMOR = 21;
   public static final int FROZEN_CLOUD = 22;
   public static final int WEAK_ELEMENTAL = 23;
   public static final int FIREBALL = 25;
   public static final int PHYSICAL_ENCHANT_DEX = 26;
   public static final int WEAPON_BREAK = 27;
   public static final int VAMPIRIC_TOUCH = 28;
   public static final int SLOW = 29;
   public static final int EARTH_JAIL = 30;
   public static final int COUNTER_MAGIC = 31;
   public static final int MEDITATION = 32;
   public static final int CURSE_PARALYZE = 33;
   public static final int CALL_LIGHTNING = 34;
   public static final int GREATER_HEAL = 35;
   public static final int TAMING_MONSTER = 36;
   public static final int REMOVE_CURSE = 37;
   public static final int CONE_OF_COLD = 38;
   public static final int MANA_DRAIN = 39;
   public static final int DARKNESS = 40;
   public static final int CREATE_ZOMBIE = 41;
   public static final int PHYSICAL_ENCHANT_STR = 42;
   public static final int HASTE = 43;
   public static final int CANCELLATION = 44;
   public static final int ERUPTION = 45;
   public static final int SUNBURST = 46;
   public static final int WEAKNESS = 47;
   public static final int BLESS_WEAPON = 48;
   public static final int HEAL_ALL = 49;
   public static final int FREEZEENG_ARMOR = 50;
   public static final int SUMMON_MONSTER = 51;
   public static final int HOLY_WALK = 52;
   public static final int TORNADO = 53;
   public static final int GREATER_HASTE = 54;
   public static final int BERSERKERS = 55;
   public static final int ENCHANT_ACURUCY = 56;
   public static final int FULL_HEAL = 57;
   public static final int FIRE_WALL = 58;
   public static final int BLIZZARD = 59;
   public static final int INVISIBILITY = 60;
   public static final int RESURRECTION = 61;
   public static final int EARTHQUAKE = 62;
   public static final int LIFE_STREAM = 63;
   public static final int SILENCE = 64;
   public static final int LIGHTNING_STORM = 65;
   public static final int FOG_OF_SLEEPING = 66;
   public static final int SHAPE_CHANGE = 67;
   public static final int IMMUNE_TO_HARM = 68;
   public static final int MASS_TELEPORT = 69;
   public static final int FIRE_STORM = 70;
   public static final int DECAY_POTION = 71;
   public static final int COUNTER_DETECTION = 72;
   public static final int DEATH_HEAL = 73;
   public static final int METEOR_STRIKE = 74;
   public static final int GREATER_RESURRECTION = 75;
   public static final int ICE_METEOR_STRIKE = 76;
   public static final int DISINTEGRATE = 77;
   public static final int ABSOLUTE_BARRIER = 78;
   public static final int FREEZING_BLIZZARD = 80;
   public static final int ETERNITI = 243;
   public static final int MATH_IMMUNE_TO_HARM = 5115;
   public static final int DEVINE_PROTECTION = 5152;
   public static final int SHOCK_STUN = 87;
   public static final int REDUCTION_ARMOR = 88;
   public static final int BOUNCE_ATTACK = 89;
   public static final int COUNTER_BARRIER = 91;
   public static final int ABSOLUTE_BLADE = 92;
   public static final int BLOW_ATTACK = 94;
   public static final int FORCE_STUN = 242;
   public static final int SHOCK_ATTACK = 5157;
   public static final int BLIND_HIDING = 97;
   public static final int ENCHANT_VENOM = 98;
   public static final int SHADOW_ARMOR = 99;
   public static final int BRING_STONE = 100;
   public static final int MOVING_ACCELERATION = 101;
   public static final int DARK_BLIND = 103;
   public static final int VENOM_RESIST = 104;
   public static final int DOUBLE_BRAKE = 105;
   public static final int UNCANNY_DODGE = 106;
   public static final int SHADOW_FANG = 107;
   public static final int DRESS_MIGHTY = 109;
   public static final int DRESS_DEXTERITY = 110;
   public static final int ARMOR_BRAKE = 112;
   public static final int SHADOW_STEP = 199;
   public static final int LUCIFER = 234;
   public static final int AVENGER = 244;
   public static final int weapon_F = 50010;
   public static final int TRUE_TARGET = 113;
   public static final int GLOWING_WEAPON = 114;
   public static final int SHINING_SHILD = 115;
   public static final int BRAVE_MENTAL = 117;
   public static final int GRACE = 122;
   public static final int EMPIRE = 123;
   public static final int PRIME = 241;
   public static final int PRIME_NO_SIEGE = 4398;
   public static final int PRIME_SIEGE = 4399;
   public static final int PRIME_SELF = 4400;
   public static final int CALL_CLAN_ADVENCE = 5112;
   public static final int TYRANT = 5028;
   public static final int BRAVE_UNION = 5047;
   public static final int CONQUEROR = 995048;
   public static final int CONQUEROR_STUN = 995049;
   public static final int TYRANT_EXCUTION = 995050;
   public static final int RESIST_MAGIC = 129;
   public static final int BODY_TO_MIND = 130;
   public static final int TELEPORT_TO_MATHER = 131;
   public static final int TRIPLE_ARROW = 132;
   public static final int ELEMENTAL_FALL_DOWN = 133;
   public static final int COUNTER_MIRROR = 134;
   public static final int SOUL_BARRIER = 135;
   public static final int INFERNO = 136;
   public static final int TRIPLE_STUN = 7320183;
   public static final int CLEAR_MIND = 137;
   public static final int MAFR = 139;
   public static final int RETURN_TO_NATURE = 145;
   public static final int BLOODY_SOUL = 146;
   public static final int ELEMENTAL_PROTECTION = 147;
   public static final int EARTH_WEAPON = 148;
   public static final int AQUA_SHOT = 149;
   public static final int EAGGLE_EYE = 150;
   public static final int FIRE_SHIELD = 151;
   public static final int QUAKE = 152;
   public static final int ERASE_MAGIC = 153;
   public static final int LESSER_ELEMENTAL = 154;
   public static final int DANCING_BLADES = 155;
   public static final int STORM_EYE = 156;
   public static final int EARTH_BIND = 157;
   public static final int NATURES_TOUCH = 158;
   public static final int EARTH_GUARDIAN = 159;
   public static final int AQUA_PROTECTER = 160;
   public static final int AREA_OF_SILENCE = 161;
   public static final int GREATER_ELEMENTAL = 162;
   public static final int BURNING_WEAPON = 163;
   public static final int NATURES_BLESSING = 164;
   public static final int CALL_OF_NATURE = 165;
   public static final int STORM_SHOT = 166;
   public static final int CYCLONE = 167;
   public static final int IRON_SKIN = 168;
   public static final int EXOTIC_VITALIZE = 169;
   public static final int WATER_LIFE = 170;
   public static final int ELEMENTAL_FIRE = 171;
   public static final int STORM_WALK = 172;
   public static final int POLLUTE_WATER = 173;
   public static final int STRIKER_GALE = 174;
   public static final int SOUL_OF_FLAME = 175;
   public static final int ADDITIONAL_FIRE = 176;
   public static final int FOCUS_WAVE = 177;
   public static final int HURRICANE = 178;
   public static final int SAND_STORM = 179;
   public static final int LIBERATION = 5113;
   public static final int BURNING_SHOT = 5158;
   public static final int VISION_TELEPORT = 5153;
   public static final int BURNING_SLASH = 182;
   public static final int DESTROY = 183;
   public static final int MAGMA_BREATH = 184;
   public static final int SCALES_EARTH_DRAGON = 185;
   public static final int BLOOD_LUST = 186;
   public static final int FOU_SLAYER = 187;
   public static final int MAGMA_ARROW = 189;
   public static final int SCALES_WATER_DRAGON = 190;
   public static final int THUNDER_GRAB = 192;
   public static final int EYE_OF_DRAGON = 194;
   public static final int SCALES_FIRE_DRAGON = 195;
   public static final int SCALES_RINDVIOR_DRAGON = 197;
   public static final int HALPAS = 245;
   public static final int BEHEMOTH = 5057;
   public static final int CHAIN_REACTION = 5059;
   public static final int MIRROR_IMAGE = 201;
   public static final int CONFUSION = 202;
   public static final int SMASH = 203;
   public static final int IllUSION_OGRE = 204;
   public static final int CUBE_OGRE = 205;
   public static final int CONCENTRATION = 206;
   public static final int MIND_BREAK = 207;
   public static final int BONE_BREAK = 208;
   public static final int CUBE_GOLEM = 210;
   public static final int PATIENCE = 211;
   public static final int PHANTASM = 212;
   public static final int IZE_BREAK = 213;
   public static final int CUBE_RICH = 215;
   public static final int INSIGHT = 216;
   public static final int PANIC = 217;
   public static final int REDUCE_WEIGHT = 218;
   public static final int IllUSION_AVATAR = 219;
   public static final int CUBE_AVATAR = 220;
   public static final int IMPACT = 222;
   public static final int FOCUS_SPRITS = 223;
   public static final int MOEBIUS = 224;
   public static final int POTENTIAL = 246;
   public static final int ENSNARE = 5036;
   public static final int OSIRIS = 5037;
   public static final int OSIRIS_TICK = 15037;
   public static final int HOWL = 225;
   public static final int POWERRIP = 228;
   public static final int TOMAHAWK = 229;
   public static final int DESPERADO = 230;
   public static final int TITANL_RISING = 231;
   public static final int SKILLS_END = 240;
   public static final int TEMPEST = 5027;
   public static final int JUDGEMENT = 5001;
   public static final int PHANTOM = 5002;
   public static final int PANTHERA = 5003;
   public static final int BLADE = 5004;
   public static final int HELLFIRE = 5005;
   public static final int ASURA = 5017;
   public static final int FLAME_ATTACK = 50000;
   public static final int DEATH_POTION = 40000;
   public static final int ALTERNATE = 5051;
   public static final int FORCE_WAVE = 5052;
   public static final int VANGUARD = 5053;
   public static final int RECOVERY = 5054;
   public static final int PRESHER = 5055;
   public static final int CRUEL = 5056;
   public static final int STATUS_WISDOM_POTION = 4516;
   public static final int STATUS_WISDOM_POTION_POWER = 4547;
   public static final int STATUS_BEGIN = 1000;
   public static final int STATUS_BRAVE = 1000;
   public static final int STATUS_HASTE = 1001;
   public static final int STATUS_BLUE_POTION = 1002;
   public static final int STATUS_UNDERWATER_BREATH = 1003;
   public static final int STATUS_CHAT_PROHIBITED = 1005;
   public static final int STATUS_POISON = 1006;
   public static final int STATUS_POISON_SILENCE = 1007;
   public static final int STATUS_POISON_PARALYZING = 1008;
   public static final int STATUS_POISON_PARALYZED = 1009;
   public static final int STATUS_CURSE_PARALYZING = 1010;
   public static final int STATUS_CURSE_PARALYZED = 1011;
   public static final int STATUS_FLOATING_EYE = 1012;
   public static final int STATUS_HOLY_WATER = 1013;
   public static final int STATUS_HOLY_MITHRIL_POWDER = 1014;
   public static final int STATUS_HOLY_WATER_OF_EVA = 1015;
   public static final int STATUS_ELFBRAVE = 1016;
   public static final int STATUS_CANCLEEND = 1016;
   public static final int STATUS_CURSE_BARLOG = 1017;
   public static final int STATUS_CURSE_YAHEE = 1018;
   public static final int STATUS_TOMAHAWK = 1020;
   public static final int STATUS_END = 1020;
   public static final int GMSTATUS_BEGIN = 2000;
   public static final int GMSTATUS_INVISIBLE = 2000;
   public static final int GMSTATUS_HPBAR = 2001;
   public static final int GMSTATUS_SHOWTRAPS = 2002;
   public static final int GMSTATUS_END = 2002;
   public static final int COOKING_NOW = 2999;
   public static final int COOKING_BEGIN = 3000;
   public static final int COOKING_1_0_N = 3000;
   public static final int COOKING_1_1_N = 3001;
   public static final int COOKING_1_2_N = 3002;
   public static final int COOKING_1_3_N = 3003;
   public static final int COOKING_1_4_N = 3004;
   public static final int COOKING_1_5_N = 3005;
   public static final int COOKING_1_6_N = 3006;
   public static final int COOKING_1_7_N = 3007;
   public static final int COOKING_1_8_N = 3008;
   public static final int COOKING_1_9_N = 3009;
   public static final int COOKING_1_10_N = 3010;
   public static final int COOKING_1_11_N = 3011;
   public static final int COOKING_1_12_N = 3012;
   public static final int COOKING_1_13_N = 3013;
   public static final int COOKING_1_14_N = 3014;
   public static final int COOKING_1_15_N = 3015;
   public static final int COOKING_1_16_N = 3016;
   public static final int COOKING_1_17_N = 3017;
   public static final int COOKING_1_18_N = 3018;
   public static final int COOKING_1_19_N = 3019;
   public static final int COOKING_1_20_N = 3020;
   public static final int COOKING_1_21_N = 3021;
   public static final int COOKING_1_22_N = 3022;
   public static final int COOKING_1_23_N = 3023;
   public static final int COOKING_1_0_S = 3050;
   public static final int COOKING_1_1_S = 3051;
   public static final int COOKING_1_2_S = 3052;
   public static final int COOKING_1_3_S = 3053;
   public static final int COOKING_1_4_S = 3054;
   public static final int COOKING_1_5_S = 3055;
   public static final int COOKING_1_6_S = 3056;
   public static final int COOKING_1_7_S = 3057;
   public static final int COOKING_1_8_S = 3058;
   public static final int COOKING_1_9_S = 3059;
   public static final int COOKING_1_10_S = 3060;
   public static final int COOKING_1_11_S = 3061;
   public static final int COOKING_1_12_S = 3062;
   public static final int COOKING_1_13_S = 3063;
   public static final int COOKING_1_14_S = 3064;
   public static final int COOKING_1_15_S = 3065;
   public static final int COOKING_1_16_S = 3066;
   public static final int COOKING_1_17_S = 3067;
   public static final int COOKING_1_18_S = 3068;
   public static final int COOKING_1_19_S = 3069;
   public static final int COOKING_1_20_S = 3070;
   public static final int COOKING_1_21_S = 3071;
   public static final int COOKING_1_22_S = 3072;
   public static final int COOKING_1_23_S = 3073;
   public static final int COOK_STR = 3074;
   public static final int COOK_DEX = 3075;
   public static final int COOK_INT = 3076;
   public static final int COOK_GROW = 3077;
   public static final int COOK_STR_Bless = 3100;
   public static final int COOK_DEX_Bless = 3101;
   public static final int COOK_INT_Bless = 3102;
   public static final int COOK_GROW_Bless = 3103;
     // 定義常量，表示不同食物的ID
     public static final int 修練者的韓牛牛排 = 3080;
     public static final int 修練者的鮭魚蒸菜 = 3081;
     public static final int 修練者的火雞烤肉 = 3082;
     public static final int 修練者的雞肉湯 = 3083;
     public static final int 阿登的特製牛排 = 3104;
     public static final int 阿登的特製開胃菜 = 3105;
     public static final int 阿登的特製沙拉 = 3106;
     public static final int 阿登的番茄湯 = 3107;
     public static final int 祝福的阿登特製牛排 = 3108;
     public static final int 祝福的阿登特製開胃菜 = 3109;
     public static final int 祝福的阿登特製沙拉 = 3110;
     public static final int 祝福的阿登番茄湯 = 3111;
     public static final int 烹飪結束 = 3077;
     public static final int 感謝糖果 = 3078;
     public static final int 狀態凍結 = 10071;
   public static final int CURSE_PARALYZE2 = 10101;
   public static final int STATUS_IGNITION = 20075;
   public static final int STATUS_QUAKE = 20076;
   public static final int STATUS_SHOCK = 20077;
   public static final int STATUS_BALANCE = 20078;
   public static final int STATUS_FRUIT = 20079;
   public static final int STATUS_OVERLAP = 20080;
   public static final int EXP_POTION = 23069;
   public static final int EXP_POTION_Event = 13069;
   public static final int STATUS_BLUE_POTION2 = 20082;
   public static final int STATUS_DESHOCK = 20083;
   public static final int STATUS_CUBE = 20084;
   public static final int STATUS_CASHSCROLL = 7893;
   public static final int STATUS_CASHSCROLL2 = 7894;
   public static final int STATUS_CASHSCROLL3 = 7895;
   public static final int STATUS_CASHSCROLL4 = 16553;
   public static final int STATUS_CASHSCROLL5 = 16552;
   public static final int STATUS_CASHSCROLL6 = 16551;
   public static final int MOB_SLOW_18 = 30000;
   public static final int MOB_SLOW_1 = 30001;
   public static final int MOB_CURSEPARALYZ_19 = 30002;
   public static final int MOB_COCA = 30003;
   public static final int MOB_BASILL = 30004;
   public static final int MOB_RANGESTUN_19 = 30005;
   public static final int MOB_RANGESTUN_18 = 30006;
   public static final int MOB_RANGESTUN_20 = 30010;
   public static final int MOB_SHOCKSTUN_30 = 30081;
   public static final int Mob_RANGESTUN_30 = 40007;
   public static final int MOB_CURSEPARALYZ_18 = 30007;
   public static final int MOB_DISEASE_30 = 30008;
   public static final int MOB_WEAKNESS_1 = 30009;
   public static final int MOB_DISEASE_1 = 30079;
   public static final int MOB_WINDSHACKLE_1 = 30084;
   public static final int ANTI_DISINTEGRATE = 50001;
   public static final int CHAINSWORD1 = 51002;
   public static final int CHAINSWORD2 = 51003;
   public static final int CHAINSWORD3 = 51004;
   public static final int CHAINSWORD4 = 51005;
   public static final int CHAINSWORD_STUN = 51006;
   public static final int CHAINSWORD_STUN_REUSE_TIME = 51007;
   public static final int ANTI_FINAL_BURN = 50005;
   public static final int COMA_A = 50006;
   public static final int COMA_B = 50007;
   public static final int ANTI_METEOR = 50008;
   public static final int LINDBIOR_SPIRIT_EFFECT = 50009;
   public static final int BUFF_CRAY = 50011;
   public static final int BUFF_Vala = 50013;
   public static final int BUFF_SAEL = 10499;
   public static final int BUFF_GUNTER = 120384;
   public static final int FEATHER_BUFF_A = 22000;
   public static final int FEATHER_BUFF_B = 22001;
   public static final int FEATHER_BUFF_C = 22002;
   public static final int FEATHER_BUFF_D = 22003;
   public static final int SetBuff = 90008;
   public static final int ANTA_MAAN = 7671;
   public static final int FAFU_MAAN = 7672;
   public static final int LIND_MAAN = 7673;
   public static final int VALA_MAAN = 7674;
   public static final int BIRTH_MAAN = 7675;
   public static final int SHAPE_MAAN = 7676;
   public static final int LIFE_MAAN = 7678;
   public static final int SIDE_OF_ME_BLESSING = 7679;
   public static final int RE_START_BLESSING = 7680;
   public static final int NEW_START_BLESSING = 7681;
   public static final int LIFE_BLESSING = 7682;
   public static final int BLACK_DRAGON_MAAN = 7687;
   public static final int NAVER_BLACK_DRAGON_MAAN = 7688;
   public static final int ANTA_MESSAGE_1 = 22020;
   public static final int ANTA_MESSAGE_2 = 22021;
   public static final int ANTA_MESSAGE_3 = 22022;
   public static final int ANTA_MESSAGE_4 = 22023;
   public static final int ANTA_MESSAGE_5 = 22024;
   public static final int ANTA_MESSAGE_6 = 22025;
   public static final int ANTA_MESSAGE_7 = 22026;
   public static final int ANTA_MESSAGE_8 = 22027;
   public static final int ANTA_MESSAGE_9 = 22028;
   public static final int ANTA_MESSAGE_10 = 22029;
   public static final int ANTA_CANCELLATION = 22030;
   public static final int ANTA_SHOCKSTUN = 22031;
   public static final int ANTA_WEAPON_BREAK = 22032;
   public static final int PREDICATEDELAY = 22033;
   public static final int PAP_FIVEPEARLBUFF = 22035;
   public static final int PAP_MAGICALPEARLBUFF = 22036;
   public static final int PAP_DEATH_PORTION = 22037;
   public static final int PAP_DEATH_HELL = 22038;
   public static final int PAP_REDUCE_HELL = 22039;
   public static final int PAP_PREDICATE1 = 22041;
   public static final int PAP_PREDICATE3 = 22043;
   public static final int PAP_PREDICATE5 = 22045;
   public static final int PAP_PREDICATE6 = 22046;
   public static final int PAP_PREDICATE7 = 22047;
   public static final int PAP_PREDICATE8 = 22048;
   public static final int PAP_PREDICATE9 = 22049;
   public static final int PAP_PREDICATE11 = 22051;
   public static final int PAP_PREDICATE12 = 22052;
   public static final int ANTA_BUFF = 22015;
   public static final int FAFU_BUFF = 22016;
   public static final int STATUS_DRAGON_PEARL = 22017;
   public static final int EMERALD_YES = 22018;
   public static final int EMERALD_NO = 22019;
   public static final int RIND_BUFF = 22060;
   public static final int OMAN_CANCELLATION = 22056;
   public static final int ICE_ERUPTION = 22058;
   public static final int RINDVIOR_WIND_SHACKLE = 7001;
   public static final int RINDVIOR_PREDICATE_CANCELLATION = 7002;
   public static final int RINDVIOR_TORNADO_FORE = 7003;
   public static final int RINDVIOR_WEAPON = 7004;
   public static final int RINDVIOR_BOW = 7005;
   public static final int RINDVIOR_WIND_SHACKLE_1 = 7007;
   public static final int RINDVIOR_WEAPON_2 = 7008;
   public static final int RINDVIOR_STORM = 7009;
   public static final int RINDVIOR_CANCELLATION = 7010;
   public static final int RINDVIOR_SILENCE = 7013;
   public static final int RINDVIOR_SUMMON_MONSTER = 7018;
   public static final int RINDVIOR_PREDICATE = 7019;
   public static final int RINDVIOR_SUMMON_MONSTER_CLOUD = 7023;
   public static final int Phoenix_Skill1 = 707049;
   public static final int Phoenix_Skill2 = 707050;
   public static final int PHOENIX_CANCELLATION = 7024;
   public static final int PHOENIX_SUMMON_MONSTER = 7025;
   public static final int EFRETE_SUMMON_MONSTER = 7028;
   public static final int DRAKE_Skill1 = 707051;
   public static final int DRAKE_Skill2 = 707052;
   public static final int DRAKE_WIND_SHACKLE = 7035;
   public static final int DRAKE_MASSTELEPORT = 7036;
   public static final int MOB_DEATH_POTION = 707137;
   public static final int COMBO_BUFF = 85010;
   public static final int BLACKELDER_DEATH_POTION = 7030;
   public static final int BLACKELDER_DEATH_HELL = 7031;
   public static final int BLACKELDER = 7037;
   public static final int DESERT_SKILL1 = 7041;
   public static final int DESERT_SKILL2 = 7042;
   public static final int DESERT_SKILL3 = 7043;
   public static final int DESERT_SKILL4 = 7044;
   public static final int DESERT_SKILL5 = 7045;
   public static final int DESERT_SKILL6 = 7046;
   public static final int DESERT_SKILL7 = 7047;
   public static final int DESERT_SKILL8 = 7048;
   public static final int DESERT_SKILL9 = 7049;
   public static final int DESERT_SKILL10 = 7050;
   public static final int DESERT_SKILL11 = 707117;
   public static final int erzabe_worms = 7040;
   public static final int Sand_worms = 7055;
   public static final int Sand_worms1 = 7056;
   public static final int Sand_worms2 = 7057;
   public static final int Sand_worms3 = 7058;
   public static final int WIDE_ARMORBREAK = 7059;
   public static final int BOSS_COUNTER_BARRIER = 7060;
   public static final int MOB_TRIPLE_ARROW = 10210;
   public static final int MOB_TRIPLE_ARROW_PRISON = 10211;
   public static final int CONE_OF_COLD_mob = 707038;
   public static final int MOB_HASTE = 707114;
   public static final int MOB_METEOR = 707128;
   public static final int MOB_METEOR_OR = 707129;
   public static final int MOB_SKILL_NEW = 707151;
   public static final int MOB_SKILL_EGG = 707153;
   public static final int IMMUNE_TO_HARM_BLADE = 707167;
   public static final int MOB_ETERNITI = 707168;
   public static final int doctor_skills1 = 707192;
   public static final int doctor_skills = 707187;
   public static final int DRAGON_HALPAS = 2100000;
   public static final int DRAGON_HALPAS_POWER = 2100001;
   public static final int DRAGON_HALPAS_STUN = 707159;
   public static final int DRAGON_HALPAS_POISON = 707157;
   public static final int DRAGON_HALPAS_FIRE = 707156;
   public static final int DRAGON_HALPAS_WATER = 707160;
   public static final int DRAGON_HALPAS_WISH = 707161;
   // 定義常量，表示不同的遊戲道具和Buff的ID
   public static final int 美蒂斯祝福卷軸 = 3000128; // 메티스축복주문서
   public static final int 美蒂斯精心料理 = 3000129; // 메티스정성요리
   public static final int 美蒂斯精心湯 = 3000130;   // 메티스정성스프
   public static final int 力量冰砂 = 10524;        // 완력빙수
   public static final int 敏捷冰砂 = 10525;        // 민첩빙수
   public static final int 智力冰砂 = 10526;        // 지식빙수
   public static final int TROGIR_MILPITAS = 252;
   public static final int TROGIR_MILPITAS1 = 253;
   public static final int TROGIR_MILPITAS2 = 254;
   public static final int TROGIR_MILPITAS3 = 10126;
   public static final int TROGIR_MILPITAS4 = 10127;
   public static final int TROGIR_MILPITAS5 = 10128;
   public static final int TROGIR_MILPITAS6 = 10129;
   public static final int TROGIR_MILPITAS7 = 10130;
   public static final int DRAGONRAID_BUFF = 55005;
   public static final int EINHASAD_AMPLIFIER = 6432;
   public static final int ARMOR_BLESSING = 8000;
   public static final int DRAGON_ARMOR_BLESSING = 8001;
   public static final int DRAGON_ARMOR_BLESSING_REDUC = 8002;
   public static final int DRAGON_ARMOR_EQUIP = 8003;
   public static final int Start_BUFF = 2563;
   public static final int Maeno_STUN = 707054;
   public static final int Besi_STUN = 707056;
   public static final int fornos_STUN = 707041;
   public static final int Moster_STUN = 707099;
   public static final int MOSTER_STUN_1 = 707119;
   public static final int BUYER_COOLTIME = 707070;
   public static final int OMAN_STUN = 22055;
   public static final int BOS_STUN18 = 707113;
   public static final int BALOCH_STUN = 707152;
   public static final int DEATH_HEAL_Mob = 707073;
   public static final int ZENITH_Poison = 707075;
   public static final int MOB_CURSEPARALYZ1 = 707095;
   public static final int GROW_LEAF = 3919;
   public static final int EIN_LEAF = 3920;
   public static final int HEAVEN_LEAF = 3921;
   public static final int PITULL_YEAGER_BARM = 3922;
   public static final int RABID_DOG_BLOOD = 4001;
   public static final int MOB_Kurtz_Pack = 707108;
   public static final int WITCH_MANA_POTION = 70702;
   public static final int WITCH_MANA_POTION1 = 70703;
   public static final int WEEK_BOX = 71000;
   public static final int DECIDING_BUFF = 85000;
   public static final int HERO_GAHO_BUFF = 85001;
   public static final int HIGH_CLASS_GAHO_BUFF = 12000;
   public static final int DISEASE = 70704;
   public static final int ICE_LANCE = 70705;
   public static final int Safemode = 9006;
   public static final int ASSASSINATION_LEVEL_1 = 9596;
   public static final int ASSASSINATION_LEVEL_2 = 9597;
   public static final int DOLL_JUDGEMENT = 9598;
   public static final int STR_BUFF = 60208;
   public static final int DEX_BUFF = 60209;
   public static final int INT_BUFF = 60210;
   public static final int STR_ADEN_SCROLL_BUFF = 60211;
   public static final int DEX_ADEN_SCROLL_BUFF = 60212;
   public static final int INT_ADEN_SCROLL_BUFF = 60213;
   public static final int EINHASAD_GRACE = 7116;
   public static final int DRAGON_EMERALD = 4004;
   public static final int DRAGON_BLESS = 8689;
   public static final int EINHASAD_BUFF_1ST = 888803;
   public static final int EINHASAD_BUFF_2ND = 888804;
   public static final int EINHASAD_BUFF_3RD = 888805;
   public static final int FORCE_STUN_FAIL = 100242;
   public static final int[] ASSASSNATIONS = new int[] { 9596, 9597 };




   public static final int[] COMPANION_BUFFS = new int[] { 3919, 3920, 3921, 3922, 4001 };

   public static final int WIND_SHACKLE = 777777;

   public static final int SAFE_MODE = 707100;

   public static final int EINHASAD_PRIMIUM_FLAT = 80000;

   public static final int EINHASAD_GREAT_FLAT = 80001;

   public static final int PC_CAFE = 80002;

   public static final int CLAN_EXP_BUFF_1ST = 888806;

   public static final int CLAN_EXP_BUFF_2ND = 888807;

   public static final int CLAN_PVP_BUFF_1ST = 888808;

   public static final int CLAN_PVP_BUFF_2ND = 888809;

   public static final int CLAN_DEFENCE_BUFF_1ST = 888820;

   public static final int CLAN_DEFENCE_BUFF_2ND = 888821;

   public static final int CLASS_RANK_BLESS_PRINCE_1 = 888810;

   public static final int CLASS_RANK_BLESS_KNIGHT_1 = 888811;
   public static final int CLASS_RANK_BLESS_ELF_1 = 888812;
   public static final int CLASS_RANK_BLESS_WIZARD_1 = 888813;
   public static final int CLASS_RANK_BLESS_DARKELF_1 = 888814;
   public static final int CLASS_RANK_BLESS_DRAGONKNIGHT_1 = 888815;
   public static final int CLASS_RANK_BLESS_BLACKWIZARD_1 = 888816;
   public static final int CLASS_RANK_BLESS_WARRIOR_1 = 888817;
   public static final int CLASS_RANK_BLESS_FENCER_1 = 888818;
   public static final int CLASS_RANK_BLESS_LANCER_1 = 888819;
   public static final int CLASS_RANK_BLESS_PRINCE_2 = 888822;
   public static final int CLASS_RANK_BLESS_KNIGHT_2 = 888823;
   public static final int CLASS_RANK_BLESS_ELF_2 = 888824;
   public static final int CLASS_RANK_BLESS_WIZARD_2 = 888825;
   public static final int CLASS_RANK_BLESS_DARKELF_2 = 888826;
   public static final int CLASS_RANK_BLESS_DRAGONKNIGHT_2 = 888827;
   public static final int CLASS_RANK_BLESS_BLACKWIZARD_2 = 888828;
   public static final int CLASS_RANK_BLESS_WARRIOR_2 = 888829;
   public static final int CLASS_RANK_BLESS_FENCER_2 = 888830;
   public static final int CLASS_RANK_BLESS_LANCER_2 = 888831;
   public static final int CLASS_RANK_BLESS_PRINCE_3 = 888832;
   public static final int CLASS_RANK_BLESS_KNIGHT_3 = 888833;
   public static final int CLASS_RANK_BLESS_ELF_3 = 888834;
   public static final int CLASS_RANK_BLESS_WIZARD_3 = 888835;
   public static final int CLASS_RANK_BLESS_DARKELF_3 = 888836;
   public static final int CLASS_RANK_BLESS_DRAGONKNIGHT_3 = 888837;
   public static final int CLASS_RANK_BLESS_BLACKWIZARD_3 = 888838;
   public static final int CLASS_RANK_BLESS_WARRIOR_3 = 888839;
   public static final int CLASS_RANK_BLESS_FENCER_3 = 888840;
   public static final int CLASS_RANK_BLESS_LANCER_3 = 888841;
   public static final int MAGIC_RAGE1 = 3087;
   public static final int MAGIC_RAGE2 = 3088;
   public static final int MAGIC_RAGE3 = 3089;
   public static final int MAGIC_RAGE4 = 3090;
   public static final int MAGIC_RAGE5 = 3091;

   public static void offEinhasadPrimiumFlat(L1PcInstance pc) {
     if (pc.hasSkillEffect(80000)) {
       return;
     }

     SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
     noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
     noti.set_spell_id(80000);
     noti.set_duration(0);
     noti.set_off_icon_id(9625);
     noti.set_end_str_id(0);
     noti.set_is_good(true);
     pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
     SC_REST_EXP_INFO_NOTI.send(pc);
   }

   public static void onEinhasadPrimiumFlat(L1PcInstance pc) {
     if (!pc.hasSkillEffect(80000)) {
       return;
     }
     int remainSeconds = pc.getSkillEffectTimeSec(80000);
     SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
     SC_REST_EXP_INFO_NOTI.send(pc);
     noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
     noti.set_spell_id(80000);
     noti.set_duration(remainSeconds);
     noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
     noti.set_icon_priority(0);
     noti.set_on_icon_id(9625);
     noti.set_off_icon_id(9625);
     noti.set_tooltip_str_id(3216);
     noti.set_new_str_id(3216);
     noti.set_end_str_id(0);
     noti.set_is_good(true);
     pc.send_effect(18530, false);
     pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
   }





   public static void offEinhasadGreatFlat(L1PcInstance pc) {
     if (pc.hasSkillEffect(80001)) {
       return;
     }

     SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
     noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
     noti.set_spell_id(4354);
     noti.set_duration(0);
     noti.set_off_icon_id(6985);
     noti.set_end_str_id(0);
     noti.set_is_good(true);
     pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
     SC_REST_EXP_INFO_NOTI.send(pc);
   }

   public static void onEinhasadGreatFlat(L1PcInstance pc) {
     if (!pc.hasSkillEffect(80001)) {
       return;
     }
     int remainSeconds = pc.getSkillEffectTimeSec(80001);
     SC_REST_EXP_INFO_NOTI.send(pc);
     SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
     noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.NEW);
     noti.set_spell_id(4354);
     noti.set_duration(remainSeconds);
     noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_EINHASAD_FAVOR);
     noti.set_icon_priority(1);
     noti.set_on_icon_id(9642);
     noti.set_off_icon_id(8704);
     noti.set_tooltip_str_id(6985);
     noti.set_new_str_id(0);
     noti.set_end_str_id(0);
     noti.set_is_good(true);
     noti.set_buff_group_priority(0);
     noti.set_buff_group_id(42);
     pc.send_effect(18530, false);
     pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
   }


   public static boolean is_tam_buff(int skill_id) {
     return (skill_id == 7791 || skill_id == 7792 || skill_id == 7793 || skill_id == 7794 || skill_id == 7795);
   }


   private static class TamNotifyInfo
   {
     int iconid;

     int strid;

     TamNotifyInfo(int iconid, int strid) {
       this.iconid = iconid;
       this.strid = strid;
     }
   }



   private static final HashMap<Integer, TamNotifyInfo> tamNotifier = new HashMap<>(); static {
     tamNotifier.put(Integer.valueOf(7791), new TamNotifyInfo(8265, 4181));
     tamNotifier.put(Integer.valueOf(7792), new TamNotifyInfo(8266, 4182));
     tamNotifier.put(Integer.valueOf(7793), new TamNotifyInfo(8267, 4183));
     tamNotifier.put(Integer.valueOf(7794), new TamNotifyInfo(8268, 5046));
     tamNotifier.put(Integer.valueOf(7795), new TamNotifyInfo(8269, 5047));
   }
   public static final int FREEZE_AFTER_DELAY = 2147483646;
   public static void recycleTam(L1PcInstance pc) {
     int tamcount = pc.tamcount();
     if (tamcount > 0) {
       long tamtime = pc.TamTime();
       int newSkillId = -1;
       if (tamcount == 1) {
         newSkillId = 7791;
       } else if (tamcount == 2) {
         newSkillId = 7792;
       } else if (tamcount == 3) {
         newSkillId = 7793;
       } else if (tamcount == 4) {
         newSkillId = 7794;
       } else if (tamcount == 5) {
         newSkillId = 7795;
       }
       if (tamNotifier.containsKey(Integer.valueOf(newSkillId))) {
         TamNotifyInfo nInfo = tamNotifier.get(Integer.valueOf(newSkillId));
         pc.sendPackets((ServerBasePacket)new S_NewCreateItem(110, (int)tamtime / 1000, nInfo.iconid, nInfo.strid));
         updateTam(pc, newSkillId, true);
         pc.setSkillEffect(newSkillId, (int)tamtime);
       }
     }
     if (!pc.is_apply_tam()) {
       pc.on_tam_ended();
     }
   }

   public static void updateTam(L1PcInstance pc, int skillId, boolean isOn) {
     int on = isOn ? 1 : -1;
     switch (skillId) {
       case 7791:
         pc.getAC().addAc(-1 * on);
         break;
       case 7792:
         pc.getAC().addAc(-2 * on);
         break;
       case 7793:
         pc.getAC().addAc(-3 * on);
         pc.addDamageReductionByArmor(2 * on);
         break;
       case 7794:
         pc.getAC().addAc(-4 * on);
         pc.addDamageReductionByArmor(2 * on);
         break;
       case 7795:
         pc.getAC().addAc(-5 * on);
         pc.addDamageReductionByArmor(2 * on);
         break;
     }
     pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
   }



   public static void onFreezeAfterDelay(L1PcInstance pc) {
     pc.setSkillEffect(2147483646, Config.Login.STUNSPEEDHACK);
   }


   public static void onPcCafeBuff(L1PcInstance pc, long remainMillis) {
     try {
       L1SkillUse.on_icons(pc, 80002, (int)(remainMillis / 1000L));


       SC_USER_START_SUNDRY_NOTI.send(pc, pc.isPcBuff());
       SC_GAMEGATE_PCCAFE_CHARGE_NOTI.send(pc, pc.isPcBuff());
       CS_PC_MASTER_INFO_REQ.newInstance();
       SC_PC_MASTER_FAVOR_UPDATE_NOTI.newInstance().send(pc);

       pc.addDamageReductionByArmor(2);
       pc.addWeightReduction(100);
       pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
     } catch (Exception e) {
       e.printStackTrace();
     }
     onPcCafeAttendanceData(pc);
   }

   public static void offPcCafeBuff(L1PcInstance pc) {
     try {
       // 移除指定ID的圖標（這裡是網吧Buff的ID 80002）
       L1SkillUse.off_icons(pc, 80002);

       // 發送通知給用戶，表示網吧Buff狀態的改變
       SC_USER_START_SUNDRY_NOTI.send(pc, pc.isPcBuff());
       SC_GAMEGATE_PCCAFE_CHARGE_NOTI.send(pc, pc.isPcBuff());

       // 向客戶端發送消息，通知玩家網吧Buff時間已經結束
       pc.sendPackets("龍之庇佑：網咖Buff時間已經結束。");
       pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("龍之庇佑：網咖Buff時間已經結束。", MJSimpleRgb.green(), 5));

       // 減少角色的護甲減傷值
       pc.addDamageReductionByArmor(-2);

       // 減少角色的負重減免
       pc.addWeightReduction(-100);

       // 更新角色狀態並發送給客戶端
       pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
     } catch (Exception e) {
       // 捕獲並打印異常
       e.printStackTrace();
     }

     // 處理網吧出勤相關數據
     onPcCafeAttendanceData(pc);
   }

   private static void onPcCafeAttendanceData(L1PcInstance pc) {
     SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
     if (userData != null) {
       AttendanceUserFlag flag = AttendanceUserFlag.USER_FLAG_PC_CAFE;
       if (!userData.get_userFlag().equals(flag)) {
         userData.set_userFlag(flag);
         SC_ATTENDANCE_USER_DATA_EXTEND.send(pc);
       }
     }
     SC_REST_EXP_INFO_NOTI.send(pc);
   }


   public static void onMoveEffectHandle(final L1PcInstance owner) {
     GeneralThreadPool.getInstance().schedule(new Runnable()
         {
           public void run() {
             L1SkillId.onSlowHandle(owner);
             L1SkillId.onHolyWalkHandler(owner);
           }
         },  1000L);
   }

   public static void onHolyWalkHandler(L1PcInstance owner) {
     int seconds = owner.getSkillEffectTimeSec(52);
     if (seconds > 0)
     {
       owner.broadcastPacket((ServerBasePacket)new S_SkillBrave(owner.getId(), 4, seconds));
     }
     seconds = owner.getSkillEffectTimeSec(155);
     if (seconds > 0)
     {
       owner.broadcastPacket((ServerBasePacket)new S_SkillBrave(owner.getId(), 1, seconds));
     }
     seconds = owner.getSkillEffectTimeSec(101);
     if (seconds > 0)
     {
       owner.broadcastPacket((ServerBasePacket)new S_SkillBrave(owner.getId(), 4, seconds));
     }
     seconds = owner.getSkillEffectTimeSec(177);
     if (seconds > 0)
     {
       owner.broadcastPacket((ServerBasePacket)new S_SkillBrave(owner.getId(), 10, seconds));
     }
     seconds = owner.getSkillEffectTimeSec(178);
     if (seconds > 0)
     {
       owner.broadcastPacket((ServerBasePacket)new S_SkillBrave(owner.getId(), 9, seconds));
     }
     seconds = owner.getSkillEffectTimeSec(179);
     if (seconds > 0)
     {
       owner.broadcastPacket((ServerBasePacket)new S_SkillBrave(owner.getId(), 1, seconds));
     }
     seconds = owner.getSkillEffectTimeSec(186);
     if (seconds > 0)
     {
       owner.broadcastPacket((ServerBasePacket)new S_SkillBrave(owner.getId(), 6, seconds));
     }
     seconds = owner.getSkillEffectTimeSec(20079);
     if (seconds > 0)
     {
       owner.broadcastPacket((ServerBasePacket)new S_SkillBrave(owner.getId(), owner.getBraveSpeed(), seconds));
     }
     seconds = owner.getSkillEffectTimeSec(22017);
     if (seconds > 0) {
       S_Liquor packet = new S_Liquor(owner.getId(), 8);
       owner.sendPackets((ServerBasePacket)packet, false);
       owner.broadcastPacket((ServerBasePacket)packet);

       owner.sendPackets((ServerBasePacket)new S_ServerMessage(1065, seconds));
     }
     if (owner.isFourgear()) {
       owner.sendPackets(SC_FOURTH_GEAR_NOTI.Fourth_Gear(owner));
       owner.broadcastPacket(SC_FOURTH_GEAR_NOTI.Fourth_Gear(owner));
     }
   }

   public static void onSlowHandle(L1PcInstance owner) {
     int remainSeconds = owner.getRemainSlowSeconds();
     if (remainSeconds > 0) {
       owner.sendPackets((ServerBasePacket)new S_SkillHaste(owner.getId(), 2, remainSeconds));
       owner.broadcastPacket((ServerBasePacket)new S_SkillHaste(owner.getId(), 2, 0));
     }
   }

   public static void DRAGON_PEARL_BUFF(L1PcInstance pc) {
     if (pc.hasSkillEffect(22017)) {
       int reminingtime = pc.getSkillEffectTimeSec(22017);
       SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
       noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
       noti.set_spell_id(22017);
       noti.set_duration(reminingtime);
       noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
       noti.set_on_icon_id(5393);
       noti.set_off_icon_id(5394);
       noti.set_icon_priority(10);
       noti.set_tooltip_str_id(1065);
       noti.set_end_str_id(0);
       noti.set_is_good(true);
       ProtoOutputStream stream = ProtoOutputStream.newInstance((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
       pc.sendPackets(stream, false);
       pc.sendPackets((ServerBasePacket)new S_Liquor(pc.getId(), 8));
       pc.setPearl(1);
     }
   }
 }


