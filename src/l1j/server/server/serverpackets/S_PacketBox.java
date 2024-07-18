package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.server.Account;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.utils.SQLUtil;

/**
 * 用於技能圖標或阻止列表顯示等多種用途的數據包類
 */
public class S_PacketBox extends ServerBasePacket {

	private static final String S_PACKETBOX = "[S] S_PacketBox";

// *** S_107 子代碼列表 ***

// 1:肯特 2:獸人 3:WW 4:吉蘭 5:海恩 6:矮人 7:亞丁 8:迪亞德 9:城名 9 ...
	/** C(id) H(?): %s的攻城戰開始了。 */
	public static final int MSG_WAR_BEGIN = 0;

	/** C(id) H(?): %s的攻城戰已結束。 */
	public static final int MSG_WAR_END = 1;

	/** C(id) H(?): %s的攻城戰正在進行中。 */
	public static final int MSG_WAR_GOING = 2;

	/** -: 佔據了城的主導權。（音樂變更） */
	public static final int MSG_WAR_INITIATIVE = 3;

	/** -: 佔領了城。 */
	public static final int MSG_WAR_OCCUPY = 4;

	/** ?: 決鬥結束了。（音樂變更） */
	public static final int MSG_WAR_DUEL_END = 5; // 註解補全，假設此行代表決鬥結束

	public static final int MSG_DUEL = 5;

	/** C(count): SMS的發送失敗了。 / 總共發送了%d條。 */
	public static final int MSG_SMS_SENT = 6;

	/** -: 祝福啊，兩人結為夫妻了。（音樂變更） */
	public static final int MSG_MARRIED = 9;

	/** C(weight): 重量（30階段） */
	public static final int WEIGHT = 10;

	/** C(food): 飽食度（30階段） */
	public static final int FOOD = 11;

	/** C(0) C(level): 這個物品只能在%d級以下使用。（除了0~49之外不會顯示） */
	public static final int MSG_LEVEL_OVER = 12;

	/** UB信息 HTML */
	public static final int HTML_UB = 14;

	/**
	 * C(id)<br>
	 * 1:感覺到寄宿在身體中的精靈的力量逐漸消散在空氣中。<br>
	 * 2:火精靈的力量滲透到身體的每一個角落。<br>
	 * 3:水精靈的力量滲透到身體的每一個角落。<br>
	 * 4:風精靈的力量滲透到身體的每一個角落。<br>
	 * 5:地精靈的力量滲透到身體的每一個角落。<br>
	 */
	public static final int MSG_ELF = 15;

	/** C(count) S(name)...: 阻止列表 */
	public static final int SHOW_LIST_EXCLUDE = 17;

	/** S(name): 添加到阻止列表 */
	public static final int ADD_EXCLUDE = 18;

	/** S(name): 解除阻止 */
	public static final int REMOVE_EXCLUDE = 19; // 假設這行代表解除阻止，補全註解
	public static final int REM_EXCLUDE = 19;

	/** 網咖Buff */
	public static final int PC방버프 = 127;

	/** 技能圖標 */
	public static final int ICONS1 = 20;

	/** 技能圖標 */
	public static final int ICONS2 = 21;

	/** 移除光環技能圖標及消除魔法圖標 */
	public static final int ICON_AURA = 22;

	/** S(name): %s被選為城鎮領袖。 */
	public static final int MSG_TOWN_LEADER = 23;

	/**
	 * D(血盟成員數) (S(成員名稱) C(成員階級)) 當血盟成員更新時的/血盟命令。
	 */
	public static final int PLEDGE_TWO = 24; // 新增

	/**
	 * D(成員名稱) C(階級) 當有成員加入血盟時發送的數據包。
	 */
	public static final int PLEDGE_REFRESH_PLUS = 25; // 新增

	/**
	 * D(成員名稱) C(階級) 當有成員離開血盟時發送的數據包。
	 */
	public static final int PLEDGE_REFRESH_MINUS = 26; // 新增

	/**
	 * C(id): 您的階級已更改為%s。<br>
	 * id - 1:見習 2:一般 3:守護者
	 */
	public static final int MSG_RANK_CHANGED = 27;

/**
 * D(血盟成員數) (S(成員名稱) C(成員階級)) 當血盟成員未更新時的/血盟命令。
 */
// public static final int PLEDGE_ONE = 119; // 新增

	/** D(?) S(name) S(clanname): %s血盟的%s清除了拉斯塔巴德軍隊。 */
	public static final int MSG_WIN_LASTAVARD = 30;

	/** -: 1心情變好了。 */
	public static final int MSG_FEEL_GOOD = 31;

	/** 未知.C_30 發送的數據包 */
	public static final int SOMETHING1 = 33;

	/** H(time): 顯示藍色藥水圖標。 */
	public static final int ICON_BLUEPOTION = 34;

