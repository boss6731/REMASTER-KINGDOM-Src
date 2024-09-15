package l1j.server.server;

import static l1j.server.server.model.skill.L1SkillId.EXP_BUFF;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Base64;
import l1j.server.Config;
import l1j.server.FatigueProperty;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eEinhasadBonusType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REST_GAUGE_CHARGE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_POINT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.SQLUtil;

public class Account {
	/** 帳號物件ID **/
	private int _id;
	/** 帳號名 */
	private String _name;
	/** 連線者IP位址 */
	private String _ip;
	/** 密碼（已加密） */
	private String _password;
	/** 最近連線日期 */
	private Timestamp _lastActive;
	/** 最近連線結束日期 */
	private Timestamp _lastlogout;
	/** 存取等級（GM嗎？） */
	private int _accessLevel;
	/** 連線者主機名 */
	private String _host;
	/** 封鎖狀態（True == 被禁止） */
	private int _banned;
	/** 帳號有效狀態（True == 有效） */
	private boolean _isValid = false;
	/** 角色欄位（太古的玉鎖） */
	private int _charslot;
	private boolean _is_changed_slot = false;
	/** 倉庫密碼 */
	private int _GamePassword;
	/** 角色密碼 */
	private String _CharPassword;
	private boolean _iscpwok = Config.Login.CharPassword;
	private byte[] _waitpacket = null;

	public int Ncoin_point;
	public int Shop_open_count;

	/** Buff_網咖 */
	public Timestamp _Buff_PCRoom;
	public int tam_point;
	public Timestamp _lastQuit;

	private int _tam = 0;
	private int _tamStep = 0;

	private int _blessOfAin;
	private int _blessOfAinCharge;
	private int _blessOfAinBonusPoint;

	private int m_fatigue_point;
	private long m_fatigue_point_time;
	private long m_fatigue_start_time;

	/** 用於訊息日誌 */
	private static Logger _log = Logger.getLogger(Account.class.getName());

	public Account() {
		m_fatigue_point = 0;
		m_fatigue_start_time = 0L;
		m_fatigue_point_time = 0L;
	}

