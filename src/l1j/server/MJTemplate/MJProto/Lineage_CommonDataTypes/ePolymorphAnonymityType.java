package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。大自然創造的。
public enum ePolymorphAnonymityType {
	eNone(0),
	eNormal(1),
	eRandom(2),
	eSpecialChar(3),
	eRandomExceptOurTeam(4),
	;

	private int value;

	ePolymorphAnonymityType(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(ePolymorphAnonymityType v) {
		return value == v.value;
	}

	public static ePolymorphAnonymityType fromInt(int i) {
		switch (i) {
			case 0:
				return eNone;
			case 1:
				return eNormal;
			case 2:
				return eRandom;
			case 3:
				return eSpecialChar;
			case 4:
				return eRandomExceptOurTeam;
			default:
				throw new IllegalArgumentException(String.format("無效的參數 ePolymorphAnonymityType, %d", i));
		}
	}
}