	/** H(time): 顯示變身圖標。 */
	public static final int ICON_POLYMORPH = 35;

	/** H(time): 顯示禁言圖標。 */
	public static final int ICON_CHATBAN = 36;

	/** 未知.C_7 發送的數據包。C_7也在打開寵物菜單時發送。 */
	public static final int SOMETHING2 = 37;

	/** 顯示血盟信息的HTML */
	public static final int HTML_CLAN1 = 38;

	/** H(time): 顯示I2H圖標 */
	public static final int ICON_I2H = 40;

	/** 發送角色的遊戲選項、快捷鍵信息等 */
	public static final int CHARACTER_CONFIG = 41;

	/** 返回角色選擇畫面 */
	public static final int LOGOUT = 42;

	/** 戰鬥中無法重新登錄。 */
	public static final int MSG_CANT_LOGOUT = 43;

	/**
	 * C(count) D(time) S(name) S(info):<br>
	 * 顯示帶有 [CALL] 按鈕的窗口。這似乎是用於檢查BOT等非法用戶的功能。雙擊名稱時
	 * 發送 C_RequestWho，並在客戶端的文件夾中創建 bot_list.txt。選擇名稱並按 + 鍵時
	 * 將打開一個新窗口。
	 */
	public static final int CALL_SOMETHING = 45;

	/**
	 * C(id): 戰鬥競技場，混沌大戰―<br>
	 * id - 1:開始 2:已刪除 3:結束
	 */
	public static final int MSG_COLOSSEUM = 49;

	// 血盟信息的 HTML
	public static final int HTML_CLAN2 = 51;

	// 打開烹飪窗口
	public static final int COOK_WINDOW = 52;

	/** C(type) H(time): 顯示烹飪圖標 */
	public static final int ICON_COOKING = 53;

	/** 顯示魚餌抖動 */
	public static final int FISHING = 55;

	/** 刪除圖標 */
	public static final int DEL_ICON = 59;

	/** 龍之珍珠（三級加速） */
	public static final int DRAGON_PEARL = 60;

	/** 聯盟名單 */
	public static final int ALLIANCE_LIST = 62;

	/** 小遊戲：5,4,3,2,1 倒計時 */
	public static final int MINIGAME_START_COUNT = 64;

	/** 小遊戲：時間（0:00 開始） */
	public static final int MINIGAME_TIME2 = 65;

	/** 小遊戲：遊戲者名單 */
	public static final int MINIGAME_LIST = 66;

	/** 小遊戲：稍後將移動到村莊（10秒倒數） */
	public static final int MINIGAME_10SECOND_COUNT = 69;

	/** 小遊戲：結束 */
	public static final int MINIGAME_END = 70; // 假設這行代表小遊戲結束，補全註解
	public static final int MINIGAME_END = 70;

	/** 小遊戲：時間 */
	public static final int MINIGAME_TIME = 71;

	/** 小遊戲：清除時間 */
	public static final int MINIGAME_TIME_CLEAR = 72;

	/** 龍騎士：弱點暴露 */
	public static final int SPOT = 75;

	public static final int aaaa1 = 78; // 攻城戰已開始。
	public static final int bbbb2 = 79; // 攻城戰已結束。
	public static final int cccc3 = 80; // 攻城戰進行中。

	/** 恩賜之力 Buff */
	public static final int EINHASAD = 82;

	/** 1:粉紅色邊框，2:抖動，3:煙花 **/
	public static final int HADIN_DISPLAY = 83;

	/** 副本綠色信息 **/
	public static final int GREEN_MESSAGE = 84;

	/** 副本黃色信息 **/
	public static final int YELLOW_MESSAGE = 61; // 副本第2章等待

	/** 副本紅色信息 **/
	public static final int RED_MESSAGE = 51; // 紅色信息

	/** 副本計分板 **/
	public static final int SCORE_MARK = 4;

	/** 綠寶石 Buff **/
	public static final int EMERALD_ICON = 86;

	/**
	 * 友好度UI顯示 + 欲望洞窟 - 影子神殿
	 */
	public static final int KARMA = 87; // 新增

	/** 狀態閃避顯示 */
	public static final int INIT_DODGE = 88; // 新增

	/** 龍之血跡 (安塔瑞斯:82，法普那:85) */
	public static final int DRAGONBLOOD = 100;

	public static final int DODGE = 101;

	public static final int DragonMenu = 102;

	/** 位置傳送 **/
	public static final int MINI_MAP_SEND = 111;

	/** 血盟倉庫列表 */
	public static final int CLAN_WAREHOUSE_LIST = 117;

	/** 巴風特伺服器數據包 */
	public static final int BAPO = 114;

	public static final int ICON_SECURITY_SERVICES = 125; // 安全Buff

