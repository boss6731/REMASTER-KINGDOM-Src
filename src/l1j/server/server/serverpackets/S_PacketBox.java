package l1j.server.server.server.serverpackets;

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
import l1j.server.server.server.datatables.PolyTable;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.utils.SQLUtil;

import static com.sun.org.apache.xml.internal.security.c14n.implementations.UtfHelpper.writeByte;

/**
     * 用於顯示技能圖標或屏蔽列表等多種用途的封包類
     */
public class S_PacketBox extends ServerBasePacket {

	public static final int ATTACK_RANGE = 1;
	private static final String S_PACKETBOX = "[S] S_PacketBox";

	// *** S_107 子代碼列表 ***

	// 1:Kent 2:Orc 3:WW 4:Giran 5:Heine 6:Dwarf 7:Aden 8:Diad 9:城名 9 ...
	/** C(id) H(?): %s的攻城戰已經開始。 */
	public static final int MSG_WAR_BEGIN = 0;

	/** C(id) H(?): %s的攻城戰已經結束。 */
	public static final int MSG_WAR_END = 1;

	/** C(id) H(?): %s的攻城戰正在進行中。 */
	public static final int MSG_WAR_GOING = 2;

	/** -: 獲得了城堡的主導權。（音樂變了） */
	public static final int MSG_WAR_INITIATIVE = 3;

	/** -: 占領了城堡。 */
	public static final int MSG_WAR_OCCUPY = 4;

	/** ?: 決鬥結束了。（音樂變了） */
	public static final int MSG_DUEL = 5;

	/** C(count): SMS發送失敗。/ 全部發送了%d條。 */
	public static final int MSG_SMS_SENT = 6;

	/** -: 在祝福下，兩人結為夫妻。（音樂變了） */
	public static final int MSG_MARRIED = 9;

	/** C(weight): 重量（30等級） */
	public static final int WEIGHT = 10;

	/** C(food): 滿腹度（30等級） */
	public static final int FOOD = 11;

	/** C(0) C(level): 此物品僅限%d級以下使用。（僅顯示0~49等級） */
	public static final int MSG_LEVEL_OVER = 12;

	/** UB資訊 HTML */
	public static final int HTML_UB = 14;

	/**
	 * C(id)<br>
	 * 1: 感覺到體內所含的精靈之力溶解在空氣中。<br>
	 * 2: 火的精靈之力滲透到身體的每個角落。<br>
	 * 3: 水的精靈之力滲透到身體的每個角落。<br>
	 * 4: 風的精靈之力滲透到身體的每個角落。<br>
	 * 5: 地的精靈之力滲透到身體的每個角落。<br>
	 */
	public static final int MSG_ELF = 15;

	/** C(count) S(name)...: 屏蔽列表 */
	public static final int SHOW_LIST_EXCLUDE = 17;

	/** S(name): 添加到屏蔽列表 */
	public static final int ADD_EXCLUDE = 18;

	/** S(name): 解除屏蔽 */
	public static final int REM_EXCLUDE = 19;

	/** 網吧BUFF */
	public static final int PC_BUFF = 127;

	/** 技能圖標 */
	public static final int ICONS1 = 20;

	/** 技能圖標 */
	public static final int ICONS2 = 21;

	/** 刪除光環系技能圖標和抹除魔法圖標 */
	public static final int ICON_AURA = 22;

	/** S(name): 被選為城鎮領袖的是%s。 */
	public static final int MSG_TOWN_LEADER = 23;

	/**
	 * D(盟友數) (S(盟友名) C(盟友等級)) 當盟友信息更新時的/盟聊。
	 */
	public static final int PLEDGE_TWO = 24;// 添加

	/**
	 * D(盟友名) C(階級) 當有新成員加入盟時發送的封包
	 */
	public static final int PLEDGE_REFRESH_PLUS = 25;// 添加

	/**
	 * D(盟友名) C(階級) 當有成員從盟中被移除時發送的封包
	 */
	public static final int PLEDGE_REFRESH_MINUS = 26; // 添加

	/**
	 * C(id): 您的階級已更改為%s。<br>
	 * id - 1: 見習 2: 一般 3: 守衛
	 */
	public static final int MSG_RANK_CHANGED = 27;

