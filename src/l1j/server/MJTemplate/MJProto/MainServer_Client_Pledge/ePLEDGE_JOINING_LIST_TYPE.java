package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

// TODO：此為自動生成的 PROTO 代碼，由 MJSoft 製作。
public enum ePLEDGE_JOINING_LIST_TYPE {
	// 用戶加入類型
	ePLEDGE_JOINING_LIST_TYPE_USER(1),
	// 公會加入類型
	ePLEDGE_JOINING_LIST_TYPE_PLEDGE(2),
	;

	private int value;

	ePLEDGE_JOINING_LIST_TYPE(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(ePLEDGE_JOINING_LIST_TYPE v) {
		return value == v.value;
	}

	public static ePLEDGE_JOINING_LIST_TYPE fromInt(int i) {
		switch (i) {
			case 1:
				return ePLEDGE_JOINING_LIST_TYPE_USER;
			case 2:
				return ePLEDGE_JOINING_LIST_TYPE_PLEDGE;
			default:
				throw new IllegalArgumentException(String.format("無效的 ePLEDGE_JOINING_LIST_TYPE 參數: %d", i));
		}
	}
}