	/** 網咖Buff圖標 */
	public static final int ICON_PC_BUFF = 127;

	public static final int ER_UpDate = 132;

	public static final int BOOKMARK_SIZE_PLUS_10 = 141; // 書籤擴展
	public static final int BOOKMARK = 142;

	/** 顯示圖標 **/
	public static final int UNLIMITED_ICON = 147;

	public static final int UNLIMITED_ICON1 = 180; // 無限制數據包
	public static final int NONE_TIME_ICON = 180;

	/** 實時封印狀態 */
	public static final int ITEM_STATUS = 149;

	public static final int MAP_TIMER = 153;

	/** 將蝴蝶卡的 castgfx 值的 Buff 圖示顯示在 Buff 窗口中 **/
	public static final int BUFFICON = 154;

	public static final int ROUND = 156;

	public static final int ROUND1 = 156;

	public static final int DungeonTime = 159; // 地下城數據包

	/** 顯示毒相關圖標 UI6 **/
	public static final int POSION_ICON = 161;

	/** 血盟Buff圖標 */
	public static final int CLAN_BUFF_ICON = 165;

	/** UI6 3.80 血盟相關 **/
	public static final int HTML_PLEDGE_ANNOUNCE = 167;

	public static final int HTML_PLEDGE_REALEASE_ANNOUNCE = 168;

	public static final int HTML_PLEDGE_WRITE_NOTES = 169;

	public static final int HTML_PLEDGE_MEMBERS = 170;

	public static final int HTML_PLEDGE_ONLINE_MEMBERS = 171;

	public static final int ITEM_ENCHANT_UPDATE = 172;

	public static final int PLEDGE_EMBLEM_STATUS = 173; // 章部狀態

	public static final int TOWN_TELEPORT = 176;

	public static final int 可攻擊距離 = 160; // 可攻擊距離
	public static final int 王族的傷害Buff2 = 184; // 主君的傷害Buff
	public static final int 王族的傷害Buff3 = 188; // 主君的傷害Buff
	public static final int 保存背包 = 189; // 保存背包
	public static final int 戰鬥鏡頭 = 181; // 戰鬥鏡頭
	public static final int 開店次數 = 198; // 開店次數
	public static final int 用戶背刺 = 193; // 用戶背刺

	public static final int EFFECT_DURATOR = 194;

	public static final int ICON_COMBO_BUFF = 204;
	public static final int DRAGONRAID_BUFF = 179;
	public static final int a = 103;
	public static final int LOGIN_UNKNOWN3 = 0x20;
	
	public S_PacketBox(int subCode) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {
		case aaaa1:
		case bbbb2:
			writeC(7);
			writeS("");
			writeS("");
			writeS("");
			writeS("");
			writeS("");
			writeS("");
			writeS("");
			writeH(0);
			break;
		case 王族的傷害Buff3:
			writeD(0);
			writeD(0);
			break;
		case 保存背包:
			writeD(0x00);//原來的
			break;
		case 王族的傷害Buff2:
			writeH(0);
			break;
		case MSG_WAR_INITIATIVE:
		case MSG_WAR_OCCUPY:
		case MSG_MARRIED:
		case MSG_FEEL_GOOD:
		case MSG_CANT_LOGOUT:
		case LOGOUT:
			break;
		case ICON_SECURITY_SERVICES:
			break;
		case FISHING:
		case MINIGAME_TIME2:
			break;
		case CALL_SOMETHING:
			callSomething();
			break;
		case MINIGAME_10SECOND_COUNT:
			writeC(10);
			writeC(109);
			writeC(85);
			writeC(208);
			writeC(2);
			writeC(220);
			break;
		case DEL_ICON:
			writeH(0);
			break;
		case MINIGAME_END:
			writeC(147);
			writeC(92);
			writeC(151);
			writeC(220);
			writeC(42);
			writeC(74);
			break;
		case MINIGAME_START_COUNT:
			writeC(5);
			writeC(129);
			writeC(252);
			writeC(125);
			writeC(110);
			writeC(17);
			break;
		case ICON_AURA:
			writeC(0x98);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			break;
		default:
			break;
		}
	}

	/** 升級 Buff **/
	public S_PacketBox(int time, boolean ck, boolean ck2) {
		writeC(Opcodes.S_EVENT);
		writeC(0x56);
		writeC(0xAA);
		writeC(0x01);
		writeH(time / 16);
		writeH(0x00);
	}

