package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum TUTORIAL_TYPE {
	TUTORIAL_TYPE_NONE(0),
	TUTORIAL_TYPE_BEGINNER(1),
	;

	private int value;

	TUTORIAL_TYPE(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(TUTORIAL_TYPE v) {
		return value == v.value;
	}

	public static TUTORIAL_TYPE fromInt(int i) {
		switch (i) {
			case 0:
				return TUTORIAL_TYPE_NONE;
			case 1:
				return TUTORIAL_TYPE_BEGINNER;
			default:
				throw new IllegalArgumentException(String.format("無效參數 TUTORIAL_TYPE，%d", i));
		}
	}
}
