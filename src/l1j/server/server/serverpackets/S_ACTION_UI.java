package l1j.server.server.serverpackets;

import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class S_ACTION_UI extends ServerBasePacket {
	public static final int TAM = 0xc2;
	public static final int CLAN_JOIN_MESSAGE = 0x43;
	public static final int TEST = 0xcc;
	public static final int TEST2 = 0x3A;
	public static final int SAFETYZONE = 0xcf;
	public static final int PCBANG_SET = 0x7e;
	public static final int MONSTER_BOOK_WEEK_QUEST = 810;
	public static final int EINHASAD = 1020;
	
	private static final String S_ACTION_UI = "S_ACTION_UI";

	private static final String 測試進行 = "" + "0a e3 01 0a 55 "
			+ "12 10 08 01 10 9a 83 2c 18 00 22 06 08 d8 87 01 10 01 "
			+ "12 11 08 02 10 81 88 6e 18 c6 20 22 06 08 d8 87 01 10 02 "
			+ "12 12 08 03 10 84 a0 b8 03 18 9f 78 22 06 08 d8 87 01 10 05 " + "1a 0b 08 01 10 b5 bf f0 06 18 c1 87 01 "
			+ "1a 0b 08 01 10 84 a0 b8 03 18 c0 87 01 20 01 ";

	public S_ACTION_UI(boolean flag) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		String aa = "3b 01 08 02 10 01 cf 3a";
		StringTokenizer st = new StringTokenizer(aa.toString());
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
	}

	/**
	 * 用於設定隊伍標記。
	 */
	public S_ACTION_UI(byte[] flag) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(339);
		writeByte(flag);
		writeH(0);
	}

	/**
	 * @param 網咖, 安全區域
	 *            設置
	 * @param isOpen
	 *            開啟/關閉
	 **/
	public S_ACTION_UI(int code, boolean isOpen) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(code);
		switch (code) {
			case PCBANG_SET:
				writeC(0x00);
				writeC(0x08); // 網咖效果
				writeC(isOpen ? 1 : 0);
				writeC(0x10); // 排行按鈕
				writeC(0x01);
				break;
			case SAFETYZONE:
				writeC(0x01);
				writeC(0x08);
				writeC(isOpen ? 128 : 0);
				writeC(0x10);
				writeC(0x00);
				writeC(0x18);
				writeC(0x00);
				break;
		}
		writeH(0x00);
	}

	/**
	 * 有關血盟
	 */
	public S_ACTION_UI(String clanname, int rank) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x19);
		writeC(0x02);
		writeC(0x0a);
		writeS2(clanname);
		writeC(0x10);
		writeC(rank); // 血盟等級
		writeH(0x00);
	}

	public S_ACTION_UI(int reward) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(146);
		writeC(0x08);
		writeBit(reward);
		writeH(0x00);
	}

	/**
	 * 為了戰士技能
	 */
	public S_ACTION_UI(int type, int skillnum) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		if (type == 145) { // 登錄
			// b3 91 01 0a 02 08 03 8c c7
			writeC(0x01);
			writeC(0x0a);
			writeC(skillnum != 5 ? 0x02 : 0x04);
			writeC(0x08);
			writeC(skillnum);
			if (skillnum == 5) { // 盔甲守護
				writeC(0x10);
				writeC(0x0a);
			}
			writeH(0xf18d);
		} else if (type == 146) { // 新角色創建時
			// b3 92 01 08 03 c2 33
			writeC(0x01);
			writeC(0x08);
			writeC(skillnum);
			if (skillnum == 5) { // 盔甲守護
				writeC(0x10);
				writeC(0x0a);
			}
		}
	}
			writeH(0x00);
		} else if (type == TAM) {
			writeC(0x01);
			writeC(0x08);
			write4bit(skillnum);
			writeH(0x00);
		}
		// test
		else if (type == TEST) {
			writeC(0x0d);
			write4bit(skillnum);
		} else if (type == CLAN_JOIN_MESSAGE) {
			writeH(0x0801);
			writeC(skillnum);
			writeH(0x00);
		}
	}
	public S_ACTION_UI(int type, L1PcInstance pc){
		// TODO 自動產生的存根建構函數
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch(type){
		case EINHASAD:
		int blessOfAin=pc.getAccount().getBlessOfAin()/10000;
		int bless_efficiency=pc.getBlessOfAinEfficiency()+pc.getBlessAinEfficiency();
		int bless_exp=pc.getBlessOfAinExp();
		int extra=0;
		//		         boolean bloodpledge = false;
		if(blessOfAin<=200){
		bless_exp+=100*100;
		//buff_end(pc);
		}else if(blessOfAin>=201&&blessOfAin<=1800){
		bless_exp+=60*100;
		extra+=40;
		//buff_end(pc);
		//BraveState(pc, 1);
		}else if(blessOfAin>=1801&&blessOfAin<=3400){
		bless_exp+=80*100;
		extra+=50;
		//buff_end(pc);
		//BraveState(pc, 2);
		}else if(blessOfAin>=3401&&blessOfAin<=8000){
		bless_exp+=100*100;
		extra+=60;
		//buff_end(pc);
		//BraveState(pc, 3);
		}

		if(pc.getAinExpBonus()>0){
		extra+=pc.getAinExpBonus()*100;
		}

		writeC(8);
		writeBit(blessOfAin);
		writeC(16);
		writeBit(bless_exp);
		writeC(24);
		writeBit(extra);
		writeC(32);
		writeBit(bless_efficiency);

		/** 日常獎勵 **/
		writeC(0x30);
		writeBit(pc.getNetConnection().getAccount().getEinDayBonus());

		/** 類型 **/
		writeC(0x38);
		// if (blessOfAin < 1/* && pc.網咖_buff */){
		if(blessOfAin< 1&&pc.hasSkillEffect(L1SkillId.EINHASAD_GRACE)){
		writeBit(0x03); // 阿因哈薩德的祝福
		}else{
		writeBit(0x01);
		}

		if((blessOfAin>0&&blessOfAin< 80000000)&&!pc._dragonbless){//龍之祝福 (Dragon Blessing)
		pc.sendPackets(new S_NewSkillIcon(L1SkillId.DRAGON_BLESS,true,-1));
		pc._dragonbless=true;
		}

		writeH(0);
		}
}
@override
public byte[] getContent() {
		return getBytes();
		}

public String getType() {
		return S_ACTION_UI;
		}

		
