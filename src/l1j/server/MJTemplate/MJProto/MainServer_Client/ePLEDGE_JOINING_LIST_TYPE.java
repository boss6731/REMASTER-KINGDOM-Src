package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum ePLEDGE_JOINING_LIST_TYPE {
	ePLEDGE_JOINING_LIST_TYPE_USER(1),
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
				throw new IllegalArgumentException(String.format("無效參數 ePLEDGE_JOINING_LIST_TYPE，%d", i));
		}
	}
}
