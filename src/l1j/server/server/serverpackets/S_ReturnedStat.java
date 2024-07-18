package l1j.server.server.serverpackets;

import l1j.server.server.GameServerSetting;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1StatReset;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ReturnedStat extends ServerBasePacket {



	private static final String S_ReturnedStat = "[S] S_ReturnedStat";

	private byte[] _byte = null;
	
	public static final int START = 0x01;

	public static final int LEVELUP = 0x02;

	public static final int END = 0x03;

	public static final int LOGIN = 0x04;

	public static final int PET_PARTY = 0x0c;

	public static final int Unknown_LOGIN2 = 0x44;//問題

	public static final int RING_RUNE_SLOT = 0x43;

	public static final int UI4 = 0x44;

	public static final int UI5 = 0x41;

	public static final int 파워북검색 = 0x019;
	
	public static final int CharNameChange = 29; //29,31,52,
	
//	public static final int NEW_ADDSTAT = 76;
	
	public static final int ELIXER = 0x4c;

	public static final int STATE_1			= 0x0f;

	public static final int BOOKMARK		= 0x2a;
	public static final int CLAN_JOIN_LEAVE	= 0x3c;
	
	public static S_ReturnedStat remainStatusPoint(int remainStatusPoint) {
		S_ReturnedStat pck = new S_ReturnedStat();
		pck.writeC(remainStatusPoint);
		pck.writeD(2);
		return pck;
	}
	
	private S_ReturnedStat() {
		writeC(Opcodes.S_VOICE_CHAT);		
	}
	
	public S_ReturnedStat(int type) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(type);
		switch (type) {
//		case STATE_1:
//			writeD(0x28);
//			writeD(0x00);
//			writeD(0x28);
//			writeD(0x28);
//			writeD(GameServerSetting.TEST);
//			writeD(0x00);
//			writeD(GameServerSetting.TEST);
//			break;
		case UI4:
			writeD(1);
			writeC(12);
			writeH(2240);
			break;
		case UI5:
			writeC(0x00);
			writeC(0x93);
			writeC(0xf6);
			//writeH(45500);
			break;
		default:
			break;
		}
	}
	




	public S_ReturnedStat(L1PcInstance pc, int type) {
		buildPacket(pc, type);
	}


	public S_ReturnedStat(int itemId, boolean eq) {
		// 64 42 dc a4 04 05 09 01 84 d4
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(0x42);
		writeD(itemId);
		writeC(0x09);
		writeC(eq ? 1 : 0);
	}

	public S_ReturnedStat(int subCode, String val) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(subCode);
		switch (subCode) {
		case 파워북검색:
			writeC(0x00);
			writeD(0x2c24a1a6);
			writeD(0x462c2e40);
			writeD(0x10567981);
			writeD(0x72771a38);
			writeS(val);
			break;
		default:
			break;
		}
	}



	public S_ReturnedStat(L1StatReset sr) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(LEVELUP);
		writeC(sr.getNowLevel());
		writeC(sr.getEndLevel());
		writeH(sr.getMaxHp());
		writeH(sr.getMaxMp());
		writeH(sr.getAC());
		writeC(sr.getStr());
		writeC(sr.getIntel());
		writeC(sr.getWis());
		writeC(sr.getDex());
		writeC(sr.getCon());
		writeC(sr.getCha());
	}

	
		public S_ReturnedStat(int type, int count, int id, boolean ck) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(type);
		switch (type) {
		case PET_PARTY:
			if (ck) {
				writeC(count);
				writeC(0x00);
				writeD(0x00);
			} else {
				writeC(count);
				writeC(0x00);
				writeC(0x01);
				writeC(0x00);
				writeC(0x00);
				writeC(0x00);
			}
			writeD(id);
			break;
		default:
			break;
		}
	}

	public static final int SUBTYPE_RING = 1;
	public static final int SUBTYPE_RUNE = 2;
	public static final int OPEN_SLOT_LRING      = 0x04;    // 左邊戒指
	public static final int OPEN_SLOT_RRING      = 0x08;    // 右邊戒指
	public static final int OPEN_SLOT_EARRING    = 0x10;    // 右邊耳環
	public static final int OPEN_SLOT_SHOULD     = 0x40;    // 肩甲 value
	public static final int OPEN_SLOT_BADGE      = 0x80;    // 徽章 value
	public static final int OPEN_SLOT_LRING95	= 0x100; // test
	public static final int OPEN_SLOT_RRING100	= 0x200; // test
	public static final int OPEN_SLOT_LEARRING101	= 0x400; // test
	public static final int OPEN_SLOT_REARRING103	= 0x800; // test
	
	public static final int OPEN_SLOT_ALL		= OPEN_SLOT_LRING | OPEN_SLOT_RRING | OPEN_SLOT_EARRING | OPEN_SLOT_SHOULD | OPEN_SLOT_BADGE | OPEN_SLOT_LRING95 | OPEN_SLOT_RRING100;
	
	public S_ReturnedStat(int type, int subType, int value) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(type);
		
		switch (type) {
		case RING_RUNE_SLOT:
			writeD(subType);
			if (subType == SUBTYPE_RING) { // 戒指 環槽
				if (value == 2)
					value = 15;
				else if (value == 1)
					value = 7;
				else if (value == 0)
					value = 3;
				writeC(value);
			} else if (subType == SUBTYPE_RUNE) { // 符文槽
				writeC(1); // 1~3
			}
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeH(0x00);
			break;
		case Unknown_LOGIN2:
			writeD(0x01);
			writeC(OPEN_SLOT_ALL);
			writeH(0);
			break;
		}
	}

	/** 
	 * 與氏族相關的數據包徽章設定
	 ***/
	public S_ReturnedStat(int pcObjId, int emblemId) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(0x3c);
		writeD(pcObjId);
		writeD(emblemId);
