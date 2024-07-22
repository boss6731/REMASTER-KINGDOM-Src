package MJShiftObject.Object;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表示 MJ 的轉移物件信息。
 */
public class MJShiftObject {
	private int m_source_id;
	private int m_destination_id;
	private MJEShiftObjectType m_shift_type;
	private String m_source_character_name;
	private String m_source_account_name;
	private String m_destination_character_name;
	private String m_destination_account_name;
	private String m_convert_parameters;

	/**
	 * 創建並初始化一個新的 MJShiftObject 實例。
	 *
	 * @param rs 結果集
	 * @return 初始化好的 MJShiftObject 實例
	 * @throws SQLException 如果數據庫存取錯誤
	 */
	public static MJShiftObject newInstance(ResultSet rs) throws SQLException {
		return newInstance()
				.set_source_id(rs.getInt("source_id"))
				.set_destination_id(rs.getInt("destination_id"))
				.set_shift_type(MJEShiftObjectType.from_name(rs.getString("shift_type")))
				.set_source_character_name(rs.getString("source_character_name"))
				.set_source_account_name(rs.getString("source_account_name"))
				.set_destination_character_name(rs.getString("destination_character_name"))
				.set_destination_account_name(rs.getString("destination_account_name"))
				.set_convert_parameters(rs.getString("convert_parameters"));
	}

	/**
	 * 創建一個新的 MJShiftObject 實例。
	 *
	 * @return 新的 MJShiftObject 實例
	 */
	public static MJShiftObject newInstance() {
		return new MJShiftObject();
	}

	/**
	 * 設置源 ID。
	 *
	 * @param source_id 源 ID
	 * @return 當前的 MJShiftObject 實例
	 */
	public MJShiftObject set_source_id(int source_id) {
		this.m_source_id = source_id;
		return this;
	}

	/**
	 * 設置目標 ID。
	 *
	 * @param destination_id 目標 ID
	 * @return 當前的 MJShiftObject 實例
	 */
	public MJShiftObject set_destination_id(int destination_id) {
		this.m_destination_id = destination_id;
		return this;
	}

	/**
	 * 設置轉換類型。
	 *
	 * @param shift_type 轉換類型
	 * @return 當前的 MJShiftObject 實例
	 */
	public MJShiftObject set_shift_type(MJEShiftObjectType shift_type) {
		this.m_shift_type = shift_type;
		return this;
	}

/**
 * 設置源角色名稱。
 *
 * @param source_character_name 源角色名稱
 * @return 當前的 MJShiftObject 實例
 */

	public MJShiftObject set_source_character_name(String source_character_name) {
		this.m_source_character_name = source_character_name;
		return this;
	}

/**
 * 	設置源賬號名稱。
 *			*
 * 	@param source_account_name 源賬號名稱
 * 	@return 當前的 MJShiftObject 實例
 */
	public MJShiftObject set_source_account_name(String source_account_name) {
		this.m_source_account_name = source_account_name;
		return this;
	}

/**
 * 	設置目標角色名稱。
 *			*
 * 	@param destination_character_name 目標角色名稱
 * 	@return 當前的 MJShiftObject 實例
 */
	public MJShiftObject set_destination_character_name(String destination_character_name) {
		this.m_destination_character_name = destination_character_name;
		return this;
	}

/**
 * 	設置目標賬號名稱。
 *			*
 * 	@param destination_account_name 目標賬號名稱
 * 	@return 當前的 MJShiftObject 實例
 */
	public MJShiftObject set_destination_account_name(String destination_account_name) {
		this.m_destination_account_name = destination_account_name;
		return this;
	}

/**
 * 	設置轉換參數。
 *			*
 * 	@param convert_parameters 轉換參數
 * 	@return 當前的 MJShiftObject 實例
 */
	public MJShiftObject set_convert_parameters(String convert_parameters) {
		this.m_convert_parameters = convert_parameters;
		return this;
	}

/**
 * 	獲取源 ID。
 *			*
 * 	@return 源 ID
 */
	public int get_source_id() {
		return this.m_source_id;
	}

/**
 * 	獲取目標 ID。
 *			*
 * 	@return 目標 ID
 */
	public int get_destination_id() {
		return this.m_destination_id;
	}

/**
 * 	獲取轉換類型。
 *
 *  @return 轉換類型
 */
	public MJEShiftObjectType get_shift_type() {
		return this.m_shift_type;
	}

/**
 * 	獲取源角色名稱。
 *			*
 * 	@return 源角色名稱
 */
	public String get_source_character_name() {
		return this.m_source_character_name;
	}

/**
 * 	獲取源賬號名稱。
 *			*
 * 			@return 源賬號名稱
 */
	public String get_source_account_name() {
		return this.m_source_account_name;
	}

/**
 * 	獲取目標角色名稱。
 *			*
 * 			@return 目標角色名稱
 */
	public String get_destination_character_name() {
		return this.m_destination_character_name;
	}

/**
 * 	獲取目標賬號名稱。
 *			*
 * 			@return 目標賬號名稱
 */
	public String get_destination_account_name() {
		return this.m_destination_account_name;
	}

/**
 * 	獲取轉換參數。
 *			*
 * 			@return 轉換參數
 */
	public String get_convert_parameters() {
		return this.m_convert_parameters;
	}
}


