package l1j.server.server.command.executor;

import l1j.server.server.GMCommandsConfig;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1GMRoom implements L1CommandExecutor {

	private L1GMRoom() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1GMRoom();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int i = 0;
			try {
				i = Integer.parseInt(arg);
			} catch (NumberFormatException e) {
			}

			 if (i == 1) {
				pc.start_teleport(32737, 32796, 99, 5, 18339, false, false);
			} else if (i == 2) {
				pc.start_teleport(33440, 32805, 4, 5, 18339, false, false);
			} else if (i == 3) {
				pc.start_teleport(33082, 33390, 4, 5, 18339, false, false);
			} else if (i == 4) {
				pc.start_teleport(34055, 32290, 4, 5, 18339, false, false);
			} else if (i == 5) {
				pc.start_teleport(33723, 32495, 4, 5, 18339, false, false);
			} else if (i == 6) {
				pc.start_teleport(32614, 32788, 4, 5, 18339, false, false);
			} else if (i == 7) {
				pc.start_teleport(32678, 32857, 2005, 5, 18339, false, false);
			} else if (i == 8) {
				pc.start_teleport(33515, 32858, 4, 5, 18339, false, false);
			} else if (i == 9) {
				pc.start_teleport(32763, 32817, 622, 5, 18339, false, false);
			} else if (i == 10) {
				pc.start_teleport(32572, 32944, 0, 5, 18339, false, false);
			} else if (i == 11) {
				pc.start_teleport(32736, 32796, 16896, 5, 18339, false, false);
			} else if (i == 12) {
				pc.start_teleport(32783, 32865, 621, pc.getHeading(), 18339, false, false);
			} else if (i == 13) {
				pc.start_teleport(32805, 32814, 5490, 5, 18339, false, false);
			} else if (i == 14) {
				pc.start_teleport(32736, 32787, 15, 5, 18339, false, false);
			} else if (i == 15) {
				pc.start_teleport(32735, 32788, 29, 5, 18339, false, false);
			} else if (i == 16) {
				pc.start_teleport(32730, 32802, 52, 5, 18339, false, false);
			} else if (i == 17) {
				pc.start_teleport(32572, 32826, 64, 5, 18339, false, false);
			} else if (i == 18) {
				pc.start_teleport(32895, 32533, 300, 5, 18339, false, false);
			} else if (i == 19) {
				pc.start_teleport(33168, 32779, 4, 5, 18339, false, false);
			} else if (i == 20) {
				pc.start_teleport(32623, 33379, 4, 5, 18339, false, false);
			} else if (i == 21) {
//				L1Teleport.teleport(pc , 33630, 32677, (short) 4, 5, true); // 기란 수호탑
				pc.start_teleport(33630, 32677, 4, 5, 18339, false, false);
			} else if (i == 22) {
//				L1Teleport.teleport(pc , 33524, 33394, (short) 4, 5, true); // 하이네 수호탑
				pc.start_teleport(33524, 33394, 4, 5, 18339, false, false);
			} else if (i == 23) {
//				L1Teleport.teleport(pc , 34090, 33260, (short) 4, 5, true); // 아덴 수호탑
				pc.start_teleport(34090, 33260, 4, 5, 18339, false, false);
			} else if (i == 24) {
//				L1Teleport.teleport(pc , 32424, 33068, (short) 440, 5, true); // 해적섬
				pc.start_teleport(32424, 33068, 440, 5, 18339, false, false);
			} else if (i == 25) {
//				L1Teleport.teleport(pc , 32800, 32868, (short) 1001, 5, true); // 베헤모스
				pc.start_teleport(32800, 32868, 1001, 5, 18339, false, false);
			} else if (i == 26) {
//				L1Teleport.teleport(pc , 32800, 32856, (short) 1000, 5, true); // 실베리아
				pc.start_teleport(32800, 32856, 1000, 5, 18339, false, false);
			} else if (i == 27) {
//				L1Teleport.teleport(pc , 32630, 32903, (short) 780, 5, true); // 테베사막
				pc.start_teleport(32630, 32903, 780, 5, 18339, false, false);
			} else if (i == 28) {
//				L1Teleport.teleport(pc , 32743, 32799, (short) 781, 5, true); // 테베 피라미드 내부
				pc.start_teleport(32743, 32799, 781, 5, 18339, false, false);
			} else if (i == 29) {
//				L1Teleport.teleport(pc , 32735, 32830, (short) 782, 5, true); // 테베 오리시스 제단
				pc.start_teleport(32735, 32830, 782, 5, 18339, false, false);
			} else if (i == 30) {
//				L1Teleport.teleport(pc , 32734, 32270, (short) 4, 5, true); // 피닉
				pc.start_teleport(32734, 32270, 4, 5, 18339, false, false);
			} else if (i == 31) {
//				L1Teleport.teleport(pc , 32699, 32819, (short) 82, 5, true); // 데몬
				pc.start_teleport(32699, 32819, 82, 5, 18339, false, false);
			} else if (i == 32) {
//				L1Teleport.teleport(pc , 32769, 32770, (short) 56, 5, true); // 기감4층
				pc.start_teleport(32769, 32770, 56, 5, 18339, false, false);
			} else if (i == 33) {
//				L1Teleport.teleport(pc , 32929, 32995, (short) 410, 5, true); // 마족신전	
				pc.start_teleport(32929, 32995, 410, 5, 18339, false, false);
			} else if (i == 34) {
//				L1Teleport.teleport(pc , 32791, 32691, (short) 1005, 5, true); // 레이드 안타라스
				pc.start_teleport(32791, 32691, 1005, 5, 18339, false, false);
			} else if (i == 35) {
//				L1Teleport.teleport(pc , 32960, 32840, (short) 1011, 5, true); // 레이드 파푸리온
				pc.start_teleport(32960, 32840, 1011, 5, 18339, false, false);
			} else if (i == 36) {
//				L1Teleport.teleport(pc , 32849, 32876, (short) 1017, 5, true); // 린드레이드
				pc.start_teleport(32849, 32876, 1017, 5, 18339, false, false);
			} else if (i == 37) {
//				L1Teleport.teleport(pc , 32725, 32800, (short) 67, 5, true); // 발라방
				pc.start_teleport(32725, 32800, 67, 5, 18339, false, false);
			} else if (i == 38) {
//				L1Teleport.teleport(pc , 32771, 32831, (short) 65, 5, true); // 파푸방
				pc.start_teleport(32771, 32831, 65, 5, 18339, false, false);
			} else if (i == 39) {
//				L1Teleport.teleport(pc , 32696, 32824, (short) 37, 5, true); // 버모스 (용던7층)
				pc.start_teleport(32696, 32824, 37, 5, 18339, false, false);
			} else if (i == 40) {
//				L1Teleport.teleport(pc , 32922, 32812, (short) 430, 5, true); // 정령무덤
				pc.start_teleport(32922, 32812, 430, 5, 18339, false, false);
			} else if (i == 41) {
//				L1Teleport.teleport(pc , 32737, 32834, (short) 2004, 5, true); // 고라스
				pc.start_teleport(32737, 32834, 2004, 5, 18339, false, false);
			} else if (i == 42) {
//				L1Teleport.teleport(pc , 32707, 32846, (short) 2, 5, true); // 섬던2층
				pc.start_teleport(32707, 32846, 2, 5, 18339, false, false);
			} else if (i == 43) {
//				L1Teleport.teleport(pc , 32772, 32861, (short) 400, 5, true); // 고대무덤
				pc.start_teleport(32772, 32861, 400, 5, 18339, false, false);
			} else if (i == 44) {
//				L1Teleport.teleport(pc , 32982, 32808, (short) 244, 5, true); // 오땅
				pc.start_teleport(32982, 32808, 244, 5, 18339, false, false);
			} else if (i == 45) {
//				L1Teleport.teleport(pc , 32811, 32819, (short) 460, 5, true); // 라바2층
				pc.start_teleport(32811, 32819, 460, 5, 18339, false, false);
			} else if (i == 46) {
//				L1Teleport.teleport(pc , 32724, 32792, (short) 536, 5, true); // 라던3층
				pc.start_teleport(32724, 32792, 536, 5, 18339, false, false);
			} else if (i == 47) {
//				L1Teleport.teleport(pc , 32847, 32793, (short) 532, 5, true); // 라던4층
				pc.start_teleport(32847, 32793, 532, 5, 18339, false, false);
			} else if (i == 48) {
//				L1Teleport.teleport(pc , 32843, 32693, (short) 550, 5, true); // 선박무덤
				pc.start_teleport(32843, 32693, 550, 5, 18339, false, false);
			} else if (i == 49) {
//				L1Teleport.teleport(pc , 32781, 32801, (short) 558, 5, true); // 심해
				pc.start_teleport(32781, 32801, 558, 5, 18339, false, false);
			} else if (i == 50) {
//				L1Teleport.teleport(pc , 32731, 32862, (short) 784, 5, true); // 제브
				pc.start_teleport(32731, 32862, 784, 5, 18339, false, false);
			} else if (i == 51) {
//				L1Teleport.teleport(pc , 32728, 32704, (short) 4, 5, true); // 균열 1
				pc.start_teleport(32728, 32704, 4, 5, 18339, false, false);
			} else if (i == 52) {
//				L1Teleport.teleport(pc , 32827, 32658, (short) 4, 5, true); // 균열 2
				pc.start_teleport(32827, 32658, 4, 5, 18339, false, false);
			} else if (i == 53) {
//				L1Teleport.teleport(pc , 32852, 32713, (short) 4, 5, true); // 균열 3
				pc.start_teleport(32852, 32713, 4, 5, 18339, false, false);
			} else if (i == 54) {
//				L1Teleport.teleport(pc , 32914, 33427, (short) 4, 5, true); // 균열 4
				pc.start_teleport(32914, 33427, 4, 5, 18339, false, false);
			} else if (i == 55) {
//				L1Teleport.teleport(pc , 32962, 33251, (short) 4, 5, true); // 균열 5
				pc.start_teleport(32962, 33251, 4, 5, 18339, false, false);
			} else if (i == 56) {
//				L1Teleport.teleport(pc , 32908, 33169, (short) 4, 5, true); // 균열 6
				pc.start_teleport(32908, 33169, 4, 5, 18339, false, false);
			} else if (i == 57) {
//				L1Teleport.teleport(pc , 34272, 33361, (short) 4, 5, true); // 균열 7
				pc.start_teleport(34272, 33361, 4, 5, 18339, false, false);
			} else if (i == 58) {
//				L1Teleport.teleport(pc , 34258, 33202, (short) 4, 5, true); // 균열 8
				pc.start_teleport(34258, 33202, 4, 5, 18339, false, false);
			} else if (i == 59) {
//				L1Teleport.teleport(pc , 34225, 33313, (short) 4, 5, true); // 균열 9
				pc.start_teleport(34225, 33313, 4, 5, 18339, false, false);
			} else if (i == 60) {
//				L1Teleport.teleport(pc , 32682, 32892, (short) 5167, 5, true); // 악마영토
				pc.start_teleport(32682, 32892, 5167, 5, 18339, false, false);
			} else if (i == 61) {
//				L1Teleport.teleport(pc , 32862, 32862, (short) 537, 5, true); // 기르타스
				pc.start_teleport(32862, 32862, 537, 5, 18339, false, false);
			} else if (i == 62) {
//				L1Teleport.teleport(pc , 32738, 32448, (short) 4, 5, true); // 화전민
				pc.start_teleport(32738, 32448, 4, 5, 18339, false, false);
			} else if (i == 63) {
//				L1Teleport.teleport(pc , 32797, 32285, (short) 4, 5, true); // 오성수탑
				pc.start_teleport(32797, 32285, 4, 5, 18339, false, false);
			} else if (i == 64) {
//				L1Teleport.teleport(pc , 33052, 32339, (short) 4, 5, true); // 요정숲
				pc.start_teleport(33052, 32339, 4, 5, 18339, false, false);
			} else if (i == 65) {
//				L1Teleport.teleport(pc, 32738, 32872, (short) 2236, 5, true); // 서버지기아지트
				pc.start_teleport(32738, 32872, 2236, 5, 18339, false, false);
			} else if (i == 66) {
				pc.start_teleport(32737, 32868, 2237, 5, 18339, false, false);
			} else if (i == 67) {
				pc.start_teleport(32736, 32796, 16896, 5, 18339, false, false);
			} else if (i == 68) {
				pc.start_teleport(32736, 32799, 34, 5, 18339, false, false);
			} else if (i == 69) {
				pc.start_teleport(32769, 32827, 610, 5, 18339, false, false);
			} else if (i == 70) {
					pc.start_teleport(32764, 32819, 5554, 5, 18339, false, false);
			} else {
                 L1Location loc = GMCommandsConfig.ROOMS.get(arg.toLowerCase()); // 根據使用者輸入的命令參數獲取對應的地點
                 if (loc == null) { // 如果地點無效，發送地點列表提示信息給使用者
                     public void execute(L1PcInstance pc, String cmdName, String arg) {
                         try {
                             // 根據使用者輸入的命令參數獲取對應的地點
                             L1Location loc = GMCommandsConfig.ROOMS.get(arg.toLowerCase());
                             if (loc == null) {
                                 // 如果地點無效，發送地點列表提示信息給使用者
                                 pc.sendPackets("\f3==================== 返還列表 =====================");
                                 pc.sendPackets(new S_SystemMessage("\aE[1:GM房間] [2:基蘭] [3:銀騎士村] [4:歐瑞] [5:威爾頓] [6:古魯丁] [7:隱藏山谷]"));
                                 pc.sendPackets(new S_SystemMessage("\aE[8:巴蘭卡] [9:羽毛村] [10:馬爾島] [11:馬爾島旅館] [12:諮詢所] [13:釣魚]"));
                                 pc.sendPackets(new S_SystemMessage("\aR[14:肯特城] [15:溫達城] [16:基蘭城] [17:海音城] [18:亞丁城]"));
                                 pc.sendPackets(new S_SystemMessage("\aR[19:守護塔] [20:守護塔] [21:守護塔] [22:守護塔] [23:守護塔]"));
                                 pc.sendPackets(new S_SystemMessage("\aW[24:海音] [25:貝希摩斯] [26:矛之島] [27:底比斯] [28:金字塔]"));
                                 pc.sendPackets(new S_SystemMessage("\aW[29:金字塔] [30:不死鳥] [31:惡魔] [32:魔族神殿4層] [33:惡魔房間]"));
                                 pc.sendPackets(new S_SystemMessage("\aU[34:安塔瑞斯] [35:巴風特] [36:林德拜爾] [37:巴拉卡斯] [38:古巴風特] [39:巴莫斯]"));
                                 pc.sendPackets(new S_SystemMessage("\aU[40:政務] [41:戈拉斯] [42:島嶼地牢2層] [43:古墓] [44:奧坦]"));
                                 pc.sendPackets(new S_SystemMessage("\aI[45:拉頓2層] [46:拉頓3層] [47:拉頓4層] [48:船艙] [49:深海]"));
                                 pc.sendPackets(new S_SystemMessage("\aI[50:賽伯雷克] [51~59:裂縫] [60:魔王領地] [61:吉爾塔斯]"));
                                 pc.sendPackets(new S_SystemMessage("\aO[62:火田民村] [63:奧克城] [64:妖精森林] [65:服務器管理員的據點]"));
                                 pc.sendPackets(new S_SystemMessage("\aO[66:幼苗街1號(諮詢所)] [67:VIP諮詢所] [68:自動地圖]"));
                                 pc.sendPackets(new S_SystemMessage("\aP[69:諮詢所1] [70:正式諮詢所]"));
                                 return;
                             }

                             // 傳送角色到指定地點
                             pc.start_teleport(loc.getX(), loc.getY(), loc.getMapId(), 5, 18339, false, false);

                             // 如果設置了有效的地點編號，發送傳送確認消息
                             int i = Integer.parseInt(arg); // 假設 arg 是一個整數字符串
                             if (i > 0 && i < 33) {
                                 pc.sendPackets(new S_SystemMessage("運營者返還(" + i + ")號地點移動完成。"));
                             }

                         } catch (Exception exception) {
                             // 捕捉異常並發送錯誤消息給使用者
                             pc.sendPackets(new S_SystemMessage(".返還 [地點名] 請輸入。(地點名請參考 GMCommands.xml)"));
                         }
                     }