// TODO 手動添加2格攻擊圖像
//    private static final int spearGfxIds[] = {
//        1002, 6830, 6831, 7143, 7144, 7147, 7148, 7151, 7152, 7155, 7156, 7159, 7160, 7163, 7164, // 龍騎士 Sharna
//        11330, 11351, 11344, 11368, 12240, 11447, 14928, 13389, 12237, 11419, 11418, 14923, 13715, 13717, 13719, 15115,
//        13721, 13723, 13725, 13727, 13729, 13731, 13733, 13735, 13737, 13739, 13741, 13743, 13745, // 排行榜變身
//        13380, 13381, 14487, // 網咖變身，橙色
//        11121, 11125, // 邂逅變身
//        16008, 16056, // 新增變身
//        15115, // 男騎士長槍
//        // TODO 變身卷軸
//        15539, 15537, 15534, 11376, 15599, 15834, 15832, 15833, 15531,
//        // TODO 真·死亡騎士變身戒指
//        17275, 17272,
//        11685, 12015,
//        17545, 17549,
//        19026
//    };


	public S_PacketBox(int subCode, L1PcInstance pc, L1ItemInstance weapon) { //108 160
		writeC(Opcodes.S_EVENT);
		writeC(공격가능거리); // 可攻擊距離
		// 1:劍, 2:匕首, 3:雙手劍, 4:弓, 5:矛, 6:鈍器, 7:法杖, 8:飛刀,
		// 9:箭, 10:手套, 11:爪, 12:雙刀, 13:單手弓, 14:單手矛, 15:雙手鈍器, 16:雙手法杖, 17:鑰匙圈, 18:鏈鋸劍
		int range = 1;
		int equipType = 0; // 劍 1 / 斧頭 2 / 弓 3 / 雙刀 4 / 矛 5 / 法杖 6 / 匕首 7 / 爪, 鑰匙圈 8 / 鏈鋸劍 10 / 第9類是什麼?
		int isTohand = 0;
		int charGfxId = pc.getCurrentSpriteId();
		int charBaseGfxId = pc.getClassId();
		if (weapon != null && weapon.isEquipped()) {
			isTohand = weapon.getItem().isTwohandedWeapon() ? 1 : 0; // 是否為雙手武器。是雙手則為1，否則為0
			int weaponType = weapon.getItem().getType();
			switch (weaponType) {
				case 1 : case 3 : equipType = 1; break; // 單手, 雙手劍
				case 6 : case 15 : equipType = 2; break; // 單手斧, 雙手斧
				case 4 : case 13 : case 10 :
					equipType = 3;
					range = 24;
					if (weaponType == 4)
						range = 24; // 雙手弓時
					if (pc.hasSkillEffect(L1SkillId.BURNING_SHOT)) {
						range = 10;
					}
					break; // 雙手弓, 單手弓, 手套
				case 12 : equipType = 4; break; // 雙刀
				case 5 : case 14 : case 18 :
					equipType = 5;
					range = 2; // 雙手矛, 單手矛, 鏈鋸劍
					if (weaponType == 18) equipType = 10;
					boolean gfxCk = false;
					L1PolyMorph poly = PolyTable.getInstance().getTemplate(charGfxId);
					if(poly != null && poly.isSpearGfx()) {
						gfxCk = true;
					}
					// for (int gfxId : spearGfxIds) {
					//     if (gfxId == charGfxId) {
					//         gfxCk = true;
					//         break;
					//     }
					// }
			}
		}
	}
		if (!gfxCk && charBaseGfxId != charGfxId)
			range = 1;

		break;

		case 7:
			case 16:
				equipType = 6;
				break; // 單手法杖, 雙手法杖
	case 2:
		equipType = 7;
		break; // 匕首
	case 11:
		equipType = 8; // 爪
	break;
	case 17:
		equipType = 8; // 爪
	range = 4;
	break; // 爪, 鑰匙圈
	default: // 其他沒有武器類型
			break;
}
}





	if (pc.isSpearModeType()) {
		if (pc != null && pc.isPassive(MJPassiveID.INCREASE_RANGE.toInt())) {
			range = 8;
		} else {
			range = 4;
		}
		}


		pc.setAttackRang(range);
		writeC(range); // 距離
		writeC(equipType); // 武器類型
		writeC(isTohand); // 雙手武器

		/*if (pc.isGm()) {
    pc.sendPackets(new S_SystemMessage("\aA攻擊可能距離 : " + range + " / 變身代碼 : " + charGfxId));
}*/
	}



	public S_PacketBox(int subCode, int range, int type, boolean bow, L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
			case 可攻擊距離: // 	공격가능거리
		writeC(range);
		writeC(type);
		if (bow) {
			writeC(1);
		} else {
			writeC(0);
		}
		pc.setAutoRange(range);
		break;
		}
	}

public S_PacketBox(int subCode, int time1, int time2, int time3, int time4) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case DungeonTime: // 12月14日變更
		writeD(7);
		writeD(1);
		writeS("$12125"); // 象牙塔
		writeD(time1);
		writeD(2);
		writeS("$6081"); // 象牙塔
		writeD(time2);
		writeD(15);
		writeS("$13527"); // 網咖巴洛克陣營
		writeD(time3);
		writeD(500);
		writeS("$19375"); // 網咖政務
		writeD(time4);
		writeD(49200);
		break;