    /**
     * D(盟友數) (S(盟友名) C(盟友等級)) 當盟友信息未更新時的/盟聊。
     */
	// public static final int PLEDGE_ONE = 119; // 添加

	/** D(?) S(name) S(clanname): %s盟的%s清理了拉斯塔巴德軍。 */
	public static final int MSG_WIN_LASTAVARD = 30;

	/** -: 1心情變好了。 */
	public static final int MSG_FEEL_GOOD = 31;

	/** 未知。C_30 封包發送 */
	public static final int SOMETHING1 = 33;

	/** H(time): 顯示部分藍色藥水的圖標。 */
	public static final int ICON_BLUEPOTION = 34;

	/** H(time): 顯示變身的圖標。 */
	public static final int ICON_POLYMORPH = 35;

	/** H(time): 顯示禁言的圖標。 */
	public static final int ICON_CHATBAN = 36;

	/** 未知。C_7 封包發送。C_7 也會在打開寵物菜單時發送。 */
	public static final int SOMETHING2 = 37;

	/** 顯示血盟信息的HTML */
	public static final int HTML_CLAN1 = 38;

	/** H(time): 顯示免疫的圖標 */
	public static final int ICON_I2H = 40;

	/** 發送角色的遊戲選項和快捷鍵信息等 */
	public static final int CHARACTER_CONFIG = 41;

	/** 返回角色選擇畫面 */
	public static final int LOGOUT = 42;

	/** 戰鬥中無法重新登錄。 */
	public static final int MSG_CANT_LOGOUT = 43;

	/**
	 * C(count) D(time) S(name) S(info):<br>
	 * 顯示帶有[CALL]按鈕的窗口。這似乎是一個用於檢查機器人等不正當行為者的功能。雙擊名稱
	 * (目標)會發送C_RequestWho封包，並在客戶端的文件夾中生成bot_list.txt。選擇名稱並按+鍵
	 * (目標)會打開一個新窗口。
	 */
	public static final int CALL_SOMETHING = 45;

	/**
	 * C(id): 戰鬥競技場，混沌對決― <br>
	 * id - 1:開始 2:已刪除 3:結束
	 */
	public static final int MSG_COLOSSEUM = 49;

	// 顯示血盟信息的HTML
	public static final int HTML_CLAN2 = 51;

	// 打開烹飪窗口
	public static final int COOK_WINDOW = 52;

	/** C(type) H(time): 顯示烹飪圖標 */
	public static final int ICON_COOKING = 53;

	/** 釣魚浮標震動效果 */
	public static final int FISHING = 55;

	/** 刪除圖標 */
	public static final int DEL_ICON = 59;

	/** 龍珠（三段加速） */
	public static final int DRAGON_PEARL = 60;

	/** 聯盟列表 */
	public static final int ALLIANCE_LIST = 62;

	/** 迷你遊戲：5,4,3,2,1倒數 */
	public static final int MINIGAME_START_COUNT = 64;

	/** 迷你遊戲：計時（從0:00開始） */
	public static final int MINIGAME_TIME2 = 65;

	/** 迷你遊戲：玩家列表 */
	public static final int MINIGAME_LIST = 66;

	/** 迷你遊戲：即將被傳送回村莊（10秒倒數） */
	public static final int MINIGAME_10SECOND_COUNT = 69;

	/** 迷你遊戲：結束 */
	public static final int MINIGAME_END = 70;

	/** 迷你遊戲：時間 */
	public static final int MINIGAME_TIME = 71;

	/** 迷你遊戲：時間清除 */
	public static final int MINIGAME_TIME_CLEAR = 72;

	/** 龍騎士：弱點暴露 */
	public static final int SPOT = 75;

	public static final int aaaa1 = 78; // 攻城戰開始了。
	public static final int bbbb2 = 79; // 攻城戰結束了。
	public static final int cccc3 = 80; // 攻城戰正在進行中。
	/** 愛因哈薩德BUFF */
	public static final int EINHASAD = 82;
	/** 1: 粉色邊框, 2: 震動, 3: 爆竹效果 */
	public static final int HADIN_DISPLAY = 83;
	/** 副本綠色消息 */
	public static final int GREEN_MESSAGE = 84;
	/** 副本黃色消息 */
	public static final int YELLOW_MESSAGE = 61; // 副本第2章等待
	/** 副本紅色消息 */
	public static final int RED_MESSAGE = 51; // 紅色消息
	/** 副本記分板 */
	public static final int SCORE_MARK = 4;
	/** 翡翠BUFF圖標 */
	public static final int EMERALD_ICON = 86;

