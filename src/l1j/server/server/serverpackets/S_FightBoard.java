package l1j.server.server.serverpackets;

import l1j.server.MJTemplate.MJString;
import l1j.server.PowerBall.PowerBallController;
import l1j.server.PowerBall.PowerBallInfo;
import l1j.server.server.Opcodes;

import java.io.IOException;

public class S_FightBoard extends l1j.server.server.serverpackets.ServerBasePacket {

	private static final String S_RaceBoard = "[C] S_RaceBoard";

	public S_FightBoard(int number) {
		buildPacket(number);
	}

	@Override
	public byte[] getContent() throws IOException {
		return new byte[0];
	}

	private void buildPacket(int number) {
	writeC(Opcodes.S_HYPERTEXT);
	writeD(number);
	writeS("psy");
	writeC(0);                        
	writeH(11);
		writeS("朱諾");
		PowerBallInfo pInfo = PowerBallController.getinfo();
		if (pInfo == null) {
			writeS("目前的強力球遊戲已被遊戲管理員暫停。");
			for (int i = 0; i < 11; ++i) {
				writeS(MJString.EmptyString);
			}
		} else {
			// writeS("來~ 來，這次運動會將要參加比賽的強力球狀態是...");
			writeS(String.format("[%s] 第%d次 [一般球]", pInfo.getDate(), pInfo.getNum()));
		}
		
		for(int i=0; i<3; ++i) {
			writeS(MJString.EmptyString);
		}

		writeS("組合號碼 ");
		writeS(String.format("%d[%s]  ", pInfo.getPlusNum(), pInfo.getoddEven()));
		writeS(String.format("[%s]  ", pInfo.getUnderOver()));
		
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

    /*writeS("摩拉");
	writeS("來~ 來，這次運動會將要參加比賽的魔法人偶們的狀態是...");
	writeS("名稱  ");
	writeS("狀態  ");
	writeS("勝率");
	for( int i = 0; i < 2; ++i ) {
	writeS(BugFightController.getInstance()._dogfight[i].getName());                 //鬥狗名稱
	writeS(" [");//DogFightController.getInstance()._FightCondition[i]);                     //狀態
	writeS(Double.toString(BugFightController.getInstance()._winRate[i]) + "%]");     //勝率
	}*/
    void writeH(0x00) {

    }
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


public void main() {
}




