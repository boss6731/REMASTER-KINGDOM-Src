package l1j.server.server.serverpackets;

import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.CalcStat;

public class S_CharStat extends ServerBasePacket {
	private static final String S_CharStat = "[S] S_CharCreateSetting";

	public static final int Str = 1;
	public static final int Int = 2;
	public static final int Wis = 3;
	public static final int Dex = 4;
	public static final int Con = 5;
	public static final int Cha = 6;

	public static final int Stat_Str = 0x30;
	public static final int Stat_Int = 0x38;
	public static final int Stat_Wis = 0x40;
	public static final int Stat_Dex = 0x48;
	public static final int Stat_Con = 0x50;
	public static final int Stat_Cha = 0x58;

	public static final int STAT_REFRESH = 0xea;
	public static final int STAT_VIEW = 0xe7;
	private static final byte[] MINUS_BYTES_1	= { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x01 };
	private static final byte[] MINUS_BYTES_2	= { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x01 };


	public S_CharStat(GameClient client, int type, int classType, int subType, int s, int i, int w, int d, int c, int cha) {
		int value = 0;
		value = subType * 2;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0xe3);
		writeC(0x01);
		writeC(0x08);
		writeC(value);
		switch (type) {
		case Str:
			int dmgup = CalcStat.calcDmgup(s);
			int hipup = CalcStat.calcHitup(s);
			int critical = CalcStat.calcDmgCritical(classType, s);
			int strweight = 0;
			if (client.getActiveChar() == null) {
				strweight = CalcStat.getMaxWeight(client.charStat[0], client.charStat[4]);
			} else {
				if (value == 0x20) {
					strweight = CalcStat.getMaxWeight(client.charStat[0], client.getActiveChar().getAbility().getTotalCon());
				} else {
					strweight = CalcStat.getMaxWeight(client.getActiveChar().getAbility().getTotalStr(), client.getActiveChar().getAbility().getTotalCon());
				}
			}
			int strsize = size7B(dmgup) + size7B(hipup) + size7B(critical) + size7B(strweight) + 4;
			writeC(0x12);
			writeC(strsize);
			writeC(0x08);
			write7B(dmgup);
			writeC(0x10);
			write7B(hipup);
			writeC(0x18);
			write7B(critical);
			writeC(0x20);
			write7B(strweight);
			break;
		case Int:
			int magicdmg = CalcStat.calcMagicDmg(i);
			int magichit = CalcStat.calcMagicHitUp(i);
			int magiccritical = CalcStat.calcMagicCritical(i);
			int magicbonus = CalcStat.calcMagicBonus(/*classType,*/ i);
			int magicdecreasemp = CalcStat.calcDecreaseMp(i);
			int intsize = size7B(magicdmg) + size7B(magichit) + size7B(magiccritical) + size7B(magicbonus) + size7B(magicdecreasemp) + 5;
			writeC(0x1a);
			writeC(intsize);
			writeC(0x08);
			write7B(magicdmg);
			writeC(0x10);
			write7B(magichit);
			writeC(0x18);
			write7B(magiccritical);
			writeC(0x20);
			write7B(magicbonus);
			writeC(0x28);
			write7B(magicdecreasemp);
			break;
		case Wis:
			int mpr = CalcStat.calcMpr(w);
			int mprpotion = CalcStat.calcMprPotion(w);
			int statmr = CalcStat.calcStatMr(classType, w);
			int minmp = CalcStat.MinincreaseMp(classType, w);
			int maxmp = CalcStat.MaxincreaseMp(classType, w);
			int baseMaxMp = client.getActiveChar() == null ? 0 : client.getActiveChar().getBaseMaxMp();
			int wissize = size7B(mpr) + size7B(mprpotion) + size7B(statmr) + size7B(minmp) + size7B(maxmp) + size7B(baseMaxMp) + 8;
			writeC(0x22);
			writeC(wissize);
			writeC(0x08);
			write7B(mpr);
			writeC(0x10);
			write7B(mprpotion);
			writeC(0x18);
			write7B(statmr);
			writeC(0x20);
			write7B(minmp);
			writeC(0x28);
			write7B(maxmp);
			writeC(0x30);
			writeC(0);
			writeC(0x38);
			writeBit(baseMaxMp);
			break;
		case Dex:
			int bowdmg = CalcStat.calcBowDmgup(d);
			int bowhitup = CalcStat.calcBowHitup(d);
			int bowcritical = CalcStat.calcBowCritical(d);
			int dexac = CalcStat.calcAc(d);
			int dexer = CalcStat.ER(d);
			int dexsize = size7B(bowdmg) + size7B(bowhitup) + size7B(bowcritical) + size7B(dexac) + size7B(dexer) + 5;
			writeC(0x2a);
			writeC(dexsize);
			writeC(0x08);
			write7B(bowdmg);
			writeC(0x10);
			write7B(bowhitup);
			writeC(0x18);
			write7B(bowcritical);
			writeC(0x20);
			write7B(dexac);
			writeC(0x28);
			write7B(dexer);
			break;
		case Con:
			int hpr = CalcStat.calcHpr(c);
			int hprpotion = (int)CalcStat.calcHprPotion(c);
			int conweight = 0;
			if (client.getActiveChar() == null) {
				conweight = CalcStat.getMaxWeight(client.charStat[0], client.charStat[4]);
			} else {
				if (value == 0x20) {
					conweight = CalcStat.getMaxWeight(client.getActiveChar().getAbility().getTotalStr(), client.charStat[4]);
				} else {
					conweight = CalcStat.getMaxWeight(client.getActiveChar().getAbility().getTotalStr(), client.getActiveChar().getAbility().getTotalCon());
				}
			}
			int purehp = CalcStat.PureHp(classType, c);
			int baseHp = client.getActiveChar() == null ? 0 : client.getActiveChar().getBaseMaxHp();
			int consize = size7B(hpr) + size7B(hprpotion) + size7B(conweight) + size7B(purehp) + size7B(baseHp) + 8;
			writeC(0x32);
			writeC(consize);
			writeC(0x08);
			write7B(hpr);
			writeC(0x10);
			write7B(hprpotion);
			writeC(0x18);
			write7B(conweight);
			writeC(0x20);
			write7B(purehp);
			writeC(0x28);
			writeC(0);
			writeC(0x30);
			writeBit(baseHp);
			break;
		case Cha:
			int pierceall = CalcStat.calcPurePierceAll(cha);
			int decreasecooltime = CalcStat.calcPureDecreaseCoolTime(cha);
			int decreaseccduration = CalcStat.calcPureDecreaseCCDuration(cha);
			int chasize = size7B(pierceall) + size7B(decreasecooltime) + size7B(decreaseccduration) + 6;
			writeC(0x3a);
			writeC(chasize);
			writeC(0x08);
			writeC(0x01);
			writeC(0x10);
			write7B(pierceall);
//			writeBit(pierceall);
			writeC(0x18);
			write7B(decreasecooltime);
			writeC(0x20);
			write7B(decreaseccduration);
			break;
		default:
			break;
		}
		writeH(0x00);
	}

	public S_CharStat(L1PcInstance pc, int settingType, int statType) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0xe3);
		writeH(0x0801);
		writeC(settingType);
		int ChaStr = pc.getAbility().getTotalStr();
		int ChaInt = pc.getAbility().getTotalInt();
		int ChaWis = pc.getAbility().getTotalWis();
		int ChaDex = pc.getAbility().getTotalDex();
		int ChaCon = pc.getAbility().getTotalCon();
		int ChaCha = pc.getAbility().getTotalCon();
		int CalcWeight = CalcStat.getMaxWeight(pc.getAbility().getTotalStr(), pc.getAbility().getTotalCon());
		switch (statType) {
		case Stat_Str:
			int calcDmg = CalcStat.calcDmgup(ChaStr);
			int calcHit = CalcStat.calcHitup(ChaStr);
			int calcCritical = CalcStat.calcDmgCritical(pc.getClassNumber(), ChaStr);
//			int calcCritical = CalcStat.calcDmgCritical(ChaStr) + (int)(CalcStat.calcPureMeleeCritical(pc.getAbility().getStr())* 100);
			int StatStrsize = size7B(calcDmg) + size7B(calcHit) + size7B(calcCritical) + size7B(CalcWeight) + 4;
			writeC(0x12);
			writeC(StatStrsize);
			writeC(0x08);
			write7B(calcDmg);
			writeC(0x10);
			write7B(calcHit);
			writeC(0x18);
			write7B(calcCritical);
			writeC(0x20);
			write7B(CalcWeight);
			break;
		case Stat_Int:
			int calcMagicDmg = CalcStat.calcMagicDmg(ChaInt);
			int calcMagicHit = CalcStat.calcMagicHitUp(ChaInt);
			int calcMagicCri = CalcStat.calcMagicCritical(ChaInt);
			int calcMagicBonus = CalcStat.calcMagicBonus(/*pc.getType(),*/ ChaInt);
			int calcMagicDecmp = CalcStat.calcDecreaseMp(ChaInt);
			int StatIntsize = size7B(calcMagicDmg) + size7B(calcMagicHit) + size7B(calcMagicCri) + size7B(calcMagicBonus) + size7B(calcMagicDecmp) + 5;
			writeC(0x1a);
			writeC(StatIntsize);
			writeC(0x08);
			write7B(calcMagicDmg);
			writeC(0x10);
			write7B(calcMagicHit);
			writeC(0x18);
			write7B(calcMagicCri);
			writeC(0x20);
			write7B(calcMagicBonus);
			writeC(0x28);
			write7B(calcMagicDecmp);
			break;
		case Stat_Wis:
			int calcMpr = CalcStat.calcMpr(ChaWis);
			int calcMprpotion = CalcStat.calcMprPotion(ChaWis);
			int calcstatMr = CalcStat.calcStatMr(pc.getType(), ChaWis);
			int calcMinmp = CalcStat.MinincreaseMp(pc.getType(), ChaWis);
			int calcMaxmp = CalcStat.MaxincreaseMp(pc.getType(), ChaWis);
			int baseMaxMp = pc.getBaseMaxMp();
			int StatWissize = size7B(calcMpr) + size7B(calcMprpotion) + size7B(calcstatMr) + size7B(calcMinmp) + size7B(calcMaxmp) + size7B(baseMaxMp) + 8;
			writeC(0x22);
			writeC(StatWissize);
			writeC(0x08);
			write7B(calcMpr);
			writeC(0x10);
			write7B(calcMprpotion);
			writeC(0x18);
			write7B(calcstatMr);
			writeC(0x20);
			write7B(calcMinmp);
			writeC(0x28);
			write7B(calcMaxmp);
			writeC(0x30);
			writeC(0);
			writeC(0x38);
			writeBit(baseMaxMp);
			break;
		case Stat_Dex:
			int calcBowDmg = CalcStat.calcBowDmgup(ChaDex);
			int calcBowHit = CalcStat.calcBowHitup(ChaDex);
			int calcBowCri = CalcStat.calcBowCritical(ChaDex);
			int calcDexAc = CalcStat.calcAc(ChaDex);
			int calcDexEr = CalcStat.ER(ChaDex);
			int StatDexsize = size7B(calcBowDmg) + size7B(calcBowHit) + size7B(calcBowCri) + size7B(calcDexAc) + size7B(calcDexEr) + 5;
			writeC(0x2a);
			writeC(StatDexsize);
			writeC(0x08);
			write7B(calcBowDmg);
			writeC(0x10);
			write7B(calcBowHit);
			writeC(0x18);
			write7B(calcBowCri);
			writeC(0x20);
			write7B(calcDexAc);
			writeC(0x28);
			write7B(calcDexEr);
			break;
		case Stat_Con:
			int calcHpr = CalcStat.calcHpr(ChaCon);
			int calcHprpotion = (int)CalcStat.calcHprPotion(ChaCon);			
			int calcPurehp = CalcStat.PureHp(pc.getType(), ChaCon);
			int baseHp = pc.getBaseMaxHp();
			int StatConsize = size7B(calcHpr) + size7B(calcHprpotion) + size7B(CalcWeight) + size7B(calcPurehp) + size7B(baseHp) + 8;
			writeC(0x32);
			writeC(StatConsize);
			writeC(0x08);
			write7B(calcHpr);
			writeC(0x10);
			write7B(calcHprpotion);
			writeC(0x18);
			write7B(CalcWeight);
			writeC(0x20);
			write7B(calcPurehp);
			writeC(0x28);
			writeC(0);
			writeC(0x30);
			writeBit(baseHp);
			break;
		case Stat_Cha:
			int pierceall = CalcStat.calcPurePierceAll(ChaCha);
			int decreasecooltime = CalcStat.calcPureDecreaseCoolTime(ChaCha);
			int decreaseccduration = CalcStat.calcPureDecreaseCCDuration(ChaCha);
			int StatChasize = size7B(pierceall) + size7B(decreasecooltime) + size7B(decreaseccduration) + 5;
			writeC(0x3a);
			writeC(StatChasize);
			writeC(0x08);
			writeC(0x01);
			writeC(0x10);
			write7B(pierceall);
//			writeBit(pierceall);
			writeC(0x18);
			write7B(decreasecooltime);
			writeC(0x20);
			write7B(decreaseccduration);
			break;
		default:
			break;
		}


		writeH(0);
	}

	/** 純屬性刷新 **/
	public S_CharStat(L1PcInstance pc, int code) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF); // 寫入操作碼，表示擴展的 Protobuf
		writeC(code); // 寫入傳入的代碼

		switch (code) {
			case STAT_REFRESH:
				writeC(0x01); // 寫入固定值 0x01
				writeC(0x08); // 寫入固定值 0x08
				writeC(pc.getAbility().getStr()); // 寫入角色的力量屬性
				writeC(0x10); // 寫入固定值 0x10
				writeC(pc.getAbility().getInt()); // 寫入角色的智力屬性
				writeC(0x18); // 寫入固定值 0x18
				writeC(pc.getAbility().getWis()); // 寫入角色的智慧屬性
				writeC(0x20); // 寫入固定值 0x20
				writeC(pc.getAbility().getDex()); // 寫入角色的敏捷屬性
				writeC(0x28); // 寫入固定值 0x28
				writeC(pc.getAbility().getCon()); // 寫入角色的體質屬性
				writeC(0x30); // 寫入固定值 0x30
				writeC(pc.getAbility().getCha()); // 寫入角色的魅力屬性
				writeH(0); // 寫入保留值 0
				break;
		}
	}
	// TODO: 屬性加成效果（當鼠標懸停時）
	public S_CharStat(int code, int value) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF); // 寫入操作碼，表示擴展的 Protobuf
		writeH(code); // 寫入傳入的代碼（2 字節）
		// writeC(code); // 註釋掉的單字節寫入代碼
		switch (code) {
			case STAT_VIEW:
				if (value == 12) {
					writeC(0x0a); // 力量
					writeC(0x0b);
					writeC(0x08);
					writeByte(MINUS_BYTES_2); // 寫入 MINUS_BYTES_2 的值
					writeC(0x12); // 智力
					writeC(0x0b);
					writeC(0x08);
					writeByte(MINUS_BYTES_2);
					writeC(0x1a); // 智慧
					writeC(0x0b);
					writeC(0x08);
					writeByte(MINUS_BYTES_2);
					writeC(0x22); // 敏捷
					writeC(0x0b);
					writeC(0x08);
					writeByte(MINUS_BYTES_2);
					writeC(0x2a); // 體質
					writeC(0x0b);
					writeC(0x08);
					writeByte(MINUS_BYTES_2);
					writeC(0x32); // 魅力
					writeC(0x08);
					writeC(0x08);
					writeC(value);
					writeC(0x10);
					writeC(0x01);
					writeC(0x18);
					writeC(0x64);
					writeC(0x20);
					writeC(0x64);
				} else {
					// 近戰屬性
					int shortDmg = value == 60 ? 5 : value == 55 ? 5 : value == 45 ? 3 : 1;
					int shortHit = value == 60 ? 5 : value == 55 ? 5 : value == 45 ? 3 : 1;
					int shortCri = value == 60 ? 5 : value == 55 ? 2 : value == 45 ? 1 : 0;
					int strLength = 3 + getBitSize(value) + getBitSize(shortDmg) + getBitSize(shortHit) + (shortCri > 0 ? getBitSize(shortCri) + 1 : 0);
					writeC(0x0a);
					writeC(strLength);
					writeC(0x08);
					writeC(value); // 等級
					writeC(0x10);
					writeC(shortDmg); // 近戰傷害
					writeC(0x18);
					writeC(shortHit); // 近戰命中
					if (shortCri > 0) {
						writeC(0x20);
						writeC(shortCri); // 近戰暴擊
					}

					// 魔法屬性
					int magicDmg = value == 60 ? 5 : value == 55 ? 5 : value == 45 ? 3 : 1;
					int magicHit = value == 60 ? 5 : value == 55 ? 5 : value == 45 ? 3 : 1;
					int magicCri = value == 60 ? 5 : value == 55 ? 2 : value == 45 ? 1 : 0;
					int intLength = 3 + getBitSize(value) + getBitSize(magicDmg) + getBitSize(magicHit) + (magicCri > 0 ? getBitSize(magicCri) + 1 : 0);
					writeC(0x12);
					writeC(intLength);
					writeC(0x08);
					writeC(value); // 等級
					writeC(0x10);
					writeC(magicDmg); // 魔法傷害
					writeC(0x18);
					writeC(magicHit); // 魔法命中
					if (magicCri > 0) {
						writeC(0x20);
						writeC(magicCri); // 魔法暴擊
					}

					// 智慧屬性
					int mpr = value == 60 ? 5 : value == 55 ? 5 : value == 45 ? 3 : 1;
					int mpBonus = value == 60 ? 5 : value == 55 ? 5 : value == 45 ? 3 : 1;
					int mp = value == 60 ? 5 : value == 55 ? 200 : value == 45 ? 150 : value == 35 ? 100 : 50;
					int wisLength = 4 + getBitSize(value) + getBitSize(mpr) + getBitSize(mpBonus) + getBitSize(mp);
					writeC(0x1a);
					writeC(wisLength);
					writeC(0x08);
					writeC(value); // 等級
					writeC(0x10);
					writeC(mpr); // MP 恢复
					writeC(0x18);
					writeC(mpBonus); // MP 恢复加成
					writeC(0x38);
					writeBit(mp); // MP 50, 100, 150, 200

					// 敏捷屬性
					int longDmg = value == 60 ? 5 : value == 55 ? 5 : value == 45 ? 3 : 1;
					int longHit = value == 60 ? 5 : value == 55 ? 5 : value == 45 ? 3 : 1;
					int longCri = value == 60 ? 5 : value == 55 ? 2 : value == 45 ? 1 : 0;
					int dexLength = 3 + getBitSize(value) + getBitSize(longDmg) + getBitSize(longHit) + (longCri > 0 ? getBitSize(longCri) + 1 : 0);
					writeC(0x22);
					writeC(dexLength);
					writeC(0x08);
					writeC(value); // 等級
					writeC(0x10);
					writeC(longDmg); // 遠程傷害
					writeC(0x18);
					writeC(longHit); // 遠程命中
					if (longCri > 0) {
						writeC(0x20);
						writeC(longCri); // 遠程暴擊
					}
				}
				break;
		}
	}
				
				// 體質
				int hpr			= value == 60 ? 5 : value == 55 ? 5 : value == 45 ? 3 : 1;
				int hpBonus		= value == 60 ? 4 : value == 55 ? 4 : value == 45 ? 2 : value == 35 ? 1 : 0;
				int hp			= value == 60 ? 200 : value == 55 ? 200 : value == 45 ? 150 : value == 35 ? 100 : 50;
				int conLength	= 3 + getBitSize(value) + getBitSize(hpr) + getBitSize(hp) + (hpBonus > 0 ? getBitSize(hpBonus) + 1 : 0);
				writeC(0x2a);
				writeC(conLength);
				writeC(0x08);
				writeC(value);// 等級
				writeC(0x10);
				writeC(hpr);// 皮克蒂克
				if(hpBonus > 0){
					writeC(0x18);
					writeC(hpBonus);// 增加藥水恢復量
				}
				writeC(0x30);
				writeBit(hp);// 血50 100 150 200
				
				// 카리
				int allHit		= value == 60 ? 1 : value == 55 ? 1 : value == 45 ? 1 : value == 35 ? 1 : 1; 
				int coolTime	= value == 60 ? 100 : value == 25 ? 100 : 100;
				int duration	= value == 60 ? 100 : value == 25 ? 100 : 100;
				int chaLength	= 1 + getBitSize(value) + (allHit > 0 ? getBitSize(allHit) + 1 : 0) + (coolTime > 0 ? getBitSize(coolTime) + 1 : 0) + (duration > 0 ? getBitSize(duration) + 1 : 0);
				writeC(0x32);
				writeC(chaLength);
				writeC(0x08);
				writeC(value);
				if(allHit > 0){
					writeC(0x10);
					writeC(allHit);
				}
				if(coolTime > 0){
					writeC(0x18);
					writeBit(coolTime);
				}
				if(duration > 0){
					writeC(0x20);
					writeBit(duration);
				}
			}
			break;
		}
		writeH(0x00);
				


