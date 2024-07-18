package l1j.server.server.serverpackets;

import l1j.server.MJTemplate.MJString;
import l1j.server.PowerBall.PowerBallController;
import l1j.server.PowerBall.PowerBallInfo;
import l1j.server.server.Opcodes;

public class S_FightBoard extends ServerBasePacket {


	private static final String S_RaceBoard = "[C] S_RaceBoard";

	public S_FightBoard(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_HYPERTEXT); // 寫入操作碼（假設為超文本格式）
		writeD(number); // 寫入數字

		writeS("psy"); // 寫入字符串 "psy"

		writeC(0); // 寫入 0

		writeH(11); // 寫入短整數 11
		writeS("朱諾"); // 寫入字符串 "朱諾"

		PowerBallInfo pInfo = PowerBallController.getinfo(); // 獲取 PowerBall 信息
		if(pInfo == null) {
			writeS("目前的 PowerBall 遊戲已被遊戲管理員暫停。"); // 如果 pInfo 為 null，寫入提示信息
			for(int i = 0; i < 11; ++i) {
				writeS(MJString.EmptyString); // 寫入 11 個空字符串
			}
		} else {
			//writeS("來來，本次運動會上即將參賽的 PowerBall 狀態如下..."); // 新增的中文輸出
			writeS(String.format("[%s] 第 %d 次 [一般球]", pInfo.getDate(), pInfo.getNum())); // 寫入 PowerBall 信息（日期和次數）

			for(int i = 0; i < 3; ++i) {
				writeS(MJString.EmptyString); // 寫入 3 個空字符串
			}

			writeS("組合號碼  "); // 寫入字符串 "組合號碼"
			writeS(String.format("%d[%s]  ", pInfo.getPlusNum(), pInfo.getoddEven())); // 寫入組合號碼和奇偶性
			writeS(String.format("[%s]  ", pInfo.getUnderOver())); // 寫入上/下位信息
		}
	}
		
/*		writeS("getDate " + pInfo.getDate() + " ");
		writeS("getoddEven " + pInfo.getoddEven() + " ");
		writeS("getUnderOver " + pInfo.getUnderOver() + " ");
		writeS("getNextNum " + pInfo.getNextNum() + " ");
		writeS("getNum " + pInfo.getNum() + " ");
		writeS("getPlusNum " + pInfo.getPlusNum() + " ");
		writeS("getTodatCount " + pInfo.getTodatCount() + " ");
		writeS("getTotalNum " + pInfo.getTotalNum() + " ");*/
		// 8
		
		for(int i=0; i<3; ++i) {
			writeS(MJString.EmptyString);
		}		
	}
	
	 /*
    writeS("莫拉");
    writeS("來來，本次運動會上即將參賽的魔法人偶狀態如下...");
    writeS("名稱  ");
    writeS("狀態  ");
    writeS("勝率");
    for (int i = 0; i < 2; ++i) {
        writeS(BugFightController.getInstance()._dogfight[i].getName()); // 鬥狗名稱
        writeS(" ["); // 狀態
        writeS(Double.toString(BugFightController.getInstance()._winRate[i]) + "%]"); // 勝率
    }
    */
}
	writeH(0x00);
/*
	writeS("");
	writeS("");
	writeS("");
	writeS("");
	writeS("");
	writeS("");
	writeS("");
	writeS("");
	writeS("");*/
}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_RaceBoard;
	}
}


