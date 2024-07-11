package l1j.server.MJTemplate.MJProto.MainServer_Client_System;

// TODO : 這是自動生成的 PROTO 代碼，由 Nature 製作。
public enum FREE_BUFF_SHIELD_TYPE {
	// 網吧盾
	PC_CAFE_SHIELD(0),
	// 活動增益盾
	EVENT_BUFF_SHIELD(1),
	// 免費增益盾
	FREE_BUFF_SHIELD(2),

	;

	private int value;

	// 枚舉類型的構造函數，用於初始化枚舉值
	FREE_BUFF_SHIELD_TYPE(int val) {
		value = val;
	}

	// 返回枚舉值的整數表示
	public int toInt() {
		return value;
	}

	// 比較當前枚舉值與其他枚舉值是否相等
	public boolean equals(FREE_BUFF_SHIELD_TYPE v) {
		return value == v.value;
	}

	// 通過整數值返回對應的枚舉類型。如果輸入的整數值無效，則拋出 IllegalArgumentException
	public static FREE_BUFF_SHIELD_TYPE fromInt(int i) {
		switch (i) {
			case 0:
				return PC_CAFE_SHIELD; // 網吧盾
			case 1:
				return EVENT_BUFF_SHIELD; // 活動增益盾
			case 2:
				return FREE_BUFF_SHIELD; // 免費增益盾
			default:
				throw new IllegalArgumentException(String.format("無效的 FREE_BUFF_SHIELD_TYPE 參數: %d", i));
		}
	}
}