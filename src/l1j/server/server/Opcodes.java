package l1j.server.server;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class Opcodes {
	private static Opcodes ins;
	private HashMap<Integer, String> C_OPCODELIST = new HashMap<Integer, String>();
	private HashMap<Integer, String> S_OPCODELIST = new HashMap<Integer, String>();
	private ArrayList<Integer> C_OPCODE_KEYS = new ArrayList<>(140);
	public ArrayList<Integer> get_C_OPCODE_KEY_LIST() {
		return C_OPCODE_KEYS;
	}
	
	public static Opcodes getIns() throws Exception {
		if (ins == null) ins = new Opcodes();
		return ins;
	}
	private Opcodes() {
		try {
			Field[] fieldArray = this.getClass().getFields();
			for (Field field : fieldArray) {
				String name = field.getName();
				if (name.startsWith("C_") || name.startsWith("S_")) {
					try {
						int opcode = (int) field.get(this.getClass());
						if (name.startsWith("C_")) {
							C_OPCODELIST.put(opcode, field.getName());
							C_OPCODE_KEYS.add(opcode);
						} else if (name.startsWith("S_")) {
							S_OPCODELIST.put(opcode, field.getName());
						}
					} catch (Exception e) {
						continue;
					}
				} else {
					//System.out.println("操作碼: " + field.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getOpcodeName(int opcode, String startW) {
		String name = C_OPCODELIST.get(opcode);
		if (name != null && name.startsWith(startW)) {
			return name;
		} 
		name = S_OPCODELIST.get(opcode);
		if (name != null && name.startsWith(startW)) {
			return name;
		}
		return null;
	}
	
	

	public static final int C_SERVER_SELECT = 3;
	  public static final int C_FAR_ATTACK = 4;
	  public static final int C_ASK_XCHG = 5;
	  public static final int C_BOARD_LIST = 6;
	  public static final int C_EXCLUDE = 7;
	  public static final int C_CHECK_PK = 9;
	  public static final int C_RANK_CONTROL = 10;
	  public static final int C_MERCENARYSELECT = 14;
	  public static final int C_SAY = 16;
	  public static final int C_LOGIN_RESULT = 18;
	  public static final int C_CHANGE_PASSWORD = 19;
	  public static final int C_UPLOAD_EMBLEM = 20;
	  public static final int C_MARRIAGE = 21;
	  public static final int C_GET = 23;
	  public static final int C_BUYABLE_SPELL = 24;
	  public static final int C_CREATE_PLEDGE = 26;
	  public static final int C_READ_NOTICE = 27;
	  public static final int C_EMBLEM = 28;
	  public static final int C_SHIFT_SERVER = 29;
	  public static final int C_TELEPORT_USER = 31;
	  public static final int C_ANSWER = 33;
	  public static final int C_CREATE_CUSTOM_CHARACTER = 35;
	  public static final int C_DEPOSIT = 36;
	  public static final int C_NPC_ITEM_CONTROL = 37;
	  public static final int C_TAX = 41;
	  public static final int C_INVITE_PARTY_TARGET = 42;
	  public static final int C_ATTACK = 43;
	  public static final int C_SLAVE_CONTROL = 45;
	  public static final int C_BAN = 46;
	  public static final int C_ACTION = 50;
	  public static final int C_DIALOG = 51;
	  public static final int C_NEW_ACCOUNT = 52;
	  public static final int C_EXTENDED = 53;
	  public static final int C_CHANNEL = 55;
	  public static final int C_QUERY_BUDDY = 56;
	  public static final int C_DEAD_RESTART = 58;
	  public static final int C_WITHDRAW = 59;
	  public static final int C_GOTO_MAP = 60;
	  public static final int C_CHANGE_CASTLE_SECURITY = 63;
	  public static final int C_ADDR = 64;
	  public static final int C_BANISH_PARTY = 65;
	  public static final int C_TELL = 67;
	  public static final int C_QUIT = 68;
	  public static final int C_WAREHOUSE_CONTROL = 71;
	  public static final int C_LOGIN_TEST = 72;
	  public static final int C_CHANGE_DIRECTION = 79;
	  public static final int C_DELETE_CHARACTER = 81;
	  public static final int C_INVITE_PARTY = 82;
	  public static final int C_BOOKMARK = 84;
	  public static final int C_LOGIN = 85;
	  public static final int C_QUERY_PERSONAL_SHOP = 86;
	  public static final int C_BUY_SELL = 89;
	  public static final int C_BOARD_WRITE = 91;
	  public static final int C_BOARD_READ = 93;
	  public static final int C_MAIL = 95;
	  public static final int C_ALT_ATTACK = 96;
	  public static final int C_DROP = 97;
	  public static final int C_PERSONAL_SHOP = 98;
	  public static final int C_REGISTER_QUIZ = 100;
	  public static final int C_ENTER_PORTAL = 101;
	  public static final int C_CHANGE_ACCOUNTINFO = 104;
	  public static final int C_BAN_MEMBER = 105;
	  public static final int C_DELETE_BOOKMARK = 106;
	  public static final int C_WHO_PARTY = 108;
	  public static final int C_SELECT_TIME = 109;
	  public static final int C_READ_NEWS = 110;
	  public static final int C_ACCEPT_XCHG = 111;
	  public static final int C_LEAVE_PLEDGE = 112;
	  public static final int C_DUEL = 118;
	  public static final int C_LEAVE_PARTY = 119;
	  public static final int C_CHAT_PARTY_CONTROL = 120;
	  public static final int C_PLATE = 121;
	  public static final int C_VERSION = 122;
	  public static final int C_BUY_SPELL = 123;
	  public static final int C_REMOVE_BUDDY = 124;
	  public static final int C_EXCHANGEABLE_SPELL = 125;
	  public static final int C_BOOK = 126;
	  public static final int C_SAVEIO = 128;
	  public static final int C_ATTACK_CONTINUE = 130;
	  public static final int C_OPEN = 131;
	  public static final int C_CONTROL_WEATHER = 132;
	  public static final int C_WHO_PLEDGE = 135;
	  public static final int C_GIVE = 137;
	  public static final int C_ADD_BUDDY = 138;
	  public static final int C_BLINK = 139;
	  public static final int C_SHUTDOWN = 140;
	  public static final int C_JOIN_PLEDGE = 141;
	  public static final int C_START_CASTING = 144;
	  public static final int C_THROW = 146;
	  public static final int C_RETURN_SUMMON = 147;
	  public static final int C_MERCENARYNAME = 148;
	  public static final int C_SAVE = 150;
	  public static final int C_EXTENDED_PROTOBUF = 157;
	  public static final int C_CHECK_INVENTORY = 160;
	  public static final int C_BUILDER_CONTROL = 161;
	  public static final int C_SUMMON = 164;
	  public static final int C_SELECTABLE_TIME = 167;
	  public static final int C_ENTER_WORLD = 168;
	  public static final int C_MERCENARYARRANGE = 169;
	  public static final int C_CHAT = 170;
	  public static final int C_QUERY_CASTLE_SECURITY = 171;
	  public static final int C_WHO = 174;
	  public static final int C_MOVE = 175;
	  public static final int C_ALIVE = 176;
	  public static final int C_MONITOR_CONTROL = 178;
	  public static final int C_EXIT_GHOST = 179;
	  public static final int C_ONOFF = 184;
	  public static final int C_ENTER_SHIP = 185;
	  public static final int C_HYPERTEXT_INPUT_RESULT = 188;
	  public static final int C_ARCHERARRANGE = 190;
	  public static final int C_RESTART = 194;
	  public static final int C_CANCEL_XCHG = 195;
	  public static final int C_WAR = 197;
	  public static final int C_DESTROY_ITEM = 198;
	  public static final int C_GOTO_PORTAL = 200;
	  public static final int C_PLEDGE_WATCH = 201;
	  public static final int C_CLIENT_READY = 202;
	  public static final int C_EXTENDED_HYBRID = 207;
	  public static final int C_USE_ITEM = 208;
	  public static final int C_EXCHANGE_SPELL = 210;
	  public static final int C_TITLE = 214;
	  public static final int C_USE_SPELL = 216;
	  public static final int C_INCLUDE = 217;
	  public static final int C_MATCH_MAKING = 221;
	  public static final int C_BOARD_DELETE = 222;
	  public static final int C_SILENCE = 223;
	  public static final int C_SMS = 224;
	  public static final int C_TELEPORT = 226;
	  public static final int C_HACTION = 227;
	  public static final int C_FIX = 228;
	  public static final int C_WANTED = 231;
	  public static final int C_VOICE_CHAT = 232;
	  public static final int C_FIXABLE_ITEM = 233;
	  public static final int C_LOGOUT = 235;
	  public static final int C_REQUEST_ROLL = 236;
	  public static final int C_ADD_XCHG = 247;
	  public static final int C_KICK = 252;
	  public static final int C_MERCENARYEMPLOY = 253;
	  public static final int C_WISH = 254;
	  
	  public static final int S_AGIT_LIST = 0;
	  public static final int S_MASTER = 1;
	  public static final int S_ADD_BOOKMARK = 2;
	  public static final int S_MAGE_STRENGTH = 4;
	  public static final int S_ATTACK = 5;
	  public static final int S_ROLL_RESULT = 6;
	  public static final int S_CHANGE_ITEM_TYPE = 8;
	  public static final int S_WITHDRAW = 12;
	  public static final int S_MERCENARYARRANGE = 13;
	  public static final int S_DECREE = 14;
	  public static final int S_CHANGE_COUNT = 15;
	  public static final int S_POLYMORPH = 16;
	  public static final int S_BREATH = 17;
	  public static final int S_AGIT_MAP = 18;
	  public static final int S_EXTENDED_PROTOBUF = 19;
	  public static final int S_ADD_SPELL = 21;
	  public static final int S_SLAVE_CONTROL = 22;
	  public static final int S_CRIMINAL = 24;
	  public static final int S_MAGIC_STATUS = 25;
	  public static final int S_PERSONAL_SHOP_LIST = 29;
	  public static final int S_ADD_INVENTORY = 30;
	  public static final int S_CHARACTER_INFO = 31;
	  public static final int S_PARALYSE = 33;
	  public static final int S_TITLE = 36;
	  public static final int S_CHANGE_ATTR = 38;
	  public static final int S_EXTENDED_HYBRID = 39;
	  public static final int S_SELL_LIST = 40;
	  public static final int S_EXP = 41;
	  public static final int S_CHANGE_PASSWORD_CHECK = 42;
	  public static final int S_VOICE_CHAT = 43;
	  public static final int S_BUYABLE_SPELL_LIST = 44;
	  public static final int S_WARNING_CODE = 45;
	  public static final int S_SERVER_LIST = 46;
	  public static final int S_HIT_POINT = 47;
	  public static final int S_DEPOSIT = 49;
	  public static final int S_REMOVE_OBJECT = 51;
	  public static final int S_REQUEST_SUMMON = 52;
	  public static final int S_MERCENARYNAME = 57;
	  public static final int S_CHANGE_LEVEL = 60;
	  public static final int S_SAY_CODE = 61;
	  public static final int S_WAR = 63;
	  public static final int S_MERCENARYEMPLOY = 66;
	  public static final int S_CHANGE_ACCOUNTINFO_CHECK = 67;
	  public static final int S_MAGE_SHIELD = 69;
	  public static final int S_CHANGE_ITEM_BLESS = 70;
	  public static final int S_CHANGE_DESC = 71;
	  public static final int S_PLEDGE_WATCH = 78;
	  public static final int S_LOGIN_CHECK = 81;
	  public static final int S_REMOVE_SPELL = 82;
	  public static final int S_NEW_ACCOUNT_CHECK = 83;
	  public static final int S_CHANGE_ALIGNMENT = 84;
	  public static final int S_ASK = 85;
	  public static final int S_EFFECT = 86;
	  public static final int S_XCHG_START = 88;
	  public static final int S_RESTART = 89;
	  public static final int S_SOUND_EFFECT = 91;
	  public static final int S_EMOTION = 92;
	  public static final int S_SPEED = 93;
	  public static final int S_TAX = 95;
	  public static final int S_REMOVE_INVENTORY = 98;
	  public static final int S_MAIL_INFO = 99;
	  public static final int S_MESSAGE_CODE = 102;
	  public static final int S_TELL = 104;
	  public static final int S_AC = 106;
	  public static final int S_VERSION_CHECK = 107;
	  public static final int S_EVENT = 108;
	  public static final int S_EXTENDED = 109;
	  public static final int S_PLEDGE = 110;
	  public static final int S_SAY = 112;
	  public static final int S_BLIND = 114;
	  public static final int S_STATUS = 116;
	  public static final int S_ENTER_WORLD_CHECK = 117;
	  public static final int S_CHANGE_DIRECTION = 120;
	  public static final int S_CHANGE_ABILITY = 122;
	  public static final int S_RETRIEVE_LIST = 127;
	  public static final int S_CLIENT_READY = 128;
	  public static final int S_NOT_ENOUGH_FOR_SPELL = 129;
	  public static final int S_BUY_LIST = 131;
	  public static final int S_SHOW_MAP = 132;
	  public static final int S_MATCH_MAKING = 133;
	  public static final int S_FIXABLE_ITEM_LIST = 136;
	  public static final int S_ATTACK_MANY = 137;
	  public static final int S_WORLD = 141;
	  public static final int S_DELETE_CHARACTER_CHECK = 143;
	  public static final int S_HYPERTEXT = 144;
	  public static final int S_ADD_XCHG = 145;
	  public static final int S_BOARD_LIST = 152;
	  public static final int S_MESSAGE = 153;
	  public static final int S_READ_MAIL = 155;
	  public static final int S_TIME = 156;
	  public static final int S_MERCENARYSELECT = 158;
	  public static final int S_INVISIBLE = 160;
	  public static final int S_EMBLEM = 161;
	  public static final int S_CHANGE_ITEM_DESC_EX = 164;
	  public static final int S_SELECTABLE_TIME_LIST = 166;
	  public static final int S_XCHG_RESULT = 167;
	  public static final int S_CLONE = 172;
	  public static final int S_EXCHANGEABLE_SPELL_LIST = 175;
	  public static final int S_WEATHER = 176;
	  public static final int S_NEWS = 179;
	  public static final int S_IDENTIFY_CODE = 181;
	  public static final int S_PUT_OBJECT = 186;
	  public static final int S_BLINK = 189;
	  public static final int S_NUM_CHARACTER = 190;
	  public static final int S_WIELD = 193;
	  public static final int S_CHANGE_ITEM_DESC = 194;
	  public static final int S_MAGE_DEXTERITY = 196;
	  public static final int S_RESURRECT = 197;
	  public static final int S_PING = 198;
	  public static final int S_PORTAL = 202;
	  public static final int S_NEW_CHAR_INFO = 204;
	  public static final int S_CHANGE_LIGHT = 206;
	  public static final int S_MOVE_OBJECT = 211;
	  public static final int S_NOTICE = 212;
	  public static final int S_POISON = 213;
	  public static final int S_CREATE_CHARACTER_CHECK = 215;
	  public static final int S_ADD_INVENTORY_BATCH = 216;
	  public static final int S_EFFECT_LOC = 219;
	  public static final int S_ABILITY_SCORES = 223;
	  public static final int S_HYPERTEXT_INPUT = 224;
	  public static final int S_WANTED_LOGIN = 225;
	  public static final int S_ACTION = 226;
	  public static final int S_COMMAND_TARGET = 228;
	  public static final int S_DRUNKEN = 229;
	  public static final int S_KEY = 231;
	  public static final int S_CHANGE_ITEM_USE = 237;
	  public static final int S_BOOK_LIST = 238;
	  public static final int S_ATTACK_ALL = 240;
	  public static final int S_KICK = 245;
	  public static final int S_BOARD_READ = 248;
	  public static final int S_ARCHERARRANGE = 249;
	  public static final int S_HIT_RATIO = 250;
	  public static final int S_CASTLE_OWNER = 252;
	  public static final int S_MANA_POINT = 254;
	
	//Team TheDay by.犹大
	public static final byte[] VERSIONBYTES_HEAD = {
					(byte) 0x38, 
					(byte) 0x96, (byte) 0x81, (byte) 0x85, (byte) 0x8a, (byte) 0x06,
					(byte) 0x40, 
					(byte) 0x00, 
					(byte) 0x48, 
					(byte) 0x00,
					(byte) 0x50, 
					(byte) 0x8b, (byte) 0xfb, (byte) 0xff, (byte) 0xa7, (byte) 0x03,
					(byte) 0x58,
			};
	
			
		public static final byte[] VERSIONBYTES_TAIL = {
				(byte) 0x60, 
				(byte) 0x89, (byte) 0xe1, (byte) 0xc4, (byte) 0xbd, (byte) 0x07,
				(byte) 0x68, 
				(byte) 0x8c, (byte) 0xe1, (byte) 0xc4, (byte) 0xbd, (byte) 0x07,
				(byte) 0x70, 
				(byte) 0xd9, (byte) 0xff, (byte) 0xb5, (byte) 0xea, (byte) 0x07,
				(byte) 0x78, 
				(byte) 0x89, (byte) 0xe0, (byte) 0xdb, (byte) 0xed, (byte) 0x07,
				(byte) 0x80, (byte) 0x01, 
				(byte) 0xeb, (byte) 0xc0, (byte) 0xe1, (byte) 0xeb, (byte) 0x07,
				(byte) 0x88, (byte) 0x01, 
				(byte) 0x00,
				(byte) 0x90, (byte) 0x01, 
				(byte) 0x00,
				(byte) 0x98, (byte) 0x01, 
				(byte) 0x00
		};
		


		public static final byte[] S_KEYBYTES = {
				(byte) (S_KEY & 0xff),
				(byte) 0x62, (byte) 0x25, (byte) 0x04, (byte) 0x62,
				(byte) 0x19, (byte) 0xdd, (byte) 0xd3, (byte) 0x7f 
		};

		public static long getSeed(){
			long seed = S_KEYBYTES[1] & 0xff;
			seed |= ((S_KEYBYTES[2] << 8) & 0xff00);
			seed |= ((S_KEYBYTES[3] << 16) & 0xff0000);
			seed |= ((S_KEYBYTES[4] << 24) & 0xff000000);
			
			return seed;
		}
	}