//		writeH(0x00);
	}

	/*public S_ReturnedStat(L1PcInstance pc, int type, String action) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(type);
		if ("logintoserver_1".equalsIgnoreCase(action)) {
			writeByte(new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00 });
		}
		if ("logintoserver_2".equalsIgnoreCase(action)) {
			writeByte(new byte[] { (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00 });
		}
		if ("logintoserver_3".equalsIgnoreCase(action)) {
			writeByte(new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1c, (byte) 0xd6,
					(byte) 0x3b });
		}

		if ("logintoserver_5".equalsIgnoreCase(action)) {
			writeByte(new byte[] { (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0xe8, (byte) 0xa6, (byte) 0xe0, (byte) 0x53, (byte) 0xbc,
					(byte) 0x3e, (byte) 0x58, (byte) 0x00, (byte) 0x05, (byte) 0x03, (byte) 0x2e, (byte) 0xb1,
					(byte) 0xd9, (byte) 0xba, (byte) 0xb8, (byte) 0xc1, (byte) 0xd8, (byte) 0xb0, (byte) 0xc7,
					(byte) 0xba, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0b, (byte) 0x05,
					(byte) 0xe3, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xac, (byte) 0xa6, (byte) 0xe0, (byte) 0x53,
					(byte) 0xbc, (byte) 0x3e, (byte) 0x58, (byte) 0x00, (byte) 0x05, (byte) 0x03, (byte) 0x2e,
					(byte) 0xc8, (byte) 0xc6, (byte) 0xbc, (byte) 0xf6, (byte) 0xbf, (byte) 0xc0, (byte) 0x00,
					(byte) 0xdb, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x23,
					(byte) 0x05, (byte) 0xe3, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3c, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x3d, (byte) 0xed });
		}
	}*/
	  public S_ReturnedStat(L1PcInstance pc, int type, int c) {//補充使用靈藥
	        writeC(Opcodes.S_VOICE_CHAT);
	        writeC(type);
			switch (type) {
			case END:
				writeC(c);
	        	if(pc.getLevel() > pc.getHighLevel()){
	    			pc.getNetConnection().kick();
//	    			pc.getNetConnection().close();
	    		}
				break;
			case ELIXER:
				writeC(c);
				break;
			default:
				break;
			}
	    }

	private void buildPacket(L1PcInstance pc, int type) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(type);

		switch (type) {
		case START:
			short init_hp = 0;
			short init_mp = 0;
			if (pc.isCrown()) { // CROWN
				init_hp = 14;
				switch (pc.getAbility().getBaseWis()) {
				case 11:
					init_mp = 2;
					break;
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 3;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 4;
					break;
				default:
					init_mp = 2;
					break;
				}
			} else if (pc.isKnight()) { // KNIGHT
				init_hp = 16;
				switch (pc.getAbility().getBaseWis()) {
				case 9:
				case 10:
				case 11:
					init_mp = 1;
					break;
				case 12:
				case 13:
					init_mp = 2;
					break;
				default:
					init_mp = 1;
					break;
				}
			} else if (pc.isElf()) { // ELF
				init_hp = 15;
				switch (pc.getAbility().getBaseWis()) {
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 4;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 4;
					break;
				}
			} else if (pc.isWizard()) { // WIZ
				init_hp = 12;
				switch (pc.getAbility().getBaseWis()) {
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 6;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 8;
					break;
				default:
					init_mp = 6;
					break;
				}
			} else if (pc.isDarkelf()) { // DE
				init_hp = 12;
				switch (pc.getAbility().getBaseWis()) {
				case 10:
				case 11:
					init_mp = 3;
					break;
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 4;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 3;
					break;
				}
			} else if (pc.isDragonknight()) { // 龍騎士
				init_hp = 16;
				init_mp = 2;
			} else if (pc.isBlackwizard()) { // 幻術師
				init_hp = 14;
				switch (pc.getAbility().getBaseWis()) {
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 5;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 5;
					break;
				}
			} else if (pc.is戰士()) {
				init_hp = 16;
				if (pc.getAbility().getBaseCon() >= 19)
					init_hp += 2;
				else if (pc.getAbility().getBaseCon() >= 17)
					init_hp += 1;
				switch (pc.getAbility().getBaseWis()) {
				case 9:
				case 10:
				case 11:
					init_mp = 1;
					break;
				case 12:
				case 13:
					init_mp = 2;
					break;
				default:
					init_mp = 1;
					break;
				}
			} else if (pc.isFencer()) {
				init_hp = 16;
				switch (pc.getAbility().getBaseWis()) {
				case 11:
					init_mp = 2;
					break;
				case 12:
				case 13:
					init_mp = 3;
					break;
				case 14:
				case 15:
					init_mp = 4;
					break;
				default:
					init_mp = 2;
					break;
				}
			} else if (pc.isLancer()) {
				init_hp = 18;
				switch (pc.getAbility().getBaseWis()) {
				case 11:
					init_mp = 2;
					break;
				case 12:
				case 13:
					init_mp = 3;
					break;
				case 14:
				case 15:
					init_mp = 4;
					break;
				default:
					init_mp = 2;
					break;
				}
			}
			writeH(init_hp);
			writeH(init_mp);
			writeC(10);
			writeC(ExpTable.getLevelByExp(pc.getReturnStat()));
			//writeC(0x05);
			break;
		case LEVELUP:
			writeC(pc.rst.level);
			writeC(ExpTable.getLevelByExp(pc.getReturnStat()));
			writeH(pc.rst.baseHp + pc.rst.upHp);
			writeH(pc.rst.baseMp + pc.rst.upMp);
			writeH(pc.rst.ac);
			writeC(pc.rst.str + pc.rst.basestr);
			writeC(pc.rst.Int + pc.rst.baseint);
			writeC(pc.rst.wis + pc.rst.basewis);
			writeC(pc.rst.dex + pc.rst.basedex);
			writeC(pc.rst.con + pc.rst.basecon);
			writeC(pc.rst.cha + pc.rst.basecha);
			break;
		case END:
			writeC(pc.getElixirStats());
			//writeC(0);
			break;
//		case NEW_ADDSTAT:
//			writeC(1);
//			break;
		case LOGIN:
			/*
			 * pc.getAblilyty 返回的最小屬性值數組順序：
			 * 0: 力量 / 1: 敏捷 / 2: 體質 / 3: 精神 / 4: 魅力 / 5: 智力
			 */
				/*writeC(0x07);
				writeC(0x00);
				writeC(0x02);
				writeC(0x00);*/
			
			int minStat[] = new int[6];
			minStat = pc.getAbility().getMinStat(pc.getClassId());
			int first = minStat[0] + minStat[5] * 16;
			int second = minStat[3] + minStat[1] * 16;
			int third = minStat[2] + minStat[4] * 16;
			// System.out.println(first + "--" + second + "--" + third );
			writeC(first); // int,str
			writeC(second); // dex,wis
			writeC(third); // cha,con
			writeC(0x00);
			break;
		case UI4:
			writeD(1);
			writeC(12);
			writeH(2240);
			break;
		case UI5:
			writeC(0);
			writeH(45500);
			break;
		case CharNameChange:
			writeC(0);
			break;
		case CLAN_JOIN_LEAVE:
			writeD(pc.getId());
			writeD(pc.getClanid());
			writeH(0x00);
			break;
		default:
			break;
		}
	}

	public S_ReturnedStat(L1PcInstance pc, int c, boolean f) {// 補充使用靈藥
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(END);
		writeC(c);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_ReturnedStat;
	}
}