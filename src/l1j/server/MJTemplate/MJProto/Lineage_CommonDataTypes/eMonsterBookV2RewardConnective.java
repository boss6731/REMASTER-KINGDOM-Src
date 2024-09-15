package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public enum eMonsterBookV2RewardConnective{
	And(1),
	Or(2);
	private int value;
	eMonsterBookV2RewardConnective(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eMonsterBookV2RewardConnective v){
		return value == v.value;
	}
	public static eMonsterBookV2RewardConnective fromInt(int i){
		switch(i){
		case 1:
			return And;
		case 2:
			return Or;
		default:
			return null;
		}
	}
}