	/**
	 * 顯示友好度UI + 欲望洞窟 - 陰影神殿
	 */
	public static final int KARMA = 87; // 添加

	/** 顯示閃避狀態 */
	public static final int INIT_DODGE = 88; // 添加

	/** 龍的血痕（安塔瑞斯: 82，巴風特: 85） */
	public static final int DRAGONBLOOD = 100;

	public static final int DODGE = 101;

	public static final int DragonMenu = 102;

	/** 位置傳送 **/
	public static final int MINI_MAP_SEND = 111;

	/** 血盟倉庫列表 */
	public static final int CLAN_WAREHOUSE_LIST = 117;

	/** 巴風特服務器封包 */
	public static final int BAPO = 114;

	public static final int ICON_SECURITY_SERVICES = 125; // 安全BUFF

	/** 網吧BUFF圖標 */
	public static final int ICON_PC_BUFF = 127;

	public static final int ER_UpDate = 132;

	public static final int BOOKMARK_SIZE_PLUS_10 = 141; // 記憶擴展
	public static final int BOOKMARK = 142;

	/** 顯示圖標 **/
	public static final int UNLIMITED_ICON = 147;

	public static final int UNLIMITED_ICON1 = 180; // 無限制封包
	public static final int NONE_TIME_ICON = 180;

	/** 封印實時狀態 */
	public static final int ITEM_STATUS = 149;

	public static final int MAP_TIMER = 153;

	/** 顯示蝴蝶騎士的 castgfx 值的BUFF圖標在BUFF欄中 **/
	public static final int BUFFICON = 154;

	public static final int ROUND = 156;

	public static final int ROUND1 = 156;

	public static final int DungeonTime = 159; // 地下城封包

	/** 顯示毒相關圖標 UI6 **/
	public static final int POSION_ICON = 161;

	/** 血盟BUFF圖標 */
	public static final int CLAN_BUFF_ICON = 165;

	/** UI6 3.80 血盟相關 **/
	public static final int HTML_PLEDGE_ANNOUNCE = 167;

	public static final int HTML_PLEDGE_REALEASE_ANNOUNCE = 168;

	public static final int HTML_PLEDGE_WRITE_NOTES = 169;

	public static final int HTML_PLEDGE_MEMBERS = 170;

	public static final int HTML_PLEDGE_ONLINE_MEMBERS = 171;

	public static final int ITEM_ENCHANT_UPDATE = 172;

	public static final int PLEDGE_EMBLEM_STATUS = 173; // 徽章狀態

	public static final int TOWN_TELEPORT = 176;

	public static final int ATTACK_RANGE = 160; // 攻擊可及距離
	public static final int UNKNOWN2 = 184; // 主君的傷害BUFF
	public static final int UNKNOWN3 = 188; // 主君的傷害BUFF
	public static final int INVENTORY_SAVE = 189; // 存儲背包
	public static final int BATTLE_SHOT = 181; // 戰鬥射擊
	public static final int SHOP_OPEN_COUNT = 198; // 開店次數
	public static final int USER_BACKSTAB = 193; // 玩家背刺

	public static final int EFFECT_DURATOR = 194;//

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
			case UNKNOWN3:
				// 處理主君的傷害BUFF的邏輯
				writeD(0);
				writeD(0);
				break;
			case INVENTORY_SAVE:
				writeD(0x00); // 原始代碼
				break;
			case UNKNOWN2:
				// 處理主君的傷害BUFF的邏輯
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