/*			writeC(1);
			int size = 0;
			int[] bonusArray = null;
			switch (value) { // 대미지, 명중, 치명타 , 무게
			case 25: 
				bonusArray = new int[] { 1, 1, 0, 0 };
				break;
			case 35:
				bonusArray = new int[] { 1, 1, 0, 0 };
				break;
			case 45:
				bonusArray = new int[] { 3, 3, 1, 0 };
				break;
			case 55:
				bonusArray = new int[] { 5, 5, 2, 0 };
				break;
			case 60:
				bonusArray = new int[] { 5, 5, 2, 0 };
				break;
			}

			size = getBitSize(value) + 1;
			for (int bonus : bonusArray) {
				if (bonus != 0) {
					size += getBitSize(bonus) + 1;
				}
			}
			writeC(10); // 힘
			writeC(size);
			writeC(8);
			writeBit(value);
			for (int i = 0; i < bonusArray.length; i++) {
				int bonus = bonusArray[i];
				if (bonus != 0) {
					writeC((i + 2) * 8);
					writeBit(bonusArray[i]);
				}

			}

			switch (value) { // 인트 효과
			case 25:
				bonusArray = new int[] { 1, 1, 0, 0, 0 };
				break;
			case 35:
				bonusArray = new int[] { 1, 1, 0, 0, 0 };
				break;
			case 45:
				bonusArray = new int[] { 3, 3, 1, 0, 0 };
				break;
			case 55:
				bonusArray = new int[] { 5, 5, 2, 0, 0 };
				break;
			case 60:
				bonusArray = new int[] { 5, 5, 2, 0, 0 };
				break;
			}

			size = getBitSize(value) + 1;
			for (int bonus : bonusArray) {
				if (bonus != 0) {
					size += getBitSize(bonus) + 1;
				}
			}
			writeC(18); // 인트
			writeC(size);
			writeC(8);
			writeBit(value);
			for (int i = 0; i < bonusArray.length; i++) {
				int bonus = bonusArray[i];
				if (bonus != 0) {
					writeC((i + 2) * 8);
					writeBit(bonusArray[i]);
				}

			}

			switch (value) { // 위즈 효과
			case 25:
				bonusArray = new int[] { 1, 1, 0, 0, 0, 50 };
				break;
			case 35:
				bonusArray = new int[] { 1, 1, 0, 0, 0, 100 };
				break;
			case 45:
				bonusArray = new int[] { 3, 3, 0, 0, 0, 150 };
				break;
			case 55:
				bonusArray = new int[] { 5, 5, 0, 0, 0, 200 };
				break;
			case 60:
				bonusArray = new int[] { 5, 5, 0, 0, 0, 200 };
				break;
			}

			size = getBitSize(value) + 1;
			for (int bonus : bonusArray) {
				if (bonus != 0) {
					size += getBitSize(bonus) + 1;
				}
			}
			writeC(26); // 위즈
			writeC(size);
			writeC(8);
			writeBit(value);
			for (int i = 0; i < bonusArray.length; i++) {
				int bonus = bonusArray[i];
				if (bonus != 0) {
					writeC((i + 2) * 8);
					writeBit(bonusArray[i]);
				}

			}

			switch (value) { // 덱스 효과
			case 25:
				bonusArray = new int[] { 1, 1, 0, 0, 0 };
				break;
			case 35:
				bonusArray = new int[] { 1, 1, 0, 0, 0 };
				break;
			case 45:
				bonusArray = new int[] { 3, 3, 1, 0, 0 };
				break;
			case 55:
				bonusArray = new int[] { 5, 5, 2, 0, 0 };
				break;
			case 60:
				bonusArray = new int[] { 5, 5, 2, 0, 0 };
				break;
			}

			size = getBitSize(value) + 1;
			for (int bonus : bonusArray) {
				if (bonus != 0) {
					size += getBitSize(bonus) + 1;
				}
			}
			writeC(34); // 덱스
			writeC(size);
			writeC(8);
			writeBit(value);
			for (int i = 0; i < bonusArray.length; i++) {
				int bonus = bonusArray[i];
				if (bonus != 0) {
					writeC((i + 2) * 8);
					writeBit(bonusArray[i]);
				}
			}
 
			switch (value) { // 콘 효과
			case 25:
				bonusArray = new int[] { 1, 0, 0, 0, 50 };
				break;
			case 35:
				bonusArray = new int[] { 1, 1, 0, 0, 100 };
				break;
			case 45:
				bonusArray = new int[] { 3, 3, 0, 0, 150 };
				break;
			case 55:
				bonusArray = new int[] { 5, 5, 0, 0, 200 };
				break;
			case 60:
				bonusArray = new int[] { 5, 5, 0, 0, 200 };
				break;
			}

			size = getBitSize(value) + 1;
			for (int bonus : bonusArray) {
				if (bonus != 0) {
					size += getBitSize(bonus) + 1;
				}
			}
			writeC(42); // 콘
			writeC(size);
			writeC(8);
			writeBit(value);
			for (int i = 0; i < bonusArray.length; i++) {
				int bonus = bonusArray[i];
				if (bonus != 0) {
					writeC((i + 2) * 8);
					writeBit(bonusArray[i]);
				}

			}
			
			switch (value) { // 카리 효과
			case 12:
				bonusArray = new int[] { 1, 100, 100 };
				break;
			case 25:
				bonusArray = new int[] { 0, 100, 100 };
				break;
			case 35:
				bonusArray = new int[] { 1, 0, 0 };
				break;
			case 45:
				bonusArray = new int[] { 1, 0, 0 };
				break;
			case 55:
				bonusArray = new int[] { 1, 0, 0 };
				break;
			case 60:
				bonusArray = new int[] { 1, 100, 100 };
				break;
			}
			size = getBitSize(value) + 1;
			for (int bonus : bonusArray) {
				if (bonus != 0) {
					size += getBitSize(bonus) + 1;
				}
			}
			writeC(50); // 카리
			writeC(size);
			writeC(8);
			writeC(value);
			for (int i = 0; i < bonusArray.length; i++) {
				int bonus = bonusArray[i];
				if (bonus != 0) {
					writeC((i + 2) * 8);
					writeBit(bonusArray[i]);
				}

			}
			writeH(0);
		
			break;
		}*/
		

	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_CharStat;
	}
}


