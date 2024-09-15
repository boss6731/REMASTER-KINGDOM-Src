package l1j.server.server.server.clientpackets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Logger;


import MJShiftObject.Object.MJShiftObject;
import l1j.server.Config;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJTemplate.MJStringAnalyser;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
import l1j.server.server.BadNamesList;

import l1j.server.server.IdFactory;
import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.server.datatables.SkillsTable;
import l1j.server.server.model.Beginner;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CharCreateStatus;
import l1j.server.server.serverpackets.S_NewCharPacket;
import l1j.server.server.templates.L1Skills;

public class C_CreateChar extends l1j.server.server.clientpackets.ClientBasePacket {
	private static Logger _log = Logger.getLogger(C_CreateChar.class.getName());
	private static final String C_CREATE_CHAR = "[C] C_CreateChar";

	public C_CreateChar(byte[] abyte0, GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance pc = new L1PcInstance();

		String name = readS();
		int class_type = readC();
		int class_sex = readC();
		
		Calendar cal = Calendar.getInstance();
		int hour = Calendar.HOUR;
		int minute = Calendar.MINUTE;
		/** 0 上午 , 1 下午 * */
		String amPm = "下午";
		if (cal.get(Calendar.AM_PM) == 0) {
			amPm = "上午";
		}

		if(Config.Login.UseShiftServer){
			MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_sender_object_from_account(client.getAccountName());
			if(sobject != null){
				System.out.println(String.format("【帳號】%s【參加中】%s【創建】%s【%s】【IP】%s 嘗試在參與對抗戰的帳號中創建角色", client.getAccountName(), sobject.get_source_character_name(), name, MJNSHandler.getLocalTime(), client.getIp()));
				SC_CUSTOM_MSGBOX.do_kick(client, String.format("因 %s 參與對抗戰，無法允許創建角色。", sobject.get_source_character_name()));
				return;
			}
		}

		// TODO 防止漏洞
		if (name.length() > 20) {
			System.out.println("■ 封包攻擊 ID ■ :" + name);
			System.out.println("■ 封包攻擊 IP ■ :" + client.getIp());
			client.kick();
			return;
		}

		MJStringAnalyser analyser = MJStringAnalyser.execute(name);
		for (int i = 0; i < Config.CharSettings.NotCreateClass.length; i++) {
			if (Config.CharSettings.NotCreateClass[i] == class_type) {
				S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(23);
				client.sendPacket(s_charcreatestatus);
				return;
			}
		}
		
		if(analyser.get_special_length() > 0 || analyser.get_invalid_length() > 0){
			S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
			client.sendPacket(s_charcreatestatus);
			return;
		}

		// 沒有輸入韓文且僅輸入英文時，最多允許輸入5個字符。
		if (analyser.get_kor_length() <= 0 && analyser.get_alpha_length() > 5) {
			S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
			client.sendPacket(s_charcreatestatus);
		}

		if (name.length() <= 0) { // 防止一個字符的ID
			// 英文字母每個字符長度為1，韓文字每個字符長度為2
			S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
			client.sendPacket(s_charcreatestatus);
			return;
		}

		if (BadNamesList.getInstance().isBadName(name)) {
			S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
			// _log.info("創建禁止的角色名稱，創建失敗");
			client.sendPacket(s_charcreatestatus);
			return;
		}
		if (MJBotNameLoader.isAlreadyName(name)) {
			_log.fine("charname: " + pc.getName() + " already exists. creation failed.");
			S_CharCreateStatus s_charcreatestatus1 = new S_CharCreateStatus(S_CharCreateStatus.REASON_ALREADY_EXSISTS);
			client.sendPacket(s_charcreatestatus1);
			return;
		}
		if (isInvalidName(name)) {
			S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
			client.sendPacket(s_charcreatestatus);
			return;
		}

		if (CharacterTable.getInstance().isContainNameList(name) || L1World.getInstance().getPlayer(name) != null) {
			_log.fine("charname: " + pc.getName() + " already exists. creation failed.");
			S_CharCreateStatus s_charcreatestatus1 = new S_CharCreateStatus(S_CharCreateStatus.REASON_ALREADY_EXSISTS);
			client.sendPacket(s_charcreatestatus1);
			return;
		}

		if (client.getAccount().countCharacters() >= 10) {
			// _log.fine("account: " + client.getAccountName() + " 請求創建超過8個角色。");
			S_CharCreateStatus s_charcreatestatus1 = new S_CharCreateStatus(S_CharCreateStatus.REASON_WRONG_AMOUNT);
			client.sendPacket(s_charcreatestatus1);
			return;
		}

		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == 'ㄱ' || name.charAt(i) == 'ㄲ' || name.charAt(i) == 'ㄴ' || name.charAt(i) == 'ㄷ' || // 單字元(char)單位
																													// 比較.
					name.charAt(i) == 'ㄸ' || name.charAt(i) == 'ㄹ' || name.charAt(i) == 'ㅁ' || name.charAt(i) == 'ㅂ' || // 單字元(char)單位
																														// 比較
					name.charAt(i) == 'ㅃ' || name.charAt(i) == 'ㅅ' || name.charAt(i) == 'ㅆ' || name.charAt(i) == 'ㅇ' || // 單字元(char)單位
																														// 比較
					name.charAt(i) == 'ㅈ' || name.charAt(i) == 'ㅉ' || name.charAt(i) == 'ㅊ' || name.charAt(i) == 'ㅋ' || // 單字元(char)單位
																														// 比較.
					name.charAt(i) == 'ㅌ' || name.charAt(i) == 'ㅍ' || name.charAt(i) == 'ㅎ' || name.charAt(i) == 'ㅛ' || // 單字元(char)單位
																														// 比較.
					name.charAt(i) == 'ㅕ' || name.charAt(i) == 'ㅑ' || name.charAt(i) == 'ㅐ' || name.charAt(i) == 'ㅔ' || // 單字元(char)單位
																														// 比較.
					name.charAt(i) == 'ㅗ' || name.charAt(i) == 'ㅓ' || name.charAt(i) == 'ㅏ' || name.charAt(i) == 'ㅣ' || // 單字元(char)單位
																														// 比較.
					name.charAt(i) == 'ㅠ' || name.charAt(i) == 'ㅜ' || name.charAt(i) == 'ㅡ' || name.charAt(i) == 'ㅒ' || // 單字元(char)單位
																														// 比較.
					name.charAt(i) == 'ㅖ' || name.charAt(i) == 'ㅢ' || name.charAt(i) == 'ㅟ' || name.charAt(i) == 'ㅝ' || // 單字元(char)單位
																														// 比較.
					name.charAt(i) == 'ㅞ' || name.charAt(i) == 'ㅙ' || name.charAt(i) == 'ㅚ' || name.charAt(i) == 'ㅘ' || // 單字元(char)單位
																														// 比較.
					name.charAt(i) == '씹' || name.charAt(i) == '좃' || name.charAt(i) == '좆' || name.charAt(i) == 'ㅤ') {
				S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
				client.sendPacket(s_charcreatestatus);
				return;
			}
		}
		
		pc.setName(name);
		pc.setType(class_type);
		pc.set_sex(class_sex);
		pc.getAbility().setBaseStr((byte) readC());
		pc.getAbility().setBaseDex((byte) readC());
		pc.getAbility().setBaseCon((byte) readC());
		pc.getAbility().setBaseWis((byte) readC());
		pc.getAbility().setBaseCha((byte) readC());
		pc.getAbility().setBaseInt((byte) readC());

		int statusAmount = pc.getAbility().getAmount();

		if (pc.getAbility().getBaseStr() > 20 || pc.getAbility().getBaseDex() > 20 || pc.getAbility().getBaseCon() > 20
				|| pc.getAbility().getBaseWis() > 20 || pc.getAbility().getBaseCha() > 20
				|| pc.getAbility().getBaseInt() > 20 || statusAmount != 75) {
			_log.finest("Character have wrong value");
			S_CharCreateStatus s_charcreatestatus3 = new S_CharCreateStatus(S_CharCreateStatus.REASON_WRONG_AMOUNT);
			client.sendPacket(s_charcreatestatus3);
			return;
		}

		_log.fine("charname: " + pc.getName() + " classId: " + pc.getClassId());
		S_CharCreateStatus s_charcreatestatus2 = new S_CharCreateStatus(S_CharCreateStatus.REASON_OK);
		client.sendPacket(s_charcreatestatus2);
		initNewChar(client, pc);
		System.out.println("角色創建: [" + pc.getName() + "] 時間: [" + amPm + " " + cal.get(hour) + "時" + cal.get(minute) + "分" + "] 創建");
	}
	//TODO 首次角色創建時的起始位置
	public static final int[] MALE_LIST = new int[] { 0, 20553, 138, 20278, 2786, 6658, 6671, 20567, 18520, 19296 };
	public static final int[] FEMALE_LIST = new int[] { 1, 48, 37, 20279, 2796, 6661, 6650, 20577, 18499, 19299 };
