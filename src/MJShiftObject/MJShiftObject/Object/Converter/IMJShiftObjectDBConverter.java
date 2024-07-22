package MJShiftObject.Object.Converter;

import MJShiftObject.Object.MJShiftObject;

/**
 * 定義對 MJShiftObject 進行數據庫轉換操作的接口。
 */
public interface IMJShiftObjectDBConverter {
	// 轉換成功
	public static final int CONVERT_SUCCESS = 1;

	// 轉換失敗：未找到 PC
	public static final int CONVERT_FAIL_NOT_FOUND_PC = 2;

	// 轉換失敗：未找到帳戶
	public static final int CONVERT_FAIL_NOT_FOUND_ACCOUNT = 4;

	// 轉換失敗：無效
	public static final int CONVERT_FAIL_INVALID = 8;

	/**
	 * 執行轉換操作。
	 *
	 * @param paramMJShiftObject 要轉換的 MJShiftObject 對象
	 * @return 轉換結果的狀態碼
	 */
	int work(MJShiftObject paramMJShiftObject);

	/**
	 * 執行刪除操作。
	 *
	 * @param paramMJShiftObject 要刪除的 MJShiftObject 對象
	 * @return 刪除結果的狀態碼
	 */
	int delete(MJShiftObject paramMJShiftObject);
}


