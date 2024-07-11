package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。由 MJSoft 製作。
public enum eMonsterBookV2RewardGrade {
	RG_NORMAL(1),
	RG_DRAGON(2),
	RG_HIGH_DRAGON(3);

	private int value;

	eMonsterBookV2RewardGrade(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(eMonsterBookV2RewardGrade v) {
		return value == v.value;
	}

	public static eMonsterBookV2RewardGrade fromInt(int i) {
		switch (i) {
			case 1:
				return RG_NORMAL;
			case 2:
				return RG_DRAGON;
			case 3:
				return RG_HIGH_DRAGON;
			default:
				return null;
		}
	}
}
