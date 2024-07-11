package l1j.server.MJTemplate.MJProto.PlaySupport;

// TODO : 這是自動生成的 PROTO 代碼，由 Nature 製作。
public enum BlessCode {
	// 祝福
	BLESSED(0),
	// 普通
	NORMAL(1),
	// 詛咒
	CURSED(2),
	// 未鑑定
	UNIDENTIFIED(3),

	;

	private int value;

	// 枚舉類型的構造函數，用於初始化枚舉值
	BlessCode(int val) {
		value = val;
	}

	// 返回枚舉值的整數表示
	public int toInt() {
		return value;
	}

	// 比較當前枚舉值與其他枚舉值是否相等
	public boolean equals(BlessCode v) {
		return value == v.value;
	}

	// 通過整數值返回對應的枚舉類型。如果輸入的整數值無效，則拋出 IllegalArgumentException
	public static BlessCode fromInt(int i) {
		switch (i) {
			case 0:
				return BLESSED; // 祝福
			case 1:
				return NORMAL; // 普通
			case 2:
				return CURSED; // 詛咒
			case 3:
				return UNIDENTIFIED; // 未鑑定
			default:
				throw new IllegalArgumentException(String.format("無效的 BlessCode 參數: %d", i));
		}
	}
}