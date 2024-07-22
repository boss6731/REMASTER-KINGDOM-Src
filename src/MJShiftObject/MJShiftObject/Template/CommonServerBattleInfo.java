package MJShiftObject.Template;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表示伺服器的戰鬥信息。
 */
public class CommonServerBattleInfo {
	private String m_server_identity;
	private long m_start_millis;
	private long m_ended_millis;
	private int m_current_kind;
	private String m_battle_name;

	/**
	 * 創建並初始化一個新的 CommonServerBattleInfo 實例。
	 *
	 * @param rs 結果集
	 * @return 初始化好的 CommonServerBattleInfo 實例
	 * @throws SQLException 如果數據庫存取錯誤
	 */
	public static CommonServerBattleInfo newInstance(ResultSet rs) throws SQLException {
		return newInstance()
				.set_server_identity(rs.getString("server_identity"))
				.set_start_millis(rs.getTimestamp("start_time").getTime())
				.set_ended_millis(rs.getTimestamp("ended_time").getTime())
				.set_current_kind(rs.getInt("current_kind"))
				.set_battle_name(rs.getString("battle_name"));
	}

	/**
	 * 創建一個新的 CommonServerBattleInfo 實例。
	 *
	 * @return 新的 CommonServerBattleInfo 實例
	 */
	public static CommonServerBattleInfo newInstance() {
		return new CommonServerBattleInfo();
	}

	/**
	 * 檢查戰鬥是否正在進行。
	 *
	 * @return 如果戰鬥正在進行，則返回 true，否則返回 false
	 */
	public boolean is_run() {
		long current_millis = System.currentTimeMillis();
		return (this.m_start_millis < current_millis && current_millis < this.m_ended_millis);
	}

	/**
	 * 檢查戰鬥是否已經結束。
	 *
	 * @return 如果戰鬥已經結束，則返回 true，否則返回 false
	 */
	public boolean is_ended() {
		long current_millis = System.currentTimeMillis();
		return (current_millis >= this.m_ended_millis);
	}

	/**
	 * 設置伺服器身份。
	 *
	 * @param server_identity 伺服器身份
	 * @return 當前的 CommonServerBattleInfo 實例
	 */
	public CommonServerBattleInfo set_server_identity(String server_identity) {
		this.m_server_identity = server_identity;
		return this;
	}

	/**
	 * 設置開始時間（毫秒）。
	 *
	 * @param start_millis 開始時間（毫秒）
	 * @return 當前的 CommonServerBattleInfo 實例
	 */
	public CommonServerBattleInfo set_start_millis(long start_millis) {
		this.m_start_millis = start_millis;
		return this;
	}

/**
 * 設置結束時間（毫秒）。
 *
 * @param ended_millis 結束時間（毫秒）
 * @return 當前的 CommonServerBattleInfo 實例
 */

	public CommonServerBattleInfo set_ended_millis(long ended_millis) {
		this.m_ended_millis = ended_millis;
		return this;
	}

	/**
	 * 設置當前類型。
	 *
	 * @param current_kind 當前類型
	 * @return 當前的 CommonServerBattleInfo 實例
	 */
	public CommonServerBattleInfo set_current_kind(int current_kind) {
		this.m_current_kind = current_kind;
		return this;
	}

	/**
	 * 設置戰鬥名稱。
	 *
	 * @param battle_name 戰鬥名稱
	 * @return 當前的 CommonServerBattleInfo 實例
	 */
	public CommonServerBattleInfo set_battle_name(String battle_name) {
		this.m_battle_name = battle_name;
		return this;
	}

	/**
	 * 獲取伺服器身份。
	 *
	 * @return 伺服器身份
	 */
	public String get_server_identity() {
		return this.m_server_identity;
	}

	/**
	 * 獲取開始時間（毫秒）。
	 *
	 * @return 開始時間（毫秒）
	 */
	public long get_start_millis() {
		return this.m_start_millis;
	}

	/**
	 * 獲取結束時間（毫秒）。
	 *
	 * @return 結束時間（毫秒）
	 */
	public long get_ended_millis() {
		return this.m_ended_millis;
	}

	/**
	 * 獲取當前類型。
	 *
	 * @return 當前類型
	 */
	public int get_current_kind() {
		return this.m_current_kind;
	}

	/**
	 * 獲取戰鬥名稱。
	 *
	 * @return 戰鬥名稱
	 */
	public String get_battle_name() {
		return this.m_battle_name;
	}
}


