package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。大自然創造的。
public enum SmeltingStoneGrade {
	SG_NORMAL(1),
	SG_RARE(2),
	SG_HERO(3),
	SG_LEGENDARY(4),
	;

	private int value;

	SmeltingStoneGrade(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(SmeltingStoneGrade v) {
		return value == v.value;
	}

	public static SmeltingStoneGrade fromInt(int i) {
		switch (i) {
			case 1:
				return SG_NORMAL;
			case 2:
				return SG_RARE;
			case 3:
				return SG_HERO;
			case 4:
				return SG_LEGENDARY;
			default:
				throw new IllegalArgumentException(String.format("無效參數 SmeltingStoneGrade，%d", i));
		}
	}
}