	/**
	 * 將密碼加密。
	 *
	 * @param rawPassword
	 *            密碼
	 * @return String
	 * @throws NoSuchAlgorithmException
	 *             無法使用加密算法時
	 * @throws UnsupportedEncodingException
	 *             不支持編碼時
	 */
	@SuppressWarnings("unused")
	private static String encodePassword(final String rawPassword)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] buf = rawPassword.getBytes("UTF-8");
		buf = MessageDigest.getInstance("SHA").digest(buf);
		return Base64.encodeBytes(buf);
	}

	// 檢查永久封禁的IP
	public static String checkIP(String name) {
		String n = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM accounts WHERE login=? ");
			pstm.setString(1, name);
			rs = pstm.executeQuery();

			if (rs.next())
				n = rs.getString("ip");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return n;
	}

	/**
	 * 創建新帳戶
	 *
	 * @param name
	 *            帳戶名
	 * @param rawPassword
	 *            密碼
	 * @param ip
	 *            連接者IP地址
	 * @param host
	 *            連接者主機名
	 * @return Account
	 */
	public static Account create(final String name, final String rawPassword, final String ip, final String host, final String phone) {
		Calendar cal = Calendar.getInstance();
		int hour = Calendar.HOUR;
		int minute = Calendar.MINUTE;
		/** 0 for AM, 1 for PM */
		String amPm = "PM";
		if (cal.get(Calendar.AM_PM) == 0) {
			amPm = "AM";
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			Account account = new Account();
			account._name = name;
			account._password = rawPassword;
			account._ip = ip;
			account._host = host;
			account._banned = 0;
			account._phone = phone;
			account._lastActive = new Timestamp(System.currentTimeMillis());
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "INSERT INTO accounts SET login=?,password=?,lastactive=?,access_level=?,ip=?,host=?,banned=?,charslot=?, gamepassword=?, phone=?, bless_of_ain=?, bless_of_ain_charge=?, bless_of_ain_bonus_point=?, fatigue_point=?, fatigue_start_time=?, fatigue_regeneration_time=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, account._name);
			pstm.setString(2, account._password);
			pstm.setTimestamp(3, account._lastActive);
			pstm.setInt(4, 0);
			pstm.setString(5, account._ip);
			pstm.setString(6, account._host);
			pstm.setInt(7, account._banned);
			pstm.setInt(8, Config.ServerAdSetting.CharactersCharSlot);
			pstm.setInt(9, 0);
			pstm.setString(10, account._phone);
			pstm.setInt(11, 10000000);
			pstm.setInt(12, 0);
			pstm.setInt(13, 0);
			pstm.setInt(14, 0);
			pstm.setInt(15, 0);
			pstm.setInt(16, 0);
			pstm.execute();
			System.out.println("帳戶創建:【" + name + "】 / IP:【" + ip + "】 / 時間:【" + amPm + " " + cal.get(Calendar.HOUR) + "時" + cal.get(Calendar.MINUTE) + "分】");
			return account;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return null;
	}

	/**
	 * 從資料庫中讀取帳戶信息
	 *
	 * @param name
	 *            帳戶名稱
	 * @return Account
	 */
	public static Account load(final String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		Account account = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "SELECT * FROM accounts WHERE login=? LIMIT 1";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				return null;
			}
			account = new Account();
			account._id = rs.getInt("id");
			account._name = rs.getString("login");
			account._password = rs.getString("password");
			account._lastActive = rs.getTimestamp("lastactive");
			account._accessLevel = rs.getInt("access_level");
			account._host = rs.getString("host");
			account._banned = rs.getInt("banned");
			account._charslot = rs.getInt("charslot");
			account._GamePassword = rs.getInt("gamepassword");
			account._phone = rs.getString("phone");
			account.tam_point = rs.getInt("Tam_Point");
			account._Buff_PCRoom = (rs.getTimestamp("BUFF_PCROOM_Time"));

			account._CharPassword = (rs.getString("CharPassword"));

			account.Ncoin_point = (rs.getInt("Ncoin_Point"));
			account.Shop_open_count = (rs.getInt("Shop_open_count"));
			account.setDragonRaid(rs.getTimestamp("raid_buff"));

			account._blessOfAin = rs.getInt("bless_of_ain");
			account._blessOfAinCharge = rs.getInt("bless_of_ain_charge");
			account._blessOfAinBonusPoint = rs.getInt("bless_of_ain_bonus_point");
			account.m_fatigue_point = rs.getInt("fatigue_point");
			account.m_fatigue_start_time = rs.getLong("fatigue_start_time");
			account._lastlogout = rs.getTimestamp("last_log_out");
			account._ShopPassword = rs.getInt("shop_password");
			account._account_pause = rs.getTimestamp("account_pause");
			account._account_pause_reason = rs.getString("account_pause_reason");
			account._attendance_premium = rs.getBoolean("attendance_premium");
			account._attendance_special = rs.getBoolean("attendance_special");
			account._attendance_brave_warrior = rs.getBoolean("attendance_brave_warrior");
			account._attendance_aden_world = rs.getBoolean("attendance_aden_world");
			account._attendance_bravery_medal = rs.getBoolean("attendance_bravery_medal");
/*			account._pc_gaho = rs.getInt("pc_gaho");
			account._pc_gaho_use = rs.getInt("pc_gaho_use");*/
			account._feather_count = rs.getInt("get_feather_count");
			account._total_feather_count = rs.getInt("total_feather_count");
			account._pk_time = rs.getLong("pk_time");
			account._index0_remain_time = rs.getInt("index0_remain_time");
			account._index0_type = rs.getInt("index0_type");
			account._index0_1 = rs.getInt("index0_1");
			account._index0_2 = rs.getInt("index0_2");
			account._index0_3 = rs.getInt("index0_3");
			account._index1_remain_time = rs.getInt("index1_remain_time");
			account._index1_type = rs.getInt("index1_type");
			account._index1_1 = rs.getInt("index1_1");
			account._index1_2 = rs.getInt("index1_2");
			account._index1_3 = rs.getInt("index1_3");

			_log.fine("account exists");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return account;
	}

	/**
	 * 更新資料庫中的最近登入日期
	 *
	 * @param account
	 *            帳戶名稱
	 */
	public static void updateLastActive(final Account account, String ip) {
		Connection con = null;
		PreparedStatement pstm = null;
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET lastactive=?, ip=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setTimestamp(1, ts);
			pstm.setString(2, ip);
			pstm.setString(3, account.getName());
			pstm.execute();
			account._lastActive = ts;
			_log.fine("更新最後活動時間為 " + account.getName());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 計算該帳戶的角色數
	 *
	 * @return result 角色數
	 */
	public int countCharacters() {
		int result = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "SELECT count(*) as cnt FROM characters WHERE account_name=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, _name);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	public void updateNcoin() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET Ncoin_Point=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, Ncoin_point);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void resetShopOpenCount() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET Shop_open_count=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, 0);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void updateShopOpenCount() {
		Shop_open_count++;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET Shop_open_count=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, Shop_open_count);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void ban(final String account, int reason) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET banned=? WHERE login=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, reason);
			pstm.setString(2, account);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 比較輸入的密碼與資料庫中儲存的密碼
	 *
	 * @param rawPassword
	 *            密碼
	 * @return boolean
	 */
	public boolean validatePassword(String accountName, final String rawPassword) {
		try {
			_isValid = _password.equals(rawPassword); // (_password.equals(rawPassword) || checkPassword(accountName, _password, rawPassword));
			return _isValid;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void updatePhone(final Account account) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET phone=? WHERE login=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, account.getphone());
			pstm.setString(2, account.getName());
			pstm.execute();
			account._phone = account.getphone();
			_log.fine("更新電話號碼為 " + account.getName());
		} catch (Exception e) {
			_log.log(Level.SEVERE, "帳戶更新電話號碼時發生錯誤", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 是否為有效帳戶
	 *
	 * @return boolean
	 */
	public boolean isValid() {
		return _isValid;
	}

	public void setValid(boolean is_valid){
		_isValid = is_valid;
	}

	/**
	 * 是否為GM帳戶
	 *
	 * @return boolean
	 */
	public boolean isGameMaster() {
		return 0 < _accessLevel;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name){
		_name = name;
	}

	public String get_Password() {
		return _password;
	}

	public void setPassword(String password){
		_password = password;
	}

	public Timestamp getBuff_PC_Bang() {
		return _Buff_PC_Bang;
	}

	public void setBuff_PC_Bang(Timestamp ts) {
		_Buff_PC_Bang = ts;
	}


	// public void set_Password(String password){
	// this._password = password;
	// }

	public String getCPW() {
		return _CharPassword;
	}

	public void setCPW(String s) {
		_CharPassword = s;
	}

	public void setcpwok(boolean f) {
		_iscpwok = f;
	}

	public boolean iscpwok() {
		return _iscpwok;
	}

	public byte[] getwaitpacket() {
		return _waitpacket;
	}

	public void setwaitpacket(byte[] s) {
		_waitpacket = s;
	}

	public void setIp(String ip) {
		_ip = ip;
	}

	public String getIp() {
		return _ip;
	}

	public Timestamp getLastActive() {
		return _lastActive;
	}

	public void setLastActive(Timestamp ts){
		_lastActive = ts;
	}

	private long _btnTimeHome;

	public long getButtonTimeHome() {
		return _btnTimeHome;
	}

	public void setButtonTimeHome(long i) {
		_btnTimeHome = i;
	}

	private long _btnTimePc;

	public long getButtonTimePc() {
		return _btnTimePc;
	}

	public void setButtonTimePc(long i) {
		_btnTimePc = i;
	}

	/**
	 * 獲取最後的登入日期。
	 */

	public int getAccessLevel() {
		return _accessLevel;
	}
	public void setAccessLevel(int accessLevel){
		_accessLevel = accessLevel;
	}

	public String getHost() {
		return _host;
	}

	public void setHost(String host){
		_host = host;
	}

	public int getBannedCode() {
		return _banned;
	}

	public void setBannedCode(int banned){
		_banned = banned;
	}

	public int getCharSlot() {
		return _charslot;
	}

	public void setCharSlot(int charSlot){
		_charslot = charSlot;
	}

	public void is_changed_slot(boolean is_changed){
		_is_changed_slot = is_changed;
	}
	public boolean is_changed_slot(){
		return _is_changed_slot;
	}

	/**
	 * 獲取聯絡方式。
	 *
	 * @return String
	 */
	private String _phone;

	public String getphone() {
		return _phone;
	}

	public void setphone(String s) {
		_phone = s;
	}

	/**
	 * 網頁密碼相關。
	 *
	 * @param account
	 *            帳戶名稱
	 */
	public void UpdateCharPassword(String pwd) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET CharPassword=? WHERE login=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, pwd);
			pstm.setString(2, getName());
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 設定角色槽數量
	 *
	 * @return boolean
	 */
	public void setCharSlot(GameClient client, int i) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET charslot=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, i);
			pstm.setString(2, client.getAccount().getName());
			pstm.execute();
			client.getAccount()._charslot = i;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static boolean checkLoginIP(String ip) {
		int num = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(ip) as cnt FROM accounts WHERE ip=? ");

			pstm.setString(1, ip);
			rs = pstm.executeQuery();

			if (rs.next())
				num = rs.getInt("cnt");

			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

			if (num < MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT)
				return false;
			else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

	public static boolean checkPassword(String accountName, String _pwd, String rawPassword) {
		String _inputPwd = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT password(?) as pwd ");

			pstm.setString(1, rawPassword);
			rs = pstm.executeQuery();
			if (rs.next()) {
				_inputPwd = rs.getString("pwd");
			}
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
			if (_pwd.equals(_inputPwd)) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

	public static boolean checkLoginBanIP(String ip) {
		int num = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(ip) as cnt FROM ban_ip WHERE ip=?");

			pstm.setString(1, ip);
			rs = pstm.executeQuery();

			if (rs.next()) {
				num = rs.getInt("cnt");
			}

			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

			if (num >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

	/**
	 * 更新網咖資訊
	 */
	public void updatePCRoom() {
		Updator.exec("UPDATE accounts SET BUFF_PCROOM_Time=? WHERE login=?", new Handler() {

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setTimestamp(++idx, _Buff_PCRoom);
				pstm.setString(++idx, _name);
			}
		});
//		Connection con = null;
//		PreparedStatement pstm = null;
//		try {
//			con = L1DatabaseFactory.getInstance().getConnection();
//			pstm = con.prepareStatement("UPDATE accounts SET BUFF_PCROOM_Time=? WHERE login = ?");
//			pstm.setTimestamp(1, _Buff_PCRoom);
//			pstm.setString(2, _name);
//			pstm.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			SQLUtil.close(pstm);
//			SQLUtil.close(con);
//		}
	}

	/**
	 * 倉庫密碼
	 *
	 * @return boolean
	 */
	public static void setGamePassword(GameClient client, int pass) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET gamepassword=? WHERE login =?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, pass);
			pstm.setString(2, client.getAccount().getName());
			pstm.execute();
			client.getAccount()._GamePassword = pass;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void updateExplorationPoints(final Account account) {
		Timestamp accountExpirationDate = _lastQuit;
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());

		long accountLastQuitTime = 0;
		long currentDateMillis = currentDate.getTime();
		long timeDifference = 0;
		if (accountExpirationDate != null) {
			accountLastQuitTime = accountExpirationDate.getTime();
		} else {
			return;
		}
		timeDifference = currentDateMillis - accountLastQuitTime;
		int explorationPointsToAdd = (int) (timeDifference / (60000 * 12));
		if (explorationPointsToAdd < 1) {
			return;
		}
		applyExplorationScore(account, accountLastQuitTime, explorationPointsToAdd);
	}

	public void applyExplorationScore(final Account account, long quitDate, int explorationPointsToAdd) {
		Connection con = null;
		Connection con2 = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		PreparedStatement pstm2 = null;
		Timestamp tamtime = null;
		long sysTime = System.currentTimeMillis();
		int tamcount = Config.ServerRates.TamNum;

		int char_objid = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `characters` WHERE account_name = ?"); // 케릭터 테이블에서군주만골라와서
			pstm.setString(1, account.getName());
			rs = pstm.executeQuery();
			while (rs.next()) {
				tamtime = rs.getTimestamp("TamEndTime");
				char_objid = rs.getInt("objid");
				if (tamtime != null) {
					if (sysTime <= tamtime.getTime()) {
						// 若至今仍在應用中.
						int additionalCount = explorationPointsToAdd;
						tamPoint += additionalCount * tamCount;
						updateTam();
					} else {
						// if(Tam_wait_count(char_objid)!=0){
						int day = Nexttam(char_objid);
						if (day != 0) {
							Timestamp deleteTime = null;
							deleteTime = new Timestamp(sysTime + (86400000 * (long) day) + 10000);// 7天
							try {
								con2 = L1DatabaseFactory.getInstance().getConnection();
								pstm2 = con2.prepareStatement(
										"UPDATE `characters` SET TamEndTime=? WHERE account_name = ? AND objid = ?"); // 從角色表中選擇君主
								pstm2.setTimestamp(1, deleteTime);
								pstm2.setString(2, account.getName());
								pstm2.setInt(3, char_objid);
								pstm2.executeUpdate();
								tamdel(char_objid);
								tamtime = deleteTime;
							}catch(Exception e) {
								e.printStackTrace();
							}finally {
								SQLUtil.close(pstm2, con2);
							}
						}
						// }
						if (quitDate <= tamtime.getTime()) {
							// 目前尚未應用，但在結束日期之後生效.
							int additionalCount = (int) ((tamtime.getTime() - quitDate) / (60000 * 12));
							tamPoint += additionalCount * tamCount;
							updateTam();
						} else {
							// System.out.println("探索時間也在結束日期之前結束。");
						}

						/**/
					}
				} else {
					// System.out.println("沒有探索時間");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm2);
			SQLUtil.close(con2);
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void updateTam() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET Tam_Point=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, tam_point);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int Nexttam(int objectId) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int day = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT day FROM `tam` WHERE objid = ? order by id asc limit 1");
			pstm.setInt(1, objectId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				day = rs.getInt("Day");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return day;
	}

	public void tamdel(int objectId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete from Tam where objid = ? order by id asc limit 1");
			pstm.setInt(1, objectId);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			// e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int getTam() {
		return _tam;
	}

	public int getTamStep() {
		return _tamStep;
	}

	public void updateTamStep(String AccountName, int step) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET tamStep=? WHERE login=?");
			pstm.setInt(1, step);
			pstm.setString(2, AccountName);
			pstm.execute();
			_tamStep = step;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 將最終登入日期反映到資料庫中。
	 *
	 * @param account
	 *            帳戶
	 */

	// 將探索賬戶信息保存並從探索商店加載數量
	public int getTamPoint() {
		return tam_point;
	}

	public int setTamPoint(int tampoint) {
		return tam_point = tampoint;
	}

	public int addTamPoint(int tampoint) {
		return tam_point += tampoint;
	}

	public int getGamePassword() {
		return _GamePassword;
	}

	public void setGamePassword(int gamePassword){
		_GamePassword = gamePassword;
	}
	private Timestamp _raidBuff;

	public Timestamp getDragonRaid() {
		return _raidBuff;
	}

	public void setDragonRaid(Timestamp ts) {
		_raidBuff = ts;
	}

	public int get_ein_level() {
		return SC_REST_EXP_INFO_NOTI.get_ein_level(_blessOfAin / SC_REST_EXP_INFO_NOTI.EINHASAD_UNIT);
	}

	/**
	 * 10000000 1000%<br>
	 * 1000000 100% <br>
	 * 100000 10% <br>
	 * 10000 1% <br>
	 * 1000 0.1%<br>
	 */
	public int getBlessOfAin() {
		return this._blessOfAin;
	}


	public long _pk_time;
	public long get_Pk_Time() {
		return _pk_time;
	}
	public void set_Pk_Time(long t) {
		_pk_time = t;
		update_Pk_Time(t);
	}

	public void update_Pk_Time(long t) {
		Updator.exec("update accounts set pk_time=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setLong(++idx, t);
				pstm.setString(++idx, getName());
			}
		});
	}


	public int _total_feather_count;

	/*public void setTotalFeatherCount(int i) {
		_total_feather_count = i;
	}

	public void addTotalFeatherCount(int i) {
		_total_feather_count += i;
		updateTotalFeatherCount(_total_feather_count);
	}

	public void useTotalFeatherCount(int i) {
		_total_feather_count -= i;
		updateTotalFeatherCount(_total_feather_count);
	}

	public int getTotalFeatherCount() {
		return _total_feather_count;
	}

	public void updateTotalFeatherCount(int i) {
		Updator.exec("update accounts set total_feather_count=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, i);
				pstm.setString(++idx, getName());
			}
		});
	}*/


	public void setBlessOfAin(int i, L1PcInstance pc) {
//		int old_level = get_ein_level();

		this._blessOfAin = i;
		if(pc == null)
			return;

		if (getBlessOfAin() < 10000) {
			update_ein_effect(pc, false);
		} else {
			update_ein_effect(pc, true);
		}
		//update_ein_effect(pc, old_level, -1);
//		this._blessOfAin = i;
//		if(pc == null)
//			return;

		//update_ein_effect(pc, get_ein_level(), 1);

		SC_REST_EXP_INFO_NOTI.send(pc);
	}

	public int getBlessOfAinCharge() {
		return this._blessOfAinCharge;
	}

	public void setBlessOfAinCharge(int i) {
		this._blessOfAinCharge = i;
		updateBlessOfAin();
	}
	// 龍之祝福效果
//	private static final HashMap<Integer, EinhasadEffectInfo> einhasadEffects;
//	static {
//		einhasadEffects = new HashMap<>();
//		einhasadEffects.put(2, new EinhasadEffectInfo(2, 5, 5, 100, 4337, 9596, 6963));
//		einhasadEffects.put(3, new EinhasadEffectInfo(3, 5, 5, 200, 4338, 9597, 6964));
//		einhasadEffects.put(4, new EinhasadEffectInfo(4, 5, 5, 500, 4339, 9598, 6965));
//	}

	public int getBlessOfAinBonusPoint() {
		return this._blessOfAinBonusPoint;
	}

	public void setBlessOfAinBonusPoint(int i) {
		_blessOfAinBonusPoint = i;
	}

	public void addBlessOfAinBonusPoint(int i) {
		//TODO 每1%艾音將增加3點.
		//int point = (i * Config.ServerRates.AinBonusPoint_Monster) / 10000;
//		System.out.println("_blessOfAinBonusPoint:" +_blessOfAinBonusPoint + "/ i:" +i);
		_blessOfAinBonusPoint += i * Config.ServerRates.AinBonusPoint_Monster;
	}

	public void addGmBlessOfAinBonusPoint(int i) {
		_blessOfAinBonusPoint += i;
	}

	public void minusBlessOfAinBonusPoint(int i) {
		int point = i;
		_blessOfAinBonusPoint -= point;
	}

	private static class EinhasadEffectInfo{
		public int damageReduction;
		public int addSpecialResistance;
		public int calcPcDefense;
		public int weightReduction;
		public int spellId;
		public int buffIconId;
		public int buffTooltipId;
		EinhasadEffectInfo(int damageReduction, int addSpecialResistance, int calcPcDefense, int weightReduction, int spellId, int buffIconId, int buffTooltipId){
			this.damageReduction = damageReduction;
			this.addSpecialResistance = addSpecialResistance;
			this.calcPcDefense = calcPcDefense;
			this.weightReduction = weightReduction;
			this.spellId = spellId;
			this.buffIconId = buffIconId;
			this.buffTooltipId = buffTooltipId;
		}
	}
	public void update_ein_effect(L1PcInstance pc, boolean onOff) {
		EinhasadEffectInfo eInfo = new EinhasadEffectInfo(4, 5, 5, 500, 4339, 9598, 6965);
		if (onOff) { // 給予增益效果時.
			if (!pc.hasSkillEffect(4339)) {
				pc.addDamageReductionByArmor(eInfo.damageReduction);
				pc.addSpecialResistance(eKind.ALL, eInfo.addSpecialResistance);
				pc.getResistance().addcalcPcDefense(eInfo.calcPcDefense);
				pc.addWeightReduction(eInfo.weightReduction);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_spell_id(eInfo.spellId);
				noti.set_duration(-1);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_EINHASAD_FAVOR);
				noti.set_on_icon_id(eInfo.buffIconId);
				noti.set_off_icon_id(eInfo.buffIconId);
				noti.set_icon_priority(3);
				noti.set_tooltip_str_id(eInfo.buffTooltipId);
				noti.set_new_str_id(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
				pc.setSkillEffect(eInfo.spellId, -1);
			}
		} else { // 移除增益效果時.
			if (pc.hasSkillEffect(4339)) {
				pc.addDamageReductionByArmor(-eInfo.damageReduction);
				pc.addSpecialResistance(eKind.ALL, -eInfo.addSpecialResistance);
				pc.getResistance().addcalcPcDefense(-eInfo.calcPcDefense);
				pc.addWeightReduction(-eInfo.weightReduction);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(eInfo.spellId);
				noti.set_duration(0);
				noti.set_off_icon_id(eInfo.buffIconId);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				pc.removeSkillEffect(eInfo.spellId);
			}
		}
	}
	// 龍之祝福效果條件
//	public void update_ein_effect(L1PcInstance pc, int ein_level, int mul) {
//		if (ein_level == 1) { // (附註) 如有變更，請注釋處理..
//			ein_level = 4;
//		}
//		/*if (pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
//			System.out.println("如果獲得網咖增益效果，是否返回?");
//			return;
//		}*/
////		if(!einhasadEffects.containsKey(ein_level)) {
////			return;
////		}
//		if (!pc.hasSkillEffect(4339)) {
//
//		}
//
//		//System.out.println("mul(1): " +mul);
//		//EinhasadEffectInfo eInfo = einhasadEffects.get(ein_level);
//		EinhasadEffectInfo eInfo = new EinhasadEffectInfo(4, 5, 5, 500, 4339, 9598, 6965)
//		//System.out.println("mul(2): " +mul);
//
//		// on
//		if(mul >= 0) {
//			pc.addDamageReductionByArmor(eInfo.damageReduction);
//			pc.addSpecialResistance(eKind.ALL, eInfo.addSpecialResistance);
//			pc.getResistance().addcalcPcDefense(eInfo.calcPcDefense);
//			pc.addWeightReduction(eInfo.weightReduction);
//			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
//			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
//			noti.set_noti_type(eNotiType.RESTAT);
//			noti.set_spell_id(eInfo.spellId);
//			noti.set_duration(-1);
//			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_EINHASAD_FAVOR);
//			noti.set_on_icon_id(eInfo.buffIconId);
//			noti.set_off_icon_id(eInfo.buffIconId);
//			noti.set_icon_priority(3);
//			noti.set_tooltip_str_id(eInfo.buffTooltipId);
//			noti.set_new_str_id(0);
//			noti.set_end_str_id(0);
//			noti.set_is_good(true);
//			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
//			pc.setSkillEffect(eInfo.spellId, -1);
//		// off
//		}else {
//			pc.addDamageReductionByArmor(-eInfo.damageReduction);
//			pc.addSpecialResistance(eKind.ALL, -eInfo.addSpecialResistance);
//			pc.getResistance().addcalcPcDefense(-eInfo.calcPcDefense);
//			pc.addWeightReduction(-eInfo.weightReduction);
//			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
//			noti.set_noti_type(eNotiType.END);
//			noti.set_spell_id(eInfo.spellId);
//			noti.set_duration(0);
//			noti.set_off_icon_id(eInfo.buffIconId);
//			noti.set_end_str_id(0);
//			noti.set_is_good(true);
//			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
//			pc.removeSkillEffect(eInfo.spellId);
//		}
//	}

	public void addBlessOfAin(int val, L1PcInstance pc, String rewardType) {
		switch (rewardType) {
			case "出席": case "怪物": case "圖鑑": case "推送": {
				addBlessOfAin(val, pc, eEinhasadBonusType.BonusNone);
				SC_REST_GAUGE_CHARGE_NOTI.send_bonus_guage(pc, val / 10000);
				//pc.sendPackets(new S_ACTION_UI(val / 10000)); // 可能是阿因增益效果？
				break;
			}
			case "祝福": break;
		}
	}

	public void addBlessOfAin(int val, L1PcInstance pc) { // 日後需要修改時，請根據上方類型進行添加和修改..
		addBlessOfAin(val, pc, eEinhasadBonusType.SectionBonus);
//		SC_REST_GAUGE_CHARGE_NOTI.send_bonus_guage(pc, val / 10000);// 阿因效果原型（透過食用道具提升阿因時）
	}

	public void addBlessOfAin(int val, L1PcInstance pc, eEinhasadBonusType type) {
		if (val < 0) {
			if (pc.hasSkillEffect(L1SkillId.EINHASAD_PRIMIUM_FLAT)) {
				pc.addVirualEinhasad(Math.abs(val));
				int virualEinhasadPer = Math.abs(IntRange.getTotalValueRint(pc.getVirualEinhasad() / 10000));
				if (virualEinhasadPer > 0) {
					pc.setVirualEinhasad(0);
					// pc.addVirualEinhasad(-10000 * virualEinhasadPer);
					this.addBlessOfAinBonusPoint(virualEinhasadPer); // 獎勵點數
					SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());

					L1Clan clan = pc.getClan();
					if (clan != null) {
						if (pc.getClanContribution() < 10000000) {
							pc.addClanContribution(virualEinhasadPer);
							clan.addContribution(virualEinhasadPer);
							ClanTable.getInstance().updateContribution(clan.getClanId(), clan.getContribution());
							pc.save();

							/*clan.createOnlineMembers().forEach((L1Clan.ClanMember m) -> {
								if (m.player != null) {
									SC_BLOOD_PLEDGE_CONTRIBUTION_ACK.clan_contribution_send(m.player);
								}
							});*/
						}
					}
				}
			} else {
				int oldEinPointByper = Math.abs(IntRange.getTotalValueRint(_blessOfAin / 10000));
				int newEinPointByPer = Math.abs(IntRange.getTotalValueRint((_blessOfAin + val) / 10000));
				if (oldEinPointByper != newEinPointByPer) {
					this.addBlessOfAinBonusPoint(oldEinPointByper - newEinPointByPer); // 獎勵點數
					SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());

					L1Clan clan = pc.getClan();
					if (clan != null) {
						if (pc.getClanContribution() < 10000000) {
							pc.addClanContribution(oldEinPointByper - newEinPointByPer);
							clan.addContribution(oldEinPointByper - newEinPointByPer);
							ClanTable.getInstance().updateContribution(clan.getClanId(), clan.getContribution());
							pc.save();

							/*clan.createOnlineMembers().forEach((L1Clan.ClanMember m) -> {
								if (m.player != null) {
									SC_BLOOD_PLEDGE_CONTRIBUTION_ACK.clan_contribution_send(m.player);
								}
							});*/
						}
					}
				}
			}
		}

		if(val < 0 && pc.hasSkillEffect(L1SkillId.EINHASAD_PRIMIUM_FLAT)) {
			//TODO 在阿因定額制中，釣魚效果
			if(pc.getMapId() != 5490) {
				return;
			}
		}

		/*if(i < 0 && _blessOfAin <= 10000000 && pc.hasSkillEffect(L1SkillId.EINHASAD_GREAT_FLAT)) {
			//TODO 在阿因定額制中，釣魚效果
			if(pc.getMapId() != 5490) {
				return;
			}
		}*/

		//int old_level = get_ein_level();
		this._blessOfAin = IntRange.ensure(_blessOfAin + val, 0, SC_REST_EXP_INFO_NOTI.EINHASAD_LIMIT);

		if (getBlessOfAin() < 10000) {
			update_ein_effect(pc, false);
		} else {
			update_ein_effect(pc, true);
		}
