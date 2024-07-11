package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。由 MJSoft 製作。
public enum eMonsterBookV2DeckDifficulty {
	DD_EASY(1),
	DD_NORMAL(2),
	DD_HARD(3);

	private int value;

	eMonsterBookV2DeckDifficulty(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(eMonsterBookV2DeckDifficulty v) {
		return value == v.value;
	}

	public static eMonsterBookV2DeckDifficulty fromInt(int i) {
		switch (i) {
			case 1:
				return DD_EASY;
			case 2:
				return DD_NORMAL;
			case 3:
				return DD_HARD;
			default:
				return null;
		}
	}
}
