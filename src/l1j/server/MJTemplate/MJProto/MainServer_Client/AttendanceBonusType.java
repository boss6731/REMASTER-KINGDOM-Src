package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum AttendanceBonusType {
	UseItem(1),
	GiveItem(2);

	private int value;

	AttendanceBonusType(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(AttendanceBonusType v) {
		return value == v.value;
	}

	public static AttendanceBonusType fromInt(int i) {
		switch (i) {
			case 1:
				return UseItem;
			case 2:
				return GiveItem;
			default:
				return null;
		}
	}
}