//		int new_level = get_ein_level();
//		if(old_level != new_level) {
//			update_ein_effect(pc, old_level, -1);
//			update_ein_effect(pc, new_level, 1);
//		}

//		if(_blessOfAin > 10000) {
//			SC_REST_EXP_INFO_NOTI.send(pc);
//		}
		SC_REST_EXP_INFO_NOTI.send(pc, type);

		if (pc.hasSkillEffect(EXP_BUFF)) {
			int skillTime = pc.getSkillEffectTimeSec(EXP_BUFF);
			if (_blessOfAin < 10000) {
				pc.sendPackets(S_InventoryIcon.iconNewUnLimit(EXP_BUFF + 1, 5087, true));
			} else {
				pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.EXP_BUFF + 1));
				pc.sendPackets(S_InventoryIcon.icoNew(EXP_BUFF, 5087, skillTime, true));
			}
		}
	}

	public void updateBlessOfAin() {
		Updator.exec("update accounts set bless_of_ain=?, bless_of_ain_charge=?, bless_of_ain_bonus_point=?, fatigue_point=?, fatigue_start_time=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, _blessOfAin);
				pstm.setInt(++idx, _blessOfAinCharge);
				pstm.setInt(++idx, _blessOfAinBonusPoint);
				pstm.setInt(++idx, m_fatigue_point);
				pstm.setLong(++idx, m_fatigue_start_time);
				pstm.setString(++idx, getName());
			}
		});
	}

	public boolean has_fatigue() {
		return FatigueProperty.getInstance().use_fatigue() && remain_fatigue() > 0L;
	}

	public long remain_fatigue() {
		return (m_fatigue_start_time + FatigueProperty.getInstance().get_fatigue_effect_millis()) - System.currentTimeMillis();
	}

	public void send_fatigue(L1PcInstance pc) {
		FatigueProperty p = FatigueProperty.getInstance();
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.RESTAT);
		noti.set_spell_id(p.get_fatigue_buff_icon_id());
		noti.set_duration((int)(remain_fatigue() / 1000));
		noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
		noti.set_on_icon_id(p.get_fatigue_buff_icon_id());
		noti.set_off_icon_id(p.get_fatigue_buff_icon_id());
		noti.set_icon_priority(2);
		noti.set_tooltip_str_id(3215);
		noti.set_new_str_id(3215);
		noti.set_is_good(false);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		pc.sendPackets(String.format("格蘭肯懲罰:經驗值獲得 -%d%% / 金幣掉落 -%d%% / 減少 -%d%%",
				(int)(p.get_fatigue_effect_exp() * 100D), (int)((1.0 - p.get_fatigue_effect_adena()) * 100D), (int)(p.get_fatigue_effect_reduction() * 100D)));
	}

	public void apply_fatigue(L1PcInstance pc) {
		m_fatigue_start_time = System.currentTimeMillis();
		m_fatigue_point = 0;
		update_fatigue_info();
		send_fatigue(pc);
	}

	public void initialize_fatigue_info(L1PcInstance pc) {
		done_fatigue(pc);
		m_fatigue_point = 0;
		update_fatigue_info();
	}

	public void done_fatigue(L1PcInstance pc) {
		FatigueProperty p = FatigueProperty.getInstance();
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.END);
		noti.set_spell_id(p.get_fatigue_buff_icon_id());
		noti.set_duration(0);
		noti.set_off_icon_id(p.get_fatigue_buff_icon_id());
		noti.set_end_str_id(0);
		noti.set_is_good(true);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		pc.sendPackets("格蘭肯的憤怒已解除。");
		m_fatigue_start_time = 0L;
	}

	public synchronized int inc_fatigue_point(L1PcInstance pc) {
		if (!FatigueProperty.getInstance().use_fatigue())
			return 0;

		//if(getBlessOfAin() <= 0) {
		long current_millis = System.currentTimeMillis();
		if(current_millis < m_fatigue_point_time + FatigueProperty.getInstance().get_fatigue_point_stackable_millis()){
			return 0;
		}

		m_fatigue_point_time = current_millis;

		if (inc_fatigue_point() >= FatigueProperty.getInstance().get_fatigue_point_limit()) {
			apply_fatigue(pc);
		} /*else {
			if (m_fatigue_point % 100 == 0)
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						String.format("格蘭肯點數為 %,d。", m_fatigue_point)));
		}*/
		//}

		return m_fatigue_point;
	}

	public void read_fatigue_resultset(ResultSet rs) throws SQLException {
		m_fatigue_point = rs.getInt("fatigue_point");
		m_fatigue_start_time = rs.getLong("fatigue_start_time");
	}

	// 阿因哈薩德祝福的各階段格蘭肯點數每分鐘增加數值
	public int inc_fatigue_point() {
		if (has_fatigue()) {
			return 0;
		}
		if (this.get_ein_level() == 4) {
			return m_fatigue_point += 1;
		} else if (this.get_ein_level() == 3) {
			return m_fatigue_point += 2;
		} else if (this.get_ein_level() == 2) {
			return m_fatigue_point += 3;
		} else if (this.get_ein_level() == 1) {
			return m_fatigue_point += 4;
		} else if (this.get_ein_level() <= 0) {
			return m_fatigue_point += 5;
		}
		return ++m_fatigue_point;
	}

	public int get_fatigue_point() {
		return m_fatigue_point;
	}

	public int add_fatigue_point(int fatigue) {
		return m_fatigue_point += fatigue;
	}

	public long get_fatigue_start_time() {
		return m_fatigue_start_time;
	}

	public long add_fatigue_start_time(long time) {
		return m_fatigue_start_time += time;
	}

	public static void initialize_fatigue() {
		Updator.exec("update accounts set fatigue_point=?, fatigue_start_time=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, 0);
				pstm.setLong(++idx, 0);
			}
		});
	}

	public void update_fatigue_info() {
		Updator.exec("update accounts set fatigue_point=?, fatigue_start_time=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, m_fatigue_point);
				pstm.setLong(++idx, m_fatigue_start_time);
				pstm.setString(++idx, getName());
			}
		});
	}

	public Timestamp getLastLogOut() {
		return _lastlogout;
	}

	public void setLastLogOut(Timestamp ts) {
		_lastlogout = ts;
	}

	public void updateLastLogOut() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			_lastlogout = new Timestamp(System.currentTimeMillis());
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET last_log_out=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			Timestamp time = new Timestamp(System.currentTimeMillis());
			pstm.setTimestamp(1, time);
			pstm.setString(2, getName());
			pstm.execute();

			_lastlogout = time;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int getAccountId() {
		return _id;
	}

	public void setAccountId(int i) {
		_id = i;
	}

	public static Integer[] loadAccountAddress(String accountName){
		MJObjectWrapper<Integer[]> wrapper = new MJObjectWrapper<Integer[]>();
		Selector.exec("select ip from accounts where login=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, accountName);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()){
					try{
						String addressValues = rs.getString("ip");
						StringTokenizer st = new StringTokenizer(addressValues, ".");

						String ip1 = st.nextToken();
						String ip2 = st.nextToken();
						String ip3 = st.nextToken();
						Integer[] itg = new Integer[]{
								Integer.parseInt(ip1),
								Integer.parseInt(ip2),
								Integer.parseInt(ip3),
						};
						wrapper.value = itg;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		return wrapper.value;
	}

	public void UpdateShopPassword() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET shop_password=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, getShopPassword());
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private int _ShopPassword;

	public int getShopPassword() {
		return _ShopPassword;
	}

	public void setShopPassword(int s) {
		_ShopPassword = s;
	}

	public Timestamp _account_pause;

	public Timestamp getAccountPause() {
		return _account_pause;
	}

	public void setAccountPause(Timestamp ts) {
		_account_pause = ts;
	}

	private String _account_pause_reason;

	public String getAccountPauseReason() {
		return _account_pause_reason;
	}

	public void setAccountPauseReason(String s) {
		_account_pause_reason = s;
	}

	private boolean _attendance_premium;

	public boolean getAttendance_Premium( ) {
		return _attendance_premium;
	}

	public void setAttendance_Premium(boolean value) {
		_attendance_premium = value;

		Updator.exec("update accounts set attendance_premium=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, value ? "true" : "false");
				pstm.setString(++idx, getName());
			}
		});
	}

	private boolean _attendance_special;

	public boolean getAttendance_Special() {
		return _attendance_special;
	}

	public void setAttendance_Special(boolean value) {
		_attendance_special = value;

		Updator.exec("update accounts set attendance_special=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, value ? "true" : "false");
				pstm.setString(++idx, getName());
			}
		});
	}


	private boolean _attendance_brave_warrior;

	public boolean getAttendance_Brave_Warrior() {
		return _attendance_brave_warrior;
	}

	public void setAttendance_Brave_Warrior(boolean value) {
		_attendance_brave_warrior = value;

		Updator.exec("update accounts set attendance_brave_warrior=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, value ? "true" : "false");
				pstm.setString(++idx, getName());
			}
		});
	}

	private boolean _attendance_aden_world;

	public boolean getAttendance_Aden_World() {
		return _attendance_aden_world;
	}

	public void setAttendance_Aden_World(boolean value) {
		_attendance_aden_world = value;

		Updator.exec("update accounts set attendance_aden_world=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, value ? "true" : "false");
				pstm.setString(++idx, getName());
			}
		});
	}

	private boolean _attendance_bravery_medal;

	public boolean getAttendance_Bravery_Medal() {
		return _attendance_bravery_medal;
	}

	public void setAttendance_Bravery_Medal(boolean value) {
		_attendance_bravery_medal = value;

		Updator.exec("update accounts set attendance_bravery_medal=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, value ? "true" : "false");
				pstm.setString(++idx, getName());
			}
		});
	}