//    public static final int[][] START_LOC_X = new int[][] { { 32731, 32733, 32731, 32728, 32730 } };// 重製隱藏階統合 X
//    public static final int[][] START_LOC_Y = new int[][] { { 32811, 32812, 32814, 32812, 32809 } };// 重製隱藏階統合 Y
//    public static final short[] MAPID_LIST = new short[] { 3, 3, 3, 3, 3, 3, 3, 3, 3 };// 重製隱藏階

	public static final int[][] START_LOC_X = { Config.CharSettings.START_LOC_X };// 重製隱藏階統合 X
	public static final int[][] START_LOC_Y = { Config.CharSettings.START_LOC_Y };// 重製隱藏階統合 Y
	public static final short[] MAPID_LIST = new short[] { Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST,
			Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST };// 리뉴얼 숨계

	//TODO 角色創建時播放視頻
	/*public static final int[] MALE_LIST = new int[] { 0, 61, 138, 734, 2786, 6658, 6671, 12490 };
	public static final int[] FEMALE_LIST = new int[] { 1, 48, 37, 1186, 2796, 6661, 6650, 12494 };
	public static final int[][] START_LOC_X = new int[][] { { 32780, 32781, 32781, 32782, 32779 } };// 重製
	public static final int[][] START_LOC_Y = new int[][] { { 32818, 32818, 32817, 32817, 32816 } };// 重製
	public static final short[] MAPID_LIST = new short[] { 7783, 7783, 7783, 7783, 7783, 7783, 7783, 7783 };// 重製
	*/
	private static void initNewChar(GameClient client, L1PcInstance pc) throws IOException, Exception {
		short init_hp = 0, init_mp = 0;
		Random random = new Random();
		final int NewHi = 0; // 隱藏階

		int startPosType = NewHi; // defalut
		int startPos = random.nextInt(5);

		pc.setId(IdFactory.getInstance().nextId());

		if (pc.get_sex() == 0)
			pc.setClassId(MALE_LIST[pc.getType()]);
		else
			pc.setClassId(FEMALE_LIST[pc.getType()]);
		
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
			startPosType = NewHi;
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
			startPosType = NewHi;
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
			startPosType = NewHi;
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
			startPosType = NewHi;
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
			startPosType = NewHi;
		} else if (pc.isDragonknight()) { // 龍騎士
			init_hp = 16;
			init_mp = 2;
			startPosType = NewHi;
		} else if (pc.isBlackwizard()) { // 幻術師
			init_hp = 14;
			switch (pc.getAbility().getBaseWis()) {
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
			startPosType = NewHi;
		} else if (pc.isWarrior()) {
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
			startPosType = NewHi;
		} else if (pc.isFencer()) {
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
			startPosType = NewHi;
		} else if (pc.isLancer()) {
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
			startPosType = NewHi;
		}
		
		pc.setX(START_LOC_X[startPosType][startPos]);
		pc.setY(START_LOC_Y[startPosType][startPos]);
		pc.setMap(MAPID_LIST[pc.getType()]);

		pc.setHeading(0);
		pc.setLawful(0);

		pc.addBaseMaxHp(init_hp);
		pc.setCurrentHp(init_hp);
		pc.addBaseMaxMp(init_mp);
		pc.setCurrentMp(init_mp);
		pc.resetBaseAc();
		pc.setTitle(Config.Message.GameServerName);
		pc.setClanid(0);
		pc.setClanRank(0);
		pc.set_food(39); // 17%
		pc.setAccessLevel((short) 0);
		pc.setGm(false);
		pc.setMonitor(false);
		pc.setGmInvis(false);
		pc.set_exp(0);// 經驗值 0
		pc.setHighLevel(1);
		pc.setStatus(0);
		pc.setAccessLevel((short) 0);
		pc.setClanname("");
		pc.setClanMemberNotes("");
		pc.setElixirStats(0);
		pc.resetBaseMr();
		pc.setElfAttr(0);
		pc.setGlory_Earth_Attr(0);
		pc.set_PKcount(0);
		pc.set_exp_res(0);
		pc.setPartnerId(0);
		pc.setOnlineStatus(0);
		pc.setHomeTownId(0);
		pc.setContribution(0);
		pc.setBanned(false);
		pc.setKarma(0);
		pc.setReturnStat(0);
		pc.setMark_count(60);
		Calendar local_c = Calendar.getInstance();
		SimpleDateFormat local_sdf = new SimpleDateFormat("yyyyMMdd");
		local_c.setTimeInMillis(System.currentTimeMillis());
		pc.setBirthDay(Integer.parseInt(local_sdf.format(local_c.getTime())));
		if (pc.isWizard()) { // WIZ
			SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
			noti.appendNewSpell(L1SkillId.ENERGY_BOLT, true);
			pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
			//pc.sendPackets(new S_AddSkill(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			int object_id = pc.getId();
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(4); // EB
			String skill_name = l1skills.getName();
			int skill_id = l1skills.getSkillId();
			SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 註冊到資料庫
		}
		if (pc.isElf()) { // 精靈 角色創建時添加傳送到母樹技能
			SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
			noti.appendNewSpell(L1SkillId.TELEPORT_TO_MATHER, true);
			pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
			//pc.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, pc.getElfAttr()));
			int object_id = pc.getId();
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(131); // 傳送到母樹
			String skill_name = l1skills.getName();
			int skill_id = l1skills.getSkillId();
			SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0);
		}
		Beginner.getInstance().GiveItem(pc);
		Beginner.getInstance().writeBookmark(pc);
		pc.setAccountName(client.getAccountName());
		CharacterTable.getInstance().storeNewCharacter(pc);
		S_NewCharPacket s_newcharpacket = new S_NewCharPacket(pc);
		client.sendPacket(s_newcharpacket);
		pc.save();
		pc.refresh();
		AinhasadSpecialStatLoader.getInstance().addSpecialStat(pc.getId(), pc.getName());
	}

	private static boolean isAlphaNumeric(String s) {
		if (s == null) {
			return false;
		}
		boolean flag = true;
		char ac[] = s.toCharArray();
		int i = 0;
		do {
			if (i >= ac.length) {
				break;
			}
			if (!Character.isLetterOrDigit(ac[i])) {
				flag = false;
				break;
			}
			i++;
		} while (true);
		return flag;
	}

	private static boolean isInvalidName(String name) {
		int numOfNameBytes = 0;
		try {
			numOfNameBytes = name.getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		if (isAlphaNumeric(name)) {
			return false;
		}

		// XXX - 尚未確認是否與本廠規格相同
		// 如果全角字符超過5個字符，或總共超過12個字節，則視為無效名稱
		if (5 < (numOfNameBytes - name.length()) || 12 < numOfNameBytes) {
			return false;
		}

		if (BadNamesList.getInstance().isBadName(name)) {
			return false;
		}
		return true;
	}

	@Override
	public String getType() {
		return C_CREATE_CHAR;
	}
}
