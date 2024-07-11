package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 這是自動生成的 PROTO 代碼，由 Nature 製作。
public enum PolymorphInfo {
	// 多變形索引
	POLY_INDEX(0),
	// 多變形描述
	POLY_DESC(1),
	// 多變形圖片
	POLY_PNG(2),
	// 高級多變形
	POLY_ADVANCED(3),
	// 多變形後綴
	POLY_POSTFIX(4),
	// 多變形信息大小
	POLYMORPH_INFO_SIZE(5),

	;

	private int value;

	// 枚舉類型的構造函數，用於初始化枚舉值
	PolymorphInfo(int val) {
		value = val;
	}

	// 返回枚舉值的整數表示
	public int toInt() {
		return value;
	}

	// 比較當前枚舉值與其他枚舉值是否相等
	public boolean equals(PolymorphInfo v) {
		return value == v.value;
	}

	// 通過整數值返回對應的枚舉類型。如果輸入的整數值無效，則拋出 IllegalArgumentException
	public static PolymorphInfo fromInt(int i) {
		switch (i) {
			case 0:
				return POLY_INDEX; // 多變形索引
			case 1:
				return POLY_DESC; // 多變形描述
			case 2:
				return POLY_PNG; // 多變形圖片
			case 3:
				return POLY_ADVANCED; // 高級多變形
			case 4:
				return POLY_POSTFIX; // 多變形後綴
			case 5:
				return POLYMORPH_INFO_SIZE; // 多變形信息大小
			default:
				throw new IllegalArgumentException(String.format("無效的 PolymorphInfo 參數: %d", i));
		}
	}
}
