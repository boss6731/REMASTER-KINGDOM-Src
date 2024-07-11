package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 這是自動生成的 PROTO 代碼，由 Nature 製作。
public enum ClassID {
	// 王子
	PRINCE(0),
	// 騎士
	KNIGHT(1),
	// 妖精
	ELF(2),
	// 魔法師
	MAGICIAN(3),
	// 黑暗妖精
	DARKELF(4),
	// 龍騎士
	DRAGON_KNIGHT(5),
	// 幻術師
	ILLUSIONIST(6),
	// 戰士
	WARRIOR(7),
	// 劍士
	FENCER(8),
	// 黃金槍騎
	LANCER(9),
	// 未知
	UNKNOWN(10),

	;

	private int value;

	// 枚舉類型的構造函數，用於初始化枚舉值
	ClassID(int val) {
		value = val;
	}

	// 返回枚舉值的整數表示
	public int toInt() {
		return value;
	}

	// 比較當前枚舉值與其他枚舉值是否相等
	public boolean equals(ClassID v) {
		return value == v.value;
	}

	// 通過整數值返回對應的枚舉類型。如果輸入的整數值無效，則拋出 IllegalArgumentException
	public static ClassID fromInt(int i) {
		switch (i) {
			case 0:
				return PRINCE; // 王子
			case 1:
				return KNIGHT; // 騎士
			case 2:
				return ELF; // 妖精
			case 3:
				return MAGICIAN; // 魔法師
			case 4:
				return DARKELF; // 黑暗妖精
			case 5:
				return DRAGON_KNIGHT; // 龍騎士
			case 6:
				return ILLUSIONIST; // 幻術師
			case 7:
				return WARRIOR; // 戰士
			case 8:
				return FENCER; // 劍士
			case 9:
				return LANCER; // 黃金槍騎
			case 10:
				return UNKNOWN; // 未知
			default:
				throw new IllegalArgumentException(String.format("無效的 ClassID 參數: %d", i));
		}
	}
}