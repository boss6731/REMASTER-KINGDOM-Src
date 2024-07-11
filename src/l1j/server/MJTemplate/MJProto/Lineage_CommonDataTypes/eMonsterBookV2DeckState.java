package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。由 MJSoft 製作。
public enum eMonsterBookV2DeckState {
	DS_IN_PROGRESS(1),
	DS_REQUEST_COMPLETE(2),
	DS_COMPLETED(3),
	DS_REQUEST_REWARD(4),
	DS_REWARDED(5);

	private int value;

	eMonsterBookV2DeckState(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(eMonsterBookV2DeckState v) {
		return value == v.value;
	}

	public static eMonsterBookV2DeckState fromInt(int i) {
		switch (i) {
			case 1:
				return DS_IN_PROGRESS;
			case 2:
				return DS_REQUEST_COMPLETE;
			case 3:
				return DS_COMPLETED;
			case 4:
				return DS_REQUEST_REWARD;
			case 5:
				return DS_REWARDED;
			default:
				return null;
		}
	}
}
