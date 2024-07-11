package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。由 MJSoft 製作。
public enum Gender {
	MALE(0),
	FEMALE(1);

	private int value;

	Gender(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(Gender v) {
		return value == v.value;
	}

	public static Gender fromInt(int i) {
		switch (i) {
			case 0:
				return MALE;
			case 1:
				return FEMALE;
			default:
				return null;
		}
	}
}
