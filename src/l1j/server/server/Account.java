package l1j.server.server;

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
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eEinhasadBonusType;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REST_GAUGE_CHARGE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_POINT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.SQLUtil;

public class Account {
  private int _id;
  private String _name;
  private String _ip;
  private String _password;
  private Timestamp _lastActive;
  private Timestamp _lastlogout;
  private int _accessLevel;
  private String _host;
  private int _banned;
  private boolean _isValid = false;
  private int _charslot;
  private boolean _is_changed_slot = false;
  private int _GamePassword;
  private String _CharPassword;
  private boolean _iscpwok = Config.Login.CharPassword;
  private byte[] _waitpacket = null;

  public int Ncoin_point;

  public int Shop_open_count;

  public Timestamp _Buff_網咖;

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
  private long _btnTimeHome;
  private long _btnTimePc;
  private String _phone;
  private Timestamp _raidBuff;
  public long _pk_time;
  public int _total_feather_count;
  private int _ShopPassword;
  private static Logger _log = Logger.getLogger(Account.class.getName());
  public Timestamp _account_pause;
  private String _account_pause_reason;
  private boolean _attendance_premium;

  public Account() {
    this.m_fatigue_point = 0;
    this.m_fatigue_start_time = 0L;
    this.m_fatigue_point_time = 0L;
  }

  private boolean _attendance_special;
  private boolean _attendance_brave_warrior;
  private boolean _attendance_aden_world;
  private boolean _attendance_bravery_medal;
  private int _feather_count;
  private String account_posess_slotkey_charname;
  private int _eindaybonus;
  private int _index0_remain_time;
  private int _index0_1;
  private int _index0_2;
  private int _index0_3;
  private int _index1_remain_time;
  private int _index1_1;
  private int _index1_2;
  private int _index1_3;
  private int _index0_type;
  private int _index1_type;

  private static String encodePassword(String rawPassword)
      throws NoSuchAlgorithmException, UnsupportedEncodingException {
    byte[] buf = rawPassword.getBytes("UTF-8");
    buf = MessageDigest.getInstance("SHA").digest(buf);
    return Base64.encodeBytes(buf);
  }

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

      if (rs.next()) {
        n = rs.getString("ip");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      SQLUtil.close(rs);
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
    return n;
  }