default:
		break;
		}
		}



public S_PacketBox(int subCode, L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case TOWN_TELEPORT:
		writeC(0x01);
		writeH(pc.getX());
		writeH(pc.getY());
		break;
		case 用戶背刺: // 	유저빽스탭
		writeH(pc.getX());
		writeH(pc.getY());
		break;
		}
		}

public S_PacketBox(int subCode, int value) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case LOGIN_UNKNOWN3:
		writeC(0x10);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0x00000000);
		break;
		case DRAGONRAID_BUFF:
		writeC(0x01);
		writeC(0x27);
		writeC(0x0E);
		writeD(value);
		writeH(0x63EF);
		break;
		case 204: // 組合系統
		writeC(value);
		writeH(0); // ?
		break;
		case 網咖Buff: // 	PC방버프
		if (value == 1) {
		writeC(0x18);
		} else {
		writeC(0);
		}
		break;
		case 開店次數: // 	상점개설횟수
		writeD(value);
		writeD(0x28);
		writeD(0x00);
		break;
		}
		case ICON_BLUEPOTION:
		case ICON_CHATBAN:
		case ICON_I2H:
		case MINIGAME_TIME:
		case INIT_DODGE:
			writeH(value); // time
			break;
		case MAP_TIMER:// 地圖計時器地下城
			writeD(value);
			break;
		case 戰鬥鏡頭: // 	배틀샷
		writeD(value);
		break;
		case MSG_WAR_BEGIN:
		case MSG_WAR_END:
		case MSG_WAR_GOING:
		writeC(value); // 城堡ID
		writeH(0); // ?
		break;
		case MSG_SMS_SENT:
		case WEIGHT: // 重量
		case FOOD: // 食物
		case DODGE: // 閃避
		writeC(value);
		break;
		case MSG_ELF:
		case MSG_COLOSSEUM:
		case SPOT:
		case ER_UpDate:
		writeC(value); // 訊息ID
		break;
		case MSG_LEVEL_OVER: // 等級超過
		writeC(0); // ?
		writeC(value); // 0-49以外不顯示
		break;
		case COOK_WINDOW: // 烹飪視窗
		writeC(0xdb); // ?
		writeC(0x31);
		writeC(0xdf);
		writeC(0x02);
		writeC(0x01);
		writeC(value); // 等級
		break;
		case MINIGAME_LIST: // 小遊戲列表
		writeH(0x00); // 參與者數量
		writeH(0x00); // 排名
		break;
		case EINHASAD: // 伊納哈德
		value /= 10000;
		writeD(value); // %數值 1~200
		writeC(0);
		break;
		}
			
			// 1 = 2
			// 2 = 5
			// 3 = 7
			// 4 = 10
			// 5 = 12
			// 8 = 20
			// 9 = 23
			// 16 = 40	: pc網咖Buff
			// 32 = 81
			writeD(16);	// 祝福
			writeC(32);
			writeC(0);
			writeC(0);
			
			writeD(1);	// 效率
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			
			break;
		case HADIN_DISPLAY:
			writeC(value);
			break;
		case BOOKMARK_SIZE_PLUS_10:
			writeC(value);
			break;
		case PLEDGE_EMBLEM_STATUS:
			writeC(1);
			if (value == 0) { // 0：關 1：開
				writeC(0);
			} else if (value == 1) {
				writeC(1);
			}
			writeD(0x00);
			break;
		case ROUND1:
			writeD(value);
			writeD(12);
			break;
		default:
			break;
		}
	}



	public S_PacketBox(int subCode, int type, int time, boolean second, boolean temp) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
			case BUFFICON:
				writeH(time);
				writeH(type);
				writeH(0x00);
				writeH(second ? 0x01 : 0x00); // 刪除 添加
		break;
		}
		// b0 04 80 08 00 00 00 00
	}



	public S_PacketBox(int subCode, int time, int gfxid, int type) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
			case SOMETHING2:
				writeC(time);
				writeD(gfxid); // 寵物 objid
		writeH(type);
		break;
		case BUFFICON:
			writeH(time); // 時間
		writeD(gfxid); // 圖標
		writeC(type); // 類型
		writeC(0x00);
		break;
		case EMERALD_ICON:
			writeC(time);
			writeC(1); // 類型
		switch (time) {
			// 類型 1 - 20% / 2 - 25% / 3 - 30% / 4 - 35% / 5 - 40%
		case 62: // 成長的藥水
		writeH(gfxid);
		writeC(20);
		writeC(type);
		break;
		case 112:
			case 129:
				writeC(gfxid);
				writeH(type);
				break;
				default:
					writeD(gfxid);
		}
		break;
		}
	}

	public S_PacketBox(int subCode, int type, int time) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {
		case ICON_POLYMORPH:
			writeH(type); // time
			writeC(time); // time
			break;
		case ICON_COOKING: // 53
			if (type != 7) {
				writeC(0x2e);
				writeC(0x00);
				writeC(0x0b);
				writeC(0x00);
				writeC(0x0a);
				writeC(0x00);
				writeC(0x10);
				writeC(0x00);
				writeC(0x10);
				writeC(0x00);
				writeC(0x08);
				writeC(0x00);
				writeC(0xd0);
				writeC(0x07);
				writeC(type);
				writeC(0x24);
				writeH(time);
				writeC(0x15);
			} else {
				// 第七名：蘑菇湯，不過我不用
				writeC(0x0c);
				writeC(0x0c);
				writeC(0x0c);
				writeC(0x12);
				writeC(0x0c);
				writeC(0x09);
				writeC(0xc8);
				writeC(0x00);
				writeC(type);
				writeC(0x26);
				writeH(time);
				writeC(0x3e);
				writeC(0x87);
			}
			break;
		case ICON_AURA:
			writeC(0xdd);
			writeH(time);
			writeC(type);
			break;
		case MSG_DUEL:
			writeD(type);
			writeD(time);
			break;
		case BUFFICON:
			writeH(time);
			writeH(type);
			writeH(0);
			break;
		case DRAGONBLOOD:
			writeC(type);
			writeD(time);
			break;
		case ROUND:
		writeD(type); // 當前回合顯示
		writeD(time); // 總回合顯示
		break;
		case DRAGON_PEARL:
		writeC(time);
		writeC(type);
		// writeC((int)((time + 2) / 4));
		// writeC(type);
		break;
		case EMERALD_ICON: // 翡翠圖標
		writeC(0x70);
		writeC(0x01);
		writeC(type);
		writeH(time); // 時間 (秒)
		break;
		case NONE_TIME_ICON:
		writeC(type); // 開/關
		writeD(time); // 166 經驗值30%, 228 清爽冰塊, 286 經驗值40%, 343 吉爾塔斯地區死亡懲罰
						// 409 裝甲破壞, 497 紅色騎士的證書 活動攻城區 // 477~479
		writeD(0x00000D67);
		writeH(0x00);
		break;
default:
		break;
		}
		}