/*	private int _pc_gaho;
	public int getPcGaho() {
		return _pc_gaho;
	}
	public void usePcGaho(int i) {
		_pc_gaho -= i;
		Updator.exec("update accounts set pc_gaho=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, _pc_gaho);
				pstm.setString(++idx, getName());
			}
		});
	}
	public void setPcGaho(int i) {
		_pc_gaho = i;
		Updator.exec("update accounts set pc_gaho=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, i);
				pstm.setString(++idx, getName());
			}
		});
	}

	private int _pc_gaho_use;
	public int getPcGahoUse() {
		return _pc_gaho_use;
	}
	public void addPcGahoUse(int i) {
		_pc_gaho_use += i;
		Updator.exec("update accounts set pc_gaho_use=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, _pc_gaho_use);
				pstm.setString(++idx, getName());
			}
		});
	}
	public void setPcGahoUse(int i) {
		_pc_gaho_use = i;
		Updator.exec("update accounts set pc_gaho_use=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, i);
				pstm.setString(++idx, getName());
			}
		});
	}
	*/

	private int _feather_count;
/*	public int getFeatherCount() {
		return _feather_count;
	}
	public void addFeatherCount(int i) {
		_feather_count += i;
		Updator.exec("update accounts set get_feather_count=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, _feather_count);
				pstm.setString(++idx, getName());
			}
		});
	}
	public void setFeatherCount(int i) {
		_feather_count = i;
		Updator.exec("update accounts set get_feather_count=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, i);
				pstm.setString(++idx, getName());
			}
		});
	}
	*/


	private String account_posess_slotkey_charname;

	public String is_posess_slotkey_charname() {
		return account_posess_slotkey_charname;
	}

	public void set_posess_slotkey_charname(String key) {
		account_posess_slotkey_charname = key;
	}

	public void get_slot_char(String name){
		Selector.exec("select objid from characters where account_name=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, name);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					search_slot_char(rs.getInt("objid"), 210083);
				}
			}
		});
	}

	public void search_slot_char(int objid, int itemid){
		Selector.exec("select * from character_items where char_id=? and item_id=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, objid);
				pstm.setInt(2, itemid);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next() && is_posess_slotkey_charname() == null){
					set_posess_slotkey_charname("dummy");
					Delete_key(objid, itemid);
				}
			}
		});
	}

	public void Delete_key(int objid, int itemid){
		Updator.exec("delete from character_items where char_id=? and item_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, objid);
				pstm.setInt(++idx, itemid);
			}
		});
	}
	/** 阿因哈薩德每日獎勵 **/
	private int _eindaybonus;

	public int getEinDayBonus() {
		return _eindaybonus;
	}

	public void setEinDayBonus(int i) {
		_eindaybonus = i;
	}
	/** PC房改造第二階段黃金增益相關 **/
	private int _index0_remain_time;
	//	private int _index0_remain_time_sec;
	private int _index0_1;
	private int _index0_2;
	private int _index0_3;
	private int _index1_remain_time;
	//	private int _index1_remain_time_sec;
	private int _index1_1;
	private int _index1_2;
	private int _index1_3;
	private int _index0_type;
	private int _index1_type;

	public int get_Index0_Remain_Time() {
		return _index0_remain_time;
	}


	public int get_Index1_Remain_Time() {
		return _index1_remain_time;
	}
	public int get_Index0_type(L1PcInstance pc) {
		if (_index0_type == 0) {
			int type = pc.getType();
			switch(type){
				case 2: // 妖精
					_index0_type = 2;
					break;
				case 3: // 法師
				case 6: // 幻術師
					_index0_type = 3;
					break;
				default:
					_index0_type = 1;
					break;
			}
		}
		return _index0_type;
	}
	public void set_Index0_type(int i) {
		_index0_type = i;
		update_Index_type(0, i);
	}

	public int get_Index0_1() {
		return _index0_1;
	}
	public int get_Index0_2() {
		return _index0_2;
	}
	public int get_Index0_3() {
		return _index0_3;
	}

	public int get_Index1_type(L1PcInstance pc) {
		if (_index1_type == 0) {
			int type = pc.getType();
			switch(type){
				case 2: // 妖精
					_index1_type = 2;
					break;
				case 3: // 法師
				case 6: // 幻術師
					_index1_type = 3;
					break;
				default:
					_index1_type = 1;
					break;
			}
		}
		return _index1_type;
	}
	public void set_Index1_type(int i) {
		_index1_type = i;
		update_Index_type(1, i);
	}

	public void update_Index_type(int index, int type) {
		String name ="";
		if (index == 0) {
			name = "index0_type";
		} else if (index == 1) {
			name = "index1_type";
		}
		if (name == "") {
			System.out.println("網咖黃金增益類型保存錯誤");
			return;
		}
		Updator.exec("update accounts set "+name+"=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, type);
				pstm.setString(++idx, getName());
			}
		});
	}

	public int get_Index1_1() {
		return _index1_1;
	}
	public int get_Index1_2() {
		return _index1_2;
	}
	public int get_Index1_3() {
		return _index1_3;
	}

	public void set_Index(int group, int index, int grade) {
		update_Index_value(group, index, grade);
	}
	public void set_Index0_1(int i) {
		_index0_1 = i;
	}

	public void set_Index0_2(int i) {
		_index0_2 = i;
	}
	public void set_Index0_3(int i) {
		_index0_3 = i;
	}
	public void set_Index1_1(int i) {
		_index1_1 = i;
	}
	public void set_Index1_2(int i) {
		_index1_2 = i;
	}
	public void set_Index1_3(int i) {
		_index1_3 = i;
	}
	public void del_Index0_1() {
		_index0_1 = 0;
		update_Index_value(0, 1, _index0_1);
	}
	public void del_Index0_2() {
		_index0_2 = 0;
		update_Index_value(0, 2, _index0_2);
	}
	public void del_Index0_3() {
		_index0_3 = 0;
		update_Index_value(0, 3, _index0_3);
	}
	public void del_Index1_1() {
		_index1_1 = 0;
		update_Index_value(1, 1, _index1_1);
	}
	public void del_Index1_2() {
		_index1_2 = 0;
		update_Index_value(1, 2, _index1_2);
	}
	public void del_Index1_3() {
		_index1_3 = 0;
		update_Index_value(1, 3, _index1_3);
	}
	public void del_Index0_Remain_Time() {
		_index0_remain_time = 0;
		update_Index0_Remain_Time();
	}
	public void del_Index1_Remain_Time() {
		_index1_remain_time = 0;
		update_Index1_Remain_Time();
	}


	public void update_Index_value(int group, int index, int grade) {
		String name = "";

		if (group == 0) {
			switch(index) {
				case 1:
					name = "index0_1";
					set_Index0_1(grade);
					break;
				case 2:
					name = "index0_2";
					set_Index0_2(grade);
					break;
				case 3:
					name = "index0_3";
					set_Index0_3(grade);
					break;
			}
		} else if (group == 1) {
			switch(index) {
				case 1:
					name = "index1_1";
					set_Index1_1(grade);
					break;
				case 2:
					name = "index1_2";
					set_Index1_2(grade);
					break;
				case 3:
					name = "index1_3";
					set_Index1_3(grade);
					break;
			}
		}
		if (name == "") {
			System.out.println("網咖黃金增益錯誤");
			return;
		}

		Updator.exec("update accounts set "+name+"=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, grade);
				pstm.setString(++idx, getName());
			}
		});
	}

	public void add_Index0_Time(int i) {
		_index0_remain_time += i;
		update_Index0_Remain_Time();
	}
	public void use_Index0_Time(int i) {
		_index0_remain_time -= i;
	}

	public void update_Index0_Remain_Time() {
		Updator.exec("update accounts set index0_remain_time=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, _index0_remain_time);
				pstm.setString(++idx, getName());
			}
		});
	}

	public void add_Index1_Time(int i) {
		_index1_remain_time += i;
		update_Index1_Remain_Time();
	}
	public void use_Index1_Time(int i) {
		_index1_remain_time -= i;
	}

	public void update_Index1_Remain_Time() {
		Updator.exec("update accounts set index1_remain_time=? where login=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, _index1_remain_time);
				pstm.setString(++idx, getName());
			}
		});
	}

}
