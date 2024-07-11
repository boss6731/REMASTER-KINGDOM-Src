package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum TUTORIAL_RESULT {
	TUTORIAL_RESULT_SUCCESS(0),
	TUTORIAL_RESULT_FAIL(1),
	TUTORIAL_RESULT_INVALID_TYPE(2),
	TUTORIAL_RESULT_ALREADY_PLAYING(3),
	TUTORIAL_RESULT_CANT_FIND_TUTORIAL(4),
	;

	private int value;

	TUTORIAL_RESULT(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(TUTORIAL_RESULT v) {
		return value == v.value;
	}

	public static TUTORIAL_RESULT fromInt(int i) {
		switch (i) {
			case 0:
				return TUTORIAL_RESULT_SUCCESS;
			case 1:
				return TUTORIAL_RESULT_FAIL;
			case 2:
				return TUTORIAL_RESULT_INVALID_TYPE;
			case 3:
				return TUTORIAL_RESULT_ALREADY_PLAYING;
			case 4:
				return TUTORIAL_RESULT_CANT_FIND_TUTORIAL;
			default:
				throw new IllegalArgumentException(String.format("無效參數 TUTORIAL_RESULT，%d", i));
		}
	}
}