/** 紫水晶, 黃玉 **/
public S_PacketBox(int time, int val, boolean ck, boolean ck2) {
		writeC(Opcodes.S_EVENT);
		writeC(EMERALD_ICON);
		writeC(0x81);
		writeC(0x01);
		writeC(val);
		writeH(time);
		}// 7e 56 81 01 02 08 07

	public S_PacketBox(int i, int time, boolean ck, boolean ck2, boolean ck3) {
		writeC(Opcodes.S_EVENT);
		writeC(EMERALD_ICON);
		writeC(0x3e);
		writeC(i);
		writeH(time);
		writeC(0x14);
		writeC(0x86);
	}// 0f 56 3e 01 08 07 14 86

	public S_PacketBox(int subCode, String name) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {
		case HTML_PLEDGE_REALEASE_ANNOUNCE:
			writeByte(name.getBytes(MJEncoding.EUCKR));
			writeH(0x00);
			break;
		case MSG_TOWN_LEADER:
			writeS(name);
			break;
		case GREEN_MESSAGE:
			writeC(2);
			writeS(name);
			break;
		default:
			break;
		}
	}



	public S_PacketBox(int subCode, int id, String name, String clanName) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {
			case MSG_WIN_LASTAVARD:
				writeD(id); // 克蘭ID或其他？
		writeS(name);
		writeS(clanName);
		break;
		default:
			break;
		}
	}



	public S_PacketBox(int subCode, L1ItemInstance item, int type) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
			case ITEM_STATUS:
				writeD(item.getId());
				writeH(type);
				break;
		}
	}



	public S_PacketBox(int subCode1, int subCode2, String name) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode1);
		switch (subCode2) {
			case RED_MESSAGE: // 紅色信息
		case YELLOW_MESSAGE: // 黃色信息
		writeC(2);
		writeH(26204);
		writeC(subCode2);
		writeS(name);
		break;
		case SCORE_MARK: // 評分標記
		writeC(subCode2);
		writeS(name);
		break;
		default: // ?
		switch (subCode1) {
			case MSG_RANK_CHANGED: // 排名變更
		writeC(subCode2);
		writeS(name);
		break;
		case ADD_EXCLUDE: // 添加排除
		case REM_EXCLUDE: // 移除排除
		writeS(name);
		writeC(subCode2);
		break;
		}
		break;
		}
	}

	/*
	 * public S_PacketBox(int subCode, String name, int type) {
	 * writeC(Opcodes.S_EVENT); writeC(subCode); switch (subCode) { case
	 * ADD_EXCLUDE: case REM_EXCLUDE: writeS(name); writeC(type); break;
	 * default: break; } }
	 */
	
	public S_PacketBox(int subCode, int range, int type, boolean bow) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case EMERALD_ICON:
			writeC(62);
			if (type == 0 || type == 2) {
				writeC(bow ? 1 : 2);
			} else {
				writeC(bow ? 1 : 3);
			}
			writeH(range);
			writeC(0);
			writeC(0);
			
			if (type == 0) {
				writeC(0x86); // 0x49（龍成長藥水已停止）
			} else {
				writeC(0x49);
			}
			break;
		case EFFECT_DURATOR:
			writeD(range);
			writeD(type);
			writeD(bow ? 0x01 : 0x00);
			writeH(0);
			break;
		}


		
		
	}

	public S_PacketBox(int subCode, Object[] names) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {
		case HTML_PLEDGE_ONLINE_MEMBERS:
			writeH(names.length);
			for (Object name : names) {
				if (name == null)
					continue;
				L1PcInstance pc = (L1PcInstance) name;
				writeS(pc.getName());
				writeC(0);
			}
			break;
		default:
			break;
		}
	}


	public S_PacketBox(int subCode, String[] names, int type) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		writeC(0);
		switch (subCode) {
		case SHOW_LIST_EXCLUDE:
			writeC(type);
			writeC(names.length);
			for (String name : names) {
				writeS(name);
			}
			writeH(0);
			break;
		}
	}

	public S_PacketBox(int subCode, L1ItemInstance item) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
			case DragonMenu:
				writeD(item.getId());
				writeC(item.getItemId() == 490012 ? 0x01 : 0x00); // 安塔拉斯 0
		writeC(item.getItemId() == 490013 ? 0x01 : 0x00); // 帕普 1
		writeC(item.getItemId() == 490014 ? 0x01 : 0x00); // 林德 2
		writeC(0);
		break;
		case ITEM_ENCHANT_UPDATE: // 1102 更新後增加的製作封包
		writeD(item.getId());
		writeC(24);
		writeC(0);
		writeH(0);
		writeH(0);
		writeC(item.getEnchantLevel());
		writeD(item.getId());
		writeD(0);
		writeD(0);
		writeD(item.getBless() >= 128 ? 3 : item.getItem().isTradable() ? 7 : 2);
		writeC(0);
		writeC(item.getAttrEnchantBit(item.getAttrEnchantLevel())); // 製作1102新增
		writeH(0);
		break;
		default:
			break;
		}
		}



		public S_PacketBox(int subCode, L1PcInstance pc, int value1, int value2) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
			case POSION_ICON:
				writeC(value1); // 類型 : 1.中毒 6:沉默
		writeD(value2); // 時間 (秒)
		break;
		default:
			break;
		}
		}

	public S_PacketBox(L1PcInstance pc, int subCode) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {

		case CLAN_WAREHOUSE_LIST:
			int count = 0;
			Connection con = null;
			PreparedStatement pstm = null;
			PreparedStatement pstm2 = null;
			PreparedStatement pstm3 = null;
			ResultSet rs = null;
			ResultSet rs3 = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT id, time FROM clan_warehouse_log WHERE clan_name='" + pc.getClanname() + "'");
				rs = pstm.executeQuery();
				while (rs.next()) {
					if (System.currentTimeMillis() - rs.getTimestamp(2).getTime() > 4320000) {// 3天
						pstm2 = con.prepareStatement("DELETE FROM clan_warehouse_log WHERE id='" + rs.getInt(1) + "'");
						pstm2.execute();
					} else
						count++;
				}
				writeD(count);
				pstm3 = con
						.prepareStatement("SELECT name, item_name, item_count, type, time FROM clan_warehouse_log WHERE clan_name='" + pc.getClanname() + "'");
				rs3 = pstm3.executeQuery();
				while (rs3.next()) {
					writeS(rs3.getString(1));
					writeC(rs3.getInt(4));// 0：委託 1：找到
					writeS(rs3.getString(2));
					writeD(rs3.getInt(3));
					writeD((System.currentTimeMillis() - rs3.getTimestamp(5).getTime()) / 60000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs3);
				SQLUtil.close(rs);
				SQLUtil.close(pstm3);
				SQLUtil.close(pstm2);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
			break;

		// case PLEDGE_REFRESH_PLUS:
		case PLEDGE_REFRESH_MINUS:
			writeS(pc.getName());
			writeC(pc.getClanRank());
			writeH(0);
			break;
		case KARMA:
			writeD(pc.getKarma());
			break;
		// case ALLIANCE_LIST:
		// StringBuffer sb = new StringBuffer();
		// for (int i : pc.getClan().Alliance()) {
		// if (i == 0)
		// continue;
		// L1Clan c = L1World.getInstance().getClan(i);
		// if (c == null)
		// continue;
		// sb.append(c.getClanName() + " ");
		// }
		// writeS(sb.toString());
		// break;
		// case PLEDGE_ONE:
		// writeD(clan.getOnlineMemberCount());
		// for (L1PcInstance targetPc : clan.getOnlineClanMember()) {
		// writeS(targetPc.getName());
		// writeC(targetPc.getClanRank());
		// }
		// writeD((int) (System.currentTimeMillis() / 1000L));
		// writeS(clan.getLeaderName());
		// break;
		// case PLEDGE_TWO:
		// writeD(clan.getClanMemberList().size());
		//
		// ClanMember member;
		// FastTable<ClanMember> clanMemberList = clan.getClanMemberList();
		// // 所有氏族成員的姓名和等級
		// for (int i = 0; i < clanMemberList.size(); i++) {
		// member = clanMemberList.get(i);
		// writeS(member.name);
		// writeC(member.rank);
		// }
		//
		// writeD(clan.getOnlineMemberCount());
		// for (L1PcInstance targetPc : clan.getOnlineClanMember()) { // 在線的
		// writeS(targetPc.getName());
		// }
		// break;
		default:
			break;
		}
	}



	public S_PacketBox(int subCode, String name, int mapid, int x, int y, int Mid) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case MINI_MAP_SEND:
			writeS(name);
			writeH(mapid);
			writeH(x);
			writeH(y);
			writeD(Mid);
			break;
		default:
			break;
		}
	}



	public S_PacketBox(int subCode, int value, boolean show) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
			case BAPO: // 旗幟
		writeD(value); // 1~7 旗幟
		writeD(show ? 0x01 : 0x00); // 開 關
		break;
		case UNLIMITED_ICON: // 無限圖標封包
		writeC(show ? 0x01 : 0x00); // 開 關 // true false
		writeC(value);
		break;
		case UNLIMITED_ICON1:
			writeC(show ? 0x01 : 0x00); // 開 關 // true false
		writeD(value);
		writeD(0);
		writeH(0);
		break;
		default:
			break;
		}
		}



	public S_PacketBox(int subCode, boolean show) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case CLAN_BUFF_ICON:
			writeC(show ? 0x01 : 0x00);
			break;
		}
	}
	
	public S_PacketBox(int subCode, int itemid, String name, List<L1BookMark> list) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case BOOKMARK: // 142
			writeC(0);
			writeD(itemid);
			writeS(name);
			writeC(list.size());
			for (L1BookMark book : list) {
				writeS(book.getName());// 姓名
				writeD(book.getMapId());// 地圖編號
				writeH(book.getLocX());// X
				writeH(book.getLocY());// Y
			}
			break;
		}
	}


	private void callSomething() {
		Iterator<L1PcInstance> itr = L1World.getInstance().getAllPlayers().iterator();

		writeC(L1World.getInstance().getAllPlayers().size());
		L1PcInstance pc = null;
		Account acc = null;
		Calendar cal = null;
		while (itr.hasNext()) {
			pc = itr.next();
			acc = Account.load(pc.getAccountName());
			// 首先將時間信息設為登錄時間
		if (acc == null) {
			writeD(0);
		} else {
			cal = Calendar.getInstance(TimeZone.getTimeZone(Config.Synchronization.TimeZone));
			long lastactive = acc.getLastActive().getTime();
			cal.setTimeInMillis(lastactive);
			cal.set(Calendar.YEAR, 1970);
			int time = (int) (cal.getTimeInMillis() / 1000);
			writeD(time); // JST 1970 1/1 09:00 為基準
		}

		// 角色信息
		writeS(pc.getName()); // 最多12個半角字符
		writeS(pc.getClanname()); // 在[]內顯示的角色行，最多12個半角字符
		}
		}

		public S_PacketBox(boolean run, int type, int time) {
		writeC(Opcodes.S_EVENT);
		writeC(0x56);
		writeC(0x3e);
		writeC(run ? 2 : 1);
		writeD(time);
		writeC(type);
		// 129 : 成長的祝福 : EXP+20%
		// 130 : 成長的祝福 : EXP+25%
		// 131 : 成長的祝福 : EXP+30%
		// 132 : 成長的祝福 : 一般 EXP+20% 網咖 EXP+40%
		// 133 : 成長的祝福 : EXP+30%
		// 134 : 成長的祝福 : 一般 EXP+20% 網咖 EXP+40%
		// 135 : 出席Buff : EXP+20% 傷害減少+5
		// 136 : 成長的祝福 : 一般 EXP+30% 網咖 EXP+40%
		// 137 : 成長的祝福 : 一般 EXP+30% 網咖 EXP+40%
		}

		@Override
		public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_PACKETBOX;
	}
}


