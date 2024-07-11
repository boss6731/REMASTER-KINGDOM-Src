package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。由 MJSoft 製作。
public enum ePLEDGE_JOIN_REQ_TYPE_COMMON {
	ePLEDGE_JOIN_REQ_TYPE_COMMON_IMMEDIATLY(0),
	ePLEDGE_JOIN_REQ_TYPE_COMMON_CONFIRMATION(1),
	ePLEDGE_JOIN_REQ_TYPE_COMMON_PASSWORD(2),
	ePLEDGE_JOIN_REQ_TYPE_COMMON_IMPOSSIBLE(3),
	;

	private int value;

	ePLEDGE_JOIN_REQ_TYPE_COMMON(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(ePLEDGE_JOIN_REQ_TYPE_COMMON v) {
		return value == v.value;
	}

	public static ePLEDGE_JOIN_REQ_TYPE_COMMON fromInt(int i) {
		switch (i) {
			case 0:
				return ePLEDGE_JOIN_REQ_TYPE_COMMON_IMMEDIATLY;
			case 1:
				return ePLEDGE_JOIN_REQ_TYPE_COMMON_CONFIRMATION;
			case 2:
				return ePLEDGE_JOIN_REQ_TYPE_COMMON_PASSWORD;
			case 3:
				return ePLEDGE_JOIN_REQ_TYPE_COMMON_IMPOSSIBLE;
			default:
				throw new IllegalArgumentException(String.format("無效的參數 ePLEDGE_JOIN_REQ_TYPE_COMMON, %d", i));
		}
	}
}
