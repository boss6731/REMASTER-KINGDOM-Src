package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

// TODO: 自動生成的 PROTO 代碼。由 Nature 製作。
public enum TIME_COLLECTION_STATE {
	TIME_COLLECTION_NONE(0),                // 無時間收集
	TIME_COLLECTION_COMPLETE(1),             // 時間收集完成
	TIME_COLLECTION_BUFF_END(2),             // 時間收集增益結束
	TIME_COLLECTION_ADDITIONAL(3),           // 額外時間收集
	TIME_COLLECTION_ADDITIONAL_BUFF_END(4),  // 額外時間收集增益結束
	TIME_COLLECTION_END(5);                  // 時間收集結束

	private int value;

	// 枚舉的構造函數
	TIME_COLLECTION_STATE(int val) {
		value = val;
	}

	// 返回枚舉的整數值
	public int toInt() {
		return value;
	}

	// 比較兩個枚舉是否相等
	public boolean equals(TIME_COLLECTION_STATE v) {
		return value == v.value;
	}

	// 根據整數值返回對應的枚舉
	public static TIME_COLLECTION_STATE fromInt(int i) {
		switch (i) {
			case 0:
				return TIME_COLLECTION_NONE;
			case 1:
				return TIME_COLLECTION_COMPLETE;
			case 2:
				return TIME_COLLECTION_BUFF_END;
			case 3:
				return TIME_COLLECTION_ADDITIONAL;
			case 4:
				return TIME_COLLECTION_ADDITIONAL_BUFF_END;
			case 5:
				return TIME_COLLECTION_END;
			default:
				throw new IllegalArgumentException(String.format("無效的 TIME_COLLECTION_STATE，%d", i));
		}
	}
}