  public static Account create(String name, String rawPassword, String ip, String host, String phone) {
    Calendar cal = Calendar.getInstance();
    int hour = cal.get(Calendar.HOUR); // 獲取當前小時
    int minute = cal.get(Calendar.MINUTE); // 獲取當前分鐘
    String period = "下午";
    if (cal.get(Calendar.AM_PM) == Calendar.AM) {
      period = "上午";
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
      String sqlstr = "INSERT INTO accounts(login, password, lastactive, access_level, ip, host, banned, charslot, gamepassword, phone, bless_of_ain, bless_of_ain_charge, bless_of_ain_bonus_point, fatigue_point, fatigue_start_time, fatigue_regeneration_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
      System.out.println("帳號創建:【" + name + "】 / 登入IP地址:【" + ip + "】 / 時間:【" + period + " " + cal.get(Calendar.HOUR_OF_DAY) + "時" + cal.get(Calendar.MINUTE) + "分】");
      return account;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
    return null;
  }

  public static Account load(String name) {
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
      account._Buff_網咖 = rs.getTimestamp("BUFF_PCROOM_Time");

      account._CharPassword = rs.getString("CharPassword");

      account.Ncoin_point = rs.getInt("Ncoin_Point");
      account.Shop_open_count = rs.getInt("Shop_open_count");
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

  public static void updateLastActive(Account account, String ip) {
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
      _log.fine("update lastactive for " + account.getName());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
  }

  public int countCharacters() {
    int result = 0;
    Connection con = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    try {
      con = L1DatabaseFactory.getInstance().getConnection();
      String sqlstr = "SELECT count(*) as cnt FROM characters WHERE account_name=?";
      pstm = con.prepareStatement(sqlstr);
      pstm.setString(1, this._name);
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
      pstm.setInt(1, this.Ncoin_point);
      pstm.setString(2, this._name);
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
    this.Shop_open_count++;
    Connection con = null;
    PreparedStatement pstm = null;
    try {
      con = L1DatabaseFactory.getInstance().getConnection();
      String sqlstr = "UPDATE accounts SET Shop_open_count=? WHERE login = ?";
      pstm = con.prepareStatement(sqlstr);
      pstm.setInt(1, this.Shop_open_count);
      pstm.setString(2, this._name);
      pstm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
  }

  public static void ban(String account, int reason) {
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

  public boolean validatePassword(String accountName, String rawPassword) {
    try {
      this._isValid = this._password.equals(rawPassword);
      return this._isValid;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  public static void updatePhone(Account account) {
    Connection con = null;
    PreparedStatement pstm = null;
    try {
      // 獲取數據庫連接
      con = L1DatabaseFactory.getInstance().getConnection();

      // SQL語句，用於更新賬號的電話號碼
      String sqlstr = "UPDATE accounts SET phone=? WHERE login=?";

      // 準備SQL語句
      pstm = con.prepareStatement(sqlstr);

      // 設置SQL語句中的參數
      pstm.setString(1, account.getphone()); // 設置第一個參數為賬號的電話號碼
      pstm.setString(2, account.getName()); // 設置第二個參數為賬號的登錄名稱

      // 執行SQL語句
      pstm.execute();

      // 更新賬號對象中的電話號碼
      account._phone = account.getphone();

      // 記錄日誌，表示電話號碼更新成功
      _log.fine("update phone for " + account.getName());
    } catch (Exception e) {
      // 當發生異常時，記錄日誌
      _log.log(Level.SEVERE, "accounts updatePhone 異常處理", e);
    } finally {
      // 關閉PreparedStatement和Connection對象，釋放資源
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
  }

  public boolean isValid() {
    return this._isValid;
  }

  public void setValid(boolean is_valid) {
    this._isValid = is_valid;
  }

  public boolean isGameMaster() {
    return (0 < this._accessLevel);
  }

  public String getName() {
    return this._name;
  }

  public void setName(String name) {
    this._name = name;
  }

  public String get_Password() {
    return this._password;
  }

  public void setPassword(String password) {
    this._password = password;
  }

  public Timestamp getBuff_網咖() {
    return this._Buff_網咖; // 返回 _Buff_網咖 的當前值
  }

  public void setBuff_網咖(Timestamp ts) {
    this._Buff_網咖 = ts; // 將參數 ts 的值賦給 _Buff_網咖
  }

  public String getCPW() {
    return this._CharPassword;
  }

  public void setCPW(String s) {
    this._CharPassword = s;
  }

  public void setcpwok(boolean f) {
    this._iscpwok = f;
  }

  public boolean iscpwok() {
    return this._iscpwok;
  }

  public byte[] getwaitpacket() {
    return this._waitpacket;
  }

  public void setwaitpacket(byte[] s) {
    this._waitpacket = s;
  }

  public void setIp(String ip) {
    this._ip = ip;
  }

  public String getIp() {
    return this._ip;
  }

  public Timestamp getLastActive() {
    return this._lastActive;
  }

  public void setLastActive(Timestamp ts) {
    this._lastActive = ts;
  }

  public long getButtonTimeHome() {
    return this._btnTimeHome;
  }

  public void setButtonTimeHome(long i) {
    this._btnTimeHome = i;
  }

  public long getButtonTimePc() {
    return this._btnTimePc;
  }

  public void setButtonTimePc(long i) {
    this._btnTimePc = i;
  }

  public int getAccessLevel() {
    return this._accessLevel;
  }

  public void setAccessLevel(int accessLevel) {
    this._accessLevel = accessLevel;
  }

  public String getHost() {
    return this._host;
  }

  public void setHost(String host) {
    this._host = host;
  }

  public int getBannedCode() {
    return this._banned;
  }

  public void setBannedCode(int banned) {
    this._banned = banned;
  }

  public int getCharSlot() {
    return this._charslot;
  }

  public void setCharSlot(int charSlot) {
    this._charslot = charSlot;
  }

  public void is_changed_slot(boolean is_changed) {
    this._is_changed_slot = is_changed;
  }

  public boolean is_changed_slot() {
    return this._is_changed_slot;
  }

  public String getphone() {
    return this._phone;
  }

  public void setphone(String s) {
    this._phone = s;
  }

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
      (client.getAccount())._charslot = i;
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

      if (rs.next()) {
        num = rs.getInt("cnt");
      }
      SQLUtil.close(rs);
      SQLUtil.close(pstm);
      SQLUtil.close(con);

      if (num < MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT) {
        return false;
      }
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
      }
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
      }
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

  public void update網咖() { // 方法名稱 update피씨방 翻譯為 "update網咖"
    Updator.exec("UPDATE accounts SET BUFF_PCROOM_Time=? WHERE login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
      // 設置時間戳參數
        pstm.setTimestamp(++idx, Account.this._Buff_網咖);
      // 設置賬號名稱參數
        pstm.setString(++idx, Account.this._name);
      }
    });
  }

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
      (client.getAccount())._GamePassword = pass;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
  }

  public void updateTamPoints(Account account) {
    // 獲取賬號的最後退出時間
    Timestamp lastQuitTime = this._lastQuit;
    // 獲取當前系統時間
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());

    // 初始化時間變量
    long lastQuitTimeInMillis = 0L;
    long currentTimeInMillis = currentTime.getTime();
    long timeDifference = 0L;

    // 如果賬號最後退出時間不為空
    if (lastQuitTime != null) {
      // 將最後退出時間轉換為毫秒
      lastQuitTimeInMillis = lastQuitTime.getTime();
    } else {
      // 如果最後退出時間為空，則直接返回
      return;
    }

    // 計算當前時間與最後退出時間之間的時間差
    timeDifference = currentTimeInMillis - lastQuitTimeInMillis;

    // 將時間差轉換為需要增加的次數（每720,000毫秒增加一次）
    int additionalTamCount = (int) (timeDifference / 720000L);

    // 如果增加次數小於1，則不做任何操作直接返回
    if (additionalTamCount < 1) {
      return;
    }

    // 調用 applyTamPoints 方法來更新Tam點數
    applyTamPoints(account, lastQuitTimeInMillis, additionalTamCount);
  }

  public void applyTamPoints(Account account, long endDate, int tamAdditionalCount) {
    Connection con = null;
    Connection con2 = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    PreparedStatement pstm2 = null;
    Timestamp tamtime = null;
    long sysTime = System.currentTimeMillis();
    int tamcount = Config.ServerRates.TamNum;

    int char_objid = 0;

    // ... 在此處添加進一步的邏輯來處理Tam點數的應用
  }

  int additionalCount = tamAdditionalCount; // 將變量 `tamAdditionalCount` 的值賦給 `additionalCount`
  this.tam_point+=additionalCount*tamcount; // 更新 `tam_point`，將計算出的 `additionalCount` 乘以 `tamcount` 並加到 `tam_point` 上

  updateTam(); // 調用 `updateTam()` 方法來更新或同步 `tam_point` 的變化
  continue; // 使用 `continue` 關鍵字跳過當前循環的剩餘部分，繼續下一次迴圈
  try

  {
    con = L1DatabaseFactory.getInstance().getConnection();
    pstm = con.prepareStatement("SELECT * FROM `characters` WHERE account_name = ?");
    pstm.setString(1, account.getName());
    rs = pstm.executeQuery();
    while (rs.next()) {
      tamtime = rs.getTimestamp("TamEndTime");
      char_objid = rs.getInt("objid");
      if (tamtime != null) {
        if (sysTime <= tamtime.getTime()) {

          int additionalCount = tamAdditionalCount; // 將變量 `tamAdditionalCount` 的值賦給 `additionalCount`
          this.tam_point += additionalCount * tamcount; // 更新 `tam_point`，將計算出的 `additionalCount` 乘以 `tamcount` 並加到 `tam_point` 上
          updateTam(); // 調用 updateTam() 方法來更新或同步 tam_point 的變化
          continue; // 使用 continue 關鍵字跳過當前循環的剩餘部分，繼續下一次迴圈
        }
        int day = Nexttam(char_objid);
        if (day != 0) {
          Timestamp deleteTime = null;
          deleteTime = new Timestamp(sysTime + 86400000L * day + 10000L);
          try {
            con2 = L1DatabaseFactory.getInstance().getConnection();
            pstm2 = con2.prepareStatement("UPDATE characters SET TamEndTime=? WHERE account_name = ? AND objid = ?");

            pstm2.setTimestamp(1, deleteTime);
            pstm2.setString(2, account.getName());
            pstm2.setInt(3, char_objid);
            pstm2.executeUpdate();
            tamdel(char_objid);
            tamtime = deleteTime;
          } catch (Exception e) {
            e.printStackTrace();
          } finally {
            SQLUtil.close(pstm2, con2);
          }
        }

        if (endDate <= scanTime.getTime()) {
          int additionalCount = (int) ((scanTime.getTime() - endDate) / 720000L);
          this.tam_point += additionalCount * scanCount;
          updateTam();

        }
      }
    }
         catch (Exception e) {
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
      pstm.setInt(1, this.tam_point);
      pstm.setString(2, this._name);
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
    } finally {

      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
  }

  public int getTam() {
    return this._tam;
  }

  public int getTamStep() {
    return this._tamStep;
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
      this._tamStep = step;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
  }

  public int getTamPoint() {
    return this.tam_point;
  }

  public int setTamPoint(int tampoint) {
    return this.tam_point = tampoint;
  }

  public int addTamPoint(int tampoint) {
    return this.tam_point += tampoint;
  }

  public int getGamePassword() {
    return this._GamePassword;
  }

  public void setGamePassword(int gamePassword) {
    this._GamePassword = gamePassword;
  }

  public Timestamp getDragonRaid() {
    return this._raidBuff;
  }

  public void setDragonRaid(Timestamp ts) {
    this._raidBuff = ts;
  }

  public int get_ein_level() {
    return SC_REST_EXP_INFO_NOTI.get_ein_level(this._blessOfAin / SC_REST_EXP_INFO_NOTI.EINHASAD_UNIT);
  }

  public int getBlessOfAin() {
    return this._blessOfAin;
  }

  public long get_Pk_Time() {
    return this._pk_time;
  }

  public void set_Pk_Time(long t) {
    this._pk_time = t;
    update_Pk_Time(t);
  }

  public void update_Pk_Time(final long t) {
    Updator.exec("update accounts set pk_time=? where login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setLong(++idx, t);
        pstm.setString(++idx, Account.this.getName());
      }
    });
  }

  public void setBlessOfAin(int i, L1PcInstance pc) {
    this._blessOfAin = i;
    if (pc == null) {
      return;
    }
    if (getBlessOfAin() < 10000) {
      update_ein_effect(pc, false);
    } else {
      update_ein_effect(pc, true);
    }

    SC_REST_EXP_INFO_NOTI.send(pc);
  }

  public int getBlessOfAinCharge() {
    return this._blessOfAinCharge;
  }

  public void setBlessOfAinCharge(int i) {
    this._blessOfAinCharge = i;
    updateBlessOfAin();
  }

  public int getBlessOfAinBonusPoint() {
    return this._blessOfAinBonusPoint;
  }

  public void setBlessOfAinBonusPoint(int i) {
    this._blessOfAinBonusPoint = i;
  }

  public void addBlessOfAinBonusPoint(int i) {
    this._blessOfAinBonusPoint += i * Config.ServerRates.AinBonusPoint_Monster;
  }

  public void addGmBlessOfAinBonusPoint(int i) {
    this._blessOfAinBonusPoint += i;
  }

  public void minusBlessOfAinBonusPoint(int i) {
    int point = i;
    this._blessOfAinBonusPoint -= point;
  }

  private static class EinhasadEffectInfo {
    public int damageReduction;
    public int addSpecialResistance;
    public int calcPcDefense;
    public int weightReduction;
    public int spellId;
    public int buffIconId;
    public int buffTooltipId;

    EinhasadEffectInfo(int damageReduction, int addSpecialResistance, int calcPcDefense, int weightReduction,
        int spellId, int buffIconId, int buffTooltipId) {
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
    if (onOff) {
      if (!pc.hasSkillEffect(4339)) {
        pc.addDamageReductionByArmor(eInfo.damageReduction);
        pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, eInfo.addSpecialResistance);
        pc.getResistance().addcalcPcDefense(eInfo.calcPcDefense);
        pc.addWeightReduction(eInfo.weightReduction);
        SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
        SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
        noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
        noti.set_spell_id(eInfo.spellId);
        noti.set_duration(-1);
        noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_EINHASAD_FAVOR);
        noti.set_on_icon_id(eInfo.buffIconId);
        noti.set_off_icon_id(eInfo.buffIconId);
        noti.set_icon_priority(3);
        noti.set_tooltip_str_id(eInfo.buffTooltipId);
        noti.set_new_str_id(0);
        noti.set_end_str_id(0);
        noti.set_is_good(true);
        pc.sendPackets((MJIProtoMessage) noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
        pc.setSkillEffect(eInfo.spellId, -1L);
      }

    } else if (pc.hasSkillEffect(4339)) {
      pc.addDamageReductionByArmor(-eInfo.damageReduction);
      pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -eInfo.addSpecialResistance);
      pc.getResistance().addcalcPcDefense(-eInfo.calcPcDefense);
      pc.addWeightReduction(-eInfo.weightReduction);
      SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
      SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
      noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
      noti.set_spell_id(eInfo.spellId);
      noti.set_duration(0);
      noti.set_off_icon_id(eInfo.buffIconId);
      noti.set_end_str_id(0);
      noti.set_is_good(true);
      pc.sendPackets((MJIProtoMessage) noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
      pc.removeSkillEffect(eInfo.spellId);
    }
  }

  public void addBlessOfAin(int val, L1PcInstance pc, String rewardType) {
    // 根據獎勵類型進行處理
    switch (rewardType) {
      case "出席": // 出席獎勵
      case "怪物": // 怪物獎勵
      case "圖鑑": // 圖鑑獎勵
      case "推送": // 推送獎勵
        // 添加艾因哈薩德的祝福
        addBlessOfAin(val, pc, eEinhasadBonusType.BonusNone);
        // 發送祝福槽增益通知
        SC_REST_GAUGE_CHARGE_NOTI.send_bonus_guage(pc, val / 10000);
        break;
    }
  }

  public void addBlessOfAin(int val, L1PcInstance pc) {
    addBlessOfAin(val, pc, eEinhasadBonusType.SectionBonus);
  }

  public void addBlessOfAin(int val, L1PcInstance pc, eEinhasadBonusType type) {
    if (val < 0) {
      if (pc.hasSkillEffect(80000)) {
        pc.addVirualEinhasad(Math.abs(val));
        int virualEinhasadPer = Math.abs(IntRange.getTotalValueRint((pc.getVirualEinhasad() / 10000)));
        if (virualEinhasadPer > 0) {
          pc.setVirualEinhasad(0);

          addBlessOfAinBonusPoint(virualEinhasadPer);
          SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());

          L1Clan clan = pc.getClan();
          if (clan != null &&
              pc.getClanContribution() < 10000000L) {
            pc.addClanContribution(virualEinhasadPer);
            clan.addContribution(virualEinhasadPer);
            ClanTable.getInstance().updateContribution(clan.getClanId(), clan.getContribution());
            pc.save();

          }

        }

      } else {

        int oldEinPointByper = Math.abs(IntRange.getTotalValueRint((this._blessOfAin / 10000)));
        int newEinPointByPer = Math.abs(IntRange.getTotalValueRint(((this._blessOfAin + val) / 10000)));
        if (oldEinPointByper != newEinPointByPer) {
          addBlessOfAinBonusPoint(oldEinPointByper - newEinPointByPer);
          SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());

          L1Clan clan = pc.getClan();
          if (clan != null &&
              pc.getClanContribution() < 10000000L) {
            pc.addClanContribution(oldEinPointByper - newEinPointByPer);
            clan.addContribution(oldEinPointByper - newEinPointByPer);
            ClanTable.getInstance().updateContribution(clan.getClanId(), clan.getContribution());
            pc.save();
          }
        }
      }
    }

