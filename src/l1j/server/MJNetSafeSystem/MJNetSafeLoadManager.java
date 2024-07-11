package l1j.server.MJNetSafeSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Properties;

import javolution.io.Struct.Unsigned16;
import javolution.util.FastMap;
import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.MJCommons;

public class MJNetSafeLoadManager {
	private static MJNetSafeLoadManager _instance;

	public static MJNetSafeLoadManager getInstance() {
		if (_instance == null)
			_instance = new MJNetSafeLoadManager();
		return _instance;
	}

	public static void commands(L1PcInstance gm, String param) {
		try {
			ArrayDeque<Integer> argsQ = MJCommons.parseToIntQ(param);
			if (argsQ == null || argsQ.isEmpty())
				throw new Exception("");

			switch (argsQ.poll()) {
				case 2:
					_instance.reload();
					gm.sendPackets(new S_SystemMessage("[MJNetSafe 系統配置重新加載完成。]"));
					// L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] {new
					// S_ServerMessage(3362), new S_SystemMessage("伺服器版本已更新。請重新啟動連線並再次接收原始補丁。")},
					// true);
					break;
				default:
					throw new Exception("");
			}

		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage("使用方法 .NetSafe [1. *][2. 重新加載]"));
		}
	}

	public void reload() {
		loadConfig();
	}

	public void release() {
		if (_filters != null) {
			_filters.clear();
			_filters = null;
		}
	}

	private MJNetSafeLoadManager() {
	}

	public void load() {
		loadConfig();
	}

	public static int isFilter(GameClient clnt, int op) {
		Integer flg = _filters.get(op);
		if (flg == null)
			return RESULT_FILTER;

		if ((flg & FILTER_BYPASS) > 0)
			return RESULT_BYPASS;

		if ((flg & FILTER_STANDBY) > 0 && Config.Login.StandbyServer)
			return RESULT_FILTER;

		L1PcInstance pc = clnt.getActiveChar();
		if (pc != null) {
			if (pc.isParalyzed() || pc.isSleeped()) {
				if ((flg & FILTER_PARALYZED) > 0)
					return RESULT_FILTER;

				if (pc.hasSkillEffect(L1SkillId.THUNDER_GRAB) || pc.hasSkillEffect(L1SkillId.STATUS_FREEZE)) {
					if ((flg & FILTER_NONMOVABLE) > 0)
						return RESULT_FILTER;
				}
			}
		}
		return RESULT_NORMAL;
	}

	private static final int FILTER_NONE = 0x00;
	private static final int FILTER_BYPASS = 0x01;
	private static final int FILTER_STANDBY = 0x02;
	private static final int FILTER_PARALYZED = 0x04;
	private static final int FILTER_NONMOVABLE = 0x08;

	public static final int RESULT_BYPASS = 0x01;
	public static final int RESULT_NORMAL = 0x02;
	public static final int RESULT_FILTER = 0x04;

	private static FastMap<Integer, Integer> _filters;

	public static int NS_CLIENT_PER_SECONDS;
	public static int NS_CLIENT_PER_SECONDS_FROM_SECONDS = 1; // for load delay
	public static int NS_PACKET_MAXSIZE;
	public static int NS_PACKET_MAXOVER_COUNT;
	public static int NS_CLIENT_VERSION;
	// public static long NS_CLIENT_VERSION;
	public static int NS_AUTHSERVER_VERSION;
	public static int NS_CACHESERVER_VERSION;
	public static int NS_TAMSERVER_VERSION;
	public static int NS_ARCASERVER_VERSION;
	public static int NS_HIBREED_INTERSERVER_VERSION;
	public static int NS_ARENACOSERVER_VERSION;
	public static int NS_CLIENT_SETTING_SWITCH;
	public static long NS_CLIENT_HANDHSAKE_CHECK_MILLIS;
	public static byte[] NS_CLIENT_SIDE_KEY;

	private static void loadConfig() {
		try {
			FastMap<Integer, Integer> filters = new FastMap<Integer, Integer>();
			filters.put(Opcodes.C_ARCHERARRANGE, FILTER_NONE);
			filters.put(Opcodes.C_SAY, FILTER_NONE);
			filters.put(Opcodes.C_GOTO_MAP, FILTER_NONE);
			filters.put(Opcodes.C_GIVE, FILTER_NONE);
			filters.put(Opcodes.C_TELL, FILTER_NONE);
			filters.put(Opcodes.C_TAX, FILTER_NONE);
			filters.put(Opcodes.C_BOARD_LIST, FILTER_NONE);
			filters.put(Opcodes.C_CHAT_PARTY_CONTROL, FILTER_NONE);
			filters.put(Opcodes.C_PLATE, FILTER_NONE);
			filters.put(Opcodes.C_HYPERTEXT_INPUT_RESULT, FILTER_NONE);
			filters.put(Opcodes.C_TELEPORT, FILTER_NONE);
			filters.put(Opcodes.C_WANTED, FILTER_NONE);
			filters.put(Opcodes.C_SAVEIO, FILTER_NONE);
			filters.put(Opcodes.C_BUYABLE_SPELL, FILTER_NONE);
			filters.put(Opcodes.C_GOTO_PORTAL, FILTER_NONE);
			filters.put(Opcodes.C_MERCENARYEMPLOY, FILTER_NONE);
			filters.put(Opcodes.C_SHIFT_SERVER, FILTER_NONE);
			filters.put(Opcodes.C_SMS, FILTER_NONE);
			filters.put(Opcodes.C_ONOFF, FILTER_NONE);
			filters.put(Opcodes.C_LOGIN_RESULT, FILTER_NONE);
			filters.put(Opcodes.C_HACTION, FILTER_NONE);
			filters.put(Opcodes.C_FIXABLE_ITEM, FILTER_NONE);
			filters.put(Opcodes.C_LOGIN_TEST, FILTER_NONE);
			filters.put(Opcodes.C_MONITOR_CONTROL, FILTER_NONE);
			filters.put(Opcodes.C_WHO, FILTER_NONE);
			filters.put(Opcodes.C_VOICE_CHAT, FILTER_NONE);
			filters.put(Opcodes.C_CHANGE_DIRECTION, FILTER_NONE);
			filters.put(Opcodes.C_FIX, FILTER_NONE);
			filters.put(Opcodes.C_MAIL, FILTER_NONE);
			filters.put(Opcodes.C_NEW_ACCOUNT, FILTER_NONE);
			filters.put(Opcodes.C_ALT_ATTACK, FILTER_NONE);
			filters.put(Opcodes.C_JOIN_PLEDGE, FILTER_NONE);
			filters.put(Opcodes.C_RESTART, FILTER_NONE);
			filters.put(Opcodes.C_TITLE, FILTER_NONE);
			filters.put(Opcodes.C_ENTER_PORTAL, FILTER_NONE);
			filters.put(Opcodes.C_SAVE, FILTER_NONE);
			filters.put(Opcodes.C_ACTION, FILTER_NONE);
			filters.put(Opcodes.C_CREATE_CUSTOM_CHARACTER, FILTER_NONE);
			filters.put(Opcodes.C_PLEDGE_WATCH, FILTER_NONE);
			filters.put(Opcodes.C_CHECK_PK, FILTER_NONE);
			filters.put(Opcodes.C_USE_SPELL, FILTER_NONE);
			filters.put(Opcodes.C_QUERY_PERSONAL_SHOP, FILTER_NONE);
			filters.put(Opcodes.C_ENTER_WORLD, FILTER_NONE);
			filters.put(Opcodes.C_CHANGE_ACCOUNTINFO, FILTER_NONE);
			filters.put(Opcodes.C_EXIT_GHOST, FILTER_NONE);
			filters.put(Opcodes.C_ENTER_SHIP, FILTER_NONE);
			filters.put(Opcodes.C_CANCEL_XCHG, FILTER_NONE);
			filters.put(Opcodes.C_SILENCE, FILTER_NONE);
			filters.put(Opcodes.C_CHECK_INVENTORY, FILTER_NONE);
			filters.put(Opcodes.C_RETURN_SUMMON, FILTER_NONE);
			filters.put(Opcodes.C_TELEPORT_USER, FILTER_NONE);
			filters.put(Opcodes.C_ANSWER, FILTER_NONE);
			filters.put(Opcodes.C_CHAT, FILTER_NONE);
			filters.put(Opcodes.C_MERCENARYARRANGE, FILTER_NONE);
			filters.put(Opcodes.C_INVITE_PARTY_TARGET, FILTER_NONE);
			filters.put(Opcodes.C_EXTENDED, FILTER_NONE);
			filters.put(Opcodes.C_BANISH_PARTY, FILTER_NONE);
			filters.put(Opcodes.C_THROW, FILTER_NONE);
			filters.put(Opcodes.C_ATTACK, FILTER_NONE);
			filters.put(Opcodes.C_INCLUDE, FILTER_NONE);
			filters.put(Opcodes.C_BOARD_DELETE, FILTER_NONE);
			filters.put(Opcodes.C_USE_ITEM, FILTER_NONE);
			filters.put(Opcodes.C_SELECT_TIME, FILTER_NONE);
			filters.put(Opcodes.C_EXCHANGEABLE_SPELL, FILTER_NONE);
			filters.put(Opcodes.C_WITHDRAW, FILTER_NONE);
			filters.put(Opcodes.C_WAREHOUSE_CONTROL, FILTER_NONE);
			filters.put(Opcodes.C_MERCENARYSELECT, FILTER_NONE);
			filters.put(Opcodes.C_MOVE, FILTER_NONE);
			filters.put(Opcodes.C_ADD_XCHG, FILTER_NONE);
			filters.put(Opcodes.C_KICK, FILTER_NONE);
			filters.put(Opcodes.C_WAR, FILTER_NONE);
			filters.put(Opcodes.C_START_CASTING, FILTER_NONE);
			filters.put(Opcodes.C_GET, FILTER_NONE);
			filters.put(Opcodes.C_BUILDER_CONTROL, FILTER_NONE);
			filters.put(Opcodes.C_LEAVE_PLEDGE, FILTER_NONE);
			filters.put(Opcodes.C_ADDR, FILTER_NONE);
			filters.put(Opcodes.C_EXCHANGE_SPELL, FILTER_NONE);
			filters.put(Opcodes.C_PERSONAL_SHOP, FILTER_NONE);
			filters.put(Opcodes.C_SLAVE_CONTROL, FILTER_NONE);
			filters.put(Opcodes.C_BLINK, FILTER_NONE);
			filters.put(Opcodes.C_ATTACK_CONTINUE, FILTER_NONE);
			filters.put(Opcodes.C_WHO_PARTY, FILTER_NONE);
			filters.put(Opcodes.C_REQUEST_ROLL, FILTER_NONE);
			filters.put(Opcodes.C_EMBLEM, FILTER_NONE);
			filters.put(Opcodes.C_CLIENT_READY, FILTER_NONE);
			filters.put(Opcodes.C_QUERY_BUDDY, FILTER_NONE);
			filters.put(Opcodes.C_OPEN, FILTER_NONE);
			filters.put(Opcodes.C_BOOK, FILTER_NONE);
			filters.put(Opcodes.C_SUMMON, FILTER_NONE);
			filters.put(Opcodes.C_BOOKMARK, FILTER_NONE);
			filters.put(Opcodes.C_BOARD_WRITE, FILTER_NONE);
			filters.put(Opcodes.C_INVITE_PARTY, FILTER_NONE);
			filters.put(Opcodes.C_CREATE_PLEDGE, FILTER_NONE);
			filters.put(Opcodes.C_DROP, FILTER_NONE);
			filters.put(Opcodes.C_CONTROL_WEATHER, FILTER_NONE);
			filters.put(Opcodes.C_LOGIN, FILTER_NONE);
			filters.put(Opcodes.C_ADD_BUDDY, FILTER_NONE);
			filters.put(Opcodes.C_LEAVE_PARTY, FILTER_NONE);
			filters.put(Opcodes.C_DESTROY_ITEM, FILTER_NONE);
			filters.put(Opcodes.C_SERVER_SELECT, FILTER_NONE);
			filters.put(Opcodes.C_SHUTDOWN, FILTER_NONE);
			filters.put(Opcodes.C_DUEL, FILTER_NONE);
			filters.put(Opcodes.C_REMOVE_BUDDY, FILTER_NONE);
			filters.put(Opcodes.C_WHO_PLEDGE, FILTER_NONE);
			filters.put(Opcodes.C_CHANNEL, FILTER_NONE);
			filters.put(Opcodes.C_LOGOUT, FILTER_NONE);
			filters.put(Opcodes.C_REGISTER_QUIZ, FILTER_NONE);
			filters.put(Opcodes.C_BAN, FILTER_NONE);
			filters.put(Opcodes.C_EXTENDED_HYBRID, FILTER_NONE);
			filters.put(Opcodes.C_MARRIAGE, FILTER_NONE);
			filters.put(Opcodes.C_BAN_MEMBER, FILTER_NONE);
			filters.put(Opcodes.C_BUY_SELL, FILTER_NONE);
			filters.put(Opcodes.C_BOARD_READ, FILTER_NONE);
			filters.put(Opcodes.C_CHANGE_PASSWORD, FILTER_NONE);
			filters.put(Opcodes.C_WISH, FILTER_NONE);
			filters.put(Opcodes.C_SELECTABLE_TIME, FILTER_NONE);
			filters.put(Opcodes.C_UPLOAD_EMBLEM, FILTER_NONE);
			filters.put(Opcodes.C_DEPOSIT, FILTER_NONE);
			filters.put(Opcodes.C_QUERY_CASTLE_SECURITY, FILTER_NONE);
			filters.put(Opcodes.C_DELETE_BOOKMARK, FILTER_NONE);
			filters.put(Opcodes.C_ASK_XCHG, FILTER_NONE);
			filters.put(Opcodes.C_READ_NEWS, FILTER_NONE);
			filters.put(Opcodes.C_ACCEPT_XCHG, FILTER_NONE);
			filters.put(Opcodes.C_READ_NOTICE, FILTER_NONE);
			filters.put(Opcodes.C_ALIVE, FILTER_NONE);
			filters.put(Opcodes.C_EXCLUDE, FILTER_NONE);
			filters.put(Opcodes.C_DELETE_CHARACTER, FILTER_NONE);
			filters.put(Opcodes.C_DIALOG, FILTER_NONE);
			filters.put(Opcodes.C_EXTENDED_PROTOBUF, FILTER_NONE);
			filters.put(Opcodes.C_BUY_SPELL, FILTER_NONE);
			filters.put(Opcodes.C_QUIT, FILTER_NONE);
			filters.put(Opcodes.C_MATCH_MAKING, FILTER_NONE);
			filters.put(Opcodes.C_FAR_ATTACK, FILTER_NONE);
			filters.put(Opcodes.C_CHANGE_CASTLE_SECURITY, FILTER_NONE);
			filters.put(Opcodes.C_DEAD_RESTART, FILTER_NONE);
			filters.put(Opcodes.C_NPC_ITEM_CONTROL, FILTER_NONE);
			filters.put(Opcodes.C_RANK_CONTROL, FILTER_NONE);

			// 要忽略的資料包操作碼
			setOpFlag(filters, new Integer[] {
					// Opcodes.C_BUYABLE_SPELL,
					Opcodes.C_ALIVE,
					Opcodes.C_CLIENT_READY,
					// Opcodes.C_SHIFT_SERVER,
					Opcodes.C_TELL,
					Opcodes.C_ENTER_PORTAL,
			}, FILTER_BYPASS);

			// 等待開啟時忽略封包操作碼
			setOpFlag(filters, new Integer[] {
					Opcodes.C_ALIVE,
					Opcodes.C_CLIENT_READY,
					// Opcodes.C_SHIFT_SERVER,
					Opcodes.C_TELL,
					// Opcodes.C_ATTACK,
					// Opcodes.C_USE_SPELL,
					// Opcodes.C_FAR_ATTACK,
					// Opcodes.C_ATTACK_CONTINUE,
			}, FILTER_STANDBY);

			// 如果可靠則忽略的資料包操作碼
			setOpFlag(filters, new Integer[] {
					Opcodes.C_ATTACK,
					Opcodes.C_ASK_XCHG,
					Opcodes.C_ACCEPT_XCHG,
					Opcodes.C_DROP,
					Opcodes.C_GET,
					Opcodes.C_FAR_ATTACK,
					Opcodes.C_GIVE,
					Opcodes.C_USE_SPELL,
					Opcodes.C_RESTART,
					Opcodes.C_ADD_XCHG,
					Opcodes.C_LOGOUT,
			}, FILTER_PARALYZED);

			// 無法移動時要忽略的資料包操作碼
			setOpFlag(filters, new Integer[] {
					Opcodes.C_DROP,
					Opcodes.C_GET,
					Opcodes.C_GIVE,
					Opcodes.C_RESTART,
			}, FILTER_NONMOVABLE);

			Properties settings = new Properties();
			InputStream is = new FileInputStream(new File("./config/mjnetsafe.properties"));
			settings.load(is);
			is.close();
			FastMap<Integer, Integer> tmp = _filters;
			_filters = filters;
			if (tmp != null) {
				tmp.clear();
				tmp = null;
			}

			String column = "NetSafe_MaxPacketSize";
			NS_PACKET_MAXSIZE = Integer.parseInt(settings.getProperty(column, "5120"));

			column = "NetworkClientPerSeconds";
			NS_CLIENT_PER_SECONDS = Integer.parseInt(settings.getProperty(column, "1"));

			column = "NetworkClientPerSecondsFromSeconds";
			NS_CLIENT_PER_SECONDS_FROM_SECONDS = Integer.parseInt(settings.getProperty(column, "1"));

			column = "NetSafe_MaxPacketOverCount";
			NS_PACKET_MAXOVER_COUNT = Integer.parseInt(settings.getProperty(column, "3"));

			column = "NetSafe_ClientVersion";
			NS_CLIENT_VERSION = Integer.parseUnsignedInt(settings.getProperty(column, "2209131701"));
			// NS_CLIENT_VERSION = Long.parseLong(settings.getProperty(column,
			// "2209131701"))
			// System.out.println("클라버전: "+Integer.toUnsignedString(NS_CLIENT_VERSION));
			// NS_CLIENT_VERSION = Long.parseLong(settings.getProperty(column,
			// "2010271702"));

			column = "NetSafe_AuthVersion";
			NS_AUTHSERVER_VERSION = Integer.parseInt(settings.getProperty(column, "2015090301"));

			column = "NetSafe_GlobalCacheVersion";
			NS_CACHESERVER_VERSION = Integer.parseInt(settings.getProperty(column, "151112700"));

			column = "NetSafe_TamServerVersion";
			NS_TAMSERVER_VERSION = Integer.parseInt(settings.getProperty(column, "161031701"));

			column = "NetSafe_ArcaServerVersion";
			NS_ARCASERVER_VERSION = Integer.parseInt(settings.getProperty(column, "161111700"));

			column = "NetSafe_HibreedInterServerVersion";
			NS_HIBREED_INTERSERVER_VERSION = Integer.parseInt(settings.getProperty(column, "1701031002"));

			column = "NetSafe_ArenacoServerVersion";
			NS_ARENACOSERVER_VERSION = Integer.parseInt(settings.getProperty(column, "160531700"));

			column = "NetSafe_ClientSettingSwitch";
			NS_CLIENT_SETTING_SWITCH = Integer.parseInt(settings.getProperty(column, "889191819"));

			column = "NetSafe_ClientHandShakeCheckSeconds";
			NS_CLIENT_HANDHSAKE_CHECK_MILLIS = Integer.parseInt(settings.getProperty(column, "3")) * 1000L;

			column = "NetSafe_ClientSideKey";
			// long value = Long.parseLong(settings.getProperty(column,
			// "0x7a834df5").replace("0x", ""), 16);
			// long value = Long.parseLong(settings.getProperty(column,
			// "0x5455b0fd").replace("0x", ""), 16);
			// long value = Long.parseLong(settings.getProperty(column,
			// "0x1a4e04da").replace("0x", ""), 16); //20220810
			// long value = Long.parseLong(settings.getProperty(column,
			// "0x142c974f").replace("0x", ""), 16); //20220817
			long value = Long.parseLong(settings.getProperty(column, "0x0507b978").replace("0x", ""), 16); // 20220920
			NS_CLIENT_SIDE_KEY = new byte[] {
					(byte) (value & 0xff),
					(byte) (value >> 8 & 0xff),
					(byte) (value >> 16 & 0xff),
					(byte) (value >> 24 & 0xff),
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setOpFlag(FastMap<Integer, Integer> maps, Integer[] list, int flg) {
		for (Integer i : list) {
			Integer op = maps.get(i);
			if (op != null)
				maps.put(i, op | flg);
		}
	}
}