	/** 等级提升BUFF **/
	public S_PacketBox(int time, boolean ck, boolean ck2) {
		writeC(Opcodes.S_EVENT);
		writeC(0x56);
		writeC(0xAA);
		writeC(0x01);
		writeH(time / 16);
		writeH(0x00);
	}
	    // TODO: 手动添加2格攻击的图像
        //private static final int spearGfxIds[] = {
        //		1002, 6830, 6831, 7143, 7144, 7147, 7148, 7151, 7152, 7155, 7156, 7159,
        //		7160, 7163, 7164, // 龙骑士 夏鲁纳
        //		11330, 11351, 11344, 11368, 12240, 11447, 14928, 13389, 12237, 11419, 11418, 14923, 13715, 13717, 13719, 15115,
        //		13721, 13723, 13725, 13727, 13729, 13731, 13733, 13735, 13737, 13739, 13741, 13743, 13745, // 排名变身
        //		13380, 13381, 14487, // PC可可马变身，橙色
        //		11121, 11125, // 遭遇变身
        //		16008, 16056, // 新变身
        //		15115, // 男骑士 矛
        //		// TODO: 变身卷轴
        //		15539, 15537, 15534, 11376, 15599, 15834, 15832, 15833, 15531,
        //		// TODO: 真 死亡骑士 变身戒指
        //		17275, 17272,
        //		11685, 12015,
        //		17545, 17549,
        //		19026