    if (val < 0 && pc.hasSkillEffect(80000)) {
      if (pc.getMapId() != 5490) {
        return;
      }
    }

    this._blessOfAin = IntRange.ensure(this._blessOfAin + val, 0, SC_REST_EXP_INFO_NOTI.EINHASAD_LIMIT);

    if (getBlessOfAin() < 10000) {
      update_ein_effect(pc, false);
    } else {
      update_ein_effect(pc, true);
    }

    SC_REST_EXP_INFO_NOTI.send(pc, type);

    if (pc.hasSkillEffect(8382)) {
      int skillTime = pc.getSkillEffectTimeSec(8382);
      if (this._blessOfAin < 10000) {
        pc.sendPackets((ServerBasePacket) S_InventoryIcon.iconNewUnLimit(8383, 5087, true));
      } else {
        pc.sendPackets((ServerBasePacket) S_InventoryIcon.icoEnd(8383));
        pc.sendPackets((ServerBasePacket) S_InventoryIcon.icoNew(8382, 5087, skillTime, true));
      }
    }
  }

  public void updateBlessOfAin() {
    Updator.exec(
        "update accounts set bless_of_ain=?, bless_of_ain_charge=?, bless_of_ain_bonus_point=?, fatigue_point=?, fatigue_start_time=? where login=?",
        new Handler() {
          public void handle(PreparedStatement pstm) throws Exception {
            int idx = 0;
            pstm.setInt(++idx, Account.this._blessOfAin);
            pstm.setInt(++idx, Account.this._blessOfAinCharge);
            pstm.setInt(++idx, Account.this._blessOfAinBonusPoint);
            pstm.setInt(++idx, Account.this.m_fatigue_point);
            pstm.setLong(++idx, Account.this.m_fatigue_start_time);
            pstm.setString(++idx, Account.this.getName());
          }
        });
  }

  public boolean has_fatigue() {
    return (FatigueProperty.getInstance().use_fatigue() && remain_fatigue() > 0L);
  }

  public long remain_fatigue() {
    return this.m_fatigue_start_time + FatigueProperty.getInstance().get_fatigue_effect_millis()
        - System.currentTimeMillis();
  }

  public void send_fatigue(L1PcInstance pc) {
    FatigueProperty p = FatigueProperty.getInstance();
    SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
    noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
    noti.set_spell_id(p.get_fatigue_buff_icon_id());
    noti.set_duration((int) (remain_fatigue() / 1000L));
    noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
    noti.set_on_icon_id(p.get_fatigue_buff_icon_id());
    noti.set_off_icon_id(p.get_fatigue_buff_icon_id());
    noti.set_icon_priority(2);
    noti.set_tooltip_str_id(3215);
    noti.set_new_str_id(3215);
    noti.set_is_good(false);
    pc.sendPackets((MJIProtoMessage) noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
    pc.sendPackets(String.format("格蘭肯的憤怒: 獲得經驗值 -%d%% / 金幣掉落 -%d%% / 減免 -%d%%", new Object[] {
        Integer.valueOf((int) (p.get_fatigue_effect_exp() * 100.0D)),
        Integer.valueOf((int) ((1.0D - p.get_fatigue_effect_adena()) * 100.0D)),
        Integer.valueOf((int) (p.get_fatigue_effect_reduction() * 100.0D)) }));
  }

  public void apply_fatigue(L1PcInstance pc) {
    this.m_fatigue_start_time = System.currentTimeMillis();
    this.m_fatigue_point = 0;
    update_fatigue_info();
    send_fatigue(pc);
  }

  public void initialize_fatigue_info(L1PcInstance pc) {
    done_fatigue(pc);
    this.m_fatigue_point = 0;
    update_fatigue_info();
  }

  public void done_fatigue(L1PcInstance pc) {
    FatigueProperty p = FatigueProperty.getInstance();
    SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
    noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
    noti.set_spell_id(p.get_fatigue_buff_icon_id());
    noti.set_duration(0);
    noti.set_off_icon_id(p.get_fatigue_buff_icon_id());
    noti.set_end_str_id(0);
    noti.set_is_good(true);
    pc.sendPackets((MJIProtoMessage) noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
    pc.sendPackets("格蘭肯的憤怒已經解除.");
    this.m_fatigue_start_time = 0L;
  }

  public synchronized int inc_fatigue_point(L1PcInstance pc) {
    if (!FatigueProperty.getInstance().use_fatigue()) {
      return 0;
    }

    long current_millis = System.currentTimeMillis();
    if (current_millis < this.m_fatigue_point_time
        + FatigueProperty.getInstance().get_fatigue_point_stackable_millis()) {
      return 0;
    }

    this.m_fatigue_point_time = current_millis;

    if (inc_fatigue_point() >= FatigueProperty.getInstance().get_fatigue_point_limit()) {
      apply_fatigue(pc);
    }

    return this.m_fatigue_point;
  }

  public void read_fatigue_resultset(ResultSet rs) throws SQLException {
    this.m_fatigue_point = rs.getInt("fatigue_point");
    this.m_fatigue_start_time = rs.getLong("fatigue_start_time");
  }

  public int inc_fatigue_point() {
    if (has_fatigue()) {
      return 0;
    }
    if (get_ein_level() == 4)
      return ++this.m_fatigue_point;
    if (get_ein_level() == 3)
      return this.m_fatigue_point += 2;
    if (get_ein_level() == 2)
      return this.m_fatigue_point += 3;
    if (get_ein_level() == 1)
      return this.m_fatigue_point += 4;
    if (get_ein_level() <= 0) {
      return this.m_fatigue_point += 5;
    }
    return ++this.m_fatigue_point;
  }

  public int get_fatigue_point() {
    return this.m_fatigue_point;
  }

  public int add_fatigue_point(int fatigue) {
    return this.m_fatigue_point += fatigue;
  }

  public long get_fatigue_start_time() {
    return this.m_fatigue_start_time;
  }

  public long add_fatigue_start_time(long time) {
    return this.m_fatigue_start_time += time;
  }

  public static void initialize_fatigue() {
    Updator.exec("update accounts set fatigue_point=?, fatigue_start_time=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setInt(++idx, 0);
        pstm.setLong(++idx, 0L);
      }
    });
  }

  public void update_fatigue_info() {
    Updator.exec("update accounts set fatigue_point=?, fatigue_start_time=? where login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setInt(++idx, Account.this.m_fatigue_point);
        pstm.setLong(++idx, Account.this.m_fatigue_start_time);
        pstm.setString(++idx, Account.this.getName());
      }
    });
  }

  public Timestamp getLastLogOut() {
    return this._lastlogout;
  }

  public void setLastLogOut(Timestamp ts) {
    this._lastlogout = ts;
  }

  public void updateLastLogOut() {
    Connection con = null;
    PreparedStatement pstm = null;
    try {
      this._lastlogout = new Timestamp(System.currentTimeMillis());
      con = L1DatabaseFactory.getInstance().getConnection();
      String sqlstr = "UPDATE accounts SET last_log_out=? WHERE login = ?";
      pstm = con.prepareStatement(sqlstr);
      Timestamp time = new Timestamp(System.currentTimeMillis());
      pstm.setTimestamp(1, time);
      pstm.setString(2, getName());
      pstm.execute();

      this._lastlogout = time;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
  }

  public int getAccountId() {
    return this._id;
  }

  public void setAccountId(int i) {
    this._id = i;
  }

  public static Integer[] loadAccountAddress(final String accountName) {
    final MJObjectWrapper<Integer[]> wrapper = new MJObjectWrapper();
    Selector.exec("select ip from accounts where login=?", new SelectorHandler() {
      public void handle(PreparedStatement pstm) throws Exception {
        pstm.setString(1, accountName);
      }

      public void result(ResultSet rs) throws Exception {
        if (rs.next()) {
          try {
            String addressValues = rs.getString("ip");
            StringTokenizer st = new StringTokenizer(addressValues, ".");

            String ip1 = st.nextToken();
            String ip2 = st.nextToken();
            String ip3 = st.nextToken();

            Integer[] itg = { Integer.valueOf(Integer.parseInt(ip1)), Integer.valueOf(Integer.parseInt(ip2)),
                Integer.valueOf(Integer.parseInt(ip3)) };

            wrapper.value = itg;
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });
    return (Integer[]) wrapper.value;
  }

  public void UpdateShopPassword() {
    Connection con = null;
    PreparedStatement pstm = null;
    try {
      con = L1DatabaseFactory.getInstance().getConnection();
      String sqlstr = "UPDATE accounts SET shop_password=? WHERE login = ?";
      pstm = con.prepareStatement(sqlstr);
      pstm.setInt(1, getShopPassword());
      pstm.setString(2, this._name);
      pstm.executeUpdate();
    } catch (Exception e) {
      _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
    } finally {
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
  }

  public int getShopPassword() {
    return this._ShopPassword;
  }

  public void setShopPassword(int s) {
    this._ShopPassword = s;
  }

  public Timestamp getAccountPause() {
    return this._account_pause;
  }

  public void setAccountPause(Timestamp ts) {
    this._account_pause = ts;
  }

  public String getAccountPauseReason() {
    return this._account_pause_reason;
  }

  public void setAccountPauseReason(String s) {
    this._account_pause_reason = s;
  }

  public boolean getAttendance_Premium() {
    return this._attendance_premium;
  }

  public void setAttendance_Premium(final boolean value) {
    this._attendance_premium = value;

    Updator.exec("update accounts set attendance_premium=? where login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setString(++idx, value ? "true" : "false");
        pstm.setString(++idx, Account.this.getName());
      }
    });
  }

  public boolean getAttendance_Special() {
    return this._attendance_special;
  }

  public void setAttendance_Special(final boolean value) {
    this._attendance_special = value;

    Updator.exec("update accounts set attendance_special=? where login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setString(++idx, value ? "true" : "false");
        pstm.setString(++idx, Account.this.getName());
      }
    });
  }

  public boolean getAttendance_Brave_Warrior() {
    return this._attendance_brave_warrior;
  }

  public void setAttendance_Brave_Warrior(final boolean value) {
    this._attendance_brave_warrior = value;

    Updator.exec("update accounts set attendance_brave_warrior=? where login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setString(++idx, value ? "true" : "false");
        pstm.setString(++idx, Account.this.getName());
      }
    });
  }

  public boolean getAttendance_Aden_World() {
    return this._attendance_aden_world;
  }

  public void setAttendance_Aden_World(final boolean value) {
    this._attendance_aden_world = value;

    Updator.exec("update accounts set attendance_aden_world=? where login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setString(++idx, value ? "true" : "false");
        pstm.setString(++idx, Account.this.getName());
      }
    });
  }

  public boolean getAttendance_Bravery_Medal() {
    return this._attendance_bravery_medal;
  }

  public void setAttendance_Bravery_Medal(final boolean value) {
    this._attendance_bravery_medal = value;

    Updator.exec("update accounts set attendance_bravery_medal=? where login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setString(++idx, value ? "true" : "false");
        pstm.setString(++idx, Account.this.getName());
      }
    });
  }

  public String is_posess_slotkey_charname() {
    return this.account_posess_slotkey_charname;
  }

  public void set_posess_slotkey_charname(String key) {
    this.account_posess_slotkey_charname = key;
  }

  public void get_slot_char(final String name) {
    Selector.exec("select objid from characters where account_name=?", new SelectorHandler() {
      public void handle(PreparedStatement pstm) throws Exception {
        pstm.setString(1, name);
      }

      public void result(ResultSet rs) throws Exception {
        while (rs.next()) {
          Account.this.search_slot_char(rs.getInt("objid"), 210083);
        }
      }
    });
  }

  public void search_slot_char(final int objid, final int itemid) {
    Selector.exec("select * from character_items where char_id=? and item_id=?", new SelectorHandler() {
      public void handle(PreparedStatement pstm) throws Exception {
        pstm.setInt(1, objid);
        pstm.setInt(2, itemid);
      }

      public void result(ResultSet rs) throws Exception {
        while (rs.next() && Account.this.is_posess_slotkey_charname() == null) {
          Account.this.set_posess_slotkey_charname("dummy");
          Account.this.Delete_key(objid, itemid);
        }
      }
    });
  }

  public void Delete_key(final int objid, final int itemid) {
    Updator.exec("delete from character_items where char_id=? and item_id=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setInt(++idx, objid);
        pstm.setInt(++idx, itemid);
      }
    });
  }

  public int getEinDayBonus() {
    return this._eindaybonus;
  }

  public void setEinDayBonus(int i) {
    this._eindaybonus = i;
  }

  public int get_Index0_Remain_Time() {
    return this._index0_remain_time;
  }

  public int get_Index1_Remain_Time() {
    return this._index1_remain_time;
  }

  public int get_Index0_type(L1PcInstance pc) {
    if (this._index0_type == 0) {
      int type = pc.getType();
      switch (type) {
        case 2:
          this._index0_type = 2;

          return this._index0_type;
        case 3:
        case 6:
          this._index0_type = 3;
          return this._index0_type;
      }
      this._index0_type = 1;
    }
    return this._index0_type;
  }

  public void set_Index0_type(int i) {
    this._index0_type = i;
    update_Index_type(0, i);
  }

  public int get_Index0_1() {
    return this._index0_1;
  }

  public int get_Index0_2() {
    return this._index0_2;
  }

  public int get_Index0_3() {
    return this._index0_3;
  }

  public int get_Index1_type(L1PcInstance pc) {
    if (this._index1_type == 0) {
      int type = pc.getType();
      switch (type) {
        case 2:
          this._index1_type = 2;

          return this._index1_type;
        case 3:
        case 6:
          this._index1_type = 3;
          return this._index1_type;
      }
      this._index1_type = 1;
    }
    return this._index1_type;
  }

  public void set_Index1_type(int i) {
    this._index1_type = i;
    update_Index_type(1, i);
  }

  public void update_Index_type(int index, final int type) {
    String name = "";
    if (index == 0) {
      name = "index0_type";
    } else if (index == 1) {
      name = "index1_type";
    }
    if (name == "") {
      System.out.println("網咖金色增益類型錯誤");
      return;
    }
    Updator.exec("update accounts set " + name + "=? where login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setInt(++idx, type);
        pstm.setString(++idx, Account.this.getName());
      }
    });
  }

  public int get_Index1_1() {
    return this._index1_1;
  }

  public int get_Index1_2() {
    return this._index1_2;
  }

  public int get_Index1_3() {
    return this._index1_3;
  }

  public void set_Index(int group, int index, int grade) {
    update_Index_value(group, index, grade);
  }

  public void set_Index0_1(int i) {
    this._index0_1 = i;
  }

  public void set_Index0_2(int i) {
    this._index0_2 = i;
  }

  public void set_Index0_3(int i) {
    this._index0_3 = i;
  }

  public void set_Index1_1(int i) {
    this._index1_1 = i;
  }

  public void set_Index1_2(int i) {
    this._index1_2 = i;
  }

  public void set_Index1_3(int i) {
    this._index1_3 = i;
  }

  public void del_Index0_1() {
    this._index0_1 = 0;
    update_Index_value(0, 1, this._index0_1);
  }

  public void del_Index0_2() {
    this._index0_2 = 0;
    update_Index_value(0, 2, this._index0_2);
  }

  public void del_Index0_3() {
    this._index0_3 = 0;
    update_Index_value(0, 3, this._index0_3);
  }

  public void del_Index1_1() {
    this._index1_1 = 0;
    update_Index_value(1, 1, this._index1_1);
  }

  public void del_Index1_2() {
    this._index1_2 = 0;
    update_Index_value(1, 2, this._index1_2);
  }

  public void del_Index1_3() {
    this._index1_3 = 0;
    update_Index_value(1, 3, this._index1_3);
  }

  public void del_Index0_Remain_Time() {
    this._index0_remain_time = 0;
    update_Index0_Remain_Time();
  }

  public void del_Index1_Remain_Time() {
    this._index1_remain_time = 0;
    update_Index1_Remain_Time();
  }

  public void update_Index_value(int group, int index, final int grade) {
    String name = "";

    if (group == 0) {
      switch (index) {
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
      switch (index) {
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
      System.out.println("網咖金色增益錯誤");

      return;
    }
    Updator.exec("update accounts set " + name + "=? where login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setInt(++idx, grade);
        pstm.setString(++idx, Account.this.getName());
      }
    });
  }

  public void add_Index0_Time(int i) {
    this._index0_remain_time += i;
    update_Index0_Remain_Time();
  }

  public void use_Index0_Time(int i) {
    this._index0_remain_time -= i;
  }

  public void update_Index0_Remain_Time() {
    Updator.exec("update accounts set index0_remain_time=? where login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setInt(++idx, Account.this._index0_remain_time);
        pstm.setString(++idx, Account.this.getName());
      }
    });
  }

  public void add_Index1_Time(int i) {
    this._index1_remain_time += i;
    update_Index1_Remain_Time();
  }

  public void use_Index1_Time(int i) {
    this._index1_remain_time -= i;
  }

  public void update_Index1_Remain_Time() {
    Updator.exec("update accounts set index1_remain_time=? where login=?", new Handler() {
      public void handle(PreparedStatement pstm) throws Exception {
        int idx = 0;
        pstm.setInt(++idx, Account.this._index1_remain_time);
        pstm.setString(++idx, Account.this.getName());
      }
    });
  }
}
