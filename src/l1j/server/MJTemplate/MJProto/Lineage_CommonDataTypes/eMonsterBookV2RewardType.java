package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public enum eMonsterBookV2RewardType{
	BaseReward(1),
	ExtraReward(2);
	private int value;
	eMonsterBookV2RewardType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eMonsterBookV2RewardType v){
		return value == v.value;
	}
	public static eMonsterBookV2RewardType fromInt(int i){
		switch(i){
		case 1:
			return BaseReward;
		case 2:
			return ExtraReward;
		default:
			return null;
		}
	}
}
