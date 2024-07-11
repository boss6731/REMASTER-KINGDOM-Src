package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO : 原始版本。大自然創造的。
public enum UserFormType {
	USER_FORM_0(0),
	USER_FORM_1(1),
	;

	private int value;

	UserFormType(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(UserFormType v) {
		return value == v.value;
	}

	public static UserFormType fromInt(int i) {
		switch (i) {
			case 0:
				return USER_FORM_0;
			case 1:
				return USER_FORM_1;
			default:
				throw new IllegalArgumentException(String.format("無效參數 UserFormType，%d", i));
		}
	}
}
