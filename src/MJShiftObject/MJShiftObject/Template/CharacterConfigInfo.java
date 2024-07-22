package MJShiftObject.Template;

/**
 * 表示角色的配置信息。
 */
public class CharacterConfigInfo {
	// 配置數據的長度
	public int length;

	// 配置數據的字節數組
	public byte[] buff;

	/**
	 * 帶參數的構造函數，用於初始化所有字段。
	 *
	 * @param length 配置數據的長度
	 * @param buff   配置數據的字節數組
	 */
	public CharacterConfigInfo(int length, byte[] buff) {
		this.length = length;
		this.buff = buff;
	}

	/**
	 * 無參數的默認構造函數。
	 */
	public CharacterConfigInfo() {
	}
}


