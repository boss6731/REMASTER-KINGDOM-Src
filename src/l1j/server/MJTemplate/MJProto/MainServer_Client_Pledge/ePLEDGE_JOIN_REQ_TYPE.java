package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

// TODO：此為自動生成的 PROTO 代碼，由 MJSoft 製作。
public enum ePLEDGE_JOIN_REQ_TYPE {
	// 即時加入
	ePLEDGE_JOIN_REQ_TYPE_IMMEDIATLY(0),
	// 確認後加入
	ePLEDGE_JOIN_REQ_TYPE_CONFIRMATION(1),
	// 密碼加入
	ePLEDGE_JOIN_REQ_TYPE_PASSWORD(2),
	// 無法加入
	ePLEDGE_JOIN_REQ_TYPE_IMPOSSIBLE(3),
	;

	private int value;

	ePLEDGE_JOIN_REQ_TYPE(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(ePLEDGE_JOIN_REQ_TYPE v) {
		return value == v.value;
	}

	public static ePLEDGE_JOIN_REQ_TYPE fromInt(int i) {
		switch (i) {
			case 0:
				return ePLEDGE_JOIN_REQ_TYPE_IMMEDIATLY;
			case 1:
				return ePLEDGE_JOIN_REQ_TYPE_CONFIRMATION;
			case 2:
				return ePLEDGE_JOIN_REQ_TYPE_PASSWORD;
			case 3:
				return ePLEDGE_JOIN_REQ_TYPE_IMPOSSIBLE;
			default:
				throw new IllegalArgumentException(String.format("無效的 ePLEDGE_JOIN_REQ_TYPE 參數: %d", i));
		}
	}
}