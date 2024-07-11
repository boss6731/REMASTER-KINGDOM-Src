package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。倫茨製造。
public enum eMonsterBookV2RewardType {
	BaseReward(1),
	ExtraReward(2);

	private int value;

	eMonsterBookV2RewardType(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(eMonsterBookV2RewardType v) {
		return value == v.value;
	}

	public static eMonsterBookV2RewardType fromInt(int i) {
		switch (i) {
			case 1:
				return BaseReward;
			case 2:
				return ExtraReward;
			default:
				return null;
		}
	}
}
