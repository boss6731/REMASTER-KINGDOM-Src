package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum eInviteListType {
	PLEDGE(1),
	FRIEND(2),
	RECENT(3),
	;

	private int value;

	eInviteListType(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(eInviteListType v) {
		return value == v.value;
	}

	public static eInviteListType fromInt(int i) {
		switch (i) {
			case 1:
				return PLEDGE;
			case 2:
				return FRIEND;
			case 3:
				return RECENT;
			default:
				throw new IllegalArgumentException(String.format("無效參數 eInviteListType，%d", i));
		}
	}
}