	public S_PacketBox(int subCode, L1PcInstance pc, L1ItemInstance weapon) { //108 160
		writeC(Opcodes.S_EVENT);
		int attackableDistance = 0;
		writeC(attackableDistance);
        //        1:劍, 2:匕首, 3:雙手劍, 4:弓, 5:槍, 6:鈍器, 7:法杖, 8:投擲刀,
        //        9:箭, 10:護手, 11:爪, 12:二刀流, 13:單手弓, 14:單手槍, 15:雙手鈍器, 16:雙手法杖, 17:鑰匙圈, 18:鏈鋸劍
		int range = 1;
		int equipType = 0; // 劍 1 / 斧頭 2 / 弓 3 / 二刀流 4 / 槍 5 / 法杖 6 / 匕首 7 / 爪、鏈刃 8 / 鏈鋸劍 10 / 9號會是什麼呢？
		int isTohand = 0;
		int charGfxId = pc.getCurrentSpriteId();
		int charBaseGfxId = pc.getClassId();
		if (weapon != null && weapon.isEquipped()) {
			isTohand = weapon.getItem().isTwohandedWeapon() ? 1 : 0; //雙手與否。雙手則為1，否則為0
			int weaponType = weapon.getItem().getType();
			switch (weaponType) {
				case 1 : case 3 : equipType = 1; break; //單手劍，雙手劍
				case 6 : case 15 : equipType = 2; break; //單手斧，雙手斧
				case 4 : case 13 : case 10 :
					equipType = 3;
					range = 24;
					if (weaponType == 4)
						range = 24; // 當使用雙手弓時

					if (pc.hasSkillEffect(L1SkillId.BURNING_SHOT)) {
						range = 10;
					}

					break; // 雙手弓，單手弓，護手
				case 12 : equipType = 4; break; // 二刀流
				case 5 : case 14 : case 18 : equipType = 5; range = 2; // 雙手槍，單手槍，鏈鋸劍
					if (weaponType == 18) equipType = 10;
					boolean gfxCk = false;
					L1PolyMorph poly = PolyTable.getInstance().getTemplate(charGfxId);
					if(poly != null && poly.isSpearGfx()) {
						gfxCk = true;
					}
                        //				for (int gfxId : spearGfxIds) {
                        //					if (gfxId == charGfxId) {
                        //						gfxCk = true;
                        //						break;
                        //					}
                        //				}
					if (!gfxCk && charBaseGfxId != charGfxId)
						range = 1;
					break;
				case 7:
				case 16:
					equipType = 6;
					// 單手法杖，雙手法杖
				case 2:
					equipType = 7;
					break; // 匕首
				case 11:
					equipType = 8; // 爪
					break;
				case 17:
					equipType = 8; // 爪
					range = 4;
					break; // 爪，鑰匙圈
				default: // 其他沒有的武器。
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
        writeC(isTohand); // 雙手與否
		/* 如果玩家是GM (遊戲管理員)，則發送訊息顯示攻擊距離和變身代碼
        if (pc.isGm()) {
            pc.sendPackets(new S_SystemMessage("\\aA攻擊可能距離 : " + range + " / 變身代碼 : " + charGfxId));
        }
        */
    }
        public S_PacketBox(int subCode, int range, int type, boolean bow, L1PcInstance pc) {
            writeC(Opcodes.S_EVENT);
            writeC(subCode);
            switch (subCode) {
                case ATTACK_RANGE:
                    writeC(range);
                    writeC(type);
                    if (bow)
                        writeC(1);
                    else {
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
                case DungeonTime:// 12月14日變更
                    writeD(7);
                    writeD(1);
                    writeS("$12125");// 幾感
                    writeD(time1);
                    writeD(2);
                    writeS("$6081");// 象牙塔
                    writeD(time2);
                    writeD(15);
                    writeS("$13527");// PC房 Balrog陣營
                    writeD(time3);
                    writeD(500);
                    writeS("$19375");// PC房 政務
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
                case USER_BACKSTAB:
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
                case 204: // 連擊系統
                    writeC(value);
                    writeH(0); // ?
                    break;
                case PC_ROOM_BUFF:
                    if (value == 1) {
                        writeC(0x18);
                    } else {
                        writeC(0);
                    }
                    break;
                case SHOP_OPENING_COUNT:
                    writeD(value);
                    writeD(0x28);
                    writeD(0x00);
                    break;
                case ICON_BLUEPOTION:
                case ICON_CHATBAN:
                case ICON_I2H:
                case MINIGAME_TIME:
                case INIT_DODGE:
                    writeH(value); // time
                    break;
                case MAP_TIMER:// 地圖計時器 地牢
                    writeD(value);
                    break;
                case BATTLE_SHOT:
                    writeD(value);
                    break;
                case MSG_WAR_BEGIN:
                case MSG_WAR_END:
                case MSG_WAR_GOING:
                    writeC(value); // castle id
                    writeH(0); // ?
                    break;
                case MSG_SMS_SENT:
                case WEIGHT:
                case FOOD:
                case DODGE:
                    writeC(value);
                    break;
                case MSG_ELF:
                case MSG_COLOSSEUM:
                case SPOT:
                case ER_UpDate:
                    writeC(value); // msg id
                    break;
                case MSG_LEVEL_OVER:
                    writeC(0); // ?
                    writeC(value); // 0-49 之外的值不顯示
                    break;
                case COOK_WINDOW:
                    writeC(0xdb); // ?
                    writeC(0x31);
                    writeC(0xdf);
                    writeC(0x02);
                    writeC(0x01);
                    writeC(value); // level
                    break;
                case MINIGAME_LIST:
                    writeH(0x00); // 參與者數
                    writeH(0x00); // 名次
                    break;
                case EINHASAD:
                    value /= 10000;
                    writeD(value);// 1 到 200 之間的百分比值
                    writeC(0);

                    // 1 = 2
                    // 2 = 5
                    // 3 = 7
                    // 4 = 10
                    // 5 = 12
                    // 8 = 20
                    // 9 = 23
                    // 16 = 40	: // 網咖
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
                    if (value == 0) { // 0 : 解除 1 : 開啟
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
            }// b0 04 80 08 00 00 00 00
        }

        public S_PacketBox(int subCode, int time, int gfxid, int type) {
            writeC(Opcodes.S_EVENT);
            writeC(subCode);
            switch (subCode) {
                case SOMETHING2:
                    writeC(time);
                    writeD(gfxid); // pet objid
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
                    case 62: // 成長藥水
                    // 在此處添加相應的處理邏輯
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
                        // 7號蘑菇湯，不使用，正式服無此項
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
                case EMERALD_ICON: // 綠寶石圖標
                    writeC(0x70);
                    writeC(0x01);
                    writeC(type);
                    writeH(time); // time(秒)
                    break;
                case NONE_TIME_ICON:
                    writeC(type);// on/off
                    writeD(time); // 166 exp 30% 228 涼爽的冰塊 286 exp 40% 343 基爾塔斯地區死亡懲罰
                    // 409 裝甲破壞 497 紅色騎士的證明 活動攻城區 // 477~479
                    writeD(0x00000D67);
                    writeH(0x00);
                    break;
                default:
                    break;
            }
        }

        /** 紫水晶，黃玉 **/
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

	private void writeByte(byte[] bytes) {

	}

	public S_PacketBox(int subCode, int id, String name, String clanName) {
            writeC(Opcodes.S_EVENT);
            writeC(subCode);

            switch (subCode) {
                case MSG_WIN_LASTAVARD:
                    writeD(id); // 克蘭 ID 還是什麼？
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
                case RED_MESSAGE:
                case YELLOW_MESSAGE:
                    writeC(2);
                    writeH(26204);
                    writeC(subCode2);
                    writeS(name);
                    break;
                case SCORE_MARK:
                    writeC(subCode2);
                    writeS(name);
                    break;
                default: // ?
                    switch (subCode1) {
                        case MSG_RANK_CHANGED:
                            writeC(subCode2);
                            writeS(name);
                            break;
                        case ADD_EXCLUDE:
                        case REM_EXCLUDE:
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
                        writeC(0x86); // 0x49 (龍之成長藥水停止)
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
                    writeC(item.getItemId() == 490012 ? 0x01 : 0x00); // 安塔 0
                    writeC(item.getItemId() == 490013 ? 0x01 : 0x00); // 法力昂 1
                    writeC(item.getItemId() == 490014 ? 0x01 : 0x00); // 林德 2
                    writeC(0);
                    break;
                case ITEM_ENCHANT_UPDATE: // 1102 更新後新增製作封包
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
                    writeC(item.getAttrEnchantBit(item.getAttrEnchantLevel()));// 製作 1102 新增
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
                    writeC(value1); // 類型：1. 毒  6. 沉默
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
                            writeC(rs3.getInt(4));// 0:存放 1:取回
                            writeS(rs3.getString(2));
                            writeD(rs3.getInt(3));
                            writeD((int) ((System.currentTimeMillis() - rs3.getTimestamp(5).getTime()) / 60000));
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
                // wrote(clan.getOnlineMemberCount());
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
                // // 所有血盟成員的姓名和等級
                // for (int i = 0; i < clanMemberList.size(); i++) {
                // member = clanMemberList.get(i);
                // writeS(member.name);
                // writeC(member.rank);
                // }
                //
                // writeD(clan.getOnlineMemberCount());
                // for (L1PcInstance targetPc : clan.getOnlineClanMember()) { // 在線
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
                case BAPO:
                    writeD(value); // 1~7 旗幟
                    writeD(show ? 0x01 : 0x00); // On Off
                    break;
                case UNLIMITED_ICON: // 無限封包
                    writeC(show ? 0x01 : 0x00); // On Off // true false
                    writeC(value); //
                    break;
                case UNLIMITED_ICON1:
                    writeC(show ? 0x01 : 0x00); // On Off // true false
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
                        writeS(book.getName()); // 姓名
                        writeD(book.getMapId()); // 地圖 ID
                        writeH(book.getLocX());// X
                        writeH(book.getLocY());// Y
                    }
                    break;
            }
        }

		protected void writeH(int locY) {

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
                // 時間信息，首先添加登錄時間
                if (acc == null) {
                    writeD(0);
                } else {
                    cal = Calendar.getInstance(TimeZone.getTimeZone(Config.Synchronization.TimeZone));
                    long lastactive = acc.getLastActive().getTime();
                    cal.setTimeInMillis(lastactive);
                    cal.set(Calendar.YEAR, 1970);
                    int time = (int) (cal.getTimeInMillis() / 1000);
                    writeD(time); // 以 JST 1970 年 1 月 1 日 09:00 為基準
                }

                // 角色信息
                writeS(pc.getName()); // 名字，最多12個半角字符
                writeS(pc.getClanname()); // 顯示在[]內的角色行，最多12個半角字符
            }
        }

		protected void writeS(String name) {

		}

		protected void writeD(int i) {

		}

		public S_PacketBox(boolean run, int type, int time) {
            writeC(Opcodes.S_EVENT);
            writeC(0x56);
            writeC(0x3e);
            writeC(run ? 2 : 1);
            writeD(time);
            writeC(type);
            // 129 : 成長的祝福 : 經驗值 +20%
            // 130 : 成長的祝福 : 經驗值 +25%
            // 131 : 成長的祝福 : 經驗值 +30%
            // 132 : 成長的祝福 : 普通經驗值 +20% 網吧經驗值 +40%
            // 133 : 成長的祝福 : 經驗值 +30%
            // 134 : 成長的祝福 : 普通經驗值 +20% 網吧經驗值 +40%
            // 135 : 出席增益 : 經驗值 +20% 傷害減少 +5
            // 136 : 成長的祝福 : 普通經驗值 +30% 網吧經驗值 +40%
            // 137 : 成長的祝福 : 普通經驗值 +30% 網吧經驗值 +40%
        }

		public void writeC(int sEvent) {

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
